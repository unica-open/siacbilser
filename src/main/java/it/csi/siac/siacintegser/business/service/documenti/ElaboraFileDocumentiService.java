/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.documenti;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.documento.ClassificatoreBilServiceCallGroup;
import it.csi.siac.siacbilser.business.service.documento.DocumentoServiceCallGroup;
import it.csi.siac.siacbilser.business.service.documento.GenericServiceCallGroup;
import it.csi.siac.siacbilser.business.service.documento.InserisceElenchiDocumentiService;
import it.csi.siac.siacbilser.business.service.documento.MovimentoGestioneServiceCallGroup;
import it.csi.siac.siacbilser.business.service.documento.ProvvedimentoServiceCallGroup;
import it.csi.siac.siacbilser.business.service.documento.SoggettoServiceCallGroup;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.StatoFile.CodiceStatoFile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenchiDocumenti;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenchiDocumentiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770Response;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBolloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvvisoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValutaResponse;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElenchiDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.NoteTesoriere;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.RitenuteDocumento;
import it.csi.siac.siacfin2ser.model.SospensioneSubdocumento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileBaseService;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

/**
 * Elaborazione dei File dei Documenti.
 * Prepara il flusso xml ricevuto per richiamare il servizio di Business {@link InserisceElenchiDocumentiService}
 * 
 * @author Domenico
 * @see InserisceElenchiDocumentiService
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileDocumentiService extends ElaboraFileBaseService {
	
	private static final String CODICE_SIOPE_TIPO_DEBITO_COMMERCIALE = "CO";
	
	protected LogUtilInner log = new LogUtilInner(this.getClass());
	
	private ElenchiDocumentiAllegato elenchiDocumentiAllegato;
	
	private DocumentoServiceCallGroup documentoServiceCallGroup;
	private SoggettoServiceCallGroup soggettoServiceCallGroup;
	private ProvvedimentoServiceCallGroup provvedimentoServiceCallGroup;
	private MovimentoGestioneServiceCallGroup movimentoGestioneServiceCallGroup;
	private GenericServiceCallGroup genericServiceCallGroup;
	private ClassificatoreBilServiceCallGroup classificatoreBilServiceCallGroup;
	
	@Autowired
	private CodificaDad codificaDad;
	
	@Override
	public void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkEntita(req.getBilancio(), "bilancio");
		checkCondition(req.getBilancio().getAnno()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio")); //Utilizzato per i servizi FIN!
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT /* NON supportato dal driver 9.1 di postgresql! */)
	public ElaboraFileResponse executeServiceTxRequiresNew(ElaboraFile serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		
		this.documentoServiceCallGroup = new DocumentoServiceCallGroup(serviceExecutor, req.getRichiedente(), ente, req.getBilancio());
		this.soggettoServiceCallGroup = new SoggettoServiceCallGroup(serviceExecutor, req.getRichiedente(), ente);
		this.provvedimentoServiceCallGroup = new ProvvedimentoServiceCallGroup(serviceExecutor, req.getRichiedente(), ente);
		this.movimentoGestioneServiceCallGroup = new MovimentoGestioneServiceCallGroup(serviceExecutor, req.getRichiedente(), ente, req.getBilancio());
		this.genericServiceCallGroup = new GenericServiceCallGroup(serviceExecutor, req.getRichiedente(), ente);
		this.classificatoreBilServiceCallGroup = new ClassificatoreBilServiceCallGroup(serviceExecutor, req.getRichiedente());
	}
	
	
	//############################################################################################################
	//############################ Caricamento e inizializzazione del flusso xml #################################
	//############################################################################################################

	@Override
	protected void initFileData() {
		String methodName = "initFileData";
		String xml;
		try {
			xml = new String(fileBytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			String msg = "Impossibile leggere il file: Charset UTF-8 non supportato.";
			log.error(methodName, msg);
			throw new BusinessException(msg);
		}
		
		elenchiDocumentiAllegato = JAXBUtility.unmarshall(xml, ElenchiDocumentiAllegato.class);
		
		//Vedi sezione: Inizializzazione delle Reference nel flusso xml 
		initXmlFlowReferences();
		
		//Vedi sezione: Check Parametri propedeutici all'inizializzazione degli Uids
		checkParamsForInitEntitiesUidsBase();
		
		//Vedi sezione: Inizializzazione degli uids necessari per il servizio InserisceElenchiDocumentiService
		initEntitiesUids();
	}
	
	
	
	//############################################################################################################
	//########################## Inizializzazione delle Reference nel flusso xml #################################
	//############################################################################################################

	/**
	 * All'interno del flusso XML per tutti gli elementi che si ripetono, solo il primo contiene le informazioni complete.
	 * Questo metodo ricollega i puntatori dell'oggetto Java ottenuto con JAXB a quello ottenuto dal primo elemento del flusso XML.
	 * In questo modo scorrendo l'oggetto ogni putamento agli oggetti ripetuti avrà lo stesso puntamento in memoria e di conseguenza l'invocazione di 
	 * un metodo set su quell'oggetto sarà comune a tutti i sui punti.
	 * 
	 * Sostanzialmente vengono impostate le refenences per uid (o eventualmente un flowUid) nel flusso xml sullo stesso Oggetto JAXB.
	 * 
	 * NOTA: gli uid inizializzati nel flusso XML sono negativi in modo da poter essere distinti da quelli reali su DB. (valutare se ne vale la pena! o se lasciarli "liberi")
	 * 
	 */
	private void initXmlFlowReferences() {
		Map<Integer,AllegatoAtto> allegatoAttoReferenceMap = new HashMap<Integer,AllegatoAtto>();
		Map<Integer,DocumentoSpesa> documentoSpesaReferenceMap = new HashMap<Integer,DocumentoSpesa>();
		Map<Integer,DocumentoEntrata> documentoEntrataReferenceMap = new HashMap<Integer,DocumentoEntrata>();
		
		
		//match per id 
		for(ElencoDocumentiAllegato elencoDocumentiAllegato : elenchiDocumentiAllegato.getElenchiDocumentiAllegato()){
			
			AllegatoAtto allegatoAtto = elencoDocumentiAllegato.getAllegatoAtto();
			elencoDocumentiAllegato.setAllegatoAtto(getReference(allegatoAttoReferenceMap, allegatoAtto));
			allegatoAtto = elencoDocumentiAllegato.getAllegatoAtto();
			
			//############ SubdocumentiSpesa
			for(SubdocumentoSpesa subdocumentoSpesa : elencoDocumentiAllegato.getSubdocumentiSpesa()) {
				
				DocumentoSpesa documentoSpesa = subdocumentoSpesa.getDocumento();
				subdocumentoSpesa.setDocumento(getReference(documentoSpesaReferenceMap, documentoSpesa));
				documentoSpesa = subdocumentoSpesa.getDocumento();
				
				if(documentoSpesa != null) {
					//######## DocumentiSpesaFiglio
					List<DocumentoSpesa> listaDocumentiSpesaFiglio = new ArrayList<DocumentoSpesa>();
					for(DocumentoSpesa documentoSpesaFiglio : documentoSpesa.getListaDocumentiSpesaFiglio()) {
						DocumentoSpesa reference = getReference(documentoSpesaReferenceMap, documentoSpesaFiglio);
						if(reference != null) {
							listaDocumentiSpesaFiglio.add(reference);
						}
					}
					documentoSpesa.setListaDocumentiSpesaFiglio(listaDocumentiSpesaFiglio); //Questo subdocumentoSpesa referenzia lo stesso presente nel primo tag.
					
					
					//######## DocumentiEntrataFiglio
					List<DocumentoEntrata> listaDocumentiEntrataFiglio = new ArrayList<DocumentoEntrata>();
					for(DocumentoEntrata documentoEntrataFiglio : documentoSpesa.getListaDocumentiEntrataFiglio()) {
						DocumentoEntrata reference = getReference(documentoEntrataReferenceMap, documentoEntrataFiglio);
						if(reference != null) {
							listaDocumentiEntrataFiglio.add(reference);
						}
					}
					documentoSpesa.setListaDocumentiEntrataFiglio(listaDocumentiEntrataFiglio);//Questo subdocumentoEntrata referenzia lo stesso presente nel primo tag.
				}
			}
			
			//############ SubdocumentiEntrata
			for(SubdocumentoEntrata subdocumentoEntrata : elencoDocumentiAllegato.getSubdocumentiEntrata()) {
				
				DocumentoEntrata documentoEntrata = subdocumentoEntrata.getDocumento();
				subdocumentoEntrata.setDocumento(getReference(documentoEntrataReferenceMap, documentoEntrata));
				documentoEntrata = subdocumentoEntrata.getDocumento();
				
				if(documentoEntrata != null) {
					//######## DocumentiSpesaFiglio
					List<DocumentoSpesa> listaDocumentiSpesaFiglio = new ArrayList<DocumentoSpesa>();
					for(DocumentoSpesa documentoSpesaFiglio : documentoEntrata.getListaDocumentiSpesaFiglio()) {
						DocumentoSpesa reference = getReference(documentoSpesaReferenceMap, documentoSpesaFiglio);
						if(reference != null) {
							listaDocumentiSpesaFiglio.add(reference);
						}
					}
					documentoEntrata.setListaDocumentiSpesaFiglio(listaDocumentiSpesaFiglio); //Questo subdocumentoSpesa referenzia lo stesso presente nel primo tag.
					
					
					//######## DocumentiEntrataFiglio
					List<DocumentoEntrata> listaDocumentiEntrataFiglio = new ArrayList<DocumentoEntrata>();
					for(DocumentoEntrata documentoEntrataFiglio : documentoEntrata.getListaDocumentiEntrataFiglio()) {
						DocumentoEntrata reference = getReference(documentoEntrataReferenceMap, documentoEntrataFiglio);
						if(reference != null) {
							listaDocumentiEntrataFiglio.add(reference);
						}
					}
					documentoEntrata.setListaDocumentiEntrataFiglio(listaDocumentiEntrataFiglio);//Questo subdocumentoEntrata referenzia lo stesso presente nel primo tag.
				}
			}
			
			//############ SubdocumentiIvaSpesa
			for(SubdocumentoIvaSpesa subdocumentoIvaSpesa : elencoDocumentiAllegato.getSubdocumentiIvaSpesa()) {
				DocumentoSpesa documentoSpesa = subdocumentoIvaSpesa.getDocumento();
				subdocumentoIvaSpesa.setDocumento(getReference(documentoSpesaReferenceMap, documentoSpesa));
			}
			
			
			//############ SubdocumentiIvaEntrata
			for(SubdocumentoIvaEntrata subdocumentoIvaEntrata : elencoDocumentiAllegato.getSubdocumentiIvaEntrata()) {
				DocumentoEntrata documentoEntrata = subdocumentoIvaEntrata.getDocumento();
				subdocumentoIvaEntrata.setDocumento(getReference(documentoEntrataReferenceMap, documentoEntrata));
			}
		}
		
		//log.logXmlTypeObject(elenchiDocumentiAllegato, "elenchiDocumentiAllegato referenziato.");
	}
	
	
	/**
	 * Restituisce il primo reference dell'{@link Entita} ({@link DocumentoSpesa}, {@link DocumentoEntrata}, {@link AllegatoAtto}) 
	 * passata come parametro.
	 * Il primo elemento nell'xml DEVE contenere le informazioni complete. I successivi avranno lo stesso uid solo per essere referenziati. 
	 * In questo modo potranno evitare di ripetere tutte le informazioni in tutti tag successivi.
	 * 
	 * @param referenceMap
	 * @param entita
	 * @return
	 */
	private <E extends Entita> E getReference(Map<Integer, E> referenceMap, E entita) {
		if(entita == null) {
			return null;
		}
		int uid = entita.getUid();
		
		if(uid >= 0) {
			throw new BusinessException(ErroreCore.FORMATO_NON_VALIDO.getErrore("uid " + entita.getClass().getSimpleName(),
					": nel flusso xml vanno assegnati degli uid minori di zero per ogni " + entita.getClass().getSimpleName()
					+ " in modo da poterli referenziare all'interno del flusso stesso. Valore fornito: " + uid));
		}
		
		if(!referenceMap.containsKey(uid)) { 
			referenceMap.put(uid, entita);
			return entita;
		} else {
			//In questo modo mi assicuro di restituire sempre la stessa reference per ogni elemento della lista ed assicurare quindi che ognuno abbia i dati completi:
			return referenceMap.get(uid); //Queste Entita referenzia lo stessa presente nel primo tag.
		}
		
	}
	
	

	//############################################################################################################
	//################### Check Parametri propedeutici all'inizializzazione degli Uids ###########################
	//############################################################################################################
	
	
	//############################### Attenzione!! ###############################################################
	//### Questi check vanno intesi come propedeutici a verificare i dati necessari per effettuare lo step di 
	//### inizializzazione degli Uids delle Entites ovvero il metodo initEntitiesUids().
	//### I check di parametri obbligatori per la business logic del servizio sono su InserisceElenchiDocumentiService.
	//### In questa sezione NON vanno duplicati i check già presenti in tale servizio e nei servizi da esso richiamati. 
	//### Se serve aggiungere check obbligatori alla business aggiungerli in InserisceElenchiDocumentiService non qui.
	//############################################################################################################
	
	
	private void checkParamsForInitEntitiesUidsBase() {
		String methodName = "checkParamsForInitEntitiesUidsBase";
		
		log.debug(methodName, "Controllo dei parametri necessari ad effettuare l'inizializzazione degli id necessari al richiamo del servizio InserisceElenchiDocumentiService.");
		
		try {
			checkParamsForInitEntitiesUids();
		} catch (ServiceParamError spe) {
			log.error(methodName, spe.getMessage(), spe);
			throw new BusinessException(spe.getMessage());
		}
		// SIAC-2263 normalizzo le date
		normalizzazioneDate();
	}
	
	private void checkParamsForInitEntitiesUids() throws ServiceParamError {
		for(ElencoDocumentiAllegato elencoDocumentiAllegato : elenchiDocumentiAllegato.getElenchiDocumentiAllegato()) {
			
			//righe 3 - 10
			if (elencoDocumentiAllegato.getAllegatoAtto() != null && elencoDocumentiAllegato.getAllegatoAtto().getAttoAmministrativo() != null) {
				checkAllegatoAtto(elencoDocumentiAllegato.getAllegatoAtto());
			}

			boolean documentoPresente = false;
			
			// righe 11 - 45
			for(SubdocumentoSpesa subdocumentoSpesa : elencoDocumentiAllegato.getSubdocumentiSpesa()) {
				documentoPresente = true;
				DocumentoSpesa documentoSpesa = subdocumentoSpesa.getDocumento();
				checkDocumentoSpesa(documentoSpesa);
				
				//righe 74 105: Quota spesa e provvisorio cassa
				checkSubdocumentoSpesa(subdocumentoSpesa);
			}
			
			// righe 46 Subdocumento IVA SPESA
			for(SubdocumentoIvaSpesa subdocumentoIvaCollegato : elencoDocumentiAllegato.getSubdocumentiIvaSpesa()){
				if (subdocumentoIvaCollegato != null) {
					String identificativoDocumento = subdocumentoIvaCollegato.getAnnoEsercizio() + " " + subdocumentoIvaCollegato.getNumeroProtocolloDefinitivo(); 
					checkSubdocumentoIVASpesa(subdocumentoIvaCollegato,	identificativoDocumento);
				}
			}

			for(SubdocumentoEntrata subdocumentoEntrata : elencoDocumentiAllegato.getSubdocumentiEntrata()) {
				documentoPresente = true;
				DocumentoEntrata documentoEntrata = subdocumentoEntrata.getDocumento();
				
				// 139 - 151
				checkDocumentoEntrata(documentoEntrata);
				
				//Quota entrata
				checkSubdocumentoEntrata(subdocumentoEntrata);
			}
			
			// righe 46 - 138
			for(SubdocumentoIvaEntrata subdocumentoIvaCollegato : elencoDocumentiAllegato.getSubdocumentiIvaEntrata()){
				if (subdocumentoIvaCollegato != null) {
					checkSubdocumentoIVAEntrata(subdocumentoIvaCollegato);
				}
			}
			
			if (!documentoPresente) {
//				res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento di spesa o entrata legato all'elenco"));
			}
		}
		
		if(res.hasErrori()) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("dati non validi per elaborazione"));
		}
	}

	private void checkAllegatoAtto(AllegatoAtto allegatoAtto) throws ServiceParamError {
		checkNotNull(allegatoAtto.getAttoAmministrativo().getAnno(), "Anno atto amministrativo", false);
//		checkNotNull(allegatoAtto.getAttoAmministrativo().getNumero(), "Numero atto amministrativo", false); //per gli atti di tipo ALG non si vuole spcificare il numero.
		checkCodifica(allegatoAtto.getAttoAmministrativo().getTipoAtto(), "tipo atto amministrativo", false);
		
		//Da analisi e' obbligatorio, su base dati no
//		checkCodifica(allegatoAtto.getAttoAmministrativo().getStrutturaAmmContabile(), "Struttura Amministrativo contabile");
	}

	private void checkSubdocumentoEntrata(SubdocumentoEntrata subdocumentoEntrata) throws ServiceParamError {
		checkSubdocumento(subdocumentoEntrata);
	}

	private void checkSubdocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa) throws ServiceParamError {
		checkSubdocumento(subdocumentoSpesa);
		
		checkNotNull(subdocumentoSpesa.getModalitaPagamentoSoggetto(), "Modalita pagamento soggetto", false);
		if(subdocumentoSpesa.getModalitaPagamentoSoggetto() != null) {
			checkNotBlank(subdocumentoSpesa.getModalitaPagamentoSoggetto().getCodiceModalitaPagamento(), "Modalita pagamento soggetto", false);
		}
	}
	
	private void checkSubdocumento(Subdocumento<?, ?> subdocumento) throws ServiceParamError {
		if(subdocumento.getAttoAmministrativo()!=null){
			checkNotNull(subdocumento.getAttoAmministrativo().getAnno(), "Anno atto amministrativo subdocumento", false);
//			checkNotNull(subdocumento.getAttoAmministrativo().getNumero(), "Numero atto amministrativo subdocumento", false);
			checkCodifica(subdocumento.getAttoAmministrativo().getTipoAtto(), "tipo atto amministrativo subdocumento", false);
		}
		
		if(subdocumento.getTipoAvviso() != null && StringUtils.isNotBlank(subdocumento.getTipoAvviso().getCodice())) {
			checkCondition(subdocumento.getDocumento() != null && subdocumento.getDocumento().getAnno() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("anno documento"), false);
		}
	}
	
	private void checkSubdocumentoIVAEntrata(SubdocumentoIvaEntrata subdocumentoIvaCollegato) throws ServiceParamError {
		String identificativoDocumento = subdocumentoIvaCollegato.getAnnoEsercizio() + " " + subdocumentoIvaCollegato.getNumeroProtocolloDefinitivo();
		checkCodifica(subdocumentoIvaCollegato.getTipoRegistrazioneIva(), "tipo registrazione iva", false);
		
		checkNotNull(subdocumentoIvaCollegato.getRegistroIva(), "Registro subdocumento IVA entrata collegato " + identificativoDocumento, false);
		if(subdocumentoIvaCollegato.getRegistroIva() != null) {
			checkNotBlank(subdocumentoIvaCollegato.getRegistroIva().getCodice(), "Registro subdocumento IVA entrata collegato " + identificativoDocumento, false);
		}
		
		for (AliquotaSubdocumentoIva aliquota : subdocumentoIvaCollegato.getListaAliquotaSubdocumentoIva()) {
			checkCodifica(aliquota.getAliquotaIva(), "Aliquota IVA subdocumento IVA entrata collegato " + identificativoDocumento, false);
		}
	}

	private void checkDocumentoEntrata(DocumentoEntrata documentoEntrata) throws ServiceParamError {
		checkCodifica(documentoEntrata.getTipoDocumento(), "tipo documento", false);
		
		checkNotNull(documentoEntrata.getSoggetto(), "soggetto", false);
		if(documentoEntrata.getSoggetto() != null) {
			checkNotBlank(documentoEntrata.getSoggetto().getCodiceSoggetto(), "codice soggetto", false);
		}
		
		//note credito
		if (documentoEntrata.getListaNoteCreditoEntrataFiglio() != null) {
			for (DocumentoEntrata documentoEntrataNota : documentoEntrata.getListaNoteCreditoEntrataFiglio()) {
				checkDocumentoEntrata(documentoEntrataNota);
			}
		}
		
		//documenti spesa collegati
		if (documentoEntrata.getListaDocumentiSpesaFiglio() != null) {
			for (DocumentoSpesa documentoSpesa : documentoEntrata.getListaDocumentiSpesaFiglio()) {
				checkDocumentoSpesa(documentoSpesa);
			}
		}
		
		//documenti iva collegati
		if (documentoEntrata.getListaSubdocumentoIva() != null) {
			for (SubdocumentoIvaEntrata documentoIVAEntrata : documentoEntrata.getListaSubdocumentoIva()) {
				checkSubdocumentoIVAEntrata(documentoIVAEntrata);
			}
		}
		
		if(documentoEntrata.getListaSubdocumenti() != null) {
			for(SubdocumentoEntrata subdocumentoEntrata : documentoEntrata.getListaSubdocumenti()) {
				checkSubdocumentoEntrata(subdocumentoEntrata);
			}
		}
	}
	
	private void checkSubdocumentoIVASpesa(SubdocumentoIvaSpesa subdocumentoIvaCollegato, String identificativoDocumento) throws ServiceParamError {
		
		checkCodifica(subdocumentoIvaCollegato.getTipoRegistrazioneIva(), "Tipo Registrazione subdocumento IVA spesa collegato " + identificativoDocumento, false);
		checkNotNull(subdocumentoIvaCollegato.getRegistroIva(), "Registro subdocumento IVA spesa collegato " + identificativoDocumento, false);
		if(subdocumentoIvaCollegato.getRegistroIva()!=null) {
			checkNotBlank(subdocumentoIvaCollegato.getRegistroIva().getCodice(), "Registro subdocumento IVA spesa collegato " + identificativoDocumento, false);
		}
		for (AliquotaSubdocumentoIva aliquota : subdocumentoIvaCollegato.getListaAliquotaSubdocumentoIva()) {
			checkCodifica(aliquota.getAliquotaIva(), "Aliquota IVA", false);
		}
	}

	private void checkDocumentoSpesa(DocumentoSpesa documentoSpesa) throws ServiceParamError {
		checkCodifica(documentoSpesa.getTipoDocumento(), "tipo documento", false);
		
		checkNotNull(documentoSpesa.getSoggetto(), "soggetto", false);
		if(documentoSpesa.getSoggetto() != null) {
			checkNotBlank(documentoSpesa.getSoggetto().getCodiceSoggetto(), "codice soggetto", false);
		}
		
		//65
		if (documentoSpesa.getRitenuteDocumento() != null && documentoSpesa.getRitenuteDocumento().getListaOnere() != null) {
			for (DettaglioOnere dettaglioOnere : documentoSpesa.getRitenuteDocumento().getListaOnere()) {
				checkCondition(dettaglioOnere.getTipoOnere()!=null && StringUtils.isNotBlank(dettaglioOnere.getTipoOnere().getCodice()), 
						ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo onere"), false);
				if(dettaglioOnere.getTipoOnere() != null) {
					checkNotNull(dettaglioOnere.getTipoOnere().getNaturaOnere()!=null 
							&& StringUtils.isNotBlank(dettaglioOnere.getTipoOnere().getNaturaOnere().getCodice()), 
							ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("natura tipo onere"), false);
				}
				
			}
		}
		
		// farlo per i documenti di entrata collegati e per le note di credito (documenti spesa)
		if (documentoSpesa.getListaNoteCreditoEntrataFiglio() != null) {
			for (DocumentoEntrata documentoEntrataNota : documentoSpesa.getListaNoteCreditoEntrataFiglio()) {
				checkDocumentoEntrata(documentoEntrataNota);
			}
		}
		if (documentoSpesa.getListaDocumentiSpesaFiglio() != null) {
			for (DocumentoSpesa documentoEntrataNota : documentoSpesa.getListaDocumentiSpesaFiglio()) {
				checkDocumentoSpesa(documentoEntrataNota);
			}
		}
		
		//documenti iva collegati
		if (documentoSpesa.getListaSubdocumentoIva() != null) {
			for (SubdocumentoIvaSpesa documentoIVASpesa : documentoSpesa.getListaSubdocumentoIva()) {
				checkSubdocumentoIVASpesa(documentoIVASpesa, "");
			}
		}
		
		if(documentoSpesa.getListaSubdocumenti() != null) {
			for(SubdocumentoSpesa subdocumentoSpesa : documentoSpesa.getListaSubdocumenti()) {
				checkSubdocumentoSpesa(subdocumentoSpesa);
			}
		}
	}
	
	/* ****************************************************************************************************
	 ******************************************************************************************************
	 ********************************* Normalizzazione delle date *****************************************
	 ******************************************************************************************************
	 *******************************************************************************************************/
	
	/**
	 * Normalizzazione delle date. Le date devono essere della forma yyyy/mm/dd-00:00:00.000 per permettere al front-end di ricercarle 
	 */
	private void normalizzazioneDate() {
		final String methodName = "normalizzazioneDate";
		log.debug(methodName, "Normalizzazione delle date dei varii componenti della request");
		for(ElencoDocumentiAllegato elencoDocumentiAllegato : elenchiDocumentiAllegato.getElenchiDocumentiAllegato()) {
			
			// Elenco
			elencoDocumentiAllegato.setDataTrasmissione(normalizeDate(elencoDocumentiAllegato.getDataTrasmissione()));
			
			if (elencoDocumentiAllegato.getAllegatoAtto() != null && elencoDocumentiAllegato.getAllegatoAtto().getAttoAmministrativo() != null) {
				// Allegato atto
				normalizzazioneDateAllegatoAtto(elencoDocumentiAllegato.getAllegatoAtto());
			}

			for(SubdocumentoSpesa subdocumentoSpesa : elencoDocumentiAllegato.getSubdocumentiSpesa()) {
				DocumentoSpesa documentoSpesa = subdocumentoSpesa.getDocumento();
				// Documento di spesa
				normalizzazioneDateDocumentoSpesa(documentoSpesa);
				
				// Subdocumento di spesa
				normalizzazioneDateSubdocumentoSpesa(subdocumentoSpesa);
			}
			
			for(SubdocumentoIvaSpesa subdocumentoIvaCollegato : elencoDocumentiAllegato.getSubdocumentiIvaSpesa()){
				if (subdocumentoIvaCollegato != null) {
					// Subdocumento iva spesa
					normalizzazioneDateSubdocumentoIvaSpesa(subdocumentoIvaCollegato);
				}
			}

			for(SubdocumentoEntrata subdocumentoEntrata : elencoDocumentiAllegato.getSubdocumentiEntrata()) {
				DocumentoEntrata documentoEntrata = subdocumentoEntrata.getDocumento();
				
				// Documento di entrata
				normalizzazioneDateDocumentoEntrata(documentoEntrata);
				
				// Subdocumento di entrata
				normalizzazioneDateSubdocumentoEntrata(subdocumentoEntrata);
			}
			
			for(SubdocumentoIvaEntrata subdocumentoIvaCollegato : elencoDocumentiAllegato.getSubdocumentiIvaEntrata()){
				if (subdocumentoIvaCollegato != null) {
					// Subdocumento iva entrata
					normalizzazioneDateSubdocumentoIvaEntrata(subdocumentoIvaCollegato);
				}
			}
		}
		log.debug(methodName, "Date normalizzate");
	}

	private void normalizzazioneDateAllegatoAtto(AllegatoAtto allegatoAtto) {
		allegatoAtto.setDataScadenza(normalizeDate(allegatoAtto.getDataScadenza()));
		allegatoAtto.setDataInizioValiditaStato(normalizeDate(allegatoAtto.getDataInizioValiditaStato()));
	}
	
	private void normalizzazioneDateDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		normalizazzioneDateDocumento(documentoSpesa);
		
		// SIAC-5115: le date non sono piu' qui. Le forzo a null
		cleanDateSospensione(documentoSpesa);
		
		// farlo per i documenti di entrata collegati e per le note di credito (documenti spesa)
		if (documentoSpesa.getListaNoteCreditoEntrataFiglio() != null) {
			for (DocumentoEntrata documentoEntrataNota : documentoSpesa.getListaNoteCreditoEntrataFiglio()) {
				normalizzazioneDateDocumentoEntrata(documentoEntrataNota);
			}
		}
		if (documentoSpesa.getListaDocumentiSpesaFiglio() != null) {
			for (DocumentoSpesa documentoSpesaNota : documentoSpesa.getListaDocumentiSpesaFiglio()) {
				normalizzazioneDateDocumentoSpesa(documentoSpesaNota);
			}
		}
		
		//documenti iva collegati
		if (documentoSpesa.getListaSubdocumentoIva() != null) {
			for (SubdocumentoIvaSpesa documentoIVASpesa : documentoSpesa.getListaSubdocumentoIva()) {
				normalizzazioneDateSubdocumentoIvaSpesa(documentoIVASpesa);
			}
		}
		
		if(documentoSpesa.getListaSubdocumenti() != null) {
			for(SubdocumentoSpesa subdocumentoSpesa : documentoSpesa.getListaSubdocumenti()) {
				normalizzazioneDateSubdocumentoSpesa(subdocumentoSpesa);
			}
		}
	}
	
	/**
	 * SIAC-5115: le date di sospensione sono state deprecate
	 * @param documentoSpesa
	 */
	@SuppressWarnings("deprecation")
	private void cleanDateSospensione(DocumentoSpesa documentoSpesa) {
		documentoSpesa.setDataSospensione(null);
		documentoSpesa.setDataRiattivazione(null);
		documentoSpesa.setDataScadenzaDopoSospensione(null);
	}

	private void normalizzazioneDateDocumentoEntrata(DocumentoEntrata documentoEntrata) {
		normalizazzioneDateDocumento(documentoEntrata);
		
		//note credito
		if (documentoEntrata.getListaNoteCreditoEntrataFiglio() != null) {
			for (DocumentoEntrata documentoEntrataNota : documentoEntrata.getListaNoteCreditoEntrataFiglio()) {
				normalizzazioneDateDocumentoEntrata(documentoEntrataNota);
			}
		}
		
		//documenti spesa collegati
		if (documentoEntrata.getListaDocumentiSpesaFiglio() != null) {
			for (DocumentoSpesa documentoSpesa : documentoEntrata.getListaDocumentiSpesaFiglio()) {
				normalizzazioneDateDocumentoSpesa(documentoSpesa);
			}
		}
		
		//documenti iva collegati
		if (documentoEntrata.getListaSubdocumentoIva() != null) {
			for (SubdocumentoIvaEntrata documentoIVAEntrata : documentoEntrata.getListaSubdocumentoIva()) {
				normalizzazioneDateSubdocumentoIvaEntrata(documentoIVAEntrata);
			}
		}
		
		if(documentoEntrata.getListaSubdocumenti() != null) {
			for(SubdocumentoEntrata subdocumentoEntrata : documentoEntrata.getListaSubdocumenti()) {
				normalizzazioneDateSubdocumentoEntrata(subdocumentoEntrata);
			}
		}
	}
	
	private void normalizazzioneDateDocumento(Documento<?, ?> documento) {
		documento.setDataEmissione(normalizeDate(documento.getDataEmissione()));
		documento.setDataScadenza(normalizeDate(documento.getDataScadenza()));
		documento.setDataRepertorio(normalizeDate(documento.getDataRepertorio()));
		documento.setDataRicezionePortale(normalizeDate(documento.getDataRicezionePortale()));
		documento.setDataInizioValiditaStato(normalizeDate(documento.getDataInizioValiditaStato()));
	}
	
	private void normalizzazioneDateSubdocumentoIvaSpesa(SubdocumentoIvaSpesa subdocumentoIvaSpesa) {
		normalizzazioneDateSubdocumentoIva(subdocumentoIvaSpesa);
	}
	
	private void normalizzazioneDateSubdocumentoIvaEntrata(SubdocumentoIvaEntrata subdocumentoIvaEntrata) {
		normalizzazioneDateSubdocumentoIva(subdocumentoIvaEntrata);
	}

	private void normalizzazioneDateSubdocumentoIva(SubdocumentoIva<?, ?, ?> subdocumentoIva) {
		subdocumentoIva.setDataRegistrazione(normalizeDate(subdocumentoIva.getDataRegistrazione()));
		subdocumentoIva.setDataProtocolloDefinitivo(normalizeDate(subdocumentoIva.getDataProtocolloDefinitivo()));
		subdocumentoIva.setDataProtocolloProvvisorio(normalizeDate(subdocumentoIva.getDataProtocolloProvvisorio()));
		subdocumentoIva.setDataOrdinativoDocumento(normalizeDate(subdocumentoIva.getDataOrdinativoDocumento()));
		subdocumentoIva.setDataCassaDocumento(normalizeDate(subdocumentoIva.getDataCassaDocumento()));
	}
	
	private void normalizzazioneDateSubdocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		normalizzazioneDateSubdocumento(subdocumentoSpesa);
		
		/*
		 * documentoSpesa.setDataSospensione(normalizeDate(documentoSpesa.getDataSospensione()));
		documentoSpesa.setDataRiattivazione(normalizeDate(documentoSpesa.getDataRiattivazione()));
		documentoSpesa.setDataScadenzaDopoSospensione(normalizeDate(documentoSpesa.getDataScadenzaDopoSospensione()));
		 */
		
		if(subdocumentoSpesa.getSospensioni() != null) {
			for(SospensioneSubdocumento sospensioneSubdocumento : subdocumentoSpesa.getSospensioni()) {
				normalizzazioneDateSospensioneSubdocumento(sospensioneSubdocumento);
			}
		}
		
		subdocumentoSpesa.setDataEsecuzionePagamento(normalizeDate(subdocumentoSpesa.getDataEsecuzionePagamento()));
		subdocumentoSpesa.setDataScadenzaDopoSospensione(normalizeDate(subdocumentoSpesa.getDataScadenzaDopoSospensione()));
		subdocumentoSpesa.setDataPagamentoCEC(normalizeDate(subdocumentoSpesa.getDataPagamentoCEC()));
	}
	
	private void normalizzazioneDateSospensioneSubdocumento(SospensioneSubdocumento sospensioneSubdocumento) {
		sospensioneSubdocumento.setDataSospensione(normalizeDate(sospensioneSubdocumento.getDataSospensione()));
		sospensioneSubdocumento.setDataRiattivazione(normalizeDate(sospensioneSubdocumento.getDataRiattivazione()));
	}

	private void normalizzazioneDateSubdocumentoEntrata(SubdocumentoEntrata subdocumentoEntrata) {
		normalizzazioneDateSubdocumento(subdocumentoEntrata);
	}
	
	private void normalizzazioneDateSubdocumento(Subdocumento<?, ?> subdocumento) {
		subdocumento.setDataScadenza(normalizeDate(subdocumento.getDataScadenza()));
	}
	//############################################################################################################
	//######## Inizializzazione degli uids necessari per il servizio InserisceElenchiDocumentiService ############
	//############################################################################################################
	

	/**
	 * Popola tutti gli Uid esterni necessari per l'utilizzo dei servizi richiamati per caricare i documenti.
	 */
	private void initEntitiesUids() {
		final String methodName = "initEntitiesUids";
		
		log.debug(methodName, "Inizializzazione di " + elenchiDocumentiAllegato.getElenchiDocumentiAllegato().size() + " elenchi.");
		
		for(ElencoDocumentiAllegato elencoDocumentiAllegato : elenchiDocumentiAllegato.getElenchiDocumentiAllegato()) {
			if(elencoDocumentiAllegato == null) {
				log.debug(methodName, "elencoDocumentiAllegato is null.");
				continue;
			}
			
			initElencoDocumentiAllegatoUids(elencoDocumentiAllegato);
			
			AllegatoAtto allegatoAtto = elencoDocumentiAllegato.getAllegatoAtto();
			if(allegatoAtto!=null) {
				initAllegatoAttoUids(allegatoAtto);
			}
			
			log.debug(methodName, "Inizializzazione di " + elencoDocumentiAllegato.getSubdocumentiSpesa().size() + " subdocumenti spesa.");
			for(SubdocumentoSpesa subdocumentoSpesa : elencoDocumentiAllegato.getSubdocumentiSpesa()) {
				if(subdocumentoSpesa == null) {
					log.debug(methodName, "subdocumentoSpesa is null.");
					continue;
				}
				log.debug(methodName, "Inizializzazione subdocumento spesa.");
				
				DocumentoSpesa documentoSpesa = subdocumentoSpesa.getDocumento();
				
				if(documentoSpesa != null) {
					initDocumentoSpesaUids(documentoSpesa);
					
					for(DocumentoSpesa documentoCollegato : documentoSpesa.getListaDocumentiSpesaFiglio()){
						log.debug(methodName, "documento spesa collegato al documento spesa.");
						initDocumentoSpesaUids(documentoCollegato);
					}
					
					for(DocumentoEntrata documentoCollegato : documentoSpesa.getListaDocumentiEntrataFiglio()){
						log.debug(methodName, "documento entrata collegato al documento spesa.");
						initDocumentoEntrataUids(documentoCollegato);
					}
					
					for(SubdocumentoIvaSpesa subdocumentoIvaCollegato : documentoSpesa.getListaSubdocumentoIva(SubdocumentoIvaSpesa.class)){
						log.debug(methodName, "subdocumento iva spesa collegato al documento spesa.");
						initSubdocumentoIvaSpesaUids(subdocumentoIvaCollegato); //i subdocumentoIva sono collegati o al documento ...
					}
					
					for(SubdocumentoIvaEntrata subdocumentoIvaCollegato : documentoSpesa.getListaSubdocumentoIva(SubdocumentoIvaEntrata.class)){
						log.debug(methodName, "subdocumento iva entrata collegato al documento spesa.");
						initSubdocumentoIvaEntrataUids(subdocumentoIvaCollegato);//i subdocumentoIva sono collegati o al documento ...
					}
				} else {
					log.debug(methodName, "documentoSpesa is null.");
				}

				if(subdocumentoSpesa.getSubdocumentoIva() != null) {
					log.debug(methodName, "subdocumento iva spesa collegato al subdocumento spesa.");
					initSubdocumentoIvaSpesaUids(subdocumentoSpesa.getSubdocumentoIva());//...o al subdocumento in modo exclusivo
				}
				
				initSubdocumentoSpesaUids(subdocumentoSpesa);
				
			}
			
			log.debug(methodName, "Inizializzazione di " + elencoDocumentiAllegato.getSubdocumentiSpesa().size() + " subdocumenti entrata.");
			for(SubdocumentoEntrata subdocumentoEntrata : elencoDocumentiAllegato.getSubdocumentiEntrata()) {
				if(subdocumentoEntrata == null) {
					log.debug(methodName, "subdocumentoEntrata is null.");
					continue;
				}
				log.debug(methodName, "Inizializzazione subdocumento entrata.");
				
				DocumentoEntrata documentoEntrata = subdocumentoEntrata.getDocumento();
				
				if(documentoEntrata != null) {
					initDocumentoEntrataUids(documentoEntrata);
					
					for(DocumentoSpesa documentoCollegato : documentoEntrata.getListaDocumentiSpesaFiglio()){
						log.debug(methodName, "documento spesa collegato al documento entrata.");
						initDocumentoSpesaUids(documentoCollegato);
					}
					
					for(DocumentoEntrata documentoCollegato : documentoEntrata.getListaDocumentiEntrataFiglio()){
						log.debug(methodName, "documento entrata collegato al documento entrata.");
						initDocumentoEntrataUids(documentoCollegato);
					}
					
					for(SubdocumentoIvaSpesa subdocumentoIvaCollegato : documentoEntrata.getListaSubdocumentoIva(SubdocumentoIvaSpesa.class)){
						log.debug(methodName, "subdocumento iva spesa collegato al documento entrata.");
						initSubdocumentoIvaSpesaUids(subdocumentoIvaCollegato);//i subdocumentoIva sono collegati o al documento ...
					}
					
					for(SubdocumentoIvaEntrata subdocumentoIvaCollegato : documentoEntrata.getListaSubdocumentoIva(SubdocumentoIvaEntrata.class)){
						log.debug(methodName, "subdocumento iva entrata collegato al documento entrata.");
						initSubdocumentoIvaEntrataUids(subdocumentoIvaCollegato); //i subdocumentoIva sono collegati o al documento ...
					}
				} else {
					log.debug(methodName, "documentoEntrata is null.");
				}
				
				if(subdocumentoEntrata.getSubdocumentoIva() != null) {
					log.debug(methodName, "subdocumento iva entrata collegato al subdocumento entrata.");
					initSubdocumentoIvaEntrataUids(subdocumentoEntrata.getSubdocumentoIva());//...o al subdocumento in modo exclusivo
				}
				initSubdocumentoEntrataUids(subdocumentoEntrata);
				
			}
			
			
			log.debug(methodName, "Inizializzazione di " + elencoDocumentiAllegato.getSubdocumentiIvaSpesa().size() + " subdocumenti iva spesa.");
			for(SubdocumentoIvaSpesa subdocumentoIvaSpesa : elencoDocumentiAllegato.getSubdocumentiIvaSpesa()) {
				if(subdocumentoIvaSpesa == null) {
					log.debug(methodName, "subdocumentoSpesa is null.");
					continue;
				}
				log.debug(methodName, "Inizializzazione subdocumento iva Spesa.");
				initSubdocumentoIvaSpesaUids(subdocumentoIvaSpesa);
				
				DocumentoSpesa documentoSpesa = subdocumentoIvaSpesa.getDocumento();
				
				if(documentoSpesa != null) {
					initDocumentoSpesaUids(documentoSpesa);
				} else {
					log.debug(methodName, "documentoSpesa is null.");
				}
				
			}
			
			
			log.debug(methodName, "Inizializzazione di " + elencoDocumentiAllegato.getSubdocumentiIvaEntrata().size() + " subdocumenti iva entrata.");
			for(SubdocumentoIvaEntrata subdocumentoIvaEntrata : elencoDocumentiAllegato.getSubdocumentiIvaEntrata()) {
				if(subdocumentoIvaEntrata == null) {
					log.debug(methodName, "subdocumentoEntrata is null.");
					continue;
				}
				log.debug(methodName, "Inizializzazione subdocumento iva entrata.");
				initSubdocumentoIvaEntrataUids(subdocumentoIvaEntrata);
				
				DocumentoEntrata documentoEntrata = subdocumentoIvaEntrata.getDocumento();
				
				if(documentoEntrata != null) {
					initDocumentoEntrataUids(documentoEntrata);
				} else {
					log.debug(methodName, "documentoEntrata is null.");
				}
				
			}
		}
	}

	private void initElencoDocumentiAllegatoUids(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		final String methodName = "initElencoDocumentiAllegatoUids";
		log.debugInizializzazionePerEntita(methodName, elencoDocumentiAllegato);
		
		elencoDocumentiAllegato.setEnte(ente);
		elencoDocumentiAllegato.setAnno(elencoDocumentiAllegato.getAnnoSysEsterno());
		//elencoDocumentiAllegato.setNumero(new Integer(elencoDocumentiAllegato.getNumeroSysEsterno()));//il numero viene staccato in automatico
	}

	private void initAllegatoAttoUids(AllegatoAtto allegatoAtto) {
		final String methodName = "initAllegatoAttoUids";
		log.debugInizializzazionePerEntita(methodName, allegatoAtto);
		
		allegatoAtto.setEnte(ente);
		
		AttoAmministrativo attoAmministrativo = allegatoAtto.getAttoAmministrativo();
		
		if(attoAmministrativo != null) {
			log.debugInizializzazionePerEntita(methodName, attoAmministrativo, " collegato all'allegato atto");
			initAttoAmministrativoUids(attoAmministrativo);
			
			AttoAmministrativo attoAmministrativoTrovato = provvedimentoServiceCallGroup.ricercaProvvedimentoSingoloCached(attoAmministrativo);
			//allegatoAtto.getAttoAmministrativo().setUid(attoAmministrativo.getUid());
			if(attoAmministrativoTrovato!=null) {
				allegatoAtto.setAttoAmministrativo(attoAmministrativoTrovato);
			}
			
			log.debugUidEntita(methodName, "attoAmministrativo", allegatoAtto.getAttoAmministrativo());
		}
		
		//forzato a false
		allegatoAtto.setDatiSensibili(Boolean.FALSE);
		
	}

	private void initAttoAmministrativoUids(AttoAmministrativo attoAmministrativo) {
		final String methodName = "initAttoAmministrativoUids";
		attoAmministrativo.setEnte(ente);
		
		if(attoAmministrativo.getTipoAtto()!=null) {
			TipoAtto tipoAttoTrovato = findTipoAtto(attoAmministrativo.getTipoAtto().getCodice());
			attoAmministrativo.setTipoAtto(tipoAttoTrovato);
			log.debugUidEntita(methodName, "tipoAtto", tipoAttoTrovato);
		}
		
		if(attoAmministrativo.getStrutturaAmmContabile()!=null 
				&& StringUtils.isNotBlank(attoAmministrativo.getStrutturaAmmContabile().getCodice())){
			StrutturaAmministrativoContabile sacTrovato = findStrutturaAmmContabile(attoAmministrativo.getAnno(), attoAmministrativo.getStrutturaAmmContabile().getCodice());
			attoAmministrativo.setStrutturaAmmContabile(sacTrovato);
			log.debugUidEntita(methodName, "strutturaAmministrativoContabile", sacTrovato);
		}
		
	}

	private void initSubdocumentoSpesaUids(SubdocumentoSpesa subdocumentoSpesa) {
		final String methodName = "initSubdocumentoSpesaUids";
		log.debugInizializzazionePerEntita(methodName, subdocumentoSpesa);
		
		initSubdocumentoUids(subdocumentoSpesa);
		//uid impegno, modalitaPagamento e sede secondaria solo se valorizzati
		if(subdocumentoSpesa.getImpegno() != null
				&& subdocumentoSpesa.getImpegno().getAnnoMovimento() != 0
				&& subdocumentoSpesa.getImpegno().getNumero() != null){
			log.debug(methodName, "Caricamento dell'uid dell'impegno");
			//servizio ottimizzato
			initImpegnoSubImpegnoUids(subdocumentoSpesa);
			
		}
		
		if(subdocumentoSpesa.getDocumento().getSoggetto() != null 
				&& subdocumentoSpesa.getDocumento().getSoggetto().getCodiceSoggetto()!=null 
				&& subdocumentoSpesa.getModalitaPagamentoSoggetto()!=null
				&& StringUtils.isNotBlank(subdocumentoSpesa.getModalitaPagamentoSoggetto().getCodiceModalitaPagamento())) {
			log.debug(methodName, "Caricamento dell'uid della modalita di pagamento del soggetto");
			initModalitaPagamentoSoggettoUids(subdocumentoSpesa);
		}
		
		if(subdocumentoSpesa.getDocumento().getSoggetto() != null 
				&& subdocumentoSpesa.getDocumento().getSoggetto().getCodiceSoggetto()!=null 
				&& subdocumentoSpesa.getSedeSecondariaSoggetto()!=null
				&& StringUtils.isNotBlank(subdocumentoSpesa.getSedeSecondariaSoggetto().getDenominazione())) {
			log.debug(methodName, "Caricamento dell'uid della sede secondaria del soggetto");
			initSedeSecondariaSoggettoUids(subdocumentoSpesa);
		}
		
		// SIAC-5311: inizializzazione assenza motivazione
		if(subdocumentoSpesa.getSiopeAssenzaMotivazione() != null && StringUtils.isNotBlank(subdocumentoSpesa.getSiopeAssenzaMotivazione().getCodice())) {
			log.debug(methodName, "Caricamento dell'uid del motivo di assenza CIG");
			initSiopeAssenzaMotivazioneUids(subdocumentoSpesa);
		}
	}
	/**
	 * effettua la ricerca dell'impegno o subimpegno ottimizzata
	 * 1- ricerca l'impegno 
	 * 2 se ho il subimpegno ricerca l'impegno con eventuale sub
	 * @param subdocumentoSpesa
	 */
	private void initImpegnoSubImpegnoUids(SubdocumentoSpesa subdocumentoSpesa){
		final String methodName = "initImpegnoSubImpegnoUids";
		RicercaAttributiMovimentoGestioneOttimizzato attributiMovimentoGestioneOttimizzato = new RicercaAttributiMovimentoGestioneOttimizzato();
		boolean isSubDaRicercare = subdocumentoSpesa.getSubImpegno() != null && subdocumentoSpesa.getSubImpegno().getNumero() != null;
		
		attributiMovimentoGestioneOttimizzato.setCaricaSub(isSubDaRicercare);
		attributiMovimentoGestioneOttimizzato.setEscludiSubAnnullati(isSubDaRicercare);
		attributiMovimentoGestioneOttimizzato.setSubPaginati(true);

		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneServiceCallGroup.ricercaImpegnoPerChiaveOttimizzatoCached(subdocumentoSpesa.getImpegno(),attributiMovimentoGestioneOttimizzato, new DatiOpzionaliCapitoli(), subdocumentoSpesa.getSubImpegno());
		Impegno impegnoTrovato = response.getImpegno();
		if(impegnoTrovato==null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Impegno",
				"con chiave " + subdocumentoSpesa.getImpegno().getAnnoMovimento() + "/" + subdocumentoSpesa.getImpegno().getNumero()));
		}
		//impegno trovato 
		subdocumentoSpesa.setImpegno(impegnoTrovato);
		log.debugUidEntita(methodName, "impegno", impegnoTrovato);
		
		if (isSubDaRicercare) {
			if (impegnoTrovato.getElencoSubImpegni() == null || impegnoTrovato.getElencoSubImpegni().isEmpty() ) {
				throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("SubImpegno", "con chiave " + subdocumentoSpesa.getImpegno().getAnnoMovimento() + "/"
						+ subdocumentoSpesa.getImpegno().getNumero() + "-" + subdocumentoSpesa.getSubImpegno().getNumero()));
			}
			subdocumentoSpesa.setSubImpegno(impegnoTrovato.getElencoSubImpegni().get(0));
			log.debugUidEntita(methodName, "subImpegno", impegnoTrovato.getElencoSubImpegni().get(0));
		}

	}
	
	private void initSedeSecondariaSoggettoUids(SubdocumentoSpesa subdocumentoSpesa) {
		String methodName = "initSedeSecondariaSoggettoUids";
		
		//FIXME workaround per il servizio ricercaSedeSecondariaPerChiave -> uso ricercaSoggettoPerChiaveCached!
		
//		RicercaSedeSecondariaPerChiaveResponse resRSSPC = soggettoServiceCallGroup.ricercaSedeSecondariaPerChiaveCached(subdocumentoSpesa.getDocumento().getSoggetto().getCodiceSoggetto(), subdocumentoSpesa.getSedeSecondariaSoggetto().getCodiceSedeSecondaria());
//		SedeSecondariaSoggetto sedeSecondariaSoggettoTrovata = resRSSPC.getSedeSecondariaSoggetto();
//		
//		if(sedeSecondariaSoggettoTrovata==null){
//			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Sede secondaria soggetto",
//				"con codice " + subdocumentoSpesa.getSedeSecondariaSoggetto().getCodiceSedeSecondaria() + " relativa al soggetto " + subdocumentoSpesa.getDocumento().getSoggetto().getCodiceSoggetto()));
//		}
//		subdocumentoSpesa.setSedeSecondariaSoggetto(sedeSecondariaSoggettoTrovata);
		
		RicercaSoggettoPerChiaveResponse resRSPC = soggettoServiceCallGroup.ricercaSoggettoPerChiaveCached(subdocumentoSpesa.getDocumento().getSoggetto().getCodiceSoggetto());
		List<SedeSecondariaSoggetto> listaSediTrovate = resRSPC.getListaSecondariaSoggetto();
		if(listaSediTrovate == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Sede secondaria soggetto",
				"con codice " + subdocumentoSpesa.getSedeSecondariaSoggetto().getCodiceSedeSecondaria() + " relativa al soggetto " + subdocumentoSpesa.getDocumento().getSoggetto().getCodiceSoggetto()));
		}
		
		for(SedeSecondariaSoggetto sedeSecondariaSoggettoTrovata : listaSediTrovate) {
			if(subdocumentoSpesa.getSedeSecondariaSoggetto().getDenominazione().equalsIgnoreCase(sedeSecondariaSoggettoTrovata.getDenominazione())) {
				subdocumentoSpesa.setSedeSecondariaSoggetto(sedeSecondariaSoggettoTrovata);
				log.debugUidEntita(methodName, "sedeSecondariaSoggetto", sedeSecondariaSoggettoTrovata);
				return;
			}
		}
		
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Sede secondaria soggetto",
				"con denominazinoe " + subdocumentoSpesa.getSedeSecondariaSoggetto().getDenominazione() + " relativa al soggetto " + subdocumentoSpesa.getDocumento().getSoggetto().getCodiceSoggetto()));
	}
	
	private void initSiopeAssenzaMotivazioneUids(SubdocumentoSpesa subdocumentoSpesa) {
		final String methodName = "initSiopeAssenzaMotivazioneUids";
		
		SiopeAssenzaMotivazione siopeAssenzaMotivazioneTrovata = codificaDad.ricercaCodifica(SiopeAssenzaMotivazione.class, subdocumentoSpesa.getSiopeAssenzaMotivazione().getCodice());
		if(siopeAssenzaMotivazioneTrovata == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Motivo di assenza CIG",
					"con codice " + subdocumentoSpesa.getSiopeAssenzaMotivazione().getCodice()));
		}
		subdocumentoSpesa.setSiopeAssenzaMotivazione(siopeAssenzaMotivazioneTrovata);
		log.debugUidEntita(methodName, "siopeAssenzaMotivazione", siopeAssenzaMotivazioneTrovata);
	}

	private void initModalitaPagamentoSoggettoUids(SubdocumentoSpesa subdocumentoSpesa) {
		String methodName = "initModalitaPagamentoSoggettoUids";
		RicercaModalitaPagamentoPerChiaveResponse resRMPC = soggettoServiceCallGroup.ricercaModalitaPagamentoPerChiaveCached(subdocumentoSpesa.getDocumento().getSoggetto().getCodiceSoggetto(), subdocumentoSpesa.getModalitaPagamentoSoggetto().getCodiceModalitaPagamento());
		ModalitaPagamentoSoggetto modalitaPagamentoSoggettoTrovata = resRMPC.getModalitaPagamentoSoggetto();
		
		if(modalitaPagamentoSoggettoTrovata==null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Modalita' di pagamento soggetto",
				"con codice " + subdocumentoSpesa.getModalitaPagamentoSoggetto().getCodiceModalitaPagamento() + " relativa al soggetto " + subdocumentoSpesa.getDocumento().getSoggetto().getCodiceSoggetto()));
		}
		
		subdocumentoSpesa.setModalitaPagamentoSoggetto(modalitaPagamentoSoggettoTrovata);
		log.debugUidEntita(methodName, "modalitaPagamentoSoggetto", modalitaPagamentoSoggettoTrovata);
	}


	private void initSubdocumentoEntrataUids(SubdocumentoEntrata subdocumentoEntrata) {
		final String methodName = "initSubdocumentoEntrataUids";
		log.debugInizializzazionePerEntita(methodName, subdocumentoEntrata);
		
		initSubdocumentoUids(subdocumentoEntrata);
		
		if(subdocumentoEntrata.getAccertamento() != null
				&& subdocumentoEntrata.getAccertamento().getAnnoMovimento() != 0
				&& subdocumentoEntrata.getAccertamento().getNumero() != null){
			initAccertamentoSubAccertamentoUids(subdocumentoEntrata);
		}
		
		if(subdocumentoEntrata.getContoTesoreria()!=null && StringUtils.isNotBlank(subdocumentoEntrata.getContoTesoreria().getCodice())) {
			log.debug(methodName, "Caricamento dell'uid del conto tesoreria");
			ContoTesoreria ct = findContoTesoreria(subdocumentoEntrata.getContoTesoreria().getCodice());
			subdocumentoEntrata.setContoTesoreria(ct);
		}
		
		if(subdocumentoEntrata.getDistinta()!=null && StringUtils.isNotBlank(subdocumentoEntrata.getDistinta().getCodice())) {
			log.debug(methodName, "Caricamento dell'uid della distinta");
			Distinta dist = findDistinta(subdocumentoEntrata.getDistinta().getCodice());
			subdocumentoEntrata.setDistinta(dist);
		}
		
		//uid accertamento, atto solo se valorizzati
		
	}

	/**
	 * effettua la ricerca dell'accertamento o subaccertamento ottimizzata
	 * 1- ricerca l'impegno 
	 * 2 se ho il subaccertamento ricerco l'accertamento con eventuale sub
	 * @param subdocumentoSpesa
	 */
	private void initAccertamentoSubAccertamentoUids(SubdocumentoEntrata subdocumentoEntrata){
		final String methodName = "initAccertamentoSubAccertamentoUids";
		RicercaAttributiMovimentoGestioneOttimizzato attributiMovimentoGestioneOttimizzato = new RicercaAttributiMovimentoGestioneOttimizzato();
		boolean isSubDaRicercare = subdocumentoEntrata.getSubAccertamento() != null && subdocumentoEntrata.getSubAccertamento().getNumero() != null;
		
		attributiMovimentoGestioneOttimizzato.setCaricaSub(isSubDaRicercare);
		attributiMovimentoGestioneOttimizzato.setEscludiSubAnnullati(isSubDaRicercare);
		attributiMovimentoGestioneOttimizzato.setSubPaginati(true);

		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneServiceCallGroup.ricercaAccertamentoPerChiaveOttimizzatoCached(subdocumentoEntrata.getAccertamento(),attributiMovimentoGestioneOttimizzato, new DatiOpzionaliCapitoli(), subdocumentoEntrata.getSubAccertamento());
		Accertamento accertamentoTrovato = response.getAccertamento();
		if(accertamentoTrovato==null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Accertamento",
				"con chiave " + subdocumentoEntrata.getAccertamento().getAnnoMovimento() + "/" + subdocumentoEntrata.getAccertamento().getNumero()));
		}
		subdocumentoEntrata.setAccertamento(accertamentoTrovato);
		log.debugUidEntita(methodName, "accertamento", accertamentoTrovato);
		
		if (isSubDaRicercare) {
			if (accertamentoTrovato.getElencoSubAccertamenti()== null || accertamentoTrovato.getElencoSubAccertamenti().isEmpty()) {
				throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("SubAccertamento",
						"con chiave " + subdocumentoEntrata.getAccertamento().getAnnoMovimento() + "/" + subdocumentoEntrata.getAccertamento().getNumero()
						+ "-" + subdocumentoEntrata.getSubAccertamento().getNumero()));
			}
			subdocumentoEntrata.setSubAccertamento(accertamentoTrovato.getElencoSubAccertamenti().get(0));
			log.debugUidEntita(methodName, "subAccertamento", accertamentoTrovato.getElencoSubAccertamenti().get(0));

		}

	}

	private void initSubdocumentoUids(Subdocumento<?,?> subdocumento) {
		final String methodName = "initSubdocumentoUids";
		subdocumento.setEnte(ente);
		
		if(subdocumento.getAttoAmministrativo()!=null){
			AttoAmministrativo attoAmministrativoTrovato = provvedimentoServiceCallGroup.ricercaProvvedimentoSingoloCached(subdocumento.getAttoAmministrativo());
			if(attoAmministrativoTrovato!=null){
				subdocumento.setAttoAmministrativo(attoAmministrativoTrovato);
			}
			log.debugUidEntita(methodName, "attoAmministrativo", attoAmministrativoTrovato);
		}
		
		if(subdocumento.getNoteTesoriere()!=null && StringUtils.isNotBlank(subdocumento.getNoteTesoriere().getCodice())){
			NoteTesoriere noteTesoriereTrovato = findNoteTesoriere(subdocumento.getNoteTesoriere().getCodice());
			subdocumento.setNoteTesoriere(noteTesoriereTrovato);
			log.debugUidEntita(methodName, "noteTesoriere", noteTesoriereTrovato);
		}
		
		if(subdocumento.getTipoAvviso()!=null && StringUtils.isNotBlank(subdocumento.getTipoAvviso().getCodice())){
			ClassificatoreGenerico tipoAvvisoTrovato = findTipoAvviso(subdocumento.getDocumento().getAnno(), subdocumento.getTipoAvviso().getCodice());
			subdocumento.getTipoAvviso().setUid(tipoAvvisoTrovato.getUid());
			log.debugUidEntita(methodName, "tipoAvviso", tipoAvvisoTrovato);
		}
		
	}

	private void initSubdocumentoIvaEntrataUids(SubdocumentoIvaEntrata subdocIva) {
		final String methodName = "initSubdocumentoIvaEntrataUids";
		log.debugInizializzazionePerEntita(methodName, subdocIva);
		
		if(subdocIva==null){
			//e' facoltativo
			return;
		}
		initSubdocumentoIvaUids(subdocIva);

	}

	private void initSubdocumentoIvaSpesaUids(SubdocumentoIvaSpesa subdocIva) {
		final String methodName = "initSubdocumentoIvaSpesaUids";
		log.debugInizializzazionePerEntita(methodName, subdocIva);
		if(subdocIva==null){
			//e' facoltativo
			return;
		}
		initSubdocumentoIvaUids(subdocIva);
	}

	private void initSubdocumentoIvaUids(SubdocumentoIva<?,?,?> subdocIva) {
		if(subdocIva==null){
			//e' facoltativo
			return;
		}
		final String methodName = "initSubdocumentoIvaUids";
		subdocIva.setEnte(ente);
		
		if(subdocIva.getRegistroIva() != null && StringUtils.isNotBlank(subdocIva.getRegistroIva().getCodice())) {
			RegistroIva registroIvaTrovato = findRegistroIva(subdocIva.getRegistroIva());
			subdocIva.setRegistroIva(registroIvaTrovato);
			log.debugUidEntita(methodName, "registroIva", registroIvaTrovato);
		}
		
		if(subdocIva.getTipoRegistrazioneIva() != null && StringUtils.isNotBlank(subdocIva.getTipoRegistrazioneIva().getCodice())) {
			TipoRegistrazioneIva tipoRegistrazioneIvaTrovato = findTipoRegistrazioneIva(subdocIva.getTipoRegistrazioneIva());
			subdocIva.setTipoRegistrazioneIva(tipoRegistrazioneIvaTrovato);
			log.debugUidEntita(methodName, "tipoRegistrazioneIva", tipoRegistrazioneIvaTrovato);
		}
		
		if(subdocIva.getValuta()!=null && StringUtils.isNotBlank(subdocIva.getTipoRegistrazioneIva().getCodice())) {
			Valuta valutaTrovata = findValuta(subdocIva.getValuta().getCodice());
			subdocIva.setValuta(valutaTrovata);
			log.debugUidEntita(methodName, "valutaIva", valutaTrovata);
		}
		
		for(AliquotaSubdocumentoIva aliquota : subdocIva.getListaAliquotaSubdocumentoIva()) {
			if(aliquota.getAliquotaIva()!=null && StringUtils.isNotBlank(aliquota.getAliquotaIva().getCodice())) {
				AliquotaIva aliquotaIvaTrovata = findAliquotaIva(aliquota.getAliquotaIva().getCodice());
				aliquota.setAliquotaIva(aliquotaIvaTrovata);
				log.debugUidEntita(methodName, "aliquotaIva", aliquotaIvaTrovata);
				
			}
		}
		
		if(subdocIva.getAttivitaIva()!=null && StringUtils.isNotBlank(subdocIva.getAttivitaIva().getCodice())) {
			AttivitaIva attivitaIvaTrovata = findAttivitaIva(subdocIva.getAttivitaIva().getCodice());
			subdocIva.setAttivitaIva(attivitaIvaTrovata);
			log.debugUidEntita(methodName, "attivitaIva", attivitaIvaTrovata);
		}
		
		
	}

	private void initDocumentoSpesaUids(DocumentoSpesa documento) {
		final String methodName = "initDocumentoSpesaUids";
		log.debugInizializzazionePerEntita(methodName, documento);
		
		initDocumentoUids(documento);
		
		if(documento.getTipoImpresa()!=null){
			log.debug(methodName, "Caricamento dell'uid del tipo impresa");
			ClassificatoreGenerico tipoImpresaTrovato = findTipoImpresa(documento.getAnno(), documento.getTipoImpresa().getCodice());
			documento.getTipoImpresa().setUid(tipoImpresaTrovato.getUid());
		}
		
		if(documento.getRitenuteDocumento() != null) {
			initRitenuteDocumentoUids(documento.getRitenuteDocumento());
		}
	}

	private void initDocumentoEntrataUids(DocumentoEntrata documento) {
		final String methodName = "initDocumentoEntrataUids";
		log.debugInizializzazionePerEntita(methodName, documento);
		initDocumentoUids(documento);
	}
	
	private void initDocumentoUids(@SuppressWarnings("rawtypes") Documento documento) {
		final String methodName = "initDocumentoUids";
		documento.setEnte(ente);
		
		if(documento.getTipoDocumento()!=null && documento.getTipoDocumento().getTipoFamigliaDocumento()!=null) {
			TipoDocumento tipoDocumentoTrovato = findTipoDocumento(documento.getTipoDocumento().getTipoFamigliaDocumento() ,documento.getTipoDocumento().getCodice());
			documento.getTipoDocumento().setUid(tipoDocumentoTrovato.getUid());
			
			log.debug(methodName, "uid tipo documento "+ tipoDocumentoTrovato.getUid());
		}
		
		if(documento.getCodiceBollo()!=null && documento.getCodiceBollo().getCodice()!=null) {
			//obbligatorio solo per spesa!
			CodiceBollo codiceBolloTrovato = findCodiceBollo(documento.getCodiceBollo().getCodice());
			documento.getCodiceBollo().setUid(codiceBolloTrovato.getUid());
			
			log.debug(methodName, "uid codice bollo " + codiceBolloTrovato.getUid());
		}
		
		if(documento.getSoggetto()!=null && documento.getSoggetto().getCodiceSoggetto()!=null) {
			RicercaSoggettoPerChiaveResponse resRSPC = soggettoServiceCallGroup.ricercaSoggettoPerChiaveCached(documento.getSoggetto().getCodiceSoggetto());
			Soggetto soggettoTrovato = resRSPC.getSoggetto();
			if(soggettoTrovato==null){
				throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Soggetto", "codice " + documento.getSoggetto().getCodiceSoggetto()));
			}
			documento.getSoggetto().setUid(soggettoTrovato.getUid());
			log.debug(methodName, "uid soggetto: "+ soggettoTrovato.getUid());
		}
		
	}
	
	private void initRitenuteDocumentoUids(RitenuteDocumento ritenuteDocumento) {
		final String methodName = "initRitenuteDocumentoUids";
		if(ritenuteDocumento.getListaOnere() != null) {
			for(DettaglioOnere dettaglioOnere : ritenuteDocumento.getListaOnere()) {
				// Natura onere
				if(dettaglioOnere.getTipoOnere() != null && dettaglioOnere.getTipoOnere().getNaturaOnere() != null && dettaglioOnere.getTipoOnere().getNaturaOnere().getCodice() != null) {
					NaturaOnere naturaOnereTrovata = findNaturaOnere(dettaglioOnere.getTipoOnere().getNaturaOnere().getCodice());
					dettaglioOnere.getTipoOnere().getNaturaOnere().setUid(naturaOnereTrovata.getUid());
					log.debug(methodName, "uid natura onere: "+naturaOnereTrovata.getUid());
				}
				
				// tipo onere
				if(dettaglioOnere.getTipoOnere() != null && dettaglioOnere.getTipoOnere().getCodice() != null) {
					TipoOnere tipoOnereTrovato = findTipoOnere(dettaglioOnere.getTipoOnere().getNaturaOnere(), dettaglioOnere.getTipoOnere().getCodice());
					dettaglioOnere.getTipoOnere().setUid(tipoOnereTrovato.getUid());
					
					log.debug(methodName, "uid tipo onere: "+tipoOnereTrovato.getUid());
				}
				
				// Attivita onere
				if(dettaglioOnere.getAttivitaOnere()!=null && dettaglioOnere.getAttivitaOnere().getCodice()!=null) {
					AttivitaOnere attivitaOnereTrovato = findAttivitaOnere(dettaglioOnere.getAttivitaOnere().getCodice());
					dettaglioOnere.getAttivitaOnere().setUid(attivitaOnereTrovato.getUid());
					log.debug(methodName, "uid attivita onere: "+attivitaOnereTrovato.getUid());
				}
				
				// Causale 770
				if(dettaglioOnere.getCausale770() != null && dettaglioOnere.getCausale770().getCodice() != null) {
					Causale770 causale770Trovata = findCausale770(dettaglioOnere.getCausale770().getCodice());
					dettaglioOnere.getCausale770().setUid(causale770Trovata.getUid());
					log.debug(methodName, "uid causale 770: "+causale770Trovata.getUid());
				}
			}
		}
	}
	
	private TipoDocumento findTipoDocumento(TipoFamigliaDocumento tipoFamigliaDocumento, String codiceTipoDocumento) {
		RicercaTipoDocumentoResponse resRTD = documentoServiceCallGroup.ricercaTipiDocumentoCached(tipoFamigliaDocumento);
		for(TipoDocumento tipoDocumento :resRTD.getElencoTipiDocumento()){
			if(tipoDocumento.getCodice().equalsIgnoreCase(codiceTipoDocumento)){
				return tipoDocumento;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Tipo documento", "con codice " + codiceTipoDocumento + " della famiglia di documenti " + tipoFamigliaDocumento));
	}
	
	private CodiceBollo findCodiceBollo(String codiceBollo) {
		RicercaCodiceBolloResponse resRCB = documentoServiceCallGroup.ricercaCodiceBolloCached();
		for(CodiceBollo cb :resRCB.getElencoCodiciBollo()){
			if(cb.getCodice().equalsIgnoreCase(codiceBollo)){
				return cb;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Codice bollo", "con codice " + codiceBollo));
	}
	
	private TipoOnere findTipoOnere(NaturaOnere naturaOnere, String codiceTipoOnere) {
		RicercaTipoOnereResponse resRAO = documentoServiceCallGroup.ricercaTipoOnereCached(naturaOnere);
		for(TipoOnere to : resRAO.getElencoTipiOnere()) {
			if(to.getCodice().equalsIgnoreCase(codiceTipoOnere)) {
				return to;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Tipo onere", "con codice " + codiceTipoOnere));
	}
	
	private AttivitaOnere findAttivitaOnere(String codiceAttivitaOnere) {
		RicercaAttivitaOnereResponse resRAO = documentoServiceCallGroup.ricercaAttivitaOnereCached(null);
		for(AttivitaOnere ao : resRAO.getElencoAttivitaOnere()) {
			if(ao.getCodice().equalsIgnoreCase(codiceAttivitaOnere)) {
				return ao;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Attivita onere", "con codice " + codiceAttivitaOnere + " per il tipo onere "));
	}
	
	private NaturaOnere findNaturaOnere(String codiceNaturaOnere) {
		RicercaNaturaOnereResponse resRNO = documentoServiceCallGroup.ricercaNaturaOnereCached();
		for(NaturaOnere no : resRNO.getElencoNatureOnere()) {
			if(no.getCodice().equalsIgnoreCase(codiceNaturaOnere)) {
				return no;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Natura onere", "con codice " + codiceNaturaOnere));
	}
	
	private Causale770 findCausale770(String codiceCausale770) {
		RicercaCausale770Response resRNO = documentoServiceCallGroup.ricercaCausale770Cached(null);
		for(Causale770 c770 : resRNO.getElencoCausali()) {
			if(c770.getCodice().equalsIgnoreCase(codiceCausale770)) {
				return c770;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Causale 770", "con codice " + codiceCausale770));
	}
	
	private ClassificatoreGenerico findTipoImpresa(Integer anno, String codiceTipoImpresa) {
		RicercaTipoImpresaResponse resRCB = documentoServiceCallGroup.ricercaTipoImpresaCached(anno);
		for(ClassificatoreGenerico ti :resRCB.getElencoTipiImpresa()){
			if(ti.getCodice().equalsIgnoreCase(codiceTipoImpresa)) {
				return ti;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Tipo impresa", "con chiave " + anno + "/" + codiceTipoImpresa));
	}
	
	
	
	private NoteTesoriere findNoteTesoriere(String codiceNoteTesoriere) {
		RicercaNoteTesoriereResponse resRNT = documentoServiceCallGroup.ricercaNoteTesoriereCached();
		for(NoteTesoriere nt :resRNT.getElencoNoteTesoriere()){
			if(nt.getCodice().equalsIgnoreCase(codiceNoteTesoriere)) {
				return nt;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Note tesoriere", "con codice " + codiceNoteTesoriere));
	}
	
	private ClassificatoreGenerico findTipoAvviso(Integer anno, String codiceTipoAvviso) {
		RicercaTipoAvvisoResponse resTA = documentoServiceCallGroup.ricercaTipoAvvisoCached(anno);
		for(ClassificatoreGenerico ta :resTA.getElencoTipiAvviso()){
			if(ta.getCodice().equalsIgnoreCase(codiceTipoAvviso)) {
				return ta;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Tipo avviso", "con codice " + codiceTipoAvviso));
	}
	
	private RegistroIva findRegistroIva(RegistroIva registroIva) {
		RicercaRegistroIvaResponse resTA = documentoServiceCallGroup.ricercaRegistroIvaCached(registroIva);
		for(RegistroIva ri :resTA.getListaRegistroIva()){
			if(ri.getCodice().equalsIgnoreCase(registroIva.getCodice())
					&& ri.getTipoRegistroIva().equals(registroIva.getTipoRegistroIva())) {
				return ri;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Registro Iva", "con codice " + registroIva.getCodice()));
	}
	
	

	private TipoRegistrazioneIva findTipoRegistrazioneIva(TipoRegistrazioneIva tipoRegistrazioneIva) {
		RicercaTipoRegistrazioneIvaResponse resTA = documentoServiceCallGroup.ricercaTipoRegistrazioneIvaCached(tipoRegistrazioneIva);
		for(TipoRegistrazioneIva ri :resTA.getListaTipoRegistrazioneIva()){
			if(ri.getCodice().equalsIgnoreCase(tipoRegistrazioneIva.getCodice())) {
				return ri;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Tipo registrazione iva", "con codice " + tipoRegistrazioneIva.getCodice()));
	}
	
	private Valuta findValuta(String codiceValuta) {
		RicercaValutaResponse resTA = documentoServiceCallGroup.ricercaValutaCached();
		for(Valuta ri :resTA.getListaValuta()){
			if(ri.getCodice().equalsIgnoreCase(codiceValuta)) {
				return ri;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Valuta", "con codice " + codiceValuta));
	}
	
	private AttivitaIva findAttivitaIva(String codiceAttivitaiva) {
		AttivitaIva attivitaIva = new AttivitaIva();
		attivitaIva.setCodice(codiceAttivitaiva);
		RicercaAttivitaIvaResponse resRAI = documentoServiceCallGroup.ricercaAttivitaIvaCached(attivitaIva);
		for(AttivitaIva ai :resRAI.getListaAttivitaIva()){
			if(ai.getCodice().equalsIgnoreCase(codiceAttivitaiva)) {
				return ai;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("AttivitaIva", "con codice " + codiceAttivitaiva));
	}
	
	private AliquotaIva findAliquotaIva(String codiceAliquotaIva) {
		RicercaAliquotaIvaResponse resRAI = documentoServiceCallGroup.ricercaAliquotaIvaCached();
		for(AliquotaIva ai : resRAI.getListaAliquotaIva()) {
			if(ai.getCodice().equalsIgnoreCase(codiceAliquotaIva)) {
				return ai;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Aliquota IVA", "con codice " + codiceAliquotaIva));
	}
	
	
	private ContoTesoreria findContoTesoreria(String codiceContoTesoreria) {
		LeggiContiTesoreriaResponse resLCT = 	documentoServiceCallGroup.leggiContiTesoreriaCached();
		for(ContoTesoreria ct :resLCT.getContiTesoreria()){
			if(ct.getCodice().equalsIgnoreCase(codiceContoTesoreria)) {
				return ct;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Conto tesoreria", "con codice " + codiceContoTesoreria));
	}
	
	private TipoAtto findTipoAtto(String codiceTipoAtto) {
		TipiProvvedimentoResponse resTP = 	provvedimentoServiceCallGroup.tipiProvvedimentoCached();
		for(TipoAtto ta :resTP.getElencoTipi()){
			if(ta.getCodice().equalsIgnoreCase(codiceTipoAtto)) {
				return ta;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Tipo atto", "con codice " + codiceTipoAtto));
	}
	
	private Distinta findDistinta(String codiceDistinta) {
		ListeResponse resL = genericServiceCallGroup.listeCached(Arrays.asList(TipiLista.DISTINTA_ENTRATA));
		for (CodificaFin codifica : resL.getDistintaEntrata()) {
			if(codifica.getCodice().equalsIgnoreCase(codiceDistinta)) {
				Distinta distinta = new Distinta();
				distinta.setUid(codifica.getUid());
				distinta.setCodice(codifica.getCodice());
				distinta.setDescrizione(codifica.getDescrizione());
				return distinta;
			}
		}
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Distinta", "con codice " + codiceDistinta));
	}
	
	private StrutturaAmministrativoContabile findStrutturaAmmContabile(Integer anno, String codiceClassificatore) {
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse resLCGBCATAA = 	classificatoreBilServiceCallGroup.leggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoCached(anno, codiceClassificatore, TipologiaClassificatore.CDC);
		if(resLCGBCATAA.getClassificatore()==null) {
			resLCGBCATAA = classificatoreBilServiceCallGroup.leggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoCached(anno, codiceClassificatore, TipologiaClassificatore.CDR);
		}
		
		if(resLCGBCATAA.getClassificatore()==null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Struttura Amministrativo Contabile", "con codice " + codiceClassificatore+ " per l'anno "+anno));
		}
				
		return resLCGBCATAA.getCastClassificatore();
	}
	
	
	
	
	
	//############################################################################################################
	//########### Caricamento dei documenti: richiamo del servizio  InserisceElenchiDocumentiService##############
	//############################################################################################################
	
	public static final String CODICE_MESSAGGIO_SERVICE_RESPONSE = "ServiceResponse";

	
	@Override
	protected void elaborateData() {
		inserisceElenchiDocumentiService(elenchiDocumentiAllegato,
				new ResponseHandler<InserisceElenchiDocumentiResponse>() {

					@Override
					protected void handleResponse(InserisceElenchiDocumentiResponse response) {
						res.addErrori(response.getErrori());
						res.addMessaggi(response.getMessaggi());
						
						//Imposto lo stesso esito del servizio InserisceElenchiDocumentiResponse.
						res.setEsito(response.getEsito());
						
						if(Esito.FALLIMENTO.equals(response.getEsito())){
							codiceStatoFileFinale = CodiceStatoFile.ELABORATO_CON_ERRORI;
						}
						
						//Imposto come Messaggio la response del servizio InserisceElenchiDocumentiResponse.
						res.addMessaggio(CODICE_MESSAGGIO_SERVICE_RESPONSE, JAXBUtility.marshall(response));
					}
				}
		
			);
	}

	private InserisceElenchiDocumentiResponse inserisceElenchiDocumentiService(ElenchiDocumentiAllegato eda, ResponseHandler<InserisceElenchiDocumentiResponse> responseHandler) {
		InserisceElenchiDocumenti reqIED = new InserisceElenchiDocumenti();
		reqIED.setRichiedente(req.getRichiedente());
		reqIED.setBilancio(req.getBilancio());
		reqIED.setElenchiDocumentiAllegato(eda);
		
		//InserisceElenchiDocumentiResponse resIED = serviceExecutor.executeServiceSuccessTxRequiresNew(InserisceElenchiDocumentiService.class, reqIED);
		InserisceElenchiDocumentiResponse resIED = serviceExecutor.executeServiceTxRequiresNew(InserisceElenchiDocumentiService.class, reqIED, responseHandler);
		
		return resIED;
		
	}
	
	private Date normalizeDate(Date toNormalize) {
		return toNormalize != null ? Utility.truncateToStartOfDay(toNormalize) : null;
	}
	
}



/**
 * Estende il loggin interno di questo servizio.
 */
class LogUtilInner extends LogUtil{

	public LogUtilInner(Class<?> clazz) {
		super(clazz);
	}

	public void debugInizializzazionePerEntita(final String methodName, Entita entita) {
		debugInizializzazionePerEntita(methodName, entita, "");
	}
	
	public void debugInizializzazionePerEntita(final String methodName, Entita entita, String optionalMessage) {
		String msg = String.format("inizializzazione uids per %s. %s %s",
				entita != null ? entita.getClass().getSimpleName() : "null", 
				entita != null && entita.getUid()<0 ? "Uid di flusso: "+entita.getUid()+"." : "", 
				optionalMessage);
		
		debug(methodName, msg);
	}
	
	public void debugUidEntita(final String methodName, String descrizioneEntita, Entita entita) {
		debug(methodName, "uid "+descrizioneEntita+": "+ (entita!=null?entita.getUid():"null"));

	}
	
}
