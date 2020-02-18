/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.test.business.documenti;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.TipoFile;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileAsyncService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileServicesEnum;
import it.csi.siac.siacintegser.business.service.stipendi.comparator.ComparatorStipendioSecondoLivello;
import it.csi.siac.siacintegser.business.service.stipendi.model.Stipendio;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

public class CaricaStipendiTest extends BaseJunit4TestCase {

	@Autowired
	private ElaboraFileService elaboraFileService;
	@Autowired
	private ElaboraFileAsyncService elaboraFileAsyncService;

	@Test
	public void elaboraFileStipendi() {
		final String methodName = "elaboraFileStipendi";
		ElaboraFile req = new ElaboraFile();
		
		//req.setRichiedente(getRichiedenteByProperties("coll", "edisu"));
		req.setRichiedente(getRichiedenteByProperties("forn1", "cmto"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setBilancio(getBilancioByProperties("forn1", "cmto", "2019"));

		String fileName = "M2019_02_0850S-soloSTI.txt";
		
		File file = new File();
		file.setTipo(new TipoFile(ElaboraFileServicesEnum.STIPE_ONERI.getCodice()));
		file.setNome(fileName);

		byte[] contenuto;
		try {
			contenuto = getTestFileBytes("docs/test/stipendi/" + fileName);
		} catch (IOException e) {
			log.error(methodName, "IOException in file read", e);
			fail("impossibile leggere il file di test: " + e.getMessage());
			return;
		}

		file.setContenuto(contenuto);

		req.setFile(file);
		ElaboraFileResponse res = elaboraFileService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void elaboraFileStipendiAsync() {
		final String methodName = "elaboraFileStipendiAsync";
		ElaboraFile req = new ElaboraFile();
		
		req.setRichiedente(getRichiedenteByProperties("coll", "ente1"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setBilancio(getBilancioTest(16, 2015));

		File file = new File();
		file.setTipo(new TipoFile(ElaboraFileServicesEnum.STIPE_ONERI.getCodice()));

		byte[] contenuto;
		try {
			contenuto = getTestFileBytes("docs/test/stipendi/flusso_stipendi_ahmad_1.txt");
		} catch (IOException e) {
			log.error(methodName, "IOException in file read", e);
			fail("impossibile leggere il file di test: " + e.getMessage());
			return;
		}

		file.setContenuto(contenuto);

		req.setFile(file);
		AsyncServiceRequestWrapper<ElaboraFile> areq = new AsyncServiceRequestWrapper<ElaboraFile>();
		areq.setAccount(req.getAccount());
		areq.setDataOra(req.getDataOra());
		areq.setEnte(req.getEnte());
		areq.setRequest(req);
		areq.setRichiedente(req.getRichiedente());
		areq.setAzioneRichiesta(new AzioneRichiesta());
		
		AsyncServiceResponse res = elaboraFileAsyncService.executeService(areq);

		assertNotNull(res);
		
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch(InterruptedException ie) {
			log.error(methodName, "Sleep interrupted", ie);
			fail("Interrupted thread sleep");
		}
	}

	/**
	 * Test ordinamento stipendi.
	 */
	@Test
	public void testOrdinamentoStipendi() {
		String nomeFile ="stipendi/stipendi-caricati.xml";
//		String nomeFileReduced = "stipendi/stipendi-caricati_reduced.xml";
//		String nomeFileReduced6267 = "stipendi/stipendi-caricati_reduced6267.xml";
		List<Stipendio> stipendiDaOrdinare =  getListaStipendiFromFile(nomeFile);
		System.out.println(stipendiDaOrdinare.size());
		if (!stipendiDaOrdinare.isEmpty()) {
			log.debug("sortStipendiSecondoLivello", "Ordinamento secondo livello in corso ...");
			Collections.sort(stipendiDaOrdinare, ComparatorStipendioSecondoLivello.INSTANCE);
			log.debug("","############################## ORDINAMENTO SECONDO LIVELLO###############################################################################");
			return;
		}
		log.debug("sortStipendiSecondoLivello", "La lista fornita in input e' null");
		
	}
	
	public static void main(String[] args) {
		String nomeFile ="stipendi/stipendi-caricati.xml";
//		String nomeFileReduced = "stipendi/stipendi-caricati_reduced.xml";
//		String nomeFileReduced6267 = "stipendi/stipendi-caricati_reduced6267.xml";
		List<Stipendio> stipendiDaOrdinare =  getListaStipendiFromFile(nomeFile);
		System.out.println(stipendiDaOrdinare.size());
		if (!stipendiDaOrdinare.isEmpty()) {
			System.out.println("Ordinamento secondo livello in corso ...");
			Collections.sort(stipendiDaOrdinare, ComparatorStipendioSecondoLivello.INSTANCE);
			Wrapper wrapper = new Wrapper();
			wrapper.stipendi = stipendiDaOrdinare;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JAXB.marshal(wrapper, baos);
			System.out.println("stipendi ordinati:");
			System.out.println(baos.toString());
			wrapper = null;
		}
	}

	private static List<Stipendio> getListaStipendiFromFile(String nomeFile) {
		Wrapper wrapper = getTestFileObject(Wrapper.class, nomeFile);
		return wrapper.stipendi;
	}
	
	@XmlType
	public static class Wrapper {
		@XmlElementWrapper(name="stipendi")
		@XmlElement(name="stipendio")
		public List<Stipendio> stipendi = new ArrayList<Stipendio>(); 
	}

	
//	public static void main(String[] args) {
//		List<Stipendio> stipendiDaOrdinare = new ArrayList<Stipendio>();
//		for(int i =0; i<7; i++) {
//			Stipendio st = new Stipendio();
//			Impegno imp = new Impegno();
//			imp.setAnnoMovimento(0);
//			imp.setNumero(new BigDecimal(0));
//			st.setImpegno(imp);
//			Accertamento acc = new Accertamento();
//			acc.setAnnoMovimento(0);
//			acc.setNumero(new BigDecimal(0));
//			st.setAccertamento(acc);
//			stipendiDaOrdinare.add(st);
//		}	
//		
//		Collections.sort(stipendiDaOrdinare, ComparatorStipendioSecondoLivello.INSTANCE);
//	}

}
