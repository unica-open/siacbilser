/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipocomponenteimpcap;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.TipoComponenteImportiCapitoloModelDetail;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaTipoComponenteImportiCapitoloService extends BaseTipoComponenteImportiCapitoloService<RicercaSinteticaTipoComponenteImportiCapitolo, RicercaSinteticaTipoComponenteImportiCapitoloResponse> {

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkParametriPaginazione(req.getParametriPaginazione());
		checkNotNull(req.getAnnoBilancio(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno bilancio"));
	}
	
	@Override
	protected void init() {
		super.init();

		if (req.getTipoComponenteImportiCapitolo() == null) {
			req.setTipoComponenteImportiCapitolo(new TipoComponenteImportiCapitolo());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaTipoComponenteImportiCapitoloResponse executeService(RicercaSinteticaTipoComponenteImportiCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		res.setListaTipoComponenteImportiCapitolo(
				tipoComponenteImportiCapitoloDad.ricercaSinteticaTipoComponenteImportiCapitolo(
						req.getTipoComponenteImportiCapitolo(), 
						req.getMacrotipoComponenteImportiCapitoloDaEscludere(),
						req.getSottotipoComponenteImportiCapitoloDaEscludere(),
						req.getPropostaDefaultComponenteImportiCapitoloDaEscludere(),
						req.getAnnoBilancio(),
						req.isSoloValidiPerBilancio(),
						req.getParametriPaginazione(),
						TipoComponenteImportiCapitoloModelDetail.AmbitoComponenteImportiCapitolo,
						TipoComponenteImportiCapitoloModelDetail.Anno,
						TipoComponenteImportiCapitoloModelDetail.FonteFinanziariaComponenteImportiCapitolo,
						TipoComponenteImportiCapitoloModelDetail.MacrotipoComponenteImportiCapitolo,
						TipoComponenteImportiCapitoloModelDetail.SottotipoComponenteImportiCapitolo,
						TipoComponenteImportiCapitoloModelDetail.MomentoComponenteImportiCapitolo,
						TipoComponenteImportiCapitoloModelDetail.PropostaDefaultComponenteImportiCapitolo,
						TipoComponenteImportiCapitoloModelDetail.StatoTipoComponenteImportiCapitolo,
						TipoComponenteImportiCapitoloModelDetail.TipoGestioneComponenteImportiCapitolo));
	}

}
