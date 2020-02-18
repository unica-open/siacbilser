/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.ordinativo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.ordinativo.RicercaEstesaOrdinativiPagamentoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaEstesaOrdinativiPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaEstesaOrdinativiPagamentoResponse;
import it.csi.siac.siacfinser.model.ordinativo.RicercaEstesaOrdinativoDiPagamento;
import it.csi.siac.siacintegser.business.service.ServiceHelper;
import it.csi.siac.siacintegser.business.service.base.IntegBaseService;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.ordinativo.RicercaEstesaOrdinativiSpesa;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.ordinativo.RicercaEstesaOrdinativiSpesaResponse;
import it.csi.siac.siacintegser.model.integ.MandatoDiPagamento;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaEstesaOrdinativiSpesaService extends
		IntegBaseService<RicercaEstesaOrdinativiSpesa, RicercaEstesaOrdinativiSpesaResponse>
{

	@Autowired
	ServiceHelper serviceHelper;
	
	@Override
	protected RicercaEstesaOrdinativiSpesaResponse execute(RicercaEstesaOrdinativiSpesa ireq)
	{
		RicercaEstesaOrdinativiPagamento req = map(ireq,
				RicercaEstesaOrdinativiPagamento.class, IntegMapId.RicercaEstesaOrdinativiSpesa_RicercaEstesaOrdinativiPagamento);
		
		req.setEnte(ente);
		
		RicercaEstesaOrdinativiSpesaResponse ires = instantiateNewIRes();
		
		
		if(!StringUtils.isEmpty(ireq.getCodiceStruttura()) && !StringUtils.isEmpty(ireq.getCodiceTipoStruttura())){
			StrutturaAmministrativoContabile sac = serviceHelper.ricercaStrutturaByCodice(ente,richiedente,ireq.getCodiceStruttura(), ireq.getCodiceTipoStruttura());
			if(sac ==null || sac.getUid()<=0){
				addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice e il tipo della struttura non esistono");
				return ires;
			}else req.getAtto().setStrutturaAmmContabile(sac);
			
		}
		
		TipoAtto tipoAtto = serviceHelper.ricercaTipoProvvedimentoByCodice(ente,richiedente, ireq.getCodiceTipoProvvedimento());
		if(tipoAtto ==null){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice del provvedimento non esiste");
			return ires;
		}else req.getAtto().setTipoAtto(tipoAtto);
		
					
		RicercaEstesaOrdinativiPagamentoResponse res = appCtx.getBean(RicercaEstesaOrdinativiPagamentoService.class).executeService(req);
		checkBusinessServiceResponse(res);
		
		if(res.getOrdinativiPagamento()==null || res.getOrdinativiPagamento().isEmpty()){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " nessun filtro di ricerca soddisfatto");
			return ires;
		}
		
		List<RicercaEstesaOrdinativoDiPagamento> elencoOrdinativiPagamento = res.getOrdinativiPagamento();
		
		List<MandatoDiPagamento> mandatiDiPagamento = dozerUtil.mapList(elencoOrdinativiPagamento, MandatoDiPagamento.class, IntegMapId.ListRicercaEstesaOrdinativoDiPagamento_MandatoDiPagamento);
		
		ires.setNumeroTotaleOrdinativiSpesaTrovati(elencoOrdinativiPagamento.size());
		ires.setMandatiDiPagamento(mandatiDiPagamento);
		
		return ires;
		
	}


	@Override
	protected void checkServiceParameters(RicercaEstesaOrdinativiSpesa ireq) throws ServiceParamError {		
		
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
