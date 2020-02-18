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

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.liquidazione.RicercaEstesaLiquidazioniFinService;
import it.csi.siac.siacfinser.model.liquidazione.LiquidazioneAtti;
import it.csi.siac.siacintegser.business.service.ServiceHelper;
import it.csi.siac.siacintegser.business.service.base.IntegBaseService;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.liquidazione.RicercaEstesaLiquidazioni;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.liquidazione.RicercaEstesaLiquidazioniResponse;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaEstesaLiquidazioniService extends
		IntegBaseService<RicercaEstesaLiquidazioni, RicercaEstesaLiquidazioniResponse>
{

	@Autowired
	ServiceHelper serviceHelper;
	
	
	@Override
	protected RicercaEstesaLiquidazioniResponse execute(RicercaEstesaLiquidazioni ireq)
	{
		
		it.csi.siac.siacfinser.frontend.webservice.msg.RicercaEstesaLiquidazioni req = map(ireq,
				it.csi.siac.siacfinser.frontend.webservice.msg.RicercaEstesaLiquidazioni.class, IntegMapId.RicercaEstesaLiquidazioni_RicercaEstesaLiquidazioniFin);
		
		req.setEnte(ente);
		RicercaEstesaLiquidazioniResponse ires = instantiateNewIRes();
		
		AttoAmministrativo provvedimento = new AttoAmministrativo();
		// Preparo il provvedimento da passare poi alla request del siac che si ricaverà l'uid
		if(!StringUtils.isEmpty(ireq.getCodiceStruttura()) && !StringUtils.isEmpty(ireq.getCodiceTipoStruttura())){
			StrutturaAmministrativoContabile sac = serviceHelper.ricercaStrutturaByCodice(ente,richiedente,ireq.getCodiceStruttura(), ireq.getCodiceTipoStruttura());
			if(sac ==null){
				addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice e il tipo della struttura non esistono");
				return ires;
			}else req.getAtto().setStrutturaAmmContabile(sac);
			
		}
		
		TipoAtto tipoAtto = serviceHelper.ricercaTipoProvvedimentoByCodice(ente, richiedente, ireq.getCodiceTipoProvvedimento());
		if(tipoAtto ==null){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice del provvedimento non esiste");
			return ires;
		}else req.getAtto().setTipoAtto(tipoAtto);
		
		
		it.csi.siac.siacfinser.frontend.webservice.msg.RicercaEstesaLiquidazioniResponse res = appCtx.getBean(RicercaEstesaLiquidazioniFinService.class).executeService(req);
		
		checkBusinessServiceResponse(res);
		
		if(res.getElencoLiquidazioni()==null || res.getElencoLiquidazioni().isEmpty()){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " nessun filtro di ricerca soddisfatto");
			return ires;
		}
		
		List<LiquidazioneAtti> elencoLiquidazioni = res.getElencoLiquidazioni();
		
		List<it.csi.siac.siacintegser.model.integ.LiquidazioneAtti> liquidazioni = dozerUtil.mapList(elencoLiquidazioni, it.csi.siac.siacintegser.model.integ.LiquidazioneAtti.class, IntegMapId.ListLiquidazioni_LiquidazioniAtti);
		
		ires.setNumeroTotaleLiqudazioniTrovate(elencoLiquidazioni.size());
		ires.setLiquidazioni(liquidazioni);
		
		return ires;
	}

	
	@Override
	protected void checkServiceParameters(RicercaEstesaLiquidazioni ireq) throws ServiceParamError {		
		
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
