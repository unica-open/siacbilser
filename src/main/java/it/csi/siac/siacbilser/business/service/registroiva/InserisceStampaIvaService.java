/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.StampaIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaIvaResponse;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;


/**
 * The Class InserisceRegistroIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceStampaIvaService extends CheckedAccountBaseService<InserisceStampaIva, InserisceStampaIvaResponse> {
	
	@Autowired
	private StampaIvaDad stampaIvaDad;
	
	private StampaIva stampaIva;
	private List<RegistroIva> listaRegistroIva;
	private Periodo periodo;
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		stampaIva = req.getStampaIva();
		checkNotNull(stampaIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stampa iva"));
		checkNotNull(stampaIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente stampa iva"));
		checkCondition(stampaIva.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente stampa iva"));
		checkNotNull(StringUtils.trimToNull(stampaIva.getCodice()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stampa iva"));
		listaRegistroIva = stampaIva.getListaRegistroIva();
		checkNotNull(listaRegistroIva,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista registro iva stampa iva"));
		checkCondition(!listaRegistroIva.isEmpty(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista registro iva stampa iva"));
		periodo = stampaIva.getPeriodo();
		checkNotNull(periodo,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("periodo stampa iva"));
		checkNotNull(stampaIva.getAnnoEsercizio(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio stampa iva"));
		checkNotNull(stampaIva.getFlagStampaDefinitivo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag stampa definitiva"));
		checkNotNull(stampaIva.getFlagStampaProvvisorio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag stampa provvisorio"));
		checkNotNull(stampaIva.getTipoStampa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo stampa"));
		checkNotNull(stampaIva.getTipoStampaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo stampa iva"));
		
	}


	@Override
	protected void init() {
		stampaIvaDad.setLoginOperazione(loginOperazione);
		stampaIvaDad.setEnte(stampaIva.getEnte());
	}
	
	
	@Transactional
	@Override
	public InserisceStampaIvaResponse executeService(InserisceStampaIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		stampaIvaDad.inserisciStampaIva(stampaIva);
		res.setStampaIva(stampaIva);
	
	}


}