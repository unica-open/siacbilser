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
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.CategoriaCespitiModelDetail;
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
public class AnnullaCategoriaCespitiService extends CheckedAccountBaseService<AnnullaCategoriaCespiti, AnnullaCategoriaCespitiResponse> {

	//DAD
	@Autowired
	private CategoriaCespitiDad categoriaCespitiDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getCategoriaCespiti(), "categoria cespiti");
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
	public AnnullaCategoriaCespitiResponse executeService(AnnullaCategoriaCespiti serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkAnnullabilita();
		Date fineAnnoPrecedente = Utility.ultimoGiornoDellAnno(req.getAnnoBilancio().intValue() - 1);
		
		categoriaCespitiDad.annullaCategoriaCespiti(req.getCategoriaCespiti(), fineAnnoPrecedente);
	}

	private void checkAnnullabilita() {
		int uidCategoria = req.getCategoriaCespiti().getUid();
		CategoriaCespiti categoriaDB = categoriaCespitiDad.findCategoriaCespitiById(uidCategoria, Utility.primoGiornoDellAnno(req.getAnnoBilancio()), new CategoriaCespitiModelDetail[] {CategoriaCespitiModelDetail.Annullato});
		String descCategoria = new StringBuilder().append("categoria uid[ ").append(uidCategoria).append(" ] ").toString();
		if(categoriaDB == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("annullamento", descCategoria));
		}
		if(Boolean.TRUE.equals(categoriaDB.getAnnullato())) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(descCategoria + " risulta essere annullata."));
		}
		
	}

	

}
