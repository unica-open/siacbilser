/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaRegistroIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaRegistroIvaService extends CheckedAccountBaseService<AggiornaRegistroIva, AggiornaRegistroIvaResponse> {


	/** The registro iva dad. */
	@Autowired
	private RegistroIvaDad registroIvaDad;
	
	/** The registro iva. */
	private RegistroIva registroIva;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		registroIva=req.getRegistroIva();
		checkNotNull(registroIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro Iva"));
		checkCondition(registroIva.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid registro Iva"));
		checkNotNull(registroIva.getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice registro Iva"));
		checkNotNull(registroIva.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione registro Iva"));
		
		// Ente
		checkNotNull(registroIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(registroIva.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		// Gruppo Attivita Iva
		checkNotNull(registroIva.getGruppoAttivitaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("gruppo attivita Iva"));
		checkCondition(registroIva.getGruppoAttivitaIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid gruppo attivita Iva"));
		// Tipo Registro Iva
		checkNotNull(registroIva.getTipoRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registro Iva"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		registroIvaDad.setLoginOperazione(loginOperazione);
		registroIvaDad.setEnte(registroIva.getEnte());
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public AggiornaRegistroIvaResponse executeService(AggiornaRegistroIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		registroIvaDad.aggiornaRegistroIva(registroIva);
		res.setRegistroIva(registroIva);
	}

}
