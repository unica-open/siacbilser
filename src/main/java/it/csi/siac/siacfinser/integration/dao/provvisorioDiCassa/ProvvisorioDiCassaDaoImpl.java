/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.provvisorioDiCassa;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacTProvCassaRepository;
import it.csi.siac.siacfinser.integration.entity.SiacTProvCassaFin;
import it.csi.siac.siacfinser.integration.util.DataValiditaUtil;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;


@Component
@Transactional
public class ProvvisorioDiCassaDaoImpl extends AbstractDao<SiacTProvCassaFin, Integer> implements ProvvisorioDiCassaDao {

	@Autowired
	SiacTProvCassaRepository siacTProvCassaRepository;

	/**
	 * E' il metodo di "engine" di ricerca dei provvisori.
	 * Utilizzato sia per avere un'anteprima del numero di risultati attesi (rispetto al filtro indicato)
	 * sia per recuperare tutti i dati (rispetto al filtro indicato)
	 */
	private Query creaQueryRicercaProvvisoriDiCassaCore(Integer enteUid, ParametroRicercaProvvisorio prp,List<String> lricPa, boolean soloCount, boolean soloIds) {
		
		Map<String,Object> param = new HashMap<String, Object>();
		Query query = null;
		
		String str = "";
		if(soloCount){
			str = "Select count(*) FROM SiacTProvCassaFin provvisorio";
		} else {
			if(soloIds){
				str = "Select provvisorio.provcId FROM SiacTProvCassaFin provvisorio";
			} else {
				str = "Select provvisorio FROM SiacTProvCassaFin provvisorio";
			}
		}

		StringBuilder jpql = new StringBuilder(str);
		
		// struttura amministrativa
		if(prp.getIdStrutturaAmministrativa()!=null && prp.getIdStrutturaAmministrativa().intValue() != 0){
			jpql.append(" , SiacRProvCassaClassFin relazioneConStruttAmm ");
		}
		
		//WHERE
		jpql.append(" WHERE provvisorio.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId");
		param.put("enteProprietarioId", enteUid);
		
//		jpql.append(" AND (provvisorio.dataFineValidita IS NULL OR :dataInput < provvisorio.dataFineValidita) AND provvisorio.dataCancellazione IS NULL");
//		param.put("dataInput", now);
		
		Date nowDate = TimingUtils.getNowDate();
		param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
		jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("provvisorio"));
		
		//WHERE Provvisorio
		if(prp.getTipoProvvisorio()!=null){
			jpql.append(" AND provvisorio.siacDProvCassaTipo.provcTipoCode = :tipoCode");
			param.put("tipoCode", prp.getTipoProvvisorio().toString());
		}
		
		if(prp.getAnno()!=null && prp.getAnno()!=0){
			jpql.append(" AND provvisorio.provcAnno = :annoProv");
			param.put("annoProv", prp.getAnno());
		}
		
		if(prp.getAnnoDa()!=null && prp.getAnnoDa()!=0){
			jpql.append(" AND provvisorio.provcAnno >= :annoDaProv");
			param.put("annoDaProv", prp.getAnnoDa());				
		}
		
		if(prp.getAnnoA()!=null && prp.getAnnoA()!=0){
			jpql.append(" AND provvisorio.provcAnno <= :annoAProv");
			param.put("annoAProv", prp.getAnnoA());				
		}
		
		if(prp.getNumero()!=null && prp.getNumero()!=0){
			jpql.append(" AND provvisorio.provcNumero = :numeroProv");
			param.put("numeroProv", new BigDecimal(prp.getNumero()));
		}
		
		if(prp.getNumeroDa()!=null && prp.getNumeroDa()!=0){
			jpql.append(" AND provvisorio.provcNumero >= :numeroDaProv");
			param.put("numeroDaProv", new BigDecimal(prp.getNumeroDa()));
		}
		
		if(prp.getNumeroA()!=null && prp.getNumeroA()!=0){
			jpql.append(" AND provvisorio.provcNumero <= :numeroAProv");
			param.put("numeroAProv", new BigDecimal(prp.getNumeroA()));
		}
		
