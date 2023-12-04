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
import it.csi.siac.siacbilser.integration.dad.DismissioneCespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteDismissioneCespiteResponse;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.DismissioneCespiteModelDetail;
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
public class CollegaCespiteDismissioneCespiteService extends CheckedAccountBaseService<CollegaCespiteDismissioneCespite, CollegaCespiteDismissioneCespiteResponse> {

	//DAD
	@Autowired
	private DismissioneCespiteDad dismissioneCespiteDad;
	@Autowired
	private CespiteDad cespiteDad;

	private DismissioneCespite dismissioneCespite;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		dismissioneCespite = req.getDismissioneCespite();
		
		checkEntita(dismissioneCespite, "dismissione");
		checkCondition(req.getUidsCespiti() != null  && !req.getUidsCespiti().isEmpty() , ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cespiti da collegare"));
		
	}
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);	
		dismissioneCespiteDad.setEnte(ente);
	}
	
	@Transactional
	@Override
	public CollegaCespiteDismissioneCespiteResponse executeService(CollegaCespiteDismissioneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkDismissioneCespiteEsistente();
		
		//TODO: cntrolla che i cespiti non siano collegati a dismissioni
		
		//conrtrolla stato
		
		cespiteDad.collegaCespitiADismissione(req.getUidsCespiti(), dismissioneCespite);
		
		res.setDismissioneCespite(dismissioneCespite);
	}
	
	/**
	 * Check dismissione cespite esistente.
	 */
	private void checkDismissioneCespiteEsistente() {
		DismissioneCespite dc = dismissioneCespiteDad.findDismissioneCespiteById(dismissioneCespite, new DismissioneCespiteModelDetail[]{});
		if(dc == null || (dc.getEnte()!= null && dc.getEnte().getUid() != ente.getUid())){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("dismissione cespite", "dismissione [uid: " + dismissioneCespite.getUid() + " ] "));
		}
		dismissioneCespite = dc;
	}

}