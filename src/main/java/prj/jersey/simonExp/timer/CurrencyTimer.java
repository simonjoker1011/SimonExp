package prj.jersey.simonExp.timer;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import prj.jersey.simonExp.timerTasks.MasterTimerTask;
import util.SchedulerUtil;

public class CurrencyTimer {
  
  public static final String timer_Name = "CURRENCY_TIMER";
  public static void init() {
    try {
      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
      bindJobs(scheduler);
      scheduler.start();
      SchedulerUtil.schedulers.add(scheduler);
    } catch (SchedulerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void bindJobs(Scheduler scheduler) {
    SimpleScheduleBuilder scheduleBuilder =
        SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(1).repeatForever();
    JobDetail jobDetail =
        SchedulerUtil.constructJob(MasterTimerTask.class, MasterTimerTask.job_Name);
    Trigger trigger = SchedulerUtil.constructTrigger(scheduleBuilder, MasterTimerTask.trigger_Name);
    try {
      scheduler.scheduleJob(jobDetail, trigger);
    } catch (SchedulerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
