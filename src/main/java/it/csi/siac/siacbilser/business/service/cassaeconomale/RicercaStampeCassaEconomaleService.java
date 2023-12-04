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
import it.csi.siac.siacbilser.integration.dad.StampeCassaFileDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaStampeCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaStampeCassaEconomaleResponse;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaStampeCassaEconomaleService extends CheckedAccountBaseService<RicercaStampeCassaEconomale, RicercaStampeCassaEconomaleResponse> {
		
	
	@Autowired
	private StampeCassaFileDad stampeCassaFileDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getStampa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stampa cassa file"));
		checkEntita(req.getStampa().getCassaEconomale(), "cassa economale");
		checkNotNull(req.getStampa().getTipoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo documento"));
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri di paginazione"));
		checkEntita(req.getStampa().getBilancio(), "bilancio");
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		stampeCassaFileDad.setEnte(ente);
		stampeCassaFileDad.setLoginOperazione(loginOperazione);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaStampeCassaEconomaleResponse executeService(RicercaStampeCassaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		ListaPaginata<StampeCassaFile> list = stampeCassaFileDad.cercaStampeCassaEconomale(req.getStampa(), req.getNomeFile(), req.getDataUltimaStampaGiornaleDa(), req.getDataUltimaStampaGiornaleA(),  req.getParametriPaginazione());
		res.setListaStampe(list);
	}

	
	
}
