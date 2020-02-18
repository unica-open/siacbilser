/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.utility.TxNestedManager;
import it.csi.siac.siaccommonser.business.service.base.BaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;


/**
 * Classe base dell'implementazione della business logic di un generico servizio.
 * Rispetto BaseService aggiunge alcune funzionalit&agrave; di comodo.
 * 
 * Estendendo questa classe bisogna aggiugere le seguenti annotazioni di Spring:
 * <br>
 * 		<code>@Service<code><br>
 * 		<code>@Scope(BeanDefinition.SCOPE_PROTOTYPE)<code><br>
 * <br>
 * 
 * @author Domenico Lisi
 *
 * @param <REQ> Input del servizio che estende ServiceRequest
 * @param <RES> Output del servizio che estende ServiceResponse
 */
public abstract class ExtendedBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends BaseService<REQ,RES> {
	
	@Autowired
	protected ServiceExecutor serviceExecutor;
	
	@Override
	protected RES instantiateNewRes() {
		//Imposta il nome del servizio su serviceExecutor.
		serviceExecutor.setServiceName(this.getClass().getSimpleName());
		return super.instantiateNewRes();
	}
	
	
	/**
	 * Support a current transaction, create a new one if none exists.
	 *  
	 *
	 * @param serviceRequest the service request
	 * @return the res
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public RES executeServiceTxRequired(REQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/**
	 * Create a new transaction, suspend the current transaction if one exists.
	 *
	 * @param serviceRequest the service request
	 * @return the res
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public RES executeServiceTxRequiresNew(REQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/**
	 * Create a new transaction, suspend the current transaction if one exists.
	 *
	 * @param serviceRequest the service request
	 * @return the res
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public RES executeServiceTxRequiresNewWithTimeout(REQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/**
	 * Execute non-transactionally, suspend the current transaction if one exists.
	 *
	 * @param serviceRequest the service request
	 * @return the res
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public RES executeServiceTxNotSupported(REQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/**
	 * Support a current transaction, throw an exception if none exists.
	 *
	 * @param serviceRequest the service request
	 * @return the res
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public RES executeServiceTxMandatory(REQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/**
	 * Support a current transaction, throw an exception if none exists.
	 *
	 * @param serviceRequest the service request
	 * @return the res
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public RES executeServiceTxSupports(REQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/**
	 * Execute within a nested transaction if a current transaction exists, behave like PROPAGATION_REQUIRED else.
	 *
	 * @deprecated Non supportato dalla configurazione attuale. Utilizzare {@link TxNestedManager}
	 * @param serviceRequest the service request
	 * @return the res
	 * @see TxNestedManager
	 */
	@Deprecated
	@Transactional(propagation=Propagation.NESTED)
	public RES executeServiceTxNested(REQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	
	
	/**
	 * Se precedentemente si &egrave; aggiunto nella resoponse un qualunque errore
	 * solleva una BusinessException terminando l'esecuzione del servizio.
	 */
	protected void checkErrori() { 
		//Se sono stati aggiunti errori esco.
		if(res.hasErrori()) {
			throw new BusinessException((Errore)null);
		}
	}


	/**
	 * Se precedentemente si &egrave; aggiunto nella response un errore avente uno dei codici passati come parametro 
	 * solleva una BusinessException terminando l'esecuzione del servizio.
	 *
	 * @param codiciErrore the codici errore
	 */
	protected void checkErrori(String... codiciErrore ) { 
		if(res.verificatoErrore(codiciErrore)) {
			throw new BusinessException((Errore)null);
		}
	}
	
	/**
	 * Check presenza di una Entita .
	 *
	 * @param entita the entita
	 * @param messaggio the messaggio
	 * @throws ServiceParamError the service param error
	 */
	protected void checkEntita(Entita entita, String messaggio) throws ServiceParamError {
		checkEntita(entita, messaggio, true);
	}
	
	/**
	 * Check presenza di una Entita .
	 *
	 * @param entita the entita
	 * @param messaggio the messaggio
	 * @param toThrow the to throw
	 * @throws ServiceParamError the service param error
	 */
	protected void checkEntita(Entita entita, String messaggio, boolean toThrow) throws ServiceParamError {
		checkNotNull(entita, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(messaggio), toThrow);
		checkCondition(entita==null || entita.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid " + messaggio), toThrow);
	}
	
	/**
	 * Check importo.
	 *
	 * @param importo the entita
	 * @param messaggio the messaggio
	 * @throws ServiceParamError the service param error
	 */
	protected void checkImporto(BigDecimal importo, String messaggio) throws ServiceParamError {
		checkImporto(importo, messaggio, true);
	}
	
	/**
	 * Check importo.
	 *
	 * @param importo the entita
	 * @param messaggio the messaggio
	 * @param toThrow the to throw
	 * @throws ServiceParamError the service param error
	 */
	protected void checkImporto(BigDecimal importo, String messaggio, boolean toThrow) throws ServiceParamError {
		checkNotNull(importo, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(messaggio), toThrow);
		checkCondition(importo == null || importo.compareTo(BigDecimal.ZERO) > 0, ErroreCore.VALORE_NON_VALIDO.getErrore(messaggio, "deve essere maggiore di zero"));
	}
	
	/**
	 * Check not blank.
	 *
	 * @param valore the valore
	 * @param messaggio the messaggio
	 * @throws ServiceParamError the service param error
	 */
	protected void checkNotBlank(String valore, String messaggio) throws ServiceParamError {
		checkNotBlank(valore, messaggio, true);
	}
	
	
	/**
	 * Check not blank.
	 *
	 * @param valore the valore
	 * @param messaggio the messaggio
	 * @param toThrow the to throw
	 * @throws ServiceParamError the service param error
	 */
	protected void checkNotBlank(String valore, String messaggio, boolean toThrow) throws ServiceParamError {
		checkCondition(StringUtils.isNotBlank(valore), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(messaggio), toThrow);
	}
	
	/**
	 * Check dei parametri di paginazione .
	 *
	 * @param parametriPaginazione the parametri paginazione
	 * @throws ServiceParamError the service param error
	 */
	protected void checkParametriPaginazione(ParametriPaginazione parametriPaginazione) throws ServiceParamError {
		checkParametriPaginazione(parametriPaginazione, true);
	}
	
	protected void checkParametriPaginazione(ParametriPaginazione parametriPaginazione, boolean toThrow) throws ServiceParamError {
		checkNotNull(parametriPaginazione, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"), toThrow);
		checkCondition(parametriPaginazione == null || parametriPaginazione.getNumeroPagina() >= 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"), toThrow);
		checkCondition(parametriPaginazione == null || parametriPaginazione.getElementiPerPagina() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elementi per pagina parametri paginazione"), toThrow);
	}
	
	/**
	 * Check not null.
	 *
	 * @param objectNotNull the object not null
	 * @param fieldName the field name
	 * @throws ServiceParamError the service param error
	 */
	protected void checkNotNull(Object objectNotNull, String fieldName) throws ServiceParamError {
		checkNotNull(objectNotNull, fieldName, true);
	}
	
	/**
	 * Check not null.
	 *
	 * @param objectNotNull the object not null
	 * @param fieldName the field name
	 * @param toThrow the to throw
	 * @throws ServiceParamError the service param error
	 */
	protected void checkNotNull(Object objectNotNull, String fieldName, boolean toThrow) throws ServiceParamError {
		super.checkNotNull(objectNotNull, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(fieldName), toThrow);
	}
	
	/**
	 * Check codifica.
	 *
	 * @param codifica the codifica
	 * @param fieldName the field name
	 * @throws ServiceParamError the service param error
	 */
	protected void checkCodifica(Codifica codifica, String fieldName) throws ServiceParamError {
		checkCodifica(codifica, fieldName, true);
	}
	
	/**
	 * Check codifica.
	 *
	 * @param codifica the codifica
	 * @param fieldName the field name
	 * @param toThrow the to throw
	 * @throws ServiceParamError the service param error
	 */
	protected void checkCodifica(Codifica codifica, String fieldName, boolean toThrow) throws ServiceParamError {
		checkCodifica(codifica, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(fieldName), toThrow);
	}
	
	/**
	 * Check codifica.
	 *
	 * @param codifica the codifica
	 * @param errore the errore
	 * @param toThrow the to throw
	 * @throws ServiceParamError the service param error
	 */
	protected void checkCodifica(Codifica codifica, Errore errore, boolean toThrow) throws ServiceParamError {
		checkNotNull(codifica, errore, toThrow);
		checkCondition(codifica == null || codifica.getUid() > 0 || StringUtils.isNotBlank(codifica.getCodice()), errore, toThrow);
	}
	
	/**
	 * Check length.
	 *
	 * @param s the s
	 * @param min the min
	 * @param max the max
	 * @param fieldName the field name
	 * @throws ServiceParamError the service param error
	 */
	protected void checkLength(String s, int min, int max, String fieldName) throws ServiceParamError {
		checkLength(s, min, max, fieldName, true);
	}
	
	/**
	 * Check length.
	 *
	 * @param s the s
	 * @param min the min
	 * @param max the max
	 * @param fieldName the field name
	 * @param toThrow the to throw
	 * @throws ServiceParamError the service param error
	 */
	protected void checkLength(String s, int min, int max, String fieldName,  boolean toThrow) throws ServiceParamError {
		Errore errore = ErroreCore.VALORE_NON_VALIDO.getErrore(fieldName, ": deve essere compreso tra " + min + " e " + max);
		checkLength(s, min, max, errore, toThrow);
	}
	
	/**
	 * Check length.
	 *
	 * @param s the s
	 * @param min the min
	 * @param max the max
	 * @param errore the errore
	 * @param toThrow the to throw
	 * @throws ServiceParamError the service param error
	 */
	protected void checkLength(String s, int min, int max, Errore errore,  boolean toThrow) throws ServiceParamError {
		checkCondition(s!=null && s.length() >= min && s.length()<= max, errore, toThrow);
	}
	
	
	
	/**
	 * Permtette di impostare la request. 
	 * Viene utilizzata in casi particolari. Vedi implementazioni di {@link AsyncBaseService}.
	 *
	 * @param serviceRequest the new service request
	 */
	public void setServiceRequest(REQ serviceRequest){
		this.req = serviceRequest;
	}
	
	/**
	 * Permtette di ottenere la response. 
	 * Viene utilizzata in casi particolari. Vedi implementazioni di {@link AsyncBaseService}.
	 *
	 * @return the service response
	 */
	public RES getServiceResponse(){
		return this.res;
	}
	
	/**
	 * Permtette di impostare la response. 
	 * Viene utilizzata in casi particolari. Vedi implementazioni di {@link AsyncBaseService}.
	 *
	 * @param serviceResponse the new service response
	 */
	protected void setServiceResponse(RES serviceResponse){
		this.res = serviceResponse;
	}
	
	
	
	protected void setRollbackOnly() {
		final String methodName = "setRollbackOnly";
		
		if(Boolean.TRUE.equals(TxNestedManager.isTxNested())) {
			log.info(methodName, "Transaction is nested. Rollback to savepoint will be performed by the TxNestedManager.");
			return;
		}
		
		super.setRollbackOnly();
		
	}
	
	/*
	 * Imposta a "rollback only" lo stato della transazione in corso se presente.
	 * Questo metodo presume che la transazione sia gestita tramite le Spring Transaction Api; nel caso venga utilizzata 
	 * un altra tecnologia questo metodo può essere sovrascritto. 
	 * <br/>
	 * la modifica va portata su BaseService!
	 *
	protected void setRollbackOnly() {
		final String methodName = "rollback";
		try{
			TransactionStatus currentTransactionStatus = TransactionAspectSupport.currentTransactionStatus();
			if(currentTransactionStatus.isNewTransaction()) {
				currentTransactionStatus.setRollbackOnly();
				log.info(methodName, "Transaction status is marked as rollback only.");
			} else {
				log.info(methodName, "Transaction is participating in an existing transaction.");
			}
			
		} catch (NoTransactionException nte){
			log.info(methodName, "Execution is not in transaction. Nothing to rollback. ");
		}
	}*/

	/**
	 * Controlla se l'entit&grave; fornita (facoltativa) sia non presente ovvero correttamente valorizzato
	 * @param entita l'entit&agrave; da controllare
	 * @param name il nome dell'entita
	 * @param toThrow se rilanciare l'eccezione
	 * @throws ServiceParamError in caso di fallimento nel controllo, e se il parametro toThrow &eacute; stato impostato come <code>true</code>
	 */
	protected void checkEntitaFacoltativa(Entita entita, String name, boolean toThrow) throws ServiceParamError {
		checkCondition(entita == null || entita.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(name), toThrow);
	}
	
	/**
	 * Controlla se l'entit&grave; fornita (facoltativa) sia non presente ovvero correttamente valorizzato
	 * @param entita l'entit&agrave; da controllare
	 * @param name il nome dell'entita
	 * @throws ServiceParamError in caso di fallimento nel controllo
	 */
	protected void checkEntitaFacoltativa(Entita entita, String name) throws ServiceParamError {
		checkEntitaFacoltativa(entita, name, true);
	}
	
	/**
	 * Controlla una condizione di business
	 * @param condition la condizione da controllare
	 * @param errore l'errore da impostare nel caso in cui la condizione non sia soddisfatta
	 * @throws BusinessException nel caso in cui la condizione non sia soddisfatta
	 */
	protected void checkBusinessCondition(boolean condition, Errore errore) {
		if(!condition) {
			throw new BusinessException(errore, Esito.FALLIMENTO);
		}
	}
	
	/**
	 * Controlla se l'entit&agrave; fornita sia valorizzata e presente di uid
	 * @param e l'entit&agrave; da controllare
	 * @return se l'uid &eacute; valorizzato
	 */
	protected boolean hasUid(Entita e) {
		return e != null && e.getUid() != 0;
	}
	
	/**
	 * Estrae l'uid dall'entit&agrave;
	 * @param e l'entita
	 * @return l'uid
	 */
	protected Integer extractUid(Entita e) {
		return e != null && e.getUid() != 0 ? Integer.valueOf(e.getUid()) : null;
	}
}
