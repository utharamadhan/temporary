package id.base.app.business.job;

import id.base.app.base.job.BaseJob;
import id.base.app.exception.SystemException;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
/**
 * @author Utha
 */
@DisallowConcurrentExecution
public class TestSchedulerJob extends BaseJob {
	public static Logger LOGGER = LoggerFactory.getLogger(TestSchedulerJob.class);
	
	@Override
	public String executeJob(JobExecutionContext context) throws SystemException {
		return "Test Scheduler Job";
	}
	
	@Override
	public String getJobName() {
		return "TEST SCHEDULER JOB";
	}

}
