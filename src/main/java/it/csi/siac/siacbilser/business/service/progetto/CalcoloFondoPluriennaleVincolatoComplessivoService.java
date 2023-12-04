/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivoResponse;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoTotale;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class CalcoloFondoPluriennaleVincolatoComplessivoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalcoloFondoPluriennaleVincolatoComplessivoService extends CheckedAccountBaseService<CalcoloFondoPluriennaleVincolatoComplessivo, CalcoloFondoPluriennaleVincolatoComplessivoResponse> {
	
	
	@Autowired
	private ProgettoDad progettoDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getProgetto(), "progetto");
		checkNotNull(req.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"));
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public CalcoloFondoPluriennaleVincolatoComplessivoResponse executeService(CalcoloFondoPluriennaleVincolatoComplessivo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	@Override
	protected void execute() {
		List<FondoPluriennaleVincolatoTotale> listaFondoPluriennaleVincolatoTotale = progettoDad.calcoloFpvTotale(req.getProgetto(), req.getAnno());
		res.setListaFondoPluriennaleVincolatoTotale(listaFondoPluriennaleVincolatoTotale);
		
	}


	


}
