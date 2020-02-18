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
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaRendicontoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

/**
 * Eliminazione del fondo a dubbia e difficile esazione, rendiconto
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaFondoDubbiaEsigibilitaRendicontoService extends CheckedAccountBaseService<EliminaFondoDubbiaEsigibilitaRendiconto, EliminaFondoDubbiaEsigibilitaRendicontoResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaRendicontoDad accantonamentoFondiDubbiaEsigibilitaRendicontoDad;

	@Override
	@Transactional
	public EliminaFondoDubbiaEsigibilitaRendicontoResponse executeService(EliminaFondoDubbiaEsigibilitaRendiconto serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//l'accantonamento e' obbligatorio
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaRendiconto(), "accantonamento");
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		
		//elimino su db
		accantonamentoFondiDubbiaEsigibilitaRendicontoDad.elimina(req.getAccantonamentoFondiDubbiaEsigibilitaRendiconto());
		
		//loggo l'uid del record eliminato
		log.debug(methodName, "Eliminato accantonamento con uid " + req.getAccantonamentoFondiDubbiaEsigibilitaRendiconto().getUid());
		//imposto in response l'accantonamento eliminato
		res.setAccantonamentoFondiDubbiaEsigibilitaRendiconto(req.getAccantonamentoFondiDubbiaEsigibilitaRendiconto());
	}

}
