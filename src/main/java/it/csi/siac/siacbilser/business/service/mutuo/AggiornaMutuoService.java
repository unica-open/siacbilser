/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siacbilser.model.mutuo.TipoTassoMutuo;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaMutuoService extends BaseInserisciAggiornaMutuoService<AggiornaMutuo, AggiornaMutuoResponse> {
	
	@Override
	@Transactional
	public AggiornaMutuoResponse executeService(AggiornaMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		super.execute();
		
		checkStato();
		
		checkNonEditableField();
		
		checkMutuoValido();
		
		mutuoDad.update(mutuo);
		
		res.setMutuo(mutuo);
	}

	private void checkStato() {
		
		checkBusinessCondition(!StatoMutuo.Annullato.equals(mutuoCorrente.getStatoMutuo()), ErroreCore.VALORE_NON_VALIDO.getErrore("mutuo", mutuoCorrente.getNumero()));
	}

	private void checkNonEditableField() {
		
		checkBusinessCondition(mutuoCorrente.getNumero().equals(mutuo.getNumero()), ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("numero","non modificabile"));
	}

	private void checkMutuoValido() {
		
		checkBusinessCondition(
				StatoMutuo.Bozza.equals(mutuoCorrente.getStatoMutuo())
				|| mutuoCorrente.getTipoTasso().getCodice().equals(mutuo.getTipoTasso().getCodice()), ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("tipoTasso","non modificabile"));
		checkBusinessCondition(
				StatoMutuo.Bozza.equals(mutuoCorrente.getStatoMutuo())
				|| mutuoCorrente.getDurataAnni().equals(mutuo.getDurataAnni()), ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("durataAnni","non modificabile"));
		checkBusinessCondition(
				StatoMutuo.Bozza.equals(mutuoCorrente.getStatoMutuo())
				|| mutuoCorrente.getAnnoInizio().equals(mutuo.getAnnoInizio()), ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("annoInizio","non modificabile"));
		checkBusinessCondition(
				StatoMutuo.Bozza.equals(mutuoCorrente.getStatoMutuo())
				|| mutuoCorrente.getAnnoFine().equals(mutuo.getAnnoFine()), ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("annoFine","non modificabile"));
		checkBusinessCondition(
				StatoMutuo.Bozza.equals(mutuoCorrente.getStatoMutuo())
				|| mutuoCorrente.getPeriodoRimborso().getCodice().equals(mutuo.getPeriodoRimborso().getCodice()), ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("periodoRimborso","non modificabile"));
		checkBusinessCondition(
				StatoMutuo.Bozza.equals(mutuoCorrente.getStatoMutuo())
				|| mutuoCorrente.getScadenzaPrimaRata().compareTo(mutuo.getScadenzaPrimaRata())==0, ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("scadenzaPrimaRata","non modificabile"));
		checkBusinessCondition(
				StatoMutuo.Bozza.equals(mutuoCorrente.getStatoMutuo())
				|| mutuoCorrente.getSommaMutuataIniziale().compareTo(mutuo.getSommaMutuataIniziale())==0, ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("importo","non modificabile"));
		checkBusinessCondition(
				StatoMutuo.Bozza.equals(mutuoCorrente.getStatoMutuo())
				|| mutuoCorrente.getTassoInteresse().compareTo(mutuo.getTassoInteresse())==0, ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("tassoInteresse","non modificabile"));
		checkBusinessCondition(
				StatoMutuo.Bozza.equals(mutuoCorrente.getStatoMutuo())
				|| (!TipoTassoMutuo.Variabile.equals(mutuo.getTipoTasso())  || mutuoCorrente.getTassoInteresseEuribor().compareTo(mutuo.getTassoInteresseEuribor())==0), ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("tassoInteresseEuribor","non modificabile"));
		checkBusinessCondition(
				StatoMutuo.Bozza.equals(mutuoCorrente.getStatoMutuo())
				|| (!TipoTassoMutuo.Variabile.equals(mutuo.getTipoTasso())  || mutuoCorrente.getTassoInteresseSpread().compareTo(mutuo.getTassoInteresseSpread())==0), ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("tassoInteresseSpread","non modificabile"));
		checkBusinessCondition(
				StatoMutuo.Bozza.equals(mutuoCorrente.getStatoMutuo())
				|| mutuoCorrente.getAnnualita().compareTo(mutuo.getAnnualita())==0, ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("annualita","non modificabile"));
	}
}
