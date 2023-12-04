/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.ClassificatoreGSADad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabile;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabileResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidazioneMassivaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidazioneMassivaPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Validazione massiva della prima nota integrata
 * 
 * @author Marchino Alessandro
 * @author Domenico
 * 
 * @version 0.1.0 17/06/2015
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ValidazioneMassivaPrimaNotaIntegrataService extends CheckedAccountBaseService<ValidazioneMassivaPrimaNotaIntegrata, ValidazioneMassivaPrimaNotaIntegrataResponse> {

	//DADs
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private ClassificatoreGSADad classificatoreGSADad;
	
	//Servizi da richiamare
	@Autowired
	private RicercaSinteticaPrimaNotaIntegrataValidabileService ricercaSinteticaPrimaNotaIntegrataValidabileService;
	
	private RicercaSinteticaPrimaNotaIntegrataValidabile ricercaSintetica;
	private ClassificatoreGSA classificatoreGSA;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRicercaSinteticaPrimaNotaIntegrataValidabile(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Request di ricerca"));
		
		ricercaSintetica = req.getRicercaSinteticaPrimaNotaIntegrataValidabile();
		
		checkEntita(ricercaSintetica.getBilancio(), "bilancio", false);
		checkNotNull(ricercaSintetica.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri di paginazione"), false);
		checkNotNull(ricercaSintetica.getPrimaNota(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("prima nota"), true);
		checkNotNull(ricercaSintetica.getPrimaNota().getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito prima nota"), false);
		
		checkCondition(!Ambito.AMBITO_GSA.equals(ricercaSintetica.getPrimaNota().getAmbito()) || req.getDataRegistrazioneLibroGiornale() != null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data registrazione prima nota"));
	}
	
	@Override
	protected void init() {
		super.init();
		classificatoreGSADad.setEnte(ente);
	}
	

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public ValidazioneMassivaPrimaNotaIntegrataResponse executeServiceTxRequiresNew(ValidazioneMassivaPrimaNotaIntegrata serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional
	public ValidazioneMassivaPrimaNotaIntegrataResponse executeService(ValidazioneMassivaPrimaNotaIntegrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		String methodName = "execute";
		
		checkFaseOperativaCompatibile();
		// SIAC-5336
		initClassificatoreGSA();
		
		List<PrimaNota> primeNoteDaValidare = ricercaPrimeNoteDaValidare();
		
		log.info(methodName, "Numero delle prime note da validare: "+ primeNoteDaValidare.size());
		
		for(PrimaNota primaNota : primeNoteDaValidare) {
			try {
				ValidaPrimaNotaResponse resVPN = validaPrimaNota(primaNota);
				if(resVPN.hasErrori() || resVPN.isFallimento()){
					addPrimanNotaScartata(primaNota, resVPN, null);
				} else {
					log.info(methodName, "Prima nota numero "+primaNota.getNumero()+" [uid: " + primaNota.getUid() + "] validata con successo");
				}
			} catch(Exception e) {
				addPrimanNotaScartata(primaNota, null, e);
			}
		}
	}
	
	private void initClassificatoreGSA() {
		final String methodName = "initClassificatoreGSA";
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
	 * Ricerca l'elenco completo delle prime note da validare.
	 * 
	 * @return prime note da validare.
	 */
	private List<PrimaNota> ricercaPrimeNoteDaValidare() {
		// Vado a modificare la ricerca sintetica: prendo pagine da 50 risultati
		ricercaSintetica.getParametriPaginazione().setElementiPerPagina(50);
		
		List<PrimaNota> primeNoteDaValidare = new ArrayList<PrimaNota>();
		
		// Parto dalla prima pagina
		int numeroPagina = 0;
		// Inizializzo il totale delle pagine
		int totalePagine = Integer.MAX_VALUE;
		
		do {
			RicercaSinteticaPrimaNotaIntegrataValidabileResponse response = ricercaSinteticaPrimaNotaIntegrataValidabile(numeroPagina);
			// In realta' serve solo la prima volta
			totalePagine = response.getTotalePagine();
			primeNoteDaValidare.addAll(response.getPrimeNote());
			
			numeroPagina++;
		} while(numeroPagina < totalePagine);
		return primeNoteDaValidare;
	}



	private void addPrimanNotaScartata(PrimaNota primaNota, ValidaPrimaNotaResponse resVPN, Exception e) {
		final String methodName = "addPrimanNotaScartata";
		
		String excMsg = e != null ? e.getMessage() : "";
		String resMsg = resVPN != null ? resVPN.getDescrizioneErrori() : "";
		String msg = "prima nota numero " + primaNota.getNumero() + " scartata [uid: "+ primaNota.getUid() +"]. Motivazione: " + excMsg + " "+ resMsg;
		
		log.error(methodName, msg, e);
		res.getMessaggi().add(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
		
		// Aggiungo la prima nota a quelle scartate
		res.getPrimeNoteScartate().add(primaNota);
	}

	/**
	 * Controlla che la fase operativa del bilancio sia compatibile con l'operazione
	 */
	private void checkFaseOperativaCompatibile() {
		Bilancio bilancio = bilancioDad.getBilancioByUid(ricercaSintetica.getBilancio().getUid());
		if(bilancio == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid " + ricercaSintetica.getBilancio().getUid()));
		}
		if(bilancio.getFaseEStatoAttualeBilancio() == null || bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio() == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Fase di bilancio", "corrispondente al bilancio con uid " + bilancio.getUid()));
		}
		FaseBilancio faseBilancio = bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio();
		// Controllo che il bilancio NON sia in fase CHIUSO, PLURIENNALE, PREVISIONE
		if(FaseBilancio.CHIUSO.equals(faseBilancio) || FaseBilancio.PLURIENNALE.equals(faseBilancio) || FaseBilancio.PREVISIONE.equals(faseBilancio)) {
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Bilancio", faseBilancio.name()).getTesto());
		}
	}



	/**
	 * Effettuo la ricerca sintetica della prima nota per la pagina passata.
	 * 
	 * @param numeroPagina il numero della pagina
	 * 
	 * @return la response del servizio
	 */
	private RicercaSinteticaPrimaNotaIntegrataValidabileResponse ricercaSinteticaPrimaNotaIntegrataValidabile(int numeroPagina) {
		ricercaSintetica.getParametriPaginazione().setNumeroPagina(numeroPagina);
		return serviceExecutor.executeServiceSuccess(ricercaSinteticaPrimaNotaIntegrataValidabileService, ricercaSintetica);
	}
	
	private ValidaPrimaNotaResponse validaPrimaNota(PrimaNota primaNota) {
		ValidaPrimaNota reqVPN = new ValidaPrimaNota();
		
		reqVPN.setDataOra(new Date());
		reqVPN.setRichiedente(req.getRichiedente());
		reqVPN.setPrimaNota(primaNota);
		if(Ambito.AMBITO_GSA.equals(primaNota.getAmbito())) {
			primaNota.setDataRegistrazioneLibroGiornale(req.getDataRegistrazioneLibroGiornale());
		}
		
		// SIAC-5336: imposto il classificatore GSA
		primaNota.setClassificatoreGSA(classificatoreGSA);
		
		ValidaPrimaNotaResponse result = serviceExecutor.executeServiceTxRequiresNew(ValidaPrimaNotaService.class, reqVPN, null);
		
		return result;
		
	}
	
}
