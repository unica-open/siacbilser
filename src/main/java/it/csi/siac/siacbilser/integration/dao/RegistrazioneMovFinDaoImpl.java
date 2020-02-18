/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.jpql.MovimentiFromAttoAmmJpqlEnum;
import it.csi.siac.siacbilser.integration.dao.jpql.MovimentiFromBilElemBySacJpqlEnum;
import it.csi.siac.siacbilser.integration.dao.jpql.MovimentiFromBilElemJpqlEnum;
import it.csi.siac.siacbilser.integration.dao.jpql.MovimentiFromMovgestJpqlEnum;
import it.csi.siac.siacbilser.integration.dao.jpql.MovimentiFromMovgestTsJpqlEnum;
import it.csi.siac.siacbilser.integration.dao.jpql.MovimentiFromSoggettoJpqlEnum;
import it.csi.siac.siacbilser.integration.entity.SiacREventoRegMovfin;
import it.csi.siac.siacbilser.integration.entity.SiacRRegMovfinStato;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEp;
import it.csi.siac.siacbilser.integration.entity.SiacTRegMovfin;
import it.csi.siac.siacbilser.integration.entity.enumeration.MovimentoJpqlEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRegMovFinStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;

/**
 * The Class RegistrazioneMovFinDaoImpl.
 * 
 * @author Valentina
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegistrazioneMovFinDaoImpl extends JpaDao<SiacTRegMovfin, Integer> implements RegistrazioneMovFinDao {
	
	@Override
	public SiacTRegMovfin create(SiacTRegMovfin regMovfin) {
		
		Date now = new Date();
		regMovfin.setDataModificaInserimento(now);
		
		if(regMovfin.getSiacREventoRegMovfins()!=null){
			for(SiacREventoRegMovfin r : regMovfin.getSiacREventoRegMovfins()){
				r.setDataModificaInserimento(now);
			}
		}

		if(regMovfin.getSiacRRegMovfinStatos()!=null){
			for(SiacRRegMovfinStato r : regMovfin.getSiacRRegMovfinStatos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(regMovfin.getSiacTMovEps()!=null){
			for(SiacTMovEp m : regMovfin.getSiacTMovEps()){
				m.setDataModificaInserimento(now);
			}
		}
		
		regMovfin.setUid(null);		
		super.save(regMovfin);
		return regMovfin;
	}
	
	@Override
	public SiacTRegMovfin update(SiacTRegMovfin regMovfin) {
		SiacTRegMovfin dAttuale = this.findById(regMovfin.getUid());
		
		Date now = new Date();
		regMovfin.setDataModificaAggiornamento(now);		
		
		//cancellazione elementi collegati		
		if(dAttuale.getSiacREventoRegMovfins()!=null){
			for(SiacREventoRegMovfin r : dAttuale.getSiacREventoRegMovfins()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}

		if(dAttuale.getSiacRRegMovfinStatos()!=null){
			for(SiacRRegMovfinStato r : dAttuale.getSiacRRegMovfinStatos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(dAttuale.getSiacTMovEps()!=null){
			for(SiacTMovEp m : dAttuale.getSiacTMovEps()){
				m.setDataCancellazioneIfNotSet(now);
			}
		}
		
		
		//inserimento elementi nuovi	
		if(regMovfin.getSiacREventoRegMovfins()!=null){
			for(SiacREventoRegMovfin r : regMovfin.getSiacREventoRegMovfins()){
				r.setDataModificaInserimento(now);
			}
		}

		if(regMovfin.getSiacRRegMovfinStatos()!=null){
			for(SiacRRegMovfinStato r : regMovfin.getSiacRRegMovfinStatos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(regMovfin.getSiacTMovEps()!=null){
			for(SiacTMovEp m : regMovfin.getSiacTMovEps()){
				m.setDataModificaInserimento(now);
			}
		}
		
		
		super.update(regMovfin);
		return regMovfin;
	}

	
	@Override
	public Page<SiacTRegMovfin> ricercaSinteticaRegistrazioneMovFin(
			Integer enteProprietarioId,
			Integer bilId,
			SiacDAmbitoEnum siacDAmbitoEnum,
			Integer eventoId,
			List<String> eventoCodes,
			SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum,
			Integer docId,
			List<Integer> campoPkId,
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA,
			SiacDRegMovFinStatoEnum siacDRegMovFinStatoEnum,
			Set<SiacDRegMovFinStatoEnum> siacDRegMovFinStatosEnumDaEscludere,
			Integer classifId,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaRegistrazioneMovFin";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaRegistrazioneMovFin(jpql, param, enteProprietarioId, bilId, siacDAmbitoEnum, eventoId, siacDCollegamentoTipoEnum, docId,
				dataRegistrazioneDa, dataRegistrazioneA, siacDRegMovFinStatoEnum, siacDRegMovFinStatosEnumDaEscludere, classifId);
		
		appendConditionEventoCodeIn(jpql, param, eventoCodes);
		appendConditionCampoPkIdIn(jpql, param, docId, campoPkId);
		
		jpql.append(" ORDER BY r.dataCreazione ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	
	@Override
	public Page<SiacTRegMovfin> ricercaSinteticaRegistrazioneMovFin(
			Integer enteProprietarioId,
			Integer bilId,
			SiacDAmbitoEnum siacDAmbitoEnum,
			Integer eventoId,
			Collection<String> eventoCodes,
			Map<Collection<String>, Collection<Integer>> pkIdsByEventoCodes,
			SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum,
			Integer docId,
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA,
			SiacDRegMovFinStatoEnum siacDRegMovFinStatoEnum,
			Set<SiacDRegMovFinStatoEnum> siacDRegMovFinStatosEnumDaEscludere,
			Integer classifId,
			Integer bilElemId,
			Integer soggettoId,
			Integer movgestId,
			Integer movgestTsId,
			//SIAC-5799		
			Integer attoAmmId,
			//SIAC-5944
			Integer idSac,
			Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnumList,
			Pageable pageable) {
		final String methodName = "ricercaSinteticaRegistrazioneMovFin";
		
		log.info(methodName, "attoAmmId passato "+ attoAmmId);

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaRegistrazioneMovFin(jpql, param, enteProprietarioId, bilId, siacDAmbitoEnum, eventoId, siacDCollegamentoTipoEnum, docId,
				dataRegistrazioneDa, dataRegistrazioneA, siacDRegMovFinStatoEnum, siacDRegMovFinStatosEnumDaEscludere, classifId);
		
		appendConditionEventoCodeIn(jpql, param, eventoCodes);
		appendConditionEventAndCampoPkId(pkIdsByEventoCodes, jpql, param);
		//SIAC-5290
		appendFilterBilElemOnCampoPkId(jpql, param, bilElemId, siacDCollegamentoTipoEnumList);
		appendFilterSoggettoOnCampoPkId(jpql, param, soggettoId, siacDCollegamentoTipoEnumList);

		appendFilterMovgestOnCampoPkId(jpql, param, movgestId, siacDCollegamentoTipoEnumList);
		appendFilterMovgestTsOnCampoPkId(jpql, param, movgestTsId, siacDCollegamentoTipoEnumList);
		
		appendFilterAttoAmmOnCampoPkId(jpql, param, attoAmmId, siacDCollegamentoTipoEnumList);
		//SIAC-5944
		appendFilterSacOnCampoPkId(jpql, param, idSac, siacDCollegamentoTipoEnumList);

		jpql.append(" ORDER BY r.dataCreazione ");
		
		log.info(methodName, "Ricerca Registazione JPQL to execute: " + jpql.toString());
		log.info(methodName, "Ricerca Registazione idSac: " + idSac);
		
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	/**
	 * Append filter movgest ts on campo pk id.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param movgestTsId the movgest ts id
	 * @param siacDCollegamentoTipoEnumList the siac D collegamento tipo enum
	 */
	private void appendFilterMovgestTsOnCampoPkId(StringBuilder jpql, Map<String, Object> param, Integer movgestTsId, Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnumList) {
		final String methodName ="appendFilterSoggettoOnCampoPkId";
		if(movgestTsId == null) {
			return;	
		}
		if(siacDCollegamentoTipoEnumList==null || siacDCollegamentoTipoEnumList.isEmpty()){
			log.warn(methodName, "Filtro non applicabile per movgestId=" + movgestTsId + " in quanto non posso dedurre il tipo di collegamento.");
			// non posso applicare il filtro
			return;
		}
		Set<MovimentiFromMovgestTsJpqlEnum> movimentiFromMovgestJpqlEnum = MovimentiFromMovgestTsJpqlEnum.toMovimentiFromBilElemJpqlEnum(siacDCollegamentoTipoEnumList);
		
		jpql.append(" AND ( ");
		List<String> chunks = new ArrayList<String>();
		
		int i = 1;
		for(MovimentiFromMovgestTsJpqlEnum rfbeje : movimentiFromMovgestJpqlEnum) {
			String paramNameCollegamentoTipoCodes = "collegamentoTipoCodes" + i;
			StringBuilder tmp = new StringBuilder();
			//NON LO SO, valutare se è da mettere o no.
			tmp.append(" ( re.siacDEvento.siacDCollegamentoTipo.collegamentoTipoCode IN (:").append(paramNameCollegamentoTipoCodes).append(") ");
			log.debug(methodName, "Aggiungo filtro per collegamento tipo: " + rfbeje.getSiacDCollegamentoTipoEnums() + " > "+ rfbeje + " paramName:" + paramNameCollegamentoTipoCodes);
			tmp.append("     AND re.campoPkId IN (").append(rfbeje.getJpql()).append(" ) ) ");
			
			chunks.add(tmp.toString());
			
			param.put(paramNameCollegamentoTipoCodes, rfbeje.getSiacDCollegamentoTipoCodes());
			i++;
		}
		jpql.append(StringUtils.join(chunks, " OR "));
		
		jpql.append(" ) ");
		
		param.put("movgestTsId", movgestTsId);

	}
	
	private void appendFilterMovgestOnCampoPkId(StringBuilder jpql, Map<String, Object> param, Integer movgestId, Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnumList) {
		final String methodName ="appendFilterSoggettoOnCampoPkId";
		if(movgestId == null) {
			return;	
		}
		if(siacDCollegamentoTipoEnumList==null || siacDCollegamentoTipoEnumList.isEmpty()){
			log.warn(methodName, "Filtro non applicabile per movgestId=" + movgestId + " in quanto non posso dedurre il tipo di collegamento.");
			// non posso applicare il filtro
			return;
		}
		Set<MovimentiFromMovgestJpqlEnum> movimentiFromMovgestJpqlEnum = MovimentiFromMovgestJpqlEnum.toMovimentiFromBilElemJpqlEnum(siacDCollegamentoTipoEnumList);
		
		jpql.append(" AND ( ");
		List<String> chunks = new ArrayList<String>();
		
		int i = 1;
		for(MovimentiFromMovgestJpqlEnum rfbeje : movimentiFromMovgestJpqlEnum) {
			String paramNameCollegamentoTipoCodes = "collegamentoTipoCodes" + i;
			StringBuilder tmp = new StringBuilder();
			//NON LO SO, valutare se è da mettere o no.
			tmp.append(" ( re.siacDEvento.siacDCollegamentoTipo.collegamentoTipoCode IN (:").append(paramNameCollegamentoTipoCodes).append(") ");
			log.debug(methodName, "Aggiungo filtro per collegamento tipo: " + rfbeje.getSiacDCollegamentoTipoEnums() + " > "+ rfbeje + " paramName:" + paramNameCollegamentoTipoCodes);
			tmp.append("     AND re.campoPkId IN (").append(rfbeje.getJpql()).append(" ) ) ");
			
			chunks.add(tmp.toString());
			
			param.put(paramNameCollegamentoTipoCodes, rfbeje.getSiacDCollegamentoTipoCodes());
			i++;
		}
		jpql.append(StringUtils.join(chunks, " OR "));
		
		jpql.append(" ) ");
		
		param.put("movgestId", movgestId);

	}

	private void appendFilterSoggettoOnCampoPkId(StringBuilder jpql, Map<String, Object> param, Integer soggettoId, Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnumList) {
		final String methodName ="appendFilterSoggettoOnCampoPkId";
		if(soggettoId == null) {
			return;	
		}
		if(siacDCollegamentoTipoEnumList==null || siacDCollegamentoTipoEnumList.isEmpty()){
			log.warn(methodName, "Filtro non applicabile per soggettoId=" + soggettoId + " in quanto non posso dedurre il tipo di collegamento.");
			// non posso applicare il filtro
			return;
		}
		Set<MovimentiFromSoggettoJpqlEnum> movimentiFromSoggettoJpqlEnum = MovimentiFromSoggettoJpqlEnum.toMovimentiFromBilElemJpqlEnum(siacDCollegamentoTipoEnumList);
		
		jpql.append(" AND ( ");
		List<String> chunks = new ArrayList<String>();
		
		int i = 1;
		for(MovimentiFromSoggettoJpqlEnum rfbeje : movimentiFromSoggettoJpqlEnum) {
			String paramNameCollegamentoTipoCodes = "collegamentoTipoCodes" + i;
			StringBuilder tmp = new StringBuilder();
			//NON LO SO, valutare se è da mettere o no.
			tmp.append(" ( re.siacDEvento.siacDCollegamentoTipo.collegamentoTipoCode IN (:").append(paramNameCollegamentoTipoCodes).append(") ");
			log.debug(methodName, "Aggiungo filtro per collegamento tipo: " + rfbeje.getSiacDCollegamentoTipoEnums() + " > "+ rfbeje + " paramName:" + paramNameCollegamentoTipoCodes);
			tmp.append("     AND re.campoPkId IN (").append(rfbeje.getJpql()).append(" ) ) ");
			
			chunks.add(tmp.toString());
			
			param.put(paramNameCollegamentoTipoCodes, rfbeje.getSiacDCollegamentoTipoCodes());
			i++;
		}
		jpql.append(StringUtils.join(chunks, " OR "));
		
		jpql.append(" ) ");
		
		param.put("soggettoId", soggettoId);
		
	}

	private void appendFilterBilElemOnCampoPkId(StringBuilder jpql, Map<String, Object> param, Integer bilElemId, Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnumList) {
		final String methodName ="appendFilterBilElemOnCampoPkId";
		if(bilElemId == null) {
			return;	
		}
		if(siacDCollegamentoTipoEnumList==null || siacDCollegamentoTipoEnumList.isEmpty()){
			log.warn(methodName, "Filtro non applicabile per bilElemId=" + bilElemId + " in quanto non posso dedurre il tipo di collegamento.");
			// non posso applicare il filtro
			return;
		}
		Set<MovimentiFromBilElemJpqlEnum> movimentiFromBilElemJpqlEnum = MovimentiFromBilElemJpqlEnum.toMovimentiFromBilElemJpqlEnum(siacDCollegamentoTipoEnumList);
		
		jpql.append(" AND ( ");
		List<String> chunks = new ArrayList<String>();
		
		int i = 1;
		for(MovimentiFromBilElemJpqlEnum rfbeje : movimentiFromBilElemJpqlEnum) {
			String paramNameCollegamentoTipoCodes = "collegamentoTipoCodes" + i;
			StringBuilder tmp = new StringBuilder();
			//NON LO SO, valutare se è da mettere o no.
			tmp.append(" ( re.siacDEvento.siacDCollegamentoTipo.collegamentoTipoCode IN (:").append(paramNameCollegamentoTipoCodes).append(") ");
			log.debug(methodName, "Aggiungo filtro per collegamento tipo: " + rfbeje.getSiacDCollegamentoTipoEnums() + " > "+ rfbeje + " paramName:" + paramNameCollegamentoTipoCodes);
			tmp.append(" AND   re.campoPkId IN (").append(rfbeje.getJpql()).append(" ) ) ");
			
			chunks.add(tmp.toString());
			
			param.put(paramNameCollegamentoTipoCodes, rfbeje.getSiacDCollegamentoTipoCodes());
			i++;
		}
		jpql.append(StringUtils.join(chunks, " OR "));
		
		jpql.append(" ) ");
		
		param.put("elemId", bilElemId);
		
	}

	//SIAC-5799
	private void appendFilterAttoAmmOnCampoPkId(StringBuilder jpql, Map<String, Object> param, Integer attoammId, Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnumList) {
		final String methodName ="appendFilterAttoAmmOnCampoPkId";
		
		if(attoammId == null) {
			return;	
		}
		if(siacDCollegamentoTipoEnumList==null || siacDCollegamentoTipoEnumList.isEmpty()){
			log.warn(methodName, "Filtro non applicabile per attoammId=" + attoammId + " in quanto non posso dedurre il tipo di collegamento.");
			// non posso applicare il filtro
			return;
		}
		
		Set<MovimentiFromAttoAmmJpqlEnum> movimentiFromAttoAmmJpqlEnum = MovimentiFromAttoAmmJpqlEnum.toMovimentiFromAttoAmmJpqlEnum(siacDCollegamentoTipoEnumList);
		
		jpql.append(" AND ( ");
		List<String> chunks = new ArrayList<String>();
		
		int i = 1;
		for(MovimentiFromAttoAmmJpqlEnum rfbeje : movimentiFromAttoAmmJpqlEnum) {
			String paramNameCollegamentoTipoCodes = "collegamentoTipoCodes" + i;
			StringBuilder tmp = new StringBuilder();
			//NON LO SO, valutare se è da mettere o no.
			tmp.append(" ( re.siacDEvento.siacDCollegamentoTipo.collegamentoTipoCode IN (:").append(paramNameCollegamentoTipoCodes).append(") ");
			log.info(methodName, "Aggiungo filtro per collegamento tipo: " + rfbeje.getSiacDCollegamentoTipoEnums() + " > "+ rfbeje + " paramName:" + paramNameCollegamentoTipoCodes);
			log.info(methodName, "rfbeje.getJpql(): " + rfbeje.getJpql());
			
			tmp.append(" AND   re.campoPkId IN (").append(rfbeje.getJpql()).append(" ) ) ");
			
			chunks.add(tmp.toString());
			
			param.put(paramNameCollegamentoTipoCodes, rfbeje.getSiacDCollegamentoTipoCodes());
			i++;
		}
		jpql.append(StringUtils.join(chunks, " OR "));
		
		jpql.append(" ) ");
		
		param.put("attoammId", attoammId);
		
	}

	//SIAC-5799
		private void appendFilterSacOnCampoPkId(StringBuilder jpql, Map<String, Object> param, Integer sacId, Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnumList) {
			final String methodName ="appendFilterSacOnCampoPkId";
			
			if(sacId == null) {
				return;	
			}
			if(siacDCollegamentoTipoEnumList==null || siacDCollegamentoTipoEnumList.isEmpty()){
				log.warn(methodName, "Filtro non applicabile per sacId=" + sacId + " in quanto non posso dedurre il tipo di collegamento.");
				// non posso applicare il filtro
				return;
			}
			
			Set<MovimentiFromBilElemBySacJpqlEnum> movimentiFromBilElemBySacJpqlEnum = MovimentiFromBilElemBySacJpqlEnum.toMovimentiFromBilElemBySacJpqlEnum(siacDCollegamentoTipoEnumList);
			
			jpql.append(" AND ( ");
			List<String> chunks = new ArrayList<String>();
			
			int i = 1;
			for(MovimentiFromBilElemBySacJpqlEnum rfbeje : movimentiFromBilElemBySacJpqlEnum) {
				String paramNameCollegamentoTipoCodes = "collegamentoTipoCodes" + i;
				StringBuilder tmp = new StringBuilder();
				//NON LO SO, valutare se è da mettere o no.
				tmp.append(" ( re.siacDEvento.siacDCollegamentoTipo.collegamentoTipoCode IN (:").append(paramNameCollegamentoTipoCodes).append(") ");
				log.info(methodName, "Aggiungo filtro per collegamento tipo: " + rfbeje.getSiacDCollegamentoTipoEnums() + " > "+ rfbeje + " paramName:" + paramNameCollegamentoTipoCodes);
				log.info(methodName, "rfbeje.getJpql(): " + rfbeje.getJpql());
				
				tmp.append(" AND   re.campoPkId IN (").append(rfbeje.getJpql()).append(" ) ) ");
				
				chunks.add(tmp.toString());
				
				param.put(paramNameCollegamentoTipoCodes, rfbeje.getSiacDCollegamentoTipoCodes());
				i++;
			}
			jpql.append(StringUtils.join(chunks, " OR "));
			
			jpql.append(" ) ");
			
			param.put("sacId", sacId);
			
		}


	
	private void appendConditionEventAndCampoPkId(Map<Collection<String>, Collection<Integer>> pkIdsByEventoCodes,
			StringBuilder jpql, Map<String, Object> param) {
		if(pkIdsByEventoCodes != null && !pkIdsByEventoCodes.isEmpty()) {
			jpql.append(" AND ( ");
			boolean first = true;
			int prog = 0;
			for(Entry<Collection<String>, Collection<Integer>> entry : pkIdsByEventoCodes.entrySet()) {
				if(!first) {
					jpql.append(" OR ");
				}
				jpql.append(" ( ");
				jpql.append("     re.siacDEvento.eventoCode IN (:eventoCodes_").append(prog).append(") ");
				jpql.append("     AND re.campoPkId IN (:campoPkId_").append(prog).append(") ");
				jpql.append(" ) ");
				
				param.put("eventoCodes_" + prog, entry.getKey());
				param.put("campoPkId_" + prog, entry.getValue());
				
				first = false;
				prog++;
			}
			
			jpql.append(" ) ");
		}
	}
	
	

	

	private void componiQueryRicercaSinteticaRegistrazioneMovFin(
			StringBuilder jpql, Map<String, Object> param,
			Integer enteProprietarioId, Integer bilId, SiacDAmbitoEnum siacDAmbitoEnum, Integer eventoId, SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum,
			Integer docId, Date dataRegistrazioneDa, Date dataRegistrazioneA,
			SiacDRegMovFinStatoEnum siacDRegMovFinStatoEnum, Set<SiacDRegMovFinStatoEnum> siacDRegMovFinStatosEnumDaEscludere, Integer classifId) {
		
		jpql.append(" SELECT DISTINCT r ");
		jpql.append(" FROM SiacTRegMovfin r, SiacREventoRegMovfin re ");
		jpql.append(" WHERE r.dataCancellazione IS NULL ");
		jpql.append(" AND re.dataCancellazione IS NULL ");
		jpql.append(" AND re.siacTRegMovfin = r ");
		jpql.append(" AND r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND r.siacTBil.bilId = :bilId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("bilId", bilId);
		
		if(siacDAmbitoEnum != null){
			jpql.append(" AND r.siacDAmbito.ambitoCode = :ambitoCode ");
			param.put("ambitoCode", siacDAmbitoEnum.getCodice());
		}
		
		if(eventoId != null && eventoId != 0) {
			jpql.append(" AND re.siacDEvento.eventoId = :eventoId ");
			param.put("eventoId", eventoId);
		}
		
		if(docId != null && docId != 0) {
			jpql.append(" AND ( ");
			jpql.append("     re.campoPkId IN ( ");
			jpql.append("         SELECT s.subdocId ");
			jpql.append("         FROM SiacTSubdoc s ");
			jpql.append("         WHERE s.siacTDoc.docId = :docId ");
			jpql.append("     ) ");
			jpql.append("     AND ( ");
			jpql.append("         re.siacDEvento.siacDCollegamentoTipo.collegamentoTipoCode IN ('").append(SiacDCollegamentoTipoEnum.SubdocumentoSpesa.getCodice()).append("', '").append(SiacDCollegamentoTipoEnum.SubdocumentoEntrata.getCodice()).append("') ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			
			param.put("docId", docId);
			
		} else { 
			if(siacDCollegamentoTipoEnum != null) {
				jpql.append(" AND re.siacDEvento.siacDCollegamentoTipo.collegamentoTipoCode = :collegamentoTipoCode ");
				param.put("collegamentoTipoCode", siacDCollegamentoTipoEnum.getCodice());
				
			}
		}
		
		if( classifId != null && classifId != 0){
			jpql.append(" AND r.siacTClass2.classifId = :classifId ");
			param.put("classifId", classifId);
		}
		
		if( dataRegistrazioneDa != null){
			jpql.append(" AND DATE_TRUNC('day', CAST(r.dataCreazione AS date)) >= DATE_TRUNC('day', CAST(:dataRegistrazioneDa AS date)) ");
			param.put("dataRegistrazioneDa", dataRegistrazioneDa);
		}
		
		if( dataRegistrazioneA != null){
			jpql.append(" AND  DATE_TRUNC('day', CAST(r.dataCreazione AS date)) <= DATE_TRUNC('day', CAST(:dataRegistrazioneA AS date)) ");
			param.put("dataRegistrazioneA", dataRegistrazioneA);
		}
		
		if( siacDRegMovFinStatoEnum != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM r.siacRRegMovfinStatos rs ");
			jpql.append("     WHERE rs.dataCancellazione IS NULL ");
			jpql.append("     AND rs.siacDRegMovfinStato.regmovfinStatoCode = :regmovfinStatoCode ");
			jpql.append(" ) ");
			param.put("regmovfinStatoCode", siacDRegMovFinStatoEnum.getCodice());
		}
		
		if( classifId != null && classifId != 0){
			jpql.append(" AND r.siacTClass2.classifId = :classifId ");
			param.put("classifId", classifId);
		}
		
		if( siacDRegMovFinStatosEnumDaEscludere != null && !siacDRegMovFinStatosEnumDaEscludere.isEmpty()){
			jpql.append(" AND NOT EXISTS ( ");
			jpql.append("     FROM r.siacRRegMovfinStatos rs ");
			jpql.append("     WHERE rs.dataCancellazione IS NULL ");
			jpql.append("     AND rs.siacDRegMovfinStato.regmovfinStatoCode IN (:regmovfinStatoCodeEscludere)  ");
			jpql.append(" ) ");
			param.put("regmovfinStatoCodeEscludere", SiacDRegMovFinStatoEnum.getCodici(siacDRegMovFinStatosEnumDaEscludere));
		}
		
	}

	private void appendConditionCampoPkIdIn(StringBuilder jpql, Map<String, Object> param, Integer docId, List<Integer> campoPkId) {
		if(docId != null && docId != 0) {
			return;
		}
		if(campoPkId != null && !campoPkId.isEmpty()) {
			jpql.append(" AND re.campoPkId IN (:campoPkId ) ");
			param.put("campoPkId", campoPkId);
		}
	}

	private void appendConditionEventoCodeIn(StringBuilder jpql, Map<String, Object> param, Collection<String> eventoCodes) {
		if(eventoCodes != null && !eventoCodes.isEmpty()){
			jpql.append(" AND re.siacDEvento.eventoCode IN (:eventoCodes )");
			param.put("eventoCodes", eventoCodes);
		}
	}
	
	
	public SiacTBase ricercaMovimentoById(String entity, String column, Integer campoPkId){
		final String methodName = "ricercaMovimentoById";
		
		String jpql = "FROM  " + entity 
					+ " WHERE " + column + " = :campoPkId";
		
		TypedQuery<SiacTBase> query = entityManager.createQuery(jpql, SiacTBase.class);
		query.setParameter("campoPkId", campoPkId);
		
		//SiacTBase queryResult = query.getSingleResult();
		List<SiacTBase> resultList = query.getResultList();
		if(resultList == null || resultList.isEmpty()) {
			return null;
		}
		if(resultList.size() > 1) {
			log.warn(methodName, "Too many results found for JPQL [" + jpql + "] (campoPkId = " + campoPkId + ", # of results " + resultList.size() + "). Only one was expected: returning the first");
		}

		return resultList.get(0);
	}



	@Override
	public Integer findIdMovimento(Integer enteProprietarioId, SiacDCollegamentoTipoEnum collegamentoTipoEnum, 
			Integer annoMovimento, String numeroMovimento, Integer numeroSubmovimento) {
		
		MovimentoJpqlEnum movimentoJpqlEnum = collegamentoTipoEnum.getMovimentoJpqlEnum();
		if(movimentoJpqlEnum==null) {
			throw new IllegalArgumentException("Il tipo di collegamento " + collegamentoTipoEnum
					+ " non supporta la ricerca per anno, numero e numero sub");
		}
		TypedQuery<SiacTBase> query = entityManager.createQuery(movimentoJpqlEnum.getJpql(), SiacTBase.class);
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("annoMovimento", annoMovimento);
		query.setParameter("numeroMovimento", movimentoJpqlEnum.toNumeroMovimentoType(numeroMovimento));
		
		if(movimentoJpqlEnum.getNumeroSubmovimentoType()!=null) {
			query.setParameter("numeroSubmovimento", movimentoJpqlEnum.toNumeroSubmovimentoType(numeroSubmovimento));
		}
		
		//Prima c'era questo, in cui mancavano le if per ModificaMovimentoGestioneSpesa/Entrata,PrimaNotaPura, RichiestaEconomale,RendicontoRichiesta:
		
//		if(SiacDCollegamentoTipoEnum.DocumentoSpesa.equals(collegamentoTipoEnum) || SiacDCollegamentoTipoEnum.DocumentoEntrata.equals(collegamentoTipoEnum)){
//			
//			query.setParameter("numeroMovimento", numeroMovimento);
//		}else if(SiacDCollegamentoTipoEnum.SubdocumentoEntrata.equals(collegamentoTipoEnum) || SiacDCollegamentoTipoEnum.SubdocumentoSpesa.equals(collegamentoTipoEnum)) {
//			query.setParameter("numeroMovimento", numeroMovimento);
//			if(numeroSubmovimento != null && numeroSubmovimento!= 0){
//				query.setParameter("numeroSubmovimento", numeroSubmovimento);
//			} else {
//				query.setParameter("numeroSubmovimento", null);
//			}
//		}else if(SiacDCollegamentoTipoEnum.SubImpegno.equals(collegamentoTipoEnum) || SiacDCollegamentoTipoEnum.SubAccertamento.equals(collegamentoTipoEnum)) {
//			query.setParameter("numeroMovimento", new BigDecimal(numeroMovimento));
//			
//			if(numeroSubmovimento != null && numeroSubmovimento!= 0){
//				query.setParameter("numeroSubmovimento", numeroSubmovimento.toString());
//			} else {
//				query.setParameter("numeroSubmovimento", "");
//			}
//		}else{
//			query.setParameter("numeroMovimento", new BigDecimal(numeroMovimento));
//		}
		try{
			SiacTBase queryResult = query.getSingleResult();
			return queryResult.getUid();
		} catch(NoResultException nre){
			throw new IllegalArgumentException("Nessun movimento di tipo "+collegamentoTipoEnum.name()+
					" trovato per anno "+annoMovimento +" e numero "+ numeroMovimento, nre);
		} catch(NonUniqueResultException nure){
			throw new IllegalArgumentException("Trovato più di un movimento di tipo "+collegamentoTipoEnum.name()+
					" per anno "+annoMovimento +" e numero "+ numeroMovimento, nure);
		}
	}
	
	@Override
	public List<Integer> findListaIdMovimento(Integer enteProprietarioId, SiacDCollegamentoTipoEnum collegamentoTipoEnum, 
			Integer annoMovimento, String numeroMovimento, Integer numeroSubmovimento) {
		MovimentoJpqlEnum movimentoJpqlEnum = collegamentoTipoEnum.getMovimentoJpqlEnum();
		if(movimentoJpqlEnum==null) {
			throw new IllegalArgumentException("Il tipo di collegamento " + collegamentoTipoEnum
					+ " non supporta la ricerca per anno, numero e numero sub");
		}
		TypedQuery<SiacTBase> query = entityManager.createQuery(movimentoJpqlEnum.getJpql(), SiacTBase.class);
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("annoMovimento", annoMovimento);
		
		query.setParameter("numeroMovimento", movimentoJpqlEnum.toNumeroMovimentoType(numeroMovimento));
	
		if(movimentoJpqlEnum.getNumeroSubmovimentoType()!=null) {
			query.setParameter("numeroSubmovimento", movimentoJpqlEnum.toNumeroSubmovimentoType(numeroSubmovimento));
		}
	
		//Prima c'era questo, in cui mancavano le if per ModificaMovimentoGestioneSpesa/Entrata,PrimaNotaPura, RichiestaEconomale,RendicontoRichiesta:
		
//		if(SiacDCollegamentoTipoEnum.DocumentoSpesa.equals(collegamentoTipoEnum) || SiacDCollegamentoTipoEnum.DocumentoEntrata.equals(collegamentoTipoEnum)){
//			query.setParameter("numeroMovimento", numeroMovimento);
//		}else if(SiacDCollegamentoTipoEnum.SubdocumentoEntrata.equals(collegamentoTipoEnum) || SiacDCollegamentoTipoEnum.SubdocumentoSpesa.equals(collegamentoTipoEnum)) {
//			query.setParameter("numeroMovimento", numeroMovimento);
//			if(numeroSubmovimento != null && numeroSubmovimento!= 0){
//				query.setParameter("numeroSubmovimento", numeroSubmovimento);
//			} else {
//				query.setParameter("numeroSubmovimento", null);
//			}
//		}else if(SiacDCollegamentoTipoEnum.SubImpegno.equals(collegamentoTipoEnum) || SiacDCollegamentoTipoEnum.SubAccertamento.equals(collegamentoTipoEnum)
//				|| SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa.equals(collegamentoTipoEnum) || SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata.equals(collegamentoTipoEnum)) {
//			query.setParameter("numeroMovimento", new BigDecimal(numeroMovimento));
//			
//			if(numeroSubmovimento != null && numeroSubmovimento!= 0){
//				query.setParameter("numeroSubmovimento", numeroSubmovimento.toString());
//			} else {
//				query.setParameter("numeroSubmovimento", "");
//			}
//		}else{
//			query.setParameter("numeroMovimento", new BigDecimal(numeroMovimento));
//		}
		
		List<SiacTBase> queryResult = query.getResultList();
		List<Integer> result = new ArrayList<Integer>();
		if(queryResult == null || queryResult.isEmpty()){
			return result;
		}
		for(SiacTBase t : queryResult){
			result.add(t.getUid());
		}
		return result;
		
	}

	@Override
	public List<SiacTRegMovfin> ricercaRegistrazionByPrimaNota(Integer uidPrimaNota, Set<SiacDRegMovFinStatoEnum> siacDRegMovFinStatosEnumDaEscludere) {
		final String methodName = "ricercaRegistrazionByPrimaNota";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		jpql.append(" SELECT tr ");
		jpql.append(" FROM SiacTRegMovfin tr, SiacTMovEp tme ");
		jpql.append(" WHERE tr.dataCancellazione IS NULL ");
		jpql.append(" AND tme.siacTRegMovfin = tr ");
		jpql.append(" AND tme.dataCancellazione IS NULL ");
		jpql.append(" AND tme.siacTPrimaNota.pnotaId = :pnotaId  ");
		param.put("pnotaId", uidPrimaNota);
		
		if( siacDRegMovFinStatosEnumDaEscludere != null && !siacDRegMovFinStatosEnumDaEscludere.isEmpty()){
			jpql.append(" AND NOT EXISTS ( ");
			jpql.append("     FROM SiacRRegMovfinStato rs ");
			jpql.append("     WHERE rs.dataCancellazione IS NULL ");
			jpql.append("     AND rs.siacTRegMovfin = tr ");			
			jpql.append("     AND rs.siacDRegMovfinStato.regmovfinStatoCode IN (:regmovfinStatoCodeEscludere)  ");
			jpql.append(" ) ");
			param.put("regmovfinStatoCodeEscludere", SiacDRegMovFinStatoEnum.getCodici(siacDRegMovFinStatosEnumDaEscludere));
		}
		
		jpql.append(" ORDER BY tr.dataCreazione ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<SiacTRegMovfin> queryResult = query.getResultList();
		return queryResult;
	}


}
