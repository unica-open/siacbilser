/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita.functionparamsadapter;

import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaProvvedimentoConsultabile;
import it.csi.siac.siaccorser.model.Ente;

/**
 * {@link FunctionParamsAdapter} per la ricerca dei soggetti.
 * 
 * @author Domenico
 *
 */
public class ProvvedimentoConsultabileFunctionParamsAdapter extends BaseImplFunctionParamsAdapter<ParametriRicercaProvvedimentoConsultabile>  {


	@Override
	public Object[] toFunctionParamsArray(ParametriRicercaProvvedimentoConsultabile pr, Ente ente) {
		
		return toArray(
				integer(ente), 
				varchar(pr.getAnnoAtto()),
				integer(pr.getNumeroAtto()),				 
				integer(pr.getTipoAtto()),
				integer(pr.getStrutturaAmministrativoContabile())			
				);
		

	}

}
