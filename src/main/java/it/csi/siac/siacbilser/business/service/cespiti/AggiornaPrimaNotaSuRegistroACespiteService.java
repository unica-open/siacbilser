/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.cespiti.utility.TipoCalcoloQuotaPrimoAnnoEnum;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.AmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siacbilser.integration.dad.DettaglioAmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siacbilser.model.exception.DadException;
import it.csi.siac.siaccecser.model.EventoRegistroACespiteSelector;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAmmortamentoMassivoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAmmortamentoMassivoCespiteResponse;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccespser.model.CategoriaCespitiModelDetail;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccespser.model.ImportiRegistroA;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoModelDetail;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoDettaglioModelDetail;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.MovimentoEPModelDetail;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.RegistrazioneMovFinModelDetail;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaProvvisoria;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
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
public class AggiornaPrimaNotaSuRegistroACespiteService extends CheckedAccountBaseService<AggiornaPrimaNotaSuRegistroACespite, AggiornaPrimaNotaSuRegistroACespiteResponse> {

	private static final ModelDetail[] defaultModelDetailsPerAmmortamento = new ModelDetail[]{
			AmmortamentoAnnuoCespiteModelDetail.DettaglioAmmortamentoAnnuoCespiteModelDetail,
			DettaglioAmmortamentoAnnuoCespiteModelDetail.PrimaNota
	};
	private static final ModelDetail[] defaultModelDetailsPerVendita = new ModelDetail[]{
			TipoBeneCespiteModelDetail.ContoAmmortamento, TipoBeneCespiteModelDetail.ContoFondoAmmortamento, 
			TipoBeneCespiteModelDetail.ContoPlusvalenza, TipoBeneCespiteModelDetail.ContoMinusValenza, TipoBeneCespiteModelDetail.CausaleAmmortamento,
	};
	
	private static final String codiceElaborazione = "COMPLETAMENTO_INTEGRAZIONE_REGISTRO_A";	
	private static final String CODICE_CAUSALE_PLUSVALENZA = "VEP";
	private static final String CODICE_CAUSALE_MINUSVALENZA = "VEM";
	
	private PrimaNota primaNotaDaIntegrare;
	private Evento evento;
	private EventoRegistroACespiteSelector eventoSelector;
	private TipoCausale tipoCausale;
	private Bilancio bilancio;
	private CausaleEP causaleEPMinusValenza;
	private CausaleEP causaleEPPlusValenza;
	
	@Autowired 
	private DettaglioAmmortamentoAnnuoCespiteDad dettaglioAmmortamentoAnnuoCespiteDad;
	@Autowired 
	private AmmortamentoAnnuoCespiteDad ammortamentoAnnuoCespiteDad;
	@Autowired
	private CespiteDad cespiteDad;
	@Autowired
	@Inventario
	private PrimaNotaInvDad primaNotaInvDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private CausaleEPDad causaleEPDad;
	@Autowired
	private RegistrazioneMovFinDad registrazioneMovFinDad;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	
	@Autowired
	private InserisciAmmortamentoMassivoCespiteService inserisciAmmortamentoMassivoCespiteService;
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
		
