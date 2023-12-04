/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.CausalePCC;

/**
 * The Enum SiacDPccCausaleEnum.
 * 
 * @author Domenico
 */
@EnumEntity(entityName="SiacDPccCausale", idPropertyName="pcccauId", codePropertyName="pcccauCode")
public enum SiacDPccCausaleEnum {
	
	AttesaLiquidazione("ATTLIQ", CausalePCC.Value.AttesaLiquidazione),
	Contenzioso("CONT", CausalePCC.Value.Contenzioso),
	AttesaNotaCredito("ATTNC", CausalePCC.Value.AttesaNotaCredito),
	NotaCredito("NCRED", CausalePCC.Value.NotaCredito),
	PagataTerzi("PAGTERZI", CausalePCC.Value.PagataTerzi),
	IvaReverseChange("IVARC", CausalePCC.Value.IvaReverseChange),
	;

	
	/** The codice. */
	private final String codice;
	private final CausalePCC.Value causalePCCValue;
	
	
	/**
	 * Instantiates a new siac d doc stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDPccCausaleEnum(String codice, CausalePCC.Value causalePCCValue){
		this.codice = codice;
		this.causalePCCValue = causalePCCValue;
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
	public CausalePCC getCausalePCC() {
		return new CausalePCC(codice);
	}
	
	/**
	 * Gets the stato operativo.
	 *
	 * @return the stato operativo
	 */
	public CausalePCC.Value getCausalePCCValue() {
		return causalePCCValue;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDPccCausaleEnum byCodice(String codice){
		for(SiacDPccCausaleEnum e : SiacDPccCausaleEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDPccCausaleEnum");
	}
	
	/**
	 * By causale pcc value.
	 *
	 * @param causalePCCValue the tipo operazione pcc value
	 * @return the siac d doc stato enum
	 */
	public static SiacDPccCausaleEnum byCausalePCCValue(CausalePCC.Value causalePCCValue){
		for(SiacDPccCausaleEnum e : SiacDPccCausaleEnum.values()){
			if(e.getCausalePCCValue().equals(causalePCCValue)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il CausalePCCValue "+ causalePCCValue + " non ha un mapping corrispondente in SiacDPccCausaleEnum");
	}
	

}
