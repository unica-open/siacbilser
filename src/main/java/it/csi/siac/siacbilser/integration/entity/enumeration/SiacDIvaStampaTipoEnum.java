/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoStampaIva;

/**
 * Enum per l'entity SiacDIvaStampaTipo
 *
 */
@EnumEntity(entityName="SiacDIvaStampaTipo", idPropertyName="ivastTipoId", codePropertyName="ivastTipoCode")
public enum SiacDIvaStampaTipoEnum {
	
	
	Registro    ("REG", TipoStampaIva.REGISTRO),
	
	LiquidazioneIva    ("LIQ", TipoStampaIva.LIQUIDAZIONE_IVA),
	
	RiepilogoAnnuale    ("RIE", TipoStampaIva.RIEPILOGO_ANNUALE),

	;
	
	
	
	/** The codice. */
	private final String codice;
	
	/** The tipo stampa. */
	private final TipoStampaIva tipoStampaIva;
	
	
	/**
	 * Instantiates a new siac d iva registro tipo enum.
	 *
	 * @param codice the codice
	 * @param tipoRegistroIva the tipo registro iva
	 */
	private SiacDIvaStampaTipoEnum(String codice, TipoStampaIva tipoStampaIva){
		this.codice = codice;
		this.tipoStampaIva = tipoStampaIva;
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
	 * Gets the tipo registro iva.
	 *
	 * @return the tipo registro iva
	 */
	public TipoStampaIva getTipoStampaIva() {
		return tipoStampaIva;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d iva registro tipo enum
	 */
	public static SiacDIvaStampaTipoEnum byCodice(String codice){
		for(SiacDIvaStampaTipoEnum e : SiacDIvaStampaTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDIvaStampaTipoEnum");
	}
	
	/**
	 * By tipo registro iva.
	 *
	 * @param tipoStampaIva the tipo
	 * @return the siac d iva registro tipo enum
	 */
	public static SiacDIvaStampaTipoEnum byTipoStampaIva(TipoStampaIva tipoStampaIva){
		for(SiacDIvaStampaTipoEnum e : SiacDIvaStampaTipoEnum.values()){
			if(e.getTipoStampaIva().equals(tipoStampaIva)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo stampa iva "+ tipoStampaIva + " non ha un mapping corrispondente in SiacDIvaStampaTipoEnum");
	}
	

}
