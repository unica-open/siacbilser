/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.entity.SiacRLiquidazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoVoceSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocClass;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocModpag;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocProvCassa;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocSog;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocSplitreverseIvaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocSospensione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAmmStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDLiquidazioneStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOrdinativoStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProvCassaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;

/**
 * The Class SubdocumentoDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SubdocumentoDaoImpl extends ExtendedJpaDao<SiacTSubdoc, Integer> implements SubdocumentoDao {
	
	@Override
	public SiacTSubdoc findById(Integer id) {
		SiacTSubdoc siacTSubdoc = super.findById(id);
		return siacTSubdoc;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.SubdocumentoDao#create(it.csi.siac.siacbilser.integration.entity.SiacTSubdoc)
	 */
	public SiacTSubdoc create(SiacTSubdoc d){
		
		Date now = new Date();
		d.setDataModificaInserimento(now);		
		
		if(d.getSiacRSubdocAttoAmms()!=null){
			for(SiacRSubdocAttoAmm att : d.getSiacRSubdocAttoAmms()){
				att.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRSubdocAttrs()!=null){
			for(SiacRSubdocAttr r : d.getSiacRSubdocAttrs()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocClasses()!=null){
			for(SiacRSubdocClass r : d.getSiacRSubdocClasses()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocLiquidaziones()!=null) {
			for(SiacRSubdocLiquidazione r : d.getSiacRSubdocLiquidaziones()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocModpags()!=null) {
			for(SiacRSubdocModpag r : d.getSiacRSubdocModpags()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocMovgestTs()!=null) {
			for(SiacRSubdocMovgestT r : d.getSiacRSubdocMovgestTs()){
				r.setDataModificaInserimento(now);
			}
		}
		
//		if(d.getSiacRSubdocOrdinativoTs()!=null) {
//			for(SiacRSubdocOrdinativoT r : d.getSiacRSubdocOrdinativoTs()){
//				r.setDataModificaInserimento(now);
//			}			
//		}
		
		if(d.getSiacRSubdocsA()!=null) {
			for(SiacRSubdoc r : d.getSiacRSubdocsA()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocsB()!=null) {
			for(SiacRSubdoc r : d.getSiacRSubdocsB()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocSogs()!=null) {
			for(SiacRSubdocSog r : d.getSiacRSubdocSogs()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocProvCassas()!=null) {
			for(SiacRSubdocProvCassa r : d.getSiacRSubdocProvCassas()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRMutuoVoceSubdocs()!=null) {
			for(SiacRMutuoVoceSubdoc r : d.getSiacRMutuoVoceSubdocs()){
				r.setDataModificaInserimento(now);
			}			
		}
		if(d.getSiacRSubdocSplitreverseIvaTipos() != null) {
			for(SiacRSubdocSplitreverseIvaTipo r : d.getSiacRSubdocSplitreverseIvaTipos()){
				r.setDataModificaInserimento(now);
			}
		}
		if(d.getSiacTSubdocSospensiones() != null) {
			for(SiacTSubdocSospensione r : d.getSiacTSubdocSospensiones()){
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
	public SiacTSubdoc update(SiacTSubdoc d){
		SiacTSubdoc dAttuale = this.findById(d.getUid());
		
		Date now = new Date();
		d.setDataModificaAggiornamento(now);		
		
		//cancellazione elementi collegati		
		if(dAttuale.getSiacRSubdocAttoAmms()!=null){
			for(SiacRSubdocAttoAmm att : dAttuale.getSiacRSubdocAttoAmms()){
				att.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(dAttuale.getSiacRSubdocAttrs()!=null){
			for(SiacRSubdocAttr r : dAttuale.getSiacRSubdocAttrs()){
				r.setDataCancellazioneIfNotSet(now);
			}			
		}
		
		if(dAttuale.getSiacRSubdocClasses()!=null){
			for(SiacRSubdocClass r : dAttuale.getSiacRSubdocClasses()){
				r.setDataCancellazioneIfNotSet(now);
			}			
		}
		
		if(dAttuale.getSiacRSubdocLiquidaziones()!=null) {
			for(SiacRSubdocLiquidazione r : dAttuale.getSiacRSubdocLiquidaziones()){
				SiacTLiquidazione siacTLiquidazione = r.getSiacTLiquidazione();
				//cancello le precedenti relazioni, tranne quelle con le liquidazioni annullate
				for(SiacRLiquidazioneStato siacRLiquidazioneStato : siacTLiquidazione.getSiacRLiquidazioneStatos()){
					if(siacRLiquidazioneStato.getDataCancellazione() == null && siacRLiquidazioneStato.getDataFineValidita() == null &&
								!siacRLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode().equals(SiacDLiquidazioneStatoEnum.Annullato.getCodice())){
						r.setDataCancellazioneIfNotSet(now);
						break;
					}
				}
//				r.setDataCancellazioneIfNotSet(now);
			}			
		}
		
		if(dAttuale.getSiacRSubdocModpags()!=null) {
			for(SiacRSubdocModpag r : dAttuale.getSiacRSubdocModpags()){
				r.setDataCancellazioneIfNotSet(now);
			}			
		}
		
		if(dAttuale.getSiacRSubdocMovgestTs()!=null) {
			for(SiacRSubdocMovgestT r : dAttuale.getSiacRSubdocMovgestTs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
//		if(dAttuale.getSiacRSubdocOrdinativoTs()!=null) {
//			for(SiacRSubdocOrdinativoT r : dAttuale.getSiacRSubdocOrdinativoTs()){
//				r.setDataCancellazioneIfNotSet(now);
//			}			
//		}
		
		if(dAttuale.getSiacRSubdocsA()!=null) {
			for(SiacRSubdoc r : dAttuale.getSiacRSubdocsA()){
				r.setDataCancellazioneIfNotSet(now);
			}			
		}
		
		if(dAttuale.getSiacRSubdocsB()!=null) {
			for(SiacRSubdoc r : dAttuale.getSiacRSubdocsB()){
				r.setDataCancellazioneIfNotSet(now);
			}			
		}
		
		if(dAttuale.getSiacRSubdocSogs()!=null) {
			for(SiacRSubdocSog r : dAttuale.getSiacRSubdocSogs()){
				r.setDataCancellazioneIfNotSet(now);
			}			
		}
		
		if(dAttuale.getSiacRSubdocProvCassas()!=null) {
			for(SiacRSubdocProvCassa r : dAttuale.getSiacRSubdocProvCassas()){
				r.setDataCancellazioneIfNotSet(now);
			}			
		}
		
		if(dAttuale.getSiacRMutuoVoceSubdocs()!=null) {
			for(SiacRMutuoVoceSubdoc r : dAttuale.getSiacRMutuoVoceSubdocs()){
				r.setDataCancellazioneIfNotSet(now);
			}			
		}
		
		if(dAttuale.getSiacRSubdocSplitreverseIvaTipos() != null) {
			for(SiacRSubdocSplitreverseIvaTipo r : dAttuale.getSiacRSubdocSplitreverseIvaTipos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		//inserimento elementi nuovi		
		if(d.getSiacRSubdocAttoAmms()!=null){
			for(SiacRSubdocAttoAmm att : d.getSiacRSubdocAttoAmms()){
				att.setDataModificaInserimento(now);
			}
		}
		
		if(d.getSiacRSubdocAttrs()!=null){
			for(SiacRSubdocAttr r : d.getSiacRSubdocAttrs()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocClasses()!=null){
			for(SiacRSubdocClass r : d.getSiacRSubdocClasses()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocLiquidaziones()!=null) {
			for(SiacRSubdocLiquidazione r : d.getSiacRSubdocLiquidaziones()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocModpags()!=null) {
			for(SiacRSubdocModpag r : d.getSiacRSubdocModpags()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocMovgestTs()!=null) {
			for(SiacRSubdocMovgestT r : d.getSiacRSubdocMovgestTs()){
				r.setDataModificaInserimento(now);
			}
		}
		
//		if(d.getSiacRSubdocOrdinativoTs()!=null) {
//			for(SiacRSubdocOrdinativoT r : d.getSiacRSubdocOrdinativoTs()){
//				r.setDataModificaInserimento(now);
//			}			
//		}
		
		if(d.getSiacRSubdocsA()!=null) {
			for(SiacRSubdoc r : d.getSiacRSubdocsA()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocsB()!=null) {
			for(SiacRSubdoc r : d.getSiacRSubdocsB()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocSogs()!=null) {
			for(SiacRSubdocSog r : d.getSiacRSubdocSogs()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocProvCassas()!=null) {
			for(SiacRSubdocProvCassa r : d.getSiacRSubdocProvCassas()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRMutuoVoceSubdocs()!=null) {
			for(SiacRMutuoVoceSubdoc r : d.getSiacRMutuoVoceSubdocs()){
				r.setDataModificaInserimento(now);
			}			
		}
		
		if(d.getSiacRSubdocSplitreverseIvaTipos() != null) {
			for(SiacRSubdocSplitreverseIvaTipo r : d.getSiacRSubdocSplitreverseIvaTipos()){
				r.setDataModificaInserimento(now);
			}
		}
		// SIAC-5115: clono i dati di sospensione
		d.setSiacTSubdocSospensiones(dAttuale.getSiacTSubdocSospensiones());
		
		super.update(d);
		return d;
	}

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#delete(java.lang.Object)
	 */
	public void delete(SiacTSubdoc e){
		SiacTSubdoc subdoc = this.findById(e.getUid());
		
		Date now = new Date();
		subdoc.setDataCancellazioneIfNotSet(now);		
		
		//cancellazione elementi collegati		
		setDataCancellazione(subdoc.getSiacRSubdocAttoAmms(), now);
		setDataCancellazione(subdoc.getSiacRSubdocAttrs(), now);
		setDataCancellazione(subdoc.getSiacRSubdocClasses(), now);
		setDataCancellazione(subdoc.getSiacRSubdocLiquidaziones(), now);
		setDataCancellazione(subdoc.getSiacRSubdocModpags(), now);
		setDataCancellazione(subdoc.getSiacRSubdocMovgestTs(), now);
//		setDataCancellazione(subdoc.getSiacRSubdocOrdinativoTs(), now);
		setDataCancellazione(subdoc.getSiacRSubdocsA(), now);
		setDataCancellazione(subdoc.getSiacRSubdocsB(), now);
		setDataCancellazione(subdoc.getSiacRSubdocSogs(), now);
		setDataCancellazione(subdoc.getSiacRSubdocProvCassas(), now);
		setDataCancellazione(subdoc.getSiacRElencoDocSubdocs(), now);
		setDataCancellazione(subdoc.getSiacRMutuoVoceSubdocs(), now);
		// SIAC-6039
		setDataCancellazione(subdoc.getSiacTRegistroPccs(), now);
		
	}
	
	@Override
	public List<SiacRSubdocAttoAmm> findSiacRSubdocAttoAmmByAttoAllegatoAndTipoDocumentoAllegato(Integer attoalId, Boolean siacDDocTipoAllegato) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		final String codiceTipoDocumentoAllegato = "ALG";
		
		jpql.append(" FROM SiacRSubdocAttoAmm r ")
			.append(" WHERE r.dataCancellazione IS NULL ")
			.append(" AND EXISTS ( ")
			.append("     FROM r.siacTAttoAmm.siacTAttoAllegatos a ")
			.append("     WHERE a.dataCancellazione IS NULL ")
			.append("     AND a.attoalId = :attoalId")
			.append(" ) ");
		if(Boolean.TRUE.equals(siacDDocTipoAllegato)) {
			jpql.append(" AND r.siacTSubdoc.siacTDoc.siacDDocTipo.docTipoCode = :docTipoCode ");
		} else{
			jpql.append(" AND r.siacTSubdoc.siacTDoc.siacDDocTipo.docTipoCode <> :docTipoCode ");
		}
		
		param.put("docTipoCode", codiceTipoDocumentoAllegato);
		param.put("attoalId", attoalId);
		
		Query query = createQuery(jpql.toString(), param);
		
		@SuppressWarnings("unchecked")
		List<SiacRSubdocAttoAmm> resultList = (List<SiacRSubdocAttoAmm>)query.getResultList();
		
		return resultList;
	}
	
	
	@Override
	public List<SiacRSubdocMovgestT> findSiacRSubdocMovgestTByAttoAllegatoAndTipoDocumentoAllegato(Integer attoalId, Boolean siacDDocTipoAllegato) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		final String codiceTipoDocumentoAllegato = "ALG";
		
		jpql.append(" FROM SiacRSubdocMovgestT r ")
			.append(" WHERE r.dataCancellazione IS NULL ")
			.append(" AND EXISTS ( ")
			.append("     FROM r.siacTSubdoc.siacRElencoDocSubdocs e ")
			.append("     WHERE e.dataCancellazione IS NULL ")
			.append("     AND EXISTS ( FROM e.siacTElencoDoc.siacRAttoAllegatoElencoDocs a ")
			.append("                  WHERE a.dataCancellazione IS NULL ")
			.append("				   AND a.siacTAttoAllegato.attoalId = :attoalId) ")
			.append(" ) ");
		if(Boolean.TRUE.equals(siacDDocTipoAllegato)) {
			jpql.append(" AND r.siacTSubdoc.siacTDoc.siacDDocTipo.docTipoCode = :docTipoCode ");
		} else{
			jpql.append(" AND r.siacTSubdoc.siacTDoc.siacDDocTipo.docTipoCode <> :docTipoCode ");
		}
		
		param.put("docTipoCode", codiceTipoDocumentoAllegato);
		param.put("attoalId", attoalId);
		
		Query query = createQuery(jpql.toString(), param);
		
		@SuppressWarnings("unchecked")
		List<SiacRSubdocMovgestT> resultList = (List<SiacRSubdocMovgestT>)query.getResultList();
		
		return resultList;
	}
	
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
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<Integer> listProvvisorioDiCassaUid,
			Pageable pageable
			) {
	
		
		final String methodName = "ricercaSubdocumento";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT sd ");
		
		componiQueryRicercaSinteticaSubdocumento(jpql, param,enteProprietarioId, bilId, tipoFam, eldocId != null ? Arrays.asList(eldocId) : null, eldocAnno, eldocNumero, eldocNumeroDa, eldocNumeroA, 
				provcAnno, provcNumero, provcDataEmissione,
				tipoProvv, docTipoId, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoCode, attoammId,
				 attoammAnno, attoammNumero, attoammTipoId, uidStruttAmm, annoCapitolo, elemCode, elemCode2, elemCode3, statiDocumento, collegatoAMovimentoDelloStessoBilancio, 
				 associatoAProvvedimentoOAdElenco,  importoDaPagareOIncassareZero, importoDaPagareOIncassareMaggioreDiZero, rilevatiIvaConRegistrazioneONonRilevantiIva,
				 collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto, siacDAttoAmmStatoEnums, associatoAdOrdinativo, conFlagConvalidaManuale,listProvvisorioDiCassaUid);
		
//		jpql.append(" ORDER BY sd.subdocNumero ");
		jpql.append(" ORDER BY sd.subdocNumero, sd.siacTDoc.siacDDocTipo.docTipoCode, sd.siacTDoc.docAnno, sd.siacTDoc.docNumero ");
		
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
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<Integer> listProvvisorioDiCassaUid
			) {
	
		
		final String methodName = "ricercaSubdocumento";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT SUM(sd.subdocImporto - sd.subdocImportoDaDedurre) ");
		
		componiQueryRicercaSinteticaSubdocumento(jpql, param,enteProprietarioId, bilId, tipoFam, eldocId != null ? Arrays.asList(eldocId) : null, eldocAnno, eldocNumero, eldocNumeroDa, eldocNumeroA, 
				provcAnno, provcNumero, provcDataEmissione,
				tipoProvv, docTipoId, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoCode, attoammId,
				attoammAnno, attoammNumero, attoammTipoId, uidStruttAmm,annoCapitolo, elemCode, elemCode2, elemCode3, statiDocumento, collegatoAMovimentoDelloStessoBilancio, 
				associatoAProvvedimentoOAdElenco,  importoDaPagareOIncassareZero, importoDaPagareOIncassareMaggioreDiZero, rilevatiIvaConRegistrazioneONonRilevantiIva,
				collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto, siacDAttoAmmStatoEnums, associatoAdOrdinativo, conFlagConvalidaManuale,listProvvisorioDiCassaUid);
		
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
	
	@Override
	public Long countSinteticaSubdocumenti(
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> tipoFam,
			Collection<Integer> eldocIds,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer eldocNumeroDa,
			Integer eldocNumeroA,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
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
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<Integer> listProvvisorioDiCassaUid
			) {
	
		
		final String methodName = "ricercaSubdocumento";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT COALESCE(COUNT(sd), 0) ");
		
		componiQueryRicercaSinteticaSubdocumento(jpql, param,enteProprietarioId, bilId, tipoFam, eldocIds, eldocAnno, eldocNumero, eldocNumeroDa, eldocNumeroA, 
				provcAnno, provcNumero, provcDataEmissione,
				tipoProvv, docTipoId, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoCode, attoammId,
				 attoammAnno, attoammNumero, attoammTipoId, uidStruttAmm, annoCapitolo, elemCode, elemCode2, elemCode3, statiDocumento, collegatoAMovimentoDelloStessoBilancio, 
				 associatoAProvvedimentoOAdElenco,  importoDaPagareOIncassareZero, importoDaPagareOIncassareMaggioreDiZero, rilevatiIvaConRegistrazioneONonRilevantiIva,
				 collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto, siacDAttoAmmStatoEnums, associatoAdOrdinativo, conFlagConvalidaManuale,listProvvisorioDiCassaUid);
		
		log.debug(methodName, jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		
		return (Long) query.getSingleResult();
	}

	
	private void componiQueryRicercaSinteticaSubdocumento(
			StringBuilder jpql, 
			Map<String, Object> param,
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> docFamTipoCodeEnums,
			Collection<Integer> eldocIds,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer eldocNumeroDa,
			Integer eldocNumeroA,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
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
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<Integer> listProvvisorioDiCassaUid
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
		
		aggiungiFiltriFacoltativiAQueryRicercaSinteticaSubdocumento(jpql, param, "d", "sd" , bilId, eldocIds, eldocAnno, eldocNumero, eldocNumeroDa, eldocNumeroA, 
				provcAnno, provcNumero, provcDataEmissione,
				tipoProvv, docTipoId, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoCode, attoammId,
				attoammAnno, attoammNumero, attoammTipoId, uidStruttAmm,annoCapitolo, elemCode, elemCode2, elemCode3, statiDocumento, collegatoAMovimentoDelloStessoBilancio, 
				associatoAProvvedimentoOAdElenco,  importoDaPagareOIncassareZero, importoDaPagareOIncassareMaggioreDiZero, rilevatiIvaConRegistrazioneONonRilevantiIva,
				collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto, siacDAttoAmmStatoEnums, associatoAdOrdinativo, conFlagConvalidaManuale,listProvvisorioDiCassaUid);
		
		
	}
	
	
	private void aggiungiFiltriFacoltativiAQueryRicercaSinteticaSubdocumento(
			StringBuilder jpql, 
			Map<String, Object> param,
			String aliasDoc,
			String aliasSubdoc,
			Integer bilId,
			Collection<Integer> eldocIds,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer eldocNumeroDa,
			Integer eldocNumeroA,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
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
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<Integer> listProvvisorioDiCassaUid
			) {
		
		if(listProvvisorioDiCassaUid!=null && !listProvvisorioDiCassaUid.isEmpty()){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM "+aliasSubdoc+".siacRSubdocProvCassas pdc ");
			jpql.append("     WHERE pdc.siacTProvCassa.provcId IN (:listProvvisorioDiCassaUid) ");
			jpql.append("     AND pdc.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("listProvvisorioDiCassaUid", listProvvisorioDiCassaUid);
		}
		
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
		
		if(eldocIds != null && !eldocIds.isEmpty()) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM "+aliasSubdoc+".siacRElencoDocSubdocs sde ");
			jpql.append("     WHERE sde.siacTElencoDoc.eldocId IN (:eldocIds) ");
			jpql.append("     AND sde.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("eldocIds", eldocIds);
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
				.append("         AND (ros.dataFineValidita IS NULL OR ros.dataFineValidita > CURRENT_TIMESTAMP) ")
				.append("         AND ros.siacDOrdinativoStato.ordinativoStatoCode NOT IN (:ordinativoStatoCode) ")
				.append("     ) ")
				
				
				.append(" ) ");
			
			param.put("ordinativoStatoCode", SiacDOrdinativoStatoEnum.Annullato.getCodice());
		}
		
		if(Boolean.TRUE.equals(conFlagConvalidaManuale)) {
			jpql.append(" AND ").append(aliasSubdoc).append(".subdocConvalidaManuale = 'M' ");
		} else if (Boolean.FALSE.equals(conFlagConvalidaManuale)) {
			jpql.append(" AND ").append(aliasSubdoc).append(".subdocConvalidaManuale = 'A' ");
		}
		
		
	}
		

	
	@Override
	public void flush() {
		entityManager.flush();
	}
	

	@Override
	public Page<SiacTSubdoc> ricercaSinteticaSubdocumentiDaAssociare(
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> tipoFam,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
//			Integer eldocNumeroDa,
//			Integer eldocNumeroA,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
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
//			Integer annoCapitolo, //???
//			Integer elemCode,
//			Integer elemCode2,
//			Integer elemCode3,
			Set<SiacDDocStatoEnum> statiDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaPagareOIncassareZero, 
//			Boolean importoDaPagareOIncassareMaggioreDiZero,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
//			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
//			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
//			Boolean associatoAdOrdinativo,
			Pageable pageable) {
		
		final String methodName = "ricercaSubdocumento";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		//log.info(methodName, "sql da analizzare");
		
		jpql.append(" SELECT sd ");
		
		componiQueryRicercaSinteticaSubdocumentiDaAssociare(jpql, param ,enteProprietarioId, bilId, tipoFam, eldocId, eldocAnno, eldocNumero, 
//				eldocNumeroDa, eldocNumeroA, 
				provcAnno, provcNumero, provcDataEmissione,
				tipoProvv, docTipoId, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoCode, attoammId,
				 attoammAnno, attoammNumero, attoammTipoId, uidStruttAmm, 
//				 annoCapitolo, elemCode, elemCode2, elemCode3, 
				 statiDocumento, collegatoAMovimentoDelloStessoBilancio, 
				 associatoAProvvedimentoOAdElenco,  importoDaPagareOIncassareZero, 
//				 importoDaPagareOIncassareMaggioreDiZero, 
				 rilevatiIvaConRegistrazioneONonRilevantiIva
//				 collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto, siacDAttoAmmStatoEnums, associatoAdOrdinativo
				 );
		
		
		jpql.append(" ORDER BY sd.subdocId ");
		
		/*
		 * I dati esposti devono essere ordinati per :
			- anno e numero elenco
			- documento: E/S, anno, tipo, numero doc. e numero quota
		 */
		
		log.info(methodName, jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	private void componiQueryRicercaSinteticaSubdocumentiDaAssociare(
			StringBuilder jpql,
			Map<String, Object> param,
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> docFamTipoCodeEnums,
			Integer eldocId,
			Integer eldocNumeroDa,
			Integer eldocNumeroA,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
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
			Set<SiacDDocStatoEnum> statiDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio,
			Boolean associatoAProvvedimentoOAdElenco,
			Boolean importoDaPagareOIncassareZero,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva
			) {
		
		jpql.append(" FROM SiacTSubdoc sd, SiacTDoc d WHERE ( ");
		
		//quote che corrispondono ai filtri inseriti dall'utente
		
			jpql.append(" 		sd.dataCancellazione IS NULL ");
			jpql.append("		AND sd.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
			jpql.append(" 		AND sd.siacTDoc = d ");
			jpql.append(" 		AND d.docCollegatoCec = FALSE "); 
			jpql.append(" 		AND  ( d.docContabilizzaGenpcc = TRUE OR");
			jpql.append(" 				NOT EXISTS (FROM d.siacDDocTipo.siacRDocTipoAttrs rs");
			jpql.append(" 							 WHERE rs.dataCancellazione IS NULL");
			jpql.append(" 							 AND rs.siacTAttr.attrCode IN ('flagAttivaGEN', 'flagComunicaPCC') ");
			jpql.append(" 							 AND rs.boolean_ = 'S' ");
			jpql.append(" 							)");
			jpql.append(" 			 )");
			
			
			// JIRA 5161
			// SIAC-5236: il siac_d_doc_gruppo puo' non esistere, quindi devo impedire che venga effettuata una CROSS JOIN
			
			
			jpql.append("       AND ( ");			
			jpql.append("           d.siacDDocTipo.siacDDocGruppo IS NULL ");
			jpql.append("           OR EXISTS ( ");
			jpql.append("               FROM SiacDDocGruppo ddg ");
			jpql.append("               WHERE ddg.dataCancellazione IS NULL ");
			jpql.append("               AND d.siacDDocTipo.siacDDocGruppo = ddg ");
			jpql.append("               AND ddg.docGruppoTipoCode <> 'NCD' ");
			jpql.append("           )");
			jpql.append("       )");			
			//SIAC-6441
			jpql.append("       AND d.siacDDocTipo.docTipoCode <> 'NCD' ");
			
			param.put("enteProprietarioId", enteProprietarioId);
			
			if(docFamTipoCodeEnums != null){
				jpql.append(" AND sd.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) ");
				
				List<String> docFamTipoCodes = new ArrayList<String>();
				for(SiacDDocFamTipoEnum sddfte : docFamTipoCodeEnums) {
					docFamTipoCodes.add(sddfte.getCodice());
				}
				param.put("docFamTipoCodes", docFamTipoCodes);
			}
			aggiungiFiltriFacoltativiAQueryRicercaSinteticaSubdocumento(jpql, param, "d", "sd", bilId, eldocId != null ? Arrays.asList(eldocId) : null, null, null, eldocNumeroDa, eldocNumeroA, 
					provcAnno, provcNumero, provcDataEmissione,
					tipoProvv, docTipoId, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoCode, attoammId,
					attoammAnno, attoammNumero, attoammTipoId, uidStruttAmm, null, null, null, null, statiDocumento, collegatoAMovimentoDelloStessoBilancio, 
					associatoAProvvedimentoOAdElenco,  importoDaPagareOIncassareZero, null, rilevatiIvaConRegistrazioneONonRilevantiIva,
					null, null, null, null,null);
		
		jpql.append(" ) OR ( ");
		
		//quote collegate alle precedenti
			
			jpql.append(" sd.dataCancellazione IS NULL ");
			jpql.append(" AND sd.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
			jpql.append(" AND sd.siacTDoc = d ");
			param.put("enteProprietarioId", enteProprietarioId);
			
			aggiungiFiltriFacoltativiAQueryRicercaSinteticaSubdocumento(jpql, param,"d", "sd", bilId,
					null, null, null, null, null, null, null, null,null, null, null, null, null, null, 
					null, null, null, null,null, null, null, null,null, null, null, null, 
					statiDocumento, collegatoAMovimentoDelloStessoBilancio, 
					associatoAProvvedimentoOAdElenco,  importoDaPagareOIncassareZero, null, rilevatiIvaConRegistrazioneONonRilevantiIva,
					null, null, null, null,null);
			
			jpql.append(" AND EXISTS ( FROM d.siacRDocsFiglio rdp");
				jpql.append("    		   WHERE rdp.dataCancellazione IS NULL ");
				jpql.append("    		   AND rdp.siacTDocPadre IN (");
				
					jpql.append(" SELECT doc ");
					jpql.append(" FROM SiacTSubdoc sub, SiacTDoc doc ");
					jpql.append(" 		WHERE sub.dataCancellazione IS NULL ");
					jpql.append("		AND sub.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
					jpql.append(" 		AND sub.siacTDoc = doc ");
					jpql.append(" 		AND doc.docCollegatoCec = FALSE "); 
					jpql.append(" 		AND  ( doc.docContabilizzaGenpcc = TRUE OR");
					jpql.append(" 				NOT EXISTS ( FROM doc.siacDDocTipo.siacRDocTipoAttrs ra");
					jpql.append(" 							 WHERE ra.dataCancellazione IS NULL");
					jpql.append(" 							 AND ra.siacTAttr.attrCode IN ('flagAttivaGEN', 'flagComunicaPCC') ");
					jpql.append(" 							 AND ra.boolean_ = 'S' ");
					jpql.append(" 							)");
					jpql.append(" 			 )");
					// JIRA 5161
					// SIAC-5236: il siac_d_doc_gruppo puo' non esistere, quindi devo impedire che venga effettuata una CROSS JOIN
					jpql.append("       AND ( ");
					jpql.append("           doc.siacDDocTipo.siacDDocGruppo IS NULL ");
					jpql.append("           OR EXISTS ( ");
					jpql.append("               FROM SiacDDocGruppo ddg ");
					jpql.append("               WHERE ddg.dataCancellazione IS NULL ");
					jpql.append("               AND doc.siacDDocTipo.siacDDocGruppo = ddg ");
					jpql.append("               AND ddg.docGruppoTipoCode <> 'NCD' ");
					jpql.append("           )");
					jpql.append("       )");
					
					// scommentare SIAC-6441
					jpql.append("       AND d.siacDDocTipo.docTipoCode <> 'NCD' ");
					
					param.put("enteProprietarioId", enteProprietarioId);
					
					if(docFamTipoCodeEnums != null){
						jpql.append(" AND sub.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) ");
						
						List<String> docFamTipoCodes = new ArrayList<String>();
						for(SiacDDocFamTipoEnum sddfte : docFamTipoCodeEnums) {
							docFamTipoCodes.add(sddfte.getCodice());
						}
						param.put("docFamTipoCodes", docFamTipoCodes);
					}
					aggiungiFiltriFacoltativiAQueryRicercaSinteticaSubdocumento(jpql, param, "doc", "sub", bilId, eldocId != null ? Arrays.asList(eldocId) : null, null, null, eldocNumeroDa, eldocNumeroA, 
							provcAnno, provcNumero, provcDataEmissione,
							tipoProvv, docTipoId, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoCode, attoammId,
							attoammAnno, attoammNumero, attoammTipoId, uidStruttAmm, null, null, null, null, statiDocumento, collegatoAMovimentoDelloStessoBilancio, 
							associatoAProvvedimentoOAdElenco,  importoDaPagareOIncassareZero, null, rilevatiIvaConRegistrazioneONonRilevantiIva,
							null, null, null, null,null);
					
				jpql.append(" ) ");
				
			jpql.append("  ) ");
		
		jpql.append(" ) ");	
	}

	
	
	
	@Override
	public BigDecimal ricercaSinteticaSubdocumentiDaAssociareTotaleImporti(
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> tipoFam,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
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
			Set<SiacDDocStatoEnum> statiDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaPagareOIncassareZero, 
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaSubdocumentiDaAssociareTotaleImporti";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT SUM(sd.subdocImporto - sd.subdocImportoDaDedurre) ");
		
		componiQueryRicercaSinteticaSubdocumentiDaAssociare(jpql, param ,enteProprietarioId, bilId, tipoFam, eldocId, eldocAnno, eldocNumero, 
				provcAnno, provcNumero, provcDataEmissione,
				tipoProvv, docTipoId, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoCode, attoammId,
				 attoammAnno, attoammNumero, attoammTipoId, uidStruttAmm, statiDocumento, collegatoAMovimentoDelloStessoBilancio, 
				 associatoAProvvedimentoOAdElenco,  importoDaPagareOIncassareZero, rilevatiIvaConRegistrazioneONonRilevantiIva);
		
		log.debug(methodName, jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		return result;
	}

	/*
	 * Chiavi di esempio restituite: "123_1000.00_10.00_1_T_N" (Quando legato ad impegno), "123_100.00_10.00_4_S_N" (Quando legato a subimpegno)
	 */
	@Override
	public String computeKeySubdocImportoMovimentoGestioneFlagRilevanteIva(int subdocId) {
		final String methodName = "computeKeySubdocImportoMovimentoGestioneFlagRilevanteIva";
		
		StringBuilder sb = new StringBuilder();

		sb.append("select s.subdoc_id || '_' || s.subdoc_importo || '_' || s.subdoc_importo_da_dedurre || '_' || mt.movgest_ts_id || '_' || dmttipo.movgest_ts_tipo_code || '_' || sa.boolean ");
		sb.append(" || '_' || ( ");
		
		
		sb.append("                        case ");
		sb.append("                        when sa.boolean='N' then 'N' ");
		sb.append("                        else   ( ");
		
		sb.append("                                  select ");
		sb.append("                                  'ivaMovIdLegatiSub:' ||     "); //subdocIva legato al subdocumento
        sb.append("                                  array_to_string(array(  ");
        sb.append("                                                        select ri.ivamov_id ");
        sb.append("                                                        from siac_r_subdoc_subdoc_iva rssi, ");
        sb.append("                                                        siac_r_ivamov ri ");
        sb.append("                                                        where rssi.data_cancellazione is null ");
        sb.append("                                                        and rssi.subdoc_id = s.subdoc_id ");
        sb.append("                                                        and rssi.subsubdociva_id = ri.subdociva_id ");
        sb.append("                                                        order by ri.ivamov_id ");
        sb.append("                                                        ), ");
        sb.append("                                                  ',', '*') ");
        sb.append("                                  || 'ivaMovIdLegatiDoc:' ||   "); //subdocIva legato al documento
        sb.append("                                  array_to_string(array( ");
        sb.append("                                                        select distinct ri.ivamov_id ");
        sb.append("                                                        from  ");
        sb.append("                                                        siac_r_doc_iva rdi, ");
        sb.append("                                                        siac_r_ivamov ri, ");
        sb.append("                                                        siac_t_subdoc_iva sii ");
        sb.append("                                                        where rdi.data_cancellazione is null ");
        sb.append("                                                        and rdi.doc_id = s.doc_id ");
//        sb.append("                                                        and ri.subdociva_id in (select sii.subdociva_id from siac_t_subdoc_iva sii where sii.dociva_r_id = rdi.dociva_r_id) ");
        sb.append("                                                        and ri.subdociva_id = sii.subdociva_id ");
        sb.append("                                                        and sii.dociva_r_id = rdi.dociva_r_id ");
        sb.append("                                                        and sii.data_cancellazione is null ");
        sb.append("                                                        and ri.data_cancellazione is null ");
        sb.append("                                                        order by ri.ivamov_id ");
        sb.append("                                                        ),");
        sb.append("                                                  ',', '*') ");
		
		
		sb.append("                                ) ");
		sb.append("                        end ");
		//SIAC-5462
		sb.append("  || '_' || ( select count(*)              ");
		sb.append("  		from siac_t_subdoc ts2              ");
		sb.append("  		where s.doc_id = ts2.doc_id         ");
		sb.append("  		and ts2.data_cancellazione is null  ");
		sb.append("  	)                                       ");
		
		sb.append(" ) ");
		sb.append(" as key");
		
		sb.append("  from siac_t_subdoc  s,");
		sb.append("       siac_r_subdoc_movgest_ts rsm, ");
		sb.append("       siac_d_movgest_ts_tipo dmttipo, ");//T Testata, S Sub
		sb.append("       siac_t_movgest_ts mt,");
		sb.append("       siac_r_subdoc_attr sa");
		sb.append(" where s.subdoc_id = :subdocId"); //Legame con subdocumento
		
		//Legame col movimento di gestione
		sb.append("   and rsm.data_cancellazione is null");
		sb.append("   and rsm.subdoc_id = s.subdoc_id");
		sb.append("   and mt.movgest_ts_id = rsm.movgest_ts_id");
		sb.append("   and dmttipo.movgest_ts_tipo_id = mt.movgest_ts_tipo_id");
		sb.append("   and sa.subdoc_id = s.subdoc_id");
		sb.append("   and sa.data_cancellazione is null");
		
		//Legame con l'attributo flagRilevanteIva
		sb.append("   and sa.attr_id = (select attr_id ");
		sb.append("                       from siac_t_attr ta");
		sb.append("                      where ta.data_cancellazione is null");
		sb.append("                        and ta.attr_code = '" + SiacTAttrEnum.FlagRilevanteIVA.getCodice() + "'");
		sb.append("                        and ta.ente_proprietario_id = s.ente_proprietario_id)");
		sb.append( "order by key ");

		Query query = entityManager.createNativeQuery(sb.toString()); 
		query.setParameter("subdocId", subdocId);
		
		@SuppressWarnings("unchecked")
		List<String> results = (List<String>) query.getResultList(); //mi aspetto un solo record! tempo di esecuzione su siac_isola_20151124: circa 20 ms
		
		if(results == null || results.isEmpty()) {
			log.warn(methodName, "Non ho ottenuto nessun risultato per subdocId="+subdocId+". Il subdoc potrebbe ad esempio non avere legami con siac_r_subdoc_movgest_ts ");
			log.debug(methodName, "Returning: " + subdocId);
			return ""+subdocId;//TODO valutare se sostituire return con throw IllegalArgumentException
		}
		
		StringBuilder sbr = new StringBuilder();
		for (String s : results) { //mi aspetto una sola iterazione.
			sbr.append(s);
		}
		String result = sbr.toString();
		
		if(results.size()>1) {
			log.warn(methodName, "Ottenuto piu' di un risultato per subdocId="+subdocId+". "
					+ "Non dovrebbe succedere! Ricontrollare dati su db e perfezionare di conseguenza query in SubdocumentoDaoImpl. "
					+ "Returning: "+ result);
		}
		
		log.debug(methodName, "Returning: " + result);
		return result;
	}

	@Override
	public Page<SiacTSubdoc> ricercaSinteticaSubdocumentiPerProvvisorio(
			int enteProprietarioId, 
			Set<SiacDDocFamTipoEnum> siacDDocFamTipoEnums,
			Integer docTipoId, 
			Integer docAnno, 
			String docNumero,
			Date docDataEmissione, 
			Integer subdocNumero,
			BigDecimal movgestNumero, 
			Integer movgestAnno, 
			Integer soggettoId,
			String soggettoCode,
			Integer eldocAnno,
			Integer eldocNumero,
			Boolean flgEscludiSubDocCollegati,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaSubdocumentiPerProvvisorio";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT DISTINCT sd ");
		jpql.append(" FROM SiacTSubdoc sd, SiacTDoc d, SiacRDocStato rds ");
		jpql.append(" LEFT OUTER JOIN sd.siacRSubdocMovgestTs rsmt ");
		jpql.append(" 		WHERE sd.dataCancellazione IS NULL ");
		jpql.append(" 		AND d.dataCancellazione IS NULL ");
		jpql.append("		AND sd.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" 		AND sd.siacTDoc = d ");
		jpql.append(" 		AND rds.siacTDoc = d ");
		//SIAC-6048
		jpql.append("       AND sd.subdocSplitreverseImporto IS NULL ");
		jpql.append(" 		AND rds.dataCancellazione IS NULL ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(siacDDocFamTipoEnums != null){
			jpql.append(" AND sd.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) ");
			
			List<String> docFamTipoCodes = new ArrayList<String>();
			for(SiacDDocFamTipoEnum sddfte : siacDDocFamTipoEnums) {
				docFamTipoCodes.add(sddfte.getCodice());
			}
			param.put("docFamTipoCodes", docFamTipoCodes);
		}
		
		appendFiltroQuotaAggiornabile(jpql);
		appendFiltriUtente(jpql, param,  docTipoId, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoId, soggettoCode, eldocAnno, eldocNumero,flgEscludiSubDocCollegati);
		
		jpql.append(" ORDER BY sd.subdocId ");
		log.debug(methodName, jpql.toString());
		return getDistinctPagedList(jpql.toString(), param, pageable);
	}

	private void appendFiltroQuotaAggiornabile(StringBuilder jpql) {
		//documento non annullato o emesso
		jpql.append(" AND rds.siacDDocStato.docStatoCode NOT IN ('A','E') ");
		//quota non collegata ad ordinativo in stato diverso da annullato
		jpql.append(" AND NOT EXISTS ( ");
		jpql.append("     SELECT rso ");
		jpql.append("     FROM sd.siacRSubdocOrdinativoTs rso, SiacTOrdinativoT ot, SiacTOrdinativo o, SiacROrdinativoStato ors ");
		jpql.append("     WHERE rso.siacTOrdinativoT.ordTsId = ot.ordTsId	");
		jpql.append("     AND ot.siacTOrdinativo.ordId = o.ordId	");
		jpql.append("     AND ors.siacTOrdinativo = o ");
		jpql.append("     AND rso.dataCancellazione IS NULL");
		jpql.append("     AND ot.dataCancellazione IS NULL ");
		jpql.append("     AND o.dataCancellazione IS NULL ");
		jpql.append("     AND (rso.dataFineValidita IS NULL OR rso.dataFineValidita > CURRENT_TIMESTAMP) ");
		jpql.append("     AND (ot.dataFineValidita IS NULL OR ot.dataFineValidita > CURRENT_TIMESTAMP) ");
		jpql.append("     AND (o.dataFineValidita IS NULL OR o.dataFineValidita > CURRENT_TIMESTAMP) ");
		jpql.append("     AND ors.dataCancellazione IS NULL ");
		jpql.append("     AND (ors.dataFineValidita IS NULL OR ors.dataFineValidita > CURRENT_TIMESTAMP) ");
		jpql.append("     AND ors.siacDOrdinativoStato.ordinativoStatoCode <> 'A' ");
		jpql.append(" ) ");
	}
	
	private void appendFiltriUtente(StringBuilder jpql, Map<String, Object> param,
			Integer docTipoId, Integer docAnno, String docNumero, Date docDataEmissione, Integer subdocNumero, 
			BigDecimal movgestNumero, Integer movgestAnno, Integer soggettoId, String soggettoCode, Integer eldocAnno, Integer eldocNumero,Boolean flgEscludiSubDocCollegati) {
		
		if(docTipoId != null && docTipoId != 0) {
			jpql.append(" AND d.siacDDocTipo.docTipoId = :docTipoId ");
			param.put("docTipoId", docTipoId);
		}
		
		if(!StringUtils.isEmpty(docNumero)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.docNumero", "CONCAT('%', :docNumero, '%')") + " ");
			param.put("docNumero", docNumero);
		}
		
		if(docAnno != null) {
			jpql.append(" AND d.docAnno = :docAnno ");
			param.put("docAnno", docAnno);
		}

		if(docDataEmissione != null) {
			jpql.append(" AND d.docDataEmissione = :docDataEmissione ");
			param.put("docDataEmissione", docDataEmissione);
		}
		
		if(subdocNumero != null){
			jpql.append(" AND sd.subdocNumero = :subdocNumero ");
			param.put("subdocNumero", subdocNumero);
		}
		
		if(movgestNumero != null){
			jpql.append(" AND rsmt.dataCancellazione IS NULL ");
			jpql.append(" AND rsmt.siacTMovgestT.siacTMovgest.movgestNumero = :movgestNumero ");
			
			param.put("movgestNumero", movgestNumero);
		}
		
		if(movgestAnno != null) {
			jpql.append(" AND rsmt.dataCancellazione IS NULL ");
			jpql.append(" AND rsmt.siacTMovgestT.siacTMovgest.movgestAnno = :movgestAnno ");
			
			param.put("movgestAnno", movgestAnno);
		}
		
		if(soggettoId != null && soggettoId != 0){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM d.siacRDocSogs sds ");
			jpql.append("     WHERE sds.siacTSoggetto.soggettoId = :soggettoId ");
			jpql.append("     AND sds.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("soggettoId", soggettoId);
		}
		
		if((soggettoId == null || soggettoId.intValue() == 0) && StringUtils.isNotBlank(soggettoCode)){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM d.siacRDocSogs sds ");
			jpql.append("     WHERE sds.siacTSoggetto.soggettoCode = :soggettoCode ");
			jpql.append("     AND sds.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("soggettoCode", soggettoCode);
		}
		
		if(eldocAnno != null) {
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM sd.siacRElencoDocSubdocs sred ");
			jpql.append("     WHERE  sred.siacTElencoDoc.eldocAnno = :eldocAnno");
			jpql.append("     AND sred.dataCancellazione IS NULL ");
			jpql.append(" ) ");
		
			param.put("eldocAnno", eldocAnno);
		}
		
		if(eldocNumero != null){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM sd.siacRElencoDocSubdocs sred ");
			jpql.append("     WHERE sred.siacTElencoDoc.eldocNumero = :eldocNumero");
			jpql.append("     AND sred.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("eldocNumero", eldocNumero);
		}
		// SIAC-6909 
		if(flgEscludiSubDocCollegati){
			jpql.append(" AND NOT EXISTS ( " );
			jpql.append("     FROM sd.siacRSubdocProvCassas srpc ");			
			jpql.append("     WHERE srpc.dataCancellazione IS NULL ");
			jpql.append(" ) ");			
		}
	}

//	@Override
//	public BigDecimal ricercaSinteticaSubdocumentiPerProvvisorioTotaleImporti(
//			int enteProprietarioId, 
//			EnumSet<SiacDDocFamTipoEnum> siacDDocFamTipoEnums,
//			Integer docTipoId, 
//			Integer docAnno, 
//			String docNumero,
//			Date docDataEmissione, 
//			Integer subdocNumero,
//			BigDecimal movgestNumero, 
//			Integer movgestAnno,
//			Integer soggettoId,
//			String soggettoCode,
//			Integer eldocAnno,
//			Integer eldocNumero,
//			Pageable pageable) {
//		final String methodName = "ricercaSinteticaSubdocumentiPerProvvisorioTotaleImporti";
//		StringBuilder jpql = new StringBuilder();
//		Map<String, Object> param = new HashMap<String, Object>();
//		
//		jpql.append(" SELECT COALESCE(SUM(ts.subdocImporto - ts.subdocImportoDaDedurre), 0) ");
//		jpql.append(" FROM SiacTSubdoc ts ");
//		jpql.append(" WHERE ts.subdocId IN ( ");
//		jpql.append("     SELECT DISTINCT sd.subdocId ");
//		jpql.append("     FROM SiacTSubdoc sd, SiacTDoc d, SiacRDocStato rds ");
//		jpql.append("     LEFT OUTER JOIN sd.siacRSubdocMovgestTs rsmt ");
//		jpql.append("     WHERE sd.dataCancellazione IS NULL ");
//		jpql.append("     AND d.dataCancellazione IS NULL ");
//		jpql.append("     AND sd.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
//		jpql.append("     AND sd.siacTDoc = d ");
//		param.put("enteProprietarioId", enteProprietarioId);
//		
//		if(siacDDocFamTipoEnums != null){
//			jpql.append(" AND sd.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) ");
//			List<String> docFamTipoCodes = new ArrayList<String>();
//			for(SiacDDocFamTipoEnum sddfte : siacDDocFamTipoEnums) {
//				docFamTipoCodes.add(sddfte.getCodice());
//			}
//			param.put("docFamTipoCodes", docFamTipoCodes);
//		}
//		
//		appendFiltroQuotaAggiornabile(jpql);
//		appendFiltriUtente(jpql, param,  docTipoId, docAnno, docNumero, docDataEmissione, subdocNumero, movgestNumero, movgestAnno, soggettoId, soggettoCode, eldocAnno, eldocNumero);
//		
//		jpql.append(" ) ");
//		
//		log.debug(methodName, jpql.toString());
//		Query query = createQuery(jpql.toString(), param);
//		BigDecimal result = (BigDecimal) query.getSingleResult();
//		
//		return result;
//	}
	
	@Override
	public BigDecimal ricercaSinteticaSubdocumentiPerProvvisorioTotaleImporti(Collection<Integer> subdocIds) {
		final String methodName = "ricercaSinteticaSubdocumentiPerProvvisorioTotaleImporti";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT COALESCE(SUM(ts.subdocImporto - ts.subdocImportoDaDedurre), 0) ");
		jpql.append(" FROM SiacTSubdoc ts ");
		jpql.append(" WHERE ts.subdocId IN (:subdocIds) ");
		
		param.put("subdocIds", subdocIds);
		
		log.debug(methodName, jpql.toString());
		Query query = createQuery(jpql.toString(), param);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		return result;
	}

	@Override
	public Page<SiacTSubdoc> ricercaSinteticaSubdocumentiByDocId(Integer docId, Boolean rilevanteIva, Pageable pageable) {
		final String methodName = "ricercaSinteticaSubdocumentiByDocId";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacTSubdoc ts ");
		jpql.append(" WHERE ts.siacTDoc.docId = :docId ");
		jpql.append(" AND ts.dataCancellazione IS NULL ");
		
		param.put("docId", docId);
		if(rilevanteIva != null) {
			jpql.append(" AND ").append(Boolean.FALSE.equals(rilevanteIva) ? "NOT " : "").append("EXISTS ( ");
			jpql.append("     FROM ts.siacRSubdocAttrs rsa ");
			jpql.append("     WHERE rsa.dataCancellazione IS NULL ");
			jpql.append("     AND rsa.boolean_ = 'S' ");
			jpql.append("     AND rsa.siacTAttr.attrCode = '").append(SiacTAttrEnum.FlagRilevanteIVA.getCodice()).append("' ");
			jpql.append(" ) ");
		}
		appendOrderBy(jpql, "ts", pageable.getSort());
		log.debug(methodName, jpql.toString());
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	private void appendOrderBy(StringBuilder sb, String alias, Sort sort) {
		if(sort == null) {
			return;
		}
		sb.append(" ORDER BY ");
		boolean first = true;
		for(Iterator<Order> it = sort.iterator(); it.hasNext();) {
			Order order = it.next();
			
			sb.append(!first ? ", " : "")
				.append(alias)
				.append(".")
				.append(order.getProperty())
				.append(" ")
				.append(order.getDirection().toString());
			first = false;
		}
	}


	@Override
	public List<SiacTSubdoc> ricercaQuoteDaEmettere(Set<SiacDDocFamTipoEnum> docFamTipoCodeEnums,
			Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Set<SiacDDocStatoEnum> siacDDocStatoEnums,
			Integer attoammId,
			Integer attoammAnno,
			Integer attoammNumero,
			List<Integer> eldocIds,
			Integer eldocAnno,
			Integer eldocNumero,
			String convalidaManualeCode,
			Integer enteProprietarioId) {
		
		final String methodName = "ricercaQuoteDaEmettere";
		String aliasSubdoc ="sd";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT sd ");
		
		createQueryQuoteDaEmettere(docFamTipoCodeEnums, siacDAttoAmmStatoEnums, siacDDocStatoEnums, attoammId,
				attoammAnno, attoammNumero, eldocIds, eldocAnno, eldocNumero, enteProprietarioId, aliasSubdoc, jpql,
				param);
		
		jpql.append(" ORDER BY sd.subdocNumero, sd.siacTDoc.siacDDocTipo.docTipoCode, sd.siacTDoc.docAnno, sd.siacTDoc.docNumero ");
		
		Query query = createQuery(jpql.toString(), param);
		
		log.debug(methodName, jpql.toString());

		@SuppressWarnings("unchecked")
		List<SiacTSubdoc> result = query.getResultList();
		log.debug(methodName, "returning result: "+result);
		
		return result;
	}

	private void createQueryQuoteDaEmettere(Set<SiacDDocFamTipoEnum> docFamTipoCodeEnums,
			Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums, Set<SiacDDocStatoEnum> siacDDocStatoEnums,
			Integer attoammId, Integer attoammAnno, Integer attoammNumero, List<Integer> eldocIds, Integer eldocAnno,
			Integer eldocNumero, Integer enteProprietarioId, String aliasSubdoc, StringBuilder jpql,
			Map<String, Object> param) {
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
		
		if(siacDDocStatoEnums!=null && !siacDDocStatoEnums.isEmpty()) {
			jpql.append(" AND EXISTS (  ");
			jpql.append("     FROM d.siacRDocStatos r ");
			jpql.append("     WHERE r.dataCancellazione IS NULL ");
			jpql.append("     AND r.siacDDocStato.docStatoCode IN (:docStatoCodes) ");
			jpql.append(" ) ");
			
			List<String> docStatoCodes = new ArrayList<String>();
			for(SiacDDocStatoEnum sddse : siacDDocStatoEnums) {
				docStatoCodes.add(sddse.getCodice());
			}
			param.put("docStatoCodes", docStatoCodes);
		}
				
		componiQueryElenco(eldocIds, eldocAnno, eldocNumero,  jpql, param);
		
		
		
		componyQueryAttoAmm(attoammId, attoammAnno, attoammNumero, siacDAttoAmmStatoEnums, jpql, param);
		
		jpql.append("AND");
		jpql.append(" ((sd.subdocImporto - sd.subdocImportoDaDedurre) > 0 ) ");
		
		componyQueryLiquidazione(jpql, param);
		jpql.append(" AND ").append(aliasSubdoc).append(".subdocConvalidaManuale = 'M' ");
	}
	
	@Override
	public Long countQuoteDaEmettere(Set<SiacDDocFamTipoEnum> docFamTipoCodeEnums,
			Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Set<SiacDDocStatoEnum> siacDDocStatoEnums,
			Integer attoammId,
			Integer attoammAnno,
			Integer attoammNumero,
			List<Integer> eldocIds,
			Integer eldocAnno,
			Integer eldocNumero,
			String convalidaManualeCode,
			Integer enteProprietarioId) {
		
		final String methodName = "countQuoteDaEmettere";
		String aliasSubdoc ="sd";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT COALESCE(COUNT(sd), 0) ");
		createQueryQuoteDaEmettere(docFamTipoCodeEnums, siacDAttoAmmStatoEnums, siacDDocStatoEnums, attoammId,
				attoammAnno, attoammNumero, eldocIds, eldocAnno, eldocNumero, enteProprietarioId, aliasSubdoc, jpql,
				param);
		
		Query query = createQuery(jpql.toString(), param);
		
		log.debug(methodName, jpql.toString());

		Long result = (Long) query.getSingleResult();
		log.debug(methodName, "returning result: "+result);
		
		return result;
	}

	/**
	 * @param aliasSubdoc
	 * @param jpql
	 * @param param
	 */
	private void componyQueryLiquidazione(StringBuilder jpql, Map<String, Object> param) {
		// Liquidazione in con AttoAmministrativo in stato DEFINITIVO
		//Liquidazione con importo disponibilita pagare >= importo da liquidare del subdocumento
		jpql.append(" AND EXISTS( ");
		jpql.append("     FROM sd.siacRSubdocLiquidaziones rl, SiacTLiquidazione l, SiacRLiquidazioneStato rls, SiacRLiquidazioneAttoAmm rlaa, SiacRAttoAmmStato raas ");
		// Join conditions
		jpql.append("     WHERE rl.siacTLiquidazione = l ");
		jpql.append("     AND rls.siacTLiquidazione = l ");
		jpql.append("     AND rlaa.siacTLiquidazione = l ");
		jpql.append("     AND rlaa.siacTAttoAmm = raas.siacTAttoAmm ");
		// Date cancellazione e fine validita
		jpql.append("     AND rl.dataCancellazione IS NULL ");
		jpql.append("     AND rls.dataCancellazione IS NULL ");
		jpql.append("     AND rlaa.dataCancellazione IS NULL ");
		jpql.append("     AND raas.dataCancellazione IS NULL ");
		jpql.append("     AND rlaa.siacTAttoAmm.dataCancellazione IS NULL ");
		jpql.append("     AND (rls.dataFineValidita IS NULL OR rls.dataFineValidita > CURRENT_TIMESTAMP) ");
		// Condizioni sui parametri
		jpql.append("     AND rls.siacDLiquidazioneStato.liqStatoCode = :liqStatoCode ");
		jpql.append("     AND raas.siacDAttoAmmStato.attoammStatoCode = :attoammStatoCode");
		// Contorllo importo
		jpql.append("     AND (l.liqImporto - ( ");
		jpql.append("         SELECT COALESCE(SUM(otd.ordTsDetImporto), 0) ");
		jpql.append("         FROM SiacTOrdinativoTsDet otd, SiacTOrdinativoT ot , SiacTOrdinativo o");
		jpql.append("         WHERE otd.dataCancellazione IS NULL ");
		jpql.append("         AND otd.siacDOrdinativoTsDetTipo.ordTsDetTipoCode = :ordTsDetTipoCode ");
		jpql.append("         AND otd.siacTOrdinativoT = ot ");
		jpql.append("         AND ot.siacTOrdinativo = o ");
		jpql.append("         AND EXISTS ( ");
		jpql.append("             FROM ot.siacRLiquidazioneOrds lo, o.siacROrdinativoStatos so ");
		jpql.append("             WHERE lo.dataCancellazione IS NULL " );
		jpql.append("             AND so.dataCancellazione IS NULL " );
		jpql.append("             AND lo.siacTLiquidazione = l ");
		jpql.append("         	  AND (so.dataFineValidita IS NULL OR so.dataFineValidita > CURRENT_TIMESTAMP) ");
		jpql.append("             AND so.siacDOrdinativoStato.ordinativoStatoCode NOT IN (:ordinativoStatoCode) ");
		jpql.append("             ) ");
		jpql.append("         ) ");
		jpql.append("     ) >= (sd.subdocImporto - sd.subdocImportoDaDedurre) ");
		
		param.put("liqStatoCode", SiacDLiquidazioneStatoEnum.Valido.getCodice());
		param.put("attoammStatoCode", SiacDAttoAmmStatoEnum.DEFINITIVO.getCodice());
		param.put("ordTsDetTipoCode", "A");
		param.put("ordinativoStatoCode", SiacDOrdinativoStatoEnum.Annullato.getCodice());
		
//		ordinativoStatoCode
		jpql.append(" ) ");
	}

	/**
	 * @param attoammId
	 * @param attoammAnno
	 * @param attoammNumero
	 * @param jpql
	 * @param param
	 */
	private void componyQueryAttoAmm(Integer attoammId, Integer attoammAnno, Integer attoammNumero, Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums, StringBuilder jpql,
			Map<String, Object> param) {
		if(attoammId!=null){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM sd.siacRSubdocAttoAmms sda ");
			jpql.append("     WHERE sda.siacTAttoAmm.attoammId = :attoammId ");
			jpql.append("     AND sda.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("attoammId", attoammId);

		}
		
		if(attoammAnno!=null){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM sd.siacRSubdocAttoAmms sda ");
			jpql.append("     WHERE sda.siacTAttoAmm.attoammAnno = :attoammAnno ");
			jpql.append("     AND sda.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("attoammAnno", String.valueOf(attoammAnno));
		}
		
		if(attoammNumero!=null){
			jpql.append(" AND EXISTS ( " );
			jpql.append("     FROM sd.siacRSubdocAttoAmms sda ");
			jpql.append("     WHERE sda.siacTAttoAmm.attoammNumero = :attoammNumero ");
			jpql.append("     AND sda.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("attoammNumero", attoammNumero);
		}
		
		if(siacDAttoAmmStatoEnums != null && !siacDAttoAmmStatoEnums.isEmpty()) {
			// il provvedimento e' in stato definito
			jpql.append(" AND EXISTS ( ")
				.append("     FROM sd.siacRSubdocAttoAmms srsaa, SiacRAttoAmmStato sraas ")
				.append("     WHERE srsaa.siacTAttoAmm = sraas.siacTAttoAmm ")
				.append("     AND srsaa.dataCancellazione IS NULL ")
				.append("     AND sraas.dataCancellazione IS NULL ")
				.append("     AND sraas.siacDAttoAmmStato.attoammStatoCode IN (:siacDAttoAmmStatos) ")
				.append(" ) ");
			
			List<String> siacDAttoAmmStatos = new ArrayList<String>();
			for(SiacDAttoAmmStatoEnum sdaase : siacDAttoAmmStatoEnums) {
				siacDAttoAmmStatos.add(sdaase.getCodice());
			}
			param.put("siacDAttoAmmStatos", siacDAttoAmmStatos);
		}
	}

	
	private void componiQueryElenco(List<Integer> eldocIds, Integer eldocAnno, Integer eldocNumero, StringBuilder jpql, Map<String, Object> param){
		if(eldocIds != null && !eldocIds.isEmpty()) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM sd.siacRElencoDocSubdocs sde ");
			jpql.append("     WHERE sde.siacTElencoDoc.eldocId IN (:eldocIds) ");
			jpql.append("     AND sde.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			
			param.put("eldocIds", eldocIds);
			
		}
		
		
		if(eldocAnno != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM sd.siacRElencoDocSubdocs sde ");
			jpql.append("     WHERE sde.siacTElencoDoc.eldocAnno = :eldocAnno ");
			jpql.append("     AND sde.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("eldocAnno", eldocAnno);
		}
		
		if(eldocNumero != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM sd.siacRElencoDocSubdocs sde ");
			jpql.append("     WHERE sde.siacTElencoDoc.eldocNumero = :eldocNumero ");
			jpql.append("     AND sde.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("eldocNumero", eldocNumero);
		}
		
	}

	@Override
	public void insertUpdateSiacTSubdocSospensione(Integer uid, List<SiacTSubdocSospensione> siacTSubdocSospensiones) {
		SiacTSubdoc tsOriginale = findById(uid);
		Date now = new Date();
		Integer zero = Integer.valueOf(0);
		
		// Id sospensione - da inserire
		Map<Integer, Boolean> subdocSospIds = new HashMap<Integer, Boolean>();
		for(SiacTSubdocSospensione tss : siacTSubdocSospensiones) {
			subdocSospIds.put(tss.getUid(), Boolean.TRUE);
			if(zero.equals(tss.getUid())) {
				tss.setUid(null);
			}
		}
		if(tsOriginale.getSiacTSubdocSospensiones() == null) {
			// Inizializzo per sicurezza
			tsOriginale.setSiacTSubdocSospensiones(new ArrayList<SiacTSubdocSospensione>());
		}
		for(Iterator<SiacTSubdocSospensione> it = tsOriginale.getSiacTSubdocSospensiones().iterator(); it.hasNext();) {
			SiacTSubdocSospensione tss = it.next();
			if(!subdocSospIds.containsKey(tss.getUid())) {
				subdocSospIds.put(tss.getUid(), Boolean.FALSE);
				tss.setDataCancellazioneIfNotSet(now);
			} else {
				it.remove();
			}
		}
		for(SiacTSubdocSospensione tss : siacTSubdocSospensiones) {
			if(Boolean.TRUE.equals(subdocSospIds.get(tss.getUid()))) {
				// Da inserire
				tss.setDataModificaInserimento(now);
			} else {
				// Da aggiornare
				tss.setDataModificaAggiornamento(now);
			}
			tss.setSiacTEnteProprietario(tsOriginale.getSiacTEnteProprietario());
			tss.setSiacTSubdoc(tsOriginale);
			tsOriginale.getSiacTSubdocSospensiones().add(tss);
		}
		
		super.update(tsOriginale);
	}

	
}
