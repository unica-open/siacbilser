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
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaRendicontoDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserimento dei fondi a dubbia e difficile esazione, rendiconto
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceFondiDubbiaEsigibilitaRendicontoService extends CheckedAccountBaseService<InserisceFondiDubbiaEsigibilitaRendiconto, InserisceFondiDubbiaEsigibilitaRendicontoResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaRendicontoDad accantonamentoFondiDubbiaEsigibilitaRendicontoDad;
	@Autowired
	private CapitoloDad capitoloDad;

	@Override
	@Transactional
	public InserisceFondiDubbiaEsigibilitaRendicontoResponse executeService(InserisceFondiDubbiaEsigibilitaRendiconto serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//imposto ente e login operazione nei dad
		accantonamentoFondiDubbiaEsigibilitaRendicontoDad.setLoginOperazione(loginOperazione);
		accantonamentoFondiDubbiaEsigibilitaRendicontoDad.setEnte(ente);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//controllo la lista degfli accantonamenti della request
		checkCondition(!req.getAccantonamentiFondiDubbiaEsigibilitaRendiconto().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("fondi a dubbia e difficile esazione"), false);
		for(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde : req.getAccantonamentiFondiDubbiaEsigibilitaRendiconto()) {
			checkNotNull(afde, "accantonamento", false);
			//un accantonamento e' necessariamente legato ad un capitolo
			checkCondition(afde == null || (afde.getCapitolo() != null && afde.getCapitolo().getUid() != 0),
					ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("capitolo accantonamento"), false);
		}
	}
	
	@Override
	protected void execute() {
		for(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde : req.getAccantonamentiFondiDubbiaEsigibilitaRendiconto()) {
			//inserisco il singolo accantonamento
			inserisciAccantonamento(afde);
		}
	}

	private void inserisciAccantonamento(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		final String methodName = "inserisciAccantonamento";
		//cxontrollo che non vi siano 
		Long countAccantonamentoPerCapitolo = accantonamentoFondiDubbiaEsigibilitaRendicontoDad.countByCapitolo(afde.getCapitolo());
		if(countAccantonamentoPerCapitolo != null && countAccantonamentoPerCapitolo.longValue() > 0L) {
			// SIAC-6031: Capitolo trovato ma senza dati. Ricerco la chiave de capitolo
			Capitolo<?, ?> cap = capitoloDad.findOneWithMinimalData(afde.getCapitolo().getUid());
			String key = isGestioneUEB() ? cap.getAnnoNumeroArticoloUEB() : cap.getAnnoNumeroArticolo();			
			//non posso continuare
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Inserimento fondi dubbia e difficile esazione", "Accantonamento per capitolo " + key));
		}
		//inserisco l'accantonamento
		AccantonamentoFondiDubbiaEsigibilitaRendiconto afdeInserito = accantonamentoFondiDubbiaEsigibilitaRendicontoDad.create(afde);
		log.debug(methodName, "Inserito accantonamento con uid " + afdeInserito.getUid());
		res.getAccantonamentiFondiDubbiaEsigibilitaRendiconto().add(afdeInserito);
	}

}
