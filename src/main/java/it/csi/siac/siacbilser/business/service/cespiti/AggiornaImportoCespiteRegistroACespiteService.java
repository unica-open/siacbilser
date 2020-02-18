/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaImportoCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaImportoCespiteRegistroACespiteResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaImportoCespiteRegistroACespiteService extends CheckedAccountBaseService<AggiornaImportoCespiteRegistroACespite, AggiornaImportoCespiteRegistroACespiteResponse> {

	//DAD
	@Autowired
	private CespiteDad cespiteDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkEntita(req.getMovimentoDettaglio(), "movimento dettaglio collegato");
		checkEntita(req.getCespite(), "cespite");
		checkNotNull(req.getImportoSuRegistroA(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo su prima nota"));
	}
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);
	}
	
	@Transactional
	@Override
	public AggiornaImportoCespiteRegistroACespiteResponse executeService(AggiornaImportoCespiteRegistroACespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		cespiteDad.aggiornaImportoSuRegistroA(req.getMovimentoDettaglio(), req.getCespite(), req.getImportoSuRegistroA());
	}

}