package rest;

import id.base.app.SystemConstant;
import id.base.app.util.DateTimeFunction;
import id.base.app.util.dao.Operator;
import id.base.app.util.dao.SearchFilter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest extends TestCase{
	@Test
	public void testConvertDate() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setDateFormat(new SimpleDateFormat(SystemConstant.SYSTEM_TIME_MASK));
		Date dt = new Date();
		SearchFilter sf = new SearchFilter("abc",Operator.EQUALS,DateTimeFunction.date2String(dt,SystemConstant.SYSTEM_TIME_MASK),Date.class);
		System.out.println(sf.getValue());
		List<SearchFilter> sfs = new ArrayList<SearchFilter>();
		sfs.add(sf);
		System.out.println(dt);
		String dateVal = mapper.writeValueAsString(dt);
		String dateSf = mapper.writeValueAsString(sfs);
		System.out.println(dateVal);
		System.out.println(dateSf);
		Date date = mapper.readValue(dateVal, Date.class);
		List<SearchFilter> jsf = mapper.readValue(dateSf, new TypeReference<List<SearchFilter>>() {});
		System.out.println(date);
		System.out.println(jsf.get(0).getValue());
	}
}
