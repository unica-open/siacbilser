/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaMutuoService extends BaseMutuoService<AnnullaMutuo, AnnullaMutuoResponse> {
		
	@Override
	@Transactional
	public AnnullaMutuoResponse executeService(AnnullaMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		super.execute();
		
		checkStato();
		
		mutuo = mutuoDad.annulla(mutuo);

		//res.setMutuo(mutuo);
	}

	private void checkStato() {
		checkBusinessCondition(
			! StatoMutuo.Annullato.equals(mutuoCorrente.getStatoMutuo()), 
			ErroreCore.TRANSAZIONE_DI_STATO_NON_POSSIBILE.getErrore("- mutuo annullato oppure presenza di rate scadute")
		);
	}

}
