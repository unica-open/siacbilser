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
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ControlloImportiImpegniVincolati;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ControlloImportiImpegniVincolatiResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ControlloImportiImpegniVincolatiService extends CheckedAccountBaseService<ControlloImportiImpegniVincolati, ControlloImportiImpegniVincolatiResponse> {
	
	
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	


	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
	}
	
	@Override
	@Transactional
	public ControlloImportiImpegniVincolatiResponse executeService(ControlloImportiImpegniVincolati serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	protected void init() {
		allegatoAttoDad.setLoginOperazione(loginOperazione);
		
	}
	
	@Override
	protected void execute() {	
		List<Integer> listaAllAttoId = req.getListaAllegatoAttoId();
		List<Messaggio> messaggi = allegatoAttoDad.controlloImportiImpegniVincolati(listaAllAttoId);
		res.setMessaggi(messaggi);
	}
	

	

}
