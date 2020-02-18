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
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccecser.model.StatoOperativoOperazioneCassa;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaOperazioneDiCassaService extends CheckedAccountBaseService<AnnullaOperazioneDiCassa, AnnullaOperazioneDiCassaResponse> {
		
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
		operazioneDiCassaDad.setLoginOperazione(loginOperazione);
		operazioneDiCassaDad.setEnte(ente);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AnnullaOperazioneDiCassaResponse executeService(AnnullaOperazioneDiCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		operazioneCassa = caricaOperazione(operazioneCassa.getUid());
		checkStato();
		operazioneCassa.setStatoOperativoOperazioneCassa(StatoOperativoOperazioneCassa.ANNULLATO);
		operazioneDiCassaDad.aggiornaOperazioneCassa(operazioneCassa);
		res.setOperazioneCassa(operazioneCassa);
		
	}

	private OperazioneCassa caricaOperazione(Integer uidCassa) {
		OperazioneCassa operazione = operazioneDiCassaDad.ricercaDettaglioOperazioneDiCassa(uidCassa);
		if(operazione == null || operazione.getUid() == 0){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("operazione di cassa", "uid: " + uidCassa));
		}
		return operazione;
	}

	private void checkStato() {
		if(StatoOperativoOperazioneCassa.ANNULLATO.equals(operazioneCassa.getStatoOperativoOperazioneCassa())){
			throw new BusinessException(ErroreCEC.CEC_ERR_0004.getErrore());
		}
		if(StatoOperativoOperazioneCassa.DEFINITIVO.equals(operazioneCassa.getStatoOperativoOperazioneCassa())){
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("operazione di cassa ", operazioneCassa.getStatoOperativoOperazioneCassa().getDescrizione() ));
		}
	}

	
	
}
