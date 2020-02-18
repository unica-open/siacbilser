/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDatiSoggettoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDatiSoggettoAllegatoAttoResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaDatiSoggettoAllegatoAttoService extends CheckedAccountBaseService<AggiornaDatiSoggettoAllegatoAtto, AggiornaDatiSoggettoAllegatoAttoResponse> {

	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getDatiSoggettoAllegato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dati soggetto"));
		
		checkEntita(req.getDatiSoggettoAllegato().getAllegatoAtto(), "allegato dati soggetto", false);
		checkEntita(req.getDatiSoggettoAllegato().getSoggetto(), "soggetto dati soggetto", false);
		checkEntita(req.getDatiSoggettoAllegato().getEnte(), "ente dati soggetto", false);
		
		checkNotNull(req.getDatiSoggettoAllegato().getCausaleSospensione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("causale sospensione dati soggetto"), false);
		checkNotNull(req.getDatiSoggettoAllegato().getDataSospensione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data sospensione dati soggetto"));
		if(req.getDatiSoggettoAllegato().getDataRiattivazione() != null) {
			checkCondition(req.getDatiSoggettoAllegato().getDataRiattivazione().compareTo(req.getDatiSoggettoAllegato().getDataSospensione()) >= 0,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("la data di riattivazione deve essere successiva o pari alla data di sospensione"));
		}
		
	}
	
	@Override
	@Transactional
	public AggiornaDatiSoggettoAllegatoAttoResponse executeService(AggiornaDatiSoggettoAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		allegatoAttoDad.setEnte(req.getDatiSoggettoAllegato().getEnte());
		allegatoAttoDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		if(req.getDatiSoggettoAllegato().getUid() == 0) {
			// Inserimento
			inserisciRelazione();
		} else {
			// Aggiornamento
			aggiornaRelazione();
		}
		res.setDatiSoggettoAllegato(req.getDatiSoggettoAllegato());
	}

	private void inserisciRelazione() {
		final String methodName = "inserisciRelazione";
		// Controllo che non vi sia gia' una relazione per i dati soggetto e allegato. In caso contrario, fornisco un errore
		Boolean giaAssociato = allegatoAttoDad.esisteRelazioneSoggettoAllegato(req.getDatiSoggettoAllegato().getAllegatoAtto(),
				req.getDatiSoggettoAllegato().getSoggetto());
		if(Boolean.TRUE.equals(giaAssociato)) {
			log.debug(methodName, "Relazione gia' presente su database: impossibile crearne una nuova per allegato "
					+ req.getDatiSoggettoAllegato().getAllegatoAtto().getUid() + " e soggetto " + req.getDatiSoggettoAllegato().getSoggetto().getUid());
			throw new BusinessException(ErroreCore.RELAZIONE_GIA_PRESENTE.getErrore());
		}
		allegatoAttoDad.inserisciRelazioneSoggettoAllegato(req.getDatiSoggettoAllegato());
	}

	private void aggiornaRelazione() {
		final String methodName = "aggiornaRelazione";
		Boolean relazionePresente = allegatoAttoDad.esisteRelazioneSoggettoAllegato(req.getDatiSoggettoAllegato().getUid());
		if(!Boolean.TRUE.equals(relazionePresente)) {
			log.debug(methodName, "Relazione non presente su database tra allegato "
					+ req.getDatiSoggettoAllegato().getAllegatoAtto().getUid() + " e soggetto " + req.getDatiSoggettoAllegato().getSoggetto().getUid()
					+ " corrispondente all'uid " + req.getDatiSoggettoAllegato().getUid());
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("relazione soggetto allegato atto", ""));
		}
		allegatoAttoDad.aggiornaRelazioneSoggettoAllegato(req.getDatiSoggettoAllegato());
	}

}
