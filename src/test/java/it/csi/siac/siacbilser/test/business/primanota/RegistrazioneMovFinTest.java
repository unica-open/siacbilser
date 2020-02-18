/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.primanota;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.business.service.primanota.RegistraMassivaPrimaNotaIntegrataService;
import it.csi.siac.siacbilser.business.service.registrazionemovfin.AggiornaElementoPianoDeiContiRegistrazioneMovFinService;
import it.csi.siac.siacbilser.business.service.registrazionemovfin.CalcolaImportoMovimentoCollegatoService;
import it.csi.siac.siacbilser.business.service.registrazionemovfin.RicercaDettaglioRegistrazioneMovFinService;
import it.csi.siac.siacbilser.business.service.registrazionemovfin.RicercaSinteticaRegistrazioneMovFinService;
import it.csi.siac.siacbilser.integration.dao.RegistrazioneMovFinDao;
import it.csi.siac.siacbilser.integration.dao.SiacTRegMovfinRepository;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRegMovFinStatoEnum;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaElementoPianoDeiContiRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.CalcolaImportoMovimentoCollegato;
import it.csi.siac.siacgenser.frontend.webservice.msg.CalcolaImportoMovimentoCollegatoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraMassivaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraMassivaPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;
import it.csi.siac.siacgenser.model.TipoEvento;

public class RegistrazioneMovFinTest extends BaseJunit4TestCase {
	
	
	
	@Autowired
	private RicercaSinteticaRegistrazioneMovFinService ricercaSinteticaRegistrazioneMovFinService;
	@Autowired
	private RicercaDettaglioRegistrazioneMovFinService ricercaDettaglioRegistrazioneMovFinService;
	@Autowired
	private AggiornaElementoPianoDeiContiRegistrazioneMovFinService aggiornaElementoPianoDeiContiRegistrazioneMovFinService;
	@Autowired
	private CalcolaImportoMovimentoCollegatoService calcolaImportoMovimentoCollegatoService;
	@Autowired
	private RegistraMassivaPrimaNotaIntegrataService registraMassivaPrimaNotaIntegrataService;

	@Autowired
	private SiacTRegMovfinRepository siacTRegMovfinRepository;
	
	@Autowired
	private RegistrazioneMovFinDao registrazioneMovFinDao;
	
	@Autowired
	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	
	@Autowired
	protected ServiceExecutor serviceExecutor;
	
	@Test
	public void ricercaSinteticaRegistrazioneMovFin() {
		RicercaSinteticaRegistrazioneMovFin req = new RicercaSinteticaRegistrazioneMovFin();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.getParametriPaginazione().setElementiPerPagina(50);
		
		req.setTipoEvento(create(TipoEvento.class, 62));
//		req.setEvento(create(Evento.class, 123));
//		req.setAnnoMovimento(Integer.valueOf(2017));
//		req.setNumeroMovimento("2750");
		
		//SIAC-5290: filtro soggetto
//		Soggetto soggetto = new Soggetto();
//		soggetto.setUid(132294);
//		req.setSoggetto(soggetto);
		
		Accertamento acc = new Accertamento();
		acc.setUid(65271);
		req.setMovimentoGestione(acc);
		
		Accertamento subacc = new Accertamento();
		subacc.setUid(68074);
		req.setSubmovimentoGestione(subacc);
		
//		
//		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
//		capitolo.setUid(91506);
//		req.setCapitolo(capitolo);
//		
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setBilancio(getBilancioTest(132, 2016));
//		registrazioneMovFin.setAmbito(Ambito.AMBITO_GSA);
		registrazioneMovFin.setAmbito(Ambito.AMBITO_FIN);
		
//		registrazioneMovFin.setStatoOperativoRegistrazioneMovFin(StatoOperativoRegistrazioneMovFin.REGISTRATO);
//		ElementoPianoDeiConti elementoPianoDeiContiAggiornato = new ElementoPianoDeiConti();
//		elementoPianoDeiContiAggiornato.setCodice("U.7.02.99.99.999");
//		registrazioneMovFin.setElementoPianoDeiContiAggiornato(elementoPianoDeiContiAggiornato );
		
//		req.setNumeroMovimento("79");
//		req.setAnnoMovimento(2015);
//		req.setNumeroSubmovimento(1);
		
//		Date da = new GregorianCalendar(2015, Calendar.APRIL, 21).getTime();
//		req.setDataRegistrazioneDa(da);
//		Date a = new GregorianCalendar(2015, Calendar.APRIL, 30).getTime();
//		req.setDataRegistrazioneA(a);

//		req.setIdDocumento(1157);
		
		req.setRegistrazioneMovFin(registrazioneMovFin);
		

		
		RicercaSinteticaRegistrazioneMovFinResponse res = ricercaSinteticaRegistrazioneMovFinService.executeService(req);
		assertNotNull(res);
		ListaPaginata<RegistrazioneMovFin> registrazioniMovFin = res.getRegistrazioniMovFin();
		System.out.println("trovate " + res.getTotaleElementi() + " registrazioni");
		for (RegistrazioneMovFin rmf : registrazioniMovFin) {
//			Liquidazione liq = (Liquidazione) rmf.getMovimento();
//			componiStringaMovimento("" + liq.getUid(), liq.getAnnoLiquidazione().toString(), liq.getNumeroLiquidazione().toString());
//			Impegno liq = (Impegno) rmf.getMovimento();
//			componiStringaMovimento(rmf.getEvento().getCodice(),"" + liq.getUid(), "" +liq.getAnnoMovimento(), liq.getNumero().toString());
		System.out.println(rmf.getUid());	
		}
	}
	
	
	
	
	
	
	
