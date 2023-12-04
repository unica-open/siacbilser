/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogrammaResponse;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * Ricerca di un cronoprogramma.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioCronoprogrammaService extends CheckedAccountBaseService<RicercaDettaglioCronoprogramma, RicercaDettaglioCronoprogrammaResponse> {

	/** The cronoprogramma. */
	private Cronoprogramma cronoprogramma;
	
	/** The cronoprogramma dad. */
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		//parametri del cronoprogramma
		cronoprogramma  = req.getCronoprogramma();
		checkNotNull(cronoprogramma, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cronoprogramma"));	
		
//		checkNotNull(cronoprogramma.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio cronoprogramma"));
//		checkCondition(cronoprogramma.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio cronoprogramma"));
//		checkNotNull(cronoprogramma.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente cronoprogramma"));
//		checkCondition(cronoprogramma.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente cronoprogramma"));
				
		checkCondition(cronoprogramma.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid cronoprogramma"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
//		cronoprogrammaDad.setEnte(cronoprogramma.getEnte());
//		cronoprogrammaDad.setBilancio(cronoprogramma.getBilancio());
		cronoprogrammaDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {		
		Cronoprogramma c = cronoprogrammaDad.findCronoprogrammaById(cronoprogramma.getUid());		
		
		if(c == null){			
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("cronoprogramma", "id: "+cronoprogramma.getUid()));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		TipoProgetto tipoProgettoAssociato = cronoprogrammaDad.getTipoProgettoByCronoprogramma(c);
		if(tipoProgettoAssociato == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("impossibile trovare uil tipo progetto per il progetto associato."));
		}
		
		List<DettaglioEntrataCronoprogramma> dettagliEntrata = cronoprogrammaDad.findDettagliEntrataCronoprogramma(c, tipoProgettoAssociato.getTipoCapitoloEntrata());
		c.setCapitoliEntrata(dettagliEntrata);
		
		List<DettaglioUscitaCronoprogramma> dettagliUscita = cronoprogrammaDad.findDettagliUscitaCronoprogramma(c, tipoProgettoAssociato.getTipoCapitoloSpesa());
		c.setCapitoliUscita(dettagliUscita);
		
		res.setCronoprogramma(c);

	}

}
