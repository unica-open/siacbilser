/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaMassivaDatiSoggettoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaMassivaDatiSoggettoAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaMassivaDatiSoggettoAllegatoAttoService extends CheckedAccountBaseService<AggiornaMassivaDatiSoggettoAllegatoAtto, AggiornaMassivaDatiSoggettoAllegatoAttoResponse> {

	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	@Autowired
	private SoggettoDad soggettoDad;
	
	private List<Soggetto> listaSoggetto;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getDatiSoggettoAllegato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dati soggetto"));
		checkEntita(req.getAllegatoAtto(), "allegato atto", false);
		
		checkNotNull(req.getDatiSoggettoAllegato().getCausaleSospensione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("causale sospensione dati soggetto"), false);
		checkNotNull(req.getDatiSoggettoAllegato().getDataSospensione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data sospensione dati soggetto"));
		if(req.getDatiSoggettoAllegato().getDataRiattivazione() != null) {
			checkCondition(req.getDatiSoggettoAllegato().getDataRiattivazione().compareTo(req.getDatiSoggettoAllegato().getDataSospensione()) >= 0,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("la data di riattivazione deve essere successiva o pari alla data di sospensione"));
		}
	}
	
	@Override
	@Transactional
	public AggiornaMassivaDatiSoggettoAllegatoAttoResponse executeService(AggiornaMassivaDatiSoggettoAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		allegatoAttoDad.setEnte(ente);
		allegatoAttoDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		checkStatoAllegatoAtto();
		initSoggetti();
		
		for(Soggetto s : listaSoggetto) {
			log.debug(methodName, "Elaborazione soggetto [" + s.getUid() + "]");
			elaboraSoggetto(s);
		}
	}
	
	/**
	 * Controllo dello stato dell'atto allegato.
	 * <br/>
	 * L'allegato deve essere in stato DA COMPLETARE o COMPLETATO
	 */
	private void checkStatoAllegatoAtto() {
		StatoOperativoAllegatoAtto statoOperativoAllegatoAtto = allegatoAttoDad.getStatoOperativoAllegatoAtto(req.getAllegatoAtto());
		
		if(statoOperativoAllegatoAtto == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Stato operativo", "dell'allegato atto " + req.getAllegatoAtto().getUid()));
		}
		
		if(!StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(statoOperativoAllegatoAtto) && !StatoOperativoAllegatoAtto.COMPLETATO.equals(statoOperativoAllegatoAtto)) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'allegato atto si trova in stato " + statoOperativoAllegatoAtto.getDescrizione()));
		}
	}

	/**
	 * Inizializzazione dei soggetti
	 */
	private void initSoggetti() {
		String errMsg;
		if(req.getElencoDocumentiAllegato() != null && req.getElencoDocumentiAllegato().getUid() != 0) {
			errMsg = "collegato all'elenco " + req.getElencoDocumentiAllegato().getUid();
			listaSoggetto = soggettoDad.findSoggettiByElencoDocumentiAllegato(req.getElencoDocumentiAllegato());
		} else {
			errMsg = "collegato all'allegato " + req.getAllegatoAtto().getUid();
			listaSoggetto = soggettoDad.findSoggettiByAllegatoAtto(req.getAllegatoAtto());
		}
		if(listaSoggetto == null || listaSoggetto.isEmpty()) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("soggetto", errMsg));
		}
	}

	/**
	 * Elaborazione del singolo soggetto
	 * @param soggetto il soggetto da elaborare
	 */
	private void elaboraSoggetto(Soggetto soggetto) {
		final String methodName = "elaboraSoggetto";
		List<DatiSoggettoAllegato> listaDatiSoggettoAllegato = allegatoAttoDad.findDatiSoggettoAllegatoByAllegatoAttoAndSoggetto(req.getAllegatoAtto(), soggetto);
		
		if(listaDatiSoggettoAllegato == null || listaDatiSoggettoAllegato.isEmpty()) {
			log.debug(methodName, "Nessun dato soggetto allegato presente tra soggetto [" + soggetto.getUid() + "] e allegato [" + req.getAllegatoAtto().getUid() + "]: inserisco la relazione");
			// Non ho dati: inserisco un nuovo record
			DatiSoggettoAllegato dsa = clonaTemplate(soggetto);
			allegatoAttoDad.inserisciRelazioneSoggettoAllegato(dsa);
			res.getListaDatiSoggettoAllegato().add(dsa);
			
			log.debug(methodName, "Inserita relazione con uid " + dsa.getUid());
		} else {
			log.debug(methodName, "Esistono dato soggetto allegato presente tra soggetto [" + soggetto.getUid() + "] e allegato [" + req.getAllegatoAtto().getUid() + "]: aggiorno la relazione");
			for(DatiSoggettoAllegato datiSoggettoAllegato : listaDatiSoggettoAllegato) {
				DatiSoggettoAllegato dsa = clonaTemplate(soggetto);
				dsa.setUid(datiSoggettoAllegato.getUid());
				allegatoAttoDad.aggiornaRelazioneSoggettoAllegato(dsa);
				res.getListaDatiSoggettoAllegato().add(dsa);
				log.debug(methodName, "Aggiornata relazione con uid " + dsa.getUid());
			}
		}
		
		
	}
	
	private DatiSoggettoAllegato clonaTemplate(Soggetto soggetto) {
		DatiSoggettoAllegato dsa = new DatiSoggettoAllegato();
		
		dsa.setAllegatoAtto(req.getAllegatoAtto());
		dsa.setCausaleSospensione(req.getDatiSoggettoAllegato().getCausaleSospensione());
		dsa.setDataSospensione(req.getDatiSoggettoAllegato().getDataSospensione());
		dsa.setDataRiattivazione(req.getDatiSoggettoAllegato().getDataRiattivazione());
		dsa.setEnte(ente);
		dsa.setLoginOperazione(loginOperazione);
		dsa.setSoggetto(soggetto);
		
		return dsa;
	}

}
