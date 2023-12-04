/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaRichiestaEconomalePagamentoService extends AggiornaRichiestaEconomaleService {
	
	@Autowired 
	private DocumentoDad documentoDad;
	@Autowired
	private SubdocumentoDad subdocumentoDad;
	@Autowired
	private SoggettoDad soggettoDad;
	
	
	@Override
	protected void checkServiceParamRichiestaEconomale() throws ServiceParamError{
		checkCondition(StringUtils.isNotBlank(richiestaEconomale.getDescrizioneDellaRichiesta()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione della spesa"), false);
		
		checkEntita(richiestaEconomale.getSoggetto(), "soggetto della richiesta");
		
		for(Giustificativo giustificativo : richiestaEconomale.getGiustificativi()){
			checkNotNull(giustificativo.getImportoGiustificativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo giustificativo"));
			checkCondition(giustificativo.getImportoGiustificativo() == null || giustificativo.getImportoGiustificativo().signum() > 0,ErroreCore.VALORE_NON_CONSENTITO.getErrore("Importo",": l'importo deve essere positivo"));
			checkEntita(giustificativo.getTipoGiustificativo(), "tipo giustificativo");
			checkEntita(giustificativo.getValuta(), "valuta giustificativo");
			checkNotNull(giustificativo.getFlagInclusoNelPagamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag incluso nel pagamento giustificativo"));
		}
		
	}
	
	@Override
	protected void init() {
		codiceTipoRichiesta = "PAGAMENTO";
		super.init();
	}
	
	@Override
	protected void preAggiornamentoRichiestaEconomale() {
		calcolaImporto();
	}
	
	@Override
	protected void determinaStato() {
		if(Boolean.TRUE.equals(datiEconomoPresenti)){
			richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.EVASA);
		}else{
			richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.PRENOTATA);
		}
	}
	
	private void calcolaImporto() {
		BigDecimal totale = BigDecimal.ZERO;
		for(Giustificativo giustificativo : richiestaEconomale.getGiustificativi()){
			totale = totale.add(giustificativo.getImportoGiustificativo());
		}
		richiestaEconomale.setImporto(totale);
		
	}
	
}