		if(!StringUtilsFin.isEmpty(prp.getDescCausale())){
			jpql.append(" AND UPPER(provvisorio.provcCausale) LIKE UPPER(CONCAT('%', :descCausale, '%'))");
			String descCausaleLike = prp.getDescCausale();//buildLikeString(prp.getDescCausale());
			param.put("descCausale", descCausaleLike);
		}
		
		if(!StringUtilsFin.isEmpty(prp.getSubCausale())){
			jpql.append(" AND UPPER(provvisorio.provcSubcausale) LIKE UPPER(CONCAT('%', :subCausale, '%'))");//LIKE UPPER (:subCausale)");
			String descSubCausaleLike = prp.getSubCausale();// buildLikeString(prp.getSubCausale());
			param.put("subCausale", descSubCausaleLike);
		}
		
		if(!StringUtilsFin.isEmpty(prp.getContoTesoriere())){
			jpql.append(" AND UPPER(provvisorio.provcCodiceContoEvidenza)=UPPER(:contoTesoriere)");
			param.put("contoTesoriere", prp.getContoTesoriere());
		}
		
		if(!StringUtilsFin.isEmpty(prp.getDenominazioneSoggetto())){
			jpql.append(" AND UPPER(provvisorio.provcDenomSoggetto) LIKE UPPER(CONCAT('%', :soggProvv, '%'))"); //LIKE UPPER (:soggProvv)");
			String descSoggLike = prp.getDenominazioneSoggetto();//buildLikeString(prp.getDenominazioneSoggetto());
			param.put("soggProvv", descSoggLike);
		}
		// ANTO
		if(Boolean.TRUE.equals(prp.getFlagProvvisoriPagoPA()) && lricPa != null && !lricPa.isEmpty()){
			String or = "";
			jpql.append(" AND ( ");
			int i = 0;
			for(String par :lricPa){					
				//jpql.append(Utility.toJpqlSearchLike("provvisorio.causale", "CONCAT(:par" + i + ", '%')"));
				jpql.append(or);
				//SIAC-7563 
				//per il pagoPA cerco solo quei provvisori che INIZIANO con i parametri presi da SiacTRicercaCausaliPagopaFin
				jpql.append(Utility.toJpqlSearchLike("provvisorio.provcCausale", "CONCAT(:par_" + i + ", '%')"));				
				param.put("par_" + i, par);
				or = " OR ";
				i++;
			}
			jpql.append(" )");			
		}
		
		
		if(prp.getDataInizioEmissione()!=null && prp.getDataFineEmissione()!=null)
		{
			jpql.append(" AND provvisorio.provcDataEmissione >= :dataInizioEmissione");
			jpql.append(" AND provvisorio.provcDataEmissione <= :dataFineEmissione");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(prp.getDataInizioEmissione());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp dataInizioEmissione = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(prp.getDataFineEmissione());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp dataFineEmissione = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataInizioEmissione", dataInizioEmissione);
			param.put("dataFineEmissione", dataFineEmissione);
		}
		
		
		else if(prp.getDataInizioEmissione()!=null){
			jpql.append(" AND provvisorio.provcDataEmissione >= :dataInizioEmissione AND provvisorio.provcDataEmissione <= :dataFineEmissione");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(prp.getDataInizioEmissione());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp dataInizioEmissione = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(prp.getDataInizioEmissione());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp dataFineEmissione = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataInizioEmissione", dataInizioEmissione);
			param.put("dataFineEmissione", dataFineEmissione);
		}
		else if(prp.getDataFineEmissione()!=null){
			jpql.append(" AND provvisorio.provcDataEmissione >= :dataInizioEmissione AND provvisorio.provcDataEmissione <= :dataFineEmissione");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(prp.getDataFineEmissione());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp dataInizioEmissione = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(prp.getDataFineEmissione());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp dataFineEmissione = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataInizioEmissione", dataInizioEmissione);
			param.put("dataFineEmissione", dataFineEmissione);
		}
		
		
		
		
		
		
		if(prp.getDataInizioTrasmissione()!=null && prp.getDataFineTrasmissione()!=null)
		{
			jpql.append(" AND provvisorio.provcDataTrasmissione >= :dataInizioTrasmissione");
			jpql.append(" AND provvisorio.provcDataTrasmissione <= :dataFineTrasmissione");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(prp.getDataInizioTrasmissione());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp dataInizioTrasmissione = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(prp.getDataFineTrasmissione());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp dataFineTrasmissione = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataInizioTrasmissione", dataInizioTrasmissione);
			param.put("dataFineTrasmissione", dataFineTrasmissione);
		}
		
		
		else if(prp.getDataInizioTrasmissione()!=null){
			jpql.append(" AND provvisorio.provcDataTrasmissione >= :dataInizioTrasmissione AND provvisorio.provcDataTrasmissione <= :dataFineTrasmissione");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(prp.getDataInizioTrasmissione());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp dataInizioTrasmissione = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(prp.getDataInizioTrasmissione());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp dataFineTrasmissione = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataInizioTrasmissione", dataInizioTrasmissione);
			param.put("dataFineTrasmissione", dataFineTrasmissione);
		}
		else if(prp.getDataFineTrasmissione()!=null){
			jpql.append(" AND provvisorio.provcDataTrasmissione >= :dataInizioTrasmissione AND provvisorio.provcDataTrasmissione <= :dataFineTrasmissione");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(prp.getDataFineTrasmissione());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp dataInizioTrasmissione = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(prp.getDataFineTrasmissione());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp dataFineTrasmissione = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataInizioTrasmissione", dataInizioTrasmissione);
			param.put("dataFineTrasmissione", dataFineTrasmissione);
		}
		

