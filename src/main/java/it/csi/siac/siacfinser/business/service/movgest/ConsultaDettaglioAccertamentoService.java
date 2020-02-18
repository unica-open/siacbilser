/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaDettaglioAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaDettaglioAccertamentoResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.AccertamentoDettaglioImporti;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConsultaDettaglioAccertamentoService extends AbstractBaseService<ConsultaDettaglioAccertamento, ConsultaDettaglioAccertamentoResponse>{

	@Autowired
	CommonDad commonDad;
	
	@Override
	protected void init() {
		final String methodName = "ConsultaDettaglioAccertamentoService : init()";
		log.debug(methodName, "- Begin");		

	}	

	@Override
	@Transactional
	public ConsultaDettaglioAccertamentoResponse executeService(ConsultaDettaglioAccertamento serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	public void execute() {
		final String methodName = "ConsultaDettaglioAccertamentoService : execute()";
		log.debug(methodName, "- Begin");
		
		Ente ente = req.getEnte();
		Richiedente richiedente = req.getRichiedente();
		Integer annoBilancio = req.getAnnoEsercizio();
		String annoBilancioString = annoBilancio.toString();
		Integer annoMovimento = req.getAnnoMovimento();
		BigDecimal numeroMovimento = req.getNumeroMovimento();
		BigDecimal numSub = req.getNumeroSub();
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, richiedente, Operazione.RICERCA, annoBilancio);
				
		AccertamentoDettaglioImporti datiConsulta = accertamentoOttimizzatoDad.consultaDettaglioAccertamento(richiedente, ente, annoBilancioString, annoMovimento, numeroMovimento,numSub,datiOperazione);
		
		res.setAcertamentoDettaglioImporti(datiConsulta);
		res.setEsito(Esito.SUCCESSO);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		
		log.debug("", " heckServiceParam - Begin");
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		
		checkNotNull(req.getAnnoEsercizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"));
		
		checkNotNull(req.getAnnoMovimento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno movimento"));
		
		checkNotNull(req.getNumeroMovimento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero movimento"));
		
		checkNotNull(req.getRichiedente(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiedente"));
		
		
		log.debug("", "checkServiceParam - End");
		
	}	

}
