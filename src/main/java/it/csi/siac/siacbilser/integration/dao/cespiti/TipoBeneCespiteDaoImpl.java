/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

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

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TipoBeneCespiteDaoImpl extends JpaDao<SiacDCespitiBeneTipo, Integer> implements TipoBeneCespiteDao {
	
	public SiacDCespitiBeneTipo create(SiacDCespitiBeneTipo e){
		Date now = new Date();
		Date dataInizioValidita = e.getDataInizioValidita();
		
		e.setDataModificaInserimento(now, dataInizioValidita);
		
		setDataModificaInserimento(e, now, dataInizioValidita);
		e.setUid(null);		
		super.save(e);
		return e;
	}


	public SiacDCespitiBeneTipo update(SiacDCespitiBeneTipo e){		
		SiacDCespitiBeneTipo eAttuale = this.findById(e.getUid());		
		Date now = new Date();
		Date dataInizioValidita = e.getDataInizioValidita();
		e.setDataModifica(now);
		//SIAC-6393
		e.setDataFineValidita(eAttuale.getDataFineValidita());
		
		setDataFineValiditaEDataCancellazioneSeNelPeriodoDiValidita(eAttuale, now, dataInizioValidita);
		e.setDataModifica(now);
		setDataModificaInserimento(e, now, dataInizioValidita);
		entityManager.flush();		
		super.update(e);
		return e;
	}

	
	@Override
	public SiacDCespitiBeneTipo delete(int uidTipoBeneCespite, String loginOperazione) {		
		SiacDCespitiBeneTipo eAttuale = this.findById(uidTipoBeneCespite);		
		Date now = new Date();		
		eAttuale.setDataCancellazioneIfNotSet(now);
		eAttuale.setLoginOperazione(loginOperazione);
		super.update(eAttuale);		
		return eAttuale;
	}
	
	@Override
	public Page<SiacDCespitiBeneTipo>ricercaSinteticaTipoBeneCespite(			
			int enteProprietarioId,
			String cespitiBeneTipoCodice,					
			String cespitiBeneTipoDescrizione,					
			Integer categoriaCespitiUid,
			String	categoriaCespitiCodice,
			Integer contoPatrimonialeUid,
			String contoPatrimonialeCodice,
			Date dataInizioValiditaFiltro,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaTipoBeneCespite";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaTipoBeneCespiti( jpql,  param, enteProprietarioId, cespitiBeneTipoCodice,cespitiBeneTipoDescrizione, categoriaCespitiUid, categoriaCespitiCodice,contoPatrimonialeUid,contoPatrimonialeCodice, dataInizioValiditaFiltro);
		
		jpql.append(" ORDER BY d.cesBeneTipoCode ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}



	private void componiQueryRicercaSinteticaTipoBeneCespiti(StringBuilder jpql, Map<String, Object> param,
			int enteProprietarioId, String cespitiBeneTipoCodice,String cespitiBeneTipoDesc, Integer categoriaCespitiUid,
			String categoriaCespitiCodice, Integer contoPatrimonialeUid, String contoPatrimonialeCodice, Date dataInizioValiditaFiltro) {
		
		jpql.append(" FROM SiacDCespitiBeneTipo d ");
		jpql.append(" WHERE ");
		jpql.append(" d.dataCancellazione IS NULL ");
		jpql.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);

		if(categoriaCespitiUid != null && categoriaCespitiUid != 0){
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRCespitiBeneTipoContoPatrCat r ");
			jpql.append(" WHERE r.siacDCespitiCategoria.cescatId = :cescatId ");
			jpql.append(" AND r.siacDCespitiBeneTipo = d ");
			jpql.append(" AND r.dataCancellazione IS NULL ");
			jpql.append(" AND (r.dataFineValidita IS NULL OR :dataInputCategoria <= r.dataFineValidita) "); 
			jpql.append(" AND (r.dataInizioValidita <= :dataInputCategoria) ");
			jpql.append(" ) ");
			
			param.put("cescatId", categoriaCespitiUid);	
			param.put("dataInputCategoria", dataInizioValiditaFiltro);
			
			
		}

		if(contoPatrimonialeUid != null  && contoPatrimonialeUid != 0){
			
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRCespitiBeneTipoContoPatrCat r ");
			jpql.append(" WHERE r.siacTPdceContoPatrimoniale.pdceContoId = :pdceContoId ");
			jpql.append(" AND r.siacDCespitiBeneTipo = d ");
			jpql.append(" AND r.dataCancellazione IS NULL ");
			jpql.append(" AND (r.dataFineValidita IS NULL OR :dataInputCategoria <= r.dataFineValidita) "); 
			jpql.append(" AND (r.dataInizioValidita <= :dataInputCategoria) ");
			jpql.append(" ) ");
			
			param.put("pdceContoId", contoPatrimonialeUid);	
			param.put("dataInputCategoria", dataInizioValiditaFiltro);
		}
		
		if(StringUtils.isNotBlank(cespitiBeneTipoCodice )){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.cesBeneTipoCode", "CONCAT('%', :cespitiBeneTipoCodice, '%')") + " ");
			param.put("cespitiBeneTipoCodice", cespitiBeneTipoCodice);	
		}

		if(StringUtils.isNotBlank(cespitiBeneTipoDesc )){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.cesBeneTipoDesc", "CONCAT('%', :cespitiBeneTipoDesc, '%')") + " ");
			param.put("cespitiBeneTipoDesc", cespitiBeneTipoDesc);	
		}

		if(StringUtils.isNotBlank(categoriaCespitiCodice )){
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRCespitiBeneTipoContoPatrCat r ");
			jpql.append(" WHERE r.siacDCespitiCategoria.cescatId = :cescatId ");
			jpql.append(" AND r.siacDCespitiBeneTipo = d ");
			jpql.append(" AND r.dataCancellazione IS NULL ");
			jpql.append(" AND (r.dataFineValidita IS NULL OR :dataInputCodiceCategoria <= r.dataFineValidita) "); 
			jpql.append(" AND (r.dataInizioValidita <= :dataInputCodiceCategoria) ");
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.siacDCespitiCategoria.cescatCode", "CONCAT('%', :categoriaCespitiCodice, '%')") + " ");
			jpql.append(" ) ");
			param.put("categoriaCespitiCodice", categoriaCespitiCodice);
			param.put("dataInputCodiceCategoria", dataInizioValiditaFiltro);
		}
		
		if(StringUtils.isNotBlank(contoPatrimonialeCodice)){
			
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRCespitiBeneTipoContoPatrCat r ");
			jpql.append(" WHERE r.siacDCespitiBeneTipo = d ");
			jpql.append(" AND r.dataCancellazione IS NULL ");
			jpql.append(" AND (r.dataFineValidita IS NULL OR :dataInputCategoria <= r.dataFineValidita) "); 
			jpql.append(" AND (r.dataInizioValidita <= :dataInputCategoria) ");
			jpql.append(" AND " + Utility.toJpqlSearchLike("r.pdceContoPatrimonialeCode", "CONCAT('%', :contoPatrimonialeCodice, '%')") + " ");
			jpql.append(" ) ");
			
			param.put("contoPatrimonialeCodice", contoPatrimonialeCodice);	
			param.put("dataInputCategoria", dataInizioValiditaFiltro);
		}
	
	}

	/**
	 * Sets the data modifica inserimento.
	 *
	 * @param e the e
	 * @param now the now
	 * @param dataInizioValidita the data inizio validita
	 */
	private void setDataModificaInserimento(SiacDCespitiBeneTipo e, Date now, Date dataInizioValidita) {
		int annoInizioValidita = Utility.getAnno(dataInizioValidita);
		Date ultimoGiornoDellAnno = Utility.ultimoGiornoDellAnno(dataInizioValidita);
		
		// SIAC-6011
		setDataFineValiditaNewRecordByAnnoBilancio(e, now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, e.getSiacRCespitiBeneTipoContoPatrCats(), "SiacRCespitiBeneTipoContoPatrCat");
		
	}
	
	/**
	 * @param e
	 * @param now
	 * @param dataInizioValidita
	 * @param annoInizioValidita
	 * @param ultimoGiornoDellAnno
	 */
	private <E extends SiacTBase> void setDataFineValiditaNewRecordByAnnoBilancio(SiacDCespitiBeneTipo e, Date now, Date dataInizioValidita,
			int annoInizioValidita, Date ultimoGiornoDellAnno, List<E> entitas, String entitaName) {
		if(entitas != null){
			//SIAC-6106
			Date validitaInizioMinima = ottieniMinValiditaInizioByNomeEntitaCollegamento(e.getUid(), dataInizioValidita, entitaName);
			
			for(E entita : entitas){
				entita.setDataModificaInserimento(now, dataInizioValidita);
				entita.setUid(null);
				Date validitaFine = ottieniValiditaInizioDaImpostareInBaseAlRecordSuccessivo(validitaInizioMinima, ultimoGiornoDellAnno, annoInizioValidita);
				
				if(validitaFine != null) {
					entita.setDataFineValiditaIfNotSet(validitaFine);
				}
			}
		}
	}
	
	/**
	 * @param uid
	 * @param aliasR
	 */
	private Date ottieniMinValiditaInizioByNomeEntitaCollegamento(Integer uid, Date dataDiPartenza, String aliasR) {
		final String methodName = "ottieniMinValiditaInizioByNomeEntitaCollegamento";
		if(uid == null || uid == 0 || StringUtils.isEmpty(aliasR)) {
			return null;
		}
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT MIN( rcoll.dataInizioValidita ) ")
			.append(" FROM " + aliasR  + " rcoll ")
			.append(" WHERE rcoll.siacDCespitiBeneTipo.cesBeneTipoId = :cesBeneTipoId ")
			.append(" AND rcoll.dataCancellazione IS NULL ")
			.append(" AND rcoll.dataInizioValidita >  :dataInizioValidita ");
		
		param.put("cesBeneTipoId", uid);
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

	
	private void setDataFineValiditaEDataCancellazioneSeNelPeriodoDiValidita(SiacDCespitiBeneTipo eAttuale, Date dataCancellazioneDaImpostare, Date primoGiornoAnnoBilancio) {
		
		Date ultimoGiornoAnnoBilancioPrecedente = Utility.ultimoGiornoDellAnnoPrecedente(primoGiornoAnnoBilancio);
		int annoBilancioDellaModifica = Utility.getAnno(primoGiornoAnnoBilancio);	
		
		setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,
				ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, eAttuale.getSiacRCespitiBeneTipoContoPatrCats());
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
							+ (entita.getDataFineValidita()!= null? entita.getDataFineValidita()  : "null")							 
								);
					entita.sovrascriviDataFineValiditaESetDataCancellazioneSeNelPeriodoDiValidita(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio, dataFineValiditaDaImpostare);
				}
			}
		}
	}


}
