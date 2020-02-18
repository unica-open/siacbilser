/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.provvedimento;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

// TODO: Auto-generated Javadoc
/**
 * The Class SiacTAttoAmmDaoImpl.
 */
@Component
@Primary
public class SiacTAttoAmmDaoImpl extends JpaDao<SiacTAttoAmm, Integer> implements SiacTAttoAmmDao {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.provvedimento.SiacTAttoAmmDao#create(it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm)
	 */
	@Override
	public SiacTAttoAmm create(SiacTAttoAmm attoAmm) {
		Date now = new Date();
		attoAmm.setDataModificaInserimento(now);
		attoAmm.setUid(null);
	
		super.save(attoAmm);
		
		entityManager.flush();
		
		findById(attoAmm.getUid());
		
		return attoAmm;
	}
	
	
//	@Override
//	@Deprecated
//	public SiacTAttoAmm updateProvvedimento(SiacTAttoAmm attoDaAggiornare, SiacDAttoAmmStato attoAmmStato, SiacTClass struttura,
//			SiacDAttoAmmTipo attoTipo, String loginOperazione, SiacTEnteProprietario siacTEnteProprietario, boolean eliminaStruttura) {
//		
//		log.debugStart("updateProvvedimento", attoDaAggiornare);
//		
//		SiacTAttoAmm attoSalvato = update(attoDaAggiornare);
//		
//		log.debugEnd("updateProvvedimento", attoDaAggiornare);
//		
//		return attoSalvato;
//	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	@Override
	public SiacTAttoAmm update(SiacTAttoAmm attoLegge) {
		Date now = new Date();
		attoLegge.setDataModificaInserimento(now);
		
		super.update(attoLegge);
		
		return attoLegge;
	}
	
	@Override
	public List<Integer> aggiornaStatoMovgestTs(Integer attoAmmId, String attoammStatCode, boolean isEsecutivo, String loginOperazione) {
		final String methodName = "aggiornaStatoMovgestTs";
		
		log.info(methodName, "Calling functionName: fnc_siac_atto_amm_aggiorna_stato_movgest for attoAmmId: "+ attoAmmId 
				+", attoammStatCode:  "+ attoammStatCode + ", isEsecutivo: "+isEsecutivo+", loginOperazione:"+loginOperazione);
		String sql = "SELECT * FROM fnc_siac_atto_amm_aggiorna_stato_movgest(:attoAmmId,:attoammStatCode,:isEsecutivo,:loginOperazione)";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("attoAmmId", attoAmmId);
		query.setParameter("attoammStatCode", attoammStatCode);
		query.setParameter("isEsecutivo", isEsecutivo);
		query.setParameter("loginOperazione", loginOperazione);
		
		@SuppressWarnings("unchecked")
		List<Integer> result = query.getResultList();
		log.debug(methodName, "returning result: "+result);
		
		return result;		
	}
	
	

	@Override
	public Boolean isAnnullabileAttoAmm(Integer attoAmmId) {
		
		final String methodName = "isAnnullabileAttoAmm";
		
		log.debug(methodName, "Calling functionName: fnc_siac_atto_amm_verifica_annullabilita for attoAmmId: "+ attoAmmId );
		String sql = "SELECT * FROM fnc_siac_atto_amm_verifica_annullabilita(:attoAmmId)";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("attoAmmId", attoAmmId);
		
		Boolean result = (Boolean) query.getSingleResult();
		log.debug(methodName, "returning result: "+result);
		
		return result;	
	}


