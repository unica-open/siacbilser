/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita.functionparamsadapter;

import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaEntitaConsultabile;
import it.csi.siac.siaccorser.model.Ente;

/**
 * Adapter che consente di ottenere i parametri di una function a partire da un estensione di {@link ParametriRicercaEntitaConsultabile}.
 * 
 * @author Domenico
 *
 * @param <P>
 */
public interface FunctionParamsAdapter<P extends ParametriRicercaEntitaConsultabile> {

	/**
	 * Ottiene un array ordinato dei parametri della function associata all'oggetto {@link ParametriRicercaEntitaConsultabile}
	 *  
	 * @param pr paretri ricerca della function
	 * @param ente
	 * @return array ordinato dei parametri di una function.
	 */
	public Object[] toFunctionParamsArray(P pr, Ente ente);
	
}
