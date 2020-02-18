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
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaVincoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaVincoloCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.VincoloCapitoliDad;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class AnnullaVincoloCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaVincoloCapitoloService extends CheckedAccountBaseService<AnnullaVincoloCapitolo, AnnullaVincoloCapitoloResponse> {
	
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
		checkNotNull(vincolo.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente vincolo"));		
		checkCondition(vincolo.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente vincolo"));
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
	public AnnullaVincoloCapitoloResponse executeService(AnnullaVincoloCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		vincoloCapitoliDad.aggiornaStatoOperativoVincolo(vincolo, StatoOperativo.ANNULLATO);
		vincolo.setStatoOperativo(StatoOperativo.ANNULLATO);
		res.setVincolo(vincolo);
	}

}
