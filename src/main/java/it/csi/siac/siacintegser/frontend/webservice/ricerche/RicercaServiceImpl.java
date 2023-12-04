/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.frontend.webservice.ricerche;
  

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.custom.oopp.business.service.ricerca.RicercaDocumentiSpesaOOPPService;
import it.csi.siac.custom.oopp.business.service.ricerca.RicercaEstesaMandatiOOPPService;
import it.csi.siac.custom.oopp.business.service.ricerca.RicercaMovimentiGestioneOOPPService;
import it.csi.siac.siacintegser.business.service.ricerche.capitolo.RicercaCapitoloEntrataGestioneService;
import it.csi.siac.siacintegser.business.service.ricerche.capitolo.RicercaCapitoloUscitaGestioneService;
import it.csi.siac.siacintegser.business.service.ricerche.documenti.RicercaDocumentoEntrataService;
import it.csi.siac.siacintegser.business.service.ricerche.documenti.RicercaDocumentoSpesaService;
import it.csi.siac.siacintegser.business.service.ricerche.liquidazione.RicercaEstesaLiquidazioniService;
import it.csi.siac.siacintegser.business.service.ricerche.liquidazione.RicercaLiquidazioneService;
import it.csi.siac.siacintegser.business.service.ricerche.movimentoGestione.RicercaAccertamentoService;
import it.csi.siac.siacintegser.business.service.ricerche.movimentoGestione.RicercaImpegnoService;
import it.csi.siac.siacintegser.business.service.ricerche.ordinativo.RicercaEstesaOrdinativiSpesaService;
import it.csi.siac.siacintegser.business.service.ricerche.ordinativo.RicercaOrdinativoEntrataService;
import it.csi.siac.siacintegser.business.service.ricerche.ordinativo.RicercaOrdinativoSpesaService;
import it.csi.siac.siacintegser.business.service.ricerche.provvisori.RicercaDeiProvvisoriDiCassaService;
import it.csi.siac.siacintegser.business.service.ricerche.soggetto.RicercaDettaglioSoggettoService;
import it.csi.siac.siacintegser.business.service.ricerche.soggetto.RicercaSinteticaSoggettiService;
import it.csi.siac.siacintegser.business.service.test.TestService;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.ricerca.RicercaDocumentiSpesaOOPP;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.ricerca.RicercaDocumentiSpesaOOPPResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.ricerca.RicercaEstesaMandatiOOPP;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.ricerca.RicercaEstesaMandatiOOPPResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.ricerca.RicercaMovimentiGestioneOOPP;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.ricerca.RicercaMovimentiGestioneOOPPResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.RicercaDocumentoEntrata;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.RicercaDocumentoEntrataResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.RicercaDocumentoSpesa;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.RicercaDocumentoSpesaResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.capitolo.RicercaCapitoloEntrataGestione;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.capitolo.RicercaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.capitolo.RicercaCapitoloUscitaGestione;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.capitolo.RicercaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.liquidazione.RicercaEstesaLiquidazioni;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.liquidazione.RicercaEstesaLiquidazioniResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.liquidazione.RicercaLiquidazione;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.liquidazione.RicercaLiquidazioneResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.movimentoGestione.RicercaAccertamento;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.movimentoGestione.RicercaAccertamentoResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.movimentoGestione.RicercaImpegno;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.movimentoGestione.RicercaImpegnoResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.ordinativo.RicercaEstesaOrdinativiSpesa;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.ordinativo.RicercaEstesaOrdinativiSpesaResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.ordinativo.RicercaOrdinativoIncasso;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.ordinativo.RicercaOrdinativoIncassoResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.ordinativo.RicercaOrdinativoSpesa;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.ordinativo.RicercaOrdinativoSpesaResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.provvisori.RicercaProvvisoriDiCassa;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.provvisori.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.soggetto.RicercaDettaglioSoggetti;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.soggetto.RicercaDettaglioSoggettiResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.soggetto.RicercaSinteticaSoggetti;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.soggetto.RicercaSinteticaSoggettiResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.test.Test;
import it.csi.siac.siacintegser.frontend.webservice.msg.test.TestResponse;

/**
 * The Class RicercaServiceImpl.
 */
