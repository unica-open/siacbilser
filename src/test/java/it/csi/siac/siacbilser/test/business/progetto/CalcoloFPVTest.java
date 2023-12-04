/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.progetto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.progetto.CalcoloFondoPluriennaleVincolatoComplessivoService;
import it.csi.siac.siacbilser.business.service.progetto.CalcoloFondoPluriennaleVincolatoEntrataService;
import it.csi.siac.siacbilser.business.service.progetto.CalcoloFondoPluriennaleVincolatoSpesaService;
import it.csi.siac.siacbilser.business.service.progetto.CambiaFlagUsatoPerFpvCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.OttieniFondoPluriennaleVincolatoCronoprogrammaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.OttieniFondoPluriennaleVincolatoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.OttieniFondoPluriennaleVincolatoCronoprogrammaResponse;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoCronoprogramma;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoEntrata;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

/**
 * 
 */
public class CalcoloFPVTest extends BaseJunit4TestCase {
	
	
	
	@Autowired
	private CalcoloFondoPluriennaleVincolatoSpesaService calcoloFondoPluriennaleVincolatoSpesaService;
	@Autowired
	private CalcoloFondoPluriennaleVincolatoEntrataService calcoloFondoPluriennaleVincolatoEntrataService;
	@Autowired
	private CalcoloFondoPluriennaleVincolatoComplessivoService calcoloFondoPluriennaleVincolatoComplessivoService;
	
//	@Autowired
//	private CalcoloProspettoRiassuntivoCronoprogrammaService calcoloProspettoRiassuntivoCronoprogrammaService;
	
	@Autowired
	private CambiaFlagUsatoPerFpvCronoprogrammaService cambiaFlagUsatoPerFpvCronoprogrammaService;
//	@Autowired
//	private CalcoloFondoPluriennaleVincolatoCronoprogrammaService  calcoloFondoPluriennaleVincolatoCronoprogrammaService;
	
//	@Autowired
//	private ProgettoDad progettoDad;
	
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;
	
	@Autowired
	private OttieniFondoPluriennaleVincolatoCronoprogrammaService ottieniFondoPluriennaleVincolatoCronoprogrammaService;
	
