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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiDismissioni;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class CespiteDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DismissioneCespiteDaoImpl extends JpaDao<SiacTCespitiDismissioni, Integer> implements DismissioneCespiteDao {
	
	public SiacTCespitiDismissioni create(SiacTCespitiDismissioni e){
		Date now = new Date();
		e.setDataModificaInserimento(now);		
		e.setUid(null);		
		super.save(e);
		return e;
	}

	public SiacTCespitiDismissioni update(SiacTCespitiDismissioni e){		
		SiacTCespitiDismissioni eAttuale = this.findById(e.getUid());		
		Date now = new Date();
		e.setDataInizioValidita(eAttuale.getDataInizioValidita());
		e.setDataModifica(now);
		entityManager.flush();		
		super.update(e);
		return e;
	}
	
	@Override
	public void delete(SiacTCespitiDismissioni entity) {		
		SiacTCespitiDismissioni eAttuale = this.findById(entity.getUid());		
		Date now = new Date();
		
		eAttuale.setDataCancellazioneIfNotSet(now);
		eAttuale.setLoginCancellazione(entity.getLoginCancellazione());
		eAttuale.setLoginOperazione(entity.getLoginOperazione());
		super.update(eAttuale);		
	}
	

	private void componiQueryRicercaSinteticaDismissioneCespite(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId, Integer annoElenco,
			Integer numeroElenco, String descrizione, Integer uidAttoAmministrativo, Date dataCessazione,
			Integer uidEvento, Integer uidCausaleEP, String descrizioneStatoCessazione, Integer uidCespite) {
		
		jpql.append(" FROM SiacTCespitiDismissioni d ");
		jpql.append(" WHERE ");
		jpql.append(" d.dataCancellazione IS NULL ");
		jpql.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(annoElenco != null) {
			jpql.append(" AND d.elencoDismissioniAnno = :elencoDismissioniAnno ");
			param.put("elencoDismissioniAnno", annoElenco);
		}
		
		if(numeroElenco != null) {
			jpql.append(" AND d.elencoDismissioniNumero = :elencoDismissioniNumero ");
			param.put("elencoDismissioniNumero", numeroElenco);
		}
		
		if(descrizione != null) {
			jpql.append(" AND ")
			.append(Utility.toJpqlSearchLike("d.cesDismissioniDesc", "CONCAT('%', :cesDismissioniDesc, '%')"))
			.append(" ");
			param.put("cesDismissioniDesc", descrizione);
		}
		
		if(uidAttoAmministrativo != null) {
			jpql.append(" AND d.siacTAttoAmm.attoammId = :attoammId ");
			param.put("attoammId", uidAttoAmministrativo);
		}
		if(dataCessazione != null) {
			jpql.append(" AND d.dataCessazione = :dataCessazione ");
			param.put("dataCessazione", dataCessazione);
		}
		if(uidEvento != null) {
			jpql.append(" AND d.siacDEvento.eventoId = :eventoId ");
			param.put("eventoId", uidEvento);
		}
		if(uidCausaleEP != null) {
			jpql.append(" AND d.siacTCausaleEp.causaleEpId = :causaleEpId ");
			param.put("causaleEpId", uidCausaleEP);
		}
		if(descrizioneStatoCessazione != null) {
			jpql.append(" AND ")
				.append(Utility.toJpqlSearchLike("d.dismissioniDescStato", "CONCAT('%', :dismissioniDescStato, '%')"))
				.append(" ");
			param.put("dismissioniDescStato", descrizioneStatoCessazione);
		}
		if(uidCespite != null) { 
			jpql.append(" AND EXISTS (");
			jpql.append("   FROM SiacTCespiti tc ");
			jpql.append("   WHERE tc.dataCancellazione IS NULL ");
			jpql.append("   AND tc.siacTCespitiDismissioni = d ");
			jpql.append("   AND tc.cesId = :cesId");
			jpql.append(" ) ");
			param.put("cesId", uidCespite);
		}
	}

	@Override
	public Page<SiacTCespitiDismissioni> ricercaSinteticaCespite(Integer enteProprietarioId, Integer annoElenco,
			Integer numeroElenco, String descrizione, Integer uidAttoAmministrativo, Date dataCessazione,
			Integer uidEvento, Integer uidCausaleEP, String descrizioneStatoCessazione, Integer uidCespite,Pageable pageable) {
		final String methodName = "ricercaSinteticaTipoBeneCespite";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaDismissioneCespite(jpql, param, enteProprietarioId, annoElenco,
				numeroElenco, descrizione, uidAttoAmministrativo, dataCessazione,
				uidEvento, uidCausaleEP, descrizioneStatoCessazione, uidCespite);
		
		jpql.append(" ORDER BY d.elencoDismissioniAnno, d.elencoDismissioniNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	public void aggiornaStato(SiacTCespitiDismissioni siacTDismissioneCespite) {
		SiacTCespitiDismissioni eAttuale = this.findById(siacTDismissioneCespite.getUid());		

		eAttuale.setSiacDCespitiDismissioniStato(siacTDismissioneCespite.getSiacDCespitiDismissioniStato());
		eAttuale.setLoginModifica(siacTDismissioneCespite.getLoginOperazione());
		super.update(eAttuale);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTPrimaNota> ricercaPrimeNoteGenerateDaDismissione(Integer uidDismissione, Integer uidCespite, String statoCode) {
		final String methodName = "ricercaPrimeNoteGenerateDaDismissione";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();  

		jpql.append(" SELECT tp ");
		jpql.append(" FROM SiacTPrimaNota tp ");
		jpql.append(" WHERE tp.dataCancellazione IS NULL ");
		
		jpql.append(" AND ( EXISTS (");
		jpql.append("         FROM SiacRCespitiDismissioniPrimaNota rcpn ");
		jpql.append("         WHERE rcpn.dataCancellazione IS NULL ");
		jpql.append("         AND rcpn.siacTPrimaNota = tp ");
		jpql.append("         AND rcpn.siacTPrimaNota = tp ");
		jpql.append("         AND rcpn.siacTCespitiDismissioni.cesDismissioniId = :cesDismissioniId ");
		jpql.append("     ) OR EXISTS (");
		jpql.append("         FROM SiacTCespitiAmmortamentoDett tcea, SiacTCespiti tc ");
		jpql.append("         WHERE tcea.dataCancellazione IS NULL ");
		jpql.append("         AND tcea.siacTCespitiAmmortamento.siacTCespiti = tc ");
		jpql.append("         AND tc.dataCancellazione IS NULL ");
		jpql.append("         AND tcea.siacTPrimaNota = tp ");
		jpql.append("         AND tc.siacTCespitiDismissioni.cesDismissioniId = :cesDismissioniId ");
		
		if (uidCespite != null) {
			jpql.append(appendInnerConditionCespite(param, uidCespite));
		}
		jpql.append("     ) ");
		jpql.append("  ) ");
		param.put("cesDismissioniId", uidDismissione);
		
		if(StringUtils.isNotBlank(statoCode)) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM SiacRPrimaNotaStato rpns ");
			jpql.append("     WHERE rpns.dataCancellazione IS NULL ");
			jpql.append("     AND rpns.siacTPrimaNota = tp ");
			jpql.append("     AND rpns.siacDPrimaNotaStato.pnotaStatoCode = :pnotaStatoCode ");
			jpql.append(" ) ");
			param.put("pnotaStatoCode", statoCode);
		}

		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query =  createQuery(jpql.toString(), param);
		
		return query.getResultList();
	}


	/**
	 * @param param
	 * @param uidCespite
	 */
	private String appendInnerConditionCespite(Map<String, Object> param, Integer uidCespite) {
		if(uidCespite == null || uidCespite.intValue() == 0) {
			return "";
		}
		param.put("cesId", uidCespite);
		return " AND tcea.siacTCespitiAmmortamento.siacTCespiti.cesId = :cesId ";	
	}

}
