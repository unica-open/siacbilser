/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.attiamministrativi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.VerificaAnnullabilitaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.VerificaAnnullabilitaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siacbilser.business.service.provvedimento.AggiornaProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.InserisceProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.VerificaAnnullabilitaProvvedimentoService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siaccommonser.business.service.base.cache.KeyAdapter;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.SistemaEsterno;
import it.csi.siac.siacintegser.business.service.attiamministrativi.model.AttoAmministrativoElab;


/*
 * 
 * @author Nazha Ahmad
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/07/2015
 * @version 1.1.0 - 11/11/2015 - CR-2547
 * @version 1.1.1 - 04/12/2015 - JIRA 2655, modifica gestione stato aggiornamento
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraAttoAmministrativoService extends CheckedAccountBaseService<ElaboraAttoAmministrativo, ElaboraAttoAmministrativoResponse> {
	
	//DADs
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private EnteDad enteDad;
	@Autowired
	private AttoAmministrativoDad attoAmministrativoDad;
	
	//Services
	@Autowired
	protected ClassificatoreService classificatoreService;
	
	protected AttoAmministrativoElab attoAmministrativo;
	
	/**
	 * Indica se l'attoAmministrativo va trattato come ESECUTIVO.
	 */
	private Boolean isEsecutivo;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAttoAmministrativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto amministrativo"));
		attoAmministrativo = req.getAttoAmministrativo();
	}
	
	@Override
	protected void init() {
		attoAmministrativoDad.setEnte(ente);
		
		this.isEsecutivo = null;
	}
	
	@Override
	protected void execute() {
		
		checkTipoDiGestione();// tipo di elaborazione
		checkAndSetEnte();
		checkDatiChiave();
		valorizzaBilancioFromEnte();
		checkTipoAttoPerEnte();
		checkCDRAndCDC();
		checkStatoOperativo();
		checkAnnoAtto();
		ricercaAttoAmministrativo();

		inserisciAggiornaAttoAmministrativo();

	}

	/**
	 * <font color='blue'><b>Imposta il messaggio di successo della fine operazione</b></font><br />
	 * @param tipoOperazione (aggiornamento ,inserimento ,annullamento)
	 */
	private void impostaMessaggioSuccesso(String tipoOperazione) {
		final String methodName = "impostaMessaggioSuccesso";
		String key = calcolaChiaveAttoAmministrativo(attoAmministrativo);
		String msg = "L'operazione e' stata completata con successo: " + tipoOperazione + " Atto Amministrativo " + key;

		log.info(methodName, "Elaborazione file Atti Amministrativi: " + msg);
		res.getMessaggi().add(new Messaggio("CRU_CON_2001", msg));
	}

	/**
	 * <font color='blue'><b> Inserisce o aggiorna l'atto amministrativo  </b></font><br />
	 * richiama i metodi di aggiornamento o di inserimento del provvedimento 
	 */
	private void inserisciAggiornaAttoAmministrativo() {
		String methodName = "inserisciAggiornaAttoAmministrativo";
		log.info(methodName, 
				new StringBuilder("Elaborazione file Atti Amministrativi: ")
					.append("atto").append(calcolaChiaveAttoAmministrativoCompleta(attoAmministrativo))
					.append(" ; tipo elaborazione: ").append(attoAmministrativo.getTipoDiVariazione())
					.append(" ; isAttoDaAggiornare: ").append(attoAmministrativo.isAttoAmministrativoDaAggiornare())
					.append(" ; stato di input : ").append(attoAmministrativo.getStatoOperativoInput())
				.toString());

		String tipoOperazione;
		if (attoAmministrativo.isAttoAmministrativoDaAggiornare()) {
			// AGGIORNARE
			verificaCompatibilitaTipoDiOperazionePerAggiornamento();
			verificaEdEseguiAggiornamentoInBaseAlloStatoOperativo();
			tipoOperazione = isStatoAnnullato(attoAmministrativo.getStatoOperativo()) ? "annullamento" : "aggiornamento";
		} else {
			inserisciAttoAmministrativo();
			tipoOperazione = "inserimento";
		}
		
		impostaMessaggioSuccesso(tipoOperazione);
	}
	/**
	 * <font color='blue'><b> Verifica tipo di operazione per aggiornamento  </b></font><br />
	 *  Tipo di operazione deve essere uguale a 'V'
	 */
	private void verificaCompatibilitaTipoDiOperazionePerAggiornamento() {
		checkBusinessConditionNotToBeSatisfied(!attoAmministrativo.isTipoDiVariazioneVariazione(),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("Tipo di Gestione = I", ": l'atto " 
					+ calcolaChiaveAttoAmministrativoCompleta(attoAmministrativo) + " e' gia' presente, l'elaborazione deve essere di tipo U"));
	}

	/**
	 * <font color='blue'><b> Verifica tipo di operazione per Inserimento  </b></font><br />
	 *  Tipo di operazione deve essere uguale a 'I'
	 */
	private void verificaCompatibilitaTipoDiOperazionePerInserimento() {
		checkBusinessConditionNotToBeSatisfied(!attoAmministrativo.isTipoDiVariazioneInserimento(),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("Tipo di Gestione = U", ": l'atto " 
						+ calcolaChiaveAttoAmministrativoCompleta(attoAmministrativo) + " non e' ancora presente, l'elaborazione deve essere di tipo I"));
	}
	/**
	 * Punto 7 della'analisi elaborazioneTracciatoAttiAmministrativi<br /> 
	 * <font color='blue'><b>Verifica Ed esegue l'aggiornamento  dell'atto In base al suo stato Operativo  </b></font><br />
	 * Se lo stato dell'Atto in archivio &eacute; Annullato
	 * <ul>
	 *     <li>memorizzare sull'elemento esaminato il messaggio <code>&lt;ATT_ERR_0002, Provvedimento Annullato&lt;Chiave provvedimento&gt;&gt;</code></li>
	 *     <li>Proseguire l'elaborazione dell'elemento successivo<br/></li>
	 * </ul>
	 * Se lo stato dell'Atto in archivio &eacute; Definitivo
	 * <ul>
	 *     <li> se lo Stato nel flusso &eacute; ANNULLATO passare alla Verifica Annullabilit &aacute;(step successivo)</li>
	 *     <li> se lo Stato flusso &eacute; PROVVISORIO
	 *         <ul>
	 *             <li> memorizzare sull'elemento esaminato il messaggio &lt;ATT_INF_0004, Il provvedimento &eacute; gi&aacute; in stato definitivo&lt;Chiave provvedimento&gt;&gt;</li>
	 *             <li> Proseguire l'elaborazione dell'elemento successivo</li>
	 *         </ul>
	 *     </li>
	 *     <li>se lo Stato flusso è DEFINITIVO proseguire con lo step Aggiorna Atto</li>
	 * </ul>
	 * Se lo stato dell'Atto in archivio &eacute; Provvisorio
	 * <ul>
	 *     <li>se il nuovo Stato &eacute; Annullato passare alla Verifica Annullabilit&agrave;</li>
	 *     <li>negli altri casi passare a Aggiorna Atto</li>
	 * </ul>
	 */
	private void verificaEdEseguiAggiornamentoInBaseAlloStatoOperativo() {

		if (isStatoAnnullato(attoAmministrativo.getStatoOperativo())) {
			throw new BusinessException(ErroreAtt.PROVVEDIMENTO_E_GIA_ANNULLATO.getErrore(calcolaChiaveAttoAmministrativo(attoAmministrativo)), Esito.FALLIMENTO);
		}

		// SIAC-2547: controllo che l'aggiornamento sia possibile
		controllaAggiornamentoNoConflitto();
		
		if (isStatoDefinitivo(attoAmministrativo.getStatoOperativo())) {
			eseguiAggiornamentoPerStatoDefinitivo();
			return;
		}

		if (isStatoProvvisorio(attoAmministrativo.getStatoOperativo())) {
			eseguiAggiornamentoPerStatoProvvisorio();
			return;
		}
		
	}

	/**
	 * Aggiornamento puo' creare conflitti, andando a trovare un provvedimento che &eacute; gi&agrave; presente in archivio.
	 * <br/>
	 * Lanciare in tal caso un'eccezione ed uscire
	 */
	private void controllaAggiornamentoNoConflitto() {
		List<String> chunks = new ArrayList<String>();
		chunks.add(attoAmministrativo.getAnno() + "");
		chunks.add(attoAmministrativo.getNumero() + "");
		if(attoAmministrativo.getTipoAtto() != null) {
			chunks.add(attoAmministrativo.getTipoAtto().getCodice());
		}
		if(attoAmministrativo.getStrutturaAmmContabile() != null) {
			chunks.add(attoAmministrativo.getStrutturaAmmContabile().getCodice());
		}
		String key = StringUtils.join(chunks, "/");
		
		Long count = attoAmministrativoDad.countByAnnoAndNumeroAndTipoAndSACAndNotUid(attoAmministrativo);
		checkBusinessConditionNotToBeSatisfied(count == null, ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore nel reperimento dei provvedimenti per chiave " + key));
		checkBusinessConditionNotToBeSatisfied(count.longValue() > 0L, ErroreCore.ENTITA_PRESENTE.getErrore("Provvedimento", key));
	}


	/**
	 *<font color='blue'><b>Esegue i controlli ed l'aggiornamento dell'atto quando il suo stato operativo nell'archivio &egrave; PROVVISORIO</b></font><br/>
	 * <ul>
	 *     <li>se il nuovo Stato &eacute; Annullato passare alla Verifica Annullabilit&agrave;</li>
	 *     <li>negli altri casi passare a Aggiorna Atto<br/></li>
	 * </ul>
	 */
	private void eseguiAggiornamentoPerStatoProvvisorio() {
		String methodName = "eseguiAggiornamentoPerStatoProvvisorio";
		String key = calcolaChiaveAttoAmministrativo(attoAmministrativo);
		if(!isStatoAnnullato(attoAmministrativo.getStatoOperativoInput())) {
			aggiornaAttoAmministrativo();
			return;
		}
		if (isAttoAnnullabile(attoAmministrativo)) {
			log.debug(methodName, "Atto Amministrativo " + key + " annullabile");
			// SIAC-2600: forzo lo stato ad annullato
			aggiornaAttoAmministrativo();
			return;
		}
		log.info(methodName, "Atto Amministrativo " + key + " Non annullabile");
		throw new BusinessException(ErroreAtt.PROVVEDIMENTO_CON_MOVIMENTI_NON_ANNULLABILE.getErrore(key), Esito.FALLIMENTO);
	}

	/**
	 * <font color='blue'><b>Calcola la chiave per l'atto amministrativo</b></font><br/>
	 * la chiave &egrave; composta da anno/numero.
	 *
	 * @param attoAmministrativo the atto amministrativo
	 * @return la chiave dell'atto
	 */
	private String calcolaChiaveAttoAmministrativoCompleta(AttoAmministrativoElab attoAmministrativo) {
		if(attoAmministrativo == null) {
			return "nessun parametroo di ricerca impostato";
		}
		return  new StringBuilder()
				.append(calcolaChiaveAttoAmministrativo(attoAmministrativo))
				.append("/")
				.append(attoAmministrativo.getTipoAtto() != null? StringUtils.defaultIfBlank(attoAmministrativo.getTipoAtto().getCodice(), "N.D.") : "null")
				.append("/")
				.append(attoAmministrativo.getStrutturaAmmContabile() != null? StringUtils.defaultIfBlank(attoAmministrativo.getStrutturaAmmContabile().getCodice(), "N.D.") : "null")
				.toString();
				
	}
	
	
	/**
	 *<font color='blue'><b>Calcola la chiave per l'atto amministrativo</b></font><br/>
	 * la chiave &egrave; composta da anno/numero
	 * @param attoAmministrativo
	 * @return la chiave dell'atto
	 */
	private String calcolaChiaveAttoAmministrativo(AttoAmministrativoElab attoAmministrativo) {
		return attoAmministrativo == null ? "null" : attoAmministrativo.getAnno() + "/" + attoAmministrativo.getNumero() + "/" + attoAmministrativo.getStatoOperativo();
	}

	/**
	 * <font color='blue'><b>Esegue i controlli ed l'aggiornamento dell'atto quando il suo stato operativo nell'archivio &egrave; DEFINITIVO</b></font><br/>
	 * Se lo stato dell'Atto in archivio &eacute; Definitivo
	 * <ul>
	 *     <li>se lo Stato nel flusso &egrave; ANNULLATO passare alla Verifica Annullabilit&agrave;(step successivo)</li>
	 * </ul>
	 * Se lo stato dell'Atto in archivio &eacute; Provvisorio
	 * <ul>
	 *     <li>memorizzare sull'elemento esaminato il messaggio &lt;ATT_ERR_0004,Il provvedimento &egrave; gi&agrave in stato definitivoChiave provvedimento&gt;&gt;</li>
	 *     <li>Proseguire l'elaborazione dell’elemento successivo<br/></li>
	 * </ul>
	 * Se lo Stato flusso &egrave; DEFINITIVO proseguire con lo step Aggiorna Atto
	 */
	private void eseguiAggiornamentoPerStatoDefinitivo() {
		String methodName = "eseguiAggiornamentoPerStatoDefinitivo";
		String key = calcolaChiaveAttoAmministrativo(attoAmministrativo);
		String keyForLog = attoAmministrativo.getAnno() + "/" + attoAmministrativo.getNumero();

		if (isStatoAnnullato(attoAmministrativo.getStatoOperativoInput())) {
			log.debug(methodName, "Stato operativo Atto " + keyForLog + " passato nel flusso e' Annullato e lo stato dell'atto sull'archivio e' DEFINITIVO verifico l'annullabilita");
			if (isAttoAnnullabile(attoAmministrativo)) {
				log.debug(methodName, "Atto " + keyForLog + " e' annullabile proseguo con l'aggiornamento");
				aggiornaAttoAmministrativo();
				return;
			}
			log.debug(methodName, "Atto " + keyForLog + " Non Annullabile ");
			throw new BusinessException(ErroreAtt.PROVVEDIMENTO_CON_MOVIMENTI_NON_ANNULLABILE.getErrore(key), Esito.FALLIMENTO);
		}

		if (isStatoProvvisorio(attoAmministrativo.getStatoOperativoInput())) {
			log.debug(methodName, "Atto " + keyForLog + " e' gia in stato Definitivo ");
			throw new BusinessException(ErroreAtt.PROVVEDIMENTO_E_GIA_IN_STATO_DEFINITIVO.getErrore(key), Esito.FALLIMENTO);
		}

		if (isStatoDefinitivo(attoAmministrativo.getStatoOperativoInput())) {
			log.debug(methodName, "Stato Operarivo dell'Atto " + keyForLog + " passato nel  flusso e' DEFINITIVO ed e' uguale a quello presente in archivio. Proseguo con l'aggiornamento");
			aggiornaAttoAmministrativo();
			return;
		}
	}

	
	/**
	 * <font color='blue'><b>Controlla se un determinato atto amministrativo  &egrave; annullabile </b></font><br/>
	 * richiama il methodo {@link #verificaAnnullabilita(AttoAmministrativoElab)}
	 * @param attoAmministrativo
	 * @return <code>true</code> se l'atto &eacute; annullabile
	 */
	private boolean isAttoAnnullabile(AttoAmministrativoElab attoAmministrativo) {
		VerificaAnnullabilitaProvvedimentoResponse response = verificaAnnullabilita(attoAmministrativo);
		return Boolean.TRUE.equals(response.getAnnullabilitaAttoAmministrativo());
	}

	/**
	 * <font color='blue'><b>Controlla se un determinato atto amministrativo  &egrave; annullabile </b></font><br/>
	 * richiama il servizio di <b>verificaAnnullabilitaProvvedimentoService</b>
	 * @param attoAmministrativo
	 * @return la response di verifica annullabilit&agrave;
	 */
	private VerificaAnnullabilitaProvvedimentoResponse verificaAnnullabilita(AttoAmministrativoElab attoAmministrativo) {
		VerificaAnnullabilitaProvvedimento request = new VerificaAnnullabilitaProvvedimento();
		request.setEnte(attoAmministrativo.getEnte());
		request.setRichiedente(req.getRichiedente());
		request.setAttoAmministrativo(attoAmministrativo);
		
		Class<? extends VerificaAnnullabilitaProvvedimentoService> verificaAnnullabilitaProvvedimentoServiceClass = 
				getVerificaAnnullabilitaProvvedimentoServiceClass();
		return serviceExecutor.executeServiceSuccess(verificaAnnullabilitaProvvedimentoServiceClass, request);
	}

	protected Class<? extends VerificaAnnullabilitaProvvedimentoService> getVerificaAnnullabilitaProvvedimentoServiceClass() {
		return VerificaAnnullabilitaProvvedimentoService.class;
	}

	/**
	 * <font color='blue'><b>Inserisce un atto amministrativo</b></font><br/>
	 * richiama il servizio di <b>InserisceProvvedimentoService</b>
	 * @return la response di inserimento del provvedimento
	 */
	private InserisceProvvedimentoResponse inserisciAttoAmministrativo() {
		final String methodName = "inserisciAttoAmministrativo";
		verificaCompatibilitaTipoDiOperazionePerInserimento();
		
		InserisceProvvedimento request = new InserisceProvvedimento();
		request.setDataOra(new Date());
		request.setRichiedente(req.getRichiedente());
		request.setEnte(attoAmministrativo.getEnte());
		request.setAttoAmministrativo(attoAmministrativo);
		request.setStrutturaAmministrativoContabile(attoAmministrativo.getStrutturaAmmContabile());
		request.setTipoAtto(attoAmministrativo.getTipoAtto());
		
		InserisceProvvedimentoResponse res = serviceExecutor.executeServiceSuccess(InserisceProvvedimentoService.class, request);
		log.info(methodName, "Elaborazione file Atti Amministrativi: inserito atto con uid " + res.getAttoAmministrativoInserito().getUid());

		return res;
	}

	/**
	 * <font color='blue'><b>Aggiorna un atto amministrativo</b></font><br/>
	 * richiama il servizio di <b>AggiornaProvvedimentoService</b>
	 */
	protected AggiornaProvvedimentoResponse aggiornaAttoAmministrativo() {
		final String methodName = "aggiornaAttoAmministrativo";
		AggiornaProvvedimento request = new AggiornaProvvedimento();
		request.setDataOra(new Date());
		request.setRichiedente(req.getRichiedente());
		request.setEnte(attoAmministrativo.getEnte());
		request.setAttoAmministrativo(attoAmministrativo);
		request.setStrutturaAmministrativoContabile(attoAmministrativo.getStrutturaAmmContabile());
		request.setTipoAtto(attoAmministrativo.getTipoAtto());
		request.setIsEsecutivo(isEsecutivo);
		
		// JIRA-2655: imposto lo stato di aggiornamento
		String newStato = attoAmministrativo.getStatoOperativoInput();
		StatoOperativoAtti soa = StatoOperativoAtti.fromString(newStato);
		attoAmministrativo.setStatoOperativo(soa); 
		
		Class<? extends AggiornaProvvedimentoService> aggiornaProvvedimentoServiceClass = getAggiornaProvvedimentoServiceClass();
		
		AggiornaProvvedimentoResponse res = serviceExecutor.executeServiceSuccess(aggiornaProvvedimentoServiceClass, request);
		log.info(methodName, "Elaborazione file Atti Amministrativi: aggiornato atto con uid " + res.getAttoAmministrativoAggiornato().getUid());

		return res;
	}

	protected Class<? extends AggiornaProvvedimentoService> getAggiornaProvvedimentoServiceClass() {
		return AggiornaProvvedimentoService.class;
	}

	
	/**
	 * <font color='blue'><b>Controlla l'anno dell'atto</b></font><br/>
	 * <ul>
	 *     <li>se l'anno non  &egrave; valorizzato o &egrave uguale a zero lancia l'errore</li>
	 * </ul>
	 */
	private void checkAnnoAtto() {
		checkParametroValorizzato(attoAmministrativo.getAnno() == 0 , "Anno Atto");
	}

	/**
	 * <font color='blue'><b>popola la request In base ai parametri valorizzati e richiama il servizio di ricerca atto amministrativo</b></font><br/>
	 * <ul>
	 *     <li>Se tipo variazione = <code>Insert</code>, si verifica l'esistenza dell'Atto su SIAC accedendo per Anno + Tipo Atto + numero  Atto + CDR e CDC (ove presenti per Ente)</li>
	 *     <li>Se tipo variazione = <code>Update</code>, si verifica l’esistenza dell'Atto su SIAC accedendo per Anno chiave + Tipo Atto chiave + numero Atto chiave + CDR e CDC chiave (ove presenti per Ente)</li>
	 * </ul>
	 * (i dati da utilizzare per la ricerca sono, come da specifica del tracciato, quelli chiave &ndash; dal campo  posizione 1 a posizione 48)
	 */
	private void ricercaAttoAmministrativo() {
		String methodName = "ricercaAttoAmministrativo";
		
		AttoAmministrativo attAmministrativoFromRicerca = null;
		
		if(attoAmministrativo.isTipoDiVariazioneInserimento()) {
			log.debug(methodName, "Inserimento. Cerco i dati che devo ancora inserire");
			AttoAmministrativo attoPerRicerca = new AttoAmministrativo();
			attoPerRicerca.setEnte(attoAmministrativo.getEnte());
			attoPerRicerca.setAnno(attoAmministrativo.getAnno());
			attoPerRicerca.setNumero(attoAmministrativo.getNumero());
			attoPerRicerca.setTipoAtto(attoAmministrativo.getTipoAtto());
			attoPerRicerca.setStrutturaAmmContabile(attoAmministrativo.getStrutturaAmmContabile());
			
			log.info(methodName, getStringaLogParametriRicerca(attoPerRicerca));
			
			attAmministrativoFromRicerca = attoAmministrativoDad.findAttoAmministrativoByAnnoAndNumeroAndTipo(attoPerRicerca);
		} else if(attoAmministrativo.isTipoDiVariazioneVariazione()) {
			AttoAmministrativo attoPerRicerca = new AttoAmministrativo();
			attoPerRicerca.setEnte(attoAmministrativo.getEnte());
			attoPerRicerca.setAnno(attoAmministrativo.getAnnoAttoChiave());
			attoPerRicerca.setNumero(attoAmministrativo.getNumeroAttoChiave());
			attoPerRicerca.setTipoAtto(attoAmministrativo.getTipoAttoChiave());
			attoPerRicerca.setStrutturaAmmContabile(attoAmministrativo.getStrutturaAmmContabileChiave());
			
			log.info(methodName, getStringaLogParametriRicerca(attoPerRicerca));
			
			attAmministrativoFromRicerca = attoAmministrativoDad.findAttoAmministrativoByAnnoAndNumeroAndTipo(attoPerRicerca);
		}
		if (attAmministrativoFromRicerca != null && attAmministrativoFromRicerca.getUid() != 0) {
			log.info(methodName, "atto amministrativo trovato su base dati");
			attoAmministrativo.setUid(attAmministrativoFromRicerca.getUid());
			attoAmministrativo.setStatoOperativo(attAmministrativoFromRicerca.getStatoOperativo());
		}
		

	}

	private String getStringaLogParametriRicerca(AttoAmministrativo attoPerRicerca) {
		return new StringBuilder()
				.append("Cerco l'atto con i seguenti parametri di ricerca. Anno: ")
				.append(attoPerRicerca.getAnno())
				.append(", numero: ")
				.append(attoPerRicerca.getNumero())
				.append(", tipo atto : ")
				.append(attoPerRicerca.getTipoAtto() != null? 
						(attoPerRicerca.getTipoAtto().getUid() + " - " + StringUtils.defaultIfBlank(attoPerRicerca.getTipoAtto().getCodice(), "N.D.")) 
						: "null" )
				.append(", struttura amministrativo contabile: ")
				.append(attoPerRicerca.getStrutturaAmmContabile() != null? 
						(attoPerRicerca.getStrutturaAmmContabile().getUid() + " - " + StringUtils.defaultIfBlank(attoPerRicerca.getStrutturaAmmContabile().getCodice(), "N.D.")) 
						: "null" )
				.toString();
	}

	/**
	 *<font color='blue'><b>Effettua il check Sullo Stato Operativo</b></font><br/>
	 * i valori ammessi Provvisorio('P'), Definitivo('D'), Annullato('A')
	 */
	private void checkStatoOperativo() {
		// controllo se e' valorizzato ---> ! null
		String statoOperativo = attoAmministrativo.getStatoOperativo();
		
		checkParametroValorizzato(statoOperativo == null, "Stato Operativo");
		// superato il check
		// controllo ora se i valori sono ammessibili
		boolean codiceStatoOperativoValido = statoOperativo != null && (
				        "P".equals(statoOperativo) 
						|| "D".equals(statoOperativo) 
						|| "A".equals(statoOperativo)
						|| "E".equals(statoOperativo));
		checkValoreParametroValido(!codiceStatoOperativoValido, "Stato Operativo");
		// ora che lo stato dal file ha il formato giusto bisogna prendere la
		// codifica giusta dalla tabella siac_r_atto_amm_stato_operativo_in_out
		valorizzaCodiceStatoOutput();
	}

	/**
	 *<font color='blue'><b>ottiene il codice stato da SIAC (ogni ente ha i suoi codici)</b></font><br/>
	 * @table tabella siac_r_atto_amm_stato_operativo_in_out
	 */
	private void valorizzaCodiceStatoOutput() {
		String methodName = "valorizzaCodiceStatoOutput";
		// ottengo la codifica dello stato operativo
		valorizzaStatoOperativoAttoFromFlusso();
		String codiceStato = attoAmministrativoDad.findCodiceStatoOut(attoAmministrativo);
		attoAmministrativo.setStatoOperativoInput(codiceStato);
		checkValiditaEntita(codiceStato.isEmpty(), "Codice Stato");
		log.debug(methodName, "Codice Stato operativo out (From SIAC) :" + codiceStato);
	}
	
	/**
	 *<font color='blue'><b>Popola  il codice corretto  dello stato a partire dal codice passato da input</b></font><br/>
	 */
	private void valorizzaStatoOperativoAttoFromFlusso() {
		String methodName = "valorizzaStatoOperativoAttoFromFlusso";
		StatoOperativoAtti statoOperativoAtti = null;
		char codiceStato = attoAmministrativo.getStatoOperativo().charAt(0);
		log.debug(methodName, "Codice Stato From Input " + codiceStato);

		switch (codiceStato) {
			case 'P':
				statoOperativoAtti = StatoOperativoAtti.PROVVISORIO;
				break;
			case 'A':
				statoOperativoAtti = StatoOperativoAtti.ANNULLATO;
				break;
			case 'D':
				statoOperativoAtti = StatoOperativoAtti.DEFINITIVO;
				break;
			case 'E':
				statoOperativoAtti = StatoOperativoAtti.DEFINITIVO; //LO stato rimane DEFINITIVO..
				this.isEsecutivo = Boolean.TRUE; //...ma mi segno che si deve gestire come ESECUTIVO.
				break;
			default:
				throw new BusinessException(ErroreCore.FORMATO_NON_VALIDO.getErrore("Stato operativo", "valore " + attoAmministrativo.getStatoOperativo() + " non supportato. Valori consentiti: P, A, D, E."));
		}
		
		attoAmministrativo.setStatoOperativo(statoOperativoAtti);
	}

	/**
	 * <font color='blue'><b> VERIFCA CDR E CDC quando vengono passati da input se sono presenti, le SAC dell'ente </b></font><br/>
	 * richiama il servizio <b>leggiStrutturaAmministrativaContabile</b><br/>
	 * richiama il METHODO <b>checkCDRAndCDCNellaListaSac</b>
	 * 
	 */
	private void checkCDRAndCDC() {
		// richiamo il servizio solo una volta
		LeggiStrutturaAmminstrativoContabileResponse resp = leggiStrutturaAmministrativaContabile();
		List<StrutturaAmministrativoContabile> listaSAC = resp.getListaStrutturaAmmContabile();
		checkCDRAndCDCNellaListaSac(listaSAC);
	}

	/**
	 * <font color='blue'><b> Controllo CDR e CDC </b></font><br/>
	 * @param listaSAC la lista delle SAC
	 */
	private void checkCDRAndCDCNellaListaSac(List<StrutturaAmministrativoContabile> listaSAC) {
		String methodName = "checkCDRAndCDCNellaListaSac";
		
		if(listaSAC.isEmpty()) {
			log.debug(methodName, "La lista delle SAC e' vuota");
			return;
		}
		
		StrutturaAmministrativoContabile sacCentroDiResponsabilita = findCDR(listaSAC, attoAmministrativo.getSacCentroDiResponsabilita());
		StrutturaAmministrativoContabile sacCentroDiCosto = findCDC(sacCentroDiResponsabilita, attoAmministrativo.getSacCentroDiCosto());
		StrutturaAmministrativoContabile sacCentroDiResponsabilitaChiave = findCDR(listaSAC, attoAmministrativo.getSacCentroDiResponsabilitaChiave());
		StrutturaAmministrativoContabile sacCentroDiCostoChiave = findCDC(sacCentroDiResponsabilitaChiave, attoAmministrativo.getSacCentroDiCostoChiave());

		// Imposto la nuova SAC
		attoAmministrativo.setStrutturaAmmContabile(sacCentroDiCosto == null ? sacCentroDiResponsabilita : sacCentroDiCosto);
		attoAmministrativo.setStrutturaAmmContabileChiave(sacCentroDiCostoChiave == null ? sacCentroDiResponsabilitaChiave : sacCentroDiCostoChiave);
		
		// Controllo che le SAC siano presenti se valorizzate
		checkSAC(attoAmministrativo.getSacCentroDiResponsabilita(), attoAmministrativo.getSacCentroDiCosto(), attoAmministrativo.getStrutturaAmmContabile(), null);
		checkSAC(attoAmministrativo.getSacCentroDiResponsabilitaChiave(), attoAmministrativo.getSacCentroDiCostoChiave(), attoAmministrativo.getStrutturaAmmContabileChiave(), "chiave");
		
		attoAmministrativo.setSacCentroDiResponsabilita(sacCentroDiResponsabilita);
		attoAmministrativo.setSacCentroDiCosto(sacCentroDiCosto);
		attoAmministrativo.setSacCentroDiResponsabilitaChiave(sacCentroDiResponsabilitaChiave);
		attoAmministrativo.setSacCentroDiCostoChiave(sacCentroDiCostoChiave);
	}
	
	/**
	 * Controlla la SAC
	 * @param cdr il Centro Di Responsabilit&agrave;
	 * @param cdc il Centro Di Costo
	 * @param sac la Struttura Amministrativo Contabile
	 * @param suffix il suffisso
	 */
	private void checkSAC(StrutturaAmministrativoContabile cdr, StrutturaAmministrativoContabile cdc, StrutturaAmministrativoContabile sac, String suffix) {
		if(cdr == null && cdc == null) {
			// Sono a posto
			return;
		}
		List<String> chunks1 = new ArrayList<String>();
		List<String> chunks2 = new ArrayList<String>();
		if(cdr != null && StringUtils.isNotBlank(cdr.getCodice())) {
			chunks1.add("CDR");
			chunks2.add(cdr.getCodice());
		}
		if(cdc != null && StringUtils.isNotBlank(cdc.getCodice())) {
			chunks1.add("CDC");
			chunks2.add(cdc.getCodice());
		}
		StringBuilder sb = new StringBuilder()
			.append(StringUtils.join(chunks1, ", "))
			.append(suffix != null ? " " + suffix : "");
		checkBusinessConditionNotToBeSatisfied(sac == null || sac.getUid() == 0, ErroreCore.VALORE_NON_CONSENTITO.getErrore(sb.toString(), ": " + StringUtils.join(chunks2, ", ")));
	}

	/**
	 * <font color='blue'><b>Reperisco il CDR</b></font><br/>
	 * @param listaSAC la lista delle SAC
	 * @param centroDiResponsabilita il CDR da reperire
	 * @return il CDR corretto
	 */
	private StrutturaAmministrativoContabile findCDR(List<StrutturaAmministrativoContabile> listaSAC, StrutturaAmministrativoContabile centroDiResponsabilita) {
		final String methodName = "findCDR";
		if(centroDiResponsabilita == null || StringUtils.isBlank(centroDiResponsabilita.getCodice())) {
			return null;
		}
		log.debug(methodName, "Codice CDR PASSATO : " + centroDiResponsabilita.getCodice());
		for (StrutturaAmministrativoContabile sa : listaSAC) {
			if (centroDiResponsabilita.getCodice().equals(sa.getCodice())) {
				log.debug(methodName, "SAC trovata CON uid " + sa.getUid() + ", Codice CDR " + sa.getCodice());
				return sa;
			}
		}
		throw new BusinessException(ErroreCore.VALORE_NON_CONSENTITO.getErrore("centro di responsabilita'", ": il centro con codice " + centroDiResponsabilita.getCodice() + " non e' presente su base dati"));
	}
	
	/**
	 * <font color='blue'><b>Reperisco il CDC</b></font><br/>
	 * @param centroDiResponsabilita il CDR da cui reperire il CDC
	 * @param centroDiCosto il CDC da reperire
	 * @return il CDC corretto
	 */
	private StrutturaAmministrativoContabile findCDC(StrutturaAmministrativoContabile centroDiResponsabilita, StrutturaAmministrativoContabile centroDiCosto) {
		final String methodName = "findCDC";
		if(centroDiResponsabilita == null || centroDiCosto == null || StringUtils.isBlank(centroDiCosto.getCodice())) {
			return null;
		}
		log.debug(methodName, "Codice CDR PASSATO : " + centroDiResponsabilita.getCodice() + " - Codice CDC PASSATO: " + centroDiCosto.getCodice());
		for (StrutturaAmministrativoContabile sa : centroDiResponsabilita.getSubStrutture()) {
			if (centroDiCosto.getCodice().equals(sa.getCodice())) {
				log.debug(methodName, "SAC trovata CON uid " + sa.getUid() + ", Codice CDR: " + centroDiResponsabilita.getCodice() + ", Codice CDC " + sa.getCodice());
				return sa;
			}
		}
		throw new BusinessException(ErroreCore.VALORE_NON_CONSENTITO.getErrore("centro di costo", ": il centro con codice " + centroDiCosto.getCodice()
				+ " (centro di responsabilita' " + centroDiResponsabilita.getCodice() + ") non e' presente su base dati"));
	}

	
	/**
	 * <font color='blue'><b> Carica la lista delle SAC </b></font><br/>
	 * richiama il servizio <b>LeggiStrutturaAmminstrativoContabileService</b>
	 * @return la response del servizio di lettura degli atti amministrativi
	 */
	private LeggiStrutturaAmminstrativoContabileResponse leggiStrutturaAmministrativaContabile() {
		LeggiStrutturaAmminstrativoContabile request = new LeggiStrutturaAmminstrativoContabile();
		request.setAnno(attoAmministrativo.getBilancio().getAnno());
		request.setIdEnteProprietario(attoAmministrativo.getEnte().getUid());
		request.setRichiedente(req.getRichiedente());

		LeggiStrutturaAmminstrativoContabileResponse response = serviceExecutor.executeServiceThreadLocalCachedSuccess(
			new ServiceInvoker<LeggiStrutturaAmminstrativoContabile, LeggiStrutturaAmminstrativoContabileResponse>() {
				@Override
				public LeggiStrutturaAmminstrativoContabileResponse invokeService(LeggiStrutturaAmminstrativoContabile req) {
					return classificatoreService.leggiStrutturaAmminstrativoContabile(req);
				}
			}, request, new KeyAdapter<LeggiStrutturaAmminstrativoContabile>() {
				@Override
				public String computeKey(LeggiStrutturaAmminstrativoContabile o) {
					return o.getIdEnteProprietario() + "_" + o.getAnno();
				}
			});

		return response;
	}

	/**
	 * <font color='blue'><b>verifica che il tipo atto inserito sia presente tra quelli definiti per l'ente</b></font><br/> 
	 * cerca il legame sulla tabella siac_r_xx_xx
	 */
	private void checkTipoAttoPerEnte() {
		
		attoAmministrativoDad.setEnte(attoAmministrativo.getEnte());
		checkParametroValorizzato(attoAmministrativo.getTipoAtto() == null, "Tipo Atto");

		List<TipoAtto> listaTipoAtto = attoAmministrativoDad.getElencoTipi();
		TipoAtto tipoAtto = ottieniTipoAttoDaLista(attoAmministrativo.getTipoAtto(), listaTipoAtto);
		TipoAtto tipoAttoChiave = ottieniTipoAttoDaLista(attoAmministrativo.getTipoAttoChiave(), listaTipoAtto);
		
		attoAmministrativo.setTipoAtto(tipoAtto);
		attoAmministrativo.setTipoAttoChiave(tipoAttoChiave);
	}
	
	/**
	 * Ottiene il tipo di atto dalla lista fornita.
	 *
	 * @param tipoAtto il tipo di atto da cercare
	 * @param lista la lista dei tipi tra i quali ricercare il tipo
	 * @return il tipo presente nella lista
	 * @throws BusinessException nel caso in cui il tipo atto non sia presente nella lista fornita
	 */
	private TipoAtto ottieniTipoAttoDaLista(TipoAtto tipoAtto, List<TipoAtto> lista) {
		String methodName = "ottieniTipoAttoDaLista";
		if(tipoAtto == null || StringUtils.isBlank(tipoAtto.getCodice())) {
			return null;
		}
		for (TipoAtto ta : lista) {
			if (tipoAtto.getCodice().equals(ta.getCodice())) {
				return ta;
			}
		}
		log.info(methodName, "Elaborazione file Atti Amministrativi: tipo atto : " + tipoAtto.getCodice() + " non definito per l'ente  " + attoAmministrativo.getEnte().getUid());
		throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Tipo Atto", tipoAtto.getCodice()), Esito.FALLIMENTO);
	}

	/**
	 * <font color='blue'><b>Valorizza il bilancio a partire dall'ente</b></font><br/>
	 * Anno pi&ugrave; recente di bilancio in fase diversa da PREVISIONE per l'ente in elaborazione
	 */
	private void valorizzaBilancioFromEnte() {
		String methodName = "valorizzaBilancioFromEnte";
		bilancioDad.setEnteEntity(attoAmministrativo.getEnte());
		Bilancio bilancio = bilancioDad.getBilancioByEnteAndFaseCodeNonPrevisione(attoAmministrativo.getEnte());
		if (bilancio == null || bilancio.getUid() == 0) {
			log.debug(methodName, "Non ci sono Bilanci  validi  Con fase != Previsione  per l'ente :" + attoAmministrativo.getEnte());
			String msg = "Bilancio con stato Diverso da PREVISIONE per ente " + attoAmministrativo.getEnte();
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore(msg), Esito.FALLIMENTO);
		}
		log.info(methodName, "Elaborazione file Atti Amministrativi: Info Bilancio Trovato  (Anno/Fase/Uid):"
				+ bilancio.getAnno() + "/" + bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio().toString() + "/" + bilancio.getUid());
		attoAmministrativo.setBilancio(bilancio);
		
	}

	
	/**
	 * <font color='blue'><b>checkTipoDiGestione() </b></font><br/> 
	 */
	private void checkTipoDiGestione() {
		checkBusinessConditionNotToBeSatisfied(!attoAmministrativo.isTipoDiGestioneValido(), ErroreCore.FORMATO_NON_VALIDO.getErrore("Tipo di Gestione", attoAmministrativo.getTipoDiVariazione()));
	}

	/**
	 * <font color='blue'><b>checkAndSetEnte() </b></font><br/> 
	 * cerca l'ente a partire dal codice Istat
	 */
	private void checkAndSetEnte() {
		String methodName = "checkAndSetEnte";
		checkParametroValorizzato(attoAmministrativo.getCodiceIstat().isEmpty(), " Codice Istat ");
		List<Ente> enti = enteDad.getEntiByCodiceAndCodiceSistemaEsterno(attoAmministrativo.getCodiceIstat(), SistemaEsterno.ATTIAMM);

		checkBusinessConditionNotToBeSatisfied(enti.isEmpty(), ErroreCore.ENTITA_INESISTENTE.getErrore("Ente con codice istat ", attoAmministrativo.getCodiceIstat()));
		
		attoAmministrativo.setEnte(enti.get(0));
		log.debug(methodName, "Il codice Istat " + attoAmministrativo.getCodiceIstat() + " corrisponde all'ente " + attoAmministrativo.getEnte().getUid());
	}
	
	/**
	 * Se tipo variazione = Insert i campi connotati come <em>Chiave</em> non devono essere valorizzati
	 */
	private void checkDatiChiave() {
		if(!attoAmministrativo.isTipoDiVariazioneInserimento()) {
			return;
		}
		
		checkBusinessConditionNotToBeSatisfied(attoAmministrativo.getAnnoAttoChiave() != null, ErroreCore.VALORE_NON_CONSENTITO.getErrore("anno atto chiave", ": non puo' essere valorizzato in inserimento"));
		checkBusinessConditionNotToBeSatisfied(attoAmministrativo.getNumeroAttoChiave() != null, ErroreCore.VALORE_NON_CONSENTITO.getErrore("numero atto chiave", ": non puo' essere valorizzato in inserimento"));
		checkBusinessConditionNotToBeSatisfied(attoAmministrativo.getTipoAttoChiave() != null, ErroreCore.VALORE_NON_CONSENTITO.getErrore("tipo atto chiave", ": non puo' essere valorizzato in inserimento"));
		checkBusinessConditionNotToBeSatisfied(attoAmministrativo.getSacCentroDiCostoChiave() != null, ErroreCore.VALORE_NON_CONSENTITO.getErrore("centro di costo chiave", ": non puo' essere valorizzato in inserimento"));
		checkBusinessConditionNotToBeSatisfied(attoAmministrativo.getSacCentroDiResponsabilitaChiave() != null, ErroreCore.VALORE_NON_CONSENTITO.getErrore("centro di responsabilita chiave", ": non puo' essere valorizzato in inserimento"));
	}

	/**
	 * Controlla che il parametro sia valorizzato
	 * @param condition la condizione da controllare
	 * @param nomeParametro il nome del parametro
	 */
	protected void checkParametroValorizzato(boolean condition, String nomeParametro) {
		checkBusinessConditionNotToBeSatisfied(condition, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeParametro));
	}

	/**
	 * Controlla che il parametro sia valorizzato
	 * @param condition la condizione da controllare
	 * @param nomeParametro il nome del parametro
	 */
	protected void checkValoreParametroValido(boolean condition, Object... nomeParametro) {
		checkBusinessConditionNotToBeSatisfied(condition, ErroreCore.VALORE_NON_CONSENTITO.getErrore(nomeParametro));
	}
	
	/**
	 * Controlla che l'enti&agrave; sia valida
	 * @param condition la condizione da controllare
	 * @param nomeParametro il nome del parametro
	 */
	protected void checkValiditaEntita(boolean condition, String nomeParametro) {
		checkBusinessConditionNotToBeSatisfied(condition, ErroreCore.ENTITA_NON_TROVATA.getErrore(nomeParametro));
	}
	
	protected void checkBusinessConditionNotToBeSatisfied(boolean condition, Errore errore) {
		if (condition) {
			throw new BusinessException(errore, Esito.FALLIMENTO);
		}
	}

	/**
	 * Controlla che lo stato sia ANNULLATO
	 * @param statoOperativo lo stato
	 * @return <code>true</code> se lo stato &eacute; ANNULLATO
	 */
	private boolean isStatoAnnullato(String statoOperativo) {
		return StatoOperativoAtti.ANNULLATO.name().equals(statoOperativo);
	}

	/**
	 * Controlla che lo stato sia DEFINITIVO
	 * @param statoOperativo lo stato
	 * @return <code>true</code> se lo stato &eacute; DEFINITIVO
	 */
	private boolean isStatoDefinitivo(String statoOperativo) {
		return StatoOperativoAtti.DEFINITIVO.name().equals(statoOperativo);
	}

	/**
	 * Controlla che lo stato sia PROVVISORIO
	 * @param statoOperativo lo stato
	 * @return <code>true</code> se lo stato &eacute; PROVVISORIO
	 */
	private boolean isStatoProvvisorio(String statoOperativo) {
		return StatoOperativoAtti.PROVVISORIO.name().equals(statoOperativo);
	}

}
