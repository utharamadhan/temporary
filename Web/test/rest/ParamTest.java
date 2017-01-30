package rest;

import id.base.app.SystemParameter;
import id.base.app.rest.MapRestCaller;
import id.base.app.rest.QueryParamInterfaceRestCaller;
import id.base.app.rest.RestConstant;
import id.base.app.rest.RestServiceConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml", "classpath:configurationContext.xml"})
@WebAppConfiguration
public class ParamTest extends TestCase {
	@Test
	public void testParam() throws Exception {
		List<String> names = new ArrayList<String>();
		names.add(SystemParameter.NAME_SESSION_EXPIRED_INTERVAL);
		names.add(SystemParameter.NAME_MAX_RECORD_PER_SCREEN);
		final List<String> fnames = names; 
		MapRestCaller<String, Object> mrc = new MapRestCaller<String, Object>(RestConstant.REST_SERVICE, RestServiceConstant.SYSTEM_PARAMETER_SERVICE);
		Map<String,Object> parameterMap = mrc.executeGetMap(new QueryParamInterfaceRestCaller() {
			
			@Override
			public String getPath() {
				return "/findAppParametersByNames";
			}
			
			@Override
			public Map<String, Object> getParameters() {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("names", fnames);
				return map;
			}
		});
		int sessExpiredInterval = Integer.valueOf(String.valueOf(parameterMap.get(SystemParameter.NAME_SESSION_EXPIRED_INTERVAL)));
		System.out.println(sessExpiredInterval);
		/*MapRestCaller<Long, String> cmCaller = new MapRestCaller<Long, String>(RestConstant.REST_SERVICE, RestConstant.RM_PREMIUM_RATE,LinkedHashMap.class);
		Map<Long,String> calculationMethods = cmCaller.executeGetMap(new QueryParamInterfaceRestCaller() {
			
			@Override
			public String getPath() {
				return "/findAllPremiumRateCalculationMethod";
			}
			
			@Override
			public Map<String, Object> getParameters() {
				return null;
			}
		}, LinkedHashMap.class);*/
		/*assertTrue(calculationMethods.size()>0);*/
	}
}
