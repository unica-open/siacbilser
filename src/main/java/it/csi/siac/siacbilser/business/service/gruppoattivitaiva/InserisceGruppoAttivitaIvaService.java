/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.gruppoattivitaiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.GruppoAttivitaIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceGruppoAttivitaIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceGruppoAttivitaIvaService extends CheckedAccountBaseService<InserisceGruppoAttivitaIva, InserisceGruppoAttivitaIvaResponse> {

	/** The gruppo attivita iva dad. */
	@Autowired
	private GruppoAttivitaIvaDad gruppoAttivitaIvaDad;
	
	/** The gruppo. */
	private GruppoAttivitaIva gruppo;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getGruppoAttivitaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("gruppo attivita Iva"));
		gruppo = req.getGruppoAttivitaIva();
		
		checkNotNull(gruppo.getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice gruppo Iva"), false);
		checkNotNull(gruppo.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione gruppo Iva"), false);
		checkNotNull(gruppo.getTipoChiusura(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo chiusura gruppo Iva"), false);
		checkNotNull(gruppo.getAnnualizzazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annualizzazione gruppo Iva"), false);
		
		checkEntita(gruppo.getEnte(), "ente gruppo", false);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		gruppoAttivitaIvaDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public InserisceGruppoAttivitaIvaResponse executeService(InserisceGruppoAttivitaIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkGruppoGiaEsistente();
		gruppoAttivitaIvaDad.inserisciGruppoAttivitaIva(gruppo);
		res.setGruppoAttivitaIva(gruppo);
	}

	/**
	 * Check gruppo gia esistente.
	 */
	private void checkGruppoGiaEsistente() {
		GruppoAttivitaIva gr= gruppoAttivitaIvaDad.findGruppoAttivitaIvaByCodice(gruppo.getCodice());
		if (gr!=null){
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("gruppo attivita' iva ", gr.getCodice()), Esito.FALLIMENTO);
		}
	}

}
