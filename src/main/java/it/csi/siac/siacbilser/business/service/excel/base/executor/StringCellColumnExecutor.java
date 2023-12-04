/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.base.executor;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

public class StringCellColumnExecutor extends CellColumnExecutor {
	@Override
	public void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value) {
		cell.setCellValue((String)value);
		cell.setCellStyle(styles.get("cell_normal"));
	}
	
	public static class Nullable extends StringCellColumnExecutor {
		@Override
		public void applyNullStyle(Map<String, CellStyle> styles, Cell cell) {
			cell.setCellValue((String)null);
			cell.setCellStyle(styles.get("cell_normal"));
		}
	}
}
