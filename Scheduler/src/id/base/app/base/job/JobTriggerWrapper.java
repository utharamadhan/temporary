package id.base.app.base.job;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

public class JobTriggerWrapper {
	private List<JobDetail> beans = new ArrayList<JobDetail>();
	private List<CronTrigger> crons = new ArrayList<CronTrigger>();
	
	public JobTriggerWrapper() {
		super();
	}
	public JobTriggerWrapper(List<JobDetail> beans, List<CronTrigger> crons) {
		super();
		this.beans = beans;
		this.crons = crons;
	}
	public List<JobDetail> getBeans() {
		return beans;
	}
	public void setBeans(List<JobDetail> beans) {
		this.beans = beans;
	}
	public List<CronTrigger> getCrons() {
		return crons;
	}
	public void setCrons(List<CronTrigger> crons) {
		this.crons = crons;
	}
	
	public void makeCronJob(String schedulerName, Class schedulerClass, String cronName, String cronExpression) throws ParseException{
		JobDetailFactoryBean bean = new JobDetailFactoryBean();
		bean.setBeanName(schedulerName);
		bean.setJobClass(schedulerClass);
		bean.setDurability(true);
		bean.setRequestsRecovery(true);
		bean.afterPropertiesSet();
		CronTriggerFactoryBean cron = new CronTriggerFactoryBean();
		cron.setBeanName(cronName);
		cron.setJobDetail(bean.getObject());
		cron.setCronExpression(cronExpression);
		cron.afterPropertiesSet();
		beans.add(bean.getObject());
		crons.add(cron.getObject());
	}
}
