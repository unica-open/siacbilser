/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.stampa.base.JAXBBaseReportHandler;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model.CapitoloReportModel;
import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model.CartaContabileReportModel;
import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model.DocumentoReportModel;
import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model.ModuloEsteroCartaContabileReportModel;
import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model.MovimentoReportModel;
import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model.PreDocumentiCartaReportModel;
import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model.PreDocumentoCartaReportModel;
import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model.SoggettoCartaReportModel;
import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model.StampaCartaContabileReportModel;
import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model.SubDocumentiReportModel;
import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model.SubDocumentoReportModel;
import it.csi.siac.siacfinser.business.service.stampa.model.CodificaModel;
import it.csi.siac.siacfinser.integration.dad.AccountFinDad;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.CartaEstera;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaCartaContabileReportHandler extends
		JAXBBaseReportHandler<StampaCartaContabileReportModel> {
	
	
	
	@Autowired
	private CodificaDad codificaDad;
	
	private CartaContabile cartaContabile;
	
	@Autowired
	protected AccountFinDad accountFinDad;
	
	@Autowired
	protected SoggettoFinDad soggettoFinDad;

//	private Ente ente;
//	
//	private Richiedente richiedente;
	
	private Bilancio bilancio;
	
	
	private CartaEstera getCartaEstera(){
		CartaEstera cartaEstera = null;
		if(cartaContabile!=null && cartaContabile.getListaCarteEstere()!=null && cartaContabile.getListaCarteEstere().size()>0){
			cartaEstera = cartaContabile.getListaCarteEstere().get(0);
		}
		return cartaEstera;
	}
	
	@Override
	protected void elaborateData() {
		//eventuali pre elaborazioni:
		cartaContabile = impostaIndirizziPrincipaliInPrimaPosizione(cartaContabile);
		//cartaContabile = caricaDatiCompletiSoggettiCessione(cartaContabile);
		cartaContabile = impostaIndirizziPrincipaliInPrimaPosizionePerSoggettiCessione(cartaContabile);
		//
		codificaDad.setEnte(ente);
		popolaStampaCartaContabileReportModel();
		
	}
	
	
	private void popolaStampaCartaContabileReportModel(){
		
		CartaContabileReportModel cartaContabileReportModel = new  CartaContabileReportModel();
		CartaEstera cartaEstera = getCartaEstera();
		
		//FIRMA 1 e FIRMA 2:
		Ente ente = accountFinDad.findEnteAssocciatoAdAccount(richiedente.getAccount().getUid());
		String firma1 = CommonUtils.getCodiceLivelloByTipo(TipologiaGestioneLivelli.FIRMA_CARTA_1, ente);
		String firma2 = CommonUtils.getCodiceLivelloByTipo(TipologiaGestioneLivelli.FIRMA_CARTA_2, ente);
		cartaContabileReportModel.setFirma1(firma1);
		cartaContabileReportModel.setFirma2(firma2);
		//
		
		cartaContabileReportModel.setTitolareFirmaUno(cartaContabile.getFirma1());
		cartaContabileReportModel.setTitolareFirmaDue(cartaContabile.getFirma2());
		
		//VALUTA E IMPORTO
		Valuta valutaDefault = codificaDad.ricercaCodifica(Valuta.class, "EUR");
		BigDecimal importoCarta = cartaContabile.getImporto();
		if(cartaEstera!=null){
			valutaDefault = codificaDad.ricercaCodifica(Valuta.class, cartaEstera.getValuta().getCodice());
			importoCarta = cartaEstera.getTotaleValutaEstera();
		}
		cartaContabileReportModel.setDivisa(valutaDefault.getCodice());
		cartaContabileReportModel.setImporto(importoCarta);
		cartaContabileReportModel.setImportoInLettere(CommonUtils.convertiNumeroInLettere(importoCarta));
		result.setValutaDefault(valutaDefault);
		//
		
		//INTESTAZIONE
		
		//ENTE:
		result.setEnte(ente);
		
		
		//SAC:
		if(cartaContabile.getAttoAmministrativo()!=null){
			cartaContabileReportModel.setAttoAmministrativo(cartaContabile.getAttoAmministrativo());
			if(cartaContabile.getAttoAmministrativo().getStrutturaAmmContabile()!=null){
				StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
				strutturaAmministrativoContabile.setDescrizione(cartaContabile.getAttoAmministrativo().getStrutturaAmmContabile().getDescrizione());
				result.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile );
			}
		}
		//
		
		result.setListaDiDistribuzione("fake1; fake2; fake3;");
		
		//CITTA 
		// COME OTTENERE LA CITTA, SILVIA TRAMITE SKYPE MAGGIO 2018:
		//      allora facciamo cosi' per la citta'
		//		siac_r_soggetto_ente_proprietario lega l'ente con un soggetto
		//		l'indirizzo principale ha una citta' ... facciamo che usare sempre quella
		IndirizzoSoggetto indirizzoPrincipaleEnte = soggettoFinDad.getIndizzoPrincipaleEnte(ente.getUid(), null,true);
		if(indirizzoPrincipaleEnte!=null){
			result.setCitta(indirizzoPrincipaleEnte.getComune());
		}
		//
		
		
		result.setData(TimingUtils.convertiDataIn_GgMmYyyy(new Date()));
		result.setIndicazioneDiRegolamento("FAKE in conformita con il regolamento di contabilita punto ecc ecc FAKE");
		
		//nome banca e citta banca
		result.setCittaBanca("cittaBanca fake");
		result.setNomeBanca("nomeBanca fake");
		
		
		//DATI CARTA:
		cartaContabileReportModel.setAnno(cartaContabile.getBilancio().getAnno());
		cartaContabileReportModel.setNumero(cartaContabile.getNumero());
		cartaContabileReportModel.setNumRegistrazione(cartaContabile.getNumRegistrazione());
		
		cartaContabileReportModel.setDataScadenza(cartaContabile.getDataScadenza());
		cartaContabileReportModel.setDataEsecuzionePagamento(cartaContabile.getDataEsecuzionePagamento());
		
		cartaContabileReportModel.setDataCreazione(cartaContabile.getDataCreazione());
		
		cartaContabileReportModel.setOggetto(cartaContabile.getOggetto());
		
		cartaContabileReportModel.setCausale(cartaContabile.getCausale());
		cartaContabileReportModel.setMotivoUrgenza(cartaContabile.getMotivoUrgenza());
		cartaContabileReportModel.setNote(cartaContabile.getNote());
		
		//DATI RIGHE:
		List<PreDocumentoCartaReportModel> listaRighe = popolaListaRigheReportModel(valutaDefault);
		PreDocumentiCartaReportModel preDocumentiCarta = new PreDocumentiCartaReportModel();
		preDocumentiCarta.setPreDocumentoCarta(listaRighe);
		cartaContabileReportModel.setPreDocumentiCarta(preDocumentiCarta );
		
		//MODULO ESTERO:
		if(cartaEstera!=null){
			String tipologiaPagamento = cartaEstera.getTipologiaPagamento();
			ModuloEsteroCartaContabileReportModel moduloEstero = new ModuloEsteroCartaContabileReportModel();
			
			moduloEstero.setTipologiaPagamento(tipologiaPagamento);
			moduloEstero.setIstruzioni(cartaEstera.getIstruzioni());
			moduloEstero.setDiversoTitolare(cartaEstera.getDiversoTitolare());
			
			PreDocumentoCarta preDoc = CommonUtils.getFirst(cartaContabile.getListaPreDocumentiCarta());
			if(preDoc!=null){
				Soggetto soggetto = preDoc.getSoggetto();
				moduloEstero.setDenominazioneSoggetto(soggetto.getDenominazione());
				IndirizzoSoggetto indirizzoPrincipale = findPrincipale(soggetto);
				moduloEstero.setIndirizzoSoggetto(indirizzoPrincipale.getIndirizzoFormattato());
				ModalitaPagamentoSoggetto modPag = CommonUtils.getFirst(soggetto.getModalitaPagamentoList());
				if(modPag!=null){
					moduloEstero.setIbanOConto(modPag.getIban());
					moduloEstero.setBic(modPag.getBic());
				}
			}
			
			moduloEstero.setCausale(cartaEstera.getCausalePagamento());
			moduloEstero.setCommissioniEstero(cartaEstera.getCommissioniEstero().getCodice());
			cartaContabileReportModel.setModuloEstero(moduloEstero);
		}
		
		//
		
		result.setCartaContabile(cartaContabileReportModel);
		
		//DATA DELLA STAMPA:
		result.setDataStampa(new Date());
	}

	private List<SubDocumentoReportModel> popolaListaSubDoccumentiReportModel(PreDocumentoCarta preDocIt) {
		List<SubdocumentoSpesa> subDocs = preDocIt.getListaSubDocumentiSpesaCollegati();
		List<SubDocumentoReportModel> listaSubDoc = new ArrayList<SubDocumentoReportModel>();
		if(!StringUtils.isEmpty(subDocs)){
			for(SubdocumentoSpesa it: subDocs){
				SubDocumentoReportModel reportIt = buildSubDocumentoReportModel(it);
				listaSubDoc.add(reportIt);
			}
		}
		return listaSubDoc;
	}
	
	private SubDocumentoReportModel buildSubDocumentoReportModel(SubdocumentoSpesa subDocSpesa){
		SubDocumentoReportModel subDocReport = new SubDocumentoReportModel();
		if(subDocSpesa!=null){
			if(subDocSpesa.getSiopeAssenzaMotivazione()!=null){
				subDocReport.setMotivazioneAssenzaCig(buildCodifica(subDocSpesa.getSiopeAssenzaMotivazione().getDescrizione(), subDocSpesa.getSiopeAssenzaMotivazione().getCodice()));
			}
			subDocReport.setCig(subDocSpesa.getCig());
			subDocReport.setCup(subDocSpesa.getCup());
			if(subDocSpesa.getNumero()!=null){
				subDocReport.setNumero(subDocSpesa.getNumero().toString());
			}
			
			//impostiamo i dati del documento:
			subDocReport.setDocumento(buildDocumentoReportModel(subDocSpesa.getDocumento()));
			
		}
		return subDocReport;
	}
	
	private DocumentoReportModel buildDocumentoReportModel(DocumentoSpesa docSpesa){
		DocumentoReportModel docReport = new DocumentoReportModel();
		if(docSpesa!=null){
			if(docSpesa.getNumero()!=null){
				docReport.setNumero(docSpesa.getNumero().toString());
			}
			if(docSpesa.getAnno()!=null){
				docReport.setAnno(docSpesa.getAnno().toString());
			}
			if(docSpesa.getTipoDocumento()!=null){
				docReport.setTipo(buildCodifica(docSpesa.getTipoDocumento().getDescrizione(), docSpesa.getTipoDocumento().getCodice()));
			}
		}
		return docReport;
	}

	private List<PreDocumentoCartaReportModel> popolaListaRigheReportModel(Valuta valutaDefault) {
		List<PreDocumentoCartaReportModel> listaRighe = new ArrayList<PreDocumentoCartaReportModel>();
		
		if(cartaContabile!=null && cartaContabile.getListaPreDocumentiCarta()!=null && cartaContabile.getListaPreDocumentiCarta().size()>0){
			
			for(PreDocumentoCarta preDocIt : cartaContabile.getListaPreDocumentiCarta()){
				
				PreDocumentoCartaReportModel preDocModel = new PreDocumentoCartaReportModel();
				
				//IMPORTO E VALUTA:
				if(getCartaEstera()!=null){
					preDocModel.setImporto(preDocIt.getImportoValutaEstera());
					preDocModel.setImportoInLettere(CommonUtils.convertiNumeroInLettere(preDocIt.getImportoValutaEstera()));
				} else {
					preDocModel.setImporto(preDocIt.getImporto());
					preDocModel.setImportoInLettere(CommonUtils.convertiNumeroInLettere(preDocIt.getImporto()));
				}
				preDocModel.setValuta(valutaDefault.getCodice());
				//////////////////////////////
				
				//PROGRESSIVO DI RIGA:
				preDocModel.setNumero(preDocIt.getNumero());
				//
				
				//SOGGETTO
				SoggettoCartaReportModel soggettoReportModel = popolaSoggettoReportModel(preDocIt.getSoggetto());
				
				preDocModel.setSoggetto(soggettoReportModel);
				//
				
				//MODALITA PAGAMENTO:
				preDocModel.setModalitaPagamentoSoggetto(preDocIt.getModalitaPagamentoSoggetto());
				//
				
				//SOGGETTO CESSIONE:
				if(preDocIt.getModalitaPagamentoSoggetto()!=null && preDocIt.getModalitaPagamentoSoggetto().getSoggettoCessione()!=null){
					Soggetto soggCessione = preDocIt.getModalitaPagamentoSoggetto().getSoggettoCessione();
					SoggettoCartaReportModel soggettoCessioneReportModel = popolaSoggettoReportModel(soggCessione);
					preDocModel.setSoggettoCc(soggettoCessioneReportModel);
					//mod pag cc:
					ModalitaPagamentoSoggetto modPagCc = preDocIt.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2();
					preDocModel.setModalitaPagamentoSoggettoCc(modPagCc);
				}
				
				//conto tesoriere:
				preDocModel.setContoTesoriere(buildCodificaContoTesoreria(preDocIt.getContoTesoreria()));
				
				//IMPEGNO:
				Impegno impegno = preDocIt.getImpegno();
				if(impegno!=null){
					MovimentoReportModel impegnoModel = buildImpegnoReportModel(preDocIt);
					preDocModel.setImpegno(impegnoModel);
				}
				
				
				//DATI SUB DOCUMENTI:
				List<SubDocumentoReportModel> listaSubDoc = popolaListaSubDoccumentiReportModel(preDocIt);
				SubDocumentiReportModel subDocumenti = new SubDocumentiReportModel();
				subDocumenti.setSubDocumentoReportModel(listaSubDoc);
				preDocModel.setSubDocumenti(subDocumenti);
				
				
				//setto in lista righe:
				listaRighe.add(preDocModel);
				
			}
		}
		
		return listaRighe;
	}

	private MovimentoReportModel buildImpegnoReportModel(PreDocumentoCarta preDocIt){
		//IMPEGNO:
		MovimentoReportModel impegnoModel = new MovimentoReportModel();
		Impegno impegno = preDocIt.getImpegno();
		if(impegno!=null){
			
			impegnoModel.setAnno(Integer.toString(impegno.getAnnoMovimento()));
			if(impegno.getNumero()!=null){
				impegnoModel.setNumero(impegno.getNumero().toString());
			}
			
			SubImpegno subImpegno = preDocIt.getSubImpegno();
			if(subImpegno!=null && subImpegno.getNumero()!=null){
				impegnoModel.setNumeroSub(subImpegno.getNumero().toString());
			}
			
			//CIG E CUP:
			impegnoModel.setCig(impegno.getCig());
			impegnoModel.setCup(impegno.getCup());
			if(impegno.getSiopeAssenzaMotivazione()!=null){
				impegnoModel.setMotivazioneAssenzaCig(buildCodifica(impegno.getSiopeAssenzaMotivazione().getDescrizione(), impegno.getSiopeAssenzaMotivazione().getCodice()));
			}
			//
			
			//PDC:
			impegnoModel.setCodPdc(impegno.getCodPdc());
			
			//  SIAC-5177  Trans.Elem. - V liv - Trans.EU - COFOG:
			impegnoModel.setCodCofog(impegno.getCodCofog());
			impegnoModel.setCodTransazioneEuropeaSpesa(impegno.getCodTransazioneEuropeaSpesa());
			impegnoModel.setCodSiope(impegno.getCodSiope());
			impegnoModel.setCodRicorrenteSpesa(impegno.getCodRicorrenteSpesa());
			impegnoModel.setCodCapitoloSanitarioSpesa(impegno.getCodCapitoloSanitarioSpesa());
			impegnoModel.setCodPrgPolReg(impegno.getCodPrgPolReg());
			
			//capitolo:
			CapitoloReportModel capitoloModel = buildCapitoloModel(impegno.getCapitoloUscitaGestione());
			impegnoModel.setCapitolo(capitoloModel);
			
			
			//UNA VOLTA ERA COSI:
			//IMPEGNO:
//			SubImpegno subImpegno = preDocIt.getSubImpegno();
//			String impegnoDiRiferimento = impegno.getAnnoMovimento() + "/" + impegno.getNumero();
//			if(subImpegno!=null){
//				impegnoDiRiferimento = impegnoDiRiferimento + "/" + subImpegno.getNumero();
//			}
//			preDocModel.setImpegno(impegnoDiRiferimento);
			//
			
		}
		return impegnoModel;
	}
	
	private CapitoloReportModel buildCapitoloModel(CapitoloUscitaGestione capitolo){
		CapitoloReportModel capitoloModel = new CapitoloReportModel();
		if(capitolo!=null){
			if(capitolo.getNumeroArticolo()!=null){
				capitoloModel.setNumeroArticolo(capitolo.getNumeroArticolo().toString());
			}
			if(capitolo.getNumeroCapitolo()!=null){
				capitoloModel.setNumeroCapitolo(capitolo.getNumeroCapitolo().toString());
			}
			if(capitolo.getAnnoCapitolo()!=null){
				capitoloModel.setAnnoCapitolo(capitolo.getAnnoCapitolo().toString());
			}
			
			// SIAC-5177  PROGRAMMA TITOLO MISSIONE:
			capitoloModel.setProgramma(buildCodificaProgramma(capitolo.getProgramma()));
			capitoloModel.setTitolo(buildCodificaTitoloSpesa(capitolo.getTitoloSpesa()));
			capitoloModel.setMissione(buildCodificaMissione(capitolo.getMissione()));
			
			//una volta era cosi:
//			CapitoloUscitaGestione capitoloUscitaGestione = impegno.getCapitoloUscitaGestione();
//			String capitoloDiRiferimento = capitoloUscitaGestione.getAnnoCapitolo() + "/" +capitoloUscitaGestione.getNumeroCapitolo() + "/" +capitoloUscitaGestione.getNumeroArticolo();
//			preDocModel.setCapitoloDiRiferimento(capitoloDiRiferimento);
			//
		}
		return capitoloModel;
	}
	
	/**
	 * Costruisce un model codice-descrizio per l'oggetto Missione
	 * @param missione
	 * @return
	 */
	private CodificaModel buildCodificaMissione(Missione missione) {
		CodificaModel codifica = null;
		if(missione!=null){
			//diverso da null
			codifica = buildCodifica(missione.getDescrizione(), missione.getCodice());
		}
		return codifica;
	}
	
	/**
	 * Costruisce un model codice-descrizio per l'oggetto ContoTesoreria
	 * @param missione
	 * @return
	 */
	private CodificaModel buildCodificaContoTesoreria(ContoTesoreria obj) {
		CodificaModel codifica = null;
		if(obj!=null){
			//diverso da null
			codifica = buildCodifica(obj.getDescrizione(), obj.getCodice());
		}
		return codifica;
	}

	/**
	 * Costruisce un model codice-descrizio per l'oggetto TitoloSpesa
	 * @param titoloSpesa
	 * @return
	 */
	private CodificaModel buildCodificaTitoloSpesa(TitoloSpesa titoloSpesa) {
		CodificaModel codifica = null;
		if(titoloSpesa!=null){
			//diverso da null
			codifica = buildCodifica(titoloSpesa.getDescrizione(), titoloSpesa.getCodice());
		}
		//ritorno il model di codifica:
		return codifica;
	}

	/**
	 * Costruisce un model codice-descrizio per l'oggetto Programma
	 * @param programma
	 * @return
	 */
	private CodificaModel buildCodificaProgramma(Programma programma) {
		CodificaModel codifica = null;
		if(programma!=null){
			//diverso da null
			
			String codice = programma.getCodice();
			
			if(codice!=null && codice.length()>=2){
				//assumiamo che il codice sia di quattro caratteri, i cui ultimi due sono quelli del programma
				codice = codice.substring(codice.length()-2, codice.length());
			}
			
			codifica = buildCodifica(programma.getDescrizione(),codice );
		}
		//ritorno il model di codifica:
		return codifica;
	}

	private SoggettoCartaReportModel popolaSoggettoReportModel(Soggetto soggetto) {
		SoggettoCartaReportModel soggettoReportModel = new SoggettoCartaReportModel();
		soggettoReportModel.setCodiceFiscale(soggetto.getCodiceFiscale());
		soggettoReportModel.setCodiceSoggetto(soggetto.getCodiceSoggetto());
		soggettoReportModel.setDenominazione(soggetto.getDenominazione());
		//l'indirizzo principale e' gia in prima posizione (garantito da impostaIndirizziPrincipaliInPrimaPosizione)
		soggettoReportModel.setIndirizzoPrincipale(soggetto.getIndirizzi().get(0));
		//
		soggettoReportModel.setPartitaIva(soggetto.getPartitaIva());
		return soggettoReportModel;
	}
	
	private SoggettoCartaReportModel popolaSoggettoReportModelFake(IndirizzoSoggetto indirizzo) {
		SoggettoCartaReportModel soggettoReportModel = new SoggettoCartaReportModel();
		soggettoReportModel.setCodiceFiscale("CODFSC243545656G");
		soggettoReportModel.setCodiceSoggetto("435");
		soggettoReportModel.setDenominazione("Soggetto fake");
		//l'indirizzo principale e' gia in prima posizione (garantito da impostaIndirizziPrincipaliInPrimaPosizione)
		soggettoReportModel.setIndirizzoPrincipale(indirizzo);
		//
		soggettoReportModel.setPartitaIva("part iva fake");
		return soggettoReportModel;
	}
	
	/**
	 * Dato che il template assume che l'indirizzo principale dei soggetti sia in prima posizione
	 * ci assicuriamo che lo sia...
	 * @param cartaContabile
	 * @return
	 */
	private CartaContabile impostaIndirizziPrincipaliInPrimaPosizione(CartaContabile cartaContabile){
		if(cartaContabile!=null){
			List<PreDocumentoCarta> listaPreDocRicostruita = new ArrayList<PreDocumentoCarta>();
			List<PreDocumentoCarta> listaPreDoc = cartaContabile.getListaPreDocumentiCarta();
			if(listaPreDoc!=null){
				for(PreDocumentoCarta it : listaPreDoc){
					Soggetto soggettoIt = it.getSoggetto();
					listaPreDocRicostruita.add(indirizziPrincipaliPerPrimi(soggettoIt, it));
				}
				cartaContabile.setListaPreDocumentiCarta(listaPreDocRicostruita);
			}
		}
		return cartaContabile;
	}
	
	private CartaContabile caricaDatiCompletiSoggettiCessione(CartaContabile cartaContabile){
		if(cartaContabile!=null){
			List<PreDocumentoCarta> listaPreDocRicostruita = new ArrayList<PreDocumentoCarta>();
			List<PreDocumentoCarta> listaPreDoc = cartaContabile.getListaPreDocumentiCarta();
			if(listaPreDoc!=null){
				for(PreDocumentoCarta it : listaPreDoc){
					
					if(it.getModalitaPagamentoSoggetto()!=null && it.getModalitaPagamentoSoggetto().getSoggettoCessione()!=null){
						Soggetto soggettoIt = it.getModalitaPagamentoSoggetto().getSoggettoCessione();
						String codiceSoggetto = soggettoIt.getCodiceSoggetto();
						Soggetto soggettoCessione = soggettoFinDad.ricercaSoggetto(Constanti.AMBITO_FIN, ente.getUid(), codiceSoggetto, false, true);
						it.getModalitaPagamentoSoggetto().setSoggettoCessione(soggettoCessione);
						listaPreDocRicostruita.add(it);
					}
					
				}
				cartaContabile.setListaPreDocumentiCarta(listaPreDocRicostruita);
			}
		}
		return cartaContabile;
	}
	
	private CartaContabile impostaIndirizziPrincipaliInPrimaPosizionePerSoggettiCessione(CartaContabile cartaContabile){
		if(cartaContabile!=null){
			List<PreDocumentoCarta> listaPreDocRicostruita = new ArrayList<PreDocumentoCarta>();
			List<PreDocumentoCarta> listaPreDoc = cartaContabile.getListaPreDocumentiCarta();
			if(listaPreDoc!=null){
				for(PreDocumentoCarta it : listaPreDoc){
					
					if(it.getModalitaPagamentoSoggetto()!=null && it.getModalitaPagamentoSoggetto().getSoggettoCessione()!=null){
						Soggetto soggettoIt = it.getModalitaPagamentoSoggetto().getSoggettoCessione();
						
						if(!StringUtils.isEmpty(soggettoIt.getIndirizzi())){
							//se un giorno verranno gia caricati dal ricerca carta passera' da qui
							listaPreDocRicostruita.add(indirizziPrincipaliPerPrimi(soggettoIt, it));
						} else {
							//ma ad oggi il ricerca carta non li carica e li carichiamo al volo:
							String codiceSoggetto = soggettoIt.getCodiceSoggetto();
							IndirizzoSoggetto indirizzoPrincipale = soggettoFinDad.getIndizzoPrincipaleSoggetto(ente.getUid(), codiceSoggetto , Constanti.AMBITO_FIN, null, true);
							soggettoIt.setIndirizzi(CommonUtils.toList(indirizzoPrincipale));
							it.getModalitaPagamentoSoggetto().setSoggettoCessione(soggettoIt);
							listaPreDocRicostruita.add(it);
						}
					}
					
				}
				cartaContabile.setListaPreDocumentiCarta(listaPreDocRicostruita);
			}
		}
		return cartaContabile;
	}
	
	private PreDocumentoCarta indirizziPrincipaliPerPrimi(Soggetto soggettoIt,PreDocumentoCarta it){
		List<IndirizzoSoggetto> indirizzi = soggettoIt.getIndirizzi(); 
		List<IndirizzoSoggetto> indirizziGenerici = new ArrayList<IndirizzoSoggetto>();
		List<IndirizzoSoggetto> indirizzoPrincipale = new ArrayList<IndirizzoSoggetto>();
		 
		 for(IndirizzoSoggetto indirizzoIt : indirizzi){
			 if(StringUtils.isTrue(indirizzoIt.getPrincipale())){
				 indirizzoPrincipale.add(indirizzoIt);
			 } else {
				 indirizziGenerici.add(indirizzoIt);
			 }
		 }
		 
		 indirizzi = new ArrayList<IndirizzoSoggetto>();
		 //per primo quello principale:
		 indirizzi.addAll(indirizzoPrincipale);
		 //gli altri a seguire:
		 indirizzi.addAll(indirizziGenerici);
		 
		 soggettoIt.setIndirizzi(indirizzi);
		 it.setSoggetto(soggettoIt);
		 
		 return it;
		 
	}
	
	
	private IndirizzoSoggetto findPrincipale(Soggetto soggetto){
		List<IndirizzoSoggetto> indirizzi = soggetto.getIndirizzi();
		IndirizzoSoggetto principale = null;
		if(indirizzi!=null && indirizzi.size()>0){
			for(IndirizzoSoggetto indirizzoIterato : indirizzi){
				if(StringUtils.isTrue(indirizzoIterato.getPrincipale())){
					principale = indirizzoIterato;
					break;
				}
			}
		}
		return principale;
	}
	
	/**
	 * Costruisce un oggetto CodificaModel
	 * @param desc
	 * @param code
	 * @return
	 */
	private CodificaModel buildCodifica(String desc, String code){
		CodificaModel codifica = new CodificaModel();
		//setto il codice:
		codifica.setCodice(code);
		//setto la descrizioe:
		codifica.setDescrizione(desc);
		//ritorno il model di codifica:
		return codifica;
	}
	
	@Override
	public String getCodiceTemplate() {
		//final String methodName = "getCodiceTemplate";
		//TODO messo a caso verificare:
		//return "StampaCartaContabile_" + cartaContabile.getNumero();
		
		return "stampaCartaContabile";
	}

	@Override
	protected void handleResponse(GeneraReportResponse res) {
		
		res.getErrori();
		persistiStampaFile(res);
		
		
	}
	/*
	 * Persiste la stampa  su database.
	 * 
	 * @param res la risposta del metodo di generazione del report
	 */
	private void persistiStampaFile(GeneraReportResponse res) {
		final String methodName = "persistiStampaFile";
		log.debug(methodName, "Persistenza della stampa");
		
		
		//TODO: punto aperto, che dad usare in questo caso
		
//		stampeCassaFileDad.setEnte(ente);
//		stampeCassaFileDad.setLoginOperazione(getRichiedente().getOperatore().getCodiceFiscale());
//		StampeCassaFile stampeCF = stampeCassaFileDad.inserisciStampa(creaStampaRicevuta(res));
	
		
		//log.info(methodName, "Stampa terminata con successo. Uid record inserito su database: " + stampeCF.getUid());
	}

	public CartaContabile getCartaContabile() {
		return cartaContabile;
	}

	public void setCartaContabile(CartaContabile cartaContabile) {
		this.cartaContabile = cartaContabile;
	}

	public Ente getEnte() {
		return ente;
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	public Richiedente getRichiedente() {
		return richiedente;
	}

	public void setRichiedente(Richiedente richiedente) {
		this.richiedente = richiedente;
	}

	public Bilancio getBilancio() {
		return bilancio;
	}

	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

}
