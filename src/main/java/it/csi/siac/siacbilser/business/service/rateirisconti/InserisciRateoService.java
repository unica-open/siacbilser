/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.rateirisconti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaAutomaticaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRateo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRateoResponse;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;

/**
 * 
 * Inserisce un Rateo. 
 * Il rateo ha lo scopo di produrre una prima nota in un esercizio precedente, 
 * rispetto all'esercizio del movimento finanziario (impegno/accertamento) a cui la prima nota afferisce.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciRateoService extends CheckedAccountBaseService<InserisciRateo, InserisciRateoResponse> {

	//DADs...
	@Autowired
	private PrimaNotaDad primaNotaDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private RegistrazioneMovFinDad registrazioneMovFinDad;
	
	//Components..
	@Autowired
	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;

	//Fields..
	private PrimaNota primaNota;
	private RegistrazioneMovFin registrazioneMovFinDiPartenza;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRateo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("rateo"));
		checkNotNull(req.getRateo().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno rateo"));
		checkEntita(req.getRateo().getPrimaNota(), "prima nota rateo");
		this.primaNota = req.getRateo().getPrimaNota();
	}
	
	@Override
	protected void init() {
		primaNotaDad.setLoginOperazione(loginOperazione);
		primaNotaDad.setEnte(ente);
		
		bilancioDad.setEnteEntity(ente);
		
		registrazioneMovFinDad.setLoginOperazione(loginOperazione);
		registrazioneMovFinDad.setEnte(ente);
		
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione);
		
	}
	
	@Transactional
	public InserisciRateoResponse executeService(InserisciRateo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	
	@Override
	protected void execute() {
		String methodName = "execute";
		checkRendicontoNonApprovato();
		
		caricaPrimaNota();
		caricaRegistrazione();
		
		checkStatoPrimaNotaDefinitivo();
		checkAnnoRateo();
		
		
		primaNotaDad.inserisciRateo(req.getRateo());
		res.setRateo(req.getRateo());
		
		//Inserimento prima nota nell'anno del rateo.
		registrazioneGENServiceHelper.initBilancio(req.getRateo().getAnno());
		Evento eventoAnnoRateo = registrazioneGENServiceHelper.determinaEventoRateo(registrazioneMovFinDiPartenza.getEvento().getTipoCollegamento(), true, false);
		RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(eventoAnnoRateo, 
				/*registrazioneMovFinDiPartenza.getMovimento()*/req.getRateo(), registrazioneMovFinDiPartenza.getElementoPianoDeiContiAggiornato(), primaNota.getAmbito() /*Ambito.AMBITO_FIN*/);
		registrazioneGENServiceHelper.flushAndClear();
		InseriscePrimaNotaAutomaticaResponse resIPNA = registrazioneGENServiceHelper.inserisciPrimaNotaAutomatica(registrazioneMovFin);
		PrimaNota primaNotaAnnoRateo = resIPNA.getPrimaNota();
		log.debug(methodName, "Prima nota anno rateo inserita. Uid: "+primaNotaAnnoRateo.getUid());
		TipoRelazionePrimaNota tipoRelazionePrimaNota = primaNotaDad.findTipoRelazioneByCodice(TipoRelazionePrimaNota.CODICE_RATEO);
		primaNotaAnnoRateo.setTipoRelazionePrimaNota(tipoRelazionePrimaNota);
		primaNotaDad.collegaPrimeNote(primaNota, primaNotaAnnoRateo);
		
		//Inserimento prima nota nell'anno corrente.
		registrazioneGENServiceHelper.initBilancio(primaNota.getBilancio());
		Evento eventoAnnoCorrente = registrazioneGENServiceHelper.determinaEventoRateo(registrazioneMovFinDiPartenza.getEvento().getTipoCollegamento(), true, true);
		RegistrazioneMovFin registrazioneMovFin2 = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(eventoAnnoCorrente, 
				/*registrazioneMovFinDiPartenza.getMovimento()*/req.getRateo(), registrazioneMovFinDiPartenza.getElementoPianoDeiContiAggiornato(), primaNota.getAmbito() /*Ambito.AMBITO_FIN*/);
		registrazioneGENServiceHelper.flushAndClear();
		InseriscePrimaNotaAutomaticaResponse resIPNA2 = registrazioneGENServiceHelper.inserisciPrimaNotaAutomatica(registrazioneMovFin2);
		PrimaNota primaNotaAnnoCorrente = resIPNA2.getPrimaNota();
		log.debug(methodName, "Prima nota anno corrente inserita. Uid: "+primaNotaAnnoCorrente.getUid());
		primaNotaAnnoCorrente.setTipoRelazionePrimaNota(tipoRelazionePrimaNota);
		primaNotaDad.collegaPrimeNote(primaNota, primaNotaAnnoCorrente);
		
	}

	private void checkStatoPrimaNotaDefinitivo() {
		if(!StatoOperativoPrimaNota.DEFINITIVO.equals(primaNota.getStatoOperativoPrimaNota())){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("La prima nota non e' in stato DEFINITIVO."));
		}
	}

	/**
	 * Check anno Rateo < anno Prima Nota
	 */
	private void checkAnnoRateo() {
		if(req.getRateo().getAnno()>=primaNota.getBilancio().getAnno()){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("L'anno del Rateo deve essere minore dell'anno della prima nota di partenza."));
		}
	}


	private void caricaRegistrazione() {
		
		//Lo prende dalla prima nota caricata precedentemente.
		for(MovimentoEP movimentoEP : primaNota.getListaMovimentiEP()){
			
			if(movimentoEP.getRegistrazioneMovFin() == null) { //caso di prime note libere.
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("La prima nota non e' associata ad una registrazione."));
			}
			
			this.registrazioneMovFinDiPartenza = registrazioneMovFinDad.findRegistrazioneMovFinById(movimentoEP.getRegistrazioneMovFin().getUid());
			
		}
		
		if(this.registrazioneMovFinDiPartenza.getMovimento() == null){
			throw new BusinessException("Movimento della registrazione non caricato.");
		}
		
	}

	private void caricaPrimaNota() {
		PrimaNota primaNotaTrovata = primaNotaDad.findPrimaNotaByUid(primaNota.getUid());
		if(primaNotaTrovata == null){
			throw new BusinessException("Impossibile trovare la primaNota con uid: "+primaNota.getUid());
		}
		this.primaNota = primaNotaTrovata;
		
	}

	/**
	 * L'azione 'Ratei' deve essere consentita solo se per l'anno precedente non 
	 * è ancora stato approvato il rendiconto, e quindi l'esercizio è ancora aperto ed 
	 * è possibile aggiornare le componenti di conto econimico e conto del patrimonio dell'esercizio
	 */
	private void checkRendicontoNonApprovato() {
		Bilancio bilancioAnnoRateo = bilancioDad.getBilancioByAnno(req.getRateo().getAnno());
		FaseEStatoAttualeBilancio faseEStatoAttualeBilancio = bilancioAnnoRateo.getFaseEStatoAttualeBilancio();
		if(FaseBilancio.CHIUSO.equals(faseEStatoAttualeBilancio.getFaseBilancio())){
			// TODO: Operazioni da effettuare?
		}
		
	}

}
