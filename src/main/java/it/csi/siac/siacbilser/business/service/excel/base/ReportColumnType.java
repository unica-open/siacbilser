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
import org.apache.poi.ss.usermodel.Workbook;

import it.csi.siac.siacbilser.business.service.excel.base.executor.BigDecimalCellColumnExecutor;
import it.csi.siac.siacbilser.business.service.excel.base.executor.BooleanCellColumnExecutor;
import it.csi.siac.siacbilser.business.service.excel.base.executor.CalendarCellColumnExecutor;
import it.csi.siac.siacbilser.business.service.excel.base.executor.CellColumnExecutor;
import it.csi.siac.siacbilser.business.service.excel.base.executor.DateCellColumnExecutor;
import it.csi.siac.siacbilser.business.service.excel.base.executor.FormulaCellColumnExecutor;
import it.csi.siac.siacbilser.business.service.excel.base.executor.IntegerCellColumnExecutor;
import it.csi.siac.siacbilser.business.service.excel.base.executor.StringCellColumnExecutor;

public enum ReportColumnType {

	STRING(String.class, false, new StringCellColumnExecutor()),
	DATE(Date.class, false, new DateCellColumnExecutor()),
	CALENDAR(Calendar.class, false, new CalendarCellColumnExecutor()),
	BIG_DECIMAL(BigDecimal.class, false, new BigDecimalCellColumnExecutor()),
	BOOLEAN(Boolean.class, false, new BooleanCellColumnExecutor()),
	INTEGER(Integer.class, false, new IntegerCellColumnExecutor()),
	FORMULA(Formula.class, false, new FormulaCellColumnExecutor()),
	// Nullable-types
	STRING_NULLABLE(String.class, true, new StringCellColumnExecutor.Nullable()),
	DATE_NULLABLE(Date.class, true, new DateCellColumnExecutor.Nullable()),
	CALENDAR_NULLABLE(Calendar.class, true, new CalendarCellColumnExecutor.Nullable()),
	BIG_DECIMAL_NULLABLE(BigDecimal.class, true, new BigDecimalCellColumnExecutor.Nullable()),
	BOOLEAN_NULLABLE(Boolean.class, true, new BooleanCellColumnExecutor.Nullable()),
	INTEGER_NULLABLE(Integer.class, true, new IntegerCellColumnExecutor.Nullable()),
	;
	
	private final Class<?> clazz;
	private final boolean nullable;
	private final CellColumnExecutor executor;
	
	private ReportColumnType(Class<?> clazz, boolean nullable, CellColumnExecutor executor) {
		this.clazz = clazz;
		this.nullable = nullable;
		this.executor = executor;
	}
	
	public static ReportColumnType byClass(Class<?> clazz, boolean nullable) {
		for(ReportColumnType type : values()) {
			if(nullable == type.nullable && type.clazz.isAssignableFrom(clazz)) {
				return type;
			}
		}
		return null;
		// FIXME: errore?
	}
	
	public void applyToCell(Map<String, CellStyle> styles, Cell cell, Object value) {
		this.executor.applyOnCellBase(styles, cell, value);
	}
	
	public Object transformValue(Object value, int row, int column) {
		return executor.transformValue(value, row, column);
	}
	
	public void applyStyle(Workbook workbook, Map<String, CellStyle> styles, Cell cell, String styleName) {
		executor.applyStyle(workbook, styles, cell, styleName);
	}
	
	/**
	 * Marker class for Formula usage
	 * @author Alessandro Marchino
	 * @version 1.0.0 - 12/07/2021
	 *
	 */
	public static class Formula {
		// Marker
	}
}
