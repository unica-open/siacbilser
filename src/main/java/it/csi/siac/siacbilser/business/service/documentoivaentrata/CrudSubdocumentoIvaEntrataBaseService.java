/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaentrata;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaDettaglioDocumentoEntrataService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.ContatoreRegistroIvaDad;
import it.csi.siac.siacbilser.integration.dad.PeriodoDad;
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoEsigibilitaIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Base service per il crud dei subdocumenti iva di Entrata.
 *
 * @author Domenico
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class CrudSubdocumentoIvaEntrataBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {
	
	@Autowired 
	protected RicercaPuntualeSubdocumentoIvaEntrataService ricercaPuntualeSubdocumentoIvaEntrataService;
	@Autowired 
	protected RicercaDettaglioDocumentoEntrataService ricercaDettaglioDocumentoEntrataService;
	@Autowired
	protected SubdocumentoEntrataDad subdocumentoEntrataDad;
	@Autowired
	protected SubdocumentoIvaEntrataDad subdocumentoIvaEntrataDad;
	@Autowired
	protected ContatoreRegistroIvaDad contatoreRegistroIvaDad;
	@Autowired
	protected PeriodoDad periodoDad;
	@Autowired
	protected RegistroIvaDad registroIvaDad;
		
	protected SubdocumentoIvaEntrata subdocIva;	
	protected DocumentoEntrata documentoEntrata; 
	protected SubdocumentoEntrata subdocumentoEntrata;
	protected Bilancio bilancio;
	
	private DecimalFormat df = (DecimalFormat)NumberFormat.getInstance(Locale.ITALY);	 

	
	@Override
	protected void init() {
		super.init();
		
		periodoDad.setEnte(ente);
		contatoreRegistroIvaDad.setEnte(ente);
		contatoreRegistroIvaDad.setLoginOperazione(loginOperazione);
		
		df.setParseBigDecimal(true);
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
	}
	
	/**
	 * Controlla sa il subdocumentoIva e' legato ad un documento
	 *
	 * @return true se e' legato ad un documento
	 */
	protected boolean isLegatoAlDocumento() {
		return  subdocIva.getDocumento() !=null && subdocIva.getDocumento().getUid()!=0; //&& subdocIva.getSubdocumento() == null;
	}
	
	/**
	 * Controlla sa il subdocumentoIva e' legato ad un subdocumento
	 *
	 * @return true se e' legato ad un subdocumento
	 */
	protected boolean isLegatoAlSubdocumento() {
		return subdocIva.getSubdocumento() !=null && subdocIva.getSubdocumento().getUid()!=0; // && subdocIva.getDocumento() == null;
	}
	
	/**
	 * Inizializza le variabili di istanza documentoEntrata e subdocumentoEntrata. 
	 * Quest'ultimo solo nel caso isLegatoAlSubdocumento sia true.
	 * 
	 */
	protected void caricaDettaglioDocumentoESubdocumentoEntrataAssociato() {	
		
		int docUid;
		
		if(isLegatoAlDocumento()) {			
			
			docUid = subdocIva.getDocumento().getUid();
			
			
		} else /*if(isLegatoAlSubdocumento())*/ {
			
			this.subdocumentoEntrata = subdocumentoEntrataDad.findSubdocumentoEntrataById(subdocIva.getSubdocumento().getUid());
			
			if(subdocumentoEntrata == null) {
				throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("Impossibile trovare la quota associata al subdocumento IVA"));
			}
			
			docUid = subdocumentoEntrata.getDocumento().getUid();
						
		}		
		
		documentoEntrata = ricercaDettaglioDocumentoEntrata(docUid);
		
		if(documentoEntrata == null) {
			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("Impossibile trovare il Documento finanziario associato al subdocumento IVA"));
		}
	}
	
	/**
	 * Popola il registroIva del subdocumentoIva a partire dall'id del registro.
	 * 
	 */
	protected void caricaRegistroIva() {
		RegistroIva registroIva = registroIvaDad.findRegistroIvaById(subdocIva.getRegistroIva().getUid());
		log.logXmlTypeObject(registroIva, "registro iva trovato");
		if(registroIva==null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("RegistroIva", "uid: "+subdocIva.getRegistroIva().getUid()));
		}
		if( !( TipoRegistroIva.VENDITE_IVA_IMMEDIATA.equals(registroIva.getTipoRegistroIva()) ||
					TipoRegistroIva.VENDITE_IVA_DIFFERITA.equals(registroIva.getTipoRegistroIva())	||
					TipoRegistroIva.CORRISPETTIVI.equals(registroIva.getTipoRegistroIva())) ){
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il registro non puo' essere di tipo "+ registroIva.getTipoRegistroIva().getDescrizione()));
		}
		subdocIva.setRegistroIva(registroIva);			
	}
	
	/**
	 * Ricerca dettaglio documento entrata.
	 *
	 * @param docUid the doc uid
	 * @return the documento entrata
	 */
	protected DocumentoEntrata ricercaDettaglioDocumentoEntrata(int docUid) {
		RicercaDettaglioDocumentoEntrata reqRDD = new RicercaDettaglioDocumentoEntrata();
		reqRDD.setRichiedente(req.getRichiedente());
		DocumentoEntrata ds = new DocumentoEntrata();
		ds.setUid(docUid);
		reqRDD.setDocumentoEntrata(ds);
		RicercaDettaglioDocumentoEntrataResponse resRDD = executeExternalService(ricercaDettaglioDocumentoEntrataService, reqRDD);
		return resRDD.getDocumento();
	}
	
	/**
	 * Esegue i seguenti controlli sulla di registrazione del protocollo provvisorio
	 * (dataProtocolloProvv): 
	 * 
	 * a. non deve essere antecedente alla  data di emissione del documento di spesa (dataEmissione). In caso
	 * contrario il sistema visualizza il messaggio: <FIN_ERR_0192, Data di regis. Antecedente alla data emiss. Del documento> .
	 * 
	 * b. non deve essere inferiore alla data di registrazione dell'ultimo documento registrato nel
	 * sistema in quello stesso registro. 
	 * 
	 * c. deve sempre essere successiva all'ultimo periodo stampato in definitivo per quello stesso registro.
	 */
	protected void checkDataRegistrazioneProtocolloProvvisorio() {
		//Punto a
		if(subdocIva.getDataProtocolloProvvisorio().compareTo(documentoEntrata.getDataEmissione())<0){
			throw new BusinessException(ErroreFin.DATA_REGIST_ANTECEDENTE_DATA_EMISSIONE_DOC.getErrore());
		}
		if(codiciAzioniConsentite.contains("OP-IVA-aggDocIvaEntBackOffice")) {
			return;
		}
		//Punto b	
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(subdocIva.getDataProtocolloProvvisorio());
		int anno = gc.get(GregorianCalendar.YEAR);
		Date ultimaDataProtocolloProvv = contatoreRegistroIvaDad.findDataProtocolloProvv(subdocIva.getRegistroIva().getUid(),  anno); 
		if(ultimaDataProtocolloProvv!= null && subdocIva.getDataProtocolloProvvisorio().compareTo(Utility.truncateToStartOfDay(ultimaDataProtocolloProvv))<0){
			throw new BusinessException(ErroreFin.DATA_DI_REGISTRAZIONE_ANTECEDENTE_ALLA_DATA_DI_REGISTRAZIONE_DELL_ULTIMO_DOCUMENTO_REGISTRATO_NEL_SISTEMA_IN_QUELLO_STESSO_REGISTRO.getErrore());
		}
		//punto c ?
	
	}
		
	/**
	 * Effettua i seguenti controlli sulla data di registrazione del protocollo definitivo (dataProtocolloDef):
	 * 
	 * a. non deve essere antecedente alla data di emissione del documento di spesa (dataEmissione). In caso contrario il sistema
	 * visualizza il messaggio: <FIN_ERR_0192, Data di regis. Antecedente alla
	 * data emiss. Del documento> .
	 * 
	 * b. non deve essere inferiore alla data di registrazione dell’ultimo Subdocumento registrato nel sistema in quello
	 * stesso registro. 
	 * 
	 * c. deve sempre essere successiva all' ultimo periodo stampato in definitivo per quello stesso registro.
	 */
	protected void checkDataRegistrazioneProtocolloDefinitivo() {
		//Punto a
		if(subdocIva.getDataProtocolloDefinitivo().compareTo(documentoEntrata.getDataEmissione())<0){
			throw new BusinessException(ErroreFin.DATA_REGIST_ANTECEDENTE_DATA_EMISSIONE_DOC.getErrore());
		}
		
		if(codiciAzioniConsentite.contains("OP-IVA-aggDocIvaEntBackOffice")) {
			return;
		}
		
		//Punto b
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(subdocIva.getDataProtocolloDefinitivo());
		int anno = gc.get(GregorianCalendar.YEAR);
		Date ultimaDataProtocolloDef = contatoreRegistroIvaDad.findDataProtocolloDef(subdocIva.getRegistroIva().getUid(),  anno); 
		if(ultimaDataProtocolloDef!= null && subdocIva.getDataProtocolloDefinitivo().compareTo(Utility.truncateToStartOfDay(ultimaDataProtocolloDef))<0){
			throw new BusinessException(ErroreFin.DATA_DI_REGISTRAZIONE_ANTECEDENTE_ALLA_DATA_DI_REGISTRAZIONE_DELL_ULTIMO_DOCUMENTO_REGISTRATO_NEL_SISTEMA_IN_QUELLO_STESSO_REGISTRO.getErrore());
		}
		
		//Punto c ?

	}
	
	/**
	 * Ricerca puntuale subdocumento iva entrata.
	 *
	 * @return the subdocumento iva entrata
	 */
	protected SubdocumentoIvaEntrata ricercaPuntualeSubdocumentoIvaEntrata() {
		RicercaPuntualeSubdocumentoIvaEntrata reqRPSI = new RicercaPuntualeSubdocumentoIvaEntrata();
		reqRPSI.setRichiedente(req.getRichiedente());
		reqRPSI.setSubdocumentoIvaEntrata(subdocIva);
		RicercaPuntualeSubdocumentoIvaEntrataResponse resRPSI = executeExternalService(ricercaPuntualeSubdocumentoIvaEntrataService,reqRPSI);
		
		return resRPSI.getSubdocumentoIvaEntrata();
	}
	
	/**
	 * Verifica che il documento abbia associato almeno un movimento iva; in
	 * caso contrario il sistema visualizza il messaggio: <FIN_ERR_0189, Non ci
	 * sono movimenti iva associati>
	 * 
	 * La somma dei totali dei movimenti iva (sommatoria del campo TOTALE
	 * dell'entità Aliquota Subdocumento Iva per tutte le aliquote collegate al
	 * Subdocumento Iva da inserire) deve subire i controlli descritti al par.
	 * 2.5.2:
	 * 
	 * Il Totale dei movimenti iva deve essere: 
	 * 
	 * - sempre minore o uguale
	 * all'importo della quota del documento finanziario nel caso in cui
	 * l'importo della quota sia passato dall'interfaccia chiamante come
	 * parametro di input. In caso contrario il sistema visualizza il messaggio:
	 * <FIN_ERR_0190, Il totale dei movimenti iva non può essere maggiore
	 * dell'importo della quota> 
	 * 
	 * - sempre minore o uguale all'importo rilevante
	 * iva nel caso in cui l'importo della quota non sia passato
	 * dall'interfaccia chiamante come parametro di input. In caso contrario il
	 * sistema visualizza il messaggio: <FIN_ERR_0208, Il totale dei movimenti
	 * iva non può essere maggiore dell'importo rilevante iva> 
	 */
	protected void checkMovimentoIvaDocumento()  {		
		BigDecimal totaleAliquota = subdocIva.calcolaImportoTotaleAliquotaSubdocumentoIva();
		
		if(isLegatoAlSubdocumento()) {
			if(totaleAliquota.compareTo(subdocumentoEntrata.getImporto())>0){
				throw new BusinessException(ErroreFin.TOTALE_MOVIMENTI_IVA_NON_MAGGIORE_IMPORTO_QUOTA.getErrore(totaleAliquota,subdocumentoEntrata.getImporto()));
			}
		} else /*if(isLegatoAlDocumento())*/ {	
			BigDecimal totaleRilevanteIva = documentoEntrata.calcolaImportoTotaleRilevanteIVASubdoumenti();
			if(totaleAliquota.compareTo(totaleRilevanteIva)>0) {
				throw new BusinessException(ErroreFin.TOTALE_MOVIMENTI_IVA_NON_MAGGIORE_TOTALE_IMPORTO_RILEVANTE_IVA.getErrore(totaleAliquota,totaleRilevanteIva));
			}
		}		
	}
	
	/**
	 * Imposta lo stato del subdocumento iva a DEFINITIVO o PROVVISORIO per tipoEsigibilitaIva rispettivamente IMMEDIATA o DIFFERITA.
	 */
	protected void impostaStatoSubdocumentoIva() {
		if(TipoEsigibilitaIva.IMMEDIATA.equals(subdocIva.getRegistroIva().getTipoRegistroIva().getTipoEsigibilitaIva())) {
			subdocIva.setStatoSubdocumentoIva(StatoSubdocumentoIva.DEFINITIVO);
		} else if(TipoEsigibilitaIva.DIFFERITA.equals(subdocIva.getRegistroIva().getTipoRegistroIva().getTipoEsigibilitaIva())) {
			subdocIva.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
		}
	}
	
	/**
	 *  Imposta il numero di protocollo provvisorio o definitivo del subdocumento iva di spesa
	 *  a seconda che il tipo di registro sia rispettivamente ACQUISTI_IVA_DIFFERITA o ACQUISTI_IVA_IMMEDIATA.
	 * 
	 */
	protected void impostaNumeroProtocollo() {
		RegistroIva registro= subdocIva.getRegistroIva();

		//caso 1: acquisto iva esigibilita differita - si imposta solo il protocollo provvisorio
		if(TipoRegistroIva.VENDITE_IVA_DIFFERITA.equals(registro.getTipoRegistroIva()) && subdocIva.getDataProtocolloProvvisorio() != null){
			impostaNumeroProtocolloProvvisorio(registro);
		}
		
		//caso 2: acquisto iva esigibilita immediata o corrispettivi - si imposta solo il protocollo definitivo
		if((TipoRegistroIva.VENDITE_IVA_IMMEDIATA.equals(registro.getTipoRegistroIva()) || TipoRegistroIva.CORRISPETTIVI.equals(registro.getTipoRegistroIva()))
				&& subdocIva.getDataProtocolloDefinitivo() != null){
			impostaNumeroProtocolloDefinitivo(registro);
		}
	}
	
	/**
	 * Imposta il numero protocollo provvisorio del subdocumento iva di entrata che si sta inserendo e 
	 * inizializza (o aggiorna se gia'' presente) il contatore registro iva
	 * per l'annoEserizio, il registro e il periodo presi in considerazione.
	 * 
	 * @param registro il registroIva del subdocumento di entrata che si sta inserendo
	 */
	protected void impostaNumeroProtocolloProvvisorio(RegistroIva registro){
		
//		Periodo p = Periodo.fromCodice(periodo.getCodice());
		Integer numeroProtocolloProvv = contatoreRegistroIvaDad.staccaNumeroProtocolloProv(registro.getUid(), /*p, subdocIva.getAnnoEsercizio(),*/ subdocIva.getDataProtocolloProvvisorio());		
		subdocIva.setNumeroProtocolloProvvisorio(numeroProtocolloProvv);

	}
	
	/**
	 * Imposta il numero protocollo definitivo del subdocumento iva di entrata che si sta inserendo e 
	 * inizializza (o aggiorna se gia'' presente) il contatore registro iva
	 * per l'annoEserizio, il registro e il periodo presi in considerazione.
	 * 
	 * @param registro il registroIva del subdocumento di entrata che si sta inserendo
	 */
	protected void impostaNumeroProtocolloDefinitivo(RegistroIva registro){
		Integer numeroProtocolloDef = contatoreRegistroIvaDad.staccaNumeroProtocolloDef(registro.getUid(), /*p, subdocIva.getAnnoEsercizio(),*/ subdocIva.getDataProtocolloDefinitivo());
		subdocIva.setNumeroProtocolloDefinitivo(numeroProtocolloDef);
	}

	/**
	 * Aggiorna il numeroRegistrazioneIva del subdocumento collegato oppure dei subdocumenti del documento collegato.
	 */
	protected void aggiornaNumeroRegistrazioneIvaSubdocumenti() {
		final String methodName = "aggiornaNumeroRegistrazioneIvaSubdocumenti";
		
		if(isLegatoAlSubdocumento()) {
			log.debug(methodName, "Aggiornamento della quota "+subdocIva.getSubdocumento().getNumero() +" associata al subdocumento con numeroRegistrazioneIVA: " + subdocIva.getNumeroRegistrazioneIVA());
			subdocIva.getSubdocumento().setNumeroRegistrazioneIVA(subdocIva.getNumeroRegistrazioneIVA());
			subdocumentoEntrataDad.aggiornaNumeroRegistrazioneIva(subdocIva.getSubdocumento());
		} else /*if(isLegatoAlDocumento()) */ {
			for(SubdocumentoEntrata s: documentoEntrata.getListaSubdocumenti()) {
				log.debug(methodName, "Aggiornamento della quota "+s.getNumero() +" associata al subdocumento con numeroRegistrazioneIVA: " + subdocIva.getNumeroRegistrazioneIVA());
				s.setNumeroRegistrazioneIVA(subdocIva.getNumeroRegistrazioneIVA());				
				subdocumentoEntrataDad.aggiornaNumeroRegistrazioneIva(s);
			}
		}
	}
	
	/**
	 * Imposta il flagRegistrazioneIva a FALSE se il subdocumentoIva e' legato ad un documento o a TRUE se e' legato ad un subdocumento.
	 */
	protected void impostaFlagRegistrazioneIva() {
		if(isLegatoAlDocumento()){
			subdocIva.setFlagRegistrazioneIva(Boolean.FALSE);
		} else {
			subdocIva.setFlagRegistrazioneIva(Boolean.TRUE);
		}
	}
	

}
