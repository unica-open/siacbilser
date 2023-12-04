/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.movimentogestione;

import java.math.BigDecimal;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.utility.StringUtilities;

public enum AccertamentiAssociatiMutuoExcelReportColumn implements ExcelReportColumn {


	ANNO("Anno", "anno", false, Integer.class),
	NUMERO_("Numero", "numero", false, Integer.class),
	STATO("Stato", "statoOperativo", false, String.class),
	TITOLO("Titolo", "codTitolo", false, String.class),
	CAPITOLO("Capitolo", "capitolo", false, String.class),
	TIPO_FINANZIAMENTO("Tipo Finanziamento", "descTipoFinanziamento", false, String.class),
	PROVVEDIEMNTO("Provvedimento", "attoAmministrativo", false, String.class),
	SOGGETTO("Soggetto", "soggetto", false, String.class),
	SUB("Sub", "elencoSubAccertamenti", false, String.class),
	ALTRI_MUTUI("Altri Mutui", "altriMutui", false, String.class),
	IMPORTO_ATTUALE("Importo Attuale", "importoAttuale", false, BigDecimal.class),
	IMPORTO_FINALE("Importo Incassato", "importoIncassato", false, BigDecimal.class),
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
	private AccertamentiAssociatiMutuoExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType) {
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
	private AccertamentiAssociatiMutuoExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType, String columnStyle) {
		this.columnName = columnName;
		this.fieldName = fieldName;
		this.nullable = nullable;
		this.columnType = columnType;
		this.columnStyle = columnStyle;
	}
	
	/**
	 * Gets the colonne.
	 *
	 * @return the colonne
	 */

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

	@Override
	public boolean isNullable() {
		return nullable;
	}
}

