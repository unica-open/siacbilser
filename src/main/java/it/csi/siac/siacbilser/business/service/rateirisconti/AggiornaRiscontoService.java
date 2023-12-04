/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.rateirisconti;

import java.util.List;

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
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.comparator.ComparatorUtil;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRisconto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRiscontoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaAutomaticaResponse;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RateoRisconto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;

/**
 * Aggiorna un Risconto.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaRiscontoService extends CheckedAccountBaseService<AggiornaRisconto, AggiornaRiscontoResponse> {

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
		checkEntita(req.getRisconto(), "risconto");
		checkEntita(req.getRisconto().getPrimaNota(), "prima nota risconto");
		checkNotNull(req.getRisconto().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno risconto"));
		
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
	public AggiornaRiscontoResponse executeService(AggiornaRisconto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	
	@Override
	protected void execute() {
		
		//carico i dati della prima nota papa'
		caricaPrimaNota();
		//carico i dati della prima nota vecchia
		caricaRegistrazione();
		
		//controllo lo stato della prima nota
		checkStatoPrimaNotaDefinitivo();
		
		//controllo la coerenza dell'anno del risconto
		checkAnnoRisconto();
		
		//recupero i dati del vecchio risconto
		RateoRisconto rateoRiscontoPrecedente = getRateoRiscontoPrecedente();
		
		//annullo la prima nota collegata al vecchio risconto
		annullaPrimaNotaRiscontoCollegata(rateoRiscontoPrecedente);
		
		primaNotaDad.aggiornaRisconto(req.getRisconto());
		
		//tolgo il vecchio risconto
		RateoRisconto rrDaTogliere = ComparatorUtil.searchByUid(primaNota.getListaRateoRisconto(), rateoRiscontoPrecedente);
		primaNota.getListaRateoRisconto().remove(rrDaTogliere);
		primaNota.addRateoRisconto(req.getRisconto());
		
		res.setRisconto(req.getRisconto());
		
		registrazioneGENServiceHelper.initBilancio(primaNota.getBilancio());
		
		//Inserimento prima nota nell'anno corrente.
		RegistrazioneMovFin registrazioneMovFinDaNuovoRisconto = inserisciRegistrazioneMovFinRisconto();
		
		//inserisco la prima nota automatica di tipo risconto
		PrimaNota primaNotaAnnoCorrente = inserisciprimaNotaRisconto(registrazioneMovFinDaNuovoRisconto);
		
		//collego la prima nota appena inserita alla prima nota papa'
		collegaPrimaNotaRisconto(primaNotaAnnoCorrente);
		
	}

	/**
	 * Collega la prima nota di tipo risconto alla prima nota da cui il risconto deriva.
	 * @param primaNotaAnnoCorrente la prima nota di tipo risconto da collegare
	 */
	private void collegaPrimaNotaRisconto(PrimaNota primaNotaAnnoCorrente) {
		TipoRelazionePrimaNota tipoRelazionePrimaNota = primaNotaDad.findTipoRelazioneByCodice(TipoRelazionePrimaNota.CODICE_RISCONTO);
		primaNotaAnnoCorrente.setTipoRelazionePrimaNota(tipoRelazionePrimaNota);
		primaNotaDad.collegaPrimeNote(primaNota, primaNotaAnnoCorrente);
	}

	/**
	 * Inserisce la prima nota di tipo risconto a partire dalla registrazione passata in input
	 * 
	 * @param registrazioneMovFinDaNuovoRisconto la registrazione da cui fare la prima nota
	 * @return la prima nota inserita
	 */
	private PrimaNota inserisciprimaNotaRisconto(RegistrazioneMovFin registrazioneMovFinDaNuovoRisconto) {
		final String methodName = "inserisciprimaNotaRisconto";
		InseriscePrimaNotaAutomaticaResponse resIPNA2 = registrazioneGENServiceHelper.inserisciPrimaNotaAutomatica(registrazioneMovFinDaNuovoRisconto);
		PrimaNota primaNotaAnnoCorrente = resIPNA2.getPrimaNota();
		log.debug(methodName, "Prima nota anno corrente inserita. Uid: "+primaNotaAnnoCorrente.getUid());
		return primaNotaAnnoCorrente;
	}

	/**
	 * Inserisce la registrazione mov fin di tipo risconto.
	 *
	 * @return the registrazione mov fin inserita
	 */
	private RegistrazioneMovFin inserisciRegistrazioneMovFinRisconto() {
		Evento eventoAnnoCorrente = registrazioneGENServiceHelper.determinaEventoRisconto(registrazioneMovFinDiPartenza.getEvento().getTipoCollegamento(), true, true);
		RegistrazioneMovFin registrazioneMovFin2 = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(eventoAnnoCorrente, 
				/*registrazioneMovFinDiPartenza.getMovimento()*/ req.getRisconto(), registrazioneMovFinDiPartenza.getElementoPianoDeiContiAggiornato(), Ambito.AMBITO_FIN);
		registrazioneGENServiceHelper.flushAndClear();
		return registrazioneMovFin2;
	}

	/**
	 * Annulla la prima nota creata a partire dal risconto.
	 * 
	 * @param rateoRiscontoPrecedente il risconto di cui annullare la registrazione
	 */
	private void annullaPrimaNotaRiscontoCollegata(RateoRisconto rateoRiscontoPrecedente) {
		final String methodName = "annullaPrimaNotaRiscontoCollegata";
		Integer idPrimaNotaRisconto = primaNotaDad.ottieniIdPrimaNotaRiscontoByRisconto(rateoRiscontoPrecedente);
		
		if(idPrimaNotaRisconto == null) {
			log.info(methodName, "non esiste nessuna nota di tipo risconto collegata al risconto con uid: " + rateoRiscontoPrecedente.getUid());
			return;
		}
		
		PrimaNota pn = new PrimaNota();
		pn.setUid(idPrimaNotaRisconto);
		
		PrimaNota primaNotaCollegata = ComparatorUtil.searchByUid(primaNota.getListaPrimaNotaFiglia(), pn);
		
		registrazioneGENServiceHelper.annullaPrimaNota(primaNotaCollegata);
		
		List<MovimentoEP> movimentiEP = primaNotaDad.findMovimentiEPByPrimaNota(primaNotaCollegata.getUid());
		for(MovimentoEP movimentoEP : movimentiEP){
			RegistrazioneMovFin registrazioneMovFinPrecedente = movimentoEP.getRegistrazioneMovFin();
			registrazioneMovFinDad.aggiornaStatoRegistrazioneMovFin(registrazioneMovFinPrecedente.getUid(), StatoOperativoRegistrazioneMovFin.ANNULLATO);
		}
	
	}
	
	/**
	 * Check stato prima nota definitivo.
	 */
	private void checkStatoPrimaNotaDefinitivo() {
		if(!StatoOperativoPrimaNota.DEFINITIVO.equals(primaNota.getStatoOperativoPrimaNota())){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("La prima nota e' in stato " 
					+ (primaNota.getStatoOperativoPrimaNota() != null? 
							primaNota.getStatoOperativoPrimaNota().getDescrizione()
							: "null")));
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
	
	/**
	 * Trova il rateo/risconto attualmente presente sulla base dati.
	 * @return
	 */
	private RateoRisconto getRateoRiscontoPrecedente() {
		for(RateoRisconto rateoRisconto : primaNota.getListaRateoRisconto()){
			if(req.getRisconto().getUid() == rateoRisconto.getUid()){
				return rateoRisconto;
			}
		}
		
		throw new IllegalStateException("Impossibile trovare il Risconto con uid: "+req.getRisconto().getUid());
	}
	
	
	/**
	 * Carica registrazione.
	 */
	private void caricaRegistrazione() {
		
		//Lo prende dalla prima nota caricata precedentemente.
		for(MovimentoEP movimentoEP : primaNota.getListaMovimentiEP()){
			
			if(movimentoEP.getRegistrazioneMovFin() == null) { //caso di prime note libere.
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("La prima nota non e' associata ad una registrazione."));
			}
			
			this.registrazioneMovFinDiPartenza = registrazioneMovFinDad.findRegistrazioneMovFinById(movimentoEP.getRegistrazioneMovFin().getUid());
		}
		
		if(this.registrazioneMovFinDiPartenza.getMovimento() == null){
			throw new BusinessException("Movimento non caricato.");
		}
		
	}

	/**
	 * Carica prima nota.
	 */
	private void caricaPrimaNota() {
		PrimaNota primaNotaTrovata = primaNotaDad.findPrimaNotaByUid(primaNota.getUid());
		if(primaNotaTrovata == null){
			throw new BusinessException("Impossibile trovare la primaNota con uid: "+primaNota.getUid());
		}
		this.primaNota = primaNotaTrovata;
		
	}

}
