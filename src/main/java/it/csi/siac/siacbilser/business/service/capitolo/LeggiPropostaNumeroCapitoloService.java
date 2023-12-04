/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiPropostaNumeroCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiPropostaNumeroCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.util.ServiceUtils;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.helper.CoreServiceHelper;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiPropostaNumeroCapitoloService extends CheckedAccountBaseService<LeggiPropostaNumeroCapitolo, LeggiPropostaNumeroCapitoloResponse>{ 
	
	
	@Autowired
	private CapitoloDad capitoloDad;

	@Autowired protected CoreServiceHelper coreServiceHelper;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkEntita(req.getEnte(), "ente");
	}
	
	@Override
	protected void init() {
		super.init();
		capitoloDad.setEnte(ente);
	}
	
	@Transactional
	public LeggiPropostaNumeroCapitoloResponse executeService(LeggiPropostaNumeroCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	@Override
	protected void execute() {
		Integer limiteNumerazioneAutomaticaCapitolo = getLimiteNumerazioneAutomaticaCapitolo();
		res.setNumeroPropostoCapitolo(capitoloDad.getPropostaNumeroCapitolo(limiteNumerazioneAutomaticaCapitolo));
	}
	
	//task-86
	private Integer getLimiteNumerazioneAutomaticaCapitolo() {
		String limiteNumerazioneAutomaticaCapitolo = coreServiceHelper.getParametroConfigurazioneEnte(
				ParametroConfigurazioneEnteEnum.LIMITE_NUMERAZIONE_AUTOMATICA_CAPITOLO.getNomeParametro(), req.getRichiedente());

		ServiceUtils.assertNotNull(limiteNumerazioneAutomaticaCapitolo, 
				ErroreCore.PARAMETRO_ENTE_NON_CONFIGURATO.getErrore(ParametroConfigurazioneEnteEnum.LIMITE_NUMERAZIONE_AUTOMATICA_CAPITOLO.name()));
		
		return Integer.parseInt(limiteNumerazioneAutomaticaCapitolo);
	}
	


	
}
