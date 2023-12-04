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
import it.csi.siac.siacfinser.business.service.ordinativo.RicercaOrdinativoPagamentoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoResponse;
import it.csi.siac.siacintegser.business.service.base.RicercaPaginataBaseService;
import it.csi.siac.siacintegser.business.service.helper.ProvvedimentoServiceHelper;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.ordinativo.RicercaOrdinativoSpesa;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.ordinativo.RicercaOrdinativoSpesaResponse;
import it.csi.siac.siacintegser.model.integ.OrdinativoPagamento;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaOrdinativoSpesaService extends
		RicercaPaginataBaseService<RicercaOrdinativoSpesa, RicercaOrdinativoSpesaResponse>
{

	@Autowired private ProvvedimentoServiceHelper provvedimentoServiceHelper;
	
	
	@Override
	protected RicercaOrdinativoSpesaResponse execute(RicercaOrdinativoSpesa ireq)
	{
		RicercaOrdinativo req = map(ireq,
				RicercaOrdinativo.class, IntegMapId.RicercaOrdinativoSpesa_RicercaOrdinativo);
		req.setEnte(ente);
		req.getParametroRicercaOrdinativoPagamento().setCaricaLiquidazione(true);
		
		RicercaOrdinativoSpesaResponse ires = instantiateNewIRes();
		
		TipoAtto tipoAtto = provvedimentoServiceHelper.findTipoAttoByCodice(ente,richiedente, ireq.getCodiceTipoProvvedimento());
		if(tipoAtto ==null){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice del provvedimento non esiste");
			return ires;
		}else req.getParametroRicercaOrdinativoPagamento().setTipoProvvedimento(String.valueOf(tipoAtto.getUid()));
		
			
		//FIXME: la struttura nella ricerca dell'ordinativo (del back-end del siac) non viene gestita 
			
		RicercaOrdinativoResponse res = appCtx.getBean(RicercaOrdinativoPagamentoService.class).executeService(req);
		checkServiceResponse(res);
		
		List<OrdinativoPagamento> elencoOrdinativi = dozerUtil.mapList(res.getElencoOrdinativoPagamento(),OrdinativoPagamento.class, IntegMapId.ListOrdinativoPagamento_IntegOrdinativoSpesa);
		if(elencoOrdinativi==null || elencoOrdinativi.isEmpty()){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " nessun filtro di ricerca soddisfatto");
			return ires;
		}
		
		//ires.setEnte(dozerUtil.map(req.getEnte(), Ente.class));
		ires.setOrdinativiPagamento(elencoOrdinativi);
		ires.setTotaleRisultati(res.getNumRisultati());

		return ires;
	}
	
	
	@Override
	protected void checkServiceParameters(RicercaOrdinativoSpesa ireq) throws ServiceParamError {		
		
		// controllo parametri in input
		checkParamCondition(
				ireq.getNumeroProvvedimento() == null || ireq.getAnnoProvvedimento() != null ||
				ireq.getCodiceTipoProvvedimento() == null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno provvedimento"));

		checkParamCondition(
				ireq.getNumeroProvvedimento() != null || ireq.getAnnoProvvedimento() == null || 
				ireq.getCodiceTipoProvvedimento()  == null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero provvedimento"));	
		
		checkParamCondition(
				ireq.getNumeroProvvedimento() == null || ireq.getAnnoProvvedimento() == null || 
				ireq.getCodiceTipoProvvedimento()  != null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo provvedimento"));	
		
		checkParamCondition(
				ireq.getCodiceStruttura() == null || ireq.getCodiceTipoStruttura() != null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo struttura"));	
		
		checkParamCondition(
				ireq.getCodiceStruttura() != null || ireq.getCodiceTipoStruttura() == null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice struttura"));	
	}	

}
