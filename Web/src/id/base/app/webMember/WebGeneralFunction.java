
package id.base.app.webMember;

import id.base.app.IPersonConstant;
import id.base.app.LoginSession;
import id.base.app.SystemConstant;
import id.base.app.exception.ErrorHolder;
import id.base.app.exception.SystemException;
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
import id.base.app.webMember.rest.AppFunctionRestCaller;
import id.base.app.webMember.rest.LoginRestCaller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class WebGeneralFunction {
   
	private final static Logger logger = LoggerFactory.getLogger(WebGeneralFunction.class);

    
    public static LoginSession getLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (null == session) {
            logger.info("Empty HTTP Session");
            throw new SystemException(new ErrorHolder("error.session.invalidated"));
        }

        LoginSession loginSession = (LoginSession) session.getAttribute(SessionConstants.USER_OBJECT_KEY);
        if (null == loginSession) {
            logger.info("Empty Login Session");
            throw new SystemException(new ErrorHolder("error.session.expired"));
        }
        return loginSession;
    }
    
    public static String toHTML(String string) {
        string = StringFunction.replace(string, "\n\r", "\n");
        string = StringFunction.replace(string, "\n", "<br />");
        return string;
    }
    
    public static String stringToHTMLString(String string) {
        StringBuffer sb = new StringBuffer(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;

        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == '"')
                    sb.append("&quot;");
                else if (c == '&')
                    sb.append("&amp;");
                else if (c == '<')
                    sb.append("&lt;");
                else if (c == '>')
                    sb.append("&gt;");
                else if (c == '\n')
                    // Handle Newline
                    sb.append("<br/>");
                else {
                    int ci = 0xffff & c;
                    if (ci < 160)
                        // nothing special only 7 Bit
                        sb.append(c);
                    else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(new Integer(ci).toString());
                        sb.append(';');
                    }
                }
            }
        }
        return sb.toString();
    }
    
    public static void buildLoginSession(RuntimeUserLogin login, HttpServletRequest request) throws Exception{
    	SpecificRestCaller<AppUser> userService = new SpecificRestCaller<AppUser>(RestConstant.REST_SERVICE, RestServiceConstant.USER_SERVICE);
    	
    	LoginSession loginSession = new LoginSession();
		loginSession.setPkAppUser(login.getUserId());
		loginSession.setUserName(login.getUserName());
		loginSession.setName(login.getName());
		loginSession.setUserType(login.getUserType());
		loginSession.setSessionType(SystemConstant.USER_TYPE_INTERNAL);
		
		if(null != login.getAccessInfo()){
			List<Long> userRoles = new ArrayList<>();
			try {
				Map<String, Object> map = new ObjectMapper().readValue(login.getAccessInfo(), new TypeReference<HashMap<String, Object>>(){});
				userRoles = (List<Long>) map.get("userRoles");
			} catch (IOException e) {
				e.printStackTrace();
			}
			loginSession.setUserRoles(userRoles);
		}
		
		final String userName = loginSession.getUserName(); 
		AppUser user = userService.executeGet(new QueryParamInterfaceRestCaller() {
			@Override
			public String getPath() {
				return "/findByUserNameParam";
			}
			
			@Override
			public Map<String, Object> getParameters() {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("userName", userName);
				return map;
			}
		});
		
		ObjectMapper om = new ObjectMapper();
		
		HashMap<String, Object> accessInfos;
		try {
			accessInfos = om.readValue(login.getAccessInfo(), new TypeReference<HashMap<String,Object>>() {});
			HashMap<String,Boolean> permissions = new HashMap<String, Boolean>();
			List<AppFunction> menus = new LinkedList<AppFunction>();
			permissions = (HashMap<String, Boolean>) accessInfos.get("accessibility");
			for (Map.Entry<String, List<Object>> menu : ((LinkedHashMap<String, List<Object>>) accessInfos.get("menus")).entrySet()) {
				List<Object> obj = (ArrayList<Object>) menu.getValue();
				String name = menu.getKey();
				String accessPage = obj.get(0).toString();
				Long fkParent = Long.valueOf(obj.get(1).toString());
				Long fkAppFunction = Long.valueOf(obj.get(2).toString());
				menus.add(AppFunction.getInstance(fkAppFunction, name, accessPage, fkParent, true));
			}
			loginSession.setSessionId(login.getAccessInfo());
			loginSession.setPermission(permissions);
			loginSession.setCompanyList(login.getCompanyList());
			loginSession.setCompanySelected(login.getCompanySelected());
			request.getSession().setAttribute(SessionConstants.USER_OBJECT_KEY, loginSession);
			request.getSession(false).setAttribute("menus", menus);
			if(user.getInitialWizardStep() == null || !user.getInitialWizardStep().equals(SystemConstant.InitialWizard.DONE)) {
				request.getSession(false).setAttribute(SessionConstants.INITIAL_WIZARD, Boolean.TRUE);
			} else {
				request.getSession(false).setAttribute(SessionConstants.INITIAL_WIZARD, Boolean.FALSE);
			}
		} catch (IOException e) {
			throw e;
		}
    }
    
    public static void createLogin(HttpServletRequest request, String cookieValue){
    	SpecificRestCaller<AppUser> userService = new SpecificRestCaller<AppUser>(RestConstant.REST_SERVICE, RestServiceConstant.USER_SERVICE);
		
		SpecificRestCaller<LoginSession> authenticationService = new SpecificRestCaller<LoginSession>(RestConstant.REST_SERVICE, RestServiceConstant.AUTHENTICATION);
		
		LoginRestCaller loginDirectoryService = new LoginRestCaller();

		try {

			AppUser user = null;
			String[] splittedToken = cookieValue.split("%");
			final String paramName = splittedToken[1];
			try{
				user = userService.executeGet(new QueryParamInterfaceRestCaller() {
					@Override
					public String getPath() {
						return "/findByUserNameParam";
					}
					
					@Override
					public Map<String, Object> getParameters() {
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("userName", paramName);
						return map;
					}
				});
			}catch(Exception e){
				throw e;
			}
		
			if (user.getStatus() == IPersonConstant.STATUS_INACTIVE) {
				throw new SystemException(new ErrorHolder("Error User Inactive"));
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
				roles.add(role.getPkAppRole());
			}
			loginSession.setUserRoles(roles);
			// for group name, use the 1st roles found
			/*loginSession.setGroupName(user.getAppRoles().get(0).getName());*/
			loginSession.setSessionId(cookieValue.replace(SystemConstant.COOKIE_SEPARATOR, SystemConstant.TOKEN_SEPARATOR));
			RuntimeUserLogin runtimeUserLogin = new RuntimeUserLogin();
			runtimeUserLogin.setName(loginSession.getName());
			runtimeUserLogin.setLoginTime(DateTimeFunction.getCurrentDate());
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
			
			Map<String, Object> cookieMenus = new LinkedHashMap<>();
			
			List<AppFunction> permissions = appFunctionService.findAppFunctionByPermissionList(user.getAppRoles());
			Map<Integer, Boolean> cookiePermissions = new HashMap<Integer, Boolean>();
			for(AppFunction appFunction: permissions) {
				loginSession.addPermission(appFunction.getPkAppFunction().intValue(), true);
				cookiePermissions.put(appFunction.getPkAppFunction().intValue(), true);
			}
			
			Map<String,Object> accessInfos = new HashMap<String, Object>();
			accessInfos.put("accessibility", cookiePermissions);
			
			
			List<AppFunction> listMenu =appFunctionService.findAppFunctionMenuByUserRoles(user.getAppRoles());
			for(AppFunction appFunction: listMenu) {
				if(SystemConstant.USER_TYPE_INTERNAL == appFunction.getUserType().intValue() ){
					menus.add(appFunction);
					cookieMenus.put(appFunction.getName(), new Object[]{appFunction.getAccessPage(), appFunction.getFkAppFunctionParent(), appFunction.getPkAppFunction()});
				}else{
					/*if(IAccessibilityConstant.FUNC_INT_POLICY_ADMINISTRATION == appFunction.getPkAppFunction().intValue()){
						menus.add(appFunction);
						cookieMenus.put(appFunction.getName(), appFunction.getAccessPage());
					}*/
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
			request.getSession().setAttribute(SessionConstants.USER_OBJECT_KEY, loginSession);
			request.getSession(false).setAttribute("menus", menus);
		}catch (SystemException | JsonProcessingException e) {
			throw new SystemException(new ErrorHolder("Error Creating Login Session"));
		}
    }
}
