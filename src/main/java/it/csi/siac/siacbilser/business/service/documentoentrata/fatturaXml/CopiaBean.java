/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata.fatturaXml;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.dozer.converters.XMLGregorianCalendarConverter;

import it.csi.siac.fattura.xml.AnagraficaType;
import it.csi.siac.fattura.xml.CedentePrestatoreType;
import it.csi.siac.fattura.xml.CessionarioCommittenteType;
import it.csi.siac.fattura.xml.CondizioniPagamentoType;
import it.csi.siac.fattura.xml.ContattiTrasmittenteType;
import it.csi.siac.fattura.xml.DatiAnagraficiCedenteType;
import it.csi.siac.fattura.xml.DatiAnagraficiCessionarioType;
import it.csi.siac.fattura.xml.DatiBeniServiziType;
import it.csi.siac.fattura.xml.DatiDocumentiCorrelatiType;
import it.csi.siac.fattura.xml.DatiGeneraliDocumentoType;
import it.csi.siac.fattura.xml.DatiGeneraliType;
import it.csi.siac.fattura.xml.DatiPagamentoType;
import it.csi.siac.fattura.xml.DatiRiepilogoType;
import it.csi.siac.fattura.xml.DatiTrasmissioneType;
import it.csi.siac.fattura.xml.DettaglioLineeType;
import it.csi.siac.fattura.xml.DettaglioPagamentoType;
import it.csi.siac.fattura.xml.EsigibilitaIVAType;
import it.csi.siac.fattura.xml.FatturaElettronicaBodyType;
import it.csi.siac.fattura.xml.FatturaElettronicaHeaderType;
import it.csi.siac.fattura.xml.FatturaElettronicaType;
import it.csi.siac.fattura.xml.FormatoTrasmissioneType;
import it.csi.siac.fattura.xml.IdFiscaleType;
import it.csi.siac.fattura.xml.IndirizzoType;
import it.csi.siac.fattura.xml.ModalitaPagamentoType;
import it.csi.siac.fattura.xml.NaturaType;
import it.csi.siac.fattura.xml.RegimeFiscaleType;
import it.csi.siac.fattura.xml.TipoDocumentoType;
import it.csi.siac.siacbilser.business.utility.Util;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoDocumento;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnte;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.Ordine;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class CopiaBean.
 * A partire dai dati forniti in input, popola i campi dell'oggetto FatturaElettronicaType.
 */
public class CopiaBean {
	

	private FatturaElettronicaType fattura;
	private int dettaglioLineeCounter=0;
//	private HashMap<String,String> codIva=null;
	
//	private static final String NULL_STRING="null";
	private static final BigDecimal ZERO_WITH_DECIMALS =  BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	private static final Map<TipoRegistroIva, EsigibilitaIVAType> MAP_REGISTRO_ESIGIBILITA;
	
	static {
		Map<TipoRegistroIva, EsigibilitaIVAType> tmp = new HashMap<TipoRegistroIva, EsigibilitaIVAType>();
		tmp.put(TipoRegistroIva.VENDITE_IVA_IMMEDIATA, EsigibilitaIVAType.I);
		tmp.put(TipoRegistroIva.VENDITE_IVA_DIFFERITA, EsigibilitaIVAType.D);
		//TODO verificare con analista
		tmp.put(TipoRegistroIva.CORRISPETTIVI, EsigibilitaIVAType.D);
		
		MAP_REGISTRO_ESIGIBILITA = Collections.unmodifiableMap(tmp);
	}
	
//	private static final Map<String, String> MAP_CODICI_IVA;
//		
//	static {
//		Map<String, String> codIva = new HashMap<String, String>();
//		codIva.put("1", "4");
//		codIva.put("2", "10");
//		codIva.put("3", "21");
//		codIva.put("4", "20");
//		codIva.put("5", "N4");
//		codIva.put("6", "N4");
//		codIva.put("8", "N3");
//		codIva.put("12", "N3");
//		codIva.put("14", "22");
//		codIva.put("16", "N6");
//		codIva.put("17", "N2");
//		codIva.put("20", "N1");
//		codIva.put("21", "N6");
//		codIva.put("22", "N6");
//		codIva.put("25", "4");
//		codIva.put("26", "10");
//		codIva.put("27", "22");
//		MAP_CODICI_IVA = Collections.unmodifiableMap(codIva);
//	}
	
	
	
	/**
	 * Instantiates a new copia bean.
	 */
	public CopiaBean() {
		super();
//		codIva=new HashMap<String, String>();
//		codIva.put("1", "4");
//		codIva.put("2", "10");
//		codIva.put("3", "21");
//		codIva.put("4", "20");
//		codIva.put("5", "N4");
//		codIva.put("6", "N4");
//		codIva.put("8", "N3");
//		codIva.put("12", "N3");
//		codIva.put("14", "22");
//		codIva.put("16", "N6");
//		codIva.put("17", "N2");
//		codIva.put("20", "N1");
//		codIva.put("21", "N6");
//		codIva.put("22", "N6");
//		codIva.put("25", "4");
//		codIva.put("26", "10");
//		codIva.put("27", "22");
	}
	
	/**
	 * Ottiene la fattura così come creata 
	 *
	 * @return the fattura
	 */
	public FatturaElettronicaType getFattura() {
		return fattura;
	}


