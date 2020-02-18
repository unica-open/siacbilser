/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloBase;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloBaseResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class VerificaAnnullabilitaCapitoloBaseService.
 */
public abstract class VerificaAnnullabilitaCapitoloBaseService<REQ extends VerificaAnnullabilitaCapitoloBase<CAP>, RES extends VerificaAnnullabilitaCapitoloBaseResponse<CAP>, CAP extends Capitolo<?, ?>>
		extends CheckedAccountBaseService<REQ, RES> {
	
	@Autowired
	protected ImportiCapitoloDad importiCapitoloDad;
	@Autowired
	protected CapitoloDad capitoloDad;
	
	@Override
	protected final void checkServiceParam() throws ServiceParamError {
		//SIAC-6884-Annulla da inserisciVariazione
//		if(req.isDecentrato()){
//			checkCondition(false, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Operatore decentrato"));
//		}
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaPrevisione"));
		
		//parametri obbligatori per ricerca puntuale
		checkCondition(req.getBilancio().getAnno()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
		checkNotNull(req.getCapitolo().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), false);
		checkNotNull(req.getCapitolo().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
		checkNotNull(req.getCapitolo().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
		checkNotNull(req.getCapitolo().getStatoOperativoElementoDiBilancio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato"), false);
		
		
	}
	
	@Override
	protected void init() {
		capitoloDad.setEnte(ente);
	}

	@Override
	protected void execute() {
		// Inizializzo l'annullabilita' a true
		res.setAnnullabilitaCapitolo(true);
		// Capitolo - Anno di Bilancio
		CAP cap = ricercaCapitolo();
		if (cap == null) {
			setNonAnnullabilitaCapitolo(ErroreBil.CAPITOLO_INESISTENTE.getErrore());
			return;
		}
		testStatoCapitolo(cap);
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
	
	/**
	 * Imposta la non annullabilit&agrave; del capitolo
	 * @param errore l'errore da impostare
	 */
	protected void setNonAnnullabilitaCapitolo(Errore errore) {
		res.setAnnullabilitaCapitolo(false);
		res.addErrore(errore);
		res.setEsito(Esito.FALLIMENTO);
	}
	
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
	
	private void testStatoCapitolo(CAP cap) {
		StatoOperativoElementoDiBilancio statoCapitolo = capitoloDad.findStatoOperativoCapitolo(cap.getUid());
		if(StatoOperativoElementoDiBilancio.ANNULLATO.equals(statoCapitolo)) {
			setNonAnnullabilitaCapitolo(ErroreBil.CAPITOLO_NON_ANNULLABILE.getErrore("gia' annullato"));
		}
	}

	protected void checkVincoli(CAP cap) {
		checkLongValue(capitoloDad.countVincoliCapitolo(cap, EnumSet.of(StatoOperativo.VALIDO)), "Vincoli presenti.");
	}
	
	protected void checkVariazioniImporti(CAP cap) {
		Collection<StatoOperativoVariazioneDiBilancio> stati = EnumSet.allOf(StatoOperativoVariazioneDiBilancio.class);
		stati.remove(StatoOperativoVariazioneDiBilancio.BOZZA);
		checkLongValue(capitoloDad.countVariazioniImportiCapitolo(cap, stati), "Variazioni importi presenti.");
	}
	
	protected void checkVariazioniCodifiche(CAP cap) {
		Collection<StatoOperativoVariazioneDiBilancio> stati = EnumSet.allOf(StatoOperativoVariazioneDiBilancio.class);
		stati.remove(StatoOperativoVariazioneDiBilancio.BOZZA);
		checkLongValue(capitoloDad.countVariazioniCodificheCapitolo(cap, stati), "Variazioni codifiche presenti.");
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
		checkLongValue(capitoloDad.countMovimentoGestioneCapitolo(cap, Arrays.asList("P", "D", "N")), "Impegni presenti.");
	}
	protected void checkAccertamenti(CAP cap) {
		// FIXME: vedere se impostare un enum
		checkLongValue(capitoloDad.countMovimentoGestioneCapitolo(cap, Arrays.asList("P", "D", "N")), "Accertamenti presenti.");
	}
	
	protected void checkLongValue(Long value, String errMsg) {
		if(value != null && value.longValue() > 0){
			setNonAnnullabilitaCapitolo(ErroreBil.CAPITOLO_NON_ANNULLABILE_PER_MOVIMENTI_COLLEGATI.getErrore(errMsg));
		}
	}

	/**
	 * Test importo.
	 *
	 * @param importo the importo
	 */
	protected void testImporto(BigDecimal importo, String tipoImporto) {
		if (importo != null && importo.compareTo(BigDecimal.ZERO) > 0) {
			setNonAnnullabilitaCapitolo(ErroreBil.CAPITOLO_NON_ANNULLABILE_PER_MOVIMENTI_COLLEGATI.getErrore("Importo di tipo " + tipoImporto + " non nullo."));
		}
	}
	
	/**
	 * Controlla che il capitolo non sia presente o sia annullabile
	 * @param resp la response del servizio
	 * @return se la condizione sia verificata
	 */
	protected <C extends Capitolo<?, ?>> boolean isInesistenteOrAnnullato(VerificaAnnullabilitaCapitoloBaseResponse<C> resp) {
		return resp.verificatoErrore(ErroreBil.CAPITOLO_INESISTENTE.getCodice())
		|| (!resp.isFallimento() && resp.isAnnullabilitaCapitolo());
	}
	
}
