/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconOperazStampa;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconStampaFile;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconStampaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRMovimentoStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampaValore;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconStampaStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconStampaTipoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SiacTCassaEconStampaDaoImpl extends JpaDao<SiacTCassaEconStampa, Integer> implements SiacTCassaEconStampaDao{

	
	@Override
	public SiacTCassaEconStampa create(SiacTCassaEconStampa stces) {
		Date now = new Date();
		stces.setDataModificaInserimento(now);
		
		if(stces.getSiacRCassaEconStampaFiles()!=null){
			for(SiacRCassaEconStampaFile file : stces.getSiacRCassaEconStampaFiles()){
				file.setDataModificaInserimento(now);
			}
		}
		
		if(stces.getSiacRCassaEconOperazStampas()!=null){
			for(SiacRCassaEconOperazStampa opSt : stces.getSiacRCassaEconOperazStampas()){
				opSt.setDataModificaInserimento(now);
			}
		}
		
		if(stces.getSiacRCassaEconStampaStatos()!=null){
			for(SiacRCassaEconStampaStato stato : stces.getSiacRCassaEconStampaStatos()){
				stato.setDataModificaInserimento(now);
			}
		}
		if(stces.getSiacRMovimentoStampas()!=null){
			for(SiacRMovimentoStampa mov : stces.getSiacRMovimentoStampas()){
				mov.setDataModificaInserimento(now);
			}
		}
		if(stces.getSiacTCassaEconStampaValores()!=null){
			for(SiacTCassaEconStampaValore val : stces.getSiacTCassaEconStampaValores()){
				val.setDataModificaInserimento(now);
			}
		}
		
		stces.setUid(null);	
		super.save(stces);
		return stces;

	}


	@Override
	public Page<SiacTCassaEconStampa> ricercaStampeCassaEconomale(
			Integer enteProprietarioId,
			Integer bilId,
			Integer cassaeconId,
			SiacDCassaEconStampaTipoEnum siacDCassaEconStampaTipoEnum,
			Date dataCreazione, 
			String fileName,
			SiacDCassaEconStampaStatoEnum siacDCassaEconStampaStatoEnum,
			Date gioUltimadatastampadef,
			Date gioUltimadatastampadefDa,
			Date gioUltimadatastampadefA,
			Date renPeriodoinizio,
			Date renPeriodofine,
			Date renData, 
			Integer renNum,
			Integer ricNummovimento,
			Pageable pageable) {
		
		final String methodName = "ricercaStampeCassaEconomale";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("FROM SiacTCassaEconStampa s ");
		jpql.append(" WHERE ");
		jpql.append(" s.dataCancellazione IS NULL ");
		jpql.append(" AND s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND s.siacTBil.bilId = :bilId ");
		jpql.append(" AND s.siacTCassaEcon.cassaeconId = :cassaeconId ");
		jpql.append(" AND s.siacDCassaEconStampaTipo.cestTipoCode = :cestTipoCode ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("bilId", bilId);
		param.put("cassaeconId", cassaeconId);
		param.put("cestTipoCode", siacDCassaEconStampaTipoEnum.getCodice());
		
		if(dataCreazione != null){
			jpql.append(" AND DATE_TRUNC('day', s.dataCreazione) = :dataCreazione ");
			
			Calendar c = GregorianCalendar.getInstance();
			c.setTime(dataCreazione);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			param.put("dataCreazione", c.getTime());
		}
		
		if(fileName!=null){
			jpql.append(" AND EXISTS( FROM s.siacRCassaEconStampaFiles sf ");
			jpql.append(" 			  WHERE sf.dataCancellazione IS NULL ");
			jpql.append("             AND sf.siacTFile.fileName = :fileName ");
			jpql.append(" 			) ");
			
			param.put("fileName", fileName);
		}
		
		if(siacDCassaEconStampaStatoEnum != null) {
			jpql.append(" AND EXISTS( FROM s.siacRCassaEconStampaStatos ss ");
			jpql.append(" 			  WHERE ss.dataCancellazione IS NULL ");
			jpql.append("             AND ss.siacDCassaEconStampaStato.cestStatoCode = :cestStatoCode  ");
			jpql.append(" 			) ");
			
			param.put("cestStatoCode", siacDCassaEconStampaStatoEnum.getCodice());
		}
		
		if(gioUltimadatastampadef != null) {
			jpql.append(" AND EXISTS( FROM s.siacTCassaEconStampaValores sv ");
			jpql.append(" 			  WHERE sv.dataCancellazione IS NULL ");
			jpql.append("             AND DATE_TRUNC('day', sv.gioUltimadatastampadef) = :gioUltimadatastampadef ");
			jpql.append(" 			) ");
			
			Calendar c = GregorianCalendar.getInstance();
			c.setTime(gioUltimadatastampadef);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			param.put("gioUltimadatastampadef", c.getTime());
		}
		
		if(gioUltimadatastampadefDa != null) {
			jpql.append(" AND EXISTS( FROM s.siacTCassaEconStampaValores sv ");
			jpql.append(" 			  WHERE sv.dataCancellazione IS NULL ");
			jpql.append("             AND DATE_TRUNC('day', sv.gioUltimadatastampadef) >= :gioUltimadatastampadefDa ");
			jpql.append(" 			) ");
			
			Calendar c = GregorianCalendar.getInstance();
			c.setTime(gioUltimadatastampadefDa);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			param.put("gioUltimadatastampadefDa", c.getTime());
		} 
		if(gioUltimadatastampadefA != null) {
			jpql.append(" AND EXISTS( FROM s.siacTCassaEconStampaValores sv ");
			jpql.append(" 			  WHERE sv.dataCancellazione IS NULL ");
			jpql.append("             AND DATE_TRUNC('day', sv.gioUltimadatastampadef) <= :gioUltimadatastampadefA ");
			jpql.append(" 			) ");
			
			Calendar c = GregorianCalendar.getInstance();
			c.setTime(gioUltimadatastampadefA);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			param.put("gioUltimadatastampadefA", c.getTime());
		}

		if(renPeriodoinizio != null) {
			jpql.append(" AND EXISTS( FROM s.siacTCassaEconStampaValores sv ");
			jpql.append(" 			  WHERE sv.dataCancellazione IS NULL ");
			jpql.append("             AND DATE_TRUNC('day', sv.renPeriodoinizio) = :renPeriodoinizio ");
			jpql.append(" 			) ");
			
			Calendar c = GregorianCalendar.getInstance();
			c.setTime(renPeriodoinizio);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			param.put("renPeriodoinizio", c.getTime());
		}
		
		
		if(renPeriodofine != null) {
			jpql.append(" AND EXISTS( FROM s.siacTCassaEconStampaValores sv ");
			jpql.append(" 			  WHERE sv.dataCancellazione IS NULL ");
			jpql.append("             AND DATE_TRUNC('day', sv.renPeriodofine) = :renPeriodofine ");
			jpql.append(" 			) ");
			
			Calendar c = GregorianCalendar.getInstance();
			c.setTime(renPeriodofine);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			param.put("renPeriodofine", c.getTime());
		}
		
		if(renData != null) {
			jpql.append(" AND EXISTS( FROM s.siacTCassaEconStampaValores sv ");
			jpql.append(" 			  WHERE sv.dataCancellazione IS NULL ");
			jpql.append("             AND DATE_TRUNC('day', sv.renData) = :renData ");
			jpql.append(" 			) ");
			
			Calendar c = GregorianCalendar.getInstance();
			c.setTime(renData);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			param.put("renData", c.getTime());
		}
		
		if(renNum != null && renNum.intValue() != 0) {
			jpql.append(" AND EXISTS( ");
			jpql.append("     FROM s.siacTCassaEconStampaValores sv ");
			jpql.append("     WHERE sv.dataCancellazione IS NULL ");
			jpql.append("     AND sv.renNum = :renNum ");
			jpql.append(" ) ");
			
			param.put("renNum", renNum);
		}
		
		if(ricNummovimento != null) {
			jpql.append(" AND EXISTS( FROM s.siacTCassaEconStampaValores sv ");
			jpql.append(" 			  WHERE sv.dataCancellazione IS NULL ");
			jpql.append("             AND sv.ricNummovimento = :ricNummovimento ");
			jpql.append(" 			) ");
			
			param.put("ricNummovimento", ricNummovimento);
		}
		
		//ordinamento UTILIZZATO PRIMA DELLA CR-3560
//		jpql.append(" ORDER BY s.cestAnno, s.cestCode ");
		jpql.append(" ORDER BY s.dataModifica DESC ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
		
	}

}
