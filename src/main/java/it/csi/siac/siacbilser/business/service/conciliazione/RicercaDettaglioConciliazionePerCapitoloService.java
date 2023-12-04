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
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.model.ConciliazionePerCapitolo;

/**
 * Ricerca di dettaglio della conciliazione per titolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/10/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioConciliazionePerCapitoloService extends CheckedAccountBaseService<RicercaDettaglioConciliazionePerCapitolo, RicercaDettaglioConciliazionePerCapitoloResponse> {

	@Autowired
	private ConciliazioneDad conciliazioneDad;
	private ConciliazionePerCapitolo conciliazionePerCapitolo;

	@Override
	@Transactional(readOnly = true)
	public RicercaDettaglioConciliazionePerCapitoloResponse executeService(RicercaDettaglioConciliazionePerCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getConciliazionePerCapitolo(), "conciliazione", true);
		conciliazionePerCapitolo = req.getConciliazionePerCapitolo();
	}
	
	@Override
	protected void init() {
		conciliazioneDad.setEnte(ente);
		conciliazioneDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		conciliazionePerCapitolo = conciliazioneDad.findConciliazionePerCapitoloByUid(conciliazionePerCapitolo.getUid());
		res.setConciliazionePerCapitolo(conciliazionePerCapitolo);
	}

}
