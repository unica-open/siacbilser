/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipocomponenteimpcap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.ComponenteImportiCapitoloDad;
import it.csi.siac.siacbilser.model.StatoTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaTipoComponenteImportiCapitoloService 
	extends BaseTipoComponenteImportiCapitoloService<AnnullaTipoComponenteImportiCapitolo, AnnullaTipoComponenteImportiCapitoloResponse> {

	@Autowired 
	protected ComponenteImportiCapitoloDad componenteImportiCapitoloDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getTipoComponenteImportiCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo componente importi capitolo"));
	}
		
	@Override
	@Transactional
	public AnnullaTipoComponenteImportiCapitoloResponse executeService(AnnullaTipoComponenteImportiCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		Long collegamentiPerTipo = componenteImportiCapitoloDad.countByTipoComponenteImportiCapitolo(req.getTipoComponenteImportiCapitolo().getUid());
		
		checkBusinessCondition(collegamentiPerTipo == null || collegamentiPerTipo.longValue() == 0L,
				ErroreCore.ANNULLAMENTO_NON_POSSIBILE.getErrore("La componente selezionata risulta essere già utilizzata."));
		
		TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo = 
			tipoComponenteImportiCapitoloDad.ricercaDettaglioTipoComponenteImportiCapitolo(req.getTipoComponenteImportiCapitolo());
		
		if (StatoTipoComponenteImportiCapitolo.ANNULLATO.equals(tipoComponenteImportiCapitolo.getStatoTipoComponenteImportiCapitolo())) {
			return;
		}
		
		tipoComponenteImportiCapitolo.setStatoTipoComponenteImportiCapitolo(StatoTipoComponenteImportiCapitolo.ANNULLATO);
		
		tipoComponenteImportiCapitoloDad.aggiornaTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo);
	}
	
}
