/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

// TODO: Auto-generated Javadoc
/**
 * Servizio massivo asincrono di associazione delle imputazioni contabili.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssociaImputazioniContabiliPreDocumentoSpesaService extends BaseServiceOneWay<AssociaImputazioniContabiliPreDocumentoSpesa/*, AssociaImputazioniContabiliPreDocumentoSpesaResponse*/> {
		
	
	
	/** The ricerca dettaglio pre documento spesa service. */
	@Autowired
	private RicercaDettaglioPreDocumentoSpesaService ricercaDettaglioPreDocumentoSpesaService;
	
	/** The ricerca sintetica pre documento spesa. */
	@Autowired
	private RicercaSinteticaPreDocumentoSpesaService ricercaSinteticaPreDocumentoSpesa;
	

	/** The aggiorna pre documento di spesa service. */
	@Autowired
	private AggiornaPreDocumentoDiSpesaService aggiornaPreDocumentoDiSpesaService;
	
	@Autowired
	private ServiceExecutor serviceExecutor;


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getPreDocumentiSpesa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumenti spesa"));
		
		
		if(req.getRicercaSinteticaPreDocumentoSpesa()==null){
			checkCondition(!req.getPreDocumentiSpesa().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista predocumenti spesa"));
			for(PreDocumentoSpesa preDoc : req.getPreDocumentiSpesa()){				
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
	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	public void executeService(AssociaImputazioniContabiliPreDocumentoSpesa serviceRequest) {
		super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#execute()
	 */
	@Override
	protected void execute() {
		
		//sleep(20000);
		
		List<PreDocumentoSpesa> preDocumentiSpesa = getPredocumentiSpesa();
		
		for (PreDocumentoSpesa preDoc : preDocumentiSpesa) {
			//sleep(5000);		
			
			preDoc = getDettaglioPreDocumentoSpesa(preDoc);
			
			if(!isAggiornabile(preDoc) || !isAssociazioneCausaleValida(preDoc)) {				
				inserisciDettaglioOperazioneAsinc("SALTATO", " predocumento: " + preDoc.getNumero() + " ["+preDoc.getUid()+"] - " + preDoc.getDescrizione(), Esito.FALLIMENTO, "Non aggiornabile o associazione causale non valida");
				continue;
			}
			
			associaImputazioniContabili(preDoc);		
			
			try{						
				aggiornaPreDocumentoDiSpesa(preDoc);				
				inserisciDettaglioOperazioneAsinc("AGGIORNATO", " predocumento: " + preDoc.getNumero() + " ["+preDoc.getUid()+"] - " + preDoc.getDescrizione(), Esito.SUCCESSO);
				
			} catch(ExecuteExternalServiceException eese){
				String errori = getTestoErrori(eese);
				inserisciDettaglioOperazioneAsinc("SALTATO", " predocumento: " + preDoc.getNumero() + " ["+preDoc.getUid()+"] - " + preDoc.getDescrizione(), Esito.FALLIMENTO, "Impossibile aggiornare il preoducmento:  " + errori);
			}
			
			//sleep(5000);
			
			
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
	 * Gets the predocumenti spesa.
	 *
	 * @return the predocumenti spesa
	 */
	private List<PreDocumentoSpesa> getPredocumentiSpesa() {	
		if(req.getRicercaSinteticaPreDocumentoSpesa()!=null){
			return ricercaSinteticaPreDocumentoSpesa();
		}
		
		return req.getPreDocumentiSpesa();
		
		
	}

	/**
	 * Ricerca sintetica pre documento spesa.
	 *
	 * @return the list
	 */
	private List<PreDocumentoSpesa> ricercaSinteticaPreDocumentoSpesa() {
		RicercaSinteticaPreDocumentoSpesaResponse resRSPD = ricercaSinteticaPreDocumentoSpesa(0);		
		List<PreDocumentoSpesa> result =  resRSPD.getPreDocumenti();
		
		for(int i = 1; i < resRSPD.getTotalePagine(); i++) {			
			resRSPD = ricercaSinteticaPreDocumentoSpesa(i);
			result.addAll(resRSPD.getPreDocumenti());			
		}
		return result;
	}
	
	/**
	 * Ricerca sintetica pre documento spesa.
	 *
	 * @param numeroPagina the numero pagina
	 * @return the ricerca sintetica pre documento spesa response
	 */
	private RicercaSinteticaPreDocumentoSpesaResponse ricercaSinteticaPreDocumentoSpesa(int numeroPagina){
		RicercaSinteticaPreDocumentoSpesa reqRSPD = req.getRicercaSinteticaPreDocumentoSpesa();		
		ParametriPaginazione pp = new ParametriPaginazione(numeroPagina);
		pp.setElementiPerPagina(100);
		reqRSPD.setParametriPaginazione(pp);
		reqRSPD.setRichiedente(req.getRichiedente());		
		RicercaSinteticaPreDocumentoSpesaResponse resRSPD = executeExternalService(ricercaSinteticaPreDocumentoSpesa, reqRSPD);
		return resRSPD;
	}

//	private void sleep(long mills) {
//		try {
//			Thread.sleep(mills);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}	
	

	/**
 * Ribalta le informazioni della causale sul preDocumento.
 *
 * @param preDoc the pre doc
 */
	private void associaImputazioniContabili(PreDocumentoSpesa preDoc) {
		CausaleSpesa causale = preDoc.getCausaleSpesa();
		
		/*
		 * Ribalta impegno e subImpegno 
		 * 
		 * - Capitolo Uscita (mod BIL ) 
		 * - Impegno/SubImpegno ( mod FIN )
		 */
		
		preDoc.setCapitoloUscitaGestione(causale.getCapitoloUscitaGestione());
		
		preDoc.setImpegno(causale.getImpegno());
		preDoc.setSubImpegno(causale.getSubImpegno());
		
		/*
		 * Ribalta Soggetto, Modalita' pagamento Soggeto, sede secondaria e AttoAmministrativo
		 * solo se presenti nelle Imputazioni
		 * 
		 * Contabili Causale Spesa (mod FIN ) 
		 * - Soggetto ,
		 * ModalitaPagamentoSoggetto, SedeSecondariaSoggetto (mod FIN) 
		 * - Atto Amministrativo (mod ATT)
		 */
		
		if(causale.getSoggetto()!=null){
			preDoc.setSoggetto(causale.getSoggetto());
		}
		
		if(causale.getModalitaPagamentoSoggetto()!=null){
			preDoc.setModalitaPagamentoSoggetto(causale.getModalitaPagamentoSoggetto());
		}
		
		if(causale.getSedeSecondariaSoggetto()!=null){
			preDoc.setSedeSecondariaSoggetto(causale.getSedeSecondariaSoggetto());
		}
		
		if(causale.getAttoAmministrativo()!=null){
			preDoc.setAttoAmministrativo(causale.getAttoAmministrativo());
		}
	}
	
	
	
	
	
	

	/**
	 * Aggiorna pre documento di spesa.
	 *
	 * @param preDoc the pre doc
	 */
	public void aggiornaPreDocumentoDiSpesa(PreDocumentoSpesa preDoc) {		
		
		AggiornaPreDocumentoDiSpesa reqAPDDS = new AggiornaPreDocumentoDiSpesa();
		reqAPDDS.setRichiedente(req.getRichiedente());
		reqAPDDS.setBilancio(req.getBilancio());
		reqAPDDS.setPreDocumentoSpesa(preDoc);
		//AggiornaPreDocumentoDiSpesaResponse resAPDDS = executeExternalServiceSuccess(aggiornaPreDocumentoDiSpesaService, reqAPDDS);
		
		serviceExecutor.executeServiceSuccessTxRequiresNew(aggiornaPreDocumentoDiSpesaService, reqAPDDS);
		
		//checkServiceResponseFallimento(resAPDDS);
		
	}

	/**
	 * Controlla se esiste l'associazione con una causale di spesa ( Causale Spesa mod FIN ),
	 * per la quale esiste la relazione Imputazioni Contabili Causale Spesa (mod
	 * FIN ), valida ( dataAnnullamento nulla) e per la quale sia definito
	 * almeno uno dei seguenti dati da associare - Impegno/SubImpegno ( mod FIN )
	 * 
	 * Se non conforme esce con errore: FIN_ERR_0228 - Associazione Imputazioni Contabili non realizzabile ,messaggio aggiuntivo.
	 *
	 * @param preDoc the pre doc
	 * @return true, if is associazione causale valida
	 */
	private boolean isAssociazioneCausaleValida(PreDocumentoSpesa preDoc) {
		CausaleSpesa c = preDoc.getCausaleSpesa();
		if(c==null) {
			return false;
		}
		return StatoOperativoCausale.VALIDA.equals(c.getStatoOperativoCausale()) && 
				(					
				 (c.getImpegno()!=null  && c.getImpegno().getUid()!=0)  
				 || (c.getSubImpegno()!=null &&  c.getSubImpegno().getUid()!=0)
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
	private boolean isAggiornabile(PreDocumentoSpesa preDoc) {	
		return StatoOperativoPreDocumento.INCOMPLETO.equals(preDoc.getStatoOperativoPreDocumento());

	}

	
	/**
	 * Carica il dettaglio del predocumento.
	 *
	 * @param preDoc the pre doc
	 * @return the dettaglio pre documento spesa
	 */
	protected PreDocumentoSpesa getDettaglioPreDocumentoSpesa(PreDocumentoSpesa preDoc) {
		RicercaDettaglioPreDocumentoSpesa req = new RicercaDettaglioPreDocumentoSpesa();
		req.setDataOra(new Date());
		req.setPreDocumentoSpesa(preDoc);
		req.setRichiedente(this.req.getRichiedente());
		RicercaDettaglioPreDocumentoSpesaResponse res = executeExternalServiceSuccess(ricercaDettaglioPreDocumentoSpesaService,req);
		return res.getPreDocumentoSpesa();
	}
}
