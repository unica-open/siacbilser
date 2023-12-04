/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.hr.HRServiceDelegate;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.TipoGiustificativoDad;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.Sospeso;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.InsMD060Type;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceRichiestaEconomaleAnticipoSpesePerMissioneService extends InserisceRichiestaEconomaleService {
	
	//DADs
	@Autowired
	private TipoGiustificativoDad tipoGiustificativoDad;
	
	//External Services
	@Autowired
	protected HRServiceDelegate hrServiceDelegate;
	
	@Override
	protected void checkServiceParamRichiestaEconomale() throws ServiceParamError{
		
		checkSoggettoRichiestaEconomale();
		
		checkNotNull(richiestaEconomale.getDatiTrasfertaMissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dati trasferta missione"));
		
		checkCondition(StringUtils.isNotBlank(richiestaEconomale.getDatiTrasfertaMissione().getMotivo()),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("motivo trasferta"));
		checkCondition(StringUtils.isNotBlank(richiestaEconomale.getDatiTrasfertaMissione().getLuogo()),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("luogo trasferta"));
		checkCondition(richiestaEconomale.getDatiTrasfertaMissione().getFlagEstero() != null, 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("estero"));
		checkCondition(richiestaEconomale.getDatiTrasfertaMissione().getDataInizio() != null, 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data inizio trasferta"));
		checkCondition(richiestaEconomale.getDatiTrasfertaMissione().getDataFine() != null, 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data fine trasferta"));
		
//		checkCondition(richiestaEconomale.getGiustificativi()!=null && !richiestaEconomale.getGiustificativi().isEmpty(), 
//				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("giustificativi richiesta economale"));
		
		for(Giustificativo giustificativo : richiestaEconomale.getGiustificativi()){
			checkNotNull(giustificativo.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione giustificativo"));
			checkNotNull(giustificativo.getImportoGiustificativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo giustificativo"));
			checkEntita(giustificativo.getTipoGiustificativo(), "tipo giustificativo");
			checkEntita(giustificativo.getValuta(), "valuta giustificativo");
			checkNotNull(giustificativo.getFlagInclusoNelPagamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag incluso nel pagamento giustificativo"));
		}
	}

	
	@Override
	protected void init() {
		codiceTipoRichiesta = "ANTICIPO_SPESE_MISSIONE";
		super.init();
	}
	
	
	@Override
	protected void checkRichiestaEconomale() {
		checkPresenzaSoggetto();
	}
	

	@Override
	protected void preInserisceRichiestaEconomale() {
		
		caricaTipoGiustificativi();
		
		BigDecimal totaleSpettanteGiustificativi = calcolaTotaleSpettanteGiustificativi();
		
		richiestaEconomale.setImporto(totaleSpettanteGiustificativi);
		checkImpegno();
		determinaStatoOperativoGiustificativi();
		
		Integer numeroSospeso = richiestaEconomaleDad.staccaNumeroSospeso(annoBilancio, richiestaEconomale.getCassaEconomale());
		Sospeso sospeso = new Sospeso();
		sospeso.setNumeroSospeso(numeroSospeso);
		richiestaEconomale.setSospeso(sospeso);
	}
	
	@Override
	protected void determinaStato() {
		if(Boolean.TRUE.equals(datiEconomoPresenti)){
			richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.DA_RENDICONTARE);
		}else{
			richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.PRENOTATA);
		}
	}
	
	private BigDecimal calcolaTotaleSpettanteGiustificativi() {
		final String methodName = "calcolaTotaleSpettanteGiustificativi";
		BigDecimal totale = BigDecimal.ZERO;
		for(Giustificativo giustificativo : richiestaEconomale.getGiustificativi()){
			// Devo ottenere la percentuale
//			TipoGiustificativo tipoGiustificativo = tipoGiustificativoDad.ricercaDettaglioTipoGiustificativo(giustificativo.getTipoGiustificativo().getUid());
//			giustificativo.setTipoGiustificativo(tipoGiustificativo);
			totale = totale.add(giustificativo.getImportoSpettanteAnticipoMissioneNotNull());
		}
		
		log.debug(methodName, "returning: "+totale);
		return totale;
	}
	
	private void caricaTipoGiustificativi() {
		for(Giustificativo giustificativo : richiestaEconomale.getGiustificativi()){
			TipoGiustificativo tipoGiustificativo = tipoGiustificativoDad.ricercaDettaglioTipoGiustificativo(giustificativo.getTipoGiustificativo().getUid());
			giustificativo.setTipoGiustificativo(tipoGiustificativo);
		}
	}
	

	
	@Override
	protected void postInserisceRichiestaEconomale() {
		String methodName = "postInserisceRichiestaEconomale";

		if(isFromMissioneEsterna()){
			log.debug(methodName, "Invio i dati della Richiesta economale ad HR.");
			try {
				inviaDatiRichiestaEconomaleAdHR();
			} catch(Exception e) {
				log.error(methodName, "Errore nell'invio dei dati ad HR. ", e);
				throw new BusinessException("Si e' verificato un problema tecnico nella trasmissione dei dati ad HR. " +e.getMessage());
			}
		} else {
			log.debug(methodName, "Soggetto NON di HR. Non invio nessun dato ad HR.");
		}
	}

	/**
	 * Valuta se la missione provenga dall'esterno.
	 * 
	 * @return true se proviene da HR.
	 */
	private boolean isFromMissioneEsterna() {
		return StringUtils.isNotBlank(richiestaEconomale.getIdMissioneEsterna());
	}

	/** 
	 * Dopo il salvataggio della richiesta anticipo missione nel caso in cui sia stata 
	 * caricata dall'esterno occorre rimandare i dati ad HR con i giustificati aggiornati.
	 * Occorre richiamare i seguenti servizi:
	 * -  "insMD060"  che viene usato per inserire in HR gli effettivi giustificativi che l'operatore 
	 *     ha confermato sulla richiesta e il numero di sospeso (va richiamato per tutti i giustificativi 
	 *     presenti, uno alla volta)
	 * -  "updMissioni" tramite ID missione (quello che era stato letto da vm140) andiamo a 
	 *     dire ad HR quale anticipo della trasferta è stato erogato, così dopo l'aggiornamento quella 
	 *     trasferta non risulterà più presente nella lista prodotta da vm140
	 *
	 * NB!!! i due servizi di HR (insMD060 e updMissioni) NON sono in transazione!!!!!!
	 */
	private void inviaDatiRichiestaEconomaleAdHR() {
		String methodName = "inviaDatiRichiestaEconomaleAdHR";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);
		List<InsMD060Type> insMD060Types = new ArrayList<InsMD060Type>();
		
		for(Giustificativo giustificativo : richiestaEconomale.getGiustificativi()){
				InsMD060Type insMD060Type = new InsMD060Type();
				
				insMD060Type.setIdMissione(richiestaEconomale.getIdMissioneEsternaInteger()); //AHHHHHHHHH!!! nel servizio vm140 e updMissioni idMissione esterna è una Stringa!!!!!!
				try{
					Integer progressivo = Integer.parseInt(giustificativo.getNumeroGiustificativo());
					insMD060Type.setProgressivo(progressivo); //ma sarà sempre un intero!?!??!
				} catch(NumberFormatException nfe){
					throw new IllegalArgumentException("Il numero di giustificativo deve essere di tipo numerico. Valore attuale: "+giustificativo.getNumeroGiustificativo());
				}
				
				insMD060Type.setCassa(richiestaEconomale.getCassaEconomale().getCodice());//TODO arriva da FE ma va caricato da db!
				insMD060Type.setAnnoMovimento(""+richiestaEconomale.getBilancio().getAnno());//TODO arriva da FE ma va caricato da db!
				insMD060Type.setCodVoce(giustificativo.getTipoGiustificativo().getCodice());
				insMD060Type.setNrososp(richiestaEconomale.getSospeso().getNumeroSospeso());
				insMD060Type.setNumMovimento(richiestaEconomale.getMovimento().getNumeroMovimento());//??
				//insMD060Type.setImporto(giustificativo.getImportoGiustificativo().floatValue());
				// JIRA-2783 - invio importo spettante
				insMD060Type.setImporto(giustificativo.getImportoSpettanteGiustificativo().floatValue());

				insMD060Type.setQuantita(giustificativo.getQuantita());
				insMD060Type.setNote(richiestaEconomale.getNote());
				
				Date dataOperazione = richiestaEconomale.getDataCreazioneRichiestaEconomale()!=null?richiestaEconomale.getDataCreazioneRichiestaEconomale():new Date();
				
				insMD060Type.setDataImpostazioneStato(sdf.format(dataOperazione)); //data in cui viene erogato anticipo - per noi è la data operazione
				insMD060Type.setDataMissione(sdf.format(richiestaEconomale.getDatiTrasfertaMissione().getDataInizio()));
				insMD060Type.setDataMovimento(sdf.format(dataOperazione)); //data in cui viene erogato anticipo - data operazione
				
				insMD060Type.setItaEst(getItaEst(richiestaEconomale));//MASSIMO UN CARATTERE!!! Il null viene tradotto in "null" che son 4 caratteri!
				insMD060Type.setFlagTotalizzatore("S"); //impostare per default a a S.  MASSIMO UN CARATTERE!!! Il null viene tradotto in "null" che son 4 caratteri!
				insMD060Type.setStato("S"); //impostare sempre a S.
				
				insMD060Types.add(insMD060Type); //Li accumulo qui per ridurre i problemi di non transazionalita'.
		}
		
		
		
		//###################### SEZIONE CRITICA NON IN Transazione! ###### INIZIO ###################
		//##### NB: Non aggiungere logiche che possono fallire in questa sezione!                    #
		//##### Qualsiasi logica di controllo e di popolamento dei dati da inviare va fatta prima    #
		//##### in modo da ridurre i casi in cui, arrivati a questo punto, si possa fallire.         #
		//############################################################################################
		
		log.info(methodName, "Popolamento dei dati terminato per IdMissioneEsterna: "+richiestaEconomale.getIdMissioneEsterna()+". Numero di giustificativi da inviare: "+insMD060Types.size());
		for(InsMD060Type insMD060Type : insMD060Types) {
			Utility.logXmlTypeObject(insMD060Type, "Invio il giustificativo ad HR (insMD60). Progressivo:"+insMD060Type.getProgressivo());
			hrServiceDelegate.insMD060(insMD060Type);
			log.info(methodName, "Inviato correttamente il giustificativo ad HR (insMD60). Progressivo:"+insMD060Type.getProgressivo());
		}
		
		log.debug(methodName, "Aggiorno lo stato della richiesta anticipo spese per missione su HR (updMissioni): " +richiestaEconomale.getIdMissioneEsterna());
		hrServiceDelegate.updMissioni(richiestaEconomale.getIdMissioneEsterna());
		log.info(methodName, "Aggiornato correttamente lo stato della richiesta anticipo spese per missione su HR (updMissioni): " +richiestaEconomale.getIdMissioneEsterna());
		
		//###################### SEZIONE CRITICA NON IN Transazione! ###### FINE ##################
	}


	/**
	 * Restituisce: R (Regione), I (Italia), E (Estero)  (noi abbiamo estero si/no) - però è un dato che arriva da HR dalla vm140 (flag_destinazione) 

	 * @param richiestaEconomale
	 * @return
	 */
	private String getItaEst(RichiestaEconomale richiestaEconomale) {
		String methodName = "getItaEst";
		
		Boolean flagEstero = richiestaEconomale.getDatiTrasfertaMissione().getFlagEstero();
		if(Boolean.TRUE.equals(flagEstero)){
			log.debug(methodName, "Returning E - Estero. [richiestaEconomale.uid:" +richiestaEconomale.getUid()+"]");
			return "E"; //Estero.
		}
		
		if(StringUtils.isNotBlank(richiestaEconomale.getNote())) { //Quando e' per Regione nelle note c'e' scritto Regione! (facepalm)
			Pattern p = Pattern.compile(".*[Rr][Ee][Gg][Ii][Oo][Nn][Ee].*", Pattern.DOTALL); //Ho degli \n nelle note
	        Matcher m = p.matcher(richiestaEconomale.getNote());
	        
	        if(m.matches()){
	        	log.debug(methodName, "Returning R - Regione. [richiestaEconomale.uid:" +richiestaEconomale.getUid()+"]");
				return "R"; //Regione.
			}
		}
		
		log.debug(methodName, "Returning I - Italia. [richiestaEconomale.uid:" +richiestaEconomale.getUid()+"]");
		return "I"; //Italia.
	}
	
	
}
