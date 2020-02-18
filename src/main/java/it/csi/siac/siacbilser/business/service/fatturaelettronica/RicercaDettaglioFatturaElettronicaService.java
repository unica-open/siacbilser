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
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronicaResponse;
import it.csi.siac.sirfelser.model.FatturaFEL;

/**
 * Ricerca di dettaglio di una fattura elettronica
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioFatturaElettronicaService extends CheckedAccountBaseService<RicercaDettaglioFatturaElettronica, RicercaDettaglioFatturaElettronicaResponse> {
	
	private FatturaFEL fatturaFEL;
	
	@Autowired
	private FatturaFELDad fatturaFELDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getFatturaFEL(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("fattura elettronica"));
		fatturaFEL = req.getFatturaFEL();
		checkCondition(fatturaFEL.getIdFattura() != null && fatturaFEL.getIdFattura().intValue() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id fattura elettronica"));
		
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
	public RicercaDettaglioFatturaElettronicaResponse executeService(RicercaDettaglioFatturaElettronica serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		FatturaFEL fattura = fatturaFELDad.ricercaDettaglioFatturaFEL(fatturaFEL.getIdFattura());
		if(fattura==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("fattura elettronica", "id: "+fatturaFEL.getIdFattura()));
			res.setEsito(Esito.FALLIMENTO);			
			return;
		}
		res.setFatturaFEL(fattura);
	}


}
