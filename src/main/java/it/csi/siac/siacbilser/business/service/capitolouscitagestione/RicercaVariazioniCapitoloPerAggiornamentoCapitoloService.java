/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaVariazioniCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaVariazioniCapitoloPerAggiornamentoCapitoloService
		extends CheckedAccountBaseService<RicercaVariazioniCapitoloPerAggiornamentoCapitolo, RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse> {

	@Autowired
	protected VariazioniDad variazioniDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getUidCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"), true);
		checkCondition(req.getUidCapitolo().intValue() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo"), false);
	}
	
	@Transactional(readOnly = true)
	public RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse executeService(RicercaVariazioniCapitoloPerAggiornamentoCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		Collection<TipoVariazione> tipiVariazione = EnumSet.allOf(TipoVariazione.class);
		tipiVariazione.remove(TipoVariazione.VARIAZIONE_CODIFICA);
		
		List<Integer> numeroVariazioniImporti =  variazioniDad.calcolaNumeroVariazioni(req.getUidCapitolo(), tipiVariazione);
		res.setVariazioneImportiNumero(numeroVariazioniImporti);
		
		List<Integer> numeroVariazioniCodifiche =  variazioniDad.calcolaNumeroVariazioniCodifiche(req.getUidCapitolo());
		res.setVariazioneCodificheNumero(numeroVariazioniCodifiche);
	}
	
}
