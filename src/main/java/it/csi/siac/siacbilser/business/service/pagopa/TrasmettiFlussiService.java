/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.TrasmettiFlussiPagoPARequest;
import it.csi.epay.epaywso.types.ResponseType;
import it.csi.epay.epaywso.types.ResultType;
import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.business.service.pagopa.msg.TrasmettiFlussi;
import it.csi.siac.siacbilser.business.service.pagopa.msg.TrasmettiFlussiResponse;
import it.csi.siac.siacbilser.business.service.pagopa.util.ResultTypeEnum;
import it.csi.siac.siacbilser.integration.dad.PagoPADad;
import it.csi.siac.siacbilser.integration.entity.SiacTFilePagopa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDFilePagopaStatoEnum;
import it.csi.siac.siaccommon.util.XmlUtils;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TrasmettiFlussiService extends ExtendedBaseService<TrasmettiFlussi, TrasmettiFlussiResponse> {

	private static LogSrvUtil log = new LogSrvUtil(TrasmettiFlussiService.class);
	
	@Autowired
	private PagoPADad pagoPADad;
	
	@Override
	protected void checkRichiedente() throws ServiceParamError {
		//Evito il check del richiedente!
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getPagoPARequest(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PagoPA request"));
		checkNotNull(req.getPagoPARequest().getTestataTrasmissioneFlussi(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("testata flusso"));
		checkCondition(StringUtils.isNotBlank(req.getPagoPARequest().getTestataTrasmissioneFlussi().getCFEnteCreditore()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("CF ente creditore"));
		checkNotNull(req.getPagoPARequest().getFlussoRiconciliato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flusso"));
		checkNotBlank(req.getPagoPARequest().getTestataTrasmissioneFlussi().getCFEnteCreditore(), "CF ente creditore");
	}
	
	@Override
	@Transactional
	public TrasmettiFlussiResponse executeService(TrasmettiFlussi serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	@Override
	protected void init() {
		String codiceFiscaleEnte = req.getPagoPARequest().getTestataTrasmissioneFlussi().getCFEnteCreditore();
		
		pagoPADad.initSiacTEnteProprietario(codiceFiscaleEnte);
		pagoPADad.setLoginOperazione(codiceFiscaleEnte);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";  

		TrasmettiFlussiPagoPARequest ppareq = req.getPagoPARequest();
		
		byte[] flussoRiconciliato = ppareq.getFlussoRiconciliato();
		
		SiacTFilePagopa siacTFilePagopa = null;
		ResultType resultType = null; 

		try {
			checkXmlPresente(ppareq.getTestataTrasmissioneFlussi().getIdentificativoFlusso());
			
			siacTFilePagopa = pagoPADad.inserisciFlusso(ppareq.getTestataTrasmissioneFlussi(), flussoRiconciliato);

			XmlUtils.validateXmlWithXsd(this.getClass().getResource("/integ/pagopa/FlussoRiconciliazione.xsd"), flussoRiconciliato);

			resultType = ResultTypeEnum.DEFAULT_RT000_OK.getResultType();
			res.setEsito(Esito.SUCCESSO);
			
		} catch (XmlPresenteException xpe) {
			log.error(methodName, "Errore, xml presente ", xpe);
			resultType = ResultTypeEnum.DEFAULT_RT100_ERRORI_APPLICATIVI.getResultType(xpe.getMessage()); 
			res.setEsito(Esito.FALLIMENTO);
			
		} catch (SAXException saxe) {
			log.error(methodName, "Errore, flusso non valido ", saxe);
			pagoPADad.aggiornaStato(siacTFilePagopa.getUid(), SiacDFilePagopaStatoEnum.RIFIUTATO);
			resultType = ResultTypeEnum.DEFAULT_RT100_ERRORI_APPLICATIVI.getResultType(saxe.getMessage()); 
			res.setEsito(Esito.FALLIMENTO);
			
		} catch (Throwable t) {
			log.error(methodName, "Errore di sistema ", t);
			resultType = ResultTypeEnum.DEFAULT_RT200_ERRORI_DI_SISTEMA.getResultType();
			res.setEsito(Esito.FALLIMENTO);
			
		} finally {
			ResponseType responseType = new ResponseType();
			responseType.setResult(resultType);
			res.setPagoPAResponse(responseType);
		}
	}

	private void checkXmlPresente(String idFlusso) throws XmlPresenteException {
		SiacTFilePagopa siacTFilePagopa = pagoPADad.findSiacTFilePagopaByIdFlusso(idFlusso);
		
		if (siacTFilePagopa != null) {
			throw new XmlPresenteException(String.format("XML IdentificativoFlusso = %s gia' presente in archivio (id = %d)", idFlusso, siacTFilePagopa.getUid()));
		}
	}

	class XmlPresenteException extends Exception {
		public XmlPresenteException(String str) {
			super(str);
		}

		private static final long serialVersionUID = 1L;
	}
//	
//	
//	@Override
//	protected void setRollbackOnly() {
//		
//		
//		if(res.verificatoErroreDiSistema()) {
//			//Errore di natura tecnica
//		} else {
//			//if(Esito.FALLIMENTO.equals(res.getEsito())) {
//				//Errore di natura applicativa bloccante
//			/*} else { 
//				//Errore di natura applicativa non bloccante 
//				// --> In realtà qui non ci arriva se la BusinessException sollevata ha correttamente impostato rollbackOnly = false
//				// --> Quando si solleva una eccezione non bloccante tramite BusinessException bisogna ricordarsi di impostare prima il ResultType!
//				resultType = ResultTypeEnum.RT003.getResultType(res.getErroriMsg());
//			}*/
//		}
//		
//		ResponseType responseType = new ResponseType();
//		responseType.setResult(resultType);
//		res.setResponseType(responseType);
//		
//		super.setRollbackOnly();
//	}

////	/**
////	 * Logga i dati di risposta che arrivano da PCC.
////	 */
////	private void logDatiRispostaElaborazione() {
////		log.logXmlTypeObject(req.getAggiornamentoStatoRichiestaRequest().getRispostaQueryOperazioneContabile(), "RispostaQueryOperazioneContabile");
////		log.logXmlTypeObject(req.getAggiornamentoStatoRichiestaRequest().getRispostaProxyOperazioneContabile(), "ProxyOperazioneContabile");
////		
////		logErroriElaborazione();
////		logErroriTrasmissione();
////	}
//
//	private void logErroriTrasmissione() {
//		List<ErroreTipo> listaErroriTx;
//		try{
//			listaErroriTx = req.getAggiornamentoStatoRichiestaRequest().getRispostaQueryOperazioneContabile().getDatiRisposta().getListaErroreTrasmissione().getErroreTrasmissione();
//		} catch (NullPointerException e){
//			listaErroriTx = new ArrayList<ErroreTipo>();
//		}
//		
//		for(ErroreTipo e : listaErroriTx){
//			log.error("", e.getCodice() + " - " +e.getDescrizione());
//		}
//	}
//
//	private void logErroriElaborazione() {
//		List<ErroreElaborazioneOperazioneTipo> listaErrori;
//		try{
//			listaErrori = req.getAggiornamentoStatoRichiestaRequest().getRispostaQueryOperazioneContabile().getDatiRisposta().getListaErroreElaborazione().getErroreElaborazione();
//		} catch (NullPointerException e){
//			listaErrori = new ArrayList<ErroreElaborazioneOperazioneTipo>();
//		}
//		
//		for(ErroreElaborazioneOperazioneTipo e : listaErrori){
//			log.error("", e.getCodiceEsitoElaborazioneOperazione() + " - " +e.getDescrizioneEsitoElaborazioneOperazione()  + " - tipoOperazione: " + e.getTipoOperazione());
//		}
//	}
//
//	private Date getDataFineElaborazionePCC() {
//		String methodName = "getDataFineElaborazionePCC";
//		try{
//			Date result = req.getAggiornamentoStatoRichiestaRequest().getRispostaQueryOperazioneContabile().getDatiRisposta().getDataFineElaborazione().getTime();
//			log.debug(methodName, "returning: "+ result);
//			return result;
//		} catch (NullPointerException npe){
//			log.debug(methodName, "returning: null!");
//			return null;
//		}
//	}
//	
////	
//	private Set<Integer> ottieniUidsRegistroComunicazioniPCC(String idTransazionePA) {
//		final String methodName = "ottieniUidsRegistroComunicazioniPCC";
//		
//		
//		Set<Integer> result = registroComunicazioniPCCDad.findRegistrazioniUidsByIdTransazionePA(idTransazionePA);
//		log.debug(methodName, "Returning: "+ result);
//		return result;
//		
		
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
//	}

//	private void impostaStatoRegistrazioni(Set<Integer> uidsRegistroComunicazioniPCC, String stato) {
//		registroComunicazioniPCCDad.impostaStatoRegistrazioni(uidsRegistroComunicazioniPCC, stato);
//	}
//	
	
}

