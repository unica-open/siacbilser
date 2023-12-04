/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class InserisceRendicontoRichiestaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaRendicontoRichiestaService extends InserisceAggiornaRendicontoRichiestaService<AggiornaRendicontoRichiesta, AggiornaRendicontoRichiestaResponse> {
		
	
//	@Autowired
//	private RendicontoRichiestaEconomaleDad rendicontoRichiestaEconomaleDad;
//	@Autowired
//	private RichiestaEconomaleDad richiestaEconomaleDad;
//	@Autowired
//	private ImpegnoBilDad impegnoBilDad;
//	
//	@Autowired
//	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
//	
//	private RendicontoRichiesta rendicontoRichiesta;
	private Calendar now;
	private Integer annoCorrente;
//
//	private Boolean isNecessarioMovimento;
//	private Boolean isRestituzioneTotale;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.rendicontoRichiesta = req.getRendicontoRichiesta();
		
		checkEntita(rendicontoRichiesta, "rendiconto richiesta");
		checkEntita(rendicontoRichiesta.getRichiestaEconomale(), "richiesta economale");
		
		checkNotNull(rendicontoRichiesta.getRichiestaEconomale().getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo anticipo "));
		
		rendicontoRichiesta.setEnte(ente);
		
		checkNotNull(rendicontoRichiesta.getDataRendiconto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data rendiconto")); 
		
		checkNotNull(rendicontoRichiesta.getImportoIntegrato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo integrato"));
		checkCondition(rendicontoRichiesta.getImportoIntegrato().compareTo(BigDecimal.ZERO)>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo integrato (maggiore di zero) "));
		checkNotNull(rendicontoRichiesta.getImportoRestituito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo restituito"));
		checkCondition(rendicontoRichiesta.getImportoRestituito().compareTo(BigDecimal.ZERO)>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo restituito (maggiore di zero) "));
		
		this.isNecessarioMovimento  = rendicontoRichiesta.getImportoIntegrato().compareTo(BigDecimal.ZERO)>0 || 
									  rendicontoRichiesta.getImportoRestituito().compareTo(BigDecimal.ZERO)>0;
									  
		if(Boolean.TRUE.equals(isNecessarioMovimento)){
			checkCondition(rendicontoRichiesta.getMovimento()!=null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("movimento"));
			checkNotNull(rendicontoRichiesta.getMovimento().getDataMovimento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data movimento"));
			checkEntita(rendicontoRichiesta.getMovimento().getModalitaPagamentoDipendente(), "modalita pagamento dipendente del movimento");
			checkCondition(StringUtils.isNotBlank(rendicontoRichiesta.getMovimento().getDettaglioPagamento()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettaglio pagamento del movimento"));
			checkEntita(rendicontoRichiesta.getMovimento().getModalitaPagamentoCassa(), "modalita pagamento cassa del movimento");
		} else {
			rendicontoRichiesta.setMovimento(null);
		}
		
		this.isRestituzioneTotale = rendicontoRichiesta.getImportoRestituito().compareTo(rendicontoRichiesta.getRichiestaEconomale().getImporto()) == 0;
		if(!Boolean.TRUE.equals(isRestituzioneTotale)){
//			checkCondition(rendicontoRichiesta.getGiustificativi()!=null && !rendicontoRichiesta.getGiustificativi().isEmpty(), 
//					ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("giustificativi rendiconto richiesta"));
			
			for(Giustificativo giustificativo : rendicontoRichiesta.getGiustificativi()){
				checkNotNull(giustificativo.getImportoGiustificativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo giustificativo"));
				checkCondition(giustificativo.getImportoGiustificativo() == null || giustificativo.getImportoGiustificativo().signum() > 0,ErroreCore.VALORE_NON_CONSENTITO.getErrore("Importo",": l'importo deve essere positivo"));
				checkNotNull(giustificativo.getTipoGiustificativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo giustificativo"));
				checkNotNull(giustificativo.getValuta(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("valuta giustificativo"));
				checkNotNull(giustificativo.getFlagInclusoNelPagamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag incluso nel pagamento giustificativo"));
				//TODO qual è il tipo 'FATTURA'? 
				if("FAT".equals(giustificativo.getTipoGiustificativo().getCodice())){
					checkCondition(StringUtils.isNotBlank(giustificativo.getNumeroProtocollo()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero protocollos del giustificativo"));
				}
			}
		}
		

		
	}
	
	
	@Override
	protected void init() {
		super.init();
		
		now = new GregorianCalendar();
		annoCorrente = now.get(Calendar.YEAR); //TODO verificare se va bene l'anno corrente
	}

	@Override
	@Transactional
	public AggiornaRendicontoRichiestaResponse executeService(AggiornaRendicontoRichiesta serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		caricaRichiestaEconomale();
		
		checkDettaglioPagamento();
		creaMovimentoSePrevisto();
		determinaStatoOperativoGiustificativi();
		rendicontoRichiestaEconomaleDad.aggiornaRendicontoRichiesta(rendicontoRichiesta);
		richiestaEconomaleDad.aggiornaStatoRichiestaEconomale(rendicontoRichiesta.getRichiestaEconomale(), determinaStatoOperativoRichiesta());
		richiestaEconomaleDad.aggiornaDescrizione(rendicontoRichiesta.getRichiestaEconomale());
		
		res.setRendicontoRichiesta(rendicontoRichiesta);
		
		gestisciRegistrazioneGEN();
	}

	/**
	 * N.B. se gli importi 'integrato' e 'restituito' sono valorizzati (diversi da zero), 
	 * quindi c'&egrave; un'entrata (importo restituito) o un'uscita (importo integrato), 
	 * occorre attribuire un numero di movimento alla richiesta (Movimento.numeromovimento) 
	 * e la data operazione (Movimento.data movimento).
	 * 
	 */
	private void creaMovimentoSePrevisto() {
		if(Boolean.TRUE.equals(isNecessarioMovimento)) {
			StatoOperativoRichiestaEconomale statoOperativoOriginale = richiestaEconomaleDad.findStatoOperativo(rendicontoRichiesta.getRichiestaEconomale().getUid());
			CassaEconomale cassaEconomale = rendicontoRichiestaEconomaleDad.findCassaEconomaleByRendicontoRichiesta(rendicontoRichiesta);
			
			if(cassaEconomale == null) {
				throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("la richiesta economale associata al rendiconto non risulta associata ad alcuna cassa economale"));
			}
			
			if(StatoOperativoRichiestaEconomale.DA_RENDICONTARE.equals(statoOperativoOriginale)){
				rendicontoRichiesta.getMovimento().setNumeroMovimento(richiestaEconomaleDad.staccaNumeroMovimento(annoCorrente, cassaEconomale));
			}
			if(rendicontoRichiesta.getMovimento().getNumeroMovimento() == null){
				//throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero movimento"));
				rendicontoRichiesta.getMovimento().setNumeroMovimento(richiestaEconomaleDad.staccaNumeroMovimento(annoCorrente, cassaEconomale));
			}
		}
		
	}
	
}
