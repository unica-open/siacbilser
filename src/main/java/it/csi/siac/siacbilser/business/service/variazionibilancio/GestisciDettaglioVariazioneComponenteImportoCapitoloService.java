/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.componenteimpcap.InserisceComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.GestisciDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.GestisciDettaglioVariazioneComponenteImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ComponenteImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.TipoComponenteImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccommon.util.cache.Cache;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siaccommon.util.cache.MapCache;
import it.csi.siac.siaccommon.util.cache.initializer.MapCacheElementInitializer;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.CapitoloModelDetail;

/**
 * Gestione del dettaglio della variazione componente importi capitolo
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/10/2019
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GestisciDettaglioVariazioneComponenteImportoCapitoloService extends CheckedAccountBaseService<GestisciDettaglioVariazioneComponenteImportoCapitolo, GestisciDettaglioVariazioneComponenteImportoCapitoloResponse> {

	// Data Access Delegates
	@Autowired private CapitoloDad capitoloDad;
	@Autowired private ImportiCapitoloDad importiCapitoloDad;
	@Autowired private ComponenteImportiCapitoloDad componenteImportiCapitoloDad;
	@Autowired private VariazioniDad variazioniDad;
	@Autowired private TipoComponenteImportiCapitoloDad tipoComponenteImportiCapitoloDad;
	
	// Services
	@Autowired private InserisceComponenteImportiCapitoloService inserisceComponenteImportiCapitoloService;
	@Autowired private InserisciDettaglioVariazioneImportoCapitoloService inserisciDettaglioVariazioneImportoCapitoloService;
	@Autowired private AggiornaDettaglioVariazioneImportoCapitoloService aggiornaDettaglioVariazioneImportoCapitoloService;
	
	// Dati di appoggio
	private final Cache<Integer, ComponenteImportiCapitolo> mappaComponentiImportoCapitoloCollegateCapitoloEVariazionePerComponente = new MapCache<Integer, ComponenteImportiCapitolo>();
	private final Cache<Integer, ComponenteImportiCapitolo> mappaComponentiImportoCapitoloCollegateCapitoloPerComponente = new MapCache<Integer, ComponenteImportiCapitolo>();
	private final Cache<Integer, Map<Integer, ComponenteImportiCapitolo>> mappaComponentiImportoCapitoloCollegateCapitoloEVariazionePerTipo = new MapCache<Integer, Map<Integer, ComponenteImportiCapitolo>>();
	private final Cache<Integer, DettaglioVariazioneComponenteImportoCapitolo> mappaDettaglioVariazioneComponentiImportoCapitoloCollegate = new MapCache<Integer, DettaglioVariazioneComponenteImportoCapitolo>();
	
	private final CacheElementInitializer<Integer, Map<Integer, ComponenteImportiCapitolo>> mappaComponentiImportoCapitoloCollegateCapitoloEVariazionePerTipoCacheInitializer = new MapCacheElementInitializer<Integer, Integer, ComponenteImportiCapitolo>();
	
	private Capitolo<?, ?> capitolo;
	private DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo;
	private int index;
	
	private boolean capitoloFondino = false;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "Bilancio", false);
		checkCondition(req.getBilancio() == null || req.getBilancio().getAnno() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno bilancio"), false);
		
		checkNotNull(req.getDettaglioVariazioneImportoCapitolo(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Dettaglio variazione importo capitolo"));
		checkNotNull(req.getDettaglioVariazioneImportoCapitolo().getStanziamentoCassa(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Stanziamento cassa dettaglio variazione importo capitolo"), false);
		checkNotNull(req.getDettaglioVariazioneImportoCapitolo().getStanziamentoResiduo(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Stanziamento residuo dettaglio variazione importo capitolo"), false);
		checkEntita(req.getDettaglioVariazioneImportoCapitolo().getCapitolo(), "Capitolo dettaglio variazione importo capitolo", false);
		checkEntita(req.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo(), "Variazione importo capitolo dettaglio variazione importo capitolo", false);
		
		checkCondition(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo() != null
				&& !req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo().isEmpty(),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Lista dettaglio componente per delta 0"), true);
		checkCondition(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo1() != null
				&& !req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo1().isEmpty(),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Lista dettaglio componente per delta 1"), true);
		checkCondition(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo2() != null
				&& !req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo2().isEmpty(),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Lista dettaglio componente per delta 2"), true);
		
		// Le tre liste devono avere la stessa dimensione
		checkCondition(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo().size() == req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo1().size()
				&& req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo().size() == req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo2().size(), 
			ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("le tre liste di componenti devono avere lo stesso numero di record"));
		checkServiceParamListaDettaglioVariazioneComponenteImportoCapitolo(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo(), 0);
		checkServiceParamListaDettaglioVariazioneComponenteImportoCapitolo(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo1(), 1);
		checkServiceParamListaDettaglioVariazioneComponenteImportoCapitolo(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo2(), 2);
	}

	/**
	 * Controlli sui dettagli forniti
	 * @param dettagli i dettagli forniti
	 * @param delta il delta
	 * @throws ServiceParamError in caso di fallimento nella validazione
	 */
	private void checkServiceParamListaDettaglioVariazioneComponenteImportoCapitolo(List<DettaglioVariazioneComponenteImportoCapitolo> dettagli, int delta) throws ServiceParamError {
		int i = 0;
		for(DettaglioVariazioneComponenteImportoCapitolo dettaglio : dettagli) {
			checkNotNull(dettaglio.getImporto(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo dettaglio " + i + " per delta " + delta), false);
			checkNotNull(dettaglio.getTipoDettaglioComponenteImportiCapitolo(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo dettaglio " + i + " per delta " + delta), false);
			checkNotNull(dettaglio.getComponenteImportiCapitolo(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Componente dettaglio " + i + " per delta " + delta), false);
			if(dettaglio.getComponenteImportiCapitolo() != null && dettaglio.getComponenteImportiCapitolo().getUid() == 0) {
				checkEntita(dettaglio.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo(), "Tipo componente dettaglio " + i + " per delta " + delta, false);
			}
			i++;
		}
	}
	
	@Override
	protected void init() {
		variazioniDad.setEnte(ente);
		variazioniDad.setLoginOperazione(loginOperazione);
		variazioniDad.setBilancio(req.getBilancio());
	}
	
	@Override
	@Transactional
	public GestisciDettaglioVariazioneComponenteImportoCapitoloResponse executeService(GestisciDettaglioVariazioneComponenteImportoCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		//SIAC-7217
		checkCapitoloFondino();
		// Inizializzazione dei dati
		initCapitolo();
		initVariazione();
		initComponenteImportoCapitolo();
		initDettaglioVariazioneImportoCapitolo();
		initDettaglioVariazioneComponenteImportoCapitolo();
		
		// Flush data
		variazioniDad.flushAndClear();
		
		// Save/update each componenteImportiCapitolo
		for(DettaglioVariazioneComponenteImportoCapitolo dettaglio : req.getDettaglioVariazioneImportoCapitolo().obtainAllListaDettaglioVariazioneComponenteImportoCapitolo()) {
			dettaglio.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
			if(dettaglio.getUid() != 0) {
				updateDettaglioComponente(dettaglio);
			} else {
				insertDettaglioComponente(dettaglio);
			}
		}
		// Eliminazione dei residui
		eliminaDettagliResidui();
	}
	
	private void checkCapitoloFondino() {
		final String methodName="checkCapitoloFondino";
		//SIAC-7217eliminata dipendenza dall'azione in seguito a comunicazioni con Sil default viene impostato a fresco solo se l'utente ha l'azione oinserisci variazione decentrata.. c'è poi anche un errore in fase di salvataggio se l'utente inserisce dele componenti di tipo diverso da fresco, ed anche questo viene lanciato solo se l'utente ha abilitata l'azione decentrata "InsVarDec"
		/*
		Giuseppe, 10:29
		Nell'analisi non e' subordinato all'azione.
		
		10:29
		ok, lo modifico in giornata e lo metto su allora
		
		
		Giuseppe, 10:29
		(intendo dire, nel documento CDU, non si cita l'azione...)
		
		Ok.
		*/
//		if(!isAzioneConsentita(AzioniConsentite.INSERISCI_VARIAZIONE_DECENTRATA.getNomeAzione())) {
//			return;
//		}
		capitoloFondino = capitoloDad.isCapitoloFondino(req.getDettaglioVariazioneImportoCapitolo().getCapitolo().getUid());
		if(!capitoloFondino){
			return;
		}
		
		Long numeroTipiComponentiNonFresco = tipoComponenteImportiCapitoloDad.countTipoComponenteWithMacrotipoDiversoDa(getListaUidTipoComponente(), MacrotipoComponenteImportiCapitolo.FRESCO);
		
		if(Long.valueOf(0).compareTo(numeroTipiComponentiNonFresco)!=0) {
			log.error(methodName, "numero di componenti con macrotipo diverso da quello indicato:" + numeroTipiComponentiNonFresco);
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("lista tipo componenti", ": sono presenti componenti con macrotipo diverso da " + MacrotipoComponenteImportiCapitolo.FRESCO.getDescrizione()));
		}
		
	}

	private List<Integer> getListaUidTipoComponente() {
		List<Integer> uids = new ArrayList<Integer>();
		for (DettaglioVariazioneComponenteImportoCapitolo dd : req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo()) {
			Integer uid = Integer.valueOf(dd.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getUid());
			if(!uids.contains(uid)) {
				uids.add(uid);
			}
		}
		return uids;
	}

	/**
	 * Inizializzazione del capitolo
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initCapitolo() {
		final String methodName = "initCapitolo";
		capitolo = capitoloDad.findOneWithMinimalData(req.getDettaglioVariazioneImportoCapitolo().getCapitolo().getUid(), CapitoloModelDetail.Categoria);
		// Il seguente controllo e' inutile in quanto gia' presente nel findOne. Lo ripeto per chiarezza, e perche' tale controllo e' logicamente di business
		checkBusinessCondition(capitolo != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo", "uid " + req.getDettaglioVariazioneImportoCapitolo().getCapitolo().getUid()));
		// Lettura degli importi per anno di competenza
		ImportiCapitolo importiCapitolo = importiCapitoloDad.findImportiCapitolo(
				capitolo,
				capitolo.getAnnoCapitolo().intValue(),
				capitolo.getTipoCapitolo().getImportiCapitoloClass(),
				// SIAC-7115
				getImportiCapitoloDaCercare(),
				true);
		checkBusinessCondition(importiCapitolo != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Importi", "capitolo " + capitolo.getDescBilancioAnnoNumeroArticolo()));
		// Forzatura causa non corretta reificazione generici
		((Capitolo)capitolo).setImportiCapitolo(importiCapitolo);

		req.getDettaglioVariazioneImportoCapitolo().setCapitolo(capitolo);
		
		// Init componenti by capitolo
		log.debug(methodName, "Calcolo delle componenti relative al capitolo");
		List<ComponenteImportiCapitolo> listaComponentiImportoCapitoloCollegate = componenteImportiCapitoloDad.findComponenteImportiCapitoloByUidCapitoloVariazione(
				capitolo.getUid(),
				req.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo().getUid(),
				ComponenteImportiCapitoloModelDetail.TipoComponenteImportiCapitolo,
				ComponenteImportiCapitoloModelDetail.ImportiCapitolo,
				ComponenteImportiCapitoloModelDetail.Importo);
		log.debug(methodName, "Componenti associate al capitolo (contando quelle in variazione): " + listaComponentiImportoCapitoloCollegate.size());
		// Caching dei dati per ottimizzazione dei dati
		for(ComponenteImportiCapitolo cic : listaComponentiImportoCapitoloCollegate) {
			mappaComponentiImportoCapitoloCollegateCapitoloEVariazionePerComponente.put(cic.getUid(), cic);
			
			Map<Integer, ComponenteImportiCapitolo> perTipo = mappaComponentiImportoCapitoloCollegateCapitoloEVariazionePerTipo.get(cic.getTipoComponenteImportiCapitolo().getUid(), mappaComponentiImportoCapitoloCollegateCapitoloEVariazionePerTipoCacheInitializer);
			perTipo.put(cic.getImportiCapitolo().getAnnoCompetenza(), cic);
		}
		// Calcolo quelle collegate al capitolo
		listaComponentiImportoCapitoloCollegate = componenteImportiCapitoloDad.findComponenteImportiCapitoloByUidCapitolo(
				capitolo.getUid(),
				ComponenteImportiCapitoloModelDetail.TipoComponenteImportiCapitolo,
				ComponenteImportiCapitoloModelDetail.Importo);
		log.debug(methodName, "Componenti associate al capitolo (contando quelle NON in variazione): " + listaComponentiImportoCapitoloCollegate.size());
		for(ComponenteImportiCapitolo cic : listaComponentiImportoCapitoloCollegate) {
			mappaComponentiImportoCapitoloCollegateCapitoloPerComponente.put(cic.getUid(), cic);
		}
	}
	
	/**
	 * Importi richiesti:
	 * <ul>
	 *   <li>disponibilit&agrave; a variare sullo stanziamento per l'anno di variazione (anno di variazione = <code>annoVariazione - annoCapitolo + 1</code>
	 *   <li>se la variazione &eacute; sull'anno del capitolo, disponibilit&agrave; a variare sulla cassa</li>
	 * </ul>
	 * @return gli importi richiesti
	 */
	private Set<ImportiCapitoloEnum> getImportiCapitoloDaCercare() {
		// Interessa la disponibilita' a variare per gli anni di variazione, e la disponibilita' a variare in cassa
		Set<ImportiCapitoloEnum> importi = EnumSet.of(
				ImportiCapitoloEnum.disponibilitaVariareAnno1,
				ImportiCapitoloEnum.disponibilitaVariareAnno2,
				ImportiCapitoloEnum.disponibilitaVariareAnno3,
				ImportiCapitoloEnum.disponibilitaVariareCassa);
		return importi;
	}

	/**
	 * Inizializzazione delle componenti capitolo
	 */
	private void initComponenteImportoCapitolo() {
		// Indice 1-based in quanto utilizzato solo per i log
		index = 1;
		Map<Integer, List<DettaglioVariazioneComponenteImportoCapitolo>> dettagliPerCuiInserireComponente = new HashMap<Integer, List<DettaglioVariazioneComponenteImportoCapitolo>>();
		
		for(DettaglioVariazioneComponenteImportoCapitolo dettaglio : req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo()) {
			initComponenteImportoCapitolo(dettaglio, 0, dettagliPerCuiInserireComponente);
			index++;
		}
		index = 1;
		for(DettaglioVariazioneComponenteImportoCapitolo dettaglio : req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo1()) {
			initComponenteImportoCapitolo(dettaglio, 1, dettagliPerCuiInserireComponente);
			index++;
		}
		index = 1;
		for(DettaglioVariazioneComponenteImportoCapitolo dettaglio : req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo2()) {
			initComponenteImportoCapitolo(dettaglio, 2, dettagliPerCuiInserireComponente);
			index++;
		}
		// Inserimento delle nuove componenti
		inserisciComponenti(dettagliPerCuiInserireComponente);
	}
	
	/**
	 * Inizializzazione della componente capitolo
	 * @param dettaglio il dettaglio
	 * @param dettagliPerCuiInserireComponente la lista dei dettagli per i quali &eacute; necessario inserire la componente <emph>ex-novo</emph>
	 */
	private void initComponenteImportoCapitolo(DettaglioVariazioneComponenteImportoCapitolo dettaglio, int delta, Map<Integer, List<DettaglioVariazioneComponenteImportoCapitolo>> dettagliPerCuiInserireComponente) {
		final String methodName = "initComponenteImportoCapitolo";
		ComponenteImportiCapitolo cic = dettaglio.getComponenteImportiCapitolo();
		if(cic.getUid() != 0) {
			log.debug(methodName, "Componente su indice " + index + ", delta " + delta + " con uid " + cic.getUid() + " - verifica esistenza dato...");
			cic = componenteImportiCapitoloDad.findComponenteImportiCapitoloByUid(cic.getUid(), ComponenteImportiCapitoloModelDetail.TipoComponenteImportiCapitolo);
			// Controllo di esistenza della componente per dato uid
			checkBusinessCondition(cic != null, ErroreCore.ENTITA_INESISTENTE.getErrore("Componente", "uid " + cic.getUid()));
			// Controllo coerenza del capitolo
			checkBusinessCondition(mappaComponentiImportoCapitoloCollegateCapitoloEVariazionePerComponente.containsKey(cic.getUid()), ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("componente " + cic.getUid() + " non collegata al capitolo"));
			dettaglio.setComponenteImportiCapitolo(cic);
		} else {
			// Non abbiamo il dato. E' probabilmente un nuovo componente... Ma per sicurezza effettuo un controllo per verificare di non averne gia' uno con dato uid
			Map<Integer, ComponenteImportiCapitolo> entry = mappaComponentiImportoCapitoloCollegateCapitoloEVariazionePerTipo.get(cic.getTipoComponenteImportiCapitolo().getUid());
			if(entry != null) {
				// Componente trovato per tipo.
				// Ottengo l'uid dell'anno corretto, riassegno l'uid ed esco
				Integer anno = capitolo.getAnnoCapitolo();
				ComponenteImportiCapitolo componenteEntry = entry.get(Integer.valueOf(anno.intValue() + delta));
				if(componenteEntry != null) {
					log.debug(methodName, "Componente su indice " + index + ", delta " + delta + " senza uid valorizzato ma corrispondente (per tipo) alla componente gia' esistente con uid " + componenteEntry.getUid() + " - forzo il dato");
					cic.setUid(componenteEntry.getUid());
				}
			}
		}
		
		if(dettaglio.isFlagEliminaComponenteCapitolo()) {
			// Posso eliminare solo componenti esistenti sul capitolo
			log.debug(methodName, "Componente su indice " + index + ", delta " + delta + " con flag eliminazione impostato. Uid della componente: " + cic.getUid());
			checkBusinessCondition(cic.getUid() != 0, ErroreCore.ANNULLAMENTO_NON_POSSIBILE.getErrore("La componente corrispondente all'indice " + index + ", delta " + delta + " di cui e' richiesta la cancellazione non risulta avere uid popolato"));
			ComponenteImportiCapitolo entry = mappaComponentiImportoCapitoloCollegateCapitoloPerComponente.get(cic.getUid());
			checkBusinessCondition(entry != null, ErroreCore.ANNULLAMENTO_NON_POSSIBILE.getErrore("La componente corrispondente all'indice " + index + ", delta " + delta + " di cui e' richiesta la cancellazione non risulta essere collegata al capitolo"));
			// Forzo l'importo: deve essere pari all'importo dalla componente, cambiato di segno
			dettaglio.setImporto(entry.computeImportoByTipoDettaglio(dettaglio.getTipoDettaglioComponenteImportiCapitolo()).negate());
		}
		// Uid presente: esco felice
		if(cic.getUid() != 0) {
			dettaglio.setFlagNuovaComponenteCapitolo(!mappaComponentiImportoCapitoloCollegateCapitoloPerComponente.containsKey(Integer.valueOf(cic.getUid())));
			log.debug(methodName, "Componente su indice " + index + " con uid valorizzata: " + cic.getUid());
			return;
		}
		// Componente non presente. Lo aggiungo nella lista delle componenti da inserire
		log.debug(methodName, "Componente su indice " + index + " senza uid e senza corrispondenza su capitolo. Forzo il flag \"NuovaComponenteCapitolo\" a true e inserisco il record");
		List<DettaglioVariazioneComponenteImportoCapitolo> daInserire = dettagliPerCuiInserireComponente.get(delta);
		if(daInserire == null) {
			daInserire = new ArrayList<DettaglioVariazioneComponenteImportoCapitolo>();
			dettagliPerCuiInserireComponente.put(delta, daInserire);
		}
		daInserire.add(dettaglio);
	}

	/**
	 * Inserimento delle componenti
	 * @param dettagliPerCuiInserireComponente i dettagli per cui popolare le componenti
	 */
	private void inserisciComponenti(Map<Integer, List<DettaglioVariazioneComponenteImportoCapitolo>> dettagliPerCuiInserireComponente) {
		final String methodName = "inserisciComponenti";
		if(dettagliPerCuiInserireComponente.isEmpty()) {
			// Tutte le componenti sono state gia' inserite
			log.debug(methodName, "Nessuna componente da inserire ex novo");
			return;
		}
		// Creazione testata request
		InserisceComponenteImportiCapitolo icic = new InserisceComponenteImportiCapitolo();
		icic.setAnnoBilancio(req.getAnnoBilancio());
		icic.setCapitolo(capitolo);
		icic.setComputeResultList(false);
		icic.setDataOra(new Date());
		icic.setRichiedente(req.getRichiedente());
		
		for(Entry<Integer, List<DettaglioVariazioneComponenteImportoCapitolo>> entry : dettagliPerCuiInserireComponente.entrySet()) {
			// Prendo il record corrispondente all'anno pari al delta della variazione
			ImportiCapitolo importiCapitolo = capitolo.getTipoCapitolo().newImportiCapitoloInstance();
			importiCapitolo.setAnnoCompetenza(Integer.valueOf(capitolo.getAnnoCapitolo().intValue() + entry.getKey().intValue()));
			
			for(DettaglioVariazioneComponenteImportoCapitolo dettaglio : entry.getValue()) {
				dettaglio.setFlagNuovaComponenteCapitolo(true);
				// Clone componente
				ComponenteImportiCapitolo clone = new ComponenteImportiCapitolo();
				clone.setTipoComponenteImportiCapitolo(dettaglio.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo());
				clone.setImportiCapitolo(importiCapitolo);
				
				DettaglioComponenteImportiCapitolo dcic = new DettaglioComponenteImportiCapitolo();
				// Imposto l'importo a ZERO si' da uniformare il comportamento in fase di definizione (in cui vado a impostare il delta sulla componente)
				dcic.setImporto(BigDecimal.ZERO);
				dcic.setTipoDettaglioComponenteImportiCapitolo(dettaglio.getTipoDettaglioComponenteImportiCapitolo());
				clone.getListaDettaglioComponenteImportiCapitolo().add(dcic);
				
				icic.getListaComponenteImportiCapitolo().add(clone);
				
			}
		}
		
		
		InserisceComponenteImportiCapitoloResponse icicr = executeExternalServiceSuccess(inserisceComponenteImportiCapitoloService, icic);
		
		// Rimappo gli uid. Suppongo che il sistema restituisca i record nell'esatto ordine di richiesta
		for(ComponenteImportiCapitolo cic : icicr.getListaComponenteImportiCapitolo()) {
			DettaglioVariazioneComponenteImportoCapitolo dettaglio = findDettaglio(dettagliPerCuiInserireComponente, cic);
			dettaglio.getComponenteImportiCapitolo().setUid(cic.getUid());
			log.debug(methodName, "Inserita componente con uid " + dettaglio.getComponenteImportiCapitolo().getUid());
			mappaComponentiImportoCapitoloCollegateCapitoloEVariazionePerComponente.put(dettaglio.getComponenteImportiCapitolo().getUid(), dettaglio.getComponenteImportiCapitolo());
		}
	}

	private DettaglioVariazioneComponenteImportoCapitolo findDettaglio(Map<Integer, List<DettaglioVariazioneComponenteImportoCapitolo>> dettagliPerCuiInserireComponente, ComponenteImportiCapitolo cic) {
		final String methodName = "findDettaglio";
		Integer delta = Integer.valueOf(cic.getImportiCapitolo().getAnnoCompetenza().intValue() - capitolo.getAnnoCapitolo().intValue());
		List<DettaglioVariazioneComponenteImportoCapitolo> dettagli = dettagliPerCuiInserireComponente.get(delta);
		checkBusinessCondition(dettagli != null && !dettagli.isEmpty(), ErroreCore.ERRORE_DI_SISTEMA.getErrore("Coerenza non possibile in inserimento della componente"));
		for(DettaglioVariazioneComponenteImportoCapitolo dettaglio : dettagli) {
			if(dettaglio.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getUid() == cic.getTipoComponenteImportiCapitolo().getUid()) {
				log.debug(methodName, "Inserita componente con uid " + cic.getUid() + " per tipo " + cic.getTipoComponenteImportiCapitolo().getUid() + " e delta " + delta);
				return dettaglio;
			}
		}
		throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Coerenza non possibile in inserimento della componente"));
	}

	/**
	 * Inizializzazione del dettaglio. Nel caso il dettaglio sia gi&agrave; presente, lo ricerca per uid e lo aggiorna; in caso contrario lo inserisce
	 */
	private void initDettaglioVariazioneImportoCapitolo() {
		final String methodName = "initDettaglioVariazioneImportoCapitolo";
		// Calcolo il dettaglio relativo al capitolo
		dettaglioVariazioneImportoCapitolo = variazioniDad.findSingoloDettaglioVariazioneImportoCapitoloByUidVariazioneAndUidCapitolo(
				req.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo().getUid(),
				capitolo.getUid());
		
		if(dettaglioVariazioneImportoCapitolo != null) {
			// Dettaglio gia' inizializzato
			log.debug(methodName, "Dettaglio gia' presente: effettuo l'aggiornamento del dato");
			dettaglioVariazioneImportoCapitolo.setStanziamento(initStanziamento(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo()));
			dettaglioVariazioneImportoCapitolo.setStanziamento1(initStanziamento(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo1()));
			dettaglioVariazioneImportoCapitolo.setStanziamento2(initStanziamento(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo2()));
			// Copio cassa e residuo
			dettaglioVariazioneImportoCapitolo.setStanziamentoCassa(req.getDettaglioVariazioneImportoCapitolo().getStanziamentoCassa());
			dettaglioVariazioneImportoCapitolo.setStanziamentoResiduo(req.getDettaglioVariazioneImportoCapitolo().getStanziamentoResiduo());
			
			// Clono le liste
			dettaglioVariazioneImportoCapitolo.setListaDettaglioVariazioneComponenteImportoCapitolo(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo());
			dettaglioVariazioneImportoCapitolo.setListaDettaglioVariazioneComponenteImportoCapitolo1(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo1());
			dettaglioVariazioneImportoCapitolo.setListaDettaglioVariazioneComponenteImportoCapitolo2(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo2());
			// Il capitolo ottenuto non ha tutti i dati necessari all'aggiornamento. Prendo quello originale
			dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
			updateDettaglioVariazioneImportoCapitolo();
			return;
		}
		// Dettaglio non inizializzato
		log.debug(methodName, "Dettaglio non ancora presente: effettuo l'inserimento del dato");
		req.getDettaglioVariazioneImportoCapitolo().setStanziamento(initStanziamento(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo()));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamento1(initStanziamento(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo1()));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamento2(initStanziamento(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo2()));
		
		insertDettaglioVariazioneImportoCapitolo();
	}
	/**
	 * Aggiornamento del dettaglio variazione importo capitolo
	 */
	private void updateDettaglioVariazioneImportoCapitolo() {
		AggiornaDettaglioVariazioneImportoCapitolo advic = new AggiornaDettaglioVariazioneImportoCapitolo();
		advic.setAnnoBilancio(req.getAnnoBilancio());
		advic.setDataOra(new Date());
		advic.setRichiedente(req.getRichiedente());
		advic.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		advic.setSkipLoadCapitolo(true);
		advic.setSkipLoadVariazione(true);
		advic.setCapitoloFondino(capitoloFondino);
		
		AggiornaDettaglioVariazioneImportoCapitoloResponse idvicr = executeExternalServiceSuccess(aggiornaDettaglioVariazioneImportoCapitoloService, advic);
		dettaglioVariazioneImportoCapitolo = idvicr.getDettaglioVariazioneImportoCapitolo();
	}
	/**
	 * Inserimento del dettaglio variazione importo capitolo
	 */
	private void insertDettaglioVariazioneImportoCapitolo() {
		InserisciDettaglioVariazioneImportoCapitolo idvic = new InserisciDettaglioVariazioneImportoCapitolo();
		idvic.setAnnoBilancio(req.getAnnoBilancio());
		idvic.setDataOra(new Date());
		idvic.setRichiedente(req.getRichiedente());
		idvic.setDettaglioVariazioneImportoCapitolo(req.getDettaglioVariazioneImportoCapitolo());
		idvic.setSkipLoadCapitolo(true);
		idvic.setSkipLoadVariazione(true);
		idvic.setCapitoloFondino(capitoloFondino);
		
		InserisciDettaglioVariazioneImportoCapitoloResponse idvicr = executeExternalServiceSuccess(inserisciDettaglioVariazioneImportoCapitoloService, idvic);
		dettaglioVariazioneImportoCapitolo = idvicr.getDettaglioVariazioneImportoCapitolo();
	}
	/**
	 * Inizialiizazione dei valori pregressi della variazione componenti importo capitolo
	 */
	private void initDettaglioVariazioneComponenteImportoCapitolo() {
		// Dati necessari per le verifiche sui dati
		Utility.MDTL.addModelDetails(DettaglioVariazioneComponenteImportoCapitoloModelDetail.ComponenteImportiCapitolo,
				DettaglioVariazioneComponenteImportoCapitoloModelDetail.Flag,
				ComponenteImportiCapitoloModelDetail.Importo);
		
		// Carico le componenti associate alla variazione
		List<DettaglioVariazioneComponenteImportoCapitolo> listaDettaglioVariazioneComponenteImportoCapitolo = variazioniDad.ricercaDettaglioVariazioneComponenteImportoCapitolo(
				req.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo().getUid(),
				capitolo.getUid(),
				Utility.MDTL.byModelDetailClass(DettaglioVariazioneComponenteImportoCapitoloModelDetail.class));
		// Trasformo la lista in una mappa prendendo come chiave l'uid: in questa maniera l'accesso e' piu' agevole
		for(DettaglioVariazioneComponenteImportoCapitolo dvcic : listaDettaglioVariazioneComponenteImportoCapitolo) {
			mappaDettaglioVariazioneComponentiImportoCapitoloCollegate.put(dvcic.getUid(), dvcic);
		}
		checkDettagliVariazioneComponenteImportiCapitolo(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo(), 0);
		checkDettagliVariazioneComponenteImportiCapitolo(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo1(), 1);
		checkDettagliVariazioneComponenteImportiCapitolo(req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo2(), 2);
	}

	/**
	 * Verifica dei dati utilizzati per le variazioni delle componenti
	 * <ul>
	 *   <li>Verifica che tutti i dettagli forniti in request con uid valorizzato (pertanto da aggiornare) siano gi&agrave; stati censiti</li>
	 *   <li>Verifica che gli importi risultanti sulle componenti non siano negativi (tranne che per il tipo capitolo BDG)</li>
	 * </ul>
	 * @param i 
	 * @param list 
	 */
	private void checkDettagliVariazioneComponenteImportiCapitolo(List<DettaglioVariazioneComponenteImportoCapitolo> dettagli, int delta) {
		final String methodName = "checkDettagliVariazioneComponenteImportiCapitolo";
		index = 0;
		for(DettaglioVariazioneComponenteImportoCapitolo dettaglio : dettagli) {
			// Verifica che tutti i dettagli forniti in request con uid valorizzato (pertanto da aggiornare) siano gia' stati censiti
			ComponenteImportiCapitolo oldComponenteImportiCapitolo = null;
			if(dettaglio.getComponenteImportiCapitolo().getUid() != 0) {
				oldComponenteImportiCapitolo = mappaComponentiImportoCapitoloCollegateCapitoloEVariazionePerComponente.get(dettaglio.getComponenteImportiCapitolo().getUid());
			}
			// Check importo risultante >= 0
			BigDecimal oldImporto = oldComponenteImportiCapitolo != null
					? oldComponenteImportiCapitolo.computeImportoByTipoDettaglio(dettaglio.getTipoDettaglioComponenteImportiCapitolo())
					: BigDecimal.ZERO;
			BigDecimal newImporto = dettaglio.getImporto();
			BigDecimal importoRisultante = oldImporto.add(newImporto);
			log.debug(methodName, "Controllo sugli importi per indice " + index + ", delta " + delta + ", l'importo risultante deve essere maggiore di zero: old (" + Utility.formatCurrencyAsString(oldImporto)
				+ ") + new (" + Utility.formatCurrencyAsString(newImporto) + ") = " + Utility.formatCurrencyAsString(importoRisultante));
			checkBusinessCondition(importoRisultante.compareTo(BigDecimal.ZERO) >= 0 || stanziamentoNegativoAmmissibile(), ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(
					"aggiornamento del dettaglio con indice " + (index + 1) + ", delta " + delta + ", in quanto l'importo risultante (" + Utility.formatCurrencyAsString(importoRisultante) + ") e' minore di zero"));
			index++;
		}
	}
	
	/**
	 * Inizializzazione dei dati della variazione
	 */
	private void initVariazione() {
		VariazioneImportoCapitolo variazioneImportoCapitolo = variazioniDad.findAnagraficaVariazioneImportoCapitoloByUid(req.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo().getUid());
		checkBusinessCondition(variazioneImportoCapitolo != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("variazione", "uid " + req.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo().getUid()));
		req.getDettaglioVariazioneImportoCapitolo().setVariazioneImportoCapitolo(variazioneImportoCapitolo);
	}
	/**
	 * Inizializzazione dello stanziamento
	 * @param listaComponenti la lista delle componenti afferenti al dettaglio
	 * @return lo stanziamento
	 */
	private BigDecimal initStanziamento(List<DettaglioVariazioneComponenteImportoCapitolo> listaComponenti) {
		final String methodName = "initStanziamento";
		BigDecimal stanziamento = BigDecimal.ZERO;
		for(DettaglioVariazioneComponenteImportoCapitolo dvcic : listaComponenti) {
			// Se richiedo di annullare una componente, devo togliere il suo intero importo
			stanziamento = stanziamento.add(dvcic.getImporto());
		}
		log.debug(methodName, "Stanziamento afferente al dettaglio: " + Utility.formatCurrency(stanziamento));
		return stanziamento;
	}
	/**
	 * Aggiornamento del dettaglio componente
	 * @param dettaglio il dettaglio da aggiornare
	 */
	private void updateDettaglioComponente(DettaglioVariazioneComponenteImportoCapitolo dettaglio) {
		final String methodName = "updateDettaglioComponente";
		variazioniDad.aggiornaDettaglioVariazioneComponenteImportoCapitolo(dettaglio);
		log.debug(methodName, "Aggiornato dettaglio con uid " + dettaglio.getUid());
		mappaDettaglioVariazioneComponentiImportoCapitoloCollegate.remove(dettaglio.getUid());
	}
	/**
	 * Inserimento del dettaglio componente
	 * @param dettaglio il dettaglio da inserire
	 */
	private void insertDettaglioComponente(DettaglioVariazioneComponenteImportoCapitolo dettaglio) {
		final String methodName = "insertDettaglioComponente";
		variazioniDad.inserisciDettaglioVariazioneComponenteImportoCapitolo(dettaglio);
		log.debug(methodName, "Inserito dettaglio con uid " + dettaglio.getUid());
	}
	/**
	 * Eliminazione dei dettagli residui sulla variazione
	 */
	private void eliminaDettagliResidui() {
		final String methodName = "eliminaDettagliResidui";
		for(Entry<Integer, DettaglioVariazioneComponenteImportoCapitolo> entry : mappaDettaglioVariazioneComponentiImportoCapitoloCollegate.entrySet()) {
			variazioniDad.cancellaDettaglioVariazioneComponenteImportoCapitolo(entry.getValue());
			log.debug(methodName, "Eliminato dettaglio con uid " + entry.getKey());
			if(entry.getValue().isFlagNuovaComponenteCapitolo()) {
				// Nuova componente. Devo eliminare anche il dettaglio inserito
				componenteImportiCapitoloDad.annullaComponenteImportiCapitolo(entry.getValue().getComponenteImportiCapitolo());
				log.debug(methodName, "Eliminata componente con uid " + entry.getValue().getComponenteImportiCapitolo().getUid());
			}
		}
	}
	
	/**
	 * Verifica se sia ammissibile uno stanziamento negativo per la componente
	 * @return se lo stanziamento negativo sia ammissibile
	 */
	private boolean stanziamentoNegativoAmmissibile() {
		// possibile solo per capitoli BUDGET
		//SIAC-7267
		return capitoloFondino;
//		return true;
	}
}
