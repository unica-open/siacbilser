/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.gruppoattivitaiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.GruppoAttivitaIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;

/**
 * The Class AggiornaGruppoAttivitaIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaGruppoAttivitaIvaService extends CheckedAccountBaseService<AggiornaGruppoAttivitaIva, AggiornaGruppoAttivitaIvaResponse> {
	
	/** The gruppo attivita iva dad. */
	@Autowired
	private GruppoAttivitaIvaDad gruppoAttivitaIvaDad;
	
	/** The gruppo. */
	private GruppoAttivitaIva gruppo;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getGruppoAttivitaIva(), "gruppo attivita Iva");
		gruppo = req.getGruppoAttivitaIva();
		
		checkNotNull(gruppo.getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice gruppo Iva"), false);
		checkNotNull(gruppo.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione gruppo Iva"), false);
		checkNotNull(gruppo.getTipoChiusura(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo chiusura gruppo Iva"), false);
		checkNotNull(gruppo.getAnnualizzazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annualizzazione gruppo Iva"), false);
		
		checkEntita(gruppo.getEnte(), "ente gruppo", false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		gruppoAttivitaIvaDad.setLoginOperazione(loginOperazione);
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public AggiornaGruppoAttivitaIvaResponse executeService(AggiornaGruppoAttivitaIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		gruppoAttivitaIvaDad.aggiornaGruppoAttivitaIva(gruppo);
		res.setGruppoAttivitaIva(gruppo);
	}
	
}
