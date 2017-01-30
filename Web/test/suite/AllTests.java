package suite;

import junit.framework.Test;
import junit.framework.TestSuite;
//import service.TestHoliday;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for suite");
		//$JUnit-BEGIN$
//		suite.addTestSuite(TestLdap.class);
		//suite.addTestSuite(TestHoliday.class);
		//$JUnit-END$
		return suite;
	}

}
