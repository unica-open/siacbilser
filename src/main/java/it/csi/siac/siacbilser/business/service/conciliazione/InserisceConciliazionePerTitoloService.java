/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conciliazione;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siacbilser.integration.dad.ConciliazioneDad;
import it.csi.siac.siacbilser.integration.dad.ContoDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.model.ConciliazionePerTitolo;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoModelDetail;

/**
 * Inserimento della conciliazione per titolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/10/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceConciliazionePerTitoloService extends CheckedAccountBaseService<InserisceConciliazionePerTitolo, InserisceConciliazionePerTitoloResponse> {

	@Autowired
	private ContoDad contoDad;
	@Autowired
	private ConciliazioneDad conciliazioneDad;
	@Autowired
	private ClassificatoriDad classificatoriDad;

	private ConciliazionePerTitolo conciliazionePerTitolo;
	private ClassificatoreGerarchico classificatoreGerarchico;

	@Override
	@Transactional
	public InserisceConciliazionePerTitoloResponse executeService(InserisceConciliazionePerTitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getConciliazionePerTitolo(), "conciliazione", true);
		conciliazionePerTitolo = req.getConciliazionePerTitolo();
		
		checkNotNull(conciliazionePerTitolo.getClasseDiConciliazione(), "classe di conciliazione", false);
		checkEntita(conciliazionePerTitolo.getConto(), "conto conciliazione", false);
		checkEntita(conciliazionePerTitolo.getEnte(), "ente conciliazione", false);
		checkEntita(conciliazionePerTitolo.getClassificatoreGerarchico(), "classificatore conciliazione", false);
		
		// Uno tra macroaggregato e categoria tipologia titolo
		checkCondition(conciliazionePerTitolo.getClassificatoreGerarchico() == null || conciliazionePerTitolo.getClassificatoreGerarchico().getUid() == 0
				|| (entitaValorizzata(conciliazionePerTitolo.getMacroaggregato()) ^ entitaValorizzata(conciliazionePerTitolo.getCategoriaTipologiaTitolo())),
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("solo uno tra macroaggregato e categoria puo' e deve essere valorizzato"));
		
		checkEntita(req.getBilancio(), "bilancio", true);
		checkCondition(req.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
	}
	
	private boolean entitaValorizzata(Entita entita) {
		return entita != null && entita.getUid() != 0;
	}
	
	@Override
	protected void init() {
		contoDad.setEnte(ente);
		contoDad.setLoginOperazione(loginOperazione);
		
		conciliazioneDad.setEnte(ente);
		conciliazioneDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		// 1. Controllo che il conto sia di GSA
		checkContoGSA();
		// 2. Controllo che i classificatori siano di tipo corretto
		checkClassificatori();
		// 3. Controllo univocita'
		checkNonGiaCollegato();
		// 4. Inserimento
		conciliazioneDad.inserisciConciliazionePerTitolo(conciliazionePerTitolo);
		// 5. Imposto i dati in response
		res.setConciliazionePerTitolo(conciliazionePerTitolo);
	}

	/**
	 * Controlli per il conto:
	 * <ul>
	 *     <li>ha un'istanza valida nell'anno di bilancio in corso</li>
	 *     <li>Conto.attivo = TRUE</li>
	 *     <li>&Eacute; un Conto foglia: Conto.contoFoglia = TRUE</li>
	 * </ul>
	 * Inoltre controllo che il conto da associare alla conciliazione sia dell'ambito GSA.
	 */
	private void checkContoGSA() {
		final String methodName = "checkContoGSA";
		Date inizioAnno = Utility.primoGiornoDellAnno(req.getBilancio().getAnno());
		Conto conto = contoDad.findContoById(conciliazionePerTitolo.getConto(), inizioAnno, ContoModelDetail.Attr);
		log.debug(methodName, "Conto trovato per uid [" + conciliazionePerTitolo.getConto().getUid() + "]? " + (conto != null ? "true" : "false"));
		if(conto == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto " + conciliazionePerTitolo.getConto().getCodice(),
					"non ha un'istanza valida nell'anno di bilancio in corso"));
		}
		
		log.debug(methodName, "Conto [uid: " + conto.getUid() + "] attivo? " + conto.getAttivo());
		if(!Boolean.TRUE.equals(conto.getAttivo())) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto " + conto.getCodice(), "non e' attivo"));
		}
		
		log.debug(methodName, "Conto [uid: " + conto.getUid() + "] foglia? " + conto.getContoFoglia());
		if(!Boolean.TRUE.equals(conto.getContoFoglia())) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto " + conto.getCodice(), "non e' un conto foglia"));
		}
		
		log.debug(methodName, "Conto [uid: " + conto.getUid() + "] ambito? " + conto.getAmbito());
		if(!Ambito.AMBITO_GSA.equals(conto.getAmbito())) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il conto " + conto.getCodice() + " non appartiene all'ambito GSA"));
		}
	}

	private void checkClassificatori() {
		final String methodName = "checkClassificatori";
		TipologiaClassificatore toCheck;
		
		if(entitaValorizzata(conciliazionePerTitolo.getMacroaggregato())) {
			classificatoreGerarchico = conciliazionePerTitolo.getMacroaggregato();
			toCheck = TipologiaClassificatore.MACROAGGREGATO;
		} else {
			classificatoreGerarchico = conciliazionePerTitolo.getCategoriaTipologiaTitolo();
			toCheck = TipologiaClassificatore.CATEGORIA;
		}
		
		log.debug(methodName, "Classificatore fornito in request: " + toCheck);
		TipologiaClassificatore tipologiaClassificatore = classificatoriDad.ricercaTipologiaClassificatoreByUidClassificatore(classificatoreGerarchico);
		log.debug(methodName, "Classificatore reperito dal db: " + tipologiaClassificatore);
		if(!toCheck.equals(tipologiaClassificatore)) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il classificatore che si vuole collegare e' di tipo " + tipologiaClassificatore));
		}
	}

	private void checkNonGiaCollegato() {
		final String methodName = "checkNonGiaCollegato";
		// Passo lo zero per indicare di non cercare per uid
		Long count = conciliazioneDad.countConciliazioniTitoloByContoAndClassificatoreAndClasse(conciliazionePerTitolo.getConto(), classificatoreGerarchico,
				conciliazionePerTitolo.getClasseDiConciliazione(), 0);
		log.debug(methodName, "Numero conciliazioni per conto [" + conciliazionePerTitolo.getConto().getUid() + "], classificatore [" + classificatoreGerarchico.getUid() + "], classe ["
				+ conciliazionePerTitolo.getClasseDiConciliazione() + "]: " + count);
		if(count != null && count.longValue() > 0L) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il conto e' gia' associato alla classe di conciliazione"));
		}
	}
}
