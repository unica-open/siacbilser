/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.liquidazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.business.service.enumeration.CodiceEventoEnum;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneModulare;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneModulareResponse;
import it.csi.siac.siacfinser.integration.dad.LiquidazioneDad;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaLiquidazioneModulareService extends AbstractBaseService<AggiornaLiquidazioneModulare, AggiornaLiquidazioneModulareResponse> {

	@Autowired
	LiquidazioneDad liquidazioneDad;
	
	@Autowired
	SoggettoFinDad soggettoDad;
	
	@Override
	protected void init() {
		final String methodName="AggiornaLiquidazioneModulareService : init()";
		log.debug(methodName, " - Begin");
		
	}	

	
	@Override
	@Transactional
	public AggiornaLiquidazioneModulareResponse executeService(AggiornaLiquidazioneModulare serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		String methodName = "AggiornaLiquidazioneModulareService - execute()";
		log.debug(methodName, " - Begin");
			
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		setBilancio(req.getBilancio());
		
		Liquidazione liquidazione = req.getLiquidazione();
		
		Liquidazione liquidazioneAggiornata = new Liquidazione();
		
		//recupero lo stato che sto per andare ad aggiornare cosi da poter effettuare i controlli per la generale
		String liqCodiceStatoPreModifica = liquidazioneDad.getStatoCode(liquidazione.getUid());
		String liqCodiceStatoInModifica = Constanti.statoOperativoLiquidazioneEnumToString(liquidazione.getStatoOperativoLiquidazione());
		
		// Commento il controllo per richiamare l'aggiornaLiquidazioneModulare in modo da non essere vincolati al cambio stato
		// anche perchè nel metodo aggiornaLiquidazioneModulare controllo già cosa fare in base a cosa arriva
		//if(req.isFlagAggiornaStato() && !liqCodiceStatoPreModifica.equalsIgnoreCase(liqCodiceStatoInModifica))
		liquidazioneAggiornata = liquidazioneDad.aggiornaLiquidazioneModulare(
					liquidazione, ente.getUid(), 
					bilancio, richiedente, 
					req.isFlagAggiornaStato(), 
					req.isFlagAggiornaFlagManuale(), 
					req.isFlagAggiornaContoTesoreria(), 
					req.isFlagAggiornaModalitaPagamento());
		
		
		// Gestione innesto!
		if(liquidazione.getStatoOperativoLiquidazione()!=null && req.isFlagAggiornaStato()){
			
			// sono in aggiorna liquidazione e varia lo stato da PROVVISORIO a VALIDO, REGISTRO con LIQ-INS
			if(liqCodiceStatoPreModifica.equals(Constanti.LIQUIDAZIONE_STATO_PROVVISORIO) && 
					Constanti.statoOperativoLiquidazioneEnumToString(liquidazioneAggiornata.getStatoOperativoLiquidazione()).equals(Constanti.LIQUIDAZIONE_STATO_VALIDO)){
				
				if(liquidazione.getSubdocumentoSpesa()!=null)
					liquidazioneAggiornata.setSubdocumentoSpesa(liquidazione.getSubdocumentoSpesa());
				
				// Qui devo leggere l'impegno! non mi arriva piu dal chiamante 
				liquidazioneAggiornata.setUid(liquidazione.getUid());				
								
				// Innesto registrazione fin -gen:  il codice evento è LIQ-INS
				// SIAC-4066 (cr 297 - Gestione delle prima note a fronte di liquidazioni rilevate nell'esercizio le cui quote finanziarie sono su impegni residui)
				boolean registraPerImpegnoResiduo = liquidazioneAggiornata.getImpegno().getAnnoMovimento() < req.getBilancio().getAnno();
				String codiceEvento = "";				
				
				if(registraPerImpegnoResiduo)
					codiceEvento =  CodiceEventoEnum.INSERISCI_LIQUIDAZIONE_CON_IMPEGNO_RESIDUO.getCodice();
				else
					codiceEvento =  CodiceEventoEnum.INSERISCI_LIQUIDAZIONE.getCodice();
				
				gestisciRegistrazioneGENPerLiquidazione(liquidazioneAggiornata, codiceEvento);	
				
			}
			
			
			// sono in aggiorna liquidazione e varia lo stato da VALIDO a PROVVISORIO, annulla la registrazione della liquidazione 
			if(liqCodiceStatoPreModifica.equals(Constanti.LIQUIDAZIONE_STATO_VALIDO) && 
					Constanti.statoOperativoLiquidazioneEnumToString(liquidazioneAggiornata.getStatoOperativoLiquidazione()).equals(Constanti.LIQUIDAZIONE_STATO_PROVVISORIO)){
				
				annullaRegistrazioneEPrimaNotaLiquidazione(TipoCollegamento.LIQUIDAZIONE, liquidazioneAggiornata);
			}
		}
		
		liquidazioneDad.flushAndClear();
			
		res.setLiquidazione(liquidazioneAggiornata);
		res.setEsito(Esito.SUCCESSO);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="AggiornaLiquidazioneModulareService : checkServiceParam()";
		
		log.debug(methodName, " - Begin");
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		
		checkNotNull(req.getLiquidazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("liquidazione"));
		
		log.debug(methodName, " - End");
		
	}	
}
