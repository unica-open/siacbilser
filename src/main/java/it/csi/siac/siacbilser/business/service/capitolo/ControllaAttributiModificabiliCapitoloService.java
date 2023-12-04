/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.model.TipologiaAttributo;

/**
 * The Class ControllaAttributiModificabiliCapitoloService.
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ControllaAttributiModificabiliCapitoloService extends ControllaModificabilitaCapitoloBaseService<ControllaAttributiModificabiliCapitolo, ControllaAttributiModificabiliCapitoloResponse> {
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.capitolo.ControllaModificabilitaCapitoloBaseService#caricaLegamiACapitolo()
	 */
	@Override
	public void caricaLegamiACapitolo() {
		Map<TipologiaAttributo,Object> attributiLegatiACapitolo = capitoloDad.findAttributiLegatiACapitolo(req.getTipoCapitolo(), req.getNumeroCapitolo());
		res.setAttributiNonModificabili(attributiLegatiACapitolo);
		
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.capitolo.ControllaModificabilitaCapitoloBaseService#caricaLegamiACapitoloArticolo()
	 */
	@Override
	public void caricaLegamiACapitoloArticolo() {
		Map<TipologiaAttributo,Object> attributiLegatiACapitoloArticolo = capitoloDad.findAttributiLegatiACapitoloArticolo(req.getTipoCapitolo(),req.getNumeroCapitolo(), req.getNumeroArticolo());
		res.setAttributiNonModificabili(attributiLegatiACapitoloArticolo);
		
	}


}
