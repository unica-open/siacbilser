/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.exception.DadException;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoDettaglioModelDetail;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.MovimentoEPModelDetail;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.RegistrazioneMovFinModelDetail;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoCollegamento;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;

/**
 * Inserisce e completa gli Elenchi di Documenti Spesa, Entrata, IVA Spesa, Iva
 * Entrata, comprensivi di AllegatoAtto.
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RifiutaPrimaNotaSuRegistroACespiteService extends CheckedAccountBaseService<RifiutaPrimaNotaSuRegistroACespite, RifiutaPrimaNotaSuRegistroACespiteResponse> {

	private static final String CODICE_TIPO_RELAZIONE_PRIME_NOTE = "COGE-INV";
	
	private PrimaNota primaNotaContabilitaGenerale;
	private Bilancio bilancio;

	@Autowired
	@Inventario
	private PrimaNotaInvDad primaNotaInvDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private CausaleEPDad causaleEPDad;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;	
	@Autowired
	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	
	@Autowired
	private InseriscePrimaNotaService inseriscePrimaNotaService;
	
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		 checkEntita(req.getPrimaNota(), "prima nota");
	}

	@Override
	protected void init() {
		super.init();
		primaNotaInvDad.setEnte(ente);
		primaNotaInvDad.setLoginOperazione(loginOperazione);
		bilancioDad.setEnteEntity(ente);
		causaleEPDad.setEnte(ente);
	}

	@Transactional
	@Override
	public RifiutaPrimaNotaSuRegistroACespiteResponse executeService(RifiutaPrimaNotaSuRegistroACespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		//carico il bilancio
		caricaBilancio();
		
		// carico la prima nota che devo validare
		caricaPrimaNotaContabilitaGenerale();
		
		checkPrimaNota();
		
		inizializzaGestioneRegistrazioni();
		
		gestisciRifiutaSuPrimaNotaInventario();
		
		//Dall'analisi: Il rifiuto comporta la modifica dello stato della prima nota da ‘DA INTEGRARE’ a ‘RIFIUTATA’. NON SI PARLA DI ANNULLAMENTO DELLA PRIMA NOTA CORRISPONDENTE
		
		res.setPrimaNotaContabilitaGenerale(this.primaNotaContabilitaGenerale);
	}

	/**
	 * Gestisci rifiuta su prima nota inventario.
	 */
	private void gestisciRifiutaSuPrimaNotaInventario() {
		final String methodName ="gestisciRifiutaSuPrimaNotaInventario";
		
		if(this.primaNotaContabilitaGenerale.getPrimaNotaInventario() == null || this.primaNotaContabilitaGenerale.getPrimaNotaInventario().getUid()== 0) {
			//la prima nota  in inventario non è ancora stata inserita, lo faccio adesso
			log.debug(methodName, "La prima nota della contabilita' generale non e' ancora mai stata integrata su inventario. Inserisco una prima nota in stato annullato e rifiutato di ambito INV.");
			PrimaNota primaNotaInventario = inserisciPrimaNotaRifiutataSuInventario();
			log.debug(methodName, "Inserita prima nota uid: " + primaNotaInventario.getUid());
			return;
		}
		log.debug(methodName, "La prima nota della contabilita' generale non e' ancora mai stata integrata su inventario. Inserisco una prima nota in stato annullato e rifiutato di ambito INV.");
		if(TipoCausale.Integrata.equals(this.primaNotaContabilitaGenerale.getTipoCausale())) {
			annullaRegistrazioniAssociateAPrimaNota(this.primaNotaContabilitaGenerale.getPrimaNotaInventario());
		}	
		
		primaNotaInvDad.aggiornaStatoOperativoPrimaNota(this.primaNotaContabilitaGenerale.getPrimaNotaInventario(), StatoOperativoPrimaNota.ANNULLATO, StatoAccettazionePrimaNotaDefinitiva.RIFIUTATO);
	}
	
	private void annullaRegistrazioniAssociateAPrimaNota(PrimaNota primaNota) {
		final String methodName ="annullaRegistrazioniAssociateAPrimaNota";
		List<RegistrazioneMovFin> registrazioniDaAnnullare = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinValideAssociateAPrimaNota(this.primaNotaContabilitaGenerale.getPrimaNotaInventario());
		if(registrazioniDaAnnullare == null) {
			log.debug(methodName, "Non ci sono registrazioni in stato diversoo da annullato per la prima nota, non devo piu' annullarle.");
			return;
		}
		for (RegistrazioneMovFin registrazioneMovFin : registrazioniDaAnnullare) {
			log.debug(methodName, "Annullo la registrazione: " + registrazioneMovFin.getUid());
			registrazioneGENServiceHelper.aggiornaStatoRegistrazione(registrazioneMovFin, StatoOperativoRegistrazioneMovFin.ANNULLATO);
		}
	}
	/**
	 * Inizializza gestione registrazioni.
	 */
	private void inizializzaGestioneRegistrazioni() {
		if(!TipoCausale.Integrata.equals(this.primaNotaContabilitaGenerale.getTipoCausale())) {
			return;	
		}
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, this.bilancio);
	}

	/**
	 * @param movEP
	 * @return
	 */

	/**
	 * Carica bilancio.
	 */
	private void caricaBilancio() {
		this.bilancio = bilancioDad.getBilancioByAnno(req.getAnnoBilancio().intValue());
		if(this.bilancio == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Bilancio non trovato per anno" + req.getAnnoBilancio()));
		}
	}
	
	/**
	 * Carica prima nota ambito di ambito INV che deve essere validata.
	 */
	private void caricaPrimaNotaContabilitaGenerale() {
		final String methodName ="caricaPrimaNotaAmbitoINV";
		
		Utility.MDTL.addModelDetails(
				MovimentoEPModelDetail.CausaleEPModelDetail, MovimentoEPModelDetail.RegistrazioneMovFinModelDetail, MovimentoEPModelDetail.MovimentoDettaglioModelDetail,
				MovimentoEPModelDetail.MovimentoDettaglioModelDetail ,MovimentoDettaglioModelDetail.ContoModelDetail, MovimentoDettaglioModelDetail.Segno, MovimentoDettaglioModelDetail.Cespiti,
				RegistrazioneMovFinModelDetail.EventoMovimento, RegistrazioneMovFinModelDetail.PianoDeiConti
				);
		log.debug(methodName, "carico da db la prima nota con uid: " + req.getPrimaNota().getUid());
		this.primaNotaContabilitaGenerale = primaNotaInvDad.findPrimaNotaByUid(req.getPrimaNota().getUid(), new PrimaNotaModelDetail[] {PrimaNotaModelDetail.MovimentiEpModelDetail, PrimaNotaModelDetail.TipoCausale, PrimaNotaModelDetail.Soggetto, PrimaNotaModelDetail.Ambito, PrimaNotaModelDetail.PrimaNotaInventario});
	}

	/**
	 * Check prima nota ambito INV.
	 */
	private void checkPrimaNota() {
		final String methodName ="checkPrimaNotaAmbitoINV";
		checkPrimaNotaCoGe();
		
		checkPrimaNotaINV();
		
	}

	/**
	 * @param methodName
	 */
	private void checkPrimaNotaINV() {
		final String methodName = "checkPrimaNotaINV";
		if(this.primaNotaContabilitaGenerale.getPrimaNotaInventario() == null || this.primaNotaContabilitaGenerale.getPrimaNotaInventario().getUid() == 0) {
			return;
		}
		if(StatoAccettazionePrimaNotaDefinitiva.INTEGRATO.equals(this.primaNotaContabilitaGenerale.getPrimaNotaInventario().getStatoAccettazionePrimaNotaDefinitiva())) {
			log.debug(methodName, "Prima nota risulta essere integrata.");
			String descStato = this.primaNotaContabilitaGenerale.getPrimaNotaInventario().getStatoAccettazionePrimaNotaDefinitiva() != null? this.primaNotaContabilitaGenerale.getPrimaNotaInventario().getStatoAccettazionePrimaNotaDefinitiva().getDescrizione() : " null";
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impossibile rifiutare la prima nota: lo stato in inventario risulta essere: " + descStato ));
		}
		
		Long numeroCespitiCollegati = primaNotaInvDad.countCespitiAssociatiAPrimaNota(this.primaNotaContabilitaGenerale.getPrimaNotaInventario());
		
		if(numeroCespitiCollegati!= null && Long.valueOf(0L).compareTo(numeroCespitiCollegati) != 0) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Esistono relazioni attive con schede cespite. Prima di Rifiutare la Prima Nota &egrave; necessario annullare tutte le relazioni con le schede cespite. "));
		}
	}

	/**
	 * @param methodName
	 */
	private void checkPrimaNotaCoGe() {
		final String methodName ="checkPrimaNotaCoGe";
		if(this.primaNotaContabilitaGenerale == null || this.primaNotaContabilitaGenerale.getUid() == 0) {
			log.debug(methodName, "Nessuna prima nota trovata su base dati.");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("validazione prima nota con uid: " + req.getPrimaNota().getUid(), "prima nota"));
		}
		
		if(StatoOperativoPrimaNota.PROVVISORIO.equals(this.primaNotaContabilitaGenerale.getStatoOperativoPrimaNota())) {
			log.debug(methodName, "La prima nota ha stato operativo annullato.");
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("prima nota",this.primaNotaContabilitaGenerale.getStatoOperativoPrimaNota().getDescrizione()));
		}
		
		if(!Ambito.AMBITO_FIN.equals(this.primaNotaContabilitaGenerale.getAmbito())) {
			log.debug(methodName, "La prima nota ha ambito errato.");
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("prima nota", "ambito non coerente, deve essere ambito di contabilita finanziaria."));
		}
		
		if(this.primaNotaContabilitaGenerale.getTipoCausale() == null) {
			log.debug(methodName, "La prima nota ha il tipo causale.");
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("prima nota", "tipo causale non presente."));
		}
		
		if(this.primaNotaContabilitaGenerale.getListaMovimentiEP() == null || this.primaNotaContabilitaGenerale.getListaMovimentiEP().isEmpty()) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("prima nota su contabilita' generale", "movimenti ep non presenti."));
		}
		
		if(!TipoCausale.Integrata.equals(this.primaNotaContabilitaGenerale.getTipoCausale())) {
			return;
		}
		
		checkPrimaNotaDaNCD();
			
	}

	/**
	 * 
	 */
	private void checkPrimaNotaDaNCD() {
		RegistrazioneMovFin registrazioneAssociata = this.primaNotaContabilitaGenerale.getListaMovimentiEP().get(0).getRegistrazioneMovFin();
		boolean isEventoDocumentoDispesa = registrazioneAssociata != null && TipoCollegamento.DOCUMENTO_SPESA.getCodice().equals(registrazioneAssociata.getEvento().getCodice());
		
		if(!isEventoDocumentoDispesa) {
			return;
		}		
		SubdocumentoSpesa subdoc = (SubdocumentoSpesa) registrazioneAssociata.getMovimento();
		boolean isNotaCredito = subdoc.getDocumento().getTipoDocumento() != null && subdoc.getDocumento().getTipoDocumento().isNotaCredito();
		if(!isNotaCredito) {
			return;
		}
		
		DocumentoSpesa ds = documentoSpesaDad.findDocumentiCollegatiByIdDocumento(subdoc.getDocumento().getUid());
		List<PrimaNota> primeNoteNotaCredito = caricaPrimeNoteDocumenti(ds.getListaDocumentiSpesaPadre(), registrazioneAssociata.getEvento());
		for (PrimaNota primaNota : primeNoteNotaCredito) {
			PrimaNota pnInventario = primaNota.getPrimaNotaInventario();
			if(pnInventario == null || !StatoAccettazionePrimaNotaDefinitiva.RIFIUTATO.equals(pnInventario.getStatoAccettazionePrimaNotaDefinitiva()) ) {
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore( "Prima nota generata da nota di credito: tutte le prime note delle fatture collegate devono essere in stato " + StatoAccettazionePrimaNotaDefinitiva.RIFIUTATO.getDescrizione())); 
			}
		}
		
	}
	
	/**
	 * Inserisci prima nota ambito FIN.
	 *
	 * @return the prima nota
	 */
	private PrimaNota inserisciPrimaNotaRifiutataSuInventario() {
		final String methodName = "inserisciPrimaNotaAmbitoFIN";
		InseriscePrimaNota reqIPN = new InseriscePrimaNota();
		reqIPN.setRichiedente(req.getRichiedente());
		reqIPN.setDataOra(new Date());
		reqIPN.setAnnoBilancio(Integer.valueOf(this.bilancio.getAnno()));
		reqIPN.setSovrascriviDefaultStatoOperativo(true);
		reqIPN.setSaltaOperazioniPreliminarisuRegistrazioni(true);
		
		PrimaNota primaNotaINV = creaPrimaNotaInventario();	
		
		reqIPN.setPrimaNota(primaNotaINV);		
		
		log.debug(methodName, "Creata prima nota di ambito INV da inserire.");
		
		InseriscePrimaNotaResponse resIPN = serviceExecutor.executeServiceSuccess(inseriscePrimaNotaService, reqIPN);
		checkServiceResponseFallimento(resIPN);
		
		log.debug(methodName, "Inserita prima nota definitiva in ambito INV: " + resIPN.getPrimaNota().getNumeroRegistrazioneLibroGiornale() + " con uid: " + resIPN.getPrimaNota().getUid());
		return resIPN.getPrimaNota();
	}

	
	/**
	 * Crea prima nota FIN.
	 *
	 * @return the prima nota
	 */
	private PrimaNota creaPrimaNotaInventario() {
		PrimaNota pn = new PrimaNota();

		pn.setAmbito(Ambito.AMBITO_INV);
		pn.setBilancio(bilancio);
		pn.setEnte(ente);
		pn.setTipoCausale(this.primaNotaContabilitaGenerale.getTipoCausale());
		pn.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.ANNULLATO);
		pn.setStatoAccettazionePrimaNotaDefinitiva(StatoAccettazionePrimaNotaDefinitiva.RIFIUTATO);
		pn.setDataRegistrazione(new Date());
		pn.setDescrizione("");
		pn.setSoggetto(this.primaNotaContabilitaGenerale.getSoggetto());

		
		for(MovimentoEP movimentoEPINV : this.primaNotaContabilitaGenerale.getListaMovimentiEP()) {
			MovimentoEP movimentoEP = copiaMovimentoEP(movimentoEPINV);
			pn.getListaMovimentiEP().add(movimentoEP);
		}
		
		impostaCollegamentoConPrimaNotaContabilitaGenerale(pn);
		
		return pn;
	}
	
	/**
	 * Imposta collegamento con prim A nota INV.
	 *
	 * @param pnFiglia the pn figlia
	 */
	private void impostaCollegamentoConPrimaNotaContabilitaGenerale(PrimaNota primaNotaDaCollegare) {
		TipoRelazionePrimaNota tipoRelazionePrimaNota = primaNotaInvDad.findTipoRelazioneByCodice(CODICE_TIPO_RELAZIONE_PRIME_NOTE);
		this.primaNotaContabilitaGenerale.setTipoRelazionePrimaNota(tipoRelazionePrimaNota);
		//la metto come prima nota padre in quanto e' padre
		primaNotaDaCollegare.getListaPrimaNotaPadre().add(this.primaNotaContabilitaGenerale);
	}

	/**
	 * Copia movimento EP.
	 *
	 * @param movimentoEPFIN the movimento EPFIN
	 * @return the movimento EP
	 */
	private MovimentoEP copiaMovimentoEP(MovimentoEP movimentoEPFIN) {
		MovimentoEP movimentoEP = new MovimentoEP();
		movimentoEP.setCausaleEP(caricaCausaleEPInventario(movimentoEPFIN.getCausaleEP()));
		movimentoEP.setAmbito(Ambito.AMBITO_INV);
		movimentoEP.setEnte(ente);
		RegistrazioneMovFin nuovaRegistrazioneINV = inserisciNuovaRegistrazioneAPartireDaRegistrazioneFIN(movimentoEPFIN.getRegistrazioneMovFin());
		movimentoEP.setRegistrazioneMovFin(nuovaRegistrazioneINV);
		
		for (MovimentoDettaglio movimentoDettaglioFIN : movimentoEPFIN.getListaMovimentoDettaglio()){
			
			MovimentoDettaglio movimentoDettaglioINV = new MovimentoDettaglio();
			movimentoDettaglioINV.setAmbito(Ambito.AMBITO_INV);
			movimentoDettaglioINV.setConto(movimentoDettaglioFIN.getConto());
			movimentoDettaglioINV.setEnte(ente);
			movimentoDettaglioINV.setImporto(movimentoDettaglioFIN.getImporto());
			movimentoDettaglioINV.setSegno(movimentoDettaglioFIN.getSegno());
			movimentoDettaglioINV.setNumeroRiga(movimentoDettaglioFIN.getNumeroRiga());
			movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioINV);
		}
		return movimentoEP;
	}
	
	/**
	 * Ottieni registrazione mov FIN.
	 *
	 * @param registrazioneMovFinDiPartenza the registrazione mov fin di partenza
	 * @return the registrazione mov fin
	 */
	private RegistrazioneMovFin inserisciNuovaRegistrazioneAPartireDaRegistrazioneFIN(RegistrazioneMovFin registrazioneMovFinDiPartenza) {
		if(!TipoCausale.Integrata.equals(this.primaNotaContabilitaGenerale.getTipoCausale())) {
			return null;	
		}
		if(registrazioneMovFinDiPartenza == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("la prima nota risulta essere integrata ma almeno un suo movimento EP non presenta registrazioni."));
		}
		RegistrazioneMovFin registrazioneMovFinInserita = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(registrazioneMovFinDiPartenza.getEvento(), registrazioneMovFinDiPartenza.getMovimento(),registrazioneMovFinDiPartenza.getMovimentoCollegato(), registrazioneMovFinDiPartenza.getElementoPianoDeiContiAggiornato(), registrazioneMovFinDiPartenza.getElementoPianoDeiContiAggiornato(), Ambito.AMBITO_INV);
		return registrazioneMovFinInserita;
		
	}
	
	/**
	 * Carica causale EP da prima nota INV.
	 *
	 * @param causaleEPINV the causale EPINV
	 * @return the causale EP
	 */
	private CausaleEP caricaCausaleEPInventario(CausaleEP causaleEPDaCercare) {
		CausaleEP criteriDiRicerca = new CausaleEP();
		criteriDiRicerca.setCodice(causaleEPDaCercare.getCodice());
		criteriDiRicerca.setAmbito(Ambito.AMBITO_FIN);
		// N.B: queste ricerca puo' essere ittimizzata, ho bisogno solo dell'id
		CausaleEP causaleEP = causaleEPDad.ricercaCausaleEPByCodice(criteriDiRicerca);
		return causaleEP;
	}
	
	/**
	 * Carica documento che ha generato la prima nota.
	 *
	 * @param primaNota the prima nota
	 * @return the documento spesa
	 */
	private DocumentoSpesa caricaDocumentoCheHaGeneratoLaPrimaNota(PrimaNota primaNota) {
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione(0, 1);
		SubdocumentoSpesaModelDetail[] subdocModelDetail = new SubdocumentoSpesaModelDetail[]{SubdocumentoSpesaModelDetail.DocPadreModelDetail};
		ListaPaginata<Entita> entitas = primaNotaInvDad.ottieniEntitaCollegatePrimaNota(primaNota, TipoCollegamento.SUBDOCUMENTO_SPESA, subdocModelDetail, parametriPaginazione);
		if(entitas == null || entitas.getTotaleElementi() == 0) {
			return null;
		}
		SubdocumentoSpesa ss = (SubdocumentoSpesa) entitas.get(0);
		
		DocumentoSpesa documento = ss.getDocumento();
		return documento;
	}
	
	/**
	 * @param docs
	 */
	private List<PrimaNota> caricaPrimeNoteDocumenti(List<DocumentoSpesa> docs, Evento evento) {
		final String methodName="caricaPrimeNoteDocumenti";
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione(0, Integer.MAX_VALUE);
		List<Integer> uidsDocumento = Utility.projectToUidList(docs);
		ListaPaginata<PrimaNota> primeNote = null;
		try {
			primeNote = primaNotaInvDad.ricercaSinteticaPrimeNoteIntegrateRegistroA(this.bilancio, new PrimaNota(), null, null, null, null, null, null, null, Arrays.asList(StatoOperativoPrimaNota.PROVVISORIO, StatoOperativoPrimaNota.DEFINITIVO), null, "S", Arrays.asList(evento.getTipoEvento()), null, null, null, null, null, null, null, null, uidsDocumento, null, null, null, null, parametriPaginazione, PrimaNotaModelDetail.PrimaNotaInventario);
		} catch (DadException e) {
			log.error(methodName, "errore durante l'elaborazione del dad");
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore durante la ricerca della prima nota nel DAD: " + e.getMessage()));
		}
		
		return primeNote;
	}
	
}
