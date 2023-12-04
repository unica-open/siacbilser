/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio.report.excel;

import java.math.BigDecimal;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.utility.StringUtilities;

/**
 * Campi delle colonne corrispondenti al report della variazione importi
 * @author Elisa Chiari
 *
 */
public enum VariazioneImportoCapitoloExcelReportColumn implements ExcelReportColumn{
	
	
	
	NUM_VARIAZIONE(            "Numero Variazione",                     "variazioneNum",                            Integer.class),
	// SIAC-6883
//	ANNO_VARIAZIONE(           "Anno Competenza",                       "variazioneAnno",                           String.class),
	
	STATO_VARIAZIONE(           "Stato",                                "statoVariazione",                         String.class),
	ANNO_CAPITOLO(              "Anno di Bilancio",                     "annoCapitolo",                            String.class),
	CAPITOLO(                   "Capitolo",                             "numeroCapitolo",                          String.class),
	ARTICOLO(                   "Articolo",                             "numeroArticolo",                          String.class),
	TIPO_CAPITOLO(              "Tipo",                                 "tipoCapitolo",                            String.class),
	DESCRIZIONE_CAPITOLO(       "Descrizione capitolo",                 "descrizioneCapitolo",                     String.class),
	MISSIONE(                   "Missione",                             "missioneCapitolo",                        String.class),
	PROGRAMMA(                  "Programma",                            "programmaCapitolo",                       String.class),
	TITOLO_SPESA(               "Titolo",                               "titoloCapitoloSpesa",                     String.class),
	MACROAGGREGATO(             "Macroaggregato",                       "macroaggregatoCapitolo",                  String.class),
	TITOLO_ENTRATA(             "Titolo",                               "titoloCapitoloEntrata",                   String.class),
	TIPOLOGIA(                  "Tipologia",                            "tipologiaCapitoloEntrata",                String.class),
	CATEGORIA(                  "Categoria",                            "categoriaTipologiaTitoloCapitoloEntrata", String.class),
	//SIAC-6468
	TIPO_FINANZIAMENTO(         "Tipo Finanziamento",                   "tipologiaFinanziamento",                  String.class),
	SAC(                        "Strutt. Amm. Responsabile",            "strutturaAmministrativaResponsabile",     String.class),
	
	COMPETENZA_CAPITOLO_ANNO0(  "Competenza ({0,number,#})",            "stanziamentoCapitolo",                    BigDecimal.class),
	RESIDUO_CAPITOLO_ANNO0(     "Residuo ({0,number,#})",               "stanziamentoResiduoCapitolo",             BigDecimal.class),
	CASSA_CAPITOLO_ANNO0(       "Cassa ({0,number,#})",                 "stanziamentoCassaCapitolo",               BigDecimal.class),
	COMPETENZA_CAPITOLO_ANNO1(  "Competenza ({1,number,#})",            "stanziamentoCapitoloAnno1",               BigDecimal.class),
	COMPETENZA_CAPITOLO_ANNO2(  "Competenza ({2,number,#})",            "stanziamentoCapitoloAnno2",               BigDecimal.class),
	COMPETENZA_VARIAZIONE_ANNO0("Variazione competenza ({0,number,#})", "stanziamentoVariazione",                  BigDecimal.class),
	RESIDUO_VARIAZIONE_ANNO0(   "Variazione residuo ({0,number,#})",    "stanziamentoResiduoVariazione",           BigDecimal.class),
	CASSA_VARIAZIONE_ANNO0(     "Variazione cassa ({0,number,#})",      "stanziamentoCassaVariazione",             BigDecimal.class),
	COMPETENZA_VARIAZIONE_ANNO1("Variazione competenza ({1,number,#})", "stanziamentoVariazioneAnno1",             BigDecimal.class),
	COMPETENZA_VARIAZIONE_ANNO2("Variazione competenza ({2,number,#})", "stanziamentoVariazioneAnno2",             BigDecimal.class),
	
	;
	
	private final String columnName;
	private final String fieldName;
	private final Class<?> columnType;
	private final String columnStyle;
	
	/**
	 * Costruttore
	 * @param columnName il nome della colonna
	 * @param fieldName il nome del campo
	 * @param columnType il tipo di campo
	 */
	private VariazioneImportoCapitoloExcelReportColumn(String columnName, String fieldName, Class<?> columnType) {
		this(columnName, fieldName, columnType, null);
	}
	
	/**
	 * Costruttore
	 * @param columnName il nome della colonna
	 * @param fieldName il nome del campo
	 * @param columnType il tipo di campo
	 */
	private VariazioneImportoCapitoloExcelReportColumn(String columnName, String fieldName, Class<?> columnType, String columnStyle) {
		this.columnName = columnName;
		this.fieldName = fieldName;
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
	
	@Override
	public boolean isNullable() {
		return false;
	}
}

