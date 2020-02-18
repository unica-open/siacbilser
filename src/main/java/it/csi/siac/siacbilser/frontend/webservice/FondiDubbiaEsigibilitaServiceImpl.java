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
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.AggiornaFondoDubbiaEsigibilitaRendicontoService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.AggiornaFondoDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.ControllaFondiDubbiaEsigibilitaAnnoPrecedenteService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrenteService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedenteService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.EliminaFondoDubbiaEsigibilitaRendicontoService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.EliminaFondoDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.InserisceFondiDubbiaEsigibilitaRendicontoService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.InserisceFondiDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteAsyncService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteAsyncService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrenteAsyncService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteAsyncService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.RicercaSinteticaFondiDubbiaEsigibilitaRendicontoService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.RicercaSinteticaFondiDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrenteResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;

/**
 * Implementazione del servizio FondiDubbiaEsigibilitaService.
 *
 * @author Alessandro Marchino
 */
@WebService(serviceName = "FondiDubbiaEsigibilitaService",
portName = "FondiDubbiaEsigibilitaServicePort",
targetNamespace = BILSvcDictionary.NAMESPACE,
endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.FondiDubbiaEsigibilitaService")
public class FondiDubbiaEsigibilitaServiceImpl implements FondiDubbiaEsigibilitaService {
	
	@Autowired
	private ApplicationContext appCtx;

	/**
	 * Inizializzazione della classe
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public InserisceFondiDubbiaEsigibilitaResponse inserisceFondiDubbiaEsigibilita(InserisceFondiDubbiaEsigibilita parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceFondiDubbiaEsigibilitaService.class, parameters);
	}

	@Override
	public AggiornaFondoDubbiaEsigibilitaResponse aggiornaFondoDubbiaEsigibilita(AggiornaFondoDubbiaEsigibilita parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaFondoDubbiaEsigibilitaService.class, parameters);
	}

	@Override
	public EliminaFondoDubbiaEsigibilitaResponse eliminaFondoDubbiaEsigibilita(EliminaFondoDubbiaEsigibilita parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaFondoDubbiaEsigibilitaService.class, parameters);
	}

	@Override
	public RicercaSinteticaFondiDubbiaEsigibilitaResponse ricercaSinteticaFondiDubbiaEsigibilita(RicercaSinteticaFondiDubbiaEsigibilita parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaFondiDubbiaEsigibilitaService.class, parameters);
	}

	@Override
	public PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteResponse popolaFondiDubbiaEsigibilitaDaAnnoPrecedente(PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente parameters) {
		return BaseServiceExecutor.execute(appCtx, PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteService.class, parameters);
	}

	@Override
	public AsyncServiceResponse popolaFondiDubbiaEsigibilitaDaAnnoPrecedenteAsync(AsyncServiceRequestWrapper<PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente> parameters) {
		return BaseServiceExecutor.execute(appCtx, PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteAsyncService.class, parameters);
	}

	// SIAC-4422

	@Override
	public InserisceFondiDubbiaEsigibilitaRendicontoResponse inserisceFondiDubbiaEsigibilitaRendiconto(InserisceFondiDubbiaEsigibilitaRendiconto parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceFondiDubbiaEsigibilitaRendicontoService.class, parameters);
	}

	@Override
	public AggiornaFondoDubbiaEsigibilitaRendicontoResponse aggiornaFondoDubbiaEsigibilitaRendiconto(AggiornaFondoDubbiaEsigibilitaRendiconto parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaFondoDubbiaEsigibilitaRendicontoService.class, parameters);
	}

	@Override
	public EliminaFondoDubbiaEsigibilitaRendicontoResponse eliminaFondoDubbiaEsigibilitaRendiconto(EliminaFondoDubbiaEsigibilitaRendiconto parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaFondoDubbiaEsigibilitaRendicontoService.class, parameters);
	}

	@Override
	public RicercaSinteticaFondiDubbiaEsigibilitaRendicontoResponse ricercaSinteticaFondiDubbiaEsigibilitaRendiconto(RicercaSinteticaFondiDubbiaEsigibilitaRendiconto parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaFondiDubbiaEsigibilitaRendicontoService.class, parameters);
	}

	@Override
	public PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteResponse popolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente(PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente parameters) {
		return BaseServiceExecutor.execute(appCtx, PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteService.class, parameters);
	}

	@Override
	public AsyncServiceResponse popolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteAsync(AsyncServiceRequestWrapper<PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente> parameters) {
		return BaseServiceExecutor.execute(appCtx, PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteAsyncService.class, parameters);
	}

	@Override
	public AsyncServiceResponse popolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrenteAsync(AsyncServiceRequestWrapper<PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente> parameters) {
		return BaseServiceExecutor.execute(appCtx, PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrenteAsyncService.class, parameters);
	}

	@Override
	public ControllaFondiDubbiaEsigibilitaAnnoPrecedenteResponse controllaFondiDubbiaEsigibilitaAnnoPrecedente(ControllaFondiDubbiaEsigibilitaAnnoPrecedente parameters) {
		return BaseServiceExecutor.execute(appCtx, ControllaFondiDubbiaEsigibilitaAnnoPrecedenteService.class, parameters);
	}

	@Override
	public ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedenteResponse controllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedente(ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedente parameters) {
		return BaseServiceExecutor.execute(appCtx, ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedenteService.class, parameters);
	}

	@Override
	public ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrenteResponse controllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente(ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente parameters) {
		return BaseServiceExecutor.execute(appCtx, ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrenteService.class, parameters);
	}

	@Override
	public PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteResponse popolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente(PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente parameters) {
		return BaseServiceExecutor.execute(appCtx, PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteService.class, parameters);
	}

	@Override
	public AsyncServiceResponse popolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteAsync(AsyncServiceRequestWrapper<PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente> parameters) {
		return BaseServiceExecutor.execute(appCtx, PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteAsyncService.class, parameters);
	}

}
