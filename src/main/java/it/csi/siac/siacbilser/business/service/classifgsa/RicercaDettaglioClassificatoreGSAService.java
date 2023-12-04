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
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioClassificatoreGSAResponse;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;


/**
 * Servizio di Inserimento di un Conto figlio
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioClassificatoreGSAService extends CrudClassificatoreGSABaseService<RicercaDettaglioClassificatoreGSA, RicercaDettaglioClassificatoreGSAResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.classificatoreGSA = req.getClassificatoreGSA();
		this.bilancio = req.getBilancio();
		checkEntita(this.classificatoreGSA, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("classificatore gsa").getTesto());
		checkEntita(this.bilancio, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("classificatore gsa").getTesto());
	}
	
	@Override
	@Transactional
	public RicercaDettaglioClassificatoreGSAResponse executeService(RicercaDettaglioClassificatoreGSA serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		caricaBilancio();
		
		//Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		
		ClassificatoreGSA classifTrovato = classificatoreGsaDad.findClassificatoreGSAById(this.classificatoreGSA, req.getClassificatoreGSAModelDetails());
		
		if(classifTrovato==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("ClassificatoreGSA ", "uid[" +  this.classificatoreGSA.getUid() + "]"));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}

		res.setClassificatoreGSA(classifTrovato);
		
	}

}
