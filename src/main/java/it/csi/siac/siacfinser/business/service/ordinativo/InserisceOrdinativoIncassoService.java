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
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoIncasso;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoIncassoResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoGestioneLigthDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoGestioneSubLigthDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInInserimentoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubOrdinativoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceOrdinativoIncassoService extends AbstractInserisceAggiornaAnnullaOrdinativoIncassoService<InserisceOrdinativoIncasso, InserisceOrdinativoIncassoResponse> {

	@Autowired
	ProvvedimentoService provvedimentoService;
	
	@Autowired
	CapitoloEntrataGestioneService capitoloEntrataGestioneService;

	@Autowired
	SoggettoFinDad soggettoDad;
	
	@Autowired
	CommonDad commonDad;
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	
	@Override
	@Transactional
	public InserisceOrdinativoIncassoResponse executeService(InserisceOrdinativoIncasso serviceRequest) {
		return super.executeService(serviceRequest);
	}	

	@Override
	protected void init() {
		final String methodName="InserisceOrdinativoIncassoService : init()";
		log.debug(methodName, " - Begin");
	}	

	@Override
	public void execute() {
		String methodName = "InserisceOrdinativoIncassoService - execute()";
		log.debug(methodName, " - Begin");

		//1. Vengono letti i valori ricevuti in input dalla request
		Richiedente richiedente = req.getRichiedente();
		ente = req.getEnte();
		setBilancio(req.getBilancio());
		OrdinativoIncasso ordinativoDiIncasso = req.getOrdinativoIncasso();
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, bilancio.getAnno());
		
		//3. Si inizializza l'oggetto OrdinativoInInserimentoInfoDto, dto di comodo specifico di questo servizio
		OrdinativoInInserimentoInfoDto datiInserimento = new OrdinativoInInserimentoInfoDto();
		datiInserimento.setBilancio(bilancio);
		datiInserimento.setOrdinativo(ordinativoDiIncasso);
		
		//4. Vengono eseguiti i vari controlli previsti da analisi al paragrafo "controlli 2.5.2":
		boolean verificaCondizioniInserimento = verificaCondizioniPerInserimentoOrdinativoDiIncasso(datiOperazione,datiInserimento);
		if(!verificaCondizioniInserimento){
			return;
		}
		
		
		//5. Gestione (eventuali) modifiche movimento di entrata:
		
		boolean inserisciModificheAccertamento = datiInserimento.getListaDistintiAccertamentiAssociatiAQuote()!=null && !datiInserimento.getListaDistintiAccertamentiAssociatiAQuote().isEmpty();
		boolean inserisciModificheSubAccertamento = datiInserimento.getListaDistintiSubAccertamentiAssociatiAQuote()!=null && !datiInserimento.getListaDistintiSubAccertamentiAssociatiAQuote().isEmpty();
		
		List<Integer> uidsTestateConInserimentoModifica = new ArrayList<Integer>();
		
		if(inserisciModificheAccertamento){
			List<Integer> uidsAccertamenti = modificheDiAccertamentoESub_Routine(datiInserimento.getListaDistintiAccertamentiAssociatiAQuote(), false, ordinativoDiIncasso.getAttoAmministrativo(), datiOperazione);
			uidsTestateConInserimentoModifica.addAll(uidsAccertamenti);
		}
				
		if(inserisciModificheSubAccertamento){
			List<Integer> uidsAccertamenti = modificheDiAccertamentoESub_Routine(datiInserimento.getListaDistintiSubAccertamentiAssociatiAQuote(), true, ordinativoDiIncasso.getAttoAmministrativo(), datiOperazione);
			uidsTestateConInserimentoModifica.addAll(uidsAccertamenti);
		}
		
		
		
		//6. inserimento ordinativo di incasso (si invoca il metodo "core" rispetto all'operazione di inserimento di un nuovo ordinativo):
		OrdinativoIncasso ordinativoIncassoInsert = (OrdinativoIncasso) ordinativoIncassoDad.inserisciOrdinativoIncasso(ordinativoDiIncasso, datiInserimento, datiOperazione);
		
		if(null!=ordinativoIncassoInsert){
			
			// Doppia Gestione: 
			// 1.chiamo il metodo valutaSubOrdinativi con idOrdinativo = null perche' voglio ottenere
			//	 solo i subordinativi "da inserire", dato che siamo nel servizio di inserisce ordinativo saranno tutti "da inserire"
			//   infoSub e' quindi un dto di comodo per la doppia gestione
			SubOrdinativoInModificaInfoDto infoSub = ordinativoIncassoDad.valutaSubOrdinativi(ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso(), null, datiOperazione,
					bilancio,richiedente,Constanti.ORDINATIVO_TIPO_INCASSO,ente);
			
			//2. Routine di doppia gestione (dentro viene eseguita solo se siamo in doppia gestione):
			EsitoControlliDto resDg = operazioniPerDoppiaGestione(ordinativoDiIncasso, bilancio, richiedente, ente, datiOperazione,infoSub,Operazione.INSERIMENTO, uidsTestateConInserimentoModifica);
			
			if(!isEmpty(resDg.getListaErrori())){
				//errori in doppia gestione:
				//Costruzione response esito negativo:
				res.setErrori(resDg.getListaErrori());
				res.setOrdinativoIncassoInserito(null);
				res.setEsito(Esito.FALLIMENTO);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return;
			}
			
			// innesto fin-gen
			// per caricare il pdc prendo il primo sub e leggo l'accertamento , come per l'impegno nell'ordinativo di pagamento
			SubOrdinativoIncasso subIncasso = ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso().get(0);
			Accertamento accertamento = subIncasso.getAccertamento();
			
			String codCapitoloSanitario = ordinativoDiIncasso.getCodCapitoloSanitarioSpesa()!=null ? ordinativoDiIncasso.getCodCapitoloSanitarioSpesa(): "";
			
			gestisciRegistrazioneGENPerOrdinativo(ordinativoIncassoInsert,accertamento, TipoCollegamento.ORDINATIVO_INCASSO, false, false, codCapitoloSanitario);
			
			
			//3. Esito ok, costruzione response
			res.setOrdinativoIncassoInserito(ordinativoIncassoInsert);
			res.setEsito(Esito.SUCCESSO);
			return;
		} else {
			//Esito KO
			res.setOrdinativoIncassoInserito(null);
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
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

//			if(ordinativoDiIncasso.getContoTesoreria()==null || (ordinativoDiIncasso.getContoTesoreria()!=null && StringUtils.isEmpty(ordinativoDiIncasso.getContoTesoreria().getCodice()))){
//				if(elencoParamentriNonInizializzati.length() > 0){
//					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", CONTO_DI_TESORERIA";
//				}else{
//					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "CONTO_DI_TESORERIA";
//				}	
//			}

			// Jira-1205 
			// questo controllo e' superfluo
//			if(ordinativoDiIncasso.getNoteTesoriere()==null || (ordinativoDiIncasso.getNoteTesoriere()!=null && StringUtils.isEmpty(ordinativoDiIncasso.getNoteTesoriere().getCodice()))){
//				if(elencoParamentriNonInizializzati.length() > 0)
//					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", NOTE_TESORIERE";
//				else
//					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "NOTE_TESORIERE";
//			}

			if(ordinativoDiIncasso.getCodiceBollo()==null || (ordinativoDiIncasso.getCodiceBollo()!=null && StringUtils.isEmpty(ordinativoDiIncasso.getCodiceBollo().getCodice()))){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", BOLLO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "BOLLO";
				}	
			}

			
			// CR - 3746 il siope diventa obbligatorio
			if(StringUtils.isEmpty(ordinativoDiIncasso.getCodSiope())){
				
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", SIOPE";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "SIOPE";
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
					if(elencoParamentriNonInizializzati.length() > 0)
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ELENCO_SUBORDINATIVI_DI_INCASSO";
					else
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ELENCO_SUBORDINATIVI_DI_INCASSO";
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
	
	/**
	 * controlli 2.5.2
	 * @param soggetto
	 * @param ente
	 * @param richiedente
	 * @return
	 */
	private boolean verificaCondizioniPerInserimentoOrdinativoDiIncasso(DatiOperazioneDto datiOperazione,OrdinativoInInserimentoInfoDto datiInserimento){
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		OrdinativoIncasso ordinativoDiIncasso = req.getOrdinativoIncasso();
		CapitoloEntrataGestione capitoloEntrataGestione = ordinativoDiIncasso.getCapitoloEntrataGestione();
		Soggetto soggetto = ordinativoDiIncasso.getSoggetto();
		AttoAmministrativo attoAmministrativo = ordinativoDiIncasso.getAttoAmministrativo();
		//List<SubOrdinativoIncasso> elencoSubOrdinativiDiIncasso = ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso();
		
		//CONTROLLO capitolo (passo 2 di 2.5.2)
		boolean controlloCapitolo = controlliCapitolo(bilancio, capitoloEntrataGestione, ente, richiedente);
		if(!controlloCapitolo){
			return false;
		}
		
		//CONTROLLO Soggetto (punto 3 di 2.5.2)
		// Soggetto: Verifica che il soggetto sia VALIDO o SOSPESO
		boolean controlliSoggetto = controlliSoggetto(soggetto, ente, richiedente,datiOperazione);
		if(!controlliSoggetto){
			return false;
		}
		
		
		//CONTROLLO Atto amministrativo (punto 4 di 2.5.2)
		// Atto Amministrativo: Verifica che il PROVVEDIMENTO sia univoco e che il suo stato non sia ANNULLATO
		boolean controlliAttoAmm =controlliAttoAmministrativo(attoAmministrativo, ente, richiedente);
		if(!controlliAttoAmm){
			return false;
		}
		
		//CONTROLLO Distinta (punto 6 di 2.5.2)
		boolean esisteDistinta = controlloDistinta(ordinativoDiIncasso, ente, datiOperazione);
		if(!esisteDistinta){
			return false;
		}
		
		//CONTROLLO Conto tesoreria (punto 7 di 2.5.2)
		boolean esisteContoTes = controlloContoTesoreria(ordinativoDiIncasso, ente, datiOperazione);
		if(!esisteContoTes){
			return false;
		}
		
		//CONTROLLO validita Accertamenti (punto 9 di 2.5.2)
		boolean controlloValidAcc =controlloValiditaAccertamentiSubAccertamenti(ordinativoDiIncasso, bilancio, datiInserimento,datiOperazione,null);
		if(!controlloValidAcc){
			return false;
		}
		
		//CONTROLLO Competenza Accertamenti (punto 8 di 2.5.2)
		boolean controlloCompetenzaAccertamenti = controlloCompetenzaAccertamenti(ordinativoDiIncasso, bilancio);
		if(!controlloCompetenzaAccertamenti){
			return false;
		}
		
		
		
		// Nel caso in cui il chiamante abbia indicato il campo FORZA = TRUE questo controllo non deve essere effettuato,
		// se non indicato il  campo forza si intende a FALSE
		if(!datiInserimento.getOrdinativo().isForza()){
			//CONTROLLO disponibilita' Accertamento-subaccertamento (punto 9 di 2.5.2 - solo il richiamo a 2.5.4)
			boolean controlloDisAccSubAcc =calcoloDisponibilitaAccertamentoSubAccertamento(ordinativoDiIncasso, bilancio, richiedente, ente, datiInserimento,datiOperazione);
			if(!controlloDisAccSubAcc){
				return false;
			}
		}
		//CONTROLLO Confruenza debitore ordinativo incasso (punto 10 di 2.5.2)
		boolean controlloCongruenzaIncasso = controlloCongruenzaDebitoreOrdinativoIncasso(ordinativoDiIncasso, bilancio, richiedente, ente, datiInserimento, datiOperazione);
		if(!controlloCongruenzaIncasso){
			return false;
		}
		
		//CONTROLLO verifiche provvisorio di cassa(punto 11 di 2.5.2)
		List<Errore> erroriVpc = ordinativoIncassoDad.verificheProvvisorioDiCassa(ordinativoDiIncasso,datiOperazione,datiInserimento);
		if(erroriVpc!=null && erroriVpc.size()>0){
			res.addErrori(erroriVpc);
			res.setOrdinativoIncassoInserito(null);
			return false;
		}
		
		return true;
	}
	
	/**
	 * si occupra delle gestioone delle modifiche movimento entrata
	 * @param ordinativoDiIncasso
	 * @param datiOperazione
	 * @param datiInserimento
	 */
	private void modificheDiAccertamentoESub(OrdinativoIncasso ordinativoDiIncasso,DatiOperazioneDto datiOperazione,OrdinativoInInserimentoInfoDto datiInserimento){
		//Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
		AttoAmministrativo attoAmministrativo = ordinativoDiIncasso.getAttoAmministrativo();
		//MODIFICHE DI ACCERTAMENTO E SUB:
		List<MovimentoGestioneLigthDto> listaDistintiAccertamentiAssociatiAQuote = datiInserimento.getListaDistintiAccertamentiAssociatiAQuote();
		List<MovimentoGestioneSubLigthDto> listaDistintiSubccertamentiAssociatiAQuote = datiInserimento.getListaDistintiSubAccertamentiAssociatiAQuote();
		modificheDiAccertamentoESub_Routine(listaDistintiAccertamentiAssociatiAQuote, false, attoAmministrativo, datiOperazione);
		modificheDiAccertamentoESub_Routine(listaDistintiSubccertamentiAssociatiAQuote, true, attoAmministrativo, datiOperazione);
	}

	

	
	
	/**
	 * Effettua i controlli al passo 2 di 2.5.2, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param bilancio
	 * @param capitoloEntrataGestione
	 * @param ente
	 * @param richiedente
	 * @return
	 */
	private boolean controlliCapitolo(Bilancio bilancio,CapitoloEntrataGestione capitoloEntrataGestione, Ente ente, Richiedente richiedente){
		
		RicercaSinteticaCapitoloEntrataGestione req = new RicercaSinteticaCapitoloEntrataGestione();
		RicercaSinteticaCapitoloEGest params = new RicercaSinteticaCapitoloEGest();
		
		params.setAnnoEsercizio(bilancio.getAnno());
		if(capitoloEntrataGestione.getNumeroCapitolo() != null) {
			params.setNumeroCapitolo(capitoloEntrataGestione.getNumeroCapitolo());
		}
		if(capitoloEntrataGestione.getNumeroArticolo() != null) {
			params.setNumeroArticolo(capitoloEntrataGestione.getNumeroArticolo());
		}
		if(capitoloEntrataGestione.getNumeroUEB() != null) {
			params.setNumeroUEB(capitoloEntrataGestione.getNumeroUEB());
		}
		req.setParametriPaginazione(new ParametriPaginazione());
		req.setEnte(ente);
		req.setRichiedente(richiedente);
		req.setRicercaSinteticaCapitoloEntrata(params);
		RicercaSinteticaCapitoloEntrataGestioneResponse respCapEnt = capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(req);
		
		boolean isCapOk = true;
		if(respCapEnt!=null){
			if(respCapEnt.getCapitoli() != null && respCapEnt.getCapitoli().size() == 1) {
				CapitoloEntrataGestione ceg = respCapEnt.getCapitoli().get(0);
				if(!ceg.getStato().equals(StatoEntita.VALIDO)){
					isCapOk = false;
				}				
			} else {
				isCapOk = false;
			}
		} else { 
			isCapOk = false;
		}
		
		if(isCapOk == false){
			//controlli 2.5.2 passo 2
			addErroreBil(ErroreBil.CAPITOLO_DI_ENTRATA_NON_ACCERTABILE);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Effettua i controlli al passo 3 di 2.5.2, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param soggetto
	 * @param ente
	 * @param richiedente
	 * @return
	 */
	private boolean controlliSoggetto(Soggetto soggetto,Ente ente, Richiedente richiedente, DatiOperazioneDto datiOperazione){
		Soggetto soggettoCheck = soggettoDad.ricercaSoggetto(Constanti.AMBITO_FIN, ente.getUid(), soggetto.getCodiceSoggetto(), false, true);
		if(null==soggettoCheck){
			addErroreFin(ErroreFin.SOGGETTO_NON_VALIDO);
			return false;
		} else if (!soggettoCheck.getStatoOperativo().name().equalsIgnoreCase(StatoOperativoAnagrafica.VALIDO.name()) && 
				   !soggettoCheck.getStatoOperativo().name().equalsIgnoreCase(StatoOperativoAnagrafica.SOSPESO.name())){
				addErroreFin(ErroreFin.SOGGETTO_NON_VALIDO);
				return false;
		}
		List<SedeSecondariaSoggetto> listaSedi = soggettoDad.ricercaSediSecondarie(Integer.valueOf(ente.getUid()), soggettoCheck.getUid(), false, false,datiOperazione);
		if(soggetto.getSediSecondarie()!=null && !soggetto.getSediSecondarie().isEmpty() && listaSedi!=null){
			// In input ho ricevuto un soggetto con al suo interno una sede secondaria alla quale dovra' essere legato l'ordinativo
			for(SedeSecondariaSoggetto sedeSecondariaDb : listaSedi){
				if(sedeSecondariaDb.getUid() == soggetto.getSediSecondarie().get(0).getUid()){
					if(!sedeSecondariaDb.getStatoOperativoSedeSecondaria().name().equalsIgnoreCase(StatoOperativoSedeSecondaria.VALIDO.name())){
						addErroreFin(ErroreFin.SEDE_SECONDARIA_SOGGETTO_NON_VALIDA);
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Effettua i controlli al passo 3 di 2.5.2, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param attoAmministrativo
	 * @param ente
	 * @param richiedente
	 * @return
	 */
	private boolean controlliAttoAmministrativo(AttoAmministrativo attoAmministrativo,Ente ente, Richiedente richiedente){
//		RicercaProvvedimentoResponse ricercaProvvedimentoResponse = ricercaProvvedimento(richiedente, attoAmministrativo);
//		AttoAmministrativo attoAmministrativoEstratto = new AttoAmministrativo();
		
		//MARZO 2017 - Passo da ricercaProvvedimento a estraiAttoAmministrativo che usa 
		//sempre ricercaProvvedimento ma non fa confusione sulla struttura amministrativa:
		AttoAmministrativo attoAmministrativoEstratto = estraiAttoAmministrativo(richiedente, attoAmministrativo);
		if(attoAmministrativoEstratto!=null) {
			if(!attoAmministrativoEstratto.getStatoOperativo().equalsIgnoreCase(StatoOperativoAtti.DEFINITIVO.getDescrizione())){
				addErroreFin(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO,"inserimento ordinativo di incasso", "definitivo");
				return false;
			}
			attoAmministrativo.setUid(attoAmministrativoEstratto.getUid());
		} else {
			addErroreCore(ErroreCore.ENTITA_NON_TROVATA,  "PROVVEDIMENTO", attoAmministrativo.getAnno() + " / " + attoAmministrativo.getNumero());
			return false;
		}
		
		/*
		if(ricercaProvvedimentoResponse.isFallimento()){
			List<Errore> listaErroriAttoAmministrativo = ricercaProvvedimentoResponse.getErrori();			
			res.setErrori(listaErroriAttoAmministrativo);
			res.setOrdinativoIncassoInserito(null);
			res.setEsito(Esito.FALLIMENTO);
			return false;
		} else {
			if(ricercaProvvedimentoResponse.getListaAttiAmministrativi()!=null) {
				
				List<AttoAmministrativo> attiFiltratuPerStatoNonAnnullato = new ArrayList<AttoAmministrativo>();
				
				for (AttoAmministrativo atto : ricercaProvvedimentoResponse.getListaAttiAmministrativi()) {
					
					if(!atto.getStatoOperativo().equals(StatoOperativoAtti.ANNULLATO.getDescrizione())){
						attiFiltratuPerStatoNonAnnullato.add(atto);
					}
					
				}
				
				if(attiFiltratuPerStatoNonAnnullato.size() == 1){
					attoAmministrativoEstratto = attiFiltratuPerStatoNonAnnullato.get(0);
					
					if(!attoAmministrativoEstratto.getStatoOperativo().equalsIgnoreCase(StatoOperativoAtti.DEFINITIVO.getDescrizione())){
						addErroreFin(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO,"inserimento ordinativo di incasso", "definitivo");
						return false;
					}
					attoAmministrativo.setUid(attoAmministrativoEstratto.getUid());
				} else {
					addErroreFin(ErroreFin.OGGETTO_NON_UNIVOCO,"PROVVEDIMENTO");
					return false;
				}
			} else {
				addErroreCore(ErroreCore.ENTITA_NON_TROVATA,"PROVVEDIMENTO", attoAmministrativo.getAnno() + " / " + attoAmministrativo.getNumero());
				return false;
			}
		}*/
		
		return true;
	}
	
	/**
	 * Effettua i controlli al passo 6 di 2.5.2, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param ordinativo
	 * @param ente
	 * @param datiOperazione
	 * @return
	 */
	private boolean controlloDistinta(OrdinativoIncasso ordinativo,Ente ente, DatiOperazioneDto datiOperazione){
		if(ordinativo.getDistinta()!=null && !StringUtils.isEmpty(ordinativo.getDistinta().getCodice())){
			//La distinta e' facoltativa, ma se viene indicata bisogna verificare che esista sul db:
			boolean esisteDistinta = ordinativoIncassoDad.esisteDistinta(ordinativo, datiOperazione);
			if(!esisteDistinta){
				addErroreCore(ErroreCore.ENTITA_NON_TROVATA,"DISTINTA", ordinativo.getDistinta().getCodice());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Effettua i controlli al passo 7 di 2.5.2, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param ordinativo
	 * @param ente
	 * @param datiOperazione
	 * @return
	 */
	private boolean controlloContoTesoreria(OrdinativoIncasso ordinativo,Ente ente, DatiOperazioneDto datiOperazione){
		if(ordinativo.getContoTesoreria()!=null && !StringUtils.isEmpty(ordinativo.getContoTesoreria().getCodice())){
			//Il conto tesoreria e' facoltativo, ma se viene indicata bisogna verificare che esista sul db:
			boolean esisteContoTes = ordinativoIncassoDad.esisteContoTesoreria(ordinativo, datiOperazione);
			if(!esisteContoTes){
				addErroreCore(ErroreCore.ENTITA_NON_TROVATA,"CONTO TESORERIA",  ordinativo.getContoTesoreria().getCodice());
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Effettua i controlli al passo 8 di 2.5.2, ritorna true se va tutto bene, false in caso di controlli non superati
	 * @param ordinativoDiIncasso
	 * @param bilancio
	 * @return
	 */
	private boolean controlloCompetenzaAccertamenti(OrdinativoIncasso ordinativoDiIncasso, Bilancio bilancio){
		int annoBilancio = bilancio.getAnno();
		
		if(ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso()!=null && ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso().size()>0){
			boolean trovatiAnnoBilancio = false;
			boolean trovatoResidui = false;
			for(SubOrdinativoIncasso subIt : ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso()){
				if(subIt!=null && subIt.getAccertamento()!=null){
					
					Accertamento accertIt = subIt.getAccertamento();
										
					int annoMovIt = accertIt.getAnnoMovimento();
					
					if(annoMovIt>annoBilancio){
						addErroreFin(ErroreFin.MOVIMENTO_GESTIONE_PLURIENNALE_NON_AMMESSO,"accertamento", "emissione ordinativo di incasso");
						return false;
					} else if(annoMovIt==annoBilancio){
						trovatiAnnoBilancio = true;
					} else if(annoMovIt<annoBilancio){
						trovatoResidui = true;
					}
				}
			}
			if(trovatoResidui && trovatiAnnoBilancio){
				addErroreFin(ErroreFin.ORDINATIVO_COMPETENZA_O_RESIDUO);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Effettua i controlli al passo 9 di 2.5.2 - richiamo a 2.5.4, ritorna true se va tutto bene, false in caso di controlli non superati
	 * 
	 * le due liste listaDistintiAccertamentiAssociatiAQuote e listaDistintiSubAccertamentiAssociatiAQuote (presenti in datiInserimento)
	 * rappresentano i dati ricevuti dal frontend ARRICCHITI di dati letti sul DB nel metodo precedente "controlloValiditaAccertamentiSubAccertamenti"
	 * 
	 * @param ordinativoDiIncasso
	 * @param bilancio
	 * @param richiedente
	 * @param ente
	 * @param datiInserimento
	 * @param datiOperazione
	 * @return
	 */
	private boolean calcoloDisponibilitaAccertamentoSubAccertamento(OrdinativoIncasso ordinativoDiIncasso, Bilancio bilancio,Richiedente richiedente, Ente ente,
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
						// quoteRicevuteDaFrontEnd sono le quote ricevute dal frontend che incidono tutte sull'accertamento iterato
						// per adesso siamo in inserimento e non considero le varie possibilita' del caso d'uso aggiorna
 						BigDecimal sommaImporti = BigDecimal.ZERO;
						for(SubOrdinativoIncasso quotaIt : quoteRicevuteDaFrontEnd){
							BigDecimal importoQuotaIt = quotaIt.getImportoAttuale();
							sommaImporti = sommaImporti.add(importoQuotaIt);
						}
						
						if(disponibilitaAIncassare.compareTo(sommaImporti)<0){
							boolean importoOK = false;
							ModificaMovimentoGestioneEntrata modGest = CommonUtils.getFirst(accIt.getListaModificheMovimentoGestioneEntrata());
							if(modGest!=null){
								//abbiamo riveuto da front end l'indicazione di inserire una modifica movimento gestione
								BigDecimal importoNew = modGest.getImportoNew();
								if(importoNew.compareTo(sommaImporti.subtract(disponibilitaAIncassare))>=0){
									importoOK = true;
								}
							}
							if(!importoOK){
								addErroreFin(ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNO, accIt.getAnnoMovimento() + "/" + accIt.getNumeroMovimento(), "inserimento ordinativo di incasso");
								return false;
							}
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
							boolean importoOK = false;
							ModificaMovimentoGestioneEntrata modGest = CommonUtils.getFirst(subAccIt.getListaModificheMovimentoGestioneEntrata());
							if(modGest!=null){
								//abbiamo riveuto da front end l'indicazione di inserire una modifica movimento gestione
								BigDecimal importoNew = modGest.getImportoNew();
								if(importoNew.compareTo(sommaImporti.subtract(disponibilitaAIncassare))>=0){
									importoOK = true;
								}
							}
							if(!importoOK){
								addErroreFin(ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNO, subAccIt.getAnnoMovimento() + "/" + subAccIt.getNumeroMovimento(), "inserimento ordinativo di incasso");
								return false;
							}
						}						
					}
				}
			}
			
		}
		
		return true;
	}
	
	/**
	 * 	Effettua i controlli al passo 10 di 2.5.2, ritorna true se va tutto bene, false in caso di controlli non superati
	 * 
	 * le due liste listaDistintiAccertamentiAssociatiAQuote e listaDistintiSubAccertamentiAssociatiAQuote (presenti in datiInserimento)
	 * rappresentano i dati ricevuti dal frontend ARRICCHITI di dati letti sul DB nel metodo precedente "controlloValiditaAccertamentiSubAccertamenti"
	 * @param ordinativoDiIncasso
	 * @param bilancio
	 * @param richiedente
	 * @param ente
	 * @param datiInserimento
	 * @param datiOperazione
	 * @return
	 */
	private boolean controlloCongruenzaDebitoreOrdinativoIncasso(OrdinativoIncasso ordinativoDiIncasso, Bilancio bilancio,Richiedente richiedente, Ente ente,
			OrdinativoInInserimentoInfoDto datiInserimento, DatiOperazioneDto datiOperazione) {
		
		if(datiInserimento!=null && ordinativoDiIncasso!=null){
			
			Soggetto soggettoOrdinativoIncasso = ordinativoDiIncasso.getSoggetto();
			String codSoggOrdIncasso = soggettoOrdinativoIncasso.getCodiceSoggetto();
			
			String soggettoClasseCodeOrdIncasso = accertamentoOttimizzatoDad.estraiSoggettoClasseCode(soggettoOrdinativoIncasso.getUid(), datiOperazione);
			
			//le due liste listaDistintiAccertamentiAssociatiAQuote e listaDistintiSubAccertamentiAssociatiAQuote
			//rappresentano i dati ricevuti dal frontend ARRICCHITI di dati letti sul DB nel metodo precedente "controlloValiditaAccertamentiSubAccertamenti"
			List<MovimentoGestioneLigthDto> listaDistintiAccertamentiAssociatiAQuote = datiInserimento.getListaDistintiAccertamentiAssociatiAQuote();
			List<MovimentoGestioneSubLigthDto> listaDistintiSubAccertamentiAssociatiAQuote = datiInserimento.getListaDistintiSubAccertamentiAssociatiAQuote();
			
			//1.	CONTROLLI SU SOGGETTO
			
			//ACCERTAMENTI NELLE QUOTE:
			if(listaDistintiAccertamentiAssociatiAQuote!=null && listaDistintiAccertamentiAssociatiAQuote.size()>0){
				//dal front ent abbiamo ricevuto quote con accertamenti collegati
				for(MovimentoGestioneLigthDto accIt : listaDistintiAccertamentiAssociatiAQuote){
					//soggettoMovimento e' stato letto dal db nel metodo "controlloValiditaAccertamentiSubAccertamenti"
					Soggetto soggettoMovimento = accIt.getSoggettoMovimento();
					String codSoggMovimento = soggettoMovimento.getCodiceSoggetto();
					/////////////////////////////////////////////////////////////////////////
					if(!StringUtils.isEmpty(codSoggMovimento) && !StringUtils.sonoUguali(codSoggOrdIncasso, codSoggMovimento)){
						addErroreFin(ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_ORDINATIVO, codSoggMovimento, " accertamento ", " il debitore deve essere lo stesso dell'ordinativo ");
						return false;
					}
				}
			}
			
			//SUB-ACCERTAMENTI NELLE QUOTE:
			if(listaDistintiSubAccertamentiAssociatiAQuote!=null && listaDistintiSubAccertamentiAssociatiAQuote.size()>0){
				//dal front ent abbiamo ricevuto quote con sub accertamenti collegati
				for(MovimentoGestioneSubLigthDto subAccIt : listaDistintiSubAccertamentiAssociatiAQuote){
					//soggettoMovimento e' stato letto dal db nel metodo "controlloValiditaAccertamentiSubAccertamenti"
					Soggetto soggettoMovimento = subAccIt.getSoggettoMovimento();
					String codSoggMovimento = soggettoMovimento.getCodiceSoggetto();
					/////////////////////////////////////////////////////////////////////////
					if(!StringUtils.isEmpty(codSoggMovimento) && !StringUtils.sonoUguali(codSoggOrdIncasso, codSoggMovimento)){
						addErroreFin(ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_ORDINATIVO, codSoggMovimento, " sub-accertamento ", " il debitore deve essere lo stesso dell'ordinativo ");
						return false;
					}
				}
			}
			
			//2.	CONTROLLI SU CLASSE SOGGETTO
		}
		return true;
	}
	
	
	/**
	 * richiama il super ed effettua operazioni aggiuntive specifiche per il servizio in questione
	 */
	@Override
	protected void addErroreFin(ErroreFin erroreFin,Object... args){
		super.addErroreFin(erroreFin, args);
		res.setOrdinativoIncassoInserito(null);
	}
	
	/**
	 * richiama il super ed effettua operazioni aggiuntive specifiche per il servizio in questione
	 */
	@Override
	protected void addErroreAtt(ErroreAtt erroreAtt,Object... args){
		super.addErroreAtt(erroreAtt, args);
		res.setOrdinativoIncassoInserito(null);
	}
	
	/**
	 * richiama il super ed effettua operazioni aggiuntive specifiche per il servizio in questione
	 */
	@Override
	protected void addErroreBil(ErroreBil erroreBil,Object... args){
		super.addErroreBil(erroreBil, args);
		res.setOrdinativoIncassoInserito(null);
	}
	
	
}