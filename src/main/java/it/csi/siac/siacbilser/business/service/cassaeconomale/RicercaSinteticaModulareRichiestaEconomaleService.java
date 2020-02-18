/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RichiestaEconomaleDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaModulareRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaModulareRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * Ricerca sintetica modulare di {@code RichiestaEconomale}.
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaModulareRichiestaEconomaleService extends CheckedAccountBaseService<RicercaSinteticaModulareRichiestaEconomale, RicercaSinteticaModulareRichiestaEconomaleResponse> {
		
	private RichiestaEconomale richiestaEconomale;
	
	@Autowired
	private RichiestaEconomaleDad richiestaEconomaleDad;

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRichiestaEconomale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiesta economale"));
		richiestaEconomale = req.getRichiestaEconomale();
		checkEntita(richiestaEconomale.getCassaEconomale(), "cassa economale");
		checkEntita(richiestaEconomale.getBilancio(), "bilancio");
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri di paginazione"));
		checkCondition(req.getDataCreazioneDa() == null || req.getDataCreazioneA() == null || !req.getDataCreazioneDa().after(req.getDataCreazioneA()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Data creazione", "la data di creazione da non puo' essere successiva alla data da creazione a"));
		checkCondition(req.getDataMovimentoDa() == null || req.getDataMovimentoA() == null || !req.getDataMovimentoDa().after(req.getDataMovimentoA()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Data creazione", "la data del movimento da non puo' essere successiva alla data del movimento a"));
	}
	
	@Override
	protected void init() {
		richiestaEconomaleDad.setEnte(ente);
	}
	

	@Override
	@Transactional
	public RicercaSinteticaModulareRichiestaEconomaleResponse executeService(RicercaSinteticaModulareRichiestaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {

		ListaPaginata<RichiestaEconomale> richiesteEconomali = richiestaEconomaleDad.ricercaSinteticaModulareRichiestaEconomale(richiestaEconomale,
				req.getDataCreazioneDa(), req.getDataCreazioneA(),
				req.getDataMovimentoDa(), req.getDataMovimentoA(),
				req.getParametriPaginazione(), 
				req.getRichiestaEconomaleModelDetails()
				);
		res.setRichiesteEconomali(richiesteEconomali);
		
		BigDecimal importoTotale = richiestaEconomaleDad.ricercaSinteticaRichiestaEconomaleTotale(richiestaEconomale,
				req.getDataCreazioneDa(), req.getDataCreazioneA(),
				req.getDataMovimentoDa(), req.getDataMovimentoA());
		res.setTotaleImporti(importoTotale);
	}

	
	
}
