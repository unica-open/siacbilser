/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseAggiornaAnnullaRipartizioneMutuoServiceRequest;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseAggiornaAnnullaRipartizioneMutuoServiceResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDFaseOperativaEnum;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

public abstract class BaseAggiornaAnnullaRipartizioneMutuoService<BAARMSREQ extends BaseAggiornaAnnullaRipartizioneMutuoServiceRequest, BAARRES extends BaseAggiornaAnnullaRipartizioneMutuoServiceResponse> 
	extends BaseMutuoService<BAARMSREQ, BAARRES> {

	private @Autowired BilancioDad bilancioDad;
	
	@Override
	@Transactional
	public BAARRES executeService(BAARMSREQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkNotNull(req.getAnnoBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
	}
	
	@Override
	protected void init() {
		super.init();
		bilancioDad.setEnteEntity(ente);
	}

	@Override
	protected void execute() {
		super.execute();

		checkStato();
		checkFaseOperativaBilancio();
		
		mutuo.setUid(mutuoCorrente.getUid());
		mutuo.setStatoMutuo(mutuoCorrente.getStatoMutuo());
		
	}
	
	private void checkStato() {
		checkBusinessCondition(!StatoMutuo.Annullato.equals(mutuoCorrente.getStatoMutuo()), 
				ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il mutuo", mutuoCorrente.getStatoMutuo().getDescrizione()));
	}

	private void checkFaseOperativaBilancio() {

		SiacDFaseOperativaEnum siacDFaseOperativaEnum = bilancioDad.getSiacDFaseOperativaEnum(req.getAnnoBilancio());
		
		checkBusinessCondition(SiacDFaseOperativaEnum.EsercizioProvvisorio.equals(siacDFaseOperativaEnum) || SiacDFaseOperativaEnum.Gestione.equals(siacDFaseOperativaEnum)
				, ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il bilancio", siacDFaseOperativaEnum.getFaseBilancio().getDescrizione()));
		
	}

}
