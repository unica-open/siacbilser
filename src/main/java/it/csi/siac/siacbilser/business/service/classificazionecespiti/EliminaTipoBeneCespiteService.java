/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificazionecespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siacbilser.integration.dad.TipoBeneCespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
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
public class EliminaTipoBeneCespiteService extends CheckedAccountBaseService<EliminaTipoBeneCespite, EliminaTipoBeneCespiteResponse> {

	//DAD
	@Autowired
	private TipoBeneCespiteDad tipoBeneCespiteDad;

	@Autowired
	private CespiteDad cespiteDad;

	
	private TipoBeneCespite tipoBeneCespite;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkEntita(req.getTipoBeneCespite(), "tipo bene cespite");		
		tipoBeneCespite = req.getTipoBeneCespite();		
		checkNotNull(req.getTipoBeneCespite(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Tipo Bene cespiti"));		
		//checkNotBlank(tipoBeneCespite.getCodice(), "codice tipo bene cespiti",false);
		//checkNotBlank(tipoBeneCespite.getDescrizione(), "descrizione tipo bene cespiti",false);		
		
	}
	
	@Override
	protected void init() {
		super.init();
		tipoBeneCespiteDad.setEnte(ente);
		tipoBeneCespiteDad.setLoginOperazione(loginOperazione);		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public EliminaTipoBeneCespiteResponse executeService(EliminaTipoBeneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkTipoBeneNonCollegatoACespite();
		tipoBeneCespiteDad.eliminaTipoBeneCespite(tipoBeneCespite);
		res.setTipoBeneCespite(tipoBeneCespite);
		
	}

	

	private void checkTipoBeneNonCollegatoACespite() {
		
		Long countCespiteCollegato = cespiteDad.countCespitiByTipoBene(tipoBeneCespite);

		if(countCespiteCollegato != null && countCespiteCollegato.longValue() != 0L) {
			//non esitono cespiti collegati non posso eliminare il tipo bene 
			throw new BusinessException(ErroreCore.ESISTONO_ENTITA_COLLEGATE.getErrore());
		}
		
	}
	
}
