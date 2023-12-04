/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.gruppoattivitaiva;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.GruppoAttivitaIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAnnualizzataGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAnnualizzataGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioGruppoAttivitaIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioAnnualizzataGruppoAttivitaIvaService extends CheckedAccountBaseService<RicercaDettaglioAnnualizzataGruppoAttivitaIva, RicercaDettaglioAnnualizzataGruppoAttivitaIvaResponse> {
	
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
		checkEntita(req.getGruppoAttivitaIva(), "gruppo");
		gruppo = req.getGruppoAttivitaIva();
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioAnnualizzataGruppoAttivitaIvaResponse executeService(RicercaDettaglioAnnualizzataGruppoAttivitaIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<GruppoAttivitaIva> gruppiAttivitaIva = new ArrayList<GruppoAttivitaIva>();
		
		// Ottengo le annualizzazioni
		List<Integer> annualizzazioni = gruppoAttivitaIvaDad.findAnnualizzazioniById(gruppo.getUid());
		// Verifico di averne trovate
		if(annualizzazioni == null || annualizzazioni.isEmpty()) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("annualizzazioni per il gruppo " + gruppo.getUid()));
		}
		// Per ogni annualizzazione, carico il gruppo e lo imposto nella lista
		for(Integer annualizzazione : annualizzazioni) {
			GruppoAttivitaIva gruppoAttivitaIva = gruppoAttivitaIvaDad.findGruppoAttivitaIvaByIdAndAnno(gruppo.getUid(), annualizzazione);
			gruppiAttivitaIva.add(gruppoAttivitaIva);
		}
		
		// Popolo la response
		res.setGruppiAttivitaIva(gruppiAttivitaIva);
	}
	
}
