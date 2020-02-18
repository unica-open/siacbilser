/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pcc.datioperazione;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.tesoro.fatture.ComunicaScadenzaTipo;
import it.tesoro.fatture.ComunicazioneScadenzaTipo;
import it.tesoro.fatture.StrutturaDatiOperazioneTipo;

/**
 * Popola la struttura dati operazione per l'operazione CS: Comunicazione Scadenza.
 * 
 * @author Domenico
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PopolaStrutturaDatiOperazioneCS extends PopolaStrutturaDatiOperazione {

	public PopolaStrutturaDatiOperazioneCS(RegistroComunicazioniPCC registroComunicazioniPCC) {
		super(registroComunicazioniPCC);
	}

	@Override
	public StrutturaDatiOperazioneTipo popolaStrutturaDatiOperazione() {
		SubdocumentoSpesa subdocumentoSpesa = registrazione.getSubdocumentoSpesa();

		StrutturaDatiOperazioneTipo strutturaDatiOperazione = new StrutturaDatiOperazioneTipo();
		
		
		ComunicazioneScadenzaTipo comunicazioneScadenza = new ComunicazioneScadenzaTipo();
		
		comunicazioneScadenza.setComunicaScadenza(ComunicaScadenzaTipo.SI);
		BigDecimal importoImponibile = subdocumentoSpesa.getImportoNotNull().subtract(subdocumentoSpesa.getImportoSplitReverseNotNull()).setScale(2, RoundingMode.HALF_UP);
		comunicazioneScadenza.setImporto(importoImponibile);
		
		
		GregorianCalendar dataScadenza = new GregorianCalendar();
		if(registrazione.getDataScadenza()==null){
			throw new BusinessException("La data di scadenza per l'operazione Comunicazione Scadenza (CS) deve essere valorizzata. RegistroComunicazioniPCC con uid: "+registrazione.getUid());
		}
		dataScadenza.setTime(registrazione.getDataScadenza());
		comunicazioneScadenza.setDataScadenza(dataScadenza);
		
		
		strutturaDatiOperazione.setComunicazioneScadenza(comunicazioneScadenza);
		
		return strutturaDatiOperazione;
	}

}
