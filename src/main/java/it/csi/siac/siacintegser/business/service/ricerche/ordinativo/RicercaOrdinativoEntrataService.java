/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.ordinativo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoResponse;
import it.csi.siac.siacintegser.business.service.ServiceHelper;
import it.csi.siac.siacintegser.business.service.base.RicercaPaginataBaseService;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.ordinativo.RicercaOrdinativoIncasso;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.ordinativo.RicercaOrdinativoIncassoResponse;
import it.csi.siac.siacintegser.model.integ.OrdinativoIncasso;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaOrdinativoEntrataService extends
		RicercaPaginataBaseService<RicercaOrdinativoIncasso, RicercaOrdinativoIncassoResponse>
{
	
	@Autowired
	ServiceHelper serviceHelper;

	@Override
	protected RicercaOrdinativoIncassoResponse execute(RicercaOrdinativoIncasso ireq)
	{
		RicercaOrdinativo req = map(ireq,
				RicercaOrdinativo.class, IntegMapId.RicercaOrdinativoIncasso_RicercaOrdinativo);
		req.setEnte(ente);
		
		RicercaOrdinativoIncassoResponse ires = instantiateNewIRes();
		
		TipoAtto tipoAtto = serviceHelper.ricercaTipoProvvedimentoByCodice(ente,richiedente, ireq.getCodiceTipoProvvedimento());
		if(tipoAtto ==null){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice del provvedimento non esiste");
			return ires;
		}else req.getParametroRicercaOrdinativoIncasso().setTipoProvvedimento(String.valueOf(tipoAtto.getUid()));
		
		//FIXME: la struttura nella ricerca dell'ordinativo (del back-end del siac) non viene gestita 
		
		RicercaOrdinativoResponse res = appCtx.getBean(it.csi.siac.siacfinser.business.service.ordinativo.RicercaOrdinativoIncassoService.class).executeService(req);
		checkBusinessServiceResponse(res);
		
		List<OrdinativoIncasso> elencoOrdinativi = dozerUtil.mapList(res.getElencoOrdinativoIncasso(),OrdinativoIncasso.class, IntegMapId.ListOrdinativoIncasso_IntegOrdinativoIncasso);
		if(elencoOrdinativi==null || elencoOrdinativi.isEmpty()){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " nessun filtro di ricerca soddisfatto");
			return ires;
		}
					
		//ires.setEnte(dozerUtil.map(req.getEnte(), Ente.class));
		ires.setOrdinativiIncasso(elencoOrdinativi);
		ires.setTotaleRisultati(res.getNumRisultati());

		return ires;
	}

	
	@Override
	protected void checkServiceParameters(RicercaOrdinativoIncasso ireq) throws ServiceParamError {		
		
		// controllo parametri in input
		checkCondition(
				ireq.getNumeroProvvedimento() == null || ireq.getAnnoProvvedimento() != null ||
				ireq.getCodiceTipoProvvedimento() == null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno provvedimento"));

		checkCondition(
				ireq.getNumeroProvvedimento() != null || ireq.getAnnoProvvedimento() == null || 
				ireq.getCodiceTipoProvvedimento()  == null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero provvedimento"));	
		
		checkCondition(
				ireq.getNumeroProvvedimento() == null || ireq.getAnnoProvvedimento() == null || 
				ireq.getCodiceTipoProvvedimento()  != null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo provvedimento"));	
		
		checkCondition(
				ireq.getCodiceStruttura() == null || ireq.getCodiceTipoStruttura() != null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo struttura"));	
		
		checkCondition(
				ireq.getCodiceStruttura() != null || ireq.getCodiceTipoStruttura() == null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice struttura"));	
	}	
}
