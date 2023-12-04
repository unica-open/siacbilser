/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.classificatorefin;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.dtomapping.converter.YearDateConverter;
import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.entity.SiacCodifica;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;

@Component
@Transactional
public class SiacTClassFinDaoImpl extends AbstractDao<SiacTClassFin, Integer> implements SiacTClassFinDao {

	/**
	 * Ricerca i classificatori generici, quelli senza livello, per tipo
	 * elemento di Bilancio e proprietario
	 * 
	 * @param anno
	 * @param entePropritario
	 * @param codiceTipoElemBilancio
	 * @return
	 */
	@Override
	public List<SiacCodifica> findClassificatoriGenericiByTipoMovimentoGestione(
			int anno, int enteProprietarioId, String codiceTipoMovimentoGestione) {
		List<SiacCodifica> ritorno = null;

//		Date startAnnoEsercizioDate = new YearDateConverter().convert(anno);
//		Date endAnnoEsercizioDate = DateUtils.addDays(DateUtils.addYears(startAnnoEsercizioDate, 1), -1);
		String jpql = "";

		   // IMPEGNO E ACCERTAMENTO
		  
		 jpql = "select c  from SiacCodifica c " 
				+ " where c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
				+ " AND c.padre is null"
				+ " AND c.codificaFamiglia is null"
				+ " AND  EXISTS ( "
				+ " select tipoMovGest from SiacDMovgestTipoFin tipoMovGest, "
				+ " IN(tipoMovGest.tipoMovimentoGestione) r "
				+ " where tipoMovGest.movgestTipoCode=:codiceTipoMovimentoGestione "
				+ " AND r.tipoClassificatore=c.tipoClassificatore "
				+ " AND c.siacTEnteProprietario=c.tipoClassificatore.siacTEnteProprietario "
				+ " AND c.siacTEnteProprietario=tipoMovGest.siacTEnteProprietario "
				+ ")"
				+ " AND c.dataCancellazione is null "
				+ " AND now() between c.dataInizioValidita and coalesce(c.dataFineValidita,now()) "
				//+ " AND " + DataValiditaUtil.validitaForQuery("c","startAnnoEsercizioDate","endAnnoEsercizioDate")
				
				+ " order by c.tipoClassificatore.codice, c.codice";
		
		Query query = entityManager.createQuery(jpql);


		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("codiceTipoMovimentoGestione", codiceTipoMovimentoGestione);
//		query.setParameter("startAnnoEsercizioDate", startAnnoEsercizioDate);
//		query.setParameter("endAnnoEsercizioDate", endAnnoEsercizioDate);
		
		@SuppressWarnings("unchecked")
		List<SiacCodifica> dtos = (List<SiacCodifica>) query.getResultList();
		ritorno = dtos;
		
		//Termino restituendo l'oggetto di ritorno: 
        return ritorno;
	}
	
