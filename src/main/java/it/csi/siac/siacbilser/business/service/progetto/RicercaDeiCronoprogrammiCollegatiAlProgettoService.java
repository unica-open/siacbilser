/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoResponse;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDeiCronoprogrammiCollegatiAlProgettoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDeiCronoprogrammiCollegatiAlProgettoService extends CheckedAccountBaseService<RicercaDeiCronoprogrammiCollegatiAlProgetto, RicercaDeiCronoprogrammiCollegatiAlProgettoResponse> {
	
	/** The cronoprogramma dad. */
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;
	
	@Autowired
	private ProgettoDad progettoDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getProgetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progetto"));
		boolean chiaveFisicaProgettoValorizzata = req.getProgetto().getUid()!= 0;
		boolean chiaveLogicaProgettoValorizzata = StringUtils.isNotBlank(req.getProgetto().getCodice()) && req.getProgetto().getTipoProgetto() != null;
		checkCondition(chiaveFisicaProgettoValorizzata || chiaveLogicaProgettoValorizzata, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave progetto"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		cronoprogrammaDad.setLoginOperazione(loginOperazione);
		cronoprogrammaDad.setEnte(ente);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<Cronoprogramma> listaCronoprogrammi = cronoprogrammaDad.findCronoprogrammiByProgetto(req.getProgetto(), req.getAnnoBilancioCronoprogrammi());		
		
		TipoProgetto tipoProgettoAssociato = caricaTipoProgetto();
		
		for (Cronoprogramma c : listaCronoprogrammi) {			
			
			List<DettaglioEntrataCronoprogramma> dettagliEntrata = cronoprogrammaDad.findDettagliEntrataCronoprogramma(c, tipoProgettoAssociato.getTipoCapitoloEntrata());
			c.setCapitoliEntrata(dettagliEntrata);
			
			List<DettaglioUscitaCronoprogramma> dettagliUscita = cronoprogrammaDad.findDettagliUscitaCronoprogramma(c,tipoProgettoAssociato.getTipoCapitoloSpesa());
			c.setCapitoliUscita(dettagliUscita);
			
			
		}
		
		
		res.setCronoprogrami(listaCronoprogrammi);

	}

	/**
	 * @return
	 */
	private TipoProgetto caricaTipoProgetto() {
		if(req.getProgetto().getTipoProgetto() != null) {
			return req.getProgetto().getTipoProgetto();
		}
		TipoProgetto tipoProgettoAssociato = progettoDad.caricaTipoProgetto(req.getProgetto());
		if(tipoProgettoAssociato == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("impossibile trovare uil tipo progetto per il progetto associato."));
		}
		return tipoProgettoAssociato;
	}

}
