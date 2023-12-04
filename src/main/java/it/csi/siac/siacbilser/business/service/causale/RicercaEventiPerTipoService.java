/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * @author Domenico
 */
package it.csi.siac.siacbilser.business.service.causale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipoResponse;
import it.csi.siac.siacgenser.model.Evento;

/**
 * The Class RicercaEventiPerTipoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaEventiPerTipoService extends CheckedAccountBaseService<RicercaEventiPerTipo, RicercaEventiPerTipoResponse> {
	
	@Autowired
	private CausaleEPDad casualeEPDad;
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getTipoEvento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo evento"));
		
	}
	
	
	@Override
	protected void init() {
		super.init();
		casualeEPDad.setEnte(ente);
		casualeEPDad.setLoginOperazione(loginOperazione);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaEventiPerTipoResponse executeService(RicercaEventiPerTipo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		List<Evento> eventi = casualeEPDad.ricercaEventiPerTipo(req.getTipoEvento());
		res.setEventi(eventi);
		res.setCardinalitaComplessiva(eventi.size());
		
		
	}

}
