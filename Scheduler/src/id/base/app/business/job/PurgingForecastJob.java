package id.base.app.business.job;

import id.base.app.base.job.BaseJob;
import id.base.app.exception.SystemException;
import id.base.app.service.forecast.call.IForecastCallService;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/**
 * @author Utha
 */
@DisallowConcurrentExecution
public class PurgingForecastJob extends BaseJob {
	
	public static Logger LOGGER = LoggerFactory.getLogger(PurgingForecastJob.class);
	
	@Autowired
	private IForecastCallService forecastCallService;
	
	@Override
	public String executeJob(JobExecutionContext context) throws SystemException {
		try {
			forecastCallService.purgingForecastData();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return "Purging Forecast Job";
	}
	
	@Override
	public String getJobName() {
		return "PURGING FORECAST JOB";
	}

}
