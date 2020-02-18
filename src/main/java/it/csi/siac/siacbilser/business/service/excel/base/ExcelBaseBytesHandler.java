/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.csi.siac.siacbilser.business.service.stampa.base.BaseReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.base.ReportElaborationException;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.model.report.ReportColumn;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;

/**
 * 
 * Specializza BaseReportHandler per l'utilizzo di JAXB come motore di generazione dell'xml.
 * 
 * @author Elisa Chiari
 * @param <T> la tipizzazione del modello del report
 */
public abstract class ExcelBaseBytesHandler<T> extends BaseReportHandler {
	private byte[] bytes;
	
	private Map<String, CellStyle> styles;
	private Workbook workbook;
	private Sheet sheet;
	private String sheetTitle = "Sheet 1";
	private int rowCount;
	private boolean isXLSX;
	protected boolean isRisultatoLimitato;
	protected boolean isColumnAutosize;
	
	protected T reportModel;
	private List<ReportColumn> headerTitles;
	
	@Override
	protected void handleResponseBase() {
		// Non devo effetture i controlli aggiuntivi
	}

	/**
	 * @return the sheet
	 */
	public Sheet getSheet() {
		return sheet;
	}

	/**
	 * @return the rows
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRowCount(int rows) {
		this.rowCount = rows;
	}

	/**
	 * @return the headerTitles
	 */
	public List<ReportColumn> getHeaderTitles() {
		return headerTitles;
	}

	/**
	 * @param headerTitles the headerTitles to set
	 */
	public void setHeaderTitles(List<ReportColumn> headerTitles) {
		this.headerTitles = headerTitles;
	}
	
	/**
	 * @return the bytes
	 */
	public byte[] getBytes(){
		return bytes;
	}
	
	@Override
	protected void elaborateDataBase() {
		reportModel = Utility.instantiateGenericType(this.getClass(), ExcelBaseBytesHandler.class, 0);
		init();
		elaborateData();
	}

	/**
	 * Inizializzazione della stampa.
	 */
	protected void init() {
		instantiateWorkbook();
		createStyles();
	}
	
	/**
	 * Aggiunge uno sheet con il titolo di default
	 */
	protected void addSheet() {
		addSheet(getSheetTitle());
	}
	
	/**
	 * Aggiunge uno sheet con dato titolo
	 * @param sheetTitle il titolo dello sheet
	 */
	protected void addSheet(String sheetTitle) {
		sheet = createSheet(sheetTitle);
		 
		//turn off gridlines
		sheet.setDisplayGridlines(false);
		sheet.setPrintGridlines(false);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		
		createPrintSetup();
	}

