/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.rendiconto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.BaseCUDAccantonamentoFondiDubbiaEsigibilitaBaseService;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.BaseAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaRendicontoDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;

/**
 * Base per l'inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
public abstract class BaseCUDAccantonamentoFondiDubbiaEsigibilitaRendicontoService<REQ extends ServiceRequest, RES extends BaseAccantonamentoFondiDubbiaEsigibilitaResponse> extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaBaseService<AccantonamentoFondiDubbiaEsigibilitaRendiconto, REQ, RES> {

	//DADs
	@Autowired protected AccantonamentoFondiDubbiaEsigibilitaRendicontoDad accantonamentoFondiDubbiaEsigibilitaRendicontoDad;
	@Autowired protected CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;

	@Override
	protected void init() {
		accantonamentoFondiDubbiaEsigibilitaRendicontoDad.setLoginOperazione(loginOperazione);
		accantonamentoFondiDubbiaEsigibilitaRendicontoDad.setEnte(ente);
		capitoloEntrataGestioneDad.setEnte(ente);
	}

	@Override
	protected Map<Integer, BigDecimal> calcolaNumeratori(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		int quinquennio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getQuinquennioRiferimento().intValue();
		return capitoloEntrataGestioneDad.getImportoContoResiduiPerAnno(afde.getCapitolo().getUid(), quinquennio - 4, quinquennio);
	}
	
	@Override
	protected Map<Integer, BigDecimal> calcolaDenominatori(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		int quinquennio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getQuinquennioRiferimento().intValue();
		return capitoloEntrataGestioneDad.getImportoResiduoInizialePerAnno(afde.getCapitolo().getUid(), quinquennio - 4, quinquennio);
	}
	
	@Override
	protected void popolaDatiUlteriori(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		//SIAC-8450
		calcolaPrimoAccantonamentoEffettivoRendiconto(afde);
	}
	
	/**
	 * SIAC-8450
	 * 
	 * A seguito della SIAC-8446 il rendiconto estrae il campo dal database senza ricalcolo, si adegua quindi l'inserimento 
	 * proponendo un primo calcolo.
	 * 
	 * @param <AccantonamentoFondiDubbiaEsigibilitaRendiconto> <b>afde</b> l'accantonamento del rendiconto
	 */
	private void calcolaPrimoAccantonamentoEffettivoRendiconto(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		if(afde == null || afde.getResiduoFinaleCapitolo() == null) return;
		
		//SIAC-8576
		BigDecimal accantonamentoEffettivo = afde.getMediaSempliceTotali() != null ? 	
			calcolaAccantonamentoEffettivo(afde.getResiduoFinaleCapitolo(), afde.getMediaSempliceTotali())
			: afde.getResiduoFinaleCapitolo();
		
		//SIAC-8393
		afde.setAccantonamento(accantonamentoEffettivo);
		afde.setAccantonamentoOriginale(accantonamentoEffettivo);
	}
	
	protected BigDecimal calcolaAccantonamentoEffettivo(BigDecimal residuoCapitolo, BigDecimal media) {
		return residuoCapitolo.multiply(calcolaPercentualeAccantonamento(media)).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED);
	}

	private BigDecimal calcolaPercentualeAccantonamento(BigDecimal media) {
		return BilUtilities.BIG_DECIMAL_ONE_HUNDRED.subtract(media);
	}
	
	@Override
	protected String calcolaNote(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		List<String> note = new ArrayList<String>();
		// Nel caso in cui, per un dato anno x si abbia un valore di totale accertato (o residuo per l'elaborazione a rendiconto) < totale incassato,
		// il sistema proporra' un warning nell'area destra del cruscotto (accanto ai pulsanti matita, orologio..)
		int quinquennio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getQuinquennioRiferimento().intValue();
		for(int i = 0; i < 5; i++) {
			BigDecimal residui = afde.recuperaNumeratore(i);
			BigDecimal residuiIniziali = afde.recuperaDenominatore(i);
			if(residui != null && residuiIniziali != null && residuiIniziali.compareTo(residui) < 0) {
				note.add("Totale residuo per l'anno " + (quinquennio - i) + " minore del totale incassato in conto residui");
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
				AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.TipoMedia,
				AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.StanziamentiCapitolo,
				AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.CapitoloEntrataGestione,
				CapitoloEntrataGestioneModelDetail.TitoloTipologiaCategoriaSAC);
		return accantonamentoFondiDubbiaEsigibilitaRendicontoDad.ricercaAccantonamentoFondiDubbiaEsigibilita(attributiBilancio);
	}
	
	/**
	 * Per un dato capitolo permetto l'aggancio a uno e un solo accantonamento (non cancellato)
	 * @param afde l'accantonamento per cui recuperare i dati
	 */
	protected void checkAccantonamentoGiaEsistente(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		// Controllo che non vi siano dati pregressi
		Capitolo<?, ?> cap = capitoloEntrataGestioneDad.findOneWithMinimalData(afde.getCapitolo().getUid());
		String key = isGestioneUEB() ? cap.getAnnoNumeroArticoloUEB() : cap.getAnnoNumeroArticolo();
		checkBusinessCondition(cap instanceof CapitoloEntrataGestione, ErroreCore.VALORE_NON_CONSENTITO.getErrore("capitolo " + key, "deve essere un capitolo di gestione"));
		
		Long countAccantonamentoPerCapitolo = accantonamentoFondiDubbiaEsigibilitaRendicontoDad.countByCapitolo(afde.getCapitolo(), accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		checkBusinessCondition(countAccantonamentoPerCapitolo == null || countAccantonamentoPerCapitolo.longValue() == 0L,
				ErroreCore.ENTITA_PRESENTE.getErrore("Inserimento fondi dubbia e difficile esazione", "Accantonamento per capitolo " + key));
	}
	
	/**
	 * Inserimento dell'accantonamento su base dati
	 * @param afde l'accantonamento
	 * @return l'accantonamento inserito
	 */
	protected AccantonamentoFondiDubbiaEsigibilitaRendiconto inserisciAccantonamento(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		final String methodName = "inserisciAccantonamento";
		// Inserisco l'accantonamento
		AccantonamentoFondiDubbiaEsigibilitaRendiconto afdeInserito = accantonamentoFondiDubbiaEsigibilitaRendicontoDad.create(afde);
		log.debug(methodName, "Inserito accantonamento con uid " + afdeInserito.getUid());
		return afdeInserito;
	}
	/**
	 * Lettura degli attributi a partire dall'accantonamento
	 * @param afde l'accantonamento
	 */
	protected void loadAttributiBilancio(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		Utility.MDTL.addModelDetails(AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.AttributiBilancio);
		AccantonamentoFondiDubbiaEsigibilitaRendiconto accantonamentoDB = accantonamentoFondiDubbiaEsigibilitaRendicontoDad.findById(afde);
		Utility.MDTL.clearByModelDetailClass(AccantonamentoFondiDubbiaEsigibilitaModelDetail.class);
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoDB.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
	}
}
