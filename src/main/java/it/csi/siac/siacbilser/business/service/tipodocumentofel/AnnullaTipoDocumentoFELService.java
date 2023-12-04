/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipodocumentofel;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaTipoDocumentoFELResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaTipoDocumentoFELService	extends BaseTipoDocumentoFELService<AnnullaTipoDocumentoFEL, AnnullaTipoDocumentoFELResponse>{

 
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getTipoDocumentoFEL(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo documento"));
	}
		
	@Override
	@Transactional
	public AnnullaTipoDocumentoFELResponse executeService(AnnullaTipoDocumentoFEL serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		
		//Verificare se possibile cancellare un tipo documento
 
		Long collegamentiPerTipo = tipoDocumentoFELDad.countFatturaBySirfelDTipoDocumento(req.getTipoDocumentoFEL().getCodice());
		
		checkBusinessCondition(collegamentiPerTipo == null || collegamentiPerTipo.longValue() == 0L,
				ErroreCore.ANNULLAMENTO_NON_POSSIBILE.getErrore("Il tipo documento selezionato risulta essere già utilizzato."));
		
 
//			tipoComponenteImportiCapitoloDad.ricercaDettaglioTipoComponenteImportiCapitolo(req.getTipoComponenteImportiCapitolo());
//		
//		if (StatoTipoComponenteImportiCapitolo.ANNULLATO.equals(tipoComponenteImportiCapitolo.getStatoTipoComponenteImportiCapitolo())) {
//			return;
//		}
//		
//		tipoComponenteImportiCapitolo.setStatoTipoComponenteImportiCapitolo(StatoTipoComponenteImportiCapitolo.ANNULLATO);
		
		tipoDocumentoFELDad.eliminaTipoDocumentoFEL(req.getTipoDocumentoFEL());
	}
	
}
