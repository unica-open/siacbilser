/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.TipoGiustificativoDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioTipoGiustificativoResponse;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * Inserimento dell'anagrafica del Documento di Entrata .
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioTipoGiustificativoService extends CheckedAccountBaseService<RicercaDettaglioTipoGiustificativo, RicercaDettaglioTipoGiustificativoResponse> {
		
	
	@Autowired
	private TipoGiustificativoDad tipoGiustificativoDad;
	private TipoGiustificativo tipoGiustificativo;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getTipoGiustificativo(), "tipo giustificativo");
		tipoGiustificativo = req.getTipoGiustificativo();
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		tipoGiustificativoDad.setEnte(ente);
		tipoGiustificativoDad.setLoginOperazione(loginOperazione);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaDettaglioTipoGiustificativoResponse executeService(RicercaDettaglioTipoGiustificativo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		TipoGiustificativo tipo = tipoGiustificativoDad.ricercaDettaglioTipoGiustificativo(tipoGiustificativo.getUid());
		if(tipo == null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("tipo giustificativo", "uid " + tipoGiustificativo.getUid()));
			res.setEsito(Esito.FALLIMENTO);
		}
		res.setTipoGiustificativo(tipo);
	}

	
	
}
