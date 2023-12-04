/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioAnagraficaVariazioneBilancioService extends CheckedAccountBaseService<RicercaDettaglioAnagraficaVariazioneBilancio, RicercaDettaglioAnagraficaVariazioneBilancioResponse> {

	@Autowired
	protected VariazioniDad variazioniDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkCondition(req.getUidVariazione()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid variazione"));
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioAnagraficaVariazioneBilancioResponse executeService(RicercaDettaglioAnagraficaVariazioneBilancio serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		VariazioneImportoCapitolo variazioneImportoCapitolo = variazioniDad.findAnagraficaVariazioneImportoCapitoloByUid(req.getUidVariazione());		
		
		if(variazioneImportoCapitolo==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("variazione importi", "uid: "+ req.getUidVariazione()));
			res.setEsito(Esito.FALLIMENTO);			
			return;
		}
		
		res.setVariazioneImportoCapitolo(variazioneImportoCapitolo);

	}

}
