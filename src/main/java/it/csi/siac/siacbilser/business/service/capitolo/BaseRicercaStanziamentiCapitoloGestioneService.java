/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.business.service.capitolo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStanziamentiCapitoloGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStanziamentiCapitoloGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.PrevisioneImpegnatoAccertatoDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.PrevisioneImpegnatoAccertato;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

public abstract class BaseRicercaStanziamentiCapitoloGestioneService<REQ extends RicercaStanziamentiCapitoloGestione, RES extends RicercaStanziamentiCapitoloGestioneResponse> 
	extends CheckedAccountBaseService<REQ, RES> {

	@Autowired protected CapitoloDad capitoloDad;
	@Autowired  protected ImportiCapitoloDad importiCapitoloDad;
	@Autowired
	protected PrevisioneImpegnatoAccertatoDad previsioneImpegnatoAccertatoDad;
	
	protected Capitolo<?,?> capitolo;
	private final static int ANNUALITA_TOT = 3;
	
	protected void caricaCapitolo() {
		capitolo = trovaCapitolo();
	}
	
	private Capitolo<?,?> trovaCapitolo() {
		//cerco il dettaglio del capitolo
		Capitolo<?,?> capitolo = capitoloDad.findOne(req.getUidCapitolo());
		
		if(capitolo == null) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo Uscita Gestione", "il capitolo di uscita gestione ricercato"));
			return null;
		}
		
		passaStrutturaAmministrativaContabile(capitolo);
		
		return capitolo;
	}
	
	private void passaStrutturaAmministrativaContabile(Capitolo<?,?> capitolo) {
		if(capitolo != null && NumberUtil.isValidAndGreaterThanZero(capitolo.getUid())) {
			StrutturaAmministrativoContabile sac = 
					capitoloDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitolo.getUid());
			if(sac != null) {
				capitolo.setStrutturaAmministrativoContabile(sac);
			}
		}
	}

	protected <IC extends ImportiCapitolo> List<IC> trovaImportiCapitolo(Capitolo<?,?> capitolo, Class<IC> clazz) {
		//esco se non ho trovato un capitolo
		if(capitolo == null) {
			return null;
		}
		
		//cerco gli importi scalando sui generici
		List<IC> listaImportiCapitolo = generaListaImporti(capitolo, clazz);
	
		if(CollectionUtils.isEmpty(listaImportiCapitolo)) {
			res.addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Nessun importo legato al capitolo trovato"));
		}
		
		return listaImportiCapitolo;
	}

	
	protected PrevisioneImpegnatoAccertato caricaPrevisioneImpegnatoAccertato() {
		return previsioneImpegnatoAccertatoDad.caricaPrevisioneImpegnatoAccertatoByIdCapitolo(req.getUidCapitolo());
	}

	/**
	 * Metodo di utilita' per la generazione della lista di importi
	 * 
	 * @param <IC>
	 * @param capitolo
	 * @param clazz
	 * 
	 * @return una lista di importi associati al capitolo in base alle annualita' da considerare
	 */
	private <IC extends ImportiCapitolo> List<IC> generaListaImporti(Capitolo<?, ?> capitolo, Class<IC> clazz) {
		List<IC> listaImportiCapitolo = new ArrayList<IC>(); 
		//scalo sulle annualita' da prendere in considerazione
		for (int i = 0; i < ANNUALITA_TOT; i++) {
			//lavoro con i generici per ottenere una lista popolata correttamente in ordine di annualita'
			IC importo = importiCapitoloDad.findImportiCapitolo(capitolo, req.getAnnoBilancio(), clazz, null);
			if(importo != null && !listaImportiCapitolo.contains(importo)) {
				listaImportiCapitolo.add(i, importo);
			}
		}
		return listaImportiCapitolo;
	}

}
