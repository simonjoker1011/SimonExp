package prj.jersey.simonExp.timerTasks;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;

import util.SchedulerUtil;

public class CurrencyInfoTask implements Job, Runnable {

    final static public String job_Name = "CURRENCY_INFO_JOB";
    public static final String trigger_Name = "CURRENCY_INFO_TRIGGER";

    @Override
    public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
        run();
    }

    public static SimpleScheduleBuilder getSchedule() {
        return SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever();
        // return SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(1).repeatForever();
    }

    public static JobDetail getJob() {
        return SchedulerUtil.constructJob(MasterTimerTask.class, job_Name);
    }

    public static Trigger getTrigger() {
        return SchedulerUtil.constructTrigger(getSchedule(), trigger_Name);
    }

    @Override
    public void run() {
        System.out.println("Say hi at: " + new Date().toString());
    }
}
