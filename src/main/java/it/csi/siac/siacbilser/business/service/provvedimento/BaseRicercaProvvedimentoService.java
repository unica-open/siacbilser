/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.provvedimento;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.msg.base.BaseRicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.base.BaseRicercaProvvedimentoResponse;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

public abstract class BaseRicercaProvvedimentoService<BRP extends BaseRicercaProvvedimento, BRPR extends BaseRicercaProvvedimentoResponse> 
	extends CheckedAccountBaseService<BRP, BRPR> {
	
	@Autowired
	protected AttoAmministrativoDad attoAmministrativoDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		attoAmministrativoDad.setLoginOperazione(loginOperazione);
		attoAmministrativoDad.setEnte(req.getEnte());
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRicercaAtti(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ricerca atti"),true);
		
		if (req.getRicercaAtti().getUid() == null) {
			// SIAC-5586: tolta l'obbligatorieta' dell'anno
//			checkNotNull(req.getRicercaAtti().getAnnoAtto(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno atto"),true);
//			checkCondition(req.getRicercaAtti().getAnnoAtto() != null && (req.getRicercaAtti().getTipoAtto() != null ||  req.getRicercaAtti().getNumeroAtto() != null), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quando valorizzato anno atto bisogna valorizzare o il tipo o il numero atto"));
			checkCondition(req.getRicercaAtti().getTipoAtto() != null ||  req.getRicercaAtti().getNumeroAtto() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipo o numero atto"));
		}
	}
}
