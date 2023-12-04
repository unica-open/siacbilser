/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioService extends CheckedAccountBaseService<RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio, RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioResponse> {
	
	/** The cronoprogramma dad. */
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;
	@Autowired
	private ProgettoDad progettoDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getProgetto(), "progetto", false);
		checkEntita(req.getBilancio(), "bilancio", false);
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioResponse executeService(RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		List<Cronoprogramma> listaCronoprogrammi = cronoprogrammaDad.findCronoprogrammiByProgettoAndBilancio(req.getProgetto(), req.getBilancio());
		
		
		TipoProgetto tipoProgettoAssociato = progettoDad.caricaTipoProgetto(req.getProgetto());
		if(tipoProgettoAssociato == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("impossibile trovare uil tipo progetto per il progetto associato."));
		}
		
		for (Cronoprogramma c : listaCronoprogrammi) {
			
			List<DettaglioEntrataCronoprogramma> dettagliEntrata = cronoprogrammaDad.findDettagliEntrataCronoprogramma(c, tipoProgettoAssociato.getTipoCapitoloEntrata());
			c.setCapitoliEntrata(dettagliEntrata);
			
			List<DettaglioUscitaCronoprogramma> dettagliUscita = cronoprogrammaDad.findDettagliUscitaCronoprogramma(c, tipoProgettoAssociato.getTipoCapitoloSpesa());
			c.setCapitoliUscita(dettagliUscita);
		}
		
		res.setCardinalitaComplessiva(listaCronoprogrammi.size());
		res.setCronoprogrami(listaCronoprogrammi);

	}

}
