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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgettoResponse;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

// TODO: Auto-generated Javadoc
/**
 * Classe di service per la ricerca sintetica del Progetto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2014
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaProgettoService extends CheckedAccountBaseService<RicercaSinteticaProgetto, RicercaSinteticaProgettoResponse> {

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
		
		checkNotNull(progetto.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente progetto"));		
		checkCondition(progetto.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente progetto"));		

		checkNotNull(req.getParametriPaginazione(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Parametri paginazione"));
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
	@Transactional(readOnly = true)
	public RicercaSinteticaProgettoResponse executeService(RicercaSinteticaProgetto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		ListaPaginata<Progetto> progetti = progettoDad.ricercaSinteticaProgetti(progetto, req.getParametriPaginazione(), req.getProgettoModelDetails());
		res.setProgetti(progetti);
		res.setCardinalitaComplessiva(progetti.size());
	}

}
