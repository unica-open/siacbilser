/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita.functionparamsadapter;

import it.csi.siac.siacbilser.business.service.consultazioneentita.FunctionEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaEntitaConsultabile;

/**
 * Ottiene un istanza del {@link FunctionParamsAdapter} associato ad una function.
 * 
 * @author Domenico
 * 
 * @see FunctionEntitaConsultabile
 *
 */
public final class FunctionParamsAdapterFactory {

	/** Costruttore vuoto privato per non permettere l'instanziazione */
	private FunctionParamsAdapterFactory() {
		// Previene l'instanziazione
	}
	
	/**
	 * Metodo di factory dell'istanza di {@link FunctionParamsAdapter}
	 * 
	 * @param fec
	 * @return l'istanza di {@link FunctionParamsAdapter}
	 */
	@SuppressWarnings("unchecked")
	public static <P extends ParametriRicercaEntitaConsultabile> FunctionParamsAdapter<P> newInstance(FunctionEntitaConsultabile fec) {
		String className = "it.csi.siac.siacbilser.business.service.consultazioneentita.functionparamsadapter."
							+ fec.getParametriRicercaEntitaConsultabileClass().getSimpleName().replaceFirst("ParametriRicerca", "")
							+ "FunctionParamsAdapter";
		try {
			Class<FunctionParamsAdapter<P>>  functionParamsFactoryClass = (Class<FunctionParamsAdapter<P>>) Class.forName(className);
			return (FunctionParamsAdapter<P>) functionParamsFactoryClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Eccezione istanziamento FunctionParamsAdapter associato alla function "+fec.name()+"->"+className,e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Impossibile accedere al costruttore vuoto del FunctionParamsAdapter "+className + " ["+fec.name()+"]",e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Impossibile trovare la classe del FunctionParamsAdapter:"+className + " ["+fec.name()+"]",e);
		}
	}
	
	
}
