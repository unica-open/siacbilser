/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconOperazStato;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconOperazTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconOperaz;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class CassaEconomaleDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OperazioneDiCassaDaoImpl extends JpaDao<SiacTCassaEconOperaz, Integer> implements OperazioneDiCassaDao {
	
	public SiacTCassaEconOperaz create(SiacTCassaEconOperaz oc){
		
		log.debug("create", oc.getDataInizioValidita());
		
		Date now = new Date();
		oc.setDataModificaInserimento(now, oc.getDataInizioValidita());	
		
		//inserimento elementi nuovi		
		if(oc.getSiacRCassaEconOperazTipos()!=null){
			for(SiacRCassaEconOperazTipo tipo : oc.getSiacRCassaEconOperazTipos()){
				tipo.setDataModificaInserimento(now);
			}
		}
		
		if(oc.getSiacRCassaEconOperazStatos()!=null){
			for(SiacRCassaEconOperazStato stato : oc.getSiacRCassaEconOperazStatos()){
				stato.setDataModificaInserimento(now);
			}
		}
		
		oc.setUid(null);		
		super.save(oc);
		return oc;
	}

	public SiacTCassaEconOperaz update(SiacTCassaEconOperaz oc){
		
		SiacTCassaEconOperaz ocAttuale = this.findById(oc.getUid());
		
		Date now = new Date();
		oc.setDataModificaAggiornamento(now, oc.getDataInizioValidita());	
		
		
		//cancellazione elementi collegati		
		if(ocAttuale.getSiacRCassaEconOperazTipos()!=null){
			for(SiacRCassaEconOperazTipo tipo: ocAttuale.getSiacRCassaEconOperazTipos()){
				tipo.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(ocAttuale.getSiacRCassaEconOperazStatos()!=null){
			for(SiacRCassaEconOperazStato stato: ocAttuale.getSiacRCassaEconOperazStatos()){
				stato.setDataCancellazioneIfNotSet(now);
			}
		}
		
		//inserimento elementi nuovi		
		if(oc.getSiacRCassaEconOperazTipos()!=null){
			for(SiacRCassaEconOperazTipo tipo : oc.getSiacRCassaEconOperazTipos()){
				tipo.setDataModificaInserimento(now);
			}
		}
		
		if(oc.getSiacRCassaEconOperazStatos()!=null){
			for(SiacRCassaEconOperazStato stato : oc.getSiacRCassaEconOperazStatos()){
				stato.setDataModificaInserimento(now);
			}
		}
		
		super.update(oc);
		return oc;
	}


	public SiacTCassaEconOperaz updateStatoOperativoOperazioneCassa(SiacTCassaEconOperaz oc){
		
		SiacTCassaEconOperaz ocAttuale = this.findById(oc.getUid());
		
		Date now = new Date();
		oc.setDataModificaAggiornamento(now, oc.getDataInizioValidita());	
		
		
		//cancellazione elementi collegati		
		
		if(ocAttuale.getSiacRCassaEconOperazStatos()!=null){
			for(SiacRCassaEconOperazStato stato: ocAttuale.getSiacRCassaEconOperazStatos()){
				stato.setDataCancellazioneIfNotSet(now);
			}
		}
		
		//inserimento elementi nuovi		
		
		if(oc.getSiacRCassaEconOperazStatos()!=null){
			for(SiacRCassaEconOperazStato stato : oc.getSiacRCassaEconOperazStatos()){
				stato.setDataModificaInserimento(now);
			}
		}
		
		super.update(oc);
		return oc;
	}



	@Override
	public Page<SiacTCassaEconOperaz> ricercaSinteticaOperazioneCassa(
			Integer bilId,
			Integer cassaeconId,
			Integer enteProprietarioId , 
			Date dataInizioValidita,
			Integer cassaeconopTipoId, 
			String cassaeconopStatoCode,
			List<String> cassaeconopStatoCodesDaescludere,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaOperazioneCassa";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaOperazioneCassa( jpql, param, bilId, cassaeconId, enteProprietarioId, dataInizioValidita, null, null, cassaeconopTipoId, cassaeconopStatoCode, cassaeconopStatoCodesDaescludere );
		
		jpql.append(" ORDER BY oc.cassaeconopNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	@Override
	public Page<SiacTCassaEconOperaz> ricercaSinteticaOperazioneCassaPerPeriodo(
			Integer bilId,
			Integer cassaeconId,
			Integer enteProprietarioId , 
			Date dataInizioPeriodo,
			Date dataFinePeriodo,
			Integer cassaeconopTipoId, 
			String cassaeconopStatoCode,
			List<String> cassaeconopStatoCodesDaescludere,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaOperazioneCassa";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaOperazioneCassa( jpql, param, bilId, cassaeconId, enteProprietarioId, null, dataInizioPeriodo, dataFinePeriodo,  cassaeconopTipoId, cassaeconopStatoCode,cassaeconopStatoCodesDaescludere );
		
		jpql.append(" ORDER BY oc.cassaeconopNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	private void componiQueryRicercaSinteticaOperazioneCassa(StringBuilder jpql,
			Map<String, Object> param, 
			Integer bilId,
			Integer cassaeconId,
			Integer enteProprietarioId,
			Date dataInizioValidita,
			Date dataInizioPeriodo,
			Date dataFinePeriodo,
			Integer cassaeconopTipoId, 
			String cassaeconopStatoCode,
			List<String> cassaeconopStatoCodesDaescludere) {
		
		jpql.append("FROM SiacTCassaEconOperaz oc ");
		jpql.append(" WHERE ");
		jpql.append(" oc.dataCancellazione IS NULL ");
		jpql.append(" AND oc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND oc.siacTCassaEcon.cassaeconId = :cassaeconId ");
		jpql.append(" AND oc.siacTBil.bilId = :bilId ");
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("cassaeconId", cassaeconId);
		param.put("bilId", bilId);
		
		if(cassaeconopTipoId != null){
			jpql.append(" AND EXISTS ( FROM oc.siacRCassaEconOperazTipos toc ");
			jpql.append("              WHERE toc.dataCancellazione IS NULL ");
			jpql.append("             AND toc.siacDCassaEconOperazTipo.cassaeconopTipoId = :cassaeconopTipoId ");
			jpql.append("             ) ");
			param.put("cassaeconopTipoId", cassaeconopTipoId);
		}
		
		if(cassaeconopStatoCode != null){
			jpql.append(" AND EXISTS ( FROM oc.siacRCassaEconOperazStatos soc ");
			jpql.append("              WHERE soc.dataCancellazione IS NULL ");
			jpql.append("             AND soc.siacDCassaEconOperazStato.cassaeconopStatoCode = :cassaeconopStatoCode ");
			jpql.append("             ) ");
			param.put("cassaeconopStatoCode", cassaeconopStatoCode);
		}
		//SIAC-7319
		if(cassaeconopStatoCodesDaescludere != null && !cassaeconopStatoCodesDaescludere.isEmpty()){
			jpql.append(" AND NOT EXISTS ( FROM oc.siacRCassaEconOperazStatos soc ");
			jpql.append("              WHERE soc.dataCancellazione IS NULL ");
			jpql.append("             AND soc.siacDCassaEconOperazStato.cassaeconopStatoCode IN (:cassaeconopStatoCodesDaescludere) ");
			jpql.append("             ) ");
			param.put("cassaeconopStatoCodesDaescludere", cassaeconopStatoCodesDaescludere);
		}
		
		if(dataInizioValidita != null){
			Date startDataInizioValidita = setMezzanotte(dataInizioValidita);
			Date endDataFineValidita = setMezzanotte(DateUtils.addDays(dataInizioValidita, 1));
			jpql.append(" AND oc.dataInizioValidita >= :startDataInizioValidita  AND oc.dataInizioValidita < :endDataFineValidita");
			param.put("startDataInizioValidita", startDataInizioValidita);
			param.put("endDataFineValidita", endDataFineValidita);
		}
		
		if(dataInizioPeriodo != null && dataFinePeriodo != null){
			Date startDataInizioValidita = setMezzanotte(dataInizioPeriodo);
			Date endDataFineValidita = setMezzanotte(DateUtils.addDays(dataFinePeriodo, 1));
			jpql.append(" AND oc.dataInizioValidita >= :startDataInizioValidita  AND oc.dataInizioValidita < :endDataFineValidita");
			param.put("startDataInizioValidita", startDataInizioValidita);
			param.put("endDataFineValidita", endDataFineValidita);
		}
		
	}

	private Date setMezzanotte(Date data) {
		data = DateUtils.setHours(data, 0);
		data = DateUtils.setMinutes(data, 0);
		data = DateUtils.setSeconds(data, 0);
		data = DateUtils.setMilliseconds(data, 0);
		return data;
	}
	

	@Override
	public Long contaOperazioniDiCassa(Date dataPeriodoInizio, Date dataPeriodoFine, Integer cassaEconomaleUid, Integer enteUid,Integer bilancioId) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		jpql.append("SELECT COALESCE(COUNT(oc), 0)");
		
		jpql.append("FROM SiacTCassaEconOperaz oc ");
		jpql.append(" WHERE oc.dataCancellazione IS NULL ");
		jpql.append(" AND oc.siacTEnteProprietario.enteProprietarioId = :enteUid ");
		jpql.append(" AND oc.siacTCassaEcon.cassaeconId = :cassaEconomaleUid ");
		jpql.append(" AND oc.siacTBil.bilId = :bilancioId ");
		jpql.append(" AND oc.dataInizioValidita >= :dataPeriodoInizio ");
		jpql.append(" AND oc.dataInizioValidita < :dataPeriodoFine ");
		
		jpql.append(" AND EXISTS ( FROM oc.siacRCassaEconOperazTipos toc ");
		jpql.append("              WHERE toc.dataCancellazione IS NULL ");
		jpql.append("             AND toc.siacDCassaEconOperazTipo.inrendiconto = 'T' ");
		jpql.append("             ) ");

		jpql.append(" AND EXISTS ( FROM oc.siacRCassaEconOperazStatos soc ");
		jpql.append("              WHERE soc.dataCancellazione IS NULL ");
		jpql.append("             AND soc.siacDCassaEconOperazStato.cassaeconopStatoCode <>'A' ");
		jpql.append("             ) ");

		
		
		param.put("cassaEconomaleUid", cassaEconomaleUid);
		param.put("enteUid", enteUid);
		param.put("bilancioId", bilancioId);
		param.put("dataPeriodoInizio", dataPeriodoInizio);
		param.put("dataPeriodoFine", dataPeriodoFine);
		
		Query query = createQuery(jpql.toString(), param);
		return (Long) query.getSingleResult();

	}
	
	

}
