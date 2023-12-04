/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipocomponenteimpcap;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTipoComponenteImportiCapitoloTotaliService extends BaseTipoComponenteImportiCapitoloService<RicercaTipoComponenteImportiCapitolo, RicercaTipoComponenteImportiCapitoloResponse> {

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
		
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.YEAR, req.getAnnoBilancio());
//		calendar.set(Calendar.MONTH, 0);
//		calendar.set(Calendar.DAY_OF_MONTH, 1);
//		calendar.set(Calendar.HOUR_OF_DAY, 0);
//		calendar.set(Calendar.MINUTE, 0);
//		calendar.set(Calendar.SECOND, 0);
//        Date annoBilancioInizio =  calendar.getTime();
		
		
		List<TipoComponenteImportiCapitolo> listaTipoCompImp = tipoComponenteImportiCapitoloDad.findAllByOnlyEnteProprietarioId( req.getTipoComponenteImportiCapitolo());
		res.setListaTipoComponenteImportiCapitolo(listaTipoCompImp);
		
	}

}
