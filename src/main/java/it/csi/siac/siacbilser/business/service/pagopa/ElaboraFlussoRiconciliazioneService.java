/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.pagopa.frontend.webservice.msg.ElaboraFlussoRiconciliazione;
import it.csi.siac.pagopa.frontend.webservice.msg.ElaboraFlussoRiconciliazioneResponse;
import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.business.service.pagopa.helper.PagoPAFlussoHelper;
import it.csi.siac.siacbilser.integration.dad.PagoPADad;
import it.csi.siac.siacbilser.integration.entity.SiacTFilePagopa;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccommonser.util.misc.TimeoutValue;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFlussoRiconciliazioneService extends ExtendedBaseService<ElaboraFlussoRiconciliazione, ElaboraFlussoRiconciliazioneResponse> {

	@Autowired
	private PagoPADad pagoPADad;
	
	@Autowired
	private PagoPAFlussoHelper pagoPAFlussoHelper;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
	}
	
	@Override
	@Transactional(timeout = TimeoutValue.INTERVAL_8_HOUR)
	public ElaboraFlussoRiconciliazioneResponse executeService(ElaboraFlussoRiconciliazione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	@Override
	protected void init() {
		String loginOperazione = req.getRichiedente().getOperatore().getCodiceFiscale();
		Ente ente = req.getRichiedente().getAccount().getEnte();
		
		pagoPADad.setEnte(ente);
		pagoPADad.setLoginOperazione(loginOperazione);
		
		pagoPAFlussoHelper.init(ente, loginOperazione);
		
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		
		List<SiacTFilePagopa> elencoFilePagopa = pagoPADad.leggiElencoFlussiAcquisiti();
		
		for (SiacTFilePagopa siacTFilePagopa : elencoFilePagopa) {
			try {
				pagoPAFlussoHelper.elaboraFlusso(siacTFilePagopa);
			} catch (BusinessException be) {
				res.addErrore(be.getErrore());
			} catch (Exception e) {
				res.addErroreDiSistema(e);
			}
		}		
		
		res.setEsito(Esito.SUCCESSO);
	}


}

