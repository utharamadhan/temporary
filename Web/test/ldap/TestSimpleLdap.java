package ldap;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import junit.framework.TestCase;

public class TestSimpleLdap extends TestCase {
	public void test() {
		Hashtable authEnv = new Hashtable(11);
    	String userName = "alexander";
    	String passWord = "password";
    	String base = "ou=people";
    	String dn = "uid=" + userName + "," + base;
    	String ldapURL = "ldap://172.16.213.32:389";
 
    	authEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
   		authEnv.put(Context.PROVIDER_URL, ldapURL);
   		authEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
   		authEnv.put(Context.SECURITY_PRINCIPAL, dn);
   		authEnv.put(Context.SECURITY_CREDENTIALS, passWord);
 
    	try {
    		DirContext authContext = new InitialDirContext(authEnv);
    		System.out.println("Authentication Success!");
    	} catch (AuthenticationException authEx) {
    		System.out.println("Authentication failed!");
    	} catch (NamingException namEx) {
    		System.out.println("Something went wrong!");
    		namEx.printStackTrace();
    	}
	}

}
