/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;
//import it.csi.siac.siacfinser.StringUtils;

import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.dao.common.dto.CodificaImportoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneModificheMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneSoggettoDto;
import it.csi.siac.siacfinser.integration.entity.SiacDMovgestStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggrelModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.base.SiacConModificaStato;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.MovimentoGestione.AttributoMovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


@Transactional
public abstract class MovimentoGestioneOptSubsDad<T extends MovimentoGestione, ST extends MovimentoGestione> extends MovimentoGestioneDad<T, ST> {
	
	
	public MovimentoGestione ricercaMovimentoPk(Richiedente richiedente, Ente ente, String annoEsercizio,
			Integer annoMovimento, BigDecimal numeroMovimento,BigDecimal numeroSub, String tipoMovimento, boolean caricaDatiUlteriori)  {

		
		String methodname="ricercaMovimentoPk";
		Integer codiceEnte = ente.getUid();
		T trovatoMovGestione = null;
		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);
		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(), Operazione.INSERIMENTO, siacTEnteProprietario, richiedente.getAccount().getId());

		SiacTMovgestFin siacTMovgest = siacTMovgestRepository.ricercaSiacTMovgestPk(codiceEnte, annoEsercizio, annoMovimento, numeroMovimento, tipoMovimento);	
		
		if(null!=siacTMovgest){

			// Ciclo sulla tabella SiacTMovgestTsFin per estrarre i dettagli dei movimenti gestionali
			List<SiacTMovgestTsFin> listaSiacTMovgestTs = getTestataPiuListaSub(siacTMovgest);
			
			Boolean movimentoConSub = isMovimentoConSub(listaSiacTMovgestTs);
			
			if(null!=listaSiacTMovgestTs && listaSiacTMovgestTs.size() > 0){
				
				
				List<SubImpegno> elencoSubImpegniTuttiConSoloGliIds = new ArrayList<SubImpegno>();
				List<SubAccertamento> elencoSubAccertamentiTuttiConSoloGliIds = new ArrayList<SubAccertamento>();

				List<SubImpegno> elencoSubImpegni = new ArrayList<SubImpegno>();
				List<SubAccertamento> elencoSubAccertamenti = new ArrayList<SubAccertamento>();
				
				// 11 FEB 2016: ottimizzazione soggetto:
				OttimizzazioneMovGestDto ottimizzazioneDto = new OttimizzazioneMovGestDto();
				List<Soggetto> distintiSoggettiCoinvolti = null;
				if(caricaDatiUlteriori){
					OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto = caricaDatiOttimizzazioneRicercaSoggetto(listaSiacTMovgestTs);
					List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti = ottimizzazioneSoggettoDto.getDistintiSiacTSoggettiCoinvolti();
					List<SiacRMovgestTsSogFin> distintiSiacRSoggettiCoinvolti = ottimizzazioneSoggettoDto.getDistintiSiacRSoggettiCoinvolti();
					//mapping model:
					distintiSoggettiCoinvolti = soggettoDad.ricercaSoggettoOPT(distintiSiacTSoggettiCoinvolti, true, false,ottimizzazioneSoggettoDto,datiOperazioneDto);
					//
					ottimizzazioneDto.setDistintiSoggettiCoinvolti(distintiSoggettiCoinvolti);
					ottimizzazioneDto.setDistintiSiacRSoggettiCoinvolti(distintiSiacRSoggettiCoinvolti);
					ottimizzazioneDto.setDistintiSiacTSoggettiCoinvolti(distintiSiacTSoggettiCoinvolti);
				}
				//
				
				//Pre carichiamo gli importi in maniera ottimizzata:
				List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(siacTMovgest.getSiacTMovgestTs(),"SiacTMovgestTsDetFin");
				ottimizzazioneDto.setDistintiSiacTMovgestTsDetCoinvolti(distintiSiacTMovgestTsDetCoinvolti);
				//
				
				
				
				OttimizzazioneMovGestDto ottimizzazioneMovGestDtoPerISub = null;
				DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
				/*caricaDatiOpzionaliDto.setCaricaCig(true);
				caricaDatiOpzionaliDto.setCaricaCup(true);
				caricaDatiOpzionaliDto.setCaricaDisponibileFinanziare(true);
				caricaDatiOpzionaliDto.setCaricaDisponibileLiquidare(true);
				caricaDatiOpzionaliDto.setCaricaDisponibilePagare(true);
				caricaDatiOpzionaliDto.setCaricaElencoModificheMovGest(true);
				caricaDatiOpzionaliDto.setCaricaMutui(true);
				caricaDatiOpzionaliDto.setCaricaVociMutuo(true);*/
				creaListaTuttiSubConISoliIds(richiedente,datiOperazioneDto,siacTMovgest, tipoMovimento, elencoSubImpegniTuttiConSoloGliIds, elencoSubAccertamentiTuttiConSoloGliIds, ottimizzazioneMovGestDtoPerISub ,caricaDatiOpzionaliDto );
				
				
				//Converter con ottimizzazione dto:
				trovatoMovGestione = convertiMovimentoGestione(siacTMovgest,ottimizzazioneDto);
				//
				
				for(SiacTMovgestTsFin siacTMovgestTs : listaSiacTMovgestTs){
					if(null!=siacTMovgestTs && siacTMovgestTs.getDataFineValidita() == null && siacTMovgestTs.getDataCancellazione() == null){
						
						
						if(!Constanti.MOVGEST_TS_TIPO_TESTATA.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
							//STIAMO ITERANDO UN SUB
							
							SubImpegno datiMinimiGiaCaricatiImpegno = CommonUtils.getById(elencoSubImpegniTuttiConSoloGliIds, siacTMovgestTs.getUid());
							
							if(numeroSub==null || new BigDecimal(siacTMovgestTs.getMovgestTsCode()).intValue()!=numeroSub.intValue()){
								//Quello iterato non e' il sub richiesto, solo dati minimi:
								elencoSubImpegni.add(datiMinimiGiaCaricatiImpegno);
								continue;
							}
						}
						
						
						//Se valido
						
						// Estraggo il soggetto
						Soggetto soggettoMovimento = null; 
						
						// 11 FEB 2016: ottimizzazione soggetto:
						//OLD LENTO:
//						if(caricaDatiUlteriori){
//							soggettoMovimento = estraiSoggettoMovimento(Constanti.AMBITO_FIN, idEnte, siacTMovgestTs);
//						}
						//NUOVO AGGIUNTO 11 FEB 2016: ottimizzazione soggetto:
						if(caricaDatiUlteriori){
							SiacTSoggettoFin siacTSog = ottimizzazioneDto.getSoggettoByMovGestTsId(siacTMovgestTs.getMovgestTsId());
							if(siacTSog!=null){
								//puo' non averlo se ha dei sub
								soggettoMovimento = CommonUtils.getSoggettoByCode(distintiSoggettiCoinvolti, siacTSog.getSoggettoCode());
								
								if(soggettoMovimento!=null && soggettoMovimento.getUid()> 0  ){
									soggettoMovimento = estraiSediSecondarieEModalitaPagamento(richiedente,idEnte,  soggettoMovimento.getCodiceSoggetto(), Constanti.AMBITO_FIN,datiOperazioneDto);
								}
							}
						}
						if(soggettoMovimento==null){
							soggettoMovimento = new Soggetto();
						}
						//
						
						String statoValido = "";
						 
						SiacDMovgestStatoFin statoSub = getStato(siacTMovgestTs, datiOperazioneDto);
						statoValido = statoSub.getMovgestStatoCode();

						// Estraggo gli eventuali record di modifica
						List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesa = new ArrayList<ModificaMovimentoGestioneSpesa>();
						List<ModificaMovimentoGestioneEntrata> elencoModificheMovimentoGestioneEntrata = new ArrayList<ModificaMovimentoGestioneEntrata>();

						/*
						 * utilizzate in ricerca impegni per chiave e ricerca accertamento per chiave
						 * con il false esegue lo skip di questa parte di codice per aumentare le performance
						 */
						if(caricaDatiUlteriori){
						   
							if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
								elencoModificheMovimentoGestioneSpesa = estraiElencoModificheMovimentoGestioneSpesa(richiedente, siacTMovgestTs);
							} else if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_ACCERTAMENTO)){
								elencoModificheMovimentoGestioneEntrata = estraiElencoModificheMovimentoGestioneEntrata(richiedente, siacTMovgestTs);
							}
						}
						// Fine estrazione degli eventuali record di modifica

				
						
						// Estraggo le eventuali voci di mutuo
						List<VoceMutuo> elencoVociMutuo = new ArrayList<VoceMutuo>();
						if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
							
							elencoVociMutuo = estraiElencoVociMutuo(annoMovimento, numeroMovimento, richiedente, idEnte, siacTMovgestTs, datiOperazioneDto);
							if(log.isDebugEnabled()){
								if(elencoVociMutuo!=null && !elencoVociMutuo.isEmpty()){
									for (VoceMutuo voceMutuo : elencoVociMutuo) {
										
										log.debug(methodname,"DescrizioneMutuo: " + voceMutuo.getDescrizioneMutuo());
										log.debug(methodname,"DescrizioneVoceMutuo: " + voceMutuo.getDescrizioneVoceMutuo());
										log.debug(methodname,"IdVoceMutuo: " + voceMutuo.getIdVoceMutuo());
										log.debug(methodname,"IdVoceMutuo: " + voceMutuo.getImportoAttualeVoceMutuo());
									}
									
								}
							}
						}		
						
						if(Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
	
							ST trovatoSubMovimento = null;
							trovatoSubMovimento = convertiSubMovimento(siacTMovgestTs, siacTMovgest, caricaDatiUlteriori,ottimizzazioneDto);							

							if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
								// Aggiungo il soggetto al sub-impegno estratto
								if(null!=soggettoMovimento){
									trovatoSubMovimento.setSoggetto(soggettoMovimento);
								}
								
								if(null!=elencoModificheMovimentoGestioneSpesa && elencoModificheMovimentoGestioneSpesa.size() > 0){
									List<ModificaMovimentoGestioneSpesa> listaModificheDefinitiva = new ArrayList<ModificaMovimentoGestioneSpesa>();
									for(ModificaMovimentoGestioneSpesa spesa : elencoModificheMovimentoGestioneSpesa){
										spesa.setTipoMovimento(Constanti.MODIFICA_TIPO_SIM);
										spesa.setUidSubImpegno(trovatoSubMovimento.getUid());
										spesa.setNumeroSubImpegno(trovatoSubMovimento.getNumero().intValue());
										listaModificheDefinitiva.add(spesa);
									}
									((SubImpegno)trovatoSubMovimento).setListaModificheMovimentoGestioneSpesa(listaModificheDefinitiva);
								}
								
								((SubImpegno)trovatoSubMovimento).setListaVociMutuo(elencoVociMutuo);
																
								BigDecimal importoAttuale =  BigDecimal.ZERO;
								
								//disp modifica:
								DisponibilitaMovimentoGestioneContainer disponibilitaModifica = calcolaDisponibilitaImpegnoModifica(siacTMovgestTs.getMovgestTsId(), datiOperazioneDto);
								((SubImpegno)trovatoSubMovimento).setDisponibilitaImpegnoModifica(disponibilitaModifica.getDisponibilita());
								// SIAC-6695
								((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaImpegnoModifica(disponibilitaModifica.getMotivazione());
									
								// calcolo disponibilità a liquidare
								// Jira 1896:  
								// se il sub è di un impegno parzialmente vincolato, la su disponiblità deve essere = 0 (quindi valgono le stesse 
								// condizioni impostate x l'impegno)
								DisponibilitaMovimentoGestioneContainer disponibilitaLiquidareContainer = calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid(), idEnte, datiOperazioneDto);
															
								
								// finanziarie
								// Jira-1784, l'importo a finanziare è sempre 0 perchè l'importo attuale, che si passa al metodo calcola disponibilità, 
								// viene passato sempre uguale a 0, mentre deve essere l'importo dell'impegno se siamo nell'impegno.. del sub se siamo nei sub
								if(((SubImpegno)trovatoSubMovimento).getImportoAttuale()!=null){
									importoAttuale = ((SubImpegno)trovatoSubMovimento).getImportoAttuale();
									
								}
								
								DisponibilitaMovimentoGestioneContainer disponibilitaFinanziare = calcolaDisponibilitaAFinanziare(siacTMovgestTs.getUid(), importoAttuale, statoValido, idEnte, datiOperazioneDto);
																
								((SubImpegno)trovatoSubMovimento).setDisponibilitaLiquidare(disponibilitaLiquidareContainer.getDisponibilita()); // disp liquidare
								// SIAC-6695
								((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaLiquidare(disponibilitaLiquidareContainer.getMotivazione());
								((SubImpegno)trovatoSubMovimento).setDisponibilitaFinanziare(disponibilitaFinanziare.getDisponibilita()); // disp finanz
								// SIAC-6695
								((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaFinanziare(disponibilitaFinanziare.getMotivazione());
								
								BigDecimal disponibilitaLiquidareBase = impegnoDao.calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid());
								((SubImpegno)trovatoSubMovimento).setDisponibilitaLiquidareBase(disponibilitaLiquidareBase);
								
								DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareSubImpegno(siacTMovgestTs, statoValido, idEnte);
								((SubImpegno)trovatoSubMovimento).setDisponibilitaPagare(disponibilitaPagare.getDisponibilita());
								// SIAC-6695
								((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaPagare(disponibilitaPagare.getMotivazione());
								elencoSubImpegni.add(((SubImpegno)trovatoSubMovimento));
								
							} else if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_ACCERTAMENTO)){
								// Aggiungo il soggetto al sub-accertamento estratto
								if(null!=soggettoMovimento){
									trovatoSubMovimento.setSoggetto(soggettoMovimento);
								}
								if(null!=elencoModificheMovimentoGestioneEntrata && elencoModificheMovimentoGestioneEntrata.size() > 0){
									List<ModificaMovimentoGestioneEntrata> listaModificheDefinitiva = new ArrayList<ModificaMovimentoGestioneEntrata>();
									for(ModificaMovimentoGestioneEntrata entrata : elencoModificheMovimentoGestioneEntrata){
										entrata.setTipoMovimento(Constanti.MODIFICA_TIPO_SAC);
										entrata.setUidSubAccertamento(trovatoSubMovimento.getUid());
										entrata.setNumeroSubAccertamento(trovatoSubMovimento.getNumero().intValue());
										listaModificheDefinitiva.add(entrata);
									}
									((SubAccertamento)trovatoSubMovimento).setListaModificheMovimentoGestioneEntrata(listaModificheDefinitiva);
								}
								
								
								DisponibilitaMovimentoGestioneContainer disponibilitaIncassare = calcolaDisponibiltaAIncassareSubAccertamento(siacTMovgestTs, statoValido, idEnte);
								((SubAccertamento)trovatoSubMovimento).setDisponibilitaIncassare(disponibilitaIncassare.getDisponibilita());
								((SubAccertamento)trovatoSubMovimento).setMotivazioneDisponibilitaIncassare(disponibilitaIncassare.getMotivazione());
								
								elencoSubAccertamenti.add(((SubAccertamento)trovatoSubMovimento));
								
							}
						} else if(Constanti.MOVGEST_TS_TIPO_TESTATA.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
							if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
								// Aggiungo il soggetto all'impegno estratto
								if(null!=soggettoMovimento){
									trovatoMovGestione.setSoggetto(soggettoMovimento);
								}
								
								if(null!=elencoModificheMovimentoGestioneSpesa && elencoModificheMovimentoGestioneSpesa.size() > 0){									
									 ((Impegno)trovatoMovGestione).setListaModificheMovimentoGestioneSpesa(elencoModificheMovimentoGestioneSpesa);
								}
								
								((Impegno)trovatoMovGestione).setListaVociMutuo(elencoVociMutuo);
								
								List<SiacTMovgestTsFin> listaTMovGestTs =  siacTMovgest.getSiacTMovgestTs();
								BigDecimal sommatoriaImportoAttualeSubImpegni = BigDecimal.ZERO;
								BigDecimal importoAttualeImpegno = BigDecimal.ZERO;
								
								//disp modifica:
								DisponibilitaMovimentoGestioneContainer disponibilitaModifica = calcolaDisponibilitaImpegnoModifica(siacTMovgestTs.getMovgestTsId(), datiOperazioneDto);
								((Impegno)trovatoMovGestione).setDisponibilitaImpegnoModifica(disponibilitaModifica.getDisponibilita());
								// SIAC-6695
								((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaImpegnoModifica(disponibilitaModifica.getMotivazione());
								
								importoAttualeImpegno = ottimizzazioneDto.estraiImporto(siacTMovgestTs.getUid(), Constanti.MOVGEST_TS_DET_TIPO_ATTUALE);
								
								if(null!=listaTMovGestTs && listaTMovGestTs.size()>0){
									for(SiacTMovgestTsFin itSubs : listaTMovGestTs){
										if(itSubs.getDataFineValidita()==null && 
											Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO.equalsIgnoreCase(itSubs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
											//Se valido e di tipo sub impegno
											BigDecimal importoAttualeSubIt = ottimizzazioneDto.estraiImporto(itSubs.getUid(), Constanti.MOVGEST_TS_DET_TIPO_ATTUALE);
											sommatoriaImportoAttualeSubImpegni = sommatoriaImportoAttualeSubImpegni.add(importoAttualeSubIt);
										}
									}
									
									DisponibilitaMovimentoGestioneContainer disponibilitaFinanziare = calcolaDisponibilitaAFinanziare(siacTMovgestTs.getUid(), importoAttualeImpegno, statoValido, idEnte, datiOperazioneDto);
									
									DisponibilitaMovimentoGestioneContainer disponibilitaSubimpegnare;
									if(!StringUtils.isEmpty(statoValido) && statoValido.equals(Constanti.MOVGEST_STATO_ANNULLATO)){
										disponibilitaSubimpegnare = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Se lo stato e' annullato, la disponibilita' deve essere ZERO");
									}else{
										disponibilitaSubimpegnare = new DisponibilitaMovimentoGestioneContainer(importoAttualeImpegno.subtract(sommatoriaImportoAttualeSubImpegni),
											"Disponibilita calcolata come differenza tra l'importo attuale (" + importoAttualeImpegno + ") e totale dei subimpegni (" + sommatoriaImportoAttualeSubImpegni + ")"); 
									}
									
									DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareImpegno(siacTMovgestTs, statoValido, idEnte);
									((Impegno)trovatoMovGestione).setDisponibilitaPagare(disponibilitaPagare.getDisponibilita());
									// SIAC-6695
									((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaPagare(disponibilitaPagare.getMotivazione());
									
									// carico eventuali VINCOLI legati
									List<VincoloImpegno> listaVincoli = getAccertamentiVincolati(siacTMovgestTs);
									// setto cmq la lista anche se nulla
									((Impegno)trovatoMovGestione).setVincoliImpegno(listaVincoli);
									
									
									// dopo aver preso i vincoli calcolo la disponibilita a liquidare
									DisponibilitaMovimentoGestioneContainer disponibilitaLiquidareContainer = calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid(), idEnte, datiOperazioneDto);
									((Impegno)trovatoMovGestione).setSommaLiquidazioniDoc(importoAttualeImpegno.subtract(disponibilitaLiquidareContainer.getDisponibilita()));
									
									((Impegno)trovatoMovGestione).setTotaleSubImpegni(sommatoriaImportoAttualeSubImpegni);
									((Impegno)trovatoMovGestione).setDisponibilitaSubimpegnare(disponibilitaSubimpegnare.getDisponibilita());
									// SIAC-6695
									((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaSubImpegnare(disponibilitaSubimpegnare.getMotivazione());
									((Impegno)trovatoMovGestione).setDisponibilitaFinanziare(disponibilitaFinanziare.getDisponibilita());
									// SIAC-6695
									((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaFinanziare(disponibilitaFinanziare.getMotivazione());
									((Impegno)trovatoMovGestione).setDisponibilitaLiquidare(disponibilitaLiquidareContainer.getDisponibilita());
									
									BigDecimal disponibilitaLiquidareBase = impegnoDao.calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid());
									((Impegno)trovatoMovGestione).setDisponibilitaLiquidareBase(disponibilitaLiquidareBase);
								}
								
							} else if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_ACCERTAMENTO)){
								// Aggiungo il soggetto all'impegno estratto
								if(null!=soggettoMovimento){
									trovatoMovGestione.setSoggetto(soggettoMovimento);
								}
								if(null!=elencoModificheMovimentoGestioneEntrata && elencoModificheMovimentoGestioneEntrata.size() > 0){
									((Accertamento)trovatoMovGestione).setListaModificheMovimentoGestioneEntrata(elencoModificheMovimentoGestioneEntrata);
								}
								
								// jira 2335: si richiede di abbattare a 0 la disponibilità a inccassare dell'accertamento se ci sono subAccertamenti
								// xchè il disponibile a incassare si calcola poi sui sub
								DisponibilitaMovimentoGestioneContainer disponibilitaIncassare;
								
								if(!movimentoConSub) {
									disponibilitaIncassare = calcolaDisponibiltaAIncassareAccertamento(siacTMovgestTs, statoValido, idEnte);
								} else {
									disponibilitaIncassare = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Se il movimento ha dei sub, la diposnibilita' a incassare deve essere ZERO");
								}
								
								((Accertamento)trovatoMovGestione).setDisponibilitaIncassare(disponibilitaIncassare.getDisponibilita());
								((Accertamento)trovatoMovGestione).setMotivazioneDisponibilitaIncassare(disponibilitaIncassare.getMotivazione());

								// disponibilita a ulitizzare
								DisponibilitaMovimentoGestioneContainer disponibilitaUtilizzare = calcolaDisponibilitaAUtilizzare(siacTMovgestTs,datiOperazioneDto);
								((Accertamento)trovatoMovGestione).setDisponibilitaUtilizzare(disponibilitaUtilizzare.getDisponibilita());
								// SIAC-6695
								((Accertamento)trovatoMovGestione).setMotivazioneDisponibilitaUtilizzare(disponibilitaUtilizzare.getMotivazione());
								
							}
						}
					}
				}
				
				if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
					if(null!=elencoSubImpegni && elencoSubImpegni.size() > 0)
						((Impegno)trovatoMovGestione).setElencoSubImpegni(elencoSubImpegni);
					
					DisponibilitaMovimentoGestioneContainer disponibilitaSubimpegnare = calcolaDisponibilitaImpegnoASubImpegnareEValorizzaTotaleSubImpegni(((Impegno)trovatoMovGestione));
					((Impegno)trovatoMovGestione).setDisponibilitaSubimpegnare(disponibilitaSubimpegnare.getDisponibilita());
					// SIAC-6695
					((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaSubImpegnare(disponibilitaSubimpegnare.getMotivazione());
				} else if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_ACCERTAMENTO)){
					if(null!=elencoSubAccertamenti && elencoSubAccertamenti.size() > 0)
						((Accertamento)trovatoMovGestione).setElencoSubAccertamenti(elencoSubAccertamenti);
					
					DisponibilitaMovimentoGestioneContainer disponibilitaSubAccertare = calcolaDisponibilitaAccertamentoASubAccertareEValorizzaTotaleSubAccertamenti(((Accertamento)trovatoMovGestione));
					((Accertamento)trovatoMovGestione).setDisponibilitaSubAccertare(disponibilitaSubAccertare.getDisponibilita());
					// SIAC-6695
					((Accertamento)trovatoMovGestione).setMotivazioneDisponibilitaSubAccertare(disponibilitaSubAccertare.getMotivazione());
				}
				
			} 
		} 
		

        return trovatoMovGestione;
	}
	
	
	private SubImpegno completaSubImpegnoInPaginazioneDatiMinimi(SubImpegno datiMinimiGiaCaricatiImpegno,SiacTMovgestTsFin siacTMovgestTs,String statoCode,
			DatiOperazioneDto datiOperazioneDto,DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto){
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		//DISPONIBILITA' finanziarie
		if(caricaDatiOpzionaliDto.isCaricaDisponibileFinanziare()){
			//e' gia' stato calcolato per ogni sub nell'elenco tutti sub ids
			//NOTHING TO DO: E' GIA' PRESENTE IN datiMinimiGiaCaricatiImpegno
		} else {
			//devo calcolarlo:
			DisponibilitaMovimentoGestioneContainer disponibilitaFinanziare = calcolaDisponibilitaAFinanziare(siacTMovgestTs.getUid(), datiMinimiGiaCaricatiImpegno.getImportoAttuale(), statoCode, idEnte, datiOperazioneDto);
			datiMinimiGiaCaricatiImpegno.setDisponibilitaFinanziare(disponibilitaFinanziare.getDisponibilita());
			// SIAC-6695
			datiMinimiGiaCaricatiImpegno.setMotivazioneDisponibilitaFinanziare(disponibilitaFinanziare.getMotivazione());
		}
		
		//DISPONIBILITA' pagare
		if(caricaDatiOpzionaliDto.isCaricaDisponibilePagare()){
			//e' gia' stato calcolato per ogni sub nell'elenco tutti sub ids
			//NOTHING TO DO: E' GIA' PRESENTE IN datiMinimiGiaCaricatiImpegno
		}  else {
			//devo calcolarlo:
			DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareSubImpegno(siacTMovgestTs, statoCode, idEnte);
			datiMinimiGiaCaricatiImpegno.setDisponibilitaPagare(disponibilitaPagare.getDisponibilita());
			datiMinimiGiaCaricatiImpegno.setMotivazioneDisponibilitaPagare(disponibilitaPagare.getMotivazione());
		}
		
		return datiMinimiGiaCaricatiImpegno;
	}
	
	private List<SubImpegno> settaTipoImpegnoPerISub(T trovatoMovGestione, List<SubImpegno> elencoSubImpegni){
		//MARZO 2016 - Il classificatore tipo impegno (finanziabile da mutuo, svincolato, ecc, ecc) e' definito nel modello logico dei dati
		//solo per la testata dell'impegno e non per i suoi sub impegni
		//Purtroppo nello sviluppo del codice e' stato erroneamente gestito anche per i singoli sub-impegni.
		//Il problema che si viene a creare e' che quando viene modificato il tipo impegno, tutti i sub impegni restano ancora al vecchio tipo impegno.
		//Per ovviare a questo problema setto a tutti i sub impegni il tipo dell'impengo (unico ad aver significato):
		ClassificatoreGenerico tipoImpegno = ((Impegno) trovatoMovGestione).getTipoImpegno();
		if(null!=elencoSubImpegni && elencoSubImpegni.size() > 0){
			for(SubImpegno it: elencoSubImpegni){
				if(it!=null){
					ClassificatoreGenerico tipoImpegnoCloned = clone(tipoImpegno);
					it.setTipoImpegno(tipoImpegnoCloned);
				}
			}
		}
		return elencoSubImpegni;
	}
	
	private List<SiacTMovgestTsFin> rimuoviAnnullati(List<SiacTMovgestTsFin> listaTMovGestTs,OttimizzazioneMovGestDto ottimizzazioneDto){
		List<SiacTMovgestTsFin> listaTMovGestTsRicostruita = new ArrayList<SiacTMovgestTsFin>();
		if(listaTMovGestTs!=null && listaTMovGestTs.size()>0){
			if(ottimizzazioneDto==null){
				 ottimizzazioneDto = new OttimizzazioneMovGestDto();
			}
			//STATI:
			if(StringUtils.isEmpty(ottimizzazioneDto.getDistintiSiacRMovgestTsStatoCoinvolti())){
				List<SiacRMovgestTsStatoFin> distintiSiacRMovgestTsStatoCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(listaTMovGestTs,"SiacRMovgestTsStatoFin");
				ottimizzazioneDto.setDistintiSiacRMovgestTsStatoCoinvolti(distintiSiacRMovgestTsStatoCoinvolti);
			}
			//
			
			for(SiacTMovgestTsFin itSubs : listaTMovGestTs){
				if(itSubs!=null){
					Integer movgestTsId = itSubs.getMovgestTsId();
					String statoCode = ottimizzazioneDto.estraiStatoCode(movgestTsId);
					if(!Constanti.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoCode)){
						listaTMovGestTsRicostruita.add(itSubs);
					}
					
				}
				
			}
		}
		listaTMovGestTsRicostruita = StringUtils.getElementiNonNulli(listaTMovGestTsRicostruita);
		return listaTMovGestTsRicostruita;
	}
	
	
	/**
	 * 
	 * Itera tutti i SiacTMovgestTsFin e per essi individua solo i SUB MOVIMENTI,
	 * per ognuno di questi SUB MOVIMENTI riporta l'uid e il numero nella lista elencoSubImpegniTuttiConSoloGliIds o nella lista elencoSubAccertamentiTuttiConSoloGliIds
	 * 
	 * metodo interno di ricercaMovimentoPk, utile per avere l'elenco di tutti gli id di tutti i sub.
	 * non deve caricare null'altro per motivi di performance, tranne le seguenti eccezioni:
	 * 
	 * -stato
	 * 
	 * Inoltre tramite il parametro caricaDatiOpzionaliDto e' possibile indicare di caricare alcuni altri dati NON TROPPO LEGGERI, da usare 
	 * con criterio in situazioni particolari
	 * 
	 * @param siacTMovgest
	 * @param tipoMovimento
	 * @param elencoSubImpegniTuttiConSoloGliIds
	 * @param elencoSubAccertamentiTuttiConSoloGliIds
	 */
	private void creaListaTuttiSubConISoliIds(Richiedente richiedente,DatiOperazioneDto datiOperazioneDto, SiacTMovgestFin siacTMovgest,String tipoMovimento,
			List<SubImpegno> elencoSubImpegniTuttiConSoloGliIds,List<SubAccertamento> elencoSubAccertamentiTuttiConSoloGliIds,
			OttimizzazioneMovGestDto ottimizzazioneDto,
			DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto){
		
		List<SiacTMovgestTsFin> listaTMovGestTs =  siacTMovgest.getSiacTMovgestTs();
		
		if(caricaDatiOpzionaliDto == null){
			caricaDatiOpzionaliDto = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		}
		if(ottimizzazioneDto==null){
			 ottimizzazioneDto = new OttimizzazioneMovGestDto();
		}
		
		if(caricaDatiOpzionaliDto.isEscludiAnnullati()){
			listaTMovGestTs = rimuoviAnnullati(listaTMovGestTs, null);
		}
		
		if(null!=listaTMovGestTs && listaTMovGestTs.size()>0){
			
			Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
			Integer annoMovimento = siacTMovgest.getMovgestAnno();
			BigDecimal numeroMovimento = siacTMovgest.getMovgestNumero();
			
			//Per la massima ottimizzazione, ottimizzazioneDto puo' gia' essere valorizzato (in toto o solo in parte) dal chiamante
			//per i casi in cui il chiamante abbia gia' avuto la necessita' di fare tali query
			
			OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModDto = ottimizzazioneDto.getOttimizzazioneModDto();
			if(ottimizzazioneModDto==null){
				ottimizzazioneModDto = new OttimizzazioneModificheMovimentoGestioneDto();
			}
			
			
			//uno alla volta gli elementi in ottimizzazioneDto vengono ricaricati SOLO SE non ricevuti GIA' VALORIZZATi dal chiamante:
			
			//STATI:
			if(StringUtils.isEmpty(ottimizzazioneDto.getDistintiSiacRMovgestTsStatoCoinvolti())){
				List<SiacRMovgestTsStatoFin> distintiSiacRMovgestTsStatoCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(siacTMovgest.getSiacTMovgestTs(),"SiacRMovgestTsStatoFin");
				ottimizzazioneDto.setDistintiSiacRMovgestTsStatoCoinvolti(distintiSiacRMovgestTsStatoCoinvolti);
			}
			//
				
			//IMPORTI:
			if(StringUtils.isEmpty(ottimizzazioneDto.getDistintiSiacTMovgestTsDetCoinvolti())){
				List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(siacTMovgest.getSiacTMovgestTs(),"SiacTMovgestTsDetFin");
				ottimizzazioneDto.setDistintiSiacTMovgestTsDetCoinvolti(distintiSiacTMovgestTsDetCoinvolti);
			}
			//
			
			//SOGGETTI:
			List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti = null;
			List<SiacRMovgestTsSogFin> distintiSiacRSoggettiCoinvolti = null;
			if(StringUtils.isEmpty(ottimizzazioneDto.getDistintiSiacRSoggettiCoinvolti())){
				OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto = caricaDatiMinimiOttimizzazioneSoggetti(siacTMovgest.getSiacTMovgestTs());
				distintiSiacTSoggettiCoinvolti = ottimizzazioneSoggettoDto.getDistintiSiacTSoggettiCoinvolti();
				distintiSiacRSoggettiCoinvolti = ottimizzazioneSoggettoDto.getDistintiSiacRSoggettiCoinvolti();
				ottimizzazioneDto.setDistintiSiacTSoggettiCoinvolti(distintiSiacTSoggettiCoinvolti);
				ottimizzazioneDto.setDistintiSiacRSoggettiCoinvolti(distintiSiacRSoggettiCoinvolti);
			} else {
				distintiSiacTSoggettiCoinvolti = ottimizzazioneDto.getDistintiSiacTSoggettiCoinvolti();
				distintiSiacRSoggettiCoinvolti = ottimizzazioneDto.getDistintiSiacRSoggettiCoinvolti();
			}
			//
			
			//T CLASS:
			if(StringUtils.isEmpty(ottimizzazioneDto.getDistintiSiacRMovgestClassCoinvolti())){
				List<SiacRMovgestClassFin> distintiSiacRMovgestClassCoinvolti =  movimentoGestioneDao.ricercaByMovGestTsMassive(siacTMovgest.getSiacTMovgestTs(),"SiacRMovgestClassFin");
				ottimizzazioneDto.setDistintiSiacRMovgestClassCoinvolti(distintiSiacRMovgestClassCoinvolti);
			}
			
			//T ATTR:
			if(richiestoAlmentoUnAttributoTAttr(caricaDatiOpzionaliDto) && 
					StringUtils.isEmpty(ottimizzazioneDto.getDistintiSiacRMovgestTsAttrCoinvolti())){
				List<SiacRMovgestTsAttrFin> distintiSiacRMovgestTsAttrCoinvolti =  movimentoGestioneDao.ricercaByMovGestTsMassive(siacTMovgest.getSiacTMovgestTs(),"SiacRMovgestTsAttrFin");
				ottimizzazioneDto.setDistintiSiacRMovgestTsAttrCoinvolti(distintiSiacRMovgestTsAttrCoinvolti);
			}
			
			//ATTI AMMINISTRATIVI:
			if(StringUtils.isEmpty(ottimizzazioneDto.getDistintiSiacRMovgestTsAttoAmmCoinvolti())){
				List<SiacRMovgestTsAttoAmmFin> distintiSiacRMovgestTsAttoAmmCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(siacTMovgest.getSiacTMovgestTs(), "SiacRMovgestTsAttoAmmFin");
				ottimizzazioneDto.setDistintiSiacRMovgestTsAttoAmmCoinvolti(distintiSiacRMovgestTsAttoAmmCoinvolti);
			}
			
			
			//ELENCO MODIFICHE MOVIMENTO GESTIONE:
			if(caricaDatiOpzionaliDto.isCaricaElencoModificheMovGest() && StringUtils.isEmpty(ottimizzazioneModDto.getDistintiSiacRMovgestTsSogModFinCoinvolti())){
				//SE dal chiamante non mi arriva gia' valorizzato lo devo caricare ora:
				ottimizzazioneModDto = caricaOttimizzazioneModificheMovimentoGestioneDto(siacTMovgest.getSiacTMovgestTs());
				List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvoltiNelleModifiche = ottimizzazioneModDto.getDistintiSiacTSoggettiCoinvolti();
				OttimizzazioneSoggettoDto ottimizzazioneSoggettoDtoPerModifiche = caricaDatiOttimizzazioneRicercaSoggettoByDistintiSoggetti(distintiSiacTSoggettiCoinvoltiNelleModifiche);
				List<Soggetto> distintiSoggettiCoinvoltiNelleModifiche = soggettoDad.ricercaSoggettoOPT(distintiSiacTSoggettiCoinvoltiNelleModifiche, true, false,ottimizzazioneSoggettoDtoPerModifiche,datiOperazioneDto);
				ottimizzazioneModDto.setDistintiSoggettiCoinvolti(distintiSoggettiCoinvoltiNelleModifiche);
				ottimizzazioneModDto.setOttimizzazioneSoggettoDtoPerModifiche(ottimizzazioneSoggettoDtoPerModifiche);
			}
			//
			
			//DISP LIQUIDARE:
			if(caricaDatiOpzionaliDto.isCaricaDisponibileLiquidareEDisponibilitaInModifica() && tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
				List<CodificaImportoDto> listaDisponibiliLiquidare = new ArrayList<CodificaImportoDto>();
				listaDisponibiliLiquidare = impegnoDao.calcolaDisponibilitaALiquidareMassive(siacTMovgest.getSiacTMovgestTs());
				ottimizzazioneDto.setListaDisponibiliLiquidareDaFunction(listaDisponibiliLiquidare);
			}
			
			
			for(SiacTMovgestTsFin itSubs : listaTMovGestTs){
				if(itSubs.getDataFineValidita()==null && Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO.equalsIgnoreCase(itSubs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
					//Se valido e di tipo sub impegno
					
					MovimentoGestione subIterato = null;
					Integer movgestTsId = itSubs.getMovgestTsId();
					
					String statoCode = ottimizzazioneDto.estraiStatoCode(movgestTsId);
					String statoDesc = ottimizzazioneDto.estraiStatoDescr(movgestTsId);
					Date dataStatoOperativo = ottimizzazioneDto.estraiStatoDataInizioValidata(movgestTsId);
					
					BigDecimal importoAttuale = ottimizzazioneDto.estraiImporto(movgestTsId, Constanti.MOVGEST_TS_DET_TIPO_ATTUALE);
					BigDecimal importoIniziale = ottimizzazioneDto.estraiImporto(movgestTsId, Constanti.MOVGEST_TS_DET_TIPO_INIZIALE);
					
					if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
						subIterato = new SubImpegno();
						((SubImpegno)subIterato).setStatoOperativoMovimentoGestioneSpesa(statoCode);
						((SubImpegno)subIterato).setDescrizioneStatoOperativoMovimentoGestioneSpesa(statoDesc);
						((SubImpegno)subIterato).setDataStatoOperativoMovimentoGestioneSpesa(dataStatoOperativo);
						
						//anno e numero impegno padre:
						((SubImpegno)subIterato).setAnnoImpegnoPadre(siacTMovgest.getMovgestAnno());
						((SubImpegno)subIterato).setNumeroImpegnoPadre(siacTMovgest.getMovgestNumero());
						
					} else if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_ACCERTAMENTO)){
						subIterato = new SubAccertamento();
						((SubAccertamento)subIterato).setStatoOperativoMovimentoGestioneEntrata(statoCode);
						((SubAccertamento)subIterato).setDescrizioneStatoOperativoMovimentoGestioneEntrata(statoDesc);
						((SubAccertamento)subIterato).setDataStatoOperativoMovimentoGestioneEntrata(dataStatoOperativo);
						
						//anno e numero accertamento padre:
						((SubAccertamento)subIterato).setAnnoAccertamentoPadre(siacTMovgest.getMovgestAnno());
						((SubAccertamento)subIterato).setNumeroAccertamentoPadre(siacTMovgest.getMovgestNumero());
					}
					
					//Setto le info su date e utenti di creazione/modifica/cancellazione:
					subIterato = EntityToModelConverter.settaDateEUtenti(subIterato, itSubs);
					
					//setto la descrizione:
					subIterato.setDescrizione(itSubs.getMovgestTsDesc());
					
					//setto gli importi:
					subIterato.setImportoAttuale(importoAttuale);
					subIterato.setImportoIniziale(importoIniziale);
					//
					
					//setto l'id e il numero del sub:
					subIterato.setUid(itSubs.getUid());
					subIterato.setNumero(new BigDecimal(itSubs.getMovgestTsCode()));
					subIterato.setAnnoMovimento(siacTMovgest.getMovgestAnno());
					//
					
					//Setto il SOGGETTO:
					SiacTSoggettoFin siacTSog = ottimizzazioneDto.getSoggettoByMovGestTsId(itSubs.getMovgestTsId());
					Soggetto soggetto = new Soggetto();
					if(siacTSog!=null){
						soggetto.setUid(siacTSog.getSoggettoId());
						soggetto.setCodiceSoggetto(siacTSog.getSoggettoCode());
						soggetto.setCodiceSoggettoNumber(new BigInteger(siacTSog.getSoggettoCode()));
						soggetto.setDenominazione(siacTSog.getSoggettoDesc());
						soggetto.setCodiceFiscale(siacTSog.getCodiceFiscale());
						soggetto.setPartitaIva(siacTSog.getPartitaIva());
						soggetto.setCodiceFiscaleEstero(siacTSog.getCodiceFiscaleEstero());
					}
					subIterato.setSoggetto(soggetto );
					//
					
					//TIPO IMPEGNO:
					if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
						ClassificatoreGenerico tipoImpegno = ottimizzazioneDto.estraiAttributoTClass(movgestTsId,Constanti.D_CLASS_TIPO_TIPO_IMPEGNO);
						((SubImpegno) subIterato).setTipoImpegno(tipoImpegno);
					}
					
					
					//COFOG:
					ClassificatoreGenerico cofog = ottimizzazioneDto.estraiAttributoTClass(movgestTsId, Constanti.D_CLASS_TIPO_GRUPPO_COFOG);
					if(cofog!=null){
						subIterato.setCodCofog(cofog.getCodice());
						subIterato.setDescCofog(cofog.getDescrizione());
					}
					
					//SIOPE:
					ClassificatoreGenerico siope = null;
					if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
						siope = ottimizzazioneDto.estraiAttributoTClass(movgestTsId, Constanti.getCodiciSiopeSpesa());
					} else if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_ACCERTAMENTO)){
						siope = ottimizzazioneDto.estraiAttributoTClass(movgestTsId, Constanti.getCodiciSiopeEntrata());
					}
					if(siope!=null){
						subIterato.setCodSiope(siope.getCodice());
						subIterato.setDescCodSiope(siope.getDescrizione());
					}
					
					
					
					//ATTO AMMINISTRATIVO:
					subIterato = EntityToModelConverter.settaDatiAttoAmmOPT(itSubs, subIterato, ottimizzazioneDto);
					
					
					//FINE DATI MINIMI VELOCI DA CARICARE.
					
					//..
					//..
					
					//INZIO DATI OPZIONALI NON TROPPO LEGGERI DA CARICARE (SOLO SU ESPLICITA RICHIESTA DEL CHIAMANTE) :
					
					
					//DISPONIBILITA' LIQUIDARE
					if(caricaDatiOpzionaliDto.isCaricaDisponibileLiquidareEDisponibilitaInModifica() && tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
						BigDecimal disponibilitaLiquidare = ottimizzazioneDto.estraiDisponibileLiquidareDaFunction(itSubs.getUid());
						((SubImpegno)subIterato).setDisponibilitaLiquidare(disponibilitaLiquidare);
					}
					
					//DISPONIBILITA' finanziarie
					if(caricaDatiOpzionaliDto.isCaricaDisponibileFinanziare() && tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
						DisponibilitaMovimentoGestioneContainer disponibilitaFinanziare = calcolaDisponibilitaAFinanziare(itSubs.getUid(), importoAttuale, statoCode, idEnte, datiOperazioneDto);
						((SubImpegno)subIterato).setDisponibilitaFinanziare(disponibilitaFinanziare.getDisponibilita());
						// SIAC-6695
						((SubImpegno)subIterato).setMotivazioneDisponibilitaFinanziare(disponibilitaFinanziare.getMotivazione());
					}
					
					//DISPONIBILITA' pagare
					if(caricaDatiOpzionaliDto.isCaricaDisponibilePagare() && tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
						DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareSubImpegno(itSubs, statoCode, idEnte);
						((SubImpegno)subIterato).setDisponibilitaPagare(disponibilitaPagare.getDisponibilita());
						((SubImpegno)subIterato).setMotivazioneDisponibilitaPagare(disponibilitaPagare.getMotivazione());
					}
					
					//MUTUI:
					if(caricaDatiOpzionaliDto.isCaricaVociMutuo()){
						List<VoceMutuo> elencoVociMutuo = estraiElencoVociMutuo(annoMovimento , numeroMovimento , richiedente , idEnte, itSubs, datiOperazioneDto);
						((SubImpegno) subIterato).setListaVociMutuo(elencoVociMutuo);
						if(caricaDatiOpzionaliDto.isCaricaMutui()){
							if(elencoVociMutuo!=null && elencoVociMutuo.size()>0){
								List<Mutuo> mutuiAssociati = getListaMutuiAssociati(elencoVociMutuo);
								((SubImpegno) subIterato).setElencoMutui(mutuiAssociati);
							}
						}
					}
					
					//ELENCO MODIFICHE MOVIMENTO GESTIONE:
					if(caricaDatiOpzionaliDto.isCaricaElencoModificheMovGest()){
						if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
							List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesa = estraiElencoModificheMovimentoGestioneSpesa(richiedente, itSubs);
							subIterato = impostaElencoModificheMovimentoGestioneSubImp(elencoModificheMovimentoGestioneSpesa, (ST) subIterato);
						} else if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_ACCERTAMENTO)){
							List<ModificaMovimentoGestioneEntrata> elencoModificheMovimentoGestioneEntrata  = estraiElencoModificheMovimentoGestioneEntrata(richiedente, itSubs);
							subIterato = impostaElencoModificheMovimentoGestioneSubAcc(elencoModificheMovimentoGestioneEntrata, (ST)  subIterato);
						}
					}
					
					//CIG:
					if(caricaDatiOpzionaliDto.isCaricaCig()){
						if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
							String cig = ottimizzazioneDto.getRMovgestTsAttr(itSubs, AttributoMovimentoGestione.cig);
							((SubImpegno) subIterato).setCig(cig);
						}
					}
					
					//CUP:
					if(caricaDatiOpzionaliDto.isCaricaCup()){
						String cup = ottimizzazioneDto.getRMovgestTsAttr(itSubs, AttributoMovimentoGestione.cup);
						subIterato.setCup(cup);
					}
					//
					
					//END DATI OPZIONALI.
					
					//Aggiungo alla lista totale:
					if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
						elencoSubImpegniTuttiConSoloGliIds.add((SubImpegno) subIterato);
					} else if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_ACCERTAMENTO)){
						elencoSubAccertamentiTuttiConSoloGliIds.add((SubAccertamento) subIterato);
					}
					
				}
			}
			
			if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)){
				elencoSubImpegniTuttiConSoloGliIds = (List<SubImpegno>) ordinaByNumero((List<ST>) elencoSubImpegniTuttiConSoloGliIds);
			} else if(tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_ACCERTAMENTO)){
				elencoSubAccertamentiTuttiConSoloGliIds = (List<SubAccertamento>) ordinaByNumero((List<ST>) elencoSubAccertamentiTuttiConSoloGliIds);
			}
			
			
			
		}
	}
	
	private ST impostaElencoModificheMovimentoGestioneSubImp(List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesa,ST trovatoSubMovimento){
		if(null!=elencoModificheMovimentoGestioneSpesa && elencoModificheMovimentoGestioneSpesa.size() > 0){
			List<ModificaMovimentoGestioneSpesa> listaModificheDefinitiva = new ArrayList<ModificaMovimentoGestioneSpesa>();
			for(ModificaMovimentoGestioneSpesa spesa : elencoModificheMovimentoGestioneSpesa){
				spesa.setTipoMovimento(Constanti.MODIFICA_TIPO_SIM);
				spesa.setUidSubImpegno(trovatoSubMovimento.getUid());
				spesa.setNumeroSubImpegno(trovatoSubMovimento.getNumero().intValue());
				listaModificheDefinitiva.add(spesa);
			}
			((SubImpegno)trovatoSubMovimento).setListaModificheMovimentoGestioneSpesa(listaModificheDefinitiva);
		}
		return trovatoSubMovimento;
	}
	
	private ST impostaElencoModificheMovimentoGestioneSubAcc(List<ModificaMovimentoGestioneEntrata> elencoModificheMovimentoGestioneEntrata,ST trovatoSubMovimento){
		if(null!=elencoModificheMovimentoGestioneEntrata && elencoModificheMovimentoGestioneEntrata.size() > 0){
			List<ModificaMovimentoGestioneEntrata> listaModificheDefinitiva = new ArrayList<ModificaMovimentoGestioneEntrata>();
			for(ModificaMovimentoGestioneEntrata entrata : elencoModificheMovimentoGestioneEntrata){
				entrata.setTipoMovimento(Constanti.MODIFICA_TIPO_SAC);
				entrata.setUidSubAccertamento(trovatoSubMovimento.getUid());
				entrata.setNumeroSubAccertamento(trovatoSubMovimento.getNumero().intValue());
				listaModificheDefinitiva.add(entrata);
			}
			((SubAccertamento)trovatoSubMovimento).setListaModificheMovimentoGestioneEntrata(listaModificheDefinitiva);
		}
		return trovatoSubMovimento;
	}
	
	private boolean richiestoAlmentoUnAttributoTAttr(DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto){
		boolean richiestoAlmentoUnAttributoTAttr = false;
		if(caricaDatiOpzionaliDto!=null){
			if(caricaDatiOpzionaliDto.isCaricaCig()){
				return true;
			}
			if(caricaDatiOpzionaliDto.isCaricaCup()){
				return true;
			}
		}
		return richiestoAlmentoUnAttributoTAttr;
	}
	
	private List<ST> ordinaByNumero(List<ST> elenco){
		if(elenco!=null && elenco.size()>1){
			Collections.sort(elenco, new Comparator<ST>() {
				@Override
				public int compare(ST o1, ST o2) {
					
					int numeroUno = o1.getNumero().intValue();
					int numeroDue = o2.getNumero().intValue();
					
					if(numeroUno>numeroDue){
						return 1;
					} else if(numeroDue>numeroUno){
						return -1;
					} else {
						return 0;
					}
						
				}
			});
		}
		return elenco;
	}
	
	/**
	 * NON E' FINALIZZATO A POPOLARE INTERAMENTE UN OGGETTO SOGGETTO
	 * ma solo i dati minimi dei soggetti dei vari movimenti indicati 
	 * @param listaSiacTMovgestTsCoinvolti
	 * @return
	 */
	public OttimizzazioneSoggettoDto caricaDatiMinimiOttimizzazioneSoggetti(List<SiacTMovgestTsFin> listaSiacTMovgestTsCoinvolti){
		OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto = new OttimizzazioneSoggettoDto();

		//1. DISTINTI SiacTSoggettoFin
		List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti = soggettoDao.ricercaBySiacTMovgestPkMassive(listaSiacTMovgestTsCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacTSoggettiCoinvolti(distintiSiacTSoggettiCoinvolti);
		//
		
		//2. DISTINTI SiacRSoggettoRelazFin
		List<SiacRSoggettoRelazFin> distintiSiacRSoggettoRelaz = soggettoDao.ricercaSiacRSoggettoRelazMassive(distintiSiacTSoggettiCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggettoRelaz(distintiSiacRSoggettoRelaz);
		//
		
		//3. DISTINI SiacRMovgestTsSogFin
		List<SiacRMovgestTsSogFin> distintiSiacRSoggettiCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacRMovgestTsSogFin");
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggettiCoinvolti(distintiSiacRSoggettiCoinvolti);
		//
		
		//4. per mod pag e mod pag cessioni:
		List<SiacTModpagFin> distintiSiacTModpagFinCoinvolti = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti,"SiacTModpagFin");
		List<SiacRSoggrelModpagFin> distintiSiacRSoggrelModpagFinCoinvolti = soggettoDao.ricercaSiacRSoggrelModpagFinMassive(distintiSiacRSoggettoRelaz);
		ottimizzazioneSoggettoDto.setDistintiSiacTModpagFinCoinvolti(distintiSiacTModpagFinCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggrelModpagFinCoinvolti(distintiSiacRSoggrelModpagFinCoinvolti);
		//
		
		List<SiacTModpagFin> distintiSiacTModpagFinCoinvoltiPerCessioni = ottimizzazioneSoggettoDto.getListaTModpagsCessioniAll();
		
		List<SiacTModpagFin> tuttiIModPagCoinvolti = CommonUtils.addAllConNewAndSoloDistintiByUid(distintiSiacTModpagFinCoinvolti, distintiSiacTModpagFinCoinvoltiPerCessioni);
		
		List<SiacTModpagModFin> distintiSiacTModpagModFinCoinvolti =  soggettoDao.ricercaSiacTModpagModFinMassive(tuttiIModPagCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacTModpagModFinCoinvolti(distintiSiacTModpagModFinCoinvolti);
		//
		
		
		//Termino restituendo l'oggetto di ritorno: 
        return ottimizzazioneSoggettoDto;
	}
	
	/**
	 * Metodo che aggrega i dati in maniera ottimizzata
	 * @param listaSiacTMovgestTsCoinvolti
	 * @return
	 */
	public OttimizzazioneModificheMovimentoGestioneDto caricaOttimizzazioneModificheMovimentoGestioneDto(List<SiacTMovgestTsFin> listaSiacTMovgestTsCoinvolti){
		OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModificheDto = new OttimizzazioneModificheMovimentoGestioneDto();
		
		//DISTINTI SIAC_R_MOVGEST_TS_SOG_MOD:
		List<SiacRMovgestTsSogModFin> distintiSiacRMovgestTsSogModFinCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacRMovgestTsSogModFin",Boolean.TRUE);
		//
		
		//DISTINTI SIAC_T_MOVGEST_TS_DET_MOD_FIN:
		List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacTMovgestTsDetModFin",Boolean.TRUE);
		//
		
		//DISTINTI SiacRMovgestTsSogclasseModFin
		List<SiacRMovgestTsSogclasseModFin> distintiSiacRMovgestTsSogclasseModFinCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacRMovgestTsSogclasseModFin",Boolean.TRUE);
		//
		
		//DISTINTI SiacTSoggettoFin
		List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti = soggettoDao.ricercaBySiacRMovgestTsSogModFin(distintiSiacRMovgestTsSogModFinCoinvolti, null);
		ottimizzazioneModificheDto.setDistintiSiacTSoggettiCoinvolti(distintiSiacTSoggettiCoinvolti);
		//
		
		//DISTINTI SIAC_R_MOVGEST_TS_SOG_MOD:
		//List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFin =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacTMovgestTsDetModFin",Boolean.TRUE);
		//
		
		List<SiacConModificaStato> listaSiacConModificaStato = new ArrayList<SiacConModificaStato>();
		if(distintiSiacRMovgestTsSogModFinCoinvolti!=null && distintiSiacRMovgestTsSogModFinCoinvolti.size()>0){
			listaSiacConModificaStato.addAll(distintiSiacRMovgestTsSogModFinCoinvolti);
		}
		if(distintiSiacTMovgestTsDetModFinCoinvolti!=null && distintiSiacTMovgestTsDetModFinCoinvolti.size()>0){
			listaSiacConModificaStato.addAll(distintiSiacTMovgestTsDetModFinCoinvolti);
		}
		
		//DISTINTI SIAC_R_MODIFICA_STATO:
		List<SiacRModificaStatoFin> distintiSiacRModificaStatoCoinvolti = movimentoGestioneDao.ricercaBySiacConModificaStatoMassive(listaSiacConModificaStato, Boolean.TRUE);
		List<SiacTModificaFin> distintiSiacTModificaCoinvolti = movimentoGestioneDao.ricercaBySiacRModificaStatoFinMassive(distintiSiacRModificaStatoCoinvolti, Boolean.TRUE);
		
		ottimizzazioneModificheDto.setDistintiSiacRModificaStatoCoinvolti(distintiSiacRModificaStatoCoinvolti);
		ottimizzazioneModificheDto.setDistintiSiacTModificaCoinvolti(distintiSiacTModificaCoinvolti);
		
		
		//POTENZIALE RIDONDANZA. Non sono sicuro di questa query (forse e' ridondante):
		List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvoltiByRModificaStato =movimentoGestioneDao.ricercaSiacTMovgestTsDetModFinBySiacRModificaStatoFinMassive(distintiSiacRModificaStatoCoinvolti, Boolean.TRUE);
		List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvoltiALL = CommonUtils.addAllConNewAndSoloDistintiByUid(distintiSiacTMovgestTsDetModFinCoinvolti, distintiSiacTMovgestTsDetModFinCoinvoltiByRModificaStato);
		ottimizzazioneModificheDto.setDistintiSiacTMovgestTsDetModFinCoinvolti(distintiSiacTMovgestTsDetModFinCoinvoltiALL);
		//il mio dubbio e' che distintiSiacTMovgestTsDetModFinCoinvolti sia gia' uguale a distintiSiacTMovgestTsDetModFinCoinvoltiALL
		
		
		//POTENZIALE RIDONDANZA. Non sono sicuro di questa query (forse e' ridondante):
		List<SiacRMovgestTsSogModFin> distintiSiacRMovgestTsSogModFinCoinvoltiByRModificaStato =movimentoGestioneDao.ricercaSiacRMovgestTsSogModFinBySiacRModificaStatoFinMassive(distintiSiacRModificaStatoCoinvolti, Boolean.TRUE);
		List<SiacRMovgestTsSogModFin> distintiSiacRMovgestTsSogModFinCoinvoltiALL = CommonUtils.addAllConNewAndSoloDistintiByUid(distintiSiacRMovgestTsSogModFinCoinvolti, distintiSiacRMovgestTsSogModFinCoinvoltiByRModificaStato);
		ottimizzazioneModificheDto.setDistintiSiacRMovgestTsSogModFinCoinvolti(distintiSiacRMovgestTsSogModFinCoinvoltiALL);
		//il mio dubbio e' che distintiSiacRMovgestTsSogModFinCoinvolti sia gia' uguale a distintiSiacRMovgestTsSogModFinCoinvoltiALL
		
		
		//POTENZIALE RIDONDANZA. Non sono sicuro di questa query (forse e' ridondante):
		List<SiacRMovgestTsSogclasseModFin> distintiSiacRMovgestTsSogclasseModFinCoinvoltiByRModificaStato =movimentoGestioneDao.ricercaSiacRMovgestTsSogclasseModFinBySiacRModificaStatoFinMassive(distintiSiacRModificaStatoCoinvolti, Boolean.TRUE);
		List<SiacRMovgestTsSogclasseModFin> distintiSiacRMovgestTsSogclasseModFinCoinvoltiALL = CommonUtils.addAllConNewAndSoloDistintiByUid(distintiSiacRMovgestTsSogclasseModFinCoinvolti, distintiSiacRMovgestTsSogclasseModFinCoinvoltiByRModificaStato);
		ottimizzazioneModificheDto.setDistintiSiacRMovgestTsSogclasseModFinCoinvolti(distintiSiacRMovgestTsSogclasseModFinCoinvoltiALL);
		//il mio dubbio e' che distintiSiacRMovgestTsSogclasseModFinCoinvolti sia gia' uguale a distintiSiacRMovgestTsSogclasseModFinCoinvoltiALL
		
		//Termino restituendo l'oggetto di ritorno: 
        return ottimizzazioneModificheDto;
	}
	
	/**
	 * Versione migliorata che non fa uso di chiamate aggiuntive al DB
	 * @param elencoVociMutuo
	 * @return
	 */
	public List<Mutuo> getListaMutuiAssociati(List<VoceMutuo> elencoVociMutuo){
		List<Mutuo> elencoMutui = new ArrayList<Mutuo>();
		if(elencoVociMutuo!=null && elencoVociMutuo.size()>0){
			List<String> listaNumeriMutuo =  listaNumeriMutuo(elencoVociMutuo);
			if(null!=listaNumeriMutuo && !listaNumeriMutuo.isEmpty()){
				for (String numeroMutuo : listaNumeriMutuo) {
					for (VoceMutuo voceMutuo : elencoVociMutuo) {
						if(voceMutuo!=null && voceMutuo.getMutuo()!=null ){
							 if(numeroMutuo.equals(voceMutuo.getMutuo().getCodiceMutuo())){
								 elencoMutui.add(voceMutuo.getMutuo());
							 }
						}
					}
				}
			}
		}
		return elencoMutui;
	}
	
	
	public List<String> listaNumeriMutuo(List<VoceMutuo> vociMutuo){
		List<String> listaNumeriMutuo = new ArrayList<String>();
		HashMap<String, String> mappa = new HashMap<String, String>();
		
		for (VoceMutuo voceMutuo : vociMutuo) {
		   mappa.put(voceMutuo.getNumeroMutuo(), voceMutuo.getNumeroMutuo());	
		}
		
		Set<Entry<String, String>> setMappa = mappa.entrySet();
		Iterator<Entry<String, String>> itMappa = setMappa.iterator();
		
		while(itMappa.hasNext()){
			
			Entry<String, String> hm = itMappa.next();
			listaNumeriMutuo.add((String)hm.getKey());
		}
		
		return listaNumeriMutuo;
		
	}
	
	public List<Mutuo> getListaMutuiAssociati(List<String> listaNumeriMutuo, Integer idEnte){
		
		List<Mutuo> elencoMutui = new ArrayList<Mutuo>();
		if(null!=listaNumeriMutuo && !listaNumeriMutuo.isEmpty()){
			
			for (String numeroMutuo : listaNumeriMutuo) {
				elencoMutui.add(ricercaMutuo(idEnte, numeroMutuo, getNow()));
			}
		}
		
		return elencoMutui;	
	}
	
}