/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.test.business.dubbiaesigibilita;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.rendiconto.CalcolaImportiPerAllegatoArconetService;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.CalcolaImportiPerAllegatoArconet;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.CalcolaImportiPerAllegatoArconetResponse;
import it.csi.siac.siacbilser.integration.dao.SiacTAccFondiDubbiaEsigRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemDetRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemDetVarRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;

public class AccantonamentoFondiDubbiaEsigibilitaTestImporti extends BaseJunit4TestCase {

	//SERVICE
	@Autowired CalcolaImportiPerAllegatoArconetService calcolaImportiPerAllegatoArconetService;
	
	//REPOSITORY
	@Autowired SiacTBilElemRepository siacTBilElemRepository;
	@Autowired SiacTAccFondiDubbiaEsigRepository siacTAccFondiDubbiaEsigRepository;
	@Autowired SiacTBilElemDetRepository siacTBilElemDetRepository;
	@Autowired SiacTBilElemDetVarRepository siacTBilElemDetVarRepository;
	
	int uidCapitolo = 205905;
	int annoBilancio = 2021;
	// simulo il quinquennio di riferimento (quinquennio => annoBilancio -1)
	int annoMin = annoBilancio - 5;
	int annoMax = annoBilancio - 1;
	
	// PREVISIONE
	@Test
	public void incassatoAccertatoQuinquennioRiferimento() {
		List<Object[]> listIncassato = siacTBilElemRepository.findImportoIncassatoPerAnno(198587, 2021 - 5, 2021);
		List<Object[]> listAccertato = siacTBilElemRepository.findImportoAccertatoPerAnno(198587, 2021 - 5, 2021);
		
		Map<String, MutablePair<BigDecimal, BigDecimal>> mapPerAnno = new HashMap<String, MutablePair<BigDecimal,BigDecimal>>();
		for(Object[] value : listIncassato) {
			String anno = (String) value[0];
			BigDecimal importo = (BigDecimal) value[1];
			if(!mapPerAnno.containsKey(anno)) {
				mapPerAnno.put(anno, new MutablePair<BigDecimal, BigDecimal>());
			}
			mapPerAnno.get(anno).setLeft(importo);
		}
		for(Object[] value : listAccertato) {
			String anno = (String) value[0];
			BigDecimal importo = (BigDecimal) value[1];
			if(!mapPerAnno.containsKey(anno)) {
				mapPerAnno.put(anno, new MutablePair<BigDecimal, BigDecimal>());
			}
			mapPerAnno.get(anno).setRight(importo);
		}
		for(Map.Entry<String, MutablePair<BigDecimal, BigDecimal>> entry : mapPerAnno.entrySet()) {
			log.info("incassatoAccertatoQuinquennioRiferimento", "ANNO: " + entry.getKey() + " - INCASSATO: " + entry.getValue().getLeft() + " - ACCERTATO: " + entry.getValue().getRight());
		}
	}
	
	@Test
	public void cercaIncassatoQuinquennioRiferimento() {
		List<Object[]> list = siacTBilElemRepository.findImportoIncassatoPerAnno(uidCapitolo, annoMin, annoMax);
		
		assertNotNull(list);
//		assertFalse(list.isEmpty());
//		assertTrue(list.isEmpty());
		
		for (Object[] objects : list) {
			log.debug("cercaIncassatoQuinquennioRiferimento", objects[0] + " - " + objects[1]);
		}
	}

	@Test
	public void cercaAccertatoQuinquennioRiferimento() {
		List<Object[]> list =
		siacTBilElemRepository.findImportoAccertatoPerAnno(uidCapitolo, annoMin, annoMax);
		
		assertNotNull(list);
//		assertFalse(list.isEmpty());
//		assertTrue(list.isEmpty());
		
		for (Object[] objects : list) {
			log.debug("cercaIncassatoQuinquennioRiferimento", objects[0] + " - " + objects[1]);
		}
	}

