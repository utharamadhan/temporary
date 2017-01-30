package id.base.app.webMember;

import id.base.app.LoginSession;
import id.base.app.SystemParameter;
import id.base.app.config.BeanUtilsConfigurer;
import id.base.app.rest.RestCaller;
import id.base.app.rest.RestConstant;
import id.base.app.rest.RestServiceConstant;
import id.base.app.valueobject.AppFunction;
import id.base.app.valueobject.AppParameter;
import id.base.app.valueobject.AppRole;
import id.base.app.valueobject.AppUser;
import id.base.app.valueobject.LogAuditTrail;
import id.base.app.valueobject.Lookup;
import id.base.app.valueobject.LookupAddress;
import id.base.app.valueobject.LookupAddressGroup;
import id.base.app.valueobject.LookupGroup;
import id.base.app.valueobject.MasterAddress;
import id.base.app.valueobject.RuntimeUserLogin;
import id.base.app.valueobject.party.Party;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jasypt.digest.config.SimpleDigesterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class ParameterLoader extends ContextLoader implements ServletContextListener  {
	
	private Logger logger=LoggerFactory.getLogger(ParameterLoader.class);

	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
	
	public ParameterLoader() {
		super();
	}
	
	public ParameterLoader(WebApplicationContext context) {
		super(context);
	}

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext servletContext=servletContextEvent.getServletContext();

		WebApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		
		/*initializeAccessibility(ctx);
		
		initializeDigester(ctx);
		
		initializeLdap(ctx);*/
		
		initializeBeanUtils();
		
		/*initializeMailSender(ctx);*/
		
		initializeRest();
	}

	private void initializeMailSender(WebApplicationContext ctx) {
		logger.info("Start initializing mail sender... ");
		JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
		mailSender.setHost(SystemParameter.MAIL_HOST);
		mailSender.setPort(SystemParameter.MAIL_PORT);
		mailSender.setUsername(SystemParameter.MAIL_USERNAME);
		mailSender.setPassword(SystemParameter.MAIL_PASSWORD);
		logger.info("Finish initializing mail sender... ");
	}

	private void initializeBeanUtils() {
		logger.info("Start initializing beanutils... ");
		BeanUtilsConfigurer.configure();
		logger.info("Finish initializing beanutils... ");
	}

	private void initializeLdap(WebApplicationContext ctx) {
		/*logger.info("Start initializing ldap context... ");
		LdapTemplate ldapTemplate = (LdapTemplate) ctx.getBean(RestServiceConstant.LDAP_TEMPLATE);
		ContextSource contextSource = ldapTemplate.getContextSource();
		LdapContextSource ldapContext = (LdapContextSource) ctx.getBean(RestServiceConstant.LDAP_CONTEXT);
		ldapContext.setUrl(SystemParameter.LDAP_URL);
		ldapContext.setUserDn(SystemParameter.LDAP_USER_DN);
		ldapContext.setBase(SystemParameter.LDAP_BASE);
		
		StringEncryptor encryptor = (StringEncryptor) ctx.getBean(RestServiceConstant.ENCRYPTOR);
		ldapContext.setPassword(encryptor.decrypt(SystemParameter.LDAP_PASSWORD));
		
		logger.info("Finish initializing ldap context... ");*/
	}

	private void initializeDigester(WebApplicationContext ctx) {
		logger.info("Start initializing digester... ");
//		StandardStringDigester digester = (StandardStringDigester) ctx.getBean(RestServiceConstant.DIGESTER);
		SimpleDigesterConfig config = (SimpleDigesterConfig) ctx.getBean("digesterConfig");
		config.setAlgorithm(SystemParameter.DIGESTER_ALGORITHM);
//		digester.setConfig(config);
		//digester.initialize();
		logger.info("Finish initializing digester... ");
	}

	private void initializeAccessibility(WebApplicationContext ctx) {
		logger.info("Start loading Accesibility Constants .. ");
		//TODO find a way to initializeAccessibility
		/*IAppFunctionService appFunctionService = (IAppFunctionService) ctx
				.getBean(RestServiceConstant.APP_FUNCTION_SERVICE);

		List<AppFunction> appFunctions = appFunctionService.findAllAppFunction();
		
		for (AppFunction appFunction : appFunctions) {
			if(appFunction!=null){
			logger.debug("Setting appfunction [{}] with functionNumber [{}] ",
						appFunction.getDescr(), appFunction.getPkAppFunction());
				IAccessibilityConstant.synchronizeFunction(appFunction.getDescr(),
						appFunction.getPkAppFunction());
			}
		}*/
		
		logger.info("Finish loading Accesibility Constants .. ");
	}

	private void initializeRest() {
		RestCaller.BASE_URL.put(RestServiceConstant.APP_FUNCTION_SERVICE, RestConstant.RM_APP_FUNCTION);
		RestCaller.BASE_CLASS.put(RestServiceConstant.APP_FUNCTION_SERVICE, AppFunction.class);
		RestCaller.BASE_URL.put(RestServiceConstant.SYSTEM_PARAMETER_SERVICE, RestConstant.RM_SYSTEM_PARAMETER);
		RestCaller.BASE_CLASS.put(RestServiceConstant.SYSTEM_PARAMETER_SERVICE, AppParameter.class);
		RestCaller.BASE_URL.put(RestServiceConstant.SECURITY_PARAMETER_SERVICE, RestConstant.RM_SECURITYPARAM);
		RestCaller.BASE_CLASS.put(RestServiceConstant.SECURITY_PARAMETER_SERVICE, AppParameter.class);
		RestCaller.BASE_URL.put(RestServiceConstant.USER_SERVICE, RestConstant.RM_USER);
		RestCaller.BASE_CLASS.put(RestServiceConstant.USER_SERVICE, AppUser.class);
		RestCaller.BASE_URL.put(RestServiceConstant.AUTHENTICATION, RestConstant.RM_AUTHENTICATION);
		RestCaller.BASE_CLASS.put(RestServiceConstant.AUTHENTICATION, LoginSession.class);
		RestCaller.BASE_URL.put(RestServiceConstant.ROLE_SERVICE, RestConstant.RM_ROLE);
		RestCaller.BASE_CLASS.put(RestServiceConstant.ROLE_SERVICE, AppRole.class);
		RestCaller.BASE_URL.put(RestServiceConstant.ROLE_SERVICE, RestConstant.RM_ROLE);
		RestCaller.BASE_CLASS.put(RestServiceConstant.ROLE_SERVICE, AppRole.class);
		RestCaller.BASE_URL.put(RestServiceConstant.AUDIT_TRAIL_SERVICE, RestConstant.RM_AUDIT);
		RestCaller.BASE_CLASS.put(RestServiceConstant.AUDIT_TRAIL_SERVICE, LogAuditTrail.class);
		RestCaller.BASE_URL.put(RestServiceConstant.LOGIN_SERVICE, RestConstant.RM_USER_LOGIN);
		RestCaller.BASE_CLASS.put(RestServiceConstant.LOGIN_SERVICE, RuntimeUserLogin.class);
		
		RestCaller.BASE_URL.put(RestServiceConstant.LOOKUP_SERVICE, RestConstant.RM_LOOKUP);
		RestCaller.BASE_CLASS.put(RestServiceConstant.LOOKUP_SERVICE, Lookup.class);
		RestCaller.BASE_URL.put(RestServiceConstant.LOOKUP_GROUP_SERVICE, RestConstant.RM_LOOKUP_GROUP);
		RestCaller.BASE_CLASS.put(RestServiceConstant.LOOKUP_GROUP_SERVICE, LookupGroup.class);
		RestCaller.BASE_URL.put(RestServiceConstant.LOOKUP_ADDRESS_SERVICE, RestConstant.RM_LOOKUP_ADDRESS);
		RestCaller.BASE_CLASS.put(RestServiceConstant.LOOKUP_ADDRESS_SERVICE, LookupAddress.class);
		RestCaller.BASE_URL.put(RestServiceConstant.LOOKUP_ADDRESS_GROUP_SERVICE, RestConstant.RM_LOOKUP_ADDRESS_GROUP);
		RestCaller.BASE_CLASS.put(RestServiceConstant.LOOKUP_ADDRESS_GROUP_SERVICE, LookupAddressGroup.class);
		
		RestCaller.BASE_URL.put(RestServiceConstant.MASTER_ADDRESS_SERVICE, RestConstant.RM_MASTER_ADDRESS);
		RestCaller.BASE_CLASS.put(RestServiceConstant.MASTER_ADDRESS_SERVICE, MasterAddress.class);
		
		RestCaller.BASE_URL.put(RestServiceConstant.PARTY, RestConstant.RM_PARTY);
		RestCaller.BASE_CLASS.put(RestServiceConstant.PARTY, Party.class);
		
		RestCaller.BASE_URL.put(RestServiceConstant.MAIL_SERVICE, RestConstant.RM_MAIL);
		RestCaller.BASE_CLASS.put(RestServiceConstant.MAIL_SERVICE, Object.class);
	}
}
