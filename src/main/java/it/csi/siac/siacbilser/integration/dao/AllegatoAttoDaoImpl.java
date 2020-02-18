/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoSog;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAllegatoStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class AllegatoAttoDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AllegatoAttoDaoImpl extends JpaDao<SiacTAttoAllegato, Integer> implements AllegatoAttoDao {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.AllegatoAttoDao#create(it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato)
	 */
	public SiacTAttoAllegato create(SiacTAttoAllegato e){
		
		Date now = new Date();
		e.setDataModificaInserimento(now);
		
		if(e.getSiacRAttoAllegatoSogs()!=null){
			for(SiacRAttoAllegatoSog r : e.getSiacRAttoAllegatoSogs()){
				r.setDataModificaInserimento(now);
			}
		}

		if(e.getSiacRAttoAllegatoStatos()!=null){
			for(SiacRAttoAllegatoStato r : e.getSiacRAttoAllegatoStatos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRAttoAllegatoElencoDocs()!=null){
			for(SiacRAttoAllegatoElencoDoc r : e.getSiacRAttoAllegatoElencoDocs()){
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
	public SiacTAttoAllegato update(SiacTAttoAllegato e){
		SiacTAttoAllegato eAttuale = this.findById(e.getUid());
		
		Date now = new Date();
		e.setDataModificaAggiornamento(now);		
		
		//cancellazione elementi collegati
		if(eAttuale.getSiacRAttoAllegatoSogs()!=null){
			for(SiacRAttoAllegatoSog r : eAttuale.getSiacRAttoAllegatoSogs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}

		if(eAttuale.getSiacRAttoAllegatoStatos()!=null){
			for(SiacRAttoAllegatoStato r : eAttuale.getSiacRAttoAllegatoStatos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(eAttuale.getSiacRAttoAllegatoElencoDocs()!=null){
			for(SiacRAttoAllegatoElencoDoc r : eAttuale.getSiacRAttoAllegatoElencoDocs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		
		//inserimento elementi nuovi		
		if(e.getSiacRAttoAllegatoSogs()!=null){
			for(SiacRAttoAllegatoSog r : e.getSiacRAttoAllegatoSogs()){
				r.setDataModificaInserimento(now);
			}
		}

		if(e.getSiacRAttoAllegatoStatos()!=null){
			for(SiacRAttoAllegatoStato r : e.getSiacRAttoAllegatoStatos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRAttoAllegatoElencoDocs()!=null) {
			for(SiacRAttoAllegatoElencoDoc r : e.getSiacRAttoAllegatoElencoDocs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		
		
		super.update(e);
		return e;
	}
	
	@Override
	public Page<SiacTAttoAllegato> ricercaSinteticaAllegatoAtto(Integer enteProprietarioId, String causale,
			SiacDAttoAllegatoStatoEnum siacDAttoAllegatoStatoEnum, Date scadenzaDa, Date scadenzaA, List<Integer> attoAmministrativoId,
			Integer soggettoId,Integer movgestId,Integer movgestTsId,
			Integer elencoId, Boolean attoalFlagRitenute, List<SiacDAttoAllegatoStatoEnum> siacDAttoAllegatoStatoEnums, Integer bilAnno, Boolean hasImpegnoConfermaDurc, Pageable pageable) {
		final String methodName = "ricercaSinteticaAllegatoAtto";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaAllegatoAtto(jpql, param,
				enteProprietarioId,
				causale,
				siacDAttoAllegatoStatoEnum,
				scadenzaDa,
				scadenzaA,
				attoAmministrativoId,
				soggettoId,
				movgestId,
				movgestTsId,
				elencoId,
				attoalFlagRitenute,
				siacDAttoAllegatoStatoEnums,
				bilAnno,
				hasImpegnoConfermaDurc);
		
		// Elenco ordinato per le colonne Data scadenza  e Anno-numero Atto in ordine decrescente
		jpql.append(" ORDER BY d.attoalDataScadenza DESC, d.siacTAttoAmm.attoammAnno DESC, d.siacTAttoAmm.attoammNumero DESC ");

		log.info(methodName, "attoAmministrativoId size "+ attoAmministrativoId.size());

		for (Integer s :attoAmministrativoId){
			log.info(methodName, "lista id attoAmministrativoId "+ s.intValue());
		}
		
		log.info(methodName, "JPQL ricercaSinteticaAllegatoAtto to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	/*public BigDecimal ricercaSinteticaAllegatoAttoImportoTotale(Integer enteProprietarioId, SiacDDocFamTipoEnum docFamTipoCode,
			Integer docAnno, 
			String docNumero, 
			Date docDataEmissione,
			Integer docTipoId, 
			SiacDDocStatoEnum docStato,
			SiacDDocStatoEnum docStatoToExclude, 
			Boolean flagRilevanteIva,
			
			Impegno impegno, // movimento
			Accertamento accertamento,
			AttoAmministrativo attoAmministrativo, // provvedimento
			Soggetto soggetto,
						
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaAllegatoAttoImportoTotale";
		
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT SUM(d.docImporto) ");
		
		componiQueryRicercaSinteticaAllegatoAtto( jpql, param, enteProprietarioId, docFamTipoCode, docAnno, docNumero, docDataEmissione, docTipoId, docStato, docStatoToExclude,
				flagRilevanteIva, impegno, accertamento, attoAmministrativo, soggetto);
		
		Query query = createQuery(jpql.toString(), param);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		log.info(methodName, "returning: "+result);
		
		return result;
	}*/
	
	

	/**
	 * Componi query ricerca sintetica allegato atto.
	 * 
	 * @param jpql
	 * @param param
	 * @param enteProprietarioId
	 * @param attoalCausale
	 * @param siacDAttoAllegatoStatoEnum
	 * @param scadenzaDa
	 * @param scadenzaA
	 * @param attoammId
	 * @param soggettoId
	 * @param movgestId
	 * @param movgestTsId
	 * @param eldocId
	 * @param attoalFlagRitenute
	 * @param siacDAttoAllegatoStatoEnums 
	 * @param hasImpegnoConfermaDurc 
	 */
	private void componiQueryRicercaSinteticaAllegatoAtto(StringBuilder jpql, Map<String, Object> param,
			Integer enteProprietarioId, String attoalCausale, SiacDAttoAllegatoStatoEnum siacDAttoAllegatoStatoEnum, Date scadenzaDa, Date scadenzaA,
			List<Integer> attoammId, Integer soggettoId,Integer movgestId,Integer movgestTsId, Integer eldocId, Boolean attoalFlagRitenute,
			List<SiacDAttoAllegatoStatoEnum> siacDAttoAllegatoStatoEnums, Integer bilAnno, Boolean hasImpegnoConfermaDurc) {
		
		jpql.append("FROM SiacTAttoAllegato d ")
			.append(" WHERE ")
			.append(" d.dataCancellazione IS NULL ")
			.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		// Causale
		if(!StringUtils.isEmpty(attoalCausale)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.attoalCausale", "CONCAT('%', :attoalCausale, '%')") + " ");
			param.put("attoalCausale", attoalCausale);			
		}
		// Stato
		impostaFiltroStato(jpql, param, siacDAttoAllegatoStatoEnum, siacDAttoAllegatoStatoEnums);
		// Data scadenza (se entrambe, compreso nei valori. Altrimenti puntuale)
		if(scadenzaDa != null || scadenzaA != null) {
			Date attoalDataScadenzaStart = null;
			Date attoalDataScadenzaEnd = null;
			if(scadenzaDa != null && scadenzaA != null) {
				attoalDataScadenzaStart = scadenzaDa;
				attoalDataScadenzaEnd = scadenzaA;
			} else {
				Date dataScadenza = scadenzaDa != null ? scadenzaDa : scadenzaA;
				// Costruisco il bordo della data selezionata
				// Inizio: floor
				attoalDataScadenzaStart = DateUtils.truncate(dataScadenza, Calendar.DAY_OF_MONTH);
				// Fine: ceiling - 1ms
				attoalDataScadenzaEnd = DateUtils.addMilliseconds(DateUtils.ceiling(dataScadenza, Calendar.DAY_OF_MONTH), -1);
			}
			jpql.append(" AND d.attoalDataScadenza BETWEEN :attoalDataScadenzaStart AND :attoalDataScadenzaEnd ");
			param.put("attoalDataScadenzaStart", attoalDataScadenzaStart);
			param.put("attoalDataScadenzaEnd", attoalDataScadenzaEnd);
		}
		// Atto amministrativo
		if(attoammId != null && !attoammId.isEmpty()) {
			jpql.append(" AND d.siacTAttoAmm.attoammId in( :attoammId )");
			param.put("attoammId", attoammId);
		}
		
		/*
		// Soggetto: si estraggono gli allegati collegati ad un soggetto in corso di sospensione (datiSoggettoAllegato.dataRiattivazione non valorizzata)
		if(soggettoId != null && soggettoId.intValue() != 0) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM d.siacRAttoAllegatoSogs sraasog ")
				.append("     WHERE sraasog.siacTSoggetto.soggettoId = :soggettoId ")
				.append("     AND sraasog.attoalSogDataSosp IS NOT NULL ")
				.append("     AND sraasog.attoalSogDataRiatt IS NULL ")
				.append("     AND sraasog.dataCancellazione IS NULL ")
				.append(" ) ");
			param.put("soggettoId", soggettoId);
		}
*/
		// Soggetto: si estraggono gli allegati collegati ad un soggetto )
		if(isValidInteger(soggettoId)) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM d.siacRAttoAllegatoElencoDocs raaed, SiacRElencoDocSubdoc reds, SiacTSubdoc ts, SiacRDocSog rds ")
				.append("     WHERE raaed.siacTElencoDoc = reds.siacTElencoDoc ")
				.append("     AND reds.siacTSubdoc = ts ")
				.append("     AND ts.siacTDoc = rds.siacTDoc ")
				.append("     AND rds.siacTSoggetto.soggettoId = :soggettoId ")
				.append("     AND raaed.dataCancellazione IS NULL ")
				.append("     AND reds.dataCancellazione IS NULL ")
				.append("     AND rds.dataCancellazione IS NULL ")
				.append("     AND rds.siacTSoggetto.dataCancellazione IS NULL ")
				.append(" ) ");
			param.put("soggettoId", soggettoId);
		}

		// Impegno: si estraggono gli allegati collegati ad un impegno specifico
		if(isValidInteger(movgestId) || isValidInteger(movgestTsId)) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM d.siacRAttoAllegatoElencoDocs raaed, SiacRElencoDocSubdoc reds, SiacRSubdocMovgestT rsmt ")
				// Condizioni di JOIN
				.append(" WHERE raaed.siacTElencoDoc = reds.siacTElencoDoc ")
				.append("     AND reds.siacTSubdoc = rsmt.siacTSubdoc ")
				// Date cancellazione
				.append("     AND raaed.dataCancellazione IS NULL ")
				.append("     AND reds.dataCancellazione IS NULL ")
				.append("     AND rsmt.dataCancellazione IS NULL ")
				.append("     AND rsmt.siacTMovgestT.dataCancellazione IS NULL ")
				.append("     AND rsmt.siacTMovgestT.siacTMovgest.dataCancellazione IS NULL ");
			if(isValidInteger(movgestTsId)) {
				jpql.append("     AND rsmt.siacTMovgestT.movgestTsId = :movgestTsId ");
				param.put("movgestTsId", movgestTsId);
			} else {
				jpql.append("     AND rsmt.siacTMovgestT.siacTMovgest.movgestId = :movgestId ");
				param.put("movgestId", movgestId);
			}
			jpql.append(" ) ");
			
		}

		
		// Elenco
		if(isValidInteger(eldocId)) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM d.siacRAttoAllegatoElencoDocs sraaed ")
				.append("     WHERE sraaed.siacTElencoDoc.eldocId = :eldocId ")
				.append("     AND sraaed.dataCancellazione IS NULL ")
				.append(" ) ");
			param.put("eldocId", eldocId);
		}
		// Flag ritenute
		if(attoalFlagRitenute != null) {
			jpql.append(" AND d.attoalFlagRitenute = :attoalFlagRitenute ");
			param.put("attoalFlagRitenute", attoalFlagRitenute);
		}
		
		// SIAC-6166
		if(bilAnno != null) {
			jpql.append(" AND EXISTS ( ")
				.append("    FROM SiacRAttoAllegatoElencoDoc raaed, SiacRElencoDocSubdoc reds, SiacRSubdocMovgestT rsmt ")
				.append("    WHERE raaed.siacTAttoAllegato = d ")
				.append("    AND reds.siacTElencoDoc = raaed.siacTElencoDoc ")
				.append("    AND rsmt.siacTSubdoc = reds.siacTSubdoc ")
				.append("    AND raaed.dataCancellazione IS NULL ")
				.append("    AND reds.dataCancellazione IS NULL ")
				.append("    AND rsmt.dataCancellazione IS NULL ")
				.append("    AND rsmt.siacTMovgestT.siacTMovgest.siacTBil.siacTPeriodo.anno = :annoBilancio ")
				.append(" ) ");
			param.put("annoBilancio", bilAnno.toString());
			
		}
		
		if(hasImpegnoConfermaDurc != null) {
			jpql.append(" AND ")
				.append(hasImpegnoConfermaDurc ? "" : " NOT ")
				.append(" EXISTS ( FROM SiacRAttoAllegatoElencoDoc raaed, SiacRElencoDocSubdoc reds , SiacRSubdocMovgestT rsmt, SiacRMovgestTsAttr rma ")
			    .append(" WHERE reds.dataCancellazione IS NULL ")
			    .append(" AND raaed.dataCancellazione IS NULL ")
			    .append(" AND rsmt.dataCancellazione IS NULL ")
			    .append(" AND rma.dataCancellazione IS NULL ")
			    .append(" AND raaed.siacTAttoAllegato = d ")
			    .append(" AND raaed.siacTElencoDoc = reds.siacTElencoDoc ")
			    .append(" AND reds.siacTElencoDoc.dataCancellazione IS NULL  ")
			    .append(" AND rsmt.siacTSubdoc = reds.siacTSubdoc ")
			    .append(" AND rma.siacTMovgestT = rsmt.siacTMovgestT ")
			    .append(" AND rsmt.siacTSubdoc.dataCancellazione IS NULL ")
			    .append(" AND rma.siacTAttr.attrCode = :impegnoConfermaDurcAttrCode  " )
			    .append(" AND rma.boolean_ = 'S' ")
			    .append(" ) ");
			param.put("impegnoConfermaDurcAttrCode", SiacTAttrEnum.FlagSoggettoDurc.getCodice());
			//param.put("booleanValue", new BooleanToStringConverter().convertTo(hasImpegnoConfermaDurc));
		}
	}

	/**
	 * Imposta filtrostato.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param siacDAttoAllegatoStatoEnum the siac D atto allegato stato enum
	 * @param siacDAttoAllegatoStatoEnums the siac D atto allegato stato enums
	 */
	private void impostaFiltroStato(StringBuilder jpql, Map<String, Object> param,SiacDAttoAllegatoStatoEnum siacDAttoAllegatoStatoEnum,List<SiacDAttoAllegatoStatoEnum> siacDAttoAllegatoStatoEnums) {
		if(siacDAttoAllegatoStatoEnum == null && (siacDAttoAllegatoStatoEnums == null || siacDAttoAllegatoStatoEnums.isEmpty())) {
			return;
		}
		List<SiacDAttoAllegatoStatoEnum> statoEnumsDaCiclare = siacDAttoAllegatoStatoEnum != null? Arrays.asList(siacDAttoAllegatoStatoEnum) : siacDAttoAllegatoStatoEnums;
		List<String> attoAlStatoCodes = new ArrayList<String>();
		for(SiacDAttoAllegatoStatoEnum stati : statoEnumsDaCiclare) {
			attoAlStatoCodes.add(stati.getCodice());
		}
		
		jpql.append(" AND EXISTS ( ")
		.append("     FROM d.siacRAttoAllegatoStatos sraas ")
		.append("     WHERE sraas.siacDAttoAllegatoStato.attoalStatoCode IN (:attoalStatoCodes) ")
		.append("     AND sraas.dataCancellazione IS NULL ")
		.append(" ) ");
		param.put("attoalStatoCodes",attoAlStatoCodes );
	}
	
	private boolean isValidInteger(Integer i) {
		return i != null && i.intValue() != 0;
	}
}
