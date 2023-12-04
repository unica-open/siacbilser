/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.StampaIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaStampaIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;

/**
 * The Class RicercaRegistroIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaStampaIvaService extends CheckedAccountBaseService<RicercaStampaIva, RicercaStampaIvaResponse> {
	
	@Autowired
	private StampaIvaDad stampaIvaDad;
	
	private StampaIva stampaIva;
	private List<RegistroIva> listaRegistroIva;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		stampaIva= req.getStampaIva();
		checkNotNull(stampaIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stampa iva"));
		checkNotNull(stampaIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente stampa iva"));
		checkCondition(stampaIva.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente stampa iva"));
		
		listaRegistroIva = stampaIva.getListaRegistroIva();
		checkNotNull(listaRegistroIva,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista registro iva stampa iva"));
		checkCondition(!listaRegistroIva.isEmpty(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista registro iva stampa iva"));
		
		checkNotNull(stampaIva.getPeriodo(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("periodo stampa iva"));
		checkNotNull(stampaIva.getAnnoEsercizio(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio stampa iva"));
		checkNotNull(stampaIva.getTipoStampaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo stampa iva"));
	}	
	
	
	@Override
	protected void init() {
		stampaIvaDad.setLoginOperazione(loginOperazione);
		stampaIvaDad.setEnte(stampaIva.getEnte());
	}

	
	@Transactional(readOnly=true)
	@Override
	public RicercaStampaIvaResponse executeService(RicercaStampaIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		List<StampaIva> listaStampaIva = stampaIvaDad.ricercaStampaIva(stampaIva, listaRegistroIva.get(0));
		res.setListaStampaIva(listaStampaIva);
		res.setCardinalitaComplessiva(listaStampaIva.size());
	}

}
