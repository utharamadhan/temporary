package id.base.app.webMember.rest;

import id.base.app.SystemConstant;
import id.base.app.exception.SystemException;
import id.base.app.rest.LoginSessionUtil;
import id.base.app.rest.RestBase;
import id.base.app.rest.RestConstant;
import id.base.app.valueobject.Lookup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class LookupRestCaller extends RestBase<Lookup>{
	protected static Logger LOGGER = LoggerFactory.getLogger(LookupRestCaller.class);
	
	private HttpHeaders headers = new HttpHeaders();
	
	public LookupRestCaller() {
		super();
		baseClass = Lookup.class;
		baseUrl = RestConstant.RM_LOOKUP;
		try{
	        String userName = LoginSessionUtil.getLogin().getUserName();
	        if(StringUtils.isNotEmpty(userName)){
				headers.add(RestConstant.USER_CALLER, userName);
			}
        }catch(Exception e){
        	headers.add(RestConstant.USER_CALLER, SystemConstant.SYSTEM_USER);
        }
	}
	
	public List<Lookup> findByLookupGroup(String lookupGroup) throws SystemException {
		ResponseEntity<List> responseEntity = null;		
		String url = RestConstant.REST_SERVICE+RestConstant.RM_LOOKUP+"/findByLookupGroup/{lookupGroup}";
		
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		RestTemplate rt = new RestTemplate();
		responseEntity = rt.exchange(url, HttpMethod.GET, entity, List.class, lookupGroup);
		
		return resolveList(responseEntity, LinkedList.class);
	}
	
	public List<Lookup> findByLookupGroupAndParentCode(String lookupGroup, String parentCode) throws SystemException {
		ResponseEntity<List> responseEntity = null;		
		String url = RestConstant.REST_SERVICE+RestConstant.RM_LOOKUP+"/findByLookupGroupAndParentCode/{lookupGroup}/{parentCode}";
		
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		RestTemplate rt = new RestTemplate();
		responseEntity = rt.exchange(url, HttpMethod.GET, entity, List.class, lookupGroup, parentCode);
		
		return resolveList(responseEntity, LinkedList.class);
	}
	
	public List<Lookup> findByLookupGroupAndUsage(String lookupGroup, String usage) throws SystemException {
		ResponseEntity<List> responseEntity = null;		
		String url = RestConstant.REST_SERVICE+RestConstant.RM_LOOKUP+"/findByLookupGroupAndUsage/{lookupGroup}/{usage}";
		
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		RestTemplate rt = new RestTemplate();
		responseEntity = rt.exchange(url, HttpMethod.GET, entity, List.class, lookupGroup, usage);
		
		return resolveList(responseEntity, LinkedList.class);
	}
	
	public List<Lookup> findByLookupGroupOrderBy(String lookupGroup, String orderBy, boolean desc) throws SystemException {
		ResponseEntity<List> responseEntity = null;		
		String url = RestConstant.REST_SERVICE+RestConstant.RM_LOOKUP+"/findByLookupGroup/{lookupGroup}/{orderBy}/{desc}";
		
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		RestTemplate rt = new RestTemplate();
		responseEntity = rt.exchange(url, HttpMethod.GET, entity, List.class, new Object[]{lookupGroup,orderBy,desc});
		
		return resolveList(responseEntity, LinkedList.class);
	}
	
	public Lookup findLookupById(Long pkLookup) throws SystemException {
		Lookup lookup = null;
		String url = RestConstant.REST_SERVICE+RestConstant.RM_LOOKUP+"/"+pkLookup;
		
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		RestTemplate rt = new RestTemplate();
		ResponseEntity<Lookup> responseEntity = rt.exchange(url, HttpMethod.GET, entity, Lookup.class);
		lookup = responseEntity.getBody();
		
		return lookup;
	}
	
	public Lookup findLookupByCodeAndLookupGroup(String code, String lookupGroup) throws SystemException {
		Lookup lookup = null;
		String url = RestConstant.REST_SERVICE+RestConstant.RM_LOOKUP+"/findLookupByCodeAndLookupGroup/"+code+"/"+lookupGroup;
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<Lookup> responseEntity= rt.exchange(url, HttpMethod.GET, entity, Lookup.class);
		lookup = responseEntity.getBody();
		return lookup;
	}
	
	public Map<String,Lookup> findMapByLookupGroup(String lookupGroup) throws SystemException {
		ResponseEntity<List> responseEntity = null;		
		String url = RestConstant.REST_SERVICE+RestConstant.RM_LOOKUP+"/findByLookupGroup/{lookupGroup}";
		
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		RestTemplate rt = new RestTemplate();
		responseEntity = rt.exchange(url, HttpMethod.GET, entity, List.class, lookupGroup);
		
		List<Lookup> list = resolveList(responseEntity, LinkedList.class);
		Map<String,Lookup> mapLookup = new HashMap<String, Lookup>();
		for (Lookup lookup : list) {
			mapLookup.put(lookup.getCode(), lookup);
		}
		return mapLookup;
	}
	
	public Map<String,List<Lookup>> findByLookupGroups(List<String> lookupGroups) throws SystemException {
		ResponseEntity<List> responseEntity = null;		
		String url = RestConstant.REST_SERVICE+RestConstant.RM_LOOKUP+"/findByLookupGroups";
		
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		RestTemplate rt = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("lg", lookupGroups.toArray(new String[lookupGroups.size()]));
		responseEntity = rt.exchange(builder.build().toUri(), HttpMethod.GET, entity, List.class);
		
		List<Lookup> lookups = resolveList(responseEntity, LinkedList.class);
		Map<String,List<Lookup>> lookupMaps = new LinkedHashMap<String,List<Lookup>>();
		for (Lookup lookup : lookups) {
			String lookupGroupString = lookup.getLookupGroupString();
			if(lookupMaps.get(lookupGroupString)==null){
				lookupMaps.put(lookupGroupString, new LinkedList<Lookup>());
			}
			lookupMaps.get(lookupGroupString).add(lookup);
		}
		return lookupMaps;
	}
}
