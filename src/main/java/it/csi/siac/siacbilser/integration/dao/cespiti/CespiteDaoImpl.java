/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import java.util.ArrayList;
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
import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCespitiClassificazioneGiuridicaEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOperazioneEpEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class CespiteDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CespiteDaoImpl extends JpaDao<SiacTCespiti, Integer> implements CespiteDao {
	
	public SiacTCespiti create(SiacTCespiti e){
		Date now = new Date();
		e.setDataModificaInserimento(now);		
		e.setUid(null);		
		super.save(e);
		return e;
	}

	public SiacTCespiti update(SiacTCespiti e){		
		SiacTCespiti eAttuale = this.findById(e.getUid());		
		Date now = new Date();
		e.setDataInizioValidita(eAttuale.getDataInizioValidita());
		e.setLoginCreazione(eAttuale.getLoginCreazione());
		e.setDataModifica(now);
		entityManager.flush();		
		super.update(e);
		return e;
	}
	
	@Override
	public SiacTCespiti delete(int uidCespite, String loginOperazione) {		
		SiacTCespiti eAttuale = this.findById(uidCespite);		
		Date now = new Date();		
		eAttuale.setDataCancellazioneIfNotSet(now);
		eAttuale.setLoginOperazione(loginOperazione);
		eAttuale.setLoginCancellazione(loginOperazione);
		super.update(eAttuale);		
		return eAttuale;
	}
	
	@Override
	public Page<SiacTCespiti>ricercaSinteticaCespite(Integer enteProprietarioId, String cespiteCodice, String cespiteDescrizione, Integer uidTipoBene, String codiceContoPatrimoniale, SiacDCespitiClassificazioneGiuridicaEnum classCode,
			Boolean cespiteFlagSoggettoTutelaBeniCulturali, Boolean cespiteFlgDonazioneRinvenimento, Boolean cespiteFlagStatoBene, String cespiteNumeroInventario, Date cespiteDataAccessoInventario,
			String cespiteUbicazione, Integer uidDismissione,Date cespiteDataCessazione,Integer uidCategoria,Integer uidDettaglioAnteprima,  SiacDOperazioneEpEnum siacDOperazioneEpEnum, Integer numInventarioNumeroDa, 
			Integer numInventarioNumeroA,Boolean escludiCollegatiADismissione, Boolean conPianoAmmortamentoCompleto, 
			Integer massimoAnnoAmmortato,Integer uidMovimentoDettaglio, Date dataInizioValiditaFiltro, Pageable pageable) {
		
		final String methodName = "ricercaSinteticaCespite";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaCespiti(jpql, param, enteProprietarioId, cespiteCodice, cespiteDescrizione, uidTipoBene, codiceContoPatrimoniale, classCode, cespiteFlagSoggettoTutelaBeniCulturali,
				cespiteFlgDonazioneRinvenimento, cespiteFlagStatoBene, cespiteNumeroInventario, cespiteDataAccessoInventario, cespiteUbicazione, uidDismissione, cespiteDataCessazione, uidCategoria,uidDettaglioAnteprima, siacDOperazioneEpEnum, numInventarioNumeroDa, numInventarioNumeroA, escludiCollegatiADismissione, 
				conPianoAmmortamentoCompleto, massimoAnnoAmmortato, uidMovimentoDettaglio,dataInizioValiditaFiltro);
		
		jpql.append(" ORDER BY d.cesCode ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	private void componiQueryRicercaSinteticaCespiti(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId, 
			String cespiteCodice, String  cespiteDescrizione,
			Integer uidTipoBene, String codiceContoPatrimoniale, 
			SiacDCespitiClassificazioneGiuridicaEnum  cgcCodice, 
			Boolean cespiteFlagSoggettoTutelaBeniCulturali, Boolean cespiteFlgDonazioneRinvenimento, Boolean cespiteFlagStatoBene,
			String cespiteNumeroInventario, Date cespiteDataAccessoInventario, String cespiteUbicazione, Integer uidDismissione, Date cespiteDataCessazione,
			Integer uidCategoria,
			Integer uidDettaglioAnteprima, SiacDOperazioneEpEnum siacDOperazioneEpEnum, 
			Integer numInventarioNumeroDa, Integer numInventarioNumeroA, Boolean escludiCollegatiADismissione, 
			Boolean conPianoAmmortamentoCompleto, Integer massimoAnnoAmmortato, 
			Integer uidMovimentoDettaglio,Date dataInizioValiditaFiltro) {
		
		componiQueryBaseCespiti(jpql, param, enteProprietarioId);

		if(StringUtils.isNotBlank(cespiteCodice)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.cesCode", "CONCAT('%', :cespiteCodice, '%')") + " ");
			param.put("cespiteCodice", cespiteCodice);
		}
		if(StringUtils.isNotBlank(cespiteDescrizione)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.cesDesc", "CONCAT('%', :cespiteDescrizione, '%')") + " ");
			param.put("cespiteDescrizione", cespiteDescrizione);
		}
		if(StringUtils.isNotBlank(cespiteUbicazione)) {
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.ubicazione", "CONCAT('%', :cespiteUbicazione, '%')") + " ");
			param.put("cespiteUbicazione", cespiteUbicazione);
		}

		if(uidTipoBene!=null && uidTipoBene.intValue() !=0){
			jpql.append(" AND d.siacDCespitiBeneTipo.cesBeneTipoId = :cesBeneTipoId ");
			param.put("cesBeneTipoId", uidTipoBene);
		}
		
		if(uidCategoria != null && uidCategoria.intValue() != 0) {
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRCespitiBeneTipoContoPatrCat r ");
			jpql.append(" WHERE r.siacDCespitiCategoria.cescatId = :cescatId ");
			jpql.append(" AND r.siacDCespitiBeneTipo = d.siacDCespitiBeneTipo ");
			jpql.append(" AND r.dataCancellazione IS NULL ");
			jpql.append(" AND (r.dataFineValidita IS NULL OR :dataInputCategoria <= r.dataFineValidita) "); 
			jpql.append(" AND (r.dataInizioValidita <= :dataInputCategoria) ");
			jpql.append(" ) ");
			
			param.put("cescatId", uidCategoria);	
			param.put("dataInputCategoria", dataInizioValiditaFiltro);
		}
		if(StringUtils.isNotBlank(codiceContoPatrimoniale)) {
			jpql.append(" AND EXISTS( ");
			jpql.append(" FROM SiacRCespitiBeneTipoContoPatrCat rcbt ");
			jpql.append(" WHERE rcbt.dataCancellazione IS NULL ");
			jpql.append(" AND rcbt.siacDCespitiBeneTipo = d.siacDCespitiBeneTipo ");
			jpql.append(" AND (rcbt.dataFineValidita IS NULL OR :dataInput <= rcbt.dataFineValidita) "); 
			jpql.append(" AND (rcbt.dataInizioValidita <= :dataInput) ");
			jpql.append(" AND rcbt.pdceContoPatrimonialeCode = :pdceContoPatrimonialeCode");
			jpql.append(" )");
			
			param.put("pdceContoPatrimonialeCode", codiceContoPatrimoniale);
			param.put("dataInput",dataInizioValiditaFiltro);
		}
		
		if(StringUtils.isNotBlank(cespiteNumeroInventario)){
			jpql.append(" AND d.numInventario = :numInventario ");
			param.put("numInventario", cespiteNumeroInventario);
		}
		
		if(cespiteFlagSoggettoTutelaBeniCulturali!=null){
			jpql.append(" AND d.soggettoBeniCulturali = :soggettoBeniCulturali ");
			param.put("soggettoBeniCulturali", cespiteFlagSoggettoTutelaBeniCulturali);
		}
		
		if(cespiteFlgDonazioneRinvenimento!=null){
			jpql.append(" AND d.flgDonazioneRinvenimento = :flgDonazioneRinvenimento ");
			param.put("flgDonazioneRinvenimento", cespiteFlgDonazioneRinvenimento);
		}
		
		if(cespiteFlagStatoBene!=null){
			jpql.append(" AND d.flgStatoBene = :cespiteFlagStatoBene ");
			param.put("cespiteFlagStatoBene", cespiteFlagStatoBene);
		}
		
		if(cespiteDataAccessoInventario!=null){
			jpql.append(" AND DATE_TRUNC('day', CAST(d.dataIngressoInventario AS date)) = DATE_TRUNC('day', CAST(:dataIngressoInventario AS date)) ");
			param.put("dataIngressoInventario", cespiteDataAccessoInventario);	
		}		
		
		if(uidDismissione != null &&  uidDismissione.intValue() != 0) {
			jpql.append("AND EXISTS ( FROM SiacTCespitiDismissioni tcd ");
			jpql.append("    WHERE d.siacTCespitiDismissioni = tcd " );
			jpql.append("    AND tcd.dataCancellazione IS NULL ");
			jpql.append("    AND tcd.cesDismissioniId = :cesDismissioniId ");
			jpql.append(" ) ") ;
			
			param.put("cesDismissioniId", uidDismissione);
		}
		
		if(cespiteDataCessazione!=null){
			
			jpql.append("AND EXISTS ( FROM SiacTCespitiDismissioni tcd ");
			jpql.append("    WHERE d.siacTCespitiDismissioni = tcd " );
			jpql.append("    AND tcd.dataCancellazione IS NULL ");
			jpql.append("    AND DATE_TRUNC('day', CAST(d.siacTCespitiDismissioni.dataCessazione AS date)) = DATE_TRUNC('day', CAST(:cespiteDataCessazione AS date))");
			jpql.append(" ) ") ;
			
			param.put("cespiteDataCessazione", cespiteDataCessazione);
		}
		
		if(uidDettaglioAnteprima != null &&  uidDettaglioAnteprima.intValue() != 0 && siacDOperazioneEpEnum != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRCespitiCespitiElabAmmortamentiDett srccead ");
			//TODO MODTABAMM: modifica tabella ammortamenti: valutare
//			jpql.append(" WHERE srccead.siacTCespiti = d ");
			//
			jpql.append(" WHERE srccead.siacTCespitiAmmortamentoDett.siacTCespitiAmmortamento.siacTCespiti = d ");
			jpql.append(" AND srccead.dataCancellazione IS NULL");
			jpql.append(" AND srccead.siacTCespitiElabAmmortamentiDett").append(siacDOperazioneEpEnum.getDescrizione()).append(".elabDettId = :elabDettId ");
			jpql.append(" ) ");
			param.put("elabDettId", uidDettaglioAnteprima);
		}
		
		if(cgcCodice!=null){
			jpql.append(" AND d.siacDCespitiClassificazioneGiuridica.cesClassGiuCode = :codice ");
			param.put("codice", cgcCodice.getCodice());	
		}
		if(numInventarioNumeroDa != null) {
			jpql.append(" AND d.numInventarioNumero >= :numInventarioNumeroDa ");
			param.put("numInventarioNumeroDa", numInventarioNumeroDa);
		}
		if(numInventarioNumeroA != null) {
			jpql.append(" AND d.numInventarioNumero <= :numInventarioNumeroA ");
			param.put("numInventarioNumeroA", numInventarioNumeroA);
		}
		
		if(Boolean.TRUE.equals(escludiCollegatiADismissione)) {
			jpql.append(" AND d.siacTCespitiDismissioni IS NULL");
		}
		
		componiQueryPianoAmmortamentoCompleto(jpql, param, conPianoAmmortamentoCompleto);
		componiQueryMassimoAnnoAmmortato(jpql, param, massimoAnnoAmmortato);
		
		if(uidMovimentoDettaglio != null && uidMovimentoDettaglio.intValue() != 0) {
			jpql.append("AND EXISTS ( ");
			jpql.append(" FROM SiacRCespitiMovEpDet rccme ");
			jpql.append(" WHERE rccme.dataCancellazione IS NULL  ");
			jpql.append(" AND rccme.siacTCespiti = d ");
			jpql.append(" AND rccme.siacTMovEpDet.movepDetId = :movepDetId ");
			jpql.append(" AND rccme.siacTMovEpDet.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("movepDetId", uidMovimentoDettaglio);
		}
		
	}

	/**
	 * @param jpql
	 * @param param
	 * @param massimoAnnoAmmortato
	 */
	private void componiQueryMassimoAnnoAmmortato(StringBuilder jpql, Map<String, Object> param,
			Integer massimoAnnoAmmortato) {
		if(massimoAnnoAmmortato != null) {
			jpql.append(" AND ");
			jpql.append(" (NOT EXISTS ( ");
			jpql.append("     FROM SiacTCespitiAmmortamento tamu ");
			jpql.append("     WHERE tamu.dataCancellazione IS NULL ");
			jpql.append("     AND tamu.siacTCespiti = d ) ");
			jpql.append(" OR EXISTS ( ");
			jpql.append("     FROM SiacTCespitiAmmortamento tamu ");
			jpql.append("     WHERE tamu.siacTCespiti = d ");
			jpql.append("     AND tamu.dataCancellazione IS NULL ");
			jpql.append("     AND (tamu.cesAmmUltimoAnnoReg IS NULL OR tamu.cesAmmUltimoAnnoReg < :massimoAnnoAmmortato )");
			jpql.append(" )	)");
			param.put("massimoAnnoAmmortato", massimoAnnoAmmortato);
		}
	}

	/**
	 * @param jpql
	 * @param param
	 * @param conPianoAmmortamentoCompleto
	 */
	private void componiQueryPianoAmmortamentoCompleto(StringBuilder jpql, Map<String, Object> param,
			Boolean conPianoAmmortamentoCompleto) {
		if(conPianoAmmortamentoCompleto != null) {
			//BUHUHUHUHUHHUHUHUHU
//			jpql.append("AND d.valoreIniziale <>  (");
//			jpql.append( "    SELECT COALESCE(tam.cesAmmImportoTotReg, 0) ");
			jpql.append(" AND (EXISTS ( ");
			jpql.append("     FROM SiacTCespitiAmmortamento tam ");
			jpql.append("     WHERE tam.siacTCespiti = d ");
			jpql.append("     AND tam.cesAmmCompleto = :cesAmmCompleto ");
			jpql.append("     AND tam.dataCancellazione IS NULL ");
			jpql.append("   ) ");
			if(Boolean.FALSE.equals(conPianoAmmortamentoCompleto)) {
				jpql.append(" OR NOT EXISTS ( ");
				jpql.append("     FROM SiacTCespitiAmmortamento tam ");
				jpql.append("     WHERE tam.siacTCespiti = d ");
				jpql.append("     AND tam.dataCancellazione IS NULL ");
				jpql.append("   ) ");
			}
			jpql.append("   ) ");
			param.put("cesAmmCompleto", conPianoAmmortamentoCompleto);
		}
	}

	/**
	 * @param jpql
	 * @param param
	 * @param enteProprietarioId
	 */
	private void componiQueryBaseCespiti(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId) {
		jpql.append(" FROM SiacTCespiti d ");
		jpql.append(" WHERE ");
		jpql.append(" d.dataCancellazione IS NULL ");
		jpql.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
	}

	@Override
	public List<SiacTPrimaNota> ricercaScrittureCespite(Integer enteProprietarioId,Integer cespiteUid, String statoOperativoCode, String statoAccettazionePrimaNotaProv, String statoAccettazionePrimaNotaDef, String ambitoCode) {
		
		final String methodName = "ricercaSinteticaCespite";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaScrittureCespiti(jpql, param, enteProprietarioId, cespiteUid, statoOperativoCode, statoAccettazionePrimaNotaProv, statoAccettazionePrimaNotaDef,ambitoCode);
		
		jpql.append(" ORDER BY rpn.siacTPrimaNota.pnotaNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<SiacTPrimaNota> res = query.getResultList();
		return res;
		
	}

	private void componiQueryRicercaSinteticaScrittureCespiti(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId, Integer cespiteUid,String statoOperativoPrimaNotaCode,  String statoAccettazionePrimaNotaProv, String statoAccettazionePrimaNotaDefCode, String ambitoCode) {
		jpql.append(" SELECT rpn.siacTPrimaNota ");
		jpql.append(" FROM SiacRCespitiPrimaNota rpn ");
		jpql.append(" WHERE ");
		jpql.append(" rpn.dataCancellazione IS NULL ");
		jpql.append(" AND rpn.siacTPrimaNota.dataCancellazione IS NULL ");
		jpql.append(" AND rpn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);

		if(cespiteUid != null){
			jpql.append(" AND rpn.siacTCespiti.cesId = :cesId ");
			param.put("cesId", cespiteUid);
		}
		if(StringUtils.isNotBlank(statoOperativoPrimaNotaCode)) {
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRPrimaNotaStato rpns ");
			jpql.append(" WHERE rpns.dataCancellazione IS NULL ");
			jpql.append(" AND rpns.siacTPrimaNota = rpn.siacTPrimaNota ");
			jpql.append(" AND rpns.siacDPrimaNotaStato.pnotaStatoCode = :pnotaStatoCode ");
			jpql.append(" ) ");
			param.put("pnotaStatoCode", statoOperativoPrimaNotaCode);
		}
		
		if(StringUtils.isNotBlank(statoAccettazionePrimaNotaDefCode)) {
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRPnProvAccettazioneStato rap ");
			jpql.append(" WHERE rap.dataCancellazione IS NULL ");
			jpql.append(" AND rap.siacTPrimaNota = rpn.siacTPrimaNota ");
			jpql.append(" AND rap.siacDPnProvAccettazioneStato.pnStaAccDefCode = :pnStaAccProvCode ");
			jpql.append(" ) ");
			param.put("pnStaAccDefCode", statoAccettazionePrimaNotaDefCode);
		}
		
		if(StringUtils.isNotBlank(statoAccettazionePrimaNotaDefCode)) {
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRPnDefAccettazioneStato rap ");
			jpql.append(" WHERE rap.dataCancellazione IS NULL ");
			jpql.append(" AND rap.siacTPrimaNota = rpn.siacTPrimaNota ");
			jpql.append(" AND rap.siacDPnDefAccettazioneStato.pnStaAccDefCode = :pnStaAccDefCode ");
			jpql.append(" ) ");
			param.put("pnStaAccDefCode", statoAccettazionePrimaNotaDefCode);
		}
		
		if(StringUtils.isNotBlank(ambitoCode)) {
			jpql.append(" AND rpn.siacTPrimaNota.siacDAmbito.ambitoCode = :ambitoCode");
			param.put("ambitoCode", ambitoCode);
		}
	}
	
	/**
	 * Carica uid cespiti da ammortare.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param massimoAnnoAmmortato the massimo anno ammortato
	 * @return the list
	 */
	public List<Integer> caricaUidCespitiDaAmmortare(Integer enteProprietarioId, String cespiteCodice, String cespiteDescrizione, Integer uidTipoBene, String codiceContoPatrimoniale, SiacDCespitiClassificazioneGiuridicaEnum classCode,
			Boolean cespiteFlagSoggettoTutelaBeniCulturali,	Boolean cespiteFlgDonazioneRinvenimento, Boolean cespiteFlagStatoBene, String cespiteNumeroInventario, Date cespiteDataAccessoInventario,
			String cespiteUbicazione, Integer uidDismissione, Date cespiteDataCessazione, Integer numInventarioNumeroDa,  Integer numInventarioNumeroA,
			Boolean conPianoAmmortamentoCompleto, Boolean escludiCollegatiADismissione,	Integer massimoAnnoAmmortato, Date dataInizioValiditaFiltro){
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		jpql.append(" SELECT d.cesId ");
		componiQueryRicercaSinteticaCespiti(jpql, param, enteProprietarioId, cespiteCodice, cespiteDescrizione, uidTipoBene, codiceContoPatrimoniale, classCode, cespiteFlagSoggettoTutelaBeniCulturali,
				cespiteFlgDonazioneRinvenimento, cespiteFlagStatoBene, cespiteNumeroInventario, cespiteDataAccessoInventario, cespiteUbicazione, uidDismissione, cespiteDataCessazione, null,null,null, numInventarioNumeroDa, numInventarioNumeroA, escludiCollegatiADismissione, 
				conPianoAmmortamentoCompleto, massimoAnnoAmmortato, null, dataInizioValiditaFiltro);
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<Integer> res = query.getResultList();
		return res;
	}
	
	@Override
	public List<SiacTCespiti> ricercaCespiteDaPrimaNotaCogeInv(Integer pnInventarioUid,Integer enteProprietarioId) {		
		final String methodName = "ricercaCespiteDaPrimaNota";		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();	
		List<SiacTCespiti> result = new ArrayList<SiacTCespiti>();
				
	    jpql.append(" SELECT    rcmed.siacTCespiti ");
		jpql.append("     FROM  SiacRCespitiMovEpDet rcmed ");
		jpql.append("     WHERE rcmed.dataCancellazione IS NULL ");
		jpql.append("     AND rcmed.siacTMovEpDet.siacTMovEp.siacTPrimaNota.pnotaId = :pnInventarioUid ");				
		jpql.append("     AND rcmed.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");	
		
		param.put("pnInventarioUid", pnInventarioUid);
		param.put("enteProprietarioId", enteProprietarioId);
	

		log.info(methodName, "JPQL to execute: " + jpql.toString());		
		Query query= createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<SiacTCespiti> res = query.getResultList();
		result.addAll(res);
		

		
		return result;
	}
	
	@Override
	public List<SiacTCespiti> ricercaCespiteDaPrimaNota(Integer pnUid,Integer enteProprietarioId) {		
		final String methodName = "ricercaCespiteDaPrimaNota";		
		StringBuilder jpql = new StringBuilder();
		StringBuilder jpqlDismissione = new StringBuilder();
		StringBuilder jpqlAmmortamento = new StringBuilder();
		StringBuilder jpqlVariazione = new StringBuilder();
		
		
		Map<String, Object> param = new HashMap<String, Object>();	
		List<SiacTCespiti> result = new ArrayList<SiacTCespiti>();
				
		componiQueryRicercaCespiteDaPrimaNota(jpql,param, pnUid,enteProprietarioId);
		log.info(methodName, "JPQL to execute: " + jpql.toString());		
		Query query= createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<SiacTCespiti> res = query.getResultList();
		result.addAll(res);
		
		componiQueryRicercaCespiteDaPrimaNotaVariazione(jpqlVariazione,param, pnUid,enteProprietarioId);
		log.info(methodName, "JPQL to execute: " + jpqlVariazione.toString());		
		Query queryVariazione = createQuery(jpqlVariazione.toString(), param);
		@SuppressWarnings("unchecked")
		List<SiacTCespiti> resVariazione= queryVariazione.getResultList();
		result.addAll(resVariazione);
		
		componiQueryRicercaCespiteDaPrimaNotaDismissione(jpqlDismissione, param, pnUid, enteProprietarioId);
		log.info(methodName, "JPQL to execute: " + jpqlDismissione.toString());	
		Query queryDismissione = createQuery(jpqlDismissione.toString(), param);
		@SuppressWarnings("unchecked")
		List<SiacTCespiti> resDismissione = queryDismissione.getResultList();
		result.addAll(resDismissione);
		
		componiQueryRicercaCespiteDaPrimaNotaAmmortamento(jpqlAmmortamento, param, pnUid, enteProprietarioId);
		log.info(methodName, "Ammortamento JPQL to execute: " + jpqlAmmortamento.toString());	
		Query queryAmmortamento = createQuery(jpqlAmmortamento.toString(), param);
		@SuppressWarnings("unchecked")
		List<SiacTCespiti> resAmmortamento = queryAmmortamento.getResultList();		
		result.addAll(resAmmortamento);
		
		return result;
	}

	
/**
 * 
 * @param jpql
 * @param param
 * @param pnUid
 * @param enteProprietarioId
 */
 private void componiQueryRicercaCespiteDaPrimaNota (StringBuilder jpql, Map<String, Object> param, Integer pnUid,Integer enteProprietarioId) {

	 jpql.append(" SELECT    rcpn.siacTCespiti ");
		jpql.append("     FROM  SiacRCespitiPrimaNota rcpn ");
		jpql.append("     WHERE rcpn.dataCancellazione IS NULL ");
		jpql.append("     AND rcpn.siacTPrimaNota.pnotaId = :pnUid ");				
		jpql.append("     AND rcpn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");	
		param.put("pnUid", pnUid);
		param.put("enteProprietarioId", enteProprietarioId);
	} 
 
 /**
  * 
  * @param jpql
  * @param param
  * @param pnUid
  * @param enteProprietarioId
  */
  private void componiQueryRicercaCespiteDaPrimaNotaVariazione (StringBuilder jpql, Map<String, Object> param, Integer pnUid,Integer enteProprietarioId) {

 		jpql.append(" SELECT    rcvpn.siacTCespitiVariazione.siacTCespiti ");
 		jpql.append("     FROM  SiacRCespitiVariazionePrimaNota rcvpn ");
 		jpql.append("     WHERE rcvpn.dataCancellazione IS NULL ");
 		jpql.append("       AND rcvpn.siacTPrimaNota.pnotaId = :pnUid ");	
 		jpql.append("       AND rcvpn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");		
 		param.put("pnUid", pnUid);
 		param.put("enteProprietarioId", enteProprietarioId);
 	} 
  
  /**
   * 
   * @param jpql
   * @param param
   * @param pnUid
   * @param enteProprietarioId
   */
   private void componiQueryRicercaCespiteDaPrimaNotaDismissione (StringBuilder jpql, Map<String, Object> param, Integer pnUid,Integer enteProprietarioId) {

  		
  		jpql.append(" SELECT    tc ");
  		jpql.append("     FROM  SiacRCespitiDismissioniPrimaNota rcdpn,SiacTCespiti tc ");
  		jpql.append("     WHERE rcdpn.dataCancellazione IS NULL ");
  		jpql.append("     AND tc.siacTCespitiDismissioni =  rcdpn.siacTCespitiDismissioni");	
  		jpql.append("     AND rcdpn.siacTPrimaNota.pnotaId = :pnUid ");			
  		jpql.append("     AND rcdpn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");	
  		jpql.append("  	  AND EXISTS ( FROM SiacTCespiti tc2 ");
  		jpql.append(" 				WHERE tc = rcdpn.siacTCespitiAmmortamentoDett.siacTCespitiAmmortamento.siacTCespiti ");
  		jpql.append("  				AND tc.dataCancellazione IS NULL  ");
  		jpql.append("  				AND tc.cesId = tc2.cesId  ");
  		jpql.append(" 	  )  ");
  		param.put("pnUid", pnUid);
  		param.put("enteProprietarioId", enteProprietarioId);
  	} 
   
   /**
    * 
    * @param jpql
    * @param param
    * @param pnUid
    * @param enteProprietarioId
    */
    private void componiQueryRicercaCespiteDaPrimaNotaAmmortamento (StringBuilder jpql, Map<String, Object> param, Integer pnUid,Integer enteProprietarioId) {
   		jpql.append(" SELECT    rtcad.siacTCespitiAmmortamento.siacTCespiti ");
   		jpql.append("     FROM  SiacTCespitiAmmortamentoDett rtcad ");
   		jpql.append("     WHERE rtcad.dataCancellazione IS NULL ");
   		jpql.append("     AND rtcad.siacTPrimaNota.pnotaId = :pnUid ");		
   		jpql.append("     AND rtcad.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");				
   		param.put("pnUid", pnUid);
   		param.put("enteProprietarioId", enteProprietarioId);
   	} 
}
