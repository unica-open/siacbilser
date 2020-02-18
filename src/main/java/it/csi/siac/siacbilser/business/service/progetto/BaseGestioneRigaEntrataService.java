/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class BaseGestioneRigaEntrataService.
 * @param <REQ> la tipizzazione della request
 * @param <RES> la tipizzazione della response
 */
public abstract class BaseGestioneRigaEntrataService<REQ extends ServiceRequest, RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {
	
	/** The dett. */
	protected DettaglioEntrataCronoprogramma dett;
	
	/** The cronoprogramma dad. */
	@Autowired
	protected CronoprogrammaDad cronoprogrammaDad;
	/** The capitolo entrata previsione dad. */
	@Autowired
	protected CapitoloEntrataPrevisioneDad capitoloEntrataPrevizioneDad;
	
	protected void checkParamsDettaglio() throws ServiceParamError {
		checkNotNull(dett, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettaglio entrata programma"));
		
		checkNotNull(dett.getCronoprogramma(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cronoprogramma"));
		
		checkNotNull(dett.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(dett.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(dett.getDescrizioneCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione capitolo"));
		checkNotNull(dett.getAnnoCompetenza(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno competenza"));
		checkNotNull(dett.getStanziamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stanziamento"));
		checkNotNull(dett.getIsAvanzoAmministrazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("avanzo di amministrazione"));
	}
	
	@Override
	protected void init() {
		cronoprogrammaDad.setEnte(dett.getEnte());
		cronoprogrammaDad.setLoginOperazione(loginOperazione);
	}

	/**
	 * Controllo sulla classificazione (Titolo, Tipologia) nel caso in cui il dettaglio non si riferisca ad avanzi di amministrazione
	 */
	protected void checkClassificazione() {
		final String methodName = "checkClassificazione";
		if(Boolean.TRUE.equals(dett.getIsAvanzoAmministrazione())) {
			log.debug(methodName, "Avanzo amministrazione, non necessito del controllo su titolo e tipologia");
			return;
		}
		if(isCapitoloEsistenteEAvanzoAmministrazione()) {
			log.debug(methodName, "Capitolo esistente di tipo 'Avanzo di Amministrazione', non necessito del controllo su titolo e tipologia");
			return;
		}
		
		log.debug(methodName, "Necessito del controllo su titolo e tipologia");
		checkClassificatoreImpostato(dett.getTitoloEntrata(), "Titolo entrata");
		checkClassificatoreImpostato(dett.getTipologiaTitolo(), "Tipologia titolo");
	}

	/**
	 * Controlla se il capitolo sia esistente e sia un avanzo di amministrazione
	 * @return <code>true</code> se il capitolo esiste ed &eacute; un avanzo di amministrazione; <code>false</code> altrimenti
	 */
	private boolean isCapitoloEsistenteEAvanzoAmministrazione() {
		if(dett.getCapitolo() == null || dett.getCapitolo().getUid() == 0) {
			return false;
		}
		CategoriaCapitolo categoriaCapitolo = capitoloEntrataPrevizioneDad.findCategoriaCapitolo(dett.getCapitolo().getUid());
		
		return categoriaCapitolo != null && "AAM".equalsIgnoreCase(categoriaCapitolo.getCodice());
	}

	/**
	 * Controlla che il classificatore sia correttamente impostato
	 * @param codifica la codifica da controllare
	 * @param nomeCodifica il nome della codifica da impostare nei parametri di errore
	 */
	private void checkClassificatoreImpostato(Codifica codifica, String nomeCodifica) {
		if(codifica == null || codifica.getUid() == 0) {
			throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(nomeCodifica));
		}
	}
}
