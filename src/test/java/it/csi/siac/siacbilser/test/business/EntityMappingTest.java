/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business;

import java.util.Date;

import org.dozer.Mapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;

// TODO: Auto-generated Javadoc
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {  "/spring/dozer.xml" })
/**
 * The Class EntityMappingTest.
 */
public class EntityMappingTest extends BaseJunit4TestCase {
	
	
	/** The mapper. */
	@Autowired
	private Mapper mapper;
	
	/**
	 * Map ente.
	 */
	@Test
	public void mapEnte(){
		final String methodName = "mapEnte";
		log.info(methodName, "start..");
		Ente ente = new Ente();
		ente.setUid(1);
		ente.setNome("mio ente");
		ente.setStato(StatoEntita.VALIDO);
		SiacTEnteProprietario enteProprietario = mapper.map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base.name());
		log.info(methodName, "# "+enteProprietario.getEnteDenominazione());
		log.info(methodName, "# "+enteProprietario.getUid());
		log.info(methodName, "# "+enteProprietario.getSiacDBilStatoOps().get(0).getBilStatoOpCode());
	}
	
	/**
	 * Map bilancio.
	 */
	@Test
	public void mapBilancio(){
		final String methodName = "mapBilancio";
		log.info(methodName, "start..");
		Bilancio bil = new Bilancio();
		bil.setUid(1);
		bil.setAnno(2010);
		bil.setDataCreazione(new Date());
		bil.setDataFineValidita(new Date());
		bil.setStato(StatoEntita.VALIDO);
		SiacTBil bilEntity = mapper.map(bil,SiacTBil.class, BilMapId.SiacTBil_Bilancio.name());
		log.info(methodName, "# "+bilEntity.getSiacTPeriodo().getAnno());
		log.info(methodName, "# "+bilEntity.getSiacTPeriodo().getUid());
		log.info(methodName, "# "+bilEntity.getUid());
		log.info(methodName, "# "+bilEntity.getDataCreazione());
		log.info(methodName, "# "+bilEntity.getDataFineValidita());
		log.info(methodName, "# "+bilEntity.getSiacRBilStatoOps().get(0).getSiacDBilStatoOp().getBilStatoOpCode());
	}
	
	/**
	 * Map integer.
	 */
	@Test
	public void mapInteger(){
		final String methodName = "mapInteger";
		log.info(methodName, "#######"+mapper.map(new Integer(5), String.class));
	}

}
