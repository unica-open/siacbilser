/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.CrudCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class InserisceCapitoloDiUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceCapitoloDiUscitaGestioneService 
	extends CrudCapitoloBaseService<InserisceCapitoloDiUscitaGestione, InserisceCapitoloDiUscitaGestioneResponse> {

	/** The capitolo uscita gestione dad. */
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	
	/** The ricerca puntuale capitolo uscita gestione service. */
	@Autowired
	private RicercaPuntualeCapitoloUscitaGestioneService ricercaPuntualeCapitoloUscitaGestioneService;
	
	@Override
	protected void init() {
		capitoloUscitaGestioneDad.setBilancio(req.getBilancio());
		capitoloUscitaGestioneDad.setEnte(req.getEnte());
		capitoloUscitaGestioneDad.setLoginOperazione(loginOperazione);
	}
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
			
		checkEntita(req.getEnte(), "ente", false);
		this.ente = req.getEnte();
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"),false);
		this.bilancio = req.getBilancio();
		
		checkNotNull(req.getCapitoloUscitaGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaGestione"), true);
		checkNotNull(req.getCapitoloUscitaGestione().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo anno"), false);
		checkNotNull(req.getCapitoloUscitaGestione().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo numero"), false);
		checkNotNull(req.getCapitoloUscitaGestione().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo numero articolo"), false);
		checkNotNull(req.getCapitoloUscitaGestione().getNumeroUEB(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo numero ueb"), false);
		
		checkEntita(req.getStruttAmmContabile(), "strutturaAmministrativoContabile", false);
		
		checkEntita(req.getCapitoloUscitaGestione().getCategoriaCapitolo(), "categoria capitolo");
		//SIAC-6884
		boolean isFondino = isCapitoloFondini(req.getCapitoloUscitaGestione().getClassificatoriGenerici());
		checkFondinoImpegnabile(req.getCapitoloUscitaGestione(), isFondino);
		
		
		
		// CR 2204
//		checkNotNull(req.getElementoPianoDeiConti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO	.getErrore("ElementoPianoDeiConti"));
//		checkCondition(req.getElementoPianoDeiConti().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO	.getErrore("ElementoPianoDeiConti uid"),false);
		
//		checkNotNull(req.getMacroaggregato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("macroaggregato"));
//		checkCondition(req.getMacroaggregato().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("macroaggregato uid"),false);
				
//		checkNotNull(req.getProgramma(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("programma"));	
//		checkCondition(req.getProgramma().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("programma uid"),false);
		
		//res.addErrore(errore) per casi particolari
	}
	//SIAC-6884
	private boolean isCapitoloFondini(List<ClassificatoreGenerico> classificatoriGenerici) {
		if(classificatoriGenerici.size()==0){
			return false;
		}else{
			for(ClassificatoreGenerico cg : classificatoriGenerici){
				if(cg.getTipoClassificatore().getCodice().equals(TipologiaClassificatore.CLASSIFICATORE_3.name()) && cg.getCodice().equals("01")){
					return true;
				}	
			}
		}
		return false;
	}
	
	@Transactional
	public InserisceCapitoloDiUscitaGestioneResponse executeService(InserisceCapitoloDiUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		//SIAC-7722 Pulisco le descrizioni da eventuali "a capo"
		req.setCapitoloUscitaGestione(pulisciDescrizioni(req.getCapitoloUscitaGestione()));
		
		//SIAC-5007
		checkCapitoloUscitaGestioneSeFPV(req.getCapitoloUscitaGestione());
		checkCapitoloUscitaGestioneSeStandard(req.getCapitoloUscitaGestione());

		if (esisteCapitoloUscitaGestione()) {
			//Se l'entita' esiste viene ritornato codice di errore ENTITA_PRESENTE
			
			CapitoloUscitaGestione cap = req.getCapitoloUscitaGestione();
			
			res.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("inserimento", String.format("Capitolo: %s", 
					enteGestisceUEB()? cap.getDescBilancioAnnoNumeroArticoloUEB(): cap.getDescBilancioAnnoNumeroArticolo()
					//,cup.getStatoOperativoElementoDiBilancio().toString()
					)));
			res.setEsito(Esito.FALLIMENTO);
			return;
			
		}
		
		checkClassificatoriModificabili();
		checkAttributiModificabili();
		
		// SIAC-6881: Inizializzazione degli importi
		initImporti();
		
		
		CapitoloUscitaGestione cug = capitoloUscitaGestioneDad.create(req.getCapitoloUscitaGestione());

		res.setCapitoloUscitaGestione(cug);
	}
	
	/**
	 * SIAC-6881:<br/>
	 * Inizializzazione degli importi
	 */
	private void initImporti() {
		List<ImportiCapitoloUG> listaImportiCapitolo = new ArrayList<ImportiCapitoloUG>();
		listaImportiCapitolo.add(initImportiCapitolo(0));
		listaImportiCapitolo.add(initImportiCapitolo(1));
		listaImportiCapitolo.add(initImportiCapitolo(2));
		req.getCapitoloUscitaGestione().setListaImportiCapitolo(listaImportiCapitolo);
		
		req.getCapitoloUscitaGestione().setImportiCapitoloUG(listaImportiCapitolo.get(0));
	}
	
	private ImportiCapitoloUG initImportiCapitolo(int deltaAnno) {
		ImportiCapitoloUG icug = new ImportiCapitoloUG();
		icug.setAnnoCompetenza(Integer.valueOf(req.getCapitoloUscitaGestione().getAnnoCapitolo().intValue() + deltaAnno));
		return icug;
	}

	/**
	 * Esiste capitolo uscita gestione.
	 *
	 * @return true se esiste
	 */
	private boolean esisteCapitoloUscitaGestione() {

		RicercaPuntualeCapitoloUGest criteriRicerca = new RicercaPuntualeCapitoloUGest();

		criteriRicerca.setAnnoEsercizio(req.getBilancio().getAnno());
		criteriRicerca.setAnnoCapitolo(req.getCapitoloUscitaGestione().getAnnoCapitolo());		
		criteriRicerca.setNumeroCapitolo(req.getCapitoloUscitaGestione().getNumeroCapitolo());
		criteriRicerca.setNumeroArticolo(req.getCapitoloUscitaGestione().getNumeroArticolo());
		criteriRicerca.setNumeroUEB(req.getCapitoloUscitaGestione().getNumeroUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(req.getCapitoloUscitaGestione().getStatoOperativoElementoDiBilancio());

		RicercaPuntualeCapitoloUscitaGestione rpcug = new RicercaPuntualeCapitoloUscitaGestione();
		rpcug.setRichiedente(req.getRichiedente());
		rpcug.setEnte(req.getEnte());
		rpcug.setRicercaPuntualeCapitoloUGest(criteriRicerca);
		
		
		RicercaPuntualeCapitoloUscitaGestioneResponse resRic = executeExternalService(ricercaPuntualeCapitoloUscitaGestioneService,rpcug);

		/*
		 * PRIMA ERA: RicercaPuntualeCapitoloUscitaGestioneResponse resRic =
		 * businessHandler .ricercaPuntualeCapitoloUscitaGestione(richiedente,
		 * ente, criteriRicerca);
		 */

		return resRic.getCapitoloUscitaGestione() != null;
	}
	
	
	/**
	 *  
	 * Controlla se i classificatori che si stanno inserendo sono compatibili con quanto previsto in configurazione.
	 */
	private void checkClassificatoriModificabili() {		
		Map<TipologiaClassificatore, Integer> classificatoriDaInserire = caricaClassificatoriDaInserire(req.getClassificatoriGenerici(),
				req.getProgramma(), req.getClassificazioneCofogProgramma(),
				req.getMacroaggregato(), req.getElementoPianoDeiConti(), req.getCapitoloUscitaGestione().getSiopeSpesa(),
				req.getStruttAmmContabile(),
				req.getTipoFinanziamento(), req.getTipoFondo(),
				// Aggiunti fase 4
				req.getCapitoloUscitaGestione().getRicorrenteSpesa(), req.getCapitoloUscitaGestione().getPerimetroSanitarioSpesa(),
				req.getCapitoloUscitaGestione().getTransazioneUnioneEuropeaSpesa(), req.getCapitoloUscitaGestione().getPoliticheRegionaliUnitarie());
		
		checkClassificatoriModificabiliInserimento(classificatoriDaInserire, req.getCapitoloUscitaGestione());		
	}
	

	/**
	 *  
	 * Controlla se i classificatori che si stanno inserendo sono compatibili con quanto previsto in configurazione.
	 */
	private void checkAttributiModificabili() {			
		checkAttributiModificabiliInserimento(req.getCapitoloUscitaGestione());		
	}


	
	
}
