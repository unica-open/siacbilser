/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.movimentoGestione;

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
import it.csi.siac.siacfinser.business.service.movgest.RicercaAccertamentiSubAccertamentiService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamentiResponse;
import it.csi.siac.siacintegser.business.service.ServiceHelper;
import it.csi.siac.siacintegser.business.service.base.RicercaPaginataBaseService;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.movimentoGestione.RicercaAccertamento;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.movimentoGestione.RicercaAccertamentoResponse;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAccertamentoService extends
		RicercaPaginataBaseService<RicercaAccertamento, RicercaAccertamentoResponse>
{
	
	@Autowired 
	ServiceHelper serviceHelper;

	@Override
	protected RicercaAccertamentoResponse execute(RicercaAccertamento ireq)
	{
		RicercaAccertamentiSubAccertamenti req = map(ireq, RicercaAccertamentiSubAccertamenti.class, IntegMapId.RicercaAccertamento_RicercaAccertamentiSubAccertamenti);
		req.setEnte(ente);
		
		RicercaAccertamentoResponse ires = instantiateNewIRes();
		
		if(!StringUtils.isEmpty(ireq.getCodiceStruttura()) && !StringUtils.isEmpty(ireq.getCodiceTipoStruttura())){
			StrutturaAmministrativoContabile sac = serviceHelper.ricercaStrutturaByCodice(ente, richiedente, ireq.getCodiceStruttura(), ireq.getCodiceTipoStruttura());
			if(sac ==null){
				addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice e il tipo della struttura non esistono");
				return ires;
			}else req.getParametroRicercaAccSubAcc().setUidStrutturaAmministrativoContabile(sac.getUid());
		}
		
		TipoAtto tipoAtto = serviceHelper.ricercaTipoProvvedimentoByCodice(ente, richiedente, ireq.getCodiceTipoProvvedimento());
		if(tipoAtto ==null){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice del provvedimento non esiste");
			return ires;
		}else req.getParametroRicercaAccSubAcc().setTipoProvvedimento(tipoAtto);
		

		RicercaAccertamentiSubAccertamentiResponse res = appCtx.getBean(RicercaAccertamentiSubAccertamentiService.class).executeService(req);
		checkBusinessServiceResponse(res);
		
		if(res.getListaAccertamenti()==null || res.getListaAccertamenti().isEmpty()){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "nessun filtro di ricerca soddisfatto");
			return ires;
		}
		
		List<it.csi.siac.siacfinser.model.Accertamento> elencoAccertamenti = res.getListaAccertamenti();
		
		List<it.csi.siac.siacintegser.model.integ.Accertamento> result = dozerUtil.mapList(elencoAccertamenti, it.csi.siac.siacintegser.model.integ.Accertamento.class, IntegMapId.ListAccertamento_IntegAccertamento);
		//ires.setEnte(dozerUtil.map(req.getEnte(), Ente.class));
		ires.setTotaleRisultati(res.getNumRisultati());
		ires.setAccertamenti(result);
		
		
		return ires;
	}
	
	
	@Override
	protected void checkServiceParameters(RicercaAccertamento ireq) throws ServiceParamError {		
		
		// controllo parametri in input
		// se ho il provvedimento è sufficiente per eseguire la ricerca
		// altrimenti diventa obbligatorio il numero impegno 
		checkCondition((ireq.getNumeroAccertamento() != null && ireq.getAnnoAccertamento() != null) || ireq.getNumeroProvvedimento() != null
				&& ireq.getAnnoProvvedimento() != null && ireq.getCodiceTipoProvvedimento()!=null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("numero e anno accertamento"));
		
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
