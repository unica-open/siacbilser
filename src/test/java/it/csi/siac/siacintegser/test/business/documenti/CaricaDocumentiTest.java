/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.test.business.documenti;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.TipoFile;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DatiCertificazioneCrediti;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElenchiDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.NoteTesoriere;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.RitenuteDocumento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoAvviso;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoImpresa;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfin2ser.model.TipoOperazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileServicesEnum;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

public class CaricaDocumentiTest extends BaseJunit4TestCase {
	
	private static final ThreadLocal<SimpleDateFormat> SDF = new ThreadLocal<SimpleDateFormat>(){
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("dd/MM/yyyy HH.mm.ss");
		}
	};

	@Autowired
	private ElaboraFileService elaboraFileService;
	@Autowired
	private ProvvedimentoDad provvedimentoDad;
//	@Autowired
//	private LeggiStatoElaborazioneDocumentoService leggiStatoElaborazioneDocumentiService;
	
//	@Test
//	public void leggiStatoElaborazioneDocumenti() {
//		LeggiStatoElaborazioneDocumento req = new LeggiStatoElaborazioneDocumento();
//		req.setAnnoBilancio(2018);
//		req.setCodiceEnte("REGP");
//		req.setCodiceFruitore("GAMOP");
//		req.setIdOperazioneAsincrona(173283);
//		LeggiStatoElaborazioneDocumentoResponse res = leggiStatoElaborazioneDocumentiService.executeService(req);
//		log.logXmlTypeObject(res, "Response");;
//	}
	
	
	protected Richiedente getRichiedenteByEnvironment (boolean isSviluppo) {
		return  isSviluppo? getRichiedenteByProperties("consip","regp") : getRichiedenteTest("AAAAAA00A11E000M",71,15);
	}
	
	protected Bilancio getBilancioByEnvironment (boolean isSviluppo) {
		return  isSviluppo? getBilancio2015Test() : getBilancioTest(46, 2015);
	}
	
	protected Ente getEnteByEnvironment (boolean isSviluppo) {
		return  isSviluppo? getEnteTest() : getEnteTest(15);
	}
	
		
	@Test
	public void testProvvedimentoDad(){
		provvedimentoDad.setEnte(getEnteTest());
		
		TipoAtto tipoAttoALG = provvedimentoDad.getTipoAttoALGEvenNull(); 
		log.logXmlTypeObject(tipoAttoALG, "tipoAttoALG");
	}
	
	
	@Test
	public void elaboraFileDocumenti() {
		final String methodName = "elaboraFileDocumenti";
		ElaboraFile req = new ElaboraFile();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioByProperties("consip", "regp", "2017"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setAccount(req.getRichiedente().getAccount());

		File file = new File();
//		file.setUid(1804); // //per dev 759, per test 1799
		file.setTipo(new TipoFile(ElaboraFileServicesEnum.DOCUMENTO_SPESA.getCodice()));
				
		byte[] contenuto;
		try {
//			contenuto = getTestXmlProgrammatically();
			contenuto = getTestXml("CaricaDocumentiTest_GAM.xml");
			log.info(methodName, "Xml di partenza: "+new String(contenuto, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
			return;
		} 
		file.setContenuto(contenuto);
		
		req.setFile(file);
		
		ElaboraFileResponse res = elaboraFileService.executeService(req);
		assertNotNull(res);
	}

	
	
	private static byte[] getTestXml(String nomeFile) throws IOException{
		byte[] byteArray = getTestFileBytes("docs/test/documenti/"+nomeFile /*CaricaDocumentiTest_quoteCollegate.xml*/);
		return byteArray;
	}

	
	@SuppressWarnings("unused")
	private byte[] getTestXmlProgrammatically() throws UnsupportedEncodingException, ParseException {
		ElenchiDocumentiAllegato elenchiDocumentiAllegato;
			elenchiDocumentiAllegato = populateAllegatiAtto();
			return  JAXBUtility.marshall(elenchiDocumentiAllegato).getBytes("UTF-8");
		
	}

	private static ElenchiDocumentiAllegato populateAllegatiAtto() throws ParseException {
		ElenchiDocumentiAllegato elenchiDocumentiAllegatoModel = new ElenchiDocumentiAllegato();
		
		
		List<ElencoDocumentiAllegato> elenchiDocumentiAllegato = new ArrayList<ElencoDocumentiAllegato>();
		elenchiDocumentiAllegatoModel.setElenchiDocumentiAllegato(elenchiDocumentiAllegato);
		
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		
		
		
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(-1);
		Ente ente = new Ente();
		ente.setCodice("COTO");
		allegatoAtto.setEnte(ente);
		
		allegatoAtto.setCausale("mia causale allegato atto");
		allegatoAtto.setDatiSensibili(Boolean.FALSE);
		
		/* L'atto è facoltativo se presente il tipo ALG a sistema
		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
//		StrutturaAmministrativoContabile strutturaAmmContabile = new StrutturaAmministrativoContabile();
//		strutturaAmmContabile.setCodice("DIR01");
//		attoAmministrativo.setStrutturaAmmContabile(strutturaAmmContabile );
		attoAmministrativo.setAnno(2015);
		attoAmministrativo.setNumero(123);
		TipoAtto tipoAtto = new TipoAtto();
		tipoAtto.setCodice("02"); //02 - Determina
		attoAmministrativo.setTipoAtto(tipoAtto);
		allegatoAtto.setAttoAmministrativo(attoAmministrativo );
		/*/ 
		
		
		elencoDocumentiAllegato.setAllegatoAtto(allegatoAtto);
		
		elencoDocumentiAllegato.setAnnoSysEsterno(2015); //deve essere lo stesso anno del bilancio in request!!! occhio!
		elencoDocumentiAllegato.setNumeroSysEsterno("123");
		
		
		
		List<Subdocumento<?, ?>> subdocumenti = new ArrayList<Subdocumento<?,?>>();
		
		DocumentoSpesa documentoSpesa = creaDocumentoSpesa(2015, "123", new BigDecimal(1500));
		
		SubdocumentoSpesa subdocumentoSpesa1 = creaSubDocumentoSpesa(ente, documentoSpesa, "FATT SYST ESTERNO S1", new BigDecimal(100), new BigDecimal(10));
		subdocumenti.add(subdocumentoSpesa1);
		
		SubdocumentoSpesa subdocumentoSpesa2 = creaSubDocumentoSpesa(ente, documentoSpesa, "FATT SYST ESTERNO S2", new BigDecimal(101), new BigDecimal(11));
		subdocumenti.add(subdocumentoSpesa2);
		
		
		DocumentoEntrata documentoEntrata = creaDocumentoEntrata(2015, "123", new BigDecimal(1500));
		documentoSpesa.addDocumentoEntrataFiglio(documentoEntrata);

		SubdocumentoEntrata subdocumentoEntrata2 = creaSubDocumentoEntrata(ente, documentoEntrata, "FATT SYST ESTERNO E1", new BigDecimal(50), new BigDecimal(1));
		subdocumenti.add(subdocumentoEntrata2);
		
		
		
		elencoDocumentiAllegato.setSubdocumenti(subdocumenti);
		
		
		SubdocumentoIvaSpesa subdocumentoIvaSpesaLegatoAQuota = creaSubDocumentoIvaSpesa(ente, null, subdocumentoSpesa1, "subIVA legato a sub 1", new BigDecimal(10), 2015);
		subdocumentoSpesa1.setSubdocumentoIva(subdocumentoIvaSpesaLegatoAQuota);
		
		SubdocumentoIvaSpesa subdocumentoIvaSpesaLegatoADocumento = creaSubDocumentoIvaSpesa(ente, documentoSpesa, null, "subIVA legato a doc spesa", new BigDecimal(10), 2015);
		documentoSpesa.setListaSubdocumentoIva(Arrays.asList(subdocumentoIvaSpesaLegatoADocumento));
		
		
		List<SubdocumentoIva<?, ?, ?>> subdocumentiIvaASeStanti = new ArrayList<SubdocumentoIva<?, ?, ?>>();
		
		DocumentoSpesa testataIva = creaDocumentoIVASpesa(2015, "2233", new BigDecimal(100));
		
		SubdocumentoIvaSpesa subdocumentoIvaSpesaLegatoATestata = creaSubDocumentoIvaSpesa(ente, null, subdocumentoSpesa1, "subIVA legato a sub testata", new BigDecimal(10), 2015);
		
		subdocumentoIvaSpesaLegatoATestata.setDocumento(testataIva);
//		testataIva.setListaSubdocumentoIva(Arrays.asList(subdocumentoIvaSpesaLegatoATestata));
		
		subdocumentiIvaASeStanti.add(subdocumentoIvaSpesaLegatoATestata);
		
		
		elencoDocumentiAllegato.setSubdocumentiIva(subdocumentiIvaASeStanti);
		
		elenchiDocumentiAllegato.add(elencoDocumentiAllegato);
		//allegatoAtto.setElenchiDocumentiAllegato(elenchiDocumentiAllegato);
		
		
		
		//elenchiDocumentiAllegato.add(elencoDocumentiAllegato);
		
		elenchiDocumentiAllegatoModel.setElenchiDocumentiAllegato(elenchiDocumentiAllegato);
		return elenchiDocumentiAllegatoModel;
	}





	private static SubdocumentoSpesa creaSubDocumentoSpesa(Ente ente, DocumentoSpesa documentoSpesa, String descrizione, BigDecimal importo, BigDecimal importoDaDedurre) throws ParseException {
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setEnte(ente);
		subdocumentoSpesa.setImporto(importo);
		subdocumentoSpesa.setLoginOperazione("APPJ"); //bisogna che il richiedente sia APPJ!
		subdocumentoSpesa.setDescrizione(descrizione);
		subdocumentoSpesa.setDataScadenza(SDF.get().parse("16/06/2014 14.30.05"));
		subdocumentoSpesa.setFlagOrdinativoSingolo(Boolean.FALSE);
		TipoAvviso tipoAvviso = new TipoAvviso();
		tipoAvviso.setCodice("TESORIERE"); //TESORIERE, ALTRO
		subdocumentoSpesa.setTipoAvviso(tipoAvviso);
		subdocumentoSpesa.setFlagEsproprio(Boolean.FALSE);
		subdocumentoSpesa.setNote("PROVA INSERIMENTO QUOTA");
		NoteTesoriere noteTesoriere = new NoteTesoriere();
		noteTesoriere.setCodice("01");
		subdocumentoSpesa.setNoteTesoriere(noteTesoriere);
		
		Impegno impegno = new Impegno();
		impegno.setAnnoMovimento(2015); //deve avere lo stesso anno del Documento!!!!
		impegno.setNumero(new BigDecimal("29014"));
		subdocumentoSpesa.setImpegno(impegno);
		
		//Facoltativo
//		SubImpegno subImpegno = new SubImpegno();
//		subImpegno.setAnnoMovimento(2015);
//		subImpegno.setNumero(new BigDecimal("1"));
//		subdocumentoSpesa.setSubImpegno(subImpegno);
		
		//subdocumentoSpesa.setNumeroMutuo("mio numero mutuo");
		
		subdocumentoSpesa.setImportoDaDedurre(importoDaDedurre);
		
		//Facoltativo
//		ProvvisorioDiCassa provvisorioDiCassa = new ProvvisorioDiCassa();
//		provvisorioDiCassa.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.S);
//		provvisorioDiCassa.setAnno(2014);
//		provvisorioDiCassa.setNumero(777);
//		provvisorioDiCassa.setDataConvalida(sdf.get().parse("01/09/2014 00.00.00")); //setDataConvalida
//		subdocumentoSpesa.setProvvisorioCassa(provvisorioDiCassa);
		
		
		subdocumentoSpesa.setCausaleOrdinativo("mia causale ordinativo SERVIZIO CARICA DOCUMENTI");
		subdocumentoSpesa.setCig("mio cig");
		subdocumentoSpesa.setCup("mio cup");
		
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = new ModalitaPagamentoSoggetto();
		modalitaPagamentoSoggetto.setCodiceModalitaPagamento("1");
		subdocumentoSpesa.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		
		SedeSecondariaSoggetto sedeSecondariaSoggetto = new SedeSecondariaSoggetto();
		sedeSecondariaSoggetto.setCodiceSedeSecondaria("myCodieceSede");
		subdocumentoSpesa.setSedeSecondariaSoggetto(sedeSecondariaSoggetto);
		
		subdocumentoSpesa.setCommissioniDocumento(CommissioniDocumento.ESENTE);
		
		
		DatiCertificazioneCrediti datiCertificazioneCrediti = new DatiCertificazioneCrediti();
		datiCertificazioneCrediti.setAnnotazione("mia annotazione");
		datiCertificazioneCrediti.setNoteCertificazione("mie note certificazione");
		datiCertificazioneCrediti.setNumeroCertificazione("mio numero certificazione");
		datiCertificazioneCrediti.setDataCertificazione(SDF.get().parse("01/09/2014 00.00.00"));
		datiCertificazioneCrediti.setFlagCertificazione(Boolean.FALSE);
		subdocumentoSpesa.setDatiCertificazioneCrediti(datiCertificazioneCrediti);
		
		subdocumentoSpesa.setDocumento(documentoSpesa);
		
		
		return subdocumentoSpesa;
	}
	
	
	
	
	
	private static SubdocumentoEntrata creaSubDocumentoEntrata(Ente ente, DocumentoEntrata documentoEntrata, String descrizione, BigDecimal importo, BigDecimal importoDaDedurre) throws ParseException {
		SubdocumentoEntrata subdocumentoEntrata = new SubdocumentoEntrata();
		subdocumentoEntrata.setEnte(ente);
		subdocumentoEntrata.setImporto(importo);
		subdocumentoEntrata.setLoginOperazione("APPJ"); //bisogna che il richiedente sia APPJ!
		subdocumentoEntrata.setDescrizione(descrizione);
		subdocumentoEntrata.setDataScadenza(SDF.get().parse("16/06/2014 14.30.05"));
		subdocumentoEntrata.setFlagOrdinativoSingolo(Boolean.FALSE);
		TipoAvviso tipoAvviso = new TipoAvviso();
		tipoAvviso.setCodice("TESORIERE"); //TESORIERE, ALTRO
		subdocumentoEntrata.setTipoAvviso(tipoAvviso);
		subdocumentoEntrata.setFlagEsproprio(Boolean.FALSE);
		subdocumentoEntrata.setNote("PROVA INSERIMENTO QUOTA");
		NoteTesoriere noteTesoriere = new NoteTesoriere();
		noteTesoriere.setCodice("01");
		subdocumentoEntrata.setNoteTesoriere(noteTesoriere);
		
		Accertamento impegno = new Accertamento();
		impegno.setAnnoMovimento(2015); //deve avere lo stesso anno del Documento!!!!
		impegno.setNumero(new BigDecimal(4));
		subdocumentoEntrata.setAccertamento(impegno);
		
		//Facoltativo
//		SubAccertamento subAccertamento = new SubAccertamento();
//		subAccertamento.setAnnoMovimento(2015);
//		subAccertamento.setNumero(new BigDecimal("1"));
//		subdocumentoEntrata.setSubAccertamento(subAccertamento);
		
		
		subdocumentoEntrata.setImportoDaDedurre(importoDaDedurre);
		
		//Facoltativo
//		ProvvisorioDiCassa provvisorioDiCassa = new ProvvisorioDiCassa();
//		provvisorioDiCassa.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.S);
//		provvisorioDiCassa.setAnno(2014);
//		provvisorioDiCassa.setNumero(777);
//		provvisorioDiCassa.setDataConvalida(sdf.get().parse("01/09/2014 00.00.00")); //setDataConvalida
//		subdocumentoEntrata.setProvvisorioCassa(provvisorioDiCassa);
		
		ContoTesoreria contoTesoreria = new ContoTesoreria();
		contoTesoreria.setCodice("01");
		subdocumentoEntrata.setContoTesoreria(contoTesoreria);
		
		Distinta distinta = new Distinta();
		distinta.setCodice("E1");
		subdocumentoEntrata.setDistinta(distinta);
		
		
		subdocumentoEntrata.setDocumento(documentoEntrata);
		
		
		return subdocumentoEntrata;
	}





	private static DocumentoSpesa creaDocumentoSpesa(Integer anno, String numero, BigDecimal importo) throws ParseException {
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(-1);
		documentoSpesa.setAnno(anno); //DEVE Avere lo stesso anno dell'impegno delle quote!!!!
		documentoSpesa.setNumero(numero);
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setCodice("FAT");
		tipoDocumento.setTipoFamigliaDocumento(TipoFamigliaDocumento.SPESA);
		documentoSpesa.setTipoDocumento(tipoDocumento);
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto("132829");
		documentoSpesa.setSoggetto(soggetto);
		
		documentoSpesa.setDescrizione("FATTURA SPESA SISTEMA ESTERNO");
		documentoSpesa.setDataEmissione(SDF.get().parse("15/05/2014 14.30.05"));
		documentoSpesa.setImporto(importo);
		documentoSpesa.setLoginOperazione("APPJ"); //bisogna avere un account ad hoc per fruitore del servizio di caricaDocumenti.
		
		documentoSpesa.setNumeroRepertorio("mio numero di repertorio");
		documentoSpesa.setArrotondamento(BigDecimal.ZERO);
		documentoSpesa.setDataScadenza(SDF.get().parse("16/05/2014 14.30.05"));
		documentoSpesa.setFlagBeneficiarioMultiplo(Boolean.FALSE);
		documentoSpesa.setTerminePagamento(1);
		CodiceBollo codiceBollo = new CodiceBollo();
		codiceBollo.setCodice("99");
		documentoSpesa.setCodiceBollo(codiceBollo);
		
		TipoImpresa tipoImpresa = new TipoImpresa();
		tipoImpresa.setCodice("ATI");
		documentoSpesa.setTipoImpresa(tipoImpresa);
		
		
		RitenuteDocumento ritenuteDocumento = new RitenuteDocumento();
		ritenuteDocumento.setImportoEsente(BigDecimal.valueOf(10));
		ritenuteDocumento.setImportoCassaPensioni(BigDecimal.valueOf(10));
		ritenuteDocumento.setImportoRivalsa(BigDecimal.valueOf(10));
		ritenuteDocumento.setImportoIVA(BigDecimal.valueOf(10));
		List<DettaglioOnere> listaOnere = new ArrayList<DettaglioOnere>();
		DettaglioOnere dettaglioOnere = new DettaglioOnere();
		
		TipoOnere tipoOnere = new TipoOnere();
		NaturaOnere naturaOnere = new NaturaOnere();
		naturaOnere.setCodice("IRPEF");
		tipoOnere.setNaturaOnere(naturaOnere);
		dettaglioOnere.setTipoOnere(tipoOnere);
		
		
		AttivitaOnere attivitaOnere = new AttivitaOnere();
		attivitaOnere.setCodice("mioCodiceAttivitaOnere");

		dettaglioOnere.setImportoImponibile(BigDecimal.valueOf(100));
		dettaglioOnere.setImportoCaricoSoggetto(BigDecimal.valueOf(100));
		dettaglioOnere.setImportoCaricoEnte(BigDecimal.valueOf(100));
		
		dettaglioOnere.setAttivitaOnere(attivitaOnere);
		
		listaOnere.add(dettaglioOnere);
		ritenuteDocumento.setListaOnere(listaOnere);
		
		documentoSpesa.setRitenuteDocumento(ritenuteDocumento);
		
		//documentoSpesa.setListaDocumentiSpesaFiglio(listaDocumentiSpesaFiglio);
		
		return documentoSpesa;
	}
	
	
	private static DocumentoEntrata creaDocumentoEntrata(Integer anno, String numero, BigDecimal importo) throws ParseException {
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(-1);
		documentoEntrata.setAnno(anno); //DEVE Avere lo stesso anno dell'impegno delle quote!!!!
		documentoEntrata.setNumero(numero);
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setCodice("TRB"); //"INCASSI TRIBUTI E CANONI"
		tipoDocumento.setTipoFamigliaDocumento(TipoFamigliaDocumento.ENTRATA);
		documentoEntrata.setTipoDocumento(tipoDocumento);
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto("132829");
		documentoEntrata.setSoggetto(soggetto);
		
		documentoEntrata.setDescrizione("FATTURA ENTRATA SISTEMA ESTERNO");
		documentoEntrata.setDataEmissione(SDF.get().parse("15/05/2014 14.30.05"));
		documentoEntrata.setImporto(importo);
		documentoEntrata.setLoginOperazione("APPJ"); //bisogna avere un account ad hoc per fruitore del servizio di caricaDocumenti.
		
		documentoEntrata.setNumeroRepertorio("mio numero di repertorio");
		documentoEntrata.setArrotondamento(BigDecimal.ZERO);
		documentoEntrata.setDataScadenza(SDF.get().parse("16/05/2014 14.30.05"));
		
		documentoEntrata.setTerminePagamento(1);
		CodiceBollo codiceBollo = new CodiceBollo();
		codiceBollo.setCodice("99");
		documentoEntrata.setCodiceBollo(codiceBollo);
	
		
		//documentoSpesa.setListaDocumentiSpesaFiglio(listaDocumentiSpesaFiglio);
		
		return documentoEntrata;
	}
	
	
	
	private static DocumentoSpesa creaDocumentoIVASpesa(Integer anno, String numero, BigDecimal importo) throws ParseException {
		
		DocumentoSpesa testataIva = new DocumentoSpesa();
//		documentoSpesa.setEnte(ente);
		testataIva.setUid(-100);
		testataIva.setAnno(anno); //DEVE Avere lo stesso anno dell'impegno delle quote!!!!
		testataIva.setNumero(numero);
		testataIva.setDescrizione("IVA SPESA SISTEMA ESTERNO");
		testataIva.setDataEmissione(SDF.get().parse("15/09/2014 14.30.05"));
		testataIva.setNote("note mia testata iva");
		testataIva.setImporto(importo);
		
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setCodice("FAT");
		tipoDocumento.setTipoFamigliaDocumento(TipoFamigliaDocumento.IVA_SPESA); //Questo discrimina un normale documento da una testata IVA!
		testataIva.setTipoDocumento(tipoDocumento);
		
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto("1");
		testataIva.setSoggetto(soggetto);
		
		
		return testataIva;
	}
	
	
	
	@SuppressWarnings("unused")
	private static DocumentoEntrata creaDocumentoIvaEntrata(Integer anno, String numero, BigDecimal importo) throws ParseException {
		DocumentoEntrata testataIva = new DocumentoEntrata();
//		documentoEntrata.setEnte(ente);
		testataIva.setUid(-100);
		testataIva.setAnno(anno); //DEVE Avere lo stesso anno dell'impegno delle quote!!!!
		testataIva.setNumero(numero);
		testataIva.setDescrizione("IVA ENTRATA SISTEMA ESTERNO");
		testataIva.setDataEmissione(SDF.get().parse("15/09/2014 14.30.05"));
		testataIva.setNote("note mia testata iva");
		testataIva.setImporto(importo);
		
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setCodice("FAT");
		tipoDocumento.setTipoFamigliaDocumento(TipoFamigliaDocumento.IVA_ENTRATA); //Questo discrimina un normale documento da una testata IVA!
		testataIva.setTipoDocumento(tipoDocumento);
		
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto("1");
		testataIva.setSoggetto(soggetto);
		
		
		return testataIva;
	}
	
	
	private static SubdocumentoIvaSpesa creaSubDocumentoIvaSpesa(Ente ente, DocumentoSpesa documentoSpesa, SubdocumentoSpesa subdoc, String descrizioneIva, BigDecimal importo, Integer annoEsercizio) throws ParseException {
		SubdocumentoIvaSpesa siva = new SubdocumentoIvaSpesa();
		siva.setEnte(ente);
		
		//NON si legano da qui ma dal subdoc o dal doc!
//		if(subdoc!=null) { //legato a Quota
//			siva.setSubdocumento(subdoc);
//		} else { //oppure legato a Documento esclusivamente
//			siva.setDocumento(documentoSpesa);
//		}
		
		siva.setImportoInValuta(importo);//?? zero!
		
		siva.setAnnoEsercizio(annoEsercizio);
		
		siva.setDataProtocolloDefinitivo(SDF.get().parse("16/06/2014 14.30.05"));
		siva.setDataRegistrazione(SDF.get().parse("16/06/2014 14.30.05"));
		siva.setDescrizioneIva(descrizioneIva);
		
		RegistroIva regIva = new RegistroIva();
		regIva.setCodice("01 Registro");
		regIva.setTipoRegistroIva(TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA);
		siva.setRegistroIva(regIva);
		
		//aliquote
		List<AliquotaSubdocumentoIva> listaAliquotaSubdocumentoIva = new ArrayList<AliquotaSubdocumentoIva>();
		AliquotaSubdocumentoIva aliquotaS =new AliquotaSubdocumentoIva();
		
		AliquotaIva aliquotaIva = new AliquotaIva();
		aliquotaIva.setCodice("04");
		aliquotaIva.setTipoOperazioneIva(TipoOperazioneIva.IMPONIBILE);
		aliquotaS.setAliquotaIva(aliquotaIva);
		
		aliquotaS.setImponibile(new BigDecimal(1)); //la somma delle aliquote dovrebbe essere uguale all'importo del subdoc iva (ma non e' obbligatorio)
		aliquotaS.setImposta(new BigDecimal(0.04));
		aliquotaS.setImpostaDetraibile(new BigDecimal(0.04));
		aliquotaS.setImpostaIndetraibile(new BigDecimal(0));
		aliquotaS.setTotale(new BigDecimal(1.04));
		
		listaAliquotaSubdocumentoIva.add(aliquotaS);
		siva.setListaAliquotaSubdocumentoIva(listaAliquotaSubdocumentoIva );
		
		
		AttivitaIva attivitaIva = new AttivitaIva();
		attivitaIva.setCodice("01");
		siva.setAttivitaIva(attivitaIva);
		
		TipoRegistrazioneIva tipoRegistrazioneIva = new TipoRegistrazioneIva();
		tipoRegistrazioneIva.setCodice("01");
		siva.setTipoRegistrazioneIva(tipoRegistrazioneIva);
		
		return siva;
	}
	
	
	

//	public static void main(String[] args) throws Exception {
//		testGeneraXmlPerCaricaDocumenti();
//		//testXmlIDXmlIDREF();
//	}

	@Test
	public void caricaDocumenti() throws ParseException, Exception {
		String methodName = "CaricaDocumenti";
		
//		String xmlTest = "<elenchiDocumentiAllegato ><elenchi><elenco saltaInserimento=\"true\"><annoSysEsterno>2015</annoSysEsterno><numeroSysEsterno>123</numeroSysEsterno><allegatoAtto><uid>-1</uid><causale>mia causale allegato atto</causale><datiSensibili>false</datiSensibili><attoAmministrativo> <anno>2016</anno> <!-- uguale anno di bilancio --><tipoAtto><codice>ALG</codice></tipoAtto><statoOperativo>DEFINITIVO</statoOperativo><oggetto>mia causale allegato atto</oggetto> <!-- stessa causale atto --><!-- <numero>555</numero>  --><strutturaAmmContabile><!-- 						<codice>A19000</codice> --><codice>022</codice></strutturaAmmContabile></attoAmministrativo> </allegatoAtto><subdocumenti><subdocumentoSpesa><documentoSpesa><uid>-1</uid><anno>2015</anno><numero>2015-1</numero><soggetto><codiceSoggetto>5</codiceSoggetto></soggetto><tipoDocumento><codice>FAT</codice><tipoFamigliaDocumento>SPESA</tipoFamigliaDocumento></tipoDocumento><descrizione> bla bla bla </descrizione><dataEmissione>2014-05-15T14:30:05+02:00</dataEmissione><documentiSpesaFiglio/><documentiEntrataFiglio><documentoEntrata><uid>-1</uid><anno>2015</anno><numero>1234</numero><importo>300</importo><descrizione>FATTURA ENTRATA SISTEMA ESTERNO</descrizione><arrotondamento>-1</arrotondamento><codiceBollo><codice>99</codice></codiceBollo><dataEmissione>2014-05-15T14:30:05+02:00</dataEmissione><dataScadenza>2014-05-16T14:30:05+02:00</dataScadenza><numeroRepertorio>mio numero di repertorio</numeroRepertorio><soggetto><codiceSoggetto>5</codiceSoggetto></soggetto><terminePagamento>1</terminePagamento><tipoDocumento><codice>TRB</codice><tipoFamigliaDocumento>ENTRATA</tipoFamigliaDocumento></tipoDocumento><flagDebitoreMultiplo>false</flagDebitoreMultiplo><documentiSpesaFiglio/><documentiEntrataFiglio/></documentoEntrata></documentiEntrataFiglio></documentoSpesa><dataScadenza>2014-06-16T14:30:05+02:00</dataScadenza><descrizione>FATT SYST ESTERNO S1</descrizione><flagEsproprio>false</flagEsproprio><flagOrdinativoSingolo>false</flagOrdinativoSingolo><importo>100</importo><importoDaDedurre>10</importoDaDedurre><note>PROVA INSERIMENTO QUOTA</note><noteTesoriere><codice>01</codice></noteTesoriere><causaleOrdinativo>mia causale ordinativo SERVIZIO CARICA DOCUMENTI</causaleOrdinativo><cig>mio cig</cig><commissioniDocumento>ESENTE</commissioniDocumento><cup>mio cup</cup><datiCertificazioneCrediti><annotazione>mia annotazione</annotazione><dataCertificazione>2014-09-01T00:00:00+02:00</dataCertificazione><flagCertificazione>false</flagCertificazione><noteCertificazione>mie note certificazione</noteCertificazione><numeroCertificazione>mio numero certificazione</numeroCertificazione></datiCertificazioneCrediti><impegno><annoMovimento>2015</annoMovimento><numero>2300</numero></impegno><subImpegno><annoMovimento>2015</annoMovimento><numero>14</numero></subImpegno><modalitaPagamentoSoggetto><codiceModalitaPagamento>1</codiceModalitaPagamento></modalitaPagamentoSoggetto></subdocumentoSpesa><subdocumentoSpesa><documentoSpesa><uid>-1</uid></documentoSpesa><dataScadenza>2014-06-16T14:30:05+02:00</dataScadenza><descrizione>FATT SYST ESTERNO S2</descrizione><flagEsproprio>false</flagEsproprio><flagOrdinativoSingolo>false</flagOrdinativoSingolo><importo>101</importo><importoDaDedurre>11</importoDaDedurre><note>PROVA INSERIMENTO QUOTA</note><noteTesoriere><codice>01</codice></noteTesoriere><causaleOrdinativo>mia causale ordinativo SERVIZIO CARICA DOCUMENTI</causaleOrdinativo><cig>mio cig</cig><commissioniDocumento>ESENTE</commissioniDocumento><cup>mio cup</cup><datiCertificazioneCrediti><annotazione>mia annotazione</annotazione><dataCertificazione>2014-09-01T00:00:00+02:00</dataCertificazione><flagCertificazione>false</flagCertificazione><noteCertificazione>mie note certificazione</noteCertificazione><numeroCertificazione>mio numero certificazione</numeroCertificazione></datiCertificazioneCrediti><impegno><annoMovimento>2015</annoMovimento><numero>630</numero></impegno><modalitaPagamentoSoggetto><codiceModalitaPagamento>1</codiceModalitaPagamento></modalitaPagamentoSoggetto></subdocumentoSpesa><subdocumentoEntrata><documentoEntrata><uid>-1</uid></documentoEntrata><dataScadenza>2014-06-16T14:30:05+02:00</dataScadenza><descrizione>FATT SYST ESTERNO E1</descrizione><flagEsproprio>false</flagEsproprio><flagOrdinativoSingolo>false</flagOrdinativoSingolo><importo>50</importo><importoDaDedurre>1</importoDaDedurre><note>PROVA INSERIMENTO QUOTA</note><noteTesoriere><codice>01</codice></noteTesoriere><accertamento><annoMovimento>2015</annoMovimento><numero>4</numero></accertamento><contoTesoreria><codice>01</codice></contoTesoreria><distinta><codice>E1</codice></distinta></subdocumentoEntrata></subdocumenti><subdocumentiIva/></elenco></elenchi></elenchiDocumentiAllegato>";
//		ElenchiDocumentiAllegato allegatiAttoModel = JAXBUtility.unmarshall(xmlTest, ElenchiDocumentiAllegato.class);
//		InserisceElenchiDocumenti inserisciElencoDocumenti = new InserisceElenchiDocumenti();
//		inserisciElencoDocumenti.setRichiedente(getRichiedenteByProperties("consip", "regp"));
//		inserisciElencoDocumenti.setBilancio(getBilancioByProperties("consip", "regp", "2019"));
//		inserisciElencoDocumenti.setElenchiDocumentiAllegato(allegatiAttoModel);
		
		ElaboraFile requestFile = new ElaboraFile();
		requestFile.setBilancio(getBilancioByProperties("consip", "regp", "2019"));
		requestFile.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		requestFile.setEnte(requestFile.getRichiedente().getAccount().getEnte());
		requestFile.setAccount(requestFile.getRichiedente().getAccount());
		requestFile.setDataOra(new Date());

		File file = new File();
//		file.setTipo(new TipoFile(ElaboraFileServicesEnum.DOCUMENTO_SPESA.getCodice()));
				
		byte[] contenuto;
		try {
//			contenuto = getTestXmlProgrammatically();
//			contenuto = getTestXml("test7047.xml");
			contenuto = getTestXml("test7047-2.xml");
			log.info(methodName, "Xml di partenza: "+new String(contenuto, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
			return;
		} 
		file.setContenuto(contenuto);
		file.setTipo(new TipoFile(ElaboraFileServicesEnum.DOCUMENTO_SPESA.getCodice()));
		
		requestFile.setFile(file);
		
		ElaboraFileResponse efr = elaboraFileService.executeService(requestFile);
		
		log.debug("INFO", efr.toString());
	
		assertNotNull(efr);
//		assertTrue(Esito.values().equals("SUCCESSO"));
	}


	private static void testGeneraXmlPerCaricaDocumenti() throws ParseException {
		ElenchiDocumentiAllegato allegatiAttoModel = populateAllegatiAtto();
		String contenutoXml = JAXBUtility.marshall(allegatiAttoModel);
		
		System.out.println(contenutoXml);
	}
	





	@SuppressWarnings("unused")
	private static void testXmlIDXmlIDREF() throws JAXBException {
		Cliente c1 = new Cliente("c1", "Domenico", "Lisi");
		Cliente c2 = new Cliente("c1", "Giovanni", "Nuu");
		
		Fattura f1 = new Fattura(c1, 1, BigDecimal.valueOf(2000));
		System.out.println("Fattura f1:\n"+JAXBUtility.marshall(f1));
		System.out.println("Fattura f1:\n"+marshall(f1, Cliente.class, Fattura.class));
		Fattura f2 = new Fattura(c2, 2, BigDecimal.valueOf(3000));
		Fattura f3 = new Fattura(c1, 3, BigDecimal.valueOf(1000));
		
		Container container = new Container();
		container.getClienti().add(c1);
		container.getClienti().add(c2);
		
		container.getFatture().add(f1);
		container.getFatture().add(f2);
		container.getFatture().add(f3);
		
	
		
		
		System.out.println("Container: ");
		String xml = JAXBUtility.marshall(container);
		System.out.println(xml);
		
		Container container1 = JAXBUtility.unmarshall(xml, Container.class);
		for(Cliente c : container1.getClienti()){
			System.out.println("cliente: "+c.getIdCliente()+ " " + c);
		}
		for(Fattura f : container1.getFatture()){
			System.out.println("fattura: "+f.getNumeroFattura()+" "+f + " cliente: "+f.getCliente());
		}
	}
	
	private static String marshall(Object o, Class<?>... contextClasses ) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(contextClasses);
        Writer writer = new StringWriter();
        context.createMarshaller().marshal(o, writer);
        return writer.toString();
	}
	
	@XmlType
	@XmlRootElement
	public  static class Container {
		private List<Fattura> fatture = new ArrayList<Fattura>();
		private List<Cliente> clienti = new ArrayList<Cliente>();

		
		public Container() {
		}
		
		/**
		 * @return the clienti
		 */
		public List<Cliente> getClienti() {
			return clienti;
		}

		/**
		 * @param clienti the clienti to set
		 */
		public void setClienti(List<Cliente> clienti) {
			this.clienti = clienti;
		}
		

		/**
		 * @return the fatture
		 */
		public List<Fattura> getFatture() {
			return fatture;
		}

		/**
		 * @param fatture the fatture to set
		 */
		public void setFatture(List<Fattura> fatture) {
			this.fatture = fatture;
		}
		
		
		
	}
	
	
	
	@XmlType
	@XmlRootElement
	public  static class Fattura {
		@XmlIDREF 
		private Cliente cliente;
		private Integer numeroFattura;
		private BigDecimal importo;
		
		public Fattura() {
		}
		
		public Fattura(Cliente cliente, Integer numeroFattura, BigDecimal importo) {
			super();
			this.cliente = cliente;
			this.numeroFattura = numeroFattura;
			this.importo = importo;
		}
		/**
		 * @return the cliente
		 */
		@XmlTransient
		public Cliente getCliente() {
			return cliente;
		}
		/**
		 * @param cliente the cliente to set
		 */
		public void setCliente(Cliente cliente) {
			this.cliente = cliente;
		}
		/**
		 * @return the numeroFattura
		 */
		public Integer getNumeroFattura() {
			return numeroFattura;
		}
		/**
		 * @param numeroFattura the numeroFattura to set
		 */
		public void setNumeroFattura(Integer numeroFattura) {
			this.numeroFattura = numeroFattura;
		}
		/**
		 * @return the importo
		 */
		public BigDecimal getImporto() {
			return importo;
		}
		/**
		 * @param importo the importo to set
		 */
		public void setImporto(BigDecimal importo) {
			this.importo = importo;
		}
		
		
	}
	
	@XmlType
	@XmlRootElement
	public static class Cliente {
		@XmlID
		private String idCliente;
		private String nome;
		private String cognome;
		
		
		public Cliente() {
		}
		
		public Cliente(String idCliente, String nome, String cognome) {
			super();
			this.idCliente = idCliente;
			this.nome = nome;
			this.cognome = cognome;
		}
		/**
		 * @return the idCliente
		 */
		@XmlTransient
		public String getIdCliente() {
			return idCliente;
		}
		/**
		 * @param idCliente the idCliente to set
		 */
		public void setIdCliente(String idCliente) {
			this.idCliente = idCliente;
		}
		/**
		 * @return the nome
		 */
		public String getNome() {
			return nome;
		}
		/**
		 * @param nome the nome to set
		 */
		public void setNome(String nome) {
			this.nome = nome;
		}
		/**
		 * @return the cognome
		 */
		public String getCognome() {
			return cognome;
		}
		/**
		 * @param cognome the cognome to set
		 */
		public void setCognome(String cognome) {
			this.cognome = cognome;
		}
	}
	
	
//		public static void main(String[] args) throws Exception {
//        JAXBContext jc = JAXBContext.newInstance(LeggiStatoElaborazioneDocumento.class);   
//        jc.generateSchema(new SchemaOutputResolver() {
//
//            @Override
//            public Result createOutput(String namespaceURI, String suggestedFileName)
//                throws IOException {
//                return new StreamResult("docs/test/documenti/a.xsd");
//            }
//
//        });
//        
//        
//        
//    	public static void main(String[] args) throws IOException, JAXBException {
//            // TODO Auto-generated method stub
//            (JAXBContext.newInstance(LeggiStatoElaborazioneDocumento.class)).generateSchema(new DataSchemaOutputResolver());
//        }
//            
//    	
//    	public class DataSchemaOutputResolver extends SchemaOutputResolver {
//    	    @Override
//    	    public Result createOutput(String arg0, String arg1) throws IOException {
//    	        // TODO Auto-generated method stub
//    	        return new StreamResult(new File("d:/data.xsd"));
//    	    }
//    	}
//
//    }
	
	
	
	
}
