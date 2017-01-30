package id.base.app.webMember.controller.setting;

import id.base.app.rest.RestCaller;
import id.base.app.util.dao.SearchFilter;
import id.base.app.util.dao.SearchOrder;
import id.base.app.valueobject.party.Party;
import id.base.app.webMember.DataTableCriterias;
import id.base.app.webMember.controller.BaseController;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Scope(value="request")
@Controller
@RequestMapping("/setting/profile")
public class ProfileWebController extends BaseController<Party> {

	@Override
	protected RestCaller<Party> getRestCaller() {
		return null;
	}
	
	@Override
	@RequestMapping(method=RequestMethod.GET)
	public String showList(final ModelMap model, @RequestParam Map<String,String> paramWrapper){
		detail = new Party();
		model.addAttribute("detail", detail);
		return "/setting/profileDetail";
	}

	@Override
	protected List<SearchFilter> convertForFilter(
			Map<String, String> paramWrapper) {
		return null;
	}

	@Override
	protected List<SearchFilter> convertForFilter(HttpServletRequest request, 
			Map<String, String> paramWrapper, DataTableCriterias columns) {
		return null;
	} 

	@Override
	protected List<SearchOrder> getSearchOrder() {
		return null;
	}

	@Override
	protected String getListPath() {
		return null;
	}
}