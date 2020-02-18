/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentoentrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AssociaProvvisorioQuoteEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaDettaglioQuotaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaQuotaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaQuoteByDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaQuoteDaEmettereEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaQuotePerProvvisorioEntrataService;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaProvvisorioQuoteEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaProvvisorioQuoteEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

// TODO: Auto-generated Javadoc
/**
 * The Class SubdocumentoSpesaDLTest.
 */
public class SubdocumentoEntrataTest extends BaseJunit4TestCase {
	
	
	@Autowired
	private RicercaQuotaEntrataService ricercaQuotaEntrataService;
	
	
	@Autowired
	private RicercaDettaglioQuotaEntrataService ricercaDettaglioQuotaEntrataService;
	
	@Autowired
	private InserisceQuotaDocumentoEntrataService inserisceQuotaDocumentoEntrataService;
	
	@Autowired
	private AggiornaQuotaDocumentoEntrataService aggiornaQuotaDocumentoEntrataService;
	
	@Autowired
	private RicercaQuoteByDocumentoEntrataService ricercaQuoteByDocumentoEntrataService;
	
	@Autowired
	private RicercaQuoteDaEmettereEntrataService ricercaQuoteDaEmettereEntrataService;
	
	@Autowired
	private RicercaQuotePerProvvisorioEntrataService ricercaQuoteEntrataPerProvvisorioService;
	
	@Autowired
	private AssociaProvvisorioQuoteEntrataService asssociaProvvisorioQuoteEntrataService;
	
	@Test
	public void asssociaProvvisorioQuoteEntrata(){
		
		AssociaProvvisorioQuoteEntrata req = new  AssociaProvvisorioQuoteEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ProvvisorioDiCassa provvisorioDiCassa = new ProvvisorioDiCassa();
		provvisorioDiCassa.setUid(5959);
		req.setProvvisorioDiCassa(provvisorioDiCassa);
		List<SubdocumentoEntrata> listaQuote = new ArrayList<SubdocumentoEntrata>();
		SubdocumentoEntrata quota1 = new SubdocumentoEntrata();
		SubdocumentoEntrata quota2 = new SubdocumentoEntrata();
		quota1.setUid(600);
		quota2.setUid(601);
		listaQuote.add(quota1);
		listaQuote.add(quota2);
		req.setListaQuote(listaQuote);
		AssociaProvvisorioQuoteEntrataResponse res = asssociaProvvisorioQuoteEntrataService.executeService(req);
		
		assertNotNull(res);
	}
	
	@Test
	public void ricercaQuotePerProvvisorio(){
		
		RicercaQuotePerProvvisorioEntrata req = new  RicercaQuotePerProvvisorioEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
//		req.setAnnoDocumento(2015);
//		req.setAnnoMovimento(2015);
//		req.setNumeroMovimento(new BigDecimal("4"));
		Soggetto soggetto = new Soggetto();
//		soggetto.setUid(14);
		soggetto.setCodiceSoggetto("5");
		req.setSoggetto(soggetto);
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setUid(32);
		req.setTipoDocumento(tipoDocumento);
		RicercaQuotePerProvvisorioEntrataResponse res = ricercaQuoteEntrataPerProvvisorioService.executeService(req);
		
		assertNotNull(res);
	}
	
	@Test
	public void ricercaQuoteByDocumentoEntrata(){
		
		RicercaQuoteByDocumentoEntrata req = new RicercaQuoteByDocumentoEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(105);
		req.setDocumentoEntrata(documentoEntrata);
		RicercaQuoteByDocumentoEntrataResponse res = ricercaQuoteByDocumentoEntrataService.executeService(req);
		
		assertNotNull(res);
	}
	

