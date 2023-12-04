/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documento.MovimentoGestioneServiceCallGroup;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
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
public abstract class CrudPreDocumentoDiSpesaBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ,RES> {
	
	
	/** The pre documento spesa dad. */
	@Autowired
	protected PreDocumentoSpesaDad preDocumentoSpesaDad;
	
	/** The soggetto dad. */
	@Autowired
	protected SoggettoDad soggettoDad;
	
	/** The provvedimento dad. */
	@Autowired
	protected AttoAmministrativoDad attoAmministrativoDad;
	
	/** The movimento gestione service. */
	@Autowired
	protected MovimentoGestioneService movimentoGestioneService;
	
	/** The aggiorna stato pre documento di spesa service. */
	@Autowired(required = false)
	protected AggiornaStatoPreDocumentoDiSpesaService aggiornaStatoPreDocumentoDiSpesaService;
	
	/** The capitolo uscita gestione dad. */
	@Autowired
	protected CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;	
	
	/** The importi capitolo dad. */
	@Autowired
	protected ImportiCapitoloDad importiCapitoloDad;	
	
	/** The biancio dad. */
	@Autowired
	protected BilancioDad biancioDad;
	
	/** The ricerca dettaglio pre documento spesa service. */
	@Autowired
	private RicercaDettaglioPreDocumentoSpesaService ricercaDettaglioPreDocumentoSpesaService;
	
	/** The pre doc. */
	protected PreDocumentoSpesa preDoc;
	
	/** The bilancio. */
	protected Bilancio bilancio;
	
	/** The msg operazione. */
	protected String msgOperazione = "";
	
	/** The Provvisorio **/
	@Autowired
	protected ProvvisorioService provvisorioService;

	
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
		if(preDoc.getCapitoloUscitaGestione()==null || preDoc.getCapitoloUscitaGestione().getUid()==0){
			return;
		}
		
		CapitoloUscitaGestione capitolo = caricaCapitolo();				

