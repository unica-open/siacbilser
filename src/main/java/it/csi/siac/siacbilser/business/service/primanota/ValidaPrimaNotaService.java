/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedElabGroupBaseService;
import it.csi.siac.siacbilser.business.service.registrazionemovfin.AssegnaContoEPRegistrazioneMovFinService;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.AssegnaContoEPRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.AssegnaContoEPRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.CollegaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNotaResponse;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ValidaPrimaNotaService extends CheckedElabGroupBaseService<ValidaPrimaNota, ValidaPrimaNotaResponse> {

	@Autowired
	private PrimaNotaDad primaNotaDad;
	@Autowired
	private RegistrazioneMovFinDad registrazioneMovFinDad;
	
	@Autowired
	private CollegaPrimeNoteService collegaPrimeNoteService;

	private PrimaNota primaNota;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getPrimaNota(), "prima nota");
		primaNota = req.getPrimaNota();
		
		checkNotNull(primaNota.getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito prima nota"));
		
		// SIAC-5853
		checkCondition(!Ambito.AMBITO_GSA.equals(primaNota.getAmbito()) || primaNota.getDataRegistrazioneLibroGiornale() != null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data registrazione prima nota"));
	}
	
	@Transactional
	@Override
	public ValidaPrimaNotaResponse executeService(ValidaPrimaNota serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void init() {
		super.init();
		
		primaNotaDad.setLoginOperazione(loginOperazione);
		primaNotaDad.setEnte(ente);
		
		registrazioneMovFinDad.setLoginOperazione(loginOperazione);
		registrazioneMovFinDad.setEnte(ente);
	}
	
	
	@Override
	protected void executeUnique() {
		caricaDettagliNecessariPrimaNota();
		
		checkValidazioneConsentita();
		checkStatoOperativo();
		aggiungiDatiRegistrazioneLibroGiornale();
		aggiornaStatoOperativo();
		if(TipoCausale.Integrata.equals(primaNota.getTipoCausale())){
			aggiornaStatoRichiesta();
			assegnaContoEP();
		}
		// SIAC-5336
		associaClassificatoreGSA();
		
		collegaEventualiPrimeNoteDefinitiveAnnullate();
		res.setPrimaNota(primaNota);
	}

	private void collegaEventualiPrimeNoteDefinitiveAnnullate() {
		List<PrimaNota> primeNoteDaCollegare = primaNotaDad.findPrimeNoteDefinitiveAnnullate(primaNota);
		if(primeNoteDaCollegare == null || primeNoteDaCollegare.isEmpty()){
			return;
		}
		TipoRelazionePrimaNota tipoRelazionePrimaNota = primaNotaDad.findTipoRelazioneByCodice("S");
		for(PrimaNota pn : primeNoteDaCollegare){
			pn.setTipoRelazionePrimaNota(tipoRelazionePrimaNota);
			collegaPrimaNota(pn);
			primaNota.getListaPrimaNotaFiglia().add(pn);
		}
	}


	private void collegaPrimaNota(PrimaNota pn) {
		CollegaPrimeNote reqCPN = new CollegaPrimeNote();
		reqCPN.setRichiedente(req.getRichiedente());
		reqCPN.setPrimaNotaFiglia(pn);
		reqCPN.setPrimaNotaPadre(primaNota);
		collegaPrimeNoteService.executeService(reqCPN);
	}

	/**
	 *  Carica i movimentiEP (con uid e registrazione) e il tipoCausale della primaNota
	 */
	private void caricaDettagliNecessariPrimaNota() {
		List<MovimentoEP> movimentiEP = primaNotaDad.findMovimentiEPByPrimaNota(primaNota.getUid());
		primaNota.setListaMovimentiEP(movimentiEP);
		TipoCausale tipoCausale = primaNotaDad.findTipoCausalePrimaNota(primaNota.getUid());
		primaNota.setTipoCausale(tipoCausale);
	}

	
	/**
	 * Controlla che tutti i movimentiEP abbiano le scritture compilate
	 */
	private void checkValidazioneConsentita() {
		//Esiste gia' dall'inserimento un movimentoEP per ogni quota.
//		Per la validazione devo controllare che ogni movimentoEP abbia i dettagli.
		for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
			if(mov.getListaMovimentoDettaglio() == null || mov.getListaMovimentoDettaglio().isEmpty()){
				throw new BusinessException(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore("non tutti i movimenti della prima nota hanno le scritture compilate."));
			}
		}
		
		// SIAC-5536
		if(primaNota.getClassificatoreGSA() != null && primaNota.getClassificatoreGSA().getUid() != 0 && !Ambito.AMBITO_GSA.equals(primaNota.getAmbito())) {
			throw new BusinessException(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile associare il Classificatore GSA a una Prima Nota di ambito non GSA"));
		}
		
	}

	
	/**
	 * Controlla che lo stato della primaNota sia PROVVISORIO
	 */
	private void checkStatoOperativo() {
		StatoOperativoPrimaNota stato = primaNotaDad.findStatoOperativoPrimaNota(primaNota.getUid());
		primaNota.setStatoOperativoPrimaNota(stato);
		if(!StatoOperativoPrimaNota.PROVVISORIO.equals(primaNota.getStatoOperativoPrimaNota())){
			throw new BusinessException(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore("stato incongruente."));
		}
	}

	
	/**
	 *  Assegna alle primaNota il numero progressivo NumeroRegistrazioneLibroGiornale e la dataRegistrazioneLibroGiornale
	 */
	private void aggiungiDatiRegistrazioneLibroGiornale() {
		String annoBilancioPrimaNota = primaNotaDad.findAnnoBilancio(primaNota.getUid());
		
		// ISAC-5853
		if(!Ambito.AMBITO_GSA.equals(primaNota.getAmbito())) {
			primaNota.setDataRegistrazioneLibroGiornale(new Date());
		}
		
		Integer numeroRegistrazioneLibroGiornale = primaNotaDad.staccaNumeroRegistrazioneLibroGiornale(annoBilancioPrimaNota, primaNota.getAmbito());
		primaNota.setNumeroRegistrazioneLibroGiornale(numeroRegistrazioneLibroGiornale);
		primaNotaDad.salvaDatiRegistrazioneLibroGiornale(primaNota.getUid(), primaNota.getDataRegistrazioneLibroGiornale(), primaNota.getNumeroRegistrazioneLibroGiornale());
	}
	
	
	/**
	 * Aggiorna lo stato della primaNota a DEFINITIVO
	 */
	private void aggiornaStatoOperativo() {
		primaNota.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.DEFINITIVO);
		primaNotaDad.aggiornaStatoOperativoPrimaNota(primaNota, StatoOperativoPrimaNota.DEFINITIVO, primaNota.getStatoAccettazionePrimaNotaDefinitiva());
	}
	
	
	/**
	 * Assegna alle registrazioni un Conto Economico Patrimoniale
	 */
	private void assegnaContoEP() {
		for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
			AssegnaContoEPRegistrazioneMovFin reqACEP = new AssegnaContoEPRegistrazioneMovFin();
			reqACEP.setRegistrazioneMovFin(mov.getRegistrazioneMovFin());
			reqACEP.setRichiedente(req.getRichiedente());
			AssegnaContoEPRegistrazioneMovFinResponse resACEP = serviceExecutor.executeServiceSuccess(AssegnaContoEPRegistrazioneMovFinService.class, reqACEP);
			mov.setRegistrazioneMovFin(resACEP.getRegistrazioneMovFin());		
		}
		
	}

	
	/**
	 * Aggiorna lo stato delle registrazioni a CONTABILIZZATO
	 */
	private void aggiornaStatoRichiesta() {
		for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
			registrazioneMovFinDad.aggiornaStatoRegistrazioneMovFin(mov.getRegistrazioneMovFin().getUid(), StatoOperativoRegistrazioneMovFin.CONTABILIZZATO);
		}
		
	}
	
	/**
	 * Associazione del classificatore GSA
	 */
	private void associaClassificatoreGSA() {
		final String methodName = "associaClassificatoreGSA";
		if(primaNota.getClassificatoreGSA() == null || primaNota.getClassificatoreGSA().getUid() == 0) {
			log.debug(methodName, "Classificatore GSA non impostato");
			return;
		}
		log.debug(methodName, "Associazione classificatore GSA [" + primaNota.getClassificatoreGSA().getUid() + "] alla prima nota [" + primaNota.getUid() + "]");
		primaNotaDad.associaClassificatoreGSA(primaNota, primaNota.getClassificatoreGSA());
	}

	@Override
	protected String getGroup() {
		return PrimaNota.class.getSimpleName();
	}

	@Override
	protected void initElabKeys() {
		elabKeys.add("PrimaNota.uid:"+req.getPrimaNota().getUid());
	}

	@Override
	protected Errore getErroreElaborazioneAttiva(ElaborazioneAttivaException eae) {
		return ErroreBil.ELABORAZIONE_ATTIVA.getErrore("Esiste gia' una elaborazione attiva per questa Prima Nota. "
				+ "Attendere il termine dell'elaborazione precedente." +" (chiave elaborazione: "+elabKeys+")");
	}

}
