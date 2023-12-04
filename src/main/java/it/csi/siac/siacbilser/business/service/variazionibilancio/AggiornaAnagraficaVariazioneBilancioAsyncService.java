/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciOperazioneAsinc;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StatoOperazioneAsincronaEnum;

/**
 * Wrapper async di {@link AggiornaAnagraficaVariazioneBilancioService}.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAnagraficaVariazioneBilancioAsyncService extends AsyncBaseService<AggiornaAnagraficaVariazioneBilancio,
																						AggiornaAnagraficaVariazioneBilancioResponse,
																						AsyncServiceRequestWrapper<AggiornaAnagraficaVariazioneBilancio>,
																						AggiornaAnagraficaVariazioneBilancioAsyncResponseHandler,
																						AggiornaAnagraficaVariazioneBilancioService> {

	@Autowired
	private ElaborazioniManager elaborazioniManager;

	@Override
	protected void init() {
		super.init();
		elaborazioniManager.init(req.getRichiedente().getAccount().getEnte(), req.getRichiedente().getOperatore().getCodiceFiscale());
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		String methodName = "checkServiceParam";
		service.checkServiceParam();
		log.debug(methodName, "Errori riscontrati: "+ service.getServiceResponse().getErrori());
		res.addErrori(service.getServiceResponse().getErrori());
	}
	
	@Override
	protected void preStartService() {
		// Nothing to do
	}
	
	@Override
	protected void startService() {
		final String methodName = "startService";
		try {
			ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(originalRequest.getVariazioneImportoCapitolo().getUid(), this.getClass());
			
			ElabKeys aggiornaVariazioneKeySelector = ElabKeys.AGGIORNA_VARIAZIONE;
			elaborazioniManager.startElaborazione(eakh.creaElabServiceFromPattern(aggiornaVariazioneKeySelector), eakh.creaElabKeyFromPattern(aggiornaVariazioneKeySelector));
		} catch (ElaborazioneAttivaException eae){
			String msg = "L'elaborazione per la variazione " + getDescVariazioneFromOriginalReq() + " e' gia' in corso. Attendere il termine dell'elaborazione. [uid: "+ originalRequest.getVariazioneImportoCapitolo().getUid()+"]";
			log.error(methodName, msg, eae);
			//SIAC-8726
			inserisciDettaglioOperazioneAsinc("ELABORAZIONE_CONCORRENTE", msg, Esito.FALLIMENTO);
			aggiornaOperazioneAsinc(StatoOperazioneAsincronaEnum.STATO_OPASINC_ERRORE);
			throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg));
		}
		//SIAC-8263
		//sono sicura di avere i dati perche' sono controllati dal checkServiceParam
		inserisciDettaglioOperazioneAsinc("DATI_VARIAZIONE", 
				"variazione " + originalRequest.getVariazioneImportoCapitolo().getNumero() + " - " + StringUtils.defaultString(originalRequest.getVariazioneImportoCapitolo().getDescrizione()), Esito.SUCCESSO);
		super.startService();
	}
	
	private String getDescVariazioneFromOriginalReq() {
		if(originalRequest == null || originalRequest.getVariazioneImportoCapitolo() == null) {
			return "N.D.";
		}
		return originalRequest.getVariazioneImportoCapitolo().getNumero() + " - " + StringUtils.defaultString(originalRequest.getVariazioneImportoCapitolo().getDescrizione());
	}

	@Override
	protected void postStartService() {
		
	}
	
	@Override
	protected InserisciOperazioneAsinc popolaRequestOperazioneAsincrona() {
		InserisciOperazioneAsinc req = super.popolaRequestOperazioneAsincrona();
		if(Boolean.TRUE.equals(originalRequest.getEvolviProcesso())) {
			req.setUidVariazioneImportoCapitolo(originalRequest.getVariazioneImportoCapitolo().getUid());
		}
		return req;
	}

}
