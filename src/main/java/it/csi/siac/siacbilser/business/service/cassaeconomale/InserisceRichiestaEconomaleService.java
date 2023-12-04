/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.errore.TipoErrore;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * The Class InserisceRichiestaEconomaleService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class InserisceRichiestaEconomaleService extends InserisceAggiornaRichiestaEconomaleService<InserisceRichiestaEconomale, InserisceRichiestaEconomaleResponse> {
	
	@Autowired
	private BilancioDad bilancioDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		this.richiestaEconomale = req.getRichiestaEconomale();
		checkNotNull(richiestaEconomale, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiesta economale"));
		
		checkEntita(richiestaEconomale.getBilancio(), "bilancio richiesta economale");
		checkEntita(richiestaEconomale.getCassaEconomale(), "cassa richiesta economale");
		checkEntita(richiestaEconomale.getImpegno(), "impegno richiesta economale");
		
		checkServiceParamRichiestaEconomale();
		
		//se e' valorizzato il movimento devono essere presenti anche i dati obbligatori del movimento.
		if(richiestaEconomale.getMovimento() != null){
			checkNotNull(richiestaEconomale.getMovimento().getDataMovimento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data movimento del movimento richiesta eoconomale"));
			checkEntita(richiestaEconomale.getMovimento().getModalitaPagamentoDipendente(), "modalita pagamento dipendente del movimento richiesta eoconomale");
			//checkCondition(StringUtils.isNotBlank(richiestaEconomale.getMovimento().getDettaglioPagamento()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettaglio pagamento movimento richiesta eoconomale"));
			checkEntita(richiestaEconomale.getMovimento().getModalitaPagamentoCassa(), "modalita pagamento cassa del movimento richiesta eoconomale");
		}
		
		
		datiEconomoPresenti = richiestaEconomale.getMovimento() != null;
		if(!datiEconomoPresenti){
			richiestaEconomale.setMovimento(null);
		}
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public InserisceRichiestaEconomaleResponse executeService(InserisceRichiestaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		Bilancio bilancio = bilancioDad.getBilancioByUid(richiestaEconomale.getBilancio().getUid());
		annoBilancio = bilancio.getAnno();
//		checkPresenzaImpegno();
		checkPresenzaCassaEconomale();
		
		checkRichiestaEconomale();
		
		checkDettaglioPagamento();
		
		Integer numeroRichiestaEconomale = richiestaEconomaleDad.staccaNumeroRichiestaEconomale(annoBilancio, richiestaEconomale.getCassaEconomale().getUid());
		richiestaEconomale.setNumeroRichiesta(numeroRichiestaEconomale);
		richiestaEconomale.setCaricataDaProceduraEsterna(Boolean.FALSE);
		
		if(Boolean.TRUE.equals(datiEconomoPresenti)){
			popolaNumeroMovimento();
		}
		caricaTipoRichiestaDaCodice();
		preInserisceRichiestaEconomale();
		determinaStato();
		
		richiestaEconomaleDad.inserisceRichiestaEconomale(richiestaEconomale);
		postInserisceRichiestaEconomale();
		
		res.setRichiestaEconomale(richiestaEconomale);
		
		gestisciRegistrazioneGEN();
	}

	/**
	 * Da sovrascrivere per specificare delle azioni prima dell'inseriemeno della richiesta economale.
	 */
	protected void preInserisceRichiestaEconomale() {
		// Da implementare nella sottoclasse se necessario
	}
	
	/**
	 * Da sovrascrivere per specificare delle azioni prima dell'inseriemeno della richiesta economale.
	 */
	protected void postInserisceRichiestaEconomale() {
		// Da implementare nella sottoclasse se necessario
	}

	/**
	 * Nel caso in cui sia compilata la sezione dell'economo (data operazione e modalita' pagamento),
	 * viene creato un movimento con numero progressivo da associare alla richiesta economale.
	 */
	private void popolaNumeroMovimento() {
		
		Movimento movimento = richiestaEconomale.getMovimento();
		movimento.setNumeroMovimento(richiestaEconomaleDad.staccaNumeroMovimento(annoBilancio, richiestaEconomale.getCassaEconomale()));
		
	}
	
	private void checkPresenzaCassaEconomale() {
		if(!cassaEconomaleDad.cassaEconomaleEsistente(richiestaEconomale.getCassaEconomale().getUid())){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("cassa economale con uid", richiestaEconomale.getCassaEconomale().getUid()));
		}
	}

	protected void checkPresenzaSoggetto() {
		if(richiestaEconomale.getSoggetto() != null && !soggettoDad.soggettoEsistente(richiestaEconomale.getSoggetto().getUid())){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("soggetto con uid", richiestaEconomale.getSoggetto().getUid()));
		}
	}
	
	
	protected void checkImpegno() {
		caricaImpegnoOSubImpegno();
		checkFormaliImpegno(impegnoOSubImpegno);
	}

	/**
	 * Controllo delle condizioni di validita dell'impegno.
	 * <br/>
	 * <ul>
	 *     <li>
	 *         L'impegno o il subimpegno selezionato deve essere in stato <code>DEFINITIVO</code> (<code>D</code>), altrimenti viene visualizzato il messaggio
	 *         <code>&lt;FIN_ERR_0096, Impegno non in stato Definitivo&gt;</code> o <code>&lt;FIN_ERR_0098, SubImpegno non in stato Definitivo&gt;</code>;
	 *     </li>
	 *     <li>
	 *         La disponibilit&agrave; dell'impegno deve essere maggiore di 0, altrimenti visualizzare il seguente messaggio
	 *         <code>COR_ERR_0044 - Operazione non consentita (messaggio: 'Non vi &eacute; disponibilit&agrave; sull'impegno')&gt;</code>.
	 *         <br/>
	 *         Se il parametro definito in fase di configurazione della cassa <code>CassaEconomale.limiteImporto</code> &eacute; stato valorizzato (diverso da 0),
	 *         occorre controllare che la disponibilit&agrave; dell'impegno sia maggiore; altrimenti visualizzare il seguente messaggio
	 *         <code>COR_ERR_0044 - Operazione non consentita (messaggio: 'La disponibilit&agrave; dell'impegno &eacute; inferiore al limite stabilito in fase di configurazione')&gt;</code>;
	 *     </li>
	 *     <li>
	 *         L'importo della richiesta da pagare deve essere inferiore o uguale alla disponibilit&agrave; dell'impegno; altrimenti visualizzare il seguente messaggio
	 *         <code>&gt;COR_ERR_0044 - Operazione non consentita (messaggio: 'Non vi &eacute; disponibilit&agrave; sull'impegno per pagare la richiesta')&gt;</code>
	 *     </li>
	 * </ul>
	 * 
	 * @param impegno              l'impegno da validare
	 */
	protected void checkFormaliImpegno(Impegno impegno) {
		
		TipoErrore tipoErroreDefinitivo;
		String sulStr;
		String delStr;
		if(impegnoOSubImpegno instanceof SubImpegno){
			tipoErroreDefinitivo = ErroreFin.SUBIMPEGNO_NON_IN_STATO_DEFINITIVO;
			sulStr = "sul subimpegno";
			delStr = "del subimpegno";
		} else {
			tipoErroreDefinitivo = ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO;
			sulStr = "sull'impegno";
			delStr = "dell'impegno";
		}
		
		BigDecimal limiteImportoCassaEconomale = cassaEconomaleDad.ottieniLimiteImportoCassaEconomale(richiestaEconomale.getCassaEconomale().getUid());
		// SIAC-4746: la disponibilita' da usare e' quella a liquidare
		BigDecimal disponibilita = impegno.getDisponibilitaLiquidare();
		
		// Stato definitivo
		if(!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa())) {
			throw new BusinessException(tipoErroreDefinitivo.getErrore());
		}
		// Disponibilita' > 0
		if(disponibilita == null || disponibilita.signum() <= 0) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Non vi e' disponibilita' " + sulStr));
		}
		// Se limite importo ==> disponibilita' > limite importo
		if(limiteImportoCassaEconomale != null && limiteImportoCassaEconomale.compareTo(disponibilita) >= 0) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("La disponibilita' " + delStr + " e' inferiore al limite stabilito in fase di configurazione"));
		}
		// Importo richiesta <= disponibilita'
		if(richiestaEconomale.getImporto().compareTo(disponibilita) > 0) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Non vi e' disponibilita' " + sulStr + " per pagare la richiesta"));
		}
	}
	
}
