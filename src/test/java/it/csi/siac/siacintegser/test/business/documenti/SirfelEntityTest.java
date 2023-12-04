/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.test.business.documenti;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.siacbilser.integration.entity.SirfelTFatturaPK;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

public class SirfelEntityTest extends BaseJunit4TestCase {
	

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Test
	@Transactional
	public void sirfelEntityTest() {
		final String methodName = "sirfelEntityTest";
		
		
		SirfelTFatturaPK pk = new SirfelTFatturaPK();
		pk.setIdFattura(2);
//		SiacTEnteProprietario ente = new SiacTEnteProprietario();
//		ente.setUid(1);
		pk.setEnteProprietarioId(1);
		SirfelTFattura sirfelTFattura = entityManager.find(SirfelTFattura.class, pk);
		log.info(methodName, "numero: " + sirfelTFattura.getNumero());
		//entityManager.refresh(sirfelTFattura);
//		log.info(methodName, "denominazione ente: "+sirfelTFattura.getId().getSiacTEnteProprietario().getEnteDenominazione());
//		log.info(methodName, "login op ente: "+sirfelTFattura.getId().getSiacTEnteProprietario().getLoginOperazione());
		int size = sirfelTFattura.getSirfelTRiepilogoBenis().size();
		log.info(methodName, "elemento 0: "+ (size>0?sirfelTFattura.getSirfelTRiepilogoBenis().get(0):null));
		
		
		log.info(methodName, "anno protocollo: "+sirfelTFattura.getSirfelTProtocollo().getAnnoProtocollo());
		log.info(methodName, "nome file fattura: "+sirfelTFattura.getSirfelTPortaleFatture().getNomeFileFattura());
	}
	
}
