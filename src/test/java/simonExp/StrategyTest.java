package simonExp;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hibernate.Session;
import org.junit.Test;

import prj.jersey.simonExp.currencyrate.CurrencyResource;
import prj.jersey.simonExp.currencyrate.StrategyResource;
import prj.jersey.simonExp.datas.CurrencyData;
import util.HibernateUtil;
import util.StrategyUtil;

public class StrategyTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(StrategyResource.class, CurrencyResource.class);
    }

    @Test
    public void test() {
        Response response = target("StrategyResource")
            .request()
            .post(Entity.entity(new Float[] { 1.1f, 3.14f, 6.28f, 3.29f }, MediaType.APPLICATION_JSON));

        HashMap<String, Double> rtnMap = response.readEntity(HashMap.class);

        System.out.println("ArithMean: " + rtnMap.get("ArithMean"));
        System.out.println("StandardDeviation: " + rtnMap.get("StandardDeviation"));
    }

    @Test
    public void test2() {
        List<CurrencyData> list = target("CurrencyResource")
            .queryParam("CurrencyName", "EUR")
            .queryParam("Rate", "Selling")
            .queryParam("CashSpot", "Spot")
            .queryParam("StartDate", "2017-06-28")
            .queryParam("EndDate", "2017-07-27")
            .request().get(new GenericType<List<CurrencyData>>() {
            });

        System.out.println("Total: " + list.size());
        List<Float> prices = list.stream().map(c -> c.getPrice()).collect(Collectors.toList());
        System.out.println(prices);

        double[] price_arr = prices.stream().mapToDouble(f -> f).toArray();

        System.out.println("Geometric Mean: " + new GeometricMean().evaluate(price_arr));
        System.out.println("Arithmetic Mean: " + new Mean().evaluate(price_arr));
        System.out.println(StrategyUtil.arithmeticMean(prices));
        System.out.println("Variance: " + new Variance().evaluate(price_arr));
        System.out.println("Standard Deviation: " + new StandardDeviation().evaluate(price_arr));
        System.out.println(StrategyUtil.standardDeviation(prices));

        System.out.println(StrategyUtil.arithmeticMean(prices) - StrategyUtil.standardDeviation(prices));
    }

    @Test
    public void test3() {

        File ff = new File("TestFile");

        File f = new File(HibernateUtil.hibernateCfgPath);
        if (!f.exists()) {
            System.out.println("no file");
        } else {
            System.out.println("file exists");
        }

        Session session = HibernateUtil.getHibernateSession();

    }
}