	// GESTIONE
	@Test
	public void cercaAccertatoQuinquennioRiferimentoGESTIONE() {
		List<Object[]> list =
		siacTBilElemRepository.findImportoAccertatoPerAnno(uidCapitolo, annoBilancio, annoBilancio);
		
		assertNotNull(list);
//		assertFalse(list.isEmpty());
//		assertTrue(list.isEmpty());
		
		for (Object[] objects : list) {
			log.debug("cercaIncassatoQuinquennioRiferimento", objects[0] + " - " + objects[1]);
		}
	}
	
	@Test
	public void cercaIncassatoCompetenzaQuinquennioRiferimento() {
		List<Object[]> list =
		siacTBilElemRepository.findImportoIncassatoCompetenzaPerAnno(uidCapitolo, annoBilancio, annoBilancio);
		
		assertNotNull(list);
//		assertFalse(list.isEmpty());
//		assertTrue(list.isEmpty());
		
		for (Object[] objects : list) {
			log.debug("cercaIncassatoQuinquennioRiferimento", objects[0] + " - " + objects[1]);
		}
	}

	// RENDICONTO
	@Test
	public void rendiconto() {
		List<Object[]> listResiduoIniziale = siacTBilElemRepository.findImportoResiduoInizialeByCapitoloPerAnno(146535, 2021 - 5, 2021-1);
		List<Object[]> listIncassatoResiduo = siacTBilElemRepository.findImportoIncassatoResiduoPerAnno(146535, 2021 - 5, 2021-1);
		
		Map<String, MutablePair<BigDecimal, BigDecimal>> mapPerAnno = new HashMap<String, MutablePair<BigDecimal,BigDecimal>>();
		for(Object[] value : listResiduoIniziale) {
			String anno = (String) value[0];
			BigDecimal importo = (BigDecimal) value[1];
			if(!mapPerAnno.containsKey(anno)) {
				mapPerAnno.put(anno, new MutablePair<BigDecimal, BigDecimal>());
			}
			mapPerAnno.get(anno).setLeft(importo);
		}
		for(Object[] value : listIncassatoResiduo) {
			String anno = (String) value[0];
			BigDecimal importo = (BigDecimal) value[1];
			if(!mapPerAnno.containsKey(anno)) {
				mapPerAnno.put(anno, new MutablePair<BigDecimal, BigDecimal>());
			}
			mapPerAnno.get(anno).setRight(importo);
		}
		for(Map.Entry<String, MutablePair<BigDecimal, BigDecimal>> entry : mapPerAnno.entrySet()) {
			log.info("rendiconto", "ANNO: " + entry.getKey() + " - RESIDUO INIZIALE: " + entry.getValue().getLeft() + " - INCASSATO RESIDUO: " + entry.getValue().getRight());
		}
	}
	
	@Test
	public void cercaResiduoInizialeQuinquennioRiferimento() {
		List<Object[]> list =
		siacTBilElemRepository.findImportoResiduoInizialeByCapitoloPerAnno(uidCapitolo, annoMin, annoMax);
		
		assertNotNull(list);
//		assertFalse(list.isEmpty());
//		assertTrue(list.isEmpty());
		
		for (Object[] objects : list) {
			log.debug("cercaIncassatoQuinquennioRiferimento", objects[0] + " - " + objects[1]);
		}
	}
	
	@Test
	public void cercaIncassatoResiduoQuinquennioRiferimento() {
		List<Object[]> list =
		siacTBilElemRepository.findImportoIncassatoResiduoPerAnno(uidCapitolo, annoMin, annoMax);
		
		assertNotNull(list);
//		assertFalse(list.isEmpty());
//		assertTrue(list.isEmpty());
		
		for (Object[] objects : list) {
			log.debug("cercaIncassatoQuinquennioRiferimento", objects[0] + " - " + objects[1]);
		}
	}
	