	@Test
	public void ricercaQuotaDocumentoEntrata (){
		RicercaQuotaEntrata req = new RicercaQuotaEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
//		req.setAnnoMovimento(2013);
//		req.setAnnoProvvedimento(2013);
//		req.setAnnoProvvisorio(2013);
//		req.setDataEmissioneDocumento(dataEmissioneDocumento);
//		req.setDataProvvisorio(dataProvvisorio);
		
		req.setAnnoDocumento(2014);
		req.setNumeroDocumento("ccc");
//		
//		req.setNumeroMovimento(new BigDecimal(1));
//		req.setNumeroProvvedimento(50000);
//		req.setNumeroQuota(1);
		
//		Soggetto soggetto = new Soggetto();
//		soggetto.setUid(9);
//		req.setSoggetto(soggetto);
		
//		TipoAtto tipoAtto = new TipoAtto();
//		tipoAtto.setUid(3);
//		req.setTipoAtto(tipoAtto);
//		
//		TipoDocumento tipoDocumento = new TipoDocumento();
//		tipoDocumento.setUid(6);
//		req.setTipoDocumento(tipoDocumento);
//		
		
//		StrutturaAmministrativoContabile strutt = new StrutturaAmministrativoContabile();
//		strutt.setUid(1);
//		req.setStruttAmmContabile(strutt);
//		
		
		RicercaQuotaEntrataResponse res = ricercaQuotaEntrataService.executeService(req);
		
		assertNotNull(res);
	}
	
	
	@Test
	public void ricercaDettaglioQuotaDocumentoEntrata (){
		int uidQuota = 0;
		RicercaDettaglioQuotaEntrataResponse res = caricaDettaglioQuota(uidQuota);
		
		assertNotNull(res);
	}

	/**
	 * @param uidQuota
	 * @return
	 */
	private RicercaDettaglioQuotaEntrataResponse caricaDettaglioQuota(int uidQuota) {
		RicercaDettaglioQuotaEntrata req = new RicercaDettaglioQuotaEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		SubdocumentoEntrata sd = new SubdocumentoEntrata();
		sd.setUid(uidQuota);
		req.setSubdocumentoEntrata(sd);
		
		RicercaDettaglioQuotaEntrataResponse res = ricercaDettaglioQuotaEntrataService.executeService(req);
		return res;
	}
	
	
	@Test
	public void aggiornaQuotaDocumentoEntrata(){
		
		
		RicercaDettaglioQuotaEntrataResponse caricaDettaglioQuota = caricaDettaglioQuota(108600);
		
		if(caricaDettaglioQuota.hasErrori()) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile caricare la quota di entrata."));
		}
		
		AggiornaQuotaDocumentoEntrata req = new AggiornaQuotaDocumentoEntrata();
		
		req.setBilancio(getBilancioByProperties("consip", "regp", "2019"));
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		SubdocumentoEntrata se = caricaDettaglioQuota.getSubdocumentoEntrata();
		
		Accertamento accertamento = new Accertamento();
		accertamento.setAnnoMovimento(2019);
		accertamento.setNumero(new BigDecimal("192"));
		accertamento.setUid(120127);
		se.setAccertamento(accertamento);
		
		req.setSubdocumentoEntrata(se);
		req.setGestisciModificaImporto(false);
		
		AggiornaQuotaDocumentoEntrataResponse res = aggiornaQuotaDocumentoEntrataService.executeService(req);

