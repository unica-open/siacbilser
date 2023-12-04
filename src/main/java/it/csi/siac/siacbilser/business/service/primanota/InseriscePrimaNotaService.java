/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.primanota.movimento.MovimentoHandler;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaResponse;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;

/**
 * Servizio di inserimento di una PrimaNota
 * 
 * @author Valentina
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InseriscePrimaNotaService extends InseriscePrimaNotaBaseService<InseriscePrimaNota, InseriscePrimaNotaResponse> {

    private static final Map<StatoOperativoPrimaNota, StatoOperativoRegistrazioneMovFin> MAPPA_STATI_PRIMA_NOTA;
	
	static {
		Map<StatoOperativoPrimaNota, StatoOperativoRegistrazioneMovFin> temp = new HashMap<StatoOperativoPrimaNota, StatoOperativoRegistrazioneMovFin>();
		temp.put(StatoOperativoPrimaNota.ANNULLATO, StatoOperativoRegistrazioneMovFin.ANNULLATO);
		temp.put(StatoOperativoPrimaNota.PROVVISORIO, StatoOperativoRegistrazioneMovFin.REGISTRATO);
		temp.put(StatoOperativoPrimaNota.DEFINITIVO, StatoOperativoRegistrazioneMovFin.CONTABILIZZATO);
		MAPPA_STATI_PRIMA_NOTA = Collections.unmodifiableMap(temp);
		temp = null;
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		
		if(primaNota.getListaMovimentiEP() != null){
			for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
				if(isPrimaNotaIntegrata()){
					checkEntita(mov.getRegistrazioneMovFin(), "registrazione");
				} else {
					checkNotNull(primaNota.getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito prima nota"));
				}
			}
		}
		
	}
	
	@Override
	@Transactional
	public InseriscePrimaNotaResponse executeService(InseriscePrimaNota serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		
		checkImportiDareAvere();
		
		effettuaOperazioniPreliminariSuRegistrazioni();
		
		popolaSoggetto();
		
		//SIAC-8134
		popolaStrutturaCompetente();
		
		Integer numero = primaNotaDad.staccaNumeroPrimaNota(Integer.toString(primaNota.getBilancio().getAnno()), primaNota.getAmbito());
		log.debug(methodName, "Numero della PrimaNota: " + numero);
		primaNota.setNumero(numero);
		
		if(!req.isSovrascriviDefaultStatoOperativo()) {
			primaNota.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		}
		
		popolaMovimentiEP();
		
		primaNotaDad.inserisciPrimaNota(primaNota);
		
		aggiornaStatoRegistrazioniMovFin();
		
		res.setPrimaNota(primaNota);
	}

	/**
	 * 
	 */
	private void effettuaOperazioniPreliminariSuRegistrazioni() {
		if(req.isSaltaOperazioniPreliminarisuRegistrazioni()) {
			return;
		}
		checkPrimaNotaGiaAssociataARegistrazioni();
		popolaRegistrazioneMovFin0();
		popolaAmbitoAPartireDalleRegistrazioni();
	}
	
	/**
	 * Controlla che tutte le registrazioni su cui si sta inserendo la primaNota siano in stato NOTIFICATO.
	 */
	private void checkPrimaNotaGiaAssociataARegistrazioni() {
		final String methodName = "checkPrimaNotaGiaAssociataARegistrazioni";
		
		if(!isPrimaNotaIntegrata()){
			log.debug(methodName, "Prima nota non di tipo Integrata. Non effettuo controlli.");
			return;
		}
		
		Set<Integer> uidsRegistrazioniMovFin = getUidsRegistrazioniMovFin();
		
		Long countRegConPnValida = registrazioneMovFinDad.countRegistrazioniByUIdsAndStato(uidsRegistrazioniMovFin, StatoOperativoRegistrazioneMovFin.REGISTRATO,
				StatoOperativoRegistrazioneMovFin.CONTABILIZZATO, StatoOperativoRegistrazioneMovFin.ELABORATO);
		
		log.debug(methodName, "Numero registrazioni in stato REGISTRATO, CONTABILIZZATO, ELABORATO: " + countRegConPnValida);
		
		// Non devono esistere Registrazioni con prime note associate.
		if(countRegConPnValida.longValue() > 0L){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Alcune registrazioni risultano gia' associate ad una prima nota. [uids: "+uidsRegistrazioniMovFin+"]"));
		}
	}

	/**
	 * Ottiene il set di uid delle registrazioni mov fin di cui si vuole inserire la primaNota.
	 * 
	 * @return uid registrazioni
	 */
	private Set<Integer> getUidsRegistrazioniMovFin() {
		Set<Integer> uidsRegistrazioniMovFin = new HashSet<Integer>();
		if(primaNota.getListaMovimentiEP() != null){
			for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
				uidsRegistrazioniMovFin.add(mov.getRegistrazioneMovFin().getUid());
			}
		}
		return uidsRegistrazioniMovFin;
	}
	
	/**
	 * Popola i dati della prima registrazioneMovFin se presente. (NB: solo la prima).
	 */
	private void popolaRegistrazioneMovFin0() {
		String methodName = "popolaRegistrazioneMovFin0";
		//Evita di far passare l'Ambito in Request e ripsetta la congruenza dell'Ambito tra registrazioni e primeNote.
		if(isPrimaNotaIntegrata() && primaNota.getListaMovimentiEP()!=null){
			for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
				RegistrazioneMovFin regMovFin = registrazioneMovFinDad.findRegistrazioneMovFinByIdBase(mov.getRegistrazioneMovFin().getUid());
				mov.setRegistrazioneMovFin(regMovFin);
				log.debug(methodName, "Dati popolati per la RegistrazioneMovFin con uid: "+ regMovFin.getUid());
				return;
			}
		}
		
	}
	
	/**
	 * Popola ambito a partire dalle registrazioni.
	 * Ricava l'ambito dalla prima registrazione collegata ai movimentiEP.
	 * (si presume l'inserimento della primaNota per movimenti appartenenti a registrazioni dello stesso ambito).
	 * 
	 */
	private void popolaAmbitoAPartireDalleRegistrazioni() { 
		String methodName = "popolaAmbitoAPartireDalleRegistrazioni";
		//Evita di far passare l'Ambito in Request e ripsetta la congruenza dell'Ambito tra registrazioni e primeNote.
		if(isPrimaNotaIntegrata() && primaNota.getListaMovimentiEP() != null){
			
			RegistrazioneMovFin registrazioneMovFin0 = getRegistrazioneMovFin0();
			if(registrazioneMovFin0 != null) {
				log.debug(methodName, "Ambito della registrazione: " + registrazioneMovFin0.getAmbito());
				primaNota.setAmbito(registrazioneMovFin0.getAmbito());
			} else {
				log.debug(methodName, "Nessuna registrazioneMovFin presente. L'ambito verra' lasciato null.");
			}
		}
	}

	private RegistrazioneMovFin getRegistrazioneMovFin0() {
		for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
			return mov.getRegistrazioneMovFin();
		}
		return null;
	}
	
	private void popolaSoggetto() {
		final String methodName = "popolaSoggetto";
		
		if(isPrimaNotaIntegrata() 
				&& primaNota.getListaMovimentiEP() != null
				&& !primaNota.getListaMovimentiEP().isEmpty()
				&& !hasUid(primaNota.getSoggetto())){
			
			log.debug(methodName, "Il Soggetto della PrimaNota non e' stato specificato in request: lo individuo a partire dalla RegistrazioneMovFin.");
			RegistrazioneMovFin registrazioniMovFin0 = getRegistrazioneMovFin0();
			if(registrazioniMovFin0 == null){
				log.warn(methodName, "Soggetto della prima nota non individuabile in quanto non sono presenti RegistrazioneMovFin. Il soggetto restera' null.");
				primaNota.setSoggetto(null);
				return;
			}
			
			MovimentoHandler<? extends Entita> movimentoHandler;
			
			movimentoHandler  = MovimentoHandler.getInstance(serviceExecutor,
					req.getRichiedente(),
					ente,
					registrazioniMovFin0.getBilancio(),
					registrazioniMovFin0.getMovimento().getClass(), 
					registrazioniMovFin0.getMovimentoCollegato() != null ? registrazioniMovFin0.getMovimentoCollegato().getClass() : null);
			
			movimentoHandler.caricaMovimento(registrazioniMovFin0);
			List<RegistrazioneMovFin> registrazioni = new ArrayList<RegistrazioneMovFin>();
			registrazioni.add(registrazioniMovFin0);
			movimentoHandler.inizializzaDatiMovimenti(registrazioni);
			
			Soggetto soggetto = movimentoHandler.getSoggetto(registrazioniMovFin0);
			//SIAC-7327
			if(soggetto != null && soggetto.getUid() != 0){
				log.debug(methodName, "Individuato soggetto: " + soggetto.getUid() + " per la registrazione con uid: " + registrazioniMovFin0.getUid());
				primaNota.setSoggetto(soggetto);
			} else {
				log.warn(methodName, "Non sono riuscito ad individuare il Soggetto per la registrazione con uid: " + registrazioniMovFin0.getUid() + " lo lascio null.");
			}
		}
	}
	
	private void aggiornaStatoRegistrazioniMovFin() {
		final String methodName = "aggiornaStatoRegistrazioniMovFin";
		
		if(!isPrimaNotaIntegrata()){
			log.debug(methodName, "PrimaNota NON di tipo Integrata. Non devo aggiornare le registrazioni.");
			return;
		}
		
		StatoOperativoRegistrazioneMovFin statoRegistrazioneNew = MAPPA_STATI_PRIMA_NOTA.get(this.primaNota.getStatoOperativoPrimaNota());
		
		if(primaNota.getListaMovimentiEP() != null) {
			for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
				registrazioneMovFinDad.aggiornaStatoRegistrazioneMovFin(mov.getRegistrazioneMovFin().getUid(), statoRegistrazioneNew);
			}
		}
		
	}
}
