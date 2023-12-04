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

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidazioneMassivaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidazioneMassivaPrimaNotaIntegrataResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ValidazioneMassivaPrimaNotaIntegrataAsyncService extends AsyncBaseService<ValidazioneMassivaPrimaNotaIntegrata,
																	   ValidazioneMassivaPrimaNotaIntegrataResponse,
																	   AsyncServiceRequestWrapper<ValidazioneMassivaPrimaNotaIntegrata>,
																	   ValidazioneMassivaPrimaNotaIntegrataAsyncResponseHandler,
																	   ValidazioneMassivaPrimaNotaIntegrataService> {
	
	@Autowired
	private BilancioDad bilancioDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(originalRequest.getRicercaSinteticaPrimaNotaIntegrataValidabile(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Request di ricerca"));
		
		checkEntita(originalRequest.getRicercaSinteticaPrimaNotaIntegrataValidabile().getBilancio(), "bilancio", false);
		checkNotNull(originalRequest.getRicercaSinteticaPrimaNotaIntegrataValidabile().getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri di paginazione"), false);
		checkNotNull(originalRequest.getRicercaSinteticaPrimaNotaIntegrataValidabile().getPrimaNota(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("prima nota"), false);
	}
	
	@Override
	@Transactional
	public AsyncServiceResponse executeService(AsyncServiceRequestWrapper<ValidazioneMassivaPrimaNotaIntegrata> serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void preStartService() {
		final String methodName = "preStartService";
		log.debug(methodName, "Operazione asincrona in procinto di avviarsi...");
		checkFaseOperativaCompatibile();
		
		// TODO Nothing else to do?
	}
	
	/**
	 * Controlla che la fase operativa del bilancio sia compatibile con l'operazione
	 */
	private void checkFaseOperativaCompatibile() {
		Bilancio bilancio = bilancioDad.getBilancioByUid(originalRequest.getRicercaSinteticaPrimaNotaIntegrataValidabile().getBilancio().getUid());
		if(bilancio == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid " + originalRequest.getRicercaSinteticaPrimaNotaIntegrataValidabile().getBilancio().getUid()));
		}
		if(bilancio.getFaseEStatoAttualeBilancio() == null || bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio() == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Fase di bilancio", "corrispondente al bilancio con uid " + bilancio.getUid()));
		}
		FaseBilancio faseBilancio = bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio();
		// Controllo che il bilancio NON sia in fase CHIUSO, PLURIENNALE, PREVISIONE
		if(FaseBilancio.CHIUSO.equals(faseBilancio) || FaseBilancio.PLURIENNALE.equals(faseBilancio) || FaseBilancio.PREVISIONE.equals(faseBilancio)) {
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Bilancio", faseBilancio.name()).getTesto());
		}
	}
	
	@Override
	protected void postStartService() {
		final String methodName = "postStartService";
		log.debug(methodName, "Operazione asincrona avviata");
		// TODO Nothing to do?
	}

}
