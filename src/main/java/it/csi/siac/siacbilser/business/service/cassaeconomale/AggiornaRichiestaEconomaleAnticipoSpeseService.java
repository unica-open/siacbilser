/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaRichiestaEconomaleAnticipoSpeseService extends AggiornaRichiestaEconomaleService {
	
	
	@Override
	protected void checkServiceParamRichiestaEconomale() throws ServiceParamError{
//		checkEntita(richiestaEconomale.getSoggetto(), "soggetto richiesta economale");
		checkSoggettoRichiestaEconomale();
		
		checkCondition(richiestaEconomale.getImporto() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo anticipo"));
		checkCondition(StringUtils.isNotBlank(richiestaEconomale.getDescrizioneDellaRichiesta()), 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione della spesa"));
		
		checkNotNull(richiestaEconomale.getSospeso(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("sospeso"));
		checkCondition(richiestaEconomale.getSospeso().getNumeroSospeso() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero sospeso"));
		
	}
	
	@Override
	protected void init() {
		codiceTipoRichiesta = "ANTICIPO_SPESE";
		super.init();
	}
	
	@Override
	protected void determinaStato() {
		if(Boolean.TRUE.equals(datiEconomoPresenti)){
			richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.DA_RENDICONTARE);
		}else{
			richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.PRENOTATA);
		}
	}
	
}
