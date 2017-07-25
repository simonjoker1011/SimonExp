package simonExp;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import prj.jersey.simonExp.currencyrate.StrategyResource;

public class StrategyTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(StrategyResource.class);
    }

    @Test
    public void test() {
        System.out.println("HELLO TEST");

        System.out.println(target("StrategyResource").request().get(String.class));
    }
}
