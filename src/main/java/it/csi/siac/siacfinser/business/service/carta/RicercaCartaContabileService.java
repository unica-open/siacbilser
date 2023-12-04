/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.carta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabileResponse;
import it.csi.siac.siacfinser.integration.dad.CartaContabileDad;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaCartaContabile;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCartaContabileService extends AbstractBaseService<RicercaCartaContabile, RicercaCartaContabileResponse> {

	@Autowired
	CartaContabileDad cartaContabileDad; 
	
	@Autowired
	CommonDad commonDad;
	
	@Autowired
	ProvvedimentoService provvedimentoService;
	
	@Autowired
	DocumentoSpesaService documentoSpesaService;
	
	@Override
	protected void init() {
		final String methodName="RicercaCartaContabileService : init()";
		log.debug(methodName, " - Begin");

	}	
	
//	@Override
//	@Transactional(readOnly=true)
//	public RicercaCartaContabileResponse executeService(RicercaCartaContabile serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName = "RicercaCartaContabileService - execute()";
		log.debug(methodName, " - Begin");
		
		Ente ente = req.getEnte();
		Integer idEnte = ente.getUid();
		Richiedente richiedente = req.getRichiedente();
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, null);
		
		ParametroRicercaCartaContabile parametroRicercaCartaContabile = req.getParametroRicercaCartaContabile();
		
		// Calcolo il numero dei record che verrebbero restituiti in base ai parametri di input ricevuti
		// Se il numero e' maggiore del numero massimo di righe estraibili non procedo con l'estrazione dei dati,
		// e ritorno l'errore di ricerca troppo estesa

		Long conteggioRecords = cartaContabileDad.calcolaNumeroCarteContabiliDaEstrarre(parametroRicercaCartaContabile, datiOperazione);
		
		// verifica sul massimo di righe visualizzabili
		if(conteggioRecords <= CostantiFin.MAX_RIGHE_ESTRAIBILI.longValue()){
			
			// Caching delle entita' soggetto, mdp e atto amm
			HashMap<String, Soggetto> cacheSoggetto = new HashMap<String, Soggetto>();
			HashMap<String, List<ModalitaPagamentoSoggetto>> cacheMdp = new HashMap<String, List<ModalitaPagamentoSoggetto>>();
			HashMap<String, AttoAmministrativo> cacheAttoAmm = new HashMap<String, AttoAmministrativo>();
			
			// Estraggo le carta contabili in base ai parametri di input ricevuti
			List<CartaContabile> listaRisultati = cartaContabileDad.ricercaCarteContabili(parametroRicercaCartaContabile, richiedente, ente, datiOperazione, req.getNumPagina(), req.getNumRisultatiPerPagina(), cacheSoggetto, cacheMdp);
			
			if(listaRisultati!=null && listaRisultati.size() > 0){
				for(CartaContabile cartaContabileEstratta : listaRisultati){
					
					// Estraggo l'atto amministrativo della carta contabile
					// attraverso la chiamata al servizio ProvvedimentoService.ricercaProvvedimento()
					
					Integer annoAttoAmministrativo = cartaContabileEstratta.getAttoAmministrativo().getAnno();
					Integer numeroAttoAmministrativo = cartaContabileEstratta.getAttoAmministrativo().getNumero();
					TipoAtto tipoAttoAmministrativo = cartaContabileEstratta.getAttoAmministrativo().getTipoAtto();
					
					if (annoAttoAmministrativo!=null && numeroAttoAmministrativo!=null && tipoAttoAmministrativo!=null) {
						Integer idStrutturaAmministrativa = leggiIdStrutturaAmministrativa(cartaContabileEstratta.getAttoAmministrativo());
						AttoAmministrativo attoAmministrativoEstratto = estraiAttoAmministrativoCaching(richiedente, annoAttoAmministrativo.toString(), numeroAttoAmministrativo, tipoAttoAmministrativo,idStrutturaAmministrativa, cacheAttoAmm);

						if(attoAmministrativoEstratto!=null){
							cartaContabileEstratta.setAttoAmministrativo(attoAmministrativoEstratto);
						}	
					}
					
					if(cartaContabileEstratta.getListaPreDocumentiCarta()!=null && cartaContabileEstratta.getListaPreDocumentiCarta().size()>0){
						for(PreDocumentoCarta preDocumentoCarta : cartaContabileEstratta.getListaPreDocumentiCarta()){
							
							// Jira-1461 
							// per ora questa parte e' commentata, in attesa di istruzioni per capire
							// se e' veramente necessario per il servizio tirare fuori tutti questi dati
							// oppure se bisogna tirar fuori solamente i dati base delle sottoentita
							// Soggetto, Impegno e Sub-Impegno
							
							/*
							 * 
							if(preDocumentoCarta.getImpegno()!=null){
							
								// Estraggo il capitolo uscita gestione dell'impegno legato al pre-documento
								// attraverso la chiamata al servizio CapitoloUscitaGestione.ricercaDettaglioCapitoloUscitaGestione()
 
								CapitoloUscitaGestione capitoloUscitaGestione = estraiCapitoloUscitaGestioneCaching(richiedente, preDocumentoCarta.getImpegno(),cacheCapitolo);
								if(capitoloUscitaGestione!=null)
									preDocumentoCarta.getImpegno().setCapitoloUscitaGestione(capitoloUscitaGestione);
								
								// Estraggo l'atto amministrativo dell'impegno legato al pre-documento
							 	// attraverso la chiamata al servizio ProvvedimentoService.ricercaProvvedimento()
 
								if(preDocumentoCarta.getImpegno().getAttoAmmAnno()!=null && preDocumentoCarta.getImpegno().getAttoAmmNumero()!=null && preDocumentoCarta.getImpegno().getAttoAmmTipoAtto()!=null){
									AttoAmministrativo attoAmministrativoImpegno = estraiAttoAmministrativoCaching(richiedente, preDocumentoCarta.getImpegno().getAttoAmmAnno(), preDocumentoCarta.getImpegno().getAttoAmmNumero(), preDocumentoCarta.getImpegno().getAttoAmmTipoAtto(), cacheAttoAmm);
									if(attoAmministrativoImpegno!=null)
										preDocumentoCarta.getImpegno().setAttoAmministrativo(attoAmministrativoImpegno);
								}
								
								// Estraggo l'atto amministrativo delle eventuali modifiche dell'impegno legato al pre-documento
								// attraverso la chiamata al servizio ProvvedimentoService.ricercaProvvedimento()
  
								List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesaImp = preDocumentoCarta.getImpegno().getListaModificheMovimentoGestioneSpesa();
								if(elencoModificheMovimentoGestioneSpesaImp!=null && elencoModificheMovimentoGestioneSpesaImp.size() > 0){
									for(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesaImp : elencoModificheMovimentoGestioneSpesaImp){
										if(modificaMovimentoGestioneSpesaImp.getAttoAmmAnno()!=null && modificaMovimentoGestioneSpesaImp.getAttoAmmNumero()!=null && modificaMovimentoGestioneSpesaImp.getAttoAmmTipoAtto()!=null){
											AttoAmministrativo attoAmministrativoModificaImp = estraiAttoAmministrativoCaching(richiedente, modificaMovimentoGestioneSpesaImp.getAttoAmmAnno(), modificaMovimentoGestioneSpesaImp.getAttoAmmNumero(), modificaMovimentoGestioneSpesaImp.getAttoAmmTipoAtto(), cacheAttoAmm);
											if(attoAmministrativoModificaImp!=null)
												modificaMovimentoGestioneSpesaImp.setAttoAmministrativo(attoAmministrativoModificaImp);
										}
									}
								}

								// Estraggo l'atto amministrativo degli eventuali sub-impegni
								// attraverso la chiamata al servizio ProvvedimentoService.ricercaProvvedimento()

								List<SubImpegno> elencoSubImpegni = preDocumentoCarta.getImpegno().getElencoSubImpegni();
								if(elencoSubImpegni!=null && elencoSubImpegni.size() > 0){
									for(SubImpegno subImpegno : elencoSubImpegni){
										if(subImpegno.getAttoAmmAnno()!=null && subImpegno.getAttoAmmNumero()!=null && subImpegno.getAttoAmmTipoAtto()!=null){
											AttoAmministrativo attoAmministrativoSub = estraiAttoAmministrativoCaching(richiedente, subImpegno.getAttoAmmAnno(), subImpegno.getAttoAmmNumero(), subImpegno.getAttoAmmTipoAtto(), cacheAttoAmm);
											if(attoAmministrativoSub!=null)
												subImpegno.setAttoAmministrativo(attoAmministrativoSub);
										}

										// Estraggo l'atto amministrativo delle eventuali modifiche del sub-impegno
										// attraverso la chiamata al servizio ProvvedimentoService.ricercaProvvedimento()

										List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesaSubImp = subImpegno.getListaModificheMovimentoGestioneSpesa();
										if(elencoModificheMovimentoGestioneSpesaSubImp!=null && elencoModificheMovimentoGestioneSpesaSubImp.size() > 0){
											for(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesaSubImp : elencoModificheMovimentoGestioneSpesaSubImp){
												if(modificaMovimentoGestioneSpesaSubImp.getAttoAmmAnno()!=null && modificaMovimentoGestioneSpesaSubImp.getAttoAmmNumero()!=null && modificaMovimentoGestioneSpesaSubImp.getAttoAmmTipoAtto()!=null){
													AttoAmministrativo attoAmministrativoModificaSubImp = estraiAttoAmministrativoCaching(richiedente, modificaMovimentoGestioneSpesaSubImp.getAttoAmmAnno(), modificaMovimentoGestioneSpesaSubImp.getAttoAmmNumero(), modificaMovimentoGestioneSpesaSubImp.getAttoAmmTipoAtto(), cacheAttoAmm);
													if(attoAmministrativoModificaSubImp!=null)
														modificaMovimentoGestioneSpesaSubImp.setAttoAmministrativo(attoAmministrativoModificaSubImp);
												}
											}
										}
									}
								}
							}
							*
							*/
							
							// Estrazione degli eventuali sub-documenti legati al PreDocumentoCarta : inizio
							// attraverso la chiamata al servizio DocumentoSpesaService.ricercaDettaglioQuotaSpesa()

							if(preDocumentoCarta.getListaIdSubDocumentiCollegati()!=null && preDocumentoCarta.getListaIdSubDocumentiCollegati().size()>0){
								List<SubdocumentoSpesa> listaSubDocumentiSpesaCollegati = new ArrayList<SubdocumentoSpesa>();
							
								/* SIAC-5261 ottobre 2017 commento anche questo pezzo perche' vedo essere inutile in quanto 
								 * listaSubDocumentiSpesaCollegati poi non viene usato in quanto il codice dopo e' stato commentato
								for(Integer idSubDocumentoSpesa : preDocumentoCarta.getListaIdSubDocumentiCollegati()){
									RicercaDettaglioQuotaSpesa ricercaDettaglioQuotaSpesa = new RicercaDettaglioQuotaSpesa();
									ricercaDettaglioQuotaSpesa.setRichiedente(richiedente);
									
									SubdocumentoSpesa subdocumentoSpesaInput = new SubdocumentoSpesa();
									subdocumentoSpesaInput.setUid(idSubDocumentoSpesa);
									ricercaDettaglioQuotaSpesa.setSubdocumentoSpesa(subdocumentoSpesaInput);
									
									RicercaDettaglioQuotaSpesaResponse ricercaDettaglioQuotaSpesaResponse = documentoSpesaService.ricercaDettaglioQuotaSpesa(ricercaDettaglioQuotaSpesa);
									if(ricercaDettaglioQuotaSpesaResponse.isFallimento()){
										res.setErrori(ricercaDettaglioQuotaSpesaResponse.getErrori());
										res.setEsito(Esito.FALLIMENTO);
										res.setElencoCarteContabili(null);
										return;
									}
									
									listaSubDocumentiSpesaCollegati.add(ricercaDettaglioQuotaSpesaResponse.getSubdocumentoSpesa());
								} */
								
								// Jira-1461 
								// per ora questa parte e' commentata, in attesa di istruzioni per capire
								// se e' veramente necessario per il servizio tirare fuori tutti questi dati
								// oppure se bisogna tirar fuori solamente i dati base delle sotto entita'
								// Soggetto, Impegno e Sub-Impegno
								
								/*
								 * 
								if(listaSubDocumentiSpesaCollegati!=null && listaSubDocumentiSpesaCollegati.size()>0){
									for(SubdocumentoSpesa subdocumentoSpesaIterato : listaSubDocumentiSpesaCollegati){
									
										// Estraggo i dati dell'impegno del SubDocumentoSpesa
										if(subdocumentoSpesaIterato.getImpegno()!=null){
											Impegno imp = (Impegno)impegnoDad.ricercaMovimentoPkByUid(subdocumentoSpesaIterato.getImpegno().getUid(),
													                                                  richiedente,
													                                                  ente,
													                                                  CostantiFin.MOVGEST_TIPO_IMPEGNO,
													                                                  false);
											
											if(imp!=null){
												// Estraggo il capitolo uscita gestione dell'impegno legato al SubDocumentoSpesa
												// attraverso la chiamata al servizio CapitoloUscitaGestione.ricercaDettaglioCapitoloUscitaGestione()

												CapitoloUscitaGestione capitoloUscitaGestioneCiclo = estraiCapitoloUscitaGestioneCaching(richiedente, imp, cacheCapitolo);
												if(capitoloUscitaGestioneCiclo!=null)
													imp.setCapitoloUscitaGestione(capitoloUscitaGestioneCiclo);
												
												subdocumentoSpesaIterato.setImpegno(imp);			
											}
										}
									}
									preDocumentoCarta.setListaSubDocumentiSpesaCollegati(listaSubDocumentiSpesaCollegati);										
								}
								
								*
								*/
							}
							// estrazione degli eventuali sub-documenti legati al PreDocumentoCarta : fine								 
						}
					}
				}
			}
			// uscita con OK
			res.setErrori(null);
			res.setEsito(Esito.SUCCESSO);
			res.setElencoCarteContabili(listaRisultati);
			res.setNumRisultati(conteggioRecords.intValue());
			res.setNumPagina(req.getNumPagina());
		} else {
			// uscita con KO
			res.setErrori(Arrays.asList(ErroreFin.RICERCA_TROPPO_ESTESA.getErrore()));
			res.setEsito(Esito.FALLIMENTO);
			res.setElencoCarteContabili(null);
		}
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {

		final String methodName="RicercaCartaContabileService : checkServiceParam()";
		log.debug(methodName, " - Begin");

		Ente ente = req.getEnte();
		ParametroRicercaCartaContabile parametroRicercaCartaContabile = req.getParametroRicercaCartaContabile();

		String elencoParametriNonInizializzati = "";
		String elencoParametriErrati = "";

		/*
		 * 
		 * Verifica parametri di input
		 * 	- Il servizio controlla che tutti i parametri di input siano stati inizializzati, in caso contrario viene segnalato
		 * 	  l'errore COR_ERR_0003 con l'indicazione dei parametri mancanti.
		 * 
		 * Se valorizzati i parametri seguenti applicare i controlli descritti: 
		 * 	- Data Scadenza da e Data Scadenza a: devono essere compresa nell'anno di bilancio, Se valorizzate  entrambe  Data Scadenza da  <= Data Scadenza a
		 *  - Numero da e Numero a: devono essere compresa nell'anno di bilancio, Se valorizzate  entrambi  Numero da  <= Numero a
		 * 	- Oggetto se valorizzato devono contenere almeno 3 caratteri
		 * 	- Capitolo: numero e articolo devono essere tutti omessi o tutti presenti
		 * 	- Impegno e Provvedimento: se presente l'anno deve essere anche digitato il numero e viceversa
		 * 
		 */
		
		// verifico obbligatorieta' ente
		if(ente==null){
			if(elencoParametriNonInizializzati.length() > 0){
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + ", ENTE";
			}else{
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + "ENTE";
			}	
		}
		
		// verifico obbligatorieta' parametroRicercaCartaContabile
		if(parametroRicercaCartaContabile==null){
			if(elencoParametriNonInizializzati.length() > 0){
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + ", PARAMETRO_RICERCA_CARTA_CONTABILE";
			}else{
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + "PARAMETRO_RICERCA_CARTA_CONTABILE";
			}	
		} else {
			// verifico obbligatorieta' annoEsercizio
			Integer annoEsercizio = parametroRicercaCartaContabile.getAnnoEsercizio();
			if(annoEsercizio==null){
				if(elencoParametriNonInizializzati.length() > 0){
					elencoParametriNonInizializzati = elencoParametriNonInizializzati + ", ANNO_ESERCIZIO";
				}else{
					elencoParametriNonInizializzati = elencoParametriNonInizializzati + "ANNO_ESERCIZIO";
				}	
			} else {
				// qui tutti gli altri controlli sui parametri ricevuti in input
				
				// numero carta contabile
				Integer numeroCartaContabileDa = parametroRicercaCartaContabile.getNumeroCartaContabileDa();
				Integer numeroCartaContabileA = parametroRicercaCartaContabile.getNumeroCartaContabileA();
				if(numeroCartaContabileDa!=null && numeroCartaContabileA!=null && numeroCartaContabileDa.compareTo(numeroCartaContabileA) > 0){
					if(elencoParametriErrati.length() > 0){
						elencoParametriErrati = elencoParametriErrati + ", NUMERO_CARTA_CONTABILE_DA MAGGIORE DI NUMERO_CARTA_CONTABILE_A";
					}else{
						elencoParametriErrati = elencoParametriErrati + "NUMERO_CARTA_CONTABILE_DA MAGGIORE DI NUMERO_CARTA_CONTABILE_A";
					}	
				}
				
				// oggetto
				String oggetto = parametroRicercaCartaContabile.getOggetto();
				if(oggetto!=null){
					if(oggetto.trim().length() < 3){
						if(elencoParametriErrati.length() > 0){
							elencoParametriErrati = elencoParametriErrati + ", OGGETTO DEVE ESSERE LUNGO ALMENO 3 CARATTERI";
						}else{
							elencoParametriErrati = elencoParametriErrati + "OGGETTO DEVE ESSERE LUNGO ALMENO 3 CARATTERI";
						}	
					}
				}
				
				// capitolo
				BigDecimal numeroCapitolo = parametroRicercaCartaContabile.getNumeroCapitolo();
				BigDecimal numeroArticolo = parametroRicercaCartaContabile.getNumeroArticolo();
				if((numeroCapitolo==null && numeroArticolo!=null) || (numeroCapitolo!=null && numeroArticolo==null)){
					if(elencoParametriErrati.length() > 0){
						elencoParametriErrati = elencoParametriErrati + ", NUMERO_CAPITOLO E NUMERO_ARTICOLO DEVONO ESSERE INDICATI ENTRAMBI";
					}else{
						elencoParametriErrati = elencoParametriErrati + "NUMERO_CAPITOLO E NUMERO_ARTICOLO DEVONO ESSERE INDICATI ENTRAMBI";
					}	
				}
				
				// impegno
				BigDecimal numeroImpegno = parametroRicercaCartaContabile.getNumeroImpegno();
				Integer annoImpegno = parametroRicercaCartaContabile.getAnnoImpegno();
				if((numeroImpegno==null && annoImpegno!=null) || (numeroImpegno!=null && annoImpegno==null)){
					if(elencoParametriErrati.length() > 0){
						elencoParametriErrati = elencoParametriErrati + ", ANNO_IMPEGNO E NUMERO_IMPEGNO DEVONO ESSERE INDICATI ENTRAMBI";
					}else{
						elencoParametriErrati = elencoParametriErrati + "ANNO_IMPEGNO E NUMERO_IMPEGNO DEVONO ESSERE INDICATI ENTRAMBI";
					}	
				}

				// provvedimento
				Integer annoProvvedimento = parametroRicercaCartaContabile.getAnnoProvvedimento();
				Integer numeroProvvedimento = parametroRicercaCartaContabile.getNumeroProvvedimento();
				String tipoProvvedimento = parametroRicercaCartaContabile.getTipoProvvedimento();
				
				if((annoProvvedimento!=null || numeroProvvedimento!=null || !StringUtilsFin.isEmpty(tipoProvvedimento))){
					if(annoProvvedimento!=null){						
						if(numeroProvvedimento==null && StringUtilsFin.isEmpty(tipoProvvedimento)){
							if(elencoParametriErrati.length() > 0){
								elencoParametriErrati = elencoParametriErrati + ", NUMERO_PROVVEDIMENTO o TIPO_PROVVEDIMENTO OBBLIGATORI CON ANNO_PROVVEDIMENTO";
							}else{
								elencoParametriErrati = elencoParametriErrati + "NUMERO_PROVVEDIMENTO o TIPO_PROVVEDIMENTO OBBLIGATORI CON ANNO_PROVVEDIMENTO";
							}	
						}						
					}
					
					if(numeroProvvedimento!=null || !StringUtilsFin.isEmpty(tipoProvvedimento)){
						if(annoProvvedimento==null){
							if(elencoParametriErrati.length() > 0){
								elencoParametriErrati = elencoParametriErrati + ", ANNO_PROVVEDIMENTO OBBLIGATORIO CON NUMERO_PROVVEDIMENTO o TIPO_PROVVEDIMENTO";
							}else{
								elencoParametriErrati = elencoParametriErrati + "ANNO_PROVVEDIMENTO OBBLIGATORIO CON NUMERO_PROVVEDIMENTO o TIPO_PROVVEDIMENTO";
							}	
						}
					}
				}

				// date scadenza
				Date dataScadenzaDa = parametroRicercaCartaContabile.getDataScadenzaDa();
				Date dataScadenzaA = parametroRicercaCartaContabile.getDataScadenzaA();

				// controlli sulle date
				if(dataScadenzaDa!=null){
					Calendar calendarDa = new GregorianCalendar();
					calendarDa.setTime(dataScadenzaDa);
					int year = calendarDa.get(Calendar.YEAR);
				    
				    if(year!=parametroRicercaCartaContabile.getAnnoEsercizio()){
						if(elencoParametriErrati.length() > 0){
							elencoParametriErrati = elencoParametriErrati + ", DATA_SCADENZA_DA DEVE ESSERE COMPRESA NELL'ANNO DI BILANCIO RICEVUTO";
						}else{
							elencoParametriErrati = elencoParametriErrati + "DATA_SCADENZA_DA DEVE ESSERE COMPRESA NELL'ANNO DI BILANCIO RICEVUTO";
						}	
				    }
				}

				if(dataScadenzaA!=null){
					Calendar calendarA = new GregorianCalendar();
					calendarA.setTime(dataScadenzaDa);
					int year = calendarA.get(Calendar.YEAR);
				    
				    if(year!=parametroRicercaCartaContabile.getAnnoEsercizio()){
						if(elencoParametriErrati.length() > 0){
							elencoParametriErrati = elencoParametriErrati + ", DATA_SCADENZA_A DEVE ESSERE COMPRESA NELL'ANNO DI BILANCIO RICEVUTO";
						}else{
							elencoParametriErrati = elencoParametriErrati + "DATA_SCADENZA_A DEVE ESSERE COMPRESA NELL'ANNO DI BILANCIO RICEVUTO";
						}	
				    }
				}
				
				if(dataScadenzaDa!=null && dataScadenzaA!=null){
					if(dataScadenzaDa.compareTo(dataScadenzaA) > 0){
						if(elencoParametriErrati.length() > 0){
							elencoParametriErrati = elencoParametriErrati + ", DATA_SCADENZA_DA DEVE ESSERE PRECEDENTE DI DATA_SCADENZA_A";
						}else{
							elencoParametriErrati = elencoParametriErrati + "DATA_SCADENZA_DA DEVE ESSERE PRECEDENTE DI DATA_SCADENZA_A";
						}	
					}
				}				
			}
		}
		
		if(!StringUtilsFin.isEmpty(elencoParametriNonInizializzati)){
			res.setErrori(Arrays.asList(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParametriNonInizializzati)));
			res.setEsito(Esito.FALLIMENTO);
			res.setElencoCarteContabili(null);
		} else if(!StringUtilsFin.isEmpty(elencoParametriErrati)){
			res.setErrori(Arrays.asList(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParametriErrati)));
			res.setEsito(Esito.FALLIMENTO);
			res.setElencoCarteContabili(null);
		}
	}
	
