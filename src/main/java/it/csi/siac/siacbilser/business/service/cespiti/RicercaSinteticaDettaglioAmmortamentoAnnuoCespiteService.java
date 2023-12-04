/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.DettaglioAmmortamentoAnnuoCespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespiteModelDetail;
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
public class RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteService extends CheckedAccountBaseService<RicercaSinteticaDettaglioAmmortamentoAnnuoCespite, RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse> {

	//DAD
	@Autowired
	private DettaglioAmmortamentoAnnuoCespiteDad dettaglioAmmortamentoAnnuoCespiteDad;
	@Autowired
	private AmmortamentoAnnuoCespiteDad ammortamentoAnnuoCespiteDad;

	private Cespite cespite;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCespite(), "cespite");
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
	}
	
	@Override
	protected void init() {
		super.init();
		dettaglioAmmortamentoAnnuoCespiteDad.setEnte(ente);
		ammortamentoAnnuoCespiteDad.setEnte(ente);
	}
	
	@Transactional
	@Override
	public RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse executeService(RicercaSinteticaDettaglioAmmortamentoAnnuoCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		Utility.MDTL.addModelDetails(req.getModelDetails());
		ListaPaginata<DettaglioAmmortamentoAnnuoCespite> listaDettaglioAmmortamentoAnnuoCespite = dettaglioAmmortamentoAnnuoCespiteDad.ricercaSinteticaDettagliAmmortamentoAnnuoCespite(req.getCespite(), 
				Utility.MDTL.byModelDetailClass(DettaglioAmmortamentoAnnuoCespiteModelDetail.class),
				req.getParametriPaginazione());
		
		res.setListaDettaglioAmmortamentoAnnuoCespite(listaDettaglioAmmortamentoAnnuoCespite);
		
		AmmortamentoAnnuoCespite amm = ammortamentoAnnuoCespiteDad.caricaAmmortamentoAnnuoByCespite(req.getCespite(), new AmmortamentoAnnuoCespiteModelDetail[] {});
		res.setTotaleImportoAmmortato(amm != null && amm.getImportoTotaleAmmortato()!= null ? amm.getImportoTotaleAmmortato() : BigDecimal.ZERO);
		
	}
	
}