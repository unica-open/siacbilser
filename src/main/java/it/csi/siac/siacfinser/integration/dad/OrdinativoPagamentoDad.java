/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.integration.dao.common.dto.AnnullaOrdinativoPagamentoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneOrdinativoPagamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaEstesaOrdinativiDiPagamentoDto;
import it.csi.siac.siacfinser.integration.dao.ordinativo.OrdinativoDao;
import it.csi.siac.siacfinser.integration.entity.SiacDContotesoreriaFin;
import it.csi.siac.siacfinser.integration.entity.SiacDOrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDRelazTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneOrdFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoProvCassaFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProvCassaFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtil;
import it.csi.siac.siacfinser.integration.util.EntityOrdinativiToModelOrdinativiConverter;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.RicercaEstesaOrdinativoDiPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class OrdinativoPagamentoDad extends OrdinativoDad<OrdinativoPagamento,SubOrdinativoPagamento> {
        
    @Autowired
    OrdinativoDao ordinativoDao;

    
        @Autowired
        SoggettoFinDad soggettoDad;
        
        @Autowired
        CommonDad commonDad;
        
     
    /**
     * Dato il parametro di ricerca indicato restituisce un'anteprima del numero di risultati di ricerca attesi 
     * @param prop
     * @param idEnte
     * @return
     */
	public Long calcolaNumeroOrdinativiPagamentoDaEstrarre(ParametroRicercaOrdinativoPagamento prop, Integer idEnte) {
		Long conteggioOrdinativiPagamento = new Long(0);
		conteggioOrdinativiPagamento = ordinativoDao.contaOrdinativiPagamento(idEnte, prop);
		//Termino restituendo l'oggetto di ritorno: 
        return conteggioOrdinativiPagamento;
	}
	
    /**
     * Dato il parametro di ricerca indicato restituisce un'anteprima del numero di risultati di ricerca attesi 
     * @param prop
     * @param idEnte
     * @return
     */
	public Long calcolaNumeroSubOrdinativiPagamentoDaEstrarre(ParametroRicercaSubOrdinativoPagamento prop, Integer idEnte,int numeroPagina, int numeroRisultatiPerPagina) {
		Long conteggio = new Long(0);
		conteggio = ordinativoDao.contaSubOrdinativiPagamento(idEnte, prop, numeroPagina, numeroRisultatiPerPagina);
		//Termino restituendo l'oggetto di ritorno: 
        return conteggio;
	}

	/**
	 * Dato il parametro di ricerca indicato esegue la ricerca degli ordinativi
	 * @param richiedente
	 * @param prop
	 * @param idEnte
	 * @param numeroPagina
	 * @param numeroRisultatiPerPagina
	 * @param now
	 * @return
	 */
	public List<OrdinativoPagamento> ricercaOrdinativiPagamento(Richiedente richiedente, ParametroRicercaOrdinativoPagamento prop, Integer idEnte, int numeroPagina, int numeroRisultatiPerPagina,DatiOperazioneDto datiOperazione){
		List<SiacTOrdinativoFin> elencoSiacTOrdinativo = new ArrayList<SiacTOrdinativoFin>();
		List<OrdinativoPagamento> elencoOrdinativiPagamento = new ArrayList<OrdinativoPagamento>();
		
		Timestamp now = datiOperazione.getTs();

		elencoSiacTOrdinativo = ordinativoDao.ricercaOrdinativiPagamento(idEnte, prop, numeroPagina, numeroRisultatiPerPagina);

		if(null!=elencoSiacTOrdinativo && elencoSiacTOrdinativo.size() > 0){
			//MARCO - completare ricercaOrdinativiPagamento in OrdinativoPagamentoDad
			elencoOrdinativiPagamento = convertiLista(elencoSiacTOrdinativo, OrdinativoPagamento.class, FinMapId.SiacTOrdinativo_OrdinativoPagamento);
			elencoOrdinativiPagamento = EntityOrdinativiToModelOrdinativiConverter.siacTOrdinativoEntityToOrdinativoPagamentoModel(elencoSiacTOrdinativo, elencoOrdinativiPagamento);
		
			for (SiacTOrdinativoFin siacTOrdinativo : elencoSiacTOrdinativo) {

				for (OrdinativoPagamento ordinativoPagamento : elencoOrdinativiPagamento) {
					
					if (siacTOrdinativo.getOrdId().intValue()==ordinativoPagamento.getUid()) {
						
						BigDecimal importoOrdinativo=BigDecimal.ZERO;

						List<SubOrdinativoPagamento> listaQuoteOrdinativo = new ArrayList<SubOrdinativoPagamento>();
						List<SiacTOrdinativoTFin> listaSiacTOrdinativoT = siacTOrdinativo.getSiacTOrdinativoTs();
						
						if(null != listaSiacTOrdinativoT && listaSiacTOrdinativoT.size() > 0){
							
							for(SiacTOrdinativoTFin siacTOrdinativoT : listaSiacTOrdinativoT){
								
								if(null != siacTOrdinativoT && siacTOrdinativoT.getDataFineValidita() == null){
									
									//Estrazione Liquidazione
									List<SiacRLiquidazioneOrdFin> listaSiacRLiquidazioneOrd = siacTOrdinativoT.getSiacRLiquidazioneOrds();
									if(null != listaSiacRLiquidazioneOrd && listaSiacRLiquidazioneOrd.size() > 0){
										for(SiacRLiquidazioneOrdFin siacRLiquidazioneOrd : listaSiacRLiquidazioneOrd){
											//JIRA SIAC-6842 liquidazione con dataCancellazione null
											if(null != siacRLiquidazioneOrd && siacRLiquidazioneOrd.getDataFineValidita() == null && siacRLiquidazioneOrd.getDataCancellazione() == null){
												if (siacRLiquidazioneOrd.getSiacTLiquidazione() != null){
													// JIRA 1838
													//&& siacRLiquidazioneOrd.getSiacTLiquidazione().getLiqConvalidaManuale()!=null 
													//&& siacRLiquidazioneOrd.getSiacTLiquidazione().getLiqConvalidaManuale().equalsIgnoreCase(CostantiFin.LIQUIDAZIONE_MAUALE_CODE)) {
													Liquidazione liquidazioneT = map(siacRLiquidazioneOrd.getSiacTLiquidazione(), Liquidazione.class, FinMapId.SiacTLiquidazione_Liquidazione);
													Liquidazione liquidazione = liquidazioneDad.ricercaLiquidazionePerChiave(liquidazioneT,CostantiFin.TIPO_RICERCA_DA_LIQUIDAZIONE,
															 richiedente, prop.getAnnoEsercizio(), CostantiFin.AMBITO_FIN, richiedente.getAccount().getEnte(),datiOperazione);

													SubOrdinativoPagamento subOrdinativoPagamento = new SubOrdinativoPagamento();
													subOrdinativoPagamento.setLiquidazione(liquidazione);
													
													//Estrazione dati importo Quota
													if (siacTOrdinativoT.getSiacTOrdinativoTsDets()!=null && siacTOrdinativoT.getSiacTOrdinativoTsDets().size()>0) {
														
														for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : siacTOrdinativoT.getSiacTOrdinativoTsDets()) {

															if (siacTOrdinativoTsDet.getSiacDOrdinativoTsDetTipo().getOrdTsDetTipoCode().equalsIgnoreCase("A")) {
																subOrdinativoPagamento.setImportoAttuale(siacTOrdinativoTsDet.getOrdTsDetImporto());
																importoOrdinativo=importoOrdinativo.add(subOrdinativoPagamento.getImportoAttuale());
															}
														}
														
													}
													
													listaQuoteOrdinativo.add(subOrdinativoPagamento);
												}
											}
										}
									}
									
									
								}
							}
						}
						ordinativoPagamento.setElencoSubOrdinativiDiPagamento(listaQuoteOrdinativo);
						ordinativoPagamento.setImportoOrdinativo(importoOrdinativo);
						
						ordinativoPagamento.setImportoRegolarizzato(getImportoRegolarizzato(siacTOrdinativo, prop));
					}
				}

			}
		}

		//Termino restituendo l'oggetto di ritorno: 
        return elencoOrdinativiPagamento;
	}
	
	
	private BigDecimal getImportoRegolarizzato(SiacTOrdinativoFin siacTOrdinativo, ParametroRicercaOrdinativoPagamento prop) {
		if (prop.getAnnoProvvCassa() == null || prop.getNumeroProvvCassa() == null) {
			return BigDecimal.ZERO;
		}
		
		List<SiacROrdinativoProvCassaFin> siacROrdinativoProvCassas = siacTOrdinativo.getSiacROrdinativoProvCassas();
		
		if (siacROrdinativoProvCassas != null) {
			for (SiacROrdinativoProvCassaFin siacROrdinativoProvCassa : siacROrdinativoProvCassas) {
				
				SiacTProvCassaFin provCassa = siacROrdinativoProvCassa.getSiacTProvCassa();
				
				if (provCassa != null && prop.getAnnoProvvCassa().equals(provCassa.getProvcAnno()) && prop.getNumeroProvvCassa().equals(provCassa.getProvcNumero())) {
					return siacROrdinativoProvCassa.getOrdProvcImporto();
				}
			}
		}
		
		return BigDecimal.ZERO;
	}

	
	/**
	 * ottimizzo il metodo della ricerca ordinativo, eliminando il caricamento dei dati che non servono 
	 * @param richiedente
	 * @param param
	 * @param idEnte
	 * @param numeroPagina
	 * @param numeroRisultatiPerPagina
	 * @param now
	 * @return
	 */
	public List<OrdinativoPagamento> ricercaSinteticaOrdinativiPagamento(Richiedente richiedente, ParametroRicercaOrdinativoPagamento parametri, Integer idEnte, int numeroPagina, int numeroRisultatiPerPagina, Timestamp now){
		List<SiacTOrdinativoFin> elencoSiacTOrdinativo = new ArrayList<SiacTOrdinativoFin>();
		List<OrdinativoPagamento> elencoOrdinativiPagamento = new ArrayList<OrdinativoPagamento>();

		elencoSiacTOrdinativo = ordinativoDao.ricercaOrdinativiPagamento(idEnte, parametri, numeroPagina, numeroRisultatiPerPagina);

		if(null!=elencoSiacTOrdinativo && elencoSiacTOrdinativo.size() > 0){
			
			elencoOrdinativiPagamento = convertiLista(elencoSiacTOrdinativo, OrdinativoPagamento.class, FinMapId.SiacTOrdinativo_OrdinativoPagamento);
			elencoOrdinativiPagamento = EntityOrdinativiToModelOrdinativiConverter.siacTOrdinativoEntityToOrdinativoPagamentoModel(elencoSiacTOrdinativo, elencoOrdinativiPagamento);

			for (SiacTOrdinativoFin siacTOrdinativo : elencoSiacTOrdinativo) {

				for (OrdinativoPagamento ordinativoPagamento : elencoOrdinativiPagamento) {
					
					if (siacTOrdinativo.getOrdId().intValue()==ordinativoPagamento.getUid()) {
						
						impostaSubOrdinativi(siacTOrdinativo, ordinativoPagamento, parametri.isCaricaLiquidazione());
						ordinativoPagamento.setImportoRegolarizzato(getImportoRegolarizzato(siacTOrdinativo, parametri));
					}
				}

			}
		}

        return elencoOrdinativiPagamento;
	}
	
	public List<SubOrdinativoPagamento> ricercaSinteticaSubOrdinativiPagamento(Richiedente richiedente, ParametroRicercaSubOrdinativoPagamento parametri
			, int numeroPagina, int numeroRisultatiPerPagina, DatiOperazioneDto datiOperazioneDto){
		List<SiacTOrdinativoTFin> elencoSiacTOrdinativoT = new ArrayList<SiacTOrdinativoTFin>();
		List<SubOrdinativoPagamento> elencoSubOrdinativiPagamento = new ArrayList<SubOrdinativoPagamento>();

		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		elencoSiacTOrdinativoT = ordinativoDao.ricercaSubOrdinativiPagamento(idEnte, parametri, numeroPagina, numeroRisultatiPerPagina);

		//carico dati ottimizzazione:
		OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione = caricaOttimizzazionePerRicercaSubOrdinativiByOrdinativi(elencoSiacTOrdinativoT, datiOperazioneDto, soggettoDad); 
		//
				
		if(!isEmpty(elencoSiacTOrdinativoT)){
			
			elencoSubOrdinativiPagamento = convertiLista(elencoSiacTOrdinativoT, SubOrdinativoPagamento.class, FinMapId.SiacTOrdinativoT_SubOrdinativoPagamento_ConOrdinativo);
			elencoSubOrdinativiPagamento = EntityOrdinativiToModelOrdinativiConverter.siacTOrdinativoTEntityToSubOrdinativoPagamentoModel(elencoSiacTOrdinativoT, elencoSubOrdinativiPagamento);
		
			for (SiacTOrdinativoTFin siacTOrdinativoT : elencoSiacTOrdinativoT) {
				for (SubOrdinativoPagamento subOrdinativoIt : elencoSubOrdinativiPagamento) {
					if (siacTOrdinativoT.getOrdTsId().intValue()==subOrdinativoIt.getUid()) {
						//CREDITORE/DEBITORE
						Soggetto creditore = componiSoggettoDiOrdinativo(siacTOrdinativoT.getSiacTOrdinativo(), CostantiFin.AMBITO_FIN, idEnte,datiOperazioneDto,datiOttimizzazione);
						subOrdinativoIt.getOrdinativoPagamento().setSoggetto(creditore);
						
						//IMPORTI DEL SUB:
						settaImportiSubOrdinativo(datiOttimizzazione, subOrdinativoIt, siacTOrdinativoT);
					}
				}
			}
			
			//refuso quando ho scritto il codice ispirandomi a ricercaSinteticaOrdinativiPagamento:
			//(valutare se dovesse servire come spunto altrimenti cancellare)
//			for (SiacTOrdinativoTFin siacTOrdinativoT : elencoSiacTOrdinativoT) {
//				for (SubOrdinativoPagamento subOrdinativoPagamento : elencoSubOrdinativiPagamento) {
//					if (siacTOrdinativoT.getOrdId().intValue()==subOrdinativoPagamento.getUid()) {
//						impostaSubOrdinativi(siacTOrdinativoT, subOrdinativoPagamento, parametri.isCaricaLiquidazione());
//					}
//				}
//			}
		}

        return elencoSubOrdinativiPagamento;
	}
	
	public BigDecimal totImporti(Integer enteUid, ParametroRicercaOrdinativoPagamento prop, int numeroPagina, int numeroRisultatiPerPagina) {
		return ordinativoDao.totImportiPagamento(enteUid, prop, numeroPagina, numeroRisultatiPerPagina);
	}
	
	public BigDecimal totImporti(Integer enteUid, ParametroRicercaSubOrdinativoPagamento prop, int numeroPagina, int numeroRisultatiPerPagina) {
		return ordinativoDao.totImportiPagamento(enteUid, prop, numeroPagina, numeroRisultatiPerPagina);
	}

	/**
	 * Sull'elenco di quote dell'ordinativo calcolo l'importo e se caricaLiquidazione è true carico i dati minimi della liquidazione e dell'impegno
	 * @param siacTOrdinativo
	 * @param ordinativoPagamento
	 * @param caricaLiquidazione
	 */
	private void impostaSubOrdinativi(SiacTOrdinativoFin siacTOrdinativo, OrdinativoPagamento ordinativoPagamento, boolean caricaLiquidazione) {
		
		BigDecimal importoOrdinativo=BigDecimal.ZERO;

		List<SubOrdinativoPagamento> listaQuoteOrdinativo = new ArrayList<SubOrdinativoPagamento>();
		List<SiacTOrdinativoTFin> listaSiacTOrdinativoT = siacTOrdinativo.getSiacTOrdinativoTs();
		
		if(null != listaSiacTOrdinativoT && listaSiacTOrdinativoT.size() > 0){
			
			for(SiacTOrdinativoTFin siacTOrdinativoT : listaSiacTOrdinativoT){
				
				if(null != siacTOrdinativoT && siacTOrdinativoT.getDataFineValidita() == null){
					
					
					/**
					 * Scorro i sub per calcolare l'importo dell'ordinativo
					 */
					SubOrdinativoPagamento subOrdinativoPagamento = new SubOrdinativoPagamento();
					if (siacTOrdinativoT.getSiacTOrdinativoTsDets()!=null && siacTOrdinativoT.getSiacTOrdinativoTsDets().size()>0) {
						
						for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : siacTOrdinativoT.getSiacTOrdinativoTsDets()) {

							if (siacTOrdinativoTsDet.getSiacDOrdinativoTsDetTipo().getOrdTsDetTipoCode().equalsIgnoreCase("A")) {
								subOrdinativoPagamento.setImportoAttuale(siacTOrdinativoTsDet.getOrdTsDetImporto());
								importoOrdinativo=importoOrdinativo.add(subOrdinativoPagamento.getImportoAttuale());
							}
						}
						
					}
					
					if(caricaLiquidazione){
						
						impostaLiquidazione(siacTOrdinativoT,subOrdinativoPagamento);
					
					}
					

					listaQuoteOrdinativo.add(subOrdinativoPagamento);	
				
				}
			}
		}
		ordinativoPagamento.setElencoSubOrdinativiDiPagamento(listaQuoteOrdinativo);
		ordinativoPagamento.setImportoOrdinativo(importoOrdinativo);
	}
	

	/**
	 * Sul subOrdinativo si caricano la liquidazione (dati minimi) e l'impegno (dati minimi)
	 * @param siacTOrdinativoT
	 * @param subOrdinativoPagamento
	 */
	private void impostaLiquidazione(SiacTOrdinativoTFin siacTOrdinativoT,
			SubOrdinativoPagamento subOrdinativoPagamento) {
		//Estrazione Liquidazione e Impegno
		List<SiacRLiquidazioneOrdFin> listaSiacRLiquidazioneOrd = siacTOrdinativoT.getSiacRLiquidazioneOrds();
		if(null != listaSiacRLiquidazioneOrd && listaSiacRLiquidazioneOrd.size() > 0){
			
			for(SiacRLiquidazioneOrdFin siacRLiquidazioneOrd : listaSiacRLiquidazioneOrd){
				
				if(null != siacRLiquidazioneOrd && siacRLiquidazioneOrd.getDataFineValidita() == null && siacRLiquidazioneOrd.getDataCancellazione() == null){
					//JIRA SIAC-6842 liquidazione con dataCancellazione null
					if (siacRLiquidazioneOrd.getSiacTLiquidazione() != null){
						
						SiacTLiquidazioneFin siacTLiquidazione = siacRLiquidazioneOrd.getSiacTLiquidazione();
						
						Liquidazione liquidazione = map(siacTLiquidazione, Liquidazione.class, FinMapId.SiacTLiquidazione_Liquidazione_Base);
						
						// Carico l'impegno
						for (SiacRLiquidazioneMovgestFin rLiquidazioneMovgest : siacTLiquidazione.getSiacRLiquidazioneMovgests()) {
							
							if (rLiquidazioneMovgest.getDataFineValidita() == null) { 
								
								Impegno impegno = new Impegno();

								// Rm : il padre puo servire se devo caricare anche i sub, per ora lascio commentato e non aggiungo altro
								// Integer idPadre = rLiquidazioneMovgest.getSiacTMovgestTs().getMovgestTsIdPadre();
								
								impegno.setUid(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getUid());
								impegno.setAnnoMovimento(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestAnno());
								impegno.setDescrizione(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestDesc());
								impegno.setNumeroBigDecimal(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestNumero());
									

								liquidazione.setImpegno(impegno);
							}
						}
						
						
						
						subOrdinativoPagamento.setLiquidazione(liquidazione);
						
						
					}
				}
			}
		}
	}
	
	
	
	/**
	 * Annulla l'ordinativo di pagamento indicato
	 * @param annoEsercizio
	 * @param annoOrdinativoPagamento
	 * @param numeroOrdinativoPagamento
	 * @param datiOperazioneDto
	 * @param richiedente
	 * @return
	 */
	public AnnullaOrdinativoPagamentoInfoDto annullaOrdinativoPagamento(Integer annoEsercizio, Integer annoOrdinativoPagamento, Integer numeroOrdinativoPagamento, DatiOperazioneDto datiOperazioneDto, Richiedente richiedente){
		AnnullaOrdinativoPagamentoInfoDto annullaInfo = new AnnullaOrdinativoPagamentoInfoDto();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();

		SiacDOrdinativoStatoFin siacDOrdinativoStato = new SiacDOrdinativoStatoFin();
		siacDOrdinativoStato = siacDOrdinativoStatoRepository.findDOrdinativoStatoValidoByEnteAndCode(idEnte, CostantiFin.statoOperativoOrdinativoEnumToString(StatoOperativoOrdinativo.ANNULLATO), datiOperazioneDto.getTs());

		SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoRepository.findOrdinativoValidoByAnnoAndNumeroAndTipo(idEnte, annoOrdinativoPagamento, BigDecimal.valueOf(numeroOrdinativoPagamento), CostantiFin.D_ORDINATIVO_TIPO_PAGAMENTO, datiOperazioneDto.getTs());
		
		//Annulla Ordinativo
		if (siacTOrdinativo!=null) {

//			TASK-264
//			setDaTrasmettere(siacTOrdinativo.getUid(), Boolean.TRUE);
			
			Timestamp tsAnnulla = datiOperazioneDto.getTs();
			DatiOperazioneDto datiOperazioneCancella = new DatiOperazioneDto(tsAnnulla.getTime(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());
			
			List<SiacROrdinativoStatoFin> listaRelOrdinativo = siacTOrdinativo.getSiacROrdinativoStatos();
			if (listaRelOrdinativo!=null && listaRelOrdinativo.size()>0) {
				
				//GENNAIO 2018 - In passato non veniva settata la data cancellazione,
				//per mettermi al riparo da questa situzione do una bonificata:
				listaRelOrdinativo = DatiOperazioneUtil.bonificaDataCancellazione(listaRelOrdinativo, siacROrdinativoStatoRepository, datiOperazioneCancella);
				//
				
				boolean invalidatoVecchioStato = false;
				for(SiacROrdinativoStatoFin itStati : listaRelOrdinativo){
					if(DatiOperazioneUtil.isValido(itStati, tsAnnulla)){
						//CANCELLO LOGICAMENTE IL LEGAME CON IL VECCHIO STATO:
						itStati = DatiOperazioneUtil.cancellaRecord(itStati, siacROrdinativoStatoRepository, datiOperazioneCancella, siacTAccountRepository);
						//
						//ok ho trovato il vecchio stato ed e' stato invalidato:
						invalidatoVecchioStato = true;
					}
				}
				
				//IN TEORIA DOVREBBE SEMPRE TROVARE IL VECCHIO STATO MA PER METTERMI 
				//AL RIPARO DA DATI SPORCHI SUL DB:
				//  -inserisco il nuovo stato solo se ho trovato ed invalidato il vecchio stato
				//  -il ciclo for appena eseguito invalida potenzialmente piu' di uno stato valido in caso di dati sporchi sul db, ripristinandone la congruenza
				if(invalidatoVecchioStato){
					// inserisco la riga con lo stato annullato
					SiacROrdinativoStatoFin siacROrdinativoStato = new SiacROrdinativoStatoFin();
					DatiOperazioneDto datiOperazioneInserimento = new DatiOperazioneDto(tsAnnulla.getTime(), Operazione.INSERIMENTO, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());

					siacROrdinativoStato = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacROrdinativoStato, datiOperazioneInserimento, siacTAccountRepository);
					siacROrdinativoStato.setSiacTOrdinativo(siacTOrdinativo);;
					siacROrdinativoStato.setSiacDOrdinativoStato(siacDOrdinativoStato);
					siacROrdinativoStatoRepository.saveAndFlush(siacROrdinativoStato);
				}
			}

			//Cancella Provvisori di cassa
			List<SiacROrdinativoProvCassaFin> listaSiacROrdinativoProvCassa = siacROrdinativoProvCassaRepository.findROrdinativoProvCassaByIdOrdinativo(idEnte, siacTOrdinativo.getOrdId(), datiOperazioneDto.getTs());
			for(SiacROrdinativoProvCassaFin siacROrdinativoProvCassa : listaSiacROrdinativoProvCassa){
				if (siacROrdinativoProvCassa.getDataCancellazione()==null) {
					siacROrdinativoProvCassa = DatiOperazioneUtil.cancellaRecord(siacROrdinativoProvCassa, siacROrdinativoProvCassaRepository, datiOperazioneCancella, siacTAccountRepository);
				}
			}
			
			
			//CR 1914 - silvia mi conferma che la relazione con i doc non si deve annullare
			/**List<SiacTOrdinativoTFin> subOrdinativi = siacTOrdinativo.getSiacTOrdinativoTs();
			for(SiacTOrdinativoTFin siacTOrdinativoTFin : subOrdinativi){
				if (siacTOrdinativoTFin.getDataCancellazione()==null) {
					if(siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs()!=null && siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs().size()> 0){
						SiacRSubdocOrdinativoTFin siacRSubdocOrdinativoTFin = siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs().get(0);
						DatiOperazioneDto datiOperazioneCancella = new DatiOperazioneDto(tsAnnulla.getTime(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());
						siacRSubdocOrdinativoTFin = DatiOperazioneUtil.cancellaRecord(siacRSubdocOrdinativoTFin, siacRSubdocOrdinativoTFinRepository, datiOperazioneCancella, siacTAccountRepository);
					}
				}
			}*/
			annullaInfo.setSiacTOrdinativo(siacTOrdinativo);
			// FIXME: CR 1914 annullare anche altri ordinativi ma si intende i collegati??

		}
		//Termino restituendo l'oggetto di ritorno: 
        return annullaInfo;
	}
	
	/**
	 * annulla la liquidazione automatica indicata
	 * @param annullaInfo
	 * @param datiOperazioneDto
	 * @param annoEsercizio
	 * @param richiedente
	 */
	public void annullaLiquidazioneAutomatica(AnnullaOrdinativoPagamentoInfoDto annullaInfo,DatiOperazioneDto datiOperazioneDto,Integer annoEsercizio,Richiedente richiedente){
		//Annulla Liquidazione automatica
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		SiacTOrdinativoFin siacTOrdinativo = annullaInfo.getSiacTOrdinativo();
		List<SiacTOrdinativoTFin> listaSiacTOrdinativoT = siacTOrdinativo.getSiacTOrdinativoTs();
		if(null != listaSiacTOrdinativoT && listaSiacTOrdinativoT.size() > 0){
			for(SiacTOrdinativoTFin siacTOrdinativoT : listaSiacTOrdinativoT){
				if(null != siacTOrdinativoT && siacTOrdinativoT.getDataFineValidita() == null){

					BigDecimal importoAttualeSubOrdPag=BigDecimal.ZERO;
					List<SiacTOrdinativoTsDetFin> importoCaricato = siacTOrdinativoTsDetRepository.findValidoByTipo(idEnte, datiOperazioneDto.getTs(), CostantiFin.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE, siacTOrdinativoT.getOrdTsId());
					if(importoCaricato!=null && importoCaricato.size()>0){
						importoAttualeSubOrdPag = importoCaricato.get(0).getOrdTsDetImporto();
					}

					//Estrazione Liquidazione
					List<SiacRLiquidazioneOrdFin> listaSiacRLiquidazioneOrd = siacTOrdinativoT.getSiacRLiquidazioneOrds();
					if(null != listaSiacRLiquidazioneOrd && listaSiacRLiquidazioneOrd.size() > 0){
						for(SiacRLiquidazioneOrdFin siacRLiquidazioneOrd : listaSiacRLiquidazioneOrd){
							//JIRA SIAC-6842 liquidazione con dataCancellazione null
							if(null != siacRLiquidazioneOrd && siacRLiquidazioneOrd.getDataFineValidita() == null && siacRLiquidazioneOrd.getDataCancellazione() == null){
								if (siacRLiquidazioneOrd.getSiacTLiquidazione() != null) {
//									Liquidazione liquidazioneT = map(siacRLiquidazioneOrd.getSiacTLiquidazione(), Liquidazione.class, FinMapId.SiacTLiquidazione_Liquidazione);
									
//									Liquidazione liquidazione = liquidazioneDad.ricercaLiquidazionePerChiave(liquidazioneT, CostantiFin.TIPO_RICERCA_DA_LIQUIDAZIONE,
//											 richiedente, annoEsercizio, CostantiFin.AMBITO_FIN, richiedente.getAccount().getEnte(),datiOperazioneDto);
									
									
									// sganciare la chiamata alla ricerca liqudazione 
									SiacTLiquidazioneFin siacTLiquidazione = siacRLiquidazioneOrd.getSiacTLiquidazione();
									
									if(siacTLiquidazione.getLiqConvalidaManuale()!=null && siacTLiquidazione.getLiqConvalidaManuale().equalsIgnoreCase(CostantiFin.LIQUIDAZIONE_AUTOMATICA_CODE)){
												
										String codiceStatoLiquidazione = "";
										if(siacTLiquidazione.getSiacRLiquidazioneStatos()!=null && !siacTLiquidazione.getSiacRLiquidazioneStatos().isEmpty()){
											
											for(SiacRLiquidazioneStatoFin rLiquidazioneStato : siacTLiquidazione.getSiacRLiquidazioneStatos()){
												
												if(rLiquidazioneStato!=null && rLiquidazioneStato.getDataFineValidita()==null && rLiquidazioneStato.getDataCancellazione() == null){
													
													codiceStatoLiquidazione = rLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode();
													break;
													
												}
											}
										}
										
										
										
										// DISPONIBILITA A PAGARE
										// jira 1538, va calcolata solo se la liquidazione non è annullata o porvvisoria
										BigDecimal disponibilitaPagare = BigDecimal.ZERO;
										if(codiceStatoLiquidazione.equalsIgnoreCase(CostantiFin.D_LIQUIDAZIONE_STATO_VALIDO)){
											Ente ente = new Ente();
											ente.setUid(idEnte);
											disponibilitaPagare = liquidazioneDad.calcolaDisponibiltaPagare(ente,siacTLiquidazione);
										}
										
																			
										if((siacTLiquidazione.getLiqImporto().subtract(disponibilitaPagare)).compareTo(importoAttualeSubOrdPag)==0){
											liquidazioneDad.annullaLiquidazione(siacTLiquidazione.getLiqAnno(), siacTLiquidazione.getLiqNumero(), String.valueOf(annoEsercizio), datiOperazioneDto, richiedente);
										}
									}
									
//									if (liquidazione!=null && liquidazione.getLiqManuale()!=null && liquidazione.getLiqManuale().equalsIgnoreCase(CostantiFin.LIQUIDAZIONE_AUTOMATICA_CODE)) {
//										if ((liquidazione.getImportoLiquidazione().subtract(liquidazione.getDisponibilitaPagare())).compareTo(importoAttualeSubOrdPag)==0) {
//											liquidazioneDad.annullaLiquidazione(liquidazione.getAnnoLiquidazione(), liquidazione.getNumeroLiquidazione(), String.valueOf(annoEsercizio), datiOperazioneDto, richiedente);
//										}
//									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	// CR - 1914 imposto l'ordinativo con i dati del DocSpesa (se esiste la relazione)
	// per procedere all'annulla stato
	public OrdinativoPagamento impostaOrdinativoPagPerAggiornaStatoDocSpesa(AnnullaOrdinativoPagamentoInfoDto dto){
		
		OrdinativoPagamento ordinativo = new OrdinativoPagamento();
		
		if(dto.getSiacTOrdinativo()!=null){
			
			SiacTOrdinativoFin tOrdinativo = dto.getSiacTOrdinativo();
			ordinativo.setUid(tOrdinativo.getUid());

			List<SiacTOrdinativoTFin> tSubOrdinativi = tOrdinativo.getSiacTOrdinativoTs();
			List<SubOrdinativoPagamento> elencoSubOrdPagamento = new ArrayList<SubOrdinativoPagamento>();
			
			for (SiacTOrdinativoTFin siacTOrdinativoTFin : tSubOrdinativi) {
				SubOrdinativoPagamento subOrdPagamento = new SubOrdinativoPagamento();
				subOrdPagamento.setUid(siacTOrdinativoTFin.getUid());
				
				SubdocumentoSpesa subDocSpesa = new SubdocumentoSpesa();
				if(siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs()!=null && !siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs().isEmpty()){
					subDocSpesa.setUid(siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs().get(0).getSiacTSubdoc().getUid());
					DocumentoSpesa docSpesa = new DocumentoSpesa();
					docSpesa =map(siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs().get(0).getSiacTSubdoc().getSiacTDoc(), DocumentoSpesa.class, FinMapId.SiacTDocFin_DocumentoSpesa);
					subDocSpesa.setDocumento(docSpesa);
					//SIAC-6345
					subDocSpesa.setNumeroRegistrazioneIVA(siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs().get(0).getSiacTSubdoc().getSubdocNregIva());
					subOrdPagamento.setSubDocumentoSpesa(subDocSpesa);
					elencoSubOrdPagamento.add(subOrdPagamento);
				}
				
			}
			
			ordinativo.setElencoSubOrdinativiDiPagamento(elencoSubOrdPagamento);
		}
		
		return ordinativo;
		
	}

	
	/**
	 * 
	 * @param annoEsercizio
	 * @param atto
	 * @param idEnte
	 * @return
	 */
	public List<RicercaEstesaOrdinativoDiPagamento> ricercaEstesaOrdinativiDiPagamento(Integer annoEsercizio, AttoAmministrativo atto,Integer idEnte) {
		List<RicercaEstesaOrdinativiDiPagamentoDto> ordinativiPagamentoDb = new ArrayList<RicercaEstesaOrdinativiDiPagamentoDto>();
		ordinativiPagamentoDb = ordinativoDao.ricercaEstesaOrdinativiDiPagamento(annoEsercizio, atto, idEnte);
		return convertiLista(ordinativiPagamentoDb, RicercaEstesaOrdinativoDiPagamento.class);
	}

	public void collegaOrdinativi(Integer idOrdinativoPagamento, Integer[] idOrdinativiIncasso, String codiceTipoRelazione, Richiedente richiedente) {
		siacROrdinativoRepository.removeByTipoRelazione(idOrdinativoPagamento, codiceTipoRelazione);

		Date now = new Date();
		Ente ente = richiedente.getAccount().getEnte();
		String loginOperazione = richiedente.getOperatore().getCodiceFiscale();

		SiacDRelazTipoFin siacDRelazTipo = siacDRelazTipoFinRepository.findDRelazTipoValidoByEnteAndCode(ente.getUid(), codiceTipoRelazione);

		SiacTOrdinativoFin siacTOrdinativoDa = new SiacTOrdinativoFin(idOrdinativoPagamento);

		for (Integer idOrdA : idOrdinativiIncasso) {
			SiacROrdinativoFin siacROrdinativo = new SiacROrdinativoFin();
			siacROrdinativo.setSiacDRelazTipo(siacDRelazTipo);
			siacROrdinativo.setDataCreazione(now);
			siacROrdinativo.setDataModifica(now);
			siacROrdinativo.setDataInizioValidita(now);
			siacROrdinativo.setSiacTOrdinativo1(siacTOrdinativoDa);
			siacROrdinativo.setSiacTOrdinativo2(new SiacTOrdinativoFin(idOrdA));
			siacROrdinativo.setSiacTEnteProprietario(new SiacTEnteProprietarioFin(ente.getUid()));
			siacROrdinativo.setLoginOperazione(loginOperazione);

			siacROrdinativoRepository.saveAndFlush(siacROrdinativo);
		}
	}
	
	public BigDecimal getDisponibilitaPagareSottoContoVincolato(ContoTesoreria conto, CapitoloUscitaGestione capitolo, Ente ente, DatiOperazioneDto datiOperazione) {
		final String methodName ="getDisponibilitaPagareSottoContoVincolato";
		if(StringUtils.isNotBlank(conto.getCodice())) {
			SiacDContotesoreriaFin found = siacDContotesoreriaRepository.findContotesoreriaByCode(ente.getUid(), conto.getCodice(), datiOperazione.getTs());
			int uidConto = found != null? found.getUid() : 0;
			conto.setUid(uidConto);
		}
		if(conto.getUid() == 0 || capitolo.getUid() == 0) {
			log.error(methodName, "impossibile reperire la disponibilita senza uid del conto e del capitolo.");
			return null;
		}
		return ordinativoDao.findDisponibilitaPagareSottoContoVincolato(conto.getUid(), capitolo.getUid(), ente.getUid());
	}
	
	//SIAC-8589
	public CapitoloUscitaGestione caricaCapitoloAssociatoAdOrdinativo(Ordinativo ordinativoDiPagamento, DatiOperazioneDto datiOperazione) {
		List<SiacTBilElemFin> bilElems = siacTOrdinativoRepository.caricaCapitoloAssociatoAdOrdinativo(ordinativoDiPagamento.getUid(), datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId());
		if(bilElems == null || bilElems.isEmpty()) {
			return null;
		}
		SiacTBilElemFin bilElem = bilElems.get(0);
		
		return mapNotNull(bilElem, CapitoloUscitaGestione.class, FinMapId.SiacTBilElemFin_CapitoloUscitaGestione_ModelDetail);
	}
}
