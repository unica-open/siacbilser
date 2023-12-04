/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * The Class SpreadsheetData.
 */
public class SpreadsheetData {

	/** The data. */
	private transient Collection<Object[]> data = null;
	//private int maxrow;
	
	/**
	 * Instantiates a new spreadsheet data.
	 */
	public SpreadsheetData() {
	}

	/**
	 * Instantiates a new spreadsheet data.
	 *
	 * @param excelInputStream the excel input stream
	 * @throws Exception the exception
	 */
	public SpreadsheetData(final InputStream excelInputStream) throws Exception {
		this(excelInputStream, "");
	}
	
	/**
	 * Instantiates a new spreadsheet data.
	 *
	 * @param excelInputStream the excel input stream
	 * @param nameSh the name sh
	 * @throws Exception the exception
	 */
	public SpreadsheetData(final InputStream excelInputStream, String nameSh) throws Exception {
		this.data = loadFromSpreadsheet(excelInputStream, nameSh);
	}


	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Collection<Object[]> getData() {
		return data;
	}

	/**
	 * Load from spreadsheet.
	 *
	 * @param excelFile the excel file
	 * @param nameSh the name sh
	 * @return the collection
	 * @throws Exception the exception
	 */
	private Collection<Object[]> loadFromSpreadsheet(final InputStream excelFile, String nameSh)
	throws Exception {
		HSSFWorkbook workbook = null;
		try{
			workbook = new HSSFWorkbook(excelFile);
		} catch(IOException e) {
			//System.out.println("FILE PARAMETRI NON PRESENTE: " + excelFile);
			throw e;
		}

		int numSh=0;
		try{
			numSh=workbook.getSheetIndex(nameSh);
		} catch(IllegalArgumentException e) {
			//System.out.println("PAGINA NON PRESENTE: " + excelFile);
			throw e;
		}

		Sheet sheet = null;
		try{
			sheet = workbook.getSheetAt(numSh);
		} catch(IllegalArgumentException e) {
			//System.out.println("PAGINA NON PRESENTE: " + numSh);
			throw e;
		}
		
		int nrow=sheet.getPhysicalNumberOfRows();
		
		//maxrow=nrow-1;

		List<Object[]> rows = new ArrayList<Object[]>();
		if (nrow > 0)
		{
			int numberOfColumns = countNonEmptyColumns(sheet);
			
			List<Object> rowData = new ArrayList<Object>();
			Row row = null;
			//for (Row row : sheet) {
			for (int indrow = 1; indrow < nrow; indrow++) {
				row = sheet.getRow(indrow);
				if (isEmpty(row)) {
					break;
				} else {
					rowData.clear();
					for (int column = 0; column < numberOfColumns; column++) {
						Cell cell = row.getCell(column);
						rowData.add(objectFrom(workbook, cell));
					}
					rows.add(rowData.toArray());
				}
			}
		}
		else {
			 System.out.println("ERRORE: FOGLIO PARAMETRI VUOTO !!!!");
		}
		return rows;
	}

	/**
	 * Checks if is empty.
	 *
	 * @param row the row
	 * @return true, if is empty
	 */
	private boolean isEmpty(final Row row) {
		Cell firstCell = row.getCell(0);
		boolean rowIsEmpty = (firstCell == null)
				|| (firstCell.getCellType() == Cell.CELL_TYPE_BLANK);
		return rowIsEmpty;
	}

	/**
	 * Count the number of columns, using the number of non-empty cells in the
	 * first row.
	 *
	 * @param sheet the sheet
	 * @return the int
	 */
	private int countNonEmptyColumns(final Sheet sheet) {
		Row firstRow = sheet.getRow(0);
		return firstEmptyCellPosition(firstRow);
	}

	/**
	 * First empty cell position.
	 *
	 * @param cells the cells
	 * @return the int
	 */
	private int firstEmptyCellPosition(final Row cells) {
		int columnCount = 0;
		for (Cell cell : cells) {
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				break;
			}
			columnCount++;
		}
		return columnCount;
	}

	/**
	 * Object from.
	 *
	 * @param workbook the workbook
	 * @param cell the cell
	 * @return the object
	 */
	private Object objectFrom(final HSSFWorkbook workbook, final Cell cell) {
		Object cellValue = null;

		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			cellValue = cell.getRichStringCellValue().getString();
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			cellValue = getNumericCellValue(cell);
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			cellValue = cell.getBooleanCellValue();
		} else if (cell.getCellType() ==Cell.CELL_TYPE_FORMULA) {
			cellValue = evaluateCellFormula(workbook, cell);
		}

		return cellValue;
	
	}

	/**
	 * Gets the numeric cell value.
	 *
	 * @param cell the cell
	 * @return the numeric cell value
	 */
	private Object getNumericCellValue(final Cell cell) {
		Object cellValue;
		if (DateUtil.isCellDateFormatted(cell)) {
			cellValue = new Date(cell.getDateCellValue().getTime());
		} else {
			cellValue = cell.getNumericCellValue();
		}
		return cellValue;
	}

	/**
	 * Evaluate cell formula.
	 *
	 * @param workbook the workbook
	 * @param cell the cell
	 * @return the object
	 */
	private Object evaluateCellFormula(final HSSFWorkbook workbook, final Cell cell) {
		FormulaEvaluator evaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();
		CellValue cellValue = evaluator.evaluate(cell);
		Object result = null;
		
		if (cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			result = cellValue.getBooleanValue();
		} else if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			result = cellValue.getNumberValue();
		} else if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
			result = cellValue.getStringValue();
		}
		
		return result;
	}

	/**
	 * @return the maxrows
	 
	public int getMaxrows() {
		return maxrow;
	}*/
}
