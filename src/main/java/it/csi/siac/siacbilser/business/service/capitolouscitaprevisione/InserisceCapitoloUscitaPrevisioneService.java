/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitaprevisione;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.CrudCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceCapitoloUscitaPrevisioneService extends CrudCapitoloBaseService<InserisceCapitoloDiUscitaPrevisione,InserisceCapitoloDiUscitaPrevisioneResponse> {
	
	/** The capitolo uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;
		
	/** The ricerca puntuale capitolo uscita previsione service. */
	@Autowired
	private RicercaPuntualeCapitoloUscitaPrevisioneService ricercaPuntualeCapitoloUscitaPrevisioneService;
	
	
	@Override
	protected void init() {
		capitoloUscitaPrevisioneDad.setBilancio(req.getBilancio());
		capitoloUscitaPrevisioneDad.setEnte(req.getEnte());
		capitoloUscitaPrevisioneDad.setLoginOperazione(loginOperazione);
	}
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getEnte(), "ente", false);
		this.ente = req.getEnte();
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"),false);
		this.bilancio = req.getBilancio();
	
		
		checkNotNull(req.getCapitoloUscitaPrevisione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"), true);
		checkNotNull(req.getCapitoloUscitaPrevisione().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo anno"), false);
		checkNotNull(req.getCapitoloUscitaPrevisione().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo numero"), false);
		checkNotNull(req.getCapitoloUscitaPrevisione().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo numero articolo"), false);
		checkNotNull(req.getCapitoloUscitaPrevisione().getNumeroUEB(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo numero ueb"), false);
		
		boolean isFondino = isCapitoloFondini(req.getCapitoloUscitaPrevisione().getClassificatoriGenerici());//findCapitoloFondini(req.getCapitoloUscitaPrevisione().getClassificatoriGenerici());
	

		checkFondinoImpegnabile(req.getCapitoloUscitaPrevisione(), isFondino);
		
		
		
		checkEntita(req.getStruttAmmContabile(), "strutturaAmministrativoContabile", false);
		
		checkEntita(req.getCapitoloUscitaPrevisione().getCategoriaCapitolo(), "categoria capitolo", false);
		
		// CR-2204
//		checkNotNull(req.getElementoPianoDeiConti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO	.getErrore("ElementoPianoDeiConti"));
//		checkCondition(req.getElementoPianoDeiConti().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO	.getErrore("ElementoPianoDeiConti uid"),false);
		
//		checkNotNull(req.getMacroaggregato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("macroaggregato"));
//		checkCondition(req.getMacroaggregato().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("macroaggregato uid"),false);
		
//		checkNotNull(req.getProgramma(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("programma"));	
//		checkCondition(req.getProgramma().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("programma uid"),false);
	}

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
	public InserisceCapitoloDiUscitaPrevisioneResponse executeService(InserisceCapitoloDiUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		//SIAC-5007
		checkCapitoloUscitaPrevisioneSeStandard(req.getCapitoloUscitaPrevisione());
		checkCapitoloUscitaPrevisioneSeFPV(req.getCapitoloUscitaPrevisione());
		if (esisteCapitoloUscitaPrevisione()) {
			//Se l'entita' esiste viene ritornato codice di errore ENTITA_PRESENTE
			
			CapitoloUscitaPrevisione cup = req.getCapitoloUscitaPrevisione();
			
			res.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("inserimento",String.format("Capitolo: %s",
					enteGestisceUEB() ? cup.getDescBilancioAnnoNumeroArticoloUEB() : cup.getDescBilancioAnnoNumeroArticolo())));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		checkClassificatoriModificabili();
		checkAttributiModificabili();
		
		// SIAC-6881: Inizializzazione degli importi
		initImporti();
		
		CapitoloUscitaPrevisione cup = capitoloUscitaPrevisioneDad.create(req.getCapitoloUscitaPrevisione());
		//ELIMINA DOPO PROVE e decommenta riga sopra
		//CapitoloUscitaPrevisione cup = new CapitoloUscitaPrevisione();
		res.setCapitoloUPrevInserito(cup);
	}

	/**
	 * SIAC-6881:<br/>
	 * Inizializzazione degli importi
	 */
	private void initImporti() {
		List<ImportiCapitoloUP> listaImportiCapitolo = new ArrayList<ImportiCapitoloUP>();
		listaImportiCapitolo.add(initImportiCapitolo(0));
		listaImportiCapitolo.add(initImportiCapitolo(1));
		listaImportiCapitolo.add(initImportiCapitolo(2));
		req.getCapitoloUscitaPrevisione().setListaImportiCapitolo(listaImportiCapitolo);
		
		req.getCapitoloUscitaPrevisione().setImportiCapitoloUP(listaImportiCapitolo.get(0));
	}
	
	private ImportiCapitoloUP initImportiCapitolo(int deltaAnno) {
		ImportiCapitoloUP icup = new ImportiCapitoloUP();
		icup.setAnnoCompetenza(Integer.valueOf(req.getCapitoloUscitaPrevisione().getAnnoCapitolo().intValue() + deltaAnno));
		return icup;
	}


	/**
	 * Esiste capitolo uscita previsione.
	 *
	 * @return true se esiste
	 */
	private boolean esisteCapitoloUscitaPrevisione() {
		RicercaPuntualeCapitoloUPrev criteriRicerca = new RicercaPuntualeCapitoloUPrev();

		criteriRicerca.setAnnoEsercizio(req.getBilancio().getAnno());
		criteriRicerca.setAnnoCapitolo(req.getCapitoloUscitaPrevisione().getAnnoCapitolo());
		criteriRicerca.setNumeroCapitolo(req.getCapitoloUscitaPrevisione().getNumeroCapitolo());
		criteriRicerca.setNumeroArticolo(req.getCapitoloUscitaPrevisione().getNumeroArticolo());
		criteriRicerca.setNumeroUEB(req.getCapitoloUscitaPrevisione().getNumeroUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(req.getCapitoloUscitaPrevisione().getStatoOperativoElementoDiBilancio());

		RicercaPuntualeCapitoloUscitaPrevisione rpcup = new RicercaPuntualeCapitoloUscitaPrevisione();
		rpcup.setRichiedente(req.getRichiedente());
		rpcup.setEnte(req.getEnte());
		rpcup.setRicercaPuntualeCapitoloUPrev(criteriRicerca);
		
		RicercaPuntualeCapitoloUscitaPrevisioneResponse resRic = executeExternalService(ricercaPuntualeCapitoloUscitaPrevisioneService,rpcup);

		/*
		 * PRIMA ERA: RicercaPuntualeCapitoloUscitaPrevisioneResponse resRic =
		 * businessHandler .ricercaPuntualeCapitoloUscitaPrevisione(richiedente,
		 * ente, criteriRicerca);
		 */

		return resRic.getCapitoloUscitaPrevisione() != null;
	}
	
	/**
	 * Controlla se i classificatori che si stanno inserendo sono compatibili con quanto previsto in configurazione.
	 */
	private void checkClassificatoriModificabili() {
		Map<TipologiaClassificatore, Integer> classificatoriDaInserire = caricaClassificatoriDaInserire(req.getClassificatoriGenerici(),
				req.getProgramma(), req.getClassificazioneCofogProgramma(),
				req.getMacroaggregato(), req.getElementoPianoDeiConti(), req.getCapitoloUscitaPrevisione().getSiopeSpesa(),
				req.getStruttAmmContabile(),
				req.getTipoFinanziamento(), req.getTipoFondo(),
				// Aggiunti fase 4
				req.getCapitoloUscitaPrevisione().getRicorrenteSpesa(), req.getCapitoloUscitaPrevisione().getPerimetroSanitarioSpesa(),
				req.getCapitoloUscitaPrevisione().getTransazioneUnioneEuropeaSpesa(), req.getCapitoloUscitaPrevisione().getPoliticheRegionaliUnitarie());
		
		checkClassificatoriModificabiliInserimento(classificatoriDaInserire, req.getCapitoloUscitaPrevisione());
	}
	

	/**
	 * Controlla se i classificatori che si stanno inserendo sono compatibili con quanto previsto in configurazione.
	 */
	private void checkAttributiModificabili() {
		checkAttributiModificabiliInserimento(req.getCapitoloUscitaPrevisione());
	}

	

}
