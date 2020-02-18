/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.liquidazione;

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
import it.csi.siac.siacfinser.business.service.liquidazione.RicercaLiquidazioniService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioniResponse;
import it.csi.siac.siacintegser.business.service.ServiceHelper;
import it.csi.siac.siacintegser.business.service.base.RicercaPaginataBaseService;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.liquidazione.RicercaLiquidazione;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.liquidazione.RicercaLiquidazioneResponse;
import it.csi.siac.siacintegser.model.integ.Liquidazione;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaLiquidazioneService extends
		RicercaPaginataBaseService<RicercaLiquidazione, RicercaLiquidazioneResponse>
{

	@Autowired
	ServiceHelper serviceHelper;
	
	@Override
	protected RicercaLiquidazioneResponse execute(RicercaLiquidazione ireq)
	{
		RicercaLiquidazioni req = map(ireq,
				RicercaLiquidazioni.class, IntegMapId.RicercaLiquidazione_RicercaLiquidazioni);
		req.setEnte(ente);
		
		RicercaLiquidazioneResponse ires = instantiateNewIRes();
		
		if(!StringUtils.isEmpty(ireq.getCodiceStruttura()) && !StringUtils.isEmpty(ireq.getCodiceTipoStruttura())){
			StrutturaAmministrativoContabile sac = serviceHelper.ricercaStrutturaByCodice(ente,richiedente,ireq.getCodiceStruttura(), ireq.getCodiceTipoStruttura());
			if(sac ==null){
				addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice e il tipo della struttura non esistono");
				return ires;
			}else req.getParametroRicercaLiquidazione().setUidStrutturaAmministrativaProvvedimento(sac.getUid());
		}
		
		TipoAtto tipoAtto = serviceHelper.ricercaTipoProvvedimentoByCodice(ente, richiedente, ireq.getCodiceTipoProvvedimento());
		if(tipoAtto ==null){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice del provvedimento non esiste");
			return ires;
		}else req.getParametroRicercaLiquidazione().setTipoProvvedimento(String.valueOf(tipoAtto.getUid()));
		
		
		RicercaLiquidazioniResponse res = appCtx.getBean(RicercaLiquidazioniService.class).executeService(req);
		
		checkBusinessServiceResponse(res);
		
		List<Liquidazione> elencoLiquidazioni = dozerUtil.mapList(res.getElencoLiquidazioni(),Liquidazione.class, IntegMapId.ListLiquidazione_IntegLiquidazione);
		
		if(elencoLiquidazioni==null || elencoLiquidazioni.isEmpty()){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " nessun filtro di ricerca soddisfatto");
			return ires;
		}
		
		//ires.setEnte(dozerUtil.map(req.getEnte(), Ente.class));
		ires.setLiquidazioni(elencoLiquidazioni);
		ires.setTotaleRisultati(elencoLiquidazioni.size());

		return ires;
	}

	
	@Override
	protected void checkServiceParameters(RicercaLiquidazione ireq) throws ServiceParamError {		
		
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
