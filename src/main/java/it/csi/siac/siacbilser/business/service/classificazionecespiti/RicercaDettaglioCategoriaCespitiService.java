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
import it.csi.siac.siacbilser.integration.dad.CategoriaCespitiDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
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
public class RicercaDettaglioCategoriaCespitiService extends CheckedAccountBaseService<RicercaDettaglioCategoriaCespiti, RicercaDettaglioCategoriaCespitiResponse> {

	//DAD
	@Autowired
	private CategoriaCespitiDad categoriaCespitiDad;

	private CategoriaCespiti categoriaCespiti;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkEntita(req.getCategoriaCespiti(), "categoria cespiti");
		categoriaCespiti = req.getCategoriaCespiti();
	}
	
	@Override
	protected void init() {
		super.init();
		categoriaCespitiDad.setEnte(ente);
		categoriaCespitiDad.setLoginOperazione(loginOperazione);		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public RicercaDettaglioCategoriaCespitiResponse executeService(RicercaDettaglioCategoriaCespiti serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		Date inizioAnno = Utility.primoGiornoDellAnno(req.getAnnoBilancio());
		categoriaCespiti.setDataInizioValiditaFiltro(inizioAnno);
		
		CategoriaCespiti cc = categoriaCespitiDad.findCategoriaCespitiById(categoriaCespiti.getUid(), inizioAnno, req.getCategoriaCespitiModelDetails());
		if (cc == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Categoria cespiti", "uid:" + categoriaCespiti.getUid()));
		}		
		res.setCategoriaCespiti(cc);
	}
	
}