/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoRelazione;

/**
 * Aggiorna ll'anagrafica della nota credito di spesa.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaNotaCreditoSpesaService extends CrudDocumentoDiSpesaBaseService<AggiornaNotaCreditoSpesa, AggiornaNotaCreditoSpesaResponse> {
	
//	/** The documento spesa dad. */
//	@Autowired
//	private DocumentoSpesaDad documentoSpesaDad;
	
	/** The aggiorna quota documento spesa service. */
	@Autowired
	private AggiornaQuotaDocumentoSpesaService aggiornaQuotaDocumentoSpesaService;
	
	@Autowired
	protected DocumentoDad documentoDad;
	
//	/** The doc. */
//	private DocumentoSpesa doc;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoSpesa();
		
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		checkCondition(doc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento"));
		
		checkNotNull(doc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(doc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(doc.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento"));
//		checkNotNull(doc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		checkNotNull(doc.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione documento"));
		
		checkNotNull(doc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo documento"));
		
		checkNotNull(doc.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione documento"));
		
		checkNotNull(doc.getTipoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo documento"));
		checkCondition(doc.getTipoDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid tipo documento"));
		
		
		checkNotNull(doc.getListaSubdocumenti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti del documento"));
		checkCondition(doc.getListaSubdocumenti().size()==1, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti (deve esserci un solo subdocumento) del documento"));
		
		checkEntita(req.getBilancio(), "bilacio");
		bilancio = req.getBilancio();
		
		for(DocumentoSpesa dsp : doc.getListaDocumentiSpesaPadre()){
			dsp.setTipoRelazione(TipoRelazione.NOTA_CREDITO);
		}
		
		for(DocumentoEntrata dsp : doc.getListaDocumentiEntrataPadre()){
			dsp.setTipoRelazione(TipoRelazione.NOTA_CREDITO);
		}
		
		// SIAC-3191
		checkNumero();
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		documentoSpesaDad.setEnte(ente);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaNotaCreditoSpesaResponse executeService(AggiornaNotaCreditoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		String methodName = "execute";
		
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		if(doc.getImportoDaDedurreSuFattura() == null){
			doc.setImportoDaDedurreSuFattura(doc.getImporto());
		}
		
		checkTotaleImportoDaDedurreSuFattura(doc);
		
		impostaFlagCollegatoCECSePadreCollegatoCEC();
		documentoSpesaDad.aggiornaAnagraficaDocumentoSpesa(doc);
		
		SubdocumentoSpesa subdocumentoSpesa = doc.getListaSubdocumenti().get(0);
		
		//Imposto lo stesso importo del documento anche sulla singola quota della nota credito.
		subdocumentoSpesa.setImporto(doc.getImporto());
		
		subdocumentoSpesa = aggiornaQuotaDocumentoSepsa(subdocumentoSpesa);
		
		doc.getListaSubdocumenti().set(0,subdocumentoSpesa);
		
		log.debug(methodName, "numero fatture (docPadre) collegate alla notaCredito:" + doc.getListaDocumentiSpesaPadre().size()); //Maggiore di 1 quando la nota di credito e' collegata a più fatture.
		for(DocumentoSpesa padre: doc.getListaDocumentiSpesaPadre()){
			DocumentoSpesa documentoConStatoAggiornato = aggiornaStatoOperativoDocumento(padre.getUid()).getDocumentoSpesa();
			padre.setStatoOperativoDocumento(documentoConStatoAggiornato.getStatoOperativoDocumento());
			padre.setDataInizioValiditaStato(documentoConStatoAggiornato.getDataInizioValiditaStato());
			break;//dovrebbe esserci un solo padre (verificare il caso di notaCredito collegata a più fatture)
		}
		
		res.setDocumentoSpesa(doc);		
		
	}
	
	private void impostaFlagCollegatoCECSePadreCollegatoCEC(){
		String methodName = "impostaFlagCollegatoCECSePadreCollegatoCEC";
		for(DocumentoSpesa padre: doc.getListaDocumentiSpesaPadre()){
			boolean collegatoCEC = documentoDad.isCollegatoCEC(padre);
			log.debug(methodName, "Imposto flagCollegatoCEC a "+collegatoCEC);
			doc.setCollegatoCEC(collegatoCEC);
			break;//dovrebbe esserci un solo padre
		}
	}
	

	/**
	 * Richiama il servizio di aggiornamento del subdocumento di spesa.
	 *
	 * @param subdocumentoSpesa the subdocumento spesa
	 * @return il subdocumento inserito.
	 */
	private SubdocumentoSpesa aggiornaQuotaDocumentoSepsa(SubdocumentoSpesa subdocumentoSpesa) {
		AggiornaQuotaDocumentoSpesa reqInsQuota = new AggiornaQuotaDocumentoSpesa();
		reqInsQuota.setRichiedente(req.getRichiedente());		
		subdocumentoSpesa.setEnte(doc.getEnte());
		reqInsQuota.setAggiornaStatoDocumento(false);
		reqInsQuota.setBilancio(bilancio);
		
		DocumentoSpesa d = new DocumentoSpesa();
		d.setUid(doc.getUid());
		subdocumentoSpesa.setDocumento(d);	
		
		if(subdocumentoSpesa.getAttoAmministrativo() != null && subdocumentoSpesa.getAttoAmministrativo().getUid() == 0){
			subdocumentoSpesa.setAttoAmministrativo(null);
		}
		
		reqInsQuota.setSubdocumentoSpesa(subdocumentoSpesa);
		
		AggiornaQuotaDocumentoSpesaResponse resInsQuota = executeExternalServiceSuccess(aggiornaQuotaDocumentoSpesaService,reqInsQuota);
		return resInsQuota.getSubdocumentoSpesa();
		
	}

	/**
	 * dataEmissione (DocumentoSpesa.dataEmissione) deve avere campo anno uguale all'anno del documento
	 */
	protected void checkDataEmissione() {
		try {
			//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.ITALY);
			Integer year = Integer.valueOf(dateFormat.format(doc.getDataEmissione()));
			Integer annoDoc = doc.getAnno();
			if (year.compareTo(annoDoc) != 0) {
				throw new BusinessException(it.csi.siac.siacfin2ser.model.errore.ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore(""), Esito.FALLIMENTO);
			}
		} catch (NumberFormatException e){
			throw new BusinessException(it.csi.siac.siacfin2ser.model.errore.ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore(""), Esito.FALLIMENTO);
		} catch (NullPointerException e) {
			throw new BusinessException(it.csi.siac.siacfin2ser.model.errore.ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore(""), Esito.FALLIMENTO);
		}
	}

//	/**
//	 * importo (DocumentoSpesa.importo) deve essere positivo
//	 */
//	protected void checkImporto() {
//		BigDecimal importo = doc.getImporto();
//		if (importo.signum() < 0) {
//			throw new BusinessException(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere positivo"), Esito.FALLIMENTO);
//		}
//	}
//	
//	/**
//	 * arrotondamento (DocumentoSpesa.arrotondamento) se esiste deve essere negativo; inoltre importo + arrotondamento deve essere potitivo
//	 */
//	protected void checkArrotondamento() {
//		BigDecimal arrotondamento = doc.getArrotondamento();
//		BigDecimal importo = doc.getImporto();
//		if (arrotondamento == null) {
//			return;
//		}
//		if (arrotondamento.signum() >= 0) {
//			throw new BusinessException(ErroreCore.FORMATO_NON_VALIDO.getErrore("Arrotondamento", ": deve essere negativo"), Esito.FALLIMENTO);
//		}
//		if (importo.add(arrotondamento).signum() <= 0) {
//			throw new BusinessException(ErroreCore.FORMATO_NON_VALIDO.getErrore("Arrotondamento", ": importo sommato ad arrotondamento deve essere positivo"), Esito.FALLIMENTO);
//		}
//	}
	
	/**
	 * Controlla la coerenza tra il totaleImportoDaDedurreSuFattura e l'importo della nota di credito.
	 * 
	 * @param notaDiCredito
	 * @throws BusinessException OPERAZIONE_NON_CONSENTITA se totaleImportoDaDedurreSuFattura supera l'importo della nota di credito.
	 */
	private void checkTotaleImportoDaDedurreSuFattura(Documento<?, ?> notaDiCredito) {
		String methodName = "checkTotaleImportoDaDedurreSuFattura";
		
		BigDecimal totaleImportoDaDedurreSuFattura = calcolaTotaleImportoDaDedurreSuFattura();
		
		log.debug(methodName, MessageFormat.format("Controllo importi:\n"
				+ "totaleImportoDaDedurreSuFattura: {0,number,###,##0.00} €. \n"
				+ "importo nota di credito: {1,number,###,##0.00} €", 
				totaleImportoDaDedurreSuFattura, 
				notaDiCredito.getImporto()));
		
		if(totaleImportoDaDedurreSuFattura.compareTo(notaDiCredito.getImporto())>0){
			String msg = MessageFormat.format("l''importo da dedurre su fatture deve essere minore o uguale dell''importo della nota di credito. "
					+ "Totale importo da dedurre su fatture: {0,number,###,##0.00} €. "
					+ "Importo nota di credito: {1,number,###,##0.00} €.", 
					totaleImportoDaDedurreSuFattura, 
					notaDiCredito.getImporto());
			log.error(methodName, msg + " [notaDiCredito.uid: "+ notaDiCredito.getUid() + "]");
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
		}
	}

	private BigDecimal calcolaTotaleImportoDaDedurreSuFattura() {
		final String methodName = "calcolaTotaleImportoDaDedurreSuFattura";
		
		BigDecimal totaleImportoDaDedurreSuFattura = BigDecimal.ZERO;
		for(DocumentoSpesa documentoSpesaPadre : doc.getListaDocumentiSpesaPadre()){
			totaleImportoDaDedurreSuFattura = totaleImportoDaDedurreSuFattura.add(documentoSpesaPadre.getImportoDaDedurreSuFattura());
		}
		log.debug(methodName, "result:"+totaleImportoDaDedurreSuFattura);
		return totaleImportoDaDedurreSuFattura;
	}
}
