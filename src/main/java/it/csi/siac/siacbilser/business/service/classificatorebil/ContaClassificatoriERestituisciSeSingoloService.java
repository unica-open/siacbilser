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
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaClassificatoriERestituisciSeSingolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaClassificatoriERestituisciSeSingoloResponse;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContaClassificatoriERestituisciSeSingoloService extends CheckedAccountBaseService<ContaClassificatoriERestituisciSeSingolo, ContaClassificatoriERestituisciSeSingoloResponse> {

	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"), false);
		checkNotNull(req.getTipologiaClassificatore(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipologia"), false);
	}
	
	@Override
	@Transactional(readOnly=true)
	public ContaClassificatoriERestituisciSeSingoloResponse executeService(ContaClassificatoriERestituisciSeSingolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		Long count = classificatoriDad.contaByTipoClassificatoreAndAnno(ente, req.getTipologiaClassificatore(), req.getAnno());
		res.setCount(count);
		
		if(count != null && count.longValue() == 1L) {
			// Ricercare il classificatore
			Codifica codifica = classificatoriDad.findSingleByTipoClassificatoreAndAnno(ente, req.getTipologiaClassificatore(), req.getAnno());
			res.setCodifica(codifica);
		}
	}

}
