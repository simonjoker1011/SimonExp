package prj.jersey.simonExp.timerTasks;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CurrencyInfoTask implements Job {

    final static public String JobName = "CurrencyInfoJob";

    public CurrencyInfoTask() {
    }

    @Override
    public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
        // TODO Auto-generated method stub
        System.out.println("Say hi at: " + new Date().toString());
    }

}
