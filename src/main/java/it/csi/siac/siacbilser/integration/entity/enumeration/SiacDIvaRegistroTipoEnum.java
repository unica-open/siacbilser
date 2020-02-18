/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDIvaRegistroTipoEnum.
 */
@EnumEntity(entityName="SiacDIvaRegistroTipo", idPropertyName="ivaregTipoId", codePropertyName="ivaregTipoCode")
public enum SiacDIvaRegistroTipoEnum {
	
	/** The Corrispettivi. */
	Corrispettivi       ("CI", TipoRegistroIva.CORRISPETTIVI),
	
	/** The Acquisti iva immediata. */
	AcquistiIvaImmediata("AI", TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA),
	
	/** The Acquisti iva differita. */
	AcquistiIvaDifferita("AD", TipoRegistroIva.ACQUISTI_IVA_DIFFERITA),
	
	/** The Vendite iva immediata. */
	VenditeIvaImmediata ("VI", TipoRegistroIva.VENDITE_IVA_IMMEDIATA),
	
	/** The Vendite iva differita. */
	VenditeIvaDifferita ("VD", TipoRegistroIva.VENDITE_IVA_DIFFERITA),
	;
	
	
	/** The codice. */
	private final String codice;
	
	/** The tipo registro iva. */
	private final TipoRegistroIva tipoRegistroIva;
	
	
	/**
	 * Instantiates a new siac d iva registro tipo enum.
	 *
	 * @param codice the codice
	 * @param tipoRegistroIva the tipo registro iva
	 */
	private SiacDIvaRegistroTipoEnum(String codice, TipoRegistroIva tipoRegistroIva){
		this.codice = codice;
		this.tipoRegistroIva = tipoRegistroIva;
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
	public TipoRegistroIva getTipoRegistroIva() {
		return tipoRegistroIva;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d iva registro tipo enum
	 */
	public static SiacDIvaRegistroTipoEnum byCodice(String codice){
		for(SiacDIvaRegistroTipoEnum e : SiacDIvaRegistroTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDIvaRegistroTipoEnum");
	}
	
	/**
	 * By tipo registro iva.
	 *
	 * @param tipo the tipo
	 * @return the siac d iva registro tipo enum
	 */
	public static SiacDIvaRegistroTipoEnum byTipoRegistroIva(TipoRegistroIva tipo){
		for(SiacDIvaRegistroTipoEnum e : SiacDIvaRegistroTipoEnum.values()){
			if(e.getTipoRegistroIva().equals(tipo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo registro iva "+ tipo + " non ha un mapping corrispondente in SiacDIvaRegistroTipoEnum");
	}
	

}
