/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.stampa.base.AsyncReportBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.GruppoAttivitaIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRiepilogoAnnualeIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRiepilogoAnnualeIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRiepilogoAnnualeIvaService extends AsyncReportBaseService<StampaRiepilogoAnnualeIva, StampaRiepilogoAnnualeIvaResponse, StampaRiepilogoAnnualeIvaReportHandler > {

	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private GruppoAttivitaIvaDad gruppoAttivitaIvaDad;
	@Autowired
	private EnteDad enteDad;
	
	private Ente ente;
	private Bilancio bilancio;
	private GruppoAttivitaIva gruppoAttivitaIva;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(req.getGruppoAttivitaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("gruppo attivita iva"));
		checkCondition(req.getGruppoAttivitaIva().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid gruppo attivita iva"));
		
		
	}	

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public StampaRiepilogoAnnualeIvaResponse executeService(StampaRiepilogoAnnualeIva serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void preStartElaboration() {
		caricaDettaglioEnte();
		caricaDettaglioBilancio();
		caricaDettaglioGruppoAttivitaIva();
		
	}
	
	@Override
	protected void initReportHandler() {
		reportHandler.setGruppoAttivitaIva(gruppoAttivitaIva);
		reportHandler.setBilancio(bilancio);
		reportHandler.setEnte(ente);
	}
	
	/**
	 * Ottiene i dati di dettaglio dell'ente.
	 */
	private void caricaDettaglioEnte() {
		ente = enteDad.getEnteByUid(req.getEnte().getUid());
		if(ente == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Ente", "uid:" + req.getEnte().getUid()));
		}
	}
	
	/**
	 * Ottiene i dati di dettaglio del bilancio.
	 */
	private void caricaDettaglioBilancio() {
		bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
		if(bilancio == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Bilancio", "uid:" + req.getBilancio().getUid()));
		}
	}
	
	/**
	 * Ottiene i dati di dettaglio del gruppo attivita iva.
	 */
	private void caricaDettaglioGruppoAttivitaIva() {
		final String methodName = "caricaDettaglioGruppoAttivitaIva";
		final Integer uid = req.getGruppoAttivitaIva().getUid();
		log.debug(methodName, "Caricamento dati gruppo attivita iva per uid " + uid);
		gruppoAttivitaIva = gruppoAttivitaIvaDad.findGruppoAttivitaIvaByIdAndAnno(uid, bilancio.getAnno());
		if(gruppoAttivitaIva == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Gruppo Attivita Iva", "con uid:" + uid));
		}
	}
	

	@Override
	protected void postStartElaboration() {
		final String methodName = "postStartElaboration";
		log.info(methodName, "post start elaborazione!");
	}


	
}
