/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCollegamentoPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCollegamentoPrimeNoteResponse;
import it.csi.siac.siacgenser.model.PrimaNota;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaCollegamentoPrimeNoteService extends CheckedAccountBaseService<EliminaCollegamentoPrimeNote, EliminaCollegamentoPrimeNoteResponse> {

	@Autowired
	private PrimaNotaDad primaNotaDad;
	
	private PrimaNota primaNotaPadre;
	private PrimaNota primaNotaFiglia;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getPrimaNotaPadre(), "prima nota padre");
		checkEntita(req.getPrimaNotaFiglia(), "prima nota collegata");
		primaNotaPadre = req.getPrimaNotaPadre();
		primaNotaFiglia = req.getPrimaNotaFiglia();
	}
	
	@Transactional
	@Override
	public EliminaCollegamentoPrimeNoteResponse executeService(EliminaCollegamentoPrimeNote serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		primaNotaDad.setEnte(ente);
		primaNotaDad.setLoginOperazione(loginOperazione);
	}

	
	@Override
	protected void execute() {
		
		primaNotaDad.eliminaCollegamentoTraPrimeNote(primaNotaPadre, primaNotaFiglia);
		if(primaNotaPadre.getListaPrimaNotaFiglia() != null){
			primaNotaPadre.getListaPrimaNotaFiglia().remove(primaNotaFiglia);
		}
		
		res.setPrimaNotaFiglia(primaNotaFiglia);
		res.setPrimaNotaPadre(primaNotaPadre);
	}


}
