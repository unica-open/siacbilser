/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.common.RicercaAttributiMovimentoGestioneService;
import it.csi.siac.siacfinser.business.service.util.Utility;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Impegno;


@Deprecated
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaImpegnoPerChiaveService extends RicercaAttributiMovimentoGestioneService<RicercaImpegnoPerChiave, RicercaImpegnoPerChiaveResponse> {
	

	@Override
	protected void init() {
		final String methodName = "RicercaImpegnoPerChiaveService : init()";
		log.debug(methodName, "- Begin");
	}	
	
	@Override
	public void execute() {
		final String methodName = "RicercaImpegnoPerChiaveService : execute()";
		log.debug(methodName, "- Begin");
		log.debug(methodName, "- req.getpRicercaImpegnoK() " + req.getpRicercaImpegnoK());
		
		//1. Vengono letti i valori ricevuti in input dalla request
		String annoEsercizio = req.getpRicercaImpegnoK().getAnnoEsercizio().toString();
		Integer annoImpegno = req.getpRicercaImpegnoK().getAnnoImpegno();
		BigDecimal numeroImpegno = req.getpRicercaImpegnoK().getNumeroImpegno();
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Impegno impegno = null;

		//2. Si richiama il metodo interno di ricerca per chiave per impegni o accertamenti:
		impegno = (Impegno) impegnoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio, annoImpegno, numeroImpegno, CostantiFin.MOVGEST_TIPO_IMPEGNO, true);
		
		if(null!=impegno){
			//si invoca il metodo completaDatiRicercaImpegnoPk che si occupa di vestire i dati ottenuti:
			impegno = completaDatiRicercaImpegnoPk(richiedente, impegno, annoEsercizio);
			


			//componiamo la respose esito positivo:
			res.setImpegno(impegno);
			res.setBilancio(impegno.getCapitoloUscitaGestione().getBilancio());
			res.setCapitoloUscitaGestione(impegno.getCapitoloUscitaGestione());
			res.setEsito(Esito.SUCCESSO);
			Utility.logXmlTypeObject(res, "WAWA");
		} else {
			//componiamo la respose esito negativo:
			res.setBilancio(null);
			res.setCapitoloUscitaGestione(null);
			res.setImpegno(null);
			res.setEsito(Esito.FALLIMENTO);
		}
	}
	
	
	
	@Override
	@Transactional(timeout=360, readOnly=true)
	public RicercaImpegnoPerChiaveResponse executeService(RicercaImpegnoPerChiave serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "RicercaImpegnoPerChiaveService : checkServiceParam()";
	
		//dati di input presi da request:
		Integer annoEsercizio = req.getpRicercaImpegnoK().getAnnoEsercizio();
		Integer annoImpegno = req.getpRicercaImpegnoK().getAnnoImpegno();
		BigDecimal numeroImpegno = req.getpRicercaImpegnoK().getNumeroImpegno();
		Ente ente = req.getRichiedente().getAccount().getEnte();

		if(null==req.getpRicercaImpegnoK()){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("RICERCA_IMPEGNO_K"));			
		} else if(null==annoEsercizio && null==annoImpegno && null==numeroImpegno && null==ente){
			checkCondition(false, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN PARAMETRO INIZIALIZZATO"));
		}  else if(null==annoEsercizio){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_ESERCIZIO"));
		} else if(null==annoImpegno){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_IMPEGNO"));
		} else if(null==numeroImpegno){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("NUMERO_IMPEGNO"));
		} else if(null==ente){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		}
	}	
	
}
