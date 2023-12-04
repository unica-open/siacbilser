/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioCapitoloEntrataGestioneService 
	extends CheckedAccountBaseService<RicercaDettaglioCapitoloEntrataGestione, RicercaDettaglioCapitoloEntrataGestioneResponse> {

	/** The capitolo entrata gestione dad. */
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloEntrataGestioneDad.setEnte(req.getEnte());		
		capitoloEntrataGestioneDad.setLoginOperazione(loginOperazione);	
		importiCapitoloDad.setEnte(req.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"),false);		
		checkNotNull(req.getRicercaDettaglioCapitoloEGest(), ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		checkCondition(req.getRicercaDettaglioCapitoloEGest().getChiaveCapitolo()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave capitolo"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaDettaglioCapitoloEntrataGestioneResponse executeService(RicercaDettaglioCapitoloEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		CapitoloEntrataGestione cap= capitoloEntrataGestioneDad.ricercaDettaglioCapitoloEntrataGestione(req.getRicercaDettaglioCapitoloEGest());
		
		if(cap==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo Entrata Gestione"));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		Bilancio bilancio = capitoloEntrataGestioneDad.getBilancioAssociatoACapitolo(cap);
		res.setBilancio(bilancio);
		
		//Anno esercizio corrente
		ImportiCapitoloEG importiCapitoloEG = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno(), ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());			
		cap.setImportiCapitoloEG(importiCapitoloEG);
		res.addImportoCapitoloEG(importiCapitoloEG);
		
		//Anno esercizio + 1
		ImportiCapitoloEG importiCapitoloEG1 = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno()+1, ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());		
		res.addImportoCapitoloEG(importiCapitoloEG1);

		//Anno esercizio + 2
		ImportiCapitoloEG importiCapitoloEG2 = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno()+2, ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());		
		res.addImportoCapitoloEG(importiCapitoloEG2);
		
//		//Capitolo equivalente gestione
//		CapitoloEntrataGestione capEquivalente = capitoloEntrataGestioneDad.getEquivalente(capitoloEntrataGestione);	
//		if(capEquivalente!=null){
//			ImportiCapitoloEG importiCapitoloEquivalente = importiCapitoloDad.findImportiCapitolo(capEquivalente,bilancio.getAnno()-1, ImportiCapitoloEG.class);			
//			res.setImportiCapitoloEquivalente(importiCapitoloEquivalente);
//		}
		
		//Ex Capitolo Gestione
		if(cap.getUidExCapitolo()!=0) {
			ImportiCapitoloEG importiExCap = importiCapitoloDad.findImportiCapitolo(cap.getUidExCapitolo(),bilancio.getAnno()-1, ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());		
			res.addImportoCapitoloEG(importiExCap);
			res.setImportiCapitoloEquivalente(importiExCap);
			
		}
		
		capitoloEntrataGestioneDad.populateFlags(cap);
		
		//classificatori gerarchici
		StrutturaAmministrativoContabile struttAmmCont = capitoloEntrataGestioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(cap);
		cap.setStrutturaAmministrativoContabile(struttAmmCont);
		
		ElementoPianoDeiConti elementoPianoDeiConti = capitoloEntrataGestioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(cap);
		cap.setElementoPianoDeiConti(elementoPianoDeiConti);
		
		SiopeEntrata siope = capitoloEntrataGestioneDad.ricercaClassificatoreSiopeEntrata(cap.getUid());
		cap.setSiopeEntrata(siope);
		
		CategoriaTipologiaTitolo categoriaTipologiaTitolo = capitoloEntrataGestioneDad.ricercaClassificatoreCategoriaTipologiaTitolo(cap);
		cap.setCategoriaTipologiaTitolo(categoriaTipologiaTitolo);
		
		TipologiaTitolo tipologiaTitolo = capitoloEntrataGestioneDad.ricercaClassificatoreTipologiaTitolo(cap, categoriaTipologiaTitolo);
		cap.setTipologiaTitolo(tipologiaTitolo);
		
		TitoloEntrata titoloEntrata = capitoloEntrataGestioneDad.ricercaClassificatoreTitoloEntrata(cap, tipologiaTitolo);
		cap.setTitoloEntrata(titoloEntrata);
		
		
		//classificatori Generici
		TipoFinanziamento tipoFin = capitoloEntrataGestioneDad.ricercaClassificatoreTipoFinanziamento(cap);
		res.setTipoFinanziamento(tipoFin);
		
		TipoFondo tipoFondo = capitoloEntrataGestioneDad.ricercaClassificatoreTipoFondo(cap);
		res.setTipoFondo(tipoFondo);
		
		RicorrenteEntrata ricorrente = capitoloEntrataGestioneDad.ricercaClassificatoreRicorrenteEntrata(cap.getUid());
		cap.setRicorrenteEntrata(ricorrente);
		
		PerimetroSanitarioEntrata perimetroSanitario = capitoloEntrataGestioneDad.ricercaClassificatorePerimetroSanitarioEntrata(cap.getUid());
		cap.setPerimetroSanitarioEntrata(perimetroSanitario);
		
		TransazioneUnioneEuropeaEntrata tues = capitoloEntrataGestioneDad.ricercaClassificatoreTransazioneUnioneEuropeaEntrata(cap.getUid());
		cap.setTransazioneUnioneEuropeaEntrata(tues);		
		
		List<ClassificatoreGenerico> listaClassificatori = capitoloEntrataGestioneDad.ricercaClassificatoriGenerici(cap);
		res.setListaClassificatori(listaClassificatori);
		
		
		//TODO Manca: Atto di legge, classificatore cofog, upb collegate!
		
		
		res.setCapitoloEntrataGestione(cap);
		res.setEsito(Esito.SUCCESSO);		
	}
}

