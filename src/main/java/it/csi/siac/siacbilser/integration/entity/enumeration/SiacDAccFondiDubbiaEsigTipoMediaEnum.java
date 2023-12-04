/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.fcde.TipoMediaAccantonamentoFondiDubbiaEsigibilita;

/**
 * The Enum SiacDDocStatoEnum.
 */
@EnumEntity(entityName="SiacDAccFondiDubbiaEsigTipoMedia", idPropertyName="afdeTipoMediaId", codePropertyName="afdeTipoMediaCode")
public enum SiacDAccFondiDubbiaEsigTipoMediaEnum {
		
	SEMPLICE_TOTALI("SEMP_TOT", TipoMediaAccantonamentoFondiDubbiaEsigibilita.SEMPLICE_TOTALI),
	SEMPLICE_RAPPORTI("SEMP_RAP", TipoMediaAccantonamentoFondiDubbiaEsigibilita.SEMPLICE_RAPPORTI), 
	PONDERATA_TOTALI("POND_TOT", TipoMediaAccantonamentoFondiDubbiaEsigibilita.PONDERATA_TOTALI),
	PONDERATA_RAPPORTI("POND_RAP", TipoMediaAccantonamentoFondiDubbiaEsigibilita.PONDERATA_RAPPORTI),
	UTENTE("UTENTE", TipoMediaAccantonamentoFondiDubbiaEsigibilita.UTENTE),
	;
	
	/** The codice. */
	private final String codice;
	
	/** The tipo accantonamento fondi dubbia esigibilita. */
	private final TipoMediaAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita;
	
	/**
	 * Instantiates a new siac d doc stato enum.
	 *
	 * @param codice the codice
	 * @param tipoAccantonamentoFondiDubbiaEsigibilita the stato operativo
	 */
	private SiacDAccFondiDubbiaEsigTipoMediaEnum(String codice, TipoMediaAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita){
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
	 * Gets the tipo accantonamento fondi dubbia esigibilita.
	 *
	 * @return the tipoAccantonamentoFondiDubbiaEsigibilita
	 */
	public TipoMediaAccantonamentoFondiDubbiaEsigibilita getTipoMediaAccantonamentoFondiDubbiaEsigibilita() {
		return this.tipoAccantonamentoFondiDubbiaEsigibilita;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDAccFondiDubbiaEsigTipoMediaEnum byCodice(String codice){
		for(SiacDAccFondiDubbiaEsigTipoMediaEnum e : SiacDAccFondiDubbiaEsigTipoMediaEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDAccFondiDubbiaEsigTipoMediaEnum");
	}
	
	

	/**
	 * By stato operativo.
	 *
	 * @param tipoAccantonamentoFondiDubbiaEsigibilita the tipo accantonamento fondi dubbia esigibilita
	 * @return the siac d doc stato enum
	 */
	public static SiacDAccFondiDubbiaEsigTipoMediaEnum byTipoMediaAccantonamentoFondiDubbiaEsigibilita(TipoMediaAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita){
		for(SiacDAccFondiDubbiaEsigTipoMediaEnum e : SiacDAccFondiDubbiaEsigTipoMediaEnum.values()){
			if(e.getTipoMediaAccantonamentoFondiDubbiaEsigibilita().equals(tipoAccantonamentoFondiDubbiaEsigibilita)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo  "+ tipoAccantonamentoFondiDubbiaEsigibilita + " non ha un mapping corrispondente in SiacDAccFondiDubbiaEsigTipoMediaEnum");
	}
	
	/**
	 * By tipo accantonamento fondi dubbia esigibilita even null.
	 *
	 * @param tipoAccantonamentoFondiDubbiaEsigibilita the tipo accantonamento fondi dubbia esigibilita
	 * @return the siac D acc fondi dubbia esig tipo enum
	 */
	public static SiacDAccFondiDubbiaEsigTipoMediaEnum byTipoMediaAccantonamentoFondiDubbiaEsigibilitaEvenNull(TipoMediaAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita){
		if(tipoAccantonamentoFondiDubbiaEsigibilita == null){
			return null;
		}
		return byTipoMediaAccantonamentoFondiDubbiaEsigibilita(tipoAccantonamentoFondiDubbiaEsigibilita);
	}
	

}
