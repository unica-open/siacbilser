/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Constants;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.CategoriaCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Fornisce funzionalità di base ai servizi di Inserimento (C),  Ricerca (R), Aggiornamento (U), Elimina (D) di un Capitolo.
 *
 * @author Domenico
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class CrudCapitoloBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ,RES> {
	
//	/** The ente. */
//	protected Ente ente;
	
	/** The bilancio. */
	protected Bilancio bilancio;
	
	/** The controlla classificatori modificabili capitolo service. */
	@Autowired
	protected ControllaClassificatoriModificabiliCapitoloService controllaClassificatoriModificabiliCapitoloService;
	
	/** The controlla attributi modificabili capitolo service. */
	@Autowired
	protected ControllaAttributiModificabiliCapitoloService controllaAttributiModificabiliCapitoloService;
	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;
	

	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	
	/**
	 * Controlla se la lista dei classificatori per il capitolo che si vuole inserire è conentita o meno.
	 *
	 * @param classificatoriDaInserire the classificatori da inserire
	 * @param capitolo the capitolo
	 */
	protected void checkClassificatoriModificabiliInserimento(Map<TipologiaClassificatore, Integer> classificatoriDaInserire, Capitolo<?, ?> capitolo) {
		Map<TipologiaClassificatore, Integer> classificatoriNonModificabili = findClassificatoriNonModificabili(capitolo, false);	
		checkClassificatoriModificabili(classificatoriNonModificabili, classificatoriDaInserire);
	}
	
	/**
	 * Controlla se gli attributi del capitolo che si sta inserendo sono modificabili. 
	 *
	 * @param capitolo the capitolo
	 */
	protected void checkAttributiModificabiliInserimento(Capitolo<?, ?> capitolo) {
		Map<TipologiaAttributo, Object> attributiNonModificabili = findAttributiNonModificabili(capitolo, false);
		Map<TipologiaAttributo, Object> attributiDaInserire = SiacTAttrEnum.getTipologiaAttributoValueMapByType(capitolo);
		checkAttributiModificabili(attributiNonModificabili, attributiDaInserire);
	}
	
	/**
	 * Controlla se la lista dei classificatori per il capitolo che si vuole aggiornare è conentita o meno.
	 * 
	 * Come checkClassificatoriModificabili ma in più permette di specifiare se il controllo è inteso per effettuare un aggiornamento 
	 * (modalitaAggiornamento = true) o un inserimento (modalitaAggiornamento=false che è il dafault)
	 *
	 * @param classificatoriDaInserire the classificatori da inserire
	 * @param capitolo the capitolo
	 */
	protected void checkClassificatoriModificabiliAggiornamento(Map<TipologiaClassificatore, Integer> classificatoriDaInserire, Capitolo<?, ?> capitolo) {
		Map<TipologiaClassificatore, Integer> classificatoriNonModificabili = findClassificatoriNonModificabili(capitolo, true);			
		checkClassificatoriModificabili(classificatoriNonModificabili, classificatoriDaInserire);
	}
	
	/**
	 * Controlla se gli attributi del capitolo che si sta aggiornando sono modificabili.
	 *
	 * @param capitolo the capitolo
	 */
	protected void checkAttributiModificabiliAggiornamento(Capitolo<?, ?> capitolo) {
		Map<TipologiaAttributo, Object> attributiNonModificabili = findAttributiNonModificabili(capitolo, true);
		Map<TipologiaAttributo, Object> attributiDaInserire = SiacTAttrEnum.getTipologiaAttributoValueMapByType(capitolo, true);
		checkAttributiModificabili(attributiNonModificabili, attributiDaInserire);
	}
	
	
	
	
	/**
	 * Controlla se ogni singolo classificatore della lista dei classificatoriDaInserire qualora presente non sia diverso da 
	 * quello corrispondente nella lista dei classificatoriNonModificabili.
	 *
	 * @param classificatoriNonModificabili the classificatori non modificabili
	 * @param classificatoriDaInserire the classificatori da inserire
	 */
	protected void checkClassificatoriModificabili(Map<TipologiaClassificatore, Integer> classificatoriNonModificabili,
			Map<TipologiaClassificatore, Integer> classificatoriDaInserire) {
		
		final String methodName = "checkClassificatoriModificabili";
		
		List<TipologiaClassificatore> classificatoriNonValidi = new ArrayList<TipologiaClassificatore>();
		
		if (classificatoriNonModificabili!=null && !classificatoriNonModificabili.isEmpty()){
			for(Entry<TipologiaClassificatore, Integer> e : classificatoriNonModificabili.entrySet()) {
				TipologiaClassificatore tipologiaClassificatore = e.getKey();
				Integer classifId = e.getValue();

				Integer classifIdDaInserire = classificatoriDaInserire.get(tipologiaClassificatore);
				
				log.info(methodName, "tipologiaClassificatore: "+ tipologiaClassificatore + " classifIdDaInserire: "+ classifIdDaInserire + " classifId: "+classifId);
				
				if((classifId==null && classifIdDaInserire!=null) || (classifId!=null && !classifId.equals(classifIdDaInserire))){				
					classificatoriNonValidi.add(tipologiaClassificatore);
				}			
			}
			
			if(!classificatoriNonValidi.isEmpty() ){
				List<String> codiciClassificatoriNonValidi = extractDenominazioneClassificatori(classificatoriNonValidi);
				throw new BusinessException(ErroreBil.CLASSIFICATORI_NON_MODIFICABILI.getErrore(Utility.listToCommaSeparatedFullyCapitalizedString(codiciClassificatoriNonValidi)));
			}
		}
	}
	
	private List<String> extractDenominazioneClassificatori(Collection<TipologiaClassificatore> classificatoriNonValidi) {
		return classificatoriDad.findDescrizioniTipoClassificatoreByTipoAndEnte(classificatoriNonValidi, bilancio.getAnno(), ente.getUid());
	}
	
	/**
	 * Controlla se ogni singolo attributo della lista dei attributiDaInserire qualora presente non sia diverso da 
	 * quello corrispondente nella lista dei attributiDaInserire.
	 *
	 * @param attributiNonModificabili the attributi non modificabili
	 * @param attributiDaInserire the attributi da inserire
	 */
	private void checkAttributiModificabili(Map<TipologiaAttributo, Object> attributiNonModificabili,
			Map<TipologiaAttributo, Object> attributiDaInserire) {
		
		final String methodName = "checkAttributiModificabili";
		
		List<TipologiaAttributo> attributiNonValidi = new ArrayList<TipologiaAttributo>();
		
		for (Entry<TipologiaAttributo, Object> e : attributiNonModificabili.entrySet()) {
			TipologiaAttributo tipologiaAttributo = e.getKey();
			Object valoreAttributoNonModificabile = e.getValue();
			
			Object valoreAttributoDaInserire = attributiDaInserire.get(tipologiaAttributo);
			
			log.info(methodName, "tipologiaAttributo: "+ tipologiaAttributo + " valoreAttributoDaInserire: "+ valoreAttributoDaInserire + " valoreAttributoNonModificabile: "+valoreAttributoNonModificabile);
			
			if((valoreAttributoNonModificabile == null && valoreAttributoDaInserire!= null) 
					|| valoreAttributoNonModificabile!=null && !valoreAttributoNonModificabile.equals(valoreAttributoDaInserire)){
				
				attributiNonValidi.add(tipologiaAttributo);//, valoreAttributoNonModificabile);
			}			
		}
		
		if(!attributiNonValidi.isEmpty()){
			throw new BusinessException(ErroreBil.ATTRIBUTI_NON_MODIFICABILI.getErrore(TipologiaAttributo.toStringDescrizione(attributiNonValidi)));
		}
	}
	
	
	/**
	 * Carica classificatori da inserire.
	 *
	 * @param listaClassificatoriGenerici the lista classificatori generici
	 * @param listaClassificatori the lista classificatori
	 * @return the map
	 */
	protected Map<TipologiaClassificatore, Integer> caricaClassificatoriDaInserire(List<ClassificatoreGenerico> listaClassificatoriGenerici, Codifica... listaClassificatori) {
		final String methodName = "caricaClassificatoriDaInserire";
		Map<TipologiaClassificatore, Integer> a = new HashMap<TipologiaClassificatore, Integer>();
		
		if(listaClassificatori!=null){
			for(Codifica cg : listaClassificatori){
				a.putAll(classificatoriDad.ricercaMappaClassificatori(cg));
			}
		}
		
		if(listaClassificatoriGenerici!=null){
			for(ClassificatoreGenerico cg : listaClassificatoriGenerici){
				a.putAll(classificatoriDad.ricercaMappaClassificatori(cg));
			}
		}	
		
		
		log.info(methodName, "returning map: "+a);
		return a;
	}
	
	/**
	 * Ricerca tutti i classificatori non modificabili associati ad un capitolo.
	 *
	 * @param cap the cap
	 * @param modalitaAggiornamento aggiornamento= true in caso di aggiornamento, false in caso di inserimento
	 * @return Mappa con chiave il tipo di classificatore e valore l'id del classificatore
	 */
	protected Map<TipologiaClassificatore, Integer> findClassificatoriNonModificabili(Capitolo<?, ?> cap, boolean modalitaAggiornamento) {
		ControllaClassificatoriModificabiliCapitolo ccreq = new ControllaClassificatoriModificabiliCapitolo();
		ccreq.setBilancio(bilancio);
		ccreq.setRichiedente(req.getRichiedente());
		ccreq.setEnte(ente);
		ccreq.setNumeroCapitolo(cap.getNumeroCapitolo());
		ccreq.setNumeroArticolo(cap.getNumeroArticolo());
		ccreq.setNumeroUEB(cap.getNumeroUEB());
		ccreq.setTipoCapitolo(cap.getTipoCapitolo());
		ccreq.setModalitaAggiornamento(modalitaAggiornamento);
		
		ControllaClassificatoriModificabiliCapitoloResponse ccres = executeExternalServiceSuccess(controllaClassificatoriModificabiliCapitoloService,ccreq);
		// Controllo se il classificatore è unico: in tal caso, ogni classificatore sarà modificabile
		long stessoNumCapArt = ccres.getStessoNumCapArt() != null ? ccres.getStessoNumCapArt().longValue() : 0L;
		long stessoNumCap = ccres.getStessoNumCap() != null ? ccres.getStessoNumCap().longValue() : 0L;
		//SIAC-5836
		boolean unico =false;
		if (isGestioneUEB()){
		 unico = stessoNumCapArt <= 1L;
		}else{
			unico = stessoNumCap <= 1L;
		}
		Map<TipologiaClassificatore, Integer> classificatoriNonModificabili = ccres.getClassificatoriNonModificabili();

		if(modalitaAggiornamento && unico) {
			return null;	
		} else {
			return classificatoriNonModificabili;	
		}		
	}
	

	/**
	 * Ricerca tutti i classificatori non modificabili associati ad un capitolo.
	 *
	 * @param cap the cap
	 * @param modalitaAggiornamento aggiornamento= true in caso di aggiornamento, false in caso di inserimento
	 * @return Mappa con chiave il tipo di classificatore e valore l'id del classificatore
	 */
	protected Map<TipologiaAttributo, Object> findAttributiNonModificabili(Capitolo<?, ?> cap, boolean modalitaAggiornamento) {
		ControllaAttributiModificabiliCapitolo careq = new ControllaAttributiModificabiliCapitolo();
		careq.setBilancio(bilancio);
		careq.setRichiedente(req.getRichiedente());
		careq.setEnte(ente);
		careq.setNumeroCapitolo(cap.getNumeroCapitolo());
		careq.setNumeroArticolo(cap.getNumeroArticolo());
		careq.setNumeroUEB(cap.getNumeroUEB());
		careq.setTipoCapitolo(cap.getTipoCapitolo());
		careq.setModalitaAggiornamento(modalitaAggiornamento);
		
		ControllaAttributiModificabiliCapitoloResponse cares = executeExternalServiceSuccess(controllaAttributiModificabiliCapitoloService,careq);
		
		Map<TipologiaAttributo, Object> attributiNonModificabili = cares.getAttributiNonModificabili();
		return attributiNonModificabili;
	}
	
	
	/**
	 * Ente gestisce ueb.
	 *
	 * @return true se l'ente gestise le ueb
	 */
	protected boolean enteGestisceUEB() {
		final String methodName = "enteGestisceUEB";
		try {
			Map<TipologiaGestioneLivelli, String> mapLivelli = ente.getGestioneLivelli();
			String tipologiaGestioneLivelli = mapLivelli.get(TipologiaGestioneLivelli.LIVELLO_GESTIONE_BILANCIO);
			return "GESTIONE_UEB".equals(tipologiaGestioneLivelli);
		} catch (NullPointerException npe) {
			// Valutare se sollevare un eccezione!
			log.warn(methodName, "ente senza Gestione UEB. Returning false", npe);
			return false;
		}
	}
	

	/**
	 * Controlla la classificazione del capitolo la categoria &eacute; FPV.
	 * 
	 * @param capitoloUscitaPrevisione il capitolo da controllare
	 */
	protected void checkCapitoloUscitaPrevisioneSeFPV(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		checkClassificazioneBilancio( capitoloUscitaPrevisione, Constants.CATEGORIA_CAPITOLO_FPV.getValue());
	}
	
	/**
	 * Controlla la classificazione del capitolo la categoria &eacute; standard.
	 * 
	 * @param capitoloUscitaPrevisione il capitolo da controllare
	 */
	protected void checkCapitoloUscitaPrevisioneSeStandard(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		checkClassificazioneBilancio( capitoloUscitaPrevisione, Constants.CATEGORIA_CAPITOLO_STANDARD.getValue());
	}

	
	/**
	 * Controlla la classificazione del capitolo a fronte della tipologia passata
	 * 
	 * @param capitoloUscitaPrevisione il capitolo da controllare
	 */
	protected void checkClassificazioneBilancio(CapitoloUscitaPrevisione capitoloUscitaPrevisione,String tipologia) {
		// Controllo la categoria capitolo
		//CategoriaCapitolo categoriaCapitoloStandard = classificatoriDad.findCategoriaCapitoloStandard(ente.getUid());
		CategoriaCapitolo categoriaCapitolo = classificatoriDad.findCategoriaCapitolo(ente.getUid(),tipologia);
		if(categoriaCapitolo == null || categoriaCapitolo.getUid() != capitoloUscitaPrevisione.getCategoriaCapitolo().getUid()) {
			// Le categorie sono differenti. Va bene.
			return;
		}
		
		checkClassificatore(capitoloUscitaPrevisione.getMissione(), "Missione");
		checkClassificatore(capitoloUscitaPrevisione.getProgramma(), "Programma");
		checkClassificatore(capitoloUscitaPrevisione.getTitoloSpesa(), "Titolo");
		checkClassificatore(capitoloUscitaPrevisione.getMacroaggregato(), "Macroaggregato");
		checkClassificatore(capitoloUscitaPrevisione.getElementoPianoDeiConti(), "Elemento piano dei conti");
		
		if(res.hasErrori()) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("necessario compilare la classificazione del capitolo"));
		}
	}
		
	
	/**
	 * Controlla la classificazione del capitolo la categoria &eacute; standard.
	 * 
	 * @param capitoloUscitaGestione il capitolo da controllare
	 */
	protected void checkCapitoloUscitaGestioneSeStandard(CapitoloUscitaGestione capitoloUscitaGestione) {
		checkClassificazioneBilancio( capitoloUscitaGestione,CategoriaCapitoloEnum.STD.getCodice());
	}	
	
	
	/**
	 * Controlla la classificazione del capitolo la categoria &eacute; FPV.
	 * 
	 * @param capitoloUscitaGestione il capitolo da controllare
	 */
	protected void checkCapitoloUscitaGestioneSeFPV(CapitoloUscitaGestione capitoloUscitaGestione) {
		checkClassificazioneBilancio( capitoloUscitaGestione,CategoriaCapitoloEnum.FPV.getCodice());
	}	
	
	
	/**
	 * Controlla la classificazione del capitolo data la tipologia.
	 * 
	 * @param capitoloUscitaGestione il capitolo da controllare
	 */
	protected void checkClassificazioneBilancio(CapitoloUscitaGestione capitoloUscitaGestione,String tipologia) {
		// Controllo la categoria capitolo
		CategoriaCapitolo categoriaCapitolo = classificatoriDad.findCategoriaCapitolo(ente.getUid(),tipologia);
		if(categoriaCapitolo == null || categoriaCapitolo.getUid() != capitoloUscitaGestione.getCategoriaCapitolo().getUid()) {
			// Le categorie sono differenti. Va bene.
			return;
		}
		
		checkClassificatore(capitoloUscitaGestione.getMissione(), "Missione");
		checkClassificatore(capitoloUscitaGestione.getProgramma(), "Programma");
		checkClassificatore(capitoloUscitaGestione.getTitoloSpesa(), "Titolo");
		checkClassificatore(capitoloUscitaGestione.getMacroaggregato(), "Macroaggregato");
		checkClassificatore(capitoloUscitaGestione.getElementoPianoDeiConti(), "Elemento piano dei conti");
		
		if(res.hasErrori()) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("necessario compilare la classificazione del capitolo"));
		}
	}	
	
	
	
	
	/**
	 * Controlla la classificazione del bilancio la categoria &eacute; standard.
	 * 
	 * @param capitoloEntrataPrevisione il capitolo da controllare
	 */
	protected void checkClassificazioneBilancioSeStandard(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		// Controllo la categoria capitolo
		CategoriaCapitolo categoriaCapitoloStandard = classificatoriDad.findCategoriaCapitoloStandard(ente.getUid());
		if(categoriaCapitoloStandard == null || categoriaCapitoloStandard.getUid() != capitoloEntrataPrevisione.getCategoriaCapitolo().getUid()) {
			// Le categorie sono differenti. Va bene.
			return;
		}
		
		checkClassificatore(capitoloEntrataPrevisione.getTitoloEntrata(), "Titolo");
		checkClassificatore(capitoloEntrataPrevisione.getTipologiaTitolo(), "Tipologia");
		checkClassificatore(capitoloEntrataPrevisione.getCategoriaTipologiaTitolo(), "Categoria");
		checkClassificatore(capitoloEntrataPrevisione.getElementoPianoDeiConti(), "Elemento piano dei conti");
		
		if(res.hasErrori()) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("necessario compilare la classificazione del capitolo"));
		}
	}
	
	/**
	 * Controlla la classificazione del bilancio la categoria &eacute; standard.
	 * 
	 * @param capitoloEntrataGestione il capitolo da controllare
	 */
	protected void checkClassificazioneBilancioSeStandard(CapitoloEntrataGestione capitoloEntrataGestione) {
		// Controllo la categoria capitolo
		CategoriaCapitolo categoriaCapitoloStandard = classificatoriDad.findCategoriaCapitoloStandard(ente.getUid());
		if(categoriaCapitoloStandard == null || categoriaCapitoloStandard.getUid() != capitoloEntrataGestione.getCategoriaCapitolo().getUid()) {
			// Le categorie sono differenti. Va bene.
			return;
		}
		
		checkClassificatore(capitoloEntrataGestione.getTitoloEntrata(), "Titolo");
		checkClassificatore(capitoloEntrataGestione.getTipologiaTitolo(), "Tipologia");
		checkClassificatore(capitoloEntrataGestione.getCategoriaTipologiaTitolo(), "Categoria");
		checkClassificatore(capitoloEntrataGestione.getElementoPianoDeiConti(), "Elemento piano dei conti");
		
		if(res.hasErrori()) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("necessario compilare la classificazione del capitolo"));
		}
	}
	
	/**
	 * Controlla il classificatore
	 * @param classificatore il classificatore da controllare
	 * @param nome il nome del classificatore
	 */
	private void checkClassificatore(Codifica classificatore, String nome) {
		if(classificatore == null || classificatore.getUid() == 0) {
			res.addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nome));
		}
	}
	
	 /**
	    * Il capitolo &eacute; modificabile solo se lo stato operativo &eacute; di tipo valido o provvisorio
	    * 
	    * @param cegAttuale
	    */
		protected void checkCapitoloModificabilePerAggiornamento(Capitolo<?, ?> cegAttuale) {
			log.debug("checkCapitoloModificabilePerAggiornamento", "START");
			StatoOperativoElementoDiBilancio statoOperativo = cegAttuale.getStatoOperativoElementoDiBilancio();
			if (!StatoOperativoElementoDiBilancio.VALIDO.equals(statoOperativo) && !StatoOperativoElementoDiBilancio.PROVVISORIO.equals(statoOperativo)){
				throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore(cegAttuale.getDescBilancioAnnoNumeroArticolo(), statoOperativo));
			}		
			log.debug("checkCapitoloModificabilePerAggiornamento", "STOP");
		}
		
		/**
		 * SIAC-7722.
		 *
		 * Metodo di controllo delle descrizioni del capitolo
		 * rimuovendo il pattern "\r\n" utilizzato come "a capo"
		 * 
		 * @param <C> il capitolo
		 * @return il capitolo
		 */
		protected <C extends Capitolo<?,?>> C pulisciDescrizioni(C capitolo) {
			if(StringUtils.isNotBlank(capitolo.getDescrizione())) {
				capitolo.setDescrizione((capitolo.getDescrizione().replace("\r", " ").replace("\n", " ")));
			}
			if(StringUtils.isNotBlank(capitolo.getDescrizioneArticolo())) {
				capitolo.setDescrizioneArticolo(capitolo.getDescrizioneArticolo().replace("\r", " ").replace("\n", " "));
			}
			return capitolo;
		}
		
		//SIAC-6884 controlli 		
		protected void checkFondinoImpegnabile(Capitolo<?, ?> cup, boolean isFondino) throws ServiceParamError{
			if(isFondino){
				if(cup.getFlagImpegnabile() != null && cup.getFlagImpegnabile()==true){
					checkCondition(false, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Non è possibile salvare un Capitolo Fondino e Impegnabile"), true);
				}
				
			}
			
		}
		
//		//SIAC-6884 NuovoCapitolo in InserisciVariazione
//		protected void checkDecentratoPerNuovoCapitolo(boolean isDecentrato) throws ServiceParamError{
//			
//			if(isDecentrato){
//				checkCondition(false, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Operatore decentrato"));
//			}
//
//		}
		
		

}
