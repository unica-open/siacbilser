/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.subdoc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAmmStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDLiquidazioneStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOrdinativoStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProvCassaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.entity.SiacTDocFin;
import it.csi.siac.siacfinser.integration.util.DataValiditaUtil;

/**
 * Versione customizzata dell'originale SubdocumentoDaoImpl di bilancio, ci serve solo per la ricerca sub documenti
 * per creare una carta contabile da documento..
 * 
 * 
 * EDIT OTTOBRE 2016: introdotti nuovi altri metodi ottimizzati per ricerca sub documenti spesa e altro 
 * DIVENTA UNA CLASSE A PARTE A TUTTI GLI EFFETTI - NON ELIMINARE
 *
 * @author Claudio Picco
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SubdocumentoDaoCustomImpl extends AbstractDao<SiacTSubdoc, Integer> implements SubdocumentoDaoCustom {
	
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.SubdocumentoDao#ricercaSinteticaSubdocumento(java.lang.Integer, java.lang.Integer, java.lang.String, java.util.Date, java.lang.Integer, org.springframework.data.domain.Pageable)
	 */
	
	@Override
	public Page<SiacTSubdoc> ricercaSinteticaSubdocumenti(
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> tipoFam,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer eldocNumeroDa,
			Integer eldocNumeroA,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
			List<String> tipiDocDaEscludere,
			Integer docAnno,
			String docNumero,
			Date docDataEmissione,
			Integer subdocNumero,
			BigDecimal movgestNumero,
			Integer movgestAnno,
			String  soggettoCode,
			Integer attoammId,
			String attoammAnno,
			Integer attoammNumero,
			Integer attoammTipoId,
			Integer uidStruttAmm,
			Integer annoCapitolo, //???
			Integer elemCode,
			Integer elemCode2,
			Integer elemCode3,
			Set<SiacDDocStatoEnum> statiDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaPagareOIncassareZero, 
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			BigDecimal importoLiquidabileEsatto,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			
			Boolean collegatoAMovimento,
			Boolean collegatoACarteContabili,
			Boolean escludiGiaPagatiDaOrdinativoSpesa,
			
			Pageable pageable
			) {
	
		
		final String methodName = "ricercaSubdocumento";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT sd ");
		
		componiQueryRicercaSinteticaSubdocumento(jpql, param,enteProprietarioId, bilId, tipoFam, eldocId, eldocAnno, eldocNumero, eldocNumeroDa, eldocNumeroA, 
				provcAnno, provcNumero, provcDataEmissione,
				tipoProvv, docTipoId, tipiDocDaEscludere, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoCode, attoammId,
				 attoammAnno, attoammNumero, attoammTipoId, uidStruttAmm, annoCapitolo, elemCode, elemCode2, elemCode3, statiDocumento, collegatoAMovimentoDelloStessoBilancio, 
				 associatoAProvvedimentoOAdElenco,  importoDaPagareOIncassareZero, importoDaPagareOIncassareMaggioreDiZero, importoLiquidabileEsatto, rilevatiIvaConRegistrazioneONonRilevantiIva,
				 collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto, siacDAttoAmmStatoEnums, associatoAdOrdinativo,
				 
				 collegatoAMovimento,collegatoACarteContabili,escludiGiaPagatiDaOrdinativoSpesa);
		
		jpql.append(" ORDER BY sd.subdocNumero ");
		
		/*
		 * I dati esposti devono essere ordinati per :
			- anno e numero elenco
			- documento: E/S, anno, tipo, numero doc. e numero quota
		 */
		
		log.debug(methodName, jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
		
	}
	
	
	@Override
	public BigDecimal ricercaSinteticaSubdocumentiTotaleImporti(
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> tipoFam,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer eldocNumeroDa,
			Integer eldocNumeroA,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
			List<String> tipiDocDaEscludere,
			Integer docAnno,
			String docNumero,
			Date docDataEmissione,
			Integer subdocNumero,
			BigDecimal movgestNumero,
			Integer movgestAnno,
			String  soggettoCode,
			Integer attoammId,
			String attoammAnno,
			Integer attoammNumero,
			Integer attoammTipoId,
			Integer uidStruttAmm,
			Integer annoCapitolo, //???
			Integer elemCode,
			Integer elemCode2,
			Integer elemCode3,
			Set<SiacDDocStatoEnum> statiDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaPagareOIncassareZero, 
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			BigDecimal importoLiquidabileEsatto,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			
			Boolean collegatoAMovimento,
			Boolean collegatoACarteContabili,
			Boolean escludiGiaPagatiDaOrdinativoSpesa,
			
			Pageable pageable
			) {
	
		
		final String methodName = "ricercaSubdocumento";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT SUM(sd.subdocImporto - sd.subdocImportoDaDedurre) ");
		
		componiQueryRicercaSinteticaSubdocumento(jpql, param,enteProprietarioId, bilId, tipoFam, eldocId, eldocAnno, eldocNumero, eldocNumeroDa, eldocNumeroA, 
				provcAnno, provcNumero, provcDataEmissione,
				tipoProvv, docTipoId, tipiDocDaEscludere, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoCode, attoammId,
				attoammAnno, attoammNumero, attoammTipoId, uidStruttAmm,annoCapitolo, elemCode, elemCode2, elemCode3, statiDocumento, collegatoAMovimentoDelloStessoBilancio, 
				associatoAProvvedimentoOAdElenco,  importoDaPagareOIncassareZero, importoDaPagareOIncassareMaggioreDiZero, importoLiquidabileEsatto, rilevatiIvaConRegistrazioneONonRilevantiIva,
				collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto, siacDAttoAmmStatoEnums, associatoAdOrdinativo,
				
				collegatoAMovimento,collegatoACarteContabili,escludiGiaPagatiDaOrdinativoSpesa);
		
		/*
		 * I dati esposti devono essere ordinati per :
			- anno e numero elenco
			- documento: E/S, anno, tipo, numero doc. e numero quota
		 */
		
		log.debug(methodName, jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		return result;
	}

	
	private void componiQueryRicercaSinteticaSubdocumento(
			StringBuilder jpql, 
			Map<String, Object> param,
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> docFamTipoCodeEnums,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer eldocNumeroDa,
			Integer eldocNumeroA,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
			List<String> tipiDocDaEscludere,
			Integer docAnno,
			String docNumero,
			Date docDataEmissione,
			Integer subdocNumero,
			BigDecimal movgestNumero,
			Integer movgestAnno,
			String soggettoCode,
			Integer attoammId,
			String attoammAnno,
			Integer attoammNumero,
			Integer attoammTipoId,
			Integer uidStruttAmm,
			Integer annoCapitolo, 
			Integer elemCode,
			Integer elemCode2,
			Integer elemCode3,
			Set<SiacDDocStatoEnum> statiDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio,
			Boolean associatoAProvvedimentoOAdElenco,
			Boolean importoDaPagareOIncassareZero,
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			BigDecimal importoLiquidabileEsatto,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			
			Boolean collegatoAMovimento,
			Boolean collegatoACarteContabili,
			Boolean escludiGiaPagatiDaOrdinativoSpesa
			) {
		
		jpql.append(" FROM SiacTSubdoc sd, SiacTDoc d ");
		jpql.append(" WHERE sd.dataCancellazione IS NULL ");
		jpql.append(" AND sd.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND sd.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) ");
		jpql.append(" AND sd.siacTDoc = d ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		List<String> docFamTipoCodes = new ArrayList<String>();
		for(SiacDDocFamTipoEnum sddfte : docFamTipoCodeEnums) {
			docFamTipoCodes.add(sddfte.getCodice());
		}
		param.put("docFamTipoCodes", docFamTipoCodes);
		
		aggiungiFiltriFacoltativiAQueryRicercaSinteticaSubdocumento(jpql, param, "d", "sd" , bilId, eldocId, eldocAnno, eldocNumero, eldocNumeroDa, eldocNumeroA, 
				provcAnno, provcNumero, provcDataEmissione,
				tipoProvv, docTipoId, tipiDocDaEscludere, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoCode, attoammId,
				attoammAnno, attoammNumero, attoammTipoId, uidStruttAmm,annoCapitolo, elemCode, elemCode2, elemCode3, statiDocumento, collegatoAMovimentoDelloStessoBilancio, 
				associatoAProvvedimentoOAdElenco,  importoDaPagareOIncassareZero, importoDaPagareOIncassareMaggioreDiZero, importoLiquidabileEsatto, rilevatiIvaConRegistrazioneONonRilevantiIva,
				collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto, siacDAttoAmmStatoEnums, associatoAdOrdinativo,
				collegatoAMovimento,collegatoACarteContabili,escludiGiaPagatiDaOrdinativoSpesa);
		
		
	}
	
	private void aggiungiFiltriFacoltativiAQueryRicercaSinteticaSubdocumento(
			StringBuilder jpql, 
			Map<String, Object> param,
			String aliasDoc,
			String aliasSubdoc,
			Integer bilId,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer eldocNumeroDa,
			Integer eldocNumeroA,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
			List<String> tipiDocDaEscludere,
			Integer docAnno,
			String docNumero,
			Date docDataEmissione,
			Integer subdocNumero,
			BigDecimal movgestNumero,
			Integer movgestAnno,
			String soggettoCode,
			Integer attoammId,
			String attoammAnno,
			Integer attoammNumero,
			Integer attoammTipoId,
			Integer uidStruttAmm,
			Integer annoCapitolo, 
			Integer elemCode,
			Integer elemCode2,
			Integer elemCode3,
			Set<SiacDDocStatoEnum> statiDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio,
			Boolean associatoAProvvedimentoOAdElenco,
			Boolean importoDaPagareOIncassareZero,
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			BigDecimal importoLiquidabileEsatto,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			
			Boolean collegatoAMovimento,
			Boolean collegatoACarteContabili,
			Boolean escludiGiaPagatiDaOrdinativoSpesa
			) {
		
		if(statiDocumento!=null && !statiDocumento.isEmpty()) {
			jpql.append(" AND EXISTS (  ");
			jpql.append("     FROM "+aliasDoc+".siacRDocStatos r ");
			jpql.append("     WHERE r.dataCancellazione IS NULL ");
			jpql.append("     AND r.siacDDocStato.docStatoCode IN (:docStatoCodes) ");
			jpql.append(" ) ");
			
			List<String> docStatoCodes = new ArrayList<String>();
			for(SiacDDocStatoEnum sddse : statiDocumento) {
				docStatoCodes.add(sddse.getCodice());
			}
			param.put("docStatoCodes", docStatoCodes);
		}
		
		if(eldocId != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM "+aliasSubdoc+".siacRElencoDocSubdocs sde ");
			jpql.append("     WHERE sde.siacTElencoDoc.eldocId = :eldocId ");
			jpql.append("     AND sde.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("eldocId", eldocId);
		}
		
		
		if(eldocAnno != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM "+aliasSubdoc+".siacRElencoDocSubdocs sde ");
			jpql.append("     WHERE sde.siacTElencoDoc.eldocAnno = :eldocAnno ");
			jpql.append("     AND sde.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("eldocAnno", eldocAnno);
		}
		
		if(eldocNumero != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM "+aliasSubdoc+".siacRElencoDocSubdocs sde ");
			jpql.append("     WHERE sde.siacTElencoDoc.eldocNumero = :eldocNumero ");
			jpql.append("     AND sde.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("eldocNumero", eldocNumero);
		}
		
		if(eldocNumeroDa != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM "+aliasSubdoc+".siacRElencoDocSubdocs sde ");
			jpql.append("     WHERE sde.siacTElencoDoc.eldocNumero >= :eldocNumeroDa ");
			jpql.append("     AND sde.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("eldocNumeroDa", eldocNumeroDa);
		}
		
		if(eldocNumeroA != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM "+aliasSubdoc+".siacRElencoDocSubdocs sde ");
			jpql.append("     WHERE sde.siacTElencoDoc.eldocNumero <= :eldocNumeroA ");
			jpql.append("     AND sde.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("eldocNumeroA", eldocNumeroA);
		}
		
		if(provcAnno != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocProvCassas sdp ");
			jpql.append("     WHERE sdp.siacTProvCassa.provcAnno = :provcAnno ");
			jpql.append("     AND sdp.siacTProvCassa.siacDProvCassaTipo.provcTipoCode = :provcTipoCode ");
			jpql.append("     AND sdp.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("provcAnno", provcAnno);
			param.put("provcTipoCode", tipoProvv.getCodice());
		}
		
		if(provcNumero != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocProvCassas sdp ");
			jpql.append("     WHERE sdp.siacTProvCassa.provcNumero = :provcNumero ");
			jpql.append("     AND sdp.siacTProvCassa.siacDProvCassaTipo.provcTipoCode = :provcTipoCode ");
			jpql.append("     AND sdp.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("provcNumero", provcNumero);
			param.put("provcTipoCode", tipoProvv.getCodice());
		}
		
		if(provcDataEmissione != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocProvCassas sdp ");
			jpql.append("     WHERE sdp.siacTProvCassa.provcDataEmissione = :provcDataEmissione ");
			jpql.append("     AND sdp.siacTProvCassa.siacDProvCassaTipo.provcTipoCode = :provcTipoCode ");
			jpql.append("     AND sdp.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("provcDataEmissione", provcDataEmissione);
			param.put("provcTipoCode",tipoProvv.getCodice());
		}
		
		if(docTipoId != null) {
			jpql.append(" AND "+aliasSubdoc+".siacTDoc.siacDDocTipo.docTipoId = :docTipoId ");
			param.put("docTipoId", docTipoId);
		}
		
		// 	SIAC-6076 introduco nuovo filtro:
		if(!it.csi.siac.siacfinser.StringUtilsFin.isEmpty(tipiDocDaEscludere)){
			//String tipiDocumentoDaEscludere = buildElencoPerClausolaIN(tipiDocDaEscludere);
			jpql.append(" AND "+aliasSubdoc+".siacTDoc.siacDDocTipo.docTipoCode NOT IN (:tipiDocumentoDaEscludere) ");
			param.put("tipiDocumentoDaEscludere", tipiDocDaEscludere);
		}
		//
		
		if(!StringUtils.isEmpty(docNumero)){
			jpql.append(" AND " + Utility.toJpqlSearchLike(aliasSubdoc+".siacTDoc.docNumero", "CONCAT('%', :docNumero, '%')") + " ");
			param.put("docNumero", docNumero);
		}
		
		if(docAnno != null) {
			jpql.append(" AND "+aliasSubdoc+".siacTDoc.docAnno = :docAnno ");
			param.put("docAnno", docAnno);
		}
		

		if(docDataEmissione != null) {
			jpql.append(" AND "+aliasSubdoc+".siacTDoc.docDataEmissione = :docDataEmissione ");
			param.put("docDataEmissione", docDataEmissione);
		}
		
		if(subdocNumero != null){
			jpql.append(" AND "+aliasSubdoc+".subdocNumero = :subdocNumero ");
			param.put("subdocNumero", subdocNumero);
		}
		
		if(movgestNumero != null){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocMovgestTs sdmg ");
			jpql.append("     WHERE sdmg.siacTMovgestT.siacTMovgest.movgestNumero = :movgestNumero");
			jpql.append("     AND sdmg.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("movgestNumero", movgestNumero);
		}
		
		if(movgestAnno != null) {
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocMovgestTs sdmg ");
			jpql.append("     WHERE  sdmg.siacTMovgestT.siacTMovgest.movgestAnno = :movgestAnno");
			jpql.append("     AND sdmg.dataCancellazione IS NULL ");
			jpql.append(" ) ");
		
			param.put("movgestAnno", movgestAnno);
		}
		
		if(collegatoAMovimentoDelloStessoBilancio!=null && bilId != null){
			jpql.append(" AND ");
			if(Boolean.FALSE.equals(collegatoAMovimentoDelloStessoBilancio)){
				jpql.append(" NOT ");
			}
			jpql.append(" EXISTS ( " );
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocMovgestTs sdmg ");
			jpql.append("     WHERE sdmg.dataCancellazione IS NULL ");
			jpql.append("           AND sdmg.siacTMovgestT.siacTMovgest.siacTBil.bilId = :bilId" );
			jpql.append(" ) ");
			
			param.put("bilId", bilId);
		}
		
		if(soggettoCode != null && StringUtils.isNotBlank(soggettoCode)){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM "+aliasDoc+".siacRDocSogs sds ");
			jpql.append("     WHERE sds.siacTSoggetto.soggettoCode = :soggettoCode ");
			jpql.append("     AND sds.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("soggettoCode", soggettoCode);
		}
		
		if(attoammId!=null){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocAttoAmms sda ");
			jpql.append("     WHERE sda.siacTAttoAmm.attoammId = :attoammId ");
			jpql.append("     AND sda.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("attoammId", attoammId);
		}
		
		if(attoammAnno!=null){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocAttoAmms sda ");
			jpql.append("     WHERE sda.siacTAttoAmm.attoammAnno = :attoammAnno ");
			jpql.append("     AND sda.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("attoammAnno", attoammAnno);
		}
		
		if(attoammNumero!=null){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocAttoAmms sda ");
			jpql.append("     WHERE sda.siacTAttoAmm.attoammNumero = :attoammNumero ");
			jpql.append("     AND sda.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("attoammNumero", attoammNumero);
		}
		
		if(associatoAProvvedimentoOAdElenco!=null){
			jpql.append(" AND ");
			if(Boolean.FALSE.equals(associatoAProvvedimentoOAdElenco)){
				jpql.append(" NOT ");
			}
			jpql.append(" ( ");
			jpql.append("     EXISTS ( " );
			jpql.append("         FROM "+aliasSubdoc+".siacRSubdocAttoAmms sda ");
			jpql.append("         WHERE sda.dataCancellazione IS NULL ");
			jpql.append("     ) ");
			jpql.append("     OR EXISTS ( " );
			jpql.append("         FROM "+aliasSubdoc+".siacRElencoDocSubdocs sde ");
			jpql.append("         WHERE sde.dataCancellazione IS NULL ");
			jpql.append("     ) ");
			jpql.append(" ) ");
		}

		if(attoammTipoId != null){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocAttoAmms sda ");
			jpql.append("     WHERE sda.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId = :attoammTipoId ");
			jpql.append("     AND sda.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("attoammTipoId", attoammTipoId);
		}

		if(uidStruttAmm != null){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocAttoAmms sda ");
			jpql.append("     WHERE sda.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM sda.siacTAttoAmm.siacRAttoAmmClasses sdac ");
			jpql.append("         WHERE sdac.siacTClass.classifId = :classifId ");
			jpql.append("         AND sdac.dataCancellazione IS NULL ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			
			param.put("classifId", uidStruttAmm);
		}
		
		if(annoCapitolo != null && annoCapitolo != 0){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocMovgestTs sdmg ");
			jpql.append("     WHERE sdmg.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( FROM sdmg.siacTMovgestT.siacTMovgest.siacRMovgestBilElems rbe ");
			jpql.append("     			   WHERE rbe.dataCancellazione IS NULL ");
			jpql.append("     			   AND rbe.siacTBilElem.siacTBil.siacTPeriodo.anno = :annoCapitolo ");
			jpql.append("     )  ");
			jpql.append("      ");
			jpql.append(" ) ");
			
			param.put("annoCapitolo", annoCapitolo.toString());
		}
		
		if(elemCode != null && elemCode != 0){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocMovgestTs sdmg ");
			jpql.append("     WHERE sdmg.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( FROM sdmg.siacTMovgestT.siacTMovgest.siacRMovgestBilElems rbe ");
			jpql.append("     			   WHERE rbe.dataCancellazione IS NULL ");
			jpql.append("     			   AND rbe.siacTBilElem.elemCode = :elemCode ");
			jpql.append("     )  ");
			jpql.append("      ");
			jpql.append(" ) ");
			
			param.put("elemCode", elemCode.toString() );
		}
		
		if(elemCode2 != null && elemCode2 != 0){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocMovgestTs sdmg ");
			jpql.append("     WHERE sdmg.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( FROM sdmg.siacTMovgestT.siacTMovgest.siacRMovgestBilElems rbe ");
			jpql.append("     			   WHERE rbe.dataCancellazione IS NULL ");
			jpql.append("     			   AND rbe.siacTBilElem.elemCode2 = :elemCode2 ");
			jpql.append("     )  ");
			jpql.append("      ");
			jpql.append(" ) ");
			
			param.put("elemCode2", elemCode2.toString());
		}
		
		if(elemCode3 != null && elemCode3 != 0){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocMovgestTs sdmg ");
			jpql.append("     WHERE sdmg.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( FROM sdmg.siacTMovgestT.siacTMovgest.siacRMovgestBilElems rbe ");
			jpql.append("     			   WHERE rbe.dataCancellazione IS NULL ");
			jpql.append("     			   AND rbe.siacTBilElem.elemCode3 = :elemCode3 ");
			jpql.append("     )  ");
			jpql.append("      ");
			jpql.append(" ) ");
			
		   param.put("elemCode3", elemCode3.toString());
		}
		
		if(importoDaPagareOIncassareZero!=null){
			jpql.append(" AND ");
			if(Boolean.FALSE.equals(importoDaPagareOIncassareZero)){
				jpql.append(" NOT ");
			}
			jpql.append(" (("+aliasSubdoc+".subdocImporto - "+aliasSubdoc+".subdocImportoDaDedurre) = 0 ) ");
		}
		
		if(importoDaPagareOIncassareMaggioreDiZero!=null){
			jpql.append(" AND ");
			if(Boolean.FALSE.equals(importoDaPagareOIncassareZero)){
				jpql.append(" NOT ");
			}
			jpql.append(" (("+aliasSubdoc+".subdocImporto - "+aliasSubdoc+".subdocImportoDaDedurre) > 0 ) ");
		}
		
		//filtro su un importo liquidabile preciso richiesto:
		if(importoLiquidabileEsatto!=null){
			jpql.append(" AND ");
			jpql.append(" (("+aliasSubdoc+".subdocImporto - "+aliasSubdoc+".subdocImportoDaDedurre) = :importoLiquidabileEsatto ) ");
			//sul db i numeric sono limitati a due decimali:
			BigDecimal importoLiquidabileEsattoDueDecimali = importoLiquidabileEsatto.setScale(2, RoundingMode.DOWN);
			//
			param.put("importoLiquidabileEsatto", importoLiquidabileEsattoDueDecimali);
		}
		//
		
		if(rilevatiIvaConRegistrazioneONonRilevantiIva!=null){
			jpql.append(" AND ");
			if(Boolean.FALSE.equals(rilevatiIvaConRegistrazioneONonRilevantiIva)){
				jpql.append(" NOT ");
			}
			jpql.append(" ( ");
			jpql.append("     ( ");
			jpql.append("         ("+aliasSubdoc+".subdocNregIva IS NOT NULL AND "+aliasSubdoc+".subdocNregIva <> '') ");
			jpql.append("         AND EXISTS( ");
			jpql.append("             FROM "+aliasSubdoc+".siacRSubdocAttrs sda ");
			jpql.append("             WHERE sda.dataCancellazione IS NULL ");
			jpql.append("             AND sda.siacTAttr.attrCode = :attrCode ");
			jpql.append("             AND sda.boolean_ = :flagRilevanteIva ");
			jpql.append("         ) ");
			jpql.append("     ) ");
			jpql.append("     OR ");
			jpql.append("     ( ");
			jpql.append("         ("+aliasSubdoc+".subdocNregIva IS NULL OR "+aliasSubdoc+".subdocNregIva = '') ");
			jpql.append("         AND NOT EXISTS( ");
			jpql.append("             FROM "+aliasSubdoc+".siacRSubdocAttrs sda ");
			jpql.append("             WHERE sda.dataCancellazione IS NULL ");
			jpql.append("             AND sda.siacTAttr.attrCode = :attrCode ");
			jpql.append("             AND sda.boolean_ = :flagRilevanteIva ");
			jpql.append("         ) ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			
			param.put("attrCode", SiacTAttrEnum.FlagRilevanteIVA.getCodice());
			param.put("flagRilevanteIva", "S");
		}
		
		
		if(collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto!=null){
			jpql.append(" AND ");
			
			if(Boolean.FALSE.equals(collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto)){
				jpql.append(" NOT ");
			}			
			
			jpql.append(" EXISTS( ");
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocLiquidaziones rl, SiacTLiquidazione l ");
			jpql.append("     WHERE rl.dataCancellazione IS NULL ");
			jpql.append("     AND rl.siacTLiquidazione = l ");
			
			//Liquidazione in stato VALIDO
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM l.siacRLiquidazioneStatos ls ");
			jpql.append("         WHERE ls.dataCancellazione IS NULL ");
			jpql.append("         AND (ls.dataFineValidita IS NULL OR ls.dataFineValidita > CURRENT_TIMESTAMP) ");
			jpql.append("         AND ls.siacDLiquidazioneStato.liqStatoCode = :liqStatoCode");
			jpql.append("     ) ");
			
			param.put("liqStatoCode", SiacDLiquidazioneStatoEnum.Valido.getCodice());
			
			//Liquidazione in con AttoAmministrativo in stato DEFINITIVO
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM l.siacRLiquidazioneAttoAmms laa, SiacTAttoAmm aa ");
			jpql.append("         WHERE laa.dataCancellazione IS NULL ");
			jpql.append("         AND laa.siacTAttoAmm = aa ");
			jpql.append("         AND aa.dataCancellazione IS NULL ");
			jpql.append("         AND EXISTS ( ");
			jpql.append("             FROM aa.siacRAttoAmmStatos aas ");
			jpql.append("             WHERE aas.dataCancellazione IS NULL ");
			jpql.append("             AND aas.siacDAttoAmmStato.attoammStatoCode = :attoammStatoCode");
			jpql.append("         ) ");
			jpql.append("     ) ");
			
			param.put("attoammStatoCode", SiacDAttoAmmStatoEnum.DEFINITIVO.getCodice());
//		
			//Liquidazione con importo disponibilita pagare >= importo da liquidare del subdocumento
			jpql.append("     AND (l.liqImporto - ( ");
			jpql.append("        		 SELECT COALESCE(SUM(otd.ordTsDetImporto), 0) ");
			jpql.append("        		 FROM SiacTOrdinativoTsDet otd, SiacTOrdinativoT ot , SiacTOrdinativo o");
			jpql.append("        		 WHERE otd.dataCancellazione IS NULL ");
			jpql.append("        		 AND otd.siacDOrdinativoTsDetTipo.ordTsDetTipoCode = :ordTsDetTipoCode ");
			jpql.append("         		 AND otd.siacTOrdinativoT = ot ");
			jpql.append("         		 AND ot.siacTOrdinativo = o ");
			jpql.append("         		 AND EXISTS ( ");
			jpql.append("            	 	FROM ot.siacRLiquidazioneOrds lo ");
			jpql.append("            		WHERE lo.dataCancellazione IS NULL " );
			jpql.append("             		AND lo.siacTLiquidazione = l ");
			jpql.append("         		    ) ");
			jpql.append("         		 AND EXISTS ( ");
			jpql.append("             	 	FROM o.siacROrdinativoStatos so ");
			jpql.append("                	WHERE so.dataCancellazione IS NULL " );
			jpql.append("         			AND ( so.dataFineValidita IS NULL OR so.dataFineValidita > CURRENT_TIMESTAMP) ");
			jpql.append("               	AND so.siacDOrdinativoStato.ordinativoStatoCode NOT IN ( :ordinativoStatoCode) ");
			jpql.append("         			) ");
			jpql.append("     			) ");
			jpql.append("     	  ) >= ("+aliasSubdoc+".subdocImporto - "+aliasSubdoc+".subdocImportoDaDedurre) ");
			
			param.put("ordTsDetTipoCode", "A");
			param.put("ordinativoStatoCode", SiacDOrdinativoStatoEnum.Annullato.getCodice());
//			ordinativoStatoCode
			jpql.append(" ) ");
			

		}
		
		if(siacDAttoAmmStatoEnums != null && !siacDAttoAmmStatoEnums.isEmpty()) {
			// il provvedimento e' in stato definito
			jpql.append(" AND EXISTS ( ")
				.append("     FROM "+aliasSubdoc+".siacRSubdocAttoAmms srsaa, SiacTAttoAmm staa ")
				.append("     WHERE srsaa.dataCancellazione IS NULL ")
				.append("     AND srsaa.siacTAttoAmm = staa ")
				.append("     AND EXISTS ( ")
				.append("         FROM staa.siacRAttoAmmStatos sraas ")
				.append("         WHERE sraas.dataCancellazione IS NULL ")
				.append("         AND sraas.siacDAttoAmmStato.attoammStatoCode IN (:siacDAttoAmmStatos) ")
				.append("     ) ")
				.append(" ) ");
			
			List<String> siacDAttoAmmStatos = new ArrayList<String>();
			for(SiacDAttoAmmStatoEnum sdaase : siacDAttoAmmStatoEnums) {
				siacDAttoAmmStatos.add(sdaase.getCodice());
			}
			param.put("siacDAttoAmmStatos", siacDAttoAmmStatos);
		}
		// Associazione all'ordinativo
		if(associatoAdOrdinativo != null) {
			jpql.append(" AND ");
			if(Boolean.FALSE.equals(associatoAdOrdinativo)){
				jpql.append(" NOT ");
			}
			jpql.append(" EXISTS ( " )
				.append("     FROM "+aliasSubdoc+".siacRSubdocOrdinativoTs srsot, SiacTOrdinativo o ")
				.append("     WHERE srsot.dataCancellazione IS NULL ")
				.append("     AND o.dataCancellazione IS NULL ")
				
				.append("     AND o = srsot.siacTOrdinativoT.siacTOrdinativo ")
				
				.append("     AND EXISTS ( ")
				.append("         FROM o.siacROrdinativoStatos ros ")
				.append("         WHERE ros.dataCancellazione IS NULL ")
				.append("         AND ( ros.dataFineValidita IS NULL OR ros.dataFineValidita > CURRENT_TIMESTAMP) ")
				.append("         AND ros.siacDOrdinativoStato.ordinativoStatoCode NOT IN (:ordinativoStatoCode) ")
				.append("     ) ")
				
				
				.append(" ) ");
			
			param.put("ordinativoStatoCode", SiacDOrdinativoStatoEnum.Annullato.getCodice());
		}
		
		
		
		//NUOVI FILTRI INTRODOTTI RISPETTO AL SERVIZIO DI BIL:
		
		if(collegatoAMovimento!=null){
			jpql.append(" AND ");
			if(Boolean.FALSE.equals(collegatoAMovimento)){
				jpql.append(" NOT ");
			}
			jpql.append(" EXISTS ( FROM SiacRSubdocMovgestT srsmg " );
			jpql.append("     WHERE srsmg.dataCancellazione IS NULL AND ");
			jpql.append("     srsmg.siacTSubdoc.subdocId = "+aliasSubdoc+".subdocId ");
			jpql.append(" ) ");
		}
		
		if(collegatoACarteContabili!=null){
			jpql.append(" AND ");
			if(Boolean.FALSE.equals(collegatoACarteContabili)){
				jpql.append(" NOT ");
			}
			jpql.append(" EXISTS ( FROM SiacRCartacontDetSubdoc srccds " );
			jpql.append(" WHERE ");
			jpql.append(DataValiditaUtil.validitaForQuery("srccds"));
			jpql.append("   AND  srccds.siacTSubdoc.subdocId = "+aliasSubdoc+".subdocId ");
			jpql.append(" ) ");
			DataValiditaUtil.aggiungiParametroDataValidita(param);
		}
		
		if(escludiGiaPagatiDaOrdinativoSpesa!=null && escludiGiaPagatiDaOrdinativoSpesa==true){
			
			jpql.append(" AND ");
			
			jpql.append(" NOT EXISTS( ");
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocLiquidaziones rl, SiacTLiquidazione l ");
			jpql.append("     WHERE rl.dataCancellazione IS NULL ");
			jpql.append("     AND rl.siacTLiquidazione = l ");
			
			//Liquidazione in stato VALIDO
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM l.siacRLiquidazioneStatos ls ");
			jpql.append("         WHERE ls.dataCancellazione IS NULL ");
			jpql.append("         AND (ls.dataFineValidita IS NULL OR ls.dataFineValidita > CURRENT_TIMESTAMP) ");
			jpql.append("         AND ls.siacDLiquidazioneStato.liqStatoCode = :liqStatoCode");
			jpql.append("     ) ");
			
			param.put("liqStatoCode", SiacDLiquidazioneStatoEnum.Valido.getCodice());
			
			//Liquidazione in con AttoAmministrativo in stato DEFINITIVO
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM l.siacRLiquidazioneAttoAmms laa, SiacTAttoAmm aa ");
			jpql.append("         WHERE laa.dataCancellazione IS NULL ");
			jpql.append("         AND laa.siacTAttoAmm = aa ");
			jpql.append("         AND aa.dataCancellazione IS NULL ");
			jpql.append("         AND EXISTS ( ");
			jpql.append("             FROM aa.siacRAttoAmmStatos aas ");
			jpql.append("             WHERE aas.dataCancellazione IS NULL ");
			jpql.append("             AND aas.siacDAttoAmmStato.attoammStatoCode = :attoammStatoCode");
			jpql.append("         ) ");
			jpql.append("     ) ");
			
			param.put("attoammStatoCode", SiacDAttoAmmStatoEnum.DEFINITIVO.getCodice());
//		
			//Liquidazione con importo disponibilita pagare >= importo da liquidare del subdocumento
			jpql.append("     AND (l.liqImporto - ( ");
			jpql.append("        		 SELECT COALESCE(SUM(otd.ordTsDetImporto), 0) ");
			jpql.append("        		 FROM SiacTOrdinativoTsDet otd, SiacTOrdinativoT ot , SiacTOrdinativo o");
			jpql.append("        		 WHERE otd.dataCancellazione IS NULL ");
			jpql.append("        		 AND otd.siacDOrdinativoTsDetTipo.ordTsDetTipoCode = :ordTsDetTipoCode ");
			jpql.append("         		 AND otd.siacTOrdinativoT = ot ");
			jpql.append("         		 AND ot.siacTOrdinativo = o ");
			jpql.append("         		 AND EXISTS ( ");
			jpql.append("            	 	FROM ot.siacRLiquidazioneOrds lo ");
			jpql.append("            		WHERE lo.dataCancellazione IS NULL " );
			jpql.append("             		AND lo.siacTLiquidazione = l ");
			jpql.append("         		    ) ");
			jpql.append("         		 AND EXISTS ( ");
			jpql.append("             	 	FROM o.siacROrdinativoStatos so ");
			jpql.append("                	WHERE so.dataCancellazione IS NULL " );
			jpql.append("         			AND ( so.dataFineValidita IS NULL OR so.dataFineValidita > CURRENT_TIMESTAMP) ");
			jpql.append("               	AND so.siacDOrdinativoStato.ordinativoStatoCode NOT IN ( :ordinativoStatoCode) ");
			jpql.append("         			) ");
			jpql.append("     			) ");
			jpql.append("     	  ) != l.liqImporto ");
			
			param.put("ordTsDetTipoCode", "A");
			param.put("ordinativoStatoCode", SiacDOrdinativoStatoEnum.Annullato.getCodice());
//			ordinativoStatoCode
			jpql.append(" ) ");
			

		}
		
//		if(statiDocumento!=null && !statiDocumento.isEmpty()) {
//			jpql.append(" AND EXISTS (  ");
//			jpql.append("     FROM SiacRDocStato srds ");
//			jpql.append("     WHERE srds.dataCancellazione IS NULL ");
//			jpql.append("     AND srds.siacTDoc.docId = "+aliasDoc+" ");
//			jpql.append("     AND srds.siacDDocStato.docStatoCode IN (:docStatoCodes) ");
//			jpql.append(" ) ");
//			
//			List<String> docStatoCodes = new ArrayList<String>();
//			for(SiacDDocStatoEnum sddse : statiDocumento) {
//				docStatoCodes.add(sddse.getCodice());
//			}
//			param.put("docStatoCodes", docStatoCodes);
//		}
		
		
	}
	
	
	public boolean giaPagatoDaOrdinativoSpesa(Integer subdocId, DatiOperazioneDto datiOperazione){
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, datiOperazione.getTs());
		
		jpql.append(" SELECT sd.subdocId FROM SiacTSubdoc sd ");
		
		jpql.append(" WHERE sd.subdocId = :subdocId ");
		param.put("subdocId", subdocId);
		
		jpql.append(" AND EXISTS( ");
		jpql.append("     FROM sd.siacRSubdocLiquidaziones rl, SiacTLiquidazione l ");
		jpql.append("     WHERE ");
		jpql.append("     rl.siacTLiquidazione = l ");
		jpql.append("     AND ").append(DataValiditaUtil.validitaForQuery("rl"));
		
		//Liquidazione in stato VALIDO
		jpql.append("     AND EXISTS ( ");
		jpql.append("         FROM l.siacRLiquidazioneStatos ls ");
		jpql.append("         WHERE  ");
		jpql.append("         ls.siacDLiquidazioneStato.liqStatoCode = :liqStatoCode");
		jpql.append("         AND ").append(DataValiditaUtil.validitaForQuery("ls"));
		jpql.append("     ) ");
		
		param.put("liqStatoCode", SiacDLiquidazioneStatoEnum.Valido.getCodice());
		
		//Liquidazione in con AttoAmministrativo in stato DEFINITIVO
		jpql.append("     AND EXISTS ( ");
		jpql.append("         FROM l.siacRLiquidazioneAttoAmms laa, SiacTAttoAmm aa ");
		jpql.append("         WHERE ");
		jpql.append("         laa.siacTAttoAmm = aa ");
		jpql.append("         AND ").append(DataValiditaUtil.validitaForQuery("laa"));
		jpql.append("         AND ").append(DataValiditaUtil.validitaForQuery("aa"));
		jpql.append("         AND EXISTS ( ");
		jpql.append("             FROM aa.siacRAttoAmmStatos aas ");
		jpql.append("             WHERE ");
		jpql.append("             aas.siacDAttoAmmStato.attoammStatoCode = :attoammStatoCode");
		jpql.append("             AND ").append(DataValiditaUtil.validitaForQuery("aas"));
		jpql.append("         ) ");
		jpql.append("     ) ");
		
		param.put("attoammStatoCode", SiacDAttoAmmStatoEnum.DEFINITIVO.getCodice());
//		
		//Liquidazione con importo disponibilita pagare >= importo da liquidare del subdocumento
		jpql.append("     AND (l.liqImporto - ( ");
		jpql.append("        		 SELECT COALESCE(SUM(otd.ordTsDetImporto), 0) ");
		jpql.append("        		 FROM SiacTOrdinativoTsDet otd, SiacTOrdinativoT ot , SiacTOrdinativo o");
		jpql.append("        		 WHERE ");
		jpql.append("        		 otd.siacDOrdinativoTsDetTipo.ordTsDetTipoCode = :ordTsDetTipoCode ");
		jpql.append("         		 AND otd.siacTOrdinativoT = ot ");
		jpql.append("         		 AND ot.siacTOrdinativo = o ");
		jpql.append("                AND ").append(DataValiditaUtil.validitaForQuery("otd"));
		jpql.append("         		 AND EXISTS ( ");
		jpql.append("            	 	FROM ot.siacRLiquidazioneOrds lo ");
		jpql.append("            		WHERE " );
		jpql.append("             		lo.siacTLiquidazione = l ");
		jpql.append("                   AND ").append(DataValiditaUtil.validitaForQuery("lo"));
		jpql.append("         		    ) ");
		jpql.append("         		 AND EXISTS ( ");
		jpql.append("             	 	FROM o.siacROrdinativoStatos so ");
		jpql.append("                	WHERE " );
		jpql.append("               	so.siacDOrdinativoStato.ordinativoStatoCode NOT IN ( :ordinativoStatoCode) ");
		jpql.append("                   AND ").append(DataValiditaUtil.validitaForQuery("so"));
		jpql.append("         			) ");
		jpql.append("     			) ");
		jpql.append("     	  ) != l.liqImporto ");
		
		param.put("ordTsDetTipoCode", "A");
		param.put("ordinativoStatoCode", SiacDOrdinativoStatoEnum.Annullato.getCodice());
//			ordinativoStatoCode
		jpql.append(" ) ");
		
		Query query =  createQuery(jpql.toString(), param);
			
		List<Integer> results = query.getResultList();
		
		return !it.csi.siac.siacfinser.StringUtilsFin.isEmpty(results);

	}
	
	
	public <ST extends SiacTBase>  List<ST> ricercaBySiacTDocFinMassive(List<SiacTDocFin> listaInput, String nomeEntity) {
		List<ST> listaRitorno = new ArrayList<ST>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTDocFin>> esploso = it.csi.siac.siacfinser.StringUtilsFin.esplodiInListe(listaInput, AbstractDao.DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTDocFin> listaIt : esploso){
					List<ST> risultatoParziale = ricercaBySiacTDocFinMassiveCORE(listaIt, nomeEntity);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtil.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTDocFin e il nome della Entity da cerca vengono restituiti TUTTI I DISTINTI 
	 * oggetti del tipo indicato in nomeEntity in relazione con i SiacTDocFin indicati.
	 * ESEMPIO: Se indico nomeEntity = "SiacRDocStatoFin" verranno restituiti tutti i distinti record SiacRDocStatoFin
	 * in relazione con i record di listaSiacTDocFin indicati
	 */
	@SuppressWarnings("unchecked")
	private <ST extends SiacTBase>  List<ST> ricercaBySiacTDocFinMassiveCORE(List<SiacTDocFin> listaSiacTDocFin, String nomeEntity) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<ST> listaRitorno = new ArrayList<ST>();
		
		if(listaSiacTDocFin!=null && listaSiacTDocFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM "+nomeEntity+" rs WHERE ");
			
			jpql.append(" rs.siacTDoc.docId IN ( ");
			int i =0;
			for(SiacTDocFin it: listaSiacTDocFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getDocId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rs"));
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, TimingUtils.getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	

}
