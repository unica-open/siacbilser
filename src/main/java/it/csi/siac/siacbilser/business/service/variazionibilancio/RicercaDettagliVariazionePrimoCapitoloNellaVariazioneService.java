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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettagliVariazionePrimoCapitoloNellaVariazioneService extends CheckedAccountBaseService<RicercaDettagliVariazioneImportoCapitoloNellaVariazione, RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse> {

	@Autowired
	protected VariazioniDad variazioniDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getUidVariazione() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid variazione"));
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri di paginazione"));
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse executeService(RicercaDettagliVariazioneImportoCapitoloNellaVariazione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		Bilancio bilancio = variazioniDad.getBilancioByVariazione(req.getUidVariazione());
		int annoBilancio = bilancio.getAnno();
		
		Integer idFirstChapter = variazioniDad.getIdPrimoCapitoloAssociato(req.getUidVariazione(), req.getTipoCapitolo(), annoBilancio);

		if(idFirstChapter != null && idFirstChapter>0){
			ListaPaginata<DettaglioVariazioneImportoCapitolo> lista = variazioniDad.findDettagliVariazionePrimoCapitoloByUidVariazione(req.getUidVariazione(), req.getParametriPaginazione(), idFirstChapter);			
			res.setListaDettaglioVariazioneImportoCapitolo(lista);
		}
		
		
		
		
	}
	

}