	@Test
	public void calcoloFondoPluriennaleVincolatoSpesa(){
		
		CalcoloFondoPluriennaleVincolatoSpesa req = new CalcoloFondoPluriennaleVincolatoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		Progetto progetto = new Progetto();
		progetto.setUid(1);
		req.setProgetto(progetto);

		req.setAnno(2015);
		
		CalcoloFondoPluriennaleVincolatoSpesaResponse res = calcoloFondoPluriennaleVincolatoSpesaService.executeService(req);
		
		assertNotNull(res);
	}
	
	
	@Test
	public void calcoloFondoPluriennaleVincolatoEntrata(){
		
		CalcoloFondoPluriennaleVincolatoEntrata req = new CalcoloFondoPluriennaleVincolatoEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		Progetto progetto = new Progetto();
		progetto.setUid(1);
		req.setProgetto(progetto);

		req.setAnno(2015);
		
		CalcoloFondoPluriennaleVincolatoEntrataResponse res = calcoloFondoPluriennaleVincolatoEntrataService.executeService(req);
		
		assertNotNull(res);
	}
	
	
	@Test
	public void calcoloFondoPluriennaleVincolatoTotale(){
		
		CalcoloFondoPluriennaleVincolatoComplessivo req = new CalcoloFondoPluriennaleVincolatoComplessivo();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		Progetto progetto = new Progetto();
		progetto.setUid(1);
		req.setProgetto(progetto);

		req.setAnno(2015);
		
		CalcoloFondoPluriennaleVincolatoComplessivoResponse res = calcoloFondoPluriennaleVincolatoComplessivoService.executeService(req);
		
		assertNotNull(res);
	}
	
//	@Test
//	public void calcoloProspettoRiassuntivoCronoprogrammaDiGestione(){
//		
//		CalcoloProspettoRiassuntivoCronoprogramma req = new CalcoloProspettoRiassuntivoCronoprogramma();
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		Progetto progetto=new Progetto();
//		progetto.setUid(3);
//
//		req.setProgetto(progetto);
//		req.setAnno(2014);
//		
//		CalcoloProspettoRiassuntivoCronoprogrammaResponse res = calcoloProspettoRiassuntivoCronoprogrammaService.executeService(req);
//		
//		assertNotNull(res);
//	}
	
	
	/**
	 * cambia il flag usato per fpv cronoprogramma
	 */
	@Test
	public void cambiaFlagUsatoPerFPVCronoprogramma() {
			
		CambiaFlagUsatoPerFpvCronoprogramma req = new CambiaFlagUsatoPerFpvCronoprogramma();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		Cronoprogramma c = new Cronoprogramma();
		c.setBilancio(getBilancioTest());
		c.setUsatoPerFpv(true);
		c.setUid(17);
        req.setCronoprogramma(c);
		CambiaFlagUsatoPerFpvCronoprogrammaResponse res=cambiaFlagUsatoPerFpvCronoprogrammaService.executeService(req);

		assertNotNull(res);
	}
	
	
//	@Test
//	public void calcoloFondoPluriennaleVincolatoCronoprogrammaPrevisione() {
//		CalcoloFondoPluriennaleVincolatoCronoprogramma request = new CalcoloFondoPluriennaleVincolatoCronoprogramma();
//		
//		request.setDataOra(new Date());
//		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		Cronoprogramma c = new Cronoprogramma();
//		c.setUid(29);
//		
//		request.setCronoprogramma(c);
//		
//		CalcoloFondoPluriennaleVincolatoCronoprogrammaResponse response = calcoloFondoPluriennaleVincolatoCronoprogrammaService.executeService(request);
//	
//		log.debug("ahmad", "Size della lista "+response.raggruppaFondoPluriennaleVincolatoEntratePerAnni().size());
//		for(RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma fpv : response.raggruppaFondoPluriennaleVincolatoEntratePerAnni()){
//			log.debug("ahmad","Anno :"+fpv.getAnno());
//			log.debug("ahmad","ImportoEntrata :"+fpv.getImportoEntrata());
//			log.debug("ahmad","ImportoTitolo1 :"+fpv.getImportoTitolo1());
//			log.debug("ahmad","ImportoTitolo2 :"+fpv.getImportoTitolo2());
//			//log.debug("ahmad","Totale Entrata :"+fpv.getSommaImportoEntrataTitolo1Titolo2());
//			//log.debug("ahmad","Totale fpv :"+fpv.getSommaImportoTitolo1Titolo2());
//		
//		/*for (Entry<Integer, FondoPluriennaleVincolatoCronoprogramma[]> entry : response.raggruppaFondoPluriennaleVincolatoPerAnno().entrySet()) {
//			Integer key = entry.getKey();
//			FondoPluriennaleVincolatoCronoprogramma[] value = entry.getValue();
//			log.debug("ahmad", "key: "+key);
//
//			for(FondoPluriennaleVincolatoCronoprogramma fpbc : value){
//				
//				log.debug("ahmad", "Anno : "+fpbc.getAnno());
//				log.debug("ahmad", "Importo : "+fpbc.getImporto());
//				log.debug("ahmad", "Importo fpv : "+fpbc.getImportoFPV());
//				log.debug("ahmad", "Stato : "+fpbc.getStato());
//				log.debug("ahmad", "\n");
//			}*/
//			
//			
//		}
//		log.debug("ahmad", "*********************************************************************************************************************************************************************");
////		log.debug("ahmad", "Size della lista DI USCITA normale "+response.getListaFondoPluriennaleVincolatoUscitaCronoprogrammaCompleta().size());
////		log.debug("ahmad","LISTA USCITA :");
////		for(FondoPluriennaleVincolatoUscitaCronoprogramma fpv : response.getListaFondoPluriennaleVincolatoUscitaCronoprogramma()){
////			log.debug("ahmad","Anno :"+fpv.getAnno());
////			log.debug("ahmad","Importo :"+fpv.getImporto());
////			log.debug("ahmad","ImportoFPV :"+fpv.getImportoFPV());
//////			log.debug("ahmad","ImportoTitolo2 :"+fpv.getImportoTitolo2());
//////			log.debug("ahmad","Totale Entrata :"+fpv.getImportoTotaleEntrata());
//////			log.debug("ahmad","Totale fpv :"+fpv.getImportoTotaleEntrataFPV());
////		}
//		log.debug("ahmad", "*********************************************************************************************************************************************************************");
//		log.debug("ahmad", "Size della lista DI USCITA completa "+response.getListaFondoPluriennaleVincolatoUscitaCronoprogramma().size());
//		log.debug("ahmad","LISTA USCITA completa  :");
//		for(FondoPluriennaleVincolatoUscitaCronoprogramma fpv : response.getListaFondoPluriennaleVincolatoUscitaCronoprogramma()){
//			log.debug("ahmad","Anno :"+fpv.getAnno());
//			log.debug("ahmad","Importo :"+fpv.getImporto());
//			log.debug("ahmad","ImportoFPV :"+fpv.getImportoFPV());
////			log.debug("ahmad","ImportoTitolo2 :"+fpv.getImportoTitolo2());
////			log.debug("ahmad","Totale Entrata :"+fpv.getImportoTotaleEntrata());
////			log.debug("ahmad","Totale fpv :"+fpv.getImportoTotaleEntrataFPV());
//		}
//			assertNotNull(response);
//
//	}
//	@Test
//	public void calcoloFpvByFunction(){
//		
//		Progetto progetto = new Progetto();
//		progetto.setUid(53);
//		
//		CalcoloFondoPluriennaleVincolatoComplessivo requestFPVComplessivoByFunc = new CalcoloFondoPluriennaleVincolatoComplessivo();
//		requestFPVComplessivoByFunc.setDataOra(new Date());
//		requestFPVComplessivoByFunc.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		requestFPVComplessivoByFunc.setAnno(Integer.valueOf(2015));
//		requestFPVComplessivoByFunc.setProgetto(progetto);
//		CalcoloFondoPluriennaleVincolatoComplessivoResponse responseFPVComplessivo = calcoloFondoPluriennaleVincolatoComplessivoService.executeService(requestFPVComplessivoByFunc);
//		List<FondoPluriennaleVincolatoTotale> listaFPVComplessivoByFunc = responseFPVComplessivo.getListaFondoPluriennaleVincolatoTotale();
//
//		CalcoloFondoPluriennaleVincolatoEntrata requestFPVEntrataByFunc = new CalcoloFondoPluriennaleVincolatoEntrata();
//		requestFPVEntrataByFunc.setDataOra(new Date());
//		requestFPVEntrataByFunc.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		requestFPVEntrataByFunc.setAnno(Integer.valueOf(2015));
//		requestFPVEntrataByFunc.setProgetto(progetto);
//		CalcoloFondoPluriennaleVincolatoEntrataResponse  responseFPVEntrataByFunct = calcoloFondoPluriennaleVincolatoEntrataService.executeService(requestFPVEntrataByFunc);
//		List<FondoPluriennaleVincolatoEntrata> listaFPVEntrataByFunc = responseFPVEntrataByFunct.getListaFondoPluriennaleVincolatoEntrata();
//		
//		CalcoloFondoPluriennaleVincolatoSpesa requestFPVSpesaByFunc = new CalcoloFondoPluriennaleVincolatoSpesa();
//		requestFPVSpesaByFunc.setDataOra(new Date());
//		requestFPVSpesaByFunc.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		requestFPVSpesaByFunc.setAnno(Integer.valueOf(2015));
//		requestFPVSpesaByFunc.setProgetto(progetto);
//		CalcoloFondoPluriennaleVincolatoSpesaResponse responseFPVSpesaByFunc = calcoloFondoPluriennaleVincolatoSpesaService.executeService(requestFPVSpesaByFunc);
//		List<FondoPluriennaleVincolatoUscitaCronoprogramma> listaFPVSpesaByFunc = responseFPVSpesaByFunc.getListaFondoPluriennaleVincolatoUscitaCronoprogramma();
//		
//		CalcoloFondoPluriennaleVincolatoCronoprogramma request = new CalcoloFondoPluriennaleVincolatoCronoprogramma();
//		Cronoprogramma c = new Cronoprogramma();
//		c.setUid(48);
//		request.setCronoprogramma(c);
//		request.setBilancio(getBilancio2015Test());
//		
//		request.setDataOra(new Date());
//		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
//				
//
//		CalcoloFondoPluriennaleVincolatoCronoprogrammaResponse response = calcoloFondoPluriennaleVincolatoCronoprogrammaService.executeService(request);
//		List<FondoPluriennaleVincolatoEntrataCronoprogramma> listaEntrataCronoprogramma = response.getListaFondoPluriennaleVincolatoEntrataCronoprogramma();
//		List<FondoPluriennaleVincolatoUscitaCronoprogramma> listaUscitaCronoprogramma = response.getListaFondoPluriennaleVincolatoUscitaCronoprogramma();
//		List<Integer> anniEntrataUscitaComplessiva = response.getListaAnniEntrataUscitaComplessiva();
//	}
	
//	@Test
//	public void calcoloFpvCronoprogrammaByFunction(){
//		
//		
//		CalcoloFondoPluriennaleVincolatoCronoprogramma request = new CalcoloFondoPluriennaleVincolatoCronoprogramma();
//		Cronoprogramma c = new Cronoprogramma();
//		c.setUid(48);
//		request.setCronoprogramma(c);
//		request.setBilancio(getBilancio2015Test());
//		
//		request.setDataOra(new Date());
//		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
//				
//
//		CalcoloFondoPluriennaleVincolatoCronoprogrammaResponse response = calcoloFondoPluriennaleVincolatoCronoprogrammaService.executeService(request);
//		List<FondoPluriennaleVincolatoEntrataCronoprogramma> listaEntrataCronoprogramma = response.getListaFondoPluriennaleVincolatoEntrataCronoprogramma();
//		List<FondoPluriennaleVincolatoUscitaCronoprogramma> listaUscitaCronoprogramma = response.getListaFondoPluriennaleVincolatoUscitaCronoprogramma();
//		List<Integer> anniEntrataUscitaComplessiva = response.getListaAnniEntrataUscitaComplessiva();
//		log.logXmlTypeObject(listaEntrataCronoprogramma, "listaEntrataCronoprogramma");
//		log.logXmlTypeObject(listaUscitaCronoprogramma, "listaUscitaCronoprogramma");
//		log.logXmlTypeObject(anniEntrataUscitaComplessiva, "anniEntrataUscitaComplessiva");
//	}
	
