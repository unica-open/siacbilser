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
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaRendicontoDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * Controlla i fondi a dubbia e difficile esazione dall'anno precedente, rendiconto
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedenteService extends CheckedAccountBaseService<ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedente, ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedenteResponse> {

	//DADs
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaRendicontoDad accantonamentoFondiDubbiaEsigibilitaRendicontoDad;
	
	//FIELDS
	private Bilancio bilancioPrecedente;

	@Override
	@Transactional(readOnly = true)
	public ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedenteResponse executeService(ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedente serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//controllo di avere l'uid del bilancio
		checkEntita(req.getBilancio(), "bilancio", false);
	}
	
	@Override
	protected void execute() {
		//carico il bilancio di anno = annobilancio - 1
		ottieniBilancioPrecedente();
		//carico i fondi relativi al bilancio appena caricato
		ottieniFondiAnnoPrecedente();
	}
	
	/**
	 * Ottiene il bilancio precedente
	 */
	private void ottieniBilancioPrecedente() {
		final String methodName = "ottiniBilancioPrecedente";
		//carico il bilancio precedente
		bilancioPrecedente = bilancioDad.getBilancioAnnoPrecedente(req.getBilancio());
		if(bilancioPrecedente == null) {
			//non essite un bilancio di anno = anno del bilancio in request -1: lancio un'eccezione
			throw new BusinessException("Nessun bilancio con anno precedente reperito per il bilancio con uid " + req.getBilancio().getUid());
		}
		log.debug(methodName, "Trovato bilancio anno precedente con uid " + bilancioPrecedente.getUid());
	}
	
	private void ottieniFondiAnnoPrecedente() {
		final String methodName = "ottieniFondiAnnoPrecedente";
		//conto quanti accantonamenti ci siano nel bilancio precedente
		Long numeroFondiAnnoPrecedente = accantonamentoFondiDubbiaEsigibilitaRendicontoDad.countByBilancio(bilancioPrecedente);
		
		//imposto in response i dati trovati
		log.debug(methodName, "Numero fondi anno precedente: " + numeroFondiAnnoPrecedente);
		res.setNumeroFondiAnnoPrecedente(numeroFondiAnnoPrecedente);
	}

}
