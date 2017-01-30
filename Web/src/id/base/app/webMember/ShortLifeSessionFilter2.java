package id.base.app.webMember;

import id.base.app.SystemConstant;
import id.base.app.valueobject.RuntimeUserLogin;
import id.base.app.webMember.rest.LoginRestCaller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ShortLifeSessionFilter2 implements Filter{
	private final static Logger LOGGER = LoggerFactory.getLogger(ShortLifeSessionFilter2.class);
	
	private static final Set<String> BYPASS_TOKEN = new HashSet<String>();
	static{
		BYPASS_TOKEN.add("/do/landingPage");
		BYPASS_TOKEN.add("/do/landingPage/loginPost");
		BYPASS_TOKEN.add("/do/landingPage/blank");
		BYPASS_TOKEN.add("/do/token/tokenExpired");
		BYPASS_TOKEN.add("/do/token/tokenInvalid");
		BYPASS_TOKEN.add("/do/registration");
		BYPASS_TOKEN.add("/do/registration/submit");
		BYPASS_TOKEN.add("/do/login/loginFromActivation");
		BYPASS_TOKEN.add("/do/login/registerCompany");
		BYPASS_TOKEN.add("/do/login/setCompanySelected");
		BYPASS_TOKEN.add("/do/registration/activationPage");
		BYPASS_TOKEN.add("/do/registration/activationSubmit");
	}
	
	private static final String URL_ACTIVATION = "/do/registration/activation";
	private static final String URL_INITIAL_WIZARD = "initialWizard";
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String requestURIminusCtxPath = request.getRequestURI().substring(request.getContextPath().length(), request.getRequestURI().length());
		LOGGER.info("SLSFilter2:"+requestURIminusCtxPath+" is being filtered");
		String redirect = null;
		if(BYPASS_TOKEN.contains(requestURIminusCtxPath) || requestURIminusCtxPath.startsWith(URL_ACTIVATION)){
			chain.doFilter(request, response);
		}else{
			Cookie[] cookies = request.getCookies();
			String cookieValue = null;
			if(cookies!=null){
				for (Cookie cookie : cookies) {
					if(SystemConstant.WEB_TRANS_COOKIE_NAME.equals(cookie.getName())){
						cookieValue = cookie.getValue();
						break;
					}
				}
			}
			boolean cookieValid = true;
			String[] splittedToken = null;
			if(cookieValue!=null){
				splittedToken = cookieValue.split("%");
				if(splittedToken.length<5){
					cookieValid = false;
				}
			}
			
			if(cookieValid){
				RuntimeUserLogin login = new LoginRestCaller().findByUserName(splittedToken[1]);
				if(login!=null){
					try {
						if(SystemConstant.USER_TYPE_INTERNAL==login.getSessionType().intValue()){
							WebGeneralFunction.buildLoginSession(login,request);
						}else{
							try{
								WebGeneralFunction.createLogin(request, cookieValue);
							}catch(Exception e){
								redirect = request.getContextPath()+"/do/landingPage/blank";
							}
						}
					} catch (Exception e) {
						redirect = SystemConstant.LOGIN_URL;
					}
				}else{
					try{
						WebGeneralFunction.createLogin(request, cookieValue);
					}catch(Exception e){
						redirect = SystemConstant.LOGIN_URL + "?error=wrongAccount";
					}
				}
			}else{
				redirect = SystemConstant.LOGIN_URL;
			}
			if(redirect!=null){
				if(request.getHeader("X-Requested-With") != null){
					request.setAttribute("message","<b>Login Required:</b> Your session is either expired or you are not login.");
	                request.getRequestDispatcher( SystemConstant.LOGIN_URL+"?error=tokenExpired" ).forward(request, response);
				}else if(request.getParameter("posodaDrevo")!=null&&request.getParameter("posodaDrevo").equals("true")){
					request.setAttribute("message","<b>Login Required:</b> Your session is either expired or you are not login.");
	                request.getRequestDispatcher( SystemConstant.LOGIN_URL+"?error=tokenExpired" ).forward(request, response);
				}else{
					response.sendRedirect(redirect);
				}
			}else{
				chain.doFilter(request, response);
			}
			if(request.getSession(false) != null){
				request.getSession(false).removeAttribute(SessionConstants.USER_OBJECT_KEY);
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
