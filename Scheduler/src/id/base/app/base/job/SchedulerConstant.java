package id.base.app.base.job;

import id.base.app.business.job.PurgingForecastJob;

import java.util.HashMap;
import java.util.Map;

public class SchedulerConstant {
	public static Map<String,Class> mapping = new HashMap<String, Class>();
	static{
		mapping.put("PURGING_FORECAST_JOB_SCHEDULER", PurgingForecastJob.class);
	}
}
