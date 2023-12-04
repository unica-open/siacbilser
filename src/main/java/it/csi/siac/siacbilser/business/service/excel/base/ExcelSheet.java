/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class ExcelSheet<ER extends BaseExcelRow> {

	private Sheet sheet;
	private int rowCount = 0;
	private String password = null;
	protected List<ER> rows = new ArrayList<ER>();

	
	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public void incRowCount() {
		rowCount++;
	}

	public Object[] getHeaderParameters() {
		return new Object[0];
	}

	protected ExcelReportColumn[] getColumnTitles() {
		return null;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void protect() {
		if (password != null) {
			sheet.protectSheet(password);
		}
	}

	public Sheet createSheet(Workbook workbook) {
		sheet = workbook.createSheet(getTitle());
		
		sheet.setDisplayGridlines(false);
		sheet.setPrintGridlines(false);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);
		sheet.setAutobreaks(true);
		printSetup.setFitHeight((short)1);
		printSetup.setFitWidth((short)1);

		return sheet;
	}

	protected String getTitle() {
		return "Sheet 1";
	}

	public Row createRow() {
		return sheet.createRow(rowCount++);
	}

	protected List<? extends BaseExcelRow> getRows(){
		return rows;
	}
}
