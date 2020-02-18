/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.test.business.ordinativo;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.business.service.ordinativo.AggiornaOrdinativoIncassoService;
import it.csi.siac.siacfinser.business.service.ordinativo.RicercaOrdinativoIncassoPerChiaveService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoIncasso;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoIncassoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoIncassoK;

public class OrdinativoIncassoTest extends BaseJunit4TestCase {
	
	@Autowired
	private RicercaOrdinativoIncassoPerChiaveService ricercaOrdinativoIncassoPerChiaveService;
	
	@Autowired
	private AggiornaOrdinativoIncassoService aggiornaOrdinativoIncassoService;
	
	
	@Test
	public void ricercaOrdinativoIncasso() {
		OrdinativoIncasso oi = getOrdinativoIncasso();
//		assertNull(res.getOrdinativoIncasso().getElencoSubOrdinativiDiIncasso().get(0).getSubDocumentoEntrata());
	}


	/**
	 * @return
	 */
	private OrdinativoIncasso getOrdinativoIncasso() {
		RicercaOrdinativoIncassoPerChiave req = new RicercaOrdinativoIncassoPerChiave();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setNumPagina(1);
		req.setNumRisultatiPerPagina(10);
		req.setPaginazioneOrdinativiCollegati(new ParametriPaginazione());
		req.getPaginazioneOrdinativiCollegati().setElementiPerPagina(10);
		req.getPaginazioneOrdinativiCollegati().setNumeroPagina(0);
		
		RicercaOrdinativoIncassoK pRicercaOrdinativoIncassoK = new RicercaOrdinativoIncassoK();
		OrdinativoIncasso ordinativoIncasso = new OrdinativoIncasso();
		ordinativoIncasso.setAnno(Integer.valueOf(2017));
		ordinativoIncasso.setAnnoBilancio(Integer.valueOf(2017));
		ordinativoIncasso.setNumero(Integer.valueOf(6241));
		ordinativoIncasso.setTipoOrdinativo("E");
		pRicercaOrdinativoIncassoK.setOrdinativoIncasso(ordinativoIncasso);
		
		pRicercaOrdinativoIncassoK.setBilancio(getBilancioByProperties("consip", "regp", "2017"));
		
		req.setpRicercaOrdinativoIncassoK(pRicercaOrdinativoIncassoK);
		
		RicercaOrdinativoIncassoPerChiaveResponse res = ricercaOrdinativoIncassoPerChiaveService.executeService(req);
		OrdinativoIncasso oi = res.getOrdinativoIncasso();
		return oi;
	}
	
	@Test
	public void aggiornaOrdinativo() {
		OrdinativoIncasso oi = getOrdinativoIncasso();
//		for (SubOrdinativoIncasso suoi : oi.getElencoSubOrdinativiDiIncasso()) {
//			suoi.setImportoAttuale(new BigDecimal("1"));
//		}
		
		AggiornaOrdinativoIncasso req = new AggiornaOrdinativoIncasso();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setBilancio(getBilancioByProperties("consip", "regp", "2017"));
		
		req.setAnnoBilancio(Integer.valueOf(req.getBilancio().getAnno()));
		
		req.setOrdinativoIncasso(oi);
		
		AggiornaOrdinativoIncassoResponse res = aggiornaOrdinativoIncassoService.executeService(req);
	}
	
}
