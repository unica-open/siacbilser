/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siacbilser.business.service.documento.MovimentoGestioneServiceCallGroup;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciModificaImportoMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciModificaImportoMovimentoGestioneEntrataResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

/**
 * Base service per il crud dei predocumenti di spesa.
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class CrudPreDocumentoDiEntrataBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ,RES> {
	
	//DADs...
	@Autowired
	protected PreDocumentoEntrataDad preDocumentoEntrataDad;
	@Autowired
	protected SoggettoDad soggettoDad;
	@Autowired
	protected ProvvedimentoDad provvedimentoDad;
	@Autowired
	protected CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	@Autowired
	protected ImportiCapitoloDad importiCapitoloDad;
	@Autowired
	protected BilancioDad biancioDad;

	
	//Services...
	@Autowired
	protected MovimentoGestioneService movimentoGestioneService;
	@Autowired(required = false)
	protected AggiornaStatoPreDocumentoDiEntrataService aggiornaStatoPreDocumentoDiEntrataService;
	@Autowired
	protected ProvvisorioService provvisorioService;
	
	
	//Fields...
	protected PreDocumentoEntrata preDoc;
	protected Bilancio bilancio;
	protected String msgOperazione = "";
	protected boolean gestisciModificaImportoAccertamento;
	
	
	protected MovimentoGestioneServiceCallGroup mgscg;
	
	@Override
	protected void init() {
		super.init();
		mgscg = new MovimentoGestioneServiceCallGroup(serviceExecutor,req.getRichiedente(),ente, bilancio);
	}
	
	/**
	 * Check capitolo.
	 */
	protected void checkCapitolo() {
		//il capitolo è facoltativo
		if(preDoc.getCapitoloEntrataGestione()==null || preDoc.getCapitoloEntrataGestione().getUid()==0){
			return;
		}
		
		CapitoloEntrataGestione capitolo = caricaCapitolo();

		checkValiditaCapitolo(capitolo);
		checkDisponibilitaCapitolo(capitolo);
	}
	
	
	/**
	 * Carica capitolo.
	 *
	 * @return the capitolo entrata gestione
	 */
	private CapitoloEntrataGestione caricaCapitolo() {
		String methodName = "caricaCapitolo";
		RicercaDettaglioCapitoloEGest ricercaDettaglioCapitoloEGest = new RicercaDettaglioCapitoloEGest();
		ricercaDettaglioCapitoloEGest.setChiaveCapitolo(preDoc.getCapitoloEntrataGestione().getUid());	
		CapitoloEntrataGestione capitolo = capitoloEntrataGestioneDad.ricercaDettaglioCapitoloEntrataGestione(ricercaDettaglioCapitoloEGest);
		
		if(capitolo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitlo entrata gestione", "uid: "+preDoc.getCapitoloEntrataGestione().getUid()));
		}
				
		log.debug(methodName, "Capitolo trovato con uid: " + preDoc.getCapitoloEntrataGestione().getUid() + " desc: "+capitolo.getDescBilancioAnnoNumeroArticoloUEB());
						
		ImportiCapitoloEG importiCapitoloEG = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno(), ImportiCapitoloEG.class, null);			
		capitolo.setImportiCapitoloEG(importiCapitoloEG);
		
		if(capitolo.getImportiCapitoloEG() == null  || capitolo.getImportiCapitoloEG().getDisponibilitaAccertareAnno1()== null) {			
			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("Impossibile ottenere la disponibilita accertare del capitolo."));			
		}
		
		log.debug(methodName, "Capitolo con disponibilita a impegnare: " + capitolo.getImportiCapitoloEG().getDisponibilitaAccertareAnno1());
		
		return capitolo;
	}
	
	/**
	 * Carica bilancio.
	 */
	protected void caricaBilancio() {
		bilancio = biancioDad.getBilancioByUid(bilancio.getUid());
	}
	
	
	
	/**
	 * Check soggetto.
	 */
	protected void checkSoggetto() {
		//il soggetto è facoltativo
		if(preDoc.getSoggetto()==null || preDoc.getSoggetto().getUid()==0){
			return;
		}
		StatoOperativoAnagrafica statoOperativoAnagrafica = soggettoDad.findStatoOperativoAnagraficaSoggetto(preDoc.getSoggetto());	
		
		if(!StatoOperativoAnagrafica.VALIDO.equals(statoOperativoAnagrafica)){
			throw new BusinessException(it.csi.siac.siacfinser.model.errore.ErroreFin.SOGGETTO_NON_VALIDO.getErrore(), Esito.FALLIMENTO);
		}
		if( StatoOperativoAnagrafica.BLOCCATO.equals(statoOperativoAnagrafica)){
			throw new BusinessException(ErroreFin.SOGGETTO_BLOCCATO.getErrore(), Esito.FALLIMENTO);
		}
	}
		
	
	/**
	 * Check congruenza soggetto incasso.
	 */
	protected void checkCongruenzaSoggettoIncasso() {
    	//il controllo viene effettuato solo se sono presenti sia soggetto sia accertamento
		if (preDoc.getSoggetto()==null || preDoc.getAccertamento()==null ){
			return;
		}
		Soggetto soggettoAccertamento= obtainSoggettoMovimentoGestione();
		
		if (soggettoAccertamento!=null &&  soggettoAccertamento.getUid()!=preDoc.getSoggetto().getUid() ){
			throw new BusinessException(ErroreFin.SOGGETTO_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto incasso","accertamento"), Esito.FALLIMENTO);
		} else {
			checkValiditaClassiSoggetto();
		}
	}
	
	/**
	 * Ottiene il soggetto collegato al movimento di gestione corretto.
	 * 
	 * @return il soggetto legato all'accertamento o al subaccertamento
	 */
	private Soggetto obtainSoggettoMovimentoGestione() {
		Soggetto result = null;
		if(preDoc.getSubAccertamento() != null && preDoc.getSubAccertamento().getUid() != 0) {
			// Devo cercare per il subimpegno
			result = soggettoDad.findSoggettoByIdSubMovimentoGestione(preDoc.getSubAccertamento().getUid());
		} else {
			result = soggettoDad.findSoggettoByIdMovimentoGestione(preDoc.getAccertamento().getUid());
		}
		return result;
	}
	
	private void checkValiditaClassiSoggetto() {
		final String methodName = "checkValiditaClassiSoggetto";
		log.debug(methodName, "Controllo coerenza classi soggetto");
		ClasseSoggetto classeSoggettoMovimentoGestione = obtainClasseSoggettoByMovimentoGestione();
		if(classeSoggettoMovimentoGestione == null) {
			log.debug(methodName, "Il movimento di gestione non e' associato ad una classe");
			// Va tutto bene?
			return;
		}
		List<ClasseSoggetto> classiSoggetto = obtainListClasseSoggettoBySoggetto();
		boolean presenteClasse = false;
		for(ClasseSoggetto cs : classiSoggetto) {
			if(cs != null && cs.getUid() == classeSoggettoMovimentoGestione.getUid()) {
				log.debug(methodName, "Classe soggetto " + cs.getUid() + " coerente tra soggetto e movimento di gestione");
				presenteClasse = true;
				break;
			}
		}
		if(!presenteClasse) {
			log.warn(methodName, "Il soggetto non appartiene alla classificazione. Non e' un errore ma deve essere segnalato");
//			throw new BusinessException(ErroreFin.SOGGETTO_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto incasso",
//				"accertamento: il soggetto di incasso deve appartenere alla classificazione"), Esito.FALLIMENTO);
		}
	}
	
	private List<ClasseSoggetto> obtainListClasseSoggettoBySoggetto() {
		List<ClasseSoggetto> result = soggettoDad.findClasseSoggetto(preDoc.getSoggetto().getUid());
		return result == null ? new ArrayList<ClasseSoggetto>() : result;
	}


	private ClasseSoggetto obtainClasseSoggettoByMovimentoGestione() {
		ClasseSoggetto result = null;
		if(preDoc.getSubAccertamento() != null && preDoc.getSubAccertamento().getUid() != 0) {
			result = soggettoDad.findClasseSoggettoByMovgestTs(preDoc.getSubAccertamento().getUid());
		} else {
			result = soggettoDad.findClasseSoggettoByMovgest(preDoc.getAccertamento().getUid());
		}
		return result;
	}
	
    
	/**
	 * Check provvedimento.
	 */
	protected void checkProvvedimento(){
    	//il provvedimento non e' obbligatorio
    	if (preDoc.getAttoAmministrativo()==null || preDoc.getAttoAmministrativo().getUid()==0){
			return;
		}
    	AttoAmministrativo attoAmministrativo = provvedimentoDad.findProvvedimentoById(preDoc.getAttoAmministrativo().getUid());
    	
    	if ( StatoOperativoAtti.ANNULLATO.equals(attoAmministrativo.getStatoOperativoAtti())){
    		throw new BusinessException(ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore(), Esito.FALLIMENTO);
    	}
    	if ( !StatoOperativoAtti.DEFINITIVO.equals(attoAmministrativo.getStatoOperativoAtti())){
    		throw new BusinessException(it.csi.siac.siacfinser.model.errore.ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore(msgOperazione + " predisposizione di entrata","definitivo"), Esito.FALLIMENTO);
    	}
    }
    
    
	/**
	 * Check accertamento.
	 */
	protected void caricaAccertamentoESubAccertemanto(){
		if(preDoc.getAccertamento()==null){
			return;
		}
    		
		Accertamento accertamento = ricercaAccertamentoPerChiaveOttimizzato();		

		preDoc.setAccertamento(accertamento);
		
		
		if(preDoc.getSubAccertamento()!= null && preDoc.getSubAccertamento().getUid()!=0 && accertamento.getElencoSubAccertamenti()!=null){
			
			if(accertamento.getSubAccertamenti() == null){
				throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("subaccertamento", preDoc.getSubAccertamento().getNumero()+""), Esito.FALLIMENTO);
			}
			
			for(SubAccertamento subAcc: accertamento.getElencoSubAccertamenti()){
				if(subAcc.getUid()==preDoc.getSubAccertamento().getUid()){
					preDoc.setSubAccertamento(subAcc);
				}
			}
		}
		
    }


	protected void checkAccertamento() {
		if(preDoc.getAccertamento()==null){
			return;
		}
		
		if(preDoc.getSubAccertamento()!= null && preDoc.getSubAccertamento().getUid()!=0){
    		//Ho il subAccertamento, i controlli verranno effettuati sul subAccertamento dal metodo #checkSubAccertamento
			return;
		}
		//se non ho subAccertamento effettuo controlli sull'accertamento
    		
		//controllo validita' accertamento
		if(!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equalsIgnoreCase(preDoc.getAccertamento().getStatoOperativoMovimentoGestioneEntrata())){
			throw new BusinessException(ErroreFin.MOVIMENTO_GESTIONE_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE.getErrore("L'accertamento","definitivo","Non può essere imputato ad una predisposizione di incasso"), Esito.FALLIMENTO);
		} 
		
		//controllo congruenza tra anno accertamento e anno bilancio
		if (preDoc.getAccertamento().getAnnoMovimento()> bilancio.getAnno()){
			throw new BusinessException(ErroreFin.MOVIMENTO_GESTIONE_PLURIENNALE_NON_AMMESSO_PER_OPERAZIONE.getErrore("accertamento",msgOperazione + " predisposizione di incasso"), Esito.FALLIMENTO);
		}
		
		//controllo congruenza tra importo predocumento e disponibilita' accertamento
		checkDisponibilitaAccertamentoSubAccertamento(preDoc.getAccertamento());
	}

	private BigDecimal caricaImportoPrecedentePreDocumentoByAccertamento() {
		BigDecimal importoPrecedente;
		Integer uidAccertamentoPrecedente = caricaUidAccertamentoPrecedente();
		if(uidAccertamentoPrecedente == null || uidAccertamentoPrecedente != preDoc.getAccertamento().getUid()){
			importoPrecedente = BigDecimal.ZERO;
		}else{
			importoPrecedente = caricaImportoPrecedentePreDocumento();
		}
		return importoPrecedente;
	}



	protected void checkSubAccertamento() {
		
		if(preDoc.getAccertamento()==null){
			return;
		}
		
		boolean subAccertamentoPresente = preDoc.getSubAccertamento()!= null && preDoc.getSubAccertamento().getUid()!=0;
		
		if(!subAccertamentoPresente){
			//Non ho il subAccertamentoValorizzato. Non eseguo alcun check.
			return;
		}
		
		//cerco il subaccertamento, se mi è passato. Se lo trovo i controlli vengono effettuati sul subAccertamento e non sull'accertamento
			
		SubAccertamento subAccertamento = preDoc.getSubAccertamento();
		if(subAccertamento==null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subaccertamento",preDoc.getSubAccertamento().getNumero()+"/"+preDoc.getSubAccertamento().getAnnoMovimento()+""), Esito.FALLIMENTO);
		}
		
		//controllo congruenza tra anno subimpegno e anno bilancio
		if (subAccertamento.getAnnoMovimento()> bilancio.getAnno()){
			throw new BusinessException(ErroreFin.MOVIMENTO_GESTIONE_PLURIENNALE_NON_AMMESSO_PER_OPERAZIONE.getErrore("subaccertamento",msgOperazione + " predisposizione di incasso"), Esito.FALLIMENTO);
		}
		
		//controllo congruenza con capitolo
		if (preDoc.getCapitoloEntrataGestione()!=null && preDoc.getCapitoloEntrataGestione().getUid()!=0 
				&& subAccertamento.getCapitoloEntrataGestione()!= null 
				&& subAccertamento.getCapitoloEntrataGestione().getUid() != preDoc.getCapitoloEntrataGestione().getUid()){
			
				throw new BusinessException(ErroreFin.ACCERTAMENTO_NON_PERTINENTE_AL_CAPITOLO_UPB.getErrore(), Esito.FALLIMENTO);
			
		}
		
		//controllo validita' subaccertamento
		if(!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equalsIgnoreCase(subAccertamento.getStatoOperativoMovimentoGestioneEntrata())){
			throw new BusinessException(ErroreFin.MOVIMENTO_GESTIONE_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE.getErrore("Il subaccertamento","definitivo","Non può essere imputato ad una predisposizione di incasso"), Esito.FALLIMENTO);
		}
		
		//controllo congruenza tra importo predocumento e disponibilita' subaccertamento
		checkDisponibilitaAccertamentoSubAccertamento(subAccertamento);
	}

	private BigDecimal caricaImportoPrecedentePredocumentoBySubAccertamento() {
		BigDecimal importoPrecedente;
		Integer uidSubAccertamentoPrecedente = caricaUidSubAccertamentoPrecedente();
		if(uidSubAccertamentoPrecedente == null || uidSubAccertamentoPrecedente != preDoc.getSubAccertamento().getUid()){
			importoPrecedente = BigDecimal.ZERO;
		}else{
			importoPrecedente = caricaImportoPrecedentePreDocumento();
		}
		return importoPrecedente;
	}
	
	private Integer caricaUidSubAccertamentoPrecedente() {
		if(preDoc.getUid() == 0){
			return null;
		}
		return preDocumentoEntrataDad.findUidSubAccertamentoPreDocumentoSpesaById(preDoc.getUid());
	}
	
	
	private Integer caricaUidAccertamentoPrecedente() {
		if(preDoc.getUid() == 0){
			return null;
		}
		return preDocumentoEntrataDad.findUidAccertamentoPreDocumentoSpesaById(preDoc.getUid());
	}


	protected BigDecimal caricaImportoPrecedentePreDocumento() {
		return preDocumentoEntrataDad.findImportoPreDocumentoById(preDoc.getUid());
	}
	
	
	/**
	 * Controlla se è soddisfatatta la seguente condizione:
	 * disponibilità incassare + quotaVecchia.importo >= quotaNuova.importo
	 * 
	 * @param accertamentoOSub
	 * @param importoNuovo - importo nuovo del predocumento
	 * @param importoPrecedente - importo precedente del documento (0 se l'accertamento/sub precedente non e' cambiato)
	 */ 
	private void checkDisponibilitaAccertamentoSubAccertamento(Accertamento accertamentoOSub) {
		final String methodName = "checkDisponibilitaAccertamentoSubAccertamento";
		boolean isSubAccertamento = accertamentoOSub instanceof SubAccertamento;
		
		BigDecimal importoPrecedente = caricaImportoPrecedentePredocumento(isSubAccertamento);
		BigDecimal importoNuovo = preDoc.getImportoNotNull();
		
		BigDecimal disponibilita = accertamentoOSub.getDisponibilitaIncassare();
		disponibilita = disponibilita != null ? disponibilita : BigDecimal.ZERO;
		
		boolean isDisponibilitaSufficiente = disponibilita.add(importoPrecedente).compareTo(importoNuovo)>=0;
		
		log.info(methodName, "disponibilita: " + disponibilita + " + importoPrecedente: " + importoPrecedente 
				+ " >= importoNuovo: " + importoNuovo + " -> isDisponibilitaSufficiente: " + isDisponibilitaSufficiente);
		
		if(isDisponibilitaSufficiente) {
			log.debug(methodName, "Disponibilita sufficiente. Non devo inserire modifiche di importo.");
			return;
		}
		
		// SIAC-4310
		gestioneModificaImporto(accertamentoOSub, importoPrecedente, disponibilita);
	}

	protected void gestioneModificaImporto(Accertamento accertamentoOSub, BigDecimal importoPrecedente, BigDecimal disponibilita) {
		final String methodName = "gestioneModificaImporto";
		boolean isSubAccertamento = accertamentoOSub instanceof SubAccertamento;
		boolean isAccertamentoResiduo = accertamentoOSub.getAnnoMovimento()< bilancio.getAnno();
		
		if(isAccertamentoResiduo){
			throw new BusinessException(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore(msgOperazione + " predisposizione incasso", accertamentoOSub.getClass().getSimpleName()), Esito.FALLIMENTO);
		}
		//l'accertamento e' di competenza (ovvero: anno accertamento = anno bilancio).
		//pertanto posso inserire una modifica movimento gestione.
		
		
		if(!gestisciModificaImportoAccertamento){
			throw new BusinessException(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore(msgOperazione + " predisposizione incasso", accertamentoOSub.getClass().getSimpleName(), "E' possibile adeguare l'importo."), Esito.FALLIMENTO);
		}
		
		// Carico eventualmente l'importo attuale dell'accertamento
		caricaImportoAccertamentoSub(accertamentoOSub);
		
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
			
			if(importoModifica.compareTo(preDoc.getAccertamento().getDisponibilitaIncassare())>0){ //inserimento contestuale della modifica accertamento
				
				BigDecimal importoModificaAccertamento = importoModifica.subtract(preDoc.getAccertamento().getDisponibilitaIncassare());
				ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = popolaModifica(preDoc.getAccertamento(), null, importoModificaAccertamento);
				log.info(methodName, "Importo Modifica Accertamento: "+importoModificaAccertamento);
				inserisciModificaImportoMovimentoGestioneEntrata(modificaMovimentoGestioneEntrata);
			}
			
			ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = popolaModifica(preDoc.getAccertamento(), (SubAccertamento)accertamentoOSub, importoModifica);
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
		return preDoc.getImportoNotNull().subtract(importoPrecedente).subtract(disponibilita);
	}
	
	/**
	 * Copia gli importi nell'accertamento
	 * @param acc
	 * @param importi
	 */
	protected void copiaImporti(Accertamento acc, Map<String, BigDecimal> importi) {
		acc.setImportoAttuale(importi.get("A"));
		acc.setImportoIniziale(importi.get("I"));
	}
	
	/**
	 * Caricamento degli importi dell'accertamento o del suub
	 * @param accertamentoOSub l'accertamento
	 */
	protected void caricaImportoAccertamentoSub(Accertamento accertamentoOSub) {
		// Da implementare nelle sottoclassi se necessario
	}

	protected BigDecimal caricaImportoPrecedentePredocumento(boolean isSubAccertamento) {
		BigDecimal importoPrecedente;
		if(isSubAccertamento){
			importoPrecedente = caricaImportoPrecedentePredocumentoBySubAccertamento();
		} else {
			importoPrecedente = caricaImportoPrecedentePreDocumentoByAccertamento();
		}
		return importoPrecedente!=null?importoPrecedente:BigDecimal.ZERO;
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
		
		//gli attuali controlli vogliono l'importoOLD con il valore della modifica e 
		//nell'importoNew l'importo attuale dell'acc al netto della modifica, 
		//quindi se importo modifica -1000 e importo attuale 150.000 mi aspetto:
		//	importoOLD = -1000
		//	importoNew = 149.000
		
		
		
		modificaMovimentoGestioneEntrata.setTipoMovimento(subacc!=null&&subacc.getUid()!=0?Constanti.MODIFICA_TIPO_SAC:Constanti.MODIFICA_TIPO_ACC); // ACCERTAMENTO
		// SIAC-5219: portata a costante la descrizione (serve nelle ricerche)
		modificaMovimentoGestioneEntrata.setDescrizione(Constanti.MODIFICA_AUTOMATICA_PREDISPOSIZIONE_INCASSO);
		modificaMovimentoGestioneEntrata.setTipoModificaMovimentoGestione("ALT"); //ALTRO
//		modificaMovimentoGestioneEntrata.setMotivoModificaEntrata(new ClassificatoreGenerico()); //TODO che classif devo passare? e' obbligatorio?
		
//		bilancio (qui basta l'anno)
//				modificaMovimentoGestioneEntrata.tipoMovimento
//				modificaMovimentoGestioneEntrata.descrizione
//				modificaMovimentoGestioneEntrata.tipoModificaMovimentoGestione
//				modificaMovimentoGestioneEntrata.accertamento
//				modificaMovimentoGestioneEntrata.motivoModificaEntrata
//				+ uidSubAccertamento per modifica di sub cosi ho direttamnete l'id del sub per leggere dei dati su cui devo poi fare delle verifiche
//				tipoMovimentoes: <tipoMovimento>ACC</tipoMovimento>
//				<TipoModificaMovimentoGestione>ALT</TipoModificaMovimentoGestione> sta per altro (queste sono codificate)
		
	
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
	 * ricercaAccertamentoPerChiaveOttimizzato 
	 * 
	 * @return
	 */
	private Accertamento ricercaAccertamentoPerChiaveOttimizzato() {
		
		RicercaAttributiMovimentoGestioneOttimizzato parametri = new RicercaAttributiMovimentoGestioneOttimizzato();
		parametri.setEscludiSubAnnullati(true);
		parametri.setCaricaSub(preDoc.getSubAccertamento()!=null && preDoc.getSubAccertamento().getNumero() != null);
				
		DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		parametriElencoIds.setEscludiAnnullati(true);
		parametri.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse resRAPC = mgscg.ricercaAccertamentoPerChiaveOttimizzato(preDoc.getAccertamento(), parametri, new DatiOpzionaliCapitoli(), preDoc.getSubAccertamento());
		log.logXmlTypeObject(resRAPC, "Risposta ottenuta dal servizio RicercaAccertamentoPerChiaveOttimizzato.");
		checkServiceResponseFallimento(resRAPC);
		
		Accertamento accertamento = resRAPC.getAccertamento();
		if(accertamento==null) {
			res.addErrori(resRAPC.getErrori());
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", preDoc.getAccertamento().getNumero()+""+ preDoc.getAccertamento().getAnnoMovimento()+""), Esito.FALLIMENTO);
		}
		return accertamento;
	}
	/**
	 * Aggiorna stato operativo pre documento.
	 *
	 * @param preDocumento the pre documento
	 * @param ricaricaDettaglioPreDocumentoSpesa the ricarica dettaglio pre documento spesa
	 * @return the stato operativo pre documento
	 */
	protected StatoOperativoPreDocumento aggiornaStatoOperativoPreDocumento(PreDocumentoEntrata preDocumento, boolean ricaricaDettaglioPreDocumentoSpesa) {
		AggiornaStatoPreDocumentoDiEntrata reqAs = new AggiornaStatoPreDocumentoDiEntrata();
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setPreDocumentoEntrata(preDocumento);
		reqAs.setRicaricaDettaglioPreDocumento(ricaricaDettaglioPreDocumentoSpesa);
		AggiornaStatoPreDocumentoDiEntrataResponse resAs = executeExternalService(aggiornaStatoPreDocumentoDiEntrataService, reqAs);
		return resAs.getPreDocumentoEntrata().getStatoOperativoPreDocumento();
	}
    
	
	/**
	 * Check validita capitolo.
	 *
	 * @param capitolo the capitolo
	 */
	private void checkValiditaCapitolo(CapitoloEntrataGestione capitolo){
		if(!StatoOperativoElementoDiBilancio.VALIDO.equals(capitolo.getStatoOperativoElementoDiBilancio())){
			throw new BusinessException(ErroreFin.CAPITOLO_NON_VALIDO_PER_OPERAZIONE.getErrore(capitolo.getStatoOperativoElementoDiBilancio(),"di entrata",msgOperazione,"predisposizione di incasso"), Esito.FALLIMENTO);
		}
	}
	
	
	/**
	 * Check disponibilita capitolo.
	 *
	 * @param capitolo the capitolo
	 */
	private void checkDisponibilitaCapitolo(CapitoloEntrataGestione capitolo){
    	if (preDoc.getAccertamento()==null && capitolo.getImportiCapitoloEG().getDisponibilitaAccertareAnno1().compareTo(preDoc.getImportoNotNull())<0) {
    		throw new BusinessException(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore("Inserimento predisposizione incasso", "capitolo"), Esito.FALLIMENTO);    		
    	}
    }
	
	//parte aggiunta il 15/06/2015 ahmad
	/**
	 * carica il provvisorio di cassa 
	 */
	protected void caricaProvvisorioDiCassa() {
		if(preDoc.getProvvisorioDiCassa() == null || preDoc.getProvvisorioDiCassa().getNumero()== null || preDoc.getProvvisorioDiCassa().getAnno() == null){
			return;
		}
		ProvvisorioDiCassa provvisorioDiCassa = ricercaProvvisorioDiCassa();
		if(provvisorioDiCassa == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("provvisorio di cassa", preDoc.getProvvisorioDiCassa().getAnno() 
					 +  "/" + preDoc.getProvvisorioDiCassa().getNumero()), Esito.FALLIMENTO);
		}else{
			log.debug("caricaProvvisorioDiCassa", "trovato provvisorio con uid: " + provvisorioDiCassa.getUid() + " e numero " + provvisorioDiCassa.getNumero());
		}
		preDoc.setProvvisorioDiCassa(provvisorioDiCassa);
	}
	

	private ProvvisorioDiCassa ricercaProvvisorioDiCassa() {
		RicercaProvvisoriDiCassa ricercaProvvisoriDiCassa = new RicercaProvvisoriDiCassa();
		ricercaProvvisoriDiCassa.setEnte(ente);
		ricercaProvvisoriDiCassa.setRichiedente(req.getRichiedente());
		
		ParametroRicercaProvvisorio parametroRicercaProvvisorio = new ParametroRicercaProvvisorio();
		parametroRicercaProvvisorio.setAnno(preDoc.getProvvisorioDiCassa().getAnno());
		parametroRicercaProvvisorio.setNumero(preDoc.getProvvisorioDiCassa().getNumero());
		parametroRicercaProvvisorio.setTipoProvvisorio(preDoc.getProvvisorioDiCassa().getTipoProvvisorioDiCassa());
		ricercaProvvisoriDiCassa.setParametroRicercaProvvisorio(parametroRicercaProvvisorio);
		RicercaProvvisoriDiCassaResponse resRPC = provvisorioService.ricercaProvvisoriDiCassa(ricercaProvvisoriDiCassa);

		log.logXmlTypeObject(resRPC, "Risposta ottenuta dal servizio ricercaProvvisorioDiCassa.");
		checkServiceResponseFallimento(resRPC);

		return resRPC == null
			? null
			: resRPC.getElencoProvvisoriDiCassa().isEmpty()
				? null
				: resRPC.getElencoProvvisoriDiCassa().get(0);
	}
	

	/**
	 * checkProvvisorioDicassaAggiornamento()
	 * 
	 */
	protected void checkProvvisorioDicassaAggiornamento(){ 
	    String methodName="checkProvvisorioDicassa";
		if(preDoc.getProvvisorioDiCassa() == null || preDoc.getProvvisorioDiCassa().getUid()== 0 ){
			log.debug(methodName, "Provvisorio di cassa Non presente");
			return;
		}
		ProvvisorioDiCassa provvisorioDiCassa=preDoc.getProvvisorioDiCassa();
		String keyProvvisorio=provvisorioDiCassa.getAnno()+" - "+provvisorioDiCassa.getNumero();

		if(provvisorioDiCassa.getDataAnnullamento() !=null) {
			log.debug(methodName, "Provvisorio di cassa Annullato");
			throw new BusinessException(ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("aggiornamento","predisposizione di incasso",keyProvvisorio,"annullato"));
		}
        
		if(provvisorioDiCassa.getDataRegolarizzazione() !=null){
			log.debug(methodName, "Provvisorio di cassa regolarizzato Totalmente");
			throw new BusinessException(ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("aggiornamento","predisposizione di incasso",keyProvvisorio,"regolarizzato completamente"));
		}
		BigDecimal importoPrec= preDocumentoEntrataDad.findImportoPreDocumentoById(preDoc.getUid());
		if(!isProvvisorioDiCassaRegolarizzabileForAggiornamento(importoPrec)){
			log.debug(methodName, "Provvisorio di cassa Non regolarizzabile ");
			throw new BusinessException(ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("aggiornamento","predisposizione di pagamento",keyProvvisorio,"L'importo della predisposizione supera	l'importo da regolarizzare del provvisorio"));

		}
		

	}
	
	/**
	 * e' regolarizzabile se :
	 * importoDaRegolarizzare  (ProvvisoriodiCassa Mod FIN)  + importoPrec (PreDocumento di Entrata Mod FIN ) deve essere >= importo documento
     * 
	 * @param importoPrec
	 * @return
	 */
	
	private boolean isProvvisorioDiCassaRegolarizzabileForAggiornamento(BigDecimal importoPrec) {
		boolean res= false;
		
		if(preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare() == null || preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare().compareTo(BigDecimal.ZERO) == 0){
			return res;
		}
		BigDecimal totaleImportoDaRegolarizzareImportoPrec = importoPrec.add( preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare());
		int ris = totaleImportoDaRegolarizzareImportoPrec.compareTo(preDoc.getImporto());

		return ris >=0 ;
	}


	/**
	 * richiama la ricerca 
	 */
	protected void checkProvvisorioDicassaInserimento(){ 
	    String methodName="checkProvvisorioDicassa";
		if(preDoc.getProvvisorioDiCassa() == null || preDoc.getProvvisorioDiCassa().getUid()== 0 ){
			log.debug(methodName, "Provvisorio di cassa Non presente");
			return;
		}
		ProvvisorioDiCassa provvisorioDiCassa=preDoc.getProvvisorioDiCassa();
		String keyProvvisorio=provvisorioDiCassa.getAnno()+" - "+provvisorioDiCassa.getNumero();

		if(provvisorioDiCassa.getDataAnnullamento() !=null) {
			log.debug(methodName, "Provvisorio di cassa Annullato");
			throw new BusinessException(ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("inserimento","predisposizione di incasso",keyProvvisorio,"annullato"));
		}
        
		if(provvisorioDiCassa.getDataRegolarizzazione() !=null){
			log.debug(methodName, "Provvisorio di cassa regolarizzato Totalmente");
			throw new BusinessException(ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("inserimento","predisposizione di incasso",keyProvvisorio,"regolarizzato completamente"));
		}
		
		if(!isProvvisorioDiCassaRegolarizzabile()){
			log.debug(methodName, "Provvisorio di cassa Non regolarizzabile ");
			throw new BusinessException(ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("inserimento","predisposizione di incasso",keyProvvisorio,"L'importo della predisposizione supera	l'importo da regolarizzare del provvisorio"));

		}

	}
	/**
	 * controlla l'importo da regolarizzare del provvisorio di cassa che sia >= importo documento
	 * @param importo del documento
	 * @return true se l'importo da regolarizzare >= importo predocumento ==> provvisorio di cassa regolarizzabile
	 */
	private boolean isProvvisorioDiCassaRegolarizzabile(){
		String methodName = "isProvvisorioDiCassaRegolarizzabile";
		boolean res= false;
		if(preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare() == null || preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare().compareTo(BigDecimal.ZERO) ==0 ){
			return res;
		}
        log.debug(methodName, "importo da regolarizzare = "+preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare());
        log.debug(methodName, "importo documento  = "+preDoc.getImporto());
		
		int ris = preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare().compareTo(preDoc.getImporto());
		return ris >=0 ;
	}
    // fine parte aggiunta il 15/06/2015
}
