/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaEntrataResponse;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class InserisceRigaEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceRigaEntrataService extends BaseGestioneRigaEntrataService<InserisceRigaEntrata, InserisceRigaEntrataResponse> {
	
	
	/**
	 * Esegue solo il check dei parametri nella request.
	 *
	 * @param req the req
	 * @throws ServiceParamError the service param error
	 */
	public void checkServiceParam(InserisceRigaEntrata req) throws ServiceParamError {
		this.req = req;
		checkServiceParamBase();
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		dett = req.getDettaglioEntrataCronoprogramma();
		checkParamsDettaglio();
	}
	
	@Override
	@Transactional
	public InserisceRigaEntrataResponse executeService(InserisceRigaEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		// CR-4103, 4153
		checkClassificazione();
		
		//SIAC-6255
		TipoCapitolo tipoCapitoloDettaglio = caricaTipoCapitoloDettaglio();
		
		cronoprogrammaDad.inserisciDettaglioEntrataCronoprogramma(dett, tipoCapitoloDettaglio);
		res.setDettaglioEntrataCronoprogramma(dett);
	}
	
	/**
	 * Carica tipo capitolo dettaglio.
	 *
	 * @return the tipo capitolo
	 */
	private TipoCapitolo caricaTipoCapitoloDettaglio() {
		TipoProgetto tipoProgettoAssciato = cronoprogrammaDad.getTipoProgettoByCronoprogramma(dett.getCronoprogramma());
		if(tipoProgettoAssciato == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("progetto associato al cronoprogramma", "impossibile reperire il tipo (previsione/gestione)"));
		}
		
		return tipoProgettoAssciato.getTipoCapitoloEntrata();
	}
	
}
