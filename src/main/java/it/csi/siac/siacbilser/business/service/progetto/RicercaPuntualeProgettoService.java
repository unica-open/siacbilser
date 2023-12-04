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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgettoResponse;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaPuntualeProgettoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualeProgettoService extends CheckedAccountBaseService<RicercaPuntualeProgetto, RicercaPuntualeProgettoResponse> {

	/** The progetto. */
	private Progetto progetto;
	
	/** The progetto dad. */
	@Autowired
	private ProgettoDad progettoDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		progettoDad.setEnte(req.getProgetto().getEnte());
		progettoDad.setLoginOperazione(loginOperazione);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		progetto = req.getProgetto();
		
		checkNotNull(progetto, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progetto"));
		
		checkNotNull(progetto.getEnte(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(progetto.getEnte().getUid() != 0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(progetto.getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice progetto"), false);
		checkNotNull(progetto.getStatoOperativoProgetto(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato progetto"), false);
		checkNotNull(progetto.getTipoProgetto(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo progetto"));
		
		checkEntita(progetto.getBilancio(), "bilancio del progetto");
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaPuntualeProgettoResponse executeService(RicercaPuntualeProgetto serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		Progetto progettoTrovato = progettoDad.ricercaPuntualeProgetto(progetto);
		
		if(progettoTrovato == null) {
			res.setEsito(Esito.FALLIMENTO);
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Progetto", progetto.getCodice() + "/" + progetto.getStatoOperativoProgetto()));
			return;
		}
		
		res.setProgetto(progettoTrovato);
	}

}
