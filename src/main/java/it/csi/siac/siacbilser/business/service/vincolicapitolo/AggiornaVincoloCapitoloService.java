/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.vincolicapitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// TODO: Auto-generated Javadoc

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVincoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVincoloCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.VincoloCapitoliDad;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class AggiornaVincoloCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaVincoloCapitoloService extends CheckedAccountBaseService<AggiornaVincoloCapitolo, AggiornaVincoloCapitoloResponse> {
	
	/** The vincolo. */
	private Vincolo vincolo;
	
	/** The vincolo capitoli dad. */
	@Autowired
	private VincoloCapitoliDad vincoloCapitoliDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		vincolo = req.getVincolo();
		
		checkNotNull(vincolo, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Vincolo"));		
		checkCondition(vincolo.getUid()!= 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid Vincolo"));
//		checkNotNull(vincolo.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio")); //In "inserisci" il bilancio serve per ricavare il periodo. In "aggiorna" il periodo non può essere modificato
		checkNotNull(vincolo.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));		
		checkCondition(vincolo.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		checkNotNull(vincolo.getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice Vincolo"));
		checkNotNull(vincolo.getTipoVincoloCapitoli(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo vincolo capitoli"));
		checkNotNull(vincolo.getStatoOperativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo"));
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		vincoloCapitoliDad.setEnte(vincolo.getEnte());
		vincoloCapitoliDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public AggiornaVincoloCapitoloResponse executeService(AggiornaVincoloCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		vincoloCapitoliDad.aggiornaVincolo(vincolo);
		res.setVincolo(vincolo);
	}

}
