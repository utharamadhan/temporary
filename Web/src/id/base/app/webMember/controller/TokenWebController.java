package id.base.app.webMember.controller;

import id.base.app.SystemConstant;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Scope(value="request")
@Controller
@RequestMapping("/token")
public class TokenWebController {
	
	@RequestMapping(method=RequestMethod.GET, value="/tokenInvalid")
	public String showErrorInvalid(ModelMap model, @RequestParam Map<String,String> paramWrapper, HttpServletRequest request, HttpServletResponse response){
		response.setHeader("ajax-expired", "true");
		return "/blankTokenInvalid";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/tokenExpired")
	public String showErrorExpired(ModelMap model, @RequestParam Map<String,String> paramWrapper, HttpServletRequest request, HttpServletResponse response){
		response.setHeader("ajax-expired", "true");
		response.setHeader("loginURL", SystemConstant.LOGIN_URL + "?error=tokenExpired");
		return "/blankTokenExpired";
	}
}