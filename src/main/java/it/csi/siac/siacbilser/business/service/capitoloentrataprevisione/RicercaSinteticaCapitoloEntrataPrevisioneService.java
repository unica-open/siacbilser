/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * The Class RicercaSinteticaCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaCapitoloEntrataPrevisioneService 
	extends CheckedAccountBaseService<RicercaSinteticaCapitoloEntrataPrevisione, RicercaSinteticaCapitoloEntrataPrevisioneResponse> {
	
	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaDad accantonamentoFondiDubbiaEsigibilitaDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		
		RicercaSinteticaCapitoloEPrev criteri = req.getRicercaSinteticaCapitoloEPrev();
		
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
		capitoloEntrataPrevisioneDad.setEnte(req.getEnte());		
		capitoloEntrataPrevisioneDad.setLoginOperazione(loginOperazione);		
		importiCapitoloDad.setEnte(req.getEnte());
	}

	@Override
	@Transactional(readOnly= true)
	public RicercaSinteticaCapitoloEntrataPrevisioneResponse executeService(RicercaSinteticaCapitoloEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		ListaPaginata<CapitoloEntrataPrevisione> listaCapitoloEntrataPrevisione = capitoloEntrataPrevisioneDad
				.ricercaSinteticaCapitoloEntrataPrevisione(req.getRicercaSinteticaCapitoloEPrev(), req.getParametriPaginazione());
		
		
		if(listaCapitoloEntrataPrevisione==null || listaCapitoloEntrataPrevisione.isEmpty()){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo entrata previsione",""));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		// Importi
		if(Boolean.TRUE.equals(req.getCalcolaTotaleImporti())) {
			ImportiCapitoloEP importi = capitoloEntrataPrevisioneDad.importiRicercaSintetica(req.getRicercaSinteticaCapitoloEPrev());
			res.setTotaleImporti(importi);
		}
		
		for (CapitoloEntrataPrevisione capitoloEntrataPrevisione : listaCapitoloEntrataPrevisione) {
			
			Bilancio bilancio = capitoloEntrataPrevisioneDad.getBilancioAssociatoACapitolo(capitoloEntrataPrevisione);
			//FIXME Sbagliato! deve essere un parametro nella lista!! va settato dentro CapitoloEntrataPrevisione il quale però non ha il Bilancio al suo interno!!!
			res.setBilancio(bilancio); 
			
			ImportiCapitoloEP importiCapitoloEP = importiCapitoloDad.findImportiCapitolo(capitoloEntrataPrevisione, req.getRicercaSinteticaCapitoloEPrev().getAnnoEsercizio(), ImportiCapitoloEP.class, req.getImportiDerivatiRichiesti());
			capitoloEntrataPrevisione.setImportiCapitoloEP(importiCapitoloEP);
			
			// XXX: Valutare la scelta effettuata
			ImportiCapitoloEP importiCapitoloEP1 = importiCapitoloDad.findImportiCapitolo(capitoloEntrataPrevisione, req.getRicercaSinteticaCapitoloEPrev().getAnnoEsercizio() + 1, ImportiCapitoloEP.class, req.getImportiDerivatiRichiesti());
			ImportiCapitoloEP importiCapitoloEP2 = importiCapitoloDad.findImportiCapitolo(capitoloEntrataPrevisione, req.getRicercaSinteticaCapitoloEPrev().getAnnoEsercizio() + 2, ImportiCapitoloEP.class, req.getImportiDerivatiRichiesti());
			capitoloEntrataPrevisione.getListaImportiCapitolo().add(importiCapitoloEP);
			capitoloEntrataPrevisione.getListaImportiCapitolo().add(importiCapitoloEP1);
			capitoloEntrataPrevisione.getListaImportiCapitolo().add(importiCapitoloEP2);
			
			//Ex Capitolo Entrata Previsione
			CapitoloEntrataGestione exCeg = capitoloEntrataPrevisioneDad.getExCapitolo(capitoloEntrataPrevisione);		
			if(exCeg!=null){			
				capitoloEntrataPrevisione.setExAnnoCapitolo(exCeg.getExAnnoCapitolo());
				capitoloEntrataPrevisione.setExArticolo(exCeg.getExArticolo());
				capitoloEntrataPrevisione.setExCapitolo(exCeg.getExCapitolo());
				capitoloEntrataPrevisione.setExUEB(exCeg.getExUEB());
			}
			
			capitoloEntrataPrevisioneDad.populateFlags(capitoloEntrataPrevisione);
			
			if(req.isRichiesto(TipologiaClassificatore.CDC) || req.isRichiesto(TipologiaClassificatore.CDR)) {
				StrutturaAmministrativoContabile struttAmmCont = capitoloEntrataPrevisioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloEntrataPrevisione);
				capitoloEntrataPrevisione.setStrutturaAmministrativoContabile(struttAmmCont);
			}
			
			if(req.isRichiestiAlmenoUno(TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V)){
				ElementoPianoDeiConti elementoPianoDeiConti = capitoloEntrataPrevisioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloEntrataPrevisione);
				capitoloEntrataPrevisione.setElementoPianoDeiConti(elementoPianoDeiConti);
			}
				
			if(req.isRichiestiAlmenoUno(TipologiaClassificatore.TITOLO_ENTRATA, TipologiaClassificatore.TIPOLOGIA, TipologiaClassificatore.CATEGORIA)){
				CategoriaTipologiaTitolo categoriaTipologiaTitolo = capitoloEntrataPrevisioneDad.ricercaClassificatoreCategoriaTipologiaTitolo(capitoloEntrataPrevisione);
				capitoloEntrataPrevisione.setCategoriaTipologiaTitolo(categoriaTipologiaTitolo);
				
				TipologiaTitolo tipologiaTitolo = capitoloEntrataPrevisioneDad.ricercaClassificatoreTipologiaTitolo(capitoloEntrataPrevisione, categoriaTipologiaTitolo);
				capitoloEntrataPrevisione.setTipologiaTitolo(tipologiaTitolo);
				
				TitoloEntrata titoloEntrata = capitoloEntrataPrevisioneDad.ricercaClassificatoreTitoloEntrata(capitoloEntrataPrevisione, tipologiaTitolo);
				capitoloEntrataPrevisione.setTitoloEntrata(titoloEntrata);
			}
			
			if(req.isRichiestiAlmenoUno(TipologiaClassificatore.SIOPE_ENTRATA, TipologiaClassificatore.SIOPE_ENTRATA_I, TipologiaClassificatore.SIOPE_ENTRATA_II, TipologiaClassificatore.SIOPE_ENTRATA_III)){
				SiopeEntrata siope = capitoloEntrataPrevisioneDad.ricercaClassificatoreSiopeEntrata(capitoloEntrataPrevisione.getUid());
				capitoloEntrataPrevisione.setSiopeEntrata(siope);
			}
			
			if(Boolean.TRUE.equals(req.getRicercaSinteticaCapitoloEPrev().getRichiediAccantonamentoFondiDubbiaEsigibilita())) {
				AccantonamentoFondiDubbiaEsigibilita accantonamentoFondiDubbiaEsigibilita = accantonamentoFondiDubbiaEsigibilitaDad.findByCapitolo(capitoloEntrataPrevisione, TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE);
				capitoloEntrataPrevisione.setAccantonamentoFondiDubbiaEsigibilita(accantonamentoFondiDubbiaEsigibilita);
			}
		}	
		
		
		res.setCapitoli(listaCapitoloEntrataPrevisione);
		res.setEsito(Esito.SUCCESSO);
		
	}

}
