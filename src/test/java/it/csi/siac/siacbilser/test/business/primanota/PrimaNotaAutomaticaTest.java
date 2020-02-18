/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.primanota;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.primanota.DummyService;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaAutomaticaService;
import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaAutomatica;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

public class PrimaNotaAutomaticaTest extends BaseJunit4TestCase {
	
	
	@Autowired
	private ServiceExecutor se;
	
	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository; 
	
	@Test
	public void inseriscePrimaNotaAutomatica() {
		InseriscePrimaNotaAutomatica req = new InseriscePrimaNotaAutomatica();
		//req.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M", 71, 15));
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setUid(472);//27,66,617
		req.addRegistrazioneMovFin(registrazioneMovFin);
		
		se.setServiceName(InseriscePrimaNotaAutomaticaService.class.getSimpleName());
		se.executeServiceSuccess(InseriscePrimaNotaAutomaticaService.class, req);
		
	}
	
	@Test
	public void dummyTest() {
		executeDummyTest("1",1000);
		executeDummyTest("2",1000);
		executeDummyTest("3",1000);
		executeDummyTest("4",1000);
		executeDummyTest("5",1000);
		executeDummyTest("6",0);
		executeDummyTest("7",0);
		executeDummyTest("8",0);
		executeDummyTest("9",0);
		executeDummyTest("10",0);
		
		sleep(60000);
	}


	


	private void executeDummyTest(final String i, long startTimeout) {
		InseriscePrimaNotaAutomatica req = new InseriscePrimaNotaAutomatica();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.getRichiedente().setTokenOperazione(i);
		
		se.setServiceName(InseriscePrimaNotaAutomaticaService.class.getSimpleName());
		se.executeServiceSingleThreadPoolAsync(DummyService.class, req, 
				
//		new ResponseHandler<InseriscePrimaNotaAutomaticaResponse>() {
//
//			@Override
//			public void handleResponse(InseriscePrimaNotaAutomaticaResponse response) {
//				System.out.println("responseHandler ["+i+"]");
//				
//			}
//		}
//		,
		startTimeout);
	}
	
	
	private void sleep(long millis) {
		try {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> Resto in gentile attesa per "+millis+" ms!");
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> Ehi mi hanno fermato prima dei "+millis+" ms!!!!!!!!!!!!!!");
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> Fine attesa dei "+millis+" ms!!!!!!!!!!!!!!");
	}
	
	@Test
	public void testRicercaImportoAttualeImpegno() {
		BigDecimal importo = siacTMovgestTRepository.findImportoByMovgestId(47977, SiacDMovgestTsDetTipoEnum.Attuale.getCodice());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+importo);
	}
	


}
