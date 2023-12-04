/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.test.business.movgest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.business.service.movgest.RicercaSinteticaModulareVincoliAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaSinteticaModulareVincoliImpegnoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliImpegnoResponse;
import it.csi.siac.siacfinser.integration.dad.VincoliAccertamentoDad;
import it.csi.siac.siacfinser.integration.dad.VincoliImpegnoDad;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.movgest.VincoliMovimentoModelDetail;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;

public class VincoliMovimentoTest extends BaseJunit4TestCase {

	@Autowired
	VincoliAccertamentoDad vincoliAccertamentoDad;
	@Autowired
	VincoliImpegnoDad vincoliImpegnoDad;
	@Autowired
	RicercaSinteticaModulareVincoliAccertamentoService ricercaSinteticaModulareVincoliAccertamentoService;
	@Autowired
	RicercaSinteticaModulareVincoliImpegnoService ricercaSinteticaModulareVincoliImpegnoService;
	
	Integer uidAccertamento = 254528; //2021/2021/1553
	Integer uidImpegno = 254532; //2021/2021/9706
	Integer annoBilancio = 2022;
	
	@Test
	public void testEstrazioneDadVincoliAccertamento() {
		Accertamento accertamento = new Accertamento();
		accertamento.setUid(uidAccertamento);
		
		Ente ente = getEnteByProperties("consip", "regp");
		
		VincoliMovimentoModelDetail[] modelDetails = 
//				null;
				VincoliMovimentoModelDetail.getAllModelDetailForVincoliAccertamento();
		
		List<VincoloAccertamento> vincoli = vincoliAccertamentoDad.cercaVincoliAccertamento(accertamento, ente, modelDetails);
		
		assertTrue(CollectionUtils.isNotEmpty(vincoli));
		
		for (VincoloAccertamento vincoloAccertamento : vincoli) {
			logVincoliAccertamento(vincoloAccertamento);
		}
	}

	@Test
	public void testEstrazioneDadVincoliImpegno() {
		Impegno impegno = new Impegno();
		impegno.setUid(uidImpegno);
		
		Ente ente = getEnteByProperties("consip", "regp");
		
		VincoliMovimentoModelDetail[] modelDetails = 
//				null;
				VincoliMovimentoModelDetail.getAllModelDetailForVincoliImpegno();
		
		List<VincoloImpegno> vincoli = vincoliImpegnoDad.cercaVincoliImpegno(impegno, ente, modelDetails);
		
		assertTrue(CollectionUtils.isNotEmpty(vincoli));
		
		for (VincoloImpegno vincoloImpegno : vincoli) {
			logVincoliImpegno(vincoloImpegno);
		}
	}
	
	@Test
	public void testEstrazioneDadVincoliAccertamentoByAnno() {
		Accertamento accertamento = new Accertamento();
		accertamento.setUid(uidAccertamento);
		
		Ente ente = getEnteByProperties("consip", "regp");
		
		VincoliMovimentoModelDetail[] modelDetails = 
//				null;
				VincoliMovimentoModelDetail.getAllModelDetailForVincoliAccertamento();
		
		List<VincoloAccertamento> vincoli = vincoliAccertamentoDad
				.cercaVincoliAccertamentoByAnnoBilancio(accertamento, annoBilancio, ente, modelDetails);
		
		assertTrue(CollectionUtils.isNotEmpty(vincoli));
		
		for (VincoloAccertamento vincoloAccertamento : vincoli) {
			logVincoliAccertamento(vincoloAccertamento);
		}
	}
	
	@Test
	public void testEstrazioneDadVincoliImpegnoByAnno() {
		Impegno impegno = new Impegno();
		impegno.setUid(uidImpegno);
		
		Ente ente = getEnteByProperties("consip", "regp");
		
		VincoliMovimentoModelDetail[] modelDetails = 
//				null;
				VincoliMovimentoModelDetail.getAllModelDetailForVincoliImpegno();
		
		List<VincoloImpegno> vincoli = vincoliImpegnoDad
				.cercaVincoliImpegnoByAnnoBilancio(impegno, annoBilancio, ente, modelDetails);
		
		assertTrue(CollectionUtils.isNotEmpty(vincoli));
		
		for (VincoloImpegno vincoloImpegno : vincoli) {
			logVincoliImpegno(vincoloImpegno);
		}
	}
	
