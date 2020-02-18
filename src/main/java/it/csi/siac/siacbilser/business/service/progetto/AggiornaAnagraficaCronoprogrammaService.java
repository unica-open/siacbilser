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

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaCronoprogrammaResponse;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaAnagraficaCronoprogrammaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAnagraficaCronoprogrammaService extends CronoprogrammaBaseService<AggiornaAnagraficaCronoprogramma, AggiornaAnagraficaCronoprogrammaResponse> {

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		//parametri del cronoprogramma
		cronoprogramma  = req.getCronoprogramma();
		checkEntita(cronoprogramma, "cronoprogramma");
		checkEntita(cronoprogramma.getBilancio(), "bilancio cronoprogramma", false);
		checkEntita(cronoprogramma.getEnte(), "ente cronoprogramma", false);
		
		checkNotNull(cronoprogramma.getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice cronoprogramma"), false);
		checkNotNull(cronoprogramma.getCronoprogrammaDaDefinire(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cronoprogramma da definire"), false);
		
		//parametri del progetto a cui è associato il cronoprogramma
		progetto = req.getCronoprogramma().getProgetto();
		checkEntita(progetto, "progetto", false);
		
		checkNotNull(cronoprogramma.getCapitoliEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettagli entrata"));
		checkCondition(!cronoprogramma.getCapitoliEntrata().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettagli entrata"));
		checkNotNull(cronoprogramma.getCapitoliUscita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettagli uscita"));
		checkCondition(!cronoprogramma.getCapitoliUscita().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettagli uscita"));
		
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		cronoprogrammaDad.setEnte(cronoprogramma.getEnte());
		cronoprogrammaDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public AggiornaAnagraficaCronoprogrammaResponse executeService(AggiornaAnagraficaCronoprogramma serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkEsisteCronoprogrammaConStessoCodice();
		checkCronoprogrammaAggiornabile();
		checkImportiDettagliUscitaCronoprogramma();
		
		if(cronoprogramma.getAttoAmministrativo() != null && cronoprogramma.getAttoAmministrativo().getUid() != 0){
			caricaAttoAmministrativo();
			checkStatoAttoAmministrativo();
		}
		
		cronoprogramma.setStatoOperativoCronoprogramma(determinaStatoOperativoCronoprogramma());
		
		cronoprogrammaDad.aggiornaAnagraficaCronoprogramma(cronoprogramma);

		aggiornaDettagliUscitaCronoprogramma();
		
		aggiornaDettagliEntrataCronoprogramma();		
		
		res.setCronoprogramma(cronoprogramma);
	}
	
	
	/**
	 * Check esiste cronoprogramma con stesso codice.
	 */
	private void checkEsisteCronoprogrammaConStessoCodice() {
		List<Integer> uid = cronoprogrammaDad.findUidCronoprogrammaConStessoCodiceNonAnnullatoAndUidProgettoAndBilancio(cronoprogramma);
		if(uid!=null && !uid.isEmpty() &&!uid.contains(cronoprogramma.getUid())){
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("cronoprogramma",cronoprogramma.getCodice()+"/"+cronoprogramma.getStatoOperativoCronoprogramma()), Esito.FALLIMENTO);
		}
	}
	

	/**
	 * Inserisce tutti i dettagli di uscita del cronoprogramma.
	 */
	private void aggiornaDettagliUscitaCronoprogramma() {		
		for (DettaglioUscitaCronoprogramma dett : cronoprogramma.getCapitoliUscita()) {
			if(dett.getUid()==0 && dett.getDataFineValidita() == null) {
				checkDettaglioUscita(dett);
				dett = inserisciDettaglioUscita(dett);
			} else if(dett.getUid()!=0 && dett.getDataFineValidita()!=null){
				dett = cancellaDettaglioUscita(dett);
			} else if(dett.getUid()!=0 && dett.getDataFineValidita()==null){
				checkDettaglioUscita(dett);
				dett = aggiornaDettaglioUscita(dett);
			}
		}
	}


	
	
	/**
	 * Inserisce tutti i dettagli di entrata del cronoprogramma.
	 */
	private void aggiornaDettagliEntrataCronoprogramma() {		
		for (DettaglioEntrataCronoprogramma dett : cronoprogramma.getCapitoliEntrata()) {			
			if(dett.getUid()==0 && dett.getDataFineValidita() == null) {
				dett = inserisciDettaglioEntrata(dett);
			} else if(dett.getUid()!=0 && dett.getDataFineValidita()!=null){
				dett = cancellaDettaglioEntrata(dett);
			} else if(dett.getUid()!=0 && dett.getDataFineValidita()==null){
				dett = aggiornaDettaglioEntrata(dett);
			}		
			
		}
	}
	
	/**
	 * UN cronoprogramma e' aggiornabile solo se e' valido e non e' stato usato per calcolo fpv
	 */
	private void checkCronoprogrammaAggiornabile(){
		if(cronoprogramma.getUsatoPerFpv() || StatoOperativoCronoprogramma.ANNULLATO.equals(cronoprogramma.getStatoOperativoCronoprogramma())){
			throw new BusinessException(ErroreBil.CRONOPROGRAMMA_CHE_NON_SI_PUO_AGGIORNARE_PERCHE_UTILIZZATO_NEL_CALCOLO_FPV.getErrore("cronoprogramma",cronoprogramma.getCodice()+"/"+cronoprogramma.getStatoOperativoCronoprogramma()), Esito.FALLIMENTO);
		}
		
		checkCoerenzaBilancioProgettoAssociato();
	}


	
	

}
