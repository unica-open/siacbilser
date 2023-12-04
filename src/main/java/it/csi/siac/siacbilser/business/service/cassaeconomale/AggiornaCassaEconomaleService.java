/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CassaEconomaleDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaCassaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCassaEconomaleService extends CheckedAccountBaseService<AggiornaCassaEconomale, AggiornaCassaEconomaleResponse> {
	
	@Autowired 
	private CassaEconomaleDad cassaEconomaleDad;
	
	private CassaEconomale cassaEconomale;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCassaEconomale(), "cassa economale", true);
		cassaEconomale = req.getCassaEconomale();
		checkCondition(StringUtils.isNotBlank(cassaEconomale.getDescrizione()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione cassa economale"), false);
		checkCondition(StringUtils.isNotBlank(cassaEconomale.getResponsabile()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("responsabile cassa economale"), false);
		checkNotNull(cassaEconomale.getTipoDiCassa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo di cassa economale"), false);
		checkCondition(cassaEconomale.getTipoDiCassa().equals(TipoDiCassa.CONTANTI) || StringUtils.isNotBlank(cassaEconomale.getNumeroContoCorrente()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero conto corrente cassa economale"), false);
		
		// Lotto M
		checkCondition(cassaEconomale.getLimiteImportoNotNull().signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Limite importo impegno", ": non puo' essere negativo"), false);
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		cassaEconomaleDad.setEnte(ente);
		cassaEconomaleDad.setLoginOperazione(loginOperazione);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaCassaEconomaleResponse executeService(AggiornaCassaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		cassaEconomaleDad.aggiornaCassaEconomale(cassaEconomale);
		res.setCassaEconomale(cassaEconomale);
	}

	
	
}
