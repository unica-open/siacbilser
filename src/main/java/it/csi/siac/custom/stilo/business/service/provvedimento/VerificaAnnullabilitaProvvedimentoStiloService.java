/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.business.service.provvedimento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.custom.stilo.integration.dad.ProvvedimentoStiloDad;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.provvedimento.VerificaAnnullabilitaProvvedimentoService;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaAnnullabilitaProvvedimentoStiloService extends VerificaAnnullabilitaProvvedimentoService {

	@Autowired
	private ProvvedimentoStiloDad provvedimentoStiloDad;

	@Override
	protected void init() {
		super.init();
		
		provvedimentoStiloDad.setLoginOperazione(loginOperazione);
		provvedimentoStiloDad.setEnte(ente);
	}

	@Override
	protected Boolean isAnnullabile(AttoAmministrativo attoAmministrativo) {
		return provvedimentoStiloDad.isAnnullabile(attoAmministrativo);
	}
}
