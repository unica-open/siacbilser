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
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaTipoBeneCespiteService extends CheckedAccountBaseService<RicercaSinteticaTipoBeneCespite, RicercaSinteticaTipoBeneCespiteResponse> {

	//DAD
	@Autowired
	private TipoBeneCespiteDad tipoBeneCespiteDad;
	
	private TipoBeneCespite tipoBeneCespite;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		
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
	public RicercaSinteticaTipoBeneCespiteResponse executeService(RicercaSinteticaTipoBeneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		Utility.MDTL.addModelDetails(req.getModelDetails());
		
		tipoBeneCespite = req.getTipoBeneCespite() != null? req.getTipoBeneCespite() : new TipoBeneCespite() ;
		Date inizioAnno = Utility.primoGiornoDellAnno(req.getAnnoBilancio());
		tipoBeneCespite.setDataInizioValiditaFiltro(inizioAnno);

		ListaPaginata<TipoBeneCespite> lista = tipoBeneCespiteDad.ricercaSinteticaTipoBeneCespite(
				tipoBeneCespite ,
				req.getContoPatrimoniale(),
				req.getCategoriaCespiti(),
				Utility.MDTL.byModelDetailClass(TipoBeneCespiteModelDetail.class),
				req.getParametriPaginazione()
		);		
		res.setListaTipoBeneCespite(lista);
	}
	
}