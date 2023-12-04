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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
//TODO servizio da implementare

/**
 * The Class RicercaPuntualeCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualeCapitoloEntrataPrevisioneService
	extends CheckedAccountBaseService<RicercaPuntualeCapitoloEntrataPrevisione, RicercaPuntualeCapitoloEntrataPrevisioneResponse> {
	
	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);		
		RicercaPuntualeCapitoloEPrev criteriRicerca = req.getRicercaPuntualeCapitoloEPrev();
		checkNotNull(criteriRicerca, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		checkNotNull(criteriRicerca.getAnnoEsercizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
		checkNotNull(criteriRicerca.getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), false);
		checkNotNull(criteriRicerca.getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
		checkNotNull(criteriRicerca.getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
		checkNotNull(criteriRicerca.getStatoOperativoElementoDiBilancio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {		
		capitoloEntrataPrevisioneDad.setEnte(req.getEnte());		
		capitoloEntrataPrevisioneDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(req.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaPuntualeCapitoloEntrataPrevisioneResponse executeService(RicercaPuntualeCapitoloEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		CapitoloEntrataPrevisione capitoloEntrataPrevisione = capitoloEntrataPrevisioneDad.ricercaPuntualeCapitoloEntrataPrevisione(req.getRicercaPuntualeCapitoloEPrev());
	
		if(capitoloEntrataPrevisione==null){
			RicercaPuntualeCapitoloEPrev criteriRicerca = req.getRicercaPuntualeCapitoloEPrev();
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo entrata previsione",String.format("Capitolo: %s/%s/%s/%s/%s/%s", criteriRicerca.getAnnoEsercizio(),
					criteriRicerca.getAnnoCapitolo(), 
					criteriRicerca.getNumeroCapitolo(), 
					criteriRicerca.getNumeroArticolo(), 
					criteriRicerca.getNumeroUEB(), 
					criteriRicerca.getStatoOperativoElementoDiBilancio().toString())));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		Bilancio bilancio = capitoloEntrataPrevisioneDad.getBilancioAssociatoACapitolo(capitoloEntrataPrevisione);
		capitoloEntrataPrevisione.setBilancio(bilancio);
		//res.setBilancio(bilancio);//TODO inutile e ridondante. eliminarlo dal Model		
		
		ImportiCapitoloEP importiCapitolo = importiCapitoloDad.findImportiCapitolo(capitoloEntrataPrevisione, req.getRicercaPuntualeCapitoloEPrev().getAnnoEsercizio(), ImportiCapitoloEP.class, req.getImportiDerivatiRichiesti());
		capitoloEntrataPrevisione.setImportiCapitoloEP(importiCapitolo);
		//res.setImportiCapitoloEP(importiCapitoloUP); //TODO inutile e ridondante. eliminarlo dal Model
		
		capitoloEntrataPrevisioneDad.populateFlags(capitoloEntrataPrevisione);
		
		StrutturaAmministrativoContabile struttAmmCont = capitoloEntrataPrevisioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloEntrataPrevisione);
		capitoloEntrataPrevisione.setStrutturaAmministrativoContabile(struttAmmCont);
		//res.setStrutturaAmministrativoContabile(struttAmmCont);//TODO inutile e ridondante.  eliminarlo dal Model
		
		ElementoPianoDeiConti elementoPianoDeiConti = capitoloEntrataPrevisioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloEntrataPrevisione);
		capitoloEntrataPrevisione.setElementoPianoDeiConti(elementoPianoDeiConti);
		//res.setElementoPianoDeiConti(elementoPianoDeiConti);	//TODO inutile e ridondante.  eliminarlo dal Model
		
		
		res.setCapitoloEntrataPrevisione(capitoloEntrataPrevisione);		
		res.setEsito(Esito.SUCCESSO);
	}
	
	

}
