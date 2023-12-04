/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conciliazione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siacbilser.integration.dad.ConciliazioneDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasseResponse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Ricerca dei conti da associare ad una causale EP
 * 
 * @author Valentina
 * @version 1.0.0 - 02/11/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaContiConciliazionePerClasseService extends CheckedAccountBaseService<RicercaContiConciliazionePerClasse, RicercaContiConciliazionePerClasseResponse> {

	@Autowired
	private CapitoloDad capitoloDad;
	@Autowired
	private SoggettoDad soggettoDad;
	@Autowired
	private ConciliazioneDad conciliazioneDad;
	@Autowired
	private CausaleEPDad causaleEPDad;
	
	private Capitolo<?,?> capitolo;
	private ClasseDiConciliazione classeDiConciliazione;

	@Override
	@Transactional
	public RicercaContiConciliazionePerClasseResponse executeService(RicercaContiConciliazionePerClasse serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getClasseDiConciliazione(), "classe di conciliazione");
		checkEntita(req.getCapitolo(), "capitolo");
	}
	
	@Override
	protected void init() {
		conciliazioneDad.setEnte(ente);
		conciliazioneDad.setLoginOperazione(loginOperazione);
	}
	
	
	@Override
	protected void execute() {
		String methodName = "execute";

		capitolo = capitoloDad.findOne(req.getCapitolo().getUid());
		classeDiConciliazione = req.getClasseDiConciliazione();
		
		caricaContiBeneficiarioCapitoloTitolo();
	}

	/**
	 * @param methodName
	 * @param classeDiConciliazione
	 */
	private void caricaContiPerTitolo() {
		final String methodName = "caricaContiPerTitolo";
		ClassificatoreGerarchico classificatore = caricaClassificatore();
		
		if(classificatore == null){
			log.debug(methodName, "Il capitolo trovato non ha macroaggregato/categoria. Non posso cercare le conciliazioni per titolo.");
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return;
		}
		
		log.debug(methodName, "trovato classificatore con uid: " + classificatore.getUid());
		List<Conto> contiPerTitolo = conciliazioneDad.ricercaContiConciliazionePerTitolo(classeDiConciliazione, classificatore);
		if(contiPerTitolo == null || contiPerTitolo.isEmpty()){
			log.debug(methodName, "Nessuna conciliazione per titolo trovata.");
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return;
		}
		
		res.setConti(contiPerTitolo);
	}

	/**
	 * @param methodName
	 * @param classeDiConciliazione
	 * @param contiPerTitolo
	 */
	private void caricaContiPerCapitolo( ) {
		final String methodName = "caricaContiPerCapitolo";
		List<Conto> contiPerCapitolo = conciliazioneDad.ricercaContiConciliazionePerCapitoloEClasse(capitolo,classeDiConciliazione);
		if(contiPerCapitolo == null || contiPerCapitolo.isEmpty()){
			log.debug(methodName, "Nessuna conciliazione per capitolo trovata.  Proseguo ricercando le conciliazioni per titolo.");
			caricaContiPerTitolo();
			return;
		}
		res.setConti(contiPerCapitolo);
	}

	/**
	 * @param methodName
	 * @param classeDiConciliazione
	 * @param contiPerCapitolo
	 */
	protected void caricaContiBeneficiarioCapitoloTitolo() {
		
		final String methodName = "caricaContiBeneficiarioCapitoloTitolo";
		
		if(req.getSoggetto() == null || req.getSoggetto().getUid() == 0){
			log.debug(methodName, "Nessun soggetto trovato, non posso cercare conciliazioni per beneficiario. Proseguo ricercando le conciliazioni per capitolo.");
			caricaContiPerCapitolo();
			return;
		}
		
		List<Conto> contiPerBeneficiario = conciliazioneDad.ricercaContiConciliazionePerBeneficiarioEClasse(req.getSoggetto(), capitolo,classeDiConciliazione);
		if(contiPerBeneficiario == null || contiPerBeneficiario.isEmpty()){
			log.debug(methodName, "Nessuna conciliazione per beneficiario trovata. Proseguo ricercando le conciliazioni per capitolo.");
			caricaContiPerCapitolo();
			return;
		}
		res.setConti(contiPerBeneficiario);
	}

	private ClassificatoreGerarchico caricaClassificatore() {
		TipoCapitolo tipoCapitolo = capitoloDad.findTipoCapitolo(capitolo.getUid());
		
		TipologiaClassificatore tc;
		if(TipoCapitolo.isTipoCapitoloEntrata(tipoCapitolo)){
			tc = TipologiaClassificatore.CATEGORIA;
		}else{
			tc = TipologiaClassificatore.MACROAGGREGATO;
		}
		
		return capitoloDad.ricercaClassificatoreGerarchico(capitolo.getUid(), tc, null);
	}

	
}
