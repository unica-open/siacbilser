/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siacbilser.integration.dad.TipoBeneCespiteDad;
import it.csi.siac.siacbilser.integration.dad.VariazioneCespiteDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
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
 * @author elisa
 * @version 1.0.0 - 20-08-2018
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class BaseInserisciAggiornaVariazioneCespiteService<REQ extends ServiceRequest, RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {

	
	@Autowired
	protected CespiteDad cespiteDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private TipoBeneCespiteDad tipoBeneCespiteDad;
	// DAD
	@Autowired
	protected VariazioneCespiteDad variazioneCespiteDad;

	@Autowired
	private InseriscePrimaNotaService inseriscePrimaNotaService;
	
	// Fields
	protected Cespite cespite;
	private TipoBeneCespite tipoBeneCespite;
	protected VariazioneCespite variazioneCespite;
	
	private static final TipoBeneCespiteModelDetail[] modelDetailIncremento = new TipoBeneCespiteModelDetail[] {
			TipoBeneCespiteModelDetail.ContoPatrimoniale,
			TipoBeneCespiteModelDetail.ContoIncremento,
			TipoBeneCespiteModelDetail.CausaleIncremento
	};

	private static final TipoBeneCespiteModelDetail[] modelDetailDecremento = new TipoBeneCespiteModelDetail[] {
			TipoBeneCespiteModelDetail.ContoPatrimoniale,
			TipoBeneCespiteModelDetail.ContoDecremento,
			TipoBeneCespiteModelDetail.CausaleDecremento};
	
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);
		variazioneCespiteDad.setEnte(ente);
		variazioneCespiteDad.setLoginOperazione(loginOperazione);
		tipoBeneCespiteDad.setEnte(ente);
		bilancioDad.setEnteEntity(ente);
	}
	
	/**
	 * @throws ServiceParamError
	 */
	protected void checkCampiVariazione() throws ServiceParamError {
		checkEntita(variazioneCespite.getCespite(), "cespite variazione cespite", false);
		checkNotBlank(variazioneCespite.getAnnoVariazione(), "anno variazione cespite", false);
		checkNotBlank(variazioneCespite.getDescrizione(), "descrizione variazione cespite", false);
		checkNotNull(variazioneCespite.getFlagTipoVariazioneIncremento(), "tipo variazione incremento variazione cespite", false);
		checkNotNull(variazioneCespite.getDataVariazione(), "data variazione variazione cespite", false);
		checkImporto(variazioneCespite.getImporto(), "importo variazione cespite", false);
	}
	
	/**
	 * Ottiene il cespite
	 */
	protected void retrieveCespite() {
		cespite = cespiteDad.findCespiteById(variazioneCespite.getCespite(), new CespiteModelDetail[] {CespiteModelDetail.TipoBeneCespiteModelDetail});
		if(cespite == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Cespite", "uid " + variazioneCespite.getCespite().getUid()));
		}
	}
	
	/**
	 * @param cespiteInserito
	 */
	protected void inserisciEAssociaPrimaNota(VariazioneCespite variazioneInserita) {
		PrimaNota primaNotaInserita = inserisciPrimaNotaVariazioneDonazione();
		variazioneCespiteDad.associaPrimaNota(variazioneInserita, primaNotaInserita);
	}

	private PrimaNota inserisciPrimaNotaVariazioneDonazione() {
		
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
		pn.setEnte(ente);
		pn.setTipoCausale(TipoCausale.Libera);
		pn.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		pn.setDataRegistrazione(variazioneCespite.getDataVariazione());
		pn.setDescrizione("");
		pn.setStatoAccettazionePrimaNotaProvvisoria(StatoAccettazionePrimaNotaProvvisoria.PROVVISORIO);

		boolean isIncremento = Boolean.TRUE.equals(variazioneCespite.getFlagTipoVariazioneIncremento());
		
		MovimentoEP movimentoEP = new MovimentoEP();

		movimentoEP.setCausaleEP(isIncremento? tipoBeneCespite.getCausaleIncremento() : tipoBeneCespite.getCausaleDecremento());
		movimentoEP.setAmbito(Ambito.AMBITO_INV);
		movimentoEP.setEnte(ente);
		
		Conto contoDare = isIncremento? tipoBeneCespite.getContoPatrimoniale(): tipoBeneCespite.getContoDecremento();
		Conto contoAvere = isIncremento? tipoBeneCespite.getContoIncremento() : tipoBeneCespite.getContoPatrimoniale();
		
		MovimentoDettaglio movimentoDettaglioDare = creaMovimentoDettaglio(
				contoDare,
				OperazioneSegnoConto.DARE, 
				Integer.valueOf(1));
		MovimentoDettaglio movimentoDettaglioAvere = creaMovimentoDettaglio(
				contoAvere,
				OperazioneSegnoConto.AVERE, 
				Integer.valueOf(1));
		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioDare);
		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioAvere);
		pn.getListaMovimentiEP().add(movimentoEP);
		return pn;
	}

	private MovimentoDettaglio creaMovimentoDettaglio(Conto contoMovimento,
			OperazioneSegnoConto operazioneSegnoConto, Integer numeroRiga) {
		MovimentoDettaglio mov = new MovimentoDettaglio();
		mov.setAmbito(Ambito.AMBITO_INV);
		mov.setConto(contoMovimento);
		mov.setEnte(ente);
		mov.setImporto(variazioneCespite.getImporto());
		mov.setSegno(operazioneSegnoConto);
		mov.setNumeroRiga(numeroRiga);
		return mov;
	}
	
	/**
	 * Carica tipo bene.
	 */
	protected void caricaTipoBene() {
		TipoBeneCespite criteriDiricerca = cespite.getTipoBeneCespite();
		criteriDiricerca.setDataInizioValiditaFiltro(Utility.ultimoGiornoDellAnno(Utility.BTL.get().getAnno()));
		this.tipoBeneCespite = tipoBeneCespiteDad.findDettaglioTipoBeneCespiteById(criteriDiricerca,
				Boolean.TRUE.equals(variazioneCespite.getFlagTipoVariazioneIncremento())? 
						modelDetailIncremento : modelDetailDecremento 
				);
	}
	
	private Bilancio caricaBilancio(){
		Bilancio bilancio = bilancioDad.getBilancioByAnno(req.getAnnoBilancio());
		return bilancio;
	}
	

	/**
	 * Controlla che il tipo bene esista e non sia annullato
	 */
	protected void checkTipoBene() {
		
		boolean valorizzatoContoPatrimoniale =  tipoBeneCespite.getContoPatrimoniale() != null
				&& tipoBeneCespite.getContoPatrimoniale().getUid() != 0;
		boolean isIncremento = Boolean.TRUE.equals(variazioneCespite.getFlagTipoVariazioneIncremento());
		
		boolean campiCorrettiPerVariazione = (!isIncremento || isTuttiUidValorizzati(tipoBeneCespite.getContoIncremento(), tipoBeneCespite.getCausaleIncremento()) 
				&& (isIncremento || isTuttiUidValorizzati(tipoBeneCespite.getContoDecremento(), tipoBeneCespite.getCausaleDecremento())));

		if (!valorizzatoContoPatrimoniale || ! campiCorrettiPerVariazione) {
			throw new BusinessException(
					ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Conti Tipo bene non indicati correttamente"));
		}
	}
	
	
	private boolean isTuttiUidValorizzati(Conto contoVariazione, CausaleEP causaleVariazione) {
		return contoVariazione != null && contoVariazione.getUid() != 0 && causaleVariazione != null && causaleVariazione.getUid() != 0;
	}
	
	public abstract void gestisciPrimeNote(VariazioneCespite var);

}


