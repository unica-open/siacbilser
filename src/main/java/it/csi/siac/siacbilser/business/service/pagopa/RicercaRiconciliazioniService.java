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

import it.csi.siac.pagopa.frontend.webservice.msg.RicercaElaborazioni;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaElaborazioniResponse;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioni;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniResponse;
import it.csi.siac.pagopa.model.Elaborazione;
import it.csi.siac.pagopa.model.Riconciliazione;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PagoPADad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronicaResponse;
import it.csi.siac.sirfelser.model.FatturaFEL;

/**
 * Ricerca sintetica di una fattura elettronica
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaRiconciliazioniService extends CheckedAccountBaseService<RicercaRiconciliazioni, RicercaRiconciliazioniResponse> {
	
	private Riconciliazione  riconciliazione;
	
	@Autowired
	private PagoPADad pagoPADad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRiconciliazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("riconcilizione"));
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		
		riconciliazione = req.getRiconciliazione();
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
	public RicercaRiconciliazioniResponse executeService(RicercaRiconciliazioni serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		ListaPaginata<Riconciliazione> listaRiconciliazioni = pagoPADad.ricercaRiconciliazioni(riconciliazione, req.getParametriPaginazione());
		//ListaPaginata<Riconciliazione> listaRiconciliazioni = pagoPADad.ricercaRiconciliazioniConDettagli(riconciliazione, req.getParametriPaginazione());
		if(listaRiconciliazioni.isEmpty()){
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		res.setRiconciliazioni(listaRiconciliazioni);
	}

}

