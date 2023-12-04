/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoEntrataCollegaDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoEntrataCollegaDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaPreDocumentoEntrataCollegaDocumentoService extends CheckedAccountBaseService<AggiornaPreDocumentoEntrataCollegaDocumento, AggiornaPreDocumentoEntrataCollegaDocumentoResponse>{

	/** The pre documento entrata dad. */
	@Autowired
	private PreDocumentoEntrataDad preDocumentoEntrataDad;
	
	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	/** The pre documento entrata service. */
	@Autowired
	private InseriscePreDocumentoEntrataService inseriscePredocumentoDiEntrataService;
	
	@Autowired
	private AggiornaPreDocumentoDiEntrataService aggiornaPredocumentoDiEntrataService;
	
	/** The pre doc. */
	private PreDocumentoEntrata preDoc;
	
	/** The sub doc. */
	private SubdocumentoEntrata subDoc;
	
	@Autowired
	protected ServiceExecutor serviceExecutor;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		preDoc = req.getPreDocumentoEntrata();
		subDoc = req.getSubDocumentoEntrata();
		
		checkEntita(preDoc, "predocumento");
		checkEntita(subDoc, "subdocumento");
	}
	
	@Override
	@Transactional
	public AggiornaPreDocumentoEntrataCollegaDocumentoResponse executeService(AggiornaPreDocumentoEntrataCollegaDocumento serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		preDocumentoEntrataDad.setLoginOperazione(loginOperazione);
		preDocumentoEntrataDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		caricaDatiPredocumento();
		caricaDatiSubodcumento();

		if(subDoc.getImporto().compareTo(preDoc.getImporto()) > 0) {
			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("Importo della quota superiore a quello della predisposizione d'incasso"));
		}
		
		PreDocumentoEntrata nuovoPreDoc = null;

		if(subDoc.getImporto().compareTo(preDoc.getImporto()) == 0) {
			
			String methodName = "Aggiorno la predisposizione con importo uguale alla quota";
			
			collegaQuotaConImportiUguali();

			AggiornaPreDocumentoDiEntrata requestAggiorna = creaRequestAggiornamentoPreDoc(preDoc, nuovoPreDoc);
			AggiornaPreDocumentoDiEntrataResponse responseAggiorna = serviceExecutor.executeServiceSuccess(aggiornaPredocumentoDiEntrataService, requestAggiorna);

			//definisco la predisposizione in modo da poterla cercare sulla siacRPredocSubdoc
			preDocumentoEntrataDad.definisciPreDocumento(responseAggiorna.getPreDocumentoEntrata().getUid(), subDoc.getUid());
			
			log.debug(methodName, "Inserimento concluso con:\t" + responseAggiorna.getEsito());

			//ritorno alla vista solo il documento aggiornato
			res.setPreDocumentoEntrataAggiornato(responseAggiorna.getPreDocumentoEntrata());
			res.setSubDocumentoEntrata(subDoc);
		}
		
		if(subDoc.getImporto().compareTo(preDoc.getImporto()) < 0) {

			String methodName = "Collego il documento con importo diverso";
			
			nuovoPreDoc = collegaQuotaConImportiDiversi();
			
			InseriscePreDocumentoEntrata requestInserimento = creaRequestInserimentoPreDoc(nuovoPreDoc);
			InseriscePreDocumentoEntrataResponse nuovoInserimentoResponse = serviceExecutor.executeServiceSuccess(inseriscePredocumentoDiEntrataService, requestInserimento);
		
			//definisco la predisposizione in modo da poterla cercare sulla siacRPredocSubdoc
			preDocumentoEntrataDad.definisciPreDocumento(nuovoInserimentoResponse.getPreDocumentoEntrata().getUid(), subDoc.getUid());
			
			log.debug(methodName, "Inserimento concluso con:\t" + nuovoInserimentoResponse.getEsito());
			
			AggiornaPreDocumentoDiEntrata requestAggiorna = creaRequestAggiornamentoPreDoc(preDoc, nuovoInserimentoResponse.getPreDocumentoEntrata());
			AggiornaPreDocumentoDiEntrataResponse responseAggiorna = serviceExecutor.executeServiceSuccess(aggiornaPredocumentoDiEntrataService, requestAggiorna);
			
			log.debug(methodName, "Inserimento concluso con:\t" + responseAggiorna.getEsito());
			

			//ritorno alla vista solo il documento aggiornato
			res.setPreDocumentoEntrataAggiornato(responseAggiorna.getPreDocumentoEntrata());
			res.setSubDocumentoEntrata(subDoc);
		}
		
	}

	private void caricaDatiSubodcumento() {
		subDoc = subdocumentoEntrataDad.findSubdocumentoEntrataById(subDoc.getUid());
		if(subDoc == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("subdocumento da associare"));
		}
		
	}

	private void caricaDatiPredocumento() {
		preDoc = preDocumentoEntrataDad.findPreDocumentoByIdModelDetail(preDoc.getUid());
		if(preDoc == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("predocumento da associare"));
		}
		
	}
	
	/**
	 * Definisci pre doc.
	 *
	 * @param predocumento the predocumento
	 */
	private void collegaPredocumentoAlSubdoc(PreDocumentoEntrata predocumento) {
		preDocumentoEntrataDad.collegaPreDocumento(predocumento.getUid(), subDoc.getUid());
	}

	private boolean checkProvvisoriAreEquals(PreDocumentoEntrata preDoc, SubdocumentoEntrata subDoc) {
		boolean result = true;
		
		// se sono presenti due provvisori occorre verificarli
		if((preDoc.getProvvisorioDiCassa() != null 
				&& preDoc.getProvvisorioDiCassa().getNumero() != null 
				&& preDoc.getProvvisorioDiCassa().getNumero() > 0)
				&& (subDoc.getProvvisorioCassa() != null 
				&& subDoc.getProvvisorioCassa().getNumero() != null 
				&& subDoc.getProvvisorioCassa().getNumero() > 0)) {
		
			// se i due provvisori non corrispondono impedire l'esecuzione del servizio
			if(preDoc.getProvvisorioDiCassa().getNumero().compareTo(subDoc.getProvvisorioCassa().getNumero()) != 0) {
				result = false;
			}
			
		}
		
		return result;
	}

	private PreDocumentoEntrata collegaQuotaConImportiUguali() {
		
		preDoc.setProvvisorioDiCassa(subDoc.getProvvisorioCassa());
		preDoc.setSubDocumento(subDoc);
		preDoc.setAccertamento(subDoc.getAccertamento());
		preDoc.setSoggetto(subDoc.getDocumento().getSoggetto());
		
		return preDoc;
	}

	private PreDocumentoEntrata collegaQuotaConImportiDiversi() {
		PreDocumentoEntrata nuovoPreDoc = SerializationUtils.clone(preDoc);
		
		nuovoPreDoc.setUid(0);
		nuovoPreDoc.setProvvisorioDiCassa(subDoc.getProvvisorioCassa());
		nuovoPreDoc.setSubDocumento(subDoc);
		nuovoPreDoc.setAccertamento(subDoc.getAccertamento());
		nuovoPreDoc.setSoggetto(subDoc.getDocumento().getSoggetto());
		nuovoPreDoc.setImporto(subDoc.getImporto());
		nuovoPreDoc.setDescrizione(preDoc.getDescrizione());
		
		return nuovoPreDoc;
	}
	
	private InseriscePreDocumentoEntrata creaRequestInserimentoPreDoc(PreDocumentoEntrata preDocumentoEntrata) {
		InseriscePreDocumentoEntrata insert = new InseriscePreDocumentoEntrata();
		
		insert.setRichiedente(getRichiedenteFromRequest());
		insert.setPreDocumentoEntrata(preDocumentoEntrata);
		insert.getPreDocumentoEntrata().setStatoOperativoPreDocumento(StatoOperativoPreDocumento.DEFINITO);
		insert.setBilancio(req.getBilancio());
		insert.setInserimentoPredocDaCollegaDocumento(Boolean.TRUE);
		
		return insert;
	}

	private AggiornaPreDocumentoDiEntrata creaRequestAggiornamentoPreDoc(PreDocumentoEntrata preDocumentoEntrata, PreDocumentoEntrata preDocumentoEntrataFiglio) {
		AggiornaPreDocumentoDiEntrata aggiorna = new AggiornaPreDocumentoDiEntrata();

		aggiorna.setRichiedente(getRichiedenteFromRequest());
		aggiorna.setPreDocumentoEntrata(preDocumentoEntrata);
		aggiorna.setBilancio(req.getBilancio());
		
		BigDecimal nuovoImporto = null;
		String nuovaDescrizione = null;
		
		if(preDocumentoEntrataFiglio != null && preDocumentoEntrataFiglio.getImporto() != null && preDocumentoEntrataFiglio.getImporto().compareTo(BigDecimal.ZERO) > 0) {			
			nuovoImporto = preDocumentoEntrata.getImporto().subtract(preDocumentoEntrataFiglio.getImporto()); 
			aggiorna.getPreDocumentoEntrata().setImporto(nuovoImporto);
		}
		
		if(preDocumentoEntrata != null && !StringUtils.isBlank(preDocumentoEntrata.getDescrizione()) && preDocumentoEntrataFiglio != null && preDocumentoEntrataFiglio.getNumero() != null) {
			nuovaDescrizione = preDocumentoEntrata.getDescrizione() + " collegata a predoc. N. " + preDocumentoEntrataFiglio.getNumero();				
		} 
		
		if(preDocumentoEntrataFiglio != null && preDocumentoEntrataFiglio.getNumero() != null){
			nuovaDescrizione = "collegata a predoc. N. " + preDocumentoEntrataFiglio.getNumero();							
		}
		
		aggiorna.setSaltaCheckDisponibilita(true);
		aggiorna.getPreDocumentoEntrata().setDescrizione(nuovaDescrizione != null ? nuovaDescrizione : preDocumentoEntrata.getDescrizione());
		
		return aggiorna;
	}

	private Richiedente getRichiedenteFromRequest() {
		return req.getRichiedente();
	}
	
	
}
