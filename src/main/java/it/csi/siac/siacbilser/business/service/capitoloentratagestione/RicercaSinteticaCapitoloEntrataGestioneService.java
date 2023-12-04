/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
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
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * The Class RicercaSinteticaCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaCapitoloEntrataGestioneService 
	extends CheckedAccountBaseService<RicercaSinteticaCapitoloEntrataGestione, RicercaSinteticaCapitoloEntrataGestioneResponse> {

	/** The capitolo entrata gestione dad. */
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		
		RicercaSinteticaCapitoloEGest criteri = req.getRicercaSinteticaCapitoloEntrata();
		
		checkNotNull(criteri, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		checkNotNull(criteri.getAnnoEsercizio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"));
		
		boolean valorizzatoNumeroArticolo = criteri.getNumeroArticolo() != null && criteri.getNumeroArticolo() != 0;
		boolean valorizzatoNumeroCapitolo = criteri.getNumeroCapitolo() != null && criteri.getNumeroCapitolo() != 0;
		
		boolean valorizzatoSoloNumeroArticolo = valorizzatoNumeroArticolo && !valorizzatoNumeroCapitolo;
		//boolean valorizzatoSoloNumeroCapitolo = !valorizzatoNumeroArticolo && valorizzatoNumeroCapitolo;
		
		//checkCondition((valorizzatoNumeroArticolo && valorizzatoNumeroCapitolo) || valorizzatoSoloNumeroCapitolo , ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
		//Obbligo di valorizzare capitolo se valorizzato articolo
		//checkCondition(valorizzatoSoloNumeroCapitolo || (valorizzatoNumeroArticolo && valorizzatoNumeroCapitolo), ErroreBil.CAPITOLO_OBBLIGATORIO_PER_ARTICOLO.getErrore());
		
		checkCondition(!valorizzatoSoloNumeroArticolo, ErroreBil.CAPITOLO_OBBLIGATORIO_PER_ARTICOLO.getErrore());
		
		if(req.getTipologieClassificatoriRichiesti() == null) {
			req.setTipologieClassificatoriRichiesti(EnumSet.allOf(TipologiaClassificatore.class));
		}
		
	}
	
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
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaSinteticaCapitoloEntrataGestioneResponse executeService(RicercaSinteticaCapitoloEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		ListaPaginata<CapitoloEntrataGestione> listaCapitoloEntrataGestione = capitoloEntrataGestioneDad
				.ricercaSinteticaCapitoloEntrataGestione(req.getRicercaSinteticaCapitoloEntrata(), req.getParametriPaginazione());
		
		
		if(listaCapitoloEntrataGestione==null || listaCapitoloEntrataGestione.isEmpty()){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo entrata Gestione",""));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		// Importi
		if(Boolean.TRUE.equals(req.getCalcolaTotaleImporti())) {
			ImportiCapitoloEG importi = capitoloEntrataGestioneDad.importiRicercaSintetica(req.getRicercaSinteticaCapitoloEntrata());
			res.setTotaleImporti(importi);
		}
		
		for (CapitoloEntrataGestione capitoloEntrataGestione : listaCapitoloEntrataGestione) {
			
			Bilancio bilancio = capitoloEntrataGestioneDad.getBilancioAssociatoACapitolo(capitoloEntrataGestione);
			//FIXME Sbagliato! deve essere un parametro nella lista!! va settato dentro CapitoloEntrataGestione il quale però non ha il Bilancio al suo interno!!!
			res.setBilancio(bilancio);
			
			ImportiCapitoloEG importiCapitoloEG = importiCapitoloDad.findImportiCapitolo(capitoloEntrataGestione, req.getRicercaSinteticaCapitoloEntrata().getAnnoEsercizio(), ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());
			capitoloEntrataGestione.setImportiCapitoloEG(importiCapitoloEG);
			
			// XXX: Valutare la scelta effettuata
			ImportiCapitoloEG importiCapitoloEG1 = importiCapitoloDad.findImportiCapitolo(capitoloEntrataGestione, req.getRicercaSinteticaCapitoloEntrata().getAnnoEsercizio() + 1, ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());
			ImportiCapitoloEG importiCapitoloEG2 = importiCapitoloDad.findImportiCapitolo(capitoloEntrataGestione, req.getRicercaSinteticaCapitoloEntrata().getAnnoEsercizio() + 2, ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());
			capitoloEntrataGestione.getListaImportiCapitolo().add(importiCapitoloEG);
			capitoloEntrataGestione.getListaImportiCapitolo().add(importiCapitoloEG1);
			capitoloEntrataGestione.getListaImportiCapitolo().add(importiCapitoloEG2);
			
			//Ex Capitolo Entrata Gestione
			CapitoloEntrataGestione ceg = capitoloEntrataGestioneDad.getExCapitolo(capitoloEntrataGestione);		
			if(ceg!=null){			
				capitoloEntrataGestione.setExAnnoCapitolo(ceg.getExAnnoCapitolo());
				capitoloEntrataGestione.setExArticolo(ceg.getExArticolo());
				capitoloEntrataGestione.setExCapitolo(ceg.getExCapitolo());
				capitoloEntrataGestione.setExUEB(ceg.getExUEB());
			}
			
			capitoloEntrataGestioneDad.populateFlags(capitoloEntrataGestione);
			
			
			
			if(req.isRichiesto(TipologiaClassificatore.CDC) || req.isRichiesto(TipologiaClassificatore.CDR)) {
				StrutturaAmministrativoContabile struttAmmCont = capitoloEntrataGestioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloEntrataGestione);
				capitoloEntrataGestione.setStrutturaAmministrativoContabile(struttAmmCont);
			}
			
			if(req.isRichiestiAlmenoUno(TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V)){
				ElementoPianoDeiConti elementoPianoDeiConti = capitoloEntrataGestioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloEntrataGestione);
				capitoloEntrataGestione.setElementoPianoDeiConti(elementoPianoDeiConti);
			}
			
			
			//TitoloEntrata -> TipologiaTitolo -> CategoriaTipologiaTitolo;
			if(req.isRichiestiAlmenoUno(TipologiaClassificatore.TITOLO_ENTRATA, TipologiaClassificatore.TIPOLOGIA, TipologiaClassificatore.CATEGORIA)){
				CategoriaTipologiaTitolo categoriaTipologiaTitolo = capitoloEntrataGestioneDad.ricercaClassificatoreCategoriaTipologiaTitolo(capitoloEntrataGestione);
				capitoloEntrataGestione.setCategoriaTipologiaTitolo(categoriaTipologiaTitolo);		
			
				TipologiaTitolo tipologiaTitolo = capitoloEntrataGestioneDad.ricercaClassificatoreTipologiaTitolo(capitoloEntrataGestione, categoriaTipologiaTitolo);
				capitoloEntrataGestione.setTipologiaTitolo(tipologiaTitolo);
				
				TitoloEntrata titoloEntrata = capitoloEntrataGestioneDad.ricercaClassificatoreTitoloEntrata(capitoloEntrataGestione, tipologiaTitolo);
				capitoloEntrataGestione.setTitoloEntrata(titoloEntrata);
			}
			
			if(req.isRichiestiAlmenoUno(TipologiaClassificatore.SIOPE_ENTRATA, TipologiaClassificatore.SIOPE_ENTRATA_I, TipologiaClassificatore.SIOPE_ENTRATA_II, TipologiaClassificatore.SIOPE_ENTRATA_III)){
				SiopeEntrata siope = capitoloEntrataGestioneDad.ricercaClassificatoreSiopeEntrata(capitoloEntrataGestione.getUid());
				capitoloEntrataGestione.setSiopeEntrata(siope);
			}
			
			// 27/05/2014 classificatori Generici
			TipoFinanziamento tipoFin = capitoloEntrataGestioneDad.ricercaClassificatoreGenerico(capitoloEntrataGestione.getUid(), TipologiaClassificatore.TIPO_FINANZIAMENTO, req.getTipologieClassificatoriRichiesti());
			capitoloEntrataGestione.setTipoFinanziamento(tipoFin);
			
			TipoFondo tipoFondo = capitoloEntrataGestioneDad.ricercaClassificatoreGenerico(capitoloEntrataGestione.getUid(), TipologiaClassificatore.TIPO_FONDO, req.getTipologieClassificatoriRichiesti());
			capitoloEntrataGestione.setTipoFondo(tipoFondo);
			
			RicorrenteEntrata ricorrente = capitoloEntrataGestioneDad.ricercaClassificatoreGenerico(capitoloEntrataGestione.getUid(), TipologiaClassificatore.RICORRENTE_ENTRATA, req.getTipologieClassificatoriRichiesti());
			capitoloEntrataGestione.setRicorrenteEntrata(ricorrente);
		
			PerimetroSanitarioEntrata perimetroSanitario = capitoloEntrataGestioneDad.ricercaClassificatoreGenerico(capitoloEntrataGestione.getUid(), TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA, req.getTipologieClassificatoriRichiesti());
			capitoloEntrataGestione.setPerimetroSanitarioEntrata(perimetroSanitario);
			
			TransazioneUnioneEuropeaEntrata tues = capitoloEntrataGestioneDad.ricercaClassificatoreGenerico(capitoloEntrataGestione.getUid(), TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA, req.getTipologieClassificatoriRichiesti());
			capitoloEntrataGestione.setTransazioneUnioneEuropeaEntrata(tues);		
			
			List<ClassificatoreGenerico> listaClassificatori = capitoloEntrataGestioneDad.ricercaClassificatoriGenerici(capitoloEntrataGestione.getUid(), req.getTipologieClassificatoriRichiesti());
			capitoloEntrataGestione.setClassificatoriGenerici(listaClassificatori);
			
		}	
		
		
		res.setCapitoli(listaCapitoloEntrataGestione);		
		res.setEsito(Esito.SUCCESSO);
		
	}

}
