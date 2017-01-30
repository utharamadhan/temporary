package id.base.app.webMember.administrator;

import id.base.app.SystemConstant;
import id.base.app.exception.ErrorHolder;
import id.base.app.exception.SystemException;
import id.base.app.paging.PagingWrapper;
import id.base.app.rest.PathInterfaceRestCaller;
import id.base.app.rest.RestCaller;
import id.base.app.rest.RestConstant;
import id.base.app.rest.RestServiceConstant;
import id.base.app.rest.SpecificRestCaller;
import id.base.app.util.StringFunction;
import id.base.app.util.dao.Operator;
import id.base.app.util.dao.SearchFilter;
import id.base.app.util.dao.SearchOrder;
import id.base.app.valueobject.AppRole;
import id.base.app.valueobject.AppUser;
import id.base.app.valueobject.UserGroupAccessNode;
import id.base.app.webMember.DataTableCriterias;
import id.base.app.webMember.controller.BaseController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Scope(value="request")
@Controller
@RequestMapping("/appRoleMaintenance")
public class AppRoleMaintenanceWebController extends BaseController<AppRole> {

	private final String PATH_LIST = "/administration/appRoleMaintenanceList";
	private final String PATH_DETAIL = "/administration/appRoleMaintenanceDetail";
	
	@Override
	protected RestCaller<AppRole> getRestCaller() {
		return new RestCaller<AppRole>(RestConstant.REST_SERVICE, RestServiceConstant.ROLE_SERVICE);
	}

	@Override
	protected List<SearchFilter> convertForFilter(Map<String, String> paramWrapper) {
		return null;
	}
	
	private void setDefaultFilter(HttpServletRequest request, List<SearchFilter> filters) {}

	@Override
	protected List<SearchFilter> convertForFilter(HttpServletRequest request, Map<String, String> paramWrapper, DataTableCriterias columns) {
		List<SearchFilter> filters = new ArrayList<>();
		setDefaultFilter(request, filters);
		if(StringFunction.isNotEmpty(columns.getSearch().get(DataTableCriterias.SearchCriterias.value))){
			filters.add(new SearchFilter(AppRole.NAME, Operator.LIKE, columns.getSearch().get(DataTableCriterias.SearchCriterias.value)));
		}
		return filters;
	}

	@Override
	protected List<SearchOrder> getSearchOrder() {
		if(orders != null) {
			orders.clear();
		}
		orders.add(new SearchOrder(AppRole.NAME, SearchOrder.Sort.ASC));
		return orders;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="showList")
	public String showList(ModelMap model, HttpServletRequest request){
		model.addAttribute("pagingWrapper", new PagingWrapper<AppUser>());
		return getListPath();
	}
	
	public void showAccessTree(Long roleId, Integer type, ModelMap model){
		try {
			final Long pkAppRole = roleId;
			final Integer userType = type;
			List<UserGroupAccessNode> accessNodes = new SpecificRestCaller<UserGroupAccessNode>(RestConstant.REST_SERVICE, RestConstant.RM_ROLE_FUNCTION, UserGroupAccessNode.class).executeGetList(new PathInterfaceRestCaller() {
				@Override
				public String getPath() {
					return "/accessTree/{pkAppRole}/{userType}";
				}
				@Override
				public Map<String, Object> getParameters() {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("pkAppRole", pkAppRole);
					map.put("userType", userType);
					return map;
				}
			});
			LOGGER.info("Access List Tree Size [{}]",accessNodes.size());
			model.addAttribute("pkAppRole", pkAppRole);
			model.addAttribute("accessNodes", accessNodes);
		} catch (SystemException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	
	public void setDefaultData(ModelMap model) {}
	
	@RequestMapping(method=RequestMethod.GET, value="showAdd")
	public String showAdd(ModelMap model, HttpServletRequest request){
		return PATH_DETAIL;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="showEdit")
	public String showEdit(@RequestParam(value="maintenancePK") final Long maintenancePK, @RequestParam Map<String, String> paramWrapper, ModelMap model, HttpServletRequest request){
		setDefaultData(model);
		AppRole detail = getRestCaller().findById(maintenancePK);
		model.addAttribute("detail", detail);
		showAccessTree(detail.getPkAppRole(), detail.getType(), model);
		return PATH_DETAIL;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="saveUser")
	@ResponseBody
	public Map<String, Object> saveUser(final AppRole anObject, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		List<ErrorHolder> errors = new ArrayList<>();
		try{
			errors = new SpecificRestCaller<AppRole>(RestConstant.REST_SERVICE, RestServiceConstant.ROLE_SERVICE).performPut("/update", anObject);
			if(errors != null && errors.size() > 0){
				resultMap.put(SystemConstant.ERROR_LIST, errors);
			}
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return resultMap;
	}

	@Override
	protected String getListPath() {
		return PATH_LIST;
	}

}
