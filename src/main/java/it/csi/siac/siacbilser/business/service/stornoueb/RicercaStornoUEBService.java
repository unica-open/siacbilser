/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stornoueb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStornoUEB;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStornoUEBResponse;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.StornoUEB;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaStornoUEBService.
 */
@Service

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Deprecated
public class RicercaStornoUEBService extends CheckedAccountBaseService<RicercaStornoUEB, RicercaStornoUEBResponse> {
	

	/** The variazioni dad. */
	@Autowired
	protected VariazioniDad variazioniDad;	
	
	/** The importi capitolo dad. */
	@Autowired
	protected ImportiCapitoloDad importiCapitoloDad;
	
	/** The storno ueb. */
	protected StornoUEB stornoUEB;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		variazioniDad.setEnte(req.getEnte());
		variazioniDad.setBilancio(req.getBilancio());
		variazioniDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(req.getEnte());
	}
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente uid"),false);
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"),false);
		
			
		stornoUEB = req.getStornoUEB();
		
		checkNotNull(stornoUEB,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("storno ueb"), true);
		
		checkCondition(req.getTipoCapitolo()==null || req.getTipoCapitolo().equals(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE) || req.getTipoCapitolo().equals(TipoCapitolo.CAPITOLO_USCITA_GESTIONE), ErroreCore.PARAMETRO_ERRATO.getErrore("tipo capitolo", req.getTipoCapitolo(), "null, uscita getstione o entrata gestione"));
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public RicercaStornoUEBResponse executeService(RicercaStornoUEB serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override	 
	protected void execute() {
		
		ListaPaginata<StornoUEB> storni =  variazioniDad.ricercaSinteticaStornoUEB(stornoUEB,req.getTipoCapitolo(), req.getParametriPaginazione());
		
		res.setStorniUEB(storni);
		res.setEsito(Esito.SUCCESSO);
			
	}
	

	

}