	@Test
	public void testRicercaSinteticaModulareVincoliAccertamentoService() {
		RicercaSinteticaModulareVincoliAccertamento req = new RicercaSinteticaModulareVincoliAccertamento();
		Accertamento accertamento = new Accertamento();
		accertamento.setUid(uidAccertamento);
		
		VincoliMovimentoModelDetail[] modelDetails = 
//		ModelDetail[] modelDetails = 
//				null;
				VincoliMovimentoModelDetail.getAllModelDetailForVincoliAccertamento();
		
		req.setAccertamento(accertamento);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(annoBilancio);
		req.setDataOra(new Date());
		req.setRicercaSinteticaModulareModelDetails(modelDetails);
		
		RicercaSinteticaModulareVincoliAccertamentoResponse res = ricercaSinteticaModulareVincoliAccertamentoService.executeService(req);
		
		assertSuccesso(res);
		assertTrue(CollectionUtils.isNotEmpty(res.getVincoliAccertamento()));
		
		for (VincoloAccertamento vincoloAccertamento : res.getVincoliAccertamento()) {
			logVincoliAccertamento(vincoloAccertamento);
		}
	}

	@Test
	public void testRicercaSinteticaModulareVincoliImpegnoService() {
		RicercaSinteticaModulareVincoliImpegno req = new RicercaSinteticaModulareVincoliImpegno();
		Impegno impegno = new Impegno();
		impegno.setUid(uidImpegno);
		
		VincoliMovimentoModelDetail[] modelDetails = 
				null;
//				VincoliMovimentoModelDetail.getAllModelDetailForVincoliImpegno();
		
		req.setImpegno(impegno);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(annoBilancio);
		req.setDataOra(new Date());
		req.setRicercaSinteticaModulareModelDetails(modelDetails);
		
		RicercaSinteticaModulareVincoliImpegnoResponse res = ricercaSinteticaModulareVincoliImpegnoService.executeService(req);
		
		assertSuccesso(res);
		assertTrue(CollectionUtils.isNotEmpty(res.getVincoliImpegno()));
		
		for (VincoloImpegno vincoloImpegno : res.getVincoliImpegno()) {
			logVincoliImpegno(vincoloImpegno);
		}
	}
	
	@Test
	public void testRepositorySommaModificheoReimpReannoImpegno() {
		Ente ente = getEnteByProperties("consip", "regp");
		Impegno impegno = create(Impegno.class, uidImpegno);
		
		//BigDecimal sommaImporti = vincoliImpegnoDad.calcolaSommaModificheReimpReannoAssociateAdImpegno(impegno, ente);
		
		//assertNotNull(sommaImporti);
		//log.debug("sommaImporti: ", "[" + sommaImporti + "]");
	}

	@Test
	public void testRepositorySommaModificheoReimpReannoAccertamento() {
		Ente ente = getEnteByProperties("consip", "regp");
		Accertamento accertamento = create(Accertamento.class, uidAccertamento);
		
		BigDecimal sommaImporti = vincoliAccertamentoDad.calcolaSommaModificheReimpReannoAssociateAdAccertamento(accertamento, ente);
	
		assertNotNull(sommaImporti);
		log.debug("sommaImporti: ", "[" + sommaImporti + "]");
	}

	@Test
	public void testRepositorySommaModificheoReimpReannoImpegnoFunction() {
		Ente ente = getEnteByProperties("consip", "regp");
		Impegno impegno = create(Impegno.class, uidImpegno);
		
		/*BigDecimal sommaImporti = vincoliImpegnoDad.calcolaSommaModificheReimpReannoAssociateAdImpegno(impegno, ente);
		
		assertNotNull(sommaImporti);
		log.debug("sommaImporti: ", "[" + sommaImporti + "]");*/
	}
	
	@Test
	public void testRepositorySommaModificheoReimpReannoAccertamentoFunction() {
		Ente ente = getEnteByProperties("consip", "regp");
		Accertamento accertamento = create(Accertamento.class, uidAccertamento);
		
		BigDecimal sommaImporti = vincoliAccertamentoDad.calcolaSommaModificheReimpReannoAssociateAdAccertamento(accertamento, ente);
		
		assertNotNull(sommaImporti);
		log.debug("sommaImporti: ", "[" + sommaImporti + "]");
	}

