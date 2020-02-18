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
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.model.ConciliazionePerCapitolo;

/**
 * Aggiornamento della conciliazione per capitolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/10/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaConciliazionePerCapitoloService extends CheckedAccountBaseService<EliminaConciliazionePerCapitolo, EliminaConciliazionePerCapitoloResponse> {

	@Autowired
	private ConciliazioneDad conciliazioneDad;

	private ConciliazionePerCapitolo conciliazionePerCapitolo;

	@Override
	@Transactional
	public EliminaConciliazionePerCapitoloResponse executeService(EliminaConciliazionePerCapitolo serviceRequest) {
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
		conciliazioneDad.eliminaConciliazionePerCapitolo(conciliazionePerCapitolo);
		res.setConciliazionePerCapitolo(conciliazionePerCapitolo);
	}

}
