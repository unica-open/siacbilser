/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.TipoCapitolo;


// TODO: Auto-generated Javadoc
/**
 * Per generarle in automatico: 
 * 
 * select replace(initcap(elem_tipo_desc),' ','') || '('|| elem_tipo_id || ', "' || elem_tipo_code || '")' || ',' from siac_d_bil_elem_tipo.
 */
@EnumEntity(entityName="SiacDBilElemTipo", idPropertyName="elemTipoId", codePropertyName="elemTipoCode")
public enum SiacDBilElemTipoEnum {
		
	CapitoloEntrataGestione("CAP-EG", TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE, CapitoloEntrataGestione.class, TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE),
	CapitoloUscitaGestione("CAP-UG", TipoCapitolo.CAPITOLO_USCITA_GESTIONE, CapitoloUscitaGestione.class, TipoCapitolo.CAPITOLO_USCITA_GESTIONE),
	CapitoloEntrataPrevisione("CAP-EP", TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE, CapitoloEntrataPrevisione.class, TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE),
	CapitoloUscitaPrevisione("CAP-UP", TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, CapitoloUscitaPrevisione.class, TipoCapitolo.CAPITOLO_USCITA_GESTIONE),
	

	ArticoloEntrataPrevisione("ART-EP", null, null, null),
	ArticoloUscitaPrevisione("ART-UP", null, null, null),
	ArticoloEntrataGestione("ART-EG", null, null, null),
	ArticoloUscitaGestione("ART-UG", null, null, null),
	

	UnitaElementareBilancioEntrataPrevisione("UEB-EP", null, null, null),
	UnitaElementareBilancioUscitaPrevisione("UEB-UP", null, null, null),
	UnitaElementareBilancioEntrataGestione("UEB-EG", null, null, null),
	UnitaElementareBilancioUscitaGestione("UEB-UG", null, null, null);

	
	/** The codice. */
	private final String codice;
	
	/** The tipo capitolo. */
	private final TipoCapitolo tipoCapitolo;
	
	/** The capitolo class. */
	private final Class<?> capitoloClass;

	private final TipoCapitolo tipoCapitoloEx;

