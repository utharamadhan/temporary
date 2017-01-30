package id.base.app.webMember.controller;

import id.base.app.SystemConstant;
import id.base.app.exception.ErrorHolder;
import id.base.app.rest.PathInterfaceRestCaller;
import id.base.app.rest.RestConstant;
import id.base.app.rest.RestServiceConstant;
import id.base.app.rest.SpecificRestCaller;
import id.base.app.util.StringFunction;
import id.base.app.valueobject.AppUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author USER
 *
 */
@Scope(value="request")
@Controller
@RequestMapping(value="/registration")
public class RegistrationController {
	
	protected static Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);
	
	@RequestMapping(method=RequestMethod.GET)
	public String landingPagePost(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("detail", AppUser.getInstance());
		return "/registration";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/submit")
	@ResponseBody
	public Map<String, Object> submitRegistration(final AppUser anObject) {
		Map<String, Object> resultMap = new HashMap<>();
		List<ErrorHolder> errors = new ArrayList<>();
		try{
			errors = new SpecificRestCaller<AppUser>(RestConstant.REST_SERVICE, RestServiceConstant.USER_SERVICE).performPost("/create", anObject);
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			errors.add(new ErrorHolder(e.getMessage()));
		} finally {
			if(errors != null && errors.size() > 0){
				resultMap.put(SystemConstant.ERROR_LIST, errors);
			} else {
				if(StringFunction.isNotEmpty(anObject.getEmail())){
					resultMap.put(SystemConstant.REDIRECT, SystemConstant.LOGIN_URL);
				}else{
					resultMap.put(SystemConstant.REDIRECT, SystemConstant.ACTIVATION_URL);
				}
			}
		}
		return resultMap;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/activation/{activationCode}")
	public String activation(@PathVariable(value="activationCode") String activationCode, ModelMap model) {
		AppUser appUser = activateUserByActivationCode(activationCode);
		if(appUser!=null){
			return "redirect:/do/login/loginFromActivation?token="+serializeToken(appUser);
		}else{
			return "redirect:" + SystemConstant.LOGIN_URL + "?error=tokenExpired";
		}
	}
	
	private String serializeToken(AppUser appUser) {
		return "900|"+appUser.getUserName()+"|"+getExpiry()+"|password|"+appUser.getActivationCode()+"|sadasdaijwiajdiwdakdmaskdmasdksaa"+SystemConstant.UNIQUE_TOKEN;
	}
	
	private String getExpiry(){
		Calendar currentCalendar = Calendar.getInstance();
			currentCalendar.add(Calendar.HOUR, 1);
		return StringFunction.date2String(currentCalendar.getTime(), SystemConstant.SYSTEM_DATE_HOUR_MINUTE_NO_DELIMITER);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/activationPage")
	public String activationPage(ModelMap model, HttpServletResponse response) {
		model.addAttribute("detail", new AppUser());
		return "/activation";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/activationSubmit")
	@ResponseBody
	public Map<String, Object> activationSubmit(final AppUser anObject) {
		Map<String, Object> resultMap = new HashMap<>();
		List<ErrorHolder> errors = new ArrayList<>();
		try{
			errors = new SpecificRestCaller<AppUser>(RestConstant.REST_SERVICE, RestServiceConstant.USER_SERVICE).performPost("/activate", anObject);
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			errors.add(new ErrorHolder(e.getMessage()));
		} finally {
			if(errors != null && errors.size() > 0){
				resultMap.put(SystemConstant.ERROR_LIST, errors);
			} else {
				resultMap.put(SystemConstant.REDIRECT, SystemConstant.LOGIN_URL);
			}
		}
		return resultMap;
	}
	
	private AppUser activateUserByActivationCode(final String activationCode) {
		return new SpecificRestCaller<AppUser>(RestConstant.REST_SERVICE, RestServiceConstant.USER_SERVICE).executeGet(new PathInterfaceRestCaller() {
			@Override
			public String getPath() {
				return "/activateUserByActivationCode/{activationCode}/";
			}
			
			@Override
			public Map<String, Object> getParameters() {
				Map<String, Object> map = new HashMap<>();
					map.put("activationCode", activationCode);
				return map;
			}
		});
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/blank")
	public String blank(){
		return "/blank";
	}
	
}
