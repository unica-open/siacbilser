/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conciliazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ConciliazioneDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.model.ConciliazionePerCapitolo;

/**
 * Ricerca di sintetica della conciliazione per capitolo.
 * 
 * @author Valentina
 * @version 1.0.0 - 28/10/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaConciliazionePerCapitoloService extends CheckedAccountBaseService<RicercaSinteticaConciliazionePerCapitolo, RicercaSinteticaConciliazionePerCapitoloResponse> {

	@Autowired
	private ConciliazioneDad conciliazioneDad;
	private ConciliazionePerCapitolo conciliazionePerCapitolo;

	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaConciliazionePerCapitoloResponse executeService(RicercaSinteticaConciliazionePerCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getParametriPaginazione(), "parametri di paginazione");
		checkNotNull(req.getConciliazionePerCapitolo(), "conciliazione", true);
		conciliazionePerCapitolo = req.getConciliazionePerCapitolo();
		checkEntita(conciliazionePerCapitolo.getCapitolo(), "capitolo");
	}
	
	@Override
	protected void init() {
		conciliazioneDad.setEnte(ente);
		conciliazioneDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		ListaPaginata<ConciliazionePerCapitolo> lista = conciliazioneDad.ricercaSinteticaConciliazioniPerCapitolo(conciliazionePerCapitolo, req.getParametriPaginazione());
		
		res.setConciliazioni(lista);
		if(lista == null || lista.isEmpty()){
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
		}
	}

}
