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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaRendicontoDad;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * Ricerca sintetica dei fondi a dubbia e difficile esazione, rendiconto
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaFondiDubbiaEsigibilitaRendicontoService extends CheckedAccountBaseService<RicercaSinteticaFondiDubbiaEsigibilitaRendiconto, RicercaSinteticaFondiDubbiaEsigibilitaRendicontoResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaRendicontoDad accantonamentoFondiDubbiaEsigibilitaRendicontoDad;

	@Override
	@Transactional
	public RicercaSinteticaFondiDubbiaEsigibilitaRendicontoResponse executeService(RicercaSinteticaFondiDubbiaEsigibilitaRendiconto serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//i parametri di paginazione sono obbligatori
		checkParametriPaginazione(req.getParametriPaginazione(), false);
	}
	
	@Override
	protected void execute() {
		//carico la pagina richiesta filtrando per ente, bilancio e popolando di ogni oggetto solo i dati richiesti
		ListaPaginata<AccantonamentoFondiDubbiaEsigibilitaRendiconto> accantonamentiFondiDubbiaEsigibilitaRendiconto = accantonamentoFondiDubbiaEsigibilitaRendicontoDad.ricercaSintetica(
				ente,
				req.getBilancio(),
				req.getAccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetails(),
				req.getParametriPaginazione());
		//imposto in response i dati ottenuti
		res.setAccantonamentiFondiDubbiaEsigibilitaRendiconto(accantonamentiFondiDubbiaEsigibilitaRendiconto);
	}

}
