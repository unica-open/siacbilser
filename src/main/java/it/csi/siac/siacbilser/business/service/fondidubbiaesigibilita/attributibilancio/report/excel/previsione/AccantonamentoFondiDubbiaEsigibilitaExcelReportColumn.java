/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.previsione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.utility.StringUtilities;

/**
 * Campi delle colonne corrispondenti al report accantonamento FCDE previsione
 * @author interlogic
 * @version 1.0.0 - 05/05/2021
 *
 */
public enum AccantonamentoFondiDubbiaEsigibilitaExcelReportColumn implements ExcelReportColumn {

	VERSIONE("Versione", "versione", false, Integer.class),
	CAPITOLO("Capitolo", "capitolo", false, String.class),
	ARTICOLO("Articolo", "articolo", false, String.class),
	UEB("UEB", "ueb", false, String.class) {
		@Override
		public boolean isApplicable() {
			// TODO: passare un parametro? Verificare se la UEB sia visibile
			return false;
		}
	},
	TITOLO("Titolo", "titolo", false, String.class),
	TIPOLOGIA("Tipologia", "tipologia", false, String.class),
	CATEGORIA("Categoria", "categoria", false, String.class),
	SAC("Struttura Amministrativo Contabile", "strutturaAmministrativoContabile", false, String.class),
	INCASSI_4("Incassi {0,number,#}", "incassi4", true, BigDecimal.class),
	ACCERTAMENTI_4("Accertamenti {0,number,#}", "accertamenti4", true, BigDecimal.class),
	INCASSI_3("Incassi {1,number,#}", "incassi3", true, BigDecimal.class),
	ACCERTAMENTI_3("Accertamenti {1,number,#}", "accertamenti3", true, BigDecimal.class),
	INCASSI_2("Incassi {2,number,#}", "incassi2", true, BigDecimal.class),
	ACCERTAMENTI_2("Accertamenti {2,number,#}", "accertamenti2", true, BigDecimal.class),
	INCASSI_1("Incassi {3,number,#}", "incassi1", true, BigDecimal.class),
	ACCERTAMENTI_1("Accertamenti {3,number,#}", "accertamenti1", true, BigDecimal.class),
	INCASSI_0("Incassi {4,number,#}", "incassi0", true, BigDecimal.class),
	ACCERTAMENTI_0("Accertamenti {4,number,#}", "accertamenti0", true, BigDecimal.class),
	//SIAC-8513 cell_decimal_five => cell_decimal
	MEDIA_SEMPLICE_TOTALI("% Media semplice dei totali", "mediaSempliceTotali", true, BigDecimal.class, "cell_decimal"),
	MEDIA_SEMPLICE_RAPPORTI("% Media semplice dei rapporti", "mediaSempliceRapporti", true, BigDecimal.class, "cell_decimal"),
	MEDIA_PONDERATA_TOTALI("% Media ponderata dei totali", "mediaPonderataTotali", true, BigDecimal.class, "cell_decimal"),
	MEDIA_PONDERATA_RAPPORTI("% Media ponderata dei rapporti", "mediaPonderataRapporti", true, BigDecimal.class, "cell_decimal"),
	MEDIA_UTENTE("% Media utente", "mediaUtente", true, BigDecimal.class, "cell_decimal"),
	PERCENTUALE_MINIMA("% Minima accantonamento a fondo", "percentualeMinima", true, BigDecimal.class, "cell_decimal"),
	PERCENTUALE_Effettiva("% Effettiva accantonamento a fondo", "percentualeEffettiva", true, BigDecimal.class, "cell_decimal"),
	//
	STANZIAMENTO_0("Stanziamento {5,number,#}", "stanziamento0", true, BigDecimal.class),
	STANZIAMENTO_1("Stanziamento {6,number,#}", "stanziamento1", true, BigDecimal.class),
	STANZIAMENTO_2("Stanziamento {7,number,#}", "stanziamento2", true, BigDecimal.class),
	ACCANTONAMENTO_FCDE_0("Accantonamento FCDE {5,number,#}", "accantonamentoFCDE0", true, BigDecimal.class),
	ACCANTONAMENTO_FCDE_1("Accantonamento FCDE {6,number,#}", "accantonamentoFCDE1", true, BigDecimal.class),
	ACCANTONAMENTO_FCDE_2("Accantonamento FCDE {7,number,#}", "accantonamentoFCDE2", true, BigDecimal.class),
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
	private AccantonamentoFondiDubbiaEsigibilitaExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType) {
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
	private AccantonamentoFondiDubbiaEsigibilitaExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType, String columnStyle) {
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
	public static List<AccantonamentoFondiDubbiaEsigibilitaExcelReportColumn> getColumns(){
		final List<AccantonamentoFondiDubbiaEsigibilitaExcelReportColumn> colonne = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaExcelReportColumn>();
		for (AccantonamentoFondiDubbiaEsigibilitaExcelReportColumn colonna : AccantonamentoFondiDubbiaEsigibilitaExcelReportColumn.values()) {
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

