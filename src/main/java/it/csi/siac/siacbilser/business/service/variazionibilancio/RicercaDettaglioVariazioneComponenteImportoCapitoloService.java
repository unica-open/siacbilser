/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitoloModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Ricerca del dettaglio della variazione componente importi capitolo
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/10/2019
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioVariazioneComponenteImportoCapitoloService extends CheckedAccountBaseService<RicercaDettaglioVariazioneComponenteImportoCapitolo, RicercaDettaglioVariazioneComponenteImportoCapitoloResponse> {

	@Autowired private CapitoloDad capitoloDad;
	@Autowired private VariazioniDad variazioniDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getDettaglioVariazioneImportoCapitolo(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Dettaglio variazione importo capitolo"));
		checkEntita(req.getDettaglioVariazioneImportoCapitolo().getCapitolo(), "Capitolo dettaglio variazione importo capitolo", false);
		checkEntita(req.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo(), "Variazione importo capitolo dettaglio variazione importo capitolo", false);
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaDettaglioVariazioneComponenteImportoCapitoloResponse executeService(RicercaDettaglioVariazioneComponenteImportoCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		Capitolo<?, ?> capitolo = capitoloDad.findOneWithMinimalData(req.getDettaglioVariazioneImportoCapitolo().getCapitolo().getUid());
		Utility.MDTL.addModelDetails(req.getModelDetails());
		
		Map<Integer, List<DettaglioVariazioneComponenteImportoCapitolo>> dettagli = variazioniDad.ricercaDettaglioVariazioneComponenteImportoCapitoloPerAnno(
				req.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo().getUid(),
				req.getDettaglioVariazioneImportoCapitolo().getCapitolo().getUid(),
				Utility.MDTL.byModelDetailClass(DettaglioVariazioneComponenteImportoCapitoloModelDetail.class));
		
		res.setListaDettaglioVariazioneComponenteImportoCapitolo(dettagli.get(capitolo.getAnnoCapitolo()));
		res.setListaDettaglioVariazioneComponenteImportoCapitolo1(dettagli.get(Integer.valueOf(capitolo.getAnnoCapitolo().intValue() + 1)));
		res.setListaDettaglioVariazioneComponenteImportoCapitolo2(dettagli.get(Integer.valueOf(capitolo.getAnnoCapitolo().intValue() + 2)));
	}
	
}
