/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.liquidazione;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioneConAllegatoAtto;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioneConAllegatoAttoResponse;
import it.csi.siac.siacfinser.integration.dad.LiquidazioneDad;
import it.csi.siac.siacfinser.model.errore.ErroreFin;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaLiquidazioneConAllegatoAttoService extends AbstractBaseService<RicercaLiquidazioneConAllegatoAtto, RicercaLiquidazioneConAllegatoAttoResponse> {

	@Autowired
	LiquidazioneDad liquidazioneDad;
	
	final String methodName = "RicercaLiquidazioneConAllegatoAttoService";
	
	@Override
	protected void init() {
		log.debug(methodName, ": init - Begin");
	}

	
//	@Override
//	@Transactional(readOnly=true)
//	public RicercaLiquidazioneConAllegatoAttoResponse executeService(RicercaLiquidazioneConAllegatoAtto serviceRequest) {
//		return super.executeService(serviceRequest);
//	}	
	
		
	@Override
	public void execute() {
				
		log.debug(methodName, ": execute - Begin");
		
		boolean liquidazioneDerivataDaAllegatoAtto = liquidazioneDad.checkLiquidazioneDaAllegatoAtto(req.getLiquidazione().getAnnoLiquidazione(), req.getLiquidazione().getNumeroLiquidazione(), req.getAnnoEsercizio(),req.getEnte(), req.getRichiedente());
		
		if (liquidazioneDerivataDaAllegatoAtto) {
			
			List<Errore> listaErrori = new ArrayList<Errore>();
			listaErrori.add(ErroreFin.ANNULLAMENTO_LIQUIDAZIONE_DERIVATA_DA_ALLEGATOA_ATTO.getErrore(""));
			res.setErrori(listaErrori);
			res.setEsito(Esito.FALLIMENTO);
			
		}
		
		log.debug(methodName, ": execute - End");
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		log.debug(methodName, ": checkServiceParam - Begin");
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		
		checkNotNull(req.getAnnoEsercizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno Esercizio"));
		
		checkNotNull(req.getLiquidazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("liquidazione"));
		
		log.debug(methodName, ": checkServiceParam - End");
		
	}	
}
