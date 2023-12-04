/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siacbilser.business.service.quadroeconomico;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.InserisceQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.InserisceQuadroEconomicoResponse;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
/*
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceClassificatoreGSAResponse;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
*/

/**
 * Servizio di Inserimento di un Conto figlio
 * 
 * @author Elisa
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceQuadroEconomicoService extends CrudQuadroEconomicoBaseService<InserisceQuadroEconomico, InserisceQuadroEconomicoResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {

		this.quadroEconomico = req.getQuadroEconomico();
		this.bilancio = req.getBilancio();
		checkNotNull(this.quadroEconomico, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quadro economico"));
		checkNotNull(req.getBilancio(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("bilancio").getTesto());
		checkNotBlank(this.quadroEconomico.getCodice(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codice quadro economico").getTesto(),false );
		checkNotBlank(this.quadroEconomico.getDescrizione(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione quadro economico").getTesto(),false );
		checkNotNull(this.quadroEconomico.getStato(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("statoquadro economico").getTesto(),false );
		checkCondition(req.getBilancio().getUid() != 0 || req.getBilancio().getAnno() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("bilancio"));
		
	}
	
	@Override
	@Transactional
	public InserisceQuadroEconomicoResponse executeService(InserisceQuadroEconomico serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		caricaBilancio();
		
		checkCodiceQuadroEconomicoUnivoco();
		
		checkQuadroEconomicoPadre();
		
		inserisciQuadroEconomico();
		
		res.setQuadroEconomico(quadroEconomico);
		
	}

	/**
	 * Inserisci quadroeconomico.
	 */
	private void inserisciQuadroEconomico() {
		popolaDatiDefaultQuadroEconomico();
		quadroEconomicoDad.inserisciQuadroEconomico(quadroEconomico);
	}

/*
	private void checkCodiceQuadroEconomicoUnivocoOld() {		
		List<QuadroEconomico> cgsa = caricaQuadroEconomicoByCodice();
		if(cgsa != null) {
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Quadro economico", "il codice \""+ cgsa.getCodice()+ "\" "));
		}		
	}
*/
	private void checkCodiceQuadroEconomicoUnivoco() {
		List<QuadroEconomico> cgsa = caricaQuadroEconomicoByCodiceAndParte();
		
		if(cgsa != null && cgsa.size()>0) {
			QuadroEconomico qe = cgsa.get(0);
			String codice = qe.getCodice();
			
			// controllare la parte
			String parte = qe.getParteQuadroEconomico().getCodice();
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Quadro economico", " Codice \""+ codice + "\" Parte: "+parte+"  " ));
		}
	}


}
