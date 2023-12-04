/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionicodifica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneCodificheResponse;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaVariazioneCodificheService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaVariazioneCodificheService extends CheckedAccountBaseService<RicercaVariazioneCodifiche, RicercaVariazioneCodificheResponse> {
	

	/** The variazioni dad. */
	@Autowired
	protected VariazioniDad variazioniDad;	
	
	/** The importi capitolo dad. */
	@Autowired
	protected ImportiCapitoloDad importiCapitoloDad;
	
	/** The variazione. */
	protected VariazioneCodificaCapitolo variazione;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		variazioniDad.setEnte(variazione.getEnte());
		variazioniDad.setBilancio(variazione.getBilancio());
		variazioniDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(variazione.getEnte());
	}
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		variazione = req.getVariazioneCodificaCapitolo();
		
		checkNotNull(variazione, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("variazione"), true);	
		
		checkNotNull(variazione.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(variazione.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente uid"),false);
		
		checkNotNull(variazione.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(variazione.getBilancio().getAnno()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"),false);
				
		if(variazione.getTipoVariazione()==null){
			variazione.setTipoVariazione(TipoVariazione.VARIAZIONE_CODIFICA);
		}
		
		//checkCondition(req.getTipoCapitolo()==null || req.getTipoCapitolo().equals(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE) || req.getTipoCapitolo().equals(TipoCapitolo.CAPITOLO_USCITA_GESTIONE), ErroreCore.PARAMETRO_ERRATO.getErrore("tipo capitolo", req.getTipoCapitolo(), "null, uscita getstione o entrata gestione"));
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public RicercaVariazioneCodificheResponse executeService(RicercaVariazioneCodifiche serviceRequest) {
		return super.executeService(serviceRequest);
	}
		

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		ListaPaginata<VariazioneCodificaCapitolo> variazioniTrovate =  variazioniDad.ricercaSinteticaVariazioneCodificaCapitolo(variazione,null,req.getTipiCapitolo(), req.getParametriPaginazione());
		
		res.setVariazioniDiBilancio(variazioniTrovate);
		res.setEsito(Esito.SUCCESSO);
			
	}
	

	

}
