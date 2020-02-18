/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.util.Date;

import it.csi.siac.siacbilser.business.service.allegatoatto.CompletaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InserisceAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InserisceElencoService;
import it.csi.siac.siacbilser.business.service.base.ServiceCallGroup;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.base.cache.keyadapter.RicercaRegistroIvaKeyAdapter;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceNotaCreditoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceTestataDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaDettaglioDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaDettaglioQuotaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaPuntualeDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaQuoteByDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaAliquotaIvaService;
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaTipoRegistrazioneIvaService;
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaValutaService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.InserisceSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.InserisceSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.EliminaQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceNotaCreditoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceTestataDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaDettaglioDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaDettaglioQuotaSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaPuntualeDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuoteByDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.LeggiContiTesoreriaService;
import it.csi.siac.siacbilser.business.service.registroiva.RicercaRegistroIvaService;
import it.csi.siac.siaccommonser.business.service.base.cache.KeyAdapter;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770Response;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBollo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBolloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvviso;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvvisoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValutaResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;

/**
 * Insieme delle chiamate dei serivizi coinvolti nella gestione del documento.  
 * 
 * @author Domenico
 *
 */
public class DocumentoServiceCallGroup extends ServiceCallGroup {
	
	private Ente ente;
	private Bilancio bilancio;

	public DocumentoServiceCallGroup(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente);
		
