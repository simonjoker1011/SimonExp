package prj.jersey.simonExp.currencyrate;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import util.StrategyUtil;

@Path("StrategyResource")
public class StrategyResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatics(List<Float> prices) {
        HashMap rtnMap = new HashMap<>();

        rtnMap.put("ArithMean", StrategyUtil.arithmeticMean(prices));
        rtnMap.put("StandardDeviation", StrategyUtil.standardDeviation(prices));

        return Response.ok().entity(rtnMap).build();
    }
}
