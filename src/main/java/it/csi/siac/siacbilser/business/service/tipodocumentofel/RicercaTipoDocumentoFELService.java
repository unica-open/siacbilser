/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipodocumentofel;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.model.TipoDocFEL;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Ricerca Tipo Documento di una fattura elettronica
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTipoDocumentoFELService extends BaseTipoDocumentoFELService<RicercaTipoDocumentoFEL, RicercaTipoDocumentoFELResponse> {
	
	@Override
	@Transactional(readOnly = true)
	public RicercaTipoDocumentoFELResponse executeService(RicercaTipoDocumentoFEL serviceRequest) {
		return super.executeService(serviceRequest);
	}

	 
 
	@Override
	protected void checkServiceParam() throws ServiceParamError {
			
	}
	
	 
	
	@Override
	protected void init() {
		super.init();

		if (req.getTipoDocFEL() == null) {
			req.setTipoDocFEL(new TipoDocFEL());
		}
	}

	
	

	@Override
	protected void execute() {
		 List<TipoDocFEL> tipoDocumentoFEL = tipoDocumentoFELDad.ricercaTipoDocumentoFEL(req.getTipoDocFEL());
			if(tipoDocumentoFEL==null){
				res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Tipo Documento", "id: "+req.getTipoDocFEL().getUid()));
				res.setEsito(Esito.FALLIMENTO);			
				return;
			}
			res.setTipoDocumentiFEL(tipoDocumentoFEL);
	}
	
  


}
