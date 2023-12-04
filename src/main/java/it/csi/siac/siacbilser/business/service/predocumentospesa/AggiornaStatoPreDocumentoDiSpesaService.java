/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaStatoPreDocumentoDiSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStatoPreDocumentoDiSpesaService extends CrudPreDocumentoDiSpesaBaseService<AggiornaStatoPreDocumentoDiSpesa, AggiornaStatoPreDocumentoDiSpesaResponse> {
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		preDoc = req.getPreDocumentoSpesa();

		checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("preDoc"));
		checkCondition(preDoc.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid preDoc"));
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		preDocumentoSpesaDad.setLoginOperazione(loginOperazione);		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaStatoPreDocumentoDiSpesaResponse executeService(AggiornaStatoPreDocumentoDiSpesa serviceRequest) {
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
			caricaDettaglioPreDocumentoSpesa();
		}

		StatoOperativoPreDocumento statoNew = determinaStatoOperativoPreDocumento(preDoc);

		if (!statoNew.equals(preDoc.getStatoOperativoPreDocumento())) {			
			log.info(methodName, "Aggiornamento dello stato da "+preDoc.getStatoOperativoPreDocumento()+ " a "+statoNew);
			preDocumentoSpesaDad.aggiornaStatoPreDocumento(preDoc.getUid(), statoNew);
			preDoc.setStatoOperativoPreDocumento(statoNew);
			
		}
		
		res.setPreDocumentoSpesa(preDoc);

	}

	
