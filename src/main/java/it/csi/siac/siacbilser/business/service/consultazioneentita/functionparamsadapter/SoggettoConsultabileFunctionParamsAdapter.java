/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita.functionparamsadapter;

import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaSoggettoConsultabile;
import it.csi.siac.siaccorser.model.Ente;

/**
 * {@link FunctionParamsAdapter} per la ricerca dei soggetti.
 * 
 * @author Domenico
 *
 */
public class SoggettoConsultabileFunctionParamsAdapter extends BaseImplFunctionParamsAdapter<ParametriRicercaSoggettoConsultabile>  {


	@Override
	public Object[] toFunctionParamsArray(ParametriRicercaSoggettoConsultabile pr, Ente ente) {
		
		return toArray(
				integer(ente), 
				varchar(pr.getCodiceSoggetto()),
				varchar(pr.getDenominazione()),				 
				varchar(pr.getCodiceFiscale()),
				varchar(pr.getPartitaIVA())				
				);
		

	}

}
