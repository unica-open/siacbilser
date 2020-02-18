/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Gestisce la convalida di uno o più elenchi o parte di essi.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConvalidaAllegatoAttoPerElenchiService extends ConvalidaAllegatoAttoBaseService<ConvalidaAllegatoAttoPerElenchi,ConvalidaAllegatoAttoPerElenchiResponse> {

	private AllegatoAtto allegatoAtto;
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//this.allegatoAtto = req.getAllegatoAtto(); NON devo distruggere la request!
		
		checkNotNull(req.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
		checkCondition(req.getAllegatoAtto().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"), false);
		
		checkNotNull(req.getAllegatoAtto().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente allegato atto"));
		checkCondition(req.getAllegatoAtto().getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente allegato atto"), false);
		this.ente = req.getAllegatoAtto().getEnte();
		
		checkNotNull(req.getAllegatoAtto().getElenchiDocumentiAllegato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti allegato allegato atto"));
		checkCondition(!req.getAllegatoAtto().getElenchiDocumentiAllegato().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti allegato allegato atto"));
		
		for(ElencoDocumentiAllegato elenco : req.getAllegatoAtto().getElenchiDocumentiAllegato()){
			checkNotNull(elenco, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti allegato allegato atto"));
			checkCondition(elenco.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid elenco documenti allegato allegato atto"));
			
			boolean elencoSubdocValorizzato = elenco.getSubdocumenti() != null && !elenco.getSubdocumenti().isEmpty();
			checkCondition(elencoSubdocValorizzato ^ req.getFlagConvalidaManuale()!=null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumenti oppure flagConvalidaManuale"));
			
			if(elencoSubdocValorizzato){
				for(Subdocumento<?, ?> subdoc: elenco.getSubdocumenti()){
					checkNotNull(subdoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento subdocumenti elenco documenti allegato allegato atto"));
					checkCondition(subdoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento subdocumenti elenco documenti allegato allegato atto"));
					checkNotNull(subdoc.getFlagConvalidaManuale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag convalida manuale subdocumento subdocumenti elenco documenti allegato allegato atto"));
				}
			}
		}
		
//		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
//		checkCondition(req.getBilancio().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"), false);
//		this.bilancio = req.getBilancio();
		
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public ConvalidaAllegatoAttoPerElenchiResponse executeServiceTxRequiresNew(ConvalidaAllegatoAttoPerElenchi serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional
	public ConvalidaAllegatoAttoPerElenchiResponse executeService(ConvalidaAllegatoAttoPerElenchi serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		String methodName = "execute";
		//SIAC-5584: imposto l'allegato atto nella response (metto quello della request perche' mi basta avere i dati minimi, non necessariamente quelli totali)
		res.setAllegatoAtto(req.getAllegatoAtto());
		this.allegatoAtto = caricaAllegatoAtto(req.getAllegatoAtto().getUid());
		//SIAC-6261
		checkDatiDurc();
		stampaDettaglioOperazione();
		checkStatoOperativoAllegatoAttoCompletato(allegatoAtto);
			
		//Per ogni elenco passato...
		for(ElencoDocumentiAllegato elencoDocumentiAllegatoReq : req.getAllegatoAtto().getElenchiDocumentiAllegato()){
			ElencoDocumentiAllegato elencoDocumentiAllegato = caricaElencoDocumentiAllegato(elencoDocumentiAllegatoReq.getUid());
			
			try{
				checkStatoElenco(elencoDocumentiAllegato);
				checkPresenzaSubordinati(elencoDocumentiAllegato, elencoDocumentiAllegatoReq);
			} catch (BusinessException be){
				log.info(methodName, "elencoDocumentiAllegato scartato! uid elenco:"+ elencoDocumentiAllegato.getUid() 
						+ ". Errore: "+ (be.getErrore()!=null? be.getErrore().getTesto():"null"));
				res.addErrore(be.getErrore());
				res.getElenchiScartati().add(elencoDocumentiAllegato);
				continue; //L'elenco viene scartato. Si continua con il prossimo.
			}
			
			List<Subdocumento<?, ?>> subdocumenti = getSubdocumentiDallaRequestOppureTutti(elencoDocumentiAllegato, elencoDocumentiAllegatoReq);
			
			//Per tutti i subdocumenti in elenco oppure per tutti quelli passati al servizio...
			for(Subdocumento<?, ?> subdocReq : subdocumenti){
				Subdocumento<?, ?> subdoc = caricaSubdocumentoConDatiDiBase(subdocReq); //TODO potrei caricarlo solo se arrivava dalla request!
				
				try{
					checkTipoConvalida(subdoc);
					checkSoggettoSospeso(req.getAllegatoAtto(), subdoc);
				} catch (BusinessException be){
					log.info(methodName, "subdocumento [uid:"+subdoc.getUid()+"] scartato! uid elenco:"+ elencoDocumentiAllegato.getUid() 
							+ ". Errore: "+ (be.getErrore()!=null? be.getErrore().getTesto():"null"));
					res.addErrore(be.getErrore());
					res.getSubdocumentiScartati().add(subdoc);
					continue; //Il subdocumento viene scartato. Si continua con il prossimo.
				}
				
				
				if(subdocReq.getFlagConvalidaManuale()==null){
					subdocReq.setFlagConvalidaManuale(getDefaultFlagConvalidaManuale());
				}
				
				aggiornaTipoConvalida(subdocReq);
				if(subdocReq instanceof SubdocumentoSpesa){
					((SubdocumentoSpesa)subdocReq).setDocumento(((SubdocumentoSpesa)subdoc).getDocumento());
					aggiornaLiquidazioneModulare((SubdocumentoSpesa)subdocReq);
				}
				
				res.incCountQuoteConvalidate();
				
				//SIAC-4752 - Aggiunto messaggio di dettaglio
				res.addMessaggio("CONV_INFO_QUOTA", "Convalidata quota numero "
						+ subdoc.getNumero() + (subdoc.getDocumento()!=null?" del documento "+ subdoc.getDocumento().getDescAnnoNumeroTipoDoc():"")
						+ " appartenente all'elenco "+ elencoDocumentiAllegato.getAnno()+"/"+ elencoDocumentiAllegato.getNumero() + " [uid: "+elencoDocumentiAllegato.getUid()+"]"
					);
			}
		}
		
		aggiornaStatoOperativoAllegatoAtto(allegatoAtto);
		
		//SIAC-4752 - Aggiunto messaggio di fine elaborazione (TODO migliorabile)
		res.addMessaggio("CONV_INFO_RIEP", "Totale elenchi elaborati: " + req.getAllegatoAtto().getElenchiDocumentiAllegato().size()
				+ ", di cui scartati: " + res.getElenchiScartati().size()
				+ ", totale quote in elenco convalidate: " + res.getCountQuoteConvalidate()
			);

	}
	
	protected Boolean getDefaultFlagConvalidaManuale() {
		final String methodName = "getDefaultFlagConvalidaManuale";
		String gca = ente.getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_CONVALIDA_AUTOMATICA);
		if("CONVALIDA_AUTOMATICA".equals(gca)){
			log.debug(methodName, TipologiaGestioneLivelli.GESTIONE_CONVALIDA_AUTOMATICA.name()+ " impostata a CONVALIDA_AUTOMATICA");
			return Boolean.FALSE;
		} else if("CONVALIDA_MANUALE".equals(gca)){
			log.debug(methodName, TipologiaGestioneLivelli.GESTIONE_CONVALIDA_AUTOMATICA.name()+ " impostata a CONVALIDA_MANUALE");
			return Boolean.TRUE;
		}
		
		return Boolean.TRUE;
//		throw new BusinessException("Tipologia Gestione Livello "+TipologiaGestioneLivelli.GESTIONE_CONVALIDA_AUTOMATICA.name() +" non configurato per l'ente (uid: "+ente.getUid()+"). "
//				+ "Occorre modificare la configurazione dell'ente.");
	}

	
	/**
	 * Ottiene l'elenco dei subdocumenti dell'allegato atto che vanno processati.
	 * L'elenco viene preso o dalla request se valorizzato, oppure dall'elenco completo dei subdocumenti dell'elenco
	 *
	 * @param elencoDocumentiAllegato elenco completo dei subdocumenti sulla base dati
	 * @param elencoDocumentiAllegatoReq elenco dei subdocumenti in request
	 * @return the subdocumenti dalla request oppure tutti
	 */
	private List<Subdocumento<?, ?>> getSubdocumentiDallaRequestOppureTutti(ElencoDocumentiAllegato elencoDocumentiAllegato,
			ElencoDocumentiAllegato elencoDocumentiAllegatoReq) {
		List<Subdocumento<?, ?>> subdocumenti; 
		if(elencoDocumentiAllegatoReq.getSubdocumenti()!=null && !elencoDocumentiAllegatoReq.getSubdocumenti().isEmpty()){
			//Sono stati passati dei subdocumenti dalla request
			subdocumenti = elencoDocumentiAllegatoReq.getSubdocumenti();
		} else {
			//NON sono stati passati dei subdocumenti dalla request: Prendo tutti quelli sulla base dati.
			subdocumenti = elencoDocumentiAllegato.getSubdocumenti();
			impostaFlagConvalidaManuale(subdocumenti);
		}
		return subdocumenti;
	}


	private void checkPresenzaSubordinati(ElencoDocumentiAllegato elencoDocumentiAllegato, ElencoDocumentiAllegato elencoDocumentiAllegatoReq) {
		if(elencoDocumentiAllegatoReq.getSubdocumenti() != null && !elencoDocumentiAllegatoReq.getSubdocumenti().isEmpty()){
			checkPresenzaSubordinati(elencoDocumentiAllegatoReq);
		} else {
			checkPresenzaSubordinati(elencoDocumentiAllegato);
		}
		
	}
	
	//stampa nei log i dettaglio (info) dell'allegato atto
	private void stampaDettaglioOperazione() {
		StringBuilder sb = new StringBuilder();
		sb.append("Elaborazione Convalida per ");
		sb.append("Atto ");
		sb.append((allegatoAtto.getAttoAmministrativo() !=null && allegatoAtto.getAttoAmministrativo().getAnno() !=0) ? allegatoAtto.getAttoAmministrativo().getAnno() :" ");
		sb.append("/");
		sb.append((allegatoAtto.getAttoAmministrativo() !=null && allegatoAtto.getAttoAmministrativo().getNumero() !=0) ? allegatoAtto.getAttoAmministrativo().getNumero() :" ");
		log.debug("stampaDettaglioOperazione", sb.toString());

	}
	
	/**
	 * non potrà essere inserita la liquidazione di soggetto con DURC scaduto 
	 * “DURC scaduto soggetto XXXX, operazione di completa Atto bloccata” oppure “DURC scaduto soggetto XXXX ricevente incasso del soggetto YYYYY, operazione di completa Atto bloccata” 
	 * In questo caso il completamento non va a buon fine.
	 */
	private void checkDatiDurc() {
		List<Integer> uidsSubdocConConfermaDurc = allegatoAttoDad.getUidsSubdocWithImpegnoConfermaDurc(allegatoAtto);
		if(uidsSubdocConConfermaDurc == null || uidsSubdocConConfermaDurc.isEmpty()) {
			return;
		}
		Map<String, Date> mappaSoggettoData = subdocumentoSpesaDad.getDataFineValiditaDurcAndSoggettoCodePiuRecenteBySubdocIds(uidsSubdocConConfermaDurc);
		Date now = new Date();
		
		//SIAC-7143
		String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(now);
		String dateFineDurc = null;
		
		for (String soggettoCode : mappaSoggettoData.keySet()) {
			Date dataFineValiditaDurc = mappaSoggettoData.get(soggettoCode);			
			//SIAC-7143
			dateFineDurc = dataFineValiditaDurc != null? new SimpleDateFormat("yyyy-MM-dd").format(dataFineValiditaDurc) : null;
			if(dateFineDurc == null  || dateNow.compareTo(dateFineDurc) > 0){
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il soggetto " + soggettoCode + " presenta dati Durc non validi."));
			}
		}
	}

}
