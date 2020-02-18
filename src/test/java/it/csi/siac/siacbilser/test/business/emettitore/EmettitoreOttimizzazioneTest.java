/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.emettitore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuoteDaEmettereSpesaService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiPagamentoDaElencoService;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.dad.OnereSpesaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAmmStatoEnum;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesaResponse;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.RitenuteDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

// TODO: Auto-generated Javadoc
/**
 * The Class SubdocumentoSpesaDLTest.
 */
public class EmettitoreOttimizzazioneTest extends BaseJunit4TestCase {
	
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	@Autowired
	private EmetteOrdinativiDiPagamentoDaElencoService emetteOrdinativiDiPagamentoDaElenco;
	
	@Autowired OnereSpesaDad onereDad;
	
//	@Autowired
//	private SubdocumentoDaoImpl subdocumentoDaoImpl;
	

	@Test
	public void testQueryricercaQuoteByElenco() {
		subdocumentoSpesaDad.setEnte(create(Ente.class, 1));
		List<StatoOperativoAtti> statiOperativiDocumento = new ArrayList<StatoOperativoAtti>();
		//	StatoOperativoDocumento  <>  A – ANNULLATO o S-STORNATO o E – EMESSO

		final Set<StatoOperativoAtti> statiOperativiSet = EnumSet.allOf(StatoOperativoAtti.class);
		statiOperativiSet.remove(StatoOperativoAtti.ANNULLATO);
		statiOperativiDocumento.addAll(statiOperativiSet);
		
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = SiacDAttoAmmStatoEnum.byStatiOperativi(statiOperativiSet);
		
		List<ElencoDocumentiAllegato> eldocIds = new ArrayList<ElencoDocumentiAllegato>();
		eldocIds.add(create(ElencoDocumentiAllegato.class,372));
//		eldocIds.add(create(ElencoDocumentiAllegato.class,369));
//		eldocIds.add(create(ElencoDocumentiAllegato.class,370));
				
		List<SubdocumentoSpesa> ricercaSubdocumentiSpesaDaEmettereByAttoAmministrativo = subdocumentoSpesaDad.ricercaSubdocumentiSpesaDaEmettereByElenco(eldocIds,Boolean.TRUE);
		
		System.out.println("ho trovato: " + ricercaSubdocumentiSpesaDaEmettereByAttoAmministrativo.size() + "quote da emettere.");
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		for (SubdocumentoSpesa subdocumentoSpesa : ricercaSubdocumentiSpesaDaEmettereByAttoAmministrativo) {
			System.out.println(" subdocumentoSpesa.getFlagOrdinativoSingolo() " + subdocumentoSpesa.getFlagOrdinativoSingolo());
			log.logXmlTypeObject(subdocumentoSpesa, "subdocumentoSpesa");
		}
	}
	
	@Test
	public void testQueryricercaQuoteByEnte() {
		
		List<SubdocumentoSpesa> ricercaSubdocumentiSpesaDaEmettereByAttoAmministrativo = subdocumentoSpesaDad.ricercaSubdocumentiSpesaDaEmettereByEnte(Boolean.TRUE);
		
		System.out.println("ho trovato: " + ricercaSubdocumentiSpesaDaEmettereByAttoAmministrativo.size() + "quote da emettere.");
		
	}

