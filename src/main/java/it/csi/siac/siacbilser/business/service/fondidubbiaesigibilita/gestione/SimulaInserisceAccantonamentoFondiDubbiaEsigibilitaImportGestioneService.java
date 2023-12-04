/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.gestione;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestioneResponse;
import it.csi.siac.siacbilser.model.fcde.TipoImportazione;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;

/**
 * Simulazione inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestioneService extends BaseAccantonamentoFondiDubbiaEsigibilitaImportGestioneService<SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione, SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestioneResponse> {
	
	@Override
	@Transactional(readOnly = true)
	public SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestioneResponse executeService(SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio");
		checkNotNull(req.getTipoImportazione(), "tipo importazione");
		if(TipoImportazione.DA_VERSIONE_PRECEDENTE.equals(req.getTipoImportazione())) {
			checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioOld(), "attributi bilancio old");
		}
	}
	
	@Override
	protected void execute() {
		// Lettura accantonamento
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findById(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid(),
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
		
		// Caricamento accantonamenti
		loadAccantonamenti(req.getTipoImportazione(), req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioOld(), CapitoloEntrataGestioneModelDetail.TitoloTipologiaCategoriaSAC);
		
		res.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(attributiOld);
		res.getListaAccantonamenti().addAll(fondiDubbiaEsigibilita);
	}

}
