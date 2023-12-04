/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.base.executor;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public class FormulaCellColumnExecutor extends CellColumnExecutor {
	@Override
	public void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value) {
		cell.setCellFormula((String)value);
		cell.setCellType(Cell.CELL_TYPE_FORMULA);
		cell.setCellStyle(styles.get("cell_normal"));
	}
	@Override
	public void applyStyle(Workbook workbook, Map<String, CellStyle> styles, Cell cell, String styleName) {
		if(styleName == null) {
			return;
		}
		// Lock
		CellStyle oldStyle = styles.get(styleName);
		CellStyle newStyle = workbook.createCellStyle();
		newStyle.cloneStyleFrom(oldStyle);
		newStyle.setLocked(true);
		cell.setCellStyle(newStyle);
	}
	@Override
	public Object transformValue(Object value, int row, int column) {
		return ((String) value)
			.replaceAll("%ROW%", Integer.toString(row))
			.replaceAll("%COLUMN%", Integer.toString(column));
	}
}
