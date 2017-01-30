package security;

import org.jasypt.digest.StringDigester;

import base.BaseTest;


public class TestDigester extends BaseTest {
	private StringDigester digester;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		digester = (StringDigester) getBean("digester");
	}

	public void testGenerate() {
		String password = "master";
		String result = digester.digest(password);
		System.out.println(result.length());
		System.out.println(result.trim().length());
		System.out.println(password +"=" + result);
	}
	
}
