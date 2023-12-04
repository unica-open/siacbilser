/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.ordinativi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.OnereSpesaDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ExecuteExternalServiceException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiPagamentoSingolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiPagamentoSingoloResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RitenuteDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamentiResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.TipoAssociazioneEmissione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.RegolarizzazioneProvvisorio;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;


/**
 * Consente di inserire una serie di ordinativi a fronte di un elenco di liquidazioni individuato in base ai parametri di input.
 * L'elaborazione deve poter essere lanciata da applicativo o schedulata. 
 * Il volume dei dati elaborati pu&ograve; raggiungere l'ordine della decina di migliaia.
 * <br/>
 * Analisi di riferimento: 
 * BIL--SIAC-FIN-SER-017-V01 - COMS003 Servizio Gestione Emissione Ordinativi.docx 
 * &sect;2.4
 * 
 * @author Domenico
 * @author Marchino Alessandro
 * @author Valentina
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmetteOrdinativoDiPagamentoSingoloService extends EmetteOrdinativiDaElencoBaseService<EmetteOrdinativoDiPagamentoSingolo, EmetteOrdinativoDiPagamentoSingoloResponse> {

	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	@Autowired
	private CapitoloEntrataGestioneDad capitoloDad;
	
	@Autowired
	private OnereSpesaDad onereSpesaDad;
	
	private SubdocumentoSpesa subdoc;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getSubdoc(), "quota spesa");
		subdoc = req.getSubdoc();
		checkEntita(req.getBilancio(), "bilancio");
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
		
		documentiSpesaCache = req.getDocumentiCache();
		liquidazioniCache = req.getLiquidazioniCache();
		soggettiCache = req.getSoggettiCache();
		bilancio = req.getBilancio();
		//SIAC-5937
		bilancioAnnoSuccessivo = req.getBilancioAnnoSuccessivo();
		bilancioInDoppiaGestione = req.isBilancioInDoppiaGestione();
		note = req.getNote();
		distinta = req.getDistinta();
		contoTesoreria = req.getContoTesoreria();
		commissioniDocumento = req.getCommissioniDocumento();
		codiceBollo = req.getCodiceBollo();
		dataScadenza = req.getDataScadenza();
		flagNoDataScadenza = req.getFlagNoDataScadenza();
		flagDaTrasmettere = req.getFlagDaTrasmettere();
		//SIAC-6206
		classificatoreStipendi = req.getClassificatoreStipendi();

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public EmetteOrdinativoDiPagamentoSingoloResponse executeServiceTxRequiresNew(EmetteOrdinativoDiPagamentoSingolo serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public EmetteOrdinativoDiPagamentoSingoloResponse executeService(EmetteOrdinativoDiPagamentoSingolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		//XXX per test SIAC-6294
		final String methodName ="emetteOrdinativiDiPagamentoDaElencoResponse";
		OrdinativoPagamento ord = emettiOrdinativoSingoloDaQuota(subdoc);
		res.setOrdinativo(ord);
		res.setDocumentiCache(documentiSpesaCache);
		res.setLiquidazioniCache(liquidazioniCache);
		res.setSoggettiCache(soggettiCache);		
	}


	private OrdinativoPagamento emettiOrdinativoSingoloDaQuota(SubdocumentoSpesa subdoc){
		final String methodName = "emettiOrdinativoSingoloDaQuota";
		
		if(!isQuotaDaEmettereSpesaValidaPerEmissione(subdoc)){
			return null;
		}
		
		//ordinativo di pagamento ottenuto dalla quota
		OrdinativoPagamento ordinativo = creaOrdinativoPagamento(subdoc);
		if(ordinativo == null) {
			log.debug(methodName, "Nessun ordinativo da creare per la quota: "+ subdoc.getUid());
			return null;
		}
		
		//ordinativi di incasso collegati ottentuti dalle ritenute del documento padre
		List<OrdinativoIncasso> ordinativiIncasso = gestioneOrdinativoDaRitenute(ordinativo, subdoc);
		ordinativo.getElencoOrdinativiCollegati().addAll(ordinativiIncasso);
		
		//ordinativo di incasso collegato per le quote split/reverse
		if(subdoc.getImportoSplitReverse() != null && subdoc.getImportoSplitReverse().compareTo(BigDecimal.ZERO) != 0 && subdoc.getTipoIvaSplitReverse() != null){
			log.debug(methodName, "elaborazione ritenute split reverse");
			OrdinativoIncasso ordIncasso = gestisciRitenutaSpiltReverse(ordinativo, subdoc);
			if(ordIncasso != null){
				ordinativo.getElencoOrdinativiCollegati().add(ordIncasso);
			}
		}
		//SIAC-5937: necessario richiamare il metodo PRIMA del servizio di inserimento ordinativo, perche' il subdocumento modifica la disponibilita' a liquidare dell'impegno nell'anno di bilancio successivo.
		gestisciDoppiaGestioneSubDocumentoSpesaSeNecessario(subdoc);
		
		OrdinativoPagamento ordinativoInserito = inserisceOrdinativoPagamento(ordinativo);
		//la response non restituisce tutti i dati passati nella request. Posso eventualmente fare una ricerca?
		ordinativoInserito.setAttoAmministrativo(ordinativo.getAttoAmministrativo());
		ordinativoInserito.setSoggetto(ordinativo.getSoggetto());
		ordinativoInserito.setCodiceBollo(ordinativo.getCodiceBollo());
		ordinativoInserito.setElencoOrdinativiCollegati(ordinativo.getElencoOrdinativiCollegati());
		
		subdoc.setOrdinativo(ordinativoInserito);
		
		gestisciDoppiaGestioneSubDocumentoSpesaSeNecessario(subdoc);
		
		//aggiorno lo stato del documento
		aggiornaStatoOperativoDocumentoSpesa(subdoc.getDocumento());
		documentiSpesaCache.put(subdoc.getDocumento().getUid(),subdoc.getDocumento());
		
		//se rilevante iva aggiorno lo stato operativo del doc iva
		if(Boolean.TRUE.equals(subdoc.getFlagRilevanteIVA())) {
			subdoc.setSubdocumentoIva(aggiornaStatoOperativoDocumentoIvaSpesa(subdoc));
		}
		
		//ricarico la liquidazione in questione, potrebbe essere cambiata diponibilità a liquidare e devo tenerne conto per le quote successive
		if(subdoc.getLiquidazione() != null && subdoc.getLiquidazione().getUid() != 0){
			Liquidazione liq = leggiDisponibilitaALiquidare(subdoc.getLiquidazione());
			liquidazioniCache.put(subdoc.getLiquidazione().getUid(), liq);
			subdoc.setLiquidazione(liq);
		}
		
		
		log.debug(methodName, "Creato ordinativo per quota " + subdoc.getUid() + " con uid: "+ ordinativoInserito.getUid());
		log.debug(methodName, "Numero subOrdinativi: " + (ordinativoInserito.getElencoSubOrdinativiDiPagamento()!=null?ordinativoInserito.getElencoSubOrdinativiDiPagamento().size():"null"));
		
		SubOrdinativoPagamento subOrdinativo = findSubOrdinativoAssociatoAllaQuotaUnica(ordinativoInserito.getElencoSubOrdinativiDiPagamento(), subdoc);
		log.debug(methodName, "subOrdinativo [uid:"+subOrdinativo.getUid()+ " Ordinativo.uid: "
				+ subdoc.getOrdinativo().getUid()+"] legato al subDocumento [uid:"+ subdoc.getUid()+"] ");
		
		return ordinativoInserito;
		
	}
	

	/**
	 * Controlla se si possa emettere un ordinativo a partire dalla quota passata come parametro
	 * 
	 * @return true se la quota si puo' emettere, false altrimenti
	 */
	private boolean isQuotaDaEmettereSpesaValidaPerEmissione(SubdocumentoSpesa subdocumentoSpesa) {
		final String methodName = "controllaQuotaDaEmettere";
		
		    Liquidazione liquidazione = caricaLiquidazione(subdocumentoSpesa);
		    Soggetto soggetto = caricaSoggetto(liquidazione);
		    liquidazione.setSoggettoLiquidazione(soggetto);
		    subdocumentoSpesa.setLiquidazione(liquidazione);
			
			log.debug(methodName, "Caricamento dati per subdocumento " + subdocumentoSpesa.getUid()
					+ ": liquidazione " + liquidazione.getUid() + ", documento " + subdocumentoSpesa.getDocumento().getUid() + ", soggetto " + soggetto.getUid());
			
			try {
				checkOrdinativoEmettibile(subdocumentoSpesa, req.getFlagConvalidaManuale()); 
				return true;
			}catch(BusinessException be){
				log.info(methodName, "subdocumento scartato! uid:"+ subdocumentoSpesa.getUid() + ". Errore: "+ (be.getErrore()!=null? be.getErrore().getTesto():"null"));
				Messaggio messaggio = new Messaggio("QUOTA_SCARTATA", be.getErrore().getTesto());
				res.addMessaggio(messaggio);
				res.setSubdocumentoScartato(subdocumentoSpesa);
				return false; //Il subdocumento viene scartato. Si continua con il prossimo.
			}
		
	}
	
	
	/**
	 * Gestione degli ordinativi di incasso emessi a partire dalle ritenute
	 * @param ordinativo ordinativo di pagamento padre
	 * @param docSpesa quota da emettere
	 * 
	 * @return la lista degli ordinativi di incasso creati
	 */
	private List<OrdinativoIncasso> gestioneOrdinativoDaRitenute(OrdinativoPagamento ordinativo, SubdocumentoSpesa subdoc) {
		final String methodName = "gestioneOrdinativoDaRitenute";
		//SIAC-6048: se la quota e' collegata ad un provvisorio di cassa, non elaboro la ritenuta
		if(Boolean.TRUE.equals(subdoc.getFlagACopertura())) {
			log.debug(methodName, "il subdocumento con uid: " + subdoc.getUid() +" ha un provvisorio di cassa. Return lista vuota.");
			return new ArrayList<OrdinativoIncasso>();
		}
		List<DettaglioOnere> oneriCollegati = onereSpesaDad.findOneryByIdDocumento(subdoc.getDocumento().getUid());
		
		if(oneriCollegati == null || oneriCollegati.isEmpty()) {
			log.debug(methodName, "non ho trovato ritenute collegate per il doc con uid: " + subdoc.getDocumento().getUid() +". Return lista vuota.");
			// Se non ho ritenute esco
			return new ArrayList<OrdinativoIncasso>();
		}
		RitenuteDocumento ritenuteDocumento = subdoc.getDocumento().getRitenuteDocumento() != null ? subdoc.getDocumento().getRitenuteDocumento() : new RitenuteDocumento();
		ritenuteDocumento.setListaOnere(oneriCollegati);
		subdoc.getDocumento().setRitenuteDocumento(ritenuteDocumento);
		
		if(ordinativo.getElencoSubOrdinativiDiPagamento() != null && !ordinativo.getElencoSubOrdinativiDiPagamento().isEmpty()) {
			//la gestione ritenute e' solo per ordinativi singoli --> nella lista ci sara' solo un subordinativo per ogni ordinativo creato
			log.debug(methodName, "trovato subordinativo di pagamento relativo all'ordinativo, elaboro le ritenute ");
			return elaborazioneDatiPerRitenute(subdoc,ordinativo, oneriCollegati);
		}
		log.debug(methodName, "non ho trovato subordinativi di pagamento relativi all'ordinativo, return lista vuota");
		return new ArrayList<OrdinativoIncasso>();
	}
	
	/**
	 * Si presuppone che sia stata individuata una quota di spesa da emettere.
	 * <br/>
	 * Per i passi successivi occorre:
	 * <ul>
	 *     <li>ricavare l'importo Documento da emettere, ovvero la somma degli importi (importiDaPagare) di tutte le quote del documento (emesse e da emettere).
	 *         D'ora poi l'importo verr&agrave; identificato come <em>totale_documento</em></li>
	 * </ul>
	 * <br/>
	 * Partendo dalla lista DettaglioRitenute, per ciascuna occorrenza si ripetono i passi seguenti.
	 * <ol>
	 *     <li>Ricavare il Capitolo Entrata ed eventuale Accertamento collegato al TipoOnere</li>
	 *     <li>Definire l'importo del DettaglioOnere non ancora emesso, ovvero non ancora collegato ad Ordinativi di incasso:
	 *         <br/>
	 *         <pre>importo_onere_da_emettere = DettaglioOnere. importoCaricoSogg - ∑SubOrdinativoIncasso.importoAttuale (collegati a Dettaglio Onere)</pre></li>
	 *     <li>Definire l'importo da emettere ovvero da associare al SubOrdinativoIncasso (e di conseguenza all'OrdinativoIncasso).
	 *         <br/>
	 *         A questo punto si presentano alcuni possibili percorsi da verificare nella sequenza indicata.
	 *         <ul>
	 *             <li>Se la quota di spesa che si sta emettendo (all'interno dell'intero Documento) &eacute; l'ultima si ricava l'importo ancora da emettere dell'onere:
	 *                 <pre>importo da attribuire al SubordinativoIncasso = importo_onere_da_emettere</pre></li>
	 *             <li>Altrimenti si proporziona l'onere sulla quota di documento:
	 *                 <pre>importo da attribuire al SubordinativoIncasso ( troncata alla seconda cifra decimale) = DettaglioOnere.importoCaricoSogg * (SubDocumento.importoDaPagare / totale_documento)</pre></li>
	 *         </ul></li>
	 *     <li>Se non presente l’Accertamento occorre effettuare un inserimento contestuale di un accertamento 'automatico' richiamando
	 *         il metodo Inserisce Accertamento del servizio FINENTS002 Gestione Accertamento.
	 *         I dati di input sono ricavati da quelli disponibili nella transazione in corso, in particolare:
	 *         <ul>
	 *             <li>Capitolo = Capitolo Entrata associato al TipoOnere</li>
	 *             <li>Soggetto = intestatario del Documento a cui &eacute; collegato il DettaglioOnere (che &eacute; anche l'intestatario dell'ordinativo 'padre')</li>
	 *             <li>Atto Amministrativo = quello usato sull’ordinativo 'padre'</li>
	 *             <li>StatoOperativoMovimentoGestione = DEFINITIVO</li>
	 *             <li>automatico = TRUE</li>
	 *             <li>importoIniziale = DettaglioOnere.importoCaricoSogg</li>
	 *         </ul></li>
	 *     <li>Impostare la descrizione Subordinativo = TipoOnere.codice ' – ' TipoOnere.descrizione</li>
	 * </ul>
	 * 
	 * @param subOrdinativoPagamento il subordinativo
	 * @param attoAmministrativo l'atto amministrativo
	 * @param ritenuteCollegate 
	 */
	private List<OrdinativoIncasso> elaborazioneDatiPerRitenute(SubdocumentoSpesa subdocumentoSpesa, OrdinativoPagamento ordinativo, List<DettaglioOnere> ritenuteCollegate) {
		final String methodName = "elaborazioneDatiPerRitenute";
		
		List<OrdinativoIncasso> ordinativiIncasso = new ArrayList<OrdinativoIncasso>();
		
		DocumentoSpesa documentoSpesa = caricaDocumento(subdocumentoSpesa);
		Liquidazione liquidazione = caricaLiquidazione(subdocumentoSpesa);
		subdocumentoSpesa.setLiquidazione(liquidazione);
		subdocumentoSpesa.setDocumento(documentoSpesa);
		
		// Ricavare l’importo Documento da emettere , ovvero la somma degli importi (importiDaPagare) di tutte le quote del documento (emesse e da emettere)
		BigDecimal importoDocumentoDaEmettere = calcolaImportoDaEmettereDocumento(documentoSpesa);
		log.debug(methodName, "Importo da emettere per il documento [uid:" + documentoSpesa.getUid() + "]: " + importoDocumentoDaEmettere);
		
		for(DettaglioOnere dettaglioOnere : ritenuteCollegate) {
			try {
				OrdinativoIncasso ordinativoIncasso = elaborazioneDettaglioOnere(dettaglioOnere, subdocumentoSpesa, importoDocumentoDaEmettere, ordinativo.getAttoAmministrativo(), ordinativo.getSoggetto(), ordinativo);
				if(ordinativoIncasso != null) {
//					log.logXmlTypeObject(ordinativoIncasso, "ordinativo incasso inserito");
					ordinativoIncasso.getElencoSubOrdinativiDiIncasso().get(0).setDettaglioOnere(dettaglioOnere);
					ordinativiIncasso.add(ordinativoIncasso);
				}
			} catch(BusinessException be) {
				log.error(methodName, "BusinessException in elaborazioneDettaglioOnere: " + be.getMessage());
				throw new BusinessException(ErroreFin.INSERIMENTO_CONTESTUALE_ORDINATIVO_IMPOSSIBILE.getErrore("Liquidazione",
						liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione(),
						"(errore riscontrato: " + be.getMessage() + ")"));
			} catch(ExecuteExternalServiceException eese) {
				log.error(methodName, "ExecuteExternalServiceException in elaborazioneDettaglioOnere: " + eese.getMessage());
				throw new BusinessException(ErroreFin.INSERIMENTO_CONTESTUALE_ORDINATIVO_IMPOSSIBILE.getErrore("Liquidazione",
						liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione(),
						"(errori riscontrati: " + eese.getMessage() + ")"));
			}
		}
		return ordinativiIncasso;
	}
	
	private BigDecimal calcolaImportoDaEmettereDocumento(DocumentoSpesa documentoSpesa) {
		return documentoSpesaDad.calcolaTotaleDaPagareQuote(documentoSpesa);
	}

	/**
	 * Elabora il dettaglio dell'onere.
	 * 
	 * @param dettaglioOnere
	 * @param liquidazione
	 * @param subdoc
	 * @param doc
	 * @param importoDocumentoDaEmettere
	 * @param attoAmministrativo
	 * @param soggetto
	 */
	private OrdinativoIncasso elaborazioneDettaglioOnere(DettaglioOnere dettaglioOnere, SubdocumentoSpesa subdoc,
			BigDecimal importoDocumentoDaEmettere, AttoAmministrativo attoAmministrativo, Soggetto soggetto, OrdinativoPagamento ordinativo) {
		final String methodName = "elaborazioneDettaglioOnere";

		// Ricavare il Capitolo Entrata ed eventuale Accertamento collegato al TipoOnere
		TipoOnere tipoOnere = dettaglioOnere.getTipoOnere();
		if(tipoOnere == null) {
			log.debug(methodName, "Tipo onere non presente per dettaglio " + dettaglioOnere.getUid());
			throw new BusinessException("Tipo onere non presente per dettaglio " + dettaglioOnere.getUid());
		}
		if(dettaglioOnere.getImportoCaricoSoggetto() == null || dettaglioOnere.getImportoCaricoSoggetto().compareTo(BigDecimal.ZERO) == 0){
			log.debug(methodName, "salto la gestione della ritenuta, l'importo a carico soggetto e' nullo");
			return null;
		}
		//TODO mettere a posto questo controllo
		if(tipoOnere.getNaturaOnere().isSplitReverse() || "ES".equals(tipoOnere.getNaturaOnere().getCodice())){
			log.debug(methodName, "salto la gestione classica delle ritenute,il tipo e': " + tipoOnere.getNaturaOnere());
			return null;
		}
		
		CapitoloEntrataGestione ceg = findFirstCapitolo(tipoOnere.getCausaliEntrata());
		if(ceg == null) {
			log.warn(methodName, "Capitolo non presente per dettaglio " + dettaglioOnere.getUid());
			throw new BusinessException("Capitolo non presente per dettaglio " + dettaglioOnere.getUid());
		}
		BigDecimal importoOnereDaEmettere = calcolaImportoOnereDaEmettere(dettaglioOnere);
		log.debug(methodName, "Importo ancora da emettere per l'onere " + dettaglioOnere.getUid() + ": " + importoOnereDaEmettere);
		BigDecimal importoDaEmettere = calcolaImportoDaEmettere(dettaglioOnere, subdoc, importoOnereDaEmettere, importoDocumentoDaEmettere);
		log.debug(methodName, "Importo da emettere nell'ordinativo collegato all'onere " + dettaglioOnere.getUid() + ": " + importoDaEmettere);
		
		Accertamento acc = findFirstAccertamento(tipoOnere.getCausaliEntrata());
		if(acc != null && acc.getDisponibilitaIncassare().compareTo(importoDaEmettere)<0){
			//se la disponibilita' e' inferiore all'importo da emettere inserisco una modifica di accertamento.
			ModificaMovimentoGestioneEntrata modifica = creaModifica(acc, importoDaEmettere);
			acc.setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
			acc.getListaModificheMovimentoGestioneEntrata().add(modifica);
		}else if(acc == null){
			//se lo inserisco io non devo verifcare la disponibilita', lo sto creando con l'importo che mi serve
			log.debug(methodName, "Inserimento dell'accertamento automatico");
			acc = popolaAccertamentoAutomatico(ceg, subdoc.getDocumento(), attoAmministrativo, dettaglioOnere, importoDaEmettere);
			
			inserisciAccertamento(acc);
			
			log.debug(methodName, "Il valore di siope del capitolo associato all'accertamento automatico inserito risulta essere: " + (acc!= null && acc.getCapitoloEntrataGestione()!= null && acc.getCapitoloEntrataGestione().getSiopeEntrata()!=null? acc.getCapitoloEntrataGestione().getSiopeEntrata().getCodice() : "null"));
		}
		TipoAssociazioneEmissione tipoAssociazione = TipoAssociazioneEmissione.RIT_ORD; 
		
		return emissioneOrdinativoDiIncassoRitenute(dettaglioOnere, ceg, acc, importoDaEmettere, soggetto, attoAmministrativo, tipoOnere, subdoc.getLiquidazione(), ordinativo, tipoAssociazione, "RISCOSSIONE RITENUTA APPLICATA");
	}
	
	/**
	 * Trova la prima distinta collegata alle causali
	 *
	 * @param causaliEntrata the causali entrata
	 * @return the accertamento
	 */
	private Distinta findFirstDistinta(List<CausaleEntrata> causaliEntrata) {
		final String methodName = "findFirstDistinta";
		for(CausaleEntrata causaleEntrata : causaliEntrata) {
			if(causaleEntrata.getDataFineValidita() != null){
				continue;
			}
			Distinta distinta = causaleEntrata.getDistinta();
			if(distinta != null) {
				log.debug(methodName, "Prima distinta trovata: " + distinta.getUid() + " per causale " + causaleEntrata.getUid());
				return distinta;				
			}
		}
		return null;
	}
	
	/**
	 * Trova il primo accertamento collegato alle causali.
	 * 
	 * @param causaliEntrata le causali da cui ottenere l'accertamento
	 * 
	 * @return il primo accertamento collegato
	 */
	private Accertamento findFirstAccertamento(List<CausaleEntrata> causaliEntrata) {
		final String methodName = "findFirstAccertamento";
		for(CausaleEntrata causaleEntrata : causaliEntrata) {
			Accertamento accertamento = causaleEntrata.getAccertamento();
			if(accertamento != null) {
				log.debug(methodName, "Primo accertamento trovato: " + accertamento.getUid() + " per causale " + causaleEntrata.getUid());
				// Ricerca di dettaglio dell'accertamento
				
				if(causaleEntrata.getSubAccertamento() == null){
					accertamento = ricercaAccertamentoPerChiave(accertamento);
					return accertamento;
				}else{
					accertamento = ricercaAccertamentoPerChiave(accertamento, causaleEntrata.getSubAccertamento());
					return cercaSubAccertamento(accertamento, causaleEntrata.getSubAccertamento());
				}
			}
		}
		return null;
	}
	
	/**
	 * Inserimento dell'accertamento automatico.
	 * 
	 * @param capitolo           il capitolo per cui inserire l'accertamento
	 * @param documento          il documento di riferimento
	 * @param attoAmministrativo l'atto amministrativo dell'accertamento
	 * @param dettaglioOnere     il dettaglio dell'onere del documento
	 * 
	 * @return l'accertamento inserito
	 */
	private Accertamento popolaAccertamentoAutomatico(CapitoloEntrataGestione capitolo, DocumentoSpesa documento, AttoAmministrativo attoAmministrativo, DettaglioOnere dettaglioOnere, BigDecimal importo) {
		
		Accertamento accertamento = new Accertamento();
		accertamento.setAnnoMovimento(bilancio.getAnno());
		// Capitolo = Capitolo Entrata associato al TipoOnere
		accertamento.setCapitoloEntrataGestione(capitolo);
		ElementoPianoDeiConti elementoPianoDeiConti = capitolo.getElementoPianoDeiConti();
		if(elementoPianoDeiConti != null){
			accertamento.setCodPdc(elementoPianoDeiConti.getCodice());
			accertamento.setIdPdc(elementoPianoDeiConti.getUid());
			accertamento.setDescPdc(elementoPianoDeiConti.getDescrizione());
		}
		
		// Soggetto = intestatario del Documento a cui e' collegato il DettaglioOnere (che e' anche leintestatario dell'ordinativo 'padre')
		accertamento.setSoggetto(documento.getSoggetto());
		// Atto Amministrativo = quello usato sull'ordinativo 'padre'
		accertamento.setAttoAmministrativo(attoAmministrativo);
		// StatoOperativoMovimentoGestione = DEFINITIVO
		accertamento.setStatoOperativoMovimentoGestioneEntrata("D");
		accertamento.setDescrizioneStatoOperativoMovimentoGestioneEntrata("DEFINITIVO");
		// automatico = TRUE
		accertamento.setAutomatico(true);
		// importoIniziale = DettaglioOnere.importoCaricoSogg
		accertamento.setImportoIniziale(importo);
		accertamento.setImportoAttuale(importo);
		accertamento.setLoginOperazione(loginOperazione);
		
		return accertamento;
	}
	
	/**
	 * Trova il primo capitolo collegato alle causali. NOTA: non viene VOLUTAMENTE filtrato per anno di bilancio.
	 * 
	 * @param causaliEntrata le causali da cui ottenere il capitolo
	 * 
	 * @return il primo capitolo collegato
	 */
	private CapitoloEntrataGestione findFirstCapitolo(List<CausaleEntrata> causaliEntrata) {
		final String methodName = "findFirstCapitolo";
//		Teoricamente risulta possibile che alla causale sia associato un capitolo con un anno di bilancio
//		diverso da quello in cui si sta cercando di emettere. In questo caso l'emissione si rompe. 
//		Si tratta per&ograve; di un problema di configurazione. Si è deciso di non inserire un filtro per capitolo con gli analisti (26/01/2018).
		for(CausaleEntrata causaleEntrata : causaliEntrata) {
			CapitoloEntrataGestione capitoloEntrataGestione = causaleEntrata.getCapitoloEntrataGestione();
			if(capitoloEntrataGestione != null) {
				log.debug(methodName, "Primo capitolo trovato: " + capitoloEntrataGestione.getUid() + " per causale " + causaleEntrata.getUid());
				// Ho necessita' degli importi del capitolo. Controllo che ci siano: in caso contrario li ricerco
				if(capitoloEntrataGestione.getListaImportiCapitolo() == null || capitoloEntrataGestione.getListaImportiCapitolo().isEmpty()) {
					// Inizializzazione degli importi
					inizializzaImportiCapitolo(capitoloEntrataGestione);
				}
				caricaPianoDeiConti(capitoloEntrataGestione);
				return capitoloEntrataGestione;
			}
		}
		return null;
	}
	
	private void caricaPianoDeiConti(CapitoloEntrataGestione capitoloEntrataGestione) {
		ElementoPianoDeiConti pdc = capitoloDad.findPianoDeiContiCapitolo(capitoloEntrataGestione.getUid());
		capitoloEntrataGestione.setElementoPianoDeiConti(pdc);
	}

	/**
	 * Calcolo degli importi capitolo per il capitolo fornito
	 * @param capitoloEntrataGestione
	 * @return
	 */
	private void inizializzaImportiCapitolo(CapitoloEntrataGestione capitoloEntrataGestione) {
		List<ImportiCapitoloEG> importiCapitolo = new ArrayList<ImportiCapitoloEG>();
		
		// AnnoEsercizio (+0, +1, +2)
		// TODO: dovrebbe bastare soltanto (annoEsercizio + 0). La classe di servizio utilizza solo tale importo, ma per sicurezza ad oggi sono calcolati anche gli altri
		// SIAC-6043: gestione del metodo ad hoc per non duplicare i controlli sul null
		inizializzaImportoCapitolo(capitoloEntrataGestione, importiCapitolo, bilancio.getAnno() + 0);
		inizializzaImportoCapitolo(capitoloEntrataGestione, importiCapitolo, bilancio.getAnno() + 1);
		inizializzaImportoCapitolo(capitoloEntrataGestione, importiCapitolo, bilancio.getAnno() + 2);
		
		capitoloEntrataGestione.setListaImportiCapitolo(importiCapitolo);
	}
	
	/**
	 * Inizializzazione degli importi del capitolo per l'anno
	 * @param capitoloEntrataGestione il capitolo
	 * @param importiCapitolo la lista degli importi
	 * @param anno l'anno
	 */
	private void inizializzaImportoCapitolo(CapitoloEntrataGestione capitoloEntrataGestione, List<ImportiCapitoloEG> importiCapitolo, int anno) {
		ImportiCapitoloEG importo = importiCapitoloDad.findImportiCapitolo(capitoloEntrataGestione, anno, ImportiCapitoloEG.class, null);
		if(importo != null) {
			importiCapitolo.add(importo);
		}
	}
	
	/**
	 * Calcola l'importo dell'onere da emettere.
	 * 
	 * @param dettaglioOnere il dettaglio dell'onere
	 * 
	 * @return l'importo
	 */
	private BigDecimal calcolaImportoOnereDaEmettere(DettaglioOnere dettaglioOnere) {
		// importo_onere_da_emettere = DettaglioOnere.importoCaricoSogg - SUM SubOrdinativoIncasso.importoAttuale

		BigDecimal result = dettaglioOnere.getImportoCaricoSoggetto();
		for(SubOrdinativoIncasso soi : dettaglioOnere.getSubordinativiIncasso()) {
			BigDecimal importoAttualeNotNull = soi.getImportoAttuale() != null ? soi.getImportoAttuale() : BigDecimal.ZERO;
			result = result.subtract(importoAttualeNotNull);
		}
		return result;
	}
	
	/**
	 * Calcola l'importo da emettere.
	 * 
	 * @param dettaglioOnere il dettaglio dell'onere
	 * 
	 * @return l'importo
	 */
	private BigDecimal calcolaImportoDaEmettere(DettaglioOnere dettaglioOnere, SubdocumentoSpesa subdoc, BigDecimal importoOnereDaEmettere, BigDecimal importoDocumentoDaEmettere) {
		// TODO: come si calcola?
		//se è l'unica quota di quel documento che ancora non ha ordinativo?
		String methodName = "calcolaImportoDaEmettere";
		Long quoteNonEmesse = subdocumentoSpesaDad.countQuoteDocumentoNonEmesse(subdoc.getDocumento());
		log.debug(methodName, "quote non emesse: " + quoteNonEmesse);
		boolean isUltimaQuota =  quoteNonEmesse == 1 ;
		log.debug(methodName, "isUltimaQuota: " + isUltimaQuota);
		BigDecimal result = BigDecimal.ZERO;
		if(isUltimaQuota) {
			// importo da attribuire al SubordinativoIncasso = importo_onere_da_emettere
			result = importoOnereDaEmettere;
			log.debug(methodName, "result (ultima quota): " + result);
		} else {
			// importo da attribuire al SubordinativoIncasso (troncata alla seconda cifra decimale)= DettaglioOnere.importoCaricoSogg * (SubDocumento.importoDaPagare / totale_documento)
			BigDecimal fact1 = dettaglioOnere.getImportoCaricoSoggetto();
			log.debug(methodName, "fact1: " + fact1);
			BigDecimal div1 = subdoc.getImportoDaPagare();
			log.debug(methodName, "div1: " + div1);
			BigDecimal div2 = importoDocumentoDaEmettere;
			log.debug(methodName, "div2: " + div2);
			BigDecimal fact2 = div1.divide(div2, MathContext.DECIMAL128);
			log.debug(methodName, "fact2: " + fact2);
			result = fact1.multiply(fact2).setScale(2, RoundingMode.HALF_DOWN);
			log.debug(methodName, "result (quota NON ultima): " + result);
		}
		return result;
	}
	
	/**
	 * Emissione dell'ordinativo di incasso per le ritenute.
	 * 
	 * @param ceg
	 * @param acc
	 * @param importoDaEmettere
	 * @param soggetto
	 * @param attoAmministrativo
	 * @param tipoOnere
	 */
	private OrdinativoIncasso emissioneOrdinativoDiIncassoRitenute(DettaglioOnere dettaglioOnere, CapitoloEntrataGestione ceg, Accertamento acc, BigDecimal importoDaEmettere, Soggetto soggetto, AttoAmministrativo attoAmministrativo, 
											TipoOnere tipoOnere, Liquidazione liquidazione, OrdinativoPagamento ordinativo, TipoAssociazioneEmissione tipoAssociazione, String descrizione) {
		OrdinativoIncasso oi = new OrdinativoIncasso();
		// descrizione: 'RISCOSSIONE RITENUTA APPLICATA'
		oi.setDescrizione(descrizione);
		// Capitolo: Capitolo Entrata ricavato dal tipo onere
		oi.setCapitoloEntrataGestione(ceg);
		// ClassificazioneFinanziaria di Gestione: Accertamento
		impostaClassificazioneFinanziariaDiGestione(oi, acc);
		// Soggetto: Ordinativo di pagamento collegato
		oi.setSoggetto(soggetto);
		// Atto Amministrativo: Ordinativo di pagamento collegato
		oi.setAttoAmministrativo(attoAmministrativo);
		
		oi.setNoteTesoriere(ordinativo.getNoteTesoriere());
		oi.setDataEmissione(ordinativo.getDataEmissione());
		oi.setLoginModifica(ordinativo.getLoginModifica());
		oi.setCodiceBollo(ordinativo.getCodiceBollo());
		oi.setElencoRegolarizzazioneProvvisori(new ArrayList<RegolarizzazioneProvvisorio>());
		oi.setStatoOperativoOrdinativo(StatoOperativoOrdinativo.INSERITO);
		
		
		oi.setForza(false);
		
		SubOrdinativoIncasso soi = new SubOrdinativoIncasso();
		
		soi.setDettaglioOnere(dettaglioOnere);
		soi.setNoteTesoriere(ordinativo.getNoteTesoriere());
		soi.setDataEmissione(ordinativo.getDataEmissione());
		soi.setLoginModifica(ordinativo.getLoginModifica());
		soi.setCodiceBollo(ordinativo.getCodiceBollo());
		soi.setElencoRegolarizzazioneProvvisori(new ArrayList<RegolarizzazioneProvvisorio>());
		soi.setStatoOperativoOrdinativo(StatoOperativoOrdinativo.INSERITO);
		impostaClassificazioneFinanziariaDiGestione(soi, acc);
		
		// Accertamento: Accertamento
		soi.setAccertamento(acc);
		// Subaccertamento: Subaccertamento
		// TODO: Dove?
		// Descrizione: TipoOnere.codice ' – ' TipoOnere.descrizione
		soi.setDescrizione(tipoOnere.getCodice() + " - " + tipoOnere.getDescrizione());
		// importoAttuale: importoDaEmettere
		soi.setImportoAttuale(importoDaEmettere);
		// importoIniziale: importoDaEmettere
		soi.setImportoIniziale(importoDaEmettere);
		oi.setTipoAssociazioneEmissione(tipoAssociazione);
		
		//TODO controllare da dove prendere questi valori!
//		oi.setCodPdc(liquidazione.getCodPdc());
//		oi.setCodContoEconomico(liquidazione.getCodContoEconomico());
//		oi.setCodiceBollo(ordinativo.getCodiceBollo());
		
		if(liquidazione.getSedeSecondariaSoggetto() != null ){
			SedeSecondariaSoggetto sede = new SedeSecondariaSoggetto();
			sede.setUid(liquidazione.getSedeSecondariaSoggetto());
			List<SedeSecondariaSoggetto> sediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
			sediSecondarie.add(sede);
			ordinativo.getSoggetto().setSediSecondarie(sediSecondarie);
		}
		
		
		List<SubOrdinativoIncasso> listSubOrdinativiIncasso = new ArrayList<SubOrdinativoIncasso>();
		listSubOrdinativiIncasso.add(soi);
		oi.setElencoSubOrdinativiDiIncasso(listSubOrdinativiIncasso);
//		oi.setSubOrdinativo(soi);
		
		if(isGestioneLivelloRevOneriContoMan()){
			ContoTesoreria conto = contoTesoreria != null ? contoTesoreriaFinFromContoTesoreriaBil(contoTesoreria) : liquidazione.getContoTesoreria();
			oi.setContoTesoreria(conto);
		}
		
		// TODO: controllare i campi, sono corretti?
		if(ceg.getImportiCapitolo() != null){
			oi.setCastellettoCompentenza(ceg.getImportiCapitolo().getStanziamento());
			oi.setCastellettoCassa(ceg.getImportiCapitolo().getStanziamentoCassa());
			
			soi.setCastellettoCompentenza(ceg.getImportiCapitolo().getStanziamento());
			soi.setCastellettoCassa(ceg.getImportiCapitolo().getStanziamentoCassa());
		}else{
			oi.setCastellettoCompentenza(BigDecimal.ZERO);
			oi.setCastellettoCassa(BigDecimal.ZERO);
			
			soi.setCastellettoCompentenza(BigDecimal.ZERO);
			soi.setCastellettoCassa(BigDecimal.ZERO);
		}
		oi.setCastellettoEmessi(importoDaEmettere);
		soi.setCastellettoEmessi(importoDaEmettere);
		
			
		Distinta distintaCaricata = caricaDistinta(liquidazione, tipoOnere);
		oi.setDistinta(distintaCaricata);
		
		oi.setStatoOperativoOrdinativo(StatoOperativoOrdinativo.INSERITO);
		
		
		// SIAC-6361
		oi.setFlagDaTrasmettere(ordinativo.getFlagDaTrasmettere());
		oi.setClassificatoreStipendi(ordinativo.getClassificatoreStipendi());

		OrdinativoIncasso ordInserito = inserisceOrdinativoIncasso(oi);
		ordInserito.setTipoAssociazioneEmissione(tipoAssociazione);
		return ordInserito;
	}


	/**
	 * Carica distinta. 
	 * SE l’onere/ritenuta che si sta incassando ha collegata una distinta, l’ordinativo di entrata dovrà utilizzarla. 
	 * ALTRIMENTI se REV_ONERI_DISTINTA_MAN = true ricercare la distinta di entrata con il medesimo codice della distinta spese, 
	 * se non si trova passare il parametro vuoto per demandare al servizio di inserimento ordinativo di incasso la prima distinta Altrimenti Null
	 *
	 * @param liquidazione the liquidazione
	 * @param tipoOnere the tipo onere
	 * @return the distinta
	 */
	private Distinta caricaDistinta(Liquidazione liquidazione, TipoOnere tipoOnere) {
		final String methodName ="caricaDistinta";
		Distinta distintaOrdinativo = findFirstDistinta(tipoOnere.getCausaliEntrata());
		if(distintaOrdinativo != null || !isGestioneLivelloRevOneriDistintaMan()){
			return distintaOrdinativo;
		}
		//non ho trovato una distinta collegata all'onere e devo gestore manualmente la distinta
		log.debug(methodName, "Forzo la Distinta della reversale a quella del mandato.");
		distintaOrdinativo = distinta != null ? distinta : liquidazione.getDistinta();
		return distintaOrdinativo != null? caricaDistintaEntrata(distintaOrdinativo) : null; 
	}
	
	
	private Distinta caricaDistintaEntrata(Distinta d) {
		return distintaBilDad.findDistintaEntrataByCodice(d.getCodice());
	}

	protected boolean isGestioneLivelloRevOneriContoMan() {
		String methodName = "isGestioneLivelloRevOneriContoMan";
		String gestioneLivello = super.getGestioneLivello(TipologiaGestioneLivelli.REV_ONERI_CONTO_MAN);
		log.debug(methodName, TipologiaGestioneLivelli.REV_ONERI_CONTO_MAN + " gestioneLivello: "+gestioneLivello);
		return "TRUE".equals(gestioneLivello);
		
	}
	
	protected boolean isGestioneLivelloRevOneriDistintaMan() {
		String methodName = "isGestioneLivelloRevOneriDistintaMan";
		String gestioneLivello = super.getGestioneLivello(TipologiaGestioneLivelli.REV_ONERI_DISTINTA_MAN);
		log.debug(methodName, TipologiaGestioneLivelli.REV_ONERI_DISTINTA_MAN + " gestioneLivello: "+gestioneLivello);
		return "TRUE".equals(gestioneLivello);
	}
	
	
	
	/**
	 * Gestione degli ordinativi di incasso emessi a partire da ritenute di tipo Slit/Reverse
	 * @param ordinativo l'ordinativo padre
	 * @param subdocSpesa la quota da emettere
	 * @return l'ordinativo di incasso creato
	 */
	private OrdinativoIncasso gestisciRitenutaSpiltReverse(OrdinativoPagamento ordinativo, SubdocumentoSpesa subdocSpesa) {
		String methodName = "gestisciRitenutaSpiltReverse";
		//SIAC-6048: se la quota e' collegata ad un provvisorio di cassa, non elaboro la ritenuta
		if(Boolean.TRUE.equals(subdoc.getFlagACopertura())) {
			log.debug(methodName, "il subdocumento con uid: " + subdoc.getUid() +" ha un provvisorio di cassa. Non elaboro la ritenuta");
			return null;
		}
		DettaglioOnere dettaglioOnere = findDettaglioOnereCompatibile(subdocSpesa);
		if(dettaglioOnere == null){
			log.debug(methodName, "salto la gestione della ritenuta, non ho trovato un dettaglio onere con TipoIvaSplitReverse corrispondente a quello della quota e importo a carico del soggeto maggiore di zero.");
			return null;
		}
		if(ordinativo.getElencoSubOrdinativiDiPagamento() != null && !ordinativo.getElencoSubOrdinativiDiPagamento().isEmpty()) {
			//la gestione ritenute e' solo per ordinativi singoli --> nella lista ci sara' solo un subordinativo per ogni ordinativo creato
			SubOrdinativoPagamento sop = ordinativo.getElencoSubOrdinativiDiPagamento().get(0);
			return elaborazioneDettaglioOnereSplitReverse(subdocSpesa, sop, ordinativo, dettaglioOnere);
		}
		log.debug(methodName, "non ho trovato subordinativi di pagamento relativi all'ordinativo con uid: " + ordinativo.getUid());
		return null;
	}
	
	
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*--------------------------------------- GESTIONE RITENUTE SPLIT REVERSE------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	private OrdinativoIncasso elaborazioneDettaglioOnereSplitReverse(SubdocumentoSpesa subdocumentoSpesa, SubOrdinativoPagamento subOrdinativoPagamento, OrdinativoPagamento ordinativo, DettaglioOnere dettaglioOnere) {
		final String methodName = "elaborazioneDettaglioOnereSplitReverse";
		
		Liquidazione liquidazione = caricaLiquidazione(subdocumentoSpesa);
		DocumentoSpesa documentoSpesa = caricaDocumento(subdocumentoSpesa);
		
		TipoOnere tipoOnere = dettaglioOnere.getTipoOnere();
		if(tipoOnere == null) {
			log.debug(methodName, "Tipo onere non presente per dettaglio " + dettaglioOnere.getUid());
			throw new BusinessException("Tipo onere non presente per dettaglio " + dettaglioOnere.getUid());
		}
		
		// Ricavare il Capitolo Entrata ed eventuale Accertamento collegato al TipoOnere
		CapitoloEntrataGestione ceg = findFirstCapitolo(tipoOnere.getCausaliEntrata());
		if(ceg == null) {
			log.warn(methodName, "Capitolo non presente per dettaglio " + dettaglioOnere.getUid());
			throw new BusinessException("Capitolo non presente per dettaglio " + dettaglioOnere.getUid());
		}
		BigDecimal importoDaEmettere = subdocumentoSpesa.getImportoSplitReverse();
		
		Accertamento acc = findFirstAccertamento(tipoOnere.getCausaliEntrata());
		if(acc != null && acc.getDisponibilitaIncassare().compareTo(importoDaEmettere)<0) {
			//se la disponibilita' e' inferiore all'importo da emettere inserisco una modifica di accertamento.
			ModificaMovimentoGestioneEntrata modifica = creaModifica(acc, importoDaEmettere);
			acc.setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
			acc.getListaModificheMovimentoGestioneEntrata().add(modifica);
			
		}else if(acc == null){
			//se lo inserisco io non devo verifcare la disponibilita', lo sto creando con l'importo che mi serve
			log.debug(methodName, "Inserimento dell'accertamento automatico");
			acc = popolaAccertamentoAutomatico(ceg, documentoSpesa, ordinativo.getAttoAmministrativo(), dettaglioOnere, importoDaEmettere);
			inserisciAccertamento(acc);
		}
		TipoAssociazioneEmissione tipoAssociazione = TipoAssociazioneEmissione.SPR;
		return emissioneOrdinativoDiIncassoRitenute(dettaglioOnere, ceg, acc, importoDaEmettere, ordinativo.getSoggetto(), ordinativo.getAttoAmministrativo(), tipoOnere, liquidazione, ordinativo, tipoAssociazione, "RISCOSSIONE PER SPLIT/REVERSE");
	}
	
	private ModificaMovimentoGestioneEntrata creaModifica(Accertamento acc, BigDecimal importoDaEmettere) {
		BigDecimal disponibilitaIncassare = acc.getDisponibilitaIncassare() != null && acc.getDisponibilitaIncassare().signum()>= 0? acc.getDisponibilitaIncassare() : BigDecimal.ZERO ;
		ModificaMovimentoGestioneEntrata modifica = new ModificaMovimentoGestioneEntrata();
		modifica.setDescrizione("Modifica contestuale all'inserimento dell'ordinativo"); //TODO utilizzare CostantiFin.MODIFICA_CONTESTUALE_INSERIMENTO_ORDINATIVO
		modifica.setParereFinanziario(Boolean.FALSE);
		modifica.setImportoOld(acc.getImportoAttuale());
		modifica.setImportoNew(importoDaEmettere.subtract(disponibilitaIncassare));
		modifica.setAttoAmministrativo(acc.getAttoAmministrativo());
		modifica.setDescrizioneModificaMovimentoGestione("Modifica contestuale all'inserimento dell'ordinativo"); //TODO utilizzare CostantiFin.MODIFICA_CONTESTUALE_INSERIMENTO_ORDINATIVO
		if(acc instanceof SubAccertamento){
			modifica.setTipoMovimento("SAC");
			SubAccertamento s = new SubAccertamento();
			s.setUid(acc.getUid());
			s.setNumeroBigDecimal(acc.getNumeroBigDecimal());
			modifica.setSubAccertamento(s);
			
		}else{
			modifica.setTipoMovimento("ACC");
			Accertamento a = new Accertamento();
			a.setUid(acc.getUid());
			a.setNumeroBigDecimal(acc.getNumeroBigDecimal());
			modifica.setAccertamento(a);
		}
		
		return modifica;
	}

	/**
	 * Ricerca il tipoOnere compatibile con il tipoIVASplitReverse indicato nella quota
	 * 
	 * @return
	 */
	private DettaglioOnere findDettaglioOnereCompatibile(SubdocumentoSpesa subdocSpesa) {
		if(subdocSpesa.getDocumento().getRitenuteDocumento() == null || subdocSpesa.getDocumento().getRitenuteDocumento().getListaOnere() == null){
			return null;
		}
		List<DettaglioOnere> dettagliOnere = subdocSpesa.getDocumento().getRitenuteDocumento().getListaOnere();
		for(DettaglioOnere dettaglioOnere : dettagliOnere){
			if(subdocSpesa.getTipoIvaSplitReverse().equals(dettaglioOnere.getTipoOnere().getTipoIvaSplitReverse()) && isImportoCaricoSoggettoMaggioreDiZero(dettaglioOnere)){
				return dettaglioOnere;
			}
		}
		return null;
	}
	
	/**
	 * Checks if is importo carico soggetto maggiore di zero.
	 *
	 * @param dettaglioOnere the dettaglio onere
	 * @return <code>true</code>, se l'importo importo carico soggetto maggiore di zero
	 */
	private boolean isImportoCaricoSoggettoMaggioreDiZero(DettaglioOnere dettaglioOnere) {
		final String methodName="isImportoOnereMaggioreDiZero";
		//SIAC-5086: devo controllare qui, altrimenti rischio di prendere un dettaglio onere con importo a carico del soggetto =0 e scoprirlo solo dopo,
		//quando non so se c'e' un altro onere con importo a carico del soggetto >0
		if(dettaglioOnere.getImportoCaricoSoggetto() == null || dettaglioOnere.getImportoCaricoSoggetto().compareTo(BigDecimal.ZERO) == 0){
			log.debug(methodName, "Il dettaglio onere con uid: " + dettaglioOnere.getUid() + " nha un importo a carico soggetto nullo o non valorizzato.");
			return false;
		}
		return true;
	}
	
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*----------------------------------------- RICHIAMO A SERVIZI ESTERNI --------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	
	/**
	 * Inserimento dell'accertamento automatico.
	 * 
	 * @param capitolo           il capitolo per cui inserire l'accertamento
	 * @param documento          il documento di riferimento
	 * @param attoAmministrativo l'atto amministrativo dell'accertamento
	 * @param dettaglioOnere     il dettaglio dell'onere del documento
	 * 
	 * @return l'accertamento inserito
	 */
	private Accertamento inserisciAccertamento(Accertamento accertamento) {
		final String methodName = "inserisciAccertamentoAutomatico";
		InserisceAccertamenti reqIA = new InserisceAccertamenti();
		
		reqIA.setDataOra(new Date());
		reqIA.setRichiedente(req.getRichiedente());
		reqIA.setBilancio(bilancio);
		reqIA.setEnte(ente);
		reqIA.setPrimoAccertamentoDaInserire(accertamento);
		
		InserisceAccertamentiResponse resIA = movimentoGestioneService.inserisceAccertamenti(reqIA);
		checkServiceResponseFallimento(resIA);
		Accertamento accertamentoDaServizio = resIA.getElencoAccertamentiInseriti().get(0);
		log.debug(methodName, "Inserito accertamento automatico " + accertamentoDaServizio.getUid());
		accertamento.setUid(accertamentoDaServizio.getUid());
		accertamento.setNumeroBigDecimal(accertamentoDaServizio.getNumeroBigDecimal());
		return accertamento;
	}
	
	@Override
	//SIAC-8017-CMTO
	protected void impostaMessaggiInResponse(List<Messaggio> messaggi) {
		res.addMessaggi(messaggi);
	}
		
}
