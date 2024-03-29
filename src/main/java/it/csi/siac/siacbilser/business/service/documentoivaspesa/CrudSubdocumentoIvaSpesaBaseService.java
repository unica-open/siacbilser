/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaspesa;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaDettaglioDocumentoSpesaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.ContatoreRegistroIvaDad;
import it.csi.siac.siacbilser.integration.dad.PeriodoDad;
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaSpesaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoEsigibilitaIva;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Base service per il crud dei subdocumenti iva di Spesa.
 *
 * @author Domenico
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class CrudSubdocumentoIvaSpesaBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {
	
	@Autowired 
	protected RicercaPuntualeSubdocumentoIvaSpesaService ricercaPuntualeSubdocumentoIvaSpesaService;
	@Autowired 
	protected RicercaDettaglioDocumentoSpesaService ricercaDettaglioDocumentoSpesaService;
	@Autowired
	protected SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	protected ContatoreRegistroIvaDad contatoreRegistroIvaDad;
	@Autowired
	protected SubdocumentoIvaSpesaDad subdocumentoIvaSpesaDad;
	@Autowired
	protected PeriodoDad periodoDad;
	@Autowired
	protected RegistroIvaDad registroIvaDad;
	
		
	protected SubdocumentoIvaSpesa subdocIva;	
	protected DocumentoSpesa documentoSpesa; 	
	protected SubdocumentoSpesa subdocumentoSpesa; 
	protected Bilancio bilancio;
	
	private DecimalFormat df = (DecimalFormat)NumberFormat.getInstance(Locale.ITALY);	 
	
	
	@Override
	protected void init() {
		super.init();
		periodoDad.setEnte(ente);
		contatoreRegistroIvaDad.setEnte(ente);
		contatoreRegistroIvaDad.setLoginOperazione(loginOperazione);	
		subdocumentoIvaSpesaDad.setEnte(ente);

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
	 * Inizializza le variabili di istanza documentoSpesa e subdocumentoSpesa. 
	 * Quest'ultimo solo nel caso isLegatoAlSubdocumento sia true.
	 * 
	 */
	protected void caricaDettaglioDocumentoESubdocumentoSpesaAssociato() {	
		
		int docUid;
		
		if(isLegatoAlDocumento()) {			
			
			docUid = subdocIva.getDocumento().getUid();
			
			
		} else /*if(isLegatoAlSubdocumento())*/ {
			
			this.subdocumentoSpesa = subdocumentoSpesaDad.findSubdocumentoSpesaById(subdocIva.getSubdocumento().getUid());
			
			if(subdocumentoSpesa == null) {
				throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("Impossibile trovare la quota associata al subdocumento IVA"));
			}
			
			docUid = subdocumentoSpesa.getDocumento().getUid();
						
		}		
		
		documentoSpesa = ricercaDettaglioDocumentoSpesa(docUid);
		
		if(documentoSpesa == null) {
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
		if( !( TipoRegistroIva.ACQUISTI_IVA_DIFFERITA.equals(registroIva.getTipoRegistroIva()) ||
						TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA.equals(registroIva.getTipoRegistroIva())	) ){
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il registro non puo' essere di tipo "+ registroIva.getTipoRegistroIva().getDescrizione()));
		}
		subdocIva.setRegistroIva(registroIva);			
	}
	
	/**
	 * Ricerca dettaglio documento spesa.
	 *
	 * @param docUid the doc uid
	 * @return the documento spesa
	 */
	protected DocumentoSpesa ricercaDettaglioDocumentoSpesa(int docUid) {
		RicercaDettaglioDocumentoSpesa reqRDD = new RicercaDettaglioDocumentoSpesa();
		reqRDD.setRichiedente(req.getRichiedente());
		DocumentoSpesa ds = new DocumentoSpesa();
		ds.setUid(docUid);
		reqRDD.setDocumentoSpesa(ds);
		RicercaDettaglioDocumentoSpesaResponse resRDD = executeExternalService(ricercaDettaglioDocumentoSpesaService, reqRDD);
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
		if(subdocIva.getDataProtocolloProvvisorio().compareTo(documentoSpesa.getDataEmissione())<0){
			throw new BusinessException(ErroreFin.DATA_REGIST_ANTECEDENTE_DATA_EMISSIONE_DOC.getErrore());
		}
		if(codiciAzioniConsentite.contains("OP-IVA-aggDocIvaSpeBackOffice")) {
			return;
		}
		//Punto b	
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(subdocIva.getDataProtocolloProvvisorio());
		int anno = gc.get(GregorianCalendar.YEAR);
		Date ultimaDataProtocolloProvv = contatoreRegistroIvaDad.findDataProtocolloProvv(subdocIva.getRegistroIva().getUid(), anno); 
		if(ultimaDataProtocolloProvv!= null && subdocIva.getDataProtocolloProvvisorio().compareTo(Utility.truncateToStartOfDay(ultimaDataProtocolloProvv))<0){
			throw new BusinessException(ErroreFin.DATA_DI_REGISTRAZIONE_ANTECEDENTE_ALLA_DATA_DI_REGISTRAZIONE_DELL_ULTIMO_DOCUMENTO_REGISTRATO_NEL_SISTEMA_IN_QUELLO_STESSO_REGISTRO.getErrore());
		}
		//Punto c?
	
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
		if(subdocIva.getDataProtocolloDefinitivo().compareTo(documentoSpesa.getDataEmissione())<0){
			throw new BusinessException(ErroreFin.DATA_REGIST_ANTECEDENTE_DATA_EMISSIONE_DOC.getErrore());
		}
		if(codiciAzioniConsentite.contains("OP-IVA-aggDocIvaSpeBackOffice")) {
			log.debug("checkDataRegistrazioneProtocolloDefinitivo", "l'utente e' abilitato per azioni bo, non controllo la coerenza delle date");
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
	 * Ricerca puntuale subdocumento iva spesa.
	 *
	 * @return the subdocumento iva spesa
	 */
	protected SubdocumentoIvaSpesa ricercaPuntualeSubdocumentoIvaSpesa() {
		RicercaPuntualeSubdocumentoIvaSpesa reqRPSI = new RicercaPuntualeSubdocumentoIvaSpesa();
		reqRPSI.setRichiedente(req.getRichiedente());
		reqRPSI.setSubdocumentoIvaSpesa(subdocIva);
		RicercaPuntualeSubdocumentoIvaSpesaResponse resRPSI = executeExternalService(ricercaPuntualeSubdocumentoIvaSpesaService,reqRPSI);
		
		return resRPSI.getSubdocumentoIvaSpesa();
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
		final String methodName = "checkMovimentoIvaDocumento";
		BigDecimal totaleAliquota = subdocIva.calcolaImportoTotaleAliquotaSubdocumentoIva();
		
		log.debug(methodName, "totaleAliquota: " + totaleAliquota);
		
		if(isLegatoAlSubdocumento()) {
			if(totaleAliquota.compareTo(subdocumentoSpesa.getImporto())>0){
				throw new BusinessException(ErroreFin.TOTALE_MOVIMENTI_IVA_NON_MAGGIORE_IMPORTO_QUOTA.getErrore(df.format(totaleAliquota),
						df.format(subdocumentoSpesa.getImporto())));
			}
		} else /*if(isLegatoAlDocumento())*/ {	
			BigDecimal totaleRilevanteIva = documentoSpesa.calcolaImportoTotaleRilevanteIVASubdoumenti();
			if(totaleAliquota.compareTo(totaleRilevanteIva)>0) {
				throw new BusinessException(ErroreFin.TOTALE_MOVIMENTI_IVA_NON_MAGGIORE_TOTALE_IMPORTO_RILEVANTE_IVA.getErrore(df.format(totaleAliquota),
						df.format(totaleRilevanteIva)));
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
		if(TipoRegistroIva.ACQUISTI_IVA_DIFFERITA.equals(registro.getTipoRegistroIva()) && subdocIva.getDataProtocolloProvvisorio() != null){
			impostaNumeroProtocolloProvvisorio(registro);
			
		}		
		//caso 2: acquisto iva esigibilita immediata  o corrispettivi- si imposta solo il protocollo definitivo
		if((TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA.equals(registro.getTipoRegistroIva()) || TipoRegistroIva.CORRISPETTIVI.equals(registro.getTipoRegistroIva()))
				&& subdocIva.getDataProtocolloDefinitivo() != null){
			impostaNumeroProtocolloDefinitivo(registro);
		}	
	}
	
	/**
	 * Imposta il numero protocollo provvisorio del subdocumento iva di spesa che si sta inserendo e 
	 * inizializza (o aggiorna se gia'' presente) il contatore registro iva
	 * per l'annoEserizio, il registro e il periodo presi in considerazione.
	 * 
	 * @param registro il registroIva del subdocumento di spesa che si sta inserendo
	 */
	protected void impostaNumeroProtocolloProvvisorio(RegistroIva registro){
		
		Integer numeroProtocolloProvv = contatoreRegistroIvaDad.staccaNumeroProtocolloProv(registro.getUid(), /*p, subdocIva.getAnnoEsercizio(),*/ subdocIva.getDataProtocolloProvvisorio());		
		subdocIva.setNumeroProtocolloProvvisorio(numeroProtocolloProvv);
	}
	
	/**
	 * Imposta il numero protocollo definitivo del subdocumento iva di spesa che si sta inserendo e
	 * inizializza (o aggiorna se gia'' presente) il contatore registro iva
	 * per l'annoEserizio, il registro e il periodo presi in considerazione.
	 * 
	 * @param registro il registroIva del subdocumento di spesa che si sta inserendo
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
			subdocumentoSpesaDad.aggiornaNumeroRegistrazioneIva(subdocIva.getSubdocumento());
		} else /*if(isLegatoAlDocumento()) */ {
			for(SubdocumentoSpesa s: documentoSpesa.getListaSubdocumenti()) {
				log.debug(methodName, "Aggiornamento della quota "+s.getNumero() +" associata al subdocumento con numeroRegistrazioneIVA: " + subdocIva.getNumeroRegistrazioneIVA());
				s.setNumeroRegistrazioneIVA(subdocIva.getNumeroRegistrazioneIVA());				
				subdocumentoSpesaDad.aggiornaNumeroRegistrazioneIva(s);
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
	
	/** Popola un subdocumento iva di entrata da inserire come controregistazione contestualmente
	 * all'inserimento di un documento iva di spesa intracomunitario.
	 * 
	 * @return controregistrazione il subdocumentoIvaEntrata da inserire
	 * 
	 */
	protected SubdocumentoIvaEntrata popolaControregistrazione() {
		SubdocumentoIvaEntrata controregistrazione = subdocIva.getSubdocumentoIvaEntrata();
		
		
		SubdocumentoIvaSpesa padre = new SubdocumentoIvaSpesa();
		padre.setUid(subdocIva.getUid());
		controregistrazione.setSubdocumentoIvaPadre(padre);
		controregistrazione.setDescrizioneIva("Controregistrazione per IVA intracomunitaria");
		
		TipoRegistrazioneIva tipoRegistrazioneIva = subdocumentoIvaSpesaDad.findTipoRegistrazioneIvaIntrastat();
		controregistrazione.setTipoRegistrazioneIva(tipoRegistrazioneIva);
		
		controregistrazione.setTipoRelazione(TipoRelazione.CONTROREGISTRAZIONE_INTRASTAT);
		
		//copio altri campi dal subdocIvaSpesa
		controregistrazione.setAnnoEsercizio(subdocIva.getAnnoEsercizio());
		controregistrazione.setEnte(subdocIva.getEnte());
		
		controregistrazione.setDataCassaDocumento(subdocIva.getDataCassaDocumento());
		controregistrazione.setDataFineValidita(subdocIva.getDataFineValidita());
		controregistrazione.setDataOrdinativoDocumento(subdocIva.getDataOrdinativoDocumento());
		
		controregistrazione.setDataProtocolloDefinitivo(subdocIva.getDataProtocolloDefinitivo()!=null?subdocIva.getDataProtocolloDefinitivo():subdocIva.getDataProtocolloProvvisorio());
		controregistrazione.setDataRegistrazione(subdocIva.getDataRegistrazione());
		
		controregistrazione.setImportoInValuta(subdocIva.getImportoInValuta());
		controregistrazione.setValuta(subdocIva.getValuta());
		// Controllare se sia necessario clonare le aliquote ed inserirne altre ex novo
		controregistrazione.setListaAliquotaSubdocumentoIva(subdocIva.getListaAliquotaSubdocumentoIva());
		
		controregistrazione.setNumeroOrdinativoDocumento(subdocIva.getNumeroOrdinativoDocumento());
		controregistrazione.setStatoSubdocumentoIva(StatoSubdocumentoIva.DEFINITIVO);
		
		return controregistrazione;
	}
	

}
