/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaPuntualeCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.documento.MovimentoGestioneServiceCallGroup;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.integration.dad.ProvvisorioBilDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciModificaImportoMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciModificaImportoMovimentoGestioneEntrataResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

/**
 * Base service per il crud dei documenti di entrata.
 *
 * @author Domenico
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class CrudDocumentoDiEntrataBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ,RES> {
	
	protected DocumentoEntrata doc;
	protected SubdocumentoEntrata subdoc;
	protected DocumentoEntrata documentoAssociato;
	protected Bilancio bilancio;
	protected CapitoloEntrataGestione capitolo;
	
	//DAD
	@Autowired
	protected DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	protected SubdocumentoEntrataDad subdocumentoEntrataDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	protected SoggettoDad soggettoDad;
	@Autowired
	private ProvvedimentoDad provvedimentoDad;
	@Autowired
	private ProvvisorioBilDad provvisorioBilDad;
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	//Servizi interni.
	@Autowired
	protected AggiornaStatoDocumentoDiEntrataService aggiornaStatoDocumentoDiEntrataService;
	@Autowired
	private RicercaPuntualeCapitoloEntrataGestioneService ricercaPuntualeCapitoloEntrataGestioneService;
	
	//Servizi esterni.
	@Autowired
	private MovimentoGestioneService movimentoGestioneService;
	@Autowired
	private ProvvisorioService provvisorioService;
	
	protected MovimentoGestioneServiceCallGroup mgscg;
	
	//SIAC-6645
	protected boolean gestisciModificaImportoAccertamento = false;
	protected String msgOperazione = "";
	
	@Override
	protected void init() {
		super.init();
		log.debug("init", "inizializzo il movimentogestioneServiceCallGrooup");
		mgscg = new MovimentoGestioneServiceCallGroup(serviceExecutor,req.getRichiedente(),ente, bilancio);
	}
	
	/**
	 * Aggiorna stato operativo documento.
	 *
	 * @param uidDocumento the uid documento
	 * @return the stato operativo documento
	 */
	protected DocumentoEntrata aggiornaStatoOperativoDocumento(Integer uidDocumento) {
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(uidDocumento);
		return aggiornaStatoOperativoDocumento(documentoEntrata);
	}
	

	/**
	 * Aggiorna stato operativo documento.
	 *
	 * @param documentoEntrata the documento entrata
	 * @return the stato operativo documento
	 */
	protected DocumentoEntrata aggiornaStatoOperativoDocumento(DocumentoEntrata documentoEntrata) {
		AggiornaStatoDocumentoDiEntrata reqAs = new AggiornaStatoDocumentoDiEntrata();
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setBilancio(bilancio);
		reqAs.setDocumentoEntrata(documentoEntrata);
		AggiornaStatoDocumentoDiEntrataResponse resAs = serviceExecutor.executeServiceSuccess(aggiornaStatoDocumentoDiEntrataService, reqAs);
		
		return resAs.getDocumentoEntrata();
	}
	
	
	
	/**
	 * soggetto (DocumentoEntrata.Soggetto) deve essere in StatoOperativoAnagrafica (statoOperativo) == VALIDO
	 * tabella siac_r_soggetto_stato (legame siac_t_soggetto e siac_d_soggetto_stato)
	 */
	protected void checkSoggetto() {
		if(doc.getSoggetto()==null || doc.getSoggetto().getUid()==0){
			return;
		}
		StatoOperativoAnagrafica statoOperativoAnagrafica = soggettoDad.findStatoOperativoAnagraficaSoggetto(doc.getSoggetto());
		if (!StatoOperativoAnagrafica.VALIDO.equals(statoOperativoAnagrafica) && !StatoOperativoAnagrafica.SOSPESO.equals(statoOperativoAnagrafica)) {
			throw new BusinessException(ErroreFin.SOGGETTO_NON_VALIDO.getErrore(""), Esito.FALLIMENTO);
		}
	}
	
	/**
	 * anno (DocumentoEntrata.anno) deve essere non maggiore dell'anno del bilancio in cui si sta agendo (non passato)
	 */
	protected void checkAnno() {
		//INFO: al momento non viene passato l'anno di bilancio!  
	}
	
	/**
	 * dataEmissione (DocumentoEntrata.dataEmissione) deve avere campo anno uguale all'anno del documento
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkDataEmissione() throws ServiceParamError {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
//		Integer year = Integer.valueOf(dateFormat.format(doc.getDataEmissione()));
//		
//		checkCondition(year.compareTo(doc.getAnno()) == 0, it.csi.siac.siacfin2ser.model.errore.ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore()); // Possibile refuso di analisi?
	}

	/**
	 * importo (DocumentoEntrata.importo) deve essere positivo
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkImportoPositivo() throws ServiceParamError {
		checkCondition(doc.getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore di zero"));
	}
	
	/**
	 * importo (DocumentoSpesa.importo) deve essere non negativo
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkImporto() throws ServiceParamError {
		checkCondition(doc.getImporto().signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore o uguale a zero"));
	}
	
	/**
	 * arrotondamento (DocumentoEntrata.arrotondamento) se esiste deve essere negativo; inoltre importo + arrotondamento deve essere potitivo
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkArrotondamento() throws ServiceParamError {
		checkCondition(doc.getArrotondamento() == null || doc.getImporto().add(doc.getArrotondamento()).signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Arrotondamento", ": importo sommato ad arrotondamento deve essere maggiore o uguale a zero"));
	}
	
	/**
	 * dataScadenza (DocumentoEntrata.dataScadenza) se esiste deve essere maggiore della data di emissione
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkDataScadenza() throws ServiceParamError {
		checkCondition(doc.getDataScadenza() == null || doc.getDataScadenza().compareTo(doc.getDataEmissione()) >= 0, it.csi.siac.siacfin2ser.model.errore.ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());
	}
	
	protected void caricaBilancio(){
		bilancio = bilancioDad.getBilancioByUid(bilancio.getUid());
	}
	
	protected void caricaAccertamentoESubAccertamento(){
		if(subdoc.getAccertamento()==null || subdoc.getAccertamento().getUid() == 0){
			return;
		}
		
		Accertamento accertamento = ricercaAccertamentoPerChiaveOttimizzato();
		accertamento.setElencoSubAccertamenti(filtraSubAccertamentiDefinitivi(accertamento.getElencoSubAccertamenti()));
		log.debug("caricaAccertamentoESubAccertamento", "trovato accertamento con uid: " + accertamento.getUid() + " e numero: " + accertamento.getNumero());
		subdoc.setAccertamento(accertamento);
		
		if(subdoc.getSubAccertamento()!= null && subdoc.getSubAccertamento().getUid()!=0){
			if(accertamento.getSubAccertamenti() == null){
				throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("subaccertamento", subdoc.getSubAccertamento().getNumero()+""), Esito.FALLIMENTO);
			}else{
				SubAccertamento subAccertamento = null;
				for(SubAccertamento subAcc: accertamento.getSubAccertamenti()){
		    		if(subAcc.getUid()==subdoc.getSubAccertamento().getUid()){
		    			subAccertamento = subAcc;
						log.debug("caricaAccertamentoESubAccertamento", "trovato subaccertamento con uid: " + subAccertamento.getUid() + " e numero: " + subAccertamento.getNumero());
		    		}
		    	}
				if(subAccertamento == null){
					throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("subaccertamento", subdoc.getSubAccertamento().getNumero()+""), Esito.FALLIMENTO);
				}else{
					subdoc.setSubAccertamento(subAccertamento);
				}
			}
		}
		
	}
	
	private List<SubAccertamento> filtraSubAccertamentiDefinitivi(List<SubAccertamento> elencoSubAccertamenti) {
		if(elencoSubAccertamenti == null || elencoSubAccertamenti.isEmpty()){
			return new ArrayList<SubAccertamento>();
		}
		List<SubAccertamento> result = new ArrayList<SubAccertamento>();
		for(SubAccertamento s : elencoSubAccertamenti){
			if(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(s.getStatoOperativoMovimentoGestioneEntrata())){
				result.add(s);
			}
		}
		return result;
	}


	private Accertamento ricercaAccertamentoPerChiaveOttimizzato() {
		
		RicercaAttributiMovimentoGestioneOttimizzato parametri = new RicercaAttributiMovimentoGestioneOttimizzato();
		parametri.setEscludiSubAnnullati(true);
		parametri.setCaricaSub(subdoc.getSubAccertamento()!=null && subdoc.getSubAccertamento().getNumero() != null);
				
		DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		parametriElencoIds.setEscludiAnnullati(true);
		parametri.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class)); //Non richiedo NESSUN importo derivato.
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse resRAPC = mgscg.ricercaAccertamentoPerChiaveOttimizzato(subdoc.getAccertamento(), parametri, datiOpzionaliCapitoli, subdoc.getSubAccertamento());
		log.logXmlTypeObject(resRAPC, "Risposta ottenuta dal servizio RicercaImpegnoPerChiaveOttimizzato.");
		checkServiceResponseFallimento(resRAPC);
		
		Accertamento accertamento = resRAPC.getAccertamento();
		if(accertamento==null) {
			res.addErrori(resRAPC.getErrori());
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", subdoc.getAccertamento().getNumero()+""+ subdoc.getAccertamento().getAnnoMovimento()+""), Esito.FALLIMENTO);
		}
		return accertamento;
	}


	protected void checkAccertamentoSubAccertamento() {
		final String methodName = "checkAccertamentoSubAccertamento";
		// SIAC-5131: nei DOCUMENTI DI ENTRATA anche se il documento è un DSI se l'operatore ha l'azione OP-ENT-PreDocNoModAcc NON PERMETTERE LO SFONDAMENTO DEL MOVIMENTO, ACCERTAMENTO O SUBACCERTAMENTO SELEZIONATO
		//effettuo i controlli sull'accertamento solo se il tipo del doocumento e' diverso da DSI
		if(documentoAssociato.getTipoDocumento().isDisposizioneDiIncasso() && isDSIAbilitatoASfondareAccertamento()){
			return;
		}
		
		Accertamento accertamentoOSubAccertamento = subdoc.getAccertamentoOSubAccertamento();
		if(accertamentoOSubAccertamento==null){
			return;
		}
		checkStatoOperativoDefinitivo(accertamentoOSubAccertamento);
		
		//SIAC-3961
		if(documentoAssociato.getTipoDocumento().isNotaCredito()){
			log.info(methodName, "Quota associata ad un documento di tipo nota credito. Salto il controllo sulla disponibilita' del movimento gestione");
			return;
		}
		
		BigDecimal importoPrecedente = BigDecimal.ZERO;
		Accertamento accertamento = subdoc.getAccertamento();
		SubAccertamento subAccertamento = subdoc.getSubAccertamento();
		
		if(subAccertamento != null && subAccertamento.getUid() != 0){
			Integer uidSubAccertamentoPrecedente = caricaUidSubAccertamentoPrecedente();
			log.debug(methodName, "uidSubAccertamentoPrecedente: " + uidSubAccertamentoPrecedente);
			if(uidSubAccertamentoPrecedente != null && uidSubAccertamentoPrecedente == subAccertamento.getUid()){
				importoPrecedente = caricaImportoDaIncassarePrecedenteQuota();
			}
		}else if(accertamento != null && accertamento.getUid() != 0){
			Integer uidAccertamentoPrecedente = caricaUidAccertamentoPrecedente();
			log.debug(methodName, "uidAccertamentoPrecedente: " + uidAccertamentoPrecedente);
			if(uidAccertamentoPrecedente != null && uidAccertamentoPrecedente == accertamento.getUid()){
				importoPrecedente = caricaImportoDaIncassarePrecedenteQuota();
			}
		}

		log.debug(methodName, "importoPrecedente" + importoPrecedente);
		checkDisponibilitaAccertamentoSubAccertamento(accertamentoOSubAccertamento, subdoc.getImportoDaIncassare(), importoPrecedente);
	}

	/**
	 * Controlla if is possibile sfondare accertamento.
	 *
	 * @return true, if is possibile sfondare accertamento
	 */
	protected boolean isDSIAbilitatoASfondareAccertamento() {
		return !isAzioneConsentita(AzioniConsentite.PREDOCUMENTO_ENTRATA_MODIFICA_ACC_NON_AMMESSA.getNomeAzione());
	}
	
	/**
	 * Restituisce l'impegno o il subimpegno della quota attualmente presenete su DB.
	 * 
	 * @return
	 */
	private Integer caricaUidAccertamentoPrecedente() {
		if(subdoc.getUid() == 0){
			return null;
		}
		return subdocumentoEntrataDad.findUidAccertamentoSubdocumentoEntrataById(subdoc.getUid());
	}
	
	/**
	 * Restituisce l'impegno o il subimpegno della quota attualmente presenete su DB.
	 * 
	 * @return
	 */
	private Integer caricaUidSubAccertamentoPrecedente() {
		if(subdoc.getUid() == 0){
			return null;
		}
		return subdocumentoEntrataDad.findUidSubAccertamentoSubdocumentoEntrataById(subdoc.getUid());
	}
	
	/**
	 * Restituisce l'importo della quota attualmente presenete su DB.
	 * Nel caso di Inserimento di una nuova quota questo metodo viene sovrascritto restituendo 0.
	 * 
	 * @return
	 */
	protected BigDecimal caricaImportoPrecedenteQuota() {
		return subdocumentoEntrataDad.findImportoSubdocumentoEntrataById(subdoc.getUid());
	}
	
	/**
	 * Restituisce l'importo della quota attualmente presenete su DB.
	 * Nel caso di Inserimento di una nuova quota questo metodo viene sovrascritto restituendo 0.
	 * 
	 * @return
	 */
	protected BigDecimal caricaImportoDaIncassarePrecedenteQuota() {
		return subdocumentoEntrataDad.findImportoDaIncassareSubdocumentoEntrataById(subdoc.getUid());
		
	}

	private void checkStatoOperativoDefinitivo(Accertamento accertamento) {
		if(!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata())){
			ErroreFin errore = accertamento instanceof SubAccertamento ? ErroreFin.SUBACCERTAMENTO_NON_IN_STATO_DEFINITIVO : ErroreFin.ACCERTAMENTO_NON_IN_STATO_DEFINITIVO;
			throw new BusinessException(errore.getErrore(""), Esito.FALLIMENTO);
		}
	}
	
	/**
	 * Controlla se è soddisfatatta la seguente condizione:
	 * disponibilità incassare + quotaVecchia.importo >= quotaNuova.importo
	 * 
	 * @param accertamento
	 * @param importoNuovo
	 * @param importoPrecedente
	 */
	private void checkDisponibilitaAccertamentoSubAccertamento(Accertamento accertamento, BigDecimal importoNuovo, BigDecimal importoPrecedente) {
		final String methodName = "checkDisponibilitaAccertamentoSubAccertamento";
		
		BigDecimal disponibilitaIncassare = accertamento.getDisponibilitaIncassare() != null ? accertamento.getDisponibilitaIncassare() : BigDecimal.ZERO;
		importoNuovo = importoNuovo!=null? importoNuovo : BigDecimal.ZERO;
		importoPrecedente = importoPrecedente!=null? importoPrecedente : BigDecimal.ZERO;
		
		boolean isDisponibilitaSufficiente = disponibilitaIncassare.add(importoPrecedente).compareTo(importoNuovo)>=0;
		
		log.info(methodName, "disponibilitaIncassare: " + disponibilitaIncassare + " + importoPrecedente: " + importoPrecedente 
				+ " >= importoNuovo: " + importoNuovo + " -> isDisponibilitaSufficiente: " + isDisponibilitaSufficiente);
		
		if(isDisponibilitaSufficiente) {
			log.debug(methodName, "Disponibilita sufficiente. Non devo inserire modifiche di importo.");
			return;
		}
		
		// SIAC-4310
		gestioneModificaImporto(accertamento, importoPrecedente, disponibilitaIncassare);
		
	}
	
	/**
	 * Calcolo della chiave dell'accertamento e subaccertamento per log.
	 * <br/>
	 * La chiave &eacute; della forma <code>&lt;ANNO&gt;/&lt;NUMERO_IMPEGNO&gt;(-&lt;NUMERO_SUB&gt;)</code>
	 * @param accertamento l'accertamento
	 * @param subaccertamento il subaccertamento
	 * @return la chiave
	 */
	private String calcolaChiaveAccertamento(Accertamento accertamento, SubAccertamento subaccertamento) {
		if(accertamento == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(accertamento.getAnnoMovimento());
		if(accertamento.getNumero() != null) {
			sb.append("/");
			sb.append(accertamento.getNumero().toPlainString());
		}
		
		if(subaccertamento != null && subaccertamento.getNumero() != null) {
			sb.append("-");
			sb.append(subaccertamento.getNumero().toPlainString());
		}
		return sb.toString();
	}
	
	
	protected void caricaAttoAmministrativo(){
		//il provvedimento non e' obbligatorio
    	if (subdoc.getAttoAmministrativo()==null || subdoc.getAttoAmministrativo().getUid()==0){
			return;
		}
    	AttoAmministrativo attoAmministrativo = provvedimentoDad.findProvvedimentoById(subdoc.getAttoAmministrativo().getUid());
    	
    	if (attoAmministrativo==null){
    		throw new BusinessException(ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore(), Esito.FALLIMENTO);
    	}
    	
    	log.debug("caricaAttoAmministrativo", "trovato atto con uid: "+ attoAmministrativo.getUid() + ", numero: " + attoAmministrativo.getNumero() +
   			 " e stato opertativo : " + attoAmministrativo.getStatoOperativoAtti());
    	
    	subdoc.setAttoAmministrativo(attoAmministrativo);
	}
	
	
	protected void checkAttoAmministrativo(){
		if (subdoc.getAttoAmministrativo()==null || subdoc.getAttoAmministrativo().getUid()==0){
			return;
		}
    	
    	if (!StatoOperativoAtti.DEFINITIVO.equals(subdoc.getAttoAmministrativo().getStatoOperativoAtti())){
    		throw new BusinessException(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Gestione quota documento di entrata","definitivo"), Esito.FALLIMENTO);
    	}
    	
//    	controllo sul tipo eliminato in seguito alla CR del 11/03/2015
//    	String codiceTipoDeterminaDiIncasso = provvedimentoDad.getCodiceTipoDeterminaDiIncasso();
//    	
//    	if (!codiceTipoDeterminaDiIncasso.equals(subdoc.getAttoAmministrativo().getTipoAtto().getCodice())){
//    		throw new BusinessException(ErroreFin.TIPO_PROVVEDIMENTO_INCONGRUENTE.getErrore(""), Esito.FALLIMENTO);
//    	}
    	// SIAC-5043
    	if(subdoc.getAttoAmministrativo().getAllegatoAtto()!= null && subdoc.getAttoAmministrativo().getAllegatoAtto().getUid() != 0 && !hasElencoStessoAllegatoAtto()){
			throw new BusinessException(ErroreFin.ATTO_GIA_ABBINATO.getErrore(), Esito.FALLIMENTO);
    	}
    	
    	/*
    	 * Inoltre il provvedimento non deve avere uno stato in convalida uguale a CONVALIDATO, ANNULLATO o RIFIUTATO, 
    	 * altrimenti si visualizza il messaggio <FIN_ERR_0075, Stato Provvedimento non consentito, ‘Gestione quota documento di entrata’,
    	 *  ‘NON CONVALIDATO-NON RIFIUTATO-NON ANNULLATO’>.
    	 */
    	
    
	}
	
	/**
	 * Controllo se vi sia un elenco dello stesso allegato atto associato alla quota
	 * @return
	 */
	private boolean hasElencoStessoAllegatoAtto() {
		if(subdoc.getElencoDocumenti() == null || subdoc.getElencoDocumenti().getUid() == 0) {
			return false;
		}
		
		AllegatoAtto allegatoAtto = allegatoAttoDad.findAllegatoAttoByElencoDocumentiAllegato(subdoc.getElencoDocumenti());
		return allegatoAtto != null && allegatoAtto.getUid() != 0 && allegatoAtto.getUid() == subdoc.getAttoAmministrativo().getAllegatoAtto().getUid();
	}
	
	
	protected void caricaProvvisorioDiCassa() {
		if(subdoc.getProvvisorioCassa() == null){
			return;
		}
		ProvvisorioDiCassa provvisorioDiCassa = ricercaProvvisorioDiCassa();
		if(provvisorioDiCassa==null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("provvisorio di cassa", subdoc.getProvvisorioCassa().getAnno() 
					 +  "/" + subdoc.getProvvisorioCassa().getNumero()), Esito.FALLIMENTO);
		}else{
			log.debug("caricaProvvisorioDiCassa", "trovato provvisorio con uid: " + provvisorioDiCassa.getUid() + " e numero " + provvisorioDiCassa.getNumero());
		}
		subdoc.setProvvisorioCassa(provvisorioDiCassa);
	}

	
	private ProvvisorioDiCassa ricercaProvvisorioDiCassa() {
//		ho bisogno dell'uid per creare legame e dell'importo da regolarizzare per il controllo. 
//		Se mi arrivano gia'(quando richiamo da InserisceDocumentoPerProvvisoriEntrataService), salto la ricerca
		if(subdoc.getProvvisorioCassa().getUid() != 0 && subdoc.getProvvisorioCassa().getImportoDaRegolarizzare() != null){
			return subdoc.getProvvisorioCassa();
		}
		RicercaProvvisorioDiCassaPerChiave reqRPK = new RicercaProvvisorioDiCassaPerChiave();
		
		reqRPK.setBilancio(bilancio);
		reqRPK.setDataOra(req.getDataOra());
		reqRPK.setEnte(subdoc.getEnte());
		reqRPK.setRichiedente(req.getRichiedente());
		RicercaProvvisorioDiCassaK pRicercaProvvisorioK = new RicercaProvvisorioDiCassaK();
		pRicercaProvvisorioK.setAnnoProvvisorioDiCassa(subdoc.getProvvisorioCassa().getAnno());
		pRicercaProvvisorioK.setNumeroProvvisorioDiCassa(subdoc.getProvvisorioCassa().getNumero());
		pRicercaProvvisorioK.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E);
		reqRPK.setpRicercaProvvisorioK(pRicercaProvvisorioK);
		
		RicercaProvvisorioDiCassaPerChiaveResponse resRPK = provvisorioService.ricercaProvvisorioDiCassaPerChiave(reqRPK);
		log.logXmlTypeObject(resRPK, "Risposta ottenuta dal servizio RicercaProvvisorioPerChiave.");
		checkServiceResponseFallimento(resRPK);
		
		return resRPK.getProvvisorioDiCassa();
	}
	

	protected void checkProvvisorioDiCassa() {
		String methodName = "checkProvvisorioDiCassa";
		
		if(subdoc.getProvvisorioCassa() == null || subdoc.getProvvisorioCassa().getUid() == 0){
			log.debug(methodName, "non ho provvisorio di cassa da controllare, esco");
			return;
		}
		
		BigDecimal importoDaRegolarizzare = subdoc.getProvvisorioCassa().getImportoDaRegolarizzare() != null ? subdoc.getProvvisorioCassa().getImportoDaRegolarizzare() : BigDecimal.ZERO;
		log.debug(methodName, "importo da regolarizzare: " + importoDaRegolarizzare);
		
		BigDecimal importoDaVerificare = BigDecimal.ZERO;
		ProvvisorioDiCassa provvisorioAttuale = null;
		
		if(subdoc.getUid() != 0){
			//solo se sono in aggiornamento, altrimenti il provvisorio e' sicuramente aggiunto in questo momento
			provvisorioAttuale = provvisorioBilDad.findProvvisorioBySubdocId(subdoc.getUid());
		}
		
		//se il provvisorio non e' cambiato controllo solo l'eventuale differenza
		if(provvisorioAttuale != null && provvisorioAttuale.getUid() == subdoc.getProvvisorioCassa().getUid()){
			BigDecimal importoDaIncassareAttuale = subdocumentoEntrataDad.findImportoDaIncassareSubdocumentoEntrataById(subdoc.getUid());
			//se l'importo da incassare e' diminuito o e' rimasto invariato, non devo controllare niente, ho gia' copertura
			if(importoDaIncassareAttuale.compareTo(subdoc.getImportoDaIncassare()) >= 0){
				log.debug(methodName, "l'importo da incassare non e' aumentato, non devo verificare la copertura del provvisorio");
				return;
			}
			//se l'importo da incassare e' aumentoto, verifico di avere copertura per la differenza
			log.debug(methodName, "l'importo da incassare e' aumentato, verificare la copertura del provvisorio sulla differenza");
			importoDaVerificare = subdoc.getImportoDaIncassare().subtract(importoDaIncassareAttuale);
			log.debug(methodName, "importo da verificare(solo differenza): " + importoDaVerificare);
		}else{
			//se il provvisorio e' stato inserito in questo momento devo controllare tutto l'importo da incassare
			importoDaVerificare = subdoc.getImportoDaIncassare();
			log.debug(methodName, "importo da verificare(tutto l'importo da incassare): " + importoDaVerificare);
		}
		
		if(importoDaRegolarizzare.subtract(importoDaVerificare).signum() < 0){
			throw new BusinessException(ErroreFin.DATI_PROVVISORIO_QUIETANZA_ERRATI.getErrore(""), Esito.FALLIMENTO);
		}
		
		subdoc.setFlagACopertura(Boolean.TRUE);
	}
	
	
	protected void impostaFlagOrdinativo() {
		//uso sempre documentoSpesaDad, il metodo è indipendente da entrata/spesa perché usa uid
		final String methodName = "impostaFlagOrdinativo";
		log.debug(methodName, "inizio del metodo impostaFlagOrdinativo");
		Boolean flag = null;
		log.debug(methodName, "flag iniziale: " + flag);
		
		for(DocumentoSpesa docSpesa : documentoAssociato.getListaDocumentiSpesaFiglio()){
			if(TipoRelazione.SUBORDINATO.equals(docSpesa.getTipoRelazione())){
				log.debug(methodName, "ho trovato un documento subordinato di spesa. Uid:" + docSpesa.getUid());
				flag = Boolean.TRUE;				
				documentoEntrataDad.impostaFlagSulleQuote(docSpesa.getUid(), TipologiaAttributo.FLAG_ORDINATIVO_SINGOLO, flag);				
			}
		}
		
		log.debug(methodName, "flag dopo il primo for (doc spesa figli) " + flag);
		
		for(DocumentoEntrata docEntrata : documentoAssociato.getListaDocumentiEntrataFiglio()){
			if(TipoRelazione.SUBORDINATO.equals(docEntrata.getTipoRelazione())){
				log.debug(methodName, "ho trovato un documento subordinato di entrata. Uid: " + docEntrata.getUid());
				flag = Boolean.TRUE;
				documentoEntrataDad.impostaFlagSulleQuote(docEntrata.getUid(), TipologiaAttributo.FLAG_ORDINATIVO_SINGOLO, flag);
			}
		}
		
		log.debug(methodName, "flag dopo il secondo for (doc entrata figli) " + flag);
		
		if(Boolean.TRUE.equals(flag)) {			
			documentoEntrataDad.impostaFlagSulleQuote(documentoAssociato.getUid(), TipologiaAttributo.FLAG_ORDINATIVO_SINGOLO, flag);			
		}
		
		//SIAC-6294
		if(subdoc.getAccertamento() != null && (flag == null || Boolean.FALSE.equals(flag))){
			//subdoc.setFlagOrdinativoSingolo(subdoc.getAccertamento().isFlagAttivaGsa());
			flag = subdoc.getAccertamento().isFlagAttivaGsa();
		}
		
		if(flag != null){
			subdoc.setFlagOrdinativoSingolo(flag);
		}
		
	}
	
	
	protected void caricaDettaglioDocumentoAssociato() {
		this.documentoAssociato = documentoEntrataDad.findDocumentoEntrataById(subdoc.getDocumento().getUid());
		subdoc.setDocumento(documentoAssociato);
	}
	
	