	@Test
	public void testEstrazionePrimoImpegnoCatenaRecursionJava() {
		Ente ente = getEnteByProperties("consip", "regp");
		
		Integer annoEsercizio = 2021;
		Integer annoMovimento = 2021; 
		Integer numeroMovimento = 1562;
		String tipoMovimento = "I";
		
		Object[] estremi = vincoliImpegnoDad.caricaMovimentoCatenaRiacceramentoReanno(
				annoEsercizio,
				ente.getUid(), 
				annoMovimento, 
				numeroMovimento, 
				tipoMovimento,
				null);
		
		assertTrue(estremi != null);
		
		String estremo = (String) estremi[4] + "/" + (String) estremi[2] + "/" + (String) estremi[3];
		
		log.debug("testEstrazionePrimoImpegnoCatenaRecursionJava", "estrazione primo movimento da catena:  [" + estremo + "]");
	}

	@Test
	public void testEstrazionePrimoAccertamentoCatena() {
		Ente ente = getEnteByProperties("consip", "regp");
		
		Integer annoEsercizio = 2021;
		Integer annoMovimento = 2021; 
		BigDecimal numeroMovimento = new BigDecimal(1562);
		String tipoMovimento = "A";
		
		Accertamento acc = new Accertamento();
		acc.setAnnoMovimento(annoMovimento);
		acc.setNumeroBigDecimal(numeroMovimento);
		acc.setTipoMovimento(tipoMovimento);
		
		Accertamento accertamento = vincoliAccertamentoDad
				.caricaPrimoAccertamentoCatenaRiaccertamentoReanno(annoEsercizio,
						ente.getUid(),
						acc
					);
		
		assertTrue(accertamento != null);
		
		String estremo = accertamento.getBilancio().getAnno() + "/" + accertamento.getAnnoMovimento() + "/" +accertamento.getNumeroBigDecimal()
			+ " [" + accertamento.getUid() + "]";
		
		log.debug("testEstrazionePrimoImpegnoCatenaRecursionJava", "estrazione primo movimento da catena:  [" + estremo + "]");
	}

	@Test
	public void testEstrazionePrimoImpegnoCatena() {
		Ente ente = getEnteByProperties("consip", "regp");
		
		Integer annoEsercizio = 2021;
		Integer annoMovimento = 2021; 
		BigDecimal numeroMovimento = new BigDecimal(1562);
		String tipoMovimento = "I";
		
		Impegno imp = new Impegno();
		imp.setAnnoMovimento(annoMovimento);
		imp.setNumeroBigDecimal(numeroMovimento);
		imp.setTipoMovimento(tipoMovimento);
		
		Impegno impegno = vincoliImpegnoDad
				.caricaPrimoImpegnoCatenaRiaccertamentoReanno(annoEsercizio,
					ente.getUid(),
					imp
				);

		assertTrue(impegno != null);
		
		String estremo = impegno.getBilancio().getAnno() + "/" + impegno.getAnnoMovimento() + "/" + impegno.getNumeroBigDecimal()
			+ " [" + impegno.getUid() + "]";
		
		log.debug("testEstrazionePrimoImpegnoCatenaRecursionJava", "estrazione primo movimento da catena:  [" + estremo + "]");
	}
	
	@Test
	public void testRicercaSinteticaModulareVincoliAccertamentoServicePrimoMovimentoCatena() {
		RicercaSinteticaModulareVincoliAccertamento req = new RicercaSinteticaModulareVincoliAccertamento();
		Accertamento accertamento = new Accertamento();
		accertamento.setUid(254528);
		accertamento.setAnnoMovimento(2021);
		accertamento.setNumeroBigDecimal(new BigDecimal(1553));
		accertamento.setTipoMovimento("A");
		
		VincoliMovimentoModelDetail[] modelDetails = 
//		ModelDetail[] modelDetails = 
//				null;
				VincoliMovimentoModelDetail.getAllModelDetailForVincoliAccertamento();
		
		req.setAccertamento(accertamento);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(2021);
		req.setDataOra(new Date());
		req.setRicercaSinteticaModulareModelDetails(modelDetails);
		req.setCaricaPrimoImpegnoCatenaReimpReanno(Boolean.TRUE);
		
		RicercaSinteticaModulareVincoliAccertamentoResponse res = ricercaSinteticaModulareVincoliAccertamentoService.executeService(req);
		
		assertSuccesso(res);
		assertTrue(CollectionUtils.isNotEmpty(res.getVincoliAccertamento()));
		
		for (VincoloAccertamento vincoloAccertamento : res.getVincoliAccertamento()) {
			logVincoliAccertamento(vincoloAccertamento);
		}
	}

