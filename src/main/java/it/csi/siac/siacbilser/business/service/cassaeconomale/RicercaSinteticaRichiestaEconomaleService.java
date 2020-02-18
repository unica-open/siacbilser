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
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.RichiestaEconomaleDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaRichiestaEconomaleService extends CheckedAccountBaseService<RicercaSinteticaRichiestaEconomale, RicercaSinteticaRichiestaEconomaleResponse> {
		
	private RichiestaEconomale richiestaEconomale;
	private TipoRichiestaEconomale tipoRichiesta;
	
	@Autowired
	private RichiestaEconomaleDad richiestaEconomaleDad;
	@Autowired
	private CodificaDad codificaDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRichiestaEconomale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiesta economale"));
		richiestaEconomale = req.getRichiestaEconomale();
		checkEntita(richiestaEconomale.getCassaEconomale(), "cassa economale");
		checkEntita(richiestaEconomale.getBilancio(), "bilancio");
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri di paginazione"));
		
		// SIAC-4497
		checkCondition(req.getDataCreazioneDa() == null || req.getDataCreazioneA() == null || !req.getDataCreazioneDa().after(req.getDataCreazioneA()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Data creazione", "la data di creazione da non puo' essere successiva alla data da creazione a"));
		
		// SIAC-4552
		checkCondition(req.getDataMovimentoDa() == null || req.getDataMovimentoA() == null || !req.getDataMovimentoDa().after(req.getDataMovimentoA()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Data creazione", "la data del movimento da non puo' essere successiva alla data del movimento a"));
	}
	
	@Override
	protected void init() {
		richiestaEconomaleDad.setEnte(ente);
	}
	
	

	@Override
	@Transactional
	public RicercaSinteticaRichiestaEconomaleResponse executeService(RicercaSinteticaRichiestaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		if(tipoRichiesta != null && tipoRichiesta.getUid() == 0 && tipoRichiesta.getCodice() != null){
			int uidCodifica = codificaDad.ricercaCodifica(TipoRichiestaEconomale.class, tipoRichiesta.getCodice()).getUid();
			tipoRichiesta.setUid(uidCodifica);
		}
		ListaPaginata<RichiestaEconomale> richiesteEconomali = richiestaEconomaleDad.ricercaSinteticaRichiestaEconomale(richiestaEconomale,
				// SIAC-4497
				req.getDataCreazioneDa(), req.getDataCreazioneA(),
				// SIAC-4552
				req.getDataMovimentoDa(), req.getDataMovimentoA(),
				req.getParametriPaginazione());
		res.setRichiesteEconomali(richiesteEconomali);
		
		BigDecimal importoTotale = richiestaEconomaleDad.ricercaSinteticaRichiestaEconomaleTotale(richiestaEconomale,
				// SIAC-4497
				req.getDataCreazioneDa(), req.getDataCreazioneA(),
				// SIAC-4552
				req.getDataMovimentoDa(), req.getDataMovimentoA());
		res.setTotaleImporti(importoTotale);
	}

	
	
}
