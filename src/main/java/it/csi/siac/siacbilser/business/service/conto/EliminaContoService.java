/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ContoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaContoResponse;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Permette di annullare un conto e tutti i suoi figli.
 * 
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaContoService extends CheckedAccountBaseService<EliminaConto, EliminaContoResponse> {
	
	//DAD
	@Autowired
	protected ContoDad contoDad;
	
	private Conto conto;
	
	/*
	 * (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.conto = req.getConto();
		checkEntita(conto, "Conto");
		
	}
	
	
	@Override
	@Transactional
	public EliminaContoResponse executeService(EliminaConto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		contoDad.setEnte(ente);
		contoDad.setLoginOperazione(loginOperazione);
		
	}
	
	
	@Override
	protected void execute() {
		
		
		checkContoConCausaliTutteAnnullate();
		checkContoConPrimeNoteTutteAnnullate();
		contoDad.eliminaConto(this.conto);
	}


	private void checkContoConPrimeNoteTutteAnnullate() {
		Long count = contoDad.countPrimeNoteNonAnnullateAssociateAlConto(this.conto);
		if(count>0) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il conto o uno dei suoi figli e' collegato a prime note in corso di validita'"));
		}
		
	}


	/**
	 *  NON è eliminabile se: è collegato ad una causale in stato diverso da 'ANNULLATA'
	 */
	private void checkContoConCausaliTutteAnnullate() {
		
		Long count = contoDad.countCausaliNonAnnullateAssociateAlConto(this.conto);
		if(count>0) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il conto o uno dei suoi figli e' collegato a causali in corso di validita', occorre scollegare le causali prima procedere all'annullamento"));
		}
		
	}

}
