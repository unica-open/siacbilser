/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaStatoPreDocumentoDiEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStatoPreDocumentoDiEntrataService extends CheckedAccountBaseService<AggiornaStatoPreDocumentoDiEntrata, AggiornaStatoPreDocumentoDiEntrataResponse> {

	/** The pre documento entrata dad. */
	@Autowired
	private PreDocumentoEntrataDad preDocumentoEntrataDad;

	/** The ricerca dettaglio pre documento entrata service. */
	@Autowired
	private RicercaDettaglioPreDocumentoEntrataService ricercaDettaglioPreDocumentoEntrataService;

	/** The pre documento. */
	private PreDocumentoEntrata preDocumento;

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		preDocumento = req.getPreDocumentoEntrata();

		checkNotNull(preDocumento, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("preDocumento"));
		checkCondition(preDocumento.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid preDocumento"));
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		preDocumentoEntrataDad.setLoginOperazione(loginOperazione);
		preDocumentoEntrataDad.setEnte(ente);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaStatoPreDocumentoDiEntrataResponse executeService(AggiornaStatoPreDocumentoDiEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		String methodName = "execute";

		if(req.isRicaricaDettaglioPreDocumento()) { 
			//Ricarica il dettaglio del predocumento solo se necessario. 
			//Il chiamante può impostare a false il parametro ricaricaDettaglioPreDocumento in modo da evitare che venga ricaricato il dettaglio dal database.
			//Tale parametro NON può essere impostato nel richiamo esterno dall'app.
			caricaDettaglioPreDocumentoEntrata();
		}		

		StatoOperativoPreDocumento statoNew = determinaStatoOperativoPreDocumento();

		if (!statoNew.equals(preDocumento.getStatoOperativoPreDocumento())) {
			log.info(methodName, "Aggiornamento dello stato da " + preDocumento.getStatoOperativoPreDocumento() + " a " + statoNew);
			preDocumentoEntrataDad.aggiornaStatoPreDocumento(preDocumento.getUid(), statoNew);
			preDocumento.setStatoOperativoPreDocumento(statoNew);

		}

		res.setPreDocumentoEntrata(preDocumento);

	}

	/**
	 * ANNULLATO e DEFINITIVO sono stati finali non modificabili. Questi stati
	 * sono gestiti da operazioni ben definite.
	 * 
	 * In automatico si può solo passare da INCOMPLETO a COMPLETO e viceversa.
	 *
	 * @param preDocumento the pre documento
	 * @return the stato operativo pre documento
	 */

	/**
	 * Determina lo stato attuale del documento analizzando lo stato dei suoi
	 * importi.
	 * 
	 * @param preDocumento
	 * 
	 * @param documentoEntrata
	 * @param totaleQuoteENoteCreditoMenoImportiDaDedurre
	 * @return
	 */
	private StatoOperativoPreDocumento determinaStatoOperativoPreDocumento() {

		if (StatoOperativoPreDocumento.ANNULLATO.equals(preDocumento.getStatoOperativoPreDocumento())) {
			return preDocumento.getStatoOperativoPreDocumento();
		}

		if (isCompleto()) {
			return StatoOperativoPreDocumento.COMPLETO;
		} 
		
//		if(isDefinito()) {
//		return StatoOperativoPreDocumento.DEFINITO;
//	}
		
		if(StatoOperativoPreDocumento.DEFINITO.equals(preDocumento.getStatoOperativoPreDocumento())){
			return StatoOperativoPreDocumento.DEFINITO;
		}
		
		return StatoOperativoPreDocumento.INCOMPLETO;
	}

	private boolean isDefinito() {
		return preDocumentoEntrataDad.hasDocumentiCollegati(preDocumento);
	}

	/**
	 * Carica dettaglio pre documento entrata.
	 */
	private void caricaDettaglioPreDocumentoEntrata() {
		RicercaDettaglioPreDocumentoEntrata req = new RicercaDettaglioPreDocumentoEntrata();
		req.setDataOra(new Date());
		req.setPreDocumentoEntrata(preDocumento);
		req.setRichiedente(this.req.getRichiedente());
		RicercaDettaglioPreDocumentoEntrataResponse res = executeExternalServiceSuccess(ricercaDettaglioPreDocumentoEntrataService, req);
		preDocumento = res.getPreDocumentoEntrata();
	}

	/*
	 * COMPLETO
		devono essere attribuiti direttamente come informazioni del predocumento le imputazioni contabili minime, ovvero devono essere passate fra i parametri in input le seguenti informazioni
		Capitolo Entrata (Mod BIL) o Accertamento/SubAccertamento (Mod FIN)
		Soggetto ( Mod FIN)
	 */
	/**
	 * Controlli per verificare se è completo.
	 *
	 * @return true, if is completo
	 */
	private boolean isCompleto() {
		String methodName = "isCompleto";
		log.debug(methodName, "Verifica completezza del PreDocumento con uid " + preDocumento.getUid() + ".");
		
		boolean subAccertamento = preDocumento.getAccertamento() != null && preDocumento.getAccertamento().getUid() != 0;
		boolean accertamento= preDocumento.getAccertamento() != null && preDocumento.getAccertamento().getUid() != 0;
		// Non e' piu' necessario per il completamento
//		boolean capitolo = preDocumento.getCapitoloEntrataGestione() != null && preDocumento.getCapitoloEntrataGestione().getUid() != 0;
		//SIAC-4868
		boolean azioneConsentita = isAzioneConsentita(AzioneConsentitaEnum.PREDOCUMENTO_ENTRATA_COMPLETA_SENZA_ACCERTAMENTO.getNomeAzione());
		boolean soggetto = preDocumento.getSoggetto() != null && preDocumento.getSoggetto().getUid() != 0;
		log.debug(methodName, "subaccertamento presente? " + subAccertamento + "accertamento presente? " + accertamento + "azioneConsentita presente? " + azioneConsentita + "soggetto presente? " + soggetto);
		return (azioneConsentita && soggetto) || (!azioneConsentita && (subAccertamento || accertamento) && soggetto);
	}

}
