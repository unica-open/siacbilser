/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.TipoVincoloCapitoli;


// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDVincoloTipoEnum.
 */
@EnumEntity(entityName="SiacDVincoloTipo", idPropertyName="vincoloTipoId", codePropertyName="vincoloTipoCode")
public enum SiacDVincoloTipoEnum {
	
	/** The Previsione. */
	Previsione("P", TipoVincoloCapitoli.PREVISIONE),
	
	/** The Gestione. */
	Gestione("G", TipoVincoloCapitoli.GESTIONE);
	
	/** The codice. */
	private final String codice;
	
	/** The tipo vincolo capitoli. */
	private final TipoVincoloCapitoli tipoVincoloCapitoli;
	
	/**
	 * Instantiates a new siac d vincolo tipo enum.
	 *
	 * @param codice the codice
	 * @param tipoVincoloCapitoli the tipo vincolo capitoli
	 */
	private SiacDVincoloTipoEnum(String codice, TipoVincoloCapitoli tipoVincoloCapitoli){
		this.codice = codice;
		this.tipoVincoloCapitoli = tipoVincoloCapitoli;
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
	 * Gets the tipo vincolo capitoli.
	 *
	 * @return the tipo vincolo capitoli
	 */
	public TipoVincoloCapitoli getTipoVincoloCapitoli() {
		return tipoVincoloCapitoli;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d vincolo tipo enum
	 */
	public static SiacDVincoloTipoEnum byCodice(String codice){
		for(SiacDVincoloTipoEnum e : SiacDVincoloTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDVincoloTipoEnum");
	}
	
	/**
	 * By tipo vincolo capitoli.
	 *
	 * @param tipo the tipo
	 * @return the siac d vincolo tipo enum
	 */
	public static SiacDVincoloTipoEnum byTipoVincoloCapitoli(TipoVincoloCapitoli tipo){
		for(SiacDVincoloTipoEnum e : SiacDVincoloTipoEnum.values()){
			if(e.getTipoVincoloCapitoli().equals(tipo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo vincolo capitoli "+ tipo + " non ha un mapping corrispondente in SiacDVincoloTipoEnum");
	}
	
	/**
	 * By tipo vincolo capitoli even null.
	 *
	 * @param tipo the tipo
	 * @return the siac d vincolo tipo enum
	 */
	public static SiacDVincoloTipoEnum byTipoVincoloCapitoliEvenNull(TipoVincoloCapitoli tipo){
		if(tipo==null){
			return null;
		}
		return byTipoVincoloCapitoli(tipo);
	}
	

}
