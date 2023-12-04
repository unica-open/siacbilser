/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSoggettoAllegatoResponse;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;

/**
 * Ricerca dei dati di soggetto allegato.
 * 
 * @author Alessandro Marchino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDatiSoggettoAllegatoService extends CheckedAccountBaseService<RicercaDatiSoggettoAllegato,RicercaDatiSoggettoAllegatoResponse> {
	
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
		checkCondition(req.getAllegatoAtto().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"));
	}

	@Override
	@Transactional(readOnly = true)
	public RicercaDatiSoggettoAllegatoResponse executeService(RicercaDatiSoggettoAllegato serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		List<DatiSoggettoAllegato> list = allegatoAttoDad.findDatiSoggettoAllegatoByAllegatoAtto(req.getAllegatoAtto());
		res.setDatiSoggettiAllegati(list);
		res.setCardinalitaComplessiva(res.getDatiSoggettiAllegati().size());
	}
	
}
