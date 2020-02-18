/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipocomponenteimpcap;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaTipoComponenteImportiCapitoloService extends BaseTipoComponenteImportiCapitoloService<AggiornaTipoComponenteImportiCapitolo, AggiornaTipoComponenteImportiCapitoloResponse>{
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getTipoComponenteImportiCapitolo(), "tipo componente importi capitolo");
		//checkNotNull(req.getTipoComponenteImportiCapitolo().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"));
		checkNotNull(req.getTipoComponenteImportiCapitolo().getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice"));
		checkNotNull(req.getTipoComponenteImportiCapitolo().getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione"));
		checkNotNull(req.getTipoComponenteImportiCapitolo().getTipoGestioneComponenteImportiCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo gestione"));
		checkCondition(
				req.getTipoComponenteImportiCapitolo().getDataInizioValidita() == null || 
					req.getTipoComponenteImportiCapitolo().getDataFineValidita() == null || 
					req.getTipoComponenteImportiCapitolo().getDataInizioValidita().before(req.getTipoComponenteImportiCapitolo().getDataFineValidita()),
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("la data di fine attività deve essere maggiore della data di inizio attività"),
				true);
	}
	
	@Override
	@Transactional
	public AggiornaTipoComponenteImportiCapitoloResponse executeService(AggiornaTipoComponenteImportiCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		res.setTipoComponenteImportiCapitolo(tipoComponenteImportiCapitoloDad.aggiornaTipoComponenteImportiCapitolo(req.getTipoComponenteImportiCapitolo()));
	}
	
}
