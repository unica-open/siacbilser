/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.model.CheckOnlyElaborazioneAttiva;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * Assicura che il servizio verra' eseguito solo se non esiste un altra elaborazione attiva per la stessa 
 * chiave (o sottoinsime di chiavi) di invocazione del servizio.
 * 
 * Se nella {@link ServiceRequest} del servizio è presente un metodo annotato con {@link CheckOnlyElaborazioneAttiva} che restituisce Boolean.TRUE
 * allora verrà solo evvettuato il controllo di elaborazione Attiva.
 * 
 * @author Domenico Lisi
 *
 * @param <REQ> req
 * @param <RES> res
 */
public abstract class CheckedElabBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {

	protected boolean isRichiestoSoloControlloElaborazioneAttiva;
	
	
	@Override
	protected void checkServiceParamBase() throws ServiceParamError {
		checkNotNull(req, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ServiceRequest"));	
		checkRichiedente();
		
		isRichiestoSoloControlloElaborazioneAttiva = isRichiestoSoloControlloElaborazioneAttiva();
		if(isRichiestoSoloControlloElaborazioneAttiva){
			checkServiceParamControlloElaborazioneAttiva();
		} else {
			checkServiceParam();
		}
		
		
		if(res.hasErrori()){			
			throw new ServiceParamError(null);
		}
	}
	
	
	/**
	 * Fare override di questo metodo se i check necessari per effettuare solamente il controllo di elaborazione attiva
	 * differiscono dai check normali del servizio. Tipicamente i dati necessari sono quelli utilizzati dal metodo {@link #getElabKeys()}.
	 * Se non si fa override verrà richiamato il normale {@link #checkServiceParam()}.
	 * 
	 * @throws ServiceParamError
	 */
	protected void checkServiceParamControlloElaborazioneAttiva() throws ServiceParamError {
		checkServiceParam();
	}
	
	@Override
	protected final void execute() {
		String methodName = "execute";
		
		initElabKeys();
		
		if(isRichiestoSoloControlloElaborazioneAttiva) {
			log.debug(methodName, "Verifica esistenza elaborazione attiva richiesta. Non verra' eseguito il servizio sottostante.");
			boolean esisteElaborazioneAttiva = esisteElaborazioneAttiva();
			log.info(methodName, "Esiste elaborazione attiva:" + esisteElaborazioneAttiva);
			if(esisteElaborazioneAttiva){
				throw new BusinessException(getErroreElaborazioneAttiva(new ElaborazioneAttivaException("Esiste gia' un'elaborazione attiva.")));
			}
			//Non proseguo con il servizio. Esco.
			return;
		}
		
		try {
			startElaborazione();
		} catch (ElaborazioneAttivaException eae){
			log.error(methodName, "Elaborazione attiva esistente.", eae);
			throw new BusinessException(getErroreElaborazioneAttiva(eae));
		}
		
		try {
			executeUnique();
		} finally {
			boolean endElab = endElaborazioneAttiva();
			if(endElab){
				log.info(methodName, "Elaborazione segnata come terminata.");
			}
		}
	}

	/**
	 * Verifica se esiste un metodo nella {@link ServiceRequest} annotato con @{@link CheckOnlyElaborazioneAttiva} che restituisca Boolean.TRUE.
	 * 
	 * @return true se richiesto solo controllo elaborazione attiva.
	 */
	protected boolean isRichiestoSoloControlloElaborazioneAttiva() {
		final String methodName = "richiestoSoloControlloElaborazioneAttiva";
		
		Method[] declaredMethods = req.getClass().getDeclaredMethods();
		
		for(Method method : declaredMethods){
			CheckOnlyElaborazioneAttiva annotation = method.getAnnotation(CheckOnlyElaborazioneAttiva.class);
			
			if (annotation != null) {
				Boolean checkElaborazioneAttiva;
				try {
					checkElaborazioneAttiva = (Boolean) method.invoke(req);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Il metodo " + method.getName() + " annotato con @" + annotation.getClass().getSimpleName()
							+ " non deve avere parametri.", e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Accesso non consentito al metodo " + method.getName() 
							+ " annotato con @" + annotation.getClass().getSimpleName()+". Verificare che il metodo sia public.", e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException( "Il metodo " + method.getName() + " annotato con @" + annotation.getClass().getSimpleName()
									+ " ha sollevato una eccezione: "
									+ (e != null && e.getTargetException() != null ? e.getTargetException().getMessage() : "null"),
									e.getTargetException());
				} catch (ClassCastException e) {
					throw new RuntimeException("Il metodo " + method.getName() + " annotato con @" + annotation.getClass().getSimpleName()
							+ " dever restituire un Boolean. ");
				}

				if (Boolean.TRUE.equals(checkElaborazioneAttiva)) {
					log.info(methodName, "returning true.");
					return true;
				}
				break;
			}
		}
		
		return false;
	}


	
	
	/**
	 *  execute del servizio unica per il bloco impostato da {@link #startElaborazione()}.
	 */
	protected abstract void executeUnique();
	
	
	/**
	 * Inizializza la/e chiave/i di elaborazione.
	 */
	protected abstract void initElabKeys();
	
	/**
	 * Errore di default da sollevare nel caso l'elaborazione sia gia' attiva.
	 * Questo metodo puo' essere sovrascritto per specificare l'errore di Business.
	 * @param eae 
	 * 
	 * @return l'errore
	 */
	protected Errore getErroreElaborazioneAttiva(ElaborazioneAttivaException eae) {
		String msg = eae!=null?eae.getMessage():"Esiste gia' un'elaborazione attiva";
		return ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg); 
	}

	/**
	 * Controlla l'esistenza di una elaborazione attiva per la stessa chiave fornita dal metodo {@link #getElabKeys()}.
	 * 
	 * @return true se esiste.
	 */
	protected abstract boolean esisteElaborazioneAttiva();
	
	/**
	 * Contrassegna come Avviata l'elaborazione.
	 * 
	 * @throws ElaborazioneAttivaException
	 */
	protected abstract void startElaborazione() throws ElaborazioneAttivaException;
	
	/**
	 * Segna come terminata l'elaborazione attiva per la chiave fornita dal metodo {@link #getElabKeys()}.
	 * 
	 * @return true se terminata, false se era gia' terminata in precedenza.
	 */
	protected abstract boolean endElaborazioneAttiva();
	
	
}
