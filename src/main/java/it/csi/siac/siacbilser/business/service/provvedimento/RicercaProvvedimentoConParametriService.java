/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.provvedimento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class RicercaProvvedimentoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaProvvedimentoConParametriService extends CheckedAccountBaseService<RicercaProvvedimento, RicercaProvvedimentoResponse> {
	
	/** The provvedimento dad. */
	@Autowired
	private AttoAmministrativoDad attoAmministrativoDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		attoAmministrativoDad.setLoginOperazione(loginOperazione);
		attoAmministrativoDad.setEnte(req.getEnte());
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRicercaAtti(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ricerca atti"),true);
		
		if (req.getRicercaAtti().getUid() == null) {
		
			// SIAC-5586: tolta l'obbligatorieta' dell'anno
//			checkNotNull(req.getRicercaAtti().getAnnoAtto(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno atto"),true);
//			checkCondition(req.getRicercaAtti().getAnnoAtto() != null && (req.getRicercaAtti().getTipoAtto() != null ||  req.getRicercaAtti().getNumeroAtto() != null), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quando valorizzato anno atto bisogna valorizzare o il tipo o il numero atto"));
			checkCondition(req.getRicercaAtti().getTipoAtto() != null ||  req.getRicercaAtti().getNumeroAtto() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipo o numero atto"));
		}
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public RicercaProvvedimentoResponse executeService(RicercaProvvedimento serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		List<AttoAmministrativo> listaAtti = attoAmministrativoDad.ricercaProvvedimenti(req.getRicercaAtti());
		
		res.setListaAttiAmministrativi(listaAtti);
		res.setCardinalitaComplessiva(listaAtti.size());
		
		// Di default e' gia' SUCCESSO
		res.setEsito(Esito.SUCCESSO);
	}

}
