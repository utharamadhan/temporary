package id.base.app.webMember.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author USER
 *
 */
@Scope(value="request")
@Controller
@RequestMapping(value="/landingPage")
public class LandingPageController {
	
	@RequestMapping(method=RequestMethod.GET)
	public String landingPagePost(@RequestParam(value="token", required=false) String token, HttpServletRequest request, HttpServletResponse response) {
		return "redirect:/do/login";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/blank")
	public String blank(){
		return "/blank";
	}
}