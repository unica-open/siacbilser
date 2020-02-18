/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pcc;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.pcc.frontend.webservice.msg.InviaOperazioniPCC;
import it.csi.siac.pcc.frontend.webservice.msg.InviaOperazioniPCCResponse;
import it.csi.siac.pcc.marc.schema.marccommontypes_1.ResultType;
import it.csi.siac.pcc.marc.services.webservices_1_0.MarcWSPortType;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.pcc.datioperazione.PopolaStrutturaDatiOperazione;
import it.csi.siac.siacbilser.business.service.pcc.datioperazione.PopolaStrutturaDatiOperazioneFactory;
import it.csi.siac.siacbilser.business.utility.comparator.RegistroComunicazioniPCCInvioComparator;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPccOperazioneTipoEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC.StatoRichiesta;
import it.csi.siac.siacfin2ser.model.SistemaEsterno;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.PrestatoreFEL;
import it.tesoro.fatture.DatiRichiestaOperazioneContabileProxyTipo;
import it.tesoro.fatture.IdentificazioneGeneraleTipo;
import it.tesoro.fatture.ListaOperazioneTipo;
import it.tesoro.fatture.OperazioneTipo;
import it.tesoro.fatture.ProxyOperazioneContabileRichiestaTipo;
import it.tesoro.fatture.StrutturaDatiOperazioneTipo;
import it.tesoro.fatture.TestataAsyncTipo;
import it.tesoro.fatture.TipoOperazioneTipo;



