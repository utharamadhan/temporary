package id.base.app.webMember.controller;

import id.base.app.IAuditTrailConstant;
import id.base.app.LoginSession;
import id.base.app.SystemConstant;
import id.base.app.exception.SystemException;
import id.base.app.rest.PathInterfaceRestCaller;
import id.base.app.rest.QueryParamInterfaceRestCaller;
import id.base.app.rest.RestConstant;
import id.base.app.rest.RestServiceConstant;
import id.base.app.rest.SpecificRestCaller;
import id.base.app.util.DateTimeFunction;
import id.base.app.util.StringFunction;
import id.base.app.valueobject.AppFunction;
import id.base.app.valueobject.AppRole;
import id.base.app.valueobject.AppUser;
import id.base.app.valueobject.RuntimeUserLogin;
import id.base.app.webMember.WebGeneralFunction;
import id.base.app.webMember.rest.AppFunctionRestCaller;
import id.base.app.webMember.rest.AuditRestCaller;
import id.base.app.webMember.rest.LoginRestCaller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Scope(value="request")
@RequestMapping(value="/login")
@Controller
public class LoginController {
	
	static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping(method=RequestMethod.GET)
	public View login(HttpServletRequest request, HttpServletResponse response){
		return execute(request, response);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/")
	public View loginSlash(HttpServletRequest request, HttpServletResponse response){
		return execute(request, response);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/loginFromActivation")
	public View loginFromActivation(HttpServletRequest request, HttpServletResponse response) {
		return execute(request, response);
	}
	
	private View execute(HttpServletRequest request, HttpServletResponse response){
		SpecificRestCaller<AppUser> userService = new SpecificRestCaller<AppUser>(RestConstant.REST_SERVICE, RestServiceConstant.USER_SERVICE);
		
		SpecificRestCaller<LoginSession> authenticationService = new SpecificRestCaller<LoginSession>(RestConstant.REST_SERVICE, RestServiceConstant.AUTHENTICATION);
		
		LoginRestCaller loginDirectoryService = new LoginRestCaller();

		RedirectView redirect = new RedirectView("/do/landingPage/blank",true, false);
		String username = null;
		try {
			Cookie[] cookies = request.getCookies();
			AppUser user = null;
			String cookieValue = null;
			if(cookies!=null){
				for (Cookie cookie : cookies) {
					if(SystemConstant.WEB_TRANS_COOKIE_NAME.equals(cookie.getName())){
						cookieValue = cookie.getValue();
						break;
					}
				}
			}
			if (cookieValue != null) {
				String[] splittedToken = cookieValue.split(SystemConstant.COOKIE_SEPARATOR);
				
				
				username = splittedToken[1];
				final String paramName = splittedToken[1];
				final String paramPassword = splittedToken[3];
				String activationCode = null;
				if(splittedToken.length > 5){
					activationCode = new String(splittedToken[4]);
				}
				final String activationCodeFinal = activationCode;
				try{
					user = userService.executeGet(new PathInterfaceRestCaller() {
						
						@Override
						public String getPath() {
							if(StringFunction.isEmpty(activationCodeFinal)){								
								return "/findByUserNameAndPassword/{userName}/{unencryptedPassword}";
							}else{
								return "/findByUserNameAndActivationCode/{userName}/{activationCode}";
							}
						}
						
						@Override
						public Map<String, Object> getParameters() {
							Map<String,Object> map = new HashMap<String, Object>();
							map.put("userName", paramName);
							if(StringFunction.isEmpty(activationCodeFinal)){							
								map.put("unencryptedPassword", paramPassword);
							} else {
								map.put("activationCode", activationCodeFinal);
							}
							return map;
						}
					});
				}catch(Exception e){
					return new RedirectView(SystemConstant.LOGIN_URL + "?error=wrongAccount",true, false);
				}
			
				if (user.getStatus() == SystemConstant.UserStatus.INACTIVE) {
					return new RedirectView("/do/landingPage/blank",true, false);
				}

				final AppUser fUser = user;
				LoginSession loginSession = authenticationService.executeGet(new QueryParamInterfaceRestCaller() {
					
					@Override
					public String getPath() {
						return "/authenticateLogin";
					}
					
					@Override
					public Map<String, Object> getParameters() {
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("u", fUser.getUserName());
						map.put("e", fUser.getEmail());
						map.put("s", fUser.getStatus());
						map.put("l", fUser.getLock());
						map.put("n", fUser.getParty().getName());
						map.put("ut", fUser.getUserType());
						map.put("pk", fUser.getPkAppUser());
						map.put("su", Boolean.TRUE);
						return map;
					}
				});
				List<Long> roles = new ArrayList<Long>();
				for(AppRole role: user.getAppRoles()) {
					if(role.getType()==SystemConstant.USER_TYPE_INTERNAL){
						roles.add(role.getPkAppRole());
					}
				}
				loginSession.setUserRoles(roles);
				// for group name, use the 1st roles found
				/*loginSession.setGroupName(user.getAppRoles().get(0).getName());*/
				loginSession.setSessionId(cookieValue.replace(SystemConstant.COOKIE_SEPARATOR, SystemConstant.TOKEN_SEPARATOR));
				RuntimeUserLogin runtimeUserLogin = new RuntimeUserLogin();
				runtimeUserLogin.setName(loginSession.getName());
				runtimeUserLogin
						.setLoginTime(DateTimeFunction.getCurrentDate());
				runtimeUserLogin.setRemoteAddress(request.getRemoteAddr());
				runtimeUserLogin.setUserId(loginSession.getPkAppUser());
				runtimeUserLogin.setUserName(loginSession.getUserName());
				runtimeUserLogin.setEmail(loginSession.getEmail());
				runtimeUserLogin.setAccessInfo(cookieValue.replace(SystemConstant.COOKIE_SEPARATOR, SystemConstant.TOKEN_SEPARATOR));
				runtimeUserLogin.setUserType(loginSession.getUserType());
				runtimeUserLogin.setSessionType(SystemConstant.USER_TYPE_INTERNAL);


				AppFunctionRestCaller appFunctionService = new AppFunctionRestCaller();
				
				// initialize app functions for accessibility
				final List<AppFunction> menus = new LinkedList<AppFunction>();
				
				Map<String, Object[]> cookieMenus = new LinkedHashMap<>();
				
				//List<AppFunction> permissions = appFunctionService.findAppFunctionByPermission(user.getAppRole().getPkAppRole());
				List<AppFunction> permissions = appFunctionService.findAppFunctionByPermissionList(user.getAppRoles());
				Map<Integer, Boolean> cookiePermissions = new HashMap<Integer, Boolean>();
				/*for(AppFunction appFunction: permissions) {
					loginSession.addPermission(appFunction.getPkAppFunction(), appFunction.getIsTransactional());
					cookiePermissions.put(appFunction.getPkAppFunction(), appFunction.getIsTransactional());
				}*/
				
				Map<String,Object> accessInfos = new HashMap<String, Object>();
				accessInfos.put("accessibility", cookiePermissions);
				
				
				List<AppFunction> listMenu =appFunctionService.findAppFunctionMenuByUserRoles(user.getAppRoles());
				for(AppFunction appFunction: listMenu) {
					if(SystemConstant.USER_TYPE_INTERNAL == appFunction.getUserType().intValue() ){
						menus.add(appFunction);
						cookieMenus.put(appFunction.getName(), new Object[]{appFunction.getAccessPage(), appFunction.getFkAppFunctionParent(), appFunction.getPkAppFunction()});
					}
				}
				
				// prepare to remove duplicate list
				Set<AppFunction> hs = new TreeSet<AppFunction>(new Comparator<AppFunction>() {
					@Override
					public int compare(AppFunction arg0, AppFunction arg1) {
						if(arg0.getOrderNo()!=null && arg1.getOrderNo()!=null){							
							return arg0.getOrderNo().compareTo(arg1.getOrderNo());
						}else{
							return 0;
						}
					}
				});				
				List[] obj = new List[] {menus};
				for(List<AppFunction> o: obj){
					hs.clear();
					hs.addAll(o);
					o.clear();
					o.addAll(hs);
				}
				
				accessInfos.put("menus", cookieMenus);
				accessInfos.put("userRoles", loginSession.getUserRoles());
				ObjectMapper mapper = new ObjectMapper();
				String stringCookie = mapper.writeValueAsString(accessInfos);
				runtimeUserLogin.setAccessInfo(stringCookie);
				
				
				loginDirectoryService.register(runtimeUserLogin);
				
				if(menus!=null && menus.size()>0 ){
					// activate this following code if you need initial wizard every time new user logins
					/*if(user.getInitialWizardStep() == null || !user.getInitialWizardStep().equals(SystemConstant.InitialWizard.DONE)) {
						//redirect to initial wizard
						redirect = setInitialWizardAndGetRedirectURL(user, userService);
					} else {
						redirect = null;
					}*/
					redirect = null;
					if(redirect == null) {					
						AppFunction objF=menus.get(0);
						redirect=new RedirectView(objF.getAccessPage(),true, false);
					}
				}else if(SystemConstant.USER_TYPE_INTERNAL == loginSession.getUserType().intValue()){
					redirect=new RedirectView(SystemConstant.ADMIN_URL+SystemConstant.WEB_ADMIN_COOKIE_NAME+"="+cookieValue.replace(SystemConstant.COOKIE_SEPARATOR, SystemConstant.TOKEN_SEPARATOR), false, false);
				}else{
					redirect=new RedirectView("/do/landingPage/blank",true, false);
				}
				
				if (loginSession.getFlagSuperUser() || loginSession.getUserType() == SystemConstant.USER_TYPE_INTERNAL) {
					request.setAttribute(SystemConstant.USER_TYPES, SystemConstant.USER_TYPE_INTERNAL);
				} else {
					request.setAttribute(SystemConstant.USER_TYPES, SystemConstant.USER_TYPE_EXTERNAL);
				}
				
				/*new AuditRestCaller().createAuditWithSubCode(
						IAuditTrailConstant.LOG_PARENT_MAPPING.get(IAuditTrailConstant.LOG_SUB_CODE_LOGIN),
						"Login with Username [" + username
								+ "] from " + request.getRemoteAddr(),
						username,
						IAuditTrailConstant.LOG_SUCCESS,
						request.getRemoteAddr(),
						IAuditTrailConstant.LOG_SUB_CODE_LOGIN);*/
			}
		}catch (SystemException | JsonProcessingException e) {
			new AuditRestCaller().createAuditWithSubCode(IAuditTrailConstant.LOG_PARENT_MAPPING.get(IAuditTrailConstant.LOG_SUB_CODE_LOGIN),
	            		"Login with Username ["+username+"] from "+request.getRemoteAddr(),username,IAuditTrailConstant.LOG_FAIL,request.getRemoteAddr(), IAuditTrailConstant.LOG_SUB_CODE_LOGIN);
		}
		return redirect;
	}
	
	public RedirectView setInitialWizardAndGetRedirectURL(AppUser user, SpecificRestCaller<AppUser> userService) {
		if(user.getInitialWizardStep() == null) {
			return new RedirectView(SystemConstant.InitialWizard.FIRST_COMPANY_REDIRECT_URL, true, false);
		} else if(user.getInitialWizardStep().equals(SystemConstant.InitialWizard.FIRST_COMPANY)) {
			return new RedirectView(SystemConstant.InitialWizard.SECOND_THIRD_PARTY_REDIRECT_URL, true, false);
		} else if(user.getInitialWizardStep().equals(SystemConstant.InitialWizard.SECOND_THIRD_PARTY)) {
			return new RedirectView(SystemConstant.InitialWizard.THIRD_PRODUCT_REDIRECT_URL, true, false);
		} else if(user.getInitialWizardStep().equals(SystemConstant.InitialWizard.THIRD_PRODUCT)) {
			return new RedirectView(SystemConstant.InitialWizard.FOURTH_WAREHOUSE_REDIRECT_URL, true, false);
		} else if(user.getInitialWizardStep().equals(SystemConstant.InitialWizard.FOURTH_WAREHOUSE)) {
			return new RedirectView(SystemConstant.InitialWizard.FIFTH_MACHINERY_REDIRECT_URL, true, false);
		} else if(user.getInitialWizardStep().equals(SystemConstant.InitialWizard.FIFTH_MACHINERY)) {
			return new RedirectView(SystemConstant.InitialWizard.SIXTH_TRANSPORTER_REDIRECT_URL, true, false);
		} else if(user.getInitialWizardStep().equals(SystemConstant.InitialWizard.SIXTH_TRANSPORTER)) {
			return new RedirectView(SystemConstant.InitialWizard.SEVENTH_MASTER_FEE_REDIRECT_URL, true, false);
		} else if(user.getInitialWizardStep().equals(SystemConstant.InitialWizard.SEVENTH_MASTER_FEE)) {
			return new RedirectView(SystemConstant.InitialWizard.EIGHTH_LOOKUP_REDIRECT_URL, true, false);
		} else if(user.getInitialWizardStep().equals(SystemConstant.InitialWizard.EIGHTH_LOOKUP_REDIRECT_URL)) {
			userService.performPost("/updateInitialWizard/"+user.getPkAppUser()+"/"+SystemConstant.InitialWizard.DONE, user);
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/out")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		 String userName="";
		 String clientHost="";
	        try {
	            if (WebGeneralFunction.getLogin(request) != null) {
	            	LoginSession ls=WebGeneralFunction.getLogin(request);
	            	userName = ls.getUserName();
	            	LoginRestCaller loginDirectoryService = new LoginRestCaller();
	            	loginDirectoryService.unregister(ls.getPkAppUser());
	            	AuditRestCaller arc = new AuditRestCaller();
	    	        arc.createAuditWithSubCode(IAuditTrailConstant.LOG_PARENT_MAPPING.get(IAuditTrailConstant.LOG_SUB_CODE_LOGOUT), 
	    	        		"Logout Username {"+userName+"}" , userName, IAuditTrailConstant.LOG_SUCCESS, clientHost,
	    	        		IAuditTrailConstant.LOG_SUB_CODE_LOGOUT);
	            }
	        }catch (SystemException e) {
	            LOGGER.error("error logout {}", e);
	        }finally{
	        	Cookie loginCookie = new Cookie(SystemConstant.WEB_TRANS_COOKIE_NAME, null);
				loginCookie.setMaxAge(0);
				loginCookie.setPath(request.getContextPath());
				response.addCookie(loginCookie);
            	request.getSession(false).invalidate();
	        }
	        return "redirect:"+SystemConstant.LOGIN_URL;
	}
}
