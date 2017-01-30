package id.base.app.webMember.fileManager;

import id.base.app.SystemConstant;
import id.base.app.rest.RestCaller;
import id.base.app.util.DateTimeFunction;
import id.base.app.util.FileManager;
import id.base.app.util.dao.SearchFilter;
import id.base.app.util.dao.SearchOrder;
import id.base.app.webMember.DataTableCriterias;
import id.base.app.webMember.controller.BaseController;

import java.io.File;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Scope(value="request")
@Controller
@RequestMapping("/fileManager")
public class FileManagerWebController extends BaseController<Path> {
	
	@Override
	protected RestCaller<Path> getRestCaller() {
		return null;
	}

	@Override
	protected List<SearchFilter> convertForFilter(Map<String, String> paramWrapper) {
		return null;
	}

	@Override
	protected List<SearchFilter> convertForFilter(HttpServletRequest request, Map<String, String> paramWrapper, DataTableCriterias columns) {
		return null;
	}

	@Override
	protected List<SearchOrder> getSearchOrder() {
		return null;
	}
	
	@Override
	protected String getListPath() {
		return null;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/upload")
	@ResponseBody
	public String upload(@RequestParam("file") final MultipartFile file, ModelMap modelMap, HttpServletRequest request) {
		String fileName = "";
		try{
			String subfolderPerDay = DateTimeFunction.date2String(Calendar.getInstance().getTime(), SystemConstant.SYSTEM_DATE_MASK_NO_DELIMITER);
			FileManager.createDir(SystemConstant.FILE_STORAGE + SystemConstant.FILE_CONTENT_DIRECTORY + subfolderPerDay);
			String originalFileName = file.getOriginalFilename();
			String fileType = originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName.length());
			fileName = subfolderPerDay + File.separator + "BTN_" + DateTimeFunction.date2String(new Date(), SystemConstant.SYSTEM_DATE_TIME_NO_DELIMITER) + "." + fileType;
			file.transferTo(new File(SystemConstant.FILE_STORAGE + SystemConstant.FILE_CONTENT_DIRECTORY + fileName));
			return SystemConstant.IMAGE_SHARING_URL + SystemConstant.FILE_CONTENT_DIRECTORY + fileName;
		}catch(Exception e){
			LOGGER.debug(e.getMessage());
		}
		return fileName;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/uploadFeaturedImage")
	@ResponseBody
	public String uploadFeaturedImage(@RequestParam("file") final MultipartFile file, ModelMap modelMap, HttpServletRequest request) {
		String fileName = "";
		try{
			String subfolderPerDay = DateTimeFunction.date2String(Calendar.getInstance().getTime(), SystemConstant.SYSTEM_DATE_MASK_NO_DELIMITER);
			FileManager.createDir(SystemConstant.FILE_STORAGE + SystemConstant.FILE_FEATURED_IMAGE_DIRECTORY + subfolderPerDay);
			String originalFileName = file.getOriginalFilename();
			String fileType = originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName.length());
			fileName = subfolderPerDay + File.separator + "BTN_" + DateTimeFunction.date2String(new Date(), SystemConstant.SYSTEM_DATE_TIME_NO_DELIMITER) + "." + fileType;
			file.transferTo(new File(SystemConstant.FILE_STORAGE + SystemConstant.FILE_FEATURED_IMAGE_DIRECTORY + "/" + fileName));
			return SystemConstant.IMAGE_SHARING_URL + SystemConstant.FILE_FEATURED_IMAGE_DIRECTORY + fileName;
		}catch(Exception e){
			LOGGER.debug(e.getMessage());
		}
		return fileName;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/uploadEBookFile")
	@ResponseBody
	public String uploadEBookFile(@RequestParam("file") final MultipartFile file, ModelMap modelMap, HttpServletRequest request) {
		String fileName = "";
		try{
			String subfolderPerDay = DateTimeFunction.date2String(Calendar.getInstance().getTime(), SystemConstant.SYSTEM_DATE_MASK_NO_DELIMITER);
			FileManager.createDir(SystemConstant.FILE_STORAGE + SystemConstant.FILE_EBOOK_DIRECTORY + subfolderPerDay);
			String originalFileName = file.getOriginalFilename();
			String fileType = originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName.length());
			fileName = subfolderPerDay + File.separator + "BTN_" + DateTimeFunction.date2String(new Date(), SystemConstant.SYSTEM_DATE_TIME_NO_DELIMITER) + "." + fileType;
			file.transferTo(new File(SystemConstant.FILE_STORAGE + SystemConstant.FILE_EBOOK_DIRECTORY + "/" + fileName));
			return SystemConstant.IMAGE_SHARING_URL + SystemConstant.FILE_EBOOK_DIRECTORY + fileName;
		}catch(Exception e){
			LOGGER.debug(e.getMessage());
		}
		return fileName;
	}

}
