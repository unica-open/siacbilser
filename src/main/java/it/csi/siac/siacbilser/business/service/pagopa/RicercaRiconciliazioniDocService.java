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

import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniDoc;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniDocResponse;
import it.csi.siac.pagopa.model.RiconciliazioneDoc;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PagoPADad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;

/**
 * Ricerca sintetica di una fattura elettronica
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaRiconciliazioniDocService extends CheckedAccountBaseService<RicercaRiconciliazioniDoc, RicercaRiconciliazioniDocResponse> {
	
	private RiconciliazioneDoc  riconciliazioneDoc;
	
	@Autowired
	private PagoPADad pagoPADad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRiconciliazioneDoc(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("riconcilizioneDoc"));
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		
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
	public RicercaRiconciliazioniDocResponse executeService(RicercaRiconciliazioniDoc serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		ListaPaginata<RiconciliazioneDoc> listaRiconciliazioniDoc = new ListaPaginataImpl<RiconciliazioneDoc>();
		if(riconciliazioneDoc.getElaborazione() != null && riconciliazioneDoc.getElaborazione().getUid() > 0){
			/* Se abbiamo l uid della riconciliazione 
			 * dobbiamo chiamare il servizio
			 * per il recupero degli errori
			 */
			if(riconciliazioneDoc.getRiconciliazione()!= null &&
					riconciliazioneDoc.getRiconciliazione().getRicId()!= null){
				listaRiconciliazioniDoc = pagoPADad.ricercaRiconciliazioniDoc(riconciliazioneDoc, req.getParametriPaginazione());
			}else{
				listaRiconciliazioniDoc = pagoPADad.ricercaRiconciliazioniConDettagli(riconciliazioneDoc, req.getParametriPaginazione());
			}
		}
		
		if(listaRiconciliazioniDoc.isEmpty()){
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		res.setRiconciliazioniDoc(listaRiconciliazioniDoc);
	}

}

