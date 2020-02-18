/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siacfinser.ReintroitoUtils;
import it.csi.siac.siacfinser.business.service.AsyncBaseServiceFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.ReintroitoOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.ReintroitoOrdinativoPagamentoResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReintroitoOrdinativoPagamentoAsyncService extends AsyncBaseServiceFin<ReintroitoOrdinativoPagamento,
	ReintroitoOrdinativoPagamentoResponse,
	AsyncServiceRequestWrapper<ReintroitoOrdinativoPagamento>,
	ReintroitoOrdinativoPagamentoAsyncResponseHandler,
	ReintroitoOrdinativoPagamentoService> {

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
	}
	
	@Override
	protected void startService() {
		final String methodName = "startService";
		String elabKey = ReintroitoUtils.buildElabKeyReintroito(originalRequest.getEnte(), originalRequest.getOrdinativoPagamento());
		try {	
			elaborazioniManager.startElaborazione(ReintroitoOrdinativoPagamentoAsyncService.class, elabKey);
		} catch (ElaborazioneAttivaException eae){
			String ordinativo = originalRequest.getOrdinativoPagamento().getAnno() +"/"
					+ originalRequest.getOrdinativoPagamento().getNumero();
			String msg = "L'elaborazione per l'Ordinativo di pagamento e' gia' in corso. Attendere il termine dell'elaborazione. [ordinativo: "+ ordinativo+"]";
			log.error(methodName, msg, eae);
			throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg));
		}
		super.startService();
	}
	
	@Override
	protected void postStartService() {
	}

}
