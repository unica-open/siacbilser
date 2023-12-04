/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.rendiconto;

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
public enum AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelReportColumn implements ExcelReportColumn {
	
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
	INCASSI_CONTO_RESIDUI_4("Incassi conto residui {0,number,#}", "incassiContoResidui4", true, BigDecimal.class),
	RESIDUI_4("Residui {0,number,#}", "residui4", true, BigDecimal.class),
	INCASSI_CONTO_RESIDUI_3("Incassi conto residui {1,number,#}", "incassiContoResidui3", true, BigDecimal.class),
	RESIDUI_3("Residui {1,number,#}", "residui3", true, BigDecimal.class),
	INCASSI_CONTO_RESIDUI_2("Incassi conto residui {2,number,#}", "incassiContoResidui2", true, BigDecimal.class),
	RESIDUI_2("Residui {2,number,#}", "residui2", true, BigDecimal.class),
	INCASSI_CONTO_RESIDUI_1("Incassi conto residui {3,number,#}", "incassiContoResidui1", true, BigDecimal.class),
	RESIDUI_1("Residui {3,number,#}", "residui1", true, BigDecimal.class),
	INCASSI_CONTO_RESIDUI_0("Incassi conto residui {4,number,#}", "incassiContoResidui0", true, BigDecimal.class),
	RESIDUI_0("Residui {4,number,#}", "residui0", true, BigDecimal.class),
	//SIAC-8513 cell_decimal_five => cell_decimal
	MEDIA_SEMPLICE_TOTALI("% Media semplice dei totali", "mediaSempliceTotali", true, BigDecimal.class, "cell_decimal"),
	MEDIA_SEMPLICE_RAPPORTI("% Media semplice dei rapporti", "mediaSempliceRapporti", true, BigDecimal.class, "cell_decimal"),
	MEDIA_PONDERATA_TOTALI("% Media ponderata dei totali", "mediaPonderataTotali", true, BigDecimal.class, "cell_decimal"),
	MEDIA_PONDERATA_RAPPORTI("% Media ponderata dei rapporti", "mediaPonderataRapporti", true, BigDecimal.class, "cell_decimal"),
	MEDIA_UTENTE("% Media utente", "mediaUtente", true, BigDecimal.class, "cell_decimal"),
	PERCENTUALE_MINIMA("% Minima accantonamento a fondo", "percentualeMinima", true, BigDecimal.class, "cell_decimal"),
	PERCENTUALE_Effettiva("% Effettiva accantonamento a fondo", "percentualeEffettiva", true, BigDecimal.class, "cell_decimal"),
	//
	RESIDUI_FINALI("Residui finali {5,number,#}", "residuiFinali", true, BigDecimal.class),
//	RESIDUI_FINALI1("Residui finali {6,number,#}", "residuiFinali1", true, BigDecimal.class),
//	RESIDUI_FINALI2("Residui finali {7,number,#}", "residuiFinali2", true, BigDecimal.class),
	ACCANTONAMENTO_FCDE("Accantonamento FCDE {5,number,#}", "accantonamentoFCDE", true, BigDecimal.class),
//	ACCANTONAMENTO_FCDE1("Accantonamento FCDE {6,number,#}", "accantonamentoFCDE1", true, BigDecimal.class),
//	ACCANTONAMENTO_FCDE2("Accantonamento FCDE {7,number,#}", "accantonamentoFCDE2", true, BigDecimal.class),
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
	private AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType) {
		this(columnName, fieldName, nullable, columnType, null);
	}
	
	/**
	 * Costruttore
	 * @param columnName il nome della colonna
	 * @param fieldName il nome del campo
	 * @param nullable se il campo sia nullable
	 * @param columnType il tipo di campo
	 * @param columnStyle lo stile della colonna
	 */
	private AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType, String columnStyle) {
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
	public static List<ExcelReportColumn> getColonne(){
		final List<ExcelReportColumn> colonne = new ArrayList<ExcelReportColumn>();
		for (AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelReportColumn colonna : AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelReportColumn.values()) {
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
		return columnStyle;
	}
	public boolean isApplicable() {
		return true;
	}
	@Override
	public boolean isNullable() {
		return nullable;
	}
}

