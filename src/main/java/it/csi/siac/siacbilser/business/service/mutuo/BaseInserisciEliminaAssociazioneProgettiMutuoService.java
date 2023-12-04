/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseInserisciEliminaAssociazioneProgettiMutuoServiceRequest;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseInserisciEliminaAssociazioneProgettiMutuoServiceResponse;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

public abstract class BaseInserisciEliminaAssociazioneProgettiMutuoService<BIEAPMSREQ extends BaseInserisciEliminaAssociazioneProgettiMutuoServiceRequest, BIEAPMSRES extends BaseInserisciEliminaAssociazioneProgettiMutuoServiceResponse> 
	extends BaseMutuoService<BIEAPMSREQ, BIEAPMSRES> {

	@Override
	@Transactional
	public BIEAPMSRES executeService(BIEAPMSREQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkCondition(CollectionUtil.isNotEmpty(req.getElencoIdProgetti()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco progetti"));
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
		checkBusinessCondition(!StatoMutuo.Annullato.equals(mutuoCorrente.getStatoMutuo()), ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il mutuo", mutuoCorrente.getStatoMutuo().getDescrizione()));
	}
}
