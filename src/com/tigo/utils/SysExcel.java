package com.tigo.utils;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class SysExcel {

	public static Logger log = Logger.getLogger(SysExcel.class);

	synchronized public static void pintarCabecera(HSSFWorkbook wb) {
		try {
			if (wb != null) {
				HSSFSheet sheet = wb.getSheetAt(0);
				HSSFRow header = sheet.getRow(0);

				HSSFCellStyle cellStyle = wb.createCellStyle();
				cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

				HSSFFont cabecera = wb.createFont();
				cabecera.setColor(HSSFColor.WHITE.index);
				cabecera.setFontHeightInPoints((short) 10);
				cabecera.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				cabecera.setFontName("Tahoma");
				cellStyle.setFont(cabecera);

				for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
					HSSFCell cell = header.getCell(i);

					cell.setCellStyle(cellStyle);
				}
			} else {
				log.warn("[pintarCabecera] La hoja excel es nulo.");
			}
		} catch (Exception e) {
			log.error("[pintarCabecera] Fallo al pintar la cabecera.", e);
		}
	}

}
