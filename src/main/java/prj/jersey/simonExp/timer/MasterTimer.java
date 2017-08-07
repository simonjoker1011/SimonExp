package prj.jersey.simonExp.timer;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import prj.jersey.simonExp.timerTasks.MasterTimerTask;
import util.SchedulerUtil;

public class MasterTimer {

    public static final String timer_Name = "MASTER_TASK";

    public static void init() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            SchedulerUtil.schedulers.add(scheduler);

            bindJobs(scheduler);
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void bindJobs(Scheduler scheduler) {
        JobDetail jobDetail = MasterTimerTask.getJob();
        Trigger trigger = MasterTimerTask.getTrigger();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
