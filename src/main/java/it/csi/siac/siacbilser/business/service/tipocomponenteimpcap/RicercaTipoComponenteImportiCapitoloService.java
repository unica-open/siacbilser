/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipocomponenteimpcap;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacfin2ser.model.TipoComponenteImportiCapitoloModelDetail;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTipoComponenteImportiCapitoloService extends BaseTipoComponenteImportiCapitoloService<RicercaTipoComponenteImportiCapitolo, RicercaTipoComponenteImportiCapitoloResponse> {

	@Override
	@Transactional(readOnly = true)
	public RicercaTipoComponenteImportiCapitoloResponse executeService(RicercaTipoComponenteImportiCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void init() {
		super.init();

		if (req.getTipoComponenteImportiCapitolo() == null) {
			req.setTipoComponenteImportiCapitolo(new TipoComponenteImportiCapitolo());
		}
	}
	
	@Override
	protected void execute() {
		res.setListaTipoComponenteImportiCapitolo(
				tipoComponenteImportiCapitoloDad.ricercaTipoComponenteImportiCapitolo(
						req.getTipoComponenteImportiCapitolo(),
						req.getMacrotipoComponenteImportiCapitoloDaEscludere(),
						req.getSottotipoComponenteImportiCapitoloDaEscludere(),
						req.getPropostaDefaultComponenteImportiCapitoloDaEscludere(),
						//SIAC-7349
						req.getImpegnabileComponenteImportiCapitoloDaEscludere(),
						null,
						false,
						TipoComponenteImportiCapitoloModelDetail.AmbitoComponenteImportiCapitolo,
						TipoComponenteImportiCapitoloModelDetail.Anno,
						TipoComponenteImportiCapitoloModelDetail.FonteFinanziariaComponenteImportiCapitolo,
						TipoComponenteImportiCapitoloModelDetail.MacrotipoComponenteImportiCapitolo,
						TipoComponenteImportiCapitoloModelDetail.SottotipoComponenteImportiCapitolo,
						TipoComponenteImportiCapitoloModelDetail.MomentoComponenteImportiCapitolo,
						TipoComponenteImportiCapitoloModelDetail.PropostaDefaultComponenteImportiCapitolo,
						//SIAC-7349
						//TipoComponenteImportiCapitoloModelDetail.TipoGestioneComponenteImportiCapitolo
						TipoComponenteImportiCapitoloModelDetail.ImpegnabileComponenteImportiCapitolo));
	}

}