	private RicercaQuoteDaEmettereSpesa creaRequestRicercaQuoteDaEmettere(int numeroPagina, Integer uidElenco, Integer annoElenco, Integer numeroElenco, Integer uidProvvedimento, Integer annoProvvedimento, Integer numeroProvvedimento) {
		RicercaQuoteDaEmettereSpesa reqRQDE = new RicercaQuoteDaEmettereSpesa();
		reqRQDE.setDataOra(new Date());
		reqRQDE.setRichiedente(getRichiedenteByProperties("forn2", "crp"));
		
		
		reqRQDE.setUidElenco(uidElenco);
//		reqRQDE.setAnnoElenco(annoElenco);
//		reqRQDE.setNumeroElenco(numeroElenco);
		reqRQDE.setUidProvvedimento(uidProvvedimento);
		reqRQDE.setAnnoProvvedimento(annoProvvedimento);
		reqRQDE.setNumeroProvvedimento(numeroProvvedimento);
		reqRQDE.setFlagConvalidaManuale(Boolean.TRUE);
		
		ParametriPaginazione pp = new ParametriPaginazione(numeroPagina);
		pp.setElementiPerPagina(20);
		reqRQDE.setParametriPaginazione(pp);
		return reqRQDE;
	}
	@Autowired
	private RicercaQuoteDaEmettereSpesaService ricercaQuoteDaEmettereService;
	
	
	@Test
	public void ricercaQuoteDaEmettereSpesaService(){
		//RicercaQuoteDaEmettereSpesa reqRQDE = creaRequestRicercaQuoteDaEmettere(0, null, null, null, null, null, null);
		RicercaQuoteDaEmettereSpesa req = new RicercaQuoteDaEmettereSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(2017);
//		req.setAnnoProvvedimento(Integer.valueOf(2012));
//		req.setNumeroProvvedimento(1546);
//		req.setTipoAtto(create(TipoAtto.class,285));
//		req.setStruttAmmContabile(create(StrutturaAmministrativoContabile.class, 285));
		req.setFlagConvalidaManuale(Boolean.TRUE);
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setSoggetto(create(Soggetto.class, 0));
		req.getSoggetto().setCodiceSoggetto("10508");
		req.setAnnoElenco(Integer.valueOf(2017));
		req.setNumeroElencoDa(Integer.valueOf(6246));
		req.setNumeroElencoA(Integer.valueOf(6246));
		
		RicercaQuoteDaEmettereSpesaResponse res = ricercaQuoteDaEmettereService.executeService(req);
		for (SubdocumentoSpesa subdocumentoSpesa : res.getListaSubdocumenti()) {
			Soggetto sog = subdocumentoSpesa.getDocumento().getSoggetto();
			Impegno imp = subdocumentoSpesa.getImpegno();
			System.out.println("data durc: " + sog.getDataFineValiditaDurc());
			System.out.println("impegno durc: " + imp.isFlagSoggettoDurc());
			
		}
		assertNotNull(res);
	}
	
	@Autowired DocumentoDad documentoDad;
	@Test
	public void controllaSubordinati(){
		Long countDocumentiFigli = documentoDad.countDocumentiFigli(1062);
		boolean hasSubordinati = Long.valueOf(0L).compareTo(countDocumentiFigli)  < 0;
		System.out.println(countDocumentiFigli);
		
		System.out.println("hasSubordinati? " + hasSubordinati);
	}
	
	public static void main(String[] args) {
		Long countDocumentiFigli = Long.valueOf(0L);
		boolean hasSubordinati = Long.valueOf(0L).compareTo(countDocumentiFigli) < 0;
		
		System.out.println("countDocumentiFigli " + countDocumentiFigli);
		System.out.println("hasSubordinati? " + hasSubordinati);
		
		countDocumentiFigli = Long.valueOf("2");
		hasSubordinati = Long.valueOf(0L).compareTo(countDocumentiFigli)  < 0;
		System.out.println("countDocumentiFigli " + countDocumentiFigli);
		System.out.println("hasSubordinati? " + hasSubordinati);
		
	}
	
