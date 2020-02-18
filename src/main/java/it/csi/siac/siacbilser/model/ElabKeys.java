/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.model;

import java.util.regex.Pattern;

public enum ElabKeys {
	//TODO:togliere il elabKeyPattern
	PRIMA_NOTA(                                "elabPrimaNota.movId:%UID%,type:%ELABORATED_CLASS%", "%(UID|ELABORATED_CLASS)%", "%AMBITO%",                      "%(UID|AMBITO)%"),
	PRIMA_NOTA_NCD(                            "elabPrimaNota.movId:%UID%,type:%ELABORATED_CLASS%", "%(UID|ELABORATED_CLASS)%", "%AMBITO%_NCD",                  "%(UID|AMBITO)%"),
	PRIMA_NOTA_NCD_(                           "elabPrimaNota.movId:%UID%,type:%ELABORATED_CLASS%", "%(UID|ELABORATED_CLASS)%", "%AMBITO%_NCD",                  "%(UID|AMBITO)%"),
	REGISTRAZIONE_MOV_FIN(                     "RegistrazioneMovFin", 					            null,                       "RegistrazioneMovFin.uid:%UID%", "%UID%"),
	EMISSIONE_ORDINATIVI_PAGAMENTO(            "%CALLER_NAME%",                    	                "%CALLER_NAME%",            "subdoc:%UID%",                  "%UID%"),
	EMISSIONE_ORDINATIVI_INCASSO(              "%CALLER_NAME%",                    	                "%CALLER_NAME%",            "subdoc:%UID%",                  "%UID%"),
	COMPLETA_ALLEGATO_ATTO(                    "CompletaAllegatoAttoService",                       null,                       "allegatoAtto.uid:%UID%",        "%UID%"),
	AGGIORNA_VARIAZIONE(                       "%CALLER_NAME%",                                     "%CALLER_NAME%",            "variazione.uid:%UID%",          "%UID%"),
	//SIAC-7271
	DEFINISCI_VARIAZIONE(                      "%CALLER_NAME%",                                     "%CALLER_NAME%",            "variazione.uid:%UID%",          "%UID%"),
	STAMPA_REGISTRO_IVA(                       "%CALLER_NAME%",                                     "%CALLER_NAME%",            "registroIva.uid:%UID%",         "%UID%"),
	POPOLA_FONDI_DUBBIA_ESIGIBILITA(           "%CALLER_NAME%",                                     "%CALLER_NAME%",            "bilancio.uid:%UID%",            "%UID%"),
	POPOLA_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO("%CALLER_NAME%",                                     "%CALLER_NAME%",            "bilancio.uid:%UID%",            "%UID%"),
	// SIAC-5481
	POPOLA_FONDI_DUBBIA_ESIGIBILITA_GESTIONE(  "%CALLER_NAME%",                                     "%CALLER_NAME%",            "bilancio.uid:%UID%",            "%UID%"),
	;
	public static final String ELAB_KEY_IDENTIFIER = "ELAB_KEY";
	public static final String ELAB_SERVICE_IDENTIFIER = "ELAB_SERVICE";
	
	private final String elabServiceBaseString;
	private final Pattern elabServicePattern;
	private final String elabKeyBaseString;
	private final Pattern elabKeyPattern;
	
	
	private ElabKeys(String elabServiceBaseString, String elabServicePatternString, String elabKeyBaseString, String elabKeyPatternString) {
		this.elabServiceBaseString = elabServiceBaseString;
		this.elabKeyBaseString = elabKeyBaseString;
		this.elabServicePattern = elabServicePatternString != null ? Pattern.compile(elabServicePatternString) : null;
		this.elabKeyPattern = elabKeyPatternString != null ? Pattern.compile(elabKeyPatternString) : null;
	}
	
	/**
	 * Gets the elab service group.
	 *
	 * @return the elab service group
	 */
	public String getElabServiceBaseString(){
		return elabServiceBaseString;
	}
	
	/**
	 * Gets the elab service pattern.
	 *
	 * @return the elab service pattern
	 */
	public Pattern getElabServicePattern(){
		return elabServicePattern;
	}

	/**
	 * Gets the elab key group.
	 *
	 * @return the elabKeyBaseString
	 */
	public String getElabKeyBaseString() {
		return elabKeyBaseString;
	}
	


	/**
	 * Gets the elab key pattern.
	 *
	 * @return the elab key pattern
	 */
	public Pattern getElabKeyPattern() {
		return elabKeyPattern;
	}
	
}
