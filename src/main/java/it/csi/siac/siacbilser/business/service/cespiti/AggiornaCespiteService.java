/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.math.MathContext;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.integration.dad.TipoBeneCespiteDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaProvvisoria;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCespiteService extends CheckedAccountBaseService<AggiornaCespite, AggiornaCespiteResponse> {

	//DAD
	@Autowired
	private CespiteDad cespiteDad;
	@Autowired
	private TipoBeneCespiteDad tipoBeneCespiteDad;
	@Autowired
	private CausaleEPDad causaleEPDad;	
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	@Inventario
	PrimaNotaInvDad primaNotaInvDad;
	@Autowired
	private InseriscePrimaNotaService inseriscePrimaNotaService;

	private Cespite cespite;
	private boolean isDonazione = false;
	private PrimaNota primaNota = null;
	private TipoBeneCespite tipoBeneCespite;
	private boolean gestisciPrimaNota = false;
	
	private static final TipoBeneCespiteModelDetail[] modelDetailCespite = new TipoBeneCespiteModelDetail[] {
			TipoBeneCespiteModelDetail.Annullato };

	private static final TipoBeneCespiteModelDetail[] modelDetailDonazione = new TipoBeneCespiteModelDetail[] {
			TipoBeneCespiteModelDetail.Annullato, TipoBeneCespiteModelDetail.ContoPatrimoniale,
			TipoBeneCespiteModelDetail.ContoDonazione };
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		cespite = req.getCespite();
		
		checkEntita(cespite,"cespite");
		checkNotBlank(cespite.getCodice(), "codice cespite", false);
		checkNotBlank(cespite.getDescrizione(), "descrizione cespite", false);
		checkEntita(cespite.getTipoBeneCespite(), "tipo bene");
		checkNotNull(cespite.getClassificazioneGiuridicaCespite(), "classificazione giuridica", false);
		checkNotNull(cespite.getFlagStatoBene(), "stato bene cespite");
		checkNotNull(cespite.getValoreIniziale(), "valore iniziale", false);
		checkNotNull(cespite.getDataAccessoInventario(), "data accesso inventario", false);
	}
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);
		tipoBeneCespiteDad.setEnte(ente);
		causaleEPDad.setEnte(ente);
		bilancioDad.setEnteEntity(ente);
		primaNotaInvDad.setEnte(ente);
		primaNotaInvDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public AggiornaCespiteResponse executeService(AggiornaCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		isDonazione = Boolean.TRUE.equals(cespite.getFlgDonazioneRinvenimento());
		
		caricaDatiCespiteDB();
		
		checkCodiceGiaEsistente();
		
		caricaDatiPrimaNotaInventario();
		
		caricaTipoBeneCespite();
		checkTipoBeneCespite();
		
		popolaDatiDefault();
		
		
		Cespite cespiteAggiornato = cespiteDad.aggiornaCespite(req.getCespite());
		
		gestisciPrimaNota(cespiteAggiornato);
		
		res.setCespite(cespiteAggiornato);
	}
	
	/**
	 * Check codice gia esistente.
	 */
	private void checkCodiceGiaEsistente() {
		Cespite ces = cespiteDad.findByCodice(cespite.getCodice());
		//ANTO SIAC-6704
		if (ces != null && ces.getUid() != cespite.getUid()) {			
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Cespite", cespite.getCodice()));		
		}
	}
	
	/**
	 * Carica dati prima nota inventario.
	 */
	private void caricaDatiPrimaNotaInventario() {
		if (!this.isDonazione || !this.gestisciPrimaNota) {
			return;
		}
		PrimaNota pn = new PrimaNota();
		//todo: che cosa succede se il cespite e' collegato ad una prima nota definitiva????
		pn.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		List<PrimaNota> primeNoteCollegate = primaNotaInvDad.ricercaScrittureInventarioByEntita(cespite,null, pn, Boolean.TRUE, new PrimaNotaModelDetail[] {}, Ambito.AMBITO_INV);
		if(primeNoteCollegate == null || primeNoteCollegate.isEmpty()) {
			return;
		}
		primaNota = primeNoteCollegate.get(0);
	}

	private void caricaDatiCespiteDB() {
		 Cespite cespiteVecchio = cespiteDad.findCespiteById(cespite, new CespiteModelDetail[] {});
		 if(cespiteVecchio == null) {
			 throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile trovare un cespite con l'uid indicato."));
		 }
		 if(cespite.getValoreIniziale().compareTo(cespiteVecchio.getValoreIniziale()) != 0) {
			 gestisciPrimaNota  = true;
			 cespite.setValoreAttuale(cespite.getValoreIniziale().subtract(cespiteVecchio.getValoreIniziale(), MathContext.DECIMAL128).add(cespiteVecchio.getValoreAttualeNotNull()));
		 }
	}
	
	/**
	 * Popolamento dei dati di default
	 */
	private void popolaDatiDefault() {
		if(cespite.getFlagSoggettoTutelaBeniCulturali() == null) {
			cespite.setFlagSoggettoTutelaBeniCulturali(Boolean.FALSE);
		}
	}

	/**
	 * Controlla che il tipo bene esista e non sia annullato
	 */
	private void caricaTipoBeneCespite() {
		TipoBeneCespite tbc = cespite.getTipoBeneCespite();
		tbc.setDataInizioValiditaFiltro(Utility.primoGiornoDellAnno(req.getAnnoBilancio()));
		tipoBeneCespite = tipoBeneCespiteDad.findDettaglioTipoBeneCespiteById(tbc, 
				this.isDonazione ? modelDetailDonazione : modelDetailCespite
		);
	}
	
	private void checkTipoBeneCespite() {
		if(tipoBeneCespite == null || Boolean.TRUE.equals(tipoBeneCespite.getAnnullato())) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile tipo bene non annullato con uid: " + cespite.getTipoBeneCespite().getUid() +" ."));
		}
		boolean valorizzatoContoPatrimoniale = tipoBeneCespite.getContoPatrimoniale() != null
				&& tipoBeneCespite.getContoPatrimoniale().getUid() != 0;
		boolean valorizzatoContoDonazione = tipoBeneCespite.getContoDonazione() != null
				&& tipoBeneCespite.getContoDonazione().getUid() != 0;
		if (this.isDonazione && (!valorizzatoContoPatrimoniale || !valorizzatoContoDonazione)) {
			throw new BusinessException(
					ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Conti Tipo bene non indicati correttamente"));
		}
	}
	
	/**
	 * @param cespiteInserito
	 */
	private void gestisciPrimaNota(Cespite cespiteInserito) {
		if (!this.isDonazione || !gestisciPrimaNota) {
			return;
		}
		annullaPrimaNotaPrecedente();
		PrimaNota primaNotaInserita = inserisciPrimaNotaDonazione();
		cespiteDad.associaPrimaNota(cespiteInserito, primaNotaInserita);
	}
	
	/**
	 * Annulla prima nota precedente.
	 */
	private void annullaPrimaNotaPrecedente() {
		final String methodName ="annullaPrimaNotaPrecedente";
		if(primaNota == null || primaNota.getUid() == 0){
			return;
		}
		log.debug(methodName, "Annullo la prima Nota precedente [uid: " + primaNota.getUid()  + " ].");
		primaNotaInvDad.aggiornaStatoPrimaNota(primaNota.getUid(), StatoOperativoPrimaNota.ANNULLATO);
	}

	private PrimaNota inserisciPrimaNotaDonazione() {
		
		InseriscePrimaNota reqIPN = new InseriscePrimaNota();
		reqIPN.setRichiedente(req.getRichiedente());
		reqIPN.setDataOra(new Date());
		Bilancio bil = caricaBilancio();
		reqIPN.setAnnoBilancio(Integer.valueOf(bil.getAnno()));
		PrimaNota primaNota = creaPrimaNota(bil);
		reqIPN.setPrimaNota(primaNota);
		InseriscePrimaNotaResponse resIPN = serviceExecutor.executeServiceSuccess(inseriscePrimaNotaService, reqIPN);
		return resIPN.getPrimaNota();
	}

	private PrimaNota creaPrimaNota(Bilancio bilancio) {
		PrimaNota pn = new PrimaNota();

		pn.setAmbito(Ambito.AMBITO_INV);
		pn.setBilancio(bilancio);
//		pn.setDescrizione("Prima nota libera che simula la prima nota inserita da cespite");
		pn.setEnte(ente);
		pn.setTipoCausale(TipoCausale.Libera);
		pn.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		pn.setDataRegistrazione(new Date());
		pn.setDescrizione("");
		pn.setStatoAccettazionePrimaNotaProvvisoria(StatoAccettazionePrimaNotaProvvisoria.PROVVISORIO);

		MovimentoEP movimentoEP = new MovimentoEP();

		movimentoEP.setCausaleEP(caricaCausaleEP());
		movimentoEP.setAmbito(Ambito.AMBITO_INV);
		movimentoEP.setEnte(ente);

		MovimentoDettaglio movimentoDettaglioDare = creaMovimentoDettaglio(tipoBeneCespite.getContoPatrimoniale(),
				OperazioneSegnoConto.DARE, Integer.valueOf(1));
		MovimentoDettaglio movimentoDettaglioAvere = creaMovimentoDettaglio(tipoBeneCespite.getContoDonazione(),
				OperazioneSegnoConto.AVERE, Integer.valueOf(1));

		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioDare);
		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioAvere);
		pn.getListaMovimentiEP().add(movimentoEP);
		return pn;
	}

	private CausaleEP caricaCausaleEP() {
		CausaleEP criteriDiRicerca = new CausaleEP();
		criteriDiRicerca.setCodice("DON");
		criteriDiRicerca.setAmbito(Ambito.AMBITO_INV);
		// N.B: queste ricerca puo' essere ittimizzata, ho bisogno solo dell'id
		CausaleEP causaleEP = causaleEPDad.ricercaCausaleEPByCodice(criteriDiRicerca);
		return causaleEP;
	}

	private MovimentoDettaglio creaMovimentoDettaglio(Conto contoPatrimoniale,
			OperazioneSegnoConto operazioneSegnoConto, Integer numeroRiga) {
		MovimentoDettaglio mov = new MovimentoDettaglio();
		mov.setAmbito(Ambito.AMBITO_INV);
		mov.setConto(contoPatrimoniale);
		mov.setEnte(ente);
		mov.setImporto(cespite.getValoreAttuale());
		mov.setSegno(operazioneSegnoConto);
		mov.setNumeroRiga(numeroRiga);
		return mov;
	}
	
	
	protected Bilancio caricaBilancio(){
		Bilancio bilancio = bilancioDad.getBilancioByAnno(req.getAnnoBilancio());
		return bilancio;
	}

	

}