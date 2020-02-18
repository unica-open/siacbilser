/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.previsione;



import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaDettaglioModulareCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaDettaglioModulareCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaDettaglioModulareCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaDettaglioModulareCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataPrevisioneModelDetail;
import it.csi.siac.siacfin2ser.model.CapitoloUscitaGestioneModelDetail;
import it.csi.siac.siacfin2ser.model.CapitoloUscitaPrevisioneModelDetail;

/**
 * The Class RicercaDettaglioModulareCapitoloServiceTest.
 * @author Elisa Chiari
 * @varsion 1.0.0-14/11/2016
 */

public class RicercaDettaglioModulareCapitoloServiceTest extends BaseJunit4TestCase
{

	/** The fondiDubbiaEsigibilitaService service. */
	@Autowired
	private RicercaDettaglioModulareCapitoloEntrataPrevisioneService ricercaDettaglioModulareCapitoloEntrataPrevisioneService;
	@Autowired
	private RicercaDettaglioModulareCapitoloEntrataGestioneService ricercaDettaglioModulareCapitoloEntrataGestioneService;
	@Autowired
	private RicercaDettaglioModulareCapitoloUscitaPrevisioneService ricercaDettaglioModulareCapitoloUscitaPrevisioneService;
	@Autowired
	private RicercaDettaglioModulareCapitoloUscitaGestioneService ricercaDettaglioModulareCapitoloUscitaGestioneService;
	
	/**
	 * Verifica eliminabilita capitolo entrata previsione.
	 *
	 * @return the verifica eliminabilita capitolo entrata previsione response
	 */
	@Test
	public void ricercaDettaglioModulareCapitoloEntrataPrevisione() {
		
		RicercaDettaglioModulareCapitoloEntrataPrevisione request = new RicercaDettaglioModulareCapitoloEntrataPrevisione();
		CapitoloEntrataPrevisioneModelDetail[] modelRichiesti = ottieniModelDetailCapitoloentrataPrevisione();
		request.setModelDetails(modelRichiesti);
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		CapitoloEntrataPrevisione capitolo = new CapitoloEntrataPrevisione();
		capitolo.setUid(11522);
		//capitolo.setUid(11178); //capitolo annullato
		//capitolo.setUid(12497); //con ex-capitolo e capirolo equivalente
		request.setCapitoloEntrataPrevisione(capitolo);
		
		
		TipologiaClassificatore[] classifRichiesti = ottieniClassificatoriEntrata();
		request.setTipologieClassificatoriRichiesti(classifRichiesti);
		//
		
		RicercaDettaglioModulareCapitoloEntrataPrevisioneResponse response = ricercaDettaglioModulareCapitoloEntrataPrevisioneService.executeService(request);
	}
	@Test
	public void ricercaDettaglioModulareCapitoloEntrataGestione() {
		
		RicercaDettaglioModulareCapitoloEntrataGestione request = new RicercaDettaglioModulareCapitoloEntrataGestione();
		CapitoloEntrataGestioneModelDetail[] modelRichiesti = ottieniModelDetailCapitoloCapitoloEntrataGestione();
		request.setModelDetails(modelRichiesti);
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
		//capitolo.setUid(31183);
		capitolo.setUid(18429); //capitolo annullato
		//capitolo.setUid(31216); //con ex-capitolo e capirolo equivalente
		request.setCapitoloEntrataGestione(capitolo);
		
		
		TipologiaClassificatore[] classifRichiesti = ottieniClassificatoriEntrata();
		request.setTipologieClassificatoriRichiesti(classifRichiesti);
		//
		
		RicercaDettaglioModulareCapitoloEntrataGestioneResponse response = ricercaDettaglioModulareCapitoloEntrataGestioneService.executeService(request);
	}
	
	@Test
	public void ricercaDettaglioModulareCapitoloUscitaPrevisione() {
		
		RicercaDettaglioModulareCapitoloUscitaPrevisione request = new RicercaDettaglioModulareCapitoloUscitaPrevisione();
		CapitoloUscitaPrevisioneModelDetail[] modelRichiesti = ottieniModelDetailCapitoloUscitaPrevisione();
		request.setModelDetails(modelRichiesti);
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		CapitoloUscitaPrevisione capitolo = new CapitoloUscitaPrevisione();
		//capitolo.setUid(62);
		//capitolo.setUid(6293); //capitolo annullato
		capitolo.setUid(30995);
		//capitolo.setUid(18444); //con ex-capitolo e capitolo equivalente
		request.setCapitoloUscitaPrevisione(capitolo);
		
		
		TipologiaClassificatore[] classifRichiesti = ottieniClassificatoriUscita();
		request.setTipologieClassificatoriRichiesti(classifRichiesti);
		//
		
		RicercaDettaglioModulareCapitoloUscitaPrevisioneResponse response = ricercaDettaglioModulareCapitoloUscitaPrevisioneService.executeService(request);
	}
	
