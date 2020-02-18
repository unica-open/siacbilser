/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.registroiva;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.registroiva.AnnullaRegistrazioneIvaPagatiService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaSpesaDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaRegistrazioneIvaPagati;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaRegistrazioneIvaPagatiResponse;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;

// TODO: Auto-generated Javadoc
/**
 * The Class RegistroIvaVTTest.
 */
public class SubdocumentoIvaTest extends BaseJunit4TestCase {
	

	@Autowired SubdocumentoIvaSpesaDad subdocumentoIvaDad;
	@Autowired AnnullaRegistrazioneIvaPagatiService annullaRegistrazioneIvaPagatiService;
	
	/**
	 * Ricerca dettaglio registro iva.
	 */
	@Test
	public void ricercasubdocumentiIvaByOrdinativo(){
		List<SubdocumentoIva<?, ?, ?>> subdocIvas = subdocumentoIvaDad.caricaSubdocumentiIvaByOrdinativo(create(Ordinativo.class, 213172), SubdocumentoIvaModelDetail.RegistroIvaModelDetail);
		StringBuilder sb = new StringBuilder();
		for (SubdocumentoIva<?, ?, ?> s : subdocIvas) {
			sb.append("Trovato subdociva con ") 
			.append(s.getNumeroProtocolloDefinitivo()!= null ? s.getNumeroProtocolloDefinitivo() : s.getNumeroProtocolloProvvisorio())
			.append(" del registro ")
			.append(s.getRegistroIva() != null ? s.getRegistroIva().getCodice() : "null")
			.append(" - ")
			.append(s.getRegistroIva() != null ? s.getRegistroIva().getDescrizione() : "null");
		}
		System.out.println(sb.toString());
	}
	
	@Test
	public void annullaSubdocumentoIvaByOrdinativo() {
		AnnullaRegistrazioneIvaPagati req = new AnnullaRegistrazioneIvaPagati();
		req.setRichiedente(getRichiedenteByProperties("forn2", "edisu"));
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setDataOra(new Date());
		req.setOrdinativo(create(Ordinativo.class, 213172));
		AnnullaRegistrazioneIvaPagatiResponse res = annullaRegistrazioneIvaPagatiService.executeService(req);
	}
	
	@Test
	public void ottieniSubdocumentoIvaStampa() {
		SubdocumentoIvaSpesa subdocIva = new SubdocumentoIvaSpesa();
		// SIAC-4609
//		subdocIva.setAnnoEsercizio(getAnnoEsercizio());
		subdocIva.setEnte(getRichiedenteByProperties("consip", "regp").getAccount().getEnte());
		subdocIva.setRegistroIva(create(RegistroIva.class, 38));
		List<SubdocumentoIvaSpesa> subdocs = subdocumentoIvaDad.ricercaDettaglioSubdocumentoIvaSpesaNonQPID(subdocIva,Periodo.DICEMBRE.getInizioPeriodo(2017), Periodo.DICEMBRE.getFinePeriodo(2017), null, null);
		for (SubdocumentoIvaSpesa sis : subdocs) {
		}
	}
	
}