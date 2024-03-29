/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.TipoGiustificativoDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * Inserimento dell'anagrafica del Documento di Entrata .
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTipoGiustificativoService extends CheckedAccountBaseService<RicercaTipoGiustificativo, RicercaTipoGiustificativoResponse> {
		
	@Autowired
	private TipoGiustificativoDad tipoGiustificativoDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getTipologiaGiustificativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipologia giustificativo"), false);
		checkEntita(req.getCassaEconomale(), "cassa economale", false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		tipoGiustificativoDad.setEnte(ente);
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaTipoGiustificativoResponse executeService(RicercaTipoGiustificativo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<TipoGiustificativo> tipiGiustificativo = tipoGiustificativoDad.ricercaTipoGiustificativoByTipologiaAndCassa(req.getTipologiaGiustificativo(), req.getCassaEconomale());
		res.setTipiGiustificativi(tipiGiustificativo);
		res.setCardinalitaComplessiva(tipiGiustificativo.size());
	}

	
	
}
