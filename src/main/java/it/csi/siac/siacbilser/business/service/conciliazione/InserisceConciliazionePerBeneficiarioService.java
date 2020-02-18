/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conciliazione;

import java.util.Date;
import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ConciliazioneDad;
import it.csi.siac.siacbilser.integration.dad.ContoDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.model.ConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoModelDetail;

/**
 * Inserimento della conciliazione per beneficiario.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/10/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceConciliazionePerBeneficiarioService extends CheckedAccountBaseService<InserisceConciliazionePerBeneficiario, InserisceConciliazionePerBeneficiarioResponse> {

	@Autowired
	private ContoDad contoDad;
	@Autowired
	private ConciliazioneDad conciliazioneDad;
	@Autowired
	private CapitoloDad capitoloDad;

	private ConciliazionePerBeneficiario conciliazionePerBeneficiario;

	@Override
	@Transactional
	public InserisceConciliazionePerBeneficiarioResponse executeService(InserisceConciliazionePerBeneficiario serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getConciliazionePerBeneficiario(), "conciliazione", true);
		conciliazionePerBeneficiario = req.getConciliazionePerBeneficiario();
		
		checkEntita(conciliazionePerBeneficiario.getCapitolo(), "capitolo conciliazione", false);
		checkEntita(conciliazionePerBeneficiario.getConto(), "conto conciliazione", false);
		checkEntita(conciliazionePerBeneficiario.getEnte(), "ente conciliazione", false);
		checkEntita(conciliazionePerBeneficiario.getSoggetto(), "soggetto conciliazione", false);
		
		checkNotNull(conciliazionePerBeneficiario.getClasseDiConciliazione(), "classe di conciliazione", false);
		
		checkEntita(req.getBilancio(), "bilancio", false);
	}
	
	@Override
	protected void init() {
		contoDad.setEnte(ente);
		contoDad.setLoginOperazione(loginOperazione);
		
		conciliazioneDad.setEnte(ente);
		conciliazioneDad.setLoginOperazione(loginOperazione);
		
		capitoloDad.setEnte(ente);
		capitoloDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		// 1. Controllo che il capitolo sia di tipo corretto
		checkCapitolo();
		// 2. Controllo sul soggetto
		checkSoggetto();
		// 3. Controllo che il conto sia di GSA
		checkContoGSA();
		// 4. Controllo univocita'
//		checkNonGiaCollegato();
		// 5. Inserimento
		conciliazioneDad.inserisciConciliazionePerBeneficiario(conciliazionePerBeneficiario);
		// 6. Imposto i dati in response
		res.setConciliazionePerBeneficiario(conciliazionePerBeneficiario);
	}

	private void checkCapitolo() {
		final String methodName = "checkCapitolo";
		TipoCapitolo tipoCapitolo = capitoloDad.findTipoCapitolo(conciliazionePerBeneficiario.getCapitolo().getUid());
		
		log.debug(methodName, "Tipo capitolo per il capitolo [" + conciliazionePerBeneficiario.getCapitolo().getUid() + "]: " + tipoCapitolo);
		if(!TipoCapitolo.isTipoCapitoloGestione(tipoCapitolo)) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il capitolo che si vuole collegare e' di tipo " + tipoCapitolo));
		}
		conciliazionePerBeneficiario.getCapitolo().setTipoCapitolo(tipoCapitolo);
	}

	private void checkSoggetto() {
		// TODO: ci sono dei controlli di validita'? Del tipo stato?
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
		
		Conto conto = contoDad.findContoById(conciliazionePerBeneficiario.getConto(), inizioAnno, ContoModelDetail.Attr);
		log.debug(methodName, "Conto trovato per uid [" + conciliazionePerBeneficiario.getConto().getUid() + "]? " + (conto != null ? "true" : "false"));
		if(conto == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto " + conciliazionePerBeneficiario.getConto().getCodice(),
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
		
		ClassificatoreGerarchico classificatoreGerarchico = findClassificatoreGerarchicoByCapitolo(conciliazionePerBeneficiario.getCapitolo());
		log.debug(methodName, "Capitolo [uid: " + conciliazionePerBeneficiario.getCapitolo().getUid() + "]. Classificatore? " + classificatoreGerarchico.getUid());
		Long count = conciliazioneDad.countConciliazioniTitoloByContoAndClassificatoreAndClasse(conto, classificatoreGerarchico, conciliazionePerBeneficiario.getClasseDiConciliazione(), 0);
		log.debug(methodName, "Conto [uid: " + conto.getUid() + "], classificatore [" + classificatoreGerarchico.getUid() + "]. Numero collegamenti? " + count);
		if(count == null || count.longValue() != 1L) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il conto " + conto.getCodice() + " non e' collegato alla classificazione del capitolo"));
		}
		
		conciliazionePerBeneficiario.getConto().setCodice(conto.getCodice());
	}

	private ClassificatoreGerarchico findClassificatoreGerarchicoByCapitolo(Capitolo<?, ?> capitolo) {
		if(TipoCapitolo.isTipoCapitoloEntrata(capitolo)) {
			return findClassificatoreGerarchicoByCapitolo(capitolo, TipologiaClassificatore.CATEGORIA);
		}
		if(TipoCapitolo.isTipoCapitoloUscita(capitolo)) {
			return findClassificatoreGerarchicoByCapitolo(capitolo, TipologiaClassificatore.MACROAGGREGATO);
		}
		// Non dovrebbe MAI capitare
		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il capitolo che si vuole collegare e' di tipo " + capitolo.getTipoCapitolo()));
	}
	
	private <C extends ClassificatoreGerarchico> C findClassificatoreGerarchicoByCapitolo(Capitolo<?, ?> capitolo, TipologiaClassificatore tipologiaClassificatore) {
		final String methodName = "findClassificatoreGerarchicoByCapitolo";
		C cg = capitoloDad.ricercaClassificatoreGerarchico(capitolo.getUid(), tipologiaClassificatore, EnumSet.of(tipologiaClassificatore));
		log.debug(methodName, "Capitolo [" + capitolo.getUid() +"]. Trovato classificatore " + tipologiaClassificatore + "? " + (cg != null ? "true, con uid " + cg.getUid() : "false"));
		
		if(cg == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore(tipologiaClassificatore, "per capitolo " + capitolo.getUid()));
		}
		return cg;
	}

	private void checkNonGiaCollegato() {
		final String methodName = "checkNonGiaCollegato";
		// Passo lo zero per indicare di non cercare per uid
		Long count = conciliazioneDad.countConciliazioniBeneficiarioByContoAndCapitoloAndSoggetto(conciliazionePerBeneficiario.getConto(), conciliazionePerBeneficiario.getCapitolo(),
				conciliazionePerBeneficiario.getSoggetto(), 0);
		log.debug(methodName, "Numero conciliazioni per conto [" + conciliazionePerBeneficiario.getConto().getUid() + "], capitolo [" + conciliazionePerBeneficiario.getCapitolo().getUid()
				+ "], soggetto [" + conciliazionePerBeneficiario.getSoggetto().getUid() + "]: " + count);
		if(count != null && count.longValue() > 0L) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il conto " + conciliazionePerBeneficiario.getConto().getCodice() + " e' gia' associato alla classe di conciliazione"));
		}
	}
}
