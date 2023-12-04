/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.rendiconto.creditistralciati;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.utility.StringUtilities;

public enum AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn implements ExcelReportColumn {
	
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
	SAC("Struttura", "strutturaAmministrativoContabile", false, String.class),
	ANNO_ACCERTAMENTO("Anno accertamento", "annoAccertamentoString", false, String.class),
	NUMERO_ACCERTAMENTO("Numero accertamento", "numeroAccertamentoString", false, String.class),
	DESCRIZIONE_ACCERTAMENTO("Descrizione accertamento", "descrizioneAccertamento", false, String.class),
	TITOLO("Titolo", "titoloAccertamento", false, String.class),
	TIPOLOGIA("Tipologia", "tipologiaAccertamento", false, String.class),
	CATEGORIA("Categoria", "categoriaAccertamento", false, String.class),
	PDC("Pdc finanziario", "pianoDeiContiAccertamento", false, String.class),
	SAC_ACC("Struttura", "strutturaAmministrativoContabileAccertamento", false, String.class),
	
	RICORRENTE("Ricorrente", "codRicorrente", false, String.class),
	//SIAC-8822
	IMPORTO_ATTUALE("Importo attuale", "importoAttuale", true, BigDecimal.class),

	ANNO_PROVVEDIMENTO_ACCERTAMENTO("Anno atto accertamento", "annoProvvedimentoAccertamento", false, String.class),
	NUMERO_PROVVEDIMENTO_ACCERTAMENTO("Numero atto accertamento", "numeroProvvedimentoAccertamentoString", false, String.class),
	TIPO_PROVVEDIMENTO_ACCERTAMENTO("Tipo atto accertamento", "tipoProvvedimentoAccertamento", false, String.class),
	SAC_PROVVEDIMENTO_ACCERTAMENTO("Struttura atto accertamento", "sacProvvedimentoAccertamento", false, String.class),
	
	RAGIONE_SOCIALE("Ragione sociale", "soggettoDesc", false, String.class),
	CLASSE_SOGGETTO("Descrizione classe soggetto", "classeSoggettoDesc", false, String.class),
	
	
	
//	RILEVANTE_GSA("Rilevante GSA", "rilevanteGSA", true, Boolean.class),

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
	private AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType) {
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
	private AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn(String columnName, String fieldName, boolean nullable, Class<?> columnType, String columnStyle) {
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
		for (AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn colonna : AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn.values()) {
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
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public Class<?> getColumnType() {
		return this.columnType;
	}

	@Override
	public String getColumnStyle() {
		return columnStyle;
	}

	@Override
	public boolean isNullable() {
		return nullable;
	}
	
	public boolean isApplicable() {
		return true;
	}

}
