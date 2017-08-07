package util;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class SchedulerUtil {
    // schedulers[0]: default timer
    public static List<Scheduler> schedulers;

    public static void init() {
        schedulers = new ArrayList<Scheduler>();
    }

    public static void addTimer(Scheduler scheduler) {
        schedulers.add(scheduler);
    }

    public static void deleteTimer(Scheduler scheduler) {
        schedulers.remove(scheduler);
    }

    public static Scheduler bindJobAndTrigger(Scheduler scheduler, JobDetail job, Trigger trigger) {
        return scheduler;
    }

    public static JobDetail constructJob(Class<? extends Job> jobClass, String identityName) {
        JobDetail job = JobBuilder
            .newJob(jobClass)
            .withIdentity(identityName)
            .build();
        return job;
    }

    public static SimpleScheduleBuilder constructSchedule() {
        return SimpleScheduleBuilder
            .simpleSchedule()
            .withIntervalInSeconds(5)
            .repeatForever();
    }

    public static Trigger constructTrigger(ScheduleBuilder<? extends Trigger> schedBuilder,
        String identityName) {
        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity(identityName)
            .startNow()
            .withSchedule(schedBuilder)
            .build();
        return trigger;
    }
}
