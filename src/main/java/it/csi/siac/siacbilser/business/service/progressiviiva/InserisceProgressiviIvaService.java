/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progressiviiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceProgressiviIva;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceProgressiviIvaResponse;
import it.csi.siac.siacbilser.integration.dad.ProgressiviIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;

/**
 * The Class InserisceProgressiviIvaService.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceProgressiviIvaService extends CheckedAccountBaseService<InserisceProgressiviIva, InserisceProgressiviIvaResponse> {
	
	//DADs
	@Autowired
	private ProgressiviIvaDad progressiviIvaDad;
	
	//fields
	private ProgressiviIva progressiviIva;
	
	@Override
	@Transactional
	public InserisceProgressiviIvaResponse executeService(InserisceProgressiviIva serviceRequest) {
		//inizio l'esecuzione
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//controllo che mi sia stato passato un progressivo in request
		checkNotNull(req.getProgressiviIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progressivi iva"));
		
		//cvontrollo l'ente del progressiv iva
		checkNotNull(req.getProgressiviIva().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente progressivi iva"));
		checkCondition(req.getProgressiviIva().getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente progressivi iva"));
		
		//controllo il registro iva
		checkNotNull(req.getProgressiviIva().getRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro iva progressivi iva"));
		checkCondition(req.getProgressiviIva().getRegistroIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid registro iva progressivi iva"));
		
		//controllo l'aliquota iva
		checkNotNull(req.getProgressiviIva().getAliquotaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aliquota iva progressivi iva"));
		checkCondition(req.getProgressiviIva().getAliquotaIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid aliquota iva progressivi iva"));
	}
	
	@Override
	protected void init() {
		progressiviIva = req.getProgressiviIva();
		
		//imposto il login operazione nei dad
		progressiviIvaDad.setLoginOperazione(loginOperazione);
	}

	@Override
	protected void execute() {
		//effetto l'inserimento
		progressiviIvaDad.inserisciProgressiviIva(progressiviIva);
		//imposto il dato appena inserito in response
		res.setProgressiviIva(progressiviIva);
	}

}
