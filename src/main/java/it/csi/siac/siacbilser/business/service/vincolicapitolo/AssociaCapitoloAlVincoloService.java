/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.vincolicapitolo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AssociaCapitoloAlVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AssociaCapitoloAlVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.VincoloCapitoliDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoVincoloCapitoli;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class AssociaCapitoloAlVincoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssociaCapitoloAlVincoloService extends CheckedAccountBaseService<AssociaCapitoloAlVincolo, AssociaCapitoloAlVincoloResponse> {

	/** The ricerca vincolo service. */
	@Autowired
	private RicercaVincoloService ricercaVincoloService;
	
	/** The vincolo capitoli dad. */
	@Autowired
	private VincoloCapitoliDad vincoloCapitoliDad;
	
	/** The capitolo dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getVincolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Vincolo"));
		checkNotNull(req.getVincolo().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente vincolo"));		
		checkCondition(req.getVincolo().getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente vincolo"));
		checkCondition(req.getVincolo().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid vincolo"));
		
		checkNotNull(req.getCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Capitolo"));
//		checkNotNull(req.getCapitolo().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente capitolo"));		
		checkCondition(req.getCapitolo().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid Capitolo"));
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		vincoloCapitoliDad.setEnte(req.getVincolo().getEnte());
		vincoloCapitoliDad.setLoginOperazione(loginOperazione);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public AssociaCapitoloAlVincoloResponse executeService(AssociaCapitoloAlVincolo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		Vincolo vincolo = vincoloCapitoliDad.findVincoloById(req.getVincolo().getUid());
		
		checkTipoVariazioneCoerenteConTipoCapitolo(vincolo);
		checkCapitoloNonPresenteInAltriVincoli();
		
		vincoloCapitoliDad.associaCapitoloAlVincolo(req.getVincolo(), req.getCapitolo());
		
	}
	

	/**
	 * Controlla che il tipo di vincolo (previsione o gestione) sia coerente con il 
	 * tipo di capitolo da associare (rispettivamente previsione o gestione).
	 *
	 * @param vincolo the vincolo
	 */
	private void checkTipoVariazioneCoerenteConTipoCapitolo(Vincolo vincolo) {		

		TipoCapitolo tipoCapitolo = capitoloDad.findTipoCapitolo(req.getCapitolo().getUid());
		
		if(vincolo.getTipoVincoloCapitoli() == TipoVincoloCapitoli.GESTIONE) {
			if(!TipoCapitolo.isTipoCapitoloGestione(tipoCapitolo)){
				throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("Impossibile associare un capitolo di Previsione ad un vincolo di Gestione."), Esito.FALLIMENTO);
			}
		} else if(vincolo.getTipoVincoloCapitoli() == TipoVincoloCapitoli.PREVISIONE) {
			if(!TipoCapitolo.isTipoCapitoloPrevisione(tipoCapitolo)){
				throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("Impossibile associare un capitolo di Gestione ad un vincolo di Previsione."), Esito.FALLIMENTO);
			}
		} else {
			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("Impossibile determinare il tipo del vincolo: Previsione o Gestione. "), Esito.FALLIMENTO);
		}
		
	}
	
	
	/**
	 * Check capitolo non presente in altri vincoli.
	 */
	private void checkCapitoloNonPresenteInAltriVincoli() {
		List<VincoloCapitoli> listaVincoli  = ricercaVincoliAssociatiAlCapitolo(req.getCapitolo());
		
		// Ciclo sui vincoli presenti per controllare se ve ne sia almeno
		// uno in stato valido
		for (VincoloCapitoli v : listaVincoli) {
			if (StatoOperativo.VALIDO.equals(v.getStatoOperativo())) {

				throw new BusinessException(ErroreBil.CAPITOLO_GIA_ASSOCIATO_A_UN_VINCOLO.getErrore("Codice vincolo = " + v.getCodice()));
			}
		}	  
				  
		//vincoloCapitoliDad.findVincoloByCapitoloAssociato(req.getCapitolo());
		//throw new BusinessException(ErroreBil.CAPITOLO_GIA_ASSOCIATO_A_UN_VINCOLO.getErrore("Codice vincolo = "));		
	}


	/**
	 * Ricerca vincoli associati al capitolo.
	 *
	 * @param capitolo the capitolo
	 * @return the lista paginata
	 */
	private ListaPaginata<VincoloCapitoli> ricercaVincoliAssociatiAlCapitolo(@SuppressWarnings("rawtypes")Capitolo capitolo) {
		RicercaVincolo rv = new RicercaVincolo();
		  
		rv.setDataOra(req.getDataOra());
		rv.setRichiedente(req.getRichiedente());		
		rv.setParametriPaginazione(impostaParametriPaginazione());
		
		//rv.setTipiCapitolo(Arrays.asList(TipoCapitolo.values()));		
		rv.setCapitolo(capitolo);
		Vincolo vincolo = new Vincolo();
		vincolo.setEnte(req.getVincolo().getEnte());
		rv.setVincolo(vincolo);
				  
		RicercaVincoloResponse rvResp = executeExternalService(ricercaVincoloService,rv);
		
		return rvResp.getVincoloCapitoli();
	}
	
	/**
	  * Imposta i parametri di paginazione per la ricerca del vincolo.
	  * 
	  * @return i parametri di paginazione
	  */
	 private ParametriPaginazione impostaParametriPaginazione() {
	  ParametriPaginazione parametri = new ParametriPaginazione();
	  
	  parametri.setElementiPerPagina(100);
	  parametri.setNumeroPagina(0);
	  
	  return parametri;
	 }



	
	

}
