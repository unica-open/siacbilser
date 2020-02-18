/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaPreDocumentoDiEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaPreDocumentoDiEntrataService extends CrudPreDocumentoDiEntrataBaseService<AggiornaPreDocumentoDiEntrata, AggiornaPreDocumentoDiEntrataResponse> {
	
	
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
		
//		checkNotNull(preDoc.getContoCorrente(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conto corrente"));
//		checkCondition(preDoc.getContoCorrente().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conto corrente"),false);
		checkNotNull(preDoc.getCausaleEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("causale"));
		checkCondition(preDoc.getCausaleEntrata().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid causale"),false);
		
		checkNotNull(preDoc.getDataDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data documento predocumento"));
		checkNotNull(preDoc.getDataCompetenza(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data competenza predocumento"));
		checkNotNull(preDoc.getPeriodoCompetenza(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("periodo competenza predocumento"));
		
		if(preDoc.getAccertamento() != null) { //accertamento facoltativo
			checkCondition(preDoc.getAccertamento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid acertamento predocumento"));
			checkCondition(preDoc.getAccertamento().getAnnoMovimento()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno accertamento predocumento"));
			checkNotNull(preDoc.getAccertamento().getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero accertamento predocumento"));
			
		}
		checkCondition(preDoc.getSoggetto() == null || (preDoc.getSoggetto()!=null && preDoc.getSoggetto().getUid()!=0), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto predocumento"));
		checkCondition(preDoc.getAttoAmministrativo() == null || (preDoc.getAttoAmministrativo()!=null && preDoc.getAttoAmministrativo().getUid()!=0), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid provvedimento predocumento"));
		checkCondition(preDoc.getProvvisorioDiCassa() == null ||  
				(preDoc.getProvvisorioDiCassa().getAnno() != null && preDoc.getProvvisorioDiCassa().getNumero() != null) ||
				(preDoc.getProvvisorioDiCassa().getAnno() == null && preDoc.getProvvisorioDiCassa().getNumero() == null),
				 ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero provvisorio di cassa") );
		
		this.gestisciModificaImportoAccertamento = req.isGestisciModificaImportoAccertamento();
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaPreDocumentoDiEntrataResponse executeService(AggiornaPreDocumentoDiEntrata serviceRequest) {
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
		msgOperazione= "aggiornamento";
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
		checkAccertamento();
		checkSubAccertamento();		
		//AGGIUNTi IL 15/06/2015
	    caricaProvvisorioDiCassa();
	    checkProvvisorioDicassaAggiornamento();
		preDocumentoEntrataDad.aggiornaAnagraficaPreDocumento(preDoc);	
		
		StatoOperativoPreDocumento statoOperativoPreDocumento = aggiornaStatoOperativoPreDocumento(preDoc, false);
		preDoc.setStatoOperativoPreDocumento(statoOperativoPreDocumento);	
		
		res.setPreDocumentoEntrata(preDoc);		
	}

}
