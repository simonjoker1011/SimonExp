package simonExp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;

import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import prj.jersey.simonExp.currencyrate.CurrencyResource;
import prj.jersey.simonExp.currencyrate.StrategyResource;
import prj.jersey.simonExp.datas.CurrencyData;
import util.StrategyUtil;

public class StrategyTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(StrategyResource.class, CurrencyResource.class);
    }

    @Test
    public void test() {
        List resp = target("StrategyResource").request().get(List.class);
        resp = new ArrayList<>();
        System.out.println(resp.size());
    }

    @Test
    public void test2() {
        List<CurrencyData> list = target("CurrencyResource")
            .queryParam("CurrencyName", "USD")
            .queryParam("Rate", "Buying")
            .queryParam("CashSpot", "Spot")
            .queryParam("StartDate", "2017-06-01")
            .queryParam("EndDate", "2017-06-15")
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
    }
}
