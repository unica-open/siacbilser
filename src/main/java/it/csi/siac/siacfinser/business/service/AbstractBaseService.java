/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.AccountDad;
import it.csi.siac.siacbilser.integration.dao.MovimentoGestioneDao;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.CapitoloUscitaGestioneModelDetail;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.enumeration.CodiceEventoEnum;
import it.csi.siac.siacfinser.business.service.enumeration.CodiciControlloInnestiFinGenEnum;
import it.csi.siac.siacfinser.business.service.util.NumericUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.PaginazioneRequest;
import it.csi.siac.siacfinser.integration.dad.AccertamentoOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.ImpegnoOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dad.MovimentoGestioneDad;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.dao.common.dto.ChiaveLogicaCapitoloDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAttivaRegistrazioniMovFinFINGSADto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImpegnoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaMovimentoGestioneEntrataInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaMovimentoGestioneSpesaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaVincoliImpegnoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovGestInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.helper.ModalitaPagamentoSoggettoHelper;
import it.csi.siac.siacfinser.integration.util.dto.PaginazioneCustomDto;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

public abstract class AbstractBaseService<REQ extends ServiceRequest, RES extends ServiceResponse>
		extends ExtendedBaseService<REQ, RES> {

	@Autowired
	protected AccertamentoOttimizzatoDad accertamentoOttimizzatoDad;

	@Autowired
	protected ImpegnoOttimizzatoDad impegnoOttimizzatoDad;

	@Autowired
	protected SoggettoFinDad soggettoDad;

	@Autowired
	protected CapitoloUscitaGestioneService capitoloUscitaGestioneService;

	@Autowired
	protected CapitoloEntrataGestioneService capitoloEntrataGestioneService;

	@Autowired
	protected ProvvedimentoService provvedimentoService;

	@Autowired
	protected AccountDad accountDad;

	@Autowired
	protected CommonDad commonDad;

	@Autowired
	protected RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	
	//SIAC-7349 - GM - 13/07/2020
	@Autowired
	protected MovimentoGestioneDao movimentoGestioneDao;
	
	protected Ente ente;
	protected String loginOperazione;
	protected Bilancio bilancio;
	
	private Boolean isImpegnoInPartitoDiGiro = false;
	
	@Autowired
	protected ApplicationContext appCtx;
	
	protected ModalitaPagamentoSoggettoHelper modalitaPagamentoSoggettoHelper;
	
	/**
	 * Inizializza l'helper per la modalita pagamento soggetto
	 * iniettandoci il contesto spring
	 */
	protected void initModalitaPagamentoSoggettoHelper(){
		if(modalitaPagamentoSoggettoHelper==null){
			//MODALITA PAGAMENTO SOGGETTO HELPER:
			modalitaPagamentoSoggettoHelper = new ModalitaPagamentoSoggettoHelper(appCtx);
			modalitaPagamentoSoggettoHelper.init();
		}
	}
	
	protected <MG extends MovimentoGestione> MG ricaricaMovimentoPerAnnullaModifica(Richiedente richiedente,BigDecimal numeroMovimento,String annoEsercizio,Integer annoMovimento,String tipoMovimento){
		MG movCaricato = null;
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		paginazioneSubMovimentiDto.setNoSub(true);
		boolean caricaDatiUlteriori=true;//va messo a true altrimenti perde il soggetto
		EsitoRicercaMovimentoPkDto esitoRicerca = null;
		
		//SIAC-6835 caricamento cig
		DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		caricaDatiOpzionaliDto.setCaricaCig(true);
		
		if(CostantiFin.MOVGEST_TIPO_IMPEGNO.equals(tipoMovimento)){
			esitoRicerca = impegnoOttimizzatoDad.ricercaMovimentoPk(
					richiedente, ente, annoEsercizio, annoMovimento, numeroMovimento,
					paginazioneSubMovimentiDto, caricaDatiOpzionaliDto, tipoMovimento, caricaDatiUlteriori, true);
			if(esitoRicerca!=null && esitoRicerca.getMovimentoGestione()!=null){
				movCaricato = (MG) esitoRicerca.getMovimentoGestione();
				movCaricato = (MG) completaDatiRicercaImpegnoPk(richiedente, (Impegno) movCaricato, annoEsercizio,null,null,null);
				((Impegno) movCaricato).setElencoSubImpegni(esitoRicerca.getElencoSubImpegniTuttiConSoloGliIds());
			}
		}else {
			esitoRicerca = accertamentoOttimizzatoDad.ricercaMovimentoPk(
					richiedente, ente, annoEsercizio,
					annoMovimento , numeroMovimento , paginazioneSubMovimentiDto,
					caricaDatiOpzionaliDto, tipoMovimento, caricaDatiUlteriori, true);
			if(esitoRicerca!=null && esitoRicerca.getMovimentoGestione()!=null){
				movCaricato = (MG) esitoRicerca.getMovimentoGestione();
				movCaricato = (MG) completaDatiRicercaAccertamentoPk(richiedente, (Accertamento) movCaricato, null,null,null);
				((Accertamento) movCaricato).setElencoSubAccertamenti(esitoRicerca.getElencoSubAccertamentiTuttiConSoloGliIds());
			}
		}
		return movCaricato;
	}
	
	@Override
	protected void checkRichiedente() throws ServiceParamError {
		super.checkRichiedente();

		checkNotNull(req.getRichiedente().getAccount(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO
						.getErrore("account richiedente"));
		loginOperazione = accountDad.findLoginOperazioneByAccountId(req
				.getRichiedente().getAccount().getUid());

		caricaEnteAssociatoAdAccount();
		
		// caricaAzioniConsentiteRichiedente();
		// checkAzioneConsentita();

	}
	
	
	protected void checkParametriPaginazione(PaginazioneRequest paramPaginazione) throws ServiceParamError { 
		
		checkCondition(paramPaginazione.getNumPagina() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"));
		checkCondition(paramPaginazione.getNumRisultatiPerPagina() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elementi per pagina parametri paginazione"));
	}

	private void caricaEnteAssociatoAdAccount() {
		this.ente = accountDad.findEnteAssocciatoAdAccount(req.getRichiedente()
				.getAccount().getUid());
	}

	/**
	 * Metodo di comodo per travasare i waring ricevuti nella resp
	 * 
	 * @param esito
	 */
	protected void addWarningToResp(EsitoControlliDto esito) {
		if (esito.getListaWarning() != null
				&& esito.getListaWarning().size() > 0) {
			res.addErrori(esito.getListaWarning());
		}
	}

	protected Timestamp getNow() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Restituisce una sublist di risultati in base ai parametri passati; se
	 * numPagina == 0 o numRisultatiPerPagina == 0 non pagina e ritorna tutti i
	 * risultati
	 * 
	 * @param totale
	 *            elenco di risultati totale
	 * @param numPagina
	 *            numero di pagina richiesto
	 * @param numRisultatiPerPagina
	 *            numero di record per pagina
	 * @return sublist di risultati o elenco totale
	 */
	protected <T> List<T> getPaginata(List<T> totale, int numPagina,
			int numRisultatiPerPagina) {
		if (totale == null || numPagina > totale.size())
			return new ArrayList<T>(0);
		if (numPagina == 0 || numRisultatiPerPagina == 0)
			return totale;
		int start = (numPagina - 1) * numRisultatiPerPagina;
		int end = start + numRisultatiPerPagina;
		if (end > totale.size())
			end = totale.size();
		return new ArrayList<T>(totale.subList(start, end));
	}

	/**
	 * metodo generico per l'operazione "ripetitiva" dell'aggiungere un
	 * errorefin alla response
	 * 
	 * @param erroreFin
	 * @param args
	 */
	protected void addErroreFin(ErroreFin erroreFin, Object... args) {
		List<Errore> listaErroriAttuale = res.getErrori();
		if (listaErroriAttuale == null) {
			listaErroriAttuale = new ArrayList<Errore>();
		}
		if (args != null && args.length > 0) {
			listaErroriAttuale.add(erroreFin.getErrore(args));
		} else {
			listaErroriAttuale.add(erroreFin.getErrore());
		}
		res.setErrori(listaErroriAttuale);
		res.setEsito(Esito.FALLIMENTO);
	}

	/**
	 * metodo generico per l'operazione "ripetitiva" dell'aggiungere un
	 * erroreatt alla response
	 * 
	 * @param erroreAtt
	 * @param args
	 */
	protected void addErroreAtt(ErroreAtt erroreAtt, Object... args) {
		List<Errore> listaErroriAttuale = res.getErrori();
		if (listaErroriAttuale == null) {
			listaErroriAttuale = new ArrayList<Errore>();
		}
		if (args != null && args.length > 0) {
			listaErroriAttuale.add(erroreAtt.getErrore(args));
		} else {
			listaErroriAttuale.add(erroreAtt.getErrore());
		}
		res.setErrori(listaErroriAttuale);
		res.setEsito(Esito.FALLIMENTO);
	}

	/**
	 * metodo generico per l'operazione "ripetitiva" dell'aggiungere un
	 * errorebil alla response
	 * 
	 * @param erroreBil
	 * @param args
	 */
	protected void addErroreBil(ErroreBil erroreBil, Object... args) {
		List<Errore> listaErroriAttuale = res.getErrori();
		if (listaErroriAttuale == null) {
			listaErroriAttuale = new ArrayList<Errore>();
		}
		if (args != null && args.length > 0) {
			listaErroriAttuale.add(erroreBil.getErrore(args));
		} else {
			listaErroriAttuale.add(erroreBil.getErrore());
		}
		res.setErrori(listaErroriAttuale);
		res.setEsito(Esito.FALLIMENTO);
	}
	
	/**
	 * metodo generico per l'operazione "ripetitiva" dell'aggiungere un
	 * errorecore alla response
	 * 
	 * @param erroreFin
	 * @param args
	 */
	protected void addErroreCore(ErroreCore erroreCore, Object... args) {
		List<Errore> listaErroriAttuale = res.getErrori();
		if (listaErroriAttuale == null) {
			listaErroriAttuale = new ArrayList<Errore>();
		}
		if (args != null && args.length > 0) {
			listaErroriAttuale.add(erroreCore.getErrore(args));
		} else {
			listaErroriAttuale.add(erroreCore.getErrore());
		}
		res.setErrori(listaErroriAttuale);
		res.setEsito(Esito.FALLIMENTO);
	}
	
	protected CapitoloUscitaGestione caricaCapitoloUscitaGestioneSinteticaCaching(Richiedente richiedente, int chiaveCapitolo,
			HashMap<Integer, CapitoloUscitaGestione> cacheCapitolo){
		return caricaCapitoloUscitaGestioneSinteticaCaching(richiedente, chiaveCapitolo, cacheCapitolo, null);
	}
	
	protected CapitoloUscitaGestione caricaCapitoloUscitaGestioneSinteticaCaching(Richiedente richiedente, int chiaveCapitolo,
			HashMap<Integer, CapitoloUscitaGestione> cacheCapitolo,DatiOpzionaliCapitoli datiOpzionaliCapitoli){
		CapitoloUscitaGestione capitoloUscitaGestione = null;
		
		Integer key = chiaveCapitolo;
		if(cacheCapitolo.containsKey(key)){
			return clone(cacheCapitolo.get(key));
		} else {
			capitoloUscitaGestione = caricaCapitoloUscitaGestione(richiedente, chiaveCapitolo, true,datiOpzionaliCapitoli);
			cacheCapitolo.put(key, clone(capitoloUscitaGestione));
		}
		
		return capitoloUscitaGestione;
	}
	
	/**
	 * Wrapper di retro compatibilita'
	 * @param richiedente
	 * @param chiaveCapitolo
	 * @param sintetica
	 * @return
	 */
	protected CapitoloUscitaGestione caricaCapitoloUscitaGestione(Richiedente richiedente, int chiaveCapitolo, boolean sintetica) {
		return caricaCapitoloUscitaGestione(richiedente, chiaveCapitolo, sintetica, null);
	}

	protected CapitoloUscitaGestione caricaCapitoloUscitaGestione(Richiedente richiedente, int chiaveCapitolo, boolean sintetica,DatiOpzionaliCapitoli datiOpzionaliCapitoli) {
		CapitoloUscitaGestione capitoloUscitaGestione = null;
		if (sintetica) {
			// caricamento sintetico:
			ChiaveLogicaCapitoloDto chiaveLogica = commonDad.chiaveFisicaToLogicaCapitolo(chiaveCapitolo);
			RicercaSinteticaCapitoloUscitaGestione rceg = buildRequestPerRicercaSinteticaCapitoloUG(richiedente.getAccount().getEnte(), richiedente,chiaveLogica,datiOpzionaliCapitoli);
			RicercaSinteticaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(rceg);
			if(response != null && response.getCapitoli()!=null && response.getCapitoli().size()>0){
				capitoloUscitaGestione = response.getCapitoli().get(0);
			} else {
				//CODICE PER DIAGNOSTICA, NON DOVREBBE MAI NON TROVARLO EPPURE E' RISULTATO DAI LOG:
				String messaggioEnte = " richiedente.getAccount().getEnte() = null ";
				if(richiedente.getAccount().getEnte()!=null){
					messaggioEnte = " richiedente.getAccount().getEnte() = " + richiedente.getAccount().getEnte().getUid();
				}
				int accountUid = richiedente.getAccount().getUid();
				log.error("AbstractBaseService caricaCapitoloUscitaGestione", "Capitolo non trovato con "
						+ "chiaveCapitolo: " + chiaveCapitolo 
						+ messaggioEnte
						+ " accountUid: " + accountUid);
			}
		} else {
			// dettaglio completo:
			RicercaDettaglioCapitoloUGest ricercaDettaglioCapitoloUGest = new RicercaDettaglioCapitoloUGest();
			ricercaDettaglioCapitoloUGest.setChiaveCapitolo(chiaveCapitolo);

			RicercaDettaglioCapitoloUscitaGestione ricercaDettaglioCapitoloUscitaGestione = new RicercaDettaglioCapitoloUscitaGestione();
			ricercaDettaglioCapitoloUscitaGestione.setRicercaDettaglioCapitoloUGest(ricercaDettaglioCapitoloUGest);
			ricercaDettaglioCapitoloUscitaGestione.setRichiedente(richiedente);
			ricercaDettaglioCapitoloUscitaGestione.setEnte(richiedente.getAccount().getEnte());

			RicercaDettaglioCapitoloUscitaGestioneResponse ricercaDettaglioCapitoloUscitaGestioneResponse = capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(ricercaDettaglioCapitoloUscitaGestione);
			capitoloUscitaGestione = ricercaDettaglioCapitoloUscitaGestioneResponse.getCapitoloUscita();
		}
		return capitoloUscitaGestione;
	}
	
	/**
	 * 
	 * Metodo di comodo per caricare gli immporti capitolo ug di un capitolo a partire dall'anno del bilancio indicato.
	 * 
	 * Si basa sul richiamo del servizio ricercaDettaglioModulareCapitoloUscitaGestione
	 * 
	 * @param richiedente
	 * @param chiaveCapitolo
	 * @param bilancio
	 * @return
	 */
	protected List<ImportiCapitoloUG>  caricaListaImportiCapitoloUG(Richiedente richiedente, int chiaveCapitolo,Bilancio bilancio){
		List<ImportiCapitoloUG>  listaImportiCapitoloUG = null;
		
		CapitoloUscitaGestione capitoloUscitaGestione = caricaCapitoloModulare(richiedente, chiaveCapitolo, bilancio);
		if(capitoloUscitaGestione!=null){
			listaImportiCapitoloUG = new ArrayList<ImportiCapitoloUG>();
			
			if(capitoloUscitaGestione.getImportiCapitoloUG()!=null){
				listaImportiCapitoloUG.add(capitoloUscitaGestione.getImportiCapitoloUG());
			}
			
			if(!it.csi.siac.siacfinser.StringUtilsFin.isEmpty(capitoloUscitaGestione.getListaImportiCapitoloUG())){
				listaImportiCapitoloUG.addAll(capitoloUscitaGestione.getListaImportiCapitoloUG());
			}
		}
		
		return listaImportiCapitoloUG;
	}
	
	/**
	 * Metodo di comodo per gestire il richiamo del servizio ricercaDettaglioModulareCapitoloUscitaGestione
	 * 
	 * @param richiedente
	 * @param chiaveCapitolo
	 * @param bilancio
	 * @return
	 */
	protected CapitoloUscitaGestione caricaCapitoloModulare(Richiedente richiedente, int chiaveCapitolo,Bilancio bilancio){
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		
		RicercaDettaglioModulareCapitoloUscitaGestione reqModulare = new RicercaDettaglioModulareCapitoloUscitaGestione();
		
		reqModulare.setRichiedente(richiedente);
		reqModulare.setModelDetails(CapitoloUscitaGestioneModelDetail.Importi);
		
		//capitolo:
		capitoloUscitaGestione.setUid(chiaveCapitolo);
		reqModulare.setCapitoloUscitaGestione(capitoloUscitaGestione);
		//
		
		//bilancio di riferimento:
		capitoloUscitaGestione.setBilancio(bilancio);
		//
		
		RicercaDettaglioModulareCapitoloUscitaGestioneResponse resp = capitoloUscitaGestioneService.ricercaDettaglioModulareCapitoloUscitaGestione(reqModulare);
		if(resp!=null && (resp.getErrori()==null || resp.getErrori().size()==0) ){
			capitoloUscitaGestione = resp.getCapitoloUscitaGestione();
		}
		
		return capitoloUscitaGestione;
	}

	protected HashMap<Integer, CapitoloUscitaGestione> caricaCapitoloUscitaGestioneEResiduo(
			Richiedente richiedente, Integer chiaveCapitolo,
			Integer chiaveCapitoloResiduo) {
		HashMap<Integer, CapitoloUscitaGestione> capitoliDaServizio = new HashMap<Integer, CapitoloUscitaGestione>();
		CapitoloUscitaGestione capitoloUscitaGestione = caricaCapitoloUscitaGestione(
				richiedente, chiaveCapitolo, true);
		capitoliDaServizio.put(chiaveCapitolo, capitoloUscitaGestione);
		if (chiaveCapitoloResiduo != null) {
			CapitoloUscitaGestione capitoloUscitaGestioneResiduo = caricaCapitoloUscitaGestione(
					richiedente, chiaveCapitoloResiduo, true);
			capitoliDaServizio.put(chiaveCapitoloResiduo,
					capitoloUscitaGestioneResiduo);
		}
		return capitoliDaServizio;
	}

	/**
	 * funzione che effettua la ricerca sintetica per capitolo uscita ed evita
	 * quella di dettaglio che altrimenti sarebbe troppo pesante
	 * 
	 * @param richiedente
	 * @param ente
	 * @param impegno
	 * @param bilancio
	 * @return
	 */
	protected HashMap<Integer, CapitoloUscitaGestione> mapRicercaSinteticaCapitoloUscita(
			Richiedente richiedente, Ente ente, Impegno impegno,
			Bilancio bilancio) {

		HashMap<Integer, CapitoloUscitaGestione> capitoliDaServizio = new HashMap<Integer, CapitoloUscitaGestione>();
		CapitoloUscitaGestione capitoloUscitaGestione = null;

		ListaPaginata<CapitoloUscitaGestione> listaCapitoliUscita = getRicercaSinteticaCapitoloUscita(
				richiedente, ente, impegno, bilancio);
		if (listaCapitoliUscita != null) {
			// prendo sempre il primo elemento, tanto ce ne sara' sempre uno,
			// visto che da front end e' gia' verificato e trappato il caso in
			// cui ci
			// siano piu' capitoli
			capitoloUscitaGestione = listaCapitoliUscita.get(0);
			capitoliDaServizio.put(capitoloUscitaGestione.getUid(),
					capitoloUscitaGestione);
		}

		return capitoliDaServizio;
	}

	/**
	 * funzione che effettua la ricerca sintetica per capitolo entrata ed evita
	 * quella di dettaglio che altrimenti sarebbe troppo pesante
	 * 
	 * @param richiedente
	 * @param ente
	 * @param accertamento
	 * @param bilancio
	 * @return
	 */
	protected HashMap<Integer, CapitoloEntrataGestione> mapRicercaSinteticaCapitoloEntrata(
			Richiedente richiedente, Ente ente, Accertamento accertamento,
			Bilancio bilancio) {

		HashMap<Integer, CapitoloEntrataGestione> capitoliDaServizio = new HashMap<Integer, CapitoloEntrataGestione>();
		CapitoloEntrataGestione capitoloEntrataGestione = null;

		ListaPaginata<CapitoloEntrataGestione> listaCapitoliEntrata = getRicercaSinteticaCapitoloEntrata(
				richiedente, ente, accertamento, bilancio);
		if (listaCapitoliEntrata != null) {
			// prendo sempre il primo elemento, tanto ce ne sara' sempre uno,
			// visto che da front end e' gia' verificato e trappato il caso in
			// cui ci
			// siano piu' capitoli
			capitoloEntrataGestione = listaCapitoliEntrata.get(0);
			capitoliDaServizio.put(capitoloEntrataGestione.getUid(),
					capitoloEntrataGestione);
		}

		return capitoliDaServizio;
	}

	protected HashMap<Integer, CapitoloEntrataGestione> caricaCapitolEntrataGestioneEResiduo(Richiedente richiedente, Integer chiaveCapitolo, Integer chiaveCapitoloResiduo) {
		final String methodName="caricaCapitolEntrataGestioneEResiduo";
		HashMap<Integer, CapitoloEntrataGestione> capitoliDaServizio = new HashMap<Integer, CapitoloEntrataGestione>();
		log.debug(methodName, "carico il capitolo di entrata con chiave: " + (chiaveCapitolo != null? chiaveCapitolo : "null"));
		
		CapitoloEntrataGestione capitoloUscitaGestione = caricaCapitoloEntrataGestione(richiedente, chiaveCapitolo, true);
		
		capitoliDaServizio.put(chiaveCapitolo, capitoloUscitaGestione);
		
		if (chiaveCapitoloResiduo != null) {
			log.debug(methodName, "carico il capitolo residuo");
			CapitoloEntrataGestione capitoloUscitaGestioneResiduo = caricaCapitoloEntrataGestione(
					richiedente, chiaveCapitoloResiduo, true);
			capitoliDaServizio.put(chiaveCapitoloResiduo,
					capitoloUscitaGestioneResiduo);
		}
		return capitoliDaServizio;
	}
	
	/**
	 * Wrapper di retro compatibilita'
	 * @param richiedente
	 * @param chiaveCapitolo
	 * @param sintetica
	 * @return
	 */
	protected CapitoloEntrataGestione caricaCapitoloEntrataGestione(Richiedente richiedente, int chiaveCapitolo, boolean sintetica) {
		return caricaCapitoloEntrataGestione(richiedente, chiaveCapitolo, sintetica, null);
	}
	
	protected CapitoloEntrataGestione caricaCapitoloEntrataGestioneCaching(	Richiedente richiedente, int chiaveCapitolo, boolean sintetica,
			DatiOpzionaliCapitoli datiOpzionaliCapitoli,HashMap<Integer, CapitoloEntrataGestione> cacheCapitolo) {
		
		CapitoloEntrataGestione capitoloEntrataGestione = null;
		
		Integer key = chiaveCapitolo;
		if(cacheCapitolo.containsKey(key)){
			return clone(cacheCapitolo.get(key));
		} else {
			capitoloEntrataGestione = caricaCapitoloEntrataGestione(richiedente, chiaveCapitolo, sintetica,datiOpzionaliCapitoli);
			cacheCapitolo.put(key, clone(capitoloEntrataGestione));
		}
		
		return capitoloEntrataGestione;
	}
	
	protected CapitoloEntrataGestione caricaCapitoloEntrataGestione(
			Richiedente richiedente, int chiaveCapitolo, boolean sintetica,DatiOpzionaliCapitoli datiOpzionaliCapitoli) {
		CapitoloEntrataGestione capitoloEntrataGestione = null;
		if (sintetica) {
			// approccio sintetico:
			ChiaveLogicaCapitoloDto chiaveLogica = commonDad
					.chiaveFisicaToLogicaCapitolo(chiaveCapitolo);
			RicercaSinteticaCapitoloEntrataGestione rceg = buildRequestPerRicercaSinteticaCapitoloEG(
					richiedente.getAccount().getEnte(), richiedente,
					//SIAC-8065
					CoreUtil.checkEntity(chiaveLogica, ChiaveLogicaCapitoloDto.class),
					datiOpzionaliCapitoli);
			RicercaSinteticaCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService
					.ricercaSinteticaCapitoloEntrataGestione(rceg);
			//SIAC-8065 si controlla la response
			capitoloEntrataGestione = checkResponseCapitoloEntrataGestione(response);
		} else {
			// Dettaglio completo:
			RicercaDettaglioCapitoloEGest ricercaDettaglioCapitoloEGest = new RicercaDettaglioCapitoloEGest();
			ricercaDettaglioCapitoloEGest.setChiaveCapitolo(chiaveCapitolo);

			RicercaDettaglioCapitoloEntrataGestione ricercaDettaglioCapitoloEntrataGestione = new RicercaDettaglioCapitoloEntrataGestione();
			ricercaDettaglioCapitoloEntrataGestione
					.setRicercaDettaglioCapitoloEGest(ricercaDettaglioCapitoloEGest);
			ricercaDettaglioCapitoloEntrataGestione.setRichiedente(richiedente);
			ricercaDettaglioCapitoloEntrataGestione.setEnte(richiedente
					.getAccount().getEnte());

			RicercaDettaglioCapitoloEntrataGestioneResponse ricercaDettaglioCapitoloEntrataGestioneResponse = capitoloEntrataGestioneService
					.ricercaDettaglioCapitoloEntrataGestione(ricercaDettaglioCapitoloEntrataGestione);
			capitoloEntrataGestione = ricercaDettaglioCapitoloEntrataGestioneResponse
					.getCapitoloEntrataGestione();
		}
		return capitoloEntrataGestione;
	}
	
	/**
	 * SIAC-8065
	 * Valutazione della response
	 * @param <RicercaSinteticaCapitoloEntrataGestioneResponse> response
	 * @param <ChiaveLogicaCapitoloDto> chiaveLogica
	 * @return <CapitoloEntrataGestione> or thrown <BusinessException>
	 */
	private CapitoloEntrataGestione checkResponseCapitoloEntrataGestione(RicercaSinteticaCapitoloEntrataGestioneResponse response) {
		String methodName = "checkResponseCapitoloEntrataGestione";
		if(response != null && !CollectionUtils.isEmpty(response.getCapitoli())) {
			return response.getCapitoli().get(0);
		} else {
			log.error(methodName, "No result for: CAPITOLO ENTRATA GESTIONE from service: ricercaSinteticaCapitoloEntrataGestione()");
			throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo entrata gestione"));
		}
//		return response != null && !CollectionUtils.isEmpty(response.getCapitoli()) ? response.getCapitoli().get(0) : null;
	}
	
	protected CapitoloEntrataGestione caricaCapitoloEntrataGestioneOnlyKey(Richiedente richiedente, int chiaveCapitolo) {
		CapitoloEntrataGestione capitoloEntrataGestione = null;
			// caricamento sintetico:
			ChiaveLogicaCapitoloDto chiaveLogica = commonDad.chiaveFisicaToLogicaCapitolo(chiaveCapitolo);
			capitoloEntrataGestione = new CapitoloEntrataGestione();
			capitoloEntrataGestione.setAnnoCapitolo(chiaveLogica.getAnnoCapitolo());
			capitoloEntrataGestione.setNumeroCapitolo(chiaveLogica.getNumeroCapitolo());
			capitoloEntrataGestione.setNumeroArticolo(chiaveLogica.getNumeroArticolo());
			capitoloEntrataGestione.setNumeroUEB(chiaveLogica.getNumeroUeb());
		return capitoloEntrataGestione;
	}
	
	protected CapitoloUscitaGestione caricaCapitoloUscitaGestioneOnlyKey(
			Richiedente richiedente, int chiaveCapitolo) {
		CapitoloUscitaGestione capitoloUscitaGestione = null;
			// caricamento sintetico:
			ChiaveLogicaCapitoloDto chiaveLogica = commonDad
					.chiaveFisicaToLogicaCapitolo(chiaveCapitolo);
			capitoloUscitaGestione = new CapitoloUscitaGestione();
			capitoloUscitaGestione.setAnnoCapitolo(chiaveLogica.getAnnoCapitolo());
			capitoloUscitaGestione.setNumeroCapitolo(chiaveLogica.getNumeroCapitolo());
			capitoloUscitaGestione.setNumeroArticolo(chiaveLogica.getNumeroArticolo());
			capitoloUscitaGestione.setNumeroUEB(chiaveLogica.getNumeroUeb());
		return capitoloUscitaGestione;
	}

	protected Impegno completaDisponibilitaImpegno(Richiedente richiedente,
			Ente ente, Impegno impegno, Bilancio bilancio) {

		//OTTIMIZZAZIONI APRILE 2016:
		
		Impegno impegnoRitorno = impegno;
				
		DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		
		caricaDatiOpzionaliDto.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		caricaDatiOpzionaliDto.setCaricaElencoModificheMovGest(true);

		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		paginazioneSubMovimentiDto.setNoSub(true);
		
		EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente,
				ente, String.valueOf(bilancio.getAnno()),
				Integer.valueOf(impegno.getAnnoMovimento()),
				impegno.getNumeroBigDecimal(), paginazioneSubMovimentiDto, caricaDatiOpzionaliDto, CostantiFin.MOVGEST_TIPO_IMPEGNO, false, false);
		
		if(esitoRicercaMov!=null){
			impegnoRitorno = (Impegno) esitoRicercaMov.getMovimentoGestione();
		}

		return impegnoRitorno;

	}

	protected Accertamento completaDisponibilitaAccertamento(
			Richiedente richiedente, Ente ente, Accertamento accertamento,
			Bilancio bilancio) {

		Accertamento accertamentoRitorno = accertamento;

		accertamentoRitorno = (Accertamento) accertamentoOttimizzatoDad
				.ricercaMovimentoPk(richiedente, ente,
						String.valueOf(bilancio.getAnno()),
						Integer.valueOf(accertamento.getAnnoMovimento()),
						accertamento.getNumeroBigDecimal(),
						CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, false, false);

		return accertamentoRitorno;

	}

	/**
	 * Wrapper di retrocompatibilita'
	 * @param richiedente
	 * @param impegno
	 * @param annoEsercizio
	 * @return
	 */
	protected Impegno completaDatiRicercaImpegnoPk(Richiedente richiedente,Impegno impegno, String annoEsercizio) {
		return completaDatiRicercaImpegnoPk(richiedente, impegno, annoEsercizio, null, null,null);
	}
	
	protected CapitoloUscitaGestione caricaCapitoloUscitaGestioneCaching(Richiedente richiedente, int chiaveCapitolo,
			boolean sintetica,DatiOpzionaliCapitoli datiOpzionaliCapitoli,HashMap<Integer, CapitoloUscitaGestione> cacheCapitolo) {

		CapitoloUscitaGestione capitoloUscitaGestione = null;
		
		Integer key = chiaveCapitolo;
		if(cacheCapitolo.containsKey(key)){
			return clone(cacheCapitolo.get(key));
		} else {
			capitoloUscitaGestione = caricaCapitoloUscitaGestione(richiedente, chiaveCapitolo, sintetica,datiOpzionaliCapitoli);
			cacheCapitolo.put(key, clone(capitoloUscitaGestione));
		}
		
		return capitoloUscitaGestione;
	}
	
	protected Impegno completaDatiRicercaImpegnoPk(Richiedente richiedente,Impegno impegno, String annoEsercizio,
			DatiOpzionaliCapitoli datiOpzionaliCapitoli,HashMap<Integer,
			CapitoloUscitaGestione> cacheCapitolo,HashMap<String, AttoAmministrativo> cacheAttoAmm ) {
		
		if(cacheCapitolo==null){
			//se il chiamante non fa uso della cache ne istanzio una per evitare null pointer
			cacheCapitolo = new HashMap<Integer, CapitoloUscitaGestione>();
		}
		
		if(cacheAttoAmm==null){
			//se il chiamante non fa uso della cache ne istanzio una per evitare null pointer
			cacheAttoAmm = new HashMap<String, AttoAmministrativo>();
		}
		
		//CapitoloUscitaGestione capitoloUscitaGestione = caricaCapitoloUscitaGestione(richiedente, impegno.getChiaveCapitoloUscitaGestione(), true,datiOpzionaliCapitoli);
		CapitoloUscitaGestione capitoloUscitaGestione = caricaCapitoloUscitaGestioneCaching(richiedente, impegno.getChiaveCapitoloUscitaGestione(), true, datiOpzionaliCapitoli, cacheCapitolo);
		
		
		if (null != capitoloUscitaGestione){
			impegno.setCapitoloUscitaGestione(capitoloUscitaGestione);
		}
		
		
		if (null != impegno.getAttoAmmAnno() && null != impegno.getAttoAmmNumero() && null != impegno.getAttoAmmTipoAtto()) {
			// leggo l'idStruttura amministrativa, che puo' anche essere nullo:
			Integer idStrutturaAmministrativa = impegno.getIdStrutturaAmministrativa();
			
			AttoAmministrativo attoAmministrativo = estraiAttoAmministrativoCaching(richiedente, impegno.getAttoAmmAnno(),impegno.getAttoAmmNumero(), impegno.getAttoAmmTipoAtto(),idStrutturaAmministrativa,cacheAttoAmm);
			
			if (null != attoAmministrativo){
				impegno.setAttoAmministrativo(attoAmministrativo);
			}
				
		}
		
		//SIAC-7349: recupero il deltaImportoVincolo di ogni MovimentoMadifica di spesa ai fini del calcolo del residuo "diCuiPending" sui vincoli
		//           GM - 13/07/2020
		List<ModificaMovimentoGestioneSpesa> elencoMMGSImp = impegno.getListaModificheMovimentoGestioneSpesa();
		if (null != elencoMMGSImp && elencoMMGSImp.size() > 0) {
			for (ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesaImp : elencoMMGSImp) {
				BigDecimal importoDeltaVincolo = movimentoGestioneDao.calcolaTotaleImportoDeltaByModificaId(modificaMovimentoGestioneSpesaImp.getUid());
				if(importoDeltaVincolo != null && importoDeltaVincolo.compareTo(BigDecimal.ZERO) > 0){
					modificaMovimentoGestioneSpesaImp.setImportoDeltaVincolo(importoDeltaVincolo);
				}
			}
		}
		
		List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesaImp = impegno.getListaModificheMovimentoGestioneSpesa();
		if (null != elencoModificheMovimentoGestioneSpesaImp && elencoModificheMovimentoGestioneSpesaImp.size() > 0) {
			
			for (ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesaImp : elencoModificheMovimentoGestioneSpesaImp) {
				
				if (null != modificaMovimentoGestioneSpesaImp.getAttoAmmAnno() && null != modificaMovimentoGestioneSpesaImp.getAttoAmmNumero() && null != modificaMovimentoGestioneSpesaImp.getAttoAmmTipoAtto()) {
					// leggo l'idStruttura amministrativa, che puo' anche essere
					// nullo:
					Integer idStrutturaAmministrativa = modificaMovimentoGestioneSpesaImp.getIdStrutturaAmministrativa();
					
					AttoAmministrativo attoAmministrativoModificaImp = estraiAttoAmministrativoCaching(richiedente,modificaMovimentoGestioneSpesaImp.getAttoAmmAnno(),modificaMovimentoGestioneSpesaImp.getAttoAmmNumero(),modificaMovimentoGestioneSpesaImp.getAttoAmmTipoAtto(),idStrutturaAmministrativa,cacheAttoAmm);
					
					if (null != attoAmministrativoModificaImp){
						modificaMovimentoGestioneSpesaImp.setAttoAmministrativo(attoAmministrativoModificaImp);
					}
						
				}
			}
		}

		List<SubImpegno> elencoSubImpegni = impegno.getElencoSubImpegni();
		if (null != elencoSubImpegni && elencoSubImpegni.size() > 0) {
			for (SubImpegno subImpegno : elencoSubImpegni) {
				
				if (null != subImpegno.getAttoAmmAnno() && null != subImpegno.getAttoAmmNumero() && null != subImpegno.getAttoAmmTipoAtto()) {
					
					// leggo l'idStruttura amministrativa, che puo' anche essere
					// nullo:
					Integer idStrutturaAmministrativa = subImpegno.getIdStrutturaAmministrativa();
					
					AttoAmministrativo attoAmministrativoSub = estraiAttoAmministrativoCaching(richiedente, subImpegno.getAttoAmmAnno(),subImpegno.getAttoAmmNumero(),subImpegno.getAttoAmmTipoAtto(),idStrutturaAmministrativa, cacheAttoAmm );
					
					if (null != attoAmministrativoSub){
						subImpegno.setAttoAmministrativo(attoAmministrativoSub);
					}
						
				}

				List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesaSubImp = subImpegno.getListaModificheMovimentoGestioneSpesa();
				
				if (null != elencoModificheMovimentoGestioneSpesaSubImp && elencoModificheMovimentoGestioneSpesaSubImp.size() > 0) {
					for (ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesaSubImp : elencoModificheMovimentoGestioneSpesaSubImp) {
						if (null != modificaMovimentoGestioneSpesaSubImp.getAttoAmmAnno() && null != modificaMovimentoGestioneSpesaSubImp.getAttoAmmNumero() && null != modificaMovimentoGestioneSpesaSubImp.getAttoAmmTipoAtto()) {
							
							// leggo l'idStruttura amministrativa, che puo'
							// anche essere nullo:
							Integer idStrutturaAmministrativa = modificaMovimentoGestioneSpesaSubImp.getIdStrutturaAmministrativa();
							
							AttoAmministrativo attoAmministrativoModificaSubImp = estraiAttoAmministrativoCaching(richiedente,modificaMovimentoGestioneSpesaSubImp.getAttoAmmAnno(),modificaMovimentoGestioneSpesaSubImp.getAttoAmmNumero(),modificaMovimentoGestioneSpesaSubImp.getAttoAmmTipoAtto(),idStrutturaAmministrativa,cacheAttoAmm);
							
							if (null != attoAmministrativoModificaSubImp){
								modificaMovimentoGestioneSpesaSubImp.setAttoAmministrativo(attoAmministrativoModificaSubImp);
							}

						}
					}
				}
			}
		}
		

		// completa dati per eventuali VINCOLI
		
		if (impegno.getVincoliImpegno() != null && !impegno.getVincoliImpegno().isEmpty()) {

			for (VincoloImpegno vincoloImpegno : impegno.getVincoliImpegno()) {
				// carico i capitoli dei vincoli
				
				if(vincoloImpegno.getAccertamento()!=null){
					
					CapitoloEntrataGestione capitoloEntrataGestione = caricaCapitoloEntrataGestione(richiedente, vincoloImpegno.getAccertamento().getChiaveCapitoloEntrataGestione(), true);
					
					// vincoloImpegno.getAccertamento().setCapitoloEntrataGestione(capitoloEntrataGestione);

					Accertamento acc = (Accertamento) accertamentoOttimizzatoDad.ricercaMovimentoPk(
							richiedente,capitoloEntrataGestione.getEnte(),annoEsercizio, vincoloImpegno.getAccertamento().getAnnoMovimento(), 
							vincoloImpegno.getAccertamento().getNumeroBigDecimal(),CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, false, false);
					
					vincoloImpegno.setAccertamento(acc);

					vincoloImpegno.getAccertamento().setCapitoloEntrataGestione(capitoloEntrataGestione);
					
				}

			}

		}
		

		// calcola disponibilita a VINCOLARE
		// SE impegno.stato = Annullato disponibilitaVincolare = 0
		// ALTRIMENTI disponibilitaVincolare = importoAttuale -
		// Somma(Vincoli.importo)

		DisponibilitaMovimentoGestioneContainer disponibilitaVincolare;

		if (!impegno.getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_ANNULLATO)) {
			BigDecimal sommaVincoli = BigDecimal.ZERO;
			if (impegno.getVincoliImpegno() != null) {
				for (VincoloImpegno vincolo : impegno.getVincoliImpegno()) {
					sommaVincoli = sommaVincoli.add(vincolo.getImporto());
				}
			}
			disponibilitaVincolare = new DisponibilitaMovimentoGestioneContainer(impegno.getImportoAttuale().subtract(sommaVincoli), "Disponibilita' calcolata come differenza tra importo attuale (" + impegno.getImportoAttuale()
					+ ") e totale dei vincoli (" + sommaVincoli + ")");
		} else {
			disponibilitaVincolare = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Se lo stato e' ANNULLATO, la disponibilita' deve essere ZERO");
		}
		impegno.setDisponibilitaVincolare(disponibilitaVincolare.getDisponibilita());
		impegno.setMotivazioneDisponibilitaVincolare(disponibilitaVincolare.getMotivazione());
		

		return impegno;
	}
	
	/**
	 * Wrapper di retrocompatibilita
	 * @param richiedente
	 * @param accertamento
	 * @return
	 */
	protected Accertamento completaDatiRicercaAccertamentoPk(Richiedente richiedente, Accertamento accertamento) {
		return completaDatiRicercaAccertamentoPk(richiedente, accertamento, null,null,null);
	}

	/**
	 * 
	 * @param richiedente
	 * @param accertamento
	 * @param datiOpzionaliCapitoli
	 * @return
	 */
	protected Accertamento completaDatiRicercaAccertamentoPk(Richiedente richiedente, Accertamento accertamento,
			DatiOpzionaliCapitoli datiOpzionaliCapitoli,HashMap<String, AttoAmministrativo> cacheAttoAmm,HashMap<Integer, CapitoloEntrataGestione> cacheCapitolo) {
		
		if(cacheCapitolo==null){
			//se il chiamante non fa uso della cache ne istanzio una per evitare null pointer
			cacheCapitolo = new HashMap<Integer, CapitoloEntrataGestione>();
		}
		
		if(cacheAttoAmm==null){
			//se il chiamante non fa uso della cache ne istanzio una per evitare null pointer
			cacheAttoAmm = new HashMap<String, AttoAmministrativo>();
		}
		
		CapitoloEntrataGestione capitoloEntrataGestione =  caricaCapitoloEntrataGestioneCaching(richiedente, accertamento.getChiaveCapitoloEntrataGestione(),true,datiOpzionaliCapitoli,cacheCapitolo);
		//CapitoloEntrataGestione capitoloEntrataGestione = caricaCapitoloEntrataGestione(richiedente, accertamento.getChiaveCapitoloEntrataGestione(),true,datiOpzionaliCapitoli);
		
		if (null != capitoloEntrataGestione){
			accertamento.setCapitoloEntrataGestione(capitoloEntrataGestione);
		}
			

		if (null != accertamento.getAttoAmmAnno() && null != accertamento.getAttoAmmNumero() && null != accertamento.getAttoAmmTipoAtto()) {
			// leggo l'idStruttura amministrativa, che puo' anche essere nullo:
			Integer idStrutturaAmministrativa = accertamento.getIdStrutturaAmministrativa();
			AttoAmministrativo attoAmministrativo = estraiAttoAmministrativoCaching(richiedente, accertamento.getAttoAmmAnno(),accertamento.getAttoAmmNumero(),accertamento.getAttoAmmTipoAtto(),idStrutturaAmministrativa,cacheAttoAmm);
			if (null != attoAmministrativo){
				accertamento.setAttoAmministrativo(attoAmministrativo);
			}
		}

		List<ModificaMovimentoGestioneEntrata> elencoModificheMovimentoGestioneEntrataAcc = accertamento.getListaModificheMovimentoGestioneEntrata();
		if (null != elencoModificheMovimentoGestioneEntrataAcc && elencoModificheMovimentoGestioneEntrataAcc.size() > 0) {
			for (ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrataAcc : elencoModificheMovimentoGestioneEntrataAcc) {
				if (null != modificaMovimentoGestioneEntrataAcc.getAttoAmmAnno() && null != modificaMovimentoGestioneEntrataAcc.getAttoAmmNumero() && null != modificaMovimentoGestioneEntrataAcc.getAttoAmmTipoAtto()) {
					// leggo l'idStruttura amministrativa, che puo' anche essere
					// nullo:
					Integer idStrutturaAmministrativa = modificaMovimentoGestioneEntrataAcc.getIdStrutturaAmministrativa();
					AttoAmministrativo attoAmministrativoModificaAcc = estraiAttoAmministrativoCaching(richiedente,modificaMovimentoGestioneEntrataAcc.getAttoAmmAnno(),modificaMovimentoGestioneEntrataAcc.getAttoAmmNumero(),modificaMovimentoGestioneEntrataAcc.getAttoAmmTipoAtto(),idStrutturaAmministrativa,cacheAttoAmm);
					if (null != attoAmministrativoModificaAcc){
						modificaMovimentoGestioneEntrataAcc.setAttoAmministrativo(attoAmministrativoModificaAcc);
					}
				}
			}
		}

		List<SubAccertamento> elencoSubAccertamenti = accertamento.getElencoSubAccertamenti();
		
		if (null != elencoSubAccertamenti && elencoSubAccertamenti.size() > 0) {
			
			for (SubAccertamento subAccertamento : elencoSubAccertamenti) {
				if (null != subAccertamento.getAttoAmmAnno() && null != subAccertamento.getAttoAmmNumero() && null != subAccertamento.getAttoAmmTipoAtto()) {
					// leggo l'idStruttura amministrativa, che puo' anche essere
					// nullo:
					Integer idStrutturaAmministrativa = subAccertamento.getIdStrutturaAmministrativa();
					AttoAmministrativo attoAmministrativoSub = estraiAttoAmministrativoCaching(richiedente, subAccertamento.getAttoAmmAnno(),
																						subAccertamento.getAttoAmmNumero(),
																						subAccertamento.getAttoAmmTipoAtto(),
																						idStrutturaAmministrativa,cacheAttoAmm);
					
					if (null != attoAmministrativoSub){
						subAccertamento.setAttoAmministrativo(attoAmministrativoSub);
					}
						
				}

				List<ModificaMovimentoGestioneEntrata> elencoModificheMovimentoGestioneEntrataSubAcc = subAccertamento.getListaModificheMovimentoGestioneEntrata();
				if (null != elencoModificheMovimentoGestioneEntrataSubAcc && elencoModificheMovimentoGestioneEntrataSubAcc.size() > 0) {
					for (ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrataSubAcc : elencoModificheMovimentoGestioneEntrataSubAcc) {
						if (null != modificaMovimentoGestioneEntrataSubAcc.getAttoAmmAnno() && null != modificaMovimentoGestioneEntrataSubAcc.getAttoAmmNumero() && null != modificaMovimentoGestioneEntrataSubAcc.getAttoAmmTipoAtto()) {
							// leggo l'idStruttura amministrativa, che puo'
							// anche essere nullo:
							Integer idStrutturaAmministrativa = modificaMovimentoGestioneEntrataSubAcc.getIdStrutturaAmministrativa();
							AttoAmministrativo attoAmministrativoModificaSubAcc = estraiAttoAmministrativoCaching(richiedente,modificaMovimentoGestioneEntrataSubAcc.getAttoAmmAnno(),modificaMovimentoGestioneEntrataSubAcc.getAttoAmmNumero(),modificaMovimentoGestioneEntrataSubAcc.getAttoAmmTipoAtto(),idStrutturaAmministrativa,cacheAttoAmm);
							if (null != attoAmministrativoModificaSubAcc){
								modificaMovimentoGestioneEntrataSubAcc.setAttoAmministrativo(attoAmministrativoModificaSubAcc);
							}
						}
					}
				}
			}
		}
		return accertamento;
	}

	/*
	 * protected CapitoloUscitaGestione
	 * estraiCapitoloUscitaGestioneMovimento(Richiedente richiedente, Impegno
	 * impegno){
	 * 
	 * CapitoloUscitaGestione capitoloUscitaGestione = new
	 * CapitoloUscitaGestione();
	 * 
	 * RicercaDettaglioCapitoloUGest ricercaDettaglioCapitoloUGest = new
	 * RicercaDettaglioCapitoloUGest(); if (impegno != null) {
	 * ricercaDettaglioCapitoloUGest
	 * .setChiaveCapitolo(impegno.getChiaveCapitoloUscitaGestione());
	 * 
	 * RicercaDettaglioCapitoloUscitaGestione
	 * ricercaDettaglioCapitoloUscitaGestione = new
	 * RicercaDettaglioCapitoloUscitaGestione();
	 * ricercaDettaglioCapitoloUscitaGestione
	 * .setRicercaDettaglioCapitoloUGest(ricercaDettaglioCapitoloUGest);
	 * ricercaDettaglioCapitoloUscitaGestione.setRichiedente(richiedente);
	 * ricercaDettaglioCapitoloUscitaGestione
	 * .setEnte(richiedente.getAccount().getEnte());
	 * 
	 * RicercaDettaglioCapitoloUscitaGestioneResponse
	 * ricercaDettaglioCapitoloUscitaGestioneResponse =
	 * capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(
	 * ricercaDettaglioCapitoloUscitaGestione);
	 * 
	 * capitoloUscitaGestione =
	 * ricercaDettaglioCapitoloUscitaGestioneResponse.getCapitoloUscita(); }
	 * return capitoloUscitaGestione; }
	 */

	/*
	 * protected CapitoloEntrataGestione
	 * estraiCapitoloEntrataGestioneMovimento(Richiedente richiedente,
	 * Accertamento accertamento){ CapitoloEntrataGestione
	 * capitoloEntrataGestione = new CapitoloEntrataGestione();
	 * 
	 * RicercaDettaglioCapitoloEGest ricercaDettaglioCapitoloEGest = new
	 * RicercaDettaglioCapitoloEGest();
	 * ricercaDettaglioCapitoloEGest.setChiaveCapitolo
	 * (accertamento.getChiaveCapitoloEntrataGestione());
	 * 
	 * RicercaDettaglioCapitoloEntrataGestione
	 * ricercaDettaglioCapitoloEntrataGestione = new
	 * RicercaDettaglioCapitoloEntrataGestione();
	 * ricercaDettaglioCapitoloEntrataGestione
	 * .setRicercaDettaglioCapitoloEGest(ricercaDettaglioCapitoloEGest);
	 * ricercaDettaglioCapitoloEntrataGestione.setRichiedente(richiedente);
	 * ricercaDettaglioCapitoloEntrataGestione
	 * .setEnte(richiedente.getAccount().getEnte());
	 * 
	 * RicercaDettaglioCapitoloEntrataGestioneResponse
	 * ricercaDettaglioCapitoloEntrataGestioneResponse =
	 * capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(
	 * ricercaDettaglioCapitoloEntrataGestione);
	 * 
	 * capitoloEntrataGestione =
	 * ricercaDettaglioCapitoloEntrataGestioneResponse.
	 * getCapitoloEntrataGestione();
	 * 
	 * return capitoloEntrataGestione; }
	 */

	/**
	 * Semplice wrapper del metodo princiale estraiAttoAmministrativo
	 * 
	 * @param richiedente
	 * @param atto
	 * @param idStrutturaAmministrativa
	 * @return
	 */
	protected AttoAmministrativo estraiAttoAmministrativo(
			Richiedente richiedente, AttoAmministrativo atto) {
		// Leggiamo l'EVENTUALE struttura amministativa:
		Integer idStrutturaAmministrativa = leggiIdStrutturaAmmCont(atto);
		// Chiamiamo il metodo core:
		return estraiAttoAmministrativo(richiedente,
				Integer.toString(atto.getAnno()), atto.getNumero(),
				atto.getTipoAtto(), idStrutturaAmministrativa);
	}

	/**
	 * Wrapper del servizio ricerca provvedimento
	 * 
	 * @param richiedente
	 * @param atto
	 * @return
	 */
	protected RicercaProvvedimentoResponse ricercaProvvedimento(
			Richiedente richiedente, AttoAmministrativo atto) {
		// Leggiamo l'EVENTUALE struttura amministativa:
		Integer idStrutturaAmministrativa = leggiIdStrutturaAmmCont(atto);
		// Chiamiamo il metodo core:
		return ricercaProvvedimento(richiedente,
				Integer.toString(atto.getAnno()), atto.getNumero(),
				atto.getTipoAtto(), idStrutturaAmministrativa);
	}
	
	protected RicercaProvvedimentoResponse ricercaProvvedimentoConUid(
			Richiedente richiedente, AttoAmministrativo atto) {
		// Leggiamo l'EVENTUALE struttura amministativa:
		Integer idStrutturaAmministrativa = leggiIdStrutturaAmmCont(atto);
		// Chiamiamo il metodo core:
		return ricercaProvvedimento(richiedente,
				Integer.toString(atto.getAnno()), atto.getNumero(),
				atto.getTipoAtto(), idStrutturaAmministrativa,atto.getUid());
	}

	/**
	 * estrare l'EVENTUALE struttura amministrativa dato che la struttura puo'
	 * non esserci ritorna null se non la trova
	 * 
	 * @param atto
	 * @return
	 */
	private Integer leggiIdStrutturaAmmCont(AttoAmministrativo atto) {
		Integer idStrutturaAmministrativa = null;
		// Leggiamo l'EVENTUALE struttura amministativa:
		if (atto.getStrutturaAmmContabile() != null
				&& atto.getStrutturaAmmContabile().getUid() > 0) {
			idStrutturaAmministrativa = atto.getStrutturaAmmContabile()
					.getUid();
		}
		return idStrutturaAmministrativa;
	}

	/**
	 * Metodo principale per l'estrazione dell'atto amministrativo . Ritorna
	 * null se non esiste.
	 * 
	 * @param richiedente
	 * @param annoAttoAmministrativo
	 * @param numeroAttoAmministrativo
	 * @param tipoAtto
	 * @param idStrutturaAmministrativa
	 * @return
	 */
	protected AttoAmministrativo estraiAttoAmministrativo(
			Richiedente richiedente, String annoAttoAmministrativo,
			Integer numeroAttoAmministrativo, TipoAtto tipoAtto,
			Integer idStrutturaAmministrativa) {
		AttoAmministrativo attoAmministrativo = null;
		RicercaProvvedimentoResponse ricercaProvvedimentoResponse = ricercaProvvedimento(
				richiedente, annoAttoAmministrativo, numeroAttoAmministrativo,
				tipoAtto, idStrutturaAmministrativa);
		List<AttoAmministrativo> listaAttoAmministrativo = ricercaProvvedimentoResponse.getListaAttiAmministrativi();
		if (null != listaAttoAmministrativo && listaAttoAmministrativo.size() > 0) {
			for(AttoAmministrativo it : listaAttoAmministrativo){
				if(it!=null){
					if( (idStrutturaAmministrativa==null || idStrutturaAmministrativa.intValue()==0)
							&& (it.getStrutturaAmmContabile()==null || it.getStrutturaAmmContabile().getUid()==0) ){
						//Quello senza SAC:
						attoAmministrativo = it;
						break;
					} else if( idStrutturaAmministrativa!=null && idStrutturaAmministrativa.intValue()>0
							&& it.getStrutturaAmmContabile()!=null && it.getStrutturaAmmContabile().getUid()>0 ){
						//Quello con SAC:
						attoAmministrativo = it;
						break;
					}
				}
			}
		}
		return attoAmministrativo;
	}
	
	protected RicercaProvvedimentoResponse ricercaProvvedimento(
			Richiedente richiedente, String annoAttoAmministrativo,
			Integer numeroAttoAmministrativo, TipoAtto tipoAtto,
			Integer idStrutturaAmministrativa) {
		return ricercaProvvedimento(richiedente, annoAttoAmministrativo, numeroAttoAmministrativo, tipoAtto, idStrutturaAmministrativa, null);
	}

	/**
	 * Wrapper del servizio ricerca provvedimento
	 * 
	 * @param richiedente
	 * @param annoAttoAmministrativo
	 * @param numeroAttoAmministrativo
	 * @param tipoAtto
	 * @param idStrutturaAmministrativa
	 * @return
	 */
	protected RicercaProvvedimentoResponse ricercaProvvedimento(
			Richiedente richiedente, String annoAttoAmministrativo,
			Integer numeroAttoAmministrativo, TipoAtto tipoAtto,
			Integer idStrutturaAmministrativa, Integer uidProvvedimento) {

		RicercaAtti ricercaAtti = new RicercaAtti();
		
		if(uidProvvedimento!=null && uidProvvedimento.intValue()>0){
			ricercaAtti.setUid(uidProvvedimento);
		} else {
			ricercaAtti.setAnnoAtto(Integer.parseInt(annoAttoAmministrativo));
			ricercaAtti.setNumeroAtto(numeroAttoAmministrativo);
			ricercaAtti.setTipoAtto(tipoAtto);

			// dati struttura amministrativa:
			if (idStrutturaAmministrativa != null) {
				StrutturaAmministrativoContabile strutturaAmm = new StrutturaAmministrativoContabile();
				strutturaAmm.setUid(idStrutturaAmministrativa);
				ricercaAtti.setStrutturaAmministrativoContabile(strutturaAmm);
			}
		}
		
		

		RicercaProvvedimento ricercaProvvedimento = new RicercaProvvedimento();
		ricercaProvvedimento.setEnte(richiedente.getAccount().getEnte());
		ricercaProvvedimento.setRichiedente(richiedente);
		ricercaProvvedimento.setRicercaAtti(ricercaAtti);

		RicercaProvvedimentoResponse ricercaProvvedimentoResponse = provvedimentoService.ricercaProvvedimento(ricercaProvvedimento);
		
		//APRILE 2017 - SE PIU DI UNO SI CONSIDERA QUELLO IN STATO NON ANNULLATO:
		if(ricercaProvvedimentoResponse!=null && ricercaProvvedimentoResponse.getListaAttiAmministrativi()!=null
				&& ricercaProvvedimentoResponse.getListaAttiAmministrativi().size()>1){
			
			List<AttoAmministrativo> lista = ricercaProvvedimentoResponse.getListaAttiAmministrativi();
			List<AttoAmministrativo> listaRicostruita = new ArrayList<AttoAmministrativo>();
			
			for(AttoAmministrativo it: lista){
				if(it!=null && it.getStatoOperativo()!=null && !CostantiFin.ATTO_AMM_STATO_ANNULLATO.equals(it.getStatoOperativo())){
					listaRicostruita.add(it);
				}
			}
			ricercaProvvedimentoResponse.setListaAttiAmministrativi(listaRicostruita);
		}

		return ricercaProvvedimentoResponse;
	}

	/**
	 * Wrapper del servizio ricerca provvedimento
	 * 
	 * @param richiedente
	 * @param ricercaAtti
	 * @return
	 */
	protected RicercaProvvedimentoResponse ricercaProvvedimento(
			Richiedente richiedente, RicercaAtti ricercaAtti) {
		Integer idStrutturaAmministrativa = null;
		if (ricercaAtti.getStrutturaAmministrativoContabile() != null) {
			idStrutturaAmministrativa = ricercaAtti
					.getStrutturaAmministrativoContabile().getUid();
		}
		String annoAttoAmministrativo = ricercaAtti.getAnnoAtto().toString();
		Integer numeroAttoAmministrativo = ricercaAtti.getNumeroAtto();
		TipoAtto tipoAtto = ricercaAtti.getTipoAtto();
		RicercaProvvedimentoResponse ricercaProvvedimentoResponse = ricercaProvvedimento(
				richiedente, annoAttoAmministrativo, numeroAttoAmministrativo,
				tipoAtto, idStrutturaAmministrativa);
		return ricercaProvvedimentoResponse;
	}

	/**
	 * Costruisce la chiave di ricerca per la cache
	 * 
	 * @param annoAttoAmministrativo
	 * @param numeroAttoAmministrativo
	 * @param tipoAtto
	 * @param idStrutturaAmministrativa
	 * @return
	 */
	protected String buildAttoAmministrativoKeyForCache(
			String annoAttoAmministrativo, Integer numeroAttoAmministrativo,
			TipoAtto tipoAtto, Integer idStrutturaAmministrativa) {
		String key = annoAttoAmministrativo + "||" + numeroAttoAmministrativo
				+ "||" + tipoAtto.getUid();
		if (idStrutturaAmministrativa != null) {
			key = key + idStrutturaAmministrativa.intValue();
		}
		return key;
	}

	/**
	 * Versione con cache dell'omonimo metodo
	 * 
	 * @param richiedente
	 * @param annoAttoAmministrativo
	 * @param numeroAttoAmministrativo
	 * @param tipoAtto
	 * @param idStrutturaAmministrativa
	 * @param cacheAttoAmm
	 * @return
	 */
	protected AttoAmministrativo estraiAttoAmministrativoCaching(
			Richiedente richiedente, String annoAttoAmministrativo,
			Integer numeroAttoAmministrativo, TipoAtto tipoAtto,
			Integer idStrutturaAmministrativa,
			HashMap<String, AttoAmministrativo> cacheAttoAmm) {

		AttoAmministrativo attoAmministrativo = null;

		String key = buildAttoAmministrativoKeyForCache(annoAttoAmministrativo,
				numeroAttoAmministrativo, tipoAtto, idStrutturaAmministrativa);

//		attoAmministrativo = cacheAttoAmm.get(key);
//
//		if (attoAmministrativo == null) {
//			attoAmministrativo = estraiAttoAmministrativo(richiedente,
//					annoAttoAmministrativo, numeroAttoAmministrativo, tipoAtto,
//					idStrutturaAmministrativa);
//			cacheAttoAmm.put(key, attoAmministrativo);
//		}
		
		if(cacheAttoAmm.containsKey(key)){
			return clone(cacheAttoAmm.get(key));
		} else {
			attoAmministrativo = estraiAttoAmministrativo(richiedente,
					annoAttoAmministrativo, numeroAttoAmministrativo, tipoAtto,
					idStrutturaAmministrativa);
			cacheAttoAmm.put(key, clone(attoAmministrativo));
		}

		return attoAmministrativo;
	}

	/**
	 * Ricarica l'atto amministrativo passando dal servizio, versione con cache
	 * 
	 * @param richiedente
	 * @param atto
	 * @param cacheAttoAmm
	 * @return
	 */
	protected AttoAmministrativo estraiAttoAmministrativoCaching(
			Richiedente richiedente, AttoAmministrativo atto,
			HashMap<String, AttoAmministrativo> cacheAttoAmm) {
		// Leggiamo l'EVENTUALE struttura amministativa:
		Integer idStrutturaAmministrativa = leggiIdStrutturaAmmCont(atto);
		// Chiamiamo il metodo core:
		return estraiAttoAmministrativoCaching(richiedente,
				Integer.toString(atto.getAnno()), atto.getNumero(),
				atto.getTipoAtto(), idStrutturaAmministrativa, cacheAttoAmm);
	}
	
	
	/**
	 * 	
	 * Controlla lo stato dell'AttoAmministrativo: 
	 * 
	 * deve esistere e non deve essere ANNULLATO 
	 *  
	 * @return  List<Errore>
	 */
	protected List<Errore> controlloAttoAmministrativo(Richiedente richiedente, AttoAmministrativo atto){
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		// Stato Atto: L'atto amministrativo da associare alla modifica deve esistere e non deve essere ANNULLATO 
		AttoAmministrativo attoRilettoDaDb = estraiAttoAmministrativo(richiedente, atto);
		if(attoRilettoDaDb==null){
			listaErrori.add(ErroreCore.ENTITA_NON_TROVATA.getErrore("Provvedimento","anno/numero: " + atto.getAnno()+"/"+ atto.getNumero()));
			return listaErrori;
		}
		
		if(attoRilettoDaDb.getStatoOperativo().equalsIgnoreCase(CostantiFin.ATTO_AMM_STATO_ANNULLATO)){
			listaErrori.add(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Modifica Movimento","Definitivo"));
			return listaErrori;
		}
		
		return listaErrori;
	}

	/**
	 * Wrapper (con cache) specifico per i movimenti gestione, assume che i dati
	 * NON siano in MovimentoGestione.getAttoAmministrativo ma nei campi
	 * singoli: mvg.getAttoAmmAnno() mvg.getAttoAmmNumero()
	 * mvg.getAttoAmmTipoAtto() mvg.getIdStrutturaAmministrativa()
	 * 
	 * @param richiedente
	 * @param mvg
	 * @param cacheAttoAmm
	 * @return
	 */
	protected <MVG extends MovimentoGestione> AttoAmministrativo estraiAttoAmministrativoMvgCaching(
			Richiedente richiedente, MVG mvg,
			HashMap<String, AttoAmministrativo> cacheAttoAmm) {
		Integer idStrutturaAmministrativa = mvg.getIdStrutturaAmministrativa();
		AttoAmministrativo attoAmministrativo = estraiAttoAmministrativoCaching(
				req.getRichiedente(), mvg.getAttoAmmAnno().toString(),
				mvg.getAttoAmmNumero(), mvg.getAttoAmmTipoAtto(),
				idStrutturaAmministrativa, cacheAttoAmm);
		return attoAmministrativo;
	}

	/**
	 * Legge l'eventuale idStrutturaAmministrativa
	 * 
	 * @param attoAmm
	 * @return
	 */
	protected Integer leggiIdStrutturaAmministrativa(AttoAmministrativo attoAmm) {
		Integer idStrutturaAmministrativa = null;
		if (attoAmm != null && attoAmm.getStrutturaAmmContabile() != null
				&& attoAmm.getUid() > 0) {
			idStrutturaAmministrativa = attoAmm.getStrutturaAmmContabile()
					.getUid();
		}
		return idStrutturaAmministrativa;
	}

	/**
	 * Metodo che permette di effettuare la copia di un oggetto, cambiando
	 * l'allocazione di memoria dell'oggetto copiato
	 * 
	 * @param o
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T o) {
		return (T) SerializationUtils.deserialize(SerializationUtils
				.serialize(o));
	}

	/**
	 * metodo comune ai service che viene utilizzato per il caricamento del
	 * capitolo uscita da ricerca sintetica
	 * 
	 * @param richiedente
	 * @param ente
	 * @param impegno
	 * @param bilancio
	 * @return
	 */
	protected ListaPaginata<CapitoloUscitaGestione> getRicercaSinteticaCapitoloUscita(
			Richiedente richiedente, Ente ente, Impegno impegno,
			Bilancio bilancio) {

		RicercaSinteticaCapitoloUscitaGestione rcug = new RicercaSinteticaCapitoloUscitaGestione();
		rcug.setEnte(ente);
		rcug.setRichiedente(richiedente);
		rcug.setParametriPaginazione(new ParametriPaginazione());
		RicercaSinteticaCapitoloUGest uGest = new RicercaSinteticaCapitoloUGest();

		uGest.setAnnoCapitolo(impegno.getCapitoloUscitaGestione()
				.getAnnoCapitolo());
		uGest.setNumeroCapitolo(impegno.getCapitoloUscitaGestione()
				.getNumeroCapitolo());
		uGest.setNumeroArticolo(impegno.getCapitoloUscitaGestione()
				.getNumeroArticolo());
		uGest.setNumeroUEB(impegno.getCapitoloUscitaGestione().getNumeroUEB());
		uGest.setAnnoEsercizio(bilancio.getAnno());

		// setto il parametro principale di ricerca
		rcug.setRicercaSinteticaCapitoloUGest(uGest);

		RicercaSinteticaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService
				.ricercaSinteticaCapitoloUscitaGestione(rcug);

		if (null == response || response.isFallimento()) {
			// errore in ricerca sintentica
			return null;
		}

		return response.getCapitoli();

	}

	/**
	 * metodo comune ai service che viene utilizzato per il caricamento del
	 * capitolo entrata da ricerca sintetica
	 * 
	 * @param richiedente
	 * @param ente
	 * @param accertamento
	 * @param bilancio
	 * @return
	 */
	protected ListaPaginata<CapitoloEntrataGestione> getRicercaSinteticaCapitoloEntrata(
			Richiedente richiedente, Ente ente, Accertamento accertamento,
			Bilancio bilancio) {
		// Costrusico la request per il servizio:
		RicercaSinteticaCapitoloEntrataGestione rceg = buildRequestPerRicercaSinteticaCapitolo(
				ente, richiedente, bilancio,
				accertamento.getCapitoloEntrataGestione());
		// Invoco il servizio:
		RicercaSinteticaCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService
				.ricercaSinteticaCapitoloEntrataGestione(rceg);
		// Analizzo la responce:
		if (null == response || response.isFallimento()) {
			// errore in ricerca sintentica
			return null;
		}
		// Ritorno della responce:
		return response.getCapitoli();

	}

	/**
	 * Metodo di comodo per costruire una requeste per il servizio di ricerca
	 * sintetica capitolo entrata gestione
	 * 
	 * @param ente
	 * @param richiedente
	 * @param bilancio
	 * @param capitoloEntrataGestione
	 * @return
	 */
	protected RicercaSinteticaCapitoloEntrataGestione buildRequestPerRicercaSinteticaCapitolo(
			Ente ente, Richiedente richiedente, Bilancio bilancio,
			CapitoloEntrataGestione capitoloEntrataGestione) {
		// Invoco il metodo "core" di costruzione della request:
		RicercaSinteticaCapitoloEntrataGestione rceg = buildRequestPerRicercaSinteticaCapitoloEG(
				ente, richiedente, bilancio.getAnno(),
				capitoloEntrataGestione.getAnnoCapitolo(),
				capitoloEntrataGestione.getNumeroCapitolo(),
				capitoloEntrataGestione.getNumeroArticolo(),
				capitoloEntrataGestione.getNumeroUEB());
		// Ritorno la request costruita:
		return rceg;
	}

	/**
	 * Metodo di comodo per costruire una requeste per il servizio di ricerca
	 * sintetica capitolo entrata gestione
	 * 
	 * @param ente
	 * @param richiedente
	 * @param chiaveLogica
	 * @return
	 */
	protected RicercaSinteticaCapitoloEntrataGestione buildRequestPerRicercaSinteticaCapitoloEG(
			Ente ente, Richiedente richiedente,
			ChiaveLogicaCapitoloDto chiaveLogica,
			DatiOpzionaliCapitoli datiOpzionaliCapitoli) {
		// Invoco il metodo "core" di costruzione della request:
		RicercaSinteticaCapitoloEntrataGestione rceg = buildRequestPerRicercaSinteticaCapitoloEG(
				ente, richiedente, chiaveLogica.getAnnoCapitolo(),
				chiaveLogica.getAnnoCapitolo(),
				chiaveLogica.getNumeroCapitolo(),
				chiaveLogica.getNumeroArticolo(), chiaveLogica.getNumeroUeb());
		
		if(datiOpzionaliCapitoli!=null){
			rceg.setTipologieClassificatoriRichiesti(datiOpzionaliCapitoli.getTipologieClassificatoriRichiesti());
			rceg.setImportiDerivatiRichiesti(datiOpzionaliCapitoli.getImportiDerivatiRichiesti());
		}
		
		// Ritorno la request costruita:
		return rceg;
	}

	/**
	 * Metodo di comodo per costruire una requeste per il servizio di ricerca
	 * sintetica capitolo uscita gestione
	 * 
	 * @param ente
	 * @param richiedente
	 * @param chiaveLogica
	 * @return
	 */
	protected RicercaSinteticaCapitoloUscitaGestione buildRequestPerRicercaSinteticaCapitoloUG(
			Ente ente, Richiedente richiedente,
			ChiaveLogicaCapitoloDto chiaveLogica, DatiOpzionaliCapitoli datiOpzionaliCapitoli) {
		// Invoco il metodo "core" di costruzione della request:
		RicercaSinteticaCapitoloUscitaGestione rceg = buildRequestPerRicercaSinteticaCapitoloUG(
				ente, richiedente, chiaveLogica.getAnnoCapitolo(),
				chiaveLogica.getAnnoCapitolo(),
				chiaveLogica.getNumeroCapitolo(),
				chiaveLogica.getNumeroArticolo(), chiaveLogica.getNumeroUeb(),datiOpzionaliCapitoli);
		// Ritorno la request costruita:
		return rceg;
	}

	/**
	 * Metodo che si occupa di istanziare e valorizzare una request
	 * RicercaSinteticaCapitoloEntrataGestione
	 * 
	 * @param ente
	 * @param richiedente
	 * @param annoBilancio
	 * @param annoCapitolo
	 * @param numeroCapitolo
	 * @param numeroArticolo
	 * @param numeroUeb
	 * @return
	 */
	protected RicercaSinteticaCapitoloEntrataGestione buildRequestPerRicercaSinteticaCapitoloEG(
			Ente ente, Richiedente richiedente, Integer annoBilancio,
			Integer annoCapitolo, Integer numeroCapitolo,
			Integer numeroArticolo, Integer numeroUeb) {
		RicercaSinteticaCapitoloEntrataGestione rceg = new RicercaSinteticaCapitoloEntrataGestione();
		rceg.setEnte(ente);
		rceg.setRichiedente(richiedente);
		rceg.setParametriPaginazione(new ParametriPaginazione());
		RicercaSinteticaCapitoloEGest eGest = new RicercaSinteticaCapitoloEGest();

		eGest.setAnnoCapitolo(annoCapitolo);
		eGest.setNumeroCapitolo(numeroCapitolo);
		eGest.setNumeroArticolo(numeroArticolo);
		eGest.setNumeroUEB(numeroUeb);
		eGest.setAnnoEsercizio(annoBilancio);

		// setto il parametro principale di ricerca
		rceg.setRicercaSinteticaCapitoloEntrata(eGest);

		return rceg;
	}

	/**
	 * Metodo che si occupa di istanziare e valorizzare una request
	 * RicercaSinteticaCapitoloEntrataGestione
	 * 
	 * @param ente
	 * @param richiedente
	 * @param annoBilancio
	 * @param annoCapitolo
	 * @param numeroCapitolo
	 * @param numeroArticolo
	 * @param numeroUeb
	 * @return
	 */
	protected RicercaSinteticaCapitoloUscitaGestione buildRequestPerRicercaSinteticaCapitoloUG(
			Ente ente, Richiedente richiedente, Integer annoBilancio,
			Integer annoCapitolo, Integer numeroCapitolo,
			Integer numeroArticolo, Integer numeroUeb,DatiOpzionaliCapitoli datiOpzionaliCapitoli) {
		RicercaSinteticaCapitoloUscitaGestione rceg = new RicercaSinteticaCapitoloUscitaGestione();
		rceg.setEnte(ente);
		rceg.setRichiedente(richiedente);
		rceg.setParametriPaginazione(new ParametriPaginazione());
		RicercaSinteticaCapitoloUGest eGest = new RicercaSinteticaCapitoloUGest();

		eGest.setAnnoCapitolo(annoCapitolo);
		eGest.setNumeroCapitolo(numeroCapitolo);
		eGest.setNumeroArticolo(numeroArticolo);
		eGest.setNumeroUEB(numeroUeb);
		eGest.setAnnoEsercizio(annoBilancio);
	
		if(datiOpzionaliCapitoli!=null){
			rceg.setTipologieClassificatoriRichiesti(datiOpzionaliCapitoli.getTipologieClassificatoriRichiesti());
			rceg.setImportiDerivatiRichiesti(datiOpzionaliCapitoli.getImportiDerivatiRichiesti());
		}

		// setto il parametro principale di ricerca
		rceg.setRicercaSinteticaCapitoloUGest(eGest);

		return rceg;
	}
	
	//SIAC-8894: verifico se esiste il progetto nel bilancio successivo per doppia gestione, se non esiste errore
	protected boolean esisteProgettoAnnoSuccPerDoppiaGestione(
				Bilancio bilancio, DatiOperazioneDto datiOperazione,
				Impegno impegnoDaAggiornare, Accertamento accertamentoDaAggiornare) {

			boolean retval = false;
			
			Ente ente = req.getRichiedente().getAccount().getEnte();
			Bilancio bilancioAnnoSuccessivo = commonDad.buildBilancioAnnoSuccessivo(bilancio, datiOperazione);

			Progetto progAnnoCorrente = null != impegnoDaAggiornare ?(Progetto) impegnoDaAggiornare.getProgetto():(Progetto) accertamentoDaAggiornare.getProgetto();
			Progetto progettoAnnoSucc = impegnoOttimizzatoDad.verificaProgrammaAnnoSuccessivo(progAnnoCorrente, bilancioAnnoSuccessivo, ente);
			if(null != progettoAnnoSucc) {
				return true;
			}	
			return retval;
	}

	//task-78: verifico se esiste il cronoprogramma nel bilancio successivo per doppia gestione, se non esiste errore
	protected boolean esisteCronoprogrammaAnnoSuccPerDoppiaGestione(
					Bilancio bilancio, DatiOperazioneDto datiOperazione,
					Impegno impegnoDaAggiornare, Accertamento accertamentoDaAggiornare) {

			boolean retval = false;
				
			Ente ente = req.getRichiedente().getAccount().getEnte();
			Bilancio bilancioAnnoSuccessivo = commonDad.buildBilancioAnnoSuccessivo(bilancio, datiOperazione);

			Integer cronopAnnoCorrente = null != impegnoDaAggiornare ? impegnoDaAggiornare.getIdCronoprogramma():0;
			Progetto progAnnoCorrente = null != impegnoDaAggiornare ?(Progetto) impegnoDaAggiornare.getProgetto():(Progetto) accertamentoDaAggiornare.getProgetto();
			Progetto progettoAnnoSucc = impegnoOttimizzatoDad.verificaProgrammaAnnoSuccessivo(progAnnoCorrente, bilancioAnnoSuccessivo, ente);
			
			Integer cronopAnnoSucc = impegnoOttimizzatoDad.verificaCronoprogrammaAnnoSuccessivo(cronopAnnoCorrente, progettoAnnoSucc.getUid(), bilancioAnnoSuccessivo, ente);
			if(null != cronopAnnoSucc) {
				return true;
			}	
			return retval;
	}

	
	protected ModificaVincoliImpegnoInfoDto caricaInfoAccVincoliPerDoppiaGest(
			Bilancio bilancio, DatiOperazioneDto datiOperazione,
			ImpegnoInModificaInfoDto impegnoInModificaInfoDto,
			Impegno impegnoDaAggiornare) {
		ModificaVincoliImpegnoInfoDto infoVincoliValutati = null;
		Ente ente = req.getRichiedente().getAccount().getEnte();
		Richiedente richiedente = req.getRichiedente();
		//
		Bilancio bilancioAnnoSuccessivo = commonDad
				.buildBilancioAnnoSuccessivo(bilancio, datiOperazione);
		int anno = bilancio.getAnno();

		SiacTMovgestTsFin siacTMovgestTsImpegno = null;

		if (impegnoInModificaInfoDto != null) {
			// INVOCAZIONE DA AGGIORNA IMPEGNO SERVICE
			siacTMovgestTsImpegno = impegnoInModificaInfoDto
					.getSiacTMovgestTs();
			infoVincoliValutati = impegnoOttimizzatoDad.valutaVincoliInAggiornamento(
					((Impegno) impegnoDaAggiornare).getVincoliImpegno(),
					siacTMovgestTsImpegno, datiOperazione);
			infoVincoliValutati.setVincoliOld(clone(infoVincoliValutati
					.getVincoliOld()));
		} else {
			// INVOCAZIONE DA INSERIMENTO IMPEGNO SERVICE
			// in questo caso i vincoli sono tutti da inserire:
			infoVincoliValutati = new ModificaVincoliImpegnoInfoDto();
			infoVincoliValutati
					.setVincoliDaInserire(((Impegno) impegnoDaAggiornare)
							.getVincoliImpegno());
		}

		List<SiacRMovgestTsFin> vincoliDaAnnullare = infoVincoliValutati
				.getVincoliDaAnnullare();
		List<VincoloImpegno> vincoliDaInserire = infoVincoliValutati
				.getVincoliDaInserire();
		List<VincoloImpegno> vincoliDaAggiornare = infoVincoliValutati
				.getVincoliDaAggiornare();
		ArrayList<MovGestInfoDto> elencoInfoAccertamentiCoinvolti = new ArrayList<MovGestInfoDto>();
		if (vincoliDaAnnullare != null && vincoliDaAnnullare.size() > 0) {
			// VINCOLI ANNULLATI
			for (SiacRMovgestTsFin siacRMovgestTsDaAnnullare : vincoliDaAnnullare) {
				MovGestInfoDto movGestInfoDto = impegnoOttimizzatoDad
						.caricaInfoAccertamentoDelVincolo(datiOperazione,
								bilancio, bilancioAnnoSuccessivo, null,
								siacRMovgestTsDaAnnullare);
				elencoInfoAccertamentiCoinvolti.add(movGestInfoDto);
			}
		}
		if (vincoliDaInserire != null && vincoliDaInserire.size() > 0) {
			// VINCOLI INSERITI
			for (VincoloImpegno vincoloImpegno : vincoliDaInserire) {
				MovGestInfoDto movGestInfoDto = impegnoOttimizzatoDad
						.caricaInfoAccertamentoDelVincolo(datiOperazione,
								bilancio, bilancioAnnoSuccessivo,
								vincoloImpegno, null);
				elencoInfoAccertamentiCoinvolti.add(movGestInfoDto);
			}
		}
		if (vincoliDaAggiornare != null && vincoliDaAggiornare.size() > 0) {
			// VINCOLI MODIFICATI
			for (VincoloImpegno vincoloImpegno : vincoliDaAggiornare) {
				MovGestInfoDto movGestInfoDto = impegnoOttimizzatoDad
						.caricaInfoAccertamentoDelVincolo(datiOperazione,
								bilancio, bilancioAnnoSuccessivo,
								vincoloImpegno, null);
				elencoInfoAccertamentiCoinvolti.add(movGestInfoDto);
			}
		}
		if (elencoInfoAccertamentiCoinvolti != null
				&& elencoInfoAccertamentiCoinvolti.size() > 0) {
			for (MovGestInfoDto accIt : elencoInfoAccertamentiCoinvolti) {
				//la condizione && accIt.getSiacTMovgest()!=null serve per escludere i vincoli di tipo Avanzo
				//i quali non hanno l'accertamento
				if (accIt.getSiacTMovgestTsResiduo() == null && accIt.getSiacTMovgest()!=null) {
					// NON ESITE l'accertamento residuo, le routine successive
					// di doppia gestione potrebbero doverlo creare da zero
					// quindi qui carichiamo i dati completi dell'accertamento
					// in modo da poterli usare per inserire un nuovo
					// accertamento in residuo tramite l'operazione interna di
					// inserimento:
					Accertamento accertamentoFresco = (Accertamento) accertamentoOttimizzatoDad
							.ricercaMovimentoPk(richiedente, ente, Integer
									.toString(anno), accIt.getSiacTMovgest()
									.getMovgestAnno(), accIt.getSiacTMovgest()
									.getMovgestNumero(),
									CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, true, false);
					if (accertamentoFresco != null) {
						accertamentoFresco = completaDatiRicercaAccertamentoPk(
								richiedente, accertamentoFresco);
						accIt.setMovGestCompleto(accertamentoFresco);
					}
				}
			}
		}
		infoVincoliValutati
				.setElencoInfoAccertamentiCoinvolti(elencoInfoAccertamentiCoinvolti);
		return infoVincoliValutati;
	}

	/**
	 * Per casi customizzati con non troppi elementi
	 * 
	 * @param listaAll
	 * @param numRisultatiPerPagina
	 * @param numeroPagina
	 * @return
	 */
	protected <T extends Entita> PaginazioneCustomDto paginazioneCustom(
			List<T> listaAll, int numRisultatiPerPagina, int numeroPagina) {
		PaginazioneCustomDto paginazioneCustomDto = new PaginazioneCustomDto();

		List<T> paginaRichiesta = null;

		int size = listaAll.size();

		// occorre restituire la pagina

		// per evitare errori di divisione:
		if (numRisultatiPerPagina == 0) {
			numRisultatiPerPagina = 10;// default arbitrario per evitare errori
										// di divisione
		}
		// valori inconsistenti di pagina ci portano alla pagina 1 come default:
		if (numeroPagina <= 0) {
			numeroPagina = 1;
		}

		int numeroPagineTot = size / numRisultatiPerPagina;
		if ((numeroPagineTot * numRisultatiPerPagina) < size) {
			numeroPagineTot++;
		}

		if (numeroPagina > numeroPagineTot) {
			// Forziamo ad uno
			numeroPagina = 1;
		}

		int startPage = numRisultatiPerPagina * (numeroPagina - 1);
		int endPage = startPage + numRisultatiPerPagina;

		if (startPage >= size) {
			// forziamo alla prima pagina
			startPage = 0;
			numeroPagina = 1;
		}
		if (endPage >= size) {
			endPage = size;
		}

		paginaRichiesta = new ArrayList<T>();
		for (int i = startPage; i < endPage; i++) {
			T daCopiare = listaAll.get(i);
			paginaRichiesta.add(daCopiare);
		}

		paginazioneCustomDto.setPaginaRichiesta(paginaRichiesta);
		paginazioneCustomDto.setPaginaCorrente(numeroPagina);
		paginazioneCustomDto.setTotaleElementi(size);
		paginazioneCustomDto.setTotalePagine(numeroPagineTot);

		return paginazioneCustomDto;
	}

	/**
	 * ##################################################################################################
	 * 
	 * OPERAZIONI COMUNI DI GESTIONE REGISTRO FIN - GEN 
	 */
	
	protected EsitoAttivaRegistrazioniMovFinFINGSADto gestisciRegistrazioneGENPerLiquidazioneInserita(Liquidazione liquidazioneInserita, Impegno impegnoRicevuto, Bilancio bilancio){
		return  gestisciRegistrazioneGENPerLiquidazioneInserita(liquidazioneInserita, impegnoRicevuto, bilancio, false);
	}
	
	protected EsitoAttivaRegistrazioniMovFinFINGSADto gestisciRegistrazioneGENPerLiquidazioneInserita(Liquidazione liquidazioneInserita, Impegno impegnoRicevuto, Bilancio bilancio, boolean saltaInserimentoPrimaNota){
		// Innesto registrazione fin -gen
		// SIAC-4066 (cr 297 - Gestione delle prima note a fronte di liquidazioni rilevate nell'esercizio le cui quote finanziarie sono su impegni residui)
		boolean registraPerImpegnoResiduo = impegnoRicevuto.getAnnoMovimento() < bilancio.getAnno();
		String codiceEvento =  registraPerImpegnoResiduo ? CodiceEventoEnum.INSERISCI_LIQUIDAZIONE_CON_IMPEGNO_RESIDUO.getCodice() : CodiceEventoEnum.INSERISCI_LIQUIDAZIONE.getCodice();
		
		return gestisciRegistrazioneGENPerLiquidazione(liquidazioneInserita, codiceEvento, saltaInserimentoPrimaNota);	
	}
	
	protected EsitoAttivaRegistrazioniMovFinFINGSADto gestisciRegistrazioneGENPerLiquidazione(Liquidazione liquidazione, String codiceEvento) {
		return gestisciRegistrazioneGENPerLiquidazione(liquidazione, codiceEvento, false);
		
	}
	
	protected EsitoAttivaRegistrazioniMovFinFINGSADto gestisciRegistrazioneGENPerLiquidazione(Liquidazione liquidazione, String codiceEvento, boolean saltaInserimentoPrimaNota) {
		String methodName = "gestisciRegistrazioneGENPerLiquidazione";
		
		if(!isCondizioneDiAttivazioneGENSoddisfatta(liquidazione)) {
			log.debug(methodName, "condizione di attivazione non soddisfatta. Non viene inserita/aggiornata la RegistrazioneMovFin.");
			EsitoAttivaRegistrazioniMovFinFINGSADto esitoAttivaRegistrazioniGENGSADto = new EsitoAttivaRegistrazioniMovFinFINGSADto();
			esitoAttivaRegistrazioniGENGSADto.setCondizioneAttivazioneRegistrazioniSoddisfatta(false);
			return esitoAttivaRegistrazioniGENGSADto;
		}

			
		return registraEInserisciPrimaNotaLiquidazione(liquidazione,TipoCollegamento.LIQUIDAZIONE, codiceEvento, saltaInserimentoPrimaNota);
	}
	
	protected void gestisciRegistrazioneGENPerImpegno(Impegno impegno, TipoCollegamento tipoCollegamento, Boolean aggiorna, String codiceEvento, Integer annoBilancioRequest) {
		gestisciRegistrazioneGENPerImpegno(impegno, tipoCollegamento, aggiorna, codiceEvento, annoBilancioRequest, false );
	}
	
	protected EsitoAttivaRegistrazioniMovFinFINGSADto gestisciRegistrazioneGENPerImpegno(Impegno impegno, TipoCollegamento tipoCollegamento, Boolean aggiorna, String codiceEvento, Integer annoBilancioRequest, boolean saltaInserimentoPrimaNota ) {
		String methodName = "gestisciRegistrazioneGENPerImpegno";
				
		if(!isCondizioneDiAttivazioneGENSoddisfatta(isImpegnoInPartitoDiGiro, impegno, null, aggiorna )) {
			EsitoAttivaRegistrazioniMovFinFINGSADto esitoAttivaRegistrazioniMovFinFINGSADto = new EsitoAttivaRegistrazioniMovFinFINGSADto();
			esitoAttivaRegistrazioniMovFinFINGSADto.setCondizioneAttivazioneRegistrazioniSoddisfatta(false);
			log.debug(methodName, "condizione di attivazione non soddisfatta. Non viene inserita/aggiornata la RegistrazioneMovFin.");
			return esitoAttivaRegistrazioniMovFinFINGSADto;
		}
		
		log.debug(methodName, "isImpegnoInPartitoDiGiro: "+isImpegnoInPartitoDiGiro);
		log.debug(methodName, "tipoCollegamento.Codice: "+tipoCollegamento.getCodice());
			
		
		if(isImpegnoInPartitoDiGiro && tipoCollegamento.getCodice().equals("I")) codiceEvento = CodiceEventoEnum.INSERISCI_IMPEGNO_PRG.getCodice();
		
		log.debug(methodName, "codiceEvento: "+ codiceEvento);
		
		return verificaRegistraGenInserisciPrimaNotaPerImpegno(impegno, tipoCollegamento, codiceEvento, impegno.isFlagAttivaGsa(), annoBilancioRequest, isImpegnoInPartitoDiGiro, saltaInserimentoPrimaNota);
		
	}
	
	/**
	 * Gestione SubImpegno
	 * @param impegno
	 * @param subImpegno
	 * @param tipoCollegamento
	 * @param aggiorna
	 * @param codiceEvento
	 * @param registraPerAnnoBilancioCorrente (SIAC-6249)
	 */
	protected EsitoAttivaRegistrazioniMovFinFINGSADto gestisciRegistrazioneGENPerSubImpegno(Impegno impegno, SubImpegno subImpegno, TipoCollegamento tipoCollegamento, Boolean aggiorna, String codiceEvento, Integer annoBilancioRequest, boolean saltaInserimentoPrimaNota, boolean registraPerAnnoBilancioCorrente) {
		String methodName = "gestisciRegistrazioneGENPerSubImpegno";
		Boolean isImpegnoInPartitoDiGiro = false;
		EsitoAttivaRegistrazioniMovFinFINGSADto esitoDto = new EsitoAttivaRegistrazioniMovFinFINGSADto();
		if(!isCondizioneDiAttivazioneGENSoddisfatta(isImpegnoInPartitoDiGiro, impegno, subImpegno, aggiorna )) {
			String chiaveImp = impegno != null && impegno.getNumeroBigDecimal() != null? ("" + impegno.getAnnoMovimento()  + impegno.getNumeroBigDecimal().toPlainString()) : " NESSUNA_CHIAVE";
			String chiaveSub = subImpegno != null && subImpegno.getNumeroBigDecimal() != null? impegno.getNumeroBigDecimal().toPlainString() : " NESSUNA_CHIAVE";
			log.debug(methodName, "condizione di attivazione su impegno " + chiaveImp + " " + chiaveSub + " non soddisfatta. Non viene inserita/aggiornata la RegistrazioneMovFin.");
			esitoDto.setAvviatoServizioInserimentoPromaNota(false);
			return esitoDto;
		}
		
		//if(isImpegnoInPartitoDiGiro && tipoCollegamento.getCodice().equals("I")) codiceEvento = CodiceEventoEnum.INSERISCI_IMPEGNO_PRG.getCodice();
		
		ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
		// Recupero dall'impegno il pdc da salvare nel registro
		// jira- 4502 Registrare con il pdc del sub e non dell'impegno!!!!
		pdc.setUid(subImpegno.getIdPdc());
		pdc.setCodice(subImpegno.getCodPdc());
		pdc.setDescrizione(subImpegno.getDescPdc());
		
		return verificaRegistraGenInserisciPrimaNotaSubImpegno(subImpegno, impegno, pdc, tipoCollegamento, codiceEvento, impegno.isFlagAttivaGsa(), annoBilancioRequest, isImpegnoInPartitoDiGiro, saltaInserimentoPrimaNota, registraPerAnnoBilancioCorrente);
		
	}
	
	/**
	 * Gestione SubAccertamento
	 * @param accertamento
	 * @param subAccertamento
	 * @param tipoCollegamento
	 * @param aggiorna
	 * @param codiceEvento
	 */
	protected EsitoAttivaRegistrazioniMovFinFINGSADto gestisciRegistrazioneGENPerSubAccertamento(Accertamento accertamento, SubAccertamento subAccertamento, TipoCollegamento tipoCollegamento, String codiceEvento, Integer annoBilancioRequest, boolean saltaInserimentoPrimaNota) {
		String methodName = "gestisciRegistrazioneGENPerSubAccertamento";
		EsitoAttivaRegistrazioniMovFinFINGSADto esitoDto = new EsitoAttivaRegistrazioniMovFinFINGSADto();
		
		Boolean flagAttivaGenStatoDefinitivo = subAccertamento.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_DEFINITIVO);
		
		//jira 
		if(!flagAttivaGenStatoDefinitivo || accertamento.isFlagFattura() || accertamento.isFlagCorrispettivo()){
			esitoDto.setCondizioneAttivazioneRegistrazioniSoddisfatta(false);
			log.debug(methodName, "condizione di attivazione non soddisfatta. Non viene inserita/aggiornata la RegistrazioneMovFin.");
			return esitoDto;
		}
		
		ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
		// Recupero dall'accertamento il pdc da salvare nel registro
		// jira- 4502 Registrare con il pdc del sub e non dell'accertamento!!!!
		pdc.setUid(subAccertamento.getIdPdc());
		pdc.setCodice(subAccertamento.getCodPdc());
		pdc.setDescrizione(subAccertamento.getDescPdc());
		
		return verificaRegistraGenInserisciPrimaNotaSubAccertamento(subAccertamento, pdc, tipoCollegamento, codiceEvento, accertamento.isFlagAttivaGsa(), annoBilancioRequest, saltaInserimentoPrimaNota);
		
		
		
	}
	
	protected void gestisciRegistrazioneGENPerAccertamento(Accertamento accertamento, TipoCollegamento tipoCollegamento, String codiceEvento, Integer annoBilancioRequest) {
		gestisciRegistrazioneGENPerAccertamento(accertamento, tipoCollegamento, codiceEvento, annoBilancioRequest, false);
	}
	/**
	 * 
	 * @param movimento
	 * @param tipoCollegamento
	 * @param codiceEvento
	 * 
	 */
	protected EsitoAttivaRegistrazioniMovFinFINGSADto gestisciRegistrazioneGENPerAccertamento(Accertamento accertamento, TipoCollegamento tipoCollegamento, String codiceEvento, Integer annoBilancioRequest, boolean saltaInserimentoPrimaNota) {
		String methodName = "gestisciRegistrazioneGENPerAccertamento";
		
		
		if(!isCondizioneDiAttivazioneGENSoddisfatta(accertamento)) {
			log.debug(methodName, "Condizioni per l'attivazione delle registrazioni contabili su accertamento non soddisfatte.");
			EsitoAttivaRegistrazioniMovFinFINGSADto esitoAttivaRegistrazioniGENGSADto = new EsitoAttivaRegistrazioniMovFinFINGSADto();
			esitoAttivaRegistrazioniGENGSADto.setCondizioneAttivazioneRegistrazioniSoddisfatta(false);
			return esitoAttivaRegistrazioniGENGSADto;
		}
		log.debug(methodName, "Condizioni per l'attivazione delle registrazioni contabili su accertamento soddisfatte. Procedo con l'attivazione.");
		return verificaRegistraGenInserisciPrimaNotaPerAccertamento(accertamento, tipoCollegamento, codiceEvento, accertamento.isFlagAttivaGsa(), annoBilancioRequest, saltaInserimentoPrimaNota);
		
	}
	
	
	/**
	 * Verifica e registra il movimento di tipo ORDINATIVO, inserendo poi la prima nota 
	 * @param ordinativo
	 * @param tipoCollegamento
	 */
	protected void gestisciRegistrazioneGENPerOrdinativo(Ordinativo ordinativo, MovimentoGestione movimentoGestione, TipoCollegamento tipoCollegamento, boolean aggiorna, boolean flagCassaEconomale, String codCapitoloSanitarioSpesa) {
		
		registrazioneGENServiceHelper.init(serviceExecutor, req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), loginOperazione, bilancio.getAnno());
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, ordinativo); //se presenti ne troverà una per ogni quota, altrimenti 0.

		boolean isOrdinativoPagamento = tipoCollegamento.getCodice().equals("OP");
		
		if(aggiorna){
			
			//definisciEventoOrdinativo(aggiorna, isOrdinativoPagamento);
			
			if(registrazioniMovFin!=null && !registrazioniMovFin.isEmpty()){
				
				eseguiRegistrazioneGENOrdinativo(ordinativo, movimentoGestione, tipoCollegamento, registrazioniMovFin, definisciEventoOrdinativo(aggiorna, isOrdinativoPagamento, flagCassaEconomale, movimentoGestione.isFlagAttivaGsa(), codCapitoloSanitarioSpesa));
			}
			
		}else eseguiRegistrazioneGENOrdinativo(ordinativo, movimentoGestione, tipoCollegamento, registrazioniMovFin, definisciEventoOrdinativo(aggiorna, isOrdinativoPagamento, flagCassaEconomale, movimentoGestione.isFlagAttivaGsa(), codCapitoloSanitarioSpesa));
	}
	
	
	/**
	 * 
	 * @param aggiorna
	 * @param isOrdinativoPagamento
	 * @return
	 */
	private String definisciEventoOrdinativo(boolean aggiorna, boolean isOrdinativoPagamento, boolean flagCassaEconomale, boolean flagGsa, String codiceCapitoloSanitarioSpesa) {
		String codiceEvento ="";
		
		boolean capitoloPerimetroSanitarioSan = (!StringUtils.isEmpty(codiceCapitoloSanitarioSpesa) && codiceCapitoloSanitarioSpesa.equalsIgnoreCase("4"))
				|| codiceCapitoloSanitarioSpesa.equalsIgnoreCase("2");
		
		
		if(isOrdinativoPagamento){
			
			if(aggiorna){
				
				if(!flagCassaEconomale){
					
					if(flagGsa && capitoloPerimetroSanitarioSan)
						codiceEvento = CodiceEventoEnum.AGGIORNA_ORDINATIVO_PAGAMENTO_SANITA.getCodice();
					else 
						codiceEvento = CodiceEventoEnum.AGGIORNA_ORDINATIVO_PAGAMENTO.getCodice();
											
				}
				else
					codiceEvento = CodiceEventoEnum.AGGIORNA_ORDINATIVO_PAGAMENTO_CEC.getCodice();
				
			}
			else{
			
				if(!flagCassaEconomale){
					
					if(flagGsa && capitoloPerimetroSanitarioSan)
						codiceEvento = CodiceEventoEnum.INSERISCI_ORDINATIVO_PAGAMENTO_SANITA.getCodice();
					else
						codiceEvento = CodiceEventoEnum.INSERISCI_ORDINATIVO_PAGAMENTO.getCodice();
					
				}
				else
					codiceEvento = CodiceEventoEnum.INSERISCI_ORDINATIVO_PAGAMENTO_CEC.getCodice();
				
			}
		}else{
			
			if(aggiorna){
				
				if(flagGsa && capitoloPerimetroSanitarioSan)
					codiceEvento = CodiceEventoEnum.AGGIORNA_ORDINATIVO_INCASSO_SANITA.getCodice();
				else 
					codiceEvento = CodiceEventoEnum.AGGIORNA_ORDINATIVO_INCASSO.getCodice();
				
			}else{
				
				if(flagGsa && capitoloPerimetroSanitarioSan)
					codiceEvento = CodiceEventoEnum.INSERISCI_ORDINATIVO_INCASSO_SANITA.getCodice();
				else	
					codiceEvento = CodiceEventoEnum.INSERISCI_ORDINATIVO_INCASSO.getCodice();
			}
				
		}
		
		return codiceEvento;
	}
	
	
	/**
	 * 
	 * @param aggiorna
	 * @param isOrdinativoPagamento
	 * @return
	 */
	private String definisciEventoOrdinativoGSA(String codiceEvento) {
		
		String codiceEventoGSA ="";
		
		if(codiceEvento.equals(CodiceEventoEnum.INSERISCI_ORDINATIVO_PAGAMENTO_SANITA.getCodice()))
			codiceEventoGSA = CodiceEventoEnum.INSERISCI_ORDINATIVO_PAGAMENTO.getCodice();
		else if(codiceEvento.equals(CodiceEventoEnum.AGGIORNA_ORDINATIVO_PAGAMENTO_SANITA.getCodice()))
			codiceEventoGSA = CodiceEventoEnum.AGGIORNA_ORDINATIVO_PAGAMENTO.getCodice();
		else if(codiceEvento.equals(CodiceEventoEnum.INSERISCI_ORDINATIVO_INCASSO_SANITA.getCodice()))
			codiceEventoGSA = CodiceEventoEnum.INSERISCI_ORDINATIVO_INCASSO.getCodice();
		else if(codiceEvento.equals(CodiceEventoEnum.AGGIORNA_ORDINATIVO_INCASSO_SANITA.getCodice()))
			codiceEventoGSA = CodiceEventoEnum.AGGIORNA_ORDINATIVO_INCASSO.getCodice();
		
		return codiceEventoGSA;
	}
	
	private void verificaRegistraGenInserisciPrimaNotaPerImpegno(Impegno movimento, TipoCollegamento tipoCollegamento, String codiceEvento, boolean flagGsa, Integer annoBilancioRequest, boolean isImpegnoInPartitoDiGiro) {
		verificaRegistraGenInserisciPrimaNotaPerImpegno(movimento, tipoCollegamento, codiceEvento, flagGsa, annoBilancioRequest, isImpegnoInPartitoDiGiro, false);
	}
	
	/**
	 * Movimento Gestione (o Impegno o Accertamento)
	 * @param impegno
	 * @param tipoCollegamento
	 * @return
	 */
	private EsitoAttivaRegistrazioniMovFinFINGSADto verificaRegistraGenInserisciPrimaNotaPerImpegno(Impegno impegno, TipoCollegamento tipoCollegamento, String codiceEvento, boolean flagGsa, Integer annoBilancioRequest, boolean isImpegnoInPartitoDiGiro, boolean saltaInserimentoPrimaNota) {
		
		final String methodName = "verificaRegistraGenInserisciPrimaNotaPerImpegno";
		
		EsitoAttivaRegistrazioniMovFinFINGSADto esitoAttivaRegistrazioniMovFinFINGSADto= new EsitoAttivaRegistrazioniMovFinFINGSADto();
		
		registrazioneGENServiceHelper.init(serviceExecutor, req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), loginOperazione, annoBilancioRequest);
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, impegno); //se presenti ne troverà una per ogni quota, altrimenti 0.
		
		registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esisteva devo annullare le eventuali primeNote associate
		
		Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
		
		ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
		// Recupero dall'impegno il pdc da salvare nel registro
		pdc.setUid(impegno.getIdPdc());
		pdc.setCodice(impegno.getCodPdc());
		pdc.setDescrizione(impegno.getDescPdc());
		
		
		if(CostantiFin.MOVGEST_STATO_DEFINITIVO.equals(impegno.getStatoOperativoMovimentoGestioneSpesa())) {
			RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, impegno, pdc, Ambito.AMBITO_FIN);
			esitoAttivaRegistrazioniMovFinFINGSADto.setRegistrazioneMovFinFINInserita(registrazioneMovFin);
			//SIAC-5333
			boolean isEnteAbilitatoASaltareInserimentoPrimaNota = isApplicabileSaltoInserimentoPrimaNota(saltaInserimentoPrimaNota);
			if(!isImpegnoInPartitoDiGiro && !isApplicabileSaltoInserimentoPrimaNota(saltaInserimentoPrimaNota)) {
				log.debug(methodName, "La prima nota per la registrazione uid[ " + (registrazioneMovFin != null? registrazioneMovFin.getUid() : "null") + "] verra' effettuata.");
				registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFin);
			}
		}
		
		
		//definitivo non liquidabile
		// jira 2659, innesto GSA, se l'impegno ha il flag 'Rilevante Co.Ge. GSA' registro e emetto prima nota in ambito GSA
		if(flagGsa){
			RegistrazioneMovFin registrazioneMovFinGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, impegno, pdc, Ambito.AMBITO_GSA);
			
			if(!isImpegnoInPartitoDiGiro)
				registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFinGSA);
		}
		
		return esitoAttivaRegistrazioniMovFinFINGSADto;
	}

	/**
	 * @param saltaInserimentoPrimaNota
	 * @param methodName
	 * @return
	 */
	private boolean isApplicabileSaltoInserimentoPrimaNota(boolean saltaInserimentoPrimaNota) {
		final String methodName = "controllaSeNecessarioSaltareInserimentoPrimaNota";
		boolean isEnteAbilitatoASaltareInserimentoPrimaNota ="TRUE".equals(ente.getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_PNOTA_DA_FIN));
		log.debug(methodName, "Devo saltare l'inserimento della prima nota automatica per l'ambito FIN?" + saltaInserimentoPrimaNota + ". L'utente e' abilitato a farlo? " + isEnteAbilitatoASaltareInserimentoPrimaNota);
		return isEnteAbilitatoASaltareInserimentoPrimaNota && saltaInserimentoPrimaNota;
	}
	
	private void verificaRegistraGenInserisciPrimaNotaPerAccertamento(MovimentoGestione movimento, TipoCollegamento tipoCollegamento, String codiceEvento, boolean flagGsa, Integer annoBilancioRequest) {
		verificaRegistraGenInserisciPrimaNotaPerAccertamento(movimento, tipoCollegamento, codiceEvento, flagGsa, annoBilancioRequest, false);
	}
	
	/**
	 * Movimento Gestione (o Impegno o Accertamento)
	 * @param movimento
	 * @param tipoCollegamento
	 * @return
	 */
	private EsitoAttivaRegistrazioniMovFinFINGSADto verificaRegistraGenInserisciPrimaNotaPerAccertamento(MovimentoGestione movimento, TipoCollegamento tipoCollegamento, String codiceEvento, boolean flagGsa, Integer annoBilancioRequest, boolean saltaInserimentoPrimaNota) {
		final String methodName = "verificaRegistraGenInserisciPrimaNotaPerAccertamento";
		EsitoAttivaRegistrazioniMovFinFINGSADto esitoAttivazioneRegistrazioni = new EsitoAttivaRegistrazioniMovFinFINGSADto();
		
		registrazioneGENServiceHelper.init(serviceExecutor, req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), loginOperazione, annoBilancioRequest);
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
		
		registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esisteva devo annullare le eventuali primeNote associate
		
		Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
		
		ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
		// Recupero dall'impegno il pdc da salvare nel registro
		pdc.setUid(movimento.getIdPdc());
		pdc.setCodice(movimento.getCodPdc());
		pdc.setDescrizione(movimento.getDescPdc());
		
		
		RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento, pdc, Ambito.AMBITO_FIN);
		esitoAttivazioneRegistrazioni.setRegistrazioneMovFinFINInserita(registrazioneMovFin);
		if( !isApplicabileSaltoInserimentoPrimaNota(saltaInserimentoPrimaNota)) {
			log.debug(methodName, "La prima nota per la registrazione uid[ " + (registrazioneMovFin != null? registrazioneMovFin.getUid() : "null") + "] verra' effettuata.");
			registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFin);
		}
		// jira 2659, innesto GSA, se l'impegno ha il flag 'Rilevante Co.Ge. GSA' registro e emetto prima nota in ambito GSA
		if(flagGsa){
			RegistrazioneMovFin registrazioneMovFinGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento, pdc, Ambito.AMBITO_GSA);
			registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFinGSA);
		}
		
		return esitoAttivazioneRegistrazioni;
	}
	
	
	/**
	 * Movimento Gestione (o SubImpegno o SubAccertamento)
	 * @param subImpegno
	 * @param tipoCollegamento
	 * @param registraPerAnnoBilancioCorrente 
	 * @return
	 */
	private EsitoAttivaRegistrazioniMovFinFINGSADto verificaRegistraGenInserisciPrimaNotaSubImpegno(SubImpegno subImpegno, Impegno impegno, ElementoPianoDeiConti pdc , TipoCollegamento tipoCollegamento, String codiceEvento, boolean flagGsa, Integer annoBilancioRequest, boolean isImpegnoPartitaDiGiro, boolean saltaInserimentoPrimaNota, boolean registraPerAnnoBilancioCorrente) {
		
		EsitoAttivaRegistrazioniMovFinFINGSADto esitoDto = new EsitoAttivaRegistrazioniMovFinFINGSADto();
		registrazioneGENServiceHelper.init(serviceExecutor, req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), loginOperazione, annoBilancioRequest);
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, subImpegno); //se presenti ne troverà una per ogni quota, altrimenti 0.
		
		// jira 3419, non devo piu annullare tutto:
		// quindi se il sub è già stato registrato non ripeto la registarzione annullando quella precedente 
		//registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esisteva devo annullare le eventuali primeNote associate
		if(registrazioniMovFin ==null  || registrazioniMovFin.isEmpty()){
			Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
			
			if(registraPerAnnoBilancioCorrente) {
				RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, subImpegno , pdc,  Ambito.AMBITO_FIN);
				esitoDto.setRegistrazioneMovFinFINInserita(registrazioneMovFin);
				if(!isImpegnoInPartitoDiGiro && !isApplicabileSaltoInserimentoPrimaNota(saltaInserimentoPrimaNota)) {
					registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFin);
				}
			}
			
			// jira 2659, innesto GSA, se l'impegno ha il flag 'Rilevante Co.Ge. GSA' registro e emetto prima nota in ambito GSA
			if(flagGsa){
				if(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(impegno.getStatoOperativoMovimentoGestioneSpesa())) {
					List<RegistrazioneMovFin> registrazioniMovFinImpegno = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.IMPEGNO, impegno, Ambito.AMBITO_GSA);
					registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFinImpegno);
				}
				RegistrazioneMovFin registrazioniMovFinInseriteGSA  =  registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, subImpegno , pdc,  Ambito.AMBITO_GSA);
				esitoDto.setRegistrazioneMovFinGSAInserita(registrazioniMovFinInseriteGSA);
				if(!isImpegnoInPartitoDiGiro) {
					registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInseriteGSA);
				}
			}
			
		}
		
		return esitoDto;
		
	}
	
	
	/**
	 * Movimento Gestione (o SubImpegno o SubAccertamento)
	 * @param movimento
	 * @param tipoCollegamento
	 * @return
	 */
	private EsitoAttivaRegistrazioniMovFinFINGSADto verificaRegistraGenInserisciPrimaNotaSubAccertamento(MovimentoGestione movimento, ElementoPianoDeiConti pdc , TipoCollegamento tipoCollegamento, String codiceEvento, boolean flagGsa, Integer annoBilancioRequest, boolean saltaInserimentoPrimaNota) {
		
		EsitoAttivaRegistrazioniMovFinFINGSADto esitoDto = new EsitoAttivaRegistrazioniMovFinFINGSADto();
		final String methodName = "verificaRegistraGenInserisciPrimaNotaSubAccertamento";
		registrazioneGENServiceHelper.init(serviceExecutor, req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), loginOperazione, annoBilancioRequest);
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
		
		// jira 3419, non devo piu annullare tutto:
		// quindi se il sub è già stato registrato non ripeto la registarzione annullando quella precedente 
		//registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esisteva devo annullare le eventuali primeNote associate
		if(registrazioniMovFin ==null  || registrazioniMovFin.isEmpty()){
			Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
			
			
			RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc,  Ambito.AMBITO_FIN);
			//SIAC-5943
			esitoDto.setRegistrazioneMovFinFINInserita(registrazioneMovFin);
			if( !isApplicabileSaltoInserimentoPrimaNota(saltaInserimentoPrimaNota)) {
				log.debug(methodName, "La prima nota per la registrazione uid[ " + (registrazioneMovFin != null? registrazioneMovFin.getUid() : "null") + "] non verra' effettuata.");
				registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFin);
			}
			// jira 2659, innesto GSA, se l'impegno ha il flag 'Rilevante Co.Ge. GSA' registro e emetto prima nota in ambito GSA
			if(flagGsa){
				RegistrazioneMovFin registrazioniMovFinInseriteGSA  =  registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc,  Ambito.AMBITO_GSA);
				registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInseriteGSA);
			}
		}
		return esitoDto;
	}
	
	/**
	 * 
	 * @param movimento (tipo Ordinativo)
	 * @param tipoCollegamento
	 * @param aggiorna
	 * @return
	 */
	private void eseguiRegistrazioneGENOrdinativo(Ordinativo ordinativo,
			MovimentoGestione movimentoGestione, 
			TipoCollegamento tipoCollegamento,
			List<RegistrazioneMovFin> registrazioniMovFin, String codiceEvento) {
		
		String methodName = "eseguiRegistrazioneGENOrdinativo";
		
		if(movimentoGestione != null && movimentoGestione.getIdPdc()!=null){
		
			registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esisteva devo annullare le eventuali primeNote associate
			
			// jira SIAC-4525, innesto GSA cambiano le causali
			Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
			
			// Per inserire le prime note anche per Gsa il servizio va richamato due volte, non ragiona sulla lista totale (verificato con Lisi)
			//List<RegistrazioneMovFin> registrazioniMovFinInserite = new ArrayList<RegistrazioneMovFin>();
			ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
		
			// Recupero dall'impegno il pdc da salvare nel registro
			pdc.setUid(movimentoGestione.getIdPdc());
			pdc.setCodice(movimentoGestione.getCodPdc());
			pdc.setDescrizione(movimentoGestione.getDescPdc());
		
			RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, ordinativo , pdc, Ambito.AMBITO_FIN);
			//registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFin);
			registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(toList(registrazioneMovFin), ordinativo, Ambito.AMBITO_FIN);
			//registrazioniMovFinInserite.add(registrazioneMovFin);
			
			// jira SIAC-2659, innesto GSA, se il movimento gestione ha il flag 'Rilevante Co.Ge. GSA' registro e emetto prima nota in ambito GSA
			// jira SIAC-4525, innesto GSA: se l'evento è OPA-SAN (INS/AGG) o OIN-SAN (INS/AGG) non bisogna inserire registrazione e prima nota in ambito GSA
			//System.out.println("isFlagAttivaGsa: "+ movimentoGestione.isFlagAttivaGsa());
			//System.out.println("evento.codice: "+ evento.getCodice());
			
			boolean isCodicePerimetrosanitario = evento.getCodice().equals(CodiceEventoEnum.INSERISCI_ORDINATIVO_PAGAMENTO_SANITA.getCodice()) ||
					evento.getCodice().equals(CodiceEventoEnum.AGGIORNA_ORDINATIVO_PAGAMENTO_SANITA.getCodice()) ||
					evento.getCodice().equals(CodiceEventoEnum.INSERISCI_ORDINATIVO_INCASSO_SANITA.getCodice())   ||
					evento.getCodice().equals(CodiceEventoEnum.AGGIORNA_ORDINATIVO_INCASSO_SANITA.getCodice());
			
			//System.out.println("isCodicePerimetrosanitario: "+ isCodicePerimetrosanitario);
			
			if(isCodicePerimetrosanitario){
				
				// se true, devo ridefinire il codice evento per GSA, es: per Fin = OPA-SAN-INS --> per Gsa = OPA-INS
				String codiceEventoGsa = definisciEventoOrdinativoGSA(evento.getCodice());
				
				//System.out.println("codiceEventoGsa: "+ codiceEventoGsa);
				
				evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEventoGsa);
				
				//System.out.println("evento.codice: "+ evento.getCodice());
				
			}
			
			if(movimentoGestione.isFlagAttivaGsa()){
				RegistrazioneMovFin registrazioniMovFinInseriteGSA  =  registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, ordinativo , pdc, Ambito.AMBITO_GSA);
				//registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInseriteGSA);
				registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(toList(registrazioniMovFinInseriteGSA), ordinativo, Ambito.AMBITO_GSA);
			}
						
		
		}else {
			log.info(methodName, "Impossibile procedere alla registrazione dell'ordinativo con uid [" + ordinativo.getUid()+" ] , verificare il pdc finanziario del movimentoGestione con uid ["+ (movimentoGestione !=null ? (movimentoGestione.getUid() + " - numero " + movimentoGestione.getNumeroBigDecimal()) : " movimento gestione NULL" ) +" ] ");
		}
	}
	
	
	/**
	 * Registra e inserisce prima nota per la liquidazione
	 * @param movimento
	 * @param tipoCollegamento
	 * @param codiceEvento
	 */
	private EsitoAttivaRegistrazioniMovFinFINGSADto registraEInserisciPrimaNotaLiquidazione(Liquidazione movimento, TipoCollegamento tipoCollegamento, String codiceEvento) {
		
		return registraEInserisciPrimaNotaLiquidazione(movimento, tipoCollegamento, codiceEvento, false);
	}

	/**
	 * @param movimento
	 * @param tipoCollegamento
	 * @param codiceEvento
	 * @return
	 */
	private EsitoAttivaRegistrazioniMovFinFINGSADto registraEInserisciPrimaNotaLiquidazione(Liquidazione movimento, TipoCollegamento tipoCollegamento, String codiceEvento, boolean saltaInserimentoPrimaNota) {
		final String methodName = "registraEInserisciPrimaNotaLiquidazione"; 
		EsitoAttivaRegistrazioniMovFinFINGSADto risultatoAttivazioneRegistrazioni = new EsitoAttivaRegistrazioniMovFinFINGSADto();
		registrazioneGENServiceHelper.init(serviceExecutor, req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), loginOperazione, bilancio.getAnno());
			
		try {
			
			
			List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
			
			if(registrazioniMovFin!=null && !registrazioniMovFin.isEmpty()) {
				registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esiste non devo inserire
			}
			Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
			
			// Per inserire le prime note anche per Gsa il servizio va richamato due volte, non ragiona sulla lista totale (verificato con Lisi)
			//List<RegistrazioneMovFin> registrazioniMovFinInserite = new ArrayList<RegistrazioneMovFin>();
			
			ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
			
			// Recupero dall'impegno/sub il pdc da salvare nel registro
			if(movimento.getImpegno().getElencoSubImpegni()== null || movimento.getImpegno().getElencoSubImpegni().isEmpty()){
				pdc.setUid(movimento.getImpegno().getIdPdc());
				pdc.setCodice(movimento.getImpegno().getCodPdc());
				pdc.setDescrizione(movimento.getImpegno().getDescPdc());
			}else{
				pdc.setUid((movimento.getImpegno().getElencoSubImpegni().get(0)).getIdPdc());
				pdc.setCodice((movimento.getImpegno().getElencoSubImpegni().get(0)).getCodPdc());
				pdc.setDescrizione((movimento.getImpegno().getElencoSubImpegni().get(0)).getDescPdc());
			}
			
			RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc, Ambito.AMBITO_FIN);
			//SIAC-5333
			risultatoAttivazioneRegistrazioni.setRegistrazioneMovFinFINInserita(registrazioneMovFin);
			//registrazioniMovFinInserite.add(registrazioneMovFin);
			//SIAC-5333
			boolean isEnteAbilitatoASaltareInserimentoPrimaNota ="TRUE".equals(ente.getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_PNOTA_DA_FIN));
			
			log.debug(methodName, "Devo saltare l'inserimento della prima nota automatica per l'ambito FIN?" + saltaInserimentoPrimaNota + ". L'utente e' abilitato a farlo? " + isEnteAbilitatoASaltareInserimentoPrimaNota);
			if( !isApplicabileSaltoInserimentoPrimaNota(saltaInserimentoPrimaNota)) {
				log.debug(methodName, "La prima nota per la registrazione uid[ " + (registrazioneMovFin != null? registrazioneMovFin.getUid() : "null") + "] non verra' effettuata.");
				registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFin);
			}
			if(movimento.getImpegno().isFlagAttivaGsa()){
				//SIAC-5333 per l'ambito GSA non devo prevedere la possibilita' di saltare l'inserimento della prima nota
				RegistrazioneMovFin registrazioneMovFinGsa = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc, Ambito.AMBITO_GSA);
				registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFinGsa);
				//registrazioniMovFinInserite.add(registrazioneMovFinGsa);
				risultatoAttivazioneRegistrazioni.setRegistrazioneMovFinGSAInserita(registrazioneMovFinGsa);
			}
			
			
		
		} catch (Exception e) {
			
			log.error(methodName, " Va in errore la registrazione gen o l'inserimento della prima nota, non faccio arrivate l'errore al client ");
		}
		return risultatoAttivazioneRegistrazioni;
	}
	
	/**
	 * Condizioni per Inserisce prima nota:
	 * Verifico 1) la condizione che determina la condizione di partita di giro (combinazione titolo e macroaggregato), 
	 * 			  se la classificazione finanziaria e' valida procedo ma senza inserire la prima nota  
	 * Verifico 2) la condizione non in partita di giro, se la classificazione finanziaria è valida inserisco la prima nota 
	 * La determina e' definitiva e c'e' il soggetto o la classe soggetto
	 * La determina e' definitiva non liquidabile ma di ambito GSA (SIAC-6343).
	 *
	 * @param isImpegnoInPartitoDiGiro the is impegno in partito di giro
	 * @param impegno the impegno
	 * @param aggiorna the aggiorna
	 * @return the boolean
	 */
	protected Boolean isCondizioneDiAttivazioneGENSoddisfatta(boolean isImpegnoInPartitoDiGiro, Impegno impegno, SubImpegno subimpegno, boolean aggiorna) {
		final String methodName = "isCondizioneDiAttivazioneGENSoddisfatta";
		
		boolean isImpegnoInStatoDefinitivo = false;
		
		boolean isImpegnoGSADefinitivoNonLiquidabile = false;
		
		Impegno testataOSub = subimpegno != null && subimpegno.getUid() != 0? subimpegno : impegno;
		
		// SIAC-6343
		if(testataOSub.getStatoOperativoMovimentoGestioneSpesa() != null) {
			isImpegnoInStatoDefinitivo = 
					CostantiFin.MOVGEST_STATO_DEFINITIVO.equals(testataOSub.getStatoOperativoMovimentoGestioneSpesa()); 
			isImpegnoGSADefinitivoNonLiquidabile = 
					testataOSub.isFlagAttivaGsa() && CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(testataOSub.getStatoOperativoMovimentoGestioneSpesa());  
		}
		
		boolean	isSoggettoOClasseValorizzato = aggiorna || (testataOSub.getSoggetto()!=null || testataOSub.getClasseSoggetto()!=null);
//		Boolean	isSoggettoOClasseValorizzato = true;
//	
//		if(!aggiorna){
//			isSoggettoOClasseValorizzato = impegno.getSoggetto()!=null || impegno.getClasseSoggetto()!=null;
//		}
		
		boolean isStatoAbilitanteScritture = ((isImpegnoInStatoDefinitivo && isSoggettoOClasseValorizzato)  || isImpegnoGSADefinitivoNonLiquidabile);
		
		boolean isClassificazioneFinanziariaAbilitanteScritture = false;
		
		String codiceTitoloSpesa = impegno.getCapitoloUscitaGestione().getTitoloSpesa().getCodice();
		String codiceMacroaggregato = impegno.getCapitoloUscitaGestione().getMacroaggregato().getCodice();
		
		log.debug(methodName, "codiceTitoloSpesa: "+codiceTitoloSpesa + ", codiceMacroaggregato: "+codiceMacroaggregato);
		
		if(verificaClassificazioneFinanziariaInPartitaDiGiro(codiceTitoloSpesa,codiceMacroaggregato)){
			
			isClassificazioneFinanziariaAbilitanteScritture =true;
			this.isImpegnoInPartitoDiGiro = true;
			
		}else isClassificazioneFinanziariaAbilitanteScritture =  Boolean.TRUE.equals(verificaClassificazioneFinanziaria(codiceTitoloSpesa,codiceMacroaggregato));
		
		
		log.debug(methodName, "Flag attivazione : isClassificazioneFinanziariaAbilitanteScritture ["+isClassificazioneFinanziariaAbilitanteScritture
				+ "],  isImpegnoInStatoDefinitivo["+isImpegnoInStatoDefinitivo + "], isImpegnoGSADefinitivoNonLiquidabile[" + isImpegnoGSADefinitivoNonLiquidabile + "], "
						+ " isSoggettoOClasseValorizzato["+isSoggettoOClasseValorizzato+"],  + isStatoAbilitanteScritture = ((isImpegnoInStatoDefinitivo && isSoggettoOClasseValorizzato)  || isImpegnoGSADefinitivoNonLiquidabile) [" + isStatoAbilitanteScritture +"]");	
		
		
		boolean condizioneAttivazioneSoddisfatta = isClassificazioneFinanziariaAbilitanteScritture  && isStatoAbilitanteScritture;
		log.info(methodName, "la condizione di attivazione delle scritture risulta essere soddisfatta?" + condizioneAttivazioneSoddisfatta);
		
		return Boolean.valueOf(condizioneAttivazioneSoddisfatta);
		
	}
	
	/**
	 * Condizioni per Inserisce prima nota:
	 * l'operatore ha indicato che l'accertamento NON sarà collegato a una fattura attiva
	 * l'accertamento deve essere definitivo
	 */
	protected Boolean isCondizioneDiAttivazioneGENSoddisfatta(Accertamento accertamento) {
		Boolean flagAttivaGenStatoDefinitivo = accertamento.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_DEFINITIVO);
		return flagAttivaGenStatoDefinitivo  && ! (accertamento.isFlagFattura() || accertamento.isFlagCorrispettivo());
	}
	
	
	

	
	/**
	 * Verifica, per la liquidazione, la condizione per attivare la registrazione GEN
	 * @param liquidazione
	 * @return
	 */
	protected Boolean isCondizioneDiAttivazioneGENSoddisfatta(Liquidazione liquidazione) {
		final String methodName = "isCondizioneDiAttivazioneGENSoddisfatta()";
		log.debug(methodName, "- Begin");
		
	 	// SIAC-5649 se residuo no scrittura: dopo confronto con analisti, tolgo il controllo
//		if(liquidazione.getImpegno().getAnnoMovimento()<liquidazione.getAnnoLiquidazione().intValue()){
//			return Boolean.FALSE;
//		}
		//
		
		/**
		 * Non si deve operare la scrittura se :
		 * impegno (o subimpegno) già collegato a prima nota
		 * se eventuale quota documento è già con prima nota
		 * la liquidazione è provvisoria
		 * 
		 * 
		 * CR-297:
		 * se la liquidazoine è associata a impegno residuo, si aggiungono i controlli su titolo 1 e macroaggregato 
		 */
		Boolean flagAttivaGenImpegnoOSubConPrimaNota = Boolean.FALSE;
		if(liquidazione.getImpegno().getElencoSubImpegni()!=null && !liquidazione.getImpegno().getElencoSubImpegni().isEmpty()){
			
			// NE DOVREBBE ARRIVARE SEMPRE E SOLO UNO, quello legato alla liquidazione
			for (SubImpegno subImpegno : liquidazione.getImpegno().getElencoSubImpegni()) {
				
				flagAttivaGenImpegnoOSubConPrimaNota = verificaRegistrazioniMovFin(subImpegno,TipoCollegamento.SUBIMPEGNO);
				
				if(flagAttivaGenImpegnoOSubConPrimaNota)
					break;
			}
			
		}else{
			
			flagAttivaGenImpegnoOSubConPrimaNota = verificaRegistrazioniMovFin(liquidazione.getImpegno(),TipoCollegamento.IMPEGNO);
		
		}
				
		Boolean flagAttivaGENSuDocumento = Boolean.FALSE;
		
		Boolean flagAttivaGenQuotaDocConPrimaNota = Boolean.FALSE;
		
		if(liquidazione.getSubdocumentoSpesa()!=null){
			
			log.debug(methodName , " La liqudazione ha subdocumenti  [uid subdoc: " + liquidazione.getSubdocumentoSpesa().getUid() + " ].. verifico se ha l'atto ");
			
			boolean verificaAncheAnniPrecedenti = true;
			flagAttivaGenQuotaDocConPrimaNota = verificaRegistrazioniMovFin(liquidazione.getSubdocumentoSpesa(),TipoCollegamento.SUBDOCUMENTO_SPESA,verificaAncheAnniPrecedenti);
			
			if(!flagAttivaGenQuotaDocConPrimaNota){
				
				// se non è stata emessa ancora la prima-prima nota sul documento (e doveva!) controllo
				// se il documento doveva andare in gen, testando il flagAttivaGEN, 
				// se flagAttivaGEN del documento e'  TRUE non registro e non emetto la prima nota per la liquidazione (lo fara' poi sul doc) 
				// NOTA: flagAttivaGEN --> il suo valore dipende da come hanno popolato il db: se quell'attributo per quel tipo documento 
				// non l'hanno popolato o se l'hanno popolato a false..
				if(liquidazione.getSubdocumentoSpesa().getDocumento()!=null && 
						liquidazione.getSubdocumentoSpesa().getDocumento().getTipoDocumento()!=null){
					
					if(liquidazione.getSubdocumentoSpesa().getDocumento().getTipoDocumento().getFlagAttivaGEN()!=null &&
							liquidazione.getSubdocumentoSpesa().getDocumento().getTipoDocumento().getFlagAttivaGEN()){					
						flagAttivaGENSuDocumento = Boolean.TRUE;
					}
				}
			}
		}
		
		Boolean flagAttivaGenLiquidazioneNonProvvisoria = liquidazione.getStatoOperativoLiquidazione().equals(StatoOperativoLiquidazione.PROVVISORIO);
		
		log.debug(methodName ," flagAttivaGenImpegnoOSubConPrimaNota: " + flagAttivaGenImpegnoOSubConPrimaNota 
				+ " flagAttivaGenLiquidazioneNonProvvisoria : " + flagAttivaGenLiquidazioneNonProvvisoria 
				+ " flagAttivaGenQuotaDocConPrimaNota : " + flagAttivaGenQuotaDocConPrimaNota 
				+ " flagAttivaGENSuDocumento : " + flagAttivaGENSuDocumento); // non lo usiamo!
		
		if(!flagAttivaGenImpegnoOSubConPrimaNota && 
				!flagAttivaGenLiquidazioneNonProvvisoria && 
				!flagAttivaGenQuotaDocConPrimaNota // && 	!flagAttivaGENSuDocumento 
				)
			return Boolean.TRUE;
		
		return Boolean.FALSE;

	}
	
	private Boolean verificaRegistrazioniMovFin(Entita movimento,TipoCollegamento tipoCollegamento) {
		return verificaRegistrazioniMovFin(movimento, tipoCollegamento,false);
	}

	private Boolean verificaRegistrazioniMovFin(Entita movimento,TipoCollegamento tipoCollegamento, boolean anniPrec) {
		registrazioneGENServiceHelper.init(serviceExecutor, req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), loginOperazione, bilancio.getAnno());
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
		if(!isEmpty(registrazioniMovFin)){
			return Boolean.TRUE;
		}
		
		if(anniPrec){
			List<RegistrazioneMovFin> registrazioniMovFinPrec = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinBilancioPrecendenteAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
			if(!isEmpty(registrazioniMovFinPrec)){
				return Boolean.TRUE;
			}
		}
		
		return Boolean.FALSE;
	}
	
	/**
	 * Verifica Classificazione finanziaria:
	 * 
	 * titolo 4 macro-aggregati 4020000, 4030000, 4040000
	 * titolo 7 macro-aggregato 7010000
	 * la determina è definitiva
	 * Deve esserci il soggetto."
	 *
	 * @param codiceTitoloSpesa the codice titolo spesa
	 * @param codiceMacroaggregato the codice macroaggregato
	 * @return the boolean
	 */
	private Boolean verificaClassificazioneFinanziariaInPartitaDiGiro(String codiceTitoloSpesa, String codiceMacroaggregato) {
		final String methodName = "verificaClassificazioneFinanziariaInPartitaDiGiro";
		if(codiceTitoloSpesa.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.TITOLO_7.getCodice()) && codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_7010000.getCodice()) ||
			(codiceTitoloSpesa.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.TITOLO_4.getCodice()) && 
					(codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_4020000.getCodice()) ||
							codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_4030000.getCodice()) ||
							codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_4040000.getCodice()) 
							))){
			log.debug(methodName, "Classificazione corretta");
			return Boolean.TRUE;
		}
		log.debug(methodName, "Classificazione non corretta");
		return Boolean.FALSE;
		
	}
	

	/**
 	* Verifica Classificazione finanziaria:
 	* 
    * titolo 1 e macro-aggregato 1040000 
	* titolo 2 macro-aggregati  2030000, 2040000, 2050000
	* titolo 3 tutto
	* titolo 4 macro-aggregato 4010000
	* titolo 5 tutto
	* titolo 7 macro-aggregati 7020000
    */
	private Boolean verificaClassificazioneFinanziaria(String codiceTitoloSpesa, String codiceMacroaggregato) {
		final String methodName = "verificaClassificazioneFinanziaria";
		log.debug(methodName, "Controllo la classificaziione finanziaria. Titolo: " + StringUtils.defaultIfBlank(codiceTitoloSpesa, "null/empty") + " Macro: " + StringUtils.defaultIfBlank(codiceMacroaggregato, "null/empty"));
		
		if((codiceTitoloSpesa.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.TITOLO_1.getCodice()) && codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_1040000.getCodice())) ||
				   (codiceTitoloSpesa.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.TITOLO_2.getCodice()) && 
						(codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_2030000.getCodice()) ||
								codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_2040000.getCodice()) ||
									codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_2050000.getCodice()))) ||
					codiceTitoloSpesa.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.TITOLO_3.getCodice())  || codiceTitoloSpesa.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.TITOLO_5.getCodice()) ||
					(codiceTitoloSpesa.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.TITOLO_4.getCodice()) && codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_4010000.getCodice())) ||
					(codiceTitoloSpesa.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.TITOLO_7.getCodice()) && 
								codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_7020000.getCodice())) 
				){
			log.debug(methodName, "Classificazione finanziaria corretta.");
			return Boolean.TRUE;
		}
		log.debug(methodName, "Classificazione finanziaria non corretta.");
		return Boolean.FALSE;
		
	}
	
	
	

	/**
	 * Annulla la registrazione e la prima nota (se presente) 
	 * 
	 */
	protected void annullaRegistrazioneEPrimaNotaMovimentoGestione(TipoCollegamento tipoCollegamento, MovimentoGestione movimento, Integer annoBilancioRequest) {
				
		String methodName = "annullaRegistrazioneEPrimaNotaMovimentoGestione";
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, annoBilancioRequest);
		
		log.debug(methodName, "Annullo la registrazione per il movimento: " + movimento.getUid());
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
		
		
		registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esisteva devo annullare le eventuali primeNote associate
		
	}
	
	/**
	 * Annulla la registrazione e la prima nota (se presente) per l'ordinativo
	 * 
	 */
	protected void annullaRegistrazioneEPrimaNotaOrdinativo(TipoCollegamento tipoCollegamento, Ordinativo movimento) {
				
		String methodName = "annullaRegistrazioneEPrimaNotaOrdinativo";
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, bilancio.getAnno());
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
		
		
		log.debug(methodName, "Annullo la registrazione per il movimento: " + movimento.getUid());
		registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esisteva devo annullare le eventuali primeNote associate
		
	}


	/**
	 * Annulla la registrazione e la prima nota (se presente) 
	 * Annullo tutto e poi reinserisco tutto!
	 * 
	 */
	protected void annullaRegistrazioneEPrimaNotaLiquidazione(TipoCollegamento tipoCollegamento, Liquidazione movimento) {
		
		String methodName = "annullaRegistrazioneEPrimaNotaLiquidazione";
				
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, bilancio.getAnno());
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
		
		log.debug(methodName, "Annullo la registrazione per il movimento: " + movimento.getUid());
		registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esisteva devo annullare le eventuali primeNote associate
		
	}
	
	
	
	/**
	 * Annulla sempre tutto e reinserisco!
	 * @param elencoModificheMovimento
	 * @param tipoCollegamento
	 * @param codiceEvento
	 */
	protected void gestisciRegistrazioneGENModificheMovimentoSpesa(Impegno impegno, SubImpegno sub, TipoCollegamento tipoCollegamento, boolean registraPerAnnoBilancioCorrente, boolean registraPerPredisposizioneConsuntivo, boolean residuo, boolean registraPerModificaImportoPluriennale, Integer annoBilancioRequest) {
		
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, annoBilancioRequest);
				
		List<RegistrazioneMovFin> registrazioniMovFin = new ArrayList<RegistrazioneMovFin>();
		List<ModificaMovimentoGestioneSpesa> elencoModificheMovimento = new ArrayList<ModificaMovimentoGestioneSpesa>();
		boolean modificheDiSub = false;
		
		if(sub == null){
			//devo registrare le modifiche solo se sull'impegno ci sono registrazioni non annullate 
			registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.IMPEGNO, impegno); //se presenti ne troverà una per ogni quota, altrimenti 0.
			elencoModificheMovimento = impegno.getListaModificheMovimentoGestioneSpesa();
		}else{
			registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.SUBIMPEGNO, sub); //se presenti ne troverà una per ogni quota, altrimenti 0.
			elencoModificheMovimento = sub.getListaModificheMovimentoGestioneSpesa();
			modificheDiSub = true;
		}
		
		// se ne trova è xchè non sono annullate
		if(registrazioniMovFin!=null && !registrazioniMovFin.isEmpty()){
			
			for (ModificaMovimentoGestioneSpesa movimento : elencoModificheMovimento) {
				
				if(!movimento.getCodiceStatoOperativoModificaMovimentoGestione().equals("A") && !movimento.getAttoAmministrativo().getStatoOperativo().equals(CostantiFin.ATTO_AMM_STATO_PROVVISORIO)){
				
					List<RegistrazioneMovFin> registrazioniMovFinMovimento = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
					
					// jira 3419, non devo piu annullare tutto:
					// quindi se la modifica è già stata registrata non ripeto la registrazione annullando quella precedente 
//					if(registrazioniMovFinMovimento!=null && !registrazioniMovFinMovimento.isEmpty())
//						registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFinMovimento);
					
					if(registrazioniMovFinMovimento == null || registrazioniMovFinMovimento.isEmpty()){
						
						String codiceEvento = "";
						
						if(registraPerAnnoBilancioCorrente && !registraPerPredisposizioneConsuntivo || registraPerModificaImportoPluriennale){
							
							codiceEvento = definisciCodiceEvento(modificheDiSub, movimento);
						}
						// RM : ripristino correzione sul controllo del tipo di modifica per definire il codice ROR (rev svn 15291)
						// un movimento puo avere modifiche di importo e di soggetto, in quest'ultimo caso non va fatto il controllo e si passa alla successiva modifica
						if(registraPerPredisposizioneConsuntivo){
							if(movimento.getImportoOld()==null){
								log.debug("gestisciRegistrazioneGENModificheMovimentoSpesa", "Siamo in modifica di importo, non effettuo la scrittura ROR perchè importo è NULL --> siamo in modifica di soggetto!");
								return ;
							}
							codiceEvento = definisciCodiceEventoROR(modificheDiSub, movimento,registraPerAnnoBilancioCorrente, residuo);
						}	
						
						// qui aggiungo il controllo in caso di modifica di soggetto, nel caso in cui non siamo in configurazione GSA non deve fare nulla
						boolean modificaSoggettoImpOSub = codiceEvento.equalsIgnoreCase(CodiceEventoEnum.INSERISCI_MODIFICA_SOGGETTO_SUBIMPEGNO.getCodice())
								|| codiceEvento.equalsIgnoreCase(CodiceEventoEnum.INSERISCI_MODIFICA_SOGGETTO_IMPEGNO.getCodice());
						
						boolean registraEScriviSoloInAmbitoGSA = false;
						
						if(modificaSoggettoImpOSub){
							if(!impegno.isFlagAttivaGsa()){
								log.debug("gestisciRegistrazioneGENModificheMovimentoSpesa", "Siamo in modifica di soggetto non Gsa, non effettuo alcuna scrittura.");
								//System.out.println("Siamo in modifica di soggetto non Gsa, non effettuo alcuna scrittura. CodiceEvento: " + codiceEvento + " e isFlagAttivaGsa: " + impegno.isFlagAttivaGsa());
								return;
							}else registraEScriviSoloInAmbitoGSA = true;
							
						}
						
						
						Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
						ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
						
						// Recupero dall'impegno il pdc da salvare nel registro
						// Jira 4506: se la modifica è di un sub il pdc deve essere quello del sub e non dell'impegno
						if(!modificheDiSub){
							pdc.setUid(impegno.getIdPdc());
							pdc.setCodice(impegno.getCodPdc());
							pdc.setDescrizione(impegno.getDescPdc());
						}else{
							
							pdc.setUid(sub.getIdPdc());
							pdc.setCodice(sub.getCodPdc());
							pdc.setDescrizione(sub.getDescPdc());
						}
											

						if(!registraEScriviSoloInAmbitoGSA){
							RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc, Ambito.AMBITO_FIN);
							if(!registraPerPredisposizioneConsuntivo)
								registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFin);
						}
						
						if(impegno.isFlagAttivaGsa()){							
							RegistrazioneMovFin registrazioneMovFinGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc, Ambito.AMBITO_GSA);
							if(!registraPerPredisposizioneConsuntivo)
								registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFinGSA);
						}
					
						
					}
				}
	
			
			}
		}
		
	}


	public String definisciCodiceEvento(boolean modificheDiSub,	ModificaMovimentoGestioneSpesa movimento) {
		
		String codiceEvento = "";
		
		// qui devo controllare se la modifica del movimento è di tipo importo o soggetto
		if(!movimento.isModificaDiImporto()){
			
			// modifica di soggetto
			
			if(modificheDiSub)
				codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_SOGGETTO_SUBIMPEGNO.getCodice();
			else
				codiceEvento = CodiceEventoEnum.INSERISCI_MODIFICA_SOGGETTO_IMPEGNO.getCodice();
			
			
		}else{
			
			// modifica di importo
			
			if(modificheDiSub)
				codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_SUBIMPEGNO.getCodice();
			else
				codiceEvento = CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_IMPEGNO.getCodice();
		}
		return codiceEvento;
	}
	
	/**
	 * Definisco il codice evento per la fase ROR
	 * @param modificheDiSub
	 * @param movimento
	 * @param competenza
	 * @param residuo
	 * @return
	 */
	public String definisciCodiceEventoROR(boolean modificheDiSub,	ModificaMovimentoGestioneSpesa movimento, boolean competenza, boolean residuo) {
		
		String codiceEvento = "";
		String methodName="definisciCodiceEventoROR";
		
		// sono sempre in modifica di importo
		// controlli su competenza/residuo e importo +/-
			
		log.debug(methodName, "ModificaMovimentoGestioneSpesa.IMPORTO MODIFICA ROR: " + movimento.getImportoOld()
		+ ". ModificaMovimentoGestioneSpesa.residuo: " + residuo 
		+ "ModificaMovimentoGestioneSpesa.competenza: " + competenza);
		
		boolean importoNegativo =  (movimento.getImportoOld().intValue() < 0) ? true : false;
		
		if(modificheDiSub){
		
			if(competenza && importoNegativo) 	codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_SUBIMPEGNO_ROR_COMPETENZA_MENO.getCodice();
			if(competenza && !importoNegativo) 	codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_SUBIMPEGNO_ROR_COMPETENZA_PIU.getCodice();
		
			if(residuo && importoNegativo) codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_SUBIMPEGNO_ROR_RESIDUO_MENO.getCodice();
			if(residuo && !importoNegativo) codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_SUBIMPEGNO_ROR_RESIDUO_PIU.getCodice();
		
		} else{
			
			if(competenza && importoNegativo) 	codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_IMPEGNO_ROR_COMPETENZA_MENO.getCodice();
			if(competenza && !importoNegativo) 	codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_IMPEGNO_ROR_COMPETENZA_PIU.getCodice();
		
			if(residuo && importoNegativo) codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_IMPEGNO_ROR_RESIDUO_MENO.getCodice();
			if(residuo && !importoNegativo) codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_IMPEGNO_ROR_RESIDUO_PIU.getCodice();
			
		}
		
		return codiceEvento;
	}
	
	protected ModificaMovimentoGestioneEntrata creaModifica(boolean isSub,Integer uIdAccertamento, 
			AttoAmministrativo attoAmministrativo, BigDecimal importoOld, BigDecimal importoNew, DatiOperazioneDto datiOperazioneDto,
			int numeroModifica){
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		ModificaMovimentoGestioneEntrata movimento = popolaDatiBaseModifica(isSub, attoAmministrativo, importoOld, importoNew);
		// movimento.setTipoMovimento(CostantiFin.MODIFICA_TIPO_ACC);
		datiOperazioneDto.setCurrMillisec(System.currentTimeMillis());
		
		//COMMENTO E USO numeroModifica al posto di questo calcolo:
		//int modifiche = accertamentoOttimizzatoDad.countModifiche(uIdAccertamento, datiOperazioneDto);
		//modifiche = modifiche + 1;
		//SIAC-7505
		return accertamentoOttimizzatoDad.aggiornaModificheMovimentoGestioneEntrata(movimento, uIdAccertamento, idEnte, datiOperazioneDto);
	}
	
	
	/**
	 * @param isSub
	 * @param attoAmministrativo
	 * @param importoOld
	 * @param importoNew
	 * @return
	 */
	protected ModificaMovimentoGestioneEntrata popolaDatiBaseModifica(boolean isSub,
			AttoAmministrativo attoAmministrativo, BigDecimal importoOld, BigDecimal importoNew) {
		ModificaMovimentoGestioneEntrata movimento = new ModificaMovimentoGestioneEntrata();

		movimento.setAttoAmministrativo(attoAmministrativo);
		
		//il metodo aggiornaModificheMovimentoGestioneEntrata non legge direttamente l'atto amministrativo ma legge i tre campi distinti
		movimento.setAttoAmministrativoAnno(String.valueOf(attoAmministrativo.getAnno()));
		movimento.setAttoAmministrativoNumero(attoAmministrativo.getNumero());
		movimento.setAttoAmministrativoTipoCode(attoAmministrativo.getTipoAtto().getCodice());

		movimento.setImportoOld(importoOld);
		movimento.setImportoNew(importoNew);
		if(isSub){
			movimento.setTipoMovimento(CostantiFin.MODIFICA_TIPO_SAC);
		} else {
			movimento.setTipoMovimento(CostantiFin.MODIFICA_TIPO_ACC);
		}
		// movimento.setTipoModificaMovimentoGestione(TipoModificaMovimentoGestione.RIAC);
		
		movimento.setDescrizione(CostantiFin.MODIFICA_CONTESTUALE_INSERIMENTO_MANUALE_ORDINATIVO);
		return movimento;
	}
	
	/**
	 * Definisco il codice evento per la fase ROR
	 * @param modificheDiSub
	 * @param movimento
	 * @param competenza
	 * @param residuo
	 * @return
	 */
	public String definisciCodiceEventoROR(boolean modificheDiSub,	ModificaMovimentoGestioneEntrata movimento, boolean competenza, boolean residuo) {
		
		String codiceEvento = "";
		String methodName = "definisciCodiceEventoROR"; 
		// sono sempre in modifica di importo
		// controlli su competenza/residuo e importo +/-
		
		log.debug(methodName,"ModificaMovimentoGestioneEntrata.IMPORTO MODIFICA ROR: " + movimento.getImportoOld() 
			+ " ModificaMovimentoGestioneEntrata.residuo: " + residuo 
			+ " ModificaMovimentoGestioneEntrata.competenza: " + competenza);
		
		boolean importoNegativo =  (movimento.getImportoOld().intValue() < 0) ? true : false;
		
		if(modificheDiSub){
		
			if(competenza && importoNegativo) 	codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_SUBACCERTAMENTO_ROR_COMPETENZA_MENO.getCodice();
			if(competenza && !importoNegativo) 	codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_SUBACCERTAMENTO_ROR_COMPETENZA_PIU.getCodice();
		
			if(residuo && importoNegativo) codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_SUBACCERTAMENTO_ROR_RESIDUO_MENO.getCodice();
			if(residuo && !importoNegativo) codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_SUBACCERTAMENTO_ROR_RESIDUO_PIU.getCodice();
		
		} else{
			
			if(competenza && importoNegativo) 	codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_ACCERTAMENTO_ROR_COMPETENZA_MENO.getCodice();
			if(competenza && !importoNegativo) 	codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_ACCERTAMENTO_ROR_COMPETENZA_PIU.getCodice();
		
			if(residuo && importoNegativo) codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_ACCERTAMENTO_ROR_RESIDUO_MENO.getCodice();
			if(residuo && !importoNegativo) codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_ACCERTAMENTO_ROR_RESIDUO_PIU.getCodice();
			
		}
		
		return codiceEvento;
	}
	
	
	public String definisciCodiceEvento(boolean modificheDiSub,	ModificaMovimentoGestioneEntrata movimento) {
		
		String codiceEvento = "";
		
		// qui devo controllare se la modifica del movimento è di tipo importo o soggetto
		if(!movimento.isModificaDiImporto()){
			
			// modifica di soggetto
			
			if(modificheDiSub)
				codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_SOGGETTO_SUBACCERTAMENTO.getCodice();
			else
				codiceEvento = CodiceEventoEnum.INSERISCI_MODIFICA_SOGGETTO_ACCERTAMENTO.getCodice();
		}else{
			
			// modifica di importo
			
			if(modificheDiSub)
				codiceEvento =  CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_SUBACCERTAMENTO.getCodice();
			else
				codiceEvento = CodiceEventoEnum.INSERISCI_MODIFICA_IMPORTO_ACCERTAMENTO.getCodice();
		}
		return codiceEvento;
	}
	
	
	/**
	 * Annulla sempre tutto e reinserisco!
	 * @param elencoModificheMovimento
	 * @param tipoCollegamento
	 * @param codiceEvento
	 */
	protected void gestisciRegistrazioneGENModificheMovimentoEntrata(Accertamento accertamento,SubAccertamento sub, TipoCollegamento tipoCollegamento, boolean registraPerAnnoBilancioCorrente, boolean flagFatturaNonModificato, boolean registraPerPredisposizioneConsuntivo, boolean residuo, boolean registraPerModificaImportoPluriennale, Integer annoBilancioRequest) {
		
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, annoBilancioRequest);
		
		List<ModificaMovimentoGestioneEntrata> elencoModificheMovimento = new ArrayList<ModificaMovimentoGestioneEntrata>();
		List<RegistrazioneMovFin> registrazioniMovFin = new ArrayList<RegistrazioneMovFin>();
		boolean modificheDiSub = false;
		
		if(sub == null){
			//devo registrare le modifiche solo se sull'accertamento ci sono registrazioni non annullate 
			registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.ACCERTAMENTO, accertamento); //se presenti ne troverà una per ogni quota, altrimenti 0.
			elencoModificheMovimento = accertamento.getListaModificheMovimentoGestioneEntrata();
		}else{
			registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.SUBACCERTAMENTO, sub); //se presenti ne troverà una per ogni quota, altrimenti 0.
			elencoModificheMovimento = sub.getListaModificheMovimentoGestioneEntrata();
			modificheDiSub = true;
		}
				
				
		// se ne trova è perchè non sono annullate
		if(registrazioniMovFin!=null && !registrazioniMovFin.isEmpty()){
			
			for (ModificaMovimentoGestioneEntrata movimento : elencoModificheMovimento) {
				
				if( (StringUtils.isNotEmpty(movimento.getCodiceStatoOperativoModificaMovimentoGestione()) && !movimento.getCodiceStatoOperativoModificaMovimentoGestione().equals("A")) && ( movimento.getAttoAmministrativo()!=null && !movimento.getAttoAmministrativo().getStatoOperativo().equals(CostantiFin.ATTO_AMM_STATO_PROVVISORIO))
						){
	
					List<RegistrazioneMovFin> registrazioniMovFinMovimento = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
					
					if(registrazioniMovFinMovimento==null || registrazioniMovFinMovimento.isEmpty()){
						
						String codiceEvento = "";
						
						if((registraPerAnnoBilancioCorrente && flagFatturaNonModificato && !registraPerPredisposizioneConsuntivo) || registraPerModificaImportoPluriennale){
							codiceEvento = definisciCodiceEvento(modificheDiSub, movimento);
						}

						if(registraPerPredisposizioneConsuntivo){
							
							if(movimento.getImportoOld()==null){
								log.debug("gestisciRegistrazioneGENModificheMovimentoEntrata", "Siamo in modifica di importo, non effettuo la scrittura ROR.");
								//System.out.println("Siamo in modifica di importo, non effettuo la scrittura ROR perchè importo è NULL, siamo in modifica di soggetto!");
								return ;
							}
							codiceEvento = definisciCodiceEventoROR(modificheDiSub, movimento,registraPerAnnoBilancioCorrente, residuo);
						}	
						
						
						
						boolean registraEScriviSoloInAmbitoGSA = false;
						
						// qui aggiungo il controllo in caso di modifica di soggetto, nel caso in cui non siamo in configurazione GSA
						if(codiceEvento.equalsIgnoreCase(CodiceEventoEnum.INSERISCI_MODIFICA_SOGGETTO_SUBACCERTAMENTO.getCodice())
								|| codiceEvento.equalsIgnoreCase(CodiceEventoEnum.INSERISCI_MODIFICA_SOGGETTO_ACCERTAMENTO.getCodice())){
							if(!accertamento.isFlagAttivaGsa()){
								return;
							}else registraEScriviSoloInAmbitoGSA = true;
							
						}
												
						//System.out.println("codiceEvento: " + codiceEvento);
						Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
						ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
						
						// Recupero dall'impegno il pdc da salvare nel registro
						// Jira 4506: se la modifica è di un sub il pdc deve essere quello del sub e non dell'impegno
						if(!modificheDiSub){
							pdc.setUid(accertamento.getIdPdc());
							pdc.setCodice(accertamento.getCodPdc());
							pdc.setDescrizione(accertamento.getDescPdc());
						}else{
							
							pdc.setUid(sub.getIdPdc());
							pdc.setCodice(sub.getCodPdc());
							pdc.setDescrizione(sub.getDescPdc());
						}
						
						if(!registraEScriviSoloInAmbitoGSA){
							RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc,  Ambito.AMBITO_FIN);
							if(!registraPerPredisposizioneConsuntivo)
								registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFin);
						}
						
						if(accertamento.isFlagAttivaGsa()){
							RegistrazioneMovFin registrazioneMovFinGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc, Ambito.AMBITO_GSA);
							if(!registraPerPredisposizioneConsuntivo)
								registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFinGSA);
						}
						
						
					}
				}
			}
		
		}
		
	}
	
	/**
	 * scorre la lista delle modifiche sui sub per definire se ci sono modifiche da INSERIRE
	 * @param list
	 * @return
	 */
	protected boolean presentiModificheSpesaSuiSubDaInserire(List<ModificaMovimentoGestioneSpesaInfoDto> list){
		boolean modifichePresenti = false;
		for(ModificaMovimentoGestioneSpesaInfoDto it: list){
			if(it.isModificheDaCrearePresenti()){
				modifichePresenti = true;
				break;
			}
		}
		return modifichePresenti;
	}
	
	/**
	 * scorre la lista delle modifiche sui sub per definire se ci sono modifiche da INSERIRE
	 * @param list
	 * @return
	 */
	protected boolean presentiModificheEntrataSuiSubDaInserire(List<ModificaMovimentoGestioneEntrataInfoDto> list){
		boolean modifichePresenti = false;
		for(ModificaMovimentoGestioneEntrataInfoDto it: list){
			if(it.isModificheDaCrearePresenti()){
				modifichePresenti = true;
				break;
			}
		}
		return modifichePresenti;
	}

	/**
	 * @return the bilancio
	 */
	public Bilancio getBilancio() {
		return bilancio;
	}

	/**
	 * @param bilancio the bilancio to set
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}
	
	
	protected RicercaProvvedimentoResponse ricercaProvvedimento(AttoAmministrativo atto, Richiedente richiedente ) {
		
		RicercaAtti ricercaAtto = new RicercaAtti();
		ricercaAtto.setAnnoAtto(atto.getAnno());
		ricercaAtto.setNumeroAtto(atto.getNumero());
		ricercaAtto.setStrutturaAmministrativoContabile(atto.getStrutturaAmmContabile());
		ricercaAtto.setTipoAtto(atto.getTipoAtto());
		
		RicercaProvvedimento reqAtto = new RicercaProvvedimento();
		reqAtto.setRicercaAtti(ricercaAtto);
		reqAtto.setRichiedente(richiedente);
		reqAtto.setEnte(ente);
		
		RicercaProvvedimentoResponse resAtto = provvedimentoService.ricercaProvvedimento(reqAtto);
		
		if(resAtto !=null && !resAtto.getListaAttiAmministrativi().isEmpty()){
			return resAtto;
		}else{
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Provvedimento", "anno: "+ricercaAtto.getAnnoAtto()+", numero: " +
																								ricercaAtto.getNumeroAtto()+ ", tipo: "+ ricercaAtto.getTipoAtto().getCodice()), Esito.FALLIMENTO);
		}
		
	}
	
	/**
	 * Metodo di comodo per costruire il riferimento anno numero della liquidazione
	 * @param liquidazione
	 * @return
	 */
	protected String riferimento(Liquidazione liquidazione){
		return CommonUtil.riferimento(liquidazione);
	}
	
	/**
	 *  Metodo di comodo per costruire il riferimento anno numero dell'ordinativo
	 * @param ordinativo
	 * @return
	 */
	protected String riferimento(Ordinativo ordinativo){
		return CommonUtil.riferimento(ordinativo);
	}
	
	/**
	 *  Metodo di comodo per costruire il riferimento anno numero del movimento indicato
	 * @param mov
	 * @return
	 */
	protected String riferimento(MovimentoGestione mov){
		return CommonUtil.riferimento(mov);
	}


	/**
	 * @return the isImpegnoInPartitoDiGiro
	 */
	public Boolean getIsImpegnoInPartitoDiGiro() {
		return isImpegnoInPartitoDiGiro;
	}


	/**
	 * @param isImpegnoInPartitoDiGiro the isImpegnoInPartitoDiGiro to set
	 */
	public void setIsImpegnoInPartitoDiGiro(Boolean isImpegnoInPartitoDiGiro) {
		this.isImpegnoInPartitoDiGiro = isImpegnoInPartitoDiGiro;
	}
	
	
	protected void checkListaErrori(List<Errore> listaErrori) {
		
		if(listaErrori!=null && listaErrori.size()>0){
			
			for (Errore errore : listaErrori) {
				
				throw new BusinessException(errore, Esito.FALLIMENTO);
			}
			
		}
	}
	
	protected <MG extends MovimentoGestione> MG caricaAnnoENumeroSeVuotiMaUidPresente(MG movimento){
		if(NumericUtils.nulloOMinoreUgualeAZero(movimento.getNumeroBigDecimal()) || NumericUtils.nulloOMinoreUgualeAZero(movimento.getAnnoMovimento())){
			if(movimento.getUid()>0){
				RicercaImpegnoK kMov = impegnoOttimizzatoDad.getChiaveLogicaByUid(movimento.getUid());
				movimento.setNumeroBigDecimal(kMov.getNumeroImpegno());
				movimento.setAnnoMovimento(kMov.getAnnoImpegno());
			}
		}
		return movimento;
	}
	
	// SEMPLICI WRAPPER DI METODI DI COMMONUTILS e StringUtils per scrivere piu velocemente le
	// chiamate ad essi:
	protected <T extends Object> List<T> toList(T... oggetto) {
		return CommonUtil.toList(oggetto);
	}

	protected <T extends Object> List<T> toList(List<T>... liste) {
		return CommonUtil.toList(liste);
	}

	protected <T extends Object> List<T> addAll(List<T> listaTo,
			List<T> listaFrom) {
		return CommonUtil.addAll(listaTo, listaFrom);
	}
	
	protected <T extends Object> List<T> addAllConNew(List<T> listaTo,
			List<T> listaFrom) {
		return CommonUtil.addAllConNew(listaTo, listaFrom);
	}
	
	protected boolean isEmpty(String s){
		return it.csi.siac.siacfinser.StringUtilsFin.isEmpty(s);
	}
	
	protected <OBJ extends Object> boolean isEmpty(List<OBJ> list){
		return it.csi.siac.siacfinser.StringUtilsFin.isEmpty(list);
	}
	//
	
	
	@Override
	protected RES instantiateNewRes() {
		return super.instantiateNewRes();
	}
	
	@Override
	protected void setServiceResponse(RES serviceResponse){
		super.setServiceResponse(serviceResponse);
	}
	
}
