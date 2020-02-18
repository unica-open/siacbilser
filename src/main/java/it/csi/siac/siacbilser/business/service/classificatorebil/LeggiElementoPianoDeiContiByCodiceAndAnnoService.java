/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificatorebil;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnnoResponse;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * 
 * Ricerca l'elenco dei classificatori in relazione ad un classificatore specifico.
 * Vedi tabella: siac_r_class
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiElementoPianoDeiContiByCodiceAndAnnoService extends CheckedAccountBaseService<LeggiElementoPianoDeiContiByCodiceAndAnno, LeggiElementoPianoDeiContiByCodiceAndAnnoResponse> {

	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getElementoPianoDeiConti() != null && StringUtils.isNotBlank(req.getElementoPianoDeiConti().getCodice()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice"));
		checkNotNull(req.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"));
	}
	
	@Transactional(readOnly=true)
	@Override
	public LeggiElementoPianoDeiContiByCodiceAndAnnoResponse executeService(LeggiElementoPianoDeiContiByCodiceAndAnno serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		ElementoPianoDeiConti elementoPianoDeiConti = classificatoriDad.ricercaElementoPianoDeiContiByCodiceAndAnno(req.getElementoPianoDeiConti(), ente.getUid(), req.getAnno());
		res.setElementoPianoDeiConti(elementoPianoDeiConti);
	}

}
