/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.pcc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.pcc.frontend.webservice.msg.InviaOperazioniPCC;
import it.csi.siac.pcc.frontend.webservice.msg.InviaOperazioniPCCResponse;
import it.csi.siac.pcc.marc.schema.callbackservicetypes_1.AggiornamentoStatoRichiestaRequest;
import it.csi.siac.pcc.marc.schema.marccommontypes_1.ResultType;
import it.csi.siac.pcc.marc.schema.marccommontypes_1.StatoRichiestaType;
import it.csi.siac.pcc.marc.services.webservices_1_0.MarcWSPortType;
import it.csi.siac.siacbilser.business.service.pcc.AggiornaStatoRichiestaService;
import it.csi.siac.siacbilser.business.service.pcc.AggiornaStatoRichiestaService.AggiornaStatoRichiesta;
import it.csi.siac.siacbilser.business.service.pcc.InviaOperazioniPCCService;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.tesoro.fatture.DatiRispostaOperazioneContabileQueryTipo;
import it.tesoro.fatture.ProxyOperazioneContabileRichiestaTipo;
import it.tesoro.fatture.QueryOperazioneContabileRispostaTipo;

public class InviaOperazioniPCCTest extends BaseJunit4TestCase {
	
	@Autowired
	private InviaOperazioniPCCService inviaOperazioniPCCService;
	
	@Autowired
	private AggiornaStatoRichiestaService aggiornaStatoRichiestaService;
	
	@Autowired
	private MarcWSPortType marcWS;
	
	@Test
	public void invioOperazioneContabile() {
		
		InviaOperazioniPCC req = new InviaOperazioniPCC();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setDataOra(new Date());
		InviaOperazioniPCCResponse res = inviaOperazioniPCCService.executeService(req);
		assertNotNull(res);
		
		for(Errore e : res.getErrori()){
			System.out.println(e.getTesto());
		}
		System.out.println("end.");
	}

	@Test
	public void marcTest(){
		ProxyOperazioneContabileRichiestaTipo datiRichiestaOperazioneContabile = new ProxyOperazioneContabileRichiestaTipo();
		System.out.println("invoco il servizio di MARC: invioOperazioneContabile");
		ResultType result = marcWS.invioOperazioneContabile(datiRichiestaOperazioneContabile);
		String xml = JAXBUtility.marshall(result);
		System.out.println("result: \n"+xml);
	}
	
	
	@Test
	public void agggiornaStatoRichiestaTest(){
		AggiornaStatoRichiesta req = new AggiornaStatoRichiesta();
		AggiornamentoStatoRichiestaRequest aggiornamentoStatoRichiestaRequest = new AggiornamentoStatoRichiestaRequest();
		aggiornamentoStatoRichiestaRequest.setIdTransazionePA("35, b36");
		aggiornamentoStatoRichiestaRequest.setStato(StatoRichiestaType.ESEGUITA);
		ResultType esitoTrattamento = new ResultType();
		esitoTrattamento.setCodice("TEST");
		esitoTrattamento.setMessaggio("test desc esito trattamento");
		aggiornamentoStatoRichiestaRequest.setEsitoTrattamento(esitoTrattamento);
		
		QueryOperazioneContabileRispostaTipo rispostaQueryOperazioneContabile = new QueryOperazioneContabileRispostaTipo();
		DatiRispostaOperazioneContabileQueryTipo datiRisposta = new DatiRispostaOperazioneContabileQueryTipo();
		datiRisposta.setDataFineElaborazione(new GregorianCalendar());
		rispostaQueryOperazioneContabile.setDatiRisposta(datiRisposta);
		aggiornamentoStatoRichiestaRequest.setRispostaQueryOperazioneContabile(rispostaQueryOperazioneContabile);
		
		
		
		req.setAggiornamentoStatoRichiestaRequest(aggiornamentoStatoRichiestaRequest);
		
		
		
		/*
		checkNotNull(req.getAggiornamentoStatoRichiestaRequest(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aggiornamento stato richiesta"));
		checkNotBlank(req.getAggiornamentoStatoRichiestaRequest().getIdTransazionePA(), "id transazione PA aggiornamento stato richiesta", false);
		checkNotNull(req.getAggiornamentoStatoRichiestaRequest().getStato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato aggiornamento stato richiesta"), false);
		checkNotNull(req.getAggiornamentoStatoRichiestaRequest().getEsitoTrattamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("esito trattamento aggiornamento stato richiesta"));	
		checkNotNull(req.getAggiornamentoStatoRichiestaRequest().getEsitoTrattamento().getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice esito trattamento aggiornamento stato richiesta"), false);
		checkNotNull(req.getAggiornamentoStatoRichiestaRequest().getEsitoTrattamento().getMessaggio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("messaggio esito trattamento aggiornamento stato richiesta"), false);

		 */
		
		aggiornaStatoRichiestaService.executeService(req);
	}

	
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	
	@Test
	public void testIdTransazionePA(){
		
		UUID uuid = UUID.randomUUID();
		String idTransazionePA = uuid.toString();
		
		List<RegistroComunicazioniPCC> registrazioniDiUnDocumento = new ArrayList<RegistroComunicazioniPCC>();
		
		List<Integer> ids = Arrays.asList(5,6,8,2);
		for (Integer id : ids) {
			RegistroComunicazioniPCC r = new RegistroComunicazioniPCC();
			r.setUid(id);
			registrazioniDiUnDocumento.add(r);
		}
		RegistroComunicazioniPCC r = new RegistroComunicazioniPCC();
		r.setUid(5);
		registrazioniDiUnDocumento.add(r);
		
		
		registroComunicazioniPCCDad.impostaIdTransazionePARegistrazioniTxNew(registrazioniDiUnDocumento, idTransazionePA);
		System.out.println("Returning: "+idTransazionePA);
		
		
		Set<Integer> result = registroComunicazioniPCCDad.findRegistrazioniUidsByIdTransazionePA(idTransazionePA);
		System.out.println("Returning: "+ result);
		
		
	}
	
	public static void main(String[] args) {
		for(int i = 0; i<100; i++){
			UUID uuid = UUID.randomUUID();
			System.out.println(uuid.toString() + " [length:"+uuid.toString().length()+"]");
		}
	}
}
