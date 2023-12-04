/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.ProvvisorioBilDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaProvvisorioQuoteSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaProvvisorioQuoteSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * Aggiorna le quote di spesa associando il provvisorio
 *
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssociaProvvisorioQuoteSpesaService extends CrudDocumentoDiSpesaBaseService<AssociaProvvisorioQuoteSpesa, AssociaProvvisorioQuoteSpesaResponse> {
	
	@Autowired
	private SubdocumentoDad subdocumentoDad;
	@Autowired
	private ProvvisorioBilDad provvisorioBilDad;
	
	private ProvvisorioDiCassa provvisorio;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getProvvisorioDiCassa(), "provvisorio");
		provvisorio = req.getProvvisorioDiCassa();
		
		checkNotNull(req.getListaQuote(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco subdocumenti"));
		for(SubdocumentoSpesa subdoc : req.getListaQuote()){
			checkEntita(subdoc, "subdocumento di spesa");
		}
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		subdocumentoDad.setEnte(ente);
		subdocumentoDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AssociaProvvisorioQuoteSpesaResponse executeService(AssociaProvvisorioQuoteSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkQuoteAggiornabili(); 
		checkImportoDaRegolarizzare();
		for(SubdocumentoSpesa subdoc : req.getListaQuote()){
			subdocumentoDad.aggiornaProvvisorioDiCassa(subdoc.getUid(), provvisorio);
			subdoc.setProvvisorioCassa(provvisorio);
			res.addQuota(subdoc);
		}
	
	}

	private void checkQuoteAggiornabili() {
		// TODO Auto-generated method stub
		// SERVE DAVVERO? dall'app sono filtrate gia' nella ricerca
		//quote non associate ad ordinativo non annullato e non collegate a documento annullato/emesso
	}

	private void checkImportoDaRegolarizzare() {
		String methodName = "checkImportoDaRegolarizzare";
		BigDecimal importoQuote = subdocumentoDad.getImportoSubdocumenti(req.getListaQuote()); 
		BigDecimal importoDaDedurreQuote = subdocumentoDad.getImportoDaDedurreSubdocumenti(req.getListaQuote()); 
		BigDecimal importoDaRegolarizzare = provvisorioBilDad.calcolaImportoDaRegolarizzareProvvisorio(provvisorio);
		
		BigDecimal importoTotaleQuote = importoQuote.subtract(importoDaDedurreQuote);
		
		log.debug(methodName, "importoQuote: " + importoQuote);
		log.debug(methodName, "importoDaDedurreQuote: " + importoDaDedurreQuote);
		log.debug(methodName, "importoDaRegolarizzare: " + importoDaRegolarizzare);
		if(importoDaRegolarizzare.compareTo(importoQuote.subtract(importoDaDedurreQuote)) < 0){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il totale delle quote deve essere minore o uguale all'importo da regolarizzare del provvisorio"));
		}
		// SIAC-6060: restituisco l'importo da regolarizzare 'aggiornato' al front-end
		res.setImportoDaRegolarizzare(importoDaRegolarizzare.subtract(importoTotaleQuote));
				
	}


}
