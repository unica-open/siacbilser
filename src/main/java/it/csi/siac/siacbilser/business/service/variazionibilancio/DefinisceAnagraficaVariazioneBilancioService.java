/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
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
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiesta;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiestaResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.TipoAzione;
import it.csi.siac.siaccorser.model.VariabileProcesso;
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
		
		checkEntita(variazione, "variazione importi capitolo");	
		checkNotNull(variazione.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("variazione importi capitolo"), false);
		
		checkEntita(variazione.getEnte(), "ente", false);
		checkNotNull(variazione.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(variazione.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"),false);
		
		checkNotNull(variazione.getTipoVariazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo variazione"), false);
		checkNotNull(variazione.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("note"), false);
		checkNotNull(variazione.getStatoOperativoVariazioneDiBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo variazione"), false);
		
		variazione.setListaDettaglioVariazioneImporto(null);
		
		checkNotNull(req.getIdAttivita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id attivita"));
		checkNotNull(req.getListaVariabiliProcesso(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("varibiali processo"));
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
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT) //hook per l'async
	public DefinisceAnagraficaVariazioneBilancioResponse executeServiceTxRequiresNew(DefinisceAnagraficaVariazioneBilancio serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional
	public DefinisceAnagraficaVariazioneBilancioResponse executeService(DefinisceAnagraficaVariazioneBilancio serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		popolaDatiDellaVariazione();
		
		checkNecessarioAttoAmministrativoVariazioneDiBilancio();
		
		checkImporti();
		boolean isQuadraturaCorrettaStanziamento = isQuadraturaCorrettaStanziamento();
		res.setIsQuadraturaCorrettaStanziamento(Boolean.valueOf(isQuadraturaCorrettaStanziamento));
		checkBusinessCondition(isQuadraturaCorrettaStanziamento, ErroreBil.QUADRATURA_NON_CORRETTA.getErrore("Definizione variazione"));
		
		boolean isQuadraturaCorrettaStanziamentoCassa = req.isSaltaCheckStanziamentoCassa() || isQuadraturaCorrettaStanziamentoCassa();
		res.setIsQuadraturaCorrettaStanziamentoCassa(Boolean.valueOf(isQuadraturaCorrettaStanziamentoCassa));
		
		//Non si vuole nemmeno avere l'errore sull'errore di quadratura della cassa! si suppone che essendo arrivati qui 
		//L'utente abbia gia' scelto nel task precedente di voler bypassare il controllo di quadratura.
//		checkBusinessCondition(isQuadraturaCorrettaStanziamentoCassa, ErroreBil.QUADRATURA_NON_CORRETTA.getErrore("Definizione variazione"));
		
		variazione.setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneDiBilancio.DEFINITIVA);
		variazioniDad.aggiornaAnagraficaVariazioneImportoCapitolo(variazione);
		
		// SIAC-6881: gestione delle componenti annegato nell'aggiornaImportiCapitolo
		aggiornaImportiCapitolo();
		annullaCapitoliDaAnnullare();
		aggiornaStatoCapitoliProvvisori();
		
		res.setVariazioneImportoCapitolo(variazione);
		definisciProcessoVariazioneDiBilancio();
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
	 * Definisci processo variazione di bilancio.
	 */
	private void definisciProcessoVariazioneDiBilancio() {
		ExecAzioneRichiesta execAzioneRichiesta = new ExecAzioneRichiesta();
		execAzioneRichiesta.setRichiedente(req.getRichiedente());
		execAzioneRichiesta.setDataOra(new Date());
		
		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		Azione azione = new Azione();
		azioneRichiesta.setAzione(azione);
		//ID DELL'attività ovvero id istanza. Ad esempio: "VariazioneDiBilancio--1.0--12--VariazioneDiBilancio_AggiornamentoVariazione--it1--mainActivityInstance--noLoop"
		azioneRichiesta.setIdAttivita(req.getIdAttivita());
		azione.setTipo(TipoAzione.ATTIVITA_PROCESSO);
		azione.setNomeProcesso("VariazioneDiBilancio");
		azione.setNomeTask("VariazioneDiBilancio-DefinizioneDellaVariazione");
		
		List<VariabileProcesso> variabiliToUse = new ArrayList<VariabileProcesso>(req.getListaVariabiliProcesso());
		
		setVariabileProcesso(azioneRichiesta, variabiliToUse, "descrizione");
		setVariabileProcesso(azioneRichiesta, variabiliToUse, "descrizioneBreve");
		setVariabileProcesso(azioneRichiesta, variabiliToUse, "siacSacProcesso");
		setVariabileProcesso(azioneRichiesta, variabiliToUse, "invioGiunta");
		setVariabileProcesso(azioneRichiesta, variabiliToUse, "invioConsiglio");
		setVariabileProcesso(azioneRichiesta, "annullaVariazione", Boolean.FALSE);
		setVariabileProcesso(azioneRichiesta, "quadraturaVariazioneDiBilancio", Boolean.TRUE);
		setVariabileProcesso(azioneRichiesta, "statoVariazioneDiBilancio", StatoOperativoVariazioneDiBilancio.DEFINITIVA.toString());
		
		execAzioneRichiesta.setAzioneRichiesta(azioneRichiesta);
		
		log.logXmlTypeObject(execAzioneRichiesta, "Request per il servizio ExecAzioneRichiesta.");
		ExecAzioneRichiestaResponse execAzioneRichiestaResponse = coreService.execAzioneRichiesta(execAzioneRichiesta);
		log.logXmlTypeObject(execAzioneRichiestaResponse, "Risposta ottenuta dal servizio ExecAzioneRichiesta.");
		checkServiceResponseFallimento(execAzioneRichiestaResponse);
	}
	
	
	protected void setVariabileProcesso(AzioneRichiesta azioneRichiesta, List<VariabileProcesso> oldVariabiliProcesso, String nome) {
		Object valore = null;
		boolean found = false;
		
		for(Iterator<VariabileProcesso> it = oldVariabiliProcesso.iterator(); it.hasNext() && !found;) {
			VariabileProcesso vp = it.next();
			if(vp != null && nome.equals(vp.getNome())) {
				valore = vp.getValore();
				found = true;
				it.remove();
			}
		}
		
		super.setVariabileProcesso(azioneRichiesta, nome, valore);
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
		((Capitolo)capitolo).getListaImportiCapitolo().set(delta, importiCap);
		if(delta == 0) {
			((Capitolo)capitolo).setImportiCapitolo(importiCap);
		}
		importiCapitoloDad.aggiornaImportiCapitolo(capitolo, importiCap, anno);
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
				aggiornaImportoComponente(dvcic);
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
	 * Aggiornamento dell'importo della componente con il dato della variazione
	 * @param dvcic il dettaglio della variazione importo
	 */
	private void aggiornaImportoComponente(DettaglioVariazioneComponenteImportoCapitolo dvcic) {
		BigDecimal newImporto = dvcic.getComponenteImportiCapitolo().computeImportoByTipoDettaglio(dvcic.getTipoDettaglioComponenteImportiCapitolo()).add(dvcic.getImporto());
		dvcic.getComponenteImportiCapitolo().impostaImportoByTipoDettaglio(newImporto, dvcic.getTipoDettaglioComponenteImportiCapitolo());
		componenteImportiCapitoloDad.aggiornaComponenteImportiCapitolo(dvcic.getComponenteImportiCapitolo());
	}
	
}
