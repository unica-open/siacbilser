/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.StampaAllegatoAttoService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAttoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAttoResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;


/**
 * Servizio di invio dell'{@link AllegatoAtto} al sistema "Attiliq" (Flux atti di liquidazione)
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InviaAllegatoAttoService extends CheckedAccountBaseService<InviaAllegatoAtto, InviaAllegatoAttoResponse> {
	
	//Components
	@Autowired
	private AttiliqStartServletInvoker attiliqStartServletInvoker;
	
	//DADs
	@Autowired
	private EnteDad enteDad;
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	@Autowired
	private ClassificatoriDad classificatoriDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;

	
	//Fields
	private AllegatoAtto allegatoAtto;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
	private NumberFormat nf = NumberFormat.getInstance(Locale.ITALY);
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getAllegatoAtto(), "allegato atto");
		
	}
	
	@Override
	protected void init() {
		super.init();
		
		allegatoAttoDad.setEnte(ente);
		allegatoAttoDad.setLoginOperazione(loginOperazione);
		subdocumentoSpesaDad.setEnte(ente);
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		nf.setGroupingUsed(false);	
	}

	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT * 3)
	public void executeService() {
		super.executeService();
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT * 3)
	public InviaAllegatoAttoResponse executeServiceTxRequiresNew(InviaAllegatoAtto serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		String methodName = "execute";
		
		caricaAllegatoAtto();
		stampaDettaglioOperazione();

		checkStrutturaAmmContabileAllegatoAtto();
		
		log.info(methodName, "versione attuale allegatoAtto: "+ allegatoAtto.getVersioneInvioFirma()+ " -> versione che verrà inviata: " + allegatoAtto.getVersioneInvioFirmaNotNull() + 1);
		allegatoAtto.setVersioneInvioFirma(allegatoAtto.getVersioneInvioFirmaNotNull() +1);
		log.info(methodName, "Aggiorno la versione invio firma dell'allegato atto a: "+ allegatoAtto.getVersioneInvioFirmaNotNull());
		//CR-3206 aggiorno anche data_invio, utente_invio
		Date now = new Date();
		allegatoAtto.setDataVersioneInvioFirma(now);
		allegatoAtto.setUtenteVersioneInvioFirma(loginOperazione);
		
		allegatoAttoDad.aggiornaVersioneInvioFirma(allegatoAtto);
		
		StampaAllegatoAttoResponse saar = stampaAllegatoAtto();
		File report = saar.getReport();
		
		if(report == null || report.getContenuto() == null || report.getContenuto().length == 0){
			throw new BusinessException("Impossibile ottenere il report.");
		}
		
		log.info(methodName, "Ottenuto report. [dimensione: " + report.getContenuto().length + " bytes]");
		
		
		inviaAdAttiliq(report);
		
	}

	private void checkStrutturaAmmContabileAllegatoAtto() {
		String methodName = "checkStrutturaAmmContabileAllegatoAtto";
		
		StrutturaAmministrativoContabile sac = allegatoAtto.getAttoAmministrativo().getStrutturaAmmContabile();
		
		if(sac==null || sac.getTipoClassificatore()==null || !SiacDClassTipoEnum.Cdc.getCodice().equals(sac.getTipoClassificatore().getCodice())){
			log.debug(methodName, "StrutturaAmministrativoContabile non valida: "+sac);
			throw new BusinessException("L'atto amministrativo "+allegatoAtto.getAttoAmministrativo().getAnno()+ "/" + allegatoAtto.getAttoAmministrativo().getNumero()+" deve essere associato ad una struttura amministrativo contabile di livello CDC.");
		}
		
		
		log.debug(methodName, "La SAC associata e' all'attoAmministrativo di livello CDC. Atto: "+allegatoAtto.getAttoAmministrativo().getAnno()+ "/" + allegatoAtto.getAttoAmministrativo().getNumero());
	}

	private void caricaAllegatoAtto() {
		this.allegatoAtto = allegatoAttoDad.findAllegatoAttoById(req.getAllegatoAtto().getUid());
	}

	private StampaAllegatoAttoResponse stampaAllegatoAtto() {
		StampaAllegatoAtto reqSAA = new StampaAllegatoAtto();
		reqSAA.setRichiedente(req.getRichiedente());
		reqSAA.setAllegatoAtto(allegatoAtto);
		reqSAA.setEnte(ente);
		
		reqSAA.setBilancio(req.getBilancio());
		reqSAA.setAnnoEsercizio(req.getAnnoEsercizio());
		
		return serviceExecutor.executeServiceSuccess(StampaAllegatoAttoService.class, reqSAA);
	}
	
	
	private void inviaAdAttiliq(File report) {
		String methodName = "inviaAdAttiliq";
		
		Map<String, String> formData = populateFormData();
		
		String reportName = formData.get("direzione")+"_"+
							formData.get("anno") + "_"+
							formData.get("numero")+"_"+
							formData.get("codice_tipo")+"_"+
							formData.get("versione");
		
		report.setNome(reportName);
		if(log.isInfoEnabled()) {
			StringBuilder params = new StringBuilder();
			for(Map.Entry<String, String> formDatum : formData.entrySet()) {
				params.append(formDatum.getKey()).append(" ==> ").append(formDatum.getValue()).append("\n");
			}
			log.info(methodName, params.toString());
		}
		
		
		log.info(methodName, "Trasmissione alla Servlet di Atti Di Liquidazione del report: "+ report.getNome());
		attiliqStartServletInvoker.invoke(formData, report);
	}

	
	/**
	 * Se la data scadenza non &egrave; stata inserita dall'utente, la si popola con la data scadenza della fattura pi&ugrave;
	 * prossima alla scadenza o, se gi&agrave; scaduta, quella pi&ugrave; vecchia.
	 * <br/>
	 * Se invece l'atto non contiene fatture la data pu&ograve; non essere valorizzata.
	 * <br/>
	 * Se vuota non presentare neanche l'etichetta
	 */
	private Date elaboraDataScadenza() {
		final String methodName = "elaboraDataScadenza";
		if(allegatoAtto.getDataScadenza() != null) {
			log.debug(methodName, "Data inserita dall'utente [" + allegatoAtto.getDataScadenza() + "]");
			return allegatoAtto.getDataScadenza();
		}
		// Recupero tutte le date
		// SIAC-5446: utilizzo i subdoc
		
		
		
		List<SubdocumentoSpesa> quoteFatture = subdocumentoSpesaDad.findSubdocWithDateScadenzaFattureByAllegatoAtto(allegatoAtto);
		
		Date now = new Date();
		Date dataScadenzaPiuProssimaAScadenza = null;
		Date dataScadenzaPiuVecchia = null;
		
		for(SubdocumentoSpesa ss : quoteFatture) {
			log.info(methodName, "ss.getDataScadenzaDopoSospensione() --> "+ ss.getDataScadenzaDopoSospensione());
			
			Date dsf = ss.getDataScadenzaDopoSospensione() != null ? ss.getDataScadenzaDopoSospensione() : ss.getDataScadenza();
			
			if(now.before(dsf) && (dataScadenzaPiuProssimaAScadenza == null || dataScadenzaPiuProssimaAScadenza.after(dsf))) {
				// Se la data e' successiva ad ora ed e' prima della data a scadenza piu' prossima gia' registrata...
				log.debug(methodName, "Impostazione della data piu' prossima a scadenza per allegato atto [" + allegatoAtto.getUid() + "] [" + dsf + "]");
				dataScadenzaPiuProssimaAScadenza = dsf;
			} else if(dataScadenzaPiuVecchia == null || dsf.before(dataScadenzaPiuVecchia)) {
				// Se la data di scadenza e' dopo la piu' vecchia gia' registrata
				log.debug(methodName, "Impostazione della data di scadenza piu' vecchia per allegato atto [" + allegatoAtto.getUid() + "] [" + dsf + "]");
				dataScadenzaPiuVecchia = dsf;
			}
		}
		
		Date dataScadenza = dataScadenzaPiuProssimaAScadenza != null ? dataScadenzaPiuProssimaAScadenza : dataScadenzaPiuVecchia;
		log.debug(methodName, "Allegato atto [" + allegatoAtto.getUid() + "], data scadenza piu' prossima a scadenza [" + dataScadenzaPiuProssimaAScadenza+ "], data scadenza piu' vecchia [" + dataScadenzaPiuVecchia + "] => data scadenza [" + dataScadenza + "]");
		
		//log.info(methodName, "dataScadenzaaaaaaaaaaaaaaaaaaaaaaaaaaa --> " + dataScadenza);
		return dataScadenza;
	}
	
	private Map<String, String> populateFormData() {
		String methodName ="populateFormData";
		
		Map<String, String> m = new HashMap<String, String>();
		
//		String codiceEnteEsterno = enteDad.findCodiceEnteEsternoByEnteAndSistemaEsterno(ente, SistemaEsterno.ATTIAMM); //TODO dovra' essere creato un sistemaEsterno ad hoc?
//		log.debug(methodName, "codiceEnteEsterno: "+codiceEnteEsterno);
		m.put("id_ente", "R1"); //codiceEnteEsterno); //Attiliq gestirà in automatico l'Ente. Questo valore lo ignorerà.
		m.put("anno", ""+allegatoAtto.getAttoAmministrativo().getAnno());
		m.put("numero", ""+allegatoAtto.getAttoAmministrativo().getNumero());
		
		StrutturaAmministrativoContabile sac = allegatoAtto.getAttoAmministrativo().getStrutturaAmmContabile();
		log.debug(methodName, "StrutturaAmmContabile: "+ sac.getCodice() +"[uid:"+sac.getUid()+"] tipoClasificatore (CDC expected!): "+sac.getTipoClassificatore().getCodice());
		
		StrutturaAmministrativoContabile sacPadre = classificatoriDad.ricercaPadreClassificatore(sac);
		log.debug(methodName, "StrutturaAmmContabile PADRE: "+ sacPadre.getCodice() +"[uid:"+sacPadre.getUid()+"] tipoClasificatore (CDR expected!): "+sacPadre.getTipoClassificatore().getCodice());
		
		m.put("direzione", sacPadre.getCodice());//A11 FIXME allegatoAtto.getAttoAmministrativo().getStrutturaAmmContabile()!=null?allegatoAtto.getAttoAmministrativo().getStrutturaAmmContabile().getCodice():"ND");
		m.put("settore", sac.getCodice());//000 FIXME allegatoAtto.getAttoAmministrativo().getStrutturaAmmContabile()!=null?allegatoAtto.getAttoAmministrativo().getStrutturaAmmContabile().getCodice():"ND");
		m.put("anno_titolario", ""+allegatoAtto.getAnnoTitolario()); //ma saranno valorizati?!!?!
		m.put("titolario", truncateToMaxLength(""+allegatoAtto.getTitolario(),50));//ma saranno valorizati?!!?! o vanno calcolati qui!??
		m.put("codice_titolario", truncateToMaxLength(""+allegatoAtto.getTitolario(),15)/*+allegatoAtto.getCodiceTitolario()*/);//ma saranno valorizati?!!?! //Il titolario non è gestito da SIAC. Non verrà passato. 
		m.put("utente_scrittore", loginOperazione); // allegatoAtto.getLoginCreazione()
		m.put("causale_pagamento", truncateToMaxLength(allegatoAtto.getCausale(),150));
		m.put("ragsoc_benef", truncateToMaxLength("Beneficiari specificati in elenco",150));
		m.put("codice_tipo", allegatoAtto.getAttoAmministrativo().getTipoAtto()!=null?allegatoAtto.getAttoAmministrativo().getTipoAtto().getCodice():"ND"); //Attiliq gestiva a "AL" ed "EL" //SIAC passa quello che c'è nella sua base dati Attiliq lo gestirà.
		m.put("fl_allegati", toSN(allegatoAtto.getAltriAllegati()!=null));
		m.put("fl_dati_sensibili", toSN(allegatoAtto.getDatiSensibili()));
		m.put("fl_fatture", toSN(esisteAlmenoUnaFattura()));
		m.put("fl_ritenute", toSN(esisteAlmenoUnaRitenuta()));
		m.put("fl_Split_Payment", toSN(esisteAlmenoUnaQuotaConSplitReverse()));
		m.put("versione", allegatoAtto.getVersioneInvioFirmaNotNull().toString());
		m.put("importo", nf.format(totaleImportoQuoteDocumentiSpesa()));
		m.put("dt_inserimento", toString(allegatoAtto.getDataCreazione()));		
		Date dataScadenza = elaboraDataScadenza(); 
		m.put("dt_scadenza", toString(dataScadenza)); //campo non obbligatorio (quindi puo'essere correttamente null).
		m.put("note", truncateToMaxLength(allegatoAtto.getNote(),70));
		
		m.put("utente_ins", "SIAC;SIAC;"+loginOperazione); //aggiunti nuovi su AttiliqStartServlet. allegatoAtto.getLoginCreazione()
		m.put("utente_agg", "SIAC;SIAC;"+loginOperazione);  //aggiunti nuovi su AttiliqStartServlet. allegatoAtto.getLoginModifica()
		
		log.info(methodName, "returning: "+ m);
		return m;
	}
		

	private String toSN(Boolean b) {
		return Boolean.TRUE.equals(b)? "S": "N";
	}
	
	private String toString(Date d) {
		if(d==null){
			return null;
		}
		return sdf.format(d);
	}
	
	/**
	 * Tronca le stringhe che farebbero bloccare la servlet AttiliqStartServlet.
	 * Togliere questo troncamento quando tale servlet verra' aggiornata.
	 * Per preucazione viene loggato a WARN il valore inviato.
	 * @param s
	 * @param maxLength
	 * @return
	 */
	private String truncateToMaxLength(String s, int maxLength) {
		final String methodName = "truncateToMaxLength";
		if (s == null || s.length() <= maxLength) {
			return s;
		}
		log.warn(methodName, "La seguente stringa verra' troncata a "+ maxLength + ": "+s);
		return s.substring(0, maxLength);
	}
	
	
	/**
	 * 
	 * @return TRUE se esiste almeno una fattura (non di tipo: ALG, CCN)
	 */
	private Boolean esisteAlmenoUnaFattura() {
		Long count = allegatoAttoDad.countFattureNonALGoCCN(allegatoAtto);
		return  count!=null && count>0;
	}
	
	/**
	 * 
	 * @return TRUE Se almeno uno dei documenti dell'allegato ha ritenute
	 */
	private Boolean esisteAlmenoUnaRitenuta() {
		Long count = allegatoAttoDad.countDocumentiConRitenute(allegatoAtto);
		return  count!=null && count>0;
	}
	
	/**
	 * @return Indico almeno una delle quote documento dell'allegato atto ha tipoIvaSplitReverse istituzionale o commerciale
	 */
	private Boolean esisteAlmenoUnaQuotaConSplitReverse() {
		Long count = allegatoAttoDad.countQuoteConSplitReverseIstituzionaleOCommerciale(allegatoAtto);
		return  count!=null && count>0;
	}
	
	private BigDecimal totaleImportoQuoteDocumentiSpesa() {
		BigDecimal totale = BigDecimal.ZERO;
		for(ElencoDocumentiAllegato elenco : allegatoAtto.getElenchiDocumentiAllegato()){
			totale = totale.add(elenco.getTotaleQuoteSpeseNotNull());
		}
		return totale;
	}
	
	//stampa nei log i dettaglio (info) dell'allegato atto
	private void stampaDettaglioOperazione() {
		String codiceElaborazione ="INVIA_ATTO";
		StringBuilder sb = new StringBuilder();
		sb.append("Elaborazione Invio per Firma ");
		sb.append("Atto ");
		sb.append((allegatoAtto.getAttoAmministrativo() !=null && allegatoAtto.getAttoAmministrativo().getAnno() !=0) ? allegatoAtto.getAttoAmministrativo().getAnno() :" ");
		sb.append("/");
		sb.append((allegatoAtto.getAttoAmministrativo() !=null && allegatoAtto.getAttoAmministrativo().getNumero() !=0) ? allegatoAtto.getAttoAmministrativo().getNumero() :" ");
		sb.append("-");
		sb.append(allegatoAtto.getVersioneInvioFirmaNotNull());
		log.debug("stampaDettaglioOperazione", sb.toString());
		Messaggio m = new Messaggio();
		m.setCodice(codiceElaborazione);
		m.setDescrizione(sb.toString());
		res.addMessaggio(m);
	}
	
}
