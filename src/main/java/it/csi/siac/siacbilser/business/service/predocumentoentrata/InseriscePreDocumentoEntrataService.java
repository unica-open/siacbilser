/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

// TODO: Auto-generated Javadoc
/**
 * The Class InseriscePreDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InseriscePreDocumentoEntrataService extends CrudPreDocumentoDiEntrataBaseService<InseriscePreDocumentoEntrata, InseriscePreDocumentoEntrataResponse> {
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		preDoc = req.getPreDocumentoEntrata();
		
		checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento"));
		
		checkNotNull(preDoc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(preDoc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(req.getRichiedente().getAccount(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("account richiedente"));
		checkNotNull(req.getRichiedente().getAccount().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente account rihiedente"));
		checkCondition(req.getRichiedente().getAccount().getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente account rihiedente"));
		
		checkNotNull(preDoc.getStatoOperativoPreDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo predocumento"));
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"),false);
		bilancio = req.getBilancio();
		
		checkNotNull(preDoc.getDataDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data documento predocumento"));
		checkNotNull(preDoc.getDataCompetenza(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data competenza predocumento"));
		checkNotNull(preDoc.getPeriodoCompetenza(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("periodo competenza predocumento"));
		
		if(preDoc.getAccertamento() != null) { //impegno facoltativo
			//checkCondition(preDoc.getAccertamento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid acertamento predocumento"));
			checkCondition(preDoc.getAccertamento().getAnnoMovimento()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno accertamento predocumento"));
			checkNotNull(preDoc.getAccertamento().getNumeroBigDecimal(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero accertamento predocumento"));
		}
		
		checkCondition(preDoc.getProvvisorioDiCassa() == null ||  
				(preDoc.getProvvisorioDiCassa().getAnno() != null && preDoc.getProvvisorioDiCassa().getNumero() != null) ||
				(preDoc.getProvvisorioDiCassa().getAnno() == null && preDoc.getProvvisorioDiCassa().getNumero() == null),
				 ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero provvisorio di cassa") );
		
		checkCondition(preDoc.getSoggetto() == null || (preDoc.getSoggetto()!=null && preDoc.getSoggetto().getUid()!=0), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto predocumento"));
		checkCondition(preDoc.getAttoAmministrativo() == null || (preDoc.getAttoAmministrativo()!=null && preDoc.getAttoAmministrativo().getUid()!=0), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid provvedimento predocumento"));
		
		checkNotNull(preDoc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo predocumento"));
//		checkCondition(preDoc.getImporto().compareTo(BigDecimal.ZERO) > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo predocumento a 0"));
		
		this.gestisciModificaImportoAccertamento = req.isGestisciModificaImportoAccertamento();
	}	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public InseriscePreDocumentoEntrataResponse executeService(InseriscePreDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		preDocumentoEntrataDad.setLoginOperazione(loginOperazione);
		preDocumentoEntrataDad.setEnte(preDoc.getEnte());
		
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
		checkCapitolo();
		checkCongruenzaSoggettoIncasso();
		checkProvvedimento();
		caricaAccertamentoESubAccertemanto();
		
		//SIAC-6780
		checkAccertamento(req.isInserimentoPredocDaCollegaDocumento());
		checkSubAccertamento(req.isInserimentoPredocDaCollegaDocumento());
		
		//AGGIUNTi IL 11/06/2015
		caricaProvvisorioDiCassa();
		checkProvvisorioDicassaInserimento();
		// SIAC-5001
		gestisciElenco();

		Integer numero = preDocumentoEntrataDad.staccaNumeroPreDocumento();
		
		//SIAC-6780
		setDescrizioneAndStatoOperativoIfNuovoPredocFromCompletaDefinisci();
		//
		
		preDoc.setNumero(numero);
		
		//SIAC-7680
		
		preDocumentoEntrataDad.inserisciAnagraficaPreDocumento(preDoc);	
		
		StatoOperativoPreDocumento statoOperativoPreDocumento = null;
		
		statoOperativoPreDocumento = aggiornaStatoOperativoPreDocumento(preDoc, false);	
		
		preDoc.setStatoOperativoPreDocumento(statoOperativoPreDocumento);
		
		res.setPreDocumentoEntrata(preDoc);		
	}

	@Override
	protected void chekProvvisorioCassaRegolarizzazioneInserimento(ProvvisorioDiCassa provvisorioDiCassa, String keyProvvisorio) {
		if(req.isInserimentoPredocDaCollegaDocumento()) {
			return;
		}
		super.chekProvvisorioCassaRegolarizzazioneInserimento(provvisorioDiCassa, keyProvvisorio);
	}

	/**
	 * Controllo della descrizione e stato operativo per il nuovo inserimento
	 */
	private void setDescrizioneAndStatoOperativoIfNuovoPredocFromCompletaDefinisci() {
		//se il predocumento viene da collega documento devo passare il numero del predocPadre nella descrizione prima che venga sostituito
		if(req.isInserimentoPredocDaCollegaDocumento() && !StringUtils.isBlank(preDoc.getDescrizione())) {
			preDoc.setDescrizione(preDoc.getDescrizione() + " - quota parte del predoc. N. " + preDoc.getNumero());	
		} else if(req.isInserimentoPredocDaCollegaDocumento() && StringUtils.isBlank(preDoc.getDescrizione())) {
			//se non ha una descrizione devo comunque metterla pur di mantenerlo tracciabile
			preDoc.setDescrizione("quota parte del predoc. N. " + preDoc.getNumero());
		}
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
}
 