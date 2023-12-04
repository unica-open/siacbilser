/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.integration.dao.common.dto.AnnullaOrdinativoIncassoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAggiornamentoMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInInserimentoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneOrdinativoPagamentoDto;
import it.csi.siac.siacfinser.integration.entity.SiacDContotesoreriaFin;
import it.csi.siac.siacfinser.integration.entity.SiacDOrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoProvCassaFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoTsMovgestTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProvCassaFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtil;
import it.csi.siac.siacfinser.integration.util.EntityOrdinativiToModelOrdinativiConverter;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.RegolarizzazioneProvvisorio;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class OrdinativoIncassoDad extends OrdinativoDad<OrdinativoIncasso,SubOrdinativoIncasso> {

	
	@Autowired
	AccertamentoOttimizzatoDad accertamentoOttimizzatoDad;

	/**
	 * Dato il parametro di ricerca indicato restituisce un'anteprima del numero di risultati di ricerca attesi 
	 * @param proi
	 * @param idEnte
	 * @return
	 */
	public Long calcolaNumeroOrdinativiIncassoDaEstrarre(ParametroRicercaOrdinativoIncasso proi, Integer idEnte) {

		Long conteggioOrdinativiIncasso = new Long(0);
		conteggioOrdinativiIncasso = ordinativoDao.contaOrdinativiIncasso(idEnte, proi);

		//Termino restituendo l'oggetto di ritorno: 
        return conteggioOrdinativiIncasso;
	}
	
    /**
     * Dato il parametro di ricerca indicato restituisce un'anteprima del numero di risultati di ricerca attesi 
     * @param prop
     * @param idEnte
     * @return
     */
	public Long calcolaNumeroSubOrdinativiIncassoDaEstrarre(ParametroRicercaSubOrdinativoIncasso prop, Integer idEnte,int numeroPagina, int numeroRisultatiPerPagina) {
		Long conteggio = new Long(0);
		conteggio = ordinativoDao.contaSubOrdinativiIncasso(idEnte, prop,numeroPagina,numeroRisultatiPerPagina);
		//Termino restituendo l'oggetto di ritorno: 
        return conteggio;
	}
	
	public BigDecimal totImporti(Integer enteUid, ParametroRicercaOrdinativoIncasso proi, int numeroPagina, int numeroRisultatiPerPagina) {
		return ordinativoDao.totImportiIncasso(enteUid, proi, numeroPagina, numeroRisultatiPerPagina);
	}
	
	public BigDecimal totImporti(Integer enteUid, ParametroRicercaSubOrdinativoIncasso proi, int numeroPagina, int numeroRisultatiPerPagina) {
		return ordinativoDao.totImportiIncasso(enteUid, proi, numeroPagina, numeroRisultatiPerPagina);
	}

	/**
	 * Dato il parametro di ricerca indicato esegue la ricerca degli ordinativi
	 * @param richiedente
	 * @param proi
	 * @param idEnte
	 * @param numeroPagina
	 * @param numeroRisultatiPerPagina
	 * @param now
	 * @return
	 */
	public List<OrdinativoIncasso> ricercaOrdinativiIncasso(Richiedente richiedente, ParametroRicercaOrdinativoIncasso proi, Integer idEnte, int numeroPagina, int numeroRisultatiPerPagina, Timestamp now){

		List<SiacTOrdinativoFin> elencoSiacTOrdinativo = new ArrayList<SiacTOrdinativoFin>();
		List<OrdinativoIncasso> elencoOrdinativiIncasso = new ArrayList<OrdinativoIncasso>();

		elencoSiacTOrdinativo = ordinativoDao.ricercaOrdinativiIncasso(idEnte, proi, numeroPagina, numeroRisultatiPerPagina);
		
		if(null!=elencoSiacTOrdinativo && elencoSiacTOrdinativo.size() > 0){
			
			elencoOrdinativiIncasso = convertiLista(elencoSiacTOrdinativo, OrdinativoIncasso.class, FinMapId.SiacTOrdinativo_OrdinativoIncasso);
			elencoOrdinativiIncasso = EntityOrdinativiToModelOrdinativiConverter.siacTOrdinativoEntityToOrdinativoIncassoModel(elencoSiacTOrdinativo, elencoOrdinativiIncasso);

			
			for (SiacTOrdinativoFin siacTOrdinativo : elencoSiacTOrdinativo) {

				for (OrdinativoIncasso ordinativoIncasso : elencoOrdinativiIncasso) {
					if (siacTOrdinativo.getOrdId().intValue()==ordinativoIncasso.getUid()) {
						
						BigDecimal importoOrdinativo=BigDecimal.ZERO;

						List<SubOrdinativoIncasso> listaQuoteOrdinativo = new ArrayList<SubOrdinativoIncasso>();
						List<SiacTOrdinativoTFin> listaSiacTOrdinativoT = siacTOrdinativo.getSiacTOrdinativoTs();
						if(null != listaSiacTOrdinativoT && listaSiacTOrdinativoT.size() > 0){
							for(SiacTOrdinativoTFin siacTOrdinativoT : listaSiacTOrdinativoT){
								if(null != siacTOrdinativoT && siacTOrdinativoT.getDataFineValidita() == null){

									SubOrdinativoIncasso subOrdinativoIncasso = new SubOrdinativoIncasso();

									//Estrazione dati importo Quota
									if (siacTOrdinativoT.getSiacTOrdinativoTsDets()!=null && siacTOrdinativoT.getSiacTOrdinativoTsDets().size()>0) {
										for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : siacTOrdinativoT.getSiacTOrdinativoTsDets()) {
											//TODO - DECODIFICA!!!
											if (siacTOrdinativoTsDet.getSiacDOrdinativoTsDetTipo().getOrdTsDetTipoCode().equalsIgnoreCase("A")) {
												subOrdinativoIncasso.setImportoAttuale(siacTOrdinativoTsDet.getOrdTsDetImporto());
												importoOrdinativo=importoOrdinativo.add(subOrdinativoIncasso.getImportoAttuale());
											}
										}
										
									}
									
									listaQuoteOrdinativo.add(subOrdinativoIncasso);
								}
								
								ordinativoIncasso.setElencoSubOrdinativiDiIncasso(listaQuoteOrdinativo);
								ordinativoIncasso.setImportoOrdinativo(importoOrdinativo);
								
								ordinativoIncasso.setImportoRegolarizzato(getImportoRegolarizzato(siacTOrdinativo, proi));
							}
						}
				
					}
				}
			}

		}
		//Termino restituendo l'oggetto di ritorno: 
        return elencoOrdinativiIncasso;
	}
	
	private BigDecimal getImportoRegolarizzato(SiacTOrdinativoFin siacTOrdinativo, ParametroRicercaOrdinativoIncasso proi) {
		
		if (proi.getAnnoProvvCassa() == null || proi.getNumeroProvvCassa() == null) {
			return BigDecimal.ZERO;
		}
		
		List<SiacROrdinativoProvCassaFin> siacROrdinativoProvCassas = siacTOrdinativo.getSiacROrdinativoProvCassas();
		
		if (siacROrdinativoProvCassas != null) {
			for (SiacROrdinativoProvCassaFin siacROrdinativoProvCassa : siacROrdinativoProvCassas) {
				
				SiacTProvCassaFin provCassa = siacROrdinativoProvCassa.getSiacTProvCassa();
				
				if (provCassa != null && proi.getAnnoProvvCassa().equals(provCassa.getProvcAnno()) && proi.getNumeroProvvCassa().equals(provCassa.getProvcNumero())) {
					return siacROrdinativoProvCassa.getOrdProvcImporto();
				}
			}
		}
		
		return BigDecimal.ZERO;
	}

	public List<SubOrdinativoIncasso> ricercaSinteticaSubOrdinativiIncasso(Richiedente richiedente, ParametroRicercaSubOrdinativoIncasso parametri,
			 int numeroPagina, int numeroRisultatiPerPagina, DatiOperazioneDto datiOperazioneDto){
		List<SiacTOrdinativoTFin> elencoSiacTOrdinativoT = new ArrayList<SiacTOrdinativoTFin>();
		List<SubOrdinativoIncasso> elencoSubOrdinativiIncasso = new ArrayList<SubOrdinativoIncasso>();

		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		elencoSiacTOrdinativoT = ordinativoDao.ricercaSubOrdinativiIncasso(idEnte, parametri, numeroPagina, numeroRisultatiPerPagina);

		//carico dati ottimizzazione:
		OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione = caricaOttimizzazionePerRicercaSubOrdinativiByOrdinativi(elencoSiacTOrdinativoT, datiOperazioneDto, soggettoDad); 
		//
		
		if(!isEmpty(elencoSiacTOrdinativoT)){
			
			elencoSubOrdinativiIncasso = convertiLista(elencoSiacTOrdinativoT, SubOrdinativoIncasso.class, FinMapId.SiacTOrdinativoT_SubOrdinativoIncasso_ConOrdinativo);
			elencoSubOrdinativiIncasso = EntityOrdinativiToModelOrdinativiConverter.siacTOrdinativoTEntityToSubOrdinativoIncassoModel(elencoSiacTOrdinativoT, elencoSubOrdinativiIncasso);
			
			for (SiacTOrdinativoTFin siacTOrdinativoT : elencoSiacTOrdinativoT) {
				for (SubOrdinativoIncasso subOrdinativoIt : elencoSubOrdinativiIncasso) {
					if (siacTOrdinativoT.getOrdTsId().intValue()==subOrdinativoIt.getUid()) {
						//CREDITORE/DEBITORE
						Soggetto creditore = componiSoggettoDiOrdinativo(siacTOrdinativoT.getSiacTOrdinativo(), CostantiFin.AMBITO_FIN, idEnte,datiOperazioneDto,datiOttimizzazione);
						subOrdinativoIt.getOrdinativoIncasso().setSoggetto(creditore);
						
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

        return elencoSubOrdinativiIncasso;
	}
	
	/**
	 * si occupa di annullare l'ordinativo di incasso indicato
	 * @param bilancio
	 * @param ente
	 * @param annoOrdinativoIncasso
	 * @param numeroOrdinativoIncasso
	 * @param datiOperazioneDto
	 * @param richiedente
	 */
	public AnnullaOrdinativoIncassoInfoDto annullaOrdinativoIncasso(Bilancio bilancio, Ente ente, Integer annoOrdinativoIncasso, Integer numeroOrdinativoIncasso, DatiOperazioneDto datiOperazioneDto, Richiedente richiedente){

		AnnullaOrdinativoIncassoInfoDto annullaInfo = new AnnullaOrdinativoIncassoInfoDto();
		Integer annoEsercizio = bilancio.getAnno();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		SiacDOrdinativoStatoFin siacDOrdinativoStato = new SiacDOrdinativoStatoFin();
		siacDOrdinativoStato = siacDOrdinativoStatoRepository.findDOrdinativoStatoValidoByEnteAndCode(idEnte, CostantiFin.statoOperativoOrdinativoEnumToString(StatoOperativoOrdinativo.ANNULLATO), datiOperazioneDto.getTs());

		SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoRepository.findOrdinativoValidoByAnnoAndNumeroAndTipo(idEnte, annoOrdinativoIncasso, BigDecimal.valueOf(numeroOrdinativoIncasso), CostantiFin.D_ORDINATIVO_TIPO_INCASSO, datiOperazioneDto.getTs());

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

			//Annulla Accertamento Automatico
			List<SiacTOrdinativoTFin> listaSiacTOrdinativoT = siacTOrdinativo.getSiacTOrdinativoTs();
			if(listaSiacTOrdinativoT!=null && listaSiacTOrdinativoT.size() > 0){
				for(SiacTOrdinativoTFin siacTOrdinativoT : listaSiacTOrdinativoT){
					if(siacTOrdinativoT!=null && siacTOrdinativoT.getDataFineValidita() == null){

						//Estrazione Accertamento
						List<SiacROrdinativoTsMovgestTFin> listaSiacROrdinativoTsMovgestT = siacTOrdinativoT.getSiacROrdinativoTsMovgestTs();
						if(listaSiacROrdinativoTsMovgestT!=null && listaSiacROrdinativoTsMovgestT.size() > 0){
							for(SiacROrdinativoTsMovgestTFin siacROrdinativoTsMovgestT : listaSiacROrdinativoTsMovgestT){
								if(siacROrdinativoTsMovgestT!=null && siacROrdinativoTsMovgestT.getDataFineValidita() == null){
									if (siacROrdinativoTsMovgestT.getSiacTMovgestTs() != null) {

										SiacTMovgestFin siacTMovgest=siacROrdinativoTsMovgestT.getSiacTMovgestTs().getSiacTMovgest();
										Accertamento accertamento = (Accertamento)accertamentoOttimizzatoDad.ricercaMovimentoPk(richiedente,
												ente,
												String.valueOf(annoEsercizio),
												siacTMovgest.getMovgestAnno(),
												siacTMovgest.getMovgestNumero(),
												CostantiFin.MOVGEST_TIPO_ACCERTAMENTO,
												false,
												false);

										//Annullare acc se automatico 
										List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr=siacROrdinativoTsMovgestT.getSiacTMovgestTs().getSiacRMovgestTsAttrs();
										if (listaSiacRMovgestTsAttr!=null && listaSiacRMovgestTsAttr.size()>0) {

											for (SiacRMovgestTsAttrFin siacRMovgestTsAttr : listaSiacRMovgestTsAttr) {
												if (siacRMovgestTsAttr.getSiacTAttr()!=null && 
														siacRMovgestTsAttr.getSiacTAttr().getAttrCode()!=null && 
														siacRMovgestTsAttr.getSiacTAttr().getAttrCode().equalsIgnoreCase(CostantiFin.T_ATTR_CODE_ACC_AUTO)) {
													if (siacRMovgestTsAttr.getBoolean_()!=null && siacRMovgestTsAttr.getBoolean_().equalsIgnoreCase(CostantiFin.TRUE)) {
														DatiOperazioneDto datiOperazioneAnnulla = commonDad.inizializzaDatiOperazione(ente, richiedente, Operazione.ANNULLA, null);
														//Modificato per doppia gestione su annulla movimento
//															Accertamento accertamentoAnnullato = (Accertamento) accertamentoOttimizzatoDad.annullaMovimento(ente, richiedente, accertamento, CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, datiOperazioneAnnulla,bilancio);
														EsitoAggiornamentoMovimentoGestioneDto esitoAnnullaMovimento = accertamentoOttimizzatoDad.annullaMovimento(ente, richiedente, accertamento, CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, datiOperazioneAnnulla,bilancio);
														Accertamento accertamentoAnnullato = (Accertamento)esitoAnnullaMovimento.getMovimentoGestione();
//															break;
													}
												}
											}
										}
									}
								}
							}
						}

					}
				}
			}


			//Cancella Provvisori di cassa
			List<SiacROrdinativoProvCassaFin> listaSiacROrdinativoProvCassa = siacROrdinativoProvCassaRepository.findROrdinativoProvCassaByIdOrdinativo(idEnte, siacTOrdinativo.getOrdId(), datiOperazioneDto.getTs());
			for(SiacROrdinativoProvCassaFin siacROrdinativoProvCassa : listaSiacROrdinativoProvCassa){
				if (siacROrdinativoProvCassa.getDataCancellazione()==null) {
					siacROrdinativoProvCassa = DatiOperazioneUtil.cancellaRecord(siacROrdinativoProvCassa, siacROrdinativoProvCassaRepository, datiOperazioneCancella, siacTAccountRepository);
				}
			}
			
			
			//CR 1914 NON annullo relazione documenti
			/**
			List<SiacTOrdinativoTFin> subOrdinativi = siacTOrdinativo.getSiacTOrdinativoTs();
			for(SiacTOrdinativoTFin siacTOrdinativoTFin : subOrdinativi){
				if (siacTOrdinativoTFin.getDataCancellazione()==null) {
					if(siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs()!=null && siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs().size()> 0){
						SiacRSubdocOrdinativoTFin siacRSubdocOrdinativoTFin = siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs().get(0);
						DatiOperazioneDto datiOperazioneCancella = new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());
						siacRSubdocOrdinativoTFin = DatiOperazioneUtil.cancellaRecord(siacRSubdocOrdinativoTFin, siacRSubdocOrdinativoTFinRepository, datiOperazioneCancella, siacTAccountRepository);
						
					}
				}
			}*/
			annullaInfo.setSiacTOrdinativo(siacTOrdinativo);
		}
		
		return annullaInfo;
	}
	
	
	/**
	 * implementa i controlli 2.5.5 verifiche provvisori di cassa
	 * @param ordinativoDiIncasso 
	 * @param datiOperazione
	 * @param datiInserimento 
	 * @return
	 */
	public List<Errore> verificheProvvisorioDiCassa(OrdinativoIncasso ordinativoDiIncasso, DatiOperazioneDto datiOperazione, OrdinativoInInserimentoInfoDto datiInserimento){
		int idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
		long currMillisec = datiOperazione.getCurrMillisec();
		Timestamp now = new Timestamp(currMillisec);
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		
		List<RegolarizzazioneProvvisorio> listaRegolarizzazioniDiCassa = ordinativoDiIncasso.getElencoRegolarizzazioneProvvisori();
		BigDecimal sommatoriaRegolarizzazioni = BigDecimal.ZERO;
		
		if(listaRegolarizzazioniDiCassa!=null && listaRegolarizzazioniDiCassa.size()>0){
			for(RegolarizzazioneProvvisorio regProvvIt : listaRegolarizzazioniDiCassa){
				if(regProvvIt!=null){
					
					ProvvisorioDiCassa provvisorioDiCassa = regProvvIt.getProvvisorioDiCassa();
					BigDecimal numero = new BigDecimal(provvisorioDiCassa.getNumero());
					Integer anno = provvisorioDiCassa.getAnno();
					SiacTProvCassaFin siacTProvCassa = siacTProvCassaRepository.findProvvisorioDiCassaValidoByAnnoNumero(idEnte, anno, numero, now, CostantiFin.PROVCASSA_TIPO_ENTRATA);
					if(siacTProvCassa==null || siacTProvCassa.getProvcDataAnnullamento()!=null){
						//TODO errore
						return listaErrori;
					}
					if(siacTProvCassa.getProvcDataRegolarizzazione()!=null){
						//TODO errore
						return listaErrori;
					}
					if(provvisorioDiCassa.getImporto()==null){
						//TODO errore
						return listaErrori;
					}
					
					BigDecimal disponibilitaRegolarizzare = 
							calcolaDisponibilitaARegolarizzare(siacTProvCassa.getProvcId(), siacTProvCassa.getProvcImporto(), idEnte, datiOperazione);
					
					if(disponibilitaRegolarizzare.compareTo(provvisorioDiCassa.getImporto())<0){
						//TODO errore
					}
					sommatoriaRegolarizzazioni.add(provvisorioDiCassa.getImporto());
				}
			}
			
			//TOInoltre e' verificato che il totale delle quote regolarizzazione sia uguale all'importo....ecc.
			// TODO
			/*
			BigDecimal importoOrdinativoIncasso = ordinativoDiIncasso.getImportoOrdinativo();
			if(sommatoriaRegolarizzazioni.compareTo(importoOrdinativoIncasso)!=0){
				//TODO errore
			}
			*/
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}

	
	
	// CR - 1914 imposto l'ordinativo con i dati del DocEntrata (se esiste la relazione)
	// per procedere all'annulla stato
	public OrdinativoIncasso impostaOrdinativoIncPerAggiornaStatoDocEntrata(AnnullaOrdinativoIncassoInfoDto dto){
		
		OrdinativoIncasso ordinativo = new OrdinativoIncasso();
		
		if(dto.getSiacTOrdinativo()!=null){
			
			SiacTOrdinativoFin tOrdinativo = dto.getSiacTOrdinativo();
			ordinativo.setUid(tOrdinativo.getUid());

			List<SiacTOrdinativoTFin> tSubOrdinativi = tOrdinativo.getSiacTOrdinativoTs();
			List<SubOrdinativoIncasso> elencoSubOrdIncasso = new ArrayList<SubOrdinativoIncasso>();
			
			for (SiacTOrdinativoTFin siacTOrdinativoTFin : tSubOrdinativi) {
				SubOrdinativoIncasso subOrdIncasso = new SubOrdinativoIncasso();
				subOrdIncasso.setUid(siacTOrdinativoTFin.getUid());
				
				SubdocumentoEntrata subDocEntrata = new SubdocumentoEntrata();
				if(siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs()!=null && 
						!siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs().isEmpty()){
					subDocEntrata.setUid(siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs().get(0).getSiacTSubdoc().getUid());
					//SIAC-6345
					subDocEntrata.setNumeroRegistrazioneIVA(siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs().get(0).getSiacTSubdoc().getSubdocNregIva());
					DocumentoEntrata docEntrata = new DocumentoEntrata();
					docEntrata =map(siacTOrdinativoTFin.getSiacRSubdocOrdinativoTs().get(0).getSiacTSubdoc().getSiacTDoc(), DocumentoEntrata.class, FinMapId.SiacTDocFin_DocumentoEntrata);
					subDocEntrata.setDocumento(docEntrata);
					
					subOrdIncasso.setSubDocumentoEntrata(subDocEntrata);
					elencoSubOrdIncasso.add(subOrdIncasso);
				}
				
			}
			
			ordinativo.setElencoSubOrdinativiDiIncasso(elencoSubOrdIncasso);
		}
		
		return ordinativo;
		
	}

	public BigDecimal getDisponibilitaIncassareSottoContoVincolato(ContoTesoreria conto, CapitoloEntrataGestione capitolo, Ente ente, DatiOperazioneDto datiOperazione) {
			final String methodName ="getDisponibilitaPagareSottoContoVincolato";
			if(conto.getUid() == 0 && StringUtils.isNotBlank(conto.getCodice())) {
				SiacDContotesoreriaFin found = siacDContotesoreriaRepository.findContotesoreriaByCode(ente.getUid(), conto.getCodice(), datiOperazione.getTs());
				int uidConto = found != null? found.getUid() : 0;
				conto.setUid(uidConto);
			}
			if(conto.getUid() == 0 || capitolo.getUid() == 0) {
				log.error(methodName, "impossibile reperire la disponibilita senza uid del conto e del capitolo.");
				return null;
			}
			return ordinativoDao.findDisponibilitaIncassareSottoContoVincolato(conto.getUid(), capitolo.getUid(), ente.getUid());
		}
}
