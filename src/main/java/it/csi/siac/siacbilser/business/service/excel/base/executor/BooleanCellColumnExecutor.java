/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.base.executor;

import java.util.Date;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

public class BooleanCellColumnExecutor extends CellColumnExecutor {
	@Override
	public void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value) {
		cell.setCellValue((Boolean)value);
		cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
		cell.setCellStyle(styles.get("cell_normal"));
	}
	public static class Nullable extends BooleanCellColumnExecutor {
		@Override
		public void applyNullStyle(Map<String, CellStyle> styles, Cell cell) {
			cell.setCellValue((String)null);
			cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
			cell.setCellStyle(styles.get("cell_normal"));
		}
	}
}
