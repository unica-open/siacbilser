/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.StampeCassaFileDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoCassaStampato;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoCassaStampatoResponse;
import it.csi.siac.siaccecser.model.StampaRendiconto;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaUltimoRendicontoCassaStampatoService extends CheckedAccountBaseService<RicercaUltimoRendicontoCassaStampato, RicercaUltimoRendicontoCassaStampatoResponse> {
	
	@Autowired
	private StampeCassaFileDad stampeCassaFileDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getStampeCassaFile(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stampe Cassa File"));
		checkEntita(req.getStampeCassaFile().getCassaEconomale(), "cassa economale", false);
		checkNotNull(req.getStampeCassaFile().getTipoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo documento "), false);
		checkEntita(req.getStampeCassaFile().getBilancio(), "bilancio");
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaUltimoRendicontoCassaStampatoResponse executeService(RicercaUltimoRendicontoCassaStampato serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		stampeCassaFileDad.setEnte(ente);
		
	}
	
	@Override
	protected void execute() {
		StampaRendiconto stampaUltimo = stampeCassaFileDad.findStampaUltimoNumeroRendiconto(req.getStampeCassaFile());

		
		if (stampaUltimo == null) {
			return;
			
		}
		StampeCassaFile sf = new StampeCassaFile();
		sf.setStampaRendiconto(stampaUltimo);
		res.setStampeCassaFile(sf);
	}

}
