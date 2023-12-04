/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.fattureelettroniche;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.entity.SirfelTCausale;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.sirfelser.model.FatturaFEL;

/**
 * The Class DocumentoSpesaDLTest.
 */
public class FELDozerTest extends BaseJunit4TestCase {
	
	
	@Autowired
	private Mapper mapper;
	
	@Test
	public void test() {
		SirfelTFattura fatt = new SirfelTFattura();
		
		fatt.setCodiceDestinatario("prova");
		List<SirfelTCausale> sirfelTCausales = new ArrayList<SirfelTCausale>();
		
		SirfelTCausale sirfelTCausale1 = new SirfelTCausale();
		sirfelTCausale1.setCausale("mia causale1 ");
		sirfelTCausales.add(sirfelTCausale1);
		
		SirfelTCausale sirfelTCausale2 = new SirfelTCausale();
		sirfelTCausale2.setCausale("mia causale2 ");
		sirfelTCausales.add(sirfelTCausale2);
		
		fatt.setSirfelTCausales(sirfelTCausales);
		
		
		FatturaFEL fatturaFEL = mapper.map(fatt, FatturaFEL.class, "SirfelTFattura_FatturaFEL");
		
		log.logXmlTypeObject(fatturaFEL, "ciao");
		

	}
	
}
