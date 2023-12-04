/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.base.executor;

import java.util.Date;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

public class DateCellColumnExecutor extends CellColumnExecutor {
	@Override
	public void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value) {
		cell.setCellValue((Date)value);
		cell.setCellStyle(styles.get("cell_normal_date"));
	}
	
	public static class Nullable extends DateCellColumnExecutor {
		@Override
		public void applyNullStyle(Map<String, CellStyle> styles, Cell cell) {
			cell.setCellValue((Date)null);
			cell.setCellStyle(styles.get("cell_normal_date"));
		}
	}
}
