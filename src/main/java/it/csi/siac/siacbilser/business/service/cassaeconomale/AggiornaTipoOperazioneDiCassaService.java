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
import it.csi.siac.siacbilser.integration.dad.TipoOperazioneDiCassaDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.StatoOperativoTipoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaTipoOperazioneDiCassaService extends CheckedAccountBaseService<AggiornaTipoOperazioneDiCassa, AggiornaTipoOperazioneDiCassaResponse> {
		
	
	@Autowired 
	private TipoOperazioneDiCassaDad tipoOperazioneDiCassaDad;
	
	private TipoOperazioneCassa tipoOperazioneCassa;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getTipoOperazioneCassa(), "tipo operazione di cassa");
		tipoOperazioneCassa = req.getTipoOperazioneCassa();
		
		checkCondition(StringUtils.isNotBlank(tipoOperazioneCassa.getCodice()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo operazione di cassa"), false);
		checkCondition(StringUtils.isNotBlank(tipoOperazioneCassa.getDescrizione()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione tipo operazione di cassa"), false);
		
		// Lotto M
		checkEntita(tipoOperazioneCassa.getCassaEconomale(), "cassa economale tipo operazione di cassa", false);
		checkEntita(tipoOperazioneCassa.getEnte(), "ente tipo operazione di cassa", false);
		checkNotNull(tipoOperazioneCassa.getTipologiaOperazioneCassa(), "tipologia operazione tipo operazione di cassa", false);
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
	public AggiornaTipoOperazioneDiCassaResponse executeService(AggiornaTipoOperazioneDiCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkStatoValido();
		tipoOperazioneDiCassaDad.aggiornaTipoOperazioneCassa(tipoOperazioneCassa);
		res.setTipoOperazioneCassa(tipoOperazioneCassa);
	}

	private void checkStatoValido() {
		TipoOperazioneCassa tipo = tipoOperazioneDiCassaDad.ricercaDettaglioTipoOperazioneDiCassa(tipoOperazioneCassa.getUid());
		if(StatoOperativoTipoOperazioneCassa.ANNULLATO.equals(tipo.getStatoOperativoTipoOperazioneCassa())){
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("tipo operazione di cassa",tipo.getStatoOperativoTipoOperazioneCassa().getDescrizione()));
		}
	}
	
	
}