	/**
	 * Crea fattura.
	 *
	 * @param doc the doc
	 * @param soggettoDocumento the soggetto
	 * @param subdocIvaCollegatoAlDocumento the subdoc iva collegato al documento
	 * @param subdocIvasCollegatiAlSubdocumento the subdoc ivas collegati al subdocumento
	 * @param soggEnte the sogg ente
	 * @param partitaIvaFEL the partita iva FEL
	 * @param codiceDestinatario the codice destinatario
	 * @param mappaConfigurazioniEnte 
	 */
	public void creaFattura(DocumentoEntrata doc, Soggetto soggettoDocumento, SubdocumentoIvaEntrata subdocIvaCollegatoAlDocumento, List<SubdocumentoIvaEntrata> subdocIvasCollegatiAlSubdocumento, 
			IndirizzoSoggetto soggEnte, String partitaIvaFEL, String codiceDestinatario, SirfelDTipoDocumento sirfelDTipoDoc, Map<ParametroConfigurazioneEnte, String> mappaConfigurazioniEnte) {
		inizializza();
		header(doc, soggettoDocumento, soggEnte, partitaIvaFEL, codiceDestinatario, mappaConfigurazioniEnte);
		body(doc, subdocIvaCollegatoAlDocumento, subdocIvasCollegatiAlSubdocumento,  soggettoDocumento , sirfelDTipoDoc);
	}
	
	
	private void inizializza(){
		
		fattura= new FatturaElettronicaType();
		fattura.setFatturaElettronicaHeader(new FatturaElettronicaHeaderType());
		
		fattura.getFatturaElettronicaHeader().setDatiTrasmissione(new DatiTrasmissioneType());
		fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setIdTrasmittente(new IdFiscaleType());
		fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setContattiTrasmittente(new ContattiTrasmittenteType());
		
		fattura.getFatturaElettronicaHeader().setCedentePrestatore(new CedentePrestatoreType());
		fattura.getFatturaElettronicaHeader().getCedentePrestatore().setDatiAnagrafici(new DatiAnagraficiCedenteType());
		fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().setIdFiscaleIVA(new IdFiscaleType());
		fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().setAnagrafica(new AnagraficaType());
		fattura.getFatturaElettronicaHeader().getCedentePrestatore().setSede(new IndirizzoType());
		
		fattura.getFatturaElettronicaHeader().setCessionarioCommittente(new CessionarioCommittenteType());
		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().setDatiAnagrafici(new DatiAnagraficiCessionarioType());
		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().setAnagrafica(new AnagraficaType());
		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().setSede(new IndirizzoType());
		
		FatturaElettronicaBodyType body= new FatturaElettronicaBodyType();
		
		body.setDatiGenerali(new DatiGeneraliType());
		
		body.getDatiGenerali().setDatiGeneraliDocumento(new DatiGeneraliDocumentoType());
		
		body.setDatiBeniServizi(new DatiBeniServiziType());
		
		fattura.getFatturaElettronicaBody().add(body);		
	}
	
