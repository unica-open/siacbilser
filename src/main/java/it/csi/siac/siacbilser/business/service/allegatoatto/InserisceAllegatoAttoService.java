/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Inserimento AllegatoAtto
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceAllegatoAttoService extends CheckedAccountBaseService<InserisceAllegatoAtto, InserisceAllegatoAttoResponse> {
	
	private AllegatoAtto allegatoAtto;
	
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	@Autowired
	private AttoAmministrativoDad attoAmministrativoDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		this.allegatoAtto = req.getAllegatoAtto();
		checkNotNull(allegatoAtto, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
		
		checkEntita(allegatoAtto.getEnte(), "ente");
		
		//checkNotNull(allegatoAtto.getStatoOperativoAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo allegato atto"));
		
		
		//checkEntita(allegatoAtto.getAttoAmministrativo(), "atto amministrativo allegato atto");
		
		//checkNotNull(allegatoAtto.getElenchiDocumentiAllegato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenchi documenti allegato atto allegato"));
		//checkNotNull(allegatoAtto.getDatiSensibili(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dati sensibili atto allegato"), false);
		
		//checkCondition(!allegatoAtto.getElenchiDocumentiAllegato().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenchi documenti allegato atto allegato"));
		
		checkNotBlank(allegatoAtto.getCausale(), "causale allegato atto");
		
	}
	
	@Override
	@Transactional
	public InserisceAllegatoAttoResponse executeService(InserisceAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	protected void init() {
		allegatoAttoDad.setLoginOperazione(loginOperazione);
		allegatoAttoDad.setEnte(allegatoAtto.getEnte());
	}
	
	@Override
	protected void execute() {
		checkAttoAmministrativoInStatoDefinitivo();
		checkAttoAmministrativoNonAbbinato();
		defaultPerAllegatoAtto();
		
		
		allegatoAttoDad.inserisciAllegatoAtto(allegatoAtto);		
		res.setAllegatoAtto(allegatoAtto);
	}
	
	/**
	 * L'attoAmministrativo non deve essere gi&agrave; stato associato  ad altro "allegato atto" e a nessuna quota documento,  
	 * in caso contrario viene inviato il messaggio <FIN_ERR_214, Atto gi&agrave; abbinato>.
	 */
	private void checkAttoAmministrativoNonAbbinato() {
		AllegatoAtto aa = allegatoAttoDad.findAllegatoAttoByAttoAmministrativo(allegatoAtto.getAttoAmministrativo());
		if(aa!=null){
			res.setAllegatoAtto(aa);
			throw new BusinessException(ErroreFin.ATTO_GIA_ABBINATO.getErrore(""));
		}
	}

	/**
	 * Il provvedimento passato in input  deve essere identificato in modo univoco 
	 * e  in stato DEFINITIVO altrimenti viene visualizzato il messaggio <FIN_ERR_0075, 
	 * Stato Provvedimento non consentito, ‘Gestione Allegato atto, ‘Definitivo’>.
	 * 
	 */
	private void checkAttoAmministrativoInStatoDefinitivo() {
		String methodName = "checkAttoAmministrativoInStatoDefinitivo";
		StatoOperativoAtti stato = attoAmministrativoDad.findStatoOperativoAttoAmministrativo(allegatoAtto.getAttoAmministrativo());
		
		log.debug(methodName, "stato: "+stato);
		if(!StatoOperativoAtti.DEFINITIVO.equals(stato)){
			throw new BusinessException(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Gestione Allegato Atto", "Definitivo"));
		}
	}
	
	/**
	 * Imposta i dati di default per l'allegato
	 */
	private void defaultPerAllegatoAtto() {
		if(allegatoAtto.getFlagRitenute() == null) {
			allegatoAtto.setFlagRitenute(Boolean.FALSE);
		}
		//SIAC-6426
		if(allegatoAtto.getVersioneInvioFirma() == null){
			allegatoAtto.setVersioneInvioFirma(0);
		}
	}
}
