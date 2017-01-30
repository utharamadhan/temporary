package id.base.app.webMember.rest;

import id.base.app.exception.SystemException;
import id.base.app.rest.MapRestCaller;
import id.base.app.rest.QueryParamInterfaceRestCaller;
import id.base.app.rest.RestBase;
import id.base.app.rest.RestConstant;
import id.base.app.rest.RestServiceConstant;
import id.base.app.valueobject.AppParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParameterRestCaller extends RestBase<AppParameter>{
	protected static Logger LOGGER = LoggerFactory.getLogger(ParameterRestCaller.class);
	
	public ParameterRestCaller() {
		super();
	}
	
	public Map<String, Object> findAppParametersByNames(final List<String> names)
			throws SystemException {
		Map<String,Object> map = new HashMap<String, Object>();
		MapRestCaller<String, Object> mrc = new MapRestCaller<String, Object>(RestConstant.REST_SERVICE, RestServiceConstant.SYSTEM_PARAMETER_SERVICE);
		map = mrc.executeGetMap(new QueryParamInterfaceRestCaller() {
			
			@Override
			public String getPath() {
				return "/findAppParametersByNames";
			}
			
			@Override
			public Map<String, Object> getParameters() {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("names", names);
				return map;
			}
		});
		return map;
	}
}
