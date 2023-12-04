/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.MovimentoDad;
import it.csi.siac.siacbilser.integration.dad.OperazioneDiCassaDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRendicontoCassaDaStampare;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRendicontoCassaDaStampareResponse;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaRendicontoCassaDaStampareService extends CheckedAccountBaseService<RicercaSinteticaRendicontoCassaDaStampare, RicercaSinteticaRendicontoCassaDaStampareResponse> {
	
	@Autowired 
	private MovimentoDad movimentoDad;
	@Autowired
	private OperazioneDiCassaDad operazioneDiCassaDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCassaEconomale(), "cassa economale", false);
		checkEntita(req.getBilancio(), "bilancio", false);
		checkNotNull(req.getPeriodoDaRendicontareDataInizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Periodo da rendicontare inizio"), false);
		checkNotNull(req.getPeriodoDaRendicontareDataFine(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Periodo rendicontare fine"), false);
		
		checkParametriPaginazione(req.getParametriPaginazione(), false);
	}
	
	@Override
	@Transactional(readOnly = true)
	public void executeService() {
		super.executeService();
	}
	
	@Override
	protected void execute() {
		ListaPaginata<Movimento> listaMovimenti = movimentoDad.findByPeriodoMovimentoCassaEconId(
				req.getPeriodoDaRendicontareDataInizio(),
				req.getPeriodoDaRendicontareDataFine(),
				req.getCassaEconomale().getUid(),
				ente.getUid(),
				req.getBilancio().getUid(),
				req.getParametriPaginazione());
		
		res.setListaMovimenti(listaMovimenti);
		
		BigDecimal importoTotale = movimentoDad.findTotaleByPeriodoMovimentoCassaEconId(
				req.getPeriodoDaRendicontareDataInizio(),
				req.getPeriodoDaRendicontareDataFine(),
				req.getCassaEconomale().getUid(),
				ente.getUid(),
				req.getBilancio().getUid(),
				null);
		res.setImportoTotale(importoTotale);
		
		//SIAC-6450
		BigDecimal importoTotaleSenzaMovimentiAnnullati = movimentoDad.findTotaleByPeriodoMovimentoCassaEconId(
				req.getPeriodoDaRendicontareDataInizio(),
				req.getPeriodoDaRendicontareDataFine(),
				req.getCassaEconomale().getUid(),
				ente.getUid(),
				req.getBilancio().getUid(),
				Arrays.asList(StatoOperativoRichiestaEconomale.ANNULLATA));
		res.setImportoTotaleSenzaMovimentiAnnullati(importoTotaleSenzaMovimentiAnnullati);
		
		
		//SIAC-5890		
		Long numOperazioniDicassa = operazioneDiCassaDad.contaOperazioniDiCassa(				
				req.getPeriodoDaRendicontareDataInizio(),
				req.getPeriodoDaRendicontareDataFine(),
				req.getCassaEconomale().getUid(),
				ente.getUid(),
				req.getBilancio().getUid()
				);
		res.setNumeroOperazioniDiCassa(numOperazioniDicassa.intValue());
	}
	

}
