/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * The Class AggiornaRichiestaEconomaleService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class AggiornaRichiestaEconomaleService extends InserisceAggiornaRichiestaEconomaleService<AggiornaRichiestaEconomale, AggiornaRichiestaEconomaleResponse> {
	
	protected Integer annoCorrente;
	protected StatoOperativoRichiestaEconomale statoOperativoOriginale;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		this.richiestaEconomale = req.getRichiestaEconomale();
		checkEntita(richiestaEconomale, "richiesta economale");
		
		richiestaEconomale.setEnte(ente);
		
		checkNotNull(richiestaEconomale.getNumeroRichiesta(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero richiesta economale"));
		
		checkEntita(richiestaEconomale.getBilancio(), "bilancio richiesta economale");
		checkEntita(richiestaEconomale.getCassaEconomale(), "cassa richiesta economale");
		checkEntita(richiestaEconomale.getImpegno(), "impegnoo richiesta economale");
		
		checkServiceParamRichiestaEconomale();
		
		//se e' valorizzato il movimento devono essere presenti anche i dati obbligatori del movimento.
		if(richiestaEconomale.getMovimento() != null){
			checkNotNull(richiestaEconomale.getMovimento().getDataMovimento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data movimento del movimento richiesta eoconomale"));
			checkEntita(richiestaEconomale.getMovimento().getModalitaPagamentoDipendente(), "modalita pagamento dipendente del movimento richiesta eoconomale");
			checkEntita(richiestaEconomale.getMovimento().getModalitaPagamentoCassa(), "modalita pagamento cassa del movimento richiesta eoconomale");
		}
		
		
		datiEconomoPresenti = richiestaEconomale.getMovimento() != null;
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		annoCorrente = now.get(Calendar.YEAR);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaRichiestaEconomaleResponse executeService(AggiornaRichiestaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		//checkA()
		//checkB()
		statoOperativoOriginale = richiestaEconomaleDad.findStatoOperativo(richiestaEconomale.getUid());
		checkRichiestaAggiornabile();
		
		checkRichiestaEconomale();
		checkDettaglioPagamento();
		
		richiestaEconomale.setCaricataDaProceduraEsterna(Boolean.FALSE);
		if(Boolean.TRUE.equals(datiEconomoPresenti)){
			creaMovimento();
		}
		determinaStato();
		caricaTipoRichiestaDaCodice();
		
		caricaImpegnoOSubImpegno();
		preAggiornamentoRichiestaEconomale();
		
		richiestaEconomaleDad.aggiornaRichiestaEconomale(richiestaEconomale);
		postAggiornamentoRichiestaEconomale();
		
		res.setRichiestaEconomale(richiestaEconomale);
		
		gestisciRegistrazioneGEN();
	}

	/**
	 * Una richiesta &eacute; aggiornabile se:
	 * <ul>
	 *     <li>non &eacute; in stato <code>ANNULLATO</code></li>
	 *     <li>
	 *         Se la richiesta &eacute; presente in una stampa rendiconto visualizzare il messaggio <code>&lt;COR_ERR_0044, Operazione non consentita
	 *         (messaggio: 'La richiesta &eacute; presente nel rendiconto N. ' + numero rendiconto)&gt;</code> e non consentire l'aggiornamento.
	 *     </li>
	 * </ul>
	 */
	private void checkRichiestaAggiornabile() {
		if(StatoOperativoRichiestaEconomale.ANNULLATA.equals(statoOperativoOriginale)){
			throw new BusinessException(ErroreCEC.CEC_ERR_0006.getErrore());
		}
		// TODO: aggiungere controllo stampa rendiconto
	}

	private void creaMovimento() {
		log.debug("crea movimento: ", statoOperativoOriginale.getDescrizione());
		if(StatoOperativoRichiestaEconomale.PRENOTATA.equals(statoOperativoOriginale)){
			richiestaEconomale.getMovimento().setNumeroMovimento(richiestaEconomaleDad.staccaNumeroMovimento(annoCorrente, richiestaEconomale.getCassaEconomale()));
		}
		if(richiestaEconomale.getMovimento().getNumeroMovimento() == null){
			throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero movimento"));
		}
	}
	
	/**
	 * Da sovrascrivere per specificare delle azioni prima dell'aggiornamento della richiesta economale.
	 */
	protected void preAggiornamentoRichiestaEconomale() {
		// Da implementare nella sottoclasse se necessario
	}
	
	/**
	 * Da sovrascrivere per specificare delle azioni dopo l'aggiornamento della richiesta economale.
	 */
	protected void postAggiornamentoRichiestaEconomale() {
		// Da implementare nella sottoclasse se necessario
	}

	/* ############################################ Attivazione GEN ############################################ */
	
	@Override
	protected void annullaRegistrazioniGENGSAPrecedenti() {
		// La gestione dell'annullamento delle vecchie richieste e' da fare? Forse no. Se fosse scommentare queste due righe
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.RICHIESTA_ECONOMALE, richiestaEconomale); //se presenti ne troverà una per ogni quota, altrimenti 0.
		registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esisteva devo annullare le eventuali primeNote associate
	}

}
