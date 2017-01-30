package report;

import id.base.app.util.JXlsUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import base.BaseTest;

public class TestGenerateUploadProductForSCM extends BaseTest {
	

	public void test() throws FileNotFoundException {
		String templateFile = "/opt/template/upload-for-scm-report-template.xls";
		FileInputStream stream = new FileInputStream(templateFile);
		
		try {
			List<List<String>> strList = new LinkedList<List<String>>();
			LinkedList<String> a = new LinkedList<String>();
			a.add("Test 1");
			a.add("Test 2");
			strList.add(a);
			LinkedList<String> b = new LinkedList<String>();
			b.add("Test 3");
			b.add("Test 4");
			b.add("Test 5");
			strList.add(b);
			
			Map<String, List<?>> dataMap = new HashMap<String, List<?>>();
			dataMap.put("columns", strList);
			byte[] bytes = new JXlsUtil<String>().write(stream,dataMap);
			File f2 = new File("/opt/report/MerchantReport/merchantReport.xls");
	   	    OutputStream out = new FileOutputStream(f2);
	   	    out.write(bytes);
	   	    out.close();
			//returPickupProblemTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void queryTest(){
//		IUploadProductSCMReportDAO uploadProductSCMReportDao = (IUploadProductSCMReportDAO) getBean("uploadProductSCMReportDAO");
//		List<Long> pks = uploadProductSCMReportDao.findProductSKUUploadPKsForReportGeneration();
//		List<String> names = uploadProductSCMReportDao.findProductSKUUploadAttributeNamesForReportGeneration(pks);
//		System.out.print(pks.size());
//		System.out.print(names.size());
	}
}