	@Test
	public void emetteOrdinativiDiPagamentoDaElenco() {
		final String methodName = "emetteOrdinativiDiPagamentoDaElenco1";
		EmetteOrdinativiDiPagamentoDaElenco req = new EmetteOrdinativiDiPagamentoDaElenco();
		req.setBilancio(getBilancioTest(131, 2017));
		req.setFlagConvalidaManuale(Boolean.TRUE);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
	
		List<ElencoDocumentiAllegato> elenchi = new ArrayList<ElencoDocumentiAllegato>();
		int[] uids = new int[] {
				11632
		};
		
		for(int uid : uids) {
			elenchi.add(create(ElencoDocumentiAllegato.class, uid));
		}
		req.setElenchi(elenchi);
		
//		req.setSubdocumenti(new ArrayList<SubdocumentoSpesa>());
//		req.getSubdocumenti().add(create(SubdocumentoSpesa.class, 37520));
		
		
		EmetteOrdinativiDiPagamentoDaElencoResponse res = emetteOrdinativiDiPagamentoDaElenco.executeService(req);
		assertNotNull(res);
//		
//		try {
//			Thread.sleep(5 * 60 * 1000);
//		} catch (InterruptedException e) {
//			log.error(methodName, "InterruptedException waiting for async method to finish");
//		}
	}
	
	
//	
//	public static void main(String[] args) {
//		List<Integer> l1 = new ArrayList<Integer>();
//		List<Integer> l2 = new ArrayList<Integer>();
//		
//		l2.add(13800);
//		l2.add(13801);
//		l2.add(13802);
//		l2.add(13803);
//		l2.add(13804);
//		l2.add(13805);
//		l2.add(13806);
//		l2.add(13807);
//		l2.add(13808);
//		l2.add(13809);
//		l2.add(13810);
//		l2.add(13811);
//		l2.add(13812);
//		l2.add(13813);
//		l2.add(13814);
//		l2.add(13815);
//		l2.add(13816);
//		l2.add(13817);
//		l2.add(13818);
//		l2.add(13819);
//		l2.add(13821);
//		l2.add(13822);
//		l2.add(13823);
//		l2.add(13789);
//		l2.add(13790);
//		l2.add(13791);
//		l2.add(13792);
//		l2.add(13793);
//		l2.add(13794);
//		l2.add(13795);
//		l2.add(13796);
//		l2.add(13797);
//		l2.add(13760);
//		l2.add(13761);
//		l2.add(13762);
//		l2.add(13763);
//		l2.add(13764);
//		l2.add(13765);
//		l2.add(13766);
//		l2.add(13767);
//		l2.add(13768);
//		l2.add(13769);
//		l2.add(13770);
//		l2.add(13771);
//		l2.add(13772);
//		l2.add(13773);
//		l2.add(13774);
//		l2.add(13775);
//		l2.add(13776);
//		l2.add(13777);
//		l2.add(13778);
//		l2.add(13779);
//		l2.add(13780);
//		l2.add(13781);
//		l2.add(13782);
//		l2.add(13783);
//		l2.add(13784);
//		l2.add(13785);
//		l2.add(13786);
//		l2.add(13787);
//		l2.add(13788);
//		l2.add(13754);
//		l2.add(13755);
//		l2.add(13756);
//		l2.add(13757);
//		l2.add(13758);
//		l2.add(13720);
//		l2.add(13721);
//		l2.add(13722);
//		l2.add(13723);
//		l2.add(13724);
//		l2.add(13725);
//		l2.add(13726);
//		l2.add(13727);
//		l2.add(13728);
//		l2.add(13729);
//		l2.add(13730);
//		l2.add(13731);
//		l2.add(13732);
//		l2.add(13733);
//		l2.add(13734);
//		l2.add(13735);
//		l2.add(13736);
//		l2.add(13737);
//		l2.add(13738);
//		l2.add(13739);
//		l2.add(13740);
//		l2.add(13741);
//		l2.add(13742);
//		l2.add(13743);
//		l2.add(13744);
//		l2.add(13745);
//		l2.add(13746);
//		l2.add(13747);
//		l2.add(13748);
//		l2.add(13749);
//		l2.add(13750);
//		l2.add(13751);
//		l2.add(13752);
//		l2.add(13753);
//		l2.add(13685);
//		l2.add(13686);
//		l2.add(13687);
//		l2.add(13688);
//		l2.add(13689);
//		l2.add(13690);
//		l2.add(13691);
//		l2.add(13692);
//		l2.add(13693);
//		l2.add(13694);
//		l2.add(13695);
//		l2.add(13696);
//		l2.add(13697);
//		l2.add(13698);
//		l2.add(13699);
//		l2.add(13700);
//		l2.add(13701);
//		l2.add(13702);
//		l2.add(13703);
//		l2.add(13704);
//		l2.add(13705);
//		l2.add(13706);
//		l2.add(13707);
//		l2.add(13708);
//		l2.add(13709);
//		l2.add(13710);
//		l2.add(13711);
//		l2.add(13712);
//		l2.add(13713);
//		l2.add(13714);
//		l2.add(13716);
//		l2.add(13717);
//		l2.add(13718);
//		l2.add(13719);
//		l2.add(13650);
//		l2.add(13651);
//		l2.add(13653);
//		l2.add(13654);
//		l2.add(13655);
//		l2.add(13656);
//		l2.add(13657);
//		l2.add(13658);
//		l2.add(13659);
//		l2.add(13660);
//		l2.add(13661);
//		l2.add(13662);
//		l2.add(13663);
//		l2.add(13664);
//		l2.add(13665);
//		l2.add(13666);
//		l2.add(13667);
//		l2.add(13668);
//		l2.add(13669);
//		l2.add(13670);
//		l2.add(13671);
//		l2.add(13672);
//		l2.add(13673);
//		l2.add(13674);
//		l2.add(13675);
//		l2.add(13676);
//		l2.add(13677);
//		l2.add(13678);
//		l2.add(13679);
//		l2.add(13680);
//		l2.add(13681);
//		l2.add(13682);
//		l2.add(13683);
//		l2.add(13684);
//		l2.add(13617);
//		l2.add(13618);
//		l2.add(13619);
//		l2.add(13620);
//		l2.add(13622);
//		l2.add(13623);
//		l2.add(13624);
//		l2.add(13615);
//		l2.add(13616);
//		l2.add(13625);
//		l2.add(13626);
//		l2.add(13627);
//		l2.add(13628);
//		l2.add(13629);
//		l2.add(13630);
//		l2.add(13631);
//		l2.add(13632);
//		l2.add(13633);
//		l2.add(13634);
//		l2.add(13635);
//		l2.add(13636);
//		l2.add(13637);
//		l2.add(13638);
//		l2.add(13639);
//		l2.add(13640);
//		l2.add(13641);
//		l2.add(13642);
//		l2.add(13820);
//		l2.add(13759);
//		l2.add(13715);
//		l2.add(13652);
//		l2.add(13621);
//		
//		l1.add(13800);
//		l1.add(13801);
//		l1.add(13802);
//		l1.add(13803);
//		l1.add(13804);
//		l1.add(13805);
//		l1.add(13806);
//		l1.add(13807);
//		l1.add(13808);
//		l1.add(13809);
//		l1.add(13810);
//		l1.add(13811);
//		l1.add(13812);
//		l1.add(13813);
//		l1.add(13814);
//		l1.add(13815);
//		l1.add(13816);
//		l1.add(13817);
//		l1.add(13818);
//		l1.add(13819);
//		l1.add(13821);
//		l1.add(13822);
//		l1.add(13823);
//		l1.add(13789);
//		l1.add(13790);
//		l1.add(13791);
//		l1.add(13792);
//		l1.add(13793);
//		l1.add(13794);
//		l1.add(13795);
//		l1.add(13796);
//		l1.add(13797);
//		l1.add(13760);
//		l1.add(13761);
//		l1.add(13762);
//		l1.add(13763);
//		l1.add(13764);
//		l1.add(13765);
//		l1.add(13766);
//		l1.add(13767);
//		l1.add(13768);
//		l1.add(13769);
//		l1.add(13770);
//		l1.add(13771);
//		l1.add(13772);
//		l1.add(13773);
//		l1.add(13774);
//		l1.add(13775);
//		l1.add(13776);
//		l1.add(13777);
//		l1.add(13778);
//		l1.add(13779);
//		l1.add(13780);
//		l1.add(13781);
//		l1.add(13782);
//		l1.add(13783);
//		l1.add(13784);
//		l1.add(13785);
//		l1.add(13786);
//		l1.add(13787);
//		l1.add(13788);
//		l1.add(13754);
//		l1.add(13755);
//		l1.add(13756);
//		l1.add(13757);
//		l1.add(13758);
//		l1.add(13720);
//		l1.add(13721);
//		l1.add(13722);
//		l1.add(13723);
//		l1.add(13724);
//		l1.add(13725);
//		l1.add(13726);
//		l1.add(13727);
//		l1.add(13728);
//		l1.add(13729);
//		l1.add(13730);
//		l1.add(13731);
//		l1.add(13732);
//		l1.add(13733);
//		l1.add(13734);
//		l1.add(13735);
//		l1.add(13736);
//		l1.add(13737);
//		l1.add(13738);
//		l1.add(13739);
//		l1.add(13740);
//		l1.add(13741);
//		l1.add(13742);
//		l1.add(13743);
//		l1.add(13744);
//		l1.add(13745);
//		l1.add(13746);
//		l1.add(13747);
//		l1.add(13748);
//		l1.add(13749);
//		l1.add(13750);
//		l1.add(13751);
//		l1.add(13752);
//		l1.add(13753);
//		l1.add(13685);
//		l1.add(13686);
//		l1.add(13687);
//		l1.add(13688);
//		l1.add(13689);
//		l1.add(13690);
//		l1.add(13691);
//		l1.add(13692);
//		l1.add(13693);
//		l1.add(13694);
//		l1.add(13695);
//		l1.add(13696);
//		l1.add(13697);
//		l1.add(13698);
//		l1.add(13699);
//		l1.add(13700);
//		l1.add(13701);
//		l1.add(13702);
//		l1.add(13703);
//		l1.add(13704);
//		l1.add(13705);
//		l1.add(13706);
//		l1.add(13707);
//		l1.add(13708);
//		l1.add(13709);
//		l1.add(13710);
//		l1.add(13711);
//		l1.add(13712);
//		l1.add(13713);
//		l1.add(13714);
//		l1.add(13716);
//		l1.add(13717);
//		l1.add(13718);
//		l1.add(13719);
//		l1.add(13650);
//		l1.add(13651);
//		l1.add(13653);
//		l1.add(13654);
//		l1.add(13655);
//		l1.add(13656);
//		l1.add(13657);
//		l1.add(13658);
//		l1.add(13659);
//		l1.add(13660);
//		l1.add(13661);
//		l1.add(13662);
//		l1.add(13663);
//		l1.add(13664);
//		l1.add(13665);
//		l1.add(13666);
//		l1.add(13667);
//		l1.add(13668);
//		l1.add(13669);
//		l1.add(13670);
//		l1.add(13671);
//		l1.add(13672);
//		l1.add(13673);
//		l1.add(13674);
//		l1.add(13675);
//		l1.add(13676);
//		l1.add(13677);
//		l1.add(13678);
//		l1.add(13679);
//		l1.add(13680);
//		l1.add(13681);
//		l1.add(13682);
//		l1.add(13683);
//		l1.add(13684);
//		l1.add(13617);
//		l1.add(13618);
//		l1.add(13619);
//		l1.add(13620);
//		l1.add(13622);
//		l1.add(13623);
//		l1.add(13624);
//		l1.add(13615);
//		l1.add(13616);
//		l1.add(13625);
//		l1.add(13626);
//		l1.add(13627);
//		l1.add(13628);
//		l1.add(13629);
//		l1.add(13630);
//		l1.add(13631);
//		l1.add(13632);
//		l1.add(13633);
//		l1.add(13634);
//		l1.add(13635);
//		l1.add(13636);
//		l1.add(13637);
//		l1.add(13638);
//		l1.add(13639);
//		l1.add(13640);
//		l1.add(13641);
//		l1.add(13642);
//		l1.add(13820);
//		l1.add(13759);
//		l1.add(13715);
//		l1.add(13652);
//		l1.add(13621);
//
//		
//		boolean sameSize = l1.size() == l2.size();
//		System.out.println("l1.size() == l2.size() ? " + sameSize);
//		if(!sameSize) {
//			return;
//		}
//		
//		for(int i = 0; i < l1.size(); i++) {
//			Integer i1 = l1.get(i);
//			Integer i2 = l2.get(i);
//			if(!i1.equals(i2)) {
//				System.out.println("Indice " + i + ", differenti valori (" + i1 + ", " + i2 + ")");
//				return;
//			}
//		}
//		System.out.println("equals!!");
//
//	}
//	

	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	@Test
	public void testContareQuote(){
		SubdocumentoEntrata subdocumentoEntrata = new SubdocumentoEntrata();
		subdocumentoEntrata.setUid(2242);
		Long countOrdinativiAssociatiAQuota = subdocumentoEntrataDad.countOrdinativiAssociatiAQuota(subdocumentoEntrata);
		System.out.println(countOrdinativiAssociatiAQuota);
	}
	
