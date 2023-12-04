/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDDocStatoEnum.
 */
@EnumEntity(entityName="SiacDAccFondiDubbiaEsigStato", idPropertyName="afdeStatoId", codePropertyName="afdeStatoCode")
public enum SiacDAccFondiDubbiaEsigStatoEnum {
		
	/** The bozza. */
	BOZZA("BOZZA", StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA),
	
	/** The definitiva. */
	DEFINITIVA("DEFINITIVA", StatoAccantonamentoFondiDubbiaEsigibilita.DEFINITIVA), 
	;
	
	/** The codice. */
	private final String codice;
	
	/** The tipo accantonamento fondi dubbia esigibilita. */
	private final StatoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita;
	
	/**
	 * Instantiates a new siac d doc stato enum.
	 *
	 * @param codice the codice
	 * @param tipoAccantonamentoFondiDubbiaEsigibilita the stato operativo
	 */
	private SiacDAccFondiDubbiaEsigStatoEnum(String codice, StatoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita){
		this.codice = codice;
		this.tipoAccantonamentoFondiDubbiaEsigibilita = tipoAccantonamentoFondiDubbiaEsigibilita;
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
	 * Gets the stato accantonamento fondi dubbia esigibilita.
	 *
	 * @return the tipoAccantonamentoFondiDubbiaEsigibilita
	 */
	public StatoAccantonamentoFondiDubbiaEsigibilita getStatoAccantonamentoFondiDubbiaEsigibilita() {
		return this.tipoAccantonamentoFondiDubbiaEsigibilita;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDAccFondiDubbiaEsigStatoEnum byCodice(String codice){
		for(SiacDAccFondiDubbiaEsigStatoEnum e : SiacDAccFondiDubbiaEsigStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDAccFondiDubbiaEsigStatoEnum");
	}
	
	

	/**
	 * By stato operativo.
	 *
	 * @param tipoAccantonamentoFondiDubbiaEsigibilita the tipo accantonamento fondi dubbia esigibilita
	 * @return the siac d doc stato enum
	 */
	public static SiacDAccFondiDubbiaEsigStatoEnum byStatoAccantonamentoFondiDubbiaEsigibilita(StatoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita){
		for(SiacDAccFondiDubbiaEsigStatoEnum e : SiacDAccFondiDubbiaEsigStatoEnum.values()){
			if(e.getStatoAccantonamentoFondiDubbiaEsigibilita().equals(tipoAccantonamentoFondiDubbiaEsigibilita)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo  "+ tipoAccantonamentoFondiDubbiaEsigibilita + " non ha un mapping corrispondente in SiacDAccFondiDubbiaEsigStatoEnum");
	}
	
	/**
	 * By stato accantonamento fondi dubbia esigibilita even null.
	 *
	 * @param tipoAccantonamentoFondiDubbiaEsigibilita the tipo accantonamento fondi dubbia esigibilita
	 * @return the siac D acc fondi dubbia esig stato enum
	 */
	public static SiacDAccFondiDubbiaEsigStatoEnum byStatoAccantonamentoFondiDubbiaEsigibilitaEvenNull(StatoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita){
		if(tipoAccantonamentoFondiDubbiaEsigibilita == null){
			return null;
		}
		return byStatoAccantonamentoFondiDubbiaEsigibilita(tipoAccantonamentoFondiDubbiaEsigibilita);
	}

}
