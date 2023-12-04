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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacRIvaStampaFile;
import it.csi.siac.siacbilser.integration.entity.SiacRIvaStampaRegistro;
import it.csi.siac.siacbilser.integration.entity.SiacRIvaStampaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampaValore;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaRegistroTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaStampaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPeriodoTipoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;


/**
 * The Class RegistroIvaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaIvaDaoImpl extends JpaDao<SiacTIvaStampa, Integer> implements StampaIvaDao {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.RegistroIvaDao#create(it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistro)
	 */
	public SiacTIvaStampa create(SiacTIvaStampa s){
		
		Date now = new Date();
		s.setDataModificaInserimento(now);
		
		if(s.getSiacRIvaStampaFiles()!=null){
			for(SiacRIvaStampaFile file : s.getSiacRIvaStampaFiles()){
				file.setDataModificaInserimento(now);
			}
		}
		
		if(s.getSiacRIvaStampaRegistros()!=null){
			for(SiacRIvaStampaRegistro registro : s.getSiacRIvaStampaRegistros()){
				registro.setDataModificaInserimento(now);
			}
		}
		
		if(s.getSiacRIvaStampaStatos()!=null){
			for(SiacRIvaStampaStato stato :s.getSiacRIvaStampaStatos()){
				stato.setDataModificaInserimento(now);
			}
		}
		
		if(s.getSiacTIvaStampaValores()!=null){
			for(SiacTIvaStampaValore sv :s.getSiacTIvaStampaValores()){
				sv.setDataModificaInserimento(now);
			}
		}
		
		s.setUid(null);	
		super.save(s);
		return s;
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	public SiacTIvaStampa update(SiacTIvaStampa s){
	
		SiacTIvaStampa sAttuale = this.findById(s.getUid());
	
		Date now = new Date();
		s.setDataModificaInserimento(now);
		
		//cancello vecchi legami
		if(sAttuale.getSiacRIvaStampaFiles()!=null){
			for(SiacRIvaStampaFile file : s.getSiacRIvaStampaFiles()){
				file.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(sAttuale.getSiacRIvaStampaRegistros()!=null){
			for(SiacRIvaStampaRegistro registro : s.getSiacRIvaStampaRegistros()){
				registro.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(sAttuale.getSiacRIvaStampaStatos()!=null){
			for(SiacRIvaStampaStato stato :s.getSiacRIvaStampaStatos()){
				stato.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(sAttuale.getSiacTIvaStampaValores()!=null){
			for(SiacTIvaStampaValore sv :s.getSiacTIvaStampaValores()){
				sv.setDataCancellazioneIfNotSet(now);
			}
		}
		
		//inserisco nuovi legami
		if(s.getSiacRIvaStampaFiles()!=null){
			for(SiacRIvaStampaFile file : s.getSiacRIvaStampaFiles()){
				file.setDataModificaInserimento(now);
			}
		}
		
		if(s.getSiacRIvaStampaRegistros()!=null){
			for(SiacRIvaStampaRegistro registro : s.getSiacRIvaStampaRegistros()){
				registro.setDataModificaInserimento(now);
			}
		}
		
		if(s.getSiacRIvaStampaStatos()!=null){
			for(SiacRIvaStampaStato stato :s.getSiacRIvaStampaStatos()){
				stato.setDataModificaInserimento(now);
			}
		}
		
		if(s.getSiacTIvaStampaValores()!=null){
			for(SiacTIvaStampaValore sv :s.getSiacTIvaStampaValores()){
				sv.setDataModificaInserimento(now);
			}
		}
		
		super.update(s);
		return s;
		
	}

	@Override
	public Page<SiacTIvaStampa> ricercaSinteticaStampaIva(Integer enteProprietarioId,String flagpagati/*Boolean flagPagati*/, SiacDIvaStampaTipoEnum ivastTipoCode, Integer ivastAnno, Integer ivagruId,
			SiacDIvaRegistroTipoEnum ivaregTipoCode, String flagincassati/*Boolean flagincassati*/, Integer uidRegistro, String codiceRegistro, SiacDPeriodoTipoEnum periodo, String fileName, Date dataCreazione,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaStampaIva";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		
		jpql.append("FROM SiacTIvaStampa s ");
		jpql.append(" WHERE ");
		jpql.append(" s.dataCancellazione IS NULL ");
		jpql.append(" AND s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(ivastTipoCode!=null){
			jpql.append(" AND s.siacDIvaStampaTipo.ivastTipoCode = :ivastTipoCode ");
			param.put("ivastTipoCode", ivastTipoCode.getCodice());
		}
		
		if(ivastAnno!=null){
			jpql.append(" AND s.ivastAnno = :ivastAnno ");
			param.put("ivastAnno", ivastAnno);
		}
		
		if("S".equalsIgnoreCase(flagpagati)) {
		//if (flagPagati!=null && flagPagati==true){
			
			jpql.append(" AND EXISTS (FROM s.siacTIvaStampaValores r ");
			jpql.append("             WHERE r.flagpagati = :flagpagati ");
			jpql.append("             AND r.dataCancellazione IS NULL ");
			jpql.append("            )");

			//jpql.append(" AND s.ivastCode not like '%NonPagati' ");
			param.put("flagpagati", flagpagati);
			
		}
		
		if(ivagruId!=null) {
			jpql.append(" AND EXISTS (FROM s.siacRIvaStampaRegistros r, SiacTIvaRegistro reg ");
			jpql.append("             WHERE r.siacTIvaRegistro = reg ");
			jpql.append("             AND r.dataCancellazione IS NULL ");
			jpql.append("             AND EXISTS (FROM reg.siacRIvaRegistroGruppos rirg");
			jpql.append("                         WHERE rirg.dataCancellazione IS NULL  ");
			jpql.append("                         AND rirg.siacTIvaGruppo.ivagruId = :ivagruId  ");
			jpql.append("                        )");
			jpql.append("            )");
			
			param.put("ivagruId", ivagruId);
		}
		
		if(ivaregTipoCode!=null) {
			jpql.append(" AND EXISTS (FROM s.siacRIvaStampaRegistros r ");
			jpql.append("             WHERE r.siacTIvaRegistro.siacDIvaRegistroTipo.ivaregTipoCode = :ivaregTipoCode ");
			jpql.append("             AND r.dataCancellazione IS NULL ");
			jpql.append("            )");
			
			param.put("ivaregTipoCode", ivaregTipoCode.getCodice());
		}
		
		
		if("S".equalsIgnoreCase(flagincassati)) {
			jpql.append(" AND EXISTS (FROM s.siacTIvaStampaValores r ");
			jpql.append("             WHERE r.flagincassati = :flagincassati ");
			jpql.append("             AND r.dataCancellazione IS NULL ");
			jpql.append("            )");
			param.put("flagincassati", flagincassati);
			//jpql.append(" AND s.ivastCode not like '%NonIncassati' ");
		}
		
		
		if(uidRegistro!=null) {
			jpql.append(" AND EXISTS (FROM s.siacRIvaStampaRegistros r ");
			jpql.append("             WHERE r.siacTIvaRegistro.ivaregId = :uidRegistro ");
			jpql.append("             AND r.dataCancellazione IS NULL ");
			jpql.append("            )");
			
			param.put("uidRegistro", uidRegistro);
		}
		
		if(StringUtils.isNotBlank(codiceRegistro)) {
			jpql.append(" AND EXISTS (FROM s.siacRIvaStampaRegistros r ");
			jpql.append("             WHERE r.siacTIvaRegistro.ivaregCode = :ivaregCode ");
			jpql.append("             AND r.dataCancellazione IS NULL ");
			jpql.append("            )");
			
			param.put("ivaregCode", codiceRegistro);
		}
		
		if(periodo!=null) {
			jpql.append(" AND s.siacTPeriodo.siacDPeriodoTipo.periodoTipoCode = :periodoTipoCode ");
			param.put("periodoTipoCode", periodo.getCodice());
		}
		
		if(StringUtils.isNotBlank(fileName)){
			jpql.append(" AND EXISTS (FROM s.siacRIvaStampaFiles r ");
			jpql.append("             WHERE " + Utility.toJpqlSearchLike("r.siacTFile.fileName", "CONCAT('%', :fileName, '%')") + " ");
			jpql.append("             AND r.dataCancellazione IS NULL ");
			jpql.append("            )");
			
			param.put("fileName", fileName);
		}
		
		if(dataCreazione!=null){
			jpql.append(" AND DATE_TRUNC('day', s.dataCreazione) = :dataCreazione ");
			Calendar c = GregorianCalendar.getInstance();
			c.setTime(dataCreazione);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			param.put("dataCreazione", c.getTime());
		}
		
		//ordinamento UTILIZZATO PRIMA DELLA CR-3560
//		jpql.append(" ORDER BY s.ivastAnno, s.ivastCode ");
		jpql.append(" ORDER BY s.dataModifica DESC ");

		
		log.info(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
		
		
		
		
	}
	
}