		this.ente = ente;
		this.bilancio = bilancio;
	}

	public InserisceElencoResponse inserisceElencoDocumentiAllegatoAtto(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		InserisceElenco reqIE = new InserisceElenco();
		reqIE.setRichiedente(richiedente);
		reqIE.setBilancio(bilancio);
		reqIE.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		InserisceElencoResponse inserisceElencoResponse = se.executeServiceSuccess(InserisceElencoService.class, reqIE);
		return inserisceElencoResponse;
	}

	public DocumentoSpesa aggiornaStatoDocumentoDiSpesa(DocumentoSpesa documentoSpesa) {
		AggiornaStatoDocumentoDiSpesa reqAs = new AggiornaStatoDocumentoDiSpesa();
		reqAs.setRichiedente(richiedente);
		reqAs.setDocumentoSpesa(documentoSpesa);
		reqAs.setBilancio(bilancio);
		AggiornaStatoDocumentoDiSpesaResponse resAs = se.executeServiceSuccess(AggiornaStatoDocumentoDiSpesaService.class, reqAs);
		return resAs.getDocumentoSpesa();
	}

	public DocumentoEntrata aggiornaStatoDocumentoDiEntrata(DocumentoEntrata documentoEntrata) {
		AggiornaStatoDocumentoDiEntrata reqAs = new AggiornaStatoDocumentoDiEntrata();
		reqAs.setRichiedente(richiedente);
		reqAs.setBilancio(bilancio);
		reqAs.setDocumentoEntrata(documentoEntrata);
		AggiornaStatoDocumentoDiEntrataResponse resAs = se.executeServiceSuccess(AggiornaStatoDocumentoDiEntrataService.class, reqAs);
		return resAs.getDocumentoEntrata();
	}

	public CompletaAllegatoAttoResponse completaAllegatoAtto(AllegatoAtto allegatoAtto, String... codiciErroreDaEscludere) {

		CompletaAllegatoAtto reqCAA = new CompletaAllegatoAtto();

		reqCAA.setAllegatoAtto(allegatoAtto);
		reqCAA.setBilancio(bilancio);
		reqCAA.setRichiedente(richiedente);

		CompletaAllegatoAttoResponse resCAA = se.executeServiceSuccess(CompletaAllegatoAttoService.class, reqCAA, codiciErroreDaEscludere);

		return resCAA;

	}

	public InserisceSubdocumentoIvaEntrataResponse inserisceSubdocumentoIvaEntrata(SubdocumentoIvaEntrata subdocumentoIvaEntrata, String... codiciErroreDaEscludere ) {
		InserisceSubdocumentoIvaEntrata reqISIE = new InserisceSubdocumentoIvaEntrata();
		reqISIE.setBilancio(bilancio);
		reqISIE.setRichiedente(richiedente);
		reqISIE.setSubdocumentoIvaEntrata(subdocumentoIvaEntrata);
		InserisceSubdocumentoIvaEntrataResponse resISIS = se.executeServiceSuccess(InserisceSubdocumentoIvaEntrataService.class,reqISIE,codiciErroreDaEscludere);
		return resISIS;

	}

	public InserisceSubdocumentoIvaSpesaResponse inserisceSubdocumentoIvaSpesa(SubdocumentoIvaSpesa subdocumentoIvaSpesa, String... codiciErroreDaEscludere ) {
		InserisceSubdocumentoIvaSpesa reqISIS = new InserisceSubdocumentoIvaSpesa();
		reqISIS.setBilancio(bilancio);
		reqISIS.setRichiedente(richiedente);
		reqISIS.setSubdocumentoIvaSpesa(subdocumentoIvaSpesa);
		InserisceSubdocumentoIvaSpesaResponse resISIS = se.executeServiceSuccess(InserisceSubdocumentoIvaSpesaService.class,reqISIS,codiciErroreDaEscludere);
		return resISIS;
	}

	public SubdocumentoSpesa inserisceQuotaSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		return inserisceQuotaDocumentoSpesa(subdocumentoSpesa, true, false, false);
	}

	public SubdocumentoSpesa inserisceQuotaDocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa, boolean aggiornaStatoDocumento){
		return inserisceQuotaDocumentoSpesa(subdocumentoSpesa, aggiornaStatoDocumento, false, false);
	}
	
	public SubdocumentoSpesa inserisceQuotaDocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa, boolean aggiornaStatoDocumento, boolean quotaContestuale) {
		return inserisceQuotaDocumentoSpesa(subdocumentoSpesa, aggiornaStatoDocumento, quotaContestuale, false);
	}
	
	public SubdocumentoSpesa inserisceQuotaDocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa, boolean aggiornaStatoDocumento, boolean quotaContestuale, boolean senzaModalitaPagamento) {
		InserisceQuotaDocumentoSpesa reqIQ = new InserisceQuotaDocumentoSpesa();
		reqIQ.setRichiedente(richiedente);
		reqIQ.setBilancio(bilancio);

		reqIQ.setSubdocumentoSpesa(subdocumentoSpesa);

		reqIQ.setAggiornaStatoDocumento(aggiornaStatoDocumento);
		reqIQ.setQuotaContestuale(quotaContestuale);
		reqIQ.setSenzaModalitaPagamento(senzaModalitaPagamento);

		InserisceQuotaDocumentoSpesaResponse resIQ = se.executeServiceSuccess(InserisceQuotaDocumentoSpesaService.class, reqIQ);
		return resIQ.getSubdocumentoSpesa();
	}
	
	public SubdocumentoSpesa aggiornaQuotaDocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa){
		return aggiornaQuotaDocumentoSpesa(subdocumentoSpesa, true);
	}
	
	public SubdocumentoSpesa aggiornaQuotaDocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa, boolean aggiornaStatoDocumento) {
		AggiornaQuotaDocumentoSpesa reqIQ = new AggiornaQuotaDocumentoSpesa();
		reqIQ.setRichiedente(richiedente);
		reqIQ.setBilancio(bilancio);

		reqIQ.setSubdocumentoSpesa(subdocumentoSpesa);

		reqIQ.setAggiornaStatoDocumento(aggiornaStatoDocumento);

		AggiornaQuotaDocumentoSpesaResponse resIQ = se.executeServiceSuccess(AggiornaQuotaDocumentoSpesaService.class, reqIQ);
		return resIQ.getSubdocumentoSpesa();
	}
	
	
	
	/**
	 * Ricerca le quote legate al documento di spesa.
	 *
	 * @param documentoSpesa the documento spesa
	 * @return the list
	 */
	public RicercaQuoteByDocumentoSpesaResponse ricercaQuoteByDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		RicercaQuoteByDocumentoSpesa reqRQ = new RicercaQuoteByDocumentoSpesa();
		reqRQ.setRichiedente(richiedente);
		
		reqRQ.setDocumentoSpesa(documentoSpesa);

		RicercaQuoteByDocumentoSpesaResponse resRQ = se.executeServiceSuccess(RicercaQuoteByDocumentoSpesaService.class, reqRQ);
		return resRQ;
	}
	
	
	public RicercaQuoteByDocumentoSpesaResponse ricercaQuoteByDocumentoSpesaCached(DocumentoSpesa documentoSpesa, String... codiciErroreDaEscludere) {
		RicercaQuoteByDocumentoSpesa reqRQ = new RicercaQuoteByDocumentoSpesa();
		reqRQ.setRichiedente(richiedente);
		
		reqRQ.setDocumentoSpesa(documentoSpesa);

		RicercaQuoteByDocumentoSpesaResponse resRQ = se.executeServiceThreadLocalCachedSuccess(RicercaQuoteByDocumentoSpesaService.class,reqRQ,
				new KeyAdapter<RicercaQuoteByDocumentoSpesa>() {
					@Override
					public String computeKey(RicercaQuoteByDocumentoSpesa o) {
						return ""+o.getDocumentoSpesa().getUid();
					}
				}, 
				
				codiciErroreDaEscludere);
		
		return resRQ;
	}
	
	
	/**
	 * Ricerca le quote legate al documento di entrata.
	 *
	 * @param documentoSpesa the documento spesa
	 * @return the list
	 */
	public RicercaQuoteByDocumentoEntrataResponse ricercaQuoteByDocumentoEntrata(DocumentoEntrata documentoEntrata) {
		RicercaQuoteByDocumentoEntrata reqRQ = new RicercaQuoteByDocumentoEntrata();
		reqRQ.setRichiedente(richiedente);
		
		reqRQ.setDocumentoEntrata(documentoEntrata);

		RicercaQuoteByDocumentoEntrataResponse resRQ = se.executeServiceSuccess(RicercaQuoteByDocumentoEntrataService.class, reqRQ);
		return resRQ;
	}
	
	
	/**
	 * Ricerca le quote legate al documento di entrata.
	 *
	 * @param documentoSpesa the documento spesa
	 * @return the list
	 */
	public RicercaQuoteByDocumentoEntrataResponse ricercaQuoteByDocumentoEntrataCached(DocumentoEntrata documentoEntrata, String... codiciErroreDaEscludere) {
		RicercaQuoteByDocumentoEntrata reqRQ = new RicercaQuoteByDocumentoEntrata();
		reqRQ.setRichiedente(richiedente);
		
		reqRQ.setDocumentoEntrata(documentoEntrata);

		RicercaQuoteByDocumentoEntrataResponse resRQ = se.executeServiceThreadLocalCachedSuccess(RicercaQuoteByDocumentoEntrataService.class, reqRQ, 
				
				new KeyAdapter<RicercaQuoteByDocumentoEntrata>() {
					@Override
					public String computeKey(RicercaQuoteByDocumentoEntrata o) {
						return ""+o.getDocumentoEntrata().getUid();
					}
				}, 
				
				codiciErroreDaEscludere);
		
		//Success(RicercaQuoteByDocumentoEntrataService.class, reqRQ);
		return resRQ;
	}
	
	
	public SubdocumentoSpesa eliminaQuotaDocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		return eliminaQuotaDocumentoSpesa(subdocumentoSpesa, true,  false);
	}
	
	public SubdocumentoSpesa eliminaQuotaDocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa, boolean aggiornaStatoDocumento, boolean saltaCheckQuotaEliminabile) {
		EliminaQuotaDocumentoSpesa reqIQ = new EliminaQuotaDocumentoSpesa();
		reqIQ.setRichiedente(richiedente);
		reqIQ.setBilancio(bilancio);
		reqIQ.setSubdocumentoSpesa(subdocumentoSpesa);

		reqIQ.setAggiornaStatoDocumento(aggiornaStatoDocumento);
		reqIQ.setSaltaCheckQuotaEliminabile(saltaCheckQuotaEliminabile);

		EliminaQuotaDocumentoSpesaResponse resIQ = se.executeServiceSuccess(EliminaQuotaDocumentoSpesaService.class, reqIQ);
		return resIQ.getSubdocumentoSpesa();
	}
	
	

	public SubdocumentoEntrata inserisceQuotaEntrata(SubdocumentoEntrata subdocumentoEntrata) {
		return inserisceQuotaDocumentoEntrata(subdocumentoEntrata, true);
	}

	public SubdocumentoEntrata inserisceQuotaDocumentoEntrata(SubdocumentoEntrata subdocumentoEntrata, boolean aggiornaStatoDocumento) {
		return inserisceQuotaDocumentoEntrata(subdocumentoEntrata, aggiornaStatoDocumento, true);
	}
	
	public SubdocumentoEntrata inserisceQuotaDocumentoEntrata(SubdocumentoEntrata subdocumentoEntrata, boolean aggiornaStatoDocumento, boolean impostaFlagConvalidaManuale) {
		InserisceQuotaDocumentoEntrata reqIQ = new InserisceQuotaDocumentoEntrata();
		reqIQ.setRichiedente(richiedente);
		reqIQ.setBilancio(bilancio);

		reqIQ.setSubdocumentoEntrata(subdocumentoEntrata);
		reqIQ.setAggiornaStatoDocumento(aggiornaStatoDocumento);
		reqIQ.setImpostaFlagConvalidaManuale(impostaFlagConvalidaManuale);

		InserisceQuotaDocumentoEntrataResponse resIQ = se.executeServiceSuccess(InserisceQuotaDocumentoEntrataService.class, reqIQ);
		return resIQ.getSubdocumentoEntrata();
	}
	
	
	public SubdocumentoEntrata aggiornaQuotaDocumentoEntrata(SubdocumentoEntrata subdocumentoEntrata, boolean aggiornaStatoDocumento) {
		AggiornaQuotaDocumentoEntrata reqIQ = new AggiornaQuotaDocumentoEntrata();
		reqIQ.setRichiedente(richiedente);
		reqIQ.setBilancio(bilancio);

		reqIQ.setSubdocumentoEntrata(subdocumentoEntrata);

		reqIQ.setAggiornaStatoDocumento(aggiornaStatoDocumento);

		AggiornaQuotaDocumentoEntrataResponse resIQ = se.executeServiceSuccess(AggiornaQuotaDocumentoEntrataService.class, reqIQ);
		return resIQ.getSubdocumentoEntrata();
	}
	
	public InserisceDocumentoSpesaResponse inserisceDocumentoSpesa(DocumentoSpesa documentoSpesa, String... codiciErroreDaEscludere) {
		return inserisceDocumentoSpesa(documentoSpesa, true, codiciErroreDaEscludere);
	}
	
	public InserisceDocumentoSpesaResponse inserisceDocumentoSpesa(DocumentoSpesa documentoSpesa, boolean inserisciQuotaContestuale, String... codiciErroreDaEscludere) {
		return inserisceDocumentoSpesa(documentoSpesa, inserisciQuotaContestuale, false, codiciErroreDaEscludere);
	}

	public InserisceDocumentoSpesaResponse inserisceDocumentoSpesa(DocumentoSpesa documentoSpesa, boolean inserisciQuotaContestuale, boolean inserisciDocumentoRegolarizzazione, String... codiciErroreDaEscludere) {
		InserisceDocumentoSpesa reqID = new InserisceDocumentoSpesa();
		reqID.setRichiedente(richiedente);
		reqID.setBilancio(bilancio);

		reqID.setDocumentoSpesa(documentoSpesa);
		/* documentoSpesa.setTipoRelazione(tipoRelazione); */

		reqID.setInserisciQuotaContestuale(inserisciQuotaContestuale);
		reqID.setInserisciDocumentoRegolarizzazione(inserisciDocumentoRegolarizzazione);

		InserisceDocumentoSpesaResponse resID = se.executeServiceSuccess("inserisceDocumentoSpesaService", InserisceDocumentoSpesaService.class, reqID,
				codiciErroreDaEscludere);

		return resID;
	}
	
	public InserisceDocumentoSpesaResponse inserisceTestataDocumentoSpesa(DocumentoSpesa documentoSpesa, boolean inserisciQuotaContestuale, boolean inserisciDocumentoRegolarizzazione, String... codiciErroreDaEscludere) {
		InserisceDocumentoSpesa reqID = new InserisceDocumentoSpesa();
		reqID.setRichiedente(richiedente);
		reqID.setBilancio(bilancio);

		reqID.setDocumentoSpesa(documentoSpesa);
		/* documentoSpesa.setTipoRelazione(tipoRelazione); */

		reqID.setInserisciQuotaContestuale(inserisciQuotaContestuale);
		reqID.setInserisciDocumentoRegolarizzazione(inserisciDocumentoRegolarizzazione);

		InserisceDocumentoSpesaResponse resID = se.executeServiceSuccess(InserisceTestataDocumentoSpesaService.class, reqID,
				codiciErroreDaEscludere);

		return resID;
	}

	
	public InserisceDocumentoEntrataResponse inserisceDocumentoEntrata(DocumentoEntrata documentoEntrata, String... codiciErroreDaEscludere) {
		return inserisceDocumentoEntrata(documentoEntrata, true, codiciErroreDaEscludere);
	}
	
	public InserisceDocumentoEntrataResponse inserisceDocumentoEntrata(DocumentoEntrata documentoEntrata, boolean inserisciQuotaContestuale,  String... codiciErroreDaEscludere) {
		return inserisceDocumentoEntrata(documentoEntrata, inserisciQuotaContestuale, true, codiciErroreDaEscludere);
	}
		
	public InserisceDocumentoEntrataResponse inserisceDocumentoEntrata(DocumentoEntrata documentoEntrata, boolean inserisciQuotaContestuale, boolean inserisciDocumentoRegolarizzazione,  String... codiciErroreDaEscludere) {
		InserisceDocumentoEntrata reqID = new InserisceDocumentoEntrata();
		reqID.setRichiedente(richiedente);
		reqID.setBilancio(bilancio);

		reqID.setDocumentoEntrata(documentoEntrata);
		/* documentoEntrata.setTipoRelazione(tipoRelazione); */

		reqID.setInserisciQuotaContestuale(inserisciQuotaContestuale);
		reqID.setInserisciDocumentoRegolarizzazione(inserisciDocumentoRegolarizzazione);

		InserisceDocumentoEntrataResponse resID = se.executeServiceSuccess("inserisceDocumentoEntrataService", InserisceDocumentoEntrataService.class, reqID,
				codiciErroreDaEscludere);

		return resID;
	}
	
	public InserisceDocumentoEntrataResponse inserisceTestataDocumentoEntrata(DocumentoEntrata documentoEntrata, boolean inserisciQuotaContestuale, boolean inserisciDocumentoRegolarizzazione,  String... codiciErroreDaEscludere) {
		InserisceDocumentoEntrata reqID = new InserisceDocumentoEntrata();
		reqID.setRichiedente(richiedente);
		reqID.setBilancio(bilancio);

		reqID.setDocumentoEntrata(documentoEntrata);
		/* documentoEntrata.setTipoRelazione(tipoRelazione); */

		reqID.setInserisciQuotaContestuale(inserisciQuotaContestuale);
		reqID.setInserisciDocumentoRegolarizzazione(inserisciDocumentoRegolarizzazione);

		InserisceDocumentoEntrataResponse resID = se.executeServiceSuccess(InserisceTestataDocumentoEntrataService.class, reqID,
				codiciErroreDaEscludere);

		return resID;
	}
	
	
	public AggiornaDocumentoDiEntrataResponse aggiornaDocumentoDiEntrata(DocumentoEntrata docEntrata, boolean aggiornaStatoDocumento, String... codiciErroreDaEscludere ) {
		AggiornaDocumentoDiEntrata reqID = new AggiornaDocumentoDiEntrata();
		reqID.setRichiedente(richiedente);
		reqID.setBilancio(bilancio);
		reqID.setDocumentoEntrata(docEntrata);
		reqID.setAggiornaStatoDocumento(aggiornaStatoDocumento);
		
		return se.executeServiceSuccess("aggiornaDocumentoDiEntrataService", AggiornaDocumentoDiEntrataService.class, reqID,
				codiciErroreDaEscludere);
	}
	
	
	public AggiornaDocumentoDiSpesaResponse aggiornaDocumentoDiSpesa(DocumentoSpesa docSpesa, boolean aggiornaStatoDocumento, String... codiciErroreDaEscludere ) {
		AggiornaDocumentoDiSpesa reqID = new AggiornaDocumentoDiSpesa();
		reqID.setRichiedente(richiedente);
		reqID.setBilancio(bilancio);
		reqID.setDocumentoSpesa(docSpesa);
		reqID.setAggiornaStatoDocumento(aggiornaStatoDocumento);
		
		return se.executeServiceSuccess("aggiornaDocumentoDiSpesaService", AggiornaDocumentoDiSpesaService.class, reqID,
				codiciErroreDaEscludere);
	}
	
	

	public DocumentoSpesa inserisceNotaCreditoSpesa(DocumentoSpesa documentoSpesa) {
		InserisceNotaCreditoSpesa reqID = new InserisceNotaCreditoSpesa();
		reqID.setRichiedente(richiedente);
		reqID.setBilancio(bilancio);

		reqID.setDocumentoSpesa(documentoSpesa);

		InserisceNotaCreditoSpesaResponse resID = se.executeServiceSuccess(InserisceNotaCreditoSpesaService.class, reqID);

		return resID.getDocumentoSpesa();
	}

	public DocumentoEntrata inserisceNotaCreditoEntrata(DocumentoEntrata documentoEntrata) {
		InserisceNotaCreditoEntrata reqID = new InserisceNotaCreditoEntrata();
		reqID.setRichiedente(richiedente);
		reqID.setBilancio(bilancio);

		reqID.setDocumentoEntrata(documentoEntrata);

		InserisceNotaCreditoEntrataResponse resID = se.executeServiceSuccess(InserisceNotaCreditoEntrataService.class, reqID);

		return resID.getDocumentoEntrata();
	}

