/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaspesa;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.documentoivaentrata.AggiornaControregistrazioneService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaControregistrazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaControregistrazioneResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Implementazione del servizio AggiornaSubdocumentoIvaSpesaService.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaSubdocumentoIvaSpesaService extends CrudSubdocumentoIvaSpesaBaseService<AggiornaSubdocumentoIvaSpesa, AggiornaSubdocumentoIvaSpesaResponse> {
	
	private AggiornaControregistrazioneService aggiornaControregistrazioneService;
	
	@Autowired
	private ApplicationContext appCtx;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	private Date dataProtProvAttuale;
	private Date dataProtDefAttuale;
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocIva = req.getSubdocumentoIvaSpesa();
				
		checkNotNull(subdocIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva spesa"));
		checkCondition(subdocIva.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento iva spesa"), false);
		
		checkNotNull(subdocIva.getAnnoEsercizio(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio subdocumento iva spesa"), false);
		
		checkNotNull(subdocIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(subdocIva.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		
		checkNotNull(subdocIva.getStatoSubdocumentoIva(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato subdocumento iva spesa"), false);
		
		checkCondition(isLegatoAlDocumento() ^ isLegatoAlSubdocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento o subdocumento iva spesa (non entrambi)"));
		
		checkCondition(!subdocIva.getListaAliquotaSubdocumentoIva().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aliquote subdocumenti iva"));

		checkNotNull(subdocIva.getRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro iva"));
		//checkNotNull(subdocIva.getRegistroIva().getTipoRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registro iva registro iva"));
		
		if(Boolean.TRUE.equals(subdocIva.getFlagIntracomunitario())){
			checkNotNull(subdocIva.getSubdocumentoIvaEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva entrata"));
			checkCondition(subdocIva.getSubdocumentoIvaEntrata().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva entrata"));
			checkNotNull(subdocIva.getSubdocumentoIvaEntrata().getRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro iva subdocumento iva entrata"));
		}
		
		checkNotNull(req.getBilancio(), "bilancio");
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		subdocumentoIvaSpesaDad.setLoginOperazione(loginOperazione);
		subdocumentoIvaSpesaDad.setEnte(ente);
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		documentoSpesaDad.setEnte(ente);
		
		aggiornaControregistrazioneService = appCtx.getBean(Utility.toDefaultBeanName(AggiornaControregistrazioneService.class), AggiornaControregistrazioneService.class);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public AggiornaSubdocumentoIvaSpesaResponse executeService(AggiornaSubdocumentoIvaSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		caricaDettaglioDocumentoESubdocumentoSpesaAssociato();
		caricaRegistroIva();
		caricaBilancio();
		
		checkAggiornabilita();
		checkNumeroProtocollo();
		
//		if(!Boolean.TRUE.equals(subdocIva.getFlagIntracomunitario())){
//			checkMovimentoIvaDocumento();
//		}
		
		if(subdocIva.getDataProtocolloDefinitivo()!=null){
			checkDataRegistrazioneProtocolloDefinitivoPerAggiornamento();
		}else if(subdocIva.getDataProtocolloProvvisorio()!=null){
			checkDataRegistrazioneProtocolloProvvisorioPerAggiornamento();
		}
		aggiornaNumeroEDataProtocolloSuContatoreSeNecessario();
		
		impostaFlagRegistrazioneIva();
		
		subdocumentoIvaSpesaDad.aggiornaAnagraficaSubdocumentoIvaSpesa(subdocIva);	
		
		if(Boolean.TRUE.equals(subdocIva.getFlagIntracomunitario())){
			aggiornaControregistrazione();
		}
		
		res.setSubdocumentoIvaSpesa(subdocIva);
		
		if(TipoFamigliaDocumento.IVA_SPESA.equals(documentoSpesa.getTipoDocumento().getTipoFamigliaDocumento())){
			//aggiornaStato(documentoSpesa);
			documentoSpesaDad.aggiornaStatoDocumentoSpesa(documentoSpesa.getUid(), StatoOperativoDocumento.VALIDO);
		} else {
			AggiornaStatoDocumentoDiSpesa reqASDDS = new AggiornaStatoDocumentoDiSpesa();
			reqASDDS.setRichiedente(req.getRichiedente());
			reqASDDS.setDocumentoSpesa(documentoSpesa);
			reqASDDS.setBilancio(req.getBilancio());
			serviceExecutor.executeServiceSuccess(AggiornaStatoDocumentoDiSpesaService.class, reqASDDS);
		}
	}

	
	private void caricaBilancio() {
		bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
	}

	//l'utente abilitato ora puo' modificare il numrero di protocollo, devo verificare che non esista già un subdocIva con lo stesso numero di protocollo definitivo o provvisorio
	//(a parte per il caso delle quote iva differita, che hanno lo stesso protocollo del subdoc padre- caso che non si dovrebbe comunque verificare, percha' a quel punto il subdoc iva non dovrebbe piu' essere aggiornabile)
	private void checkNumeroProtocollo() {
		if(subdocIva.getNumeroProtocolloDefinitivo() != null && subdocumentoIvaSpesaDad.numeroProtDefGiaPresente(subdocIva)){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il numero di protocollo definitivo indicato e' gia' stato assegnato per il registro selezionato."));
		}
		if(subdocIva.getNumeroProtocolloProvvisorio() != null && subdocumentoIvaSpesaDad.numeroProtProvGiaPresente(subdocIva)){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il numero di protocollo provvisorio indicato e' gia' stato assegnato per il registro selezionato."));
		}
	}

	private void aggiornaControregistrazione() {
		
		SubdocumentoIvaEntrata controregistrazione = popolaControregistrazione();
		
		AggiornaControregistrazione reqAC = new AggiornaControregistrazione();
		reqAC.setBilancio(req.getBilancio());
		reqAC.setRichiedente(req.getRichiedente());
		reqAC.setSubdocumentoIvaEntrata(controregistrazione);
		
		AggiornaControregistrazioneResponse resAC = executeExternalServiceSuccess(aggiornaControregistrazioneService,reqAC);
		
		subdocIva.setSubdocumentoIvaEntrata(resAC.getSubdocumentoIvaEntrata());
		
	}
	
	/**
	 * Il sistema verifica se il Subdocumento Iva selezionato &egrave; modificabile effettuando i seguenti controlli:
	 * <ul>
	 *     <li>Il registro non deve essere <strong>BLOCCATO</strong>, in questo caso segnalare un messaggio di Attenzione indicando che il registro selezionato
	 *         &egrave; Bloccato ('Dati Iva non gestibili perch&eacute; il registro selezionato &egrave; stato bloccato.').</li>
	 *     <li>Lo stato del Documento Iva Spesa deve essere diverso da <trong>'PD'</strong>, ovvero quando ancora nessuna delle sue quote
	 *         &egrave; stata pagata; se la registrazione Iva &egrave; fatta sulla singola quota allora lo stato del Documento Iva Spesa
	 *         &egrave; diverso da 'PD' quando quella quota non &egrave; ancora stata pagata.</li>
	 *     <li>Il documento iva non deve  appartenere ad un registro stampato in definitivo, ovvero quando i campi (derivati) del Documento Iva
	 *         <em>'flagStampaDef_Provv'</em> e <em>'flagStampaDef_Def'</em> sono entrambi = 0
	 *         <br/>
	 *         NOTA: questo controllo &egrave; l'unico da mantenere se l'operatore ha l'azione <em>OP-IVA-aggDocIvaSpeBackOffice</em> abilitata,
	 *         se l'azione &egrave; stata passata tra i parametri (ad es. se il servizio &egrave; stato richiamato da web app).</li>
	 *     <li>Il documento di riferimento (se presente, ad es. per i documenti relativi alla Testata IVA questo controllo non &egrave; da fare)
	 *         <strong>NON DEVE</strong> avere nessuna quota liquidata o con ordinativo associato.</li>
	 * </ul>
	 */
	private void checkAggiornabilita() {
		final String methodName = "checkAggiornabilita";
		// Controllo numero 3
		dataProtProvAttuale = subdocumentoIvaSpesaDad.findDataProtocolloProvvisorio(subdocIva);
		dataProtDefAttuale = subdocumentoIvaSpesaDad.findDataProtocolloDefinitivo(subdocIva);
		List<StampaIva> stampeDefinitive = subdocumentoIvaSpesaDad.findStampaIvaBySubdocumentoIva(subdocIva, dataProtProvAttuale, dataProtDefAttuale);
		boolean stampeDefinitivePresenti = !stampeDefinitive.isEmpty();
		log.info(methodName, "Stampe definitive presenti e collegate al periodo del subdocumento iva? " + stampeDefinitivePresenti);
		if(stampeDefinitivePresenti) {
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", 
					"dati Iva non gestibili perche' il registro selezionato e' stato gia' stampato in definitivo per il periodo corrispondente alla data di protocollo."));
		}
		
		if(codiciAzioniConsentite.contains("OP-IVA-aggDocIvaSpeBackOffice") && !req.isIgnoraPermessiUtente()) {
			return;
		}
		
		// Controllo numero 1
		boolean registroIvaBloccato = !Boolean.FALSE.equals(subdocIva.getRegistroIva().getFlagBloccato());
		log.info(methodName, "Registro iva bloccato? " + registroIvaBloccato);
		if(registroIvaBloccato) {
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", 
					"dati Iva non gestibili perche' il registro selezionato e' stato bloccato."));
		}
		// Controllo numero 2
		StatoSubdocumentoIva stato = subdocumentoIvaSpesaDad.findStatoSubdocumentoIva(subdocIva);
		boolean statoProvvisorioDefinitivo = StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO.equals(stato);
		log.info(methodName, "Stato PROVVISORIO/DEFINTIIVO? " + statoProvvisorioDefinitivo);
		if(statoProvvisorioDefinitivo){
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Subdocumento iva", StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO.getCodice()));
		}
		
		// TODO: Controllo numero 4, aggiungere liquidazioni
		Long ordinativiCollegati = subdocumentoIvaSpesaDad.countOrdinativiAssociatiAQuoteDocumentoCollegato(subdocIva);
		Long liquidazioniCollegate = subdocumentoIvaSpesaDad.countLiquidazioniAssociateAQuoteDocumentoCollegato(subdocIva);
		boolean hasOrdinativiCollegati = ordinativiCollegati.longValue() > 0;
		boolean hasLiquidazioniCollegate = liquidazioniCollegate.longValue() > 0;
		log.info(methodName, "Ha ordinativi collegati? " + hasOrdinativiCollegati + ", ha liquidazioni collegate? " + hasLiquidazioniCollegate);
		if(hasOrdinativiCollegati) {
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", 
				"Dati Iva non gestibili perche' il documento di riferimento ha "
				+ (ordinativiCollegati.longValue() > 1 ? ordinativiCollegati.longValue() + " ordinativi collegati" : "un ordinativo collegato") + "."));
		}
		if(hasLiquidazioniCollegate) {
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", 
				"Dati Iva non gestibili perche' il documento di riferimento ha "
				+ (liquidazioniCollegate.longValue() > 1 ? liquidazioniCollegate.longValue() + " liquidazioni collegate" : "una liquidazione collegata") + "."));
		}
		
	}
	
	/**
	 *  Durante l'aggiornamento di un subdocumentoIva, oltre ad effettuare i controlli sulla data di protocollo definitivo si aggiorna tale data
	 */
	private void checkDataRegistrazioneProtocolloDefinitivoPerAggiornamento() {
		if(subdocIva.getDataProtocolloDefinitivo().equals(dataProtDefAttuale)){
			log.debug("checkDataRegistrazioneProtocolloDefinitivoPerAggiornamento", "la data non e' stata modificata, non devo controllarla");
			return;
		}
		checkDataRegistrazioneProtocolloDefinitivo();
		
	}
	
	/**
	 *  Durante l'aggiornamento di un subdocumentoIva, oltre ad effettuare i controlli sulla data di protocollo provvisorio si aggiorna tale data
	 */
	private void checkDataRegistrazioneProtocolloProvvisorioPerAggiornamento() {
		if(subdocIva.getDataProtocolloProvvisorio().equals(dataProtProvAttuale)){
			return;
		}
		checkDataRegistrazioneProtocolloProvvisorio();
		
	}
	
	private void aggiornaNumeroEDataProtocolloSuContatoreSeNecessario() {
		String methodName = "aggiornaNumeroEDataProtocolloSuContatoreSeNecessario";
		if(subdocIva.getNumeroProtocolloDefinitivo() != null){
			log.debug(methodName, "protocollo definitivo valorizzato");
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(subdocIva.getDataProtocolloDefinitivo());
			int anno = gc.get(GregorianCalendar.YEAR);
			Integer ultimoProtDefAttuale = contatoreRegistroIvaDad.findUltimoNumeroProtocolloDef(subdocIva.getRegistroIva().getUid(), anno);
			if(ultimoProtDefAttuale == null){
				//non esiste ancora un record per questo anno e questo registro, lo inserisco
				log.debug(methodName, "non esiste ancora un record (def) per questo anno e questo registro, lo inserisco");
				contatoreRegistroIvaDad.inserisciContatoreProtDef(subdocIva.getRegistroIva(), subdocIva.getNumeroProtocolloDefinitivo(), subdocIva.getDataProtocolloDefinitivo(), anno);
			}else{
				contatoreRegistroIvaDad.aggiornaDataENumProtDef(subdocIva.getRegistroIva().getUid(), subdocIva.getNumeroProtocolloDefinitivo(), subdocIva.getDataProtocolloDefinitivo(), anno);
			}
		}
		if(subdocIva.getNumeroProtocolloProvvisorio() != null){
			log.debug(methodName, "protocollo provvisorio valorizzato");
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(subdocIva.getDataProtocolloProvvisorio());
			int anno = gc.get(GregorianCalendar.YEAR);
			Integer ultimoProtProvAttuale = contatoreRegistroIvaDad.findUltimoNumeroProtocolloProvv(subdocIva.getRegistroIva().getUid(), anno);
			if(ultimoProtProvAttuale == null){
				log.debug(methodName, "non esiste ancora un record (prov) per questo anno e questo registro, lo inserisco");
				contatoreRegistroIvaDad.inserisciContatoreProtProv(subdocIva.getRegistroIva(), subdocIva.getNumeroProtocolloProvvisorio(), subdocIva.getDataProtocolloProvvisorio(), anno);
			}else{
				contatoreRegistroIvaDad.aggiornaDataENumProtProv(subdocIva.getRegistroIva().getUid(), subdocIva.getNumeroProtocolloProvvisorio(), subdocIva.getDataProtocolloProvvisorio(), anno);
			}
		}
	}

}
