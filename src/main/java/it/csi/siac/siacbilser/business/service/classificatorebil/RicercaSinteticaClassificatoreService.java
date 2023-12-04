/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificatorebil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatoreResponse;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

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
public class RicercaSinteticaClassificatoreService extends CheckedAccountBaseService<RicercaSinteticaClassificatore, RicercaSinteticaClassificatoreResponse> {

	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"), false);
		checkNotNull(req.getTipologiaClassificatore(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipologia"), false);
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		checkCondition(req.getParametriPaginazione().getNumeroPagina() >= 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"), false);
		checkCondition(req.getParametriPaginazione().getElementiPerPagina() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"), false);
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaSinteticaClassificatoreResponse executeService(RicercaSinteticaClassificatore serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		ListaPaginata<Codifica> codifiche = classificatoriDad.ricercaSinteticaClassificatore(ente, req.getTipologiaClassificatore(), req.getAnno(),
				req.getCodice(), req.getDescrizione(), req.getParametriPaginazione());
		res.setCodifiche(codifiche);
	}

}
