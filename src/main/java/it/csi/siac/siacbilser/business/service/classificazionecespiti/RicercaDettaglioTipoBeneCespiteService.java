/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificazionecespiti;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.TipoBeneCespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespiteResponse;
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
public class RicercaDettaglioTipoBeneCespiteService extends CheckedAccountBaseService<RicercaDettaglioTipoBeneCespite, RicercaDettaglioTipoBeneCespiteResponse> {

	//DAD
	@Autowired
	private TipoBeneCespiteDad tipoBeneCespiteDad;
	
	private TipoBeneCespite tipoBeneCespite;

	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkEntita(req.getTipoBeneCespite(), "tipo bene");
		tipoBeneCespite = req.getTipoBeneCespite();
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
	public RicercaDettaglioTipoBeneCespiteResponse executeService(RicercaDettaglioTipoBeneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {

		
		Date inizioAnno = Utility.primoGiornoDellAnno(req.getAnnoBilancio());
		tipoBeneCespite.setDataInizioValiditaFiltro(inizioAnno);
		
		TipoBeneCespite tipoBene = tipoBeneCespiteDad.findDettaglioTipoBeneCespiteById(
				tipoBeneCespite ,
				req.getTipoBeneCespiteModelDetail()
		);		
		if (tipoBene == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Tipo Bene cespite non trovato ", "uid:" +req.getTipoBeneCespite().getUid()));
		}		

		res.setTipoBeneCespite(tipoBene);
		
	}

	

}