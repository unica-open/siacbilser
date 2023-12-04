/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registrocomunicazionipcc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistriComunicazioniPCCSubdocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistriComunicazioniPCCSubdocumentoResponse;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;

/**
 * The Class RicercaRegistriComunicazioniPCCSubdocumentoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaRegistriComunicazioniPCCSubdocumentoService extends CheckedAccountBaseService<RicercaRegistriComunicazioniPCCSubdocumento, RicercaRegistriComunicazioniPCCSubdocumentoResponse> {
	
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getSubdocumentoSpesa(), "subdocumento");
	}
	
	@Override
	protected void init() {
		registroComunicazioniPCCDad.setEnte(ente);
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaRegistriComunicazioniPCCSubdocumentoResponse executeService(RicercaRegistriComunicazioniPCCSubdocumento serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		List<RegistroComunicazioniPCC> listRegistroComunicazioniPCC = registroComunicazioniPCCDad.findRegistriBySubdocumento(req.getSubdocumentoSpesa());
		
		res.setRegistriComunicazioniPCC(listRegistroComunicazioniPCC);
		res.setCardinalitaComplessiva(listRegistroComunicazioniPCC.size());
	}

}
