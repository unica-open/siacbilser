/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RichiestaEconomaleDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaRichiestaEconomaleService extends CheckedAccountBaseService<AnnullaRichiestaEconomale, AnnullaRichiestaEconomaleResponse> {
		
	private RichiestaEconomale richiestaEconomale;
	
	@Autowired
	private RichiestaEconomaleDad richiestaEconomaleDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getRichiestaEconomale(), "richiesta economale");
		richiestaEconomale = req.getRichiestaEconomale();
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		richiestaEconomaleDad.setEnte(ente);
		richiestaEconomaleDad.setLoginOperazione(loginOperazione);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AnnullaRichiestaEconomaleResponse executeService(AnnullaRichiestaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkRichiestaAnnullabile();
		richiestaEconomaleDad.aggiornaStatoRichiestaEconomale(richiestaEconomale, StatoOperativoRichiestaEconomale.ANNULLATA);
		
	}

	private void checkRichiestaAnnullabile() {
		StatoOperativoRichiestaEconomale statoOperativo = richiestaEconomaleDad.findStatoOperativo(richiestaEconomale.getUid());
		
		if( !statoOperativo.equals(StatoOperativoRichiestaEconomale.PRENOTATA) &&
			!statoOperativo.equals(StatoOperativoRichiestaEconomale.EVASA) &&	
			!statoOperativo.equals(StatoOperativoRichiestaEconomale.DA_RENDICONTARE) ){
			
			throw new BusinessException(ErroreCEC.CEC_ERR_0008.getErrore());
		}
	}

	
	
}
