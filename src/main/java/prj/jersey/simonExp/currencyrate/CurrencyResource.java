package prj.jersey.simonExp.currencyrate;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;

import com.opencsv.CSVReader;

import prj.jersey.simonExp.datas.CurrencyData;
import prj.jersey.simonExp.enums.CurrencyType;
import util.HibernateUtil;

@Path("CurrencyResource")
public class CurrencyResource {
    private static final String botcsvUrl = "http://rate.bot.com.tw/xrt/flcsv/0/";
    private static final String[] reportColName = { "Currency", "Rate", "Cash", "Spot" };

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String saveCurrency(
        @QueryParam("urlString") String urlString) {

        System.out.println("\nget file from:");
        System.out.println(urlString + "\n");

        CSVReader reader = null;
        File file = new File("csvfile");
        try {
            URL url = new URL(urlString);
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
            String dateFromUrl = parseDateOnUrl(urlString);

            while ((line = reader.readNext()) != null) {
                System.out.println(Arrays.toString(line));
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
                    CurrencyData buyingCash = new CurrencyData(
                        Integer.valueOf(CurrencyType.getCurrencyCodeByName(line[currNameIdx])),
                        line[buyingIdx],
                        "Cash",
                        new Timestamp(dateFormat.parse(dateFromUrl).getTime()),
                        line[currNameIdx],
                        Float.parseFloat(line[buyingCashIdx]),
                        urlString);
                    CurrencyData buyingSpot = new CurrencyData(
                        Integer.valueOf(CurrencyType.getCurrencyCodeByName(line[currNameIdx])),
                        line[buyingIdx],
                        "Spot",
                        new Timestamp(dateFormat.parse(dateFromUrl).getTime()),
                        line[currNameIdx],
                        Float.parseFloat(line[buyingSpotIdx]),
                        urlString);
                    CurrencyData sellingCash = new CurrencyData(
                        Integer.valueOf(CurrencyType.getCurrencyCodeByName(line[currNameIdx])),
                        line[sellingIdx],
                        "Cash",
                        new Timestamp(dateFormat.parse(dateFromUrl).getTime()),
                        line[currNameIdx],
                        Float.parseFloat(line[sellingCashIdx]),
                        urlString);
                    CurrencyData sellingSpot = new CurrencyData(
                        Integer.valueOf(CurrencyType.getCurrencyCodeByName(line[currNameIdx])),
                        line[sellingIdx],
                        "Spot",
                        new Timestamp(dateFormat.parse(dateFromUrl).getTime()),
                        line[currNameIdx],
                        Float.parseFloat(line[sellingSpotIdx]),
                        urlString);

                    HibernateUtil.basicCreate(buyingCash);
                    HibernateUtil.basicCreate(buyingSpot);
                    HibernateUtil.basicCreate(sellingCash);
                    HibernateUtil.basicCreate(sellingSpot);
                }

                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Done";
    }

    private String parseDateOnUrl(String url) {
        // url format:
        // http://rate.bot.com.tw/xrt/flcsv/0/2000-12-29?Lang=en-US
        String[] urlArr = url.split("/");

        return urlArr[urlArr.length - 1].split("\\?")[0];
    }
}
