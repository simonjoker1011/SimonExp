package prj.jersey.simonExp.currencyrate;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import prj.jersey.simonExp.timerTasks.CurrencyInfoTask;
import util.SchedulerUtil;

@Path("ConfigResource")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {

    @POST
    public Response switchCurrencyJob() throws SchedulerException {

        // default scheduler
        Scheduler scheduler = SchedulerUtil.schedulers.get(0);
        // default job group
        String job_group = SchedulerUtil.schedulers.get(0).getJobGroupNames().get(0);
        String job_key = CurrencyInfoTask.job_Name;
        JobKey jobKey = new JobKey(job_key, job_group);

        if (scheduler.getJobDetail(jobKey) != null) {
            scheduler.deleteJob(jobKey);
            System.out.println("Switch Off!");
        } else {
            scheduler.scheduleJob(CurrencyInfoTask.getJob(), CurrencyInfoTask.getTrigger());
            System.out.println("Switch On!");
        }
        return Response.ok().build();
    }
}
