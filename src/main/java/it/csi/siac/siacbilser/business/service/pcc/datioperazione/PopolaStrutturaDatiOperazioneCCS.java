/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pcc.datioperazione;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.tesoro.fatture.StrutturaDatiOperazioneTipo;

/**
 * Popola la struttura dati operazione per l'operazione CCS: Cancellazione Comunicazioni Scadenza
 * <br/>
 * <strong>NOTA</strong>: nel caso di operazione CCS: CANCELLAZIONE COMUNICAZIONI SCADENZA seguire le indicazioni delle specifiche di interfaccia WS
 * (par. 4.1. Struttura del servizio WS-Proxy Operazione Contabile), in cui si specifica che: per operazione CCS (Cancellazione Comunicazioni Scadenza)
 * non &eacute; prevista la valorizzazione della corrispondente sezione "Struttura dati operazione", per cui dovrebbe essere sufficiente inviare i dati obbligatori.
 * 
 * @author Marchino Alessandro
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PopolaStrutturaDatiOperazioneCCS extends PopolaStrutturaDatiOperazione {

	public PopolaStrutturaDatiOperazioneCCS(RegistroComunicazioniPCC registrazione) {
		super(registrazione);
	}

	
	@Override
	public StrutturaDatiOperazioneTipo popolaStrutturaDatiOperazione() {
		// Cfr. la NOTA
		// Necessito almeno del wrapper?
		return new StrutturaDatiOperazioneTipo();
	}

}
