/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.test.business.documenti;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacatt.test.business.provvedimento.ProvvedimentoImplTest;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.TipoFile;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileServicesEnum;
import it.csi.siac.siacintegser.business.service.documenti.ElaboraDocumentoGenericoAsyncService;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumento;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumentoAsyncResponse;

public class CaricaAttiAmministrativiTest extends BaseJunit4TestCase {
	
	@Autowired
	private ElaboraFileService elaboraFileService;
	
	@Autowired 
	protected SoggettoService soggettoService;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private ProvvedimentoDad provvedimentoDad;
	private static final LogUtil log = new LogUtil(ProvvedimentoImplTest.class);

	@Test
	public void getAnnoBilancioFromEnte(){
		String methodName="getBilancioFromEnte";
		bilancioDad.setEnteEntity(getEnteTest());
        
		String annoBilancio =bilancioDad.getAnnoBilancioByEnteAndFaseCodeNonPrevisione(getEnteTest());
		
		log.debug(methodName, "Anno Bilancio ottenuto :"+annoBilancio);
	}
	@Test
	public void getBilancioFromEnte(){
		String methodName = "getBilancioFromEnte";
		bilancioDad.setEnteEntity(getEnteTest(1));

		Bilancio bilancio = bilancioDad.getBilancioByEnteAndFaseCodeNonPrevisione(getEnteTest());
		if (bilancio != null && bilancio.getUid() != 0) {
			log.info(methodName,"Info Bilancio Trovato  (Anno/Fase/Uid):" + bilancio.getAnno() + "/" + bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio().toString() + "/" + bilancio.getUid());
		} else {
			log.info(methodName,"Nessun bilancio e' stato trovato per l'ente :"+ getEnteTest(1).getUid());

		}
			
	}
	
	@Test
	public void caricaTipiAtto(){
		provvedimentoDad.setEnte(getEnteTest());
		List<TipoAtto> listaTipoAtto = provvedimentoDad.getElencoTipi();
		
		log.debug("ahmad", "Size"+listaTipoAtto.size());
		for(TipoAtto ta : listaTipoAtto)
		log.logXmlTypeObject(ta, "TIPO Atto "+listaTipoAtto.indexOf(ta));

	}
	
	@Test
	public void caricaCodiceStatoOut(){
		String methodName = "caricaCodiceStatoOut";
		AttoAmministrativo attoAmministrativo =  new  AttoAmministrativo();
		attoAmministrativo.setEnte(getEnteTest());
		attoAmministrativo.setStatoOperativo("DEFINITIVO");
		String codiceStato =provvedimentoDad.findCodiceStatoOut(attoAmministrativo);
		
		log.debug(methodName, "CODICE STATO REPERITO :"+codiceStato);
	}

	@Test
	public void elaboraFileAttiAmministrativi() {
		ElaboraFile req = new ElaboraFile();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("forn1", "cmto"));
		
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setBilancio(getBilancio2015Test());
		req.setAccount(req.getRichiedente().getAccount());

		File file = new File();
		file.setTipo(new TipoFile(ElaboraFileServicesEnum.FLUSSO_ATTI_AMMINISTRATIVI.getCodice())); 
		
		byte[] contenuto;
		try {
			contenuto = getTestFileBytes("docs/test/atti/SIAC-4819_v2.txt");
//			contenuto = getTestFileBytes("docs/test/atti/atti_29_09_2015.txt");

		} catch (IOException e) {
			e.printStackTrace();
			fail("impossibile leggere il file di test: "+e.getMessage());
			return;
		} 
		
		file.setContenuto(contenuto);
		file.setUid(0);
		
		req.setFile(file);
		
		ElaboraFileResponse res = elaboraFileService.executeService(req);
		assertNotNull(res);
	}
	
	@Autowired
	private ElaboraDocumentoGenericoAsyncService elaboraDocumentoAsyncService;
	
	@Test
	public void test() {
		StringBuilder sb = new StringBuilder()
				.append("<elaboraDocumento>")
				.append("	<annoBilancio>2018</annoBilancio>")
				.append("	<codiceEnte>CMTO</codiceEnte>")
				.append("	<codiceFruitore>SIAC</codiceFruitore>")
				.append("	<codiceTipoDocumento>FLUSSO_ATTI</codiceTipoDocumento>")
				.append("	<contenutoDocumento>")
				.append("		<![CDATA[I|CMTO  |    |     |    |          |          |2018|2617 |32  |HE        |HE3       |20180119000000|        |              |        |P|FORNITURA DI CALORE MEDIANTE TELERISCALDAMENTO PRESSO IL  CENTRO PER L'IMPIEGO DI VIA CASTELGOMBERTO, 75 IN TORINO - IMPEGNO DI SPESA PER L'ESERCIZIO 2018. (U.I. EURO 10.000,00)                                                                                                                                                                                                                                                                                                                                   |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ")
				.append("]]>")
				.append("	</contenutoDocumento>")
				.append("</elaboraDocumento>");
		ElaboraDocumento req = JAXBUtility.unmarshall(sb.toString(), ElaboraDocumento.class);
		ElaboraDocumentoAsyncResponse res = elaboraDocumentoAsyncService.executeService(req);
		assertNotNull(res);
	}


	public Richiedente getRichiedentePerAmbienteDiTest(){
		Richiedente richiedente = new Richiedente();
		Operatore operatore = new Operatore();
		operatore.setCodiceFiscale("");
		richiedente.setOperatore(operatore);
		Account account = new Account();
		account.setUid(6);
		account.setEnte(getEnteTest(1));
		account.setNome("AHMAD_TEST");
		richiedente.setAccount(account);
		
		return richiedente;
	}

	@Override
	protected Ente getEnteTest(){
		return getEnteTest(1);
	}
	
}