	@Test
	public void calcoloFpvEntrataPrevisione(){
		Cronoprogramma  cronoprogramma = new Cronoprogramma();
		cronoprogramma.setUid(48);
		List<FondoPluriennaleVincolatoEntrata> listaFpvEntrata = cronoprogrammaDad.calcoloFpvEntrataPrevisione(cronoprogramma, Integer.valueOf(2015),null);
		if(listaFpvEntrata.isEmpty()){
			log.debug("calcoloFpvEntrataPrevisione", "Lista vuota!");
		}else{
			log.debug("calcoloFpvEntrataPrevisione", "Lista non vuota!");
			StringBuilder sb = new StringBuilder();
			for(FondoPluriennaleVincolatoEntrata fpve : listaFpvEntrata){
				sb.append(" anno: ").append(fpve.getAnno())
				.append(" importo: ").append(fpve.getImporto())
				.append(" entrataSpesaCorrente: ").append(fpve.getFpvEntrataSpesaCorrente())
				.append(" entrataSpesaContoCapitale: ").append(fpve.getFpvEntrataSpesaContoCapitale())
				.append(" totale: ").append(fpve.getTotale())
				.append(" importofpv:: ").append(fpve.getImportoFPV())
				.append("\n");
			}
			
			System.out.println(sb);
			
		}
		
		
	}
	
