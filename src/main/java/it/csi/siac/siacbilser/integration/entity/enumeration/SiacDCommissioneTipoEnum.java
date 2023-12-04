/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.CommissioniDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDCommissioneTipoEnum.
 */
@EnumEntity(entityName="SiacDCommissioneTipo", idPropertyName="commTipoId", codePropertyName="commTipoCode")
public enum SiacDCommissioneTipoEnum {
	
	/** The Beneficiario. */
	Beneficiario("BN", CommissioniDocumento.BENEFICIARIO),
	
	/** The Esente. */
	Esente("ES", CommissioniDocumento.ESENTE),
	
	/** The Carico ente. */
	CaricoEnte("CE", CommissioniDocumento.CARICO_ENTE);
	
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final CommissioniDocumento statoOperativo;
	
	/**
	 * Instantiates a new siac d commissione tipo enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDCommissioneTipoEnum(String codice, CommissioniDocumento statoOperativo){
		this.codice = codice;
		this.statoOperativo = statoOperativo;
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
	 * Gets the commissioni documento.
	 *
	 * @return the commissioni documento
	 */
	public CommissioniDocumento getCommissioniDocumento() {
		return statoOperativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d commissione tipo enum
	 */
	public static SiacDCommissioneTipoEnum byCodice(String codice){
		for(SiacDCommissioneTipoEnum e : SiacDCommissioneTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDCommissioneTipoEnum");
	}
	
	
	/**
	 * By commissioni documento.
	 *
	 * @param commissioniDocumento the commissioni documento
	 * @return the siac d commissione tipo enum
	 */
	public static SiacDCommissioneTipoEnum byCommissioniDocumento(CommissioniDocumento commissioniDocumento){
		for(SiacDCommissioneTipoEnum e : SiacDCommissioneTipoEnum.values()){
			if(e.getCommissioniDocumento().equals(commissioniDocumento)){
				return e;
			}
		}
		throw new IllegalArgumentException("La commissioniDocumento "+ commissioniDocumento + " non ha un mapping corrispondente in SiacDCommissioneTipoEnum");
	}
	
	/**
	 * By commissioni documento even null.
	 *
	 * @param commissioniDocumento the commissioni documento
	 * @return the siac d commissione tipo enum. null if input is null
	 */
	public static SiacDCommissioneTipoEnum byCommissioniDocumentoEvenNull(CommissioniDocumento commissioniDocumento){
		if(commissioniDocumento==null){
			return null;
		}
		return byCommissioniDocumento(commissioniDocumento);
	}
	

}
