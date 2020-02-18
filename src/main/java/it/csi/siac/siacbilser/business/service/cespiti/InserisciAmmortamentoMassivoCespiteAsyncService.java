/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAmmortamentoMassivoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAmmortamentoMassivoCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciAmmortamentoMassivoCespiteAsyncService extends AsyncBaseService<InserisciAmmortamentoMassivoCespite,
                                                                       InserisciAmmortamentoMassivoCespiteResponse,
																	   AsyncServiceRequestWrapper<InserisciAmmortamentoMassivoCespite>,
																	   InserisciAmmortamentoMassivoCespiteAysncResponseHandler,
																	   InserisciAmmortamentoMassivoCespiteService> {
	
	@Autowired
	private CespiteDad cespiteDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(originalRequest.getUltimoAnnoAmmortamento() != null && !originalRequest.getUltimoAnnoAmmortamento().equals(Integer.valueOf(0)), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ultimo anno ammortamento"));
		checkCondition((originalRequest.getUidsCespiti() != null && !originalRequest.getUidsCespiti().isEmpty())
				|| originalRequest.getRequestRicerca() != null				
				, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ultimo anno ammortamento"));
	}
	
	@Override
	@Transactional
	public AsyncServiceResponse executeService(AsyncServiceRequestWrapper<InserisciAmmortamentoMassivoCespite> serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(req.getRichiedente().getAccount().getEnte());
	}
	
	@Override
	protected void preStartService() {
		if(originalRequest.getUidsCespiti() == null || originalRequest.getUidsCespiti().isEmpty()) {
			caricaCespitiPianoAmmortamentoMancante(originalRequest.getRequestRicerca());
		}
		
	}

	
	private void caricaCespitiPianoAmmortamentoMancante(RicercaSinteticaCespite ricercaSinteticaCespite) {
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setNumeroPagina(0);
		pp.setElementiPerPagina(Integer.MAX_VALUE);
		
		List<Integer> uidsDaAmmortare = cespiteDad.caricaUidCespitiDaAmmortare(
				ricercaSinteticaCespite.getCespite(),
				ricercaSinteticaCespite.getTipoBeneCespite(), 
				ricercaSinteticaCespite.getClassificazioneGiuridicaCespite(),
				ricercaSinteticaCespite.getDismissioneCespite(), 
				ricercaSinteticaCespite.getNumeroInventarioDa(), 
				ricercaSinteticaCespite.getNumeroInventarioA(), 
				ricercaSinteticaCespite.getEscludiCespitiCollegatiADismissione(),
				ricercaSinteticaCespite.getConPianoAmmortamentoCompleto(),
				ricercaSinteticaCespite.getMassimoAnnoAmmortato(),
				Utility.ultimoGiornoDellAnno(originalRequest.getAnnoBilancio())
				);
		originalRequest.setUidsCespiti(uidsDaAmmortare);
	}

	@Override
	protected void postStartService() {
		final String methodName = "preStartService";
		log.debug(methodName, "Operazione asincrona avviata");
	}
	
	@Override
	protected boolean mayRequireElaborationOnDedicatedQueue() {
		Long threshold = getThreshold();
	    Long numeroCespiti = Long.valueOf(originalRequest.getUidsCespiti().size());
		return threshold != null && numeroCespiti != null && numeroCespiti.compareTo(threshold) > 0;
	}
}