	@Test
	public void testOneri() {
		final String methodName ="testOneri";
		SubdocumentoSpesa subdocSpesa = new SubdocumentoSpesa();
		DocumentoSpesa documento = new DocumentoSpesa();
		
		subdocSpesa.setDocumento(documento);
		
		subdocSpesa.setTipoIvaSplitReverse(TipoIvaSplitReverse.SPLIT_ISTITUZIONALE);
		
		//CASO STANDARD
//		documento.setUid(1437);
//		subdocSpesa.setUid(2268);
		
		
		//CASO MULT
		documento.setUid(1435);
		subdocSpesa.setUid(2265);
		
		
		List<DettaglioOnere> oneriCollegati = onereDad.findOneryByIdDocumento(subdocSpesa.getDocumento().getUid());
		if(oneriCollegati == null || oneriCollegati.isEmpty()) {
			log.debug(methodName, "non ho trovato ritenute collegate per il doc con uid: " + subdocSpesa.getDocumento().getUid() +". Return lista vuota.");
			// Se non ho ritenute esco
			return;
		}
		RitenuteDocumento ritenuteDocumento = subdocSpesa.getDocumento().getRitenuteDocumento() != null ? subdocSpesa.getDocumento().getRitenuteDocumento() : new RitenuteDocumento();
		ritenuteDocumento.setListaOnere(oneriCollegati);
		subdocSpesa.getDocumento().setRitenuteDocumento(ritenuteDocumento);
		
		DettaglioOnere dettaglioOnereDaElaborare = null;
		
		if(subdocSpesa.getDocumento().getRitenuteDocumento() == null || subdocSpesa.getDocumento().getRitenuteDocumento().getListaOnere() == null){
			log.debug(methodName, "non ho trovato ritenute collegate per il doc con uid: " + subdocSpesa.getDocumento().getUid() +". Nessuna elaborazione SplitReverse");
			return;
		}
		List<DettaglioOnere> dettagliOnere = subdocSpesa.getDocumento().getRitenuteDocumento().getListaOnere();
		log.debug(methodName, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

		for(DettaglioOnere dettaglioOnere : dettagliOnere){
			
			StringBuilder sb = new StringBuilder();
			sb.append("dettaglioOnere.uid: ")
			.append(dettaglioOnere.getUid())
			.append(" . TipoOnere = ")
			.append(dettaglioOnere.getTipoOnere().getDescrizione())
			.append(" ImportoACaricoDelsoggetto :  ")
			.append(dettaglioOnere.getImportoCaricoSoggetto());
			
			log.debug(methodName, sb.toString() );
			if(dettaglioOnereDaElaborare ==null && subdocSpesa.getTipoIvaSplitReverse().equals(dettaglioOnere.getTipoOnere().getTipoIvaSplitReverse())){
				dettaglioOnereDaElaborare = dettaglioOnere;
			}
			log.debug(methodName, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		}
		
		if(dettaglioOnereDaElaborare == null){
			log.debug(methodName, "salto la gestione della ritenuta, non ho trovato un dettaglio onere con TipoIvaSplitReverse corrispondente");
			return;
		}
		if(dettaglioOnereDaElaborare.getImportoCaricoSoggetto() == null || dettaglioOnereDaElaborare.getImportoCaricoSoggetto().compareTo(BigDecimal.ZERO) == 0){
			log.debug(methodName, "salto la gestione della ritenuta, l'importo a carico soggetto e' nullo");
			return;
		}
		StringBuilder sbF = new StringBuilder();
		sbF.append("L'onere che lancia l'elaborazione e' dettaglioOnere.uid: ")
		.append(dettaglioOnereDaElaborare.getUid())
		.append(" . TipoOnere = ")
		.append(dettaglioOnereDaElaborare.getTipoOnere().getDescrizione())
		.append(" ImportoACaricoDelsoggetto :  ")
		.append(dettaglioOnereDaElaborare.getImportoCaricoSoggetto());
		
		log.debug(methodName, sbF.toString() );
	}
	
	public void testComparison() {
		Map<String, List<SubdocumentoSpesa>> result = new HashMap<String, List<SubdocumentoSpesa>>();
		SubdocumentoSpesa s = new SubdocumentoSpesa();
			Liquidazione l = s.getLiquidazione();
	
			String keyLiq = computeGroupKeyLiquidazione(l);
			String keySubdoc = computeGroupKeySubdocumento(s);
	
			String key = keyLiq + "_" + keySubdoc;
	
			if (!result.containsKey(key)) {
				List<SubdocumentoSpesa> lista = new ArrayList<SubdocumentoSpesa>();
				result.put(key, lista);
			}
			result.get(key).add(s);
		
		
		//Ordinemento dei gruppi individuati per anno e numero liquidazione.
		for(List<SubdocumentoSpesa> subdocs : result.values()){
			Collections.sort(subdocs, new Comparator<SubdocumentoSpesa>(){
				@Override
				public int compare(SubdocumentoSpesa s1, SubdocumentoSpesa s2) {
					return new CompareToBuilder()
							.append(s1.getLiquidazione().getAnnoLiquidazione(), s2.getLiquidazione().getAnnoLiquidazione())
							.append(s1.getLiquidazione().getNumeroLiquidazione(), s2.getLiquidazione().getNumeroLiquidazione())
							.toComparison();
				}
			});
		}
		//return result;
	}

	private String computeGroupKeyLiquidazione(Liquidazione liquidazione) {
		final String separator = "_";
		StringBuilder key = new StringBuilder();
		
		// Compongo la stringa
		key.append(computeKeyAttoAmministrativo(liquidazione.getAttoAmministrativoLiquidazione()))
			.append(separator)
			.append(computeKeyCapitolo(liquidazione.getCapitoloUscitaGestione()))
			.append(separator)
			.append(computeKeyMovimentoGestione(liquidazione.getImpegno()))
			.append(separator)
			.append(computeKeySoggetto(liquidazione.getSoggettoLiquidazione()))
			.append(separator)
			.append(computeKeyModalitaPagamento(liquidazione.getModalitaPagamentoSoggetto()))
			.append(separator)
			.append(computeKeyPianoDeiContiFinanziario(liquidazione.getImpegno()))
			.append(separator)
			.append(computeKeyContoTesoreria(liquidazione.getContoTesoreria()))
			.append(separator)
			.append(computeKeyDistinta(liquidazione.getDistinta()));
		
		return key.toString();
	}

	/**
	 * Calcola la chiave del subdocumento.
	 * 
	 * @param subdocumento il subdocumento
	 * 
	 * @return la chiave
	 */
	private String computeGroupKeySubdocumento(SubdocumentoSpesa subdocumento) {
		final String separator = "_";
		StringBuilder key = new StringBuilder();
		
		key.append(computeKeyFlagBeneficiarioMultiplo(subdocumento.getDocumento()))
			.append(separator)
			.append(computeKeyCodiceBollo(subdocumento.getDocumento().getCodiceBollo()))
			.append(separator)
			.append(subdocumento.getFlagAvviso())
			.append(separator)
			.append(computeKeyNote(subdocumento.getNote()))
			.append(separator)
			.append(computeKeyCommissioniDocumento(subdocumento.getCommissioniDocumento()))
			.append(separator)
			.append(computeKeyFlagACopertura(subdocumento.getFlagACopertura()))
			.append(computeKeyDataScadenza(subdocumento.getDataScadenza()));
		
		return key.toString();
	}

	/**
	 * Calcola la chiave del flag beneficiario multiplo.
	 * 
	 * @param d il documento
	 * @return la chiave
	 */
	private String computeKeyFlagBeneficiarioMultiplo(DocumentoSpesa d) {
		final StringBuilder sb = new StringBuilder();
		// Documento.flagBeneficiarioMultiplo
		sb.append("flagBenefMult")
			.append("<")
			.append(d != null ? d.getFlagBeneficiarioMultiplo() : "null")
			.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave delle commissioni documento
	 * 
	 * @param cd le commissioni
	 * @return la chiave
	 */
	private String computeKeyCommissioniDocumento(CommissioniDocumento cd) {
		final StringBuilder sb = new StringBuilder();
		// Commissioni documento
		sb.append("commissDoc")
			.append("<")
			.append(cd != null ? cd.name() : "null")
			.append(">");
		return sb.toString();
	}
	
	/**
	 * Calcola la chiave della nota tesoriere.
	 * 
	 * @param noteTesoriere la nota di cui calcolare la chiave
	 * @return la chiave della nota
	 */
	protected String computeKeyNote(String note) {
		final StringBuilder sb = new StringBuilder();
		// Codice Bollo
		sb.append("noteTes")
			.append("<")
			.append(note == null ? "null" : note)
			.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave del flag a copertura.
	 * 
	 * @param flag il flag di cui calcolare la chiave
	 * @return la chiave del flag
	 */
	protected String computeKeyFlagACopertura(Boolean flag) {
		final StringBuilder sb = new StringBuilder();
		// Codice Bollo
		sb.append("flagACopertura")
			.append("<")
			.append(flag)
			.append(">");
		return sb.toString();
	}
	
	/**
	 * Calcola la chiave del data scadenza.
	 * 
	 * @param flag il flag di cui calcolare la chiave
	 * @return la chiave del flag
	 */
	protected String computeKeyDataScadenza(Date dataScadenza) {
		final StringBuilder sb = new StringBuilder();
		// Codice Bollo
		sb.append("dataScadenza")
			.append("<")
			.append(dataScadenza)
			.append(">");
		return sb.toString();
	}
	
	protected String computeKeyAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		final StringBuilder sb = new StringBuilder();
		// Atto Amministrativo  => anno, numero, Tipo, Struttura Amministrativa
		
		sb.append("attoAmm")
			.append("<");
		if(attoAmministrativo == null) {
			sb.append("null");
		} else {
			sb.append(attoAmministrativo.getAnno())
				.append("/")
				.append(attoAmministrativo.getNumero())
				.append("/")
				.append(attoAmministrativo.getTipoAtto() == null ? "null" : attoAmministrativo.getTipoAtto().getUid())
				.append("/")
				.append(attoAmministrativo.getStrutturaAmmContabile() == null ? "null" : attoAmministrativo.getStrutturaAmmContabile().getUid());
		}
		sb.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave del capitolo.
	 * 
	 * @param capitolo il capitolo di cui calcolare la chiave
	 * @return la chiave del capitolo
	 */
	protected String computeKeyCapitolo(Capitolo<?, ?> capitolo) {
		final StringBuilder sb = new StringBuilder();
		// Capitolo => numeroCapitolo, numeroArticolo, Tipo Finanziamento (derivato dal MovimentoGestione)
		
		sb.append("capitolo")
			.append("<");
		if(capitolo == null) {
			sb.append("null");
		} else {
			sb.append(capitolo.getNumeroCapitolo())
				.append("/")
				.append(capitolo.getNumeroArticolo())
				.append("/")
				.append(capitolo.getTipoFinanziamento() == null ? "null" : capitolo.getTipoFinanziamento().getUid());
		}
		sb.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave del movimento di gestione.
	 * 
	 * @param movimentoGestione il movimento di cui calcolare la chiave
	 * @return la chiave del movimento
	 */
	protected String computeKeyMovimentoGestione(MovimentoGestione movimentoGestione) {
		final StringBuilder sb = new StringBuilder();
		// MovimentoGestione => annoMovimento
		
		sb.append("movGest")
			.append("<")
			.append(movimentoGestione == null ? "null" : movimentoGestione.getAnnoMovimento())
			.append(">");
		return sb.toString();
	}
	
	protected String computeKeyPianoDeiContiFinanziario(MovimentoGestione movimentoGestione) {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("codPdC")
			.append("<")
			.append(movimentoGestione == null ? "null" : movimentoGestione.getCodPdc())
			.append(">");
		return sb.toString();
 	}
	
	/**
	 * Calcola la chiave del soggetto.
	 * 
	 * @param soggetto il soggetto di cui calcolare la chiave
	 * @return la chiave del soggetto
	 */
	protected String computeKeySoggetto(Soggetto soggetto) {
		final StringBuilder sb = new StringBuilder();
		// Soggetto
		
		sb.append("sog")
			.append("<")
			.append(soggetto == null ? "null" : soggetto.getCodiceSoggetto())
			.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave della modalita di pagamento.
	 * 
	 * @param modalitaPagamentoSoggetto la modalita di cui calcolare la chiave
	 * @return la chiave della modalita
	 */
	protected String computeKeyModalitaPagamento(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		final StringBuilder sb = new StringBuilder();
		// ModalitaPagamentoSoggetto (da cui discende in modo indiretto anche la rottura per SedeSecondaria)
		
		sb.append("modPagSog")
			.append("<")
			.append(modalitaPagamentoSoggetto == null ? "null" : modalitaPagamentoSoggetto.getUid())
			.append(">");
		return sb.toString();
	}
	
	/**
	 * Calcola la chiave della sede secondaria
	 * 
	 * @param sedeSecondaria la sede di cui calcolare la chiave
	 * @return la chiave della sede
	 */
	protected String computeKeySedeSecondaria(SedeSecondariaSoggetto sedeSecondaria) {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("sedeSecSog")
			.append("<")
			.append(sedeSecondaria == null ? "null" : sedeSecondaria.getUid())
			.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave del conto tesoreria.
	 * 
	 * @param contoTesoreria il conto di cui calcolare la chiave
	 * @return la chiave del conto
	 */
	protected String computeKeyContoTesoreria(it.csi.siac.siacfinser.model.ContoTesoreria contoTesoreria) {
		final StringBuilder sb = new StringBuilder();
		// Conto di Tesoreria => se impostato il corrispondente parametro dell'operazione non e' significativo 
		sb.append("contoTes")
			.append("<");
			sb.append(contoTesoreria == null ? "null" : contoTesoreria.getUid());
		sb.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave della distinta.
	 * 
	 * @param d la distinta di cui calcolare la chiave
	 * @return la chiave della distinta
	 */
	protected Object computeKeyDistinta(Distinta d) {
		final StringBuilder sb = new StringBuilder();
		// Conto di Tesoreria => se impostato il corrispondente parametro dell'operazione non e' significativo 
		sb.append("distinta")
			.append("<");
			sb.append(d == null ? "null" : d.getUid());
		sb.append(">");
		return sb.toString();
	}
	
	/**
	 * Calcola la chiave del codice bollo.
	 * 
	 * @param cb il codice di cui calcolare la chiave
	 * @return la chiave del codice
	 */
	protected String computeKeyCodiceBollo(it.csi.siac.siacfin2ser.model.CodiceBollo cb) {
		final StringBuilder sb = new StringBuilder();
		// Codice Bollo
		sb.append("codBollo")
			.append("<")
			.append(cb == null ? "null" : cb.getUid())
			.append(">");
		return sb.toString();
	}
}
