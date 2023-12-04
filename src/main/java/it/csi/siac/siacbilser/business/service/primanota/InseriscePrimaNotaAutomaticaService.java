/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.base.cache.keyadapter.RicercaSinteticaCausaleKeyAdapter;
import it.csi.siac.siacbilser.business.service.causale.RicercaSinteticaCausaleService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaContiConciliazionePerClasseService;
import it.csi.siac.siacbilser.business.service.primanota.movimento.MovimentoHandler;
import it.csi.siac.siacbilser.business.service.registrazionemovfin.AssegnaContoEPRegistrazioneMovFinService;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoEnum;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AssegnaContoEPRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.AssegnaContoEPRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaAutomatica;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaAutomaticaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasseResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Il Servizio inserisce una PrimaNota a partire dalle registrazioniMovFin solo se tutti i dati sono desumibili in modo automatico.
 * In caso contrario la PrimaNota non viene inserita e lo stato delle registrazioniMovFin rimane in NOTIFICATO.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InseriscePrimaNotaAutomaticaService extends CheckedAccountBaseService<InseriscePrimaNotaAutomatica, InseriscePrimaNotaAutomaticaResponse> {
	
	@Autowired
	private RegistrazioneMovFinDad registrazioneMovFinDad;
	
	private List<RegistrazioneMovFin> registrazioniMovFin;
	private RegistrazioneMovFin registrazioniMovFin0;

	private MovimentoHandler<? extends Entita> movimentoHandler;
	
	private StringBuilder msgBuilder = new StringBuilder();
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRegistrazioniMovFin(), "lista registrazioni movimento finanziario");
		checkCondition(!req.getRegistrazioniMovFin().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista registrazioni movimento finanziario"), false);
		
		for(RegistrazioneMovFin registrazioneMovFin : req.getRegistrazioniMovFin()){
			checkEntita(registrazioneMovFin, "registrazione movimento finanziario", false);
		}
		
	}
	
	
	@Override
	@Transactional
	public InseriscePrimaNotaAutomaticaResponse executeService(InseriscePrimaNotaAutomatica serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		String methodName = "execute";
		
		caricaRegistrazioniMovFin();
		checkStatoNotificato();
		
		for(RegistrazioneMovFin regMovFin : registrazioniMovFin) {
			if(SiacDEventoEnum.ImpegnoPRG.getCodice().equals(regMovFin.getEvento().getCodice())){
				
				log.info(methodName, "Registrazione [uid:"+regMovFin.getUid()+"] per l'evento "+SiacDEventoEnum.ImpegnoPRG.getCodice()
						+". L'inserimento della prima nota automatica viene saltato. Assegno solo il Conto EP.");
				
				//per questo evento non devo inserire una prima nota, ma solo assegnare il conto EP
				assegnaContoEPRegistrazioneMovFin(regMovFin);
				//posso uscire dopo la prima tanto per gli impegni avrò una sola registrazione
				return;
			}
		}
		
		if(registrazioniMovFin0.getMovimento()==null){
			throw new BusinessException("Impossibile ottenere il Movimento associato alla Registrazione [uid: " + registrazioniMovFin0.getUid() + "] per l'evento: "
						+ (registrazioniMovFin0.getEvento()!=null?registrazioniMovFin0.getEvento().getCodice():" null "));
		}
		
		movimentoHandler  = MovimentoHandler.getInstance(serviceExecutor, req.getRichiedente(), ente, registrazioniMovFin0.getBilancio(),
				registrazioniMovFin0.getMovimento().getClass(), 
				registrazioniMovFin0.getMovimentoCollegato()!=null?registrazioniMovFin0.getMovimentoCollegato().getClass():null);
		
		caricaMovimenti();
		
		PrimaNota primaNota = popolaPrimaNota();
		if(primaNota==null) {
			String msg = msgBuilder.toString();
			log.warn(methodName, "PrimaNota automatica non inseribile. Abbandono l'operazione. "+ msg);
			
//			throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("inserimento automatico PrimaNota Integrata"));
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("Inserimento automatico PrimaNota Integrata abbandonato. "+msg));
		}
		
		registraPrimaNotaIntegrata(primaNota, Boolean.FALSE /*res.getRegistrazioniMovFinScartate().isEmpty()*/);
		log.info(methodName, "PrimaNota automatica registrata. [uid:"+ primaNota.getUid()+"]");
		res.setPrimaNota(primaNota);
		
	}
	
	private AssegnaContoEPRegistrazioneMovFinResponse assegnaContoEPRegistrazioneMovFin(RegistrazioneMovFin registrazioneMovFin) {
		AssegnaContoEPRegistrazioneMovFin reqACEP = new AssegnaContoEPRegistrazioneMovFin();
		reqACEP.setRegistrazioneMovFin(registrazioneMovFin);
		reqACEP.setRichiedente(req.getRichiedente());
		AssegnaContoEPRegistrazioneMovFinResponse resACEP =  serviceExecutor.executeServiceSuccess(AssegnaContoEPRegistrazioneMovFinService.class, reqACEP);
		return resACEP;
	}


	/**
	 * Carica le Registrazioni Mov Fin.
	 * 
	 * @throws BusinessException ErroreCore.ENTITA_INESISTENTE se non viene trovata la registrazione.
	 */
	private void caricaRegistrazioniMovFin() {
		
		this.registrazioniMovFin = new ArrayList<RegistrazioneMovFin>();
		for(RegistrazioneMovFin registrazioneMovFin : req.getRegistrazioniMovFin()){
			RegistrazioneMovFin r = registrazioneMovFinDad.findRegistrazioneMovFinById(registrazioneMovFin.getUid());
			if(r == null){
				throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Inseriemento prima nota integrata", "Registrazione Movimento finanziario con uid: "+registrazioneMovFin.getUid()));
			}
			registrazioniMovFin.add(r);
		}
		
		registrazioniMovFin0 = registrazioniMovFin.get(0);
	}


	/**
	 * La registrazione deve essere in stato 'Notificato' (RegistrazioneMovFin.StatoOperativoRegistrazioneMovFin='N')
	 * 
	 * @throws BusinessException 
	 */
	private void checkStatoNotificato() {
		for(RegistrazioneMovFin registrazioneMovFin: registrazioniMovFin){
			if(!StatoOperativoRegistrazioneMovFin.NOTIFICATO.equals(registrazioneMovFin.getStatoOperativoRegistrazioneMovFin())){
				throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Registrazione Movimento finanziario", registrazioneMovFin.getStatoOperativoRegistrazioneMovFin()));
			}
		}
	}


	/**
	 * Carica i dati del Movimento delle registrazioniMovFin necessari alla creazione della PrimaNota.
	 */
	private void caricaMovimenti() {
		
		for(RegistrazioneMovFin registrazioneMovFin : registrazioniMovFin){
			movimentoHandler.caricaMovimento(registrazioneMovFin);
		}
		
		//solo i subdocumenti effettuano per ora inizializzazioni di Imponibile E Imposta per la suddivisione nei MovimentiDettaglio.
		movimentoHandler.inizializzaDatiMovimenti(registrazioniMovFin); 
		
		
	}


	/**
	 * Popola prima nota.
	 *
	 * @return the prima nota
	 */
	private PrimaNota popolaPrimaNota() {
		String methodName = "popolaPrimaNota";
		PrimaNota primaNota = new PrimaNota();
		
		primaNota.setEnte(ente);
		primaNota.setBilancio(registrazioniMovFin0.getBilancio());

		primaNota.setTipoCausale(TipoCausale.Integrata);
		primaNota.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);

		// SIAC-5459: limito il campo a 500 caratteri
		primaNota.setDescrizione(StringUtils.substring(movimentoHandler.ottieniDescrizionePrimaNota(registrazioniMovFin0), 0, 500));
		primaNota.setDataRegistrazione(new Date());
		
		primaNota.setAmbito(registrazioniMovFin0.getAmbito());
		
		primaNota.setSoggetto(movimentoHandler.getSoggetto(registrazioniMovFin0));
		
		List<MovimentoEP> listaMovimentiEP = ottieniListaMovimentoEP();
		if(listaMovimentiEP.isEmpty()){
			String msg = "Deve essere presente almeno un MovimentiEP. "; 
			log.info(methodName, msg + "Impossibile creare la Prima Nota. Returning null.");
			msgBuilder.append(msg);
			return null;
		}
		primaNota.setListaMovimentiEP(listaMovimentiEP);
		
		return primaNota;
		
	}
	
	/**
	 * Ottieni lista movimento ep della PrimaNota.
	 *
	 * @return the list
	 */
	private List<MovimentoEP> ottieniListaMovimentoEP() {
		final String methodName = "ottieniListaMovimentoEP";
		
		List<MovimentoEP> listaMovimentiEP = new ArrayList<MovimentoEP>();
		
		for(RegistrazioneMovFin registrazioneMovFin : registrazioniMovFin) {
			
			//Ogni registrazione dà origine ad un MovimentoEP. 
			//Nel caso delle Registrazioni afferenti a Quote ho un MovimentoEP per ogni Quota.
			MovimentoEP movimentoEP = new MovimentoEP();
		
			CausaleEP causaleEP = individuaCausaleEP(registrazioneMovFin);
			if(causaleEP == null){
				String msg = "Impossibile selezionare in automatico una CausaleEP per la RegistrazioneMovFin con uid:"+registrazioneMovFin.getUid() 
						+ (registrazioneMovFin.getEvento()!=null?" Evento: "+registrazioneMovFin.getEvento().getCodice():"") 
						+ (registrazioneMovFin.getElementoPianoDeiContiAggiornato()!=null?" ElementoPianoDeiConti: "+registrazioneMovFin.getElementoPianoDeiContiAggiornato().getCodice(): "")
						+ ". Verrà saltata la creazione del MovimentoEP corrispondente. ";
				log.info(methodName, msg);
				msgBuilder.append(msg);
				res.addRegistrazioneMovFinScartata(registrazioneMovFin);
				continue;
			}
			
			causaleEP = sostituisciContiPerClassiDiConciliazioneSeNecessario(causaleEP, registrazioneMovFin);
			
			movimentoEP.setCausaleEP(causaleEP);
			movimentoEP.setDescrizione(movimentoHandler.ottieniDescrizioneMovimentoEP(registrazioneMovFin));
			movimentoEP.setRegistrazioneMovFin(registrazioneMovFin);
			
			movimentoEP.setAmbito(registrazioneMovFin.getAmbito());
			
			List<MovimentoDettaglio> listaMovimentoDettaglio = new ArrayList<MovimentoDettaglio>();
			
			int i = 0;
			for(ContoTipoOperazione contoTipoOperazione : causaleEP.getContiTipoOperazione()){
						
				MovimentoDettaglioConContoTipoOperazione md = new MovimentoDettaglioConContoTipoOperazione();
				md.setNumeroRiga(i);
				md.setConto(contoTipoOperazione.getConto());
				md.setSegno(contoTipoOperazione.getOperazioneSegnoConto());
				md.setImporto(BigDecimal.ZERO);
				
				md.setContoTipoOperazione(contoTipoOperazione);
				
				listaMovimentoDettaglio.add(md);
				i++;
			}
			movimentoEP.setListaMovimentoDettaglio(listaMovimentoDettaglio);
			
			movimentoHandler.impostaImportiListaMovimentiDettaglio(movimentoEP);
			
			if(importiMovimentoValidi(movimentoEP)){
				listaMovimentiEP.add(movimentoEP);	
			}
			
		}
		return listaMovimentiEP;
	}

	private CausaleEP sostituisciContiPerClassiDiConciliazioneSeNecessario(CausaleEP causaleEP, RegistrazioneMovFin registrazioneMovFin) {
		String methodName = "sostituisciContiPerClassiDiConciliazione";
		if(!Ambito.AMBITO_GSA.equals(causaleEP.getAmbito())){
			log.debug(methodName, "l'ambito non e' GSA oppure, non devo fare nulla");
			//l'ambito non e' GSA oppure non ho classe di conciliazione, non devo fare nulla
			return causaleEP;
		}
		for(ContoTipoOperazione contoTipoOperazione : causaleEP.getContiTipoOperazione()){
			if(contoTipoOperazione.getClasseDiConciliazione() == null){
				log.debug(methodName, "non ho classe di conciliazione, non devo fare nulla");
				//l'ambito non e' GSA oppure non ho classe di conciliazione, non devo fare nulla
				continue;
			}
			if(contoTipoOperazione.getConto() == null && contoTipoOperazione.getClasseDiConciliazione() != null){
				//ho un conto nullo, cerco di sostituirlo con la classe di conciliazione
				movimentoHandler.caricaCapitolo(registrazioneMovFin);
				log.debug(methodName, "ho un conto nullo, cerco di sostituirlo con la classe di conciliazione");
				Conto conto = ottieniContoDaCausaleEP(contoTipoOperazione.getClasseDiConciliazione(), registrazioneMovFin);
				log.debug(methodName, "trovato conto: " + (conto == null ? "null" : conto.getCodice()));
				contoTipoOperazione.setConto(conto);
			}
		
		}
		return causaleEP;
	}

	private Conto ottieniContoDaCausaleEP(ClasseDiConciliazione classeDiConciliazione, RegistrazioneMovFin registrazioneMovFin) {
		RicercaContiConciliazionePerClasse reqRCC = new RicercaContiConciliazionePerClasse();
		reqRCC.setClasseDiConciliazione(classeDiConciliazione);
		reqRCC.setCapitolo(movimentoHandler.getCapitolo(registrazioneMovFin));
		reqRCC.setSoggetto(movimentoHandler.getSoggetto(registrazioneMovFin));
		reqRCC.setRichiedente(req.getRichiedente());
		RicercaContiConciliazionePerClasseResponse resRCC = serviceExecutor.executeServiceSuccess(RicercaContiConciliazionePerClasseService.class, reqRCC);
		
		return (resRCC.getConti() != null && resRCC.getConti().size() == 1) ? resRCC.getConti().get(0) : null;
	}
	
