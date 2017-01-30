package beanutils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;

public class TestBeanUtils extends TestCase {

	public void testConverter() {
//		assertEquals(0, ConvertUtils.convert("", Integer.class));
		IntegerConverter intConverter = new IntegerConverter(null);
		ConvertUtils.register(intConverter, Integer.class);
		assertNull(ConvertUtils.convert("", Integer.class));
		
		LongConverter longConverter = new LongConverter(null);
		ConvertUtils.register(longConverter, Long.class);
		assertNull(ConvertUtils.convert("", Long.class));
		
		DoubleConverter doubleConverter = new DoubleConverter(null);
		ConvertUtils.register(doubleConverter, Double.class);
		assertNull(ConvertUtils.convert("", Double.class));
	}
	
	public void testBeanPopulate() {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("integer", "");
		
		IntegerConverter intConverter = new IntegerConverter(null);
		ConvertUtils.register(intConverter, Integer.class);
		
		PopulateMe me = new PopulateMe();
		try {
			BeanUtils.populate(me, values);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static class PopulateMe {
		Integer integer;

		public Integer getInteger() {
			return integer;
		}

		public void setInteger(Integer integer) {
			this.integer = integer;
		}
		
		
	}
}
