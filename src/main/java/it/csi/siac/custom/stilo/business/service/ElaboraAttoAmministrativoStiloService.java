/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.business.service;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.custom.stilo.business.service.provvedimento.AggiornaProvvedimentoStiloService;
import it.csi.siac.custom.stilo.business.service.provvedimento.VerificaAnnullabilitaProvvedimentoStiloService;
import it.csi.siac.siacbilser.business.service.provvedimento.AggiornaProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.VerificaAnnullabilitaProvvedimentoService;
import it.csi.siac.siacintegser.business.service.attiamministrativi.ElaboraAttoAmministrativoService;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraAttoAmministrativoStiloService extends ElaboraAttoAmministrativoService {
	
	@Override
	protected Class<? extends VerificaAnnullabilitaProvvedimentoService> getVerificaAnnullabilitaProvvedimentoServiceClass() {
		return VerificaAnnullabilitaProvvedimentoStiloService.class;
	}

	@Override
	protected Class<? extends AggiornaProvvedimentoService> getAggiornaProvvedimentoServiceClass() {
		return AggiornaProvvedimentoStiloService.class;
	}
}
