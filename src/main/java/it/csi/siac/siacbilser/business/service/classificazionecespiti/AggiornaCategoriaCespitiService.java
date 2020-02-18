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
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCategoriaCespitiService extends CheckedAccountBaseService<AggiornaCategoriaCespiti, AggiornaCategoriaCespitiResponse> {

	//DAD
	@Autowired
	private CategoriaCespitiDad categoriaCespitiDad;

	private CategoriaCespiti categoriaCespiti;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getCategoriaCespiti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("categoria cespiti"));	
		
		categoriaCespiti = req.getCategoriaCespiti();
		checkNotBlank(categoriaCespiti.getCodice(), "codice categoria cespiti",false);
		checkNotBlank(categoriaCespiti.getDescrizione(), "descrizione categoria cespiti",false);		
		checkNotNull(categoriaCespiti.getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito"));
		checkNotNull(categoriaCespiti.getAliquotaAnnua(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aliquota annua"));
		checkEntita(categoriaCespiti.getCategoriaCalcoloTipoCespite(), "tipo calcolo");
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
	public AggiornaCategoriaCespitiResponse executeService(AggiornaCategoriaCespiti serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		Date inizioAnno = Utility.primoGiornoDellAnno(req.getAnnoBilancio());
		categoriaCespiti.setDataInizioValidita(inizioAnno);
		categoriaCespitiDad.aggiornaCategoriaCespiti(categoriaCespiti);				
		res.setCategoriaCespiti(categoriaCespiti);
	}

	

}