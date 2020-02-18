/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.business.service.progetto.AggiornaAnagraficaCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.AggiornaAnagraficaProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.AggiornaRigaEntrataService;
import it.csi.siac.siacbilser.business.service.progetto.AggiornaRigaSpesaService;
import it.csi.siac.siacbilser.business.service.progetto.AnnullaCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.AnnullaProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.CalcoloDeltaTraCronoprogrammiService;
import it.csi.siac.siacbilser.business.service.progetto.CalcoloFondoPluriennaleVincolatoComplessivoService;
import it.csi.siac.siacbilser.business.service.progetto.CalcoloFondoPluriennaleVincolatoCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.CalcoloFondoPluriennaleVincolatoEntrataService;
import it.csi.siac.siacbilser.business.service.progetto.CalcoloFondoPluriennaleVincolatoSpesaService;
import it.csi.siac.siacbilser.business.service.progetto.CalcoloProspettoRiassuntivoCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.CambiaFlagUsatoPerFpvCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.CancellaRigaEntrataService;
import it.csi.siac.siacbilser.business.service.progetto.CancellaRigaSpesaService;
import it.csi.siac.siacbilser.business.service.progetto.InserisceAnagraficaProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.InserisceCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.InserisceRigaEntrataService;
import it.csi.siac.siacbilser.business.service.progetto.InserisceRigaSpesaService;
import it.csi.siac.siacbilser.business.service.progetto.OttieniFondoPluriennaleVincolatoCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.PreparazioneCronoprogrammaDiGestioneService;
import it.csi.siac.siacbilser.business.service.progetto.RiattivaProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaDeiCronoprogrammiCollegatiAlProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaDeiCronoprogrammiCollegatiAlProvvedimentoService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaDettaglioCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaDettaglioProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaPuntualeProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaSinteticaProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaTipiAmbitoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDeltaTraCronoprogrammi;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDeltaTraCronoprogrammiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloProspettoRiassuntivoCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.OttieniFondoPluriennaleVincolatoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.OttieniFondoPluriennaleVincolatoCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.PreparazioneCronoprogrammaDiGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.PreparazioneCronoprogrammaDiGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RiattivaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RiattivaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProvvedimento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProvvedimentoResponse;

/**
 * Implementazione del servizio ProgettoService.
 *
 * @author Alessandro Marchino, Domenico Lisi,Ahmad Nazha 
 * @version 1.0.0 - 04/02/2013
 */
