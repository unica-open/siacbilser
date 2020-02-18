/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.ArrayList;
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
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleEpPdceConto;
import it.csi.siac.siacbilser.integration.entity.SiacRPdceConto;
import it.csi.siac.siacbilser.integration.entity.SiacRPdceContoAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRPdceContoClass;
import it.csi.siac.siacbilser.integration.entity.SiacRPdceContoSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;

/**
 * The Class ContoDaoImpl.
 * 
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContoDaoImpl extends JpaDao<SiacTPdceConto, Integer> implements ContoDao {
	
	public SiacTPdceConto create(SiacTPdceConto e){
		Date now = new Date();
		Date dataInizioValidita = e.getDataInizioValidita();
		
		e.setDataModificaInserimento(now, dataInizioValidita);
		
		setDataModificaInserimento(e, now, dataInizioValidita);
		
		
		e.setUid(null);		
		super.save(e);
		return e;
	}


	public SiacTPdceConto update(SiacTPdceConto e){
		
		SiacTPdceConto eAttuale = this.findById(e.getUid());
		
		Date now = new Date();
		Date dataInizioValidita = e.getDataInizioValidita();

		e.setDataModifica(now);
		
		//e.setDataInizioValidita(dataInizioValidita); //non si puo' aggiornare in quanto dataCreazione 
		                                               //ha attributo updatable a false e va bene così perchè in analisi non si vuole aggiornare
		//e.setDataCreazione(eAttuale.getDataCreazione()); //non necessario in quanto dataCreazione ha updatable a false
		
		
		//cancellazione elementi collegati	
		setDataFineValiditaEDataCancellazioneSeNelPeriodoDiValidita(eAttuale, now, dataInizioValidita);
		
		entityManager.flush();
		
		setDataModificaInserimento(e, now, dataInizioValidita);
		
		super.update(e);
		return e;
	}
	
	
	@Override
	public void annulla(SiacTPdceConto entity) {
		
		SiacTPdceConto eAttuale = this.findById(entity.getUid());
		
		Date now = new Date();
		Date dataFineValidiata = Utility.ultimoGiornoDellAnno(eAttuale.getDataInizioValidita());
		
		eAttuale.setDataFineValiditaIfNotSet(dataFineValidiata);
		setDataFineValiditaEDataCancellazioneSeNelPeriodoDiValidita(eAttuale, now, dataFineValidiata);
		
	}
	
	
	@Override
	public void elimina(SiacTPdceConto entity) {
		
		SiacTPdceConto e = this.findById(entity.getUid());
		Date now = new Date();
		e.setDataCancellazioneIfNotSet(now);
		
		if(e.getSiacRCausaleEpPdceContos() != null){
			for(SiacRCausaleEpPdceConto r: e.getSiacRCausaleEpPdceContos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(e.getSiacRPdceContoAttrs() != null){
			for(SiacRPdceContoAttr r: e.getSiacRPdceContoAttrs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(e.getSiacRPdceContoClasses() != null){
			for(SiacRPdceContoClass r: e.getSiacRPdceContoClasses()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(e.getSiacRPdceContos1() != null){
			for(SiacRPdceConto r: e.getSiacRPdceContos1()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(e.getSiacRPdceContos2() != null){
			for(SiacRPdceConto r: e.getSiacRPdceContos2()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(e.getSiacRPdceContoSoggettos() != null){
			for(SiacRPdceContoSoggetto r: e.getSiacRPdceContoSoggettos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		
	}
	
	/**
	 * Sets the data modifica inserimento.
	 *
	 * @param e the e
	 * @param now the now
	 * @param dataInizioValidita 
	 */
	private void setDataModificaInserimento(SiacTPdceConto e, Date now, Date dataInizioValidita) {
		int annoInizioValidita = Utility.getAnno(dataInizioValidita);
		Date ultimoGiornoDellAnno = Utility.ultimoGiornoDellAnno(dataInizioValidita);
		
		// SIAC-6011
		setDataFineValiditaNewRecordByAnnoBilancio(e, now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, e.getSiacRCausaleEpPdceContos(), "SiacRCausaleEpPdceConto");
		setDataFineValiditaNewRecordByAnnoBilancio(e, now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, e.getSiacRPdceContoAttrs(), "SiacRPdceContoAttr");
		setDataFineValiditaNewRecordByAnnoBilancio(e, now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, e.getSiacRPdceContoClasses(), "SiacRPdceContoClass");
		setDataFineValiditaNewRecordByAnnoBilancio(e, now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, e.getSiacRPdceContos1(), "SiacRPdceConto");
		setDataFineValiditaNewRecordByAnnoBilancio(e, now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, e.getSiacRPdceContos2(), "SiacRPdceConto");
		setDataFineValiditaNewRecordByAnnoBilancio(e, now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, e.getSiacRPdceContoSoggettos(), "SiacRCausaleEpPdceConto");
		
	}


	/**
	 * @param e
	 * @param now
	 * @param dataInizioValidita
	 * @param annoInizioValidita
	 * @param ultimoGiornoDellAnno
	 */
	private <E extends SiacTBase> void setDataFineValiditaNewRecordByAnnoBilancio(SiacTPdceConto e, Date now, Date dataInizioValidita,
			int annoInizioValidita, Date ultimoGiornoDellAnno, List<E> entitas, String entitaName) {
		if(entitas != null){
			//SIAC-6106
			Date validitaInizioMinima = ottieniMinValiditaInizioByNomeEntitaCollegamento(e.getPdceContoId(), dataInizioValidita, entitaName);
			
			for(E entita : entitas){
				entita.setDataModificaInserimento(now, dataInizioValidita);
				entita.setUid(null);
				//SIAC-6106
				Date validitaFine = ottieniValiditaInizioDaImpostareInBaseAlRecordSuccessivo(validitaInizioMinima, ultimoGiornoDellAnno, annoInizioValidita);
				
				if(validitaFine != null) {
					entita.setDataFineValiditaIfNotSet(validitaFine);
				}
			}
		}
	}


	/**
	 * @param uidConto
	 * @param aliasR
	 */
	private Date ottieniMinValiditaInizioByNomeEntitaCollegamento(Integer uidConto, Date dataDiPartenza, String aliasR) {
		final String methodName = "ottieniMinValiditaInizioByNomeEntitaCollegamento";
		if(uidConto == null || uidConto == 0 || StringUtils.isEmpty(aliasR)) {
			return null;
		}
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT MIN( rcoll.dataInizioValidita ) ")
			.append(" FROM " + aliasR  + " rcoll ")
			.append(" WHERE rcoll.siacTPdceConto.pdceContoId = :pdceContoId ")
			.append(" AND rcoll.dataCancellazione IS NULL ")
			.append(" AND rcoll.dataInizioValidita >  :dataInizioValidita ");
		
		param.put("pdceContoId", uidConto);
		param.put("dataInizioValidita", dataDiPartenza);
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		Query query = createQuery(jpql.toString(), param);
		return (Date) query.getSingleResult();
	}
	
	//SIAC-6106
	private Date ottieniValiditaInizioDaImpostareInBaseAlRecordSuccessivo(Date validitaInizioMinima, Date ultimoGiornoDellAnno, int annoInizioValidita){
		if(validitaInizioMinima == null) {
			return null;
		}
		int annoValiditaInizioMinima = Utility.getAnno(validitaInizioMinima);
		Date validitaFineDaImpostare = Utility.ultimoGiornoDellAnnoPrecedente(validitaInizioMinima);
		return annoValiditaInizioMinima > annoInizioValidita ? Utility.dataPiuRecente(validitaFineDaImpostare, ultimoGiornoDellAnno) : null;
	}
	
	


	private void setDataFineValiditaEDataCancellazioneSeNelPeriodoDiValidita(SiacTPdceConto eAttuale, Date dataCancellazioneDaImpostare, Date primoGiornoAnnoBilancio) {
		
		Date ultimoGiornoAnnoBilancioPrecedente = Utility.ultimoGiornoDellAnnoPrecedente(primoGiornoAnnoBilancio);
		//SIAC-6106
		int annoBilancioDellaModifica = Utility.getAnno(primoGiornoAnnoBilancio);	
		
		// SIAC-6106
		setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,
				ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, eAttuale.getSiacRCausaleEpPdceContos());
		setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,
				ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, eAttuale.getSiacRPdceContoAttrs());
		setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,
				ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, eAttuale.getSiacRPdceContoClasses());
		setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,
				ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, eAttuale.getSiacRPdceContos1());
		setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,
				ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, eAttuale.getSiacRPdceContos2());
		setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,
				ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, eAttuale.getSiacRPdceContoSoggettos());
	}


	/**
	 * @param dataCancellazioneDaImpostare
	 * @param primoGiornoAnnoBilancio
	 * @param ultimoGiornoAnnoBilancioPrecedente
	 * @param annoBilancioDellaModifica
	 * @param entita
	 */
	private <E extends SiacTBase> void setDataFineValiditaOldRecordsByAnnoBilancio(Date dataCancellazioneDaImpostare,
			Date primoGiornoAnnoBilancio, Date ultimoGiornoAnnoBilancioPrecedente, int annoBilancioDellaModifica,
			List<E> entitas) {
		final String methodName ="setDataFineValiditaOldRecordsByAnnoBilancio";
		
		if(entitas != null){
			for(E entita: entitas) {
				// SIAC-6106
				if(entita.getDataCancellazione() == null) {
					Date ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale = Utility.ultimoGiornoDellAnno(entita.getDataInizioValidita());
					//SIAC-6106
					int annoInizioValidita = Utility.getAnno(entita.getDataInizioValidita());				
					Date dataFineValiditaDaImpostare = annoInizioValidita <= annoBilancioDellaModifica ? 
							//ho inserito/aggiornato una relazione nel 2017 ma ho relazioni nel 2018, non modifico successiva
							Utility.dataPiuRecente(ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale, ultimoGiornoAnnoBilancioPrecedente) :
							//ho inserito/aggiornato una relazione nel 2019 e sto aggiornando una relazione nel 2018
							entita.getDataFineValidita();
					log.info(methodName, 
							" dataFineValiditaDaImpostare: " + dataFineValiditaDaImpostare
							+ " A partire dall' ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale: " 
							+ (ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale != null ? ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale : "null")
							+ "e ultimoGiornoAnnoBilancioPrecedente: "
							+ (ultimoGiornoAnnoBilancioPrecedente != null? ultimoGiornoAnnoBilancioPrecedente : "null")
							+ "la data piu' recente e': "
							+ (Utility.dataPiuRecente(ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale, ultimoGiornoAnnoBilancioPrecedente) != null ? Utility.dataPiuRecente(ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale, ultimoGiornoAnnoBilancioPrecedente) : "null" )
							+ "e la data validita fine pregressa e' "
							+ entita.getDataFineValidita()!= null? entita.getDataFineValidita()  : "null"							 
								);
							
					entita.sovrascriviDataFineValiditaESetDataCancellazioneSeNelPeriodoDiValidita(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio, dataFineValiditaDaImpostare);
				}
			}
		}
	}


	@Override
	public Page<SiacTPdceConto> ricercaSinteticaConto(Integer enteProprietarioId, SiacDAmbitoEnum siacDAmbitoEnum, Integer anno, Integer uidClassePiano, String codiceInterno, String codice, Integer uidContoPadre,
			Integer livello, Integer classifId, Boolean ammortamento,Boolean contoFoglia, Boolean attivo, Integer uidTipoConto, String codiceTipoConto, Pageable pageable) {

		final String methodName = "ricercaSinteticaConto";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		siacDAmbitoEnum = siacDAmbitoEnum != null ? siacDAmbitoEnum : SiacDAmbitoEnum.AmbitoFin;
		
		componiQueryRicercaSinteticaConto(jpql, param, enteProprietarioId, siacDAmbitoEnum, anno, uidClassePiano, codiceInterno, codice, uidContoPadre, livello, classifId, ammortamento,contoFoglia, attivo, uidTipoConto, codiceTipoConto);
		
		jpql.append(" ORDER BY c.ordine ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	private void appendFilterDataInizioFineValidita(StringBuilder jpql, Map<String, Object> param, final String tableAlias, Integer anno) {
		jpql.append(" AND ( ");
		jpql.append("     DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno"+tableAlias+", ' 12 31'), 'YYYY MM DD') )  ");
		jpql.append("     BETWEEN  ");
		jpql.append("         DATE_TRUNC('day',"+tableAlias+".dataInizioValidita) ");
		jpql.append("     AND ");
		jpql.append("         COALESCE("+tableAlias+".dataFineValidita, DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno"+tableAlias+", ' 12 31'), 'YYYY MM DD') ) ) ");
		jpql.append(" ) ");
		
		param.put("anno"+tableAlias, anno);
		
	}
	

	private void componiQueryRicercaSinteticaConto(StringBuilder jpql, Map<String, Object> param, 
			Integer enteProprietarioId, SiacDAmbitoEnum siacDAmbitoEnum, Integer anno, Integer pdceFamId, String codiceInterno, String pdceContoCode, Integer uidContoPadre,
			Integer livello, Integer classifId, Boolean ammortamento, Boolean contoFoglia, Boolean attivo, Integer uidTipoConto, String codiceTipoConto) {
		
		jpql.append("FROM SiacTPdceConto c ");
		jpql.append(" WHERE ");
		jpql.append(" c.dataCancellazione IS NULL ");
		jpql.append(" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND c.siacDAmbito.ambitoCode = :ambitoCode ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("ambitoCode", siacDAmbitoEnum.getCodice());
		
		appendFilterDataInizioFineValidita(jpql, param, "c", anno);
		
		if(pdceFamId != null){
			jpql.append(" AND c.siacTPdceFamTree.siacDPdceFam.pdceFamId = :pdceFamId ");
			param.put("pdceFamId", pdceFamId);			
		}
		
		
		if(StringUtils.isNotBlank(codiceInterno)) {
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM c.siacRPdceContoAttrs pca ");
			jpql.append("	  WHERE pca.dataCancellazione IS NULL ");
			jpql.append("	  AND pca.siacTAttr.attrCode = '" + SiacTAttrEnum.PdceContoCodificaInterna.getCodice() +"' ");
			jpql.append("	  AND UPPER(pca.testo) = UPPER(:codiceInterno) ");
			
			appendFilterDataInizioFineValidita(jpql, param, "pca", anno);
			
			jpql.append("   ) ");
			
			param.put("codiceInterno", codiceInterno);	
		}
		
		if(StringUtils.isNotBlank(pdceContoCode)){
			jpql.append(" AND  UPPER(c.pdceContoCode) = UPPER(:pdceContoCode) ");
			param.put("pdceContoCode", pdceContoCode);			
		}
		
		if(uidContoPadre != null) {
			jpql.append(" AND c IN ( ");
			
			jpql.append("           SELECT cFiglio FROM SiacTPdceConto cFiglio, SiacTPdceConto cPadre ");
			jpql.append("           WHERE cFiglio.dataCancellazione IS NULL ");
			jpql.append("           AND cPadre.dataCancellazione IS NULL ");
			jpql.append("           AND cFiglio.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
			jpql.append("           AND cPadre.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
			
			jpql.append("           AND cPadre.pdceContoId = :uidContoPadre ");
			
			jpql.append("           AND cFiglio.siacTPdceConto = cPadre ");
			
			jpql.append("           AND cFiglio.siacTPdceFamTree = cPadre.siacTPdceFamTree ");
			jpql.append("           AND cFiglio.livello = (cPadre.livello  + 1) ");
			
			appendFilterDataInizioFineValidita(jpql, param, "cFiglio", anno);
			appendFilterDataInizioFineValidita(jpql, param, "cPadre", anno);
					
			jpql.append("          ) ");
			
			
			param.put("uidContoPadre", uidContoPadre);	
		}
		
		if(livello != null){
			jpql.append(" AND c.livello = :livello ");
			param.put("livello", livello);			
		}
		
		if(classifId != null && classifId != 0){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM c.siacRPdceContoClasses pcc ");
			jpql.append("	  WHERE pcc.dataCancellazione IS NULL ");
			jpql.append("	  AND pcc.siacTClass.classifId = :classifId ");
			
			appendFilterDataInizioFineValidita(jpql, param, "pcc", anno);
			
			jpql.append(" 			) ");
			param.put("classifId", classifId);	
		}
		
		if(ammortamento != null){
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM c.siacRPdceContoAttrs pca ");
			jpql.append("	  WHERE pca.dataCancellazione IS NULL ");
			jpql.append("	  AND pca.siacTAttr.attrCode = '" + SiacTAttrEnum.PdceAmmortamento.getCodice() +"' ");
			jpql.append("	  AND UPPER(pca.boolean_) = UPPER(:stringaAmmortamento) ");
			
			appendFilterDataInizioFineValidita(jpql, param, "pca", anno);
			
			jpql.append("   ) ");
			
			param.put("stringaAmmortamento", ammortamento ? "S" : "N");	
		}
		if(contoFoglia != null){
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM c.siacRPdceContoAttrs pca ");
			jpql.append("	  WHERE pca.dataCancellazione IS NULL ");
			jpql.append("	  AND pca.siacTAttr.attrCode = '" + SiacTAttrEnum.PdceContoFoglia.getCodice() +"' ");
			jpql.append("	  AND UPPER(pca.boolean_) = UPPER(:stringaContoFoglia) ");
			
			appendFilterDataInizioFineValidita(jpql, param, "pca", anno);
			
			jpql.append("   ) ");
			
			param.put("stringaContoFoglia", contoFoglia ? "S" : "N");	
		}
		if(attivo != null){
			jpql.append("   AND EXISTS( ");
			jpql.append("     FROM c.siacRPdceContoAttrs pca ");
			jpql.append("	  WHERE pca.dataCancellazione IS NULL ");
			jpql.append("	  AND pca.siacTAttr.attrCode = '" + SiacTAttrEnum.PdceContoAttivo.getCodice() +"' ");
			jpql.append("	  AND UPPER(pca.boolean_) = UPPER(:stringaAttivo) ");
			
			appendFilterDataInizioFineValidita(jpql, param, "pca", anno);
			
			jpql.append("   ) ");
			
			param.put("stringaAttivo", attivo ? "S" : "N");	
		}
		if(uidTipoConto != null) {
			jpql.append(    "and c.siacDPdceContoTipo.pdceCtTipoId = :pdceCtTipoId");
			param.put("pdceCtTipoId", uidTipoConto);	
			
		}else if(codiceTipoConto != null) {
			jpql.append(    "and c.siacDPdceContoTipo.pdceCtTipoCode = :pdceCtTipoCode");
			param.put("pdceCtTipoCode", codiceTipoConto);	
		}
	}


	@Override
	public List<SiacTPdceConto> ricercaFigliRicorsiva(Integer uidConto) {
		final String methodName = "ricercaFigliRicorsiva";
		StringBuilder jpql = new StringBuilder();
		
		jpql.append(" SELECT c FROM SiacTPdceConto c ");
		jpql.append(" WHERE c.dataCancellazione IS NULL ");
		//jpql.append(" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND c.siacTPdceConto.pdceContoId = :pdceContoIdPadre ");
		
		
		TypedQuery<SiacTPdceConto> query = entityManager.createQuery(jpql.toString(), SiacTPdceConto.class);
				
		query.setParameter("pdceContoIdPadre", uidConto);
		//query.setParameter("enteProprietarioId", uidConto);
		
		List<SiacTPdceConto> siacTPdceContos = query.getResultList();
		
		List<SiacTPdceConto> result = new ArrayList<SiacTPdceConto>();
		result.addAll(siacTPdceContos);
		
		
		for(SiacTPdceConto c : siacTPdceContos) {
			List<SiacTPdceConto> figli = ricercaFigliRicorsiva(c.getUid());
			result.addAll(figli);
		}
		
		log.debug(methodName, "Totale figli di uidConto: "+uidConto + " -> "+result.size());
		
		return result;
	}
	
	
	
	
	
	
	

}
