/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.TipoOnereDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.TipoOnere;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaTipoOnereService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioTipoOnereService extends CheckedAccountBaseService<RicercaDettaglioTipoOnere, RicercaDettaglioTipoOnereResponse>{
	
	private TipoOnere tipoOnere;
	
	@Autowired
	private TipoOnereDad tipoOnereDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		tipoOnere = req.getTipoOnere();
		checkEntita(tipoOnere, "tipo onere");
		tipoOnere.setEnte(ente);
	}
	
	@Override
	protected void init() {
		tipoOnereDad.setEnte(ente);
		tipoOnereDad.setLoginOperazione(loginOperazione);
		tipoOnereDad.setRichiedente(req.getRichiedente());
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public RicercaDettaglioTipoOnereResponse executeService(RicercaDettaglioTipoOnere serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		TipoOnere onere = tipoOnereDad.ricercaDettaglioTipoOnere(this.tipoOnere, req.getTipoOnereModelDetails());
		
		if (onere == null) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("tipo onere", "uid:" + this.tipoOnere.getUid()));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		res.setTipoOnere(onere);
		
	}
}
