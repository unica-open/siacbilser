/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.classificatorebil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.GestioneClassificatoreBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

// TODO: Auto-generated Javadoc
/**
 * The Class GestioneClassificatoreBilImplTest.
 */
public class GestioneClassificatoreBilImplTest extends
		BaseJunit4TestCase {

	/** The gestione classificatore bil. */
	@Autowired
	private GestioneClassificatoreBil gestioneClassificatoreBil;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() {
		/*gestioneClassificatoreBil = (GestioneClassificatoreBil) applicationContext
				.getBean("gestioneClassificatoreBil");*/
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	/**
	 * Test find classificatori generici by tipo elemento bil.
	 */
	@Test
	public void testFindClassificatoriGenericiByTipoElementoBil() {
		
		LeggiClassificatoriGenericiByTipoElementoBil params = new LeggiClassificatoriGenericiByTipoElementoBil();
		params.setAnno(2019);
		params.setIdEnteProprietario(1);
		params.setTipoElementoBilancio("CAP-UG");
		LeggiClassificatoriGenericiByTipoElementoBilResponse res = gestioneClassificatoreBil.findClassificatoriGenericiByTipoElementoBil(params);
		Assert.assertNull("Classificatori Generici trovati", res);
		
	}
	
	/**
	 * Test find classificatori con livello by tipo elemento bil.
	 */
	@Test
	public void testFindClassificatoriConLivelloByTipoElementoBil() {
		
		LeggiClassificatoriByTipoElementoBil params = new LeggiClassificatoriByTipoElementoBil();
		params.setAnno(2020);
		params.setIdEnteProprietario(2);
		params.setTipoElementoBilancio("CAP-UG");
		
		LeggiClassificatoriByTipoElementoBilResponse res = gestioneClassificatoreBil.findClassificatoriConLivelloByTipoElementoBil(params);
		Assert.assertNull("Classificatori con livello trovati", res);
		
	}
	
	/**
	 * Test find classificatori by id padre.
	 */
	@Test
	public void testFindClassificatoriByIdPadre() {
		LeggiClassificatoriBilByIdPadre params = new LeggiClassificatoriBilByIdPadre();
		params.setAnno(2013);
		params.setIdEnteProprietario(1);
		params.setIdPadre(2);
		
		LeggiClassificatoriBilByIdPadreResponse res = gestioneClassificatoreBil.findClassificatoriByIdPadre(params);
		Assert.assertNull("Classificatori Gerarchici trovati", res);
		
	}
	
	/**
	 * Test find tree piano dei conti.
	 */
	@Test
	public void testFindTreePianoDeiConti() {
		LeggiTreePianoDeiConti request = new LeggiTreePianoDeiConti();
		request.setAnno(2013);
		request.setIdCodificaPadre(119);
		request.setIdEnteProprietario(1);
		//request.setFamigliaTreeCodice("00008");
		LeggiTreePianoDeiContiResponse response = gestioneClassificatoreBil.findTreePianoDeiConti(request);
		
		System.out.println(ReflectionToStringBuilder.reflectionToString(response, ToStringStyle.MULTI_LINE_STYLE));
		
		assertNotNull("Nessuna response ottenuta", response);
		assertNotNull("Tree non inizializzato", response.getTreeElementoPianoDeiConti());
		assertTrue("Lista vuota", !response.getTreeElementoPianoDeiConti().isEmpty());
		for(ElementoPianoDeiConti e : response.getTreeElementoPianoDeiConti()) {
			System.out.println(e.getUid() + " || " + e.getCodice() + " || " + e.getDescrizione());
		}
		
	}

}
