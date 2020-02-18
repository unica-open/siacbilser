/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbilser.model.ApplicazioneVariazione;

/**
 * Per generarle in automatico: 
 * 
 * select replace(initcap(applicazione_desc),' ','') || '('|| applicazione_id || ', "' || applicazione_code || '")' || ',' from siac_d_variazione_applicazione.
 */
@EnumEntity(entityName="SiacDVariazioneApplicazione", idPropertyName="applicazioneId", codePropertyName="applicazioneCode")
public enum SiacDVariazioneApplicazioneEnum {
		
	//Assestamento("ASSESTAMENTO", ApplicazioneVariazione.ASSESTAMENTO),
	Gestione("GESTIONE", ApplicazioneVariazione.GESTIONE),
	Previsione("PREVISIONE", ApplicazioneVariazione.PREVISIONE),
	;
	
	
	private final String codice;
	private final ApplicazioneVariazione applicazioneVariazione;

	/**
	 * 
	 * @param codice
	 * @param applicazioneVariazione
	 */
	private SiacDVariazioneApplicazioneEnum(String codice, ApplicazioneVariazione applicazioneVariazione){		
		this.codice = codice;
		this.applicazioneVariazione = applicazioneVariazione;
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
	 * Gets the applicazione variazione.
	 *
	 * @return the applicazione variazione
	 */
	public ApplicazioneVariazione getApplicazioneVariazione() {
		return applicazioneVariazione;
	}
	
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d bil elem tipo enum
	 */
	public static SiacDVariazioneApplicazioneEnum byCodice(String codice){
		for(SiacDVariazioneApplicazioneEnum e : SiacDVariazioneApplicazioneEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDVariazioneApplicazioneEnum");
	}
	
	/**
	 * By applicazione variazione.
	 *
	 * @param applicazioneVariazione the applicazione variazione
	 * @return the siac d bil elem tipo enum
	 */
	public static SiacDVariazioneApplicazioneEnum byApplicazioneVariazione(ApplicazioneVariazione applicazioneVariazione){
		for(SiacDVariazioneApplicazioneEnum e : SiacDVariazioneApplicazioneEnum.values()){
			if(e.getApplicazioneVariazione().equals(applicazioneVariazione)){
				return e;
			}
		}
		
		throw new IllegalArgumentException("ApplicazioneVariazione "+ applicazioneVariazione + " non ha un mapping corrispondente in SiacDVariazioneApplicazioneEnum");
	}
	
	/**
	 * By applicazione variazione even null.
	 *
	 * @param applicazioneVariazione the applicazione variazione
	 * @return the siac d bil elem tipo enum
	 */
	public static SiacDVariazioneApplicazioneEnum byApplicazioneVariazioneEvenNull(ApplicazioneVariazione applicazioneVariazione){
		if(applicazioneVariazione==null){
			return null;
		}
		return byApplicazioneVariazione(applicazioneVariazione);
		
	}
	
	/**
	 * By applicazione variazione list even null.
	 *
	 * @param applicazioniVariazione the applicazioni variazione
	 * @return the list
	 */
	public static List<SiacDVariazioneApplicazioneEnum> byApplicazioneVariazioneListEvenNull(List<ApplicazioneVariazione> applicazioniVariazione){
		List<SiacDVariazioneApplicazioneEnum> result = new ArrayList<SiacDVariazioneApplicazioneEnum>();
		if(applicazioniVariazione!=null){
			for(ApplicazioneVariazione applicazioneVariazione : applicazioniVariazione){
				if(applicazioneVariazione!=null){
					result.add(byApplicazioneVariazione(applicazioneVariazione));
				}
			}
		}
		
		return result;
		
	}
	
	/**
	 * By applicazione variazione list even null.
	 *
	 * @param applicazioniVariazione the applicazioni variazione
	 * @return the list
	 */
	public static List<SiacDVariazioneApplicazioneEnum> byApplicazioneVariazioneListEvenNull(ApplicazioneVariazione... applicazioniVariazione){
			
		return byApplicazioneVariazioneListEvenNull(applicazioniVariazione!=null?Arrays.asList(applicazioniVariazione):null);
		
	}
	
	/**
	 * Gets the codici.
	 *
	 * @param dVariazioneApplicazione the bil elems tipo
	 * @return the codici
	 */
	public static List<String> getCodici(List<SiacDVariazioneApplicazioneEnum> dVariazioneApplicazione){
		List<String> codici = new ArrayList<String>();
		for (SiacDVariazioneApplicazioneEnum siacDVariazioneApplicazioneEnum : dVariazioneApplicazione) {
			codici.add(siacDVariazioneApplicazioneEnum.getCodice());
		}
		return codici;
	}
	

	/**
	 * To list.
	 *
	 * @param applicazioniVariazione the applicazioni variazione
	 * @return the list
	 */
	public static List<SiacDVariazioneApplicazioneEnum> toList(List<ApplicazioneVariazione> applicazioniVariazione) {
		List<SiacDVariazioneApplicazioneEnum> result = new ArrayList<SiacDVariazioneApplicazioneEnum>();
		if(applicazioniVariazione!=null){
			for (ApplicazioneVariazione applicazioneVariazione : applicazioniVariazione) {
				SiacDVariazioneApplicazioneEnum sdbete = byApplicazioneVariazioneEvenNull(applicazioneVariazione);
				if(sdbete!=null) {	
					result.add(sdbete);
				}
			}
		}
		return result;
	}
	
}