/*
	protected CapitoloUscitaGestione estraiCapitoloUscitaGestioneCaching(Richiedente richiedente, Impegno impegno, HashMap<Integer, CapitoloUscitaGestione> cacheCapitoli){
		
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		
		
		capitoloUscitaGestione = cacheCapitoli.get(impegno.getChiaveCapitoloUscitaGestione());
		
		if(null==capitoloUscitaGestione){
			RicercaDettaglioCapitoloUGest ricercaDettaglioCapitoloUGest = new RicercaDettaglioCapitoloUGest();
			ricercaDettaglioCapitoloUGest.setChiaveCapitolo(impegno.getChiaveCapitoloUscitaGestione());
		
			RicercaDettaglioCapitoloUscitaGestione ricercaDettaglioCapitoloUscitaGestione = new RicercaDettaglioCapitoloUscitaGestione();
			ricercaDettaglioCapitoloUscitaGestione.setRicercaDettaglioCapitoloUGest(ricercaDettaglioCapitoloUGest);
			ricercaDettaglioCapitoloUscitaGestione.setRichiedente(richiedente);
			ricercaDettaglioCapitoloUscitaGestione.setEnte(richiedente.getAccount().getEnte());
							
			RicercaDettaglioCapitoloUscitaGestioneResponse ricercaDettaglioCapitoloUscitaGestioneResponse = capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(ricercaDettaglioCapitoloUscitaGestione);
			
			capitoloUscitaGestione = ricercaDettaglioCapitoloUscitaGestioneResponse.getCapitoloUscita();
			
			// cache
			cacheCapitoli.put(impegno.getChiaveCapitoloUscitaGestione(), capitoloUscitaGestione);
		}

		return capitoloUscitaGestione;
	}
	*/
}