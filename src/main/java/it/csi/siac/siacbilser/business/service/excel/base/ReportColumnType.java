/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.base;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

public enum ReportColumnType {

	STRING(String.class, new CellColumnExecutor() {
		@Override
		void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value) {
			cell.setCellValue((String)value);
			cell.setCellStyle(styles.get("cell_normal"));
		}
	}),
	DATE(Date.class, new CellColumnExecutor() {
		@Override
		void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value) {
			cell.setCellValue((String)value);
			cell.setCellStyle(styles.get("cell_normal_date"));
		}
	}),
	CALENDAR(Calendar.class, new CellColumnExecutor() {
		@Override
		void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value) {
			cell.setCellValue((String)value);
			cell.setCellStyle(styles.get("cell_normal_date"));
		}
	}),
	BIG_DECIMAL(BigDecimal.class, new CellColumnExecutor() {
		@Override
		void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value) {
			cell.setCellValue(((BigDecimal)value).doubleValue());
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(styles.get("cell_decimal"));
		}
	}),
	BOOLEAN(Boolean.class, new CellColumnExecutor() {
		@Override
		void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value) {
			cell.setCellValue((String)value);
			cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
			cell.setCellStyle(styles.get("cell_normal"));
		}
	}),
	INTEGER(Integer.class, new CellColumnExecutor() {
		@Override
		void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value) {
			cell.setCellValue((Integer)value);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(styles.get("cell_normal"));
		}
	}),
	;
	
	private final Class<?> clazz;
	private final CellColumnExecutor executor;
	
	private ReportColumnType(Class<?> clazz, CellColumnExecutor executor) {
		this.clazz = clazz;
		this.executor = executor;
	}
	
	public static ReportColumnType byClass(Class<?> clazz) {
		for(ReportColumnType type : values()) {
			if(type.clazz.isAssignableFrom(clazz)) {
				return type;
			}
		}
		return null;
		// FIXME: errore?
	}
	
	public void applyToCell(Map<String, CellStyle> styles, Cell cell, Object value) {
		this.executor.applyOnCellBase(styles, cell, value);
	}
	
	private abstract static class CellColumnExecutor {
		void applyOnCellBase(Map<String, CellStyle> styles, Cell cell, Object value) {
			if(value == null){
				applyNullStyle(styles, cell);
				return;
			}
			applyOnCell(styles, cell, value);
		}
		void applyNullStyle(Map<String, CellStyle> styles, Cell cell) {
			cell.setCellValue("ND");
			cell.setCellStyle(styles.get("cell_normal"));
		}
		abstract void applyOnCell(Map<String, CellStyle> styles, Cell cell, Object value);
	}
	
}
