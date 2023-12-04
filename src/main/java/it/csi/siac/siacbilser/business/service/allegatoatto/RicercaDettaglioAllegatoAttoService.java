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
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAttoModelDetail;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioAllegatoAttoService extends CheckedAccountBaseService<RicercaDettaglioAllegatoAtto,RicercaDettaglioAllegatoAttoResponse> {

private AllegatoAtto allegatoAtto;
	
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.allegatoAtto = req.getAllegatoAtto();
		
		checkNotNull(allegatoAtto, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
		checkCondition(allegatoAtto.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"));
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioAllegatoAttoResponse executeService(RicercaDettaglioAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	protected void init() {
		allegatoAttoDad.setLoginOperazione(loginOperazione);
//		allegatoAttoDad.setEnte(allegatoAtto.getEnte());
	}
	
	
	@Override
	protected void execute() {
		// Retrocompatibilita'
		if(req.getAllegatoAttoModelDetails() == null) {
			req.setAllegatoAttoModelDetails(AllegatoAttoModelDetail.DataInizioValiditaStato, AllegatoAttoModelDetail.DatiSoggetto, AllegatoAttoModelDetail.ElencoDocumenti);
		}
		
		Utility.MDTL.addModelDetails(req.getModelDetailsEntitaCollegate());
		
//		AllegatoAtto aa = allegatoAttoDad.findAllegatoAttoById(allegatoAtto.getUid());		
		AllegatoAtto aa = allegatoAttoDad.findAllegatoAttoById(allegatoAtto.getUid(), req.getAllegatoAttoModelDetails());
//		AllegatoAtto aa = allegatoAttoDad.findAllegatoAttoById(allegatoAtto.getUid(), AllegatoAttoDatiSoggettoConverter.class);
		
		if (aa == null) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Allegato atto", "uid:" + allegatoAtto.getUid()));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		//aa.setDataCompletamento(allegatoAttoDad.findDataUltimoInizioValiditaStato(aa, StatoOperativoAllegatoAtto.COMPLETATO));		
		
		res.setAllegatoAtto(aa);

	}

}