	@Test
	public void testRicercaSinteticaModulareVincoliImpegnoServicePrimoMovimentoCatena() {
		RicercaSinteticaModulareVincoliImpegno req = new RicercaSinteticaModulareVincoliImpegno();
		Impegno impegno = new Impegno();
		impegno.setUid(254611);
		impegno.setAnnoMovimento(2022);
		impegno.setNumeroBigDecimal(new BigDecimal(1300));
		impegno.setTipoMovimento("I");

		req.setAnnoBilancio(2022);
		
		VincoliMovimentoModelDetail[] modelDetails = 
				null;
//				VincoliMovimentoModelDetail.getAllModelDetailForVincoliImpegno();
		
		req.setImpegno(impegno);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setRicercaSinteticaModulareModelDetails(modelDetails);
		req.setCaricaPrimoImpegnoCatenaReimpReanno(Boolean.TRUE);
		
		RicercaSinteticaModulareVincoliImpegnoResponse res = ricercaSinteticaModulareVincoliImpegnoService.executeService(req);
		
		assertSuccesso(res);
		assertTrue(CollectionUtils.isNotEmpty(res.getVincoliImpegno()));
		
		for (VincoloImpegno vincoloImpegno : res.getVincoliImpegno()) {
			logVincoliImpegno(vincoloImpegno);
		}
	}
	
	@Test
	public void testRicercaImpegnoPadreDaResiduo() {
		RicercaSinteticaModulareVincoliImpegno req = new RicercaSinteticaModulareVincoliImpegno();
		Impegno impegno = new Impegno();
		impegno.setUid(254727);
		impegno.setAnnoMovimento(2021);
		impegno.setNumeroBigDecimal(new BigDecimal(973));
		impegno.setTipoMovimento("I");
		
		req.setAnnoBilancio(2022);

		CapitoloUscitaGestione capug = new CapitoloUscitaGestione();
		capug.setAnnoCapitolo(req.getAnnoBilancio());
		capug.setNumeroCapitolo(7);
		capug.setNumeroArticolo(0);
		
		impegno.setCapitoloUscitaGestione(capug);

		
		VincoliMovimentoModelDetail[] modelDetails = 
				null;
//				VincoliMovimentoModelDetail.getAllModelDetailForVincoliImpegno();
		
		req.setImpegno(impegno);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setRicercaSinteticaModulareModelDetails(modelDetails);
		req.setCaricaPrimoImpegnoCatenaReimpReanno(Boolean.TRUE);
		
		RicercaSinteticaModulareVincoliImpegnoResponse res = ricercaSinteticaModulareVincoliImpegnoService.executeService(req);
		
		assertSuccesso(res);
		assertTrue(CollectionUtils.isNotEmpty(res.getVincoliImpegno()));
		
		for (VincoloImpegno vincoloImpegno : res.getVincoliImpegno()) {
			logVincoliImpegno(vincoloImpegno);
		}
	}

	@Test
	public void testRicercaAccertamentoPadreDaResiduo() {
		RicercaSinteticaModulareVincoliAccertamento req = new RicercaSinteticaModulareVincoliAccertamento();
		Accertamento accertamento = new Accertamento();
		accertamento.setUid(254727);
		accertamento.setAnnoMovimento(2021);
		accertamento.setNumeroBigDecimal(new BigDecimal(973));
		accertamento.setTipoMovimento("A");
		
		req.setAnnoBilancio(2022);
		
		CapitoloEntrataGestione capeg = new CapitoloEntrataGestione();
		capeg.setAnnoCapitolo(req.getAnnoBilancio());
		capeg.setNumeroCapitolo(7);
		capeg.setNumeroArticolo(0);
		
		accertamento.setCapitoloEntrataGestione(capeg);
		
		
		VincoliMovimentoModelDetail[] modelDetails = 
				null;
//				VincoliMovimentoModelDetail.getAllModelDetailForVincoliImpegno();
		
		req.setAccertamento(accertamento);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setRicercaSinteticaModulareModelDetails(modelDetails);
		req.setCaricaPrimoImpegnoCatenaReimpReanno(Boolean.TRUE);
		
		RicercaSinteticaModulareVincoliAccertamentoResponse res = ricercaSinteticaModulareVincoliAccertamentoService.executeService(req);
		
		assertSuccesso(res);
		assertTrue(CollectionUtils.isNotEmpty(res.getVincoliAccertamento()));
		
		for (VincoloAccertamento vincoloAccertamento : res.getVincoliAccertamento()) {
			logVincoliAccertamento(vincoloAccertamento);
		}
	}
	
