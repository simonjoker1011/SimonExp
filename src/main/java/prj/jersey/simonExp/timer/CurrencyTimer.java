package prj.jersey.simonExp.timer;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import util.SchedulerUtil;

public class CurrencyTimer {

    public static final String timer_Name = "CURRENCY_TIMER";

    public static void init() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            SchedulerUtil.schedulers.add(scheduler);
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
