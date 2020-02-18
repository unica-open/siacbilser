/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import java.math.BigDecimal;
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
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoria;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CategoriaCespitiDaoImpl extends JpaDao<SiacDCespitiCategoria, Integer> implements CategoriaCespitiDao {
	
	public SiacDCespitiCategoria create(SiacDCespitiCategoria e){
		Date now = new Date();
		Date dataInizioValidita = e.getDataInizioValidita();
		
		e.setDataModificaInserimento(now, dataInizioValidita);
		
		setDataModificaInserimento(e, now, dataInizioValidita);	
		e.setUid(null);		
		super.save(e);
		return e;
	}


	public SiacDCespitiCategoria update(SiacDCespitiCategoria e){		
		SiacDCespitiCategoria eAttuale = this.findById(e.getUid());		
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
	public SiacDCespitiCategoria delete(int uidCategoriaCespiti, String loginOperazione) {		
		SiacDCespitiCategoria eAttuale = this.findById(uidCategoriaCespiti);		
		Date now = new Date();		
		eAttuale.setDataCancellazioneIfNotSet(now);
		eAttuale.setLoginOperazione(loginOperazione);
		super.update(eAttuale);		
		return eAttuale;
	}
	
	


	@Override
	public Page<SiacDCespitiCategoria> ricercaSinteticaCategoriaCespiti(int enteProprietarioId, String cescatCode, String cescatDesc, BigDecimal aliquotaAnnua, Integer cescatCalcoloTipoId, Boolean escludiAnnullati, String ambitoCode, Date dataValidita, Pageable pageable) {
		
		final String methodName = "ricercaSinteticaCategoriaCespiti";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaCategoriaCespiti( jpql,  param, enteProprietarioId, cescatCode, cescatDesc, aliquotaAnnua, cescatCalcoloTipoId, escludiAnnullati, ambitoCode, dataValidita);
		
		jpql.append(" ORDER BY d.cescatCode ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}


	private void componiQueryRicercaSinteticaCategoriaCespiti(StringBuilder jpql, Map<String, Object> param,
			int enteProprietarioId, String cescatCode, String cescatDesc, BigDecimal aliquotaAnnua,
			Integer cescatCalcoloTipoId, Boolean escludiAnnullati, String ambitoCode, Date dataValidita) {
		
		jpql.append(" FROM SiacDCespitiCategoria d ");
		jpql.append(" WHERE ");
		jpql.append(" d.dataCancellazione IS NULL ");
		jpql.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND d.siacDAmbito.ambitoCode = :ambitoCode ");
		jpql.append(" AND d.dataInizioValidita <= :dataInizioValidita ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("ambitoCode", ambitoCode);
		param.put("dataInizioValidita", dataValidita);
		
		if(Boolean.TRUE.equals(escludiAnnullati)) {
			jpql.append(" AND d.dataFineValidita IS NULL ");
		}
		
		if(StringUtils.isNotBlank(cescatCode)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.cescatCode", "CONCAT('%', :cescatCode, '%')") + " ");
			param.put("cescatCode", cescatCode);	
		}
		
		if(StringUtils.isNotBlank(cescatDesc)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.cescatDesc", "CONCAT('%', :cescatDesc, '%')") + " ");
			param.put("cescatDesc", cescatDesc);	
		}
		
		if(aliquotaAnnua!= null){
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRCespitiCategoriaAliquotaCalcoloTipo r ");
			jpql.append(" WHERE r.siacDCespitiCategoria.cescatId = d.cescatId ");
			jpql.append(" AND r.dataCancellazione IS NULL ");
			jpql.append(" AND r.dataCancellazione IS NULL ");
			jpql.append(" AND (r.dataFineValidita IS NULL OR :dataInputAliquota <= r.dataFineValidita) "); 
			jpql.append(" AND (r.dataInizioValidita <= :dataInputAliquota) ");
			jpql.append(" AND (r.aliquotaAnnua = :aliquotaAnnua) ");
			jpql.append(" ) ");
			param.put("aliquotaAnnua", aliquotaAnnua);
			param.put("dataInputAliquota", dataValidita);
		}

		if(cescatCalcoloTipoId!= null){
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRCespitiCategoriaAliquotaCalcoloTipo r ");
			jpql.append(" WHERE r.siacDCespitiCategoria.cescatId = d.cescatId ");
			jpql.append(" AND r.dataCancellazione IS NULL ");
			jpql.append(" AND r.dataCancellazione IS NULL ");
			jpql.append(" AND (r.dataFineValidita IS NULL OR :dataInputTipoCalcolo <= r.dataFineValidita) "); 
			jpql.append(" AND (r.dataInizioValidita <= :dataInputTipoCalcolo) ");
			jpql.append(" AND (r.siacDCespitiCategoriaCalcoloTipo.cescatCalcoloTipoId = :cescatCalcoloTipoId) ");
			jpql.append(" ) ");
			param.put("cescatCalcoloTipoId", cescatCalcoloTipoId);
			param.put("dataInputTipoCalcolo", dataValidita);
		}
		
	}
	
	/**
	 * Sets the data modifica inserimento.
	 *
	 * @param e the e
	 * @param now the now
	 * @param dataInizioValidita 
	 */
	private void setDataModificaInserimento(SiacDCespitiCategoria e, Date now, Date dataInizioValidita) {
		int annoInizioValidita = Utility.getAnno(dataInizioValidita);
		Date ultimoGiornoDellAnno = Utility.ultimoGiornoDellAnno(dataInizioValidita);
		
		// SIAC-6011
		setDataFineValiditaNewRecordByAnnoBilancio(e, now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, e.getSiacRCategoriaCespitiAliquotaCalcoloTipos(), "SiacRCespitiCategoriaAliquotaCalcoloTipo");
		
	}
	
	/**
	 * @param e
	 * @param now
	 * @param dataInizioValidita
	 * @param annoInizioValidita
	 * @param ultimoGiornoDellAnno
	 */
	private <E extends SiacTBase> void setDataFineValiditaNewRecordByAnnoBilancio(SiacDCespitiCategoria e, Date now, Date dataInizioValidita,
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
			.append(" WHERE rcoll.siacDCespitiCategoria.cescatId = :cescatId ")
			.append(" AND rcoll.dataCancellazione IS NULL ")
			.append(" AND rcoll.dataInizioValidita >  :dataInizioValidita ");
		
		param.put("cescatId", uid);
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

	
private void setDataFineValiditaEDataCancellazioneSeNelPeriodoDiValidita(SiacDCespitiCategoria eAttuale, Date dataCancellazioneDaImpostare, Date primoGiornoAnnoBilancio) {
		
		Date ultimoGiornoAnnoBilancioPrecedente = Utility.ultimoGiornoDellAnnoPrecedente(primoGiornoAnnoBilancio);
		int annoBilancioDellaModifica = Utility.getAnno(primoGiornoAnnoBilancio);	
		
		setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,
				ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, eAttuale.getSiacRCategoriaCespitiAliquotaCalcoloTipos());
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
