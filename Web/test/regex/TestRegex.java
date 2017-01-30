package regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class TestRegex extends TestCase {
	public void testMatches() {
		String fileName = "aneka-foto_fuji-instax-mini-7-pink_pink_full01.jpg";
//		String fileName = "aneka-foto_fuji-instax-mini-7-pink_full01.jpg";
//		String pattern = "^([^_\"'\\s]*_[^_\"'\\s]*(_[^_\"'\\s]*)?)_full\\d+\\.[a-zA-Z0-9]+"; //".*_full\\d+\\.[a-zA-Z0-9]+";//\\S+";
		String pattern = "^(([^_\"'\\s]*_){2}(_[^_\"'\\s]*)?)_full\\d+\\.[a-zA-Z0-9]+"; //".*_full\\d+\\.[a-zA-Z0-9]+";//\\S+";
		Pattern p = Pattern.compile(pattern);
		System.out.println(p.matcher(fileName).matches());
		assertTrue(p.matcher(fileName).matches());
		System.out.println(fileName.matches(pattern));
		assertTrue(fileName.matches(pattern));
	}
	
	public void testRegex2() {
		String fileName = "aneka-foto_fuji-instax-mini-7-pink_pink_full";
		String pattern = "^([^\"'_\\s]*_[^\"'_\\s]*_([^\"'_\\s]*)?)_full"; //".*_full\\d+\\.[a-zA-Z0-9]+";//\\S+";
		Pattern p = Pattern.compile(pattern);
		System.out.println(p.matcher(fileName).matches());
		assertTrue(p.matcher(fileName).matches());
		System.out.println(fileName.matches(pattern));
		assertTrue(fileName.matches(pattern));
	}
	
	public void testReplace() {
		String pattern = "(_full)(\\d+)";
		String fileName = "test_full2.jpg";
		String expected = "test_thumb2.jpg";
		System.out.println(fileName.replaceAll(pattern,"_thumb$2"));
		assertEquals(expected, fileName.replaceAll(pattern,"_thumb$2"));
	}
	
	public void testNothing() {
		List<Long> test = new ArrayList<Long>();
		test.add(1l);
		test.add(2l);
		test.add(3l);
		
		System.out.println(test);
		
		String time1 = "10:30";
		String time2 = "10:30";
		
		int result = time1.compareTo(time2);
		
		if (result < 0) {
			System.out.println(time1 + " < " + time2);
		} else if (result == 0) {
			System.out.println(time1 + " = " + time2);
		} else {
			System.out.println(time1 + " > " + time2);
		}
	}
}
