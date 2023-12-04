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
import it.csi.siac.siacbilser.business.service.classificatorebil.ContaClassificatoriERestituisciSeSingoloService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriBilByIdFiglioService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriBilByIdPadreService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriByRelazioneService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriByTipoElementoBilService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriByTipologieClassificatoriService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriGenericiByTipoElementoBilService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiElementoPianoDeiContiByCodiceAndAnnoService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiTreePianoDeiContiService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiTreeSiopeEntrataService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiTreeSiopeSpesaService;
import it.csi.siac.siacbilser.business.service.classificatorebil.RicercaPuntualeClassificatoreService;
import it.csi.siac.siacbilser.business.service.classificatorebil.RicercaSinteticaClassificatoreService;
import it.csi.siac.siacbilser.business.service.classificatorebil.RicercaTipoClassificatoreGenericoService;
import it.csi.siac.siacbilser.business.service.classificatorebil.RicercaTipoClassificatoreService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaClassificatoriERestituisciSeSingolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaClassificatoriERestituisciSeSingoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglio;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatori;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatoriResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnnoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiope;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiopeResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeClassificatoreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatoreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenerico;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenericoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreResponse;

/**
 * Implementazione del servizio ClassificatoreBilService, i metodi esposti sono
 * propri del modulo BIL.
 *
 * @author rmontuori
 * @version $Id: $
 */
@WebService(serviceName = "ClassificatoreBilService", portName = "ClassificatoreBilServicePort", targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService")
public class ClassificatoreBilServiceImpl implements ClassificatoreBilService {

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
	public LeggiClassificatoriGenericiByTipoElementoBilResponse leggiClassificatoriGenericiByTipoElementoBil(LeggiClassificatoriGenericiByTipoElementoBil parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiClassificatoriGenericiByTipoElementoBilService.class, parameters);
	}

	@Override
	public LeggiClassificatoriByTipoElementoBilResponse leggiClassificatoriByTipoElementoBil(LeggiClassificatoriByTipoElementoBil parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiClassificatoriByTipoElementoBilService.class, parameters);
	}
	
	@Override
	public LeggiTreePianoDeiContiResponse leggiTreePianoDeiConti(LeggiTreePianoDeiConti parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiTreePianoDeiContiService.class, parameters);
	}

	@Override
	public LeggiClassificatoriBilByIdPadreResponse leggiClassificatoriByIdPadre(LeggiClassificatoriBilByIdPadre parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiClassificatoriBilByIdPadreService.class, parameters);
	}
	
	@Override
	public LeggiClassificatoriBilByIdFiglioResponse leggiClassificatoriByIdFiglio(LeggiClassificatoriBilByIdFiglio parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiClassificatoriBilByIdFiglioService.class, parameters);
	}

	@Override
	public LeggiTreeSiopeResponse leggiTreeSiopeSpesa(LeggiTreeSiope parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiTreeSiopeSpesaService.class, parameters);
	}
	
	@Override
	public LeggiTreeSiopeResponse leggiTreeSiopeEntrata(LeggiTreeSiope parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiTreeSiopeEntrataService.class, parameters);
	}

	@Override
	public LeggiClassificatoriByRelazioneResponse leggiClassificatoriByRelazione(LeggiClassificatoriByRelazione request){
		return BaseServiceExecutor.execute(appCtx, LeggiClassificatoriByRelazioneService.class, request);
	}

	@Override
	public LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse leggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno request) {
		return BaseServiceExecutor.execute(appCtx, LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoService.class, request);
	}

	@Override
	public LeggiElementoPianoDeiContiByCodiceAndAnnoResponse leggiElementoPianoDeiContiByCodiceAndAnno(LeggiElementoPianoDeiContiByCodiceAndAnno request) {
		return BaseServiceExecutor.execute(appCtx, LeggiElementoPianoDeiContiByCodiceAndAnnoService.class, request);
	}

	@Override
	public RicercaSinteticaClassificatoreResponse ricercaSinteticaClassificatore(RicercaSinteticaClassificatore request) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaClassificatoreService.class, request);
	}

	@Override
	public ContaClassificatoriERestituisciSeSingoloResponse contaClassificatoriERestituisciSeSingolo(ContaClassificatoriERestituisciSeSingolo request) {
		return BaseServiceExecutor.execute(appCtx, ContaClassificatoriERestituisciSeSingoloService.class, request);
	}

	@Override
	public LeggiClassificatoriByTipologieClassificatoriResponse leggiClassificatoriByTipologieClassificatori(LeggiClassificatoriByTipologieClassificatori request) {
		return BaseServiceExecutor.execute(appCtx, LeggiClassificatoriByTipologieClassificatoriService.class, request);
	}

	@Override
	public RicercaPuntualeClassificatoreResponse ricercaPuntualeClassificatore(RicercaPuntualeClassificatore request) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeClassificatoreService.class, request);
	}

	@Override
	public RicercaTipoClassificatoreResponse ricercaTipoClassificatore(RicercaTipoClassificatore request) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoClassificatoreService.class, request);
	}

	@Override
	public RicercaTipoClassificatoreGenericoResponse ricercaTipoClassificatoreGenerico(RicercaTipoClassificatoreGenerico request) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoClassificatoreGenericoService.class, request);
	}

}
