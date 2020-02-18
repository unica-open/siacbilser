/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documento.cache.SiopeTipoDebitoImpegnoCacheElementInitializer;
import it.csi.siac.siacbilser.business.service.provvedimento.InserisceProvvedimentoService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siaccommon.util.cache.Cache;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siaccommon.util.cache.MapCache;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenchiDocumenti;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenchiDocumentiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElenchiDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;

/**
 * Inserisce e completa gli Elenchi di Documenti Spesa, Entrata, IVA Spesa, Iva Entrata, comprensivi di AllegatoAtto.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceElenchiDocumentiService extends CheckedAccountBaseService<InserisceElenchiDocumenti, InserisceElenchiDocumentiResponse> {
	
	private static final String CODICE_SIOPE_TIPO_DEBITO_COMMERCIALE = "CO";
	
	//DAD
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private DocumentoDad documentoDad;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	private ProvvedimentoDad provvedimentoDad;
	@Autowired
	private SubdocumentoDad subdocumentoDad;
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	
	//Servizi interni.
	private DocumentoServiceCallGroup dscg;
	
	
	private ElenchiDocumentiAllegato elenchiDocumentiAllegato;
	
	private final Map<String, AllegatoAtto> allegatiAttoInseritiMap = new HashMap<String, AllegatoAtto>();
	private final Map<String, DocumentoSpesa> documentiSpesaInseritiMap = new HashMap<String, DocumentoSpesa>();
	private final Map<String, DocumentoEntrata> documentiEntrataInseritiMap = new HashMap<String, DocumentoEntrata>();
	private final Cache<String, SiopeTipoDebito> cacheSiopeTipoDebitoPerImpegno = new MapCache<String, SiopeTipoDebito>();
	
	private Counter counter;
	private CacheElementInitializer<String, SiopeTipoDebito> cacheInitializerSiopeTipoDebitoPerImpegno;


	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		
		checkEntita(req.getBilancio(), "bilancio", false);
		
		checkNotNull(req.getElenchiDocumentiAllegato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenchi documenti allegato"));
		checkNotNull(req.getElenchiDocumentiAllegato().getElenchiDocumentiAllegato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenchi documenti allegato in elenchi documenti allegato"));
		checkCondition(!req.getElenchiDocumentiAllegato().getElenchiDocumentiAllegato().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenchi documenti allegato in elenchi documenti allegato"));
		elenchiDocumentiAllegato = req.getElenchiDocumentiAllegato();
		res.setElenchiDocumentiAllegato(elenchiDocumentiAllegato);
		
		for(ElencoDocumentiAllegato elencoDocumentiAllegato : elenchiDocumentiAllegato.getElenchiDocumentiAllegato()) {
			checkNotNull(elencoDocumentiAllegato, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti allegato"),false);
			if(elencoDocumentiAllegato==null){
				continue;
			}
			
//			if(elencoDocumentiAllegato.getAllegatoAtto()!=null){
//				checkEntita(elencoDocumentiAllegato.getAllegatoAtto().getAttoAmministrativo(), "atto amministrativo allegato atto",false);
			 	//L'atto amministrativo puo' essere inserito in automatico
//			}
			
//			checkCondition(elencoDocumentiAllegato.getSubdocumenti()!=null && !elencoDocumentiAllegato.getSubdocumenti().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumenti elenco documenti allegato"),false);
			if(elencoDocumentiAllegato.getSubdocumenti()==null){
				continue;
			}
			
			for(Subdocumento<?, ?> subdocumento : elencoDocumentiAllegato.getSubdocumenti()){
				
				checkNotNull(subdocumento, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento elenco documenti allegato"),false);
				if(subdocumento==null) {
					continue;
				}
				
				checkNotNull(subdocumento.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo subdocumento spesa"), false);
				
				checkCondition(subdocumento.getProvvisorioCassa() == null ||  
						(subdocumento.getProvvisorioCassa().getAnno() != null && subdocumento.getProvvisorioCassa().getNumero() != null) ||
						(subdocumento.getProvvisorioCassa().getAnno() == null && subdocumento.getProvvisorioCassa().getNumero() == null),
						 ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero provvisorio di cassa"), false);
				
				if(subdocumento instanceof SubdocumentoSpesa) {
					SubdocumentoSpesa subdocumentoSpesa = (SubdocumentoSpesa)subdocumento;
					if(StringUtils.isNotBlank(subdocumentoSpesa.getCig()) ){
						checkLength(subdocumentoSpesa.getCig(), 0 , 10, "cig quota spesa", false);
					}
					if(StringUtils.isNotBlank(subdocumentoSpesa.getCup())){
						checkLength(subdocumentoSpesa.getCup(), 0 , 15, "cup ordinativo quota spesa", false);
					}
					
					checkCondition(subdocumentoSpesa.getImpegno()==null || subdocumentoSpesa.getImpegno().getUid()== 0 ||
							(subdocumentoSpesa.getImpegno().getAnnoMovimento()!=0 && subdocumentoSpesa.getImpegno().getNumero()!=null), 
							ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero impegno"));
				}
				
				
				Documento<?,?> documento = subdocumento.getDocumento();
				checkNotNull(documento, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento del subdocumento elenco documenti allegato"),false);
				
				if(documento!=null) {
					checkNotNull(documento.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento del subdocumento elenco documenti allegato"),false);
					//checkNotBlank(documento.getNumero(), "anno documento del subdocumento elenco documenti allegato",false); //Il numero verra' staccato in automatico per alcune tipologie di documento
					
					checkEntita(documento.getTipoDocumento(), "tipo documento del subdocumento elenco documenti allegato",false);
					if(documento.getTipoDocumento()!=null){
						checkNotNull(documento.getTipoDocumento().getTipoFamigliaDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo documento del subdocumento elenco documenti allegato"),false);
					}
					
//					checkNotNull(documento.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo documento del subdocumento elenco documenti allegato"),false);
//					checkCondition(documento.getImporto() == null || documento.getImporto().signum() > 0, ErroreCore.VALORE_NON_VALIDO.getErrore("importo documento del subdocumento elenco documenti allegato", ": deve essere positivo"),false);
			/*		if (documento.getArrotondamento() != null) {
						// SIAC-2141: l'arrotondamento e' facoltativo. Ma sull'entita' e' un campo con default a zero => controllo che il segno sia al peggio 0
						checkCondition(documento.getArrotondamento().signum() <= 0, ErroreCore.VALORE_NON_VALIDO.getErrore("Arrotondamento documento", ": deve essere negativo"), false);
						checkCondition(documento.getImporto() == null || documento.getImporto().signum() < 0 || documento.getArrotondamento().signum() > 0
								|| documento.getImporto().add(documento.getArrotondamento()).signum() > 0, ErroreCore.VALORE_NON_VALIDO.getErrore("Arrotondamento documento", ": importo sommato ad arrotondamento deve essere positivo"));
					}
					
					checkEntita(documento.getSoggetto(), "soggetto documento",false);
					
					checkNotNull(documento.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione documento"), false);
					checkNotNull(documento.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo documento"), false);
					checkNotNull(documento.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione documento"), false);
					*/
				}
				
				if(subdocumento.getSubdocumentoIva()!=null) {
					SubdocumentoIva<?,?,?> subdocIva = subdocumento.getSubdocumentoIva();
					checkNotNull(subdocIva.getAnnoEsercizio(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio subdocumento iva spesa"), false);
					
					checkEntita(subdocIva.getTipoRegistrazioneIva(), "tipo registrazione iva", false);
					checkEntita(subdocIva.getRegistroIva(), "registro iva", false);
					if(subdocIva.getRegistroIva()!=null){
						//questo che segue e' l'unico check in più rispetto al servizio sottostante! valutare se tenerlo o far fare la ricerca di tutti i registri iva sperando che siano univoci per codice!
						checkNotNull(subdocIva.getRegistroIva().getTipoRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registro iva"), false);
					}
					
					checkNotNull(subdocIva.getStatoSubdocumentoIva(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato subdocumento iva spesa"), false);
					
					checkCondition(!subdocIva.getListaAliquotaSubdocumentoIva().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aliquote subdocumenti iva"), false);
					
					for (AliquotaSubdocumentoIva aliquota : subdocIva.getListaAliquotaSubdocumentoIva()) {
						checkEntita(aliquota.getAliquotaIva(), "Aliquota IVA", false);
						checkNotNull(aliquota.getImponibile(), "Imponibile aliquota IVA", false);
						checkNotNull(aliquota.getImposta(), "Imposta aliquota IVA", false);
						checkNotNull(aliquota.getTotale(), "Totale aliquota IVA", false);
						checkCondition(aliquota.getImponibile() == null || aliquota.getImposta() == null || aliquota.getTotale() == null
								|| aliquota.getImponibile().add(aliquota.getImposta()).compareTo(aliquota.getTotale()) == 0,
								ErroreCore.VALORE_NON_VALIDO.getErrore("Totale aliquota IVA", ": deve essere pari al totale di imponibile e imposta"), false);
					}
					
				}
				
			}
		}
		
	}
	
	@Override
	protected void init() {
		super.init();
		this.dscg = new DocumentoServiceCallGroup(super.serviceExecutor, req.getRichiedente(), ente, req.getBilancio());
		//this.dse = (DocumentoServiceExecutor)this.serviceExecutor;
		
		documentoDad.setEnte(ente);
		documentoDad.setLoginOperazione(loginOperazione);
		
		provvedimentoDad.setEnte(ente);
		provvedimentoDad.setLoginOperazione(loginOperazione);
		
		subdocumentoDad.setEnte(ente);
		subdocumentoDad.setLoginOperazione(loginOperazione);
		
		counter = new Counter();
		cacheInitializerSiopeTipoDebitoPerImpegno = new SiopeTipoDebitoImpegnoCacheElementInitializer(impegnoBilDad);
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public InserisceElenchiDocumentiResponse executeServiceTxRequiresNew(InserisceElenchiDocumenti serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	public InserisceElenchiDocumentiResponse executeService(InserisceElenchiDocumenti serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		String methodName = "execute";
		
		caricaBilancio();
		checkFaseBilancio();
		
		checkCoerenzaSiope();
		
		for(ElencoDocumentiAllegato elencoDocumentiAllegato : elenchiDocumentiAllegato.getElenchiDocumentiAllegato()) {
			
			log.info(methodName, "Elaborazione elenco: "+elencoDocumentiAllegato.getAnno() +"/"+ elencoDocumentiAllegato.getNumero()); 
			
			inserisciAllegatoAttoEProvvedimento(elencoDocumentiAllegato);
		
			//########## Elaboro i subdocumenti di spesa ########## 
			for(SubdocumentoSpesa subdocumentoSpesa : elencoDocumentiAllegato.getSubdocumentiSpesa()) {
				DocumentoSpesa documentoSpesa = subdocumentoSpesa.getDocumento();
				//documentoSpesa.setTipoRelazione(null);
				
				inserisceSeNecessarioDocumentoSpesaConDocumentiFigliCollegatiESubdocumentiIva(documentoSpesa);
				
				//subdocumentoSpesa.setAttoAmministrativo(attoAmministrativo);??
				dscg.inserisceQuotaDocumentoSpesa(subdocumentoSpesa, false); //il false evita l'aggiornamento dello stato che verrà effettuato solo alla fine!
				counter.incQuoteSpesa();
				String msg = "Inserita Quota "+subdocumentoSpesa.getNumero()+" del DocumentoSpesa "+documentoSpesa.getDescAnnoNumeroTipoDoc();
				res.addMessaggio("DOCSQ-INS", msg);
				log.debug(methodName, msg + " [subcocSpesa uid: "+subdocumentoSpesa.getUid()+"]");
				
				SubdocumentoIvaSpesa subdocumentoIvaCollegatoAQuota = subdocumentoSpesa.getSubdocumentoIva();
				if(subdocumentoIvaCollegatoAQuota!=null){
					subdocumentoIvaCollegatoAQuota.setSubdocumento(new SubdocumentoSpesa());
					subdocumentoIvaCollegatoAQuota.getSubdocumento().setUid(subdocumentoSpesa.getUid());
					dscg.inserisceSubdocumentoIvaSpesa(subdocumentoIvaCollegatoAQuota);
					String msg2 = "Inserito Subdoc Iva "+subdocumentoIvaCollegatoAQuota.getAnnoEsercizio()+"/"+subdocumentoIvaCollegatoAQuota.getProgressivoIVA()+" legato alla quota "+subdocumentoSpesa.getNumero()+" del documento spesa "+documentoSpesa.getDescAnnoNumeroTipoDoc();
					res.addMessaggio("DOCEI-INS", msg2);
					log.debug(methodName, msg2 + " [subdocumentoIvaCollegatoAQuota uid: "+subdocumentoIvaCollegatoAQuota.getUid()+"]");
				}
			}
			
			//########## Elaboro i subdocumenti di entrata ########## 
			for(SubdocumentoEntrata subdocumentoEntrata : elencoDocumentiAllegato.getSubdocumentiEntrata()) {
				DocumentoEntrata documentoEntrata = subdocumentoEntrata.getDocumento();
				//documentoEntrata.setTipoRelazione(null);
				
				DocumentoEntrata documentoEntrataInserito = inserisceSeNecessarioDocumentoEntrataConDocumentiFigliCollegatiESubdocumentiIva(documentoEntrata);

				inserisceAggiornaQuotaDocumentoEntrata(subdocumentoEntrata, documentoEntrataInserito);
				
				SubdocumentoIvaEntrata subdocumentoIvaCollegatoAQuota = subdocumentoEntrata.getSubdocumentoIva();
				if(subdocumentoIvaCollegatoAQuota!=null){
					subdocumentoIvaCollegatoAQuota.setSubdocumento(new SubdocumentoEntrata());
					subdocumentoIvaCollegatoAQuota.getSubdocumento().setUid(subdocumentoEntrata.getUid());
					dscg.inserisceSubdocumentoIvaEntrata(subdocumentoIvaCollegatoAQuota);
					String msg = "Inserito Subdoc Iva "+subdocumentoIvaCollegatoAQuota.getAnnoEsercizio()+"/"+subdocumentoIvaCollegatoAQuota.getProgressivoIVA()+" legato alla quota " + subdocumentoEntrata.getNumero() + " del documento entrata "+documentoEntrata.getDescAnnoNumeroTipoDoc();
					res.addMessaggio("DOCEI-INS", msg);
					log.debug(methodName, msg + " [subdocumentoIvaCollegatoAQuota uid: "+subdocumentoIvaCollegatoAQuota.getUid()+"]");
				}
			}
			
			//########## Elaboro i subdocumenti iva spesa ########## 
			for(SubdocumentoIvaSpesa subdocumentoIvaSpesa : elencoDocumentiAllegato.getSubdocumentiIvaSpesa()) {
				DocumentoSpesa documentoSpesa = subdocumentoIvaSpesa.getDocumento();
				//documentoSpesa.setTipoRelazione(null);
				
				inserisceSeNecessarioDocumentoSpesaConDocumentiFigliCollegatiESubdocumentiIva(documentoSpesa);
				
				DocumentoSpesa documentoSpesaReferenced = new DocumentoSpesa();
				documentoSpesaReferenced.setUid(documentoSpesa.getUid());
				subdocumentoIvaSpesa.setDocumento(documentoSpesaReferenced);
				dscg.inserisceSubdocumentoIvaSpesa(subdocumentoIvaSpesa);
				String msg = "Inserito Subdoc Iva "+subdocumentoIvaSpesa.getAnnoEsercizio()+"/"+subdocumentoIvaSpesa.getProgressivoIVA()+" del DocumentoSpesa "+documentoSpesa.getDescAnnoNumeroTipoDoc();
				res.addMessaggio("DOCSI-INS", msg);
				log.debug(methodName, msg + " [subdocumentoIvaSpesa uid: "+subdocumentoIvaSpesa.getUid()+"]");
				
			}
			
			//########## Elaboro i subdocumenti iva entrata ########## 
			for(SubdocumentoIvaEntrata subdocumentoIvaEntrata : elencoDocumentiAllegato.getSubdocumentiIvaEntrata()) {
				DocumentoEntrata documentoEntrata = subdocumentoIvaEntrata.getDocumento();
				//documentoEntrata.setTipoRelazione(null);
				
				inserisceSeNecessarioDocumentoEntrataConDocumentiFigliCollegatiESubdocumentiIva(documentoEntrata);
				
				DocumentoEntrata documentoEntrataReferenced = new DocumentoEntrata();
				documentoEntrataReferenced.setUid(documentoEntrata.getUid());
				subdocumentoIvaEntrata.setDocumento(documentoEntrataReferenced);
				dscg.inserisceSubdocumentoIvaEntrata(subdocumentoIvaEntrata);
				String msg = "Inserito Subdoc Iva "+subdocumentoIvaEntrata.getAnnoEsercizio()+"/"+subdocumentoIvaEntrata.getProgressivoIVA()+" del DocumentoEntrata "+documentoEntrata.getDescAnnoNumeroTipoDoc();
				res.addMessaggio("DOCEI-INS", msg);
				log.debug(methodName, msg + " [subdocumentoIvaEntrata uid: "+subdocumentoIvaEntrata.getUid()+"]");
			}

			inserisceSeNecessarioElencoDocumentiAllegato(elencoDocumentiAllegato);
			
		}
		

		//per tutti i documenti  spesa   precedentemente inseriti viene invocato l'aggiornamento dello stato operativo.
		for(DocumentoSpesa documentoSpesaInserito : documentiSpesaInseritiMap.values()){
			
			
			for(SubdocumentoIvaSpesa subdocumentoIvaCollegatoAlDoc : documentoSpesaInserito.getListaSubdocumentoIva(SubdocumentoIvaSpesa.class)) {
				subdocumentoIvaCollegatoAlDoc.setDocumento(new DocumentoSpesa());
				subdocumentoIvaCollegatoAlDoc.getDocumento().setUid(documentoSpesaInserito.getUid());
				dscg.inserisceSubdocumentoIvaSpesa(subdocumentoIvaCollegatoAlDoc);
			}
			
			for(SubdocumentoIvaEntrata subdocumentoIvaCollegatoAlDoc : documentoSpesaInserito.getListaSubdocumentoIva(SubdocumentoIvaEntrata.class)){
				subdocumentoIvaCollegatoAlDoc.setDocumento(new DocumentoEntrata());
				subdocumentoIvaCollegatoAlDoc.getDocumento().setUid(documentoSpesaInserito.getUid());
				dscg.inserisceSubdocumentoIvaEntrata(subdocumentoIvaCollegatoAlDoc);
			}
			
			dscg.aggiornaStatoDocumentoDiSpesa(documentoSpesaInserito);
		}
		
		//per tutti i documenti  entrata precedentemente inseriti viene invocato l'aggiornamento dello stato operativo.
		for(DocumentoEntrata documentoEntrataInserito : documentiEntrataInseritiMap.values()){
			
			for(SubdocumentoIvaSpesa subdocumentoIvaCollegatoAlDoc : documentoEntrataInserito.getListaSubdocumentoIva(SubdocumentoIvaSpesa.class)) {
				subdocumentoIvaCollegatoAlDoc.setDocumento(new DocumentoSpesa());
				subdocumentoIvaCollegatoAlDoc.getDocumento().setUid(documentoEntrataInserito.getUid());
				dscg.inserisceSubdocumentoIvaSpesa(subdocumentoIvaCollegatoAlDoc);
			}
			
			for(SubdocumentoIvaEntrata subdocumentoIvaCollegatoAlDoc : documentoEntrataInserito.getListaSubdocumentoIva(SubdocumentoIvaEntrata.class)){
				subdocumentoIvaCollegatoAlDoc.setDocumento(new DocumentoEntrata());
				subdocumentoIvaCollegatoAlDoc.getDocumento().setUid(documentoEntrataInserito.getUid());
				dscg.inserisceSubdocumentoIvaEntrata(subdocumentoIvaCollegatoAlDoc);
			}
			
			dscg.aggiornaStatoDocumentoDiEntrata(documentoEntrataInserito);
		}
		
		TransactionAspectSupport.currentTransactionStatus().flush();
		
		//SIAC-7047
		//su richiesta si disabilita il servizio CompletaAllegatoAtto
		log.info(methodName,"Operazione CompletaAllegatoAtto temporaneamente disabilitata");
		//tutti gli allegati precedentemente inseriti vengono completati.
//		for(AllegatoAtto allegatoAttoInserito : allegatiAttoInseritiMap.values()){
//			dscg.completaAllegatoAtto(allegatoAttoInserito);
//		}

		
		aggiungiMessaggioDiRiepilogo();
		
		TransactionAspectSupport.currentTransactionStatus().flush();
//		throw new BusinessException("Finalmente ha finito!!! Avrebbe committato ma preferisco fare rollback!!!");

	}

	/**
	 * Controllo della coerenza dei dati per il SIOPE+
	 */
	private void checkCoerenzaSiope() {
		for(ElencoDocumentiAllegato eda : this.elenchiDocumentiAllegato.getElenchiDocumentiAllegato()) {
			for(SubdocumentoSpesa ss : eda.getSubdocumentiSpesa()) {
				/*
				 * SIAC-5311
				 * Se l'impegno ha Tipo Debito SIOPE commerciale il campo CIG o in alternativa il Motivo esclusione SIOPE sono obbligatori.
				 * Nel caso in cui il CIG fosse obbligatorio e non venisse passato dal fruitore nella chiamata e necessario recuperarlo dall'impegno,
				 * se fosse assente anche li' evidenziarlo in un opportune messaggio di assenza del CIG sul movimento selezionato.
				 */
				checkCoerenzaSiope(ss);
			}
		}
	}

	private void checkCoerenzaSiope(SubdocumentoSpesa ss) {
		final String methodName = "checkCoerenzaSiope";
		Impegno impSub = ss.getImpegnoOSubImpegno();
		if(impSub == null || impSub.getUid() == 0) {
			log.debug(methodName, "Impegno non presente sulla quota [" + ss.getUid() + "]");
			ss.setSiopeAssenzaMotivazione(null);
			// nessun check da fare
			return;
		}
		SiopeTipoDebito std = impSub.getSiopeTipoDebito();
		String impKey = calcolaChiaveImpegno(ss);
		String cacheKey = (impSub instanceof SubImpegno ? "SI" : "I") + "_" + impSub.getUid();
		if(std == null || std.getUid() == 0 || StringUtils.isBlank(std.getCodice())) {
			log.debug(methodName, "Inizializzazione del siope tipo debito per " + cacheKey);
			std = cacheSiopeTipoDebitoPerImpegno.get(cacheKey, cacheInitializerSiopeTipoDebitoPerImpegno);
		} else {
			cacheSiopeTipoDebitoPerImpegno.put(cacheKey, std);
		}
		// Controllo se sia un commerciale
		if(std == null || !CODICE_SIOPE_TIPO_DEBITO_COMMERCIALE.equals(std.getCodice())) {
			log.debug(methodName, "Siope tipo debito NON COMMERCIALE per impegno " + cacheKey);
			ss.setSiopeAssenzaMotivazione(null);
			return;
		}
		if(ss.getSiopeAssenzaMotivazione() != null && ss.getSiopeAssenzaMotivazione().getUid() != 0) {
			log.debug(methodName, "Siope assenza motivazione presente sulla quota");
			return;
		}
		if(StringUtils.isNotBlank(ss.getCig())) {
			log.debug(methodName, "CIG presente sulla quota");
			return;
		}
		if(impSub.getSiopeAssenzaMotivazione() != null && impSub.getSiopeAssenzaMotivazione().getUid() != 0) {
			log.debug(methodName, "Motivo assenza CIG presente sull'impegno [" + cacheKey + "]: clone del dato sulla quota");
			ss.setSiopeAssenzaMotivazione(impSub.getSiopeAssenzaMotivazione());
			return;
		}
		if(StringUtils.isNotBlank(impSub.getCig())) {
			log.debug(methodName, "CIG presente sull'impegno [" + cacheKey + "]: clone del dato sulla quota");
			ss.setCig(impSub.getCig());
			return;
		}
		log.debug(methodName, "Impegno senza CIG");
		throw new BusinessException(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("CIG o motivo di assenza CIG per la quota relativa all'impegno " + impKey));
	}

	private String calcolaChiaveImpegno(SubdocumentoSpesa ss) {
		Impegno i = ss.getImpegno();
		SubImpegno si = ss.getSubImpegno();
		StringBuilder sb = new StringBuilder();
		sb.append(i.getAnnoMovimento());
		sb.append("/");
		sb.append(i.getNumero().toPlainString());
		if(si != null && si.getUid() != 0) {
			sb.append("-");
			sb.append(si.getNumero().toPlainString());
		}
		return sb.toString();
	}

	/**
	 * Aggiungi un messaggio di riepilogo sulle entita' elaborate.
	 * 
	 */
	private void aggiungiMessaggioDiRiepilogo() {
		res.addMessaggio("RIEP", counter.toString());
	}

	private void inserisceSeNecessarioElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		final String methodName = "inserisceSeNecessarioElencoDocumentiAllegato";
		
		if(Boolean.TRUE.equals(elencoDocumentiAllegato.getSaltaInserimentoInCaricaDocumenti())) {
			log.debug(methodName, "inserimento dell'elenco saltato per impostazione dell'attributo 'saltaInserimento' sull'elenco.");
			return;
		}
		
		log.debug(methodName, "inserimento dell'elenco.");
		dscg.inserisceElencoDocumentiAllegatoAtto(elencoDocumentiAllegato);
		counter.incElenchi();
	}

	private void inserisciAllegatoAttoEProvvedimento(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		final String methodName = "inserisciAllegatoAttoEProvvedimento";
		
		AllegatoAtto allegatoAtto = elencoDocumentiAllegato.getAllegatoAtto();
		
		if(allegatoAtto==null) { //L'allegato atto e' facoltativo.
			log.debug(methodName, "allegatoAtto in input non valorizzato. Non verrà inserito.");
			return;
		}
			
		//Il provvedimento deve essere in stato DEFINITIVO: (tale controllo viene già fatto da 
		//InserisceAllegatoAttoService invocato successivamente. Non aggiungere qui)
		if(allegatoAtto.getAttoAmministrativo() == null) {
			
			log.debug(methodName, "AttoAmministrativo in input non valorizzato. Viene valutato l'inserimento automatico.");
			//l'atto amministrativo non e' stato passato! Lo creo in automatico.
			
			
			TipoAtto tipoAtto = provvedimentoDad.getTipoAttoALGEvenNull();  //In caso di assenza del tipo provvedimento 'ALG' NON inserire l'allegato atto e 
			//segnalare nei log il seguente messaggio 'Tipo provvedimento 'ALG' non presente in archivio, provvedimento allegato atto non inserito' e proseguire nell'elaborazione.
			if(tipoAtto==null) {
				log.debug(methodName, "tipoProvvedimento 'ALG' NON presente per l'ente "+ente.getUid()+". L'allagato Atto non verrà inserito.");
				
				res.addMessaggio("","Tipo provvedimento 'ALG' non presente in archivio, provvedimento allegato atto non inserito per l'elenco: "+
										elencoDocumentiAllegato.getAnno() +"/"+ elencoDocumentiAllegato.getNumero());
				
				//elimino la reference dell'allegato atto nell'elenco.
				elencoDocumentiAllegato.setAllegatoAtto(null);
				return;
			}
			
			log.debug(methodName, "AttoAmministrativo in input non valorizzato e tipo provvedimento 'ALG' presente. Inserisco l'AttoAmministrativo in automatico.");
			
			AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
			attoAmministrativo.setAnno(req.getBilancio().getAnno());
			attoAmministrativo.setTipoAtto(tipoAtto);
			attoAmministrativo.setOggetto(allegatoAtto.getCausale());
			attoAmministrativo.setStatoOperativo(StatoOperativoAtti.DEFINITIVO);
			
			InserisceProvvedimentoResponse resIP = inserisceProvvedimento(attoAmministrativo);
			counter.incAttoAmministrativiAlg();
			
			attoAmministrativo.setUid(resIP.getAttoAmministrativoInserito().getUid());
			
			allegatoAtto.setAttoAmministrativo(attoAmministrativo);
		}
		
		if(allegatoAtto.getAttoAmministrativo().getUid()==0) {
			//l'atto amministrativo e' da inserire. Vedi CR SIAC-4228.
			InserisceProvvedimentoResponse resIP = inserisceProvvedimento(allegatoAtto.getAttoAmministrativo());
			counter.incAttoAmministrativiAlg(); //TODO Prevedere un counter NON ALG. Non per forza e' ALG. Ma per adesso questi automatici saranno solo di tipo ALG!
			allegatoAtto.getAttoAmministrativo().setUid(resIP.getAttoAmministrativoInserito().getUid());
		}
		
		String keyAllegatoAtto = computeLogicalKey(allegatoAtto);
		if(!allegatiAttoInseritiMap.containsKey(keyAllegatoAtto /*allegatoAtto.getUid()*/)){
			AllegatoAtto allegatoAttoInserito = dscg.inserisceAllegatoAtto(allegatoAtto, StatoOperativoAllegatoAtto.DA_COMPLETARE/*, ErroreFin.ATTO_GIA_ABBINATO.getCodice()*/);
			counter.incAllegatiAtto();
			TransactionAspectSupport.currentTransactionStatus().flush();
			allegatoAtto.setUid(allegatoAttoInserito.getUid());//questo Set agisce su tutte le istanze di allegatoAtto nel flusso.
			allegatiAttoInseritiMap.put(keyAllegatoAtto /*allegatoAttoInserito.getUid()*/, allegatoAttoInserito);
		}
			
	}

	private InserisceProvvedimentoResponse inserisceProvvedimento(AttoAmministrativo attoAmministrativo) {
		InserisceProvvedimento reqIP = new InserisceProvvedimento();
		reqIP.setRichiedente(req.getRichiedente());
		reqIP.setEnte(ente);
		
		reqIP.setAttoAmministrativo(attoAmministrativo);
		reqIP.setStrutturaAmministrativoContabile(attoAmministrativo.getStrutturaAmmContabile());
		reqIP.setTipoAtto(attoAmministrativo.getTipoAtto());
		InserisceProvvedimentoResponse resIP = serviceExecutor.executeServiceSuccess(InserisceProvvedimentoService.class, reqIP);
		
		return resIP;
	}

	/**
	 * Inserisce/aggiorna quota documento entrata.
	 * Se la quota appartiene ad un documento già esistente viene valutata l'esistenza del provvisorio di cassa e in tal caso
	 * viene applicato l'algoritmo descritto al punto 3 in analisi per le quote entrata.
	 *
	 * @param subdocumentoEntrata the subdocumento entrata
	 * @param documentoEntrataInArchivio the documento entrata in archivio
	 */
	private void inserisceAggiornaQuotaDocumentoEntrata(SubdocumentoEntrata subdocumentoEntrata, DocumentoEntrata documentoEntrataInArchivio) {
		final String methodName = "inserisceAggiornaQuotaDocumentoEntrata";
		
		DocumentoEntrata documentoEntrata = subdocumentoEntrata.getDocumento();
		//Nota bene: documentoEntrataInArchivio ha la stessa reference a documentoEntrata se questo è stato veramente inserito, 
		//mentre ha una reference diversa se si è nel caso in cui il documento esisteva già!
		
		
		if(documentoEntrataInArchivio == documentoEntrata
				|| !Boolean.TRUE.equals(documentoEntrata.getTipoDocumento().getFlagAggiornaQuoteDaElenco())) { 
			//hanno la stessa reference, quindi il documento è nuovo ed inserisco tutte le quote.
			// oppure sto aggiornando un documentoEntrata già esistente ma il flag aggiornamento sul tipo documento è FALSE o null.
			dscg.inserisceQuotaDocumentoEntrata(subdocumentoEntrata, false); //il false evita l'aggiornamento dello stato che verrà effettuato solo alla fine!
			counter.incQuoteEntrata();
			String msg = "Inserita Quota "+subdocumentoEntrata.getNumero()+" del documento entrata "+documentoEntrata.getDescAnnoNumeroTipoDoc();
			res.addMessaggio("DOCEQ-INS", msg);
			log.debug(methodName, msg + " [subcocEntrata uid: "+subdocumentoEntrata.getUid()+"]");
			
			return;
		}
		
		//Sto aggiornando un documentoEntrata già esistente e il flag aggiornamento è TRUE.
		
		//se lo stato del documento già presente in archivio è 'EM' o 'ST' si segnala il 
		//messaggio <FIN_ERR_0268, Documento non modificabile, 'Documento già emesso o stornato'>
		if(StatoOperativoDocumento.EMESSO.equals(documentoEntrataInArchivio.getStatoOperativoDocumento()) || 
				StatoOperativoDocumento.STORNATO.equals(documentoEntrataInArchivio.getStatoOperativoDocumento())){
			throw new BusinessException(ErroreFin.DOCUMENTO_NON_MODIFICABILE.getErrore("non modificabile", "Documento gia' emesso o stornato"));
		}
		
		//le quote passate in input senza provvisorio di cassa si inseriscono e si associano al documento 
		//aggiornando anche l'importo del documento andando a sommare l'importo delle quota appena inserita all'importo totale del documento
		if(subdocumentoEntrata.getProvvisorioCassa()==null || subdocumentoEntrata.getProvvisorioCassa().getUid()==0) {
			//la quota in input NON ha il provvisorio di cassa ...aggiorno l'importo del doc e inserisco la quota.
			documentoDad.updateImportoDocumento(subdocumentoEntrata.getImporto(), null, documentoEntrataInArchivio);
			dscg.inserisceQuotaDocumentoEntrata(subdocumentoEntrata, false);
			counter.incQuoteEntrata();
			counter.incQuoteEntrataProvvCassa();
			String msg = "Inserita Quota "+subdocumentoEntrata.getNumero()+" del documento entrata "+documentoEntrata.getDescAnnoNumeroTipoDoc();
			res.addMessaggio("DOCEQ-INS", msg);
			log.debug(methodName, msg + " [subcocEntrata uid: "+subdocumentoEntrata.getUid()+"]");
			
			return;
		} 
		
		//La quota in input HA il provvisorio di cassa.
		
		//si considerano solamente le quote passate in input con i dati del provvisorio di cassa, 
		//e per ogni quota del documento si effettuano i seguenti controlli e le successive operazioni:
		
		//si ricerca tra le quote già presenti in archivio che non hanno i dati del provvisorio di cassa, 
		//quella che ha i dati dell'accertamento (anche se nulli) uguali alla quota passata in input, inoltre 
		//l'importo della quota passata come parametro deve essere minore o uguale a quella trovata in archivio. 
		//Nel caso in cui si trovino più quote con queste caratteristiche si segnala il messaggio 
		//<FIN_ERR_0268, Documento non modificabile, 'Quote incongruenti rispetto ai dati in archivio'>
		SubdocumentoEntrata subdocEntrataInArchivioTrovato = cercaSubdocumentoEntrataConStessoAccertamentoPrivoDelProvvisorioDiCassa(subdocumentoEntrata);
		
		if(subdocEntrataInArchivioTrovato==null){ //non e' stata trovata nessuna quota a cui mancava il provvisorio di cassa la inserisco
			documentoDad.updateImportoDocumento(subdocumentoEntrata.getImporto(), null, documentoEntrataInArchivio);
			dscg.inserisceQuotaDocumentoEntrata(subdocumentoEntrata, false);
			counter.incQuoteEntrata();
			String msg = "Inserita Quota "+subdocumentoEntrata.getNumero()+" del DocumentoEntrata "+documentoEntrata.getDescAnnoNumeroTipoDoc();
			res.addMessaggio("DOCEQ-INS", msg);
			log.debug(methodName, msg + " [subcocEntrata uid: "+subdocumentoEntrata.getUid()+"]");
			
			return;
		} 
		
		//Aggiorno la quota trovata...
			
		//si aggiorna la quota già presente in archivio con i dati del provvisorio di cassa e 
		//dell'eventuale provvedimento dell'allegato atto passati come parametro.
		subdocumentoDad.aggiornaProvvisorioDiCassa(subdocEntrataInArchivioTrovato.getUid(), subdocumentoEntrata.getProvvisorioCassa());
		documentoDad.aggiornaAttoAmministrativo(subdocEntrataInArchivioTrovato.getUid(), subdocumentoEntrata.getAttoAmministrativo());
		
		
		if(subdocumentoEntrata.getImporto().compareTo(subdocEntrataInArchivioTrovato.getImporto())<0){
			//Se importo quota passato in input <importo quota trovata in archivio allora  
			//Si calcola la differenza tra la quota reperita in archivio e quella passata in input; 
			//Si crea, contestualmente, una nuova quota identica a quella aggiornata con importo pari alla differenza calcolata, SENZA provvisorio di cassa 
			BigDecimal differenzaImporto = subdocEntrataInArchivioTrovato.getImporto().subtract(subdocumentoEntrata.getImporto());
			log.debug(methodName, "creo una nuova quota entrata SENZA provvisorio per l'importo rimanente: "+differenzaImporto);
			
			subdocumentoEntrata.setImporto(differenzaImporto);
			dscg.inserisceQuotaDocumentoEntrata(subdocumentoEntrata, false);
			counter.incQuoteEntrata();
			counter.incQuoteEntrataProvvCassa();
			String msg = "Inserita Quota "+subdocumentoEntrata.getNumero()+" del DocumentoEntrata "+documentoEntrata.getDescAnnoNumeroTipoDoc();
			res.addMessaggio("DOCEQ-INS", msg);
			log.debug(methodName, msg + " [subcocEntrata uid: "+subdocumentoEntrata.getUid()+"]");
		}
		
		
		
	}

	/**
	 * Cerca subdocumento entrata con stesso accertamento privo del provvisorio di cassa.
	 *
	 * @param subdocumentoEntrata the subdocumento entrata
	 * @return the subdocumento entrata
	 * 
	 * @throws BusinessException se trova più di una quota possibile.
	 */
	private SubdocumentoEntrata cercaSubdocumentoEntrataConStessoAccertamentoPrivoDelProvvisorioDiCassa(SubdocumentoEntrata subdocumentoEntrata) {
		
		final String methodName = "cercaSubdocumentoConStessoAccertamentoPrivoDelProvvisorioDiCassa";
		
		DocumentoEntrata documentoEntrata = subdocumentoEntrata.getDocumento();
		
		SubdocumentoEntrata subdocEntrataInArchivioTrovato = null;
		
		
		RicercaQuoteByDocumentoEntrataResponse resRQBDS = dscg.ricercaQuoteByDocumentoEntrataCached(documentoEntrata);
		
		for(SubdocumentoEntrata subdocEntrataInArchivio: resRQBDS.getSubdocumentiEntrata()) {
				
				if(
						//Non ha il provvisorio di cassa 
						(subdocEntrataInArchivio.getProvvisorioCassa()==null || subdocEntrataInArchivio.getProvvisorioCassa().getUid()==0)
						
						&& //e
						
						(
							//ha l'accertamento nulli sia in archivio che in input (ma siamo sicuri?? in analisi dice così!)
							(subdocEntrataInArchivio.getAccertamento()==null &&  subdocumentoEntrata.getAccertamento()==null)
							
							|| //oppure
							
							//stesso accertamento sia in archivio che in input
							(subdocEntrataInArchivio.getAccertamento()!=null 
								&& subdocumentoEntrata.getAccertamento()!=null 
								&& subdocEntrataInArchivio.getAccertamento().getUid() == subdocumentoEntrata.getAccertamento().getUid())
							
						)
								
					) {
					
					log.info(methodName, "Trovata quota Entrata numero "+ subdocEntrataInArchivio.getNumero() 
							+ " [uid:"+ subdocEntrataInArchivio.getUid()+"] del documentoEntrata "+documentoEntrata.getDescAnnoNumeroTipoDoc()
							+ " [uid:"+ documentoEntrata.getUid() + "] esiste con Accertamento: "
							+ (subdocEntrataInArchivio.getAccertamento()!=null?subdocEntrataInArchivio.getAccertamento().getUid(): "null")
							+ " con stesso Accertamento di quello passato in input."
							);
					
					if(subdocEntrataInArchivioTrovato == null) {
						subdocEntrataInArchivioTrovato = subdocEntrataInArchivio;
						//non faccio break per controllare se ci sono altre quote selezionabili!
						
					} else {
						//ce n'era già una!
						
						log.error(methodName, "Trovata piu' di una quota Entrata che con Accertamento: "
												+ (subdocEntrataInArchivio.getAccertamento()!=null?subdocEntrataInArchivio.getAccertamento().getUid(): "null")
												+ " [uids quote entrata:"+subdocEntrataInArchivioTrovato.getUid() + ", " 
												+ subdocEntrataInArchivio.getUid()+"]");
						
						throw new BusinessException(ErroreFin.DOCUMENTO_NON_MODIFICABILE.getErrore("non modificabile", "Quote incongruenti rispetto ai dati in archivio"));
						
					}
				}
			
		}
		return subdocEntrataInArchivioTrovato;
	}
	
	/**
	 * Controlla che il Bilancio sia nelle seguenti fasi:<br>
	 * - Esercizio Provvisorio<br>
	 * - Gestione<br>
	 * - Assestamento<br>
	 * - Predisposizione Consuntivo<br>
	 */
	private void checkFaseBilancio() {
		FaseBilancio fase = req.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		Set<FaseBilancio> fasi = EnumSet.of(FaseBilancio.ESERCIZIO_PROVVISORIO, FaseBilancio.GESTIONE, FaseBilancio.ASSESTAMENTO,  FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO);
		
		if(!fasi.contains(fase)){
			//Il Bilancio qui e' PLURIENNALE, PREVISIONE, CHIUSO
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("bilancio", fase.toString()));
		}
		
		
		
	}

	protected Bilancio caricaBilancio(){
		Bilancio bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
		req.setBilancio(bilancio);
		return bilancio;
	}
	

	private String computeLogicalKey(DocumentoEntrata documentoEntrata) {
		return documentoEntrata.getDescAnnoNumeroTipoDoc() + "_" + documentoEntrata.getTipoDocumento().getTipoFamigliaDocumento();
	}

	private String computeLogicalKey(DocumentoSpesa documentoSpesa) {
		return documentoSpesa.getDescAnnoNumeroTipoDoc() + "_" + documentoSpesa.getTipoDocumento().getTipoFamigliaDocumento();
	}

	private String computeLogicalKey(AllegatoAtto allegatoAtto) {
		//la chiave è 'latto amministrativo stesso: corrispondenza biunivoca allegatoAtto-attoAmministrativo
		return ""+allegatoAtto.getAttoAmministrativo().getUid();
	}
	
	private DocumentoSpesa inserisceSeNecessarioDocumentoSpesaConDocumentiFigliCollegatiESubdocumentiIva(/*Map<String, DocumentoSpesa> documentiSpesaInseritiMap,*/ DocumentoSpesa documentoSpesa) {
		
		//Se il documentono NON e' già stato inserito:
		String keyDocumentoSpesa = computeLogicalKey(documentoSpesa);
		if(!documentiSpesaInseritiMap.containsKey(keyDocumentoSpesa /*documentoSpesa.getUid()*/)){
			DocumentoSpesa documentoSpesaInserito = inserisceDocumentoSpesaConDocumentiFigliCollegatiESubdocumentiIva(documentoSpesa);
			//documentoSpesa.setUid(documentoSpesaInserito.getUid(); //questo Set agisce su tutte le istanze di allegatoAtto nel flusso.
			documentiSpesaInseritiMap.put(keyDocumentoSpesa /*documentoSpesaInserito.getUid()*/, documentoSpesaInserito);
			return documentoSpesaInserito;
		} else {
			return documentiSpesaInseritiMap.get(keyDocumentoSpesa);
		}
		
	}

	private DocumentoSpesa inserisceDocumentoSpesaConDocumentiFigliCollegatiESubdocumentiIva(DocumentoSpesa documentoSpesa) {
		final String methodName = "inserisceDocumentoSpesaConDocumentiFigliCollegatiESubdocumentiIva";
		
		gestisciNumerazioneAutomaticaDocumento(documentoSpesa);
		
		TipoFamigliaDocumento tipoFamigliaDocumento = documentoSpesa.getTipoDocumento().getTipoFamigliaDocumento();
		InserisceDocumentoSpesaResponse resIDS;
		if(TipoFamigliaDocumento.SPESA.equals(tipoFamigliaDocumento)) {
			RicercaPuntualeDocumentoSpesaResponse resRPDS = ricercaDocumentoGiaPresente(documentoSpesa);
			if(resRPDS.getDocumentoSpesa()==null){ //Il documento non esiste. Lo inserisco.
				resIDS = dscg.inserisceDocumentoSpesa(documentoSpesa, false, false);
			} else { //Il documento esisteva già...
				resIDS = new InserisceDocumentoSpesaResponse();
				resIDS.setDocumentoSpesa(resRPDS.getDocumentoSpesa());
				resIDS.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("Documento spesa", documentoSpesa.getDescAnnoNumeroTipoDoc()));
			}
			counter.incDocSpesa();
		} else if(TipoFamigliaDocumento.IVA_SPESA.equals(tipoFamigliaDocumento)) {
			DocumentoSpesa docTrovato = ricercaDocumentoTestataIVAGiaPresente(documentoSpesa);
			if(docTrovato==null){ //Il documento non esiste. Lo inserisco.
				resIDS = dscg.inserisceTestataDocumentoSpesa(documentoSpesa, true, false);
			} else { //Il documento esisteva già...
				resIDS = new InserisceDocumentoSpesaResponse();
				resIDS.setDocumentoSpesa(docTrovato);
				resIDS.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("Testata IVA spesa", documentoSpesa.getDescAnnoNumeroTipoDoc()));
			}
			counter.incTestataIVASpesa();
		} else {
			throw new IllegalArgumentException("Il TipoFamigliaDocumento specificato deve essere di tipo "
							+TipoFamigliaDocumento.SPESA +" o "
							+TipoFamigliaDocumento.IVA_SPESA +" per il documento: "
							+ documentoSpesa.getDescAnnoNumeroTipoDoc() 
							+ "[valore passato: "+tipoFamigliaDocumento+"] ");
		}
		
		DocumentoSpesa documentoSpesaInserito = resIDS.getDocumentoSpesa();
		documentoSpesa.setUid(documentoSpesaInserito.getUid());
		
//		documentiInseritiMap.put(documentoSpesaInserito.getUid(), documentoSpesaInserito);
		
		if(resIDS.verificatoErrore(ErroreCore.ENTITA_PRESENTE)){
			/*Se il documento è stato trovato non si effettua alcuna operazione e 
			 * si prosegue l'elaborazione al punto successivo (non si può re-inserire ma nemmeno aggiornare in quanto potrebbe 
			 * già essere stato modificato dagli operatori della ragioneria).*/
			//return documentoSpesaInserito;
			
			//Si eliminano tutte le quote senza impegno già abbinate al documento presenti in archivio. (solo per DocumentoSpesa, per DocumentoEntrata non si fa nulla)
			
			log.debug(methodName, "Il documentoSpesa "+documentoSpesa.getDescAnnoNumeroTipoDoc()+" [uid:"+ documentoSpesa.getUid() + "] è già presente in archivio. Le sue quote prive di Impegno verranno eliminate.");
			RicercaQuoteByDocumentoSpesaResponse resRQBDS = dscg.ricercaQuoteByDocumentoSpesa(documentoSpesa);
			for(SubdocumentoSpesa subdocSpesa: resRQBDS.getSubdocumentiSpesa()) {
				if(subdocSpesa.getImpegnoOSubImpegno()==null) {
					log.debug(methodName, "Elimino la quota spesa numero "+ subdocSpesa.getNumero() 
							+ " [uid:"+ subdocSpesa.getUid()+"] del documentoSpesa "+documentoSpesa.getDescAnnoNumeroTipoDoc()
							+ " [uid:"+ documentoSpesa.getUid() + "]");
					
					dscg.eliminaQuotaDocumentoSpesa(subdocSpesa, false, true);
				}
			}
			
			log.debug(methodName, "Aggiornato DocumentoSpesa "+documentoSpesaInserito.getDescAnnoNumeroTipoDoc()+" con uid: "+documentoSpesaInserito.getUid());
			res.addMessaggio("DOCS-AGG", "Aggiornato documento Spesa "+documentoSpesaInserito.getDescAnnoNumeroTipoDoc());
			
		} else {
			log.debug(methodName, "Inserito DocumentoSpesa "+documentoSpesaInserito.getDescAnnoNumeroTipoDoc()+" con uid: "+documentoSpesaInserito.getUid());
			res.addMessaggio("DOCS-INS", "Inserito documento Spesa "+documentoSpesaInserito.getDescAnnoNumeroTipoDoc());
		}
			
		for(DocumentoSpesa documentoCollegato : documentoSpesa.getListaDocumentiSpesaFiglio()){
			log.debug(methodName, "tipoRelazione: " + documentoCollegato.getTipoRelazione());
			documentoCollegato.addDocumentoSpesaPadre(documentoSpesa);
			
			if(documentoCollegato.getTipoRelazione().equals(TipoRelazione.NOTA_CREDITO)  /*TipoGruppoDocumento.NOTA_DI_CREDITO.getCodice().equals(documentoCollegato.getTipoDocumento().getCodiceGruppo())*/){
				dscg.inserisceNotaCreditoSpesa(documentoCollegato);
			} else {
				//dscg.inserisceDocumentoSpesa(documentoCollegato);//TODO Valutare ricorsione!!!
				//if(firstRecursion) {
					inserisceSeNecessarioDocumentoSpesaConDocumentiFigliCollegatiESubdocumentiIva(documentoCollegato/*, false*/);
				//}
			}
			
			documentoCollegato.getListaDocumentiSpesaPadre().clear(); //rimuovo dall lista per evitare la "infinite recursion in object graph"!!
		}                      
		
		for(DocumentoEntrata documentoCollegato : documentoSpesa.getListaDocumentiEntrataFiglio()){
			log.debug(methodName, "tipoRelazione: " + documentoCollegato.getTipoRelazione());
			
			documentoCollegato.addDocumentoSpesaPadre(documentoSpesa);
			
			/*if(documentoCollegato.getTipoRelazione().equals(TipoRelazione.NOTA_CREDITO)){  //TipoGruppoDocumento.NOTA_DI_CREDITO.getCodice().equals(documentoCollegato.getTipoDocumento().getCodiceGruppo())
				documentoCollegato.addDocumentoSpesaPadre(documentoSpesa);
				dscg.inserisceNotaCreditoEntrata(documentoCollegato);
			} else {*/
				//dscg.inserisceDocumentoEntrata(documentoCollegato, false);  //TODO Valutare ricorsione!!!
//				if(firstRecursion) {
					inserisceSeNecessarioDocumentoEntrataConDocumentiFigliCollegatiESubdocumentiIva(documentoCollegato);
//				}
			//}
			documentoCollegato.getListaDocumentiSpesaPadre().clear(); //rimuovo dall lista per evitare la "infinite recursion in object graph"!!
		}
		
		//#########Spostato nel ciclo di aggiorna stato documento - INIZIO
//		for(SubdocumentoIvaSpesa subdocumentoIvaCollegatoAlDoc : documentoSpesa.getListaSubdocumentoIva(SubdocumentoIvaSpesa.class)) {
//			subdocumentoIvaCollegatoAlDoc.setDocumento(new DocumentoSpesa());
//			subdocumentoIvaCollegatoAlDoc.getDocumento().setUid(documentoSpesa.getUid());
//			dscg.inserisceSubdocumentoIvaSpesa(subdocumentoIvaCollegatoAlDoc);
//		}
//		
//		for(SubdocumentoIvaEntrata subdocumentoIvaCollegatoAlDoc : documentoSpesa.getListaSubdocumentoIva(SubdocumentoIvaEntrata.class)){
//			subdocumentoIvaCollegatoAlDoc.setDocumento(new DocumentoEntrata());
//			subdocumentoIvaCollegatoAlDoc.getDocumento().setUid(documentoSpesa.getUid());
//			dscg.inserisceSubdocumentoIvaEntrata(subdocumentoIvaCollegatoAlDoc);
//		}
		//#########Spostato nel ciclo di aggiorna stato documento - FINE 
		
		/*
		 * eventuale inserimento documento IVA di spesa collegato ed eventuale
		 * NOTA CREDITO IVA (entita' SUBDOCUMENTO IVA SPESA), alla quota
		 * documento di spesa (rilevante IVA) inserita: utilizzare per ogni
		 * subdocumento di spesa l'operazione Inserisce subdocumento IVA spesa
		 * del servizio BIL--SIAC-FIN-SER-203-IVAS004 Servizio Gestione Doc Iva
		 * Spesa, collegando il subdocumento IVA di spesa di tipo NOTA CREDITO
		 * se presente al subdocumento IVA a cui e' associato con il Tipo di
		 * associazione corretto (ad es. NCD per le note credito collegate).
		 */
			
			
		//}
		
		
		return documentoSpesaInserito;
	}
	
	private DocumentoEntrata inserisceSeNecessarioDocumentoEntrataConDocumentiFigliCollegatiESubdocumentiIva(/*Map<String, DocumentoEntrata> documentiEntrataInseritiMap,*/ DocumentoEntrata documentoEntrata) {
	
		//Se il documentono NON e' già stato inserito:
		String keyDocumentoEntrata = computeLogicalKey(documentoEntrata);
		if(!documentiEntrataInseritiMap.containsKey(keyDocumentoEntrata /*documentoEntrata.getUid()*/)){
			DocumentoEntrata documentoEntrataInserito = inserisceDocumentoEntrataConDocumentiFigliCollegatiESubdocumentiIva(documentoEntrata);
			documentiEntrataInseritiMap.put(keyDocumentoEntrata /*documentoEntrataInserito.getUid()*/, documentoEntrataInserito);
			return documentoEntrataInserito;
		} else {
			return documentiEntrataInseritiMap.get(keyDocumentoEntrata);
		}
	}
	
	private DocumentoEntrata inserisceDocumentoEntrataConDocumentiFigliCollegatiESubdocumentiIva(DocumentoEntrata documentoEntrata) {
		final String methodName = "inserisceDocumentoEntrataConDocumentiFigliCollegatiESubdocumentiIva";
		
		gestisciNumerazioneAutomaticaDocumento(documentoEntrata);
		
		TipoDocumento tipoDocumento = documentoEntrata.getTipoDocumento();
		InserisceDocumentoEntrataResponse resIDS;
		if(TipoFamigliaDocumento.ENTRATA.equals(tipoDocumento.getTipoFamigliaDocumento())) {
			RicercaPuntualeDocumentoEntrataResponse resRPDS = ricercaDocumentoGiaPresente(documentoEntrata);
			if(resRPDS.getDocumentoEntrata()==null){ //Il documento non esiste. Lo inserisco.
				resIDS = dscg.inserisceDocumentoEntrata(documentoEntrata, false, false);
			} else {//Il documento esisteva già...
				resIDS = new InserisceDocumentoEntrataResponse();
				resIDS.setDocumentoEntrata(resRPDS.getDocumentoEntrata());
				resIDS.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("Documento Entrata", documentoEntrata.getDescAnnoNumeroTipoDoc()));
			}
			counter.incDocEntrata();
		} else if(TipoFamigliaDocumento.IVA_ENTRATA.equals(tipoDocumento.getTipoFamigliaDocumento())) {
			DocumentoEntrata documentoTrovato = ricercaDocumentoTestataIVAEntrataGiaPresente(documentoEntrata);
			if(documentoTrovato==null){//Il documento non esiste. Lo inserisco.
				resIDS = dscg.inserisceTestataDocumentoEntrata(documentoEntrata, true, false);
			} else {//Il documento esisteva già...
				resIDS = new InserisceDocumentoEntrataResponse();
				resIDS.setDocumentoEntrata(documentoTrovato);
				resIDS.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("Testata IVA Entrata", documentoEntrata.getDescAnnoNumeroTipoDoc()));
			}
			counter.incTestataIVAEntrata();
		} else {
			throw new IllegalArgumentException("Il TipoFamigliaDocumento specificato deve essere di tipo "
							+ TipoFamigliaDocumento.ENTRATA +" o "
							+ TipoFamigliaDocumento.IVA_ENTRATA +" per il documento: "
							+ documentoEntrata.getDescAnnoNumeroTipoDoc() 
							+ "[valore passato: "+tipoDocumento.getTipoFamigliaDocumento()+"] ");
		}
		
		DocumentoEntrata documentoEntrataInserito = resIDS.getDocumentoEntrata();
		documentoEntrata.setUid(documentoEntrataInserito.getUid());
		
		if(resIDS.verificatoErrore(ErroreCore.ENTITA_PRESENTE)){
			/*Se il documento è stato trovato non si effettua alcuna operazione e 
			 * si prosegue l'elaborazione al punto successivo (non si può re-inserire ma nemmeno aggiornare in quanto potrebbe 
			 * già essere stato modificato dagli operatori della ragioneria).*/
			//return documentoEntrataInserito;
			log.debug(methodName, "Il documentoEntrata "+documentoEntrata.getDescAnnoNumeroTipoDoc()+" [uid:"+ documentoEntrata.getUid() + "] è già presente in archivio.");
			
			log.debug(methodName, "Aggiornato DocumentoEntrata "+documentoEntrataInserito.getDescAnnoNumeroTipoDoc()+" con uid: "+documentoEntrataInserito.getUid());
			res.addMessaggio("DOCE-AGG", "Aggiornato documento Entrata "+documentoEntrataInserito.getDescAnnoNumeroTipoDoc());
	
		} else {
			log.debug(methodName, "Inserito DocumentoEntrata "+documentoEntrataInserito.getDescAnnoNumeroTipoDoc()+" con uid: "+documentoEntrataInserito.getUid());
			res.addMessaggio("DOCE-INS", "Inserito documento Entrata "+documentoEntrataInserito.getDescAnnoNumeroTipoDoc());
		}
		
		for(DocumentoSpesa documentoCollegato : documentoEntrata.getListaDocumentiSpesaFiglio()){
			log.debug(methodName, "tipoRelazione: " + documentoCollegato.getTipoRelazione());
			documentoCollegato.addDocumentoEntrataPadre(documentoEntrata);
			
			//Le note credito di un documento di entrata saranno solo notecredito entrata!
			/*if(documentoCollegato.getTipoRelazione().equals(TipoRelazione.NOTA_CREDITO)){  //TipoGruppoDocumento.NOTA_DI_CREDITO.getCodice().equals(documentoCollegato.getTipoDocumento().getCodiceGruppo())
				dscg.inserisceNotaCreditoSpesa(documentoCollegato);
			} else {*/
				//dscg.inserisceDocumentoSpesa(documentoCollegato); //TODO Valutare Ricorsione!! e fare molta attenzione all'inifinte deep object graph!!!
			inserisceSeNecessarioDocumentoSpesaConDocumentiFigliCollegatiESubdocumentiIva(documentoCollegato);
			//}
				
			documentoCollegato.getListaDocumentiEntrataPadre().clear();
				
		} 
		
		for(DocumentoEntrata documentoCollegato : documentoEntrata.getListaDocumentiEntrataFiglio()){
			log.debug(methodName, "tipoRelazione: " + documentoCollegato.getTipoRelazione());
			documentoCollegato.addDocumentoEntrataPadre(documentoEntrata);
			if(documentoCollegato.getTipoRelazione().equals(TipoRelazione.NOTA_CREDITO)  /*TipoGruppoDocumento.NOTA_DI_CREDITO.getCodice().equals(documentoCollegato.getTipoDocumento().getCodiceGruppo())*/){
				dscg.inserisceNotaCreditoEntrata(documentoCollegato);
			} else {
				dscg.inserisceDocumentoEntrata(documentoCollegato);
			}
			documentoCollegato.getListaDocumentiEntrataPadre().clear();
		}
		
		
		//#########Spostato nel ciclo di aggiorna stato documento - INIZIO
