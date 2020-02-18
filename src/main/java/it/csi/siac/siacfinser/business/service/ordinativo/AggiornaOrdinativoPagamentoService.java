/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.OrdinativoPagamentoDad;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubOrdinativoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaOrdinativoPagamentoService extends AbstractInserisceAggiornaAnnullaOrdinativoPagamentoService<AggiornaOrdinativoPagamento, AggiornaOrdinativoPagamentoResponse> {

	@Autowired
	ProvvedimentoService provvedimentoService;

	@Autowired
	SoggettoFinDad soggettoDad;

	@Autowired
	LiquidazioneService liquidazioneService;
	
	@Autowired
	OrdinativoPagamentoDad ordinativoPagamentoDad; 
	
	@Autowired
	CommonDad commonDad;
	
	@Autowired
	MovimentoGestioneService movimentoGestioneService;
	

	
	@Override
	@Transactional
	public AggiornaOrdinativoPagamentoResponse executeService(AggiornaOrdinativoPagamento serviceRequest) {
		return super.executeService(serviceRequest);
	}	

	@Override
	protected void init() {
		final String methodName="AggiornaOrdinativoPagamentoService : init()";
		log.debug(methodName, " - Begin");
	}	

	private Liquidazione leggiLiquidazioneSubOrdinativo(Richiedente richiedente, RicercaLiquidazioneK ricercaLiquidazioneK){
		
		final String methodName="leggiLiquidazioneSubOrdinativo : init()";
		log.debug(methodName, " - Begin");
		
		Liquidazione liquidazione = null;

		RicercaLiquidazionePerChiave ricercaLiquidazionePerChiave = new RicercaLiquidazionePerChiave();
		ricercaLiquidazionePerChiave.setRichiedente(richiedente);
		ricercaLiquidazionePerChiave.setpRicercaLiquidazioneK(ricercaLiquidazioneK);
        ricercaLiquidazionePerChiave.setEnte(req.getEnte());
	    ricercaLiquidazionePerChiave.setDataOra(new Date());
		
		RicercaLiquidazionePerChiaveResponse ricercaLiquidazionePerChiaveResponse = liquidazioneService.ricercaLiquidazionePerChiave(ricercaLiquidazionePerChiave);

		if(ricercaLiquidazionePerChiaveResponse.isFallimento()){
			List<Errore> listaErroriLiquidazione = ricercaLiquidazionePerChiaveResponse.getErrori();			
			res.setErrori(listaErroriLiquidazione);
			res.setOrdinativoPagamentoAggiornato(null);
			res.setEsito(Esito.FALLIMENTO);
			return liquidazione;
		} 
			
		return ricercaLiquidazionePerChiaveResponse.getLiquidazione();
	}
	
	@Override
	public void execute() {
		String methodName = "AggiornaOrdinativoPagamentoService - execute()";
		log.debug(methodName, " - Begin");
		
		//1. Vengono letti i valori ricevuti in input dalla request
		Richiedente richiedente = req.getRichiedente();
		ente = req.getEnte();
		setBilancio(req.getBilancio());
		OrdinativoPagamento ordinativoDiPagamento = req.getOrdinativoPagamento();
		CapitoloUscitaGestione capitoloUscitaGestione = ordinativoDiPagamento.getCapitoloUscitaGestione();
		//Impegno impegno = null;
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.MODIFICA, bilancio.getAnno());
		
		//3. Si inizializza l'oggetto OrdinativoInModificaInfoDto, dto di comodo specifico di questo servizio
		OrdinativoInModificaInfoDto ordinativoInModificaInfoDto = ordinativoPagamentoDad.getDatiGeneraliOrdinativoInAggiornamento(ordinativoDiPagamento, datiOperazione, bilancio, richiedente,ente);

		
		//4. Si effettuano i vari controlli di merito definiti in analisi:
		boolean verificaCondizioniInserimento = verificaCondizioniPerInserimentoOrdinativoDiPagamento(datiOperazione, ordinativoInModificaInfoDto);
		if(!verificaCondizioniInserimento){
			throw new BusinessException("Le verifiche sull'ordinativo hanno dato esito negativo");
		}

		//5. aggiornamento ordinativo di pagamento (si invoca il metodo "core" rispetto all'operazione di aggiornamento di un ordinativo):
		OrdinativoPagamento ordinativoPagamentoUpdated = 
				(OrdinativoPagamento) ordinativoPagamentoDad.aggiornaOrdinativo(ordinativoDiPagamento, ordinativoInModificaInfoDto, datiOperazione, bilancio,richiedente,ente);
		
		if(null!=ordinativoPagamentoUpdated){
			//Chiamo il metodo che si occupa di gestire la doppia gestione:
			EsitoControlliDto resDg = operazioniPerDoppiaGestione(ordinativoDiPagamento, bilancio, richiedente, ente, datiOperazione,ordinativoInModificaInfoDto.getInfoModificheSubOrdinativi(),Operazione.MODIFICA);
			if(!isEmpty(resDg.getListaErrori())){
				//errori in doppia gestione:
				//Costruzione response esito negativo:
				res.setErrori(resDg.getListaErrori());
				res.setOrdinativoPagamentoAggiornato(null);
				res.setEsito(Esito.FALLIMENTO);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return;
			}
			
			ordinativoPagamentoUpdated.setCapitoloUscitaGestione(capitoloUscitaGestione);
			
			/** 
			 * Innesto fin-gen
			 * controllo di avere l'impegno, che in caso di inserimento/aggiornamento nuova quota viene settato
			 * altrimenti no, e qui lo faccio 
			*/
			if(getImpegno()==null){
				// prendo sempre il primo, si dà per buono che abbia il pdc valido x l'ordinativo
				setImpegno(ordinativoPagamentoUpdated.getElencoSubOrdinativiDiPagamento().get(0).getLiquidazione().getImpegno());
				
			}
			
			
			String codCapitoloSanitario = ordinativoDiPagamento.getCodCapitoloSanitarioSpesa()!=null ? ordinativoDiPagamento.getCodCapitoloSanitarioSpesa(): "";
			

			//GENNAIO 2017 introdotta la possibilita' di escludere le registrazioni,
			//il chiamante (l'esigenza e' nata con il servizio di reintroito) che deve
			//orchestrare in maniera atomica piu servizi che possono fallire e metterci tempo si occupera'
			//di richiamare i gen solo dopo essere certo che siano terminati tutti correttamente
			
			//SIAC-6970
			if(req.isRegistraGen() && ordinativoInModificaInfoDto.getInfoModificheSubOrdinativi() != null && ordinativoInModificaInfoDto.getInfoModificheSubOrdinativi().getSubOrdinativiNuoviOModificatoImporto() != null && !ordinativoInModificaInfoDto.getInfoModificheSubOrdinativi().getSubOrdinativiNuoviOModificatoImporto().isEmpty()) {
				log.info(methodName, "soddisfatte le condizioni per la chiamata alla contabilita' generale. Numero subordinativi con importo variato: " +  ordinativoInModificaInfoDto.getInfoModificheSubOrdinativi().getSubOrdinativiNuoviOModificatoImporto().size());
				gestisciRegistrazioneGENPerOrdinativo(ordinativoPagamentoUpdated,getImpegno(), TipoCollegamento.ORDINATIVO_PAGAMENTO, true, getImpegno().isFlagCassaEconomale(), codCapitoloSanitario);
			}else {
				log.info(methodName, "condizioni per la contabilita' generale non soddisfatte.");
			}
			
			//Costruzione response esito positivo:
			
			//Completiamo i dati del provvedimento:
			//Metodo introdotto per la jira SIAC-2125, non venivano caricati tutti i dati del provvedimento
			AttoAmministrativo attoCaricato = completaDatiProvvedimento(ordinativoPagamentoUpdated.getAttoAmministrativo(),richiedente);
			if(attoCaricato!=null){
				ordinativoPagamentoUpdated.setAttoAmministrativo(attoCaricato);
			}
			//
			
			res.setOrdinativoPagamentoAggiornato(ordinativoPagamentoUpdated);
			res.setEsito(Esito.SUCCESSO);
			return;
		} else {
			//Costruzione response esito negativo:
			res.setOrdinativoPagamentoAggiornato(null);
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
	}
	
	/**
	 * Metodo introdotto per la jira SIAC-2125, non venivano caricati tutti i dati del provvedimento
	 * @param atto
	 * @param richiedente
	 * @return
	 */
	private AttoAmministrativo completaDatiProvvedimento(AttoAmministrativo atto, Richiedente richiedente){
		AttoAmministrativo attoCaricato = null;
		if(atto!=null && atto.getUid()>0){
			RicercaProvvedimentoResponse ricercaProvvedimentoResponse = ricercaProvvedimento(richiedente, atto);
			List<AttoAmministrativo> listaAttoAmministrativo = ricercaProvvedimentoResponse.getListaAttiAmministrativi();
			if(null != listaAttoAmministrativo && listaAttoAmministrativo.size() > 0){
				for(int i=0;i<listaAttoAmministrativo.size();i++){
					AttoAmministrativo provvedimento = listaAttoAmministrativo.get(i);
					if(provvedimento!=null && provvedimento.getUid()==atto.getUid()){
						attoCaricato = provvedimento;
					}
				}
			}
		}
		return attoCaricato;
	}
	
	private boolean verificaCondizioniPerInserimentoOrdinativoDiPagamento(DatiOperazioneDto datiOperazione,OrdinativoInModificaInfoDto ordinativoInModificaInfoDto){
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		SiacTOrdinativoFin siacTOrdinativo = ordinativoInModificaInfoDto.getSiacTOrdinativo();
		OrdinativoPagamento ordinativoDiPagamento = req.getOrdinativoPagamento();
		Bilancio bilancio = req.getBilancio();
		Richiedente richiedente = req.getRichiedente();
		CapitoloUscitaGestione capitoloUscitaGestione = ordinativoDiPagamento.getCapitoloUscitaGestione();
		
		if(null!=siacTOrdinativo){
			
			listaErrori = ordinativoPagamentoDad.controlliDiMeritoAggiornamentoOrdinativoDiPagamento(ordinativoDiPagamento, ordinativoInModificaInfoDto, datiOperazione);

			SubOrdinativoInModificaInfoDto infoModificheSubOrdinativi = ordinativoInModificaInfoDto.getInfoModificheSubOrdinativi();
			List<SubOrdinativo> listaSubOrdinativiDaAggiornare = infoModificheSubOrdinativi.getSubOrdinativiDaModificare();
			List<SubOrdinativo> listaSubOrdinativiDaInserire = infoModificheSubOrdinativi.getSubOrdinativiDaInserire();
			
			boolean tuttiImpegniFrazionabili = true;
			int annoMovimenti = 0;

		    // Calcolo il vecchio importo dell'ordinativo, con i dati estratti dal db
		    OrdinativoPagamento ordinativoPagamentoDaDb = (OrdinativoPagamento)ordinativoInModificaInfoDto.getOrdinativo();
		    List<SubOrdinativoPagamento> listaSubOrdinativiDaDb = ordinativoPagamentoDaDb.getElencoSubOrdinativiDiPagamento();
		    BigDecimal importoOrdinativoDaDb = BigDecimal.ZERO;
		    if(null!=listaSubOrdinativiDaDb && listaSubOrdinativiDaDb.size() > 0){
		    	for(SubOrdinativoPagamento subOrdinativoPagamentoDaDb : listaSubOrdinativiDaDb){
		    		if(subOrdinativoPagamentoDaDb.getDataFineValidita()==null){
		    			importoOrdinativoDaDb = importoOrdinativoDaDb.add(subOrdinativoPagamentoDaDb.getImportoAttuale());
		    			
		    			Liquidazione liquidazione = subOrdinativoPagamentoDaDb.getLiquidazione();
		    			if(!liquidazione.getImpegno().isFlagFrazionabile()){
							tuttiImpegniFrazionabili = false;
						}
						// cosi se l'ordinativo ha piu quote su impegni diversi, prendo cmq sempre e solo il primo 
						if(getImpegno() == null){
							super.impegno = liquidazione.getImpegno();
							annoMovimenti = liquidazione.getImpegno().getAnnoMovimento();
						}
		    			
		    		}			    		
		    	}
		    }

			BigDecimal sommaImportiQuote = BigDecimal.ZERO;
			
			
			// Sub-ordinativi da inserire
			// PER OGNI SUBORDINATIVO
			// *	Importo Quota: deve sottostare alla disponibilita' della liquidazione secondo la regola che segue.
			// DISPONIBILE A PAGARE SULLA LIQUIDAZIONE  >=  IMPORTO QUOTA
			if(null!=listaSubOrdinativiDaInserire && listaSubOrdinativiDaInserire.size() > 0){
				for(SubOrdinativo subOrdinativo : listaSubOrdinativiDaInserire){
					
					SubOrdinativoPagamento subOrdinativoPagamento = (SubOrdinativoPagamento) subOrdinativo;
					
					sommaImportiQuote = sommaImportiQuote.add(subOrdinativoPagamento.getImportoAttuale());
					
					RicercaLiquidazioneK ricercaLiquidazioneK = new RicercaLiquidazioneK();
					ricercaLiquidazioneK.setAnnoEsercizio(bilancio.getAnno());
					Liquidazione liquidazioneIn = new Liquidazione();
					liquidazioneIn.setAnnoLiquidazione( subOrdinativoPagamento.getLiquidazione().getAnnoLiquidazione());
					liquidazioneIn.setNumeroLiquidazione( subOrdinativoPagamento.getLiquidazione().getNumeroLiquidazione());
					ricercaLiquidazioneK.setLiquidazione(liquidazioneIn);
					
					Liquidazione liquidazione = leggiLiquidazioneSubOrdinativo(richiedente, ricercaLiquidazioneK);

					if(null!=liquidazione){
						if(liquidazione.getStatoOperativoLiquidazione().name().equalsIgnoreCase(StatoOperativoLiquidazione.ANNULLATO.name())){
							addErroreCore(ErroreCore.ENTITA_NON_TROVATA, "LIQUIDAZIONE", subOrdinativoPagamento.getLiquidazione().getAnnoLiquidazione() 
									+ " / " + subOrdinativoPagamento.getLiquidazione().getNumeroLiquidazione());
							return false;
						} else {
							BigDecimal importoToCompare = BigDecimal.ZERO;
							importoToCompare = liquidazione.getDisponibilitaPagare();

							if(importoToCompare.compareTo(subOrdinativoPagamento.getImportoAttuale()) < 0){
								addErroreFin(ErroreFin.DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE, "LIQUIDAZIONE");
								return false;
							}
						}
						
						if(!liquidazione.getImpegno().isFlagFrazionabile()){
							tuttiImpegniFrazionabili = false;
						}
						
						if(getImpegno() == null){
							super.impegno = liquidazione.getImpegno();
							annoMovimenti = liquidazione.getImpegno().getAnnoMovimento();
						}
						
						
					} else {
						addErroreCore(ErroreCore.ENTITA_NON_TROVATA, "LIQUIDAZIONE", subOrdinativoPagamento.getLiquidazione().getAnnoLiquidazione() + " / " + subOrdinativoPagamento.getLiquidazione().getNumeroLiquidazione());
						return false;
					}
				}						
			}
			
			// Sub-ordinativi da aggiornare
			// PER OGNI SUBORDINATIVO
			// Importo Quota: deve sottostare alla disponibilita' della liquidazione secondo la regola che segue.
			// (DISPONIBILE A PAGARE SULLA LIQUIDAZIONE + VECCHIO IMPORTO QUOTA)  >=  IMPORTO QUOTA	
			// In caso il risultato sia falso il servizio restituisce l'errore
			if(null!=listaSubOrdinativiDaAggiornare && listaSubOrdinativiDaAggiornare.size() > 0){
				for(SubOrdinativo subOrdinativo : listaSubOrdinativiDaAggiornare){
					SubOrdinativoPagamento subOrdinativoPagamento = (SubOrdinativoPagamento) subOrdinativo;
					sommaImportiQuote = sommaImportiQuote.add(subOrdinativoPagamento.getImportoAttuale());
					
					RicercaLiquidazioneK ricercaLiquidazioneK = new RicercaLiquidazioneK();
					ricercaLiquidazioneK.setAnnoEsercizio(bilancio.getAnno());
					Liquidazione liquidazioneIn = new Liquidazione();
					liquidazioneIn.setAnnoLiquidazione(subOrdinativoPagamento.getLiquidazione().getAnnoLiquidazione());
					liquidazioneIn.setNumeroLiquidazione(subOrdinativoPagamento.getLiquidazione().getNumeroLiquidazione());
					ricercaLiquidazioneK.setLiquidazione(liquidazioneIn);
					
					Liquidazione liquidazione = leggiLiquidazioneSubOrdinativo(richiedente, ricercaLiquidazioneK);

					if(null!=liquidazione){
						if(liquidazione.getStatoOperativoLiquidazione().name().equalsIgnoreCase(StatoOperativoLiquidazione.ANNULLATO.name())){
							addErroreCore(ErroreCore.ENTITA_NON_TROVATA, subOrdinativoPagamento.getLiquidazione().getAnnoLiquidazione() + " / " + subOrdinativoPagamento.getLiquidazione().getNumeroLiquidazione());
							return false;
						} else {

							// estraggo il vecchio importo della quota
							BigDecimal importoSubOrdinativoDaDb = BigDecimal.ZERO;
						    if(null!=listaSubOrdinativiDaDb && listaSubOrdinativiDaDb.size() > 0){
						    	for(SubOrdinativoPagamento subOrdinativoPagamentoDaDb : listaSubOrdinativiDaDb){
						    		if(subOrdinativoPagamentoDaDb.getUid() == subOrdinativoPagamento.getUid()){
						    			importoSubOrdinativoDaDb = subOrdinativoPagamentoDaDb.getImportoAttuale();
						    		}			    		
						    	}
						    }

							BigDecimal importoToCompare = BigDecimal.ZERO;
							importoToCompare = importoSubOrdinativoDaDb.add(liquidazione.getDisponibilitaPagare());
							
							if(importoToCompare.compareTo(subOrdinativoPagamento.getImportoAttuale()) < 0){
								addErroreFin(ErroreFin.DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE, "LIQUIDAZIONE");
								return false;
							}
							
							if(!liquidazione.getImpegno().isFlagFrazionabile()){
								tuttiImpegniFrazionabili = false;
							}
							// cosi se l'ordinativo ha piu quote su impegni diversi, prendo cmq sempre e solo il primo 
							if(getImpegno() == null){
								super.impegno = liquidazione.getImpegno();
								annoMovimenti = liquidazione.getImpegno().getAnnoMovimento();
							}
						}			
						
						
					} else {
						addErroreCore(ErroreCore.ENTITA_NON_TROVATA, "LIQUIDAZIONE", subOrdinativoPagamento.getLiquidazione().getAnnoLiquidazione() + " / " + subOrdinativoPagamento.getLiquidazione().getNumeroLiquidazione());
						return false;
					}
				}						
			}
			
			//se non ci sono modifiche sui subOrdinativi carico il pdc dei conti da quelli vecchi
			// altrimenti l'innesto va in errore:
			

			// Somma Importi quote: deve rispettare il controllo di disponibilita' sul capitolo secondo la regola che segue.
			// (DISPONIBILE a PAGARE SUL CAPITOLO + VECCHIO importoOrdinativo) >= SOMMA IMPORTI QUOTE
			// In caso il risultato sia falso il servizio restituisce l'errore:
			List<ImportiCapitoloUG>  listaImportiCapitoloUG = capitoloUscitaGestione.getListaImportiCapitoloUG();
			if(null!=listaImportiCapitoloUG && listaImportiCapitoloUG.size() > 0) {
				for(ImportiCapitoloUG importoCapitoloUG : listaImportiCapitoloUG) {
					if(importoCapitoloUG.getAnnoCompetenza().intValue() == bilancio.getAnno()){

						BigDecimal importoToCompare = BigDecimal.ZERO;
						importoToCompare = importoOrdinativoDaDb.add(importoCapitoloUG.getDisponibilitaPagare());
					
						if(importoToCompare.compareTo(sommaImportiQuote) < 0){
							addErroreFin(ErroreFin.DISPONIBILITA_DI_CASSA_INSUFFICIENTE, "CAPITOLO");
							return false;
						}
					}
				}				
			}
			
			
			if(ordinativoPagamentoDad.isBilancioInStato(bilancio, Constanti.BIL_FASE_OPERATIVA_ESERCIZIO_PROVVISORIO, datiOperazione) &&
					annoMovimenti==bilancio.getAnno() &&
					tuttiImpegniFrazionabili){
				BigDecimal dispDodicesimi = ordinativoPagamentoDad.calcolaDisponibilitaAPagarePerDodicesimi(capitoloUscitaGestione.getUid());
				
				BigDecimal importoToCompare = BigDecimal.ZERO;
				importoToCompare = importoOrdinativoDaDb.add(dispDodicesimi);
			
				if(importoToCompare.compareTo(sommaImportiQuote) < 0){
					addErroreFin(ErroreFin.DISPONIBILITA_INSUFFICIENTE,"Ordinativo","dodicesimi");
					return false;
				}
			}
		
		
		} else {
			addErroreCore(ErroreCore.ENTITA_NON_TROVATA, "Ordinativo di pagamento", ordinativoDiPagamento.getAnno() + " / " + ordinativoDiPagamento.getNumero());
			return false;
		}
		return true;
	}
	
	
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="InserisceOrdinativoPagamentoService : checkServiceParam()";
		log.debug(methodName, " - Begin");

		//dati di input presi da request:
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		
		OrdinativoPagamento ordinativoDiPagamento = req.getOrdinativoPagamento();
		
		//lunghezza massima descrizioni:
		controlliDescrizioniOrdinativoPagamento(ordinativoDiPagamento);
		
		String elencoParamentriNonInizializzati = "";
		
		if(null==ente){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ENTE";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ENTE";
			}	
		}
		
		if(null==ordinativoDiPagamento){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ORDINATIVO_DI_PAGAMENTO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ORDINATIVO_DI_PAGAMENTO";
			}	
		}

		if(null==bilancio){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", BILANCIO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "BILANCIO";
			}	
		}

		if(null!=bilancio && bilancio.getAnno() == 0){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ANNO_DI_ESERCIZIO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ANNO_DI_ESERCIZIO";
			}	
		}

		if(null!=ordinativoDiPagamento && null==ordinativoDiPagamento.getContoTesoreria()){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", CONTO_DI_TESORERIA";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "CONTO_DI_TESORERIA";
			}	
		}
		
		if(null!=ordinativoDiPagamento && null==ordinativoDiPagamento.getCodiceBollo()){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", BOLLO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "BOLLO";
			}	
		}
		
		if(null!=ordinativoDiPagamento && null==ordinativoDiPagamento.getSoggetto()){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", CREDITORE_PAGAMENTO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "CREDITORE_PAGAMENTO";
			}	
		}
		
		if(null!=ordinativoDiPagamento && null!=ordinativoDiPagamento.getSoggetto() && null==ordinativoDiPagamento.getSoggetto().getElencoModalitaPagamento()){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", MODALITA_DI_PAGAMENTO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "MODALITA_DI_PAGAMENTO";
			}	
		}

		if(null!=ordinativoDiPagamento && null!=ordinativoDiPagamento.getSoggetto() && null!=ordinativoDiPagamento.getSoggetto().getElencoModalitaPagamento() && ordinativoDiPagamento.getSoggetto().getElencoModalitaPagamento().size() == 0){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", MODALITA_DI_PAGAMENTO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "MODALITA_DI_PAGAMENTO";
			}	
		}
		
		if(null!=ordinativoDiPagamento && null==ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento()){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ELENCO_SUBORDINATIVI_DI_PAGAMENTO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ELENCO_SUBORDINATIVI_DI_PAGAMENTO";
			}	
		}

		if(null!=ordinativoDiPagamento && null!=ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento() && ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento().size() == 0){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ELENCO_SUBORDINATIVI_DI_PAGAMENTO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ELENCO_SUBORDINATIVI_DI_PAGAMENTO";
			}	
		}

		if(null!=ordinativoDiPagamento && null!=ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento() && ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento().size() > 0){
			for(SubOrdinativoPagamento subOrdinativoPagamento : ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento()){
				if(null!=subOrdinativoPagamento && subOrdinativoPagamento.getLiquidazione()==null){
					if(elencoParamentriNonInizializzati.length() > 0){
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", LIQUIDAZIONE";
					}else{
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "LIQUIDAZIONE";
					}
					break;
				}				
			}
		}

		if(elencoParamentriNonInizializzati.length() > 0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParamentriNonInizializzati));
		}	
	}
}