	/**
	 * Crea la riga degli header
	 */
	private void createHeaderRow() {
		rowCount = 0;
		Row headerRow = sheet.createRow(rowCount++);
		headerRow.setHeightInPoints(12.75f);
		Object[] headerParameters = instantiateHeaderParameters();
		
		int i = 0;
		for (ReportColumn title : headerTitles) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(title.getColumnName(headerParameters));
			cell.setCellStyle(styles.get("header"));
			i++;
		}
	}

	/**
	 * Instanziazione dei parametri da utilizzare negli headers
	 * @return i parametri degli header
	 */
	protected Object[] instantiateHeaderParameters() {
		return new Object[0];
	}

	/**
	 * Crea il setup di stampa
	 */
	private void createPrintSetup() {
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);
		//the following three statements are required only for HSSF
		sheet.setAutobreaks(true);
		printSetup.setFitHeight((short)1);
		printSetup.setFitWidth((short)1);
	}
	
	@Override
	protected void generaReportBase() {
		final String methodName = "generaReportBase";
		log.info(methodName, "Inizio generazione report... ");
		try {
			generaBytes();
		} catch (IOException e) {
			String msg = "Errore durante la generazione dell'excel.";
			log.error(methodName, msg, e);
			throw new ReportElaborationException(msg, e);
		}
		
		log.debug(methodName, "Fine generazione report.");
	}

	/**
	 * Generazione del byte array
	 * @throws IOException
	 */
	protected void generaBytes() throws IOException{
		//aggiungo un foglio al workbook
		addSheet();
		//Creo la riga di intestazione
		createHeaderRow();
		//aggiungo le righe
		addRows();
	
		this.bytes = toBytes();
	}
	
	/**
	 * Crea un nuovo sheet
	 * @param sheetTitle il titolo dello sheet
	 * @return lo sheet creato
	 */
	private Sheet createSheet(String sheetTitle) {
		return workbook.createSheet(sheetTitle);
	}

	/**
	 * Instanziazione del workbook
	 */
	private void instantiateWorkbook() {
		if(isXLSX) {
			workbook = new XSSFWorkbook();
		} else {
			workbook = new HSSFWorkbook();
		}
	}
	
	/**
	 * Aggiunta delle righe con i dati.
	 * <pre>
	 * for(Entita ec : entitas){
	 *     Map<String, Object> campiColonne = getDatiColonne();
	 *     Row row = sheet.createRow(rows++);
	 *     int i = 0;
	 *     for (String key : headerTitles) {
	 *         Cell cell = row.createCell(i++);
	 *         setCellValueAndStyle(cell, campiColonne.get(key));
	 *     }
	 * }
	 * </pre>
	 * 
	 */
	protected abstract void addRows();

	/**
	 * Trasfornamzione in bytes.
	 * @param isRisultatoLimitato se il risultato sia limitato
	 * @return i bytes corrispondenti al file excel
	 * @throws IllegalStateException in caso di errore nella generazione del file
	 */
	public byte[] toBytes() {
		final String methodName = "toBytes";
		// XXX: valutare se mantenere ovvero togliere
		if(isRisultatoLimitato){
			rowCount++;
			Row warnRow = sheet.createRow(rowCount);
			warnRow.setHeightInPoints(30f);
			Cell cell = warnRow.createCell(0);
			cell.setCellValue("Attenzione! Risultato limitato a " + (rowCount - 2) + " elementi.");
			cell.setCellStyle(styles.get("cell_warn"));
		}
		
		// Autosize delle colonne...
		if(isColumnAutosize) {
			for (int j = 0; j < headerTitles.size(); j++) {
				sheet.autoSizeColumn(j, false);
			}
		}
		
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
	
	

	/**
	 * Imposta il valore e lo stile nella cella.
	 * 
	 * @param cell la cella da popolare
	 * @param reportColumn i dati della colonna 
	 * @param value l'oggetto popolante
	 */
	protected void setCellValueAndStyle(Cell cell, ReportColumn reportColumn, Object value) {
		ReportColumnType reportColumnType = ReportColumnType.byClass(reportColumn.getColumnType());
		reportColumnType.applyToCell(styles, cell, value);
	}

	/**
	 * Creazione degli stili.
	 */
	private void createStyles(){
		styles = new HashMap<String, CellStyle>();
		DataFormat df = workbook.createDataFormat();

		CellStyle style;
		style = createFontStyle();
		styles.put("header", style);

		style = createBorderedStyle();
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setWrapText(true);
		styles.put("cell_normal", style);
		
		style = createBorderedStyle();
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("###,##0.00"));
		styles.put("cell_decimal", style);

		style = createBorderedStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		styles.put("cell_normal_centered", style);

		style = createBorderedStyle();
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("dd/mm/YYYY"));
		styles.put("cell_normal_date", style);

		style = createBorderedStyle();
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setIndention((short)1);
		style.setWrapText(true);
		styles.put("cell_indented", style);

		style = createBorderedStyle();
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styles.put("cell_blue", style);
		
		style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		styles.put("cell_warn", style);
	}

	/**
	 * Creazione del font style
	 * @return lo stile
	 */
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
	
	/**
	 * Crea uno stile con bordo.
	 * @return lo stile creato
	 */
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
	
	/**
	 * @return the content type
	 */
	public String getContentType() {
		return isXLSX ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" : "application/vnd.ms-excel";
	}
	
	/**
	 * @return the extension
	 */
	public String getExtension() {
		return isXLSX ? "xlsx" : "xls";
	}

	/**
	 * @return the sheetTitle
	 */
	public String getSheetTitle() {
		return sheetTitle;
	}

	/**
	 * @param sheetTitle the sheetTitle to set
	 */
	public void setSheetTitle(String sheetTitle) {
		this.sheetTitle = sheetTitle;
	}

	/**
	 * @return the isXLSX
	 */
	public boolean isXLSX() {
		return isXLSX;
	}

	/**
	 * @param isXLSX the isXLSX to set
	 */
	public void setXLSX(boolean isXLSX) {
		this.isXLSX = isXLSX;
	}

	@Override
	protected String getReportXml() {
		// Non da usare
		return null;
	}

	@Override
	public String getCodiceTemplate() {
		// Non da usare
		return null;
	}

	@Override
	protected void handleResponse(GeneraReportResponse res) {
		// Non da usare
	}
}
