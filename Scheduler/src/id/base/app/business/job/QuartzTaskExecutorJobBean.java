package id.base.app.business.job;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 
 * @author anicka andry
 *
 */
public abstract class  QuartzTaskExecutorJobBean extends QuartzJobBean implements InitializingBean {

	private TaskExecutor taskExecutor ;

	protected TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(taskExecutor==null)
		   throw new RuntimeException("org.springframework.core.task.TaskExecutor implementation must be set in applicationContext.xml first ..");
		
	}

}
