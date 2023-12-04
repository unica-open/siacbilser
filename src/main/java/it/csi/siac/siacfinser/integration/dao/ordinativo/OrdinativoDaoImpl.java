/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaPageableDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaEstesaOrdinativiDiPagamentoDto;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneOrdFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTsDetFin;
import it.csi.siac.siacfinser.integration.util.DataValiditaUtil;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtil;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoIncassoK;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoK;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;


@Component
@Transactional
public class OrdinativoDaoImpl extends AbstractDao<SiacTOrdinativoFin, Integer> implements OrdinativoDao {

	
	@Autowired
	SiacTOrdinativoRepository siacTOrdinativoRepository;
	

	/**
	 * Wrapper di creaQueryRicercaOrdinativiPagamento per ottenere tutti i dati completi per la ricerca indicata
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTOrdinativoFin> ricercaOrdinativiPagamento(Integer enteUid, ParametroRicercaOrdinativoPagamento prop, int numeroPagina, int numeroRisultatiPerPagina) {

		List<SiacTOrdinativoFin> lista = new ArrayList<SiacTOrdinativoFin>();
		
		Query query = creaQueryRicercaOrdinativiPagamento(enteUid, prop, false,null);
		
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
	 * Wrapper di creaQueryRicercaSubOrdinativiPagamento per ottenere tutti i dati completi per la ricerca indicata
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTOrdinativoTFin> ricercaSubOrdinativiPagamento(Integer enteUid, ParametroRicercaSubOrdinativoPagamento prop, int numeroPagina, int numeroRisultatiPerPagina) {
		List<SiacTOrdinativoTFin> lista = new ArrayList<SiacTOrdinativoTFin>();
		Pageable pageable = buildPageable(numeroPagina, numeroRisultatiPerPagina);
		EsitoRicercaPageableDto esitoRicerca = creaQueryRicercaSubOrdinativiPagamento(enteUid, prop,pageable,false);
		Page<SiacTOrdinativoTFin> paged = esitoRicerca.getEntitiesList();
		if(paged!=null){
			lista = paged.getContent();	
		}
		//Termino restituendo l'oggetto di ritorno: 
        return lista;
	}
	
	@Override
	public BigDecimal totImportiIncasso(Integer enteUid, ParametroRicercaOrdinativoIncasso proi, int numeroPagina, int numeroRisultatiPerPagina) {
		Query queryAllIds = creaQueryRicercaOrdinativiIncasso(enteUid, proi, true,true);
		List<Integer> ordIds = (List<Integer>) queryAllIds.getResultList();
		BigDecimal totImporti = calcolatotImportiOrdinativi(ordIds);
		//Termino restituendo l'oggetto di ritorno: 
        return totImporti;	
	}

	@Override
	public BigDecimal totImportiIncasso(Integer enteUid, ParametroRicercaSubOrdinativoIncasso proi, int numeroPagina, int numeroRisultatiPerPagina) {
		EsitoRicercaPageableDto esitoRicerca = creaQueryRicercaSubOrdinativiIncasso(enteUid,proi, buildPageableAllRecords(), true);
		List<Integer> ordIds = esitoRicerca.getSoloIdsList().getContent();
		BigDecimal totImporti = calcolatotImportiSubOrdinativi(ordIds);
		//Termino restituendo l'oggetto di ritorno: 
        return totImporti;	
	}
	
	@Override
	public BigDecimal totImportiPagamento(Integer enteUid, ParametroRicercaOrdinativoPagamento prop, int numeroPagina, int numeroRisultatiPerPagina) {
		Query queryAllIds = creaQueryRicercaOrdinativiPagamento(enteUid, prop, true,true);
		List<Integer> ordIds = (List<Integer>) queryAllIds.getResultList();
		BigDecimal totImporti = calcolatotImportiOrdinativi(ordIds);
		//Termino restituendo l'oggetto di ritorno: 
        return totImporti;	
	}
	
	@Override
	public BigDecimal totImportiPagamento(Integer enteUid, ParametroRicercaSubOrdinativoPagamento prop, int numeroPagina, int numeroRisultatiPerPagina) {
		EsitoRicercaPageableDto esitoRicerca = creaQueryRicercaSubOrdinativiPagamento(enteUid,prop, buildPageableAllRecords(), true);
		List<Integer> ordIds = esitoRicerca.getSoloIdsList().getContent();
		BigDecimal totImporti = calcolatotImportiSubOrdinativi(ordIds);
		//Termino restituendo l'oggetto di ritorno: 
        return totImporti;	
	}

	/**
	 * Wrapper di creaQueryRicercaOrdinativiIncasso per ottenere tutti i dati completi per la ricerca indicata
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTOrdinativoFin> ricercaOrdinativiIncasso(Integer enteUid, ParametroRicercaOrdinativoIncasso proi, int numeroPagina, int numeroRisultatiPerPagina) {

		List<SiacTOrdinativoFin> lista = new ArrayList<SiacTOrdinativoFin>();
		
		Query query = creaQueryRicercaOrdinativiIncasso(enteUid, proi, false,null);
		
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
	 * Wrapper di creaQueryRicercaSubOrdinativiPagamento per ottenere tutti i dati completi per la ricerca indicata
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTOrdinativoTFin> ricercaSubOrdinativiIncasso(Integer enteUid, ParametroRicercaSubOrdinativoIncasso prop, int numeroPagina, int numeroRisultatiPerPagina) {
        List<SiacTOrdinativoTFin> lista = new ArrayList<SiacTOrdinativoTFin>();
		Pageable pageable = buildPageable(numeroPagina, numeroRisultatiPerPagina);
		EsitoRicercaPageableDto esitoRicerca = creaQueryRicercaSubOrdinativiIncasso(enteUid, prop,pageable,false);
		Page<SiacTOrdinativoTFin> paged = esitoRicerca.getEntitiesList();
		if(paged!=null){
			lista = paged.getContent();	
		}
		//Termino restituendo l'oggetto di ritorno: 
        return lista;
	}

	/**
	 * E' il metodo di "engine" di ricerca degli ordinativi di pagamento.
	 * Utilizzato sia per avere un'anteprima del numero di risultati attesi (rispetto al filtro indicato)
	 * sia per recuperare tutti i dati (rispetto al filtro indicato)
	 */
	@Override
	public Query creaQueryRicercaOrdinativiPagamento(Integer enteUid, ParametroRicercaOrdinativoPagamento prop, boolean soloCount, Boolean richiestiIds) {

		Map<String,Object> param = new HashMap<String, Object>();
		Query query = null;
			
		Integer annoEsercizio=prop.getAnnoEsercizio();
					
		String str = "";
		
		if(richiestiIds!=null && richiestiIds){
			//RICHIESTI GLI IDS:
			str = "Select distinct ordinativo.ordId FROM SiacTOrdinativoFin ordinativo";
		} else {
			//COMPORTAMENTO STANDARD
			str = "Select distinct ordinativo FROM SiacTOrdinativoFin ordinativo";
		}
		
		
		StringBuilder jpql = new StringBuilder(str);
		
		boolean indicatoImpegno = (prop.getNumeroImpegno()!= null && prop.getNumeroImpegno().intValue() != 0)||
				(prop.getAnnoImpegno() != null && prop.getAnnoImpegno().intValue() != 0);
		
		
		//atto amministrativo!
		if((null!=prop.getAnnoProvvedimento() && prop.getAnnoProvvedimento().intValue()!=0  
			&&  null!=prop.getNumeroProvvedimento() && prop.getNumeroProvvedimento().intValue()!=0) 
			|| 
			(null!=prop.getAnnoProvvedimento() && prop.getAnnoProvvedimento().intValue()!=0  
			&& null!=prop.getCodiceTipoProvvedimento() && !StringUtilsFin.isEmpty(prop.getCodiceTipoProvvedimento()))
			||
			prop.getUidProvvedimento()!=null && prop.getUidProvvedimento()!=0){
			
			jpql.append(", SiacROrdinativoAttoAmmFin rOrdinativoAttoAmm");
		
			// struttura amministrativa
			//if(prop.getUidStrutturaAmministrativoContabile()!=null && prop.getUidStrutturaAmministrativoContabile()!=0){
				jpql.append(" left join rOrdinativoAttoAmm.siacTAttoAmm.siacRAttoAmmClasses rAttoClass left join rAttoClass.siacTClass tClassAtto ");
			//}
					
				
		}
		
		//CASI IN CUI SI AGGIUNGE LA RELAZIONE VERSO LO STATO:
		if( prop.getDataTrasmissioneOIL()!=null
			|| !StringUtilsFin.isEmpty(prop.getStatoOperativo())
			|| !StringUtilsFin.isEmpty(prop.getStatiDaEscludere())){
			jpql.append(", SiacROrdinativoStatoFin rOrdinativoStato");
		}
		//
		
		// FROM impegno-mutuo
		// jira 2750, errore di sintassi:
		// lo stesso alias sulla liquidazone viene usato sia nel caso ci sia il movimento gestione
		// sia se inserisco la liquidazione (nei paramentri di ricerca)
		Boolean aliasRLiquidazioneOrdPresente = false;
		if(indicatoImpegno){				
			jpql.append(", SiacRLiquidazioneOrdFin rLiquidazioneOrd");
			jpql.append(", SiacRLiquidazioneMovgestFin rLiquidazioneMovgest");
			
			aliasRLiquidazioneOrdPresente = true;
			
		}

		//FROM capitolo	//jira 1327 - ricerca capitolo per 0
		if (prop.getNumeroCapitolo()!=null ||
				prop.getNumeroArticolo()!=null ||
				prop.getNumeroUEB()!=null) {				
			jpql.append(", SiacROrdinativoBilElemFin rOrdinativoBilElem");	
		}
		
		//FROM Liquidazione
		if((prop.getNumeroLiquidazione()!=null && prop.getNumeroLiquidazione().intValue()!=0) ||
				(prop.getAnnoLiquidazione()!=null && prop.getAnnoLiquidazione().intValue()!=0)){
			if(!aliasRLiquidazioneOrdPresente)
				jpql.append(", SiacRLiquidazioneOrdFin rLiquidazioneOrd");
		}

		//FROM Provvisorio cassa
		if((prop.getNumeroProvvCassa()!=null && prop.getNumeroProvvCassa().intValue()!=0) ||
				(prop.getAnnoProvvCassa()!=null && prop.getAnnoProvvCassa().intValue()!=0)){
			jpql.append(", SiacROrdinativoProvCassaFin rOrdinativoProvCassa");
		}

		//FROM soggetto
		if(!StringUtilsFin.isEmpty(prop.getCodiceCreditore())){
			jpql.append(", SiacROrdinativoSoggettoFin rOrdinativoSoggetto");
		}
		
		//SOGGETTO CESSIONE INCASSO:
		if(!StringUtilsFin.isEmpty(prop.getCodiceCreditoreCessioneIncasso())){
			jpql.append(", SiacROrdinativoModpagFin rOrdinativoModpagFin left join rOrdinativoModpagFin.cessioneId rSoggettoRelazFin");
		}
		
		//WHERE
		jpql.append(" WHERE ordinativo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId");
		param.put("enteProprietarioId", enteUid);
		
		jpql.append(" AND ordinativo.siacTBil.siacTPeriodo.anno = :annoEsercizio");
		param.put("annoEsercizio", annoEsercizio.toString());
		
		jpql.append(" AND ordinativo.siacDOrdinativoTipo.ordTipoCode = :codeTipoOrdinativo");
		param.put("codeTipoOrdinativo", CostantiFin.D_ORDINATIVO_TIPO_PAGAMENTO);
		
		//DESCRIZIONE LIKE:
		if(!StringUtilsFin.isEmpty(prop.getDescrizione())){
			buildClausolaLikeGenerico(jpql,"ordinativo.ordDesc", prop.getDescrizione(), "likeDescrizione", param);
		}
		
		// SIAC-6175
		if (prop.getDaTrasmettere() != null) {
			jpql.append(" AND ordinativo.ordDaTrasmettere=" + (prop.getDaTrasmettere() == 1 ? "TRUE" : "FALSE") + " ");
		}
		
		//DATA VALIDITA:
		Date nowDate = TimingUtils.getNowDate();
		if(prop.getDataTrasmissioneOIL()==null){
			//solo se non c'e questo filtro per non interferire
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("ordinativo"));
		}
		
