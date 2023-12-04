/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.carta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RegolarizzaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.RegolarizzaCartaContabileResponse;
import it.csi.siac.siacfinser.integration.dad.CartaContabileDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.RicercaCartaContabileK;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegolarizzaCartaContabileService extends AbstractBaseService<RegolarizzaCartaContabile, RegolarizzaCartaContabileResponse> {

	@Autowired
	private CartaContabileDad cartaContabileDad;

	@Autowired
	private DocumentoSpesaService documentoSpesaService;
	
	
	private static final String IMPEGNO = "IMPEGNO";
	private static final String NUMERO = "NUMERO";
	private static final String LISTA_REGOLARIZZAZIONI ="LISTA REGOLARIZZAZIONI";
	private static final String LISTA_PRE_DOCUMENTI_CARTA_DA_REGOLARIZZARE ="LISTA PRE DOCUMENTI CARTA DA REGOLARIZZARE";
	private static final String CARTA_CONTABILE_DA_REGOLARIZZARE = "CARTA CONTABILE DA REGOLARIZZARE";
	private static final String ANNO_BILANCIO = "ANNO BILANCIO";
	private static final String ANNO_BILANCIO_CARTA_CONTABILE = "ANNO BILANCIO CARTA CONTABILE";
	private static final String BILANCIO = "BILANCIO";
	private static final String BILANCIO_CARTA_CONTABILE = "BILANCIO CARTA CONTABILE";
	private static final String ENTE = "ENTE";
	private static final String IMPORTO = "IMPORTO";
	private static final String IMPORTO_DA_REGOLARIZZARE = "IMPORTO DA REGOLARIZZARE";
	
	
	@Override
	protected void init() {
		final String methodName="RegolarizzaCartaContabileService : init()";
		log.debug(methodName, " - Begin");

	}	
	
	
	@Override
	@Transactional
	public RegolarizzaCartaContabileResponse executeService(RegolarizzaCartaContabile serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		final String methodName = "RegolarizzaCartaContabileService - execute()";
		log.debug(methodName, " - Begin");
		
		Ente ente = req.getEnte();
		Integer annoBilancio = req.getBilancio().getAnno();
		Richiedente richiedente = req.getRichiedente();
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.MODIFICA, annoBilancio);
		
		CartaContabile cartaContabileDaRegolarizzare = req.getCartaContabileDaRegolarizzare();
		Integer annoCarta = cartaContabileDaRegolarizzare.getBilancio().getAnno();
		Integer numeroCarta = cartaContabileDaRegolarizzare.getNumero();
		List<PreDocumentoCarta> listaPreDocumentiCartaDaRegolarizzare = cartaContabileDaRegolarizzare.getListaPreDocumentiCarta();

		List<Errore> listaErrori = new ArrayList<Errore>();

		// controllo l'esistenza della carta contabile da regolarizzare : inizio
		RicercaCartaContabileK rccK = new RicercaCartaContabileK();
		
		rccK.setBilancio(req.getBilancio());
		rccK.setCartaContabile(cartaContabileDaRegolarizzare);
		
		boolean esisteCartaContabile = cartaContabileDad.esisteCartaContabile(rccK, datiOperazione);
		
		if(!esisteCartaContabile){
			listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore(annoCarta + "/" + numeroCarta));
		}
		// controllo l'esistenza della carta contabile da regolarizzare : fine

		if(listaErrori.size()==0){
			for(PreDocumentoCarta preDocumentoCarta : listaPreDocumentiCartaDaRegolarizzare){
				
				/*
				 * Controllo importo : per ciascun elemento della  "Lista PreDocumentiCarta da regolarizzare"
				 *  
				 * Ciascuna riga  deve avere importoDaRegolarizzare >= Sommatoria di importo di ogni elemento di "Lista Regolarizzazioni" ricevuta
				 * L'errore e' segnalato attraverso il messaggio
				 * <FIN_ERR_0187: Importo regolarizzato errato (messaggio aggiuntivo = riga <CartaContabile.anno/numero/PreDocumentocarta.numero> gia' regolarizzata)>  
				 * 
				 */
				
				List<SubdocumentoSpesa> listaRegolarizzazioni = preDocumentoCarta.getListaSubDocumentiSpesaCollegati();
				Integer numeroPreDocumento = preDocumentoCarta.getNumero();
				BigDecimal totaleRegolarizzazioni = BigDecimal.ZERO; 
				for(SubdocumentoSpesa subdocumentoSpesa : listaRegolarizzazioni){
					totaleRegolarizzazioni = totaleRegolarizzazioni.add(subdocumentoSpesa.getImporto());
				}
				// verifica importo da regolarizzare
				if(preDocumentoCarta.getImportoDaRegolarizzare().compareTo(totaleRegolarizzazioni)<0){
					String messaggioAggiuntivo = "riga " + annoCarta + "/" + numeroCarta + "/" + numeroPreDocumento + " gia' regolarizzata";
					listaErrori.add(ErroreFin.IMPORTO_REGOLARIZZATO_ERRATO.getErrore(messaggioAggiuntivo));
				}
			}
		}
		
		if(listaErrori.size()==0){
			/*
			 * Per ciascun elemento della "lista regolarizzazioni" all'interno della "Lista PreDocumentiCarta da regolarizzare" e' creato
			 * un DocumentoSpesa e una quota SubDocumentoSpesa (Mod FIN ) spesa come segue:
			 * 
			 * Inserimento Documento di Spesa 
			 * Viene creato un documento di spesa (DocumentoSpesa Mod FIN) attraverso la chiamata al metodo Inserisce documento spesa del servizio
			 * FINSPES006 - Gestione Documento di Spesa e vanno passate le seguenti informazioni:
			 *
			 * anno documento    -->   anno bilancio corrente passato come dato in input
			 * 
  			 * numero documento  -->   numero "costruito" come risultato di concatenzione di
  			 *                         <annoCarta>/<numeroCarta>/<numeroRiga>/<numero progressivo legami riga-subdocumenti>
  			 *                         
  			 * descrizione       -->   costruita come risultato di concatenazione di
  			 *                         "Regolarizzazione Carta numero : <annoCarta>/<numeroCarta> Riga <numeroRiga>/<descrizioneRigaCarta>/<dataElab>"
  			 *                         
			 * dataEmissione     -->   data di sistema
			 * 
			 * importo           -->   importo del sub-documento ricevuto in input
			 * 
			 * tipo documento    -->   CNN - CARTA CONTABILE (costante)
			 * 
			 * stato operativo   -->   VALIDO (costante)
			 * 
			 * soggetto          -->   soggetto del PreDocumentoCartaContabile ricevuto in input
			 * 
			 * codice bollo      -->   99 - Esente da bollo (costante)
			 */

			for(PreDocumentoCarta preDocumentoCartaIterato : cartaContabileDaRegolarizzare.getListaPreDocumentiCarta()){
				List<SubdocumentoSpesa> listaRegolarizzazioni = preDocumentoCartaIterato.getListaSubDocumentiSpesaCollegati();
				 
				// Per ogni PreDocumentoCarta ricevuto in input, calcolo il progressivo dei legami con SubDocumentiSpesa
				Integer progressivoLegamiRigaSubDocumenti = cartaContabileDad.calcolaProgressivoLegamiPreDocumentoCartaSubDocumenti(cartaContabileDaRegolarizzare,
						                                                                                                            preDocumentoCartaIterato,
						                                                                                                            datiOperazione);

				for(SubdocumentoSpesa subdocumentoSpesaIterato : listaRegolarizzazioni){
					// inserisco il documento di spesa
					InserisceDocumentoSpesa inserisceDocumentoSpesa = new InserisceDocumentoSpesa();

					inserisceDocumentoSpesa.setRichiedente(richiedente);
					inserisceDocumentoSpesa.setBilancio(req.getBilancio());
					
					// 23/09/2014 : aggiunta la valorizzazione di questo parametro
					// che da analisi non era previsto
					inserisceDocumentoSpesa.setInserisciDocumentoRegolarizzazione(false);

					// compongo il numero del documento
					String numeroDocumento = annoCarta + "/" +
					                         numeroCarta + "/" +
							                 preDocumentoCartaIterato.getNumero() + "/" +
					                         progressivoLegamiRigaSubDocumenti;

					// compongo la descrizione del documento
					String dataElaborazione = TimingUtils.convertiDataIn_GgMmYyyy(datiOperazione.getTs());
					String descrizioneDocumento = "Regolarizzazione Carta numero: " + annoCarta + "/" +  numeroCarta +
					                              " Riga: " + preDocumentoCartaIterato.getNumero() + "/" + preDocumentoCartaIterato.getDescrizione() + 
					                              "/" + dataElaborazione;

					// tipo documento
					TipoDocumento tipoDocumento = new TipoDocumento();
					tipoDocumento = commonDad.getTipoDocumento(CostantiFin.D_DOC_TIPO_CARTA_CONTABILE_CODE, CostantiFin.D_DOC_TIPO_CARTA_CONTABILE_FAMIGLIA_SPESA, datiOperazione);

					// codice bollo documento
					CodiceBollo codiceBolloDocumento = new CodiceBollo(); 
					codiceBolloDocumento = commonDad.getCodiceBollo(CostantiFin.D_CODICE_BOLLO_ESENTE_DA_BOLLO_CODE, datiOperazione);

					// setto i valori del documento di spesa che verra' inserito
					DocumentoSpesa documentoSpesaInsert = new DocumentoSpesa();

					documentoSpesaInsert.setEnte(ente);
					documentoSpesaInsert.setAnno(annoBilancio);
					documentoSpesaInsert.setNumero(numeroDocumento);
					documentoSpesaInsert.setDescrizione(descrizioneDocumento);
					documentoSpesaInsert.setDataEmissione(datiOperazione.getTs());
					documentoSpesaInsert.setImporto(subdocumentoSpesaIterato.getImporto());
					documentoSpesaInsert.setSoggetto(preDocumentoCartaIterato.getSoggetto());
					documentoSpesaInsert.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);

					documentoSpesaInsert.setTipoDocumento(tipoDocumento);
					documentoSpesaInsert.setCodiceBollo(codiceBolloDocumento);

					// JIRA-4200 FIN - CARTA CONTABILE: inserimento e aggiornamento data esecuzione (CR 648)
					// impostare la dt scadenza sul documento
					// SIAC-6162: se la data di scadenza e' nello stesso giorno della data di emissione, ma in un timestamp diverso, prendo la data di emissione
					Date dataScadenza = ottieniDataScadenza(cartaContabileDaRegolarizzare, datiOperazione);
					documentoSpesaInsert.setDataScadenza(dataScadenza);
					
					inserisceDocumentoSpesa.setDocumentoSpesa(documentoSpesaInsert);
					
					

					// chiamata per inserimento doc spesa
					InserisceDocumentoSpesaResponse inserisceDocumentoSpesaResponse = documentoSpesaService.inserisceDocumentoSpesa(inserisceDocumentoSpesa);

					if(inserisceDocumentoSpesaResponse.isFallimento()){
						res.setErrori(inserisceDocumentoSpesaResponse.getErrori());
						res.setEsito(Esito.FALLIMENTO);
						res.setCartaContabile(null);
						
						throw new BusinessException(res.getDescrizioneErrori());
					}

					SubdocumentoSpesa subDocumentoSpesaInserito = null;
					SubdocumentoSpesa subDocDaCompletare = null;
					
					DocumentoSpesa documentoSpesaInserito = inserisceDocumentoSpesaResponse.getDocumentoSpesa();
					
					AggiornaQuotaDocumentoSpesa aggiornaQuotaDocumentoSpesa = new AggiornaQuotaDocumentoSpesa();

					/*
					 * A fronte del documento di spesa inserito e' contestualmente inserita anche una quota (SubDocumentoSpesa Mod FIN)
					 * 
					 * La quota contestualmente inserita' dovra' essere successivamente aggiornata passando le seguenti informazioni
					 * 
					 * descrizione           --> PreDocumentoCarta.descrizione
					 * importo               --> importo del sub-documento ricevuto in input 
					 * causaleOrdinativo     --> valorizzare con il campo CartaContabile.causale
					 *                           Se CartaContabile.causale e' nullo crearla concatenando  'DOCUMENTO N.'+numero documento +'DEL'+data emissione documento
					 * commissioni documento --> BENEFICIARIO-BN
					 * impegno               --> impegno del sub-documento ricevuto in input
					 * sub-impegno           --> se presente, sub-impegno del sub-documento ricevuto in input
					 * modalita' pagamento    --> modalita' di pagamento di PreDocumentoCarta
					 * sede secondaria       --> sede secondaria di PreDocumentoCarta
					 * 					
					 */

					// recupero il subDocumento incompleto 
					// BIL--SIAC--DEV- 004 - V03 - Linee guida servizi esposti vs FIN
					// par. 2.4.2
					//
					// =============
					//
					// Il servizio di inserisceDocumentoSpesa con inserimento della quota contestuale garantisce che lo stato del documento al termine
					// dell'inserimento sia 'INCOMPLETO'. Per assicurare che il documento sia in stato 'VALIDO', procedere con:
					//
					// - inserimento del documento con quota contestuale
					// - aggiornamento della quota con aggiunta dei dati relativi all'impegno/subimpegno associato.

					if(null!=documentoSpesaInserito.getListaSubdocumenti() && documentoSpesaInserito.getListaSubdocumenti().size()>0){
						
						subDocDaCompletare = documentoSpesaInserito.getListaSubdocumenti().get(0);
						
						subDocDaCompletare.setEnte(ente);
						subDocDaCompletare.setDescrizione(preDocumentoCartaIterato.getDescrizione());
						subDocDaCompletare.setImporto(subdocumentoSpesaIterato.getImporto());

						// commissioni documento
						subDocDaCompletare.setCommissioniDocumento(CommissioniDocumento.BENEFICIARIO);

						String causaleOrdinativo = "";
						if(cartaContabileDaRegolarizzare.getCausale()!=null && StringUtils.isNotEmpty(cartaContabileDaRegolarizzare.getCausale())){
							causaleOrdinativo = cartaContabileDaRegolarizzare.getCausale();
						} else {
							causaleOrdinativo = "Documento num. " + documentoSpesaInserito.getNumero() + " del " + TimingUtils.convertiDataIn_GgMmYyyy(documentoSpesaInserito.getDataEmissione());
						}
						subDocDaCompletare.setCausaleOrdinativo(causaleOrdinativo);
						
						subDocDaCompletare.setImpegno(subdocumentoSpesaIterato.getImpegno());
						
						if(subdocumentoSpesaIterato.getSubImpegno()!=null){
							subDocDaCompletare.setSubImpegno(subdocumentoSpesaIterato.getSubImpegno());
						}
						subDocDaCompletare.setModalitaPagamentoSoggetto(calcolaModalitaPagamentoSoggettoPerSubdoc(preDocumentoCartaIterato.getModalitaPagamentoSoggetto()));
						subDocDaCompletare.setSedeSecondariaSoggetto(preDocumentoCartaIterato.getSedeSecondariaSoggetto());
						
						
						// JIRA-4200 FIN - CARTA CONTABILE: inserimento e aggiornamento data esecuzione (CR 648)
						// impostare la dt di esecuzione pagamento sulla quota del doc
						// 		dataEsecuzionePagamento = rigaCarta.dataEsecuzione
						//		SE RigaCarta.dataEsecuzione è nullo: uso Carta.dataPagamento 
						//		SE Carta.dataPagamento è nullo uso la data di sistema
						Date dataEsecuzionePagamento = new Date(System.currentTimeMillis());
						
						if(preDocumentoCartaIterato.getDataEsecuzioneRiga()!=null){
							
							dataEsecuzionePagamento = preDocumentoCartaIterato.getDataEsecuzioneRiga();
							
						}else if (cartaContabileDaRegolarizzare.getDataEsecuzionePagamento()!=null){
							
							dataEsecuzionePagamento = cartaContabileDaRegolarizzare.getDataEsecuzionePagamento();
						}
						
						subDocDaCompletare.setDataEsecuzionePagamento(dataEsecuzionePagamento);
						
						aggiornaQuotaDocumentoSpesa.setSubdocumentoSpesa(subDocDaCompletare);
						aggiornaQuotaDocumentoSpesa.setRichiedente(richiedente);
						aggiornaQuotaDocumentoSpesa.setAggiornaStatoDocumento(true);
						aggiornaQuotaDocumentoSpesa.setBilancio(req.getBilancio());
						
						// SIAC-: aggiungo la carta contabile per ripristinare correttamente la disponibilita' dell'impegno
						CartaContabile cartaContabile = new CartaContabile();
						cartaContabile.setUid(cartaContabileDaRegolarizzare.getUid());
						aggiornaQuotaDocumentoSpesa.setCartaContabile(cartaContabile);
						
						AggiornaQuotaDocumentoSpesaResponse aggiornaQuotaDocumentoSpesaResponse = documentoSpesaService.aggiornaQuotaDocumentoSpesa(aggiornaQuotaDocumentoSpesa);
						
						if(aggiornaQuotaDocumentoSpesaResponse.isFallimento()){
							res.setErrori(aggiornaQuotaDocumentoSpesaResponse.getErrori());
							res.setEsito(Esito.FALLIMENTO);
							res.setCartaContabile(null);
							
							throw new BusinessException(res.getDescrizioneErrori());
						}
						
						subDocumentoSpesaInserito = aggiornaQuotaDocumentoSpesaResponse.getSubdocumentoSpesa();
						
					}
					
					// Aggiornamento di PreDocumentoCarta 
					// utente aggiornamento   -->  PreDocumentoCarta.utente passato input
					// Ed e' inserita la relazione con il SubDocumentoSpesa inserito
					// aggiorno la riga su siac_t_cartacont_det 
					// ed inserisco la relazione con il sub-documento di spesa inserito
					
					boolean regolarizzaPredocumento = cartaContabileDad.regolarizzaPreDocumentoCarta(cartaContabileDaRegolarizzare,
							                                                                         preDocumentoCartaIterato,
							                                                                         documentoSpesaInserito,
							                                                                         subDocumentoSpesaInserito,
							                                                                         datiOperazione);

					if(!regolarizzaPredocumento){
						// uscita con KO
						res.setCartaContabile(null);
						throw new BusinessException(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("REGOLARIZZAZIONE PRE DOCUMENTO CARTA"));
					}

					// incremento il contatore
					progressivoLegamiRigaSubDocumenti = progressivoLegamiRigaSubDocumenti + 1;
				}
			}
			
			// Prima di restituirla, rileggo dal db i dati della carta appena regolarizzata
			RicercaCartaContabileK k = new RicercaCartaContabileK();
			
			k.setBilancio(req.getBilancio());
			k.setCartaContabile(cartaContabileDaRegolarizzare);
			
			// ricarco la carta
			CartaContabile cartaContRestituita = cartaContabileDad.ricercaCartaContabile(k, richiedente, CostantiFin.AMBITO_FIN,ente, datiOperazione, true);
			
			if(cartaContRestituita.getListaPreDocumentiCarta()!=null && cartaContRestituita.getListaPreDocumentiCarta().size()>0){
				for(PreDocumentoCarta preDocumentoCarta : cartaContRestituita.getListaPreDocumentiCarta()){
					// Estrazione degli eventuali sub-documenti legati al PreDocumentoCarta : inizio
					// attraverso la chiamata al servizio DocumentoSpesaService.ricercaDettaglioQuotaSpesa()
					// estrazione degli eventuali sub-documenti legati al PreDocumentoCarta : inizio
					if(preDocumentoCarta.getListaIdSubDocumentiCollegati()!=null && preDocumentoCarta.getListaIdSubDocumentiCollegati().size()>0){
						List<SubdocumentoSpesa> listaSubDocumentiSpesaCollegati = new ArrayList<SubdocumentoSpesa>();
						for(Integer idSubDocumentoSpesa : preDocumentoCarta.getListaIdSubDocumentiCollegati()){
							RicercaDettaglioQuotaSpesa ricercaDettaglioQuotaSpesa = new RicercaDettaglioQuotaSpesa();
							ricercaDettaglioQuotaSpesa.setRichiedente(richiedente);
							
							SubdocumentoSpesa subdocumentoSpesaInput = new SubdocumentoSpesa();
							subdocumentoSpesaInput.setUid(idSubDocumentoSpesa);
							ricercaDettaglioQuotaSpesa.setSubdocumentoSpesa(subdocumentoSpesaInput);
							
							RicercaDettaglioQuotaSpesaResponse ricercaDettaglioQuotaSpesaResponse = documentoSpesaService.ricercaDettaglioQuotaSpesa(ricercaDettaglioQuotaSpesa);
							if(ricercaDettaglioQuotaSpesaResponse.isFallimento()){
								res.setErrori(ricercaDettaglioQuotaSpesaResponse.getErrori());
								res.setEsito(Esito.FALLIMENTO);
								res.setCartaContabile(null);
								
								throw new BusinessException(res.getDescrizioneErrori());
							}
							
							listaSubDocumentiSpesaCollegati.add(ricercaDettaglioQuotaSpesaResponse.getSubdocumentoSpesa());
						}
						
						if(listaSubDocumentiSpesaCollegati!=null && listaSubDocumentiSpesaCollegati.size()>0){
							for(SubdocumentoSpesa subdocumentoSpesaIterato : listaSubDocumentiSpesaCollegati){
								if(subdocumentoSpesaIterato.getImpegno()!=null){
									
									//APRILE 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
									PaginazioneSubMovimentiDto pag = new PaginazioneSubMovimentiDto();
									pag.setNoSub(true);
									//
									
									// Estraggo i dati dell'impegno del SubDocumentoSpesa
									Impegno imp = (Impegno)impegnoOttimizzatoDad.ricercaMovimentoPkByUid(subdocumentoSpesaIterato.getImpegno().getUid(),
											                                                             richiedente,
											                                                             ente,
											                                                             CostantiFin.MOVGEST_TIPO_IMPEGNO,
											                                                             false,pag,null);

									if(imp!=null){
										// Estraggo il capitolo uscita gestione dell'impegno legato al SubDocumentoSpesa
										// attraverso la chiamata al servizio CapitoloUscitaGestione.ricercaDettaglioCapitoloUscitaGestione()

										CapitoloUscitaGestione capitoloUscitaGestioneCiclo = caricaCapitoloUscitaGestione(richiedente, imp.getChiaveCapitoloUscitaGestione(), true);
										if(capitoloUscitaGestioneCiclo!=null){
											imp.setCapitoloUscitaGestione(capitoloUscitaGestioneCiclo);
										}
										subdocumentoSpesaIterato.setImpegno(imp);			
									}
								}
							}
							
							preDocumentoCarta.setListaSubDocumentiSpesaCollegati(listaSubDocumentiSpesaCollegati);										
						}
					}
					// estrazione degli eventuali sub-documenti legati al PreDocumentoCarta : fine		
			
				}
			}	

			res.setErrori(null);
			res.setEsito(Esito.SUCCESSO);
			res.setCartaContabile(cartaContRestituita);
		} else {
			res.setErrori(listaErrori);
			res.setEsito(Esito.FALLIMENTO);
			res.setCartaContabile(null);
		}
	}


	private Date ottieniDataScadenza(CartaContabile cartaContabileDaRegolarizzare, DatiOperazioneDto datiOperazione) {
		Date dataScadenza = cartaContabileDaRegolarizzare.getDataScadenza();
		Calendar calDataScadenza = Calendar.getInstance();
		calDataScadenza.setTime(dataScadenza);
		
		Calendar calDataEmissione = Calendar.getInstance();
		calDataEmissione.setTime(datiOperazione.getTs());
		
		if(calDataScadenza.get(Calendar.YEAR) == calDataEmissione.get(Calendar.YEAR)
				&& calDataScadenza.get(Calendar.MONTH) == calDataEmissione.get(Calendar.MONTH)
				&& calDataScadenza.get(Calendar.DAY_OF_MONTH) == calDataEmissione.get(Calendar.DAY_OF_MONTH)
				&& calDataScadenza.compareTo(calDataEmissione) <= 0) {
			// Caso in cui ho fatto la regolarizzazione nel giorno della scadenza e i timestamp non coincidono. Riprendo il timestamp dell'operazione
			dataScadenza = datiOperazione.getTs();
		}
		
		return dataScadenza;
	}
	
	private ModalitaPagamentoSoggetto calcolaModalitaPagamentoSoggettoPerSubdoc(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		if(modalitaPagamentoSoggetto == null) {
			// Null-safe
			return null;
		}
		if(modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2() != null) {
			// Se sono in cessione, utilizzo la MPS della cessione
			return modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2();
		}
		// Utilizzo la MPS originale
		return modalitaPagamentoSoggetto;
	}


	@Override
	protected void checkServiceParam() throws ServiceParamError {

		final String methodName="RegolarizzaCartaContabileService : checkServiceParam()";
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

		CartaContabile cartaContabileDaRegolarizzare = req.getCartaContabileDaRegolarizzare();
		
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

		if(cartaContabileDaRegolarizzare==null){
			listParametriNonInizializzati.add(CARTA_CONTABILE_DA_REGOLARIZZARE);
		} else {
			// Verifico il bilancio della carta contabile
			if(cartaContabileDaRegolarizzare.getBilancio()==null){
				listParametriNonInizializzati.add(BILANCIO_CARTA_CONTABILE);
			} else if(cartaContabileDaRegolarizzare.getBilancio().getAnno()==0){
				listParametriNonInizializzati.add(ANNO_BILANCIO_CARTA_CONTABILE);
			}
			
			// verifico obbligatorieta' lista PreDocumentiCarta da Regolarizzare
			List<PreDocumentoCarta> listaPreDocumentiCartaDaRegolarizzare = cartaContabileDaRegolarizzare.getListaPreDocumentiCarta();

			if(listaPreDocumentiCartaDaRegolarizzare==null){
				listParametriNonInizializzati.add(LISTA_PRE_DOCUMENTI_CARTA_DA_REGOLARIZZARE);
			} else if(listaPreDocumentiCartaDaRegolarizzare.isEmpty()){
				listParametriNonInizializzati.add(LISTA_PRE_DOCUMENTI_CARTA_DA_REGOLARIZZARE);
			} else {
				for(PreDocumentoCarta preDocumentoCarta : listaPreDocumentiCartaDaRegolarizzare){
					if(preDocumentoCarta.getNumero()==null){
						listParametriNonInizializzati.add(NUMERO + " riga");
						break;
					}
					if(preDocumentoCarta.getImportoDaRegolarizzare()==null){
						listParametriNonInizializzati.add(IMPORTO_DA_REGOLARIZZARE + " riga " + preDocumentoCarta.getNumero());
						break;
					}
					
					List<SubdocumentoSpesa> listaSubDocumentiSpesa = preDocumentoCarta.getListaSubDocumentiSpesaCollegati();
					
					// verifico obbligatorieta' lista SubDocumentiSpesa (regolarizzazioni)
					if(listaSubDocumentiSpesa==null){
						listParametriNonInizializzati.add(LISTA_REGOLARIZZAZIONI + " riga " + preDocumentoCarta.getNumero());
						break;
					} else if(listaPreDocumentiCartaDaRegolarizzare.isEmpty()){
						listParametriNonInizializzati.add(LISTA_REGOLARIZZAZIONI + " riga " + preDocumentoCarta.getNumero());
						break;
					} else {
						for(SubdocumentoSpesa subdocumentoSpesa : listaSubDocumentiSpesa){
							if(subdocumentoSpesa.getImpegno()==null){
								listParametriNonInizializzati.add(IMPEGNO + " riga " + preDocumentoCarta.getNumero());
								break;
							}
							
							if(subdocumentoSpesa.getImporto()==null){
								listParametriNonInizializzati.add(IMPORTO + " riga " + preDocumentoCarta.getNumero());
								break;
							}
						}
					}
				}
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