/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siaccommon.util.log.LogUtil;

public class ElaborazioniAttiveKeyHandler {

	private static LogUtil log = new LogUtil(ElaborazioniAttiveKeyHandler.class);
	
	//campi caratteristici dell'elemennto
	private Integer uid;
	private Class<?> chiamanteClazz;
	private Class<?> entitaInElaborazioneClazz;
	private String ambito;
	
	private static final String UID_KEY = "%UID%";
	private static final String ENTITA_ELABORATA_CLASS_KEY = "%ELABORATED_CLASS%";
	private static final String CHIAMANTE_CLASS_KEY = "%CALLER_NAME%";
	private static final String AMBITO_KEY = "%AMBITO%";
	
	private Map<String,String> mappaParametri = new HashMap<String, String>();
	

	/**
	 * Instantiates a new elab keys factory.
	 *
	 * @param uid the uid
	 * @param chiamanteClazz the chiamante clazz
	 * @param entitaInElaborazioneClazz the entita in elaborazione clazz
	 * @param ambito the ambito
	 * @param tipoEntita the tipo entita
	 */
	public ElaborazioniAttiveKeyHandler(Integer uid, Class<?> chiamanteClazz,Class<?> entitaInElaborazioneClazz, String ambito) {
		this.uid = uid;
		this.chiamanteClazz = chiamanteClazz;
		this.entitaInElaborazioneClazz = entitaInElaborazioneClazz;
		this.ambito = ambito;
	}
	
	/**
	 * Instantiates a new elab keys factory.
	 *
	 * @param uid the uid
	 * @param chiamanteClazz the chiamante clazz
	 * @param entitaInElaborazioneClazz the entita in elaborazione clazz
	 */
	public ElaborazioniAttiveKeyHandler(Integer uid, Class<?> chiamanteClazz,Class<?> entitaInElaborazioneClazz) {
		this(uid, chiamanteClazz, null, null);
	}
	
	/**
	 * Instantiates a new elab keys factory.
	 *
	 * @param uid the uid
	 * @param chiamanteClazz the chiamante clazz
	 */
	public ElaborazioniAttiveKeyHandler(Integer uid, Class<?> chiamanteClazz) {
		this(uid, chiamanteClazz, null, null);
	}
	
	/**
	 * Instantiates a new elab keys factory.
	 *
	 * @param uid the uid
	 */
	public ElaborazioniAttiveKeyHandler(Integer uid) {
		this(uid, null, null, null);
	}
	
	/**
	 * Gets the mappa parametri.
	 *
	 * @return the mappa parametri
	 */
	private Map<String,String> creaMappaParametri() {
		//TODO: renderla dinamica
		if(this.uid != null) {
			mappaParametri.put(UID_KEY, uid.toString());
		}
		
		if(this.entitaInElaborazioneClazz != null) {
			mappaParametri.put(ENTITA_ELABORATA_CLASS_KEY, entitaInElaborazioneClazz.getSimpleName());
		}
		if(this.chiamanteClazz != null) {
			mappaParametri.put(CHIAMANTE_CLASS_KEY, chiamanteClazz.getSimpleName());
		}
		if(this.ambito != null) {
			mappaParametri.put(AMBITO_KEY, ambito);
		}
		return mappaParametri;
	}
	
