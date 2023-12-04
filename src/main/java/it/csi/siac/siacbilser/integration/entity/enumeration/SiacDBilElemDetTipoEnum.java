/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;



/**
 * select replace(initcap(elem_det_tipo_desc),' ','') || '("' || elem_det_tipo_code || '"),' as d from siac_d_bil_elem_det_tipo group by d.
 */
@EnumEntity(entityName="SiacDBilElemDetTipo", idPropertyName="elemDetTipoId", codePropertyName="elemDetTipoCode")
public enum SiacDBilElemDetTipoEnum {
	
	Stanziamento("STA", "stanziamento"),
	StanziamentoCassa("SCA", "stanziamentoCassa"),
	StanziamentoResiduo("STR", "stanziamentoResiduo"),
	FondoPluriennaleVincolato("FPV", "fondoPluriennaleVinc"),
	StanziamentoCassaIniziale("SCI", "stanziamentoCassaIniziale"),
	StanziamentoIniziale("STI", "stanziamentoIniziale"),
	StanziamentoProposto("STP", "stanziamentoProposto"),
	StanziamentoResiduoIniziale("SRI", "stanziamentoResiduoIniziale"),
	StanziamentoAssest("STASS", "stanziamentoAsset"),
	StanziamentoCassaAssest("STCASS", "stanziamentoCassaAsset"),
	StanziamentoResiduoAssest("STRASS", "stanziamentoResAsset"),
	MassimoImpegnabile("MI", "massimoImpegnabile"),
	;
	
		
	private final String codice;
	private final String importiCapitoloFieldName;

	/**
	 * Instantiates a new siac d bil elem det tipo enum.
	 *
	 * @param codice the codice
	 * @param importiCapitoloFieldName - nome del Field in nella classe di Model <? extends ImportiCapitolo>
	 */
	SiacDBilElemDetTipoEnum(String codice, String importiCapitoloFieldName){
		//this.id = id;
		this.codice = codice;
		this.importiCapitoloFieldName = importiCapitoloFieldName;
	}
	
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d bil elem det tipo enum
	 */
	public static SiacDBilElemDetTipoEnum byCodice(String codice){
		for(SiacDBilElemDetTipoEnum e : SiacDBilElemDetTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("importo capitolo con codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemDetTipoEnum");
	}

	/**
	 * By importi capitolo model field name.
	 *
	 * @param modelFieldName the model field name
	 * @return the siac d bil elem det tipo enum
	 */
	public static SiacDBilElemDetTipoEnum byImportiCapitoloModelFieldName(String modelFieldName){
		for(SiacDBilElemDetTipoEnum e : SiacDBilElemDetTipoEnum.values()){
			if(e.getImportiCapitoloFieldName().equals(modelFieldName)){
				return e;
			}
		}
		throw new IllegalArgumentException("importo capitolo di tipo "+ modelFieldName + " non ha un mapping corrispondente in SiacDBilElemDetTipoEnum");
	}
	
	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}
		
	/**
	 * Gets the importi capitolo field name.
	 *
	 * @return the importi capitolo field name
	 */
	public String getImportiCapitoloFieldName() {
		return importiCapitoloFieldName;
	}
	
}