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
import it.csi.siac.siacfinser.business.service.movgest.RicercaImpegniSubimpegniService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacintegser.business.service.ServiceHelper;
import it.csi.siac.siacintegser.business.service.base.RicercaPaginataBaseService;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.movimentoGestione.RicercaImpegno;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.movimentoGestione.RicercaImpegnoResponse;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaImpegnoService extends
RicercaPaginataBaseService<RicercaImpegno, RicercaImpegnoResponse>
{

	@Autowired 
	ServiceHelper serviceHelper;
	
	@Override
	protected RicercaImpegnoResponse execute(RicercaImpegno ireq)
	{
		RicercaImpegniSubImpegni req = map(ireq, RicercaImpegniSubImpegni.class, IntegMapId.RicercaImpegno_RicercaImpegniSubImpegni);
		req.setEnte(ente);
		
		RicercaImpegnoResponse ires = instantiateNewIRes();
		
		if(!StringUtils.isEmpty(ireq.getCodiceStruttura()) && !StringUtils.isEmpty(ireq.getCodiceTipoStruttura())){
			StrutturaAmministrativoContabile sac = serviceHelper.ricercaStrutturaByCodice(ente,richiedente,ireq.getCodiceStruttura(), ireq.getCodiceTipoStruttura());
			if(sac ==null){
				addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice e il tipo della struttura non esistono");
				return ires;
			}else req.getParametroRicercaImpSub().setUidStrutturaAmministrativoContabile(sac.getUid());
		}
		
		TipoAtto tipoAtto = serviceHelper.ricercaTipoProvvedimentoByCodice(ente, richiedente, ireq.getCodiceTipoProvvedimento());
		if(tipoAtto ==null){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice del provvedimento non esiste");
			return ires;
		}else req.getParametroRicercaImpSub().setTipoProvvedimento(tipoAtto);
		

		RicercaImpegniSubimpegniResponse res = appCtx.getBean(RicercaImpegniSubimpegniService.class).executeService(req);
		checkBusinessServiceResponse(res);
		
		if( res.getListaImpegni()==null ||  res.getListaImpegni().isEmpty()){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " nessun filtro di ricerca soddisfatto");
			return ires;
		}
		
		List<Impegno> elencoImpegni = res.getListaImpegni();
				
		List<it.csi.siac.siacintegser.model.integ.Impegno> result = dozerUtil.mapList(elencoImpegni, it.csi.siac.siacintegser.model.integ.Impegno.class, IntegMapId.ListImpegno_IntegImpegno);
		//ires.setEnte(dozerUtil.map(req.getEnte(), Ente.class));
		ires.setTotaleRisultati(res.getNumRisultati());
		ires.setImpegni(result);
		
		return ires;
	}


	@Override
	protected void checkServiceParameters(RicercaImpegno ireq) throws ServiceParamError {		
		
		// controllo parametri in input
		// se ho il provvedimento è sufficiente per eseguire la ricerca
		// altrimenti diventa obbligatorio il numero impegno 
		checkCondition((ireq.getNumeroImpegno() != null && ireq.getAnnoImpegno() != null) || ireq.getNumeroProvvedimento() != null
				&& ireq.getAnnoProvvedimento() != null && ireq.getCodiceTipoProvvedimento()!=null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("numero e anno impegno"));
		
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
		
		checkCondition((ireq.getCodiceStruttura()!=null || ireq.getCodiceTipoStruttura()==null) && 
				(ireq.getCodiceStruttura()==null || ireq.getCodiceTipoStruttura()!=null),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice struttura / codice tipo struttura"));	

	}	

}
