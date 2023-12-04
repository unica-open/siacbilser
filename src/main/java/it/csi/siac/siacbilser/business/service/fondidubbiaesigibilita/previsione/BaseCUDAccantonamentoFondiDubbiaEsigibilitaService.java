/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.previsione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.BaseCUDAccantonamentoFondiDubbiaEsigibilitaBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.BaseAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataPrevisioneModelDetail;

/**
 * Base per l'inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
public abstract class BaseCUDAccantonamentoFondiDubbiaEsigibilitaService<REQ extends ServiceRequest, RES extends BaseAccantonamentoFondiDubbiaEsigibilitaResponse> extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaBaseService<AccantonamentoFondiDubbiaEsigibilita, REQ, RES> {
	
	//DADs
	@Autowired protected AccantonamentoFondiDubbiaEsigibilitaDad accantonamentoFondiDubbiaEsigibilitaDad;
	@Autowired protected CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;

	@Override
	protected void init() {
		accantonamentoFondiDubbiaEsigibilitaDad.setLoginOperazione(loginOperazione);
		accantonamentoFondiDubbiaEsigibilitaDad.setEnte(ente);
	}

	@Override
	protected Map<Integer, BigDecimal> calcolaNumeratori(AccantonamentoFondiDubbiaEsigibilita afde) {
		int quinquennio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getQuinquennioRiferimento().intValue();
		return capitoloEntrataPrevisioneDad.getImportoIncassatoPerAnno(afde.getCapitolo().getUid(), quinquennio - 4, quinquennio);
	}

	@Override
	protected Map<Integer, BigDecimal> calcolaDenominatori(AccantonamentoFondiDubbiaEsigibilita afde) {
		int quinquennio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getQuinquennioRiferimento().intValue();
		return capitoloEntrataPrevisioneDad.getImportoAccertatoPerAnno(afde.getCapitolo().getUid(), quinquennio - 4, quinquennio);
	}
	
	@Override
	protected void popolaDatiUlteriori(AccantonamentoFondiDubbiaEsigibilita afde) {
		afde.setAccantonamento(afde.getStanziamentoCapitolo());
		afde.setAccantonamento1(afde.getStanziamentoCapitolo1());
		afde.setAccantonamento2(afde.getStanziamentoCapitolo2());
	}
	
	@Override
	protected String calcolaNote(AccantonamentoFondiDubbiaEsigibilita afde) {
		List<String> note = new ArrayList<String>();
		// Nel caso in cui, per un dato anno x si abbia un valore di totale accertato (o residuo per l'elaborazione a rendiconto) < totale incassato,
		// il sistema proporra' un warning nell'area destra del cruscotto (accanto ai pulsanti matita, orologio..)
		int quinquennio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getQuinquennioRiferimento().intValue();
		for(int i = 0; i < 5; i++) {
			BigDecimal incassi = afde.recuperaNumeratore(i);
			BigDecimal accertamenti = afde.recuperaDenominatore(i);
			if(incassi != null && accertamenti != null && accertamenti.compareTo(incassi) < 0) {
				note.add("Totale accertato per l'anno " + (quinquennio - i) + " minore del totale incassato");
			}
		}

		//SIAC-8382 non e' piu' la MIN ma la MAX
		// Se media utente < min(medie calcolate dal sistema). Sara' proposto un warning e non un errore bloccante.
		BigDecimal massimoMedie = Utility.max(afde.getMediaSempliceTotali(), afde.getMediaSempliceRapporti(), afde.getMediaPonderataTotali(), afde.getMediaPonderataRapporti());
		if(afde.getMediaUtente() != null && massimoMedie != null && afde.getMediaUtente().compareTo(massimoMedie) > 0) {
			note.add("Media utente maggiore alle medie calcolate");
		}
		
		return StringUtils.join(note, ", ");
	}

	/**
	 * Caricamento degli accantonamenti
	 * @return gli accantonamenti
	 */
	protected List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> caricaAccantonamenti(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancio) {
		Utility.MDTL.addModelDetails(
				AccantonamentoFondiDubbiaEsigibilitaModelDetail.TipoMedia,
				AccantonamentoFondiDubbiaEsigibilitaModelDetail.StanziamentiCapitolo,
				AccantonamentoFondiDubbiaEsigibilitaModelDetail.CapitoloEntrataPrevisione,
				CapitoloEntrataPrevisioneModelDetail.TitoloTipologiaCategoriaSAC);
		return accantonamentoFondiDubbiaEsigibilitaDad.ricercaAccantonamentoFondiDubbiaEsigibilita(attributiBilancio);
	}

	/**
	 * Per un dato capitolo permetto l'aggancio a uno e un solo accantonamento (non cancellato)
	 * @param afde l'accantonamento per cui recuperare i dati
	 */
	protected void checkAccantonamentoGiaEsistente(AccantonamentoFondiDubbiaEsigibilita afde) {
		// Controllo che non vi siano dati pregressi
		Capitolo<?, ?> cap = capitoloEntrataPrevisioneDad.findOneWithMinimalData(afde.getCapitolo().getUid());
		String key = isGestioneUEB() ? cap.getAnnoNumeroArticoloUEB() : cap.getAnnoNumeroArticolo();
		checkBusinessCondition(cap instanceof CapitoloEntrataPrevisione, ErroreCore.VALORE_NON_CONSENTITO.getErrore("capitolo " + key, "deve essere un capitolo di previsione"));
		
		Long countAccantonamentoPerCapitolo = accantonamentoFondiDubbiaEsigibilitaDad.countByCapitolo(afde.getCapitolo(), accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		checkBusinessCondition(countAccantonamentoPerCapitolo == null || countAccantonamentoPerCapitolo.longValue() == 0L,
				ErroreCore.ENTITA_PRESENTE.getErrore("Inserimento fondi dubbia e difficile esazione", "Accantonamento per capitolo " + key));
	}
	
	/**
	 * Inserimento dell'accantonamento su base dati
	 * @param afde l'accantonamento
	 * @return l'accantonamento inserito
	 */
	protected AccantonamentoFondiDubbiaEsigibilita inserisciAccantonamento(AccantonamentoFondiDubbiaEsigibilita afde) {
		final String methodName = "inserisciAccantonamento";
		// Inserisco l'accantonamento
		AccantonamentoFondiDubbiaEsigibilita afdeInserito = accantonamentoFondiDubbiaEsigibilitaDad.create(afde);
		log.debug(methodName, "Inserito accantonamento con uid " + afdeInserito.getUid());
		return afdeInserito;
	}
	
	/**
	 * Lettura degli attributi a partire dall'accantonamento
	 * @param afde l'accantonamento
	 */
	protected void loadAttributiBilancio(AccantonamentoFondiDubbiaEsigibilita afde) {
		Utility.MDTL.addModelDetails(AccantonamentoFondiDubbiaEsigibilitaModelDetail.AttributiBilancio);
		AccantonamentoFondiDubbiaEsigibilita accantonamentoDB = accantonamentoFondiDubbiaEsigibilitaDad.findById(afde);
		Utility.MDTL.clearByModelDetailClass(AccantonamentoFondiDubbiaEsigibilitaModelDetail.class);
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoDB.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
	}
}