		if(prp.getDataInizioInvioServizio() != null) {
			jpql.append(" AND DATE_TRUNC('day', provvisorio.provcDataInvioServizio) >= DATE_TRUNC('day', CAST(:dataInizioInvioServizio AS date)) ");
			param.put("dataInizioInvioServizio", prp.getDataInizioInvioServizio());
		}

		if(prp.getDataFineInvioServizio() != null) {
			jpql.append(" AND DATE_TRUNC('day', provvisorio.provcDataInvioServizio) <= DATE_TRUNC('day', CAST(:dataFineInvioServizio AS date)) ");
			param.put("dataFineInvioServizio", prp.getDataFineInvioServizio());
		}
		
		if(prp.getDataInizioPresaInCaricoServizio() != null) {
			jpql.append(" AND DATE_TRUNC('day', provvisorio.provcDataPresaInCaricoServizio) >= DATE_TRUNC('day', CAST(:dataInizioPresaInCaricoServizio AS date)) ");
			param.put("dataInizioPresaInCaricoServizio", prp.getDataInizioPresaInCaricoServizio());
		}

		if(prp.getDataFinePresaInCaricoServizio() != null) {
			jpql.append(" AND DATE_TRUNC('day', provvisorio.provcDataPresaInCaricoServizio) <= DATE_TRUNC('day', CAST(:dataFinePresaInCaricoServizio AS date)) ");
			param.put("dataFinePresaInCaricoServizio", prp.getDataFinePresaInCaricoServizio());
		}
		
		if(prp.getDataInizioRifiutoErrataAttribuzione() != null) {
			jpql.append(" AND DATE_TRUNC('day', provvisorio.provcDataRifiutoErrataAttribuzione) >= DATE_TRUNC('day', CAST(:dataInizioRifiutoErrataAttribuzione AS date)) ");
			param.put("dataInizioRifiutoErrataAttribuzione", prp.getDataInizioRifiutoErrataAttribuzione());
		}

		if(prp.getDataFineRifiutoErrataAttribuzione() != null) {
			jpql.append(" AND DATE_TRUNC('day', provvisorio.provcDataRifiutoErrataAttribuzione) <= DATE_TRUNC('day', CAST(:dataFineRifiutoErrataAttribuzione AS date)) ");
			param.put("dataFineRifiutoErrataAttribuzione", prp.getDataFineRifiutoErrataAttribuzione());
		}
		
		
		
		
		