	/**
	 * Crea chiavi per blocco elaborazioniattive.
	 *
	 * @param elabKeys the elab keys pattern
	 * @return the map
	 */
	public Map<String,String> creaChiaviPerBloccoElaborazioniAttive(ElabKeys elabKeys){
		final String methodName ="creaChiaviPerBloccoElaborazioniattive";
		
		Map<String,String> chiaviElaborazioneAttiva = new HashMap<String, String>();
		String elabService = creaElabServiceFromPattern(elabKeys);
		String elabKey = creaElabKeyFromPattern(elabKeys);
		
		if(StringUtils.isEmpty(elabService) || StringUtils.isEmpty(elabKey)) {
			log.error(methodName, "Impossibile ottenere la coppia elabService/elabKey. elabKey trovata? " + StringUtils.isEmpty(elabKey) + " elabservice trovata? " + StringUtils.isEmpty(elabService));
			throw new IllegalStateException("Impossibile creare una coppia di stringhe per l'elaborazione attiva");
		}
		chiaviElaborazioneAttiva.put(ElabKeys.ELAB_SERVICE_IDENTIFIER, elabService);
		chiaviElaborazioneAttiva.put(ElabKeys.ELAB_KEY_IDENTIFIER, elabKey);
		return chiaviElaborazioneAttiva;
	}

	
	/**
	 * Crea la stringa delle azioni a partire dal pattern, dalla mappa delle sostituzioni e dalla stringa con i placeholder
	 * @param pattern il pattern da utilizzare
	 * @param map     la mappa delle sostituzioni
	 * @param elabKeys 
	 * @param string  la stringa con i placeholder da sostituire
	 * 
	 * @return una stringa corrispondente alla stringa di base con i placeholder sostituiti secondo la mappa fornita
	 */
	public String creaElabServiceFromPattern(ElabKeys elabKeys) {
		//chiamo il metodo di creazione della stringa con il giusto pattern e la giusta stringa da, eventualmente, elaborare
		return creaStringaByPatternEGruppo(elabKeys.getElabServicePattern(),  elabKeys.getElabServiceBaseString());
	}
	
	/**
	 * Crea la stringa elabKey a partire una stringa base, un pattern utilizzato per sostituire eventuali placeholder all'interno della stringa 
	 * @param elabKeys il tipo di elabKey richiesta
	 * 
	 * @return la stringa elaborata
	 */
	public String creaElabKeyFromPattern(ElabKeys elabKeys) {
		//chiamo il metodo di creazione della stringa con il giusto pattern e la giusta stringa da, eventualmente, elaborare
		return creaStringaByPatternEGruppo(elabKeys.getElabKeyPattern(), elabKeys.getElabKeyBaseString());
	}

	
	/**
	 * Crea una stringa a partire da una stringa base sostituendo gli eventuali place holder in base ai valori passati all'oggetto durante l'instanziazione. 
	 *
	 * @param pattern il pattern in base al quale sostituire eventuali placeholder
	 * @param elabBaseString la stringa su cui effettuare le sostituzioni 
	 * @return la stringa elaborata
	 */
	private String creaStringaByPatternEGruppo(Pattern pattern, String elabBaseString) {
		final String methodName = "creaStringaByPatternEGruppo";
		Map<String, String> map = creaMappaParametri();
		//valutare. potrei accettare una mappa vuota (ed un costruttore senza parametri), se prendo di default la stringa dell'enum. Pero' non avrebbe senso avere una chiave non specificata da nemmeno un uid
		if(map ==null || map.isEmpty()) {
			log.error(methodName, "impossibile ottenere i parametri attraverso i quali popolare le chiavi.");
			throw new IllegalArgumentException("Impossibile creare una chiave per l'elaborazione: parametri null.");
		}
		if(pattern == null) {
			//il pattern non deve essere eseguito, quindi posso andarmene
			log.debug(methodName, "Non eseguo nessuna elaborazione sulla stringa base.");
			return elabBaseString;
		}
		return StringUtilities.replacePlaceholders(pattern, elabBaseString, map, true);
	}

	
    /**
     * Gets the uid by elab key.
     *
     * @param elab the elab
     * @param elaboratedString the elaborated string
     * @return the uid by elab key
     */
    public static Integer getUidByElabKey(ElabKeys elab, String elaboratedString) {
    	final String methodName="getUidByElabKey";
    	if(elab == null || StringUtils.isEmpty(elaboratedString)) {
    		return null;
    	}
    	Matcher m = Pattern.compile(UID_KEY).matcher(elab.getElabKeyBaseString());
		  while (m.find()) {
			  String ss = StringUtils.substring(elaboratedString, m.start());
		    	Integer uid = null;
		    	try {
					uid = Integer.valueOf(ss);
				} catch (NumberFormatException e) {
					log.error(methodName, "Impossibile ottenere l'id dalla stringa " + ss);
					return null;
				}
				return uid;
		  }
    	return null;
	}
}
