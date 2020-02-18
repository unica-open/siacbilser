/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.dad.OnereSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOnereSpesaResponse;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;

/**
 * The Class AggiornaOnereSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaOnereSpesaService extends CheckedAccountBaseService<AggiornaOnereSpesa, AggiornaOnereSpesaResponse> {
	
	/** The onere spesa dad. */
	@Autowired
	private OnereSpesaDad onereSpesaDad;
	/** The documento dad */
	@Autowired
	private DocumentoDad documentoDad;
	
	/** The dettaglio onere. */
	private DettaglioOnere dettaglioOnere;

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		dettaglioOnere = req.getDettaglioOnere();
		
		checkEntita(dettaglioOnere, "dettaglio onere");
		checkEntita(dettaglioOnere.getEnte(), "ente dettaglio onere");
		checkEntita(dettaglioOnere.getTipoOnere(), "tipo onere");
		checkEntita(dettaglioOnere.getDocumentoSpesa(), "documento spesa");
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		onereSpesaDad.setLoginOperazione(loginOperazione);
		onereSpesaDad.setEnte(ente);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaOnereSpesaResponse executeService(AggiornaOnereSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		// SIAC-6099: l'imponibile non deve essere superiore all'importo del documento
		checkImportoOnere();
		onereSpesaDad.aggiornaAnagraficaDettaglioOnere(dettaglioOnere);
		res.setDettaglioOnere(dettaglioOnere);
	}
	
	/**
	 * SIAC-6099: si richiede di inserire un controllo bloccante che verifichi che l'importo specificato nel campo Imponibile sia minore o uguale all'importo del documento.
	 */
	private void checkImportoOnere() {
		BigDecimal importoDocumento = documentoDad.findImportoDocumento(dettaglioOnere.getDocumentoSpesa());
//		BigDecimal importoOneriGiaAssociati = onereSpesaDad.getImportoImponibileOneriCollegatiAlDocumento(dettaglioOnere.getDocumentoSpesa().getUid(), dettaglioOnere.getUid());
		
		// SIAC-6136: l'importo di cui tenere in considerazione e' quello dell'onere
		BigDecimal imponibile = dettaglioOnere.getImportoImponibile() == null ? BigDecimal.ZERO : dettaglioOnere.getImportoImponibile();
		
		if(imponibile.compareTo(importoDocumento) > 0) {
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("imponibile", "non puo' essere maggiore dell'importo del documento ("
					+ "importo documento: " + Utility.formatCurrencyAsString(importoDocumento)
					+ ", imponibile onere: " + Utility.formatCurrencyAsString(imponibile) + ")"));
		}
	}

}