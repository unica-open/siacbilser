/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSospensioneAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSospensioneAllegatoAttoResponse;

/**
 * Ricerca dei dati di soggetto allegato.
 * 
 * @author Alessandro Marchino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDatiSospensioneAllegatoAttoService extends CheckedAccountBaseService<RicercaDatiSospensioneAllegatoAtto,RicercaDatiSospensioneAllegatoAttoResponse> {
	
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getAllegatoAtto(), "allegato atto");
	}

	@Override
	@Transactional(readOnly = true)
	public RicercaDatiSospensioneAllegatoAttoResponse executeService(RicercaDatiSospensioneAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void execute() {
		
		Set<?>[] datiSospensione;
		if(req.getElencoDocumentiAllegato() != null && req.getElencoDocumentiAllegato().getUid() != 0) {
			datiSospensione = allegatoAttoDad.caricaDatiSospensionePerElenco(req.getElencoDocumentiAllegato());
		}else {
		
			datiSospensione = allegatoAttoDad.caricaDatiSospensionePerAllegatoAtto(req.getAllegatoAtto());
		
		}
		
		res.setDataSospensione((Set<Date>) datiSospensione[0]);
		res.setDataRiattivazione((Set<Date>) datiSospensione[1]);
		res.setCausaleSospensioneAllegato((Set<String>) datiSospensione[2]);
		
	
	}
}
