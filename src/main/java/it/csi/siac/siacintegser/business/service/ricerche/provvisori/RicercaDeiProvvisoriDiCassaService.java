/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.provvisori;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.provvisorio.RicercaProvvisoriDiCassaService;
import it.csi.siac.siacfinser.business.service.provvisorio.RicercaProvvisorioDiCassaPerChiaveService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;
import it.csi.siac.siacintegser.business.entitymapping.ProvvisoriDiCassaIntegToFinConverter;
import it.csi.siac.siacintegser.business.service.ServiceHelper;
import it.csi.siac.siacintegser.business.service.base.RicercaPaginataBaseService;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.provvisori.RicercaProvvisoriDiCassa;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.provvisori.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacintegser.model.enumeration.TipoProvvisorioDiCassa;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDeiProvvisoriDiCassaService extends
		RicercaPaginataBaseService<RicercaProvvisoriDiCassa, RicercaProvvisoriDiCassaResponse>
{

	@Autowired 
	ServiceHelper serviceHelper;
	
	@Override
	protected RicercaProvvisoriDiCassaResponse execute(RicercaProvvisoriDiCassa ireq) 
	{	
		
		RicercaProvvisoriDiCassaResponse ires = null;
		
		if ( shouldUseFindByNumero(ireq) ) {
			ires = findByNumero(ireq);
		} else {
			ires = findByFiltro(ireq);
		}
		
		return ires;
	}
	
	protected boolean shouldUseFindByNumero(RicercaProvvisoriDiCassa ireq) {
		if ( ireq.getNumeroProvvisorio() != null
				&& StringUtils.isNotEmpty( ireq.getDescrizioneCausale() )
				&& ireq.getDataA() != null
				&& ireq.getDataDa() != null
				&& ireq.getImportoA() != null
				&& ireq.getImportoDa() != null) {			
			return true;
		}
				
		return false;
	}
	
	
	protected RicercaProvvisoriDiCassaResponse findByFiltro(RicercaProvvisoriDiCassa ireq)
	{
				
		// CAMPI MAPPABILI CON DOZER:
		it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa req = map(ireq, it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa.class, IntegMapId.RicercaProvvisoriDiCassa_IntegRicercaProvvisoriDiCassa);
		
		// CAMPI PIU' COMPLESSI:
		req = ProvvisoriDiCassaIntegToFinConverter.requestIntegToRequestFin(ireq, req);
		
		req.setEnte(ente);
		
		RicercaProvvisoriDiCassaResponse ires = instantiateNewIRes();

		it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse res = appCtx.getBean(RicercaProvvisoriDiCassaService.class).executeService(req);
		checkBusinessServiceResponse(res);
		
		if( res.getElencoProvvisoriDiCassa()==null ||  res.getElencoProvvisoriDiCassa().isEmpty() ) {
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " nessun filtro di ricerca soddisfatto");
			return ires;
		}
		
		List<ProvvisorioDiCassa> elencoProvvisori = res.getElencoProvvisoriDiCassa();
				
		List<it.csi.siac.siacintegser.model.integ.ProvvisorioDiCassa> result = dozerUtil.mapList(elencoProvvisori, it.csi.siac.siacintegser.model.integ.ProvvisorioDiCassa.class, IntegMapId.ListProvvisorioDiCassa_IntegImpegnoProvvisorioDiCassa);
		
		//ires.setEnte(dozerUtil.map(req.getEnte(), Ente.class));
		ires.setTotaleRisultati(res.getNumRisultati());
		ires.setProvvisoriDiCassa(result);
		
		return ires;
	}

	private	RicercaProvvisoriDiCassaResponse findByNumero(RicercaProvvisoriDiCassa ireq)
	{
		// mappa la richiesta del wrapper con quella da fare ad i servizi di backend
//		RicercaProvvisorioDiCassaPerChiave beRequest = map(ireq, RicercaProvvisorioDiCassaPerChiave.class, IntegMapId.RicercaProvvisoriDiCassaPerChiave_IntegRicercaProvvisoriDiCassa);
				
		RicercaProvvisorioDiCassaPerChiave beRequest = new RicercaProvvisorioDiCassaPerChiave();

		{ 	// istanzia i parametri per la ricerca
			RicercaProvvisorioDiCassaK pk = new RicercaProvvisorioDiCassaK();
			// 
			pk.setAnnoProvvisorioDiCassa( ireq.getAnnoBilancio() );
			pk.setNumeroProvvisorioDiCassa( ireq.getNumeroProvvisorio() );
			
			TipoProvvisorioDiCassa tipo = ireq.getTipoProvvisorioDiCassa();
			
			if ( tipo == TipoProvvisorioDiCassa.ENTRATA ) {
			 	pk.setTipoProvvisorioDiCassa(ProvvisorioDiCassa.TipoProvvisorioDiCassa.E);
			}
			 
			if  ( tipo == TipoProvvisorioDiCassa.SPESA ) {
			 	pk.setTipoProvvisorioDiCassa(ProvvisorioDiCassa.TipoProvvisorioDiCassa.S);
			}
			
			beRequest.setpRicercaProvvisorioK(pk);
		}
		
		beRequest.setEnte(ente);
		beRequest.setRichiedente(richiedente);
		
		if ( log.isDebugEnabled() ) {
			log.logXmlTypeObject(beRequest, "beRequest di findByNumero");
		}
		
		RicercaProvvisorioDiCassaPerChiaveResponse resp = appCtx.getBean(RicercaProvvisorioDiCassaPerChiaveService.class).executeService(beRequest);

		checkBusinessServiceResponse(resp);
				
		RicercaProvvisoriDiCassaResponse ires = instantiateNewIRes();
				
		// check backend reponse
		if ( resp.getProvvisorioDiCassa() == null ) {
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO,"");
			return ires;
		}
		
		
		// mappa (il solo) provvisorio di cassa presente nella response
		List<it.csi.siac.siacintegser.model.integ.ProvvisorioDiCassa> mappedList = dozerUtil.mapList( Collections.singletonList(resp.getProvvisorioDiCassa()),
			it.csi.siac.siacintegser.model.integ.ProvvisorioDiCassa.class, 
			IntegMapId.ListProvvisorioDiCassa_IntegImpegnoProvvisorioDiCassa);
			
		ires.setProvvisoriDiCassa(mappedList);
		
		// TODO da chiedere se il valore 1 e' corretto 					
		ires.setTotaleRisultati(1);

		return ires; 
	}  

	@Override
	protected void checkServiceParameters(RicercaProvvisoriDiCassa ireq) throws ServiceParamError {		

//		// 1. Controlli sul tipo provvisorio:
//		TipoProvvisorioDiCassa tipoProvvisorio = ireq.getTipoProvvisorioDiCassa();		
//		boolean provvisorioOK = true;
//		if ( tipoProvvisorio!=null ) {
//			//se indicato deve essere delle tipologie accettabili:
//			if ( !tipoProvvisorio.equals(TipoProvvisorioDiCassa.ENTRATA) && !tipoProvvisorio.equals(TipoProvvisorioDiCassa.SPESA) ) {
//				provvisorioOK = false;
//			}
//		} else {
//			provvisorioOK = false;
//		}			
//		checkCondition(provvisorioOK, ErroreCore.PARAMETRO_ERRATO.getErrore("TipoProvvisorio",ireq.getTipoProvvisorioDiCassa(),"ENTRATA o SPESA"));
		
		TipoProvvisorioDiCassa tipoProvvisorio = ireq.getTipoProvvisorioDiCassa();		
		
		checkCondition(
				tipoProvvisorio != null
				, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("TipoProvvisorio") );
		
		checkCondition( tipoProvvisorio != null && 
				( TipoProvvisorioDiCassa.ENTRATA.equals(tipoProvvisorio) 
				|| TipoProvvisorioDiCassa.SPESA.equals(tipoProvvisorio) )
				, ErroreCore.PARAMETRO_ERRATO.getErrore("TipoProvvisorio","","ENTRATA o SPESA"));
		
		checkCondition(
				ireq.getAnnoBilancio() != null
				, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annoBilancio"));
		
//		// controlla che siano specificati entrambi gli estremi dei range sulle date di emissione!?
//		if ( ireq.getDataA() != null || ireq.getDataDa() != null ) {
//			checkCondition(ireq.getDataA() != null && ireq.getDataDa() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dataA o dataDa"));
//		}
		
		checkCondition( false
				|| ireq.getNumeroProvvisorio() != null
				|| StringUtils.isNotEmpty( ireq.getDescrizioneCausale() )
				|| ireq.getDataA() != null
				|| ireq.getDataDa() != null
				|| ireq.getImportoA() != null
				|| ireq.getImportoDa() != null
				, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("E' richiesto almeno un parametro di ricerca tra: numeroProvvisorio, descrizioneCausale, dataDa, dataA, importoDa, importoA"));		
	}	
}