//	public String computeKey(AttoAmministrativo attoAmministrativo) {
//		return attoAmministrativo.getAnno() + "/" + attoAmministrativo.getNumero() + "/" + attoAmministrativo.getTipoAtto();
//	}
//
//	public Map<String, AttoAmministrativo> attoAmministrativoCache = new HashMap<String, AttoAmministrativo>();
//
//	public AttoAmministrativo ricercaProvvedimentoCached(AttoAmministrativo attoAmministrativo) {
//		String key = computeKey(attoAmministrativo);
//		if (!attoAmministrativoCache.containsKey(key)) {
//			AttoAmministrativo attoAmministrativoTrovato = ricercaProvvedimento(attoAmministrativo);
//			attoAmministrativoCache.put(key, attoAmministrativoTrovato);
//			return attoAmministrativoTrovato;
//		}
//		return attoAmministrativoCache.get(key);
//	}
//
//	public AttoAmministrativo ricercaProvvedimento(AttoAmministrativo attoAmministrativo) {
//		RicercaProvvedimento ricercaProvvedimento = new RicercaProvvedimento();
//		ricercaProvvedimento.setRichiedente(richiedente);
//		ricercaProvvedimento.setEnte(ente);
//
//		RicercaAtti ricercaAtti = new RicercaAtti();
//		ricercaAtti.setAnnoAtto(attoAmministrativo.getAnno());
//		ricercaAtti.setNumeroAtto(attoAmministrativo.getNumero());
//		ricercaAtti.setTipoAtto(attoAmministrativo.getTipoAtto());
//		ricercaAtti.setStrutturaAmministrativoContabile(attoAmministrativo.getStrutturaAmmContabile());
//
//		ricercaProvvedimento.setRicercaAtti(ricercaAtti);
//
//		RicercaProvvedimentoResponse ricercaProvvedimentoResponse = se.executeServiceSuccess(RicercaProvvedimentoService.class,
//				ricercaProvvedimento);
//
//		List<AttoAmministrativo> listaAttiAmministrativi = ricercaProvvedimentoResponse.getListaAttiAmministrativi();
//
//		int size = listaAttiAmministrativi.size();
//		if (size != 1) {
//			throw new BusinessException("Trovati "+size+" provvedimenti per "+attoAmministrativo.getAnno()+"/" +attoAmministrativo.getNumero()+ (attoAmministrativo.getTipoAtto()!=null?"("+attoAmministrativo.getTipoAtto().getCodice()+")":"") + " . Deve essercene uno e uno solo.");
//		}
//
//		return listaAttiAmministrativi.get(0);
//	}

	public AllegatoAtto inserisceAllegatoAtto(AllegatoAtto allegatoAtto, StatoOperativoAllegatoAtto statoOperativoAllegatoAtto, String... codiciErroreDaEscludere) {
		allegatoAtto.setStatoOperativoAllegatoAtto(statoOperativoAllegatoAtto);
		return inserisceAllegatoAtto(allegatoAtto, codiciErroreDaEscludere);
	}
	
	
	public AllegatoAtto inserisceAllegatoAtto(AllegatoAtto allegatoAtto, String... codiciErroreDaEscludere) {
		InserisceAllegatoAtto reqIAA = new InserisceAllegatoAtto();

		reqIAA.setRichiedente(richiedente);
		reqIAA.setBilancio(bilancio);

		allegatoAtto.setEnte(ente);

		reqIAA.setAllegatoAtto(allegatoAtto);

		InserisceAllegatoAttoResponse resIAA = se.executeServiceSuccess(InserisceAllegatoAttoService.class, reqIAA,
				codiciErroreDaEscludere);

		return resIAA.getAllegatoAtto();
	}
	
	
	
	public RicercaCodiceBolloResponse ricercaCodiceBollo() {
		RicercaCodiceBollo reqRCB = new RicercaCodiceBollo();
		reqRCB.setEnte(ente);
		reqRCB.setRichiedente(richiedente);

		return se.executeServiceSuccess(RicercaCodiceBolloService.class, reqRCB);
	}
	
	public RicercaCodiceBolloResponse ricercaCodiceBolloCached() {
		RicercaCodiceBollo reqRCB = new RicercaCodiceBollo();
		reqRCB.setEnte(ente);
		reqRCB.setRichiedente(richiedente);

		return se.executeServiceThreadLocalCachedSuccess(RicercaCodiceBolloService.class, reqRCB, new KeyAdapter<RicercaCodiceBollo>() {
			@Override
			public String computeKey(RicercaCodiceBollo r) {
				return ""+r.getEnte().getUid();
			}
		});
	}
	

	public RicercaNaturaOnereResponse ricercaNaturaOnere() {
		RicercaNaturaOnere reqRNO = new RicercaNaturaOnere();
		reqRNO.setEnte(ente);
		reqRNO.setRichiedente(richiedente);
		return se.executeServiceSuccess(RicercaNaturaOnereService.class, reqRNO);
	}
	
	public RicercaNaturaOnereResponse ricercaNaturaOnereCached() {
		RicercaNaturaOnere reqRNO = new RicercaNaturaOnere();
		reqRNO.setEnte(ente);
		reqRNO.setRichiedente(richiedente);
		
		return se.executeServiceThreadLocalCachedSuccess(RicercaNaturaOnereService.class, reqRNO, new KeyAdapter<RicercaNaturaOnere>() {
			@Override
			public String computeKey(RicercaNaturaOnere r) {
				return ""+r.getEnte().getUid();
			}
		});
	}


	public RicercaNoteTesoriereResponse ricercaNoteTesoriere() {
		RicercaNoteTesoriere reqRNT = new RicercaNoteTesoriere();
		reqRNT.setEnte(ente);
		reqRNT.setRichiedente(richiedente);

		return se.executeServiceSuccess(RicercaNoteTesoriereService.class, reqRNT);
	}
	
	public RicercaNoteTesoriereResponse ricercaNoteTesoriereCached() {
		RicercaNoteTesoriere reqRNT = new RicercaNoteTesoriere();
		reqRNT.setEnte(ente);
		reqRNT.setRichiedente(richiedente);
		
		return se.executeServiceThreadLocalCachedSuccess(RicercaNoteTesoriereService.class, reqRNT, new KeyAdapter<RicercaNoteTesoriere>() {
			@Override
			public String computeKey(RicercaNoteTesoriere r) {
				return ""+r.getEnte().getUid();
			}
		});
		
	}	
	
	public LeggiContiTesoreriaResponse leggiContiTesoreria() {
		LeggiContiTesoreria reqLCT = new LeggiContiTesoreria();
		reqLCT.setEnte(ente);
		reqLCT.setRichiedente(richiedente);

		return se.executeServiceSuccess(LeggiContiTesoreriaService.class, reqLCT);
	}
	
	public LeggiContiTesoreriaResponse leggiContiTesoreriaCached() {
		LeggiContiTesoreria reqLCT = new LeggiContiTesoreria();
		reqLCT.setEnte(ente);
		reqLCT.setRichiedente(richiedente);
		
		return se.executeServiceThreadLocalCachedSuccess(LeggiContiTesoreriaService.class, reqLCT, new KeyAdapter<LeggiContiTesoreria>() {
			@Override
			public String computeKey(LeggiContiTesoreria r) {
				return ""+r.getEnte().getUid();
			}
		});
		
	}
	

	public RicercaTipoDocumentoResponse ricercaTipiDocumento(TipoFamigliaDocumento tipoFamigliaDocumento) {
		RicercaTipoDocumento reqRTD = new RicercaTipoDocumento();
		reqRTD.setRichiedente(richiedente);
		reqRTD.setEnte(ente);

		reqRTD.setTipoFamDoc(tipoFamigliaDocumento);

		return se.executeServiceSuccess(RicercaTipoDocumentoService.class, reqRTD);

	}
	
	public RicercaTipoDocumentoResponse ricercaTipiDocumentoCached(TipoFamigliaDocumento tipoFamigliaDocumento) {
		RicercaTipoDocumento reqRTD = new RicercaTipoDocumento();
		reqRTD.setRichiedente(richiedente);
		reqRTD.setEnte(ente);

		reqRTD.setTipoFamDoc(tipoFamigliaDocumento);

		
		return se.executeServiceThreadLocalCachedSuccess(RicercaTipoDocumentoService.class, reqRTD, new KeyAdapter<RicercaTipoDocumento>() {
			@Override
			public String computeKey(RicercaTipoDocumento r) {
				return ""+r.getEnte().getUid()+ r.getTipoFamDoc();
			}
		});

	}

	public RicercaTipoAvvisoResponse ricercaTipoAvviso(Integer anno, String... codiciErroreDaEscludere) {
		RicercaTipoAvviso reqRTA = new RicercaTipoAvviso();
		reqRTA.setEnte(ente);
		reqRTA.setRichiedente(richiedente);
		
		reqRTA.setAnno(anno);

		return se.executeServiceSuccess(RicercaTipoAvvisoService.class, reqRTA, codiciErroreDaEscludere);
	}
	
	public RicercaTipoAvvisoResponse ricercaTipoAvvisoCached(Integer anno, String... codiciErroreDaEscludere) {
		RicercaTipoAvviso reqRTA = new RicercaTipoAvviso();
		reqRTA.setEnte(ente);
		reqRTA.setRichiedente(richiedente);
		
		reqRTA.setAnno(anno);
		
		return se.executeServiceThreadLocalCachedSuccess(RicercaTipoAvvisoService.class, reqRTA, new KeyAdapter<RicercaTipoAvviso>() {
			@Override
			public String computeKey(RicercaTipoAvviso r) {
				return ""+r.getEnte().getUid()+ r.getAnno();
			}
		}, codiciErroreDaEscludere);
	}

	public RicercaTipoOnereResponse ricercaTipoOnere(NaturaOnere naturaOnere, String... codiciErroreDaEscludere) {
		RicercaTipoOnere reqRTO = new RicercaTipoOnere();
		reqRTO.setEnte(ente);
		reqRTO.setRichiedente(richiedente);
		reqRTO.setNaturaOnere(naturaOnere);

		return se.executeServiceSuccess(RicercaTipoOnereService.class, reqRTO, codiciErroreDaEscludere);
	}
	
	public RicercaTipoOnereResponse ricercaTipoOnereCached(NaturaOnere naturaOnere, String... codiciErroreDaEscludere) {
		RicercaTipoOnere reqRTO = new RicercaTipoOnere();
		reqRTO.setEnte(ente);
		reqRTO.setRichiedente(richiedente);
		reqRTO.setNaturaOnere(naturaOnere);

		return se.executeServiceThreadLocalCachedSuccess(RicercaTipoOnereService.class, reqRTO, new KeyAdapter<RicercaTipoOnere>() {
			@Override
			public String computeKey(RicercaTipoOnere r) {
				return ""+r.getEnte().getUid()+ (r.getNaturaOnere() != null ? r.getNaturaOnere().getUid() : "null");
			}
		}, codiciErroreDaEscludere);
	}
	
	public RicercaTipoImpresaResponse ricercaTipoImpresa(Integer anno, String... codiciErroreDaEscludere) {
		RicercaTipoImpresa reqRTO = new RicercaTipoImpresa();
		reqRTO.setEnte(ente);
		reqRTO.setRichiedente(richiedente);
		reqRTO.setAnno(anno);

		return se.executeServiceSuccess(RicercaTipoImpresaService.class, reqRTO, codiciErroreDaEscludere);
	}
	
	public RicercaTipoImpresaResponse ricercaTipoImpresaCached(Integer anno, String... codiciErroreDaEscludere) {
		RicercaTipoImpresa reqRTO = new RicercaTipoImpresa();
		reqRTO.setEnte(ente);
		reqRTO.setRichiedente(richiedente);
		reqRTO.setAnno(anno);

		return se.executeServiceThreadLocalCachedSuccess(RicercaTipoImpresaService.class, reqRTO, new KeyAdapter<RicercaTipoImpresa>() {
			@Override
			public String computeKey(RicercaTipoImpresa r) {
				return ""+r.getEnte().getUid()+ r.getAnno();
			}
		}, codiciErroreDaEscludere);
	}
	
	public RicercaRegistroIvaResponse ricercaRegistroIva(RegistroIva registroIva, String... codiciErroreDaEscludere) {
		RicercaRegistroIva reqRRI = new RicercaRegistroIva();
		reqRRI.setRichiedente(richiedente);
		
		registroIva.setEnte(ente);
		reqRRI.setRegistroIva(registroIva);
		
		return se.executeServiceSuccess(RicercaRegistroIvaService.class, reqRRI, codiciErroreDaEscludere);
		
	}
	
	public RicercaRegistroIvaResponse ricercaRegistroIvaCached(RegistroIva registroIva, String... codiciErroreDaEscludere) {
		RicercaRegistroIva reqRRI = new RicercaRegistroIva();
		reqRRI.setRichiedente(richiedente);
		
		registroIva.setEnte(ente);
		reqRRI.setRegistroIva(registroIva);
		
		return se.executeServiceThreadLocalCachedSuccess(RicercaRegistroIvaService.class, reqRRI, new RicercaRegistroIvaKeyAdapter(), codiciErroreDaEscludere);
		
	}
	
	/**
	 * Ricerca tipo registrazione iva.
	 *
	 * @param tipoRegistrazioneIva (.tipoFamigliaDocumento)
	 * @return the ricerca tipo registrazione iva response
	 */
	public RicercaTipoRegistrazioneIvaResponse ricercaTipoRegistrazioneIva(TipoRegistrazioneIva tipoRegistrazioneIva, String... codiciErroreDaEscludere) {
		RicercaTipoRegistrazioneIva reqRRI = new RicercaTipoRegistrazioneIva();
		reqRRI.setRichiedente(richiedente);
		
		tipoRegistrazioneIva.setEnte(ente);
		reqRRI.setTipoRegistrazioneIva(tipoRegistrazioneIva);
		
		return se.executeServiceSuccess(RicercaTipoRegistrazioneIvaService.class, reqRRI, codiciErroreDaEscludere);
	}
	
	/**
	 * Ricerca tipo registrazione iva cached.
	 *
	 * @param tipoRegistrazioneIva (.tipoFamigliaDocumento)
	 * @return the ricerca tipo registrazione iva response
	 */
	public RicercaTipoRegistrazioneIvaResponse ricercaTipoRegistrazioneIvaCached(TipoRegistrazioneIva tipoRegistrazioneIva, String... codiciErroreDaEscludere) {
		RicercaTipoRegistrazioneIva reqRRI = new RicercaTipoRegistrazioneIva();
		reqRRI.setRichiedente(richiedente);
		
		tipoRegistrazioneIva.setEnte(ente);
		reqRRI.setTipoRegistrazioneIva(tipoRegistrazioneIva);
		
		return se.executeServiceThreadLocalCachedSuccess(RicercaTipoRegistrazioneIvaService.class, reqRRI, new KeyAdapter<RicercaTipoRegistrazioneIva>() {

			@Override
			public String computeKey(RicercaTipoRegistrazioneIva r) {
				// TODO Auto-generated method stub
				return ""+ r.getTipoRegistrazioneIva().getEnte() 
						+ (r.getTipoRegistrazioneIva().getTipoFamigliaDocumento()!=null?r.getTipoRegistrazioneIva().getTipoFamigliaDocumento().name():"null");
			}
		}, codiciErroreDaEscludere);
	}

	
	public RicercaValutaResponse ricercaValuta(String... codiciErroreDaEscludere) {
		RicercaValuta reqRV = new RicercaValuta();
		reqRV.setRichiedente(richiedente);
		reqRV.setEnte(ente);
		
		return se.executeServiceSuccess(RicercaValutaService.class, reqRV, codiciErroreDaEscludere);
	}

	
	
	public RicercaValutaResponse ricercaValutaCached(String... codiciErroreDaEscludere) {
		RicercaValuta reqRV = new RicercaValuta();
		reqRV.setRichiedente(richiedente);
		reqRV.setEnte(ente);
		
		return se.executeServiceThreadLocalCachedSuccess(RicercaValutaService.class, reqRV, 
				new KeyAdapter<RicercaValuta>() {
					@Override	
					public String computeKey(RicercaValuta r) {
						return ""+ r.getEnte().getUid();
					}
				}, codiciErroreDaEscludere);
	}
	
	public RicercaAttivitaIvaResponse ricercaAttivitaIvaCached(AttivitaIva attivitaIva, String... codiciErroreDaEscludere) {
		RicercaAttivitaIva reqRAI = new RicercaAttivitaIva();
		reqRAI.setRichiedente(richiedente);
		reqRAI.setEnte(ente);
		reqRAI.setAttivitaIva(attivitaIva);
		
		return se.executeServiceThreadLocalCachedSuccess(RicercaAttivitaIvaService.class, reqRAI, 
				new KeyAdapter<RicercaAttivitaIva>() {
					@Override	
					public String computeKey(RicercaAttivitaIva r) {
						return "" + (r.getEnte()!=null?r.getEnte().getUid():"null") + "_"
								+ (r.getAttivitaIva()!=null?r.getAttivitaIva().getCodice() + r.getAttivitaIva().getDescrizione():"null");
					}
				}, codiciErroreDaEscludere);
	}
	
	
	
	public RicercaAliquotaIvaResponse ricercaAliquotaIvaCached(String... codiciErroreDaEscludere) {
		RicercaAliquotaIva reqRAI = new RicercaAliquotaIva();
		
		reqRAI.setDataOra(new Date());
		reqRAI.setEnte(ente);
		reqRAI.setRichiedente(richiedente);
		
		return se.executeServiceThreadLocalCachedSuccess(RicercaAliquotaIvaService.class, reqRAI,
			new KeyAdapter<RicercaAliquotaIva>() {
				@Override	
				public String computeKey(RicercaAliquotaIva r) {
					return ""+ r.getEnte().getUid();
				}
			}, codiciErroreDaEscludere);
	}
	
	public RicercaAttivitaOnereResponse ricercaAttivitaOnereCached(TipoOnere tipoOnere, String... codiciErroreDaEscludere) {
		RicercaAttivitaOnere reqRAO = new RicercaAttivitaOnere();
		
		reqRAO.setDataOra(new Date());
		reqRAO.setRichiedente(richiedente);
		reqRAO.setEnte(ente);
		reqRAO.setTipoOnere(tipoOnere);
		
		return se.executeServiceThreadLocalCachedSuccess(RicercaAttivitaOnereService.class, reqRAO,
				new KeyAdapter<RicercaAttivitaOnere>() {
					@Override
					public String computeKey(RicercaAttivitaOnere r) {
						return "" + r.getEnte().getUid() + "_" + (r.getTipoOnere() != null ? r.getTipoOnere().getUid() : "null");
					}
				}, codiciErroreDaEscludere);
	}
	
	public RicercaCausale770Response ricercaCausale770Cached(TipoOnere tipoOnere, String... codiciErroreDaEscludere) {
		RicercaCausale770 reqRC770 = new RicercaCausale770();
		
		reqRC770.setDataOra(new Date());
		reqRC770.setRichiedente(richiedente);
		reqRC770.setEnte(ente);
		reqRC770.setTipoOnere(tipoOnere);
		
		return se.executeServiceThreadLocalCachedSuccess(RicercaCausale770Service.class, reqRC770,
				new KeyAdapter<RicercaCausale770>() {
					@Override
					public String computeKey(RicercaCausale770 r) {
						return "" + r.getEnte().getUid() + "_" + (r.getTipoOnere() != null ? r.getTipoOnere().getUid() : "null");
					}
				}, codiciErroreDaEscludere);
	}

	
	
	
	public RicercaPuntualeDocumentoSpesaResponse ricercaPuntualeDocumentoSpesa(DocumentoSpesa documentoSpesa, StatoOperativoDocumento statoOperativoDocumentoDaEscludere){
		RicercaPuntualeDocumentoSpesa reqRPDS = new RicercaPuntualeDocumentoSpesa();
		reqRPDS.setRichiedente(richiedente);
		reqRPDS.setDocumentoSpesa(documentoSpesa);
		reqRPDS.setStatoOperativoDocumentoDaEscludere(statoOperativoDocumentoDaEscludere);

		return se.executeServiceSuccess(RicercaPuntualeDocumentoSpesaService.class,reqRPDS);
	}
	
	public RicercaPuntualeDocumentoEntrataResponse ricercaPuntualeDocumentoEntrata(DocumentoEntrata documentoEntrata, StatoOperativoDocumento statoOperativoDocumentoDaEscludere){
		RicercaPuntualeDocumentoEntrata reqRPDE= new RicercaPuntualeDocumentoEntrata();
		reqRPDE.setRichiedente(richiedente);
		reqRPDE.setDocumentoEntrata(documentoEntrata);
		reqRPDE.setStatoOperativoDocumentoDaEscludere(statoOperativoDocumentoDaEscludere);

		return se.executeServiceSuccess(RicercaPuntualeDocumentoEntrataService.class,reqRPDE);
	}
	
	
	public RicercaDettaglioDocumentoSpesaResponse ricercaDettaglioDocumentoSpesa(DocumentoSpesa documentoSpesa, String... codiciErroreDaEscludere) {
		RicercaDettaglioDocumentoSpesa reqRDDS = new RicercaDettaglioDocumentoSpesa();
		reqRDDS.setRichiedente(richiedente);
		reqRDDS.setDocumentoSpesa(documentoSpesa);
		
		return se.executeServiceSuccess(RicercaDettaglioDocumentoSpesaService.class,reqRDDS, codiciErroreDaEscludere);
	}
	
	public RicercaDettaglioDocumentoSpesaResponse ricercaDettaglioDocumentoSpesaCached(DocumentoSpesa documentoSpesa, String... codiciErroreDaEscludere) {
		RicercaDettaglioDocumentoSpesa reqRDDS = new RicercaDettaglioDocumentoSpesa();
		reqRDDS.setRichiedente(richiedente);
		reqRDDS.setDocumentoSpesa(documentoSpesa);
		
		return se.executeServiceThreadLocalCachedSuccess(RicercaDettaglioDocumentoSpesaService.class,reqRDDS, new KeyAdapter<RicercaDettaglioDocumentoSpesa>() {
			@Override
			public String computeKey(RicercaDettaglioDocumentoSpesa o) {
				return ""+ (o.getDocumentoSpesa()!=null? o.getDocumentoSpesa().getUid():"null");
			}
		}, codiciErroreDaEscludere);
	}
	
	public RicercaDettaglioDocumentoEntrataResponse ricercaDettaglioDocumentoEntrata(DocumentoEntrata documentoEntrata, String... codiciErroreDaEscludere) {
		RicercaDettaglioDocumentoEntrata reqRDDS = new RicercaDettaglioDocumentoEntrata();
		reqRDDS.setRichiedente(richiedente);
		reqRDDS.setDocumentoEntrata(documentoEntrata);
		
		return se.executeServiceSuccess(RicercaDettaglioDocumentoEntrataService.class,reqRDDS, codiciErroreDaEscludere);
	}
	
	public RicercaDettaglioDocumentoEntrataResponse ricercaDettaglioDocumentoEntrataCached(DocumentoEntrata documentoEntrata, String... codiciErroreDaEscludere) {
		RicercaDettaglioDocumentoEntrata reqRDDS = new RicercaDettaglioDocumentoEntrata();
		reqRDDS.setRichiedente(richiedente);
		reqRDDS.setDocumentoEntrata(documentoEntrata);
		
		return se.executeServiceThreadLocalCachedSuccess(RicercaDettaglioDocumentoEntrataService.class,reqRDDS, new KeyAdapter<RicercaDettaglioDocumentoEntrata>() {
			@Override
			public String computeKey(RicercaDettaglioDocumentoEntrata o) {
				return ""+ (o.getDocumentoEntrata()!=null? o.getDocumentoEntrata().getUid():"null");
			}
		}, codiciErroreDaEscludere);
	}
	
	public RicercaDettaglioQuotaSpesaResponse ricercaDettaglioQuotaSpesa(SubdocumentoSpesa subdocumentoSpesa, String... codiciErroreDaEscludere) {
		RicercaDettaglioQuotaSpesa reqRDQS = new RicercaDettaglioQuotaSpesa();
		reqRDQS.setRichiedente(richiedente);
		reqRDQS.setSubdocumentoSpesa(subdocumentoSpesa);
		
		return se.executeServiceSuccess(RicercaDettaglioQuotaSpesaService.class,reqRDQS, codiciErroreDaEscludere);
	}
	
	public RicercaDettaglioQuotaEntrataResponse ricercaDettaglioQuotaEntrata(SubdocumentoEntrata subdocumentoEntrata, String... codiciErroreDaEscludere) {
		RicercaDettaglioQuotaEntrata reqRDQE = new RicercaDettaglioQuotaEntrata();
		reqRDQE.setRichiedente(richiedente);
		reqRDQE.setSubdocumentoEntrata(subdocumentoEntrata);
		
		return se.executeServiceSuccess(RicercaDettaglioQuotaEntrataService.class,reqRDQE, codiciErroreDaEscludere);
	}

	/**
	 * @return the ente
	 */
	public Ente getEnte() {
		return ente;
	}

	/**
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	/**
	 * @return the bilancio
	 */
	public Bilancio getBilancio() {
		return bilancio;
	}

	/**
	 * @param bilancio the bilancio to set
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

	

	

	

	


	
	
	
}
