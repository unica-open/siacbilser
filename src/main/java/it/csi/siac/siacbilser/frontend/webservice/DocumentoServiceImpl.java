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
import it.csi.siac.siacbilser.business.service.documento.CollegaDocumentiService;
import it.csi.siac.siacbilser.business.service.documento.RicercaAttivitaOnereService;
import it.csi.siac.siacbilser.business.service.documento.RicercaCausale770Service;
import it.csi.siac.siacbilser.business.service.documento.RicercaCodiceBolloService;
import it.csi.siac.siacbilser.business.service.documento.RicercaCodicePCCService;
import it.csi.siac.siacbilser.business.service.documento.RicercaCodiceUfficioDestinatarioPCCService;
import it.csi.siac.siacbilser.business.service.documento.RicercaNaturaOnereService;
import it.csi.siac.siacbilser.business.service.documento.RicercaNoteTesoriereService;
import it.csi.siac.siacbilser.business.service.documento.RicercaQuoteDaAssociareService;
import it.csi.siac.siacbilser.business.service.documento.RicercaSommaNonSoggettaService;
import it.csi.siac.siacbilser.business.service.documento.RicercaTipoAvvisoService;
import it.csi.siac.siacbilser.business.service.documento.RicercaTipoDocumentoService;
import it.csi.siac.siacbilser.business.service.documento.RicercaTipoImpresaService;
import it.csi.siac.siacbilser.business.service.documento.RicercaTipoOnereService;
import it.csi.siac.siacbilser.business.service.documento.ScollegaDocumentiService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRelazioneDocumenti;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRelazioneDocumentiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770Response;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBollo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBolloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociare;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociareResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggetta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggettaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvviso;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvvisoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnereResponse;


/**
 * The Class DocumentoServiceImpl.
 */
@WebService(serviceName = "DocumentoService", portName = "DocumentoServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService")
public class DocumentoServiceImpl implements DocumentoService {
	
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
	public RicercaTipoDocumentoResponse ricercaTipoDocumento(RicercaTipoDocumento parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoDocumentoService.class, parameters);
	}

	@Override
	public RicercaCodiceBolloResponse ricercaCodiceBollo(RicercaCodiceBollo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaCodiceBolloService.class, parameters);
	}

	@Override
	public RicercaTipoImpresaResponse ricercaTipoImpresa(RicercaTipoImpresa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoImpresaService.class, parameters);
	}

	@Override
	public RicercaTipoAvvisoResponse ricercaTipoAvviso(RicercaTipoAvviso parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoAvvisoService.class, parameters);
	}

	@Override
	public RicercaNaturaOnereResponse ricercaNaturaOnere(RicercaNaturaOnere parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaNaturaOnereService.class, parameters);
	}

	@Override
	public RicercaTipoOnereResponse ricercaTipoOnere(RicercaTipoOnere parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoOnereService.class, parameters);
	}
	
	@Override
	public RicercaAttivitaOnereResponse ricercaAttivitaOnere(RicercaAttivitaOnere parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaAttivitaOnereService.class, parameters);
	}

	@Override
	public RicercaCausale770Response ricercaCausale770(RicercaCausale770 parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaCausale770Service.class, parameters);
	}
	
	@Override
	public RicercaSommaNonSoggettaResponse ricercaSommaNonSoggetta(RicercaSommaNonSoggetta parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSommaNonSoggettaService.class, parameters);
	}

	@Override
	public RicercaNoteTesoriereResponse ricercaNoteTesoriere(RicercaNoteTesoriere parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaNoteTesoriereService.class, parameters);
	}

	@Override
	public RicercaCodicePCCResponse ricercaCodicePCC(RicercaCodicePCC parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaCodicePCCService.class, parameters);
	}

	@Override
	public RicercaCodiceUfficioDestinatarioPCCResponse ricercaCodiceUfficioDestinatarioPCC(RicercaCodiceUfficioDestinatarioPCC parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaCodiceUfficioDestinatarioPCCService.class, parameters);
	}

	@Override
	public RicercaQuoteDaAssociareResponse ricercaQuoteDaAssociare(RicercaQuoteDaAssociare parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaQuoteDaAssociareService.class, parameters);
	}

	@Override
	public AggiornaRelazioneDocumentiResponse collegaDocumenti(AggiornaRelazioneDocumenti parameters) {
		return BaseServiceExecutor.execute(appCtx, CollegaDocumentiService.class, parameters);
	}

	@Override
	public AggiornaRelazioneDocumentiResponse scollegaDocumenti(AggiornaRelazioneDocumenti parameters) {
		return BaseServiceExecutor.execute(appCtx, ScollegaDocumentiService.class, parameters);
	}

}
