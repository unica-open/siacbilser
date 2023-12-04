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

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLeggeResponse;
import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AttoDiLeggeDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaAttoDiLeggeService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAttoDiLeggeService extends CheckedAccountBaseService<RicercaAttoDiLegge, RicercaAttoDiLeggeResponse> {
	
	/** The atto di legge dad. */
	@Autowired
	private AttoDiLeggeDad attoDiLeggeDad;

	
	
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
		checkNotNull(req.getAttoDiLegge().getAnno(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno atto di legge"));
		checkCondition(req.getAttoDiLegge().getNumero()!=null || req.getAttoDiLegge().getTipoAtto()!=null,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero atto di legge o tipo atto di legge"));
		
		initParametriPaginazione(req.getParametriPaginazione(),100);
		

	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaAttoDiLeggeResponse executeService(RicercaAttoDiLegge serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		ListaPaginata<AttoDiLegge> elencoTrovati = attoDiLeggeDad.ricerca(req.getAttoDiLegge(), req.getParametriPaginazione());
				
		res.setAttiTrovati(elencoTrovati);
		
		res.setEsito(Esito.SUCCESSO); //di default è già SUCCESSO!
		
	}
	
	
	/**
	 * Se parametri paginazione è null imposta il default elementi per pagina passato come parametro.
	 *
	 * @param pp the pp
	 * @param defaultElementiPerPagina the default elementi per pagina
	 */
	protected void initParametriPaginazione(ParametriPaginazione pp, int defaultElementiPerPagina) {
		if(pp==null) {
			pp = new ParametriPaginazione();
			pp.setElementiPerPagina(defaultElementiPerPagina);
			pp.setNumeroPagina(0);
			req.setParametriPaginazione(pp);
		}
	}
	


	
}
