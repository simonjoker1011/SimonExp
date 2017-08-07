package prj.jersey.simonExp.timerTasks;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import prj.jersey.simonExp.currencyrate.CurrencyResource;
import util.SchedulerUtil;

public class CurrencyInfoTask implements Job, Runnable {

    final static public String job_Name = "CURRENCY_INFO_JOB";
    public static final String trigger_Name = "CURRENCY_INFO_TRIGGER";

    @Override
    public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
        run();
    }

    public static CronScheduleBuilder getSchedule() {
        String exp = "0 0 0 1/1 * ? *";

        return CronScheduleBuilder.cronSchedule(exp);
        // return SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(1).repeatForever();
    }

    public static JobDetail getJob() {
        return SchedulerUtil.constructJob(CurrencyInfoTask.class, job_Name);
    }

    public static Trigger getTrigger() {
        return SchedulerUtil.constructTrigger(getSchedule(), trigger_Name);
    }

    @Override
    public void run() {
        System.out.println("Currency timer fired at: " + new Date().toString());
        CurrencyResource.updateTilToday();
    }
}
