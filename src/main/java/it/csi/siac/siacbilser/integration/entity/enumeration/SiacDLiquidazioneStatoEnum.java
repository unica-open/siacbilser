/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

/**
 * The Enum SiacDLiquidazioneStatoEnum.
 */
@EnumEntity(entityName="SiacDLiquidazioneStato", idPropertyName="liqStatoId", codePropertyName="liqStatoCode")
public enum SiacDLiquidazioneStatoEnum {
	
	Provvisorio("P", Liquidazione.StatoOperativoLiquidazione.PROVVISORIO),
	Valido("V", Liquidazione.StatoOperativoLiquidazione.VALIDO),
	Annullato("A", Liquidazione.StatoOperativoLiquidazione.ANNULLATO);
	

	private final String codice;
	private final Liquidazione.StatoOperativoLiquidazione statoOperativoLiquidazione;

	

	/**
	 * Instantiates a new siac d liquidazione stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativoLiquidazione the stato operativo liquidazione
	 */
	SiacDLiquidazioneStatoEnum(String codice,  Liquidazione.StatoOperativoLiquidazione statoOperativoLiquidazione){
		this.codice = codice;
		this.statoOperativoLiquidazione = statoOperativoLiquidazione;
	}
	
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d liquidazione stato enum
	 */
	public static SiacDLiquidazioneStatoEnum byCodice(String codice){
		for(SiacDLiquidazioneStatoEnum e : SiacDLiquidazioneStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Stato Liquidazione "+ codice + " non ha un mapping corrispondente in SiacDLiquidazioneStatoEnum");
	}
	
	/**
	 * By stato operativo liquidazione.
	 *
	 * @param statoOperativoLiquidazione the stato operativo liquidazione
	 * @return the siac d liquidazione stato enum
	 */
	public static SiacDLiquidazioneStatoEnum byStatoOperativoLiquidazione(Liquidazione.StatoOperativoLiquidazione statoOperativoLiquidazione){
		for(SiacDLiquidazioneStatoEnum e : SiacDLiquidazioneStatoEnum.values()){
			if(e.getStatoOperativoLiquidazione().equals(statoOperativoLiquidazione)){
				return e;
			}
		}
		throw new IllegalArgumentException("Stato Liquidazione "+ statoOperativoLiquidazione + " non ha un mapping corrispondente in SiacDLiquidazioneStatoEnum");
	}
	
	/**
	 * By stato operativo liquidazione even null.
	 *
	 * @param statoOperativoLiquidazione the stato operativo liquidazione
	 * @return the siac d liquidazione stato enum
	 */
	public static SiacDLiquidazioneStatoEnum byStatoOperativoLiquidazioneEvenNull(Liquidazione.StatoOperativoLiquidazione statoOperativoLiquidazione) {
		if(statoOperativoLiquidazione == null) {
			return null;
		}
		return byStatoOperativoLiquidazione(statoOperativoLiquidazione);
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
	 * Gets the tipo famiglia documento.
	 *
	 * @return the tipo famiglia documento
	 */
	public Liquidazione.StatoOperativoLiquidazione getStatoOperativoLiquidazione() {
		return statoOperativoLiquidazione;
	}
	
	

}