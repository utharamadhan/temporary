package id.base.app.webMember.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//~--- JDK imports ------------------------------------------------------------

public abstract class POIUtil<T>
{
	static Logger logger = LoggerFactory.getLogger(POIUtil.class);

	/** Creates a new instance of POIExcelReader */
	public POIUtil()
	{}


	@SuppressWarnings ("unchecked")
	public static List<Object> readFromExcel (InputStream xlsDataStream, int sheetIndex, int rowNum) throws IOException
	{
		List<Object> cols = new ArrayList<Object>();
		
		POIFSFileSystem fileSystem = null;

		fileSystem = new POIFSFileSystem (xlsDataStream);

		HSSFWorkbook      workBook = new HSSFWorkbook (fileSystem);
		HSSFSheet         sheet    = workBook.getSheetAt (sheetIndex);
		HSSFRow row     = sheet.getRow(rowNum);

		// once get a row its time to iterate through cells.
		Iterator<Cell> cells = row.cellIterator();

		while (cells.hasNext ())
		{
			Cell cell = cells.next();
			
			//logger.debug("Cell {} : {}", cell.getRichStringCellValue().getString(), cell.getCellType());
			switch (cell.getCellType())
			{
				case HSSFCell.CELL_TYPE_NUMERIC :
				{
					cols.add(cell.getNumericCellValue());
					break;
				}

				case HSSFCell.CELL_TYPE_STRING :
				{
					RichTextString richTextString = cell.getRichStringCellValue ();
					cols.add(richTextString.getString ());
					break;
				}
				default :
				{
					cols.add(cell.getStringCellValue());
					break;
				}
			}
		}
		return cols;
	}
	
	public abstract List<T> buildObjectsFromExcel (InputStream xlsDataStream, int sheetIndex, Map<String, String> maps, Object[] otherProperties)
		throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException;
}						