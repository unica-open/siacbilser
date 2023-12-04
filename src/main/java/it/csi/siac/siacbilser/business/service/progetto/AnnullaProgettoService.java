/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaProgettoResponse;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnullaProgettoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaProgettoService extends CheckedAccountBaseService<AnnullaProgetto, AnnullaProgettoResponse> {

	/** The progetto. */
	private Progetto progetto;
	
	/** The progetto dad. */
	@Autowired
	private ProgettoDad progettoDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		progetto = req.getProgetto();
		
		checkNotNull(progetto, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progetto"));
		checkCondition(progetto.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid progetto"), false);
		
		checkNotNull(progetto.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente progetto"));
		checkCondition(progetto.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente progetto"), false);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		progettoDad.setEnte(progetto.getEnte());
		progettoDad.setLoginOperazione(loginOperazione);
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public AnnullaProgettoResponse executeService(AnnullaProgetto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		// Controllo la coerenza dello stato
		checkStatoOperativoProgettoCoerente();
		
		progettoDad.aggiornaStatoOperativoProgetto(progetto, StatoOperativoProgetto.ANNULLATO);
		progetto.setStatoOperativoProgetto(StatoOperativoProgetto.ANNULLATO);
		res.setProgetto(progetto);
	}

	/**
	 * Controlla che lo stato operativo del Progetto sia coerente con l'intenzione di annullamento.
	 * <br>
	 * In particolare, il controllo effettuato &eacute; che tale stato non sia gi&agrave; pari ad <code>ANNULLATO</code>.
	 */
	private void checkStatoOperativoProgettoCoerente() {
		StatoOperativoProgetto statoOperativoProgetto = progettoDad.findStatoOperativoByProgetto(progetto);
		if(StatoOperativoProgetto.ANNULLATO.equals(statoOperativoProgetto)) {
			throw new BusinessException(ErroreCore.TRANSAZIONE_DI_STATO_NON_POSSIBILE.getErrore(), Esito.FALLIMENTO);
		}
	}

}
