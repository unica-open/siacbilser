/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaentrata;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoDocumentoDiEntrataService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Implementazione del servizio AggiornaSubdocumentoIvaEntrataService.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaSubdocumentoIvaEntrataService extends CrudSubdocumentoIvaEntrataBaseService<AggiornaSubdocumentoIvaEntrata, AggiornaSubdocumentoIvaEntrataResponse> {
	
	/** The subdocumento iva entrata dad. */
	@Autowired
	private SubdocumentoIvaEntrataDad subdocumentoIvaEntrataDad;
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	private Date dataProtProvAttuale;
	private Date dataProtDefAttuale;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocIva = req.getSubdocumentoIvaEntrata();
				
		checkNotNull(subdocIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva entrata"));
		checkCondition(subdocIva.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento iva entrata"), false);
		
		checkNotNull(subdocIva.getAnnoEsercizio(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio subdocumento iva entrata"), false);
		
		checkNotNull(subdocIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(subdocIva.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		
		checkNotNull(subdocIva.getStatoSubdocumentoIva(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato subdocumento iva entrata"), false);
		
		checkCondition(isLegatoAlDocumento() ^ isLegatoAlSubdocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento o subdocumento iva entrata (non entrambi)"));
		
		checkCondition(!subdocIva.getListaAliquotaSubdocumentoIva().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aliquote subdocumenti iva"));

		checkNotNull(subdocIva.getRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro iva"));
		//checkNotNull(subdocIva.getRegistroIva().getTipoRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registro iva registro iva"));
		
		checkNotNull(req.getBilancio(), "bilancio");
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		subdocumentoIvaEntrataDad.setLoginOperazione(loginOperazione);
		subdocumentoIvaEntrataDad.setEnte(subdocIva.getEnte());
		documentoEntrataDad.setLoginOperazione(loginOperazione);
		documentoEntrataDad.setEnte(subdocIva.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public AggiornaSubdocumentoIvaEntrataResponse executeService(AggiornaSubdocumentoIvaEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		caricaDettaglioDocumentoESubdocumentoEntrataAssociato();
		caricaRegistroIva();
		caricaBilancio();
		
		checkAggiornabilita();
		checkNumeroProtocollo();
		
		if(subdocIva.getDataProtocolloDefinitivo()!=null){
			checkDataRegistrazioneProtocolloDefinitivoPerAggiornamento();
		}else if(subdocIva.getDataProtocolloProvvisorio()!=null){
			checkDataRegistrazioneProtocolloProvvisorioPerAggiornamento();
		}
		aggiornaNumeroEDataProtocolloSuContatoreSeNecessario();
		impostaFlagRegistrazioneIva();
		
		
		
		
		subdocumentoIvaEntrataDad.aggiornaAnagraficaSubdocumentoIvaEntrata(subdocIva);	
		
		res.setSubdocumentoIvaEntrata(subdocIva);
		
		
		
		
		
		if(TipoFamigliaDocumento.IVA_ENTRATA.equals(documentoEntrata.getTipoDocumento().getTipoFamigliaDocumento())){
			documentoEntrataDad.aggiornaStatoDocumentoEntrata(documentoEntrata.getUid(), StatoOperativoDocumento.VALIDO);
		} else {
			AggiornaStatoDocumentoDiEntrata reqASDDS = new AggiornaStatoDocumentoDiEntrata();
			reqASDDS.setRichiedente(req.getRichiedente());
			reqASDDS.setDocumentoEntrata(documentoEntrata);
			reqASDDS.setBilancio(req.getBilancio());
			serviceExecutor.executeServiceSuccess(AggiornaStatoDocumentoDiEntrataService.class, reqASDDS);
		}

	}

	private void caricaBilancio() {
		bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
	}
	
	/**
	 * Il sistema verifica se il Subdocumento Iva selezionato &eacute; modificabile effettuando i seguenti controlli:
	 * <ul>
	 *     <li>Il registro non deve essere <strong>BLOCCATO</strong>, in questo caso segnalare un messaggio di Attenzione indicando che il registro selezionato
	 *         &egrave; Bloccato ('Dati Iva non gestibili perch&eacute; il registro selezionato &egrave; stato bloccato.').</li>
	 *     <li>Lo stato del Documento Iva Entrata deve essere diverso da <strong>'PD'</strong>, ovvero quando ancora nessuna delle sue quote &egrave; stata incassata;
	 *         se la registrazione Iva &egrave; fatta sulla singola quota allora lo stato del Documento Iva Entrata &egrave; diverso da 'PD' quando quella quota
	 *         non &egrave; ancora stata incassata.</li>
	 *     <li>Il documento iva non deve appartenere ad un registro stampato in definitivo, ovvero quando i campi (derivati) del Documento Iva
	 *         <em>'flagStampaDef_Provv'</em> e <em>'flagStampaDef_Def'</em> sono entrambi = 0
	 *         <br/>
	 *         NOTA: questo controllo &egrave; l'unico da mantenere se l'operatore ha l'azione <em>OP-IVA-aggDocIvaEntBackOffice</em> abilitata,
	 *         se l'azione &egrave; stata passata tra i parametri (ad es. se il servizio &egrave; stato richiamato da web app).</li>
	 *     <li>Il documento di riferimento (se presente, ad es. per i documenti relativi alla Testata IVA questo controllo non &egrave; da fare)
	 *         <strong>NON DEVE</strong> avere nessuna quota con ordinativo associato.</li>
	 * </ul>
	 */
	private void checkAggiornabilita() {
		final String methodName = "checkAggiornabilita";
		dataProtProvAttuale = subdocumentoIvaEntrataDad.findDataProtocolloProvvisorio(subdocIva);
		dataProtDefAttuale = subdocumentoIvaEntrataDad.findDataProtocolloDefinitivo(subdocIva);
		//questo e' l'unico controllo che deve essere effettuato se l'utente e' abilitato all'azione OP-IVA-aggDocIvaEntBackOffice abilitata.
		// Controllo numero 3
		List<StampaIva> stampeDefinitive = subdocumentoIvaEntrataDad.findStampaIvaBySubdocumentoIva(subdocIva, dataProtProvAttuale, dataProtDefAttuale);
		boolean stampeDefinitivePresenti = !stampeDefinitive.isEmpty();
		log.info(methodName, "Stampe definitive presenti e collegate al periodo del subdocumento iva? " + stampeDefinitivePresenti);
		if(stampeDefinitivePresenti) {
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", 
					"dati Iva non gestibili perche' il registro selezionato e' stato gia' stampato in definitivo per il periodo corrispondente alla data di protocollo."));
		}
		
		// TODO: valutare: Controllo in piu' che il numero di protocollo non sia gia' stato assegnato a un subdoc iva diverso da me stesso
//		Long subdocivaConPariNumeroRegistrazioneDefinitivo = subdocumentoIvaEntrataDad.contaSubdocumentiIvaConPariNumeroProtocolloDefinitivoEDiversoUid(subdocIva);
//		boolean hasSubdocIvaConPariNumeroDefinitivo = subdocivaConPariNumeroRegistrazioneDefinitivo.longValue() > 0;
//		log.info(methodName, "Esistono altri subdoc con pari numero protocollo definitivo? " + hasSubdocIvaConPariNumeroDefinitivo);
//		if(hasSubdocIvaConPariNumeroDefinitivo) {
//			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", 
//				"Dati Iva non gestibili perche' esistono altri subdocumenti iva con pari numero di protocollo definitivo."));
//		}
//		
//		Long subdocivaConPariNumeroRegistrazioneProvvisorio = subdocumentoIvaEntrataDad.contaSubdocumentiIvaConPariNumeroProtocolloProvvisorioEDiversoUid(subdocIva);
//		boolean hasSubdocIvaConPariNumeroProvvisorio = subdocivaConPariNumeroRegistrazioneProvvisorio.longValue() > 0;
//		log.info(methodName, "Esistono altri subdoc con pari numero protocollo provvisorio? " + hasSubdocIvaConPariNumeroProvvisorio);
//		if(hasSubdocIvaConPariNumeroProvvisorio) {
//			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", 
//				"Dati Iva non gestibili perche' esistono altri subdocumenti iva con pari numero di protocollo provvisorio."));
//		}
		
		if(codiciAzioniConsentite.contains("OP-IVA-aggDocIvaEntBackOffice") && !req.isIgnoraPermessiUtente()) {
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
		StatoSubdocumentoIva stato = subdocumentoIvaEntrataDad.findStatoSubdocumentoIva(subdocIva);
		boolean statoProvvisorioDefinitivo = StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO.equals(stato);
		log.info(methodName, "Stato PROVVISORIO/DEFINTIIVO? " + statoProvvisorioDefinitivo);
		if(statoProvvisorioDefinitivo){
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Subdocumento iva", StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO.getCodice()));
		}
		
		// Controllo numero 4
		Long ordinativiCollegati = subdocumentoIvaEntrataDad.countOrdinativiAssociatiAQuoteDocumentoCollegato(subdocIva);
		boolean hasOrdinativiCollegati = ordinativiCollegati.longValue() > 0;
		log.info(methodName, "Ha ordinativi collegati? " + hasOrdinativiCollegati);
		if(hasOrdinativiCollegati) {
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", 
				"Dati Iva non gestibili perche' il documento di riferimento ha "
				+ (ordinativiCollegati.longValue() > 1 ? ordinativiCollegati.longValue() + " ordinativi collegati" : "un ordinativo collegato") + "."));
		}
		
	}
	
	private void checkNumeroProtocollo() {
		if(subdocIva.getNumeroProtocolloDefinitivo() != null && subdocumentoIvaEntrataDad.numeroProtDefGiaPresente(subdocIva)){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il numero di protocollo definitivo indicato e' gia' stato assegnato per il registro selezionato."));
		}
		if(subdocIva.getNumeroProtocolloProvvisorio() != null && subdocumentoIvaEntrataDad.numeroProtProvGiaPresente(subdocIva)){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il numero di protocollo provvisorio indicato e' gia' stato assegnato per il registro selezionato."));
		}
	}
	
	/**
	 *  Durante l'aggiornamento di un subdocumentoIva, oltre ad effettuare i controlli sulla data di protocollo definitivo si aggiorna tale data
	 */
	private void checkDataRegistrazioneProtocolloDefinitivoPerAggiornamento() {
		if(subdocIva.getDataProtocolloDefinitivo().equals(dataProtDefAttuale)){
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
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(subdocIva.getDataProtocolloDefinitivo());
			int anno = gc.get(GregorianCalendar.YEAR);
			log.debug(methodName, "protocollo definitivo valorizzato");
			Integer ultimoProtDefAttuale = contatoreRegistroIvaDad.findUltimoNumeroProtocolloDef(subdocIva.getRegistroIva().getUid(), anno);
			if(ultimoProtDefAttuale == null){
				//non esiste ancora un record per questo anno e questo registro, lo inserisco
				log.debug(methodName, "non esiste ancora un record (def) per questo anno e questo registro, lo inserisco");
				contatoreRegistroIvaDad.inserisciContatoreProtDef(subdocIva.getRegistroIva(), subdocIva.getNumeroProtocolloDefinitivo(), subdocIva.getDataProtocolloDefinitivo(), anno);
			}else{
				contatoreRegistroIvaDad.aggiornaDataENumProtDef(subdocIva.getRegistroIva().getUid(), subdocIva.getNumeroProtocolloDefinitivo(), subdocIva.getDataProtocolloDefinitivo() ,anno);
			}
		}
		if(subdocIva.getNumeroProtocolloProvvisorio() != null){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(subdocIva.getDataProtocolloProvvisorio());
			int anno = gc.get(GregorianCalendar.YEAR);
			log.debug(methodName, "protocollo provvisorio valorizzato");
			Integer ultimoProtProvAttuale = contatoreRegistroIvaDad.findUltimoNumeroProtocolloProvv(subdocIva.getRegistroIva().getUid(), anno);
			if(ultimoProtProvAttuale == null){
				log.debug(methodName, "non esiste ancora un record (prov) per questo anno e questo registro, lo inserisco");
				contatoreRegistroIvaDad.inserisciContatoreProtProv(subdocIva.getRegistroIva(), subdocIva.getNumeroProtocolloProvvisorio(), subdocIva.getDataProtocolloProvvisorio(), anno);
			}else{
				contatoreRegistroIvaDad.aggiornaDataENumProtProv(subdocIva.getRegistroIva().getUid(), subdocIva.getNumeroProtocolloProvvisorio(), subdocIva.getDataProtocolloProvvisorio() ,anno);
			}
		}
	}
	
}
