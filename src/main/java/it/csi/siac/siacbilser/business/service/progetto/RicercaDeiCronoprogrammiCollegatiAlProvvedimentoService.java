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
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProvvedimento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProvvedimentoResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDeiCronoprogrammiCollegatiAlProgettoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDeiCronoprogrammiCollegatiAlProvvedimentoService 
	extends CheckedAccountBaseService<RicercaDeiCronoprogrammiCollegatiAlProvvedimento, RicercaDeiCronoprogrammiCollegatiAlProvvedimentoResponse> {
	
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
		List<Cronoprogramma> listaCronoprogrammi = cronoprogrammaDad.findCronoprogrammiByProvvedimento(req.getNumeroAttoAmm(), req.getAnnoAttoAmm(), req.getIdTipoProvvedimento(), req.getIdStrutturaAmministrativoContabile());		
		
		for (Cronoprogramma c : listaCronoprogrammi) {			
			c.setProgetto(progettoDad.findProgettoById(c.getProgetto().getUid()));
			
			TipoProgetto tipoProgettoAssociato = caricaTipoProgetto(c.getProgetto());

//			List<DettaglioEntrataCronoprogramma> dettagliEntrata = cronoprogrammaDad.findDettagliEntrataCronoprogramma(c, tipoProgettoAssociato.getTipoCapitoloEntrata());
//			c.setCapitoliEntrata(dettagliEntrata);
//			
			List<DettaglioUscitaCronoprogramma> dettagliUscita = cronoprogrammaDad.findDettagliUscitaCronoprogramma(c,tipoProgettoAssociato.getTipoCapitoloSpesa());
			c.setCapitoliUscita(dettagliUscita);
		}
		
		res.setCronoprogrammi(listaCronoprogrammi);
	}

	/**
	 * @return
	 */
	private TipoProgetto caricaTipoProgetto(Progetto p) {
		if(p.getTipoProgetto() != null) {
			return p.getTipoProgetto();
		}
		
		TipoProgetto tipoProgettoAssociato = progettoDad.caricaTipoProgetto(p);
		
		if(tipoProgettoAssociato == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("impossibile trovare uil tipo progetto per il progetto associato."));
		}
		
		return tipoProgettoAssociato;
	}

}
