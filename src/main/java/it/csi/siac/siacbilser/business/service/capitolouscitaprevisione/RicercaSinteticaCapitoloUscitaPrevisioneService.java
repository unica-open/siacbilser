/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitaprevisione;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * The Class RicercaSinteticaCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaCapitoloUscitaPrevisioneService extends CheckedAccountBaseService<RicercaSinteticaCapitoloUscitaPrevisione, RicercaSinteticaCapitoloUscitaPrevisioneResponse> {
	
	/** The capitolo uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;
	
//	@Autowired
//	private ClassificatoriDad classificatoriDad; //siacTClassDao
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		
		RicercaSinteticaCapitoloUPrev criteri = req.getRicercaSinteticaCapitoloUPrev();
		
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
		capitoloUscitaPrevisioneDad.setEnte(req.getEnte());		
		capitoloUscitaPrevisioneDad.setLoginOperazione(loginOperazione);		
		importiCapitoloDad.setEnte(req.getEnte());
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaSinteticaCapitoloUscitaPrevisioneResponse executeService(RicercaSinteticaCapitoloUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		ListaPaginata<CapitoloUscitaPrevisione> listaCapitoloUscitaPrevisione = capitoloUscitaPrevisioneDad
				.ricercaSinteticaCapitoloUscitaPrevisione(req.getRicercaSinteticaCapitoloUPrev(), req.getParametriPaginazione());
		
		
		if(listaCapitoloUscitaPrevisione==null || listaCapitoloUscitaPrevisione.isEmpty()){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo uscita previsione",""));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		// Importi
		if(Boolean.TRUE.equals(req.getCalcolaTotaleImporti())) {
			ImportiCapitoloUP importi = capitoloUscitaPrevisioneDad.importiRicercaSintetica(req.getRicercaSinteticaCapitoloUPrev());
			res.setTotaleImporti(importi);
		}
		
		for (CapitoloUscitaPrevisione capitoloUscitaPrevisione : listaCapitoloUscitaPrevisione) {
			
			Bilancio bilancio = capitoloUscitaPrevisioneDad.getBilancioAssociatoACapitolo(capitoloUscitaPrevisione);
			res.setBilancio(bilancio); //FIXME Sbagliato! deve essere un parametro nella lista!! va settato dentro CapitoloUscitaPrevisione il quale però non ha il Bilancio al suo interno!!!
			
			ImportiCapitoloUP importiCapitoloUP = importiCapitoloDad.findImportiCapitolo(capitoloUscitaPrevisione, req.getRicercaSinteticaCapitoloUPrev().getAnnoEsercizio(), ImportiCapitoloUP.class, req.getImportiDerivatiRichiesti());
			capitoloUscitaPrevisione.setImportiCapitoloUP(importiCapitoloUP);
			
			// XXX: Valutare la scelta effettuata
			ImportiCapitoloUP importiCapitoloUP1 = importiCapitoloDad.findImportiCapitolo(capitoloUscitaPrevisione, req.getRicercaSinteticaCapitoloUPrev().getAnnoEsercizio() + 1, ImportiCapitoloUP.class, req.getImportiDerivatiRichiesti());
			ImportiCapitoloUP importiCapitoloUP2 = importiCapitoloDad.findImportiCapitolo(capitoloUscitaPrevisione, req.getRicercaSinteticaCapitoloUPrev().getAnnoEsercizio() + 2, ImportiCapitoloUP.class, req.getImportiDerivatiRichiesti());
			capitoloUscitaPrevisione.getListaImportiCapitolo().add(importiCapitoloUP);
			capitoloUscitaPrevisione.getListaImportiCapitolo().add(importiCapitoloUP1);
			capitoloUscitaPrevisione.getListaImportiCapitolo().add(importiCapitoloUP2);
			
			//Ex Capitolo Uscita Previsione
			CapitoloUscitaGestione cug = capitoloUscitaPrevisioneDad.getExCapitolo(capitoloUscitaPrevisione);		
			if(cug!=null){			
				capitoloUscitaPrevisione.setExAnnoCapitolo(cug.getExAnnoCapitolo());
				capitoloUscitaPrevisione.setExArticolo(cug.getExArticolo());
				capitoloUscitaPrevisione.setExCapitolo(cug.getExCapitolo());
				capitoloUscitaPrevisione.setExUEB(cug.getExUEB());
			}
			
			capitoloUscitaPrevisioneDad.populateFlags(capitoloUscitaPrevisione);
			
			if(req.isRichiesto(TipologiaClassificatore.CDC) || req.isRichiesto(TipologiaClassificatore.CDR)) {
				StrutturaAmministrativoContabile struttAmmCont = capitoloUscitaPrevisioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloUscitaPrevisione);
				capitoloUscitaPrevisione.setStrutturaAmministrativoContabile(struttAmmCont);
			}
			
			if(req.isRichiestiAlmenoUno(TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V)){
				ElementoPianoDeiConti elementoPianoDeiConti = capitoloUscitaPrevisioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloUscitaPrevisione);
				capitoloUscitaPrevisione.setElementoPianoDeiConti(elementoPianoDeiConti);
			}
			
			if(req.isRichiestiAlmenoUno(TipologiaClassificatore.SIOPE_SPESA, TipologiaClassificatore.SIOPE_SPESA_I, TipologiaClassificatore.SIOPE_SPESA_II, TipologiaClassificatore.SIOPE_SPESA_III)){
				SiopeSpesa siope = capitoloUscitaPrevisioneDad.ricercaClassificatoreSiopeSpesa(capitoloUscitaPrevisione.getUid());
				capitoloUscitaPrevisione.setSiopeSpesa(siope);
			}
			
			
			Programma programma = capitoloUscitaPrevisioneDad.ricercaClassificatoreGerarchico(capitoloUscitaPrevisione.getUid(), TipologiaClassificatore.PROGRAMMA, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaPrevisione.setProgramma(programma);
		
			
			Missione missione = capitoloUscitaPrevisioneDad.ricercaClassificatoreGerarchico(capitoloUscitaPrevisione.getUid(), TipologiaClassificatore.MISSIONE, programma, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaPrevisione.setMissione(missione);
			
			
			Macroaggregato macroaggregato = capitoloUscitaPrevisioneDad.ricercaClassificatoreGerarchico(capitoloUscitaPrevisione.getUid(), TipologiaClassificatore.MACROAGGREGATO, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaPrevisione.setMacroaggregato(macroaggregato);	

			TitoloSpesa titoloSpesa = capitoloUscitaPrevisioneDad.ricercaClassificatoreGerarchico(capitoloUscitaPrevisione.getUid(), TipologiaClassificatore.TITOLO_SPESA, macroaggregato, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaPrevisione.setTitoloSpesa(titoloSpesa);
			
			
		}	
		
		
		res.setCapitoli(listaCapitoloUscitaPrevisione);		
		res.setEsito(Esito.SUCCESSO);
		
	}

}
