package id.base.app.base.job;

import id.base.app.service.MaintenanceService;
import id.base.app.valueobject.monitoring.JobMonitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public abstract class BaseJob extends QuartzJobBean {
	private static Logger logger = LoggerFactory.getLogger(BaseJob.class);
	
	@Autowired
	MaintenanceService<JobMonitor> jobMonitorService;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("Starting scheduled job [" + getJobName() + "] at " + new Date());
		long startTime = System.currentTimeMillis();
		JobMonitor jobMonitor = new JobMonitor();
		jobMonitor.setName(getJobName());
		jobMonitor.setStartTime(new Date());
		jobMonitorService.saveOrUpdate(jobMonitor);
		String ipAddress = "";
		try {
			ipAddress = ". Executed on " + InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			// do nothing 
		}
		try {
			String remark = executeJob(context);
			jobMonitor.setStatus(JobMonitor.STATUS_OK);
			jobMonitor.setRemark(remark + ipAddress);
		} catch (Exception e) {
			e.printStackTrace();
			jobMonitor.setStatus(JobMonitor.STATUS_ERROR);
			StringWriter writer = new StringWriter();
			PrintWriter pw = new PrintWriter(writer);
			e.printStackTrace(pw);
			jobMonitor.setRemark(writer.toString() + ipAddress);
		} finally {
			jobMonitor.setEndTime(new Date());
			long endTime = System.currentTimeMillis();
			jobMonitor.setDuration(endTime - startTime);
			jobMonitorService.saveOrUpdate(jobMonitor);
		}
		logger.info("Finishing scheduled job [" + getJobName() + "] at " + new Date());
	}
	
	/**
	 * Job implementation should be put inside this method
	 * @param context
	 * @return error/remark message if any
	 * @throws Exception 
	 */
	public abstract String executeJob(JobExecutionContext context) throws Exception;
	
	/**
	 * Returns job name
	 * @return job name
	 */
	public abstract String getJobName();

}
