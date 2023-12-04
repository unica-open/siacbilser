/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.integ.business.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.custom.stilo.business.service.movimentogestione.RicercaMovimentoGestioneService;
import it.csi.siac.custom.stilo.frontend.webservice.msg.RicercaMovimentoGestione;
import it.csi.siac.custom.stilo.frontend.webservice.msg.RicercaMovimentoGestioneResponse;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.base.IntegBaseService;
import it.csi.siac.siacintegser.business.service.helper.ProvvedimentoServiceHelper;
import it.csi.siac.siacintegser.business.service.helper.StrutturaAmministrativoContabileServiceHelper;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.stilo.RicercaMovimentoGestioneStilo;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.stilo.RicercaMovimentoGestioneStiloResponse;
import it.csi.siac.siacintegser.model.custom.stilo.MovimentoGestioneStilo;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaMovimentoGestioneStiloService extends
		IntegBaseService<RicercaMovimentoGestioneStilo, RicercaMovimentoGestioneStiloResponse> {

	@Autowired private ProvvedimentoServiceHelper provvedimentoServiceHelper;
	@Autowired private StrutturaAmministrativoContabileServiceHelper strutturaAmministrativoContabileServiceHelper;

	@Override
	protected void checkServiceParameters(RicercaMovimentoGestioneStilo ireq) throws ServiceParamError {
		
		
		// controllo parametri in input
		checkParamCondition(
				ireq.getAnnoBilancio() != null &&  ireq.getAnnoBilancio() !=0 ,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
		
		checkParamCondition(
				ireq.getNumeroProvvedimento() == null || (ireq.getAnnoProvvedimento() != null && ireq.getAnnoProvvedimento() !=0) ||
				ireq.getCodiceTipoProvvedimento() == null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno provvedimento"));

		checkParamCondition(
				(ireq.getNumeroProvvedimento() != null && ireq.getNumeroProvvedimento() !=0) || ireq.getAnnoProvvedimento() == null || 
				ireq.getCodiceTipoProvvedimento()  == null,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero provvedimento"));	
		
		checkParamCondition(
				ireq.getNumeroProvvedimento() == null || ireq.getAnnoProvvedimento() == null || 
				(ireq.getCodiceTipoProvvedimento()  != null && ireq.getCodiceTipoProvvedimento().length()>0),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo provvedimento"));	
		
		checkParamCondition(
				(ireq.getCodiceStruttura() == null || ireq.getCodiceStruttura().length()==0) || (ireq.getCodiceTipoStruttura() != null && ireq.getCodiceTipoStruttura().length()>0),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo struttura"));	
		
		checkParamCondition(
				(ireq.getCodiceStruttura() != null && ireq.getCodiceStruttura().length()>0) || (ireq.getCodiceTipoStruttura() == null || ireq.getCodiceTipoStruttura().length()==0),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice struttura"));	
	
	}	

	
	@Override
	protected RicercaMovimentoGestioneStiloResponse execute(RicercaMovimentoGestioneStilo ireq) {
			
		RicercaMovimentoGestione req = new RicercaMovimentoGestione();
		req.setEnteProprietarioId(ente.getUid());
		req.setRichiedente(richiedente);
		RicercaMovimentoGestioneStiloResponse ires = instantiateNewIRes();
		StrutturaAmministrativoContabile sac = strutturaAmministrativoContabileServiceHelper.findStrutturaAmministrativoContabileByCodice(ente,richiedente,ireq.getCodiceStruttura(), ireq.getCodiceTipoStruttura());
		if(!StringUtils.isEmpty(ireq.getCodiceStruttura()) && !StringUtils.isEmpty(ireq.getCodiceTipoStruttura())){
			if(sac ==null){
				addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice e il tipo della struttura non esistono");
				return ires;
			}
			
		}
		
		TipoAtto tipoAtto = provvedimentoServiceHelper.findTipoAttoByCodice(ente, richiedente, ireq.getCodiceTipoProvvedimento());
		if(tipoAtto ==null || tipoAtto.getUid()==0){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "il codice del provvedimento non esiste");
			return ires;
		}
		
		
		req.setAnnoBilancio(ireq.getAnnoBilancio());
		
		//PROVVEDIMENTO
		req.setAnnoProvvedimento(ireq.getAnnoProvvedimento().toString());
		req.setNumeroProvvedimento(ireq.getNumeroProvvedimento());
		req.setTipoProvvedimento(tipoAtto.getUid());
		//SAC
		req.setStrutturaAmministrativoContabileProvvedimento((sac!= null && sac.getUid() != 0) ? sac.getUid() : null );
		RicercaMovimentoGestioneResponse res = appCtx.getBean(RicercaMovimentoGestioneService.class).executeService(req);
		List<MovimentoGestioneStilo> movimentiGestione = 
				dozerUtil.mapList(res.getMovimentiGestione(), MovimentoGestioneStilo.class, IntegMapId.ListMovimentoGestione_MovimentoGestioneStilo);
		
		ires.setMovimentiGestione(movimentiGestione);
		return ires;
	}

}
