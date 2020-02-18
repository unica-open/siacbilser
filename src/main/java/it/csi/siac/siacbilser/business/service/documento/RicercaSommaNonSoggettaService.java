/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.TipoOnereDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggetta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggettaResponse;
import it.csi.siac.siacfin2ser.model.CodiceSommaNonSoggetta;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaCausale770Service.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSommaNonSoggettaService extends CheckedAccountBaseService<RicercaSommaNonSoggetta, RicercaSommaNonSoggettaResponse>{
	
	/** The causale dad. */
	@Autowired
	private TipoOnereDad tipoOnereDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkEntita(req.getTipoOnere(), "tipo onere");
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public RicercaSommaNonSoggettaResponse executeService(RicercaSommaNonSoggetta serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<CodiceSommaNonSoggetta> result = tipoOnereDad.ricercaSommeNonSoggetteByTipoOnere(req.getTipoOnere());
		res.setElencoCodiciSommaNonSoggetta(result);
		res.setCardinalitaComplessiva(result.size());
		res.setDataOra(new Date());
		res.setEsito(Esito.SUCCESSO);
	}
}
