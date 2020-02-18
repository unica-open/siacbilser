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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsProgramma;
import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaClass;
import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProgrammaStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

// TODO: Auto-generated Javadoc
/**
 * Implementazione del DAO per il Progetto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/02/2014
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProgettoDaoImpl extends JpaDao<SiacTProgramma, Integer> implements ProgettoDao {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.ProgettoDao#create(it.csi.siac.siacbilser.integration.entity.SiacTProgramma)
	 */
	@Override
	public SiacTProgramma create(SiacTProgramma p) {
		Date now = new Date();
		p.setDataModificaInserimento(now);
		
		// Inserisco ex novo le varie SiacR*
		if(p.getSiacRMovgestTsProgrammas() != null){
			for(SiacRMovgestTsProgramma siacRMovgestTsProgramma : p.getSiacRMovgestTsProgrammas()){
				siacRMovgestTsProgramma.setDataModificaInserimento(now);
			}
		}
		
		if(p.getSiacRProgrammaAttoAmms() != null){
			for(SiacRProgrammaAttoAmm siacRProgrammaAttoAmm : p.getSiacRProgrammaAttoAmms()){
				siacRProgrammaAttoAmm.setDataModificaInserimento(now);
			}
		}
		
		if(p.getSiacRProgrammaAttrs() != null){
			for(SiacRProgrammaAttr siacRProgrammaAttr : p.getSiacRProgrammaAttrs()){
				siacRProgrammaAttr.setDataModificaInserimento(now);
			}
		}
		
		if(p.getSiacRProgrammaClasses() != null){
			for(SiacRProgrammaClass siacRProgrammaClass : p.getSiacRProgrammaClasses()){
				siacRProgrammaClass.setDataModificaInserimento(now);
			}
		}
		
		if(p.getSiacRProgrammaStatos() != null){
			for(SiacRProgrammaStato siacRProgrammaStato : p.getSiacRProgrammaStatos()){
				siacRProgrammaStato.setDataModificaInserimento(now);
			}
		}
		
		p.setUid(null);		
		super.save(p);
		return p;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	@Override
	public SiacTProgramma update(SiacTProgramma p) {
		SiacTProgramma pAttuale = this.findById(p.getUid());
		
		Date now = new Date();
		p.setDataModificaAggiornamento(now);		
		
		// Cancellazione elementi collegati con il Cascade
		if(pAttuale.getSiacRProgrammaAttoAmms() != null){
			for(SiacRProgrammaAttoAmm siacRProgrammaAttoAmm : pAttuale.getSiacRProgrammaAttoAmms()){				
				siacRProgrammaAttoAmm.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(pAttuale.getSiacRProgrammaAttrs() != null){
			for(SiacRProgrammaAttr siacRProgrammaAttr : pAttuale.getSiacRProgrammaAttrs()){
				siacRProgrammaAttr.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(pAttuale.getSiacRProgrammaClasses() != null){
			for(SiacRProgrammaClass siacRProgrammaClass : pAttuale.getSiacRProgrammaClasses()){
				siacRProgrammaClass.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(pAttuale.getSiacRProgrammaStatos() != null){
			for(SiacRProgrammaStato siacRProgrammaStato : pAttuale.getSiacRProgrammaStatos()){
				siacRProgrammaStato.setDataCancellazioneIfNotSet(now);
			}
		}
		
		// Inserimento nuove relazioni
		if(p.getSiacRProgrammaAttoAmms() != null){
			for(SiacRProgrammaAttoAmm siacRProgrammaAttoAmm : p.getSiacRProgrammaAttoAmms()){
				siacRProgrammaAttoAmm.setDataModificaInserimento(now);
			}
		}
		
		if(p.getSiacRProgrammaAttrs() != null){
			for(SiacRProgrammaAttr siacRProgrammaAttr : p.getSiacRProgrammaAttrs()){
				siacRProgrammaAttr.setDataModificaInserimento(now);
			}
		}
		
		if(p.getSiacRProgrammaClasses() != null){
			for(SiacRProgrammaClass siacRProgrammaClass : p.getSiacRProgrammaClasses()){
				siacRProgrammaClass.setDataModificaInserimento(now);
			}
		}
		
		if(p.getSiacRProgrammaStatos() != null){
			for(SiacRProgrammaStato siacRProgrammaStato : p.getSiacRProgrammaStatos()){
				siacRProgrammaStato.setDataModificaInserimento(now);
			}
		}
		
		super.update(p);
		return p;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.ProgettoDao#ricercaSinteticaProgetto(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Boolean, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProgrammaStatoEnum, java.lang.String, java.lang.Integer, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<SiacTProgramma> ricercaSinteticaProgetto(Integer enteProprietarioId, String codiceProgetto, TipoProgetto tipoProgetto, Integer tipoAmbitoId, Integer sacId, Boolean flagRilevanteFPV,
			SiacDProgrammaStatoEnum statoProgetto, String descrizioneProgetto, Integer attoAmministrativoId, Date dataIndizioneGara,Date dataAggiudicazioneGara,Boolean investimentoInCorsoDiDefinizione,String annoBil,Pageable pageable) {
		final String methodName = "ricercaSinteticaProgetto";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("FROM SiacTProgramma p ");
		jpql.append(" WHERE ");
		jpql.append(" p.dataCancellazione IS NULL ");
		jpql.append(" AND p.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(StringUtils.isNotBlank(codiceProgetto)) {
			jpql.append(" AND " + Utility.toJpqlSearchLike("p.programmaCode", "CONCAT('%', :programmaCode, '%')"));
			param.put("programmaCode", codiceProgetto);			
		}

		if (tipoProgetto != null) {
			jpql.append(" AND p.siacDProgrammaTipo.programmaTipoCode=:programmaTipoCode ");
			param.put("programmaTipoCode", tipoProgetto.getCodice());			
		}
		
		if (dataIndizioneGara != null){
			jpql.append(" AND "+Utility.toJpqlDateParamEquals( "p.programmaDataGaraIndizione",":programmaDataGaraIndizione"));
			param.put("programmaDataGaraIndizione", dataIndizioneGara);			
		}
		
		if(dataAggiudicazioneGara != null){
			jpql.append(" AND "+Utility.toJpqlDateParamEquals( "p.programmaDataGaraAggiudicazione",":programmaDataGaraAggiudicazione"));
			param.put("programmaDataGaraAggiudicazione", dataAggiudicazioneGara);			
		}
		if(investimentoInCorsoDiDefinizione != null) {			
			jpql.append(" AND p.investimentoInDefinizione= :investimentoInDefinizione ");
			param.put("investimentoInDefinizione", investimentoInCorsoDiDefinizione);
		}
		
		if (annoBil != null){
			jpql.append(" AND p.siacTBil.siacTPeriodo.anno= :annoBil ");			
			param.put("annoBil", annoBil);
		}
		
		if(tipoAmbitoId != null) {
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM p.siacRProgrammaClasses pc1 ");
			jpql.append("	WHERE pc1.siacTClass.classifId = :tipoAmbitoId ");
			jpql.append("	AND pc1.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("tipoAmbitoId", tipoAmbitoId);
		}
		
		if(sacId != null) {
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM p.siacRProgrammaClasses pc2 ");
			jpql.append("	WHERE pc2.siacTClass.classifId = :sacId ");
			jpql.append("	AND pc2.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("sacId", sacId);
		}
		
		if(flagRilevanteFPV != null) {
			jpql.append(" AND EXISTS( ");
			jpql.append("	FROM p.siacRProgrammaAttrs pa ");
			jpql.append("	WHERE pa.siacTAttr.attrCode = '" + SiacTAttrEnum.FlagRilevanteFondoPluriennaleVincolato.getCodice() +"' ");
			jpql.append(" 	AND pa.dataCancellazione IS NULL ");
			jpql.append(" 	AND pa.boolean_ = :flagRilevanteFondoPluriennaleVincolato ");
			jpql.append(" )");
			
			param.put("flagRilevanteFondoPluriennaleVincolato", Boolean.TRUE.equals(flagRilevanteFPV) ? "S" : "N");
		}
		
		if(statoProgetto != null) {
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM p.siacRProgrammaStatos ps ");
			jpql.append("	WHERE ps.siacDProgrammaStato.programmaStatoCode = :programmaStatoCode ");
			jpql.append("	AND ps.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("programmaStatoCode", statoProgetto.getCodice());
		}
		
		if(StringUtils.isNotBlank(descrizioneProgetto)) {
			jpql.append(" AND (" + Utility.toJpqlSearchLike("p.programmaDesc", "CONCAT('%', :programmaDesc, '%')" ) +" ) ");			
			param.put("programmaDesc", descrizioneProgetto);
		}
		
		if(attoAmministrativoId != null && attoAmministrativoId != 0) {
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM p.siacRProgrammaAttoAmms paa ");
			jpql.append("	WHERE paa.siacTAttoAmm.attoammId = :attoammId ");
			jpql.append("	AND paa.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("attoammId", attoAmministrativoId);
		}
		jpql.append(" ORDER BY p.programmaCode ");
		
		String jpqlString = jpql.toString();
		log.debug(methodName, jpqlString);
		
		return getPagedList(jpqlString, param, pageable);
	}
	

	@Override
	public List<Object[]> calcoloFpvSpesa(Integer uidProgetto, String anno) {
		return calcoloFpv(uidProgetto, anno, "fnc_siac_fpv_spesa");
	}
	
	@Override
	public List<Object[]> calcoloFpvEntrata(Integer uidProgetto, String anno) {
		return calcoloFpv(uidProgetto, anno, "fnc_siac_fpv_entrata");
	}
	
	@Override
	public List<Object[]> calcoloFpvTotale(Integer uidProgetto, String anno) {
		return calcoloFpv(uidProgetto, anno, "fnc_siac_fpv_totale");
	}

	/**
	 * Richiama le 3 function native per il calcolo 
	 * dell'FPV: fnc_siac_fpv_spesa, fnc_siac_fpv_entrata e fnc_siac_fpv_totale.
	 *
	 * @param uidProgetto the uid progetto
	 * @param anno the anno
	 * @param functionName the function name
	 * @return the list
	 */
	private List<Object[]> calcoloFpv(Integer uidProgetto, String anno, String functionName) {
		final String methodName = "calcoloFpv";
		
		log.debug(methodName, "Calling functionName: "+ functionName + " for uidProgetto: "+ uidProgetto +", anno:  "+ anno);
		String sql = "SELECT * FROM "+ functionName + "(:uidProgetto,:anno)";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("uidProgetto", uidProgetto);
		query.setParameter("anno", anno);		
		
		@SuppressWarnings("unchecked")
		List<Object[]> result = (List<Object[]>) query.getResultList();
		return result;
	}

	/**
	 * 
	 */
	@Override
	public List<Object[]> calcoloProspettoRiassuntivoCronoprogrammaDiGestione(Integer uidProgetto, String anno) {
		
		final String methodName ="calcoloProspettoRiassuntivoCronoprogrammaDiGestione";

		 String functionName="fnc_siac_cronoprogramma_spesa_entrata";
		
		log.debug(methodName, "Calling functionName: "+ functionName + " for uidProgetto: "+ uidProgetto +", anno:  "+ anno);
		String sql = "SELECT * FROM "+ functionName + "(:uidProgetto,:anno)";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("uidProgetto", uidProgetto);
		query.setParameter("anno", anno);		
		
		@SuppressWarnings("unchecked")
		List<Object[]> result = (List<Object[]>) query.getResultList();
		return result;
	}
	
	

}
