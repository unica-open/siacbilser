/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CausaleDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioCausaleEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioCausaleEntrataService extends CheckedAccountBaseService<RicercaDettaglioCausaleEntrata, RicercaDettaglioCausaleEntrataResponse> {
	
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
		causale = req.getCausaleEntrata();
		
		checkNotNull(causale, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("causale"));		
		checkCondition(causale.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid causale"));
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaDettaglioCausaleEntrataResponse executeService(RicercaDettaglioCausaleEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		causaleDad.setLoginOperazione(loginOperazione);
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		CausaleEntrata preDocumentoSpesa = causaleDad.findCausaleEntrataById(causale.getUid());
		if(preDocumentoSpesa==null) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("CausaleEntrata","uid: "+ causale.getUid()));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		res.setCausaleEntrata(preDocumentoSpesa);	
	}

}
