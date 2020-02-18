/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RiCompletaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RiCompletaAllegatoAttoPerElenchiResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RiCompletaAllegatoAttoPerElenchiAsyncService extends AsyncBaseService<RiCompletaAllegatoAttoPerElenchi, 
																				  RiCompletaAllegatoAttoPerElenchiResponse, 
																				  AsyncServiceRequestWrapper<RiCompletaAllegatoAttoPerElenchi>, 
																				  RiCompletaAllegatoAttoPerElenchiAsyncResponseHandler, 
																				  RiCompletaAllegatoAttoPerElenchiService> {
	
	//DADs
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	@Override
	public void executeService() {
		stampaDettaglioOperazione();
		super.executeService();
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		String methodName = "checkServiceParam";
		service.checkServiceParam();
		log.debug(methodName, "Errori riscontrati: "+ service.getServiceResponse().getErrori());
		res.addErrori(service.getServiceResponse().getErrori());
	}

	@Override
	protected void preStartService() {
		//Nessun check sui dati da effettuare.
	}

	@Override
	protected void postStartService() {
		// Nessuna elaborazione ulteriore

	}
	//stampa nei log i dettaglio (info) dell'allegato atto
	private void stampaDettaglioOperazione() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Elaborazione Ritorna A Completato per ");
		sb.append("Atto ");
		if(req !=null && req.getRequest() !=null && req.getRequest().getAllegatoAtto()!= null ){
			AllegatoAtto aa = ottieniAnnoNumeroEVersioneFirmaAllegato();
			sb.append((aa.getAttoAmministrativo() !=null &&aa.getAttoAmministrativo().getAnno() !=0) ? aa.getAttoAmministrativo().getAnno() :" ");
			sb.append("/");
			sb.append((aa.getAttoAmministrativo() !=null && aa.getAttoAmministrativo().getNumero() !=0) ? aa.getAttoAmministrativo().getNumero() :" ");
			sb.append("-");
			sb.append(aa.getVersioneInvioFirmaNotNull());
			log.debug("stampaDettaglioOperazione", sb.toString());
			return;
		}
		sb.append(" null");
		log.debug("stampaDettaglioOperazione", sb.toString());
	}


	private AllegatoAtto ottieniAnnoNumeroEVersioneFirmaAllegato() {

		AllegatoAtto temp = allegatoAttoDad.findAnnoNumeroEVersioneFirmaAllegatoByUid(req.getRequest().getAllegatoAtto().getUid());
		if (temp == null) {
//			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("allegato atto", "uid " + req.getRequest().getAllegatoAtto().getUid()));
		}

		return temp;
	}
	
}
