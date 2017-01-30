package id.base.app.webMember;

import id.base.app.LoginSession;
import id.base.app.exception.SystemException;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AuthenticationFilter implements Filter {
    //String of array that doesn't necessary to authenticate
    private static final String[] BYPASS_AUTH_PATTERN = {
        "/LoginAction.do",
        "/LogoffAction.do",
        "/ResetPassword.do",
        "/services"
    };
    
    static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    public void destroy() {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
    	HttpServletRequest httpServletRequest = (HttpServletRequest)req;
        boolean cekAuth = true;
        
        
        for (String check : BYPASS_AUTH_PATTERN) {
            if( check.equalsIgnoreCase(httpServletRequest.getServletPath())) {
                cekAuth = false;
                break;
            }
        }
        
        if (cekAuth) {
        	logger.info(" authenticate request [{}] for uri [{}]" , httpServletRequest.getServletPath(),  httpServletRequest.getRequestURI());
            try {
                LoginSession loginSession = WebGeneralFunction.getLogin((HttpServletRequest) req);
                //OsWorkflowContextHolder.getWorkflowContext().setActor(loginSession.getPersonID());
                //OsWorkflowContextHolder.getWorkflowContext().setCaller(loginSession.getUserName());
            }catch (SystemException e) {
                req.setAttribute("message","<b>Login Required:</b> Your session is either expired or you are not login.");
                ((HttpServletRequest) req).getRequestDispatcher( "/errorNotLogin.jsp").forward(req, resp);
                return;
            }
        } else if (httpServletRequest.getServletPath().equals("/services")) {
        	// set to 0 (SYSTEM)
        	//OsWorkflowContextHolder.getWorkflowContext().setActor(0);
            //OsWorkflowContextHolder.getWorkflowContext().setCaller(SystemConstant.SYSTEM_USER);
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
    }
}