	/**
	 * Ricerca i classificatori generici, quelli senza livello, per tipo
	 * elemento di Bilancio e proprietario
	 * 
	 * @param anno
	 * @param entePropritario
	 * @param codiceTipoElemBilancio
	 * @return
	 */
	@Override
	public List<SiacCodifica> findClassificatoriGenericiByTipoOrdinativoGestione(int anno, int enteProprietarioId, String codiceTipoOrdinativoGestione){
		List<SiacCodifica> ritorno = null;

//		Date startAnnoEsercizioDate = new YearDateConverter().convert(anno);
//		Date endAnnoEsercizioDate = DateUtils.addDays(DateUtils.addYears(startAnnoEsercizioDate, 1), -1);
		
		String jpql = "";

		 jpql = "SELECT c FROM SiacCodifica c " 
				+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
				+ " AND c.padre is null"
				+ " AND c.codificaFamiglia is null"
				+ " AND  EXISTS ( "
				+ " SELECT tipoOrdGest FROM SiacDOrdinativoTipoFin tipoOrdGest, "
				+ " IN(tipoOrdGest.siacROrdinativoTipoClassTips) r "
				+ " WHERE tipoOrdGest.ordTipoCode = :codiceTipoOrdinativoGestione "
				+ " AND r.siacDClassTipo = c.tipoClassificatore "
				+ " AND c.siacTEnteProprietario = c.tipoClassificatore.siacTEnteProprietario "
				+ " AND c.siacTEnteProprietario = tipoOrdGest.siacTEnteProprietario "
				+ ")"
				+ " AND c.dataCancellazione is null "
				+ " AND now() between c.dataInizioValidita and coalesce(c.dataFineValidita,now()) "
				//+ " AND " + DataValiditaUtil.validitaForQuery("c","startAnnoEsercizioDate","endAnnoEsercizioDate")
				+ " order by c.tipoClassificatore.codice";
		
		Query query = entityManager.createQuery(jpql);

		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("codiceTipoOrdinativoGestione", codiceTipoOrdinativoGestione);
//		query.setParameter("startAnnoEsercizioDate", startAnnoEsercizioDate);
//		query.setParameter("endAnnoEsercizioDate", endAnnoEsercizioDate);
		
		@SuppressWarnings("unchecked")
		List<SiacCodifica> dtos = (List<SiacCodifica>) query.getResultList();
		ritorno = dtos;
	
		//Termino restituendo l'oggetto di ritorno: 
        return ritorno;
	}

