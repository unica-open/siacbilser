/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * Classe di Service per la ricerca dei TipoAmbito.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/02/2014
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTipiAmbitoService extends CheckedAccountBaseService<RicercaTipiAmbito, RicercaTipiAmbitoResponse> {

	/** The progetto dad. */
	@Autowired
	private ProgettoDad progettoDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		
		checkNotNull(req.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		progettoDad.setEnte(req.getEnte());
		progettoDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly = true)
	public RicercaTipiAmbitoResponse executeService(RicercaTipiAmbito serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<TipoAmbito> listaTipiAmbito = progettoDad.ricercaListaTipiAmbito(req.getAnno());
		res.setTipiAmbito(listaTipiAmbito);
		res.setCardinalitaComplessiva(listaTipiAmbito.size());
	}

}
