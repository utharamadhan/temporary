package id.base.app.business.job;

import id.base.app.mail.MailManager;
import id.base.app.service.user.IUserNotificationService;
import id.base.app.util.DateTimeFunction;
import id.base.app.valueobject.AppUser;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserPasswordExpiredEmailNotificationJob  extends QuartzTaskExecutorJobBean {
	
	private Logger logger= LoggerFactory.getLogger(UserPasswordExpiredEmailNotificationJob.class);
	private IUserNotificationService userNotificationService;
	private MailManager<AppUser> mailManager;
	
	
	public void setUserNotificationService(IUserNotificationService userNotificationService){
		this.userNotificationService=userNotificationService;
	}
	
	public void setMailManager(MailManager<AppUser> mailManager){
		this.mailManager=mailManager;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		logger.debug("Begin Execute UserPasswordExpiredEmailNotification at [{}]",DateTimeFunction.getCurrentDate());
		try{
			List<AppUser> notifyingUsers=(List<AppUser>)userNotificationService.findNotifiyingPasswordUser();
			/*for(AppUser appUser:notifyingUsers){
				if(!StringFunction.isEmpty(appUser.getEmail())){
					logger.debug("sending mail notification password expired to [{}].",appUser.getUserName()+"["+appUser.getEmail()+"]");
					getTaskExecutor().execute(new UserPasswordEmailNotificationTask(mailManager, appUser));
				}else{
					logger.debug("cannot find email address for user [{}] for notifying password expired.",appUser.getUserName());
				}
			}*/
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		logger.debug("end Execute UserPasswordExpiredEmailNotification at [{}]",DateTimeFunction.getCurrentDate());
	}
}
