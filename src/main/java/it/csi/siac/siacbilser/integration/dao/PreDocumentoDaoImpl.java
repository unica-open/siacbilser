/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.entity.SiacDPredocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocPredoc;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoVocePredoc;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocCausale;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocClass;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocModpag;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocProvCassa;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocSog;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.SiacTPredocAnagr;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPredocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTPredocOrderByEnum;

/**
 * The Class PreDocumentoDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PreDocumentoDaoImpl extends ExtendedJpaDao<SiacTPredoc, Integer> implements PreDocumentoDao {
	
	@Override
	public SiacTPredoc create(SiacTPredoc e){
		
		Date now = new Date();
		e.setDataModificaInserimento(now);
		
		if(e.getSiacRPredocAttoAmms()!=null){
			for(SiacRPredocAttoAmm r : e.getSiacRPredocAttoAmms()){
				r.setDataModificaInserimento(now);
			}
		}

		if(e.getSiacRPredocBilElems()!=null){
			for(SiacRPredocBilElem r : e.getSiacRPredocBilElems()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocCausales()!=null){
			for(SiacRPredocCausale r : e.getSiacRPredocCausales()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocClasses()!=null){
			for(SiacRPredocClass r : e.getSiacRPredocClasses()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocModpags()!=null){
			for(SiacRPredocModpag r : e.getSiacRPredocModpags()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocMovgestTs()!=null){
			for(SiacRPredocMovgestT r : e.getSiacRPredocMovgestTs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocProvCassas()!=null){
			for(SiacRPredocProvCassa r : e.getSiacRPredocProvCassas()){
				r.setDataModificaInserimento(now);
			}
		}		
		
		if(e.getSiacRPredocSogs()!=null){
			for(SiacRPredocSog r : e.getSiacRPredocSogs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocStatos()!=null){
			for(SiacRPredocStato r : e.getSiacRPredocStatos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocSubdocs()!=null){
			for(SiacRPredocSubdoc r : e.getSiacRPredocSubdocs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacTPredocAnagrs()!=null){
			for(SiacTPredocAnagr r : e.getSiacTPredocAnagrs()){
				r.setDataModificaInserimento(now);
				r.setUid(null);
			}
		}
		
		if(e.getSiacRMutuoVocePredocs()!=null){
			for(SiacRMutuoVocePredoc r : e.getSiacRMutuoVocePredocs()){
				r.setDataModificaInserimento(now);
				r.setUid(null);
			}
		}
		
		// SIAC-5001
		if(e.getSiacRElencoDocPredocs() != null) {
			for(SiacRElencoDocPredoc r : e.getSiacRElencoDocPredocs()){
				r.setDataModificaInserimento(now);
				r.setUid(null);
			}
		}
		
		
		e.setUid(null);
		super.save(e);
		return e;
		
	}

	@Override
	public SiacTPredoc update(SiacTPredoc e){
		SiacTPredoc eAttuale = this.findById(e.getUid());
		
		Date now = new Date();
		e.setDataModificaAggiornamento(now);		
		
		//cancellazione elementi collegati
		if(eAttuale.getSiacRPredocAttoAmms()!=null){
			for(SiacRPredocAttoAmm r : eAttuale.getSiacRPredocAttoAmms()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}

		if(eAttuale.getSiacRPredocBilElems()!=null){
			for(SiacRPredocBilElem r : eAttuale.getSiacRPredocBilElems()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(eAttuale.getSiacRPredocCausales()!=null){
			for(SiacRPredocCausale r : eAttuale.getSiacRPredocCausales()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(eAttuale.getSiacRPredocClasses()!=null){
			for(SiacRPredocClass r : eAttuale.getSiacRPredocClasses()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(eAttuale.getSiacRPredocModpags()!=null){
			for(SiacRPredocModpag r : eAttuale.getSiacRPredocModpags()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(eAttuale.getSiacRPredocMovgestTs()!=null){
			for(SiacRPredocMovgestT r : eAttuale.getSiacRPredocMovgestTs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(eAttuale.getSiacRPredocProvCassas()!=null){
			for(SiacRPredocProvCassa r : eAttuale.getSiacRPredocProvCassas()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}		
		
		if(eAttuale.getSiacRPredocSogs()!=null){
			for(SiacRPredocSog r : eAttuale.getSiacRPredocSogs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(eAttuale.getSiacRPredocStatos()!=null){
			for(SiacRPredocStato r : eAttuale.getSiacRPredocStatos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(eAttuale.getSiacRPredocSubdocs()!=null){
			for(SiacRPredocSubdoc r : eAttuale.getSiacRPredocSubdocs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(eAttuale.getSiacTPredocAnagrs()!=null){
			for(SiacTPredocAnagr r : eAttuale.getSiacTPredocAnagrs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(eAttuale.getSiacRMutuoVocePredocs()!=null){
			for(SiacRMutuoVocePredoc r : eAttuale.getSiacRMutuoVocePredocs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		entityManager.flush();
		
		//inserimento elementi nuovi		
		if(e.getSiacRPredocAttoAmms()!=null){
			for(SiacRPredocAttoAmm r : e.getSiacRPredocAttoAmms()){
				r.setDataModificaInserimento(now);
			}
		}

		if(e.getSiacRPredocBilElems()!=null){
			for(SiacRPredocBilElem r : e.getSiacRPredocBilElems()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocCausales()!=null){
			for(SiacRPredocCausale r : e.getSiacRPredocCausales()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocClasses()!=null){
			for(SiacRPredocClass r : e.getSiacRPredocClasses()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocModpags()!=null){
			for(SiacRPredocModpag r : e.getSiacRPredocModpags()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocMovgestTs()!=null){
			for(SiacRPredocMovgestT r : e.getSiacRPredocMovgestTs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocProvCassas()!=null){
			for(SiacRPredocProvCassa r : e.getSiacRPredocProvCassas()){
				r.setDataModificaInserimento(now);
			}
		}		
		
		if(e.getSiacRPredocSogs()!=null){
			for(SiacRPredocSog r : e.getSiacRPredocSogs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocStatos()!=null){
			for(SiacRPredocStato r : e.getSiacRPredocStatos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRPredocSubdocs()!=null){
			for(SiacRPredocSubdoc r : e.getSiacRPredocSubdocs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacTPredocAnagrs()!=null){
			for(SiacTPredocAnagr r : e.getSiacTPredocAnagrs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRMutuoVocePredocs()!=null){
			for(SiacRMutuoVocePredoc r : e.getSiacRMutuoVocePredocs()){
				r.setDataModificaInserimento(now);
				r.setUid(null);
			}
		}
		
		// SIAC-5001: non aggiorno l'elenco
		e.setSiacRElencoDocPredocs(eAttuale.getSiacRElencoDocPredocs());
		
		
		super.update(e);
		return e;
	}

	@Override
	public Page<SiacTPredoc> ricercaSinteticaPreDocumento(int enteProprietarioId, 
			SiacDDocFamTipoEnum docFamTipoEnum,
			String predocNumero,
			String predocanRagioneSociale,
			String predocanCognome,
			String predocanNome,
			String predocanCodiceFiscale,
			String predocanPartitaIva,
			String predocPeriodoCompetenza,
			BigDecimal predocImporto,
			Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA,
			Date predocDataTrasmissioneDa,
			Date predocDataTrasmissioneA,
			Integer struttId,
			Integer causId,
			Integer causTipoId,
			Boolean causaleSpesaMancante,
			Integer contotesId,
			Boolean contoTesoreriaMancante,
			String predocStatoCode,
			Integer elemId, 
			Integer movgestId,
			Integer movgestTsId,
			Integer soggettoId,
			Boolean soggettoMancante,
			Integer provCassaId,
			Integer attoammId,
			Boolean attoAmmMancante,
			Integer docAnno,
			String docNumero,
			Integer docTipoId,
			//Boolean  estraiNonPagato,
			//Boolean  estraiNonIncassato,
			Integer contoCorrenteId, //solo per predoc entrata
			Boolean contoCorrenteMancante,
			Boolean nonAnnullati,
			Integer ordAnno,
			BigDecimal ordNumero,
			// SIAC-5001
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			// SIAC-5250
			Collection<SiacTPredocOrderByEnum> siacTPredocOrderByEnums,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaPreDocumento";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT d ");
		componiQueryRicercaSinteticaPreDocumento( jpql, param, enteProprietarioId, docFamTipoEnum, predocNumero, predocanRagioneSociale,predocanCognome,predocanNome,
				 predocanCodiceFiscale, predocanPartitaIva,predocPeriodoCompetenza, predocImporto, predocDataCompetenzaDa,predocDataCompetenzaA,predocDataTrasmissioneDa,predocDataTrasmissioneA,struttId,causId,causTipoId,
				 causaleSpesaMancante,contotesId,contoTesoreriaMancante, predocStatoCode, elemId, movgestId, movgestTsId,
				 soggettoId, soggettoMancante,provCassaId, attoammId, attoAmmMancante, docAnno, docNumero, docTipoId,
				//Boolean  estraiNonPagato,Boolean  estraiNonIncassato
				 contoCorrenteId, contoCorrenteMancante, nonAnnullati, ordAnno, ordNumero, eldocId, eldocAnno, eldocNumero
				);
		
		orderQuery(jpql, "CAST(d.predocNumero AS int)", Arrays.asList("d", "tpa"), siacTPredocOrderByEnums);

		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	@Override
	public BigDecimal ricercaSinteticaPreDocumentoImportoTotale(int enteProprietarioId, 
			SiacDDocFamTipoEnum docFamTipoEnum,
			String predocNumero,
			String predocanRagioneSociale,
			String predocanCognome,
			String predocanNome,
			String predocanCodiceFiscale,
			String predocanPartitaIva,
			String predocPeriodoCompetenza,
			BigDecimal predocImporto,
			Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA,
			Date predocDataTrasmissioneDa,
			Date predocDataTrasmissioneA,
			Integer struttId,
			Integer causId,
			Integer causTipoId,
			Boolean causaleSpesaMancante,
			Integer contotesId,
			Boolean contoTesoreriaMancante,
			String predocStatoCode,
			Integer elemId, 
			Integer movgestId,
			Integer movgestTsId,
			Integer soggettoId,
			Boolean soggettoMancante,
			Integer provCassaId,
			Integer attoammId,
			Boolean attoAmmMancante,
			Integer docAnno,
			String docNumero,
			Integer docTipoId,
			Integer contoCorrenteId,
			Boolean contoCorrenteMancante,
			Boolean nonAnnullati,
			Integer ordAnno,
			BigDecimal ordNumero,
			// SIAC-5001
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero
			) {
		
		final String methodName = "ricercaSinteticaPreDocumentoImportoTotale";
		
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT COALESCE(SUM(d.predocImporto), 0) ");
		
		componiQueryRicercaSinteticaPreDocumento( jpql, param, enteProprietarioId, docFamTipoEnum, predocNumero, predocanRagioneSociale,predocanCognome,predocanNome,
				 predocanCodiceFiscale, predocanPartitaIva,predocPeriodoCompetenza, predocImporto, predocDataCompetenzaDa,predocDataCompetenzaA,predocDataTrasmissioneDa,predocDataTrasmissioneA,struttId,causId,causTipoId,
				 causaleSpesaMancante,contotesId,contoTesoreriaMancante, predocStatoCode, elemId, movgestId, movgestTsId,
				 soggettoId, soggettoMancante,provCassaId, attoammId, attoAmmMancante, docAnno, docNumero, docTipoId, contoCorrenteId, contoCorrenteMancante, nonAnnullati, ordAnno, ordNumero,
				 eldocId, eldocAnno, eldocNumero
				);
		
		Query query = createQuery(jpql.toString(), param);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		log.info(methodName, "returning: "+result);
		
		return result;
	}
	
	

	/**
	 * Componi query ricerca sintetica pre documento.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param enteProprietarioId the ente proprietario id
	 * @param docFamTipoEnum the doc fam tipo enum
	 * @param predocNumero the predoc numero
	 * @param predocanRagioneSociale the predocan ragione sociale
	 * @param predocanCognome the predocan cognome
	 * @param predocanNome the predocan nome
	 * @param predocanCodiceFiscale the predocan codice fiscale
	 * @param predocanPartitaIva the predocan partita iva
	 * @param predocPeriodoCompetenza the predoc periodo competenza
	 * @param predocDataCompetenzaDa the predoc data competenza da
	 * @param predocDataCompetenzaA the predoc data competenza a
	 * @param predocDataTrasmissioneDa the predoc data trasmissione da
	 * @param predocDataTrasmissioneA the predoc data trasmissione a
	 * @param struttId the strutt id
	 * @param causId the caus id
	 * @param causTipoId the caus tipo id
	 * @param causaleMancante the causale mancante
	 * @param contotesId the contotes id
	 * @param contoTesoreriaMancante the conto tesoreria mancante
	 * @param predocStatoCode the predoc stato code
	 * @param elemId the elem id
	 * @param movgestTsId the movgest ts id
	 * @param soggettoId the soggetto id
	 * @param soggettoMancante the soggetto mancante
	 * @param attoammId the attoamm id
	 * @param attoAmmMancante the atto amm mancante
	 * @param docAnno the doc anno
	 * @param docNumero the doc numero
	 * @param docTipoId the doc tipo id
	 * @param nonAnnullati 
	 * @param ordAnno the ord anno
	 * @param eldocId the eldoc id
	 * @param eldocAnno the eldoc anno
	 * @param eldocNumero the eldoc numero
	 */
	private void componiQueryRicercaSinteticaPreDocumento(StringBuilder jpql, Map<String, Object> param,
			int enteProprietarioId, 
			SiacDDocFamTipoEnum docFamTipoEnum,
			String predocNumero,
			String predocanRagioneSociale,
			String predocanCognome,
			String predocanNome,
			String predocanCodiceFiscale,
			String predocanPartitaIva,
			String predocPeriodoCompetenza,
			BigDecimal predocImporto,
			Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA,
			Date predocDataTrasmissioneDa,
			Date predocDataTrasmissioneA,
			Integer struttId,
			Integer causId,
			Integer causTipoId,
			Boolean causaleMancante,
			Integer contotesId,
			Boolean contoTesoreriaMancante,
			String predocStatoCode,
			Integer elemId, 
			Integer movgestId,
			Integer movgestTsId,
			Integer soggettoId,
			Boolean soggettoMancante,
			Integer provCassaId,
			Integer attoammId,
			Boolean attoAmmMancante,
			//Integer ContoCorrenteId, Boolean ContoCorrenteMancante
			Integer docAnno,
			String docNumero,
			Integer docTipoId,
			//Boolean  estraiNonPagato,
			//Boolean  estraiNonIncassato,
			Integer contoCorrenteId,
			Boolean contoCorrenteMancante,
			Boolean nonAnnullati,
			Integer ordAnno,
			BigDecimal ordNumero,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero
			) {
		
		
		jpql.append("FROM SiacTPredoc d, SiacTPredocAnagr tpa ");
		jpql.append(" WHERE d.dataCancellazione IS NULL ");
		jpql.append(" AND tpa.dataCancellazione IS NULL ");
		jpql.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND d.siacDDocFamTipo.docFamTipoCode = :docFamTipoCode ");
		jpql.append(" AND tpa.siacTPredoc = d ");
		//siacTDocPadre.getSiacDDocTipo().getSiacDDocFamTipo().getDocFamTipoCode();			
		
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("docFamTipoCode", docFamTipoEnum.getCodice());
		
		if(!StringUtils.isEmpty(predocNumero)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.predocNumero", "CONCAT('%', :predocNumero, '%')") + " ");
			param.put("predocNumero", predocNumero);
		}
		
		if(!StringUtils.isEmpty(predocanRagioneSociale)){
			jpql.append(" AND ").append(Utility.toJpqlSearchLike("tpa.predocanRagioneSociale", "CONCAT('%', :predocanRagioneSociale, '%')")).append(" ");
			param.put("predocanRagioneSociale",predocanRagioneSociale);
		}
		
		if(!StringUtils.isEmpty(predocanCognome)){
			jpql.append(" AND ").append(Utility.toJpqlSearchLike("tpa.predocanCognome", "CONCAT('%', :predocanCognome, '%')")).append(" ");
			param.put("predocanCognome",predocanCognome);
		}
		
		if(!StringUtils.isEmpty(predocanNome)){
			jpql.append(" AND ").append(Utility.toJpqlSearchLike("tpa.predocanNome", "CONCAT('%', :predocanNome, '%')")).append(" ");
			param.put("predocanNome",predocanNome);
		}
		
		if(!StringUtils.isEmpty(predocanCodiceFiscale)){
			jpql.append("AND ").append(Utility.toJpqlSearchLike("tpa.predocanCodiceFiscale", "CONCAT('%', :predocanCodiceFiscale, '%')")).append(" ");
			param.put("predocanCodiceFiscale",predocanCodiceFiscale);
		}
		
		if(!StringUtils.isEmpty(predocanPartitaIva)){
			jpql.append("AND ").append(Utility.toJpqlSearchLike("tpa.predocanPartitaIva", "CONCAT('%', :predocanPartitaIva, '%')")).append(" ");
			param.put("predocanPartitaIva",predocanPartitaIva);
		}
		
		if(!StringUtils.isEmpty(predocPeriodoCompetenza)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.predocPeriodoCompetenza", "CONCAT('%', :predocPeriodoCompetenza, '%')") + " ");
			param.put("predocPeriodoCompetenza",predocPeriodoCompetenza);
		}
		
		// SIAC-4622
		if(predocImporto != null) {
			jpql.append(" AND d.predocImporto = :predocImporto ");
			param.put("predocImporto", predocImporto);
		}
		
		if(predocDataCompetenzaDa != null) {
			jpql.append(" AND d.predocDataCompetenza >= :predocDataCompetenzaDa ");
			param.put("predocDataCompetenzaDa", predocDataCompetenzaDa);		
		}
		
		if(predocDataCompetenzaA != null) {
			jpql.append(" AND d.predocDataCompetenza <= :predocDataCompetenzaA ");
			param.put("predocDataCompetenzaA", predocDataCompetenzaA);		
		}
		
		if(predocDataTrasmissioneDa != null) {
			jpql.append(" AND d.predocDataTrasmissione >= :predocDataTrasmissioneDa ");
			param.put("predocDataTrasmissioneDa", predocDataTrasmissioneDa);		
		}
		
		if(predocDataTrasmissioneA != null) {
			jpql.append(" AND d.predocDataTrasmissione <= :predocDataTrasmissioneA ");
			param.put("predocDataTrasmissioneA", predocDataTrasmissioneA);		
		}

		if (Boolean.TRUE.equals(causaleMancante)) {
			jpql.append(" AND NOT EXISTS ( ");
			jpql.append("     FROM d.siacRPredocCausales dc ");
			jpql.append("     WHERE dc.dataCancellazione IS NULL ");
			jpql.append(" ) ");
		} else if (causId != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocCausales dc ");
			jpql.append("     WHERE dc.siacDCausale.causId= :causId ");
			jpql.append("     AND dc.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("causId", causId);
		}
		
		if (causTipoId != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM SiacRPredocCausale rpc, SiacRCausaleTipo rct ");
			jpql.append("     WHERE rpc.siacTPredoc = d ");
			jpql.append("     AND rct.siacDCausale = rpc.siacDCausale ");
			jpql.append("     AND rpc.dataCancellazione IS NULL ");
			jpql.append("     AND rct.dataCancellazione IS NULL ");
			jpql.append("     AND rct.siacDCausaleTipo.causTipoId = :causTipoId ");
			jpql.append(" ) ");
			param.put("causTipoId", causTipoId);
		}
		
		if(Boolean.TRUE.equals(contoTesoreriaMancante)){
			jpql.append(" AND d.siacDContotesoreria IS NULL "); 
		} else if(contotesId != null){
			jpql.append(" AND d.siacDContotesoreria.contotesId= :contotesId ");
			param.put("contotesId", contotesId);
		}
		 
		
		if(StringUtils.isNotBlank(predocStatoCode)) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocStatos ds ");
			jpql.append("     WHERE ds.dataCancellazione IS NULL ");
			jpql.append("     AND ds.siacDPredocStato.predocStatoCode = :predocStatoCode ");
			jpql.append(" ) ");
			param.put("predocStatoCode", predocStatoCode);
		}
		
		if(elemId != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocBilElems db ");
			jpql.append("     WHERE db.siacTBilElem.elemId  = :elemId ");
			jpql.append("     AND db.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("elemId", elemId);
		}
		
		if(movgestId != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocMovgestTs dm ");
			jpql.append("     WHERE dm.siacTMovgestT.siacTMovgest.movgestId  = :movgestId ");
			jpql.append("     AND dm.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("movgestId",movgestId);
		}
		
		if(movgestTsId != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocMovgestTs dm ");
			jpql.append("     WHERE dm.siacTMovgestT.movgestTsId  = :movgestTsId ");
			jpql.append("     AND dm.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("movgestTsId",movgestTsId);
		}
		
		if(Boolean.TRUE.equals(soggettoMancante)){
			jpql.append(" AND NOT EXISTS ( ");
			jpql.append("     FROM d.siacRPredocSogs ds ");
			jpql.append("     WHERE ds.dataCancellazione IS NULL ");
			jpql.append(" ) ");
		} else if(soggettoId != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocSogs ds ");
			jpql.append("     WHERE ds.siacTSoggetto.soggettoId  = :soggettoId ");
			jpql.append("     AND ds.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("soggettoId",soggettoId);
		}
		if(provCassaId != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocProvCassas pv ");
			jpql.append("     WHERE pv.siacTProvCassa.provcId  = :provCassaId ");
			jpql.append("     AND pv.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("provCassaId",provCassaId);
		}
		
		if(Boolean.TRUE.equals(attoAmmMancante)){
			jpql.append(" AND NOT EXISTS ( ");
			jpql.append("     FROM d.siacRPredocAttoAmms da ");
			jpql.append("     WHERE da.dataCancellazione IS NULL ");
			jpql.append(" ) ");
		}else if(attoammId != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocAttoAmms da ");
			jpql.append("     WHERE da.siacTAttoAmm.attoammId  = :attoammId ");
			jpql.append("     AND da.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("attoammId",attoammId);
		}
		
		if(struttId != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocClasses da ");
			jpql.append("     WHERE da.siacTClass.classifId  = :struttId ");
			jpql.append("     AND da.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("struttId",struttId);
		}
		
		if(docAnno != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocSubdocs ds ");
			jpql.append("     WHERE ds.siacTSubdoc.siacTDoc.docAnno  = :docAnno ");
			jpql.append("     AND ds.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("docAnno",docAnno);
		}
	
		if(!StringUtils.isEmpty(docNumero)){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocSubdocs ds ");
			jpql.append("     WHERE ").append(Utility.toJpqlSearchLike("ds.siacTSubdoc.siacTDoc.docNumero", "CONCAT('%', :docNumero, '%')")).append(" ");
			jpql.append("     AND ds.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("docNumero",docNumero);
		}
		
		if(docTipoId != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocSubdocs ds ");
			jpql.append("     WHERE ds.siacTSubdoc.siacTDoc.siacDDocTipo.docTipoId  = :docTipoId ");
			jpql.append("     AND ds.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("docTipoId",docTipoId);
		}
		
		if(Boolean.TRUE.equals(contoCorrenteMancante)){
			jpql.append(" AND NOT EXISTS( ");
			jpql.append("     FROM d.siacRPredocClasses dacc ");
			jpql.append("     WHERE dacc.siacTClass.siacDClassTipo.classifTipoCode = 'CBPI'");
			jpql.append("     AND dacc.dataCancellazione IS NULL ");
			jpql.append(" )");
		} else if(contoCorrenteId != null && contoCorrenteId.intValue() != 0){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM d.siacRPredocClasses rpc ");
			jpql.append("     WHERE rpc.siacTClass.classifId  = :contoCorrenteId ");
			jpql.append("     AND rpc.dataCancellazione IS NULL ");
			jpql.append("     AND rpc.siacTClass.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("contoCorrenteId",contoCorrenteId);
		}
		
		// SIAC-4620
		if(Boolean.TRUE.equals(nonAnnullati)) {
			jpql.append(" AND NOT EXISTS ( ");
			jpql.append("     FROM d.siacRPredocStatos ds ");
			jpql.append("     WHERE ds.siacDPredocStato.predocStatoCode = :predocStatoCodeAnnullato ");
			jpql.append("     AND ds.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("predocStatoCodeAnnullato", SiacDPredocStatoEnum.Annullato.getCodice());
		}
		
		// SIAC-4772
		if(ordAnno != null && ordNumero != null) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM SiacTSubdoc ts, SiacRSubdocOrdinativoT rsot, SiacTOrdinativo tor, d.siacRPredocSubdocs rps ")
				.append("     WHERE ts = rps.siacTSubdoc ")
				.append("     AND ts = rsot.siacTSubdoc ")
				.append("     AND tor = rsot.siacTOrdinativoT.siacTOrdinativo")
				.append("     AND rps.dataCancellazione IS NULL ")
				.append("     AND rsot.dataCancellazione IS NULL ")
				.append("     AND ts.dataCancellazione IS NULL ")
				.append("     AND tor.ordAnno = :ordAnno ")
				.append("     AND tor.ordNumero = :ordNumero ")
				.append(" ) ");
			param.put("ordAnno", ordAnno);
			param.put("ordNumero", ordNumero);
		}
		
		// SIAC-5001 (precedenza all'uid)
		if(eldocId != null) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM d.siacRElencoDocPredocs redp ")
				.append("     WHERE redp.dataCancellazione IS NULL ")
				.append("     AND redp.siacTElencoDoc.dataCancellazione IS NULL ")
				.append("     AND redp.siacTElencoDoc.eldocId = :eldocId ")
				.append(" ) ");
			param.put("eldocId", eldocId);
		} else if (eldocAnno != null && eldocNumero != null) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM d.siacRElencoDocPredocs redp ")
				.append("     WHERE redp.dataCancellazione IS NULL ")
				.append("     AND redp.siacTElencoDoc.dataCancellazione IS NULL ")
				.append("     AND redp.siacTElencoDoc.eldocAnno = :eldocAnno ")
				.append("     AND redp.siacTElencoDoc.eldocNumero = :eldocNumero ")
				.append(" ) ");
			param.put("eldocAnno", eldocAnno);
			param.put("eldocNumero", eldocNumero);
		}
		
	}

	@Override
	public List<SiacTPredoc> findBySubdocId(Integer subdocId) {
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT DISTINCT rps.siacTPredoc ")
			.append(" FROM SiacRPredocSubdoc rps ")
			.append(" WHERE rps.dataCancellazione IS NULL ")
			.append(" AND rps.siacTSubdoc.subdocId = :subdocId ");
		param.put("subdocId", subdocId);
		
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<SiacTPredoc> res = query.getResultList();
		return res;
	}
	
	private void componiQueryPredocByEldocIdMovgestIdMovgestTsIdPredocStatos(StringBuilder jpql, Map<String, Object> param, Integer eldocId, Integer movgestId, Integer movgestTsId, Collection<String> predocStatoCodes) {
		jpql.append(" FROM SiacRPredocStato rps ")
			.append(" WHERE rps.dataCancellazione IS NULL ")
			.append(" AND rps.siacDPredocStato.predocStatoCode IN (:predocStatoCodes) ")
			.append(" AND EXISTS ( ")
			.append(    " FROM SiacRElencoDocPredoc redp ")
			.append(    " WHERE redp.dataCancellazione IS NULL ")
			.append(    " AND redp.siacTPredoc = rps.siacTPredoc ")
			.append(    " AND redp.siacTElencoDoc.eldocId = :eldocId ")
			.append(" ) ");
		param.put("eldocId", eldocId);
		param.put("predocStatoCodes", predocStatoCodes);
		
		if(movgestTsId != null) {
			jpql.append(" AND EXISTS ( ")
				.append(    " FROM SiacRPredocMovgestT rpmt ")
				.append(    " WHERE rpmt.dataCancellazione IS NULL ")
				.append(    " AND rpmt.siacTMovgestT.movgestTsId = :movgestTsId ")
				.append(" ) ");
			param.put("movgestTsId", movgestTsId);
		} else if (movgestId != null) {
			jpql.append(" AND EXISTS ( ")
				.append(    " FROM SiacRPredocMovgestT rpmt ")
				.append(    " WHERE rpmt.dataCancellazione IS NULL ")
				.append(    " AND rpmt.siacTMovgestT.siacTMovgest.movgestId = :movgestId ")
				.append(" ) ");
			param.put("movgestId", movgestId);
		}
	}

	@Override
	public Map<String, BigDecimal> findImportiByEldocIdAndPredocStato(Integer eldocId, Integer movgestId, Integer movgestTsId, Collection<String> predocStatoCodes) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT rps.siacDPredocStato.predocStatoCode, COALESCE(SUM(rps.siacTPredoc.predocImporto), 0) ");
		componiQueryPredocByEldocIdMovgestIdMovgestTsIdPredocStatos(jpql, param, eldocId, movgestId, movgestTsId, predocStatoCodes);
		jpql.append(" GROUP BY rps.siacDPredocStato.predocStatoCode ");
		
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		
		Map<String, BigDecimal> res = new HashMap<String, BigDecimal>();
		
		for(Object[] line : resultList) {
			res.put((String)line[0], (BigDecimal)line[1]);
		}
		
		return res;
	}

	@Override
	public Map<String, Long> countByEldocIdAndPredocStato(Integer eldocId, Integer movgestId, Integer movgestTsId, Collection<String> predocStatoCodes) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT rps.siacDPredocStato.predocStatoCode, COALESCE(COUNT(rps.siacTPredoc), 0) ");
		componiQueryPredocByEldocIdMovgestIdMovgestTsIdPredocStatos(jpql, param, eldocId, movgestId, movgestTsId, predocStatoCodes);
		jpql.append(" GROUP BY rps.siacDPredocStato.predocStatoCode ");
		
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		
		Map<String, Long> res = new HashMap<String, Long>();
		
		for(Object[] line : resultList) {
			res.put((String)line[0], (Long)line[1]);
		}
		
		return res;
	}

	@Override
	public List<SiacTPredoc> findByEldocIdAndPredocStato(Integer eldocId, Integer movgestId, Integer movgestTsId, Collection<String> predocStatoCodes) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT rps.siacTPredoc ");
		componiQueryPredocByEldocIdMovgestIdMovgestTsIdPredocStatos(jpql, param, eldocId, movgestId, movgestTsId, predocStatoCodes);
		
		TypedQuery<SiacTPredoc> query = createQuery(jpql.toString(), param, SiacTPredoc.class);
		return query.getResultList();
	}

	@Override
	public void associaMovgestByEldocIdAndPredocStato(SiacTPredoc template, Integer eldocId, Integer movgestId, Integer movgestTsId, Collection<String> predocStatoCodes) {
		final String methodName = "associaMovgestByEldocIdAndPredocStato";
		final Date now = new Date();
		final SiacTMovgestT siacTMovgestT = estraiSiacTMovgestT(template);
		final SiacDPredocStato siacDPredocStato = estraiSiacDPredocStato(template);
		
		List<SiacTPredoc> siacTPredocs = findByEldocIdAndPredocStato(eldocId, null, null, predocStatoCodes);
		
		for(SiacTPredoc siacTPredoc : siacTPredocs) {
			// Cancello i vecchi collegamenti
			for(SiacRPredocMovgestT srpmt : siacTPredoc.getSiacRPredocMovgestTs()) {
				srpmt.setDataCancellazioneIfNotSet(now);
			}
			for(SiacRPredocStato srps : siacTPredoc.getSiacRPredocStatos()) {
				srps.setDataCancellazioneIfNotSet(now);
			}
			
			SiacRPredocMovgestT rpmt = new SiacRPredocMovgestT();
			rpmt.setDataModificaInserimento(now);
			rpmt.setSiacTPredoc(siacTPredoc);
			rpmt.setSiacTEnteProprietario(siacTPredoc.getSiacTEnteProprietario());
			rpmt.setSiacTMovgestT(siacTMovgestT);
			rpmt.setLoginOperazione(template.getLoginOperazione());
			
			SiacRPredocStato rps = new SiacRPredocStato();
			rps.setDataModificaInserimento(now);
			rps.setSiacTPredoc(siacTPredoc);
			rps.setSiacTEnteProprietario(siacTPredoc.getSiacTEnteProprietario());
			rps.setSiacDPredocStato(siacDPredocStato);
			rps.setLoginOperazione(template.getLoginOperazione());
			
			siacTPredoc.getSiacRPredocMovgestTs().add(rpmt);
			siacTPredoc.getSiacRPredocStatos().add(rps);
			
			log.debug(methodName, "Associato movimento di gestione al predoc " + siacTPredoc.getPredocId() + " e aggiornato stato");
		}
	}
	
	private SiacTMovgestT estraiSiacTMovgestT(SiacTPredoc stp) {
		return stp.getSiacRPredocMovgestTs() != null && !stp.getSiacRPredocMovgestTs().isEmpty() ? stp.getSiacRPredocMovgestTs().get(0).getSiacTMovgestT() : null;
	}

	private SiacDPredocStato estraiSiacDPredocStato(SiacTPredoc stp) {
		return stp.getSiacRPredocStatos() != null && !stp.getSiacRPredocStatos().isEmpty() ? stp.getSiacRPredocStatos().get(0).getSiacDPredocStato() : null;
	}
	
	private SiacTAttoAmm estraiSiacTAttoAmm(SiacTPredoc stp) {
		return stp.getSiacRPredocAttoAmms() != null && !stp.getSiacRPredocAttoAmms().isEmpty() ? stp.getSiacRPredocAttoAmms().get(0).getSiacTAttoAmm() : null;
	}
	
	private SiacTSoggetto estraiSiacTSoggetto(SiacTPredoc stp) {
		return stp.getSiacRPredocSogs() != null && !stp.getSiacRPredocSogs().isEmpty() ? stp.getSiacRPredocSogs().get(0).getSiacTSoggetto() : null;
	}

	@Override
	public List<SiacTPredoc> associaMovgestSoggettoAttoAmmByDataCompetenzaCausIdAndPredocStato(SiacTPredoc template, Integer enteProprietarioId, Integer causId, Date dataCompetenzaDa, Date dataCompetenzaA, Collection<String> predocStatoCodes) {
		final String methodName = "associaMovgestSoggettoAttoAmmByDataCompetenzaCausIdAndPredocStato";
		final Date now = new Date();
		
		final SiacTMovgestT siacTMovgestT = estraiSiacTMovgestT(template);
		final SiacDPredocStato siacDPredocStato = estraiSiacDPredocStato(template);
		final SiacTAttoAmm siacTAttoAmm = estraiSiacTAttoAmm(template);
		final SiacTSoggetto siacTSoggetto = estraiSiacTSoggetto(template);
		
		//List<SiacTPredoc> siacTPredocs = findByEldocIdAndPredocStato(eldocId, null, null, predocStatoCodes);
		List<SiacTPredoc> siacTPredocs = findByEnteCausIdDataCompetenzaDaAPredocStato(enteProprietarioId, template.getSiacDDocFamTipo().getDocFamTipoCode(), causId, dataCompetenzaDa, dataCompetenzaA, predocStatoCodes);
		
		for(SiacTPredoc siacTPredoc : siacTPredocs) {
			// Cancello i vecchi collegamenti
			setDataCancellazione(siacTPredoc.getSiacRPredocMovgestTs(), now);
			setDataCancellazione(siacTPredoc.getSiacRPredocStatos(), now);
			setDataCancellazione(siacTPredoc.getSiacRPredocSogs(), now);
			
			// Crea nuovi legami
			SiacRPredocMovgestT rpmt = new SiacRPredocMovgestT();
			rpmt.setDataModificaInserimento(now);
			rpmt.setSiacTPredoc(siacTPredoc);
			rpmt.setSiacTEnteProprietario(siacTPredoc.getSiacTEnteProprietario());
			rpmt.setSiacTMovgestT(siacTMovgestT);
			rpmt.setLoginOperazione(template.getLoginOperazione());
			
			SiacRPredocStato rps = new SiacRPredocStato();
			rps.setDataModificaInserimento(now);
			rps.setSiacTPredoc(siacTPredoc);
			rps.setSiacTEnteProprietario(siacTPredoc.getSiacTEnteProprietario());
			rps.setSiacDPredocStato(siacDPredocStato);
			rps.setLoginOperazione(template.getLoginOperazione());
			
			SiacRPredocSog rpso = new SiacRPredocSog();
			rpso.setDataModificaInserimento(now);
			rpso.setSiacTPredoc(siacTPredoc);
			rpso.setSiacTEnteProprietario(siacTPredoc.getSiacTEnteProprietario());
			rpso.setSiacTSoggetto(siacTSoggetto);
			rpso.setLoginOperazione(template.getLoginOperazione());
			
			siacTPredoc.getSiacRPredocMovgestTs().add(rpmt);
			siacTPredoc.getSiacRPredocStatos().add(rps);
			siacTPredoc.getSiacRPredocSogs().add(rpso);
			
			if(siacTAttoAmm != null) {
				// Lego solo se presente
				
				// Cancello i vecchi dati
				setDataCancellazione(siacTPredoc.getSiacRPredocAttoAmms(), now);
				
				// Creo la nuova r
				SiacRPredocAttoAmm rpaa = new SiacRPredocAttoAmm();
				rpaa.setDataModificaInserimento(now);
				rpaa.setSiacTPredoc(siacTPredoc);
				rpaa.setSiacTEnteProprietario(siacTPredoc.getSiacTEnteProprietario());
				rpaa.setSiacTAttoAmm(siacTAttoAmm);
				rpaa.setLoginOperazione(template.getLoginOperazione());
				
				// Lego la r
				siacTPredoc.getSiacRPredocAttoAmms().add(rpaa);
			}
			
			log.debug(methodName, "Associato movimento di gestione al predoc " + siacTPredoc.getPredocId() + " e aggiornato stato");
		}
		
		return siacTPredocs;
	}

	@Override
	public List<SiacTPredoc> findByEnteCausIdDataCompetenzaDaAPredocStato(Integer enteProprietarioId, String docFamTipoCode,
			Integer causId, Date dataCompetenzaDa, Date dataCompetenzaA, Collection<String> predocStatoCodes) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacTPredoc tp ");
		jpql.append(" WHERE tp.dataCancellazione IS NULL ");
		jpql.append(" AND tp.siacDDocFamTipo.docFamTipoCode = :docFamTipoCode ");
		jpql.append(" AND tp.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		
		params.put("docFamTipoCode", docFamTipoCode);
		params.put("enteProprietarioId", enteProprietarioId);
		
		if(causId != null && causId.intValue() != 0) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM tp.siacRPredocCausales rpc ");
			jpql.append("     WHERE rpc.dataCancellazione IS NULL ");
			jpql.append("     AND rpc.siacDCausale.causId = :causId ");
			jpql.append(" ) ");
			params.put("causId", causId);
		}
		if(dataCompetenzaDa != null) {
			jpql.append(" AND tp.predocDataCompetenza >= :dataCompetenzaDa ");
			params.put("dataCompetenzaDa", dataCompetenzaDa);
		}
		if(dataCompetenzaA != null) {
			jpql.append(" AND tp.predocDataCompetenza <= :dataCompetenzaA ");
			params.put("dataCompetenzaA", dataCompetenzaA);
		}
		if(predocStatoCodes != null && !predocStatoCodes.isEmpty()) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM tp.siacRPredocStatos rps ");
			jpql.append("     WHERE rps.dataCancellazione IS NULL ");
			jpql.append("     AND rps.siacDPredocStato.predocStatoCode IN (:predocStatoCodes) ");
			jpql.append(" ) ");
			params.put("predocStatoCodes", predocStatoCodes);
		}
		
		TypedQuery<SiacTPredoc> query = createQuery(jpql.toString(), params, SiacTPredoc.class);
		return query.getResultList();
	}

	@Override
	public Map<String, BigDecimal> findImportiByPredocDataCompetenzaDaAAndCausaleEpAndPredocStato(Integer enteProprietarioId, String docFamTipoCode, Date predocDataCompetenzaDa, Date predocDataCompetenzaA, Integer causId, List<String> predocStatoCodes) {
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT rps.siacDPredocStato.predocStatoCode, COALESCE(SUM(rps.siacTPredoc.predocImporto), 0) ");
		componiQueryPredocByPredocDataCompetenzaDaACausIdPredocStatos(jpql, param, enteProprietarioId, docFamTipoCode, predocDataCompetenzaDa, predocDataCompetenzaA, causId, predocStatoCodes);
		jpql.append(" GROUP BY rps.siacDPredocStato.predocStatoCode ");
		
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		
		Map<String, BigDecimal> res = new HashMap<String, BigDecimal>();
		
		for(Object[] line : resultList) {
			res.put((String)line[0], (BigDecimal)line[1]);
		}
		return res;
	}

	@Override
	public Map<String, Long> countByPredocDataCompetenzaDaAAndCausaleEpAndPredocStato(Integer enteProprietarioId, String docFamTipoCode, Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA, Integer causId, List<String> predocStatoCodes) {
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT rps.siacDPredocStato.predocStatoCode, COALESCE(COUNT(rps.siacTPredoc), 0) ");
		componiQueryPredocByPredocDataCompetenzaDaACausIdPredocStatos(jpql, param, enteProprietarioId, docFamTipoCode, predocDataCompetenzaDa, predocDataCompetenzaA, causId, predocStatoCodes);
		jpql.append(" GROUP BY rps.siacDPredocStato.predocStatoCode ");
		
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		
		Map<String, Long> res = new HashMap<String, Long>();
		
		for(Object[] line : resultList) {
			res.put((String)line[0], (Long)line[1]);
		}
		return res;
	}
	
	private void componiQueryPredocByPredocDataCompetenzaDaACausIdPredocStatos(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId, String docFamTipoCode,
			Date predocDataCompetenzaDa, Date predocDataCompetenzaA, Integer causId, Collection<String> predocStatoCodes) {
		jpql.append(" FROM SiacRPredocStato rps ")
			.append(" WHERE rps.dataCancellazione IS NULL ")
			.append(" AND rps.siacDPredocStato.predocStatoCode IN (:predocStatoCodes) ")
			.append(" AND rps.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
			.append(" AND rps.siacTPredoc.siacDDocFamTipo.docFamTipoCode = :docFamTipoCode ");
		param.put("predocStatoCodes", predocStatoCodes);
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("docFamTipoCode", docFamTipoCode);
		
		if(causId != null && causId.intValue() != 0) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM SiacRPredocCausale rpc ");
			jpql.append("     WHERE rpc.dataCancellazione IS NULL ");
			jpql.append("     AND rpc.siacTPredoc = rps.siacTPredoc ");
			jpql.append("     AND rpc.siacDCausale.causId = :causId ");
			jpql.append(" ) ");
			param.put("causId", causId);
		}
		if(predocDataCompetenzaDa != null) {
			jpql.append(" AND rps.siacTPredoc.predocDataCompetenza >= :predocDataCompetenzaDa ");
			param.put("predocDataCompetenzaDa", predocDataCompetenzaDa);
		}
		if(predocDataCompetenzaA != null) {
			jpql.append(" AND rps.siacTPredoc.predocDataCompetenza <= :predocDataCompetenzaA ");
			param.put("predocDataCompetenzaA", predocDataCompetenzaA);
		}
	}
	
}