//		for(SubdocumentoIvaEntrata subdocumentoIvaCollegatoAlDoc : documentoEntrata.getListaSubdocumentoIva(SubdocumentoIvaEntrata.class)) {
//			subdocumentoIvaCollegatoAlDoc.setDocumento(new DocumentoEntrata());
//			subdocumentoIvaCollegatoAlDoc.getDocumento().setUid(documentoEntrataInserito.getUid());
//			dscg.inserisceSubdocumentoIvaEntrata(subdocumentoIvaCollegatoAlDoc);
//		}
//		
//		for(SubdocumentoIvaSpesa subdocumentoIvaCollegatoAlDoc : documentoEntrata.getListaSubdocumentoIva(SubdocumentoIvaSpesa.class)) {
//			subdocumentoIvaCollegatoAlDoc.setDocumento(new DocumentoSpesa());
//			subdocumentoIvaCollegatoAlDoc.getDocumento().setUid(documentoEntrata.getUid());
//			dscg.inserisceSubdocumentoIvaSpesa(subdocumentoIvaCollegatoAlDoc);
//		}
		//#########Spostato nel ciclo di aggiorna stato documento - FINE
		
		return documentoEntrataInserito;
	}
	
	/**
	 * Genera il numero documento se non specificato e se il tipoDocumento e configurato per supportare la numerazione automatica.
	 *
	 * @param documento the documento
	 */
	private void gestisciNumerazioneAutomaticaDocumento(Documento<?,?> documento) {
		final String methodName = "gestisciNumerazioneAutomaticaDocumento";
		
		if(documento.getNumero() != null){
			log.debug(methodName, "Il documento ha già il numero: "+ documento.getDescAnnoNumeroTipoDoc() + " Esco.");
			return;
		}
		
		//Il documento è stato passato senza il numero. Controllo se il suo TipoDocumento supporta la numerazione automatica.
		if(!Boolean.TRUE.equals(documento.getTipoDocumento().getFlagSenzaNumero())) {
			throw new BusinessException("Il TipoDocumento con codice "
						+ (documento.getTipoDocumento()!=null?documento.getTipoDocumento().getCodice() + " [uid: "+documento.getTipoDocumento().getUid()+"]":"null")
						+" non supporta la numerazione automatica. Bisogna specificare il numero o cambiare il flagSenzaNumero del TipoDocumento.");
		}
		
		String numeroDocumento = documentoDad.staccaNumeroDocumento(documento.getAnno(),documento.getTipoDocumento().getUid());
		//String numeroDocumento = "SIAC" + new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date()); //Alternativa a staccaNumeroDocumento
		
		documento.setNumero(numeroDocumento);
		log.debug(methodName, "Numerazione automatica per il documento: "+ documento.getDescAnnoNumeroTipoDoc() + ".");
	}
	
	
	
	/*private void gestioneDocumentoSpesaGiaPresente(DocumentoSpesa documentoSpesa, InserisceDocumentoSpesaResponse resIDS) {
		if(resIDS.verificatoErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO)){ 
			//se non sono stati passati parametri verifico che il documento esista gia' in archivio.
			
			RicercaPuntualeDocumentoSpesaResponse resRP = ricercaDocumentoGiaPresente(documentoSpesa);
			if(resRP.getDocumentoSpesa()==null) {
				//Il documento non esiste e non sono stati passati i parametri
				for(Errore errore : resIDS.getErrori()){
					if(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getCodice().equals(errore.getCodice())){
						throw new BusinessException(errore, Esito.FALLIMENTO, false);
					}
				}
				//Qui non ci dovrebbe mai arrivare! ma metto comunqe una eccezione di fallback.
				throw new BusinessException("Documento specificato non esistente: "+documentoSpesa.getDescAnnoNumeroTipoDoc(), Esito.FALLIMENTO);
			}
			resIDS.setDocumentoSpesa(resRP.getDocumentoSpesa());
			resIDS.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("Documento spesa", documentoSpesa.getDescAnnoNumeroTipoDoc()));
		}
	}*/

	private RicercaPuntualeDocumentoSpesaResponse ricercaDocumentoGiaPresente(DocumentoSpesa documentoSpesa) {
		DocumentoSpesa docDaCercare = new DocumentoSpesa();
		docDaCercare.setAnno(documentoSpesa.getAnno());
		docDaCercare.setNumero(documentoSpesa.getNumero());
		docDaCercare.setTipoDocumento(documentoSpesa.getTipoDocumento());
		docDaCercare.setStatoOperativoDocumento(null);
		docDaCercare.setSoggetto(documentoSpesa.getSoggetto());
		docDaCercare.setEnte(ente);
		
		RicercaPuntualeDocumentoSpesaResponse resRP = dscg.ricercaPuntualeDocumentoSpesa(docDaCercare, StatoOperativoDocumento.ANNULLATO);
		return resRP;
	}
	
	private DocumentoSpesa ricercaDocumentoTestataIVAGiaPresente(DocumentoSpesa documentoSpesa) {
		DocumentoSpesa docDaCercare = new DocumentoSpesa();
		docDaCercare.setAnno(documentoSpesa.getAnno());
		docDaCercare.setNumero(documentoSpesa.getNumero());
		docDaCercare.setTipoDocumento(documentoSpesa.getTipoDocumento());
		docDaCercare.setStatoOperativoDocumento(null);
		docDaCercare.setSoggetto(documentoSpesa.getSoggetto());
		docDaCercare.setEnte(ente);
		
		DocumentoSpesa docTrovato = documentoSpesaDad.ricercaPuntualeDocumentoIvaSpesa(docDaCercare, StatoOperativoDocumento.ANNULLATO);
		return docTrovato;
	}
	
	/*private void gestioneTestataIvaSpesaGiaPresente(DocumentoSpesa documentoSpesa, InserisceDocumentoSpesaResponse resIDS) {
		
		if(resIDS.verificatoErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO)){
			//se non sono stati passati parametri verifico che il documento esista gia' in archivio.
			
			DocumentoSpesa docTrovato = ricercaDocumentoTestataIVAGiaPresente(documentoSpesa);
			
			if(docTrovato==null){
				for(Errore errore : resIDS.getErrori()){
					if(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getCodice().equals(errore.getCodice())){
						throw new BusinessException(errore, Esito.FALLIMENTO, false);
					}
				}
				//Qui non ci dovrebbe mai arrivare! ma metto comunqe una eccezione di fallback.
				throw new BusinessException("Documento specificato non esistente: "+documentoSpesa.getDescAnnoNumeroTipoDoc(), Esito.FALLIMENTO);
			}
			
			resIDS.setDocumentoSpesa(docTrovato);
			resIDS.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("Testata IVA spesa", documentoSpesa.getDescAnnoNumeroTipoDoc()));
		}
	}*/


	
	
