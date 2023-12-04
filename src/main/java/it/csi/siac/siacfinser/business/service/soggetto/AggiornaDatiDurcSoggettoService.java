/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaDatiDurcSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaDatiDurcSoggettoResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.TipoFonteDurc;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaDatiDurcSoggettoService extends CheckedAccountBaseService<AggiornaDatiDurcSoggetto, AggiornaDatiDurcSoggettoResponse> {
	
	@Autowired
	private SoggettoFinDad soggettoDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getIdSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id soggetto"));
		checkNotNull(req.getTipoFonteDurc(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo fonte durc"));
		checkCondition(
				TipoFonteDurc.AUTOMATICA.getCodice().equals(req.getTipoFonteDurc()) || 
				TipoFonteDurc.MANUALE.getCodice().equals(req.getTipoFonteDurc()), 
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("tipo fonte durc", "- valori validi: '"
						+ TipoFonteDurc.MANUALE.getCodice() + "', '" + TipoFonteDurc.AUTOMATICA.getCodice() + "'"));
		checkCondition(StringUtils.isEmpty(req.getFonteDurcAutomatica()) || req.getFonteDurcClassifId() == null, 
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("fonte durc", "- fonteDurcClassifId e fonteDurcAutomatica sono mutuamente esclusivi"));
	}
	
	@Override
	protected void execute() {
		
		soggettoDad.aggiornaDatiDurcSoggetto(
				req.getIdSoggetto(),
				req.getDataFineValiditaDurc(), 
				req.getTipoFonteDurc(),
				req.getFonteDurcClassifId(), 
				req.getFonteDurcAutomatica(), 
				req.getNoteDurc(),
				loginOperazione);		
	}
	
}
