/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.pagopa.model.Riconciliazione;
import it.csi.siac.pagopa.model.RiconciliazioneDoc;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.entity.PagopaDRiconciliazioneErrore;
import it.csi.siac.siacbilser.integration.entity.PagopaTElaborazioneFlusso;
import it.csi.siac.siacbilser.integration.entity.PagopaTRiconciliazione;
import it.csi.siac.siacbilser.integration.entity.PagopaTRiconciliazioneDoc;

	
	
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PagopaElaborazioneDaoImpl extends ExtendedJpaDao<PagopaTElaborazioneFlusso, Integer> implements PagopaElaborazioneDao {

	@Override
	public Page<PagopaTElaborazioneFlusso> ricercaElaborazioni(Integer enteProprietarioId, String numeroProvvisorio,
			String flusso, Date dataEmissioneDa, Date dataEmissioneA, Date dataElaborazioneFlussoDa,
			Date dataElaborazioneFlussoA, String esitoElaborazioneFlussoFE, Pageable pageable)
	{

		final String methodName = "ricercaElaborazioni";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaElaborazioni( jpql, param, enteProprietarioId, numeroProvvisorio,
				 flusso,  dataEmissioneDa,  dataEmissioneA,  dataElaborazioneFlussoDa, dataElaborazioneFlussoA, esitoElaborazioneFlussoFE);
		
		//SIAC-7556 GM 20/07/2020
		//siac-7911-3 Inizio FL
		jpql.append(" ORDER BY tef.pagopaElabFlussoNumProvvisorio ");
		//jpql.append(" ORDER BY tef.pagopaElabFlussoData DESC");
		//siac-7911-3 Fine FL
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
		

	}
	
	private void componiQueryRicercaElaborazioni(StringBuilder jpql,
			Map<String, Object> param, 
			Integer enteProprietarioId,
			 String numeroProvvisorio, String flusso, Date dataEmissioneDa, Date dataEmissioneA, Date dataElaborazioneFlussoDa, Date dataElaborazioneFlussoA, String esitoElaborazioneFlussoFE) {
		
		jpql.append("SELECT DISTINCT tef ");
		
		jpql.append("FROM PagopaTElaborazioneFlusso tef , SiacTProvCassa tpc ");
		jpql.append(" WHERE tef.dataCancellazione IS NULL ");
		jpql.append(" AND tef.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		jpql.append(" AND tef.pagopaElabFlussoNumProvvisorio = tpc.provcNumero ");
		jpql.append(" AND tef.pagopaElabFlussoAnnoProvvisorio = tpc.provcAnno ");
		jpql.append(" AND tef.siacTEnteProprietario.enteProprietarioId  = tpc.siacTEnteProprietario.enteProprietarioId  ");
		
		jpql.append(" AND tpc.dataCancellazione IS NULL ");
		jpql.append(" AND tpc.siacDProvCassaTipo.provcTipoCode = :provcTipoCode ");
		param.put("provcTipoCode","E");
		
		
		
		
		
		
		if (numeroProvvisorio != null && !numeroProvvisorio.equals("") ) {
			try{
				Integer numProvvInt = Integer.parseInt(numeroProvvisorio);
				jpql.append("             AND tef.pagopaElabFlussoNumProvvisorio = :numeroProvvisorio ");
					param.put("numeroProvvisorio", numProvvInt);
			}catch (Exception e) {
				log.error("PagopaElaborazioneDaoImpl", "numero provvisorio non corretto");
			}
		}
			
		if (flusso != null && !flusso.equals("") ) {
				//jpql.append("             AND tef.pagopaElabRicFlussoId = :flusso "); 
			jpql.append("             AND LOWER(tef.pagopaElabRicFlussoId) like '%' || :flusso || '%' "); 
			param.put("flusso", flusso.toLowerCase());
		}

		
		if(dataElaborazioneFlussoDa != null) {
			Date startDataElaborazioneFlussoDa = setMezzanotte(dataElaborazioneFlussoDa);
			jpql.append(" AND "+Utility.toJpqlDateParamGreaterOrEquals( "tef.pagopaElabFlussoData",":startDataElaborazioneFlussoDa"));
			param.put("startDataElaborazioneFlussoDa", startDataElaborazioneFlussoDa);
		}
		
		if(dataElaborazioneFlussoA != null) {
			Date endElaborazioneFlussoA = setMezzanotte(DateUtils.addDays(dataElaborazioneFlussoA, 1));
			jpql.append(" AND "+Utility.toJpqlDateParamLesser( "tef.pagopaElabFlussoData",":endDataElaborazioneFlussoA"));
			param.put("endDataElaborazioneFlussoA", endElaborazioneFlussoA);
		}
	 
		
		//SIAC-7911-2 Inizio
		//if (dataEmissioneDa != null || dataEmissioneA != null) {
//			jpql.append(" AND tef.siacTProvCassa.dataCancellazione IS NULL ");
//			jpql.append(" AND tef.siacTProvCassa.siacDProvCassaTipo.provcTipoCode = :provcTipoCode ");
//			param.put("provcTipoCode","E");
		//}
		//SIAC-7911-2 Fine
		
		if(dataEmissioneDa != null) {
			Date startDataEmissioneDa = setMezzanotte(dataEmissioneDa);
			//SIAC-7911
			//jpql.append(" AND "+Utility.toJpqlDateParamGreaterOrEquals( "tef.pagopaElabRicFlussoData",":startDataEmissioneDa"));
			jpql.append(" AND "+Utility.toJpqlDateParamGreaterOrEquals( "tpc.provcDataEmissione",":startDataEmissioneDa"));
			param.put("startDataEmissioneDa", startDataEmissioneDa);
		}
		
		if(dataEmissioneA != null) {
			Date endDataEmissioneA = setMezzanotte(DateUtils.addDays(dataEmissioneA, 1));
			//SIAC-7911
			//jpql.append(" AND "+Utility.toJpqlDateParamLesser( "tef.pagopaElabRicFlussoData",":endDataEmissioneA")); 
			jpql.append(" AND "+Utility.toJpqlDateParamLesser( "tpc.provcDataEmissione",":endDataEmissioneA"));

			param.put("endDataEmissioneA", endDataEmissioneA);
		}
	 	 
		
		//SIAC-8046 CM 12/03/2021 Inizio
		//if(StringUtils.isNotBlank(esitoElaborazioneFlussoFE) && !esitoElaborazioneFlussoFE.equals("TUTTI")) {
		if(esitoElaborazioneFlussoFE != null && (esitoElaborazioneFlussoFE.equals("OK") || (esitoElaborazioneFlussoFE.equals("KO")))) {
	
			jpql.append("AND ");
			if(esitoElaborazioneFlussoFE.equals("OK")){
				jpql.append(" NOT ");
			}
			jpql.append(" EXISTS (");
			jpql.append("FROM PagopaTRiconciliazioneDoc trd ");
			jpql.append(" WHERE ");
			jpql.append(" trd.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
			param.put("enteProprietarioId", enteProprietarioId);
			jpql.append(" AND trd.pagopaTElaborazioneFlusso.pagopaElabFlussoId = tef.pagopaElabFlussoId ");
			jpql.append(" AND trd.dataCancellazione IS NULL ");
			jpql.append(" AND trd.pagopaRicDocFlagDett = :flagDett ");
			param.put("flagDett", false);
			//SIAC-8123 CM 06/05/2021 Inizio - Intervento 1 - aggiunta condizione per esito flusso elaborazione fix dopo test in collaudo
			jpql.append(" AND trd.pagopaRicDocFlagConDett = :flagConDett ");
			param.put("flagConDett", false);
			//SIAC-8123 CM 06/05/2021 Fine
			jpql.append(" AND trd.pagopaRicDocSubdocId  IS NULL ");  //questa è stata aggiunta rispetto a quella di riferimento 'ricercaRiconciliazioniDocByElabFlussoId'
			jpql.append(" ORDER BY trd.dataCreazione");
			jpql.append(")");
		}
		//SIAC-8046 CM 12/03/2021 Fine
	}
	
	
	

	@Override
	public Page<PagopaTRiconciliazione> ricercaRiconciliazioni(Integer enteProprietarioId,  Riconciliazione riconciliazione, Pageable pageable)
	{

		final String methodName = "ricercaRiconciliazioni";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaRiconciliazioni( jpql, param, enteProprietarioId, riconciliazione );
		
		jpql.append(" ORDER BY tr.pagopaRicFlussoData ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
		

	}
	
	private void componiQueryRicercaRiconciliazioni(StringBuilder jpql,
			Map<String, Object> param, 
			Integer enteProprietarioId,
			Riconciliazione riconciliazione) {
		
		jpql.append("FROM PagopaTRiconciliazione tr ");
		jpql.append(" WHERE tr.dataCancellazione IS NULL ");
		jpql.append(" AND tr.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		
		jpql.append(" AND tr.pagopaRicFlussoId = :pagopaRicFlussoId ");
		param.put("pagopaRicFlussoId", riconciliazione.getFlusso());
		
		
	}


	@Override
	public Page<PagopaTRiconciliazioneDoc> ricercaRiconciliazioniDoc(Integer enteProprietarioId,  RiconciliazioneDoc riconciliazioneDoc, Pageable pageable)
	{

		final String methodName = "ricercaRiconciliazioniDoc";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaRiconciliazioniDoc( jpql, param, enteProprietarioId, riconciliazioneDoc );
		
		jpql.append(" ORDER BY trd.pagopaTElaborazioneFlusso.pagopaElabFlussoId");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
		

	}
	
	private void componiQueryRicercaRiconciliazioniDoc(StringBuilder jpql,
			Map<String, Object> param, 
			Integer enteProprietarioId,
			RiconciliazioneDoc riconciliazioneDoc) {
		
		jpql.append("FROM PagopaTRiconciliazioneDoc trd ");
		jpql.append(" WHERE ");
		jpql.append(" trd.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);

		//is not nulll
		jpql.append(" AND trd.pagopaTRiconciliazione.pagopaRicId = :pagopaRicId ");
		param.put("pagopaRicId", riconciliazioneDoc.getRiconciliazione().getRicId());
		
		jpql.append(" AND trd.pagopaTElaborazioneFlusso.pagopaElabFlussoId = :flussoId ");
		param.put("flussoId", riconciliazioneDoc.getElaborazione().getUid());

		jpql.append(" AND trd.pagopaDRiconciliazioneErrore IS NOT NULL ");
		
		jpql.append(" AND trd.dataCancellazione IS NULL ");
		
		jpql.append(" AND trd.pagopaRicDocFlagDett = :flagDett ");
		param.put("flagDett",true);
		
		
	}
	
	private Date setMezzanotte(Date data) {
		data = DateUtils.setHours(data, 0);
		data = DateUtils.setMinutes(data, 0);
		data = DateUtils.setSeconds(data, 0);
		data = DateUtils.setMilliseconds(data, 0);
		return data;
	}
	
	@Override
	public Page<Object[]> ricercaRiconciliazioniConDettagli(Integer enteProprietarioId, RiconciliazioneDoc riconciliazione, Pageable pageable){
        final String methodName = "ricercaRiconciliazioniConDettagli";
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> param = new HashMap<String, Object>();
        componiQueryRicercaRiconciliazioniConDettagli( jpql, param, enteProprietarioId, riconciliazione );
        log.debug(methodName, "JPQL to execute: " + jpql.toString());
        return getNativePagedList(jpql.toString(), param, pageable, true);
    }
   
    private void componiQueryRicercaRiconciliazioniConDettagli(StringBuilder jpql,
            Map<String, Object> param,
            Integer enteProprietarioId,
            RiconciliazioneDoc riconciliazione) {
       
    	jpql.append(" SELECT ");
    	jpql.append(" (select doc1.pagopa_ric_errore_id from siac.pagopa_t_riconciliazione_doc doc1 ");
    	jpql.append(" where doc1.pagopa_elab_flusso_id = :pagopaRicFlussoId");
        jpql.append(" AND doc1.pagopa_ric_doc_flag_dett = true ");
        // SIAC-8123 CM 26/03/2021 Inizio
        jpql.append(" AND doc1.pagopa_ric_id = doc.pagopa_ric_id ");
        // SIAC-8123 CM 26/03/2021 Fine
        jpql.append(" and doc1.pagopa_ric_errore_id is not null limit 1) as erroreDettaglio, ");
        //05/08/2020 SIAC-7556 GM - trovato errore in riga di sintesi
    	//modificato recupero errore da catena elaborazione inserendo anche questa possibilità
        //jpql.append(" COALESCE(doc.pagopa_ric_errore_id, (select doc1.pagopa_ric_errore_id from siac.pagopa_t_riconciliazione_doc doc1 where doc1.pagopa_elab_flusso_id = :pagopaRicFlussoId  and doc1.pagopa_ric_doc_flag_dett = true  and doc1.pagopa_ric_errore_id is not null limit 1) ) as erroreDettaglio, ");
        // SIAC-8046 CM 16-19/03/2021 Task 2.0 Inizio
        jpql.append(" doc.pagopa_ric_doc_voce_code, doc.pagopa_ric_doc_voce_desc, doc.pagopa_ric_doc_sottovoce_code,  doc.pagopa_ric_doc_anno_accertamento, ");
    	jpql.append(" doc.pagopa_ric_doc_num_accertamento, tr.pagopa_ric_flusso_anno_accertamento, tr.pagopa_ric_flusso_num_accertamento, doc.pagopa_elab_flusso_id, ");
    	jpql.append(" doc.pagopa_ric_doc_sottovoce_importo, doc.pagopa_ric_id, doc.pagopa_ric_errore_id, doc.pagopa_ric_doc_subdoc_id, doc.pagopa_ric_doc_stato_elab, ");
    	//SIAC-8123 CM 29/04/2021 - Aggiunto campo 'pagopa_ric_doc_flag_con_dett' tra i dati di ritorno
    	jpql.append(" doc.pagopa_ric_doc_flag_con_dett, fppstato.file_pagopa_stato_code ");
        jpql.append(" FROM pagopa_t_riconciliazione_doc doc , pagopa_t_riconciliazione tr, siac_t_file_pagopa fppa, siac_d_file_pagopa_stato fppstato ");
        jpql.append(" WHERE doc.pagopa_elab_flusso_id = :pagopaRicFlussoId ");
        jpql.append(" AND doc.ente_proprietario_id = :enteProprietarioId ");
        jpql.append(" AND doc.data_cancellazione is null ");
        jpql.append(" AND doc.pagopa_ric_doc_flag_dett = false ");
        jpql.append(" AND doc.pagopa_ric_id = tr.pagopa_ric_id ");
        jpql.append(" AND fppa.file_pagopa_id = tr.file_pagopa_id ");
        jpql.append(" AND fppa.file_pagopa_stato_id = fppstato.file_pagopa_stato_id ");
        // SIAC-8046 CM 16-19/03/2021 Fine
        jpql.append(" order by doc.data_creazione desc ");
        
        param.put("pagopaRicFlussoId", riconciliazione.getElaborazione().getUid());
        param.put("enteProprietarioId", enteProprietarioId);   
    }
    
    public List<PagopaTRiconciliazioneDoc> ricercaRiconciliazioniDocByElabFlussoId(Integer enteProprietarioId, Integer elabFlussoId){
    	final String methodName = "ricercaRiconciliazioniDocByElabFlussoId";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		jpql.append("FROM PagopaTRiconciliazioneDoc trd ");
		jpql.append(" WHERE ");
		jpql.append(" trd.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		jpql.append(" AND trd.pagopaTElaborazioneFlusso.pagopaElabFlussoId = :flussoId ");
		param.put("flussoId", elabFlussoId);
		jpql.append(" AND trd.dataCancellazione IS NULL ");
		jpql.append(" AND trd.pagopaRicDocFlagDett = :flagDett ");
		param.put("flagDett", false);
		//SIAC-8123 CM 28/04/2021 Inizio - Intervento 1 - aggiunta condizione per esito flusso elaborazione
		jpql.append(" AND trd.pagopaRicDocFlagConDett = :flagConDett ");
		param.put("flagConDett", false);
		//SIAC-8123 CM 28/04/2021 Fine
		jpql.append(" ORDER BY trd.dataCreazione");
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<PagopaTRiconciliazioneDoc> list = query.getResultList();
		return list;
		
    }
    
    
    //SIAC-8046 Task 2.2 CM 01/04/2021 Inizio
	@Override
	public Integer ricercaAccertamentoInRiconciliazione(Integer enteProprietarioId, String annoEsercizio, RiconciliazioneDoc riconciliazioneDoc){
        final String methodName = "ricercaRiconciliazioniConDettagli";
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> param = new HashMap<String, Object>();
        //componiQueryRicercaAccertamentoInRiconciliazione( jpql, param, enteProprietarioId, riconciliazioneDoc);
        Query query = componiQueryRicercaAccertamentoInRiconciliazione( jpql, param, enteProprietarioId, annoEsercizio, riconciliazioneDoc);
        log.debug(methodName, "JPQL to execute: " + jpql.toString());
        return query.getResultList() != null ? query.getResultList().size() : 0;
    }
	
    private Query componiQueryRicercaAccertamentoInRiconciliazione(StringBuilder jpql,
            Map<String, Object> param,
            Integer enteProprietarioId,
            String annoEsercizio,
            RiconciliazioneDoc riconciliazioneDoc) {
    	
    	Integer annoAccConvert = Integer.parseInt(riconciliazioneDoc.getRiconciliazione().getAnno());
		Integer numeroAccConverter = Integer.parseInt(riconciliazioneDoc.getRiconciliazione().getNumeroAccertamento());
		
    	String movgestTipoCode = "A";
    	String movgestStatoCode = "D";
    	String movgestTsTipoCode = "T";
       
    	//jpql.append(" SELECT stm.movgest_id, stm.movgest_anno, stm.movgest_numero, sdmt.movgest_tipo_id, stm.ente_proprietario_id, stb.bil_tipo_id, dmgs.movgest_stato_id ");
    	jpql.append(" SELECT stm.movgestId ");
    	//jpql.append(" FROM siac_t_movgest stm, siac_t_ente_proprietario step, siac_t_bil stb, siac_t_periodo stp, siac_r_movgest_ts_stato rmts, siac_d_movgest_stato dmgs, siac_d_movgest_tipo sdmt, siac_t_movgest_ts stmt, siac_d_movgest_ts_tipo sdmtt ");
    	jpql.append(" FROM SiacTMovgest stm, SiacTEnteProprietario step, SiacTBil stb, SiacTPeriodo stp, SiacRMovgestTsStato rmts, SiacDMovgestStato dmgs, SiacDMovgestTipo sdmt, SiacTMovgestT stmt, SiacDMovgestTsTipo sdmtt ");
        jpql.append(" WHERE stm.movgestAnno = :annoAcc ");
        jpql.append(" AND stm.movgestNumero = :numeroAcc ");
        jpql.append(" AND stm.siacTEnteProprietario.enteProprietarioId = step.enteProprietarioId ");
        jpql.append(" AND step.enteProprietarioId = :enteProprietarioId ");
        jpql.append(" AND stm.siacTBil.bilId = stb.bilId ");
        jpql.append(" AND stb.siacTPeriodo.periodoId = stp.periodoId ");
        jpql.append(" AND stp.anno = :annoEsercizio ");
        jpql.append(" AND stm.siacDMovgestTipo.movgestTipoId = sdmt.movgestTipoId ");
        jpql.append(" AND sdmt.movgestTipoCode = :movgestTipoCode ");        		
        //jpql.append(" AND stm.siacTMovgestTs.movgestTsId = stmt.movgestTsId ");
        jpql.append(" AND stm.movgestId = stmt.siacTMovgest.movgestId ");
        //jpql.append(" AND stmt.siacRMovgestTsStatos.siacTMovgestT = rmts.siacTMovgestT ");
        //stmt.movgestTsId = stmt.siacTMovgestT.movgestTsId
        jpql.append(" AND stmt.movgestTsId = rmts.siacTMovgestT.movgestTsId ");
        jpql.append(" AND rmts.siacDMovgestStato.movgestStatoId = dmgs.movgestStatoId ");
        jpql.append(" AND dmgs.movgestStatoCode = :movgestStatoCode ");
        jpql.append(" AND stmt.siacDMovgestTsTipo.movgestTsTipoId = sdmtt.movgestTsTipoId ");
        jpql.append(" AND sdmtt.movgestTsTipoCode = :movgestTsTipoCode ");
        jpql.append(" AND stm.dataFineValidita is null ");
        jpql.append(" AND step.dataFineValidita is null ");
        jpql.append(" AND sdmt.dataFineValidita is null ");
        jpql.append(" AND stb.dataFineValidita is null ");
        jpql.append(" AND stp.dataFineValidita is null ");
        jpql.append(" AND stmt.dataFineValidita is null ");
        jpql.append(" AND sdmtt.dataFineValidita is null ");
        jpql.append(" AND stm.dataCancellazione is null ");
        jpql.append(" AND step.dataCancellazione is null ");
        jpql.append(" AND sdmt.dataCancellazione is null ");
        jpql.append(" AND stp.dataCancellazione is null ");
        jpql.append(" AND stb.dataCancellazione is null ");
        jpql.append(" AND stmt.dataCancellazione is null ");
        jpql.append(" AND sdmtt.dataCancellazione is null ");
        
        param.put("annoAcc", annoAccConvert);
        param.put("numeroAcc", BigDecimal.valueOf(numeroAccConverter)); 
        param.put("enteProprietarioId", enteProprietarioId);   
        param.put("annoEsercizio", annoEsercizio.toString());   
        param.put("movgestTipoCode", movgestTipoCode);   //A - indica se il movimento è accertamento o impegno
        param.put("movgestStatoCode", movgestStatoCode);  //D - indica se il movimento è in stato definitivo
        param.put("movgestTsTipoCode", movgestTsTipoCode);  //T - indica se il movimento è un sub oppure no
        
        return createQuery(jpql.toString(), param);
    }
    //SIAC-8046 Task 2.2 CM 01/04/2021 Fine
}
