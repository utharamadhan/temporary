package id.base.app.webMember.controller.security;

import id.base.app.rest.RestConstant;
import id.base.app.rest.SpecificInterfaceRestCaller;
import id.base.app.rest.SpecificRestCaller;
import id.base.app.valueobject.UserGroupAccessNode;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/*@Scope(value="request")
@Controller
@RequestMapping("/accessibility")*/
public class AccessibilityWebController {

	@RequestMapping(method=RequestMethod.POST, value="/save")
	public String save(@RequestParam(value="pkAppRole") final Long pkAppRole, @RequestParam(value="accessibilities") final String accessibilities, final ModelMap model){
		SpecificRestCaller<UserGroupAccessNode> roleFunctionCaller = new SpecificRestCaller<UserGroupAccessNode>(RestConstant.REST_SERVICE, RestConstant.RM_ROLE_FUNCTION, UserGroupAccessNode.class);
		roleFunctionCaller.executePost(new SpecificInterfaceRestCaller() {
			
			@Override
			public String getPath() {
				return "/updateAccessibilities";
			}
			
			@Override
			public Map<String, Object> getParameters() {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("pkAppRole", pkAppRole);
				map.put("accessibilities", accessibilities);
				return map;
			}
		});
		return "success";
	}
}
