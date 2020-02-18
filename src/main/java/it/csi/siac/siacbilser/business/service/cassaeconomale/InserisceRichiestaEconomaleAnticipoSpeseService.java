/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccecser.model.Sospeso;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceRichiestaEconomaleAnticipoSpeseService extends InserisceRichiestaEconomaleService {
	
	@Override
	protected void checkServiceParamRichiestaEconomale() throws ServiceParamError{
		checkSoggettoRichiestaEconomale();
		
		checkCondition(richiestaEconomale.getImporto() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo anticipo"));
		checkCondition(StringUtils.isNotBlank(richiestaEconomale.getDescrizioneDellaRichiesta()), 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione della spesa"));
		
	}
	
	
	@Override
	protected void init() {
		codiceTipoRichiesta = "ANTICIPO_SPESE";
		super.init();
	}
	
	
	@Override
	protected void checkRichiestaEconomale() {
		checkPresenzaSoggetto();
	}
	

	@Override
	protected void preInserisceRichiestaEconomale() {
		checkImpegno();
		
		Integer numeroSospeso = richiestaEconomaleDad.staccaNumeroSospeso(annoBilancio, richiestaEconomale.getCassaEconomale());
		Sospeso sospeso = new Sospeso();
		sospeso.setNumeroSospeso(numeroSospeso);
		richiestaEconomale.setSospeso(sospeso);
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
