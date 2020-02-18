/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;

/**
 * The Class RicercaSinteticaQuoteByDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaModulareQuoteEntrataService extends CheckedAccountBaseService<RicercaSinteticaModulareQuoteEntrata, RicercaSinteticaModulareQuoteEntrataResponse> {
	
	/** The subdocumento spesa dad. */
	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getSubdocumentoEntrata(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("subdocumento spesa"));
		checkParametriPaginazione(req.getParametriPaginazione());
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaModulareQuoteEntrataResponse executeService(RicercaSinteticaModulareQuoteEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		// Imposto i model detail
		Utility.MDTL.addModelDetails(req.getModelDetails());
		
		SubdocumentoEntrata ss = req.getSubdocumentoEntrata();
		
		ListaPaginata<SubdocumentoEntrata> result = subdocumentoEntrataDad.ricercaSubdocumentiEntrataModelDetail(
				ente,
				//bilancio,
				null,
				//uidElenco,
				extractUid(ss.getElencoDocumenti()),
				//annoElenco,
				ss.getElencoDocumenti() != null ? ss.getElencoDocumenti().getAnno() : null,
				//numeroElenco,
				ss.getElencoDocumenti() != null ? ss.getElencoDocumenti().getNumero() : null,
				//numeroElencoDa,
				null,
				//numeroElencoA,
				null,
				//annoProvvisorio,
				ss.getProvvisorioCassa() != null ? ss.getProvvisorioCassa().getAnno() : null,
				//numeroProvvisorio,
				ss.getProvvisorioCassa() != null ? ss.getProvvisorioCassa().getNumero() : null,
				//dataProvvisorio, FIXME: questa?
				ss.getProvvisorioCassa() != null ? ss.getProvvisorioCassa().getDataRegolarizzazione() : null,
				//tipoDocumento,
				ss.getDocumento() != null ? ss.getDocumento().getTipoDocumento() : null,
				//annoDocumento,
				ss.getDocumento() != null ? ss.getDocumento().getAnno() : null,
				//numeroDocumento,
				ss.getDocumento() != null ? ss.getDocumento().getNumero() : null,
				//dataEmissioneDocumento,
				ss.getDocumento() != null ? ss.getDocumento().getDataEmissione() : null,
				//numeroQuota,
				ss.getNumero(),
				//numeroMovimento,
				ss.getAccertamento() != null ? ss.getAccertamento().getNumero() : null,
				//annoMovimento,
				ss.getAccertamento() != null ? ss.getAccertamento().getAnnoMovimento() : null,
				//soggetto,
				ss.getDocumento() != null ? ss.getDocumento().getSoggetto() : null,
				//uidProvvedimento,
				extractUid(ss.getAttoAmministrativo()),
				//annoProvvedimento,
				ss.getAttoAmministrativo() != null ? ss.getAttoAmministrativo().getAnno() : null,
				//numeroProvvedimento,
				ss.getAttoAmministrativo() != null ? ss.getAttoAmministrativo().getNumero() : null,
				//tipoAtto,
				ss.getAttoAmministrativo() != null ? ss.getAttoAmministrativo().getTipoAtto() : null,
				//struttAmmContabile,
				ss.getAttoAmministrativo() != null ? ss.getAttoAmministrativo().getStrutturaAmmContabile() : null,
				//annoCapitolo,
				null,
				//numeroCapitolo,
				null,
				//numeroArticolo,
				null,
				//numeroUEB,
				null,
				//statiOperativoDocumento,
				null,
				//collegatoAMovimentoDelloStessoBilancio,
				null,
				//associatoAProvvedimentoOAdElenco,
				null,
				//importoDaPagareZero,
				null,
				//importoDaPagareOIncassareMaggioreDiZero,
				null,
				//rilevatiIvaConRegistrazioneONonRilevantiIva,
				null,
				//collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
				null,
				//statiOperativiAtti,
				null,
				//associatoAdOrdinativo,
				null,
				//conFlagConvalidaManuale,
				null,
				//listprovCassa
				null,
				req.getParametriPaginazione(),
				Utility.MDTL.byModelDetailClass(SubdocumentoEntrataModelDetail.class));
		res.setSubdocumentiEntrata(result);
		
		if(req.isRequireTotale()) {
			BigDecimal totale = subdocumentoEntrataDad.ricercaSubdocumentiEntrataTotaleImporti(
					ente,
					//bilancio,
					null,
					//uidElenco,
					extractUid(ss.getElencoDocumenti()),
					//annoElenco,
					ss.getElencoDocumenti() != null ? ss.getElencoDocumenti().getAnno() : null,
					//numeroElenco,
					ss.getElencoDocumenti() != null ? ss.getElencoDocumenti().getNumero() : null,
					//numeroElencoDa,
					null,
					//numeroElencoA,
					null,
					//annoProvvisorio,
					ss.getProvvisorioCassa() != null ? ss.getProvvisorioCassa().getAnno() : null,
					//numeroProvvisorio,
					ss.getProvvisorioCassa() != null ? ss.getProvvisorioCassa().getNumero() : null,
					//dataProvvisorio, FIXME: questa?
					ss.getProvvisorioCassa() != null ? ss.getProvvisorioCassa().getDataRegolarizzazione() : null,
					//tipoDocumento,
					ss.getDocumento() != null ? ss.getDocumento().getTipoDocumento() : null,
					//annoDocumento,
					ss.getDocumento() != null ? ss.getDocumento().getAnno() : null,
					//numeroDocumento,
					ss.getDocumento() != null ? ss.getDocumento().getNumero() : null,
					//dataEmissioneDocumento,
					ss.getDocumento() != null ? ss.getDocumento().getDataEmissione() : null,
					//numeroQuota,
					ss.getNumero(),
					//numeroMovimento,
					ss.getAccertamento() != null ? ss.getAccertamento().getNumero() : null,
					//annoMovimento,
					ss.getAccertamento() != null ? ss.getAccertamento().getAnnoMovimento() : null,
					//soggetto,
					ss.getDocumento() != null ? ss.getDocumento().getSoggetto() : null,
					//uidProvvedimento,
					extractUid(ss.getAttoAmministrativo()),
					//annoProvvedimento,
					ss.getAttoAmministrativo() != null ? ss.getAttoAmministrativo().getAnno() : null,
					//numeroProvvedimento,
					ss.getAttoAmministrativo() != null ? ss.getAttoAmministrativo().getNumero() : null,
					//tipoAtto,
					ss.getAttoAmministrativo() != null ? ss.getAttoAmministrativo().getTipoAtto() : null,
					//struttAmmContabile,
					ss.getAttoAmministrativo() != null ? ss.getAttoAmministrativo().getStrutturaAmmContabile() : null,
					//annoCapitolo,
					null,
					//numeroCapitolo,
					null,
					//numeroArticolo,
					null,
					//numeroUEB,
					null,
					//statiOperativoDocumento,
					null,
					//collegatoAMovimentoDelloStessoBilancio,
					null,
					//associatoAProvvedimentoOAdElenco,
					null,
					//importoDaPagareZero,
					null,
					//importoDaPagareOIncassareMaggioreDiZero,
					null,
					//rilevatiIvaConRegistrazioneONonRilevantiIva,
					null,
					//collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
					null,
					//statiOperativiAtti,
					null,
					//associatoAdOrdinativo,
					null,
					//conFlagConvalidaManuale,
					null,
					//listprovCassa
					null
					);
			res.setTotale(totale);
		}
		
	}
	
}
