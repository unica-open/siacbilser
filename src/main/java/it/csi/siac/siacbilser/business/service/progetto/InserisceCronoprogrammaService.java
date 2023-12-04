/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCronoprogrammaResponse;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * Inserimento di un cronoprogramma associato ad un Progetto.
 *
 * @author Domenico Lisi
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceCronoprogrammaService extends CronoprogrammaBaseService<InserisceCronoprogramma, InserisceCronoprogrammaResponse> {

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		//parametri del cronoprogramma
		cronoprogramma  = req.getCronoprogramma();
		checkNotNull(cronoprogramma, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cronoprogramma"));
		checkEntita(cronoprogramma.getBilancio(), "bilancio cronoprogramma", false);
		checkEntita(cronoprogramma.getEnte(), "ente cronoprogramma", false);
		
		checkNotNull(cronoprogramma.getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice cronoprogramma"), false);
		checkNotNull(cronoprogramma.getCronoprogrammaDaDefinire(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cronoprogramma da definire"), false);
		
		//parametri del progetto a cui è associato il cronoprogramma
		progetto = req.getCronoprogramma().getProgetto();
		checkEntita(progetto, "progetto");
		
		
		checkNotNull(cronoprogramma.getCapitoliEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettagli entrata"));
		checkCondition(!cronoprogramma.getCapitoliEntrata().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettagli entrata"), false);
		checkNotNull(cronoprogramma.getCapitoliUscita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettagli uscita"));
		checkCondition(!cronoprogramma.getCapitoliUscita().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettagli uscita"), false);
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		cronoprogrammaDad.setEnte(ente);
		cronoprogrammaDad.setLoginOperazione(loginOperazione);
		attoAmministrativoDad.setEnte(ente);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public InserisceCronoprogrammaResponse executeService(InserisceCronoprogramma serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		checkCoerenzaBilancioProgettoAssociato();
		
		checkImportiDettagliUscitaCronoprogramma();
		
		if(cronoprogramma.getAttoAmministrativo() != null && cronoprogramma.getAttoAmministrativo().getUid() != 0){
			caricaAttoAmministrativo();
			checkStatoAttoAmministrativo();
		}
		
		cronoprogramma.setStatoOperativoCronoprogramma(determinaStatoOperativoCronoprogramma());
		
		checkEsisteCronoprogrammaConStessoCodiceEStatoOperativo();
		
		cronoprogrammaDad.inserisciAnagraficaCronoprogramma(cronoprogramma);
		
		inserisciDettagliUscitaCronoprogramma();
		
		inserisciDettagliEntrataCronoprogramma();		
		
		res.setCronoprogramma(cronoprogramma);
	}
	
	/**
	 * Check esiste cronoprogramma con stesso codice.
	 */
	private void checkEsisteCronoprogrammaConStessoCodiceEStatoOperativo() {
		List<Integer> uid = cronoprogrammaDad.findUidCronoprogrammaConStessoCodiceNonAnnullatoAndUidProgettoAndBilancio(cronoprogramma);
		if(uid!=null && !uid.isEmpty()){
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("cronoprogramma",cronoprogramma.getCodice()+"/"+cronoprogramma.getStatoOperativoCronoprogramma()), Esito.FALLIMENTO);
		}		
	}

	/**
	 * Inserisce tutti i dettagli di uscita del cronoprogramma.
	 */
	private void inserisciDettagliUscitaCronoprogramma() {		
		for (DettaglioUscitaCronoprogramma dett : cronoprogramma.getCapitoliUscita()) {
			checkDettaglioUscita(dett);
			dett = inserisciDettaglioUscita(dett);
		}
	}
	
	/**
	 * Inserisce tutti i dettagli di entrata del cronoprogramma.
	 */
	private void inserisciDettagliEntrataCronoprogramma() {		
		for (DettaglioEntrataCronoprogramma dett : cronoprogramma.getCapitoliEntrata()) {
			dett = inserisciDettaglioEntrata(dett);
		}
	}
	
}
