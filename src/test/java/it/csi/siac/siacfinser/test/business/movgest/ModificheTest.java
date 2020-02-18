/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.test.business.movgest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfinser.business.service.movgest.AnnullaMovimentoSpesaService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesaResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;

public class ModificheTest extends BaseJunit4TestCase {
	
	@Autowired AnnullaMovimentoSpesaService annullaMovimentoSpesaService;
	
	
	
	
	@Test
	public void annullaMovimentoSpesa() {
		AnnullaMovimentoSpesa req = new AnnullaMovimentoSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setBilancio(getBilancioByProperties("consip", "regp", "2017"));
		Impegno impegno = new Impegno();
		impegno.setUid(64761);
		List<ModificaMovimentoGestioneSpesa> lista = new ArrayList<ModificaMovimentoGestioneSpesa>();
		ModificaMovimentoGestioneSpesa mod = new ModificaMovimentoGestioneSpesa();
		mod.setUid(6619);
		lista.add(mod);
		impegno.setListaModificheMovimentoGestioneSpesa(lista);
		req.setImpegno(impegno);
		
		
		AnnullaMovimentoSpesaResponse res = annullaMovimentoSpesaService.executeService(req);
	}
	

}