//	eliminato per CR dell'11/03/2015
//	protected void gestisciAllegatoAtto(){
//		/*Inoltre se il provvedimento è valido, si verifica se ha almeno un elenco 
//		 * (entità Elenco Documenti Allegato) abbinato, se è così lo stato dell’ultimo Elenco 
//		 * abbinato che è stato inserito nel sistema deve essere aggiornato a ‘Provvisorio’. 
//		 * Se non ha un elenco abbinato è necessario inserire un nuovo Elenco ed abbinarlo alla determina stessa, 
//		 * l’Elenco sarà in stato ‘Provvisorio’.*/
//		AttoAmministrativo atto = subdoc.getAttoAmministrativo();
//		if(atto == null ||atto.getUid() == 0){
//			return;
//		}
//		if(atto.getAllegatoAtto() == null || atto.getAllegatoAtto().getUid() == 0){
//			inserisciNuovoElenco();
//			inserisciAllegato();
//		}else{
//			ElencoDocumentiAllegato elenco = elencoDocumentiAllegatoDad.findElencoDocPiuRecenteByAllegato(atto.getAllegatoAtto());
//			if(elenco == null){
//				throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("elenco documenti legato ad allegato atto: ", atto.getAllegatoAtto().getUid()));
//			}
//			aggiornaStatoUltimoElenco(elenco);
//		}
//	}
	