		assertNotNull(res);
	}
	
	
	@Test
	public void inserisceQuotaDocumentoEntrata(){
		
		InserisceQuotaDocumentoEntrata req = new InserisceQuotaDocumentoEntrata();
		
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(6);
		FaseEStatoAttualeBilancio faseEStatoAttualeBilancio = new FaseEStatoAttualeBilancio();
		faseEStatoAttualeBilancio.setFaseBilancio(FaseBilancio.GESTIONE);
		bilancio.setFaseEStatoAttualeBilancio(faseEStatoAttualeBilancio );
		req.setBilancio(bilancio);
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		SubdocumentoEntrata se = new SubdocumentoEntrata();
		
		se.setEnte(getEnteTest());
		
		DocumentoEntrata doc = new DocumentoEntrata();
		doc.setUid(261);
		se.setDocumento(doc);
		
		
		se.setImporto(new BigDecimal("100"));
		
//		TipoAvviso tipoAvviso = new TipoAvviso(); //41158,41163
//		tipoAvviso.setUid(41158);
//		se.setTipoAvviso(tipoAvviso);
		
//		
//		SubImpegno subImpegno = new SubImpegno();
//		subImpegno.setUid(5);		
//		sd.setSubImpegno(subImpegno);
		
//		Accertamento accertamento = new Accertamento();
//		accertamento.setAnnoMovimento(2014);
//		accertamento.setNumero(new BigDecimal("124"));
//		se.setAccertamento(accertamento);
		
		AttoAmministrativo atto = new AttoAmministrativo();
		atto.setUid(8);
//		atto.setAnno(2013);
//		atto.setNumero(25);
		se.setAttoAmministrativo(atto);
		
//		ProvvisorioDiCassa provvisorio = new ProvvisorioDiCassa();
//		provvisorio.setAnno(2014);
//		provvisorio.setNumero(90);
//		se.setProvvisorioCassa(provvisorio);
		
		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
		capitolo.setAnnoCapitolo(2014);
		capitolo.setNumeroArticolo(1);
		capitolo.setNumeroCapitolo(3);
		capitolo.setNumeroUEB(1);
		req.setCapitolo(capitolo);
		
		
		req.setSubdocumentoEntrata(se);
		
		InserisceQuotaDocumentoEntrataResponse res = inserisceQuotaDocumentoEntrataService.executeService(req);

		assertNotNull(res);
	}
	
	

	
	@Test
	public void ricercaQuoteDaEmettereEntrata (){
		List<String> results = new ArrayList<String>();
		int numPagina = 0;
		int i = 0;
		
		RicercaQuoteDaEmettereEntrata req = new RicercaQuoteDaEmettereEntrata();
		RicercaQuoteDaEmettereEntrataResponse res;
		req.setRichiedente(getRichiedenteByProperties("forn2", "edisu"));
		req.setAnnoElenco(2016);
		req.setNumeroElencoDa(1);
		req.setNumeroElencoA(9999);
		
		do {
			req.setParametriPaginazione(getParametriPaginazione(numPagina++, 10));
			res = ricercaQuoteDaEmettereEntrataService.executeService(req);
			for(SubdocumentoEntrata s : res.getListaSubdocumenti()) {
				String row = String.format("ROW %3d => ", i);
				results.add(row + printSubdocumentoEntrata(s));
				i++;
			}
		} while(numPagina < res.getTotalePagine());
		
		for(String str : results) {
			log.info("ricercaQuoteDaEmettereEntrata", str);
		}
		assertNotNull(res);
	}
	
	private String printSubdocumentoEntrata(SubdocumentoEntrata se) {
		if(se == null) {
			return "";
		}
		return new StringBuilder()
				.append(printAttoAmministrativo(se.getAttoAmministrativo()))
				.append("  ")
				.append(printElenco(se.getElencoDocumenti()))
				.append("  ")
				.append(printDocumento(se))
				.toString();
	}
	
	private String printAttoAmministrativo(AttoAmministrativo aa) {
		if(aa == null) {
			return "NULL";
		}
		
		StringBuilder sb = new StringBuilder()
				.append(aa.getAnno())
				.append("/")
				.append(aa.getNumero());
		if(aa.getStrutturaAmmContabile() != null) {
			sb.append("/")
				.append(aa.getStrutturaAmmContabile().getCodice());
		}
		String res = sb.toString();
		return String.format("%14s", res);
	}
	
	
	private String printElenco(ElencoDocumentiAllegato eda) {
		if(eda == null) {
			return "NULL";
		}
		String res = new StringBuilder()
				.append(eda.getAnno())
				.append("/")
				.append(eda.getNumero())
				.toString();
		return String.format("%9s", res);
	}
	
	/**
	 * @return the domStringDocumento
	 */
	private String printDocumento(SubdocumentoEntrata se) {
		if(se.getDocumento() == null) {
			return "NULL";
		}
		
		StringBuilder sb = new StringBuilder()
				.append(se.getDocumento().getAnno());
	
		if(se.getDocumento().getTipoDocumento() != null) {
			sb.append("/")
				.append(se.getDocumento().getTipoDocumento().getCodice());
		}
		String res = sb.append("/")
				.append(se.getDocumento().getNumero())
				.append("-")
				.append(se.getNumero())
				.toString();
		//23
		return String.format("%36s", res);
	}
}