		//WHERE Mandato
		if(prop.getNumeroOrdinativoDa()!=null && prop.getNumeroOrdinativoA()!=null){
			jpql.append(" AND ordinativo.ordNumero >= :numeroOrdinativoDa");
			jpql.append(" AND ordinativo.ordNumero <= :numeroOrdinativoA");
			param.put("numeroOrdinativoDa", new BigDecimal(prop.getNumeroOrdinativoDa()));
			param.put("numeroOrdinativoA", new BigDecimal(prop.getNumeroOrdinativoA()));
		}
		else if(prop.getNumeroOrdinativoDa()!=null){
			jpql.append(" AND ordinativo.ordNumero = :numeroOrdinativoDa");
			param.put("numeroOrdinativoDa", new BigDecimal(prop.getNumeroOrdinativoDa()));
		}
		else if(prop.getNumeroOrdinativoA()!=null){
			jpql.append(" AND ordinativo.ordNumero = :numeroOrdinativoA");
			param.put("numeroOrdinativoA", new BigDecimal(prop.getNumeroOrdinativoA()));
		}
		
		
		
		if(prop.getDataEmissioneDa()!=null && prop.getDataEmissioneA()!=null){
			jpql.append(" AND ordinativo.ordEmissioneData >= :dataEmissioneDa");
			jpql.append(" AND ordinativo.ordEmissioneData <= :dataEmissioneA");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(prop.getDataEmissioneDa());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp dataEmissioneDa = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(prop.getDataEmissioneA());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp dataEmissioneA = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataEmissioneDa", dataEmissioneDa);
			param.put("dataEmissioneA", dataEmissioneA);
			
		}else if(prop.getDataEmissioneDa()!=null) {
			
			jpql.append(" AND ordinativo.ordEmissioneData >= :dataEmissioneDaFrom AND ordinativo.ordEmissioneData <= :dataEmissioneDaTo");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(prop.getDataEmissioneDa());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp from = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(prop.getDataEmissioneDa());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp to = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataEmissioneDaFrom", from);
			param.put("dataEmissioneDaTo", to);
		}
		else if(prop.getDataEmissioneA()!=null){
			jpql.append(" AND ordinativo.ordEmissioneData >= :dataEmissioneAFrom AND ordinativo.ordEmissioneData <= :dataEmissioneATo");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(prop.getDataEmissioneA());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp from = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(prop.getDataEmissioneA());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp to = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataEmissioneAFrom", from);
			param.put("dataEmissioneATo", to);
		}
		
