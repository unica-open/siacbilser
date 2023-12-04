/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaScrittureInventarioByEntitaCollegata;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaScrittureInventarioByEntitaCollegataResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;


/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaScrittureInventarioByEntitaCollegataService extends CheckedAccountBaseService<RicercaScrittureInventarioByEntitaCollegata, RicercaScrittureInventarioByEntitaCollegataResponse> {

	//DAD
	@Autowired
	private CespiteDad cespiteDad;
	@Autowired
	@Inventario
	private PrimaNotaInvDad primaNotaInvDad;

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition((req.getEntitaGeneranteScritture() != null && req.getEntitaGeneranteScritture().getUid() != 0) || 
				(req.getCespiteCollegatoAdEntitaGenerante() != null && req.getCespiteCollegatoAdEntitaGenerante().getUid() != 0) , 
				ErroreCore.ERRORE_DI_SISTEMA.getErrore("Entita collegata alla prima nota")
				);
		
	}
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);
		primaNotaInvDad.setEnte(ente);
		primaNotaInvDad.setLoginOperazione(loginOperazione);
	}
	
	@Transactional
	@Override
	public RicercaScrittureInventarioByEntitaCollegataResponse executeService(RicercaScrittureInventarioByEntitaCollegata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		Utility.MDTL.addModelDetails(req.getModelDetails());
		List<PrimaNota> listaCespite = primaNotaInvDad.ricercaScrittureInventarioByEntita(
				req.getEntitaGeneranteScritture(),
				req.getCespiteCollegatoAdEntitaGenerante(),
				null,
				null,
				Utility.MDTL.byModelDetailClass(PrimaNotaModelDetail.class), 
				Ambito.AMBITO_INV);
		res.setListaPrimaNota(listaCespite);
	}
	
}