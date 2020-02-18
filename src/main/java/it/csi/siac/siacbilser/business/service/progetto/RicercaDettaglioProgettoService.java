/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgettoResponse;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * Classe di Service per la ricerca di dettaglio del Progetto.
 * 
 * @author Marchino Alessandro
 * @versione 1.0.0 - 04/02/2014
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioProgettoService extends CheckedAccountBaseService<RicercaDettaglioProgetto, RicercaDettaglioProgettoResponse> {

	/** The progetto dad. */
	@Autowired
	private ProgettoDad progettoDad;
	
	/** The ricerca dei cronoprogrammi collegati al progetto service. */
	@Autowired 
	private RicercaDeiCronoprogrammiCollegatiAlProgettoService ricercaDeiCronoprogrammiCollegatiAlProgettoService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {				
		checkNotNull(req.getChiaveProgetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progetto"));		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		progettoDad.setLoginOperazione(loginOperazione);
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaDettaglioProgettoResponse executeService(RicercaDettaglioProgetto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		Progetto progetto = progettoDad.findProgettoById(req.getChiaveProgetto());
		
		popolaCronoprogrammi(progetto);
		
		res.setProgetto(progetto);
	}

	/**
	 * Popola cronoprogrammi.
	 *
	 * @param progetto the progetto
	 */
	private void popolaCronoprogrammi(Progetto progetto) {
		List<Cronoprogramma> cronoprogrammi = ricercaDeiCronoprogrammiCollegatiAlProgetto(progetto);
		progetto.setCronoprogrammi(cronoprogrammi);
		
	}

	/**
	 * Ricerca dei cronoprogrammi collegati al progetto.
	 *
	 * @param progetto the progetto
	 * @return the list
	 */
	private List<Cronoprogramma> ricercaDeiCronoprogrammiCollegatiAlProgetto(Progetto progetto) {		
		RicercaDeiCronoprogrammiCollegatiAlProgetto ricercaDeiCronoprogrammiCollegatiAlProgetto = new RicercaDeiCronoprogrammiCollegatiAlProgetto();
		ricercaDeiCronoprogrammiCollegatiAlProgetto.setProgetto(progetto);
		ricercaDeiCronoprogrammiCollegatiAlProgetto.setDataOra(new Date());
		ricercaDeiCronoprogrammiCollegatiAlProgetto.setRichiedente(req.getRichiedente());
		
		RicercaDeiCronoprogrammiCollegatiAlProgettoResponse ricercaDeiCronoprogrammiCollegatiAlProgettoResponse = 
				executeExternalServiceSuccess(ricercaDeiCronoprogrammiCollegatiAlProgettoService, ricercaDeiCronoprogrammiCollegatiAlProgetto);		
		return ricercaDeiCronoprogrammiCollegatiAlProgettoResponse.getCronoprogrami();
	}

}
