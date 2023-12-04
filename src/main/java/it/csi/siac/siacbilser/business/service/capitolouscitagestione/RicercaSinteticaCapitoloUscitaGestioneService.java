/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;


/**
 * The Class RicercaSinteticaCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaCapitoloUscitaGestioneService 
	extends CheckedAccountBaseService<RicercaSinteticaCapitoloUscitaGestione, RicercaSinteticaCapitoloUscitaGestioneResponse> {

	/** The capitolo uscita gestione dad. */
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		
		RicercaSinteticaCapitoloUGest criteri = req.getRicercaSinteticaCapitoloUGest();	
		
		checkNotNull(criteri, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		checkNotNull(criteri.getAnnoEsercizio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"));
		
		boolean valorizzatoNumeroArticolo = criteri.getNumeroArticolo() != null && criteri.getNumeroArticolo() != 0;
		boolean valorizzatoNumeroCapitolo = criteri.getNumeroCapitolo() != null && criteri.getNumeroCapitolo() != 0;
		
		boolean valorizzatoSoloNumeroArticolo = valorizzatoNumeroArticolo && !valorizzatoNumeroCapitolo;
		//boolean valorizzatoSoloNumeroCapitolo = !valorizzatoNumeroArticolo && valorizzatoNumeroCapitolo;
		
//		checkCondition((valorizzatoNumeroArticolo && valorizzatoNumeroCapitolo) || valorizzatoSoloNumeroCapitolo , ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
//		
//		//Obbligo di valorizzare capitolo se valorizzato articolo
//		checkCondition(valorizzatoSoloNumeroCapitolo 
//				|| (valorizzatoNumeroArticolo && valorizzatoNumeroCapitolo), ErroreBil.CAPITOLO_OBBLIGATORIO_PER_ARTICOLO.getErrore());
		
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
		capitoloUscitaGestioneDad.setEnte(req.getEnte());		
		capitoloUscitaGestioneDad.setLoginOperazione(loginOperazione);		
		importiCapitoloDad.setEnte(req.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaSinteticaCapitoloUscitaGestioneResponse executeService(RicercaSinteticaCapitoloUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override	
	protected void execute() {
		
		ListaPaginata<CapitoloUscitaGestione> listaCapitoloUscitaGestione = capitoloUscitaGestioneDad
				.ricercaSinteticaCapitoloUscitaGestione(req.getRicercaSinteticaCapitoloUGest(), req.getParametriPaginazione());
		
		
		if(listaCapitoloUscitaGestione==null || listaCapitoloUscitaGestione.isEmpty()){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo uscita Gestione", ""));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		// Importi
		if(Boolean.TRUE.equals(req.getCalcolaTotaleImporti())) {
			ImportiCapitoloUG importi = capitoloUscitaGestioneDad.importiRicercaSintetica(req.getRicercaSinteticaCapitoloUGest());
			res.setTotaleImporti(importi);
		}
		
		for (CapitoloUscitaGestione capitoloUscitaGestione : listaCapitoloUscitaGestione) {
			
			Bilancio bilancio = capitoloUscitaGestioneDad.getBilancioAssociatoACapitolo(capitoloUscitaGestione);
			//FIXME Sbagliato! deve essere un parametro nella lista!! va settato dentro CapitoloUscitaGestione il quale però non ha il Bilancio al suo interno!!!
			res.setBilancio(bilancio);
			
			ImportiCapitoloUG importiCapitoloUG = importiCapitoloDad.findImportiCapitolo(capitoloUscitaGestione, req.getRicercaSinteticaCapitoloUGest().getAnnoEsercizio(), ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());
			capitoloUscitaGestione.setImportiCapitoloUG(importiCapitoloUG);
			
			// XXX: Valutare la scelta effettuata
			ImportiCapitoloUG importiCapitoloUG1 = importiCapitoloDad.findImportiCapitolo(capitoloUscitaGestione, req.getRicercaSinteticaCapitoloUGest().getAnnoEsercizio() + 1, ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());
			ImportiCapitoloUG importiCapitoloUG2 = importiCapitoloDad.findImportiCapitolo(capitoloUscitaGestione, req.getRicercaSinteticaCapitoloUGest().getAnnoEsercizio() + 2, ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());
			capitoloUscitaGestione.getListaImportiCapitolo().add(importiCapitoloUG);
			capitoloUscitaGestione.getListaImportiCapitolo().add(importiCapitoloUG1);
			capitoloUscitaGestione.getListaImportiCapitolo().add(importiCapitoloUG2);
			
			//Ex Capitolo Uscita Gestione
			CapitoloUscitaGestione exCug = capitoloUscitaGestioneDad.getExCapitolo(capitoloUscitaGestione);		
			if(exCug!=null) {			
				capitoloUscitaGestione.setExAnnoCapitolo(exCug.getExAnnoCapitolo());
				capitoloUscitaGestione.setExArticolo(exCug.getExArticolo());
				capitoloUscitaGestione.setExCapitolo(exCug.getExCapitolo());
				capitoloUscitaGestione.setExUEB(exCug.getExUEB());
			}
			
			capitoloUscitaGestioneDad.populateFlags(capitoloUscitaGestione);
			
			if(req.isRichiesto(TipologiaClassificatore.CDC) || req.isRichiesto(TipologiaClassificatore.CDR)) {
				StrutturaAmministrativoContabile struttAmmCont = capitoloUscitaGestioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloUscitaGestione);
				capitoloUscitaGestione.setStrutturaAmministrativoContabile(struttAmmCont);
			}
			
			if(req.isRichiestiAlmenoUno(TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V)){
				ElementoPianoDeiConti elementoPianoDeiConti = capitoloUscitaGestioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloUscitaGestione);
				capitoloUscitaGestione.setElementoPianoDeiConti(elementoPianoDeiConti);
			}
			
			if(req.isRichiestiAlmenoUno(TipologiaClassificatore.DIVISIONE_COFOG, TipologiaClassificatore.CLASSE_COFOG, TipologiaClassificatore.GRUPPO_COFOG)){
				ClassificazioneCofog classificazioneCofog = capitoloUscitaGestioneDad.ricercaClassificatoreCofogCapitolo(capitoloUscitaGestione);
			    capitoloUscitaGestione.setClassificazioneCofog(classificazioneCofog);  //TODO IN ANALISI MANCA IL RITORNO DEI COFOG! Scommentare se verrà richiesta la change.
			}
			
			if(req.isRichiestiAlmenoUno(TipologiaClassificatore.SIOPE_SPESA, TipologiaClassificatore.SIOPE_SPESA_I, TipologiaClassificatore.SIOPE_SPESA_II, TipologiaClassificatore.SIOPE_SPESA_III)){
				SiopeSpesa siope = capitoloUscitaGestioneDad.ricercaClassificatoreSiopeSpesa(capitoloUscitaGestione.getUid());
				capitoloUscitaGestione.setSiopeSpesa(siope);
			}
			
			Programma programma = capitoloUscitaGestioneDad.ricercaClassificatoreGerarchico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.PROGRAMMA, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaGestione.setProgramma(programma);
		
			
			Missione missione = capitoloUscitaGestioneDad.ricercaClassificatoreGerarchico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.MISSIONE, programma, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaGestione.setMissione(missione);
			
			
			Macroaggregato macroaggregato = capitoloUscitaGestioneDad.ricercaClassificatoreGerarchico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.MACROAGGREGATO, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaGestione.setMacroaggregato(macroaggregato);	

			TitoloSpesa titoloSpesa = capitoloUscitaGestioneDad.ricercaClassificatoreGerarchico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.TITOLO_SPESA, macroaggregato, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaGestione.setTitoloSpesa(titoloSpesa);
			
			
			// 27/05/2014 classificatori Generici
			TipoFinanziamento tipoFin = capitoloUscitaGestioneDad.ricercaClassificatoreGenerico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.TIPO_FINANZIAMENTO, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaGestione.setTipoFinanziamento(tipoFin);
			
			TipoFondo tipoFondo = capitoloUscitaGestioneDad.ricercaClassificatoreGenerico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.TIPO_FONDO, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaGestione.setTipoFondo(tipoFondo);
			
			RicorrenteSpesa ricorrente = capitoloUscitaGestioneDad.ricercaClassificatoreGenerico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.RICORRENTE_SPESA, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaGestione.setRicorrenteSpesa(ricorrente);
			
			PerimetroSanitarioSpesa perimetroSanitario = capitoloUscitaGestioneDad.ricercaClassificatoreGenerico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.PERIMETRO_SANITARIO_SPESA, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaGestione.setPerimetroSanitarioSpesa(perimetroSanitario);
			
			TransazioneUnioneEuropeaSpesa tues = capitoloUscitaGestioneDad.ricercaClassificatoreGenerico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.TRANSAZIONE_UE_SPESA, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaGestione.setTransazioneUnioneEuropeaSpesa(tues);
			
			
			PoliticheRegionaliUnitarie pru = capitoloUscitaGestioneDad.ricercaClassificatoreGenerico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.POLITICHE_REGIONALI_UNITARIE, req.getTipologieClassificatoriRichiesti());
			capitoloUscitaGestione.setPoliticheRegionaliUnitarie(pru);
			
			
			List<ClassificatoreGenerico> listaClassificatori = capitoloUscitaGestioneDad.ricercaClassificatoriGenerici(capitoloUscitaGestione.getUid(), req.getTipologieClassificatoriRichiesti());
			capitoloUscitaGestione.setClassificatoriGenerici(listaClassificatori);
			
			
		}	
		
		
		res.setCapitoli(listaCapitoloUscitaGestione);		
		res.setEsito(Esito.SUCCESSO);
		
	}

}

