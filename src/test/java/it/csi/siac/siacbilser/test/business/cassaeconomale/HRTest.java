/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cassaeconomale;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaRichiesteAnticipoMissioniNonErogateService;
import it.csi.siac.siacbilser.business.service.hr.HRServiceDelegate;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaRichiesteAnticipoMissioniNonErogate;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaRichiesteAnticipoMissioniNonErogateResponse;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.InsMD060Type;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioneConsuntivoType;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioneContabileType;

public class HRTest extends BaseJunit4TestCase {
	
	@Autowired
	private ServiceExecutor se;

	@Autowired
	protected HRServiceDelegate iMissioniService;

	
	@Test
	public void vm140(){
		
		List<MissioneContabileType> mcts = iMissioniService.vm140();
		
		int i = 0;
		for(MissioneContabileType mct : mcts){
			System.out.println(++i+" - "+JAXBUtility.marshall(mct));
		}
	}
	
	@Test
	public void vm160(){
		
		List<MissioneConsuntivoType> mcts = iMissioniService.vm160();
		
		int i = 0;
		for(MissioneConsuntivoType mct : mcts){
			System.out.println(++i+" - "+JAXBUtility.marshall(mct));
		}
	}
	
	@Test 
	public void insMD060(){
		InsMD060Type insMD060Type = new InsMD060Type();
		
		insMD060Type.setIdMissione(755124); //AHHHHHHHHH!!! nel servizio vm140 e updMissioni idMissione esterna è una Stringa!!!!!!
		
		insMD060Type.setProgressivo(2); //ma sarà sempre un intero!?!??!
		
		insMD060Type.setCassa("1"); //');"); SQL Injection!!!!!!!!
		insMD060Type.setAnnoMovimento("2015");
		insMD060Type.setCodVoce("1");
		insMD060Type.setNrososp(1);
		insMD060Type.setNumMovimento(1);//??
		
		insMD060Type.setDataImpostazioneStato("02/12/2015"); //dd-mm-yyyy //Obbligatorio sennò si schianta
		insMD060Type.setDataMissione("02/12/2015"); //dd-mm-yyyy //Obbligatorio sennò si schianta
		insMD060Type.setDataMovimento("02/12/2015"); //dd-mm-yyyy //Obbligatorio sennò si schianta
		
		insMD060Type.setFlagTotalizzatore("S" ); //MASSIMO UN CARATTERE!!! Il null viene tradotto in "null" che son 4 caratteri!
		insMD060Type.setItaEst("I");//MASSIMO UN CARATTERE!!! Il null viene tradotto in "null" che son 4 caratteri!
		insMD060Type.setStato("1");
		
		insMD060Type.setImporto(new Float(101.12));
		
		iMissioniService.insMD060(insMD060Type);
	}
	
	
	@Test
	public void ricercaSinteticaRichiestaEconomale(){
		
		RicercaRichiesteAnticipoMissioniNonErogate req = new RicercaRichiesteAnticipoMissioniNonErogate();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		//req.setCaricaDettaglioGiustificativi(true);
		se.setServiceName("HRTest");
		RicercaRichiesteAnticipoMissioniNonErogateResponse res = se.executeServiceSuccess(RicercaRichiesteAnticipoMissioniNonErogateService.class, req);
		System.out.println(JAXBUtility.marshall(res));
	}
	
	
	
	
	
	
}
