/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.gestione;

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
public enum AccantonamentoFondiDubbiaEsigibilitaGestioneExcelReportColumn implements ExcelReportColumn {
	
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
	INCASSO_CONTO_COMPETENZA("Incassi c/competenza {1,number,#}", "incassoContoCompetenza", true, BigDecimal.class),
	ACCERTATO_CONTO_COMPETENZA("Accertatamenti {1,number,#}", "accertatoContoCompetenza", true, BigDecimal.class),
	//SAIC-8513 cell_decimal_five => cell_decimal, SIAC-8774
	PERCENTUALE_INCASSO_GESTIONE("% incassi / accertamenti", "percentualeIncassoGestione", true, BigDecimal.class, "cell_decimal"),
	//SIAC-8774
	PERCENTUALE_ACCANTONAMENTO("% incassi / stanziamento", "percentualeAccantonamento", true, BigDecimal.class, "cell_decimal"),
	TIPO_ACCANTONAMENTO_PRECEDENTE("Tipo accantonamento precedente", "tipoAccantonamentoPrecedente", true, String.class, "cell_decimal"),
	//SIAC-8774
	PERCENTUALE_ACCANTONAMENTO_PRECEDENTE("% accantonamento precedente di confronto", "percentualeAccantonamentoPrecedente", true, BigDecimal.class, "cell_decimal"),
	PERCENTUALE_MINIMA_ACCANTONAMENTO("% minima accantonamento a fondo", "percentualeMinimaAccantonamento", true, BigDecimal.class, "cell_decimal"),
	PERCENTUALE_EFFETTIVA_ACCANTONAMENTO("% effettiva accantonamento a fondo", "percentualeEffettivaAccantonamento", true, BigDecimal.class, "cell_decimal"),
	
	//SIAC-8768
	STANZIAMENTO_SENZA_VAR_0("Stanziamento definitivo {1,number,#}", "stanziatoSenzaVariazioni", true, BigDecimal.class),
	DELTA_VAR_0("Variazione stanziamento {1,number,#}", "deltaVariazioni", true, BigDecimal.class),
	STANZIATO_0("Somma stanziamento {1,number,#} e variazioni", "stanziato", true, BigDecimal.class),
	
	STANZIAMENTO_SENZA_VAR_1("Stanziamento definitivo {2,number,#}", "stanziatoSenzaVariazioni1", true, BigDecimal.class),
	DELTA_VAR_1("Variazione stanziamento {2,number,#}", "deltaVariazioni1", true, BigDecimal.class),
	STANZIATO_1("Somma stanziamento {2,number,#} e variazioni", "stanziato1", true, BigDecimal.class),
	
	STANZIAMENTO_SENZA_VAR_2("Stanziamento definitivo {3,number,#}", "stanziatoSenzaVariazioni2", true, BigDecimal.class),
	DELTA_VAR_2("Variazione stanziamento {3,number,#}", "deltaVariazioni2", true, BigDecimal.class),
	STANZIATO_2("Somma stanziamento {3,number,#} e variazioni", "stanziato2", true, BigDecimal.class),
	
	// SIAC-8795
	ACCANTONAMENTO_FCDE_0("Accantonamento FCDE {1,number,#}", "accantonamentoFcde0", true, BigDecimal.class),
	ACCANTONAMENTO_FCDE_1("Accantonamento FCDE {2,number,#}", "accantonamentoFcde1", true, BigDecimal.class),
	ACCANTONAMENTO_FCDE_2("Accantonamento FCDE {3,number,#}", "accantonamentoFcde2", true, BigDecimal.class),
	
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
	 * @param nullable se il campo sia nullable
	 * @param columnType il tipo di campo
	 */
	private AccantonamentoFondiDubbiaEsigibilitaGestioneExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType) {
		this(columnName, fieldName, nullable, columnType, null);
	}
	
	/**
	 * Costruttore
	 * @param columnName il nome della colonna
	 * @param fieldName il nome del campo
	 * @param columnType il tipo di campo
	 * @param nullable se il campo sia nullable
	 * @param columnStyle lo stile della colonna
	 */
	private AccantonamentoFondiDubbiaEsigibilitaGestioneExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType, String columnStyle) {
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
		for (AccantonamentoFondiDubbiaEsigibilitaGestioneExcelReportColumn colonna : AccantonamentoFondiDubbiaEsigibilitaGestioneExcelReportColumn.values()) {
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

