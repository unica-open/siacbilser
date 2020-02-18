/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaVariazioniSingoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaVariazioniSingoloCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoSingoloCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * The Class RicercaVariazioniCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaVariazioniSingoloCapitoloService extends CheckedAccountBaseService<RicercaSinteticaVariazioniSingoloCapitolo, RicercaSinteticaVariazioniSingoloCapitoloResponse> {

	/** The capitolo dad. */
	@Autowired
	protected CapitoloDad capitoloDad;
	
	/** The variazioni dad. */
	@Autowired
	protected VariazioniDad variazioniDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkParametriPaginazione(req.getParametriPaginazione(), false);
		
		checkEntita(req.getCapitolo(), "capitolo", false);
		checkEntita(req.getEnte(), "ente", false);
		checkEntita(req.getBilancio(), "bilancio", true);
		checkCondition(req.getBilancio().getAnno() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"), false);
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaVariazioniSingoloCapitoloResponse executeService(RicercaSinteticaVariazioniSingoloCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		Capitolo<?, ?> capitolo = capitoloDad.findOneWithMinimalData(Integer.valueOf(req.getCapitolo().getUid()));
		res.setCapitolo(capitolo);
		
		ListaPaginata<VariazioneImportoSingoloCapitolo> variazioni = variazioniDad.ricercaSinteticaVariazioneImportoSingoloCapitolo(capitolo, req.getBilancio(),
				req.getEnte(), req.getSegnoImportiVariazione(), req.getParametriPaginazione());
		res.setVariazioni(variazioni);
	}
	
}
