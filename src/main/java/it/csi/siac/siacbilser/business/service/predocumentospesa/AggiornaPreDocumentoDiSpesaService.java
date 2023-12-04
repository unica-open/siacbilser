/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaPreDocumentoDiSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaPreDocumentoDiSpesaService extends  CrudPreDocumentoDiSpesaBaseService<AggiornaPreDocumentoDiSpesa, AggiornaPreDocumentoDiSpesaResponse> {
	

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		preDoc = req.getPreDocumentoSpesa();
		
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
		
		checkNotNull(preDoc.getCausaleSpesa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("causale predocumento"));	
		checkCondition(preDoc.getCausaleSpesa().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid causale predocumento"),false);
		checkNotNull(preDoc.getContoTesoreria(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conto tesoreria"));
		checkCondition(preDoc.getContoTesoreria().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid conto tesoreria"));
		
		if(preDoc.getImpegno() != null) { //impegno facoltativo
			checkCondition(preDoc.getImpegno().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid impegno predocumento"));
			checkCondition(preDoc.getImpegno().getAnnoMovimento()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno impegno predocumento"));
			checkNotNull(preDoc.getImpegno().getNumeroBigDecimal(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero impegno predocumento"));
			
		}
		
		//aggiunto il 16/06/2015
		//provvisorio di cassa
		checkCondition(preDoc.getProvvisorioDiCassa() == null ||  
				(preDoc.getProvvisorioDiCassa().getAnno() != null && preDoc.getProvvisorioDiCassa().getNumero() != null) ||
				(preDoc.getProvvisorioDiCassa().getAnno() == null && preDoc.getProvvisorioDiCassa().getNumero() == null),
				 ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero provvisorio di cassa") );
		
		checkCondition(preDoc.getSoggetto() == null || (preDoc.getSoggetto()!=null && preDoc.getSoggetto().getUid()!=0), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto predocumento"));
		checkCondition(preDoc.getAttoAmministrativo() == null || (preDoc.getAttoAmministrativo()!=null && preDoc.getAttoAmministrativo().getUid()!=0), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid provvedimento predocumento"));
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaPreDocumentoDiSpesaResponse executeService(AggiornaPreDocumentoDiSpesa serviceRequest) {
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
		msgOperazione= "aggiornamento";
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
		//aggiunti il 16/06/2015Ra
	    caricaProvvisorioDiCassa();
	    checkProvvisorioDicassaAggiornamento();
	    
		preDocumentoSpesaDad.aggiornaAnagraficaPreDocumento(preDoc);		
		
		StatoOperativoPreDocumento statoOperativoPreDocumento = aggiornaStatoOperativoPreDocumento(preDoc, false);
		preDoc.setStatoOperativoPreDocumento(statoOperativoPreDocumento);	
		
		res.setPreDocumentoSpesa(preDoc);		
	}
	
	


}
