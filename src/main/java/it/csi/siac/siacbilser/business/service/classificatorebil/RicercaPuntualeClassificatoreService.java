/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificatorebil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeClassificatoreResponse;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualeClassificatoreService extends CheckedAccountBaseService<RicercaPuntualeClassificatore, RicercaPuntualeClassificatoreResponse> {

	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		if(req.getUid() != null) {
			checkCondition(req.getUid().intValue() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid"), false);
		} else {
			checkEntita(req.getEnte(), "ente", false);
			checkNotNull(req.getTipologiaClassificatore(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipologia"), false);
			checkNotBlank(req.getCodice(), "codice", false);
			checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
			
			checkCondition(req.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"), false);
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaPuntualeClassificatoreResponse executeService(RicercaPuntualeClassificatore serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		Codifica codifica;
		if(req.getUid() != null) {
			codifica = classificatoriDad.ricercaClassificatoreById(req.getUid());
		} else {
			codifica = classificatoriDad.findByEnteCodiceTipoClassificatoreAndAnno(req.getEnte(), req.getCodice(), req.getTipologiaClassificatore(), req.getBilancio().getAnno());
		}
		res.setCodifica(codifica);
	}

}
