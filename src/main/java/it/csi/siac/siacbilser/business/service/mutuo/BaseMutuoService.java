/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseMutuoServiceRequest;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseMutuoServiceResponse;
import it.csi.siac.siacbilser.integration.dad.MutuoDad;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

public abstract class BaseMutuoService<BMSREQ extends BaseMutuoServiceRequest, BMSRES extends BaseMutuoServiceResponse> 
	extends CheckedAccountBaseService<BMSREQ, BMSRES> {
	
	protected Mutuo mutuo;
	protected Mutuo mutuoCorrente; 
	protected @Autowired MutuoDad mutuoDad;
	
	@Override
	@Transactional
	public BMSRES executeService(BMSREQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		mutuo = req.getMutuo();
		checkNotNull(mutuo, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("mutuo"));
	}
	
	@Override
	protected void init() {
		mutuoDad.setEnte(ente);
		mutuoDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		checkMutuo();
		
	}
	
	protected void checkMutuo() {
		mutuoCorrente = mutuoDad.ricercaMutuo(mutuo, MutuoModelDetail.Stato, MutuoModelDetail.PeriodoRimborso);
		checkBusinessCondition(mutuoCorrente != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("mutuo"));
		checkBusinessCondition(mutuoCorrente.isEntitaValida(), ErroreCore.VALORE_NON_VALIDO.getErrore(mutuo.getNumero()));
	}
}
