/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.provvedimento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.integration.exception.AttoNonTrovatoException;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * The Class AggiornaProvvedimentoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaProvvedimentoService extends CheckedAccountBaseService<AggiornaProvvedimento, AggiornaProvvedimentoResponse> {
	
	/** The provvedimento dad. */
	@Autowired
	private AttoAmministrativoDad attoAmministrativoDad;
	@Autowired
	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	@Autowired
	private ClassificatoriDad classificatoriDad;
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;
	
	
	private StatoOperativoAtti statoOperativoAttuale;
	
	private TipoAtto tipoAtto;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getEnte(), "ente", true);
		checkEntita(req.getAttoAmministrativo(), "atto amministrativo");
		checkCondition(req.getAttoAmministrativo().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno atto amministrativo"), true);
		checkCondition(req.getAttoAmministrativo().getNumero() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero atto amministrativo"), true);
		checkEntita(req.getTipoAtto(), "tipo atto amministrativo", true);
		
		this.tipoAtto = req.getTipoAtto();
		this.strutturaAmministrativoContabile = req.getStrutturaAmministrativoContabile();
		
		//???checkNotNull(req.getAttoAmministrativo().getStatoOperativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Codice stato operativo"));
		
		// SIAC-4285
		if(req.getAttoAmministrativo().getParereRegolaritaContabile() == null) {
			req.getAttoAmministrativo().setParereRegolaritaContabile(Boolean.FALSE);
		}
	}
	
	@Transactional
	public AggiornaProvvedimentoResponse executeService(AggiornaProvvedimento serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		attoAmministrativoDad.setLoginOperazione(loginOperazione);
		attoAmministrativoDad.setEnte(req.getEnte());
		
		cronoprogrammaDad.setEnte(ente);
		cronoprogrammaDad.setLoginOperazione(loginOperazione);
		
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione);
	}
	

	@Override
	protected void execute() {
		String methodName = "execute";
		
		caricaStatoOperativoAttuale();
		checkAnnullabilita();
		
		caricaTipoAtto();
		caricaStrutturaAmministrativoContabile();
		checkEsistenzaProvvedimento();
		
		AttoAmministrativo attoAmministrativoAggiornato;
		try {
			
			attoAmministrativoAggiornato = attoAmministrativoDad.update(req.getAttoAmministrativo(), req.getStrutturaAmministrativoContabile(), req.getTipoAtto(), req.getCodiceInc());
			
			// RM: il metodo aggiornaStatoImpegniEAccertamentiCollegati usa la response per leggere l'atto amministrativo per elaborare l'aggiornamento dello stato
			// del provvedimento 
			// FIXME: se non si vuole gestire l'atto da res modificare il metodo
			res.setAttoAmministrativoAggiornato(attoAmministrativoAggiornato);
			
			effettuaOperazioniCollegateCambioStatoProvvedimento(attoAmministrativoAggiornato);
			
			
			
		} catch (AttoNonTrovatoException e) {
			log.warn(methodName, "Atto non trovato. ", e);
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("aggiornamento", String.format("Atto di Legge: %s/%s/%s", req.getAttoAmministrativo().getUid(), req.getAttoAmministrativo().getAnno(), req.getAttoAmministrativo().getNumero())));
			res.setEsito(Esito.FALLIMENTO);
			setRollbackOnly();
			return;
		}
		// SIAC-5639
		if(req.getAttoAmministrativo().getStatoOperativoAtti() == null) {
			// Ripristino il vecchio stato
			req.getAttoAmministrativo().setStatoOperativo(this.statoOperativoAttuale);
		}
		res.setAttoAmministrativoAggiornato(attoAmministrativoAggiornato);
		
	}

	/**
	 * @param attoAmministrativoAggiornato 
	 */
	protected void effettuaOperazioniCollegateCambioStatoProvvedimento(AttoAmministrativo attoAmministrativoAggiornato) {
		final String methodName="effettuaOperazioniCollegateCambioStatoProvvedimento";
		
		boolean isProvvedimentoDefinitivo = StatoOperativoAtti.DEFINITIVO.equals(req.getAttoAmministrativo().getStatoOperativoAtti());
		// SIAC-5639: girate le condizioni per prevenire NPE
		boolean isPassaggioDiStatoDaProvvisorioADefinitivo = StatoOperativoAtti.PROVVISORIO.equals(this.statoOperativoAttuale) && isProvvedimentoDefinitivo;
		//boolean isPassaggioDiStatoAdAnnullato = !this.statoOperativoAttuale.equals(StatoOperativoAtti.ANNULLATO) && req.getAttoAmministrativo().getStatoOperativoAtti().equals(StatoOperativoAtti.ANNULLATO);
		log.debug(methodName, "isPassaggioDiStatoDaProvvisorioADefinitivo: "+ isPassaggioDiStatoDaProvvisorioADefinitivo 
				 /*+  " isPassaggioDiStatoAdAnnullato: "+ isPassaggioDiStatoAdAnnullato*/);
		aggiornaStatoImpegniEAccertamentiCollegati(isPassaggioDiStatoDaProvvisorioADefinitivo, isProvvedimentoDefinitivo);
			
		//SIAC-6255
		aggiornaStatoCronoprogrammiCollegati(isPassaggioDiStatoDaProvvisorioADefinitivo, attoAmministrativoAggiornato);
	}

	private void aggiornaStatoCronoprogrammiCollegati(boolean isPassaggioDiStatoDaProvvisorioADefinitivo, AttoAmministrativo attoAmministrativoAggiornato) {
		if(!isPassaggioDiStatoDaProvvisorioADefinitivo) {
			return;
		}
		cronoprogrammaDad.aggiornaStatoCronoprogrammiByProvvedimento(attoAmministrativoAggiornato, StatoOperativoCronoprogramma.PROVVISORIO, StatoOperativoCronoprogramma.VALIDO);
	}

	/**
	 * Aggiorna stato impegni e accertamenti collegati.
	 * 
	 * Se l'operazione di aggiornamento comporta un passaggio di stato Provvisorio – Definitivo 
	 * il sistema aggiorna coerentemente anche gli stati dei movimenti finanziari (Impegni ed Accertamenti) associati al provvedimento
	 * @param isPassaggioDiStatoDaProvvisorioADefinitivo 
	 * @param isProvvedimentoDefinitivo 
	 * 
	 */
	private void aggiornaStatoImpegniEAccertamentiCollegati(boolean isPassaggioDiStatoDaProvvisorioADefinitivo, boolean isProvvedimentoDefinitivo) {
		final String methodName = "aggiornaStatoImpegniEAccertamentiCollegati";
		
		//SIAC-8729
		if(isPassaggioDiStatoDaProvvisorioADefinitivo) {
			List<Integer> listaRegistrazioneMovFin = attoAmministrativoDad.aggiornaStatoImpegniEAccertamentiCollegati(res.getAttoAmministrativoAggiornato(), req.getAttoAmministrativo().getStatoOperativoAtti(), req.getIsEsecutivo());
			// TODO: verificare il ritorno della function utilizzata per l'aggiornamento degli stati degli impegni collegati.
			log.error(methodName, "Impegni e accertamenti collegati aggiornati correttamente. Numero registrazioni create: " + listaRegistrazioneMovFin.size());
			inserisciPrimeNoteAutomatiche(listaRegistrazioneMovFin);
		}else if(Boolean.TRUE.equals(req.getIsEsecutivo())&& isProvvedimentoDefinitivo){
			//SIAC-8729
			 attoAmministrativoDad.aggiornaParereFinanziarioImpegniEAccertamentiCollegati(res.getAttoAmministrativoAggiornato(), req.getIsEsecutivo());
			 log.error(methodName, "aggiornato il solo parerefinanziario");
		}
		
	}
	
	
	/**
	 * Carica stato operativo attuale.
	 */
	private void caricaStatoOperativoAttuale() {
		String methodName = "caricaStatoOperativoAttuale";
		this.statoOperativoAttuale = attoAmministrativoDad.findStatoOperativoAttoAmministrativo(req.getAttoAmministrativo());
		log.debug(methodName, "statoOperativoAttuale: "+this. statoOperativoAttuale);
	}
	
	

	/**
	 * Controlla, nel caso il nuovo stato sia ANULLATO, se il provvedimento è annullabile.
	 */
	private void checkAnnullabilita() {
		String methodName = "checkAnnullabilita";
		if(!StatoOperativoAtti.ANNULLATO.equals(req.getAttoAmministrativo().getStatoOperativoAtti())){
			log.debug(methodName, "Atto non da annullare.");
			return;
		}
		
		if(StatoOperativoAtti.ANNULLATO.equals(this.statoOperativoAttuale)){
			log.debug(methodName, "Atto gia' annullato. Salto la verifica di annullabilità.");
			return;
		}
		
		Boolean isAnnullabile = isAnnullabile();
		log.debug(methodName, "isAnnullabile: "+isAnnullabile);
		
		if(!Boolean.TRUE.equals(isAnnullabile)){
			throw new BusinessException(ErroreCore.ANNULLAMENTO_NON_POSSIBILE.getErrore(), Esito.FALLIMENTO);
		}
	}

	protected Boolean isAnnullabile() {
		return attoAmministrativoDad.isAnnullabile(req.getAttoAmministrativo());
	}

	/**
	 * Inserisci una PrimaNota per ogni uid RegistrazioneMovFin passato come parametro.
	 *
	 * @param listaUidRegistrazioneMovFin lista degli uid RegistrazioneMovFin
	 */
	private void inserisciPrimeNoteAutomatiche(List<Integer> listaUidRegistrazioneMovFin) {
		// Creo la lista delle registrazioni a partire dagli uid delle stesse
		for(Integer uid : listaUidRegistrazioneMovFin) {
			RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
			registrazioneMovFin.setUid(uid);
			registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFin);
		}
	}
	
	private void caricaTipoAtto() {
		TipoAtto ta = attoAmministrativoDad.findTipoAttoByUid_DEPRECATED(this.tipoAtto.getUid());
		if(ta == null || ta.getUid() == 0) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Tipo atto", "uid " + this.tipoAtto.getUid()));
		}
		this.tipoAtto = ta;
	}

	private void caricaStrutturaAmministrativoContabile() {
		if(this.strutturaAmministrativoContabile == null || this.strutturaAmministrativoContabile.getUid() == 0) {
			// Non ho alcunche' da cercare
			this.strutturaAmministrativoContabile = null;
			return;
		}
		StrutturaAmministrativoContabile sac = classificatoriDad.ricercaStrutturaAmministrativoContabile(this.strutturaAmministrativoContabile.getUid());
		if(sac == null || sac.getUid() == 0) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Struttura Amministrativo Contabile", "uid " + this.strutturaAmministrativoContabile.getUid()));
		}
		this.strutturaAmministrativoContabile = sac;
	}
	
	/**
	 * Dovrebbe essere possibile inserire, per uno stesso ente, stesso anno e stesso codice, 
	 * atti appartenenti a strutture diverse o con tipologie diverse.
	 */
	private void checkEsistenzaProvvedimento() {
		final String methodName = "checkEsistenzaProvvedimento";
		RicercaProvvedimento extReq = new RicercaProvvedimento();
		
		extReq.setEnte(req.getEnte());
		extReq.setRichiedente(req.getRichiedente());
		
		RicercaAtti ricerca = new RicercaAtti();
		ricerca.setAnnoAtto(req.getAttoAmministrativo().getAnno());
		ricerca.setNumeroAtto(req.getAttoAmministrativo().getNumero());
		//Dovrebbe essere possibile inserire, per uno stesso ente, stesso anno e stesso codice, 
		//atti appartenenti a strutture diverse o con tipologie diverse. (JIRA 1636)
		ricerca.setTipoAtto(req.getTipoAtto());
		ricerca.setStrutturaAmministrativoContabile(req.getStrutturaAmministrativoContabile());
		extReq.setRicercaAtti(ricerca);
		
		RicercaProvvedimentoResponse extRes = serviceExecutor.executeServiceSuccess(RicercaProvvedimentoService.class, extReq);
		
		final List<AttoAmministrativo> listaAttiAmministrativi = extRes.getListaAttiAmministrativi();
		if(listaAttiAmministrativi == null || listaAttiAmministrativi.isEmpty()) {
			log.debug(methodName, "Lista inesistente o vuota. Proseguo");
			// Non ho alcun atto amministrativo con dato numero. Va bene, lo aggiorno all'attuale
			return;
		}
		final int size = listaAttiAmministrativi.size();
		final int uidProvvedimento = req.getAttoAmministrativo().getUid();
		
		log.debug(methodName, "Lista non vuota. Numero dei risultati: " + size);
		// Se filtro per SAC, devo averne al piu' una
		
		if(req.getStrutturaAmministrativoContabile() != null && req.getStrutturaAmministrativoContabile().getUid() != 0 && size > 1) {
			String key = calcolaChiaveProvvedimento(req.getAttoAmministrativo(), tipoAtto, strutturaAmministrativoContabile);
			log.debug(methodName, "Esiste gia' piu' di un elemento nella lista, e non e' stato applicato il filtro per SAC. Errore: non aggiorno il Provvedimento " + key + " con uid " + uidProvvedimento);
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Provvedimento", key));
		}
		
		if(size == 1) {
			// Controllo che abbia il mio stesso id (mi sto aggiornando, in fondo
			AttoAmministrativo aa = listaAttiAmministrativi.get(0);
			log.debug(methodName, "Vi e' un unico elemento nella lista. Controllo che sia lo stesso che ho fornito. Mio uid " + uidProvvedimento + "- uid dalla lista " + aa.getUid());
			if(aa.getUid() != uidProvvedimento) {
				log.debug(methodName, "Errore: uid differenti");
				String key = calcolaChiaveProvvedimento(req.getAttoAmministrativo(), tipoAtto, strutturaAmministrativoContabile);
				throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Provvedimento", key));
			}
			// Tutto bene, esco
			return;
		}
		
		// Sono nel caso nessuna SAC + lista. Cerco che ci sia esattamente un valore con il mio id
		log.debug(methodName, "SAC non impostata come filtro. Controllo che al peggio l'unico elemento senza SAC abbia uid pari al mio");
		for(AttoAmministrativo aa : listaAttiAmministrativi) {
			if(aa.getUid() != uidProvvedimento && (aa.getStrutturaAmmContabile() == null || aa.getStrutturaAmmContabile().getUid() == 0)) {
				log.debug(methodName, "Errore: uid differenti per SAC " + aa.getUid());
				String key = calcolaChiaveProvvedimento(req.getAttoAmministrativo(), tipoAtto, strutturaAmministrativoContabile);
				throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Provvedimento", key));
			}
		}
	}
	
	private String calcolaChiaveProvvedimento(AttoAmministrativo attoAmministrativo, TipoAtto tipoAtto, StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(attoAmministrativo.getAnno());
		sb.append(" - ");
		sb.append(attoAmministrativo.getNumero());
		sb.append(" - ");
		sb.append(tipoAtto.getDescrizione());
		if(strutturaAmministrativoContabile != null && strutturaAmministrativoContabile.getUid() != 0) {
			sb.append(" - Struttura ");
			sb.append(strutturaAmministrativoContabile.getCodice());
		}
		
		return sb.toString();
	}



	
}