		checkValiditaCapitolo(capitolo);
		checkDisponibilitaCapitolo(capitolo);
	}


	/**
	 * Carica capitolo.
	 *
	 * @return the capitolo uscita gestione
	 */
	private CapitoloUscitaGestione caricaCapitolo() {
		String methodName = "caricaCapitolo";
		RicercaDettaglioCapitoloUGest ricercaDettaglioCapitoloUGest = new RicercaDettaglioCapitoloUGest();
		ricercaDettaglioCapitoloUGest.setChiaveCapitolo(preDoc.getCapitoloUscitaGestione().getUid());	
		CapitoloUscitaGestione capitolo = capitoloUscitaGestioneDad.ricercaDettaglioCapitoloUscitaGestione(ricercaDettaglioCapitoloUGest);
		
		if(capitolo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitlo uscita gestione", "uid: "+preDoc.getCapitoloUscitaGestione().getUid()));
		}
		
		
		log.debug(methodName, "Capitolo trovato con uid: " + preDoc.getCapitoloUscitaGestione().getUid() + " desc: "+capitolo.getDescBilancioAnnoNumeroArticoloUEB());
		
		ImportiCapitoloUG importiCapitoloUG = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno(), ImportiCapitoloUG.class, EnumSet.of(ImportiCapitoloEnum.disponibilitaImpegnareAnno1));			
		capitolo.setImportiCapitoloUG(importiCapitoloUG);
		
		if(capitolo.getImportiCapitoloUG() == null  || capitolo.getImportiCapitoloUG().getDisponibilitaImpegnareAnno1()== null){
			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("Impossibile ottenere la disponibilita impegnare del capitolo."));
		}
		
		log.debug(methodName, "Capitolo con disponibilita a impegnare: " + capitolo.getImportiCapitoloUG().getDisponibilitaImpegnareAnno1());
		
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
		final String methodName = "checkSoggetto";
		log.debug(methodName, "il soggetto è null? " + preDoc.getSoggetto() == null);
		//il soggetto è facoltativo
		if(preDoc.getSoggetto()==null || preDoc.getSoggetto().getUid()==0){
			return;
		}
		
		log.debug(methodName, "uid soggetto: " +  preDoc.getSoggetto().getUid());
		StatoOperativoAnagrafica statoOperativoAnagrafica = soggettoDad.findStatoOperativoAnagraficaSoggetto(preDoc.getSoggetto());	
		
		if(!StatoOperativoAnagrafica.VALIDO.equals(statoOperativoAnagrafica)){
			throw new BusinessException(it.csi.siac.siacfinser.model.errore.ErroreFin.SOGGETTO_NON_VALIDO.getErrore(), Esito.FALLIMENTO);
		}
		if( StatoOperativoAnagrafica.BLOCCATO.equals(statoOperativoAnagrafica)){
			throw new BusinessException(ErroreFin.SOGGETTO_BLOCCATO.getErrore(), Esito.FALLIMENTO);
		}
	}
	
	
	/**
	 * Check congruenza soggetto pagamento.
	 */
	protected void checkCongruenzaSoggettoPagamento() {
		final String methodName = "checkCongruenzaSoggettoPagamento";
		log.debug(methodName, "il soggetto è null? " + (preDoc.getSoggetto() == null));
		log.debug(methodName, "l'impegno è null? " + (preDoc.getImpegno() == null));
		log.debug(methodName, "Il subimpegno è null? " + (preDoc.getSubImpegno() == null));
    	//il controllo viene effettuato solo se sono presenti sia soggetto sia impegno
		if (preDoc.getSoggetto()==null || preDoc.getSoggetto().getUid() == 0 || preDoc.getImpegno()==null || preDoc.getImpegno().getUid() ==0 ){
			return;
		}
		log.debug(methodName, "ho continuato il check con impegno " + preDoc.getImpegno().getUid() + " e soggetto " + preDoc.getSoggetto().getUid());
		Soggetto soggettoImpegno = obtainSoggettoMovimentoGestione();
		
		if (soggettoImpegno!=null && soggettoImpegno.getUid() != 0 && soggettoImpegno.getUid() != preDoc.getSoggetto().getUid()){
			throw new BusinessException(ErroreFin.SOGGETTO_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto pagamento","impegno"), Esito.FALLIMENTO);
		} else {
			checkValiditaClassiSoggetto();
		}
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
//			throw new BusinessException(ErroreFin.SOGGETTO_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto pagamento",
//				"impegno: il soggetto di pagamento deve appartenere alla classificazione"), Esito.FALLIMENTO);
		}
	}


	/**
     * Ottiene il soggetto collegato al movimento di gestione corretto.
     * 
     * @return il soggetto legato all'impegno o al subimpegno
     */
	private Soggetto obtainSoggettoMovimentoGestione() {
		Soggetto result = null;
		if(preDoc.getSubImpegno() != null && preDoc.getSubImpegno().getUid() != 0) {
			// Devo cercare per il subimpegno
			result = soggettoDad.findSoggettoByIdSubMovimentoGestione(preDoc.getSubImpegno().getUid());
		} else {
			result = soggettoDad.findSoggettoByIdMovimentoGestione(preDoc.getImpegno().getUid());
		}
		return result;
	}
	
	private List<ClasseSoggetto> obtainListClasseSoggettoBySoggetto() {
		List<ClasseSoggetto> result = soggettoDad.findClasseSoggetto(preDoc.getSoggetto().getUid());
		return result == null ? new ArrayList<ClasseSoggetto>() : result;
	}


	private ClasseSoggetto obtainClasseSoggettoByMovimentoGestione() {
		ClasseSoggetto result = null;
		if(preDoc.getSubImpegno() != null && preDoc.getSubImpegno().getUid() != 0) {
			result = soggettoDad.findClasseSoggettoByMovgestTs(preDoc.getSubImpegno().getUid());
		} else {
			result = soggettoDad.findClasseSoggettoByMovgest(preDoc.getImpegno().getUid());
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
    	AttoAmministrativo attoAmministrativo = attoAmministrativoDad.findProvvedimentoById(preDoc.getAttoAmministrativo().getUid());
    	
    	if ( StatoOperativoAtti.ANNULLATO.equals(attoAmministrativo.getStatoOperativoAtti())){
    		throw new BusinessException(ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore(), Esito.FALLIMENTO);
    	}
    	if ( !StatoOperativoAtti.DEFINITIVO.equals(attoAmministrativo.getStatoOperativoAtti())){
    		throw new BusinessException(it.csi.siac.siacfinser.model.errore.ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore(msgOperazione + " predisposizione di spesa","definitivo"), Esito.FALLIMENTO);
    	}
    }
    
    
	/**
	 * Check impegno.
	 */
	protected void checkImpegno(){
		final String methodName = "checkImpegno";
		log.debug(methodName, "l'impegno è null? " + preDoc.getImpegno() == null);

    	if (preDoc.getImpegno()!=null && preDoc.getImpegno().getUid()!=0){
    		
    		log.debug(methodName, "sto per ricercare l'impegno con uid " + preDoc.getImpegno().getUid());
    		
    		Impegno impegno = ricercaImpegnoPerChiaveOttimizzato();  		
    		preDoc.setImpegno(impegno);
    		
    		BigDecimal importoPrecedente;
    		
    		
    		//cerco il subimpegno, se mi è passato. Se lo trovo i controlli vengono effettuati sul subImpegno e non sull'impegno
    		if(preDoc.getSubImpegno()!= null && preDoc.getSubImpegno().getUid()!=0){
    			
    			log.debug(methodName, "sto per ricercare (e fare i check su ) il subimpegno con uid " + preDoc.getSubImpegno().getUid());
    			
    			SubImpegno subImpegno = null;
    			if(impegno.getElencoSubImpegni()!=null){
	    			for(SubImpegno subImp: impegno.getElencoSubImpegni()){
	    				if(subImp.getUid()==preDoc.getSubImpegno().getUid()){
	    					subImpegno = subImp;
	    				}
	    			}
    			}
    			if(subImpegno==null) {
        			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno",preDoc.getSubImpegno().getNumeroBigDecimal()+"/"+preDoc.getSubImpegno().getAnnoMovimento()+""), Esito.FALLIMENTO);
        		}
    			
    			//controllo validita' subimpegno
    			if(!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equalsIgnoreCase(subImpegno.getStatoOperativoMovimentoGestioneSpesa())){
        			throw new BusinessException(ErroreFin.MOVIMENTO_GESTIONE_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE.getErrore("subimpegno","definitivo","non può essere imputato ad una predisposizione di pagamento"), Esito.FALLIMENTO);
        		}
    			
    			//controllo congruenza tra anno subimpegno e anno bilancio
        		if (subImpegno.getAnnoMovimento()> bilancio.getAnno()){
        			throw new BusinessException(ErroreFin.MOVIMENTO_GESTIONE_PLURIENNALE_NON_AMMESSO_PER_OPERAZIONE.getErrore("subimpegno",msgOperazione + " predisposizione di pagamento"), Esito.FALLIMENTO);
        		}

        		//controllo congruenza tra importo predocumento e disponibilita' subimpegno
        		Integer uidSubImpegnoPrecedente = caricaUidSubImpegnoPrecedente();
        		if(uidSubImpegnoPrecedente == null || uidSubImpegnoPrecedente != subImpegno.getUid()){
        			importoPrecedente = BigDecimal.ZERO;
        		}else{
        			importoPrecedente = caricaImportoPrecedentePreDocumento();
        		}
	    		checkDisponibilitaImpegnoSubimpegno(subImpegno, preDoc.getImportoNotNull(), importoPrecedente);
        		
    		}else{
    		
    			log.debug(methodName, "effettuo i check sull'impegno con uid " + preDoc.getImpegno().getUid());
	    		//se non ho subimpegno effettuo controlli sull'impegno
	    		
	    		//controllo validita' impegno
	    		if(impegno.getElencoSubImpegni()!=null){
	    			for(SubImpegno subImpegno: impegno.getElencoSubImpegni()){
	    				if(!StatoOperativoMovimentoGestione.ANNULLATO.getCodice().equalsIgnoreCase(subImpegno.getStatoOperativoMovimentoGestioneSpesa())){
	    					throw new BusinessException(ErroreFin.IMPEGNO_CON_SUBIMPEGNI_VALIDI.getErrore(), Esito.FALLIMENTO);
	    				}
	    			}
	    		}else if(!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equalsIgnoreCase(impegno.getStatoOperativoMovimentoGestioneSpesa())){
	    			throw new BusinessException(ErroreFin.MOVIMENTO_GESTIONE_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE.getErrore("impegno","definitivo","non può essere imputato ad una predisposizione di pagamento"), Esito.FALLIMENTO);
	    			} 
	    		
	    		//controllo congruenza tra anno impegno e anno bilancio
	    		if (impegno.getAnnoMovimento()> bilancio.getAnno()){
	    			throw new BusinessException(ErroreFin.MOVIMENTO_GESTIONE_PLURIENNALE_NON_AMMESSO_PER_OPERAZIONE.getErrore("impegno",msgOperazione + " predisposizione di pagamento"), Esito.FALLIMENTO);
	    		}
	    		

	    		//controllo congruenza tra importo predocumento e disponibilita' impegno
	    		Integer uidImpegnoPrecedente = caricaUidImpegnoPrecedente();
	    		if(uidImpegnoPrecedente == null || uidImpegnoPrecedente != impegno.getUid()){
        			importoPrecedente = BigDecimal.ZERO;
        		}else{
        			importoPrecedente = caricaImportoPrecedentePreDocumento();
        		}
	    		checkDisponibilitaImpegnoSubimpegno(impegno, preDoc.getImportoNotNull(), importoPrecedente);
	    		
    	    }
    	}
    }
	
	
	private Integer caricaUidImpegnoPrecedente() {
		if(preDoc.getUid() == 0){
			return null;
		}
		return preDocumentoSpesaDad.findUidImpegnoPreDocumentoSpesaById(preDoc.getUid());
	}
	
	private Integer caricaUidSubImpegnoPrecedente() {
		if(preDoc.getUid() == 0){
			return null;
		}
		return preDocumentoSpesaDad.findUidSubImpegnoPreDocumentoSpesaById(preDoc.getUid());
	}


	protected BigDecimal caricaImportoPrecedentePreDocumento() {
		return preDocumentoSpesaDad.findImportoPreDocumentoById(preDoc.getUid());
	}
	
	protected BigDecimal caricaImportoPrecedentePredocumento(boolean isSubAccertamento) {
		BigDecimal importoPrecedente;
		if(isSubAccertamento){
			importoPrecedente = caricaImportoPrecedentePredocumentoBySubImpegno();
		} else {
			importoPrecedente = caricaImportoPrecedentePreDocumentoByImpegno();
		}
		return importoPrecedente!=null?importoPrecedente:BigDecimal.ZERO;
	}
	
	private BigDecimal caricaImportoPrecedentePreDocumentoByImpegno() {
		BigDecimal importoPrecedente;
		Integer uidImpegnoPrecedente = caricaUidImpegnoPrecedente();
		if(uidImpegnoPrecedente == null || uidImpegnoPrecedente.intValue() != preDoc.getImpegno().getUid()){
			importoPrecedente = BigDecimal.ZERO;
		}else{
			importoPrecedente = caricaImportoPrecedentePreDocumento();
		}
		return importoPrecedente;
	}
	
	private BigDecimal caricaImportoPrecedentePredocumentoBySubImpegno() {
		BigDecimal importoPrecedente;
		Integer uidSubImpegnoPrecedente = caricaUidSubImpegnoPrecedente();
		if(uidSubImpegnoPrecedente == null || uidSubImpegnoPrecedente.intValue() != preDoc.getSubImpegno().getUid()){
			importoPrecedente = BigDecimal.ZERO;
		}else{
			importoPrecedente = caricaImportoPrecedentePreDocumento();
		}
		return importoPrecedente;
	}


	private void checkDisponibilitaImpegnoSubimpegno(Impegno impegno, BigDecimal importoNuovo, BigDecimal importoPrecedente) {
		final String methodName = "checkDisponibilitaImpegnoSubimpegno";
		
		BigDecimal disponibilitaLiquidare = impegno.getDisponibilitaLiquidare() != null ? impegno.getDisponibilitaLiquidare() : BigDecimal.ZERO;
		importoNuovo = importoNuovo!=null? importoNuovo : BigDecimal.ZERO;
		importoPrecedente = importoPrecedente!=null? importoPrecedente : BigDecimal.ZERO;
		
		boolean isDisponibilitaSufficiente = disponibilitaLiquidare.add(importoPrecedente).compareTo(importoNuovo)>=0;
		
		log.info(methodName, "disponibilitaLiquidare: " + disponibilitaLiquidare + " + importoPrecedente: " + importoPrecedente 
				+ " >= importoNuovo: " + importoNuovo + " -> isDisponibilitaSufficiente: " + isDisponibilitaSufficiente);
		
		if(!isDisponibilitaSufficiente){
			throw new BusinessException(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore(msgOperazione + " predisposizione pagamento", impegno.getClass().getSimpleName()), Esito.FALLIMENTO);
		}
	}
	
	
	/**
	 * Ricerca impegno per chiave ottimizzato.
	 *
	 * @return the impegno
	 */
	protected Impegno ricercaImpegnoPerChiaveOttimizzato() {
		RicercaAttributiMovimentoGestioneOttimizzato parametri = new RicercaAttributiMovimentoGestioneOttimizzato();
		parametri.setEscludiSubAnnullati(true);
		parametri.setCaricaSub(preDoc.getSubImpegno()!=null && preDoc.getSubImpegno().getNumeroBigDecimal() != null);
	
		DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		parametriElencoIds.setEscludiAnnullati(true);
		parametri.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);

		RicercaImpegnoPerChiaveOttimizzatoResponse resRIPC = mgscg.ricercaImpegnoPerChiaveOttimizzatoCached(preDoc.getImpegno(), parametri, new DatiOpzionaliCapitoli(), preDoc.getSubImpegno());
		log.logXmlTypeObject(resRIPC, "Risposta ottenuta dal servizio RicercaImpegnoPerChiaveOttimizzato.");
	
		Impegno impegno = resRIPC.getImpegno();
		if(impegno==null) {
			res.addErrori(resRIPC.getErrori());
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", preDoc.getImpegno().getNumeroBigDecimal()+"/"+ preDoc.getImpegno().getAnnoMovimento()+""), Esito.FALLIMENTO);
		}
	
		return impegno;
	}  
	
	/**
	 * Aggiorna stato operativo pre documento.
	 *
	 * @param preDocumento the pre documento
	 * @param ricaricaDettaglioPreDocumento the ricarica dettaglio pre documento
	 * @return the stato operativo pre documento
	 */
	protected StatoOperativoPreDocumento aggiornaStatoOperativoPreDocumento(PreDocumentoSpesa preDocumento, boolean ricaricaDettaglioPreDocumento) {
		AggiornaStatoPreDocumentoDiSpesa reqAs = new AggiornaStatoPreDocumentoDiSpesa();
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setPreDocumentoSpesa(preDocumento);
		reqAs.setRicaricaDettaglioPreDocumento(ricaricaDettaglioPreDocumento);
		AggiornaStatoPreDocumentoDiSpesaResponse resAs = executeExternalService(aggiornaStatoPreDocumentoDiSpesaService, reqAs);
		return resAs.getPreDocumentoSpesa().getStatoOperativoPreDocumento();
	}
    
	
	/**
	 * Check validita capitolo.
	 *
	 * @param capitolo the capitolo
	 */
	private void checkValiditaCapitolo(CapitoloUscitaGestione capitolo){
		if(!StatoOperativoElementoDiBilancio.VALIDO.equals(capitolo.getStatoOperativoElementoDiBilancio())){
			throw new BusinessException(ErroreFin.CAPITOLO_NON_VALIDO_PER_OPERAZIONE.getErrore(capitolo.getStatoOperativoElementoDiBilancio(),"di spesa",msgOperazione,"predisposizione di pagamento"), Esito.FALLIMENTO);
		}
	}
	
	
	/**
	 * Check disponibilita capitolo.
	 *
	 * @param capitolo the capitolo
	 */
	private void checkDisponibilitaCapitolo(CapitoloUscitaGestione capitolo){
    	if (preDoc.getImpegno()==null 
    			&& capitolo.getImportiCapitoloUG().getDisponibilitaImpegnareAnno1().compareTo(preDoc.getImportoNotNull())<0) {
    		
    		throw new BusinessException(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore("Inserimento predisposizione pagamento", "capitolo"), Esito.FALLIMENTO);
    		
    	} 
    }
	
	/**
	 * Carica il dettaglio del predocumento.
	 */
	protected void caricaDettaglioPreDocumentoSpesa() {
		RicercaDettaglioPreDocumentoSpesa req = new RicercaDettaglioPreDocumentoSpesa();
		req.setDataOra(new Date());
		req.setPreDocumentoSpesa(preDoc);
		req.setRichiedente(this.req.getRichiedente());
		RicercaDettaglioPreDocumentoSpesaResponse res = executeExternalServiceSuccess(ricercaDettaglioPreDocumentoSpesaService,req);
		preDoc = res.getPreDocumentoSpesa();
	}
	
	// parte aggiunta il 16/06/2015 ahmad
	/**
	 * carica il provvisorio di cassa
	 */
	protected void caricaProvvisorioDiCassa() {
		if (preDoc.getProvvisorioDiCassa() == null || preDoc.getProvvisorioDiCassa().getNumero() == null || preDoc.getProvvisorioDiCassa().getAnno() == null) {
			return;
		}
		ProvvisorioDiCassa provvisorioDiCassa = ricercaProvvisorioDiCassa();
		if (provvisorioDiCassa == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("provvisorio di cassa", preDoc.getProvvisorioDiCassa().getAnno() + "/"
					+ preDoc.getProvvisorioDiCassa().getNumero()), Esito.FALLIMENTO);
		} else {
			log.debug("caricaProvvisorioDiCassa", "trovato provvisorio con uid: " + provvisorioDiCassa.getUid() + " e numero " + provvisorioDiCassa.getNumero());
		}
		preDoc.setProvvisorioDiCassa(provvisorioDiCassa);
	}

	
	private ProvvisorioDiCassa ricercaProvvisorioDiCassa() {
		RicercaProvvisoriDiCassa ricercaProvvisoriDiCassa = new RicercaProvvisoriDiCassa();
		ricercaProvvisoriDiCassa.setEnte(ente);
		ricercaProvvisoriDiCassa.setRichiedente(req.getRichiedente());
		//SIAC-7765
		ricercaProvvisoriDiCassa.setNumPagina(1);
		ricercaProvvisoriDiCassa.setNumRisultatiPerPagina(1);
		//
		
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
	protected void checkProvvisorioDicassaAggiornamento() {
		String methodName = "checkProvvisorioDicassa";
		if (preDoc.getProvvisorioDiCassa() == null || preDoc.getProvvisorioDiCassa().getUid() == 0) {
			log.debug(methodName, "Provvisorio di cassa Non presente");
			return;
		}
		ProvvisorioDiCassa provvisorioDiCassa = preDoc.getProvvisorioDiCassa();
		String keyProvvisorio = provvisorioDiCassa.getAnno() + " - " + provvisorioDiCassa.getNumero();

		if (provvisorioDiCassa.getDataAnnullamento() != null) {
			log.debug(methodName, "Provvisorio di cassa Annullato");
			throw new BusinessException(ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("aggiornamento", "predisposizione di pagamento", keyProvvisorio, "annullato"));
		}

		if (provvisorioDiCassa.getDataRegolarizzazione() != null) {
			log.debug(methodName, "Provvisorio di cassa regolarizzato Totalmente");
			throw new BusinessException(ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("aggiornamento", "predisposizione di pagamento", keyProvvisorio, "regolarizzato completamente"));
		}
		BigDecimal importoPrec = preDocumentoSpesaDad.findImportoPreDocumentoById(preDoc.getUid());
		if (!isProvvisorioDiCassaRegolarizzabileForAggiornamento(importoPrec)) {
			log.debug(methodName, "Provvisorio di cassa Non regolarizzabile ");
			throw new BusinessException(ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("aggiornamento", "predisposizione di pagamento", keyProvvisorio,
					"L’importo della predisposizione supera	l’importo da regolarizzare del provvisorio"));

		}

	}

	/**
	 * e' regolarizzabile se : importoDaRegolarizzare (ProvvisoriodiCassa Mod
	 * FIN) + importoPrec (PreDocumento di Entrata Mod FIN ) deve essere >=
	 * importo documento
	 * 
	 * @param importoPrec
	 * @return
	 */

	private boolean isProvvisorioDiCassaRegolarizzabileForAggiornamento(BigDecimal importoPrec) {
		boolean res = false;
		if (preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare() == null || preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare().compareTo(BigDecimal.ZERO) ==0) {
			return res;
		}
		BigDecimal totaleImportoDaRegolarizzareImportoPrec = importoPrec.add(preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare());
		int ris = totaleImportoDaRegolarizzareImportoPrec.compareTo(preDoc.getImporto());
		return ris >=0;
	}

	/**
	 * richiama la ricerca
	 */
	protected void checkProvvisorioDicassaInserimento() {
		String methodName = "checkProvvisorioDicassa";
		if (preDoc.getProvvisorioDiCassa() == null || preDoc.getProvvisorioDiCassa().getUid() == 0) {
			log.debug(methodName, "Provvisorio di cassa Non presente");
			return;
		}
		ProvvisorioDiCassa provvisorioDiCassa = preDoc.getProvvisorioDiCassa();
		String keyProvvisorio = provvisorioDiCassa.getAnno() + " - " + provvisorioDiCassa.getNumero();

		if (provvisorioDiCassa.getDataAnnullamento() != null) {
			log.debug(methodName, "Provvisorio di cassa Annullato");
			throw new BusinessException(ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("inserimento", "predisposizione di incasso", keyProvvisorio, "annullato"));
		}

		if (provvisorioDiCassa.getDataRegolarizzazione() != null) {
			log.debug(methodName, "Provvisorio di cassa regolarizzato Totalmente");
			throw new BusinessException(ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("inserimento", "predisposizione di incasso", keyProvvisorio, "regolarizzato completamente"));
		}

		if (!isProvvisorioDiCassaRegolarizzabile()) {
			log.debug(methodName, "Provvisorio di cassa Non regolarizzabile ");
			throw new BusinessException(ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("inserimento", "predisposizione di pagamento", keyProvvisorio,
					"L’importo della predisposizione supera	l’importo da regolarizzare del provvisorio"));

		}

	}

	/**
	 * controlla l'importo da regolarizzare del provvisorio di cassa che sia >=
	 * importo documento
	 * 
	 * @param importo
	 *            del documento
	 * @return true se l'importo da regolarizzare >= importo predocumento ==>
	 *         provvisorio di cassa regolarizzabile
	 */
	private boolean isProvvisorioDiCassaRegolarizzabile() {
		String methodName = "isProvvisorioDiCassaRegolarizzabile";
		boolean res = false;
		if (preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare() == null || preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare().compareTo(BigDecimal.ZERO) == 0) {
			return res;
		}
        log.debug(methodName, "importo da regolarizzare = "+preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare());
        log.debug(methodName, "importo documento  = "+preDoc.getImporto());
		int ris = preDoc.getProvvisorioDiCassa().getImportoDaRegolarizzare().compareTo(preDoc.getImporto());
		return ris >=0;
	}



}
