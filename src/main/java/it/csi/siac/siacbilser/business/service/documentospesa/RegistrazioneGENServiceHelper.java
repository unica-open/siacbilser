/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.primanota.AnnullaPrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaAutomaticaService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaAutomatica;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaAutomaticaResponse;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.RegistrazioneMovFinModelDetail;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;


/**
 * Componente utilizzato nei servizi che inseriscono una Registrazione per la Contabilità Generale (modulo GEN).
 * @author Domenico
 */

@Component 
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegistrazioneGENServiceHelper extends ServiceHelper {
	
	protected LogUtil log = new LogUtil(this.getClass());
	
	//Services
	@Autowired
	private AnnullaPrimaNotaService annullaPrimaNotaService;
	
	//Components
	@Autowired
	private ElaborazioniManager elaborazioniManager;
	
	
	//DADs
	@Autowired
	private RegistrazioneMovFinDad registrazioneMovFinDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private PrimaNotaDad primaNotaDad;
	
	// JPA
	@PersistenceContext
	private EntityManager em;
	
	
	//Fields
	private Bilancio bilancio;
	
	public void init(ServiceExecutor serviceExecutor, Ente ente, Richiedente richiedente, String loginOperazione) {
		super.init(serviceExecutor, ente, richiedente);
		
		registrazioneMovFinDad.setEnte(ente);
		registrazioneMovFinDad.setLoginOperazione(loginOperazione);
		
		bilancioDad.setEnteEntity(ente);
		
		primaNotaDad.setEnte(ente);
		primaNotaDad.setLoginOperazione(loginOperazione);
		
		elaborazioniManager.init(ente, loginOperazione);
		
	}
	
	public void init(ServiceExecutor serviceExecutor, Ente ente, Richiedente richiedente, String loginOperazione, Bilancio bil) {
		this.init(serviceExecutor, ente, richiedente, loginOperazione);
		initBilancio(bil);
	}
	
	public void init(ServiceExecutor serviceExecutor, Ente ente, Richiedente richiedente, String loginOperazione, Integer anno) {
		this.init(serviceExecutor, ente, richiedente, loginOperazione);
		initBilancio(anno);
	}

	public void initBilancio(Integer anno) {
		Bilancio bil = new Bilancio();
		bil.setAnno(anno);
		initBilancio(bil);
	}

	public void initBilancio(Bilancio bilancio) {
		if(bilancio==null){
			throw new IllegalArgumentException("Inizializzazione RegistrazioneGENServiceHelper fallita: il parametro Bilancio deve essere valorizzato.");
		}
		this.bilancio = bilancio;
		if(this.bilancio.getUid()==0){
			this.bilancio = bilancioDad.getBilancioByAnno(bilancio.getAnno());
		}
		if(this.bilancio==null){
			throw new IllegalArgumentException("Inizializzazione RegistrazioneGENServiceHelper fallita: il parametro Bilancio deve essere valorizzato.");
		}
		Utility.BTL.initBilancio(this.bilancio.getAnno());
	}
	
	
	/**
	 * Determina un Evento censito per la registrazione automatica nel registro di GEN (RegistrazioneMovFin).
	 * 
	 * @param tipoCollegamento
	 * @param isAggiornamentoRegistrazioneMovFin
	 * @param isRilevanteIva
	 * @param isResiduo
	 * @return Evento
	 */
	public Evento determinaEvento(TipoCollegamento tipoCollegamento, boolean isAggiornamentoRegistrazioneMovFin, 
			boolean isRilevanteIva, //true se va selezionato evenco "con iva"
			boolean isTipoIvaPromisqua,//true se iva promisqua (viene in questo caso ignorato il valore di isRilevanteIva).
			boolean isNotaCredito, boolean isResiduo) {
		String methodName = "determinaEvento";
		Evento evento = null;
		if(isNotaCredito){
			evento = registrazioneMovFinDad.ricercaEventoCensitoNotaCredito(tipoCollegamento, isAggiornamentoRegistrazioneMovFin, isRilevanteIva, isTipoIvaPromisqua, isResiduo);
		} else {
			evento = registrazioneMovFinDad.ricercaEventoCensito(tipoCollegamento, isAggiornamentoRegistrazioneMovFin, isRilevanteIva, isTipoIvaPromisqua, isResiduo);
		}
		log.debug(methodName, "Evento: " + (evento != null ? evento.getCodice() + "[" + evento.getUid() + "]" : "null"));
		return evento;
	}
	
	/**
	 * Determina un Evento censito per la registrazione automatica nel registro di GEN (RegistrazioneMovFin).
	 * 
	 * @param tipoCollegamento
	 * @param isAggiornamentoRegistrazioneMovFin
	 * @param isRilevanteIva
	 * @param isTipoIvaPromisqua 
	 * @return Evento
	 */
	public Evento determinaEventoNotaCredito(TipoCollegamento tipoCollegamento, boolean isAggiornamentoRegistrazioneMovFin, boolean isRilevanteIva, boolean isTipoIvaPromisqua, boolean isResiduo) {
		String methodName = "determinaEventoNotaCredito";
		Evento evento = registrazioneMovFinDad.ricercaEventoCensitoNotaCredito(tipoCollegamento, isAggiornamentoRegistrazioneMovFin, isRilevanteIva, isTipoIvaPromisqua, isResiduo);
		log.debug(methodName, "Evento: " + (evento != null ? evento.getCodice() + "[" + evento.getUid() + "]" : "null"));
		return evento;
	}
	
	
	/**
	 * Determina un Evento relativo alla Cassa Economale aggiornamento o inserimento quota.
	 *
	 * @param tipoCollegamento the tipo collegamento
	 * @param isAggiornamentoRegistrazioneMovFin the is aggiornamento registrazione mov fin
	 * @return the evento
	 */
	public Evento determinaEventoCassaEconomale(TipoCollegamento tipoCollegamento, boolean isAggiornamentoRegistrazioneMovFin) {
		String methodName = "determinaEventoCassaEconomale";
		Evento evento = registrazioneMovFinDad.ricercaEventoCensitoCassaEconomale(tipoCollegamento, isAggiornamentoRegistrazioneMovFin);
		log.debug(methodName, "Evento: " + (evento != null ? evento.getCodice() + "[" + evento.getUid() + "]" : "null"));
		return evento;
	}
	
	/**
	 * Determina un Evento relativo alla Cassa Economale aggiornamento o inserimento quota.
	 *
	 * @param tipoCollegamento the tipo collegamento
	 * @param isAggiornamentoRegistrazioneMovFin the is aggiornamento registrazione mov fin
	 * @return the evento
	 */
	public Evento determinaEventoCassaEconomaleENotaCredito(TipoCollegamento tipoCollegamento, boolean isAggiornamentoRegistrazioneMovFin, boolean isNotaCredito) {
		String methodName = "determinaEventoCassaEconomale";
		Evento evento = registrazioneMovFinDad.ricercaEventoCensitoCassaEconomaleENotaCredito(tipoCollegamento, isAggiornamentoRegistrazioneMovFin, isNotaCredito);
		log.debug(methodName, "Evento: " + (evento != null ? evento.getCodice() + "[" + evento.getUid() + "]" : "null"));
		return evento;
	}
	
	public Evento determinaEventoCassaEconomale(TipoCollegamento tipoCollegamento, TipoRichiestaEconomale tipoRichiestaEconomale, boolean isAggiornamentoRegistrazioneMovFin) {
		String methodName = "determinaEventoCassaEconomale";
		Evento evento = registrazioneMovFinDad.ricercaEventoCensitoCassaEconomale(tipoCollegamento, tipoRichiestaEconomale, isAggiornamentoRegistrazioneMovFin);
		log.debug(methodName, "Evento: " + (evento != null ? evento.getCodice() + "[" + evento.getUid() + "]" : "null"));
		return evento;
	}
	
	
//	public Evento determinaEventoCassaEconomale(SubdocumentoSpesa subdoc, boolean isAggiornamentoRegistrazioneMovFin) {
//		String methodName = "determinaEventoCassaEconomale";
//		
//		TipoRichiestaEconomale tipoRichiestaEconomale = registrazioneMovFinDad.ricercaTipoRichiestaEconomaleAssociataAlSubdoc(subdoc); //TODO!!!!!!!! quiiiiiiiiiiiiiiiiiiii
//		
//		Evento evento = registrazioneMovFinDad.ricercaEventoCensitoCassaEconomale(TipoCollegamento.SUBDOCUMENTO_SPESA, tipoRichiestaEconomale, isAggiornamentoRegistrazioneMovFin);
//		log.debug(methodName, "Evento: "+evento!=null?evento.getCodice() + "["+evento.getUid()+"]":"null");
//		return evento;
//	}
	
	/**
	 * Determina un Evento relativo ai movimenti della finanziaria.
	 *
	 * @param tipoCollegamento the tipo collegamento
	 * @param isAggiornamentoRegistrazioneMovFin the is aggiornamento registrazione mov fin
	 * @return the evento
	 */
	public Evento determinaEventoMovimentiFinanziaria(TipoCollegamento tipoCollegamento, String codiceEvento) {
		String methodName = "determinaEventoMovimentiFinanziaria";
		Evento evento = registrazioneMovFinDad.ricercaEventoCensitoMovimentoFinanziaria(tipoCollegamento, codiceEvento);
		log.debug(methodName, "Evento: " + (evento != null ? evento.getCodice() + " [" + evento.getUid() + "]" : "null"));
		return evento;
	}
	
	
	public Evento determinaEventoRateo(TipoCollegamento tipoCollegamento, boolean isInserimento, boolean isAnnoCorrente) {
		String methodName = "determinaEventoRateo";
		
		Evento evento = registrazioneMovFinDad.ricercaEventoCensitoRateoRisconti(tipoCollegamento, true,  isInserimento, isAnnoCorrente);
		log.debug(methodName, "Evento: " + (evento != null ? evento.getCodice() + " [" + evento.getUid() + "]" : "null"));
		return evento;
	}
	
	public Evento determinaEventoRisconto(TipoCollegamento tipoCollegamento, boolean isInserimento, boolean isAnnoCorrente) {
		String methodName = "determinaEventoRisconto";
		
		Evento evento = registrazioneMovFinDad.ricercaEventoCensitoRateoRisconti(tipoCollegamento, false,  isInserimento, isAnnoCorrente);
		log.debug(methodName, "Evento: " + (evento != null ? evento.getCodice() + "[" + evento.getUid() + "]" : "null"));
		return evento;
	}
	
	
	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<RegistrazioneMovFin> ricercaRegistrazioniMovFinAssociateAlMovimentoTxRequiresNew(TipoCollegamento tipoCollegamento, Entita movimento) {
		return ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento);
	}
	
	
	/**
	 * Ricerca le registrazioni mov fin non annullate relative ad un Movimento (Documento, Subdocumento, Impegno, Accertamento, ecc...)
	 * @param ambito 
	 *
	 * @return the registrazione mov fin
	 */
	public List<RegistrazioneMovFin> ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento tipoCollegamento, Entita movimento){
		return ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, movimento, null);
	}
			
	/**
	 * Ricerca le registrazioni mov fin non annullate relative ad un Movimento (Documento, Subdocumento, Impegno, Accertamento, ecc...)
	 *
	 * @return the registrazione mov fin
	 */
	public List<RegistrazioneMovFin> ricercaRegistrazioniMovFinBilancioPrecendenteAssociateAlMovimento(TipoCollegamento tipoCollegamento, Entita movimento){
		return ricercaRegistrazioniMovFinBilancioPrecendenteAssociateAlMovimento(tipoCollegamento, movimento, null);
	}
	
	
	private List<RegistrazioneMovFin> ricercaRegistrazioniMovFinBilancioPrecendenteAssociateAlMovimento(
			TipoCollegamento tipoCollegamento, Entita movimento, Ambito ambito) {
		return ricercaRegistrazioniMovFinAssociateAlMovimentoByBilancio(tipoCollegamento, movimento, ambito, bilancioDad.getBilancioAnnoPrecedente(bilancio));
	}

	public List<RegistrazioneMovFin> ricercaRegistrazioniMovFinBilancioPrecendenteAssociateAlMovimento(TipoCollegamento tipoCollegamento, Integer idMovimento, Bilancio bilancio){
		return ricercaRegistrazioniMovFinBilancioPrecendenteAssociateAlMovimento(tipoCollegamento, idMovimento, bilancio, null);
	}
	
	
	/**
	 * Ricerca le registrazioni mov fin non annullate relative ad un Movimento (Documento, Subdocumento, Impegno, Accertamento, ecc...)
	 *
	 * @param primaNota the prima nota
	 * @return the registrazione mov fin
	 */
	public List<RegistrazioneMovFin> ricercaRegistrazioniMovFinValideAssociateAPrimaNota(PrimaNota primaNota) {
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneMovFinDad.findRegistrazioniByPrimaNota(primaNota, Arrays.asList(StatoOperativoRegistrazioneMovFin.ANNULLATO), new RegistrazioneMovFinModelDetail[] {});
		return registrazioniMovFin;
	}
			
	/**
	 * Ricerca le registrazioni mov fin non annullate relative ad un Movimento (Documento, Subdocumento, Impegno, Accertamento, ecc...)
	 *
	 * @return the registrazione mov fin
	 */
	public List<RegistrazioneMovFin> ricercaRegistrazioniMovFinBilancioPrecendenteAssociateAlMovimento(
			TipoCollegamento tipoCollegamento, Integer idMovimento, Bilancio bilancio, Ambito ambito) {
		
		final String methodName = "ricercaRegistrazioneMovFin";
		
		ListaPaginata<RegistrazioneMovFin> registrazioniMovFin = registrazioneMovFinDad.ricercaSinteticaRegistrazioneMovFin(
				buildRegistrazioneMovFin(ambito, bilancio),
				tipoCollegamento, 
				null,  
				Arrays.asList(idMovimento),
				Arrays.asList(StatoOperativoRegistrazioneMovFin.ANNULLATO),
				new ParametriPaginazione(0, Integer.MAX_VALUE) 
				);
		
		log.debug(methodName, "RegistrazioneMovFin trovata/e " +registrazioniMovFin.getTotaleElementi() +". ");
		
		return registrazioniMovFin;
	}
			
			
	/**
	 * Ricerca le registrazioni mov fin non annullate relative ad un Movimento (Documento, Subdocumento, Impegno, Accertamento, ecc...)
	 *
	 * @param tipoCollegamento the tipo collegamento
	 * @param movimento the movimento
	 * @param ambito the ambito
	 * @return the registrazione mov fin
	 */
	public List<RegistrazioneMovFin> ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento tipoCollegamento, Entita movimento, Ambito ambito) {
		
		return ricercaRegistrazioniMovFinAssociateAlMovimentoByBilancio(tipoCollegamento, movimento, ambito, bilancio);
	}

	/**
	 * @param tipoCollegamento
	 * @param movimento
	 * @param ambito
	 * @return
	 */
	public List<RegistrazioneMovFin> ricercaRegistrazioniMovFinAssociateAlMovimentoByBilancio(TipoCollegamento tipoCollegamento,Entita movimento, Ambito ambito, Bilancio bilancioReg) {
		
		RegistrazioneMovFin registrazioneMovFin = buildRegistrazioneMovFin(ambito, bilancioReg);
		
		final String methodName = "ricercaRegistrazioneMovFin";
		
		List<StatoOperativoRegistrazioneMovFin> statiOperativiRegistrazioneMovFinDaEscludere = Arrays.asList(StatoOperativoRegistrazioneMovFin.ANNULLATO);
		
		List<Integer> idMovimento = new ArrayList<Integer>();
		Integer docId;
		if(movimento instanceof Documento){
			docId = movimento.getUid();
			idMovimento = null;
		}else{
			docId = null;
			idMovimento.add(movimento.getUid());
		}
		ListaPaginata<RegistrazioneMovFin> registrazioniMovFin = registrazioneMovFinDad.ricercaSinteticaRegistrazioneMovFin(
				registrazioneMovFin,
				tipoCollegamento, //ad esempio: TipoCollegamento.SUBDOCUMENTO_SPESA,
				docId,  //doc.getUid(), //movimento.getUid()
				idMovimento,
				statiOperativiRegistrazioneMovFinDaEscludere,
				new ParametriPaginazione(0, Integer.MAX_VALUE) 
				);
		
		log.debug(methodName, "RegistrazioneMovFin trovata/e " +registrazioniMovFin.getTotaleElementi() +". ");
		
		return registrazioniMovFin;
	}

	private RegistrazioneMovFin buildRegistrazioneMovFin(Ambito ambito, Bilancio bilancio) {
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setBilancio(bilancio != null && bilancio.getUid() !=0? bilancio : this.bilancio); //Serve Davvero??? :(
		registrazioneMovFin.setAmbito(ambito);
		return registrazioneMovFin;
	}
	
	/**
	 * Inserisci registrazione mov fin.
	 * Attenzione! l'Ambito verra impostato di default come {@link Ambito#AMBITO_FIN}.
	 * Questo metodo verr&agrave; eliminato nelle prossime release.
	 *
	 * @param evento the evento
	 * @param movimento the movimento
	 * @param elementoPianoDeiConti the elemento piano dei conti
	 * @return the registrazione mov fin
	 * @deprecated utilizzare {@link #inserisciRegistrazioneMovFin(Evento, Entita, ElementoPianoDeiConti, Ambito)}
	 */
	@Deprecated
	public RegistrazioneMovFin inserisciRegistrazioneMovFin(Evento evento, Entita movimento, ElementoPianoDeiConti elementoPianoDeiConti) {
		return this.inserisciRegistrazioneMovFin(evento, movimento, elementoPianoDeiConti, Ambito.AMBITO_FIN);
	}
	
	
	/**
	 * Inserisci registrazione mov fin.
	 *
	 * @param evento the evento
	 * @param movimento the movimento
	 * @param elementoPianoDeiConti the elemento piano dei conti
	 * @param ambito the ambito
	 * @return the registrazione mov fin
	 */
	public RegistrazioneMovFin inserisciRegistrazioneMovFin(Evento evento, Entita movimento, ElementoPianoDeiConti elementoPianoDeiConti, Ambito ambito) {
		return this.inserisciRegistrazioneMovFin(evento, movimento, null, elementoPianoDeiConti, ambito);
	}
	
	
	/**
	 * Inserisci registrazione mov fin.
	 *
	 * @param evento the evento
	 * @param movimento the movimento
	 * @param elementoPianoDeiConti the elemento piano dei conti
	 * @param ambito the ambito
	 * @return the registrazione mov fin
	 */
	public RegistrazioneMovFin inserisciRegistrazioneMovFin(Evento evento, Entita movimento, Entita movimentoCollegato, ElementoPianoDeiConti elementoPianoDeiConti, Ambito ambito) {
		return inserisciRegistrazioneMovFin(evento, movimento, movimentoCollegato, elementoPianoDeiConti, elementoPianoDeiConti, ambito);
	}
	
	/**
	 * Inserisci registrazione mov fin.
	 * 
	 * @param evento
	 * @param movimento
	 * @param movimentoCollegato
	 * @param elementoPianoDeiContiIniziale
	 * @param elementoPianoDeiContiAggiornato
	 * @param ambito
	 * @return
	 */
	public RegistrazioneMovFin inserisciRegistrazioneMovFin(Evento evento, Entita movimento, Entita movimentoCollegato, ElementoPianoDeiConti elementoPianoDeiContiIniziale, ElementoPianoDeiConti elementoPianoDeiContiAggiornato,  Ambito ambito) {
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setEnte(ente);
		registrazioneMovFin.setBilancio(bilancio);
		
		registrazioneMovFin.setMovimento(movimento);
		registrazioneMovFin.setMovimentoCollegato(movimentoCollegato);
		
		registrazioneMovFin.setEvento(evento);
		registrazioneMovFin.setStatoOperativoRegistrazioneMovFin(StatoOperativoRegistrazioneMovFin.NOTIFICATO); //Lo stato iniziale è Notificato. Verrà messo a Registrato quando verrà creata la Prima nota Integrata.
		
		//registrazioneMovFin.setConto(conto);									//popolato quando la prima nota passerà allo stato DEFINITA!
		//registrazioneMovFin.setListaMovimentiEP(listaMovimentiEP);			//popolato quando creo la prima nota!
		
//		ElementoPianoDeiConti elementoPianoDeiConti = capitoloUscitaGestioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(subdoc.getImpegnoOSubImpegno().getCapitoloUscitaGestione());
		registrazioneMovFin.setElementoPianoDeiContiIniziale(elementoPianoDeiContiIniziale);
		registrazioneMovFin.setElementoPianoDeiContiAggiornato(elementoPianoDeiContiAggiornato); 
		//registrazioneMovFin.setElementoPianoDeiContiAggiornato(elementoPianoDeiContiAggiornato);	//viene modificato per particolari attività utente.
		
		
		registrazioneMovFin.setAmbito(ambito);
		
		registrazioneMovFinDad.inserisciRegistrazioneMovFin(registrazioneMovFin);
		
		return registrazioneMovFin;
	}
	
	
	
	
	public void annullaRegistrazioniMovFinEPrimeNote(List<RegistrazioneMovFin> registrazioniMovFin) {
		String methodName = "annullaRegistrazioniMovFinEPrimeNote";
		Map<Integer, PrimaNota> primeNoteAnnullate = new HashMap<Integer, PrimaNota>();
		
		if(registrazioniMovFin!=null) {
			for(RegistrazioneMovFin reg : registrazioniMovFin) {
				
				//la registrazioneMovFin ha un solo MovimentoEP in realtà
				for(MovimentoEP mov : reg.getListaMovimentiEP()) {
					PrimaNota primaNota = mov.getPrimaNota();
					
					//evita di annullare prime note gia' annullate! (in realtà ne annullerà una sola! ovvero quella associata a tutte le quote del documento!)
					if(primaNota != null && primaNota.getUid()!=0 && !primeNoteAnnullate.containsKey(primaNota.getUid())) { 
						
						//annullaPrimaNota(primaNota);
						primaNotaDad.aggiornaStatoPrimaNota(primaNota.getUid(),StatoOperativoPrimaNota.ANNULLATO);
						
						primeNoteAnnullate.put(primaNota.getUid(), primaNota);
					}
				}
				aggiornaStatoRegistrazione(reg, StatoOperativoRegistrazioneMovFin.ANNULLATO);
			}
		}
		
		log.debug(methodName, "primeNote annullate: "+primeNoteAnnullate.size() + ". Expected: [0, 1]");
	}
	
	
	/**
	 * 
	 * @param reg registrazione da annullare
	 * @param registrazioniMovFinPrecedenti registrazioni precedenti NON annullate. Ricercate col metodo {@link #ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento, Entita)}
	 */
	public void annullaRegistrazioneMovFinEPrimaNota(RegistrazioneMovFin reg, List<RegistrazioneMovFin> registrazioniMovFinPrecedenti){
		String methodName = "annullaRegistrazioneMovFinEPrimaNota";
		
		annullaPrimaNota(reg);
		
		aggiornaStatoRegistrazione(reg, StatoOperativoRegistrazioneMovFin.ANNULLATO);
		log.debug(methodName, "registrazione annullata [uid:"+reg.getUid()+"]. Stato precedente: "+ reg.getStatoOperativoRegistrazioneMovFin());
		reg.setStatoOperativoRegistrazioneMovFin(StatoOperativoRegistrazioneMovFin.ANNULLATO);
		
		//aggiornaro lo stato di tutte le altre registrazioni NON annullate in "Notificato".
		for(RegistrazioneMovFin regPrecedente : registrazioniMovFinPrecedenti){
			if(regPrecedente.getUid()==reg.getUid()){
				continue; //questa regisrazione è quella che sto annullando
			}
			if(StatoOperativoRegistrazioneMovFin.ANNULLATO.equals(regPrecedente.getStatoOperativoRegistrazioneMovFin()) //le registrazioni che arrivano a questo metodo sono già tutte NON annullate (ma un controllo in piu non guasta).
					|| StatoOperativoRegistrazioneMovFin.NOTIFICATO.equals(regPrecedente.getStatoOperativoRegistrazioneMovFin())){ 
				continue; //TODO forse sarebbe meglio escludere anche le contabilizzate?
			}	
			aggiornaStatoRegistrazione(regPrecedente, StatoOperativoRegistrazioneMovFin.NOTIFICATO);
			log.debug(methodName, "registrazione rimessa in stato Notificato [uid:"+regPrecedente.getUid()+"]. Stato precedente: "+regPrecedente.getStatoOperativoRegistrazioneMovFin());
			regPrecedente.setStatoOperativoRegistrazioneMovFin(StatoOperativoRegistrazioneMovFin.NOTIFICATO);
			
			annullaPrimaNota(regPrecedente);
		}
	}

	/**
	 * @param reg
	 * @param stato
	 */
	public void aggiornaStatoRegistrazione(RegistrazioneMovFin reg, StatoOperativoRegistrazioneMovFin stato) {
		registrazioneMovFinDad.aggiornaStatoRegistrazioneMovFin(reg.getUid(), stato);
	}

	public void annullaPrimaNota(RegistrazioneMovFin reg) {
		final String methodName = "annullaPrimaNota";
		//la registrazioneMovFin ha un solo MovimentoEP in realtà
		for(MovimentoEP mov : reg.getListaMovimentiEP()) {
			PrimaNota primaNota = mov.getPrimaNota();
			
			if(primaNota == null || primaNota.getUid()==0){
				continue;
			}
			
			if(!StatoOperativoPrimaNota.ANNULLATO.equals(primaNota.getStatoOperativoPrimaNota()) || primaNota.getStatoOperativoPrimaNota()==null){
				//l'annullamento e' condizionato per evitare di annullare prima note già annullata.
				primaNotaDad.aggiornaStatoPrimaNota(primaNota.getUid(),StatoOperativoPrimaNota.ANNULLATO); 
				log.debug(methodName, "primeNota annullata [uid:"+primaNota.getUid()+"]. Stato precedente: "+ primaNota.getStatoOperativoPrimaNota());
				primaNota.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.ANNULLATO); //mi segno che l'ho annullate per evitare di annullarla una seconda volta.
			} else {
				log.debug(methodName, "primeNota gia' annullata precedentemente [uid:"+primaNota.getUid()+"]. ");
			}
				
		}
	}
	

	public AnnullaPrimaNotaResponse annullaPrimaNota(PrimaNota primaNota) {
		AnnullaPrimaNota reqAPN = new AnnullaPrimaNota();
		reqAPN.setRichiedente(richiedente);
		reqAPN.setBilancio(primaNota.getBilancio());
		reqAPN.setPrimaNota(primaNota);
		//SIAC-5848
		reqAPN.setAnnoBilancio(bilancio != null ? bilancio.getAnno() : null);
		AnnullaPrimaNotaResponse resAPN = serviceExecutor.executeServiceSuccess(annullaPrimaNotaService, reqAPN);
		return resAPN;
	}

	
	/**
	 * Prova ad inserire una prima nota in modalità asincrona.
	 * 
	 * Di default il servizio partirà dopo 10 secondi dalla chiamata.
	 * Per specificare il tempo chiamare {@link #inserisciPrimaNotaAutomaticaAsync(RegistrazioneMovFin, long)}.
	 * 
	 * @param registrazioniMovFin
	 */
	public void inserisciPrimaNotaAutomaticaAsync(RegistrazioneMovFin registrazioneMovFin) {
		final String methodName = "inserisciPrimaNotaAutomaticaAsync";
		if(registrazioneMovFin == null){
			log.debug(methodName, "Nessuna registrazione da inserire.");
			return;
		}
		
		List<RegistrazioneMovFin> registrazioniMovFin = new ArrayList<RegistrazioneMovFin>();
		registrazioniMovFin.add(registrazioneMovFin);
		inserisciPrimaNotaAutomaticaAsync(registrazioniMovFin);
	}
	
	/**
	 * Prova ad inserire una prima nota in modalità asincrona.
	 * 
	 * E' possibile specificare l'intervallo di tempo dopo il quale far partire il servizio.
	 *
	 * @param registrazioniMovFin the registrazioni mov fin
	 * @param startTimeout tempo in millisecondi dopo il quale far partire serivizio.
	 */
	public void inserisciPrimaNotaAutomaticaAsync(RegistrazioneMovFin registrazioneMovFin, long startTimeout) {
		final String methodName = "inserisciPrimaNotaAutomaticaAsync";
		if(registrazioneMovFin == null){
			log.debug(methodName, "Nessuna registrazione da inserire.");
			return;
		}
		
		List<RegistrazioneMovFin> registrazioniMovFin = new ArrayList<RegistrazioneMovFin>();
		registrazioniMovFin.add(registrazioneMovFin);
		inserisciPrimaNotaAutomaticaAsync(registrazioniMovFin, null, startTimeout);
	}
	
	/**
	 * Prova ad inserire una prima nota in modalità asincrona.
	 * 
	 * Di default il servizio partirà dopo 30 secondi dalla chiamata.
	 * Per specificare il tempo chiamare {@link #inserisciPrimaNotaAutomaticaAsync(List, long)}.
	 * 
	 * @param registrazioniMovFin
	 */
	public void inserisciPrimaNotaAutomaticaAsync(List<RegistrazioneMovFin> registrazioniMovFin) {
		inserisciPrimaNotaAutomaticaAsync(registrazioniMovFin, null,  30000);
	}
	
	
	public void inserisciPrimaNotaAutomaticaAsync(List<RegistrazioneMovFin> registrazioniMovFin, final String group, Errore errore, final String... elabKeys) {
		startElaborazionePrimeNote(group, errore, elabKeys);
		inserisciPrimaNotaAutomaticaAsync(registrazioniMovFin, new ResponseHandler<InseriscePrimaNotaAutomaticaResponse>() {

			@Override
			public void handleResponseBase(InseriscePrimaNotaAutomaticaResponse response) {
				endElaborazionePrimeNote(group, elabKeys);
			}

			@Override
			protected void handleResponse(InseriscePrimaNotaAutomaticaResponse response) {
				// Nessuna elaborazione
			}
			
		});
	}
	
	/**
	 * 
	 * Nuova versione con gestione della concorrenza
	 * 
	 * @param registrazioniMovFin
	 * @param ordinativo
	 * @param elabKeys
	 */
	public <E extends Entita> void inserisciPrimaNotaAutomaticaAsync(List<RegistrazioneMovFin> registrazioniMovFin, E entita, Ambito ambito ){
		//SIAC-4906: centralizzo la chiamata
		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(entita.getUid(),this.getClass(),entita.getClass(), ambito.name());
		ElabKeys elabKeyPrimaNota = ElabKeys.PRIMA_NOTA;
		String elabService = eakh.creaElabServiceFromPattern(elabKeyPrimaNota);
		String elabKey = eakh.creaElabKeyFromPattern(elabKeyPrimaNota);
		String msg = "L'elaborazione di alcune prime note afferenti a "+ entita.getUid()+" e' ancora in corso. Attendere il termine dell'elaborazione";
		Errore errore = ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg);
		log.debug("inserisciPrimaNotaAutomaticaAsync", "elabService " + elabService + " elabKey " + elabKey);
		inserisciPrimaNotaAutomaticaAsync(registrazioniMovFin, elabService, errore, elabKey);
	}
	

	/**
	 * Prova ad inserire una prima nota in modalità asincrona.
	 * 
	 * Di default il servizio partirà dopo 30 secondi dalla chiamata.
	 * Per specificare il tempo chiamare {@link #inserisciPrimaNotaAutomaticaAsync(List, long)}.
	 * 
	 * @param registrazioniMovFin
	 * @param responseHandler  permette di specificare un {@link ResponseHandler} per gestire la ServiceResponse.
	 */
	public void inserisciPrimaNotaAutomaticaAsync(List<RegistrazioneMovFin> registrazioniMovFin, ResponseHandler<InseriscePrimaNotaAutomaticaResponse> responseHandler) {
		inserisciPrimaNotaAutomaticaAsync(registrazioniMovFin, responseHandler,  30000);
	}
	
	/**
	 * Prova ad inserire una prima nota in modalità asincrona.
	 * 
	 * E' possibile specificare l'intervallo di tempo dopo il quale far partire il servizio.
	 *
	 * @param registrazioniMovFin the registrazioni mov fin
	 * @param startTimeout tempo in millisecondi dopo il quale far partire serivizio.
	 * @param responseHandler 
	 */
	public void inserisciPrimaNotaAutomaticaAsync(List<RegistrazioneMovFin> registrazioniMovFin, ResponseHandler<InseriscePrimaNotaAutomaticaResponse> responseHandler, long startTimeout) {
		final String methodName = "inserisciPrimaNotaAutomaticaAsync";
		if(registrazioniMovFin == null || registrazioniMovFin.isEmpty()){
			log.debug(methodName, "Nessuna registrazione da inserire.");
			if(responseHandler!=null) {
				responseHandler.handleResponseBase(null);
			}
			return;
		}
		
		
		InseriscePrimaNotaAutomatica reqIPNA = new InseriscePrimaNotaAutomatica();
		reqIPNA.setRichiedente(richiedente);
		reqIPNA.setRegistrazioniMovFin(registrazioniMovFin);
		//SIAC-5848
		reqIPNA.setAnnoBilancio(bilancio != null ? bilancio.getAnno() : null);
		serviceExecutor.executeServiceSingleThreadPoolAsync(InseriscePrimaNotaAutomaticaService.class, reqIPNA, responseHandler, startTimeout);
	}
	
	public InseriscePrimaNotaAutomaticaResponse inserisciPrimaNotaAutomatica(RegistrazioneMovFin registrazioneMovFin) {
		List<RegistrazioneMovFin> registrazioniMovFin = new ArrayList<RegistrazioneMovFin>();
		registrazioniMovFin.add(registrazioneMovFin);
		return inserisciPrimaNotaAutomatica(registrazioniMovFin);
	}
	
	public InseriscePrimaNotaAutomaticaResponse inserisciPrimaNotaAutomatica(List<RegistrazioneMovFin> registrazioniMovFin) {
		final String methodName = "inserisciPrimaNotaAutomaticaAsync";
		if(registrazioniMovFin == null || registrazioniMovFin.isEmpty()){
			log.debug(methodName, "Nessuna registrazione da inserire.");
			return null;
		}
		
		InseriscePrimaNotaAutomatica reqIPNA = new InseriscePrimaNotaAutomatica();
		reqIPNA.setRichiedente(richiedente);
		reqIPNA.setRegistrazioniMovFin(registrazioniMovFin);
		//SIAC-5848
		reqIPNA.setAnnoBilancio(bilancio != null ? bilancio.getAnno() : null);
		
		return serviceExecutor.executeServiceSuccess(InseriscePrimaNotaAutomaticaService.class, reqIPNA);
	}
	
	public void flushAndClear() {
		em.flush();
		em.clear();
	}
	
	


	protected void startElaborazionePrimeNote(String group, Errore errore, String... elabKeys) {
		String methodName = "startElaborazionePrimeNote";
		//String group = "elabPrimaNota.docId:"+doc.getUid();
		try {
			elaborazioniManager.startElaborazioni(group, elabKeys);
		} catch (ElaborazioneAttivaException eae) {
			
			String msg = "L'elaborazione di alcune prime note afferenti al gruppo "+group+" e' ancora in corso. Attendere il termine dell'elaborazione";
			String msgGroup = "[group: "+group+" elabKeys: " + ToStringBuilder.reflectionToString(elabKeys, ToStringStyle.SIMPLE_STYLE)+"]";
			String msgSuffix = ". Si prega di rieseguire piu' tardi";
			log.error(methodName, msg + " " +  msgGroup, eae);
			
			if(errore == null) {
				errore = ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg);
			}
			
			if(ErroreBil.ELABORAZIONE_ATTIVA.getCodice().equals(errore.getCodice()) 
					&& errore.getDescrizione().indexOf(msgSuffix)!=-1){
				errore.setDescrizione(errore.getDescrizione().replaceAll(msgSuffix, " " + msgGroup + msgSuffix));
			} else {
				errore.setDescrizione(errore.getDescrizione() + " " + msgGroup + msgSuffix);
			}
			
			log.error(methodName, errore.getTesto(), eae);
			
			throw new BusinessException(errore);
		}
	}
	
	protected void endElaborazionePrimeNote(String group, String... elabKeys) {
		elaborazioniManager.endElaborazioni(group, elabKeys);
	}

	public void startElaborazioneRegistrazioni(List<? extends RegistrazioneMovFin> registrazioni) {
		String methodName = "startElaborazioneRegistrazioni";
		String[] elabKeys = getElabKeys(registrazioni);
		try {
			elaborazioniManager.startElaborazioni(RegistrazioneMovFin.class.getSimpleName(), elabKeys);
		} catch (ElaborazioneAttivaException eae){
			String msg = "L'elaborazione di alcune registrazioni contabili afferenti al documento e' ancora in corso. Attendere il termine dell'elaborazione. ["+eae.getMessage()+" "+Arrays.toString(elabKeys)+"]";
			log.error(methodName, msg, eae);
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
		}
	}

	public void endElaborazioneRegistrazioni(List<? extends RegistrazioneMovFin> registrazioni) {
		String[] elabKeys = getElabKeys(registrazioni);
		elaborazioniManager.endElaborazioni(RegistrazioneMovFin.class.getSimpleName(), elabKeys);
	}

	private String[] getElabKeys(List<? extends RegistrazioneMovFin> registrazioni) {
		List<String> elabKeys = new ArrayList<String>();
		for(RegistrazioneMovFin reg : registrazioni){
			elabKeys.add("RegistrazioneMovFin.uid:"+reg.getUid());
		}
		return elabKeys.toArray(new String[elabKeys.size()]);
	}
	
}
