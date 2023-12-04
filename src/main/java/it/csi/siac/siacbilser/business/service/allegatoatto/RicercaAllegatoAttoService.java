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
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAttoModelDetail;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAllegatoAttoService extends CheckedAccountBaseService<RicercaAllegatoAtto,RicercaAllegatoAttoResponse> {

	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	//private Soggetto soggetto;
	private AllegatoAtto allegatoAtto;
	private ElencoDocumentiAllegato elencoDocumentiAllegato;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		this.allegatoAtto = req.getAllegatoAtto();
		
		//this.soggetto     = req.getSoggetto();
		this.elencoDocumentiAllegato = req.getElencoDocumentiAllegato();
		
		checkNotNull(allegatoAtto, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
		checkNotNull(allegatoAtto.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(allegatoAtto.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		
		// Le seguenti condizioni sono controllate a front-end: necessito dell'uid per effettuare la ricerca
//		// Se indicato anno elenco deve esserlo anche il numero e viceversa.
//		checkCondition(elencoDocumentiAllegato == null || !(elencoDocumentiAllegato.getAnno() == null ^ elencoDocumentiAllegato.getNumero() == null),
//				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("anno e numero elenco devono essere entrambi valorizzati o non valorizzati"), false);
//		// Se indicato anno provvedimento deve esserlo anche il numero e viceversa.
//		checkCondition(req.getAllegatoAtto().getAttoAmministrativo() == null
//				|| !(req.getAllegatoAtto().getAttoAmministrativo().getAnno() == 0 ^ req.getAllegatoAtto().getAttoAmministrativo().getNumero() == 0),
//				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("anno e numero provvedimento devono essere entrambi valorizzati o non valorizzati"), false);
		
		// Se indicata causale devono essere presenti almeno 3 caratteri.
		checkCondition(req.getAllegatoAtto().getCausale() == null
				|| req.getAllegatoAtto().getCausale().isEmpty()
				|| req.getAllegatoAtto().getCausale().length() >= 3,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("la causale deve avere almeno tre caratteri, se presente"));
	}
	
	@Override
	@Transactional
	public RicercaAllegatoAttoResponse executeService(RicercaAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	protected void init() {
		allegatoAttoDad.setLoginOperazione(loginOperazione);
		allegatoAttoDad.setEnte(allegatoAtto.getEnte());
	}
	
	
	@Override
	protected void execute() {
		// SIAC-5021: retrocompatibilita'
		if(req.getAllegatoAttoModelDetails() == null) {
			req.setAllegatoAttoModelDetails(AllegatoAttoModelDetail.IsAssociatoAdAlmenoUnaQuotaSpesa, AllegatoAttoModelDetail.IsAssociatoAdUnDocumento);
		}
		
		ListaPaginata<AllegatoAtto> allegatiAtto = allegatoAttoDad.ricercaSinteticaAllegatoAtto(
				allegatoAtto,
				elencoDocumentiAllegato,
				req.getDataScadenzaDa(),
				req.getDataScadenzaA(),
				req.getFlagRitenute(),
				req.getSoggetto(),
				req.getImpegno(),
				req.getSubImpegno(),
				req.getStatiOperativiFiltri(),
				// SIAC-5660
				req.getListaAttoAmministrativo(),
				// SIAC-6166
				req.getBilancio(),
				req.getParametriPaginazione(),
				req.getAllegatoAttoModelDetails());
		res.setAllegatiAtto(allegatiAtto);
	}

}
