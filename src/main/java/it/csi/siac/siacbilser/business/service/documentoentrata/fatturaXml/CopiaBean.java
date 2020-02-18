/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata.fatturaXml;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.fattura.xml.AnagraficaType;
import it.csi.siac.fattura.xml.CedentePrestatoreType;
import it.csi.siac.fattura.xml.CessionarioCommittenteType;
import it.csi.siac.fattura.xml.CondizioniPagamentoType;
import it.csi.siac.fattura.xml.ContattiTrasmittenteType;
import it.csi.siac.fattura.xml.DatiAnagraficiCedenteType;
import it.csi.siac.fattura.xml.DatiAnagraficiCessionarioType;
import it.csi.siac.fattura.xml.DatiBeniServiziType;
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
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class CopiaBean {
	
	public CopiaBean() {
		super();
		codIva=new HashMap<String, String>();
		codIva.put("1", "4");
		codIva.put("2", "10");
		codIva.put("3", "21");
		codIva.put("4", "20");
		codIva.put("5", "N4");
		codIva.put("6", "N4");
		codIva.put("8", "N3");
		codIva.put("12", "N3");
		codIva.put("14", "22");
		codIva.put("16", "N6");
		codIva.put("17", "N2");
		codIva.put("20", "N1");
		codIva.put("21", "N6");
		codIva.put("22", "N6");
		codIva.put("25", "4");
		codIva.put("26", "10");
		codIva.put("27", "22");
	}


	public void creaFattura(DocumentoEntrata doc, Soggetto soggetto, SubdocumentoIvaEntrata docIva, IndirizzoSoggetto soggEnte, String partitaIvaFEL) {
		inizializza();
		header(doc, soggetto, soggEnte, partitaIvaFEL);
		body(doc, docIva);
	}
	
	
	private FatturaElettronicaType fattura;
	HashMap<String,String> codIva=null;
	
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
//		fattura.getFatturaElettronicaHeader().getCedentePrestatore().setStabileOrganizzazione(new IndirizzoType());
//		fattura.getFatturaElettronicaHeader().getCedentePrestatore().setIscrizioneREA(new IscrizioneREAType());
//		fattura.getFatturaElettronicaHeader().getCedentePrestatore().setContatti(new ContattiType());
		
//		fattura.getFatturaElettronicaHeader().setRappresentanteFiscale(new RappresentanteFiscaleType());
//		fattura.getFatturaElettronicaHeader().getRappresentanteFiscale().setDatiAnagrafici(new DatiAnagraficiRappresentanteType());
//		fattura.getFatturaElettronicaHeader().getRappresentanteFiscale().getDatiAnagrafici().setIdFiscaleIVA(new IdFiscaleType());
//		fattura.getFatturaElettronicaHeader().getRappresentanteFiscale().getDatiAnagrafici().setAnagrafica(new AnagraficaType());
		
		fattura.getFatturaElettronicaHeader().setCessionarioCommittente(new CessionarioCommittenteType());
		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().setDatiAnagrafici(new DatiAnagraficiCessionarioType());
		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().setAnagrafica(new AnagraficaType());
//		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().setIdFiscaleIVA(new IdFiscaleType());
		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().setSede(new IndirizzoType());
//		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().setStabileOrganizzazione(new IndirizzoType());
//		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().setRappresentanteFiscale(new RappresentanteFiscaleCessionarioType());
//		fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getRappresentanteFiscale().setIdFiscaleIVA(new IdFiscaleType());
		
//		fattura.getFatturaElettronicaHeader().setTerzoIntermediarioOSoggettoEmittente(new TerzoIntermediarioSoggettoEmittenteType());
//		fattura.getFatturaElettronicaHeader().getTerzoIntermediarioOSoggettoEmittente().setDatiAnagrafici(new DatiAnagraficiTerzoIntermediarioType());
//		fattura.getFatturaElettronicaHeader().getTerzoIntermediarioOSoggettoEmittente().getDatiAnagrafici().setAnagrafica(new AnagraficaType());
		
		
		
		FatturaElettronicaBodyType body= new FatturaElettronicaBodyType();
		
		body.setDatiGenerali(new DatiGeneraliType());
		
		body.getDatiGenerali().setDatiGeneraliDocumento(new DatiGeneraliDocumentoType());
//		body.getDatiGenerali().getDatiGeneraliDocumento().setDatiBollo(new DatiBolloType());
//		b.getDatiGenerali().getDatiGeneraliDocumento().setDatiRitenuta(new DatiRitenutaType());
//		DatiCassaPrevidenzialeType c=new DatiCassaPrevidenzialeType();
//		b.getDatiGenerali().getDatiGeneraliDocumento().getDatiCassaPrevidenziale().add(c);
//		ScontoMaggiorazioneType d= new ScontoMaggiorazioneType();
//		b.getDatiGenerali().getDatiGeneraliDocumento().getScontoMaggiorazione().add(d);
//		b.getDatiGenerali().getDatiGeneraliDocumento().getCausale().add(new String());
		
//		b.getDatiGenerali().setDatiTrasporto(new DatiTrasportoType());
//		b.getDatiGenerali().getDatiTrasporto().setDatiAnagraficiVettore(new DatiAnagraficiVettoreType() );
//		b.getDatiGenerali().getDatiTrasporto().getDatiAnagraficiVettore().setIdFiscaleIVA(new IdFiscaleType());
//		b.getDatiGenerali().getDatiTrasporto().getDatiAnagraficiVettore().setAnagrafica(new AnagraficaType());
//		b.getDatiGenerali().getDatiTrasporto().setIndirizzoResa(new IndirizzoType());
		
//		b.getDatiGenerali().setFatturaPrincipale(new FatturaPrincipaleType());
		
		
//		DatiDocumentiCorrelatiType e= new DatiDocumentiCorrelatiType();
//		DatiDocumentiCorrelatiType f= new DatiDocumentiCorrelatiType();
//		DatiDocumentiCorrelatiType g= new DatiDocumentiCorrelatiType();
//		DatiDocumentiCorrelatiType h= new DatiDocumentiCorrelatiType();
//		DatiDocumentiCorrelatiType i= new DatiDocumentiCorrelatiType();
//		b.getDatiGenerali().getDatiContratto().add(e);
//		b.getDatiGenerali().getDatiOrdineAcquisto().add(f);
//		b.getDatiGenerali().getDatiFattureCollegate().add(g);
//		b.getDatiGenerali().getDatiRicezione().add(h);
//		b.getDatiGenerali().getDatiConvenzione().add(i);
//		DatiSALType l= new DatiSALType();
//		b.getDatiGenerali().getDatiSAL().add(l);
//		DatiDDTType m=new DatiDDTType();
//		b.getDatiGenerali().getDatiDDT().add(m);
		
		body.setDatiBeniServizi(new DatiBeniServiziType());
//		DettaglioLineeType n=new DettaglioLineeType();
//		AltriDatiGestionaliType o= new AltriDatiGestionaliType();
//		n.getAltriDatiGestionali().add(o);
//		CodiceArticoloType p=new CodiceArticoloType();
//		n.getCodiceArticolo().add(p);
//		ScontoMaggiorazioneType q= new ScontoMaggiorazioneType();
//		n.getScontoMaggiorazione().add(q);
//		body.getDatiBeniServizi().getDettaglioLinee().add(n);
//		DatiRiepilogoType r=new DatiRiepilogoType();
//		b.getDatiBeniServizi().getDatiRiepilogo().add(r);
//		b.setDatiVeicoli(new DatiVeicoliType());
//		DatiPagamentoType s=new DatiPagamentoType();
//		DettaglioPagamentoType t= new DettaglioPagamentoType();
//		s.getDettaglioPagamento().add(t);
//		b.getDatiPagamento().add(s);
//		AllegatiType u=new AllegatiType();
//		b.getAllegati().add(u);
		
		fattura.getFatturaElettronicaBody().add(body);		
}
	
	public FatturaElettronicaType getFattura() {
		return fattura;
	}
	
	public void header(DocumentoEntrata doc, Soggetto soggetto, IndirizzoSoggetto soggEnte, String partitaIvaFEL) {

		/* 
		 * Dati Trasmissione 
		 * 
		 * */
		
		// Fix per decodifica codice nazione = 1 --> IT
		if (!"".equals(soggEnte.getCodiceNazione()) && soggEnte.getCodiceNazione() != null) {
			if ("1".equals(soggEnte.getCodiceNazione())) {
				fattura.getFatturaElettronicaHeader().getDatiTrasmissione().getIdTrasmittente()
					.setIdPaese("IT"); // NAZIONE ITALIA
			} else {
				fattura.getFatturaElettronicaHeader().getDatiTrasmissione().getIdTrasmittente()
						.setIdPaese(soggEnte.getCodiceNazione()); // NAZIONE ESTERO
			}
		}
		
		fattura.getFatturaElettronicaHeader().getDatiTrasmissione().getIdTrasmittente().setIdCodice(soggetto.getCodiceFiscale().trim());
		fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setProgressivoInvio((new Integer(doc.getUid())).toString());

		fattura.getFatturaElettronicaHeader().getDatiTrasmissione().getContattiTrasmittente().setEmail("assistenza.fel@csi.it");
		fattura.getFatturaElettronicaHeader().getDatiTrasmissione().getContattiTrasmittente().setTelefono("0113168111");

		String codiceDest = "";
		if((soggetto.getCodDestinatario() == null) || (soggetto.getCodDestinatario().equalsIgnoreCase("")))
			codiceDest = "0000000";
		else
			codiceDest = soggetto.getCodDestinatario();
			
		if ("PA".equals(soggetto.getCanalePA())) {
			fattura.setVersione(FormatoTrasmissioneType.FPA_12);
			fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setFormatoTrasmissione(FormatoTrasmissioneType.FPA_12);
			fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setCodiceDestinatario(codiceDest);
			//fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setPECDestinatario(soggetto.getEmailPec());
		} else {
			fattura.setVersione(FormatoTrasmissioneType.FPR_12);
			fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setFormatoTrasmissione(FormatoTrasmissioneType.FPR_12);
			fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setCodiceDestinatario(codiceDest);
			if(codiceDest.equalsIgnoreCase("0000000") && StringUtils.isNotBlank(soggetto.getEmailPec()))
				fattura.getFatturaElettronicaHeader().getDatiTrasmissione().setPECDestinatario(soggetto.getEmailPec());
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
		valorizzaCessionarioCommittente(soggetto);

	}
	
	private void body(DocumentoEntrata doc, SubdocumentoIvaEntrata docIva) {
		

		/* 
		 * Dati Generali 
		 * 
		 */

		if ("FTV".equals(doc.getTipoDocumento().getCodice())) {
			fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setTipoDocumento(TipoDocumentoType.TD_01);
		} else if ("NCV".equals(doc.getTipoDocumento().getCodice())) {
			fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setTipoDocumento(TipoDocumentoType.TD_04);
		}

		fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setDivisa("EUR");
		fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setData(Util.toXMLGregorianCalendar(doc.getDataEmissione()));
//		SIAC-6814: revertata da SIAC-6980
//		fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setNumero(docIva != null && docIva.getNumeroProtocolloDefinitivo() != null? docIva.getNumeroProtocolloDefinitivo().toString() : doc.getNumero());
//      SIAC-6980
		
		
		fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setNumero(doc.getNumero());
		
		
		/*
		 * 2.1.1.5 DATI RITENUTA
		 * 
		 * */
		//fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().set
		

		/*
		 * 2.1.1.5 DATI BOLLO
		 * 
		 * */

		try {
//			if (doc.getCodiceBollo() != null
//					&& UtilScd.isNumber(pagamento.getBollo())
//					&& !"0.00".equals(UtilScd.getStringNumberXML(pagamento.getBollo()))) {
//				fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setDatiBollo(new DatiBolloType());
//				fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getDatiBollo().setBolloVirtuale(BolloVirtualeType.SI);
//				fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getDatiBollo().setImportoBollo(UtilScd.getStringNumberXML(pagamento.getBollo()));
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		fattura.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().setImportoTotaleDocumento(doc.getImporto().setScale(2, BigDecimal.ROUND_HALF_EVEN));

		/* 
		 * Dati Beni Servizi 
		 * 
		 * */
		
		/*
		 * 2.2.1 Dettaglio Linee
		 * 
		 */
		
		DettaglioLineeType dettLinea = null;
		List<SubdocumentoEntrata> listSubDoc = doc.getListaSubdocumenti();
		
		AliquotaSubdocumentoIva subDocIva = null;
		BigDecimal aliqIvaWithDec = new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		String  naturaAliquota = "N4";
		
		String esigibilitaIVA = null;
		
		if(docIva != null) {
			List<AliquotaSubdocumentoIva> listSubDocIva =  docIva.getListaAliquotaSubdocumentoIva();
			
			subDocIva = listSubDocIva.get(0);
			AliquotaIva aliqIva = subDocIva.getAliquotaIva();

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
				if((docIva.getRegistroIva() != null) && (docIva.getRegistroIva().getTipoRegistroIva() != null)) {
					if(docIva.getRegistroIva().getTipoRegistroIva().getCodice().equalsIgnoreCase("VI"))
						esigibilitaIVA = "I";
					else if(docIva.getRegistroIva().getTipoRegistroIva().getCodice().equalsIgnoreCase("VD"))
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
				
					if(subDocIva != null) {
						dettLinea.setPrezzoUnitario(subDocIva.getImponibile());
						dettLinea.setPrezzoTotale(subDocIva.getImponibile());
					
					}
					else {
						dettLinea.setPrezzoUnitario(subDocEnt.getImporto());
						dettLinea.setPrezzoTotale(subDocEnt.getImporto());
					}
					
					dettLinea.setAliquotaIVA(aliqIvaWithDec);
				}
				else {
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
		
		if(subDocIva != null) {
			datiRiep.setImponibileImporto(subDocIva.getImponibile().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			datiRiep.setImposta(subDocIva.getImposta().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			
		}
		else {
			datiRiep.setImponibileImporto(doc.getImporto().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			datiRiep.setImposta(new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_EVEN));
		}
		fattura.getFatturaElettronicaBody().get(0).getDatiBeniServizi().getDatiRiepilogo().add(datiRiep);
		
		
		//SIAC 6677
		/*
		 *2.4.2.21 
		 */
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
				fattura.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0).getDettaglioPagamento().get(0).setModalitaPagamento(ModalitaPagamentoType.MP_04);
			}
			
		}
		
		
		
		
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
	
		
	private void valorizzaCessionarioCommittente(Soggetto soggetto) {
		
		if((soggetto.getNome() != null) && (soggetto.getCognome() != null)) {
			fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().getAnagrafica().setNome(soggetto.getNome());
			fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().getAnagrafica().setCognome(soggetto.getCognome());
		}
		
		if(soggetto.getDenominazione() != null) {
			if (soggetto.getDenominazione().length() > 80)
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().getAnagrafica().setDenominazione(soggetto.getDenominazione().substring(0, 80));
			else
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().getAnagrafica().setDenominazione(soggetto.getDenominazione());
		}
		
		
		
		IndirizzoSoggetto ind=null;
		for (IndirizzoSoggetto i: soggetto.getIndirizzi() ) {
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
		if((soggetto.getTipoSoggetto() != null) && (soggetto.getTipoSoggetto().getSoggettoTipoCode() != null)) {
			
			if( (soggetto.getTipoSoggetto().getSoggettoTipoCode().equalsIgnoreCase("PF")) &&
					(soggetto.getCodiceFiscale() != null) ) {
				// PERSONA FISICA
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().setCodiceFiscale(soggetto.getCodiceFiscale().trim());
			}
			else if( (soggetto.getTipoSoggetto().getSoggettoTipoCode().equalsIgnoreCase("PFI")) && 
					(soggetto.getPartitaIva() != null) ) {
				// PERSONA FISICA CON PIVA
				IdFiscaleType idFisc =  new IdFiscaleType();
				
				if (!"".equals(ind.getCodiceNazione()) && ind.getCodiceNazione() != null) {
					if ("1".equals(ind.getCodiceNazione())) {
						idFisc.setIdPaese("IT"); // NAZIONE ITALIA
					} else {
						idFisc.setIdPaese(ind.getCodiceNazione()); // NAZIONE ESTERO
					}
				}	

				idFisc.setIdCodice(soggetto.getPartitaIva());
				
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().setIdFiscaleIVA(idFisc);
			}
			else if( (soggetto.getTipoSoggetto().getSoggettoTipoCode().equalsIgnoreCase("PG")) &&
						(soggetto.getCodiceFiscale() != null) ) {
				// PERSONA GIURIDICA SENZA PVIVA
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().setCodiceFiscale(soggetto.getCodiceFiscale().trim());				
			}
			else if( (soggetto.getTipoSoggetto().getSoggettoTipoCode().equalsIgnoreCase("PGI")) && 
						(soggetto.getPartitaIva() != null) ) {
				// PERSONA GIURIDICA
				IdFiscaleType idFisc =  new IdFiscaleType();
				
				if (!"".equals(ind.getCodiceNazione()) && ind.getCodiceNazione() != null) {
					if ("1".equals(ind.getCodiceNazione())) {
						idFisc.setIdPaese("IT"); // NAZIONE ITALIA
					} else {
						idFisc.setIdPaese(ind.getCodiceNazione()); // NAZIONE ESTERO
					}
				}	

				idFisc.setIdCodice(soggetto.getPartitaIva());
				
				fattura.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().setIdFiscaleIVA(idFisc);
			}
		}
		
	}
	
}