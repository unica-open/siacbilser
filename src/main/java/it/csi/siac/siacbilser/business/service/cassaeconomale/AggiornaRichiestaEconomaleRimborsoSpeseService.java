/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaRichiestaEconomaleRimborsoSpeseService extends AggiornaRichiestaEconomaleService {
	
	
	@Override
	protected void checkServiceParamRichiestaEconomale() throws ServiceParamError{
//		checkEntita(richiestaEconomale.getSoggetto(), "soggetto richiesta economale");
		checkSoggettoRichiestaEconomale();
		
		checkCondition(StringUtils.isNotBlank(richiestaEconomale.getDescrizioneDellaRichiesta()), 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione della spesa"));
		
//		checkCondition(richiestaEconomale.getGiustificativi()!=null && !richiestaEconomale.getGiustificativi().isEmpty(), 
//				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("giustificativi richiesta economale"));
		
		for(Giustificativo giustificativo : richiestaEconomale.getGiustificativi()){
			checkNotNull(giustificativo.getImportoGiustificativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo giustificativo"));
			checkCondition(giustificativo.getImportoGiustificativo() == null || giustificativo.getImportoGiustificativo().signum() > 0,ErroreCore.VALORE_NON_CONSENTITO.getErrore("Importo",": l'importo deve essere positivo"));
			checkNotNull(giustificativo.getTipoGiustificativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo giustificativo"));
			checkNotNull(giustificativo.getValuta(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("valuta giustificativo"));
			checkNotNull(giustificativo.getFlagInclusoNelPagamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag incluso nel pagamento giustificativo"));
		}
	}
	
	
	@Override
	protected void init() {
		codiceTipoRichiesta = "RIMBORSO_SPESE";
		super.init();
	}
	
	
	@Override
	protected void preAggiornamentoRichiestaEconomale() {
		richiestaEconomale.setImporto(calcolaTotaleGiustificativi());
		
		determinaStatoOperativoGiustificativi();
	}
	
	
	@Override
	protected void determinaStato() {
		if(Boolean.TRUE.equals(datiEconomoPresenti)){
			richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.EVASA);
		}else{
			richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.PRENOTATA);
		}
	}
	
	
	private BigDecimal calcolaTotaleGiustificativi() {
		BigDecimal totale = BigDecimal.ZERO;
		for(Giustificativo giustificativo : richiestaEconomale.getGiustificativi()){
			totale = totale.add(giustificativo.getImportoGiustificativo());
		}
		return totale;
	}
	
}
