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

import it.csi.siac.siacbilser.integration.entity.SiacRGsaClassifStato;
import it.csi.siac.siacbilser.integration.entity.SiacTGsaClassif;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDGsaClassifStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class ContoDaoImpl.
 * 
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ClassificatoreGsaDaoImpl extends JpaDao<SiacTGsaClassif, Integer> implements ClassificatoreGsaDao {
	
	public SiacTGsaClassif create(SiacTGsaClassif e){
		Date now = new Date();
		Date dataInizioValidita = e.getDataInizioValidita();
		
		e.setDataModificaInserimento(now, dataInizioValidita);
		
		setDataModificaInserimento(e, now, dataInizioValidita);
		
		
		e.setUid(null);		
		super.save(e);
		return e;
	}


	public SiacTGsaClassif update(SiacTGsaClassif e){
		
		SiacTGsaClassif eAttuale = this.findById(e.getUid());
		
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
	public Page<SiacTGsaClassif> ricercaSinteticaGsaClassif(Integer uidEnte, SiacDAmbitoEnum siacDAmbitoEnum, String codice, String descrizione, SiacDGsaClassifStatoEnum siacDGsaClassifStatoEnum, Pageable pageable){
		return ricercaSinteticaGsaClassif(uidEnte,siacDAmbitoEnum, codice, descrizione, 0, siacDGsaClassifStatoEnum, pageable);
	}
	
	
	/**
	 * Ricerca sintetica gsa classif.
	 *
	 * @param uidEnte the uid ente
	 * @param siacDAmbitoEnum the siac D ambito enum
	 * @param codice the codice
	 * @param descrizione the descrizione
	 * @param livello the livello
	 * @param siacDGsaClassifStatoEnum the siac D gsa classif stato enum
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<SiacTGsaClassif> ricercaSinteticaGsaClassif(Integer uidEnte, SiacDAmbitoEnum siacDAmbitoEnum, String codice, String descrizione, Integer livello, SiacDGsaClassifStatoEnum siacDGsaClassifStatoEnum, Pageable pageable){

		final String methodName = "ricercaSinteticaGsaClassif";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		siacDAmbitoEnum = siacDAmbitoEnum != null ? siacDAmbitoEnum : SiacDAmbitoEnum.AmbitoGsa;
		
		componiQueryRicerca(uidEnte, siacDAmbitoEnum, codice, descrizione, livello, siacDGsaClassifStatoEnum, jpql, param);
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}


	private void componiQueryRicerca(Integer uidEnte, SiacDAmbitoEnum siacDAmbitoEnum, String codice,
			String descrizione, Integer livello, SiacDGsaClassifStatoEnum siacDGsaClassifStatoEnum, StringBuilder jpql,
			Map<String, Object> param) {
		jpql.append("FROM SiacTGsaClassif c ");
		jpql.append(" WHERE c.dataCancellazione IS NULL ");
		jpql.append(" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND c.siacDAmbito.ambitoCode = :ambitoCode ");
		jpql.append(" AND c.livello = :livello ");
		
		param.put("enteProprietarioId", uidEnte);
		param.put("ambitoCode", siacDAmbitoEnum.getCodice());
		param.put("livello", livello);
		
		appendFilterCodice(codice, jpql, param);
		
		appendFilterDescrizione(descrizione, jpql, param);
		
		appendFilterStato(siacDGsaClassifStatoEnum, jpql, param);
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
		jpql.append(" AND UPPER(c.gsaClassifCode) = UPPER(:gsaClassifCode) ");
		param.put("gsaClassifCode", codice);			
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
		jpql.append(" AND UPPER(c.gsaClassifDesc) = UPPER(:gsaClassifDesc) ");
		param.put("gsaClassifDesc", descrizione);			
	}



	/**
	 * Append filter gsa classif padre.
	 *
	 * @param uidGsaClassifPadre the uid gsa classif padre
	 * @param jpql the jpql
	 * @param param the param
	 */
	private void appendFilterGsaClassifPadre(Integer uidGsaClassifPadre, StringBuilder jpql,Map<String, Object> param) {
		if(uidGsaClassifPadre == null || uidGsaClassifPadre == 0) {
			return;
		}
		jpql.append(" AND c.siacTGsaClassifPadre.gsaClassifId =  :uidContoPadre ");
		param.put("uidContoPadre", uidGsaClassifPadre);	
	}


	/**
	 * Append filter stato.
	 *
	 * @param siacDGsaClassifStatoEnum the siac D gsa classif stato enum
	 * @param jpql the jpql
	 * @param param the param
	 */
	private void appendFilterStato(SiacDGsaClassifStatoEnum siacDGsaClassifStatoEnum, StringBuilder jpql,Map<String, Object> param) {
		if(siacDGsaClassifStatoEnum == null) {
			return;
		}
		jpql.append(" AND EXISTS( " );
		jpql.append("     FROM SiacRGsaClassifStato rgcs " );
		jpql.append("     WHERE rgcs.dataCancellazione IS NULL " );
		jpql.append("     AND rgcs.siacTGsaClassif = c " );
		jpql.append("     AND rgcs.siacDGsaClassifStato.gsaClassifStatoCode = :gsaClassifStatoCode" );
		jpql.append(" )" );
			
		param.put("gsaClassifStatoCode", siacDGsaClassifStatoEnum.getCodice());
	}
	
	/**
	 * Sets the data modifica inserimento.
	 *
	 * @param e the e
	 * @param now the now
	 * @param dataInizioValidita 
	 */
	private void setDataModificaInserimento(SiacTGsaClassif e, Date now, Date dataInizioValidita) {
		
		if(e.getSiacRGsaClassifStatos() != null) {
			for(SiacRGsaClassifStato stato : e.getSiacRGsaClassifStatos()){
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
	private void setDataFineValiditaEDataCancellazioneSeNelPeriodoDiValidita(SiacTGsaClassif eAttuale, Date dataCancellazioneDaImpostare, Date dataNelPeriodoDiValidita) {
		
		if(eAttuale.getSiacRGsaClassifStatos() != null){
			for(SiacRGsaClassifStato r : eAttuale.getSiacRGsaClassifStatos()){
				r.setDataCancellazioneIfNotSet(dataCancellazioneDaImpostare);
				r.setDataFineValiditaIfNotSet(dataCancellazioneDaImpostare);
			}
		}	

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTGsaClassif> ricercaGsaClassif(Integer uidEnte, SiacDAmbitoEnum siacDAmbitoEnum, String codice, String descrizione, SiacDGsaClassifStatoEnum siacDGsaClassifStatoEnum) {
		
		final String methodName = "ricercaGsaClassif";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		siacDAmbitoEnum = siacDAmbitoEnum != null ? siacDAmbitoEnum : SiacDAmbitoEnum.AmbitoGsa;
		
		componiQueryRicerca(uidEnte, siacDAmbitoEnum, codice, descrizione, 0, siacDGsaClassifStatoEnum, jpql, param);
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		return query.getResultList();
	}
	

}
