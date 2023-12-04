/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.math.BigDecimal;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitoloResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


// TODO: Auto-generated Javadoc
/* Consente di calcolare la disponibilità di un capitolo di Bilancio 
 * con i dati passati in input.
 * VM*/

/**
 * The Class CalcoloDisponibilitaDiUnCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalcoloDisponibilitaDiUnCapitoloService
		extends CheckedAccountBaseService<CalcoloDisponibilitaDiUnCapitolo, CalcoloDisponibilitaDiUnCapitoloResponse>{ 
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Bilancio"), false);
		checkNotNull(req.getFase(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Fase di bilancio"), false);
		checkCondition(req.getAnnoCapitolo()>0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno Capitolo"), false);
		checkCondition(req.getNumroCapitolo()>0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero Capitolo"), false);
		checkNotNull(req.getTipoDisponibilitaRichiesta(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Tipo Disponibilità Richiesta"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public CalcoloDisponibilitaDiUnCapitoloResponse executeService(CalcoloDisponibilitaDiUnCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {		
//		Ente ente = req.getEnte();
//		Bilancio bilancio = req.getBilancio();
//		FaseEStatoAttualeBilancio faseEStatoAttualeBilancio = req.getFase();
//		int annoCapitolo = req.getAnnoCapitolo();
//		int numroCapitolo = req.getNumroCapitolo();
//		String tipoDisponibilitaRichiesta = req.getTipoDisponibilitaRichiesta();
		
		//INFO nella versione 1 l'implementazione restituisce un importo a 0.
		
		res.setDisponibilitaRichiesta(BigDecimal.ZERO);
		res.setEsito(Esito.SUCCESSO);
	}
}
