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
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRelazioneAttivitaIvaCapitoloResponse;
import it.csi.siac.siacfin2ser.model.AttivitaIva;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceRelazioneAttivitaIvaCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceRelazioneAttivitaIvaCapitoloService extends CheckedAccountBaseService<InserisceRelazioneAttivitaIvaCapitolo, InserisceRelazioneAttivitaIvaCapitoloResponse> {
	
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
		
		// Controllo gli enti
		checkNotNull(attivitaIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente attivita Iva"));
		checkCondition(attivitaIva.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente attivita Iva"));
		checkNotNull(capitolo.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente capitolo"));
		checkCondition(capitolo.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente capitolo"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		attivitaIvaDad.setLoginOperazione(loginOperazione);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public InserisceRelazioneAttivitaIvaCapitoloResponse executeService(InserisceRelazioneAttivitaIvaCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkRelazioneGiaPresente();
		checkEnte();
		attivitaIvaDad.inserisciRelazioneAttivitaIvaCapitolo(attivitaIva, capitolo);
		List<AttivitaIva> listaAttivitaIva = attivitaIvaDad.ricercaAttivitaLegateAlCapitolo(capitolo);
		res.setListaAttivitaIva(listaAttivitaIva);
	}

	/**
	 * Check ente.
	 */
	private void checkEnte() {
		if(attivitaIva.getEnte().getUid() != capitolo.getEnte().getUid()){
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("ente attivita iva, ente capitolo"), Esito.FALLIMENTO);
		}
		
	}

	/**
	 * Check relazione gia presente.
	 */
	private void checkRelazioneGiaPresente() {
		if(attivitaIvaDad.verificaRelazioneAttivitaIvaCapitolo(attivitaIva, capitolo)){
			throw new BusinessException(ErroreCore.RELAZIONE_GIA_PRESENTE.getErrore(), Esito.FALLIMENTO);
		}
	}

}
