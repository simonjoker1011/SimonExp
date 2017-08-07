package prj.jersey.simonExp.timerTasks;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;

import util.SchedulerUtil;

public class MasterTimerTask implements Job, Runnable {

    public static final String job_Name = "MASTER_TASK_JOB";
    public static final String trigger_Name = "MASTER_TASK_TRIGGER";

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        run();
    }

    public static SimpleScheduleBuilder getSchedule() {
        return SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(1).repeatForever();
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
        List<Scheduler> schedulers = SchedulerUtil.schedulers;
        for (Scheduler scheduler : schedulers) {
            try {
                System.out.println("---------------------------");

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
                System.out.println("-> Scheduler Hash: ");
                System.out.println(scheduler.hashCode());

                System.out.println("---------------------------");

            } catch (SchedulerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
