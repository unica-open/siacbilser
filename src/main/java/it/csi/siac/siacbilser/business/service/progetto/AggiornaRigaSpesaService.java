/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaSpesaResponse;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaRigaSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaRigaSpesaService extends CheckedAccountBaseService<AggiornaRigaSpesa, AggiornaRigaSpesaResponse> {
	
	
	/** The dett. */
	private DettaglioUscitaCronoprogramma dett;
	
	/** The cronoprogramma dad. */
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		dett = req.getDettaglioUscitaCronoprogramma();
		checkNotNull(dett, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettaglio entrata programma"));
		checkCondition(dett.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid dettaglio entrata programma"));
		
		checkNotNull(dett.getCronoprogramma(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cronoprogramma"));
		checkCondition(dett.getCronoprogramma().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid cronoprogramma"));
		
		checkNotNull(dett.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(dett.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		//checkNotNull(dett.getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"));
		checkNotNull(dett.getDescrizioneCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione capitolo"));
		
		checkNotNull(dett.getStanziamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stanziamento"));
		
//		checkNotNull(dett.getMissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("missione"));
//		checkNotNull(dett.getProgramma(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("programma"));
//		checkNotNull(dett.getTitoloSpesa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("titolo"));

	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaRigaSpesaResponse executeService(AggiornaRigaSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		cronoprogrammaDad.setEnte(dett.getEnte());
		//cronoprogrammaDad.setBilancio(dett.getBilancio());
		cronoprogrammaDad.setLoginOperazione(loginOperazione);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkAnni();
		
		//SIAC-6255
		TipoCapitolo tipoCapitoloDettaglio = caricaTipoCapitoloDettaglio();
		
		cronoprogrammaDad.aggiornaDettaglioUscitaCronoprogramma(dett, tipoCapitoloDettaglio);
		res.setDettaglioUscitaCronoprogramma(dett);
	}

	/**
	 * Controlla l'obbligatoriet&agrave; degli anni
	 */
	private void checkAnni() {
		final String methodName = "checkAnni";
		boolean daDefinire = cronoprogrammaDad.isDaDefinire(dett.getCronoprogramma());
		
		if(daDefinire) {
			log.debug(methodName, "Cronoprogramma da definire: controlli sugli anni saltati");
			return;
		}
		
		if(dett.getAnnoCompetenza() == null) {
			log.warn(methodName, "Anno competenza assente");
			throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno competenza"));
		}
		if(dett.getAnnoEntrata() == null) {
			log.warn(methodName, "Anno entrata assente");
			throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno entrata"));
		}
		if(dett.getAnnoCompetenza().compareTo(dett.getAnnoEntrata()) < 0) {
			log.warn(methodName, "Anni non coerenti (non e' vero che l'anno di competenza " + dett.getAnnoCompetenza() + " sia maggiore o uguale all'anno dell'entrata " + dett.getAnnoEntrata() + ")");
			throw new BusinessException(ErroreBil.COMPETENZA_SPESA_ANTECEDENTE_COMPETENZA_ENTRATA.getErrore());
		}
	}
	
	/**
	 * Carica tipo capitolo dettaglio.
	 *
	 * @return the tipo capitolo
	 */
	private TipoCapitolo caricaTipoCapitoloDettaglio() {
		TipoProgetto tipoProgettoAssciato = cronoprogrammaDad.getTipoProgettoByCronoprogramma(dett.getCronoprogramma());
		if(tipoProgettoAssciato == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("progetto associato al cronoprogramma", "impossibile reperire il tipo (previsione/gestione)"));
		}
		
		return tipoProgettoAssciato.getTipoCapitoloSpesa();
	}
}
