/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.attodilegge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLeggeResponse;
import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacattser.model.ric.RicercaLeggi;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AttoDiLeggeDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceAttoDiLeggeService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceAttoDiLeggeService extends CheckedAccountBaseService<InserisceAttoDiLegge, InserisceAttoDiLeggeResponse> {
	
	/** The atto di legge dad. */
	@Autowired
	AttoDiLeggeDad attoDiLeggeDad = null;
	
	/** The ricerca atto di legge service. */
	@Autowired
	RicercaAttoDiLeggeService ricercaAttoDiLeggeService = null;

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		attoDiLeggeDad.setLoginOperazione(loginOperazione);
		attoDiLeggeDad.setEnte(req.getEnte());
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"),true);
		
		checkNotNull(req.getAttoDiLegge(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto di legge"),true);
		checkNotNull(req.getAttoDiLegge().getAnno(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno atto di legge"));
		checkNotNull(req.getAttoDiLegge().getNumero(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero atto di legge"));
		
		checkNotNull(req.getAttoDiLegge().getTipoAtto(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo atto di legge"));
		checkNotNull(req.getAttoDiLegge().getTipoAtto().getUid(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo atto di legge"));		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public InserisceAttoDiLeggeResponse executeService(InserisceAttoDiLegge serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		

		if (esisteAttoDiLegge()) {
			//Se l'entità esiste viene ritornato codice di errore ENTITA_PRESENTE
			
			AttoDiLegge atto = req.getAttoDiLegge();			
			
			res.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("inserimento", String.format("Atto di Legge: %s/%s", atto.getAnno(), atto.getNumero())));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		
		AttoDiLegge cup = attoDiLeggeDad.create(req.getAttoDiLegge(), req.getAttoDiLegge().getTipoAtto());

		res.setAttoDiLeggeInserito(cup);
		
		res.setEsito(Esito.SUCCESSO); //di default è già SUCCESSO!
		
	}

	/**
	 * Esiste atto di legge.
	 *
	 * @return true, if successful
	 */
	private boolean esisteAttoDiLegge() {
		RicercaAttoDiLegge serviceRequest = new RicercaAttoDiLegge();
		RicercaLeggi attoDiLegge = new RicercaLeggi();
		attoDiLegge.setAnno(req.getAttoDiLegge().getAnno());
		attoDiLegge.setArticolo(req.getAttoDiLegge().getArticolo());
		attoDiLegge.setComma(req.getAttoDiLegge().getComma());
		attoDiLegge.setNumero(req.getAttoDiLegge().getNumero());
		attoDiLegge.setPunto(req.getAttoDiLegge().getPunto());
		serviceRequest.setAttoDiLegge(attoDiLegge);
		attoDiLegge.setTipoAtto(req.getAttoDiLegge().getTipoAtto());
		serviceRequest.setAttoDiLegge(attoDiLegge);
		serviceRequest.setRichiedente(req.getRichiedente());
		serviceRequest.setEnte(req.getEnte());
		RicercaAttoDiLeggeResponse serviceResponse = executeExternalService(ricercaAttoDiLeggeService, serviceRequest);
		
		return serviceResponse.getAttiTrovati() != null && !serviceResponse.getAttiTrovati().isEmpty();
	}


	/**
	 * Gets the atto di legge dad.
	 *
	 * @return the atto di legge dad
	 */
	public AttoDiLeggeDad getAttoDiLeggeDad() {
		return attoDiLeggeDad;
	}


	/**
	 * Sets the atto di legge dad.
	 *
	 * @param attoDiLeggeDad the new atto di legge dad
	 */
	public void setAttoDiLeggeDad(AttoDiLeggeDad attoDiLeggeDad) {
		this.attoDiLeggeDad = attoDiLeggeDad;
	}

	/**
	 * Gets the ricerca atto di legge service.
	 *
	 * @return the ricerca atto di legge service
	 */
	public RicercaAttoDiLeggeService getRicercaAttoDiLeggeService() {
		return ricercaAttoDiLeggeService;
	}

	/**
	 * Sets the ricerca atto di legge service.
	 *
	 * @param ricercaAttoDiLeggeService the new ricerca atto di legge service
	 */
	public void setRicercaAttoDiLeggeService(
			RicercaAttoDiLeggeService ricercaAttoDiLeggeService) {
		this.ricercaAttoDiLeggeService = ricercaAttoDiLeggeService;
	}

	
}
