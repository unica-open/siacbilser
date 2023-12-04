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
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaProgressiviIva;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaProgressiviIvaResponse;
import it.csi.siac.siacbilser.integration.dad.ProgressiviIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;

/**
 * The Class AggiornaProgressiviIvaService.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaProgressiviIvaService extends CheckedAccountBaseService<AggiornaProgressiviIva, AggiornaProgressiviIvaResponse> {
	
	//DADs
	@Autowired
	private ProgressiviIvaDad progressiviIvaDad;
	
	//FIELDS
	private ProgressiviIva progressiviIva;
	
	@Override
	@Transactional
	public AggiornaProgressiviIvaResponse executeService(AggiornaProgressiviIva serviceRequest) {
		//inizio l'esecuzione
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//controllo il progressivo iva passato dalla request
		checkNotNull(req.getProgressiviIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progressivi iva"));
		checkCondition(req.getProgressiviIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid progressivi iva"));
		
		//controllo l'ente passato dalla request
		checkNotNull(req.getProgressiviIva().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente progressivi iva"));
		checkCondition(req.getProgressiviIva().getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente progressivi iva"));
		
		//controllo il resgistro passata dalla request
		checkNotNull(req.getProgressiviIva().getRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro iva progressivi iva"));
		checkCondition(req.getProgressiviIva().getRegistroIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid registro iva progressivi iva"));
		
		//controllo l'aliquota passata dalla request
		checkNotNull(req.getProgressiviIva().getAliquotaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aliquota iva progressivi iva"));
		checkCondition(req.getProgressiviIva().getAliquotaIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid aliquota iva progressivi iva"));
	}
	
	@Override
	protected void init() {
		progressiviIva = req.getProgressiviIva();
		//il dad ha bisogno del login operazione per poter aggiornare il record
		progressiviIvaDad.setLoginOperazione(loginOperazione);
	}

	@Override
	protected void execute() {
		//chiamo il dad
		progressiviIvaDad.aggiornaProgressiviIva(progressiviIva);
		//imposto in response il progressivo con i dati che ho aggiornato
		res.setProgressiviIva(progressiviIva);
	}

}
