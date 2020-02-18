/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.classificatorebil;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriBilByIdPadreService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriByTipoElementoBilService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriGenericiByTipoElementoBilService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiTreePianoDeiContiService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;

// TODO: Auto-generated Javadoc
/**
 * The Class GestioneClassificatoreBilImplMVTest.
 */
public class GestioneClassificatoreBilImpl2Test 
		extends	BaseJunit4TestCase {

	/** The leggi classificatori generici by tipo elemento bil service. */
	@Autowired
	private LeggiClassificatoriGenericiByTipoElementoBilService leggiClassificatoriGenericiByTipoElementoBilService;
	
	/** The leggi classificatori by tipo elemento bil service. */
	@Autowired
	private LeggiClassificatoriByTipoElementoBilService leggiClassificatoriByTipoElementoBilService;
	
	/** The leggi tree piano dei conti service. */
	@Autowired
	private LeggiTreePianoDeiContiService leggiTreePianoDeiContiService;
	
	/** The leggi classificatori bil by id padre service. */
	@Autowired
	private LeggiClassificatoriBilByIdPadreService leggiClassificatoriBilByIdPadreService;
	
	
	
	/**
	 * Test find tree piano dei conti service.
	 */
	@Test
	public void testFindTreePianoDeiContiService() {
		LeggiTreePianoDeiConti params = new LeggiTreePianoDeiConti();
		params.setAnno(2013);
		params.setIdEnteProprietario(1);
		params.setIdCodificaPadre(5952);
		//params.setFamigliaTreeCodice("00008");
		params.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		LeggiTreePianoDeiContiResponse res = leggiTreePianoDeiContiService.executeService(params);
		Assert.assertNotNull("Classificatori Gerarchici trovati", res);
		
	}
	
	
	
	/**
	 * Test find classificatori by id padre.
	 */
	@Test
	public void testFindClassificatoriByIdPadre() {
		LeggiClassificatoriBilByIdPadre params = new LeggiClassificatoriBilByIdPadre();
		params.setAnno(2013);
		params.setIdEnteProprietario(1);
		params.setIdPadre(34);
		params.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		LeggiClassificatoriBilByIdPadreResponse res = leggiClassificatoriBilByIdPadreService.executeService(params);
		System.out.println("getClassificatoriCofog: "+res.getClassificatoriClassificazioneCofog());
		
		Assert.assertNotNull("Classificatori Gerarchici trovati", res);
		
	}
	
	
	/**
	 * Test find classificatori generici by tipo elemento bil.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testFindClassificatoriGenericiByTipoElementoBil()
			throws Throwable {
		try
		{
			LeggiClassificatoriGenericiByTipoElementoBil req = new LeggiClassificatoriGenericiByTipoElementoBil();
			
			req.setAnno(2017);
			req.setIdEnteProprietario(2);
			req.setTipoElementoBilancio("CAP-UG");
			
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			
			LeggiClassificatoriGenericiByTipoElementoBilResponse res = leggiClassificatoriGenericiByTipoElementoBilService.executeService(req);
			
			Assert.assertNotNull("Classificatori Generici trovati", res);
			
			
			for(ClassificatoreGenerico c : res.getClassificatoriGenerici3()){
				System.out.println(c);
			}
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Test find classificatori con livello by tipo elemento bil.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testFindClassificatoriConLivelloByTipoElementoBil()
			throws Throwable {
		try
		{
			LeggiClassificatoriByTipoElementoBil params = new LeggiClassificatoriByTipoElementoBil();
			params.setAnno(2015);
			params.setIdEnteProprietario(1);
			params.setTipoElementoBilancio("CAP-UG");
			
			params.setRichiedente(getRichiedenteByProperties("consip","regp"));
			
			LeggiClassificatoriByTipoElementoBilResponse res = leggiClassificatoriByTipoElementoBilService.executeService(params);
			
			Assert.assertNotNull("Classificatori con livello trovati", res);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	

}
