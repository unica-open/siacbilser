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
import it.csi.siac.siacbilser.business.service.predocumentospesa.AggiornaCausaleSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AggiornaPreDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AggiornaStatoPreDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AnnullaCausaleSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AnnullaPreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AssociaImputazioniContabiliPreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AssociaImputazioniContabiliVariatePreDocumentoSpesaAsyncService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AssociaImputazioniContabiliVariatePreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DefiniscePreDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DefiniscePreDocumentoSpesaAsyncService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DefiniscePreDocumentoSpesaPerElencoAsyncService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DefiniscePreDocumentoSpesaPerElencoService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DefiniscePreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DettaglioStoricoCausaleSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.InserisceCausaleSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.InseriscePreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.LeggiContiTesoreriaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.LeggiTipiCausaleSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.RicercaDettaglioCausaleSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.RicercaDettaglioPreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.RicercaPuntualePreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.RicercaSinteticaCausaleSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.RicercaSinteticaPreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.ValidaStatoOperativoPreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaPerElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaPerElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoSpesaResponse;

/**
 * Implementazione del web service PreDocumentoSpesaService.
 *
 * @author Domenico
 */
@WebService(serviceName = "PreDocumentoSpesaService", portName = "PreDocumentoSpesaServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService")
public class PreDocumentoSpesaServiceImpl implements PreDocumentoSpesaService {
	
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
	public InseriscePreDocumentoSpesaResponse inseriscePreDocumentoSpesa(InseriscePreDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, InseriscePreDocumentoSpesaService.class, parameters);
	}

	@Override
	public ValidaStatoOperativoPreDocumentoSpesaResponse validaStatoOperativoPreDocumentoSpesa(ValidaStatoOperativoPreDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, ValidaStatoOperativoPreDocumentoSpesaService.class, parameters);
	}

	@Override
	public RicercaPuntualePreDocumentoSpesaResponse ricercaPuntualePreDocumentoSpesa(RicercaPuntualePreDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualePreDocumentoSpesaService.class, parameters);
	}

	@Override
	public RicercaSinteticaPreDocumentoSpesaResponse ricercaSinteticaPreDocumentoSpesa(RicercaSinteticaPreDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaPreDocumentoSpesaService.class, parameters);
	}

	@Override
	public RicercaDettaglioPreDocumentoSpesaResponse ricercaDettaglioPreDocumentoSpesa(RicercaDettaglioPreDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioPreDocumentoSpesaService.class, parameters);
	}

	@Override
	public AnnullaPreDocumentoSpesaResponse annullaPreDocumentoSpesa(AnnullaPreDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaPreDocumentoSpesaService.class, parameters);
	}

	@Override
	public void associaImputazioniContabiliPreDocumentoSpesa(AssociaImputazioniContabiliPreDocumentoSpesa parameters) {
		appCtx.getBean(Utility.toDefaultBeanName(AssociaImputazioniContabiliPreDocumentoSpesaService.class), AssociaImputazioniContabiliPreDocumentoSpesaService.class).executeService(parameters);
	}

	@Override
	public AggiornaPreDocumentoDiSpesaResponse aggiornaPreDocumentoDiSpesa(AggiornaPreDocumentoDiSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaPreDocumentoDiSpesaService.class, parameters);
	}

	@Override
	@Deprecated
	public void definiscePreDocumentoDiSpesa(DefiniscePreDocumentoDiSpesa parameters) {
		appCtx.getBean(Utility.toDefaultBeanName(DefiniscePreDocumentoDiSpesaService.class), DefiniscePreDocumentoDiSpesaService.class).executeService(parameters);
	}

	@Override
	public InserisceCausaleSpesaResponse inserisceCausaleSpesa(InserisceCausaleSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceCausaleSpesaService.class, parameters);
	}

	@Override
	public AggiornaCausaleSpesaResponse aggiornaCausaleSpesa(AggiornaCausaleSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaCausaleSpesaService.class, parameters);
	}

	@Override
	public RicercaDettaglioCausaleSpesaResponse ricercaDettaglioCausaleSpesa(RicercaDettaglioCausaleSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioCausaleSpesaService.class, parameters);
	}

	@Override
	public RicercaSinteticaCausaleSpesaResponse ricercaSinteticaCausaleSpesa(RicercaSinteticaCausaleSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaCausaleSpesaService.class, parameters);
	}
	
	@Override
	public LeggiTipiCausaleSpesaResponse leggiTipiCausaleSpesa(LeggiTipiCausaleSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiTipiCausaleSpesaService.class, parameters);
	}

	@Override
	public AnnullaCausaleSpesaResponse annullaCausaleSpesa(AnnullaCausaleSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaCausaleSpesaService.class, parameters);
	}

	@Override
	public LeggiContiTesoreriaResponse leggiContiTesoreria(LeggiContiTesoreria parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiContiTesoreriaService.class, parameters);
	}

	@Override
	public AggiornaStatoPreDocumentoDiSpesaResponse aggiornaStatoPreDocumentoDiSpesa(AggiornaStatoPreDocumentoDiSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaStatoPreDocumentoDiSpesaService.class, parameters);
	}
	
	@Override
	public DettaglioStoricoCausaleSpesaResponse dettaglioStoricoCausaleSpesa(DettaglioStoricoCausaleSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, DettaglioStoricoCausaleSpesaService.class, parameters);
	}

	// CR-4280
	
	@Override
	public AssociaImputazioniContabiliVariatePreDocumentoSpesaResponse associaImputazioniContabiliVariatePreDocumentoSpesa(AssociaImputazioniContabiliVariatePreDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AssociaImputazioniContabiliVariatePreDocumentoSpesaService.class, parameters);
	}

	@Override
	public AsyncServiceResponse associaImputazioniContabiliVariatePreDocumentoSpesaAsync(AsyncServiceRequestWrapper<AssociaImputazioniContabiliVariatePreDocumentoSpesa> parameters) {
		return BaseServiceExecutor.execute(appCtx, AssociaImputazioniContabiliVariatePreDocumentoSpesaAsyncService.class, parameters);
	}

	// SIAC-5001
	@Override
	public DefiniscePreDocumentoSpesaResponse definiscePreDocumentoSpesa(DefiniscePreDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, DefiniscePreDocumentoSpesaService.class, parameters);
	}

	@Override
	public AsyncServiceResponse definiscePreDocumentoSpesaAsync(AsyncServiceRequestWrapper<DefiniscePreDocumentoSpesa> parameters) {
		return BaseServiceExecutor.execute(appCtx, DefiniscePreDocumentoSpesaAsyncService.class, parameters);
	}

	@Override
	public DefiniscePreDocumentoSpesaPerElencoResponse definiscePreDocumentoSpesaPerElenco(DefiniscePreDocumentoSpesaPerElenco parameters) {
		return BaseServiceExecutor.execute(appCtx, DefiniscePreDocumentoSpesaPerElencoService.class, parameters);
	}

	@Override
	public AsyncServiceResponse definiscePreDocumentoSpesaPerElencoAsync(AsyncServiceRequestWrapper<DefiniscePreDocumentoSpesaPerElenco> parameters) {
		return BaseServiceExecutor.execute(appCtx, DefiniscePreDocumentoSpesaPerElencoAsyncService.class, parameters);
	}
	
}
