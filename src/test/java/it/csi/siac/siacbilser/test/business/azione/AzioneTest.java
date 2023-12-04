/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.azione;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.azione.RicercaAzionePerChiaveService;
import it.csi.siac.siacbilser.business.service.azione.RicercaVisibilitaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.azione.RicercaAzionePerChiave;
import it.csi.siac.siacbilser.frontend.webservice.msg.azione.RicercaAzionePerChiaveResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.azione.RicercaVisibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.azione.RicercaVisibilitaResponse;
import it.csi.siac.siacbilser.model.visibilita.Visibilita;
import it.csi.siac.siacbilser.model.visibilita.modeldetail.VisibilitaModelDetail;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Azione;

/**
 * The Class AzioneTest.
 */
public class AzioneTest extends BaseJunit4TestCase {
	
	@Autowired
	private RicercaAzionePerChiaveService ricercaAzionePerChiaveService;
	
	@Autowired
	private RicercaVisibilitaService ricercaVisibilitaService;
	
	/**
	 * Ricerca azione per chiave
	 */
	@Test
	public void ricercaAzionePerChiave() {
		RicercaAzionePerChiave req = new RicercaAzionePerChiave();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		
		Azione azione = new Azione();
		azione.setNome("OP-GEN-aggiornaPRNotaIntegrataGSA");
		req.setAzione(azione);
		
		RicercaAzionePerChiaveResponse res = ricercaAzionePerChiaveService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaVisibilita() {
		final String methodName = "ricercaVisibilita";
		RicercaVisibilita req = new RicercaVisibilita();
		req.setAnnoBilancio(Integer.valueOf(2021));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setVisibilitaModelDetails(VisibilitaModelDetail.TipoCampo);
		req.setVisibilita(new Visibilita());
		
		Map<String, List<Visibilita>> results = new HashMap<String, List<Visibilita>>();
		String[] funzionalita = new String[] { "FCDE_PREV", "FCDE_GEST", "FCDE_REND" };
		
		for(String fnz : funzionalita) {
			req.getVisibilita().setFunzionalita(fnz);
			
			RicercaVisibilitaResponse res = ricercaVisibilitaService.executeService(req);
			results.put(fnz, res.getListaVisibilita());
		}
		
//		int[] funzionalita = new int[] { 12968, 12917, 13205 };
//		
//		for(int fnz : funzionalita) {
//			req.getVisibilita().setAzione(create(Azione.class, fnz));
//			
//			RicercaVisibilitaResponse res = ricercaVisibilitaService.executeService(req);
//			results.put(fnz + "", res.getListaVisibilita());
//		}

		for(Entry<String, List<Visibilita>> entry : results.entrySet()) {
			for(Visibilita vis : entry.getValue()) {
				Object valore = vis.getParsedDefault();
				log.info(methodName, "TIPO: " + entry.getKey()
				+ "\n  CAMPO: " + vis.getCampo()
				+ "\n  DEFAULT (string): " + vis.getValoreDefault()
				+ "\n  DEFAULT (enum type): " + vis.getTipoCampo()
				+ "\n  DEFAULT (type): " + (valore != null ? valore.getClass() : null)
				+ "\n  DEFAULT (parsed): " + valore);
				
			}
		}
	}
	
}
