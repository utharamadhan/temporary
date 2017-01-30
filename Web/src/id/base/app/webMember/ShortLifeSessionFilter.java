package id.base.app.webMember;

import id.base.app.LoginSession;
import id.base.app.SystemConstant;
import id.base.app.valueobject.AppFunction;
import id.base.app.valueobject.RuntimeUserLogin;
import id.base.app.webMember.rest.LoginRestCaller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ShortLifeSessionFilter implements Filter{
	
	private static final String[] BYPASS_AUTH_PATTERN = {
        "/LoginAction.do",
        "/LogoffAction.do",
        "/ResetPassword.do",
        "/services"
    };
	
	private static final String[] BYPASS_ACCEPT_HEADERS = {
        "text/css",
        "image/"
    };

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		Cookie[] cookies = request.getCookies();
		String cookieValue = null;
		if(cookies!=null){
			for (Cookie cookie : cookies) {
				if(SessionConstants.ACCESS_KEY.equals(cookie.getName())){
					cookieValue = cookie.getValue();
					break;
				}
			}
		}
		boolean handleExpired = false;
		if(cookieValue!=null){			
			RuntimeUserLogin login = new LoginRestCaller().findByAccessInfoId(cookieValue);
			if(login!=null){
				LoginSession loginSession = new LoginSession();
				loginSession.setPkAppUser(login.getUserId());
				loginSession.setUserName(login.getUserName());
				loginSession.setName(login.getName());
				loginSession.setUserType(login.getUserType());
				
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
				
				ObjectMapper om = new ObjectMapper();
				
				HashMap<String,Object> accessInfos = om.readValue(login.getAccessInfo(), new TypeReference<HashMap<String,Object>>() {});
				HashMap<String,Boolean> permissions = new HashMap<String, Boolean>();
				List<AppFunction> menus = new LinkedList<AppFunction>();
				permissions = (HashMap<String, Boolean>) accessInfos.get("accessibility");
				for (Map.Entry<String, String> menu : ((LinkedHashMap<String, String>) accessInfos.get("menus")).entrySet()) {
					menus.add(new AppFunction(menu.getKey(), null, menu.getValue(), true));
				}
				loginSession.setPermission(permissions);
				request.getSession().setAttribute(SessionConstants.USER_OBJECT_KEY, loginSession);
				request.getSession(false).setAttribute("menus", menus);
				chain.doFilter(request, response);
			}else{
				handleExpired = true;
			}
		}else{
			boolean bypass = false;
			String requestURIminusCtxPath = request.getRequestURI().substring(request.getContextPath().length(), request.getRequestURI().length());
			for (String check : BYPASS_AUTH_PATTERN) {
	            if( check.equalsIgnoreCase(request.getServletPath())) {
	                bypass = true;
	                break;
	            }else{
	            	if((SystemConstant.LOGIN_URL + "?error=tokenExpired").equals(requestURIminusCtxPath) || "/do/token/tokenInvalid".equals(requestURIminusCtxPath)){
	            		bypass = true;
	            	}
	            }
	        }
			String acceptHeader = request.getHeader("Accept");
			for (String check : BYPASS_ACCEPT_HEADERS) {
	            if( acceptHeader.contains(check)) {
	                bypass = true;
	                break;
	            }
	        }
			
			if(!bypass){
				handleExpired = true;
			}else{
				chain.doFilter(request, response);
			}
		}
		if(handleExpired){
			Cookie loginCookie = new Cookie(SessionConstants.ACCESS_KEY, null);
			loginCookie.setMaxAge(0);
			response.addCookie(loginCookie);
			boolean toLogin = true;
			if(request.getHeader("X-Requested-With") != null){
				response.setHeader("ajax-expired", "true");
				toLogin = false;
			}else if(request.getParameter("posodaDrevo")!=null&&request.getParameter("posodaDrevo").equals("true")){
				toLogin = false;
			}
			
			if(toLogin){
				response.sendRedirect(request.getContextPath()+"/LogoffAction.do");					
			}else{
				request.setAttribute("message","<b>Login Required:</b> Your session is either expired or you are not login.");
                request.getRequestDispatcher( "/errorNotLogin.jsp").forward(request, response);
			}
		}
		if(request.getSession(false)!=null){
			request.getSession(false).removeAttribute(SessionConstants.USER_OBJECT_KEY);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
