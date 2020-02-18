/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.OttieniFondoPluriennaleVincolatoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.OttieniFondoPluriennaleVincolatoCronoprogrammaResponse;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoEntrata;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoUscitaCronoprogramma;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class OttieniFondoPluriennaleVincolatoCronoprogrammaService.
 * Questo servizio permette di ottenere l'fpv di entrata e spesa cosi' come calcolato sul db tramite function
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OttieniFondoPluriennaleVincolatoCronoprogrammaService extends CheckedAccountBaseService<OttieniFondoPluriennaleVincolatoCronoprogramma, OttieniFondoPluriennaleVincolatoCronoprogrammaResponse> {
	
	
	/** The cronoprogramma. */
	private Cronoprogramma cronoprogramma;
	
	
	/** The ricerca dettaglio cronoprogramma service. */
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//parametri del cronoprogramma
		cronoprogramma  = req.getCronoprogramma();

		checkNotNull(cronoprogramma, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cronoprogramma"));
		checkCondition(cronoprogramma.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid cronoprogramma"));
		
		//aggiunti in data 17_07_2015
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno bilancio"));
	}
	
	@Override
	@Transactional(readOnly=true)
	public OttieniFondoPluriennaleVincolatoCronoprogrammaResponse executeService(OttieniFondoPluriennaleVincolatoCronoprogramma serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		// TODO: SIAC-4103, 4153: al momento blocco la generazione dell'FPV se il crono e' da definire
		checkDaDefinire();
		
//		popolaDettaglioCronoprogramma();
//		
//		caricaAnnoMinimoRigheCronoprogramma();
		
		calcolaFondoPluriennaleVincolatoUscita();
		calcolaFondoPluriennaleVincolatoEntrata();
		logFPVUscita(methodName, res.getListaFondoPluriennaleVincolatoUscitaCronoprogramma());
		logFPVEntrata(methodName, res.getListaFondoPluriennaleVincolatoEntrata());
	}

	private void checkDaDefinire() {
		boolean isDaDefinire = cronoprogrammaDad.isDaDefinire(cronoprogramma);
		if(isDaDefinire) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il cronoprogramma e' da definire, il calcolo dell'FPV non e' disponibile"));
		}
	}
	
	/**
	 * Calcola fondo pluriennale vincolato uscita.
	 */
	private void calcolaFondoPluriennaleVincolatoUscita() {
		List<FondoPluriennaleVincolatoUscitaCronoprogramma> listaFondoPluriennaleVincolatoUscita = cronoprogrammaDad.calcoloFpvSpesaPrevisione(req.getCronoprogramma(), req.getBilancio().getAnno(),req.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio());
		res.setListaFondoPluriennaleVincolatoUscitaCronoprogramma(listaFondoPluriennaleVincolatoUscita);
	}
	
	/**
	 * Calcola fondo pluriennale vincolato entrata.
	 */
	private void calcolaFondoPluriennaleVincolatoEntrata() {
		List<FondoPluriennaleVincolatoEntrata> listaFondoPluriennaleVincolatoEntrata = cronoprogrammaDad.calcoloFpvEntrataPrevisione(req.getCronoprogramma(), req.getBilancio().getAnno(),req.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio());
		res.setListaFondoPluriennaleVincolatoEntrata(listaFondoPluriennaleVincolatoEntrata);
	}


	
	/**
	 * Log list.
	 *
	 * @param <T> the generic type
	 * @param methodName the method name
	 * @param message the message
	 * @param list the list
	 */
	private <T> void logList(String methodName, String message, List<T> list) {
		if(log.isDebugEnabled()) {	
			StringBuilder sb = new StringBuilder();
			sb.append(message).append(": ");
			
			if(list!=null){
				for(T o : list) {
					sb.append("\n\t").append(o);
				}
			}
			
			log.debug(methodName, sb.toString());
		}
	}

	/**
	 * Log fpv uscita.
	 *
	 * @param methodName the method name
	 * @param detts the detts
	 */
	private void logFPVUscita(String methodName, List<FondoPluriennaleVincolatoUscitaCronoprogramma> detts) {
		logList(methodName, "FpvUscita", detts);
	}
	
	/**
	 * Log fpv entrata.
	 *
	 * @param methodName the method name
	 * @param detts the detts
	 */
	private void logFPVEntrata(String methodName, List<FondoPluriennaleVincolatoEntrata> detts) {
		logList(methodName, "FpvEntrata", detts);
	}	

}