/**
 * Invio operazioni alla Piattaforma di Certificazione Crediti tramite il servizio MARC.
 * 
 * @author Domenico Lisi
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InviaOperazioniPCCService extends CheckedAccountBaseService<InviaOperazioniPCC, InviaOperazioniPCCResponse> {

	
	//DADs
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private EnteDad enteDad;
		
	@Autowired
	private MarcWSPortType marcWS;
	
	@Autowired
	private PopolaStrutturaDatiOperazioneFactory popolaStrutturaDatiOperazioneFactory;
	
	//Fields
	private Counter counter;
	private long progressivoOperazione = 0;
	
	private Map<Integer,DocumentoSpesa> cacheDocumentoSpesa = new HashMap<Integer, DocumentoSpesa>();
	private Map<Integer,SubdocumentoSpesa> cacheSubdocumentoSpesa = new HashMap<Integer, SubdocumentoSpesa>();
	
	protected static final ThreadLocal<DatatypeFactory> DATATYPE_FACTORY_HOLDER = new ThreadLocal<DatatypeFactory>() {
		@Override
		protected DatatypeFactory initialValue() {
			try {
				return DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException e) {
				throw new IllegalStateException("Impossibile ottenere un'istanza di  DatatypeFactory.", e);
			}
		}
	};
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//Non ci sono parametri. L'unico parametro e' l'"ente".
	}
	
	@Override
	protected void init() {
		super.init();
		
		cacheDocumentoSpesa = new HashMap<Integer, DocumentoSpesa>();
		cacheSubdocumentoSpesa = new HashMap<Integer, SubdocumentoSpesa>();
		
		registroComunicazioniPCCDad.setEnte(ente);
		registroComunicazioniPCCDad.setLoginOperazione(loginOperazione);
		
		documentoSpesaDad.setEnte(ente);
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		
		subdocumentoSpesaDad.setEnte(ente);
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		
		counter = new Counter();
		progressivoOperazione = 0;
		
	}
	
	@Override
	@Transactional(timeout=3600)
	public InviaOperazioniPCCResponse executeService(InviaOperazioniPCC serviceRequest) {
		return super.executeService(serviceRequest);
	}
		
	@Override
	protected void execute() {
		String methodName = "execute";
		//data invio non valorizzata e ordinati per inserimento
		List<RegistroComunicazioniPCC> registrazioniDaInviare  = registroComunicazioniPCCDad.ricercaRegistrazioniDaInviareEdImpostaLoStato(StatoRichiesta.PREPARAZIONE_INVIO_A_MARC.name());
		
		Map<String, List<RegistroComunicazioniPCC>> registrazioniRaggruppatePerDocumento = raggruppaRegistrazioniPerDocumento(registrazioniDaInviare);
		log.debug(methodName, "Numero di Documenti da inviare: " + registrazioniRaggruppatePerDocumento.size() + " numero di Registrazioni: "
				+ registrazioniDaInviare.size());
		
		counter.totDocumenti = registrazioniRaggruppatePerDocumento.size();
		counter.totRegistrazioni = registrazioniDaInviare.size();
		
		for(Entry<String, List<RegistroComunicazioniPCC>> entry : registrazioniRaggruppatePerDocumento.entrySet()) {
			String key = entry.getKey();
			List<RegistroComunicazioniPCC> registrazioniDiUnDocumento = entry.getValue();
			try {
				inviaGruppoRegistrazioni(registrazioniDiUnDocumento, key);
			} catch (BusinessException be){
				Errore errore = be!=null?be.getErrore():null;
				String msg = errore!=null?errore.getTesto():null;
				if(msg == null){
					msg = "Si e' verificato un errore di business nella trasmissione del documento "+key+". Messaggio: "+ (be!=null?be.getMessage():"null.");
					errore = new Errore("ERR1",msg);
				}
				log.error(methodName, "Si e' verificato un errore di business nella trasmissione del documento "+key+". Messaggio: "+ msg);
				impostaStatoRegistrazioni(registrazioniDiUnDocumento, StatoRichiesta.ERRORE_BUSINESS_INVIO_A_MARC.name());
				//Impostando questo stato il documento NON verrà ripreso per essere rinviato
				res.addErrore(errore);
				counter.totDocInviatiConErroreBusiness++;
			} catch (RuntimeException re){
				String msg = "Si e' verificato un errore tecnico nella trasmissione del documento "+key+". Messaggio: "+ re.getMessage();
				log.error(methodName, msg, re);
				impostaStatoRegistrazioni(registrazioniDiUnDocumento, null);
				//Impostando questo stato il documento verrà ripreso per essere rinviato
				res.addErrore(new it.csi.siac.siaccorser.model.Errore("ERR1",msg));
				counter.totDocInviatiConErroreTecnico++;
			}
		}
		
		res.addMessaggio("ELAB_TERMINATA", counter.toString());
	
	}

	private void inviaGruppoRegistrazioni(List<RegistroComunicazioniPCC> registrazioniDiUnDocumento, String key) {
		final String methodName = "inviaGruppoRegistrazioni";
		
		log.info(methodName, "Preparo l'invio per il Documento: " + key + " [totale Registrazioni: "+registrazioniDiUnDocumento.size()+"]");
		
		caricaDatiAggiuntiviRegistrazioni(registrazioniDiUnDocumento);
		
		ProxyOperazioneContabileRichiestaTipo datiRichiestaOperazioneContabile = popolaDatiRichiestaOperazioneContabile(registrazioniDiUnDocumento);
		
		log.info(methodName, "Invio delle registrazioni PCC per il Documento: " + key);
		log.logXmlTypeObject(datiRichiestaOperazioneContabile, "Dati invio richiesta a MARC");
		ResultType result = marcWS.invioOperazioneContabile(datiRichiestaOperazioneContabile);
		log.logXmlTypeObject(result, "result");
		log.info(methodName, "result from MARC: "+result.getCodice() +" - "+ result.getMessaggio());
		
		if (result.getCodice() != null && result.getCodice().matches("0[0-9][0-9]")){ 
			//000: esito positivo, da 001 a 099: esito positivo ma con segnalazione di anomalie non rilevanti
			log.info(methodName, "Invio del documento "+key+" a MARC terminato con esito positivo: "+result.getCodice()+" - "+ result.getMessaggio());
			impostaStatoRegistrazioni(registrazioniDiUnDocumento, StatoRichiesta.INVIATA_A_MARC.name()); 
			//Impostando questo stato il documento non verrà più ripreso dalla "ricercaRegistrazioniDaInviare".
			res.addMessaggio("REG-INV", registrazioniDiUnDocumento.size() + " registrazioni del documento inviate con successo. "+ key);
			counter.totDocInviatiConEsitoPositivo++;
			impostaDataInvioRegistrazioni(registrazioniDiUnDocumento, new Date());
			
		} else if (result.getCodice() != null && result.getCodice().matches("1[0-9][0-9]")) {
			//da 100 a 199: esito negativo a causa di motivi di natura applicativa con autorizzazione al trattamento delle fatture successive dell'ente
			log.error(methodName, "Si e' verificato un errore di business nella trasmissione del documento "+key+" a MARC: "+result.getCodice()+" - "+ result.getMessaggio());
			impostaStatoRegistrazioni(registrazioniDiUnDocumento, StatoRichiesta.ERRORE_BUSINESS_INVIO_A_MARC.name());
			res.addErrore(new it.csi.siac.siaccorser.model.Errore(result.getCodice(),result.getMessaggio()));
			counter.totDocInviatiConErroreBusiness++;
			
		}  else if (result.getCodice() != null && result.getCodice().matches("2[0-9][0-9]")) {
			//o	da 200 a 299: esito negativo a causa di motivi di natura sistemistica/tecnica. La trasimssione verrà rieffettuata.
			log.error(methodName, "Si e' verificato un errore tecnico nella trasmissione del documento "+key+" a MARC: "+result.getCodice()+" - "+ result.getMessaggio());
			impostaStatoRegistrazioni(registrazioniDiUnDocumento, null);
			//Impostando questo stato il documento verrà ripreso per essere rinviato
			res.addErrore(new it.csi.siac.siaccorser.model.Errore(result.getCodice(),result.getMessaggio()));
			counter.totDocInviatiConErroreTecnico++;
		}
	}
	
	private ProxyOperazioneContabileRichiestaTipo popolaDatiRichiestaOperazioneContabile(List<RegistroComunicazioniPCC> registrazioniDiUnDocumento) {
		DocumentoSpesa documentoSpesa = registrazioniDiUnDocumento.get(0).getDocumentoSpesa(); //Il documento e' lo stesso per tutte queste registrazioni.
		
		ProxyOperazioneContabileRichiestaTipo datiRichiestaOperazioneContabile = new ProxyOperazioneContabileRichiestaTipo();
		
		DatiRichiestaOperazioneContabileProxyTipo datiRichiesta = new DatiRichiestaOperazioneContabileProxyTipo();
		
		ListaOperazioneTipo listaOperazione = new ListaOperazioneTipo();
		for(RegistroComunicazioniPCC registrazione : registrazioniDiUnDocumento){
			//Le "OperazioneTipo" si riferiscono ad operazioni sulle singole quote o sul documento.
			OperazioneTipo operazioneTipo = popolaOperazioneTipo(registrazione);
			listaOperazione.getOperazione().add(operazioneTipo);
		}
		datiRichiesta.setListaOperazione(listaOperazione);
		
		
		IdentificazioneGeneraleTipo identificazioneGenerale = new IdentificazioneGeneraleTipo();
		GregorianCalendar dataEmissione = new GregorianCalendar();
		dataEmissione.setTime(documentoSpesa.getDataEmissione());
		identificazioneGenerale.setDataEmissione(dataEmissione); //Data emissione del Documento registrazione.getDocumentoSpesa();
		identificazioneGenerale.setIdFiscaleIvaFornitore(getIdFiscaleIvaFornitore(documentoSpesa));
		identificazioneGenerale.setImportoTotaleDocumento(documentoSpesa.getImporto()); //Importo totale del documento
		identificazioneGenerale.setNumeroFattura(documentoSpesa.getNumero()); //anno + numero no?
		
		datiRichiesta.setIdentificazioneGenerale(identificazioneGenerale);
		
		//IdentificazionePCC - noi usiamo quella Generale!
//		datiRichiesta.setIdentificazionePCC(documentoSpesa.getCodicePCC()!=null?documentoSpesa.getCodicePCC().getCodice():null);
		
		//IdeintificazioneSDI - noi usiamo quella Generale!
//		IdentificazioneSDITipo identificazioneSDI = new IdentificazioneSDITipo();
//		identificazioneSDI.setNumeroFattura(documentoSpesa.getNumero());// ???????
//		identificazioneSDI.setLottoSDI(new BigDecimal(1));// ???????
//		datiRichiesta.setIdentificazioneSDI(identificazioneSDI);
		
		datiRichiestaOperazioneContabile.setDatiRichiesta(datiRichiesta);
		
		//Informazioni della tesata
		TestataAsyncTipo testataRichiesta = new TestataAsyncTipo();
		testataRichiesta.setIdentificativoPCCAmministrazione(getIdentificativoPCCAmministrazione());//Per test: "87670016" 
		testataRichiesta.setCodiceFiscaleTrasmittente(ente.getCodiceFiscale()!=null?ente.getCodiceFiscale():ente.getPartitaIva());
		//testataRichiesta.setCodiceFiscaleTrasmittente("KKKLLL77H55L261L"); //TODO L'ente ha un codice fiscale NON VALIDO! SISTEMARE!! //(ente.getCodiceFiscale()!=null?ente.getCodiceFiscale():ente.getPartitaIva());
		testataRichiesta.setIdentificativoTransazionePA(getIdentificativoTransazionePA(registrazioniDiUnDocumento));
		testataRichiesta.setTimestampTrasmissione(new GregorianCalendar());
		testataRichiesta.setVersioneApplicativa("1.0");
		
		datiRichiestaOperazioneContabile.setTestataRichiesta(testataRichiesta);
		
		return datiRichiestaOperazioneContabile;
	}

	/**
	 * Restituisce IT e poi la partita IVA se presente, o in alternativa il CF del soggetto abbinato al documento fattura.
	 * 
	 * @param documentoSpesa
	 * @return l'id fiscale del fornitore
	 */
	private String getIdFiscaleIvaFornitore(DocumentoSpesa documentoSpesa) {
		// SIAC-5115: prendo i dati della fattura FEL. Fallback alla vecchia implementazione se non presenti
		String result = getIdFiscaleIvaFornitoreByFatturaFEL(documentoSpesa);
		if(StringUtils.isBlank(result)) {
			// Fallback
			result = getIdFiscaleIvaFornitoreFallback(documentoSpesa);
		}
		return result;
	}
	
	/**
	 * Restituisce il codice paese del fornitore FEL e il suo codice.
	 * 
	 * @param documentoSpesa
	 * @return l'id fiscale del fornitore
	 */
	private String getIdFiscaleIvaFornitoreByFatturaFEL(DocumentoSpesa documentoSpesa) {
		final String methodName = "getIdFiscaleIvaFornitoreByFatturaFEL";
		FatturaFEL fatturaFEL = documentoSpesa.getFatturaFEL();
		if(fatturaFEL == null) {
			log.debug(methodName, "Fattura FEL non presente per il documento [" + documentoSpesa.getUid() + "]");
			return null;
		}
		PrestatoreFEL prestatoreFEL = fatturaFEL.getPrestatore();
		if(prestatoreFEL == null) {
			log.debug(methodName, "Prestatore FEL non presente per la fattura [" + fatturaFEL.getUid() + "] collegata al documento [" + documentoSpesa.getUid() + "]");
			return null;
		}
		return prestatoreFEL.getCodicePaese() + prestatoreFEL.getCodicePrestatore();
	}
	
	/**
	 * Restituisce IT e poi la partita IVA se presente, o in alternativa il CF del soggetto abbinato al documento fattura.
	 * 
	 * @param documentoSpesa
	 * @return l'id fiscale del fornitore
	 */
	private String getIdFiscaleIvaFornitoreFallback(DocumentoSpesa documentoSpesa) {
		Soggetto soggetto = documentoSpesa.getSoggetto();
		if(StringUtils.isNotBlank(soggetto.getPartitaIva())){
			return "IT"+ soggetto.getPartitaIva().trim();
		}
		if(StringUtils.isNotBlank(soggetto.getCodiceFiscale())) {
			return soggetto.getCodiceFiscale().trim();
		}
		throw new IllegalStateException("Il Soggetto "+soggetto.getCodiceSoggetto()+" [uid: "+soggetto.getUid()+"] deve avere valorizzato almeno uno dei due campi: Partita Iva o Codice Fiscale.");
	}
	
	/**
	 * Raggruppa le registrazioni da inviare per Documento.
	 * <p>
	 * <strong>SIAC-4981</strong>
	 * <p>
	 * Attualmente i servizi PCC non gestiscono per una <code>id_transazione_pa</code> un numero di transazioni maggiore di 30, per questo motivo una fattura che
	 * ad es. ha 16 quote di cui si comunicano le operazioni di contabilizzazione CO e comunicazione scadenza CS, va in errore.
	 * <br/>
	 * Questo limite ci &eacute; stato comunicato via ticket da SOGEI, non &eacute; presente nella guida tecnica, verr&agrave; aggiunto dal Ministero in futuro.
	 * 
	 * <p>
	 * A questo punto &eacute; necessario cambiare leggermente la logica attuale che per ogni fattura attualmente crea un'unica transazione pa:
	 * quando una transazione pa raggiunge le 30 operazioni collegate, &eacute; necessario creare un nuovo <code>id_transazione</code> con al più altre 30 operazioni collegate.
	 * <br/>
	 * Questo significa che una fattura potrebbe anche avere pi&uacute; transazioni pa associate alle operazioni delle singole quote.
	 * 
	 * @param registrazioniDaInviare
	 * @return 
	 */
	private Map<String, List<RegistroComunicazioniPCC>> raggruppaRegistrazioniPerDocumento(List<RegistroComunicazioniPCC> registrazioniDaInviare) {
		final String methodName = "raggruppaRegistrazioniPerDocumento";
		
		Map<String, List<RegistroComunicazioniPCC>> map = new HashMap<String, List<RegistroComunicazioniPCC>>();
		
		for(RegistroComunicazioniPCC registrazione : registrazioniDaInviare) {
			String key = "docId:"+registrazione.getDocumentoSpesa().getUid();
			
			if(!map.containsKey(key)){
				map.put(key, new ArrayList<RegistroComunicazioniPCC>());
			}
			
			List<RegistroComunicazioniPCC> group = map.get(key);
			group.add(registrazione);
		}
		// SIAC-5115: ordino le comunicazioni: prima le CCS, poi le altre (devo evitare che le CCS e le CS si interpongano)
		for(Entry<String, List<RegistroComunicazioniPCC>> entry : map.entrySet()) {
			log.debug(methodName, "Sorting delle comunicazioni PCC: precedenza alle CCS, successivamente le altre. Elaborazione per chiave " + entry.getKey());
			sortComunicazioniPCC(entry.getValue());
		}
		
		// SIAC-5464: inserisco solo una CS, le altre le segno come invio fittizio
		handleFakeSend(map);
		
		// SIAC-4981: limite delle dimensioni pari a 30
		return splitValueList(map, 30);
		
	}

	/**
	 * Gestione dell'invio fasullo verso PCC
	 * @param methodName
	 * @param map
	 */
	private void handleFakeSend(Map<String, List<RegistroComunicazioniPCC>> map) {
		final String methodName = "handleFakeSend";
		List<RegistroComunicazioniPCC> comunicazioniScadenzaFittizie = new ArrayList<RegistroComunicazioniPCC>();
		for(Entry<String, List<RegistroComunicazioniPCC>> entry : map.entrySet()) {
			log.debug(methodName, "Rimozione delle comunicazioni CS oltre la prima per chiave " + entry.getKey());
			fakeSendCS(entry.getValue(), comunicazioniScadenzaFittizie);
		}
		if(!comunicazioniScadenzaFittizie.isEmpty()) {
			log.debug(methodName, "Invio fasullo di " + comunicazioniScadenzaFittizie.size() + " comunicazioni");
			registroComunicazioniPCCDad.fakeInvio(comunicazioniScadenzaFittizie);
		}
	}
	
	/**
	 * Ordino le comunicazioni a PCC
	 * @param list la lista di comunicazioni da ordinare
	 */
	private void sortComunicazioniPCC(List<RegistroComunicazioniPCC> list) {
		Collections.sort(list, RegistroComunicazioniPCCInvioComparator.INSTANCE);
	}
	
	/**
	 * Invio fasullo delle comunicazioni CS verso PCC
	 * @param list la lista di comunicazioni da inviare
	 */
	private void fakeSendCS(List<RegistroComunicazioniPCC> list, List<RegistroComunicazioniPCC> comunicazioniScadenzaFittizie) {
		final String methodName = "fakeSendCS";
		Set<Integer> uidSubdocs = new HashSet<Integer>();
		
		for(Iterator<RegistroComunicazioniPCC> it = list.iterator(); it.hasNext();) {
			RegistroComunicazioniPCC rcp = it.next();
			TipoOperazionePCC.Value tipoOperazione = TipoOperazionePCC.Value.byTipoOperazionePCC(rcp.getTipoOperazionePCC());
			
			if(TipoOperazionePCC.Value.ComunicazioneDataScadenza.equals(tipoOperazione)) {
				log.debug(methodName, "Trovata comunicazione data scadenza CS con uid " + rcp.getUid());
				if(uidSubdocs.contains(rcp.getSubdocumentoSpesa().getUid())) {
					log.debug(methodName, "Rimozione della registrazione dalla lista da inviare");
					comunicazioniScadenzaFittizie.add(rcp);
					it.remove();
				}
				uidSubdocs.add(rcp.getSubdocumentoSpesa().getUid());
			}
		}
	}

	/**
	 * Suddivide una mappa di liste in una mappa di liste, in cui ciascuna lista &eacute; composta
	 * da al pi&ugrave; <code>size</code> elementi.
	 * @param original la mappa originale
	 * @param size la lunghezza massima delle liste sottostanti
	 * @return la mappa di liste, con ogni lista lunga al pi&ugrave; <code>size</code>
	 */
	private <T> Map<String, List<T>> splitValueList(Map<String, List<T>> original, int size) {
		Map<String, List<T>> res = new HashMap<String, List<T>>();
		
		for(Entry<String, List<T>> entry : original.entrySet()) {
			int entrySize = entry.getValue().size();
			int maxChunks = (entrySize + size - 1) / size;
			
			for(int chunkId = 0; chunkId < maxChunks; chunkId++) {
				List<T> newList = new ArrayList<T>();
				int limit = Math.min((chunkId + 1) * size, entrySize);
				for(int i = chunkId * size; i < limit; i++) {
					newList.add(entry.getValue().get(i));
				}
				res.put(entry.getKey() + "_chunk_" + chunkId, newList);
			}
		}
		
		return res;
	}

	/**
	 * Imposta lo stato delle registrazioni in modo che non siano più reperibili per un ulteriore invio.
	 * Ovvero che non vengano ritrovate dal metodo del DAD registroComunicazioniPCCDad.ricercaRegistrazioniDaInviare();
	 * 
	 * @param registrazioniDaInviare
	 */
	private void impostaStatoRegistrazioni(List<RegistroComunicazioniPCC> registrazioniDaInviare, String stato) {
		registroComunicazioniPCCDad.impostaStatoRegistrazioniTxNew(registrazioniDaInviare, stato); //StatoRichiestaType.IN_CORSO_DI_ACQUISIZIONE.name()
	}
	
	private void impostaDataInvioRegistrazioni(List<RegistroComunicazioniPCC> registrazioniDaInviare, Date dataInvio) {
		registroComunicazioniPCCDad.impostaDataInvioRegistrazioniTxNew(registrazioniDaInviare, dataInvio); //StatoRichiestaType.IN_CORSO_DI_ACQUISIZIONE.name()
	}

	/**
	 * Ricava l'identificativoPCCAmministrazione tramite l'Ente proprietario del registro PCC 
	 * (ad es. utilizzando la tabella siac_d_sistema_esterno e siac_r_sistema_esterno_ente come per il flusso stipendi)
	 *
	 * @return the identificativo pcc amministrazione
	 */
	private Long getIdentificativoPCCAmministrazione(){
		String methodName = "getIdentificativoPCCAmministrazione";
		
		String codiceEnteEsterno = enteDad.findCodiceEnteEsternoByEnteAndSistemaEsterno(ente, SistemaEsterno.PCC);
		
		long result;
		try {
			result = Long.parseLong(codiceEnteEsterno);
		} catch(NumberFormatException nfe){
			throw new BusinessException("Il codiceEnteEsterno per PCC deve essere di tipo Numerico. Viene utilizzato per valorizzare l'IdentificativoPCCAmministrazione dei servizi PCC");
		}
		
		log.debug(methodName, "Returning: "+result);
		return result;
	}
	
	/**
	 * Ottiene l'identificativo Transazione come chiave della richiesta sul registro PCC (id della richiesta);
	 * @param registrazioniDiUnDocumento 
	 *
	 * @return the identificativo transazione pa
	 */
	private String getIdentificativoTransazionePA(List<RegistroComunicazioniPCC> registrazioniDiUnDocumento) {
		final String methodName = "getIdentificativoTransazionePA";
		
		UUID uuid = UUID.randomUUID();
		
		String idTransazionePA = uuid.toString();
		
		registroComunicazioniPCCDad.impostaIdTransazionePARegistrazioniTxNew(registrazioniDiUnDocumento, idTransazionePA);
		log.debug(methodName, "Returning: "+idTransazionePA);
		return idTransazionePA;
		
//		// TODO in analisi c'è scritto:	Ricava l'identificativoTransazionePA come chiave della richiesta sul registro PCC (id della richiesta). Ma non e' sufficiente!!
//		// Gli id della richiesta sono in realtà N per ogni invio! bisogna trovare qui un modo univoco e "ripetibile" di ricavare un ID!
//		
//		List<Integer> ids = new ArrayList<Integer>();
//		for(RegistroComunicazioniPCC reg : registrazioniDiUnDocumento){
//			ids.add(reg.getUid());
//		}
//		Collections.sort(ids);
//		
//		String result = ids.toString();
//		result = result.substring(1, result.length()-1);
//		
//		String timeStampSuffix = generateTimeStampSuffix(); //SIAC-3293 
//		
//		//NOTA: ...se il numero diverra' troppo lungo ocorrera' salvere su db l'identificativo (privo di timeStampSuffix) e restituire qui solo l'id di una sequence.
//		return result + timeStampSuffix;
		
	}

