/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conciliazione;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerCapitoloResponse;
import it.csi.siac.siacgenser.model.ConciliazionePerCapitolo;

/**
 * Inserimento di multiple conciliazioni per capitolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/10/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceConciliazioniPerCapitoloService extends CheckedAccountBaseService<InserisceConciliazioniPerCapitolo, InserisceConciliazioniPerCapitoloResponse> {

	@Override
	@Transactional
	public InserisceConciliazioniPerCapitoloResponse executeService(InserisceConciliazioniPerCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio", false);
		checkNotNull(req.getConciliazioniPerCapitolo(), "conciliazioni", true);
		checkCondition(!req.getConciliazioniPerCapitolo().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("conciliazioni"));
		
		int i = 1;
		for(ConciliazionePerCapitolo cpc : req.getConciliazioniPerCapitolo()) {
			if(cpc == null) {
				continue;
			}
			checkEntita(cpc.getCapitolo(), "capitolo conciliazione " + i, false);
			checkEntita(cpc.getConto(), "conto conciliazione " + i, false);
			checkEntita(cpc.getEnte(), "ente conciliazione " + i, false);
			checkNotNull(cpc.getClasseDiConciliazione(), "classe di conciliazione " + i, false);
			
			i++;
		}
	}
	
	@Override
	protected void execute() {
		for(ConciliazionePerCapitolo conciliazionePerCapitolo : req.getConciliazioniPerCapitolo()) {
			if(conciliazionePerCapitolo != null) {
				elaboraConciliazionePerCapitolo(conciliazionePerCapitolo);
			}
		}
	}

	private void elaboraConciliazionePerCapitolo(ConciliazionePerCapitolo conciliazionePerCapitolo) {
		InserisceConciliazionePerCapitolo reqICPC = new InserisceConciliazionePerCapitolo();
		
		reqICPC.setBilancio(req.getBilancio());
		reqICPC.setDataOra(new Date());
		reqICPC.setRichiedente(req.getRichiedente());
		reqICPC.setConciliazionePerCapitolo(conciliazionePerCapitolo);
		
		InserisceConciliazionePerCapitoloResponse resICPC = serviceExecutor.executeServiceSuccess(InserisceConciliazionePerCapitoloService.class, reqICPC);
		ConciliazionePerCapitolo conciliazioneResponse = resICPC.getConciliazionePerCapitolo();
		res.getConciliazioniPerCapitolo().add(conciliazioneResponse);
	}

}
