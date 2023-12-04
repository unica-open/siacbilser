/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.liquidazione;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaEstesaLiquidazioniDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaLiquidazioneParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.util.DataValiditaUtil;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;


@Component
@Transactional
public class LiquidazioneFinDaoImpl extends AbstractDao<SiacTLiquidazioneFin, Integer> implements LiquidazioneFinDao {

	@Autowired
	SiacTLiquidazioneFinRepository siacTLiquidazioneRepository;

	/**
	 * wrapper di "creaQueryRicercaLiquidazioni"
	 */
	@Override
	public List<SiacTLiquidazioneFin> ricercaLiquidazioni(Integer enteUid, RicercaLiquidazioneParamDto prl, int numeroPagina, int numeroRisultatiPerPagina) {
		List<SiacTLiquidazioneFin> lista = new ArrayList<SiacTLiquidazioneFin>();
		Query query = creaQueryRicercaLiquidazioni(enteUid, prl, false);

		if(numeroPagina == 0 || numeroRisultatiPerPagina == 0){ 
			lista = query.getResultList();
		} else {
			int start = (numeroPagina - 1) * numeroRisultatiPerPagina;
			int end = start + numeroRisultatiPerPagina;
			
			if (end > query.getResultList().size()){
				end = query.getResultList().size();
			}
			lista = query.getResultList().subList(start, end);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return lista;	
	}

	/**
	 * E' il metodo di "engine" di ricerca delle liquidazioni.
	 * Utilizzato sia per avere un'anteprima del numero di risultati attesi (rispetto al filtro indicato)
	 * sia per recuperare tutti i dati (rispetto al filtro indicato)
	 */
	@Override
	public Query creaQueryRicercaLiquidazioni(Integer enteUid,
			RicercaLiquidazioneParamDto prl, boolean soloCount) {
		Map<String,Object> param = new HashMap<String, Object>();
		Date nowDate = TimingUtils.getNowDate();
		param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
		Query query = null;
		// Parametri di input ricevuti dal servizio :
		BigDecimal numeroLiquidazione = prl.getNumeroLiquidazione();
		Integer annoLiquidazione = prl.getAnnoLiquidazione();
		String codiceSoggettoLiquidazione = prl.getCodiceCreditore();
		
		
		String str = "";
		String valutaSubDocLiquidazione ="";
		if(soloCount==true){
			str = "Select count(*) FROM SiacTLiquidazioneFin liquidazione";
		} else {
			str = "Select distinct liquidazione FROM SiacTLiquidazioneFin liquidazione";
		}

		StringBuilder jpql = new StringBuilder(str);
		
	
		
		//FROM impegno-capitolo
		if (prl.getNumeroCapitolo()!=null ||
				prl.getNumeroArticolo()!=null ||
				prl.getNumeroUEB()!=null ||
				prl.getAnnoImpegno()!=null || 
				prl.getNumeroImpegno()!=null) {				

			jpql.append(", SiacRLiquidazioneMovgestFin rLiquidazioneMovgest");

			if (prl.getNumeroCapitolo()!=null  ||
					prl.getNumeroArticolo()!=null ||
					prl.getNumeroUEB()!=null) {				
				jpql.append(", SiacRMovgestBilElemFin siacRMovgestBilElem");								
			}
		}

		//FROM soggetto
		if(!StringUtilsFin.isEmpty(codiceSoggettoLiquidazione)){
			jpql.append(", SiacRLiquidazioneSoggettoFin rLiquidazioneSoggetto");
		}

		//FROM AttoAmministrativo
		if(prl.getUidProvvedimento()!=null && prl.getUidProvvedimento()!=0 || (null!=prl.getAnnoProvvedimento() && prl.getAnnoProvvedimento().intValue()!=0  && 
				null!=prl.getNumeroProvvedimento() && prl.getNumeroProvvedimento().intValue()!=0 || null!=prl.getTipoProvvedimento())){
			
			jpql.append(", SiacRLiquidazioneAttoAmmFin rLiquidazioneAttoAmm ");
			
			// struttura amministrativa
			if(prl.getUidStrutturaAmministrativaProvvedimento()!=null && prl.getUidStrutturaAmministrativaProvvedimento()!=0){
				jpql.append(" left join rLiquidazioneAttoAmm.siacTAttoAmm.siacRAttoAmmClasses rAttoClass left join rAttoClass.siacTClass tClassAtto ");
			}
		}
		
		//FILTRI SUGLI STATI:
		if(!StringUtilsFin.isEmpty(prl.getStatiDaEscludere())){
			jpql.append(" left join liquidazione.siacRLiquidazioneStatos rLiquidazioneStato left join rLiquidazioneStato.siacDLiquidazioneStato dLiquidazioneStato");
		}
		
		//INIZIO WHERE:
		jpql.append(" WHERE liquidazione.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId");
		
		jpql.append(" AND  ").append(DataValiditaUtil.validitaForQuery("liquidazione"));
		param.put("enteProprietarioId", enteUid);
				
		jpql.append(" AND liquidazione.siacTBil.siacTPeriodo.anno = :annoEsercizio");
		param.put("annoEsercizio", prl.getAnnoEsercizio().toString());
		
		//WHERE Liquidazione
		if(numeroLiquidazione!=null && numeroLiquidazione.intValue()!=0){
			jpql.append(" AND liquidazione.liqNumero = :numeroLiquidazione");
			param.put("numeroLiquidazione", numeroLiquidazione);
		}			
		if(annoLiquidazione!=null && annoLiquidazione!=0){
			jpql.append(" AND liquidazione.liqAnno = :annoLiquidazione");
			param.put("annoLiquidazione", annoLiquidazione);	
		}

		
		// Se la ricerca arriva da inserisci ordinativo non devo prendere le liquidazioni con documenti
		if(prl.getTipoRicerca().equals(CostantiFin.TIPO_RICERCA_DA_ORDINATIVO)){
			// jpql.append(", SiacRSubdocLiquidazioneFin siacRSubdocLiquidazione");
			// valutaSubDocLiquidazione=" and siacRSubdocLiquidazione.siacTLiquidazione is null";
			jpql.append(" AND NOT EXISTS ( FROM liquidazione.siacRSubdocLiquidaziones rsubdoc, SiacTSubdocFin subdoc " +
					" WHERE rsubdoc.siacTSubdoc = subdoc )");
					
			
		}
			
		//jira 1339 - ricerca capitolo con 0
		if(prl.getNumeroCapitolo()!=null ||
				prl.getNumeroArticolo()!=null ||
				prl.getNumeroUEB()!=null ||
				prl.getNumeroImpegno()!= null ||
				prl.getAnnoImpegno() != null) {				
			jpql.append(" AND rLiquidazioneMovgest.siacTLiquidazione.liqId = liquidazione.liqId");	
			
			
			if(prl.getNumeroImpegno()!= null && prl.getNumeroImpegno().intValue() != 0){
				if (prl.getNumeroSubImpegno()!=null) {

					jpql.append(" AND rLiquidazioneMovgest.siacTMovgestT.movgestTsCode=  :numeroSubImpegno");
					jpql.append(" AND rLiquidazioneMovgest.siacTMovgestT.siacTMovgest.movgestNumero=  :numeroImpegno");
					
					param.put("numeroSubImpegno", prl.getNumeroSubImpegno().intValue()+"");
					param.put("numeroImpegno", prl.getNumeroImpegno());
					
				} else {
					jpql.append(" AND rLiquidazioneMovgest.siacTMovgestT.siacTMovgest.movgestNumero = :numeroImpegno ");
					param.put("numeroImpegno", prl.getNumeroImpegno());

				}
			}
		
			if(prl.getAnnoImpegno() != null){
				jpql.append(" AND rLiquidazioneMovgest.siacTMovgestT.siacTMovgest.movgestAnno = :annoImpegno ");
				param.put("annoImpegno",prl.getAnnoImpegno());
			}

			if (prl.getNumeroCapitolo()!=null ||
					prl.getNumeroArticolo()!=null ||
					prl.getNumeroUEB()!=null){

				jpql.append(" AND rLiquidazioneMovgest.siacTMovgestT.siacTMovgest.movgestId = siacRMovgestBilElem.siacTMovgest.movgestId");

				if(prl.getNumeroCapitolo()!=null){
					jpql.append(" AND siacRMovgestBilElem.siacTBilElem.elemCode = :numeroCapitolo");
					param.put("numeroCapitolo", prl.getNumeroCapitolo().intValue()+"");
				}						
				if(prl.getNumeroArticolo()!=null){
					jpql.append(" AND siacRMovgestBilElem.siacTBilElem.elemCode2 = :numeroArticolo");
					jpql.append(" AND siacRMovgestBilElem.dataFineValidita IS NULL AND siacRMovgestBilElem.dataCancellazione IS NULL "); // SIAC-6449
					param.put("numeroArticolo", prl.getNumeroArticolo().intValue()+"");
				}						
				if(prl.getNumeroUEB()!=null){
					jpql.append(" AND siacRMovgestBilElem.siacTBilElem.elemCode3 = :numeroUEB");				
					param.put("numeroUEB", prl.getNumeroUEB().toString());
				}			
				if(prl.getAnnoCapitolo()!=null){
					jpql.append(" AND siacRMovgestBilElem.siacTBilElem.siacTBil.siacTPeriodo.anno = :annoCapitolo");				
					param.put("annoCapitolo", prl.getAnnoCapitolo().toString());
				}
			}
			
			jpql.append(" AND (rLiquidazioneMovgest.dataFineValidita IS NULL AND rLiquidazioneMovgest.dataCancellazione IS NULL) ");
		}
		
		//STATO:
		if(!StringUtilsFin.isEmpty(prl.getStatiDaEscludere()) ){
			
			String statiDaEscludere = buildElencoPerClausolaIN(prl.getStatiDaEscludere());
			jpql.append(" AND dLiquidazioneStato.liqStatoCode NOT IN :statiDaEscludere");
			param.put("statiDaEscludere", statiDaEscludere);
			
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rLiquidazioneStato"));
		}
		
	
		
		//WHERE soggetto
		if(!StringUtilsFin.isEmpty(codiceSoggettoLiquidazione)){
			jpql.append(" AND rLiquidazioneSoggetto.siacTLiquidazione.liqId = liquidazione.liqId");
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rLiquidazioneSoggetto"));
			jpql.append(" AND rLiquidazioneSoggetto.siacTSoggetto.soggettoCode = :codiceSoggettoLiquidazione");
			param.put("codiceSoggettoLiquidazione", codiceSoggettoLiquidazione);
		} 
		
		//WHERE AttoAmministrativo
		//Provvedimento: se arriva l'uid vado direttamente a cercare per quello
		if(prl.getUidProvvedimento() != null && prl.getUidProvvedimento() != 0){
				jpql.append(" AND rLiquidazioneAttoAmm.siacTLiquidazione.liqId = liquidazione.liqId");
				jpql.append(" AND (rLiquidazioneAttoAmm.dataFineValidita IS NULL or rLiquidazioneAttoAmm.dataCancellazione IS NULL)");
				jpql.append(" AND rLiquidazioneAttoAmm.siacTAttoAmm.attoammId = :attoId ");
				param.put("attoId", prl.getUidProvvedimento());
		} else{
			
			if((null!=prl.getAnnoProvvedimento() && prl.getAnnoProvvedimento().intValue()!=0  
					&& null!=prl.getNumeroProvvedimento() && prl.getNumeroProvvedimento().intValue()!=0) || 
					(null!=prl.getAnnoProvvedimento() && prl.getAnnoProvvedimento().intValue()!=0  
					&& null!=prl.getTipoProvvedimento() && !StringUtilsFin.isEmpty(prl.getTipoProvvedimento()))){
				
				jpql.append(" AND rLiquidazioneAttoAmm.siacTLiquidazione.liqId = liquidazione.liqId");
				jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rLiquidazioneAttoAmm"));
				
				if(null!=prl.getAnnoProvvedimento()){
					jpql.append(" AND rLiquidazioneAttoAmm.siacTAttoAmm.attoammAnno = :annoProvvedimento");
					param.put("annoProvvedimento", prl.getAnnoProvvedimento().toString());
				}
					
				if(null!=prl.getAnnoProvvedimento() && null!=prl.getNumeroProvvedimento()){
					jpql.append(" AND rLiquidazioneAttoAmm.siacTAttoAmm.attoammNumero = :numeroProvvedimento");
					param.put("numeroProvvedimento", Integer.valueOf(prl.getNumeroProvvedimento().intValue()));
				}
				
				if(null!=prl.getAnnoProvvedimento() && null!=prl.getTipoProvvedimento()) {
					jpql.append(" AND rLiquidazioneAttoAmm.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId = :codiceTipoProvvedimento");
					param.put("codiceTipoProvvedimento", Integer.valueOf(prl.getTipoProvvedimento()));
				}
				
				if(prl.getUidStrutturaAmministrativaProvvedimento()!=null && prl.getUidStrutturaAmministrativaProvvedimento()!=0){
					jpql.append(" AND tClassAtto.classifId = :uidStrutturaAmmProvvedimento ");
					param.put("uidStrutturaAmmProvvedimento", prl.getUidStrutturaAmministrativaProvvedimento());
				}
				
			}
			
		}


		if(soloCount==false){
			jpql.append(" ORDER BY liquidazione.liqNumero");
		} 
		
		query = createQuery(jpql.toString(), param);			

		//Termino restituendo l'oggetto di ritorno: 
        return query;
	}

	/**
	 * Wrapper di creaQueryRicercaLiquidazioni per avere un'anteprima del numero di risultati attesi
	 */
	@Override
	public Long contaLiquidazioni(Integer enteUid, RicercaLiquidazioneParamDto prl) {
		Long conteggioLiquidazioni = new Long(0);
		Query query = creaQueryRicercaLiquidazioni(enteUid, prl, true);
		conteggioLiquidazioni = (Long)query.getSingleResult();
		//Termino restituendo l'oggetto di ritorno: 
        return conteggioLiquidazioni;
	}

	/**
	 * Carica la liquidazione referenziata dalla chiave di ricerca indicata
	 */
	@Override
	public SiacTLiquidazioneFin ricercaLiquidazionePerChiave(Integer codiceEnte, Integer annoLiquidazione, BigDecimal numeroLiquidazione, Timestamp now, String annoBilancio) {
		SiacTLiquidazioneFin siacTLiquidazione = null;
		siacTLiquidazione = siacTLiquidazioneRepository.findLiquidazioneByAnnoNumeroAnnoBilancio(codiceEnte, annoLiquidazione, numeroLiquidazione, annoBilancio, now);
		//Termino restituendo l'oggetto di ritorno: 
        return siacTLiquidazione;
	}
	
	/**
	 * Carica la liquidazione referenziata dalla chiave di ricerca indicata
	 */
	@Override
	public SiacTLiquidazioneFin ricercaLiquidazionePerChiaveLiqManuale(Integer codiceEnte, Liquidazione liquidazione, Timestamp now, Integer annoBilancio) {
		SiacTLiquidazioneFin siacTLiquidazione = null;
		siacTLiquidazione = siacTLiquidazioneRepository.findLiquidazioneByAnnoNumeroBilancioLiqConvalida(codiceEnte, liquidazione.getAnnoLiquidazione(),
											liquidazione.getNumeroLiquidazione(), now, annoBilancio, liquidazione.getLiqManuale());
		//Termino restituendo l'oggetto di ritorno: 
        return siacTLiquidazione;
	}
	
	public List<SiacTLiquidazioneFin> ricercaDistiniteLiquidazioniByRLiquidazioneMovgest(List<SiacRLiquidazioneMovgestFin> listaInput) {
		List<SiacTLiquidazioneFin> listaRitorno = new ArrayList<SiacTLiquidazioneFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacRLiquidazioneMovgestFin>> esploso = StringUtilsFin.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacRLiquidazioneMovgestFin> listaIt : esploso){
					List<SiacTLiquidazioneFin> risultatoParziale = ricercaDistiniteLiquidazioniByRLiquidazioneMovgestCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtil.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacTLiquidazioneFin> ricercaDistiniteLiquidazioniByRLiquidazioneMovgestCORE(List<SiacRLiquidazioneMovgestFin> listaSiacRLiquidazioneMovgestFin){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTLiquidazioneFin> listaRitorno = new ArrayList<SiacTLiquidazioneFin>();
		
		if(listaSiacRLiquidazioneMovgestFin!=null && listaSiacRLiquidazioneMovgestFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT liq FROM SiacTLiquidazioneFin liq WHERE ");
			
			jpql.append(" liq.liqId IN ( ");
			int i =0;
			for(SiacRLiquidazioneMovgestFin it: listaSiacRLiquidazioneMovgestFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getSiacTLiquidazione().getLiqId());
				i++;
			}
			jpql.append(" ) ");
			
			//SOLO VALIDI
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("liq"));
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public <ST extends SiacTBase>  List<ST> ricercaByLiquidazioneMassive(List<SiacTLiquidazioneFin> listaInput, String nomeEntity) {
		List<ST> listaRitorno = new ArrayList<ST>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTLiquidazioneFin>> esploso = StringUtilsFin.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTLiquidazioneFin> listaIt : esploso){
					List<ST> risultatoParziale = ricercaByLiquidazioneMassiveCORE(listaIt, nomeEntity);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtil.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	/**
	 * Versione speculare di ricercaByLiquidazioneMassive verso le enetita' di bil (per particolare esigenze di ottimizzazione)
	 * @param listaInput
	 * @param nomeEntity
	 * @return
	 */
	public <ST extends it.csi.siac.siaccommonser.integration.entity.SiacTBase>  List<ST> ricercaByLiquidazioneBilMassive(List<SiacTLiquidazioneFin> listaInput, String nomeEntity) {
		List<ST> listaRitorno = new ArrayList<ST>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTLiquidazioneFin>> esploso = StringUtilsFin.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTLiquidazioneFin> listaIt : esploso){
					List<ST> risultatoParziale = ricercaByLiquidazioneBilMassiveCORE(listaIt, nomeEntity);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtil.ritornaSoloDistintiBilByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTLiquidazioneFin e il nome della Entity da cerca vengono restituiti TUTTI I DISTINTI 
	 * oggetti del tipo indicato in nomeEntity in relazione con i SiacTLiquidazioneFin indicati.
	 * ESEMPIO: Se indico nomeEntity = "SiacRLiquidazioneSoggettoFin" verranno restituiti tutti i distinti record SiacRLiquidazioneSoggettoFin
	 * in relazione con i record di listaSiacTLiquidazione indicati
	 */
	@SuppressWarnings("unchecked")
	private <ST extends SiacTBase>  List<ST> ricercaByLiquidazioneMassiveCORE(List<SiacTLiquidazioneFin> listaSiacTLiquidazione, String nomeEntity) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<ST> listaRitorno = new ArrayList<ST>();
		
		if(listaSiacTLiquidazione!=null && listaSiacTLiquidazione.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM "+nomeEntity+" rs WHERE ");
			
			jpql.append(" rs.siacTLiquidazione.liqId IN ( ");
			int i =0;
			for(SiacTLiquidazioneFin it: listaSiacTLiquidazione){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getLiqId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rs"));
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	/**
	 *Versione specualare di ricercaByLiquidazioneMassiveCORE verso le entity mappate in bil
	 */
	@SuppressWarnings("unchecked")
	private <ST extends it.csi.siac.siaccommonser.integration.entity.SiacTBase>  List<ST> ricercaByLiquidazioneBilMassiveCORE(List<SiacTLiquidazioneFin> listaSiacTLiquidazione, String nomeEntity) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<ST> listaRitorno = new ArrayList<ST>();
		
		if(listaSiacTLiquidazione!=null && listaSiacTLiquidazione.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM "+nomeEntity+" rs WHERE ");
			
			jpql.append(" rs.siacTLiquidazione.liqId IN ( ");
			int i =0;
			for(SiacTLiquidazioneFin it: listaSiacTLiquidazione){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getLiqId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rs"));
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	
	
	@Override
	public List<RicercaEstesaLiquidazioniDto> ricercaEstesaLiquidazioni(Integer annoEsercizio,AttoAmministrativo atto, Integer idEnte) {

		List<RicercaEstesaLiquidazioniDto> liquidazioniEstratte = new ArrayList<RicercaEstesaLiquidazioniDto>();
		StringBuffer sql = new StringBuffer();
		//"-- L I Q U I D A Z I O N I  -- "
		sql.append("select distinct a.anno annoEsercizio, ");
		//"-- ATTO "
		sql.append("p.attoamm_anno annoAtto, p.attoamm_numero numAtto, ");
		//"-- CREDITORE "
		sql.append("c.soggetto_code||'-'||c.soggetto_desc creditore , ");
		//"-- LIQUIDAZIONE "
		sql.append("li.liq_anno, li.liq_numero,  ");
		//"-- IMPEGNO "
		sql.append("m.movgest_anno aimp, m.movgest_numero nimp,md.movgest_ts_code nsub, ");
		//"-- CAPITOLO "
		sql.append("cap.elem_code elem_code, cap.elem_code2 elem_code2, ");
		sql.append("li.liq_importo Importo, stato.liq_stato_desc descStato, ");
		sql.append("pt.attoamm_tipo_code, tc.classif_code, ct.classif_tipo_code ");
		sql.append("from siac_t_atto_amm p  ");
		sql.append(" left outer join siac_r_atto_amm_class rac on p.attoamm_id = rac.attoamm_id "); 
		sql.append(" left outer join siac_t_class tc on rac.classif_id = tc.classif_id ");
		sql.append(" left outer join siac_d_class_tipo ct on tc.classif_tipo_id = ct.classif_tipo_id,"); 
		sql.append("siac_d_atto_amm_tipo pt,");
		sql.append("siac_r_liquidazione_atto_amm l, ");
		sql.append("siac_t_soggetto c, siac_r_liquidazione_soggetto ls, ");
		sql.append("siac_d_liquidazione_stato stato, siac_r_liquidazione_stato rlstato,");
		sql.append("siac_t_liquidazione li, ");
		sql.append("siac_r_liquidazione_movgest lm, siac_t_movgest_ts md, siac_t_movgest m, ");
		sql.append("siac_r_movgest_bil_elem ic, siac_t_bil_elem cap, siac_t_bil b, siac_t_periodo a ");
		sql.append("where  ");
		//"-- JOIN "
		sql.append("p.attoamm_id = l.attoamm_id and l.liq_id = li.liq_id ");
		sql.append("and p.attoamm_tipo_id = pt.attoamm_tipo_id ");
		
		sql.append("and c.soggetto_id = ls.soggetto_id and ls.liq_id = li.liq_id ");
		sql.append("and li.liq_id = lm.liq_id and lm.movgest_ts_id = md.movgest_ts_id  ");
		sql.append("and md.movgest_id = m.movgest_id ");
		sql.append("and ic.movgest_id = m.movgest_id and ic.elem_id = cap.elem_id ");
		sql.append("and cap.bil_id = b.bil_id ");
		sql.append("and a.periodo_id = b.periodo_id ");
		sql.append("and l.liq_id =rlstato.liq_id and rlstato.liq_stato_id = stato.liq_stato_id ");
		//"-- NON ANNULLATI ");
		sql.append("and l.validita_fine is null  ");
		sql.append("and p.validita_fine is null  ");
		sql.append("and rac.validita_fine is null ");
		sql.append("and c.validita_fine is null and ls.validita_fine is null ");
		sql.append("and li.validita_fine is null ");
		sql.append("and lm.validita_fine is null and m.validita_fine is null ");
		sql.append("and md.validita_fine is null ");
		sql.append("and ic.validita_fine is null and cap.validita_fine is null ");
		sql.append("and l.data_cancellazione is null ");
		sql.append("and p.data_cancellazione is null  ");
		sql.append("and rac.data_cancellazione is null  ");
		sql.append("and c.data_cancellazione is null and ls.data_cancellazione is null ");
		sql.append("and li.data_cancellazione is null ");
		sql.append("and lm.data_cancellazione is null and m.data_cancellazione is null ");
		sql.append("and md.data_cancellazione is null ");
		sql.append("and ic.data_cancellazione is null and cap.data_cancellazione is null ");
		sql.append("and stato.data_cancellazione is null and rlstato.data_cancellazione is null ");
		//"-- CONDIZIONI DA PARAMETRIZZARE "
		//"--          ente proprietario "
		sql.append("and li.ente_proprietario_id = :idEnte  ");
		//"--          PROVVEDIMENTO ");
		//sql.append("and p.attoamm_anno = ? and p.attoamm_numero = ? ");
		sql.append("and l.attoamm_id = :idAtto ");
		
		// -- l'anno esercizio non è obbigatorio
		if(annoEsercizio!=null){
			//sql.append("and a.anno = :annoEsercizio ");
			sql.append("and a.anno <= :annoEsercizio ");
		}
		
		log.debug("ricercaEstesaLiquidazioni", sql.toString());

		Query query = entityManager.createNativeQuery(sql.toString());
		
		query.setParameter("idEnte", idEnte);	
		query.setParameter("idAtto", atto.getUid());
		
		if(annoEsercizio!=null){
			query.setParameter("annoEsercizio", String.valueOf(annoEsercizio));
		}
		@SuppressWarnings("unchecked")
		List<Object[]> result = (List<Object[]>) query.getResultList();  
  

		if (!result.isEmpty()) {

			for (Object[] lp : result) {
				RicercaEstesaLiquidazioniDto liquidazioneDto = new RicercaEstesaLiquidazioniDto();
		
				liquidazioneDto.setAnnoEsercizio(Integer.parseInt((String) lp[0])); // su t_periodo l'anno è un varchar
				liquidazioneDto.setAnnoAtto(Integer.parseInt((String) lp[1])); // su t_atto_amm l'anno è varchar
				liquidazioneDto.setNumeroAtto((Integer) lp[2]); // su t_atto_amm il numero è integer
				liquidazioneDto.setCreditore((String) lp[3]); 
				liquidazioneDto.setAnnoLiquidazione((Integer) lp[4]); // su t_ordinativo il numero è numerico
				liquidazioneDto.setNumeroLiquidazione(((BigDecimal) lp[5]).intValue());
				
				liquidazioneDto.setAnnoImpegno((Integer) lp[6]);
				liquidazioneDto.setNumeroImpegno(((BigDecimal) lp[7]).intValue());
				
				String codiceSubI =  (String)lp[8];
				if(!codiceSubI.equals(liquidazioneDto.getNumeroImpegno().toString()))
					liquidazioneDto.setCodiceSubImpegno(codiceSubI);	
				else liquidazioneDto.setCodiceSubImpegno("");
				
				liquidazioneDto.setNumeroCapitolo(Integer.parseInt((String) lp[9]));
				liquidazioneDto.setNumeroArticolo(Integer.parseInt((String) lp[10]));
		
				liquidazioneDto.setImportoLiquidazione((BigDecimal) lp[11]);
				
				liquidazioneDto.setDescrizioneStato((String) lp[12]);
				liquidazioneDto.setCodiceTipoAtto((String) lp[13]);
				liquidazioneDto.setCodiceSacAtto((String) lp[14]);
				liquidazioneDto.setCodiceTipoSacAtto((String) lp[15]);
				
				liquidazioniEstratte.add(liquidazioneDto);
		
			}
		}
			
		return liquidazioniEstratte;

	}

}