	@Test
	public void cercaResiduoFinalePerAnnoBilancio() {
		final String methodName = "cercaResiduoFinale";
		
		String annoAsString = Integer.toString(annoBilancio);
		List<SiacTBilElemDet> siacTBilElemDets = siacTBilElemDetRepository.findBilElemDetsByBilElemIdAndAnnoAndTipo(uidCapitolo, annoAsString, SiacDBilElemDetTipoEnum.StanziamentoResiduo.getCodice());
		BigDecimal stanziamento = siacTBilElemDets == null || siacTBilElemDets.isEmpty() ? BigDecimal.ZERO : siacTBilElemDets.get(0).getElemDetImporto();
		BigDecimal variazione = siacTBilElemDetVarRepository.sumByElemIdAndAnnoNotDefinitiva(uidCapitolo, annoAsString, SiacDBilElemDetTipoEnum.StanziamentoResiduo.getCodice());
		
		log.debug(methodName, "Stanziamento Residuo trovato: " + stanziamento);
		log.debug(methodName, "Somma importi variazioni trovati: " + variazione);
		stanziamento = stanziamento.add(variazione);
		
		log.debug(methodName, "Residuo finale: " + stanziamento);
		
		assertNotNull(stanziamento);
	}
	
	@Test
	public void testAnagraficaOrderBy() {
		Integer bilId = 147;
		Integer versione = 20;
		
		List<SiacTBilElem> list = siacTBilElemRepository.findCollegabiliFCDE(
				bilId, 
				SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice(), 
				versione, 
				TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO.getCodice(), 
				SiacTAttrEnum.FlagEntrataDubbiaEsigFCDE.getCodice()
			);
		
		assertNotNull(list);
		if(CollectionUtils.isNotEmpty(list)) {
			log.debug("findCollegabiliFCDE", " totale capitoli: [" + list.size() + "]");

			for (int i = 0; i < list.size(); i++) {
				log.debug("findCollegabiliFCDE - ", list.get(i).getElemCode() + "/" + list.get(i).getElemCode2());
			}			
		}
		
		List<SiacTBilElem> listNative = siacTBilElemRepository.findCollegabiliFCDENative(
				bilId, 
				SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice(), 
				versione, 
				TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO.getCodice(), 
				SiacTAttrEnum.FlagEntrataDubbiaEsigFCDE.getCodice()
			);
		
		assertNotNull(listNative);
		if(CollectionUtils.isNotEmpty(listNative)) {
			log.debug("findCollegabiliFCDENative", " totale capitoli: [" + listNative.size() + "]");
			
			for (int i = 0; i < listNative.size(); i++) {
				log.debug("findCollegabiliFCDENative - ", listNative.get(i).getElemCode() + "/" + listNative.get(i).getElemCode2());
			}
		}
	}
	
