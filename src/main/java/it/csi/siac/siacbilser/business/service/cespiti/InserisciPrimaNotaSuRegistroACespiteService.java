/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.ValidaPrimaNotaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNotaResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
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
import it.csi.siac.siacgenser.model.TipoCausale;
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
public class InserisciPrimaNotaSuRegistroACespiteService extends CheckedAccountBaseService<InserisciPrimaNotaSuRegistroACespite, InserisciPrimaNotaSuRegistroACespiteResponse> {

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
	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	
	@Autowired
	private InseriscePrimaNotaService inseriscePrimaNotaService;
	
	@Autowired
	private ValidaPrimaNotaService validaPrimaNotaService;
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getPrimaNota(), "prima nota da validare");

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
	public InserisciPrimaNotaSuRegistroACespiteResponse executeService(InserisciPrimaNotaSuRegistroACespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		//carico il bilancio
		caricaBilancio();
		
		// carico la prima nota che devo validare
		caricaPrimaNotaContabilitaGenerale();
		
		checkPrimaNotaContabilitaGenerale();
		
		inizializzaGestioneRegistrazioni();
		
		PrimaNota primaNotaInventario = inserisciPrimaNotaSuInventario();
		
		res.setPrimaNotaContabilitaGenerale(this.primaNotaContabilitaGenerale);
		res.setPrimaNotaInventario(primaNotaInventario);
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
				MovimentoEPModelDetail.MovimentoDettaglioModelDetail ,MovimentoDettaglioModelDetail.ContoModelDetail, MovimentoDettaglioModelDetail.Segno,
				RegistrazioneMovFinModelDetail.EventoMovimento, RegistrazioneMovFinModelDetail.PianoDeiConti
				);
		log.debug(methodName, "carico da db la prima nota con uid: " + req.getPrimaNota().getUid());
		this.primaNotaContabilitaGenerale = primaNotaInvDad.findPrimaNotaByUid(req.getPrimaNota().getUid(), new PrimaNotaModelDetail[] {PrimaNotaModelDetail.MovimentiEpModelDetail, PrimaNotaModelDetail.TipoCausale, PrimaNotaModelDetail.Soggetto, PrimaNotaModelDetail.Ambito, PrimaNotaModelDetail.PrimaNotaInventario});
	}

	/**
	 * Check prima nota ambito INV.
	 */
	private void checkPrimaNotaContabilitaGenerale() {
		final String methodName ="checkPrimaNotaAmbitoINV";
		if(this.primaNotaContabilitaGenerale == null || this.primaNotaContabilitaGenerale.getUid() == 0) {
			log.debug(methodName, "Nessuna prima nota trovata su base dati.");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("validazione prima nota con uid: " + req.getPrimaNota().getUid(), "prima nota"));
		}
		
		if(StatoOperativoPrimaNota.ANNULLATO.equals(this.primaNotaContabilitaGenerale.getStatoOperativoPrimaNota())) {
			log.debug(methodName, "La prima nota ha stato operativo annullato.");
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("prima nota",StatoOperativoPrimaNota.ANNULLATO.getDescrizione()));
		}
		
		if(!Ambito.AMBITO_FIN.equals(this.primaNotaContabilitaGenerale.getAmbito())) {
			log.debug(methodName, "La prima nota ha ambito errato.");
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("prima nota", "ambito non coerente, deve essere ambito di contabilita finanziaria."));
		}
		
		if(this.primaNotaContabilitaGenerale.getTipoCausale() == null) {
			log.debug(methodName, "La prima nota ha il tipo causale.");
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("prima nota", "tipo causale non presente."));
		}
		
		if(this.primaNotaContabilitaGenerale.getPrimaNotaInventario() != null && this.primaNotaContabilitaGenerale.getPrimaNotaInventario().getUid() != 0) {
			log.debug(methodName, "Prima nota gia' integrata.");
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("la prima nota risulta essere gia' integrata."));
		}
		
		//valutare se questo controllo abbia senso e nel caso caricare il bilancio
//		if(this.primaNotaContabilitaGenerale.getBilancio() == null || this.bilancio.getUid() != this.primaNotaContabilitaGenerale.getBilancio().getUid()) {
//			log.debug(methodName, "bilancio non presente o non coerente con bilancio attuale.");
//			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("prima nota", "bilancio non presente o non coerente con bilancio attuale."));
//		}
		
		if(this.primaNotaContabilitaGenerale.getListaMovimentiEP() == null || this.primaNotaContabilitaGenerale.getListaMovimentiEP().isEmpty()) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("prima nota ambito inventario", "movimenti ep non presenti."));
		}
		
	}
	
	/**
	 * Inserisci prima nota ambito FIN.
	 *
	 * @return the prima nota
	 */
	private PrimaNota inserisciPrimaNotaSuInventario() {
		final String methodName = "inserisciPrimaNotaAmbitoFIN";
		InseriscePrimaNota reqIPN = new InseriscePrimaNota();
		reqIPN.setRichiedente(req.getRichiedente());
		reqIPN.setDataOra(new Date());
		reqIPN.setAnnoBilancio(Integer.valueOf(this.bilancio.getAnno()));
		reqIPN.setSaltaOperazioniPreliminarisuRegistrazioni(true);
		
		PrimaNota primaNotaINV = creaPrimaNotaInventario();	
		
		reqIPN.setPrimaNota(primaNotaINV);		
		
		log.debug(methodName, "Creata prima nota di ambito INV da inserire.");
//		log.logXmlTypeObject(primaNotaINV, "primaNotaINV");
		
		primaNotaInvDad.flush();
		
		InseriscePrimaNotaResponse resIPN = serviceExecutor.executeServiceSuccess(inseriscePrimaNotaService, reqIPN);
		
		checkServiceResponseFallimento(resIPN);
		
		resIPN.getPrimaNota().setStatoAccettazionePrimaNotaDefinitiva(StatoAccettazionePrimaNotaDefinitiva.DA_ACCETTARE);
		
		validaPrimaNota(resIPN.getPrimaNota());
		
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
		RegistrazioneMovFin registrazioneMovFinInserita = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(registrazioneMovFinDiPartenza.getEvento(), registrazioneMovFinDiPartenza.getMovimento(),registrazioneMovFinDiPartenza.getMovimentoCollegato(),  registrazioneMovFinDiPartenza.getElementoPianoDeiContiAggiornato(), Ambito.AMBITO_INV);
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
	 * Valida prima nota.
	 *
	 * @param primaNota the prima nota
	 */
	private void validaPrimaNota(PrimaNota primaNota) {
		ValidaPrimaNota reqVPN = new ValidaPrimaNota();
		reqVPN.setPrimaNota(primaNota);
		reqVPN.setRichiedente(req.getRichiedente());
		ValidaPrimaNotaResponse resVPN = executeExternalServiceSuccess(validaPrimaNotaService, reqVPN, ErroreCore.OPERAZIONE_NON_CONSENTITA.getCodice());
		if(resVPN.verificatoErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA)){ //SIAC-3089
			res.addErrori(resVPN.getErrori());
			throw new BusinessException("Errore durante la validazione della prima nota.");
		}
	}
	
}
