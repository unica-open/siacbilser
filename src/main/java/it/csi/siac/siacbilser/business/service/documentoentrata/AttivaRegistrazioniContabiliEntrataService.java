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

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Attiva le registrazioni contabili (GEN) impostando il FlagContabilizzaGenPcc=TRUE sul documento di entrata.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AttivaRegistrazioniContabiliEntrataService extends CheckedAccountBaseService<AttivaRegistrazioniContabiliEntrata, AttivaRegistrazioniContabiliEntrataResponse> {
	
		
	@Autowired
	private DocumentoDad documentoDad;
	
	private DocumentoEntrata doc;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getDocumentoEntrata(), "documento spesa");
		doc = req.getDocumentoEntrata();
		checkEntita(req.getBilancio(), "bilancio");
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoDad.setEnte(ente);
		documentoDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AttivaRegistrazioniContabiliEntrataResponse executeService(AttivaRegistrazioniContabiliEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkAttivazioneGiaEffettuata();
		checkStatoDocumento();
		documentoDad.aggiornaFlagContabilizzaGenPcc(doc, Boolean.TRUE);
		
		//Imposta il valore del flagAttivaRegistrazioneResidui con quello passato dall'esterno.
		documentoDad.aggiornaAttributo(doc, TipologiaAttributo.FLAG_DISABILITA_REGISTRAZIONE_RESIDUI, doc.getFlagDisabilitaRegistrazioneResidui());
		
		AggiornaStatoDocumentoDiEntrataResponse resASDE = aggiornaStatoDocumento();
		doc = resASDE.getDocumentoEntrata();
		
		//boolean condizioniAttivazioneGENePCCSoddisfatte = resASDE.getRegistrazioniMovFinInserite() != null && !resASDE.getRegistrazioniMovFinInserite().isEmpty();
		boolean condizioniAttivazioneGENSoddisfatte = resASDE.isCondizioneDiAttivazioneGENSoddisfatta();
		
		if(!condizioniAttivazioneGENSoddisfatte){
			documentoDad.aggiornaFlagContabilizzaGenPcc(doc, Boolean.FALSE);
			res.addErrore(ErroreFin.CONDIZIONI_GEN_PCC_NON_SODDISFATTE.getErrore());
		}
		res.setDocumentoEntrata(doc);
		res.setRegistrazioniMovFinInserite(resASDE.getRegistrazioniMovFinInserite());
	}
	

	private void checkStatoDocumento() {
		StatoOperativoDocumento stato = documentoDad.findStatoOperativoDocumento(doc.getUid());
		if(StatoOperativoDocumento.INCOMPLETO.equals(stato) || StatoOperativoDocumento.ANNULLATO.equals(stato) || StatoOperativoDocumento.EMESSO.equals(stato)){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il documento [uid: "+doc.getUid()+"] e' in stato " + stato.getDescrizione()));
		}
		
	}

	private AggiornaStatoDocumentoDiEntrataResponse aggiornaStatoDocumento() {
		AggiornaStatoDocumentoDiEntrata reqASDE = new AggiornaStatoDocumentoDiEntrata();
		reqASDE.setDocumentoEntrata(doc);
		reqASDE.setRichiedente(req.getRichiedente());
		reqASDE.setBilancio(req.getBilancio());
		reqASDE.setSaltaInserimentoPrimaNota(req.getSaltaInserimentoPrimaNota());
		AggiornaStatoDocumentoDiEntrataResponse resASDS = serviceExecutor.executeServiceSuccess(AggiornaStatoDocumentoDiEntrataService.class, reqASDE);
		return resASDS;
	}
	
	private void checkAttivazioneGiaEffettuata() {
		Boolean flagContabilizzaGenPccAttuale = documentoDad.findFlagContabilizzaGenPccDocumento(doc);
		if(Boolean.TRUE.equals(flagContabilizzaGenPccAttuale)){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'attivazione delle registrazioni contabili per questo documento [uid: "+doc.getUid()+"] e' gia' stata effettuata."));
		}
	}
	

}
