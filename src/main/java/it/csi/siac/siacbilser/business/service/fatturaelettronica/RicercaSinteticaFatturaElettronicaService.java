/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fatturaelettronica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.FatturaFELDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronicaResponse;
import it.csi.siac.sirfelser.model.FatturaFEL;

/**
 * Ricerca sintetica di una fattura elettronica
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaFatturaElettronicaService extends CheckedAccountBaseService<RicercaSinteticaFatturaElettronica, RicercaSinteticaFatturaElettronicaResponse> {
	
	private FatturaFEL fatturaFEL;
	
	@Autowired
	private FatturaFELDad fatturaFELDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getFatturaFEL(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("fattura elettronica"));
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		
		fatturaFEL = req.getFatturaFEL();
	}
	
	@Override
	protected void init() {
		fatturaFELDad.setEnte(ente);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaSinteticaFatturaElettronicaResponse executeService(RicercaSinteticaFatturaElettronica serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		ListaPaginata<FatturaFEL> listaFatture = fatturaFELDad.ricercaSinteticaFatturaFEL(fatturaFEL, req.getDataFatturaDa(), req.getDataFatturaA(), req.getParametriPaginazione());
		if(listaFatture.isEmpty()){
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		res.setFattureFEL(listaFatture);
	}

}
