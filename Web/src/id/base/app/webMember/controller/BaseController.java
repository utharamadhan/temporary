package id.base.app.webMember.controller;

import id.base.app.IAuditTrailConstant;
import id.base.app.SystemParameter;
import id.base.app.paging.PagingUtil;
import id.base.app.paging.PagingWrapper;
import id.base.app.rest.RestCaller;
import id.base.app.rest.RestConstant;
import id.base.app.rest.RestServiceConstant;
import id.base.app.util.StringFunction;
import id.base.app.util.dao.SearchFilter;
import id.base.app.util.dao.SearchOrder;
import id.base.app.valueobject.AppUser;
import id.base.app.valueobject.Lookup;
import id.base.app.webMember.Constants;
import id.base.app.webMember.DataTableCriterias;
import id.base.app.webMember.rest.AuditRestCaller;
import id.base.app.webMember.rest.ParameterRestCaller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Scope(value="request")
@Controller
public abstract class BaseController<T> {
	
	protected static Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	
	protected abstract RestCaller<T> getRestCaller();
	protected abstract List<SearchFilter> convertForFilter(Map<String,String> paramWrapper);
	protected abstract List<SearchFilter> convertForFilter(HttpServletRequest request, Map<String,String> paramWrapper, DataTableCriterias columns);
	protected abstract List<SearchOrder> getSearchOrder();
	protected abstract String getListPath();
	protected T detail;
	protected List<SearchFilter> filters = new ArrayList<SearchFilter>();
	protected List<SearchOrder> orders = new ArrayList<SearchOrder>();
	
	protected Long maintenancePK = null;
	private Long[] maintenancePKs;
	
	protected AuditRestCaller getAuditRestCaller() {
		return new AuditRestCaller();
	}
	
	protected void sendToAuditRestCaller(String userName, String remoteAddress, String description, String status, int subCode){
		int parentCode = IAuditTrailConstant.LOG_PARENT_MAPPING.get(subCode);
		getAuditRestCaller().createAuditWithSubCode(parentCode, description, userName, status, remoteAddress, subCode);
	}
	
	protected int[] getStartAndOffset(Map<String,String> paramWrapper){
		int[] soff = new int[2];
		List<String> names = new ArrayList<String>();
		names.add(SystemParameter.NAME_MAX_RECORD_PER_SCREEN);
		Map<String,Object> parameterMap = new ParameterRestCaller().findAppParametersByNames(names);
		int offset = SystemParameter.MAX_RECORD_PER_SCREEN;
        try{
        	offset = Integer.valueOf(String.valueOf(parameterMap.get(SystemParameter.NAME_MAX_RECORD_PER_SCREEN)));
        }catch(NumberFormatException nfe){
        	offset = SystemParameter.MAX_RECORD_PER_SCREEN;
        }
        int page = 1;
        if(paramWrapper.containsKey(Constants.PAGE_NO)){
        	String pageValue = paramWrapper.get(Constants.PAGE_NO).trim();
        	page = Integer.valueOf(StringFunction.isEmpty(pageValue) || pageValue.equals("0") ? "1" : paramWrapper.get(Constants.PAGE_NO));
        }
        int startNo = PagingUtil.getStartRowFromPage(page, offset);
        soff[0]=startNo;
        soff[1]=offset;
        return soff;
	}
	
	public PagingWrapper<T> getPagingWrapper(Map<String,String> paramWrapper){
		PagingWrapper<T> pw = new PagingWrapper<T>();
		int[] soff = getStartAndOffset(paramWrapper);
		pw = getRestCaller().findAllByFilter(soff[0], soff[1], convertForFilter(paramWrapper), getSearchOrder());
		filters.clear();
		orders.clear();
		return pw;
	}
	