	// =============================================== METODI DI UTILITA ============================================== 
	
	private void logVincoliAccertamento(VincoloAccertamento vincolo) {
		log.debug("logVincoliAccertamento", vincoloUscita(vincolo) + ", importo vincolo: [" + vincolo.getImporto() 
			+ "], somma: " + vincolo.getSommaImportiModReimpReanno());
	}

	private String vincoloUscita(VincoloAccertamento vincolo) {
		String vincoloUscita = " - PROBLEMA CON VINCOLO USCITA - ";
		if(vincolo.getImpegno() != null) {			
			vincoloUscita = "impegno: ";
			if(vincolo.getImpegno().getBilancio() != null) {
				vincoloUscita += vincolo.getImpegno().getBilancio().getAnno();
			}
			vincoloUscita += "/" +  vincolo.getImpegno().getAnnoMovimento() 
			+ "/" +  vincolo.getImpegno().getNumeroBigDecimal() 
			+ " importo imp: " + vincolo.getImpegno().getImportoAttuale()
			+ ", importo vincolo: [" + vincolo.getImporto() + "]";
		}
		return vincoloUscita;
	}
	
	private void logVincoliImpegno(VincoloImpegno vincolo) {
		log.debug("logVincoliImpegno", vincoloEntrata(vincolo) + ", importo vincolo: [" + vincolo.getImporto() 
			+ "], somma: " + vincolo.getSommaImportiModReimpReanno());
	}

	private String vincoloEntrata(VincoloImpegno vincolo) {
		String vincoloEntrata = " - PROBLEMA CON VINCOLO ENTRATA - ";
		if(vincolo.getAccertamento() != null) {
			vincoloEntrata = "accertamento: "; 
			if(vincolo.getAccertamento().getBilancio() != null) {
				vincoloEntrata += vincolo.getAccertamento().getBilancio().getAnno();				
			}
			vincoloEntrata += "/" +  vincolo.getAccertamento().getAnnoMovimento() 
			+ "/" +  vincolo.getAccertamento().getNumeroBigDecimal()
			+ " importo acc: " + vincolo.getAccertamento().getImportoAttuale();
		}
		if(vincolo.getAvanzoVincolo() != null && vincolo.getAvanzoVincolo().getTipoAvanzovincolo() != null) {
			vincoloEntrata = "avanzo vincolo: " +  vincolo.getAvanzoVincolo().getTipoAvanzovincolo().getCodice() 
					+ " - " +  vincolo.getAvanzoVincolo().getTipoAvanzovincolo().getDescrizione() 
					+ ", massimale importo: [" + vincolo.getAvanzoVincolo().getAvavImportoMassimale() + "]";
		}
		return vincoloEntrata;
	}
	
}

class PrimoImpegnoCatenaDto {
	
	private String flagDaReanno;
	private String flagDaReimputazione;
	private String annoMovimento;
	private String numeroMovimento;
	
	public PrimoImpegnoCatenaDto() {}

	public String getFlagDaReanno() {
		return flagDaReanno;
	}

	public void setFlagDaReanno(String flagDaReanno) {
		this.flagDaReanno = flagDaReanno;
	}

	public String getFlagDaReimputazione() {
		return flagDaReimputazione;
	}

	public void setFlagDaReimputazione(String flagDaReimputazione) {
		this.flagDaReimputazione = flagDaReimputazione;
	}

	public String getAnnoMovimento() {
		return annoMovimento;
	}

	public void setAnnoMovimento(String annoMovimento) {
		this.annoMovimento = annoMovimento;
	}

	public String getNumeroMovimento() {
		return numeroMovimento;
	}

	public void setNumeroMovimento(String numeroMovimento) {
		this.numeroMovimento = numeroMovimento;
	}
	
}
