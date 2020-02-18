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
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaQuoteElencoService extends CheckedAccountBaseService<RicercaSinteticaQuoteElenco,RicercaSinteticaQuoteElencoResponse> {
	//SIAC-5589 cambiare nome in RicercaSinteticaQuoteElencoService
	
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getElencoDocumentiAllegato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti allegato"));
		checkCondition(req.getElencoDocumentiAllegato().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid elenco documenti allegato"));
		checkParametriPaginazione(req.getParametriPaginazione());
	}
	
	@Override
	@Transactional(readOnly=true, timeout=120)
	public RicercaSinteticaQuoteElencoResponse executeService(RicercaSinteticaQuoteElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	protected void init() {
		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);
		elencoDocumentiAllegatoDad.setEnte(ente);
	}
	
	
	@Override
	protected void execute() {
		ListaPaginata<Subdocumento<?,?>> subdocumenti = elencoDocumentiAllegatoDad.findElencoQuoteDocumentiAllegatoPageableById(req.getElencoDocumentiAllegato().getUid(),req.getSoggetto() ,req.getParametriPaginazione());
        res.setSubdocumenti(subdocumenti);

        ElencoDocumentiAllegato eda = elencoDocumentiAllegatoDad.findElencoDocumentiAllegatoMinimalLightById(req.getElencoDocumentiAllegato().getUid());
        res.setElencoDocumentiAllegato(eda);
	}

}
