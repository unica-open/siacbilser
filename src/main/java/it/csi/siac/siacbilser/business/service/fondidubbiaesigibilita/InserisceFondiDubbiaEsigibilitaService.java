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
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaDad;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceFondiDubbiaEsigibilitaService extends CheckedAccountBaseService<InserisceFondiDubbiaEsigibilita, InserisceFondiDubbiaEsigibilitaResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaDad accantonamentoFondiDubbiaEsigibilitaDad;

	@Override
	@Transactional
	public InserisceFondiDubbiaEsigibilitaResponse executeService(InserisceFondiDubbiaEsigibilita serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//imposto ente e login operazione nei dad
		accantonamentoFondiDubbiaEsigibilitaDad.setLoginOperazione(loginOperazione);
		accantonamentoFondiDubbiaEsigibilitaDad.setEnte(ente);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//controllo la lista di accantonamenti ottenuta dalla request
		checkCondition(!req.getAccantonamentiFondiDubbiaEsigibilita().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("fondi a dubbia e difficile esazione"), false);
		//controllo ogni accantonamento
		for(AccantonamentoFondiDubbiaEsigibilita afde : req.getAccantonamentiFondiDubbiaEsigibilita()) {
			
			checkNotNull(afde, "accantonamento", false);
			//un accantonamento deve essere necessariamente legato ad un capitolo
			checkCondition(afde == null || (afde.getCapitolo() != null && afde.getCapitolo().getUid() != 0),
					ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("capitolo accantonamento"), false);
		}
	}
	
	@Override
	protected void execute() {
		for(AccantonamentoFondiDubbiaEsigibilita afde : req.getAccantonamentiFondiDubbiaEsigibilita()) {
			//inserisco il singolo accantonamento
			inserisciAccantonamento(afde);
		}
	}

	private void inserisciAccantonamento(AccantonamentoFondiDubbiaEsigibilita afde) {
		final String methodName = "inserisciAccantonamento";
		//controllo che non sia gia' presente un accantonamento per il capitolo
		Long countAccantonamentoPerCapitolo = accantonamentoFondiDubbiaEsigibilitaDad.countByCapitolo(afde.getCapitolo());
		if(countAccantonamentoPerCapitolo != null && countAccantonamentoPerCapitolo.longValue() > 0L) {
			//non posso procedere con l'inserimento: lancio un'eccezione
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Inserimento fondi dubbia e difficile esazione", "Accantonamento per capitolo " + afde.getCapitolo().getUid()));
		}
		
		//inserisco l'accantonamenot
		AccantonamentoFondiDubbiaEsigibilita afdeInserito = accantonamentoFondiDubbiaEsigibilitaDad.create(afde);
		log.debug(methodName, "Inserito accantonamento con uid " + afdeInserito.getUid());
		//imposto il dato inserito nella request
		res.getAccantonamentiFondiDubbiaEsigibilita().add(afdeInserito);
	}

}
