/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.quadroeconomico;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.AggiornaQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.AggiornaQuadroEconomicoResponse;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


/**
 * Servizio di Inserimento di un Conto figlio
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaQuadroEconomicoService extends CrudQuadroEconomicoBaseService<AggiornaQuadroEconomico, AggiornaQuadroEconomicoResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.quadroEconomico = req.getQuadroEconomico();
		this.bilancio = req.getBilancio();
		checkEntita(this.quadroEconomico, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quadro economico").getTesto());
		checkEntita(this.bilancio, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("bilancio").getTesto());
		checkNotBlank(this.quadroEconomico.getCodice(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codice quadro economico").getTesto(),false );
		checkNotNull(this.quadroEconomico.getStatoOperativoQuadroEconomico(), "stato quadro Economico");
		checkNotNull(this.quadroEconomico.getParteQuadroEconomico(), "parte quadro Economico");
	}
	
	@Override
	@Transactional
	public AggiornaQuadroEconomicoResponse executeService(AggiornaQuadroEconomico serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {		
		caricaBilancio();	
		checkEsistenzaQuadroEconomico();		
		popolaDatiDefaultQuadroEconomico();	
		aggiornaQuadroEconomico();		
		res.setQuadroEconomico(quadroEconomico);
	}
	
	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}

	private void aggiornaQuadroEconomico() {
		quadroEconomicoDad.aggiornaQuadroEconomico(quadroEconomico);
		
	}

	private void checkEsistenzaQuadroEconomico() {
		QuadroEconomico cgsaOld = checkQuadroEconomicoEsistente();		
		checkCodiceQuadroEconomico(cgsaOld);		
	}

	private void checkCodiceQuadroEconomico(QuadroEconomico cgsaOld) {
		if(cgsaOld.getCodice().equals(quadroEconomico.getCodice()) && cgsaOld.getParteQuadroEconomico().getCodice().equals(quadroEconomico.getParteQuadroEconomico().getCodice())) {
			return;
		}
		List<QuadroEconomico> cgsa =  caricaQuadroEconomicoByCodiceAndParte();
		if(cgsa != null && cgsa.size()>0 && cgsa.get(0).getUid() != cgsaOld.getUid()) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Codice non univoco: "+ this.quadroEconomico.getCodice()));
		}
		
	}
	

}
