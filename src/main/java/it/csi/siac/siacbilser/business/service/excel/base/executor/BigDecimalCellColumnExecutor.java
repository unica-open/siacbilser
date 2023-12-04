/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.base.executor;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

public class BigDecimalCellColumnExecutor extends CellColumnExecutor {
	@Override
	public void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value) {
		cell.setCellValue(((BigDecimal)value).doubleValue());
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(styles.get("cell_decimal"));
	}
	public static class Nullable extends BigDecimalCellColumnExecutor {
		@Override
		public void applyNullStyle(Map<String, CellStyle> styles, Cell cell) {
			cell.setCellValue((String)null);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(styles.get("cell_decimal"));
		}
	}
}