//	private String generateTimeStampSuffix() {
//		return MessageFormat.format("_{0,date,yyyyMMddHHmmssSSS}", new Date());
//	}

	private OperazioneTipo popolaOperazioneTipo(RegistroComunicazioniPCC registrazione) {
		
		OperazioneTipo operazioneTipo = new OperazioneTipo();
		
		TipoOperazioneTipo tipoOperazioneTipo = SiacDPccOperazioneTipoEnum.byCodice(registrazione.getTipoOperazionePCC().getCodice()).getTipoOpeazionePCCValue().getTipoOperazioneTipo();
		
		operazioneTipo.setTipoOperazione(tipoOperazioneTipo);
		
		operazioneTipo.setProgressivoOperazione(getProgressivoOperazione());
		
		PopolaStrutturaDatiOperazione popolaStrutturaDatiOperazione = popolaStrutturaDatiOperazioneFactory.create(tipoOperazioneTipo, registrazione);
		
		StrutturaDatiOperazioneTipo strutturaDatiOperazione = popolaStrutturaDatiOperazione.popolaStrutturaDatiOperazione();
		
		operazioneTipo.setStrutturaDatiOperazione(strutturaDatiOperazione);
		
		return operazioneTipo;
	}
	
	/**
	 * Progressivo Operazione: il campo indica il numero progressivo dell'operazione nell'ambito della transazione comunicata; il  campo è obbligatorio.  
	 * @return Progressivo Operazione
	 */
	private long getProgressivoOperazione(){
		progressivoOperazione++;
		return progressivoOperazione;
	}

	
	/**
	 * Carica dati aggiuntivi necessari per il popolamento dei dati da inviare al servizio InvioOperazioneContabile di MARC.
	 * @param registrazioniDaInviare 
	 */
	private void caricaDatiAggiuntiviRegistrazioni(List<RegistroComunicazioniPCC> registrazioniDaInviare) {
		for (RegistroComunicazioniPCC registroComunicazioniPCC : registrazioniDaInviare) {
			
			DocumentoSpesa documentoSpesa = caricaDocumentoSpesaCached(registroComunicazioniPCC.getDocumentoSpesa().getUid());
			registroComunicazioniPCC.setDocumentoSpesa(documentoSpesa);
			
			SubdocumentoSpesa subdocumentoSpesa = caricaSubdocumentoSpesaCached(registroComunicazioniPCC.getSubdocumentoSpesa().getUid());
			registroComunicazioniPCC.setSubdocumentoSpesa(subdocumentoSpesa);
			
		}
	}
	
	private DocumentoSpesa caricaDocumentoSpesaCached(Integer uidDocumento) {
		
		if(cacheDocumentoSpesa.containsKey(uidDocumento)){
			return cacheDocumentoSpesa.get(uidDocumento);
		}
		
		// SIAC-5115: aggiungere i dati della fattura FEL
		DocumentoSpesa documentoSpesa = documentoSpesaDad.findDocumentoSpesaById(uidDocumento, DocumentoSpesaModelDetail.Soggetto, DocumentoSpesaModelDetail.CodicePCC, DocumentoSpesaModelDetail.FatturaFEL);
		cacheDocumentoSpesa.put(uidDocumento, documentoSpesa);
		return documentoSpesa;
	}
	
	
	private SubdocumentoSpesa caricaSubdocumentoSpesaCached(Integer uidSubdocumento) {
		
		if(cacheSubdocumentoSpesa.containsKey(uidSubdocumento)){
			return cacheSubdocumentoSpesa.get(uidSubdocumento);
		}
		
		SubdocumentoSpesa subdocumentoSpesa = subdocumentoSpesaDad.findSubdocumentoSpesaById(uidSubdocumento);
		cacheSubdocumentoSpesa.put(uidSubdocumento, subdocumentoSpesa);
		return subdocumentoSpesa;
	}
	
	
	
	/**
	 * Utilizzata per comporre il messaggio di riepilogo dell'elaborazione.
	 */
	private static class Counter {
		int totDocumenti = 0;
		int totRegistrazioni = 0;

		int totDocInviatiConEsitoPositivo = 0;
		int totDocInviatiConErroreBusiness = 0;
		int totDocInviatiConErroreTecnico = 0;
		
		Counter() {
		}
		
		@Override
		public String toString() {
			
			String msgRiepilogoFormat = "Elaborati: {0,choice,0#nessun documento|1#un documento|1<{0, number} documenti}"
					+ "{1,choice,0#|1# (una registrazione)|1< ({1, number} registrazioni)}, di cui {2,choice,0#nessuno|1#uno|1<{2, number}} con esito positivo, "
					+ "{3,choice,0#nessuno|1#uno|1<{3, number}} con errore applicativo, {4,choice,0#nessuno|1#uno|1<{4, number}} con errore tecnico.";
			
			String msgRiepilogo = MessageFormat.format(msgRiepilogoFormat, 
					totDocumenti,
					totRegistrazioni,
					totDocInviatiConEsitoPositivo,
					totDocInviatiConErroreBusiness,
					totDocInviatiConErroreTecnico
					);
			
			return msgRiepilogo;
		}
	
	}
	
}

