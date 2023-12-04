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
import it.csi.siac.siacbilser.integration.dad.OperazioneDiCassaDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioOperazioneDiCassaService extends CheckedAccountBaseService<RicercaDettaglioOperazioneDiCassa, RicercaDettaglioOperazioneDiCassaResponse> {
		
	
	private OperazioneCassa operazioneCassa;
	
	@Autowired
	private OperazioneDiCassaDad operazioneDiCassaDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getOperazioneCassa(), "operazione di cassa");
		operazioneCassa = req.getOperazioneCassa();
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		operazioneDiCassaDad.setEnte(ente);
		operazioneDiCassaDad.setLoginOperazione(loginOperazione);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaDettaglioOperazioneDiCassaResponse executeService(RicercaDettaglioOperazioneDiCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		OperazioneCassa operazione = operazioneDiCassaDad.ricercaDettaglioOperazioneDiCassa(operazioneCassa.getUid());
		if(operazione == null){
			res.setEsito(Esito.FALLIMENTO);
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("operazione di cassa ", "uid: " + operazioneCassa.getUid()));
		}
		res.setOperazioneCassa(operazione);
	}

	
	
}
