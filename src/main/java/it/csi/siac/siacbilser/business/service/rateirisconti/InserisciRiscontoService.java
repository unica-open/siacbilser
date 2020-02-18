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
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaAutomaticaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRisconto;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRiscontoResponse;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;

/**
 * 
 * Inserisce un Risconto. 
 * Salva una prima nota nell'esercizio corrente. Quelle sull'esercizio futuro sono demandate ad una funzione esterna.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciRiscontoService extends CheckedAccountBaseService<InserisciRisconto, InserisciRiscontoResponse> {

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
		checkNotNull(req.getRisconto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("risconto"));
		checkNotNull(req.getRisconto().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno risconto"));
		checkEntita(req.getRisconto().getPrimaNota(), "prima nota risconto");
		this.primaNota = req.getRisconto().getPrimaNota();
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
	public InserisciRiscontoResponse executeService(InserisciRisconto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	
	@Override
	protected void execute() {
		String methodName = "execute";
		
//		checkRendicontoNonApprovato();
		
		caricaPrimaNota();
		caricaRegistrazione();
		
		checkStatoPrimaNotaDefinitivo();
		checkAnnoRisconto();
		
		primaNotaDad.inserisciRisconto(req.getRisconto());
		res.setRisconto(req.getRisconto());
		
		//Inserimento prima nota nell'anno corrente.
		registrazioneGENServiceHelper.initBilancio(primaNota.getBilancio());
		Evento eventoAnnoCorrente = registrazioneGENServiceHelper.determinaEventoRisconto(registrazioneMovFinDiPartenza.getEvento().getTipoCollegamento(), true, true);
		RegistrazioneMovFin registrazioneMovFin2 = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(eventoAnnoCorrente, 
				/*registrazioneMovFinDiPartenza.getMovimento()*/req.getRisconto(), registrazioneMovFinDiPartenza.getElementoPianoDeiContiAggiornato(), primaNota.getAmbito() /*Ambito.AMBITO_FIN*/);
		registrazioneGENServiceHelper.flushAndClear();
		InseriscePrimaNotaAutomaticaResponse resIPNA2 = registrazioneGENServiceHelper.inserisciPrimaNotaAutomatica(registrazioneMovFin2);
		PrimaNota primaNotaAnnoCorrente = resIPNA2.getPrimaNota();
		log.debug(methodName, "Prima nota anno corrente inserita. Uid: "+primaNotaAnnoCorrente.getUid());
		TipoRelazionePrimaNota tipoRelazionePrimaNota = primaNotaDad.findTipoRelazioneByCodice(TipoRelazionePrimaNota.CODICE_RISCONTO);
		primaNotaAnnoCorrente.setTipoRelazionePrimaNota(tipoRelazionePrimaNota);
		primaNotaDad.collegaPrimeNote(primaNota, primaNotaAnnoCorrente);
		
	}
	
	private void checkStatoPrimaNotaDefinitivo() {
		if(!StatoOperativoPrimaNota.DEFINITIVO.equals(primaNota.getStatoOperativoPrimaNota())){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("La prima nota e' in stato ANNULLATO."));
		}
	}

	
	/**
	 * Check anno Risconto > anno Prima Nota
	 */
	private void checkAnnoRisconto() {
		if(req.getRisconto().getAnno()<=primaNota.getBilancio().getAnno()){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("L'anno del Risconto deve essere maggiore dell'anno della prima nota di partenza."));
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
			throw new BusinessException("Impegno e/o Accertamento non caricato.");
		}
		
	}

	private void caricaPrimaNota() {
		PrimaNota primaNotaTrovata = primaNotaDad.findPrimaNotaByUid(primaNota.getUid());
		if(primaNotaTrovata == null){
			throw new BusinessException("Impossibile trovare la primaNota con uid: "+primaNota.getUid());
		}
		this.primaNota = primaNotaTrovata;
		
	}

}
