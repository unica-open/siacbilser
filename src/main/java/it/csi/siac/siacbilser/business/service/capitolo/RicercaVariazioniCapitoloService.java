/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.RiepilogoDatiVariazioneImportoCapitoloAnno;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;

/**
 * The Class RicercaVariazioniCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaVariazioniCapitoloService extends CheckedAccountBaseService<RicercaVariazioniCapitolo, RicercaVariazioniCapitoloResponse> {

	/** The variazioni dad. */
	@Autowired
	protected VariazioniDad variazioniDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitolo(), "capitolo", false);
		checkEntita(req.getBilancio(), "bilancio", false);
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaVariazioniCapitoloResponse executeService(RicercaVariazioniCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		RiepilogoDatiVariazioneImportoCapitoloAnno riepilogoDatiVariazioneImportiCapitoloPerAnnoPositive = variazioniDad.findDatiVariazioneImportoCapitoloByAnnoPositive(req.getCapitolo(), req.getBilancio());
		RiepilogoDatiVariazioneImportoCapitoloAnno riepilogoDatiVariazioneImportiCapitoloPerAnnoNegative = variazioniDad.findDatiVariazioneImportoCapitoloByAnnoNegative(req.getCapitolo(), req.getBilancio());
		
		res.setRiepilogoDatiVariazioneImportiCapitoloPerAnnoPositive(riepilogoDatiVariazioneImportiCapitoloPerAnnoPositive);
		res.setRiepilogoDatiVariazioneImportiCapitoloPerAnnoNegative(riepilogoDatiVariazioneImportiCapitoloPerAnnoNegative);
		
		res.setEsito(Esito.SUCCESSO);
	}
	
}
