package util;

import org.hibernate.Session;
import org.joda.time.LocalDate;

import prj.jersey.simonExp.datas.CurrencyData;
import prj.jersey.simonExp.datas.CurrencyInfo;

public class CurrencyUtil {

    public static CurrencyInfo constructCurrencyInfo(String currName, LocalDate baseLine, LocalDate weekLine, LocalDate monthLine, LocalDate seasonLine, LocalDate halfYearLine,
        LocalDate yearLine, LocalDate fourYearLine, LocalDate decadeLine) {

        CurrencyInfo currencyInfo = new CurrencyInfo();
        Session session = null;
        session = HibernateUtil.getHibernateSession();

        String queryString =
            "FROM " + CurrencyData.EntityName +
                " c WHERE c.currName = :CurrencyName"
                + " AND c.rate = :rate"
                + " AND c.cashspot = :cashspot"
                + " AND c.rateDate >= :StartDate"
                + " AND :EndDate >= c.rateDate";

        try {

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return currencyInfo;
    }
}
