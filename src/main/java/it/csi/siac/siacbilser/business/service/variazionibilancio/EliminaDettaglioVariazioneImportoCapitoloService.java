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
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaDettaglioVariazioneImportoCapitoloService extends CheckedAccountBaseService<EliminaDettaglioVariazioneImportoCapitolo, EliminaDettaglioVariazioneImportoCapitoloResponse> {

	//DADs..
	@Autowired
	protected VariazioniDad variazioniDad;
	
	//Fields..
	private DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getDettaglioVariazioneImportoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettaglio variazione importo capitolo"));
		checkEntita(req.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo(), "variazione dettaglio variazione importo capitolo");
		
		checkEntita(req.getDettaglioVariazioneImportoCapitolo().getCapitolo(), "capitolo del dettaglio variazione importo");
	}
	
	@Override
	@Transactional
	public EliminaDettaglioVariazioneImportoCapitoloResponse executeService(EliminaDettaglioVariazioneImportoCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		dettaglioVariazioneImportoCapitolo = req.getDettaglioVariazioneImportoCapitolo();
		checkCapitoloPresenteInVariazione();
		
		variazioniDad.eliminaDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		res.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);

	}


	/**
	 * Controlla che il capitolo sia gia' presente nella variazione.
	 */
	private void checkCapitoloPresenteInVariazione() {
		boolean capitoloPresenteInVariazione = variazioniDad.checkCapitoloAssociatoAllaVariazione(dettaglioVariazioneImportoCapitolo.getCapitolo().getUid(),
				dettaglioVariazioneImportoCapitolo.getVariazioneImportoCapitolo().getUid());
		
		if(!capitoloPresenteInVariazione){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Capitolo non associato alla variazione"));
		}
	}

}
