/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.processi.GestoreProcessiVariazioneBilancio;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Definisce la Variazione di Bilancio aggiornando solo l'anagrafica.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefinisceAnagraficaVariazioneBilancioService extends VariazioneDiBilancioBaseService<DefinisceAnagraficaVariazioneBilancio, DefinisceAnagraficaVariazioneBilancioResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		variazione = req.getVariazioneImportoCapitolo();
		// Ho bisogno che sia impostato nella response per l'async responseHandler.
		res.setVariazioneImportoCapitolo(variazione);

		checkEntita(variazione, "variazione importi capitolo");	
		checkNotNull(variazione.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("variazione importi capitolo"), false);
		
		checkEntita(variazione.getEnte(), "ente", false);
		checkNotNull(variazione.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(variazione.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"),false);
		
		checkNotNull(variazione.getTipoVariazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo variazione"), false);
		checkNotNull(variazione.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("note"), false);
		checkNotNull(variazione.getStatoOperativoVariazioneDiBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo variazione"), false);
		
		variazione.setListaDettaglioVariazioneImporto(null);
		//SIAC-8332
//		checkNotNull(req.getIdAttivita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id attivita"));
//		checkNotNull(req.getListaVariabiliProcesso(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("varibiali processo"));
	}
	
	@Override
	protected void init() {
		capitoloDad.setEnte(variazione.getEnte());
		variazioniDad.setEnte(variazione.getEnte());
		variazioniDad.setBilancio(variazione.getBilancio());
		variazioniDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(variazione.getEnte());
		
		componenteImportiCapitoloDad.setEnte(ente);
		componenteImportiCapitoloDad.setLoginOperazione(loginOperazione);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT*4) //SIAC-7403
	public DefinisceAnagraficaVariazioneBilancioResponse executeServiceTxRequiresNew(DefinisceAnagraficaVariazioneBilancio serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT*4)//SIAC-7403
	public DefinisceAnagraficaVariazioneBilancioResponse executeService(DefinisceAnagraficaVariazioneBilancio serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		popolaDatiDellaVariazione();
		
		checkNecessarioAttoAmministrativoVariazioneDiBilancio();
		
		//SIAC-7972
		loadAndCheckImportiInDiminuzione();
		
		boolean isQuadraturaCorrettaStanziamento = isQuadraturaCorrettaStanziamento();
		res.setIsQuadraturaCorrettaStanziamento(Boolean.valueOf(isQuadraturaCorrettaStanziamento));
		checkBusinessCondition(isQuadraturaCorrettaStanziamento, ErroreBil.QUADRATURA_NON_CORRETTA.getErrore("Definizione variazione"));
		
		boolean isQuadraturaCorrettaStanziamentoCassa = req.isSaltaCheckStanziamentoCassa() || isQuadraturaCorrettaStanziamentoCassa();
		res.setIsQuadraturaCorrettaStanziamentoCassa(Boolean.valueOf(isQuadraturaCorrettaStanziamentoCassa));
		
		//Non si vuole nemmeno avere l'errore sull'errore di quadratura della cassa! si suppone che essendo arrivati qui 
		//L'utente abbia gia' scelto nel task precedente di voler bypassare il controllo di quadratura.
//		checkBusinessCondition(isQuadraturaCorrettaStanziamentoCassa, ErroreBil.QUADRATURA_NON_CORRETTA.getErrore("Definizione variazione"));
				
		//task-276 (stato precedente nel caso di due utenti che modificano la stessa variazione, contiene lo stato pre-definizione) 
		StatoOperativoVariazioneBilancio statoPrecedente = variazione.getStatoOperativoVariazioneDiBilancio();
		
		StatoOperativoVariazioneBilancio statoCorrente = findStatoOperativoCorrente();
		
		if (!statoPrecedente.getVariableName().equals(statoCorrente.getVariableName())) {
			throw new BusinessException(ErroreBil.VARIAZIONE_MODIFICATA.getErrore());
		}
		
		StatoOperativoVariazioneBilancio statoSuccessivo = GestoreProcessiVariazioneBilancio.getStatoFinaleVariazioneDiBilancio(statoCorrente, isQuadraturaCorrettaStanziamento);
		if(statoSuccessivo == null) {
			//qua non dovrei mai finire in quanto la quadratura e' gia' controllata prima, ma lo metto per sicurezza
			//task-276: ci si finisce in caso di concorrenza
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impossibile evolvere il processo allo stato successivo."));
		}
		variazione.setStatoOperativoVariazioneDiBilancio(statoSuccessivo);
		variazioniDad.aggiornaAnagraficaVariazioneImportoCapitolo(variazione);
		
		// SIAC-6881: gestione delle componenti annegato nell'aggiornaImportiCapitolo
		aggiornaImportiCapitolo();
		annullaCapitoliDaAnnullare();
		aggiornaStatoCapitoliProvvisori();
		
		res.setVariazioneImportoCapitolo(variazione);
	}
	
	//task-276
	private StatoOperativoVariazioneBilancio findStatoOperativoCorrente() {
		final String methodName = "findStatoOperativoCorrente";
		log.debug(methodName, "popolo i dati della variazione con uid : " + variazione.getUid());
		StatoOperativoVariazioneBilancio statoCorrente = variazioniDad.findStatoOperativoVariazioneDiBilancio(variazione);
		return statoCorrente;
	}
	
	
	private void popolaDatiDellaVariazione() {
		final String methodName = "popolaDatiDellaVariazione";
		log.debug(methodName, "popolo i dati della variazione con uid : " + variazione.getUid());
		VariazioneImportoCapitolo variazioneImportoCapitolo = variazioniDad.findVariazioneImportoCapitoloByUid(variazione.getUid());
		variazione.setApplicazioneVariazione(variazioneImportoCapitolo.getApplicazioneVariazione());
		variazione.setAttoAmministrativo(variazioneImportoCapitolo.getAttoAmministrativo());
		variazione.setListaDettaglioVariazioneImporto(variazioneImportoCapitolo.getListaDettaglioVariazioneImporto());
		//metto in response la variazione appena caricata, perche' serve come chiave
		res.setVariazioneImportoCapitolo(variazione);
	}

	/**
	 * Annulla i capitoli della variazione con flagAnnullaCapitolo impostato a TRUE.
	 */
	private void annullaCapitoliDaAnnullare() {
		for(DettaglioVariazioneImportoCapitolo dettVarImp : variazione.getListaDettaglioVariazioneImporto()){
			Capitolo<?, ?> cap = dettVarImp.getCapitolo();
			if(Boolean.TRUE.equals(dettVarImp.getFlagAnnullaCapitolo())){
				capitoloDad.aggiornaStato(cap.getUid(), StatoOperativoElementoDiBilancio.ANNULLATO);
			}
		}
	}
	
	/**
	 * Modifica lo statoOperativoElementoDiBilancio a VALIDO per i capitolo della
	 * variazione con flagNuovoCapitolo impostato a TRUE.
	 */
	private void aggiornaStatoCapitoliProvvisori() {
		final String methodName="aggiornaStatoCapitoliProvvisori";
		for(DettaglioVariazioneImportoCapitolo dettVarImp : variazione.getListaDettaglioVariazioneImporto()){
			Capitolo<?, ?> cap = dettVarImp.getCapitolo();
			if(Boolean.TRUE.equals(dettVarImp.getFlagNuovoCapitolo()) && isStatoCapitoloProvvisorio(cap)){
				log.debug(methodName, "rendo valido il capitolo: " + cap.getUid());
				capitoloDad.aggiornaStato(cap.getUid(), StatoOperativoElementoDiBilancio.VALIDO);
			}
		}
	}
	
	//SIAC-6705
	private boolean isStatoCapitoloProvvisorio(Capitolo<?, ?> cap) {
		StatoOperativoElementoDiBilancio stato = capitoloDad.findStatoOperativoCapitolo(cap.getUid());
		return stato != null && StatoOperativoElementoDiBilancio.PROVVISORIO.equals(stato);
	}

	
	/**
	 * Controlla la coerenza della variazione con la fase del bilancio.
	 * Solleva {@link BusinessException} se la variazione e il bilancio NON sono coerenti.
	 */
	protected void checkCoerenzaApplicazioneVariazioneFaseBilancio() {
		final String methodName = "isCoerenteApplicazioneVariazioneFaseBilancio";
		log.debug(methodName, "Effettuo una ricerca di dettaglio per il bilancio sì da avere i dati necessarii");
		// caricaBilancio();
		
		boolean isTipoCapitoloPrevisione = TipoCapitolo.isTipoCapitoloPrevisione(variazione.getCapitoli().get(0).getTipoCapitolo());
		log.debug(methodName, "isTipoCapitoloPrevisione: " + isTipoCapitoloPrevisione);
		
		FaseBilancio faseBilancio = variazione.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		log.debug(methodName, "Fase bilancio: " + faseBilancio);

		boolean isCoerente =
				// Previsione si può fare solo quando il bilancio e' in PREVISIONE o in ESERCIZIO_PROVVISORIO
				(isTipoCapitoloPrevisione && inFase(faseBilancio, FaseBilancio.PREVISIONE,FaseBilancio.ESERCIZIO_PROVVISORIO))
				||
				// Gestione si può fare solo quando il bilancio e' in ESERCIZIO_PROVVISORIO, GESTIONE o ASSESTAMENTO
				(!isTipoCapitoloPrevisione && inFase(faseBilancio, FaseBilancio.ESERCIZIO_PROVVISORIO, FaseBilancio.GESTIONE, FaseBilancio.ASSESTAMENTO));
		checkBusinessCondition(isCoerente, ErroreBil.DEFINIZIONE_NON_POSSIBILE_PERCHE_FASE_DI_BILANCIO_INCONGRUENTE.getErrore("Definizione"));
	}

	private boolean inFase(FaseBilancio faseToCheck, FaseBilancio... fasiPossibili) {
		boolean result = false;
		for(int i = 0; i < fasiPossibili.length && !result; i++) {
			result = fasiPossibili[i].equals(faseToCheck);
		}
		return result;
	}

	// SIAC-6881: spostati dalla classe base per permettere una gestione piu' puntuale
	/**
	 * Aggiorna gli importi su database.
	 */
	private void aggiornaImportiCapitolo() {
		Utility.MDTL.addModelDetails(
				DettaglioVariazioneComponenteImportoCapitoloModelDetail.Flag,
				DettaglioVariazioneComponenteImportoCapitoloModelDetail.ComponenteImportiCapitolo,
				ComponenteImportiCapitoloModelDetail.Importo,
				ComponenteImportiCapitoloModelDetail.TipoComponenteImportiCapitolo,
				ComponenteImportiCapitoloModelDetail.ImportiCapitolo);
		
		final String methodName = "aggiornaImportiCapitolo";
		log.debug(methodName, "Aggiorno gli importi del capitolo.");
		for(DettaglioVariazioneImportoCapitolo dettVarImp : variazione.getListaDettaglioVariazioneImporto()){
			// SIAC-6881
			Utility.CTL.set(dettVarImp.getCapitolo());
			List<DettaglioVariazioneComponenteImportoCapitolo> dettagliComponenti = variazioniDad.ricercaDettaglioVariazioneComponenteImportoCapitolo(
					variazione.getUid(),
					dettVarImp.getCapitolo().getUid(),
					Utility.MDTL.byModelDetailClass(DettaglioVariazioneComponenteImportoCapitoloModelDetail.class));
			if(dettagliComponenti != null && !dettagliComponenti.isEmpty()) {
				log.debug(methodName, "Aggiornamento delle componenti afferenti al capitolo con uid: " + dettVarImp.getCapitolo().getUid());
				aggiornaComponenti(dettagliComponenti, dettVarImp);
			}
			log.debug(methodName, "Aggiorno gli importi del capitolo con uid: " + dettVarImp.getCapitolo() + " .");
			aggiornaImportiCapitolo(dettVarImp);
		}
		// Cleanup ThreadLocal
		Utility.MDTL.remove();
		Utility.CTL.remove();
	}
	
	private void aggiornaImportiCapitolo(DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo) {
		aggiornaImportiCapitolo(dettaglioVariazioneImportoCapitolo.getCapitolo(), 0, dettaglioVariazioneImportoCapitolo.getStanziamento(), dettaglioVariazioneImportoCapitolo.getStanziamentoResiduo(), dettaglioVariazioneImportoCapitolo.getStanziamentoCassa());
		aggiornaImportiCapitolo(dettaglioVariazioneImportoCapitolo.getCapitolo(), 1, dettaglioVariazioneImportoCapitolo.getStanziamento1(), BigDecimal.ZERO, BigDecimal.ZERO);
		aggiornaImportiCapitolo(dettaglioVariazioneImportoCapitolo.getCapitolo(), 2, dettaglioVariazioneImportoCapitolo.getStanziamento2(), BigDecimal.ZERO, BigDecimal.ZERO);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void aggiornaImportiCapitolo(Capitolo<?, ?> capitolo, int delta, BigDecimal stanziamento, BigDecimal stanziamentoResiduo, BigDecimal stanziamentoCassa) {
		final String methodName = "aggiornaImportiCapitolo";
		int anno = capitolo.getAnnoCapitolo().intValue() + delta;
		ImportiCapitolo importiCap = importiCapitoloDad.findImportiCapitolo(
				capitolo,
				anno,
				capitolo.getTipoCapitolo().getImportiCapitoloClass(),
				EnumSet.noneOf(ImportiCapitoloEnum.class));
		if(importiCap == null) {
			log.debug(methodName, "Importi del capitolo con uid: " + capitolo.getUid() + " per anno: " + anno + " non presenti.");
			return;
		}
		log.debug(methodName, "Importi da impostare in variazione. Stanziamento: " + stanziamento + "StanziamentoCassa: " + stanziamentoCassa + "Residuo: " + stanziamentoResiduo);
		importiCap.addStanziamento(stanziamento);
		importiCap.addStanziamentoResiduo(stanziamentoResiduo);
		importiCap.addStanziamentoCassa(stanziamentoCassa);
		//SIAC-7784
		adeguaMassimoImpegnabile(stanziamento, importiCap);
		
		((Capitolo)capitolo).getListaImportiCapitolo().set(delta, importiCap);
		if(delta == 0) {
			((Capitolo)capitolo).setImportiCapitolo(importiCap);
		}
		importiCapitoloDad.aggiornaImportiCapitolo(capitolo, importiCap, anno);
	}

	//SIAC-7784
	private void adeguaMassimoImpegnabile(BigDecimal deltaVariazione, ImportiCapitolo importiCap) {
		//SIAC-8008
		if((deltaVariazione != null && BigDecimal.ZERO.compareTo(deltaVariazione) <= 0) || ! (importiCap instanceof ImportiCapitoloUG)) {
			//variazione importo positiva oppure capitolo non di uscita gestione, esco
			return;
		}
		BigDecimal massimoImpegnabile =	((ImportiCapitoloUG) importiCap).getMassimoImpegnabile();
		BigDecimal importoSta = importiCap.getStanziamento();
		//SIAC-8008
		if(importoSta != null && massimoImpegnabile != null && importoSta.compareTo(massimoImpegnabile) <0) {
			//lo stanziamento e' minore del masismo impegnabile: lo adeguo
			 ((ImportiCapitoloUG) importiCap).setMassimoImpegnabile(importoSta);
		}
	}
	
	/**
	 * Aggiornamento delle componenti
	 * @param dettagliComponenti i dettagli delle componenti da aggiornare
	 * @param dettVarImp il dettaglio dell'importo
	 */
	private void aggiornaComponenti(List<DettaglioVariazioneComponenteImportoCapitolo> dettagliComponenti, DettaglioVariazioneImportoCapitolo dettVarImp) {
		// SIAC-6881 + SIAC-6883
		for(DettaglioVariazioneComponenteImportoCapitolo dvcic : dettagliComponenti) {
			if(dvcic.isFlagEliminaComponenteCapitolo()) {
				// Aggiorna importi per anni <> anno variazione
				eliminaStanziamenti(dvcic, dettVarImp);
				// Elimina la componente
				componenteImportiCapitoloDad.annullaRigaComponenteImportiCapitolo(dvcic.getComponenteImportiCapitolo());
			} else {
				aggiornaImportoComponente(dvcic, dettVarImp);
			}
		}
	}
	
	/**
	 * Eliminazione degli stanziamenti afferenti al tipo di componente
	 * @param dvcic il dettaglio della componente
	 * @param dettVarImp il dettaglio della variazione
	 */
	private void eliminaStanziamenti(DettaglioVariazioneComponenteImportoCapitolo dvcic, DettaglioVariazioneImportoCapitolo dettVarImp) {
		Integer annoCompetenza = dvcic.getComponenteImportiCapitolo().getImportiCapitolo().getAnnoCompetenza();
		// Recupero l'importo
		List<ComponenteImportiCapitolo> componenti = componenteImportiCapitoloDad.findComponenteImportiCapitoloByUidCapitoloAnnoTipoComponente(
				Integer.valueOf(dettVarImp.getCapitolo().getUid()),
				annoCompetenza,
				Integer.valueOf(dvcic.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getUid()),
				ComponenteImportiCapitoloModelDetail.Importo);
		BigDecimal importo = BigDecimal.ZERO;
		for(ComponenteImportiCapitolo cic : componenti) {
			importo = importo.add(cic.computeImportoByTipoDettaglio(dvcic.getTipoDettaglioComponenteImportiCapitolo()));
		}
		dvcic.getComponenteImportiCapitolo().getImportiCapitolo().subtractStanziamento(importo);

		importiCapitoloDad.aggiornaImportiCapitolo(dettVarImp.getCapitolo(), dvcic.getComponenteImportiCapitolo().getImportiCapitolo(), annoCompetenza.intValue());
	}

	/**
	 * Aggiornamento degli importi sulle componenti.
	 * Il capitolo ha un  <code> siac_t_bil_ele_det </code> (lo stanziamento) a cui e' associato un <code> siac_t_bil_elem_det_comp </code> 
	 * (la componente relativa allo stanziamento). 
	 * Quando l'utente associa un capitolo alla variazione, il sistema va a creare un 
	 * <code>siac_t_bil_elem_det_var</code> collegato al <code>siac_t_bil_elem_det </code> (lo stanziamento che si sta variando) perche' e' 
	 * necessario avere un legame tra l'importo (ad esempio "-10,00") ed il <code> siac_t_bil_elem_det </code> su cui andrà applicato. 
	 * Analogamente va a scrivere un <code>siac_t_bil_elem_det_comp_var </code> che è collegato al <code> siac_t_bil_elem_det_comp </code>.
	 * <br>
	 * <strong> Cosa succede quando l'utente decide di inserire una nuova componente sul capitolo? </strong><br> 
	 * Il sistema puo' scrivere un record sulla <code> siac_t_bil_elem_det_var </code> e collegarlo al <code> siac_t_bil_elem_det</code>
	 *  ma quando prova a scrivere un <code> siac_t_bil_elem_det_comp_var </code> non c'e' ancora un record sulla <code> siac_t_bil_elem_det_comp</code>. 
	 *  Il sistema e' pertanto costretto a inserire una componente "provvisoria". Il fatto che tale componente non venga mostrata come gia' collegata al capitolo e' 
	 *  garantito dal fatto che in consultazione del capitolo non vengono tirate su le componenti collegate a variazioni in stato non definitivo e marcate con il flag "nuova componente"
	 *
	 * @param dvcic the dvcic
	 * @param dettVarImp the dett var imp
	 */
	//SIAC-7688
	private void aggiornaImportoComponente(DettaglioVariazioneComponenteImportoCapitolo dvcic, DettaglioVariazioneImportoCapitolo dettVarImp) {
		//1 - considero la componente associata alla siac_t_bil_elem_det_var_comp
		ComponenteImportiCapitolo componenteDelDettaglio = dvcic.getComponenteImportiCapitolo();
		// la componente e' segnata come nuova componente??
		boolean gestioneNuovaComponente = dvcic.isFlagNuovaComponenteCapitolo()
				//controllo se vi siano altre componenti dello stesso tipo associate al capitolo tramite variazioni concorrente
				&& hasComponentiDefinitiveEProvvisorieAssociate(dettVarImp.getCapitolo(), componenteDelDettaglio.getTipoComponenteImportiCapitolo());
		
		//se la componente e' nuova, devo controllare che non sia gia' stata inserita da una variazione concorrente
		if(gestioneNuovaComponente) {
			gestisciComponenteNuovaSuCapitolo(dvcic, dettVarImp, componenteDelDettaglio);
			return;
		}
		
		effettuaAggiornamentoImportoComponenteSuDB(dvcic, componenteDelDettaglio);
	}

	/**
	 * Gestisco i casi in cui la componente sia associata al capitolo tramite la variazione
	 * @param dvcic
	 * @param dettVarImp
	 * @param componenteDelDettaglio
	 */
	//SIAC-7688
	private void gestisciComponenteNuovaSuCapitolo(DettaglioVariazioneComponenteImportoCapitolo dvcic, DettaglioVariazioneImportoCapitolo dettVarImp, ComponenteImportiCapitolo componenteDelDettaglio) {
		ComponenteImportiCapitolo componenteImportiCapitoloCollegataAVariazone = dvcic.getComponenteImportiCapitolo();
		//carico la componente asociata al capitolo tramite un'altra variazione resa definitiva nel periodo tra la creazione di questa variazione e adesso (la sua definizione)
		ComponenteImportiCapitolo cicDefinitivaSuCapitolo = caricaComponenteCorrispondenteCollegatalCapitoloEAVariazioneDefONoVariazione(dettVarImp.getCapitolo(), componenteDelDettaglio);
		
		if(cicDefinitivaSuCapitolo == null || cicDefinitivaSuCapitolo.getUid() == componenteImportiCapitoloCollegataAVariazone.getUid()) {
			//non esiste nessuna componente associata al capitolo e collegata a variazione degfinitiva: sono la prima delle variazioni concorrenti che 
			//cerca di inserire la nuova componente sul capitolo, mi comporto come se fossi l'unica e aggiorno la componente a me collegata.
			effettuaAggiornamentoImportoComponenteSuDB(dvcic, componenteImportiCapitoloCollegataAVariazone);
			return;
		}
		//la componente era nuova quando e' stata inserita la variazione,  ma nel frattempo un'altra variazione ha collegato quella stessa componente al capitolo.
		//aggiorno pertanto la componente gia' associata al capitolo per non avere record duplicati
		effettuaAggiornamentoImportoComponenteSuDB(dvcic, cicDefinitivaSuCapitolo);
		// il record di dettaglio va spostato sulla componente del capitolo e la riga di componente "vecchia" eliminata
		componenteImportiCapitoloDad.spostaDettaglioVariazioneSuComponente(dvcic, cicDefinitivaSuCapitolo);
		return;
	}

	/**
	 * Effettua aggiornamento dell'importo della componente su DB.
	 *
	 * @param dvcic the dvcic
	 * @param componenteImportoCapitoloDaAggiornare the componente importo capitolo da aggiornare
	 */
	private void effettuaAggiornamentoImportoComponenteSuDB(DettaglioVariazioneComponenteImportoCapitolo dvcic,	ComponenteImportiCapitolo componenteImportoCapitoloDaAggiornare) {
		BigDecimal newImporto = componenteImportoCapitoloDaAggiornare.computeImportoByTipoDettaglio(dvcic.getTipoDettaglioComponenteImportiCapitolo()).add(dvcic.getImporto());
		componenteImportoCapitoloDaAggiornare.impostaImportoByTipoDettaglio(newImporto, dvcic.getTipoDettaglioComponenteImportiCapitolo());
		componenteImportiCapitoloDad.aggiornaComponenteImportiCapitolo(componenteImportoCapitoloDaAggiornare);
	}

	/**
	 * Checks for componenti definitive E provvisorie associate.
	 * Considero il seguente caso (limite);
	 * <ul>
	 *    <li> l'utente inserisce la variazione A che aggiunge la componente di tipo a' sul capitolo alpha e la lascia in stato BOZZA
	 *    <li> l'utente inserisce la variazione B che anch'essa aggiunge la componente di tipo a' sul capitolo alpha e lascia anche tale variazioni in stato BOZZA.
	 * </ul>
	 * Il sistema in questo caso sia per la variazione A che per la variazione B ha inserito tre record (uno per ogni anno) della componente a' sul capitolo alpha.
	 * Quindi il capitolo è in totale collegato a 6 siac_t_bil_elem_det_comp per la componente di tipo a' con importo a zero. 
	 * Tutte le componenti collegate ad un capitolo E ad una variazione in stato <> 'D' non vengono considerate nella lettura delle componenti e quindi nessuna 
	 * delle sei componenti di cui sopra appare in consultazione del capitolo. Pertanto vengono considerate per semplicita' "provvisorie".
	 * <ul>
	 *    <li> l'utente rende definitiva la variazione B
	 * </ul>
	 * Quando questo accade, l'importo del capitolo viene aggiornato ed i 3 record siac_t_bil_elem_det_comp collegati al capitolo tramite la variazione B 
	 * vengono considerati collegati effettivamente al capitolo. Per semplicita, nel metodo si chiamano queste componenti "definitive".
	 * 
	 * Questo metodo si occupa di controllare se, per un dato capitolo ed un dato tipo componente, vi siano piu' componenti  (siac_t_bil_elem_det_comp) 
	 * associate a questo capitolo, quindi se e' stato in qualche modo seguito l'iter descritto sopra. 
	 * 
	 * @param cp the cp
	 * @param tc the tc
	 * @return true, if successful
	 */
	private boolean hasComponentiDefinitiveEProvvisorieAssociate(Capitolo<?,?> cp, TipoComponenteImportiCapitolo tc) {
		return componenteImportiCapitoloDad.hasComponentiDefinitiveEProvvisorieAssociate(cp, tc);
	}

	/**
	 * Dato un capitolo ed una componente, ottengo la componente (ove presente) che abbia lo stesso tipo della componente input, 
	 * sia relativa allo stesso anno, ma sia collegata al capitolo e ad una variazione definitiva o a nessuna variazione.
	 *
	 * @param capitolo the capitolo
	 * @param comp the comp
	 * @return the componente importi capitolo
	 */
	private ComponenteImportiCapitolo caricaComponenteCorrispondenteCollegatalCapitoloEAVariazioneDefONoVariazione(Capitolo<?,?> capitolo, ComponenteImportiCapitolo comp) {

		return componenteImportiCapitoloDad.caricaComponenteConStessoTipoCollegataAlCapitoloEAVariazioneDefONoVariazione(capitolo, comp);
		
	}
	
	@Override
	protected boolean isAnnullamentoVariazione() {
		//nello step di definizione non e' consentito l'annullamento della variazione
		return false;
	}
	
}