	private void componiStringaMovimento(String...args) {
		StringBuilder sb = new StringBuilder();
		for (String str : args) {
			sb.append(str)
			.append(" - ");
		}
		System.out.println(sb.toString());
	}
	
	
	
	@Test
	public void ricercaDettaglioRegistrazioneMovFin() {
		RicercaDettaglioRegistrazioneMovFin req = new RicercaDettaglioRegistrazioneMovFin();
	
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setUid(75);
		req.setRegistrazioneMovFin(registrazioneMovFin);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RicercaDettaglioRegistrazioneMovFinResponse res = ricercaDettaglioRegistrazioneMovFinService.executeService(req);
		
		System.out.println(">>>>>>>> Movimento Class:" + res.getRegistrazioneMovFin().getMovimento().getClass());
	}
	
	@Test
	public void impegno_ricercaDettaglioRegistrazioneMovFin() {
		RicercaDettaglioRegistrazioneMovFin req = new RicercaDettaglioRegistrazioneMovFin();
	
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setUid(8);
		req.setRegistrazioneMovFin(registrazioneMovFin);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RicercaDettaglioRegistrazioneMovFinResponse res = ricercaDettaglioRegistrazioneMovFinService.executeService(req);
		
		Impegno impegno = (Impegno) res.getRegistrazioneMovFin().getMovimento();
		
		Soggetto s = impegno.getSoggetto();
	}
	
	@Test
	public void accertamento_ricercaDettaglioRegistrazioneMovFin() {
		RicercaDettaglioRegistrazioneMovFin req = new RicercaDettaglioRegistrazioneMovFin();
	
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setUid(8);
		req.setRegistrazioneMovFin(registrazioneMovFin);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RicercaDettaglioRegistrazioneMovFinResponse res = ricercaDettaglioRegistrazioneMovFinService.executeService(req);
		
		Accertamento accertamento = (Accertamento) res.getRegistrazioneMovFin().getMovimento();
		
		Soggetto s = accertamento.getSoggetto();
	}
	
	@Test
	public void registrazioneMovFinDaoImplFindIdMovimentoTest(){
		for(SiacDCollegamentoTipoEnum collegamentoTipo: SiacDCollegamentoTipoEnum.values()){
			try{
				Integer idMovimento = registrazioneMovFinDao.findIdMovimento(1, collegamentoTipo, 2015, "12", 1);
				System.out.println(">>>>>>>>idMovimento per "+collegamentoTipo+": "+idMovimento);
			} catch (IllegalArgumentException iae){
				if(iae.getMessage().indexOf("Nessun movimento di tipo")!=-1 
						|| iae.getMessage().indexOf("non supporta la ricerca per anno, numero e numero sub")!=-1
						|| iae.getMessage().indexOf("Trovato più di un movimento di tipo")!=-1){
					System.out.println("Errore tollerato: "+ iae.getMessage());
					continue;
				} else {
					throw iae;
				}
			}
		}
	}
	
	@Test
	public void registrazioneMovFinDaoImplRicercaMovimentoByIdTest(){
		SiacDCollegamentoTipoEnum collegamentoTipo = SiacDCollegamentoTipoEnum.SubdocumentoSpesa;
		System.out.println(collegamentoTipo.getEntityClass().getSimpleName() );
		System.out.println(collegamentoTipo.getIdColumnName());
			try{
				SiacTBase idMovimento = registrazioneMovFinDao.ricercaMovimentoById(collegamentoTipo.getEntityClass().getSimpleName(), collegamentoTipo.getIdColumnName(), 13);
				System.out.println(">>>>>>>>idMovimento per "+collegamentoTipo+": "+idMovimento);
			} catch (IllegalArgumentException iae){
				if(iae.getMessage().indexOf("Nessun movimento di tipo")!=-1 
						|| iae.getMessage().indexOf("non supporta la ricerca per anno, numero e numero sub")!=-1
						|| iae.getMessage().indexOf("Trovato più di un movimento di tipo")!=-1){
					System.out.println("Errore tollerato: "+ iae.getMessage());
				} else {
					throw iae;
				}
			}
	}
	
