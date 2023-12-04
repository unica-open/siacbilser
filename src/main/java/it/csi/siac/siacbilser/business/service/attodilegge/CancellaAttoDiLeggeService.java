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

import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLeggeResponse;
import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AttoDiLeggeDad;
import it.csi.siac.siacbilser.integration.exception.AttoAnnullatoException;
import it.csi.siac.siacbilser.integration.exception.AttoNonTrovatoException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class CancellaAttoDiLeggeService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CancellaAttoDiLeggeService extends CheckedAccountBaseService<CancellaAttoDiLegge, CancellaAttoDiLeggeResponse> {
	
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
		checkNotNull(req.getAttoDiLegge().getUid(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid atto di legge"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public CancellaAttoDiLeggeResponse executeService(CancellaAttoDiLegge serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		AttoDiLegge atto = req.getAttoDiLegge();
		
		if (esistonoCollegamentiAttoDiLegge()) {
			
			res.addErrore(ErroreCore.ESISTONO_ENTITA_COLLEGATE.getErrore("aggiornamento", String.format("Atto di Legge: %s/%s", atto.getAnno(), atto.getNumero())));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		try {
			atto = attoDiLeggeDad.delete(req.getAttoDiLegge().getUid());
		} catch (AttoNonTrovatoException e) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("aggiornamento", String.format("Atto di Legge: %s/%s", atto.getAnno(), atto.getNumero())));
			res.setEsito(Esito.FALLIMENTO);
			return;
		} catch (AttoAnnullatoException e) {
			res.addErrore(ErroreAtt.ATTO_LEGGE_NON_ANNULLABILE.getErrore());
			res.setEsito(Esito.FALLIMENTO);
			return;
			
		}

		res.setAttoDiLeggeCancellato(atto);
		
		res.setEsito(Esito.SUCCESSO);
		
	}
	
	/**
	 * Esistono collegamenti atto di legge.
	 *
	 * @return true, if successful
	 */
	private boolean esistonoCollegamentiAttoDiLegge() {
		return attoDiLeggeDad.esistonoCollegamentiBil(req.getAttoDiLegge().getUid());
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
