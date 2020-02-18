/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.RicercaProvvisoriPagoPARequest;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.RicercaProvvisoriPagoPARequest.ElencoCausaliVersamenti;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.RicercaProvvisoriPagoPAResponse;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.RicercaProvvisoriPagoPAResponse.ElencoProvvisori;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.StatoProvvisorioType;
import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.business.service.pagopa.msg.RicercaProvvisori;
import it.csi.siac.siacbilser.business.service.pagopa.msg.RicercaProvvisoriResponse;
import it.csi.siac.siacbilser.business.service.pagopa.util.PagoPAUtils;
import it.csi.siac.siacbilser.business.service.pagopa.util.ResultTypeEnum;
import it.csi.siac.siacbilser.business.service.pagopa.util.SiacTProvCassaProvvisorioTypeConverter;
import it.csi.siac.siacbilser.integration.dad.PagoPADad;
import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaProvvisoriService extends ExtendedBaseService<RicercaProvvisori, RicercaProvvisoriResponse> {

	private static LogUtil log = new LogUtil(RicercaProvvisoriService.class);
	
	@Autowired
	private PagoPADad pagoPADad;
	
	@Autowired
	private SiacTProvCassaProvvisorioTypeConverter siacTProvCassaProvvisorioTypeConverter;
	
	@Override
	protected void checkRichiedente() throws ServiceParamError {
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getPagoPARequest(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PagoPA request"));
		
		RicercaProvvisoriPagoPARequest pagoPAReq = req.getPagoPARequest();
		
		checkCondition(StringUtils.isNotBlank(pagoPAReq.getCFEnteCreditore()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("CF ente creditore"));
		
		checkCondition(pagoPAReq.getAnnoEsercizio() > 0 ||
					   isElencoCausaliNotBlank(pagoPAReq.getElencoCausaliVersamenti()) || 
					   pagoPAReq.getNumeroProvvisorioDal() != null ||
					   pagoPAReq.getNumeroProvvisorioAl() != null ||
					   pagoPAReq.getDataProvvisorioDal() != null ||
					   pagoPAReq.getDataProvvisorioAl() != null ||
					   pagoPAReq.getStato() != null,
					   ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("CF ente creditore")					   
		);
	}
	
	private boolean isElencoCausaliNotBlank(ElencoCausaliVersamenti elencoCausaliVersamenti) {
		
		if (elencoCausaliVersamenti == null || 
			elencoCausaliVersamenti.getCausaleVersamento() == null || 
			elencoCausaliVersamenti.getCausaleVersamento().isEmpty()) {
			
			return false;
		}
		
		for (String causaleVersamento : elencoCausaliVersamenti.getCausaleVersamento()) {
			if (StringUtils.isNotBlank(causaleVersamento)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	@Transactional
	public RicercaProvvisoriResponse executeService(RicercaProvvisori serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	@Override
	protected void init() {
		String codiceFiscaleEnte = req.getPagoPARequest().getCFEnteCreditore();
		
		pagoPADad.initSiacTEnteProprietario(codiceFiscaleEnte);
		pagoPADad.setLoginOperazione(codiceFiscaleEnte);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";  

		RicercaProvvisoriPagoPARequest ppareq = req.getPagoPARequest();
		RicercaProvvisoriPagoPAResponse ricercaProvvisoriPagoPAResponse = new RicercaProvvisoriPagoPAResponse();

		try {
			
			List<SiacTProvCassa> siacTProvCassaList = pagoPADad.ricercaProvvisori(
				ppareq.getAnnoEsercizio() == 0 ? null : Integer.valueOf(ppareq.getAnnoEsercizio()), 
				ppareq.getElencoCausaliVersamenti() == null ? null : ppareq.getElencoCausaliVersamenti().getCausaleVersamento(), 
				ppareq.getNumeroProvvisorioDal(), 
				ppareq.getNumeroProvvisorioAl(), 
				PagoPAUtils.xmlGregorianCalendarToDate(ppareq.getDataProvvisorioDal()),
				PagoPAUtils.xmlGregorianCalendarToDate(ppareq.getDataProvvisorioAl()),
				ppareq.getStato() == null ? null : ppareq.getStato().equals(StatoProvvisorioType.VALIDO));
			
			ElencoProvvisori elencoProvvisori = new ElencoProvvisori();

			for (SiacTProvCassa siacTProvCassa : siacTProvCassaList) {
				elencoProvvisori.getProvvisorio().add(siacTProvCassaProvvisorioTypeConverter.toProvvisorioType(siacTProvCassa));	
			}
			
			ricercaProvvisoriPagoPAResponse.setElencoProvvisori(elencoProvvisori);
			
			ricercaProvvisoriPagoPAResponse.setResult(ResultTypeEnum.DEFAULT_RT000_OK.getResultType());
			res.setEsito(Esito.SUCCESSO);
			
		} catch (BusinessException be) {
			log.error(methodName, be);
			ricercaProvvisoriPagoPAResponse.setResult(ResultTypeEnum.DEFAULT_RT100_ERRORI_APPLICATIVI.getResultType(be.getMessage())); 
			res.setEsito(Esito.FALLIMENTO);
			
		} catch (Throwable t) {
			log.error(methodName, "Errore di sistema ", t);
			ricercaProvvisoriPagoPAResponse.setResult(ResultTypeEnum.DEFAULT_RT200_ERRORI_DI_SISTEMA.getResultType(t.getMessage()));
			res.setEsito(Esito.FALLIMENTO);
			
		} finally {
			res.setPagoPAResponse(ricercaProvvisoriPagoPAResponse);
		}	
	}
}

