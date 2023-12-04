/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnagraficaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnagraficaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.model.StatoDismissioneCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserisce e completa gli Elenchi di Documenti Spesa, Entrata, IVA Spesa, Iva
 * Entrata, comprensivi di AllegatoAtto.
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciAnagraficaDismissioneCespiteService extends BaseInserisciAggiornaAnagraficaDismissioneCespiteService<InserisciAnagraficaDismissioneCespite, InserisciAnagraficaDismissioneCespiteResponse> {


	@Override
	protected void checkServiceParam() throws ServiceParamError {
		dismissioneCespite = req.getDismissioneCespite();
		checkNotNull(dismissioneCespite, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dismissione cespite"));
		checkCampiDismissioneCespite();
	}

	@Transactional
	@Override
	public InserisciAnagraficaDismissioneCespiteResponse executeService(InserisciAnagraficaDismissioneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkEvento();
		caricaCausale();
		checkCausale();
		caricaAttoAmministrativo();
		
		popolaElencoDismissione();
		
		popolaStato();
		
		dismissioneCespiteDad.inserisciDismissioneCespite(dismissioneCespite);
		
		res.setDismissioneCespite(dismissioneCespite);
	}

	/**
	 * Popola stato.
	 */
	private void popolaStato() {
		dismissioneCespite.setStatoDismissioneCespite(StatoDismissioneCespite.NON_DEFINITO);
	}

	/**
	 * Popola anno e numero elenco della dismissione
	 */
	private void popolaElencoDismissione() {
		Integer numeroElenco = dismissioneCespiteDad.staccaNumeroElenco(req.getAnnoBilancio());
		dismissioneCespite.setAnnoElenco(req.getAnnoBilancio());
		dismissioneCespite.setNumeroElenco(numeroElenco);
		
	}

}


