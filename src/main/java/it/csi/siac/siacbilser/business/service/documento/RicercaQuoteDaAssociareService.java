/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociare;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociareResponse;
import it.csi.siac.siacfin2ser.model.Subdocumento;

/**
 * The Class RicercaQuoteDaAssociareService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaQuoteDaAssociareService extends BaseRicercaQuoteDaAssociareService<RicercaQuoteDaAssociare, RicercaQuoteDaAssociareResponse> {

	
	@Override
	@Transactional(readOnly=true)
	public RicercaQuoteDaAssociareResponse executeService(RicercaQuoteDaAssociare serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		//se la request non e' per collegaDocumento lascio invariato il giro per allegato atto
		//altrimenti passo alla ricerca delle quote per la predisposizione d'incasso
		//ALLEGATO ATTO
		ListaPaginata<Subdocumento<?,?>> listaSubdocumenti = subdocumentoDad.ricercaSubdocumentiDaAssociare(
				req.getEnte(),
				req.getBilancio(),
				req.getTipoFamigliaDocumento(),
				req.getUidElenco(),
				req.getAnnoElenco(),
				req.getNumeroElenco(),
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
				req.getStatiOperativoDocumento(),
				req.getCollegatoAMovimentoDelloStessoBilancio(),
				req.getAssociatoAProvvedimentoOAdElenco(),
				req.getImportoDaPagareZero(),
				req.getRilevatiIvaConRegistrazioneONonRilevantiIva(),				
				req.getParametriPaginazione()
				);
		
		
		res.setListaSubdocumenti(listaSubdocumenti);
		
		BigDecimal totaleImporti = subdocumentoDad.ricercaSubdocumentiDaAssociareTotaleImporti(
				req.getEnte(),
				req.getBilancio(),
				req.getTipoFamigliaDocumento(),
				req.getUidElenco(),
				req.getAnnoElenco(),
				req.getNumeroElenco(),
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
				req.getStatiOperativoDocumento(),
				req.getCollegatoAMovimentoDelloStessoBilancio(),
				req.getAssociatoAProvvedimentoOAdElenco(),
				req.getImportoDaPagareZero(),
				req.getRilevatiIvaConRegistrazioneONonRilevantiIva(),				
				req.getParametriPaginazione()
				);
		
		res.setTotaleImporti(totaleImporti);
	}
}
