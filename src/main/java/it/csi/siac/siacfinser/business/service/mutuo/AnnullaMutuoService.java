/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.mutuo;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMutuoResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.MutuoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.errore.ErroreFin;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaMutuoService extends AbstractBaseService<AnnullaMutuo, AnnullaMutuoResponse> {

	@Autowired
	ProvvedimentoService provvedimentoService;

	@Autowired
	MutuoDad mutuoDad;
	
	@Autowired
	CommonDad commonDad;
	
	
	
	private DatiOperazioneDto datiOperazioneAnnulla = new DatiOperazioneDto();
	
	@Override
	protected void init() {
		final String methodName = "AnnullaMutuoService : init()";
		log.debug(methodName, "- Begin");
		
		
	}
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	
	@Override
	@Transactional
	public AnnullaMutuoResponse executeService(AnnullaMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	
	
	@Override
	public void execute() {
		final String methodName = "AnnullaMutuoService : execute()";
		
		// Procedo con l'annullamento
		// reperimento mutuo da annullare
		// reperimento stato mutuo in tavola di decodifica
		// salvataggio dato sulla tavola di relazione
		// aggiornamento login per il mutuo
		mutuoDad.annullaMutuo(req.getMutuoDaAnnullare().getCodiceMutuo(), datiOperazioneAnnulla, req.getRichiedente());
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "AnnullaMutuoService : checkServiceParam()";
		log.debug(methodName, "- Begin");
		
		// corretta valorizzazione dei parametri
		if(req.getEnte()!=null && req.getRichiedente()!=null){
			datiOperazioneAnnulla = commonDad.inizializzaDatiOperazione(req.getEnte(), req.getRichiedente(), Operazione.ANNULLA, null);
		}else{
			res.setErrori(Arrays.asList(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente o richiedente")));
			res.setEsito(Esito.FALLIMENTO);	
		}
		
		if(req.getMutuoDaAnnullare()==null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Mutuo da annullare"));
		}
		if(req.getMutuoDaAnnullare()!=null && 
			StringUtils.isEmpty(req.getMutuoDaAnnullare().getCodiceMutuo())){
			
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Codice mutuo"));
		}
		
		// controllo la presenza di voci di mutuo
		if(!res.isFallimento()){
		    if(mutuoDad.presenzaVociMutuo(req.getMutuoDaAnnullare().getCodiceMutuo(), datiOperazioneAnnulla)){
		    	res.setErrori(Arrays.asList(ErroreFin.ANNULLAMENTO_MUTUO_IMPOSSIBILE.getErrore("")));
		    }
		}
	}	
}
