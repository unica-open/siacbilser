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
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.model.ConciliazionePerTitolo;

/**
 * Ricerca di sintetica della conciliazione per titolo.
 * 
 * @author Valentina
 * @version 1.0.0 - 28/10/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaConciliazionePerTitoloService extends CheckedAccountBaseService<RicercaSinteticaConciliazionePerTitolo, RicercaSinteticaConciliazionePerTitoloResponse> {

	@Autowired
	private ConciliazioneDad conciliazioneDad;
	private ConciliazionePerTitolo conciliazionePerTitolo;

	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaConciliazionePerTitoloResponse executeService(RicercaSinteticaConciliazionePerTitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getParametriPaginazione(), "parametri di paginazione");
		checkNotNull(req.getConciliazionePerTitolo(), "conciliazione", true);
		conciliazionePerTitolo = req.getConciliazionePerTitolo();
		checkNotNull(conciliazionePerTitolo.getClasseDiConciliazione(), "classe di conciliazione", true);
	}
	
	@Override
	protected void init() {
		conciliazioneDad.setEnte(ente);
		conciliazioneDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		ListaPaginata<ConciliazionePerTitolo> lista = conciliazioneDad.ricercaSinteticaConciliazioniPerTitolo(conciliazionePerTitolo, req.getTitolo(), req.getTipologia(), req.getParametriPaginazione());
		
		res.setConciliazioni(lista);
		if(lista == null || lista.isEmpty()){
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
		}
	}

}