	@Test
	public void ricercaDettaglioModulareCapitoloUscitaGestione() {
		
		RicercaDettaglioModulareCapitoloUscitaGestione request = new RicercaDettaglioModulareCapitoloUscitaGestione();
		CapitoloUscitaGestioneModelDetail[] modelRichiesti = ottieniModelDetailCapitoloUscitaGestione();
		request.setModelDetails(modelRichiesti);
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(16856);
		//capitolo.setUid(17131); //capitolo annullato
		//capitolo.setUid(31216); //con ex-capitolo e capirolo equivalente
		request.setCapitoloUscitaGestione(capitolo);
		
		
		TipologiaClassificatore[] classifRichiesti = ottieniClassificatoriUscita();
		request.setTipologieClassificatoriRichiesti(classifRichiesti);
		//
		
		RicercaDettaglioModulareCapitoloUscitaGestioneResponse response = ricercaDettaglioModulareCapitoloUscitaGestioneService.executeService(request);
	}
	
	private CapitoloUscitaGestioneModelDetail[] ottieniModelDetailCapitoloUscitaGestione() {
		return new CapitoloUscitaGestioneModelDetail[]{
				CapitoloUscitaGestioneModelDetail.AttoDiLegge,
				CapitoloUscitaGestioneModelDetail.Attributi,
				CapitoloUscitaGestioneModelDetail.Bilancio,
				CapitoloUscitaGestioneModelDetail.CapitoloEquivalente,
				CapitoloUscitaGestioneModelDetail.Categoria,
				CapitoloUscitaGestioneModelDetail.Classificatori,
				CapitoloUscitaGestioneModelDetail.DataAnnullamento,
				CapitoloUscitaGestioneModelDetail.Ente, 
				CapitoloUscitaGestioneModelDetail.ExCapitolo,
				CapitoloUscitaGestioneModelDetail.Importi,
				CapitoloUscitaGestioneModelDetail.Stato
		};
	}
	private CapitoloEntrataGestioneModelDetail[] ottieniModelDetailCapitoloCapitoloEntrataGestione() {
		return new CapitoloEntrataGestioneModelDetail[]{
				CapitoloEntrataGestioneModelDetail.AttoDiLegge,
				CapitoloEntrataGestioneModelDetail.Attributi,
				CapitoloEntrataGestioneModelDetail.Bilancio,
				CapitoloEntrataGestioneModelDetail.CapitoloEquivalente,
				CapitoloEntrataGestioneModelDetail.Categoria,
				CapitoloEntrataGestioneModelDetail.Classificatori,
				CapitoloEntrataGestioneModelDetail.DataAnnullamento,
				CapitoloEntrataGestioneModelDetail.Ente, 
				CapitoloEntrataGestioneModelDetail.ExCapitolo,
				CapitoloEntrataGestioneModelDetail.Importi,
				CapitoloEntrataGestioneModelDetail.Stato
		};
	}
	
	
	private TipologiaClassificatore[] ottieniClassificatoriUscita() {
		return new TipologiaClassificatore[] {
				TipologiaClassificatore.CDC,TipologiaClassificatore.CDR,
				TipologiaClassificatore.PDC,
				TipologiaClassificatore.PDC_I,
				TipologiaClassificatore.PDC_II,
				TipologiaClassificatore.PDC_III,
				TipologiaClassificatore.PDC_IV,
				TipologiaClassificatore.PDC_IV,
				TipologiaClassificatore.TITOLO_SPESA,
				TipologiaClassificatore.MACROAGGREGATO,
				TipologiaClassificatore.MISSIONE,
				TipologiaClassificatore.PROGRAMMA,
				TipologiaClassificatore.CLASSE_COFOG,
				TipologiaClassificatore.SIOPE_ENTRATA,
				TipologiaClassificatore.SIOPE_ENTRATA_I,
				TipologiaClassificatore.SIOPE_ENTRATA_II,
				TipologiaClassificatore.SIOPE_ENTRATA_III,
				TipologiaClassificatore.TIPO_FINANZIAMENTO,
				TipologiaClassificatore.TIPO_FONDO,
				TipologiaClassificatore.RICORRENTE_ENTRATA,
				TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA,
				TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA,
				TipologiaClassificatore.CLASSIFICATORE_1,
				TipologiaClassificatore.CLASSIFICATORE_2,
				TipologiaClassificatore.CLASSIFICATORE_3,
				TipologiaClassificatore.CLASSIFICATORE_4,
				TipologiaClassificatore.CLASSIFICATORE_5,
				TipologiaClassificatore.CLASSIFICATORE_6,
				TipologiaClassificatore.CLASSIFICATORE_7,
				TipologiaClassificatore.CLASSIFICATORE_8,
				TipologiaClassificatore.CLASSIFICATORE_9,
				TipologiaClassificatore.CLASSIFICATORE_10,
				TipologiaClassificatore.CLASSIFICATORE_31,
				TipologiaClassificatore.CLASSIFICATORE_32,
				TipologiaClassificatore.CLASSIFICATORE_33,
				TipologiaClassificatore.CLASSIFICATORE_34,
				TipologiaClassificatore.CLASSIFICATORE_35,
				TipologiaClassificatore.POLITICHE_REGIONALI_UNITARIE
		};
	}

