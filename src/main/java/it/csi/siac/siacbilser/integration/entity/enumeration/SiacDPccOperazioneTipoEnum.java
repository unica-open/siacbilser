/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;

/**
 * The Enum SiacDPccOperazioneTipoEnum.
 * 
 * @author Domenico
 */
@EnumEntity(entityName="SiacDPccOperazioneTipo", idPropertyName="pccopTipoId", codePropertyName="pccopTipoCode")
public enum SiacDPccOperazioneTipoEnum {
		
	
	Contabilizzazione("CO", TipoOperazionePCC.Value.Contabilizzazione),
	ComunicazioneDataScadenza("CS", TipoOperazionePCC.Value.ComunicazioneDataScadenza),
	ComunicazionePagamento("CP", TipoOperazionePCC.Value.ComunicazionePagamento),
	CancellazioneComunicazioniScadenza("CCS", TipoOperazionePCC.Value.CancellazioneComunicazioniScadenza),
	;
	
	/** The codice. */
	private final String codice;
	private final TipoOperazionePCC.Value tipoOperazioneValue;
	
	
	/**
	 * Instantiates a new siac d doc stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDPccOperazioneTipoEnum(String codice, TipoOperazionePCC.Value tipoOperazioneValue){
		this.codice = codice;
		this.tipoOperazioneValue = tipoOperazioneValue;
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
	 * Gets the stato operativo.
	 *
	 * @return the stato operativo
	 */
	public TipoOperazionePCC getTipoOpeazionePCC() {
		return new TipoOperazionePCC(codice);
	}
	
	/**
	 * Gets the stato operativo.
	 *
	 * @return the stato operativo
	 */
	public TipoOperazionePCC.Value getTipoOpeazionePCCValue() {
		return tipoOperazioneValue;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDPccOperazioneTipoEnum byCodice(String codice){
		for(SiacDPccOperazioneTipoEnum e : SiacDPccOperazioneTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDPccOperazioneTipoEnum");
	}
	
	/**
	 * By tipo operazione pcc value.
	 *
	 * @param tipoOperazionePCCValue the tipo operazione pcc value
	 * @return the siac d doc stato enum
	 */
	public static SiacDPccOperazioneTipoEnum byTipoOperazionePCCValue(TipoOperazionePCC.Value tipoOperazionePCCValue){
		for(SiacDPccOperazioneTipoEnum e : SiacDPccOperazioneTipoEnum.values()){
			if(e.getTipoOpeazionePCCValue().equals(tipoOperazionePCCValue)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipoOperazionePCCValue "+ tipoOperazionePCCValue + " non ha un mapping corrispondente in SiacDPccOperazioneTipoEnum");
	}
	

}
