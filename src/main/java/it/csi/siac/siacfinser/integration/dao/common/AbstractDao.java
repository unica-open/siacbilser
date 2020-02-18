/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.common.dto.CodificaImportoDto;
import it.csi.siac.siacfinser.integration.util.DataValiditaUtils;

	
public class AbstractDao<T, ID extends Serializable> extends JpaDao<T, ID>{
	
	public final static int DIMENSIONE_MASSIMA_QUERY_IN = 100;
	
	@Autowired
	@Qualifier("dataSource")
	protected DataSource dataSource;
	
	
	protected String buildClausolaRicercaAttr(String attrCode, String valoreRicerca, String aliasTAtt, String aliasRAtt,String paramName,Map<String,Object> param){
		String clausola = "";
		if(!StringUtils.isEmpty(valoreRicerca)){
			clausola = clausola + " AND  "+aliasTAtt+".attrCode = '"+attrCode+"' AND UPPER("+aliasRAtt+".testo) LIKE UPPER(:"+paramName+") ";
			String cupLike = buildLikeString(valoreRicerca);
			param.put(paramName, cupLike);
		}
		return clausola;
	}
	
	protected String buildClausolaLikeGenerico(StringBuilder jpql, String colonna, String valoreRicerca,String paramName,Map<String,Object> param){
		String clausola = "";
		if(!StringUtils.isEmpty(valoreRicerca)){
			clausola = clausola + " AND UPPER("+colonna+") LIKE UPPER(:"+paramName+") ";
			String likeValue = buildLikeString(valoreRicerca);
			param.put(paramName, likeValue);
			jpql.append(clausola);
		}
		return clausola;
	}
	
	protected String buildLikeString(String valoreRicerca){
		String likeString = null;
		if(valoreRicerca.contains("%")){
			likeString = valoreRicerca;
		} else {
			likeString = '%' + valoreRicerca + '%';
		}
		return likeString;
	}
	
	protected String buildClausolaRicercaTClass(String classCode, String codiceDaCercare, String aliasTClass, String aliasRClass,String paramName,Map<String,Object> param){
		ArrayList<String> list = new  ArrayList<String>();
		list.add(classCode);
		return buildClausolaRicercaTClass(list, codiceDaCercare, aliasTClass, aliasRClass, paramName, param);
	}

		
	
	protected String buildClausolaRicercaTClass(List<String> classCode, String codiceDaCercare, String aliasTClass, String aliasRClass,String paramName,Map<String,Object> param){
		String clausola = "";
		if(!StringUtils.isEmpty(codiceDaCercare)){
			
			String elencoInOr = null;
			
			if(classCode.size()==1){
				elencoInOr = aliasTClass+".siacDClassTipo.classifTipoCode = '"+classCode.get(0)+"' ";
			} else {
				elencoInOr = "(";
				boolean first = true;
				for(String it : classCode){
					if(!first){
						elencoInOr = elencoInOr + " OR ";
					}
					elencoInOr = elencoInOr + aliasTClass+".siacDClassTipo.classifTipoCode = '"+it+"' ";
					first = false;
				}
				elencoInOr = elencoInOr + ")";
			}
			
			//clausola = clausola + " AND  "+aliasTAtt+".attrCode = '"+attrCode+"' AND UPPER("+aliasRAtt+".testo) LIKE UPPER(:"+paramName+") ";
			clausola = clausola + " AND  "+ elencoInOr +" AND UPPER("+aliasTClass+".classifCode) = UPPER(:"+paramName+") ";
			clausola = clausola + " AND  " + DataValiditaUtils.validitaForQuery(aliasRClass);
			param.put(paramName, codiceDaCercare);
		}
		return clausola;
	}
	
	protected Date getNowDate(){
		return TimingUtils.getNowDate();
	}
	
	protected Timestamp getNow(){
		return new Timestamp(System.currentTimeMillis());
	}
	
