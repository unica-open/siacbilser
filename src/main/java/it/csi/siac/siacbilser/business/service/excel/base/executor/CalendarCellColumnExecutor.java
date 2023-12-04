/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.base.executor;

import java.util.Calendar;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

public class CalendarCellColumnExecutor extends CellColumnExecutor {
	@Override
	public void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value) {
		cell.setCellValue((Calendar)value);
		cell.setCellStyle(styles.get("cell_normal_date"));
	}
	public static class Nullable extends CalendarCellColumnExecutor {
		@Override
		public void applyNullStyle(Map<String, CellStyle> styles, Cell cell) {
			cell.setCellValue((Calendar)null);
			cell.setCellStyle(styles.get("cell_normal_date"));
		}
	}
}
