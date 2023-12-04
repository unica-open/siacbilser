/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.StampaIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIvaResponse;
import it.csi.siac.siacfin2ser.model.StampaIva;

/**
 * The Class RicercaSinteticaStampaIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaStampaIvaService extends CheckedAccountBaseService<RicercaSinteticaStampaIva, RicercaSinteticaStampaIvaResponse> {
	
	@Autowired
	private StampaIvaDad stampaIvaDad;
	
	private StampaIva stampaIva;
//	private List<RegistroIva> listaRegistroIva;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getStampaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stampa iva"));
		stampaIva= req.getStampaIva();
		stampaIva.setEnte(ente);
		
		checkParametriPaginazione(req.getParametriPaginazione());
	}	
	
	
	@Override
	protected void init() {
		stampaIvaDad.setLoginOperazione(loginOperazione);
		stampaIvaDad.setEnte(ente);
	}

	
	@Transactional(readOnly=true)
	@Override
	public RicercaSinteticaStampaIvaResponse executeService(RicercaSinteticaStampaIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		ListaPaginata<StampaIva> listaStampaIva = stampaIvaDad.ricercaSinteticaStampaIva(stampaIva,
				stampaIva.getListaRegistroIva()!=null && !stampaIva.getListaRegistroIva().isEmpty()?stampaIva.getListaRegistroIva().get(0): null, 
				req.getNomeFile(), 
				req.getParametriPaginazione());
		
		res.setStampeIva(listaStampaIva);
		res.setCardinalitaComplessiva(listaStampaIva.size());
	}

}
