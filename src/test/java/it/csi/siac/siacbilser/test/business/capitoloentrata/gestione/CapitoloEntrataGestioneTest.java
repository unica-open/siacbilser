/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.gestione;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaSinteticaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di test del capitolo entrata gestione
 * @author Pro Logic
 *
 */
public class CapitoloEntrataGestioneTest extends BaseJunit4TestCase {
	
	@Autowired
	private CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	@Autowired private RicercaSinteticaCapitoloEntrataGestioneService ricercaSinteticaCapitoloEntrataGestioneService;
	
	/**
	 * 
	 */
	@Test
	public void ricercaMassivaCapitoloEntrataGestione(){
		RicercaDettaglioMassivaCapitoloEntrataGestione request = creaRequestRicercaDettaglioMassivaFromXml();
		
		RicercaDettaglioMassivaCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaDettaglioMassivaCapitoloEntrataGestione(request);
		
		assertNotNull(response);
	}
	
	@Test
	public void ricercaSinteticaCapitoloEntrataGestione(){
		RicercaSinteticaCapitoloEntrataGestione req = new RicercaSinteticaCapitoloEntrataGestione();
		req.setAnnoBilancio(Integer.valueOf(2021));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setCalcolaTotaleImporti(Boolean.FALSE);
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setTipologieClassificatoriRichiesti(TipologiaClassificatore.CDR);
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		req.setRicercaSinteticaCapitoloEntrata(new RicercaSinteticaCapitoloEGest());
		req.getRicercaSinteticaCapitoloEntrata().setAnnoEsercizio(2021);
		req.getRicercaSinteticaCapitoloEntrata().setAnnoCapitolo(2021);
		req.getRicercaSinteticaCapitoloEntrata().setNumeroCapitolo(17745);
		
		RicercaSinteticaCapitoloEntrataGestioneResponse res = ricercaSinteticaCapitoloEntrataGestioneService.executeService(req);
		assertNotNull(res);
	}

	private RicercaDettaglioMassivaCapitoloEntrataGestione creaRequestRicercaDettaglioMassivaFromXml() {
		StringBuilder sb = new StringBuilder();

		sb.append("<ricercaDettaglioMassivaCapitoloEntrataGestione>");
		sb.append("	<dataOra>2016-07-25T14:34:46.229+02:00</dataOra>");
		sb.append("	<richiedente>");
		sb.append("		<account>");
		sb.append("			<stato>VALIDO</stato>");
		sb.append("			<uid>1</uid>");
		sb.append("			<codice>Demo 21 - CittÃ  di Torino</codice>");
		sb.append("			<nome>Demo 21</nome>");
		sb.append("			<descrizione>Demo 21 - CittÃ  di Torino</descrizione>");
		sb.append("			<indirizzoMail>email</indirizzoMail>");
		sb.append("			<ente>");
		sb.append("				<stato>VALIDO</stato>");
		sb.append("				<uid>1</uid>");
		sb.append("				<gestioneLivelli>");
		sb.append("					<entry>");
		sb.append("						<key>LIVELLO_GESTIONE_BILANCIO</key>");
		sb.append("						<value>GESTIONE_UEB</value>");
		sb.append("					</entry>");
		sb.append("				</gestioneLivelli>");
		sb.append("				<nome>CittÃ  di Torino</nome>");
		sb.append("			</ente>");
		sb.append("		</account>");
		sb.append("		<operatore>");
		sb.append("			<stato>VALIDO</stato>");
		sb.append("			<uid>0</uid>");
		sb.append("			<codiceFiscale>AAAAAA00A11B000J</codiceFiscale>");
		sb.append("			<cognome>AAAAAA00A11B000J</cognome>");
		sb.append("			<nome>Demo</nome>");
		sb.append("		</operatore>");
		sb.append("	</richiedente>");
		sb.append("	<ente>");
		sb.append("		<stato>VALIDO</stato>");
		sb.append("		<uid>1</uid>");
		sb.append("		<gestioneLivelli>");
		sb.append("			<entry>");
		sb.append("				<key>LIVELLO_GESTIONE_BILANCIO</key>");
		sb.append("				<value>GESTIONE_UEB</value>");
		sb.append("			</entry>");
		sb.append("		</gestioneLivelli>");
		sb.append("		<nome>CittÃ  di Torino</nome>");
		sb.append("	</ente>");
		sb.append("	<ricercaSinteticaCapitoloEGest>");
		sb.append("		<annoCapitolo>2015</annoCapitolo>");
		sb.append("		<annoEsercizio>2015</annoEsercizio>");
		sb.append("		<numeroArticolo>2559</numeroArticolo>");
		sb.append("		<numeroCapitolo>1807</numeroCapitolo>");
		sb.append("	</ricercaSinteticaCapitoloEGest>");
		sb.append("</ricercaDettaglioMassivaCapitoloEntrataGestione>");		
		
		RicercaDettaglioMassivaCapitoloEntrataGestione request = JAXBUtility.unmarshall(sb.toString(), RicercaDettaglioMassivaCapitoloEntrataGestione.class);
		
		return request;
	}

}