/*

Query di utility per consultare i dati nel registro:

select rpcc_registrazione_data, reg.doc_id, doc.doc_anno, doc.doc_numero, subdoc.subdoc_numero, doc.doc_importo, subdoc.subdoc_importo, 

pcccau_code || ' - '|| pcccau_desc pcc_causale,
pccdeb_stato_code || ' - ' || pccdeb_stato_desc debito_stato,
pccop_tipo_code || ' - ' || pccop_tipo_desc operazione_tipo

from siac_t_registro_pcc reg, 
siac_t_doc doc, 
siac_t_subdoc subdoc, 
--siac_d_pcc_codice  d_pcc_cod, 
siac_d_pcc_debito_stato d_pcc_debito_stato,
siac_d_pcc_causale d_pcc_causale,
siac_d_pcc_operazione_tipo d_pcc_operazione_tipo

where reg.doc_id = doc.doc_id
and reg.subdoc_id = subdoc.subdoc_id
and reg.pcccau_id = d_pcc_causale.pcccau_id
and reg.pccdeb_stato_id = d_pcc_debito_stato.pccdeb_stato_id
and reg.pccop_tipo_id = d_pcc_operazione_tipo.pccop_tipo_id

order by reg.data_creazione


--------------



update siac_t_registro_pcc 
set rpcc_richiesta_stato=null 
where (data_cancellazione is null) and ente_proprietario_id=1 and (rpcc_registrazione_data is null) --and (rpcc_richiesta_stato is null)
and doc_id = 466


 */