	@Test
	public void calcolaDenominatori() {
		uidCapitolo = 206409; //205928;
		annoMin = 2017;
		annoMax = 2021;
		List<Integer> ids = siacTBilElemRepository.findElemIdsNelQuinquennio(uidCapitolo, annoMin, annoMax);
		for (Integer i : ids) {
			System.out.println(i);
		}
		System.out.println("-----");
		List<Object[]> importiPerAnno = siacTBilElemRepository.findImportoResiduoInizialeByMovimentoPerAnno(ids, "A", Arrays.asList("D", "N"), 3);
		for(Object[] row : importiPerAnno) {
			System.out.println(Integer.valueOf((String)row[0]) + " ->" + (BigDecimal)row[1]);
		}
	}
	
	
	@Test
	public void jpaVsNative() {
		Integer bilId = 147;
		Integer afdeBilId = 57;
		Integer afdeBilIdOld = 18;
		String elemTipoCode = SiacDBilElemTipoEnum.CapitoloEntrataPrevisione.getCodice();
		
		//JPA
		List<SiacTBilElem> listJpa = siacTAccFondiDubbiaEsigRepository
				.findSiacTBilElemEquivalentiNonCollegati(bilId, afdeBilId, afdeBilIdOld, elemTipoCode);
		
		assertNotNull(listJpa);
		log.debug("jpaVsNative", " totale capitoli: [" + listJpa.size() + "]");
		
		for (int i = 0; i < listJpa.size(); i++) {
			log.debug("listJpa - ", listJpa.get(i).getElemCode() + "/" + listJpa.get(i).getElemCode2());
		}
		
		//NATIVE
		List<SiacTBilElem> listNative =  siacTAccFondiDubbiaEsigRepository
				.findSiacTBilElemEquivalentiNonCollegatiJPA(bilId, afdeBilId, afdeBilIdOld, elemTipoCode);
		
		assertNotNull(listNative);
		log.debug("jpaVsNative", " totale capitoli: [" + listNative.size() + "]");
		
		for (int i = 0; i < listNative.size(); i++) {
			log.debug("listNative - ", listNative.get(i).getElemCode() + "/" + listNative.get(i).getElemCode2());
		}
	}
	
	
	@Test
	public void testServizioImportiAllegatoC() {
		CalcolaImportiPerAllegatoArconet request = JAXBUtility.unmarshall("<ns2:calcolaCampiAllegatoArconet xmlns:ns2=\"http://siac.csi.it/bil/svc/1.0\"><annoBilancio>2021</annoBilancio><dataOra>2021-10-15T15:43:08.760+02:00</dataOra><richiedente><account><stato>VALIDO</stato><uid>1890</uid><codice>Demo 22a - AAAAAA00A11C000K</codice><nome>Servizio</nome><descrizione>Servizio: Utenti del Servizio CSI Piemonte</descrizione><indirizzoMail/><ente><stato>VALIDO</stato><uid>3</uid><gestioneLivelli><entry><key>REV_ONERI_DISTINTA_MAN</key><value>TRUE</value></entry><entry><key>GESTIONE_CONSULTAZIONE_CAP_PRENOTAZIONI</key><value>GESTIONE_CONSULTAZIONE_CAP_PRENOTAZIONI</value></entry><entry><key>GESTIONE_PARERE_FINANZIARIO</key><value>GESTIONE_PARERE_FINANZIARIO</value></entry><entry><key>CODICE_PROGETTO_AUTOMATICO</key><value>CODICE_PROGETTO_AUTOMATICO</value></entry><entry><key>GESTIONE_EVASIONE_ORDINI</key><value>CON_VERIFICA</value></entry><entry><key>VARIAZ_ORGANO_AMM</key><value>Giunta</value></entry><entry><key>GESTIONE_CONVALIDA_AUTOMATICA</key><value>CONVALIDA_MANUALE</value></entry><entry><key>REV_ONERI_CONTO_MAN</key><value>FALSE</value></entry><entry><key>VARIAZ_ORGANO_LEG</key><value>Consiglio</value></entry></gestioneLivelli><nome>CITTA' METROPOLITANA DI TORINO</nome></ente></account><operatore><stato>VALIDO</stato><uid>0</uid><codiceFiscale>AAAAAA00A11C000K</codiceFiscale><cognome>Montuori</cognome><nome>Raffaela</nome></operatore></richiedente><accantonamentoFondiDubbiaEsigibilitaAttributiBilancio><dataCreazione>2021-10-06T10:49:46.188+02:00</dataCreazione><dataInizioValidita>2021-10-06T10:49:46.188+02:00</dataInizioValidita><dataModifica>2021-10-06T10:57:42.204+02:00</dataModifica><loginOperazione>Demo 22a - AAAAAA00A11C000K</loginOperazione><stato>VALIDO</stato><uid>54</uid><accantonamentoGraduale>100.00</accantonamentoGraduale><accertamentiAnniSuccessivi>3000.00</accertamentiAnniSuccessivi><accertamentiAnniSuccessiviFcde>2800.00</accertamentiAnniSuccessiviFcde><bilancio><stato>VALIDO</stato><uid>147</uid><anno>2021</anno></bilancio><creditiStralciati>3000.00</creditiStralciati><creditiStralciatiFcde>3000.00</creditiStralciatiFcde><quinquennioRiferimento>2019</quinquennioRiferimento><riscossioneVirtuosa>false</riscossioneVirtuosa><statoAccantonamentoFondiDubbiaEsigibilita>BOZZA</statoAccantonamentoFondiDubbiaEsigibilita><tipoAccantonamentoFondiDubbiaEsigibilita>RENDICONTO</tipoAccantonamentoFondiDubbiaEsigibilita><versione>18</versione></accantonamentoFondiDubbiaEsigibilitaAttributiBilancio></ns2:calcolaCampiAllegatoArconet>"
				, CalcolaImportiPerAllegatoArconet.class);
		
		CalcolaImportiPerAllegatoArconetResponse response = calcolaImportiPerAllegatoArconetService.executeService(request);
		
		assertNotNull(response);
		assertSuccesso(response);
	}
	
}
