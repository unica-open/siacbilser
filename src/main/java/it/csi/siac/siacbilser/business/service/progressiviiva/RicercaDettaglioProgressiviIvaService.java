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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgressiviIva;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgressiviIvaResponse;
import it.csi.siac.siacbilser.integration.dad.ProgressiviIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;

/**
 * The Class RicercaDettaglioProgressiviIvaService.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioProgressiviIvaService extends CheckedAccountBaseService<RicercaDettaglioProgressiviIva, RicercaDettaglioProgressiviIvaResponse> {
	
	//DADs
	@Autowired
	private ProgressiviIvaDad progressiviIvaDad;
	
	@Override
	@Transactional(readOnly = true)
	public RicercaDettaglioProgressiviIvaResponse executeService(RicercaDettaglioProgressiviIva serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//controllo di aver i dati del progressivo necessari alla ricerca
		checkNotNull(req.getProgressiviIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progressivi iva"));
		checkCondition(req.getProgressiviIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid progressivi iva"));
		
		//controllo di aver i dati dell'ente necessari alla ricerca
		checkNotNull(req.getProgressiviIva().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente progressivi iva"));
		checkCondition(req.getProgressiviIva().getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente progressivi iva"));
	}
	
	@Override
	protected void execute() {
		
		Integer uid = req.getProgressiviIva().getUid();
		//effettuo la ricerca tramite uid
		ProgressiviIva progressiviIva = progressiviIvaDad.findProgressiviIvaByUid(uid);
		if(progressiviIva == null) {
			//se ho specificato un uid, questo deve esserci, altrimenti qualcosa e' andato storto
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Progressivi iva", "uid " + uid));
		}
		//imposto i dati in response
		res.setProgressiviIva(progressiviIva);
	}

}
