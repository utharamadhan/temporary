package id.base.app.webMember;

import id.base.app.SystemConstant;
import id.base.app.util.StringFunction;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AjaxDetectorFilter implements Filter {

	static Logger logger = LoggerFactory.getLogger(AjaxDetectorFilter.class);
	
	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
        String xRequestedWith = ((HttpServletRequest) request).getHeader("X-Requested-With");
        
        ServletRequest requestWrapper = request;
        if(StringFunction.isNotEmpty(xRequestedWith)){
	        if(xRequestedWith.equals("XMLHttpRequest")){
	        	request.setAttribute(SystemConstant.AJAX_INDICATOR, Boolean.TRUE.toString());
	        	requestWrapper = new RequestWrapper((HttpServletRequest) request, true);
	        }
        }

        chain.doFilter(requestWrapper, response);
	
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
	
	public class RequestWrapper extends HttpServletRequestWrapper {
		private boolean isAjax;
		private Map<String, String[]> wrappedMap;
		
		public RequestWrapper(HttpServletRequest request, boolean isAjax) {
			super(request);
			this.isAjax = isAjax;
		}
		
		private String getAjaxString() {
			return isAjax ? Boolean.TRUE.toString(): Boolean.FALSE.toString();
		}
		
		@SuppressWarnings({ "unchecked" })
		private Map<String, String[]> getWrappedMap() {
			if (wrappedMap == null) {
				wrappedMap = new Hashtable<String, String[]>(super.getParameterMap());
				wrappedMap.put(SystemConstant.AJAX_INDICATOR, new String[]{getAjaxString()});
			}
			return Collections.unmodifiableMap(wrappedMap);
		}
		
		@Override
		public String getParameter(String name) {
			if (name.equals(SystemConstant.AJAX_INDICATOR)) {
				return getAjaxString();
			}
			return super.getParameter(name);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Enumeration getParameterNames() {
			return Collections.enumeration( getWrappedMap().keySet());
		}
		
		@Override
		public String[] getParameterValues(String name) {
			if (name.equals(SystemConstant.AJAX_INDICATOR)) {
				return new String[] {getAjaxString()};
			}
			return super.getParameterValues(name);
		}
		
		@Override
		public Map<String, String[]> getParameterMap() {
			return getWrappedMap();
		}
	}

}
