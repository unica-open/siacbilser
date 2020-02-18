/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.EsitoFlussiPagoPAResponse;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.StatoElaborazioneFlussoType;
import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.business.service.pagopa.msg.LeggiEsitoFlussi;
import it.csi.siac.siacbilser.business.service.pagopa.msg.LeggiEsitoFlussiResponse;
import it.csi.siac.siacbilser.business.service.pagopa.util.ResultTypeEnum;
import it.csi.siac.siacbilser.integration.dad.PagoPADad;
import it.csi.siac.siacbilser.integration.entity.SiacTFilePagopa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDFilePagopaStatoEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiEsitoFlussiService extends ExtendedBaseService<LeggiEsitoFlussi, LeggiEsitoFlussiResponse> {

	private static LogUtil log = new LogUtil(LeggiEsitoFlussiService.class);
	
	@Autowired
	private PagoPADad pagoPADad;
	
	@Override
	protected void checkRichiedente() throws ServiceParamError {
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getPagoPARequest(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PagoPA request"));
		checkCondition(StringUtils.isNotBlank(req.getPagoPARequest().getCFEnteCreditore()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("CF ente creditore"));
		checkCondition(StringUtils.isNotBlank(req.getPagoPARequest().getIdMessaggio()) || 
					   StringUtils.isNotBlank(req.getPagoPARequest().getIdentificativoFlusso()), 
					   ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id messaggio oppure identificativo flusso"));
	}
	
	@Override
	@Transactional
	public LeggiEsitoFlussiResponse executeService(LeggiEsitoFlussi serviceRequest) {
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
		
		EsitoFlussiPagoPAResponse esitoFlussiPagoPAResponse = new EsitoFlussiPagoPAResponse();

		try {
			SiacTFilePagopa siacTFilePagopa = findSiacTFilePagopa();
			
			esitoFlussiPagoPAResponse.setStatoElaborazioneFlusso(siacTFilePagopa == null ? StatoElaborazioneFlussoType.DA_ELABORARE :
					SiacDFilePagopaStatoEnum.toStatoElaborazioneFlussoType(siacTFilePagopa.getSiacDFilePagopaStato().getFilePagopaStatoCode()));
			
			esitoFlussiPagoPAResponse.setDettaglioEsitoElaborazione(siacTFilePagopa == null ? "" :
					(siacTFilePagopa.getSiacDFilePagopaStato().getFilePagopaStatoDesc() + 
					(siacTFilePagopa.getPagopaDRiconciliazioneErrore() == null ? "" :
						" - " + siacTFilePagopa.getPagopaDRiconciliazioneErrore().getCodiceDescrizione())));
			
			esitoFlussiPagoPAResponse.setResult(ResultTypeEnum.DEFAULT_RT000_OK.getResultType());
			res.setEsito(Esito.SUCCESSO);
			
		} catch (Throwable t) {
			log.error(methodName, "Errore di sistema ", t);
			esitoFlussiPagoPAResponse.setResult(ResultTypeEnum.DEFAULT_RT200_ERRORI_DI_SISTEMA.getResultType(t.getMessage()));
			res.setEsito(Esito.FALLIMENTO);
			
		} finally {
			res.setPagoPAResponse(esitoFlussiPagoPAResponse);
		}
	}

	private SiacTFilePagopa findSiacTFilePagopa() {

		if (StringUtils.isNotBlank(req.getPagoPARequest().getIdMessaggio())) {
			return pagoPADad.findSiacTFilePagopaByIdMessaggio(req.getPagoPARequest().getIdMessaggio());
		} 

		if (StringUtils.isNotBlank(req.getPagoPARequest().getIdentificativoFlusso())) {
			return pagoPADad.findSiacTFilePagopaByIdentificativoFlusso(req.getPagoPARequest().getIdentificativoFlusso());
		}
		
		return null;
	}
}

