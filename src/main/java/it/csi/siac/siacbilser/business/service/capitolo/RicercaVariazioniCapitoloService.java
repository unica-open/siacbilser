/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.util.List;

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
import it.csi.siac.siacbilser.model.RiepilogoDatiVariazioneStatoIdVariazione;
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
		// CONTABILIA-285
		RiepilogoDatiVariazioneImportoCapitoloAnno riepilogoDatiVariazioneImportiCapitoloPerAnnoNeutre = variazioniDad.findDatiVariazioneImportoCapitoloByAnnoNeutre(req.getCapitolo(), req.getBilancio());
		
		res.setRiepilogoDatiVariazioneImportiCapitoloPerAnnoPositive(riepilogoDatiVariazioneImportiCapitoloPerAnnoPositive);
		res.setRiepilogoDatiVariazioneImportiCapitoloPerAnnoNegative(riepilogoDatiVariazioneImportiCapitoloPerAnnoNegative);
		// CONTABILIA-285
		res.setRiepilogoDatiVariazioneImportiCapitoloPerAnnoNeutre(riepilogoDatiVariazioneImportiCapitoloPerAnnoNeutre);
		//SIAC-7735
		List<RiepilogoDatiVariazioneStatoIdVariazione>  riepilogoDatiVariazioneStatoIdVariazioneList = variazioniDad.findDatiVariazioneImportoCapitoloByAnnoNeutreVarId(req.getCapitolo(), req.getBilancio());
		res.setRiepilogoDatiVariazioneStatoIdVariazioneList(riepilogoDatiVariazioneStatoIdVariazioneList);
		res.setEsito(Esito.SUCCESSO);
	}
	
}
