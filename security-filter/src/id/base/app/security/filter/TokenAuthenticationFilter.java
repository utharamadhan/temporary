package id.base.app.security.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenAuthenticationFilter implements Filter{
	
	private static String LANDING_PAGE = "";
	private static String LANDING_PAGE_LOGIN = "/do/landingPage/loginPost";
	private static String REGISTRATION = "/do/registration";
	private static String ACTIVATION_PAGE = "/do/registration/activationPage";
	private static String ACTIVATION = "/do/registration/activation/";
	private static String LOGIN_FROM_ACTIVATION = "/do/login/loginFromActivation";
	private static String ACTIVATION_SUBMIT = "/do/registration/activationSubmit";
	private static String REGISTRATION_SUBMIT = REGISTRATION + "/submit";
	private static String TOKEN_EXPIRED = "";
	private static String TOKEN_EXPIRED_AJAX = "";
	private static String TOKEN_INVALID = "";
	private static String REGEN_EXPIRE = "15";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		String urlTokenExpired = TOKEN_EXPIRED;
		String urlTokenExpiredAjax = request.getContextPath()+TOKEN_EXPIRED_AJAX;
		String urlRegistration = request.getContextPath()+REGISTRATION;
		String urlActivation = request.getContextPath()+ACTIVATION;
		String urlActivationLogin = request.getContextPath()+LOGIN_FROM_ACTIVATION;
		String urlActivationPage = request.getContextPath()+ACTIVATION_PAGE;
		String urlActivationSubmit = request.getContextPath()+ACTIVATION_SUBMIT;
		String urlRegistrationSubmit = request.getContextPath()+REGISTRATION_SUBMIT;
		String urlTokenExpiredWithoutDec = urlTokenExpiredAjax.substring(0,urlTokenExpired.indexOf("?")-1);
		String urlLandingPage = request.getContextPath()+LANDING_PAGE;
		if(request.getRequestURI().equals(urlTokenExpiredWithoutDec)){
			chain.doFilter(request, response);
		}else{		
			Cookie cookie = null;
			boolean doChain = false;
			if(request.getRequestURI().equals(urlLandingPage) || request.getRequestURI().equals(urlActivationLogin)){
				//landingPage
				//get token
				String token = request.getParameter("token");
				if(token!=null){
				//token exist
					//buang token
					//redirect ke request sama tanpa token
					String[] splittedToken = token.split("\\|");
					cookie = new Cookie("token",token.replace("|", "%"));
					cookie.setMaxAge(getExpiry(splittedToken));
					cookie.setPath(request.getContextPath());
					response.addCookie(cookie);
					response.sendRedirect(urlLandingPage);
				}else{	
				//token not exist
					doChain = true;
				}
			} else if(request.getRequestURI().equals(urlRegistration) 
					|| request.getRequestURI().equals(urlRegistrationSubmit) 
						|| request.getRequestURI().startsWith(urlActivation)
							|| request.getRequestURI().equals(urlActivationPage) 
								|| request.getRequestURI().equals(urlActivationSubmit)) {
				//not doChain
				chain.doFilter(request, response);
			} else {
				//not landingPage
				doChain = true;
			}
			
			if(doChain){
				//get cookie
				Cookie cookieValue = getCookie(request.getCookies(), request);
				if(cookieValue!=null){						
				//cookie exist
					//chain filter
					Cookie newCookie = cookieValue;
					String newValue = cookieValue.getValue();
					newValue = newValue.substring(0, newValue.lastIndexOf("%")+1);
					newValue = newValue + getRandomString();
					newCookie.setValue(newValue);
					newCookie.setMaxAge((60*Integer.valueOf(REGEN_EXPIRE)));
					newCookie.setPath(request.getContextPath());
					response.addCookie(newCookie);
					chain.doFilter(request, response);
				}else{
				//cookie not exist
					//redirect expired token
					cookie = new Cookie("token","");
					cookie.setMaxAge(0);
					cookie.setPath(request.getContextPath());
					response.addCookie(cookie);
					String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
					if(header != null && header.equals("XMLHttpRequest")) {
						response.sendRedirect(urlTokenExpiredAjax);
					} else {
						response.sendRedirect(urlTokenExpired);
					}
				}
			}
		}
	}
	
	private String getRandomString(){
		Random rand = new Random();
		return "sadasdaijwiajdiwdakdmaskdmasdksaa"+(Math.abs(rand.nextInt()%10));
	}
	
	private int getDiff(int defValue, Calendar now, Calendar then){
		long milliseconds1 = now.getTimeInMillis();
	    long milliseconds2 = then.getTimeInMillis();
	    long diff = milliseconds2 - milliseconds1;
	    long diffSeconds = diff / 1000;
	    if(defValue<(int)diffSeconds){
	    	defValue = (int) diffSeconds;
	    }
	    return defValue;
	}

	private int getExpiry(String[] splittedToken) {
		int expire = 60*15;
		if(splittedToken.length>2 && splittedToken[2]!=null){
			String yyyyMMddHHmm = splittedToken[2];
			Calendar tcal = Calendar.getInstance();
			Date d = null;
		    if (yyyyMMddHHmm != null && yyyyMMddHHmm.length()>0) {
		        ParsePosition pos = new ParsePosition(0);
		        d = (new SimpleDateFormat("yyyyMMddHHmm")).parse(yyyyMMddHHmm, pos);
		        tcal.setTime(d);
		    }
			Calendar cal = Calendar.getInstance();
			if(d!=null){
				expire = getDiff(expire,cal,tcal);
			}
		}
		return expire;
	}

	private Cookie getCookie(Cookie[] cookies, HttpServletRequest request) {
		Cookie cookie = null;
		if(cookies!=null){
			for (Cookie cook : cookies) {
				if(cook.getName().equals("token")){
					cookie = new Cookie("token",cook.getValue().replace("|", "%"));
					cookie.setPath(request.getContextPath());
					cookie.setMaxAge(-1);
					break;
				}
			}
		}
		return cookie;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		String propertyLocation = arg0.getInitParameter("filterConfigFile");
		File file = new File(propertyLocation);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			Properties p = new Properties();
			p.load(fis);
			LANDING_PAGE = p.getProperty("landingPage");
			TOKEN_EXPIRED = p.getProperty("tokenExpired");
			TOKEN_INVALID = p.getProperty("tokenInvalid");
			TOKEN_EXPIRED_AJAX = p.getProperty("tokenExpiredAjax");
			REGEN_EXPIRE = p.getProperty("regenExpire");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
