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
import it.csi.siac.siacbilser.business.service.consultazioneentita.OttieniNavigazioneTipoEntitaConsultabileService;
import it.csi.siac.siacbilser.business.service.consultazioneentita.RicercaFigliEntitaConsultabileService;
import it.csi.siac.siacbilser.business.service.consultazioneentita.RicercaSinteticaEntitaConsultabileService;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.ConsultazioneEntitaService;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.OttieniNavigazioneTipoEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.OttieniNavigazioneTipoEntitaConsultabileResponse;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaFigliEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaFigliEntitaConsultabileResponse;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabileResponse;

/**
 * Implementazione del servizio ConsultazioneEntitaService.
 *
 * @author Domenico Liis
 */
@WebService(serviceName = "ConsultazioneEntitaService", portName = "ConsultazioneEntitaServicePort", targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacconsultazioneentitaser.frontend.webservice.ConsultazioneEntitaService")
public class ConsultazioneEntitaServiceImpl implements ConsultazioneEntitaService {

	/** The app ctx. */
	@Autowired
	private ApplicationContext appCtx;

	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public OttieniNavigazioneTipoEntitaConsultabileResponse ottieniNavigazioneTipoEntitaConsultabile(
			OttieniNavigazioneTipoEntitaConsultabile parameters) {
		return BaseServiceExecutor.execute(appCtx, OttieniNavigazioneTipoEntitaConsultabileService.class, parameters);
	}

	@Override
	public RicercaFigliEntitaConsultabileResponse ricercaFigliEntitaConsultabile(RicercaFigliEntitaConsultabile parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaFigliEntitaConsultabileService.class, parameters);
	}

	@Override
	public RicercaSinteticaEntitaConsultabileResponse ricercaSinteticaEntitaConsultabile(RicercaSinteticaEntitaConsultabile parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaEntitaConsultabileService.class, parameters);
	}



}
