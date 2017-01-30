package mail;

import id.base.app.SystemParameter;
import id.base.app.mail.MailManager;
import base.BaseTest;

public class TestMail extends BaseTest{
	MailManager mailManager;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		mailManager = (MailManager) getBean("mailManager");
	}
	
	public void testSendMail() {
		// first thing to do is to add the certificate to jdk keystore
		mailManager.sendMail(SystemParameter.EMAIL_SENDER, "alexander@it-mld-01.lokal", "Test Setting mail", "Mail content");
	}
	
	public void testSendSMIME() {
		
	}
}
