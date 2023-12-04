/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.azione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.azione.RicercaAzionePerChiave;
import it.csi.siac.siacbilser.frontend.webservice.msg.azione.RicercaAzionePerChiaveResponse;
import it.csi.siac.siacbilser.integration.dad.AzioneDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Servizio per la ricerca dell'azione per chiave
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAzionePerChiaveService extends CheckedAccountBaseService<RicercaAzionePerChiave, RicercaAzionePerChiaveResponse> {
	
	@Autowired
	private AzioneDad azioneDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAzione(), "azione");
		checkNotBlank(req.getAzione().getNome(), "nome azione");
	}
	
	@Override
	protected void init() {
		azioneDad.setEnte(ente);
	}
	
	@Override
	@Transactional(readOnly= true)
	public RicercaAzionePerChiaveResponse executeService(RicercaAzionePerChiave serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		Azione azione = azioneDad.getAzioneByNomeWithGruppo(req.getAzione().getNome());
		
		if(azione == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Azione", req.getAzione().getNome()));
		}
		res.setAzione(azione);
		res.setGruppoAzioni(azione.getGruppo());
	}

}
