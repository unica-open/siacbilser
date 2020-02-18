/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacfinser.business.service.AbstractBaseServicePerVecchiaRicercaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestione;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneResponse;

@Deprecated
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public abstract class RicercaAttributiMovimentoGestioneService<REQ extends RicercaAttributiMovimentoGestione, RES extends RicercaAttributiMovimentoGestioneResponse> extends AbstractBaseServicePerVecchiaRicercaImpegno<REQ, RES> {

	@Autowired
	protected CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
	@Autowired
	protected CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	@Autowired
	protected ProvvedimentoService provvedimentoService;
	
}