@WebService(serviceName = "ProgettoService", portName = "ProgettoServicePort", targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.ProgettoService")
public class ProgettoServiceImpl implements ProgettoService {
	
	/** The app ctx. */
	@Autowired
	private ApplicationContext appCtx;

	/**
	 * Inits the.
	 */
	@PostConstruct 
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#inserisceAnagraficaProgetto(it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgetto)
	 */
	@Override
	public InserisceAnagraficaProgettoResponse inserisceAnagraficaProgetto(InserisceAnagraficaProgetto parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceAnagraficaProgettoService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#aggiornaAnagraficaProgetto(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgetto)
	 */
	@Override
	public AggiornaAnagraficaProgettoResponse aggiornaAnagraficaProgetto(AggiornaAnagraficaProgetto parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaAnagraficaProgettoService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#ricercaPuntualeProgetto(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgetto)
	 */
	@Override
	public RicercaPuntualeProgettoResponse ricercaPuntualeProgetto(RicercaPuntualeProgetto parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeProgettoService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#ricercaSinteticaProgetto(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto)
	 */
	@Override
	public RicercaSinteticaProgettoResponse ricercaSinteticaProgetto(RicercaSinteticaProgetto parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaProgettoService.class, parameters);  
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#ricercaDettaglioProgetto(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto)
	 */
	@Override
	public RicercaDettaglioProgettoResponse ricercaDettaglioProgetto(RicercaDettaglioProgetto parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioProgettoService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#annullaProgetto(it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaProgetto)
	 */
	@Override
	public AnnullaProgettoResponse annullaProgetto(AnnullaProgetto parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaProgettoService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#riattivaProgetto(it.csi.siac.siacbilser.frontend.webservice.msg.RiattivaProgetto)
	 */
	@Override
	public RiattivaProgettoResponse riattivaProgetto(RiattivaProgetto parameters) {
		return BaseServiceExecutor.execute(appCtx, RiattivaProgettoService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#inserisceCronoprogramma(it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCronoprogramma)
	 */
	@Override
	public InserisceCronoprogrammaResponse inserisceCronoprogramma(InserisceCronoprogramma parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceCronoprogrammaService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#ricercaDeiCronoprogrammiCollegatiAlProgetto(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgetto)
	 */
	@Override
	public RicercaDeiCronoprogrammiCollegatiAlProgettoResponse ricercaDeiCronoprogrammiCollegatiAlProgetto(RicercaDeiCronoprogrammiCollegatiAlProgetto parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDeiCronoprogrammiCollegatiAlProgettoService.class, parameters);
	}

	@Override
	public RicercaDeiCronoprogrammiCollegatiAlProvvedimentoResponse ricercaDeiCronoprogrammiCollegatiAlProvvedimento(RicercaDeiCronoprogrammiCollegatiAlProvvedimento parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDeiCronoprogrammiCollegatiAlProvvedimentoService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#ricercaCronoprogramma(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCronoprogramma)
	 */
	@Override
	public RicercaCronoprogrammaResponse ricercaCronoprogramma(RicercaCronoprogramma parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaCronoprogrammaService.class, parameters);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#ricercaDettaglioCronoprogramma(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma)
	 */
	@Override
	public RicercaDettaglioCronoprogrammaResponse ricercaDettaglioCronoprogramma(RicercaDettaglioCronoprogramma parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioCronoprogrammaService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#aggiornaAnagraficaCronoprogramma(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaCronoprogramma)
	 */
	@Override
	public AggiornaAnagraficaCronoprogrammaResponse aggiornaAnagraficaCronoprogramma(AggiornaAnagraficaCronoprogramma parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaAnagraficaCronoprogrammaService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#annullaCronoprogramma(it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCronoprogramma)
	 */
	@Override
	public AnnullaCronoprogrammaResponse annullaCronoprogramma(AnnullaCronoprogramma parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaCronoprogrammaService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#aggiornaRigaEntrata(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaEntrata)
	 */
	@Override
	public AggiornaRigaEntrataResponse aggiornaRigaEntrata(AggiornaRigaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaRigaEntrataService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#inserisceRigaEntrata(it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaEntrata)
	 */
	@Override
	public InserisceRigaEntrataResponse inserisceRigaEntrata(InserisceRigaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceRigaEntrataService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#cancellaRigaEntrata(it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaEntrata)
	 */
	@Override
	public CancellaRigaEntrataResponse cancellaRigaEntrata(CancellaRigaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, CancellaRigaEntrataService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#aggiornaRigaSpesa(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaSpesa)
	 */
	@Override
	public AggiornaRigaSpesaResponse aggiornaRigaSpesa(AggiornaRigaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaRigaSpesaService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#inserisceRigaSpesa(it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaSpesa)
	 */
	@Override
	public InserisceRigaSpesaResponse inserisceRigaSpesa(InserisceRigaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceRigaSpesaService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#cancellaRigaSpesa(it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaSpesa)
	 */
	@Override
	public CancellaRigaSpesaResponse cancellaRigaSpesa(CancellaRigaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, CancellaRigaSpesaService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#calcoloProspettoRiassuntivoCronoprogramma(it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloProspettoRiassuntivoCronoprogramma)
	 */
	@Override
	public CalcoloProspettoRiassuntivoCronoprogrammaResponse calcoloProspettoRiassuntivoCronoprogramma(CalcoloProspettoRiassuntivoCronoprogramma parameters) {
		return BaseServiceExecutor.execute(appCtx, CalcoloProspettoRiassuntivoCronoprogrammaService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#calcoloDeltaTraCronoprogrammi(it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDeltaTraCronoprogrammi)
	 */
	@Override
	public CalcoloDeltaTraCronoprogrammiResponse calcoloDeltaTraCronoprogrammi(CalcoloDeltaTraCronoprogrammi parameters) {
		return BaseServiceExecutor.execute(appCtx, CalcoloDeltaTraCronoprogrammiService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#preparazioneCronoprogrammaDiGestione(it.csi.siac.siacbilser.frontend.webservice.msg.PreparazioneCronoprogrammaDiGestione)
	 */
	@Override
	public PreparazioneCronoprogrammaDiGestioneResponse preparazioneCronoprogrammaDiGestione(PreparazioneCronoprogrammaDiGestione parameters) {
		return BaseServiceExecutor.execute(appCtx, PreparazioneCronoprogrammaDiGestioneService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#ricercaTipiAmbito(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito)
	 */
	@Override
	public RicercaTipiAmbitoResponse ricercaTipiAmbito(RicercaTipiAmbito parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipiAmbitoService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.ProgettoService#calcoloFondoPluriennaleVincolatoCronoprogramma(it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoCronoprogramma)
	 */
	@Override
	public CalcoloFondoPluriennaleVincolatoCronoprogrammaResponse calcoloFondoPluriennaleVincolatoCronoprogramma(CalcoloFondoPluriennaleVincolatoCronoprogramma parameters) {	
		return  BaseServiceExecutor.execute(appCtx, CalcoloFondoPluriennaleVincolatoCronoprogrammaService.class, parameters);
	}
	
	
	@Override
	public CalcoloFondoPluriennaleVincolatoComplessivoResponse calcoloFondoPluriennaleVincolatoComplessivo(
			CalcoloFondoPluriennaleVincolatoComplessivo parameters) {
		return  BaseServiceExecutor.execute(appCtx, CalcoloFondoPluriennaleVincolatoComplessivoService.class, parameters);
	}

	@Override
	public CalcoloFondoPluriennaleVincolatoEntrataResponse calcoloFondoPluriennaleVincolatoEntrata(CalcoloFondoPluriennaleVincolatoEntrata parameters) {
		return  BaseServiceExecutor.execute(appCtx, CalcoloFondoPluriennaleVincolatoEntrataService.class, parameters);
	}

	@Override
	public CalcoloFondoPluriennaleVincolatoSpesaResponse calcoloFondoPluriennaleVincolatoSpesa(CalcoloFondoPluriennaleVincolatoSpesa parameters) {
		return  BaseServiceExecutor.execute(appCtx, CalcoloFondoPluriennaleVincolatoSpesaService.class, parameters);
	}

	@Override
	public CambiaFlagUsatoPerFpvCronoprogrammaResponse cambiaFlagUsatoPerFpvCronoprogramma(CambiaFlagUsatoPerFpvCronoprogramma parameters) {
		return BaseServiceExecutor.execute(appCtx, CambiaFlagUsatoPerFpvCronoprogrammaService.class, parameters);
	}

	@Override
	public OttieniFondoPluriennaleVincolatoCronoprogrammaResponse ottieniFondoPluriennaleVincolatoCronoprogramma(OttieniFondoPluriennaleVincolatoCronoprogramma parameters) {
		
		return BaseServiceExecutor.execute(appCtx, OttieniFondoPluriennaleVincolatoCronoprogrammaService.class, parameters);
	}

	@Override
	public RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioResponse ricercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio(RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioService.class, parameters);
	}

	

}
