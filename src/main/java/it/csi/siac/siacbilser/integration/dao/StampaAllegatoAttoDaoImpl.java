/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacRAllegatoAttoStampaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoStampaFile;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegatoStampa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAllegatoStampaTipoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;


/**
 * The Class RegistroIvaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaAllegatoAttoDaoImpl extends JpaDao<SiacTAttoAllegatoStampa, Integer> implements StampaAllegatoAttoDao {
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.StampaAllegatpAttoDao#create(it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegatoStampa)
	 */
	@Override
	public SiacTAttoAllegatoStampa create(SiacTAttoAllegatoStampa s) {
		
		Date now = new Date();
		s.setDataModificaInserimento(now); 
		
		if(s.getSiacRAttoAllegatoStampaFiles()!=null){
			for(SiacRAttoAllegatoStampaFile file : s.getSiacRAttoAllegatoStampaFiles()){
				file.setDataModificaInserimento(now);
			}
		}
		
		if(s.getSiacRAllegatoAttoStampaStatos()!=null){
			for(SiacRAllegatoAttoStampaStato stato :s.getSiacRAllegatoAttoStampaStatos()){
				stato.setDataModificaInserimento(now);
			}
		}
		
		s.setUid(null);	
		super.save(s);
		return s;

	}
	
	@Override
	public Page<SiacTAttoAllegatoStampa> ricercaSinteticaStampaAllegatoAtto(Integer enteProprietarioId,
			SiacDAttoAllegatoStampaTipoEnum attoalstTipoCode,  Integer bilId,Integer attoammId,Date dataCreazione, Pageable pageable) {
		final String methodName = "ricercaStampaAllegatoAtto";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacTAttoAllegatoStampa s ");
		jpql.append(" WHERE ");
		jpql.append(" s.dataCancellazione IS NULL ");
		jpql.append(" AND s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(attoalstTipoCode!=null){
			jpql.append(" AND s.siacDAttoAllegatoStampaTipo.attoalstTipoCode = :attoalstTipoCode ");
			param.put("attoalstTipoCode", attoalstTipoCode.getCodice());
		}
		if(bilId != null){
			jpql.append(" AND s.siacTBil.bilId = :bilId");
			param.put("bilId", bilId);
		}
		if(attoammId != null){
			jpql.append(" AND s.siacTAttoAllegato.siacTAttoAmm.attoammId = :attoammId");
			param.put("attoammId", attoammId);
		}
		if(dataCreazione!=null){
			jpql.append(" AND " + Utility.toJpqlDateParamEquals("s.dataCreazione", ":dataCreazione"));
			param.put("dataCreazione", dataCreazione);
			}
		
		//ordinamento UTILIZZATO PRIMA DELLA CR-3560
//		jpql.append(" ORDER BY s.attoalstAnno, s.attoalstCode ");
		jpql.append(" ORDER BY s.dataModifica DESC ");
	
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);

	}

//	@Override
//	/public Page<SiacTAttoAllegatoStampa> ricercaStampaAllegatoAtto(Integer enteProprietarioId, String fileCode, Pageable pageable) {
//		
//		final String methodName = "ricercaStampaAllegatoAtto";
//		
//		StringBuilder jpql = new StringBuilder();
//		Map<String, Object> param = new HashMap<String, Object>();
//		
//		
//		jpql.append("FROM SiacTFile s ");
//		jpql.append(" WHERE ");
//		jpql.append(" s.dataCancellazione IS NULL ");
//		jpql.append(" AND s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
//		
//		param.put("enteProprietarioId", enteProprietarioId);
//		
//		jpql.append("AND" + Utility.toJpqlSearchLike("s.fileCode", "CONCAT('%', :fileCode, '%')") + " ");
//		param.put("fileCode", fileCode);
		
//		if(dataCreazione!=null){
//			jpql.append(" AND DATE_TRUNC('day', s.dataCreazione) = :dataCreazione ");
//			Calendar c = GregorianCalendar.getInstance();
//			c.setTime(dataCreazione);
//			c.set(Calendar.HOUR_OF_DAY, 0);
//			c.set(Calendar.MINUTE, 0);
//			c.set(Calendar.SECOND, 0);
//			c.set(Calendar.MILLISECOND, 0);
//			param.put("dataCreazione", c.getTime());
//		}
		
//		
//		jpql.append(" ORDER BY s.fileCode ");
//		
//		//log.debug(methodName, "JPQL to execute: " + jpql.toString());
//		
//		return getPagedList(jpql.toString(), param, pageable);
//	}
	
	

}
