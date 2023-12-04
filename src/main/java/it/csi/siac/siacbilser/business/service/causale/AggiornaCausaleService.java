/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.causale;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Servizio di aggiornamento di una CausaleEP
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCausaleService extends CrudCausaleBaseService<AggiornaCausale, AggiornaCausaleResponse> {
	
	/*
	 * 
	 * Per il salvataggio effettuare i controlli descritti al par.  2.10.1. 
	 * 
	 * 
	 * 2.10.1	Algoritmo di salvataggio della causale
	 *       I controlli indicati si effettuano per l’operazione di inserimento e per quella di aggiornamento.
	 *       
	 *       Il sistema effettuerà i seguenti controlli:
	 *          	Il codice di una Causale deve  essere univoco, se si prova ad inserire un codice Causale già esistente (anche nel caso appartenga ad una causale in stato ANNULLATO) il sistema deve visualizzare il messaggio di errore <COR_ERR_0008, Entità già presente, ‘Causale’, ‘Codice della causale che si sta inserendo’>.
	 *       
	 *       Per le le causali DI RACCORDO effettuare  i seguenti controlli.
	 *          	Ci siano almeno due conti, uno con TipoOperazione-segnoConto=DARE e uno con TipoOperazione-segnoConto=AVERE; se non è così il sistema presenta il seguente messaggio < GEN_ERR_0007, Assenza Conti obbligatori causali di raccordo.>.
	 *          	Ci sia almeno un conto collegato allo stesso V livello del Piano dei Conti Finanziario della causale, se non è così il sistema presenta il seguente messaggio <COR_ERR_043 Entità non completa (<nome entità>: “La causale“,   <motivo> :  “deve essere presente un conto collegato ad conto finanziario di  V livello”.>.
	 *          	Se la causale  è associata ad un Soggetto deve essere presente un Conto collegato allo stesso Soggetto; se non è così il sistema presenta il seguente messaggio <COR_ERR_043 Entità non completa (<nome entità>: “La causale“,   <motivo> :  “deve essere presente un conto collegato al soggetto della causale”.>.
	 *       
	 *       Si aggiungono poi condizioni specifiche di una tipologia di eventi.
	 *       Per TipoEvento = EP – Scrittura epilogativa
	 *          	Deve essere presente almeno un conto appartenente alla ClasseConto = EP – Conti epilogativi. In caso non sia rispettato il controllo inviare il messaggio <COR_ERR_043 Entità non completa (<nome entità>: “La causale“,  <motivo> :  “deve essere presente un conto epilogativo” )>. 
	 *       
	 *
	 * 
	 * 
	 * Se la verifica ha esito positivo si opera l’aggiornamento come descritto di seguito. Per una migliore comprensione si specifica che deve esistere una sola istanza per anno, per cui per la prima istanza della vita di una causale è ammesso aggiornare tutti i dati, per le successive si opera in aggiornamento delle relazioni per le quali è gestita la storicizzazione.
	 * 
	 * •	Se data inizio validità  della causale  =  anno corrente :  si procede con aggiornamento dei dati dell’intera causale e sue relazioni mantenendo le date di inizio validità di tutte le istanze e relazioni
	 * •	Se data inizio validità della causale  <  anno corrente : sono possibili aggiornamenti delle sole relazioni (e solo di quelle previste dalla maschera) . Operare in questo caso discriminando le due casistiche.
	 * 			Primo aggiornamento dell’anno di bilancio (data inizio validità della relazione < anno corrente) :  inserire una nuova istanza delle relazioni interessate dalle modifiche con data inizio validità = 01/01/ anno corrente e chiudere la precedente con data fine validità = 31/12/anno precedente
	 * 			Successivo aggiornamento (data inizio validità della relazione = anno corrente) :  aggiornare  i dati dell’istanza delle relazioni interessate.
	 * 
	 * Il servizio deve:
	 * -	salvare i dati modificati del piano (entità CausaleEP e sue relazioni) 
	 * -	impostare la data di aggiornamento con la data corrente per l’entità e relazioni interessate dall’aggiornamento.
	 * 
	 * 
	 * 
	 */
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCausaleEP(),"causaleEP");
		this.causaleEP = req.getCausaleEP();
		this.causaleEP.setEnte(ente);
		
		checkEntita(req.getBilancio(), "bilancio", false);
		this.bilancio = req.getBilancio();
		
		checkNotBlank(causaleEP.getCodice(), "codice causaleEP", false);
		checkNotBlank(causaleEP.getDescrizione(), "descrizione causaleEP", false);
		
		checkNotNull(causaleEP.getTipoCausale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo causale"), false);
		
		checkNotNull(causaleEP.getEventi(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("eventi"));
		checkCondition(!causaleEP.getEventi().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("eventi"));
		
		for(Evento evento: causaleEP.getEventi()){
			checkEntita(evento, "evento conto", false);
		}
		
//		checkNotNull(causaleEP.getContiTipoOperazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conti tipo operazione"));
//		checkCondition(!causaleEP.getContiTipoOperazione().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conti tipo operazione"));
		
		if(causaleEP.getContiTipoOperazione()!=null){
			for(ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()){
				if(cto.getClasseDiConciliazione() == null || !Ambito.AMBITO_GSA.equals(causaleEP.getAmbito())){
					checkEntita(cto.getConto(), "conto",false);
				}
				checkNotNull(cto.getOperazioneSegnoConto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("operazione segno conto conto"),false);
				checkNotNull(cto.getOperazioneTipoImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("operazione tipo importo conto"),false);
				if(TipoCausale.Integrata.equals(causaleEP.getTipoCausale())){
					checkNotNull(cto.getOperazioneUtilizzoConto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("operazione utilizzo conto conto"),false);
					checkNotNull(cto.getOperazioneUtilizzoImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("operazione utilizzo importo conto"),false);
				}
				
			}
		}
		
		if(causaleEP.getCausaleDiDefault() == null){
			causaleEP.setCausaleDiDefault(Boolean.FALSE);
		}
		
	}
	
	@Override
	@Transactional
	public AggiornaCausaleResponse executeService(AggiornaCausale serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		
		caricaBilancio();
		checkFaseBilancio();
		
		checkContiConciliazione();
		checkCodiceCausale();
		checkCausaleEPIntegrata();
		checkCausaleEPConEventoEP();
		checkUguaglianzaContiCausale();
		
		checkContiCoerentiConElementoPianoDeiConti();
		
		
		
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		causaleEP.setDataInizioValidita(inizioAnno);
		
		causaleEPDad.aggiornaCausaleEP(causaleEP);
		
		res.setCausaleEP(causaleEP);
		
	}
	
	/**
	 * Controlla se il codice causale e' già sato utilizzato
	 */
	private void checkCodiceCausale() {
		CausaleEP causaleEPTrovata = causaleEPDad.ricercaCausaleEPByCodice(causaleEP);
		if(causaleEPTrovata != null  && causaleEPTrovata.getUid() != causaleEP.getUid()) {
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("CausaleEP", "il codice \""+ causaleEPTrovata.getCodice()+ "\" "));
		}
		
	}
	
	/**
	 * Controlla se esiste gia' una causale con gli stessi conti ep e con lo stesso piano dei conti.
	 */
	private void checkUguaglianzaContiCausale() {
		String methodName = "checkUguaglianzaContiCausale";
		if(!TipoCausale.Integrata.equals(causaleEP.getTipoCausale())){
			log.debug(methodName, "tipoCausale diverso da Integrata. Salto il check.");
			return;
		}
		if(Ambito.AMBITO_GSA.equals(causaleEP.getAmbito())){
			log.debug(methodName, "tipoCausale con Ambito GSA. Salto il check.");
			return;
		}
		List<CausaleEP> causali = causaleEPDad.findCausaleEpIdByConti(causaleEP);
		if(causali.size()>1 || (causali.size() == 1 && causali.get(0).getUid() != causaleEP.getUid())){
			throw new BusinessException(ErroreGEN.OPERAZIONE_NON_CONSENTITA_0016.getErrore(), Esito.FALLIMENTO);
		}
		
	}
	

}
