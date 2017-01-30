package id.base.app.webMember.rest;

import id.base.app.exception.SystemException;
import id.base.app.rest.RestCaller;
import id.base.app.rest.RestConstant;
import id.base.app.rest.RestServiceConstant;
import id.base.app.valueobject.LogAuditTrail;

import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class AuditRestCaller extends RestCaller<LogAuditTrail>{

	public AuditRestCaller() {
		super(RestConstant.REST_SERVICE, RestServiceConstant.AUDIT_TRAIL_SERVICE);
	}

	public void createAuditBulk(List<LogAuditTrail> logAuditTrails) {
		URI url;
		try {
			url = new URI(RestConstant.REST_SERVICE+baseUrl+"/createBulk");
			ObjectMapper mapper = new ObjectMapper();
			String auditTrails = "";
			try {
				if(logAuditTrails!=null){
					auditTrails = mapper.writeValueAsString(logAuditTrails);
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				LOGGER.error("findAllByFilter error build order{}", e);
			}
			UriComponentsBuilder builder = UriComponentsBuilder.fromUri(url).queryParam("auditTrails", auditTrails);
			
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

			rt.exchange(builder.build().toUri(), HttpMethod.POST, entity, String.class);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			LOGGER.error("findAllByFilter rest error {}", e);
		}
	}
	
	public void createAudit(LogAuditTrail logAuditTrail) {
		super.saveOrUpdate(logAuditTrail);
	}
	
	public void createAuditWithSubCode(int code, String descr, String userName, String status, String clientHost, int subCode) throws SystemException {
		URI url;
		try{
			url = new URI(RestConstant.REST_SERVICE+baseUrl+"/createAuditWithSubCode");
			ObjectMapper mapper = new ObjectMapper();
			UriComponentsBuilder builder = UriComponentsBuilder.fromUri(url)
					.queryParam("code", code)
					.queryParam("descr", descr)
					.queryParam("userName", userName)
					.queryParam("status", status)
					.queryParam("clientHost", clientHost)
					.queryParam("subCode", subCode);
			
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

			rt.exchange(builder.build().toUri(), HttpMethod.POST, entity, String.class);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			LOGGER.error("findAllByFilter rest error {}", e);
		}
	}
	
	
	/**
	 * please use createAuditWithSubCode instead
	 */
	@Deprecated
	public void createAudit(int code,String descr,String userName,String status,String clientHost)throws SystemException{
		URI url;
		try {
			url = new URI(RestConstant.REST_SERVICE+baseUrl+"/createAudit");
			ObjectMapper mapper = new ObjectMapper();
			UriComponentsBuilder builder = UriComponentsBuilder.fromUri(url)
					.queryParam("code", code)
					.queryParam("descr", descr)
					.queryParam("userName", userName)
					.queryParam("status", status)
					.queryParam("clientHost", clientHost);
			
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

			rt.exchange(builder.build().toUri(), HttpMethod.POST, entity, String.class);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			LOGGER.error("findAllByFilter rest error {}", e);
		}
	}
	
	public OutputStream getCsvBackupFile(OutputStream os, Integer code)
			throws SystemException {
		return null;
	}
}
