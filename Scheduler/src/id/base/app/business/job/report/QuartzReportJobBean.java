package id.base.app.business.job.report;


import id.base.app.SystemConstant;
import id.base.app.exception.SystemException;
import id.base.app.service.ReportService;
import id.base.app.util.JXlsUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * @author anicka andry
 *
 */
public abstract class QuartzReportJobBean extends QuartzJobBean implements InitializingBean{
	
	static Logger logger = LoggerFactory.getLogger(QuartzReportJobBean.class);

	protected abstract String getFileName() ;
	
	protected abstract String getReportOutputFolder();
	
	protected abstract String getJrxlsTemplate();
	
	protected abstract String getJrxlsTemplateDir();
	
	protected ReportService reportService; 
	
	protected abstract Object[] populateReportParam() ;
	
	protected abstract void populateReportMap(Map reportMap,Collection reportSource) ;

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	protected abstract Map<Object, Object> populateReportParameterMap();
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			Collection collectionSource = reportService.findReportCollection(populateReportParam());
			Map beans = new HashMap();
			populateReportMap(beans,collectionSource);
			beans.put("detail", new ArrayList(collectionSource));
			new JXlsUtil().write(getJrxlsTemplateDir()+"/" + getJrxlsTemplate() , getReportOutputFolder()+"/"+ getFileName() , beans);
			reportService.updateExportedObjects(new ArrayList(collectionSource));
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}catch (SystemException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		File reportOutputFolder = new File(getReportOutputFolder());
	    File reportFile = new File(getReportOutputFolder()+SystemConstant.PATH_SEPARATOR +  getFileName());
		
		if(!reportOutputFolder.isDirectory()){
			throw new RuntimeException(" fatal error , directory " + getReportOutputFolder() + " is not exists .. " );
		}
		
		if(getFileName()==null || getFileName()==""){
			throw new RuntimeException(" fatal error , fileName is empty .. " );
		}
		
		if(!reportFile.createNewFile()){
			throw new RuntimeException(" fatal error , file  cannot be created .. " );
		}

		if(getJrxlsTemplateDir()==null || getJrxlsTemplateDir()==""){
			throw new RuntimeException(" fatal error , Jrxls template dir is empty .. " );
		}
		
		if(getJrxlsTemplate()==null || getJrxlsTemplate()==""){
			throw new RuntimeException(" fatal error , Jrxls Template is empty .. " );
		}
		
		
	}
	

}
