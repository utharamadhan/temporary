package id.base.app.webMember.controller.dashboard;

import id.base.app.rest.RestConstant;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Scope(value="request")
@Controller
@RequestMapping(RestConstant.RM_DASHBOARD)
public class DashboardWebController{

	protected static Logger LOGGER = LoggerFactory.getLogger(DashboardWebController.class);
	
	@RequestMapping(method=RequestMethod.GET)
	public String view(ModelMap model, HttpServletRequest request){
		return "/dashboard/dashboardDetail";
	}
	
}
