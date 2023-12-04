/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNoteResponse;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Ricerca sintetica di una PrimaNota
 * 
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPrimeNoteService extends CheckedAccountBaseService<RicercaPrimeNote, RicercaPrimeNoteResponse> {
	
	@Autowired 
	private PrimaNotaDad primaNotaDad;
	
	private PrimaNota primaNota;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri di paginazione"));
		checkNotNull(req.getPrimaNota(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("prima nota"));
		primaNota = req.getPrimaNota();
		checkNotNull(primaNota.getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito prima nota"));
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaPrimeNoteResponse executeService(RicercaPrimeNote	 serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void init() {
		primaNotaDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		 ListaPaginata<PrimaNota> list = primaNotaDad.ricercaPrimeNote(
				 primaNota,
				 req.getAnnoPrimaNota(),
				 req.getTipoEvento(),
				 req.getElementoPianoDeiConti(),
				 req.getAnnoMovimento(),
				 req.getNumeroMoviemento(),
				 req.getNumeroSubmovimento(),
				 req.getParametriPaginazione());
		
		 res.setPrimeNote(list);
		
	}
	

}
