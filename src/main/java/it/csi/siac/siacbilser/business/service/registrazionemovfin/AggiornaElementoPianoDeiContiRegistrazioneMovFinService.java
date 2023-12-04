/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registrazionemovfin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedElabGroupBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaElementoPianoDeiContiRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaElementoPianoDeiContiRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;

/**
 * Aggiorna Elemento Piano Dei Conti RegistrazioneMovFin
 * 
 * Consente la correzione delle prime note: vedi CR SIAC-3648
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaElementoPianoDeiContiRegistrazioneMovFinService extends
		CheckedElabGroupBaseService<AggiornaElementoPianoDeiContiRegistrazioneMovFin, AggiornaElementoPianoDeiContiRegistrazioneMovFinResponse> {

	// Dads..
	@Autowired
	private RegistrazioneMovFinDad registrazioneMovFinDad;

	// Components..
	@Autowired
	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;

	// Felds..
	private RegistrazioneMovFin registrazioneMovFin;

	private boolean insPrimaNotaAutomaticaAsyncAvviato = false;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getRegistrazioneMovFin(), "registrazione");
		registrazioneMovFin = req.getRegistrazioneMovFin();

		checkEntita(req.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato(), "elemento piano dei conti aggiornato registrazione");
	}

	@Override
	protected void checkServiceParamControlloElaborazioneAttiva() throws ServiceParamError {
		checkEntita(req.getRegistrazioneMovFin(), "registrazione");
		registrazioneMovFin = req.getRegistrazioneMovFin();
	}

	@Override
	@Transactional
	public AggiornaElementoPianoDeiContiRegistrazioneMovFinResponse executeService(AggiornaElementoPianoDeiContiRegistrazioneMovFin serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void init() {
		super.init();
		registrazioneMovFinDad.setEnte(ente);
		registrazioneMovFinDad.setLoginOperazione(loginOperazione);

		insPrimaNotaAutomaticaAsyncAvviato = false;
	}

	@Override
	protected void executeUnique() {
		String methodName = "executeSerial";

		ElementoPianoDeiConti elementoPianoDeiContiAggiornato = registrazioneMovFin.getElementoPianoDeiContiAggiornato();
		registrazioneMovFin = registrazioneMovFinDad.findRegistrazioneMovFinById(registrazioneMovFin.getUid());

		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, registrazioneMovFin.getBilancio());
		registrazioneGENServiceHelper.flushAndClear();

		Entita movimento = registrazioneMovFin.getMovimento();
		if (movimento instanceof Subdocumento) {
			movimento = ((Subdocumento<?, ?>) movimento).getDocumento();
		}
		List<RegistrazioneMovFin> registrazioniMovFinPrecedenti = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(
				registrazioneMovFin.getEvento().getTipoCollegamento(), movimento, registrazioneMovFin.getAmbito());

		// Annulla la prima nota
		registrazioneGENServiceHelper.annullaPrimaNota(registrazioneMovFin);

		// Annulla tutte le sue registrazioni e le ricrea.
		registrazioneMovFinDad.aggiornaStatoRegistrazioneMovFin(registrazioneMovFin.getUid(), StatoOperativoRegistrazioneMovFin.ANNULLATO);
		log.debug(methodName, "registrazione annullata [uid:" + registrazioneMovFin.getUid() + "]. ");
		registrazioneMovFin.setStatoOperativoRegistrazioneMovFin(StatoOperativoRegistrazioneMovFin.ANNULLATO);

		RegistrazioneMovFin reg = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(registrazioneMovFin.getEvento(),
				registrazioneMovFin.getMovimento(), 
				registrazioneMovFin.getMovimentoCollegato(),
				registrazioneMovFin.getElementoPianoDeiContiIniziale(), // elemento
																													// piano
																													// dei
																													// conti
																													// originale
																													// della
																													// registrazione
																													// (prendo
																													// l'aggiornato
																													// che
																													// è
																													// l'ultimo
																													// valido)
				elementoPianoDeiContiAggiornato, registrazioneMovFin.getAmbito()); // elemento
																					// piano
																					// dei
																					// conti
																					// che
																					// sto
																					// aggiornando

		List<RegistrazioneMovFin> registrazioniNuove = new ArrayList<RegistrazioneMovFin>();
		registrazioniNuove.add(reg);

		// aggiornaro lo stato di tutte le altre registrazioni in "ANNULLATO".
		for (RegistrazioneMovFin regPrecedente : registrazioniMovFinPrecedenti) {
			if (regPrecedente.getMovimento().getUid() == registrazioneMovFin.getMovimento().getUid()) {
				continue; // questa regisrazione è quella che sto annullando
			}

			// regPrecedente =
			// registrazioneMovFinDad.findRegistrazioneMovFinById(regPrecedente.getUid());

			registrazioneMovFinDad.aggiornaStatoRegistrazioneMovFin(regPrecedente.getUid(), StatoOperativoRegistrazioneMovFin.ANNULLATO);
			log.debug(methodName, "registrazione annullata [uid:" + regPrecedente.getUid() + "]. Stato precedente: "
					+ regPrecedente.getStatoOperativoRegistrazioneMovFin());
			regPrecedente.setStatoOperativoRegistrazioneMovFin(StatoOperativoRegistrazioneMovFin.ANNULLATO);
			
			registrazioneGENServiceHelper.annullaPrimaNota(regPrecedente);

			RegistrazioneMovFin regMovFinNuova = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(regPrecedente.getEvento(),
					regPrecedente.getMovimento(), null ,regPrecedente.getElementoPianoDeiContiIniziale(),
					regPrecedente.getElementoPianoDeiContiAggiornato(), regPrecedente.getAmbito());

			registrazioniNuove.add(regMovFinNuova);

		}

		registrazioneGENServiceHelper.flushAndClear();

		if (registrazioniNuove != null && !registrazioniNuove.isEmpty()) {
			
			Errore errore = getErroreElaborazioneAttiva(null);
			
			ElabKeys elabKeyPrimaNota = ElabKeys.PRIMA_NOTA;
			ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(movimento.getUid(),this.getClass(),movimento.getClass(),  registrazioneMovFin.getAmbito().name());
			
			
			
			// aggiunge il suffisso "_NCD" nel caso sia una registrazione di una Nota di credito.
			if(movimento instanceof Documento) {
				Documento<?,?> documento = (Documento<?,?>) movimento;
				if(documento.getTipoDocumento().isNotaCredito()) {
					log.debug(methodName, "seleziono la chiave per nota di credito. ");
					elabKeyPrimaNota = ElabKeys.PRIMA_NOTA_NCD;
				}
			}
			String elabService    = eakh.creaElabServiceFromPattern(elabKeyPrimaNota);
			String elabKey        = eakh.creaElabKeyFromPattern(elabKeyPrimaNota);
			
			registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniNuove, elabService , errore, elabKey);

		}

		res.setRegistrazioneMovFin(registrazioneMovFin);

	}

	@Override
	protected boolean endElaborazioneAttiva() {
		if (insPrimaNotaAutomaticaAsyncAvviato) {
			return false;
		}
		return super.endElaborazioneAttiva();
	}

//	private boolean endElaborazioneAttivaForce() {
//		return super.endElaborazioneAttiva();
//	}

	@Override
	protected Errore getErroreElaborazioneAttiva(ElaborazioneAttivaException eae) {
		return ErroreBil.ELABORAZIONE_ATTIVA.getErrore("Esiste gia' una elaborazione attiva per questa Registrazione (chiave elaborazione: "+elabKeys+"). "
				+ "Attendere il termine dell'elaborazione precedente");
	}
	
	@Override
	protected String getGroup() {
		return RegistrazioneMovFin.class.getSimpleName();
	}

	@Override
	protected void initElabKeys() {
		elabKeys.add("RegistrazioneMovFin.uid:" + req.getRegistrazioneMovFin().getUid());
	}

	

}
