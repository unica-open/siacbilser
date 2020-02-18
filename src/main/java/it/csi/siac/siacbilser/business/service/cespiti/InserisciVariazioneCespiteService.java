/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.math.BigDecimal;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciVariazioneCespiteResponse;
import it.csi.siac.siaccespser.model.StatoVariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserimento della variazione cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/08/2018
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciVariazioneCespiteService extends BaseInserisciAggiornaVariazioneCespiteService<InserisciVariazioneCespite, InserisciVariazioneCespiteResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getVariazioneCespite(), "variazione cespite");
		variazioneCespite = req.getVariazioneCespite();
		checkCampiVariazione();
	}


	@Transactional
	@Override
	public InserisciVariazioneCespiteResponse executeService(InserisciVariazioneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		retrieveCespite();
		caricaTipoBene();
		checkTipoBene();
		
		impostaStato();
		aggiornaImportoCespite();
		
		VariazioneCespite variazioneCespiteInserita = variazioneCespiteDad.inserisciVariazioneCespite(variazioneCespite);
		gestisciPrimeNote(variazioneCespiteInserita);
		// Imposto il cespite si' da avere l'importo attuale aggiornato
		variazioneCespite.setCespite(cespite);
		res.setVariazioneCespite(variazioneCespiteInserita);
	}
	
	
	private void aggiornaImportoCespite() {
		BigDecimal importoVariazione = variazioneCespite.getImporto();
		BigDecimal importoToAdd = Boolean.TRUE.equals(variazioneCespite.getFlagTipoVariazioneIncremento()) ? importoVariazione : importoVariazione.negate();
		BigDecimal newImporto = cespite.getValoreAttuale().add(importoToAdd);
		
		if(newImporto.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'importo del cespite non puo' diventare negativo"));
		}
		cespite.setValoreAttuale(newImporto);
		cespiteDad.aggiornaImportoCespite(cespite);
	}
	
	private void impostaStato() {
		variazioneCespite.setStatoVariazioneCespite(StatoVariazioneCespite.PROVVISORIO);
	}

	@Override
	public void gestisciPrimeNote(VariazioneCespite var) {
		inserisciEAssociaPrimaNota(var);
	}

}
