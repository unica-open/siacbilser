/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.previsione;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.RipristinaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.RipristinaAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RipristinaAccantonamentoFondiDubbiaEsigibilitaService extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaService<RipristinaAccantonamentoFondiDubbiaEsigibilita, RipristinaAccantonamentoFondiDubbiaEsigibilitaResponse> {
	
	@Override
	@Transactional
	public RipristinaAccantonamentoFondiDubbiaEsigibilitaResponse executeService(RipristinaAccantonamentoFondiDubbiaEsigibilita serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getListaAccantonamentoFondiDubbiaEsigibilita(), "lista accantonamenti");
		checkCondition(!req.getListaAccantonamentoFondiDubbiaEsigibilita().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("lista accantonamenti"));
		int i = 1;
		for(AccantonamentoFondiDubbiaEsigibilita afde : req.getListaAccantonamentoFondiDubbiaEsigibilita()) {
			checkEntita(afde, "accantonamento numero " + i++);
		}
	}
	
	@Override
	protected void execute() {
		// Lettura bilancio di un accantonamento
		loadAttributiBilancio(req.getListaAccantonamentoFondiDubbiaEsigibilita().get(0));
		
		Utility.MDTL.addModelDetails(
			AccantonamentoFondiDubbiaEsigibilitaModelDetail.CapitoloEntrataPrevisione,
			AccantonamentoFondiDubbiaEsigibilitaModelDetail.TipoMedia,
			AccantonamentoFondiDubbiaEsigibilitaModelDetail.Tipo);

		for(AccantonamentoFondiDubbiaEsigibilita afde : req.getListaAccantonamentoFondiDubbiaEsigibilita()) {
			// Caricamento dati salvati
			AccantonamentoFondiDubbiaEsigibilita accantonamentoDB = accantonamentoFondiDubbiaEsigibilitaDad.findById(afde);
			// Ripopolamento dell'accantonamento
			popolaDatiAccantonamento(accantonamentoDB);
			
			//SIAC-8519 non sono ammesse medie ponderate
			accantonamentoDB.setMediaPonderataTotali(null);
			accantonamentoDB.setMediaPonderataRapporti(null);
			
			accantonamentoFondiDubbiaEsigibilitaDad.update(accantonamentoDB);
		}
		if(!Boolean.TRUE.equals(req.getSkipLoadAccantonamenti())) {
			// popolaResponse
			List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> accantonamentiFondiDubbiaEsigibilita = caricaAccantonamenti(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
			res.setListaAccantonamenti(accantonamentiFondiDubbiaEsigibilita);
		}
	}
	
}
