/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;

/**
 * The Enum SiacDBilElemDetCompTipoDefEnum.
 */
@EnumEntity(entityName="SiacDBilElemDetCompTipoDef", idPropertyName="elemDetCompTipoDefId", codePropertyName="elemDetCompTipoDefCode")
public enum SiacDBilElemDetCompTipoDefEnum {

	SI("01", PropostaDefaultComponenteImportiCapitolo.SI),
	NO("02", PropostaDefaultComponenteImportiCapitolo.NO),
	SOLO_PREVISIONE("03", PropostaDefaultComponenteImportiCapitolo.SOLO_PREVISIONE),
	SOLO_GESTIONE("04", PropostaDefaultComponenteImportiCapitolo.SOLO_GESTIONE),
	;
	
	/** The codice. */
	private final String codice;
	/** The proposta default componente importi capitolo. */
	private final PropostaDefaultComponenteImportiCapitolo propostaDefaultComponenteImportiCapitolo;
	
	/**
	 * Instantiates a new siac d bil elem det comp fonte finanziaria enum.
	 *
	 * @param codice the codice
	 * @param propostaDefaultComponenteImportiCapitolo the proposta default componente importi capitolo
	 */
	private SiacDBilElemDetCompTipoDefEnum(String codice, PropostaDefaultComponenteImportiCapitolo propostaDefaultComponenteImportiCapitolo){
		this.codice = codice;
		this.propostaDefaultComponenteImportiCapitolo = propostaDefaultComponenteImportiCapitolo;
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
	 * @return the propostaDefaultComponenteImportiCapitolo
	 */
	public PropostaDefaultComponenteImportiCapitolo getPropostaDefaultComponenteImportiCapitolo() {
		return this.propostaDefaultComponenteImportiCapitolo;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d bil elem det comp tipo def enum
	 */
	public static SiacDBilElemDetCompTipoDefEnum byCodice(String codice){
		for(SiacDBilElemDetCompTipoDefEnum e : SiacDBilElemDetCompTipoDefEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemDetCompTipoDefEnum");
	}
	
	

	/**
	 * By proposta default componente importi capitolo.
	 *
	 * @param proposta defaultComponenteImportiCapitolo the proposta defaultComponenteImportiCapitolo
	 * @return the siac d bil elem det comp tipo def enum
	 */
	public static SiacDBilElemDetCompTipoDefEnum byPropostaDefaultComponenteImportiCapitolo(PropostaDefaultComponenteImportiCapitolo propostaDefaultComponenteImportiCapitolo){
		for(SiacDBilElemDetCompTipoDefEnum e : SiacDBilElemDetCompTipoDefEnum.values()){
			if(e.getPropostaDefaultComponenteImportiCapitolo().equals(propostaDefaultComponenteImportiCapitolo)){
				return e;
			}
		}
		throw new IllegalArgumentException("La proposta default " + propostaDefaultComponenteImportiCapitolo + " non ha un mapping corrispondente in SiacDBilElemDetTipoDefEnum");
	}
	/**
	 * By fonte finanziaria componente importi capitolo, even null.
	 *
	 * @param propostaDefaultComponenteImportiCapitolo the propostaDefaultComponenteImportiCapitolo
	 * @return the siac d bil elem det comp tipo def enum
	 */
	public static SiacDBilElemDetCompTipoDefEnum byPropostaDefaultComponenteImportiCapitoloEvenNull(PropostaDefaultComponenteImportiCapitolo propostaDefaultComponenteImportiCapitolo){
		if(propostaDefaultComponenteImportiCapitolo == null){
			return null;
		}
		return byPropostaDefaultComponenteImportiCapitolo(propostaDefaultComponenteImportiCapitolo);
	}
	
}
