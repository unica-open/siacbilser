/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siacbilser.business.service.classifgsa;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceClassificatoreGSAResponse;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;


/**
 * Servizio di Inserimento di un Conto figlio
 * 
 * @author Elisa
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceClassificatoreGSAService extends CrudClassificatoreGSABaseService<InserisceClassificatoreGSA, InserisceClassificatoreGSAResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.classificatoreGSA = req.getClassificatoreGSA();
		this.bilancio = req.getBilancio();
		checkNotNull(this.classificatoreGSA, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("classificatore gsa"));
		checkNotNull(req.getBilancio(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("bilancio").getTesto());
		checkNotBlank(this.classificatoreGSA.getCodice(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codice classificatore gsa").getTesto(),false );
		checkNotBlank(this.classificatoreGSA.getDescrizione(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione classificatore gsa").getTesto(),false );
		checkNotNull(this.classificatoreGSA.getStato(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("stato classificatore gsa").getTesto(),false );
		checkCondition(req.getBilancio().getUid() != 0 || req.getBilancio().getAnno() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("bilancio"));
		
	}
	
	@Override
	@Transactional
	public InserisceClassificatoreGSAResponse executeService(InserisceClassificatoreGSA serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		caricaBilancio();
		
		checkCodiceClassificatoreGSAUnivoco();
		
		checkClassificatorePadre();
		
		inserisciClassificatoreGSA();
		
		res.setClassificatoreGSA(classificatoreGSA);
		
	}

	/**
	 * Inserisci classificatore GSA.
	 */
	private void inserisciClassificatoreGSA() {
		popolaDatiDefaultClassificatoreGSA();
		classificatoreGsaDad.inserisciClassificatoreGSA(classificatoreGSA);
	}

	/**
	 * Check esistenza classificatore GSA.
	 */
	private void checkCodiceClassificatoreGSAUnivoco() {
		ClassificatoreGSA cgsa = caricaClassificatoreGSAByCodice();
		if(cgsa != null) {
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Classificatore gsa", "il codice \""+ cgsa.getCodice()+ "\" "));
		}
		
	}
}
