/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CausaleDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnullaCausaleEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaCausaleEntrataService extends CheckedAccountBaseService<AnnullaCausaleEntrata, AnnullaCausaleEntrataResponse> {
	
	/** The causale dad. */
	@Autowired
	private CausaleDad causaleDad;
	
	/** The causale. */
	private CausaleEntrata causale;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		causale= req.getCausaleEntrata();
		
		checkNotNull(causale, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("causale"));
		checkCondition(causale.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("causale"));
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AnnullaCausaleEntrataResponse executeService(AnnullaCausaleEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		inizializzaDataFineValiditaSeNonImpostata();
		causaleDad.annullaCausaleEntrata(causale);
		causale.setStatoOperativoCausale(StatoOperativoCausale.ANNULLATA);
		res.setCausaleEntrata(causale);
		
		
	}
	
	private void inizializzaDataFineValiditaSeNonImpostata() {
		Date dataFineValidita = causale.getDataScadenza() != null ? causale.getDataScadenza() : new Date();
		causale.setDataFineValidita(dataFineValidita);
	}

}
