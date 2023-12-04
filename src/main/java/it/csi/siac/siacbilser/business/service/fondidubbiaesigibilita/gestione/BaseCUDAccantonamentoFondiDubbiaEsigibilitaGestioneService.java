/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.gestione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.BaseCUDAccantonamentoFondiDubbiaEsigibilitaBaseService;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.BaseAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaGestioneDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;

/**
 * Base per l'inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
public abstract class BaseCUDAccantonamentoFondiDubbiaEsigibilitaGestioneService<REQ extends ServiceRequest, RES extends BaseAccantonamentoFondiDubbiaEsigibilitaResponse> extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaBaseService<AccantonamentoFondiDubbiaEsigibilitaGestione, REQ, RES> {

	//DADs
	@Autowired protected AccantonamentoFondiDubbiaEsigibilitaGestioneDad accantonamentoFondiDubbiaEsigibilitaGestioneDad;
	@Autowired protected CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	
	@Override
	protected void init() {
		accantonamentoFondiDubbiaEsigibilitaGestioneDad.setLoginOperazione(loginOperazione);
		accantonamentoFondiDubbiaEsigibilitaGestioneDad.setEnte(ente);
	}

	@Override
	protected Map<Integer, BigDecimal> calcolaNumeratori(AccantonamentoFondiDubbiaEsigibilitaGestione afde) {
		// Per gestione interessa solo l'anno di riferimento
		int quinquennio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getQuinquennioRiferimento().intValue();
		return capitoloEntrataGestioneDad.getImportoContoCompetenzaPerAnno(afde.getCapitolo().getUid(), quinquennio, quinquennio);
	}
	
	@Override
	protected Map<Integer, BigDecimal> calcolaDenominatori(AccantonamentoFondiDubbiaEsigibilitaGestione afde) {
		// Per gestione interessa solo l'anno di riferimento
		int quinquennio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getQuinquennioRiferimento().intValue();
		return capitoloEntrataGestioneDad.getImportoAccertatoPerAnno(afde.getCapitolo().getUid(), quinquennio, quinquennio);
	}
	
	@Override
	protected void popolaDatiUlteriori(AccantonamentoFondiDubbiaEsigibilitaGestione afde) {
		// Popolamento Media di confronto
//		popolaMediaConfronto(afde);
		cercaMediaConfronto(afde);
		
		// SIAC-8394
		afde.setAccantonamento(afde.getStanziamentoCapitolo());
//		afde.setAccantonamento1(afde.getStanziamentoCapitolo1());
//		afde.setAccantonamento2(afde.getStanziamentoCapitolo2());
	}

	protected void popolaMediaConfronto(AccantonamentoFondiDubbiaEsigibilitaGestione afde) {
		final String methodName = "popolaMediaConfronto";
		if(afde.getUid() != 0) {
			log.debug(methodName, "Uid popolato: non sovrascrivo la media di confronto");
			// Se ho l'uid, non sovrascrivo la media
			return;
		}
		Utility.MDTL.clearByModelDetailClass(AccantonamentoFondiDubbiaEsigibilitaModelDetail.class);
		Utility.MDTL.addModelDetails(AccantonamentoFondiDubbiaEsigibilitaModelDetail.TipoMedia);
		
		TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita tipoMedia = null;

//		Salvataggio e definitivita':
//			
//			E' necessario garantire i salvataggi delle elaborazioni e, in particolare, la possibilità di flaggare l'elaborazione come definitiva.
//			Un'elaborazione definitiva e' utile ai confronti delle medie previsti nei calcoli in gestione/assestamento
//				>	La prima elaborazione in gestione confronterà la % di incasso con quella calcolata in previsione
//				>	La seconda elaborazione in gestione (o successive) confronteranno la % di incasso con la prima elaborazione in gestione

		AccantonamentoFondiDubbiaEsigibilita accantonamentoEquivalente = accantonamentoFondiDubbiaEsigibilitaGestioneDad.findEquivalenteByCapitolo(afde.getCapitolo(), TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE);
		
		if(accantonamentoEquivalente == null) {
			log.debug(methodName, "Accantonamento equivalente non presente");
			// vado avanti e permetto la valorizzazione poiche', se non ho risultati, mi apsetto di essere in PREVISIONE
			tipoMedia = TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE;
			accantonamentoEquivalente = new AccantonamentoFondiDubbiaEsigibilita();
		} else {
			tipoMedia = TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita.GESTIONE;
		}
		
		BigDecimal media = accantonamentoEquivalente.recuperaMedia();
		if(media == null) {
			log.debug(methodName, "Media equivalente non trovata: utilizzo del default 100");
			media = BilUtilities.BIG_DECIMAL_ONE_HUNDRED;
		}
		afde.setMediaConfronto(media);
		afde.setTipoMediaConfronto(tipoMedia);
	}
	
	protected void cercaMediaConfronto(AccantonamentoFondiDubbiaEsigibilitaGestione afde) {
		Utility.MDTL.addModelDetails(AccantonamentoFondiDubbiaEsigibilitaModelDetail.TipoMedia);
		
		List<Object[]> list = accantonamentoFondiDubbiaEsigibilitaGestioneDad.trovaMediaConfronto(afde.getCapitolo());

		// Setto i valori solo se ho la percentuale della media di confronto
		if(CollectionUtils.isNotEmpty(list) && list.get(0) != null && list.get(0)[0] != null) {
			afde.setMediaConfronto(new BigDecimal(list.get(0)[0].toString()));
			afde.setTipoMediaConfronto(TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita.byCodice(list.get(1)[0].toString()));			
		}
	}
	
	@Override
	protected String calcolaNote(AccantonamentoFondiDubbiaEsigibilitaGestione afde) {
		List<String> note = new ArrayList<String>();
		// Nel caso in cui, per un dato anno x si abbia un valore di totale accertato (o residuo per l'elaborazione a rendiconto) < totale incassato,
		// il sistema proporra' un warning nell'area destra del cruscotto (accanto ai pulsanti matita, orologio..)
		int quinquennio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getQuinquennioRiferimento().intValue();
		BigDecimal incassi = afde.recuperaNumeratore(0);
		BigDecimal accertamenti = afde.recuperaDenominatore(0);
		if(incassi != null && accertamenti != null && accertamenti.compareTo(incassi) < 0) {
			note.add("Totale accertato per l'anno " + (quinquennio - 0) + " minore del totale incassato in conto competenza");
		}
		
		//SIAC-8382 non e' piu' la MIN ma la MAX
		// Se media utente < min(medie calcolate dal sistema). Sara' proposto un warning e non un errore bloccante.
		BigDecimal massimoMedie = afde.getMediaConfronto();
		if(afde.getMediaUtente() != null && massimoMedie != null && afde.getMediaUtente().compareTo(massimoMedie) > 0) {
			note.add("Media utente maggiore alla media di confronto");
		}
		
		return StringUtils.join(note, ", ");
	}

	/**
	 * Caricamento degli accantonamenti
	 * @return gli accantonamenti
	 */
	protected List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> caricaAccantonamenti(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancio) {
		Utility.MDTL.clear();
		Utility.MDTL.addModelDetails(
				AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.TipoMedia,
				AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.TipoMediaConfronto,
				AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.StanziamentiCapitolo,
				AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.CapitoloEntrataGestione,
				CapitoloEntrataGestioneModelDetail.TitoloTipologiaCategoriaSAC);
		return accantonamentoFondiDubbiaEsigibilitaGestioneDad.ricercaAccantonamentoFondiDubbiaEsigibilita(attributiBilancio);
	}
	
	/**
	 * Per un dato capitolo permetto l'aggancio a uno e un solo accantonamento (non cancellato)
	 * @param afde l'accantonamento per cui recuperare i dati
	 */
	protected void checkAccantonamentoGiaEsistente(AccantonamentoFondiDubbiaEsigibilitaGestione afde) {
		// Controllo che non vi siano dati pregressi
		Capitolo<?, ?> cap = capitoloEntrataGestioneDad.findOneWithMinimalData(afde.getCapitolo().getUid());
		String key = isGestioneUEB() ? cap.getAnnoNumeroArticoloUEB() : cap.getAnnoNumeroArticolo();
		checkBusinessCondition(cap instanceof CapitoloEntrataGestione, ErroreCore.VALORE_NON_CONSENTITO.getErrore("capitolo " + key, "deve essere un capitolo di gestione"));
		
		Long countAccantonamentoPerCapitolo = accantonamentoFondiDubbiaEsigibilitaGestioneDad.countByCapitolo(afde.getCapitolo(), accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		checkBusinessCondition(countAccantonamentoPerCapitolo == null || countAccantonamentoPerCapitolo.longValue() == 0L,
				ErroreCore.ENTITA_PRESENTE.getErrore("Inserimento fondi dubbia e difficile esazione", "Accantonamento per capitolo " + key));
	}
	
	/**
	 * Inserimento dell'accantonamento su base dati
	 * @param afde l'accantonamento
	 * @return l'accantonamento inserito
	 */
	protected AccantonamentoFondiDubbiaEsigibilitaGestione inserisciAccantonamento(AccantonamentoFondiDubbiaEsigibilitaGestione afde) {
		final String methodName = "inserisciAccantonamento";
		// Inserisco l'accantonamento
		AccantonamentoFondiDubbiaEsigibilitaGestione afdeInserito = accantonamentoFondiDubbiaEsigibilitaGestioneDad.create(afde);
		log.debug(methodName, "Inserito accantonamento con uid " + afdeInserito.getUid());
		return afdeInserito;
	}
	/**
	 * Lettura degli attributi a partire dall'accantonamento
	 * @param afde l'accantonamento
	 */
	protected void loadAttributiBilancio(AccantonamentoFondiDubbiaEsigibilitaGestione afde) {
		Utility.MDTL.addModelDetails(AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.AttributiBilancio);
		AccantonamentoFondiDubbiaEsigibilitaGestione accantonamentoDB = accantonamentoFondiDubbiaEsigibilitaGestioneDad.findById(afde);
		Utility.MDTL.clearByModelDetailClass(AccantonamentoFondiDubbiaEsigibilitaModelDetail.class);
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoDB.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
	}
}
