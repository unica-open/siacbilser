/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.ProvvisorioDiCassaBilDao;
import it.csi.siac.siacbilser.integration.dao.SiacTProvCassaBilRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * Data access delegate di un provvisorio di cassa. Per le funzionalit&agrave; 
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ProvvisorioBilDad extends BaseDadImpl {
	
	
	/** The siac t subdoc repository. */
	@Autowired
	private ProvvisorioDiCassaBilDao provvisorioDiCassaBilDao;
	
	@Autowired
	private SiacTProvCassaBilRepository siacTProvCassaBilRepository;
	
	public BigDecimal calcolaImportoDaRegolarizzareProvvisorio(ProvvisorioDiCassa provvisorio) {
		return provvisorioDiCassaBilDao.calcolaImportoDaRegolarizzare(provvisorio.getUid());
	}

	public String ottieniDescrizioneCausaleProvvisorio(ProvvisorioDiCassa provvisorioCassa) {
		return siacTProvCassaBilRepository.findCausaleByUidProvCassa(provvisorioCassa.getUid());
	}
	
	public ProvvisorioDiCassa findProvvisorioBySubdocId(Integer uidSubdoc) {
		SiacTProvCassa siacTProvCassa = siacTProvCassaBilRepository.findSiacTProvCassaBySubdocId(uidSubdoc);
		if(siacTProvCassa == null){
			return null;
		}
		ProvvisorioDiCassa provvisorio = new ProvvisorioDiCassa();
		provvisorio.setUid(siacTProvCassa.getUid());
		return provvisorio;
	}

	public ProvvisorioDiCassa findProvvisorioById(Integer uid) {
		SiacTProvCassa siacTProvCassa = siacTProvCassaBilRepository.findOne(uid);
		return mapNotNull(siacTProvCassa, ProvvisorioDiCassa.class, BilMapId.SiacTProvCassa_ProvvisorioDiCassa);
	}
	
	

}
