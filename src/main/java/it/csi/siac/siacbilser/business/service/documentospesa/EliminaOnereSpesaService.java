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
import it.csi.siac.siacbilser.integration.dad.OnereSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOnereSpesaResponse;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;

// TODO: Auto-generated Javadoc
/**
 * The Class EliminaOnereSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaOnereSpesaService extends CheckedAccountBaseService<EliminaOnereSpesa, EliminaOnereSpesaResponse> {
	
	/** The onere spesa dad. */
	@Autowired
	private OnereSpesaDad onereSpesaDad;
	
	/** The dettaglio onere. */
	private DettaglioOnere dettaglioOnere;

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		dettaglioOnere = req.getDettaglioOnere();
		
		checkNotNull(dettaglioOnere, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettaglio onere"));
		checkCondition(dettaglioOnere.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettaglio onere"));		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		onereSpesaDad.setLoginOperazione(loginOperazione);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public EliminaOnereSpesaResponse executeService(EliminaOnereSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkIvaSplitReverseOnere();
		onereSpesaDad.eliminaDettaglioOnere(dettaglioOnere);
		res.setDettaglioOnere(dettaglioOnere);
		
	}

	private void checkIvaSplitReverseOnere() {
		DettaglioOnere dettaglioOnereTrovato = onereSpesaDad.findDettaglioOnereById(dettaglioOnere.getUid());
		if(dettaglioOnereTrovato.getTipoOnere().getTipoIvaSplitReverse() == null ){
			return;
		}
		
		BigDecimal sommaImportiOneriStessoTipoSR = onereSpesaDad.ivaSplitReverseCheckConnection(dettaglioOnereTrovato);
		BigDecimal sommaImportiSplitreverseImportoSR = onereSpesaDad.ivaSplitReverseCheckConnectionImporto(dettaglioOnereTrovato);
		BigDecimal importoOnereDaEliminare = dettaglioOnereTrovato.getImportoCaricoSoggetto();
		
		if (sommaImportiOneriStessoTipoSR.subtract(importoOnereDaEliminare).compareTo(sommaImportiSplitreverseImportoSR) < 0){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("per l'onere split reverse ci sono degli oneri per cui sono stati calcolati gli importi iva"));
		}
	}

}
