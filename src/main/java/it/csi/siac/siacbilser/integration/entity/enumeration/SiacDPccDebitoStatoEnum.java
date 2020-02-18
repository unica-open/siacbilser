/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.StatoDebito;

/**
 * The Enum SiacDPccDebitoStatoEnum.
 * 
 * @author Domenico
 */
@EnumEntity(entityName="SiacDPccDebitoStato", idPropertyName="pccdebStatoId", codePropertyName="pccdebStatoCode")
public enum SiacDPccDebitoStatoEnum {
		
	ImportoLiquidato("LIQ",StatoDebito.Value.ImportoLiquidato),
	EsigibilitaImportoSospesa("SOSP",StatoDebito.Value.EsigibilitaImportoSospesa),
	ImportoValutatoNonLiquidabile("NOLIQ",StatoDebito.Value.ImportoValutatoNonLiquidabile),
	CambioStatoDaSospesoALiquidato("LIQdaSOSP",StatoDebito.Value.CambioStatoDaSospesoALiquidato),
	CambioStatoDaNonLiquidabileALiquidato("LIQdaNL",StatoDebito.Value.CambioStatoDaNonLiquidabileALiquidato),
	CambioStatoDaLiquidatoASospeso("SOSPdaLIQ",StatoDebito.Value.CambioStatoDaLiquidatoASospeso),
	CambioStatoDaNonLiquidabileASospeso("SOSPdaNL",StatoDebito.Value.CambioStatoDaNonLiquidabileASospeso),
	CambioStatoDaLiquidatoANonLiquidabile("NLdaLIQ",StatoDebito.Value.CambioStatoDaLiquidatoANonLiquidabile),
	CambioStatoDaSospesoANonLiquidabile("NLdaSOSP",StatoDebito.Value.CambioStatoDaSospesoANonLiquidabile),
	VariazioneNaturaDiSpesaAllinternoDeslloStatoLiquidato("ADEGLIQ", StatoDebito.Value.VariazioneNaturaDiSpesaAllinternoDelloStatoLiquidato),
	;
	
	/** The codice. */
	private final String codice;
	private final StatoDebito.Value statoDebitoValue;
	
	
	/**
	 * Instantiates a new siac d doc stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDPccDebitoStatoEnum(String codice, StatoDebito.Value statoDebitoValue){
		this.codice = codice;
		this.statoDebitoValue = statoDebitoValue;
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
	public StatoDebito getStatoDebito() {
		return new StatoDebito(codice);
	}
	
	/**
	 * Gets the stato operativo.
	 *
	 * @return the stato operativo
	 */
	public StatoDebito.Value getStatoDebitoValue() {
		return statoDebitoValue;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDPccDebitoStatoEnum byCodice(String codice){
		for(SiacDPccDebitoStatoEnum e : SiacDPccDebitoStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDPccDebitoStatoEnum");
	}
	
	/**
	 * By stato debito pcc value.
	 *
	 * @param statoDebitoValue the tipo operazione pcc value
	 * @return the siac d doc stato enum
	 */
	public static SiacDPccDebitoStatoEnum byStatoDebitoValue(StatoDebito.Value statoDebitoValue){
		for(SiacDPccDebitoStatoEnum e : SiacDPccDebitoStatoEnum.values()){
			if(e.getStatoDebitoValue().equals(statoDebitoValue)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il StatoDebitoValue "+ statoDebitoValue + " non ha un mapping corrispondente in SiacDPccDebitoStatoEnum");
	}
	

}
