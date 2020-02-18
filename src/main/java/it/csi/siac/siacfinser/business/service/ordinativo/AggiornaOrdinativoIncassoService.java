/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoIncasso;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoIncassoResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.OrdinativoIncassoDad;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoGestioneLigthDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoGestioneSubLigthDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInInserimentoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaOrdinativoPerChiaveDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubOrdinativoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoIncassoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaOrdinativoIncassoService extends AbstractInserisceAggiornaAnnullaOrdinativoIncassoService<AggiornaOrdinativoIncasso, AggiornaOrdinativoIncassoResponse> {

	@Autowired
	ProvvedimentoService provvedimentoService;

	@Autowired
	SoggettoFinDad soggettoDad;

	@Autowired
	LiquidazioneService liquidazioneService;
	
	@Autowired
	OrdinativoIncassoDad ordinativoIncassoDad; 
	
	@Autowired
	CommonDad commonDad;
	
	@Override
	@Transactional
	public AggiornaOrdinativoIncassoResponse executeService(AggiornaOrdinativoIncasso serviceRequest) {
		return super.executeService(serviceRequest);
	}	

	@Override
	protected void init() {
		final String methodName="AggiornaOrdinativoIncassoService : init()";
		log.debug(methodName, " - Begin");
	}	
	
	@Override
	public void execute() {
		String methodName = "AggiornaOrdinativoIncassoService - execute()";
		log.debug(methodName, " - Begin");
		
		//1. Vengono letti i valori ricevuti in input dalla request
		Richiedente richiedente = req.getRichiedente();
		ente = req.getEnte();
		
		setBilancio(req.getBilancio());
		OrdinativoIncasso ordinativoDiIncasso = req.getOrdinativoIncasso();
		CapitoloEntrataGestione capitoloEntrataGestione = ordinativoDiIncasso.getCapitoloEntrataGestione();
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, bilancio.getAnno());
		
		//3. Si caricano i dati dell'ordinativo di incasso per avere il riferimento prima della modifica:
		RicercaOrdinativoIncassoK pk = new RicercaOrdinativoIncassoK();
		pk.setOrdinativoIncasso(ordinativoDiIncasso);
		pk.setBilancio(bilancio);
		RicercaOrdinativoPerChiaveDto datiPerChiave = ordinativoIncassoDad.ricercaOrdinativoIncasso(pk, datiOperazione, richiedente);
		
		//4. Si inizializza l'oggetto OrdinativoInModificaInfoDto, dto di comodo specifico di questo servizio
		OrdinativoInModificaInfoDto datiModifica = ordinativoIncassoDad.getDatiGeneraliOrdinativoInAggiornamento(ordinativoDiIncasso, datiOperazione, bilancio, richiedente,ente);
		datiModifica.setDatiOrdinativo(datiPerChiave);
		datiModifica.setOrdinativo(ordinativoDiIncasso);
		
		//5. Si effettuano i vari controlli di merito definiti in analisi:
		boolean verificaCondizioniInserimento = verificaCondizioniPerAggiornamentoOrdinativoDiIncasso(datiOperazione, datiModifica);
		if(!verificaCondizioniInserimento){
			return;
		}
		
		//6. Gestione (eventuali) modifiche movimento di entrata:
		modificheDiAccertamentoESub(ordinativoDiIncasso, datiOperazione, datiModifica);
		
		//7. aggiornamento ordinativo di incasso (si invoca il metodo "core" rispetto all'operazione di aggiornamento di un ordinativo):
		OrdinativoIncasso ordinativoIncassoUpdated = (OrdinativoIncasso) ordinativoIncassoDad.aggiornaOrdinativo(ordinativoDiIncasso, datiModifica, datiOperazione, bilancio,richiedente,ente);
		
		//8. completa dati provvedimento
		List<AttoAmministrativo> listaAttoAmministrativo = new ArrayList<AttoAmministrativo>();
				
		RicercaProvvedimentoResponse ricercaProvvedimentoResponse = ricercaProvvedimento(richiedente, ordinativoIncassoUpdated.getAttoAmministrativo());
		
		listaAttoAmministrativo = ricercaProvvedimentoResponse.getListaAttiAmministrativi();
		
		if(null != listaAttoAmministrativo && listaAttoAmministrativo.size() > 0){
			AttoAmministrativo provvedimento = listaAttoAmministrativo.get(0);
			ordinativoIncassoUpdated.setAttoAmministrativo(provvedimento);
		}
		//
		
		if(null!=ordinativoIncassoUpdated){
			// Ci si predispone per l'eventuale doppia gestione
			ordinativoIncassoUpdated.setCapitoloEntrataGestione(capitoloEntrataGestione);
			//Viene clonato il model perche' diversamente da dei problemi durante l'aggiornamento della doppia gestione:
			OrdinativoIncasso ordinativoClone = clone(ordinativoIncassoUpdated);
		    SubOrdinativoInModificaInfoDto subInfoClone = clone(datiModifica.getInfoModificheSubOrdinativi());
			
		    //Chiamo il metodo che si occupa fi gestire la doppia gestione:
			EsitoControlliDto resDg = operazioniPerDoppiaGestione(ordinativoClone, bilancio, richiedente, ente, datiOperazione,subInfoClone,Operazione.INSERIMENTO);
			
			if(!isEmpty(resDg.getListaErrori())){
				//errori in doppia gestione:
				//Costruzione response esito negativo:
				res.setErrori(resDg.getListaErrori());
				res.setOrdinativoIncassoAggiornato(null);
				res.setEsito(Esito.FALLIMENTO);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return;
			}
			
			//innesto fin-gen
			// per caricare il pdc prendo il primo sub e leggo l'accertamento , come per l'impegno nell'ordinativo di pagamento
			SubOrdinativoIncasso subIncasso = ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso().get(0);
			Accertamento accertamento = subIncasso.getAccertamento();
			
			
			String codCapitoloSanitario = ordinativoDiIncasso.getCodCapitoloSanitarioSpesa()!=null ? ordinativoDiIncasso.getCodCapitoloSanitarioSpesa(): "";
			
			//SIAC-6970
			if(datiModifica.getInfoModificheSubOrdinativi() != null && datiModifica.getInfoModificheSubOrdinativi().getSubOrdinativiNuoviOModificatoImporto() != null && !datiModifica.getInfoModificheSubOrdinativi().getSubOrdinativiNuoviOModificatoImporto().isEmpty()) {
				log.info(methodName, "soddisfatte le condizioni per la chiamata alla contabilita' generale. Numero subordinativi con importo variato: " +  datiModifica.getInfoModificheSubOrdinativi().getSubOrdinativiNuoviOModificatoImporto().size());
				gestisciRegistrazioneGENPerOrdinativo(ordinativoIncassoUpdated, accertamento, TipoCollegamento.ORDINATIVO_INCASSO, true, false, codCapitoloSanitario);
			}else {
				log.info(methodName, "condizioni per la contabilita' generale non soddisfatte.");
			}
			
			
			
			//Costruzione response esito positivo:
			res.setOrdinativoIncassoAggiornato(ordinativoIncassoUpdated);
			res.setEsito(Esito.SUCCESSO);
			return;
		} else {
			//Costruzione response esito negativo:
			res.setOrdinativoIncassoAggiornato(null);
			res.setEsito(Esito.FALLIMENTO);
			return;
		}		
	}
	
	
	/**
	 * si occupra delle gestioone delle modifiche movimento entrata
	 * @param ordinativoDiIncasso
	 * @param datiOperazione
	 * @param datiModifica
	 */
	private void modificheDiAccertamentoESub(OrdinativoIncasso ordinativoDiIncasso,DatiOperazioneDto datiOperazione,OrdinativoInModificaInfoDto datiModifica){
		AttoAmministrativo attoAmministrativo = ordinativoDiIncasso.getAttoAmministrativo();
		//MODIFICHE DI ACCERTAMENTO E SUB:
		List<MovimentoGestioneLigthDto> listaDistintiAccertamentiAssociatiAQuote = datiModifica.getListaDistintiAccertamentiAssociatiAQuote();
		List<MovimentoGestioneSubLigthDto> listaDistintiSubccertamentiAssociatiAQuote = datiModifica.getListaDistintiSubAccertamentiAssociatiAQuote();
		modificheDiAccertamentoESub_Routine(listaDistintiAccertamentiAssociatiAQuote, false, attoAmministrativo, datiOperazione);
		modificheDiAccertamentoESub_Routine(listaDistintiSubccertamentiAssociatiAQuote, true, attoAmministrativo, datiOperazione);
	}
	
	/**
	 *  controlli 2.5.3
	 * @param datiOperazione
	 * @param datiModifica
	 * @return
	 */
	private boolean verificaCondizioniPerAggiornamentoOrdinativoDiIncasso(DatiOperazioneDto datiOperazione,OrdinativoInModificaInfoDto datiModifica){
		
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		OrdinativoIncasso ordinativoDiIncasso = req.getOrdinativoIncasso();
		CapitoloEntrataGestione capitoloEntrataGestione = ordinativoDiIncasso.getCapitoloEntrataGestione();
		Soggetto soggetto = ordinativoDiIncasso.getSoggetto();
		AttoAmministrativo attoAmministrativo = ordinativoDiIncasso.getAttoAmministrativo();
		List<SubOrdinativoIncasso> elencoSubOrdinativiDiIncasso = ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso();
		
		boolean controllo = true; 
		
		//(passo 1 di 2.5.3)
		controllo = esistenzaOrdinativoIncasso(ordinativoDiIncasso, ente, richiedente,datiModifica);
		if(!controllo){
			return false;
		}
		
		//(passo 2 di 2.5.3)
		controllo = controlloStatoOperativoOrdinativoDiIncasso(ordinativoDiIncasso, ente, richiedente,datiModifica);
		if(!controllo){
			return false;
		}
		
		//(passo 3 di 2.5.3)
		controllo = controlloOrdinativoDiIncassoCollegatoAdAltroOrdinativo(ordinativoDiIncasso, ente, richiedente,datiModifica);
		if(!controllo){
			return false;
		}
		
		//(passo 4 di 2.5.3)
		controllo = controlloSedeSoggetto(ordinativoDiIncasso, ente, richiedente,datiModifica);
		if(!controllo){
			return false;
		}
		
		//(passo 5 di 2.5.3)
		controllo = controlloTransazioneElementare(ordinativoDiIncasso, ente, richiedente,datiModifica);
		if(!controllo){
			return false;
		}
		
		//(passo 6 di 2.5.3)
		controllo = validitaDisponibilitaAccertamentiSubAccertamenti(ordinativoDiIncasso, datiOperazione, datiModifica);
		if(!controllo){
			return false;
		}
		
		//(passo 7 di 2.5.3)
		controllo = verificheOrdinativoACopertura(ordinativoDiIncasso, ente, richiedente,datiModifica);
		if(!controllo){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Effettua i controlli al passo 1 di 2.5.3, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param ordinativoDiIncasso
	 * @param ente
	 * @param richiedente
	 * @return
	 */
	private boolean esistenzaOrdinativoIncasso(OrdinativoIncasso ordinativoDiIncasso,Ente ente, Richiedente richiedente,OrdinativoInModificaInfoDto datiModifica){
		boolean esiste = false;
		if(datiModifica!=null && datiModifica.getOrdinativo()!=null && datiModifica.getOrdinativo().getNumero()!=null){
			esiste = true;
		}
		if(!esiste){
			addErroreFin(ErroreFin.ORDINATIVO_INESISTENTE);
		}
		return esiste;
	}
	
	/**
	 * Effettua i controlli al passo 2 di 2.5.3, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param ordinativoDiIncasso
	 * @param ente
	 * @param richiedente
	 * @return
	 */
	private boolean controlloStatoOperativoOrdinativoDiIncasso(OrdinativoIncasso ordinativoDiIncasso,Ente ente, Richiedente richiedente,OrdinativoInModificaInfoDto datiModifica){
		OrdinativoIncasso caricatoDaDb = datiModifica.getDatiOrdinativo().getOrdinativoIncasso();
		if(Constanti.D_ORDINATIVO_STATO_ANNULLATO.equalsIgnoreCase(Constanti.statoOperativoOrdinativoEnumToString(caricatoDaDb.getStatoOperativoOrdinativo()))){
			addErroreFin(ErroreFin.OPERAZIONE_NON_POSSIBILE, "AGGIORNAMENTO ORDINATIVO");
			return false;
		}
		return true;
	}
	
	/**
	 * Effettua i controlli al passo 3 di 2.5.3, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param ordinativoDiIncasso
	 * @param ente
	 * @param richiedente
	 * @return
	 */
	private boolean controlloOrdinativoDiIncassoCollegatoAdAltroOrdinativo(OrdinativoIncasso ordinativoDiIncasso,Ente ente, Richiedente richiedente,OrdinativoInModificaInfoDto datiModifica){
		//si tratta di siac_r_ordinativo ?
		return true;
	}
	
	/**
	 * Effettua i controlli al passo 4 di 2.5.3, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param ordinativoDiIncasso
	 * @param ente
	 * @param richiedente
	 * @return
	 */
	private boolean controlloSedeSoggetto(OrdinativoIncasso ordinativoDiIncasso,Ente ente, Richiedente richiedente,OrdinativoInModificaInfoDto datiModifica){
		Integer idEnte = ente.getUid();
		OrdinativoIncasso caricatoDaDb = datiModifica.getDatiOrdinativo().getOrdinativoIncasso();
		SedeSecondariaSoggetto sedeDaFrontEnd = CommonUtils.getFirst(ordinativoDiIncasso.getSoggetto().getSediSecondarie());
		if(sedeDaFrontEnd!=null){
			SedeSecondariaSoggetto sedeSulDb = CommonUtils.getFirst(caricatoDaDb.getSoggetto().getSediSecondarie());
			if(sedeSulDb==null || !StringUtils.sonoUguali(sedeDaFrontEnd.getCodiceSedeSecondaria(), sedeSulDb.getCodiceSedeSecondaria())){
				//se la sede e' cambiata bisogna assicurarsi che sia valida
				
				Soggetto soggettoDb = caricatoDaDb.getSoggetto();
				String codSoggetto = soggettoDb.getCodiceSoggetto();
				Integer idSedeSecondaria = sedeDaFrontEnd.getUid();
				
				SedeSecondariaSoggetto sedeCaricata = soggettoDad.ricercaSedeSecondariaPerChiave(idEnte, codSoggetto, idSedeSecondaria);	
				
				boolean sedeOk = true;
				if(sedeCaricata !=null){
					if (!StatoOperativoSedeSecondaria.VALIDO.name().equals(sedeCaricata.getStatoOperativoSedeSecondaria().name())){
						sedeOk = false;
					}
				} else {
					sedeOk = false;
				}
				if(!sedeOk){
					addErroreFin(ErroreFin.SEDE_SECONDARIA_SOGGETTO_NON_VALIDA);
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Effettua i controlli al passo 5 di 2.5.3, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param ordinativoDiIncasso
	 * @param ente
	 * @param richiedente
	 * @return
	 */
	private boolean controlloTransazioneElementare(OrdinativoIncasso ordinativoDiIncasso,Ente ente, Richiedente richiedente,OrdinativoInModificaInfoDto datiModifica){
		//DA NON SVILUPPARE IN V1
		return true;
	}
	
	/**
	 * Effettua i controlli al passo 6 di 2.5.3, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param ordinativoDiIncasso
	 * @param ente
	 * @param richiedente
	 * @return
	 */
	private boolean validitaDisponibilitaAccertamentiSubAccertamenti(OrdinativoIncasso ordinativoDiIncasso,DatiOperazioneDto datiOperazione,OrdinativoInModificaInfoDto datiModifica){
		Bilancio bilancio = req.getBilancio();
		
		OrdinativoInInserimentoInfoDto datiInserimento = new OrdinativoInInserimentoInfoDto();
		boolean controlli = controlloValiditaAccertamentiSubAccertamenti(ordinativoDiIncasso, bilancio, datiInserimento, datiOperazione, datiModifica.getInfoModificheSubOrdinativi());
		datiModifica.setListaDistintiAccertamentiAssociatiAQuote(datiInserimento.getListaDistintiAccertamentiAssociatiAQuote());
		datiModifica.setListaDistintiSubAccertamentiAssociatiAQuote(datiInserimento.getListaDistintiSubAccertamentiAssociatiAQuote());
		
		if(!controlli){
			return false;
		}
		
		//2.5.4:
		//controlli = calcoloDisponibilitaAccertamentoSubAccertamento(ordinativoDiIncasso, bilancio,datiInserimento, datiOperazione);
		if(!controlli){
			return false;
		}
		
		return true;
	}
	
	private boolean calcoloDisponibilitaAccertamentoSubAccertamento(OrdinativoIncasso ordinativoDiIncasso, Bilancio bilancio,
			OrdinativoInInserimentoInfoDto datiInserimento, DatiOperazioneDto datiOperazione) {
	
		if(datiInserimento!=null){
			
			//le due liste listaDistintiAccertamentiAssociatiAQuote e listaDistintiSubAccertamentiAssociatiAQuote
			//rappresentano i dati ricevuti dal frontend ARRICCHITI di dati letti sul DB nel metodo precedente "controlloValiditaAccertamentiSubAccertamenti"
			List<MovimentoGestioneLigthDto> listaDistintiAccertamentiAssociatiAQuote = datiInserimento.getListaDistintiAccertamentiAssociatiAQuote();
			List<MovimentoGestioneSubLigthDto> listaDistintiSubAccertamentiAssociatiAQuote = datiInserimento.getListaDistintiSubAccertamentiAssociatiAQuote();
			
			
			//ACCERTAMENTI NELLE QUOTE:
			if(listaDistintiAccertamentiAssociatiAQuote!=null && listaDistintiAccertamentiAssociatiAQuote.size()>0){
				//dal front ent abbiamo ricevuto quote con accertamenti collegati
				for(MovimentoGestioneLigthDto accIt : listaDistintiAccertamentiAssociatiAQuote){
					
					//disponibilita ad incassare e' stato letto dal db nel metodo "controlloValiditaAccertamentiSubAccertamenti"
					BigDecimal disponibilitaAIncassare = accIt.getDisponibilitaAIncassare();
					/////////////////////////////////////////////////////////////////////////
					
					ArrayList<SubOrdinativoIncasso> quoteRicevuteDaFrontEnd= accIt.getQuoteRicevuteDaFrontEnd();
					if(quoteRicevuteDaFrontEnd!=null && quoteRicevuteDaFrontEnd.size()>0){
						//quoteRicevuteDaFrontEnd sono le quote ricevute dal frontend che incidono tutte sull'accertamento iterato
						
						//per adesso siamo in inserimento e non considero le varie possibilita' del caso d'uso aggiorna
						BigDecimal sommaImporti = BigDecimal.ZERO;
						for(SubOrdinativoIncasso quotaIt : quoteRicevuteDaFrontEnd){
							BigDecimal importoQuotaIt = quotaIt.getImportoAttuale();
							sommaImporti = sommaImporti.add(importoQuotaIt);
						}
						
						if(disponibilitaAIncassare.compareTo(sommaImporti)<0){
							//disponibilita a incassare non puo' essere minore della somma degli importi:
							addErroreFin(ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNO, accIt.getAnnoMovimento() + "/" + accIt.getNumeroMovimento(), "inserimento ordinativo di incasso");
							return false;
						}
						
					}
				}
			}
			
			//SUB-ACCERTAMENTI NELLE QUOTE:
			if(listaDistintiSubAccertamentiAssociatiAQuote!=null && listaDistintiSubAccertamentiAssociatiAQuote.size()>0){
				
				//dal front ent abbiamo ricevuto quote con sub accertamenti collegati
				for(MovimentoGestioneSubLigthDto subAccIt : listaDistintiSubAccertamentiAssociatiAQuote){
					
					//disponibilita ad incassare e' stato letto dal db nel metodo "controlloValiditaAccertamentiSubAccertamenti"
					BigDecimal disponibilitaAIncassare = subAccIt.getDisponibilitaAIncassare();
					/////////////////////////////////////////////////////////////////////////
					
					ArrayList<SubOrdinativoIncasso> quoteRicevuteDaFrontEnd= subAccIt.getQuoteRicevuteDaFrontEnd();
					if(quoteRicevuteDaFrontEnd!=null && quoteRicevuteDaFrontEnd.size()>0){
						//quoteRicevuteDaFrontEnd sono le quote ricevute dal frontend che incidono tutte sul sub-accertamento iterato
						
						//per adesso siamo in inserimento e non considero le varie possibilita' del caso d'uso aggiorna
						BigDecimal sommaImporti = BigDecimal.ZERO;
						for(SubOrdinativoIncasso quotaIt : quoteRicevuteDaFrontEnd){
							BigDecimal importoQuotaIt = quotaIt.getImportoAttuale();
							sommaImporti = sommaImporti.add(importoQuotaIt);
						}
						
						if(disponibilitaAIncassare.compareTo(sommaImporti)<0){
							//disponibilita a incassare non puo' essere minore della somma degli importi:
							addErroreFin(ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNO, subAccIt.getAnnoMovimento() + "/" + subAccIt.getNumeroMovimento(), "inserimento ordinativo di incasso");
							return false;
						}
						
					}
				}
			}
			
		}
		
		return true;
	}
	
	/**
	 * Effettua i controlli al passo 7 di 2.5.3, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param ordinativoDiIncasso
	 * @param ente
	 * @param richiedente
	 * @return
	 */
	private boolean verificheOrdinativoACopertura(OrdinativoIncasso ordinativoDiIncasso,Ente ente, Richiedente richiedente,OrdinativoInModificaInfoDto datiModifica){
		//TODO: aspettiamo il ricerca ordinativo incasso per chiave service
		return true;
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="InserisceOrdinativoIncassoService : checkServiceParam()";
		log.debug(methodName, " - Begin");

		//dati di input presi da request:
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		
		OrdinativoIncasso ordinativoDiIncasso = req.getOrdinativoIncasso();
		
		//lunghezza massima descrizioni:
		controlliDescrizioniOrdinativoIncasso(ordinativoDiIncasso);

		String elencoParamentriNonInizializzati = "";
		
		if(ente==null){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ENTE";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ENTE";
			}	
		}
		
		if(ordinativoDiIncasso==null){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ORDINATIVO_DI_INCASSO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ORDINATIVO_DI_INCASSO";
			}	
		} else {
			if(ordinativoDiIncasso.getCapitoloEntrataGestione()==null){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", CAPITOLO_ENTRATA";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "CAPITOLO_ENTRATA";
				}	
			}

			if(ordinativoDiIncasso.getCodPdc()==null || (ordinativoDiIncasso.getCodPdc()!=null && ordinativoDiIncasso.getCodPdc().isEmpty())){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ELEMENTO_PIANO_DEI_CONTI";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ELEMENTO_PIANO_DEI_CONTI";
				}	
			}
			
//			CR-2023 si elimina
//			if(ordinativoDiIncasso.getCodContoEconomico()==null || (ordinativoDiIncasso.getCodContoEconomico()!=null && ordinativoDiIncasso.getCodContoEconomico().isEmpty())){
//				if(elencoParamentriNonInizializzati.length() > 0){
//					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", PIANO_DEI_CONTI_ECONOMICO";
//				}else{
//					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "PIANO_DEI_CONTI_ECONOMICO";
//				}	
//			}

			if(ordinativoDiIncasso.getContoTesoreria()==null || (ordinativoDiIncasso.getContoTesoreria()!=null && StringUtils.isEmpty(ordinativoDiIncasso.getContoTesoreria().getCodice()))){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", CONTO_DI_TESORERIA";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "CONTO_DI_TESORERIA";
				}	
			}

			if(ordinativoDiIncasso.getCodiceBollo()==null || (ordinativoDiIncasso.getCodiceBollo()!=null && StringUtils.isEmpty(ordinativoDiIncasso.getCodiceBollo().getCodice()))){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", BOLLO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "BOLLO";
				}	
			}

			if(ordinativoDiIncasso.getSoggetto() == null){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", DEBITORE_INCASSO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "DEBITORE_INCASSO";
				}	
			} else {
				if(ordinativoDiIncasso.getSoggetto().getSediSecondarie()!=null && ordinativoDiIncasso.getSoggetto().getSediSecondarie().size() != 1 ){
					if(elencoParamentriNonInizializzati.length() > 0){
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", SEDE_SECONDARIA_SOGGETTO_NON_UNIVOCA";
					}else{
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "SEDE_SECONDARIA_SOGGETTO_NON_UNIVOCA";
					}	
				}
			}
			
			if(ordinativoDiIncasso.getAttoAmministrativo()==null){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ATTO_AMMINISTRATIVO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ATTO_AMMINISTRATIVO";
				}	
			}
			
			if(ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso()==null){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ELENCO_SUBORDINATIVI_DI_INCASSO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ELENCO_SUBORDINATIVI_DI_INCASSO";
				}	
			} else {
				if(ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso().size() == 0){
					if(elencoParamentriNonInizializzati.length() > 0){
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ELENCO_SUBORDINATIVI_DI_INCASSO";
					}else{
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ELENCO_SUBORDINATIVI_DI_INCASSO";
					}	
				} else {
					if(ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso().size() > 0){
						for(SubOrdinativoIncasso subOrdinativoIncasso : ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso()){
							if(null!=subOrdinativoIncasso && subOrdinativoIncasso.getAccertamento()==null){
								if(elencoParamentriNonInizializzati.length() > 0){
									elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ACCERTAMENTO";
								} else {
									elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ACCERTAMENTO";
								}
								
								break;
							}				
						}
					}
				}
			}
		}

		if(bilancio==null){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", BILANCIO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "BILANCIO";
			}	
		} else {
			if(bilancio.getAnno() == 0){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ANNO_DI_ESERCIZIO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ANNO_DI_ESERCIZIO";
				}	
			}
		}

		if(!StringUtils.isEmpty(elencoParamentriNonInizializzati)){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParamentriNonInizializzati));
		}	
	}
}