package misc;

import id.base.app.mail.MailDestination;
import id.base.app.mail.MailDestinationLocator;
import id.base.app.mail.MailDestinationResolver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.BaseTest;

public class TestMailResolver extends BaseTest {
	
	static Logger logger = LoggerFactory.getLogger(TestMailResolver.class);

	MailDestinationLocator mailDestinationLocator ;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		mailDestinationLocator = (MailDestinationLocator)context.getBean("mailDestinationLocator");
	}
	
	
	public void testMerchantPartnerMailResolver() {
		  List<MailDestination> emails	= mailDestinationLocator.resolveMailDestination(MailDestinationResolver.MERCHANT_PARTNER, 1041L,null) ;
		  for (MailDestination email : emails) {
			   System.err.println(" resolve mail " + email.getEmailAddress());
		  }
	}
	
    public void testMerchantAdminMailResolver() {
    	List<MailDestination> emails	= mailDestinationLocator.resolveMailDestination(MailDestinationResolver.USER_HANDLE_MERCHANT, 1041L,null) ;
		  for (MailDestination email : emails) {
			  System.err.println(" resolve mail " + email.getEmailAddress());
		  }
	}
    
    public void testInitiatorMailResolver() {
    	  List<MailDestination> emails	= mailDestinationLocator.resolveMailDestination(MailDestinationResolver.INITIATOR, null,2650L) ;
		  for (MailDestination email : emails) {
			  System.err.println(" resolve mail " + email.getEmailAddress());
		  }
	}
	
    public void testLastActorMailResolver() {
    	  List<MailDestination> emails	= mailDestinationLocator.resolveMailDestination(MailDestinationResolver.LAST_ACTOR, null,2657L) ;
		  for (MailDestination email : emails) {
			  System.err.println(" resolve mail " + email.getEmailAddress());
		  }
	}
    
    public void testDirectMailResolver() {
  	  List<MailDestination> emails	= mailDestinationLocator.resolveMailDestination("leo@xxx.com", null,null) ;
		  for (MailDestination email : emails) {
			  System.err.println(" resolve mail " + email.getEmailAddress());
		  }
	}
    
    
}
