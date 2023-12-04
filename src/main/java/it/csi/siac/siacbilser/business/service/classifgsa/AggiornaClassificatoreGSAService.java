/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classifgsa;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaClassificatoreGSAResponse;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;


/**
 * Servizio di Inserimento di un Conto figlio
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaClassificatoreGSAService extends CrudClassificatoreGSABaseService<AggiornaClassificatoreGSA, AggiornaClassificatoreGSAResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.classificatoreGSA = req.getClassificatoreGSA();
		this.bilancio = req.getBilancio();
		checkEntita(this.classificatoreGSA, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("classificatore gsa").getTesto());
		checkEntita(this.bilancio, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("bilancio").getTesto());
		checkNotBlank(this.classificatoreGSA.getCodice(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codice classificatore gsa").getTesto(),false );
		checkNotNull(this.classificatoreGSA.getStatoOperativoClassificatoreGSA(), "stato classificatore");
	}
	
	@Override
	@Transactional
	public AggiornaClassificatoreGSAResponse executeService(AggiornaClassificatoreGSA serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		caricaBilancio();
		
		checkEsistenzaClassificatoreGSA();
		
		popolaDatiDefaultClassificatoreGSA();
		
		aggiornaClassificatoreGSA();
		
		res.setClassificatoreGSA(classificatoreGSA);
	}
	
	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}


	private void aggiornaClassificatoreGSA() {
		classificatoreGsaDad.aggiornaClassificatoreGSA(classificatoreGSA);
		
	}

	private void checkEsistenzaClassificatoreGSA() {
		ClassificatoreGSA cgsaOld = checkClassificatoreGSAEsistente();
		
		checkCodiceClassificatoreGSA(cgsaOld);
		
	}

	
	private void checkCodiceClassificatoreGSA(ClassificatoreGSA cgsaOld) {
		if(cgsaOld.getCodice().equals(classificatoreGSA.getCodice())) {
			return;
		}
		ClassificatoreGSA cgsa = caricaClassificatoreGSAByCodice();
		if(cgsa != null && cgsa.getUid() != cgsaOld.getUid()) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Codice non univoco: "+ this.classificatoreGSA.getCodice()));
		}
		
	}
	

}
