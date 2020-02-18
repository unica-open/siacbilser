/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classifgsa;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaClassificatoreGSAResponse;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;


/**
 * Servizio di Inserimento di un Conto figlio
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaClassificatoreGSAService extends CrudClassificatoreGSABaseService<RicercaSinteticaClassificatoreGSA, RicercaSinteticaClassificatoreGSAResponse> {
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.classificatoreGSA = req.getClassificatoreGSA();
		checkNotNull(classificatoreGSA, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("classificatore gsa"));
		//L'operatore inserisce il Classificatore e/o la descrizione e preme il pulsante
//		checkCondition(StringUtils.isNotBlank(classificatoreGSA.getCodice()) ^ StringUtils.isNotBlank(classificatoreGSA.getDescrizione()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codice classificatore gsa"));
		
	}
	
	@Override
	@Transactional
	public RicercaSinteticaClassificatoreGSAResponse executeService(RicercaSinteticaClassificatoreGSA serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		caricaClassificatoriGSA();
	}

	private void caricaClassificatoriGSA() {
		ListaPaginata<ClassificatoreGSA> classificatoriGSA = classificatoreGsaDad.ricercaSinteticaClassificatoreGSA(req.getClassificatoreGSA(), req.getParametriPaginazione());
		res.setClassificatoriGSA(classificatoriGSA);
		res.setCardinalitaComplessiva(classificatoriGSA.getTotaleElementi());
	}

}
