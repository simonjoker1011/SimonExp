package prj.jersey.simonExp.currencyrate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import util.StrategyUtil;

@Path("StrategyResource")
@Consumes(MediaType.APPLICATION_JSON)
public class StrategyResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatics(
        List<Float> prices) {
        HashMap<String, Double> rtnMap = new HashMap<>();

        rtnMap.put("ArithMean", StrategyUtil.arithmeticMean(prices));
        rtnMap.put("StandardDeviation", StrategyUtil.standardDeviation(prices));

        return Response.ok().entity(rtnMap).build();
    }

    @POST
    @Path("accumulated")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccumulatedStatics(
        List<Float> prices) {
        HashMap<String, List<Double>> rtnMap = new HashMap<>();
        List<Double> arithmeans = new ArrayList<>();
        List<Double> standarddeviations = new ArrayList<>();

        for (int i = 0; i < prices.size(); i++) {
            List<Float> subprices = prices.subList(0, i + 1);
            arithmeans.add(StrategyUtil.arithmeticMean(subprices));
            standarddeviations.add(StrategyUtil.standardDeviation(subprices));
        }

        rtnMap.put("accuMean", arithmeans);
        rtnMap.put("accuSD", standarddeviations);
        return Response.ok().entity(rtnMap).build();
    }

}
