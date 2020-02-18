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
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AggiornaCausaleEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AggiornaDataTrasmissionePreDocumentoEntrataAsyncService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AggiornaDataTrasmissionePreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AggiornaPreDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AggiornaStatoPreDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AnnullaCausaleEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AnnullaPreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AssociaImputazioniContabiliPreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AssociaImputazioniContabiliVariatePreDocumentoEntrataAsyncService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AssociaImputazioniContabiliVariatePreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.CompletaDefiniscePreDocumentoEntrataAsyncService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.CompletaDefiniscePreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.DefiniscePreDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.DefiniscePreDocumentoEntrataAsyncService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.DefiniscePreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.DettaglioStoricoCausaleEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.InserisceCausaleEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.InseriscePreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.LeggiContiCorrenteService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.LeggiTipiCausaleEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.RicercaDettaglioCausaleEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.RicercaDettaglioPreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.RicercaPuntualePreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.RicercaSinteticaCausaleEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.RicercaSinteticaPreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.RicercaTotaliPreDocumentoEntrataPerStatoService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.ValidaStatoOperativoPreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDataTrasmissionePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDataTrasmissionePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiCorrente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiCorrenteResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStatoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoEntrataResponse;


/**
 * The Class PreDocumentoEntrataServiceImpl.
 */
@WebService(serviceName = "PreDocumentoEntrataService", portName = "PreDocumentoEntrataServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService")
public class PreDocumentoEntrataServiceImpl implements PreDocumentoEntrataService {

	
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

