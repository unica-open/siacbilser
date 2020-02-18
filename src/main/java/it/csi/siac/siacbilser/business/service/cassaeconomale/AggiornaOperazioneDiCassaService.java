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
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccecser.model.StatoOperativoOperazioneCassa;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaOperazioneDiCassaService extends CheckedAccountBaseService<AggiornaOperazioneDiCassa, AggiornaOperazioneDiCassaResponse> {
		
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
		
		checkEntita(operazioneCassa.getCassaEconomale(), "cassa economale");
		checkEntita(operazioneCassa.getBilancio(), "bilancio operazione di cassa");
		checkNotNull(operazioneCassa.getNumeroOperazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero operazione"));
		checkNotNull(operazioneCassa.getDataOperazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data operazione"));
		checkNotNull(operazioneCassa.getTipoOperazioneCassa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo operazione"));
		checkNotNull(operazioneCassa.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo"));
		checkNotNull(operazioneCassa.getStatoOperativoOperazioneCassa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo operazione"));
		checkNotNull(operazioneCassa.getModalitaPagamentoCassa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("modalità di pagamento"));
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		operazioneDiCassaDad.setLoginOperazione(loginOperazione);
		operazioneDiCassaDad.setEnte(ente);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaOperazioneDiCassaResponse executeService(AggiornaOperazioneDiCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkStatoProvvisorio();
		operazioneDiCassaDad.aggiornaOperazioneCassa(operazioneCassa);
		res.setOperazioneCassa(operazioneCassa);
		
	}

	private void checkStatoProvvisorio() {
		if(!StatoOperativoOperazioneCassa.PROVVISORIO.equals(operazioneCassa.getStatoOperativoOperazioneCassa())){
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("l'operazione di cassa ", operazioneCassa.getStatoOperativoOperazioneCassa().getDescrizione()));
		}
	}

	
	
}
