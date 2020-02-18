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
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaTipoGiustificativoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaTipoOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AnnullaCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AnnullaOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AnnullaTipoGiustificativoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AnnullaTipoOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.CalcolaDisponibilitaCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceTipoGiustificativoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceTipoOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioTipoGiustificativoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioTipoOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaModalitaPagamentoCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaSinteticaCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaSinteticaOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaSinteticaTipoGiustificativoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaSinteticaTipoOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaTipoOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaTipoRichiestaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.CECSvcDictionary;
import it.csi.siac.siaccecser.frontend.webservice.CassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaModalitaPagamentoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaModalitaPagamentoCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoRichiestaEconomaleResponse;


/**
 * The Class CassaEconomaleServiceImpl.
 */
@WebService(serviceName = "CassaEconomaleService", portName = "CassaEconomaleServicePort", 
targetNamespace = CECSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siaccecser.frontend.webservice.CassaEconomaleService")
public class CassaEconomaleServiceImpl implements CassaEconomaleService {

	
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
	public AggiornaCassaEconomaleResponse aggiornaCassaEconomale(AggiornaCassaEconomale parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaCassaEconomaleService.class, parameters);
	}

	@Override
	public AnnullaCassaEconomaleResponse annullaCassaEconomale(AnnullaCassaEconomale parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaCassaEconomaleService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaCassaEconomaleResponse ricercaSinteticaCassaEconomale(RicercaSinteticaCassaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaCassaEconomaleService.class, parameters);
	}
	
	@Override
	public RicercaDettaglioCassaEconomaleResponse ricercaDettaglioCassaEconomale(RicercaDettaglioCassaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioCassaEconomaleService.class, parameters);
	}
	
	@Override
	public InserisceTipoGiustificativoResponse inserisceTipoGiustificativo(InserisceTipoGiustificativo parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceTipoGiustificativoService.class, parameters);
	}

	@Override
	public AggiornaTipoGiustificativoResponse aggiornaTipoGiustificativo(AggiornaTipoGiustificativo parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaTipoGiustificativoService.class, parameters);
	}

	@Override
	public AnnullaTipoGiustificativoResponse annullaTipoGiustificativo(AnnullaTipoGiustificativo parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaTipoGiustificativoService.class, parameters);
	}
	
	@Override
	public CalcolaDisponibilitaCassaEconomaleResponse calcolaDisponibilitaCassaEconomale(CalcolaDisponibilitaCassaEconomale parameters) {
		return BaseServiceExecutor.execute(appCtx, CalcolaDisponibilitaCassaEconomaleService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaTipoGiustificativoResponse ricercaSinteticaTipoGiustificativo(RicercaSinteticaTipoGiustificativo parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaTipoGiustificativoService.class, parameters);
	}
	
	@Override
	public RicercaDettaglioTipoGiustificativoResponse ricercaDettaglioTipoGiustificativo(RicercaDettaglioTipoGiustificativo parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioTipoGiustificativoService.class, parameters);
	}

	@Override
	public InserisceOperazioneDiCassaResponse inserisceOperazioneDiCassa(InserisceOperazioneDiCassa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceOperazioneDiCassaService.class, parameters);
	}

	@Override
	public AggiornaOperazioneDiCassaResponse aggiornaOperazioneDiCassa(AggiornaOperazioneDiCassa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaOperazioneDiCassaService.class, parameters);
	}

	@Override
	public AnnullaOperazioneDiCassaResponse annullaOperazioneDiCassa(AnnullaOperazioneDiCassa parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaOperazioneDiCassaService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaOperazioneDiCassaResponse ricercaSinteticaOperazioneDiCassa(RicercaSinteticaOperazioneDiCassa parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaOperazioneDiCassaService.class, parameters);
	}
	
	@Override
	public RicercaDettaglioOperazioneDiCassaResponse ricercaDettaglioOperazioneDiCassa(RicercaDettaglioOperazioneDiCassa parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioOperazioneDiCassaService.class, parameters);
	}
	
	@Override
	public InserisceTipoOperazioneDiCassaResponse inserisceTipoOperazioneDiCassa(InserisceTipoOperazioneDiCassa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceTipoOperazioneDiCassaService.class, parameters);
	}

	@Override
	public AggiornaTipoOperazioneDiCassaResponse aggiornaTipoOperazioneDiCassa(AggiornaTipoOperazioneDiCassa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaTipoOperazioneDiCassaService.class, parameters);
	}

	@Override
	public AnnullaTipoOperazioneDiCassaResponse annullaTipoOperazioneDiCassa(AnnullaTipoOperazioneDiCassa parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaTipoOperazioneDiCassaService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaTipoOperazioneDiCassaResponse ricercaSinteticaTipoOperazioneDiCassa(RicercaSinteticaTipoOperazioneDiCassa parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaTipoOperazioneDiCassaService.class, parameters);
	}
	
	@Override
	public RicercaDettaglioTipoOperazioneDiCassaResponse ricercaDettaglioTipoOperazioneDiCassa(RicercaDettaglioTipoOperazioneDiCassa parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioTipoOperazioneDiCassaService.class, parameters);
	}

	@Override
	public RicercaTipoRichiestaEconomaleResponse ricercaTipoRichiestaEconomale(RicercaTipoRichiestaEconomale parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoRichiestaEconomaleService.class, parameters);
	}
	
	@Override
	public RicercaTipoOperazioneDiCassaResponse ricercaTipoOperazioneDiCassa(RicercaTipoOperazioneDiCassa parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaTipoOperazioneDiCassaService.class, parameters);
	}
	
	@Override
	public RicercaModalitaPagamentoCassaResponse ricercaModalitaPagamentoCassa(RicercaModalitaPagamentoCassa parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaModalitaPagamentoCassaService.class, parameters);
	}

}
