/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.helper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.business.service.RicercaStrutturaAmministrativaService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStrutturaAmministrativa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStrutturaAmministrativaResponse;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StrutturaAmministrativoContabileServiceHelper extends IntegServiceHelper {

	public StrutturaAmministrativoContabile findStrutturaAmministrativoContabileByCodice(Ente ente, Richiedente richiedente, String codiceStruttura, String codiceTipoStruttura) {
		
		StrutturaAmministrativoContabile sac = null; 
		
		RicercaStrutturaAmministrativa reqSac = new RicercaStrutturaAmministrativa();
		reqSac.setEnte(ente);
		reqSac.setRichiedente(richiedente);
		reqSac.setCodice(codiceStruttura);
		reqSac.setTipoCodice(codiceTipoStruttura);
			
		RicercaStrutturaAmministrativaResponse resSac = appCtx.getBean(RicercaStrutturaAmministrativaService.class).executeService(
				reqSac);

		checkServiceResponse(resSac);

		if(resSac!=null && resSac.getStrutturaAmministrativoContabile()!=null)
			sac =  resSac.getStrutturaAmministrativoContabile();
		
		
		return sac;
	}
	
	public StrutturaAmministrativoContabile findCdr(Ente ente, Richiedente richiedente, String codice, String codiceTipoStruttura) {
		
		StrutturaAmministrativoContabile sac = null; 
		
		RicercaStrutturaAmministrativa reqSac = new RicercaStrutturaAmministrativa();
		reqSac.setEnte(ente);
		reqSac.setRichiedente(richiedente);
		reqSac.setCodice(codice);
		reqSac.setTipoCodice(codiceTipoStruttura);
		reqSac.setRicercaCdr(true);	
		
		RicercaStrutturaAmministrativaResponse resSac = appCtx.getBean(RicercaStrutturaAmministrativaService.class).executeService(
				reqSac);

		checkServiceResponse(resSac);

		if(resSac!=null && resSac.getStrutturaAmministrativoContabile()!=null)
			sac =  resSac.getStrutturaAmministrativoContabile();
				
		return sac;
	}
}
