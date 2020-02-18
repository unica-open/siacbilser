/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di service per l'aggiornamento del Progetto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2014
 * @version 1.0.1 - 24/09/2015 (provvedimento non e' piu obbligatorio)
 * @version 1.0.2 - 02/02/2015 (SIAC-4427)
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAnagraficaProgettoService extends CheckedAccountBaseService<AggiornaAnagraficaProgetto, AggiornaAnagraficaProgettoResponse> {

	/** The progetto. */
	private Progetto progetto;
	
	/** The progetto dad. */
	@Autowired
	private ProgettoDad progettoDad;
	
	/** The provvedimento dad. */
	@Autowired
	private ProvvedimentoDad provvedimentoDad;
	
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;
	
	@Autowired
	private BilancioDad bilancioDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		progetto = req.getProgetto();
		
		checkEntita(progetto, "progetto");
		
		checkNotBlank(progetto.getCodice(), "codice progetto", false);
		checkNotNull(progetto.getStatoOperativoProgetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo progetto"), false);
		checkNotNull(progetto.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione progetto"), false);
		
		checkEntita(progetto.getEnte(), "ente", false);
		
//		checkEntita(progetto.getTipoAmbito(), "tipo ambito");
		
		// ***** CR 2305 PROVVEDIMENTO NON E' PIU OBBLIGATORIO  *****
		// SIAC-4427
		checkEntita(progetto.getAttoAmministrativo(), "provvedimento", false);
		
		//SIAC-6255
		checkNotNull(progetto.getTipoProgetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo progetto"));
		checkEntita(progetto.getBilancio(), "bilancio del progetto");
	}
	
	
	@Override
	protected void init() {
		progettoDad.setEnte(progetto.getEnte());
		progettoDad.setLoginOperazione(loginOperazione);
		bilancioDad.setEnteEntity(ente);
	}
	
	@Transactional
	public AggiornaAnagraficaProgettoResponse executeService(AggiornaAnagraficaProgetto serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkTipoProgettoCoerenteConFaseBilancio();
		// Controllo che non vi sia un altro progetto gia' attivo con lo stesso codice ma uid differente
		checkAltroProgettoGiaAttivoConStessoCodice();
		// Controllo che il provvedimento associato non sia in stato ANNULLATO
		/************CR 2305 *********/
		// SIAC-4427
		checkProvvedimentoNonAnnullato();
		//
		checkImportoCronoprogrammiCollegati();
		
		progettoDad.aggiornaProgetto(progetto);
		res.setProgetto(progetto);
	}
	
	private void checkImportoCronoprogrammiCollegati() {
		if(progetto.getValoreComplessivo() == null || BigDecimal.ZERO.compareTo( progetto.getValoreComplessivo()) ==0) {
			return;
		}
		List<String> cronoCheSuperanoIlValoreComplessivo = cronoprogrammaDad.findCronoprogrammiConSpeseMaggioriImportoProgetto(progetto);
		if(cronoCheSuperanoIlValoreComplessivo != null && !cronoCheSuperanoIlValoreComplessivo.isEmpty()) {
			throw new BusinessException(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" delle spese previste di alcuni cronoprogrammi associati al progetto ", " i seguenti cronoprogrammi superano il valore complessivo del progetto: " + StringUtils.join(cronoCheSuperanoIlValoreComplessivo, ", ")));
		}

	}


	private void checkTipoProgettoCoerenteConFaseBilancio() {
		Bilancio bilancio = bilancioDad.getBilancioByAnno(req.getAnnoBilancio());
		if(bilancio == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("bilancio", "anno " + req.getAnnoBilancio()));
		}
		if(bilancio.getFaseEStatoAttualeBilancio() == null || bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio() == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("bilancio", "impossibile caricare la fase bilancio"));
		}
		FaseEStatoAttualeBilancio faseEStatoAttualeBilancio = bilancio.getFaseEStatoAttualeBilancio();
		FaseBilancio faseBilancio = faseEStatoAttualeBilancio.getFaseBilancio();
		boolean faseBilancioPerProgettoPrevisione = FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio) || FaseBilancio.PREVISIONE.equals(faseBilancio);
		if((faseBilancioPerProgettoPrevisione && !TipoProgetto.PREVISIONE.equals(progetto.getTipoProgetto()) 
				|| (FaseBilancio.GESTIONE.equals(faseBilancio) && !TipoProgetto.GESTIONE.equals(progetto.getTipoProgetto())))){
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("fase bilancio: " + faseBilancio.name() + " e progetto: " + progetto.getTipoProgetto().getDescrizione()));
		}
	}


	/**
	 * Controllo che non vi sia un altro progetto con medesimo codice e stato VALIDO gi&agrave; presente.
	 */
	private void checkAltroProgettoGiaAttivoConStessoCodice() {
		Progetto p = progettoDad.ricercaPuntualeProgetto(progetto);
		if(p != null && p.getUid() != progetto.getUid()) {
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Progetto", progetto.getCodice()));
		}
	}
	
	/**
	 * Controlla che il Provvedimento fornito in input al servizio non sia in stato <code>ANNULLATO</code>.
	 */
	private void checkProvvedimentoNonAnnullato() {
		AttoAmministrativo attoAmministrativo = provvedimentoDad.findProvvedimentoById(progetto.getAttoAmministrativo().getUid());
		if(attoAmministrativo == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("atto amministrativo", "uid " + progetto.getAttoAmministrativo().getUid()));
		}
		if(StatoOperativo.ANNULLATO.name().equals(attoAmministrativo.getStatoOperativo())) {
			throw new BusinessException(ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore(""), Esito.FALLIMENTO);
		}
	}
	
}
