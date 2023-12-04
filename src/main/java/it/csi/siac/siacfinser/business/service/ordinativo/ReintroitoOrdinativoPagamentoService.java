/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommon.util.threadlocal.ThreadLocalUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.EntitaUtils;
import it.csi.siac.siacfinser.ReintroitoUtils;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.business.service.liquidazione.InserisceLiquidazioneService;
import it.csi.siac.siacfinser.business.service.liquidazione.RicercaLiquidazionePerChiaveService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ReintroitoOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.ReintroitoOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dao.common.dto.AccertamentoModAutomaticaPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.AccertamentoPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImpegnoPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.LiquidazioneScritturaGenInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovGestInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInReintroitoDatiDiInputDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoIncassoScritturaGenInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoPagamentoScritturaGenInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaOrdinativoPerChiaveDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RitenutaSpiltPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RitenuteReintroitoConStessoMovimentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ScritturaGenRitenuteStessoImpegnoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ScrittureGenPerReintroitoOrdinativoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubAccertamentiModAutomaticaPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.integration.utility.threadlocal.UseClockTimeThreadLocal;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.TipoAssociazioneEmissione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.MovimentoKey;
import it.csi.siac.siacfinser.model.ric.OrdinativoKey;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.SoggettoSedeModPagInfo;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReintroitoOrdinativoPagamentoService extends AbstractInserisceAggiornaAnnullaOrdinativoPagamentoService<ReintroitoOrdinativoPagamento, ReintroitoOrdinativoPagamentoResponse> {

	@Autowired
	InserisceOrdinativoPagamentoService inserisceOrdinativoPagamentoService;
	
	@Autowired
	AggiornaOrdinativoPagamentoService aggiornaOrdinativoPagamentoService;
	
	@Autowired
	InserisceLiquidazioneService inserisceLiquidazioneService;
	
	@Autowired
	RicercaLiquidazionePerChiaveService ricercaLiquidazionePerChiaveService;
	
	@Autowired
	RicercaOrdinativoPagamentoPerChiaveService ricercaOrdinativoPagamentoPerChiaveService;
	
	@Autowired
	AnnullaOrdinativoPagamentoService annullaOrdinativoPagamentoService;
	
	@Autowired
	AggiornaStatoDocumentoDiSpesaService aggiornaStatoDocumentoDiSpesaService;
	
	
	private boolean ricaricaLiquidazione = true;
	private boolean ricaricaOrdPagDopoAggiornaCodifiche = true;
	
	private static final UseClockTimeThreadLocal USE_CLOCK_TIME = (UseClockTimeThreadLocal) ThreadLocalUtil.registerThreadLocal(UseClockTimeThreadLocal.class);
	
	@Override
	protected void init() {
		final String methodName = "ReintroitoOrdinativoPagamentoService : init()";
		log.debug(methodName, "- Begin");
	}
	
	@Override
	@Transactional(timeout=600)
	public ReintroitoOrdinativoPagamentoResponse executeService(ReintroitoOrdinativoPagamento serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	@Transactional(timeout=600)
	public ReintroitoOrdinativoPagamentoResponse executeServiceTxRequiresNew(ReintroitoOrdinativoPagamento serviceRequest) {
		return super.executeService(serviceRequest);
	}
		
	@Override
	public void execute() {
		
		//reverto le ottimizzazioni perche' ci sono anora problemi:
		this.ricaricaLiquidazione = false;
		this.ricaricaOrdPagDopoAggiornaCodifiche = false;
		//
		
		
		//Forziamo AbstractFinDad.getCurrentMilliseconds ad utilizzare
		// CLOCK_TIMESTAMP() al posto di CURRENT_TIMESTAMP
		USE_CLOCK_TIME.set(Boolean.TRUE);
		// altrimenti non vede le entita appena create
		
		
		final String methodName = "ReintroitoOrdinativoPagamentoService : execute()";
		log.debug(methodName, " - Begin");
		
		//inizializzo la lista messaggi:
		if(res.getMessaggi()==null){
			List<Errore> listaMessaggi = new ArrayList<Errore>();
			res.setMessaggi(listaMessaggi);
		}
		//
		
		//1. VENGONO LETTI I VALORI RICEVUTI IN INPUT DALLA REQUEST:
		Ente ente = req.getEnte();
		setBilancio(req.getBilancio());
		Richiedente richiedente = req.getRichiedente();
		
		//2. INIZIALIZZO I DATI OPERAZIONE:
		DatiOperazioneDto datiOperazioneDto = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, bilancio.getAnno());
		
		//3. DALLA REQUEST COSTRUISCO UN OrdinativoInReintroitoDatiDiInputDto
		OrdinativoInReintroitoDatiDiInputDto datiInput =  buildOrdinativoInReintroitoDatiDiInputDto();
		//
		
		//4. CARICO E VERIFICO L'ORDINATIVO DI PAGAMENTO INDICATO:
		OrdinativoInReintroitoInfoDto datiReintroito = caricaEVerificaEsistenzaOrdinativoPagamentoPerReintroito(datiInput, datiOperazioneDto);
		if(datiReintroito.presenzaErrori()){
			res.setErrori(datiReintroito.getListaErrori());
			return;
		}
		//
		
		//6. CONTINUO CON I CARICAMENTI E I CONTROLLI SUGLI ALTRI DATI IN INPU:
		datiReintroito = ordinativoPagamentoDad.caricaEVerificaDatiPerReintroitoOrdinativoPagamento(datiInput,datiReintroito, datiOperazioneDto);
		if(datiReintroito.presenzaErrori()){
			res.setErrori(datiReintroito.getListaErrori());
			return;
		}
		//
		
		//7. DEVO DI NUOVO "SPEZZARE" L'ESECUZIONE DEI CONTROLLI SUL DAD E RITORNARE QUI SUL SERVICE
		//PERCHE' DEVO VESTIRE GLI IMPEGNI e GLI ACCERTAMENTI CON I LORO CAPITOLI e PROVVEDIMENTI:
		datiReintroito = completaDatiRicercaMovimentiPk(datiReintroito, datiOperazioneDto);
		//
		
		//8. ORA EFFETTUO I CONTROLLI DI DISPONIBILITA' (per i quali mi serviva conoscere anche i capitoli degli impegni):
		datiReintroito = ordinativoPagamentoDad.controlliDisponibilitaMovimentiPerReintroito(datiReintroito, datiOperazioneDto,richiedente);
		if(datiReintroito.presenzaErrori()){
			res.setErrori(datiReintroito.getListaErrori());
			return;
		}
		
		//9. CONTROLLI COERENZA SOGGETTI:
		datiReintroito = ordinativoPagamentoDad.controlliCoerenzaSoggettiPerReintroito(datiReintroito, datiOperazioneDto);
		if(datiReintroito.presenzaErrori()){
			res.setErrori(datiReintroito.getListaErrori());
			return;
		}
		//
		
		//10. CREAZIONE EVENTUALI MODIFICHE AUTOMATICHE PER GLI ACCERTAMENTI:
		effettuaModificheAutomaticheAccertamenti(datiReintroito,datiOperazioneDto);
		//
		
		//avanzo la data in modo da non restare indietro con le date inizio validita:
		datiOperazioneDto = commonDad.aggiornaTimeStamp(datiOperazioneDto);
		//
		
		//11. OK, ORA POSSO REINTROITARE:
		reintroitoOrdinativoPagamento(datiReintroito, datiOperazioneDto);
		
		//avanzo la data in modo da non restare indietro con le date inizio validita:
		datiOperazioneDto = commonDad.aggiornaTimeStamp(datiOperazioneDto);
		//
		
		//12. RICHIAMIAMO LE SCRITTURE GEN TUTTE AL FONDO:
		scrittureGen(datiReintroito, datiOperazioneDto);
		
	}
	
	
	/**
	 * 
	 * Carica e verifica l'esistenza dell'ordinativo di pagamento in corso di reintroito.
	 * 
	 * @param datiInput
	 * @param datiOperazione
	 * @return
	 * @throws RuntimeException
	 */
	public OrdinativoInReintroitoInfoDto caricaEVerificaEsistenzaOrdinativoPagamentoPerReintroito(OrdinativoInReintroitoDatiDiInputDto datiInput,DatiOperazioneDto datiOperazione) throws RuntimeException {
		
		OrdinativoInReintroitoInfoDto esito = new OrdinativoInReintroitoInfoDto();
		
		Bilancio bilancio = datiInput.getBilancio();
		Richiedente richiedente = datiInput.getRichiedente();
		
		//CARICO L'ORDINATIVO DI PAGAMENTO:
		RicercaOrdinativoPerChiaveDto ordCaricato = caricaOrdinativoPagamentoPerReintroito(datiInput.getOrdinativoPagamento(), bilancio, richiedente, datiOperazione);
		
		// CONTROLLO ESISTENZA SULL'ORD CARICATO
		if(ordCaricato==null || ordCaricato.getOrdinativoPagamento()==null || ordCaricato.getOrdinativoPagamento().getNumero()==null){
			//NON ESISTE
			esito.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Ordinativo pagamento", datiInput.getOrdinativoPagamento().getAnno() + " / " + datiInput.getOrdinativoPagamento().getNumero()));
			return esito;
		}
		
		//COMPLETO I DATI DEL CAPITOLO E PROVVEDIMENTO:
		OrdinativoPagamento ordinativoPagamentoRicaricato = completaDatiCapitoloEProvvedimento(ordCaricato, richiedente);
		//
		
		//OCCORRE NON CONSIDERARE I SUB ORDINATIVI ANNULLATI:
		List<SubOrdinativoPagamento> subOrdNonAnnullati = CommonUtil.rimuoviSubOrdinativiPagAnnullati(ordinativoPagamentoRicaricato.getElencoSubOrdinativiDiPagamento());
		ordinativoPagamentoRicaricato.setElencoSubOrdinativiDiPagamento(subOrdNonAnnullati);
		//
		
		/****      COLLEGATI NEL PRIMO VERSO DA - A ***********************/
		// COMPLETO L'ORDINATIVO CON GLI EVENTUALI ORDINATIVI DI INCASSO COLLEGATI:
		ordinativoPagamentoRicaricato = completaOrdinativiCollegati(ordinativoPagamentoRicaricato, datiOperazione, richiedente);
		//OCCORRE NON CONSIDERARE GLI ORDINATIVI DI INCASSO ANNULLATI:
		List<Ordinativo> ordCollegatiNonAnnullati = CommonUtil.rimuoviOrdinativiAnnullati(ordinativoPagamentoRicaricato.getElencoOrdinativiCollegati());
		//verifichiamo che non sia gia' coinvolto in un reintroito nel primo verso:
		esito = controlloPresenzaReintroito(ordinativoPagamentoRicaricato, ordCollegatiNonAnnullati, esito);
		if(esito.presenzaErrori()){
			return esito;
		}
		//Tolgo eventuali relazioni verso ord pag perche' qui devono esserci solo ord incasso (non dovrebbero esserci
		//ma non si sa mai di eventuali usi futuri):
		List<Ordinativo> ordIncNonAnnullati = CommonUtil.rimuoviOrdinativiDiPagamento(ordCollegatiNonAnnullati);
		ordinativoPagamentoRicaricato.setElencoOrdinativiCollegati(ordIncNonAnnullati);
		//
		
		
		/****      COLLEGATI NEL SECONDO VERSO A - DA ***********************/
		// CONTROLLIAMO QUELLI COLLEGATI NELL'ALTRO VERSO DELLA RELAZIONE DA - A:
		ordinativoPagamentoRicaricato = completaOrdinativiACuiSonoCollegato(ordinativoPagamentoRicaricato, datiOperazione, richiedente);
		//Occorre non considerare gli ordinativi annullati:
		List<Ordinativo> ordACuiSonoCollegato = CommonUtil.rimuoviOrdinativiAnnullati(ordinativoPagamentoRicaricato.getElencoOrdinativiACuiSonoCollegato());
		//verifichiamo che non sia gia' coinvolto in un reintroito nel secondo verso:
		esito = controlloPresenzaReintroito(ordinativoPagamentoRicaricato, ordACuiSonoCollegato, esito);
		if(esito.presenzaErrori()){
			return esito;
		}
		ordinativoPagamentoRicaricato.setElencoOrdinativiACuiSonoCollegato(ordACuiSonoCollegato);
		//
		
		
		//SETTO L'ORDINATIVO PAGAMENTO CARICATO:
		esito.setOrdinativoPagamento(clone(ordinativoPagamentoRicaricato));
		//
		
		return esito;
	}
	
	/**
	 * Verifica tra gli ordinativi collegati da a o a cui e' collegato nell'altro verso a d
	 * non ci sia nessuna relazione di reintroito
	 * @param ordinativoPagamentoRicaricato
	 * @param ordInCollegamento
	 * @param esito
	 */
	private OrdinativoInReintroitoInfoDto controlloPresenzaReintroito(OrdinativoPagamento ordinativoPagamentoRicaricato, List<Ordinativo> ordInCollegamento,OrdinativoInReintroitoInfoDto esito){
		if(CommonUtil.presenteTipoAssociazioneEmissione(ordInCollegamento, TipoAssociazioneEmissione.SOS_ORD)){
			OrdinativoKey ordKey = ReintroitoUtils.toOrdKey(ordinativoPagamentoRicaricato);
			String nomeEntita = ReintroitoUtils.buildNomeEntitaPerMessaggiDiErrore(ordKey, true);
			String codiceEntita = ReintroitoUtils.buildCodiceEntitaPerMessaggiDiErrore(ordKey);
			String msg = "E' gia' coinvolto in una relazione di reintroito di tipo SOS_ORD";
			esito.addErrore(ErroreCore.VALORE_NON_CONSENTITO.getErrore(nomeEntita + ": " + codiceEntita, msg));
		}
		return esito;
	}
	
	private RicercaOrdinativoPerChiaveDto caricaOrdinativoPagamentoPerReintroito(OrdinativoKey ordPag, Bilancio bilancio,Richiedente richiedente, DatiOperazioneDto datiOperazione){
		RicercaOrdinativoPagamentoK pk = new RicercaOrdinativoPagamentoK();
		pk.setBilancio(bilancio);
		OrdinativoPagamento ordinativoPagamento = new OrdinativoPagamento();
		pk.setOrdinativoPagamento(ordinativoPagamento);
		ordinativoPagamento.setAnno(ordPag.getAnno());
		ordinativoPagamento.setNumero(ordPag.getNumero());
		ordinativoPagamento.setAnnoBilancio(ordPag.getAnnoBilancio());
		return ordinativoPagamentoDad.ricercaOrdinativoPagamento(pk, datiOperazione, richiedente);
	}

	private void scrittureGen(OrdinativoInReintroitoInfoDto datiReintroito,DatiOperazioneDto datiOperazioneDto) {
		
		ScrittureGenPerReintroitoOrdinativoInfoDto scrittureGen = datiReintroito.getScrittureGen();
		
		//1. Scrittura gen per la liquidazione inserita:
		scritturaGenLiquidazioneInserita(scrittureGen.getLiquidazionePrincipale());
		//
		
		//2. Ordinativo principale spostato o inserito:
		OrdinativoPagamentoScritturaGenInfoDto ordPagSpostatoScrittGen = scrittureGen.getOrdPagPrincipaleSpostato();
		OrdinativoPagamentoScritturaGenInfoDto nuovoOrdPag = scrittureGen.getNuovoOrdPagPrincipaleInserito();
		
		boolean annullaOrdDopoCicloRitenute = true;
		
		if(ordPagSpostatoScrittGen!=null && ordPagSpostatoScrittGen.getOrdinativoPagamento()!=null){
			OrdinativoPagamento ordPrincipaleSpostato = ordPagSpostatoScrittGen.getOrdinativoPagamento();
			//A. Chiamare la GEN per ANNULLAMENTO ORDINATIVO (OPA-ANN)
			annullaRegistrazioneEPrimaNotaOrdinativo(TipoCollegamento.ORDINATIVO_PAGAMENTO, ordPrincipaleSpostato);
			annullaOrdDopoCicloRitenute = false;
			//B. Chiamare la GEN per INSERIMENTO ORDINATIVO (OPA-INS)
			scritturaGenOrdinativoPagamentoInserito(ordPagSpostatoScrittGen);
		} else if(nuovoOrdPag!=null && nuovoOrdPag.getOrdinativoPagamento()!=null){
			//A. Chiamare la GEN per INSERIMENTO ORDINATIVO (OPA-INS)
			scritturaGenOrdinativoPagamentoInserito(nuovoOrdPag);
		}
		
		//SCRITTURE DELLE RITENUTE RAGGRUPPATE PER STESSO IMPEGNO:
		List<ScritturaGenRitenuteStessoImpegnoInfoDto> raggruppateStessoImp = scrittureGen.getRitenuteRaggruppatePerImpegno();
		if(!StringUtilsFin.isEmpty(raggruppateStessoImp)){
			for(ScritturaGenRitenuteStessoImpegnoInfoDto it: raggruppateStessoImp){
				
				//A. LIQUIDAZIONE INSERITA
				scritturaGenLiquidazioneInserita(it.getLiquidazioneInserita());
				
				//B. ORDINATIVO PAGAMENTO INSERITO
				scritturaGenOrdinativoPagamentoInserito(it.getOrdinativoInserito());
				
			}
		}
		//
		
		//Stando all'analisi in caso di ordinativo con ritenute o con fattura commerciale
		//il richiamo al gen annulla va fatto qui:
		if(annullaOrdDopoCicloRitenute){
			//CONCLUSO il ciclo sulle ritenute
			//	Annullare l'Ordinativo di origine
			OrdinativoPagamento ordOrigine = datiReintroito.getOrdinativoPagamento();
			annullaRegistrazioneEPrimaNotaOrdinativo(TipoCollegamento.ORDINATIVO_PAGAMENTO, ordOrigine);
		}
		
		//SCRITTURE ORDINATIVI INCASSO RITENUTE:
		List<OrdinativoIncassoScritturaGenInfoDto> ordIncassoSpostati = scrittureGen.getOrdinativiIncassoSpostati();
		if(!StringUtilsFin.isEmpty(ordIncassoSpostati)){
			for(OrdinativoIncassoScritturaGenInfoDto it: ordIncassoSpostati){
				
				OrdinativoIncasso ordIncasso = it.getOrdinativoIncasso();
				Accertamento acc = it.getAccertamento();
			
				//A. Chiamare la GEN per ANNULLAMENTO ORDINATIVO (OIN-ANN)
				annullaRegistrazioneEPrimaNotaOrdinativo(TipoCollegamento.ORDINATIVO_INCASSO, ordIncasso);
				
				//B. Chiamare la GEN per INSERIMENTO ORDINATIVO (OIN-INS)
				String codCapitoloSanitario = ordIncasso.getCodCapitoloSanitarioSpesa()!=null ? ordIncasso.getCodCapitoloSanitarioSpesa(): "";
				gestisciRegistrazioneGENPerOrdinativo(ordIncasso,acc, TipoCollegamento.ORDINATIVO_INCASSO, false, false, codCapitoloSanitario);
				
			}
		}
		
	}
	
	private void scritturaGenLiquidazioneInserita(LiquidazioneScritturaGenInfoDto scritturaLiqInserita){
		Liquidazione liquidazioneInserita = scritturaLiqInserita.getLiquidazione();
		Impegno impegnoRicevuto = scritturaLiqInserita.getImpegno();
		gestisciRegistrazioneGENPerLiquidazioneInserita(liquidazioneInserita, impegnoRicevuto, req.getBilancio());
	}
	
	private void scritturaGenOrdinativoPagamentoInserito(OrdinativoPagamentoScritturaGenInfoDto nuovoOrdPag){
		OrdinativoPagamento ordinativoPagamento = nuovoOrdPag.getOrdinativoPagamento();
		Liquidazione liquidazione = nuovoOrdPag.getLiquidazione();
		String codCapitoloSanitario = ordinativoPagamento.getCodCapitoloSanitarioSpesa()!=null ? ordinativoPagamento.getCodCapitoloSanitarioSpesa(): "";
		Impegno imp = liquidazione.getImpegno();
		gestisciRegistrazioneGENPerOrdinativo(ordinativoPagamento, imp , TipoCollegamento.ORDINATIVO_PAGAMENTO, false, imp.isFlagCassaEconomale(), codCapitoloSanitario);
	}

	private void reintroitoOrdinativoPagamento(OrdinativoInReintroitoInfoDto datiReintroito,DatiOperazioneDto datiOperazioneDto) {
		
		boolean presenzaRitenute = datiReintroito.presenzaRitenute();
		
		//1. Inserire una Liquidazione con
			//Importo = 'Netto'
			//impegno indicato per il 'Netto'
			//provvedimento indicato allo step1 se 'Provvedimento Unico' altrimenti si deriva dall'ordinativo da reintroitare
		    //tutti gli altri dati derivati dall'ordinativo da reintroitare
			//l'operazione deve comprendere anche la chiamata alla GEN che carichera' le scritture se ci sono i presupposti

		OrdinativoPagamento ordinativoPagamentoRicaricato = datiReintroito.getOrdinativoPagamento();
		boolean conFatturaCommerciale = conFatturaCommerciale(ordinativoPagamentoRicaricato);
		
		BigDecimal importoNetto = ReintroitoUtils.calcolaImportoNettoPerReintroito(ordinativoPagamentoRicaricato);
		ImpegnoPerReintroitoInfoDto impDestInfo = datiReintroito.getImpegnoDestinazione();
		
		//CREO LA LIQUIDAZONE:
		LiquidazioneScritturaGenInfoDto scritturaGenLiq = creaLiquidazione(datiReintroito, impDestInfo, importoNetto, ordinativoPagamentoRicaricato);
		
		//avanzo la data in modo da non restare indietro con le date inizio validita:
		datiOperazioneDto = commonDad.aggiornaTimeStamp(datiOperazioneDto);
		//
		
		//IMPOSTO I DATI PER LA SCRITTURA GEN DELLA LIQUIDAZIONE INSERITA:
		ScrittureGenPerReintroitoOrdinativoInfoDto scrittureGen = new ScrittureGenPerReintroitoOrdinativoInfoDto();
		scrittureGen.setLiquidazionePrincipale(scritturaGenLiq);
		datiReintroito.setScrittureGen(scrittureGen);
		//
		
		Liquidazione liquidazioneCreata = clone(scritturaGenLiq.getLiquidazione());
		
		//SPOSTA O INSERISCI:
		if(!presenzaRitenute && !conFatturaCommerciale){
			//Sposto l'ordinativo di pagamento:
			OrdinativoPagamentoScritturaGenInfoDto ordSpostatoGen = spostaOrdinativoPagamento(ordinativoPagamentoRicaricato,liquidazioneCreata,datiOperazioneDto);
			datiReintroito.getScrittureGen().setOrdPagPrincipaleSpostato(ordSpostatoGen);
			
			//avanzo la data in modo da non restare indietro con le date inizio validita:
			datiOperazioneDto = commonDad.aggiornaTimeStamp(datiOperazioneDto);
			//
			
			//devo aggiornarlo con le codifiche prese dalla nuova liquidazione:
			ordinativoPagamentoRicaricato = aggiornaCodificheOrdinativoPagamento(ordinativoPagamentoRicaricato, liquidazioneCreata, datiOperazioneDto);
			
			/*
			
			//avanzo la data in modo da non restare indietro con le date inizio validita:
			datiOperazioneDto = commonDad.aggiornaTimeStamp(datiOperazioneDto);
			//
			
			//Devo aggregare tutti i sub ordinativi in uno unico con la somma degli importi
			//e privo di documenti:
			aggiornaSubOrdinativiPagamento(ordinativoPagamentoRicaricato, datiOperazioneDto);
			
			*/
			
		} else {
			OrdinativoPagamentoScritturaGenInfoDto ordCreatoGen = inserisciNuovoOrdinativoPagamento(ordinativoPagamentoRicaricato, liquidazioneCreata, datiOperazioneDto);
			datiReintroito.getScrittureGen().setNuovoOrdPagPrincipaleInserito(ordCreatoGen);
		}
		//
		
		//avanzo la data in modo da non restare indietro con le date inizio validita:
		datiOperazioneDto = commonDad.aggiornaTimeStamp(datiOperazioneDto);
		//
		
		if(presenzaRitenute || conFatturaCommerciale){
			
			//2. PAGAMENTI per RITENUTE (se ci sono)
			if(presenzaRitenute){
				reintroitoPagamentiRitenute(datiReintroito, datiOperazioneDto);
			}
			
			//CONCLUSO il ciclo sulle ritenute
			//	Annullare l'Ordinativo di origine
			annullaOrdinativoOriginale(datiReintroito);
			
			//avanzo la data in modo da non restare indietro con le date inizio validita:
			datiOperazioneDto = commonDad.aggiornaTimeStamp(datiOperazioneDto);
			//
			
			//rimuovo anche i documenti dall'ordinativo origine:
			rimuoviDocumentiSpesaPerReintroito(datiReintroito, datiOperazioneDto);
			
			//3. INCASSI per RITENUTE (se ci sono)
			if(presenzaRitenute){
				reintroitoIncassiRitenute(datiReintroito,datiOperazioneDto);
			}
			
		}
		
	}
	
	private void rimuoviDocumentiSpesaPerReintroito(OrdinativoInReintroitoInfoDto datiReintroito, DatiOperazioneDto datiOperazioneDto){
		OrdinativoPagamento ordinativoOrigine = datiReintroito.getOrdinativoPagamento();
		
		//RIMUOVO IL LEGAME:
		ordinativoPagamentoDad.rimuoviDocumentiSpesaPerReintroito(ordinativoOrigine, datiOperazioneDto);
		
		//AGGIORNO STATO DOCUMENTI:
		aggiornaStatoDocumentiDiSpesa(ordinativoOrigine);
	}
	
	private void addMessaggioOperazioneEffettuata(String operazione, String codiceOggettoCreato){
		Errore msg = ErroreFin.OPERAZIONE_EFFETTUATA.getErrore(operazione, codiceOggettoCreato);
		res.getMessaggi().add(msg);
	}
	
	private RicercaLiquidazionePerChiave buildRequestRicercaLiquidazione(Liquidazione liquidazioneAppenaCreata) {
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		
		RicercaLiquidazionePerChiave ricercaRequest = new RicercaLiquidazionePerChiave();
		
		ricercaRequest.setDataOra(getNow());
		ricercaRequest.setEnte(ente);
		RicercaLiquidazioneK pRicercaLiquidazioneK = new RicercaLiquidazioneK();
		Liquidazione liquidazione = new Liquidazione();
		liquidazione.setAnnoLiquidazione(liquidazioneAppenaCreata.getAnnoLiquidazione());
		liquidazione.setNumeroLiquidazione(liquidazioneAppenaCreata.getNumeroLiquidazione());
		pRicercaLiquidazioneK.setLiquidazione(liquidazione );
		pRicercaLiquidazioneK.setTipoRicerca(CostantiFin.TIPO_RICERCA_DA_ORDINATIVO);
		pRicercaLiquidazioneK.setAnnoEsercizio(bilancio.getAnno());
		ricercaRequest.setpRicercaLiquidazioneK(pRicercaLiquidazioneK);
		ricercaRequest.setRichiedente(richiedente);
		
		return ricercaRequest;
	}
	
	private RicercaOrdinativoPagamentoPerChiave buildRequestRicercaOrdPag(OrdinativoPagamento ordinativoDaCaricare) {
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		RicercaOrdinativoPagamentoPerChiave ricercaRequest = new RicercaOrdinativoPagamentoPerChiave();
		RicercaOrdinativoPagamentoK ordRicercaK = new RicercaOrdinativoPagamentoK();
		ordRicercaK.setOrdinativoPagamento(new OrdinativoPagamento());
		ordRicercaK.getOrdinativoPagamento().setNumero(ordinativoDaCaricare.getNumero());
		ordRicercaK.getOrdinativoPagamento().setAnno(ordinativoDaCaricare.getAnno());
		ordRicercaK.setBilancio(bilancio);
		ricercaRequest.setDataOra(getNow());
		ricercaRequest.setRichiedente(richiedente);
		ricercaRequest.setEnte(ente);
		ricercaRequest.setpRicercaOrdinativoPagamentoK(ordRicercaK);
		return ricercaRequest;
	}
	
	/**
	 * Per ricaricare una liquidazione, lo uso dopo averla creata o modificata per essere sicuro di avere 
	 * tutti i dati aggiornati.
	 * 
	 * @param liquidazioneAppenaCreata
	 * @return
	 */
	private Liquidazione ricercaLiquidazionePerChiave(Liquidazione liquidazioneAppenaCreata) {
		
		//costruisco la request:
		RicercaLiquidazionePerChiave ricercaRequest = buildRequestRicercaLiquidazione(liquidazioneAppenaCreata);
		
		//invoco il servizio:
		RicercaLiquidazionePerChiaveResponse esito = ricercaLiquidazionePerChiaveService.executeService(ricercaRequest);
		
		//DEVO VERIFICARE CHE LA LIQUIDAZIONE SIA STATA CREATA,
		//IN CASO CONTRARIO SI DEVE ASSOLUTAMENT RILANCIARE UN'ECCEZIONE
		//PER FARE ROLLBACKARE TUTTO:
		if(esito==null){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("ricercaLiquidazionePerChiaveService: esito nullo"));
		} else if(!StringUtilsFin.isEmpty(esito.getErrori()) && !Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(esito.getErrori().get(0));
		} else if(esito.getLiquidazione()==null || !CommonUtil.maggioreDiZero(esito.getLiquidazione().getNumeroLiquidazione())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("ricercaLiquidazionePerChiaveService: esito privo di liquidazione"));
		}  else if(!Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("ricercaLiquidazionePerChiaveService: esito negativo"));
		}
				
		return esito.getLiquidazione();
	}
	
	/**
	 * Per ricaricare un ordinativo di pagamneto, lo uso per essere sicuro di avere 
	 * tutti i dati aggiornati.
	 * 
	 * @param liquidazioneAppenaCreata
	 * @return
	 */
	private OrdinativoPagamento ricercaOrdinativoPagamentoPerChiave(OrdinativoPagamento ordinativoDaCaricare) {
		
		//costruisco la request:
		RicercaOrdinativoPagamentoPerChiave ricercaRequest = buildRequestRicercaOrdPag(ordinativoDaCaricare);
		
		//invoco il servizio:
		RicercaOrdinativoPagamentoPerChiaveResponse esito = ricercaOrdinativoPagamentoPerChiaveService.executeService(ricercaRequest);
		
		//DEVO VERIFICARE CHE L'ORDINATIVO SIA STATO CARICATO,
		//IN CASO CONTRARIO SI DEVE ASSOLUTAMENT RILANCIARE UN'ECCEZIONE
		//PER FARE ROLLBACKARE TUTTO:
		if(esito==null){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("ricercaOrdinativoPagamentoPerChiave: esito nullo"));
		} else if(!StringUtilsFin.isEmpty(esito.getErrori()) && !Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(esito.getErrori().get(0));
		} else if(esito.getOrdinativoPagamento()==null || !CommonUtil.maggioreDiZero(esito.getOrdinativoPagamento().getNumero())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("ricercaOrdinativoPagamentoPerChiave: esito privo di liquidazione"));
		}  else if(!Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("ricercaOrdinativoPagamentoPerChiave: esito negativo"));
		}
				
		return esito.getOrdinativoPagamento();
	}

	private void reintroitoPagamentiRitenute(OrdinativoInReintroitoInfoDto datiReintroito, DatiOperazioneDto datiOperazioneDto){
		//SE sono presenti righe di ritenute SOMMARE raggruppandole per "IMPEGNO DESTINAZIONE" e per ogni gruppo:
		List<RitenuteReintroitoConStessoMovimentoDto> raggruppatePerImp = datiReintroito.raggruppatePerImpegni();
		
		if(!StringUtilsFin.isEmpty(raggruppatePerImp)){
			
			//predispongo i dto per le scritture gen:
			List<ScritturaGenRitenuteStessoImpegnoInfoDto> scrittureStessoImp = new ArrayList<ScritturaGenRitenuteStessoImpegnoInfoDto>();
			//
			
			OrdinativoPagamento ordinativoOrigine = datiReintroito.getOrdinativoPagamento();
			
			for(RitenuteReintroitoConStessoMovimentoDto conStessoImp : raggruppatePerImp){
				
				if(conStessoImp!=null){
					ScritturaGenRitenuteStessoImpegnoInfoDto scritturaIterata = new ScritturaGenRitenuteStessoImpegnoInfoDto();
					
					List<RitenutaSpiltPerReintroitoInfoDto> ritenuteConQuestoImp = conStessoImp.getListaRitenute();
					
					//sicuramente ritenuteConQuestoImp ha almeno un elemento:
					//(se non dovesse averlo e' giusto che scoppi tutto in quanto sarebbe una situazione incoerente)
					ImpegnoPerReintroitoInfoDto impegnoInfoIt = ritenuteConQuestoImp.get(0).getImpegno();
					//
					
					// Inserire una Liquidazione con:
					//	Importo = SOMMATORIA "Ritenute"
					//	impegno del raggruppamento
					//	provvedimento indicato allo step1 se "Provvedimento Unico" altrimenti si deriva dall'ordinativo da reintroitare
					//	tutti gli altri dati derivati dall'ordinativo di origine
					//	l'operazione deve comprendere anche la chiamata alla GEN che carichera' le scritture se ci sono i presupposti

					BigDecimal sommatoriaRitenute = ReintroitoUtils.sommaImportiOrdinativiIncasso(ritenuteConQuestoImp);
					
					LiquidazioneScritturaGenInfoDto scrittLiq = creaLiquidazione(datiReintroito, impegnoInfoIt, sommatoriaRitenute, ordinativoOrigine);
					
					//GEN PER LIQUIDAZIONE INSERITA:
					scritturaIterata.setLiquidazioneInserita(scrittLiq);
					//
					
					
					//	Inserire un ordinativo di pagamento con:
					//	liquidazione appena creata 
					//	derivando i dati descrittivi dall'ordinativo di origine
					//	una relazione SOS_ORD che colleghi l'ordinativo originale (da) a quello appena inserito (a)
					//	L'operazione deve comprendere anche la chiamata alla GEN che carichera' la scrittura OPA-INS

					Liquidazione nuovaLiquidazione = clone(scrittLiq.getLiquidazione());
					OrdinativoPagamentoScritturaGenInfoDto scritturaOrdPag = inserisciNuovoOrdinativoPagamento(ordinativoOrigine, nuovaLiquidazione, datiOperazioneDto);
					
					//GEN PER ORDINATIVO PAG INSERITO:
					scritturaIterata.setOrdinativoInserito(scritturaOrdPag);
					//
				
					//aggiungo alla lista scritture:
					scrittureStessoImp.add(scritturaIterata);
				}
			}
			
			//SETTO LE SCRITTURE GEN:
			datiReintroito.getScrittureGen().setRitenuteRaggruppatePerImpegno(scrittureStessoImp);
			
		}
	}
	
	
	private void annullaOrdinativoOriginale(OrdinativoInReintroitoInfoDto datiReintroito){
		
		OrdinativoPagamento ordinativoOrigine = datiReintroito.getOrdinativoPagamento();
		
		//CONCLUSO il ciclo sulle ritenute
		//	Annullare l'Ordinativo di origine
		//	L'operazione deve comprendere anche la chiamata alla GEN che carichera' la scrittura OPA-ANN
		
		//costruisco request:
		AnnullaOrdinativoPagamento annullaReq = buildRequestAnnullaOrdPag(ordinativoOrigine);
		//richiamo annulla:
		annullaOrdinativoPagamento(annullaReq);
		
		//aggiungo il messaggio di ordinativo annullato:
		String messaggio = "Ord annullato: " + riferimento(ordinativoOrigine);
		addMessaggioOperazioneEffettuata("annullato ordinativo pagamento", messaggio);
		//

		//GEN: verra' fatto a fine esecuzione
	}
	
	private void reintroitoIncassiRitenute(OrdinativoInReintroitoInfoDto datiReintroito,DatiOperazioneDto datiOperazioneDto) {
		
		//Per ogni Ordinativo di incasso presente:
		//	Spostare l'ordinativo di Incasso come segue:
		//	Chiamare la GEN per ANNULLAMENTO ORDINATIVO (OIN-ANN)
		//	Scollegare l'ordinativo dall'accertamento
		//	Scollegare l'ordinativo dal capitolo
		//	Scollegare l'ordinativo dalle classificazioni finanziarie
		//	Collegare nuovamente l'ordinativo all'accertamento indicato e conseguentemente al suo capitolo e alle sue classificazioni finanziarie 
		//	Aggiornare la DATA SPOSTAMENTO
		//	Chiamare la GEN per INSERIMENTO ORDINATIVO (OIN-INS)
		
		List<RitenutaSpiltPerReintroitoInfoDto> righe = datiReintroito.getListaRitenuteSplit();
		
		if(!StringUtilsFin.isEmpty(righe)){
			
			//predispongo i dto per le scritture gen:
			List<OrdinativoIncassoScritturaGenInfoDto> scrittureOrdInc = new ArrayList<OrdinativoIncassoScritturaGenInfoDto>();
			//
			
			for(RitenutaSpiltPerReintroitoInfoDto rigaIt: righe){
				
				OrdinativoIncassoScritturaGenInfoDto scritturaGenIt = new OrdinativoIncassoScritturaGenInfoDto();
				OrdinativoIncasso ordinativoIncassoRicaricato = rigaIt.getOrdinativoIncasso();
				
				//   Scollegare l'ordinativo dall'accertamento
				//	 Collegare nuovamente l'ordinativo all'accertamento indicato
				AccertamentoPerReintroitoInfoDto nuovoAcc = rigaIt.getAccertamento();
				ordinativoIncassoDad.spostaOrdinativoIncassoPerReintroito(ordinativoIncassoRicaricato, nuovoAcc, datiOperazioneDto);
				
				//3. Dati per scritture gen:
				scritturaGenIt.setOrdinativoIncasso(ordinativoIncassoRicaricato);
				scritturaGenIt.setAccertamento(nuovoAcc.getAccertamento());
				
				//aggiungo alla lista scritture:
				scrittureOrdInc.add(scritturaGenIt);
				
				//aggiungo il messaggio di ordinativo incasso spostato:
				String messaggio = "Ord: " + riferimento(ordinativoIncassoRicaricato) + " spostato su accertamento: " + riferimento(nuovoAcc.getAccertamento());
				addMessaggioOperazioneEffettuata("spostato ordinativo incasso", messaggio);
				//
			}
			
			//SETTO LE SCRITTURE GEN:
			datiReintroito.getScrittureGen().setOrdinativiIncassoSpostati(scrittureOrdInc);
			
		}

	}
	

	/**
	 * Costruisce la request per annullare un ordinativo di pagamento
	 * @param ordinativoPagamentoDaAnnullare
	 * @return
	 */
	private AnnullaOrdinativoPagamento buildRequestAnnullaOrdPag(OrdinativoPagamento ordinativoPagamentoDaAnnullare){
		
		Ente ente = req.getEnte();
		Richiedente richiedente = req.getRichiedente();
		Bilancio bilancio = req.getBilancio();
		
		//istanzio la request per il servizio annullaOrdinativoPagamento:
		AnnullaOrdinativoPagamento annullaOrdinativoPagamento=new AnnullaOrdinativoPagamento();
		
		//setto i dati nella request:
		OrdinativoPagamento ordinativoPagamento=new OrdinativoPagamento();
		ordinativoPagamento.setAnno(ordinativoPagamentoDaAnnullare.getAnno());
		ordinativoPagamento.setNumero(ordinativoPagamentoDaAnnullare.getNumero());
		annullaOrdinativoPagamento.setOrdinativoPagamentoDaAnnullare(ordinativoPagamento);
		annullaOrdinativoPagamento.setEnte(ente);
		annullaOrdinativoPagamento.setRichiedente(richiedente);
		annullaOrdinativoPagamento.setBilancio(bilancio);
		
		return annullaOrdinativoPagamento;
	}
	
	/***
	 * Richiama il metodo di inserimento della liquidazione
	 * @param ente
	 * @param richiedente
	 * @param annoEsercizio
	 * @param bilancio
	 * @param liquidazioneInput
	 * @param datiOperazione
	 * @return
	 */
	/*private EsitoGestioneLiquidazioneDto inserisciLiquidazione(Ente ente, Richiedente richiedente, String annoEsercizio, Bilancio bilancio,
			Liquidazione liquidazioneInput, DatiOperazioneDto datiOperazioneDto){
		//INVOCO IL METRO CENTRALIZZATO DI CREAZIONE LIQUIDAZIONE:
		EsitoGestioneLiquidazioneDto esito = liquidazioneDad.operazioneInternaInserisciLiquidazione(ente, 
						richiedente, annoEsercizio, bilancio, liquidazioneInput , 0, false,datiOperazioneDto);
		//DEVO VERIFICARE CHE LA LIQUIDAZIONE SIA STATA CREATA,
		//IN CASO CONTRARIO SI DEVE ASSOLUTAMENT RILANCIARE UN'ECCEZIONE
		//PER FARE ROLLBACKARE TUTTO:
		if(esito==null){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("operazioneInternaInserisciLiquidazione: esito nullo"));
		} else if(!StringUtils.isEmpty(esito.getListaErrori())){
			throw new BusinessException(esito.getListaErrori().get(0));
		} else if(esito.getLiquidazione()==null || !CommonUtil.maggioreDiZero(esito.getLiquidazione().getNumeroLiquidazione())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("operazioneInternaInserisciLiquidazione: esito privo di liquidazione"));
		}
		return esito;
	}*/
	
	/**
	 * Richiama il metodo di inserimento della liquidazione
	 * @param insLiqReq
	 * @return
	 */
	private InserisceLiquidazioneResponse inserisciLiquidazione(InserisceLiquidazione insLiqReq){
		//inibisco le scritture gen dentro al servizio (le facciamo da qui alla fine se tutto e' andato bene):
		insLiqReq.setRegistraGen(false);
		//
		//INVOCO IL METRO CENTRALIZZATO DI CREAZIONE LIQUIDAZIONE:
		 InserisceLiquidazioneResponse esito = inserisceLiquidazioneService.executeService(insLiqReq);
		//DEVO VERIFICARE CHE LA LIQUIDAZIONE SIA STATA CREATA,
		//IN CASO CONTRARIO SI DEVE ASSOLUTAMENT RILANCIARE UN'ECCEZIONE
		//PER FARE ROLLBACKARE TUTTO:
		if(esito==null){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("inserisciLiquidazione: esito nullo"));
		} else if(!StringUtilsFin.isEmpty(esito.getErrori()) && !Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(esito.getErrori().get(0));
		} else if(esito.getLiquidazione()==null || !CommonUtil.maggioreDiZero(esito.getLiquidazione().getNumeroLiquidazione())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("inserisciLiquidazione: esito privo di liquidazione"));
		}  else if(!Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("inserisciLiquidazione: esito negativo"));
		}
		return esito;
	}
	
	/**
	 * Richiama il metodo di inserimento dell'ordinativo
	 * @param insOrdPagReq
	 * @return
	 */
	private InserisceOrdinativoPagamentoResponse inserisciOrdinativoPagamento(InserisceOrdinativoPagamento insOrdPagReq){
		//inibisco le scritture gen dentro al servizio (le facciamo da qui alla fine se tutto e' andato bene):
		insOrdPagReq.setRegistraGen(false);
		//
		//INVOCO IL METRO CENTRALIZZATO DI CREAZIONE ORDINATIVO:
		InserisceOrdinativoPagamentoResponse esito = inserisceOrdinativoPagamentoService.executeService(insOrdPagReq);
		//DEVO VERIFICARE CHE L'ORDINATIVO SIA STATO CREATO,
		//IN CASO CONTRARIO SI DEVE ASSOLUTAMENT RILANCIARE UN'ECCEZIONE
		//PER FARE ROLLBACKARE TUTTO:
		if(esito==null){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("inserisceOrdinativoPagamentoService: esito nullo"));
		} else if(!StringUtilsFin.isEmpty(esito.getErrori()) && !Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(esito.getErrori().get(0));
		} else if(esito.getOrdinativoPagamentoInserito()==null || !CommonUtil.maggioreDiZero(esito.getOrdinativoPagamentoInserito().getNumero())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("inserisceOrdinativoPagamentoService: esito privo di ordinativo"));
		}  else if(!Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("inserisceOrdinativoPagamentoService: esito negativo"));
		}
		return esito;
	}
	
	/**
	 * Richiama il metodo di inserimento dell'ordinativo
	 * @param insOrdPagReq
	 * @return
	 */
	private AggiornaOrdinativoPagamentoResponse aggiornaOrdinativoPagamento(AggiornaOrdinativoPagamento aggOrdPagReq){
		//inibisco le scritture gen dentro al servizio (le facciamo da qui alla fine se tutto e' andato bene):
		aggOrdPagReq.setRegistraGen(false);
		//
		//INVOCO IL METRO CENTRALIZZATO DI CREAZIONE ORDINATIVO:
		AggiornaOrdinativoPagamentoResponse esito = aggiornaOrdinativoPagamentoService.executeService(aggOrdPagReq);
		//DEVO VERIFICARE CHE L'ORDINATIVO SIA STATO AGGIORNAT,
		//IN CASO CONTRARIO SI DEVE ASSOLUTAMENT RILANCIARE UN'ECCEZIONE
		//PER FARE ROLLBACKARE TUTTO:
		if(esito==null){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("aggiornaOrdinativoPagamentoService: esito nullo"));
		} else if(!StringUtilsFin.isEmpty(esito.getErrori()) && !Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(esito.getErrori().get(0));
		} else if(esito.getOrdinativoPagamentoAggiornato()==null || !CommonUtil.maggioreDiZero(esito.getOrdinativoPagamentoAggiornato().getNumero())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("aggiornaOrdinativoPagamentoService: esito privo di ordinativo"));
		}  else if(!Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("aggiornaOrdinativoPagamentoService: esito negativo"));
		}
		return esito;
	}
	
	private AnnullaOrdinativoPagamentoResponse annullaOrdinativoPagamento(AnnullaOrdinativoPagamento annullaReq){
		//inibisco le scritture gen dentro al servizio (le facciamo da qui alla fine se tutto e' andato bene):
		annullaReq.setRegistraGen(false);
		//
		//INVOCO IL METRO CENTRALIZZATO DI ANNULLAMENTO ORDINATIVO:
		AnnullaOrdinativoPagamentoResponse esito = annullaOrdinativoPagamentoService.executeService(annullaReq);
		//DEVO VERIFICARE CHE L'ORDINATIVO SIA STATO ANNULLATO CORRETTAMENTE,
		//IN CASO CONTRARIO SI DEVE ASSOLUTAMENT RILANCIARE UN'ECCEZIONE
		//PER FARE ROLLBACKARE TUTTO:
		if(esito==null){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("annullaOrdinativoPagamentoService: esito nullo"));
		} else if(!StringUtilsFin.isEmpty(esito.getErrori()) && !Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(esito.getErrori().get(0));
		} else if(!Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("annullaOrdinativoPagamentoService: esito negativo"));
		}
		return esito;
	}
	
	private boolean conFatturaCommerciale(OrdinativoPagamento ordinativoPagamentoRicaricato) {
		//   L'ordinativo non e' collegato a documenti oppure 
		//   i documenti a cui e' collegato NON sono di tipo commerciale
		//   e NON sono documenti "di servizio": ALG, DSP, CCN
		//List<SubOrdinativoPagamento> elencoSubOrd = ordinativoPagamentoRicaricato.getElencoSubOrdinativiDiPagamento();
		
		boolean commerciale = false;
		
		/*
		String codiceTipoDebito = null;
		if(ordinativoPagamentoRicaricato.getSiopeTipoDebito()!=null){
			codiceTipoDebito = ordinativoPagamentoRicaricato.getSiopeTipoDebito().getCodice();
		}
		if(CostantiFin.SIOPE_CODE_COMMERCIALE.equals(codiceTipoDebito)){
			commerciale = true;
		}
		*/
		
		List<SubOrdinativoPagamento> elencoSubOrd = ordinativoPagamentoRicaricato.getElencoSubOrdinativiDiPagamento();
		if(!StringUtilsFin.isEmpty(elencoSubOrd)){
			for(SubOrdinativoPagamento itSubOrd: elencoSubOrd){
				if(itSubOrd!=null &&
						itSubOrd.getSubDocumentoSpesa()!=null &&
						itSubOrd.getSubDocumentoSpesa().getDocumento()!=null){
					TipoDocumento tipoDoc = itSubOrd.getSubDocumentoSpesa().getDocumento().getTipoDocumento();
					if(tipoDoc.isFattura() || tipoDoc.isNotaCredito()){
						//SE IL TIPO FA PARTE DEL GRUPPO FAT o NCD RITORNO TRUE:
						return true;
					}
					
				}
			}
		}
		
		return commerciale;
	}
	
	
	/**
	 * Questo metodo implementa questo passo di analisi:
	 * 
	 * ALTRIMENTI (ci sono ritenute o l'ordinativo e' collegato a un documento di tipo Commerciale)
	 *	Inserire un ordinativo di pagamento con:
	 *	liquidazione appena creata 
	 *	derivando i dati descrittivi dall'ordinativo di origine
	 *	una relazione SOS_ORD che colleghi l'ordinativo originale (da) a quello appena inserito (a)
	 *	L'operazione deve comprendere anche la chiamata alla GEN che carichera' la scrittura OPA-INS
	 * 
	 * 
	 */
	private OrdinativoPagamentoScritturaGenInfoDto inserisciNuovoOrdinativoPagamento(OrdinativoPagamento ordinativoPagamentoOriginale, Liquidazione nuovaLiquidazione, DatiOperazioneDto datiOperazioneDto){
		
		OrdinativoPagamentoScritturaGenInfoDto scritturaGen = new OrdinativoPagamentoScritturaGenInfoDto();
		
		//1. Inserire un ordinativo di pagamento con:
			//liquidazione appena creata 
			//derivando i dati descrittivi dall'ordinativo di origine
		InserisceOrdinativoPagamento insOrdPagReq = buildOrdinativoPagamentoPerInserimento(ordinativoPagamentoOriginale, nuovaLiquidazione, datiOperazioneDto);
		InserisceOrdinativoPagamentoResponse esitoInserimento = inserisciOrdinativoPagamento(insOrdPagReq);
		OrdinativoPagamento ordinativoCreato = esitoInserimento.getOrdinativoPagamentoInserito();
		
		//aggiungo il messaggio di ordinativo spostato:
		String messaggio = "Ord creato: " + riferimento(ordinativoCreato);
		addMessaggioOperazioneEffettuata("creato ordinativo pagamento", messaggio);
		//
		
		//2. Creare una relazione SOS_ORD che colleghi l'ordinativo originale (da) a quello appena inserito (a)
		ordinativoPagamentoDad.collegaDueOrdinativi(ordinativoPagamentoOriginale, ordinativoCreato, TipoAssociazioneEmissione.SOS_ORD, datiOperazioneDto);
		
		//3. L'operazione deve comprendere anche la chiamata alla GEN che carichera' la scrittura OPA-INS
		scritturaGen.setOrdinativoPagamento(ordinativoCreato);
		scritturaGen.setLiquidazione(nuovaLiquidazione);
		
		return scritturaGen;
	}
	
	private LiquidazioneScritturaGenInfoDto creaLiquidazione(OrdinativoInReintroitoInfoDto datiReintroito,
			ImpegnoPerReintroitoInfoDto impDestInfo, BigDecimal importo, OrdinativoPagamento ordinativoPagamentoRicaricato){
		
		InserisceLiquidazione insLiqReq = buildLiquidazioneFromImpegno(impDestInfo, importo,ordinativoPagamentoRicaricato,datiReintroito);
		
		//forzo il caricamento completo:
		//insLiqReq.setCaricaDatiLiquidazione(true);
		//
		
		//RICHIAMO IL SERVIZIO INSERISCI LIQUIDAZIONE:
		Liquidazione liqInputInserisci = clone(insLiqReq.getLiquidazione());
		InserisceLiquidazioneResponse respInsLiq = inserisciLiquidazione(insLiqReq);
		//
		
		Liquidazione liquidazioneAppenaCreata = respInsLiq.getLiquidazione();
		
		//RICARICO LA LIQUIDAZIONE PER AVERE DATI SICURAMENTE COERENTI:
		//Liquidazione liquidazioneCreataReload = liquidazioneAppenaCreata;
		
		Liquidazione liquidazioneCreataReload = null;
		
		if(this.ricaricaLiquidazione){
			liquidazioneCreataReload = ricercaLiquidazionePerChiave(liquidazioneAppenaCreata);
		} else {
			//Proviamo ad ottimizzare evitando di 
			//richiamare il caricamento della liquidazione per chiave
			//e impostando direttamente i campi mancanti dall'output dell'inserimento:
			
			liquidazioneCreataReload = liqInputInserisci;
			liquidazioneCreataReload.setAnnoLiquidazione(liquidazioneAppenaCreata.getAnnoLiquidazione());
			liquidazioneCreataReload.setNumeroLiquidazione(liquidazioneAppenaCreata.getNumeroLiquidazione());
			liquidazioneCreataReload.setUid(liquidazioneAppenaCreata.getUid());
			liquidazioneCreataReload.setCapitoloUscitaGestione(impDestInfo.getImpegno().getCapitoloUscitaGestione());
			liquidazioneCreataReload.setStatoOperativoLiquidazione(liquidazioneAppenaCreata.getStatoOperativoLiquidazione());
			
			//puntualmente carichiamo i soli dati mancanti:
			Liquidazione datiMancanti = liquidazioneDad.completaDatiLiquidazionePerReintroiti(liquidazioneAppenaCreata.getUid());
			
			liquidazioneCreataReload.setCodicePdc(datiMancanti.getCodicePdc());
			liquidazioneCreataReload.setIdPdc(datiMancanti.getIdPdc());
			liquidazioneCreataReload.setIdSiope(datiMancanti.getIdSiope());
			
			/*
			liquidazioneCreataReload = liquidazioneAppenaCreata;
			liquidazioneCreataReload.setCodSiope(liqInputInserisci.getCodSiope());
			liquidazioneCreataReload.setCodPdc(liqInputInserisci.getCodSiope());liqInputInserisci.getCig()
			liquidazioneCreataReload.setCapitoloUscitaGestione(impDestInfo.getImpegno().getCapitoloUscitaGestione());
			
			liquidazioneCreataReload.setSiopeAssenzaMotivazione(liqInputInserisci.getSiopeAssenzaMotivazione());
			liquidazioneCreataReload.setSiopeTipoDebito(liqInputInserisci.getSiopeTipoDebito());
			liquidazioneCreataReload.setCig(liqInputInserisci.getCig());
			liquidazioneCreataReload.setCup(liqInputInserisci.getCup());
			*/
		}
		
		
		//IMPOSTO I DATI PER LA SCRITTURA GEN DELLA LIQUIDAZIONE INSERITA:
		LiquidazioneScritturaGenInfoDto scritturaGenLiq = new LiquidazioneScritturaGenInfoDto();
		scritturaGenLiq.setLiquidazione(liquidazioneCreataReload);
		scritturaGenLiq.setImpegno(impDestInfo.getImpegno());
		//
		
		//aggiungo il messaggio di liquidazione creata:
		addMessaggioOperazioneEffettuata("inserimento liquidazione", "Liq creata: " +riferimento(liquidazioneCreataReload));
		//
		
		return scritturaGenLiq;
	}
	
	/**
	 * Costruisce un ordinativo di pagamento pronto per l'inserimento a partire dai 
	 * dati dell'ordinativo indicato e usando la liquidazione indicata.
	 * 
	 * Ispirato al metodo che costruisce la request di inserimento ordinativo nella action di finapp.
	 * 
	 * @param ordinativoDaCopiare
	 * @param nuovaLiquidazione
	 * @param datiOperazioneDto
	 * @return
	 */
	private InserisceOrdinativoPagamento buildOrdinativoPagamentoPerInserimento(OrdinativoPagamento ordinativoDaCopiare, Liquidazione nuovaLiquidazione, DatiOperazioneDto datiOperazioneDto){
		
		InserisceOrdinativoPagamento request = new InserisceOrdinativoPagamento();
		
		Ente ente = req.getEnte();
		Richiedente richiedente = req.getRichiedente();
		Bilancio bilanio = req.getBilancio();
		
		request.setEnte(ente);
		request.setRichiedente(richiedente);
		request.setBilancio(bilanio);
		
		OrdinativoPagamento nuovoOrdPag = clone(ordinativoDaCopiare);
		nuovoOrdPag.setNumero(null);
		nuovoOrdPag.setUid(0);
		
		//request.setOrdinativoPagamento(model.getGestioneOrdinativoStep1Model().getOrdinativo());
		
		//CAPITOLO:
		nuovoOrdPag.setCapitoloUscitaGestione(nuovaLiquidazione.getCapitoloUscitaGestione());
		
		//PROVVEDIMENTO: 
		//TODO capire se l'atto puo' essere quello indicato in input al sevizio
		nuovoOrdPag.setAttoAmministrativo(ordinativoDaCopiare.getAttoAmministrativo());
		
		//SOGGETTO (basta indicare il codice):
		Soggetto sogg = new Soggetto();
		sogg.setCodiceSoggetto(ordinativoDaCopiare.getSoggetto().getCodiceSoggetto());
		nuovoOrdPag.setSoggetto(sogg);
		nuovoOrdPag.getSoggetto().setElencoModalitaPagamento(new ArrayList<ModalitaPagamentoSoggetto>());
		
		//MODALITA PAGAMENTO:
		SoggettoSedeModPagInfo soggEModPagOrdinativo = EntitaUtils.modalitaPagamentoOrdPag(ordinativoDaCopiare);
		nuovoOrdPag.getSoggetto().getElencoModalitaPagamento().add(soggEModPagOrdinativo.getModalitaPagamento());
		
		//DESCRIZIONE:
//		String soggPerDesc = "Soggetto: " + soggEModPagOrdinativo.getSoggetto().getCodiceSoggetto();
//		String ordPerDesc = "Ordinativo: " + ordinativoDaCopiare.getAnno() + " / " + ordinativoDaCopiare.getNumero();
//		String descrizione = "'REINTROITO' "+ordPerDesc+" " + soggPerDesc;
		// SIAC-6252
		nuovoOrdPag.setDescrizione(ordinativoDaCopiare.getDescrizione());
		
		//Imposto dati siope plus:
		nuovoOrdPag = impostaDatiSiopePlus(nuovoOrdPag, nuovaLiquidazione);
		
		//SUB ORDINATIVI:
		List<SubOrdinativoPagamento> subOrd = nuovoOrdPag.getElencoSubOrdinativiDiPagamento();
		List<SubOrdinativoPagamento> nuoviSub = subOrdinativiPerInserimento(subOrd, nuovaLiquidazione);
		nuovoOrdPag.setElencoSubOrdinativiDiPagamento(nuoviSub);
		
		//STATO
		nuovoOrdPag.setStatoOperativoOrdinativo(StatoOperativoOrdinativo.INSERITO);
		
		// 	SIAC-5757 il nuovo ordinativo di pagamento inserito non
		//  deve mantenere il legame verso gli ordinativi di incasso:
		nuovoOrdPag.setElencoOrdinativiCollegati(null);
		//
		
		
		//CONTO TESORERIA:
		nuovoOrdPag.setContoTesoreria(nuovaLiquidazione.getContoTesoreria());
		
		//DISTINTA:
		nuovoOrdPag.setDistinta(nuovaLiquidazione.getDistinta());
		
		//CODIFICHE VARIE PRESE DALLA LIQUIDAZIONE NUOVA:
		
		nuovoOrdPag.setCodMissione(nuovaLiquidazione.getCodMissione());
		nuovoOrdPag.setCodProgramma(nuovaLiquidazione.getCodProgramma());
		nuovoOrdPag.setCodPdc(nuovaLiquidazione.getCodPdc());
		nuovoOrdPag.setCodCofog(nuovaLiquidazione.getCodCofog());
		
		nuovoOrdPag.setCodTransazioneEuropeaSpesa(nuovaLiquidazione.getCodTransazioneEuropeaSpesa());
		nuovoOrdPag.setCodSiope(nuovaLiquidazione.getCodSiope());
		nuovoOrdPag.setCodRicorrenteSpesa(nuovaLiquidazione.getCodRicorrenteSpesa());
		nuovoOrdPag.setCodCapitoloSanitarioSpesa(nuovaLiquidazione.getCodCapitoloSanitarioSpesa());
		nuovoOrdPag.setCodPrgPolReg(nuovaLiquidazione.getCodPrgPolReg());
		
		nuovoOrdPag.setCup(nuovaLiquidazione.getCup());
		
		//SETTO L'ORDINATIVO:
		request.setOrdinativoPagamento(nuovoOrdPag);
		
		// SIAC-6252
		request.setUsaDatiLoginOrig(true);
		
		return request;
	}
	
	/**
	 * Imposta nell'ordinativo di pagamento i dati del siope plus dalla liquidazione indicata
	 * @param nuovoOrdPag
	 * @param nuovaLiquidazione
	 * @return
	 */
	private OrdinativoPagamento impostaDatiSiopePlus(OrdinativoPagamento nuovoOrdPag, Liquidazione nuovaLiquidazione){
		nuovoOrdPag.setSiopeAssenzaMotivazione(nuovaLiquidazione.getSiopeAssenzaMotivazione());
		nuovoOrdPag.setSiopeTipoDebito(nuovaLiquidazione.getSiopeTipoDebito());
		nuovoOrdPag.setCig(nuovaLiquidazione.getCig());
		return nuovoOrdPag;
	}
	
	/**
	 * Imposta nel sub ordinativo di pagamento i dati del siope plus dalla liquidazione indicata
	 * @param nuovoSubOrdPag
	 * @param nuovaLiquidazione
	 * @return
	 */
	private SubOrdinativoPagamento impostaDatiSiopePlus(SubOrdinativoPagamento nuovoSubOrdPag, Liquidazione nuovaLiquidazione){
		nuovoSubOrdPag.setSiopeAssenzaMotivazione(nuovaLiquidazione.getSiopeAssenzaMotivazione());
		nuovoSubOrdPag.setSiopeTipoDebito(nuovaLiquidazione.getSiopeTipoDebito());
		return nuovoSubOrdPag;
	}
	
	
	/**
	 * 
	 * Restituise una lista di un solo elemento:
	 * 
	 * non serve duplicare tutti i sub ordinativi perche' si 
	 * tratta di un reintroito per partita di giro quindi
	 * serve una sola quota con l'importo della liquidazione
	 * 
	 * @param subOrd
	 * @param liquidazione
	 * @return
	 */
	private List<SubOrdinativoPagamento> subOrdinativiPerInserimento(List<SubOrdinativoPagamento> subOrd, Liquidazione liquidazione){
		List<SubOrdinativoPagamento> nuovi = new ArrayList<SubOrdinativoPagamento>();
		List<SubOrdinativoPagamento> senzaAnnullati = CommonUtil.rimuoviSubOrdinativiPagAnnullati(subOrd);
		if(!StringUtilsFin.isEmpty(senzaAnnullati)){
			for(SubOrdinativoPagamento it: senzaAnnullati){
				
				//clono i vari dati:
				SubOrdinativoPagamento nuovo = clone(it);
				
				//pulisco il riferimento all'eventuale documento:
				nuovo.setSubDocumentoSpesa(null);
				
				//pulisco i riferimenti ad id e numero della quota:
				nuovo.setUid(0);
				nuovo.setNumero(null);
				nuovo.setNumeroOrdinativo(null);
				
				//dati siope plus:
				nuovo = impostaDatiSiopePlus(nuovo, liquidazione);
				
				//setto la liquidazione:
				nuovo.setLiquidazione(liquidazione);
				
				//imposto l'importo uguale a quello della liquidazione:
				nuovo.setImportoIniziale(liquidazione.getImportoLiquidazione());
				nuovo.setImportoAttuale(liquidazione.getImportoLiquidazione());
				nuovo.setImportoOrdinativo(liquidazione.getImportoLiquidazione());
				
				//aggiungo in lista:
				nuovi.add(nuovo);
				
				//esco dal ciclo al primo per il discorso scritto in javadoc:
				break;
				
			}
		}
		return nuovi;
	}

	/**
	 * Questo metodo implementa questo passo di analisi:
	 * 
	 * SPOSTARE l'ordinativo come segue:
	 *	Chiamare la GEN per ANNULLAMENTO ORDINATIVO (OPA-ANN)
	 *	Scollegare l'ordinativo dalla liquidazione (o dalle liquidazioni se ho tante quote)  
	 *	Scollegare l'ordinativo dal capitolo
	 *	Scollegare l'ordinativo dalle classificazioni finanziarie
	 *	Collegare nuovamente l'ordinativo alla liquidazione creata e conseguentemente al suo capitolo e alle sue classificazioni finanziarie 
	 *	Aggiornare la DATA SPOSTAMENTO
	 *	Chiamare la GEN per INSERIMENTO ORDINATIVO (OPA-INS)
	 * 
	 */
	private OrdinativoPagamentoScritturaGenInfoDto spostaOrdinativoPagamento(OrdinativoPagamento ordinativoPagamentoRicaricato,
			Liquidazione nuovaLiquidazione, DatiOperazioneDto datiOperazioneDto){
		
		//Le chiamate a gen verranno fatto tutte assieme a fine elaborazione:
		OrdinativoPagamentoScritturaGenInfoDto datiPerScritturaGen = new OrdinativoPagamentoScritturaGenInfoDto();
		datiPerScritturaGen.setOrdinativoPagamento(ordinativoPagamentoRicaricato);
		datiPerScritturaGen.setLiquidazione(nuovaLiquidazione);
		//
		
		//2. Scollegare l'ordinativo dalla liquidazione (o dalle liquidazioni se ho tante quote)  
		     //Scollegare l'ordinativo dal capitolo
		 	//Scollegare l'ordinativo dalle classificazioni finanziarie
		    //Collegare nuovamente l'ordinativo alla liquidazione creata e conseguentemente al suo capitolo e alle sue classificazioni finanziarie 
		    //Aggiornare la DATA SPOSTAMENTO
		ordinativoPagamentoDad.spostaOrdinativoPagamentoPerReintroito(ordinativoPagamentoRicaricato, nuovaLiquidazione, datiOperazioneDto);
		
		
		//3. Per scollegare completamente i documenti di spesa occorre richiamare aggiornaStatoDocumentoDiSpesaService
		aggiornaStatoDocumentiDiSpesa(ordinativoPagamentoRicaricato);
		
		
		//aggiungo il messaggio di ordinativo spostato:
		String messaggio = "Ord " + riferimento(ordinativoPagamentoRicaricato) + " spostato su liq " + riferimento(nuovaLiquidazione);
		addMessaggioOperazioneEffettuata("sposta ordinativo pagamento", messaggio);
		//
		
		return datiPerScritturaGen;
	}
	
	private void aggiornaStatoDocumentiDiSpesa(OrdinativoPagamento ordinativoPagamentoRicaricato){
		if(ordinativoPagamentoRicaricato!=null && ordinativoPagamentoRicaricato.getElencoSubOrdinativiDiPagamento()!=null){
			for (SubOrdinativoPagamento subOrdinativoPagamento : ordinativoPagamentoRicaricato.getElencoSubOrdinativiDiPagamento()) {
				if(subOrdinativoPagamento!=null && subOrdinativoPagamento.getSubDocumentoSpesa()!=null
						&& subOrdinativoPagamento.getSubDocumentoSpesa().getDocumento()!=null
						&& subOrdinativoPagamento.getSubDocumentoSpesa().getDocumento().getUid()>0){
					AggiornaStatoDocumentoDiSpesa reqDoc = buildRequestAggiornaStatoDocumentoDiSpesa(subOrdinativoPagamento);
					//invoco il servizio:
					aggiornaStatoDocumentoDiSpesa(reqDoc);
				}
			}
		}
	}
	
	private AggiornaStatoDocumentoDiSpesa buildRequestAggiornaStatoDocumentoDiSpesa(SubOrdinativoPagamento subOrdinativoPagamento){
		AggiornaStatoDocumentoDiSpesa reqDoc = new AggiornaStatoDocumentoDiSpesa();
		Richiedente richiedente = req.getRichiedente();
		reqDoc.setDocumentoSpesa(subOrdinativoPagamento.getSubDocumentoSpesa().getDocumento());
		reqDoc.setRichiedente(richiedente);
		return reqDoc;
	}
	
	private AggiornaStatoDocumentoDiSpesaResponse aggiornaStatoDocumentoDiSpesa(AggiornaStatoDocumentoDiSpesa aggStatoReq){
		//inibisco le scritture gen dentro al servizio (le facciamo da qui alla fine se tutto e' andato bene):
		//insLiqReq.setRegistraGen(false);
		//
		//INVOCO IL METRO CENTRALIZZATO DI AGGIORNAMENTO:
		AggiornaStatoDocumentoDiSpesaResponse esito = aggiornaStatoDocumentoDiSpesaService.executeService(aggStatoReq);
		//DEVO VERIFICARE CHE SIA STATO AGGIORNATO,
		//IN CASO CONTRARIO SI DEVE ASSOLUTAMENT RILANCIARE UN'ECCEZIONE
		//PER FARE ROLLBACKARE TUTTO:
		if(esito==null){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("aggiornaStatoDocumentoDiSpesa: esito nullo"));
		} else if(!StringUtilsFin.isEmpty(esito.getErrori()) && !Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(esito.getErrori().get(0));
		} else if(esito.getDocumentoSpesa()==null || esito.getDocumentoSpesa().getNumero() == null){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("aggiornaStatoDocumentoDiSpesa: esito privo di documento"));
		}  else if(!Esito.SUCCESSO.equals(esito.getEsito())){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("aggiornaStatoDocumentoDiSpesa: esito negativo"));
		}
		return esito;
	}
	
	
	private OrdinativoPagamento aggiornaCodificheOrdinativoPagamento(OrdinativoPagamento ordinativoDaAggiornare, Liquidazione nuovaLiquidazione, DatiOperazioneDto datiOperazioneDto){
		
		//avanzo la data in modo da non restare indietro con le date inizio validita:
		datiOperazioneDto = commonDad.aggiornaTimeStamp(datiOperazioneDto);
		//
		
		//1. Ricarico per avere dati freschi:
		OrdinativoPagamento ordRicaricato = ricercaOrdinativoPagamentoPerChiave(ordinativoDaAggiornare);
		
		
		//2. Aggiorna un ordinativo di pagamento con i dati delle codifiche della liquidazione:
		AggiornaOrdinativoPagamento aggReq = buildAggiornaCodificheFromLiquidazione(ordRicaricato, nuovaLiquidazione);
		
		OrdinativoPagamento ordPagConCodificheSettate = clone(aggReq.getOrdinativoPagamento());
		
		AggiornaOrdinativoPagamentoResponse esitoAggiornamento = aggiornaOrdinativoPagamento(aggReq);
		OrdinativoPagamento ordAggiornato = esitoAggiornamento.getOrdinativoPagamentoAggiornato();
		
		//3. aggiungo il messaggio di ordinativo aggiornato:
		String messaggio = "Ord aggiornato: " + riferimento(ordAggiornato) + " -  Nuova liq: " + riferimento(nuovaLiquidazione);
		addMessaggioOperazioneEffettuata("aggiornato ordinativo pagamento con le codifiche della nuova liquidazione", messaggio);
		//
		
		//Mando il timestamp piu avanti delle ultime modifiche:
		datiOperazioneDto = commonDad.aggiornaTimeStamp(datiOperazioneDto);
		//
		
		//4. Ricarico per restiutire dati freschi:
		
		if(ricaricaOrdPagDopoAggiornaCodifiche){
			return ricercaOrdinativoPagamentoPerChiave(ordinativoDaAggiornare);
		} else {
			return ordPagConCodificheSettate;
		}
		
		
	}
	
	/**
	 * 
	 * Elimina i subordinativi e ne ricrea al loro posto uno riepilogativo senza documenti
	 * 
	 * @param ordinativoDaAggiornare
	 * @param nuovaLiquidazione
	 * @param datiOperazioneDto
	 * @return
	 */
	private OrdinativoPagamento aggiornaSubOrdinativiPagamento(OrdinativoPagamento ordinativoDaAggiornare, DatiOperazioneDto datiOperazioneDto){
		
		//avanzo la data in modo da non restare indietro con le date inizio validita:
		datiOperazioneDto = commonDad.aggiornaTimeStamp(datiOperazioneDto);
		//
		
		//1. Ricarico per avere dati freschi:
		OrdinativoPagamento ordRicaricato = ricercaOrdinativoPagamentoPerChiave(ordinativoDaAggiornare);
		
		
		//2. Aggiorna un ordinativo di pagamento con i dati delle codifiche della liquidazione:
		AggiornaOrdinativoPagamento aggReq = buildAggiornaSubOrdinativiPagamento(ordRicaricato);
		AggiornaOrdinativoPagamentoResponse esitoAggiornamento = aggiornaOrdinativoPagamento(aggReq);
		OrdinativoPagamento ordAggiornato = esitoAggiornamento.getOrdinativoPagamentoAggiornato();
		
		//3. aggiungo il messaggio di ordinativo aggiornato:
		String messaggio = "Ord aggiornato: " + riferimento(ordAggiornato);
		addMessaggioOperazioneEffettuata("Rimossi i subordinativi e ricreato un subordinativo aggregato senza documenti", messaggio);
		//
		
		//Mando il timestamp piu avanti delle ultime modifiche:
		datiOperazioneDto = commonDad.aggiornaTimeStamp(datiOperazioneDto);
		//
		
		//4. Ricarico per restiutire dati freschi:
		return ricercaOrdinativoPagamentoPerChiave(ordinativoDaAggiornare);
		
	}
	
	private InserisceLiquidazione buildLiquidazioneFromImpegno(ImpegnoPerReintroitoInfoDto impDestInfo,
			BigDecimal importo, OrdinativoPagamento ordPag,
			OrdinativoInReintroitoInfoDto datiReintroito){
		
		Ente ente = req.getEnte();
		Richiedente richiedente = req.getRichiedente();
		Bilancio bilancio = req.getBilancio();
		
		InserisceLiquidazione insLiqReq = new InserisceLiquidazione();
		
		insLiqReq.setRichiedente(richiedente);
		insLiqReq.setEnte(ente);
		insLiqReq.setBilancio(bilancio);
		insLiqReq.setAnnoEsercizio(Integer.toString(bilancio.getAnno()));
		
		Liquidazione liquidazione = new Liquidazione();
		
		//li clono per evitare ogni problema ai chiamanti:
		Impegno impegno = clone(impDestInfo.getImpegno());
		SubImpegno subImpegno = clone(impDestInfo.getSubImpegno());
		//
		
		//Prendo "quello di riferimento":
		Impegno impOrSub = impDestInfo.getImpOSub();
		//
		
		//setto imp e sub:
		liquidazione.setImpegno(impegno);
		liquidazione.setSubImpegno(subImpegno);
		
		//DESCRIZIONE
		String descrizione = ReintroitoUtils.descrizionePerLiquidazione(impDestInfo, datiReintroito);
		liquidazione.setDescrizioneLiquidazione(descrizione);		
		
		//IMPORTO
		liquidazione.setImportoLiquidazione(importo);	
		
		//CIG
		liquidazione.setCig(impOrSub.getCig());
		
		//CUP
		liquidazione.setCup(impOrSub.getCup());
		
		//SIOPE TIPO DEBITO
		liquidazione.setSiopeTipoDebito(impOrSub.getSiopeTipoDebito());
		
		//ASSENZA MOTIVAZIONE CIG:
		liquidazione.setSiopeAssenzaMotivazione(impOrSub.getSiopeAssenzaMotivazione());
		
		//PROVVEDIMENTO:
		//NOTA: l'analisi riporta: provvedimento indicato allo step1 se "Provvedimento Unico" altrimenti si deriva dall'ordinativo da reintroitare
		//ma credo sia sbagliato e uso quello dell'impegno destinazione:
		AttoAmministrativo attoAmministrativo = ReintroitoUtils.attoAmministrativoDaUsare(impDestInfo,datiReintroito);
		liquidazione.setAttoAmministrativoLiquidazione(attoAmministrativo);
		
		//SETTIAMO IL SUB SE E' SUB:
		if(subImpegno!=null && CommonUtil.maggioreDiZero(subImpegno.getNumeroBigDecimal())){
			//bisogna settare il sub dentro l'impegno come unico sub perche'
			//l'inserisci liquidazione ragiona cosi:
			ArrayList<SubImpegno> subImpegni = new ArrayList<SubImpegno>();
			subImpegni.add(subImpegno);
			impegno.setElencoSubImpegni(subImpegni);
		}
			
		liquidazione.setImpegno(impegno);
		liquidazione.setSubImpegno(subImpegno);
		
		
		SoggettoSedeModPagInfo soggEModPagOrdinativo = EntitaUtils.modalitaPagamentoOrdPag(ordPag);
		
		Soggetto soggetto = soggEModPagOrdinativo.getSoggetto();
		ArrayList<ModalitaPagamentoSoggetto> listmps = new ArrayList<ModalitaPagamentoSoggetto>();
		listmps.add(soggEModPagOrdinativo.getModalitaPagamento());
		soggetto.setModalitaPagamentoList(listmps);
		ArrayList<SedeSecondariaSoggetto> listsss = new ArrayList<SedeSecondariaSoggetto>();
		listsss.add(soggEModPagOrdinativo.getSedeSecondaria());
		soggetto.setSediSecondarie(listsss);
		liquidazione.setSoggettoLiquidazione(soggetto);	
		
		//CONTO TESORERIA:
		liquidazione.setContoTesoreria(ordPag.getContoTesoreria());
		
		//DISTINTA:
		liquidazione.setDistinta(ordPag.getDistinta());		
		
		//LIQ MANUALE:
		liquidazione.setLiqManuale(CostantiFin.LIQUIDAZIONE_MAUALE);
		liquidazione.setLiqAutomatica(CostantiFin.LIQUIDAZIONE_LIQ_AUTOMATICA_NO);
		
		liquidazione.setCodMissione(impOrSub.getCodMissione());
		liquidazione.setCodProgramma(impOrSub.getCodProgramma());
		liquidazione.setCodPdc(impOrSub.getCodPdc());
		liquidazione.setCodCofog(impOrSub.getCodCofog());
		
		liquidazione.setCodTransazioneEuropeaSpesa(impOrSub.getCodTransazioneEuropeaSpesa());
		liquidazione.setCodSiope(impOrSub.getCodSiope());
		liquidazione.setCodRicorrenteSpesa(impOrSub.getCodRicorrenteSpesa());
		liquidazione.setCodCapitoloSanitarioSpesa(impOrSub.getCodCapitoloSanitarioSpesa());
		liquidazione.setCodPrgPolReg(impOrSub.getCodPrgPolReg());
		
		//SETTO LA LIQUIDAZIONE:
		insLiqReq.setLiquidazione(liquidazione);
		
		
		return insLiqReq;
	}
	
	private AggiornaOrdinativoPagamento buildAggiornaCodificheFromLiquidazione(OrdinativoPagamento ordPagDaAggiornare, Liquidazione nuovaLiquidazione){
		
		Ente ente = req.getEnte();
		Richiedente richiedente = req.getRichiedente();
		Bilancio bilancio = req.getBilancio();
		
		AggiornaOrdinativoPagamento aggOrdPagReq = new AggiornaOrdinativoPagamento();
		
		aggOrdPagReq.setRichiedente(richiedente);
		aggOrdPagReq.setEnte(ente);
		aggOrdPagReq.setBilancio(bilancio);
		
		OrdinativoPagamento ordPagConCodifiche = clone(ordPagDaAggiornare);
		
		//CIG
		ordPagConCodifiche.setCig(nuovaLiquidazione.getCig());
		
		//CUP
		ordPagConCodifiche.setCup(nuovaLiquidazione.getCup());
		
		//SIOPE TIPO DEBITO
		ordPagConCodifiche.setSiopeTipoDebito(nuovaLiquidazione.getSiopeTipoDebito());
		
		//ASSENZA MOTIVAZIONE CIG:
		ordPagConCodifiche.setSiopeAssenzaMotivazione(nuovaLiquidazione.getSiopeAssenzaMotivazione());
		
		//CONTO TESORERIA:
		ordPagConCodifiche.setContoTesoreria(nuovaLiquidazione.getContoTesoreria());
		
		//DISTINTA:
		ordPagConCodifiche.setDistinta(nuovaLiquidazione.getDistinta());		
		
		
		ordPagConCodifiche.setCodMissione(nuovaLiquidazione.getCodMissione());
		ordPagConCodifiche.setCodProgramma(nuovaLiquidazione.getCodProgramma());
		ordPagConCodifiche.setCodPdc(nuovaLiquidazione.getCodPdc());
		ordPagConCodifiche.setCodCofog(nuovaLiquidazione.getCodCofog());
		
		ordPagConCodifiche.setCodTransazioneEuropeaSpesa(nuovaLiquidazione.getCodTransazioneEuropeaSpesa());
		ordPagConCodifiche.setCodSiope(nuovaLiquidazione.getCodSiope());
		ordPagConCodifiche.setCodRicorrenteSpesa(nuovaLiquidazione.getCodRicorrenteSpesa());
		ordPagConCodifiche.setCodCapitoloSanitarioSpesa(nuovaLiquidazione.getCodCapitoloSanitarioSpesa());
		ordPagConCodifiche.setCodPrgPolReg(nuovaLiquidazione.getCodPrgPolReg());
		
		// Null safety!!
		if(ordPagConCodifiche.getSoggetto() != null) {
			ordPagConCodifiche.getSoggetto().setElencoModalitaPagamento(ordPagConCodifiche.getSoggetto().getModalitaPagamentoList());
		}
		
		//SETTO L'ORDINATIVO:
		aggOrdPagReq.setOrdinativoPagamento(ordPagConCodifiche);
		
		return aggOrdPagReq;
	}
	
	private AggiornaOrdinativoPagamento buildAggiornaSubOrdinativiPagamento(OrdinativoPagamento ordPagDaAggiornare){
		
		Ente ente = req.getEnte();
		Richiedente richiedente = req.getRichiedente();
		Bilancio bilancio = req.getBilancio();
		
		AggiornaOrdinativoPagamento aggOrdPagReq = new AggiornaOrdinativoPagamento();
		
		aggOrdPagReq.setRichiedente(richiedente);
		aggOrdPagReq.setEnte(ente);
		aggOrdPagReq.setBilancio(bilancio);
		
		OrdinativoPagamento ordPagConSubAggregato = clone(ordPagDaAggiornare);
		
		
		List<SubOrdinativoPagamento> quote = ordPagConSubAggregato.getElencoSubOrdinativiDiPagamento();
		
		BigDecimal sommaSub = CommonUtil.sommaImportiAttualiNonAnnullatiSubOrdPag(quote);
		SubOrdinativoPagamento primoSubNonAnnullato = CommonUtil.getFirstNonAnnullato(quote);
		
		SubOrdinativoPagamento nuovoSubAggregato = clone(primoSubNonAnnullato);
		nuovoSubAggregato.setNumero(null);
		nuovoSubAggregato.setUid(0);
		nuovoSubAggregato.setSubDocumentoSpesa(null);
		nuovoSubAggregato.setImportoAttuale(sommaSub);
		
		//Diventa l'unico sub che verra' salvato facendo eliminare gli altri:
		ordPagConSubAggregato.setElencoSubOrdinativiDiPagamento(toList(nuovoSubAggregato));
		
		//SETTO L'ORDINATIVO:
		aggOrdPagReq.setOrdinativoPagamento(ordPagConSubAggregato);
		
		return aggOrdPagReq;
	}

	private void effettuaModificheAutomaticheAccertamenti(OrdinativoInReintroitoInfoDto datiReintroito,DatiOperazioneDto datiOperazioneDto) {
		if(!StringUtilsFin.isEmpty(datiReintroito.getModificheAutomaticheNecessarie())){
			
			//1. Come prima cosa aggreghiamo le modifiche che vertono sullo stesso subaccertamento
			//   per capire quanto va modificato sull'accertamento di cui fanno parte
			List<AccertamentoModAutomaticaPerReintroitoInfoDto> listaModInfo = datiReintroito.getModificheAutomaticheNecessarie();
			List<SubAccertamentiModAutomaticaPerReintroitoInfoDto> modSubAccertamenti = aggregaModificheSub(listaModInfo);
			List<AccertamentoModAutomaticaPerReintroitoInfoDto> listaModAccertamenti = aggregaModificheAcc(listaModInfo);
			
			//2. Imposto il valore della eventuale modifica anche sugli accertamenti:
			modSubAccertamenti = calcolaImportoModificaSuAcccertamenti(modSubAccertamenti);
			
			//A. MODIFICHE SUB (piu eventualmente anche i loro acc)
			creaModificheSubERelativiAccertamenti(modSubAccertamenti,datiReintroito,datiOperazioneDto);
			
			//B. MODIFICHE SUI SOLI ACCERTAMENTI:
			creaModificheSoloAccertamenti(listaModAccertamenti,datiReintroito,datiOperazioneDto);
			
		}
	}
	
	/**
	 * Effettua le modifiche di importo sui sub accertamenti e se necessario sui loro accertamenti.
	 * @param modSubAccertamenti
	 * @param datiReintroito 
	 */
	private void creaModificheSubERelativiAccertamenti(List<SubAccertamentiModAutomaticaPerReintroitoInfoDto> modSubAccertamenti,
			OrdinativoInReintroitoInfoDto datiReintroito, DatiOperazioneDto datiOperazioneDto){
		final String methodName ="creaModificheSubERelativiAccertamenti";
		if(!StringUtilsFin.isEmpty(modSubAccertamenti)){
			for(SubAccertamentiModAutomaticaPerReintroitoInfoDto subAccModAutIt: modSubAccertamenti){
				 
				Integer uIdAccertamento = subAccModAutIt.getAccertamento().getUid();

				//SIAC-7505
				Integer maxNumeroModificaDaBaseDati = accertamentoOttimizzatoDad.getMaxNumeroModificaDaBaseDati(uIdAccertamento, datiOperazioneDto.getSiacTEnteProprietario().getUid());
				
				int numeroModifiche = maxNumeroModificaDaBaseDati.intValue();
				
				List<AccertamentoModAutomaticaPerReintroitoInfoDto> listaModSub = subAccModAutIt.getModificheSub();
				
				for(AccertamentoModAutomaticaPerReintroitoInfoDto amaInfoIt: listaModSub){
					
					//gestione atto amministrativo:
					AttoAmministrativo attoAmministrativo = ReintroitoUtils.attoAmministrativoDaUsare(amaInfoIt.getSubAccertamento(), datiReintroito);
					//
					
					BigDecimal importoAttualeSubAccertamento = amaInfoIt.getSubAccertamento().getImportoAttuale(); 
					
					BigDecimal importoNew = importoAttualeSubAccertamento.add(amaInfoIt.getModificaDaApportare());
					BigDecimal importoOld = amaInfoIt.getModificaDaApportare();
					
					
					
					// Il metodo creaModifica sembra far
					// distinzione tra sub e non sub per l'id, e per renderci la vita complicata:
					//  1. se sub vuole il movegestTsId
					//  2. se acc vuold il movgestId
					// In questo caso essendo sub passiamo il movgestTsId:
					Integer uidTs = subAccModAutIt.getSubAccertamento().getUid();
					
					numeroModifiche = numeroModifiche + 1;
					log.debug(methodName, new StringBuilder("Inserisco una modifica con numero ").append(numeroModifiche)
							.append(" per il subaccertamento [uid: ").append(uidTs)
							.append(" ] dell'accertamento [uid: ").append(uIdAccertamento)
							.append(" ]. L'Importo attuale dell'accertamento e' ")
							.append(amaInfoIt.getAccertamento().getImportoAttuale()).append(" , l'importo modifica da aggiungere e': ").append(amaInfoIt.getModificaDaApportare())
							.append(" , importo new della modifica : " ).append(importoNew)
							.append(" , importo old della modifica : " ).append(importoOld));
					ModificaMovimentoGestioneEntrata movimento = popolaDatiBaseModifica(true, attoAmministrativo, importoOld, importoNew);
					
					accertamentoOttimizzatoDad.creaModificaImportoSub(uIdAccertamento, subAccModAutIt.getSubAccertamento(),
							movimento, datiOperazioneDto, numeroModifiche);
					
				}
				
				
				if(subAccModAutIt.getModificaAccertamento()!=null){
					//ANCHE l'ACC VA MODIFICATO
					
					//gestione atto amministrativo:
					AttoAmministrativo attoAmministrativo = ReintroitoUtils.attoAmministrativoDaUsare(subAccModAutIt.getAccertamento(), datiReintroito);
					//
					
					BigDecimal importoAttualeAccertamento = subAccModAutIt.getAccertamento().getImportoAttuale();
					BigDecimal importoNew = importoAttualeAccertamento.add(subAccModAutIt.getModificaAccertamento());
					BigDecimal importoOld = subAccModAutIt.getModificaAccertamento();
					
					// Il metodo creaModifica sembra far
					// distinzione tra sub e non sub per l'id, e per renderci la vita complicata:
					//  1. se sub vuole il movegestTsId
					//  2. se acc vuold il movgestId
					// In questo caso essendo acc passiamo il movgestId:
					numeroModifiche = numeroModifiche + 1;
					log.debug(methodName, new StringBuilder("Inserisco una modifica con numero").append(numeroModifiche).append(" per l'accertamento [uid: ").append(uIdAccertamento)
							.append(" ]. L'Importo attuale dell'accertamento e' ")
							.append(subAccModAutIt.getAccertamento().getImportoAttuale()).append(" , l'importo modifica da aggiungere e': ").append(subAccModAutIt.getModificaAccertamento())
							.append(" , importo new della modifica : " ).append(importoNew)
							.append(" , importo old della modifica : " ).append(importoOld));
					creaModifica(false, uIdAccertamento.intValue(), attoAmministrativo, importoOld, importoNew, datiOperazioneDto,numeroModifiche);
				}
				
			}
		}
	}
	
	/**
	 * Crea le modifiche dei soli accertamenti che necessitano di modifiche.
	 * @param listaModAccertamenti
	 * @param datiReintroito 
	 */
	private void creaModificheSoloAccertamenti(List<AccertamentoModAutomaticaPerReintroitoInfoDto> listaModAccertamenti,
			OrdinativoInReintroitoInfoDto datiReintroito,DatiOperazioneDto datiOperazioneDto){
		final String methodName = "creaModificheSoloAccertamenti";
		if(!StringUtilsFin.isEmpty(listaModAccertamenti)){
			
			for(AccertamentoModAutomaticaPerReintroitoInfoDto amaInfoIt: listaModAccertamenti){
				
				MovGestInfoDto movGestInfoAccertamento = amaInfoIt.getMovGestInfoAccertamento();
				
				
				// Il metodo creaModifica sembra far
				// distinzione tra sub e non sub per l'id, e per renderci la vita complicata:
				//  1. se sub vuole il movegestTsId
				//  2. se acc vuold il movgestId
				// In questo caso essendo acc passiamo il movgestId:
				Integer uIdAccertamento = movGestInfoAccertamento.getSiacTMovgest().getMovgestId();

				//gestione atto amministrativo:
				AttoAmministrativo attoAmministrativo = ReintroitoUtils.attoAmministrativoDaUsare(amaInfoIt.getAccertamento(), datiReintroito);
				//
				
				BigDecimal importoOld = amaInfoIt.getAccertamento().getImportoAttuale();
				BigDecimal importoNew = importoOld.add(amaInfoIt.getModificaDaApportare());
				
//				int numeroModifiche = accertamentoOttimizzatoDad.countModificheTotali(movgestTsIdAccertamento, datiOperazioneDto);
				Integer maxNumeroModificaDaBaseDati = accertamentoOttimizzatoDad.getMaxNumeroModificaDaBaseDati(uIdAccertamento, datiOperazioneDto.getSiacTEnteProprietario().getUid());
				
				int numeroModifiche = maxNumeroModificaDaBaseDati.intValue() +1;
				log.debug(methodName, new StringBuilder("Inserisco una modifica con numero ").append(numeroModifiche).append(" per l'accertamento [uid: ").append(uIdAccertamento)
						.append(" ]. L'Importo attuale dell'accertamento e' ")
						.append(amaInfoIt.getAccertamento().getImportoAttuale()).append(" , l'importo modifica da aggiungere e': ").append(amaInfoIt.getModificaDaApportare())
						.append(" , importo new della modifica : " ).append(importoNew));
								
				creaModifica(false, uIdAccertamento.intValue(), attoAmministrativo, importoOld, importoNew, datiOperazioneDto,numeroModifiche);
				
			}
		}
	}
	
	/**
	 * Data la lista listaModInfo con tutte le modifiche che si rendono necessarie,
	 * filtra solo quelle che riguardano dei sub accertamenti e le aggrega per sub accertamenti dello stesso accertamento.
	 * 
	 * Ogni elemento della lista in out rappresenta quindi un raggruppamento di modifiche di sub accertamenti di uno stesso accertamento.
	 * 
	 * @param listaModInfo
	 * @return
	 */
	private List<SubAccertamentiModAutomaticaPerReintroitoInfoDto> aggregaModificheSub(List<AccertamentoModAutomaticaPerReintroitoInfoDto> listaModInfo){
		final String methodName="aggregaModificheSub";
		List<SubAccertamentiModAutomaticaPerReintroitoInfoDto> modSubAccertamenti = new ArrayList<SubAccertamentiModAutomaticaPerReintroitoInfoDto>();
		List<MovimentoKey> giaControllati = new ArrayList<MovimentoKey>();
		if(!StringUtilsFin.isEmpty(listaModInfo)){
			for(AccertamentoModAutomaticaPerReintroitoInfoDto modInfoIt: listaModInfo){
				MovimentoKey movKey = modInfoIt.getKey();
				log.debug(methodName, "movimento Key: " + movKey.getDescrizioneMovimentoKey());
				if(!ReintroitoUtils.contenutoInLista(giaControllati, movKey) && ReintroitoUtils.isSub(movKey)){
					List<AccertamentoModAutomaticaPerReintroitoInfoDto> conStessoSub = ReintroitoUtils.conStessoMovKey(listaModInfo, movKey);
					SubAccertamentiModAutomaticaPerReintroitoInfoDto subModInfo = new SubAccertamentiModAutomaticaPerReintroitoInfoDto();
					subModInfo.setAccertamento(modInfoIt.getAccertamento());
					subModInfo.setKey(ReintroitoUtils.subToMovKey(movKey));
					subModInfo.setSubAccertamento(modInfoIt.getSubAccertamento());
					subModInfo.setModificheSub(conStessoSub);
					modSubAccertamenti.add(subModInfo);
				}
			}
		}
		return modSubAccertamenti;
	}
	
	/**
	 * Data la lista listaModInfo con tutte le modifiche che si rendono necessarie,
	 * filtra solo quelle che riguardano degli accertamenti.
	 * 
	 * La lista in output sara' come la lista in input ma SENZA gli elementi che riguardano subaccertamenti.
	 * 
	 * Serve per avere l'elenco dei soli accertamenti da modificare per i quali non ci sono sub da modificare.
	 * 
	 * @param listaModInfo
	 * @return
	 */
	private List<AccertamentoModAutomaticaPerReintroitoInfoDto> aggregaModificheAcc(List<AccertamentoModAutomaticaPerReintroitoInfoDto> listaModInfo){
		List<AccertamentoModAutomaticaPerReintroitoInfoDto> listaModAccertamenti = new ArrayList<AccertamentoModAutomaticaPerReintroitoInfoDto>();
		List<MovimentoKey> giaControllati = new ArrayList<MovimentoKey>();
		if(!StringUtilsFin.isEmpty(listaModInfo)){
			for(AccertamentoModAutomaticaPerReintroitoInfoDto modInfoIt: listaModInfo){
				MovimentoKey movKey = modInfoIt.getKey();
				if(!ReintroitoUtils.contenutoInLista(giaControllati, movKey)){
					if(!ReintroitoUtils.isSub(modInfoIt.getKey())){
						listaModAccertamenti.add(clone(modInfoIt));
					}
				}
			}
		}
		return listaModAccertamenti;
	}
	
	/**
	 * 
	 * Per un dato raggruppamento di sub dello stesso accertamento che devono subire modifiche
	 * (un oggetto SubAccertamentiModAutomaticaPerReintroitoInfoDto rappresenta un insieme di sub accertamenti di un di uno stesso accertamento)
	 * valuta se il loro Accertamento deve essere modificato anch'esso:
	 * 
	 * se deve essere modificato anche l'accertamento setta il valore della modifica che si rende necessaria
	 * in modSubAccertamenti.setModificaAccertamento
	 * se non si rende nessaria e'settata a null
	 * 
	 * @param modSubAccertamenti
	 * @return
	 */
	private SubAccertamentiModAutomaticaPerReintroitoInfoDto calcolaImportoModificaSuAcccertamenti(SubAccertamentiModAutomaticaPerReintroitoInfoDto modSubAccertamenti){
		//LEGGO L'ESITO RICERCA PER UN SUB QUALSIASI:
		EsitoRicercaMovimentoPkDto esitoRicercaDiUnSub = modSubAccertamenti.getModificheSub().get(0).getEsitoRicercaMovPkDto();
		
		List<SubAccertamento> elencoTuttiSub = esitoRicercaDiUnSub.getElencoSubAccertamentiTuttiConSoloGliIds();
		
		BigDecimal sommaSub = CommonUtil.sommaImportiAttualiNonAnnullati(elencoTuttiSub);
		
		BigDecimal sommaModificheSub = ReintroitoUtils.sommaImportiModifiche(modSubAccertamenti.getModificheSub());
		
		
		BigDecimal sommaSubDopoModifiche = sommaSub.add(sommaModificheSub);
		
		BigDecimal importoAttualeAccertamento = modSubAccertamenti.getAccertamento().getImportoAttuale();
		
		if(importoAttualeAccertamento.compareTo(sommaSubDopoModifiche)<0){
			//OCCORRE INCREMENTEARE ANCHE L'ACCERTAMENTO:
			BigDecimal modificaSuAccertamento = sommaSubDopoModifiche.subtract(importoAttualeAccertamento);
			modSubAccertamenti.setModificaAccertamento(modificaSuAccertamento);
		} else {
			modSubAccertamenti.setModificaAccertamento(null);
		}
		
		return modSubAccertamenti;
		
	}
	
	/**
	 * Itera i raggruppamenti di sub dello stesso accertamento che devono subire modifiche
	 * (ogni oggetto SubAccertamentiModAutomaticaPerReintroitoInfoDto rappresenta un insieme di sub accertamenti di uno stesso accertamento)
	 * e valuta per ognuno di questi raggruppamenti se il loro accertamento deve essere modificato anch'esso.
	 * 
	 * @param modSubAccertamenti
	 * @return
	 */
	private List<SubAccertamentiModAutomaticaPerReintroitoInfoDto> calcolaImportoModificaSuAcccertamenti(List<SubAccertamentiModAutomaticaPerReintroitoInfoDto> modSubAccertamenti){
		if(!StringUtilsFin.isEmpty(modSubAccertamenti)){
			for(SubAccertamentiModAutomaticaPerReintroitoInfoDto it: modSubAccertamenti){
				it = calcolaImportoModificaSuAcccertamenti(it);
			}
		}
		return modSubAccertamenti;
	}

	/**
	 * 
	 * Si occupa di caricare i capitoli e provvedimenti degi movimenti
	 * 
	 * @param datiReintroito
	 * @param datiOperazione2
	 * @return
	 */
	private OrdinativoInReintroitoInfoDto completaDatiRicercaMovimentiPk(OrdinativoInReintroitoInfoDto datiReintroito,DatiOperazioneDto datiOperazioneDto) {
		
		//ISTANZIO LE CACHE:
		HashMap<Integer, CapitoloUscitaGestione> cacheCapitoloUscita = new HashMap<Integer, CapitoloUscitaGestione>();
		HashMap<Integer, CapitoloEntrataGestione> cacheCapitoloEntrata = new HashMap<Integer, CapitoloEntrataGestione>();
		HashMap<String, AttoAmministrativo> cacheAttoAmm = new HashMap<String, AttoAmministrativo>();
		
		//1. Come prima cosa completo i dati dell'impegno destinazione:
		ImpegnoPerReintroitoInfoDto impegnoDestinazione = datiReintroito.getImpegnoDestinazione();
		datiReintroito.setImpegnoDestinazione(completaDatiImpegno(impegnoDestinazione,cacheCapitoloUscita,cacheAttoAmm));
		
		//2. Itero le ritenute e completo i dati per i vari impegni e accertamenti:
		List<RitenutaSpiltPerReintroitoInfoDto> listaRitenute = datiReintroito.getListaRitenuteSplit();
		if(!StringUtilsFin.isEmpty(listaRitenute)){
			for(RitenutaSpiltPerReintroitoInfoDto ritIt: listaRitenute){
				
				//IMPEGNO
				ImpegnoPerReintroitoInfoDto impegnoIt = ritIt.getImpegno();
				impegnoIt = completaDatiImpegno(impegnoIt,cacheCapitoloUscita,cacheAttoAmm);
				
				//ACCERTAMENTO
				AccertamentoPerReintroitoInfoDto accertamentoIt = ritIt.getAccertamento();
				accertamentoIt = completaDatiAccertamento(accertamentoIt,cacheCapitoloEntrata,cacheAttoAmm);
				
			}
		}
		
		return datiReintroito;
	}
	
	private AccertamentoPerReintroitoInfoDto completaDatiAccertamento(AccertamentoPerReintroitoInfoDto accInfo,
			HashMap<Integer, CapitoloEntrataGestione> cacheCapitolo,
			HashMap<String,AttoAmministrativo> cacheAttoAmm){
		
		Richiedente richiedente = req.getRichiedente();
		
		Accertamento acc = accInfo.getAccertamento();
		MovimentoKey accDestKey = accInfo.getKey();
		SubAccertamento subAcc = accInfo.getSubAccertamento();
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = null;
		
		//RICHIAMO LO STESSO METODO UTILIZZATO IN RICERCA MOVIMENTO PER CHIAVE SERVICE:
		completaDatiRicercaAccertamentoPk(richiedente, acc, datiOpzionaliCapitoli,cacheAttoAmm,cacheCapitolo);
		//
		
		if(ReintroitoUtils.isSub(accDestKey)){
			//IN CASO DI SUB MI ASPETTO In getElencoSubAccertamenti.get(0) il sub in questione 
			//per il quale il richiamo a completaDatiRicercaImpegnoPk a vestito i dati provv:
			subAcc = acc.getElencoSubAccertamenti().get(0);
			//RIPORTO IL CAPITOLO DELL'IMPEGNO:
			subAcc.setCapitoloEntrataGestione(acc.getCapitoloEntrataGestione());
			//SETTO NEL DTO PRINCIPALE:
			accInfo.setSubAccertamento(subAcc);
		}
		
		return accInfo;
	}
	
	private ImpegnoPerReintroitoInfoDto completaDatiImpegno(ImpegnoPerReintroitoInfoDto impInfo,
			HashMap<Integer, CapitoloUscitaGestione> cacheCapitolo,HashMap<String, AttoAmministrativo> cacheAttoAmm){
		
		Richiedente richiedente = req.getRichiedente();
		
		Impegno imp = impInfo.getImpegno();
		MovimentoKey impDestKey = impInfo.getKey();
		SubImpegno subImp = impInfo.getSubImpegno();
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(ImportiCapitoloEnum.disponibilitaPagare);
		
		//RICHIAMO LO STESSO METODO UTILIZZATO IN RICERCA MOVIMENTO PER CHIAVE SERVICE:
		completaDatiRicercaImpegnoPk(richiedente, imp, impDestKey.getAnnoEsercizio().toString(), datiOpzionaliCapitoli,cacheCapitolo,cacheAttoAmm);
		//
		
		if(ReintroitoUtils.isSub(impDestKey)){
			//IN CASO DI SUB MI ASPETTO In getElencoSubImpegni.get(0) il sub in questione 
			//per il quale il richiamo a completaDatiRicercaImpegnoPk a vestito i dati provv:
			subImp = imp.getElencoSubImpegni().get(0);
			//RIPORTO IL CAPITOLO DELL'IMPEGNO:
			subImp.setCapitoloUscitaGestione(imp.getCapitoloUscitaGestione());
			//SETTO NEL DTO PRINCIPALE:
			impInfo.setSubImpegno(subImp);
		}
		
		return impInfo;
	}

	/**
	 * Importante da eseguire come prima cosa nel checkServiceParam, 
	 * in modo da avere i dati per ricostruire la elab key settati nella response
	 * a fronte di qualsiasi errore che possa succedere successivamente
	 */
	private void settaDatiPerElabKey(){
		res.setOrdPagReintroitato(req.getOrdinativoPagamento());
		res.setEnte(req.getEnte());
	}
	
	/**
	 * Metto i controlli sui dati necessari per cotruire la elab key in un metodo a parte per evidenziarne 
	 * l'importanza
	 * @return
	 */
	private StringBuffer checkParametriPerElabKey(){
		StringBuffer elencoParamentriNonInizializzati = new StringBuffer();
		if(ReintroitoUtils.isEmpty(req.getOrdinativoPagamento())){
			elencoParamentriNonInizializzati.append("Anno o Numero Ordinativo Pagamento Da Reintroitare");
		}
		if(req.getEnte()==null || req.getRichiedente()==null){
			elencoParamentriNonInizializzati.append("ente o richiedente");
		}
		return elencoParamentriNonInizializzati;
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = " : checkServiceParam()";
		log.debug(methodName, "- Begin");
		
		//da fare come prima cosa:
		settaDatiPerElabKey();
		//

		//L'elencoParamentriNonInizializzati e' inizializzato a partire dai 
		//controlli sui dati minimi per elab key che non devono assolutamente essere dimenticati:
		StringBuffer elencoParamentriNonInizializzati = checkParametriPerElabKey();
		//
		
		if(req.getBilancio()==null){
			if (elencoParamentriNonInizializzati.length()>0) {
				elencoParamentriNonInizializzati.append(", ");
			}
			elencoParamentriNonInizializzati.append("bilancio");
		}else if (req.getBilancio().getAnno()==0) {
			if (elencoParamentriNonInizializzati.length()>0) {
				elencoParamentriNonInizializzati.append(", ");
			}
			elencoParamentriNonInizializzati.append("anno bilancio");
		}
		
		if(ReintroitoUtils.isEmpty(req.getImpegnoSuCuiSpostare())){
			elencoParamentriNonInizializzati.append("Anno o Numero Impegno su cui spostare");
		}
		
		//CONTROLLI SULL'ATTO AMMINISRATIVO:
		elencoParamentriNonInizializzati = controlliAttoValorizzato(elencoParamentriNonInizializzati);
		//
		
		if(elencoParamentriNonInizializzati.length() > 0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParamentriNonInizializzati));
		}
		
	}
	
	private StringBuffer controlliAttoValorizzato(StringBuffer elencoParamentriNonInizializzati){
		if(!req.isUtilizzaProvvedimentoDaMovimento()){
			
			 AttoAmministrativo attoAmm = req.getAttoAmministrativo();
			 
			 if(attoAmm==null){
				 elencoParamentriNonInizializzati.append("Atto amministrativo non valorizzato");
			 } else {
				 
				 if(attoAmm.getAnno()<=0){
					 elencoParamentriNonInizializzati.append("Anno atto amministrativo non valorizzato");
				 }
				 if(attoAmm.getNumero()<=0){
					 elencoParamentriNonInizializzati.append("Numero atto amministrativo non valorizzato");
				 }
				 
				 if(attoAmm.getTipoAtto()==null || StringUtilsFin.isEmpty(attoAmm.getTipoAtto().getCodice())){
					 elencoParamentriNonInizializzati.append("Tipo atto amministrativo non valorizzato");
				 }
			 }
		}
		return elencoParamentriNonInizializzati;
	}
	
	
	/**
	 * Dato che non posso passare la request ai dad popolo un dto di comodo
	 * che ricalca l'input al servizio
	 * @return
	 */
	private OrdinativoInReintroitoDatiDiInputDto buildOrdinativoInReintroitoDatiDiInputDto(){
		OrdinativoInReintroitoDatiDiInputDto ord = new OrdinativoInReintroitoDatiDiInputDto();
		ord.setAttoAmministrativo(req.getAttoAmministrativo());
		ord.setImpegnoSuCuiSpostare(req.getImpegnoSuCuiSpostare());
		ord.setOrdinativoPagamento(req.getOrdinativoPagamento());
		ord.setRitenute(req.getRitenute());
		ord.setRitenuteSplit(req.getRitenuteSplit());
		ord.setUtilizzaProvvedimentoDaMovimento(req.isUtilizzaProvvedimentoDaMovimento());
		ord.setBilancio(req.getBilancio());
		ord.setRichiedente(req.getRichiedente());
		ord.setEnte(req.getEnte());
		return ord;
	}

}
