package prj.jersey.simonExp.currencyrate;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.joda.time.LocalDate;

import com.opencsv.CSVReader;

import prj.jersey.simonExp.datas.CurrencyConfig;
import prj.jersey.simonExp.datas.CurrencyData;
import prj.jersey.simonExp.datas.CurrencyInfo;
import prj.jersey.simonExp.enums.CurrencyType;
import util.HibernateUtil;
import util.TimeUtil;

@Path("CurrencyResource")
public class CurrencyResource {
    private static final String botcsvUrl = "http://rate.bot.com.tw/xrt/flcsv/0/";
    private static final String postfixLang = "?Lang=en-US";
    private static final String[] reportColName = { "Currency", "Rate", "Cash", "Spot" };

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchCurrency(
        @QueryParam("CurrencyName") String CurrencyName,
        @QueryParam("Rate") String Rate,
        @QueryParam("CashSpot") String CashSpot,
        @QueryParam("StartDate") String StartDate,
        @QueryParam("EndDate") String EndDate) {

        List<CurrencyData> currList = new ArrayList<>();
        Session session = null;
        String queryString =
            "FROM " + CurrencyData.EntityName +
                " c WHERE c.currName = :CurrencyName"
                + " AND c.rate = :rate"
                + " AND c.cashspot = :cashspot"
                + " AND c.rateDate >= :StartDate"
                + " AND :EndDate >= c.rateDate";

        try {
            session = HibernateUtil.getHibernateSession();

            currList = session.createQuery(queryString)
                .setParameter("CurrencyName", CurrencyName)
                .setParameter("rate", Rate)
                .setParameter("cashspot", CashSpot)
                .setParameter("StartDate", TimeUtil.strToDate(StartDate))
                .setParameter("EndDate", TimeUtil.strToDate(EndDate))
                .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return Response.ok().entity(currList).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String saveCurrency(
        @QueryParam("fileDate") String fileDate) {

        System.out.println("\nget file from:");
        System.out.println(botcsvUrl + fileDate + postfixLang + "\n");

        return fetchNprocessCsvFile(fileDate);
    }

    @POST
    @Path("savePeriodCurrency")
    @Produces(MediaType.TEXT_PLAIN)
    public String savePeriodCurrency(
        @QueryParam("startdate") String startDate,
        @QueryParam("enddate") String endDate) throws ParseException {

        LocalDate start = TimeUtil.strToDate(startDate);
        LocalDate end = TimeUtil.strToDate(endDate);

        for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
            System.out.println("import file: " + TimeUtil.dateToStr(date));
            fetchNprocessCsvFile(TimeUtil.dateToStr(date));
        }
        return "Done";
    }

    @POST
    @Path("updateTilToday")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTilToday() {

        Session session = null;
        session = HibernateUtil.getHibernateSession();

        // 1. updaate config
        LocalDate today = new LocalDate();
        CurrencyConfig config = null;
        try {
            config = (CurrencyConfig) session.createQuery("FROM " + CurrencyConfig.EntityName).getSingleResult();
            System.out.println(TimeUtil.dateToStr(today) + " <-----> " + TimeUtil.timestampToStr(config.getUpdateDate()));
            System.out.println(TimeUtil.dateToStr(today).equals(TimeUtil.timestampToStr(config.getUpdateDate())));

            if (!TimeUtil.dateToStr(today).equals(TimeUtil.timestampToStr(config.getUpdateDate()))) {
                config.setUpdateDate(new Timestamp(today.toDateTimeAtStartOfDay().getMillis()));
                System.out.println(TimeUtil.timestampToStr(config.getUpdateDate()));

                session.beginTransaction();
                session.update(config);
                session.getTransaction().commit();
            }

        } catch (Exception e) {
            System.out.println("update config fail");
            if (session != null) {
                session.close();
            }
            e.printStackTrace();
            return Response.serverError().build();
        }

        // 2. import datas til today
        try {
            System.out.println("Max date: " + TimeUtil.timestampToStr((Timestamp) session.createQuery("SELECT MAX(rateDate) FROM " + CurrencyData.EntityName).getSingleResult()));
            String fromDate = TimeUtil.timestampToStr((Timestamp) session.createQuery("SELECT MAX(rateDate) FROM " + CurrencyData.EntityName).getSingleResult());
            String toDate = TimeUtil.dateToStr(today.minusDays(1));
            savePeriodCurrency(fromDate, toDate);

        } catch (Exception e) {
            if (session != null) {
                session.close();
            }
            e.printStackTrace();
            return Response.serverError().build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("updateHistory")
    @Produces(MediaType.TEXT_PLAIN)
    public String updateHistory(
        @QueryParam("CurrencyName") String CurrencyName,
        @QueryParam("BaseDate") String BaseDate) {

        Session session = null;
        session = HibernateUtil.getHibernateSession();

        LocalDate baseLine = TimeUtil.strToDate(BaseDate);
        LocalDate weekLine = new LocalDate(baseLine).minusWeeks(1);
        LocalDate monthLine = new LocalDate(baseLine).minusMonths(1);
        LocalDate seasonLine = new LocalDate(baseLine).minusMonths(3);
        LocalDate halfYearLine = new LocalDate(baseLine).minusMonths(6);
        LocalDate yearLine = new LocalDate(baseLine).minusYears(1);
        LocalDate fourYearLine = new LocalDate(baseLine).minusYears(4);
        LocalDate decadeLine = new LocalDate(baseLine).minusYears(10);

        System.out.println(TimeUtil.dateToStr(baseLine));
        System.out.println(TimeUtil.dateToStr(weekLine));
        System.out.println(TimeUtil.dateToStr(monthLine));
        System.out.println(TimeUtil.dateToStr(seasonLine));
        System.out.println(TimeUtil.dateToStr(halfYearLine));
        System.out.println(TimeUtil.dateToStr(yearLine));
        System.out.println(TimeUtil.dateToStr(fourYearLine));
        System.out.println(TimeUtil.dateToStr(decadeLine));

        CurrencyInfo buyingCash = new CurrencyInfo();
        CurrencyInfo buyingSpot = new CurrencyInfo();
        CurrencyInfo SellingCash = new CurrencyInfo();
        CurrencyInfo SellingSpot = new CurrencyInfo();

        // try {
        // System.out.println("Max date: " + TimeUtil.timestampToStr((Timestamp) session.createQuery("SELECT MAX(rateDate) FROM
        // " + CurrencyData.EntityName).getSingleResult()));
        // session.createQuery("SELECT MAX(rateDate) FROM " + CurrencyData.EntityName).getSingleResult();
        // } catch (Exception e) {
        // if (session != null) {
        // session.close();
        // }
        // e.printStackTrace();
        // return "Failed";
        // }

        return "Done";
    }

    private String fetchNprocessCsvFile(String parseDate) {
        CSVReader reader = null;
        File file = new File("csvfile");
        String fileUrl = botcsvUrl + parseDate + postfixLang;
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

            while ((line = reader.readNext()) != null) {
                if (count == 0) {
                    if ("Can not find any data for this inquiry".equals(Arrays.toString(line))) {
                        System.out.println("No datas for date: " + parseDate);
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
                    Timestamp rateDate = TimeUtil.strToTimestamp(parseDate);
                    if (currCode == -1) {
                        System.out.println("Do not handle currency: " + line[currNameIdx]);
                        count++;
                        continue;
                    }

                    CurrencyData buyingCash = new CurrencyData(
                        currCode,
                        line[buyingIdx],
                        "Cash",
                        rateDate,
                        line[currNameIdx],
                        Float.parseFloat(line[buyingCashIdx]),
                        fileUrl);
                    CurrencyData buyingSpot = new CurrencyData(
                        currCode,
                        line[buyingIdx],
                        "Spot",
                        rateDate,
                        line[currNameIdx],
                        Float.parseFloat(line[buyingSpotIdx]),
                        fileUrl);
                    CurrencyData sellingCash = new CurrencyData(
                        currCode,
                        line[sellingIdx],
                        "Cash",
                        rateDate,
                        line[currNameIdx],
                        Float.parseFloat(line[sellingCashIdx]),
                        fileUrl);
                    CurrencyData sellingSpot = new CurrencyData(
                        currCode,
                        line[sellingIdx],
                        "Spot",
                        rateDate,
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
}
