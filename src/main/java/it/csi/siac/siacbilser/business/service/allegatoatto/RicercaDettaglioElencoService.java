/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioElencoService extends CheckedAccountBaseService<RicercaDettaglioElenco,RicercaDettaglioElencoResponse> {

	
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getElencoDocumentiAllegato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti allegato"));
		checkCondition(req.getElencoDocumentiAllegato().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid elenco documenti allegato"));
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioElencoResponse executeService(RicercaDettaglioElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	protected void init() {
		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);
		//elencoDocumentiAllegatoDad.setEnte(allegatoAtto.getEnte());
	}
	
	
	@Override
	protected void execute() {
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = elencoDocumentiAllegatoDad.findElencoDocumentiAllegatoCompletoById(req.getElencoDocumentiAllegato().getUid());
		
		res.setElencoDocumentiAllegato(elencoDocumentiAllegato);

	}

}
