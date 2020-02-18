/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCategoriaCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCategoriaCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CategoriaCapitoloDad;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * @author paggio
 * 
 * RicercaCategoria Capitolo 
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCategoriaCapitoloService extends CheckedAccountBaseService<RicercaCategoriaCapitolo, RicercaCategoriaCapitoloResponse>{
	
	/** The codice bollo dad. */
	@Autowired
	private CategoriaCapitoloDad categoriaCapitoloDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		checkNotNull(req.getTipoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo capitolo"));
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public RicercaCategoriaCapitoloResponse executeService(RicercaCategoriaCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<CategoriaCapitolo> elencoCategoriaCapitolo = categoriaCapitoloDad.ricercaCategoriaCapitolo(req.getEnte(), req.getTipoCapitolo());
		res.setElencoCategoriaCapitolo(elencoCategoriaCapitolo);
		res.setDataOra(new Date());
		res.setEsito(Esito.SUCCESSO);
		res.setCardinalitaComplessiva(elencoCategoriaCapitolo.size());
	}
}