	public PagingWrapper<T> getPagingWrapper(HttpServletRequest request, Map<String,String> paramWrapper, DataTableCriterias columns){
		PagingWrapper<T> pw = new PagingWrapper<T>();
		int[] soff = getStartAndOffset(paramWrapper);
		pw = getRestCaller().findAllByFilter(soff[0], soff[1], convertForFilter(request, paramWrapper, columns), getSearchOrder());
		filters.clear();
		orders.clear();
		return pw;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String showList(final ModelMap model, @RequestParam Map<String,String> paramWrapper){
		model.addAttribute("pagingWrapper", getPagingWrapper(paramWrapper));
		return getListPath();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/empty")
	public String showEmptyList(final ModelMap model, @RequestParam Map<String,String> paramWrapper){
		populateColumnFilter(model);
		model.addAttribute("pagingWrapper", new PagingWrapper<T>());
		return getListPath();
	}
	
	//override this if needed
	protected void populateColumnFilter(ModelMap model){
		
	}
	
	protected List<Map<String,Object>> resolveLookupToDTOptions(boolean provideDefaultValue, List<Lookup> lookups){
		return resolveObjectToDTOptions(provideDefaultValue, lookups, Lookup.ID, new String[]{Lookup.CODE, Lookup.DESCRIPTION});
	}
	
	protected List<Map<String,Object>> resolveObjectToDTOptions(List<? extends Object> objects, String valueKey, String labelKey){
		return resolveObjectToDTOptions(true,objects,valueKey,labelKey);
	}
	
	protected List<Map<String,Object>> resolveObjectToDTOptions(boolean provideDefaultValue, List<? extends Object> objects, String valueKey, String labelKey){
		return resolveObjectToDTOptions(provideDefaultValue, objects, valueKey, new String[]{labelKey});
	}
	
	protected List<Map<String,Object>> resolveObjectToDTOptions(boolean provideDefaultValue, List<? extends Object> objects, String valueKey, String[] labelKeys){
		List<Map<String,Object>> opts = new LinkedList<Map<String,Object>>();
		try {
			if(objects!=null&&objects.size()>0){
				if(provideDefaultValue){
					Map<String,Object> opt = new LinkedHashMap<String,Object>();
					Class type = PropertyUtils.getPropertyType(objects.get(0), valueKey);
					opt.put("value", "");
					opt.put("label", "Please Select");
					opts.add(opt);
				}
				for (Object obj : objects) {
					Map<String,Object> opt = new LinkedHashMap<String,Object>();
					opt.put("value", BeanUtils.getProperty(obj, valueKey));
					opt.put("label", BeanUtils.getProperty(obj, labelKeys[0]));
					for (String labelKey : labelKeys) {
						opt.put(labelKey, BeanUtils.getProperty(obj, labelKey));
					}
					opts.add(opt);
				}
			}
		} catch (Exception e) {
			LOGGER.error("error resolving Lookup to DT Options");
		}
		return opts;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/list")
	@ResponseBody
	public PagingWrapper<T> showJsonList(final ModelMap model, @ModelAttribute DataTableCriterias columns, @RequestParam Map<String,String> paramWrapper,
			HttpServletRequest request){
		boolean emptyList = false;
		if(paramWrapper.containsKey("emptyList")){
			if(paramWrapper.get("emptyList")!=null){
				if("true".equalsIgnoreCase(paramWrapper.get("emptyList"))){
					emptyList = true; 
				}
			}
		}
		PagingWrapper<T> pw = new PagingWrapper<T>();
		if(!emptyList){
			pw = getPagingWrapper(request, paramWrapper, columns);
		}
		return pw;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/save")
	public String save(final T anObject, final BindingResult bindingResult, final ModelMap model){
		if(bindingResult.hasErrors()){
			return "error";
		}
		getRestCaller().saveOrUpdate(anObject);
		model.clear();
		return "success";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/delete")
	public String delete(@RequestParam(value="maintenancePKs") Long[] maintenancePKs){
		getRestCaller().delete(maintenancePKs);
		return "success";
	}
	
	public AppUser getUser(final Long pkAppUser) {
		return new RestCaller<AppUser>(RestConstant.REST_SERVICE, RestServiceConstant.USER_SERVICE).findById(pkAppUser);
	}
	
}
