/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classifgsa;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassificatoreGSAValido;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassificatoreGSAValidoResponse;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;


/**
 * Servizio di ricerca dei classificatori GSA validi
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/01/2018
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaClassificatoreGSAValidoService extends CrudClassificatoreGSABaseService<RicercaClassificatoreGSAValido, RicercaClassificatoreGSAValidoResponse> {

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//checkEntita(req.getBilancio(), "bilancio");
		checkNotNull(req.getAmbito(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Ambito"));
	}
	
	@Override
	@Transactional
	public RicercaClassificatoreGSAValidoResponse executeService(RicercaClassificatoreGSAValido serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		List<ClassificatoreGSA> classificatoriGSA = classificatoreGsaDad.ricercaClassificatoreGSAValidi(req.getAmbito());
		res.setClassificatoriGSA(classificatoriGSA);
		res.setCardinalitaComplessiva(classificatoriGSA.size());
	}

}