//	private void aggiornaStatoUltimoElenco(ElencoDocumentiAllegato elenco) {
//		if(elenco == null){
//			return;
//		}
//		
//		boolean subdocGiaPresente = false;
//		for(SubdocumentoEntrata subdocEntrata : elenco.getSubdocumentiEntrata()){
//			if(subdocEntrata.getUid() == subdoc.getUid()){
//				subdocGiaPresente=true;
//				break;
//			}
//		}
//		if(!subdocGiaPresente){
//			SubdocumentoEntrata subdocEntrata = new SubdocumentoEntrata();
//			subdocEntrata.setUid(subdoc.getUid());
//			elenco.getSubdocumenti().add(subdocEntrata);
//		}
//		elenco.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
//		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);
//		elencoDocumentiAllegatoDad.aggiornaElencoDocumentiAllegato(elenco);
//		subdoc.setElencoDocumenti(elenco);
//		
//	}
//
//
//	private void inserisciNuovoElenco() {
//		InserisceElenco request = new InserisceElenco();
//		
//		ElencoDocumentiAllegato elenco = new ElencoDocumentiAllegato();
//		List<Subdocumento<?,?>> lista = new ArrayList<Subdocumento<?,?>>();
//		SubdocumentoEntrata se = new SubdocumentoEntrata();
//		se.setUid(subdoc.getUid());
//		lista.add(se);
//		elenco.setSubdocumenti(lista);
//		elenco.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
//		elenco.setEnte(subdoc.getEnte());
//		elenco.setAnno(bilancio.getAnno());
//		elenco.setAllegatoAtto(subdoc.getAttoAmministrativo().getAllegatoAtto());
//		
//		request.setElencoDocumentiAllegato(elenco);
//		request.setBilancio(bilancio);
//		request.setRichiedente(req.getRichiedente());
//		
//		InserisceElencoResponse response = executeExternalService(inserisceElencoService, request);
//		subdoc.setElencoDocumenti(response.getElencoDocumentiAllegato());
//	}
//	
//	
//	private void inserisciAllegato() {
//		InserisceAllegatoAtto request = new InserisceAllegatoAtto();
//		request.setBilancio(bilancio);
//		request.setRichiedente(req.getRichiedente());
//		
//		AllegatoAtto allegato = new AllegatoAtto();
//		allegato.setCausale("Allegato Atto Per Atto Amministrativo "+ subdoc.getAttoAmministrativo().getNumero());
//		allegato.setEnte(subdoc.getEnte());
//		AttoAmministrativo aa = new AttoAmministrativo();
//		aa.setUid(subdoc.getAttoAmministrativo().getUid());
//		allegato.setAttoAmministrativo(aa);
//		allegato.setStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.DA_COMPLETARE);
//		allegato.setDatiSensibili(Boolean.FALSE);
//		List<ElencoDocumentiAllegato> list = new ArrayList<ElencoDocumentiAllegato>();
//		list.add(subdoc.getElencoDocumenti());
//		allegato.setElenchiDocumentiAllegato(list);
//		request.setAllegatoAtto(allegato);
//		
//		InserisceAllegatoAttoResponse response = executeExternalService(inserisceAllegatoAttoService, request);
//		subdoc.getAttoAmministrativo().setAllegatoAtto(response.getAllegatoAtto());
//	}
//

	

	
	protected void caricaCapitolo(){
		final String methodName = "caricaCapitolo";
		if(subdoc.getAccertamento() != null && subdoc.getAccertamento().getUid() !=0){
			//se ho già l'accertamento ignoro in ogni caso il capitolo
			log.debug(methodName, "ho già l'accertamento, non inserisco quello automatico");
			return;
		}
		if(capitolo == null ||capitolo.getAnnoCapitolo() == null || capitolo.getNumeroCapitolo() == null || capitolo.getNumeroArticolo() == null){
			log.debug(methodName, "non sono presenti campi sufficienti per effettuare la ricerca capitolo");
			return;
		}
		RicercaPuntualeCapitoloEntrataGestione reqCap = creaRequestRicercaPuntualeCapitolo();
		reqCap.setImportiDerivatiRichiesti(new HashSet<ImportiCapitoloEnum>()); //Non mi servono gli importi derivati e fanno perdere almeno 8 secondi sul totale dell'operazione!
		RicercaPuntualeCapitoloEntrataGestioneResponse resCap= executeExternalService(ricercaPuntualeCapitoloEntrataGestioneService, reqCap);
		capitolo = resCap.getCapitoloEntrataGestione();
		if(capitolo == null){
			log.debug(methodName, "non ho trovato un capitolo capitolo");
		}else{
			log.debug(methodName, "ho trovato il capitolo con uid: " + capitolo.getUid());
		}
		
		
	}
	
	private RicercaPuntualeCapitoloEntrataGestione creaRequestRicercaPuntualeCapitolo() {
		
		RicercaPuntualeCapitoloEntrataGestione request = new RicercaPuntualeCapitoloEntrataGestione();
		
		RicercaPuntualeCapitoloEGest ricercaPuntualeCapitoloEGest = new RicercaPuntualeCapitoloEGest();
		ricercaPuntualeCapitoloEGest.setAnnoCapitolo(capitolo.getAnnoCapitolo());
		ricercaPuntualeCapitoloEGest.setAnnoEsercizio(bilancio.getAnno());
		ricercaPuntualeCapitoloEGest.setNumeroCapitolo(capitolo.getNumeroCapitolo());
		ricercaPuntualeCapitoloEGest.setNumeroArticolo(capitolo.getNumeroArticolo());
		ricercaPuntualeCapitoloEGest.setNumeroUEB(capitolo.getNumeroUEB());
		ricercaPuntualeCapitoloEGest.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		
		request.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.CDC, TipologiaClassificatore.PDC, TipologiaClassificatore.SIOPE_ENTRATA,
				TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA, TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA, TipologiaClassificatore.RICORRENTE_ENTRATA));
		request.setRichiedente(req.getRichiedente());
		request.setEnte(subdoc.getEnte());
	
		request.setRicercaPuntualeCapitoloEGest(ricercaPuntualeCapitoloEGest);
		return request;
	}


	protected void inserisciAccertamentoAutomatico(){
		final String methodName = "inserisciAccertamentoAutomatico";
		if(capitolo == null || capitolo.getUid() == 0 || (subdoc.getAccertamento() != null && subdoc.getAccertamento().getUid() != 0)){
			log.debug(methodName, "non ci sono dati sufficienti per inserire l'accertamento automatico");
			return;
		}
		log.debug(methodName, "inserisco l'accertamento automatico.");
		Accertamento accertamento = new Accertamento();
		accertamento.setAutomatico(true);
		accertamento.setAnnoMovimento(capitolo.getAnnoCapitolo());
		accertamento.setImportoAttuale(subdoc.getImporto());
		//SIAC-3969
		accertamento.setImportoIniziale(subdoc.getImporto()); 
		accertamento.setImportoUtilizzabile(subdoc.getImporto());
		
		accertamento.setSoggetto(documentoAssociato.getSoggetto());
		accertamento.setAttoAmministrativo(subdoc.getAttoAmministrativo());
		accertamento.setCapitoloEntrataGestione(capitolo);
		accertamento.setStatoOperativoMovimentoGestioneEntrata(StatoOperativoMovimentoGestione.DEFINITIVO.name()); //???
		if(capitolo.getElementoPianoDeiConti()!=null) {
			accertamento.setCodPdc(capitolo.getElementoPianoDeiConti().getCodice());
			accertamento.setIdPdc(capitolo.getElementoPianoDeiConti().getUid());
			accertamento.setDescPdc(capitolo.getElementoPianoDeiConti().getDescrizione());
		}
		
		//SIAC-4049
		accertamento.setCodTransazioneEuropeaSpesa(capitolo.getTransazioneUnioneEuropeaEntrata()!=null?capitolo.getTransazioneUnioneEuropeaEntrata().getCodice():null);
		if(capitolo.getSiopeEntrata()!=null) {
			accertamento.setIdSiope(capitolo.getSiopeEntrata().getUid());
			accertamento.setCodSiope(capitolo.getSiopeEntrata().getCodice());
		}
		accertamento.setCodRicorrenteSpesa(capitolo.getRicorrenteEntrata()!=null?capitolo.getRicorrenteEntrata().getCodice():null);
		accertamento.setCodCapitoloSanitarioSpesa(capitolo.getPerimetroSanitarioEntrata()!=null?capitolo.getPerimetroSanitarioEntrata().getCodice():null);
		
		InserisceAccertamenti reqInsAcc = new InserisceAccertamenti();
		reqInsAcc.setPrimoAccertamentoDaInserire(accertamento);
		reqInsAcc.setBilancio(bilancio);
		reqInsAcc.setEnte(subdoc.getEnte());
		reqInsAcc.setRichiedente(req.getRichiedente());
		
		InserisceAccertamentiResponse resInsAcc = movimentoGestioneService.inserisceAccertamenti(reqInsAcc);
	   /* Il servizio di inserisceAccertamenti restituisce esito successo anche quando riscontra errori che bloccano l'inserimento.
		  Impostiamo l'esito a FALLIMENTO se ci sono errori nella response	*/
		if(resInsAcc.hasErrori()){
			resInsAcc.setEsito(Esito.FALLIMENTO);
		}
		checkServiceResponseFallimento(resInsAcc);
		if(resInsAcc.getElencoAccertamentiInseriti() != null && !resInsAcc.getElencoAccertamentiInseriti().isEmpty()){
			subdoc.setAccertamento(resInsAcc.getElencoAccertamentiInseriti().get(0));
			log.debug(methodName, "ho inserito l'accertamento: " + subdoc.getAccertamento().getUid());
		}
		
		
	}
	
	protected void checkNumero() throws ServiceParamError {
		checkCondition(StringUtils.isNotBlank(doc.getNumero()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		doc.setNumero(StringUtils.trimToNull(doc.getNumero()));
	}
	
	protected void gestisciNumeroRegistrazioneIva() {
		if(!Boolean.TRUE.equals(subdoc.getFlagRilevanteIVA())){
			//la quota non e' rilevante iva, non devo fare nulla
			return;
		}
		if(documentoAssociato == null || documentoAssociato.getListaSubdocumentoIva() == null || documentoAssociato.getListaSubdocumentoIva().isEmpty()){
			//non esiste subdoc iva collegato all'intero documento, non devo fare null
			return;
		}
		SubdocumentoIvaEntrata subdocIva = documentoAssociato.getListaSubdocumentoIva().get(0);
		subdoc.setNumeroRegistrazioneIVA(subdocIva.getNumeroRegistrazioneIVA());
		
	}
	
	/**
	 * Se a livello di ente &egrave; prevista l'emissione automatica dell'ordinativo 
	 * (GESTIONE_CONVALIDA_AUTOMATICA) il default &egrave; Automatico altrimenti &egrave; Manuale.
	 * 
	 * @return
	 */
	protected Boolean getDefaultFlagConvalidaManuale() {
		final String methodName = "getDefaultFlagConvalidaManuale";
		String gca = ente.getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_CONVALIDA_AUTOMATICA);
		if("CONVALIDA_AUTOMATICA".equals(gca)){
			log.debug(methodName, TipologiaGestioneLivelli.GESTIONE_CONVALIDA_AUTOMATICA.name()+ " impostata a CONVALIDA_AUTOMATICA");
			return Boolean.FALSE;
		} else if("CONVALIDA_MANUALE".equals(gca)){
			log.debug(methodName, TipologiaGestioneLivelli.GESTIONE_CONVALIDA_AUTOMATICA.name()+ " impostata a CONVALIDA_MANUALE");
			return Boolean.TRUE;
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * Impostazione del flag di convalida manuale.
	 * <br/>
	 * Se il flag non &eacute; fi&agrave; stato impostato, lo inizializza con il default per l'ente
	 * @param aggiornaFlagSeNonSelezionato se effettuare l'impostazione nel caso in cui il flag non sia selezionato
	 */
	protected void impostaFlagConvalidaManuale(boolean aggiornaFlagSeNonSelezionato) {
		if(subdoc.getFlagConvalidaManuale() == null && aggiornaFlagSeNonSelezionato) {
			subdoc.setFlagConvalidaManuale(getDefaultFlagConvalidaManuale());
		}
	}
	
	/**
	 * Controlla se esiste gi&agrave; un documento con lo stesso numero e anno.
	 * Se esiste viene sollevata l'eccezione con il messaggio:
	 * 
	 * &lt;COR_ERR_0008, Entit&agrave; gi&agrave; presente, 'DOCUMENTO ENTRATA', 
	 * (chiave ricerca entit&agrave; = anno documento del parametro di input + "/" + numero documento del parametro di input + "/" +
	 * tipo documento del parametro di input + "/" + soggetto del parametro
	 * di input + "/" + stato del documento trovato)&gt;
	 * 
	 * &lt;FIN_ERR_0087, Operazione non possibile, 'Inserimento Documento Entrata.'&gt; 
	 * 
	 */
	protected void checkDocumentoGiaEsistente() {
		RicercaPuntualeDocumentoEntrata reqRP = new RicercaPuntualeDocumentoEntrata();
		reqRP.setRichiedente(req.getRichiedente());
		
		DocumentoEntrata d = new DocumentoEntrata();
		d.setAnno(doc.getAnno());
		d.setNumero(doc.getNumero());
		d.setTipoDocumento(doc.getTipoDocumento());
		d.setStatoOperativoDocumento(null);
		d.setSoggetto(doc.getSoggetto());
		d.setEnte(doc.getEnte());
		
		reqRP.setStatoOperativoDocumentoDaEscludere(StatoOperativoDocumento.ANNULLATO);
		
		reqRP.setDocumentoEntrata(d);
		RicercaPuntualeDocumentoEntrataResponse resRP = serviceExecutor.executeService(RicercaPuntualeDocumentoEntrataService.class, reqRP);
		
		if(resRP.getDocumentoEntrata() != null) {	
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Inserimento Documento Entrata", resRP.getDocumentoEntrata().getDescAnnoNumeroTipoDocSoggettoStato()), Esito.FALLIMENTO, false);
		}
		
	}
	
	
	//SIAC-6645
	protected void gestioneModificaImporto(Accertamento accertamentoOSub, BigDecimal importoPrecedente, BigDecimal disponibilita) {
		final String methodName = "gestioneModificaImporto";
		boolean isSubAccertamento = accertamentoOSub instanceof SubAccertamento;
		boolean isAccertamentoResiduo = accertamentoOSub.getAnnoMovimento()< bilancio.getAnno();
		boolean isDisposizioneIncasso = documentoAssociato.getTipoDocumento().isDisposizioneDiIncasso();
		
		String key = calcolaChiaveAccertamento(subdoc.getAccertamento(), subdoc.getSubAccertamento());
		
		if(isAccertamentoResiduo || (isDisposizioneIncasso && !isDSIAbilitatoASfondareAccertamento())){
			log.debug(methodName, "blocco l'inserimento. isAccertamentoResiduo: " + isAccertamentoResiduo + "isDisposizioneIncasso: " + isDisposizioneIncasso + " sfondamento di un documento DSI ammesso per l'utente: " + isDSIAbilitatoASfondareAccertamento());
			throw new BusinessException(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore(msgOperazione, accertamentoOSub.getClass().getSimpleName() + " " + key), Esito.FALLIMENTO);
		}
		//l'accertamento e' di competenza (ovvero: anno accertamento = anno bilancio).
		//pertanto posso inserire una modifica movimento gestione.		
		
		if(!gestisciModificaImportoAccertamento){
			log.debug(methodName, "inserimento di modifica consentito solo se flag gestisciModifcaImportoAccertamento == true. Lancio l'errore.");
			throw new BusinessException(ErroreFin.DISPONIBILITA_MOVIMENTO_INSUFFICIENTE_MA_ADEGUABILE.getErrore(msgOperazione, accertamentoOSub.getClass().getSimpleName(), ""), Esito.FALLIMENTO);
		}
		
		BigDecimal importoModifica = calcolaImportoModifica(importoPrecedente, disponibilita);
		
		if(BigDecimal.ZERO.compareTo(importoModifica) >= 0) {
			// La modifica sarebbe uguale o minore a zero. Non la effettuo
			return;
		}
		
		
		if(isSubAccertamento){
			//HO subAccertamento
			//- in caso di subaccertamento: verificare se la modifica di sub e' <= al disponibile a subaccertare e  
			//inserire una 'modifica di importo' di subaccertamento pari alla differenza tra importoAttuale del subaccertamento e disponibilitàIncassare del subaccertamento; 
			//altrimenti se modifica di sub è > al disponibile a subaccertare occorrerà inserire contenstualmente e subito prima una modifica 
			//di accertamento pari alla differenza (tra importo modifica e disponibile a subaccertare).
			
			if(importoModifica.compareTo(subdoc.getAccertamento().getDisponibilitaIncassare())>0){ //inserimento contestuale della modifica accertamento
				
				BigDecimal importoModificaAccertamento = importoModifica.subtract(subdoc.getAccertamento().getDisponibilitaIncassare());
				ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = popolaModifica(subdoc.getAccertamento(), null, importoModificaAccertamento);
				log.info(methodName, "Importo Modifica Accertamento: "+importoModificaAccertamento);
				inserisciModificaImportoMovimentoGestioneEntrata(modificaMovimentoGestioneEntrata);
			}
			
			ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = popolaModifica(subdoc.getAccertamento(), (SubAccertamento)accertamentoOSub, importoModifica);
			log.info(methodName, "Importo Modifica SubAccertamento: "+importoModifica);
			inserisciModificaImportoMovimentoGestioneEntrata(modificaMovimentoGestioneEntrata);
			
		} else {
			//Ho l'accertamento (niente sub)
			//- in caso di accertamento: inserire una 'modifica di importo' di accertamento pari 
			//  alla differenza tra importoAttuale dell'accertamento e disponibilitàIncassare dell'accertamento
			
			ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = popolaModifica(accertamentoOSub, null, importoModifica);
			log.info(methodName, "Importo Modifica Accertamento: "+importoModifica);
			inserisciModificaImportoMovimentoGestioneEntrata(modificaMovimentoGestioneEntrata);
		}
	}
	
	/**
	 * Calcolo dell'importo in modifica
	 * @param disponibilita la disponibilit&agrave; del movimento di gestione
	 * @return la modifica di importo da applicare
	 */
	protected BigDecimal calcolaImportoModifica(BigDecimal importoPrecedente, BigDecimal disponibilita) {
		return subdoc.getImportoNotNull().subtract(importoPrecedente).subtract(disponibilita);
	}
	
	private ModificaMovimentoGestioneEntrata popolaModifica(Accertamento acc, SubAccertamento subacc, BigDecimal importoModifica/*, BigDecimal importoAlNettoDellaModifica*/) {
		ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = new ModificaMovimentoGestioneEntrata();
		modificaMovimentoGestioneEntrata.setAccertamento(acc);
		modificaMovimentoGestioneEntrata.setUidAccertamento(acc.getUid());
		
		modificaMovimentoGestioneEntrata.setSubAccertamento(subacc);
		modificaMovimentoGestioneEntrata.setUidSubAccertamento(subacc!=null?subacc.getUid():null);
		
		AttoAmministrativo attoAmm = subacc!=null?subacc.getAttoAmministrativo():acc.getAttoAmministrativo();
		modificaMovimentoGestioneEntrata.setAttoAmministrativo(attoAmm);
		modificaMovimentoGestioneEntrata.setIdStrutturaAmministrativa(attoAmm.getStrutturaAmmContabile()!=null?attoAmm.getStrutturaAmmContabile().getUid():null);
		modificaMovimentoGestioneEntrata.setAttoAmministrativoAnno(""+attoAmm.getAnno());
		modificaMovimentoGestioneEntrata.setAttoAmministrativoNumero(attoAmm.getNumero());
		modificaMovimentoGestioneEntrata.setAttoAmministrativoTipoCode(attoAmm.getTipoAtto().getCodice());
		
		if(acc.getElencoSubAccertamenti()==null){ //workaround servizio FIN..se trova la lista null va in NullPointer.
			acc.setElencoSubAccertamenti(new ArrayList<SubAccertamento>());
		}
		
		if(acc.getListaModificheMovimentoGestioneEntrata()==null){ //workaround servizio FIN..se trova la lista null va in NullPointer.
			acc.setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
		}
		
		if(subacc!=null && subacc.getListaModificheMovimentoGestioneEntrata()==null){ //workaround servizio FIN..se trova la lista null va in NullPointer.
			subacc.setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
		}
		
		BigDecimal importoAlNettoDellaModifica = (subacc!=null?subacc.getImportoAttuale():acc.getImportoAttuale()).add(importoModifica);
		modificaMovimentoGestioneEntrata.setImportoNew(importoAlNettoDellaModifica);
		modificaMovimentoGestioneEntrata.setImportoOld(importoModifica);
		
		modificaMovimentoGestioneEntrata.setTipoMovimento(subacc!=null&&subacc.getUid()!=0?Constanti.MODIFICA_TIPO_SAC:Constanti.MODIFICA_TIPO_ACC); // ACCERTAMENTO
		// SIAC-5219: portata a costante la descrizione (serve nelle ricerche)
		modificaMovimentoGestioneEntrata.setDescrizione(Constanti.MODIFICA_AUTOMATICA_PREDISPOSIZIONE_INCASSO);
		modificaMovimentoGestioneEntrata.setTipoModificaMovimentoGestione("ALT"); //ALTRO
	
		return modificaMovimentoGestioneEntrata;
	}

	private InserisciModificaImportoMovimentoGestioneEntrataResponse inserisciModificaImportoMovimentoGestioneEntrata(ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata) {
		
		InserisciModificaImportoMovimentoGestioneEntrata reqIMIMGE = new InserisciModificaImportoMovimentoGestioneEntrata();
		reqIMIMGE.setEnte(ente);
		reqIMIMGE.setRichiedente(req.getRichiedente());
		reqIMIMGE.setBilancio(bilancio);
		reqIMIMGE.setDataOra(new Date());
		reqIMIMGE.setModificaMovimentoGestioneEntrata(modificaMovimentoGestioneEntrata);
		InserisciModificaImportoMovimentoGestioneEntrataResponse resIMIMGE = serviceExecutor.executeServiceSuccess(new ServiceInvoker<InserisciModificaImportoMovimentoGestioneEntrata, InserisciModificaImportoMovimentoGestioneEntrataResponse>() {
			@Override
			public InserisciModificaImportoMovimentoGestioneEntrataResponse invokeService(InserisciModificaImportoMovimentoGestioneEntrata req) {
				return movimentoGestioneService.inserisciModificaImportoMovimentoGestioneEntrata(req);
			}
		}, reqIMIMGE);
		log.logXmlTypeObject(resIMIMGE, "Response InserisciModificaImportoMovimentoGestioneEntrataService");
		return resIMIMGE;
		
	}
	
	/**
	 * SIAC-6565 - 
	 * SIAC-6737 - INSERIMENTO DOCUMENTO DI ENTRATA (Soggetto)
	 *
	 * @throws BusinessException 
	 */
	protected void checkSoggettoDocumento(){
		if((doc.getSoggetto() == null) || (doc.getSoggetto().getCanalePA() == null) || (doc.getSoggetto().getCanalePA().equalsIgnoreCase(""))) {
			throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto : Canale PA"));
		}
		else {
			if(doc.getSoggetto().getCanalePA().equalsIgnoreCase("PA")) {
				if((doc.getSoggetto().getCodDestinatario() == null) || (doc.getSoggetto().getCodDestinatario().equalsIgnoreCase("")))
					throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto : Codice IPA"));
			}
			else {
				if ( ((doc.getSoggetto().getCodDestinatario() == null) || (doc.getSoggetto().getCodDestinatario().equalsIgnoreCase(""))) &&
					 ((doc.getSoggetto().getEmailPec() == null) || (doc.getSoggetto().getEmailPec().equalsIgnoreCase(""))) )
					throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto : Codice IPA"));
			}
		}
	}	
}









