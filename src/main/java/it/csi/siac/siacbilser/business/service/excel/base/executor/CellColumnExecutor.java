/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.base.executor;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class CellColumnExecutor {
	public void applyOnCellBase(Map<String, CellStyle> styles, Cell cell, Object value) {
		if(value == null){
			applyNullStyle(styles, cell);
			return;
		}
		applyOnCell(styles, cell, value);
	}
	public void applyNullStyle(Map<String, CellStyle> styles, Cell cell) {
		cell.setCellValue("ND");
		cell.setCellStyle(styles.get("cell_normal"));
	}
	public abstract void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value);
	
	public void applyStyle(Workbook workbook, Map<String, CellStyle> styles, Cell cell, String styleName) {
		if(styleName == null) {
			return;
		}
		cell.setCellStyle(styles.get(styleName));
	}
	
	public Object transformValue(Object value, int row, int column) {
		return value;
	}
}
