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
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespitePerChiave;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespitePerChiaveResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Ricerca cespite per chiave
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/08/2018
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCespitePerChiaveService extends CheckedAccountBaseService<RicercaCespitePerChiave, RicercaCespitePerChiaveResponse> {

	// DAD
	@Autowired
	private CespiteDad cespiteDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCespite(), "cespite");
		checkNotBlank(req.getCespite().getNumeroInventario(), "numero inventario cespite");
	}
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
	}
	
	@Transactional(readOnly = true)
	@Override
	public RicercaCespitePerChiaveResponse executeService(RicercaCespitePerChiave serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		Utility.MDTL.addModelDetails(req.getModelDetails());
		Cespite cespite = cespiteDad.findByNumeroInventario(req.getCespite().getNumeroInventario(), Utility.MDTL.byModelDetailClass(CespiteModelDetail.class));
		if (cespite == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("cespite", "numero inventario " + req.getCespite().getNumeroInventario()));
		}
		res.setCespite(cespite);
	}

}