/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * The Class VerificaEliminabilitaCapitoloBaseService.
 */
public abstract class VerificaEliminabilitaCapitoloBaseService<REQ extends ServiceRequest, RES extends ServiceResponse, CAP extends Capitolo<?, ?>>
		extends CheckedAccountBaseService<REQ, RES> {
	
	@Autowired
	protected ImportiCapitoloDad importiCapitoloDad;
	@Autowired
	protected CapitoloDad capitoloDad;

	@Override
	protected void execute() {
		// Capitolo - Anno di Bilancio
		CAP cap = ricercaCapitolo();
		if (cap == null) {
			setNonEliminabilitaCapitolo(ErroreBil.CAPITOLO_INESISTENTE.getErrore());
			return;
		}
		testImporti(cap, 0);
		testImporti(cap, 1); // Capitolo Uscita Previsione - Anno di Bilancio + 1
		testImporti(cap, 2); // Capitolo Uscita Previsione - Anno di Bilancio + 2
		
		testMovimenti(cap);
	}

	/**
	 * Ricerca capitolo uscita gestione.
	 *
	 * @return the capitolo uscita gestione
	 */
	protected abstract CAP ricercaCapitolo();
	
	protected abstract void setNonEliminabilitaCapitolo(Errore errore);
	
	/**
	 * Test importi.
	 *
	 * @param cug the cug
	 * @param offsetAnno l'offset dell'anno
	 */
	protected abstract void testImporti(CAP cap, int offsetAnno);
	/**
	 * Test movimenti
	 * @param cap il capitolo
	 */
	protected abstract void testMovimenti(CAP cap);

	protected void checkVincoli(CAP cap) {
		checkLongValue(capitoloDad.countVincoliCapitolo(cap, EnumSet.allOf(StatoOperativo.class)), "Vincoli presenti.");
	}
	
	protected void checkVariazioniImporti(CAP cap) {
		checkLongValue(capitoloDad.countVariazioniImportiCapitolo(cap, EnumSet.allOf(StatoOperativoVariazioneBilancio.class)), "Variazioni importi presenti.");
	}
	
	protected void checkVariazioniCodifiche(CAP cap) {
		checkLongValue(capitoloDad.countVariazioniCodificheCapitolo(cap, EnumSet.allOf(StatoOperativoVariazioneBilancio.class)), "Variazioni codifiche presenti.");
	}
	
	protected void checkDocumentiSpesa(CAP cap) {
		// TODO: V1 non implementato
		//checkLongValue(dad.ricercaDocumentiSpesaNonAnnullatiCapitolo(cap), "Documenti spesa presenti.");
	}
	
	protected void checkDocumentiEntrata(CAP cap) {
		// TODO: V1 non implementato
		//checkLongValue(dad.ricercaDocumentiEntrataNonAnnullatiCapitolo(cap), "Documenti entrata presenti.");
	}
	
	protected void checkImpegni(CAP cap) {
		// FIXME: vedere se impostare un enum
		checkLongValue(capitoloDad.countMovimentoGestioneCapitolo(cap, Arrays.asList("P", "D", "A", "N")), "Impegni presenti.");
	}
	protected void checkAccertamenti(CAP cap) {
		// FIXME: vedere se impostare un enum
		checkLongValue(capitoloDad.countMovimentoGestioneCapitolo(cap, Arrays.asList("P", "D", "A", "N")), "Accertamenti presenti.");
	}
	
	protected void checkLongValue(Long value, String errMsg) {
		if(value != null && value.longValue() > 0){
			setNonEliminabilitaCapitolo(ErroreBil.CAPITOLO_NON_ELIMINABILE.getErrore(errMsg));
		}
	}

	/**
	 * Test importo.
	 *
	 * @param importo the importo
	 */
	protected void testImporto(BigDecimal importo, String tipoImporto) {
		if (importo != null && importo.compareTo(BigDecimal.ZERO) > 0) {
			setNonEliminabilitaCapitolo(ErroreBil.CAPITOLO_NON_ELIMINABILE.getErrore("Importo di tipo " + tipoImporto + " non nullo."));
		}
	}
	
}
