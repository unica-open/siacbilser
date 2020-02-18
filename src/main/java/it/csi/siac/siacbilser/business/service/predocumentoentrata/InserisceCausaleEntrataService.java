/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.model.ModelloCausale;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceCausaleEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceCausaleEntrataService extends CrudCausaleEntrataBaseService<InserisceCausaleEntrata, InserisceCausaleEntrataResponse> {

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		causale = req.getCausaleEntrata();
		
		checkNotNull(causale, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("causale"));
		
		checkNotNull(causale.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente causale"));
		checkCondition(causale.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente causale"));
		
		checkNotNull(causale.getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice causale"));
		checkNotNull(causale.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione causale"));
		
		checkNotNull(causale.getTipoCausale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo causale"));
		checkCondition(causale.getTipoCausale().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid tipo causale"));
		
		tipoCausale = causale.getTipoCausale();
		
		if(causale.getModelloCausale()==null){
			causale.setModelloCausale(ModelloCausale.PREDOCUMENTO_ENTRATA);
		}
		
		checkCondition(causale.getModelloCausale()!=null && (causale.getModelloCausale().equals(ModelloCausale.PREDOCUMENTO_ENTRATA) 
				|| causale.getModelloCausale().equals(ModelloCausale.ONERI)), ErroreCore.PARAMETRO_ERRATO.getErrore("modello causale"));
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public InserisceCausaleEntrataResponse executeService(InserisceCausaleEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		causaleDad.setLoginOperazione(loginOperazione);
		causaleDad.setEnte(causale.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkSoggetto();
		checkCodiceCausaleInserimento();
		checkCongruenzaSoggettoIncasso();
		checkAccertamento();
		
		causaleDad.inserisciCausaleEntrata(causale);
		res.setCausaleEntrata(causale);
		
		
	}
	
	
}