	private CapitoloUscitaPrevisioneModelDetail[] ottieniModelDetailCapitoloUscitaPrevisione() {
		return new CapitoloUscitaPrevisioneModelDetail[]{
				CapitoloUscitaPrevisioneModelDetail.AttoDiLegge,
				//CapitoloUscitaPrevisioneModelDetail.Attributi,
				CapitoloUscitaPrevisioneModelDetail.Bilancio,
				//CapitoloUscitaPrevisioneModelDetail.CapitoloEquivalente,
				CapitoloUscitaPrevisioneModelDetail.Categoria,
				CapitoloUscitaPrevisioneModelDetail.Classificatori,
				//CapitoloUscitaPrevisioneModelDetail.DataAnnullamento,
				CapitoloUscitaPrevisioneModelDetail.Ente, 
				//CapitoloUscitaPrevisioneModelDetail.ExCapitolo,
				CapitoloUscitaPrevisioneModelDetail.Importi,
				//CapitoloUscitaPrevisioneModelDetail.Stato
		};
	}

	private CapitoloEntrataPrevisioneModelDetail[] ottieniModelDetailCapitoloentrataPrevisione() {
		return new CapitoloEntrataPrevisioneModelDetail[]{
				CapitoloEntrataPrevisioneModelDetail.AttoDiLegge,
				CapitoloEntrataPrevisioneModelDetail.Attributi,
				CapitoloEntrataPrevisioneModelDetail.Bilancio,
				CapitoloEntrataPrevisioneModelDetail.CapitoloEquivalente,
				CapitoloEntrataPrevisioneModelDetail.Categoria,
				CapitoloEntrataPrevisioneModelDetail.Classificatori,
				CapitoloEntrataPrevisioneModelDetail.DataAnnullamento,
				CapitoloEntrataPrevisioneModelDetail.Ente, 
				CapitoloEntrataPrevisioneModelDetail.ExCapitolo,
				CapitoloEntrataPrevisioneModelDetail.Importi,
				CapitoloEntrataPrevisioneModelDetail.Stato
		};
	}

	private TipologiaClassificatore[] ottieniClassificatoriEntrata() {
		
		return new TipologiaClassificatore[] {
				TipologiaClassificatore.CDC,TipologiaClassificatore.CDR,
				TipologiaClassificatore.PDC,
				TipologiaClassificatore.PDC_I,
				TipologiaClassificatore.PDC_II,
				TipologiaClassificatore.PDC_III,
				TipologiaClassificatore.PDC_IV,
				TipologiaClassificatore.PDC_IV,
				TipologiaClassificatore.TITOLO_ENTRATA,
				TipologiaClassificatore.TIPOLOGIA,TipologiaClassificatore.CATEGORIA,
				TipologiaClassificatore.SIOPE_ENTRATA,
				TipologiaClassificatore.SIOPE_ENTRATA_I,
				TipologiaClassificatore.SIOPE_ENTRATA_II,
				TipologiaClassificatore.SIOPE_ENTRATA_III,
				TipologiaClassificatore.TIPO_FINANZIAMENTO,
				TipologiaClassificatore.TIPO_FONDO,
				TipologiaClassificatore.RICORRENTE_ENTRATA,
				TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA,
				TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA,
				TipologiaClassificatore.CLASSIFICATORE_36,
				TipologiaClassificatore.CLASSIFICATORE_37,
				TipologiaClassificatore.CLASSIFICATORE_38,
				TipologiaClassificatore.CLASSIFICATORE_39,
				TipologiaClassificatore.CLASSIFICATORE_40,
				TipologiaClassificatore.CLASSIFICATORE_41,
				TipologiaClassificatore.CLASSIFICATORE_42,
				TipologiaClassificatore.CLASSIFICATORE_43,
				TipologiaClassificatore.CLASSIFICATORE_44,
				TipologiaClassificatore.CLASSIFICATORE_45,
				TipologiaClassificatore.CLASSIFICATORE_46,
				TipologiaClassificatore.CLASSIFICATORE_47,
				TipologiaClassificatore.CLASSIFICATORE_48,
				TipologiaClassificatore.CLASSIFICATORE_49,
				TipologiaClassificatore.CLASSIFICATORE_50		
		};
	}


}
