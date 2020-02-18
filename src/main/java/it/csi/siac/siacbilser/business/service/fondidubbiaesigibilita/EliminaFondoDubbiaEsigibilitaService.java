/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

/**
 * Eliminazione del fondo a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaFondoDubbiaEsigibilitaService extends CheckedAccountBaseService<EliminaFondoDubbiaEsigibilita, EliminaFondoDubbiaEsigibilitaResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaDad accantonamentoFondiDubbiaEsigibilitaDad;

	@Override
	@Transactional
	public EliminaFondoDubbiaEsigibilitaResponse executeService(EliminaFondoDubbiaEsigibilita serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//l'accantonamento e' obbligatorio
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilita(), "accantonamento");
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		
		//elimino su db
		accantonamentoFondiDubbiaEsigibilitaDad.elimina(req.getAccantonamentoFondiDubbiaEsigibilita());
		//loggo l'uid del record eliminato
		log.debug(methodName, "Eliminato accantonamento con uid " + req.getAccantonamentoFondiDubbiaEsigibilita().getUid());
		//imposto in response l'accantonamento eliminato
		res.setAccantonamentoFondiDubbiaEsigibilita(req.getAccantonamentoFondiDubbiaEsigibilita());
	}

}
