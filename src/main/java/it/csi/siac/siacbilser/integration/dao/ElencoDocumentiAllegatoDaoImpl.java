/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDElencoDocStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class ElencoDocumentiAllegatoDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElencoDocumentiAllegatoDaoImpl extends JpaDao<SiacTElencoDoc, Integer> implements ElencoDocumentiAllegatoDao {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.ElencoDocumentiAllegatoDao#create(it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc)
	 */
	public SiacTElencoDoc create(SiacTElencoDoc e){
		
		Date now = new Date();
		e.setDataModificaInserimento(now);
		
		if(e.getSiacRAttoAllegatoElencoDocs()!=null){
			for(SiacRAttoAllegatoElencoDoc r : e.getSiacRAttoAllegatoElencoDocs()){
				r.setDataModificaInserimento(now);
			}
		}

		if(e.getSiacRElencoDocStatos()!=null){
			for(SiacRElencoDocStato r : e.getSiacRElencoDocStatos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRElencoDocSubdocs()!=null){
			for(SiacRElencoDocSubdoc r : e.getSiacRElencoDocSubdocs()){
				r.setDataModificaInserimento(now);
			}
		}		
		
		e.setUid(null);		
		super.save(e);
		return e;
		
	}

	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	public SiacTElencoDoc update(SiacTElencoDoc e){
		SiacTElencoDoc eAttuale = this.findById(e.getUid());
		
		Date now = new Date();
		e.setDataModificaAggiornamento(now);		
		
		//cancellazione elementi collegati
		if(eAttuale.getSiacRAttoAllegatoElencoDocs()!=null){
			for(SiacRAttoAllegatoElencoDoc r : eAttuale.getSiacRAttoAllegatoElencoDocs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}

		if(eAttuale.getSiacRElencoDocStatos()!=null){
			for(SiacRElencoDocStato r : eAttuale.getSiacRElencoDocStatos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(eAttuale.getSiacRElencoDocSubdocs()!=null){
			for(SiacRElencoDocSubdoc r : eAttuale.getSiacRElencoDocSubdocs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		
		//inserimento elementi nuovi		
		if(e.getSiacRAttoAllegatoElencoDocs()!=null){
			for(SiacRAttoAllegatoElencoDoc r : e.getSiacRAttoAllegatoElencoDocs()){
				r.setDataModificaInserimento(now);
			}
		}

		if(e.getSiacRElencoDocStatos()!=null){
			for(SiacRElencoDocStato r : e.getSiacRElencoDocStatos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRElencoDocSubdocs()!=null){
			for(SiacRElencoDocSubdoc r : e.getSiacRElencoDocSubdocs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		
		
		super.update(e);
		return e;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.ElencoDocumentiAllegatoDao#ricercaPuntualeElencoDocumentiAllegatoAtto(int, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.util.List)
	 */
	@Override
	public SiacTElencoDoc ricercaPuntualeElencoDocumentiAllegatoAtto(int enteProprietarioId, 
			Integer uidAnnoEsercizio,
			Integer annoElenco,
			Integer numeroElenco,
			Integer uidAttoAmministrativo,
			List<SiacDElencoDocStatoEnum> stati
			) {
		
		final String methodName = "ricercaPuntualeElencoDocumentiAllegatoAtto";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaElencoDocumentiAllegato( jpql, param, enteProprietarioId, null, null, null, null, null, null, null,null,null);
		
		jpql.append(" ORDER BY d.eldocAnno, d.eldocNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(),param);
		
		return (SiacTElencoDoc) query.getSingleResult();
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.ElencoDocumentiAllegatoDao#ricercaSinteticaElenco(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.util.Date, java.util.Set, java.lang.Integer, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<SiacTElencoDoc> ricercaSinteticaElenco(Integer uidEnte, Integer anno, Integer numero, Integer annoEsterno, String numeroEsterno,
			Date dataTrasmissione, Set<SiacDElencoDocStatoEnum> siacDElencoDocStati, Integer uidAttoAmministrativo, Integer eldocNumeroDa, Integer eldocNumeroA,Pageable pageable) {
		final String methodName = "ricercaSinteticaElenco";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaElencoDocumentiAllegato(jpql, param, uidEnte, anno, numero, annoEsterno, numeroEsterno, dataTrasmissione, siacDElencoDocStati,
				uidAttoAmministrativo, eldocNumeroDa, eldocNumeroA);
		
		jpql.append(" ORDER BY d.eldocAnno, d.eldocNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	@Override
	public BigDecimal calcolaTotaleDaPagare(Integer uidEnte, Integer anno, Integer numero, Integer annoEsterno, String numeroEsterno, Date dataTrasmissione,
			Set<SiacDElencoDocStatoEnum> siacDElencoDocStati, Integer uidAttoAmministrativo) {
		
		final String methodName = "calcolaTotaleDaPagare";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("SELECT COALESCE(SUM(siacTLiq.liqImporto), 0) AS TOT ")
			.append(" FROM SiacTLiquidazione siacTLiq ")
			.append(" WHERE siacTLiq.dataCancellazione IS NULL ")
			.append(" AND (siacTLiq.dataFineValidita IS NULL OR siacTLiq.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append(" AND EXISTS ( ")
			.append("     FROM siacTLiq.siacRSubdocLiquidaziones siacRSubdocLiq, SiacTSubdoc siacTSubd ")
			.append("     WHERE siacRSubdocLiq.siacTSubdoc = siacTSubd ")
			.append("     AND siacRSubdocLiq.dataCancellazione IS NULL ")
			.append("     AND (siacRSubdocLiq.dataFineValidita IS NULL OR siacRSubdocLiq.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append("     AND ( ")
			.append("         siacTSubd.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'S' ")
			.append("         OR siacTSubd.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'IS' ")
			.append("         ) ")
			.append("     AND EXISTS ( ")
			.append("         FROM siacTSubd.siacRElencoDocSubdocs siacRElencoDocSubd ")
			.append("         WHERE siacRElencoDocSubd.dataCancellazione IS NULL ")
			.append("         AND siacRElencoDocSubd.siacTElencoDoc IN ( ");
		
		// Aggiungo i dati della ricerca
		componiQueryRicercaSinteticaElencoDocumentiAllegato(jpql, param, uidEnte, anno, numero, annoEsterno, numeroEsterno, dataTrasmissione, siacDElencoDocStati,
				uidAttoAmministrativo,null,null);
		
		jpql.append("         ) ")
			.append("     ) ")
			.append(" ) ")
			.append(" AND EXISTS ( ")
			.append("     FROM siacTLiq.siacRLiquidazioneStatos siacRLiquidazioneSta ")
			.append("     WHERE siacRLiquidazioneSta.dataCancellazione IS NULL ")
			.append("     AND (siacRLiquidazioneSta.dataFineValidita IS NULL OR siacRLiquidazioneSta.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append("     AND siacRLiquidazioneSta.siacDLiquidazioneStato.liqStatoCode = 'V' ") //stato liquidazione VALIDO! (DEFINITIVO non esiste)
			.append(" ) ")
			.append(" AND NOT EXISTS ( ")
			.append("     FROM siacTLiq.siacRLiquidazioneOrds siacRLiquidazioneOr, SiacTOrdinativo siacTOrd ")
			.append("     WHERE siacRLiquidazioneOr.dataCancellazione IS NULL ")
			.append("     AND (siacRLiquidazioneOr.dataFineValidita IS NULL OR siacRLiquidazioneOr.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append("     AND siacRLiquidazioneOr.siacTOrdinativoT.siacTOrdinativo = siacTOrd ")
			.append("     AND EXISTS ( ")
			.append("         FROM siacTOrd.siacROrdinativoStatos siacROrdSta")
			.append("    	  WHERE siacROrdSta.dataCancellazione IS NULL ")
			.append("         AND (siacROrdSta.dataFineValidita IS NULL OR siacROrdSta.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append("         AND siacROrdSta.siacDOrdinativoStato.ordinativoStatoCode <> 'A' ")
			.append("     ) ")
			.append(" ) ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		log.info(methodName, "returning: " + result);
		
		return result;
	}



	@Override
	public BigDecimal calcolaTotaleDaIncassare(Integer uidEnte, Integer anno, Integer numero, Integer annoEsterno, String numeroEsterno, Date dataTrasmissione,
			Set<SiacDElencoDocStatoEnum> siacDElencoDocStati, Integer uidAttoAmministrativo) {
		
		final String methodName = "calcolaTotaleDaIncassare";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("SELECT COALESCE(SUM(siacTSubd.subdocImporto), 0) AS TOT ")
			.append(" FROM SiacTSubdoc siacTSubd, SiacTDoc siacTDo " )
			.append(" WHERE siacTSubd.dataCancellazione IS NULL ")
			.append(" AND siacTSubd.siacTDoc = siacTDo ")
			.append(" AND ( ")
			.append("     siacTSubd.subdocConvalidaManuale IS NOT NULL")
			.append("     AND siacTSubd.subdocConvalidaManuale <> '' ")
			.append(" ) ")
			.append(" AND EXISTS ( ")
			.append("     FROM siacTSubd.siacRElencoDocSubdocs siacRElencoDocSubd ")
			.append("     WHERE siacRElencoDocSubd.dataCancellazione IS NULL ")
			.append("     AND siacRElencoDocSubd.siacTElencoDoc.eldocId IN ( ");
		
		// Aggiungo i dati della ricerca
		componiQueryRicercaSinteticaElencoDocumentiAllegato(jpql, param, uidEnte, anno, numero, annoEsterno, numeroEsterno, dataTrasmissione, siacDElencoDocStati,
				uidAttoAmministrativo,null,null);
		
		jpql.append("     ) ")
			.append(" ) ")
			.append(" AND EXISTS ( ")
			.append("     FROM siacTDo.siacRDocStatos siacRDocSt ")
			.append("     WHERE siacRDocSt.dataCancellazione IS NULL ")
			.append("     AND siacRDocSt.siacDDocStato.docStatoCode NOT IN ( 'A' ) ")
			.append(" ) ")
			.append(" AND ( ")
			.append("     siacTDo.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'E' ")
			.append("     OR siacTDo.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'IE' ")
			.append(" ) ")
			.append(" AND NOT EXISTS ( ")
			.append("     FROM siacTSubd.siacRSubdocOrdinativoTs siacRSubdocOrdinativots, SiacTOrdinativo siacTOrd ")
			.append("     WHERE siacRSubdocOrdinativots.dataCancellazione IS NULL ")
			.append("     AND siacRSubdocOrdinativots.siacTOrdinativoT.siacTOrdinativo = siacTOrd ")
			.append("     AND EXISTS ( ")
			.append("         FROM siacTOrd.siacROrdinativoStatos siacROrdSta ")
			.append("         WHERE siacROrdSta.siacDOrdinativoStato.ordinativoStatoCode <> 'A' ")
			.append("         AND siacROrdSta.dataCancellazione IS NULL ")
			.append("         AND (siacROrdSta.dataFineValidita IS NULL OR siacROrdSta.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append("     ) ")
			.append(" ) ");
			
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		log.info(methodName, "returning: " + result);
		
		return result;
		
	}



	@Override
	public BigDecimal calcolaTotaleQuoteCollegate(Integer uidEnte, Integer anno, Integer numero, Integer annoEsterno, String numeroEsterno,
			Date dataTrasmissione, Set<SiacDElencoDocStatoEnum> siacDElencoDocStati, Integer uidAttoAmministrativo, Set<SiacDDocFamTipoEnum> siacDDocFamTipi) {
		
		final String methodName = "calcolaTotaleQuoteCollegate";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("SELECT COALESCE(SUM(siacTSubd.subdocImporto - siacTSubd.subdocImportoDaDedurre), 0) AS tot ")
			.append(" FROM SiacTSubdoc siacTSubd, SiacTDoc siacTDo " )
			.append(" WHERE siacTSubd.dataCancellazione IS NULL ")
			.append(" AND siacTSubd.siacTDoc = siacTDo ")
			.append(" AND EXISTS ( ")
			.append("     FROM siacTSubd.siacRElencoDocSubdocs siacRElencoDocSubd ")
			.append("     WHERE siacRElencoDocSubd.dataCancellazione IS NULL ")
			.append("     AND siacRElencoDocSubd.siacTElencoDoc.eldocId IN ( ");
		
		// Aggiungo i dati della ricerca
		componiQueryRicercaSinteticaElencoDocumentiAllegato(jpql, param, uidEnte, anno, numero, annoEsterno, numeroEsterno, dataTrasmissione, siacDElencoDocStati,
				uidAttoAmministrativo,null,null);
		
		jpql.append("     ) ")
			.append(" ) ")
			.append(" AND EXISTS ( ")
			.append("     FROM siacTDo.siacRDocStatos siacRDocSt ")
			.append("     WHERE siacRDocSt.dataCancellazione IS NULL ")
			.append("     AND siacRDocSt.siacDDocStato.docStatoCode NOT IN ( 'A' ) ")
			.append(" ) ")
			.append(" AND siacTDo.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) ");
		List<String> docFamTipoCodes = new ArrayList<String>();
		for(SiacDDocFamTipoEnum sddfte : siacDDocFamTipi) {
			docFamTipoCodes.add(sddfte.getCodice());
		}
		param.put("docFamTipoCodes", docFamTipoCodes);
		
		jpql.append(" AND NOT EXISTS ( ")
			.append("     FROM SiacDDocGruppo siacDDocGru ")
			.append("     WHERE siacDDocGru.docGruppoTipoCode = 'NCD' ")
			.append("     AND siacTDo.siacDDocTipo.siacDDocGruppo = siacDDocGru ")
			.append(" ) ");
			
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		log.info(methodName, "returning: " + result);
		
		return result;
	}

	/**
	 * Componi query ricerca sintetica allegato atto.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param enteProprietarioId the ente proprietario id
	 * @param eldocAnno the eldoc anno
	 * @param eldocNumero the eldoc numero
	 * @param eldocSysesternoAnno the eldoc sysesterno anno
	 * @param eldocSysesternoNumero the eldoc sysesterno numero
	 * @param eldocDataTrasmissione the eldoc data trasmissione
	 * @param siacDElencoDocStati the siac d elenco doc stati
	 * @param attoammId the attoamm id
	 * @param numeroA 
	 * @param numeroDa 
	 */
	private void componiQueryRicercaSinteticaElencoDocumentiAllegato(StringBuilder jpql, Map<String, Object> param,
			Integer enteProprietarioId, Integer eldocAnno, Integer eldocNumero, Integer eldocSysesternoAnno, String eldocSysesternoNumero,
			Date eldocDataTrasmissione, Set<SiacDElencoDocStatoEnum> siacDElencoDocStati, Integer attoammId, Integer eldocNumeroDa, Integer eldocNumeroA) {
		
		jpql.append("FROM SiacTElencoDoc d ")
			.append(" WHERE ")
			.append(" d.dataCancellazione IS NULL ")
			.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(eldocAnno != null) {
			jpql.append(" AND d.eldocAnno = :eldocAnno ");
			param.put("eldocAnno", eldocAnno);
		}
		componiFiltroByNumero(jpql, param, eldocNumero, eldocNumeroDa, eldocNumeroA);
		
		if(eldocSysesternoAnno != null) {
			jpql.append(" AND d.eldocSysesternoAnno = :eldocSysesternoAnno ");
			param.put("eldocSysesternoAnno", eldocSysesternoAnno);
		}
		if(eldocSysesternoNumero != null && !eldocSysesternoNumero.isEmpty()) {
			jpql.append(" AND d.eldocSysesternoNumero = :eldocSysesternoNumero ");
			param.put("eldocSysesternoNumero", eldocSysesternoNumero);
		}
		if(eldocDataTrasmissione != null) {
			jpql.append(" AND d.eldocDataTrasmissione = :eldocDataTrasmissione ");
			param.put("eldocDataTrasmissione", eldocDataTrasmissione);
		}
		if(siacDElencoDocStati != null && !siacDElencoDocStati.isEmpty()) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM d.siacRElencoDocStatos sreds ")
				.append("     WHERE sreds.siacDElencoDocStato.eldocStatoCode IN ( ");
			int i = 0;
			for(SiacDElencoDocStatoEnum stato : siacDElencoDocStati) {
				if(i > 0) {
					jpql.append(" , ");
				}
				jpql.append(" :eldocStatoCode" + i);
				param.put("eldocStatoCode" + i, stato.getCodice());
				i++;
			}
			jpql.append("     ) " )
				.append("     AND sreds.dataCancellazione IS NULL ")
				.append(" ) ");
		}
		if(attoammId != null) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM d.siacRAttoAllegatoElencoDocs sraaed ")
				.append("     WHERE sraaed.siacTAttoAllegato.siacTAttoAmm.attoammId = :attoammId ")
				.append(" ) ");
			param.put("attoammId", attoammId);
		}
	}



	/**
	 * Compone il filtro sul numero elenco. Se viene fornito un solo numero elenco questo prevale, se viene fornito un intervallo di numeri (numeroDa..numeroA), invece, si cerca per intervallo. 
	 * @param jpql
	 * @param param
	 * @param eldocNumero
	 * @param eldocNumeroDa
	 * @param eldocNumeroA
	 */
	private void componiFiltroByNumero(StringBuilder jpql, Map<String, Object> param, Integer eldocNumero,
			Integer eldocNumeroDa, Integer eldocNumeroA) {
		if(eldocNumero != null) {
			jpql.append(" AND d.eldocNumero = :eldocNumero ");
			param.put("eldocNumero", eldocNumero);
			return;
		}
		if(eldocNumeroDa != null){
			jpql.append(" AND d.eldocNumero >= :eldocNumeroDa");
			param.put("eldocNumeroDa", eldocNumeroDa);
		}
		if(eldocNumeroA != null){
			jpql.append(" AND d.eldocNumero <= :eldocNumeroA");
			param.put("eldocNumeroA", eldocNumeroA);
		}
		
	}
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.ElencoDocumentiAllegatoDao#ricercaSinteticaElenco(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.util.Date, java.util.Set, java.lang.Integer, org.springframework.data.domain.Pageable)
	 */
	@Override
	                          
	public Page<SiacTSubdoc>  ricercaSinteticaQuoteElenco(Integer uidElenco, Integer soggettoId,String soggettoCode,Integer enteId,Pageable pageable) {
		final String methodName = "ricercaSinteticaQuoteElenco";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();

		//SIAC-5589 aggiungere filtro soggetto, cambiare nome in RicercaSinteticaQuoteElenco
		componiQueryRicercaSinteticaQuoteElenco(jpql, uidElenco, soggettoId, soggettoCode, enteId,param );		

		log.info(methodName, "JPQL to execute: " + jpql.toString());
		
		String jpqlCount = getCountQuery(jpql.toString());
		Query qc = createQuery(jpqlCount, param);
		long count = ((Number) qc.getSingleResult()).longValue();
		
		List<Object[]> results = new ArrayList<Object[]>();
		if (count > 0) {
			Query query = createQuery(jpql.toString(), param);
			query.setFirstResult(pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
			results = query.getResultList();
		}
		List<SiacTSubdoc> siacTSubdocs = new ArrayList<SiacTSubdoc>();
		for(Object[] obj : results) {
			siacTSubdocs.add((SiacTSubdoc) obj[0]);
		}
		return new PageImpl<SiacTSubdoc>(siacTSubdocs, pageable, count);
	}
	
	private String getCountQuery(String jpql) {
		// FIXME: verificare se portarlo al livello di JpaDao o ExtendedJpaDao (rifattorizzando JpaDao per rendere la countQuery protected)
		String jpqlUpper = jpql.toUpperCase(Locale.ITALIAN);
		
		int indexDistinct = jpqlUpper.indexOf("DISTINCT");
		int fromIndex = jpqlUpper.indexOf("FROM");
		String jpqlCount = jpql.substring(fromIndex);
		
		int toIndex = jpqlCount.toUpperCase(Locale.ITALIAN).lastIndexOf("ORDER BY");
		if (toIndex != -1) {
			jpqlCount = jpqlCount.substring(0, toIndex);
		}
		// Handle distinct
		
		if(indexDistinct == -1) {
			// Old version
			return String.format("SELECT COUNT(*) %s", jpqlCount);
		}
		
		// Devo eliminare la parentesi attorno al 'distinct'
		int indexClauseInit = jpqlUpper.indexOf("(", indexDistinct) + 1;
		int indexClauseEnd;
		if(indexClauseInit == -1) {
			// Non ho la parentesi
			indexClauseInit = jpqlUpper.indexOf(" ", indexDistinct) + 1;
			indexClauseEnd = jpqlUpper.indexOf("FROM", indexClauseInit);
		} else {
			// Ho la parentesi
			indexClauseEnd = jpqlUpper.indexOf(")", indexClauseInit);
		}
		
		String distinctClause = jpql.substring(indexClauseInit, indexClauseEnd);
		
		return String.format("SELECT COUNT(DISTINCT %s) %s", distinctClause, jpqlCount);
	}



	/**
	 * Compone la query di ricerca di dettaglio delle quote di un elenco
	 * <br/>
	 * Ordinamento: le righe di un atto allegato devono essere ordinate come segue:
	 * <ol type="a">
	 *     <li>Codice soggetto</li>
	 *     <li>Documento/quota</li>
	 * </ol>
	 * @param jpql il jpql
	 * @param param i parametri
	 * @param uidElenco l'uid dell'elenco
	 */
	private void componiQueryRicercaSinteticaQuoteElenco(StringBuilder jpql, Integer uidElenco,Integer soggettoId,String soggettoCode,Integer enteId, Map<String, Object> param) {
	/*
		jpql.append(" SELECT DISTINCT (reds.siacTSubdoc), rds.siacTSoggetto.soggettoCode, reds.siacTSubdoc.siacTDoc.siacDDocTipo.docTipoCode, reds.siacTSubdoc.siacTDoc.docAnno, reds.siacTSubdoc.siacTDoc.docNumero, reds.siacTSubdoc.subdocNumero ")
			.append(" FROM SiacRElencoDocSubdoc reds, SiacRDocSog rds ")
			.append(" WHERE reds.dataCancellazione IS NULL ")
			.append(" AND rds.dataCancellazione IS NULL ")
			.append(" AND rds.siacTDoc = reds.siacTSubdoc.siacTDoc ")
			.append(" AND reds.siacTElencoDoc.eldocId = :uidElenco ");
		
		param.put("uidElenco", uidElenco);
*/
		 
		jpql.append(" SELECT DISTINCT (reds.siacTSubdoc), ts.soggettoCode, td.siacDDocTipo.docTipoCode, td.docAnno, td.docNumero, tsd.subdocNumero")
		.append(" from")
		.append("   SiacRElencoDocSubdoc reds")
		.append("  ,SiacTSubdoc   tsd")
		.append("  ,SiacTDoc      td ")
		.append("  ,SiacRDocSog   rds")
		.append("  ,SiacTSoggetto ts")
		.append(" WHERE ")
		.append("     	reds.siacTSubdoc.subdocId  = tsd.subdocId")
		.append("   AND tsd.siacTDoc.docId      = td.docId")
		.append(" 	AND td.docId       = rds.siacTDoc.docId")
		.append(" 	AND rds.siacTSoggetto.soggettoId = ts.soggettoId")
		.append("	AND reds.siacTElencoDoc.eldocId = :uidElenco");
		param.put("uidElenco", uidElenco);
	    
		if (soggettoId !=null){
	    	jpql.append("	AND ts.soggettoId = :soggettoId");
	        param.put("soggettoId", soggettoId);
	    }else{		
	        if(soggettoCode !=null){
	        	jpql.append("	AND	ts.soggettoCode = :soggettoCode")
	    		    .append("	AND ts.siacTEnteProprietario.enteProprietarioId = :enteId");	        				
	            param.put("soggettoCode", soggettoCode);
	            param.put("enteId", enteId);
	        }
	    }
		jpql.append("   AND reds.dataCancellazione IS NULL")
			.append("   AND tsd.dataCancellazione IS NULL")
			.append("  	AND td.dataCancellazione IS NULL ")
			.append(" 	AND rds.dataCancellazione IS NULL")
			.append("   AND ts.dataCancellazione IS NULL");		
		jpql.append(" ORDER BY ts.soggettoCode, td.siacDDocTipo.docTipoCode, td.docAnno, td.docNumero, tsd.subdocNumero");
		//jpql.append(" ORDER BY rds.siacTSubdoc.subdocNumero ");		
	}
}
