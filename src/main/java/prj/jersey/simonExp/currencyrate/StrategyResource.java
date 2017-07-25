package prj.jersey.simonExp.currencyrate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("StrategyResource")
public class StrategyResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response testGet() {
        return Response.ok().entity("kkk123").build();
    }
}
