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
import util.CurrencyUtil;
import util.HibernateUtil;
import util.TimeUtil;

@Path("CurrencyResource")
public class CurrencyResource {
    private static final String botcsvUrl = "http://rate.bot.com.tw/xrt/flcsv/0/";
    private static final String postfixLang = "?Lang=en-US";
    private static final String[] reportColName = { "Currency", "Rate", "Cash", "Spot" };
    private static String beginDate = "2001-01-01";

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
                .setParameter("StartDate", TimeUtil.strToTimestamp(StartDate))
                .setParameter("EndDate", TimeUtil.strToTimestamp(EndDate))
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

            if (!TimeUtil.dateToStr(today).equals(TimeUtil.timestampToStr(config.getUpdateDate()))) {
                config.setUpdateDate(new Timestamp(today.toDateTimeAtStartOfDay().getMillis()));

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

    private Object constructSqlandExecute(Session session, String tbName, String MaxMin, String CurrencyName, String rate, String cashspot, Timestamp start,
        Timestamp end) {

        String sqlString = " SELECT " + MaxMin + "(price) FROM " + tbName
            + " c WHERE c.currName = :currName"
            + " AND c.rate = :rate"
            + " AND c.cashspot = :cashspot"
            + " AND c.rateDate >= :startDate"
            + " AND :endDate >= c.rateDate";

        return session.createQuery(sqlString)
            .setParameter("currName", CurrencyName)
            .setParameter("rate", rate)
            .setParameter("cashspot", cashspot)
            .setParameter("startDate", start)
            .setParameter("endDate", end)
            .getSingleResult();
    }

    private CurrencyInfo constructCurrencyInfo1(String currName, String rate, String cashspot, String BaseDate) throws ParseException {
        CurrencyInfo currencyInfo = CurrencyUtil.constructCurrencyInfo(currName, rate, cashspot);
        if (currencyInfo == null) {
            currencyInfo = new CurrencyInfo(CurrencyType.getCurrencyCodeByName(currName), rate, cashspot, currName, TimeUtil.strToTimestamp(BaseDate));
        }
        currencyInfo.setDateBase(TimeUtil.strToTimestamp(BaseDate));
        return currencyInfo;
    }

    private void constructCurrencyInfo2(Session session, CurrencyInfo currencyInfo, String rate, String cashspot, LocalDate baseLine) throws ParseException {
        LocalDate weekLine = new LocalDate(baseLine).minusWeeks(1);
        LocalDate monthLine = new LocalDate(baseLine).minusMonths(1);
        LocalDate seasonLine = new LocalDate(baseLine).minusMonths(3);
        LocalDate halfYearLine = new LocalDate(baseLine).minusMonths(6);
        LocalDate yearLine = new LocalDate(baseLine).minusYears(1);
        LocalDate fourYearLine = new LocalDate(baseLine).minusYears(4);
        LocalDate decadeLine = new LocalDate(baseLine).minusYears(10);
        String CurrencyName = currencyInfo.getCurrName();

        currencyInfo.setWeekHigh((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Max", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(weekLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setWeekLow((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Min", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(weekLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setMonthHigh((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Max", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(monthLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setMonthLow((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Min", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(monthLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setSeasonHigh((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Max", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(seasonLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setSeasonLow((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Min", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(seasonLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setHalfYearHigh((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Max", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(halfYearLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setHalfYearLow((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Min", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(halfYearLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setYearHigh((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Max", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(yearLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setYearLow((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Min", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(yearLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setFourYearHigh((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Max", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(fourYearLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setFourYearLow((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Min", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(fourYearLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setDecadeHigh((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Max", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(decadeLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setDecadeLow((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Min", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(TimeUtil.dateToStr(decadeLine)), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setHistoryHigh((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Max", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(beginDate), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
        currencyInfo.setHistoryLow((Float) constructSqlandExecute(session, CurrencyData.EntityName, "Min", CurrencyName, rate, cashspot,
            TimeUtil.strToTimestamp(beginDate), TimeUtil.strToTimestamp(TimeUtil.dateToStr(baseLine))));
    }

    @GET
    @Path("fetchHistory")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchHistory(
        @QueryParam("CurrencyName") String CurrencyName) {
        Session session = null;
        session = HibernateUtil.getHibernateSession();

        String queryString = "FROM " + CurrencyInfo.EntityName + " WHERE currName = :currName";

        return Response.ok(session.createQuery(queryString)
            .setParameter("currName", CurrencyName).list()).build();
    }

    @POST
    @Path("updateHistory")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateHistory(
        @QueryParam("CurrencyName") String CurrencyName,
        @QueryParam("BaseDate") String BaseDate) throws ParseException {

        Session session = null;
        session = HibernateUtil.getHibernateSession();

        LocalDate baseLine = TimeUtil.strToDate(BaseDate);

        CurrencyInfo buyingCash = constructCurrencyInfo1(CurrencyName, "Buying", "Cash", BaseDate);
        CurrencyInfo buyingSpot = constructCurrencyInfo1(CurrencyName, "Buying", "Spot", BaseDate);
        CurrencyInfo SellingCash = constructCurrencyInfo1(CurrencyName, "Selling", "Cash", BaseDate);
        CurrencyInfo SellingSpot = constructCurrencyInfo1(CurrencyName, "Selling", "Spot", BaseDate);

        try {
            constructCurrencyInfo2(session, buyingCash, "Buying", "Cash", baseLine);
            constructCurrencyInfo2(session, buyingSpot, "Buying", "Spot", baseLine);
            constructCurrencyInfo2(session, SellingCash, "Selling", "Cash", baseLine);
            constructCurrencyInfo2(session, SellingSpot, "Selling", "Spot", baseLine);

            session.beginTransaction();

            session.saveOrUpdate(buyingCash);
            session.saveOrUpdate(buyingSpot);
            session.saveOrUpdate(SellingCash);
            session.saveOrUpdate(SellingSpot);

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) {
                session.close();
            }
            e.printStackTrace();
            return Response.serverError().build();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        CurrencyInfo[] rtninfo = { buyingCash, buyingSpot, SellingCash, SellingSpot };
        return Response.ok(rtninfo).build();
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
