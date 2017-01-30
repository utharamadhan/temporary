package id.base.app.webMember.rest;

import id.base.app.SystemConstant;
import id.base.app.exception.SystemException;
import id.base.app.rest.PathInterfaceRestCaller;
import id.base.app.rest.QueryParamInterfaceRestCaller;
import id.base.app.rest.RestCaller;
import id.base.app.rest.RestConstant;
import id.base.app.rest.RestServiceConstant;
import id.base.app.rest.SpecificRestCaller;
import id.base.app.util.StringFunction;
import id.base.app.valueobject.RuntimeUserLogin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginRestCaller{
	protected static Logger LOGGER = LoggerFactory.getLogger(LoginRestCaller.class);
	
	public LoginRestCaller() {
		super();
	}
	
	public void register(RuntimeUserLogin userLogin) throws SystemException {
		new SpecificRestCaller<RuntimeUserLogin>(RestConstant.REST_SERVICE, RestServiceConstant.LOGIN_SERVICE).executePost("/register", userLogin);
	}
	
	public void unregister(Long userPK) throws SystemException {
		new SpecificRestCaller<Long>(RestConstant.REST_SERVICE, RestConstant.RM_USER_LOGIN,Long.class).executePost("/unregister", userPK);
	}
	
	public RuntimeUserLogin findById(final Long id) {
		return new RestCaller<RuntimeUserLogin>(RestConstant.REST_SERVICE, RestServiceConstant.LOGIN_SERVICE).findById(id);
	}
	
	public RuntimeUserLogin findByUserName(final String userName) {
		return new SpecificRestCaller<RuntimeUserLogin>(RestConstant.REST_SERVICE, RestServiceConstant.LOGIN_SERVICE).executeGet(new QueryParamInterfaceRestCaller() {
			@Override
			public String getPath() {
				return "/findByUserNameParam";
			}
			
			@Override
			public Map<String, Object> getParameters() {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("userName", userName);
				return map;
			}
		});
	}
	
	public RuntimeUserLogin findByAccessInfoId(final String accessInfoId) throws SystemException {
		if(StringFunction.isEmpty(accessInfoId)){
			return null;
		}else{
			final String finalAccessInfoId = accessInfoId.replace(SystemConstant.COOKIE_SEPARATOR, SystemConstant.TOKEN_SEPARATOR);
			return new SpecificRestCaller<RuntimeUserLogin>(RestConstant.REST_SERVICE, RestServiceConstant.LOGIN_SERVICE).executeGet(new PathInterfaceRestCaller() {
				
				@Override
				public String getPath() {
					return "/findByAccessInfoId/{aiid}";
				}
				
				@Override
				public Map<String, Object> getParameters() {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("aiid", finalAccessInfoId);
					return map;
				}
			});
		}
	}
}
