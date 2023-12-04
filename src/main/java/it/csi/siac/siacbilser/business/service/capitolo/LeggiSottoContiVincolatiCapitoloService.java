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
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiSottoContiVincolatiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiSottoContiVincolatiCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;



@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiSottoContiVincolatiCapitoloService extends CheckedAccountBaseService<LeggiSottoContiVincolatiCapitolo, LeggiSottoContiVincolatiCapitoloResponse>{ 
	
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private CapitoloDad capitoloDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCapitoloUscitaGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"));
	}
	
	@Override
	protected void init() {
		super.init();
		subdocumentoSpesaDad.setEnte(ente);
		capitoloDad.setEnte(ente);
	}
	
	@Transactional
	public LeggiSottoContiVincolatiCapitoloResponse executeService(LeggiSottoContiVincolatiCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	@Override
	protected void execute() {
		res.setContoTesoreria(capitoloDad.getContoTesoreriaCapitolo(req.getCapitoloUscitaGestione()));
	}
	
}
