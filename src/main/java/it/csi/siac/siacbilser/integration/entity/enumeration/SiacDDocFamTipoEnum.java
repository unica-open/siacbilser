/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

/**
 * The Enum SiacDDocFamTipoEnum.
 */
@EnumEntity(entityName="SiacDDocFamTipo", idPropertyName="docFamTipoId", codePropertyName="docFamTipoCode")
public enum SiacDDocFamTipoEnum {
	
	
	IvaEntrata("IE", TipoFamigliaDocumento.IVA_ENTRATA),
	IvaSpesa("IS", TipoFamigliaDocumento.IVA_SPESA),
	Entrata("E", TipoFamigliaDocumento.ENTRATA, IvaEntrata),
	Spesa("S", TipoFamigliaDocumento.SPESA, IvaSpesa);


	private final String codice;
	private final TipoFamigliaDocumento tipoFamigliaDocumento;
	private final SiacDDocFamTipoEnum equivalenteIva;

	
	public static final String CODICE_SPESA = "S";
	public static final String CODICE_ENTRATA = "E";
	public static final String CODICE_IVA_SPESA = "IS";
	public static final String CODICE_IVA_ENTRATA = "IE";
	

	/**
	 * Instantiates a new siac d doc fam tipo enum.
	 *
	 * @param codice the codice
	 * @param importiCapitoloFieldName - nome del Field in nella classe di Model <? extends ImportiCapitolo>
	 */
	SiacDDocFamTipoEnum(String codice, TipoFamigliaDocumento importiCapitoloFieldName){
		this(codice, importiCapitoloFieldName, null);
	}
	
	SiacDDocFamTipoEnum(String codice, TipoFamigliaDocumento importiCapitoloFieldName, SiacDDocFamTipoEnum equivalenteIva){
		this.codice = codice;
		this.tipoFamigliaDocumento = importiCapitoloFieldName;
		this.equivalenteIva = equivalenteIva;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc fam tipo enum
	 */
	public static SiacDDocFamTipoEnum byCodice(String codice){
		for(SiacDDocFamTipoEnum e : SiacDDocFamTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Famiglia documento "+ codice + " non ha un mapping corrispondente in SiacDDocFamTipoEnum");
	}
	
	/**
	 * By tipo famiglia documento.
	 *
	 * @param tipoFamigliaDocumento the tipo famiglia documento
	 * @return the siac d doc fam tipo enum
	 */
	public static SiacDDocFamTipoEnum byTipoFamigliaDocumento(TipoFamigliaDocumento tipoFamigliaDocumento){
		for(SiacDDocFamTipoEnum e : SiacDDocFamTipoEnum.values()){
			if(e.getTipoFamigliaDocumento().equals(tipoFamigliaDocumento)){
				return e;
			}
		}
		throw new IllegalArgumentException("Famiglia documento "+ tipoFamigliaDocumento + " non ha un mapping corrispondente in SiacDDocFamTipoEnum");
	}
	
	/**
	 * By tipo famiglia documento even null.
	 *
	 * @param tipoFamigliaDocumento the tipo famiglia documento
	 * @return the siac d doc fam tipo enum
	 */
	public static SiacDDocFamTipoEnum byTipoFamigliaDocumentoEvenNull(TipoFamigliaDocumento tipoFamigliaDocumento) {
		if(tipoFamigliaDocumento == null) {
			return null;
		}
		return byTipoFamigliaDocumento(tipoFamigliaDocumento);
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
	public TipoFamigliaDocumento getTipoFamigliaDocumento() {
		return tipoFamigliaDocumento;
	}

	/**
	 * @return the equivalenteIva
	 */
	public SiacDDocFamTipoEnum getEquivalenteIva() {
		return equivalenteIva;
	}
	
	public static List<String> getCodices() {
		List<String> result = new ArrayList<String>();
		for(SiacDDocFamTipoEnum sddfte : values()) {
			result.add(sddfte.getCodice());
		}
		return result;
	}
	
	public static Set<SiacDDocFamTipoEnum> byTipoFamigliaDocumento(Collection<TipoFamigliaDocumento> tipiFamigliaDocumento) {
		return byTipiFamiglia(tipiFamigliaDocumento!=null && !tipiFamigliaDocumento.isEmpty()?EnumSet.copyOf(tipiFamigliaDocumento):null);
	}
	
	public static Set<SiacDDocFamTipoEnum> byTipiFamiglia(Set<TipoFamigliaDocumento> tipi) {
		Set<SiacDDocFamTipoEnum> enums = EnumSet.noneOf(SiacDDocFamTipoEnum.class);
		if(tipi!=null){
			for(TipoFamigliaDocumento tipo : tipi){
				enums.add(byTipoFamigliaDocumento(tipo));
			}
		}
		return enums;
	}
	

}