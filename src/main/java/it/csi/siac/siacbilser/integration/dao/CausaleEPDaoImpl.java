/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

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
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleEpClass;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleEpPdceConto;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleEpPdceContoOper;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleEpSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleEpStato;
import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneClasseCausaleEp;
import it.csi.siac.siacbilser.integration.entity.SiacREventoCausale;
import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDConciliazioneClasseEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;

/**
 * The Class CausaleEPDaoImpl.
 * 
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CausaleEPDaoImpl extends JpaDao<SiacTCausaleEp, Integer> implements CausaleEPDao {
	
	@Override
	public SiacTCausaleEp create(SiacTCausaleEp e){
		Date now = new Date();
		Date dataInizioValidita = e.getDataInizioValidita();
		
		e.setDataModificaInserimento(now, dataInizioValidita);
		
		setDataModificaInserimento(e, now, dataInizioValidita);
		
		
		e.setUid(null);		
		super.save(e);
		return e;
	}

	@Override
	public SiacTCausaleEp update(SiacTCausaleEp e){
		
		SiacTCausaleEp eAttuale = this.findById(e.getUid());
		
		Date now = new Date();
		Date dataInizioValidita = e.getDataInizioValidita(); //2014
			
		//e.setDataModificaAggiornamento(now, dataInizioValidita);
		e.setDataModifica(now);
		//e.setDataInizioValidita(dataInizioValidita); //non si puo' aggiornare in quanto dataCreazione 
		                                               //ha attributo updatable a false e va bene così perchè in analisi non si vuole aggiornare
		//e.setDataCreazione(eAttuale.getDataCreazione()); //non necessario in quanto dataCreazione ha updatable a false
		
		
		//imposto data fine validita e cancellazione elementi collegati	
		setDataFineValiditaEDataCancellazioneSeNelPeriodoDiValidita(eAttuale, now, dataInizioValidita/*, dataFineValidiata*/);
		
		entityManager.flush();
		//SIAC-6871
		setDataModificaInserimento(e, now, dataInizioValidita);

		return super.update(e);
	}
	
	@Override
	public SiacTCausaleEp updateStato(SiacTCausaleEp e){
		SiacTCausaleEp eAttuale = this.findById(e.getUid());
		
		Date now = new Date();
		Date dataInizioValidita = e.getDataInizioValidita();
		Date dataFineValidiataAnnoPrecedente = Utility.ultimoGiornoDellAnnoPrecedente(dataInizioValidita);
		
		if(eAttuale.getSiacRCausaleEpStatos() != null) {
			for(SiacRCausaleEpStato r: eAttuale.getSiacRCausaleEpStatos()){
				
				Date dataFineValidiataEntityAttuale = Utility.ultimoGiornoDellAnno(r.getDataInizioValidita());
				Date dataFineValidiata = Utility.dataPiuRecente(dataFineValidiataEntityAttuale,dataFineValidiataAnnoPrecedente);
				
				r.setDataFineValiditaEDataCancellazioneSeNelPeriodoDiValidita(now, dataInizioValidita, dataFineValidiata);
			}
		}
		
		entityManager.flush();

		if(e.getSiacRCausaleEpStatos() != null){
			for(SiacRCausaleEpStato r: e.getSiacRCausaleEpStatos()){
				r.setDataModificaInserimento(now, dataInizioValidita);
				r.setUid(null);
				
				eAttuale.addSiacRCausaleEpStato(r);
			}
		}

		return e;
	}
	
	
	@Override
	public void delete(SiacTCausaleEp entity) {
		
		SiacTCausaleEp eAttuale = this.findById(entity.getUid());
		
		Date now = new Date();
		
		eAttuale.setDataCancellazioneIfNotSet(now);
		eAttuale.setLoginCancellazione(entity.getLoginCancellazione());
		eAttuale.setLoginOperazione(entity.getLoginOperazione());
		
		setDataCancellazioneIfNotSet(eAttuale,now, entity.getLoginOperazione());
		
	}
	
	/**
	 * Sets the data modifica inserimento.
	 *
	 * @param e the e
	 * @param now the now
	 * @param dataInizioValidita 
	 */
	private void setDataModificaInserimento(SiacTCausaleEp e, Date now, Date dataInizioValidita) {
		
		//SIAC-6871
		int annoInizioValidita = Utility.getAnno(dataInizioValidita);
		Date ultimoGiornoDellAnno = Utility.ultimoGiornoDellAnno(dataInizioValidita);
		
		//SIAC-6871
		setDataFineValiditaNewRecordByAnnoBilancio(e, now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, e.getSiacRCausaleEpClasses(), "SiacRCausaleEpClass");
		setDataFineValiditaNewRecordByAnnoBilancio(e, now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, e.getSiacRCausaleEpSoggettos(), "SiacRCausaleEpSoggetto");
		setDataFineValiditaNewRecordByAnnoBilancio(e, now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, e.getSiacRCausaleEpStatos(), "SiacRCausaleEpStato");
		setDataFineValiditaNewRecordByAnnoBilancio(e, now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, e.getSiacREventoCausales(), "SiacREventoCausale");
		
		if(e.getSiacRCausaleEpPdceContos() != null){
			Date validitaInizioMinimaRConto = ottieniMinValiditaInizioByNomeEntitaCollegamentoConCausaleEp(e.getCausaleEpId(), dataInizioValidita, "SiacRCausaleEpPdceConto");
			for(SiacRCausaleEpPdceConto r: e.getSiacRCausaleEpPdceContos()){

				setDataFineValiditaNewRecordByAnnoBilancioSingolaEntita(now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, validitaInizioMinimaRConto, r);
				if(r.getSiacRCausaleEpPdceContoOpers()!=null){
					for(SiacRCausaleEpPdceContoOper rOper : r.getSiacRCausaleEpPdceContoOpers()){
						if(rOper.getSiacDOperazioneEp() == null) {
							continue;
						}
						Date validitaInizioMinima =  ottieniMinValiditaInizioSiacRCausaleEpPdceContoOper(e.getCausaleEpId(), dataInizioValidita, rOper.getSiacDOperazioneEp().getUid());
						setDataFineValiditaNewRecordByAnnoBilancioSingolaEntita(now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, validitaInizioMinima, rOper);
					}
				}
				
				if(r.getSiacRConciliazioneClasseCausaleEps() != null){
					Date validitaInizioMinima = ottieniMinValiditaInizioSiacRConciliazioneClasseCausaleEp(e.getCausaleEpId(),dataInizioValidita);
					for(SiacRConciliazioneClasseCausaleEp rConc: r.getSiacRConciliazioneClasseCausaleEps()){
						setDataFineValiditaNewRecordByAnnoBilancioSingolaEntita(now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, validitaInizioMinima, rConc);
					}
				}
			}
		}

		
	}

	/**
	 * Sets the data cancellazione if not set.
	 *
	 * @param eAttuale the e attuale
	 * @param dataCancellazioneDaImpostare the now
	 * @param primoGiornoAnnoBilancio 
	 */
	private void setDataFineValiditaEDataCancellazioneSeNelPeriodoDiValidita(SiacTCausaleEp eAttuale, Date dataCancellazioneDaImpostare, Date primoGiornoAnnoBilancio) {
		//SIAC-6871
		Date ultimoGiornoAnnoBilancioPrecedente = Utility.ultimoGiornoDellAnnoPrecedente(primoGiornoAnnoBilancio);
		int annoBilancioDellaModifica = Utility.getAnno(primoGiornoAnnoBilancio);
		
		
		setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,
				ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, eAttuale.getSiacRCausaleEpClasses());
		setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,
				ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, eAttuale.getSiacRCausaleEpSoggettos());
		setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, eAttuale.getSiacRCausaleEpStatos());
		setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,
				ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, eAttuale.getSiacREventoCausales());
		
		if(eAttuale.getSiacRCausaleEpPdceContos() != null){
			for(SiacRCausaleEpPdceConto r: eAttuale.getSiacRCausaleEpPdceContos()){
				if(r.getDataCancellazione() != null) {
					continue;
				}
				
				setDataFineValiditaSingoloOldRecodByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio, ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, r);
				setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica,r.getSiacRCausaleEpPdceContoOpers());
				setDataFineValiditaOldRecordsByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio,ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica,r.getSiacRConciliazioneClasseCausaleEps());
			}
		}
		
	}
	
	
	
	
	
	/**
	 * @param dataCancellazioneDaImpostare
	 * @param primoGiornoAnnoBilancio
	 * @param ultimoGiornoAnnoBilancioPrecedente
	 * @param annoBilancioDellaModifica
	 * @param entita
	 */
	private <E extends SiacTBase> void setDataFineValiditaOldRecordsByAnnoBilancio(Date dataCancellazioneDaImpostare, Date primoGiornoAnnoBilancio, Date ultimoGiornoAnnoBilancioPrecedente, int annoBilancioDellaModifica,
			List<E> entitas) {
		if(entitas != null){
			for(E entita: entitas) {
				// SIAC-6871
				setDataFineValiditaSingoloOldRecodByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio, ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, entita);
			}
		}
	}
	
	private <E extends SiacTBase> void setDataFineValiditaOldRecordsByAnnoBilancio(Date dataCancellazioneDaImpostare, Date primoGiornoAnnoBilancio, Date ultimoGiornoAnnoBilancioPrecedente, int annoBilancioDellaModifica,
			List<E> entitas, String aliasR) {
		//SIAC-6871
		if(entitas != null){
			for(E entita: entitas) {
				// SIAC-6871
				setDataFineValiditaSingoloOldRecodByAnnoBilancio(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio, ultimoGiornoAnnoBilancioPrecedente, annoBilancioDellaModifica, entita);
			}
		}
	}
	

	/**
	 * @param <E>
	 * @param dataCancellazioneDaImpostare
	 * @param primoGiornoAnnoBilancio
	 * @param ultimoGiornoAnnoBilancioPrecedente
	 * @param annoBilancioDellaModifica
	 * @param methodName
	 * @param entita
	 */
	private <E extends SiacTBase> void setDataFineValiditaSingoloOldRecodByAnnoBilancio(Date dataCancellazioneDaImpostare, Date primoGiornoAnnoBilancio, Date ultimoGiornoAnnoBilancioPrecedente,
			int annoBilancioDellaModifica, E entita) {
		final String methodName = "setDataFineValiditaSingoloOldRecodByAnnoBilancio";
		if(entita.getDataCancellazione() == null) {
			Date ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale = Utility.ultimoGiornoDellAnno(entita.getDataInizioValidita()); // alpha
			//SIAC-6871
			int annoInizioValidita = Utility.getAnno(entita.getDataInizioValidita());
			int deltaBilancioInizioValidita = annoBilancioDellaModifica - annoInizioValidita;
			boolean esistonoRecordSuccessiviNonCoinvoltiDaModifica = entita.getDataFineValidita() != null && (deltaBilancioInizioValidita > 1);
			Date dataFineValiditaDaImpostare = annoInizioValidita <= annoBilancioDellaModifica  && !esistonoRecordSuccessiviNonCoinvoltiDaModifica ?
//			Date dataFineValiditaDaImpostare = annoInizioValidita <= annoBilancioDellaModifica ?
					//ho inserito/aggiornato una relazione nel 2017 ma ho relazioni nel 2018, non modifico successiva
					Utility.dataPiuRecente(ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale, ultimoGiornoAnnoBilancioPrecedente) :
					//ho inserito/aggiornato una relazione nel 2019 e sto aggiornando una relazione nel 2018
					entita.getDataFineValidita();
			log.info(methodName, 
					new StringBuilder().append(" dataFineValiditaDaImpostare: ")
					.append(dataFineValiditaDaImpostare != null? dataFineValiditaDaImpostare : "null")
					.append(" A partire dall' ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale: ") 
					.append(ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale != null ? ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale : "null")
					.append("e ultimoGiornoAnnoBilancioPrecedente: ")
					.append(ultimoGiornoAnnoBilancioPrecedente != null? ultimoGiornoAnnoBilancioPrecedente : "null")
					.append("la data piu' recente e': ")
					.append(Utility.dataPiuRecente(ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale, ultimoGiornoAnnoBilancioPrecedente) != null ? Utility.dataPiuRecente(ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale, ultimoGiornoAnnoBilancioPrecedente) : "null" )
					.append("e la data validita fine pregressa e' ")
					.append(entita.getDataFineValidita()!= null? entita.getDataFineValidita()  : "null")
					.toString());
			entita.sovrascriviDataFineValiditaESetDataCancellazioneSeNelPeriodoDiValidita(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio, dataFineValiditaDaImpostare);
		}
	}
	
	
	private void setDataCancellazioneIfNotSet(SiacTCausaleEp eAttuale, Date now, String loginOperazione) {
		
		if(eAttuale.getSiacRCausaleEpClasses() != null){
			for(SiacRCausaleEpClass r: eAttuale.getSiacRCausaleEpClasses()){
				r.setDataCancellazioneIfNotSet(now);
				r.setLoginOperazione(loginOperazione);
			}
		}
		
		if(eAttuale.getSiacRCausaleEpPdceContos() != null){
			for(SiacRCausaleEpPdceConto r: eAttuale.getSiacRCausaleEpPdceContos()){
				r.setDataCancellazioneIfNotSet(now);
				r.setLoginOperazione(loginOperazione);
				
				if(r.getSiacRCausaleEpPdceContoOpers()!=null) {
					for(SiacRCausaleEpPdceContoOper rOper : r.getSiacRCausaleEpPdceContoOpers()){
						rOper.setDataCancellazioneIfNotSet(now);
						rOper.setLoginOperazione(loginOperazione);
					}
				}
				
				if(r.getSiacRConciliazioneClasseCausaleEps() != null){
					for(SiacRConciliazioneClasseCausaleEp rConc: r.getSiacRConciliazioneClasseCausaleEps()){
						rConc.setDataCancellazioneIfNotSet(now);
						rConc.setLoginOperazione(loginOperazione);
					}
				}
			}
		}
		
		if(eAttuale.getSiacRCausaleEpSoggettos() != null){
			for(SiacRCausaleEpSoggetto r: eAttuale.getSiacRCausaleEpSoggettos()){
				r.setDataCancellazioneIfNotSet(now);
				r.setLoginOperazione(loginOperazione);
			}
		}
		
		if(eAttuale.getSiacRCausaleEpStatos() != null) {
			for(SiacRCausaleEpStato r: eAttuale.getSiacRCausaleEpStatos()){
				r.setDataCancellazioneIfNotSet(now);
				r.setLoginOperazione(loginOperazione);
			}
		}
		
		if(eAttuale.getSiacREventoCausales() != null) {
			for(SiacREventoCausale r: eAttuale.getSiacREventoCausales()){
				r.setDataCancellazioneIfNotSet(now);
				r.setLoginOperazione(loginOperazione);
			}
		}
		
		
	}
	
	/**
	 * @param e
	 * @param now
	 * @param dataInizioValidita
	 * @param annoInizioValidita
	 * @param ultimoGiornoDellAnno
	 */
	private <E extends SiacTBase> void setDataFineValiditaNewRecordByAnnoBilancio(SiacTCausaleEp e, Date now, Date dataInizioValidita,
			int annoInizioValidita, Date ultimoGiornoDellAnno, List<E> entitas, String entitaName) {
		if(entitas != null){
			//SIAC-6871
			Date validitaInizioMinima = ottieniMinValiditaInizioByNomeEntitaCollegamentoConCausaleEp(e.getCausaleEpId(), dataInizioValidita, entitaName);
			
			for(E entita : entitas){
				setDataFineValiditaNewRecordByAnnoBilancioSingolaEntita(now, dataInizioValidita, annoInizioValidita, ultimoGiornoDellAnno, validitaInizioMinima, entita);
			}
		}
	}

	/**
	 * @param <E>
	 * @param now
	 * @param dataInizioValidita
	 * @param annoInizioValidita
	 * @param ultimoGiornoDellAnno
	 * @param validitaInizioMinima
	 * @param entita
	 */
	private <E extends SiacTBase> void setDataFineValiditaNewRecordByAnnoBilancioSingolaEntita(Date now, Date dataInizioValidita, int annoInizioValidita, Date ultimoGiornoDellAnno, Date validitaInizioMinima,
			E entita) {
		entita.setDataModificaInserimento(now, dataInizioValidita);
		entita.setUid(null);
		//SIAC-6871
		Date validitaFine = ottieniValiditaInizioDaImpostareInBaseAlRecordSuccessivo(validitaInizioMinima, ultimoGiornoDellAnno, annoInizioValidita);
		
		if(validitaFine != null) {
			entita.setDataFineValiditaIfNotSet(validitaFine);
		}
	}

	
	//SIAC-6871
	private Date ottieniValiditaInizioDaImpostareInBaseAlRecordSuccessivo(Date validitaInizioMinima, Date ultimoGiornoDellAnno, int annoInizioValidita){
		if(validitaInizioMinima == null) {
			return null;
		}
		int annoValiditaInizioMinima = Utility.getAnno(validitaInizioMinima);
		Date validitaFineDaImpostare = Utility.ultimoGiornoDellAnnoPrecedente(validitaInizioMinima);
		return annoValiditaInizioMinima > annoInizioValidita ? Utility.dataPiuRecente(validitaFineDaImpostare, ultimoGiornoDellAnno) : null;
	}
	
	/**
	 * @param uidCausaleEP
	 * @param aliasR
	 */
	private Date ottieniMinValiditaInizioByNomeEntitaCollegamentoConCausaleEp(Integer uidCausaleEP, Date dataDiPartenza, String aliasR) {
		final String methodName = "ottieniMinValiditaInizioByNomeEntitaCollegamento";
		//SIAC-6871
		if(uidCausaleEP == null || uidCausaleEP == 0 || StringUtils.isEmpty(aliasR)) {
			return null;
		}
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT MIN( rcoll.dataInizioValidita ) ")
			.append(" FROM " + aliasR  + " rcoll ")
			.append(" WHERE rcoll.siacTCausaleEp.causaleEpId = :causaleEpId ")
			.append(" AND rcoll.dataCancellazione IS NULL ")
			.append(" AND rcoll.dataInizioValidita >  :dataInizioValidita ");
		
		param.put("causaleEpId", uidCausaleEP);
		param.put("dataInizioValidita", dataDiPartenza);
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		Query query = createQuery(jpql.toString(), param);
		return (Date) query.getSingleResult();
	}
	
	/**
	 * @param uidCausaleEP
	 * @param aliasR
	 */
	private Date ottieniMinValiditaInizioSiacRCausaleEpPdceContoOper(Integer uidCausaleEP, Date dataDiPartenza, Integer operazioneEpId) {
		final String methodName = "ottieniMinValiditaInizioByNomeEntitaCollegamento";
		//SIAC-6871
		if(uidCausaleEP == null || uidCausaleEP.intValue() == 0) {
			return null;
		}
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT MIN( rcoll.dataInizioValidita ) ")
			.append(" FROM  SiacRCausaleEpPdceContoOper rcoll ")
			.append(" WHERE rcoll.siacRCausaleEpPdceConto.siacTCausaleEp.causaleEpId = :causaleEpId ")
			.append(" AND rcoll.dataCancellazione IS NULL ")
			.append(" AND rcoll.dataInizioValidita >  :dataInizioValidita ")
			.append(" AND EXISTS ( ")
			.append(" FROM SiacDOperazioneEp det ")
			.append(" WHERE det.dataCancellazione IS NULL")
			.append(" AND det.siacDOperazioneEpTipo = rcoll.siacDOperazioneEp.siacDOperazioneEpTipo ")
			.append(" AND det.operEpId = :operEpId ")
			.append(" ) ");
		
		param.put("causaleEpId", uidCausaleEP);
		param.put("dataInizioValidita", dataDiPartenza);
		param.put("operEpId", operazioneEpId);
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		Query query = createQuery(jpql.toString(), param);
		return (Date) query.getSingleResult();
	}
	
	/**
	 * @param uidCausaleEP
	 * @param aliasR
	 */
	private Date ottieniMinValiditaInizioSiacRConciliazioneClasseCausaleEp(Integer uidCausaleEP, Date dataDiPartenza) {
		final String methodName = "ottieniMinValiditaInizioByNomeEntitaCollegamento";
		//SIAC-6871
		if(uidCausaleEP == null || uidCausaleEP.intValue() == 0) {
			return null;
		}
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT MIN( rcoll.dataInizioValidita ) ")
			.append(" FROM SiacRConciliazioneClasseCausaleEp rcoll ")
			.append(" WHERE rcoll.siacRCausaleEpPdceConto.siacTCausaleEp.causaleEpId = :causaleEpId ")
			.append(" AND rcoll.dataCancellazione IS NULL ")
			.append(" AND rcoll.dataInizioValidita >  :dataInizioValidita ");
		
		param.put("causaleEpId", uidCausaleEP);
		param.put("dataInizioValidita", dataDiPartenza);
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		Query query = createQuery(jpql.toString(), param);
		return (Date) query.getSingleResult();
	}
	


	@Override
	public Page<SiacTCausaleEp> ricercaSintetica(Integer enteProprietarioId, Integer anno, SiacDAmbitoEnum siacDAmbito, String codice, String descrizione,
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, SiacDCausaleEpStatoEnum siacDCausaleEpStatoEnum, Integer contoId,
			Integer tipoEventoId, Integer eventoId, Integer elementoPianoDeiContiId, Integer soggettoId, SiacDConciliazioneClasseEnum siacDConciliazioneClasse,
			Pageable pageable) {
	
		final String methodName = "ricercaSintetica";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaCausale(jpql, param, enteProprietarioId, anno, siacDAmbito, codice, descrizione, siacDCausaleEpTipoEnum, siacDCausaleEpStatoEnum, contoId, tipoEventoId,
				eventoId, elementoPianoDeiContiId, soggettoId, siacDConciliazioneClasse);
		
		jpql.append(" ORDER BY tce.siacDCausaleEpTipo.causaleEpTipoCode, tce.causaleEpCode ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	
	private void appendFilterDataInizioFineValidita(StringBuilder jpql,Map<String, Object> param, final String tableAlias, Integer anno) {
		jpql.append(" AND ( ");
		jpql.append("     DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno"+tableAlias+", ' 12 31'), 'YYYY MM DD') )  ");
		jpql.append("     BETWEEN  ");
		jpql.append("         DATE_TRUNC('day',"+tableAlias+".dataInizioValidita) ");
		jpql.append("     AND ");
		jpql.append("         COALESCE("+tableAlias+".dataFineValidita, DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno"+tableAlias+", ' 12 31'), 'YYYY MM DD') ) ) ");
		jpql.append(" ) ");
		
		param.put("anno"+tableAlias, anno);
		
	}

	/**
	 * SIAC-8169  
	 * A seguito della SIAC-4596 e'stato deprecato ed invalidato il legame tra 
	 * la SiacTCausaleEp e SiacRConciliazioneClasseCausaleEp, come collegamento
	 * tra le due tabelle e' stata aggiunta la tabella SiacRCausaleEpPdceConto
	 * 
	 * @param jpql
	 * @param param
	 * @param enteProprietarioId
	 * @param anno
	 * @param siacDAmbito
	 * @param causaleEpCode
	 * @param causaleEpDesc
	 * @param siacDCausaleEpTipoEnum
	 * @param siacDCausaleEpStatoEnum
	 * @param pdceContoId
	 * @param eventoTipoId
	 * @param eventoId
	 * @param elementoPianoDeiContiId
	 * @param soggettoId
	 * @param siacDConciliazioneClasse
	 */
	private void componiQueryRicercaSinteticaCausale(StringBuilder jpql, Map<String, Object> param,
			Integer enteProprietarioId,
			Integer anno,
			SiacDAmbitoEnum siacDAmbito,
			String causaleEpCode,
			String causaleEpDesc,
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum,
			SiacDCausaleEpStatoEnum siacDCausaleEpStatoEnum,
			Integer pdceContoId,
			Integer eventoTipoId,
			Integer eventoId,
			Integer elementoPianoDeiContiId,
			Integer soggettoId,
			SiacDConciliazioneClasseEnum siacDConciliazioneClasse) {
	
		jpql.append(" FROM SiacTCausaleEp tce ");
		jpql.append(" WHERE tce.dataCancellazione IS NULL ");
		jpql.append(" AND tce.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		
	    appendFilterDataInizioFineValidita(jpql, param, "tce", anno);
		
	    if(siacDAmbito!=null){
	    	jpql.append(" AND tce.siacDAmbito.ambitoCode = :ambitoCode ");
	    	param.put("ambitoCode", siacDAmbito.getCodice());
	    }
		
		if(StringUtils.isNotBlank(causaleEpCode)) {
			jpql.append(" AND ")
				.append(Utility.toJpqlSearchLike("tce.causaleEpCode", "CONCAT('%', :causaleEpCode, '%')"))
				.append(" ");
			param.put("causaleEpCode", causaleEpCode);
		}
		
		if(StringUtils.isNotBlank(causaleEpDesc)) {
			jpql.append(" AND ")
				.append(Utility.toJpqlSearchLike("tce.causaleEpDesc", "CONCAT('%', :causaleEpDesc, '%')"))
				.append(" ");
			param.put("causaleEpDesc", causaleEpDesc);
		}
		
		if(siacDCausaleEpTipoEnum != null && siacDCausaleEpTipoEnum.getCodice() != null) {
			jpql.append(" AND tce.siacDCausaleEpTipo.causaleEpTipoCode = :causaleEpTipoCode ");
			param.put("causaleEpTipoCode", siacDCausaleEpTipoEnum.getCodice());
		}
		
		if(siacDCausaleEpStatoEnum != null && siacDCausaleEpStatoEnum.getCodice() != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM tce.siacRCausaleEpStatos rces ");
			jpql.append("     WHERE rces.dataCancellazione IS NULL ");
			jpql.append("     AND rces.siacDCausaleEpStato.causaleEpStatoCode = :causaleEpStatoCode ");
			//jpql.append(     _AND_DataInizioFineValidita("rces"));
				
			appendFilterDataInizioFineValidita(jpql, param, "rces", anno);
			
			jpql.append(" ) ");
			
			param.put("causaleEpStatoCode", siacDCausaleEpStatoEnum.getCodice());
		}
		
		if(pdceContoId != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM tce.siacRCausaleEpPdceContos rcepc ");
			jpql.append("     WHERE rcepc.dataCancellazione IS NULL ");
			jpql.append("     AND rcepc.siacTPdceConto.pdceContoId = :pdceContoId ");
				
			appendFilterDataInizioFineValidita(jpql, param, "rcepc", anno);
			
			jpql.append(" ) ");
			
			param.put("pdceContoId", pdceContoId);
		}
		
		if(eventoTipoId != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM tce.siacREventoCausales rect ");
			jpql.append("     WHERE rect.dataCancellazione IS NULL ");
			jpql.append("     AND rect.siacDEvento.siacDEventoTipo.eventoTipoId = :eventoTipoId ");

			appendFilterDataInizioFineValidita(jpql, param, "rect", anno);
			
			jpql.append(" ) ");
			
			param.put("eventoTipoId", eventoTipoId);
		}
		
		if(eventoId != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM tce.siacREventoCausales rec ");
			jpql.append("     WHERE rec.dataCancellazione IS NULL ");
			jpql.append("     AND rec.siacDEvento.eventoId = :eventoId ");
			
			appendFilterDataInizioFineValidita(jpql, param, "rec", anno);
			
			jpql.append(" ) ");
			
			param.put("eventoId", eventoId);
		}
		
		if(elementoPianoDeiContiId != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM tce.siacRCausaleEpClasses rcec ");
			jpql.append("     WHERE rcec.dataCancellazione IS NULL ");
			jpql.append("     AND rcec.siacTClass.classifId = :classifId ");

			appendFilterDataInizioFineValidita(jpql, param, "rcec", anno);
			
			jpql.append(" ) ");
			
			param.put("classifId", elementoPianoDeiContiId);
		}
		
		if(soggettoId != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM tce.siacRCausaleEpSoggettos rces ");
			jpql.append("     WHERE rces.dataCancellazione IS NULL ");
			jpql.append("     AND rces.siacTSoggetto.soggettoId = :soggettoId ");
			
			appendFilterDataInizioFineValidita(jpql, param, "rces", anno);
			
			jpql.append(" ) ");
			
			param.put("soggettoId", soggettoId);
		}
		//SIAC-8169 aggiunta la join con la SiacRCausaleEpPdceConto
		if(siacDConciliazioneClasse != null && siacDConciliazioneClasse.getCodice() != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM SiacRConciliazioneClasseCausaleEp rcec ");
			jpql.append("     JOIN rcec.siacRCausaleEpPdceConto rpdceconto ");
			jpql.append("     WHERE rcec.dataCancellazione IS NULL ");
			jpql.append("     AND rpdceconto.dataCancellazione IS NULL ");
			jpql.append("     AND rpdceconto.siacTCausaleEp = tce ");
			jpql.append("     AND rcec.siacDConciliazioneClasse.concclaCode = :concclaCode ");
				
			appendFilterDataInizioFineValidita(jpql, param, "rcec", anno);
			appendFilterDataInizioFineValidita(jpql, param, "rpdceconto", anno);
			
			jpql.append(" ) ");
			
			param.put("concclaCode", siacDConciliazioneClasse.getCodice());
		}
		
		
	}

}
