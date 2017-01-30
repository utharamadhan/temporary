package ldap;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import base.BaseTest;

public class TestLdap extends BaseTest {
	LdapTemplate ldapTemplate;
	
	public void setUp() throws Exception {
		super.setUp();
		ldapTemplate = (LdapTemplate) context.getBean("ldapTemplate");
	}
	
	@SuppressWarnings("unchecked")
	public void test() {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("uid", "alexander"));
		assertTrue(ldapTemplate.authenticate("ou=people", filter.toString(), "1234567890"));
//		boolean authenticated = ldapTemplate.authenticate(DistinguishedName.EMPTY_PATH, filter.toString(), password);
//		 
//		ldapTemplate.authenticate(base, filter, password, callback)
//		String filter = "(&(uid=alexander))";
//		List<String> result = ldapTemplate.search(
//		         "", filter,
//		         new AttributesMapper() {
//					public Object mapFromAttributes(Attributes attrs) throws NamingException {
//						// print out all attributes
//						NamingEnumeration<? extends Attribute> enumerate = attrs.getAll();
//						while (enumerate.hasMoreElements()) {
//							Attribute attribute = enumerate.nextElement();
//							System.out.println(attribute.getID() + ":" + attribute.get());
//						}
//						// returns only user password
//						return attrs.get("userPassword").get();
//					}
//				});
//		for(Object cn: result) {
//			System.out.println("User password = " + new String((byte [])cn));
//		}
//		assertNotNull(result);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
}