	/**
	 * Ricerca le codifiche per tipo elemento movimento gestione ricerca le
	 * codifiche con 1 livello, vedi Missione
	 * 
	 * @param anno
	 * @param entePropritario
	 * @param codiceTipoElemBilancio
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacCodifica> findClassificatoriGerarchiciILivelloByTipoMovimentoGestione(
			int anno, int enteProprietarioId, String codiceTipoMovimentoGestione) {
		List<SiacCodifica> dtos = null;

		Date startAnnoEsercizioDate = new YearDateConverter().convert(anno);
		Date endAnnoEsercizioDate = DateUtils.addDays(
				DateUtils.addYears(startAnnoEsercizioDate, 1), -1);
		Query query = entityManager
				.createQuery("select c  from SiacCodifica c "
						+ " where c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						+ " AND c.padre is null"
						+ " AND c.codificaFamiglia is not null"
						+ " AND  EXISTS ( "
						+ " select tipoMovGest from SiacDMovgestTipoFin tipoMovGest, "
						+ " IN(tipoMovGest.tipoMovimentoGestione) r "
						+ " where tipoMovGest.movgestTipoCode=:codiceTipoMovimentoGestione "
						+ " AND r.tipoClassificatore=c.tipoClassificatore "
						+ " AND c.siacTEnteProprietario=c.tipoClassificatore.siacTEnteProprietario "
						+ " AND c.siacTEnteProprietario=tipoMovGest.siacTEnteProprietario "
						+ ")" + " AND c.dataCancellazione is null "

						+ " AND now() between c.dataInizioValidita and coalesce(c.dataFineValidita,now()) "
//						+ DataValiditaUtil.validitaForQuery("c",
//								"startAnnoEsercizioDate",
//								"endAnnoEsercizioDate")

						+ " order by c.tipoClassificatore.codice");

		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("codiceTipoMovimentoGestione",
				codiceTipoMovimentoGestione);
//		query.setParameter("startAnnoEsercizioDate", startAnnoEsercizioDate);
//		query.setParameter("endAnnoEsercizioDate", endAnnoEsercizioDate);

		dtos = (List<SiacCodifica>) query.getResultList();

		//Termino restituendo l'oggetto di ritorno: 
        return dtos;
	}

	/**
	 * Ricerca le codifiche per idPadre e proprietarioId, quelle che in
	 * r_class_fam_tree hanno una dipendenza da un'altra codifica (vedi
	 * Programma, Cofog etc...)
	 * 
	 * @param anno
	 * @param entePropritario
	 * @param idPadreCodifica
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SiacCodifica> findClassificatoriGerarchiciByIdPadre(int anno,
			int enteProprietarioId, int idPadreCodifica) {
		List<SiacCodifica> dtos = null;

//		Date startAnnoEsercizioDate = new YearDateConverter().convert(anno);
//		Date endAnnoEsercizioDate = DateUtils.addDays(
//				DateUtils.addYears(startAnnoEsercizioDate, 1), -1);

		Query query = entityManager
				.createQuery("select c  from SiacCodifica c "
						+ " where "
						+ " c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId and"
						+ " c.padre.uid= :idPadreCodifica and "
						+ " c.dataCancellazione is null and "
						+ " now() between c.dataInizioValidita and coalesce(c.dataFineValidita,now()) ");

		//						+ DataValiditaUtil.validitaForQuery("c",
//								"startAnnoEsercizioDate",
//								"endAnnoEsercizioDate"));

		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("idPadreCodifica", idPadreCodifica);

		// dal 01/01/dell'anno passato in input - al 31/12/ dell'anno
		// passato in input
//		query.setParameter("startAnnoEsercizioDate", startAnnoEsercizioDate);
//		query.setParameter("endAnnoEsercizioDate", endAnnoEsercizioDate);

		dtos = (List<SiacCodifica>) query.getResultList();

		//Termino restituendo l'oggetto di ritorno: 
        return dtos;
	}

	/**
	 * Carica l'alberto dei classificatori gerarchici a partire dalla famiglia indicata
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacCodifica> findTreeClassificatoriGerarchiciByFamigliaId(Integer anno, Integer enteProprietarioId,String codicefamigliaTree, Integer idCodificaPadre) {
		List<SiacCodifica> dtos = null;

		StringBuilder querybuilder = new StringBuilder();
		StringBuilder querybuilderfrom = new StringBuilder();

		querybuilderfrom.append("select distinct c from SiacCodifica c ");
		if (idCodificaPadre != null)
			querybuilderfrom.append(", IN (c.codificheFiglie) cf ");

		querybuilder
				.append(" where ")
				.append(" c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId and")

				// Correzione: non si filtra per idFamiglia ma su
				// codiceFamiglia, perche' l'id sul multiente non e'
				// univoco
				.append(" c.codificaFamiglia.codiceCodificaFamigliaDto.codice = :codicefamigliaTree and ")
				.append(" c.codificaFamiglia.codiceCodificaFamigliaDto.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId and ");

		// Da gestire per caricare il conto economico senza partire da un
		// classificatoe (relazione in r_class)
		// macroaggregato e pdc
		if (idCodificaPadre != null)
			querybuilder
					.append(" c.padre.uid is not null and cf.codifica.uid = :idCodificaPadre and ");
		else
			querybuilder.append(" c.padre.uid is null and ");

		querybuilder.append(" c.dataCancellazione is null and ")
					.append(" now() between c.dataInizioValidita and coalesce(c.dataFineValidita,now()) ")

				// .append(" c.dataInizioValidita <= :startAnnoEsercizioDate and ")
				// .append(" (c.dataFineValidita is null or c.dataFineValidita <= :endAnnoEsercizioDate)")

//				.append(DataValiditaUtil.validitaForQuery("c",
//						"startAnnoEsercizioDate", "endAnnoEsercizioDate"))

				.append(" order by c.uid");

		Query query = entityManager.createQuery(querybuilderfrom.append(
				querybuilder.toString()).toString());

//		Date startAnnoEsercizioDate = new YearDateConverter().convert(anno);
//		Date endAnnoEsercizioDate = DateUtils.addDays(
//				DateUtils.addYears(startAnnoEsercizioDate, 1), -1);
//		query.setParameter("startAnnoEsercizioDate", startAnnoEsercizioDate);
//		query.setParameter("endAnnoEsercizioDate", endAnnoEsercizioDate);
		
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("codicefamigliaTree", codicefamigliaTree);

		if (idCodificaPadre != null)
			query.setParameter("idCodificaPadre", idCodificaPadre);

		dtos = (List<SiacCodifica>) query.getResultList();


		//Termino restituendo l'oggetto di ritorno: 
        return dtos;
	}

}