	@Override
	public Long countByEnteProprietarioIdAttoammAnnoAttoammNumeroAttoammTipoIdClassifIdNotAttoammId(Integer enteProprietarioId, String attoammAnno, Integer attoammNumero, Integer attoammTipoId,
			Integer classifId, Integer attoammId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		sql.append(" SELECT COALESCE(COUNT(taa), 0) ")
			.append(" FROM SiacTAttoAmm taa ")
			.append(" WHERE taa.dataCancellazione IS NULL ")
			.append(" AND taa.attoammAnno = :attoammAnno ")
			.append(" AND taa.attoammNumero = :attoammNumero ")
			.append(" AND taa.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
			.append(" AND taa.siacDAttoAmmTipo.attoammTipoId = :attoammTipoId ")
			.append(" AND taa.attoammId <> :attoammId ");
		
		params.put("attoammAnno", attoammAnno);
		params.put("attoammNumero", attoammNumero);
		params.put("enteProprietarioId", enteProprietarioId);
		params.put("attoammTipoId", attoammTipoId);
		params.put("attoammId", attoammId);
		
		if(classifId == null) {
			sql.append(" AND NOT EXISTS ( ")
				.append("     FROM taa.siacRAttoAmmClasses raac ")
				.append("     WHERE raac.dataCancellazione IS NULL ")
				.append(" ) ");
		} else {
			sql.append(" AND EXISTS ( ")
				.append("     FROM taa.siacRAttoAmmClasses raac ")
				.append("     WHERE raac.dataCancellazione IS NULL ")
				.append("     AND raac.siacTClass.classifId = :classifId ")
				.append(" ) ");
			params.put("classifId", classifId);
		}
		
		Query query = createQuery(sql.toString(), params);
		
		return (Long) query.getSingleResult();
	}


	@Override
	public Page<SiacTAttoAmm> ricercaProvvedimento(Integer uidEnte, String anno, String oggetto, Integer numero,
			String note, Integer tipoAttoId, Integer uidStrutturaAmm, String statoOperativo, Boolean conDocumentoAssociato, Pageable pageable) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		jpql.append("FROM SiacTAttoAmm atto ");
		jpql.append(" WHERE atto.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		params.put("enteProprietarioId", uidEnte);
		
		if(StringUtils.isNotBlank(anno)){
			jpql.append(" AND atto.attoammAnno = :attoammAnno ");
			params.put("attoammAnno", anno);
		}
		
		if(StringUtils.isNotBlank(oggetto)){
			jpql.append(" AND ").append(Utility.toJpqlSearchLike("atto.attoammOggetto", "CONCAT('%', :attoammOggetto, '%')")).append(" ");
			params.put("attoammOggetto", oggetto);
		}
		
		if(numero != null){
			jpql.append(" AND atto.attoammNumero = :attoammNumero ");
			params.put("attoammNumero", numero);
		}
		
		if(StringUtils.isNotBlank(note)){
			jpql.append(" AND ").append(Utility.toJpqlSearchLike("atto.attoammNote", "CONCAT('%', :attoammNote, '%')")).append(" ");
			params.put("attoammNote", note);
		}
		
		if(tipoAttoId != null){
			jpql.append(" AND atto.siacDAttoAmmTipo.attoammTipoId = :attoammTipoId ");
			params.put("attoammTipoId", tipoAttoId);
		}
		
		if(uidStrutturaAmm != null){
			jpql.append(" AND EXISTS ( FROM atto.siacRAttoAmmClasses da "  );
			jpql.append(" 	WHERE da.siacTClass.classifId  = :uidStrutturaAmm ");
			jpql.append(" 	AND da.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			params.put("uidStrutturaAmm", uidStrutturaAmm);
		}
		
		if(StringUtils.isNotBlank(statoOperativo)){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM atto.siacRAttoAmmStatos raas ");
			jpql.append("     WHERE raas.siacDAttoAmmStato.attoammStatoDesc = :attoammStatoDesc ");
			jpql.append("     AND raas.dataCancellazione IS NULL ");
			jpql.append("     AND raas.siacDAttoAmmStato.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			params.put("attoammStatoDesc", statoOperativo);
		}
		
		if(Boolean.TRUE.equals(conDocumentoAssociato)){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM atto.siacRSubdocAttoAmms rs ");
			jpql.append("     WHERE rs.dataCancellazione IS NULL ");
			jpql.append(" ) ");
		}
		
		jpql.append(" ORDER BY atto.attoammAnno, atto.attoammNumero ");
		
		return getPagedList(jpql.toString(), params, pageable);
	}


	@Override
	public void annullaMovimentiGestioneCollegatiAllAttoAmm(Integer attoAmmId, String loginOperazione) {
		throw new UnsupportedOperationException();
	}
}
