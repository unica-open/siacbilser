/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.carta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.ModelUtils;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.CollegaQuotaDocumentoARigaCarta;
import it.csi.siac.siacfinser.frontend.webservice.msg.CollegaQuotaDocumentoARigaCartaResponse;
import it.csi.siac.siacfinser.integration.dad.CartaContabileDad;
import it.csi.siac.siacfinser.integration.dad.SubdocumentoSpesaDadCustom;
import it.csi.siac.siacfinser.integration.dao.common.dto.CollegaQuotaDocumentoARigaCartaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.errore.ErroreFin;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CollegaQuotaDocumentoARigaCartaService extends AbstractBaseService<CollegaQuotaDocumentoARigaCarta, CollegaQuotaDocumentoARigaCartaResponse> {

	@Autowired
	private CartaContabileDad cartaContabileDad;
	
	@Autowired
	private SubdocumentoSpesaDadCustom subdocumentoSpesaDadCustom;

	@Autowired
	private DocumentoSpesaService documentoSpesaService;
	
	private static final String ANNO_BILANCIO = "ANNO BILANCIO";
	private static final String BILANCIO = "BILANCIO";
	private static final String RICHIEDENTE = "RICHIEDENTE";
	private static final String ENTE = "ENTE";
	private static final String DOCUMENTO_DA_COLLEGARE = "DOCUMENTO DA COLLEGARE";
	private static final String RIGA_CARTA_CONTABILE = "RIGA CARTA CONTABILE";
	private static final String CARTA_CONTABILE = "CARTA CONTABILE";
	
	@Override
	protected void init() {
		final String methodName="CollegaQuotaDocumentoARigaCartaService : init()";
		log.debug(methodName, " - Begin");

	}	
	
	@Override
	@Transactional
	public CollegaQuotaDocumentoARigaCartaResponse executeService(CollegaQuotaDocumentoARigaCarta serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		final String methodName = "CollegaQuotaDocumentoARigaCartaService - execute()";
		log.debug(methodName, " - Begin");
		
		String codiceAmbito = Constanti.AMBITO_FIN;
		
		Ente ente = req.getEnte();
		Integer annoBilancio = req.getBilancio().getAnno();
		Richiedente richiedente = req.getRichiedente();
		PreDocumentoCarta rigaRichiesta = req.getRigaCarta();
		Integer numeroRiga = rigaRichiesta.getNumero();
		Integer numeroCarta = req.getCartaContabile().getNumero();
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.MODIFICA, annoBilancio);
		
		CollegaQuotaDocumentoARigaCartaInfoDto collegaQuotaInfo = null;
		
//		RicercaCartaContabileK pk = new RicercaCartaContabileK();
//		pk.setCartaContabile(req.getCartaContabile());
//		pk.setBilancio(req.getBilancio());
//		CartaContabile cartaContabile = cartaContabileDad.ricercaCartaContabile(pk , richiedente, codiceAmbito, ente, datiOperazione, false);
		
		//Invece che invocare il ricercaCartaContabile per chiave richiamo un metodo di caricamento personalizzato
		//per i soli dati minimi necessari (tale metodo effettua anche i controlli di merito sulla carta: esistenza, stato, ecc):
		collegaQuotaInfo = cartaContabileDad.caricaDatiCartaPerCollegaQuotaDocumentoARigaCarta(numeroCarta, numeroRiga, req.getBilancio(), richiedente, codiceAmbito, datiOperazione);
		if(collegaQuotaInfo.presenzaErrori()){
			res.setErrori(collegaQuotaInfo.getListaErrori());
			return;
		}
		//
		
		//Proseguiamo con il caricamento del documento:
		//(tale metodo effettua anche i controlli di merito sul documento: esistenza, ecc):
		collegaQuotaInfo = caricaDatiDocumentoPerCollegaQuotaDocumentoARigaCarta(collegaQuotaInfo, datiOperazione);
		if(collegaQuotaInfo.presenzaErrori()){
			res.setErrori(collegaQuotaInfo.getListaErrori());
			return;
		}
		//
		
		//Ulteriori controlli che possono essere svolti adesso che abbiamo caricato e appurato l'esistenza
		// della carta e del documento indicati:
		collegaQuotaInfo = controlliDiMerito(collegaQuotaInfo, datiOperazione);
		if(collegaQuotaInfo.presenzaErrori()){
			res.setErrori(collegaQuotaInfo.getListaErrori());
			return;
		}
		
		//leggo i dati necessari per l'esecuzione del collegamento:
		CartaContabile cartaRicaricata = collegaQuotaInfo.getCartaContabile();
		int uidCartaContabile = cartaRicaricata.getUid();
		SubdocumentoSpesa subDocRicaricato = collegaQuotaInfo.getSubdocumentoSpesa();
		
		//qui abbiamo superato tutti i controlli possiamo
		//richiamare il metodo "core" che si occupa di collegare i dati:
		cartaContabileDad.collegaQuotaDocumentoARigaCarta(uidCartaContabile, numeroRiga, subDocRicaricato, req.getBilancio(), datiOperazione);

		res.setErrori(null);
		res.setEsito(Esito.SUCCESSO);
	}
	
	/**
	 * Si occupa di caricare i soli dati del documento necessari all'esecuzione
	 * di tale servizio.
	 * 
	 * Oltre al caricamento effettua anche i controlli di merito
	 * che dipendono solo dal documento (esistenza, stato accettabile, ecc).
	 * @param collegaQuotaInfo
	 * @return
	 */
	private CollegaQuotaDocumentoARigaCartaInfoDto caricaDatiDocumentoPerCollegaQuotaDocumentoARigaCarta(CollegaQuotaDocumentoARigaCartaInfoDto collegaQuotaInfo,DatiOperazioneDto datiOperazione){
		
		//Costruisco la request
		RicercaDettaglioDocumentoSpesa reqDettDoc = buildRequestPerRicercaDoc();
		
		//Richiamo il servizio di caricamento:
		RicercaDettaglioDocumentoSpesaResponse respDettDoc = documentoSpesaService.ricercaDettaglioDocumentoSpesa(reqDettDoc);
		
		//Analizziamo la response:
		String numeroDocumento = reqDettDoc.getDocumentoSpesa().getNumero();
		Errore erroreDocNonTrovato = ErroreCore.ENTITA_NON_TROVATA.getErrore("Documento Spesa",numeroDocumento);
		if(respDettDoc==null || isEmpty(respDettDoc.getDocumento().getNumero())){
			collegaQuotaInfo.addErrore(erroreDocNonTrovato);
			return collegaQuotaInfo;
		} else if(!isEmpty(respDettDoc.getErrori()) && !Esito.SUCCESSO.equals(respDettDoc.getEsito())){
			collegaQuotaInfo.addErrore(erroreDocNonTrovato);
			collegaQuotaInfo.addErrore(respDettDoc.getErrori().get(0));
			return collegaQuotaInfo;
		}  else if(!Esito.SUCCESSO.equals(respDettDoc.getEsito())){
			collegaQuotaInfo.addErrore(erroreDocNonTrovato);
			collegaQuotaInfo.addErrore(ErroreBil.ERRORE_GENERICO.getErrore("ricercaDettaglioDocumentoSpesa: esito negativo"));
			return collegaQuotaInfo;
		}
		//
		
		//Documento spesa appena caricato:
		DocumentoSpesa documentoSpesa = respDettDoc.getDocumento();
		
		
		//deve avere un soggetto (che andra' poi confrontanto con quello della carta in controlli successivi a questo metodo):
		if(documentoSpesa.getSoggetto()==null || StringUtils.isEmpty(documentoSpesa.getSoggetto().getCodiceSoggetto())){
			String messaggio = numeroDocumento + " privo di Soggetto ";
			collegaQuotaInfo.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Documento spesa", messaggio));
			return collegaQuotaInfo;
		}
		
		
		//LO STATO NON DEVE ESSERE ANNULLATO:
		if(documentoSpesa.getStato().equals(StatoOperativoDocumento.ANNULLATO)){
			collegaQuotaInfo.addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Documento Spesa" + " " +numeroDocumento," annullato"));
			return collegaQuotaInfo;
		}
		//
		
		//I Tipi documento accettati possono essere tutti (compreso ALG) si esclude solo NCD e CCN
		if(documentoSpesa.getTipoDocumento().getCodice().equals("NCD") || 
				documentoSpesa.getTipoDocumento().getCodice().equals("CCN")){
			collegaQuotaInfo.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Documento Spesa", "Il documento non puo' essere di tipo NCD o CCN"));
			return collegaQuotaInfo;
		}
		//
		
		//Verifichiamo che il numero di sub doc richiesto corrisponda ad un
		//sub documento esistente per il documento richiesto:
		List<SubdocumentoSpesa> listaSubDoc = documentoSpesa.getListaSubdocumenti();
		SubdocumentoSpesa subDocSpesaDaCollegare = req.getSubDocumentoDaCollegare();
		Integer numeroSubDocRichiesto = subDocSpesaDaCollegare.getNumero();
		SubdocumentoSpesa subDocRicaricato = ModelUtils.getSubdocumentoSpesaByNumero(listaSubDoc,numeroSubDocRichiesto);
		if(subDocRicaricato==null || !CommonUtils.maggioreDiZero(subDocRicaricato.getNumero())){
			String messaggio = numeroSubDocRichiesto + " per il documento " + numeroDocumento;
			collegaQuotaInfo.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Sub Documento Spesa", messaggio));
			return collegaQuotaInfo;
		}
		//
		
		//I SUB-DOCUMENTI NON DEVONO ESSERE GIA' COLLEGATI ALLE CARTE
		if(cartaContabileDad.isCollegatoACarta(subDocSpesaDaCollegare.getUid(), datiOperazione)){
			collegaQuotaInfo.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Sub Documento Spesa", "Gia' collegato ad una carta contabile"));
			return collegaQuotaInfo;
		}
		
		//non deve essere gia pagato da un ordinativo di pagamento:
		boolean giaPagato = subdocumentoSpesaDadCustom.giaPagatoDaOrdinativoSpesa(subDocRicaricato.getUid(), datiOperazione);
		if(giaPagato){
			collegaQuotaInfo.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Sub Documento Spesa", "Gia' pagato da un ordinativo di spesa"));
			return collegaQuotaInfo;
		}
		
		
		//settto i dati caricati correttamente:
		collegaQuotaInfo.setDocumentoSpesa(documentoSpesa);
		collegaQuotaInfo.setSubdocumentoSpesa(subDocRicaricato);
		//
		
		return collegaQuotaInfo;
	}
	
	/**
	 * Qui vengono implementanti i controlli di merito incrociati (o quelli piu complessi) tra carta e documento indicati.
	 * 
	 * I controlli di merito 'diretti' (esistenza della carta indicata, del documento indicato, ecc) sono gia' stati 
	 * svolti da caricaDatiDocumentoPerCollegaQuotaDocumentoARigaCarta 
	 * e da cartaContabileDad.caricaDatiCartaPerCollegaQuotaDocumentoARigaCarta
	 * 
	 * 
	 * @param collegaQuotaInfo
	 * @param datiOperazione
	 * @return
	 */
	private CollegaQuotaDocumentoARigaCartaInfoDto controlliDiMerito(CollegaQuotaDocumentoARigaCartaInfoDto collegaQuotaInfo,DatiOperazioneDto datiOperazione){
		
		//CartaContabile cartaContabile = collegaQuotaInfo.getCartaContabile();
		PreDocumentoCarta preDocumentoCarta = collegaQuotaInfo.getPreDocumentoCarta();
		DocumentoSpesa documentoSpesa = collegaQuotaInfo.getDocumentoSpesa();
		SubdocumentoSpesa subdocumentoSpesa = collegaQuotaInfo.getSubdocumentoSpesa();
		
		//I Documenti di spesa devono pagare lo stesso soggetto della carta
		if(!documentoSpesa.getSoggetto().getCodiceSoggetto().equals(preDocumentoCarta.getSoggetto().getCodiceSoggetto()) ){
			collegaQuotaInfo.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Sub Documento Spesa", "Il soggetto del documento non e' lo stesso della riga carta"));
			return collegaQuotaInfo;
		}
		
		//I sub-documenti devono essere dello stesso importo della riga carta 
		if(!preDocumentoCarta.getImporto().equals(subdocumentoSpesa.getImporto())){
			collegaQuotaInfo.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Sub Documento Spesa", "Il subdocumento deve essere dello stesso importo della riga carta"));
			return collegaQuotaInfo;
		}
		
		return collegaQuotaInfo;
	}
	
	
	private RicercaDettaglioDocumentoSpesa buildRequestPerRicercaDoc(){
		SubdocumentoSpesa subDocSpesaDaCollegare = req.getSubDocumentoDaCollegare();
		RicercaDettaglioDocumentoSpesa reqDocSpesa = new RicercaDettaglioDocumentoSpesa();
		reqDocSpesa.setDocumentoSpesa(subDocSpesaDaCollegare.getDocumento());
		reqDocSpesa.setRichiedente(req.getRichiedente());
		reqDocSpesa.setDataOra(getNow());
		reqDocSpesa.setAnnoBilancio(req.getAnnoBilancio());
		return reqDocSpesa;
	}
	
	private RicercaModulareDocumentoSpesa buildRequestPerRicercaDocModulare(){
		SubdocumentoSpesa subDocSpesaDaCollegare = req.getSubDocumentoDaCollegare();
		RicercaModulareDocumentoSpesa reqModulare = new RicercaModulareDocumentoSpesa();
		reqModulare.setDocumentoSpesa(subDocSpesaDaCollegare.getDocumento());
		reqModulare.setDocumentoSpesaModelDetails(DocumentoSpesaModelDetail.CodicePCC);
		reqModulare.setSubdocumentoSpesaModelDetails(SubdocumentoSpesaModelDetail.DocPadre);
		reqModulare.setRichiedente(req.getRichiedente());
		reqModulare.setDataOra(getNow());
		reqModulare.setAnnoBilancio(req.getAnnoBilancio());
		return reqModulare;
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {

		final String methodName="CollegaQuotaDocumentoARigaCartaService : checkServiceParam()";
		log.debug(methodName, " - Begin");

		/*
		 * Verifica parametri di input
		 * Il servizio controlla che tutti i parametri di input siano stati inizializzati, in caso contrario viene segnalato l'errore
		 * <COR_ERR_0003.Parametro non inizializzato (elenco parametri mancanti)>
		 * 
		 * In particolare controlla che nella "Lista PreDocumenti da definire"  esista almeno un elemento da definire.
		 * 
		 */
		
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		Richiedente richiedente = req.getRichiedente();

		List<String> listParametriNonInizializzati = new ArrayList<String>();

		// verifico obbligatorieta' ente
		if(ente==null){
			listParametriNonInizializzati.add(ENTE);
		}

		// verifico obbligatorieta' bilancio
		if(bilancio==null){
			listParametriNonInizializzati.add(BILANCIO);
		} else if(bilancio.getAnno()==0){
			listParametriNonInizializzati.add(ANNO_BILANCIO);
		}
		
		// verifico obbligatorieta' richiedente
		if(richiedente==null){
			listParametriNonInizializzati.add(RICHIEDENTE);
		}

		
		PreDocumentoCarta preDocumentoCarta = req.getRigaCarta();
		if(preDocumentoCarta==null){
			listParametriNonInizializzati.add(RIGA_CARTA_CONTABILE);
		} else {
			if(!CommonUtils.maggioreDiZero(preDocumentoCarta.getNumero())){
				listParametriNonInizializzati.add("Numero Riga Carta");
			}
		}
		
		SubdocumentoSpesa documentoDaCollegare = req.getSubDocumentoDaCollegare();
		if(documentoDaCollegare==null){
			listParametriNonInizializzati.add(DOCUMENTO_DA_COLLEGARE);
		} else {
			boolean valorizzatoDoc = documentoDaCollegare.getDocumento() != null
					&& documentoDaCollegare.getDocumento().getAnno() != null
					&& documentoDaCollegare.getDocumento().getNumero() != null
					&& documentoDaCollegare.getDocumento().getTipoDocumento() != null
					&& documentoDaCollegare.getDocumento().getSoggetto() != null;
			if(!valorizzatoDoc){
				listParametriNonInizializzati.add(DOCUMENTO_DA_COLLEGARE);
			}
		}
		
		
		CartaContabile cartaContabile = req.getCartaContabile();
		
		if(cartaContabile==null){
			listParametriNonInizializzati.add(CARTA_CONTABILE);
		} else {
			if(!CommonUtils.maggioreDiZero(cartaContabile.getNumero())){
				listParametriNonInizializzati.add("Numero Carta Contabile");
			}
			if(cartaContabile.getBilancio()==null || !CommonUtils.maggioreDiZero(cartaContabile.getBilancio().getAnno())){
				listParametriNonInizializzati.add("Bilancio Carta Contabile");
			}
		}
		
		String elencoParametriNonInizializzati = StringUtils.join(listParametriNonInizializzati, ", ");
		if(StringUtils.isNotEmpty(elencoParametriNonInizializzati)){
			res.setErrori(Arrays.asList(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParametriNonInizializzati)));
			res.setEsito(Esito.FALLIMENTO);
			res.setCartaContabile(null);
		}
	}	
	
}