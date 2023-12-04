/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.utility.StringUtilities;

/**
 * Campi delle colonne corrispondenti al report accantonamento FCDE previsione
 * @author interlogic
 * @version 1.0.0 - 05/05/2021
 *
 */
public enum IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn implements ExcelReportColumn {
	
	VERSIONE("Versione", "versione", true, Integer.class),
	FASE("Fase", "faseAttributiBilancio", false, String.class),
	STATO("Stato", "statoAttributiBilancio", false, String.class),
//	UTENTE("Utente", "utente", false, String.class), // ???
	DATA_ORA("Data elaborazione", "dataOraElaborazione", false, Date.class),
	ANNI_ESERCIZIO("Anni esercizio", "anniEsercizio", false, String.class),
	RISCOSSIONE_VIRTUOSA("Riscossione virtuosa", "riscossioneVirtuosa", false, String.class),
	QUINQUENNIO_RIFERIMENTO("Quinquennio riferimento", "quinquennioRiferimento", false, String.class),
	ACCANTONAMENTO_GRADUALE_ENTI_LOCALI("Accantonamento graduale enti locali", "accantonamentoGradualeEntiLocali", false, BigDecimal.class),
	
	;
	
	private final String columnName;
	private final String fieldName;
	private final Class<?> columnType;
	private final boolean nullable;
	private final String columnStyle;
	
	/**
	 * Costruttore
	 * @param columnName il nome della colonna
	 * @param fieldName il nome del campo
	 * @param columnType il tipo di campo
	 */
	private IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType) {
		this(columnName, fieldName, columnType, nullable, null);
	}
	
	/**
	 * Costruttore
	 * @param columnName il nome della colonna
	 * @param fieldName il nome del campo
	 * @param columnType il tipo di campo
	 * @param nullable se il campo &eacute; nullable
	 * @param columnStyle lo stile della colonna
	 */
	private IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn(String columnName, String fieldName, Class<?> columnType, boolean nullable, String columnStyle) {
		this.columnName = columnName;
		this.fieldName = fieldName;
		this.columnType = columnType;
		this.nullable = nullable;
		this.columnStyle = columnStyle;
	}

	/**
	 * Gets the colonne.
	 *
	 * @return the colonne
	 */
	public static List<IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn> getColonne(){
		final List<IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn> colonne = new ArrayList<IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn>();
		for (IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn colonna : values()) {
			if(colonna.isApplicable()) {
				colonne.add(colonna);
			}
		}
		return colonne;
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

