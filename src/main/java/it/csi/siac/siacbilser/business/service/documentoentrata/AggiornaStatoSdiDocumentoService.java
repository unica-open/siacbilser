/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.fel.eis.feltobilancio.EsitoFatturaAttivaRequest;
import it.csi.fel.eis.feltobilancio.TestataEsitoFatturaAttivaType;
import it.csi.fel.eis.types.FatturaType;
import it.csi.fel.eis.types.ResponseType;
import it.csi.fel.eis.types.ResultType;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoSDIFel;

/**
 * The Class AggiornaStatoSdiDocumentoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStatoSdiDocumentoService { //extends CheckedAccountBaseService<AggiornaStatoSdiDocumento, AggiornaStatoSdiDocumentoResponse> {

	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	
	private LogUtil log = new LogUtil(getClass());


	@Transactional
	public ResponseType executeService(EsitoFatturaAttivaRequest serviceRequest) {
		log.info("executeService", "chiamata!");

		ResponseType response = new ResponseType();
		
		try {
			checkServiceParam(serviceRequest);
			execute(serviceRequest, response);
			
		} catch (ServiceParamError e) {
			impostaRisposta("KO",e.getMessage(),response);
		}
		
		return response;
	}

	private void checkServiceParam(EsitoFatturaAttivaRequest serviceRequest) throws ServiceParamError {
		log.info("checkServiceParam", "chiamata!");
		
		// verificare input
		checkStatoSDI(serviceRequest);
		
		checkUID(serviceRequest);
	}
	
	
	private void execute(EsitoFatturaAttivaRequest serviceRequest, ResponseType response) {
		// TODO Auto-generated method stub
		log.info("execute", "chiamata!");
		
		int statoFT =  serviceRequest.getStatoElaborazioneFattura();
		String esito = serviceRequest.getDettaglioEsitoElaborazione();
		
		/*String statoSdi = "";
		if(statoFT.name() == statoFT.CONSEGNATO.name())
			statoSdi = "2"; // Accettata/Consegnata
		else if(statoFT.name() == statoFT.IN_ERRORE.name())
			statoSdi =  "5"; // Scartata
		else if(statoFT.name() == statoFT.DA_CONSEGNARE.name())
			statoSdi =  "1"; // Scartata
		else {
			impostaRisposta("KO","idDocumento non trovato", response);
			return;
		}*/
			
		TestataEsitoFatturaAttivaType tEFA =  serviceRequest.getTestataInvioFatturaAttiva(); 
		
		FatturaType faType = tEFA.getDatiFattura();
		
		Integer uid = new Integer(faType.getIdFatturaBilancio());
		
		// verifica se esiste
		DocumentoEntrata docEnt = documentoEntrataDad.findDocumentoEntrataByIdMinimal(uid);
	
		if(docEnt != null) {
			documentoEntrataDad.aggiornaStatoSDIDocumentoEntrata(uid, StatoSDIFel.getCodiceContabilia(statoFT),esito);
			impostaRisposta("OK","Aggiornamento effettuato con successo", response);
		}
		else {
			impostaRisposta("KO","idDocumento non trovato", response);
		}

	}
	
	private void checkStatoSDI(EsitoFatturaAttivaRequest serviceRequest) throws ServiceParamError{
		
		int statoFT =  serviceRequest.getStatoElaborazioneFattura();
		
		if(StatoSDIFel.getCodiceContabilia(statoFT).equalsIgnoreCase("")){
			Errore err = ErroreCore.PARAMETRO_ERRATO.getErrore("Stato sdi non riconosciuto");
			throw new ServiceParamError(err);
		}
		
	}

	private void checkUID(EsitoFatturaAttivaRequest req) throws ServiceParamError{
		
		TestataEsitoFatturaAttivaType tEFA =  req.getTestataInvioFatturaAttiva();
		
		FatturaType faType = tEFA.getDatiFattura();
		
		if((faType == null) || (faType.getIdFatturaBilancio() == null)) {
			Errore err = ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Id documento");
			throw new ServiceParamError(err);
		}

		String idDoc =	faType.getIdFatturaBilancio();
			
		if((idDoc == null) || (idDoc.equalsIgnoreCase(""))) {
			Errore err = ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Id documento");
			throw new ServiceParamError(err);
		}
		
		// verifica se numerico
		
	}
	
	private void impostaRisposta(String esito, String messaggio, ResponseType response) {
		
		ResultType res = new ResultType();
		res.setCodice(esito);
		res.setMessaggio(messaggio);
		response.setResult(res);
	}

}
