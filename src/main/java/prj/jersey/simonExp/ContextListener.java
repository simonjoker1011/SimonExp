package prj.jersey.simonExp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import prj.jersey.simonExp.timer.MasterTimer;
import util.SchedulerUtil;

@WebListener()
public class ContextListener implements ServletContextListener {


    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("Context Destroyed!");
        System.out.println("Timer Size: "+SchedulerUtil.schedulers.size());
        SchedulerUtil.schedulers.forEach(s->{
          try {
            if (s.isStarted()) {
              s.shutdown();
            }
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        });
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("Context Initialized!");
        
        SchedulerUtil.init();
        MasterTimer.init();
        System.out.println("Timer Size: "+SchedulerUtil.schedulers.size());
    }

   
}
