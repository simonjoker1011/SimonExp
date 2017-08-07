package prj.jersey.simonExp.timerTasks;

import java.util.Arrays;
import java.util.List;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;

import util.SchedulerUtil;

public class MasterTimerTask implements Job {

  public static final String job_Name = "MASTER_TASK_JOB";
  public static final String trigger_Name = "MASTER_TASK_TRIGGER";

  @Override
  public void execute(JobExecutionContext arg0) throws JobExecutionException {
    // TODO Auto-generated method stub
    List<Scheduler> schedulers = SchedulerUtil.schedulers;
    for (Scheduler scheduler : schedulers) {
      try {
        System.out.println("---------------------------");
        System.out.println("-> Calendar Name: ");
        scheduler.getCalendarNames().forEach(n -> System.out.println(n));

        System.out.println("-> Context Keys: ");
        Arrays.asList(scheduler.getContext().getKeys()).forEach(n -> System.out.println(n));

        System.out.println("-> Execution Jobs: ");
        scheduler.getCurrentlyExecutingJobs().forEach(j -> {
          System.out.println(j.getFireInstanceId());
          System.out.println(j.getFireTime());
          System.out.println(j.getNextFireTime());
          System.out.println(j.getJobDetail().getKey().toString());
          System.out.println(j.getTrigger().getKey().toString());
        });
        System.out.println("-> Scheduler Id: ");
        System.out.println(scheduler.getSchedulerInstanceId());
        System.out.println("-> Scheduler Name: ");
        System.out.println(scheduler.getSchedulerName());
        System.out.println("---------------------------");
      } catch (SchedulerException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public static SimpleScheduleBuilder getSchedule() {
    return SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(1).repeatForever();
  }

}
