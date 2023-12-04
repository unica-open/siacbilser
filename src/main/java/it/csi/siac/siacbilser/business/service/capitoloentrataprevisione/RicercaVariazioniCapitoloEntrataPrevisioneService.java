/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
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
 * The Class RicercaVariazioniCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaVariazioniCapitoloEntrataPrevisioneService 
	extends CheckedAccountBaseService<RicercaVariazioniCapitoloEntrataPrevisione, RicercaVariazioniCapitoloEntrataPrevisioneResponse> {
	
	/** The Constant NUMERO_ELEMENTI_PER_PAGINA_DEFAULT. */
	private static final int NUMERO_ELEMENTI_PER_PAGINA_DEFAULT = 100;

	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	/** The variazioni dad. */
	@Autowired
	protected VariazioniDad variazioniDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCapitoloEntrataPrev(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"), true);
		checkCondition(req.getCapitoloEntrataPrev().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo"), false);
		checkNotNull(req.getCapitoloEntrataPrev().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getCapitoloEntrataPrev().getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"), false);
		initParametriPaginazione(req.getParametriPaginazione(),NUMERO_ELEMENTI_PER_PAGINA_DEFAULT);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloEntrataPrevisioneDad.setBilancio(req.getCapitoloEntrataPrev().getBilancio());
		capitoloEntrataPrevisioneDad.setEnte(req.getCapitoloEntrataPrev().getEnte());
		variazioniDad.setBilancio(req.getCapitoloEntrataPrev().getBilancio());
		variazioniDad.setEnte(req.getCapitoloEntrataPrev().getEnte());
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaVariazioniCapitoloEntrataPrevisioneResponse executeService(RicercaVariazioniCapitoloEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		ListaPaginata<VariazioneImportoCapitolo> variazioneImportiTrovate =  variazioniDad.ricercaSinteticaVariazioneImportoCapitolo(req.getCapitoloEntrataPrev(), req.getParametriPaginazione());		
		res.setListaVariazioniImporti(variazioneImportiTrovate);
				
		ListaPaginata<VariazioneCodificaCapitolo> variazioneCodificheTrovate =  variazioniDad.ricercaSinteticaVariazioneCodificaCapitolo(req.getCapitoloEntrataPrev(), req.getParametriPaginazione());		
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
