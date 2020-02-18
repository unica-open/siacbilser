/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.integration.dad.CausaleDad;
import it.csi.siac.siacbilser.integration.dad.NaturaOnereDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.dad.TipoOnereDad;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.ModelloCausale;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaTipoOnereService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaTipoOnereService extends CheckedAccountBaseService<AggiornaTipoOnere,AggiornaTipoOnereResponse>{
	
	private TipoOnere tipoOnere;
	List<Causale> listaCausali = new ArrayList<Causale>();
	
	@Autowired
	private TipoOnereDad tipoOnereDad;
	@Autowired
	private CausaleDad causaleDad;
	@Autowired
	private SoggettoDad soggettoDad;
	@Autowired
	private NaturaOnereDad naturaOnereDad;

	private MovimentoGestioneServiceCallGroup movimentoGestioneServiceCallGroup;
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		tipoOnere = req.getTipoOnere();
		checkEntita(tipoOnere, "tipo onere");
		tipoOnere.setEnte(ente);
		
		checkCondition(req.getAnnoEsercizio() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"));
		checkEntita(tipoOnere.getNaturaOnere(), "natura onere");
		checkCondition(tipoOnere.getCodice() != null && StringUtils.isNotBlank(tipoOnere.getCodice()),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo onere"));
		checkCondition(tipoOnere.getDescrizione() != null && StringUtils.isNotBlank(tipoOnere.getDescrizione())
				,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione tipo onere"));
		checkCondition(tipoOnere.getDescrizione().length()<=200,  ErroreCore.FORMATO_NON_VALIDO.getErrore("descrizione tipo onere",": lunghezza massima 200 caratteri"));
		checkCondition(tipoOnere.getAliquotaCaricoEnte() == null || tipoOnere.getAliquotaCaricoEnte().signum()>0, ErroreCore.FORMATO_NON_VALIDO.getErrore("aliquota a carico ente",": deve essere non negativo"));
		checkCondition(tipoOnere.getAliquotaCaricoSoggetto() == null || tipoOnere.getAliquotaCaricoSoggetto().signum()>0,  ErroreCore.FORMATO_NON_VALIDO.getErrore("aliquota a carico soggetto",": deve essere non negativo"));
		checkCondition(tipoOnere.getAliquotaCaricoSoggetto() == null ||tipoOnere.getAliquotaCaricoEnte() == null ||
				tipoOnere.getAliquotaCaricoSoggetto().add(tipoOnere.getAliquotaCaricoEnte()).subtract(BilUtilities.BIG_DECIMAL_ONE_HUNDRED).signum()<=0
				,  ErroreCore.FORMATO_NON_VALIDO.getErrore("aliquota a carico soggetto o aliquota a carico ente",": la somma delle aliquote deve essere minore o uguale a 100"));
		
//		for(Causale c : tipoOnere.getCausali()){
//			c.setEnte(ente);
//		}
	}
	
	@Override
	protected void init() {
		tipoOnereDad.setLoginOperazione(loginOperazione);
		tipoOnereDad.setEnte(ente);
		
		causaleDad.setEnte(ente);
		causaleDad.setLoginOperazione(loginOperazione);
		
		Bilancio bilancio = new Bilancio();
		bilancio.setAnno(req.getAnnoEsercizio());
		movimentoGestioneServiceCallGroup = new MovimentoGestioneServiceCallGroup(serviceExecutor, req.getRichiedente(), ente, bilancio);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public AggiornaTipoOnereResponse executeService(AggiornaTipoOnere serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkSplitReverseSeNaturaCorrispondente();
		checkCausaliSpesa();
		checkCausaliEntrata();
		
		popolaCausaliSpesa();
		popolaCausaliEntrata();
		
		TipoOnere onere = tipoOnereDad.aggiornaTipoOnere(tipoOnere);
		res.setTipoOnere(onere);
	}
	
	/**
	 * Controlla se i dati dello split/reverse sono stati popolati nel caso in cui la natura sia corrispondente
	 */
	private void checkSplitReverseSeNaturaCorrispondente() {
		NaturaOnere naturaOnere = naturaOnereDad.findNaturaOnereByUid(tipoOnere.getNaturaOnere().getUid());
		if(naturaOnere == null) {
			// Sarebbe bello NON usare l'uid
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Natura onere", "uid: " + tipoOnere.getNaturaOnere().getUid()));
		}
		
		// Se il tipo e' SP oppure ES il campo deve essere valorizzato
		if((naturaOnere.isSplitReverse() || "ES".equals(naturaOnere.getCodice())) && tipoOnere.getTipoIvaSplitReverse() == null) {
			throw new BusinessException(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipo IVA split / reverse"));
		}
		
		// Se la natura ha codice ES (ESENZIONE) => il tipo deve essere Esenzione
		if("ES".equals(naturaOnere.getCodice()) && !TipoIvaSplitReverse.ESENZIONE.equals(tipoOnere.getTipoIvaSplitReverse())) {
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("tipo IVA split / reverse",
					": se la natura onere e' ES deve essere " + TipoIvaSplitReverse.ESENZIONE.getDescrizione() + ". Valore fornito " + tipoOnere.getTipoIvaSplitReverse().getDescrizione()));
		}
		
		tipoOnere.setNaturaOnere(naturaOnere);
	}

	
	private void checkCausaliSpesa() {
		final String methodName = "checkCausaliSpesa";
		for(CausaleSpesa causaleSpesa : tipoOnere.getCausaliSpesa()){
			if( causaleSpesa.getDataFineValiditaCausale() == null && causaleSpesa.getDataScadenza() == null){
				log.debug(methodName, "causale spesa codice: " + causaleSpesa.getCodice());
				checkSoggettoPagamento(causaleSpesa);
				checkImpegnoOttimizzato(causaleSpesa);
				checkCongruenzaSoggettoImpegno(causaleSpesa);
				checkCongruenzaCapitoloImpegno(causaleSpesa);
			}
		}		
	}
	
	

	private void checkCausaliEntrata() {
		final String methodName = "checkCausaliEntrata";
		for(CausaleEntrata causaleEntrata : tipoOnere.getCausaliEntrata()){
			if(causaleEntrata.getDataFineValiditaCausale() == null && causaleEntrata.getDataScadenza() == null){
				log.debug(methodName, "causale entrata codice: " + causaleEntrata.getCodice());
				checkAccertamentoOttimizzato(causaleEntrata);
				checkCongruenzaCapitoloAccertamento(causaleEntrata);
			}
		}		
	}

	

	private void popolaCausaliSpesa() {
		
		TipoCausale tipoCausale= causaleDad.ricercaTipoCausaleVersamento();
		int index = 1;
		for(CausaleSpesa causaleSpesa : tipoOnere.getCausaliSpesa()){
			
			if(causaleSpesa.getUid()==0){ //Se la causale e' nuova imposto i dati
				
				causaleSpesa.setTipoCausale(tipoCausale);
				causaleSpesa.setCodice("CAUS_S_"+ tipoOnere.getCodice() +"_"+ index);
				causaleSpesa.setDescrizione("Causale spesa per impegno " +
					(causaleSpesa.getImpegno() != null ? causaleSpesa.getImpegno().getAnnoMovimento() + "/" + causaleSpesa.getImpegno().getNumero() : "null")
				);
				causaleSpesa.setModelloCausale(ModelloCausale.ONERI);
				index++;
			}
		}
		
	}
	
	
	private void popolaCausaliEntrata() {
		
		TipoCausale tipoCausale= causaleDad.ricercaTipoCausaleRitenuta();
		
		int index = 1;
		for(CausaleEntrata causaleEntrata : tipoOnere.getCausaliEntrata()){
			
			if(causaleEntrata.getUid()==0){ //Se la causale e' nuova imposto i dati
				
				causaleEntrata.setTipoCausale(tipoCausale);
				causaleEntrata.setCodice("CAUS_E_"+ tipoOnere.getCodice() +"_"+ index);
				causaleEntrata.setDescrizione("Causale entrata per accertamento " +
					(causaleEntrata.getAccertamento() != null ? causaleEntrata.getAccertamento().getAnnoMovimento() + "/" + causaleEntrata.getAccertamento().getNumero() : "null")
				);
				causaleEntrata.setModelloCausale(ModelloCausale.ONERI);
				index++;
			}
		}
		
	}
	

	
	/*
	 * Il sistema verifica che il soggetto selezionato per il pagamento rispetti le seguenti condizioni
		Non sia in  stato ANNULLATO , il sistema emette il seguente messaggio:
		<FIN_ERR_0007.Soggetto Annullato>
		Non sia in  stato BLOCCATO , il sistema emette il seguente messaggio:
		<FIN_ERR_0094.Soggetto Bloccato>

	 */
	private void checkSoggettoPagamento(CausaleSpesa causaleSpesa) {
		if(causaleSpesa.getSoggetto() == null || causaleSpesa.getSoggetto().getUid() == 0){
			return;
		}
		StatoOperativoAnagrafica statoOperativoSoggetto = soggettoDad.findStatoOperativoAnagraficaSoggetto(causaleSpesa.getSoggetto().getUid());
		//TODO caricaSoggetto
		if(StatoOperativoAnagrafica.ANNULLATO.equals(statoOperativoSoggetto)){
			throw new BusinessException(ErroreFin.SOGGETTO_ANNULLATO.getErrore());
		}
		if(StatoOperativoAnagrafica.BLOCCATO.equals(statoOperativoSoggetto)){
			throw new BusinessException(ErroreFin.SOGGETTO_BLOCCATO.getErrore());
		}
		
	}
	
	/*
	 * Sono richiesti solo movimenti di gestione  in stato operativo DEFINITIVO oppure relativi sub, sempre in stato
	 * operativo DEFINITIVO (puo' capitare di avere il movimento di gestione in stato 'N – definitivo non utilizzabile' 
	 * ed i sub in stato DEFINITIVO).
	 * 
	 * Se l'operatore seleziona un movimento di gestione con sub NON ANNULLATI e' fornito il seguente messaggio al fine di bloccarne la selezione:
	 * Per l'entrata <FIN_ERR-0043 - Accertamento con subaccertamenti >
	 * Per la spesa  <FIN_ERR_0231 - Impegno con subimpegni validi >
	 */
	private void checkImpegnoOttimizzato(CausaleSpesa causaleSpesa) {
		final String methodName = "checkImpegno";
		//se non ho impegno o non ho dati sufficienti per la ricerca, esco
		if(causaleSpesa.getImpegno() == null || causaleSpesa.getImpegno().getNumero() == null || causaleSpesa.getImpegno().getAnnoMovimento() == 0){
			log.debug(methodName, "non ho impegno o non ho dati sufficienti per la ricerca, esco");
			return;
		}
		//carico l'impegno
		RicercaImpegnoPerChiaveOttimizzatoResponse resRIPC  = ricercaImpegnoPerChiaveOttimizzato(causaleSpesa);
		Impegno impegno = resRIPC.getImpegno();
		log.debug(methodName, "dalla ricerca per chiave ho trovato impegno con uid: " + impegno.getUid());
		causaleSpesa.setImpegno(impegno);
		
		if(causaleSpesa.getSubImpegno() != null){
			if(impegno.getElencoSubImpegni() ==null || impegno.getElencoSubImpegni().isEmpty()){
				throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("subimpegno", causaleSpesa.getSubImpegno().getNumero()+""), Esito.FALLIMENTO);

			}
			
			log.debug(methodName, "il subimpegno non è null, ne ricerco i dettagli");
			//se ho il subimpegno lo popolo cercandolo tra l'elenco dei subimpegni dell'impegno appena caricato
			SubImpegno subimpegno = impegno.getElencoSubImpegni().get(0);
			log.debug(methodName, "ho trovato il subimpegno cnon uid: " + subimpegno.getUid());
			causaleSpesa.setSubImpegno(subimpegno);
			//controllo che il subimpegno sia in stato definitivo
			log.debug(methodName, "stato operativo subimpegno: " + subimpegno.getStatoOperativoMovimentoGestioneSpesa());
			if(!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(subimpegno.getStatoOperativoMovimentoGestioneSpesa())){
				throw new BusinessException(ErroreFin.SUBIMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore(""));
			}
			
		}else{
			//controllo che l'impegno sia in stato definitivo o definitivo non liquidabile
			log.debug(methodName, "stato operativo impegno: " + impegno.getStatoOperativoMovimentoGestioneSpesa());
			if(!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa())
				&& !StatoOperativoMovimentoGestione.DEFINITIVO_NON_LIQUIDABILE.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa())){
				throw new BusinessException(ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore(""));
			}
			//controllo che l'impegno non abbia subimpegni validi
			if(resRIPC.getElencoSubImpegniTuttiConSoloGliIds() != null && !resRIPC.getElencoSubImpegniTuttiConSoloGliIds().isEmpty()){
				for(SubImpegno subimp : resRIPC.getElencoSubImpegniTuttiConSoloGliIds()){
					if(!StatoOperativoMovimentoGestione.ANNULLATO.getCodice().equals(subimp.getStatoOperativoMovimentoGestioneSpesa())){
						throw new BusinessException(ErroreFin.IMPEGNO_CON_SUBIMPEGNI_VALIDI.getErrore());
					}
				}
			}
		}
		
		
	}

	private void checkAccertamentoOttimizzato(CausaleEntrata causaleEntrata) {
		final String methodName = "checkAccertamento";
		if(causaleEntrata.getAccertamento() == null || causaleEntrata.getAccertamento().getUid() == 0){
			log.debug(methodName, "la causale non ha accertamento, esco");
			return;
		}
		//se non ho accertamento o non ho dati sufficienti per la ricerca, esco
		if(causaleEntrata.getAccertamento() == null || causaleEntrata.getAccertamento().getNumero() == null || causaleEntrata.getAccertamento().getAnnoMovimento() == 0){
			log.debug(methodName, "non ho accertamento o non ho dati sufficienti per la ricerca, esco");
			return;
		}
		RicercaAccertamentoPerChiaveOttimizzatoResponse resRAPCO = ricercaAccertamentoPerChiaveOttimizzato(causaleEntrata);
		//carico l'accertamento
		Accertamento accertamento = resRAPCO.getAccertamento();
		log.debug(methodName, "dalla ricerca per chiave ho trovato accertamento con uid: " + accertamento.getUid());
		causaleEntrata.setAccertamento(accertamento);
		
		if(causaleEntrata.getSubAccertamento() != null){
			if(accertamento.getElencoSubAccertamenti() ==null || accertamento.getElencoSubAccertamenti().isEmpty()){
				throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("subaccertamento", causaleEntrata.getSubAccertamento().getNumero()+""), Esito.FALLIMENTO);
			}
			
			log.debug(methodName, "il subaccertamento non è null, ne ricerco i dettagli");
			SubAccertamento subaccertamento = accertamento.getElencoSubAccertamenti().get(0);
			log.debug(methodName, "ho trovato il subaccertamento con uid: " + subaccertamento.getUid());
			causaleEntrata.setSubAccertamento(subaccertamento);
			log.debug(methodName, "stato operativo subaccertamento: " + subaccertamento.getStatoOperativoMovimentoGestioneEntrata());
			if(!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(subaccertamento.getStatoOperativoMovimentoGestioneEntrata())){
				throw new BusinessException(ErroreFin.SUBACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(""));
			}
		}else{
			log.debug(methodName, "stato operativo accertamento: " + accertamento.getStatoOperativoMovimentoGestioneEntrata());
			if(!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata())
				&& !StatoOperativoMovimentoGestione.DEFINITIVO_NON_LIQUIDABILE.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata())){
				throw new BusinessException(ErroreFin.ACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(""));
			}
			if(resRAPCO.getElencoSubAccertamentiTuttiConSoloGliIds() != null && !resRAPCO.getElencoSubAccertamentiTuttiConSoloGliIds().isEmpty()){
				for(SubAccertamento subacc : resRAPCO.getElencoSubAccertamentiTuttiConSoloGliIds()){
					if(!StatoOperativoMovimentoGestione.ANNULLATO.getCodice().equals(subacc.getStatoOperativoMovimentoGestioneEntrata())){
						throw new BusinessException(ErroreFin.ACCERTAMENTO_CON_SUBACCERTAMENTI.getErrore());
					}
				}
			}
		}
	}
	//Se già presente il capitolo, il movimento di gestione deve appartenere al capitolo indicato. 
	//Altrimenti inviare un errore.
	private void checkCongruenzaCapitoloImpegno(CausaleSpesa causaleSpesa) {
		final String methodName = "checkCongruenzaCapitoloImpegno";
		if(causaleSpesa.getImpegno() == null || causaleSpesa.getImpegno().getUid() == 0){
			log.debug(methodName, "la causale non ha impegno, esco");
			return;
		}
		if(causaleSpesa.getCapitoloUscitaGestione() == null 
			||causaleSpesa.getCapitoloUscitaGestione().getUid() == 0
			||causaleSpesa.getImpegno().getCapitoloUscitaGestione() == null 
			||causaleSpesa.getImpegno().getCapitoloUscitaGestione().getUid() == 0){
			log.debug(methodName, "non ho il capitolo della causale o il capitolo dell'impegno, esco");
			return;
		}
		log.debug(methodName, "uid capitolo della causale: " + causaleSpesa.getCapitoloUscitaGestione().getUid());
		log.debug(methodName, "uid capitolo dell'impegno: " + causaleSpesa.getImpegno().getCapitoloUscitaGestione().getUid());
		if(causaleSpesa.getCapitoloUscitaGestione().getUid() != causaleSpesa.getImpegno().getCapitoloUscitaGestione().getUid()){
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("impegno della causale e impegno del capitolo"));
		}
	}
	
	//Se già presente il capitolo, il movimento di gestione deve appartenere al capitolo indicato. Altrimenti inviare un errore.
	private void checkCongruenzaCapitoloAccertamento(CausaleEntrata causaleEntrata) {
		final String methodName = "checkCongruenzaCapitoloAccertamento";
		if(causaleEntrata.getAccertamento() == null || causaleEntrata.getAccertamento().getUid() == 0){
			log.debug(methodName, "la causale non ha accertamento, esco");
			return;
		}
		if(causaleEntrata.getCapitoloEntrataGestione() == null 
			||causaleEntrata.getCapitoloEntrataGestione().getUid() == 0
			||causaleEntrata.getAccertamento().getCapitoloEntrataGestione() == null 
			||causaleEntrata.getAccertamento().getCapitoloEntrataGestione().getUid() == 0){
			log.debug(methodName, "non ho il capitolo della causale o il capitolo dell'accertamento, esco");
			return;
		}
		log.debug(methodName, "uid capitolo della causale: " + causaleEntrata.getCapitoloEntrataGestione().getUid());
		log.debug(methodName, "uid capitolo dell'accertamento: " + causaleEntrata.getAccertamento().getCapitoloEntrataGestione().getUid());
		if(causaleEntrata.getCapitoloEntrataGestione().getUid() != causaleEntrata.getAccertamento().getCapitoloEntrataGestione().getUid()){
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("impegno della causale e impegno del capitolo"));
		}
	}

	/*
	 * Se gia'  presente un soggetto il movimento di gestione o sub scelto deve rispettare la coerenza:
	 * - se associato ad un soggetto deve essere lo stesso
	 * - se associato ad una classe, il soggetto deve appartenere a questa classe
	 * In caso contrario inviare un messaggio di errore.
	 * 
	 * Ovvero l'impegno o subimpegno, deve rispettare le seguenti condizioni di congruenza rispetto al proprio soggetto e quello
	 * indicato nella causale:
	 * 
	 * impegni e subimpegni: se associati ad un soggetto specifico, deve essere lo stesso indicato per il Tipo Onere
	 * impegni: se associati ad una classificazione (Classe Soggetto mod FIN) questa  deve essere compatibile con il soggetto
	 *     indicato per il Tipo Onere, ovvero quest'ultimo deve essere associato alla classificazione stessa
	 *     
	 * Se il soggetto non e' congruente è fornito il seguente messaggio di errore <FIN_ERR_0282 - Soggetto Movimento Gestione
	 * non valido per incogruenza, 'soggetto', <movimentoGestione>,'per la causale di pagamento', <messaggio aggiuntivo>> dove
	 * <movimentoGestione> = 'impegno' se il movimento scelto e' un impegno (Impegno mod FIN) diversamente 'subimpegno' (SubImpegno mod FIN)
	 * <messaggio aggiuntivo> = impegno o subimpegno con soggetto specifico: 'il soggetto deve essere lo stesso del <movimentoGestione>'
	 * impegno con classificazione: 'il soggetto deve appartenere alla classificazione'.
	 */
	private void checkCongruenzaSoggettoImpegno(CausaleSpesa causaleSpesa) {
		final String methodName = "checkCongruenzaSoggettoImpegno";
		if(causaleSpesa.getImpegno() == null || causaleSpesa.getImpegno().getUid() == 0){
			log.debug(methodName, "la causale non ha impegno, esco");
			return;
		}
		if(causaleSpesa.getSoggetto() == null || causaleSpesa.getSoggetto().getUid() == 0){
			log.debug(methodName, "la causale non ha soggetto, esco");
			return;
		}
		Soggetto soggettoCausale = causaleSpesa.getSoggetto();
		log.debug(methodName, "uid soggetto causale: " + soggettoCausale.getUid());
		
		//popolo il soggetto (o eventulmente la classe) a partire dall'impegno o dal subimpegno
		Soggetto soggettoMovGest = null;
		ClasseSoggetto classeSoggetto = null;
		if(causaleSpesa.getSubImpegno() != null && causaleSpesa.getSubImpegno().getUid() != 0){
			soggettoMovGest = causaleSpesa.getSubImpegno().getSoggetto();
			log.debug(methodName, "soggetto subimpegno: " + soggettoMovGest.getUid());
		}else{
			soggettoMovGest = causaleSpesa.getImpegno().getSoggetto();
			log.debug(methodName, soggettoMovGest != null ? " uid soggetto impegno: " + soggettoMovGest.getUid() : "SOGGETTO NULL");
			classeSoggetto = causaleSpesa.getImpegno().getClasseSoggetto();
			log.debug(methodName, classeSoggetto != null ? " codice classe soggetto: " + classeSoggetto.getCodice() : "CLASSE SOGGETTO NULL");
		}
		
		//controllo il soggetto specifico se l'impegno(o il subimpegno) ne ha uno
		if(soggettoMovGest != null && soggettoMovGest.getUid() != 0 && soggettoMovGest.getUid() != soggettoCausale.getUid()){
			throw new BusinessException(ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore(" soggetto", 
				" l'impegno o il subimpegno", " per la causale di pagamento", "impegno o subimpegno con soggetto specifico : il soggetto deve essere lo stesso dell'impegno o del subimpegno."));
		}
		
		//controllo la classe soggetto se l'impegno quand l'impegno è associato ad una classe
		if(classeSoggetto != null){
			
			List<ClasseSoggetto> listaClasseSoggettoSoggetto = soggettoDad.findClasseSoggetto(soggettoCausale.getUid());
			if(listaClasseSoggettoSoggetto == null) {
				throw new BusinessException(ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore(" soggetto", 
						" l'impegno o il subimpegno", " per la causale di pagamento", "impegno con classificazione : il soggetto deve appartenere alla classificazione."));
			}
			for(ClasseSoggetto cs : listaClasseSoggettoSoggetto) {
				log.debug(methodName, "classe soggetto: " + cs.getCodice());
				if(cs.getCodice().equals(classeSoggetto.getCodice())) {
					log.debug(methodName, "ho trovato la classe a cui appartiene il soggetto!!! " + classeSoggetto.getCodice());
					return;
				}
			}
			throw new BusinessException(ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore(" soggetto", 
			    " l'impegno o il subimpegno", " per la causale di pagamento", "impegno con classificazione : il soggetto deve appartenere alla classificazione."));
		}
		
	}
	
	/**
	 * Ricerca impegno per chiave ottimizzato.
	 *
	 * @return the impegno
	 */
	protected RicercaImpegnoPerChiaveOttimizzatoResponse ricercaImpegnoPerChiaveOttimizzato(CausaleSpesa causaleSpesa) {
		RicercaAttributiMovimentoGestioneOttimizzato parametri = new RicercaAttributiMovimentoGestioneOttimizzato();
		parametri.setEscludiSubAnnullati(false);
		parametri.setCaricaSub(causaleSpesa.getSubImpegno()!=null && causaleSpesa.getSubImpegno().getNumero() != null);
	
		DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		parametriElencoIds.setEscludiAnnullati(false);
		parametri.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);
		parametri.setSubPaginati(true);//per la paginazione dei sub
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		// Non richiedo NESSUN importo derivato.
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		// Non richiedo NESSUN classificatore
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.noneOf(TipologiaClassificatore.class));
	
		RicercaImpegnoPerChiaveOttimizzatoResponse resRIPC = movimentoGestioneServiceCallGroup.ricercaImpegnoPerChiaveOttimizzatoCached(causaleSpesa.getImpegno(), parametri, datiOpzionaliCapitoli, causaleSpesa.getSubImpegno());
		log.logXmlTypeObject(resRIPC, "Risposta ottenuta dal servizio RicercaImpegnoPerChiaveOttimizzato.");
	
		Impegno impegno = resRIPC.getImpegno();
		if(impegno==null) {
			res.addErrori(resRIPC.getErrori());
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", causaleSpesa.getImpegno().getNumero()+"/"+ causaleSpesa.getImpegno().getAnnoMovimento()+""), Esito.FALLIMENTO);
		}
		//Se il sub e' presente = ho effettuato la ricerca per sub
		if(causaleSpesa.getSubImpegno()!=null && causaleSpesa.getSubImpegno().getNumero() != null 
				&& (impegno.getElencoSubImpegni() == null || impegno.getElencoSubImpegni().isEmpty())){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("subimpegno", causaleSpesa.getSubImpegno().getNumero()+""), Esito.FALLIMENTO);
	
		}
	
		return resRIPC;
	}
	
	/**
	 * ricercaAccertamentoPerChiaveOttimizzato
	 * @param causaleEntrata
	 * @return
	 */
	private RicercaAccertamentoPerChiaveOttimizzatoResponse ricercaAccertamentoPerChiaveOttimizzato(CausaleEntrata causaleEntrata) {
		
	RicercaAttributiMovimentoGestioneOttimizzato parametri = new RicercaAttributiMovimentoGestioneOttimizzato();
	parametri.setEscludiSubAnnullati(false);
	parametri.setCaricaSub(causaleEntrata.getSubAccertamento()!=null && causaleEntrata.getSubAccertamento().getNumero() != null);
			
	DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
	parametriElencoIds.setEscludiAnnullati(false);
	parametri.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);
	parametri.setSubPaginati(true);
	
	DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
	datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
	
	RicercaAccertamentoPerChiaveOttimizzatoResponse resRAPC = movimentoGestioneServiceCallGroup.ricercaAccertamentoPerChiaveOttimizzato(causaleEntrata.getAccertamento(), parametri, datiOpzionaliCapitoli, causaleEntrata.getSubAccertamento());
	log.logXmlTypeObject(resRAPC, "Risposta ottenuta dal servizio RicercaAccertamentoPerChiaveOttimizzato.");
	checkServiceResponseFallimento(resRAPC);
	
	Accertamento accertamento = resRAPC.getAccertamento();
	if(accertamento==null) {
		res.addErrori(resRAPC.getErrori());
		throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", causaleEntrata.getAccertamento().getNumero()+""+ causaleEntrata.getAccertamento().getAnnoMovimento()+""), Esito.FALLIMENTO);
	}
	
	//Se il sub e' presente = ho effettuato la ricerca per sub
	if(causaleEntrata.getSubAccertamento()!=null && causaleEntrata.getSubAccertamento().getNumero() != null 
			&& (accertamento.getElencoSubAccertamenti() == null || accertamento.getElencoSubAccertamenti().isEmpty())){
		throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("subaccertamento", causaleEntrata.getSubAccertamento().getNumero()+""), Esito.FALLIMENTO);
	}
	return resRAPC;
 }
	
}