//	private Conto ottieniContoDaCausaleEP(CausaleEP causaleEP, RegistrazioneMovFin registrazioneMovFin) {
//		RicercaContiConciliazionePerCausaleEP reqRCC = new RicercaContiConciliazionePerCausaleEP();
//		reqRCC.setCausaleEP(causaleEP);
//		reqRCC.setCapitolo(movimentoHandler.getCapitolo(registrazioneMovFin));
//		reqRCC.setSoggetto(movimentoHandler.getSoggetto(registrazioneMovFin));
//		reqRCC.setRichiedente(req.getRichiedente());
//		RicercaContiConciliazionePerCausaleEPResponse resRCC = serviceExecutor.executeServiceSuccess(RicercaContiConciliazionePerCausaleEPService.class, reqRCC);
//		
//		return (resRCC.getConti() != null && resRCC.getConti().size() == 1) ? resRCC.getConti().get(0) : null;
//	}

	private boolean importiMovimentoValidi(MovimentoEP movimentoEP) {
		String methodName = "importiMovimentoValidi";
		
		BigDecimal totaleDare = BigDecimal.ZERO;
		BigDecimal totaleAvere = BigDecimal.ZERO;
		Boolean dare = Boolean.FALSE;
		Boolean avere = Boolean.FALSE;
		
		for(MovimentoDettaglio det : movimentoEP.getListaMovimentoDettaglio()){
			if(OperazioneSegnoConto.DARE.equals(det.getSegno())){
				totaleDare = totaleDare.add(det.getImporto());
				dare = Boolean.TRUE;
			}else{
				totaleAvere = totaleAvere.add(det.getImporto());
				avere = Boolean.TRUE;
			}
		}
		
		if( !(Boolean.TRUE.equals(dare) && Boolean.TRUE.equals(avere)) ) {
			String msg = " Devono essere presenti almeno due conti con segni differenti. ";
			log.error(methodName, msg);
			msgBuilder.append(msg);
			return false;
		}
		if(totaleDare.compareTo(totaleAvere) != 0) {
			String msg = " Il totale DARE deve essere UGUALE al totale AVERE. ";
			log.debug(methodName, msg);
			msgBuilder.append(msg);
			return false;
		}
		
		return true;
	}




	/**
	 * Aggiunge al MovimentoDettaglio informazioni sul ContoTipoOperazioni 
	 * necessarie per suddividere gli importi sui Conti.
	 *
	 */
	public static class MovimentoDettaglioConContoTipoOperazione extends MovimentoDettaglio {
		/**
		 * 
		 */
		private static final long serialVersionUID = -9136472276933872211L;
		private ContoTipoOperazione contoTipoOperazione;
		
		public MovimentoDettaglioConContoTipoOperazione() {
			
		}
		
		/**
		 * @return the contoTipoOperazione
		 */
		public ContoTipoOperazione getContoTipoOperazione() {
			return contoTipoOperazione;
		}
		/**
		 * @param contoTipoOperazione the contoTipoOperazione to set
		 */
		public void setContoTipoOperazione(ContoTipoOperazione contoTipoOperazione) {
			this.contoTipoOperazione = contoTipoOperazione;
		}
		
	}
	
	

	/**
	 * A partire dal TipoEvento e dall'Evento associato alla richiesta di registrazione selezionare 
	 * l'elenco delle causali integrate (TipoCausale = I ) possibili (sono ammesse causali  in stato V-Valido e attive nell'anno di bilancio su cui si sta lavorando);
	 * 
	 * Dopodiché in base al V livello del Piano dei Conti Finanziario associato al Movimento Finanziario 
	 * coinvolto (ad es. impegno, accertamento, ...)  si prendono in considerazione quelle collegate alle stesso conto finanziario. 
	 * 
	 * Per le quote documento si fa riferimento al piano dei conti finanziario del movimento gestione ad esse collegato, quindi impegno o accertamento ad es.).
	 * Se il V livello del piano dei conti finanziario non è sufficiente per determinare univocamente una causale è 
	 * necessario verificare anche i suoi eventuali classificatori collegati, ed infine l'eventuale soggetto abbinato. 
	 * 
	 * Oppure si controllano gli altri classificatori previsti per tutti gli enti:
	 * Rilevante IVA
	 * Tipo documento collegato
	 * 
	 * @param movimento 
	 * 
	 * @return
	 */
	private CausaleEP individuaCausaleEP(RegistrazioneMovFin registrazioneMovFin) {
		String methodName = "individuaCausaleEP";
		
		RicercaSinteticaCausaleResponse resRSC = ricercaSinteticaCausaleCached(
				registrazioneMovFin
//				,movimentoHandler.getSoggetto(registrazioneMovFin.getMovimento())
				
				);
		
		List<CausaleEP> causaliEP = resRSC.getCausali();
		
		// Se non ho causali
		if(causaliEP == null || causaliEP.isEmpty()) {
			String msg = "Nessuna Causale EP disponibile. Occorre inserirne almeno una in stato VALIDO. ";
			log.info(methodName, msg + "Impossibile selezionare in automatico la Causale EP. Returning null.");
			msgBuilder.append(msg);
			return null;
		}
		 
		// Se ho un'unica causale, la prendo
		if(causaliEP.size() == 1) {
			CausaleEP result = causaliEP.get(0);
			log.info(methodName, "Un'unica Causale EP presente. La seleziono subito. Returning CausaleEP con uid: "+result.getUid());
			return result;
		}
		
		// Prendo il movimento di gestione
		ElementoPianoDeiConti elementoPianoDeiConti = registrazioneMovFin.getElementoPianoDeiContiAggiornato();//getElementoPianoDeiConti(registrazioneMovFin.getMovimento());
		if(elementoPianoDeiConti == null) {
			String msg = "Nessun Elemento del Piano dei Conti collegato al Movimento. ";
			log.info(methodName, msg+"Impossibile selezionare in automatico la Causale EP. Returning null.");
			msgBuilder.append(msg);
			return null;
		}
		
		
		log.debug(methodName, "Uid elemento del piano dei conti per cui filtrare: " + elementoPianoDeiConti.getUid());
		  
		Iterator<CausaleEP> it = causaliEP.iterator();
		while(it.hasNext()) {
		   CausaleEP causaleEP = it.next();
		   if(causaleEP.getElementoPianoDeiConti() == null || causaleEP.getElementoPianoDeiConti().getUid() != elementoPianoDeiConti.getUid()) {
			   it.remove();
		   }
		}
		
		// Se non ho causali
		if(causaliEP.isEmpty()) {
			String msg = "Nessuna Causale EP disponibile dopo aver filtrato per Elemento piano dei conti. "
					+ "";
			log.info(methodName, msg+"Impossibile selezionare in automatico la Causale EP. Returning null.");
			msgBuilder.append(msg);
			return null;
		}
		
		// Se ho un'unica causale, la prendo
		if(causaliEP.size() == 1) {
			CausaleEP result = causaliEP.get(0);
			log.info(methodName, "Un'unica Causale EP presente. La seleziono subito. Returning CausaleEP con uid: "+result.getUid());
			return result;
		}
		
		Soggetto soggetto = movimentoHandler.getSoggetto(registrazioneMovFin);
		log.debug(methodName, "Uid soggetto per cui filtrare: " + soggetto.getUid());
		
		for(it = causaliEP.iterator(); it.hasNext();) {
			CausaleEP causaleEP = it.next();
			if(causaleEP.getSoggetto() == null || causaleEP.getSoggetto().getUid() != soggetto.getUid()) {
				it.remove();
			}
		}
		
		if(causaliEP.isEmpty()) {
			String msg = "Nessuna causale EP selezionabile per il Soggetto (uid: " + soggetto.getUid() + "). ";
			log.info(methodName, msg + "Returning null.");
			msgBuilder.append(msg);
			return null;
		}
		// Se ho un'unica causale, la prendo
		if(causaliEP.size() == 1) {
			log.debug(methodName, "Un'unica causale collegata al soggetto con uid " + soggetto.getUid());
			return causaliEP.get(0);
		}
		
		String msg = "Nessuna Causale EP selezionabile per Elemento del Piano dei Conti con uid: " + elementoPianoDeiConti.getUid()
			+ " e Soggetto con uid: " + soggetto.getUid() + ".  ";
		
		log.info(methodName, msg + "Impossibile selezionare in automatico la Causale EP. Returning null.");
		msgBuilder.append(msg);
		return null;
		
	}
	
	
	private RegistraPrimaNotaIntegrataResponse registraPrimaNotaIntegrata(PrimaNota primaNota, Boolean isDaValidare) {
		RegistraPrimaNotaIntegrata reqRPNI = new RegistraPrimaNotaIntegrata();
		reqRPNI.setRichiedente(req.getRichiedente());
		
		reqRPNI.setPrimaNota(primaNota);
		reqRPNI.setIsAggiornamento(Boolean.FALSE);
		reqRPNI.setIsDaValidare(isDaValidare);
		
		RegistraPrimaNotaIntegrataResponse resRPNI = serviceExecutor.executeServiceSuccess(RegistraPrimaNotaIntegrataService.class, reqRPNI);
		return resRPNI;
	}


	private RicercaSinteticaCausaleResponse ricercaSinteticaCausaleCached(RegistrazioneMovFin registrazioneMovFin) {
		
		RicercaSinteticaCausale reqRSC = new RicercaSinteticaCausale();
		reqRSC.setRichiedente(req.getRichiedente());
		
		CausaleEP causEP = new CausaleEP();
		causEP.setTipoCausale(TipoCausale.Integrata);
		if(Ambito.AMBITO_FIN.equals(registrazioneMovFin.getAmbito())) {
			causEP.setElementoPianoDeiConti(registrazioneMovFin.getElementoPianoDeiContiAggiornato() /*getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato()*/);
		}
		causEP.setAmbito(registrazioneMovFin.getAmbito());
		causEP.setStatoOperativoCausaleEP(StatoOperativoCausaleEP.VALIDO);
		causEP.setEventi(Arrays.asList(registrazioneMovFin.getEvento()));
		
		reqRSC.setCausaleEP(causEP);
		reqRSC.setBilancio(registrazioneMovFin.getBilancio());
		reqRSC.setTipoEvento(registrazioneMovFin.getEvento().getTipoEvento());
		
		
		
		ParametriPaginazione pp = new ParametriPaginazione(0, 1000);
		reqRSC.setParametriPaginazione(pp);
		
		RicercaSinteticaCausaleResponse resRSC = serviceExecutor.executeServiceThreadLocalCachedSuccess(RicercaSinteticaCausaleService.class, reqRSC, new RicercaSinteticaCausaleKeyAdapter());
		
		if(resRSC.getTotalePagine()>1){
			throw new BusinessException("RicercaSinteticaCausale: Trovati piu' di " + pp.getElementiPerPagina() + " elementi! Aumentare il numero di elementi per pagina.");
		}
		
		return resRSC;
	}
	
}
