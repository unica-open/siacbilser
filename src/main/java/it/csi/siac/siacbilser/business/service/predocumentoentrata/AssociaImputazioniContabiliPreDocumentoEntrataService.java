/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay;
import it.csi.siac.siaccommonser.business.service.base.exception.ExecuteExternalServiceException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StatoOperazioneAsincronaEnum;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class AssociaImputazioniContabiliPreDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssociaImputazioniContabiliPreDocumentoEntrataService extends BaseServiceOneWay<AssociaImputazioniContabiliPreDocumentoEntrata> {
		
		
		
	/** The ricerca dettaglio pre documento entrata service. */
	@Autowired
	private RicercaDettaglioPreDocumentoEntrataService ricercaDettaglioPreDocumentoEntrataService;
	
	/** The ricerca sintetica pre documento entrata. */
	@Autowired
	private RicercaSinteticaPreDocumentoEntrataService ricercaSinteticaPreDocumentoEntrata;
	
	
	/** The aggiorna pre documento di entrata service. */
	@Autowired
	private AggiornaPreDocumentoDiEntrataService aggiornaPreDocumentoDiEntrataService;

	@Autowired
	private ServiceExecutor serviceExecutor;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getPreDocumentiEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumenti entrata"));
		
		
		if(req.getRicercaSinteticaPreDocumentoEntrata()==null){
			checkCondition(!req.getPreDocumentiEntrata().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista predocumenti entrata"));
			for(PreDocumentoEntrata preDoc : req.getPreDocumentiEntrata()){
				checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento"));
				checkCondition(preDoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid predocumento"));
			}
		}
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		
		
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#executeService(it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequest)
	 */
	//timeout settato pari a AsyncBaseService.TIMEOUT
	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	public void executeService(AssociaImputazioniContabiliPreDocumentoEntrata serviceRequest) {
		super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#execute()
	 */
	@Override
	protected void execute() {
		
		List<PreDocumentoEntrata> preDocumentiEntrata = getPredocumentiEntrata();
		
		for (PreDocumentoEntrata preDoc : preDocumentiEntrata) {
			
			preDoc = getDettaglioPreDocumentoEntrata(preDoc);
			
			if(!isAggiornabile(preDoc) || !isAssociazioneCausaleValida(preDoc)) {				
				inserisciDettaglioOperazioneAsinc("SALTATO", " predocumento: " + preDoc.getNumero() + " ["+preDoc.getUid()+"] - " + preDoc.getDescrizione(), Esito.FALLIMENTO, "Non aggiornabile o associazione causale non valida");
				continue;
			}
			
			associaImputazioniContabili(preDoc);
			
			try{
				aggiornaPreDocumentoDiEntrata(preDoc);			
				inserisciDettaglioOperazioneAsinc("AGGIORNATO", " predocumento: " + preDoc.getNumero() + " ["+preDoc.getUid()+"] - " + preDoc.getDescrizione(), Esito.SUCCESSO);	
			
			} catch(ExecuteExternalServiceException eese){
				String errori = getTestoErrori(eese);
				inserisciDettaglioOperazioneAsinc("SALTATO", " predocumento: " + preDoc.getNumero() + " ["+preDoc.getUid()+"] - " + preDoc.getDescrizione(), Esito.FALLIMENTO, "Impossibile aggiornare il preoducmento:  " + errori);
			}
			
		}		
		
		statoFinaleOperazione = StatoOperazioneAsincronaEnum.STATO_OPASINC_CONCLUSA; //Di default è già impostato a STATO_OPASINC_CONCLUSA quindi si può omettere.
	
	}
	
	private String getTestoErrori(ExecuteExternalServiceException eese) {
		StringBuilder sb = new StringBuilder();
		for(Errore errore: eese.getErrori()){
			sb.append(errore.getTesto()).append(" ");
		}
		String errori=sb.toString();
		return errori;
	}
	
	/**
	 * Gets the predocumenti entrata.
	 *
	 * @return the predocumenti entrata
	 */
	private List<PreDocumentoEntrata> getPredocumentiEntrata() {	
		if(req.getRicercaSinteticaPreDocumentoEntrata()!=null){
			return ricercaSinteticaPreDocumentoEntrata();
		}
		
		return req.getPreDocumentiEntrata();
		
		
	}

	/**
	 * Ricerca sintetica pre documento entrata.
	 *
	 * @return the list
	 */
	private List<PreDocumentoEntrata> ricercaSinteticaPreDocumentoEntrata() {
		RicercaSinteticaPreDocumentoEntrataResponse resRSPD = ricercaSinteticaPreDocumentoEntrata(0);		
		List<PreDocumentoEntrata> result =  resRSPD.getPreDocumenti();
		
		for(int i = 1; i < resRSPD.getTotalePagine(); i++) {			
			resRSPD = ricercaSinteticaPreDocumentoEntrata(i);
			result.addAll(resRSPD.getPreDocumenti());			
		}
		return result;
	}
	
	/**
	 * Ricerca sintetica pre documento entrata.
	 *
	 * @param numeroPagina the numero pagina
	 * @return the ricerca sintetica pre documento entrata response
	 */
	private RicercaSinteticaPreDocumentoEntrataResponse ricercaSinteticaPreDocumentoEntrata(int numeroPagina){
		RicercaSinteticaPreDocumentoEntrata reqRSPD = req.getRicercaSinteticaPreDocumentoEntrata();		
		ParametriPaginazione pp = new ParametriPaginazione(numeroPagina);
		pp.setElementiPerPagina(100);
		reqRSPD.setParametriPaginazione(pp);
		reqRSPD.setRichiedente(req.getRichiedente());		
		RicercaSinteticaPreDocumentoEntrataResponse resRSPD = executeExternalService(ricercaSinteticaPreDocumentoEntrata, reqRSPD);
		return resRSPD;
	}
	
	
	/**
	 * Ribalta le informazioni della causale sul preDocumento.
	 *
	 * @param preDoc the pre doc
	 */
	private void associaImputazioniContabili(PreDocumentoEntrata preDoc) {
		CausaleEntrata causale = preDoc.getCausaleEntrata();
		
		/*
		 * Ribalta accertamento e subaccertamento 
		 * 
		 * - Capitolo Entrata (mod BIL ) 
		 * - Accertamento/SubAccertamento ( mod FIN )
		 */
		
		preDoc.setCapitoloEntrataGestione(causale.getCapitoloEntrataGestione());
		
		preDoc.setAccertamento(causale.getAccertamento());
		preDoc.setSubAccertamento(causale.getSubAccertamento());
		
		/*
		 * Ribalta Soggetto, Modalita' pagamento Soggeto, sede secondaria e AttoAmministrativo
		 * solo se presenti nelle Imputazioni
		 * 
		 * Contabili Causale Entrata (mod FIN ) 
		 * - Soggetto ,
		 * ModalitaPagamentoSoggetto, SedeSecondariaSoggetto (mod FIN) 
		 * - Atto Amministrativo (mod ATT)
		 */
		
		if(causale.getSoggetto()!=null){
			preDoc.setSoggetto(causale.getSoggetto());
		}
		
//		if(causale.getModalitaPagamentoSoggetto()!=null){
//			preDoc.setModalitaPagamentoSoggetto(causale.getModalitaPagamentoSoggetto());
//		}
//		
//		if(causale.getSedeSecondariaSoggetto()!=null){
//			preDoc.setSedeSecondariaSoggetto(causale.getSedeSecondariaSoggetto());
//		}
		
//		if(causale.getAttoAmministrativo()!=null){
//			preDoc.setAttoAmministrativo(causale.getAttoAmministrativo());
//		}
	}
	
	
	
	
	
	
	
	/**
	 * Aggiorna pre documento di entrata.
	 *
	 * @param preDoc the pre doc
	 */
	private void aggiornaPreDocumentoDiEntrata(PreDocumentoEntrata preDoc) {		
		
		AggiornaPreDocumentoDiEntrata reqAPDDS = new AggiornaPreDocumentoDiEntrata();
		reqAPDDS.setRichiedente(req.getRichiedente());
		reqAPDDS.setBilancio(req.getBilancio());
		reqAPDDS.setPreDocumentoEntrata(preDoc);
		reqAPDDS.setGestisciModificaImportoAccertamento(req.isGestisciModificaImportoAccertamento());
		
		//AggiornaPreDocumentoDiEntrataResponse resAPDDS = executeExternalServiceSuccess(aggiornaPreDocumentoDiEntrataService, reqAPDDS);
		serviceExecutor.executeServiceSuccessTxRequiresNew(aggiornaPreDocumentoDiEntrataService, reqAPDDS);
		//checkServiceResponseFallimento(resAPDDS);
		
	}
	
	/**
	 * Controlla se esiste l'associazione con una causale di spesa ( Causale Entrata mod FIN ),
	 * per la quale esiste la relazione Imputazioni Contabili Causale Entrata (mod
	 * FIN ), valida ( dataAnnullamento nulla) e per la quale sia definito
	 * almeno uno dei seguenti dati da associare - Impegno/SubImpegno ( mod FIN )
	 * 
	 * Se non conforme esce con errore: FIN_ERR_0228 - Associazione Imputazioni Contabili non realizzabile ,messaggio aggiuntivo.
	 *
	 * @param preDoc the pre doc
	 * @return true, if is associazione causale valida
	 */
	private boolean isAssociazioneCausaleValida(PreDocumentoEntrata preDoc) {
		CausaleEntrata c = preDoc.getCausaleEntrata();
		if(c==null){
			return false;
		}
		return StatoOperativoCausale.VALIDA.equals(c.getStatoOperativoCausale()) && 
				(c.getCapitoloEntrataGestione()!=null && c.getCapitoloEntrataGestione().getUid()!=0)
				|| (c.getAccertamento()!=null  && c.getAccertamento().getUid()!=0)  
				|| (c.getSubAccertamento()!=null &&  c.getSubAccertamento().getUid()!=0)
				|| (c.getSoggetto()!=null && c.getSoggetto().getUid()!=0
				); 
		
	}
	
	/**
	 * 1) deve essere aggiornabile, quindi non deve essere in uno dei seguenti stati operativi: ANNULLATO,DEFINITO 
	 * 2) non deve essere già in stato operativo COMPLETO
	 * 
	 * Se non conforme esce con errore: COR_ERR_0028 - Operazione incompatibile con stato dell'entità ,keyPredisposizione ,statoOp .
	 *
	 * @param preDoc the pre doc
	 * @return true, if is aggiornabile
	 */
	private boolean isAggiornabile(PreDocumentoEntrata preDoc) {		
		return StatoOperativoPreDocumento.INCOMPLETO.equals(preDoc.getStatoOperativoPreDocumento());
	
	}
	
	
	/**
	 * Carica il dettaglio del predocumento.
	 *
	 * @param preDoc the pre doc
	 * @return the dettaglio pre documento entrata
	 */
	protected PreDocumentoEntrata getDettaglioPreDocumentoEntrata(PreDocumentoEntrata preDoc) {
		RicercaDettaglioPreDocumentoEntrata req = new RicercaDettaglioPreDocumentoEntrata();
		req.setDataOra(new Date());
		req.setPreDocumentoEntrata(preDoc);
		req.setRichiedente(this.req.getRichiedente());
		RicercaDettaglioPreDocumentoEntrataResponse res = executeExternalServiceSuccess(ricercaDettaglioPreDocumentoEntrataService,req);
		return res.getPreDocumentoEntrata();
	}
}