/**
 * ANNULLATO e DEFINITIVO sono stati finali non modificabili.
 * Questi stati sono gestiti da operazioni ben definite.
 * 
 * In automatico si può solo passare da INCOMPLETO a COMPLETO e viceversa.
 *
 * @param preDocumento the pre documento
 * @return the stato operativo pre documento
 */
	
	/**
	 * Determina lo stato attuale del documento analizzando lo stato dei suoi
	 * importi.
	 * @param preDoc 
	 * 
	 * @param documentoSpesa
	 * @param totaleQuoteENoteCreditoMenoImportiDaDedurre
	 * @return
	 */
	private StatoOperativoPreDocumento determinaStatoOperativoPreDocumento(PreDocumentoSpesa preDocumento) {

		if (StatoOperativoPreDocumento.ANNULLATO.equals(preDocumento.getStatoOperativoPreDocumento())) {
			return preDocumento.getStatoOperativoPreDocumento();
		}

		if (isCompleto()) {
			return StatoOperativoPreDocumento.COMPLETO;
		} 
		
		if(StatoOperativoPreDocumento.DEFINITO.equals(preDocumento.getStatoOperativoPreDocumento())){
			return StatoOperativoPreDocumento.DEFINITO;
		}else {
			return StatoOperativoPreDocumento.INCOMPLETO;
		}
	}



	/*
	COMPLETO
	devono esistere attribuiti sia il Conto del tesoriere (Conto del tesoriere Mod FIN) che la causale di spesa (Causale Spesa Mod FIN); 
	inoltre devono essere attribuiti direttamente come informazioni del predocumento le imputazioni contabili minime, ovvero devono essere passate fra i parametri in input le seguenti informazioni
	Impegno/SubImpegno (Mod FIN)
	Soggetto ( Mod FIN)
	ModalitaPagamentoSoggetto (Mod FIN)  e eventuale SedeSecondariaSoggetto (Mod FIN)
	 */
	/**
	 * Controlli per verificare se è completo.
	 *
	 * @return true, if is completo
	 */
	private boolean isCompleto() {
		String methodName = "isCompleto";
		log.debug(methodName, "Verifica completezza del PreDocumento con uid " + preDoc.getUid() + ".");
			
		//definito il Conto del tesoriere (Conto del tesoriere Mod FIN)
		if (preDoc.getContoTesoreria() == null || preDoc.getContoTesoreria().getUid() == 0) {
			log.debug(methodName, "Lo stato del preDoc è INCOMPLETO. (Conto del tesoriere)");
			return false;
		}
		
		// definito  la causale di spesa (Causale Spesa Mod FIN)
		if (preDoc.getCausaleSpesa() == null || preDoc.getCausaleSpesa().getUid() == 0) {
			log.debug(methodName, "Lo stato del preDoc è INCOMPLETO. (causale di spesa)");
			return false;
		}
		
		if ( (preDoc.getImpegno() == null || preDoc.getImpegno().getUid() == 0) && (preDoc.getSubImpegno() == null || preDoc.getSubImpegno().getUid() == 0) ) {			
			log.debug(methodName, "Lo stato del preDoc è INCOMPLETO. (Impegno/SubImpegno)");
			return false;
		}
		
		if( preDoc.getSoggetto() == null || preDoc.getSoggetto().getUid() == 0){
			log.debug(methodName, "Lo stato del preDoc è INCOMPLETO. (Soggetto)");
			return false;
		}
		
		if( preDoc.getModalitaPagamentoSoggetto() == null || preDoc.getModalitaPagamentoSoggetto().getUid() == 0){
			log.debug(methodName, "Lo stato del preDoc è INCOMPLETO. (ModalitaPagamentoSoggetto)");
			return false;
		}
			
			
	//		if(preDoc.getModalitaPagamentoSoggetto().getUid() != 0){
	//			soggettoDad.popolaStatoModPag(preDoc.getModalitaPagamentoSoggetto());
	//		}
	//		// ModalitaPagamentoSoggetto (Mod FIN) in stato valido con dataScadenza > data corrente.
	//		if ( !( "VALIDO".equalsIgnoreCase(preDoc.getModalitaPagamentoSoggetto().getCodiceStatoModalitaPagamento()) &&
	//				   ( preDoc.getModalitaPagamentoSoggetto().getDataFineValidita() == null ||		
	//				     (new Date()).before(preDoc.getModalitaPagamentoSoggetto().getDataFineValidita()) 
	//				   ) 
	//			  ) ) {
	//			log.debug(methodName, "Lo stato del preDoc è INCOMPLETO. (ModalitaPagamentoSoggetto)");
	//			return false;
	//		}
			
	//		// Soggetto (Mod FIN) in stato diverso da ANNULLATO e BLOCCATO
	//		if (StatoOperativoAnagrafica.ANNULLATO.equals(preDoc.getSoggetto().getStatoOperativo()) || 
	//				StatoOperativoAnagrafica.BLOCCATO.equals(preDoc.getSoggetto().getStatoOperativo())) {
	//			log.debug(methodName, "Lo stato del preDoc è INCOMPLETO. (Soggetto)");
	//			return false;
	//		}
			
			// Impegno/SubImpegno (Mod FIN) in stato DEFINITIVO condictio sine qua non
	//		if (!"D".equalsIgnoreCase(preDoc.getImpegno().getStatoOperativoMovimentoGestioneSpesa())) {
	//			if (!"D".equalsIgnoreCase(preDoc.getSubImpegno().getStatoOperativoMovimentoGestioneSpesa())) {
	//				log.debug(methodName, "Lo stato del preDoc è INCOMPLETO. (Impegno/SubImpegno)");
	//				return false;
	//			}
	//		}
			
	//		// Un provvedimento in stato VALIDO
	//		if (StatoOperativoAtti.ANNULLATO.equals(preDoc.getAttoAmministrativo().getStatoOperativoAtti())) {
	//			log.debug(methodName, "Lo stato del preDoc è INCOMPLETO. (provvedimento)");
	//			return false;
	//		}
			
	//		// Descrizione del Predocumento deve essere valorizzata 	
	//		if (preDoc.getDescrizione().isEmpty()) {
	//			log.debug(methodName, "Lo stato del preDoc è INCOMPLETO. (Descrizione)");
	//			return false;
	//		}
		
		log.debug(methodName,"Lo stato del preDoc è COMPLETO.");	
		return true;
		
	}



}
