/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.relazioneattodileggecapitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.AttoDiLeggeDad;
import it.csi.siac.siacbilser.integration.exception.AttoNonTrovatoException;
import it.csi.siac.siacbilser.integration.exception.CapitoloNonTrovatoException;
import it.csi.siac.siacbilser.integration.exception.RelazioneEsistenteException;
import it.csi.siac.siacbilser.model.AttoDiLeggeCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceRelazioneAttoDiLeggeCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceRelazioneAttoDiLeggeCapitoloService extends CheckedAccountBaseService<InserisceRelazioneAttoDiLeggeCapitolo, InserisceRelazioneAttoDiLeggeCapitoloResponse> {
	
	/** The atto di legge dad. */
	@Autowired
	AttoDiLeggeDad attoDiLeggeDad = null;
	
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
		checkNotNull(req.getRichiedente(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiedente"),true);
		checkNotNull(req.getCapitolo(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"),true);
		checkCondition(req.getCapitolo().getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo uid"),true);
		checkNotNull(req.getAttoDiLegge(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto di legge"),true);
		checkCondition(req.getAttoDiLegge().getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto di legge uid"),true);
		checkNotNull(req.getAttoDiLeggeCapitolo(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto di legge capitolo"),true);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public InserisceRelazioneAttoDiLeggeCapitoloResponse executeService(InserisceRelazioneAttoDiLeggeCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		// Per sicurezza
		if(req.getAttoDiLeggeCapitolo().getDescrizione().length() > 500) {
			req.getAttoDiLeggeCapitolo().setDescrizione(req.getAttoDiLeggeCapitolo().getDescrizione().substring(0, Math.min(req.getAttoDiLeggeCapitolo().getDescrizione().length(), 500)));
		}
		try {
			AttoDiLeggeCapitolo cup = attoDiLeggeDad.createRelazioneAttoCapitolo(req.getAttoDiLegge(), req.getAttoDiLeggeCapitolo(), req.getCapitolo());
			res.setAttoDiLeggeCapitolo(cup);
			res.setEsito(Esito.SUCCESSO);
		} catch (AttoNonTrovatoException e) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("inserimento", String.format("Atto di Legge uid: %s", req.getAttoDiLegge().getUid())));
			res.setEsito(Esito.FALLIMENTO);
		} catch (CapitoloNonTrovatoException e) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("inserimento", String.format("Capitolo uid: %s", req.getCapitolo().getUid())));
			res.setEsito(Esito.FALLIMENTO);
		} catch (RelazioneEsistenteException e) {
			res.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("inserimento", e.getMessage()));
			res.setEsito(Esito.FALLIMENTO);
			//e.printStackTrace();
		}
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
