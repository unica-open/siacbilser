/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.appjwebsrv.business.EsitoServizio;
import it.csi.appjwebsrv.business.Evasioni;
import it.csi.appjwebsrv.business.WSInterface;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.ContoTesoreriaDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.LiquidazioneBilDad;
import it.csi.siac.siacbilser.integration.dad.OrdineDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Ordine;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneModulare;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneModulareResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;


/**
 * Componente utilizzato nei servizi che inseriscono una Registrazione per la Contabilità Generale (modulo GEN).
 * @author Domenico
 */

@Component 
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LiquidazioneServiceHelper extends ServiceHelper {
	
	protected LogUtil log = new LogUtil(this.getClass());
	
	//Services
	//APPJ
	@Autowired
	private WSInterface wWSInterface;
	@Autowired
	private SoggettoService soggettoService;
	@Autowired
	private LiquidazioneService liquidazioneService;
	
	// JPA
	@PersistenceContext
	private EntityManager em;
	
	//DADs
	@Autowired
	private ContoTesoreriaDad contoTesoreriaDad;
	@Autowired
	private SoggettoDad soggettoDad;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private OrdineDad ordineDad;
	@Autowired
	private LiquidazioneBilDad liquidazioneBilDad;
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	@Autowired 
	private CodificaDad codificaDad;
	
	//Fields
	private Bilancio bilancio;
	private boolean isFromAllegatoAtto;
//	private DocumentoSpesa documentoAssociato;
	
//	-----------------------------------------------
	
	public void init(ServiceExecutor serviceExecutor, Ente ente, Richiedente richiedente, Bilancio bilancio, boolean isFromAllegatoAtto) {
		this.init(serviceExecutor, ente, richiedente);
		this.bilancio = bilancio;
		
		soggettoDad.setEnte(ente);
		
		this.isFromAllegatoAtto = isFromAllegatoAtto;
	}

	
//	-----------------------------------------------------------------------------------
	
	public boolean isOrdinePresente(DocumentoSpesa doc){
		log.debug("isOrdinePresente", "inizio");
		Long numeroOrdini = documentoSpesaDad.countOrdiniAssociati(doc);
		log.debug("isOrdinePresente", "trovati ordini: " + numeroOrdini);
		return Long.valueOf(1).compareTo(numeroOrdini) <= 0;
	}
	
	public EsitoServizio  checkOrdiniEvasi(SubdocumentoSpesa subdoc){
		String methodName = "checkOrdiniEvasi";
		
		DocumentoSpesa doc = subdoc.getDocumento();
		Evasioni evasioni = new Evasioni();
		evasioni.setAnnoDocumento(doc.getAnno().toString());
		evasioni.setCodiceFornitore(Integer.valueOf(doc.getSoggetto().getCodiceSoggetto()));
		String codiceUtente = StringUtils.substring(richiedente.getOperatore().getCodiceFiscale(), 0, 8);
		evasioni.setCodiceUtente(codiceUtente);
		evasioni.setNumeroDocumento(doc.getNumero());
		evasioni.setTipoDocumento(doc.getTipoDocumento().getCodice());
		it.csi.appjwebsrv.business.Impegno impegno = createImpegno(subdoc);
		evasioni.getImpegni().add(impegno);
		List<String> ordini = createOrdini(doc);
		evasioni.getOrdini().addAll(ordini);
		
		long currentTimeMillis = System.currentTimeMillis();
		EsitoServizio esitoServizio = wWSInterface.verificaEvasione(evasioni);
		long elapsed = System.currentTimeMillis()-currentTimeMillis;
		log.info(methodName, "Servizio verificaEvasioni: elapsed time: "+elapsed +" ms.");
		
		return esitoServizio;
	}
	
	private it.csi.appjwebsrv.business.Impegno createImpegno(SubdocumentoSpesa subdoc) {
		it.csi.appjwebsrv.business.Impegno imp = new it.csi.appjwebsrv.business.Impegno();
		imp.setAnnoImpegno(String.valueOf(subdoc.getImpegno().getAnnoMovimento()));
		imp.setImportoQuota(subdoc.getImportoDaPagare());
		imp.setNumeroImpegno(Integer.valueOf(subdoc.getImpegno().getNumero().intValue()));
		imp.setNumeroPrenotazione(subdoc.getSubImpegno() != null ? Integer.valueOf(subdoc.getSubImpegno().getNumero().intValue()) : Integer.valueOf(0));
		return imp;
	}
	
	private List<String> createOrdini(DocumentoSpesa doc) {
		List<String> ordiniString = new ArrayList<String>();
		List<Ordine> ordini = ordineDad.ricercaOrdiniDocumento(doc);
		for(Ordine ordine : ordini){
			ordiniString.add(ordine.getNumeroOrdine());
		}
		return ordiniString;
	}

	public Liquidazione inserisciLiquidazione(Liquidazione liquidazione) {
		InserisceLiquidazione reqIL = new InserisceLiquidazione();
		reqIL.setRichiedente(richiedente);
		reqIL.setBilancio(bilancio);
		reqIL.setEnte(ente);
		reqIL.setAnnoEsercizio(String.valueOf(bilancio.getAnno()));
		reqIL.setLiquidazione(liquidazione);
		
		InserisceLiquidazioneResponse resIL = liquidazioneService.inserisceLiquidazione(reqIL);
		log.logXmlTypeObject(resIL, "");
		serviceExecutor.checkServiceResponseFallimento(resIL);
		return resIL.getLiquidazione();
	}
	
	public void aggiornaLiquidazioneModulare(Liquidazione liquidazione) {
		
		Bilancio bil = liquidazioneBilDad.findBilancioAssociatoALiquidazione(liquidazione.getUid());
		AggiornaLiquidazioneModulare reqALM = new AggiornaLiquidazioneModulare();
		reqALM.setRichiedente(richiedente);
		reqALM.setBilancio(bil);
		reqALM.setEnte(ente);
		reqALM.setLiquidazione(liquidazione);
		reqALM.setFlagAggiornaContoTesoreria(true);
		reqALM.setFlagAggiornaModalitaPagamento(true);
		reqALM.setFlagAggiornaStato(!StatoOperativoLiquidazione.PROVVISORIO.equals(liquidazione.getStatoOperativoLiquidazione()));
		
		AggiornaLiquidazioneModulareResponse resALM = liquidazioneService.aggiornaLiquidazioneModulare(reqALM);
		serviceExecutor.checkServiceResponseFallimento(resALM);
		
		
	}
	
	public Liquidazione popolaLiquidazione(SubdocumentoSpesa subdoc) {
		
		Liquidazione liquidazione = new Liquidazione();
		
		//se sono in aggiornamento della liquidazione -> carico quella presente sul db e sovrascrivo dopo i dati che potrebbero essere cambiati
		if(subdoc.getLiquidazione() != null && subdoc.getLiquidazione().getUid() != 0){		
			RicercaLiquidazionePerChiaveResponse resRLPC= ricercaLiquidazionePerChiave(subdoc.getLiquidazione(), bilancio);
			liquidazione = resRLPC.getLiquidazione();
			if(liquidazione == null){
				throw new BusinessException("Errore aggioranmento Liquidazione "+subdoc.getLiquidazione().getAnnoLiquidazione()+"/"+subdoc.getLiquidazione().getNumeroLiquidazione()
				+": Impossibile trovare la liquidazione con uid: "+subdoc.getLiquidazione().getUid());
			}
		}
		
//		TransazioneElementare te = subdoc.getImpegnoOSubImpegno(); //firstNotNull(subdoc.getSubImpegno(), subdoc.getImpegno());
//		copiaDatiDaTransazioneElementareALiquidazione(te,liquidazione);
		
		Impegno impegno = subdoc.getImpegno();
		SubImpegno subImpegno = subdoc.getSubImpegno();
		//impegnoBilDad.aggiungiCodiciClasssificatoriAImpegnoEOSubImpegno(impegno,subImpegno);
		
		if(subImpegno != null){
			impostaCodiciNellaLiquidazione(subImpegno, liquidazione);
		}else{
			impostaCodiciNellaLiquidazione(impegno, liquidazione);
		}
		
		liquidazione.setImportoLiquidazione(subdoc.getImportoDaPagare());
		liquidazione.setDescrizioneLiquidazione(subdoc.getCausaleOrdinativo());
		
		liquidazione.setImpegno(subdoc.getImpegno());
		if(subdoc.getSubImpegno() != null){
			liquidazione.setSubImpegno(subdoc.getSubImpegno());
		}
		liquidazione.setAttoAmministrativoLiquidazione(subdoc.getAttoAmministrativo());
		liquidazione.setCup(subdoc.getCup());
		liquidazione.setCig(subdoc.getCig());
		
		// Il numero del mutuo e' il numero della voceMutuo
		if(subdoc.getVoceMutuo() != null  && StringUtils.isNotBlank(subdoc.getVoceMutuo().getNumeroMutuo())) {
			liquidazione.setNumeroMutuo(integerize(subdoc.getVoceMutuo().getNumeroMutuo()));
		}
		
		if(subdoc.getContoTesoreria() != null){
			// Ricerco i dati: il servizio delle liquidazioni richiede il codice per l'inserimento
			ContoTesoreria contoTesoreria = contoTesoreriaDad.findByUid(subdoc.getContoTesoreria().getUid());
			liquidazione.setContoTesoreria(contoTesoreria);
		}
		
		//e l'operatore ha l'azione associata con quietanza ('OP-Spe-insDocSpeQuietanza' o 'OP-Spe-aggDocSpeQuietanza').
		if(Boolean.TRUE.equals(subdoc.getFlagACopertura()) || (subdoc.getAttoAmministrativo().getAllegatoAtto()!= null && subdoc.getAttoAmministrativo().getAllegatoAtto().getUid() != 0)){
			liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.PROVVISORIO);
		}else{
			liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.VALIDO);
		}
		
		//gestione soggetto e modalita' pagamento
		Soggetto soggettoLiquidazione = creaSoggettoLiquidazione(subdoc);
		liquidazione.setSoggettoLiquidazione(soggettoLiquidazione);
		
		//se il TipoAccredito e' CSC o CSI la gestione della modalita' pagamento e' differente
		gestisciCessioneDelCredito(liquidazione);
		
		SubdocumentoSpesa quota = new SubdocumentoSpesa();
		quota.setUid(subdoc.getUid());
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setUid(subdoc.getDocumento().getUid());
		doc.setTipoDocumento(subdoc.getDocumento().getTipoDocumento());
		quota.setDocumento(doc);
		
		if(subdoc.getAttoAmministrativo()!=null) {
			quota.setAttoAmministrativo(subdoc.getAttoAmministrativo());
		}
		
		// SIAC-5391
		liquidazione.setSiopeAssenzaMotivazione(estraiSiopeAssenzaMotivazione(subdoc));
		SiopeTipoDebito siopeTipoDebito = impegnoBilDad.findSiopeTipoDebito(impegno, subImpegno);
		liquidazione.setSiopeTipoDebito(siopeTipoDebito);
			
		liquidazione.setSubdocumentoSpesa(quota);
		liquidazione.setForza(true);
		return liquidazione;
	}


	/**
	 * @param subdoc
	 * @return
	 */
	private SiopeAssenzaMotivazione estraiSiopeAssenzaMotivazione(SubdocumentoSpesa subdoc) {
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = subdoc.getSiopeAssenzaMotivazione();
		if(siopeAssenzaMotivazione == null || siopeAssenzaMotivazione.getUid()==0) {
			return null;
		}
		if(StringUtils.isEmpty(siopeAssenzaMotivazione.getCodice())) {
			siopeAssenzaMotivazione = codificaDad.ricercaCodifica(SiopeAssenzaMotivazione.class, siopeAssenzaMotivazione.getUid());
		}
		return siopeAssenzaMotivazione;
	}
	
	public void popolaLiquidazioneAggiornamento(SubdocumentoSpesa subdoc) {
		
		Liquidazione liquidazione = subdoc.getLiquidazione();
		
		if(subdoc.getContoTesoreria() != null){
			// Ricerco i dati: il servizio delle liquidazioni richiede il codice per l'inserimento
			ContoTesoreria contoTesoreria = contoTesoreriaDad.findByUid(subdoc.getContoTesoreria().getUid());
			liquidazione.setContoTesoreria(contoTesoreria);
		}
		
		//e l'operatore ha l'azione associata con quietanza ('OP-Spe-insDocSpeQuietanza' o 'OP-Spe-aggDocSpeQuietanza').
		if(Boolean.TRUE.equals(subdoc.getFlagACopertura()) || (subdoc.getAttoAmministrativo().getAllegatoAtto()!= null && subdoc.getAttoAmministrativo().getAllegatoAtto().getUid() != 0)){
			liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.PROVVISORIO);
		}else{
			liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.VALIDO);
		}
		
		//gestione soggetto e modalita' pagamento
		Soggetto soggettoLiquidazione = creaSoggettoLiquidazione(subdoc);
		liquidazione.setSoggettoLiquidazione(soggettoLiquidazione);
		gestisciCessioneDelCredito(liquidazione);
		
		//modifica dell'importo della liquidazione. (SIAC-4592)
		liquidazione.setImportoLiquidazione(subdoc.getImportoDaPagare());
		liquidazione.setImpegno(subdoc.getImpegno());
		liquidazione.setSubImpegno(subdoc.getSubImpegno());
		
		// SIAC-5391
		liquidazione.setSiopeAssenzaMotivazione(estraiSiopeAssenzaMotivazione(subdoc));
		SiopeTipoDebito siopeTipoDebito = impegnoBilDad.findSiopeTipoDebito(subdoc.getImpegno(), subdoc.getSubImpegno());
		liquidazione.setSiopeTipoDebito(siopeTipoDebito);
		
	}
	
	
	private RicercaLiquidazionePerChiaveResponse ricercaLiquidazionePerChiave(Liquidazione liquidazione, Bilancio bilancio2) {
		RicercaLiquidazionePerChiave reqRLPC = new RicercaLiquidazionePerChiave();
		reqRLPC.setRichiedente(richiedente);
		reqRLPC.setEnte(ente);
		RicercaLiquidazioneK k = new RicercaLiquidazioneK();
		k.setAnnoEsercizio(bilancio.getAnno());
		k.setAnnoLiquidazione(liquidazione.getAnnoLiquidazione());
		k.setBilancio(bilancio);
		k.setLiquidazione(liquidazione);
		k.setNumeroLiquidazione(liquidazione.getNumeroLiquidazione());
		//k.setTipoRicerca(""); //Lascio il default
		reqRLPC.setpRicercaLiquidazioneK(k);
		RicercaLiquidazionePerChiaveResponse resRLPC = liquidazioneService.ricercaLiquidazionePerChiave(reqRLPC);
		
		return resRLPC;
	}
	
	private void impostaCodiciNellaLiquidazione(Impegno impegno, Liquidazione l) {
		l.setCodPdc(impegno.getCodPdc() != null ? impegno.getCodPdc() : "");
		l.setCodTransazioneEuropeaSpesa(impegno.getCodTransazioneEuropeaSpesa() != null ? impegno.getCodTransazioneEuropeaSpesa() : "");
		l.setCodRicorrenteSpesa(impegno.getCodRicorrenteSpesa() != null ? impegno.getCodRicorrenteSpesa() : "");
		l.setCodContoEconomico(impegno.getCodContoEconomico() != null ? impegno.getCodContoEconomico() : "" );
		l.setCodSiope(impegno.getCodSiope() != null ? impegno.getCodSiope() : "" );
		l.setCodCofog(impegno.getCodCofog() != null ? impegno.getCodCofog() : "");
		// SIAC-3655
		l.setCodCapitoloSanitarioSpesa(impegno.getCodCapitoloSanitarioSpesa() != null ? impegno.getCodCapitoloSanitarioSpesa() : "");
		l.setCodPrgPolReg(impegno.getCodPrgPolReg() != null ? impegno.getCodPrgPolReg() : "");
	}
	
	private Integer integerize(String str) {
		final String methodName = "integerize";
		try {
			return Integer.valueOf(str);
		} catch(NumberFormatException nfe) {
			log.debug(methodName, "NumberFormatException per la stringa " + str + ": " + nfe.getMessage());
		} catch(NullPointerException npe) {
			log.debug(methodName, "NullPointerException per la stringa " + str + ": " + npe.getMessage());
		}
		log.debug(methodName, "Fallimento dell'integerizzazione della stringa " + str + ". Restituisco null");
		return null;
	}
	
	private Soggetto creaSoggettoLiquidazione(SubdocumentoSpesa subdoc) {
		String methodName = "creaSoggettoLiquidazione";
		Soggetto soggettoLiquidazione = new Soggetto();
		
		RicercaSoggettoPerChiaveResponse resRSPC = ricercaSoggettoPerChiave(subdoc.getDocumento().getSoggetto().getCodiceSoggetto());
		Soggetto soggettoDocumento = resRSPC.getSoggetto();
		if(soggettoDocumento == null || soggettoDocumento.getUid() == 0){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", "codice " + subdoc.getDocumento().getSoggetto().getCodiceSoggetto()));
		}
		
		soggettoLiquidazione.setUid(soggettoDocumento.getUid());
		soggettoLiquidazione.setCodiceSoggetto(soggettoDocumento.getCodiceSoggetto());
		soggettoLiquidazione.setStatoOperativo(soggettoDocumento.getStatoOperativo());
		
		List<ModalitaPagamentoSoggetto> modalitaPagamentoList = new ArrayList<ModalitaPagamentoSoggetto>();
		if(resRSPC.getListaModalitaPagamentoSoggetto() == null) {
			// TODO
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("ListaModalitaPagamentoSoggetto", "Soggetto codice " + subdoc.getDocumento().getSoggetto().getCodiceSoggetto()));
		}
		if(subdoc.getModalitaPagamentoSoggetto() != null) {
			ModalitaPagamentoSoggetto mpsQuota = subdoc.getModalitaPagamentoSoggetto();
			int uidMpsQuota = this.isFromAllegatoAtto && mpsQuota.getModalitaPagamentoSoggettoCessione2() != null && mpsQuota.getModalitaPagamentoSoggettoCessione2().getUid() != 0
					? mpsQuota.getModalitaPagamentoSoggettoCessione2().getUid()
					: mpsQuota.getUid();
			
			for(ModalitaPagamentoSoggetto mps : resRSPC.getListaModalitaPagamentoSoggetto()){
				int uidMps = mps.getModalitaPagamentoSoggettoCessione2() != null && mps.getModalitaPagamentoSoggettoCessione2().getUid() != 0
						? mps.getModalitaPagamentoSoggettoCessione2().getUid()
						: mps.getUid();
				if(uidMps == uidMpsQuota){
					mps = ricercaModalitaPagamento(mps, soggettoDocumento);
					if(mps == null){
						log.error(methodName, "Attenzione! Nessuna modalita pagamento soggetto trovata per uid:"+uidMps); //valutare throw new BusinessException
					}
					modalitaPagamentoList.add(mps);
					break;
				}
			}
		}
		soggettoLiquidazione.setModalitaPagamentoList(modalitaPagamentoList);
		
		if(subdoc.getSedeSecondariaSoggetto() != null){
			List<SedeSecondariaSoggetto> sediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
			SedeSecondariaSoggetto sedeSecondariaSoggetto = new SedeSecondariaSoggetto();
			sedeSecondariaSoggetto.setUid(subdoc.getSedeSecondariaSoggetto().getUid());
			sediSecondarie.add(sedeSecondariaSoggetto);
			soggettoLiquidazione.setSediSecondarie(sediSecondarie);
		}
		
		return soggettoLiquidazione;
	}
	
	private void gestisciCessioneDelCredito(Liquidazione liquidazione) {
		
		Soggetto soggettoLiquidazione = liquidazione.getSoggettoLiquidazione();
		ModalitaPagamentoSoggetto mps = null;
		if(soggettoLiquidazione.getModalitaPagamentoList()!=null && !soggettoLiquidazione.getModalitaPagamentoList().isEmpty()){
			//riutilizzo ModalitaPagamentoSoggetto caricata dal servizio RicercaModalitaPagamentoPerChiave in precedenza (nel metodo #creaSoggettoLiquidazione)
			mps = soggettoLiquidazione.getModalitaPagamentoList().get(0); 
		}
		if(mps == null) {
			return;
		}
		
		String codiceGruppoAccredito = getCodiceGruppoAccredito(mps);
		
		// TODO: configurazione per FA e PI
		if(isTipoAccreditoCSCLike(codiceGruppoAccredito)) {
			Soggetto soggettoCessione = ottieniSoggettoCessione(mps);
			ModalitaPagamentoSoggetto mpsCessione = mps.getModalitaPagamentoSoggettoCessione2();
			popolaModalitaPagamentoCessione(liquidazione, mpsCessione, soggettoCessione);
			return;
		}
		
	}
	
	private RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiave(String codiceSoggetto) {
		RicercaSoggettoPerChiave reqRSPC = new RicercaSoggettoPerChiave();
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(codiceSoggetto);
		reqRSPC.setParametroSoggettoK(parametroSoggettoK);
		reqRSPC.setRichiedente(richiedente);
		reqRSPC.setEnte(ente /*documentoAssociato.getEnte()*/);
		RicercaSoggettoPerChiaveResponse resRSPC = soggettoService.ricercaSoggettoPerChiave(reqRSPC);
		return resRSPC;
	}
	
	private ModalitaPagamentoSoggetto ricercaModalitaPagamento(ModalitaPagamentoSoggetto mps, Soggetto soggetto) {
		RicercaModalitaPagamentoPerChiave reqRMPC = new RicercaModalitaPagamentoPerChiave();
		reqRMPC.setEnte(ente);
		reqRMPC.setRichiedente(richiedente);
		reqRMPC.setModalitaPagamentoSoggetto(mps);
		reqRMPC.setSoggetto(soggetto);
		RicercaModalitaPagamentoPerChiaveResponse resRMPS = soggettoService.ricercaModalitaPagamentoPerChiave(reqRMPC);
		return resRMPS.getModalitaPagamentoSoggetto();
	}
	
	private String getCodiceGruppoAccredito(ModalitaPagamentoSoggetto mps) {
		return soggettoDad.ottieniCodiceGruppoAccreditoByTipoAccredito(mps.getTipoAccredito());
	}

	private boolean isTipoAccreditoCSCLike(String codiceGruppoAccredito) {
		return TipoAccredito.CSC.name().equalsIgnoreCase(codiceGruppoAccredito);
	}
	
	private Soggetto ottieniSoggettoCessione(ModalitaPagamentoSoggetto mps) {
		RicercaSoggettoPerChiaveResponse resRSPC = ricercaSoggettoPerChiave(mps.getCessioneCodSoggetto());
		Soggetto soggettoCessione = resRSPC.getSoggetto();
		if(soggettoCessione == null || soggettoCessione.getUid() == 0){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", "codice " + mps.getCessioneCodSoggetto()));
		}
		return soggettoCessione;
	}
	
	private void popolaModalitaPagamentoCessione(Liquidazione liquidazione, ModalitaPagamentoSoggetto mpsCessione, Soggetto soggetto) {
		List<ModalitaPagamentoSoggetto> modalitaPagamentoList = new ArrayList<ModalitaPagamentoSoggetto>();
		modalitaPagamentoList.add(mpsCessione);
		soggetto.setModalitaPagamentoList(modalitaPagamentoList);
		liquidazione.setSoggettoLiquidazione(soggetto);
		liquidazione.setModalitaPagamentoSoggetto(mpsCessione);
	}
	
//	-------------------------------------------------------------------------------------
	
	public void flushAndClear() {
		em.flush();
		em.clear();
	}
	

}
