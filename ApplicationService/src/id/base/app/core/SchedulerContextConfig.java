package id.base.app.core;

import id.base.app.base.job.AutowiringSpringBeanJobFactory;
import id.base.app.base.job.JobTriggerWrapper;
import id.base.app.base.job.SchedulerConstant;
import id.base.app.service.parameter.IParameterService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public class SchedulerContextConfig {
	public static Logger LOGGER = LoggerFactory.getLogger(SchedulerContextConfig.class);
	
	private JobTriggerWrapper jobTriggerWrapper = new JobTriggerWrapper();
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	IParameterService parameterService;
	
	@Autowired
    private PlatformTransactionManager transactionManager;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Bean(name="schedulerFactory")
	public SchedulerFactoryBean getSchedulerFactory() throws ParseException{
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setTransactionManager(transactionManager);
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        factory.setJobFactory(jobFactory);
		constructJobTriggerWrappers(parameterService.findParameterPairValue("%JOB_SCHEDULER"));
		List<JobDetail> details = jobTriggerWrapper.getBeans();
		factory.setJobDetails(details.toArray(new JobDetail[details.size()]));
		List<CronTrigger> triggers = jobTriggerWrapper.getCrons();
		factory.setTriggers(triggers.toArray(new CronTrigger[triggers.size()]));
		factory.setDataSource(dataSource);
		factory.setOverwriteExistingJobs(true);
		factory.setQuartzProperties(getQuartzProperties());
		return factory;
	}
	
	private void constructJobTriggerWrappers(Map<String,String> cronMap) throws ParseException{
		for (Map.Entry<String, String> entry : cronMap.entrySet()) {
			LOGGER.info("making cron job for " + entry.getKey());
			if (SchedulerConstant.mapping.containsKey(entry.getKey())) {
				try {
					jobTriggerWrapper.makeCronJob(entry.getKey(), SchedulerConstant.mapping.get(entry.getKey()), entry.getKey()+"Cron", entry.getValue());
				} catch (Exception e) {
					LOGGER.error("Error occurred during preparing scheduler for " + entry.getKey());
				}
			} else {
				LOGGER.warn("No job mapping found for " + entry.getKey());
			}
		}
	}
	
	private Properties getQuartzProperties(){
		Properties props = new Properties();
		props.put("org.quartz.scheduler.instanceName","ClusteredScheduler");
		props.put("org.quartz.scheduler.instanceId","AUTO");
		props.put("org.quartz.jobStore.misfireThreshold","60000");
		props.put("org.quartz.jobStore.class","org.quartz.impl.jdbcjobstore.JobStoreTX");
		props.put("org.quartz.jobStore.driverDelegateClass","org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
		props.put("org.quartz.jobStore.tablePrefix","QRTZ_");
		props.put("org.quartz.jobStore.isClustered","true");
		props.put("org.quartz.threadPool.class","org.quartz.simpl.SimpleThreadPool");
		props.put("org.quartz.threadPool.threadCount","25");
		props.put("org.quartz.threadPool.threadPriority","5");
//		props.put("org.quartz.scheduler.skipUpdateCheck", "true");
		return props;
	}
}
