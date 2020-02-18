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

import it.csi.siac.siacbilser.integration.dad.TipoGiustificativoDad;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.Sospeso;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceRichiestaEconomaleAnticipoPerTrasfertaDipendentiService extends InserisceRichiestaEconomaleService {
	
	@Autowired
	private TipoGiustificativoDad tipoGiustificativoDad;
	
	@Override
	protected void checkServiceParamRichiestaEconomale() throws ServiceParamError{
		
		checkSoggettoRichiestaEconomale();
		
		checkNotNull(richiestaEconomale.getDatiTrasfertaMissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dati trasferta missione"));
		
		checkCondition(StringUtils.isNotBlank(richiestaEconomale.getDatiTrasfertaMissione().getMotivo()),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("motivo trasferta"));
		checkCondition(StringUtils.isNotBlank(richiestaEconomale.getDatiTrasfertaMissione().getLuogo()),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("luogo trasferta"));
		checkCondition(richiestaEconomale.getDatiTrasfertaMissione().getFlagEstero() != null, 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("estero"));
		checkCondition(richiestaEconomale.getDatiTrasfertaMissione().getDataInizio() != null, 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data inizio trasferta"));
		checkCondition(richiestaEconomale.getDatiTrasfertaMissione().getDataFine() != null, 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data fine trasferta"));
		
//		checkCondition(richiestaEconomale.getGiustificativi()!=null && !richiestaEconomale.getGiustificativi().isEmpty(), 
//				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("giustificativi richiesta economale"));
		
		for(Giustificativo giustificativo : richiestaEconomale.getGiustificativi()){
			checkNotNull(giustificativo.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione giustificativo"));
			checkNotNull(giustificativo.getImportoGiustificativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo giustificativo"));
			checkEntita(giustificativo.getTipoGiustificativo(), "tipo giustificativo");
			checkEntita(giustificativo.getValuta(), "valuta giustificativo");
			checkNotNull(giustificativo.getFlagInclusoNelPagamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag incluso nel pagamento giustificativo"));
		}
		
	}
	
	
	@Override
	protected void init() {
		codiceTipoRichiesta = "ANTICIPO_TRASFERTA_DIPENDENTI";
		super.init();
	}
	
	
	@Override
	protected void checkRichiestaEconomale() {
		checkPresenzaSoggetto();
	}
	
	@Override
	protected void preInserisceRichiestaEconomale() {
		
		richiestaEconomale.setImporto(calcolaTotaleSpettanteGiustificativi());
		checkImpegno();
		
		determinaStatoOperativoGiustificativi();
		
		Integer numeroSospeso = richiestaEconomaleDad.staccaNumeroSospeso(annoBilancio, richiestaEconomale.getCassaEconomale());
		Sospeso sospeso = new Sospeso();
		sospeso.setNumeroSospeso(numeroSospeso);
		richiestaEconomale.setSospeso(sospeso);
	}
	
	@Override
	protected void determinaStato() {
		if(Boolean.TRUE.equals(datiEconomoPresenti)){
			richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.EVASA);
		}else{
			richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.PRENOTATA);
		}
	}
	
	private BigDecimal calcolaTotaleSpettanteGiustificativi() {
		BigDecimal totale = BigDecimal.ZERO;
		for(Giustificativo giustificativo : richiestaEconomale.getGiustificativi()){
			// Devo ottenere la percentuale
			TipoGiustificativo tipoGiustificativo = tipoGiustificativoDad.ricercaDettaglioTipoGiustificativo(giustificativo.getTipoGiustificativo().getUid());
			giustificativo.setTipoGiustificativo(tipoGiustificativo);
			totale = totale.add(giustificativo.getImportoSpettanteAnticipoTrasfertaNotNull());
		}
		return totale;
	}
	
}
