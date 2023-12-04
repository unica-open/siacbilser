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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneService extends CheckedAccountBaseService<RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione, RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse> {

	@Autowired
	protected VariazioniDad variazioniDad;
	
	private Capitolo<?,?> capitolo;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getUidVariazione() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid variazione"), false);
		capitolo = req.getCapitolo();
		checkCondition(capitolo != null && capitolo.getNumeroCapitolo() != null && capitolo.getNumeroCapitolo() != null && capitolo.getTipoCapitolo() != null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"), false);
		
		checkEntita(req.getBilancio(), "bilancio");
		checkCondition(req.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"), false);
	}
	
	@Override
	protected void init() {
		variazioniDad.setEnte(ente);
		variazioniDad.setBilancio(req.getBilancio());
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse executeService(RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		capitolo.setAnnoCapitolo(Integer.valueOf(req.getBilancio().getAnno()));
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = variazioniDad.findSingoloDettaglioVariazioneImportoCapitoloByUidVariazioneAndUidCapitolo(req.getUidVariazione(), capitolo);
		checkBusinessCondition(dettaglioVariazioneImportoCapitolo != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("dettaglio per capitolo", capitolo.getAnnoNumeroArticolo()));
		res.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		
	}

}