	@Test
	public void registrazioneMovFinDaoImplFindListaIdMovimentoTest(){
		for(SiacDCollegamentoTipoEnum collegamentoTipo: SiacDCollegamentoTipoEnum.values()){
			try{
				List<Integer> idsMovimento = registrazioneMovFinDao.findListaIdMovimento(1, collegamentoTipo, 2015, "12",null); //, "12", 1);
				System.out.println(">>>>>>>>idsMovimento per "+collegamentoTipo+": "+idsMovimento);
			} catch (IllegalArgumentException iae){
				if(iae.getMessage().indexOf("Nessun movimento di tipo")!=-1 
						|| iae.getMessage().indexOf("non supporta la ricerca per anno, numero e numero sub")!=-1
						|| iae.getMessage().indexOf("Trovato più di un movimento di tipo")!=-1){
					System.out.println("Errore tollerato: "+ iae.getMessage());
					continue;
				} else {
					throw iae;
				}
			}
		}
	}
	
	@Test
	public void testAggiornaElementoPianoDeiContiRegistrazioneMovFinService(){
		
		
		AggiornaElementoPianoDeiContiRegistrazioneMovFin req = new AggiornaElementoPianoDeiContiRegistrazioneMovFin();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setUid(40065);
		ElementoPianoDeiConti elementoPianoDeiContiAggiornato = new ElementoPianoDeiConti();
		elementoPianoDeiContiAggiornato.setUid(128465);
		registrazioneMovFin.setElementoPianoDeiContiAggiornato(elementoPianoDeiContiAggiornato);
		req.setRegistrazioneMovFin(registrazioneMovFin);
		aggiornaElementoPianoDeiContiRegistrazioneMovFinService.executeServiceTxRequiresNew(req);
		
		try {
			Thread.sleep(1000*60*60);
		} catch (InterruptedException e) {
			System.out.println("Mi hai interrotto!!!!!!!!!!!!");
		}
		
	}
	
	@Test
	public void calcolaImportoMovimentoCollegato() {
		CalcolaImportoMovimentoCollegato req = new CalcolaImportoMovimentoCollegato();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(2018);
		req.setDataOra(new Date());
		req.setRegistrazioneMovFin(create(RegistrazioneMovFin.class, 1));
		
		CalcolaImportoMovimentoCollegatoResponse res = calcolaImportoMovimentoCollegatoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void testCountSiacTRegistrazioneMovFinByUIdsAndStato(){
		Set<Integer> uidsRegistrazioniMovFin = new HashSet<Integer>();
		uidsRegistrazioniMovFin.add(1902);
		uidsRegistrazioniMovFin.add(1901);
		uidsRegistrazioniMovFin.add(1900);
		uidsRegistrazioniMovFin.add(1829);
		
		List<String> stati = new ArrayList<String>();
		stati.add(SiacDRegMovFinStatoEnum.Notificato.getCodice());
		stati.add(SiacDRegMovFinStatoEnum.Contabilizzato.getCodice());
		
		Long count = siacTRegMovfinRepository.countSiacTRegistrazioneMovFinByUIdsAndStato(uidsRegistrazioniMovFin, stati);
		System.out.println(">>>>>>>>>>>>>>>> count: "+count);
	}
	
	@Test
	public void testRegistraMassivaPrimaNotaIntegrataService() {
		RegistraMassivaPrimaNotaIntegrata req = new RegistraMassivaPrimaNotaIntegrata();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setAnnoBilancio(2017);
		RicercaSinteticaRegistrazioneMovFin r = getTestFileObject(RicercaSinteticaRegistrazioneMovFin.class, "coge/ricercaSinteticaResgostrazioneMovFin.xml");
		r.setAttoAmministrativo(null);
		r.setSoggetto(null);
		req.setRegistrazioneMovFin(r.getRegistrazioneMovFin());
		req.setTipoEvento(r.getTipoEvento());
		req.setEvento(r.getEvento());
		req.setEventoRegistrazioneIniziale(r.getEventoRegistrazioneIniziale());
		req.setAnnoMovimento(r.getAnnoMovimento());
		req.setNumeroMovimento(r.getNumeroMovimento());
		req.setNumeroSubmovimento(r.getNumeroSubmovimento());
		req.setDataRegistrazioneDa(r.getDataRegistrazioneDa());
		req.setDataRegistrazioneA(r.getDataRegistrazioneA());
		req.setIdDocumento(r.getIdDocumento());
		//SIAC-6248
		req.setCapitolo(r.getCapitolo());
		req.setAttoAmministrativo(r.getAttoAmministrativo());
		req.setSoggetto(r.getSoggetto());
		
		RegistraMassivaPrimaNotaIntegrataResponse res = registraMassivaPrimaNotaIntegrataService.executeService(req);
		
	}
	
	@Test
	public void testFinRegDocumento() {
		Bilancio bilancioAnnoPrecedente = getBilancioByProperties("consip", "regp", "2017");
		Richiedente richiedenteByProperties = getRichiedenteByProperties("consip", "regp");
		registrazioneGENServiceHelper.init(serviceExecutor,richiedenteByProperties.getAccount().getEnte() , richiedenteByProperties, "test");
		registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimentoByBilancio(TipoCollegamento.SUBDOCUMENTO_SPESA, create(DocumentoSpesa.class, 12345), null,bilancioAnnoPrecedente);
	}
	
	
	
}
