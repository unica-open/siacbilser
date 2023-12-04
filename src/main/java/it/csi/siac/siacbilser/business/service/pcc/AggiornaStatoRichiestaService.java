/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pcc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.pcc.marc.schema.callbackservicetypes_1.AggiornamentoStatoRichiestaRequest;
import it.csi.siac.pcc.marc.schema.marccommontypes_1.ResponseType;
import it.csi.siac.pcc.marc.schema.marccommontypes_1.ResultType;
import it.csi.siac.pcc.marc.schema.marccommontypes_1.StatoRichiestaType;
import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.business.service.pcc.AggiornaStatoRichiestaService.AggiornaStatoRichiesta;
import it.csi.siac.siacbilser.business.service.pcc.AggiornaStatoRichiestaService.AggiornaStatoRichiestaResponse;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.tesoro.fatture.ErroreElaborazioneOperazioneTipo;
import it.tesoro.fatture.ErroreTipo;

/**
 * Servizio richiamatao da MARC per aggiornare lo stato di elaborazione delle registrazioni.
 * 
 * @author Domenico Lisi
 *
 */
//http://dev-www.ruparpiemonte.it/siacbilser/MarcCallbackService?wsdl
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStatoRichiestaService extends ExtendedBaseService<AggiornaStatoRichiesta, AggiornaStatoRichiestaResponse> {

	private static LogSrvUtil log = new LogSrvUtil(AggiornaStatoRichiestaService.class);
	
	//DADs
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	
	@Override
	protected void checkRichiedente() throws ServiceParamError {
		//Evito il check del richiedente!
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAggiornamentoStatoRichiestaRequest(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aggiornamento stato richiesta"));
		checkNotBlank(req.getAggiornamentoStatoRichiestaRequest().getIdTransazionePA(), "id transazione PA aggiornamento stato richiesta", false);
		checkNotNull(req.getAggiornamentoStatoRichiestaRequest().getStato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato aggiornamento stato richiesta"), false);
		checkNotNull(req.getAggiornamentoStatoRichiestaRequest().getEsitoTrattamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("esito trattamento aggiornamento stato richiesta"));	
		checkNotNull(req.getAggiornamentoStatoRichiestaRequest().getEsitoTrattamento().getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice esito trattamento aggiornamento stato richiesta"), false);
		checkNotNull(req.getAggiornamentoStatoRichiestaRequest().getEsitoTrattamento().getMessaggio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("messaggio esito trattamento aggiornamento stato richiesta"), false);
	}
	
	@Override
	@Transactional
	public AggiornaStatoRichiestaResponse executeService(AggiornaStatoRichiesta serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";

		logDatiRispostaElaborazione();
		
		String idTransazionePA = req.getAggiornamentoStatoRichiestaRequest().getIdTransazionePA();
		Set<Integer> uidsRegistroComunicazioniPCC = ottieniUidsRegistroComunicazioniPCC(idTransazionePA);
		
		//Aggiorno lo stato per le registrazioni coinvolte.
		StatoRichiestaType stato = req.getAggiornamentoStatoRichiestaRequest().getStato();
		log.info(methodName, "Imposto lo stato "+stato+" per i seguendi uid del RegistroComunicazioniPCC: "+uidsRegistroComunicazioniPCC);
		impostaStatoRegistrazioni(uidsRegistroComunicazioniPCC, stato.name()); //TODO sarebbe meglio verificare che lo stato precedente sia coerente.
		
		//Aggiorno codiceEsito e descrizioneEsito per le registrazioni coinvolte.
		ResultType esitoTrattamento = req.getAggiornamentoStatoRichiestaRequest().getEsitoTrattamento();
		registroComunicazioniPCCDad.impostaEsitoRegistrazioni(uidsRegistroComunicazioniPCC, esitoTrattamento.getCodice(), esitoTrattamento.getMessaggio());
		
		
		if(StatoRichiestaType.ESEGUITA.equals(stato)) {
			log.debug(methodName, "Richista in stato ESEGUITA. Aggiorno la data fine elaborazione.");
			//Aggiorno data della registrazione
			Date dataFineElaborazione = getDataFineElaborazionePCC();
			if(dataFineElaborazione==null){
				String msg = "Richista in stato ESEGUITA ma con data fine elaborazione non valorizzata. IdTransazionePA: "+req.getAggiornamentoStatoRichiestaRequest().getIdTransazionePA();
				log.error(methodName, msg);
				
				ResponseType responseType = new ResponseType();
				responseType.setResult(ResultTypeEnum.RT003.getResultType(msg));
				res.setResponseType(responseType);
				throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dataFineElaborazione"), Esito.SUCCESSO, false);
				//TODO quando il servizio verrà veramente invocato con i dati di PCC verificare se questo dato e' da considerarsi sempre presente.
				//in tal caso cambiare in "true" l'ultimo parametro della BusinessException sollevata qui sopra e l'esito in FALLIMENTO.
				
			} else {
				registroComunicazioniPCCDad.impostaDataInvioRegistrazioni(uidsRegistroComunicazioniPCC, dataFineElaborazione);
			}
			
		}
		
		//Imposto l'esito di SUCCESSO.
		ResponseType responseType = new ResponseType();
		responseType.setResult(ResultTypeEnum.RT000.getResultType());
		res.setResponseType(responseType);
	}
	
	
	@Override
	protected void setRollbackOnly() {
		
		ResultType resultType; 
		
		if(res.verificatoErroreDiSistema()) {
			//Errore di natura tecnica
			resultType = ResultTypeEnum.RT210.getResultType(res.getErroriMsg());
		} else {
			//if(Esito.FALLIMENTO.equals(res.getEsito())) {
				//Errore di natura applicativa bloccante
				resultType = ResultTypeEnum.RT109.getResultType(res.getErroriMsg()); //Gli errori di checkServiceParam finiscono qui.
			/*} else { 
				//Errore di natura applicativa non bloccante 
				// --> In realtà qui non ci arriva se la BusinessException sollevata ha correttamente impostato rollbackOnly = false
				// --> Quando si solleva una eccezione non bloccante tramite BusinessException bisogna ricordarsi di impostare prima il ResultType!
				resultType = ResultTypeEnum.RT003.getResultType(res.getErroriMsg());
			}*/
		}
		
		ResponseType responseType = new ResponseType();
		responseType.setResult(resultType);
		res.setResponseType(responseType);
		
		super.setRollbackOnly();
	}

	/**
	 * Logga i dati di risposta che arrivano da PCC.
	 */
	private void logDatiRispostaElaborazione() {
		log.logXmlTypeObject(req.getAggiornamentoStatoRichiestaRequest().getRispostaQueryOperazioneContabile(), "RispostaQueryOperazioneContabile");
		log.logXmlTypeObject(req.getAggiornamentoStatoRichiestaRequest().getRispostaProxyOperazioneContabile(), "ProxyOperazioneContabile");
		
		logErroriElaborazione();
		logErroriTrasmissione();
	}

	private void logErroriTrasmissione() {
		List<ErroreTipo> listaErroriTx;
		try{
			listaErroriTx = req.getAggiornamentoStatoRichiestaRequest().getRispostaQueryOperazioneContabile().getDatiRisposta().getListaErroreTrasmissione().getErroreTrasmissione();
		} catch (NullPointerException e){
			listaErroriTx = new ArrayList<ErroreTipo>();
		}
		
		for(ErroreTipo e : listaErroriTx){
			log.error("", e.getCodice() + " - " +e.getDescrizione());
		}
	}

	private void logErroriElaborazione() {
		List<ErroreElaborazioneOperazioneTipo> listaErrori;
		try{
			listaErrori = req.getAggiornamentoStatoRichiestaRequest().getRispostaQueryOperazioneContabile().getDatiRisposta().getListaErroreElaborazione().getErroreElaborazione();
		} catch (NullPointerException e){
			listaErrori = new ArrayList<ErroreElaborazioneOperazioneTipo>();
		}
		
		for(ErroreElaborazioneOperazioneTipo e : listaErrori){
			log.error("", e.getCodiceEsitoElaborazioneOperazione() + " - " +e.getDescrizioneEsitoElaborazioneOperazione()  + " - tipoOperazione: " + e.getTipoOperazione());
		}
	}

	private Date getDataFineElaborazionePCC() {
		String methodName = "getDataFineElaborazionePCC";
		try{
			Date result = req.getAggiornamentoStatoRichiestaRequest().getRispostaQueryOperazioneContabile().getDatiRisposta().getDataFineElaborazione().getTime();
			log.debug(methodName, "returning: "+ result);
			return result;
		} catch (NullPointerException npe){
			log.debug(methodName, "returning: null!");
			return null;
		}
	}
	
	
	private Set<Integer> ottieniUidsRegistroComunicazioniPCC(String idTransazionePA) {
		final String methodName = "ottieniUidsRegistroComunicazioniPCC";
		
		
		Set<Integer> result = registroComunicazioniPCCDad.findRegistrazioniUidsByIdTransazionePA(idTransazionePA);
		log.debug(methodName, "Returning: "+ result);
		return result;
		
		
//		//rimuovo il suffisso con il timestamp
//		idTransazionePA = idTransazionePA.replaceAll("_[0-9]*$", "");
//		
//		Set<Integer> result = new HashSet<Integer>();
//		if(StringUtils.isBlank(idTransazionePA)){
//			return result;
//		}
//		String[] split = idTransazionePA.split("( )*,( )*");
//		
//		for (int i = 0; i < split.length; i++) {
//			try{
//				Integer value = Integer.valueOf(split[i]);
//				result.add(value);
//			} catch(NumberFormatException nfe) {
//				log.error(methodName, "idTransazionePA devono contenere numeri separati da virgola. Valore ricevuto: "+ idTransazionePA, nfe);
//				throw new BusinessException(ErroreCore.FORMATO_NON_VALIDO.getErrore("idTransazionePA", "Devono essere numeri separati da virgola. Valore ricevuto: "+ idTransazionePA));
//			}
//			
//		}
//		log.debug(methodName, "returning: "+ result);
//		return result;
	}

	private void impostaStatoRegistrazioni(Set<Integer> uidsRegistroComunicazioniPCC, String stato) {
		registroComunicazioniPCCDad.impostaStatoRegistrazioni(uidsRegistroComunicazioniPCC, stato);
	}
	
	
	/**
	 * Enumera i possibili ResultType di questo servizio.
	 * 
	 * @author Domenico
	 *
	 */
	public static enum ResultTypeEnum {
		/** esito positivo */
		RT000("000", "Stato richiesta correttamente aggiornato."),
		/** esito positivo con segnalazione di anomalie non rilevanti */
		RT003("003", "Stato richiesta correttamente aggiornato con errori non rilevanti."),
		/** esito negativo a causa di motivi di natura applicativa */
		RT109("109", "Impossibile aggiornare la richiesta: Errori di natura applicativa."),
		/** esito negativo a causa di motivi di natura sistemistica (es. DB non disponibile)  */
		RT210("210", "Impossibile aggiornare la richiesta: Errori di natura sistemistica."),
		;
		
		private String codiceErrore;
		private String messaggioErrroreDiDefault;

		ResultTypeEnum(String codiceErrore, String messaggioErrroreDiDefault){
			this.codiceErrore = codiceErrore;
			this.messaggioErrroreDiDefault = messaggioErrroreDiDefault;
			
		}

		/**
		 * Ottiene una nuova istanza di ResultType valorizzata con il codice di errore e un messaggio di dafault.
		 * Una volta ottenuta l'istanza tale messaggio puo' essere modificato o meno in base alle proprie esigenze.
		 * 
		 * @return the resultType
		 */
		public ResultType getResultType() {
			ResultType rt = new ResultType();
			rt.setCodice(codiceErrore);
			rt.setMessaggio(messaggioErrroreDiDefault);
			return rt;
		}
		
		/**
		 * Ottiene una nuova istanza di ResultType valorizzata con il codice di errore e il messaggio passato come parametro.
		 * 
		 * @param messaggioErrore
		 * @return the resultType
		 */
		public ResultType getResultType(String messaggioErrore) {
			ResultType rt = new ResultType();
			rt.setCodice(codiceErrore);
			rt.setMessaggio(messaggioErrore);
			return rt;
		}

		/**
		 * @return the codiceErrore
		 */
		public String getCodiceErrore() {
			return codiceErrore;
		}

		/**
		 * @return the descrizioneErrroreDiDefault
		 */
		public String getMessaggioErrroreDiDefault() {
			return messaggioErrroreDiDefault;
		}
		
	}
	
	
	
	public static class AggiornaStatoRichiesta extends ServiceRequest {
		private AggiornamentoStatoRichiestaRequest aggiornamentoStatoRichiestaRequest;

		/**
		 * @return the aggiornamentoStatoRichiestaRequest
		 */
		public AggiornamentoStatoRichiestaRequest getAggiornamentoStatoRichiestaRequest() {
			return aggiornamentoStatoRichiestaRequest;
		}

		/**
		 * @param aggiornamentoStatoRichiestaRequest the aggiornamentoStatoRichiestaRequest to set
		 */
		public void setAggiornamentoStatoRichiestaRequest(AggiornamentoStatoRichiestaRequest aggiornamentoStatoRichiestaRequest) {
			this.aggiornamentoStatoRichiestaRequest = aggiornamentoStatoRichiestaRequest;
		}
		
		
	}
	
	public static class AggiornaStatoRichiestaResponse extends ServiceResponse {
		private ResponseType responseType;

		/**
		 * @return the responseType
		 */
		public ResponseType getResponseType() {
			return responseType;
		}

		/**
		 * @param responseType the responseType to set
		 */
		public void setResponseType(ResponseType responseType) {
			this.responseType = responseType;
		}
		
		/**
		 * Ottiene un messggio di descrizione di tutti gli errori che si sono verificati.
		 * 
		 * @return messaggio di errore unificato.
		 */
		public String getErroriMsg(){
			StringBuilder msg = new StringBuilder();
			for(Errore e : getErrori()){
				msg.append("; ").append(e.getTesto());
			}
			return msg.substring(2);
		}
		
		
		
	}
	

	
}

