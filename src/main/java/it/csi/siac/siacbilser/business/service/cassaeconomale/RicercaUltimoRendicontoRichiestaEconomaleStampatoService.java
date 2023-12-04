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
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoRichiestaEconomaleStampato;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoRichiestaEconomaleStampatoResponse;
import it.csi.siac.siaccecser.model.StampaRendiconto;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaUltimoRendicontoRichiestaEconomaleStampatoService extends CheckedAccountBaseService<RicercaUltimoRendicontoRichiestaEconomaleStampato, RicercaUltimoRendicontoRichiestaEconomaleStampatoResponse> {
	
	@Autowired
	private StampeCassaFileDad stampeCassaFileDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getRichiestaEconomale(), "Richiesta economale");
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaUltimoRendicontoRichiestaEconomaleStampatoResponse executeService(RicercaUltimoRendicontoRichiestaEconomaleStampato serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		stampeCassaFileDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		StampaRendiconto stampaUltimo = stampeCassaFileDad.findStampaUltimoNumeroRendiconto(req.getRichiestaEconomale());

		if (stampaUltimo == null) {
			return;
		}
		StampeCassaFile sf = new StampeCassaFile();
		sf.setStampaRendiconto(stampaUltimo);
		res.setStampeCassaFile(sf);
	}

}
