/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacfin2ser.model.AccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.SubAccertamentoModelDetail;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AccertamentoBilDad  extends MovimentoGestioneBilDad<Accertamento>  {
	
	public Accertamento findMiniminalAccertamentoDataByUid(Integer uid) {
		SiacTMovgestT siacTMovgestT = findTestataByUidMovimento(uid);
		if(siacTMovgestT == null) {
			return null;
		}
		return mapNotNull(siacTMovgestT.getSiacTMovgest(), Accertamento.class, BilMapId.SiacTMovgest_Accertamento);
	}
	
	public Accertamento findAccertamentoByUid(Integer uid, AccertamentoModelDetail... modelDetails) {
		SiacTMovgestT siacTMovgestT = findTestataByUidMovimento(uid);
		if(siacTMovgestT == null) {
			return null;
		}
		return mapNotNull(siacTMovgestT.getSiacTMovgest(), Accertamento.class, BilMapId.SiacTMovgest_Accertamento_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	public SubAccertamento findMiniminalSubAccertamentoDataByUid(Integer uid) {
		SiacTMovgestT siacTMovgestT = siacTMovgestTRepository.findOne(uid);
		return mapNotNull(siacTMovgestT, SubAccertamento.class, BilMapId.SiacTMovgestT_SubAccertamento_ModelDetail);
	}
	
	public SubAccertamento findSubAccertamentoByUid(Integer uid, SubAccertamentoModelDetail... modelDetails) {
		SiacTMovgestT siacTMovgestT = siacTMovgestTRepository.findOne(uid);
		return mapNotNull(siacTMovgestT, SubAccertamento.class, BilMapId.SiacTMovgestT_SubAccertamento_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	public BigDecimal ottieniDisponibilitaIncassare(Accertamento accertamento, SubAccertamento subAccertamento, Integer annoBilancio) {
		Integer uid = null;
		// Per l'Accertamento devo ricavare l'uid della testata
		if(accertamento != null && accertamento.getUid() != 0) {
			SiacTMovgestT siacTMovgestT = findTestataByUidMovimento(accertamento.getUid());
			if(siacTMovgestT == null) {
				return null;
			}
			uid = siacTMovgestT.getUid();
		}
		
		if(subAccertamento != null && subAccertamento.getUid() != 0) {
			uid = subAccertamento.getUid();
		}
		
		if(uid == null) {
			return null;
		}
		return calcolaDisponibilita(uid, "fnc_siac_disponibilitaincassaremovgest");
	}
	
	public BigDecimal ottieniDisponibilitaIncassareDaTestata(Integer uidTestata) {
		return calcolaDisponibilita(uidTestata, "fnc_siac_disponibilitaliquidaremovgest");
	}
	
	public Accertamento findAccertamentoQuota(int uid) {
		final String methodName = "findAccertamentoQuota";
		SiacTMovgest siacTMovgest = siacTMovgestTRepository.findSiacTMovgestBySubdoc(uid);
		if(siacTMovgest == null){
			log.debug(methodName, "nessun accertamento trovato");
			return null;
		}
		Accertamento accertamento = new Accertamento();
		accertamento.setUid(siacTMovgest.getUid());
		accertamento.setAnnoMovimento(siacTMovgest.getMovgestAnno());
		accertamento.setNumero(siacTMovgest.getMovgestNumero());
		log.debug(methodName, " accertamento trovato: " + accertamento.getUid());
		return accertamento;
	}

	public SubAccertamento findSubAccertamentoQuota(int uid) {
		final String methodName = "findSubAccertamentoQuota";
		SiacTMovgestT siacTMovgestTs = siacTMovgestTRepository.findSiacTMovgestTSSubimpegnoBySubdoc(uid);
		if(siacTMovgestTs == null){
			log.debug(methodName, "nessun subaccertamento trovato");
			return null;
		}
		SubAccertamento subaccertamento = new SubAccertamento();
		subaccertamento.setUid(siacTMovgestTs.getUid());
		subaccertamento.setAnnoMovimento(siacTMovgestTs.getSiacTMovgest().getMovgestAnno());
		subaccertamento.setNumero(siacTMovgestTs.getSiacTMovgest().getMovgestNumero());
		log.debug(methodName, " subaccertamento trovato: " + subaccertamento.getUid());
		return subaccertamento;
	}
	


}
