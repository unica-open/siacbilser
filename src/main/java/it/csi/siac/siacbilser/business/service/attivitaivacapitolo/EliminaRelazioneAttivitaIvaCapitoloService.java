/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.attivitaivacapitolo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AttivitaIvaDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRelazioneAttivitaIvaCapitoloResponse;
import it.csi.siac.siacfin2ser.model.AttivitaIva;

/**
 * The Class EliminaRelazioneAttivitaIvaCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaRelazioneAttivitaIvaCapitoloService extends CheckedAccountBaseService<EliminaRelazioneAttivitaIvaCapitolo, EliminaRelazioneAttivitaIvaCapitoloResponse> {
	
	/** The attivita iva dad. */
	@Autowired
	private AttivitaIvaDad attivitaIvaDad;
	
	/** The attivita iva. */
	private AttivitaIva attivitaIva;
	
	/** The capitolo. */
	@SuppressWarnings("rawtypes")
	private Capitolo capitolo;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		attivitaIva = req.getAttivitaIva();
		capitolo = req.getCapitolo();
		
		checkNotNull(attivitaIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("attivita Iva"));
		checkCondition(attivitaIva.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid attivita Iva"));
		checkNotNull(capitolo, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"));
		checkCondition(capitolo.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo"));
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public EliminaRelazioneAttivitaIvaCapitoloResponse executeService(EliminaRelazioneAttivitaIvaCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		attivitaIvaDad.eliminaRelazioneAttivitaIvaCapitolo(attivitaIva, capitolo);
		List<AttivitaIva> listaAttivitaIva = attivitaIvaDad.ricercaAttivitaLegateAlCapitolo(capitolo);
		res.setListaAttivitaIva(listaAttivitaIva);
	}
	
}