	@Test
	public void calcoloFpvSpesaPrevisione(){
		Cronoprogramma  cronoprogramma = new Cronoprogramma();
		cronoprogramma.setUid(48);
		List<FondoPluriennaleVincolatoUscitaCronoprogramma> listaFpvSpesa = cronoprogrammaDad.calcoloFpvSpesaPrevisione(cronoprogramma, Integer.valueOf(2015), null);
		if(listaFpvSpesa.isEmpty()){
			log.debug("calcoloFpvEntrataPrevisione", "Lista vuota!");
		}else{
			log.debug("calcoloFpvEntrataPrevisione", "Lista non vuota!");
			StringBuilder sb = new StringBuilder();
			for(FondoPluriennaleVincolatoUscitaCronoprogramma fpve : listaFpvSpesa){
				sb.append(" anno: ").append(fpve.getAnno())
				.append(" importo: ").append(fpve.getImporto())
				.append(" importoFpv: ").append(fpve.getImportoFPV())
				.append(" missione: ").append(fpve.getMissione().getCodice())
				.append(" programma: ").append(fpve.getProgramma().getCodice())
				.append(" titolo: ").append(fpve.getTitoloSpesa().getCodice())
				.append("\n");
			}
			
			System.out.println(sb);
			
		}
	
	}
	