		dettaglioAmmortamentoAnnuoCespiteDad.setEnte(ente);
		dettaglioAmmortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		
		ammortamentoAnnuoCespiteDad.setEnte(ente);
		ammortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);
		
		causaleEPDad.setEnte(ente);
		causaleEPDad.setLoginOperazione(loginOperazione);
		
		registrazioneMovFinDad.setEnte(ente);
		
		bilancioDad.setEnteEntity(ente);
		
		Utility.MDTL.addModelDetails(MovimentoEPModelDetail.CausaleEPModelDetail, MovimentoEPModelDetail.MovimentoDettaglioModelDetail,
				MovimentoDettaglioModelDetail.ContoModelDetail, MovimentoDettaglioModelDetail.Segno, MovimentoDettaglioModelDetail.Cespiti,
				ContoModelDetail.TipoConto,
				CespiteModelDetail.TipoBeneCespiteModelDetail,
				DettaglioAmmortamentoAnnuoCespiteModelDetail.PrimaNotaModelDetail,
				CategoriaCespitiModelDetail.TipoCalcolo,
				TipoBeneCespiteModelDetail.CategoriaCespitiModelDetail,
				TipoBeneCespiteModelDetail.ContoPatrimoniale);
		
	}

	@Transactional
	@Override
	public AggiornaPrimaNotaSuRegistroACespiteResponse executeService(AggiornaPrimaNotaSuRegistroACespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		//carico il bilancio
		caricaBilancio();		

		//l'importo su prima nota dei cespiti collegati non può superare l'importo dei movimenti dettaglio collegati
		checkImportoRegistroA(req.getPrimaNota());
		
		//carico i dati dell'evento
		caricaEventoEdEventoSelector();
		
		boolean isVendita = EventoRegistroACespiteSelector.FatturaAttiva.equals(this.eventoSelector) || EventoRegistroACespiteSelector.Vendita.equals(eventoSelector);
		Utility.MDTL.addModelDetails(isVendita? defaultModelDetailsPerVendita : defaultModelDetailsPerAmmortamento);

		caricaPrimaNota();
		checkPrimaNota(); 		
		
		
		
		for (MovimentoEP mep : this.primaNotaDaIntegrare.getListaMovimentiEP()) {			
			
			List<MovimentoDettaglio> movimentiDettaglioCollegati = mep.getListaMovimentoDettaglio();
			
			if( movimentiDettaglioCollegati == null || movimentiDettaglioCollegati.isEmpty()) {
				throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("movimento ep della prima nota", "lista movimento dettaglio non presente."));
			}
			
			for (MovimentoDettaglio mov : movimentiDettaglioCollegati) {
				effettuaOperazioniInventarioSuMovimentoDettaglio(isVendita, mov);
			}
			
		}
		
		aggiornaStatoOperativoPrimaNota();

	}

	/**
	 * @param isVendita
	 * @param mov
	 */
	private void effettuaOperazioniInventarioSuMovimentoDettaglio(boolean isVendita, MovimentoDettaglio mov) {
		List<Cespite> listaCespitiCollegatiAlMovimentoEP = mov.getCespiti();
		BigDecimal importoDaMovimentare =  mov.getImporto();
		if(mov.getConto() == null || mov.getConto().getTipoConto() == null || !mov.getConto().getTipoConto().isTipoCespiti() || mov.getCespiti() == null || mov.getCespiti().isEmpty() || importoDaMovimentare == null) {
			return;
		}
		
		checkContoPatrimonialeCespiti(listaCespitiCollegatiAlMovimentoEP, mov.getConto(), isVendita);
		aggiornaImportiCespitiCollegati(mov,listaCespitiCollegatiAlMovimentoEP, importoDaMovimentare, isVendita);			
		
		calcolaNuovoPianoAmmortamento(listaCespitiCollegatiAlMovimentoEP, isVendita);
		
		effettuaOperazioniPerVendita(mov, isVendita, listaCespitiCollegatiAlMovimentoEP, importoDaMovimentare);
	}
	
	/**
	 * Carica evento ed evento selector.
	 */
	private void caricaEventoEdEventoSelector() {
		this.tipoCausale = primaNotaInvDad.findTipoCausalePrimaNota(req.getPrimaNota().getUid());
		if(this.tipoCausale == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile caricare il tipo causale della prima nota."));
		}		
		this.evento = TipoCausale.Integrata.equals(tipoCausale) ? 
				primaNotaInvDad.caricaEventoPrimaNotaIntegrata(req.getPrimaNota())
				: primaNotaInvDad.caricaEventoPrimaNotaLibera(req.getPrimaNota(), Utility.ultimoGiornoDellAnno(req.getAnnoBilancio().intValue()));
		if(this.evento == null || this.evento.getTipoEvento() == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile reperire l'evento della prima nota."));
		}
		
		this.eventoSelector = EventoRegistroACespiteSelector.byCodiceEventoAndTipoCausale(this.evento.getCodice(), tipoCausale);
		if(this.eventoSelector == null) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("evento non gestito"));
		}
	}

	/**
	 * Effettua operazioni per vendita.
	 *
	 * @param mep the mep
	 * @param isVendita the is vendita
	 * @param listaCespitiCollegatiAlMovimentoEP the lista cespiti collegati al movimento EP
	 * @param importoDaMovimentare the importo da movimentare
	 */
	private void effettuaOperazioniPerVendita(MovimentoDettaglio movimentoDettaglio, boolean isVendita, List<Cespite> listaCespitiCollegatiAlMovimentoEP, BigDecimal importoDaMovimentare) {
		if(!isVendita) {
			return;
		}
		for (Cespite cespite : listaCespitiCollegatiAlMovimentoEP) {
			
			 AmmortamentoAnnuoCespite ammortamentoAnnuoCespite = ammortamentoAnnuoCespiteDad.caricaAmmortamentoAnnuoByCespite(cespite, AmmortamentoAnnuoCespiteModelDetail.DettaglioAmmortamentoAnnuoCespite);
			 if(ammortamentoAnnuoCespite == null) {
				 throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impossibile integrare una prima nota collegata ad un cespite senza piano di ammortamento."));
			 }
			 BigDecimal importoFondoAccantonatoPrecedentemente = ammortamentoAnnuoCespite.getImportoTotaleAmmortato();
			 BigDecimal importoFondoAccantonatoInserito = effettuaScrittureAmmortamentoResiduo(cespite, ammortamentoAnnuoCespite);
			 BigDecimal importoFondoAccantonatoTotale = importoFondoAccantonatoPrecedentemente.add(importoFondoAccantonatoInserito);
			 PrimaNota primaNotaAlienazione = effettuaScrittureAlienazione(cespite,importoDaMovimentare, importoFondoAccantonatoTotale);
			 collegaPrimaNotaAlienazione(primaNotaAlienazione, cespite, movimentoDettaglio);
		}
	}
	
	
	/**
	 * Collega prima nota alienazione.
	 *
	 * @param primaNotaAlienazione the prima nota alienazione
	 * @param cespite the cespite
	 * @param movimentoEPDellaPrimaNotaPadre the movimento EP della prima nota padre
	 */
	private void collegaPrimaNotaAlienazione(PrimaNota primaNotaAlienazione, Cespite cespite, MovimentoDettaglio movimentoDettaglio) {
		cespiteDad.associaPrimaNotaAlienzazioneACespite(movimentoDettaglio, cespite, primaNotaAlienazione);
	}
	

	/**
	 * Carica bilancio.
	 */
	private void caricaBilancio(){
		this.bilancio = bilancioDad.getBilancioByAnno(req.getAnnoBilancio());
		if(this.bilancio == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("inserimento prime note", "bilancio"));
		}
	}
	

	/**
	 * Effettua scritture minus plus valenza.
	 *
	 * @param mep the mep
	 * @param ces the ces
	 * @param valoreDiVendita the valore di vendita
	 * @param importoTotaleAmmortato the importo totale ammortato
	 */
	private PrimaNota effettuaScrittureAlienazione(Cespite ces, BigDecimal valoreDiVendita, BigDecimal importoTotaleAmmortato) {
		final String methodName ="effettuaScrittureAlienazione";
		BigDecimal valoreAttuale = ces.getValoreAttuale();
		BigDecimal sommaAmmortatoEVendita = importoTotaleAmmortato.add(valoreDiVendita);
		
		BigDecimal importoContoFondoAmmortamento = importoTotaleAmmortato;
		BigDecimal importoContoPatrimoniale = valoreAttuale.subtract(valoreDiVendita, MathContext.DECIMAL128);
		BigDecimal importoMinusValenza = valoreAttuale.subtract(sommaAmmortatoEVendita);
		BigDecimal importoPlusValenza = importoContoFondoAmmortamento.subtract(importoContoPatrimoniale);
		
		
		log.debug(methodName, new StringBuilder()
				.append("Cespite ").append(ces.getCodice()).append(" con valore attuale: ").append(valoreAttuale.toPlainString())
				.append(" , importo conto patrimoniale: ").append(importoContoPatrimoniale.toPlainString())
				.append(" , importo minusvalenza: ").append(importoMinusValenza.toPlainString())
				.append(" , importo plusvalenza: ").append(importoPlusValenza.toPlainString())
				.toString());
		
		TipoBeneCespite tipoBeneCespite = ces.getTipoBeneCespite();
		boolean isPlusvalenza = valoreAttuale.subtract(sommaAmmortatoEVendita).signum() < 0;
		
		CausaleEP causaleAlienazione = isPlusvalenza? 
				caricaCausalePlusValenza() : 
					caricaCausaleMinusValenza();
		
		PrimaNota pni = popolaDatiBasePrimaNota(causaleAlienazione);
		
		MovimentoEP movimentoEP = pni.getListaMovimentiEP().get(0);
		
		
		MovimentoDettaglio movimentoDettaglioFondoAmmortamento = creaMovimentoDettaglio(tipoBeneCespite.getContoAmmortamento(),
				OperazioneSegnoConto.DARE, importoContoFondoAmmortamento, Integer.valueOf(1));
		MovimentoDettaglio movimentoDettaglioAvere = creaMovimentoDettaglio(tipoBeneCespite.getContoPatrimoniale(),
				OperazioneSegnoConto.AVERE, importoContoPatrimoniale, Integer.valueOf(1));
		MovimentoDettaglio movimentoDettaglioAlienazione = creaMovimentoDettaglioAlienazione(isPlusvalenza,tipoBeneCespite, importoContoPatrimoniale, importoContoFondoAmmortamento, Integer.valueOf(1));
		

		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioFondoAmmortamento);
		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioAvere);
		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioAlienazione);
		
		PrimaNota pnAlienazione = inserisciPrimaNota(pni);
		
		res.addMessaggio(new Messaggio(codiceElaborazione, 
				new StringBuilder().append("Inserita la prima nota per alienazione ").append(pnAlienazione.getBilancio() != null? pnAlienazione.getBilancio().getAnno() : this.bilancio.getAnno()).append("/").append(pnAlienazione.getNumero()).toString()));
		return pnAlienazione;
	}

	/**
	 * Calcola nuovo piano ammortamento.
	 *
	 * @param eventoInventarioSelector the evento inventario selector
	 * @param listaCespitiCollegatiAlMovimentoEP the lista cespiti collegati al movimento EP
	 * @param isVendita the is vendita
	 */
	private void calcolaNuovoPianoAmmortamento(List<Cespite> listaCespitiCollegatiAlMovimentoEP, boolean isVendita) {		
		//if(isVendita || listaCespitiCollegatiAlMovimentoEP.isEmpty()) {
		if(isVendita ) {
			return;
		}
		List<Integer> uidsCespiti = new ArrayList<Integer>();
		for (Cespite ces : listaCespitiCollegatiAlMovimentoEP) {
			uidsCespiti.add(ces.getUid());
		}
		
		InserisciAmmortamentoMassivoCespite reqIAMC = new InserisciAmmortamentoMassivoCespite();
		reqIAMC.setRichiedente(req.getRichiedente());
		reqIAMC.setDataOra(new Date());
		reqIAMC.setInserisciAmmortamentoCompleto(Boolean.TRUE);
		reqIAMC.setAnnoBilancio(Integer.valueOf(this.bilancio.getAnno()));
		reqIAMC.setUidsCespiti(uidsCespiti);
		InserisciAmmortamentoMassivoCespiteResponse resIPN = serviceExecutor.executeServiceSuccess(inserisciAmmortamentoMassivoCespiteService, reqIAMC);
		checkServiceResponseFallimento(resIPN);
	}

	/**
	 * Aggiorna stato operativo prima nota.
	 */
	private void aggiornaStatoOperativoPrimaNota() {
		StatoOperativoPrimaNota statoOperativoPrimaNota = determinaStatoOperativoPrimaNota();
		StatoAccettazionePrimaNotaDefinitiva statoAccettazioneNew = determinaStatoAccettazionePrimaNota();
		
		if(statoOperativoPrimaNota != null && !this.primaNotaDaIntegrare.getStatoAccettazionePrimaNotaDefinitiva().equals(statoAccettazioneNew)) {
			primaNotaInvDad.aggiornaStatoOperativoPrimaNota(this.primaNotaDaIntegrare, statoOperativoPrimaNota, statoAccettazioneNew);
		}
	}
	
	/**
	 * Aggiorna importi cespiti collegati.
	 *
	 * @param movimentoEP the movimento EP
	 * @param listaCespiti the lista cespiti
	 * @param importoDaMovimentare the importo da movimentare
	 * @param eventoInventarioSelector the evento inventario selector
	 * @param isVendita the is vendita
	 */
	private void aggiornaImportiCespitiCollegati(MovimentoDettaglio movimentoDettaglio, List<Cespite> listaCespiti, BigDecimal importoDaMovimentare, boolean isVendita) {
		if(isVendita) {
			return;
		}
		
		for (Cespite ces : listaCespiti) {
			//lo devo caricare qui in quanto devo filtrare anche per movimento dettaglio (un cespite puo' essere collegato a piu' prime note, purchè sia sempre collegato ad una sola prima nota in stato da accettare per volta)
			// questo accade sempre nel caso di note di credito sui documenti spesa.
			BigDecimal importoSuPrimaNota = cespiteDad.getImportosuRegistroA(ces, movimentoDettaglio);
			BigDecimal newImporto = ottieniNuovoImporto(movimentoDettaglio, importoDaMovimentare, importoSuPrimaNota, ces);
			if(newImporto.compareTo(BigDecimal.ZERO) < 0) {
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'importo del cespite non puo' diventare negativo"));
			}
			ces.setValoreAttuale(newImporto);
			cespiteDad.aggiornaImportoCespite(ces);
			
		}
	}


	/**
	 * Ottieni nuovo importo.
	 *
	 * @param movimentoEP the movimento EP
	 * @param eventoInventarioSelector the evento inventario selector
	 * @param importoDaMovimentare the importo da movimentare
	 * @param importoSuPrimaNota the valore attuale
	 * @param cespite the cespite
	 * @return the big decimal
	 */
	private BigDecimal ottieniNuovoImporto(MovimentoDettaglio movimentoDettaglio, BigDecimal importoDaMovimentare, BigDecimal importoSuPrimaNota, Cespite cespite) {
		if(EventoRegistroACespiteSelector.ModificaValore.equals(this.eventoSelector) || EventoRegistroACespiteSelector.NotaCredito.equals(this.eventoSelector)) {
			BigDecimal importo = cespite.getValoreAttualeNotNull().subtract(importoSuPrimaNota);
			return importo != null && importo.signum()> 0? importo : BigDecimal.ZERO;
		}
		if(EventoRegistroACespiteSelector.Acquisto.equals(this.eventoSelector) || EventoRegistroACespiteSelector.FatturaPassiva.equals(this.eventoSelector) || EventoRegistroACespiteSelector.Liquidazione.equals(this.eventoSelector)) {
			return isInserimentoCespiteContestuale(cespite, movimentoDettaglio) ? importoSuPrimaNota : cespite.getValoreAttualeNotNull().add(importoSuPrimaNota);
		}
		return importoSuPrimaNota;
	}
	
	/**
	 * Checks if is inserimento cespite contestuale.
	 *
	 * @param cespite the cespite
	 * @param movimentoEP the movimento EP
	 * @return true, if is inserimento cespite contestuale
	 */
	private boolean isInserimentoCespiteContestuale(Cespite cespite, MovimentoDettaglio movimentoDettaglio) {
		return Boolean.TRUE.equals(cespiteDad.isCespiteInseritoDaRegistroA(cespite, movimentoDettaglio));
	}

	/**
	 * Determina stato operativo prima nota.
	 *
	 * @return the stato operativo prima nota
	 */
	private StatoOperativoPrimaNota determinaStatoOperativoPrimaNota() {
		return StatoOperativoPrimaNota.DEFINITIVO;
	}

	/**
	 * Determina stato accettazione prima nota.
	 *
	 * @return the stato accettazione prima nota definitiva
	 */
	private StatoAccettazionePrimaNotaDefinitiva determinaStatoAccettazionePrimaNota() {
		return StatoAccettazionePrimaNotaDefinitiva.INTEGRATO;
	}

	/**
	 * Carica prima nota.
	 */
	private void caricaPrimaNota() {
		final String methodName ="caricaPrimaNotaInventario";
		
		log.debug(methodName, "carico da db la prima nota con uid: " + req.getPrimaNota().getUid());
		this.primaNotaDaIntegrare = primaNotaInvDad.findPrimaNotaByUid(req.getPrimaNota().getUid(), new PrimaNotaModelDetail[] {PrimaNotaModelDetail.PrimaNotaInventario, PrimaNotaModelDetail.MovimentiEpModelDetail, PrimaNotaModelDetail.StatoOperativo,PrimaNotaModelDetail.Bilancio, PrimaNotaModelDetail.StatoAccettazionePrimaNotaDefinitiva});
	}

	
	/**
	 * Check conto patrimoniale.
	 * @param listaCespitiCollegatiAlMovimentoEP 
	 * @param contoMovimento 
	 *
	 * @param cespitiCollegati the cespiti collegati
	 */
	private void checkContoPatrimonialeCespiti(List<Cespite> listaCespitiCollegatiAlMovimentoEP, Conto contoMovimento, boolean isVendita) {
		List<String> codiciCespitiNonValidi = new ArrayList<String>();
		
		for (Cespite cespite : listaCespitiCollegatiAlMovimentoEP) {
			TipoBeneCespite tipoBeneCespite = cespite.getTipoBeneCespite();
			if(tipoBeneCespite == null) {
				codiciCespitiNonValidi.add(cespite.getCodice());
			}
			if(cespite.getTipoBeneCespite().getContoPatrimoniale() == null || cespite.getTipoBeneCespite().getContoPatrimoniale().getUid() != contoMovimento.getUid()) {
				codiciCespitiNonValidi.add(cespite.getCodice());
			}
			if(isVendita && !isTipoBeneCorrettoPerAmmortamento(tipoBeneCespite)) {
				throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("tipo bene del cespite", "campi per l'ammortamento non correttamente indicati"));
			}
			
		}
		if(codiciCespitiNonValidi.isEmpty()) {
			return;
		}
		StringBuilder msgErrore = new StringBuilder()
				.append("I seguenti cepsiti presentano un codice patrimoniale incongruente con la prima nota : ")
				.append(StringUtils.join(codiciCespitiNonValidi, ", "));
		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msgErrore));
	}
	
	/**
	 * Checks if is tipo bene corretto per ammortamento.
	 *
	 * @param tipoBene the tipo bene
	 * @return true, if is tipo bene corretto per ammortamento
	 */
	private boolean isTipoBeneCorrettoPerAmmortamento(TipoBeneCespite tipoBene) {
		
		return  isNotNullNorInvalidUid(tipoBene.getCausaleAmmortamento()) && 
		isNotNullNorInvalidUid(tipoBene.getContoAmmortamento()) && 
		isNotNullNorInvalidUid(tipoBene.getContoFondoAmmortamento()) && 
		//contollo la categoria
		isNotNullNorInvalidUid(tipoBene.getCategoriaCespiti()) &&
		isNotNullNorInvalidUid(tipoBene.getCategoriaCespiti().getCategoriaCalcoloTipoCespite())
		;
		
	}
	
	/**
	 * Checks if is not null nor invalid uid.
	 *
	 * @param e the e
	 * @return true, if is not null nor invalid uid
	 */
	private boolean isNotNullNorInvalidUid(Entita e ) {
		return e != null && e.getUid() != 0;
	}

	/**
	 * Check prima nota ambito INV.
	 */
	private void checkPrimaNota() {
		final String methodName ="checkPrimaNotaAmbitoINV";
		
		if(this.primaNotaDaIntegrare == null || this.primaNotaDaIntegrare.getUid() == 0) {
			log.debug(methodName, "Nessuna prima nota trovata su base dati.");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("validazione prima nota con uid: " + req.getPrimaNota().getUid(), "prima nota"));
		}
		
		if(this.primaNotaDaIntegrare.getStatoOperativoPrimaNota() == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("prima nota", "impossibile reperire lo stato."));
		}
		
		if(!StatoAccettazionePrimaNotaDefinitiva.DA_ACCETTARE.equals(this.primaNotaDaIntegrare.getStatoAccettazionePrimaNotaDefinitiva())) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("prima nota", "dati inventario non presenti."));
		}
		
		if(!StatoOperativoPrimaNota.DEFINITIVO.equals(this.primaNotaDaIntegrare.getStatoOperativoPrimaNota())) {
			log.debug(methodName, "La prima nota ha stato operativo annullato.");
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("prima nota", this.primaNotaDaIntegrare.getStatoAccettazionePrimaNotaDefinitiva().getDescrizione() + " - " + this.primaNotaDaIntegrare.getStatoOperativoPrimaNota().getDescrizione()));
		}
		
		if(this.primaNotaDaIntegrare.getListaMovimentiEP() == null || this.primaNotaDaIntegrare.getListaMovimentiEP().isEmpty()) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("prima nota ambito inventario", "movimenti ep non presenti."));
		}
		
	}
	
	/**
	 * Effettua le operazioni necessarie alle scritture del fondo ammortamento e le scritture stesse.
	 *
	 * @param ammortamentoAnnuoCespite l'ammortamento del cespite
	 * @param cespite il cespite da ammortare
	 * @param ammortamentoAnnuoCespite 
	 * @return l'importo della scritture effettuate
	 */
	private BigDecimal effettuaScrittureAmmortamentoResiduo(Cespite cespite, AmmortamentoAnnuoCespite ammortamentoAnnuoCespite) {
		DettaglioAmmortamentoAnnuoCespite dettaglioAnnoVendita = filtraDettaglioAnnoVendita(ammortamentoAnnuoCespite.getDettagliAmmortamentoAnnuoCespite());
		cancellaDettagliAnniSuccessiviAVendita(ammortamentoAnnuoCespite, dettaglioAnnoVendita);
		BigDecimal importoFondoAmmortamento = obtainImportoFondoAmmortamento(dettaglioAnnoVendita, cespite);
		PrimaNota pn = inserisciPrimaNotaAmmortamento(importoFondoAmmortamento, cespite.getTipoBeneCespite());
		inserisciNuovoDettaglioAmmortamento(ammortamentoAnnuoCespite, importoFondoAmmortamento,pn);
		res.addMessaggio(new Messaggio(codiceElaborazione, 
				new StringBuilder().append("Inserita la prima nota per ammortamento residuo: ").append(pn.getBilancio() != null? pn.getBilancio().getAnno() : this.bilancio.getAnno()).append("/").append(pn.getNumero()).toString()));
		return importoFondoAmmortamento;
		
	}
	

	/**
	 * Inserisci prima nota ammortamento.
	 *
	 * @param importoFondoAmmortamento the importo fondo ammortamento
	 * @param tipoBeneCespite the tipo bene cespite
	 * @return the prima nota
	 */
	private PrimaNota inserisciPrimaNotaAmmortamento(BigDecimal importoFondoAmmortamento, TipoBeneCespite tipoBeneCespite) {
		PrimaNota primaNota = creaPrimaNotaAmmortamento(importoFondoAmmortamento, tipoBeneCespite );
		return inserisciPrimaNota(primaNota);
	}

	/**
	 * Inserisci prima nota.
	 *
	 * @param primaNota the prima nota
	 * @return the prima nota
	 */
	private PrimaNota inserisciPrimaNota(PrimaNota primaNota) {
		final String methodName = "inserisciPrimaNota";
		InseriscePrimaNota reqIPN = new InseriscePrimaNota();
		reqIPN.setRichiedente(req.getRichiedente());
		reqIPN.setDataOra(new Date());
		reqIPN.setAnnoBilancio(Integer.valueOf(this.bilancio.getAnno()));
		
		reqIPN.setPrimaNota(primaNota);
		log.info(methodName, "inserisco la prima nota per l'ammortamento.");
		InseriscePrimaNotaResponse resIPN = serviceExecutor.executeServiceSuccess(inseriscePrimaNotaService, reqIPN);
		checkServiceResponseFallimento(resIPN);
		return resIPN.getPrimaNota();
	}

	/**
	 * Inserisci nuovo dettaglio ammortamento.
	 *
	 * @param ammortamentoAnnuoCespite the ammortamento annuo cespite
	 * @param importoFondoAmmortamento the importo fondo ammortamento
	 * @param primaNota the prima nota
	 * @return the dettaglio ammortamento annuo cespite
	 */
	private DettaglioAmmortamentoAnnuoCespite inserisciNuovoDettaglioAmmortamento(AmmortamentoAnnuoCespite ammortamentoAnnuoCespite, BigDecimal importoFondoAmmortamento, PrimaNota primaNota) {
		final String methodName ="inserisciNuovoDettaglioAmmortamento";
		DettaglioAmmortamentoAnnuoCespite nuovoDettaglio = new DettaglioAmmortamentoAnnuoCespite();
		nuovoDettaglio.setQuotaAnnuale(importoFondoAmmortamento);
		nuovoDettaglio.setAnno(Integer.valueOf(this.bilancio.getAnno()));
		nuovoDettaglio.setDataAmmortamento(new Date());
		nuovoDettaglio.setPrimaNota(primaNota);
		log.debug(methodName, " inserisco un nuiovo dettaglio per l'ammortamento con importo " + nuovoDettaglio.getQuotaAnnuale().toPlainString() + " e anno: " +nuovoDettaglio.getAnno().toString() );
		dettaglioAmmortamentoAnnuoCespiteDad.inserisciDettaglioAmmortamentoAnnuoCespite(nuovoDettaglio, ammortamentoAnnuoCespite.getUid());
		if(nuovoDettaglio.getUid() == 0) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile inserire un dettaglio di ammortamento"));
		}
		return nuovoDettaglio;
	}
	
	/**
	 * Crea prima nota ammortamento.
	 *
	 * @param importoFondoAmmortamento the importo fondo ammortamento
	 * @return the prima nota
	 */
	private PrimaNota creaPrimaNotaAmmortamento(BigDecimal importoFondoAmmortamento, TipoBeneCespite tipoBeneCespite) {
		
		PrimaNota pni = popolaDatiBasePrimaNota(tipoBeneCespite.getCausaleAmmortamento());
		
		MovimentoEP movimentoEP = pni.getListaMovimentiEP().get(0);
		
		MovimentoDettaglio movimentoDettaglioDare = creaMovimentoDettaglio(tipoBeneCespite.getContoAmmortamento(),
				OperazioneSegnoConto.DARE, importoFondoAmmortamento, Integer.valueOf(1));
		MovimentoDettaglio movimentoDettaglioAvere = creaMovimentoDettaglio(tipoBeneCespite.getContoFondoAmmortamento(),
				OperazioneSegnoConto.AVERE, importoFondoAmmortamento, Integer.valueOf(1));

		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioDare);
		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioAvere);
		
		return pni;
	}
	
	
	/**
	 * Popola dati base prima nota.
	 *
	 * @param causaleEP the causale EP
	 * @return the prima nota
	 */
	private PrimaNota popolaDatiBasePrimaNota(CausaleEP causaleEP) {
		PrimaNota pn = new PrimaNota();

		pn.setAmbito(Ambito.AMBITO_INV);
		pn.setBilancio(this.bilancio);
		pn.setEnte(ente);
		pn.setTipoCausale(TipoCausale.Libera);
		pn.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		pn.setDataRegistrazione(new Date());
		pn.setDescrizione("");
		pn.setStatoAccettazionePrimaNotaProvvisoria(StatoAccettazionePrimaNotaProvvisoria.PROVVISORIO);

		MovimentoEP movimentoEP = new MovimentoEP();

		movimentoEP.setCausaleEP(causaleEP);
		movimentoEP.setAmbito(Ambito.AMBITO_INV);
		movimentoEP.setEnte(ente);

		pn.getListaMovimentiEP().add(movimentoEP);
		return pn;
	}
	
	
	/**
	 * Crea movimento dettaglio.
	 *
	 * @param contoPatrimoniale the conto patrimoniale
	 * @param operazioneSegnoConto the operazione segno conto
	 * @param importo the importo
	 * @param numeroRiga the numero riga
	 * @return the movimento dettaglio
	 */
	private MovimentoDettaglio creaMovimentoDettaglio(Conto contoPatrimoniale, OperazioneSegnoConto operazioneSegnoConto, BigDecimal importo, Integer numeroRiga) {
		if(importo == null || BigDecimal.ZERO.compareTo(importo) >0){
			return null;
		}
		MovimentoDettaglio mov = new MovimentoDettaglio();
		mov.setAmbito(Ambito.AMBITO_INV);
		mov.setConto(contoPatrimoniale);
		mov.setEnte(ente);
		mov.setImporto(importo);
		mov.setSegno(operazioneSegnoConto);
		mov.setNumeroRiga(numeroRiga);
		return mov;
	}
	
	/**
	 * Crea movimento dettaglio.
	 * @param tipoBeneCespite 
	 *
	 * @param contoPatrimoniale the conto patrimoniale
	 * @param operazioneSegnoConto the operazione segno conto
	 * @param importo the importo
	 * @param numeroRiga the numero riga
	 * @return the movimento dettaglio
	 */
	private MovimentoDettaglio creaMovimentoDettaglioAlienazione(boolean isPlusvalenza, TipoBeneCespite tipoBeneCespite, BigDecimal importoContoPatrimoniale, BigDecimal importoFondoAmmortamento, Integer numeroRiga) {
		OperazioneSegnoConto operazioneSegnoConto;
		Conto contoDettaglio;
		BigDecimal importo;
		if(isPlusvalenza) {
			operazioneSegnoConto = OperazioneSegnoConto.AVERE;
			contoDettaglio = tipoBeneCespite.getContoPlusvalenza();
			importo = importoFondoAmmortamento.subtract(importoContoPatrimoniale, MathContext.DECIMAL128);
			return creaMovimentoDettaglio(contoDettaglio, operazioneSegnoConto, importo, numeroRiga);
		}
		operazioneSegnoConto = OperazioneSegnoConto.DARE;
		contoDettaglio = tipoBeneCespite.getContoMinusValenza();
		importo =importoContoPatrimoniale.subtract(importoFondoAmmortamento, MathContext.DECIMAL128);
		return creaMovimentoDettaglio(contoDettaglio, operazioneSegnoConto, importo, numeroRiga);
	}
	
	private BigDecimal obtainImportoFondoAmmortamento(DettaglioAmmortamentoAnnuoCespite dettaglioAnnoDismissione, Cespite cespite) {
		String codiceTipoCalcolo = cespite.getTipoBeneCespite().getCategoriaCespiti().getCategoriaCalcoloTipoCespite().getCodice();
		BigDecimal multiplier = TipoCalcoloQuotaPrimoAnnoEnum.byCodiceTipoCalcolo(codiceTipoCalcolo).getFattoreProporzionamentoDaInizioAnnoAData(this.primaNotaDaIntegrare.getDataRegistrazione());
		BigDecimal importoFondoAmmortamento = dettaglioAnnoDismissione.getQuotaAnnuale().multiply(multiplier).setScale(2, RoundingMode.HALF_DOWN);
		return importoFondoAmmortamento;
	}
	
	/**
	 * Filtra dettaglio anno vendita.
	 *
	 * @param dettagliAmmortamentoPresenti the dettagli ammortamento presenti
	 * @return the dettaglio ammortamento annuo cespite
	 */
	private DettaglioAmmortamentoAnnuoCespite filtraDettaglioAnnoVendita(List<DettaglioAmmortamentoAnnuoCespite> dettagliAmmortamentoPresenti) {
		int annoDismissione =  this.bilancio.getAnno();
		for (DettaglioAmmortamentoAnnuoCespite dett : dettagliAmmortamentoPresenti) {
			if(dett.getAnno() != null && dett.getAnno().intValue() == annoDismissione) {
				return dett;
			}
		}
		throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore(" impossibile trovare un dettaglio ammortamento per l'anno " + annoDismissione));
	}
	
	/**
	 * Cancella dettagli anni successivi A dismissione.
	 *
	 * @param dettagliAmmortamentoPresenti the dettagli ammortamento presenti
	 * @param dettaglioAnnoDismissione the dettaglio anno dismissione
	 */
	private void cancellaDettagliAnniSuccessiviAVendita(AmmortamentoAnnuoCespite ammortamentoAnnuoCespite, DettaglioAmmortamentoAnnuoCespite dettaglioAnnoDismissione) {
		final String methodName ="cancellaDettagliAnniSuccessiviADismissione";
		
		//se il dettaglio dell'anno in corso ha una prima nota (non definitiva), duplico la riga in modo tale che l'operatore possa vedere che e' successa questa cosa e decidere il da farsi:
		//in quel caso pertanto devo saltare l'eliminazione di tale dettaglio
		int annoDaCuiCancellare = dettaglioAnnoDismissione.getPrimaNota() != null && dettaglioAnnoDismissione.getPrimaNota().getUid() != 0?
				dettaglioAnnoDismissione.getAnno().intValue() + 1 : dettaglioAnnoDismissione.getAnno();
		
		log.debug(methodName, "cancello i dettagli dell'ammortamento a partire dall'anno " + annoDaCuiCancellare);
		for (DettaglioAmmortamentoAnnuoCespite dettaglioDaEliminare : ammortamentoAnnuoCespite.getDettagliAmmortamentoAnnuoCespite()) {
			
			if(dettaglioDaEliminare.getAnno() != null && StringUtils.isBlank(dettaglioDaEliminare.getRegistrazioneDefinitivaAmmortamento()) && dettaglioDaEliminare.getAnno().intValue() >= annoDaCuiCancellare) {
				log.debug(methodName, "cancello il dettaglio con uid " + dettaglioDaEliminare.getUid() + " dell'anno " + dettaglioDaEliminare.getAnno());
				dettaglioAmmortamentoAnnuoCespiteDad.eliminaDettaglioAmmortamentoAnnuoCespite(dettaglioDaEliminare);
			}
		}
	}
	
	/**
	 * Carica causale plus valenza.
	 *
	 * @return the causale EP
	 */
	private CausaleEP caricaCausalePlusValenza() {
		if(this.causaleEPPlusValenza != null  && this.causaleEPPlusValenza.getUid() != 0) {
			return  this.causaleEPPlusValenza;
		}
		
		return caricaCausaleEPDaPrimaNotaINV(CODICE_CAUSALE_PLUSVALENZA);
	}
	
	/**
	 * Carica causale minus valenza.
	 *
	 * @return the causale EP
	 */
	private CausaleEP caricaCausaleMinusValenza() {
		if(this.causaleEPMinusValenza != null && this.causaleEPMinusValenza.getUid() != 0) {
			return  this.causaleEPMinusValenza;
		}
		
		return caricaCausaleEPDaPrimaNotaINV(CODICE_CAUSALE_MINUSVALENZA);
	}
	
	/**
	 * Carica causale EP da prima nota INV.
	 *
	 * @param codice the codice
	 * @return the causale EP
	 */
	private CausaleEP caricaCausaleEPDaPrimaNotaINV(String codice) {
		CausaleEP criteriDiRicerca = new CausaleEP();
		criteriDiRicerca.setCodice(codice);
		criteriDiRicerca.setAmbito(Ambito.AMBITO_INV);
		// N.B: queste ricerca puo' essere ittimizzata, ho bisogno solo dell'id
		return causaleEPDad.ricercaCausaleEPByCodice(criteriDiRicerca);
	}
	
	/**
	 * Check importo registro A.
	 * @param primaNota 
	 */
	public void checkImportoRegistroA(PrimaNota primaNota) {
		List<ImportiRegistroA> importiRegistroACalcolati = primaNotaInvDad.calcolaImportiPrimaNotaSuRegistroA(primaNota);
		List<String> identificativiErrore = new ArrayList<String>();
		for (ImportiRegistroA importiRegistroA : importiRegistroACalcolati) {
			if(importiRegistroA.getImportoDaInventariare() == null  || importiRegistroA.getImportoDaInventariare().compareTo(importiRegistroA.getImportoInventariato()) == 0) {
				continue;
			}
			String identificativoConto = new StringBuilder()
					.append("Conto: ")
					.append(StringUtils.defaultIfBlank(importiRegistroA.getContoCespite().getCodice(), ""))
					.append(" - ")
					.append(StringUtils.defaultIfBlank(importiRegistroA.getContoCespite().getDescrizione(), ""))
					.append(" , importo da inventariare: ")
					.append(importiRegistroA.getImportoDaInventariare() != null? importiRegistroA.getImportoDaInventariare() : BigDecimal.ZERO)
					.append(" , importo inventariato: ")
					.append(importiRegistroA.getImportoInventariato() != null? importiRegistroA.getImportoInventariato() : BigDecimal.ZERO)
					.toString();
			identificativiErrore.add(identificativoConto);
		}
		if(!identificativiErrore.isEmpty()) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(" esiste un'incongruenza negli importi della prima nota: " + StringUtils.join(identificativiErrore, ",")));
		}
	}
	
	/**
	 * Check prima nota da NCD.
	 */
	private void checkPrimaNotaDaNCD() {
		final String methodName="checkPrimaNotaDaNCD";
		boolean isNotaDiCredito = EventoRegistroACespiteSelector.NotaCredito.equals(this.eventoSelector);
//		
		if(!isNotaDiCredito) {
			return;
		}
		log.info(methodName, "La prima nota risulta essere generata da una nota di credito.");
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneMovFinDad.findRegistrazioniByPrimaNota(this.primaNotaDaIntegrare, Arrays.asList(StatoOperativoRegistrazioneMovFin.ANNULLATO), RegistrazioneMovFinModelDetail.EventoMovimento);
		
		if(registrazioniMovFin == null || registrazioniMovFin.isEmpty()) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Prima nota collegata ad una nota credito, ma non sono presenti registrazioni"));
		}
		
		SubdocumentoSpesa subdoc = (SubdocumentoSpesa) registrazioniMovFin.get(0).getMovimento();
		
		DocumentoSpesa ds = documentoSpesaDad.findDocumentiCollegatiByIdDocumento(subdoc.getDocumento().getUid());
		List<PrimaNota> primeNoteNotaCredito = caricaPrimeNoteDocumenti(ds.getListaDocumentiSpesaPadre());
		for (PrimaNota primaNota : primeNoteNotaCredito) {
			PrimaNota pnInventario = primaNota.getPrimaNotaInventario();
			if(pnInventario == null || !StatoAccettazionePrimaNotaDefinitiva.INTEGRATO.equals(pnInventario.getStatoAccettazionePrimaNotaDefinitiva()) ) {
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore( "Prima nota generata da nota di credito: tutte le prime note delle fatture collegate devono essere in stato " + StatoAccettazionePrimaNotaDefinitiva.INTEGRATO.getDescrizione())); 
			}
		}
		
	}
	

	/**
	 * @param docs
	 */
	private List<PrimaNota> caricaPrimeNoteDocumenti(List<DocumentoSpesa> docs) {
		final String methodName="caricaPrimeNoteDocumenti";
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione(0, Integer.MAX_VALUE);
		List<Integer> uids = new ArrayList<Integer>();
		 for (DocumentoSpesa ds : docs) {
				if(ds != null && ds.getUid() != 0) {
					uids.add(Integer.valueOf(ds.getUid()));
				}
			}
		ListaPaginata<PrimaNota> primeNote = null;
		try {
			primeNote = primaNotaInvDad.ricercaSinteticaPrimeNoteIntegrateRegistroA(this.bilancio, new PrimaNota(), null, null, null, null, null, null, null, Arrays.asList(StatoOperativoPrimaNota.PROVVISORIO, StatoOperativoPrimaNota.DEFINITIVO), null, "S", Arrays.asList(this.evento.getTipoEvento()), null, null, null, null, null, null, null, null, uids, null, null, null, null, parametriPaginazione, PrimaNotaModelDetail.PrimaNotaInventario);
		} catch (DadException e) {
			log.error(methodName, "errore durante l'elaborazione del dad");
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore durante la ricerca della prima nota nel DAD: " + e.getMessage()));
		}
		
		return primeNote;
	}
	
}
