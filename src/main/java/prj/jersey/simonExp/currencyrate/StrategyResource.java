package prj.jersey.simonExp.currencyrate;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;

import prj.jersey.simonExp.datas.CurrencyData;
import util.HibernateUtil;
import util.TimeUtil;

@Path("StrategyResource")
public class StrategyResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response testGet() {

        String CurrencyName = "USD";
        String Rate = "Buying";
        String CashSpot = "Spot";
        String StartDate = "2017-06-01";
        String EndDate = "2017-07-01";

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
}
