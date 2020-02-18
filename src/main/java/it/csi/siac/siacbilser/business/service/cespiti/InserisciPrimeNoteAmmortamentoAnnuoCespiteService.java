/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaService;
import it.csi.siac.siacbilser.integration.dad.AnteprimaAmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.model.AnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaProvvisoria;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Inserisce e completa gli Elenchi di Documenti Spesa, Entrata, IVA Spesa, Iva
 * Entrata, comprensivi di AllegatoAtto.
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciPrimeNoteAmmortamentoAnnuoCespiteService extends CheckedAccountBaseService<InserisciPrimeNoteAmmortamentoAnnuoCespite, InserisciPrimeNoteAmmortamentoAnnuoCespiteResponse> {

	private static final String primaNotaAmmortamentoNonInserita = "PRIMA_NOTA_NON_INSERITA";
	private static final String primaNotaAmmortamentoInseritaCorrettamente = "INSERITA_PRIMA_NOTA";
	private static final DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail[] modelDetailsDettagli = new  DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail[] { DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail.Conto, DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail.Segno};
	private static final String codiceCausaleAmmortamento ="AMA";
	//DADs
	@Autowired
	private AnteprimaAmmortamentoAnnuoCespiteDad anteprimaAmmortamentoAnnuoCespiteDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private CausaleEPDad causaleEPDad;
	@Autowired
	private PrimaNotaInvDad primaNotaInvDad;
	
	//Servizi esterni
	@Autowired
	private InseriscePrimaNotaService inseriscePrimaNotaService;
	
	//questa potrebbe essere mappa uid - object[]
	
	private List<DettaglioAnteprimaAmmortamentoAnnuoCespite> dettagliAnteprimaAmmortamentoAnnuoCespiteSegnoDare = new ArrayList<DettaglioAnteprimaAmmortamentoAnnuoCespite>();
	private AnteprimaAmmortamentoAnnuoCespite anteprimaAmmortamentoAnnuoCespite;
	private CausaleEP causaleEP;
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAnnoAmmortamentoAnnuo(), "anno");
	}

	@Override
	protected void init() {
		super.init();
		anteprimaAmmortamentoAnnuoCespiteDad.setEnte(ente);
		anteprimaAmmortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		bilancioDad.setEnteEntity(ente);
		causaleEPDad.setEnte(ente);
		primaNotaInvDad.setEnte(ente);
		primaNotaInvDad.setLoginOperazione(loginOperazione);
		
	}

	@Transactional
	@Override
	public InserisciPrimeNoteAmmortamentoAnnuoCespiteResponse executeService(InserisciPrimeNoteAmmortamentoAnnuoCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		caricaAnteprima();
		annullaPrimeNotePrecedenti();
		caricaCausaleEP();
		caricaDettagliDare();
		inserisciPrimeNote();
	}
	
	
	/**
	 * Carica anteprima.
	 */
	private void caricaAnteprima() {
		final String methodName ="caricaAnteprima";
		this.anteprimaAmmortamentoAnnuoCespite = anteprimaAmmortamentoAnnuoCespiteDad.caricaAnteprimaAmmprtamentoAnnuo(req.getAnnoAmmortamentoAnnuo());
		if(this.anteprimaAmmortamentoAnnuoCespite != null) {
			log.debug(methodName, "trovata anteprima su db con uid: " + this.anteprimaAmmortamentoAnnuoCespite.getUid() );
			return;
		}
		log.debug(methodName, "Nessuna anteprima presente su db, ne creo una nuova.");
		//TODO: modificare
		Integer numeroCespiti = anteprimaAmmortamentoAnnuoCespiteDad.inserisciNuovaAnteprimaAmmortamentoAnnuoCespite(req.getAnnoAmmortamentoAnnuo());
		if(numeroCespiti == null) {
			log.warn(methodName, "Ho provato ad inserire un'anteprima, ma questa non eho trovato cespiti per crearla. ");
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("anteprima delle scritture", "nessun cespite soddisfa i criteri per la creazione delle scritture."));
		}
		log.debug(methodName, "Carico l'anteprima appena creata..");
		this.anteprimaAmmortamentoAnnuoCespite = anteprimaAmmortamentoAnnuoCespiteDad.caricaAnteprimaAmmprtamentoAnnuo(req.getAnnoAmmortamentoAnnuo());
		if(this.anteprimaAmmortamentoAnnuoCespite == null) {
			log.warn(methodName, "Ho inserito un'anteprima, ma questa non e' presente su db. ");
			String anno = req.getAnnoAmmortamentoAnnuo() !=null ? String.valueOf(req.getAnnoAmmortamentoAnnuo()) : "" ;
			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore(" per l'anno " + anno  + " non e' presente un'anteprima e non risulta essere possibile l'inserimento contestuale."));
		}
	}

	private void annullaPrimeNotePrecedenti() {
		final String methodName="annullaPrimeNotePrecedenti";
		log.debug(methodName, "Carico eventuali prime note non annullate.");
		List<PrimaNota> primeNote = primaNotaInvDad.ricercaScrittureInventarioByEntita(this.anteprimaAmmortamentoAnnuoCespite,null, null, Boolean.TRUE, null, null);
		if(primeNote == null || primeNote.isEmpty()) {
			log.debug(methodName, "Non sono presenti prime note per l'anteprima.");
			return;
		}
		for (PrimaNota primaNota : primeNote) {
			annullaPrimaNotaPrecedente(primaNota);
		}
	}
	
	/**
	 * Carica causale EP.
	 */
	private void caricaCausaleEP() {
		final String methodName="caricaCausaleEP";
		
		log.debug(methodName, "Cerco una causale con codice " + codiceCausaleAmmortamento);
		CausaleEP criteriDiRicerca = new CausaleEP();
		criteriDiRicerca.setCodice(codiceCausaleAmmortamento);
		criteriDiRicerca.setAmbito(Ambito.AMBITO_INV);
		// N.B: queste ricerca puo' essere ittimizzata, ho bisogno solo dell'id
		this.causaleEP = causaleEPDad.ricercaCausaleEPByCodice(criteriDiRicerca);
		if(this.causaleEP == null || this.causaleEP.getUid() == 0) {
			log.warn(methodName, "Nessuna una causale con codice " + codiceCausaleAmmortamento + " presente in archivio.");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("inserimento prime note da ammortamento", "causale EP con codice AMM"));
		}
		log.debug(methodName, "Trovata causale con uid: " + this.causaleEP.getUid());
	}

	/**
	 * Carica dettagli dare.
	 */
	private void caricaDettagliDare() {
		final String methodName ="caricaDettagliDare";
		log.debug(methodName, "carico tutti i dettagli presenti in anteprima con il segno DARE.");
		//carico prima i dettagli dare per essere sicura di mantenere la corrispondenza dare/avere corretta
		dettagliAnteprimaAmmortamentoAnnuoCespiteSegnoDare = anteprimaAmmortamentoAnnuoCespiteDad.caricaListaDettagliAnteprimaSegno(anteprimaAmmortamentoAnnuoCespite, OperazioneSegnoConto.DARE, modelDetailsDettagli);
		if(dettagliAnteprimaAmmortamentoAnnuoCespiteSegnoDare == null || dettagliAnteprimaAmmortamentoAnnuoCespiteSegnoDare.isEmpty()) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile trovare dettagli con segno DARE per l'anteprima."));
		}
	}
	
	/**
	 * Inserisci prime note.
	 */
	private void inserisciPrimeNote() {
		final String methodName ="inserisciPrimeNote";
		
		for (DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioDare : dettagliAnteprimaAmmortamentoAnnuoCespiteSegnoDare) {
			log.info(methodName, "voglio inserire una prima nota a partire dal dettaglio dare con uid: " + (dettaglioDare != null ? dettaglioDare.getUid() : "null"));
			
			DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioAvere = null;
			
			PrimaNota pnotaInserita = null;
			
			try {
				checkDettaglio(dettaglioDare);
				dettaglioAvere = anteprimaAmmortamentoAnnuoCespiteDad.caricaDettaglioAmmortamentoAvereCollegato(dettaglioDare,modelDetailsDettagli);
				checkDettaglio(dettaglioAvere);
				pnotaInserita = inserisciPrimaNota(dettaglioDare, dettaglioAvere);
				anteprimaAmmortamentoAnnuoCespiteDad.collegaDettagliPrimaNota(dettaglioDare, dettaglioAvere,pnotaInserita);
			}catch (RuntimeException e) {
				Errore errore = new Errore();
				errore.setCodice(primaNotaAmmortamentoNonInserita);
				String descrizione = new StringBuilder().append("Scartato dettaglio: ")
						.append(dettaglioDare != null && dettaglioDare.getUid() != 0 ? dettaglioDare.getUid() : "null" )
						.append(" , conto in dare: ")
						.append(dettaglioDare != null && dettaglioDare.getConto() != null && dettaglioDare.getConto().getCodice()!= null? dettaglioDare.getConto().getCodice() : "null")
						.append(" e conto in avere: ")
						.append(dettaglioAvere != null && dettaglioAvere.getConto() != null && dettaglioAvere.getConto().getCodice()!= null? dettaglioAvere.getConto().getCodice() : "null")
						.append(" . Errore riscontrato: ")
						.append(e.getMessage())
						.toString();
				errore.setDescrizione(descrizione);
				res.addErrore(errore);
				pnotaInserita = null;
			}
			
			addMessaggioPrimaNotaResponse(pnotaInserita);
		}
	}

	private void addMessaggioPrimaNotaResponse(PrimaNota pnotaInserita) {
		if(pnotaInserita == null) {
			return;
		}
		String desc= new StringBuilder()
				.append("Inserita prima nota")
				.append(pnotaInserita.getBilancio() != null? pnotaInserita.getBilancio().getAnno() : "")
				.append("/")
				.append(pnotaInserita.getNumero() != null? pnotaInserita.getNumero() : "")
				.toString();
		
		res.addMessaggio(primaNotaAmmortamentoInseritaCorrettamente, desc);
		
	}

	/**
	 * Check dettaglio.
	 *
	 * @param dett the dett
	 */
	private void checkDettaglio(DettaglioAnteprimaAmmortamentoAnnuoCespite dett) {
		if(dett == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("dettaglio"));
		}
		if(dett.getConto() == null || dett.getConto().getUid() == 0 || StringUtils.isBlank(dett.getConto().getCodice())) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("conto dettaglio"));
		}
		if(dett.getSegno() == null || dett.getImporto() == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("importo  e segno del dettaglio"));
		}
	}

	/**
	 * Inserisci prima nota.
	 *
	 * @param dettaglioDare the dettaglio dare
	 * @param dettaglioAvere the dettaglio avere
	 * @return the prima nota
	 */
	private PrimaNota inserisciPrimaNota(DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioDare, DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioAvere) {
		final String methodName="inserisciPrimaNota";
		
		InseriscePrimaNota reqIPN = new InseriscePrimaNota();
		reqIPN.setRichiedente(req.getRichiedente());
		reqIPN.setDataOra(new Date());
		Bilancio bil = caricaBilancio();
		reqIPN.setAnnoBilancio(Integer.valueOf(bil.getAnno()));
		log.debug(methodName, "creo la prima nota");
		
		PrimaNota primaNota = creaPrimaNota(bil,dettaglioDare, dettaglioAvere );
		reqIPN.setPrimaNota(primaNota);
		
		log.debug(methodName, "chiamo il servizio di inserimento prima nota.");
		InseriscePrimaNotaResponse resIPN = serviceExecutor.executeServiceSuccess(inseriscePrimaNotaService, reqIPN);
		checkServiceResponseFallimento(resIPN, "");
		PrimaNota pn = resIPN.getPrimaNota();
		
		//questo non dovrebbe succedere!!
		if(pn == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Prima nota inserita null"));
		}
		
		return pn;
	}

	/**
	 * Crea prima nota.
	 *
	 * @param bilancio the bilancio
	 * @param dettaglioDare the dettaglio dare
	 * @param dettaglioAvere the dettaglio avere
	 * @return the prima nota
	 */
	private PrimaNota creaPrimaNota(Bilancio bilancio, DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioDare, DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioAvere) {
		PrimaNota pn = new PrimaNota();

		pn.setAmbito(Ambito.AMBITO_INV);
		pn.setBilancio(bilancio);
		pn.setEnte(ente);
		pn.setTipoCausale(TipoCausale.Libera);
		pn.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		pn.setDataRegistrazione(new Date());
		pn.setDescrizione("");
		pn.setStatoAccettazionePrimaNotaProvvisoria(StatoAccettazionePrimaNotaProvvisoria.PROVVISORIO);

		MovimentoEP movimentoEP = new MovimentoEP();

		movimentoEP.setCausaleEP(this.causaleEP);
		movimentoEP.setAmbito(Ambito.AMBITO_INV);
		movimentoEP.setEnte(ente);
		
		movimentoEP.getListaMovimentoDettaglio().add(creaMovimentoDettaglio(dettaglioDare, Integer.valueOf(1)));
		movimentoEP.getListaMovimentoDettaglio().add(creaMovimentoDettaglio(dettaglioAvere, Integer.valueOf(1)));
		pn.getListaMovimentiEP().add(movimentoEP);
		return pn;
	}

	/**
	 * Crea movimento dettaglio.
	 *
	 * @param dettaglio the dettaglio
	 * @param numeroRiga the numero riga
	 * @return the movimento dettaglio
	 */
	private MovimentoDettaglio creaMovimentoDettaglio(DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglio,	Integer numeroRiga) {
		MovimentoDettaglio mov = new MovimentoDettaglio();
		mov.setAmbito(Ambito.AMBITO_INV);
		mov.setConto(dettaglio.getConto());
		mov.setEnte(ente);
		mov.setImporto(dettaglio.getImporto());
		mov.setSegno(dettaglio.getSegno());
		mov.setNumeroRiga(numeroRiga);
		return mov;
	}

	
	
	private Bilancio caricaBilancio(){
		
		Bilancio bilancio = bilancioDad.getBilancioByAnno(req.getAnnoBilancio());
		return bilancio;
	}
	
	/**
	 * Annulla prima nota precedente.
	 *
	 * @param primaNota the prima nota
	 */
	private void annullaPrimaNotaPrecedente(PrimaNota primaNota) {
		final String methodName ="annullaPrimaNotaPrecedente";
		if(primaNota == null || primaNota.getUid() == 0){
			log.debug(methodName, "Prima nota da annullare null o con uid non valorizzato.Esco.");
			return;
		}
		
		log.debug(methodName, "Annullo la prima Nota precedente [uid: " + primaNota.getUid()  + " ].");
		if(StatoOperativoPrimaNota.DEFINITIVO.equals(primaNota.getStatoOperativoPrimaNota()) && !req.isAnnullaPrimeNoteDefinitivePrecedenti()) {
			log.warn(methodName, "Non posso annullare la prima nota poiche' lo stato e' definitivo ed isAnnullaPrimeNoteDefinitivePrecedenti = " + req.isAnnullaPrimeNoteDefinitivePrecedenti());
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("esistono delle prime note definitive ma i parametri impostati non consentono il loro annullamento."));
		}
		
		primaNotaInvDad.aggiornaStatoPrimaNota(primaNota.getUid(), StatoOperativoPrimaNota.ANNULLATO);
		log.debug(methodName, "Prima Nota correttamente annullata.");
	}

	
}


