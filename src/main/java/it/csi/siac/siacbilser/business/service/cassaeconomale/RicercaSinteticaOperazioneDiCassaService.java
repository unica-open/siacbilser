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
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaOperazioneDiCassaService extends CheckedAccountBaseService<RicercaSinteticaOperazioneDiCassa, RicercaSinteticaOperazioneDiCassaResponse> {
		
	
	private OperazioneCassa operazioneCassa;
	
	@Autowired 
	private OperazioneDiCassaDad operazioneDiCassaDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getOperazioneCassa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("operazione di cassa"));
		operazioneCassa = req.getOperazioneCassa();
		checkEntita(operazioneCassa.getCassaEconomale(),"cassa economale");
		checkEntita(operazioneCassa.getBilancio(),"bilancio operazione di cassa");
		
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("paramentri di paginazione"));
		
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
	public RicercaSinteticaOperazioneDiCassaResponse executeService(RicercaSinteticaOperazioneDiCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		ListaPaginata<OperazioneCassa> listaOperazioni = operazioneDiCassaDad.ricercaSinteticaOperazioneDiCassa(operazioneCassa.getBilancio(),operazioneCassa.getCassaEconomale(),operazioneCassa.getDataOperazione(),
				operazioneCassa.getTipoOperazioneCassa(), operazioneCassa.getStatoOperativoOperazioneCassa(),null, req.getParametriPaginazione());
		
		if(listaOperazioni == null || listaOperazioni.isEmpty()){
			res.setEsito(Esito.FALLIMENTO);
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
		}
		res.setOperazioniCassa(listaOperazioni);
		
	}

	
	
}