	/**
	 * Header.
	 *
	 * @param doc the doc
	 * @param soggettoDocumento the soggetto
	 * @param soggEnte the sogg ente
	 * @param partitaIvaFEL the partita iva FEL
	 * @param codiceDestinatario the codice destinatario
	 * @param mappaConfigurazioniEnte 
	 */
	private void header(DocumentoEntrata doc, Soggetto soggettoDocumento, IndirizzoSoggetto soggEnte, String partitaIvaFEL, String codiceDestinatario, Map<ParametroConfigurazioneEnte, String> mappaConfigurazioniEnte) {

		/* 
		 * Dati Trasmissione 
		 * 
		 * */
		
		// Fix per decodifica codice nazione = 1 --> IT
	
		if (StringUtils.isNotEmpty(soggEnte.getCodiceNazione())) {//!"".equals(soggEnte.getCodiceNazione()) && soggEnte.getCodiceNazione() != null) {
			if ("1".equals(soggEnte.getCodiceNazione())) {
				fattura.getFatturaElettronicaHeader().getDatiTrasmissione().getIdTrasmittente()
					.setIdPaese("IT"); // NAZIONE ITALIA
			} else {
				fattura.getFatturaElettronicaHeader().getDatiTrasmissione().getIdTrasmittente()
						.setIdPaese(soggEnte.getCodiceNazione()); // NAZIONE ESTERO
			}
		}
		
		fattura.getFatturaElettronicaHeader().getDatiTrasmissione().getIdTrasmittente().setIdCodice(soggettoDocumento.getCodiceFiscale().trim());
		fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setProgressivoInvio((new Integer(doc.getUid())).toString());
		
		String mailTrasmittente = mappaConfigurazioniEnte.get(ParametroConfigurazioneEnte.FEL_MAIL_TRASMITTENTE);
		String telefonoTrasmittente = mappaConfigurazioniEnte.get(ParametroConfigurazioneEnte.FEL_TELEFONO_TRASMITTENTE);
		if(StringUtils.isBlank(telefonoTrasmittente) || StringUtils.isBlank(mailTrasmittente)) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("telefono trasmittente o mail non impostate sul sistema"));
		}
		fattura.getFatturaElettronicaHeader().getDatiTrasmissione().getContattiTrasmittente().setEmail(mailTrasmittente);
		fattura.getFatturaElettronicaHeader().getDatiTrasmissione().getContattiTrasmittente().setTelefono(telefonoTrasmittente);

		if (soggettoDocumento.isSoggettoPA()) {
			fattura.setVersione(FormatoTrasmissioneType.FPA_12);
			fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setFormatoTrasmissione(FormatoTrasmissioneType.FPA_12);
			fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setCodiceDestinatario(codiceDestinatario);
			//fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setPECDestinatario(soggetto.getEmailPec());
		} else {
			fattura.setVersione(FormatoTrasmissioneType.FPR_12);
			fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setFormatoTrasmissione(FormatoTrasmissioneType.FPR_12);
			fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setCodiceDestinatario(codiceDestinatario);
			if("0000000".equalsIgnoreCase(codiceDestinatario) && StringUtils.isNotBlank(soggettoDocumento.getEmailPec()))
				fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setPECDestinatario(soggettoDocumento.getEmailPec());
		}

		
		/* 
		 * Cedente Prestatore 
		 * 
		 * */
		valorizzaCedentePrestatore(doc.getEnte(),  soggEnte, partitaIvaFEL);
		

		/* 
		 * Cessionario Committente 
		 * 
		 * */
		valorizzaCessionarioCommittente(soggettoDocumento);

	}
	
	/**
	 * Body.
	 *
	 * @param doc the doc
	 * @param subdocIvaCollegatoDocumento the doc iva
	 * @param soggettoDocumento 
	 */
	private void body(DocumentoEntrata doc, SubdocumentoIvaEntrata subdocIvaCollegatoDocumento, 
			List<SubdocumentoIvaEntrata> subdocIvasCollegatiAlSubdocumento, Soggetto soggettoDocumento, SirfelDTipoDocumento sirfelDTipoDoc) {
		

		/* 
		 * Dati Generali 
		 * 
		 */

		/*
		 * START SIAC-7557-VG
		 */
		
//		if ("FTV".equals(doc.getTipoDocumento().getCodice())) {
//			fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setTipoDocumento(TipoDocumentoType.TD_01);
//		} else if ("NCV".equals(doc.getTipoDocumento().getCodice())) {
//			fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setTipoDocumento(TipoDocumentoType.TD_04);
//		}
		
		
		if(sirfelDTipoDoc!= null ){
			try{
				TipoDocumentoType tipoDocValEnum = TipoDocumentoType.fromValue(sirfelDTipoDoc.getCodice());
				fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setTipoDocumento(tipoDocValEnum);
			}catch(Exception e){
				throw new BusinessException(ErroreFin.INVIO_FEL.getErrore("KO","Corrispondenza tra tipo Documento Contabilia e Tipo Documento FEL non trovata"));
			}
			
		}
		
		/*
		 * END SIAC-7557
		 */
		
		

		fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setDivisa("EUR");
		fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setData(Util.toXMLGregorianCalendar(doc.getDataEmissione()));
//		SIAC-6814: revertata da SIAC-6980
//		fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setNumero(docIva != null && docIva.getNumeroProtocolloDefinitivo() != null? docIva.getNumeroProtocolloDefinitivo().toString() : doc.getNumero());
//      SIAC-6980
		
		
		fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setNumero(doc.getNumero());
		
		fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setImportoTotaleDocumento(doc.getImporto().setScale(2, BigDecimal.ROUND_HALF_EVEN));

		//SIAC-7557
		if(doc.getOrdini() != null && !doc.getOrdini().isEmpty()){
			for(Ordine ordine : doc.getOrdini()){
				DatiDocumentiCorrelatiType datiOrdineAcquisto = new DatiDocumentiCorrelatiType();
				datiOrdineAcquisto.setIdDocumento(ordine.getNumeroOrdine());
				fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiOrdineAcquisto().add(datiOrdineAcquisto);
			}
		}
		//SIAC-6988
		if("NCV".equals(doc.getTipoDocumento().getCodice()) && doc.getListaDocumentiEntrataPadre() != null && !doc.getListaDocumentiEntrataPadre().isEmpty()){
			this.gestioneFattureCollegate(doc.getListaDocumentiEntrataPadre());
		}
		
		//SIAC-7569 e SIAC-7516
		if(soggettoDocumento.isSoggettoPA() 
				//N.B: interpretazione di sviluppo: metto il nuovo campo solo se almeno uno tra i due campi e' popolato
				// per timore che fel, se gli arriva un oggetto vuoto, la prenda male
				&& (StringUtils.isNotEmpty(doc.getCig()) || StringUtils.isNotEmpty(doc.getCup()))
				) {
			DatiDocumentiCorrelatiType datiOrdineAcquisto = new DatiDocumentiCorrelatiType();
			//empiricamente, facendo i test, e' venuto fuori questo errore:
			//Invalid content was found starting with element 'CodiceCUP'. One of '{"":RiferimentoNumeroLinea, "":IdDocumento}' is expected.
			//non avendo ottenuto indicazioni su come popolarlo, si prova a mettere il numero documento come id del documento
			datiOrdineAcquisto.setIdDocumento(doc.getNumero());
			
			//campo 2.1.2.5
			//SIAC-6988 : cig e cup potrebbero arrivare con valore stringa vuota, quindi serve controllare
			if(doc.getCig() != null && !doc.getCig().equals(""))
				datiOrdineAcquisto.setCodiceCIG(doc.getCig());
			//campo 2.1.2.6
			if(doc.getCup() != null && !doc.getCup().equals(""))
				datiOrdineAcquisto.setCodiceCUP(doc.getCup());
			fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiOrdineAcquisto().add(datiOrdineAcquisto);
		}
		
		//SIAC-7711
		gestioneDettaglioLineeRiepilogo(doc, subdocIvaCollegatoDocumento, subdocIvasCollegatiAlSubdocumento);
		
		
		//SIAC 6677
		if(doc.getCodAvvisoPagoPA()!= null && doc.getCodAvvisoPagoPA().length()>0){
			if( fattura.getFatturaElettronicaBody().get(0).getDatiPagamento().isEmpty()){
				DatiPagamentoType dpt = new DatiPagamentoType();
				fattura.getFatturaElettronicaBody().get(0).getDatiPagamento().add(dpt);
			}
			if(fattura.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0).getDettaglioPagamento().isEmpty()){
				DettaglioPagamentoType dettaglioPagamentoType = new DettaglioPagamentoType();
				fattura.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0).getDettaglioPagamento().add(dettaglioPagamentoType);
			}
			fattura.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0).getDettaglioPagamento().get(0).setCodicePagamento(doc.getCodAvvisoPagoPA());
			if(fattura.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0).getCondizioniPagamento()==null){
				fattura.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0).setCondizioniPagamento(CondizioniPagamentoType.TP_02);
			}
			//IMPORTO
			if(fattura.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0).getDettaglioPagamento().get(0).getImportoPagamento()==null){
				BigDecimal importoPagamento = fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento();
				fattura.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0).getDettaglioPagamento().get(0).setImportoPagamento(importoPagamento);
			}
			//MODALITA PAGAMENTO
			if(fattura.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0).getDettaglioPagamento().get(0).getModalitaPagamento()==null){
				//SIAC-7569 e SIAC-7516
				//SIAC-7557
				ModalitaPagamentoType modPag = soggettoDocumento.isSoggettoPA() ? ModalitaPagamentoType.MP_15 : ModalitaPagamentoType.MP_23;
				fattura.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0).getDettaglioPagamento().get(0).setModalitaPagamento(modPag);
			}
			
		}
	}

	/**
	 * Gestione dellla parte DettaglioLinee e dettaglio riepilogo del blocco dati beni servizi
	 *
	 * @param doc il documento di entrata di partenza
	 * @param subdocIvaCollegatoDocumento il subdoc iva collegato documento
	 * @param subdocIvasCollegatiAlSubdocumento i subdocumenti iva collegati al subdocumento di partenza
	 */
	private void gestioneDettaglioLineeRiepilogo(DocumentoEntrata doc, SubdocumentoIvaEntrata subdocIvaCollegatoDocumento, List<SubdocumentoIvaEntrata> subdocIvasCollegatiAlSubdocumento) {
		
		this.dettaglioLineeCounter = 0;
		
		List<DettaglioLineeType> lineeDettaglioFel = new ArrayList<DettaglioLineeType>();
		List<DatiRiepilogoType> listaDatiRiepilogo = new ArrayList<DatiRiepilogoType>();
		List<Integer> uidSubdocumentiConIva = new ArrayList<Integer>();
		Map<String, AliquotaTotaleSulDocumento> raggruppamentoPerAliquotaEdEsigibilita = new HashMap<String, AliquotaTotaleSulDocumento>();
		
		// 1 - dati iva cllegati al documento
		if(subdocIvaCollegatoDocumento != null) {
			
			String descrizioneDocumento = doc.getDescrizione();
			aggiungiLineaDettaglioAListaEPopolaMappaRaggruppamentoPErAliquotaEdEsigibilita(lineeDettaglioFel,
					raggruppamentoPerAliquotaEdEsigibilita, subdocIvaCollegatoDocumento, descrizioneDocumento);
			
		}
		
		//2 - dati iva collegati ai subdocumenti
		if(subdocIvasCollegatiAlSubdocumento != null && !subdocIvasCollegatiAlSubdocumento.isEmpty()) {
			
			for (SubdocumentoIvaEntrata subdocIvaSubdoc : subdocIvasCollegatiAlSubdocumento) {
				String descrizioneSubdoc = subdocIvaSubdoc.getSubdocumento().getDescrizione();
				uidSubdocumentiConIva.add(subdocIvaSubdoc.getSubdocumento().getUid());
				aggiungiLineaDettaglioAListaEPopolaMappaRaggruppamentoPErAliquotaEdEsigibilita(lineeDettaglioFel,raggruppamentoPerAliquotaEdEsigibilita, 
						subdocIvaSubdoc, descrizioneSubdoc);
			}
			
		}
		
		popolaDatiRiepilogoDaRaggruppamento(listaDatiRiepilogo, raggruppamentoPerAliquotaEdEsigibilita);
		
		//3 - subdocumenti senza dati iva
		BigDecimal importoSubdocumentiSenzaIva=BigDecimal.ZERO;
		boolean presentiSubdocSenzaIva = false;
		for (SubdocumentoEntrata subdocumentoEntrata : doc.getListaSubdocumenti()) {
			if(subdocIvaCollegatoDocumento != null || (
					Boolean.TRUE.equals(subdocumentoEntrata.getFlagRilevanteIVA()) &&
					 (!uidSubdocumentiConIva.isEmpty() && uidSubdocumentiConIva.contains(Integer.valueOf(subdocumentoEntrata.getUid()))))) {
				//per ora, assumo che le quote rilevanti iva abbiano gia' un  subdocumento iva prima e quindi che siano gia' state messe
				continue;
			}
			presentiSubdocSenzaIva = true;
			this.dettaglioLineeCounter++;
			DettaglioLineeType linea =  new DettaglioLineeType();
			linea.setNumeroLinea(this.dettaglioLineeCounter);
			linea.setDescrizione(subdocumentoEntrata.getDescrizione());
			linea.setPrezzoUnitario(subdocumentoEntrata.getImporto());
			linea.setPrezzoTotale(subdocumentoEntrata.getImporto());
			linea.setAliquotaIVA(ZERO_WITH_DECIMALS);
			linea.setNatura(NaturaType.N_4);
			
			lineeDettaglioFel.add(linea);
			
			importoSubdocumentiSenzaIva= importoSubdocumentiSenzaIva.add(subdocumentoEntrata.getImporto());
		}
		
		if(presentiSubdocSenzaIva) {
			DatiRiepilogoType datiRiep = new DatiRiepilogoType();		
			datiRiep.setAliquotaIVA(ZERO_WITH_DECIMALS);
			datiRiep.setNatura(NaturaType.N_4);		
			datiRiep.setImponibileImporto(importoSubdocumentiSenzaIva.setScale(2, BigDecimal.ROUND_HALF_EVEN));
			datiRiep.setImposta(ZERO_WITH_DECIMALS);
			listaDatiRiepilogo.add(datiRiep);
		}
		
		
		fattura.getFatturaElettronicaBody().get(0).getDatiBeniServizi().getDettaglioLinee().addAll(lineeDettaglioFel);
		fattura.getFatturaElettronicaBody().get(0).getDatiBeniServizi().getDatiRiepilogo().addAll(listaDatiRiepilogo);
	}

	/**
	 * Popola dati riepilogo da raggruppamento.
	 * Dall'xml del tracciato:
	 * <br/>
	 * <strong> Dati Riepilogo </strong>
	 * <br/>
	 * Blocco obbligatorio, gli elementi informativi che lo compongono riepilogano le informazioni di dettaglio, 
	 * aggregandole per <strong> aliquota IVA distinta </strong> oppure per aliquota IVA nulla e <strong> Natura distinta </strong> 
	 * oppure, a parita' di questi elementi, per <strong> Esigbilita' distinta </strong>
	 *
	 * @param listaDatiRiepilogo the lista dati riepilogo da popolare
	 * @param raggruppamentoperAliquotaNaturaEsiibilitaDocumento the raggruppamento per aliquota natura esiibilita documento pre-calcolata
	 */
	private void popolaDatiRiepilogoDaRaggruppamento(List<DatiRiepilogoType> listaDatiRiepilogo, Map<String, AliquotaTotaleSulDocumento> raggruppamentoperAliquotaNaturaEsiibilitaDocumento) {
		if(raggruppamentoperAliquotaNaturaEsiibilitaDocumento == null || raggruppamentoperAliquotaNaturaEsiibilitaDocumento.values() == null) {
//			documento senza iva, esco
			return;
		}
		for (AliquotaTotaleSulDocumento aliquotaTotaleSulDocumento : raggruppamentoperAliquotaNaturaEsiibilitaDocumento.values()) {
			
			DatiRiepilogoType datiRiep = new DatiRiepilogoType();				
			datiRiep.setAliquotaIVA(aliquotaTotaleSulDocumento.getAliquotaIva().getPercentualeAliquota().setScale(2, BigDecimal.ROUND_HALF_EVEN));				
			datiRiep.setEsigibilitaIVA(aliquotaTotaleSulDocumento.getEsigibilitaIvaType());				
			datiRiep.setImponibileImporto(aliquotaTotaleSulDocumento.getImponibileTotaleSuDocumento());
			datiRiep.setImposta(aliquotaTotaleSulDocumento.getImpostaTotaleSulDocumento());
			//SIAC-7557
			if(datiRiep.getAliquotaIVA() != null && aliquotaTotaleSulDocumento.getAliquotaIva().getPercentualeAliquota() != null &&
					aliquotaTotaleSulDocumento.getAliquotaIva().getPercentualeAliquota().compareTo(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN)) == 0 &&
					aliquotaTotaleSulDocumento.getAliquotaIva().getTipoNatura() != null &&
					aliquotaTotaleSulDocumento.getAliquotaIva().getTipoNatura().getCodice() != null){
				datiRiep.setNatura(NaturaType.fromValue(aliquotaTotaleSulDocumento.getAliquotaIva().getTipoNatura().getCodice()));
			}
			listaDatiRiepilogo.add(datiRiep);
		}
	}

	/**
	 * Aggiungi una linea di dettaglio alla lista e popola la mappa che raggruppa i dati per aliquota ed esigibilita
	 *
	 * @param lineeDettaglioFel the linee dettaglio fel
	 * @param raggruppamentoperAliquotaNaturaEsiibilita the raggruppamentoper aliquota natura esiibilita
	 * @param subdocIvaCollegatoDocumento the subdoc iva collegato documento
	 * @param descrizioneLinea la descrizione della linea
	 */
	private void aggiungiLineaDettaglioAListaEPopolaMappaRaggruppamentoPErAliquotaEdEsigibilita(List<DettaglioLineeType> lineeDettaglioFel,
			Map<String, AliquotaTotaleSulDocumento> raggruppamentoperAliquotaNaturaEsiibilita,
			SubdocumentoIvaEntrata subdocIvaCollegatoDocumento,
			String descrizioneLinea) {
		EsigibilitaIVAType esigibilitaIVATypeRegistro = MAP_REGISTRO_ESIGIBILITA.get(subdocIvaCollegatoDocumento.getRegistroIva().getTipoRegistroIva());
		
		//CASO in cui il subdocumento iva e' collegato alla testata
		List<AliquotaSubdocumentoIva> listaAliquoteSubdocumento =  subdocIvaCollegatoDocumento.getListaAliquotaSubdocumentoIva();
		for (AliquotaSubdocumentoIva aliquotaSubdocumento : listaAliquoteSubdocumento) {
			
			AliquotaIva aliquotaIva = aliquotaSubdocumento.getAliquotaIva();
			
			if(aliquotaIva.getPercentualeAliquota() == null) {
				//TODO: ma questo caso e' davvero possibile??
				continue;
			}
			
			BigDecimal percentuale = aliquotaIva.getPercentualeAliquota().setScale(2, BigDecimal.ROUND_HALF_EVEN);
			//dati per il riepilogo, ma da valutare come ancora vanno messi.
			//
			
			this.dettaglioLineeCounter++;
			DettaglioLineeType linea =  new DettaglioLineeType();
			linea.setNumeroLinea(this.dettaglioLineeCounter);
			linea.setDescrizione(descrizioneLinea);
			linea.setPrezzoUnitario(aliquotaSubdocumento.getImponibile());
			linea.setPrezzoTotale(aliquotaSubdocumento.getImponibile());
			linea.setAliquotaIVA(percentuale);
			//SIAC-7557
			if(aliquotaIva.getTipoNatura()!= null){
				linea.setNatura(NaturaType.fromValue(aliquotaIva.getTipoNatura().getCodice()));
			}
			lineeDettaglioFel.add(linea);
			
			EsigibilitaIVAType esigibilitaIva = Boolean.TRUE.equals(aliquotaIva.getFlagIvaSplit())? EsigibilitaIVAType.S : esigibilitaIVATypeRegistro;
			String key = new StringBuilder()
					.append(aliquotaIva.getUid())
					.append("_")
					//commento perche' con una alqiuota non avro' mai una natura diversa da null, inutile
//						.append(NULL_STRING)
//						.append("_")
					.append(esigibilitaIva.value())
					.toString();
			
			
			if(raggruppamentoperAliquotaNaturaEsiibilita.get(key) == null) {
				raggruppamentoperAliquotaNaturaEsiibilita.put(key, new AliquotaTotaleSulDocumento(aliquotaIva, esigibilitaIva));
			}
			raggruppamentoperAliquotaNaturaEsiibilita.get(key).addImponibileTotaleSuDocumento(aliquotaSubdocumento.getImponibile());
			raggruppamentoperAliquotaNaturaEsiibilita.get(key).addImpostaTotaleSulDocumento(aliquotaSubdocumento.getImposta());
		}
	}

	/**
	 * @param doc
	 * @param subdocIvaCollegatoDocumento
	 */
	private void gestioneDettaglioOld(DocumentoEntrata doc, SubdocumentoIvaEntrata subdocIvaCollegatoDocumento) {
		/*
		 * 2.2.1 Dettaglio Linee
		 * 
		 */
		
		AliquotaSubdocumentoIva singolaAliquotaIva = null;
		BigDecimal aliqIvaWithDec = new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		String  naturaAliquota = "N4";
		
		String esigibilitaIVA = null;
		
		
		DettaglioLineeType dettLinea = null;
		List<SubdocumentoEntrata> listSubDoc = doc.getListaSubdocumenti();
		
		
		
		if(subdocIvaCollegatoDocumento != null) {
			
			List<AliquotaSubdocumentoIva> aliquoteIva =  subdocIvaCollegatoDocumento.getListaAliquotaSubdocumentoIva();
			
			singolaAliquotaIva = aliquoteIva.get(0);
			AliquotaIva aliqIva = singolaAliquotaIva.getAliquotaIva();

			if(aliqIva.getPercentualeAliquota() != null) {
				aliqIvaWithDec = aliqIva.getPercentualeAliquota();
				aliqIvaWithDec = aliqIvaWithDec.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				naturaAliquota = "";
			}
			
			// SE SIAC_T_IVA_ALIQUOTA.IVAALIQUOTA_SPLIT = TRUE
			if(aliqIva.getFlagIvaSplit()) {
				esigibilitaIVA = "S";
			}
			else {
				if((subdocIvaCollegatoDocumento.getRegistroIva() != null) && (subdocIvaCollegatoDocumento.getRegistroIva().getTipoRegistroIva() != null)) {
					if(subdocIvaCollegatoDocumento.getRegistroIva().getTipoRegistroIva().getCodice().equalsIgnoreCase("VI"))
						esigibilitaIVA = "I";
					else if(subdocIvaCollegatoDocumento.getRegistroIva().getTipoRegistroIva().getCodice().equalsIgnoreCase("VD"))
						esigibilitaIVA = "D";
				}
			}
		
		}
		
		if((listSubDoc != null) && (listSubDoc.size() > 0)) {
			
			Iterator<SubdocumentoEntrata> iListSubDoc = listSubDoc.iterator();
			int numLinea = 1;
			
			while(iListSubDoc.hasNext()) {
				
				SubdocumentoEntrata subDocEnt = iListSubDoc.next();
				
				dettLinea = new DettaglioLineeType();
				
				dettLinea.setNumeroLinea(numLinea);
				dettLinea.setDescrizione(subDocEnt.getDescrizione());
				
				if(subDocEnt.getFlagRilevanteIVA()) {
				
					if(singolaAliquotaIva != null) {
						dettLinea.setPrezzoUnitario(singolaAliquotaIva.getImponibile());
						dettLinea.setPrezzoTotale(singolaAliquotaIva.getImponibile());
					
					}
					else {
						dettLinea.setPrezzoUnitario(subDocEnt.getImporto());
						dettLinea.setPrezzoTotale(subDocEnt.getImporto());
					}
					
					dettLinea.setAliquotaIVA(aliqIvaWithDec);
				}
				else {
					//SUBDOC SENZA IVA
					dettLinea.setPrezzoUnitario(subDocEnt.getImporto());
					dettLinea.setPrezzoTotale(subDocEnt.getImporto());
					
					BigDecimal aliqIva0WithDec = new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_EVEN);
					dettLinea.setAliquotaIVA(aliqIva0WithDec);
					
					dettLinea.setNatura(NaturaType.N_4);
				}
				
				fattura.getFatturaElettronicaBody().get(0).getDatiBeniServizi().getDettaglioLinee().add(dettLinea);
				numLinea++;
			}
		}
		
		/*
		 * 2.2.2 Dai Riepilogo
		 * 
		 */
		DatiRiepilogoType datiRiep = new DatiRiepilogoType();
		
		datiRiep.setAliquotaIVA(aliqIvaWithDec);
		
		if(!naturaAliquota.equalsIgnoreCase("")) {
			datiRiep.setNatura(NaturaType.N_4);
		}
		
		if(esigibilitaIVA != null) {
			datiRiep.setEsigibilitaIVA(EsigibilitaIVAType.valueOf(esigibilitaIVA));
		}
		
		if(singolaAliquotaIva != null) {
			datiRiep.setImponibileImporto(singolaAliquotaIva.getImponibile().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			datiRiep.setImposta(singolaAliquotaIva.getImposta().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			
		}
		else {
			datiRiep.setImponibileImporto(doc.getImporto().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			datiRiep.setImposta(new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_EVEN));
		}
		fattura.getFatturaElettronicaBody().get(0).getDatiBeniServizi().getDatiRiepilogo().add(datiRiep);
	}
	
	private void valorizzaCedentePrestatore(Ente ente, IndirizzoSoggetto indirizzoSoggEnte, String partitaIvaFEL) {

		/*switch (codEnte) {
			case 2 : 
				// REGIONE
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().setIdPaese("IT");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().setIdCodice("00514490010");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().setDenominazione("Comune di Torino");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setIndirizzo("Piazza Palazzo di Citta");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setNumeroCivico("1");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setCAP("10122");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().setRegimeFiscale(RegimeFiscaleType.RF_01);
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setComune("Torino");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setProvincia("TO");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setNazione("IT");
				break;
			default :
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().setIdPaese("IT");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().setIdCodice("00514490010");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().setDenominazione("Comune di Torino");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setIndirizzo("Piazza Palazzo di Citta");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setNumeroCivico("1");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setCAP("10122");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().setRegimeFiscale(RegimeFiscaleType.RF_01);
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setComune("Torino");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setProvincia("TO");
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setNazione("IT");
		}*/
		
		if (!"".equals(indirizzoSoggEnte.getCodiceNazione()) && indirizzoSoggEnte.getCodiceNazione() != null) {
			if ("1".equals(indirizzoSoggEnte.getCodiceNazione())) {
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA()
					.setIdPaese("IT"); // NAZIONE ITALIA
			} else {
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA()
					.setIdPaese(indirizzoSoggEnte.getCodiceNazione()); // NAZIONE ESTERO
			}
		}
		//SIAC-7157
		fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().setIdCodice(partitaIvaFEL);
		fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().setDenominazione(ente.getNome());
		fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setIndirizzo(indirizzoSoggEnte.getSedime() + " " + indirizzoSoggEnte.getDenominazione());
		fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setNumeroCivico(indirizzoSoggEnte.getNumeroCivico());
		fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setCAP(indirizzoSoggEnte.getCap());
		fattura.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().setRegimeFiscale(RegimeFiscaleType.RF_01);
		fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setComune(indirizzoSoggEnte.getComune());
		fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede().setProvincia(indirizzoSoggEnte.getProvincia());
		
		if (!"".equals(indirizzoSoggEnte.getCodiceNazione()) && indirizzoSoggEnte.getCodiceNazione() != null) {
			if ("1".equals(indirizzoSoggEnte.getCodiceNazione())) {
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede()
					.setNazione("IT"); // NAZIONE ITALIA
			} else {
				fattura.getFatturaElettronicaHeader().getCedentePrestatore().getSede()
					.setNazione(indirizzoSoggEnte.getCodiceNazione()); // NAZIONE ESTERO
			}
		}		
		
	}
	
		
	private void valorizzaCessionarioCommittente(Soggetto soggettoDocumento) {
		
		if((soggettoDocumento.getNome() != null) && (soggettoDocumento.getCognome() != null)) {
			fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().getAnagrafica().setNome(soggettoDocumento.getNome());
			fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().getAnagrafica().setCognome(soggettoDocumento.getCognome());
		}
		
		if(soggettoDocumento.getDenominazione() != null) {
			if (soggettoDocumento.getDenominazione().length() > 80)
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().getAnagrafica().setDenominazione(soggettoDocumento.getDenominazione().substring(0, 80));
			else
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().getAnagrafica().setDenominazione(soggettoDocumento.getDenominazione());
		}
		
		
		
		IndirizzoSoggetto ind=null;
		for (IndirizzoSoggetto i: soggettoDocumento.getIndirizzi() ) {
			if (i.isCheckPrincipale())
			{
				ind=i;
				break;
			}
		}
	
		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getSede().setComune(ind.getComune());
		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getSede().setCAP(ind.getCap());
		
		if((ind.getProvincia() != null) && (!ind.getProvincia().equalsIgnoreCase("")))
			fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getSede().setProvincia(ind.getProvincia());

		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getSede().setIndirizzo(ind.getDenominazione());
		
		if((ind.getNumeroCivico() != null) && (!ind.getNumeroCivico().equalsIgnoreCase("")))
			fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getSede().setNumeroCivico(ind.getNumeroCivico());
		
		// Fix per decodifica codice nazione = 1 --> IT
		if (!"".equals(ind.getCodiceNazione()) && ind.getCodiceNazione() != null) {
			if ("1".equals(ind.getCodiceNazione())) {
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getSede().setNazione("IT"); // NAZIONE ITALIA
			} else {
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getSede()
						.setNazione(ind.getCodiceNazione()); // NAZIONE ESTERO
			}
		} 
		

		// VERIFICA TIPO SOGGETTO
		if((soggettoDocumento.getTipoSoggetto() != null) && (soggettoDocumento.getTipoSoggetto().getSoggettoTipoCode() != null)) {
			
			if( (soggettoDocumento.getTipoSoggetto().getSoggettoTipoCode().equalsIgnoreCase("PF")) &&
					(soggettoDocumento.getCodiceFiscale() != null) ) {
				// PERSONA FISICA
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().setCodiceFiscale(soggettoDocumento.getCodiceFiscale().trim());
			}
			else if( (soggettoDocumento.getTipoSoggetto().getSoggettoTipoCode().equalsIgnoreCase("PFI")) && 
					(soggettoDocumento.getPartitaIva() != null) ) {
				// PERSONA FISICA CON PIVA
				IdFiscaleType idFisc =  new IdFiscaleType();
				
				if (!"".equals(ind.getCodiceNazione()) && ind.getCodiceNazione() != null) {
					if ("1".equals(ind.getCodiceNazione())) {
						idFisc.setIdPaese("IT"); // NAZIONE ITALIA
					} else {
						idFisc.setIdPaese(ind.getCodiceNazione()); // NAZIONE ESTERO
					}
				}	

				idFisc.setIdCodice(soggettoDocumento.getPartitaIva());
				
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().setIdFiscaleIVA(idFisc);
			}
			else if( (soggettoDocumento.getTipoSoggetto().getSoggettoTipoCode().equalsIgnoreCase("PG")) &&
						(soggettoDocumento.getCodiceFiscale() != null) ) {
				// PERSONA GIURIDICA SENZA PVIVA
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().setCodiceFiscale(soggettoDocumento.getCodiceFiscale().trim());				
			}
			else if( (soggettoDocumento.getTipoSoggetto().getSoggettoTipoCode().equalsIgnoreCase("PGI")) && 
						(soggettoDocumento.getPartitaIva() != null) ) {
				// PERSONA GIURIDICA
				IdFiscaleType idFisc =  new IdFiscaleType();
				
				if (!"".equals(ind.getCodiceNazione()) && ind.getCodiceNazione() != null) {
					if ("1".equals(ind.getCodiceNazione())) {
						idFisc.setIdPaese("IT"); // NAZIONE ITALIA
					} else {
						idFisc.setIdPaese(ind.getCodiceNazione()); // NAZIONE ESTERO
					}
				}	

				idFisc.setIdCodice(soggettoDocumento.getPartitaIva());
				
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().setIdFiscaleIVA(idFisc);
			}
		}
		
	}
	
	private class AliquotaTotaleSulDocumento{
		private AliquotaIva  aliquotaIva;
		private BigDecimal imponibileTotaleSuDocumento = BigDecimal.ZERO;
		private BigDecimal impostaTotaleSulDocumento = BigDecimal.ZERO;
		private EsigibilitaIVAType esigibilitaIvaType;
		
		public AliquotaTotaleSulDocumento(AliquotaIva aliquotaIva, EsigibilitaIVAType esigibilitaIVA) {
			this.aliquotaIva = aliquotaIva;
			this.esigibilitaIvaType = esigibilitaIVA;
		}
		
		/**
		 * Adds the imponibile totale su documento.
		 *
		 * @param augend the augend
		 */
		public void addImponibileTotaleSuDocumento(BigDecimal augend) {
			this.imponibileTotaleSuDocumento = this.imponibileTotaleSuDocumento.add(augend);
		}
		
		/**
		 * Adds the imposta totale sul documento.
		 *
		 * @param augend the augend
		 */
		public void addImpostaTotaleSulDocumento(BigDecimal augend) {
			this.impostaTotaleSulDocumento = this.impostaTotaleSulDocumento.add(augend);
		}

		/**
		 * @return the aliquotaIva
		 */
		public AliquotaIva getAliquotaIva() {
			return aliquotaIva;
		}

		/**
		 * @return the importoTotaleSulDocumento
		 */
		public BigDecimal getImponibileTotaleSuDocumento() {
			return imponibileTotaleSuDocumento.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		}

		/**
		 * @return the impostaTotaleSulDocumento
		 */
		public BigDecimal getImpostaTotaleSulDocumento() {
			return impostaTotaleSulDocumento.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		}

		/**
		 * @return the esigibilitaIvaType
		 */
		public EsigibilitaIVAType getEsigibilitaIvaType() {
			return esigibilitaIvaType;
		}
	}

	//SIAC-6988 INIZIO
		private void gestioneFattureCollegate(List<DocumentoEntrata> lista){
			List<DatiDocumentiCorrelatiType> listDocCorrelati = new ArrayList<DatiDocumentiCorrelatiType>();
			for(DocumentoEntrata fatturaCollegata : lista){
				DatiDocumentiCorrelatiType documentoCorrelato = new DatiDocumentiCorrelatiType();
				/*fonte documentazione: https://www.fatturapa.gov.it/export/documenti/fatturapa/v1.3/Rappresentazione-tabellare-fattura-ordinaria.pdf
				  <xs:complexType name="DatiDocumentiCorrelatiType">
				    <xs:sequence>
				      <xs:element name="RiferimentoNumeroLinea"    type="RiferimentoNumeroLineaType" minOccurs="0" maxOccurs="unbounded" /> 
				      	--> Linea di dettaglio della fattura a cui si fa riferimento (se il riferimento è all'intera fattura, non viene valorizzato) (vedi elemento informativo 2.2.1.1) elemento informativo 2.2.1: Blocco contenente le linee di dettaglio del documento (gli elementi informativi del blocco si ripetono per ogni riga di dettaglio).
				      
				      <xs:element name="IdDocumento"  type="String20Type" /> 
				      	--> Numero del documento
				      	
				      <xs:element name="Data" type="xs:date" minOccurs="0" /> 
				      	--> Data del documento (secondo il formato ISO 8601:2004) formato ISO 8601:2004, con la precisione seguente: YYYY-MMDD
				      	 I’ll use ISO 8601, an international standard covering the exchange of date and time-related data, as the string format. 
				      	 Date and time expressed according to ISO 8601 is:
							2017-02-16T20:22:28+00:00
							2017-02-16T20:22:28.000+00:00
				      	
				      <xs:element name="NumItem"  type="String20Type" minOccurs="0" /> 
				      	--> Identificativo della singola voce all'interno del documento (ad esempio, nel caso di ordine di acquisto, è il numero della linea dell'ordine di acquisto, oppure, nel caso di contratto, è il numero della linea del contratto, etc. ) 
				      
				      <xs:element name="CodiceCommessaConvenzione" type="String100LatinType" minOccurs="0" /> 
				      	--> Codice della commessa o della convenzione
				      
				      <xs:element name="CodiceCUP" type="String15Type" minOccurs="0" /> 
				      	--> Rappresenta il codice gestito dal CIPE che caratterizza ogni progetto di investimento pubblico (Codice Unitario Progetto) f
				      
				      <xs:element name="CodiceCIG" type="String15Type" minOccurs="0" /> 
				      	--> Rappresenta il Codice Identificativo della Gara 
				    </xs:sequence>
				  </xs:complexType>*/
				/*Dopo call con Laura: inseriamo idDocumento e data presa come dataEmissione della fattura, se presenti anche cig e cup, e tralasciamo gli altri campi*/
				documentoCorrelato.setIdDocumento(fatturaCollegata.getNumero());
				try {
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");// new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String dataString = format.format(fatturaCollegata.getDataEmissione());//fatturaCollegata.getDataCreazioneDocumento()
					Date date = format.parse(dataString);
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(date);
					XMLGregorianCalendar xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
					documentoCorrelato.setData(xmlGregCal);
				} catch (DatatypeConfigurationException e) {
					e.printStackTrace();
				} catch (ParseException pe){
					pe.printStackTrace();
				} catch (Exception pe){
					pe.printStackTrace();
				} 
				//SIAC-6988 : cig e cup potrebbero arrivare con valore stringa vuota, quindi serve controllare
				if(fatturaCollegata.getCig() != null && !fatturaCollegata.getCig().equals(""))
					documentoCorrelato.setCodiceCIG(fatturaCollegata.getCig());
				if(fatturaCollegata.getCup() != null && !fatturaCollegata.getCup().equals(""))
					documentoCorrelato.setCodiceCUP(fatturaCollegata.getCup());
				listDocCorrelati.add(documentoCorrelato);
			}
			fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().setDatiFattureCollegate(listDocCorrelati);
		}
		//SIAC-6988 FINE
}