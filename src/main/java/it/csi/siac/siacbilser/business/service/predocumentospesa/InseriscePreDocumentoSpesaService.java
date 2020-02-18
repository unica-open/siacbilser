/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.allegatoatto.InserisceElencoService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Inserimento di un PreDocumento.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InseriscePreDocumentoSpesaService extends CrudPreDocumentoDiSpesaBaseService<InseriscePreDocumentoSpesa, InseriscePreDocumentoSpesaResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		preDoc = req.getPreDocumentoSpesa();
		
		checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento"));
		
		checkNotNull(preDoc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente predocumento"));
		checkCondition(preDoc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente predocumento"),false);
		
		checkNotNull(req.getRichiedente().getAccount(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("account richiedente"));
		checkNotNull(req.getRichiedente().getAccount().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente account rihiedente"));
		checkCondition(req.getRichiedente().getAccount().getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente account rihiedente"));
		
		checkNotNull(preDoc.getStatoOperativoPreDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo predocumento"));
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"),false);
		bilancio = req.getBilancio();
		
		checkNotNull(preDoc.getDataDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data documento predocumento"),false);
		checkNotNull(preDoc.getDataCompetenza(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data competenza predocumento"),false);
		checkNotNull(preDoc.getPeriodoCompetenza(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("periodo competenza predocumento"),false);
		
		checkNotNull(preDoc.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione predocumento"),false);
		
		checkNotNull(preDoc.getCausaleSpesa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("causale predocumento"));	
		checkCondition(preDoc.getCausaleSpesa().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid causale predocumento"),false);
		checkNotNull(preDoc.getContoTesoreria(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conto tesoreria"));
		checkCondition(preDoc.getContoTesoreria().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid conto tesoreria"));
		
		if(preDoc.getImpegno() != null) { //impegno facoltativo
			checkCondition(preDoc.getImpegno().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid impegno predocumento"));
			checkCondition(preDoc.getImpegno().getAnnoMovimento()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno impegno predocumento"));
			checkNotNull(preDoc.getImpegno().getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero impegno predocumento"));
			
		}
		//aggiunto il 16/06/2015
		//provvisorio di cassa
		checkCondition(preDoc.getProvvisorioDiCassa() == null ||  
				(preDoc.getProvvisorioDiCassa().getAnno() != null && preDoc.getProvvisorioDiCassa().getNumero() != null) ||
				(preDoc.getProvvisorioDiCassa().getAnno() == null && preDoc.getProvvisorioDiCassa().getNumero() == null),
				 ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero provvisorio di cassa") );
        // numero mutuo ---> voceMutuo
		checkCondition(preDoc.getVoceMutuo() == null || StringUtils.isBlank(preDoc.getVoceMutuo().getNumeroMutuo())
				|| (preDoc.getImpegno() != null && preDoc.getImpegno().getNumero() != null && preDoc.getImpegno().getAnnoMovimento() != 0),
				ErroreFin.IMPEGNO_FINANZIATO_DA_MUTUO.getErrore());
		checkCondition(preDoc.getSoggetto() == null || (preDoc.getSoggetto()!=null && preDoc.getSoggetto().getUid()!=0), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto predocumento"));
		checkCondition(preDoc.getAttoAmministrativo() == null || (preDoc.getAttoAmministrativo()!=null && preDoc.getAttoAmministrativo().getUid()!=0), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid provvedimento predocumento"));
		
		checkNotNull(preDoc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo predocumento"));
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public InseriscePreDocumentoSpesaResponse executeService(InseriscePreDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		preDocumentoSpesaDad.setLoginOperazione(loginOperazione);
		preDocumentoSpesaDad.setEnte(preDoc.getEnte());
		
		//inizializzo msgOperazione per i messaggi di errore
		msgOperazione= "inserimento";
		
		caricaBilancio();
	} 

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {

		checkSoggetto();
		checkCongruenzaSoggettoPagamento();
		checkProvvedimento();
		checkImpegno();
		checkCapitolo();
		//AGGIUNTi IL 16/06/2015
		caricaProvvisorioDiCassa();
		checkProvvisorioDicassaInserimento();
		// SIAC-5001
		gestisciElenco();
		
		Integer numero = preDocumentoSpesaDad.staccaNumeroPreDocumento();
		preDoc.setNumero(numero);
		
		preDocumentoSpesaDad.inserisciAnagraficaPreDocumento(preDoc);		
		
		StatoOperativoPreDocumento statoOperativoPreDocumento = aggiornaStatoOperativoPreDocumento(preDoc, false);
		preDoc.setStatoOperativoPreDocumento(statoOperativoPreDocumento);
		
		res.setPreDocumentoSpesa(preDoc);		
	}

	
	
	/**
	 * Gestione dell'elenco
	 */
	private void gestisciElenco() {
		final String methodName = "gestisciElenco";
		if(!req.isInserisciElenco()) {
			// Se non devo inserire l'elenco, per sicurezza lo sbianco
			log.debug(methodName, "Elenco documenti allegato non da inserire");
			preDoc.setElencoDocumentiAllegato(null);
			return;
		}
		if(preDoc.getElencoDocumentiAllegato() != null && preDoc.getElencoDocumentiAllegato().getUid() != 0) {
			// Se non devo inserire l'elenco, ovvero devo inserirlo ma ne ho gia' uno popolato, salto il controllo
			log.debug(methodName, "Elenco documenti allegato gia' inserito con uid " + preDoc.getElencoDocumentiAllegato().getUid()
					+ ", salto l'inserimento");
			return;
		}
		// Inserisco l'elenco
		InserisceElenco reqIES = new InserisceElenco();
		reqIES.setDataOra(new Date());
		reqIES.setRichiedente(req.getRichiedente());
		reqIES.setBilancio(bilancio);
		reqIES.setAccettaElencoSenzaSubdocumenti(true);
		
		ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
		reqIES.setElencoDocumentiAllegato(eda);
		
		eda.setEnte(ente);
		eda.setAnno(bilancio.getAnno());
		
		InserisceElencoResponse resIES = serviceExecutor.executeServiceSuccess(InserisceElencoService.class, reqIES);
		// Prendo l'elenco e lo associo al predoc
		eda = resIES.getElencoDocumentiAllegato();
		preDoc.setElencoDocumentiAllegato(eda);
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.predocumentospesa.CrudPreDocumentoDiSpesaBaseService#checkSoggetto()
	 */
	@Override
	protected void checkSoggetto() {
		if (req.isSkipCheckSoggetto()) {
			return;
		}
		
		super.checkSoggetto();
	}
	

}
