/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.csi.siac.siacbilser.business.service.stampa.base.BaseReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.base.ReportElaborationException;
import it.csi.siac.siaccommon.util.MimeType;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;

public abstract class BaseExcelReportHandler extends BaseReportHandler {
	private byte[] bytes;
	
	protected Map<String, CellStyle> styles;
	private Workbook workbook;
	private boolean xlsx;
	
	@Override
	protected void handleResponseBase() {
	}

	public byte[] getBytes(){
		return bytes;
	}
	
	
	@Override
	protected void elaborateDataBase() {
		init();
		elaborateData();
		postElaborateData();
	}

	protected void init() {
		workbook = xlsx ? new XSSFWorkbook() : new HSSFWorkbook();
		createStyles();
	}
	
	protected void postElaborateData() {}
	
	protected void addSheet(ExcelSheet<? extends BaseExcelRow> excelSheet) {
		excelSheet.createSheet(workbook);
		
		Row headerRow = excelSheet.createRow();
		headerRow.setHeightInPoints(12.75f);
		Object[] headerParameters = excelSheet.getHeaderParameters();
		
		int i = 0;
		for (ExcelReportColumn title : excelSheet.getColumnTitles()) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(title.getColumnName(headerParameters));
			cell.setCellStyle(styles.get("header"));
			i++;
		}

		for (BaseExcelRow report : excelSheet.getRows()) {
			Row row = excelSheet.createRow();
			int columnCount = 0;
			for (ExcelReportColumn columnName : excelSheet.getColumnTitles()) {
				setCellValueAndStyle(row.createCell(columnCount++), columnName, getColumnValue(report, columnName.getFieldName()));
			}
		}

		excelSheet.protect();
	}

	
	
	protected Object getColumnValue(BaseExcelRow report, String colName) {
		try {
			return report.getClass().getMethod("get" + StringUtils.capitalize(colName)).invoke(report);
		}
		catch (SecurityException e) {
			throw new IllegalArgumentException("Errore di sicurezza nella generazione della mappa per la classe "
					+ this.getClass() + " e campo " + colName, e);
		}
		catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Getter non trovato nella generazione della mappa per la classe "
					+ this.getClass() + " e campo " + colName, e);
		}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					"Errore nel passaggio degli argomenti al getter nella generazione della mappa per la classe "
							+ this.getClass() + " e campo " + colName,
					e);
		}
		catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Accesso non permesso nella generazione della mappa per la classe "
					+ this.getClass() + " e campo " + colName, e);
		}
		catch (InvocationTargetException e) {
			throw new IllegalArgumentException("Errore interno nella generazione della mappa per la classe "
					+ this.getClass() + " e campo " + colName, e);
		}
	}		
	
	
	@Override
	protected void generaReportBase() {
		final String methodName = "generaReportBase";
		log.info(methodName, "Inizio generazione report... ");
		try {
			generateBytes();
		} catch (IOException e) {
			String msg = "Errore durante la generazione dell'excel.";
			log.error(methodName, msg, e);
			throw new ReportElaborationException(msg, e);
		}
		
		log.debug(methodName, "Fine generazione report.");
	}

	protected void generateBytes() throws IOException{
		
		for (ExcelSheet<? extends BaseExcelRow> excelSheet : instantiateExcelSheets()) {
			addSheet(excelSheet);
		}
		
		autoSizingColumns(workbook);
	
		this.bytes = toBytes();
	}
	
	protected abstract ExcelSheet<? extends BaseExcelRow>[] instantiateExcelSheets();

	public byte[] toBytes() {
		final String methodName = "toBytes";
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			workbook.write(baos);
		} catch (IOException ioe) {
			log.debug(methodName, "Errore durante la scrittura del file excel: " + ioe.getMessage());
			throw new IllegalStateException("Errore generazione Excel.", ioe);
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				log.error(methodName, "Errore durante la chiusura del file: " + e.getMessage(), e);
				throw new IllegalStateException("Errore chiusura file Excel.", e);
			}
		}
		
		return baos.toByteArray();
	}
	
	protected void setCellValueAndStyle(Cell cell, ExcelReportColumn excelReportColumn, Object value) {
		ReportColumnType reportColumnType = ReportColumnType.byClass(excelReportColumn.getColumnType(), excelReportColumn.isNullable());
		Object transformedValue = reportColumnType.transformValue(value, cell.getRowIndex() + 1, cell.getColumnIndex());
		reportColumnType.applyToCell(styles, cell, transformedValue);
		reportColumnType.applyStyle(workbook, styles, cell, excelReportColumn.getColumnStyle());
	}

	private void createStyles(){
		styles = new HashMap<String, CellStyle>();
		DataFormat df = workbook.createDataFormat();
		
		CellStyle style;
		style = createFontStyle();
		styles.put("header", style);

		style = createBorderedStyle();
		style.setLocked(false);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setWrapText(true);
		styles.put("cell_normal", style);
		
		style = createBorderedStyle();
		style.setLocked(false);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("###,##0.00"));
		styles.put("cell_decimal", style);
		
		style = createBorderedStyle();
		style.setLocked(false);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("###,##0.00000"));
		styles.put("cell_decimal_five", style);

		style = createBorderedStyle();
		style.setLocked(false);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		styles.put("cell_normal_centered", style);

		style = createBorderedStyle();
		style.setLocked(false);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("dd/mm/YYYY"));
		styles.put("cell_normal_date", style);

		style = createBorderedStyle();
		style.setLocked(false);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setIndention((short)1);
		style.setWrapText(true);
		styles.put("cell_indented", style);

		style = createBorderedStyle();
		style.setLocked(false);
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styles.put("cell_blue", style);
		
		style = workbook.createCellStyle();
		style.setLocked(false);
		style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		styles.put("cell_warn", style);
	}

	private CellStyle createFontStyle() {
		Font headerFont = workbook.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		
		CellStyle style = createBorderedStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		return style;
	}
	
	private CellStyle createBorderedStyle(){
		CellStyle style = workbook.createCellStyle();
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}
	
	protected void autoSizingColumns(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	    	// Fogli
	    	Sheet sheet = workbook.getSheetAt(i);
	        if (sheet.getPhysicalNumberOfRows() > 0) {
	        	// Righe
	        	Row row = sheet.getRow(sheet.getFirstRowNum());
	            Iterator<Cell> cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
	                // Celle
	            	Cell cell = cellIterator.next();
	                int columnIndex = cell.getColumnIndex();
	                sheet.autoSizeColumn(columnIndex);
	            }
	        }
	    }
	}

	public MimeType getContentType() {
		return xlsx ? MimeType.XLSX : MimeType.XLS;
	}
	
	public String getExtension() {
		return xlsx ? "xlsx" : "xls";
	}


	public boolean isXlsx() {
		return xlsx;
	}

	public void setXlsx(boolean isXlsx) {
		this.xlsx = isXlsx;
	}

	@Override
	protected String getReportXml() {
		return null;
	}

	@Override
	public String getCodiceTemplate() {
		return null;
	}

	@Override
	protected void handleResponse(GeneraReportResponse res) {
	}
}
