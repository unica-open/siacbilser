/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.stampa.base.AsyncReportBaseService;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.AzioneDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAttoResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.OperazioneAsincronaService;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciOperazioneAsinc;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciOperazioneAsincResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.TipoStampaAllegatoAtto;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaAllegatoAttoAsyncService extends AsyncReportBaseService<StampaAllegatoAtto, StampaAllegatoAttoResponse, StampaAllegatoAttoReportHandler> {
	
	@Autowired
	protected OperazioneAsincronaService operazioneAsincronaService;
	
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	@Autowired
	private AzioneDad azioneDad;
	
	private AllegatoAtto allegatoAtto;

	private Integer idOperazioneAsincrona;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkCondition(req.getEnte() == null || req.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkCondition(req.getBilancio() == null || req.getBilancio().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"), false);
		
		checkNotNull(req.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"), false);
		checkCondition(req.getAllegatoAtto() == null || req.getAllegatoAtto().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"), false);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public StampaAllegatoAttoResponse executeService(StampaAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		allegatoAttoDad.setEnte(req.getEnte());
	}

	@Override
	protected void initReportHandler() {
		reportHandler.setAllegatoAtto(allegatoAtto);
		reportHandler.setEnte(req.getEnte());
		reportHandler.setRichiedente(req.getRichiedente());
		reportHandler.setBilancio(req.getBilancio());
		reportHandler.setTipoStampa(TipoStampaAllegatoAtto.ALLEGATO);
		reportHandler.setAnnoEsercizio(req.getAnnoEsercizio());
		reportHandler.setIdOperazioneAsincrona(idOperazioneAsincrona);
	}
	
	@Override
	protected void preStartElaboration() {
		caricaDettaglioAllegatoAtto();
		//aggiungere chiamata al inserisci dettaglio operazione asincrona -> ottieni un idOperazioneasincrona -> settalo nel reportHandler
		//SIAC-6433
		inserisciOperazioneAsinc();
		stampaDettaglioOperazione();
		checkAlmenoUnaQuotaSpesaPresente();
	}

	//SIAC-6433
	private InserisciOperazioneAsincResponse inserisciOperazioneAsinc(){		
		InserisciOperazioneAsinc reqIOS = new InserisciOperazioneAsinc();		
		reqIOS.setAccount(req.getRichiedente().getAccount());
		reqIOS.setDataOra(new Date());
		reqIOS.setEnte(req.getEnte());
		reqIOS.setRichiedente(req.getRichiedente());
		reqIOS.setAzioneRichiesta(creaAzioneRichiesta());
		InserisciOperazioneAsincResponse resAgg = operazioneAsincronaService.inserisciOperazioneAsinc(reqIOS);
		//SIAC-6433
		//log.info("InserisciOperazioneAsincResponse", "*********************************");
		//log.info("InserisciOperazioneAsincResponse", "resAgg.getIdOperazione() " + resAgg.getIdOperazione());
		
		this.idOperazioneAsincrona = resAgg.getIdOperazione();
		
		//aggiornaOperazioneAsinc(StatoOperazioneAsincronaEnum.STATO_OPASINC_AVVIATA);
		checkServiceResponseFallimento(resAgg);
		return resAgg;
	}
	
	private AzioneRichiesta creaAzioneRichiesta() {
		final String methodName = "creaAzioneRichiesta";
		AzioneRichiesta ar = new AzioneRichiesta();
		
		// TODO: qui solo per sicurezza
		azioneDad.setEnte(req.getEnte());
		String nomeAzione = AzioniConsentite.ALLEGATO_ATTO_STAMPA.getNomeAzione();
		log.debug(methodName, "Caricamento azione per nome " + nomeAzione);
		Azione azione = azioneDad.getAzioneByNome(nomeAzione);
		log.debug(methodName, "Azione caricata per nome " + nomeAzione + ": " + (azione != null ? azione.getUid() : "null"));
		
		if(azione == null) {
			throw new BusinessException("Nessuna azione configurata con nome " + nomeAzione);
		}
		
		ar.setAzione(azione);
		
		return ar;
	}

	//stampa nei log i dettaglio (info) dell'allegato atto
	private void stampaDettaglioOperazione() {
		StringBuilder sb = new StringBuilder();
		sb.append("Elaborazione Stampa Allegato per ");
		sb.append("Atto ");
		sb.append((allegatoAtto.getAttoAmministrativo() !=null && allegatoAtto.getAttoAmministrativo().getAnno() !=0) ? allegatoAtto.getAttoAmministrativo().getAnno() :" ");
		sb.append("/");
		sb.append((allegatoAtto.getAttoAmministrativo() !=null && allegatoAtto.getAttoAmministrativo().getNumero() !=0) ? allegatoAtto.getAttoAmministrativo().getNumero() :" ");
		sb.append("-");
		sb.append(allegatoAtto.getVersioneInvioFirmaNotNull());
		log.debug("stampaDettaglioOperazione", sb.toString());

	}

	private void caricaDettaglioAllegatoAtto() {
		AllegatoAtto temp = allegatoAttoDad.findAllegatoAttoById(req.getAllegatoAtto().getUid());
		if(temp == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("allegato atto", "uid " + req.getAllegatoAtto().getUid()));
		}
		allegatoAtto = temp;
	}

	// CR-2996: STAMPA e INVIO a FLUX: solo per Atti che abbiano almeno una spesa collegata 
	private void checkAlmenoUnaQuotaSpesaPresente() {
		final String methodName = "checkAlmenoUnaQuotaSpesaPresente";
		Boolean hasQuoteSpesa = Boolean.valueOf(allegatoAttoDad.countQuoteSpesaAssociateAdAllegato(allegatoAtto) >= 1);
		if (!hasQuoteSpesa) {
			log.debug(methodName, "L'allegato [uid:" + allegatoAtto.getUid() + "] non risulta collegato a nessuna quota spesa. ");
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("stampa", "allegato atto non collegato a nessuna quota spesa"));
		}
	}

	@Override
	protected void postStartElaboration() {
		final String methodName = "postStartElaboration";
		log.info(methodName, "post start elaborazione!");
	}

}