	protected List<CodificaImportoDto> mapResultToElencoCodificaImportoDto(List<Object[]> result) {
		
		List<CodificaImportoDto> elencoCodificaImportoDto = new ArrayList<CodificaImportoDto>(); 

		if (!result.isEmpty()) {

			for (Object[] lp : result) {
				CodificaImportoDto codificaImportoDto = new CodificaImportoDto();

				codificaImportoDto.setIdOggetto((Integer) lp[0]);
				codificaImportoDto.setImporto((BigDecimal) lp[1]);
				elencoCodificaImportoDto.add(codificaImportoDto);

			}

		}

		return elencoCodificaImportoDto;
	}

	protected String buildCondizioneValiditaSql(String aliasTabella, boolean ancheDataCancellazione){
		String condizioneSQL =
		" date_trunc('day', now()) between date_trunc('day', "+aliasTabella+".validita_inizio) " +
		" and coalesce("+aliasTabella+".validita_fine, date_trunc('day', now())) and "+aliasTabella+".validita_fine is null ";
		if(ancheDataCancellazione){
			//non tutte le tabella hanno questa colonna
			condizioneSQL = condizioneSQL + " and "+aliasTabella+".data_cancellazione is null ";
		}
		return condizioneSQL;
	}
	
	protected String buildElencoIntegerPerClausolaIN(List<Integer> lista){
		String elenco = "";
		boolean first = true;
		for(Integer it : lista){
			if(!first){
				elenco = elenco + ",";
			}
			elenco = elenco + it.toString();
			first = false;
		}
		return elenco;
	}
	
	protected String buildElencoPerClausolaIN(List<String> lista){
		String elenco = "";
		boolean first = true;
		for(String it : lista){
			if(!first){
				elenco = elenco + ",";
			}
			elenco = elenco + it;
			first = false;
		}
		return elenco;
	}
	
	/**
	 * Aggiunge una clausola del tipo:
	 * 
	 * AND (
	 * 
	 *   campoUguaglianza = : aliasParams1
	 *   OR campoUguaglianza = : aliasParams2
	 *   OR campoUguaglianza = : aliasParams3
	 *   ..
	 *   OR campoUguaglianza = : aliasParamsN
	 * 
	 * )
	 * 
	 * Dove N e' la dimensione di valori 
	 * e mette in params tali valori rispetto a detti alias
	 * 
	 * @param campoUguaglianza
	 * @param aliasParams
	 * @param valori
	 * @param param
	 * @param jpql
	 */
	protected void buildClausolaAlmentoUnoUguale(String campoUguaglianza, String aliasParams,List<String> valori, Map<String,Object> param,StringBuilder jpql){
		if(!StringUtils.isEmpty(valori)){
			jpql.append(" AND ( ");
			int statoIndex = 0;
			for(String statoIt: valori){
				if(!StringUtils.isEmpty(statoIt)){
					if(statoIndex>0){
						jpql.append(" OR ");
					}
					String statoParamName = aliasParams+statoIndex+1;
					jpql.append(" "+campoUguaglianza+" = :"+statoParamName +" ");
					statoIndex++;
					param.put(statoParamName, statoIt);
				}
			}
			jpql.append(" )");
		}
	}
	
	/**
	 * Metodo centralizzato per gestire la creazione di un oggetto Pageable.
	 * 
	 * Scala numeroPagina di un elemento perche' i nostri servizi si aspettano
	 * le pagine che partono da 1 mentre Pageable di JPA inizia a contare la prima pagina da 0.
	 * 
	 * @param numeroPagina
	 * @param numeroRisultatiPerPagina
	 * @return
	 */
	protected Pageable buildPageable(int numeroPagina, int numeroRisultatiPerPagina){
		return new PageRequest(numeroPagina-1, numeroRisultatiPerPagina);
	}
	
	protected Pageable buildPageableAllRecords(){
		return new PageRequest(0, Integer.MAX_VALUE);
	}
	
}
