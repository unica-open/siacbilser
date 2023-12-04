/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AllineaProtocolloRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AllineaProtocolloRegistroIvaResponse;

/**
 * Wrapper async di {@link AllineaProtocolloRegistroIvaAsyncService}.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AllineaProtocolloRegistroIvaAsyncService extends AsyncBaseService<AllineaProtocolloRegistroIva, AllineaProtocolloRegistroIvaResponse, AsyncServiceRequestWrapper<AllineaProtocolloRegistroIva>,
		AllineaProtocolloRegistroIvaAsyncResponseHandler, AllineaProtocolloRegistroIvaService> {

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
		// Nessuna pre-elaborazione
	}
	
	@Override
	protected void startService() {
		final String methodName = "startService";
		try {
			elaborazioniManager.startElaborazione(AllineaProtocolloRegistroIvaAsyncService.class, "registroIva.uid:"+originalRequest.getRegistroIva().getUid());
		} catch (ElaborazioneAttivaException eae){
			String msg = "L'elaborazione per il Registro IVA e' gia' in corso. Attendere il termine dell'elaborazione. [uid: "+ originalRequest.getRegistroIva().getUid()+"]";
			log.error(methodName, msg, eae);
			throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg));
		}
		super.startService();
	}

	@Override
	protected void postStartService() {
		// Nessuna post-elaborazione
	}

}
