/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CodifichePCCDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCCResponse;
import it.csi.siac.siacfin2ser.model.CodicePCC;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCodicePCCService extends CheckedAccountBaseService<RicercaCodicePCC, RicercaCodicePCCResponse> {
	
	@Autowired
	private CodifichePCCDad codifichePCCDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		for(StrutturaAmministrativoContabile sac : req.getStruttureAmministrativoContabili()) {
			checkEntita(sac, "Struttura amministrativo contabile");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public RicercaCodicePCCResponse executeService(RicercaCodicePCC serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		codifichePCCDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		List<CodicePCC> codiciPcc = codifichePCCDad.findCodiciPCCByEnteAndStrutturaAmministrativoContabile(req.getStruttureAmministrativoContabili(),req.getCodiceUfficioDestinatarioPCC());
		
		res.setCodiciPCC(codiciPcc);
		res.setCardinalitaComplessiva(codiciPcc.size());
	}

}
