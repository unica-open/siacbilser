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
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerCausaleEP;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerCausaleEPResponse;
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
public class RicercaContiConciliazionePerCausaleEPService extends CheckedAccountBaseService<RicercaContiConciliazionePerCausaleEP, RicercaContiConciliazionePerCausaleEPResponse> {

	@Autowired
	private CapitoloDad capitoloDad;
	@Autowired
	private SoggettoDad soggettoDad;
	@Autowired
	private ConciliazioneDad conciliazioneDad;
	@Autowired
	private CausaleEPDad causaleEPDad;
	
	private Capitolo<?,?> capitolo;

	@Override
	@Transactional
	public RicercaContiConciliazionePerCausaleEPResponse executeService(RicercaContiConciliazionePerCausaleEP serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCausaleEP(), "causale EP");
//		checkNotNull(req.getCausaleEP().getClasseDiConciliazione(), "classe di conciliazione");
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
		
		List<ClasseDiConciliazione> classi = causaleEPDad.findClasseConciliazioneByCausale(req.getCausaleEP());
		if(classi.isEmpty()){
			log.debug(methodName, "Nessuna classe di conciliazione trovata per la causale");
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return;
		}
		
		capitolo = capitoloDad.findOne(req.getCapitolo().getUid());
		ClassificatoreGerarchico classificatore = caricaClassificatore();
		
		if(classificatore == null){
			log.debug(methodName, "Il capitolo trovato non ha macroaggregato/categoria");
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return;
		}
		
		log.debug(methodName, "trovato classificatore con uid: " + classificatore.getUid());
		List<Conto> contiPerTitolo = conciliazioneDad.ricercaContiConciliazionePerTitolo(classi.get(0), classificatore);
		if(contiPerTitolo == null || contiPerTitolo.isEmpty()){
			log.debug(methodName, "Nessuna conciliazione per titolo trovata.");
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return;
		}
		
		if(contiPerTitolo.size() == 1){
			log.debug(methodName, "Trovata un'unica conciliazione per titolo. Conto con uid: " + contiPerTitolo.get(0).getUid());
			res.setConti(contiPerTitolo);
			return;
		}
		
		List<Conto> contiPerCapitolo = conciliazioneDad.ricercaContiConciliazionePerCapitolo(capitolo);
		if(contiPerCapitolo == null || contiPerCapitolo.isEmpty()){
			log.debug(methodName, "Nessuna conciliazione per capitolo trovata. Restituisco i conti delle conciliazioni per titolo.");
			res.setConti(contiPerTitolo);
			return;
		}
		if(contiPerCapitolo.size() == 1){
			log.debug(methodName, "Trovata un'unica conciliazione per capitolo. Conto con uid: " + contiPerTitolo.get(0).getUid());
			res.setConti(contiPerCapitolo);
			return;
		}
		
		if(req.getSoggetto() == null || req.getSoggetto().getUid() == 0){
			log.debug(methodName, "Nessun soggetto trovato, non posso cercare conciliazioni per beneficiario. Restituisco i conti delle conciliazioni per capitolo.");
			res.setConti(contiPerCapitolo);
			return;
		}
		
		List<Conto> contiPerBeneficiario = conciliazioneDad.ricercaContiConciliazionePerBeneficiario(req.getSoggetto(), capitolo);
		if(contiPerBeneficiario == null || contiPerBeneficiario.isEmpty()){
			log.debug(methodName, "Nessuna conciliazione per beneficiario trovata. Restituisco i conti delle conciliazioni per capitolo.");
			res.setConti(contiPerCapitolo);
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
