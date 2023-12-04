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
import it.csi.siac.pagopa.model.Elaborazione;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PagoPADad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;


/**
 * Ricerca sintetica di una fattura elettronica
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaElaborazioniService extends CheckedAccountBaseService<RicercaElaborazioni, RicercaElaborazioniResponse> {
	
	private Elaborazione  elaborazione;
	
	@Autowired
	private PagoPADad pagoPADad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getElaborazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elaborazione"));
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		
		elaborazione = req.getElaborazione();
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
	public RicercaElaborazioniResponse executeService(RicercaElaborazioni serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		ListaPaginata<Elaborazione> listaElaborazioni = pagoPADad.ricercaElaborazioni(elaborazione, req.getDataEmissioneDa(), req.getDataEmissioneA(), req.getDataElaborazioneFlussoDa(), req.getDataElaborazioneFlussoA(), req.getEsitoElaborazioneFlusso(), req.getParametriPaginazione());
		if(listaElaborazioni.isEmpty()){
			//SIAC-7556(epic)/SIAC-7911/punto 4: richiesta sostituzione del msg per ricerca che non produce risultati
			res.addErrore(ErroreBil.PROVVISORIO_FLUSSO_NON_TROVATO.getErrore());
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		res.setElaborazioni(listaElaborazioni);
	}

}

