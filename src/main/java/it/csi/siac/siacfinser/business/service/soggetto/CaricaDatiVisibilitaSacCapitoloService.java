/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.CaricaDatiVisibilitaSacCapitolo;
import it.csi.siac.siacfinser.frontend.webservice.msg.CaricaDatiVisibilitaSacCapitoloResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.CaricaDatiVisibilitaSacCapitoloDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CaricaDatiVisibilitaSacCapitoloService extends AbstractBaseService<CaricaDatiVisibilitaSacCapitolo, CaricaDatiVisibilitaSacCapitoloResponse>
{

	@Autowired
	CommonDad commonDad;

	@Override
	protected void init(){
		commonDad.setEnte(req.getRichiedente().getAccount().getEnte());
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public CaricaDatiVisibilitaSacCapitoloResponse executeService(CaricaDatiVisibilitaSacCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}	

	@Override
	public void execute()
	{
		Integer uidCapitolo = req.getUidCapitolo();
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi:
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.ANNULLA, null);
		
		
		CaricaDatiVisibilitaSacCapitoloDto datiVisibilita = commonDad.caricaDatiVisibilitaSacCapitoloService(uidCapitolo, datiOperazione);
		
		if(datiVisibilita!=null){
			res.setVisibiliAll(datiVisibilita.isVisibiliAll());
			res.setIdSacVisibili(datiVisibilita.getIdSacVisibili());
		}
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
	
		//dati di input presi da request:
		Integer idCapitolo = req.getUidCapitolo();

		if(null==idCapitolo){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("UID_CAPITOLO"));			
		} else if(idCapitolo<=0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("UID_CAPITOLO"));
		} 
	}	
	
}
