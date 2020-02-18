/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitaprevisione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaPuntualeCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualeCapitoloUscitaPrevisioneService extends
		CheckedAccountBaseService<RicercaPuntualeCapitoloUscitaPrevisione, RicercaPuntualeCapitoloUscitaPrevisioneResponse> {
	
	/** The capitolo uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);		
		RicercaPuntualeCapitoloUPrev criteriRicerca = req.getRicercaPuntualeCapitoloUPrev();
		checkNotNull(criteriRicerca, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		checkNotNull(criteriRicerca.getAnnoEsercizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
		checkNotNull(criteriRicerca.getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), false);
		checkNotNull(criteriRicerca.getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
		checkNotNull(criteriRicerca.getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
		checkNotNull(criteriRicerca.getStatoOperativoElementoDiBilancio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato"), false);
		//Non ha senso che sia obbligatorio StatoOperativoElementoDiBilancio ai fini della ricerca puntuale!!! Ma l'analisi chiede questo.

	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {		
		capitoloUscitaPrevisioneDad.setEnte(req.getEnte());		
		capitoloUscitaPrevisioneDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(req.getEnte());
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaPuntualeCapitoloUscitaPrevisioneResponse executeService(RicercaPuntualeCapitoloUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		CapitoloUscitaPrevisione capitoloUscitaPrevisione = capitoloUscitaPrevisioneDad.ricercaPuntualeCapitoloUscitaPrevisione(req.getRicercaPuntualeCapitoloUPrev());
	
		if(capitoloUscitaPrevisione==null){
			RicercaPuntualeCapitoloUPrev criteriRicerca = req.getRicercaPuntualeCapitoloUPrev();
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo uscita previsione",String.format("Capitolo: %s/%s/%s/%s/%s/%s", criteriRicerca.getAnnoEsercizio(),
					criteriRicerca.getAnnoCapitolo(), 
					criteriRicerca.getNumeroCapitolo(), 
					criteriRicerca.getNumeroArticolo(), 
					criteriRicerca.getNumeroUEB(), 
					criteriRicerca.getStatoOperativoElementoDiBilancio().toString())));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		Bilancio bilancio = capitoloUscitaPrevisioneDad.getBilancioAssociatoACapitolo(capitoloUscitaPrevisione);
		capitoloUscitaPrevisione.setBilancio(bilancio);
//		res.setBilancio(bilancio);		
		
		ImportiCapitoloUP importiCapitoloUP = importiCapitoloDad.findImportiCapitolo(capitoloUscitaPrevisione, req.getRicercaPuntualeCapitoloUPrev().getAnnoEsercizio(), ImportiCapitoloUP.class, req.getImportiDerivatiRichiesti());
		capitoloUscitaPrevisione.setImportiCapitoloUP(importiCapitoloUP);
		//res.setImportiCapitoloUP(importiCapitoloUP); //TODO inutile e ridondante. eliminarlo dal Model
		
		capitoloUscitaPrevisioneDad.populateFlags(capitoloUscitaPrevisione);
		
		StrutturaAmministrativoContabile struttAmmCont = capitoloUscitaPrevisioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloUscitaPrevisione);
		capitoloUscitaPrevisione.setStrutturaAmministrativoContabile(struttAmmCont);
		//res.setStrutturaAmministrativoContabile(struttAmmCont);//TODO inutile e ridondante.  eliminarlo dal Model
		
		ElementoPianoDeiConti elementoPianoDeiConti = capitoloUscitaPrevisioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloUscitaPrevisione);
		capitoloUscitaPrevisione.setElementoPianoDeiConti(elementoPianoDeiConti);
		//res.setElementoPianoDeiConti(elementoPianoDeiConti);	//TODO inutile e ridondante.  eliminarlo dal Model
		
		
		res.setCapitoloUscitaPrevisione(capitoloUscitaPrevisione);		
		res.setEsito(Esito.SUCCESSO);
	}
	
	

}
