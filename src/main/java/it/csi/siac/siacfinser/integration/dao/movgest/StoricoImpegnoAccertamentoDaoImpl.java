/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaStoricoImpegnoAccertamentoParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStoricoImpAccFin;

@Component
@Transactional
public class StoricoImpegnoAccertamentoDaoImpl extends AbstractDao<SiacRMovgestTsStoricoImpAccFin, Integer> implements StoricoImpegnoAccertamentoDao{

	
	@Override
	public Integer contaStorico(Integer enteUid, RicercaStoricoImpegnoAccertamentoParamDto paramDto){ 
		Query query = creaQueryRicercaStoricoImpegniAccertamento(enteUid, paramDto, true,null);
		return query.getResultList() != null ? query.getResultList().size() : 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacRMovgestTsStoricoImpAccFin> ricercaStoricoImpegnoAccertamento(Integer enteUid, RicercaStoricoImpegnoAccertamentoParamDto prop, int numeroPagina, int numeroRisultatiPerPagina) {

		List<SiacRMovgestTsStoricoImpAccFin> lista = new ArrayList<SiacRMovgestTsStoricoImpAccFin>();
		
		Query query = creaQueryRicercaStoricoImpegniAccertamento(enteUid, prop, false,null);
		
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
	
	
	public Query creaQueryRicercaStoricoImpegniAccertamento(Integer enteUid, RicercaStoricoImpegnoAccertamentoParamDto paramDto, boolean soloCount, Boolean richiestiIds) {
		

		Map<String,Object> param = new HashMap<String, Object>();
		StringBuilder jpql = new StringBuilder();
		
		//query di base
		jpql.append(" SELECT DISTINCT storico ")
			.append(Boolean.TRUE.equals(richiestiIds)?".movgestTsRStoricoId " : "")
			.append(" FROM SiacRMovgestTsStoricoImpAccFin storico ")
			.append(" WHERE storico.dataCancellazione IS NULL ")
			.append(" AND storico.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		
		param.put("enteProprietarioId", enteUid);
		
		if(paramDto.getAnnoAccertamento() != null){
			jpql.append(" AND storico.movgestAnnoAcc = :movgestAnnoAcc ");
			param.put("movgestAnnoAcc", paramDto.getAnnoAccertamento());
		}
		
		if(paramDto.getNumeroAccertamento() != null){
			jpql.append(" AND storico.movgestNumeroAcc = :movgestNumeroAcc");
			param.put("movgestNumeroAcc", paramDto.getNumeroAccertamento());
		}
		
		if(paramDto.getNumeroSubAccertamento() != null){
			jpql.append(" AND storico.movgestSubnumeroAcc = :movgestSubnumeroAcc");
			param.put("movgestNumeroSubAcc", paramDto.getNumeroSubAccertamento());
		}
		
		if(paramDto.getAnnoImpegno() != null){
			jpql.append(" AND storico.siacTMovgestT.siacTMovgest.movgestAnno = :movgestAnno");
			param.put("movgestAnno", paramDto.getAnnoImpegno());
		}
		
		if(paramDto.getNumeroImpegno() != null){
			jpql.append(" AND storico.siacTMovgestT.siacTMovgest.movgestNumero = :movgestNumero");
			param.put("movgestNumero", paramDto.getNumeroImpegno());
		}
		
		if(paramDto.getNumeroSubImpegno() != null){
			jpql.append(" AND storico.siacTMovgestT.movgestTsCode = :movgestTsCode");
			param.put("movgestTsCode", paramDto.getNumeroSubImpegno().toPlainString());
		}
		
		if(paramDto.getBilancio() != null ) {
			if(paramDto.getBilancio().getUid() != 0) {
				jpql.append(" AND storico.siacTMovgestT.siacTMovgest.siacTBil.bilId = :bilId");
				param.put("bilId", paramDto.getBilancio().getUid());
			}else if(paramDto.getBilancio().getAnno() != 0){
				jpql.append(" AND storico.siacTMovgestT.siacTMovgest.siacTBil.siacTPeriodo.anno = :periodoAnno");
				param.put("periodoAnno", String.valueOf(paramDto.getBilancio().getAnno()));
			}
		}
		
		if(Boolean.TRUE.equals(paramDto.getEscludiSubImpegni())) {
			jpql.append(" AND storico.siacTMovgestT.siacDMovgestTsTipo.movgestTsTipoCode = 'T' ");
		}
		//Termino restituendo l'oggetto di ritorno: 
        return createQuery(jpql.toString(), param);
	}
	
	@Override
	public SiacRMovgestTsStoricoImpAccFin findSiacRMovgestTsStoricoImpAccCorrispondenteInAnnoBilancio(Integer movgestTsRStoricoId, String anno, boolean storicizzazioneSuSubAccertamento) {
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuilder jpql = new StringBuilder();
		
		jpql.append(" FROM SiacRMovgestTsStoricoImpAccFin st ")
			.append(" WHERE st.dataCancellazione IS NULL ")
			.append(" AND st.siacTMovgestT.siacTMovgest.siacTBil.siacTPeriodo.anno = :anno ")
			.append(" AND st.dataCancellazione IS NULL ")
			.append(" AND EXISTS ( ")
			.append("	FROM SiacRMovgestTsStoricoImpAccFin stInput ")
			.append("	WHERE stInput.siacTMovgestT.movgestTsCode = st.siacTMovgestT.movgestTsCode ")
			.append("	AND stInput.siacTMovgestT.siacTMovgest.movgestAnno = st.siacTMovgestT.siacTMovgest.movgestAnno ")
			.append("	AND stInput.siacTMovgestT.siacTMovgest.movgestNumero = st.siacTMovgestT.siacTMovgest.movgestNumero ")
			.append("	AND stInput.siacTMovgestT.siacTMovgest.siacDMovgestTipo = st.siacTMovgestT.siacTMovgest.siacDMovgestTipo ")
			.append("	AND stInput.movgestAnnoAcc = st.movgestAnnoAcc ")
			.append("	AND stInput.movgestNumeroAcc = st.movgestNumeroAcc ");
		if(storicizzazioneSuSubAccertamento) {
			jpql.append("	AND stInput.movgestSubnumeroAcc = st.movgestSubnumeroAcc ");
		}else {
			jpql.append("	AND stInput.movgestSubnumeroAcc IS NULL");
		}
			
		jpql.append("	AND stInput.movgestTsRStoricoId = :movgestTsRStoricoId ")
			.append("	AND stInput.dataCancellazione IS NULL ")
			.append(") ");
		
		param.put("movgestTsRStoricoId", movgestTsRStoricoId);
		param.put("anno", anno);
		
		
		Query query = createQuery(jpql.toString(), param);
		
		@SuppressWarnings("unchecked")
		List<SiacRMovgestTsStoricoImpAccFin> resultList = query.getResultList();
			
		return resultList != null && resultList.size() != 0 ? resultList.get(0) : null; 
	}

}

