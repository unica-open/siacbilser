/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacRIvaGruppoAttivita;
import it.csi.siac.siacbilser.integration.entity.SiacRIvaGruppoChiusura;
import it.csi.siac.siacbilser.integration.entity.SiacRIvaGruppoProrata;
import it.csi.siac.siacbilser.integration.entity.SiacRIvaRegistroGruppo;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaGruppo;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class GruppoAttivitaIvaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GruppoAttivitaIvaDaoImpl extends JpaDao<SiacTIvaGruppo, Integer> implements GruppoAttivitaIvaDao {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.GruppoAttivitaIvaDao#create(it.csi.siac.siacbilser.integration.entity.SiacTIvaGruppo)
	 */
	@Override
	public SiacTIvaGruppo create(SiacTIvaGruppo g){
		
		Date now = new Date();
		g.setDataModificaInserimento(now);
		
		if(g.getSiacRIvaGruppoAttivitas()!=null){
			for(SiacRIvaGruppoAttivita att : g.getSiacRIvaGruppoAttivitas()){
				att.setDataModificaInserimento(now);
			}
		}

		if(g.getSiacRIvaGruppoChiusuras()!=null){
			for(SiacRIvaGruppoChiusura chiusura : g.getSiacRIvaGruppoChiusuras()){
				chiusura.setDataModificaInserimento(now);
			}
		}
		
		if(g.getSiacRIvaGruppoProratas()!=null){
			for(SiacRIvaGruppoProrata prorata : g.getSiacRIvaGruppoProratas()){
				prorata.setDataModificaInserimento(now);
			}
		}
		
//		if(g.getSiacRIvaRegistroGruppos()!=null){
//			for(SiacRIvaRegistroGruppo registro : g.getSiacRIvaRegistroGruppos()){
//				registro.setDataModificaInserimento(now);
//			}
//		}
		
		g.setUid(null);	
		super.save(g);
		return g;
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	@Override
	public SiacTIvaGruppo update(SiacTIvaGruppo g){
		Integer anno = null;
		for(SiacRIvaGruppoChiusura siacRIvaGruppoChiusura : g.getSiacRIvaGruppoChiusuras()) {
			anno = siacRIvaGruppoChiusura.getIvagruchitipoAnno();
		}
	
		SiacTIvaGruppo gAttuale = this.findById(g.getUid());
	
		Date now = new Date();
		g.setDataModificaInserimento(now);
		
		//cancellazione elementi collegati	
		if(gAttuale.getSiacRIvaGruppoAttivitas()!=null){
			for(SiacRIvaGruppoAttivita att : gAttuale.getSiacRIvaGruppoAttivitas()){
				att.setDataCancellazioneIfNotSet(now);
			}
		}

		if(gAttuale.getSiacRIvaGruppoChiusuras()!=null){
			for(SiacRIvaGruppoChiusura chiusura : gAttuale.getSiacRIvaGruppoChiusuras()){
				if(anno == null || anno.equals(chiusura.getIvagruchitipoAnno())) {
					chiusura.setDataCancellazioneIfNotSet(now);
				}
			}
		}
		
//		if(gAttuale.getSiacRIvaGruppoProratas()!=null){
//			for(SiacRIvaGruppoProrata prorata : gAttuale.getSiacRIvaGruppoProratas()){
//				if(anno == null || anno.equals(prorata.getIvagruproAnno())) {
//					prorata.setDataCancellazioneIfNotSet(now);
//				}
//			}
//		}
		
//		if(gAttuale.getSiacRIvaRegistroGruppos()!=null){
//			for(SiacRIvaRegistroGruppo registro : gAttuale.getSiacRIvaRegistroGruppos()){
//				registro.setDataCancellazioneIfNotSet(now);
//			}
//		}
		
		
		//inserimento elementi nuovi
		if(g.getSiacRIvaGruppoAttivitas()!=null){
			for(SiacRIvaGruppoAttivita att : g.getSiacRIvaGruppoAttivitas()){
				att.setDataModificaInserimento(now);
			}
		}

		if(g.getSiacRIvaGruppoChiusuras()!=null){
			for(SiacRIvaGruppoChiusura chiusura : g.getSiacRIvaGruppoChiusuras()){
				chiusura.setDataModificaInserimento(now);
			}
		}
		
//		if(g.getSiacRIvaGruppoProratas()!=null){
//			for(SiacRIvaGruppoProrata prorata : g.getSiacRIvaGruppoProratas()){
//				prorata.setDataModificaInserimento(now);
//			}
//		}
		
//		if(g.getSiacRIvaRegistroGruppos()!=null){
//			for(SiacRIvaRegistroGruppo registro : g.getSiacRIvaRegistroGruppos()){
//				registro.setDataModificaInserimento(now);
//			}
//		}
			
		super.update(g);
		return g;
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#delete(java.lang.Object)
	 */
	@Override
	public void delete(SiacTIvaGruppo g){
		SiacTIvaGruppo gruppo = this.findById(g.getUid());
		
		Date now = new Date();
		gruppo.setDataCancellazioneIfNotSet(now);		
		
		//cancellazione elementi collegati	
				if(gruppo.getSiacRIvaGruppoAttivitas()!=null){
					for(SiacRIvaGruppoAttivita att : gruppo.getSiacRIvaGruppoAttivitas()){
						att.setDataCancellazioneIfNotSet(now);
					}
				}

				if(gruppo.getSiacRIvaGruppoChiusuras()!=null){
					for(SiacRIvaGruppoChiusura chiusura : gruppo.getSiacRIvaGruppoChiusuras()){
						chiusura.setDataCancellazioneIfNotSet(now);
					}
				}
				
				if(gruppo.getSiacRIvaGruppoProratas()!=null){
					for(SiacRIvaGruppoProrata prorata : gruppo.getSiacRIvaGruppoProratas()){
						prorata.setDataCancellazioneIfNotSet(now);
						prorata.getSiacTIvaProrata().setDataCancellazione(now);
					}
				}
				
				if(gruppo.getSiacRIvaRegistroGruppos()!=null){
					for(SiacRIvaRegistroGruppo registro : gruppo.getSiacRIvaRegistroGruppos()){
						registro.setDataCancellazioneIfNotSet(now);
					}
				}
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.GruppoAttivitaIvaDao#ricercaSinteticaGruppoAttivitaIva(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<SiacTIvaGruppo> ricercaSinteticaGruppoAttivitaIva(
			Integer enteProprietarioId, String ivagruCode ,String ivagruDesc, String ivagruTipoCode, Pageable pageable) {
		
		final String methodName = "ricercaSinteticaGruppoAttivitaIva";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaGruppoAttivitaIva( jpql, param, enteProprietarioId, ivagruCode, ivagruDesc, ivagruTipoCode);
		
		jpql.append(" ORDER BY ig.ivagruCode");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	/**
	 * Componi query ricerca sintetica gruppo attivita iva.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param enteProprietarioId the ente proprietario id
	 * @param ivagruproAnno the ivagrupro anno
	 * @param ivagruCode the ivagru code
	 * @param ivagruDesc the ivagru desc
	 * @param ivagruTipoCode the ivagru tipo code
	 */
	private void componiQueryRicercaSinteticaGruppoAttivitaIva(StringBuilder jpql,
			Map<String, Object> param, Integer enteProprietarioId,
			String ivagruCode, String ivagruDesc, String ivagruTipoCode) {
		
		jpql.append("FROM SiacTIvaGruppo ig ");
		jpql.append(" WHERE ");
		jpql.append(" ig.dataCancellazione IS NULL ");
		jpql.append(" AND ig.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(!StringUtils.isEmpty(ivagruCode)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("ig.ivagruCode", "CONCAT('%', :ivagruCode, '%')") + " ");
			param.put("ivagruCode", ivagruCode);			
		}
		
		if(!StringUtils.isEmpty(ivagruDesc)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("ig.ivagruDesc", "CONCAT('%', :ivagruDesc, '%')") + " ");
			param.put("ivagruDesc", ivagruDesc);			
		}
		
		if(!StringUtils.isEmpty(ivagruTipoCode)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("ig.siacDIvaGruppoTipo.ivagruTipoCode", "CONCAT('%', :ivagruTipoCode, '%')") + " ");
			param.put("ivagruTipoCode", ivagruTipoCode);			
		}
		
	}	
	
	
	
}
