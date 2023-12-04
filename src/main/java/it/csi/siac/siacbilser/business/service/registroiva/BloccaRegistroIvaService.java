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
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.BloccaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.BloccaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * The Class BloccaRegistroIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BloccaRegistroIvaService extends CheckedAccountBaseService<BloccaRegistroIva, BloccaRegistroIvaResponse> {
	
	/** The registro iva dad. */
	@Autowired
	private RegistroIvaDad registroIvaDad;
	
	private RegistroIva registroIva;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getRegistroIva(), "registro");
		this.registroIva = req.getRegistroIva();
	}

	@Transactional
	@Override
	public BloccaRegistroIvaResponse executeService(BloccaRegistroIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		
		log.debug(methodName, "Controlli di coerenza per il registro");
		checkRegistroSbloccato();
		log.debug(methodName, "Blocco del registro");
		bloccaRegistro();
		res.setRegistroIva(registroIva);
	}

	/**
	 * Controlla che il registro sia sbloccato. In caso contrario, previene il blocco
	 */
	private void checkRegistroSbloccato() {
		final String methodName = "checkRegistroSbloccato";
		RegistroIva ri = registroIvaDad.findRegistroIvaByIdMinimal(registroIva.getUid());
		if(ri == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Registro IVA", "uid " + registroIva.getUid()));
		}
		log.debug(methodName, "Registro iva bloccato? " + Boolean.TRUE.equals(ri.getFlagBloccato()));
		if(!Boolean.FALSE.equals(ri.getFlagBloccato())) {
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Blocco Registro IVA", "il registro risulta gia' bloccato"));
		}
	}

	private void bloccaRegistro() {
		registroIva.setFlagBloccato(Boolean.TRUE);
		registroIvaDad.aggiornaFlagBloccato(registroIva);
	}
	
}