		/*
		if(prp.getFlagDaRegolarizzare()!=null){
			//Da regolarizzare: no relazioni con SubDoc- PreDoc- Ord
			if(prp.getFlagDaRegolarizzare().equalsIgnoreCase(CostantiFin.TRUE)){										
				
				//Da gestire in AND anche con SiacRPredocProvCassaFin e SiacRSubdocProvCassaFin
				jpql.append(" AND provvisorio.provcId NOT IN (SELECT sropc.siacTProvCassa.provcId FROM SiacROrdinativoProvCassaFin sropc WHERE "
						+ "((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL))");
				
				param.put("dataInput", now);										
			}else {
				//Da gestire in OR anche con SiacRPredocProvCassaFin e SiacRSubdocProvCassaFin
				jpql.append(" AND provvisorio.provcId IN (SELECT sropc.siacTProvCassa.provcId FROM SiacROrdinativoProvCassaFin sropc WHERE "
						+ "((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL))");
				
				param.put("dataInput", now);										
			}
			
		}*/
		
		if(prp.getFlagAnnullato()!=null && !StringUtilsFin.isEmpty(prp.getFlagAnnullato())){
			if(prp.getFlagAnnullato().equalsIgnoreCase(CostantiFin.FALSE)){
				jpql.append(" AND provvisorio.provcDataAnnullamento IS NULL");					
			}
			
			if(prp.getFlagAnnullato().equalsIgnoreCase(CostantiFin.TRUE)){
				jpql.append(" AND provvisorio.provcDataAnnullamento IS NOT NULL");					
			}
		}
		
		
		if (prp.getFlagAccettato() != null) {
				jpql.append(" AND provvisorio.accettato IS " + (prp.getFlagAccettato() ? "TRUE" : "FALSE") + " ");					
		}
		
		
		//IMPORTO:
		if(prp.getImportoDa()!=null){
			jpql.append(" AND provvisorio.provcImporto >= :importoDa");
			param.put("importoDa", prp.getImportoDa());
		}
		if(prp.getImportoA()!=null){
			jpql.append(" AND provvisorio.provcImporto <= :importoA");
			param.put("importoA", prp.getImportoA());
		}
		
		
		//STRUTTURA AMMINISTRATIVA:
		if(prp.getIdStrutturaAmministrativa()!=null && prp.getIdStrutturaAmministrativa().intValue() != 0){
			jpql.append(" AND relazioneConStruttAmm.siacTProvCassaFin.provcId = provvisorio.provcId ");
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("relazioneConStruttAmm"));
			jpql.append(" AND relazioneConStruttAmm.siacTClass.classifId = :uidStrutturaAmmProvvedimento ");
			param.put("uidStrutturaAmmProvvedimento", prp.getIdStrutturaAmministrativa());
		}
		//
		//
		if(Boolean.TRUE.equals(prp.getEscludiProvvisoriConImportoDaEmettereZero())) {
			jpql.append(" AND (provvisorio.provcImporto <> ");
			jpql.append(" ( SELECT COALESCE(SUM(collegati.ordProvcImporto), 0) ");
			jpql.append("  FROM SiacROrdinativoProvCassaFin collegati ");
			//quando l'ordinativo viene annullato viene imposta la data cancellazione sul record
			jpql.append("  WHERE collegati.dataCancellazione IS NULL ");
			jpql.append("  AND collegati.siacTProvCassa = provvisorio");
			jpql.append(" ) )");
		}
		
		
		if(!soloCount){
			jpql.append(" ORDER BY provvisorio.provcNumero");
		} 
		
		query = createQuery(jpql.toString(), param);

		//Termino restituendo l'oggetto di ritorno: 
        return query;
	}
	
	private Query creaQueryRicercaProvvisoriDiCassaCoreByIDs(List<SiacTProvCassaFin> lista, boolean soloCount) {
		
		Map<String,Object> param = new HashMap<String, Object>();
		Query query = null;
		
		String str = "";
		if(soloCount){
			str = "Select count(*) FROM SiacTProvCassaFin provvisorio";
		} else {
			str = "Select provvisorio FROM SiacTProvCassaFin provvisorio";
		}

		StringBuilder jpql = new StringBuilder(str);
		
		if(lista!=null && lista.size()>0){
			//WHERE
			jpql.append(" WHERE provvisorio.provcId IN ( ");
			
			String clausolaIn = "";
			int i=0;
			for(SiacTProvCassaFin idIt : lista){
				if(i>0){
					clausolaIn = clausolaIn + " , ";
				}
				String idParamNameIt = "provcId" + i;
				clausolaIn = clausolaIn + ":" + idParamNameIt;
				param.put(idParamNameIt, idIt.getProvcId());
				i++;
			}
			
			jpql.append(clausolaIn + " ) ");
			
		} else {
			//WHERE NON TORNA NULLA
			jpql.append(" WHERE 1 != 1 ");
		}
		
		if(!soloCount){
			jpql.append(" ORDER BY provvisorio.provcNumero");
		} 
		
		query = createQuery(jpql.toString(), param);

		//Termino restituendo l'oggetto di ritorno: 
        return query;
	}
	
	/**
	 * Wrapper di creaQueryRicercaProvvisoriDiCassa per avere un'anteprima del numero atteso di risultati
	 */
	public List<Integer> ricercaProvvisoriDiCassaSoloIds(Integer enteUid, ParametroRicercaProvvisorio prp, List<String> lricPa){
		List<Integer>  idList = new ArrayList<Integer>();
		
		boolean soloIds = true;
		boolean soloCount = false;
		Query query = creaQueryRicercaProvvisoriDiCassaCore(enteUid, prp, lricPa,soloCount,soloIds);
		
		List<Integer> idsDaQueryPrincipale = ( List<Integer> )query.getResultList();
		
		//POST FILTRO SU REGOLARIZZARE TRAMITE FUNCION MASSIVE:
		List<Integer> idsRegolarizzare = null;
		if(!StringUtilsFin.isEmpty(prp.getFlagDaRegolarizzare())){
			if(prp.getFlagDaRegolarizzare().equalsIgnoreCase(CostantiFin.TRUE)){	
				idsRegolarizzare = elencoProvvisoriIdsDaRegolarizzare(enteUid);
			} else if(prp.getFlagDaRegolarizzare().equalsIgnoreCase(CostantiFin.FALSE)){
				idsRegolarizzare = elencoProvvisoriIdsRegolarizzati(enteUid);
			}
			idList = CommonUtil.soloQuelliInEntrambe(idsDaQueryPrincipale, idsRegolarizzare);
		}else {
			//NO FILTRO
			idList = idsDaQueryPrincipale;
		}
		
		//Termino restituendo l'oggetto di ritorno: 
		return idList;
	}

	@Override
	public List<Integer> ricercaProvvisoriDiCassaSoloIds(Integer enteUid, ParametroRicercaProvvisorio prp) {
		return ricercaProvvisoriDiCassaSoloIds( enteUid,  prp,  null);
	}
	
	/**
	 * Solo per test 
	 */
	@Deprecated
	@Override
	public List<Integer> ricercaProvvisoriDiCassaSoloIdsOldNoFunction(Integer enteUid, ParametroRicercaProvvisorio prp) {
		List<Integer>  idList = new ArrayList<Integer>();
		
		boolean soloIds = true;
		boolean soloCount = false;
		Query query = creaQueryRicercaProvvisoriDiCassaCore(enteUid, prp, null,soloCount,soloIds);
		
		List<Integer> idsDaQueryPrincipale = ( List<Integer> )query.getResultList();
		
		//POST FILTRO SU REGOLARIZZARE: 
		if(!StringUtilsFin.isEmpty(prp.getFlagDaRegolarizzare())){
			if(idsDaQueryPrincipale!=null && idsDaQueryPrincipale.size()>0){
				for(Integer it : idsDaQueryPrincipale){
					if(it!=null){
						BigDecimal importoIT = calcolaImportoDaRegolarizzare(it);
						
						if(prp.getFlagDaRegolarizzare().equalsIgnoreCase(CostantiFin.TRUE)){	
							if(importoIT!=null && importoIT.longValue()!=0){
								//DA REGOLARIZZARE SI
								idList.add(it);
							}
						} else if(prp.getFlagDaRegolarizzare().equalsIgnoreCase(CostantiFin.FALSE)){	
							if(importoIT==null || importoIT.longValue()==0){
								//DA REGOLARIZZARE NO
								idList.add(it);
							}
						}
						
					}
				}
			}
		} else {
			//NO FILTRO
			idList = idsDaQueryPrincipale;
		}
		
		//Termino restituendo l'oggetto di ritorno: 
		return idList;
	}


	/**
	 * Carica un SiacTProvCassaFin per chiave
	 */
	@Override
	public SiacTProvCassaFin ricercaProvvisorioDiCassaPerChiave(Integer codiceEnte, Integer anno, Integer numero, Timestamp now, String TipoProvvisorio) {
		SiacTProvCassaFin siacTProvCassa = null;
		siacTProvCassa = siacTProvCassaRepository.findProvvisorioDiCassaValidoByAnnoNumero(codiceEnte, anno, BigDecimal.valueOf(numero), now, TipoProvvisorio);
		//Termino restituendo l'oggetto di ritorno: 
        return siacTProvCassa;
	}
	
	
	/**
	 * calcola l'importo da regolarizzare per un provvisorio di cassa
	 * tramite richiamo a function su database
	 * @param idMovGestTs
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal calcolaImportoDaRegolarizzare(Integer provcId){
		BigDecimal result  = new  BigDecimal(0);
		String methodName ="calcolaImportoDaRegolarizzare";
		String functionName="fnc_siac_daregolarizzareprovvisorio ";
		try{
			Query query = entityManager.createNativeQuery("SELECT "+ functionName  + "(:provcId)");
			query.setParameter("provcId", provcId);		
			result = (BigDecimal) query.getSingleResult();
			log.debug(methodName, "Returning result: "+ result + " for provcId: "+ provcId + " and functionName: "+ functionName);
		}catch(NoResultException nre){
			return BigDecimal.ZERO;
		} 
		return result;
	}

	public List<Integer> elencoProvvisoriIdsDaRegolarizzare(Integer idEnte){
		List<Integer>  result  = null;
		String methodName ="elencoProvvisoriIdDaRegolarizzare";
		String functionName="fnc_siac_daregolarizzareprovvisorio_ente";
		try{
			Query query = entityManager.createNativeQuery("SELECT * from "+ functionName  + "(:idEnte) a where a.number_out >0 ");
			query.setParameter("idEnte", idEnte);	
			List<Object[]> queryResult = (List<Object[]>) query.getResultList();
			result = getDistintiIdProvvisoriDaQueryResult(queryResult);
		}catch(NoResultException nre){
			return null;
		} 
		return result;
	}
	
	public List<Integer> elencoProvvisoriIdsRegolarizzati(Integer idEnte){
		List<Integer>  result  = null;
		String methodName ="elencoProvvisoriIdDaRegolarizzare";
		String functionName="fnc_siac_daregolarizzareprovvisorio_ente";
		try{
			Query query = entityManager.createNativeQuery("SELECT * from "+ functionName  + "(:idEnte) a where a.number_out =0 ");
			query.setParameter("idEnte", idEnte);		
			List<Object[]> queryResult = (List<Object[]>) query.getResultList();
			result = getDistintiIdProvvisoriDaQueryResult(queryResult);
		}catch(NoResultException nre){
			return null;
		} 
		return result;
	}
	
	
	private List<Integer> getDistintiIdProvvisoriDaQueryResult(List<Object[]> queryResult){
		List<Integer>  result  = null;
		if(queryResult!=null && queryResult.size()>0){
			result = new ArrayList<Integer>();
			for(Object[] it : queryResult){
				if(it!=null && it[0]!=null){
					Integer idToAdd = (Integer) it[0];
					if(!result.contains(idToAdd)){
						result.add(idToAdd);
					}
				}
			}
			result = StringUtilsFin.getElementiNonNulli(result);
		}
		return result;
	}


	
	
}
