/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.inviofatturapa;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siacbilser.business.service.inviofatturapa.factory.InvioFatturaPACassaPrevidenzialeFELFactory;
import it.csi.siac.siacbilser.business.service.inviofatturapa.factory.InvioFatturaPACausaleFELFactory;
import it.csi.siac.siacbilser.business.service.inviofatturapa.factory.InvioFatturaPADatiGestionaliFELFactory;
import it.csi.siac.siacbilser.business.service.inviofatturapa.factory.InvioFatturaPAFatturaFELFactory;
import it.csi.siac.siacbilser.business.service.inviofatturapa.factory.InvioFatturaPAFattureCollegateFELFactory;
import it.csi.siac.siacbilser.business.service.inviofatturapa.factory.InvioFatturaPAOrdineAcquistoFELFactory;
import it.csi.siac.siacbilser.business.service.inviofatturapa.factory.InvioFatturaPAPagamentoFELFactory;
import it.csi.siac.siacbilser.business.service.inviofatturapa.factory.InvioFatturaPAPortaleFattureFELFactory;
import it.csi.siac.siacbilser.business.service.inviofatturapa.factory.InvioFatturaPAPrestatoreFELFactory;
import it.csi.siac.siacbilser.business.service.inviofatturapa.factory.InvioFatturaPAProtocolloFELFactory;
import it.csi.siac.siacbilser.business.service.inviofatturapa.factory.InvioFatturaPARiepilogoBeniFELFactory;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.FatturaFELDad;
import it.csi.siac.siacbilser.model.exception.UnmappedEnteException;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.SistemaEsterno;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.InvioFatturaPA;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.InvioFatturaPAResponse;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.AltriDatiGestionaliType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.CedentePrestatoreType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiBeniServiziType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiCassaPrevidenzialeType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiDocumentiCorrelatiType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiGeneraliType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiPagamentoType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiRiepilogoType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiTrasmissioneType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DettaglioLineeType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DettaglioPagamentoType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.FatturaElettronicaBodyType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.FatturaElettronicaHeaderType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.FatturaElettronicaType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.DatiPortaleFattureType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.EmbeddedXMLType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.InformazioneType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.InformazioniAggiuntiveType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.ResultType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.ServiceResponseType;
import it.csi.siac.sirfelser.model.CassaPrevidenzialeFEL;
import it.csi.siac.sirfelser.model.CausaleFEL;
import it.csi.siac.sirfelser.model.DatiGestionaliFEL;
import it.csi.siac.sirfelser.model.DettaglioOrdineAcquistoFEL;
import it.csi.siac.sirfelser.model.DettaglioPagamentoFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.FattureCollegateFEL;
import it.csi.siac.sirfelser.model.OrdineAcquistoFEL;
import it.csi.siac.sirfelser.model.PagamentoFEL;
import it.csi.siac.sirfelser.model.PortaleFattureFEL;
import it.csi.siac.sirfelser.model.PrestatoreFEL;
import it.csi.siac.sirfelser.model.ProtocolloFEL;
import it.csi.siac.sirfelser.model.RiepilogoBeniFEL;

