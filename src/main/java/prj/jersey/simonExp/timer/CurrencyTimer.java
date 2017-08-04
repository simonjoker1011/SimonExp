package prj.jersey.simonExp.timer;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import prj.jersey.simonExp.ContextListener;

public class CurrencyTimer {

    public static Scheduler timer;

    public static void init() throws SchedulerException {
        timer = StdSchedulerFactory.getDefaultScheduler();
        ContextListener.schedulers.add(timer);
    }
}
