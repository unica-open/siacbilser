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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SbloccaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SbloccaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * The Class SbloccaRegistroIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SbloccaRegistroIvaService extends CheckedAccountBaseService<SbloccaRegistroIva, SbloccaRegistroIvaResponse> {
	
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
	public SbloccaRegistroIvaResponse executeService(SbloccaRegistroIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		checkRegistroBloccato();
		bloccaRegistro();
		res.setRegistroIva(registroIva);
	}

	/**
	 * Controlla che il registro sia bloccato. In caso contrario, previene lo sblocco
	 */
	private void checkRegistroBloccato() {
		RegistroIva ri = registroIvaDad.findRegistroIvaByIdMinimal(registroIva.getUid());
		if(ri == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Registro IVA", "uid " + registroIva.getUid()));
		}
		if(!Boolean.TRUE.equals(ri.getFlagBloccato())) {
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Blocco Registro IVA", "il registro risulta gia' sbloccato"));
		}
	}

	private void bloccaRegistro() {
		registroIva.setFlagBloccato(Boolean.FALSE);
		registroIvaDad.aggiornaFlagBloccato(registroIva);
	}
	
}
