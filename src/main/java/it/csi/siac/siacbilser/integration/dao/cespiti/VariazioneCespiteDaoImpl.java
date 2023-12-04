/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.dao.cespiti.jpql.EntitaCollegatePrimaNotaInventarioJpqlEnum;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiVariazionePrimaNota;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiVariazione;
import it.csi.siac.siaccespser.model.VariazioneCespite;

/**
 * The Class VariazioneCespiteDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VariazioneCespiteDaoImpl extends ExtendedJpaDao<SiacTCespitiVariazione, Integer> implements VariazioneCespiteDao {

	@Override
	public SiacTCespitiVariazione create(SiacTCespitiVariazione e) {
		Date now = new Date();
		e.setDataModificaInserimento(now);
		e.setUid(null);
		super.save(e);
		return e;
	}
	
	@Override
	public SiacTCespitiVariazione update(SiacTCespitiVariazione e) {
		Date now = new Date();
		// TODO: aggiornare le relazioni con le prime note?
		e.setDataModificaAggiornamento(now);
		return super.update(e);
	}

	@Override
	public SiacTCespitiVariazione delete(Integer cesVarId, String loginOperazione) {
		SiacTCespitiVariazione eAttuale = this.findById(cesVarId);
		Date now = new Date();
		
		eAttuale.setDataCancellazioneIfNotSet(now);
		eAttuale.setLoginOperazione(loginOperazione);
		// TODO: annullare le relazioni con le prime note?
		for (SiacRCespitiVariazionePrimaNota siacRCespitiVariazionePrimaNota : eAttuale.getSiacRCespitiVariazionePrimaNotas()) {
			siacRCespitiVariazionePrimaNota.setDataCancellazioneIfNotSet(now);
		}
		return super.update(eAttuale);
	}

	@Override
	public Page<SiacTCespitiVariazione> ricercaSinteticaVariazioneCespite(Integer enteProprietarioId, String cesVarAnno, Date cesVarData, String cesVarDesc, Boolean flgTipoVariazioneIncr,
			Integer cesId, String cesCode, String cesDesc, Boolean soggettoBeniCulturali, Boolean flgDonazioneRinvenimento, String numInventario, Date dataIngressoInventario, Integer cesBeneTipoId,
			String cesClassGiuCode, Pageable pageable) {
		
		final String methodName = "ricercaSinteticaVariazioneCespite";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaVariazioneCespite(jpql, param, enteProprietarioId, cesVarAnno, cesVarData, cesVarDesc, flgTipoVariazioneIncr, cesId, cesCode, cesDesc, soggettoBeniCulturali,
				flgDonazioneRinvenimento, numInventario, dataIngressoInventario, cesBeneTipoId, cesClassGiuCode);
		
		jpql.append(" ORDER BY tcv.cesVarAnno, tcv.cesVarData, tcv.cesVarId ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	private void componiQueryRicercaSinteticaVariazioneCespite(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId, String cesVarAnno, Date cesVarData, String cesVarDesc,
			Boolean flgTipoVariazioneIncr, Integer cesId, String cesCode, String cesDesc, Boolean soggettoBeniCulturali, Boolean flgDonazioneRinvenimento, String numInventario, Date dataIngressoInventario,
			Integer cesBeneTipoId, String cesClassGiuCode) {
		
		jpql.append(" FROM SiacTCespitiVariazione tcv ");
		jpql.append(" WHERE tcv.dataCancellazione IS NULL ");
		jpql.append(" AND tcv.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(StringUtils.isNotBlank(cesVarAnno)) {
			jpql.append(" AND tcv.cesVarAnno = :cesVarAnno ");
			param.put("cesVarAnno", cesVarAnno);
		}
		
		// cesVarData
		if(cesVarData != null) {
			jpql.append(" AND tcv.cesVarData = :cesVarData ");
			param.put("cesVarData", cesVarData);
		}
		// cesVarDesc
		if(StringUtils.isNotBlank(cesVarDesc)) {
			jpql.append(" AND ").append(Utility.toJpqlSearchLikePercent("tcv.cesVarDesc", ":cesVarDesc")).append(" ");
			param.put("cesVarDesc", cesVarDesc);
		}
		// flgTipoVariazioneIncr
		if(flgTipoVariazioneIncr != null) {
			jpql.append(" AND tcv.flgTipoVariazioneIncr = :flgTipoVariazioneIncr ");
			param.put("flgTipoVariazioneIncr", flgTipoVariazioneIncr);
		}
		// cesId
		if(cesId != null) {
			jpql.append(" AND tcv.siacTCespiti.cesId = :cesId ");
			param.put("cesId", cesId);
		}
		// cesCode
		if(StringUtils.isNotBlank(cesCode)) {
			jpql.append(" AND ").append(Utility.toJpqlSearchEqual("tcv.siacTCespiti.cesCode", ":cesCode")).append(" ");
			param.put("cesCode", cesCode);
		}
		// cesDesc
		if(StringUtils.isNotBlank(cesDesc)) {
			jpql.append(" AND ").append(Utility.toJpqlSearchLikePercent("tcv.siacTCespiti.cesDesc", ":cesDesc")).append(" ");
			param.put("cesDesc", cesDesc);
		}
		// soggettoBeniCulturali
		if(soggettoBeniCulturali != null) {
			jpql.append(" AND tcv.siacTCespiti.soggettoBeniCulturali = :soggettoBeniCulturali ");
			param.put("soggettoBeniCulturali", soggettoBeniCulturali);
		}
		// flgDonazioneRinvenimento
		if(flgDonazioneRinvenimento != null) {
			jpql.append(" AND tcv.siacTCespiti.flgDonazioneRinvenimento = :flgDonazioneRinvenimento ");
			param.put("flgDonazioneRinvenimento", flgDonazioneRinvenimento);
		}
		// numInventario
		if(StringUtils.isNotBlank(numInventario)) {
			jpql.append(" AND ").append(Utility.toJpqlSearchEqual("tcv.siacTCespiti.numInventario", ":numInventario")).append(" ");
			param.put("numInventario", numInventario);
		}
		// dataIngressoInventario
		if(dataIngressoInventario != null) {
			jpql.append(" AND tcv.siacTCespiti.dataIngressoInventario = :dataIngressoInventario ");
			param.put("dataIngressoInventario", dataIngressoInventario);
		}
		// cesBeneTipoId
		if(cesBeneTipoId != null) {
			jpql.append(" AND tcv.siacTCespiti.siacDCespitiBeneTipo.cesBeneTipoId = :cesBeneTipoId ");
			param.put("cesBeneTipoId", cesBeneTipoId);
		}
		// cesClassGiuCode
		if(cesClassGiuCode != null) {
			jpql.append(" AND tcv.siacTCespiti.siacDCespitiClassificazioneGiuridica.cesClassGiuCode = :cesClassGiuCode ");
			param.put("cesClassGiuCode", cesClassGiuCode);
		}
	}

	@Override
	public VariazioneCespite getVariazioneCespiteDaPrimaNotaTramiteJpql(Integer uidEntitaCollegata,	Integer uidCespiteCollegatoAdEntitaGenerante, Integer uidPrimaNota, Integer enteProprietarioId,	EntitaCollegatePrimaNotaInventarioJpqlEnum jpqlEnum) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void aggiornaStatoVariazione(SiacTCespitiVariazione siacTCespitiVariazione) {
		SiacTCespitiVariazione eAttuale = this.findById(siacTCespitiVariazione.getUid());		
		eAttuale.setSiacDCespitiVariazioneStato(siacTCespitiVariazione.getSiacDCespitiVariazioneStato());
		super.update(eAttuale);	
	}
	

}
