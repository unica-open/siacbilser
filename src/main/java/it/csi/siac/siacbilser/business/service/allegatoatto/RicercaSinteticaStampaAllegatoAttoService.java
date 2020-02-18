/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.StampaAllegatoAttoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAttoStampa;


/**
 * The Class RicercaSinteticaStampaAllegatoAttoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaStampaAllegatoAttoService extends CheckedAccountBaseService<RicercaSinteticaStampaAllegatoAtto, RicercaSinteticaStampaAllegatoAttoResponse> {

	@Autowired
	private StampaAllegatoAttoDad stampaAllegatoAttoDad;
	
	private AllegatoAttoStampa allegatoAttoStampa;
	
	private ParametriPaginazione parametriPaginazione;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAllegatoAttoStampa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stampa allegato atto"));
		allegatoAttoStampa = req.getAllegatoAttoStampa();
		
		checkEntita(allegatoAttoStampa.getEnte(), "ente stampa allegato atto", false);
		
	
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"), false);
		

		// Se indicato anno provvedimento deve esserlo anche il numero e viceversa.
		if(req.getAllegatoAttoStampa().getAllegatoAtto()!=null){
		checkCondition(req.getAllegatoAttoStampa().getAllegatoAtto().getAttoAmministrativo() == null
				|| !(req.getAllegatoAttoStampa().getAllegatoAtto().getAttoAmministrativo().getAnno() == 0 ^ req.getAllegatoAttoStampa().getAllegatoAtto().getAttoAmministrativo().getNumero() == 0),
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("anno e numero provvedimento devono essere entrambi valorizzati o non valorizzati"), false);
		}
	}
	
	@Override
	@Transactional
	public RicercaSinteticaStampaAllegatoAttoResponse executeService(RicercaSinteticaStampaAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
	
		stampaAllegatoAttoDad.setEnte(req.getEnte());
		stampaAllegatoAttoDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		parametriPaginazione = req.getParametriPaginazione();
		
		ListaPaginata<AllegatoAttoStampa> listaAllegatoAttoStampa = stampaAllegatoAttoDad.ricercaSinteticaStampaAllegatoAtto(allegatoAttoStampa, parametriPaginazione);
		res.setListaAllegatoAttoStampa(listaAllegatoAttoStampa);
		res.setCardinalitaComplessiva(listaAllegatoAttoStampa.size());
		
	}

		
	

}
