/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.fcde.TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita;

/**
 * The Enum SiacDDocStatoEnum.
 */
@EnumEntity(entityName="SiacDAccFondiDubbiaEsigTipoMediaConfronto", idPropertyName="afdeTipoMediaConfId", codePropertyName="afdeTipoMediaConfCode")
public enum SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum {
		
	PREVISIONE("PREVISIONE", TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE),
	GESTIONE("GESTIONE", TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita.GESTIONE), 
	;
	
	/** The codice. */
	private final String codice;
	
	/** The tipo accantonamento fondi dubbia esigibilita. */
	private final TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita;
	
	/**
	 * Instantiates a new siac d doc stato enum.
	 *
	 * @param codice the codice
	 * @param tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita the tipo media confronto
	 */
	private SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum(String codice, TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita){
		this.codice = codice;
		this.tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita = tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita;
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
	 * Gets the tipo media confronto accantonamento fondi dubbia esigibilita.
	 *
	 * @return the tipoAccantonamentoFondiDubbiaEsigibilita
	 */
	public TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita getTipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita() {
		return this.tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum byCodice(String codice){
		for(SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum e : SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum");
	}
	
	

	/**
	 * By tipo media confronto.
	 *
	 * @param tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita the tipo media confronto accantonamento fondi dubbia esigibilita
	 * @return the siac d doc stato enum
	 */
	public static SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum byTipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita(TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita){
		for(SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum e : SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum.values()){
			if(e.getTipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita().equals(tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo  "+ tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita + " non ha un mapping corrispondente in SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum");
	}
	
	/**
	 * By tipo media confronto accantonamento fondi dubbia esigibilita even null.
	 *
	 * @param tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita the tipo media accantonamento fondi dubbia esigibilita
	 * @return the siac D acc fondi dubbia esig tipo enum
	 */
	public static SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum byTipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilitaEvenNull(TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita){
		if(tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita == null){
			return null;
		}
		return byTipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita(tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita);
	}
	

}
