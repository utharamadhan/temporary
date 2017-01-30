package id.base.app.webMember.rest;

import id.base.app.SystemConstant;
import id.base.app.exception.SystemException;
import id.base.app.rest.LoginSessionUtil;
import id.base.app.rest.RestBase;
import id.base.app.rest.RestConstant;
import id.base.app.valueobject.LookupAddress;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class LookupAddressRestCaller extends RestBase<LookupAddress>{
	protected static Logger LOGGER = LoggerFactory.getLogger(LookupAddressRestCaller.class);
	
	private HttpHeaders headers = new HttpHeaders();
	
	public LookupAddressRestCaller() {
		super();
		baseClass = LookupAddress.class;
		baseUrl = RestConstant.RM_LOOKUP_ADDRESS;
		try{
	        String userName = LoginSessionUtil.getLogin().getUserName();
	        if(StringUtils.isNotEmpty(userName)){
				headers.add(RestConstant.USER_CALLER, userName);
			}
        }catch(Exception e){
        	headers.add(RestConstant.USER_CALLER, SystemConstant.SYSTEM_USER);
        }
	}
	
	public List<LookupAddress> findByLookupAddressGroup(String lookupAddressGroup) throws SystemException {
		ResponseEntity<List> responseEntity = null;		
		String url = RestConstant.REST_SERVICE+RestConstant.RM_LOOKUP_ADDRESS+"/findByLookupAddressGroup/{lookupAddressGroup}";
		
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		RestTemplate rt = new RestTemplate();
		responseEntity = rt.exchange(url, HttpMethod.GET, entity, List.class, lookupAddressGroup);
		
		return resolveList(responseEntity, LinkedList.class);
	}
	
	public List<LookupAddress> findByLookupAddressGroupOrderBy(String lookupAddressGroup, String orderBy, boolean desc) throws SystemException {
		ResponseEntity<List> responseEntity = null;		
		String url = RestConstant.REST_SERVICE+RestConstant.RM_LOOKUP_ADDRESS+"/findByLookupAddressGroupOrderBy/{lookupAddressGroup}/{orderBy}/{desc}";
		
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		RestTemplate rt = new RestTemplate();
		responseEntity = rt.exchange(url, HttpMethod.GET, entity, List.class, new Object[]{lookupAddressGroup,orderBy,desc});
		
		return resolveList(responseEntity, LinkedList.class);
	}
	
	public LookupAddress findLookupAddressById(Long pkLookupAddress) throws SystemException {
		LookupAddress lookupAddress = null;
		String url = RestConstant.REST_SERVICE+RestConstant.RM_LOOKUP_ADDRESS+"/"+pkLookupAddress;
		
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		RestTemplate rt = new RestTemplate();
		ResponseEntity<LookupAddress> responseEntity = rt.exchange(url, HttpMethod.GET, entity, LookupAddress.class);
		lookupAddress = responseEntity.getBody();
		
		return lookupAddress;
	}
	
	public List<LookupAddress> findAddressByParent(String addressGroupSource, Long fkLookupAddressParent) throws SystemException {
		ResponseEntity<List> responseEntity = null;		
		String url = RestConstant.REST_SERVICE+RestConstant.RM_MASTER_ADDRESS+"/findAddressByParent/{addressGroupSource}/{fkLookupAddressParent}";
		
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		RestTemplate rt = new RestTemplate();
		responseEntity = rt.exchange(url, HttpMethod.GET, entity, List.class, addressGroupSource, fkLookupAddressParent);
		
		return resolveList(responseEntity, LinkedList.class);
	}
}