	/**
	 * Instantiates a new siac d bil elem tipo enum.
	 *
	 * @param codice the codice
	 * @param tipoCapitolo the tipo capitolo
	 * @param capitoloClass the capitolo class
	 */
	SiacDBilElemTipoEnum(String codice, TipoCapitolo tipoCapitolo, Class<? extends Capitolo<?, ?>> capitoloClass, TipoCapitolo tipoCapitoloEx){		
		this.codice = codice;
		this.tipoCapitolo = tipoCapitolo;
		this.capitoloClass = capitoloClass;
		this.tipoCapitoloEx = tipoCapitoloEx;
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
	 * Gets the codice single quote.
	 *
	 * @return the codice single quote
	 */
	public String getCodiceSingleQuote() {
		return "'"+codice+"'";
	}
	
	/**
	 * Gets the tipo capitolo.
	 *
	 * @return the tipo capitolo
	 */
	public TipoCapitolo getTipoCapitolo() {
		return tipoCapitolo;
	}
	
	
	
	/**
	 * Gets the capitolo class.
	 *
	 * @return the capitolo class
	 */
	public Class<?> getCapitoloClass() {
		return capitoloClass;
	}

	/**
	 * Gets the capitolo instance.
	 *
	 * @param <T> the generic type
	 * @return the capitolo instance
	 */
	@SuppressWarnings("unchecked")
	public <T extends Capitolo<?, ?>> T getCapitoloInstance() {
		if(capitoloClass==null){
			return null;
		}
		try {
			return (T) capitoloClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Eccezione istanziamento SiacDBilElemTipo."+name()+"->"+capitoloClass + " ["+codice+"]",e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Impossibile accedere al costruttore vuoto di  "+capitoloClass + " ["+codice+"]",e);
		}
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d bil elem tipo enum
	 */
	public static SiacDBilElemTipoEnum byCodice(String codice){
		for(SiacDBilElemTipoEnum e : SiacDBilElemTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemTipoEnum");
	}
	
	/**
	 * By tipo capitolo.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @return the siac d bil elem tipo enum
	 */
	public static SiacDBilElemTipoEnum byTipoCapitolo(TipoCapitolo tipoCapitolo){
		for(SiacDBilElemTipoEnum e : SiacDBilElemTipoEnum.values()){
			if(tipoCapitolo.equals(e.getTipoCapitolo())){
				return e;
			}
		}
		
		throw new IllegalArgumentException("Il tipo capitolo "+ tipoCapitolo + " non ha un mapping corrispondente in SiacDBilElemTipoEnum");
	}
	
	/**
	 * By tipo capitolo even null.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @return the siac d bil elem tipo enum
	 */
	public static SiacDBilElemTipoEnum byTipoCapitoloEvenNull(TipoCapitolo tipoCapitolo){
		if(tipoCapitolo==null){
			return null;
		}
		return byTipoCapitolo(tipoCapitolo);
		
	}
	
	/**
	 * By tipo capitolo list even null.
	 *
	 * @param tipiCapitolo the tipi capitolo
	 * @return the list
	 */
	public static List<SiacDBilElemTipoEnum> byTipoCapitoloListEvenNull(List<TipoCapitolo> tipiCapitolo){
		List<SiacDBilElemTipoEnum> result = new ArrayList<SiacDBilElemTipoEnum>();
		if(tipiCapitolo!=null){
			for(TipoCapitolo tipoCapitolo : tipiCapitolo){
				if(tipoCapitolo!=null){
					result.add(byTipoCapitolo(tipoCapitolo));
				}
			}
		}
		
		return result;
		
	}
	
	/**
	 * By tipo capitolo list even null.
	 *
	 * @param tipiCapitolo the tipi capitolo
	 * @return the list
	 */
	public static List<SiacDBilElemTipoEnum> byTipoCapitoloListEvenNull(TipoCapitolo... tipiCapitolo){
			
		return byTipoCapitoloListEvenNull(tipiCapitolo!=null?Arrays.asList(tipiCapitolo):null);
		
	}
	
	/**
	 * Gets the codici.
	 *
	 * @param bilElemsTipo the bil elems tipo
	 * @return the codici
	 */
	public static List<String> getCodici(List<SiacDBilElemTipoEnum> bilElemsTipo){
		List<String> codici = new ArrayList<String>();
		for (SiacDBilElemTipoEnum siacDBilElemTipoEnum : bilElemsTipo) {
			codici.add(siacDBilElemTipoEnum.getCodice());
		}
		return codici;
	}
	
	
	/**
	 * Gets the codici single quote.
	 *
	 * @param bilElemsTipo the bil elems tipo
	 * @return the codici single quote
	 */
	public static List<String> getCodiciSingleQuote(List<SiacDBilElemTipoEnum> bilElemsTipo){
		List<String> codici = new ArrayList<String>();
		for (SiacDBilElemTipoEnum siacDBilElemTipoEnum : bilElemsTipo) {
			codici.add(siacDBilElemTipoEnum.getCodiceSingleQuote());
		}
		return codici;
	}
	
	/**
	 * Gets the codici single quote comma separated.
	 *
	 * @param bilElemsTipo the bil elems tipo
	 * @return the codici single quote comma separated
	 */
	public static String getCodiciSingleQuoteCommaSeparated(List<SiacDBilElemTipoEnum> bilElemsTipo){
		return StringUtils.join(getCodiciSingleQuote(bilElemsTipo),",");
	}
	
	

//	public SiacDBilElemTipo getEntity(){
//		SiacDBilElemTipo result = new SiacDBilElemTipo();
//		result.setElemTipoId(getId());
//		result.setElemTipoCode(getCodice());
//		return result;
//	}

	
	/**
	 * To list.
	 *
	 * @param tipiCapitolo the tipi capitolo
	 * @return the list
	 */
	public static List<SiacDBilElemTipoEnum> toList(List<TipoCapitolo> tipiCapitolo) {
		List<SiacDBilElemTipoEnum> result = new ArrayList<SiacDBilElemTipoEnum>();
		if(tipiCapitolo!=null){
			for (TipoCapitolo tipoCapitolo : tipiCapitolo) {
				SiacDBilElemTipoEnum sdbete = byTipoCapitoloEvenNull(tipoCapitolo);
				if(sdbete!=null) {	
					result.add(sdbete);
				}
			}
		}
		return result;
	}

	/**
	 * @return the exTipoCapitolo
	 */
	public TipoCapitolo getTipoCapitoloEx() {
		return tipoCapitoloEx;
	}

	


	
}
