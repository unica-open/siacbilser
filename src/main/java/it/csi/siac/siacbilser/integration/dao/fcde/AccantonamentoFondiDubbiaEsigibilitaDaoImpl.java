/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.fcde;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccantonamentoFondiDubbiaEsigibilitaDaoImpl extends JpaDao<SiacTAccFondiDubbiaEsig, Integer> implements AccantonamentoFondiDubbiaEsigibilitaDao {
	
	@Override
	public SiacTAccFondiDubbiaEsig create(SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig){
		Date now = new Date();
		siacTAccFondiDubbiaEsig.setDataModificaInserimento(now);
		siacTAccFondiDubbiaEsig.setUid(null);
		
		super.save(siacTAccFondiDubbiaEsig);
		return siacTAccFondiDubbiaEsig;
	}

	@Override
	public SiacTAccFondiDubbiaEsig update(SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig){
		Date now = new Date();
		siacTAccFondiDubbiaEsig.setDataModificaAggiornamento(now);
		super.update(siacTAccFondiDubbiaEsig);
		return siacTAccFondiDubbiaEsig;
	}

	@Override
	public SiacTAccFondiDubbiaEsig logicalDelete(SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig) {
		Date now = new Date();
		siacTAccFondiDubbiaEsig.setDataCancellazioneIfNotSet(now);
		return siacTAccFondiDubbiaEsig;
	}

	@Override
	public List<SiacTAccFondiDubbiaEsig> ricerca(Integer afdeBilId, boolean doOrder) {
		final String methodName = "ricerca";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT tafde ");
		jpql.append(" FROM SiacTAccFondiDubbiaEsig tafde ");
		if(doOrder) {
			jpql.append(" JOIN tafde.siacTBilElem.siacRBilElemClasses rbec ");
			jpql.append(" JOIN rbec.siacTClass tc_CATEGORIA ");
			jpql.append(" JOIN tc_CATEGORIA.siacRClassFamTreesFiglio rcft_CATEGORIA ");
			jpql.append(" JOIN rcft_CATEGORIA.siacTClassFamTree tcf_CATEGORIA ");
			jpql.append(" JOIN rcft_CATEGORIA.siacTClassPadre tc_TIPOLOGIA ");
			jpql.append(" JOIN tc_TIPOLOGIA.siacRClassFamTreesFiglio rcft_TIPOLOGIA ");
			jpql.append(" JOIN rcft_TIPOLOGIA.siacTClassPadre tc_TITOLO_ENTRATA ");
			jpql.append(" JOIN tc_TITOLO_ENTRATA.siacRClassFamTreesFiglio rcft_TITOLO_ENTRATA ");
		}
		jpql.append(" WHERE tafde.dataCancellazione IS NULL ");
		jpql.append(" AND tafde.dataFineValidita IS NULL ");
		jpql.append(" AND tafde.siacTAccFondiDubbiaEsigBil.afdeBilId = :afdeBilId ");
		if(doOrder) {
			jpql.append(" AND rbec.siacTClass.siacDClassTipo.classifTipoCode = 'CATEGORIA' ");
			jpql.append(" AND tcf_CATEGORIA.siacDClassFam.classifFamCode = '00003' ");
			Utility.addJpqlCondizioniValidita(jpql, "tafde.siacTBilElem");
			Utility.addJpqlCondizioniValidita(jpql, "rbec");
			Utility.addJpqlCondizioniValidita(jpql, "rcft_CATEGORIA");
			Utility.addJpqlCondizioniValidita(jpql, "rcft_TIPOLOGIA");
			Utility.addJpqlCondizioniValidita(jpql, "rcft_TITOLO_ENTRATA");
			jpql.append(" GROUP BY tafde.accFdeId, tafde.siacTBilElem, tafde.siacTBilElem.elemCode, tafde.siacTBilElem.elemCode2, tafde.siacTBilElem.elemCode3, tc_CATEGORIA.classifCode ");
			jpql.append(" ORDER BY " );
			jpql.append("  CAST(tc_CATEGORIA.classifCode as integer) ASC, ");
			jpql.append("  CAST(tafde.siacTBilElem.elemCode AS integer) ASC, ");
			jpql.append("  CAST(tafde.siacTBilElem.elemCode2 AS integer) ASC, ");
			jpql.append("  CAST(tafde.siacTBilElem.elemCode3 AS integer) ASC ");
		}

		param.put("afdeBilId", afdeBilId);
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getList(jpql.toString(), param);
	}
	
}
