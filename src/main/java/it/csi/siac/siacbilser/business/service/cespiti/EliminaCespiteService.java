/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaCespiteService extends CheckedAccountBaseService<EliminaCespite, EliminaCespiteResponse> {

	//DAD
	@Autowired
	private CespiteDad cespiteDad;

	private Cespite cespite;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkEntita(req.getCespite(), "cespite");		
		cespite = req.getCespite();
	}
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);		
	}
	
	@Transactional
	@Override
	public EliminaCespiteResponse executeService(EliminaCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkCespiteEsistente();
		Cespite cespiteEliminato = cespiteDad.eliminaCespite(cespite);
		if(cespiteEliminato == null){
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile eliminare il cespite indicato."));
		}
		res.setCespite(cespiteEliminato);
	}

	/**
	 * Check cespite esistente.
	 */
	private void checkCespiteEsistente() {
		Cespite cespiteDaEliminare = cespiteDad.findCespiteById(cespite, new CespiteModelDetail[]{CespiteModelDetail.IsCollegatoAPrimeNote});
		if(cespiteDaEliminare == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("eliminazione cespite", "cespite [uid: " + cespite.getUid() + " ] "));
		}
		if(Boolean.TRUE.equals(cespiteDaEliminare.getIsCollegatoPrimeNote())) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il cespite risulta essere collegato a delle prime note."));
		}
	}

}