/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.predocumentospesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.predocumentospesa.AssociaImputazioniContabiliVariatePreDocumentoSpesaService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;

public class AssociaImputazioniContabiliVariatePreDocumentoSpesaServiceTest extends BaseJunit4TestCase {
	@Autowired
	private AssociaImputazioniContabiliVariatePreDocumentoSpesaService service;
	
	@Test
	public void associaImputazioniContabiliTest(){
		AssociaImputazioniContabiliVariatePreDocumentoSpesa req = new AssociaImputazioniContabiliVariatePreDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		Impegno impegno = new Impegno();
		impegno.setUid(95620);
		impegno.setNumeroBigDecimal(new BigDecimal(30003));
		impegno.setAnnoMovimento(2015);
		//impegno.setNumero(numero);
		req.setImpegno(impegno);
		PreDocumentoSpesa preDoc = new PreDocumentoSpesa();
		preDoc.setUid(130);
		List<PreDocumentoSpesa> preDocumentiSpesa = new ArrayList<PreDocumentoSpesa>();
		preDocumentiSpesa.add(preDoc);
		req.setPreDocumentiSpesa(preDocumentiSpesa);
//		Impegno associaImputazioniContabili = service.associaImputazioniContabili(preDoc, req);
		
//		System.out.println(associaImputazioniContabili.toString());
	}
}
