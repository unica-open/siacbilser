/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.stipendi.oneri;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacintegser.business.service.stipendi.ElaboraFileStipendiBaseService;
import it.csi.siac.siacintegser.business.service.stipendi.model.StipendioParams;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileOneriService extends ElaboraFileStipendiBaseService {
	
	
	@Override
	protected boolean isTipoElaborazioneONERI() {
		return true;
	}

	@Override
	protected boolean isTipoElaborazioneSTIPE() {
		return false;
	}
	
	@Override
	protected boolean isTipoElaborazioneTUTTO() {
		return false;
	}
	
	@Override
	protected String getCodiceDocumento() {
		return StipendioParams.CODICE_DOCUMENTO; 
	}
	
}
