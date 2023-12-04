/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.business.service.AbstractBaseServiceRicercaOrdinativo;
import it.csi.siac.siacfinser.integration.dao.common.dto.AccertamentoPerDoppiaGestioneInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.CapitoliInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.InsAggOrdinativoIncassoDGInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoGestioneLigthAbstractDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoGestioneLigthDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoGestioneSubLigthDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInInserimentoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubOrdinativoImportoVariatoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubOrdinativoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;

public abstract class AbstractInserisceAggiornaAnnullaOrdinativoIncassoService <REQ extends ServiceRequest, RES extends ServiceResponse> 
				extends AbstractBaseServiceRicercaOrdinativo<REQ, RES> {
	
	    
	
	
	protected HashMap<Integer, MovimentoGestioneSubLigthDto> addToMap( 
			HashMap<Integer, MovimentoGestioneSubLigthDto> mappaSubAccertamentiRicevuti,MovimentoGestioneSubLigthDto subInfo, SubOrdinativoIncasso subIt){
		Integer key = subInfo.getMovgestTsId();
		MovimentoGestioneSubLigthDto value = mappaSubAccertamentiRicevuti.get(key);
		if(value==null){
			value = subInfo;
		}
		value.addQuotaRicevutaDaFrontEnd(subIt);
		mappaSubAccertamentiRicevuti.put(key, value);
		return mappaSubAccertamentiRicevuti;
	}
	
	protected HashMap<Integer, MovimentoGestioneLigthDto> addToMap(
			HashMap<Integer, MovimentoGestioneLigthDto> mappaAccertamentiRicevuti,MovimentoGestioneLigthDto info, SubOrdinativoIncasso subIt){
		Integer key = info.getMovgestTsId();
		MovimentoGestioneLigthDto value = mappaAccertamentiRicevuti.get(key);
		if(value==null){
			value = info;
		}
		value.addQuotaRicevutaDaFrontEnd(subIt);
		mappaAccertamentiRicevuti.put(key, value);
		return mappaAccertamentiRicevuti;
	}
	
	protected boolean controlloValiditaAccertamentiSubAccertamenti(OrdinativoIncasso ordinativoDiIncasso, Bilancio bilancio,
			OrdinativoInInserimentoInfoDto datiInserimento,DatiOperazioneDto datiOperazione,SubOrdinativoInModificaInfoDto subOrdinativoInModificaInfoDto){
		String annoEsercizio =Integer.toString(bilancio.getAnno());
		String tipoMovimento = CostantiFin.MOVGEST_TIPO_ACCERTAMENTO;
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getUid();
		if(ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso()!=null && ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso().size()>0){
			HashMap<Integer, MovimentoGestioneLigthDto> mappaAccertamentiRicevuti = new HashMap<Integer, MovimentoGestioneLigthDto>();
			HashMap<Integer, MovimentoGestioneSubLigthDto> mappaSubAccertamentiRicevuti = new HashMap<Integer, MovimentoGestioneSubLigthDto>();
			
			for(SubOrdinativoIncasso subIt : ordinativoDiIncasso.getElencoSubOrdinativiDiIncasso()){
				if(subIt!=null && subIt.getAccertamento()!=null){
					
					//DA ANALISI: per ciascun subordinativo da aggiornare per cui E' STATO MODIFICATO IN AUMENTO L'IMPORTO..
					boolean controlloStatoDefinitivo = false;
					if(subOrdinativoInModificaInfoDto==null){
						//siamo dal servizio di inserimento, il controllo e' obbligato
						controlloStatoDefinitivo = true;
					} else if(CommonUtil.contenutoInLista(subOrdinativoInModificaInfoDto.getSubOrdinativiDaModificarePerImportoAumentato(), subIt.getUid())){
						//in caso di importo aumentato il controllo ci vuole
						controlloStatoDefinitivo = true;
					} else if(CommonUtil.contenutoInLista(subOrdinativoInModificaInfoDto.getSubOrdinativiDaInserire(), subIt.getUid())){
						//in caso di nuovo subordinativo (da caso d'uso aggiorna) e' comunque il caso di fare il controllo
						controlloStatoDefinitivo = true;
					}
					
					//dati dell'accertamento:
					Accertamento accertIt = subIt.getAccertamento();
					BigDecimal numeroMovimento = accertIt.getNumeroBigDecimal();
					Integer annoMovimento = accertIt.getAnnoMovimento();
					List<ModificaMovimentoGestioneEntrata>  listaModificheMovimentoGestioneEntrata = accertIt.getListaModificheMovimentoGestioneEntrata();
					//dati del sub accertamento:
					BigDecimal numeroSub = null;
					SubAccertamento subAccertIt = null;
					if(accertIt instanceof SubAccertamento){
						subAccertIt = (SubAccertamento) accertIt;
						numeroSub = subAccertIt.getNumeroBigDecimal();
						numeroMovimento = subAccertIt.getNumeroAccertamentoPadre();
						annoMovimento=subAccertIt.getAnnoAccertamentoPadre();
					}
					MovimentoGestioneLigthDto movInfo = 
							accertamentoOttimizzatoDad.caricaMovimentoLigth(idEnte, annoEsercizio, annoMovimento, numeroMovimento, tipoMovimento, datiOperazione);
					
					if(subAccertIt!=null){
						//abbiamo ricevuto un sub accertamento
						MovimentoGestioneSubLigthDto subInfo = movInfo.getSubByNumero(numeroSub);
						if(subInfo==null){
							//sub accertamento inesistente lanciare errore
							addErroreFin(ErroreFin.ACCERTAMENTO_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE, "Accertamento / sub-accertamento", "definitivo", "non puo' essere incassato");
							return false;
						} else {
							//esiste il sub accertamento assicuriamoci sia definitivo
							if(controlloStatoDefinitivo && !CostantiFin.MOVGEST_STATO_DEFINITIVO.equals(subInfo.getStatoCode())){
								addErroreFin(ErroreFin.ACCERTAMENTO_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE, "Accertamento / sub-accertamento", "definitivo", "non puo' essere incassato");
								return false;
							}
							subInfo.setListaModificheMovimentoGestioneEntrata(listaModificheMovimentoGestioneEntrata);
							mappaSubAccertamentiRicevuti = addToMap(mappaSubAccertamentiRicevuti, subInfo, subIt);
						}
					} else {
						//abbiamo ricevuto un accertamento
						if(movInfo==null){
							//accertamento inesistente lanciare errore
							addErroreFin(ErroreFin.ACCERTAMENTO_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE, "Accertamento / sub-accertamento", "definitivo", "non puo' essere incassato");
							return false;
						} else {
							//esiste assicuriamoci sia definitivo
							if(controlloStatoDefinitivo && !CostantiFin.MOVGEST_STATO_DEFINITIVO.equals(movInfo.getStatoCode())){
								//accertamento in stato diverso da definitivo lanciare errore
								addErroreFin(ErroreFin.ACCERTAMENTO_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE, "Accertamento / sub-accertamento", "definitivo", "non puo' essere incassato");
								return false;
							}
							movInfo.setListaModificheMovimentoGestioneEntrata(listaModificheMovimentoGestioneEntrata);
							mappaAccertamentiRicevuti = addToMap(mappaAccertamentiRicevuti, movInfo, subIt);
						}
						
					}
				}
			}
			
			//riepilogo dei DISTINTI acc e dei DISTINTI sub acc ricevuti con le QUOTE
			ArrayList<MovimentoGestioneLigthDto> listaDistintiAccertamentiRicevuti = StringUtilsFin.hashMapToArrayList(mappaAccertamentiRicevuti);
			ArrayList<MovimentoGestioneSubLigthDto> listaDistintiSubAccertamentiRicevuti = StringUtilsFin.hashMapToArrayList(mappaSubAccertamentiRicevuti);
			datiInserimento.setListaDistintiAccertamentiAssociatiAQuote(listaDistintiAccertamentiRicevuti);
			datiInserimento.setListaDistintiSubAccertamentiAssociatiAQuote(listaDistintiSubAccertamentiRicevuti);
			////////////////////////////////////////////////////////////////////////////
		}
		return true;
	}
	
	private <M extends MovimentoGestioneLigthAbstractDto> List<M> soloConModificheDaCreare(List<M> listaDistintiAccertamentiAssociatiAQuoteInput){
		List<M> listaDistintiAccertamentiAssociatiAQuote = new ArrayList<M>();
		if(listaDistintiAccertamentiAssociatiAQuoteInput!=null && listaDistintiAccertamentiAssociatiAQuoteInput.size()>0){
			for(M accIt : listaDistintiAccertamentiAssociatiAQuoteInput){
				if(accIt!=null){
					List<ModificaMovimentoGestioneEntrata> soloModificheDaCreare = CommonUtil.soloConIdZero(accIt.getListaModificheMovimentoGestioneEntrata());
					if(soloModificheDaCreare!=null && soloModificheDaCreare.size()>0){
						M cloned = clone(accIt);
						cloned.setListaModificheMovimentoGestioneEntrata(soloModificheDaCreare);
						listaDistintiAccertamentiAssociatiAQuote.add(cloned);
					}
				}
			}
		}
		return listaDistintiAccertamentiAssociatiAQuote;
	}
	
	/**
	 * Modifiche di accertamento E sub routine.
	 *
	 * @param <M> the generic type
	 * @param listaDistintiAccertamentiAssociatiAQuoteInput the lista distinti accertamenti associati A quote input
	 * @param isSub the is sub
	 * @param attoAmministrativo the atto amministrativo
	 * @param datiOperazione the dati operazione
	 * @return  la lista degli id degli accertamenti acccertamenti (siacTMovgest) su cui e' stata inserita la modifica
	 */
	protected <M extends MovimentoGestioneLigthAbstractDto> List<Integer> modificheDiAccertamentoESub_Routine(List<M> listaDistintiAccertamentiAssociatiAQuoteInput, boolean isSub, AttoAmministrativo attoAmministrativo,DatiOperazioneDto datiOperazione){
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
		
		//FIX PER  	SIAC-4514 dobbiamo creare le modifiche per le sole quote che il front end ci ha chiesto:
		List<M> listaDistintiAccertamentiAssociatiAQuote = soloConModificheDaCreare(listaDistintiAccertamentiAssociatiAQuoteInput);
		
		if(listaDistintiAccertamentiAssociatiAQuote==null || listaDistintiAccertamentiAssociatiAQuote.isEmpty()){
			return new ArrayList<Integer>();
		}
		//
		List<Integer> idAccertamentiGiaCorretti = new ArrayList<Integer>();
		List<Integer> idTestateAccertamentiConModificheContestuali = new ArrayList<Integer>();
		
		
		for(M accIt : listaDistintiAccertamentiAssociatiAQuote){
			
			BigDecimal importoAttuale = accIt.getImportoAttuale();
			
			int numeroModifiche = accertamentoOttimizzatoDad.countModificheTotali(accIt.getMovgestTsId(), datiOperazione);
			
			//Nel caso di sub accertamento verifichiamo se dobbiamo correggere l'importo dell'accertamento di cui fa parte:
			if(isSub){
				
				MovimentoGestioneSubLigthDto subLigth = ((MovimentoGestioneSubLigthDto)accIt);
				
				Integer uIdAccertamentoTs = subLigth.getMovgestTsIdPadre();
				Integer uIdAccertamento = subLigth.getMovgestIdPadre();
				
				if(!idAccertamentiGiaCorretti.contains(uIdAccertamentoTs)){
					
					idAccertamentiGiaCorretti.add(uIdAccertamentoTs);
					
					List<MovimentoGestioneSubLigthDto> subDelloStessoAcc =
							MovimentoGestioneSubLigthDto.getSubDelloStessoAccertamento((List<MovimentoGestioneSubLigthDto>) listaDistintiAccertamentiAssociatiAQuote, subLigth);
					
					if(subDelloStessoAcc!=null && subDelloStessoAcc.size()>0){
						
						BigDecimal totModifiche = BigDecimal.ZERO;
						
						for(MovimentoGestioneSubLigthDto subIt : subDelloStessoAcc){
							if(subIt!=null){
								ModificaMovimentoGestioneEntrata modGest = CommonUtil.getFirst(subIt.getListaModificheMovimentoGestioneEntrata());
								if(modGest!=null){
									BigDecimal modificaSubIt = modGest.getImportoNew();
									totModifiche = totModifiche.add(modificaSubIt);
								}
							}
						}
						
						if(totModifiche.compareTo(BigDecimal.ZERO)>0){
							BigDecimal disponibilitaASubAccertare = accertamentoOttimizzatoDad.calcolaDisponibilitaASubAccertare(uIdAccertamento.intValue(), datiOperazione);
							
							if(totModifiche.compareTo(disponibilitaASubAccertare)>0){
								
								BigDecimal importoAttualeAccertamento = accertamentoOttimizzatoDad.estraiImportoAttualeByMovgestId(uIdAccertamento.intValue(), datiOperazione);
								BigDecimal importoOld = totModifiche.subtract(disponibilitaASubAccertare);
								BigDecimal importoNew = importoOld.add(importoAttualeAccertamento);
								
								numeroModifiche = numeroModifiche + 1;
								//inserisco la modifica sul db
								creaModifica(false, uIdAccertamento.intValue(), attoAmministrativo, importoOld, importoNew, datiOperazione,numeroModifiche);
								idTestateAccertamentiConModificheContestuali.add(uIdAccertamento);
							}
						}
						
					}
				}
			}
			
			ModificaMovimentoGestioneEntrata modGest = CommonUtil.getFirst(accIt.getListaModificheMovimentoGestioneEntrata());
			
			if(modGest!=null && !modGest.isRiepilogoAutomatiche()){
				//abbiamo riveuto da front end l'indicazione di inserire una modifica movimento gestione
				ModificaMovimentoGestioneEntrata movimento = new ModificaMovimentoGestioneEntrata();				
				movimento.setAttoAmministrativo(attoAmministrativo);				
				//il metodo aggiornaModificheMovimentoGestioneEntrata non legge direttamente l'atto amministrativo ma legge i tre campi distinti
				movimento.setAttoAmministrativoAnno(String.valueOf(attoAmministrativo.getAnno()));
				movimento.setAttoAmministrativoNumero(attoAmministrativo.getNumero());
				movimento.setAttoAmministrativoTipoCode(attoAmministrativo.getTipoAtto().getCodice());

				//il metodo aggiornaModificheMovimentoGestioneEntrata sembra far distinzione tra sub e non sub per l'id:
				Integer uIdAccertamento = null;
				if(isSub){
					uIdAccertamento = ((MovimentoGestioneSubLigthDto)accIt).getMovgestTsId();
				} else {
					uIdAccertamento = ((MovimentoGestioneLigthDto)accIt).getMovgestId();
					idTestateAccertamentiConModificheContestuali.add(uIdAccertamento);
				}
				
				if(modGest.getImportoNew()==null){
					modGest.setImportoNew(BigDecimal.ZERO);
				}
				if(importoAttuale==null){
					importoAttuale = BigDecimal.ZERO;
				}
				//////////////////////////
				
				movimento.setImportoOld(modGest.getImportoNew());
				movimento.setImportoNew(modGest.getImportoNew().add(importoAttuale));
				
				movimento.setTipoMovimento(modGest.getTipoMovimento());
				
				movimento.setDescrizione(modGest.getDescrizioneModificaMovimentoGestione());
				
				datiOperazione.setCurrMillisec(System.currentTimeMillis());
				
				
				numeroModifiche = numeroModifiche + 1;
				//problema produzione 15/05
				//SIAC-7505
				accertamentoOttimizzatoDad.aggiornaModificheMovimentoGestioneEntrata(movimento, uIdAccertamento, idEnte, datiOperazione);
			}
		}
		
		return idTestateAccertamentiConModificheContestuali;
	}
	
	protected InsAggOrdinativoIncassoDGInfoDto caricaMappePerDoppiaGestione(OrdinativoIncasso ordinativoDiIncasso, Bilancio bilancio, Richiedente richiedente, Ente ente, SubOrdinativoInModificaInfoDto infoModificheSubOrdinativi) {
		InsAggOrdinativoIncassoDGInfoDto insAggOrdinativoIncassoDGInfoDto = new  InsAggOrdinativoIncassoDGInfoDto();
		
		ArrayList<SubOrdinativoImportoVariatoDto> subOrdConImportoMod = infoModificheSubOrdinativi.getSubOrdinativiModificatiConImportoVariato();
		ArrayList<SubOrdinativoImportoVariatoDto> subOrdEliminati = infoModificheSubOrdinativi.getSubOrdinativiEliminatiConImportoVariato();
		ArrayList<SubOrdinativoImportoVariatoDto> subOrdNuovi = infoModificheSubOrdinativi.getSubOrdinativiNuoviConImportoVariato();
		
		//PER verificare a debug il corretto funzionamento di getSubOrdinativiNuoviOModificatoImporto vedere se 
		// quoteImpattate effettivamente e' la somma di subOrdConImportoMod, subOrdNuovi e subOrdEliminati
		ArrayList<SubOrdinativoImportoVariatoDto> quoteImpattate = infoModificheSubOrdinativi.getAllsPerDoppiaGestione();
		
		if(quoteImpattate!=null && quoteImpattate.size()>0){
			for(SubOrdinativoImportoVariatoDto subOrdConImportoModIt : quoteImpattate){
				SubOrdinativoIncasso subOrdinativoIncasso = (SubOrdinativoIncasso) subOrdConImportoModIt.getSubOrdinativo();
				BigDecimal deltaImportoSubOrd = subOrdConImportoModIt.getDelta();
				
				Integer annoAccertamento = subOrdinativoIncasso.getAccertamento().getAnnoMovimento();
				String annoEsercizio = Integer.toString(bilancio.getAnno());
				BigDecimal numeroAccertamento = null;
				if(subOrdinativoIncasso.getAccertamento() instanceof SubAccertamento){
					// in caso di sub ricavo il numero del padre
					numeroAccertamento = ((SubAccertamento)subOrdinativoIncasso.getAccertamento()).getNumeroAccertamentoPadre();
				}else{
				
					numeroAccertamento = subOrdinativoIncasso.getAccertamento().getNumeroBigDecimal();
				}
				
				
				Accertamento accertamento = (Accertamento) accertamentoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, 
						annoEsercizio, annoAccertamento, numeroAccertamento, CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, true, false);
				
				if(accertamento!=null){
					List<SubAccertamento> subAccs = accertamento.getSubAccertamenti();
					if(subAccs!=null && subAccs.size()>0){
						//HA SUB ACCERTAMENTI
						for(SubAccertamento subIt : subAccs){
							insAggOrdinativoIncassoDGInfoDto.addAccertamento(accertamento, subIt, deltaImportoSubOrd);
						}
					} else {
						//NON HA SUB ACCERTAMENTI
						insAggOrdinativoIncassoDGInfoDto.addAccertamento(accertamento, null, deltaImportoSubOrd);
					}
				}
			}
		}
		
		return insAggOrdinativoIncassoDGInfoDto;
	}
	
	protected EsitoControlliDto operazioniPerDoppiaGestione(OrdinativoIncasso ordinativoDiIncasso, Bilancio bilancio, Richiedente richiedente, Ente ente, 
			DatiOperazioneDto datiOperazione,SubOrdinativoInModificaInfoDto infoModificheSubOrdinativi,Operazione operazioneServizio){
		return operazioniPerDoppiaGestione(ordinativoDiIncasso, bilancio, richiedente, ente, 
				datiOperazione,infoModificheSubOrdinativi,operazioneServizio, null); 
	}
	
	protected EsitoControlliDto operazioniPerDoppiaGestione(OrdinativoIncasso ordinativoDiIncasso, Bilancio bilancio, Richiedente richiedente, Ente ente, 
			DatiOperazioneDto datiOperazione,SubOrdinativoInModificaInfoDto infoModificheSubOrdinativi,Operazione operazioneServizio, List<Integer> uidsAccertamentiConModificaImportoContestualeInsOrdinativo){
		EsitoControlliDto esito = new EsitoControlliDto();
		boolean abilitaDoppiaGest = ordinativoIncassoDad.abilitaDoppiaGestione(bilancio, ordinativoDiIncasso, datiOperazione);
		
		if(abilitaDoppiaGest){
			// doppia gestione : nuova gestione : inizio
			InsAggOrdinativoIncassoDGInfoDto insAggOrdinativoIncassoDGInfoDto = caricaMappePerDoppiaGestione(ordinativoDiIncasso, bilancio, richiedente, ente,infoModificheSubOrdinativi);
			
			esito = gestioneAccSubAccPerDoppiaGestione(ordinativoDiIncasso, insAggOrdinativoIncassoDGInfoDto, bilancio, richiedente, ente, datiOperazione,operazioneServizio, uidsAccertamentiConModificaImportoContestualeInsOrdinativo);
		}
		
		return esito;
   }
	
	protected EsitoControlliDto gestioneAccSubAccPerDoppiaGestione(OrdinativoIncasso ord, InsAggOrdinativoIncassoDGInfoDto dto, 
			Bilancio bil, Richiedente richiedente, Ente ente, DatiOperazioneDto datiOperazione,Operazione operazioneServizio, List<Integer> uidsAccertamentiConModificaImportoContestualeInsOrdinativo){
		EsitoControlliDto esito = new EsitoControlliDto();
		ArrayList<AccertamentoPerDoppiaGestioneInfoDto> listaAccertamenti = dto.getAccertamenti();
		if(listaAccertamenti!=null && listaAccertamenti.size()>0){
			
			int annoBilancio = bil.getAnno();
			int annoBilancioSuccessivo = annoBilancio +1;
			Bilancio bilancioAnnoSuccessivo = commonDad.buildBilancioAnnoSuccessivo(bil, datiOperazione);

			
			for(AccertamentoPerDoppiaGestioneInfoDto accertamento : listaAccertamenti){
				//SIAC-6681
				boolean hasMovgestResiduoInAnnoBilancioSuccessivo =accertamentoOttimizzatoDad.hasMovgestResiduoInAnnoBilancioSuccessivo(datiOperazione, accertamento.getAccertamento(), ente.getUid(), annoBilancioSuccessivo); 
				if(uidsAccertamentiConModificaImportoContestualeInsOrdinativo != null && uidsAccertamentiConModificaImportoContestualeInsOrdinativo.contains(accertamento.getAccertamento().getUid())
						&& !hasMovgestResiduoInAnnoBilancioSuccessivo){
					continue;
				}
				// chiamare il metodo accertamentoOttimizzatoDad.aggiornamentoAccertamentoInDoppiaGest (quello che rispecchia il "2.5.6")
				//l'input sara' accertamento.getAccertamento()
				
				Integer chiaveCapitolo = accertamento.getAccertamento().getChiaveCapitoloEntrataGestione();
				Integer chiaveCapitoloResiduo = 
						accertamentoOttimizzatoDad.getChiaveCapitoloAccertamentoResiduo(datiOperazione, accertamento.getAccertamento().getAnnoMovimento(), 
								accertamento.getAccertamento().getNumeroBigDecimal().intValue(), annoBilancio);
				
				HashMap<Integer, CapitoloEntrataGestione> capitoliDaServizio = 
						caricaCapitolEntrataGestioneEResiduo(richiedente, chiaveCapitolo, chiaveCapitoloResiduo);
				CapitoliInfoDto capitoliInfo = new CapitoliInfoDto();
				capitoliInfo.setCapitoliDaServizioEntrata(capitoliDaServizio);
				
				//SIAC-8894
				if (accertamento.getAccertamento().getProgetto() != null && !isEmpty(accertamento.getAccertamento().getProgetto().getCodice())) {
					Progetto progettoAnnoSucc = accertamentoOttimizzatoDad.verificaProgrammaAnnoSuccessivo(accertamento.getAccertamento().getProgetto(), bilancioAnnoSuccessivo, ente);
					//se non ho trovato il progetto anno successivo allora errore
					if (progettoAnnoSucc == null) {
						List<Errore> errorsProgetto = new ArrayList<Errore>();
						errorsProgetto.add(ErroreFin.PROGETTO_NONTROVATO_DOPPIAGESTIONE_ACCERTAMENTO.getErrore(accertamento.getAccertamento().getProgetto().getCodice()));
						esito.setListaErrori(errorsProgetto);
						return esito;
					} else {
						accertamento.getAccertamento().setProgetto(progettoAnnoSucc);			
					}
				}
				
				//task-78 no su accertamento				
				
				List<Errore> errors = accertamentoOttimizzatoDad.aggiornamentoInDoppiaGestioneAccertamento(richiedente, ente, bil, accertamento.getAccertamento(), datiOperazione,capitoliInfo);
				if(!isEmpty(errors)){
					esito.setListaErrori(errors);
					return esito;
				}
				
			}
		}
		return esito;
	}
	
	protected boolean controlliDescrizioniOrdinativoIncasso(OrdinativoIncasso ordinativo) throws ServiceParamError{
		boolean descrizioniOk = true;
		if(ordinativo!=null){
			if(!isEmpty(ordinativo.getDescrizione()) && ordinativo.getDescrizione().length()>500){
				String lunghezza = "(" + ordinativo.getDescrizione().length() + " catteri, massimo ammesso 500" + ")";
				checkCondition(false, ErroreCore.VALORE_NON_CONSENTITO.getErrore("Descrizione ordinativo", "Descrizione troppo lunga"+lunghezza));
				descrizioniOk = false;
			}
			if(!isEmpty(ordinativo.getElencoSubOrdinativiDiIncasso())){
				for(SubOrdinativoIncasso it: ordinativo.getElencoSubOrdinativiDiIncasso()){
					if(it!=null && !isEmpty(it.getDescrizione()) && it.getDescrizione().length()>500){
						String lunghezza = "(" + it.getDescrizione().length() + " catteri, massimo ammesso 500" + ")";
						checkCondition(false, ErroreCore.VALORE_NON_CONSENTITO.getErrore("Descrizione Quota", " - Descrizione troppo lunga "+lunghezza));
						descrizioniOk = false;
					}
				}
			}
		}
		//in caso di ordinativo nullo ritorno ok = true perche'
		//mi aspetto che il chiamante controlli e lanci gia' un errore specifico per ordinativo nullo
		return descrizioniOk;
	}

	//SIAC-8017-CMTO
	protected BigDecimal extractImportoOrdinativo(OrdinativoIncasso ordinativoDiPagamento) {
		List<SubOrdinativoIncasso> elencoSubOrdinativi = ordinativoDiPagamento.getElencoSubOrdinativiDiIncasso();
		BigDecimal importo = BigDecimal.ZERO; 
		if(elencoSubOrdinativi == null) {
			return importo;
		}
		for (SubOrdinativoIncasso sub : elencoSubOrdinativi) {
			BigDecimal importoSub = sub.getImportoAttuale() != null? sub.getImportoAttuale() :  BigDecimal.ZERO;
			importo = importo.add(importoSub);
		}
		return importo;
	}


	
}