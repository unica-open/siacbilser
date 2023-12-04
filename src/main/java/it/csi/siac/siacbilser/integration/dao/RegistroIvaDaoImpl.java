/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacRIvaRegistroGruppo;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistro;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;


// TODO: Auto-generated Javadoc
/**
 * The Class RegistroIvaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegistroIvaDaoImpl extends JpaDao<SiacTIvaRegistro, Integer> implements RegistroIvaDao {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.RegistroIvaDao#create(it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistro)
	 */
	public SiacTIvaRegistro create(SiacTIvaRegistro r){
		
		Date now = new Date();
		r.setDataModificaInserimento(now);
		
		if(r.getSiacRIvaRegistroGruppos()!=null){
			for(SiacRIvaRegistroGruppo gruppo : r.getSiacRIvaRegistroGruppos()){
				gruppo.setDataModificaInserimento(now);
			}
		}

		r.setUid(null);	
		super.save(r);
		return r;
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	public SiacTIvaRegistro update(SiacTIvaRegistro r){
	
		SiacTIvaRegistro rAttuale = this.findById(r.getUid());
	
		Date now = new Date();
		r.setDataModificaInserimento(now);
		
		//cancellazione elementi collegati	
		if(rAttuale.getSiacRIvaRegistroGruppos()!=null){
			for(SiacRIvaRegistroGruppo gruppo : rAttuale.getSiacRIvaRegistroGruppos()){
				gruppo.setDataCancellazioneIfNotSet(now);
			}
		}
		
		//inserimento elementi nuovi
		if(r.getSiacRIvaRegistroGruppos()!=null){
			for(SiacRIvaRegistroGruppo gruppo : r.getSiacRIvaRegistroGruppos()){
				gruppo.setDataModificaInserimento(now);
			}
		}
		
		super.update(r);
		return r;
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#delete(java.lang.Object)
	 */
	public void delete(SiacTIvaRegistro r){
		SiacTIvaRegistro registro = this.findById(r.getUid());
		
		Date now = new Date();
		registro.setDataCancellazioneIfNotSet(now);		
		
		//cancellazione elementi collegati	
		if(registro.getSiacRIvaRegistroGruppos()!=null){
			for(SiacRIvaRegistroGruppo gruppo : registro.getSiacRIvaRegistroGruppos()){
					gruppo.setDataCancellazioneIfNotSet(now);
			}
		}

	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.RegistroIvaDao#ricercaSinteticaRegistroIva(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<SiacTIvaRegistro> ricercaSinteticaRegistroIva(
			Integer enteProprietarioId, 
			Integer ivagruId,
			Integer ivaregTipoId,
			String ivaregCode,
			String ivaregDesc,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaRegistroIva";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaRegistroIva( jpql, param, enteProprietarioId, ivagruId, ivaregTipoId, ivaregCode, ivaregDesc);
		
		jpql.append(" ORDER BY ir.ivaregCode");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	/**
	 * Componi query ricerca sintetica registro iva.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param enteProprietarioId the ente proprietario id
	 * @param ivagruId the ivagru id
	 * @param ivaregTipoId the ivareg tipo id
	 * @param ivaregCode the ivareg code
	 * @param ivaregDesc the ivareg desc
	 */
	private void componiQueryRicercaSinteticaRegistroIva(StringBuilder jpql,
			Map<String, Object> param, Integer enteProprietarioId, Integer ivagruId, 
			Integer ivaregTipoId, String ivaregCode,String ivaregDesc) {
		
		jpql.append("FROM SiacTIvaRegistro ir ");
		jpql.append(" WHERE ");
		jpql.append(" ir.dataCancellazione IS NULL ");
		jpql.append(" AND ir.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(!StringUtils.isEmpty(ivaregCode)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("ir.ivaregCode", "CONCAT('%', :ivaregCode, '%')") + " ");
			param.put("ivaregCode", ivaregCode);			
		}
		
		if(!StringUtils.isEmpty(ivaregDesc)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("ir.ivaregDesc", "CONCAT('%', :ivaregDesc, '%')") + " ");
			param.put("ivaregDesc", ivaregDesc);			
		}
		
		if(ivagruId != null) {
				jpql.append(" AND EXISTS( ");
				jpql.append(" 	FROM ir.siacRIvaRegistroGruppos irg ");
				jpql.append("	WHERE irg.siacTIvaGruppo.ivagruId = :ivagruId ");
				jpql.append("	AND irg.dataCancellazione IS NULL ");
				jpql.append(" ) ");
				
				param.put("ivagruId", ivagruId);
		}
		
		if(ivaregTipoId != null) {
			jpql.append(" 	AND ir.siacDIvaRegistroTipo.ivaregTipoId = :ivaregTipoId");
			jpql.append("	AND ir.siacDIvaRegistroTipo.dataCancellazione IS NULL ");
			
			param.put("ivaregTipoId", ivaregTipoId);
	}
		

	}

	@Override
	public List<SiacTIvaRegistro> findByEnteProprietarioEGruppoETipo(Integer enteProprietarioId, Integer ivagruId, Integer ivaregTipoId, Collection<Integer> ivaattIds) {
		final String methodName = "findByEnteProprietarioEGruppoETipo";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("SELECT r ")
			.append(" FROM SiacTIvaRegistro r ")
			.append(" WHERE r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
			.append(" AND r.dataCancellazione IS NULL ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(ivagruId != null) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM r.siacRIvaRegistroGruppos rg ")
				.append("     WHERE rg.siacTIvaGruppo.ivagruId = :ivagruId ")
				.append("     AND rg.dataCancellazione IS NULL ")
				.append(" ) ");
			
			param.put("ivagruId", ivagruId);
		}
		if(ivaattIds != null && !ivaattIds.isEmpty()) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM r.siacRIvaRegistroGruppos rg, SiacTIvaGruppo tig ")
				.append("     WHERE rg.dataCancellazione IS NULL ")
				.append("     AND rg.siacTIvaGruppo = tig ")
				.append("     AND EXISTS ( ")
				.append("         FROM tig.siacRIvaGruppoAttivitas riga ")
				.append("         WHERE riga.dataCancellazione IS NULL ")
				.append("         AND riga.siacTIvaAttivita.ivaattId IN (:ivaattIds) ")
				.append("     ) ")
				.append(" ) ");
			
			param.put("ivaattIds", ivaattIds);
		}
		if(ivaregTipoId != null) {
			jpql.append(" AND r.siacDIvaRegistroTipo.ivaregTipoId = :ivaregTipoId ");
			
			param.put("ivaregTipoId", ivaregTipoId);
		}
		jpql.append(" ORDER BY r.ivaregCode ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<SiacTIvaRegistro> results = query.getResultList();
		
		return results;
	}

	
	
	
}
