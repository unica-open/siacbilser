/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStanziamentiCapitoliVariati;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStanziamentiCapitoliVariatiResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaStanziamentiCapitoliVariatiService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStanziamentiCapitoliVariatiService
		extends CheckedAccountBaseService<AggiornaStanziamentiCapitoliVariati, AggiornaStanziamentiCapitoliVariatiResponse>{
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getVariazioneImportoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Variazione Importo Capitolo"), false);
		checkNotNull(req.getStatoVariazionePrecedente(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Stato Variazione Precedente"), false);
		checkNotNull(req.getStatoVariazioneSuccessivo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Stato Variazione Successivo"), false);
	}
		
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public AggiornaStanziamentiCapitoliVariatiResponse executeService(AggiornaStanziamentiCapitoliVariati serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
//		VariazioneImportoCapitolo variazioneImportoCapitolo = req.getVariazioneImportoCapitolo();
//		StatoOperativoAttualeVariazioneDiBilancio statoVariazionePrecedente = req.getStatoVariazionePrecedente();
//		StatoOperativoAttualeVariazioneDiBilancio statoVariazioneSuccessivo = req.getStatoVariazioneSuccessivo();
	
		//INFO a V1 di questa operazione dovrà essere implementata in modo da non effettuare nessuna azione.
	}

}
