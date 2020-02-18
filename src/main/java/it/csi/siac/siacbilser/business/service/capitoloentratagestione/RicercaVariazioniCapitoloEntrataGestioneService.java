/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaVariazioniCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaVariazioniCapitoloEntrataGestioneService 
	extends CheckedAccountBaseService<RicercaVariazioniCapitoloEntrataGestione, RicercaVariazioniCapitoloEntrataGestioneResponse> {

	/** The capitolo entrata gestione dad. */
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	
	/** The variazioni dad. */
	@Autowired
	protected VariazioniDad variazioniDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCapitoloEntrataGest(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"), true);
		checkCondition(req.getCapitoloEntrataGest().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo"), false);
		checkNotNull(req.getCapitoloEntrataGest().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getCapitoloEntrataGest().getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"), false);
		initParametriPaginazione(req.getParametriPaginazione(),100);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloEntrataGestioneDad.setBilancio(req.getCapitoloEntrataGest().getBilancio());
		capitoloEntrataGestioneDad.setEnte(req.getCapitoloEntrataGest().getEnte());
		variazioniDad.setBilancio(req.getCapitoloEntrataGest().getBilancio());
		variazioniDad.setEnte(req.getCapitoloEntrataGest().getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaVariazioniCapitoloEntrataGestioneResponse executeService(RicercaVariazioniCapitoloEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		ListaPaginata<VariazioneImportoCapitolo> variazioneImportiTrovate =  variazioniDad.ricercaSinteticaVariazioneImportoCapitolo(req.getCapitoloEntrataGest(), req.getParametriPaginazione());		
		res.setListaVariazioniImporti(variazioneImportiTrovate);
				
		ListaPaginata<VariazioneCodificaCapitolo> variazioneCodificheTrovate =  variazioniDad.ricercaSinteticaVariazioneCodificaCapitolo(req.getCapitoloEntrataGest(), req.getParametriPaginazione());		
		res.setListaVariazioniCodifiche(variazioneCodificheTrovate);
		
		res.setEsito(Esito.SUCCESSO);
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
