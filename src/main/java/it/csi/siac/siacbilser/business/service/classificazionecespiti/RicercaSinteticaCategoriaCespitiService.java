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
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
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
public class RicercaSinteticaCategoriaCespitiService extends CheckedAccountBaseService<RicercaSinteticaCategoriaCespiti, RicercaSinteticaCategoriaCespitiResponse> {

	//DAD
	@Autowired
	private CategoriaCespitiDad categoriaCespitiDad;

	private CategoriaCespiti categoriaCespiti;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		categoriaCespiti = req.getCategoriaCespiti();
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		
	}
	
	@Override
	protected void init() {
		super.init();
		categoriaCespitiDad.setEnte(ente);
		categoriaCespitiDad.setLoginOperazione(loginOperazione);		
	}
	
	@Transactional
	@Override
	public RicercaSinteticaCategoriaCespitiResponse executeService(RicercaSinteticaCategoriaCespiti serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		impostaValoriDefault();
		
		ListaPaginata<CategoriaCespiti> listaCategoriaCespiti = categoriaCespitiDad.ricercaSinteticaCategoriaCespiti(categoriaCespiti, req.getEscludiAnnullati(), req.getParametriPaginazione(), req.getCategoriaCespitiModelDetails());
		res.setListaCategoriaCespiti(listaCategoriaCespiti);
	}

	/**
	 * 
	 */
	private void impostaValoriDefault() {
		if(categoriaCespiti == null){
			categoriaCespiti = new CategoriaCespiti();
		}
		Date inizioAnno = Utility.primoGiornoDellAnno(req.getAnnoBilancio());
		categoriaCespiti.setDataInizioValiditaFiltro(inizioAnno);
	}
	
}