	@Test
	public void ottieniFpvByfunction(){
		
		OttieniFondoPluriennaleVincolatoCronoprogramma request = new OttieniFondoPluriennaleVincolatoCronoprogramma();
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		request.setBilancio(getBilancio2015Test());
		request.setDataOra(new Date());
		
		Cronoprogramma cronoprogramma = new Cronoprogramma();
		cronoprogramma.setUid(52);
		
		request.setCronoprogramma(cronoprogramma);
		
		OttieniFondoPluriennaleVincolatoCronoprogrammaResponse response = ottieniFondoPluriennaleVincolatoCronoprogrammaService.executeService(request);
		
		Map<Integer, FondoPluriennaleVincolatoCronoprogramma[]> mappaFondoPluriennaleVincolatoTotale = raggruppaFondoPluriennaleVincolatoTotale(response.getListaFondoPluriennaleVincolatoEntrata(), response.getListaFondoPluriennaleVincolatoUscitaCronoprogramma());
		
		
		for(FondoPluriennaleVincolatoCronoprogramma[] arr : mappaFondoPluriennaleVincolatoTotale.values()){
			
			FondoPluriennaleVincolatoEntrata fpvENellaMappa = (FondoPluriennaleVincolatoEntrata) arr[0];
			FondoPluriennaleVincolatoUscitaCronoprogramma fpvUNellaMappa = (FondoPluriennaleVincolatoUscitaCronoprogramma) arr[1];
			
			loggaFpvEntrata(fpvENellaMappa);
			
			loggaFpvUscita(fpvUNellaMappa);
		}
	}


	private void loggaFpvUscita(FondoPluriennaleVincolatoUscitaCronoprogramma fpvUNellaMappa) {
		StringBuilder sbSpesa = new StringBuilder();			
		sbSpesa.append(" anno: ").append(fpvUNellaMappa.getAnno())
		.append(" importo: ").append(fpvUNellaMappa.getImporto())
		.append(" importoFpv: ").append(fpvUNellaMappa.getImportoFPV())
		.append(" missione: ").append(fpvUNellaMappa.getMissione()!= null? fpvUNellaMappa.getMissione().getCodice() : "null")
		.append(" programma: ").append(fpvUNellaMappa.getProgramma()!= null? fpvUNellaMappa.getProgramma().getCodice() : "null")
		.append(" titolo: ").append(fpvUNellaMappa.getTitoloSpesa()!= null? fpvUNellaMappa.getProgramma().getCodice() : "null")
		.append("\n");
		System.out.println("Spesa:");
		System.out.println(sbSpesa);
	}


//	private void loggaFpvEntrata(RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma fpvE) {
//		StringBuilder sbEntrata = new StringBuilder();
//		sbEntrata.append(" anno: ").append(fpvE.getAnno())
//		.append(" importo: ").append(fpvE.getImportoEntrata())
//		.append(" entrataSpesaCorrente: ").append(fpvE.getImportoTitolo1())
//		.append(" entrataSpesaContoCapitale: ").append(fpvE.getImportoTitolo2())
//		.append(" totale: ").append(fpvE.getImportoTotaleEntrata())
//		.append(" importofpv:: ").append(fpvE.getImportoTotaleEntrataFPV())
//		.append("\n");
//		System.out.println("Entrata:");
//		System.out.println(sbEntrata);
//	}
	
	private void loggaFpvEntrata(FondoPluriennaleVincolatoEntrata fpvENellaMappa) {
		StringBuilder sbEntrata = new StringBuilder();
		sbEntrata.append(" anno: ").append(fpvENellaMappa.getAnno())
		.append(" importo: ").append(fpvENellaMappa.getImporto())
		.append(" entrataSpesaCorrente: ").append(fpvENellaMappa.getFpvEntrataSpesaCorrente())
		.append(" entrataSpesaContoCapitale: ").append(fpvENellaMappa.getFpvEntrataSpesaContoCapitale())
		.append(" totale: ").append(fpvENellaMappa.getTotale())
		.append(" importofpv:: ").append(fpvENellaMappa.getImportoFPV())
		.append("\n");
		System.out.println("Entrata:");
		System.out.println(sbEntrata);
	}
	
	
//	@Test
//	public void calcolaFpvNoFunction(){
//		CalcoloFondoPluriennaleVincolatoCronoprogramma request = new CalcoloFondoPluriennaleVincolatoCronoprogramma();
//		request.setDataOra(new Date());
//		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		request.setBilancio(getBilancio2015Test());
//		
//		Cronoprogramma cronoprogramma = new Cronoprogramma();
//		cronoprogramma.setUid(54);
//		
//		request.setCronoprogramma(cronoprogramma);
//		
//
//		CalcoloFondoPluriennaleVincolatoCronoprogrammaResponse response = calcoloFondoPluriennaleVincolatoCronoprogrammaService.executeService(request);
//
//		List<FondoPluriennaleVincolatoUscitaCronoprogramma> listaFpvU = response.getListaFondoPluriennaleVincolatoUscitaCronoprogramma();
//		
//		for(FondoPluriennaleVincolatoUscitaCronoprogramma fpvU : listaFpvU){
//			loggaFpvUscita(fpvU);
//		}
////		for(RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma fpvE: response.raggruppaFondoPluriennaleVincolatoEntratePerAnni()){
////			loggaFpvEntrata(fpvE);
////		}
//		/*model.setListaRiepilogoFondoPluriennaleVincolatoPerEntrate(response.raggruppaFondoPluriennaleVincolatoEntratePerAnni());
//		model.setListaFondoPluriennaleVincolatoUscitaCronoprogramma(response.getListaFondoPluriennaleVincolatoUscitaCronoprogramma());
//		model.setMappaFondoPluriennaleVincolatoPerAnno(response.raggruppaFondoPluriennaleVincolatoPerAnno());*/
//	}
	
