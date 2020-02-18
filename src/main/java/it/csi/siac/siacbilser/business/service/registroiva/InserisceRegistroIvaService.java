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
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceRegistroIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceRegistroIvaService extends CheckedAccountBaseService<InserisceRegistroIva, InserisceRegistroIvaResponse> {
	
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
	public InserisceRegistroIvaResponse executeService(InserisceRegistroIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkRegistroGiaEsistente();
		registroIvaDad.inserisciRegistroIva(registroIva);
		res.setRegistroIva(registroIva);
	}

	private void checkRegistroGiaEsistente() {
		RegistroIva registro = registroIvaDad.findRegistroIvaByCodice(registroIva.getCodice(), registroIva.getEnte().getUid(), registroIva.getGruppoAttivitaIva().getUid());
		
		if(registro!=null){
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("registro iva ", registro.getCodice()), Esito.FALLIMENTO);
		}
	}
	
}
