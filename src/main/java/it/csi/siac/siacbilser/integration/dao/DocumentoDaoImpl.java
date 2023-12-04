/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacRDoc;
import it.csi.siac.siacbilser.integration.entity.SiacRDocAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRDocClass;
import it.csi.siac.siacbilser.integration.entity.SiacRDocSog;
import it.csi.siac.siacbilser.integration.entity.SiacRDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTRegistrounicoDoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class DocumentoDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DocumentoDaoImpl extends JpaDao<SiacTDoc, Integer> implements DocumentoDao {
	
	@Override
	public SiacTDoc findById(Integer id) {
		SiacTDoc siacTDoc = super.findById(id);
		return siacTDoc;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.DocumentoDao#create(it.csi.siac.siacbilser.integration.entity.SiacTDoc)
	 */
	public SiacTDoc create(SiacTDoc c){
		
		Date now = new Date();
		c.setDataModificaInserimento(now);
		
		if(c.getSiacRDocAttrs()!=null){
			for(SiacRDocAttr att : c.getSiacRDocAttrs()){
				att.setDataModificaInserimento(now);
			}
		}

		if(c.getSiacRDocClasses()!=null){
			for(SiacRDocClass classif : c.getSiacRDocClasses()){
				classif.setDataModificaInserimento(now);
			}
		}
		
//		if(c.getSiacRDocOneres()!=null){
//			for(SiacRDocOnere onere : c.getSiacRDocOneres()){
//				onere.setDataModificaInserimento(now);
//			}
//		}
		
		if(c.getSiacRDocsPadre()!=null){
			for(SiacRDoc rDoc : c.getSiacRDocsPadre()){
				rDoc.setDataModificaInserimento(now);
			}
		}
		
		if(c.getSiacRDocsFiglio()!=null){
			for(SiacRDoc rDoc : c.getSiacRDocsFiglio()){
				rDoc.setDataModificaInserimento(now);
			}
		}
		
		if(c.getSiacRDocSogs()!=null){
			for(SiacRDocSog sog : c.getSiacRDocSogs()){
				sog.setDataModificaInserimento(now);
			}
		}
		
		if(c.getSiacRDocStatos()!=null){
			for(SiacRDocStato stato : c.getSiacRDocStatos()){
				stato.setDataModificaInserimento(now);
			}
		}
		
		if(c.getSiacTRegistrounicoDoc()!=null){
			SiacTRegistrounicoDoc ru = c.getSiacTRegistrounicoDoc();
			ru.setDataModificaInserimento(now);
			ru.setUid(null); //viene inserito sempre contestualmente al documento!
		}
		
		c.setUid(null);		
		super.save(c);
		return c;
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	public SiacTDoc update(SiacTDoc d){
		SiacTDoc dAttuale = this.findById(d.getUid());
		
		Date now = new Date();
		d.setDataModificaAggiornamento(now);		
		
		//cancellazione elementi collegati		
		if(dAttuale.getSiacRDocAttrs()!=null){
			for(SiacRDocAttr att : dAttuale.getSiacRDocAttrs()){
				att.setDataCancellazioneIfNotSet(now);
			}
		}

		if(dAttuale.getSiacRDocClasses()!=null){
			for(SiacRDocClass classif : dAttuale.getSiacRDocClasses()){
				classif.setDataCancellazioneIfNotSet(now);
			}
		}
		
//		if(dAttuale.getSiacRDocOneres()!=null){
//			for(SiacRDocOnere onere : dAttuale.getSiacRDocOneres()){
//				onere.setDataCancellazioneIfNotSet(now);
//			}
//		}
		
		if(dAttuale.getSiacRDocsPadre()!=null){
			for(SiacRDoc rDoc : dAttuale.getSiacRDocsPadre()){
				rDoc.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(dAttuale.getSiacRDocsFiglio()!=null){
			for(SiacRDoc rDoc : dAttuale.getSiacRDocsFiglio()){
				rDoc.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(dAttuale.getSiacRDocSogs()!=null){
			for(SiacRDocSog sog : dAttuale.getSiacRDocSogs()){
				sog.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(dAttuale.getSiacRDocStatos()!=null){
			for(SiacRDocStato stato : dAttuale.getSiacRDocStatos()){
				stato.setDataCancellazioneIfNotSet(now);
			}
		}		
		
		
		//inserimento elementi nuovi		
		if(d.getSiacRDocAttrs()!=null){
			for(SiacRDocAttr att : d.getSiacRDocAttrs()){
				att.setDataModificaInserimento(now);
			}
		}

		if(d.getSiacRDocClasses()!=null){
			for(SiacRDocClass classif : d.getSiacRDocClasses()){
				classif.setDataModificaInserimento(now);
			}
		}
		
//		if(d.getSiacRDocOneres()!=null){
//			for(SiacRDocOnere onere : d.getSiacRDocOneres()){
//				onere.setDataModificaInserimento(now);
//			}
//		}
		
		if(d.getSiacRDocsPadre()!=null){
			for(SiacRDoc rDoc : d.getSiacRDocsPadre()){
				rDoc.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRDocsFiglio()!=null){
			for(SiacRDoc rDoc : d.getSiacRDocsFiglio()){
				rDoc.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRDocSogs()!=null){
			for(SiacRDocSog sog : d.getSiacRDocSogs()){
				sog.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRDocStatos()!=null){
			for(SiacRDocStato stato : d.getSiacRDocStatos()){
				stato.setDataModificaInserimento(now);
			}
		}
		
		//Il numero di registro unico non puo' essere mutato.
		d.setSiacTRegistrounicoDoc(dAttuale.getSiacTRegistrounicoDoc());
		
		super.update(d);
		return d;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.DocumentoDao#ricercaSinteticaDocumento(java.lang.Integer, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum, java.lang.Integer, java.lang.String, java.lang.String, java.util.Date, java.lang.Integer, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum, java.lang.Boolean, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<SiacTDoc> ricercaSinteticaDocumento(Integer enteProprietarioId, SiacDDocFamTipoEnum docFamTipoCode,
			Integer docAnno, 
			String docNumero, 
			String docNumeroEsatto,
			Date docDataEmissione,
			Date docDataOperazione,
			Integer docTipoId, 
			SiacDDocStatoEnum docStato,
			SiacDDocStatoEnum docStatoToExclude,
			
			Boolean flagRilevanteIva,
			
			Integer impegnoId, // movimento
			Integer accertamentoId,
			Integer attoAmministrativoId, // provvedimento

			String  annoProv,
			Integer numProv,
			Integer attoammTipoId,
			Integer sacId,
			
			Integer soggettoId,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer liqAnno, 
			BigDecimal liqNumero, 
			Integer bilId,
			String anno,
			Boolean collegatoCEC,
			Integer annoRepertorio,
			Date dataRepertorio, 
			String numeroRepertorio, 
			String registroRepertorio, 
			Boolean contabilizzaGenPcc,
			String statoSDI, //SIAC-6565-CR1215
			String numerPreDoc,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaDocumento";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaDocumento( jpql, param, enteProprietarioId, docFamTipoCode, docAnno, docNumero, docNumeroEsatto, docDataEmissione,docDataOperazione, docTipoId, docStato, docStatoToExclude,
				flagRilevanteIva, impegnoId, accertamentoId, attoAmministrativoId, annoProv,numProv, attoammTipoId,sacId, soggettoId, eldocAnno, eldocNumero, liqAnno, liqNumero, bilId, anno, collegatoCEC, annoRepertorio, dataRepertorio, numeroRepertorio, registroRepertorio, contabilizzaGenPcc,statoSDI,numerPreDoc);
		
		jpql.append(" ORDER BY d.docAnno, d.docNumero, d.dataCreazione ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.DocumentoDao#ricercaSinteticaDocumentoImportoTotale(java.lang.Integer, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum, java.lang.Integer, java.lang.String, java.lang.String, java.util.Date, java.lang.Integer, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum, java.lang.Boolean, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, org.springframework.data.domain.Pageable)
	 */
	@Override
	public BigDecimal ricercaSinteticaDocumentoImportoTotale(Integer enteProprietarioId, SiacDDocFamTipoEnum docFamTipoCode,
			Integer docAnno, 
			String docNumero, 
			String docNumeroEsatto, 
			Date docDataEmissione,
			Date docDataOperazione,
			Integer docTipoId, 
			SiacDDocStatoEnum docStato,
			SiacDDocStatoEnum docStatoToExclude, 
			Boolean flagRilevanteIva,
			
			Integer impegnoId, // movimento
			Integer accertamentoId,
			Integer attoAmministrativoId, // provvedimento
			String annoProv,
			Integer numProv,
			Integer attoammTipoId,
			Integer sacId,
			Integer soggettoId,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer liqAnno, 
			BigDecimal liqNumero, 
			Integer bilId,
			String anno,
			
			Boolean collegatoCEC,
			Integer annoRepertorio,
			Date dataRepertorio, 
			String numeroRepertorio, 
			String registroRepertorio,
			Boolean contabilizzaGenPcc,
			String statoSDI, //SIAC-6565-CR1215
			String numeroPreDoc,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaDocumentoImportoTotale";
		
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT SUM(d.docImporto) ");
		
		componiQueryRicercaSinteticaDocumento( jpql, param, enteProprietarioId, docFamTipoCode, docAnno, docNumero, docNumeroEsatto,
				docDataEmissione, docDataOperazione,docTipoId, docStato, docStatoToExclude,
				flagRilevanteIva, impegnoId, accertamentoId, attoAmministrativoId, annoProv, numProv, attoammTipoId,sacId,soggettoId, eldocAnno, eldocNumero, liqAnno, liqNumero, bilId, anno, 
				collegatoCEC, annoRepertorio, dataRepertorio, numeroRepertorio, registroRepertorio, contabilizzaGenPcc, statoSDI, numeroPreDoc);
		
		Query query = createQuery(jpql.toString(), param);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		log.info(methodName, "returning: "+result);
		
		return result;
	}
	
	

	/**
	 * Compone la query per la ricerca sintetica del documento.
	 * 
	 * @param jpql the jpql
	 * @param param the param
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param docFamTipoCode codice del tipo famiglia
	 * @param docAnno anno del documento
	 * @param docNumero stringa da cercare all'interno del numero del documento
	 * @param docNumeroEsatto il numero documento esatto da ricercare
	 * @param docDataEmissione data di emissione
	 * @param docTipoId uid del tipo documento
	 * @param docStato stato del documento
	 * @param docStatoToExclude stato che non deve essere verificato
	 * @param flagRilevanteIva flag rilevante iva
	 * @param impegnoId uid dell'impegno
	 * @param accertamentoId uid dell'accertamento
	 * @param attoAmministrativoId uid dell'atto amministrativo
	 * @param soggettoId uid del soggetto
	 * @param eldocAnno anno dell'elenco documenti
	 * @param eldocNumero numero dell'elenco documenti
	 * @param liqAnno anno della liquidazione
	 * @param liqNumero numero della liquidazione
	 * @param bilId id del bilancio
	 * @param anno the anno
	 * @param flagCollegatoCEC flag collegato CEC
	 */
	private void componiQueryRicercaSinteticaDocumento(StringBuilder jpql, Map<String, Object> param, 
			Integer enteProprietarioId, SiacDDocFamTipoEnum docFamTipoCode, Integer docAnno,
			String docNumero,String docNumeroEsatto, Date docDataEmissione,Date docDataOperazione, Integer docTipoId, SiacDDocStatoEnum docStato, SiacDDocStatoEnum docStatoToExclude,  
			Boolean flagRilevanteIva, Integer impegnoId, Integer accertamentoId, Integer attoAmministrativoId,String annoProv, Integer numProv,Integer attoammTipoId,Integer sacId, Integer soggettoId, Integer eldocAnno,
			Integer eldocNumero,Integer liqAnno, BigDecimal liqNumero, Integer bilId, String anno, Boolean flagCollegatoCEC, Integer annoRepertorio,
			Date dataRepertorio, String numeroRepertorio, String registroRepertorio, Boolean contabilizzaGenPcc,String statoSDI, String numeroPreDoc) {
		
		jpql.append("FROM SiacTDoc d ");
		jpql.append(" WHERE ");
		jpql.append(" d.dataCancellazione IS NULL ");
		jpql.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = :docFamTipoCode ");
		//siacTDocPadre.getSiacDDocTipo().getSiacDDocFamTipo().getDocFamTipoCode();		
		param.put("docFamTipoCode", docFamTipoCode.getCodice());
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(!StringUtils.isEmpty(docNumero)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.docNumero", "CONCAT('%', :docNumero, '%')") + " ");
			param.put("docNumero", docNumero);			
		}
		
		if(!StringUtils.isEmpty(docNumeroEsatto)){
			jpql.append(" AND d.docNumero = :docNumeroEsatto ");
			param.put("docNumeroEsatto", docNumeroEsatto);			
		}
		
		
		if(docAnno != null) {
			jpql.append(" AND d.docAnno = :docAnno");
			param.put("docAnno", docAnno);		
		}
		
    	if(statoSDI != null && !statoSDI.isEmpty()){ 
			
			jpql.append(" AND  d.statoSDI = :statoSDI ");
			param.put("statoSDI", statoSDI);
		}

		if(docDataEmissione != null) {
			jpql.append(" AND d.docDataEmissione = :docDataEmissione");
			param.put("docDataEmissione", docDataEmissione);		
		}
		
		// SIAC 6677
		// SIAC-6840 si riabilita il metodo
		if(docDataOperazione != null) {
			jpql.append(" AND d.docDataOperazione = :docDataOperazione");
			param.put("docDataOperazione", docDataOperazione);		
		}
		
		if(docTipoId != null && docTipoId!=0) {
			jpql.append(" AND d.siacDDocTipo.docTipoId = :docTipoId");
			param.put("docTipoId", docTipoId);		
		}
		
		if(docStato != null && docStato.getCodice()!=null) {
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacRDocStatos ds ");
			jpql.append("	WHERE ds.siacDDocStato.docStatoCode = :docStato ");
			jpql.append("	AND ds.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("docStato", docStato.getCodice());
		}
		
		if(docStatoToExclude != null && docStatoToExclude.getCodice()!=null) {
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacRDocStatos ds ");
			jpql.append("	WHERE ds.siacDDocStato.docStatoCode <> :docStatoToExclude ");
			jpql.append("	AND ds.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("docStatoToExclude", docStatoToExclude.getCodice());
		}
		
		
		if(flagRilevanteIva != null) {
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacTSubdocs sd");
			jpql.append("	WHERE sd.dataCancellazione IS NULL ");
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM sd.siacRSubdocAttrs sda ");
			jpql.append("	  WHERE sda.dataCancellazione IS NULL ");
			jpql.append("	  and sda.siacTAttr.attrCode = '" + SiacTAttrEnum.FlagRilevanteIVA.getCodice() +"' ");
			jpql.append("	  AND sda.boolean_ = :flagRilevanteIva ");
			jpql.append("   ) ");
			jpql.append(" ) ");
			
			param.put("flagRilevanteIva", Boolean.TRUE.equals(flagRilevanteIva) ? "S" : "N");
		}
		
		if(impegnoId != null && impegnoId!=0) {
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacTSubdocs sd ");
			jpql.append("	WHERE sd.dataCancellazione IS NULL ");
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM sd.siacRSubdocMovgestTs sdmp ");
			jpql.append("	  WHERE sdmp.dataCancellazione IS NULL ");
			jpql.append("	  AND sdmp.siacTMovgestT.siacTMovgest.movgestId = :movgestIdImpegno ");
			jpql.append("   ) ");
			jpql.append(" ) ");
			
			param.put("movgestIdImpegno", impegnoId);
		}
		
		if(accertamentoId != null && accertamentoId!=0) { 
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacTSubdocs sd ");
			jpql.append("	WHERE sd.dataCancellazione IS NULL ");
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM sd.siacRSubdocMovgestTs sdmp ");
			jpql.append("	  WHERE sdmp.dataCancellazione IS NULL ");
			jpql.append("	  AND sdmp.siacTMovgestT.siacTMovgest.movgestId = :movgestIdAccertamento ");
			jpql.append("   ) ");
			jpql.append(" ) ");
			
			param.put("movgestIdAccertamento", accertamentoId);
		}
		 
		if(attoAmministrativoId != null && attoAmministrativoId!=0) {
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacTSubdocs sd");
			jpql.append("	WHERE sd.dataCancellazione IS NULL ");
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM sd.siacRSubdocAttoAmms sdaa ");
			jpql.append("	  WHERE sdaa.dataCancellazione IS NULL ");
			jpql.append("	  AND sdaa.siacTAttoAmm.attoammId = :attoAmmId ");
			jpql.append("   ) ");
			jpql.append(" ) ");
			
			param.put("attoAmmId", attoAmministrativoId);
		}else{
			/*
			String methodName="a caso ";			
			log.info(methodName, StringUtils.isNotBlank(annoProv)); 
			log.info(methodName, numProv != null  );
			log.info(methodName, attoammTipoId != null); 
			log.info(methodName, sacId != null);

			log.info(methodName, annoProv !=null ? annoProv : "null"); 
			log.info(methodName, numProv != null ? numProv : "null" );
			log.info(methodName, attoammTipoId != null ? attoammTipoId : "null"); 
			log.info(methodName, sacId != null ? sacId : "null");
			 */
					
			if(StringUtils.isNotBlank(annoProv) || numProv != null || attoammTipoId != null || sacId != null){
				jpql.append(" AND EXISTS( ");
				jpql.append(" 	FROM SiacRSubdocAttoAmm sdaa ");
				jpql.append("	WHERE sdaa.dataCancellazione IS NULL ");
				jpql.append("	AND sdaa.siacTSubdoc.siacTDoc = d ");
				
				if(StringUtils.isNotBlank(annoProv)){
					jpql.append("   AND sdaa.siacTAttoAmm.attoammAnno = :annoProv ");
					param.put("annoProv", annoProv);
				}
				
				if(numProv != null){
					jpql.append("   AND sdaa.siacTAttoAmm.attoammNumero = :numProv ");
					param.put("numProv", numProv);
				}
				
				if(attoammTipoId != null){
					jpql.append("   AND sdaa.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId = :attoammTipoId ");
					param.put("attoammTipoId", attoammTipoId);
				}
				
				if(sacId != null){
					jpql.append(" AND EXISTS ( ");
					jpql.append("         FROM SiacRAttoAmmClass sdac ");//  sdaa.siacTAttoAmm.siacRAttoAmmClasses sdac ");
					jpql.append("         WHERE sdac.siacTClass.classifId = :sacId ");
					jpql.append("         AND sdac.siacTAttoAmm = sdaa.siacTAttoAmm ");
					jpql.append("         AND sdac.dataCancellazione IS NULL ");
					jpql.append("     ) ");
					param.put("sacId", sacId);
				}				
				
				jpql.append(" ) ");				
			}
		}
		
		if(soggettoId != null && soggettoId != 0) {
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacRDocSogs ds ");
			jpql.append("	WHERE ds.siacTSoggetto.soggettoId = :soggettoId ");
			jpql.append("	AND ds.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("soggettoId", soggettoId);
		}
		
		if(eldocAnno != null && eldocAnno != 0){
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacTSubdocs sd");
			jpql.append("	WHERE sd.dataCancellazione IS NULL ");
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM sd.siacRElencoDocSubdocs sde ");
			jpql.append("	  WHERE sde.dataCancellazione IS NULL ");
			jpql.append("	  AND sde.siacTElencoDoc.eldocAnno = :eldocAnno ");
			jpql.append("   ) ");
			jpql.append(" ) ");
			
			param.put("eldocAnno", eldocAnno);
		}
		
		if(eldocNumero != null && eldocNumero != 0){
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacTSubdocs sd");
			jpql.append("	WHERE sd.dataCancellazione IS NULL ");
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM sd.siacRElencoDocSubdocs sde ");
			jpql.append("	  WHERE sde.dataCancellazione IS NULL ");
			jpql.append("	  AND sde.siacTElencoDoc.eldocNumero = :eldocNumero ");
			jpql.append("   ) ");
			jpql.append(" ) ");
			
			param.put("eldocNumero", eldocNumero);
		}
		
		if(liqAnno != null && liqAnno != 0){
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacTSubdocs sd");
			jpql.append("	WHERE sd.dataCancellazione IS NULL ");
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM sd.siacRSubdocLiquidaziones sdl ");
			jpql.append("	  WHERE sdl.dataCancellazione IS NULL ");
			jpql.append("	  AND sdl.siacTLiquidazione.liqAnno = :liqAnno ");
			jpql.append("   ) ");
			jpql.append(" ) ");
			
			param.put("liqAnno", liqAnno);
		}
		
		if(liqNumero != null){
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacTSubdocs sd");
			jpql.append("	WHERE sd.dataCancellazione IS NULL ");
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM sd.siacRSubdocLiquidaziones sdl ");
			jpql.append("	  WHERE sdl.dataCancellazione IS NULL ");
			jpql.append("	  AND sdl.siacTLiquidazione.liqNumero = :liqNumero ");
			jpql.append("   ) ");
			jpql.append(" ) ");
			
			param.put("liqNumero", liqNumero);
		}
		
		if( bilId != null && bilId != 0){
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacTSubdocs sd");
			jpql.append("	WHERE sd.dataCancellazione IS NULL ");
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM sd.siacRSubdocLiquidaziones sdl ");
			jpql.append("	  WHERE sdl.dataCancellazione IS NULL ");
			jpql.append("	  AND sdl.siacTLiquidazione.siacTBil.bilId = :bilId ");
			jpql.append("   ) ");
			jpql.append(" ) ");
			
			param.put("bilId", bilId);
		}
		
		if(StringUtils.isNotBlank(anno)){
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacTSubdocs sd");
			jpql.append("	WHERE sd.dataCancellazione IS NULL ");
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM sd.siacRSubdocLiquidaziones sdl ");
			jpql.append("	  WHERE sdl.dataCancellazione IS NULL ");
			jpql.append("	  AND sdl.siacTLiquidazione.siacTBil.siacTPeriodo.anno = :anno ");
			jpql.append("   ) ");
			jpql.append(" ) ");
			
			param.put("anno", anno);
		}
		
		if(flagCollegatoCEC != null) {
			jpql.append(" AND d.docCollegatoCec = :docCollegatoCec");
			
			param.put("docCollegatoCec", flagCollegatoCEC);
		}
		
		if(annoRepertorio != null && annoRepertorio != 0){
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacRDocAttrs ra");
			jpql.append("	WHERE ra.dataCancellazione IS NULL ");
			jpql.append("	AND ra.siacTAttr.attrCode = '" +SiacTAttrEnum.AnnoRepertorio.getCodice()+ "' ");
			jpql.append("	AND ra.numerico = :annoRepertorio");
			jpql.append(" ) ");
			
			param.put("annoRepertorio", new BigDecimal(annoRepertorio));
		}
	
		if(dataRepertorio != null){
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
			
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacRDocAttrs ra");
			jpql.append("	WHERE ra.dataCancellazione IS NULL ");
			jpql.append("	AND ra.siacTAttr.attrCode = '" +SiacTAttrEnum.DataRepertorio.getCodice()+ "' ");
			jpql.append(" 	AND " + Utility.toJpqlSearchLike("ra.testo", "CONCAT('%', :dataRepertorio, '%')") + " ");
			jpql.append(" ) ");
			
			param.put("dataRepertorio", sdf.format(dataRepertorio));
		}
		
		if(StringUtils.isNotBlank(numeroRepertorio)){
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacRDocAttrs ra");
			jpql.append("	WHERE ra.dataCancellazione IS NULL ");
			jpql.append("	AND ra.siacTAttr.attrCode = '" +SiacTAttrEnum.NumeroRepertorio.getCodice()+ "' ");
			jpql.append("	AND ra.testo = :numeroRepertorio");
			jpql.append(" ) ");
			
			param.put("numeroRepertorio", numeroRepertorio);
			
		}
		
		if(StringUtils.isNotBlank(registroRepertorio)){
			jpql.append(" AND EXISTS( ");
			jpql.append(" 	FROM d.siacRDocAttrs ra");
			jpql.append("	WHERE ra.dataCancellazione IS NULL ");
			jpql.append("	AND ra.siacTAttr.attrCode = '" +SiacTAttrEnum.RegistroRepertorio.getCodice()+ "' ");
			jpql.append("	AND ra.testo = :registroRepertorio");
			jpql.append(" ) ");
			
			param.put("registroRepertorio", registroRepertorio);
		}
		
		if(contabilizzaGenPcc != null){
			jpql.append(" AND (");
			jpql.append("     d.docContabilizzaGenpcc = :docContabilizzaGenpcc");
			jpql.append("     OR NOT EXISTS( ");
			jpql.append("         FROM SiacRDocTipoAttr ra");
			jpql.append("         WHERE d.siacDDocTipo = ra.siacDDocTipo ");
			jpql.append("         AND ra.dataCancellazione IS NULL ");
			jpql.append("         AND ra.siacTAttr.attrCode IN ('").append(SiacTAttrEnum.FlagAttivaGEN.getCodice()).append("', '").append(SiacTAttrEnum.FlagComunicaPCC.getCodice()).append("') ");
			jpql.append("         AND ra.boolean_ = 'S' ");
			jpql.append("     ) ");
			jpql.append(" ) ");			
			param.put("docContabilizzaGenpcc", contabilizzaGenPcc);
		}
		
		//SIAC-6780
		if(StringUtils.isNotBlank(numeroPreDoc)) {
			jpql.append(" AND EXISTS(");
			jpql.append("         FROM SiacRPredocSubdoc rp ");
			jpql.append("         WHERE rp.siacTPredoc.predocNumero = :predocNumero ");
			jpql.append("         AND rp.siacTSubdoc.siacTDoc = d ");
			jpql.append("         AND rp.dataCancellazione IS NULL ");
			jpql.append("         AND rp.siacTSubdoc.dataCancellazione IS NULL ");
			jpql.append(" ) ");			
			param.put("predocNumero", numeroPreDoc);
		}
	}

}
