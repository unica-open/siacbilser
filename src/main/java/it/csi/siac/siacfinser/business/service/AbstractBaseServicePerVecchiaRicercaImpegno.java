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

import org.springframework.beans.factory.annotation.Autowired;
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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.AccountDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
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
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.enumeration.CodiceEventoEnum;
import it.csi.siac.siacfinser.business.service.enumeration.CodiciControlloInnestiFinGenEnum;
import it.csi.siac.siacfinser.frontend.webservice.msg.PaginazioneRequest;
import it.csi.siac.siacfinser.integration.dad.AccertamentoDad;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.ImpegnoDad;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.dao.common.dto.ChiaveLogicaCapitoloDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImpegnoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaMovimentoGestioneEntrataInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaMovimentoGestioneSpesaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaVincoliImpegnoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovGestInfoDto;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
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
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Deprecated
public abstract class AbstractBaseServicePerVecchiaRicercaImpegno<REQ extends ServiceRequest, RES extends ServiceResponse>
		extends ExtendedBaseService<REQ, RES> {

	@Autowired
	protected AccertamentoDad accertamentoDad;

	@Autowired
	protected ImpegnoDad impegnoDad;

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

	protected Ente ente;
	protected String loginOperazione;
	protected Bilancio bilancio;
	
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

	protected CapitoloUscitaGestione caricaCapitoloUscitaGestione(
			Richiedente richiedente, int chiaveCapitolo, boolean sintetica) {
		CapitoloUscitaGestione capitoloUscitaGestione = null;
		if (sintetica) {
			// caricamento sintetico:
			ChiaveLogicaCapitoloDto chiaveLogica = commonDad
					.chiaveFisicaToLogicaCapitolo(chiaveCapitolo);
			RicercaSinteticaCapitoloUscitaGestione rceg = buildRequestPerRicercaSinteticaCapitoloUG(
					richiedente.getAccount().getEnte(), richiedente,
					chiaveLogica);
			RicercaSinteticaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService
					.ricercaSinteticaCapitoloUscitaGestione(rceg);
			capitoloUscitaGestione = response.getCapitoli().get(0);
		} else {
			// dettaglio completo:
			RicercaDettaglioCapitoloUGest ricercaDettaglioCapitoloUGest = new RicercaDettaglioCapitoloUGest();
			ricercaDettaglioCapitoloUGest.setChiaveCapitolo(chiaveCapitolo);

			RicercaDettaglioCapitoloUscitaGestione ricercaDettaglioCapitoloUscitaGestione = new RicercaDettaglioCapitoloUscitaGestione();
			ricercaDettaglioCapitoloUscitaGestione
					.setRicercaDettaglioCapitoloUGest(ricercaDettaglioCapitoloUGest);
			ricercaDettaglioCapitoloUscitaGestione.setRichiedente(richiedente);
			ricercaDettaglioCapitoloUscitaGestione.setEnte(richiedente
					.getAccount().getEnte());

			RicercaDettaglioCapitoloUscitaGestioneResponse ricercaDettaglioCapitoloUscitaGestioneResponse = capitoloUscitaGestioneService
					.ricercaDettaglioCapitoloUscitaGestione(ricercaDettaglioCapitoloUscitaGestione);
			capitoloUscitaGestione = ricercaDettaglioCapitoloUscitaGestioneResponse
					.getCapitoloUscita();
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

	protected HashMap<Integer, CapitoloEntrataGestione> caricaCapitolEntrataGestioneEResiduo(
			Richiedente richiedente, Integer chiaveCapitolo,
			Integer chiaveCapitoloResiduo) {
		HashMap<Integer, CapitoloEntrataGestione> capitoliDaServizio = new HashMap<Integer, CapitoloEntrataGestione>();
		CapitoloEntrataGestione capitoloUscitaGestione = caricaCapitoloEntrataGestione(
				richiedente, chiaveCapitolo, true);
		capitoliDaServizio.put(chiaveCapitolo, capitoloUscitaGestione);
		if (chiaveCapitoloResiduo != null) {
			CapitoloEntrataGestione capitoloUscitaGestioneResiduo = caricaCapitoloEntrataGestione(
					richiedente, chiaveCapitoloResiduo, true);
			capitoliDaServizio.put(chiaveCapitoloResiduo,
					capitoloUscitaGestioneResiduo);
		}
		return capitoliDaServizio;
	}

	protected CapitoloEntrataGestione caricaCapitoloEntrataGestione(
			Richiedente richiedente, int chiaveCapitolo, boolean sintetica) {
		CapitoloEntrataGestione capitoloEntrataGestione = null;
		if (sintetica) {
			// approccio sintetico:
			ChiaveLogicaCapitoloDto chiaveLogica = commonDad
					.chiaveFisicaToLogicaCapitolo(chiaveCapitolo);
			RicercaSinteticaCapitoloEntrataGestione rceg = buildRequestPerRicercaSinteticaCapitoloEG(
					richiedente.getAccount().getEnte(), richiedente,
					chiaveLogica);
			RicercaSinteticaCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService
					.ricercaSinteticaCapitoloEntrataGestione(rceg);
			capitoloEntrataGestione = response.getCapitoli().get(0);
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

		Impegno impegnoRitorno = impegno;

		impegnoRitorno = (Impegno) impegnoDad.ricercaMovimentoPk(richiedente,
				ente, String.valueOf(bilancio.getAnno()),
				Integer.valueOf(impegno.getAnnoMovimento()),
				impegno.getNumeroBigDecimal(), CostantiFin.MOVGEST_TIPO_IMPEGNO, false);

		return impegnoRitorno;

	}

	protected Accertamento completaDisponibilitaAccertamento(
			Richiedente richiedente, Ente ente, Accertamento accertamento,
			Bilancio bilancio) {

		Accertamento accertamentoRitorno = accertamento;

		accertamentoRitorno = (Accertamento) accertamentoDad
				.ricercaMovimentoPk(richiedente, ente,
						String.valueOf(bilancio.getAnno()),
						Integer.valueOf(accertamento.getAnnoMovimento()),
						accertamento.getNumeroBigDecimal(),
						CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, false);

		return accertamentoRitorno;

	}

	protected Impegno completaDatiRicercaImpegnoPk(Richiedente richiedente,Impegno impegno, String annoEsercizio) {
		
		HashMap<String, AttoAmministrativo> cacheAttoAmm = new HashMap<String, AttoAmministrativo>();
		
		CapitoloUscitaGestione capitoloUscitaGestione = caricaCapitoloUscitaGestione(richiedente, impegno.getChiaveCapitoloUscitaGestione(), true);
		
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

					Accertamento acc = (Accertamento) accertamentoDad.ricercaMovimentoPk(richiedente,capitoloEntrataGestione.getEnte(),annoEsercizio, vincoloImpegno.getAccertamento().getAnnoMovimento(), vincoloImpegno.getAccertamento().getNumeroBigDecimal(),CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, false);
					
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

	protected Accertamento completaDatiRicercaAccertamentoPk(
			Richiedente richiedente, Accertamento accertamento) {
		CapitoloEntrataGestione capitoloEntrataGestione = caricaCapitoloEntrataGestione(
				richiedente, accertamento.getChiaveCapitoloEntrataGestione(),
				true);
		if (null != capitoloEntrataGestione)
			accertamento.setCapitoloEntrataGestione(capitoloEntrataGestione);

		if (null != accertamento.getAttoAmmAnno()
				&& null != accertamento.getAttoAmmNumero()
				&& null != accertamento.getAttoAmmTipoAtto()) {
			// leggo l'idStruttura amministrativa, che puo' anche essere nullo:
			Integer idStrutturaAmministrativa = accertamento
					.getIdStrutturaAmministrativa();
			AttoAmministrativo attoAmministrativo = estraiAttoAmministrativo(
					richiedente, accertamento.getAttoAmmAnno(),
					accertamento.getAttoAmmNumero(),
					accertamento.getAttoAmmTipoAtto(),
					idStrutturaAmministrativa);
			if (null != attoAmministrativo)
				accertamento.setAttoAmministrativo(attoAmministrativo);
		}

		List<ModificaMovimentoGestioneEntrata> elencoModificheMovimentoGestioneEntrataAcc = accertamento
				.getListaModificheMovimentoGestioneEntrata();
		if (null != elencoModificheMovimentoGestioneEntrataAcc
				&& elencoModificheMovimentoGestioneEntrataAcc.size() > 0) {
			for (ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrataAcc : elencoModificheMovimentoGestioneEntrataAcc) {
				if (null != modificaMovimentoGestioneEntrataAcc
						.getAttoAmmAnno()
						&& null != modificaMovimentoGestioneEntrataAcc
								.getAttoAmmNumero()
						&& null != modificaMovimentoGestioneEntrataAcc
								.getAttoAmmTipoAtto()) {
					// leggo l'idStruttura amministrativa, che puo' anche essere
					// nullo:
					Integer idStrutturaAmministrativa = modificaMovimentoGestioneEntrataAcc
							.getIdStrutturaAmministrativa();
					AttoAmministrativo attoAmministrativoModificaAcc = estraiAttoAmministrativo(
							richiedente,
							modificaMovimentoGestioneEntrataAcc
									.getAttoAmmAnno(),
							modificaMovimentoGestioneEntrataAcc
									.getAttoAmmNumero(),
							modificaMovimentoGestioneEntrataAcc
									.getAttoAmmTipoAtto(),
							idStrutturaAmministrativa);
					if (null != attoAmministrativoModificaAcc)
						modificaMovimentoGestioneEntrataAcc
								.setAttoAmministrativo(attoAmministrativoModificaAcc);
				}
			}
		}

		List<SubAccertamento> elencoSubAccertamenti = accertamento.getElencoSubAccertamenti();
		
		if (null != elencoSubAccertamenti && elencoSubAccertamenti.size() > 0) {
			
			for (SubAccertamento subAccertamento : elencoSubAccertamenti) {
				if (null != subAccertamento.getAttoAmmAnno() && null != subAccertamento.getAttoAmmNumero()
																	&& null != subAccertamento.getAttoAmmTipoAtto()) {
					// leggo l'idStruttura amministrativa, che puo' anche essere
					// nullo:
					Integer idStrutturaAmministrativa = subAccertamento.getIdStrutturaAmministrativa();
					AttoAmministrativo attoAmministrativoSub = estraiAttoAmministrativo(richiedente, subAccertamento.getAttoAmmAnno(),
																						subAccertamento.getAttoAmmNumero(),
																						subAccertamento.getAttoAmmTipoAtto(),
																						idStrutturaAmministrativa);
					
					if (null != attoAmministrativoSub)
						subAccertamento.setAttoAmministrativo(attoAmministrativoSub);
				}

				List<ModificaMovimentoGestioneEntrata> elencoModificheMovimentoGestioneEntrataSubAcc = subAccertamento
						.getListaModificheMovimentoGestioneEntrata();
				if (null != elencoModificheMovimentoGestioneEntrataSubAcc
						&& elencoModificheMovimentoGestioneEntrataSubAcc.size() > 0) {
					for (ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrataSubAcc : elencoModificheMovimentoGestioneEntrataSubAcc) {
						if (null != modificaMovimentoGestioneEntrataSubAcc
								.getAttoAmmAnno()
								&& null != modificaMovimentoGestioneEntrataSubAcc
										.getAttoAmmNumero()
								&& null != modificaMovimentoGestioneEntrataSubAcc
										.getAttoAmmTipoAtto()) {
							// leggo l'idStruttura amministrativa, che puo'
							// anche essere nullo:
							Integer idStrutturaAmministrativa = modificaMovimentoGestioneEntrataSubAcc
									.getIdStrutturaAmministrativa();
							AttoAmministrativo attoAmministrativoModificaSubAcc = estraiAttoAmministrativo(
									richiedente,
									modificaMovimentoGestioneEntrataSubAcc
											.getAttoAmmAnno(),
									modificaMovimentoGestioneEntrataSubAcc
											.getAttoAmmNumero(),
									modificaMovimentoGestioneEntrataSubAcc
											.getAttoAmmTipoAtto(),
									idStrutturaAmministrativa);
							if (null != attoAmministrativoModificaSubAcc)
								modificaMovimentoGestioneEntrataSubAcc
										.setAttoAmministrativo(attoAmministrativoModificaSubAcc);
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
		List<AttoAmministrativo> listaAttoAmministrativo = ricercaProvvedimentoResponse
				.getListaAttiAmministrativi();
		if (null != listaAttoAmministrativo
				&& listaAttoAmministrativo.size() > 0) {
			attoAmministrativo = listaAttoAmministrativo.get(0);
		}
		return attoAmministrativo;
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
			Integer idStrutturaAmministrativa) {

		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setAnnoAtto(Integer.parseInt(annoAttoAmministrativo));
		ricercaAtti.setNumeroAtto(numeroAttoAmministrativo);
		ricercaAtti.setTipoAtto(tipoAtto);

		// dati struttura amministrativa:
		if (idStrutturaAmministrativa != null) {
			StrutturaAmministrativoContabile strutturaAmm = new StrutturaAmministrativoContabile();
			strutturaAmm.setUid(idStrutturaAmministrativa);
			ricercaAtti.setStrutturaAmministrativoContabile(strutturaAmm);
		}

		RicercaProvvedimento ricercaProvvedimento = new RicercaProvvedimento();
		ricercaProvvedimento.setEnte(richiedente.getAccount().getEnte());
		ricercaProvvedimento.setRichiedente(richiedente);
		ricercaProvvedimento.setRicercaAtti(ricercaAtti);

		RicercaProvvedimentoResponse ricercaProvvedimentoResponse = provvedimentoService
				.ricercaProvvedimento(ricercaProvvedimento);

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

		attoAmministrativo = cacheAttoAmm.get(key);

		if (attoAmministrativo == null) {
			attoAmministrativo = estraiAttoAmministrativo(richiedente,
					annoAttoAmministrativo, numeroAttoAmministrativo, tipoAtto,
					idStrutturaAmministrativa);
			cacheAttoAmm.put(key, attoAmministrativo);
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
			ChiaveLogicaCapitoloDto chiaveLogica) {
		// Invoco il metodo "core" di costruzione della request:
		RicercaSinteticaCapitoloEntrataGestione rceg = buildRequestPerRicercaSinteticaCapitoloEG(
				ente, richiedente, chiaveLogica.getAnnoCapitolo(),
				chiaveLogica.getAnnoCapitolo(),
				chiaveLogica.getNumeroCapitolo(),
				chiaveLogica.getNumeroArticolo(), chiaveLogica.getNumeroUeb());
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
			ChiaveLogicaCapitoloDto chiaveLogica) {
		// Invoco il metodo "core" di costruzione della request:
		RicercaSinteticaCapitoloUscitaGestione rceg = buildRequestPerRicercaSinteticaCapitoloUG(
				ente, richiedente, chiaveLogica.getAnnoCapitolo(),
				chiaveLogica.getAnnoCapitolo(),
				chiaveLogica.getNumeroCapitolo(),
				chiaveLogica.getNumeroArticolo(), chiaveLogica.getNumeroUeb());
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
			Integer numeroArticolo, Integer numeroUeb) {
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

		// setto il parametro principale di ricerca
		rceg.setRicercaSinteticaCapitoloUGest(eGest);

		return rceg;
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
			infoVincoliValutati = impegnoDad.valutaVincoliInAggiornamento(
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
				MovGestInfoDto movGestInfoDto = impegnoDad
						.caricaInfoAccertamentoDelVincolo(datiOperazione,
								bilancio, bilancioAnnoSuccessivo, null,
								siacRMovgestTsDaAnnullare);
				elencoInfoAccertamentiCoinvolti.add(movGestInfoDto);
			}
		}
		if (vincoliDaInserire != null && vincoliDaInserire.size() > 0) {
			// VINCOLI INSERITI
			for (VincoloImpegno vincoloImpegno : vincoliDaInserire) {
				MovGestInfoDto movGestInfoDto = impegnoDad
						.caricaInfoAccertamentoDelVincolo(datiOperazione,
								bilancio, bilancioAnnoSuccessivo,
								vincoloImpegno, null);
				elencoInfoAccertamentiCoinvolti.add(movGestInfoDto);
			}
		}
		if (vincoliDaAggiornare != null && vincoliDaAggiornare.size() > 0) {
			// VINCOLI MODIFICATI
			for (VincoloImpegno vincoloImpegno : vincoliDaAggiornare) {
				MovGestInfoDto movGestInfoDto = impegnoDad
						.caricaInfoAccertamentoDelVincolo(datiOperazione,
								bilancio, bilancioAnnoSuccessivo,
								vincoloImpegno, null);
				elencoInfoAccertamentiCoinvolti.add(movGestInfoDto);
			}
		}
		if (elencoInfoAccertamentiCoinvolti != null
				&& elencoInfoAccertamentiCoinvolti.size() > 0) {
			for (MovGestInfoDto accIt : elencoInfoAccertamentiCoinvolti) {
				if (accIt.getSiacTMovgestTsResiduo() == null) {
					// NON ESITE l'accertamento residuo, le routine successive
					// di doppia gestione potrebbero doverlo creare da zero
					// quindi qui carichiamo i dati completi dell'accertamento
					// in modo da poterli usare per inserire un nuovo
					// accertamento in residuo tramite l'operazione interna di
					// inserimento:
					Accertamento accertamentoFresco = (Accertamento) accertamentoDad
							.ricercaMovimentoPk(richiedente, ente, Integer
									.toString(anno), accIt.getSiacTMovgest()
									.getMovgestAnno(), accIt.getSiacTMovgest()
									.getMovgestNumero(),
									CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, true);
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
	
	
	protected void gestisciRegistrazioneGENPerLiquidazione(Liquidazione liquidazione, String codiceEvento) {
		String methodName = "gestisciRegistrazioneGENPerLiquidazione";
		
		if(!isCondizioneDiAttivazioneGENSoddisfatta(liquidazione)) {
			log.debug(methodName, "condizione di attivazione non soddisfatta. Non viene inserita/aggiornata la RegistrazioneMovFin.");
			return;
		}
		
		registraEInserisciPrimaNotaLiquidazione(liquidazione,TipoCollegamento.LIQUIDAZIONE, codiceEvento);
		
	}
	
	
	
	protected void gestisciRegistrazioneGENPerImpegno(Impegno impegno, TipoCollegamento tipoCollegamento, Boolean aggiorna, String codiceEvento) {
		String methodName = "gestisciRegistrazioneGENPerImpegno";
		Boolean isImpegnoInPartitoDiGiro = false;
		
		if(!isCondizioneDiAttivazioneGENSoddisfatta(isImpegnoInPartitoDiGiro, impegno , aggiorna )) {
				log.debug(methodName, "condizione di attivazione non soddisfatta. Non viene inserita/aggiornata la RegistrazioneMovFin.");
				return;
		}
		
		if(isImpegnoInPartitoDiGiro && tipoCollegamento.getCodice().equals("I")) codiceEvento = CodiceEventoEnum.INSERISCI_IMPEGNO_PRG.getCodice();
		
		List<RegistrazioneMovFin> registrazioniMovFinInserite = verificaERegistraMovimewntoGestione(impegno, tipoCollegamento, codiceEvento, impegno.isFlagAttivaGsa());
		
		if(!isImpegnoInPartitoDiGiro)
			registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInserite);
		
	}
	
	/**
	 * Gestione SubImpegno
	 * @param impegno
	 * @param subImpegno
	 * @param tipoCollegamento
	 * @param aggiorna
	 * @param codiceEvento
	 */
	protected void gestisciRegistrazioneGENPerSubImpegno(Impegno impegno, SubImpegno subImpegno, TipoCollegamento tipoCollegamento, Boolean aggiorna, String codiceEvento) {
		String methodName = "gestisciRegistrazioneGENPerSubImpegno";
		Boolean isImpegnoInPartitoDiGiro = false;
		
		if(!isCondizioneDiAttivazioneGENSoddisfatta(isImpegnoInPartitoDiGiro, impegno , aggiorna )) {
				log.debug(methodName, "condizione di attivazione non soddisfatta. Non viene inserita/aggiornata la RegistrazioneMovFin.");
				return;
		}
		
		//if(isImpegnoInPartitoDiGiro && tipoCollegamento.getCodice().equals("I")) codiceEvento = CodiceEventoEnum.INSERISCI_IMPEGNO_PRG.getCodice();
		
		ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
		// Recupero dall'impegno il pdc da salvare nel registro
		pdc.setUid(impegno.getIdPdc());
		pdc.setCodice(impegno.getCodPdc());
		pdc.setDescrizione(impegno.getDescPdc());
		
		List<RegistrazioneMovFin> registrazioniMovFinInserite = verificaERegistraSubMovimewntoGestione(subImpegno, pdc, tipoCollegamento, codiceEvento, impegno.isFlagAttivaGsa());
		
		if(!isImpegnoInPartitoDiGiro)
			registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInserite);
		
	}
	
	/**
	 * Gestione SubAccertamento
	 * @param impegno
	 * @param subImpegno
	 * @param tipoCollegamento
	 * @param aggiorna
	 * @param codiceEvento
	 */
	protected void gestisciRegistrazioneGENPerSubAccertamento(Accertamento accertamento, SubAccertamento subAccertamento, TipoCollegamento tipoCollegamento, String codiceEvento) {
		String methodName = "gestisciRegistrazioneGENPerSubAccertamento";
		
		
		Boolean flagAttivaGenStatoDefinitivo = subAccertamento.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_DEFINITIVO);
		
		if(	!flagAttivaGenStatoDefinitivo || accertamento.isFlagFattura() || accertamento.isFlagCorrispettivo() ){
			log.debug(methodName, "condizione di attivazione non soddisfatta. Non viene inserita/aggiornata la RegistrazioneMovFin.");
			return;
		}

		
		ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
		// Recupero dall'accertamento il pdc da salvare nel registro
		pdc.setUid(accertamento.getIdPdc());
		pdc.setCodice(accertamento.getCodPdc());
		pdc.setDescrizione(accertamento.getDescPdc());
	
		
		List<RegistrazioneMovFin> registrazioniMovFinInserite = verificaERegistraSubMovimewntoGestione(subAccertamento, pdc, tipoCollegamento, codiceEvento, accertamento.isFlagAttivaGsa());
		
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInserite);
		
	}
	
	
	/**
	 * 
	 * @param movimento
	 * @param tipoCollegamento
	 * @param codiceEvento
	 * 
	 */
	protected void gestisciRegistrazioneGENPerAccertamento(Accertamento accertamento, TipoCollegamento tipoCollegamento, String codiceEvento) {
		String methodName = "gestisciRegistrazioneGENPerAccertamento";
		
		
		if(!isCondizioneDiAttivazioneGENSoddisfatta(accertamento)) {
			log.debug(methodName, "condizione di attivazione non soddisfatta. Non viene inserita/aggiornata la RegistrazioneMovFin.");
			return;
		}

		List<RegistrazioneMovFin> registrazioniMovFinInserite = verificaERegistraMovimewntoGestione(accertamento, tipoCollegamento, codiceEvento, accertamento.isFlagAttivaGsa());
				
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInserite);
	
	}
	
	
	/**
	 * Verifica e registra il movimento di tipo ORDINATIVO, inserendo poi la prima nota 
	 * @param ordinativo
	 * @param tipoCollegamento
	 */
	protected void gestisciRegistrazioneGENPerOrdinativo(Ordinativo ordinativo, MovimentoGestione movimentoGestione, TipoCollegamento tipoCollegamento, boolean aggiorna) {
		
		registrazioneGENServiceHelper.init(serviceExecutor, req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), loginOperazione, bilancio.getAnno());
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, ordinativo); //se presenti ne troverà una per ogni quota, altrimenti 0.

		boolean isOrdinativoPagamento = tipoCollegamento.getCodice().equals("OP");
		
		if(aggiorna){
			
			definisciEventoOrdinativo(aggiorna, isOrdinativoPagamento);
			
			if(registrazioniMovFin!=null && !registrazioniMovFin.isEmpty()){
				eseguiRegistrazioneGENOrdinativo(ordinativo, movimentoGestione, tipoCollegamento, registrazioniMovFin, definisciEventoOrdinativo(aggiorna, isOrdinativoPagamento));
			}
			
		}else eseguiRegistrazioneGENOrdinativo(ordinativo, movimentoGestione, tipoCollegamento, registrazioniMovFin, definisciEventoOrdinativo(aggiorna, isOrdinativoPagamento));
	}
	
	
	/**
	 * 
	 * @param aggiorna
	 * @param isOrdinativoPagamento
	 * @return
	 */
	private String definisciEventoOrdinativo(boolean aggiorna, boolean isOrdinativoPagamento) {
		String codiceEvento;
		
		if(isOrdinativoPagamento){
			
			if(aggiorna)
				codiceEvento = CodiceEventoEnum.AGGIORNA_ORDINATIVO_PAGAMENTO.getCodice();
			else
				codiceEvento = CodiceEventoEnum.INSERISCI_ORDINATIVO_PAGAMENTO.getCodice();
		}else{
			
			if(aggiorna)
				codiceEvento = CodiceEventoEnum.AGGIORNA_ORDINATIVO_INCASSO.getCodice();
			else
				codiceEvento = CodiceEventoEnum.INSERISCI_ORDINATIVO_INCASSO.getCodice();
		}
		
		return codiceEvento;
	}
	

	/**
	 * Movimento Gestione (o Impegno o Accertamento)
	 * @param movimento
	 * @param tipoCollegamento
	 * @return
	 */
	private List<RegistrazioneMovFin> verificaERegistraMovimewntoGestione(MovimentoGestione movimento, TipoCollegamento tipoCollegamento, String codiceEvento, boolean flagGsa) {
		
		registrazioneGENServiceHelper.init(serviceExecutor, req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), loginOperazione, bilancio.getAnno());
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
		
		registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esisteva devo annullare le eventuali primeNote associate
		
		Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
		List<RegistrazioneMovFin> registrazioniMovFinInserite = new ArrayList<RegistrazioneMovFin>();
		
	
		ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
		// Recupero dall'impegno il pdc da salvare nel registro
		pdc.setUid(movimento.getIdPdc());
		pdc.setCodice(movimento.getCodPdc());
		pdc.setDescrizione(movimento.getDescPdc());
		
		
		RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento, pdc, Ambito.AMBITO_FIN);
		registrazioniMovFinInserite.add(registrazioneMovFin);
		
		// jira 2659, innesto GSA, se l'impegno ha il flag 'Rilevante Co.Ge. GSA' registro e emetto prima nota in ambito GSA
		if(flagGsa){
			RegistrazioneMovFin registrazioneMovFinGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento, pdc, Ambito.AMBITO_GSA);
			registrazioniMovFinInserite.add(registrazioneMovFinGSA);
		}
		
		return registrazioniMovFinInserite;
	}
	
	
	/**
	 * Movimento Gestione (o SubImpegno o SubAccertamento)
	 * @param movimento
	 * @param tipoCollegamento
	 * @return
	 */
	private List<RegistrazioneMovFin> verificaERegistraSubMovimewntoGestione(MovimentoGestione movimento, ElementoPianoDeiConti pdc , TipoCollegamento tipoCollegamento, String codiceEvento, boolean flagGsa) {
		
		registrazioneGENServiceHelper.init(serviceExecutor, req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), loginOperazione, bilancio.getAnno());
		List<RegistrazioneMovFin> registrazioniMovFinInserite = new ArrayList<RegistrazioneMovFin>();
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
		
		// jira 3419, non devo piu annullare tutto:
		// quindi se il sub è già stato registrato non ripeto la registarzione annullando quella precedente 
		//registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esisteva devo annullare le eventuali primeNote associate
		if(registrazioniMovFin ==null  || registrazioniMovFin.isEmpty()){
			Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
			
			
			RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc,  Ambito.AMBITO_FIN);
			registrazioniMovFinInserite.add(registrazioneMovFin);
			
			
			// jira 2659, innesto GSA, se l'impegno ha il flag 'Rilevante Co.Ge. GSA' registro e emetto prima nota in ambito GSA
			if(flagGsa){
				RegistrazioneMovFin registrazioniMovFinInseriteGSA  =  registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc,  Ambito.AMBITO_GSA);
				registrazioniMovFinInserite.add(registrazioniMovFinInseriteGSA);
			}
		}
		
		return registrazioniMovFinInserite;
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
			Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
			List<RegistrazioneMovFin> registrazioniMovFinInserite = new ArrayList<RegistrazioneMovFin>();
			ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
		
			// Recupero dall'impegno il pdc da salvare nel registro
			pdc.setUid(movimentoGestione.getIdPdc());
			pdc.setCodice(movimentoGestione.getCodPdc());
			pdc.setDescrizione(movimentoGestione.getDescPdc());
		
			RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, ordinativo , pdc, Ambito.AMBITO_FIN);
			registrazioniMovFinInserite.add(registrazioneMovFin);
			
			// jira 2659, innesto GSA, se il movimento gestione ha il flag 'Rilevante Co.Ge. GSA' registro e emetto prima nota in ambito GSA
			if(movimentoGestione.isFlagAttivaGsa()){
				RegistrazioneMovFin registrazioniMovFinInseriteGSA  =  registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, ordinativo , pdc, Ambito.AMBITO_GSA);
				registrazioniMovFinInserite.add(registrazioniMovFinInseriteGSA);
			}
			
			registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInserite);
		
		}else {
			log.info(methodName, "Impossibile procedere alla registrazione dell'ordinativo con uid [" + ordinativo.getUid()+" ] , verificare il pdc finanziario del movimentoGestione con uid ["+ movimentoGestione !=null ? (movimentoGestione.getUid() + " - numero " + movimentoGestione.getNumeroBigDecimal()) : " movimento gestione NULL" +" ] ");
		}
	}
	
	
	/**
	 * Registra e inserisce prima nota per la liquidazione
	 * @param movimento
	 * @param tipoCollegamento
	 * @param codiceEvento
	 */
	private void registraEInserisciPrimaNotaLiquidazione(Liquidazione movimento, TipoCollegamento tipoCollegamento, String codiceEvento) {
		
		String methodName = "registraEInserisciPrimaNotaLiquidazione"; 
		registrazioneGENServiceHelper.init(serviceExecutor, req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), loginOperazione, bilancio.getAnno());
		
		try {
			
			
			List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
			
			if(registrazioniMovFin!=null && !registrazioniMovFin.isEmpty())
				registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esiste non devo inserire
			
			Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
			
			List<RegistrazioneMovFin> registrazioniMovFinInserite = new ArrayList<RegistrazioneMovFin>();
			
			ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
			
			// Recupero dall'impegno il pdc da salvare nel registro
			pdc.setUid(movimento.getImpegno().getIdPdc());
			pdc.setCodice(movimento.getImpegno().getCodPdc());
			pdc.setDescrizione(movimento.getImpegno().getDescPdc());
			
			RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc, Ambito.AMBITO_FIN);
			registrazioniMovFinInserite.add(registrazioneMovFin);
			
			if(movimento.getImpegno().isFlagAttivaGsa()){
				RegistrazioneMovFin registrazioneMovFinGsa = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc, Ambito.AMBITO_GSA);
				registrazioniMovFinInserite.add(registrazioneMovFinGsa);
			}
			
			registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInserite);
		
		} catch (Exception e) {
			
			log.error(methodName, " Va in errore la registrazione gen o l'inserimento della prima nota, non faccio arrivate l'errore al client ");
		}
	}
	
	/**
	 * Verifica le condizione per attivare la resgistrazione sul resgistro fin -gen 
	 * @param isImpegnoInPartitoDiGiro
	 * @param impegno
	 * @return
	 */
	protected Boolean isCondizioneDiAttivazioneGENSoddisfatta(boolean isImpegnoInPartitoDiGiro, Impegno impegno , boolean aggiorna) {
		final String methodName = "isCondizioneDiAttivazioneGENSoddisfatta()";
		log.debug(methodName, "- Begin");
		
	
		/**
		 	* Condizioni per Inserisce prima nota:
		 	* Verifico 1) la condizione che determina la condizione di partita di giro (combinazione titolo e macroaggregato), 
		 	* 			  se la classificazione finanziaria è valida procedo ma senza inserire la prima nota  
			* Verifico 2) la condizione non in partita di giro, se la classificazione finanziaria è valida inserisco la prima nota 
		    
		    * La determina è definitiva
		    * 
		    * Deve esserci il soggetto; se non c'è il soggetto ma c'è la classe per effettuare la registrazione verificare che l'mpegno abbia flagGsa = false

		 */
		
		Boolean flagAttivaGenDeterminaDefinitiva = false;
		if(impegno.getAttoAmministrativo()!=null){
			flagAttivaGenDeterminaDefinitiva = impegno.getAttoAmministrativo().getStatoOperativo().equalsIgnoreCase(CostantiFin.ATTO_AMM_STATO_DEFINITIVO);
		}
		
		Boolean	flagAttivaGenSoggettoPresente = true;
		if(!aggiorna){
			flagAttivaGenSoggettoPresente = impegno.getSoggetto()!=null || (!impegno.isFlagAttivaGsa() && impegno.getClasseSoggetto()!=null);
			
			//System.out.println("flagAttivaGenSoggettoPresente: " + flagAttivaGenSoggettoPresente);
		}
		
		Boolean flagAttivaGenClassificazioneFinanziaria = false;
		
		String codiceTitoloSpesa = impegno.getCapitoloUscitaGestione().getTitoloSpesa().getCodice();
		String codiceMacroaggregato = impegno.getCapitoloUscitaGestione().getMacroaggregato().getCodice();
		
		log.debug(methodName, "codiceTitoloSpesa: "+codiceTitoloSpesa + ", codiceMacroaggregato: "+codiceMacroaggregato);
		
		if(verificaClassificazioneFinanziariaInPartitaDiGiro(codiceTitoloSpesa,codiceMacroaggregato)){
			flagAttivaGenClassificazioneFinanziaria = Boolean.TRUE;
			isImpegnoInPartitoDiGiro = true;
		}else flagAttivaGenClassificazioneFinanziaria =  verificaClassificazioneFinanziaria(codiceTitoloSpesa,codiceMacroaggregato);
		
		log.debug(methodName, "flag attivazione - flagAttivaGenClassificazioneFinanziaria: "+flagAttivaGenClassificazioneFinanziaria
				+ ", flagAttivaGenDeterminaDefinitiva: "+flagAttivaGenDeterminaDefinitiva + ", flagAttivaGenSoggettoPresente: "+flagAttivaGenSoggettoPresente);	
		
		
		if(	flagAttivaGenDeterminaDefinitiva && 
					flagAttivaGenSoggettoPresente && flagAttivaGenClassificazioneFinanziaria)
			return Boolean.TRUE;
		
		return Boolean.FALSE;

	}
	
	
	
	protected Boolean isCondizioneDiAttivazioneGENSoddisfatta(Accertamento accertamento) {
		final String methodName = "isCondizioneDiAttivazioneGENSoddisfatta()";
		log.debug(methodName, "- Begin");
		
	
		/**
		 * Condizioni per Inserisce prima nota:
		 * l'operatore ha indicato che l'accertamento NON sarà collegato a una fattura attiva
		 * l'accertamento deve essere definitivo
		 */
		
		
		Boolean flagAttivaGenStatoDefinitivo = false;
		flagAttivaGenStatoDefinitivo = accertamento.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_DEFINITIVO);
		
		return flagAttivaGenStatoDefinitivo  && !(accertamento.isFlagFattura() || accertamento.isFlagCorrispettivo());
	}
	
	
	

	
	/**
	 * Verifica, per la liquidazione, la condizione per attivare la registrazione GEN
	 * @param liquidazione
	 * @return
	 */
	protected Boolean isCondizioneDiAttivazioneGENSoddisfatta(Liquidazione liquidazione) {
		final String methodName = "isCondizioneDiAttivazioneGENSoddisfatta()";
		log.debug(methodName, "- Begin");

		/**
		 * Non si deve operare la scrittura se :
		 * impegno (o subimpegno) già collegato a prima nota
		 * se eventuale quota documento è già con prima nota
		 * la liquidazione è provvisoria
		 */
		Boolean flagAttivaGenImpegnoConPrimaNota = verificaRegistrazioniMovFin(liquidazione.getImpegno(),TipoCollegamento.IMPEGNO);
				
		Boolean flagAttivaGENSuDocumento = Boolean.FALSE;
		
		Boolean flagAttivaGenQuotaDocConPrimaNota = Boolean.FALSE;
		
		if(liquidazione.getSubdocumentoSpesa()!=null){
			
			log.debug(methodName , " la liqudazione ha subdocumenti .. verifico se ha l'atto ");
			log.debug(methodName," Liquidazione.subDocumento.uid: " + liquidazione.getSubdocumentoSpesa().getUid());
			
			
			flagAttivaGenQuotaDocConPrimaNota = verificaRegistrazioniMovFin(liquidazione.getSubdocumentoSpesa(),TipoCollegamento.SUBDOCUMENTO_SPESA);
			
			if(!flagAttivaGenQuotaDocConPrimaNota){
				
				// se non è stata emessa ancora la prima-prima nota sul documento (e doveva!) controllo
				// se il documento doveva andare in gen, testando il flagAttivaGEN, 
				// se flagAttivaGEN è TRUE non registro e non emetto la prima nota per la liquidazione altrimenti si 
				// NOTA: flagAttivaGEN --> il suo valore dipende da come hanno popolato il db, se quell'attributo per quel tipo documento 
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
		
		log.debug(methodName ," flagAttivaGenImpegnoConPrimaNota: " + flagAttivaGenImpegnoConPrimaNota);
		log.debug(methodName ," flagAttivaGenLiquidazioneNonProvvisoria : " + flagAttivaGenLiquidazioneNonProvvisoria);
		log.debug(methodName ," flagAttivaGenQuotaDocConPrimaNota : " + flagAttivaGenQuotaDocConPrimaNota);
		log.debug(methodName ," flagAttivaGENSuDocumento : " + flagAttivaGENSuDocumento);
		
		if(!flagAttivaGenImpegnoConPrimaNota && 
				!flagAttivaGenLiquidazioneNonProvvisoria && 
				!flagAttivaGenQuotaDocConPrimaNota // && 	!flagAttivaGENSuDocumento 
				)
			return Boolean.TRUE;
		
		return Boolean.FALSE;

	}

	private Boolean verificaRegistrazioniMovFin(Entita movimento,TipoCollegamento tipoCollegamento) {
		registrazioneGENServiceHelper.init(serviceExecutor, req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), loginOperazione, bilancio.getAnno());
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
		if(registrazioniMovFin!=null && !registrazioniMovFin.isEmpty()){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * Verifica l'impengo in partita di giro per l'innesto fin - gen 
	 * @return
	 */
	private Boolean verificaClassificazioneFinanziariaInPartitaDiGiro(String codiceTitoloSpesa, String codiceMacroaggregato) {
		final String methodName = "InserisceImpegniService : verificaClassificazioneFinanziariaInPartitaDiGiro()";
		log.debug(methodName, "- Begin");
		
		/**
		 	* Verifica Classificazione finanziaria:
		 	* 
		 	* titolo 4 macro-aggregati 4020000, 4030000, 4040000
		 	* titolo 7 macro-aggregato 7010000
		 	* la determina è definitiva
		 	* Deve esserci il soggetto."


		 */
		
		if(codiceTitoloSpesa.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.TITOLO_7.getCodice()) && codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_7010000.getCodice()) ||
			(codiceTitoloSpesa.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.TITOLO_4.getCodice()) && 
					(codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_4020000.getCodice()) ||
							codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_4030000.getCodice()) ||
							codiceMacroaggregato.equalsIgnoreCase(CodiciControlloInnestiFinGenEnum.MACROAGGREGATO_4040000.getCodice()) 
							))){
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
		
	}
	

	private Boolean verificaClassificazioneFinanziaria(String codiceTitoloSpesa, String codiceMacroaggregato) {
		final String methodName = "InserisceImpegniService : verificaClassificazioneFinanziaria";
		log.debug(methodName, "- Begin");
		
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
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
		
	}
	
	
	

	/**
	 * Annulla la registrazione e la prima nota (se presente) 
	 * 
	 */
	protected void annullaRegistrazioneEPrimaNotaMovimentoGestione(TipoCollegamento tipoCollegamento, MovimentoGestione movimento) {
				
		String methodName = "annullaRegistrazioneEPrimaNotaMovimentoGestione";
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, bilancio.getAnno());
		
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
	protected void gestisciRegistrazioneGENModificheMovimentoSpesa(Impegno impegno, SubImpegno sub, TipoCollegamento tipoCollegamento) {
		
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, bilancio.getAnno());
				
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
				
				if(!movimento.getCodiceStatoOperativoModificaMovimentoGestione().equals("A")){
				
					List<RegistrazioneMovFin> registrazioniMovFinMovimento = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
					
					// jira 3419, non devo piu annullare tutto:
					// quindi se la modifica è già stata registrata non ripeto la registrazione annullando quella precedente 
//					if(registrazioniMovFinMovimento!=null && !registrazioniMovFinMovimento.isEmpty())
//						registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFinMovimento); //se la registrazione esisteva devo annullare le eventuali primeNote associate
					
					if(registrazioniMovFinMovimento == null || registrazioniMovFinMovimento.isEmpty()){
						
						String codiceEvento = definisciCodiceEvento(modificheDiSub, movimento);
						
						// qui aggiungo il controllo in caso di modifica di soggetto, nel caso in cui non siamo in configurazione GSA
						if(codiceEvento.equalsIgnoreCase(CodiceEventoEnum.INSERISCI_MODIFICA_SOGGETTO_SUBIMPEGNO.getCodice())
								|| codiceEvento.equalsIgnoreCase(CodiceEventoEnum.INSERISCI_MODIFICA_SOGGETTO_IMPEGNO.getCodice())){
							if(!impegno.isFlagAttivaGsa()){
								log.debug("gestisciRegistrazioneGENModificheMovimentoSpesa", "Siamo in modifica di soggetto non Gsa, non effettuo alcuna scrittura.");
								//System.out.println("Siamo in modifica di soggetto non Gsa, non effettuo alcuna scrittura. CodiceEvento: " + codiceEvento + " e isFlagAttivaGsa: " + impegno.isFlagAttivaGsa());
								return;
							}
							
						}
						
						
						Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
						List<RegistrazioneMovFin> registrazioniMovFinInserite = new ArrayList<RegistrazioneMovFin>();
						ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
						
						// Recupero dall'impegno il pdc da salvare nel registro
						pdc.setUid(impegno.getIdPdc());
						pdc.setCodice(impegno.getCodPdc());
						pdc.setDescrizione(impegno.getDescPdc());
					
						RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc, Ambito.AMBITO_FIN);
						registrazioniMovFinInserite.add(registrazioneMovFin);
						
						if(impegno.isFlagAttivaGsa()){
							
							RegistrazioneMovFin registrazioneMovFinGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc, Ambito.AMBITO_GSA);
							registrazioniMovFinInserite.add(registrazioneMovFinGSA);
						}
					
						registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInserite);
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
	protected void gestisciRegistrazioneGENModificheMovimentoEntrata(Accertamento accertamento,SubAccertamento sub, TipoCollegamento tipoCollegamento) {
		
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, bilancio.getAnno());
		
		List<ModificaMovimentoGestioneEntrata> elencoModificheMovimento = new ArrayList<ModificaMovimentoGestioneEntrata>();
		List<RegistrazioneMovFin> registrazioniMovFin = new ArrayList<RegistrazioneMovFin>();
		boolean modificheDiSub = false;
		
		if(sub == null){
			//devo registrare le modifiche solo se sull'impegno ci sono registrazioni non annullate 
			registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.ACCERTAMENTO, accertamento); //se presenti ne troverà una per ogni quota, altrimenti 0.
			elencoModificheMovimento = accertamento.getListaModificheMovimentoGestioneEntrata();
		}else{
			registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.SUBACCERTAMENTO, sub); //se presenti ne troverà una per ogni quota, altrimenti 0.
			elencoModificheMovimento = sub.getListaModificheMovimentoGestioneEntrata();
			modificheDiSub = true;
		}
				
				
		// se ne trova è xchè non sono annullate
		if(registrazioniMovFin!=null && !registrazioniMovFin.isEmpty()){
			
			for (ModificaMovimentoGestioneEntrata movimento : elencoModificheMovimento) {
				
				if(!movimento.getCodiceStatoOperativoModificaMovimentoGestione().equals("A")){
	
					List<RegistrazioneMovFin> registrazioniMovFinMovimento = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento); //se presenti ne troverà una per ogni quota, altrimenti 0.
					
//					if(registrazioniMovFinMovimento!=null && !registrazioniMovFinMovimento.isEmpty())
//						registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFinMovimento); //se la registrazione esisteva devo annullare le eventuali primeNote associate
					
					if(registrazioniMovFinMovimento==null || registrazioniMovFinMovimento.isEmpty()){
						
						String codiceEvento = definisciCodiceEvento(modificheDiSub, movimento);
						
						
						// qui aggiungo il controllo in caso di modifica di soggetto, nel caso in cui non siamo in configurazione GSA
						if(codiceEvento.equalsIgnoreCase(CodiceEventoEnum.INSERISCI_MODIFICA_SOGGETTO_SUBACCERTAMENTO.getCodice())
								|| codiceEvento.equalsIgnoreCase(CodiceEventoEnum.INSERISCI_MODIFICA_SOGGETTO_ACCERTAMENTO.getCodice())){
							if(!accertamento.isFlagAttivaGsa()){
								log.debug("gestisciRegistrazioneGENModificheMovimentoEntrata", "Siamo in modifica di soggetto non Gsa, non effettuo alcuna scrittura.");
								//System.out.println("Siamo in modifica di soggetto non Gsa, non effettuo alcuna scrittura. CodiceEvento: " + codiceEvento + " e isFlagAttivaGsa: " + accertamento.isFlagAttivaGsa());
								return;
							}
							
						}
												
						Evento evento = registrazioneGENServiceHelper.determinaEventoMovimentiFinanziaria(tipoCollegamento, codiceEvento);
						List<RegistrazioneMovFin> registrazioniMovFinInserite = new ArrayList<RegistrazioneMovFin>();
						ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
						
						// Recupero dall'impegno il pdc da salvare nel registro
						pdc.setUid(accertamento.getIdPdc());
						pdc.setCodice(accertamento.getCodPdc());
						pdc.setDescrizione(accertamento.getDescPdc());
						
						
						RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc,  Ambito.AMBITO_FIN);
						registrazioniMovFinInserite.add(registrazioneMovFin);
						
						if(accertamento.isFlagAttivaGsa()){
							
							RegistrazioneMovFin registrazioneMovFinGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, movimento , pdc, Ambito.AMBITO_GSA);
							registrazioniMovFinInserite.add(registrazioneMovFinGSA);
						}
						
						registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInserite);
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
	

}
