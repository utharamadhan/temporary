package base;

import java.io.File;

import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BaseTest extends TestCase {
	protected static BeanFactory context;
	
	public void setUp() throws Exception {
		if (context == null) {
			System.out.println(new File("WebContent/WEB-INF/").getAbsolutePath());
			context = new FileSystemXmlApplicationContext(new String[] {"WebContent/WEB-INF/applicationContext.xml",
								"WebContent/WEB-INF/ldapContext.xml","WebContent/WEB-INF/configurationContextTest.xml"});
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
		context = null;
	}
	
	protected Object getBean(String beanName) {
		return context.getBean(beanName);
	}
}
