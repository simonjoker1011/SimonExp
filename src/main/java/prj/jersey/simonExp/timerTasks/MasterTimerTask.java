package prj.jersey.simonExp.timerTasks;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import util.SchedulerUtil;

public class MasterTimerTask implements Job, Runnable {

    public static final String job_Name = "MASTER_TASK_JOB";
    public static final String trigger_Name = "MASTER_TASK_TRIGGER";

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        run();
    }

    public static SimpleScheduleBuilder getSchedule() {
        return SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(1).repeatForever();
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
        System.out.println();
        System.out.println("Master task fired at: " + new Date().toString());
        List<Scheduler> schedulers = SchedulerUtil.schedulers;
        for (Scheduler sched : schedulers) {
            try {
                System.out.println("----------------- Jobs -------------------");
                for (String group : sched.getJobGroupNames()) {
                    // enumerate each job in group
                    for (JobKey jobKey : sched.getJobKeys(GroupMatcher.groupEquals(group))) {
                        System.out.println("Found job identified by: " + jobKey);

                        for (Trigger trigger : sched.getTriggersOfJob(jobKey)) {
                            System.out.println("    \\-------> With triggers: " + trigger.getKey());
                            System.out.println("        \\-------> With next fired at: " + trigger.getNextFireTime().toString());
                        }
                    }
                }
                System.out.println("----------------- Triggers -------------------");
                for (String group : sched.getTriggerGroupNames()) {
                    // enumerate each trigger in group
                    for (TriggerKey triggerKey : sched.getTriggerKeys(GroupMatcher.groupEquals(group))) {
                        System.out.println("Found trigger identified by: " + triggerKey);
                    }
                }
            } catch (SchedulerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println();
    }

}
