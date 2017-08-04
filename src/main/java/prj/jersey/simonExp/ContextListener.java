package prj.jersey.simonExp;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import prj.jersey.simonExp.timer.CurrencyTimer;
import prj.jersey.simonExp.timerTasks.CurrencyInfoTask;

@WebListener()
public class ContextListener implements ServletContextListener {

    public static List<Scheduler> schedulers;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("Context Destroyed!");
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("Context Initialized!");
        schedulers = new ArrayList<Scheduler>();
        try {
            CurrencyTimer.init();

            // Grab the Scheduler instance from the Factory
            // Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            //
            // bindJobs(scheduler);

            // and start it off
            // scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void bindJobs(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(CurrencyInfoTask.class)
            .withIdentity(CurrencyInfoTask.JobName)
            .build();
        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("Trigger1")
            .startNow()
            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)
                .repeatForever())
            .build();
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
