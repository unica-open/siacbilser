/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;

/**
 * The Enum SiacDDocFamTipoEnum.
 */
@EnumEntity(entityName="SiacDProvCassaTipo", idPropertyName="provcTipoId", codePropertyName="provcTipoCode")
public enum SiacDProvCassaTipoEnum {
	
	/** The Entrata. */
	Entrata("E", TipoProvvisorioDiCassa.E),
	
	/** The Spesa. */
	Spesa("S", TipoProvvisorioDiCassa.S);
	

	/** The codice. */
	private final String codice;
	
	/** The tipo provvisorio. */
	private final TipoProvvisorioDiCassa tipoProvvisorioDiCassa;

	/**
	 * Instantiates a new siac d doc fam tipo enum.
	 *
	 * @param codice the codice
	 * @param importiCapitoloFieldName - nome del Field in nella classe di Model <? extends ImportiCapitolo>
	 */
	SiacDProvCassaTipoEnum(String codice, TipoProvvisorioDiCassa tipoProvvisorioDiCassa){
		this.codice = codice;
		this.tipoProvvisorioDiCassa = tipoProvvisorioDiCassa;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc fam tipo enum
	 */
	public static SiacDProvCassaTipoEnum byCodice(String codice){
		for(SiacDProvCassaTipoEnum e : SiacDProvCassaTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Tipo provvisorio "+ codice + " non ha un mapping corrispondente in SiacDProvCassaTipoEnum");
	}
	
	/**
	 * By tipo famiglia documento.
	 *
	 * @param tipoFamigliaDocumento the tipo famiglia documento
	 * @return the siac d doc fam tipo enum
	 */
	public static SiacDProvCassaTipoEnum byTipoProvvisorioDiCassa(TipoProvvisorioDiCassa tipoProvvisorioDiCassa){
		for(SiacDProvCassaTipoEnum e : SiacDProvCassaTipoEnum.values()){
			if(e.getTipoProvvisorioDiCassa().equals(tipoProvvisorioDiCassa)){
				return e;
			}
		}
		throw new IllegalArgumentException("Tipo provvisorio "+ tipoProvvisorioDiCassa + " non ha un mapping corrispondente in SiacDProvCassaTipoEnum");
	}
	
	/**
	 * By tipo famiglia documento even null.
	 *
	 * @param tipoFamigliaDocumento the tipo famiglia documento
	 * @return the siac d doc fam tipo enum
	 */
	public static SiacDProvCassaTipoEnum byTipoProvvisorioDiCassaEvenNull(TipoProvvisorioDiCassa tipoProvvisorioDiCassa) {
		if(tipoProvvisorioDiCassa == null) {
			return null;
		}
		return byTipoProvvisorioDiCassa(tipoProvvisorioDiCassa);
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
	public TipoProvvisorioDiCassa getTipoProvvisorioDiCassa() {
		return tipoProvvisorioDiCassa;
	}

}