		//MARCO - da verficare stato operativo
		if(!StringUtilsFin.isEmpty(prop.getStatoOperativo()) || !StringUtilsFin.isEmpty(prop.getStatiDaEscludere()) ){
			
			jpql.append(" AND ordinativo.ordId = rOrdinativoStato.siacTOrdinativo.ordId");
			
			if(!StringUtilsFin.isEmpty(prop.getStatoOperativo())){
				//APRILE 2018, SIAC-6098, ottimizzazione query:
				if(indicatoImpegno){
					//indicando l'impegno sembra piu' efficiente usare questa:
					jpql.append(" AND EXISTS ( ");
					jpql.append(" SELECT sdos1 FROM SiacDOrdinativoStatoFin sdos1, SiacROrdinativoStatoFin sros1 ");
					jpql.append(" WHERE sdos1.ordStatoId = sros1.siacDOrdinativoStato.ordStatoId ");
					jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("sros1"));
					jpql.append(" AND sdos1.ordStatoCode = :statoOperativo ");
					jpql.append(" AND sros1.siacDOrdinativoStato.ordStatoId = rOrdinativoStato.siacDOrdinativoStato.ordStatoId ");
					jpql.append(" ) ");
				} else {
					//mentre senza la condizione sul'impegno sembra meglio cosi:
					jpql.append(" AND rOrdinativoStato.siacDOrdinativoStato.ordStatoCode = :statoOperativo");
				}
				param.put("statoOperativo", prop.getStatoOperativo());
			}
			if(!StringUtilsFin.isEmpty(prop.getStatiDaEscludere()) ){
				//APRILE 2018, SIAC-6098, ottimizzazione query:
				if(indicatoImpegno){
					//indicando l'impegno sembra piu' efficiente usare questa:
					jpql.append(" AND EXISTS ( ");
					jpql.append(" SELECT sdos FROM SiacDOrdinativoStatoFin sdos, SiacROrdinativoStatoFin sros ");
					jpql.append(" WHERE sdos.ordStatoId = sros.siacDOrdinativoStato.ordStatoId ");
					jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("sros"));
					jpql.append(" AND sdos.ordStatoCode NOT IN :statiDaEscludere ");
					jpql.append(" AND sros.siacDOrdinativoStato.ordStatoId = rOrdinativoStato.siacDOrdinativoStato.ordStatoId ");
					jpql.append(" ) ");
				} else {
					//mentre senza la condizione sul'impegno sembra meglio cosi:
					jpql.append(" AND rOrdinativoStato.siacDOrdinativoStato.ordStatoCode NOT IN :statiDaEscludere");
				}
				param.put("statiDaEscludere", prop.getStatiDaEscludere());
			}
			
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rOrdinativoStato"));
		}
		
		
		//MARCO - Implementare filtro Data trasmissione OIL
		if(prop.getDataTrasmissioneOIL()!=null){
			jpql.append(" AND ordinativo.dataInizioValidita >= :dataTrasmissioneOILFrom");
			jpql.append(" AND ordinativo.dataInizioValidita <= :dataTrasmissioneOILTo");
			jpql.append(" AND ordinativo.ordId = rOrdinativoStato.siacTOrdinativo.ordId");
			jpql.append(" AND rOrdinativoStato.siacDOrdinativoStato.ordStatoDesc = 'TRASMESSO'");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(prop.getDataTrasmissioneOIL());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp from = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(prop.getDataTrasmissioneOIL());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp to = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataTrasmissioneOILFrom", from);
			param.put("dataTrasmissioneOILTo", to);

		}
		
		//Distinta
		if(!StringUtilsFin.isEmpty(prop.getCodiceDistinta())){
			jpql.append(" AND ordinativo.siacDDistinta.distCode = :codiceDistinta");				

			param.put("codiceDistinta", prop.getCodiceDistinta());
		}
		
		//Conto del tesoriere
		if(!StringUtilsFin.isEmpty(prop.getContoDelTesoriere())){
			jpql.append(" AND ordinativo.siacDContotesoreria.contotesCode = :codiceContoDelTesoriere");				

			param.put("codiceContoDelTesoriere", prop.getContoDelTesoriere());
		}
		
		
		//WHERE impegno-mutuo
		if(indicatoImpegno){
							
			jpql.append(" AND rLiquidazioneOrd.siacTOrdinativoT.siacTOrdinativo.ordId = ordinativo.ordId");	
			jpql.append(" AND rLiquidazioneOrd.siacTLiquidazione.liqId = rLiquidazioneMovgest.siacTLiquidazione.liqId");
							
			//correttiva anomalia 1132
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rLiquidazioneOrd.siacTOrdinativoT"));
			
			// SIAC-5561
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rLiquidazioneOrd"));

			//impegno
			if(prop.getNumeroImpegno()!= null && prop.getNumeroImpegno().intValue() != 0){
				if (prop.getNumeroSubImpegno()!=null) {

					jpql.append(" AND rLiquidazioneMovgest.siacTMovgestT.movgestTsCode=  :numeroSubImpegno");
					jpql.append(" AND rLiquidazioneMovgest.siacTMovgestT.siacTMovgest.movgestNumero=  :numeroImpegno");

					param.put("numeroSubImpegno", prop.getNumeroSubImpegno().toString());
					param.put("numeroImpegno", prop.getNumeroImpegno());

				} else {
					jpql.append(" AND rLiquidazioneMovgest.siacTMovgestT.siacTMovgest.movgestNumero = :numeroImpegno");
					param.put("numeroImpegno", prop.getNumeroImpegno());

				}
				
				
			}

			if(prop.getAnnoImpegno() != null && prop.getAnnoImpegno().intValue() != 0){
				jpql.append(" AND rLiquidazioneMovgest.siacTMovgestT.siacTMovgest.movgestAnno = :annoImpegno ");
				param.put("annoImpegno",prop.getAnnoImpegno());
			}

			
			jpql.append(" AND (rLiquidazioneMovgest.dataFineValidita IS NULL AND rLiquidazioneMovgest.dataCancellazione IS NULL) ");
		}

		//Capitolo
		if (prop.getNumeroCapitolo()!=null ||
				prop.getNumeroArticolo()!=null ||
				prop.getNumeroUEB()!=null) {				
			jpql.append(" AND rOrdinativoBilElem.siacTOrdinativo.ordId = ordinativo.ordId");
			jpql.append(" AND rOrdinativoBilElem.dataFineValidita IS NULL AND rOrdinativoBilElem.dataCancellazione IS NULL ");
			if(prop.getNumeroCapitolo()!=null){
				jpql.append(" AND rOrdinativoBilElem.siacTBilElem.elemCode = :numeroCapitolo");
				param.put("numeroCapitolo", prop.getNumeroCapitolo().toString());
			}
			if(prop.getNumeroArticolo()!=null){
				jpql.append(" AND rOrdinativoBilElem.siacTBilElem.elemCode2 = :numeroArticolo");
				param.put("numeroArticolo", prop.getNumeroArticolo().toString());
			}						
			if(prop.getNumeroUEB()!=null){
				jpql.append(" AND rOrdinativoBilElem.siacTBilElem.elemCode3 = :numeroUEB");
				param.put("numeroUEB", prop.getNumeroUEB().toString());
			}
			if(prop.getAnnoCapitolo()!=null){
				jpql.append(" AND rOrdinativoBilElem.siacTBilElem.siacTBil.siacTPeriodo.anno = :annoCapitolo");				
				param.put("annoCapitolo", prop.getAnnoCapitolo().toString());
			}
			
		}
		
		
		//WHERE Liquidazione
		if((prop.getNumeroLiquidazione()!=null && prop.getNumeroLiquidazione().intValue()!=0) ||
				(prop.getAnnoLiquidazione()!=null && prop.getAnnoLiquidazione().intValue()!=0)){
			jpql.append(" AND rLiquidazioneOrd.siacTOrdinativoT.siacTOrdinativo.ordId = ordinativo.ordId");	
			
			if(prop.getNumeroLiquidazione()!=null && prop.getNumeroLiquidazione().intValue()!=0){
				jpql.append(" AND rLiquidazioneOrd.siacTLiquidazione.liqNumero = :numeroLiquidazione");
				param.put("numeroLiquidazione", new BigDecimal(prop.getNumeroLiquidazione()));
			}			
			if(prop.getAnnoLiquidazione()!=null && prop.getAnnoLiquidazione().intValue()!=0){
				jpql.append(" AND rLiquidazioneOrd.siacTLiquidazione.liqAnno = :annoLiquidazione");
				param.put("annoLiquidazione", prop.getAnnoLiquidazione());	
			}
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rLiquidazioneOrd"));
		}
		
		//WHERE Provvisorio Cassa
		if((prop.getNumeroProvvCassa()!=null && prop.getNumeroProvvCassa().intValue()!=0) ||
				(prop.getAnnoProvvCassa()!=null && prop.getAnnoProvvCassa().intValue()!=0)){
			jpql.append(" AND rOrdinativoProvCassa.siacTOrdinativo.ordId = ordinativo.ordId");	
			
			if(prop.getNumeroProvvCassa()!=null && prop.getNumeroProvvCassa().intValue()!=0){
				jpql.append(" AND rOrdinativoProvCassa.siacTProvCassa.provcNumero = :numeroProvvCassa");
				param.put("numeroProvvCassa", prop.getNumeroProvvCassa());
			}			
			if(prop.getAnnoProvvCassa()!=null && prop.getAnnoProvvCassa().intValue()!=0){
				jpql.append(" AND rOrdinativoProvCassa.siacTProvCassa.provcAnno = :annoProvvCassa");
				param.put("annoProvvCassa", prop.getAnnoProvvCassa());	
			}
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rOrdinativoProvCassa"));
		}
		
		
		
		//Provvedimento: se arriva l'uid vado direttamente a cercare per quello
		if(prop.getUidProvvedimento() != null && prop.getUidProvvedimento() != 0){
				jpql.append(" AND rOrdinativoAttoAmm.siacTOrdinativo.ordId = ordinativo.ordId");
				jpql.append(" AND (rOrdinativoAttoAmm.dataFineValidita IS NULL or rOrdinativoAttoAmm.dataCancellazione IS NULL)");
				jpql.append(" AND rOrdinativoAttoAmm.siacTAttoAmm.attoammId = :attoId ");
				jpql.append(" AND (rAttoClass.dataFineValidita IS NULL or rAttoClass.dataCancellazione IS NULL) ");
				param.put("attoId", prop.getUidProvvedimento());
		} else{
			
			//WHERE AttoAmministrativo
			if((null!=prop.getAnnoProvvedimento() && prop.getAnnoProvvedimento().intValue()!=0  
					&& null!=prop.getNumeroProvvedimento() && prop.getNumeroProvvedimento().intValue()!=0) || 
					(null!=prop.getAnnoProvvedimento() && prop.getAnnoProvvedimento().intValue()!=0  
					&& null!=prop.getCodiceTipoProvvedimento() && !StringUtilsFin.isEmpty(prop.getCodiceTipoProvvedimento()))){
				
				jpql.append(" AND rOrdinativoAttoAmm.siacTOrdinativo.ordId = ordinativo.ordId");
				jpql.append(" AND (rOrdinativoAttoAmm.dataFineValidita IS NULL or rOrdinativoAttoAmm.dataCancellazione IS NULL)");
				
				if(null!=prop.getAnnoProvvedimento()){
					jpql.append(" AND rOrdinativoAttoAmm.siacTAttoAmm.attoammAnno = :annoProvvedimento");
					param.put("annoProvvedimento", prop.getAnnoProvvedimento().toString());
				}
					
				if(null!=prop.getAnnoProvvedimento() && null!=prop.getNumeroProvvedimento()){
					jpql.append(" AND rOrdinativoAttoAmm.siacTAttoAmm.attoammNumero = :numeroProvvedimento");
					param.put("numeroProvvedimento", Integer.valueOf(prop.getNumeroProvvedimento().intValue()));
				}
				
				if(null!=prop.getAnnoProvvedimento() && null!=prop.getCodiceTipoProvvedimento()) {
					jpql.append(" AND rOrdinativoAttoAmm.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId = :codiceTipoProvvedimento");
					param.put("codiceTipoProvvedimento", Integer.valueOf(prop.getCodiceTipoProvvedimento()));
				}
				
				if(prop.getUidStrutturaAmministrativoContabile()!=null && prop.getUidStrutturaAmministrativoContabile()!=0) {
					jpql.append(" AND tClassAtto.classifId = :uidSac");
					jpql.append(" AND (rAttoClass.dataFineValidita IS NULL or rAttoClass.dataCancellazione IS NULL) ");
					param.put("uidSac", prop.getUidStrutturaAmministrativoContabile());
				}
			}
		}
		
		//WHERE soggetto
		if(!StringUtilsFin.isEmpty(prop.getCodiceCreditore())){
			jpql.append(" AND rOrdinativoSoggetto.siacTOrdinativo.ordId = ordinativo.ordId");
			jpql.append(" AND rOrdinativoSoggetto.dataFineValidita IS NULL");
			jpql.append(" AND rOrdinativoSoggetto.siacTSoggetto.soggettoCode = :codiceCreditore");
			param.put("codiceCreditore", prop.getCodiceCreditore()); 
		} 
		
		//soggetto cessione incasso
		if(!StringUtilsFin.isEmpty(prop.getCodiceCreditoreCessioneIncasso())){
			jpql.append(" AND  rOrdinativoModpagFin.siacTOrdinativo.ordId = ordinativo.ordId " );
			jpql.append(" AND rSoggettoRelazFin.siacTSoggetto2.soggettoCode = :codiceCredCessIncasso");
			param.put("codiceCredCessIncasso", prop.getCodiceCreditoreCessioneIncasso()); 
			
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rOrdinativoModpagFin"));
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rSoggettoRelazFin"));
		}

		if(soloCount==false){
			jpql.append(" ORDER BY ordinativo.ordNumero");
		} 
		
		query = createQuery(jpql.toString(), param);			

		//Termino restituendo l'oggetto di ritorno: 
        return query;
	}
	
	/**
	 * E' il metodo di "engine" di ricerca degli ordinativi di pagamento.
	 * Utilizzato sia per avere un'anteprima del numero di risultati attesi (rispetto al filtro indicato)
	 * sia per recuperare tutti i dati (rispetto al filtro indicato)
	 */
	@Override
	public EsitoRicercaPageableDto creaQueryRicercaSubOrdinativiPagamento(Integer enteUid, ParametroRicercaSubOrdinativoPagamento prop, Pageable pageable, boolean soloIds){
		
		EsitoRicercaPageableDto esito = new EsitoRicercaPageableDto();
		
		Date nowDate = TimingUtils.getNowDate();
		
		Map<String,Object> param = new HashMap<String, Object>();
		Query query = null;
			
		Integer annoEsercizio=prop.getAnnoEsercizio();
					
		String str = "";
		
		if(soloIds){
			//RICHIESTI SOLO IL COUNT:
			str = "Select subOrdinativo.ordTsId FROM SiacTOrdinativoTFin subOrdinativo, SiacTOrdinativoFin ordinativo";
		} else {
			//COMPORTAMENTO STANDARD
			str = "Select subOrdinativo FROM SiacTOrdinativoTFin subOrdinativo, SiacTOrdinativoFin ordinativo";
		}
		
		
		StringBuilder jpql = new StringBuilder(str);

		//FROM Provvisorio cassa
		if((prop.getNumeroProvvCassa()!=null && prop.getNumeroProvvCassa().intValue()!=0) ||
				(prop.getAnnoProvvCassa()!=null && prop.getAnnoProvvCassa().intValue()!=0)){
			jpql.append(", SiacROrdinativoProvCassaFin rOrdinativoProvCassa");
		}
		
		//WHERE
		
		jpql.append(" WHERE ordinativo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId");
		param.put("enteProprietarioId", enteUid);
		
		jpql.append(" AND ordinativo.siacTBil.siacTPeriodo.anno = :annoEsercizio");
		param.put("annoEsercizio", annoEsercizio.toString());
		
		jpql.append(" AND ordinativo.siacDOrdinativoTipo.ordTipoCode = :codeTipoOrdinativo");
		param.put("codeTipoOrdinativo", CostantiFin.D_ORDINATIVO_TIPO_PAGAMENTO);
		
		//legame tra ordinativo e sub:
		jpql.append(" AND subOrdinativo.siacTOrdinativo.ordId = ordinativo.ordId");
		//
		
		//WHERE Provvisorio Cassa
		if((prop.getNumeroProvvCassa()!=null && prop.getNumeroProvvCassa().intValue()!=0) ||
				(prop.getAnnoProvvCassa()!=null && prop.getAnnoProvvCassa().intValue()!=0)){
			jpql.append(" AND rOrdinativoProvCassa.siacTOrdinativo.ordId = ordinativo.ordId");	
			
			if(prop.getNumeroProvvCassa()!=null && prop.getNumeroProvvCassa().intValue()!=0){
				jpql.append(" AND rOrdinativoProvCassa.siacTProvCassa.provcNumero = :numeroProvvCassa");
				param.put("numeroProvvCassa", prop.getNumeroProvvCassa());
			}			
			if(prop.getAnnoProvvCassa()!=null && prop.getAnnoProvvCassa().intValue()!=0){
				jpql.append(" AND rOrdinativoProvCassa.siacTProvCassa.provcAnno = :annoProvvCassa");
				param.put("annoProvvCassa", prop.getAnnoProvvCassa());	
			}
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rOrdinativoProvCassa"));
		}

		jpql.append(" ORDER BY ordinativo.ordNumero, subOrdinativo.ordTsCode");
		
		if(soloIds){
			Page<Integer> soloIdsList = getPagedList(jpql.toString(), param, pageable);
			esito.setSoloIdsList(soloIdsList);
		} else{
			Page<SiacTOrdinativoTFin> entitiesList = getPagedList(jpql.toString(), param, pageable);
			esito.setEntitiesList(entitiesList);
		}
		return esito;
	}
	
	/**
	 * E' il metodo di "engine" di ricerca degli ordinativi di incasso.
	 * Utilizzato sia per avere un'anteprima del numero di risultati attesi (rispetto al filtro indicato)
	 * sia per recuperare tutti i dati (rispetto al filtro indicato)
	 */
	@Override
	public EsitoRicercaPageableDto creaQueryRicercaSubOrdinativiIncasso(Integer enteUid, ParametroRicercaSubOrdinativoIncasso prop,Pageable pageable, boolean soloIds) {

		EsitoRicercaPageableDto esito = new EsitoRicercaPageableDto();
		
		Date nowDate = TimingUtils.getNowDate();
		
		Map<String,Object> param = new HashMap<String, Object>();
		Query query = null;
			
		Integer annoEsercizio=prop.getAnnoEsercizio();
					
		String str = "";
	
		if(soloIds){
			//RICHIESTI GLI IDS:
			str = "Select subOrdinativo.ordTsId FROM SiacTOrdinativoTFin subOrdinativo, SiacTOrdinativoFin ordinativo";
		} else {
			//COMPORTAMENTO STANDARD
			str = "Select subOrdinativo FROM SiacTOrdinativoTFin subOrdinativo, SiacTOrdinativoFin ordinativo";
		}
		
		
		StringBuilder jpql = new StringBuilder(str);

		//FROM Provvisorio cassa
		if((prop.getNumeroProvvCassa()!=null && prop.getNumeroProvvCassa().intValue()!=0) ||
				(prop.getAnnoProvvCassa()!=null && prop.getAnnoProvvCassa().intValue()!=0)){
			jpql.append(", SiacROrdinativoProvCassaFin rOrdinativoProvCassa");
		}
		
		//WHERE
		
		jpql.append(" WHERE ordinativo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId");
		param.put("enteProprietarioId", enteUid);
		
		jpql.append(" AND ordinativo.siacTBil.siacTPeriodo.anno = :annoEsercizio");
		param.put("annoEsercizio", annoEsercizio.toString());
		
		jpql.append(" AND ordinativo.siacDOrdinativoTipo.ordTipoCode = :codeTipoOrdinativo");
		param.put("codeTipoOrdinativo", CostantiFin.D_ORDINATIVO_TIPO_INCASSO);
		
		//legame tra ordinativo e sub:
		jpql.append(" AND subOrdinativo.siacTOrdinativo.ordId = ordinativo.ordId");
		//
		
		
		//WHERE Provvisorio Cassa
		if((prop.getNumeroProvvCassa()!=null && prop.getNumeroProvvCassa().intValue()!=0) ||
				(prop.getAnnoProvvCassa()!=null && prop.getAnnoProvvCassa().intValue()!=0)){
			jpql.append(" AND rOrdinativoProvCassa.siacTOrdinativo.ordId = ordinativo.ordId");	
			
			if(prop.getNumeroProvvCassa()!=null && prop.getNumeroProvvCassa().intValue()!=0){
				jpql.append(" AND rOrdinativoProvCassa.siacTProvCassa.provcNumero = :numeroProvvCassa");
				param.put("numeroProvvCassa", prop.getNumeroProvvCassa());
			}			
			if(prop.getAnnoProvvCassa()!=null && prop.getAnnoProvvCassa().intValue()!=0){
				jpql.append(" AND rOrdinativoProvCassa.siacTProvCassa.provcAnno = :annoProvvCassa");
				param.put("annoProvvCassa", prop.getAnnoProvvCassa());	
			}
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rOrdinativoProvCassa"));
		}

		jpql.append(" ORDER BY ordinativo.ordNumero, subOrdinativo.ordTsCode");
		
		if(soloIds){
			Page<Integer> soloIdsList = getPagedList(jpql.toString(), param, pageable);
			esito.setSoloIdsList(soloIdsList);
		} else{
			Page<SiacTOrdinativoTFin> entitiesList = getPagedList(jpql.toString(), param, pageable);
			esito.setEntitiesList(entitiesList);
		}
		
		return esito;
	}
	
	private BigDecimal calcolatotImportiOrdinativi(List<Integer> ordIds) {
		BigDecimal totImporti = BigDecimal.ZERO;
		if(ordIds!=null && ordIds.size()>0){
			List<List<Integer>> esploso = StringUtilsFin.esplodiInListe(ordIds, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<Integer> listaIt : esploso){
					BigDecimal risultatoParziale = calcolatotImportiOrdinativiCORE(listaIt);
					if(risultatoParziale!=null){
						totImporti = totImporti.add(risultatoParziale);
					}
				}
			}
		}
        return totImporti;
	}
	
	private BigDecimal calcolatotImportiSubOrdinativi(List<Integer> ordTIds) {
		BigDecimal totImporti = BigDecimal.ZERO;
		if(ordTIds!=null && ordTIds.size()>0){
			List<List<Integer>> esploso = StringUtilsFin.esplodiInListe(ordTIds, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<Integer> listaIt : esploso){
					BigDecimal risultatoParziale = calcolatotImportiSubOrdinativiCORE(listaIt);
					if(risultatoParziale!=null){
						totImporti = totImporti.add(risultatoParziale);
					}
				}
			}
		}
        return totImporti;
	}
	
	private BigDecimal calcolatotImportiOrdinativiCORE(List<Integer> ordIds) {
		
		BigDecimal totImporti = BigDecimal.ZERO;

		if(ordIds!=null && ordIds.size()>0){
			
			Date nowDate = TimingUtils.getNowDate();
			
			Map<String,Object> param = new HashMap<String, Object>();
			Query query = null;
				
						
			String str = "";
			
			str = "Select SUM (ordTsDet.ordTsDetImporto) "
					+ "FROM SiacTOrdinativoFin ordinativo  " 
					+ " ,SiacTOrdinativoTFin ordinativoT "
					+ " ,SiacTOrdinativoTsDetFin ordTsDet ";
			
			
			StringBuilder jpql = new StringBuilder(str);
			
			//ID ORDINATIVI COINVOLTI:
			//String elencoIds = buildElencoIntegerPerClausolaIN(ordIds);
			jpql.append(" WHERE ordinativo.ordId IN :elencoIds");
			param.put("elencoIds", ordIds);
			//
			
			jpql.append(" AND ordinativoT.siacTOrdinativo.ordId = ordinativo.ordId");
			jpql.append(" AND ordTsDet.siacTOrdinativoT.ordTsId = ordinativoT.ordTsId");
			
			jpql.append(" AND ordTsDet.siacDOrdinativoTsDetTipo.ordTsDetTipoCode = 'A' ");
			
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("ordTsDet"));
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("ordinativoT"));
			
			query = createQuery(jpql.toString(), param);	
			
			totImporti = (BigDecimal) query.getSingleResult();
		}

		//Termino restituendo l'oggetto di ritorno: 
        return totImporti;
	}
	
	private BigDecimal calcolatotImportiSubOrdinativiCORE(List<Integer> ordTIds) {
		
		BigDecimal totImporti = BigDecimal.ZERO;

		if(ordTIds!=null && ordTIds.size()>0){
			
			Date nowDate = TimingUtils.getNowDate();
			
			Map<String,Object> param = new HashMap<String, Object>();
			Query query = null;
				
						
			String str = "";
			
			str = "Select SUM (ordTsDet.ordTsDetImporto) "
					+ "FROM SiacTOrdinativoTFin ordinativoT  " 
					+ " ,SiacTOrdinativoTsDetFin ordTsDet ";
			
			
			StringBuilder jpql = new StringBuilder(str);
			
			//ID ORDINATIVI COINVOLTI:
			//String elencoIds = buildElencoIntegerPerClausolaIN(ordIds);
			jpql.append(" WHERE ordinativoT.ordTsId IN :elencoIds");
			param.put("elencoIds", ordTIds);
			//
			
			jpql.append(" AND ordTsDet.siacTOrdinativoT.ordTsId = ordinativoT.ordTsId");
			
			jpql.append(" AND ordTsDet.siacDOrdinativoTsDetTipo.ordTsDetTipoCode = 'A' ");
			
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("ordTsDet"));
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("ordinativoT"));
			
			query = createQuery(jpql.toString(), param);	
			
			totImporti = (BigDecimal) query.getSingleResult();
		}

		//Termino restituendo l'oggetto di ritorno: 
        return totImporti;
	}
	
	

	/**
	 * E' il metodo di "engine" di ricerca degli ordinativi di incasso.
	 * Utilizzato sia per avere un'anteprima del numero di risultati attesi (rispetto al filtro indicato)
	 * sia per recuperare tutti i dati (rispetto al filtro indicato)
	 */
	@Override
	public Query creaQueryRicercaOrdinativiIncasso(Integer enteUid, ParametroRicercaOrdinativoIncasso parametroRicercaOrdinativoIncasso, boolean soloCount, Boolean richiestiIds) {

		Map<String,Object> param = new HashMap<String, Object>();
		Query query = null;

		Integer annoEsercizio=parametroRicercaOrdinativoIncasso.getAnnoEsercizio();
					
		String str = "";

		if(richiestiIds!=null && richiestiIds){
			//RICHIESTI GLI IDS:
			str = "Select distinct ordinativo.ordId FROM SiacTOrdinativoFin ordinativo";
		} else {
			//COMPORTAMENTO STANDARD
			str = "Select distinct ordinativo FROM SiacTOrdinativoFin ordinativo";
		}
		
		StringBuilder jpql = new StringBuilder(str);
		
		//atto amministrativo!
		if((null!=parametroRicercaOrdinativoIncasso.getAnnoProvvedimento() && parametroRicercaOrdinativoIncasso.getAnnoProvvedimento().intValue()!=0  
			&& null!=parametroRicercaOrdinativoIncasso.getNumeroProvvedimento() && parametroRicercaOrdinativoIncasso.getNumeroProvvedimento().intValue()!=0)
		    || 
		    (null!=parametroRicercaOrdinativoIncasso.getAnnoProvvedimento() && parametroRicercaOrdinativoIncasso.getAnnoProvvedimento().intValue()!=0  
			&& null!=parametroRicercaOrdinativoIncasso.getCodiceTipoProvvedimento() && !StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getCodiceTipoProvvedimento()))				
			|| 
			parametroRicercaOrdinativoIncasso.getUidProvvedimento()!=null && parametroRicercaOrdinativoIncasso.getUidProvvedimento()!=0){
			
			jpql.append(", SiacROrdinativoAttoAmmFin rOrdinativoAttoAmm");
		
			// struttura amministrativa
			if(parametroRicercaOrdinativoIncasso.getUidStrutturaAmministrativoContabile()!=null && parametroRicercaOrdinativoIncasso.getUidStrutturaAmministrativoContabile()!=0){
				jpql.append(" left join rOrdinativoAttoAmm.siacTAttoAmm.siacRAttoAmmClasses rAttoClass left join rAttoClass.siacTClass tClassAtto ");
			}
		}
		
		//CASI IN CUI SI AGGIUNGE LA RELAZIONE VERSO LO STATO:
		if( parametroRicercaOrdinativoIncasso.getDataTrasmissioneOIL()!=null
			|| !StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getStatoOperativo())
			|| !StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getStatiDaEscludere())){
			jpql.append(", SiacROrdinativoStatoFin rOrdinativoStato");
		}
		//

		//FROM Accertamento
		if((parametroRicercaOrdinativoIncasso.getNumeroMovimento()!= null && parametroRicercaOrdinativoIncasso.getNumeroMovimento().intValue() != 0) ||
				(parametroRicercaOrdinativoIncasso.getAnnoMovimento() != null && parametroRicercaOrdinativoIncasso.getAnnoMovimento().intValue() != 0) ||
				(parametroRicercaOrdinativoIncasso.getNumeroSubMovimento() != null && parametroRicercaOrdinativoIncasso.getNumeroSubMovimento().intValue() != 0)){
			jpql.append(", SiacROrdinativoTsMovgestTFin rOrdinativoTsMovgestT");
		}

		//FROM capitolo
		if (parametroRicercaOrdinativoIncasso.getNumeroCapitolo()!=null ||
				parametroRicercaOrdinativoIncasso.getNumeroArticolo()!=null ||
				parametroRicercaOrdinativoIncasso.getNumeroUEB()!=null) {				
			jpql.append(", SiacROrdinativoBilElemFin rOrdinativoBilElem");	
		}


		
		//FROM soggetto
		if(!StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getCodiceCreditore())){
			jpql.append(", SiacROrdinativoSoggettoFin rOrdinativoSoggetto");
		}
		
		
		//FROM Provvisorio cassa
		if((parametroRicercaOrdinativoIncasso.getNumeroProvvCassa()!=null && parametroRicercaOrdinativoIncasso.getNumeroProvvCassa().intValue()!=0) ||
				(parametroRicercaOrdinativoIncasso.getAnnoProvvCassa()!=null && parametroRicercaOrdinativoIncasso.getAnnoProvvCassa().intValue()!=0)){
			jpql.append(", SiacROrdinativoProvCassaFin rOrdinativoProvCassa");
		}
		

		
		
		//WHERE
		jpql.append(" WHERE ordinativo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId");
		param.put("enteProprietarioId", enteUid);
		jpql.append(" AND ordinativo.siacTBil.siacTPeriodo.anno = :annoEsercizio");
		param.put("annoEsercizio", annoEsercizio.toString());
		jpql.append(" AND ordinativo.siacDOrdinativoTipo.ordTipoCode = :codeTipoOrdinativo");
		param.put("codeTipoOrdinativo", CostantiFin.D_ORDINATIVO_TIPO_INCASSO);
		
		
		//DESCRIZIONE LIKE:
		if(!StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getDescrizione())){
			buildClausolaLikeGenerico(jpql,"ordinativo.ordDesc", parametroRicercaOrdinativoIncasso.getDescrizione(), "likeDescrizione", param);
		}
		
		// SIAC-6175
		if (parametroRicercaOrdinativoIncasso.getDaTrasmettere() != null) {
			jpql.append(" AND ordinativo.ordDaTrasmettere=" + (parametroRicercaOrdinativoIncasso.getDaTrasmettere() == 1 ? "TRUE" : "FALSE") + " ");
		}
		

		
		Date nowDate = TimingUtils.getNowDate();
		if(parametroRicercaOrdinativoIncasso.getDataTrasmissioneOIL()==null){
			//solo se non c'e questo filtro per non interferire
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("ordinativo"));
		}
		
		if((parametroRicercaOrdinativoIncasso.getNumeroMovimento()!= null && parametroRicercaOrdinativoIncasso.getNumeroMovimento().intValue() != 0) ||
				(parametroRicercaOrdinativoIncasso.getAnnoMovimento() != null && parametroRicercaOrdinativoIncasso.getAnnoMovimento().intValue() != 0) ||
				(parametroRicercaOrdinativoIncasso.getNumeroSubMovimento() != null && parametroRicercaOrdinativoIncasso.getNumeroSubMovimento().intValue() != 0)){
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rOrdinativoTsMovgestT"));
		}
		
		//CAUSALE:
		if(!StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getCodiceCausale())){
			jpql.append(" AND ordinativo.siacDCausale.causCode = :codiceCausale");
			param.put("codiceCausale", parametroRicercaOrdinativoIncasso.getCodiceCausale());
		}
		
		//WHERE Provvisorio Cassa
		if((parametroRicercaOrdinativoIncasso.getNumeroProvvCassa()!=null && parametroRicercaOrdinativoIncasso.getNumeroProvvCassa().intValue()!=0) ||
				(parametroRicercaOrdinativoIncasso.getAnnoProvvCassa()!=null && parametroRicercaOrdinativoIncasso.getAnnoProvvCassa().intValue()!=0)){
			jpql.append(" AND rOrdinativoProvCassa.siacTOrdinativo.ordId = ordinativo.ordId");	
			
			if(parametroRicercaOrdinativoIncasso.getNumeroProvvCassa()!=null && parametroRicercaOrdinativoIncasso.getNumeroProvvCassa().intValue()!=0){
				jpql.append(" AND rOrdinativoProvCassa.siacTProvCassa.provcNumero = :numeroProvvCassa");
				param.put("numeroProvvCassa", parametroRicercaOrdinativoIncasso.getNumeroProvvCassa());
			}			
			if(parametroRicercaOrdinativoIncasso.getAnnoProvvCassa()!=null && parametroRicercaOrdinativoIncasso.getAnnoProvvCassa().intValue()!=0){
				jpql.append(" AND rOrdinativoProvCassa.siacTProvCassa.provcAnno = :annoProvvCassa");
				param.put("annoProvvCassa", parametroRicercaOrdinativoIncasso.getAnnoProvvCassa());	
			}
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rOrdinativoProvCassa"));
		}
		

		//WHERE Mandato
		if(parametroRicercaOrdinativoIncasso.getNumeroOrdinativoDa()!=null && parametroRicercaOrdinativoIncasso.getNumeroOrdinativoA()!=null){
		
			jpql.append(" AND ordinativo.ordNumero >= :numeroOrdinativoDa");
			jpql.append(" AND ordinativo.ordNumero <= :numeroOrdinativoA");
			param.put("numeroOrdinativoDa", new BigDecimal(parametroRicercaOrdinativoIncasso.getNumeroOrdinativoDa()));
			param.put("numeroOrdinativoA", new BigDecimal(parametroRicercaOrdinativoIncasso.getNumeroOrdinativoA()));
		
		}else if(parametroRicercaOrdinativoIncasso.getNumeroOrdinativoDa()!=null){
			
				jpql.append(" AND ordinativo.ordNumero = :numeroOrdinativoDa");
				param.put("numeroOrdinativoDa", new BigDecimal(parametroRicercaOrdinativoIncasso.getNumeroOrdinativoDa()));
				
		}else if(parametroRicercaOrdinativoIncasso.getNumeroOrdinativoA()!=null){
			
				jpql.append(" AND ordinativo.ordNumero = :numeroOrdinativoA");
				param.put("numeroOrdinativoA", new BigDecimal(parametroRicercaOrdinativoIncasso.getNumeroOrdinativoA()));
		}
			
		if(parametroRicercaOrdinativoIncasso.getDataEmissioneDa()!=null && parametroRicercaOrdinativoIncasso.getDataEmissioneA()!=null){
			jpql.append(" AND ordinativo.ordEmissioneData >= :dataEmissioneDa");
			jpql.append(" AND ordinativo.ordEmissioneData <= :dataEmissioneA");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(parametroRicercaOrdinativoIncasso.getDataEmissioneDa());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp dataEmissioneDa = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(parametroRicercaOrdinativoIncasso.getDataEmissioneA());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp dataEmissioneA = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataEmissioneDa", dataEmissioneDa);
			param.put("dataEmissioneA", dataEmissioneA);
		}
		else if(parametroRicercaOrdinativoIncasso.getDataEmissioneDa()!=null){
			jpql.append(" AND ordinativo.ordEmissioneData >= :dataEmissioneDaFrom AND ordinativo.ordEmissioneData <= :dataEmissioneDaTo");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(parametroRicercaOrdinativoIncasso.getDataEmissioneDa());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp from = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(parametroRicercaOrdinativoIncasso.getDataEmissioneDa());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp to = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataEmissioneDaFrom", from);
			param.put("dataEmissioneDaTo", to);
		}
		else if(parametroRicercaOrdinativoIncasso.getDataEmissioneA()!=null){
			jpql.append(" AND ordinativo.ordEmissioneData >= :dataEmissioneAFrom AND ordinativo.ordEmissioneData <= :dataEmissioneATo");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(parametroRicercaOrdinativoIncasso.getDataEmissioneA());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp from = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(parametroRicercaOrdinativoIncasso.getDataEmissioneA());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp to = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataEmissioneAFrom", from);
			param.put("dataEmissioneATo", to);
		}
			
		//MARCO - da verficare stato operativo
		if(!StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getStatoOperativo()) || !StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getStatiDaEscludere())  ){
			
			jpql.append(" AND ordinativo.ordId = rOrdinativoStato.siacTOrdinativo.ordId");
			
			if(!StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getStatoOperativo())){
				jpql.append(" AND rOrdinativoStato.siacDOrdinativoStato.ordStatoCode = :statoOperativo");
				param.put("statoOperativo", parametroRicercaOrdinativoIncasso.getStatoOperativo());
			}
			if(!StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getStatiDaEscludere()) ){
				String statiDaEscludere = buildElencoPerClausolaIN(parametroRicercaOrdinativoIncasso.getStatiDaEscludere());
				jpql.append(" AND rOrdinativoStato.siacDOrdinativoStato.ordStatoCode NOT IN :statiDaEscludere");
				param.put("statiDaEscludere", statiDaEscludere);
			}
			
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rOrdinativoStato"));
		}
			
		//MARCO - Implementare filtro Data trasmissione OIL
		//MARCO - Cambiare riferimento stato con costante
		if(parametroRicercaOrdinativoIncasso.getDataTrasmissioneOIL()!=null){
			jpql.append(" AND ordinativo.dataInizioValidita >= :dataTrasmissioneOILFrom");
			jpql.append(" AND ordinativo.dataInizioValidita <= :dataTrasmissioneOILTo");
			jpql.append(" AND ordinativo.ordId = rOrdinativoStato.siacTOrdinativo.ordId");
			jpql.append(" AND rOrdinativoStato.siacDOrdinativoStato.ordStatoCode = :statoOperativoTrasmesso");
			
			Calendar cFrom = Calendar.getInstance();
			cFrom.setTime(parametroRicercaOrdinativoIncasso.getDataTrasmissioneOIL());

			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			cFrom.set(Calendar.MILLISECOND, 0);
			Timestamp from = new Timestamp(cFrom.getTime().getTime());
			
			Calendar cTo = Calendar.getInstance();
			cTo.setTime(parametroRicercaOrdinativoIncasso.getDataTrasmissioneOIL());

			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			cTo.set(Calendar.MILLISECOND, 999);
			Timestamp to = new Timestamp(cTo.getTime().getTime());
			
			
			param.put("dataTrasmissioneOILFrom", from);
			param.put("dataTrasmissioneOILTo", to);

			param.put("statoOperativoTrasmesso",CostantiFin.D_ORDINATIVO_STATO_TRASMESSO);
		}
		
		//Distinta
		if(!StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getCodiceDistinta())){
			jpql.append(" AND ordinativo.siacDDistinta.distCode = :codiceDistinta");				

			param.put("codiceDistinta", parametroRicercaOrdinativoIncasso.getCodiceDistinta());
		}
		
		//Conto del tesoriere
		if(!StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getContoDelTesoriere())){
			jpql.append(" AND ordinativo.siacDContotesoreria.contotesCode = :codiceContoDelTesoriere");				

			param.put("codiceContoDelTesoriere", parametroRicercaOrdinativoIncasso.getContoDelTesoriere());
		}

		//WHERE Accertamento
		if((parametroRicercaOrdinativoIncasso.getNumeroMovimento()!= null && parametroRicercaOrdinativoIncasso.getNumeroMovimento().intValue() != 0) ||
				(parametroRicercaOrdinativoIncasso.getAnnoMovimento() != null && parametroRicercaOrdinativoIncasso.getAnnoMovimento().intValue() != 0) ||
				(parametroRicercaOrdinativoIncasso.getNumeroSubMovimento() != null && parametroRicercaOrdinativoIncasso.getNumeroSubMovimento().intValue() != 0)){
			if(parametroRicercaOrdinativoIncasso.getNumeroMovimento()!= null && parametroRicercaOrdinativoIncasso.getNumeroMovimento().intValue() != 0){
				jpql.append(" AND rOrdinativoTsMovgestT.siacTOrdinativoT.siacTOrdinativo.ordId = ordinativo.ordId");	

				if (parametroRicercaOrdinativoIncasso.getNumeroSubMovimento()!=null) {

					jpql.append(" AND rOrdinativoTsMovgestT.siacTMovgestT.movgestTsCode=  :numeroSubAccertamento");
					jpql.append(" AND rOrdinativoTsMovgestT.siacTMovgestT.siacTMovgest.movgestNumero=  :numeroAccertamento");

					param.put("numeroSubAccertamento", parametroRicercaOrdinativoIncasso.getNumeroSubMovimento().toString());
					param.put("numeroAccertamento", BigDecimal.valueOf(parametroRicercaOrdinativoIncasso.getNumeroMovimento()));

				} else {
					jpql.append(" AND rOrdinativoTsMovgestT.siacTMovgestT.siacTMovgest.movgestNumero = :numeroAccertamento");
					param.put("numeroAccertamento", BigDecimal.valueOf(parametroRicercaOrdinativoIncasso.getNumeroMovimento()));

				}
			}

			if(parametroRicercaOrdinativoIncasso.getAnnoMovimento() != null && parametroRicercaOrdinativoIncasso.getAnnoMovimento().intValue() != 0){
				jpql.append(" AND rOrdinativoTsMovgestT.siacTMovgestT.siacTMovgest.movgestAnno = :annoAccertamento ");
				param.put("annoAccertamento", parametroRicercaOrdinativoIncasso.getAnnoMovimento());
			}
		}

		//Capitolo
		if (parametroRicercaOrdinativoIncasso.getNumeroCapitolo()!=null ||
				parametroRicercaOrdinativoIncasso.getNumeroArticolo()!=null ||
				parametroRicercaOrdinativoIncasso.getNumeroUEB()!=null) {				
			jpql.append(" AND rOrdinativoBilElem.siacTOrdinativo.ordId = ordinativo.ordId");
			jpql.append(" AND rOrdinativoBilElem.dataFineValidita IS NULL");
			if(parametroRicercaOrdinativoIncasso.getNumeroCapitolo()!=null){
				jpql.append(" AND rOrdinativoBilElem.siacTBilElem.elemCode = :numeroCapitolo");
				param.put("numeroCapitolo", parametroRicercaOrdinativoIncasso.getNumeroCapitolo().toString());
			}
			if(parametroRicercaOrdinativoIncasso.getNumeroArticolo()!=null){
				jpql.append(" AND rOrdinativoBilElem.siacTBilElem.elemCode2 = :numeroArticolo");
				param.put("numeroArticolo", parametroRicercaOrdinativoIncasso.getNumeroArticolo().toString());
			}						
			if(parametroRicercaOrdinativoIncasso.getNumeroUEB()!=null){
				jpql.append(" AND rOrdinativoBilElem.siacTBilElem.elemCode3 = :numeroUEB");
				param.put("numeroUEB", parametroRicercaOrdinativoIncasso.getNumeroUEB().toString());
			}
			if(parametroRicercaOrdinativoIncasso.getAnnoCapitolo()!=null){
				jpql.append(" AND rOrdinativoBilElem.siacTBilElem.siacTBil.siacTPeriodo.anno = :annoCapitolo");				
				param.put("annoCapitolo", parametroRicercaOrdinativoIncasso.getAnnoCapitolo().toString());
			}
		}
		
		
		//Provvedimento: se arriva l'uid vado direttamente a cercare per quello
		if(parametroRicercaOrdinativoIncasso.getUidProvvedimento() != null && parametroRicercaOrdinativoIncasso.getUidProvvedimento() != 0){
				jpql.append(" AND rOrdinativoAttoAmm.siacTOrdinativo.ordId = ordinativo.ordId");
				jpql.append(" AND (rOrdinativoAttoAmm.dataFineValidita IS NULL or rOrdinativoAttoAmm.dataCancellazione IS NULL)");
				jpql.append(" AND rOrdinativoAttoAmm.siacTAttoAmm.attoammId = :attoId ");
				param.put("attoId", parametroRicercaOrdinativoIncasso.getUidProvvedimento());
		} else{
				
			//WHERE AttoAmministrativo
			if((null!=parametroRicercaOrdinativoIncasso.getAnnoProvvedimento() && parametroRicercaOrdinativoIncasso.getAnnoProvvedimento().intValue()!=0  
					&& null!=parametroRicercaOrdinativoIncasso.getNumeroProvvedimento() && parametroRicercaOrdinativoIncasso.getNumeroProvvedimento().intValue()!=0) || 
					(null!=parametroRicercaOrdinativoIncasso.getAnnoProvvedimento() && parametroRicercaOrdinativoIncasso.getAnnoProvvedimento().intValue()!=0  
					&& null!=parametroRicercaOrdinativoIncasso.getCodiceTipoProvvedimento() && !StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getCodiceTipoProvvedimento()))){
				
				jpql.append(" AND rOrdinativoAttoAmm.siacTOrdinativo.ordId = ordinativo.ordId");
				jpql.append(" AND (rOrdinativoAttoAmm.dataFineValidita IS NULL or rOrdinativoAttoAmm.dataCancellazione IS NULL) ");
				
				if(null!=parametroRicercaOrdinativoIncasso.getAnnoProvvedimento()){
					jpql.append(" AND rOrdinativoAttoAmm.siacTAttoAmm.attoammAnno = :annoProvvedimento");
					param.put("annoProvvedimento", parametroRicercaOrdinativoIncasso.getAnnoProvvedimento().toString());
				}
					
				if(null!=parametroRicercaOrdinativoIncasso.getAnnoProvvedimento() && null!=parametroRicercaOrdinativoIncasso.getNumeroProvvedimento()){
					jpql.append(" AND rOrdinativoAttoAmm.siacTAttoAmm.attoammNumero = :numeroProvvedimento");
					param.put("numeroProvvedimento", Integer.valueOf(parametroRicercaOrdinativoIncasso.getNumeroProvvedimento().intValue()));
				}
				
				if(null!=parametroRicercaOrdinativoIncasso.getAnnoProvvedimento() && null!=parametroRicercaOrdinativoIncasso.getCodiceTipoProvvedimento()) {
					jpql.append(" AND rOrdinativoAttoAmm.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId = :codiceTipoProvvedimento");
					param.put("codiceTipoProvvedimento", Integer.valueOf(parametroRicercaOrdinativoIncasso.getCodiceTipoProvvedimento()));
				}
				
				if(parametroRicercaOrdinativoIncasso.getUidStrutturaAmministrativoContabile()!=null && parametroRicercaOrdinativoIncasso.getUidStrutturaAmministrativoContabile()!=0) {
					jpql.append(" AND tClassAtto.classifId = :uidSac");
					param.put("uidSac", parametroRicercaOrdinativoIncasso.getUidStrutturaAmministrativoContabile());
				}
			}
		}
		
		//WHERE soggetto
		if(!StringUtilsFin.isEmpty(parametroRicercaOrdinativoIncasso.getCodiceCreditore())){
			jpql.append(" AND rOrdinativoSoggetto.siacTOrdinativo.ordId = ordinativo.ordId");
			jpql.append(" AND rOrdinativoSoggetto.dataFineValidita IS NULL");
			jpql.append(" AND rOrdinativoSoggetto.siacTSoggetto.soggettoCode = :codiceCreditore");
			param.put("codiceCreditore", parametroRicercaOrdinativoIncasso.getCodiceCreditore()); 
		}
		
		// SIAC-6585
		if (parametroRicercaOrdinativoIncasso.getFiltraPerCollegaReversali()) {
			jpql.append(filtraPerCollegaReversali());
		}

		if(soloCount==false){
			jpql.append(" ORDER BY ordinativo.ordNumero");
		}
		
		query = createQuery(jpql.toString(), param);			

		//Termino restituendo l'oggetto di ritorno: 
        return query;
	}

	private StringBuilder filtraPerCollegaReversali() {
		// Le reversali visualizzate oltre a soddisfare i criteri di ricerca devono:
		
		return new StringBuilder() 
		// Non essere annullate (tutti gli altri stati vanno bene);
			.append(" AND NOT EXISTS ( ")
			.append(" 	SELECT 1 FROM ordinativo.siacROrdinativoStatos os ")
			.append(" 	  WHERE os.siacDOrdinativoStato.ordStatoCode='A' ")
			.append("     AND os.dataCancellazione IS NULL ")
			.append(" 	  AND os.dataInizioValidita <= CURRENT_TIMESTAMP ")
			.append(" 	  AND (os.dataFineValidita IS NULL OR os.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append(" ) ")
		
		// Non essere a copertura (ossia non presentano provvisori di cassa associati);
			.append(" AND NOT EXISTS ( ")
			.append(" 	SELECT 1 FROM ordinativo.siacROrdinativoProvCassas opc ")
			.append(" 	  WHERE opc.dataCancellazione IS NULL ")
			.append(" 	  AND opc.dataInizioValidita <= CURRENT_TIMESTAMP ")
			.append(" 	  AND (opc.dataFineValidita IS NULL OR opc.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append(" ) ")
			
		// Non devono essere associate a nessun altro ordinativo.
			.append(" AND NOT EXISTS ( ")
			.append(" 	SELECT 1 FROM ordinativo.siacROrdinativos1 o1 ")
			.append(" 	  WHERE o1.dataCancellazione IS NULL ")
			.append(" 	  AND o1.dataInizioValidita <= CURRENT_TIMESTAMP ")
			.append(" 	  AND (o1.dataFineValidita IS NULL OR o1.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append(" ) ")
			.append(" AND NOT EXISTS ( ")
			.append(" 	SELECT 1 FROM ordinativo.siacROrdinativos2 o2 ")
			.append(" 	  WHERE o2.dataCancellazione IS NULL ")
			.append(" 	  AND o2.dataInizioValidita <= CURRENT_TIMESTAMP ")
			.append(" 	  AND (o2.dataFineValidita IS NULL OR o2.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append(" ) ");
	}

	/**
	 * Wrapper di creaQueryRicercaOrdinativiPagamento per avere un'anteprima del numero atteso di risultati
	 */
	@Override
	public Long contaOrdinativiPagamento(Integer enteUid, ParametroRicercaOrdinativoPagamento prop) {
		
		Query query = creaQueryRicercaOrdinativiPagamento(enteUid, prop, true,null);
		       
		// Rm commentato xchè x come è fatta la query in presenza di n subOrdinativi 
     	// il count tiene conto di essi, quindi ricercando un ordinativo restituisce 
     	// il numero di occorenze dei sub
     	// conteggiOrdinativiIncasso = (Long)query.getSingleResult();
     	Integer conteggio = query.getResultList().size();
     	return (conteggio!=0 ? conteggio.longValue(): 0) ;
	}
	
	/**
	 * Wrapper di creaQueryRicercaSubOrdinativiPagamento per avere un'anteprima del numero atteso di risultati
	 */
	@Override
	public Long contaSubOrdinativiPagamento(Integer enteUid, ParametroRicercaSubOrdinativoPagamento prop,int numeroPagina, int numeroRisultatiPerPagina) {
		Pageable pageable = buildPageable(numeroPagina, numeroRisultatiPerPagina);
		EsitoRicercaPageableDto esitoRicerca = creaQueryRicercaSubOrdinativiPagamento(enteUid, prop, pageable,true);
		Page<Integer> paged = esitoRicerca.getSoloIdsList();
		return paged.getTotalElements();
	}
	
	/**
	 * Wrapper di creaQueryRicercaSubOrdinativiPagamento per avere un'anteprima del numero atteso di risultati
	 */
	@Override
	public Long contaSubOrdinativiIncasso(Integer enteUid, ParametroRicercaSubOrdinativoIncasso prop,int numeroPagina, int numeroRisultatiPerPagina) {
		Pageable pageable = buildPageable(numeroPagina, numeroRisultatiPerPagina);
		EsitoRicercaPageableDto esitoRicerca = creaQueryRicercaSubOrdinativiIncasso(enteUid, prop,pageable,true);
		Page<Integer> paged = esitoRicerca.getSoloIdsList();
     	return paged.getTotalElements();
	}

	/**
	 * Wrapper di creaQueryRicercaOrdinativiIncasso per avere un'anteprima del numero atteso di risultati
	 */
	@Override
	public Long contaOrdinativiIncasso(Integer enteUid, ParametroRicercaOrdinativoIncasso proi) {
		
		Query query = creaQueryRicercaOrdinativiIncasso(enteUid, proi, true,null);
		// Rm commentato xchè x come è fatta la query in presenza di n subOrdinativi 
		// il count tiene conto di essi, quindi ricercando un ordinativo restituisce 
		// il numero di occorenze dei sub
		// conteggiOrdinativiIncasso = (Long)query.getSingleResult();
		Integer conteggio = query.getResultList().size();
		//Termino restituendo l'oggetto di ritorno: 
        return (conteggio!=0 ? conteggio.longValue(): 0) ;
	}
	
	/**
	 * Carica un SiacTOrdinativoFin per chiave
	 */
	public SiacTOrdinativoFin ricercaOrdinativo(Integer codiceEnte, RicercaOrdinativoK pk, String codeTipoOrdinativo, Timestamp now) {

		Ordinativo ord;
	    	 
    	 if (CostantiFin.D_ORDINATIVO_TIPO_PAGAMENTO.equals(codeTipoOrdinativo)) {
    		 ord = ((RicercaOrdinativoPagamentoK) pk).getOrdinativoPagamento();
    	 } else if (CostantiFin.D_ORDINATIVO_TIPO_INCASSO.equals(codeTipoOrdinativo)) {
    		 ord = ((RicercaOrdinativoIncassoK) pk).getOrdinativoIncasso();
    	 } else {
    		 throw new IllegalArgumentException("Tipo ordinativo non valido: " + codeTipoOrdinativo);
    	 }
    	 
    	 if (ord.getUid() > 0) {
    		 return siacTOrdinativoRepository.findOne(ord.getUid());
    	 }
       	 //Correzione siac-6646
    	return siacTOrdinativoRepository.findOrdinativoByCodeAndAnno(codiceEnte, ord.getAnno(), Integer.toString(pk.getBilancio().getAnno()), BigDecimal.valueOf(ord.getNumero()), codeTipoOrdinativo); 
    	//return siacTOrdinativoRepository.findOrdinativoByCodeAndAnno(codiceEnte, ord.getAnno(), Integer.toString(ord.getAnnoBilancio()), BigDecimal.valueOf(ord.getNumero()), codeTipoOrdinativo);
	}
	
	

	/**
	 * Query native sql 
	 */
	@Override
	public List<RicercaEstesaOrdinativiDiPagamentoDto> ricercaEstesaOrdinativiDiPagamento(Integer annoEsercizio, AttoAmministrativo atto, 
			Integer idEnte) {

			List<RicercaEstesaOrdinativiDiPagamentoDto> ordinativiEstratti = new ArrayList<RicercaEstesaOrdinativiDiPagamentoDto>();
			  
			StringBuffer sql = new StringBuffer();
			//-- O R D I N A T I V I   di   P A G A M E N T O  -- 
			sql.append("select distinct ");
			sql.append("a.anno anno_Esercizio, ");
			//"-- ATTO "
			sql.append("p.attoamm_anno anno_Atto, p.attoamm_numero num_Atto, ");
			
			//"-- ORDINATIVO numero e STATO ");
			sql.append("o.ord_anno oanno,o.ord_numero num_Ord,q.ord_ts_code sub_Ord, ");
			sql.append("so.ord_stato_desc stato, ");
			//"-- CREDITORE ");
			sql.append("c.soggetto_code||'-'||c.soggetto_desc creditore , ");
			//"-- LIQUIDAZIONE ");
			sql.append("li.liq_anno liq_anno, li.liq_numero liq_numero,  ");
			  //"-- IMPEGNO ");
			sql.append("m.movgest_anno aimp, m.movgest_numero nimp,md.movgest_ts_code nsub, ");
			//"-- CAPITOLO ");
			sql.append("cap.elem_code elem_code, cap.elem_code2 elem_code2, ");
			//"-- DATA e IMPORTO ");
			sql.append("date_trunc('day',o.ord_emissione_data) data_emissione, d.ord_ts_det_importo importo ,"); //, '**da calcolare **' quietanza ");
			//sql.append("to_char(o.data_creazione, 'dd-mm-YYYY') data_creazione, d.ord_ts_det_importo Importo ");
			sql.append("pt.attoamm_tipo_code, tc.classif_code, ct.classif_tipo_code, ");
			sql.append("rquietanza.ord_quietanza_data dataQuietanza, ");
			sql.append("rquietanza.ord_quietanza_importo importoQuietanza ");
			
			sql.append("from siac_t_atto_amm p " );
			
			sql.append(" left outer join siac_r_atto_amm_class rac on  p.attoamm_id = rac.attoamm_id ");
			sql.append(" left outer join siac_t_class tc on rac.classif_id = tc.classif_id ");
			sql.append(" left outer join siac_d_class_tipo ct on tc.classif_tipo_id = ct.classif_tipo_id, "); 
			
			sql.append("siac_r_ordinativo_atto_amm l, ");
			
			sql.append("siac_d_atto_amm_tipo pt, ");
			sql.append("siac_t_ordinativo o left outer join siac_r_ordinativo_quietanza rquietanza on o.ord_id = rquietanza.ord_id, ");
			sql.append("siac_t_ordinativo_ts q,  ");
			sql.append("siac_t_ordinativo_ts_det d,  ");
			sql.append("siac_d_ordinativo_ts_det_tipo t, siac_d_ordinativo_tipo ip, ");
			sql.append("siac_t_soggetto c, siac_r_ordinativo_soggetto ls, ");
			sql.append("siac_r_ordinativo_stato ll, siac_d_ordinativo_stato so, ");
			sql.append("siac_r_liquidazione_ord rl, siac_t_liquidazione li, ");
			sql.append("siac_r_liquidazione_movgest lm, siac_t_movgest_ts md, siac_t_movgest m, ");
			sql.append("siac_r_movgest_bil_elem ic, siac_t_bil_elem cap, ");
			sql.append("siac_t_bil b, siac_t_periodo a "); //, siac_r_ordinativo_atto_amm rOrdinativoAtto ");
			
			sql.append("where  ");
			//"-- JOIN ");
			sql.append("p.attoamm_id = l.attoamm_id and l.ord_id = o.ord_id ");
			
			//sql.append("and p.attoamm_id = rOrdinativoAtto.attoamm_id ");
			
			sql.append("and p.attoamm_tipo_id = pt.attoamm_tipo_id ");
			//sql.append("and p.attoamm_id = rac.attoamm_id and  rac.classif_id = tc.classif_id ");
			//sql.append("and tc.classif_tipo_id = ct.classif_tipo_id ");
			sql.append("and o.ord_id = q.ord_id ");
			sql.append("and d.ord_ts_id = q.ord_ts_id ");
			sql.append("and t.ord_ts_det_tipo_id = d.ord_ts_det_tipo_id ");
			sql.append("and ip.ord_tipo_id = o.ord_tipo_id ");
			sql.append("and c.soggetto_id = ls.soggetto_id and ls.ord_id = o.ord_id ");
			sql.append("and o.ord_id =ll.ord_id and ll.ord_stato_id = so.ord_stato_id ");
			sql.append("and q.ord_ts_id = rl.sord_id and rl.liq_id = li.liq_id ");
			sql.append("and li.liq_id = lm.liq_id and lm.movgest_ts_id = md.movgest_ts_id  ");
			sql.append("and md.movgest_id = m.movgest_id ");
			sql.append("and ic.movgest_id = m.movgest_id and ic.elem_id = cap.elem_id ");
			sql.append("and cap.bil_id = b.bil_id ");
			sql.append("and a.periodo_id = b.periodo_id ");
			
			
			//"-- NON ANNULLATI ");
			sql.append("and d.validita_fine is null and l.validita_fine is null ");
			sql.append("and o.validita_fine is NULL and q.validita_fine is null ");
			sql.append("and p.validita_fine is null and t.validita_fine is null ");
			sql.append("and c.validita_fine is null and ls.validita_fine is null ");
			sql.append("and ll.validita_fine is null and so.validita_fine is null ");
			sql.append("and rl.validita_fine is null and li.validita_fine is null ");
			sql.append("and lm.validita_fine is null and m.validita_fine is null ");
			sql.append("and md.validita_fine is null ");
			sql.append("and ic.validita_fine is null and cap.validita_fine is null ");
			sql.append("and d.data_cancellazione is null and l.data_cancellazione is null ");
			sql.append("and o.data_cancellazione is NULL and q.data_cancellazione is null ");
			sql.append("and p.data_cancellazione is null and t.data_cancellazione is null ");
			sql.append("and c.data_cancellazione is null and ls.data_cancellazione is null ");
			sql.append("and ll.data_cancellazione is null and so.data_cancellazione is null ");
			sql.append("and rl.data_cancellazione is null and li.data_cancellazione is null ");
			sql.append("and lm.data_cancellazione is null and m.data_cancellazione is null ");
			sql.append("and md.data_cancellazione is null ");
			sql.append("and ic.data_cancellazione is null and cap.data_cancellazione is null ");
			sql.append("and rac.data_cancellazione is null ");
			sql.append("and rquietanza.data_cancellazione is null ");
			//"-- CONDIZIONI FISSE ");
			sql.append("and t.ord_ts_det_tipo_code = 'A' "); //importo attuale
			sql.append("and ip.ord_tipo_code = 'P' "); //pagamenti
			
			//"-- CONDIZIONI DA PARAMETRIZZARE ");
			//"--          ente proprietario ");
			sql.append("and o.ente_proprietario_id = :idEnte  ");
			
			//"--          Provvedimento ");
			//sql.append("and p.attoamm_anno = :annoAtto and p.attoamm_numero = :numeroAtto ");
			sql.append("and l.attoamm_id = :idAtto ");
			
			
			//"-- eventuale anno di esercizio "
			if(annoEsercizio!=null) {
				//sql.append("and a.anno = :annoEsercizio ");
				sql.append("and a.anno <= :annoEsercizio ");
			}
		 
			log.debug("ricercaEstesaOrdinativiSpesa", sql.toString());
			  
			Query query = entityManager.createNativeQuery(sql.toString());
			
			query.setParameter("idEnte", idEnte);	
			query.setParameter("idAtto", atto.getUid());
			
			if(annoEsercizio!=null) {
				query.setParameter("annoEsercizio", String.valueOf(annoEsercizio));
			}
			
			@SuppressWarnings("unchecked")
			List<Object[]> result = (List<Object[]>) query.getResultList();  
			  
			
			if (!result.isEmpty()) {

				for (Object[] lp : result) {
					RicercaEstesaOrdinativiDiPagamentoDto ordinativoPagamentoDto = new RicercaEstesaOrdinativiDiPagamentoDto();

					ordinativoPagamentoDto.setAnnoEsercizio(Integer.parseInt((String) lp[0])); // su t_periodo l'anno è un varchar
					ordinativoPagamentoDto.setAnnoAtto(Integer.parseInt((String) lp[1])); // su t_atto_amm l'anno è varchar
					ordinativoPagamentoDto.setNumeroAtto((Integer) lp[2]); // su t_atto_amm il numero è integer
					ordinativoPagamentoDto.setAnnoOrdinativo((Integer) lp[3]); // su t_ordinativo l'anno è integer
					ordinativoPagamentoDto.setNumeroOrdinativo(((BigDecimal) lp[4]).intValue()); // su t_ordinativo il numero è numerico
					ordinativoPagamentoDto.setCodiceSubOrdinativo((String) lp[5]);
					ordinativoPagamentoDto.setDescrizioneStato((String) lp[6]);
					ordinativoPagamentoDto.setCreditore((String) lp[7]);
					ordinativoPagamentoDto.setAnnoLiquidazione((Integer) lp[8]);
					ordinativoPagamentoDto.setNumeroLiquidazione(((BigDecimal) lp[9]).intValue());
					
					ordinativoPagamentoDto.setAnnoImpegno((Integer) lp[10]);
					ordinativoPagamentoDto.setNumeroImpegno(((BigDecimal) lp[11]).intValue());
					ordinativoPagamentoDto.setCodiceSubImpegno((String) lp[12]);	
					
					ordinativoPagamentoDto.setNumeroCapitolo(Integer.parseInt((String) lp[13]));
					ordinativoPagamentoDto.setNumeroArticolo(Integer.parseInt((String) lp[14]));
				    
				    ordinativoPagamentoDto.setDataEmissione((Date) lp[15]);
				    ordinativoPagamentoDto.setImportoOrdinativo((BigDecimal) lp[16]);
				    
				    ordinativoPagamentoDto.setCodiceTipoAtto((String) lp[17]);
				    ordinativoPagamentoDto.setCodiceSacAtto((String) lp[18]);
				    ordinativoPagamentoDto.setCodiceTipoSacAtto((String) lp[19]);
				    
				    ordinativoPagamentoDto.setDataQuietanza((Date) lp[20]);
				    ordinativoPagamentoDto.setImportoQuietanza(((BigDecimal) lp[21]));
				    
				    ordinativiEstratti.add(ordinativoPagamentoDto);

				}

			}

		return ordinativiEstratti;
	}
	
	public List<SiacTOrdinativoTFin> ricercaDistinitiSiacTOrdinativoTFinByRLiquidazioneOrd(List<SiacRLiquidazioneOrdFin> listaSiacRLiquidazioneOrdFin){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTOrdinativoTFin> listaRitorno = new ArrayList<SiacTOrdinativoTFin>();
		
		if(listaSiacRLiquidazioneOrdFin!=null && listaSiacRLiquidazioneOrdFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT ord FROM SiacTOrdinativoTFin ord WHERE ");
			
			jpql.append(" ord.ordTsId IN ( ");
			int i =0;
			for(SiacRLiquidazioneOrdFin it: listaSiacRLiquidazioneOrdFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getSiacTOrdinativoT().getOrdTsId());
				i++;
			}
			jpql.append(" ) ");
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			listaRitorno = DatiOperazioneUtil.soloValidi(listaRitorno, getNow());
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacTOrdinativoFin> ricercaDistinitiOrdinativoByOrdinativoTestata(List<SiacTOrdinativoTFin> listaSiacTOrdinativoTFin){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTOrdinativoFin> listaRitorno = new ArrayList<SiacTOrdinativoFin>();
		
		if(listaSiacTOrdinativoTFin!=null && listaSiacTOrdinativoTFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT ord FROM SiacTOrdinativoFin ord WHERE ");
			
			jpql.append(" ord.ordId IN ( ");
			int i =0;
			for(SiacTOrdinativoTFin it: listaSiacTOrdinativoTFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getSiacTOrdinativo().getOrdId());
				i++;
			}
			jpql.append(" ) ");
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			listaRitorno = DatiOperazioneUtil.soloValidi(listaRitorno, getNow());
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacTOrdinativoTsDetFin> ricercaDistinitiSiacTOrdinativoTsDetFinByOrdinativoTestata(List<SiacTOrdinativoTFin> listaSiacTOrdinativoTFin){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTOrdinativoTsDetFin> listaRitorno = new ArrayList<SiacTOrdinativoTsDetFin>();
		
		if(listaSiacTOrdinativoTFin!=null && listaSiacTOrdinativoTFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT ordDet FROM SiacTOrdinativoTsDetFin ordDet WHERE ");
			
			jpql.append(" ordDet.siacTOrdinativoT.ordTsId IN ( ");
			int i =0;
			for(SiacTOrdinativoTFin it: listaSiacTOrdinativoTFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getOrdTsId());
				i++;
			}
			jpql.append(" ) ");
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			listaRitorno = DatiOperazioneUtil.soloValidi(listaRitorno, getNow());
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacROrdinativoStatoFin> ricercaDistinitiSiacROrdinativoStatoFinByOrdinativo(List<SiacTOrdinativoFin> listaSiacTOrdinativoFin){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacROrdinativoStatoFin> listaRitorno = new ArrayList<SiacROrdinativoStatoFin>();
		
		if(listaSiacTOrdinativoFin!=null && listaSiacTOrdinativoFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT ordStato FROM SiacROrdinativoStatoFin ordStato WHERE ");
			
			jpql.append(" ordStato.siacTOrdinativo.ordId IN ( ");
			int i =0;
			for(SiacTOrdinativoFin it: listaSiacTOrdinativoFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getOrdId());
				i++;
			}
			jpql.append(" ) ");
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			listaRitorno = DatiOperazioneUtil.soloValidi(listaRitorno, getNow());
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public <ST extends SiacTBase>  List<ST> ricercaBySiacTOrdinativoTFinMassive(List<SiacTOrdinativoTFin> listaInput, String nomeEntity) {
		List<ST> listaRitorno = new ArrayList<ST>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTOrdinativoTFin>> esploso = StringUtilsFin.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTOrdinativoTFin> listaIt : esploso){
					List<ST> risultatoParziale = ricercaBySiacTOrdinativoTFinMassiveCORE(listaIt, nomeEntity);
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
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTOrdinativoTFin e il nome della Entity da cerca vengono restituiti TUTTI I DISTINTI 
	 * oggetti del tipo indicato in nomeEntity in relazione con i SiacTLiquidazioneFin indicati.
	 * ESEMPIO: Se indico nomeEntity = "SiacRLiquidazioneOrdFin" verranno restituiti tutti i distinti record SiacRLiquidazioneOrdFin
	 * in relazione con i record di listaSiacTOrdinativoTFin indicati
	 */
	@SuppressWarnings("unchecked")
	private <ST extends SiacTBase>  List<ST> ricercaBySiacTOrdinativoTFinMassiveCORE(List<SiacTOrdinativoTFin> listaSiacTOrdinativoTFin, String nomeEntity) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<ST> listaRitorno = new ArrayList<ST>();
		
		if(listaSiacTOrdinativoTFin!=null && listaSiacTOrdinativoTFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM "+nomeEntity+" rs WHERE ");
			
			jpql.append(" rs.siacTOrdinativoT.ordTsId IN ( ");
			int i =0;
			for(SiacTOrdinativoTFin it: listaSiacTOrdinativoTFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getOrdTsId());
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
	
	
	public <ST extends SiacTBase>  List<ST> ricercaBySiacTOrdinativoFinMassive(List<SiacTOrdinativoFin> listaInput, String nomeEntity) {
		List<ST> listaRitorno = new ArrayList<ST>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTOrdinativoFin>> esploso = StringUtilsFin.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTOrdinativoFin> listaIt : esploso){
					List<ST> risultatoParziale = ricercaBySiacTOrdinativoFinMassiveCORE(listaIt, nomeEntity);
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
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTOrdinativoFin e il nome della Entity da cerca vengono restituiti TUTTI I DISTINTI 
	 * oggetti del tipo indicato in nomeEntity in relazione con i SiacTLiquidazioneFin indicati.
	 * ESEMPIO: Se indico nomeEntity = "SiacROrdinativoStatoFin" verranno restituiti tutti i distinti record SiacROrdinativoStatoFin
	 * in relazione con i record di listaSiacTOrdinativoFin indicati
	 */
	@SuppressWarnings("unchecked")
	private <ST extends SiacTBase>  List<ST> ricercaBySiacTOrdinativoFinMassiveCORE(List<SiacTOrdinativoFin> listaSiacTOrdinativoFin, String nomeEntity) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<ST> listaRitorno = new ArrayList<ST>();
		
		if(listaSiacTOrdinativoFin!=null && listaSiacTOrdinativoFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM "+nomeEntity+" rs WHERE ");
			
			jpql.append(" rs.siacTOrdinativo.ordId IN ( ");
			int i =0;
			for(SiacTOrdinativoFin it: listaSiacTOrdinativoFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getOrdId());
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
	
	protected List<String> codiciStatiOrdinativo(){
		List<String> codici = new ArrayList<String>();
		codici.add(CostantiFin.D_ORDINATIVO_STATO_INSERITO);
		codici.add(CostantiFin.D_ORDINATIVO_STATO_TRASMESSO);
		codici.add(CostantiFin.D_ORDINATIVO_STATO_FIRMATO);
		codici.add(CostantiFin.D_ORDINATIVO_STATO_QUIETANZATO);
		codici.add(CostantiFin.D_ORDINATIVO_STATO_ANNULLATO);
		return codici;
	}

	@Override
	public BigDecimal findDisponibilitaPagareSottoContoVincolato(Integer uidContoTesoreria, Integer uidCapitolo,
			Integer enteProprietarioId) {
		final String methodName = "findDisponibilitaPagareSottoContoVincolato";
		String functionName = "fnc_siac_disp_pagare_sottoconto_vincolo";
		log.debug(methodName, "Calling functionName: "+ functionName );
		String sql = "SELECT * FROM "+ functionName + "(:uidContoTesoreria, :uidCapitolo, :enteProprietarioId)";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("uidContoTesoreria", uidContoTesoreria);
		query.setParameter("uidCapitolo", uidCapitolo);
		query.setParameter("enteProprietarioId", enteProprietarioId);
				
		
		return (BigDecimal) query.getSingleResult();
	}

	@Override
	public BigDecimal findDisponibilitaIncassareSottoContoVincolato(Integer uidContoTesoreria, Integer uidCapitolo,	Integer enteProprietarioId) {
		final String methodName = "findDisponibilitaIncassareSottoContoVincolato";
		String functionName = "fnc_siac_disp_incassare_sottoconto_vincolo";
		log.debug(methodName, "Calling functionName: "+ functionName );
		String sql = "SELECT * FROM "+ functionName + "(:uidContoTesoreria, :uidCapitolo, :enteProprietarioId)";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("uidContoTesoreria", uidContoTesoreria);
		query.setParameter("uidCapitolo", uidCapitolo);
		query.setParameter("enteProprietarioId", enteProprietarioId);
				
		
		return (BigDecimal) query.getSingleResult();
	}}
