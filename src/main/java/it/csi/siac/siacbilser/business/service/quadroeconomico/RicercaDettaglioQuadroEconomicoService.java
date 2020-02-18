/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.quadroeconomico;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioClassificatoreGSA;
//import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioClassificatoreGSAResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaDettaglioQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaDettaglioQuadroEconomicoResponse;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


/**
 * Servizio di Inserimento di un Conto figlio
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioQuadroEconomicoService extends CrudQuadroEconomicoBaseService<RicercaDettaglioQuadroEconomico, RicercaDettaglioQuadroEconomicoResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.quadroEconomico = req.getQuadroEconomico();
		this.bilancio = req.getBilancio();
		checkEntita(this.quadroEconomico, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quadro economico ").getTesto());
		checkEntita(this.bilancio, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quadro economico ").getTesto());
	}
	
	@Override
	@Transactional
	public RicercaDettaglioQuadroEconomicoResponse executeService(RicercaDettaglioQuadroEconomico serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		caricaBilancio();
		
		//Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		
		QuadroEconomico quadroEconomicoTrovato = quadroEconomicoDad.findQuadroEconomicoById(this.quadroEconomico, req.getQuadroEconomicoModelDetails());
		
		if(quadroEconomicoTrovato==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("quadro economico ", "uid[" +  this.quadroEconomico.getUid() + "]"));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}

		res.setQuadroEconomico(quadroEconomicoTrovato);
		
	}

}
