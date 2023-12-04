/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.pianoammortamento;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.utility.StringUtilities;

public enum PianoAmmortamentoMutuoExcelReportColumn implements ExcelReportColumn {


	NUMERO_RATA("Numero Rata", "numeroRata", true, Integer.class),
	ANNO("Anno", "anno", false, Integer.class),
	NUMERO_RATA_ANNO("Numero Rata Anno", "numeroRataAnno", false, Integer.class),
	DATA_SCADENZA("Data Scadenza", "dataScadenza", false, Date.class),
	IMPORTO_RATA("Importo Rata", "importoRata", false, BigDecimal.class),
	QUOTA_CAPITALE("Quota Capitale", "quotaCapitale", false, BigDecimal.class),
	QUOTA_INTERESSI("Quota Interessi", "quotaInteressi", false, BigDecimal.class),
	QUOTA_ONERI("Quota Oneri", "quotaOneri", false, BigDecimal.class),
	DEBITO_INIZIALE("Debito Iniziale", "debitoIniziale", false, BigDecimal.class),
	DEBITO_RESIDUO("Debito Residuo", "debitoResiduo", false, BigDecimal.class),
	;
	
	private final String columnName;
	private final String fieldName;
	private final boolean nullable;
	private final Class<?> columnType;
	private final String columnStyle;
	
	/**
	 * Costruttore
	 * @param columnName il nome della colonna
	 * @param fieldName il nome del campo
	 * @param nullable se il campo sia nullable
	 * @param columnType il tipo di campo
	 */
	private PianoAmmortamentoMutuoExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType) {
		this(columnName, fieldName, nullable, columnType, null);
	}
	
	/**
	 * Costruttore
	 * @param columnName il nome della colonna
	 * @param fieldName il nome del campo
	 * @param nullable se il campo sia nullable
	 * @param columnType il tipo di campo
	 * @param columnStyle lo stile di campo
	 */
	private PianoAmmortamentoMutuoExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType, String columnStyle) {
		this.columnName = columnName;
		this.fieldName = fieldName;
		this.nullable = nullable;
		this.columnType = columnType;
		this.columnStyle = columnStyle;
	}
	
	@Override
	public String getColumnName(Object... params) {
		return StringUtilities.formatStringWithDefaultReplacements(this.columnName, "", params);
	}
	
	@Override
	public Class<?> getColumnType() {
		return this.columnType;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}
	
	@Override
	public String getColumnStyle() {
		return this.columnStyle;
	}

	public boolean isApplicable() {
		return true;
	}
	
	@Override
	public boolean isNullable() {
		return nullable;
	}
}

