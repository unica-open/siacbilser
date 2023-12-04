/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.rendiconto;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.EliminaAccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.EliminaAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaAccantonamentoFondiDubbiaEsigibilitaRendicontoService extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaRendicontoService<EliminaAccantonamentoFondiDubbiaEsigibilitaRendiconto, EliminaAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse> {
	
	@Override
	@Transactional
	public EliminaAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse executeService(EliminaAccantonamentoFondiDubbiaEsigibilitaRendiconto serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(), "lista accantonamenti");
		checkCondition(!req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("lista accantonamenti"));
		int i = 1;
		for(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde : req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto()) {
			checkEntita(afde, "accantonamento numero " + i++);
		}
	}
	
	@Override
	protected void execute() {
		loadAttributiBilancio(req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto().get(0));
		for(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde : req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto()) {
			// Cancellazione dell'accantonamento
			accantonamentoFondiDubbiaEsigibilitaRendicontoDad.elimina(afde);
		}
		// popolaResponse
		List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> accantonamentiFondiDubbiaEsigibilita = caricaAccantonamenti(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		res.setListaAccantonamenti(accantonamentiFondiDubbiaEsigibilita);
	}
	
}
