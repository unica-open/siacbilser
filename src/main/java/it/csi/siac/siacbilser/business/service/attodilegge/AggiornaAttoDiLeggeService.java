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

import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLeggeResponse;
import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AttoDiLeggeDad;
import it.csi.siac.siacbilser.integration.exception.AttoNonTrovatoException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaAttoDiLeggeService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAttoDiLeggeService extends CheckedAccountBaseService<AggiornaAttoDiLegge, AggiornaAttoDiLeggeResponse> {
	
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
		checkNotNull(req.getAttoDiLegge().getUid(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("identificativo atto di legge"),true);
		checkNotNull(req.getAttoDiLegge().getAnno(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno atto di legge"));
		checkNotNull(req.getAttoDiLegge().getNumero(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero atto di legge"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public AggiornaAttoDiLeggeResponse executeService(AggiornaAttoDiLegge serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		String methodName = "execute";
		
		AttoDiLegge atto = req.getAttoDiLegge();

		try {
			atto = attoDiLeggeDad.update(atto, req.getAttoDiLegge().getTipoAtto());
		} catch (AttoNonTrovatoException e) {
			log.warn(methodName, "Atto non trovato: "+ e.getMessage());
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("aggiornamento", String.format("Atto di Legge: %s/%s/%s", atto.getUid(), atto.getAnno(), atto.getNumero())));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}

		res.setAttoDiLeggeAggiornato(atto);
		
		res.setEsito(Esito.SUCCESSO);
		
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

	
}
