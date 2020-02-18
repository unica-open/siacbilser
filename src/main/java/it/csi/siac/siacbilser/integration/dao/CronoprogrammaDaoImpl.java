/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.ArrayList;
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
import it.csi.siac.siacbilser.integration.entity.SiacRCronopAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacRCronopAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRCronopStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCronop;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVincoloTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;


// TODO: Auto-generated Javadoc
/**
 * The Class CronoprogrammaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CronoprogrammaDaoImpl extends JpaDao<SiacTCronop, Integer> implements CronoprogrammaDao {

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.CronoprogrammaDao#create(it.csi.siac.siacbilser.integration.entity.SiacTCronop)
	 */
	public SiacTCronop create(SiacTCronop c) {
		Date now = new Date();
		c.setDataModificaInserimento(now);
		
		if(c.getSiacRCronopStatos()!=null){
			for(SiacRCronopStato stato : c.getSiacRCronopStatos()){
				stato.setDataModificaInserimento(now);
			}
		}

		if(c.getSiacRCronopAttrs()!=null){
			for(SiacRCronopAttr attrs : c.getSiacRCronopAttrs()){
				attrs.setDataModificaInserimento(now);
			}
		}	
		
		if(c.getSiacRCronopAttoAmms()!=null){
			for(SiacRCronopAttoAmm attoamms : c.getSiacRCronopAttoAmms()){
				attoamms.setDataModificaInserimento(now);
			}
		}	
		
		c.setUid(null);		
		super.save(c);
		return c;
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	public SiacTCronop update(SiacTCronop c) {
		
		SiacTCronop cAttuale = this.findById(c.getUid());
		
		Date now = new Date();
		c.setDataModificaAggiornamento(now);		
		
		//cancellazione elementi collegati
		if(cAttuale.getSiacRCronopStatos()!=null){
			for(SiacRCronopStato stato : cAttuale.getSiacRCronopStatos()){				
				stato.setDataCancellazioneIfNotSet(now);
			}
		}
	
		if(cAttuale.getSiacRCronopAttrs()!=null){
			for(SiacRCronopAttr attr : cAttuale.getSiacRCronopAttrs()){
				attr.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(cAttuale.getSiacRCronopAttoAmms()!=null){
			for(SiacRCronopAttoAmm attoamms : cAttuale.getSiacRCronopAttoAmms()){
				attoamms.setDataCancellazioneIfNotSet(now);
			}
		}	
		
		//inserimento elementi nuovi
		if(c.getSiacRCronopStatos()!=null){
			for(SiacRCronopStato stato : c.getSiacRCronopStatos()){
				stato.setDataModificaInserimento(now);
			}
		}
		
		if(c.getSiacRCronopAttrs()!=null){
			for(SiacRCronopAttr attr : c.getSiacRCronopAttrs()){
				attr.setDataModificaInserimento(now);
			}
		}
		
		if(c.getSiacRCronopAttoAmms()!=null){
			for(SiacRCronopAttoAmm attoamms : c.getSiacRCronopAttoAmms()){
				attoamms.setDataModificaInserimento(now);
			}
		}
		
		super.update(c);
		return c;
		
	}

	
	/**
	 * Ricerca sintetica vincolo capitoli.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param codiceVincolo the codice vincolo
	 * @param tipoVincolo the tipo vincolo
	 * @param descrizioneVincolo the descrizione vincolo
	 * @param flagTrasferimentiVincolatiVincolo the flag trasferimenti vincolati vincolo
	 * @param uidCapitolo the uid capitolo
	 * @param tipoCapitolo the tipo capitolo
	 * @param tipiCapitolo the tipi capitolo
	 * @param annoCapitolo the anno capitolo
	 * @param bilancioAnnoCapitolo the bilancio anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<SiacTCronop> ricercaSinteticaVincoloCapitoli(Integer enteProprietarioId, String codiceVincolo, SiacDVincoloTipoEnum tipoVincolo,
			String descrizioneVincolo, Boolean flagTrasferimentiVincolatiVincolo, Integer uidCapitolo, SiacDBilElemTipoEnum tipoCapitolo, List<SiacDBilElemTipoEnum> tipiCapitolo,
			String annoCapitolo, String bilancioAnnoCapitolo, String numeroCapitolo, String numeroArticolo, String numeroUEB, Pageable pageable) {

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("FROM SiacTVincolo v ");
		jpql.append("WHERE ");
		jpql.append("v.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append("AND v.dataCancellazione IS NULL ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(!StringUtils.isEmpty(codiceVincolo)){
			jpql.append(" AND v.vincoloCode = :vincoloCode ");
			param.put("vincoloCode", codiceVincolo);			
		}
		
		if(!StringUtils.isEmpty(descrizioneVincolo)) {
			jpql.append(" AND "+Utility.toJpqlSearchLike("v.vincoloDesc", "CONCAT('%',:vincoloDesc,'%')" ) +" ) ");			
			param.put("vincoloDesc", descrizioneVincolo);
		}
		
		if(tipoVincolo!=null) {
			jpql.append(" AND v.siacDVincoloTipo.vincoloTipoCode = :vincoloTipoCode ");			
			param.put("vincoloTipoCode", tipoVincolo.getCodice());
		}
		
		if(flagTrasferimentiVincolatiVincolo != null) {
			jpql.append(" AND EXISTS( FROM v.siacRVincoloAttrs attr WHERE  attr.siacTAttr.attrCode = '" + SiacTAttrEnum.FlagTrasferimentiVincolati.getCodice() +"' ");
			jpql.append(" AND attr.dataCancellazione IS NULL ");
			jpql.append(" AND attr.boolean_ = :flagTrasferimentiVincolatiVincolo )");
			param.put("flagTrasferimentiVincolatiVincolo", Boolean.TRUE.equals(flagTrasferimentiVincolatiVincolo)?"S":"N");
		}
		
		
		
		appendCapitoloFilter(jpql, param, uidCapitolo, tipoCapitolo, tipiCapitolo, annoCapitolo, bilancioAnnoCapitolo, numeroCapitolo, numeroArticolo,
				numeroUEB);
		
		jpql.append(" ORDER BY v.vincoloCode ");
		
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	/**
	 * Appende alla query i filtri sul capitolo associato al vincolo.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param uidCapitolo the uid capitolo
	 * @param tipoCapitolo the tipo capitolo
	 * @param tipiCapitolo the tipi capitolo
	 * @param annoCapitolo the anno capitolo
	 * @param bilancioAnnoCapitolo the bilancio anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 */
	private void appendCapitoloFilter(StringBuilder jpql, Map<String, Object> param, Integer uidCapitolo, SiacDBilElemTipoEnum tipoCapitolo,
			List<SiacDBilElemTipoEnum> tipiCapitolo, String annoCapitolo, String bilancioAnnoCapitolo, String numeroCapitolo,
			String numeroArticolo, String numeroUEB) {
		
		if (uidCapitolo == null && tipoCapitolo == null && (tipiCapitolo == null || tipiCapitolo.isEmpty()) && annoCapitolo == null
				&& bilancioAnnoCapitolo == null && numeroCapitolo == null && numeroArticolo == null && numeroUEB == null) {
			return;
		}
		
		
		jpql.append(" AND EXISTS( FROM v.siacRVincoloBilElems c WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND c.dataCancellazione IS NULL ");
		
		if(uidCapitolo != null && uidCapitolo != 0) {
			jpql.append(" AND c.siacTBilElem.elemId = :uidCapitolo ");
			param.put("uidCapitolo", uidCapitolo);
			jpql.append("  ) ");
			return; // se c'è l'uid del capitolo gli altri parametri non sono più necessari.
		}
		
		if(tipoCapitolo != null) {
			if(tipiCapitolo==null){
				tipiCapitolo = new ArrayList<SiacDBilElemTipoEnum>();
			}
			
			tipiCapitolo.add(tipoCapitolo);
			
		}
		
		if(tipiCapitolo != null && !tipiCapitolo.isEmpty()) {			
				jpql.append(" AND c.siacTBilElem.siacDBilElemTipo.elemTipoCode IN (:codiciTipoCapitolo) ");
				param.put("codiciTipoCapitolo", toStringArray(tipiCapitolo));			
		}
		
		if(annoCapitolo==null) {
			annoCapitolo = bilancioAnnoCapitolo;
		}
		
		if(annoCapitolo != null) {
			jpql.append(" AND c.siacTBilElem.siacTBil.siacTPeriodo.anno = :annoCapitolo ");
			param.put("annoCapitolo", annoCapitolo);
		}
		
		if(numeroCapitolo != null) {
			jpql.append(" AND c.siacTBilElem.elemCode = :numeroCapitolo ");
			param.put("numeroCapitolo", numeroCapitolo);
		}
		
		if(numeroArticolo != null) {
			jpql.append(" AND c.siacTBilElem.elemCode2 = :numeroArticolo ");
			param.put("numeroArticolo", numeroArticolo);
		}
		
		if(numeroUEB != null) {
			jpql.append(" AND c.siacTBilElem.elemCode3 = :numeroUEB ");
			param.put("numeroUEB", numeroUEB);
		}
		
		
		jpql.append("  ) ");
	}

	/**
	 * To string array.
	 *
	 * @param tipiCapitolo the tipi capitolo
	 * @return the list
	 */
	private List<String> toStringArray(List<SiacDBilElemTipoEnum> tipiCapitolo) {
		List<String> result = new ArrayList<String>();
		for (SiacDBilElemTipoEnum tipoCapitolo : tipiCapitolo) {
			result.add(tipoCapitolo.getCodice());
		}		
		return result;
	}
	
	public List<Object[]> calcoloFpvEntrataPrevisione(Integer uidCronoprogramma){
		return calcoloFpv(uidCronoprogramma, "fnc_siac_fpv_entrata_previsione");
	}
	
	public List<Object[]> calcoloFpvEntrataGestione(Integer uidCronoprogramma){
		return calcoloFpv(uidCronoprogramma, "fnc_siac_fpv_entrata_gestione");
	}

	public List<Object[]> calcoloFpvSpesaPrevisione(Integer uidCronoprogramma){
		return calcoloFpv(uidCronoprogramma, "fnc_siac_fpv_spesa_previsione");
	}
	
	public List<Object[]> calcoloFpvSpesaGestione(Integer uidCronoprogramma){
		return calcoloFpv(uidCronoprogramma, "fnc_siac_fpv_spesa_gestione");
	}

	/**
	 * Richiama le 2 function native per il calcolo 
	 * dell'FPV: fnc_siac_fpv_spesa_previsione, fnc_siac_fpv_entrata_previsione
	 *
	 * @param uidProgetto the uid progetto
	 * @param anno the anno
	 * @param functionName the function name
	 * @return the list
	 */
	private List<Object[]> calcoloFpv(Integer uidCronoprogramma,  String functionName) {
		final String methodName = "calcoloFpv";
		
		log.debug(methodName, "Calling functionName: "+ functionName + " for uidCronoprogramma: "+ uidCronoprogramma );
		String sql = "SELECT * FROM "+ functionName + "(:uidCronoprogramma)";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("uidCronoprogramma", uidCronoprogramma);
		//query.setParameter("anno", anno);		
		
		@SuppressWarnings("unchecked")
		List<Object[]> result = (List<Object[]>) query.getResultList();
		return result;
	}
	
	/**
	 * Ricerca cronoprogramma by progetto.
	 *
	 * @param programmaId the programma id
	 * @param programmaCode the programma code
	 * @param programmaTipoCode the programma tipo code
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	public List<SiacTCronop> ricercaCronoprogrammaByProgetto(Integer programmaId, String programmaCode, String programmaTipoCode,String anno, Integer enteProprietarioId){
		final String methodName ="ricercaCronoprogrammaByProgetto";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		jpql.append(" SELECT c FROM SiacTCronop c ");
		jpql.append("WHERE c.dataCancellazione IS NULL ");
		jpql.append(" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId");
		params.put("enteProprietarioId", enteProprietarioId);
		if(programmaId != null && programmaId.intValue()!= 0) {
			jpql.append(" AND c.siacTProgramma.programmaId = :programmaId ");
			params.put("programmaId", programmaId);
		}else {
			appendFilterSiacTProgramma(jpql, params,programmaCode);
			appendFilterSiacDProgrammaTipo(jpql, params,programmaTipoCode);
			appendFilterAnno(jpql, params,anno);
		}
		
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		Query query = createQuery(jpql.toString(), params);
		
		@SuppressWarnings("unchecked")
		List<SiacTCronop> result = (List<SiacTCronop>) query.getResultList();
		return result;
	}

	private void appendFilterAnno(StringBuilder jpql, Map<String, Object> params, String anno) {
		if(StringUtils.isBlank(anno)) {
			return;
		}
		jpql.append(" AND c.siacTBil.siacTPeriodo.anno = :anno ");
		params.put("anno", anno);
		
	}

	private void appendFilterSiacDProgrammaTipo(StringBuilder jpql, Map<String, Object> params, String programmaTipoCode) {
		if(StringUtils.isBlank(programmaTipoCode)) {
			return;
		}
		jpql.append(" AND c.siacTProgramma.siacDProgrammaTipo.programmaTipoCode = :programmaTipoCode ");
		params.put("programmaTipoCode", programmaTipoCode);
	}

	private void appendFilterSiacTProgramma(StringBuilder jpql, Map<String, Object> params, String programmaCode) {
		if(StringUtils.isBlank(programmaCode)) {
			return;
		}
		jpql.append(" AND c.siacTProgramma.programmaCode = :programmaCode ");
		params.put("programmaCode", programmaCode);
		
	}

}
