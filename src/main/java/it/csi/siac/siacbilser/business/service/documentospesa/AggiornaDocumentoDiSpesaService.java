/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.OnereSpesaDad;
import it.csi.siac.siacbilser.model.TipoCodifica;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipoAnalogico;

// TODO: Auto-generated Javadoc
/**
 * Aggiornamento di un documento di spesa .
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaDocumentoDiSpesaService extends CrudDocumentoDiSpesaBaseService<AggiornaDocumentoDiSpesa, AggiornaDocumentoDiSpesaResponse> {
	
	//SIAC-8301
	private static final String CODICE_TIPO_DOCUMENTO_SIOPE_ANALOGICO = "A";
	private static final String CODICE_TIPO_DOCUMENTO_SIOPE_ELETTRONICO = "E";
	private static final String CODICE_TIPO_DOCUMENTO_SIOPE_ANALOGICO_DOC_EQUIVALENTE = "DE";
	@Autowired
	private OnereSpesaDad onereSpesaDad;
	//SIAC-8301
	@Autowired
	private CodificaDad codificaDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoSpesa();
		
		checkEntita(req.getBilancio(), "bilancio");
		bilancio = req.getBilancio();
		
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		checkCondition(doc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		
		checkNotNull(doc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(doc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(doc.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento"));
		//checkNotNull(doc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		checkNotNull(doc.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione documento"));
		
		checkNotNull(doc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo documento"));
		
		checkNotNull(doc.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione documento"));
		
		//checkNotNull(doc.getFlagBeneficiarioMultiplo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag beneficiario multiplo"));
		if(doc.getFlagBeneficiarioMultiplo()==null) {
			doc.setFlagBeneficiarioMultiplo(Boolean.FALSE);
		}
		
		checkNotNull(doc.getStatoOperativoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo documento"));
		
		checkNotNull(doc.getSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto"));
		checkCondition(doc.getSoggetto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto"));
		
		// SIAC-3191
		checkNumero();
		checkImporto();
		checkArrotondamento();
		checkDataEmissione();
		checkDataScadenza();
		checkDataSospensione();
		// SIAC-4749
		checkDatiFatturaPagataIncassata();
	}
	
	private void checkDatiFatturaPagataIncassata() throws ServiceParamError {
		checkCondition(doc.getDatiFatturaPagataIncassata() == null
				|| (Boolean.TRUE.equals(doc.getDatiFatturaPagataIncassata().getFlagPagataIncassata()) && StringUtils.isNotBlank(doc.getDatiFatturaPagataIncassata().getNotePagamentoIncasso()) && doc.getDatiFatturaPagataIncassata().getDataOperazione() != null)
				|| (!Boolean.TRUE.equals(doc.getDatiFatturaPagataIncassata().getFlagPagataIncassata()) && StringUtils.isBlank(doc.getDatiFatturaPagataIncassata().getNotePagamentoIncasso()) && doc.getDatiFatturaPagataIncassata().getDataOperazione() == null),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("documento pagato, note, data pagamento", ": nel caso almeno uno sia valorizzato devono essere tutti valorizzati"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		documentoSpesaDad.setEnte(ente);
		codificaDad.setEnte(ente);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaDocumentoDiSpesaResponse executeService(AggiornaDocumentoDiSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		checkDocumentoAggiornabile();
		
		checkSoggetto();
		checkAnno();
		
		// SIAC-4749: controllo di coerenza sulle quote nel caso in cui siano settati i dati di pagamento/incasso
		checkDatiFatturaPagataIncassataQuoteSenzaImpegno();
		// SIAC-6099
		checkImportoOnere();
		
		//SIAC-8301
		gestioneDefaultSiope();
		
		documentoSpesaDad.aggiornaAnagraficaDocumentoSpesa(doc);	
		
		if(req.isAggiornaStatoDocumento()) {
			DocumentoSpesa statoOperativoDocumento = aggiornaStatoOperativoDocumento(doc).getDocumentoSpesa();
			doc.setStatoOperativoDocumento(statoOperativoDocumento.getStatoOperativoDocumento());
			doc.setDataInizioValiditaStato(statoOperativoDocumento.getDataInizioValiditaStato());
		}
		
		res.setDocumentoSpesa(doc);
	}
	
	//SIAC-8301
	/**
	 * Default per il SIOPE+.
	 * <ul>
	 *     <li>TipoDocumentoSiope: nel caso non venisse specificato il default ad es. per l'inserimento
	 *     dei documenti creati in automatico (ALG, etc…), &eacute; ANALOGICO</li>
	 *     <li>TipoDocumentoAnalogicoSiope: nel caso non venisse specificato il default
	 *     ad es. per l'inserimento dei documenti creati in automatico (ALG, etc…), &eacute; DOC_EQUIVALENTE</li>
	 * </ul>
	 */
	private void gestioneDefaultSiope() {
		final String methodName = "gestioneDefaultSiope";
					
		SiopeDocumentoTipo siopeDocumentoTipoDaImpostare =  estraiSiopeDocumento();
		doc.setSiopeDocumentoTipo(siopeDocumentoTipoDaImpostare);
					
		String codiceSiopeTipoDocumento = doc.getSiopeDocumentoTipo().getCodice();
				
		if(CODICE_TIPO_DOCUMENTO_SIOPE_ELETTRONICO.equals(codiceSiopeTipoDocumento)) {
			doc.setSiopeDocumentoTipoAnalogico(null);
			return;
		}
		
		if(CODICE_TIPO_DOCUMENTO_SIOPE_ANALOGICO.equals(codiceSiopeTipoDocumento) && (doc.getSiopeDocumentoTipoAnalogico() == null || doc.getSiopeDocumentoTipoAnalogico().getUid() == 0)) {
			// Default per il tipo documento analogico siope
			log.debug(methodName, "Tipo documento analogico siope non valorizzato: impostazione del default DOC_EQUIVALENTE");
			SiopeDocumentoTipoAnalogico siopeDocumentoTipoAnalogico = codificaDad.ricercaCodifica(new TipoCodifica(SiopeDocumentoTipoAnalogico.class), CODICE_TIPO_DOCUMENTO_SIOPE_ANALOGICO_DOC_EQUIVALENTE);
			doc.setSiopeDocumentoTipoAnalogico(siopeDocumentoTipoAnalogico);
		}
	}
	

	private SiopeDocumentoTipo estraiSiopeDocumento() {
		final String methodName = "estraiSiopeDocumento";
		SiopeDocumentoTipo siopeDocumentoFornito = doc.getSiopeDocumentoTipo();
		boolean idTipoDocSiopeFornito = siopeDocumentoFornito!= null && siopeDocumentoFornito.getUid() != 0;
		
		if(!idTipoDocSiopeFornito) {			
			SiopeDocumentoTipo siopeDocTipoDaBaseDati = documentoSpesaDad.getSiopeDocumentoTipoByDocId(doc);
			if(siopeDocTipoDaBaseDati == null) {
				log.debug(methodName, "Tipo documento siope non valorizzato e non presente su base dati: impostazione del default ANALOGICO");
				siopeDocTipoDaBaseDati = codificaDad.ricercaCodifica(new TipoCodifica(SiopeDocumentoTipo.class), CODICE_TIPO_DOCUMENTO_SIOPE_ANALOGICO);
			}
			return siopeDocTipoDaBaseDati;
		}
		
		if(StringUtils.isBlank(siopeDocumentoFornito.getCodice())) {
			log.debug(methodName, "Fornito solo uid del tipo documento siope, carico anche il codice per definire se sia analogico o elettronico.");
			siopeDocumentoFornito = codificaDad.ricercaCodifica(SiopeDocumentoTipo.class, doc.getSiopeDocumentoTipo().getUid());
		}
	
		return siopeDocumentoFornito;
	}

	/**
	 * Nel caso venissero valorizzati i dati per la fattura pagata il sistema deve controllare che NESSUNA delle quote documento
	 * abbia un impegno-subimpegno associato: lo stato del documento deve diventare EMESSO. Nel caso i controlli non andassero a buon fine &eacute;
	 * necessario visualizzare un messaggio di errore sulla presenza di impegni sulle quote.
	 */
	private void checkDatiFatturaPagataIncassataQuoteSenzaImpegno() {
		final String methodName = "checkDatiFatturaPagataIncassataQuoteSenzaImpegno";
		if(doc.getDatiFatturaPagataIncassata() == null
				|| (!Boolean.TRUE.equals(doc.getDatiFatturaPagataIncassata().getFlagPagataIncassata()) && StringUtils.isBlank(doc.getDatiFatturaPagataIncassata().getNotePagamentoIncasso()) && doc.getDatiFatturaPagataIncassata().getDataOperazione() == null)) {
			log.debug(methodName, "Dati di pagamento/incasso non valorizzati");
			return;
		}
		long subdocConMovimentoGestione = documentoSpesaDad.countSubdocumentiConMovimentoGestioneByUidDocumento(doc.getUid()).longValue();
		log.debug(methodName, "Il documento [uid = " + doc.getUid() + "] ha " + subdocConMovimentoGestione + " quote associate");
		if(subdocConMovimentoGestione > 0L) {
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("aggiornamento documento di spesa",
				subdocConMovimentoGestione == 1L ? "esiste una quota collegata a un movimento di gestione" : "esistono " + subdocConMovimentoGestione + " quote collegate a un movimento di gestione"));
		}
	}

	protected void checkDocumentoAggiornabile() {
		/*
		 * 1. il documento è in uno stato modificabile (diverso dallo stato ‘EMESSO’ e ‘ANNULLATO’) 
		 * altrimenti viene segnalato il messaggio <FIN_ERR_0141, Documento non aggiornabile perché stato incongruente>.
		 */
		
		if(StatoOperativoDocumento.EMESSO.equals(doc.getStatoOperativoDocumento()) 
				|| StatoOperativoDocumento.ANNULLATO.equals(doc.getStatoOperativoDocumento()) ){
			throw new BusinessException(ErroreFin.DOCUMENTO_NON_AGGIORNABILE_PERCHE_STATO_INCONGRUENTE.getErrore());
		}
		
	}

	/**
	 * SIAC-6099: si richiede di inserire un controllo bloccante che verifichi che l'importo specificato nel campo Imponibile sia minore o uguale all'importo del documento.
	 */
	private void checkImportoOnere() {
		BigDecimal importoDocumento = doc.getImporto();
		BigDecimal importoOneriGiaAssociati = onereSpesaDad.getMassimoImportoImponibileOneriCollegatiAlDocumento(doc.getUid());
		
		if(importoOneriGiaAssociati.compareTo(importoDocumento) > 0) {
			throw new BusinessException(ErroreCore.VALORE_NON_CONSENTITO.getErrore("importo", "non puo' essere minore dell'imponibile di alcuno degli oneri associati ("
					+ "importo documento: " + Utility.formatCurrencyAsString(importoDocumento)
					+ ", massimo degli imponibili per gli oneri associati: " + Utility.formatCurrencyAsString(importoOneriGiaAssociati) + ")"));
		}
	}
	

}
