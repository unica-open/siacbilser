/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentospesa;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documentospesa.AttivaRegistrazioniContabiliSpesaService;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * The Class AttivaRegistrazioniContabiliSpesaTest.
 * 
 * @author Domenico
 */
public class AttivaRegistrazioniContabiliSpesaTest extends BaseJunit4TestCase {
	
	@Autowired
	private DocumentoDad documentoDad;
	@Autowired
	private AttivaRegistrazioniContabiliSpesaService attivaRegistrazioniContabiliSpesaService;
	
	@PostConstruct
	private void init() {
		documentoDad.setLoginOperazione("AttivaRegistrazioniContabiliSpesaTest");
	}
	
	@Test
	public void attivaRegistrazioniContabiliSpesa(){
		AttivaRegistrazioniContabiliSpesa req = new AttivaRegistrazioniContabiliSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(133, 2018));
		req.setAnnoBilancio(Integer.valueOf(req.getBilancio().getAnno()));
		req.setDocumentoSpesa(create(DocumentoSpesa.class, 74092));
		
		AttivaRegistrazioniContabiliSpesaResponse res = attivaRegistrazioniContabiliSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaFlagDisabilitaRegistrazioneResidui(){
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(991);
		documentoSpesa.setFlagDisabilitaRegistrazioneResidui(Boolean.FALSE);
		
		documentoDad.aggiornaAttributo(documentoSpesa, TipologiaAttributo.FLAG_DISABILITA_REGISTRAZIONE_RESIDUI, documentoSpesa.getFlagDisabilitaRegistrazioneResidui());
	}

}
