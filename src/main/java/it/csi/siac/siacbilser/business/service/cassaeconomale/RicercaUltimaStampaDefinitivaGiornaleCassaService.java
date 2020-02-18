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
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimaStampaDefinitivaGiornaleCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimaStampaDefinitivaGiornaleCassaResponse;
import it.csi.siac.siaccecser.model.StampaGiornale;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaUltimaStampaDefinitivaGiornaleCassaService extends CheckedAccountBaseService<RicercaUltimaStampaDefinitivaGiornaleCassa, RicercaUltimaStampaDefinitivaGiornaleCassaResponse> {
	@Autowired
	private StampeCassaFileDad stampeCassaFileDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getStampeCassaFile(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stampe Cassa File"));
		checkEntita(req.getStampeCassaFile().getCassaEconomale(), "cassa economale");
		checkEntita(req.getStampeCassaFile().getBilancio(), "bilancio");
		checkNotNull(req.getStampeCassaFile().getTipoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo documento "));
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaUltimaStampaDefinitivaGiornaleCassaResponse executeService(RicercaUltimaStampaDefinitivaGiornaleCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		stampeCassaFileDad.setEnte(ente);
		
	}
	
	@Override
	protected void execute() {
		StampaGiornale stampaUltimaDef = stampeCassaFileDad.findUltimaStampaDefinitivaGiornaleCassa(req.getStampeCassaFile());

		
		if (stampaUltimaDef == null) {
			return;
			
		}
		StampeCassaFile sf = new StampeCassaFile();
		sf.setStampaGiornale(stampaUltimaDef);
		res.setStampeCassaFile(sf);
	}

}
