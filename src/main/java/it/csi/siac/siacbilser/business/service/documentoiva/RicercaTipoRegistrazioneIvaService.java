/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoiva;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIvaResponse;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaTipoRegistrazioneIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTipoRegistrazioneIvaService extends CheckedAccountBaseService<RicercaTipoRegistrazioneIva, RicercaTipoRegistrazioneIvaResponse> {
	
	/** The documento iva dad. */
	@Autowired
	private DocumentoIvaDad documentoIvaDad;
	
	/** The ente. */
	private Ente ente;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		TipoRegistrazioneIva tipoRegistrazioneIva = req.getTipoRegistrazioneIva();
		checkNotNull(tipoRegistrazioneIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registrazione iva"));
		
		ente = tipoRegistrazioneIva.getEnte();
		checkNotNull(ente, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente tipo registrazione iva"));
		checkCondition(ente.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente tipo registrazione iva"));
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	@Override
	public RicercaTipoRegistrazioneIvaResponse executeService(RicercaTipoRegistrazioneIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoIvaDad.setEnte(ente);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<TipoRegistrazioneIva> listaTipoRegistrazioneIva = documentoIvaDad.ricercaTipoRegistrazioneIva(req.getTipoRegistrazioneIva());
		res.setListaTipoRegistrazioneIva(listaTipoRegistrazioneIva);
		res.setCardinalitaComplessiva(listaTipoRegistrazioneIva.size());
	}
	
}
