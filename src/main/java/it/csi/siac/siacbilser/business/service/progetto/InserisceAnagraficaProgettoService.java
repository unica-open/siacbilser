/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserisce l'anagrafica del progetto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2014
 * @version 1.0.1 - 24/09/2015 (provvedimento non e' piu obbligatorio)
 * @version 1.0.2 - 02/02/2017 (SIAC-4427)
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceAnagraficaProgettoService extends CheckedAccountBaseService<InserisceAnagraficaProgetto, InserisceAnagraficaProgettoResponse> {

	private static final String CODICE_PROGETTO_AUTOMATICO = "CODICE_PROGETTO_AUTOMATICO";

	private Progetto progetto;
	private boolean codiceAutomatico;
	
	@Autowired private BilancioDad bilancioDad;
	@Autowired private ProgettoDad progettoDad;
	@Autowired private AttoAmministrativoDad attoAmministrativoDad;
	@Autowired private ClassificatoriDad classificatoriDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		progetto = req.getProgetto();
		codiceAutomatico = req.getCodiceAutomatico() != null ? req.getCodiceAutomatico().booleanValue() :
				CODICE_PROGETTO_AUTOMATICO.equals(ente.getGestioneLivelli().get(TipologiaGestioneLivelli.CODICE_PROGETTO_AUTOMATICO));
		
		checkNotNull(progetto, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progetto"));
		checkNotNull(progetto.getStatoOperativoProgetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo progetto"), false);
		checkNotNull(progetto.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione progetto"), false);
		
		checkEntita(progetto.getEnte(), "ente", false);
		
//		checkEntita(progetto.getTipoAmbito(), "tipo ambito", false);
		// ***** CR 2305 PROVVEDIMENTO NON E' PIU OBBLIGATORIO  *****
//		checkEntita(progetto.getAttoAmministrativo(), "provvedimento");
		
		// SIAC-4427
		checkCondition(codiceAutomatico || StringUtils.isNotBlank(progetto.getCodice()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice progetto"), false);
		checkEntita(progetto.getAttoAmministrativo(), "provvedimento", false);
		checkEntita(req.getBilancio(), "bilancio");
		
		//SIAC-6255
		checkNotNull(progetto.getTipoProgetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo progetto"));
		
	}
	
	@Override
	protected void init() {
		progettoDad.setEnte(progetto.getEnte());
		progettoDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	@Transactional
	public InserisceAnagraficaProgettoResponse executeService(InserisceAnagraficaProgetto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		Bilancio bilancio = caricaBilancio();
		//SIAC-+6255
		checkTipoProgettoCoerenteConFaseBilancio(bilancio);
		
		staccaCodice(bilancio);		
		progetto.setBilancio(bilancio);
		
		checkAltroProgettoGiaAttivoConStessoCodice();
		// Controllo che il provvedimento non sia annullato
		// ************CR 2305 *********
		// SIAC-4427 ripristino
		checkProvvedimentoNonAnnullato();
		
		// SIAC-8814
		if (req.getRetrieveAggregateData()) {
			checkTipoAmbito(progetto);
			checkStrutturaAmministrativoContabile(progetto);
		}
		
		progettoDad.inserisciProgetto(progetto);
		res.setProgetto(progetto);
	}
	
	private void checkStrutturaAmministrativoContabile(Progetto progetto) {
		if (progetto.getStrutturaAmministrativoContabile() == null || progetto.getStrutturaAmministrativoContabile().getUid() > 0) {
			return;
		}
		
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = (StrutturaAmministrativoContabile) classificatoriDad.ricercaClassificatoreGerarchicoByCodiceAndTipoAndAnno(
				progetto.getStrutturaAmministrativoContabile(), 
				TipologiaClassificatore.fromCodice(progetto.getStrutturaAmministrativoContabile().getTipoClassificatore().getCodice()), 
				Integer.valueOf(ente.getUid()), 
				Integer.valueOf(req.getBilancio().getAnno()));

		if (strutturaAmministrativoContabile == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("strutturaAmministrativoContabile", 
					String.format("%s/%s", progetto.getStrutturaAmministrativoContabile().getCodice(), progetto.getStrutturaAmministrativoContabile().getTipoClassificatore().getCodice())));
		}
		
		progetto.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile); 
	}

	private void checkTipoAmbito(Progetto progetto) {
		
		if (progetto.getTipoAmbito() == null || progetto.getTipoAmbito().getUid() > 0) {
			return;
		}
		
		TipoAmbito tipoAmbito = progettoDad.ricercaTipoAmbito(req.getBilancio().getAnno(), progetto.getTipoAmbito().getCodice());

		if (tipoAmbito.getUid() == 0) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("tipoAmbito", "codice " + progetto.getTipoAmbito().getCodice()));
		}
		
		progetto.setTipoAmbito(tipoAmbito);
	}

	private void checkTipoProgettoCoerenteConFaseBilancio(Bilancio bilancio) {
		FaseEStatoAttualeBilancio faseEStatoAttualeBilancio = bilancio.getFaseEStatoAttualeBilancio();
		FaseBilancio faseBilancio = faseEStatoAttualeBilancio.getFaseBilancio();
		boolean faseBilancioPerProgettoPrevisione = FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio) || FaseBilancio.PREVISIONE.equals(faseBilancio);
		//SIAC-8900
		if(!req.getByPassFaseBilancioProgetto()) {
			if((faseBilancioPerProgettoPrevisione && !TipoProgetto.PREVISIONE.equals(progetto.getTipoProgetto()) 
					|| (FaseBilancio.GESTIONE.equals(faseBilancio) && !TipoProgetto.GESTIONE.equals(progetto.getTipoProgetto())))){
				throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("fase bilancio: " + faseBilancio.name() + " e progetto: " + progetto.getTipoProgetto().getDescrizione()));
			}
		}
	}
	
	/**
	 * Stacca il codice del progetto se necessario
	 * @param bilancio 
	 */
	private void staccaCodice(Bilancio bilancio) {
		final String methodName = "staccaCodice";
		if(!codiceAutomatico) {
			log.debug(methodName, "Il codice e' stato inserito manualmente");
			return;
		}
		String codice = progettoDad.staccaCodiceProgetto(bilancio.getAnno(), ente);
		log.debug(methodName, "Staccato codice " + codice + " per il progetto (anno " + bilancio.getAnno() + ", ente " + ente.getUid() + ")");
		progetto.setCodice(codice);
	}

	/**
	 * @return
	 */
	private Bilancio caricaBilancio() {
		Bilancio bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
		if(bilancio == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("bilancio", "uid " + req.getBilancio().getUid()));
		}
		if(bilancio.getFaseEStatoAttualeBilancio() == null || bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio() == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("bilancio", "impossibile caricare la fase bilancio"));
		}
		
		return bilancio;
	}

	/**
	 * Controllo che non vi sia un altro progetto con medesimo codice e stato VALIDO gi&agrave; presente.
	 */
	private void checkAltroProgettoGiaAttivoConStessoCodice() {
		Progetto p = progettoDad.ricercaPuntualeProgetto(progetto);
		if(p != null) {
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Progetto", progetto.getCodice()));
		}
	}

	/**
	 * Controlla che il Provvedimento fornito in input al servizio non sia in stato ANNULLATO.
	 *
	 * @param attoAmministrativo the atto amministrativo
	 */
	private void checkProvvedimentoNonAnnullato() {
		AttoAmministrativo attoConStato = attoAmministrativoDad.findProvvedimentoById(progetto.getAttoAmministrativo().getUid());
		if(attoConStato == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("atto amministrativo", "uid " + progetto.getAttoAmministrativo().getUid()));
		}
		if(StatoOperativo.ANNULLATO.name().equals(attoConStato.getStatoOperativo())) {
			throw new BusinessException(ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore());
		}
	}
	
}
