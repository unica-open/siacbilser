/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceDocumentoEntrataService;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.FatturaFELDad;
import it.csi.siac.siacbilser.integration.dad.OnereSpesaDad;
import it.csi.siac.siacbilser.integration.dad.OrdineDad;
import it.csi.siac.siacbilser.integration.dad.RegistroUnicoDad;
import it.csi.siac.siacbilser.integration.dad.TipoOnereDad;
import it.csi.siac.siacbilser.model.TipoCodifica;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Ordine;
import it.csi.siac.siacfin2ser.model.RegistroUnico;
import it.csi.siac.siacfin2ser.model.RitenuteDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipoAnalogico;
import it.csi.siac.sirfelser.model.RiepilogoBeniFEL;
import it.csi.siac.sirfelser.model.StatoAcquisizioneFEL;

/**
 * Inserimento dell'anagrafica del Documento di Spesa .
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class InserisceDocumentoSpesaService extends CrudDocumentoDiSpesaBaseService<InserisceDocumentoSpesa, InserisceDocumentoSpesaResponse> {
	
	private static final String CODICE_TIPO_DOCUMENTO_SIOPE_ANALOGICO = "A";
	private static final String CODICE_TIPO_DOCUMENTO_SIOPE_ANALOGICO_DOC_EQUIVALENTE = "DE";
	
	/** The inserisce quota documento spesa service. */
	@Autowired
	private InserisceQuotaDocumentoSpesaService inserisceQuotaDocumentoSpesaService;
	
	/** The inserisce documento entrata service. */
	private InserisceDocumentoEntrataService inserisceDocumentoEntrataService;	
	
	@Autowired 
	private FatturaFELDad fatturaFELDad;
	
	@Autowired
	private RegistroUnicoDad registroUnicoDad;
	
	@Autowired
	private OrdineDad ordineDad;
	
	@Autowired
	private OnereSpesaDad onereSpesaDad;
	
	@Autowired
	private TipoOnereDad tipoOnereDad;
	
	@Autowired
	private CodificaDad codificaDad;
	
	/** The app ctx. */
	@Autowired
	private ApplicationContext appCtx;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName = "checkServiceParam";
		
		doc = req.getDocumentoSpesa();
		
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		bilancio = req.getBilancio();
		
		checkNotNull(doc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(doc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(doc.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento"));
		//checkNotNull(doc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		checkNotNull(doc.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione documento"));
		
		checkNotNull(doc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo documento"));
		
		checkNotNull(doc.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione documento"));
		
		checkNotNull(doc.getTipoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo documento"));
		checkCondition(doc.getTipoDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid tipo documento"));
		
//		checkNotNull(doc.getCodiceBollo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice bollo documento"));
//		checkCondition(doc.getCodiceBollo().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid codice bollo documento"));
		
		//checkNotNull(doc.getFlagBeneficiarioMultiplo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag beneficiario multiplo"));
		if(doc.getFlagBeneficiarioMultiplo()==null) { //NON obbligatorio! Default a FALSE
			doc.setFlagBeneficiarioMultiplo(Boolean.FALSE);
		}
		
		checkNotNull(doc.getSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto documento"));
		checkCondition(doc.getSoggetto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto"));
		
		try {
			checkNotNull(doc.getListaSubdocumenti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti del documento"));
			checkCondition(doc.getListaSubdocumenti().size() == 1, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti (deve esserci un solo subdocumento) del documento"));
		} catch (ServiceParamError spe) { // Se il subdocumento non viene passato 
											// ne viene inizializzato uno vuoto
			log.info(methodName, "Subdocumento passato come parametro non presente o ignorato (deve esserci un solo subdocumento)! Verrà creato un subdocumento che copre l'intero importo del documento.");
			SubdocumentoSpesa subdoc = new SubdocumentoSpesa();
			List<SubdocumentoSpesa> listaSubdocumenti = new ArrayList<SubdocumentoSpesa>();
			listaSubdocumenti.add(subdoc);
			doc.setListaSubdocumenti(listaSubdocumenti);
		}
		
		if(doc.getOrdini() != null){
			for(Ordine o : doc.getOrdini()){
				checkCondition(StringUtils.isNotBlank(o.getNumeroOrdine()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero ordine"));
			}
		}
		
		// JIRA-3191
		checkNumero();
		checkImporto();
		checkArrotondamento();
		checkDataEmissione();
		checkDataScadenza();
		checkDataSospensione();
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		documentoSpesaDad.setEnte(doc.getEnte());
		fatturaFELDad.setLoginOperazione(loginOperazione);
		fatturaFELDad.setEnte(ente);
		registroUnicoDad.setLoginOperazione(loginOperazione);
		registroUnicoDad.setEnte(ente);
		ordineDad.setLoginOperazione(loginOperazione);
		ordineDad.setEnte(ente);
		onereSpesaDad.setLoginOperazione(loginOperazione);
		onereSpesaDad.setEnte(ente);
		tipoOnereDad.setLoginOperazione(loginOperazione);
		tipoOnereDad.setEnte(ente);
		codificaDad.setEnte(ente);
		
		inserisceDocumentoEntrataService = appCtx.getBean("inserisceDocumentoEntrataService", InserisceDocumentoEntrataService.class);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public InserisceDocumentoSpesaResponse executeService(InserisceDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		caricaBilancio();
		checkDocumentoGiaEsistente();
		checkSoggetto();
		checkAnno();
		caricaTipoDocumento();
		
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);
		
		impostaNumeroRegistroUnico();
		
		// SIAC-5311 SIOPE+
		// Gestione dei default
		gestioneDefaultSiope();
		
		documentoSpesaDad.inserisciAnagraficaDocumentoSpesa(doc);
		//TransactionAspectSupport.currentTransactionStatus().flush();
		
		if(req.isInserisciQuotaContestuale()) { 
			//Ad esempio il servizio DefiniscePreDocumentoDiSpesaService salta l'inserimento contestuale della quota.
		
			SubdocumentoSpesa subdocumentoSpesa = doc.getListaSubdocumenti().get(0);
			
			//Imposto lo stesso importo del documento anche sulla singola quota della nota credito.
			subdocumentoSpesa.setImporto(doc.getImportoNetto());
			subdocumentoSpesa.setDescrizione(doc.getDescrizione());
			subdocumentoSpesa.setDataScadenza(doc.getDataScadenza());
			
			subdocumentoSpesa = inserisceQuotaDocumentoSpesa(subdocumentoSpesa);
		
		
			doc.getListaSubdocumenti().set(0,subdocumentoSpesa);
		
		}
		
		gestisciFatturaFEL();
		
		gestisciFlagRegolarizzazione();
		checkTipoDocumentoConFlagSubordinato();
		gestisciFlagAggiornaQuoteDaElenco();
		gestisciOrdini();
		
		res.setDocumentoSpesa(doc);
		
		
	}

	private void gestisciOrdini() {
		if(doc.getOrdini() != null){
			for(Ordine ordine : doc.getOrdini()){
				DocumentoSpesa documento = new DocumentoSpesa();
				documento.setUid(doc.getUid());
				ordine.setDocumento(documento);
				ordine = ordineDad.inserisceOrdine(ordine);
			}
		}
	}

	/**
	 * Stacca il numero di {@link RegistroUnico} per il documento e lo imposta nell'oggetto {@link #doc}
	 */
	private void impostaNumeroRegistroUnico() {
		final String methodName = "impostaNumeroRegistroUnico";
		if(!Boolean.TRUE.equals(doc.getTipoDocumento().getFlagRegistroUnico())) {
			log.debug(methodName, "Il tipo documento " + doc.getTipoDocumento().getCodice() + " per l'ente " + ente.getUid() + " ha il flagRegistroUnico non a TRUE: registro unico non da impostare");
			// Per sicurezza
			doc.setRegistroUnico(null);
			return;
		}
		
		Integer numeroRegistroUnico = registroUnicoDad.staccaNumeroRegistroUnico(bilancio.getAnno());
		log.debug(methodName, "numeroRegistroUnico: "+numeroRegistroUnico + " anno: "+ bilancio.getAnno());
		
		RegistroUnico registroUnico = new RegistroUnico();
		registroUnico.setAnno(bilancio.getAnno());
		registroUnico.setNumero(numeroRegistroUnico);
		registroUnico.setDateRegistro(new Date());
		doc.setRegistroUnico(registroUnico);
	}
	
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
		
		if(doc.getSiopeDocumentoTipo() == null || doc.getSiopeDocumentoTipo().getUid() == 0) {
			// Default per il tipo documento siope
			log.debug(methodName, "Tipo documento siope non valorizzato: impostazione del default ANALOGICO");
			SiopeDocumentoTipo siopeDocumentoTipo = codificaDad.ricercaCodifica(new TipoCodifica(SiopeDocumentoTipo.class), CODICE_TIPO_DOCUMENTO_SIOPE_ANALOGICO);
			doc.setSiopeDocumentoTipo(siopeDocumentoTipo);
		} else if(StringUtils.isBlank(doc.getSiopeDocumentoTipo().getCodice())) {
			SiopeDocumentoTipo siopeDocumentoTipo = codificaDad.ricercaCodifica(SiopeDocumentoTipo.class, doc.getSiopeDocumentoTipo().getUid());
			doc.setSiopeDocumentoTipo(siopeDocumentoTipo);
		}
		
		if(CODICE_TIPO_DOCUMENTO_SIOPE_ANALOGICO.equals(doc.getSiopeDocumentoTipo().getCodice()) && (doc.getSiopeDocumentoTipoAnalogico() == null || doc.getSiopeDocumentoTipoAnalogico().getUid() == 0)) {
			// Default per il tipo documento analogico siope
			log.debug(methodName, "Tipo documento analogico siope non valorizzato: impostazione del default DOC_EQUIVALENTE");
			SiopeDocumentoTipoAnalogico siopeDocumentoTipoAnalogico = codificaDad.ricercaCodifica(new TipoCodifica(SiopeDocumentoTipoAnalogico.class), CODICE_TIPO_DOCUMENTO_SIOPE_ANALOGICO_DOC_EQUIVALENTE);
			doc.setSiopeDocumentoTipoAnalogico(siopeDocumentoTipoAnalogico);
		}
	}
	
	private void gestisciFatturaFEL() {
		final String methodName = "gestisciFatturaFEL";
		
		if(doc.getFatturaFEL() != null && doc.getFatturaFEL().getIdFattura() != null){
			log.debug(methodName, "Inserisco il legame tra il documento[uid:"+doc.getUid()+"] e la fatturaFEL[idFattura:"+doc.getFatturaFEL().getIdFattura()+"]");
			
			fatturaFELDad.inserisciRelazioneDocumentoFattura(doc, doc.getFatturaFEL());
			fatturaFELDad.aggiornaStatoFattura(doc.getFatturaFEL(), StatoAcquisizioneFEL.IMPORTATA);
			fatturaFELDad.impostaDataCaricamentoFattura(doc.getFatturaFEL(), new Date());
			elaboraDettagliOnereDocumento();
		}
	}

	/**
	 * CR 2661
	 * Inserire per ogni record presente nell'entit&agrave; Riepilogo Beni un ONERE in elenco 
	 * (con i dati preimpostati indicati nelle celle sottostanti) se vengono soddisfatte le seguenti condizioni:
	 * <ul>
	 *     <li>Il codice presente nel campo Riepilogo Beni.Aliquota IVA (che &eacute; una percentuale) deve essere
	 *     uguale ad un unico valore percentuale presente tra le aliquote dei codici di ONERE che hanno Natura=SPLIT/REVERSE
	 *     e Tipo Split/Reverse=Split Istituzionale del sistema contabile affinch&eacute; si possa identificare univocamente
	 *     l'onere da inserire;</li>
	 *     <li>Ogni record con i dati preimpostati come indicato nelle celle a seguire deve soddisfare i controlli descritti al par.2.5.3 </li>
	 * </ul>
	 * Se queste condizioni non fossero soddisfatte il record e quindi l'ONERE non dovr&agrave; essere inserito nell'elenco
	 * ed il sistema dopo aver verificato-inserito tutti i records presenti in Riepilogo Beni dovr&agrave; visualizzare
	 * il messaggio cos&iacute; modificato
	 * <br/>
	 * <code>&lt;FIN_INF_0284, Dati inconsistenti da documento FEL, 'impossibile completare la tabella degli oneri da documento FEL'&gt;</code>
	 */
	private void elaboraDettagliOnereDocumento() {
		if(doc.getFatturaFEL().getRiepiloghiBeni() == null || doc.getFatturaFEL().getRiepiloghiBeni().isEmpty()){
			// la fattura fel non ha niente che possa essere trasformato in onere. esco.
			return;
		}
		RitenuteDocumento ritenute = new RitenuteDocumento();
		boolean listaOnereCompleta = true;
		for(RiepilogoBeniFEL riepilogoBeniFEL : doc.getFatturaFEL().getRiepiloghiBeni()){
			if(!"S".equals(riepilogoBeniFEL.getEsigibilitaIva())){
				log.debug("elaboraDettagliOnereDocumento", "non inserisco nessun onere, il tipo esigibilita' e' di tipo " + riepilogoBeniFEL.getEsigibilitaIva());
				continue;
			}
			List<TipoOnere> listaTipoOnere = tipoOnereDad.identificaDettaglioOnerePerAliquotaFatturaFEL(riepilogoBeniFEL.getAliquotaIva());
			if(listaTipoOnere == null || listaTipoOnere.size() != 1){
				//non ho una lista, oppure la lista non e' valida. proseguo con il prossimo dettaglio onere.
				listaOnereCompleta = false;
				continue;
			}
			DettaglioOnere dettaglioOnere = popolaDettaglioOnere(listaTipoOnere.get(0), riepilogoBeniFEL);
			//inserisco l'onere su db
			onereSpesaDad.inserisciAnagraficaDettaglioOnere(dettaglioOnere);
			ritenute.getListaOnere().add(dettaglioOnere);
		}
		// inserisco le ritenute mappate nel documento
		doc.setRitenuteDocumento(ritenute);
		if(!listaOnereCompleta){
			//la lista onere non e' ben valorizzata:
			res.addMessaggio(ErroreFin.DATI_INCOSTISTENTI_DA_DOCUMENTO_FEL.getErrore("degli oneri").getCodice(),ErroreFin.DATI_INCOSTISTENTI_DA_DOCUMENTO_FEL.getErrore("degli oneri").getDescrizione());
		}
		
	}

	private DettaglioOnere popolaDettaglioOnere(TipoOnere tipoOnere, RiepilogoBeniFEL riepilogoBeniFEL) {
		//creo il dettaglio onere
		DettaglioOnere dettaglioOnere = new DettaglioOnere();
		dettaglioOnere.setTipoOnere(tipoOnere);
		dettaglioOnere.setImportoImponibile(BilUtilities.arrotondaAllaSecondaCifra(riepilogoBeniFEL.getImponibileImporto()));
		dettaglioOnere.setImportoCaricoSoggetto(BilUtilities.arrotondaAllaSecondaCifra(riepilogoBeniFEL.getImpostaNotNull().add(riepilogoBeniFEL.getArrotondamentoNotNull())));
		//creo il legame con il documento
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(doc.getUid());
		dettaglioOnere.setDocumentoSpesa(documentoSpesa);
		dettaglioOnere.setEnte(ente);
		return dettaglioOnere;
	}

	private void gestisciFlagAggiornaQuoteDaElenco() {
		// TODO Auto-generated method stub
		
		//doc.getTipoDocumento().getFlagAggiornaQuoteDaElenco() ???
	}

	private void checkTipoDocumentoConFlagSubordinato() {
		/* TODO	Se il tipo documento che si sta inserendo ha valorizzato flagSubordinato = true 
		 * verificare che l'importo del documento non superi l'importo del documento originario, 
		 * altrimenti inviare il messaggio di errore bloccante 
		 * <COR_ERR_0031 Aggiornamento non possibile (<entità>:  Il documento ,  <operazione> : di tipo subordinato, 
		 * l'importo non può superare l'importo del documento origine)>.
		 */
		
		//doc.getTipoDocumento().getFlagSubordinato()
	}

	/**
	 * Gestisce, se necessario, l'inserimento automatico del documento di regolarizzazione.
	 * 
	 */
	protected void gestisciFlagRegolarizzazione() {		
		if(req.isInserisciDocumentoRegolarizzazione() && Boolean.TRUE.equals(doc.getTipoDocumento().getFlagRegolarizzazione())) {
			
			InserisceDocumentoEntrata reqIDS = new InserisceDocumentoEntrata();
			reqIDS.setRichiedente(req.getRichiedente());
			reqIDS.setBilancio(req.getBilancio());
			reqIDS.setInserisciDocumentoRegolarizzazione(false);			
			
			DocumentoEntrata documentoEntrata = creaDocumentoEntrataEquivalente();
			
			reqIDS.setDocumentoEntrata(documentoEntrata);
			executeExternalServiceSuccess(inserisceDocumentoEntrataService, reqIDS);
		}
	}

	/**
	 * Crea documento entrata equivalente.
	 *
	 * @return the documento entrata
	 */
	private DocumentoEntrata creaDocumentoEntrataEquivalente() {
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setAnno(doc.getAnno());
		documentoEntrata.setArrotondamento(doc.getArrotondamento());
		documentoEntrata.setCodiceBollo(doc.getCodiceBollo());			
		documentoEntrata.setDataEmissione(doc.getDataEmissione());
		documentoEntrata.setDataRepertorio(doc.getDataRepertorio());
		documentoEntrata.setDataScadenza(doc.getDataScadenza());
		documentoEntrata.setDescrizione(doc.getDescrizione());
		documentoEntrata.setEnte(doc.getEnte());
		documentoEntrata.setImporto(doc.getImporto());
		documentoEntrata.setNote(doc.getNote());
		documentoEntrata.setNumero(doc.getNumero()); 
		documentoEntrata.setNumeroRepertorio(doc.getNumeroRepertorio());
		documentoEntrata.setSoggetto(doc.getSoggetto());
		documentoEntrata.setStatoOperativoDocumento(doc.getStatoOperativoDocumento());
		documentoEntrata.setTerminePagamento(doc.getTerminePagamento());
		
		TipoDocumento tipoDocumentoEntrata = documentoSpesaDad.findTipoDocumentoByCodiceEFamiglia(doc.getTipoDocumento().getCodice(), TipoFamigliaDocumento.ENTRATA);			
		documentoEntrata.setTipoDocumento(tipoDocumentoEntrata);
		
		documentoEntrata.setTipoRelazione(TipoRelazione.SUBORDINATO);
		documentoEntrata.addDocumentoSpesaPadre(doc);
		return documentoEntrata;
	}

	/**
	 * Carica tipo documento.
	 */
	protected void caricaTipoDocumento() {
		TipoDocumento tipoDocumento = documentoSpesaDad.findTipoDocumentoById(doc.getTipoDocumento().getUid());
		doc.setTipoDocumento(tipoDocumento);		
	}
	
	/**
	 * Richiama il servizio di inserimento del subdocumento di spesa.
	 *
	 * @param subdocumentoSpesa the subdocumento spesa
	 * @return il subdocumento inserito.
	 */
	private SubdocumentoSpesa inserisceQuotaDocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		InserisceQuotaDocumentoSpesa reqInsQuota = new InserisceQuotaDocumentoSpesa();
		reqInsQuota.setRichiedente(req.getRichiedente());	
		reqInsQuota.setBilancio(req.getBilancio());
		subdocumentoSpesa.setEnte(doc.getEnte());
		
		DocumentoSpesa d = new DocumentoSpesa();
		d.setUid(doc.getUid());
		subdocumentoSpesa.setDocumento(d);		
		reqInsQuota.setSubdocumentoSpesa(subdocumentoSpesa);	
		
		reqInsQuota.setAggiornaStatoDocumento(false);
		reqInsQuota.setQuotaContestuale(true);
		
		InserisceQuotaDocumentoSpesaResponse resInsQuota = executeExternalServiceSuccess(inserisceQuotaDocumentoSpesaService,reqInsQuota);
		return resInsQuota.getSubdocumentoSpesa();
		
	}
	
}
