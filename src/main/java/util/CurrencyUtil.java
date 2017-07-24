package util;

import javax.persistence.NoResultException;

import org.hibernate.Session;

import prj.jersey.simonExp.datas.CurrencyInfo;
import prj.jersey.simonExp.enums.CurrencyType;

public class CurrencyUtil {

    public static CurrencyInfo constructCurrencyInfo(String currName, String rate, String cashspot) {

        CurrencyInfo currencyInfo = null;
        Session session = null;
        session = HibernateUtil.getHibernateSession();

        String queryString =
            "FROM " + CurrencyInfo.EntityName +
                " c WHERE c.currCode = :currCode"
                + " AND c.rate = :rate"
                + " AND c.cashspot = :cashspot";
        try {
            currencyInfo = (CurrencyInfo) session.createQuery(queryString)
                .setParameter("currCode", CurrencyType.getCurrencyCodeByName(currName))
                .setParameter("rate", rate)
                .setParameter("cashspot", cashspot)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return currencyInfo;
    }
}