/**
 * Gestione dell'invio della fattura Elettronica
 * 
 * @author Marchino Alessandro
 * @version 1.0.0
 * @version 1.0.1 - 23/11/2015 - SIAC-2544
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InvioFatturaPAService {
	
	private LogUtil log = new LogUtil(getClass());
	private InvioFatturaPA req;
	private InvioFatturaPAResponse res;
	private Ente ente;
	
	@Autowired
	private EnteDad enteDad;
	@Autowired
	private FatturaFELDad fatturaFELDad;
	
	@Transactional
	public InvioFatturaPAResponse executeService(InvioFatturaPA serviceRequest) {
		this.req = serviceRequest;
		final String methodName = "executeService";
				
		log.info(methodName, "Start.");
		logServiceRequest();
		
		instantiateNewResponse();
		
		try {
			checkServiceParam();
			execute();
		} catch(ServiceParamError spe) {
			log.error(methodName, "Check parametri del servizio terminato con errori", spe);
			setResultByThrowableAndCode("100", spe);
		} catch(UnmappedEnteException uee) {
			log.error(methodName, "Errore di mappatura dell'ente sulla base dati", uee);
			setResultByThrowableAndCode("121", uee);
		} catch (BusinessException be) {
			log.error(methodName, "Errore di business nell'esecuzione del Servizio", be);
			setResultByThrowableAndCode("100", be);
		} catch (RuntimeException re) {
			log.error(methodName, "Errore di runtime nell'esecuzione del Servizio.", re);
			setResultByThrowableAndCode("200", re);
		} catch (Throwable t) {
			log.error(methodName, "Errore di sistema nell'esecuzione del Servizio.", t);
			setResultByThrowableAndCode("200", t);
		}
		logServiceResponse();
		return res;
	}
	
	private void logServiceRequest() {
		EmbeddedXMLType embeddedXMLType = req.getFatturaElettronica();
		req.setFatturaElettronica(null);
		log.logXmlTypeObject(req, "Service Request param (W/O fattura elettronica)");
		req.setFatturaElettronica(embeddedXMLType);
	}
	
	private void logServiceResponse() {
		log.logXmlTypeObject(res, "Service Response param");
	}
	
	private void setResultByThrowableAndCode(String code, Throwable t) {
		impostaRisposta(code, t.getMessage());
		setRollbackOnly();
	}
	
	private void impostaRisposta(String codice, String messaggio) {
		ResultType risultato = new ResultType();
		risultato.setCodice(codice);
		risultato.setMessaggio(messaggio);
		
		ServiceResponseType esito = new ServiceResponseType();
		esito.setResult(risultato);
	
		res.getResult().setEsito(esito);
	}
	
	private void setRollbackOnly() {
		final String methodName = "setRollbackOnly";
		try{
			TransactionStatus currentTransactionStatus = TransactionAspectSupport.currentTransactionStatus();
			currentTransactionStatus.setRollbackOnly();
			log.info(methodName, "Transaction status is marked as rollback only.");
		} catch (NoTransactionException nte){
			log.info(methodName, "Execution is not in transaction. Nothing to rollback. ");
		}
	}
	
	private void checkCondition(boolean condition, Errore errore) throws ServiceParamError{
		checkCondition(condition, errore, true);
	}
	
	private void checkCondition(boolean condition, Errore errore, boolean toThrow) throws ServiceParamError{
		if(!condition){
			if(toThrow){
				throw new ServiceParamError(errore);
			} else {
				addInformazione(errore.getCodice(), errore.getDescrizione());
			}
		}
		
	}

	private void addInformazione(String codice, String descrizione) {
		InformazioniAggiuntiveType informazioniAggiuntiveType = res.getResult().getInformazioniAggiuntive();
		if(informazioniAggiuntiveType == null) {
			informazioniAggiuntiveType = new InformazioniAggiuntiveType();
			res.getResult().setInformazioniAggiuntive(informazioniAggiuntiveType);
		}
		
		InformazioneType informazioneType = new InformazioneType();
		informazioneType.setNome(codice);
		informazioneType.setValore(descrizione);
		
		informazioniAggiuntiveType.getInformazione().add(informazioneType);
	}

	private void instantiateNewResponse() {
		res = new InvioFatturaPAResponse();
		InvioFatturaPAResponse.Result result = new InvioFatturaPAResponse.Result();
		res.setResult(result);
	}
	
	private void checkServiceParam() throws ServiceParamError {
		// Dati portale fatture
		checkDatiPortaleFatture();
		
		// Fattura
		checkDatiFattura();
		
		// Codice ente
		checkCondition(req.getCodiceEnte() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codice ente"), false);
		
		// Informazioni aggiuntive
		checkDatiInformazioniAggiuntive();
		
		// JIRA-2544: non serve piu' gestire gli errori aggiuntivi
	}

	/**
	 * Controlli formali dei DatiPortaleFatture
	 * 
	 * @throws ServiceParamError non viene mai lanciata
	 */
	private void checkDatiPortaleFatture() throws ServiceParamError {
		// SIAC-2544 - Errori non accumulati
		checkCondition(req.getDatiPortaleFatture() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Dati portale fatture"));
		checkCondition(req.getDatiPortaleFatture().getIdentificativoFEL() > 0L, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("identificativo FEL dati portale fatture"));
		checkCondition(req.getDatiPortaleFatture().getIdentificativoSDI() > 0L, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("identificativo SDI dati portale fatture"));
		checkCondition(req.getDatiPortaleFatture().getNomeFileFattura() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("nome file fattura dati portale fatture"));

		// Estremi esito
		checkCondition(req.getDatiPortaleFatture().getEstremiEsito() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("estremi esito dati portale fatture"));
		// Utente
		if(req.getDatiPortaleFatture().getEstremiEsito().getUtente() != null) {
			checkCondition(req.getDatiPortaleFatture().getEstremiEsito().getUtente().getCodice() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codice utente estremi esito dati portale fatture"));
			checkCondition(req.getDatiPortaleFatture().getEstremiEsito().getUtente().getCognome() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("cognome utente estremi esito dati portale fatture"));
		}
		checkCondition(req.getDatiPortaleFatture().getEstremiEsito().getDataOra() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("data ora estremi esito dati portale fatture"));
		checkCondition(req.getDatiPortaleFatture().getEstremiEsito().getStatoFattura() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("stato fattura estremi esito dati portale fatture"));
		checkCondition(req.getDatiPortaleFatture().getEstremiEsito().getStatoFattura() == null
			|| "ACCETTATA".equals(req.getDatiPortaleFatture().getEstremiEsito().getStatoFattura())
			|| "DECORRENZA_TERMINI".equals(req.getDatiPortaleFatture().getEstremiEsito().getStatoFattura())
			|| "RIFIUTATA".equals(req.getDatiPortaleFatture().getEstremiEsito().getStatoFattura()),
			ErroreCore.VALORE_NON_VALIDO.getErrore("stato fattura estremi esito dati portale fatture",
				"puo' assumere i valori ACCETTATA, DECORRENZA_TERMINI o RIFIUTATA (valore fornito: " + req.getDatiPortaleFatture().getEstremiEsito().getStatoFattura() + ")"));
	}
	
	/**
	 * Controlli formali dei DatiFattura
	 * 
	 * @throws ServiceParamError non viene mai lanciata
	 */
	private void checkDatiFattura() throws ServiceParamError {
		checkCondition(req.getFatturaElettronica() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("fattura elettronica"));
		checkCondition(req.getFatturaElettronica().getContent() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("content fattura elettronica"));
	}
	
	/**
	 * Controlli formali dei DatiInformazioniAggiuntive
	 * 
	 * @throws ServiceParamError non viene mai lanciata
	 */
	private void checkDatiInformazioniAggiuntive() throws ServiceParamError {
		if(req.getInformazioniAggiuntive() == null) {
			// Non sono obbligatorie
			return;
		}
		
		checkCondition(req.getInformazioniAggiuntive().getInformazione() != null && !req.getInformazioniAggiuntive().getInformazione().isEmpty(),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("informazione informazioni aggiuntive elettronica"));
		int i = 0;
		for(InformazioneType informazioneType : req.getInformazioniAggiuntive().getInformazione()) {
			checkCondition(informazioneType.getNome() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("nome informazione " + i + " informazioni aggiuntive elettronica"));
			checkCondition(informazioneType.getValore() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("valore informazione " + i + " informazioni aggiuntive elettronica"));
			i++;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Esecuzione del servizio
	 */
	private void execute() {
		final String methodName = "execute";
		if(!"ACCETTATA".equals(req.getDatiPortaleFatture().getEstremiEsito().getStatoFattura()) && !"DECORRENZA_TERMINI".equals(req.getDatiPortaleFatture().getEstremiEsito().getStatoFattura())) {
			impostaRisposta("000", "OK. Fattura scartata.");
			log.info(methodName, "Fattura scartata (" + req.getDatiPortaleFatture().getEstremiEsito().getStatoFattura() +")");
			return;
		}
		log.debug(methodName, "0.ente");
		ente = gestioneEnte();
		log.debug(methodName, "1.unmarshalFattura");
		FatturaElettronicaType fattura = unmarshalFattura(req.getFatturaElettronica());
		log.debug(methodName, "2.checkFattura");
		checkFattura(fattura);
		log.debug(methodName, "3.fatturaElettronicaHeader");
		FatturaElettronicaHeaderType fatturaElettronicaHeader = fattura.getFatturaElettronicaHeader();
		log.debug(methodName, "4.fatturaPA");
		FatturaElettronicaBodyType fatturaPA = fattura.getFatturaElettronicaBody().get(0);
		log.debug(methodName, "5.datiGenerali");
		DatiGeneraliType datiGenerali = fatturaPA.getDatiGenerali();
		log.debug(methodName, "6.datiBeniServizi");
		DatiBeniServiziType datiBeniServizi = fatturaPA.getDatiBeniServizi();
		
		log.info(methodName, "FEL - Tipo documento : " + datiGenerali.getDatiGeneraliDocumento().getTipoDocumento());
		log.info(methodName, "FEL - Numero         : " + datiGenerali.getDatiGeneraliDocumento().getNumero());
		log.info(methodName, "FEL - Data           : " + datiGenerali.getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime());
		log.info(methodName, "FEL - Denominazione  : " + fatturaElettronicaHeader.getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione());
		
		// I clean servono solo come test
		log.debug(methodName, "7.Prestatore");
		PrestatoreFEL prestatoreFEL = gestisciPrestatore(fatturaElettronicaHeader.getCedentePrestatore());
//		fatturaFELDad.clean();
		log.debug(methodName, "8.FatturaElettronica");
		FatturaFEL fatturaFEL = gestisciFatturaFEL(datiGenerali, prestatoreFEL, fatturaElettronicaHeader.getDatiTrasmissione(), datiBeniServizi.getDettaglioLinee());
//		fatturaFELDad.clean();
		log.debug(methodName, "9.gestisciCausale");
		gestisciCausale(datiGenerali.getDatiGeneraliDocumento().getCausale(), fatturaFEL);
//		fatturaFELDad.clean();
		log.debug(methodName, "10.gestisciDatiCassaPrevidenziale");
		gestisciDatiCassaPrevidenziale(datiGenerali.getDatiGeneraliDocumento().getDatiCassaPrevidenziale(), fatturaFEL);
//		fatturaFELDad.clean();
		log.debug(methodName, "11.gestisciFattureCollegate");
		gestisciFattureCollegate(datiGenerali.getDatiFattureCollegate(), fatturaFEL);
//		fatturaFELDad.clean();
		log.debug(methodName, "12.gestisciDatiBeniServizi");
		gestisciDatiBeniServizi(fatturaPA.getDatiBeniServizi().getDatiRiepilogo(), fatturaFEL);
//		fatturaFELDad.clean();
		log.debug(methodName, "13.gestisciDatiGestionali");
		gestisciDatiGestionali(fatturaPA.getDatiBeniServizi().getDettaglioLinee(), fatturaFEL);
//		fatturaFELDad.clean();
		log.debug(methodName, "14.gestisciDatiPagamento");
		gestisciDatiPagamento(fatturaPA.getDatiPagamento(), fatturaFEL);
//		fatturaFELDad.clean();
		log.debug(methodName, "15.gestisciDatiPortaleFatture");
		gestisciDatiPortaleFatture(req.getDatiPortaleFatture(), fatturaFEL);
//		fatturaFELDad.clean();
		log.debug(methodName, "16.gestisciDatiProtocollazione");
		gestisciDatiProtocollazione(req.getInformazioniAggiuntive(), fatturaFEL);
//		fatturaFELDad.clean();
		log.debug(methodName, "17.gestisciOrdineAcquisto");
		gestisciOrdineAcquisto(fatturaPA.getDatiGenerali().getDatiOrdineAcquisto(), fatturaFEL);
//		fatturaFELDad.clean();
		
		//risposta
		String codiceRisposta = calcolaCodiceRisposta();
		impostaRisposta(codiceRisposta, "OK");
		
		log.info(methodName, "Fattura elaborata correttamente: inserita in archivio con id " + fatturaFEL.getIdFattura());
		log.info(methodName, "***** FINE elaborazione fattura elettronica *****");
	}

	/**
	 * Caricamento dell'ente a partire da quello indicato nella request.
	 * 
	 * @return l'ente della request
	 */
	private Ente gestioneEnte() {
		List<Ente> enti = enteDad.getEntiByCodiceAndCodiceSistemaEsterno(req.getCodiceEnte(), SistemaEsterno.FEL);
		if(enti == null || enti.isEmpty()) {
			throw new UnmappedEnteException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Ente", "codice " + req.getCodiceEnte()));
		}
		// Dovrebbe essercene solo uno. Per sicurezza prendo solo il primo
		return enti.get(0);
	}

	/**
	 * Effettua un unmarshal della fattura per ottenerne i dati.
	 * 
	 * @param fatturaXml l'xml della fattura
	 * @return la fattura
	 */
	private FatturaElettronicaType unmarshalFattura(EmbeddedXMLType fatturaXml) {
		final String methodName = "unmarshalFattura";
		byte[] xmlBytes = extractContent(fatturaXml);
		InputStream is = new ByteArrayInputStream(xmlBytes);
		try {
			JAXBContext jc = JAXBContext.newInstance(FatturaElettronicaType.class);
			Unmarshaller u = jc.createUnmarshaller();
			FatturaElettronicaType fattura = (FatturaElettronicaType) u.unmarshal(is);
			log.logXmlTypeObject(fattura, "Fattura elettronica");
			return fattura;
		} catch(JAXBException jaxbe) {
			log.error(methodName, "Errore di unmarshalling della fattura", jaxbe);
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore di unmarshalling della fattura elettronica" + (jaxbe != null ? " (" + jaxbe.getMessage() + ")" : "")));
		}
	}

	/**
	 * Extracts the content from the embedded XML
	 * @param embeddedXMLType the embedded type
	 * @return the bytes comprising the content
	 */
	private byte[] extractContent(EmbeddedXMLType embeddedXMLType) {
		byte[] content;
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Result result = new StreamResult(baos);
			t.transform(embeddedXMLType.getContent(), result);
			content = baos.toByteArray();
		} catch (TransformerException te) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore di trasformazionew della fattura elettronica" + (te != null ? " (" + te.getMessage() + ")" : "")));
		}
		return content;
	}
	
	private void controllaComponenteNotNull(Object componente, String nomeComponente) {
		controllaComponenteValido(componente != null, nomeComponente);
	}
	private void controllaComponenteValido(boolean condition, String nomeComponente) {
		if(!condition) {
			throw new BusinessException(ErroreCore.FORMATO_NON_VALIDO.getErrore(nomeComponente, "non puo' essere vuoto (vedasi xds)"));
		}
	}
	
	private void checkFattura(FatturaElettronicaType fattura) {
		if(fattura == null) {
			throw new BusinessException(ErroreCore.FORMATO_NON_VALIDO.getErrore("FatturaElettronica", "non puo' essere vuota"));
		}
		// Controlli header
		controllaComponenteNotNull(fattura.getFatturaElettronicaHeader(), "FatturaElettronicaHeader");
		FatturaElettronicaHeaderType fatturaElettronicaHeader = fattura.getFatturaElettronicaHeader();
		controllaComponenteNotNull(fatturaElettronicaHeader.getCedentePrestatore(), "CedentePrestatore FatturaElettronicaHeader");
		controllaComponenteNotNull(fatturaElettronicaHeader.getCedentePrestatore().getDatiAnagrafici(), "DatiAnagrafici CedentePrestatore FatturaElettronicaHeader");
		controllaComponenteNotNull(fatturaElettronicaHeader.getCedentePrestatore().getDatiAnagrafici().getAnagrafica(), "Anagrafica DatiAnagrafici CedentePrestatore FatturaElettronicaHeader");
		controllaComponenteNotNull(fatturaElettronicaHeader.getDatiTrasmissione(), "DatiTrasmissione FatturaElettronicaHeader");
		
		
		// Controlli body
		controllaComponenteValido(fattura.getFatturaElettronicaBody() != null && !fattura.getFatturaElettronicaBody().isEmpty() && fattura.getFatturaElettronicaBody().get(0) != null, "FatturaElettronicaBody");
		FatturaElettronicaBodyType fatturaPA = fattura.getFatturaElettronicaBody().get(0);
		controllaComponenteNotNull(fatturaPA.getDatiGenerali(), "DatiGenerali FatturaElettronicaBody");
		controllaComponenteNotNull(fatturaPA.getDatiGenerali().getDatiGeneraliDocumento(), "DatiGeneraliDocumento DatiGenerali FatturaElettronicaBody");
		controllaComponenteNotNull(fatturaPA.getDatiGenerali().getDatiGeneraliDocumento().getTipoDocumento(), "TipoDocumento DatiGeneraliDocumento DatiGenerali FatturaElettronicaBody");
		controllaComponenteNotNull(fatturaPA.getDatiGenerali().getDatiGeneraliDocumento().getNumero(), "Numero DatiGeneraliDocumento DatiGenerali FatturaElettronicaBody");
		controllaComponenteNotNull(fatturaPA.getDatiGenerali().getDatiGeneraliDocumento().getData(), "Data DatiGeneraliDocumento DatiGenerali FatturaElettronicaBody");
		controllaComponenteNotNull(fatturaPA.getDatiBeniServizi(), "DatiBeniServizi FatturaElettronicaBody");
		controllaComponenteNotNull(fatturaPA.getDatiBeniServizi().getDettaglioLinee(), "DettaglioLinee DatiBeniServizi FatturaElettronicaBody");
		controllaComponenteNotNull(fatturaPA.getDatiBeniServizi().getDatiRiepilogo(), "DatiRiepilogo DatiBeniServizi FatturaElettronicaBody");
	}
	
	/**
	 * Gestione del prestatoreFEL.
	 * 
	 * @param cedentePrestatoreType il prestatore
	 * @param fatturaFEL            la fatturaFEL
	 */
	private PrestatoreFEL gestisciPrestatore(CedentePrestatoreType cedentePrestatoreType) {
		final String methodName = "gestionePrestatore";
		
		PrestatoreFEL prestatoreFEL = InvioFatturaPAPrestatoreFELFactory.init(cedentePrestatoreType, ente);
		// Cerco l'eventuale prestatore gia' esistente
		List<PrestatoreFEL> prestatoriFELEsistenti = fatturaFELDad.ricercaPrestatoreFEL(prestatoreFEL);
		prestatoreFEL = InvioFatturaPAPrestatoreFELFactory.cercaPrestatoreEsistente(prestatoriFELEsistenti, prestatoreFEL);
		log.debug(methodName, "prestatoreFEL.idPrestatore: " + prestatoreFEL.getIdPrestatore());
		// Verifiche per l'inserimento
		if (prestatoreFEL.getIdPrestatore() == null) {
			// TODO: verificare se effettivamente enum o codifica. Nel secondo caso, predisporre la ricerca
//			// Verifico Regime fiscale
//			verificaRegimeFiscale(prestatore.getRegimeFiscale());
			
			fatturaFELDad.inserisciPrestatoreFEL(prestatoreFEL);
			
			log.debug(methodName, "Inserito prestatore FEL con id " + prestatoreFEL.getIdPrestatore() + " per ente " + prestatoreFEL.getEnte().getUid());
		}
		return prestatoreFEL;
	}
	
	/**
	 * Gestione della fatturaFEL.
	 * 
	 * @param datiGeneraliType     i dati generali della fattura
	 * @param datiTrasmissioneType i dati di trasmissione
	 * @param dettaglioLineeTypes  i dettagli delle linee
	 * @return la fatturaFEL
	 */
	private FatturaFEL gestisciFatturaFEL(DatiGeneraliType datiGeneraliType, PrestatoreFEL prestatoreFEL, DatiTrasmissioneType datiTrasmissioneType, List<DettaglioLineeType> dettaglioLineeTypes) {
		final String methodName = "gestisciFatturaFEL";
		FatturaFEL fatturaFEL = InvioFatturaPAFatturaFELFactory.init(datiGeneraliType, prestatoreFEL, datiTrasmissioneType, dettaglioLineeTypes, ente);
		
		if (fatturaFEL.getImportoTotaleDocumento() == null) {
			addInformazione("Importo Totale FatturaPA nullo", "ATTENZIONE! Il totale importo della fattura elettronica non e' valorizzato.");
		}
		// TODO: verificare se effettivamente enum o codifica. Nel secondo caso, predisporre la ricerca
//		//verifica Tipo Documento
//		verificaTipoDocumento(fatturaElettronica.getTipoDocumento());
		
		fatturaFELDad.inserisciFatturaFEL(fatturaFEL);
		
		log.debug(methodName, "Inserita fattura FEL con id " + fatturaFEL.getIdFattura() + " per ente " + fatturaFEL.getEnte().getUid());
		
		return fatturaFEL;
	}
	
	/**
	 * Gestione della causaleFEL.
	 * 
	 * @param causali    le descrizioni delle causali
	 * @param fatturaFEL la fattura FEL
	 */
	private void gestisciCausale(List<String> causali, FatturaFEL fatturaFEL) {
		final String methodName = "gestisciCausale";
		
		int progressivo = 0;
		if(causali != null && !causali.isEmpty()) {
			for (String descrizioneCausale : causali) {
				CausaleFEL causaleFEL = InvioFatturaPACausaleFELFactory.init(fatturaFEL, ++progressivo, descrizioneCausale, ente);
				fatturaFELDad.inserisciCausaleFEL(causaleFEL);
				log.debug(methodName, "Inserita causale FEL con progressivo " + causaleFEL.getProgressivo() + " per fattura " + causaleFEL.getFattura().getIdFattura()
						+ " per ente " + causaleFEL.getEnte().getUid());
			}
			
		}
	}
	
	/**
	 * Gestione dei datiCassaPrevidenziale
	 * 
	 * @param datiCassaPrevidenzialeTypes i dati della cassa previdenziale
	 * @param fatturaFEL                  la fattura FEL
	 */
	private void gestisciDatiCassaPrevidenziale(List<DatiCassaPrevidenzialeType> datiCassaPrevidenzialeTypes, FatturaFEL fatturaFEL) {
		final String methodName = "gestisciDatiCassaPrevidenziale";
		int progressivo = 0;
		if(datiCassaPrevidenzialeTypes != null && !datiCassaPrevidenzialeTypes.isEmpty()) {
			for (DatiCassaPrevidenzialeType datiCassaPrevidenziale : datiCassaPrevidenzialeTypes) {
				CassaPrevidenzialeFEL cassaPrevidenzialeFEL = InvioFatturaPACassaPrevidenzialeFELFactory.init(fatturaFEL, ++progressivo, datiCassaPrevidenziale, ente);
				// TODO: verificare se effettivamente enum o codifica. Nel secondo caso, predisporre la ricerca
//				//verifica Tipo Cassa
//				verificaTipoCassa(cassaPrevidenziale.getTipoCassa());
//				//verifica Natura
//				verificaNatura(cassaPrevidenziale.getNatura());
				
				fatturaFELDad.inserisciCassaPrevidenzialeFEL(cassaPrevidenzialeFEL);
				log.debug(methodName, "Inserita cassa previdenziale FEL con progressivo " + cassaPrevidenzialeFEL.getProgressivo() + " per fattura " + cassaPrevidenzialeFEL.getFattura().getIdFattura()
						+ " per ente " + cassaPrevidenzialeFEL.getEnte().getUid());
			}
			
		}
	}
	
	/**
	 * Gestione delle fattureFEL collegate.
	 * 
	 * @param datiDocumentiCorrelatiTypes i dati delle fatture correlate
	 * @param fatturaFEL                  la fattura FEL
	 */
	private void gestisciFattureCollegate(List<DatiDocumentiCorrelatiType> datiDocumentiCorrelatiTypes, FatturaFEL fatturaFEL) {
		final String methodName = "gestisciFattureCollegate";
		int progressivo = 0;
		if(datiDocumentiCorrelatiTypes != null && !datiDocumentiCorrelatiTypes.isEmpty()) {
			for (DatiDocumentiCorrelatiType datiDocumentoCorrelato : datiDocumentiCorrelatiTypes) {
				FattureCollegateFEL fatturaCollegata = InvioFatturaPAFattureCollegateFELFactory.init(fatturaFEL, ++progressivo, datiDocumentoCorrelato, ente);
				
				fatturaFELDad.inserisciFattureCollegateFEL(fatturaCollegata);
				log.debug(methodName, "Inserita fattura collegata FEL con progressivo " + fatturaCollegata.getProgressivo() + " per fattura " + fatturaCollegata.getFattura().getIdFattura()
						+ " per ente " + fatturaCollegata.getEnte().getUid());
			}
			
		}
	}
	
	/**
	 * Gestione dei riepilogoBeniFEL.
	 * 
	 * @param datiRiepilogoTypes i dati di riepilogo
	 * @param fatturaFEL         la fattura FEL
	 */
	private void gestisciDatiBeniServizi(List<DatiRiepilogoType> datiRiepilogoTypes, FatturaFEL fatturaFEL) {
		final String methodName = "gestisciDatiBeniServizi";
		int progressivo = 0;
		if(datiRiepilogoTypes != null && !datiRiepilogoTypes.isEmpty()) {
			for (DatiRiepilogoType datiRiepilogo : datiRiepilogoTypes) {
				RiepilogoBeniFEL riepilogoBeni = InvioFatturaPARiepilogoBeniFELFactory.init(fatturaFEL, ++progressivo, datiRiepilogo, ente);
				// TODO: verificare se effettivamente enum o codifica. Nel secondo caso, predisporre la ricerca
//				//verifica Natura
//				verificaNatura(riepilogoBeni.getNatura());
				
				fatturaFELDad.inserisciRiepilogoBeniFEL(riepilogoBeni);
				log.debug(methodName, "Inserito riepilogo beni FEL con progressivo " + riepilogoBeni.getProgressivo() + " per fattura " + riepilogoBeni.getFattura().getIdFattura()
						+ " per ente " + riepilogoBeni.getEnte().getUid());
			}
			
		}
	}
	
	/**
	 * Gestione dei datiGestionaliFEL.
	 * 
	 * @param dettaglioLineeTypes il dettaglio delle linee
	 * @param fatturaFEL          la fattura
	 */
	private void gestisciDatiGestionali(List<DettaglioLineeType> dettaglioLineeTypes, FatturaFEL fatturaFEL) {
		final String methodName = "gestisciDatiGestionali";
		int progressivo = 0;
		if(dettaglioLineeTypes != null && !dettaglioLineeTypes.isEmpty()) {
			for (DettaglioLineeType dettaglioLinea : dettaglioLineeTypes) {
				if(dettaglioLinea != null) {
					for (AltriDatiGestionaliType altriDatiGestionali : dettaglioLinea.getAltriDatiGestionali()) {
						DatiGestionaliFEL datiGestionali = InvioFatturaPADatiGestionaliFELFactory.init(fatturaFEL, ++progressivo, altriDatiGestionali, ente);
						
						fatturaFELDad.inserisciDatiGestionaliFEL(datiGestionali);
						log.debug(methodName, "Inseriti dati gestionali FEL con progressivo " + datiGestionali.getProgressivo()
								+ " per fattura " + datiGestionali.getFattura().getIdFattura() + " per ente " + datiGestionali.getEnte().getUid());
					}
				}
			}
			
		}
	}
	
	/**
	 * Gestione dei pagamentoFEL.
	 * 
	 * @param datiPagamentoTypes i dati di pagamento
	 * @param fatturaFEL         la fattura
	 */
	private void gestisciDatiPagamento(List<DatiPagamentoType> datiPagamentoTypes, FatturaFEL fatturaFEL) {
		final String methodName = "gestisciDatiPagamento";
		int progressivoPagamento = 0;
		if(datiPagamentoTypes != null && !datiPagamentoTypes.isEmpty()) {
			for (DatiPagamentoType datiPagamentoType : datiPagamentoTypes) {
				PagamentoFEL pagamentoFEL = InvioFatturaPAPagamentoFELFactory.init(fatturaFEL, ++progressivoPagamento, datiPagamentoType, ente);
				
				fatturaFELDad.inserisciPagamentoFEL(pagamentoFEL);
				log.debug(methodName, "Inserito pagamento FEL con progressivo " + pagamentoFEL.getProgressivo() + " per fattura " + pagamentoFEL.getFattura().getIdFattura()
						+ " per ente " + pagamentoFEL.getEnte().getUid());
				
				int progressivoDettaglio = 0;
				for (DettaglioPagamentoType datiDettaglioPagamento : datiPagamentoType.getDettaglioPagamento()) {
					DettaglioPagamentoFEL dettaglioPagamentoFEL = InvioFatturaPAPagamentoFELFactory.initDettaglio(pagamentoFEL, ++progressivoDettaglio, datiDettaglioPagamento, ente);
					
					// TODO: verificare se effettivamente enum o codifica. Nel secondo caso, predisporre la ricerca
//					verificaModalitaPagamento(dettaglioPagamento.getModalitaPagamento());
					
					fatturaFELDad.inserisciDettaglioPagamentoFEL(dettaglioPagamentoFEL);
					log.debug(methodName, "Inserito dettaglio pagamento FEL con progressivo " + dettaglioPagamentoFEL.getProgressivoDettaglio()
							+ " per fattura " + dettaglioPagamentoFEL.getPagamento().getFattura().getIdFattura() + " per ente " + dettaglioPagamentoFEL.getEnte().getUid()
							+ " per pagamento " + dettaglioPagamentoFEL.getPagamento().getProgressivo());
				}
			}
			
		}
	}
	
	/**
	 * Gestione del portaleFattureFEL.
	 * 
	 * @param datiPortaleFattureType i dati del portale
	 * @param fatturaFEL             la fatturaFEL
	 */
	private void gestisciDatiPortaleFatture(DatiPortaleFattureType datiPortaleFattureType, FatturaFEL fatturaFEL) {
		final String methodName = "gestisciDatiPortaleFatture";
		PortaleFattureFEL portaleFatture = InvioFatturaPAPortaleFattureFELFactory.init(fatturaFEL, datiPortaleFattureType, ente);
		
		fatturaFELDad.inserisciPortaleFattureFEL(portaleFatture);
		
		log.debug(methodName, "Inserito portale fattura FEL per fattura " + portaleFatture.getFattura().getIdFattura() + " per ente " + portaleFatture.getEnte().getUid());
//		executeUpdate(new InserisciPortaleFattureDAO(portaleFatture));
	}
	
	/**
	 * Gestione del protocolloFEL.
	 * 
	 * @param informazioniAggiuntiveType le informazioni di protocollazione
	 * @param fatturaFEL                 la fattura
	 */
	private void gestisciDatiProtocollazione(InformazioniAggiuntiveType informazioniAggiuntiveType, FatturaFEL fatturaFEL) {
		final String methodName = "gestisciDatiProtocollazione";
		if (informazioniAggiuntiveType != null) {
			List<InformazioneType> informazioneTypes = informazioniAggiuntiveType.getInformazione();
			ProtocolloFEL protocolloFEL = InvioFatturaPAProtocolloFELFactory.init(fatturaFEL, informazioneTypes, ente);
			fatturaFEL.setProtocolloFEL(protocolloFEL);
			
			fatturaFELDad.inserisciProtocolloFEL(protocolloFEL);
			log.debug(methodName, "Inserito protocollo FEL per fattura " + protocolloFEL.getFattura().getIdFattura() + " per ente " + protocolloFEL.getEnte().getUid());
//			executeUpdate(new InserisciProtocolloDAO(protocollo));
		}
	}
	
	/**
	 * Gestione dell'ordineAcquistoFEL.
	 * 
	 * @param datiDocumentiCorrelatiTypes le informazioni dell'ordine
	 * @param fatturaFEL                  la fattura
	 */
	private void gestisciOrdineAcquisto(List<DatiDocumentiCorrelatiType> datiDocumentiCorrelatiTypes, FatturaFEL fatturaFEL) {
		final String methodName = "gestisciOrdineAcquisto";
		
		Map<String, OrdineAcquistoFEL> ordiniInseriti = new HashMap<String, OrdineAcquistoFEL>();
		
		if(datiDocumentiCorrelatiTypes != null && !datiDocumentiCorrelatiTypes.isEmpty()) {
			for (DatiDocumentiCorrelatiType datiDocumentiCorrelatiType : datiDocumentiCorrelatiTypes) {
				OrdineAcquistoFEL ordineAcquistoFEL = InvioFatturaPAOrdineAcquistoFELFactory.init(fatturaFEL, datiDocumentiCorrelatiType, ente);
				
				String ordineAcquistKey = computeKeyOrdineAcquistoFEL(ordineAcquistoFEL);
				
				if(!ordiniInseriti.containsKey(ordineAcquistKey)) {
					fatturaFELDad.inserisciOrdineAcquistoFEL(ordineAcquistoFEL);
					log.debug(methodName, "Inserito ordine acquisto FEL con numero documento " + ordineAcquistoFEL.getNumeroDocumento() + " per fattura " + ordineAcquistoFEL.getFatturaFEL().getIdFattura()
							+ " per ente " + ordineAcquistoFEL.getEnte().getUid());
					
					
					
					ordiniInseriti.put(ordineAcquistKey, ordineAcquistoFEL);
				} else {
					ordineAcquistoFEL = ordiniInseriti.get(ordineAcquistKey);
					
					log.debug(methodName, "Aggiungo RiferimentoNumeroLinea per ordine acquisto FEL con numero documento " + ordineAcquistoFEL.getNumeroDocumento() + " per fattura " + ordineAcquistoFEL.getFatturaFEL().getIdFattura()
							+ " per ente " + ordineAcquistoFEL.getEnte().getUid());
					
				}
				//ordineAcquistKey
				
				if(datiDocumentiCorrelatiType.getRiferimentoNumeroLinea() != null && !datiDocumentiCorrelatiType.getRiferimentoNumeroLinea().isEmpty()) {
					for(Integer riferimentoNumeroLinea : datiDocumentiCorrelatiType.getRiferimentoNumeroLinea()) {
						DettaglioOrdineAcquistoFEL dettaglioOrdineAcquistoFEL = InvioFatturaPAOrdineAcquistoFELFactory.initDettaglio(ordineAcquistoFEL, riferimentoNumeroLinea, ente);
						
						fatturaFELDad.inserisciDettaglioOrdineAcquistoFEL(dettaglioOrdineAcquistoFEL);
						log.debug(methodName, "Inserito dettaglio ordine acquisto FEL con numero dettaglio " + dettaglioOrdineAcquistoFEL.getNumeroDettaglio()
								+ " per fattura " + dettaglioOrdineAcquistoFEL.getOrdineAcquistoFEL().getFatturaFEL().getIdFattura()
								+ " per ente " + dettaglioOrdineAcquistoFEL.getEnte().getUid()
								+ " per ordine acquisto " + dettaglioOrdineAcquistoFEL.getOrdineAcquistoFEL().getNumeroDocumento());
					}
				}
			}
			
		}
	}

	private String computeKeyOrdineAcquistoFEL(OrdineAcquistoFEL ordineAcquistoFEL) {
		return ordineAcquistoFEL.getEnte().getUid()+
				ordineAcquistoFEL.getFatturaFEL().getIdFattura()+
				ordineAcquistoFEL.getNumeroDocumento();
	}
	
	private String calcolaCodiceRisposta() {
		return res.getResult().getInformazioniAggiuntive() == null
				|| res.getResult().getInformazioniAggiuntive().getInformazione() == null
				|| res.getResult().getInformazioniAggiuntive().getInformazione().isEmpty()
					? "000"
					: "005";
	}
}