	public Map<Integer, FondoPluriennaleVincolatoCronoprogramma[]> raggruppaFondoPluriennaleVincolatoTotale(List<FondoPluriennaleVincolatoEntrata> listaFondoPluriennaleVincolatoEntrata, List<FondoPluriennaleVincolatoUscitaCronoprogramma> listaFondoPluriennaleVincolatoUscitaCronoprogramma){
		Map<Integer, FondoPluriennaleVincolatoCronoprogramma[]> map =  new TreeMap<Integer, FondoPluriennaleVincolatoCronoprogramma[]>();
		
		// Creo i fondi di entrata
		for (FondoPluriennaleVincolatoEntrata fpvE : listaFondoPluriennaleVincolatoEntrata) {
			Integer anno = fpvE.getAnno();
			FondoPluriennaleVincolatoCronoprogramma[] arr = map.get(anno);
			
			FondoPluriennaleVincolatoEntrata fpvENellaMappa = null;
			
			if(arr == null) {
				arr = new FondoPluriennaleVincolatoCronoprogramma[2];
				FondoPluriennaleVincolatoUscitaCronoprogramma fpvU = new FondoPluriennaleVincolatoUscitaCronoprogramma();
				fpvU.setAnno(anno);
				arr[1] = fpvU;
				
				fpvENellaMappa = new FondoPluriennaleVincolatoEntrata();
				fpvENellaMappa.setAnno(anno);
			} else {
				fpvENellaMappa = (FondoPluriennaleVincolatoEntrata) arr[0];
			}
			
			fpvENellaMappa.setImporto(fpvENellaMappa.getImporto().add(fpvE.getImporto()));
			fpvENellaMappa.setImportoFPV(fpvENellaMappa.getImportoFPV().add(fpvE.getImportoFPV()));
			
			arr[0] = fpvENellaMappa;
			
			map.put(anno, arr);
		}
		
		// Creo i fondi di uscita
		for (FondoPluriennaleVincolatoUscitaCronoprogramma fpvU : listaFondoPluriennaleVincolatoUscitaCronoprogramma) {
			
			Integer anno = fpvU.getAnno();
			FondoPluriennaleVincolatoCronoprogramma[] arr = map.get(anno);
			
			FondoPluriennaleVincolatoUscitaCronoprogramma fpvUNellaMappa = null;
			
			if(arr == null) {
				arr = new FondoPluriennaleVincolatoCronoprogramma[2];
				FondoPluriennaleVincolatoEntrata fpvE = new FondoPluriennaleVincolatoEntrata();
				fpvE.setAnno(anno);
				arr[0] = fpvE;
				
				fpvUNellaMappa = new FondoPluriennaleVincolatoUscitaCronoprogramma();
				fpvUNellaMappa.setAnno(anno);
			} else {
				fpvUNellaMappa = (FondoPluriennaleVincolatoUscitaCronoprogramma) arr[1];
			}
			
			fpvUNellaMappa.setImporto(fpvUNellaMappa.getImporto().add(fpvU.getImporto()));
			fpvUNellaMappa.setImportoFPV(fpvUNellaMappa.getImportoFPV().add(fpvU.getImportoFPV()));
			
			arr[1] = fpvUNellaMappa;
			
			map.put(anno, arr);
		}
		return map;
	}
	
	@Test
	public void testCrono() {
		Progetto progetto = create(Progetto.class, 84);
		progetto.setValoreComplessivo(new BigDecimal("20"));
		List<String> cronosCodes = cronoprogrammaDad.findCronoprogrammiConSpeseMaggioriImportoProgetto(progetto);
		for (String string : cronosCodes) {
			System.out.println(string);
		}
	}
}
