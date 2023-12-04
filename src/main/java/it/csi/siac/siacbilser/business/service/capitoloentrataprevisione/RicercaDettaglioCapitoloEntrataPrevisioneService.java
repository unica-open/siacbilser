/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
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
 * The Class RicercaDettaglioCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioCapitoloEntrataPrevisioneService 
	extends CheckedAccountBaseService<RicercaDettaglioCapitoloEntrataPrevisione, RicercaDettaglioCapitoloEntrataPrevisioneResponse> {

	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	
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
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"),false);		
		checkNotNull(req.getRicercaDettaglioCapitoloEPrev(), ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		checkCondition(req.getRicercaDettaglioCapitoloEPrev().getChiaveCapitolo()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave capitolo"));
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaDettaglioCapitoloEntrataPrevisioneResponse executeService(RicercaDettaglioCapitoloEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		final String methodName = "execute";
		CapitoloEntrataPrevisione cap = capitoloEntrataPrevisioneDad.ricercaDettaglioCapitoloEntrataPrevisione(req.getRicercaDettaglioCapitoloEPrev());
		
		if(cap==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo Entrata previsione"));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		Bilancio bilancio = capitoloEntrataPrevisioneDad.getBilancioAssociatoACapitolo(cap);
		res.setBilancio(bilancio);
		
		//Anno esercizio corrente
		ImportiCapitoloEP importiCapitoloEP = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno(), ImportiCapitoloEP.class, req.getImportiDerivatiRichiesti());			
		cap.setImportiCapitoloEP(importiCapitoloEP);
		res.addImportoCapitoloEP(importiCapitoloEP);
		
		//Anno esercizio + 1
		ImportiCapitoloEP importiCapitoloEP1 = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno()+1, ImportiCapitoloEP.class, req.getImportiDerivatiRichiesti());		
		res.addImportoCapitoloEP(importiCapitoloEP1);

		//Anno esercizio + 2
		ImportiCapitoloEP importiCapitoloEP2 = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno()+2, ImportiCapitoloEP.class, req.getImportiDerivatiRichiesti());		
		res.addImportoCapitoloEP(importiCapitoloEP2);
		
//		// TODO: controllare
//		//Anno esercizio - 1
//		Boolean isPresentImportiCapitoloEPM1 = importiCapitoloDad.isPresent(cap.getUid(), bilancio.getAnno() - 1);
//		log.debug(methodName, "Capitolo " + cap.getUid() + " importi anno - 1 (" + (bilancio.getAnno() - 1) + ") presenti? " + isPresentImportiCapitoloEPM1);
//		if(Boolean.TRUE.equals(isPresentImportiCapitoloEPM1)) {
//			ImportiCapitoloEP importiCapitoloEPM1 = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno() - 1, ImportiCapitoloEP.class);		
//			res.addImportoCapitoloEP(importiCapitoloEPM1);
//		}
		
//		//Capitolo equivalente gestione
		int uidEquivalente = capitoloEntrataPrevisioneDad.getEquivalenteUid(cap);
		cap.setUidCapitoloEquivalente(uidEquivalente);
		
//		if(uidEquivalente!=0){
//			cap.setUidCapitoloEquivalente(uidEquivalente);
//			ImportiCapitoloEG importiCapEq = importiCapitoloDad.findImportiCapitolo(uidEquivalente,bilancio.getAnno(), ImportiCapitoloEG.class);		
//			res.setImportiCapitoloEG(importiCapEq);
//		}
		
		//Ex Capitolo Gestione
		if(cap.getUidExCapitolo()!=0) {			
			ImportiCapitoloEG importiExCap = importiCapitoloDad.findImportiCapitolo(cap.getUidExCapitolo(),bilancio.getAnno()-1, ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());		
			res.setImportiCapitoloEG(importiExCap);
		}
		
		capitoloEntrataPrevisioneDad.populateFlags(cap);
		
		//classificatori gerarchici
		StrutturaAmministrativoContabile struttAmmCont = capitoloEntrataPrevisioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(cap);
		cap.setStrutturaAmministrativoContabile(struttAmmCont);
		
		ElementoPianoDeiConti elementoPianoDeiConti = capitoloEntrataPrevisioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(cap);
		cap.setElementoPianoDeiConti(elementoPianoDeiConti);
		
		SiopeEntrata siope = capitoloEntrataPrevisioneDad.ricercaClassificatoreSiopeEntrata(cap.getUid());
		cap.setSiopeEntrata(siope);
		
		CategoriaTipologiaTitolo categoriaTipologiaTitolo = capitoloEntrataPrevisioneDad.ricercaClassificatoreCategoriaTipologiaTitolo(cap);
		cap.setCategoriaTipologiaTitolo(categoriaTipologiaTitolo);
		
		TipologiaTitolo tipologiaTitolo = capitoloEntrataPrevisioneDad.ricercaClassificatoreTipologiaTitolo(cap, categoriaTipologiaTitolo);
		cap.setTipologiaTitolo(tipologiaTitolo);
		
		TitoloEntrata titoloEntrata = capitoloEntrataPrevisioneDad.ricercaClassificatoreTitoloEntrata(cap, tipologiaTitolo);
		cap.setTitoloEntrata(titoloEntrata);		
		
		
		//classificatori Generici
		TipoFinanziamento tipoFin = capitoloEntrataPrevisioneDad.ricercaClassificatoreTipoFinanziamento(cap);
		res.setTipoFinanziamento(tipoFin);
		
		TipoFondo tipoFondo = capitoloEntrataPrevisioneDad.ricercaClassificatoreTipoFondo(cap);
		res.setTipoFondo(tipoFondo);
		
		RicorrenteEntrata ricorrente = capitoloEntrataPrevisioneDad.ricercaClassificatoreRicorrenteEntrata(cap.getUid());
		cap.setRicorrenteEntrata(ricorrente);
		
		PerimetroSanitarioEntrata perimetroSanitario = capitoloEntrataPrevisioneDad.ricercaClassificatorePerimetroSanitarioEntrata(cap.getUid());
		cap.setPerimetroSanitarioEntrata(perimetroSanitario);
		
		TransazioneUnioneEuropeaEntrata tues = capitoloEntrataPrevisioneDad.ricercaClassificatoreTransazioneUnioneEuropeaEntrata(cap.getUid());
		cap.setTransazioneUnioneEuropeaEntrata(tues);	
		
		List<ClassificatoreGenerico> listaClassificatori = capitoloEntrataPrevisioneDad.ricercaClassificatoriGenerici(cap);
		res.setListaClassificatori(listaClassificatori);
		
		
		//TODO Manca: Atto di legge, classificatore cofog, upb collegate!
		
		
		res.setCapitoloEntrataPrevisione(cap);
		res.setEsito(Esito.SUCCESSO);		
	}
}
