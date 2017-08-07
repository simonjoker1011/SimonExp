package prj.jersey.simonExp.currencyrate;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("ConfigResource")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {

  @POST
  public Response switchCurrencyTimer(
      @QueryParam("turningOn") Boolean turningOn){
    
    
    
    return Response.ok().build(); 
  }
}
