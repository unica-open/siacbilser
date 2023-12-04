/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conciliazione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ConciliazioneDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Ricerca conti della conciliazione per titolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/11/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaContiConciliazionePerTitoloService extends CheckedAccountBaseService<RicercaContiConciliazionePerTitolo, RicercaContiConciliazionePerTitoloResponse> {

	@Autowired
	private ConciliazioneDad conciliazioneDad;

	@Override
	@Transactional(readOnly=true)
	public RicercaContiConciliazionePerTitoloResponse executeService(RicercaContiConciliazionePerTitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getConciliazionePerTitolo(), "conciliazione", true);
		checkNotNull(req.getConciliazionePerTitolo().getClasseDiConciliazione(), "classe di conciliazione", false);
		checkEntita(req.getConciliazionePerTitolo().getClassificatoreGerarchico(), "classificatore conciliazione", false);
	}
	
	@Override
	protected void init() {
		conciliazioneDad.setEnte(ente);
		conciliazioneDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		List<Conto> conti = conciliazioneDad.ricercaContiConciliazioniPerTitolo(req.getConciliazionePerTitolo());
		
		res.setConti(conti);
	}

}
