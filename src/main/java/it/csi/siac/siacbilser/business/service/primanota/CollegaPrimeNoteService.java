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
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacgenser.frontend.webservice.msg.CollegaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.CollegaPrimeNoteResponse;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CollegaPrimeNoteService extends CheckedAccountBaseService<CollegaPrimeNote, CollegaPrimeNoteResponse> {

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
	public CollegaPrimeNoteResponse executeService(CollegaPrimeNote serviceRequest) {
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
		checkCollegamentoGiaPresente();
		primaNotaDad.collegaPrimeNote(primaNotaPadre, primaNotaFiglia);
		if(primaNotaPadre.getListaPrimaNotaFiglia() != null){
			primaNotaPadre.getListaPrimaNotaFiglia().remove(primaNotaFiglia);
		}
		
		res.setPrimaNotaFiglia(primaNotaFiglia);
		res.setPrimaNotaPadre(primaNotaPadre);
	}

	private void checkCollegamentoGiaPresente() {
		if(primaNotaDad.isCollegamentoPresente(primaNotaPadre.getUid(), primaNotaFiglia.getUid())){
			throw new BusinessException(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore("il collegamento tra le prime note gia' presente."));
		}
		
	}


}
