/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import it.csi.siac.siacbilser.integration.entity.SiacRDocIva;
import it.csi.siac.siacbilser.integration.entity.SiacRIvamov;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocIva;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocIvaAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocIvaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocSubdocIva;
import it.csi.siac.siacbilser.integration.entity.SiacTIvamov;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSubdocIvaStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class SubdocumentoIvaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SubdocumentoIvaDaoImpl extends JpaDao<SiacTSubdocIva, Integer> implements SubdocumentoIvaDao {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.SubdocumentoIvaDao#create(it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva)
	 */
	public SiacTSubdocIva create(SiacTSubdocIva d){
		
		Date now = new Date();
		d.setDataModificaInserimento(now);
		
		if(d.getSiacRDocIva()!=null){
			SiacRDocIva r = d.getSiacRDocIva();
			r.setDataModificaInserimento(now);
			
		}
		
		if(d.getSiacRIvamovs()!=null){
			for(SiacRIvamov r : d.getSiacRIvamovs()){
				r.setDataModificaInserimento(now);
				r.setUid(null);
				SiacTIvamov siacTIvamov = r.getSiacTIvamov();
				if(siacTIvamov!=null)	{
					siacTIvamov.setDataModificaInserimento(now);
					siacTIvamov.setUid(null);
				}
			}
		}
		
		if(d.getSiacRSubdocIvaAttrs()!=null){
			for(SiacRSubdocIvaAttr r : d.getSiacRSubdocIvaAttrs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRSubdocIvasFiglio()!=null) {
			for(SiacRSubdocIva r : d.getSiacRSubdocIvasFiglio()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRSubdocIvasPadre()!=null) {
			for(SiacRSubdocIva r : d.getSiacRSubdocIvasPadre()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRSubdocIvaStatos()!=null) {
			for(SiacRSubdocIvaStato r : d.getSiacRSubdocIvaStatos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRSubdocSubdocIvas()!=null) {
			for(SiacRSubdocSubdocIva r : d.getSiacRSubdocSubdocIvas()){
				r.setDataModificaInserimento(now);
			}
		}
		
		
				
		
		d.setUid(null);
		super.save(d);
		return d;
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	public SiacTSubdocIva update(SiacTSubdocIva d){
		SiacTSubdocIva dAttuale = this.findById(d.getUid());
		
		Date now = new Date();
		d.setDataModificaAggiornamento(now);
		
		//cancellazione elementi collegati
		if(dAttuale.getSiacRDocIva()!=null){
			SiacRDocIva r = dAttuale.getSiacRDocIva();
			r.setDataCancellazioneIfNotSet(now);
			
		}
		
		if(dAttuale.getSiacRIvamovs()!=null){
			for(SiacRIvamov r : dAttuale.getSiacRIvamovs()){
				r.setDataCancellazioneIfNotSet(now);
				SiacTIvamov siacTIvamov = r.getSiacTIvamov();
				if(siacTIvamov!=null)	{
					siacTIvamov.setDataCancellazioneIfNotSet(now);
				}
			}
		}
		
		
		if(dAttuale.getSiacRSubdocIvaAttrs()!=null){
			for(SiacRSubdocIvaAttr r : dAttuale.getSiacRSubdocIvaAttrs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(dAttuale.getSiacRSubdocIvasFiglio()!=null) {
			for(SiacRSubdocIva r : dAttuale.getSiacRSubdocIvasFiglio()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(dAttuale.getSiacRSubdocIvasPadre()!=null) {
			for(SiacRSubdocIva r : dAttuale.getSiacRSubdocIvasPadre()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(dAttuale.getSiacRSubdocIvaStatos()!=null) {
			for(SiacRSubdocIvaStato r : dAttuale.getSiacRSubdocIvaStatos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(dAttuale.getSiacRSubdocSubdocIvas()!=null) {
			for(SiacRSubdocSubdocIva r : dAttuale.getSiacRSubdocSubdocIvas()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		//SIAC-8173
		//invalido il vecchio dato settando le date cancellazione
		//in modo da mantenere uno storico delle modifiche
		super.update(dAttuale);
		
		//inserimento elementi nuovi		
		if(d.getSiacRDocIva()!=null){
			SiacRDocIva r = d.getSiacRDocIva();
			r.setDataModificaInserimento(now);
			
		}
		
		if(d.getSiacRIvamovs()!=null){
			for(SiacRIvamov r : d.getSiacRIvamovs()){
				r.setDataModificaInserimento(now);
				r.setUid(null);
				SiacTIvamov siacTIvamov = r.getSiacTIvamov();
				if(siacTIvamov!=null)	{
					siacTIvamov.setDataModificaInserimento(now);
					siacTIvamov.setUid(null);
				}
			}
		}
		
		
		if(d.getSiacRSubdocIvaAttrs()!=null){
			for(SiacRSubdocIvaAttr r : d.getSiacRSubdocIvaAttrs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRSubdocIvasFiglio()!=null) {
			for(SiacRSubdocIva r : d.getSiacRSubdocIvasFiglio()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRSubdocIvasPadre()!=null) {
			for(SiacRSubdocIva r : d.getSiacRSubdocIvasPadre()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRSubdocIvaStatos()!=null) {
			for(SiacRSubdocIvaStato r : d.getSiacRSubdocIvaStatos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRSubdocSubdocIvas()!=null) {
			for(SiacRSubdocSubdocIva r : d.getSiacRSubdocSubdocIvas()){
				r.setDataModificaInserimento(now);
			}
		}
		
		//SIAC-8173
		//inserisco il nuovo record
		d.setUid(null);
		return super.create(d);
	}

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#delete(java.lang.Object)
	 */
	public void delete(SiacTSubdocIva e){
		SiacTSubdocIva subdoc = this.findById(e.getUid());
		
		Date now = new Date();
		subdoc.setDataCancellazioneIfNotSet(now);
		
		//cancellazione elementi collegati		
		if(subdoc.getSiacRDocIva()!=null){
			SiacRDocIva r = subdoc.getSiacRDocIva();
			r.setDataCancellazioneIfNotSet(now);
			
		}
		
		if(subdoc.getSiacRIvamovs()!=null){
			for(SiacRIvamov r : subdoc.getSiacRIvamovs()){
				r.setDataCancellazioneIfNotSet(now);
				SiacTIvamov siacTIvamov = r.getSiacTIvamov();
				if(siacTIvamov!=null)	{
					siacTIvamov.setDataCancellazioneIfNotSet(now);
				}
			}
		}
		
		
		if(subdoc.getSiacRSubdocIvaAttrs()!=null){
			for(SiacRSubdocIvaAttr r : subdoc.getSiacRSubdocIvaAttrs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(subdoc.getSiacRSubdocIvasFiglio()!=null) {
			for(SiacRSubdocIva r : subdoc.getSiacRSubdocIvasFiglio()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(subdoc.getSiacRSubdocIvasPadre()!=null) {
			for(SiacRSubdocIva r : subdoc.getSiacRSubdocIvasPadre()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(subdoc.getSiacRSubdocIvaStatos()!=null) {
			for(SiacRSubdocIvaStato r : subdoc.getSiacRSubdocIvaStatos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(subdoc.getSiacRSubdocSubdocIvas()!=null) {
			for(SiacRSubdocSubdocIva r : subdoc.getSiacRSubdocSubdocIvas()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.SubdocumentoIvaDao#ricercaSinteticaSubdocumentoIva(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.util.Date, java.util.Date, java.lang.Integer, java.lang.Integer, java.util.Date, java.util.Date, java.lang.Boolean, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Boolean, java.lang.Integer, java.lang.Integer, java.lang.String, java.util.Date, java.lang.Integer, java.lang.Integer, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<SiacTSubdocIva> ricercaSinteticaSubdocumentoIva(Integer enteProprietarioId, SiacDDocFamTipoEnum tipoFam, String subdocivaAnno, Integer subdocivaNumero,
			Integer subdocivaProtProvDa, Integer subdocivaProtProvA, 
			Date subdocivaDataProtProvDa, Date subdocivaDataProtProvA,
			Integer subdocivaProtDefDa, Integer subdocivaProtDefA, 
			Date subdocivaDataProtDefDa, Date subdocivaDataProtDefA,
			Integer subdocivaNumeroDa, Integer subdocivaNumeroA,
			
			Boolean flagIntracomunitario, Integer regTipoId, Integer ivaregTipoId, Integer ivaattId, Boolean flagRilevanteIrap,
			Integer ivaregId, 
			Integer docAnno, String docNumero, Date docDataEmissione, Integer docTipoId, Integer soggettoId,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaSubdocumentoIva";
		
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("FROM SiacTSubdocIva d ");
		jpql.append(" WHERE ");
		jpql.append(" d.dataCancellazione IS NULL ");
		jpql.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		
		jpql.append(SiacTSubdocIvaRepository.SIACTSUBDOCIVA_DOC_FAM_TIPO_FILTER);
		param.put("docFamTipoCode", tipoFam.getCodice());
		param.put("docFamTipoCodeIva", tipoFam.getEquivalenteIva().getCodice());
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(StringUtils.isNotBlank(subdocivaAnno)) {
			jpql.append(" AND d.subdocivaAnno = :subdocivaAnno");
			param.put("subdocivaAnno", subdocivaAnno);
		}
		
		if(subdocivaNumero != null) {
			jpql.append(" AND d.subdocivaNumero = :subdocivaNumero");
			param.put("subdocivaNumero", subdocivaNumero);
		}
		
		if(subdocivaProtProvDa!=null){
			jpql.append(" AND d.subdocivaProtProv <> ''");
			jpql.append(" AND ");
			jpql.append(Utility.castToType(Utility.castToType("d.subdocivaProtProv", "string"), "int"));
			jpql.append(" >= :subdocivaProtProvDa ");
			param.put("subdocivaProtProvDa", subdocivaProtProvDa);
		}
		
		if(subdocivaProtProvA!=null){
			jpql.append(" AND d.subdocivaProtProv <> ''");
			jpql.append(" AND ");
			jpql.append(Utility.castToType(Utility.castToType("d.subdocivaProtProv", "string"), "int"));
			jpql.append(" <= :subdocivaProtProvA ");
			param.put("subdocivaProtProvA", subdocivaProtProvA);
		}
		
		if(subdocivaDataProtProvDa!=null){
			jpql.append(" AND d.subdocivaDataProtProv >= :subdocivaDataProtProvDa");
			param.put("subdocivaDataProtProvDa", subdocivaDataProtProvDa);
		}
		
		if(subdocivaDataProtProvA!=null){
			jpql.append(" AND d.subdocivaDataProtProv <= :subdocivaDataProtProvA");
			param.put("subdocivaDataProtProvA", subdocivaDataProtProvA);
		}
		
		
		
		if(subdocivaProtDefDa!=null){
			jpql.append(" AND d.subdocivaProtDef <> ''");
			jpql.append(" AND ");
			jpql.append(Utility.castToType(Utility.castToType("d.subdocivaProtDef", "string"), "int"));
			jpql.append(" >= :subdocivaProtDefDa ");
			param.put("subdocivaProtDefDa", subdocivaProtDefDa);
		}
		
		if(subdocivaProtDefA!=null){
			jpql.append(" AND d.subdocivaProtDef <> ''");
			jpql.append(" AND ");
			jpql.append(Utility.castToType(Utility.castToType("d.subdocivaProtDef", "string"), "int"));
			jpql.append(" <= :subdocivaProtDefA ");
			param.put("subdocivaProtDefA", subdocivaProtDefA);
		}
		
		if(subdocivaDataProtDefDa!=null){
			jpql.append(" AND d.subdocivaDataProtDef >= :subdocivaDataProtDefDa");
			param.put("subdocivaDataProtDefDa", subdocivaDataProtDefDa);
		}
		
		if(subdocivaDataProtDefA!=null){
			jpql.append(" AND d.subdocivaDataProtDef <= :subdocivaDataProtDefA");
			param.put("subdocivaDataProtDefA", subdocivaDataProtDefA);
		}
		
		if(subdocivaNumeroDa!=null){
			jpql.append(" AND d.subdocivaNumero >= :subdocivaNumeroDa");
			param.put("subdocivaNumeroDa", subdocivaNumeroDa);
		}
		
		if(subdocivaNumeroA!=null){
			jpql.append(" AND d.subdocivaNumero <= :subdocivaNumeroA");
			param.put("subdocivaNumeroA", subdocivaNumeroA);
		}
		
		if(flagIntracomunitario!=null) {
			
			jpql.append(" AND EXISTS( ");
			jpql.append("     FROM d.siacRSubdocIvaAttrs a ");
			jpql.append("     WHERE a.siacTAttr.attrCode = '" + SiacTAttrEnum.FlagIntracomunitario.getCodice() +"' ");
			jpql.append("     AND a.dataCancellazione IS NULL ");
			jpql.append("     AND a.boolean_ = :flagIntracomunitario ");
			jpql.append(" )");
			
			param.put("flagIntracomunitario", Boolean.TRUE.equals(flagIntracomunitario) ? "S" : "N");
			
		}
		
		//tipoRegistrazioneId
		if(regTipoId != null){
			jpql.append(" AND d.siacDIvaRegistrazioneTipo.regTipoId = :regTipoId");
			param.put("regTipoId", regTipoId);
		}
		
		//tipoRegistroIvaId
		if(ivaregTipoId != null){
			jpql.append(" AND d.siacTIvaRegistro.siacDIvaRegistroTipo.ivaregTipoId = :ivaregTipoId");
			param.put("ivaregTipoId", ivaregTipoId);
		}
		
		//attivitaId
		if(ivaattId != null){
			jpql.append(" AND d.siacTIvaAttivita.ivaattId = :ivaattId");
			param.put("ivaattId", ivaattId);
		}
		
		//flagRilevanteIrap
		if(flagRilevanteIrap!=null) {
			
			jpql.append(" AND EXISTS( ");
			jpql.append("     FROM d.siacRSubdocIvaAttrs a ");
			jpql.append("     WHERE a.siacTAttr.attrCode = '" + SiacTAttrEnum.FlagRilevanteIRAP.getCodice() +"' ");
			jpql.append("     AND a.dataCancellazione IS NULL ");
			jpql.append("     AND a.boolean_ = :flagRilevanteIrap ");
			jpql.append(" )");
			
			param.put("flagRilevanteIrap", Boolean.TRUE.equals(flagRilevanteIrap) ? "S" : "N");
			
		}
		

		//registroId,
		if(ivaregId != null){
			jpql.append(" AND d.siacTIvaRegistro.ivaregId = :ivaregId");
			param.put("ivaregId", ivaregId);
		}
		
		
		//annoDocumento,
		if(docAnno!=null) {
			
			jpql.append(" AND ( ");
			jpql.append("     EXISTS ( ");
			jpql.append("         FROM d.siacRSubdocSubdocIvas r ");
			jpql.append("         WHERE r.siacTSubdoc.siacTDoc.docAnno = :docAnno ");
			jpql.append("     ) ");
			jpql.append("     OR EXISTS ( ");
			jpql.append("         FROM d.siacRDocIva r ");
			jpql.append("         WHERE r.siacTDoc.docAnno = :docAnno ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			param.put("docAnno", docAnno);
		}
		
		//numeroDocumento
		if(StringUtils.isNotBlank(docNumero)) {
			
			jpql.append(" AND ( ");
			jpql.append("     EXISTS ( ");
			jpql.append("         FROM d.siacRSubdocSubdocIvas r ");
//			jpql.append("         WHERE r.siacTSubdoc.siacTDoc.docNumero = :docNumero");
			jpql.append("		    WHERE	 " + Utility.toJpqlSearchLike("r.siacTSubdoc.siacTDoc.docNumero", "CONCAT('%', :docNumero, '%')") + " ");
			jpql.append("     ) ");
			jpql.append("     OR EXISTS ( ");
			jpql.append("         FROM d.siacRDocIva r ");
//			jpql.append("         WHERE r.siacTDoc.docNumero = :docNumero");
			jpql.append("		    WHERE	 " + Utility.toJpqlSearchLike("r.siacTDoc.docNumero", "CONCAT('%', :docNumero, '%')") + " ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			param.put("docNumero", docNumero);
		}
		
		//dataEmissione, 
		if(docDataEmissione!=null) {
			
			jpql.append(" AND ( ");
			jpql.append("     EXISTS ( ");
			jpql.append("         FROM d.siacRSubdocSubdocIvas r ");
			jpql.append("         WHERE r.siacTSubdoc.siacTDoc.docDataEmissione = :docDataEmissione ");
			jpql.append("     ) ");
			jpql.append("     OR EXISTS ( ");
			jpql.append("         FROM d.siacRDocIva r ");
			jpql.append("         WHERE r.siacTDoc.docDataEmissione = :docDataEmissione ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			param.put("docDataEmissione", docDataEmissione);
		}
		
		//docTipoId
		//siacDDocTipo.docTipoId
		if(docTipoId!=null) {
			jpql.append(" AND ( ");
			jpql.append("     EXISTS ( ");
			jpql.append("         FROM d.siacRSubdocSubdocIvas r ");
			jpql.append("         WHERE r.siacTSubdoc.siacTDoc.siacDDocTipo.docTipoId = :docTipoId ");
			jpql.append("     ) ");
			jpql.append("     OR EXISTS ( ");
			jpql.append("         FROM d.siacRDocIva r ");
			jpql.append("         WHERE r.siacTDoc.siacDDocTipo.docTipoId = :docTipoId ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			param.put("docTipoId", docTipoId);
		}
		
		
		//soggettoId		
		if(soggettoId!=null) {
			
			jpql.append(" AND ( ");
			jpql.append("     EXISTS ( ");
			jpql.append("         FROM d.siacRSubdocSubdocIvas r ");
			jpql.append("         WHERE EXISTS ( ");
			jpql.append("             FROM r.siacTSubdoc.siacTDoc.siacRDocSogs ds ");
			jpql.append("             WHERE ds.siacTSoggetto.soggettoId = :soggettoId ");
			jpql.append("             AND ds.dataCancellazione IS NULL ");
			jpql.append("         ) ");
			jpql.append("     ) ");
			jpql.append("     OR EXISTS ( ");
			jpql.append("         FROM d.siacRDocIva r ");
			jpql.append("         WHERE EXISTS ( ");
			jpql.append("             FROM r.siacTDoc.siacRDocSogs ds ");
			jpql.append("             WHERE ds.siacTSoggetto.soggettoId = :soggettoId ");
			jpql.append("             AND ds.dataCancellazione IS NULL ");
			jpql.append("         ) ");
			jpql.append("     ) ");
			jpql.append(" ) ");
					
			param.put("soggettoId", soggettoId);
		}
		
		

		jpql.append(" ORDER BY d.subdocivaAnno, d.subdocivaNumero ");
		
		log.debug(methodName, jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
		
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<SiacTSubdocIva> ricercaSubdocumentoIva(Integer enteProprietarioId,
			Collection<SiacDDocFamTipoEnum> siacDDocFamTipoEnums,
			String subdocivaAnno, Integer subdocivaNumero,
			SiacDSubdocIvaStatoEnum siacDSubdocIvaStatoEnum,
			Date subdocivaDataProtProvDa, Date subdocivaDataProtProvA,
			Date subdocivaDataProtDefDa, Date subdocivaDataProtDefA,
			//SIAC-7516
			Date docDataOperazioneDa, Date docDataOperazioneA,
			Integer registroId) {
		final String methodName = "ricercaSinteticaSubdocumentoIva";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("FROM SiacTSubdocIva d ");
		jpql.append(" WHERE ");
		jpql.append(" d.dataCancellazione IS NULL ");
		jpql.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		//escludo le quote iva differita per sicurezza
		jpql.append(" AND NOT EXISTS ( ");
		jpql.append("     FROM d.siacRSubdocIvasFiglio f ");
		jpql.append("     WHERE f.dataCancellazione IS NULL ");
		jpql.append("     AND  f.siacDRelazTipo.relazTipoCode = 'QPID' ");
		jpql.append(" ) ");
		
		appendFilterOnSiacTDocCollegato(jpql, param, siacDDocFamTipoEnums, docDataOperazioneDa, docDataOperazioneA);
		
		if(StringUtils.isNotBlank(subdocivaAnno)) {
			jpql.append(" AND d.subdocivaAnno = :subdocivaAnno ");
			param.put("subdocivaAnno", subdocivaAnno);
		}
		
		if(subdocivaNumero != null) {
			jpql.append(" AND d.subdocivaNumero = :subdocivaNumero ");
			param.put("subdocivaNumero", subdocivaNumero);
		}
		
		if(siacDSubdocIvaStatoEnum != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRSubdocIvaStatos r ");
			jpql.append("     WHERE r.siacDSubdocIvaStato.subdocivaStatoCode = :subdocivaStatoCode ");
			jpql.append("     AND r.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("subdocivaStatoCode", siacDSubdocIvaStatoEnum.getCodice());
		}
		
		if(subdocivaDataProtProvDa!=null){
			jpql.append(" AND d.subdocivaDataProtProv >= :subdocivaDataProtProvDa ");
			param.put("subdocivaDataProtProvDa", subdocivaDataProtProvDa);
		}
		
		if(subdocivaDataProtProvA!=null){
			jpql.append(" AND d.subdocivaDataProtProv <= :subdocivaDataProtProvA ");
			param.put("subdocivaDataProtProvA", subdocivaDataProtProvA);
		}
		
		if(subdocivaDataProtDefDa!=null){
			jpql.append(" AND d.subdocivaDataProtDef >= :subdocivaDataProtDefDa ");
			param.put("subdocivaDataProtDefDa", subdocivaDataProtDefDa);
		}
		
		if(subdocivaDataProtDefA!=null){
			jpql.append(" AND d.subdocivaDataProtDef <= :subdocivaDataProtDefA ");
			param.put("subdocivaDataProtDefA", subdocivaDataProtDefA);
		}
		
		//registroId,		
		if(registroId != null){
			jpql.append(" AND d.siacTIvaRegistro.ivaregId = :ivaregId ");
			param.put("ivaregId", registroId);
		}
		
		jpql.append(" ORDER BY d.subdocivaAnno, d.subdocivaNumero ");
		
		log.debug(methodName, jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		
		return query.getResultList();
	}

	
	private String getAndConditionDocFamTipoCodes(boolean applicaFiltro, String aliasSiacTDoc) {
		if(!applicaFiltro) {
			return "";
		}
		return  new StringBuilder().append(" AND ").append(aliasSiacTDoc).append(".siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) ").toString();
	}
	
	private String getAndConditionDataOperazione(boolean applicaFiltro, String aliasSiacTDoc, String comparison, String aliasData) {
		if(!applicaFiltro) {
			return "";
		}
		return  new StringBuilder().append(" AND ").append(aliasSiacTDoc)
				.append(".docDataOperazione ").append(comparison).append(" :").append(aliasData).toString();
	}
	
	/**
	 * @param jpql
	 * @param jpql
	 * @param siacDDocFamTipoEnums
	 * @param docDataOperazioneA 
	 * @param docDataOperazioneDa 
	 */
	private void appendFilterOnSiacTDocCollegato(StringBuilder jpql, Map<String, Object> param, Collection<SiacDDocFamTipoEnum> siacDDocFamTipoEnums, Date docDataOperazioneDa, Date docDataOperazioneA) {
		boolean filtroPerDocTipo = siacDDocFamTipoEnums != null && !siacDDocFamTipoEnums.isEmpty();
		boolean filtroPerDocDataOperazioneDa = docDataOperazioneDa != null;
		boolean filtroPerDocDataOperazioneA = docDataOperazioneA != null;
		if(jpql == null || (!filtroPerDocTipo && !filtroPerDocDataOperazioneDa && !filtroPerDocDataOperazioneA)) {
			return;
		}
		
		
		jpql.append(" AND ( ");
		jpql.append("     EXISTS( ");
		jpql.append("         FROM d.siacRSubdocSubdocIvas r ");
		jpql.append("         WHERE r.dataCancellazione IS NULL " );
		jpql.append(getAndConditionDocFamTipoCodes(filtroPerDocTipo, "r.siacTSubdoc.siacTDoc"));
		jpql.append(getAndConditionDataOperazione(filtroPerDocDataOperazioneDa, "r.siacTSubdoc.siacTDoc", ">=", "docDataOperazioneDa"));
		jpql.append(getAndConditionDataOperazione(filtroPerDocDataOperazioneA, "r.siacTSubdoc.siacTDoc", "<=", "docDataOperazioneA"));
//		jpql.append("         AND r.siacTSubdoc.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) ");
		jpql.append("     ) " );
		jpql.append("     OR EXISTS( " );
		jpql.append("         FROM d.siacRDocIva r " );
		jpql.append("         WHERE r.dataCancellazione IS NULL ");
		jpql.append(getAndConditionDocFamTipoCodes(filtroPerDocTipo, "r.siacTDoc"));

		jpql.append(getAndConditionDataOperazione(filtroPerDocDataOperazioneDa, "r.siacTDoc", ">=", "docDataOperazioneDa"));
		jpql.append(getAndConditionDataOperazione(filtroPerDocDataOperazioneA, "r.siacTDoc", "<=", "docDataOperazioneA"));
//		jpql.append("         AND r.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN(:docFamTipoCodes) ");
		jpql.append("     ) ");
		jpql.append("     OR EXISTS( " );
		jpql.append("         FROM d.siacRSubdocIvasFiglio si " );
		jpql.append("         WHERE EXISTS( " );
		jpql.append("             FROM si.siacTSubdocIvaPadre.siacRSubdocSubdocIvas r " );
		jpql.append("             WHERE r.dataCancellazione IS NULL " );
		jpql.append(getAndConditionDocFamTipoCodes(filtroPerDocTipo, "r.siacTSubdoc.siacTDoc"));
		jpql.append(getAndConditionDataOperazione(filtroPerDocDataOperazioneDa, "r.siacTSubdoc.siacTDoc", ">=", "docDataOperazioneDa"));
		jpql.append(getAndConditionDataOperazione(filtroPerDocDataOperazioneA, "r.siacTSubdoc.siacTDoc", "<=", "docDataOperazioneA"));
//		jpql.append("             AND r.siacTSubdoc.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN(:docFamTipoCodes) " );
		jpql.append("         ) " );
		jpql.append("         OR EXISTS( ");
		jpql.append("             FROM si.siacTSubdocIvaPadre.siacRDocIva r ");
		jpql.append("             WHERE r.dataCancellazione IS NULL " );
		jpql.append(getAndConditionDocFamTipoCodes(filtroPerDocTipo, "r.siacTDoc"));
		jpql.append(getAndConditionDataOperazione(filtroPerDocDataOperazioneDa, "r.siacTDoc", ">=", "docDataOperazioneDa"));
		jpql.append(getAndConditionDataOperazione(filtroPerDocDataOperazioneA, "r.siacTDoc", "<=", "docDataOperazioneA"));
//		jpql.append("             AND r.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN(:docFamTipoCodes) " );
		jpql.append("         ) ");
		jpql.append("     ) ");
		jpql.append(" ) ");
		
		if(filtroPerDocTipo) {
			Collection<String> docFamTipoCodes = new HashSet<String>();
			for(SiacDDocFamTipoEnum siacDDocFamTipoEnum : siacDDocFamTipoEnums) {
				docFamTipoCodes.add(siacDDocFamTipoEnum.getCodice());
			}
			
			param.put("docFamTipoCodes", docFamTipoCodes);
		}
		if(filtroPerDocDataOperazioneDa) {
			param.put("docDataOperazioneDa", docDataOperazioneDa);
		}
		
		if(filtroPerDocDataOperazioneA) {
			param.put("docDataOperazioneA", docDataOperazioneA);
		}
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.SubdocumentoIvaDao#ricercaPuntualeSubdocumentoIva(java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	@Override
	public SiacTSubdocIva ricercaPuntualeSubdocumentoIva(Integer enteProprietarioId, String subdocivaAnno, Integer subdocivaNumero) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("FROM SiacTSubdocIva d ");
		jpql.append(" WHERE ");
		jpql.append(" d.dataCancellazione IS NULL ");
		jpql.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(subdocivaAnno != null) {
			jpql.append(" AND d.subdocivaAnno = :subdocivaAnno");
			param.put("subdocivaAnno", subdocivaAnno);
		}
		
		if(subdocivaNumero != null) {
			jpql.append(" AND d.subdocivaNumero = :subdocivaNumero");
			param.put("subdocivaNumero", subdocivaNumero);
		}
		
		
		Query query = createQuery(jpql.toString(), param);
		return (SiacTSubdocIva) query.getSingleResult();
		
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<SiacTSubdocIva> ricercaSubdocumentoIvaByOrdinativo(Integer uidOrdinativo, String subdocivaStatoCode){
		final String methodName ="ricercaSubdocumentoIvaByOrdinativo";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT rss.siacTSubdocIva");
		jpql.append(" FROM SiacRSubdocOrdinativoT rts, SiacRSubdocSubdocIva rss, SiacRSubdocIvaStato rst ");
		jpql.append(" WHERE rts.dataCancellazione IS NULL ");
		jpql.append(" AND rss.dataCancellazione IS NULL ");
		jpql.append(" AND rst.dataCancellazione IS NULL ");
		jpql.append(" AND rts.siacTOrdinativoT.siacTOrdinativo.ordId = :ordId ");
		jpql.append(" AND rss.siacTSubdoc = rts.siacTSubdoc ");
		jpql.append(" AND rst.siacTSubdocIva = rss.siacTSubdocIva ");
		jpql.append(" AND rst.siacDSubdocIvaStato.subdocivaStatoCode = :subdocivaStatoCode  ");
		param.put("ordId", uidOrdinativo);
		param.put("subdocivaStatoCode", subdocivaStatoCode);
		
		log.debug(methodName, "JPQL TO EXECUTE: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		return query.getResultList();
	}

}
