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

import it.csi.siac.siacbilser.integration.entity.SiacRQuadroEconomicoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTQuadroEconomico;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDQuadroEconomicoParteEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDQuadroEconomicoStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class ContoDaoImpl.
 * 
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class QuadroEconomicoDaoImpl extends JpaDao<SiacTQuadroEconomico, Integer> implements QuadroEconomicoDao {
	
	public SiacTQuadroEconomico create(SiacTQuadroEconomico e){
		Date now = new Date();
		Date dataInizioValidita = e.getDataInizioValidita();
		
		e.setDataModificaInserimento(now, dataInizioValidita);
		
		setDataModificaInserimento(e, now, dataInizioValidita);
		
		
		e.setUid(null);		
		super.save(e);
		return e;
	}


	public SiacTQuadroEconomico update(SiacTQuadroEconomico e){
		
		SiacTQuadroEconomico eAttuale = this.findById(e.getUid());
		
		Date now = new Date();
		Date dataInizioValidita = e.getDataInizioValidita();

		e.setDataModifica(now);
		
		//cancellazione elementi collegati	
		setDataFineValiditaEDataCancellazioneSeNelPeriodoDiValidita(eAttuale, now, dataInizioValidita);
		
		entityManager.flush();
		
		setDataModificaInserimento(e, now, dataInizioValidita);
		
		super.update(e);
		return e;
	}
	
	@Override
	public Page<SiacTQuadroEconomico> ricercaSinteticaQuadroEconomico(Integer uidEnte, String codice, String descrizione, SiacDQuadroEconomicoStatoEnum siacDQuadroEconomicoStatoEnum,SiacDQuadroEconomicoParteEnum siacDQuadroEconomicoparteEnum, Pageable pageable){
		return ricercaSinteticaQuadroEconomico(uidEnte, codice, descrizione, 0, siacDQuadroEconomicoStatoEnum, siacDQuadroEconomicoparteEnum, pageable);
	}
	
	
	/**
	 * Ricerca sintetica gsa classif.
	 *
	 * @param uidEnte the uid ente
	 * @param codice the codice
	 * @param descrizione the descrizione
	 * @param livello the livello
	 * @param siacDQuadroEconomicoStatoEnum the siac D gsa classif stato enum
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<SiacTQuadroEconomico> ricercaSinteticaQuadroEconomico(Integer uidEnte,  String codice, String descrizione, Integer livello, SiacDQuadroEconomicoStatoEnum siacDQuadroEconomicoStatoEnum,SiacDQuadroEconomicoParteEnum siacDQuadroEconomicoParteEnum, Pageable pageable){

		final String methodName = "ricercaSinteticaQuadroEconomico";
		log.info(methodName, "BEGIN");
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicerca(uidEnte,  codice, descrizione, livello, siacDQuadroEconomicoStatoEnum, siacDQuadroEconomicoParteEnum, jpql, param);
		
		log.info(methodName, "JPQL to execute: " + jpql.toString());
		log.info(methodName, "END");
		
		return getPagedList(jpql.toString(), param, pageable);
	}


	private void componiQueryRicerca(Integer uidEnte,  String codice,
			String descrizione, Integer livello, SiacDQuadroEconomicoStatoEnum siacDQuadroEconomicoStatoEnum,SiacDQuadroEconomicoParteEnum siacDQuadroEconomicoParteEnum, StringBuilder jpql,
			Map<String, Object> param) {
		jpql.append(" FROM SiacTQuadroEconomico c ");
		jpql.append(" WHERE c.dataCancellazione IS NULL ");
		jpql.append(" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND c.livello = :livello ");
		
		param.put("enteProprietarioId", uidEnte);
		param.put("livello", livello);
		
		appendFilterCodice(codice, jpql, param);
		
		appendFilterDescrizione(descrizione, jpql, param);
		
		appendFilterStato(siacDQuadroEconomicoStatoEnum, jpql, param);
		
		jpql.append(" ORDER BY c.quadroEconomicoCode ");
	}


	/**
	 * @param codice
	 * @param jpql
	 * @param param
	 */
	private void appendFilterCodice(String codice, StringBuilder jpql, Map<String, Object> param) {
		if(StringUtils.isBlank(codice)){
			return;
		}
		jpql.append(" AND UPPER(c.quadroEconomicoCode) = UPPER(:quadroEconomicoCode) ");
		param.put("quadroEconomicoCode", codice);			
	}
	
	/**
	 * Append filter descrizione.
	 *
	 * @param descrizione the descrizione
	 * @param jpql the jpql
	 * @param param the param
	 */
	private void appendFilterDescrizione(String descrizione, StringBuilder jpql, Map<String, Object> param) {
		if(StringUtils.isBlank(descrizione)){
			return;
		}
		jpql.append(" AND UPPER(c.quadroEconomicoDesc) = UPPER(:quadroEconomicoDesc) ");
		param.put("quadroEconomicoDesc", descrizione);			
	}



	/**
	 * Append filter gsa classif padre.
	 *
	 * @param uidQuadroEconomicoPadre the uid gsa classif padre
	 * @param jpql the jpql
	 * @param param the param
	 */
	private void appendFilterQuadroEconomicoPadre(Integer uidQuadroEconomicoPadre, StringBuilder jpql,Map<String, Object> param) {
		if(uidQuadroEconomicoPadre == null || uidQuadroEconomicoPadre == 0) {
			return;
		}
		jpql.append(" AND c.siacTQuadroEconomicoPadre.quadroEconomicoId =  :uidQuadroEconomicoPadre ");
		param.put("uidQuadroEconomicoPadre", uidQuadroEconomicoPadre);	
	}


	/**
	 * Append filter stato.
	 *
	 * @param siacDQuadroEconomicoStatoEnum the siac D gsa classif stato enum
	 * @param jpql the jpql
	 * @param param the param
	 */
	private void appendFilterStato(SiacDQuadroEconomicoStatoEnum siacDQuadroEconomicoStatoEnum, StringBuilder jpql,Map<String, Object> param) {
		if(siacDQuadroEconomicoStatoEnum == null) {
			return;
		}
		jpql.append(" AND EXISTS( " );
		jpql.append("     FROM SiacRQuadroEconomicoStato rgcs " );
		jpql.append("     WHERE rgcs.dataCancellazione IS NULL " );
		jpql.append("     AND rgcs.siacTQuadroEconomico = c " );
		jpql.append("     AND rgcs.siacDQuadroEconomicoStato.quadroEconomicoStatoCode = :quadroEconomicoStatoCode" );
		jpql.append(" )" );
			
		param.put("quadroEconomicoStatoCode", siacDQuadroEconomicoStatoEnum.getCodice());
	}
	
	/**
	 * Sets the data modifica inserimento.
	 *
	 * @param e the e
	 * @param now the now
	 * @param dataInizioValidita 
	 */
	private void setDataModificaInserimento(SiacTQuadroEconomico e, Date now, Date dataInizioValidita) {
		
		if(e.getSiacRQuadroEconomicoStatos() != null) {
			for(SiacRQuadroEconomicoStato stato : e.getSiacRQuadroEconomicoStatos()){
				stato.setDataModificaInserimento(now, dataInizioValidita);
			}
		}
		
	}


	/**
	 * Sets the data fine validita E data cancellazione se nel periodo di validita.
	 *
	 * @param eAttuale the e attuale
	 * @param dataCancellazioneDaImpostare the data cancellazione da impostare
	 * @param dataNelPeriodoDiValidita the data nel periodo di validita
	 */
	private void setDataFineValiditaEDataCancellazioneSeNelPeriodoDiValidita(SiacTQuadroEconomico eAttuale, Date dataCancellazioneDaImpostare, Date dataNelPeriodoDiValidita) {
		
		if(eAttuale.getSiacRQuadroEconomicoStatos() != null){
			for(SiacRQuadroEconomicoStato r : eAttuale.getSiacRQuadroEconomicoStatos()){
				r.setDataCancellazioneIfNotSet(dataCancellazioneDaImpostare);
				r.setDataFineValiditaIfNotSet(dataCancellazioneDaImpostare);
			}
		}	

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTQuadroEconomico> ricercaQuadroEconomico(Integer uidEnte, String codice, String descrizione, SiacDQuadroEconomicoStatoEnum siacDQuadroEconomicoStatoEnum,SiacDQuadroEconomicoParteEnum siacDQuadroEconomicoParteEnum) {
		
		final String methodName = "ricercaQuadroEconomico";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicerca(uidEnte, codice, descrizione, 0, siacDQuadroEconomicoStatoEnum, siacDQuadroEconomicoParteEnum, jpql, param);
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTQuadroEconomico> findSiacTQuadroEconomicoValidoByCodeAndParte(String quadroEconomicoCode,String parteCode, Integer quadroEconomicoPadreId , Integer enteProprietarioId) {
		
		final String methodName = "findSiacTQuadroEconomicoValidoByCodeAndParte";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();

		jpql.append(" FROM SiacTQuadroEconomico c " );
		jpql.append(" WHERE c.dataCancellazione IS NULL " );
		jpql.append(" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " );
		jpql.append(" AND c.quadroEconomicoCode = :quadroEconomicoCode " );
		jpql.append(" AND c.siacDQuadroEconomicoParte.parteCode = :parteCode " );
		
		if (quadroEconomicoPadreId!= null && quadroEconomicoPadreId != 0){		
			jpql.append(" AND c.siacTQuadroEconomicoPadre.quadroEconomicoId = :quadroEconomicoPadreId " );
			param.put("quadroEconomicoPadreId" ,quadroEconomicoPadreId );
		}else{
			jpql.append(" AND c.siacTQuadroEconomicoPadre.quadroEconomicoId IS NULL " );			
		}
		
		jpql.append(" AND EXISTS(  " );
		jpql.append(" FROM SiacRQuadroEconomicoStato rgcs  " );
		jpql.append(" WHERE rgcs.dataCancellazione IS NULL  " );
		jpql.append(" AND rgcs.siacTQuadroEconomico = c  " );
		jpql.append(" AND rgcs.siacDQuadroEconomicoStato.quadroEconomicoStatoCode = 'V'  " );
		jpql.append(" ) ");

		param.put("enteProprietarioId", enteProprietarioId);
		param.put("quadroEconomicoCode",quadroEconomicoCode );
		param.put("parteCode",parteCode );
		
		log.info(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		List<SiacTQuadroEconomico> ris  =  query.getResultList();
		
		return ris;
	}

	
}
