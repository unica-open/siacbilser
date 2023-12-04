/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.gestione.RipristinaAccantonamentoFondiDubbiaEsigibilitaGestioneService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.previsione.RipristinaAccantonamentoFondiDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.rendiconto.RipristinaAccantonamentoFondiDubbiaEsigibilitaRendicontoService;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.RipristinaAccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.RipristinaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.RipristinaAccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaDad;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommonser.business.service.base.BaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Salvataggio degli attributi FCDE per il bilancio
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioService extends CheckedAccountBaseService<SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio, SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse> {

	//DADs
	@Autowired private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
	@Autowired private AccantonamentoFondiDubbiaEsigibilitaDad accantonamentoFondiDubbiaEsigibilitaDad;
	@Autowired private BilancioDad bilancioDad;

	@Override
	@Transactional
	public SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse executeService(SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//imposto ente e login operazione nei dad
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.setLoginOperazione(loginOperazione);
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.setEnte(ente);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio");
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getBilancio(), "bilancio attributi bilancio", false);
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getTipoAccantonamentoFondiDubbiaEsigibilita(), "tipo accantonamento attributi bilancio", false);
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getStatoAccantonamentoFondiDubbiaEsigibilita(), "stato accantonamento attributi bilancio", false);
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getRiscossioneVirtuosa(), "riscossione virtuosa", false);
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getQuinquennioRiferimento(), "quinquennio riferimento", false);

		// Accantonamento graduale
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getAccantonamentoGraduale(), "accantonamento graduale");
		
		// solo RENDICONTO e PREVISIONE possono avere un accantonamento graduale, compreso tra 0 e 100
		if(!TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE.equals(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getTipoAccantonamentoFondiDubbiaEsigibilita())) {
			checkCondition(BigDecimal.ZERO.compareTo(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getAccantonamentoGraduale()) <= 0 && 
					BilUtilities.BIG_DECIMAL_ONE_HUNDRED.compareTo(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getAccantonamentoGraduale()) >= 0,
					ErroreCore.VALORE_NON_CONSENTITO.getErrore("accantonamento graduale", "deve essere compreso tra 0 e 100"), false);
		} else {
			checkCondition(BilUtilities.BIG_DECIMAL_ONE_HUNDRED.compareTo(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getAccantonamentoGraduale()) == 0,
					ErroreCore.VALORE_NON_CONSENTITO.getErrore("accantonamento graduale per la Gestione", "deve essere 100"), false);
		}
		
	}
	
	@Override
	protected void execute() {
		// Controllo quinquennio pari a anno bilancio-1 o anno bilancio-2
		checkQuinquennioRiferimento();
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
		if(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid() == 0) {
			// Check non esistenza
			accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = inserisciAccantonamento();
		} else {
			// Check esistenza per uid
			accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = aggiornaAccantonamento();
		}
		res.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
	}

	/**
	 * Aggiornamento dell'accantonamento
	 * @return l'accantonamento aggiornato
	 */
	private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio aggiornaAccantonamento() {
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio current = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findById(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid(),
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Stato,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
		checkBusinessCondition(current != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("attributi bilancio", "uid " + req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid()));
		// Bilancio, tipo, stato e versione non possono cambiare
		checkBusinessCondition(current.getBilancio() != null && current.getBilancio().getUid() == req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getBilancio().getUid(), ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile modificare il bilancio di riferimento"));
		checkBusinessCondition(current.getTipoAccantonamentoFondiDubbiaEsigibilita() != null && current.getTipoAccantonamentoFondiDubbiaEsigibilita().equals(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getTipoAccantonamentoFondiDubbiaEsigibilita()), ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile modificare il tipo di fondo"));
		checkBusinessCondition(current.getStatoAccantonamentoFondiDubbiaEsigibilita() != null && current.getStatoAccantonamentoFondiDubbiaEsigibilita().equals(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getStatoAccantonamentoFondiDubbiaEsigibilita()), ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile modificare lo stato del fondo"));

		// Forzo la parita' della versione
		req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setVersione(current.getVersione());

		// Aggiornamento
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.update(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());

		// Reset accantonamenti
		resetAccantonamenti(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), current);

		return accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
	}
	
	/**
	 * Reset di tutti gli accantonamenti per il dato bilancio e tipo.
	 * <br/>
	 * Tale reset sar&agarave; effettuato solo per i dati che impattano i calcoli FCDE (non i dati stampa)
	 * @param newInstance il valore da salvare
	 * @param currentInstance il valore attuale su base dati
	 */
	private void resetAccantonamenti(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio newInstance, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio currentInstance) {
		// Se cambio dei dati che comportano logche sui dati, elimino tutti i capitoli
		if(areDistinct(newInstance.getAccantonamentoGraduale(), currentInstance.getAccantonamentoGraduale())
				|| areDistinct(newInstance.getQuinquennioRiferimento(), currentInstance.getQuinquennioRiferimento())
				|| areDistinct(newInstance.getRiscossioneVirtuosa(), currentInstance.getRiscossioneVirtuosa())) {
			List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> listaAccantonamentoFondiDubbiaEsigibilita = accantonamentoFondiDubbiaEsigibilitaDad.ricercaAccantonamentoFondiDubbiaEsigibilita(currentInstance);
			
			// Proseguo con i ripristini solo se ho effetteivamente una lista di accaontamneti da ripristinare
			if(CollectionUtils.isNotEmpty(listaAccantonamentoFondiDubbiaEsigibilita)) {
				
				// FIXME: possibile rendere piu' dinamico?
				switch(newInstance.getTipoAccantonamentoFondiDubbiaEsigibilita()) {
					case PREVISIONE:
						RipristinaAccantonamentoFondiDubbiaEsigibilita reqPrevisione = new RipristinaAccantonamentoFondiDubbiaEsigibilita();
						reqPrevisione.setListaAccantonamentoFondiDubbiaEsigibilita(CollectionUtil.extractByType(listaAccantonamentoFondiDubbiaEsigibilita, AccantonamentoFondiDubbiaEsigibilita.class));
						reqPrevisione.setSkipLoadAccantonamenti(Boolean.TRUE);
						executeRipristino(reqPrevisione, RipristinaAccantonamentoFondiDubbiaEsigibilitaService.class);
						break;
					case RENDICONTO:
						RipristinaAccantonamentoFondiDubbiaEsigibilitaRendiconto reqRendiconto = new RipristinaAccantonamentoFondiDubbiaEsigibilitaRendiconto();
						reqRendiconto.setListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(CollectionUtil.extractByType(listaAccantonamentoFondiDubbiaEsigibilita, AccantonamentoFondiDubbiaEsigibilitaRendiconto.class));
						reqRendiconto.setSkipLoadAccantonamenti(Boolean.TRUE);
						executeRipristino(reqRendiconto, RipristinaAccantonamentoFondiDubbiaEsigibilitaRendicontoService.class);
						break;
					case GESTIONE:
						RipristinaAccantonamentoFondiDubbiaEsigibilitaGestione reqGestione = new RipristinaAccantonamentoFondiDubbiaEsigibilitaGestione();
						reqGestione.setListaAccantonamentoFondiDubbiaEsigibilitaGestione(CollectionUtil.extractByType(listaAccantonamentoFondiDubbiaEsigibilita, AccantonamentoFondiDubbiaEsigibilitaGestione.class));
						reqGestione.setSkipLoadAccantonamenti(Boolean.TRUE);
						executeRipristino(reqGestione, RipristinaAccantonamentoFondiDubbiaEsigibilitaGestioneService.class);
						break;
					default:
						throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Tipo accantonamento non censito: " + newInstance.getTipoAccantonamentoFondiDubbiaEsigibilita()));
				}
			}
		}
	}
	
	private boolean areDistinct(Object a, Object b) {
		return (a == null && b != null)
				|| (a != null && b == null)
				|| (a != null && b != null && !a.equals(b));
	}
	private boolean areDistinct(BigDecimal a, BigDecimal b) {
		return (a == null && b != null)
				|| (a != null && b == null)
				|| (a != null && b != null && a.compareTo(b) != 0);
	}
	private <EREQ extends ServiceRequest, ERES extends ServiceResponse, ESER extends BaseService<EREQ, ERES>> void executeRipristino(EREQ requestRipristino, Class<ESER> serviceClass) {
		requestRipristino.setAnnoBilancio(req.getAnnoBilancio());
		requestRipristino.setDataOra(new Date());
		requestRipristino.setRichiedente(req.getRichiedente());
		serviceExecutor.executeServiceSuccess(serviceClass, requestRipristino);
	}

	private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio inserisciAccantonamento() {
		// Stacco versione
		Integer versione = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.staccaVersione(
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getBilancio(),
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getTipoAccantonamentoFondiDubbiaEsigibilita());
		req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setVersione(versione);
		
		// Salvataggio
		return accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.create(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
	}

	/**
	 * Il quinquennio di riferimento pu&ograve; essere pari all'anno di bilancio - 1 oppure all'anno di bilancio - 2
	 */
	private void checkQuinquennioRiferimento(int lowerBound, int upperBound) {
		int quinquennioRiferimento = req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getQuinquennioRiferimento().intValue();
		checkBusinessCondition(quinquennioRiferimento >= lowerBound && quinquennioRiferimento <= upperBound, ErroreCore.VALORE_NON_CONSENTITO.getErrore("quinquennio riferimento", "deve essere compreso tra " + lowerBound + " e " + upperBound));
	}

	/**
	 * Il quinquennio di riferimento pu&ograve; essere pari all'anno di bilancio - 1 oppure all'anno di bilancio - 2 per la Gestione e la Previsione,
	 * mentre per il Rendicono pari all'anno di bilancio - 0 oppure all'anno di bilancio - 2 
	 */
	//SIAC-8681
	private void checkQuinquennioRiferimento() {
		Bilancio bilancio = bilancioDad.getBilancioByUid(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getBilancio().getUid());
		checkBusinessCondition(bilancio != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Bilancio", "uid " + req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getBilancio().getUid()));
		
		switch (req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getTipoAccantonamentoFondiDubbiaEsigibilita()) {
			case PREVISIONE:
				checkQuinquennioRiferimento(bilancio.getAnno() - 2, bilancio.getAnno() - 1);
				break;
			case GESTIONE:
				//SIAC-8754
				checkQuinquennioRiferimento(bilancio.getAnno(), bilancio.getAnno());
				break;
			case RENDICONTO:
				checkQuinquennioRiferimento(bilancio.getAnno() - 2, bilancio.getAnno() - 0);
				break;
			default:
				break;
		}

	}
	
}
