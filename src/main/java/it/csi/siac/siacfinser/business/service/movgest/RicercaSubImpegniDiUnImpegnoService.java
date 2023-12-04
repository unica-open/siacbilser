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
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.common.RicercaAttributiMovimentoGestioneOttimizzatoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubImpegniDiUnImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubImpegniDiUnImpegnoResponse;
import it.csi.siac.siacfinser.integration.dad.ImpegnoOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSubImpegniDiUnImpegnoService extends RicercaAttributiMovimentoGestioneOttimizzatoService<RicercaSubImpegniDiUnImpegno, RicercaSubImpegniDiUnImpegnoResponse> {

	@Autowired
	ImpegnoOttimizzatoDad impegnoOttimizzatoDad;
	
	@Override
	protected void init() {
		final String methodName = "RicercaSubImpegniDiUnImpegnoService : init()";
		log.debug(methodName, "- Begin");		
	}	

	@Override
	@Transactional(readOnly=true)
	public RicercaSubImpegniDiUnImpegnoResponse executeService(RicercaSubImpegniDiUnImpegno serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		
		long startUno = System.currentTimeMillis();
		
		final String methodName = "RicercaSubImpegniDiUnImpegnoService : execute()";
		log.debug(methodName, "- Begin");
		log.debug(methodName, "- req.getParametroRicercaImpegno() " + req.getpRicercaImpegnoK());
		
		
		//1. Leggiamo i dati ricevuti dalla request:
		RicercaImpegnoK ricercaImpegnoK = req.getpRicercaImpegnoK();
		Ente ente = req.getRichiedente().getAccount().getEnte();
		Integer idEnte = ente.getUid();
		
		String annoEsercizio = req.getpRicercaImpegnoK().getAnnoEsercizio().toString();
		Integer annoImpegno = req.getpRicercaImpegnoK().getAnnoImpegno();
		BigDecimal numeroImpegno = req.getpRicercaImpegnoK().getNumeroImpegno();
		Richiedente richiedente = req.getRichiedente();
		Impegno impegno = null;

		//2. Si invoca il metodo ricercaImpegni che ci restituisce il numero di risultati attesi dalla query composta
		//secondo i parametri di ricerca
		
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		paginazioneSubMovimentiDto.setDimensionePagina(req.getNumRisultatiPerPagina());
		paginazioneSubMovimentiDto.setNumeroPagina(req.getNumPagina());
		paginazioneSubMovimentiDto.setPaginazione(true);
		paginazioneSubMovimentiDto.setNoSub(false);
		paginazioneSubMovimentiDto.setEscludiSubAnnullati(req.isEscludiSubAnnullati());
		paginazioneSubMovimentiDto.setFiltroSubSoloInQuestoStato(req.getFiltroSubSoloInQuestoStato());
		paginazioneSubMovimentiDto.setPaginazioneSuDatiMinimi(req.isPaginazioneSuDatiMinimi());
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = req.getDatiOpzionaliElencoSubTuttiConSoloGliIds();
		
		EsitoRicercaMovimentoPkDto esito = impegnoOttimizzatoDad.ricercaMovimentoPk(
				richiedente, ente, annoEsercizio, annoImpegno, numeroImpegno, paginazioneSubMovimentiDto,
				caricaDatiOpzionaliDto, CostantiFin.MOVGEST_TIPO_IMPEGNO, true, req.isCaricalistaModificheCollegate());
	
		long endUno = System.currentTimeMillis();
		long startDue = System.currentTimeMillis();
		
		if(esito!=null && esito.getMovimentoGestione()!=null){
			impegno = ((Impegno) esito.getMovimentoGestione());
			if(impegno!=null){
				
				DatiOpzionaliCapitoli datiOpzionaliCapitoli = req.getDatiOpzionaliCapitoli();
				
				//set dei dati di paginazione sub:
				impostaDatiRespPaginazioneSub(esito);
				//
				
				//Imposto i sub trovati:
				res.setElencoSubImpegni(impegno.getElencoSubImpegni());
				//
				
				impegno = completaDatiImpegnoESubDopoRicercaMovimentoPk(impegno, richiedente, annoEsercizio, req.getEnte().getUid(),datiOpzionaliCapitoli);
				
				
				long endDue = System.currentTimeMillis();
				
				long totUno = endUno - startUno;
				long totDue = endDue - startDue;
				
				CommonUtil.println("totUno: " + totUno + " - totDue: " + totDue);
				
				//setto i dati della testata:
				res.setImpegno(impegno);
				
				res.setEsito(Esito.SUCCESSO);
			}
		}
			
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "RicercaSubImpegniDiUnImpegnoService : checkServiceParam()";

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
		
		if(req.getNumPagina()<=0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("NUMERO PAGINA DEVE ESSERE MAGGIORE DI ZERO"));
		} if(req.getNumRisultatiPerPagina()<=0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("NUMERO RISULTATI PER PAGINA DEVE ESSERE MAGGIORE DI ZERO"));
		}
	}	
	
	
}

