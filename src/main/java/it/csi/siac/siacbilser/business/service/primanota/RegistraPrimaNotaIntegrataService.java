/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedElabGroupBaseService;
import it.csi.siac.siacbilser.integration.dad.ClassificatoreGSADad;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNotaResponse;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Servizio di registrazione di una prima nota integrata
 * 
 * @author Valentina
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegistraPrimaNotaIntegrataService extends CheckedElabGroupBaseService<RegistraPrimaNotaIntegrata, RegistraPrimaNotaIntegrataResponse> {

	private PrimaNota primaNota;
	
	@Autowired
	private ClassificatoreGSADad classificatoreGSADad;
	
	@Autowired
	private InseriscePrimaNotaService inseriscePrimaNotaService;
	@Autowired
	private AggiornaPrimaNotaService aggiornaPrimaNotaService;
	@Autowired
	private ValidaPrimaNotaService validaPrimaNotaService;
	
	private ClassificatoreGSA classificatoreGSA;

	private Date dataRegistrazioneLibroGiornale;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getIsAggiornamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("isAggiornamento"));
		checkNotNull(req.getIsDaValidare(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("isDaValidare"));
		checkNotNull(req.getPrimaNota(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("prima nota"));
		primaNota = req.getPrimaNota();
		if(Boolean.TRUE.equals(req.getIsAggiornamento())){
			checkCondition(primaNota.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid prima nota"));
		}
		checkNotNull(primaNota.getTipoCausale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registrazione"));
		checkNotNull(primaNota.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione"));
		checkCondition(primaNota.getDescrizione().length()<=500, ErroreCore.FORMATO_NON_VALIDO.getErrore("descrizione", "deve essere minore uguale a 500 caratteri."), false);
		checkNotNull(primaNota.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		
		if(primaNota.getListaMovimentiEP() != null){
			for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
				checkEntita(mov.getCausaleEP(), "causale");
				checkEntita(mov.getRegistrazioneMovFin(), "registrazione");
				if(mov.getListaMovimentoDettaglio() != null){
					for(MovimentoDettaglio det : mov.getListaMovimentoDettaglio()){
						checkNotNull(det.getConto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conto movimento ep"));
						checkNotNull(det.getNumeroRiga(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero riga"));
						checkNotNull(det.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo"));
						checkNotNull(det.getSegno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("segno"));
					}
				}
			}
			eliminaRegistrazioniDuplicateInInput();
		}
	}
	
	@Override
	protected void checkServiceParamControlloElaborazioneAttiva() throws ServiceParamError {
		checkNotNull(req.getPrimaNota(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("prima nota"));
		primaNota = req.getPrimaNota();
		
		if(primaNota.getListaMovimentiEP() != null){
			for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
				checkEntita(mov.getRegistrazioneMovFin(), "registrazione");
			}
		}
	}
	
	@Override
	protected void init() {
		super.init();
		classificatoreGSADad.setEnte(ente);
	}
	
	@Override
	@Transactional
	public RegistraPrimaNotaIntegrataResponse executeService(RegistraPrimaNotaIntegrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void executeUnique() {
		checkClassificatoreGSA();
		// SIAC-5853: estraggo la data definitiva per evitare aggiornamenti errati
		estrazioneDataRegistrazioneDefinitiva();
		
		if(Boolean.TRUE.equals(req.getIsAggiornamento())){
			aggiornaPrimaNota();
		}else{
			inserisciPrimaNota();
		}
		if(Boolean.TRUE.equals(req.getIsDaValidare())){
			
			validaPrimaNota();
		}
		aggiornaMovimentoFinanziario();
		
		// Imposto la prima nota integrata che ho inserito/aggiornato/validato
		if(dataRegistrazioneLibroGiornale != null) {
			primaNota.setDataRegistrazioneLibroGiornale(dataRegistrazioneLibroGiornale);
		}
		res.setPrimaNota(primaNota);
	}

	private void estrazioneDataRegistrazioneDefinitiva() {
		if(!Boolean.TRUE.equals(req.getIsDaValidare()) && Boolean.TRUE.equals(req.getIsAggiornamento())) {
			return;
		}
		dataRegistrazioneLibroGiornale = req.getPrimaNota().getDataRegistrazioneLibroGiornale();
		req.getPrimaNota().setDataRegistrazioneLibroGiornale(null);
	}

	/**
	 * Controlli di validit&agrave; per il classificatore GSA
	 */
	private void checkClassificatoreGSA() {
		final String methodName = "checkClassificatoreGSA";
		if(req.getClassificatoreGSA() == null || req.getClassificatoreGSA().getUid() == 0) {
			log.debug(methodName, "Classificatore GSA non impostato");
			return;
		}
		log.debug(methodName, "Caricamento classificatore GSA per uid " + req.getClassificatoreGSA().getUid());
		classificatoreGSA = classificatoreGSADad.findClassificatoreGSAById(req.getClassificatoreGSA());
		if(classificatoreGSA == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("Classificatore GSA fornito"));
		}
	}

	/**
	 * Introdotta eliminazione silente (effettua solo log ad ERROR) dei movimentiEp duplicati (afferenti alla stessa RegistrazioneMovFin).
	 * Controllo introdotto per sicurezza in seguito a segnalazione: SIAC-4476. Non dovrebbe comunque ricapitare.
	 */
	private void eliminaRegistrazioniDuplicateInInput() {
		String methodName = "eliminaRegistrazioniDuplicateInInput";
		if(primaNota.getListaMovimentiEP() == null || primaNota.getListaMovimentiEP().isEmpty()){
			return;
		}
		
		int size = primaNota.getListaMovimentiEP().size();
		
		List<MovimentoEP> result = new ArrayList<MovimentoEP>();
		Map<Integer,Integer> m = new HashMap<Integer,Integer>();
		for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
			Integer key = mov.getRegistrazioneMovFin().getUid();
			if(m.containsKey(key)){
				m.put(key, m.get(key) + 1);
				continue;
			}
			m.put(key, 1);
			result.add(mov);
		}
		
		int sizeResult = result.size();
		
		if(sizeResult!=size){
			log.error(methodName, "Il servizio e' stato richiamato con piu' movimenti EP afferenti alla stessa RegistrazioneMovFin."
					+ " Dimensione originale: "+ size + ". Dimensione dopo eliminazione duplicati: "+ sizeResult +"."
					+ " Tali registrazioni in input verranno ignorate {uidRegistrazione, conteggioOccorrenze}: "+m);
		}
		
		primaNota.setListaMovimentiEP(result);
	}

	private void inserisciPrimaNota() {
		InseriscePrimaNota reqIPN = new InseriscePrimaNota();
		reqIPN.setPrimaNota(primaNota);
		reqIPN.setRichiedente(req.getRichiedente());
		InseriscePrimaNotaResponse resIPN = executeExternalServiceSuccess(inseriscePrimaNotaService, reqIPN, ErroreCore.OPERAZIONE_NON_CONSENTITA.getCodice());
		if(resIPN.verificatoErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA)){ //SIAC-3089
			res.addErrori(resIPN.getErrori());
			throw new BusinessException("Errore durante l'inserimento della prima nota.");
		}
		primaNota = resIPN.getPrimaNota();
	}
	
	private void aggiornaPrimaNota() {
		AggiornaPrimaNota reqAPN = new AggiornaPrimaNota();
		reqAPN.setPrimaNota(primaNota);
		reqAPN.setRichiedente(req.getRichiedente());
		reqAPN.setSaltaCheckStatoPerAmbito(true);
		AggiornaPrimaNotaResponse resAPN = executeExternalServiceSuccess(aggiornaPrimaNotaService, reqAPN, ErroreCore.OPERAZIONE_NON_CONSENTITA.getCodice(), ErroreGEN.MOVIMENTO_CONTABILE_NON_MODIFICABILE.getCodice());
		if(resAPN.verificatoErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getCodice(), ErroreGEN.MOVIMENTO_CONTABILE_NON_MODIFICABILE.getCodice())){ //SIAC-3089
			res.addErrori(resAPN.getErrori());
			throw new BusinessException("Errore durante l'aggiornamento della prima nota.");
		}
		primaNota = resAPN.getPrimaNota();
	}


	private void validaPrimaNota() {
		ValidaPrimaNota reqVPN = new ValidaPrimaNota();
		reqVPN.setPrimaNota(primaNota);
		primaNota.setClassificatoreGSA(classificatoreGSA);
		primaNota.setDataRegistrazioneLibroGiornale(dataRegistrazioneLibroGiornale);
		reqVPN.setRichiedente(req.getRichiedente());
		ValidaPrimaNotaResponse resVPN = executeExternalServiceSuccess(validaPrimaNotaService, reqVPN, ErroreCore.OPERAZIONE_NON_CONSENTITA.getCodice());
		if(resVPN.verificatoErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA)){ //SIAC-3089
			res.addErrori(resVPN.getErrori());
			throw new BusinessException("Errore durante la validazione della prima nota.");
		}
		primaNota = resVPN.getPrimaNota();
	}

	

	private void aggiornaMovimentoFinanziario() {
//		TODO
		//cosa devo aggiornare e con che dati?
	}


	@Override
	protected String getGroup() {
		return RegistrazioneMovFin.class.getSimpleName();
	}

	@Override
	protected void initElabKeys() {
		if(primaNota.getListaMovimentiEP() != null){
			for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
				elabKeys.add("RegistrazioneMovFin.uid:"+mov.getRegistrazioneMovFin().getUid());
			}
		}
	}
	
	@Override
	protected Errore getErroreElaborazioneAttiva(ElaborazioneAttivaException eae) {
		return ErroreBil.ELABORAZIONE_ATTIVA.getErrore("Esiste gia' una elaborazione attiva per questa Registrazione. "
				+ "Attendere il termine dell'elaborazione precedente. (chiavi elaborazione: "+elabKeys+")");
	}

}
