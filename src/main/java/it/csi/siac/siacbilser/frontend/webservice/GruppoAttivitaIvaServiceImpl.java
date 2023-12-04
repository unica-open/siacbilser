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
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.AggiornaGruppoAttivitaIvaEProrataService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.AggiornaGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.AggiornaProrataEChiusuraGruppoIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.EliminaGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.InserisceGruppoAttivitaIvaEProrataService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.InserisceGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.InserisceProrataEChiusuraGruppoIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.RicercaDettaglioAnnualizzataGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.RicercaDettaglioGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.RicercaGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.RicercaSinteticaGruppoAttivitaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.GruppoAttivitaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIvaEProrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIvaEProrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaProrataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaProrataEChiusuraGruppoIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaEProrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaEProrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceProrataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceProrataEChiusuraGruppoIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAnnualizzataGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAnnualizzataGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaGruppoAttivitaIvaResponse;

/**
 * Implementazione del web service GruppoAttivitaIvaService.
 *
 * @author Alessandro Marchino
 */
@WebService(serviceName = "GruppoAttivitaIvaService", portName = "GruppoAttivitaIvaServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.GruppoAttivitaIvaService")
public class GruppoAttivitaIvaServiceImpl implements GruppoAttivitaIvaService {
	
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
	public RicercaGruppoAttivitaIvaResponse ricercaGruppoAttivitaIva(RicercaGruppoAttivitaIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaGruppoAttivitaIvaService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaGruppoAttivitaIvaResponse ricercaSinteticaGruppoAttivitaIva(RicercaSinteticaGruppoAttivitaIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaGruppoAttivitaIvaService.class, parameters);
	}

	@Override
	public RicercaDettaglioGruppoAttivitaIvaResponse ricercaDettaglioGruppoAttivitaIva(RicercaDettaglioGruppoAttivitaIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioGruppoAttivitaIvaService.class, parameters);
	}
	
	@Override
	public RicercaDettaglioAnnualizzataGruppoAttivitaIvaResponse ricercaDettaglioAnnualizzataGruppoAttivitaIva(RicercaDettaglioAnnualizzataGruppoAttivitaIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioAnnualizzataGruppoAttivitaIvaService.class, parameters);
	}
	
	@Override
	public InserisceGruppoAttivitaIvaResponse inserisceGruppoAttivitaIva(InserisceGruppoAttivitaIva parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceGruppoAttivitaIvaService.class, parameters);
	}
	
	@Override
	public InserisceProrataEChiusuraGruppoIvaResponse inserisceProrataEChiusuraGruppoIva(InserisceProrataEChiusuraGruppoIva parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceProrataEChiusuraGruppoIvaService.class, parameters);
	}
	
	@Override
	public InserisceGruppoAttivitaIvaEProrataResponse inserisceGruppoAttivitaIvaEProrata(InserisceGruppoAttivitaIvaEProrata parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceGruppoAttivitaIvaEProrataService.class, parameters);
	}

	@Override
	public AggiornaGruppoAttivitaIvaResponse aggiornaGruppoAttivitaIva(AggiornaGruppoAttivitaIva parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaGruppoAttivitaIvaService.class, parameters);
	}
	
	@Override
	public AggiornaProrataEChiusuraGruppoIvaResponse aggiornaProrataEChiusuraGruppoIva(AggiornaProrataEChiusuraGruppoIva parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaProrataEChiusuraGruppoIvaService.class, parameters);
	}
	
	@Override
	public AggiornaGruppoAttivitaIvaEProrataResponse aggiornaGruppoAttivitaIvaEProrata(AggiornaGruppoAttivitaIvaEProrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaGruppoAttivitaIvaEProrataService.class, parameters);
	}

	@Override
	public EliminaGruppoAttivitaIvaResponse eliminaGruppoAttivitaIva(EliminaGruppoAttivitaIva parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaGruppoAttivitaIvaService.class, parameters);
	}

}
