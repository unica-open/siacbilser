/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.gestione;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.EliminaAccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.EliminaAccantonamentoFondiDubbiaEsigibilitaGestioneResponse;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaAccantonamentoFondiDubbiaEsigibilitaGestioneService extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaGestioneService<EliminaAccantonamentoFondiDubbiaEsigibilitaGestione, EliminaAccantonamentoFondiDubbiaEsigibilitaGestioneResponse> {
	
	@Override
	@Transactional
	public EliminaAccantonamentoFondiDubbiaEsigibilitaGestioneResponse executeService(EliminaAccantonamentoFondiDubbiaEsigibilitaGestione serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getListaAccantonamentoFondiDubbiaEsigibilitaGestione(), "lista accantonamenti");
		checkCondition(!req.getListaAccantonamentoFondiDubbiaEsigibilitaGestione().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("lista accantonamenti"));
		int i = 1;
		for(AccantonamentoFondiDubbiaEsigibilitaGestione afde : req.getListaAccantonamentoFondiDubbiaEsigibilitaGestione()) {
			checkEntita(afde, "accantonamento numero " + i++);
		}
	}
	
	@Override
	protected void execute() {
		loadAttributiBilancio(req.getListaAccantonamentoFondiDubbiaEsigibilitaGestione().get(0));
		for(AccantonamentoFondiDubbiaEsigibilitaGestione afde : req.getListaAccantonamentoFondiDubbiaEsigibilitaGestione()) {
			// Cancellazione dell'accantonamento
			accantonamentoFondiDubbiaEsigibilitaGestioneDad.elimina(afde);
		}
		
		// popolaResponse
		List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> accantonamentiFondiDubbiaEsigibilita = caricaAccantonamenti(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		res.setListaAccantonamenti(accantonamentiFondiDubbiaEsigibilita);
	}
	
}
