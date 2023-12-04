/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaAnagraficaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaAnagraficaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.model.StatoDismissioneCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

/**
 * Inserisce e completa gli Elenchi di Documenti Spesa, Entrata, IVA Spesa, Iva
 * Entrata, comprensivi di AllegatoAtto.
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAnagraficaDismissioneCespiteService extends BaseInserisciAggiornaAnagraficaDismissioneCespiteService<AggiornaAnagraficaDismissioneCespite, AggiornaAnagraficaDismissioneCespiteResponse> {


	@Override
	protected void checkServiceParam() throws ServiceParamError {
		dismissioneCespite = req.getDismissioneCespite();
		checkEntita(dismissioneCespite, "dismissione cespite");
		checkCampiDismissioneCespite();
	}

	@Transactional
	@Override
	public AggiornaAnagraficaDismissioneCespiteResponse executeService(AggiornaAnagraficaDismissioneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkEvento();
		caricaCausale();
		checkCausale();
		caricaAttoAmministrativo();
		
		caricaStatoDismissioneCespite();
		
		dismissioneCespiteDad.aggiornaDismissioneCespite(dismissioneCespite);
		
		annullaPrimeNoteProvvisorie();
		
		res.setDismissioneCespite(dismissioneCespite);
	}

	/**
	 * 
	 */
	private void caricaStatoDismissioneCespite() {
		StatoDismissioneCespite statoDismissioneCespite = dismissioneCespiteDad.ottieniStatoDismissioneDaPrimeNoteCollegate(dismissioneCespite);
		dismissioneCespite.setStatoDismissioneCespite(statoDismissioneCespite);
	}

	/**
	 * Se viene modificata la data di cessazione tutte le eventuali prime note provvisorie collegate alla dismissione per tutti i cespiti coinvolti dovranno essere annullate e nel caso verranno reinserite dall'utente. 
	 */
	private void annullaPrimeNoteProvvisorie() {
		// TODO Auto-generated method stub
		
	}

}


