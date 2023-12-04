/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class RicercaQuotaSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaQuotaSpesaService extends CheckedAccountBaseService<RicercaQuotaSpesa, RicercaQuotaSpesaResponse> {

	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		boolean altriCampiNull = (req.getUidElenco() == null || req.getUidElenco() == 0) && req.getAnnoElenco() == null && req.getNumeroElenco() == null 
				&& req.getAnnoProvvisorio() == null && req.getNumeroProvvisorio() == null && req.getDataProvvisorio() == null
				&& req.getAnnoDocumento() == null && (req.getNumeroDocumento() == null || StringUtils.isBlank(req.getNumeroDocumento())) && req.getNumeroQuota() == null
				&& req.getNumeroMovimento() == null && req.getAnnoMovimento() == null
				&& (req.getUidProvvedimento() == null || req.getUidProvvedimento() == 0) && req.getAnnoProvvedimento() == null && req.getNumeroProvvedimento() == null
				&& req.getTipoAtto() == null && req.getStruttAmmContabile() == null;
//				&& (req.getStatiOperativoDocumento() == null || req.getStatiOperativoDocumento().isEmpty());
		
		boolean soloDataOSoggettoOTipoValorizzatoONessuno = (req.getDataEmissioneDocumento() != null && req.getSoggetto() == null && req.getTipoDocumento() == null) || 
				(req.getTipoDocumento() != null && req.getSoggetto() == null &&  req.getDataEmissioneDocumento() == null) ||
				(req.getSoggetto() != null && req.getTipoDocumento() == null && req.getDataEmissioneDocumento() == null) ||
				(req.getSoggetto() == null && req.getTipoDocumento() == null && req.getDataEmissioneDocumento() == null);
		
		checkCondition(!(soloDataOSoggettoOTipoValorizzatoONessuno && altriCampiNull), ErroreCore.RICERCA_TROPPO_ESTESA.getErrore());

		checkCondition(!(req.getNumeroProvvedimento() == null ^ req.getAnnoProvvedimento() == null),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno provvedimento o numero provvedimento"));
		checkCondition(!(req.getAnnoElenco() == null ^ req.getNumeroElenco() == null),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno elenco o numero elenco"));
		checkCondition(!(req.getAnnoDocumento() == null ^ (req.getNumeroDocumento() == null || StringUtils.isBlank(req.getNumeroDocumento()))),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento o numero documento"));
		checkCondition(!(req.getNumeroMovimento() == null ^ req.getAnnoMovimento() == null),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno movimento gestione o numero movimento gestione"));
		
		checkCondition((req.getNumeroProvvisorio() != null && req.getAnnoProvvisorio() != null && req.getDataProvvisorio() != null) ||
				(req.getNumeroProvvisorio() == null && req.getAnnoProvvisorio() == null && req.getDataProvvisorio() == null) ||
				(req.getNumeroProvvisorio() != null && req.getAnnoProvvisorio() != null) ||
				(req.getNumeroProvvisorio() != null && req.getDataProvvisorio() != null),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno, numero o data provvedimento"));
		
		checkParametriPaginazione(req.getParametriPaginazione());
		
		if(Boolean.TRUE.equals(req.getCollegatoAMovimentoDelloStessoBilancio())){
			checkEntita(req.getBilancio(), "bilancio");
		}
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaQuotaSpesaResponse executeService(RicercaQuotaSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		ListaPaginata<SubdocumentoSpesa> listaSubdocumentiSpesa = subdocumentoSpesaDad.ricercaSubdocumentiSpesa(
				req.getEnte(),
				req.getBilancio(),
				req.getUidElenco(),
				req.getAnnoElenco(),
				req.getNumeroElenco(),
				null,
				null,
				req.getAnnoProvvisorio(),
				req.getNumeroProvvisorio(),
				req.getDataProvvisorio(),
				req.getTipoDocumento(),
				req.getAnnoDocumento(),
				req.getNumeroDocumento(),
				req.getDataEmissioneDocumento(),
				req.getNumeroQuota(),
				req.getNumeroMovimento(),
				req.getAnnoMovimento(),
				req.getSoggetto(),
				req.getUidProvvedimento(),
				req.getAnnoProvvedimento(),
				req.getNumeroProvvedimento(),
				req.getTipoAtto(),
				req.getStruttAmmContabile(),
				null,
				null,
				null,
				null,
				req.getStatiOperativoDocumento(),
				req.getCollegatoAMovimentoDelloStessoBilancio(),
				req.getAssociatoAProvvedimentoOAdElenco(),
				req.getImportoDaPagareZero(),
				null,
				req.getRilevatiIvaConRegistrazioneONonRilevantiIva(),				
				null,
				null,
				null,
				null,
				null,
				req.getParametriPaginazione()
				);
		
		res.setListaSubdocumenti(listaSubdocumentiSpesa);
		
		BigDecimal totaleImporti = subdocumentoSpesaDad.ricercaSubdocumentiSpesaTotaleImporti(
				req.getEnte(),
				req.getBilancio(),
				req.getUidElenco(),
				req.getAnnoElenco(),
				req.getNumeroElenco(),
				null,
				null,
				req.getAnnoProvvisorio(),
				req.getNumeroProvvisorio(),
				req.getDataProvvisorio(),
				req.getTipoDocumento(),
				req.getAnnoDocumento(),
				req.getNumeroDocumento(),
				req.getDataEmissioneDocumento(),
				req.getNumeroQuota(),
				req.getNumeroMovimento(),
				req.getAnnoMovimento(),
				req.getSoggetto(),
				req.getUidProvvedimento(),
				req.getAnnoProvvedimento(),
				req.getNumeroProvvedimento(),
				req.getTipoAtto(),
				req.getStruttAmmContabile(),
				null,
				null,
				null,
				null,
				req.getStatiOperativoDocumento(),
				req.getCollegatoAMovimentoDelloStessoBilancio(),
				req.getAssociatoAProvvedimentoOAdElenco(),
				req.getImportoDaPagareZero(),
				null,
				req.getRilevatiIvaConRegistrazioneONonRilevantiIva(),	
				null,
				null,
				null,
				null,
				null);
		
		res.setTotaleImporti(totaleImporti);
		
	}

}
