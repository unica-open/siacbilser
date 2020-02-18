/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AnteprimaAmmortamentoAnnuoCespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;


/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteService extends CheckedAccountBaseService<RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite, RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse> {

	//DAD
	@Autowired
	private AnteprimaAmmortamentoAnnuoCespiteDad anteprimaAmmortamentoAnnuoCespiteDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
	}
	
	@Override
	protected void init() {
		super.init();
		anteprimaAmmortamentoAnnuoCespiteDad.setEnte(ente);
	}
	
	@Transactional
	@Override
	public RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse executeService(RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		Utility.MDTL.addModelDetails(req.getModelDetails());
		ListaPaginata<DettaglioAnteprimaAmmortamentoAnnuoCespite> listaDettaglioAnteprimaAmmortamentoAnnuoCespite = anteprimaAmmortamentoAnnuoCespiteDad.ricercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite(
				req.getAnteprimaAmmortamentoAnnuoCespite(), 
				Utility.MDTL.byModelDetailClass(DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail.class),
				req.getParametriPaginazione());
		
		res.setListaDettaglioAnteprimaAmmortamentoAnnuoCespite(listaDettaglioAnteprimaAmmortamentoAnnuoCespite);
		
		
	}
	
}