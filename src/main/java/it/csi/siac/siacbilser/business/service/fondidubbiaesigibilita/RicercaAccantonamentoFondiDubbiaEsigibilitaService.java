/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaDad;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

/**
 * Ricerca sintetica dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAccantonamentoFondiDubbiaEsigibilitaService extends CheckedAccountBaseService<RicercaAccantonamentoFondiDubbiaEsigibilita, RicercaAccantonamentoFondiDubbiaEsigibilitaResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaDad accantonamentoFondiDubbiaEsigibilitaDad;

	@Override
	@Transactional(readOnly = true)
	public RicercaAccantonamentoFondiDubbiaEsigibilitaResponse executeService(RicercaAccantonamentoFondiDubbiaEsigibilita serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//i parametri di paginazione sono obbligatori
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio", false);
	}
	
	@Override
	protected void execute() {
		Utility.MDTL.addModelDetails(req.getModelDetails());
		//carico la pagina richiesta filtrando per ente, bilancio e popolando di ogni oggetto solo i dati richiesti
		List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> accantonamentiFondiDubbiaEsigibilita = accantonamentoFondiDubbiaEsigibilitaDad.ricercaAccantonamentoFondiDubbiaEsigibilita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		//imposto in response i dati ottenuti
		res.setListaAccantonamenti(accantonamentiFondiDubbiaEsigibilita);
	}

}
