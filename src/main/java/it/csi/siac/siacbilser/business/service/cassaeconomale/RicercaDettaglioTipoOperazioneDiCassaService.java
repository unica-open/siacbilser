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
import it.csi.siac.siacbilser.integration.dad.TipoOperazioneDiCassaDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioTipoOperazioneDiCassaService extends CheckedAccountBaseService<RicercaDettaglioTipoOperazioneDiCassa, RicercaDettaglioTipoOperazioneDiCassaResponse> {
		
	
	private TipoOperazioneCassa tipoOperazioneCassa;
	
	@Autowired
	private TipoOperazioneDiCassaDad tipoOperazioneDiCassaDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getTipoOperazioneCassa(), "tipo operazione di cassa");
		tipoOperazioneCassa = req.getTipoOperazioneCassa();
		
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		tipoOperazioneDiCassaDad.setEnte(ente);
		tipoOperazioneDiCassaDad.setLoginOperazione(loginOperazione);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaDettaglioTipoOperazioneDiCassaResponse executeService(RicercaDettaglioTipoOperazioneDiCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		TipoOperazioneCassa tipo = tipoOperazioneDiCassaDad.ricercaDettaglioTipoOperazioneDiCassa(tipoOperazioneCassa.getUid());
		if(tipo == null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("tipo operazione di cassa", "uid " + tipoOperazioneCassa.getUid()));
			res.setEsito(Esito.FALLIMENTO);
		}
		res.setTipoOperazioneCassa(tipo);
	}

	
	
}
