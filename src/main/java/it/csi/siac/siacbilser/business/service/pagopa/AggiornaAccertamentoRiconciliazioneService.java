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

import it.csi.siac.pagopa.frontend.webservice.msg.AggiornaAccertamentoModaleResponse;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniDoc;
import it.csi.siac.pagopa.model.RiconciliazioneDoc;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PagoPADad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/*
 * SIAC-8046 Task 2.2-2.3 CM 13/04/2021 - Aggiorna Accertamento Riconciliazione
 * 
 * Aggiorna anno e numero dell'accertamento riferito alla riconciliazione
 * 
 * */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAccertamentoRiconciliazioneService extends CheckedAccountBaseService<RicercaRiconciliazioniDoc, AggiornaAccertamentoModaleResponse>{

	private RiconciliazioneDoc  riconciliazioneDoc;
	
	@Autowired
	private PagoPADad pagoPADad;
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRiconciliazioneDoc(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("riconcilizioneDoc"));
		
		riconciliazioneDoc = req.getRiconciliazioneDoc();
	}
	
	@Override
	protected void init() {
		pagoPADad.setEnte(ente);
	}
	

	@Override
	@Transactional(readOnly=true)
	public AggiornaAccertamentoModaleResponse executeService(RicercaRiconciliazioniDoc serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	@Override
	protected void execute() {
		
		List<Object[]> resAggiornaAcc = pagoPADad.aggiornaAccertamentoRicModalePagoPa(ente.getUid(), req.getAnnoBilancio(), riconciliazioneDoc);
					
		for (Object[] o : resAggiornaAcc) {		
			
			String resFunction = (String) o[0];
			String[] parts = resFunction.split("-");
			res.setCodice(Integer.parseInt(parts[0]));
			res.setDescrizione(parts[1]);
		}

	}
}
