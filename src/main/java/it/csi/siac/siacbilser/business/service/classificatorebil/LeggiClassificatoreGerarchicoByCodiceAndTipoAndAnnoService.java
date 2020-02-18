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
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Codifica;
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
public class LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoService extends CheckedAccountBaseService<LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno, LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse> {

	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getClassificatore() != null && StringUtils.isNotBlank(req.getClassificatore().getCodice()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice classificatore"));
		checkNotNull(req.getTipologiaClassificatore(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipologia classificatore"));
		checkNotNull(req.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"));
	}
	
	@Transactional(readOnly=true)
	@Override
	public LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse executeService(LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		Codifica codifica = classificatoriDad.ricercaClassificatoreGerarchicoByCodiceAndTipoAndAnno(req.getClassificatore(), req.getTipologiaClassificatore(), ente.getUid(), req.getAnno());
		res.setClassificatore(codifica);
	}

}
