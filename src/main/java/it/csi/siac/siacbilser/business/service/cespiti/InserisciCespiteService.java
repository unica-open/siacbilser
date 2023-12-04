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
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siacbilser.integration.dad.DettaglioAmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.TipoBeneCespiteDad;
import it.csi.siac.siacbilser.integration.utility.cespite.CespiteInventarioWrapper;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
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
public class InserisciCespiteService extends CheckedAccountBaseService<InserisciCespite, InserisciCespiteResponse> {

	// DAD
	@Autowired
	private CespiteDad cespiteDad;

	@Autowired
	private TipoBeneCespiteDad tipoBeneCespiteDad;
	
	@Autowired
	private AmmortamentoAnnuoCespiteDad ammortamentoAnnuoCespiteDad;

	@Autowired
	private DettaglioAmmortamentoAnnuoCespiteDad dettaglioAmmortamentoAnnuoCespiteDad;

	@Autowired
	private CausaleEPDad causaleEPDad;
	
	@Autowired
	private BilancioDad bilancioDad;

	@Autowired
	private InseriscePrimaNotaService inseriscePrimaNotaService;

	private Cespite cespite;

	private TipoBeneCespite tipoBeneCespite;

	private boolean isDonazione = false;

	private static final TipoBeneCespiteModelDetail[] modelDetailCespite = new TipoBeneCespiteModelDetail[] {
			TipoBeneCespiteModelDetail.Annullato };

	private static final TipoBeneCespiteModelDetail[] modelDetailDonazione = new TipoBeneCespiteModelDetail[] {
			TipoBeneCespiteModelDetail.Annullato, TipoBeneCespiteModelDetail.ContoPatrimoniale,
			TipoBeneCespiteModelDetail.ContoDonazione };

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		cespite = req.getCespite();
		checkNotNull(cespite, "cespite");

		checkNotBlank(cespite.getCodice(), "codice cespite", false);
		checkNotBlank(cespite.getDescrizione(), "descrizione cespite", false);
		checkEntita(cespite.getTipoBeneCespite(), "tipo bene");
		checkNotNull(cespite.getClassificazioneGiuridicaCespite(), "classificazione giuridica", false);
		checkNotNull(cespite.getFlagStatoBene(), "stato bene cespite");
		checkNotNull(cespite.getValoreIniziale(), "valore iniziale", false);
		checkNotNull(cespite.getDataAccessoInventario(), "data accesso inventario", false);
		checkNotNull(cespite.getValoreIniziale(), "valore iniziale");

	}

	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);
		ammortamentoAnnuoCespiteDad.setEnte(ente);
		ammortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		dettaglioAmmortamentoAnnuoCespiteDad.setEnte(ente);
		dettaglioAmmortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		tipoBeneCespiteDad.setEnte(ente);
		causaleEPDad.setEnte(ente);
		bilancioDad.setEnteEntity(ente);
	}

	@Transactional
	@Override
	public InserisciCespiteResponse executeService(InserisciCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		isDonazione = Boolean.TRUE.equals(cespite.getFlgDonazioneRinvenimento());
		checkCodiceGiaEsistente();
		caricaTipoBene();
		checkTipoBene();
		popolaDatiDefaultCespite();
		Cespite cespiteInserito = cespiteDad.inserisciCespite(cespite);
		gestisciFondoAmmortamento(cespiteInserito);
		gestisciPrimaNota(cespiteInserito);
		res.setCespite(cespiteInserito);
	}

	// SIAC-6958
	/**
	 * 
	 * @param cespiteInserito
	 */
	private void gestisciFondoAmmortamento(Cespite cespite) {
		if (cespite.getFondoAmmortamento() == null) {
			return;
		}
		
		Integer cesAmmId = ammortamentoAnnuoCespiteDad.inserisciTestataFondoAmmortamento(cespite);
		dettaglioAmmortamentoAnnuoCespiteDad.inserisciFondoAmmortamento(cespite, cesAmmId);
	}

	/**
	 * @param cespiteInserito
	 */
	private void gestisciPrimaNota(Cespite cespiteInserito) {
		if (!this.isDonazione) {
			return;
		}
		PrimaNota primaNotaInserita = inserisciPrimaNotaDonazione();
		cespiteDad.associaPrimaNota(cespiteInserito, primaNotaInserita);
		res.setPrimaNota(primaNotaInserita);
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

	private void caricaTipoBene() {
		TipoBeneCespite criteriDiricerca = cespite.getTipoBeneCespite();
		criteriDiricerca.setDataInizioValiditaFiltro(Utility.ultimoGiornoDellAnno(Utility.BTL.get().getAnno()));
		this.tipoBeneCespite = tipoBeneCespiteDad.findDettaglioTipoBeneCespiteById(criteriDiricerca,
				this.isDonazione ? modelDetailDonazione : modelDetailCespite);
	}

	/**
	 * Popola dati default cespite.
	 */
	private void popolaDatiDefaultCespite() {
		popolaNumeroInventario();
		// SIAC-6590
		if(!req.isPreserveValoreAttuale()) {
			cespite.setValoreAttuale(cespite.getValoreIniziale());
		}
		if (cespite.getFlagSoggettoTutelaBeniCulturali() == null) {
			cespite.setFlagSoggettoTutelaBeniCulturali(Boolean.FALSE);
		}
	}

	/**
	 * Stacca numero inventario.
	 */
	private void popolaNumeroInventario() {
		CespiteInventarioWrapper ciw = cespiteDad.staccaNumeroInventario();
		if (ciw == null) {
			throw new BusinessException(
					ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile ottenere un numero di inventario."));
		}
		cespite.setNumeroInventario(ciw.getNumeroInventario());
		cespite.setNumeroInventarioPrefisso(ciw.getPrefisso());
		cespite.setNumeroInventarioNumero(ciw.getNumero());
	}

	/**
	 * Check codice gia esistente.
	 */
	private void checkCodiceGiaEsistente() {
		if (cespiteDad.findByCodice(cespite.getCodice()) != null) {
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Cespite", cespite.getCodice()));
		}
	}

	/**
	 * Controlla che il tipo bene esista e non sia annullato
	 */
	private void checkTipoBene() {
		if (this.tipoBeneCespite == null || Boolean.TRUE.equals(this.tipoBeneCespite.getAnnullato())) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(
					"impossibile tipo bene non annullato con uid: " + cespite.getTipoBeneCespite().getUid() + " ."));
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
	
	protected Bilancio caricaBilancio(){
		Bilancio bilancio = bilancioDad.getBilancioByAnno(req.getAnnoBilancio());
		return bilancio;
	}
}
