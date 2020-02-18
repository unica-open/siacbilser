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
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class InserisceRendicontoRichiestaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceRendicontoRichiestaService extends InserisceAggiornaRendicontoRichiestaService<InserisceRendicontoRichiesta, InserisceRendicontoRichiestaResponse> {
		
	
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
//	//private Calendar now;
	private Integer annoBilancio;
//
//	private Boolean isNecessarioMovimento;
//	private Boolean isRestituzioneTotale;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.rendicontoRichiesta = req.getRendicontoRichiesta();
		checkNotNull(rendicontoRichiesta, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("rendiconto richiesta"));
		
		checkEntita(rendicontoRichiesta.getRichiestaEconomale(), "richiesta economale");
		checkNotNull(rendicontoRichiesta.getRichiestaEconomale().getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo anticipo"));
		
		checkNotNull(rendicontoRichiesta.getDataRendiconto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data rendiconto")); 
		
		checkNotNull(rendicontoRichiesta.getImportoIntegrato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo integrato"));
		checkCondition(rendicontoRichiesta.getImportoIntegrato().compareTo(BigDecimal.ZERO)>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo integrato (maggiore di zero) "));
		checkNotNull(rendicontoRichiesta.getImportoRestituito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo restituito"));
		checkCondition(rendicontoRichiesta.getImportoRestituito().compareTo(BigDecimal.ZERO)>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo restituito (maggiore di zero) "));
		
		this.isNecessarioMovimento  = rendicontoRichiesta.getImportoIntegrato().compareTo(BigDecimal.ZERO)>0 || 
				rendicontoRichiesta.getImportoRestituito().compareTo(BigDecimal.ZERO)>0;
		
		this.isRestituzioneTotale = rendicontoRichiesta.getImportoRestituito().compareTo(rendicontoRichiesta.getRichiestaEconomale().getImporto()) == 0;//FIXME l'importo della richiestaEconomale arriva dal frontend! va riletto da DB!
		
		if(Boolean.TRUE.equals(isNecessarioMovimento)){
			checkCondition(rendicontoRichiesta.getMovimento()!=null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("movimento"));
			checkNotNull(rendicontoRichiesta.getMovimento().getDataMovimento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data movimento"));
			checkEntita(rendicontoRichiesta.getMovimento().getModalitaPagamentoDipendente(), "modalita pagamento dipendente del movimento");
			checkEntita(rendicontoRichiesta.getMovimento().getModalitaPagamentoCassa(), "modalita pagamento cassa del movimento");
		}else{
			rendicontoRichiesta.setMovimento(null);
		}
		
		if(!Boolean.TRUE.equals(isRestituzioneTotale)){
//			checkCondition(rendicontoRichiesta.getGiustificativi()!=null && !rendicontoRichiesta.getGiustificativi().isEmpty(), 
//					ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("giustificativi rendiconto richiesta"));
			
			for(Giustificativo giustificativo : rendicontoRichiesta.getGiustificativi()){
				checkNotNull(giustificativo.getImportoGiustificativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo giustificativo"));
				checkCondition(giustificativo.getImportoGiustificativo() == null || giustificativo.getImportoGiustificativo().signum() > 0,
						ErroreCore.VALORE_NON_VALIDO.getErrore("Importo",": l'importo deve essere positivo"));
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
	@Transactional
	public InserisceRendicontoRichiestaResponse executeService(InserisceRendicontoRichiesta serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		caricaRichiestaEconomale();
		annoBilancio = rendicontoRichiesta.getRichiestaEconomale().getBilancio().getAnno();
		
		this.isRestituzioneTotale = rendicontoRichiesta.getImportoRestituito().compareTo(rendicontoRichiesta.getRichiestaEconomale().getImporto()) == 0;
		
		checkDettaglioPagamento();
		creaMovimentoSePrevisto();
		determinaStatoOperativoGiustificativi();
		rendicontoRichiestaEconomaleDad.inserisceRendicontoRichiestaEconomale(rendicontoRichiesta);
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
			CassaEconomale cassaEconomale = richiestaEconomaleDad.findCassaByRichiestaEconomale(rendicontoRichiesta.getRichiestaEconomale());
			Integer numeroMovimento = richiestaEconomaleDad.staccaNumeroMovimento(annoBilancio, cassaEconomale);
			rendicontoRichiesta.getMovimento().setNumeroMovimento(numeroMovimento);
		}
		
	}
	
}
