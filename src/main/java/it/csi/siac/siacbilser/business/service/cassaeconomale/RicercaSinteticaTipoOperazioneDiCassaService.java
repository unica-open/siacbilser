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
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaTipoOperazioneDiCassaService extends CheckedAccountBaseService<RicercaSinteticaTipoOperazioneDiCassa, RicercaSinteticaTipoOperazioneDiCassaResponse> {
		
	private TipoOperazioneCassa tipoOperazioneCassa;
	
	@Autowired
	private TipoOperazioneDiCassaDad tipoOperazioneDiCassaDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		tipoOperazioneCassa = req.getTipoOperazioneCassa();
		
		checkNotNull(tipoOperazioneCassa, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo operazione di cassa"));
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri di paginazione"));
		
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
	public RicercaSinteticaTipoOperazioneDiCassaResponse executeService(RicercaSinteticaTipoOperazioneDiCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		ListaPaginata<TipoOperazioneCassa> listaTipiOperazione = tipoOperazioneDiCassaDad.ricercaSinteticaTipoOperazioneCassa(tipoOperazioneCassa, req.getParametriPaginazione());
		if(listaTipiOperazione == null || listaTipiOperazione.isEmpty()){
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			res.setEsito(Esito.FALLIMENTO);
		}
		res.setTipiOperazione(listaTipiOperazione);
	}

	
	
}
