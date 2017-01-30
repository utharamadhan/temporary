package misc;

import org.jasypt.encryption.StringEncryptor;

import base.BaseTest;

public class TestEncryptor extends BaseTest {
	StringEncryptor encryptor;
	
	public void setUp() throws Exception {
		super.setUp();
		encryptor = (StringEncryptor) context.getBean("encryptor"); 
	}
	
	public void testEncrypt() {
		String stringToEncrypt = "m@st3r";
		String result = encryptor.encrypt(stringToEncrypt);
		String expected = "ocxKUuIdQrQ=";
		System.out.println(result);
		assertEquals(expected, result);
	}
	
	public void testDecrypt() {
		String stringToDecrypt = "ocxKUuIdQrQ=";
		String expected = "m@st3r";
		String result = encryptor.decrypt(stringToDecrypt);
		
		System.out.println(result);
		assertEquals(expected, result);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		encryptor = null;
	}

}
