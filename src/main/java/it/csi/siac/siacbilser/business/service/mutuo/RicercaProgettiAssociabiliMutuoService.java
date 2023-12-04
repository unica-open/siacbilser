/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaProgettiAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaProgettiAssociabiliMutuoResponse;
import it.csi.siac.siacbilser.integration.dad.MutuoDad;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.model.ProgettoModelDetail;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaProgettiAssociabiliMutuoService extends CheckedAccountBaseService<RicercaProgettiAssociabiliMutuo, RicercaProgettiAssociabiliMutuoResponse> {

	protected @Autowired MutuoDad mutuoDad;
	
	@Override
	@Transactional(readOnly = true)
	public RicercaProgettiAssociabiliMutuoResponse executeService(RicercaProgettiAssociabiliMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getProgetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progetto"));
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametriPaginazione"));
	}
	
	@Override
	protected void init() {
		mutuoDad.setEnte(ente);
	}	
	
	@Override
	protected void execute() {
		
		ListaPaginata<Progetto> listaProgetti = mutuoDad.ricercaProgettiAssociabiliMutuo(req.getProgetto(), req.getParametriPaginazione(), ProgettoModelDetail.Dettaglio);
		res.setProgetti(listaProgetti);
		
		res.setCardinalitaComplessiva(listaProgetti.size());
	}
}
