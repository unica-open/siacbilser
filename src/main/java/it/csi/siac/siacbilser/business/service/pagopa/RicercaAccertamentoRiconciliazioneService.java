/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.pagopa.frontend.webservice.msg.RicercaAccertamentoResponse;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniDoc;
import it.csi.siac.pagopa.model.RiconciliazioneDoc;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PagoPADad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


/*
 * SIAC-8046 Task 2.2 CM 31/03/2021 - Ricerca Accertamento Riconciliazione
 * 
 * Utile per controllo su esistenza e validità dell'accertamento 
 * in modale di "modifica accertamento" per ricerca/consulta pagoPA
 * 
 * */

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAccertamentoRiconciliazioneService extends CheckedAccountBaseService<RicercaRiconciliazioniDoc, RicercaAccertamentoResponse>{

	private RiconciliazioneDoc  riconciliazioneDoc;
	
	@Autowired
	private PagoPADad pagoPADad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRiconciliazioneDoc(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("riconcilizioneDoc"));
		
		riconciliazioneDoc = req.getRiconciliazioneDoc();
	}
	
	@Override
	protected void init() {
		pagoPADad.setEnte(ente);
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaAccertamentoResponse executeService(RicercaRiconciliazioniDoc serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		//si devono passare altri parametri - enteProprietario, statoCode, tipoCode del movimento
		boolean resIsAccertamentoExist = pagoPADad.checkAccertamentoExist(ente.getUid(), req.getAnnoBilancio(), riconciliazioneDoc);
		
		res.setAccertamentoExist(resIsAccertamentoExist);
	}

}