	@Override
	public InseriscePreDocumentoEntrataResponse inseriscePreDocumentoEntrata(InseriscePreDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, InseriscePreDocumentoEntrataService.class, parameters);
	}

	@Override
	public ValidaStatoOperativoPreDocumentoEntrataResponse validaStatoOperativoPreDocumentoEntrata(ValidaStatoOperativoPreDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, ValidaStatoOperativoPreDocumentoEntrataService.class, parameters);	
	}

	@Override
	public RicercaPuntualePreDocumentoEntrataResponse ricercaPuntualePreDocumentoEntrata(RicercaPuntualePreDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualePreDocumentoEntrataService.class, parameters);	
	}

	@Override
	public RicercaSinteticaPreDocumentoEntrataResponse ricercaSinteticaPreDocumentoEntrata(RicercaSinteticaPreDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaPreDocumentoEntrataService.class, parameters);
	}

	@Override
	public RicercaDettaglioPreDocumentoEntrataResponse ricercaDettaglioPreDocumentoEntrata(RicercaDettaglioPreDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioPreDocumentoEntrataService.class, parameters);
	}

	@Override
	public AnnullaPreDocumentoEntrataResponse annullaPreDocumentoEntrata(AnnullaPreDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaPreDocumentoEntrataService.class, parameters);
	}

	@Override
	public void associaImputazioniContabiliPreDocumentoEntrata(AssociaImputazioniContabiliPreDocumentoEntrata parameters) {
		//BaseServiceExecutor.execute(appCtx, AssociaImputazioniContabiliPreDocumentoEntrataService.class, parameters);
		appCtx.getBean(Utility.toDefaultBeanName(AssociaImputazioniContabiliPreDocumentoEntrataService.class), AssociaImputazioniContabiliPreDocumentoEntrataService.class).executeService(parameters);
	}

	@Override
	public AggiornaPreDocumentoDiEntrataResponse aggiornaPreDocumentoDiEntrata(AggiornaPreDocumentoDiEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaPreDocumentoDiEntrataService.class, parameters);
	}

	@Override
	public void definiscePreDocumentoDiEntrata(DefiniscePreDocumentoDiEntrata parameters) {
		//BaseServiceExecutor.execute(appCtx, DefiniscePreDocumentoDiEntrataService.class, parameters);
		appCtx.getBean(Utility.toDefaultBeanName(DefiniscePreDocumentoDiEntrataService.class), DefiniscePreDocumentoDiEntrataService.class).executeService(parameters);
	}

	@Override
	public InserisceCausaleEntrataResponse inserisceCausaleEntrata(InserisceCausaleEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceCausaleEntrataService.class, parameters);
	}

	@Override
	public AggiornaCausaleEntrataResponse aggiornaCausaleEntrata(AggiornaCausaleEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaCausaleEntrataService.class, parameters);
	}

	@Override
	public RicercaDettaglioCausaleEntrataResponse ricercaDettaglioCausaleEntrata(RicercaDettaglioCausaleEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioCausaleEntrataService.class, parameters);
	}

	@Override
	public RicercaSinteticaCausaleEntrataResponse ricercaSinteticaCausaleEntrata(RicercaSinteticaCausaleEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaCausaleEntrataService.class, parameters);
	}

	@Override
	public LeggiTipiCausaleEntrataResponse leggiTipiCausaleEntrata(LeggiTipiCausaleEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiTipiCausaleEntrataService.class, parameters);
	}

	@Override
	public AnnullaCausaleEntrataResponse annullaCausaleEntrata(AnnullaCausaleEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaCausaleEntrataService.class, parameters);
	}

	@Override
	public LeggiContiCorrenteResponse leggiContiCorrente(LeggiContiCorrente parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiContiCorrenteService.class, parameters);
	}

	@Override
	public AggiornaStatoPreDocumentoDiEntrataResponse aggiornaStatoPreDocumentoDiEntrata(AggiornaStatoPreDocumentoDiEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaStatoPreDocumentoDiEntrataService.class, parameters);
	}
	
	@Override
	public DettaglioStoricoCausaleEntrataResponse dettaglioStoricoCausaleEntrata(DettaglioStoricoCausaleEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, DettaglioStoricoCausaleEntrataService.class, parameters);
	}

	// CR-4280
	
	@Override
	public AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse associaImputazioniContabiliVariatePreDocumentoEntrata(AssociaImputazioniContabiliVariatePreDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AssociaImputazioniContabiliVariatePreDocumentoEntrataService.class, parameters);
	}

	@Override
	public AsyncServiceResponse associaImputazioniContabiliVariatePreDocumentoEntrataAsync(AsyncServiceRequestWrapper<AssociaImputazioniContabiliVariatePreDocumentoEntrata> parameters) {
		return BaseServiceExecutor.execute(appCtx, AssociaImputazioniContabiliVariatePreDocumentoEntrataAsyncService.class, parameters);
	}

	// SIAC-4383
	@Override
	public AggiornaDataTrasmissionePreDocumentoEntrataResponse aggiornaDataTrasmissionePreDocumentoEntrata(AggiornaDataTrasmissionePreDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaDataTrasmissionePreDocumentoEntrataService.class, parameters);
	}

	@Override
	public AsyncServiceResponse aggiornaDataTrasmissionePreDocumentoEntrataAsync(AsyncServiceRequestWrapper<AggiornaDataTrasmissionePreDocumentoEntrata> parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaDataTrasmissionePreDocumentoEntrataAsyncService.class, parameters);
	}

	// SIAC-5001
	@Override
	public DefiniscePreDocumentoEntrataResponse definiscePreDocumentoEntrata(DefiniscePreDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, DefiniscePreDocumentoEntrataService.class, parameters);
	}

	@Override
	public AsyncServiceResponse definiscePreDocumentoEntrataAsync(AsyncServiceRequestWrapper<DefiniscePreDocumentoEntrata> parameters) {
		return BaseServiceExecutor.execute(appCtx, DefiniscePreDocumentoEntrataAsyncService.class, parameters);
	}

	@Override
	public CompletaDefiniscePreDocumentoEntrataResponse completaDefiniscePreDocumentoEntrata(CompletaDefiniscePreDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, CompletaDefiniscePreDocumentoEntrataService.class, parameters);
	}

	@Override
	public AsyncServiceResponse completaDefiniscePreDocumentoEntrataAsync(AsyncServiceRequestWrapper<CompletaDefiniscePreDocumentoEntrata> parameters) {
		return BaseServiceExecutor.execute(appCtx, CompletaDefiniscePreDocumentoEntrataAsyncService.class, parameters);
	}

	@Override
	public RicercaTotaliPreDocumentoEntrataPerStatoResponse ricercaTotaliPreDocumentoEntrataPerStato(RicercaTotaliPreDocumentoEntrataPerStato parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTotaliPreDocumentoEntrataPerStatoService.class, parameters);
	}
	
}