@WebService(serviceName = "RicercaService", 
portName = "RicercaServicePort", 
targetNamespace = RicercheSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacintegser.frontend.webservice.ricerche.RicercaService")
public class RicercaServiceImpl implements RicercaService {

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
	@WebMethod
	public @WebResult TestResponse test(@WebParam Test request) {
		return appCtx.getBean(TestService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaSinteticaSoggettiResponse ricercaSinteticaSoggetti(@WebParam RicercaSinteticaSoggetti request)
	{		
		return appCtx.getBean(RicercaSinteticaSoggettiService.class).executeService(request);
	}

	
	@Override
	@WebMethod
	public @WebResult
	RicercaDettaglioSoggettiResponse ricercaDettaglioSoggetto(@WebParam RicercaDettaglioSoggetti request)
	{
		return appCtx.getBean(RicercaDettaglioSoggettoService.class).executeService(request);
	}



	@Override
	@WebMethod
	public @WebResult
	RicercaCapitoloUscitaGestioneResponse ricercaCapitoloUscitaGestione(
			@WebParam RicercaCapitoloUscitaGestione request)
	{

		return appCtx.getBean(RicercaCapitoloUscitaGestioneService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaCapitoloEntrataGestioneResponse ricercaCapitoloEntrataGestione(
			@WebParam RicercaCapitoloEntrataGestione request)
	{
		return appCtx.getBean(RicercaCapitoloEntrataGestioneService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaLiquidazioneResponse ricercaLiquidazione(
			@WebParam RicercaLiquidazione request) {
		return appCtx.getBean(RicercaLiquidazioneService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaOrdinativoSpesaResponse ricercaOrdinativoSpesa(
			@WebParam RicercaOrdinativoSpesa request) {
		return appCtx.getBean(RicercaOrdinativoSpesaService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaImpegnoResponse ricercaImpegno(
			@WebParam RicercaImpegno request) {
		return appCtx.getBean(RicercaImpegnoService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public @WebResult
	RicercaProvvisoriDiCassaResponse ricercaProvvisoriDiCassa(
			@WebParam RicercaProvvisoriDiCassa request) {
		return appCtx.getBean(RicercaDeiProvvisoriDiCassaService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaAccertamentoResponse ricercaAccertamento(
			@WebParam RicercaAccertamento request) {
		return appCtx.getBean(RicercaAccertamentoService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaOrdinativoIncassoResponse ricercaOrdinativoIncasso(
			@WebParam RicercaOrdinativoIncasso request) {
		return appCtx.getBean(RicercaOrdinativoEntrataService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaDocumentoSpesaResponse ricercaDocumentoSpesa(
			@WebParam RicercaDocumentoSpesa request) {
		return appCtx.getBean(RicercaDocumentoSpesaService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaDocumentoEntrataResponse ricercaDocumentoEntrata(
			@WebParam RicercaDocumentoEntrata request) {
		return appCtx.getBean(RicercaDocumentoEntrataService.class).executeService(request);
	}

	
	@Override
	public @WebResult
	RicercaEstesaLiquidazioniResponse ricercaEstesaLiquidazioni(
			@WebParam RicercaEstesaLiquidazioni request) {
		return appCtx.getBean(RicercaEstesaLiquidazioniService.class).executeService(request);
		
	}
	
	@Override
	public @WebResult
	RicercaEstesaOrdinativiSpesaResponse ricercaEstesaOrdinativiSpesa(
			@WebParam RicercaEstesaOrdinativiSpesa request) {
		return appCtx.getBean(RicercaEstesaOrdinativiSpesaService.class).executeService(request);
	}

	@Override
	public @WebResult RicercaMovimentiGestioneOOPPResponse ricercaMovimentiGestioneOOPP(@WebParam RicercaMovimentiGestioneOOPP request) {
		return appCtx.getBean(RicercaMovimentiGestioneOOPPService.class).executeService(request);
	}

	@Override
	public @WebResult RicercaEstesaMandatiOOPPResponse ricercaEstesaMandatiOOPP(@WebParam RicercaEstesaMandatiOOPP request) {
		return appCtx.getBean(RicercaEstesaMandatiOOPPService.class).executeService(request);
	}

	@Override
	public @WebResult RicercaDocumentiSpesaOOPPResponse ricercaDocumentiSpesaOOPP(@WebParam RicercaDocumentiSpesaOOPP request) {
		return appCtx.getBean(RicercaDocumentiSpesaOOPPService.class).executeService(request);
	}

}
