/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.cespiti.utility.TipoCalcoloQuotaPrimoAnnoEnum;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siacbilser.integration.dad.DettaglioAmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.DismissioneCespiteDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteDismissioneCespiteResponse;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.CategoriaCespitiModelDetail;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.DismissioneCespiteModelDetail;
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
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciPrimeNoteDismissioneCespiteService extends CheckedAccountBaseService<InserisciPrimeNoteDismissioneCespite, InserisciPrimeNoteDismissioneCespiteResponse> {

	
	@Autowired
	private CespiteDad cespiteDad;	
	@Autowired
	private DismissioneCespiteDad dismissioneCespiteDad;	
	@Autowired 
	private DettaglioAmmortamentoAnnuoCespiteDad dettaglioAmmortamentoAnnuoCespiteDad;
	@Autowired 
	private AmmortamentoAnnuoCespiteDad ammortamentoAnnuoCespiteDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	@Autowired 
	InseriscePrimaNotaService inseriscePrimaNotaService;
	
	Map<Integer, PrimaNota> mappaDettaglioAmmortamentoPrimaNotaCollegata = new HashMap<Integer, PrimaNota>();
	
	private static final ModelDetail[] defaultModelDetails = new ModelDetail[]{
			CategoriaCespitiModelDetail.TipoCalcolo,
			TipoBeneCespiteModelDetail.CategoriaCespitiModelDetail, TipoBeneCespiteModelDetail.ContoPatrimoniale, TipoBeneCespiteModelDetail.ContoAmmortamento, TipoBeneCespiteModelDetail.ContoFondoAmmortamento, TipoBeneCespiteModelDetail.ContoMinusValenza, TipoBeneCespiteModelDetail.CausaleAmmortamento,
			AmmortamentoAnnuoCespiteModelDetail.DettaglioAmmortamentoAnnuoCespiteModelDetail,
			DettaglioAmmortamentoAnnuoCespiteModelDetail.PrimaNota
	};
	
	private DismissioneCespite dismissione;
	private Bilancio bilancio;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkEntita(req.getDismissioneCespite(), "dismissione");
	}
	
	@Override
	protected void init() {
		super.init();
		Utility.MDTL.addModelDetails(defaultModelDetails);
		
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);
		dismissioneCespiteDad.setEnte(ente);
		dismissioneCespiteDad.setLoginOperazione(loginOperazione);
		dettaglioAmmortamentoAnnuoCespiteDad.setEnte(ente);
		dettaglioAmmortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		ammortamentoAnnuoCespiteDad.setEnte(ente);
		ammortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		bilancioDad.setEnteEntity(ente);
	}
	
	@Transactional
	@Override
	public InserisciPrimeNoteDismissioneCespiteResponse executeService(InserisciPrimeNoteDismissioneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		
		//carico il bilancio che mi servirà per fare le prima nota
		caricaBilancio();
		
		//carico i dati relativi all'uid dismissione che mi è arrivata dalla request
		caricaDismissione();
		
		checkDismissione();
		
		//carico tutti i cespiti collegati
		List<Cespite> cespitiCollegati = caricaCespitiCollegati();
		
		for (Cespite cespite : cespitiCollegati) {
			
			log.info(methodName, "Elaborazione del cespite con uid:  " + cespite.getUid());
			
			checkDatiCespiteDismissione(cespite);	
			
			DettaglioAmmortamentoAnnuoCespite dettaglioAmmortamentoCollegatoAllaScrittura =  effettuaScrittureAmmortamentoResiduo(cespite);
			
			BigDecimal importoFondoAmmortamentoAccumulato = cespite.getAmmortamentoAnnuoCespite() != null && cespite.getAmmortamentoAnnuoCespite().getImportoTotaleAmmortato() != null && dettaglioAmmortamentoCollegatoAllaScrittura != null? 
					cespite.getAmmortamentoAnnuoCespite().getImportoTotaleAmmortato().add(dettaglioAmmortamentoCollegatoAllaScrittura.getQuotaAnnuale()) : null;
			
			PrimaNota primaNotaDismissione = inserisciPrimaNotaDismissione(cespite, importoFondoAmmortamentoAccumulato);
			
			mappaDettaglioAmmortamentoPrimaNotaCollegata.put(Integer.valueOf(dettaglioAmmortamentoCollegatoAllaScrittura.getUid()), primaNotaDismissione);
			
			dismettiCespite(cespite);
		}
		
		log.info(methodName, "Tutti i cespiti collegati alla dismissione sono stati elaborati. Elaboro la dismissione.");
		aggiornaDismissione();
		
	}
	

	/**
	 * Carica dismissione.
	 */
	private void caricaDismissione() {
		this.dismissione = dismissioneCespiteDad.findDismissioneCespiteById(req.getDismissioneCespite(), DismissioneCespiteModelDetail.CausaleEP);
	}
	
	private void checkDismissione() {
		if(this.dismissione == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("dismissione"));
		}
		
		if(this.dismissione.getCausaleEP() == null || this.dismissione.getCausaleEP() .getUid() == 0) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("dismissione", "causale dismissione non presente"));
		}
		
	}
	
	/**
	 * Carica bilancio.
	 */
	protected void caricaBilancio(){
		this.bilancio = bilancioDad.getBilancioByAnno(req.getAnnoBilancio());
		if(this.bilancio == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("inserimento prime note", "bilancio"));
		}
	}
	
	private List<Cespite> caricaCespitiCollegati() {
		return cespiteDad.caricaCespitiCollegatiDismissioneByStato(this.dismissione, Boolean.TRUE, CespiteModelDetail.TipoBeneCespiteModelDetail, CespiteModelDetail.AmmortamentoAnnuoCespiteModelDetail);
	}
	
	/**
	 * Check dati cespite dismissione.
	 *
	 * @param cespite the cespite
	 */
	private void checkDatiCespiteDismissione(Cespite cespite) {
		final String methodName ="checkDatiCespiteDismissione";
		TipoBeneCespite tipoBene = cespite.getTipoBeneCespite();
		if(tipoBene == null) {
			log.warn(methodName, "Il cespite non ha un tipo bene associato. controllare il db.");
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("cespite con codice: " + cespite.getCodice() , " tipo bene non presente. "));
		}
		//controllo il tipo bene ed i suoi conti
		String descrizioneTipoBene = new StringBuilder().append(" tipo bene ").append(tipoBene.getCodice()).append(" ").toString();
		if(tipoBene.getCausaleAmmortamento() == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore( descrizioneTipoBene, "causale ammortamento"));
		}
		
		checkConto(tipoBene.getContoPatrimoniale(), " conto patrimoniale " + descrizioneTipoBene);
		checkConto(tipoBene.getContoAmmortamento(), "conto ammortamento " + descrizioneTipoBene);
		checkConto(tipoBene.getContoFondoAmmortamento(), "fondo ammortamento" + descrizioneTipoBene);
		checkConto(tipoBene.getContoMinusValenza(), "minusvalenza" + descrizioneTipoBene);
		
		//contollo la categoria
		checkCategoria(tipoBene.getCategoriaCespiti(), descrizioneTipoBene);
		
		if(cespite.getAmmortamentoAnnuoCespite() == null || cespite.getAmmortamentoAnnuoCespite().getDettagliAmmortamentoAnnuoCespite() == null || cespite.getAmmortamentoAnnuoCespite().getDettagliAmmortamentoAnnuoCespite().isEmpty()) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("cespite con codice: " + cespite.getCodice(), "scritture non realizzabili, ammortamento non presente."));
		}
	}
	
	/**
	 * Effettua le operazioni necessarie alle scritture del fondo ammortamento e le scritture stesse.
	 *
	 * @param ammortamentoAnnuoCespite l'ammortamento del cespite
	 * @param cespite il cespite da ammortare
	 * @return l'importo della scritture effettuate
	 */
	private DettaglioAmmortamentoAnnuoCespite effettuaScrittureAmmortamentoResiduo(Cespite cespite) {
		final String methodName = "gestisciFondoAmmortamentoEScritture";
		
		AmmortamentoAnnuoCespite ammortamentoAnnuoCespite = cespite.getAmmortamentoAnnuoCespite();
		
		DettaglioAmmortamentoAnnuoCespite dettaglioAnnoDismissione = filtraDettaglioAnnoDismissione(ammortamentoAnnuoCespite.getDettagliAmmortamentoAnnuoCespite());
		
		if(dettaglioAnnoDismissione == null || dettaglioAnnoDismissione.getUid() == 0) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("cespite con codice: " + cespite.getCodice(), "scritture non realizzabili, dettaglio ammortamento per l'anno di dismissione non presente."));
		}
		
		cancellaDettagliAnniSuccessiviADismissione(ammortamentoAnnuoCespite, dettaglioAnnoDismissione);
					
		BigDecimal importoFondoAmmortamento = obtainImportoFondoAmmortamento(dettaglioAnnoDismissione, cespite);
		
		PrimaNota pn = inserisciPrimaNotaAmmortamento(importoFondoAmmortamento, cespite.getTipoBeneCespite());
		DettaglioAmmortamentoAnnuoCespite dettaglioAmmortamentoCollegatoAllaScritturaDiAmmortamentoResiduo = inserisciNuovoDettaglioAmmortamento(ammortamentoAnnuoCespite, importoFondoAmmortamento,pn);
		return dettaglioAmmortamentoCollegatoAllaScritturaDiAmmortamentoResiduo;
		
	}
	
	private DettaglioAmmortamentoAnnuoCespite inserisciNuovoDettaglioAmmortamento(AmmortamentoAnnuoCespite ammortamentoAnnuoCespite, BigDecimal importoFondoAmmortamento, PrimaNota primaNota) {
		final String methodName ="inserisciNuovoDettaglioAmmortamento";
		DettaglioAmmortamentoAnnuoCespite nuovoDettaglio = new DettaglioAmmortamentoAnnuoCespite();
		nuovoDettaglio.setQuotaAnnuale(importoFondoAmmortamento);
		nuovoDettaglio.setAnno(Integer.valueOf(Utility.getAnno(this.dismissione.getDataCessazione())));
		nuovoDettaglio.setDataAmmortamento(new Date());
		nuovoDettaglio.setPrimaNota(primaNota);
		log.debug(methodName, " inserisco un nuiovo dettaglio per l'ammortamento con importo " + nuovoDettaglio.getQuotaAnnuale().toPlainString() + " e anno: " +nuovoDettaglio.getAnno().toString() );
		dettaglioAmmortamentoAnnuoCespiteDad.inserisciDettaglioAmmortamentoAnnuoCespite(nuovoDettaglio, ammortamentoAnnuoCespite.getUid());
		if(nuovoDettaglio.getUid() == 0) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile inserire un dettaglio di ammortamento"));
		}
		return nuovoDettaglio;
	}
	
	private BigDecimal obtainImportoFondoAmmortamento(DettaglioAmmortamentoAnnuoCespite dettaglioAnnoDismissione, Cespite cespite) {
		String codiceTipoCalcolo = cespite.getTipoBeneCespite().getCategoriaCespiti().getCategoriaCalcoloTipoCespite().getCodice();
		BigDecimal multiplier = TipoCalcoloQuotaPrimoAnnoEnum.byCodiceTipoCalcolo(codiceTipoCalcolo).getFattoreProporzionamentoDaInizioAnnoAData(this.dismissione.getDataCessazione());
		BigDecimal importoFondoAmmortamento = dettaglioAnnoDismissione.getQuotaAnnuale().multiply(multiplier).setScale(2, RoundingMode.HALF_DOWN);
		return importoFondoAmmortamento;
	}
	
	/**
	 * Cancella dettagli anni successivi A dismissione.
	 *
	 * @param dettagliAmmortamentoPresenti the dettagli ammortamento presenti
	 * @param dettaglioAnnoDismissione the dettaglio anno dismissione
	 */
	private void cancellaDettagliAnniSuccessiviADismissione(AmmortamentoAnnuoCespite ammortamentoAnnuoCespite, DettaglioAmmortamentoAnnuoCespite dettaglioAnnoDismissione) {
		final String methodName ="cancellaDettagliAnniSuccessiviADismissione";
		
		//se il dettaglio dell'anno in corso ha una prima nota (non definitiva), duplico la riga in modo tale che l'operatore possa vedere che e' successa questa cosa e decidere il da farsi:
		//in quel caso pertanto devo saltare l'eliminazione di tale dettaglio
		int annoDaCuiCancellare = dettaglioAnnoDismissione.getPrimaNota() != null && dettaglioAnnoDismissione.getPrimaNota().getUid() != 0?
				dettaglioAnnoDismissione.getAnno().intValue() + 1 : dettaglioAnnoDismissione.getAnno();
		int countEliminati = 0;
		
		log.debug(methodName, "cancello i dettagli dell'ammortamento a partire dall'anno " + annoDaCuiCancellare);
		for (DettaglioAmmortamentoAnnuoCespite dettaglioDaEliminare : ammortamentoAnnuoCespite.getDettagliAmmortamentoAnnuoCespite()) {
			
			if(dettaglioDaEliminare.getAnno() != null && StringUtils.isBlank(dettaglioDaEliminare.getRegistrazioneDefinitivaAmmortamento()) && dettaglioDaEliminare.getAnno().intValue() >= annoDaCuiCancellare) {
				log.debug(methodName, "cancello il dettaglio con uid " + dettaglioDaEliminare.getUid() + " dell'anno " + dettaglioDaEliminare.getAnno());
				dettaglioAmmortamentoAnnuoCespiteDad.eliminaDettaglioAmmortamentoAnnuoCespite(dettaglioDaEliminare);
				countEliminati++;
			}
			
		}
		
		//non devo cancellare la testata, perche' ho sicuramente inserito almeno un record
//		cancellaTestataSeNecessario(ammortamentoAnnuoCespite,countEliminati);
	}

	/**
	 * Cancella testata se necessario.
	 *
	 * @param ammortamentoAnnuoCespite the ammortamento annuo cespite
	 * @param numeroDettagliEliminati the numero dettagli eliminati
	 */
	private void cancellaTestataSeNecessario(AmmortamentoAnnuoCespite ammortamentoAnnuoCespite, int numeroDettagliEliminati) {
		final String methodName="";
		if(ammortamentoAnnuoCespite != null && ammortamentoAnnuoCespite.getDettagliAmmortamentoAnnuoCespite() != null && ammortamentoAnnuoCespite.getDettagliAmmortamentoAnnuoCespite().size() != numeroDettagliEliminati) {
			//non ho eliminato tutti i dettagli, esco.
			return;
		}
		//cancello la testata
		log.info(methodName, "cancello la testata dell'ammortamento, perche' ne ho eliminato tutti i record ");
		ammortamentoAnnuoCespiteDad.eliminaAmmortamentoAnnuoCespite(ammortamentoAnnuoCespite);
	}
	
	/**
	 * Aggiorna dismissione.
	 *
	 * @param primeNoteCollegateADismissione the prime note collegate A dismissione
	 */
	private void aggiornaDismissione() {
		final String methodName ="aggiornaDismissione";
		log.info(methodName, "numero prime note dismissione da collegare:" + mappaDettaglioAmmortamentoPrimaNotaCollegata.size());
		
		dismissioneCespiteDad.collegaPrimaNotaADismissione(this.dismissione, mappaDettaglioAmmortamentoPrimaNotaCollegata);
		dismissioneCespiteDad.aggiornaStatoDismissioneCespite(this.dismissione);
	}
	
	/**
	 * Dismetti cespite.
	 *
	 * @param cespite the cespite
	 */
	private void dismettiCespite(Cespite cespite) {
		final String methodName = "dismettiCespite";
		log.debug(methodName, "dismetto il cespite rendendolo non attivo e mettendoci la data cessazione.");
		cespiteDad.dismettiCespite(cespite, this.dismissione.getDataCessazione());
	}


	private PrimaNota inserisciPrimaNotaDismissione(Cespite cespite, BigDecimal importoFondoAmmortamentoAccumulato) {
		final String methodName ="inserisciPrimaNotaDismissione";
		InseriscePrimaNota reqIPN = new InseriscePrimaNota();
		reqIPN.setRichiedente(req.getRichiedente());
		reqIPN.setDataOra(new Date());
		reqIPN.setAnnoBilancio(Integer.valueOf(this.bilancio.getAnno()));
		PrimaNota primaNota = creaPrimaNotaDismissione(cespite, importoFondoAmmortamentoAccumulato);
		reqIPN.setPrimaNota(primaNota);
		log.debug(methodName, "inserisco la prima nota per dismissione.");
		InseriscePrimaNotaResponse resIPN = serviceExecutor.executeServiceSuccess(inseriscePrimaNotaService, reqIPN);
		checkServiceResponseFallimento(resIPN);
		return resIPN.getPrimaNota();
	}

	private PrimaNota inserisciPrimaNotaAmmortamento(BigDecimal importoFondoAmmortamento, TipoBeneCespite tipoBeneCespite) {
		final String methodName = "inserisciPrimaNotaAmmortamento";
		InseriscePrimaNota reqIPN = new InseriscePrimaNota();
		reqIPN.setRichiedente(req.getRichiedente());
		reqIPN.setDataOra(new Date());
		reqIPN.setAnnoBilancio(Integer.valueOf(this.bilancio.getAnno()));
		PrimaNota primaNota = creaPrimaNotaAmmortamento(importoFondoAmmortamento, tipoBeneCespite );
		reqIPN.setPrimaNota(primaNota);
		log.info(methodName, "inserisco la prima nota per l'ammortamento.");
		InseriscePrimaNotaResponse resIPN = serviceExecutor.executeServiceSuccess(inseriscePrimaNotaService, reqIPN);
		checkServiceResponseFallimento(resIPN);
		return resIPN.getPrimaNota();

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
	
	private PrimaNota popolaDatiBasePrimaNota(CausaleEP causaleEP) {
		PrimaNota pn = new PrimaNota();

		pn.setAmbito(Ambito.AMBITO_INV);
		pn.setBilancio(this.bilancio);
		pn.setEnte(ente);
		pn.setTipoCausale(TipoCausale.Libera);
		pn.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		pn.setDataRegistrazione(this.dismissione.getDataCessazione());
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
	 * Crea prima nota dismissione.
	 *
	 * @param importoFondoAmmortamento the importo fondo ammortamento
	 * @return the prima nota
	 */
	private PrimaNota creaPrimaNotaDismissione(Cespite cespite, BigDecimal fondoAmmortamentoAccantonato) {
		PrimaNota pni = popolaDatiBasePrimaNota(this.dismissione.getCausaleEP());
		
		MovimentoEP movimentoEP = pni.getListaMovimentiEP().get(0);
		
		MovimentoDettaglio movimentoDettaglioDare1 = creaMovimentoDettaglio(cespite.getTipoBeneCespite().getContoFondoAmmortamento(),
				OperazioneSegnoConto.DARE, fondoAmmortamentoAccantonato, Integer.valueOf(1));
		MovimentoDettaglio movimentoDettaglioAvere = creaMovimentoDettaglio(cespite.getTipoBeneCespite().getContoPatrimoniale(),
				OperazioneSegnoConto.AVERE, cespite.getValoreAttuale(), Integer.valueOf(1));
		
		
		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioDare1);
		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioAvere);
		
		BigDecimal importoRimanente = cespite.getValoreAttualeNotNull().subtract(fondoAmmortamentoAccantonato, MathContext.DECIMAL128);
		
		if(BigDecimal.ZERO.compareTo(importoRimanente)>= 0) {
			return pni;
		}
		
		MovimentoDettaglio movimentoDettaglioDare2 = creaMovimentoDettaglio(cespite.getTipoBeneCespite().getContoMinusValenza(),
				OperazioneSegnoConto.DARE, importoRimanente, Integer.valueOf(1));
		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglioDare2);
		return pni;
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
		MovimentoDettaglio mov = new MovimentoDettaglio();
		mov.setAmbito(Ambito.AMBITO_INV);
		mov.setConto(contoPatrimoniale);
		mov.setEnte(ente);
		mov.setImporto(importo);
		mov.setSegno(operazioneSegnoConto);
		mov.setNumeroRiga(numeroRiga);
		return mov;
	}

	private void checkCategoria(CategoriaCespiti categoriaCespiti, String descrizioneTipoBene) {
		if(categoriaCespiti == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("inserimento prima nota", "categoria del"+ descrizioneTipoBene));
		}
		if(categoriaCespiti.getCategoriaCalcoloTipoCespite() == null || StringUtils.isEmpty(categoriaCespiti.getCategoriaCalcoloTipoCespite().getCodice())) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("inserimento prima nota", " tipo calcolo legato alla categoria del "+ descrizioneTipoBene));
		}
	}

	private void checkConto(Conto conto, String descrizioneConto) {
		if(conto == null || conto.getUid() == 0) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("tipo bene", descrizioneConto + "non presente. "));
		}
	}

	private DettaglioAmmortamentoAnnuoCespite filtraDettaglioAnnoDismissione(List<DettaglioAmmortamentoAnnuoCespite> dettagliAmmortamentoPresenti) {
		int annoDismissione = Utility.getAnno(this.dismissione.getDataCessazione());
		for (DettaglioAmmortamentoAnnuoCespite dett : dettagliAmmortamentoPresenti) {
			if(dett.getAnno() != null && dett.getAnno().intValue() == annoDismissione) {
				return dett;
			}
		}
		return null;
	}

}