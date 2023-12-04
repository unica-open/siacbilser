/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.ComponenteImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TotaliAnnoDiEsercizio;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class CalcolaTotaliStanziamentiDiPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalcolaTotaliStanziamentiDiPrevisioneService extends CheckedAccountBaseService<CalcolaTotaliStanziamentiDiPrevisione, CalcolaTotaliStanziamentiDiPrevisioneResponse>{
	
	/** The importi capitolo dad. */
	@Autowired private ImportiCapitoloDad importiCapitoloDad;
	/** The componente importi capitolo dad. */
	@Autowired private ComponenteImportiCapitoloDad componenteImportiCapitoloDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio", false);
		checkCondition(req.getAnnoEsercizio()>0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
	}
	
	@Override
	protected void init() {
		importiCapitoloDad.setEnte(req.getRichiedente().getAccount().getEnte());
	}
	
	@Override
	@Transactional(readOnly = true)
	public CalcolaTotaliStanziamentiDiPrevisioneResponse executeService(CalcolaTotaliStanziamentiDiPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		calcolaTotaliAnnoPassato(req.getBilancio().getUid(), req.getAnnoEsercizio());
		calcolaTotaliAnnoAttuale(req.getBilancio().getUid(), req.getAnnoEsercizio());
		calcolaTotaliAnnoSuccessivo(req.getBilancio().getUid(), req.getAnnoEsercizio());
		
		res.setRichiedente(req.getRichiedente());
		res.setEsito(Esito.SUCCESSO);
	}
	
	/**
	 * Anno Esercizio : totale stanziamenti di competenza Entrata totale
	 * stanziamenti di competenza Uscita totale stanziamenti residui Entrata
	 * totale stanziamenti residui Uscita totale stanziamenti di cassa
	 * Entrata totale stanziamenti di cassa Uscita.
	 *
	 * @param annoRif the anno rif
	 */
	private void calcolaTotaliAnnoPassato(Integer bilId, int annoRif) {
		TotaliAnnoDiEsercizio tot = new TotaliAnnoDiEsercizio();
		tot.setTotStanziamentiCompetenzaEntrata(importiCapitoloDad.totaleStanziamentoCapitoliEntrataPrevisione(bilId, annoRif));
		tot.setTotStanziamentiDiCompetenzaSpesa(importiCapitoloDad.totaleStanziamentoCapitoliUscitaPrevisione(bilId, annoRif));
		tot.setTotStanziamentiResiduiEntrata(importiCapitoloDad.totaleStanziamentoResiduoCapitoliEntrataPrevisione(bilId, annoRif));
		tot.setTotaleStanziamentiResiduiSpesa(importiCapitoloDad.totaleStanziamentoResiduoCapitoliUscitaPrevisione(bilId, annoRif));
		tot.setTotStanziamentiDiCassaEntrata(importiCapitoloDad.totaleStanziamentoCassaCapitoliEntrataPrevisione(bilId, annoRif));
		tot.setTotaleStanziamentiDiCassaSpesa(importiCapitoloDad.totaleStanziamentoCassaCapitoliUscitaPrevisione(bilId, annoRif));
		
		// SIAC-6881: calcolo i dati delle componenti se richiesto
		addDatiComponenti(bilId, annoRif, tot);
		
		res.setTotaliAnnoEsercizioPassato(tot);
	}

	/**
	 * Anno Esercizio + 1 : totale stanziamenti di competenza Entrata totale
	 * stanziamenti di competenza Uscita.
	 *
	 * @param annoRif the anno rif
	 */
	private void calcolaTotaliAnnoAttuale(Integer bilId, int annoRif) {
		TotaliAnnoDiEsercizio tot = new TotaliAnnoDiEsercizio();
		tot.setTotStanziamentiCompetenzaEntrata(importiCapitoloDad.totaleStanziamentoCapitoliEntrataPrevisione(bilId, annoRif + 1));
		tot.setTotStanziamentiDiCompetenzaSpesa(importiCapitoloDad.totaleStanziamentoCapitoliUscitaPrevisione(bilId, annoRif + 1));
		addDatiComponenti(bilId, annoRif + 1, tot);
		res.setTotaliAnnoEsercizioAttuale(tot);
	}
	
	/**
	 * Anno Esercizio + 2 : totale stanziamenti di competenza Entrata totale
	 * stanziamenti di competenza Uscita.
	 *
	 * @param annoRif the anno rif
	 */
	private void calcolaTotaliAnnoSuccessivo(Integer bilId, int annoRif) {
		TotaliAnnoDiEsercizio tot = new TotaliAnnoDiEsercizio();
		tot.setTotStanziamentiCompetenzaEntrata(importiCapitoloDad.totaleStanziamentoCapitoliEntrataPrevisione(bilId, annoRif + 2));
		tot.setTotStanziamentiDiCompetenzaSpesa(importiCapitoloDad.totaleStanziamentoCapitoliUscitaPrevisione(bilId, annoRif + 2));
		addDatiComponenti(bilId, annoRif + 2, tot);
		res.setTotaliAnnoEsercizioSuccessivo(tot);
	}
	
	private void addDatiComponenti(Integer bilId, int annoRif, TotaliAnnoDiEsercizio tot) {
		if(req.isCalcolaComponenti()) {
			List<ComponenteImportiCapitolo> listaComponenteImportiCapitolo = componenteImportiCapitoloDad.totaleComponenti(bilId, annoRif);
			tot.setListaComponenteImportiCapitoloSpesa(listaComponenteImportiCapitolo);
		}
	}
}