/*	private void gestioneDocumentoEntrataGiaPresente(DocumentoEntrata documentoEntrata, InserisceDocumentoEntrataResponse resIDS) {
		if(resIDS.verificatoErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO)){
			//se non sono stati passati parametri verifico che il documento esista gia' in archivio.
			
			RicercaPuntualeDocumentoEntrataResponse resRP = ricercaDocumentoGiaPresente(documentoEntrata);
			if(resRP.getDocumentoEntrata()==null) {
				//Il documento non esiste e non sono stati passati i parametri
				for(Errore errore : resIDS.getErrori()){
					if(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getCodice().equals(errore.getCodice())){
						throw new BusinessException(errore, Esito.FALLIMENTO, false);
					}
				}
				//Qui non ci dovrebbe mai arrivare! ma metto comunqe una eccezione di fallback.
				throw new BusinessException("Documento specificato non esistente: "+documentoEntrata.getDescAnnoNumeroTipoDoc(), Esito.FALLIMENTO);
			}
			resIDS.setDocumentoEntrata(resRP.getDocumentoEntrata());
			resIDS.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("Documento Entrata", documentoEntrata.getDescAnnoNumeroTipoDoc()));
		}
	}*/

	private RicercaPuntualeDocumentoEntrataResponse ricercaDocumentoGiaPresente(DocumentoEntrata documentoEntrata) {
		DocumentoEntrata docDaCercare = new DocumentoEntrata();
		docDaCercare.setAnno(documentoEntrata.getAnno());
		docDaCercare.setNumero(documentoEntrata.getNumero());
		docDaCercare.setTipoDocumento(documentoEntrata.getTipoDocumento());
		docDaCercare.setStatoOperativoDocumento(null);
		docDaCercare.setSoggetto(documentoEntrata.getSoggetto());
		docDaCercare.setEnte(ente);
		
		RicercaPuntualeDocumentoEntrataResponse resRP = dscg.ricercaPuntualeDocumentoEntrata(docDaCercare, StatoOperativoDocumento.ANNULLATO);
		return resRP;
	}
	
	/*private void gestioneTestataIvaEntrataGiaPresente(DocumentoEntrata documentoEntrata, InserisceDocumentoEntrataResponse resIDS) {
		
		if(resIDS.verificatoErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO)){
			//se non sono stati passati parametri verifico che il documento esista gia' in archivio.
			
			DocumentoEntrata docTrovato = ricercaDocumentoTestataIVAEntrataGiaPresente(documentoEntrata);
			
			if(docTrovato==null){
				for(Errore errore : resIDS.getErrori()){
					if(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getCodice().equals(errore.getCodice())){
						throw new BusinessException(errore, Esito.FALLIMENTO, false);
					}
				}
				//Qui non ci dovrebbe mai arrivare! ma metto comunqe una eccezione di fallback.
				throw new BusinessException("Documento specificato non esistente: "+documentoEntrata.getDescAnnoNumeroTipoDoc(), Esito.FALLIMENTO);
			}
			
			resIDS.setDocumentoEntrata(docTrovato);
			resIDS.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("Testata IVA Entrata", documentoEntrata.getDescAnnoNumeroTipoDoc()));
		}
	}*/

	private DocumentoEntrata ricercaDocumentoTestataIVAEntrataGiaPresente(DocumentoEntrata documentoEntrata) {
		DocumentoEntrata docDaCercare = new DocumentoEntrata();
		docDaCercare.setAnno(documentoEntrata.getAnno());
		docDaCercare.setNumero(documentoEntrata.getNumero());
		docDaCercare.setTipoDocumento(documentoEntrata.getTipoDocumento());
		docDaCercare.setStatoOperativoDocumento(null);
		docDaCercare.setSoggetto(documentoEntrata.getSoggetto());
		docDaCercare.setEnte(ente);
		
		DocumentoEntrata docTrovato = documentoEntrataDad.ricercaPuntualeDocumentoIvaEntrata(docDaCercare, StatoOperativoDocumento.ANNULLATO);
		return docTrovato;
	}
	
	
	/**
	 * Utilizzata per comporre il messaggio di riepilogo delle entita elaborate.
	 */
	private static class Counter {
		//Totale elenchi
		int totElenchi = 0;
		//Totale documenti di spesa
		int totDocSpesa = 0;
		//Totale documenti di entrata
		int totDocEntrata = 0;
		//Totale quote documenti di spesa
		int totQuoteSpesa = 0;
		//Totale quote documenti di entrata
		int totQuoteEntrata = 0;
		//Totale allegati atto
		int totAllegatiAtto = 0;
		//Totale provvedimenti di tipo ‘ALG’
		int totAttoAmministrativiALG = 0;
		//Totale quote entrata derivanti da un provvisorio di cassa.
		int totQuoteEntrataProvvCassa = 0;
		//Totale testate IVA di spesa
		int totTestataIVASpesa = 0;
		//Totale testate IVA di entrata
		int totTestataIVAEntrata = 0;
		
		
		public Counter() {
		}

		void incElenchi() {
			totElenchi++;
		}
		
		void incDocSpesa() {
			totDocSpesa++;
		}
		
		void incDocEntrata() {
			totDocEntrata++;
		}
		
		void incQuoteSpesa() {
			totQuoteSpesa++;
		}
		
		void incQuoteEntrata() {
			totQuoteEntrata++;
		}
		
		void incQuoteEntrataProvvCassa() {
			totQuoteEntrataProvvCassa++;
		}
		
		void incAllegatiAtto() {
			totAllegatiAtto++;
		}
		
		void incAttoAmministrativiAlg() {
			totAttoAmministrativiALG++;
		}
		
		void incTestataIVASpesa() {
			totTestataIVASpesa++;
		}
		
		void incTestataIVAEntrata() {
			totTestataIVAEntrata++;
		}
	
		@Override
		public String toString() {
			
			String msgRiepilogoFormat = "Elaborati: {0,choice,0#nessun elenco|1#un elenco|1<{0, number} elenchi}, "
					+ "{1,choice,0#nessun documento|1#un documento|1<{1, number} documenti} di spesa{2,choice,0#|1# (una quota)|1< ({2, number} quote)}, "
					+ "{3,choice,0#nessun documento|1#un documento|1<{3, number} documenti} di entrata{4,choice,0#|1# (una quota)|1< ({4, number} quote)}, "
					+ "{5,choice,0#nessun allegato atto|1#un allegato atto|1<{5, number} allegati atto}, "
					+ "{6,choice,0#nessun atto amministrativo|1#un atto amministrativo|1<{6, number} atti amministrativi}. ";
			
			String msgRiepilogo = MessageFormat.format(msgRiepilogoFormat, 
					Integer.valueOf(totElenchi),
					Integer.valueOf(totDocSpesa + totTestataIVASpesa),
					Integer.valueOf(totQuoteSpesa),
					Integer.valueOf(totDocEntrata + totTestataIVAEntrata),
					Integer.valueOf(totQuoteEntrata),
					Integer.valueOf(totAllegatiAtto),
					Integer.valueOf(totAttoAmministrativiALG)
					);
			
			return msgRiepilogo;
		}
	
}

}



