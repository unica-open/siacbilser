/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;



/**
 * 
select replace(initcap(attoamm_tipo_desc),' ','') || '("' || attoamm_tipo_code || '", "' ||  initcap(attoamm_tipo_desc) || '"),' 
from siac_d_atto_amm_tipo where ente_proprietario_id = 1
 */
@EnumEntity(entityName="SiacDAttoAmmTipo", idPropertyName="attoammTipoId", codePropertyName="attoammTipoCode")
public enum SiacDAttoAmmTipoEnum {
	
	@Deprecated
	//Non piu' presente su DB
	Delibera("01", "Delibera"),
	
	Determina("02", "Determina"),
	MovimentoInterno("MIN", "Movimento Interno"),
	DeliberaDiGiunta("DG", "Delibera Di Giunta"),
	DeliberaDiGiuntaDUrgenza("DGU", "Delibera Di Giunta D'Urgenza"),
	DeliberaDiConsiglio("DC", "Delibera Di Consiglio"),
	DeliberaDiCircoscrizionePropria("DCIP", "Delibera Di Circoscrizione Propria"),
	DeliberaDiCircoscrizioneDelegata("DCID", "Delibera Di Circoscrizione Delegata"),
	Parere("DPCI", "Parere"),
	DeterminaDiImpegno("DTI", "Determina Di Impegno"),
	DeterminaDiLiquidazione("DTL", "Determina Di Liquidazione"),
	Mozione("MO", "Mozione"),
	Interpellanza("INT", "Interpellanza"),
	DeliberaGenerica("FF", "Delibera Generica"),
	DeliberaDiNomina("NO", "Delibera Di Nomina"),
	DeterminaDiAccertamento("DTA", "Determina Di Accertamento"),
	DeterminaDiIncasso("DTS", "Determina Di Incasso"),
	DeterminaBianca("DTB", "Determina Bianca"),
	Alg("ALG", "ALG");

	
	
	

	/** The codice. */
	private final String codice;
	
	/** The descrizione. */
	private final String descrizione;

	/**
	 * Instantiates a new siac d atto amm tipo enum.
	 *
	 * @param codice the codice
	 * @param importiCapitoloFieldName - nome del Field in nella classe di Model <? extends ImportiCapitolo>
	 */
	SiacDAttoAmmTipoEnum(String codice, String importiCapitoloFieldName){
		this.codice = codice;
		this.descrizione = importiCapitoloFieldName;
	}
	
//	public SiacDBilElemDetTipo getEntity(){
//		SiacDBilElemDetTipo result = new SiacDBilElemDetTipo();
//		result.setElemDetTipoId(getId());
//		result.setElemDetTipoCode(getCodice());
//		return result;
//	}
	
	/**
 * By codice.
 *
 * @param codice the codice
 * @return the siac d atto amm tipo enum
 */
public static SiacDAttoAmmTipoEnum byCodice(String codice){
		for(SiacDAttoAmmTipoEnum e : SiacDAttoAmmTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("importo capitolo con codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemDetTipoEnum");
	}

	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}
		

//	public Integer getId() {
//		return id;
//	}
//	
//	public void setId(Integer id) {
//		this.id = id;
//	}
	
	

	/**
 * Gets the descrizione.
 *
 * @return the descrizione
 */
public String getDescrizione() {
		return descrizione;
	}
	
}