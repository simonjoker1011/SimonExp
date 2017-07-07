package prj.jersey.simonExp.currencyrate;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.persistence.PersistenceException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.opencsv.CSVReader;

import prj.jersey.simonExp.datas.CurrencyData;
import prj.jersey.simonExp.enums.CurrencyType;
import util.HibernateUtil;

@Path("CurrencyResource")
public class CurrencyResource {
    private static final String botcsvUrl = "http://rate.bot.com.tw/xrt/flcsv/0/";
    private static final String postfixLang = "?Lang=en-US";
    private static final String[] reportColName = { "Currency", "Rate", "Cash", "Spot" };

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String saveCurrency(
        @QueryParam("urlString") String urlString) {

        System.out.println("\nget file from:");
        System.out.println(urlString + "\n");

        return fetchNprocessCsvFile(urlString);
    }

    @POST
    @Path("savePeriodCurrency")
    @Produces(MediaType.TEXT_PLAIN)
    public String savePeriodCurrency(
        @QueryParam("startdate") String startDate,
        @QueryParam("enddate") String endDate) throws ParseException {

        LocalDate start = strToDate(startDate);
        LocalDate end = strToDate(endDate);

        for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
            System.out.println("import file: " + dateToStr(date));
            fetchNprocessCsvFile(botcsvUrl + dateToStr(date) + postfixLang);
        }
        return "Done";
    }

    private LocalDate strToDate(String dateStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        return dateTimeFormatter.parseLocalDate(dateStr);
    }

    private String dateToStr(LocalDate date) {
        return date.toString(DateTimeFormat.forPattern("yyyy-MM-dd"));
    }

    private String fetchNprocessCsvFile(String fileUrl) {
        CSVReader reader = null;
        File file = new File("csvfile");
        try {
            URL url = new URL(fileUrl);
            FileUtils.copyURLToFile(url, file);
            reader = new CSVReader(new FileReader(file));
            String[] line;
            Integer count = 0;
            Integer currNameIdx = -1;

            Integer buyingIdx = -1;
            Integer buyingCashIdx = -1;
            Integer buyingSpotIdx = -1;

            Integer sellingIdx = -1;
            Integer sellingCashIdx = -1;
            Integer sellingSpotIdx = -1;

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateFromUrl = parseDateOnUrl(fileUrl);

            while ((line = reader.readNext()) != null) {
                if (count == 0) {
                    if ("Can not find any data for this inquiry".equals(Arrays.toString(line))) {
                        return "No datas for date: " + dateFromUrl;
                    }
                    // hardcode for BOM issue
                    line[0] = line[0].substring(1);
                    for (int i = 0; i < line.length - 1; i++) {
                        switch (line[i]) {
                            case "Currency":
                                currNameIdx = i;
                                break;
                            case "Rate":
                                if (buyingIdx == -1) {
                                    buyingIdx = i;
                                } else {
                                    sellingIdx = i;
                                }
                                break;
                            case "Cash":
                                if (buyingCashIdx == -1) {
                                    buyingCashIdx = i;
                                } else {
                                    sellingCashIdx = i;
                                }
                                break;
                            case "Spot":
                                if (buyingSpotIdx == -1) {
                                    buyingSpotIdx = i;
                                } else {
                                    sellingSpotIdx = i;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                } else {

                    Integer currCode = CurrencyType.getCurrencyCodeByName(line[currNameIdx]);
                    if (currCode == -1) {
                        System.out.println("Do not handle currency: " + line[currNameIdx]);
                        count++;
                        continue;
                    }

                    CurrencyData buyingCash = new CurrencyData(
                        currCode,
                        line[buyingIdx],
                        "Cash",
                        new Timestamp(dateFormat.parse(dateFromUrl).getTime()),
                        line[currNameIdx],
                        Float.parseFloat(line[buyingCashIdx]),
                        fileUrl);
                    CurrencyData buyingSpot = new CurrencyData(
                        currCode,
                        line[buyingIdx],
                        "Spot",
                        new Timestamp(dateFormat.parse(dateFromUrl).getTime()),
                        line[currNameIdx],
                        Float.parseFloat(line[buyingSpotIdx]),
                        fileUrl);
                    CurrencyData sellingCash = new CurrencyData(
                        currCode,
                        line[sellingIdx],
                        "Cash",
                        new Timestamp(dateFormat.parse(dateFromUrl).getTime()),
                        line[currNameIdx],
                        Float.parseFloat(line[sellingCashIdx]),
                        fileUrl);
                    CurrencyData sellingSpot = new CurrencyData(
                        currCode,
                        line[sellingIdx],
                        "Spot",
                        new Timestamp(dateFormat.parse(dateFromUrl).getTime()),
                        line[currNameIdx],
                        Float.parseFloat(line[sellingSpotIdx]),
                        fileUrl);

                    try {
                        HibernateUtil.basicCreate(buyingCash);
                        HibernateUtil.basicCreate(buyingSpot);
                        HibernateUtil.basicCreate(sellingCash);
                        HibernateUtil.basicCreate(sellingSpot);
                    } catch (PersistenceException e) {
                        if (e.getMessage().equals("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
                            System.out.println("Create fail for " + line[currNameIdx]);
                        }
                    }
                }
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Process file from " + fileUrl + " is done";
    }

    private String parseDateOnUrl(String url) {
        // url format:
        // http://rate.bot.com.tw/xrt/flcsv/0/2000-12-29?Lang=en-US
        String[] urlArr = url.split("/");

        return urlArr[urlArr.length - 1].split("\\?")[0];
    }
}
