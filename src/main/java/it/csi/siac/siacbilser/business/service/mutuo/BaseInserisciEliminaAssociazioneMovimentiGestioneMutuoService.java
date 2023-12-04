/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseInserisciEliminaAssociazioneMovimentiGestioneMutuoServiceRequest;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseInserisciEliminaAssociazioneMovimentiGestioneMutuoServiceResponse;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

public abstract class BaseInserisciEliminaAssociazioneMovimentiGestioneMutuoService<BIEAMGMSREQ extends BaseInserisciEliminaAssociazioneMovimentiGestioneMutuoServiceRequest, BIEAMGMSRES extends BaseInserisciEliminaAssociazioneMovimentiGestioneMutuoServiceResponse> 
	extends BaseMutuoService<BIEAMGMSREQ, BIEAMGMSRES> {

	@Override
	@Transactional
	public BIEAMGMSRES executeService(BIEAMGMSREQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkCondition(CollectionUtil.isNotEmpty(req.getElencoIdMovimentiGestione()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco movimenti gestione"));
	}


	@Override
	protected void execute() {
		super.execute();
	}

	@Override
	protected void checkMutuo() {
		super.checkMutuo();
		checkStatoMutuo();
	}
	
	private void checkStatoMutuo() {
		checkBusinessCondition(StatoMutuo.Definitivo.equals(mutuoCorrente.getStatoMutuo()) || StatoMutuo.Predefinitivo.equals(mutuoCorrente.getStatoMutuo()), ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il mutuo", mutuoCorrente.getStatoMutuo().getDescrizione()));
	}
}
